package serveur;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import serveur.handling.HandlerServeur;
import serveur.handling.HandlingException;

import commun.Message;
import commun.MessageType;

/**
 * This class determines the server's behavior upon receiving a Message from a client 
 * and prepares an appropriate answer.
 * <br />Note that the actual reception and response are delegated to the Handler objects.
 * @author etudiant
 * @see HandlerServeur
 * @see UDPHandler
 * @see TCPHandler
 */
public class ServerMessageManager {
	
	private Serveur serveur;
	private HandlerServeur handler;
	private Map<String, InetAddress> clientIps;
	private Map<String, String> clientPorts;
	
	/**
	 * Constructs a message manager for the specified server and handler.
	 * @param serveur
	 * @param handler
	 */
	public ServerMessageManager(Serveur serveur, HandlerServeur handler) {
		this.serveur = serveur;
		this.handler = handler;
		clientIps = serveur.getClientIps();
		clientPorts = serveur.getClientPorts();
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
		String login = message.getInfo("login");
		switch (message.getType()) {
		
		
		case CONNECT:
			String pass = message.getInfo("pass");
			if (serveur.authenticateClient(login, pass)) {
				clientIps.put(login, ip);
				serveur.getTimeoutHandler().addClient(socket.getInetAddress());
				System.out.println(socket.getInetAddress());
				Message okMsg = new Message(
						MessageType.OK,
						"Able to authenticate client.");
				handler.sendMessage(okMsg, socket);
			} else {
				Message errorMsg = new Message(
						MessageType.ERROR,
						"Unable to authenticate client: wrong login or password.");
				handler.sendMessage(errorMsg, socket);
			}
			break;
			
		case DISCONNECT:
			clientIps.remove(login);
			serveur.getTimeoutHandler().removeClient(ip);
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
		String login = message.getInfo("login");
		switch (message.getType()) {
		case REQUEST_LIST:
		if (clientIps.containsKey(login)) {
			
			Message clientListMsg = new Message(MessageType.CLIENT_LIST);
			List<String> clientLogins = getClientLogins();
			//clientListMsg.addObject("clientIps", clientIps);
			clientListMsg.addObject("clientLogins", clientLogins);
			//System.out.println("SENDER PORT :::: " + message.getInfo("port"));
			clientListMsg.addInfo("senderPort", message.getInfo("port"));
			
			clientPorts.put(login, message.getInfo("port"));
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
