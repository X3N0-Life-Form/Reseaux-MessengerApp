package client.handling;

import client.Client;

public class UDPHandlerClient extends Thread {
	
	private Client client;
	private UDPHandlerClientListening listen;
	private UDPHandlerClientSending send;
	
	public UDPHandlerClient(Client client) {
		this.client = client;
		listen = new UDPHandlerClientListening(client);
		send = new UDPHandlerClientSending(client);
	}

	public void run() {
		listen.start();
		send.start();
	}
	
	public Client getClient() {
		return client;
	}

}
