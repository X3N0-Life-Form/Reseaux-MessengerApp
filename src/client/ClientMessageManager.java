package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.handling.HandlerClient;
import common.ErrorTypes;
import common.Message;
import common.MessageInfoStrings;
import common.MessageManager;
import common.MessageType;
import common.handling.HandlingException;
import common.logging.EventType;
import common.logging.Log;

/**
 * Similar to its Server-side counterpart, decyphers received {@link Message} objects
 * and sends Messages back to the Server or other Clients.
 * @author etudiant
 *
 */
public class ClientMessageManager implements MessageManager {
	
	private Client client;
	private HandlerClient handler;
	private Map<String, InetAddress> clientIps;
	private Map<String, String> clientPorts;
	private Log log;
	
	private int counter = 0;
	
	/**
	 * Constructs a MessageManager for the specified Client and Handler.
	 * @param client
	 * @param handler
	 */
	public ClientMessageManager(Client client, HandlerClient handler){
		this.client=client;
		log = client.getLog();
		this.handler = handler;
		this.clientIps = client.getClientIps();
		this.clientPorts = client.getClientPorts();
	}
	
	@Override
	public void handleMessage(Message message, Socket socket) throws IOException, HandlingException {
		switch (message.getType()) {
		
		case OK:
			log.log(EventType.RECEIVE_TCP, "Received OK message: " + message);
			client.setConnected(true);
			break;

		case ERROR:
			log.log(EventType.ERROR, "Warning: Received Error message: " + message);
			client.setConnected(false);
			client.getLoginController().fireErrorMessage(message);
			break;
			
		case CONNECT:
			log.log(EventType.SEND_TCP, "Sending connect request");
			Message connectMsg = new Message(MessageType.CONNECT);//Note: copied from handleDialog()
			connectMsg.addInfo(MessageInfoStrings.LOGIN,client.getLogin());//TODO handleDialog() redundant
			connectMsg.addInfo(MessageInfoStrings.PASSWORD,client.getPass());// call messageManager instead
			handler.sendMessage(connectMsg, socket);
			break;
			
		default:
			log.log(EventType.WARNING, "Warning: could not handle message: " + message);
			break;
		}
	}
	
	@Override
	public void handleMessage(Message message, DatagramSocket socket) throws HandlingException, IOException, ClassNotFoundException {
		switch (message.getType()) {
		
		case REQUEST_LIST:
		case REQUEST_IP:
		case DISCONNECT:
			handler.sendMessage(message, socket);
			break;
			
		case CLIENT_LIST:
			@SuppressWarnings("unchecked")
			List<String> logins = (List<String>) message.getObject(MessageInfoStrings.REQUEST_LIST_CLIENT_LOGINS);
			if (counter == 0) {
				client.setClientLogins(logins);
				counter++;
			} else {
				client.setClientLogins(logins);
				client.getContactListController().refresh();
			}
			break;
			
		case CLIENT_PORT_LIST://TODO:not used anymore, remove it
			@SuppressWarnings("unchecked")
			Map<String, String> temp2 = new HashMap<String, String>((Map<String, String>) message.getObject("clientPorts"));
			client.setClientPorts(temp2);
			clientPorts=temp2;
			System.out.println("liste de amis connectés avec leur ports: "+temp2);
			break;
			
		case MSG_DISCUSS_CLIENT:
			System.out.println("envoi du message au client");
			handler.sendMessage(message, socket);
			break;
			
		case CLIENT_IP:
			InetAddress targetIP = (InetAddress) message.getObject(MessageInfoStrings.REQUEST_IP_TARGET_IP);
			// if target client is also on the server
			if (targetIP.isAnyLocalAddress() || targetIP.isLoopbackAddress()) {
				targetIP = InetAddress.getByName(client.getServerIp());
			}
			String targetPort = message.getInfo(MessageInfoStrings.REQUEST_IP_TARGET_PORT);
			String targetLogin = message.getInfo(MessageInfoStrings.LOGIN);
			clientIps.put(targetLogin, targetIP);
			clientPorts.put(targetLogin, targetPort);
			break;
			
		case ERROR:
			String type = message.getInfo(MessageInfoStrings.ERROR_TYPE);
			if (type.equals(ErrorTypes.CLIENT_UNKNOWN)) {
				client.getContactListController().getClw().disconnect();
			} else if (type.equals(ErrorTypes.ALREADY_CONNECTED)) {
				
			}
			log.log(EventType.ERROR, "ERROR:" + message);
			client.getLoginController().fireErrorMessage(message);
			break;
			
		default:
			log.log(EventType.WARNING, "Warning: could not handle message: " + message);
			break;
		}
	}

	/**
	 * Not implemented. Throws a {@link HandlingException}.
	 */
	@Override
	public void handleMessage(Message message, DatagramSocket socket,
			DatagramPacket packet) throws HandlingException, IOException,
			ClassNotFoundException {
		throw new HandlingException("This method has not been implemented on this MessageManager");
	}

}
