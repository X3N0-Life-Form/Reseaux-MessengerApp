package client.handling;

import java.net.DatagramSocket;
import java.net.SocketException;

import commun.logging.EventType;
import commun.logging.Log;

import client.Client;

public class UDPHandlerClient extends Thread {
	
	private Client client;
	private UDPHandlerClientListening listen;
	private UDPHandlerClientSending send;
	private Log log;
	//private DatagramSocket socket;
	
	public UDPHandlerClient(Client client) {
		this.client = client;
		log = client.getLog();
		/*try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}*/
		//client.setUDPMainListeningPort(socket.getPort());
		//log.log(EventType.INFO, "UDP port set to " + socket.getPort());
		try {
			listen = new UDPHandlerClientListening(client/*, socket*/);
			send = new UDPHandlerClientSending(client/*, socket*/);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		log.log(EventType.START, "Starting UDP listening handler");
		listen.start();
		
		log.log(EventType.START, "Starting UDP sending handler");
		send.start();
	}
	
	public Client getClient() {
		return client;
	}

}
