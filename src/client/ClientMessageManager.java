package client;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import client.handling.Handler;
import client.handling.HandlingException;
import commun.Message;

public class ClientMessageManager {
	
	private Client client;
	private Handler handler;
	@SuppressWarnings("unused")
	private Map<String, InetAddress> clientIps;
	
	public ClientMessageManager(Client client, Handler handler){
		this.client=client;
		this.handler = handler;
		this.clientIps = client.getClientIps();
	}

	public void handleMessage(Message message, Socket socket) throws IOException, HandlingException {
		switch (message.getType()) {
		
		case OK:
			System.out.println(message.getMessage());
			break;
		
		case ERROR:
			System.out.println(message.getMessage());
			break;
		}
	}
	
	public void handleMessage(Message message, DatagramSocket socket) throws HandlingException, IOException, ClassNotFoundException {
		switch (message.getType()) {
		
		case LIVE:
			System.out.println("envoi d'un signe de vie");
			handler.sendMessage(message, socket);
			break;
			
		case CLIENT_LIST:
			System.out.println("Reception de la liste des amis connecté");
			@SuppressWarnings("unchecked")
			Map<String, InetAddress> temp = new HashMap<String, InetAddress>((Map<String, InetAddress>) message.getObject("clientIps"));
			client.setClientIps(temp);
			clientIps=temp;
			break;
		}
	}
}
