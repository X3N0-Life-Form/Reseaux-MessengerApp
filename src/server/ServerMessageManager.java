package server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import common.Message;
import common.MessageInfoStrings;
import common.MessageType;
import common.handling.Handler;
import common.handling.HandlingException;
import common.logging.EventType;
import common.logging.Log;

/**
 * This class determines the server's behavior upon receiving a Message from a client 
 * and prepares an appropriate answer.
 * <br />Note that the actual reception and response are delegated to the Handler objects.
 * @author etudiant
 * @see Handler
 * @see UDPHandlerServer
 * @see TCPHandlerServer
 */
public class ServerMessageManager {
	
	private Server server;
	private Handler handler;
	private Map<String, InetAddress> clientIps;
	private Map<String, String> clientPorts;
	private Log log;
	
	/**
	 * Constructs a message manager for the specified server and handler.
	 * @param server
	 * @param handler
	 */
	public ServerMessageManager(Server server, Handler handler) {
		this.server = server;
		this.handler = handler;
		clientIps = server.getClientIps();
		clientPorts = server.getClientPorts();
		log = server.getLog();
	}
	
	/**
	 * Deals with Messages received through a regular Socket.
	 * @param message - Message that needs to be handled.
	 * @param socket
	 * @throws IOException
	 * @throws HandlingException If this Manager's Handler is unable to deal with Sockets.
	 */
	public void handleMessage(Message message, Socket socket) throws IOException, HandlingException {
		InetAddress ip = socket.getInetAddress();
		String login = message.getInfo(MessageInfoStrings.LOGIN);
		switch (message.getType()) {
		
		
		case CONNECT:
			if (clientIps.containsKey(login)) {
				Message errorMsg = new Message(
						MessageType.ERROR,
						"Error: client already connected.");
				handler.sendMessage(errorMsg, socket);
				//log.log(EventType.ERROR, "Rejected client authentification: " + login
				//		+ " is already connected.");
				break;
			}
			String pass = message.getInfo(MessageInfoStrings.PASSWORD);
			if (server.authenticateClient(login, pass)) {
				clientIps.put(login, ip);
				clientPorts.put(login, socket.getPort() + "");
				server.getTimeoutHandler().addClient(login);
				Message okMsg = new Message(
						MessageType.OK,
						"Able to authenticate client.");
				handler.sendMessage(okMsg, socket);
				//log.log(EventType.INFO, "Client authenticated: " + login);
			} else {
				Message errorMsg = new Message(
						MessageType.ERROR,
						"Unable to authenticate client: wrong login or password.");
				handler.sendMessage(errorMsg, socket);
				//log.log(EventType.ERROR, "Rejected client authentification: " + login);
			}
			break;
			
		case DISCONNECT:
			clientIps.remove(login);
			clientPorts.remove(login);
			server.getTimeoutHandler().removeClient(login);
			break;
		default:
			throw new HandlingException("Message type " + message.getType() + " not handled by " + handler.getClass());
		}
	}

	/**
	 * Deals with Messages received through a DatagramSocket.
	 * @param message - Message that needs to be handled.
	 * @param socket
	 * @param paquet
	 * @throws HandlingException If this Manager's Handler is unable to deal with DatagramSockets.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void handleMessage(Message message, DatagramSocket socket, DatagramPacket paquet) throws HandlingException, IOException, ClassNotFoundException {
		String login = message.getInfo(MessageInfoStrings.LOGIN);
		server.getTimeoutHandler().updateClient(login, new Date());
		switch (message.getType()) {
		case REQUEST_LIST:
			if (clientIps.containsKey(login)) {
				
				Message clientListMsg = new Message(MessageType.CLIENT_LIST);
				List<String> clientLogins = getClientLogins();
				//clientListMsg.addObject("clientIps", clientIps);
				clientListMsg.addObject(MessageInfoStrings.REQUEST_LIST_CLIENT_LOGINS, clientLogins);
				//System.out.println("SENDER PORT :::: " + message.getInfo("port"));
				clientListMsg.addInfo("senderPort", message.getInfo(MessageInfoStrings.PORT));
				
				clientPorts.put(login, message.getInfo(MessageInfoStrings.PORT));
				//System.out.println("liste des ports avec leur login : "+clientPorts);
				
				/*
				Message clientPortListMsg = new Message(MessageType.CLIENT_PORT_LIST);
				clientPortListMsg.addObject("clientPorts", clientPorts);
				//System.out.println("SENDER PORT :::: " + message.getInfo("port"));
				clientPortListMsg.addInfo("senderPort", message.getInfo("port"));
				*/
				handler.sendMessage(clientListMsg, socket, paquet);
				//handler.sendMessage(clientPortListMsg, socket, paquet);
				
			} else {
				Message errorMsg = new Message(
						MessageType.ERROR,
						"Client unknown, please reconnect.");
				handler.sendMessage(errorMsg, socket, paquet);
			}
			break;
		case REQUEST_IP:
			if (clientIps.containsKey(login)) {
				
				Message clientIPMsg = new Message(MessageType.CLIENT_IP);
				clientIPMsg.addObject(MessageInfoStrings.REQUEST_IP_TARGET_IP, clientIps.get(login));
				clientIPMsg.addInfo(MessageInfoStrings.REQUEST_IP_TARGET_PORT, clientPorts.get(login));
				clientIPMsg.addInfo(MessageInfoStrings.LOGIN, login);
				handler.sendMessage(clientIPMsg, socket, paquet);
				
			} else {
				Message errorMsg = new Message(
						MessageType.ERROR,
						"Client unknown, please reconnect.");
				handler.sendMessage(errorMsg, socket, paquet);
			}
			break;
		default:
			throw new HandlingException("Message type " + message.getType() + " not handled by " + handler.getClass());
	}
	}

	/**
	 * Get the list of logins.
	 * @return A List containing the name of every connected clients.
	 */
	public List<String> getClientLogins() {
		List<String> res = new ArrayList<String>();
		for (String current : clientIps.keySet()) {
			res.add(current);
		}
		return res;
	}
}
