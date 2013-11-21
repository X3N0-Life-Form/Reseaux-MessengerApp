package client;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import client.handling.HandlerClient;
import client.handling.HandlingException;
import commun.Message;
import commun.logging.EventType;
import commun.logging.Log;

public class ClientMessageManager {
	
	private Client client;
	private HandlerClient handler;
	@SuppressWarnings("unused")
	private Map<String, InetAddress> clientIps;
	@SuppressWarnings("unused")
	private Map<String, String> clientPorts;
	private Log log;
	
	public ClientMessageManager(Client client, HandlerClient handler){
		this.client=client;
		log = client.getLog();
		this.handler = handler;
		this.clientIps = client.getClientIps();
		this.clientPorts = client.getClientPorts();
	}

	public void handleMessage(Message message, Socket socket) throws IOException, HandlingException {
		switch (message.getType()) {
		
		case OK:
			log.log(EventType.RECEIVE_TCP, "Received OK message: " + message);
			break;
		
		case ERROR:
			log.log(EventType.RECEIVE_TCP, "Warning: Received Error message: " + message);
			break;
		}
	}
	
	public void handleMessage(Message message, DatagramSocket socket) throws HandlingException, IOException, ClassNotFoundException {
		switch (message.getType()) {
		
		case REQUEST_LIST:
			System.out.println("envoi d'un signe de vie");
			handler.sendMessage(message, socket);
			break;
			
		case CLIENT_LIST:
			System.out.println("Reception de la liste des amis connecté avec leur adresses Ip");
			@SuppressWarnings("unchecked")
			Map<String, InetAddress> temp1 = new HashMap<String, InetAddress>((Map<String, InetAddress>) message.getObject("clientIps"));
			client.setClientIps(temp1);
			System.out.println("liste de amis connectés avec leur Ip: "+temp1);
			clientIps=temp1;
			break;
			
		case CLIENT_PORT_LIST:
			System.out.println("Reception de la liste des amis connecté avec leur ports d'écoute");
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
			
		case ERROR:
			System.out.println("ERROR:" + message);
			break;
			
			default:
				log.log(EventType.INFO, "Warning: could not handle message: " + message);
				break;
		}
	}
}
