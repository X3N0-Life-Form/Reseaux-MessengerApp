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
	private UDPHandlerDiscussClientSending sending_client;
	private Log log;
	
	public UDPHandlerClient(Client client) {
		this.client = client;
		log = client.getLog();
		try {
			listen = new UDPHandlerClientListening(this.client);
			send = new UDPHandlerClientSending(this.client);
			sending_client = new UDPHandlerDiscussClientSending(this.client);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		
		log.log(EventType.START, "Starting UDP listening handler");
		listen.start();
		
		log.log(EventType.START, "Starting UDP sending handler");
		send.start();
		
		
		if(client.getLogin().equals("test01"))
		{
			while(client.getClientIps().get("test02") == null)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			client.setLoginotherclient("test02");
			client.setIpotherclient(client.getClientIps().get("test02"));
			while(client.getClientPorts().get("test02") == null)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			client.setPortotherclient(Integer.valueOf(client.getClientPorts().get("test02")));
		}else{
			while(client.getClientIps().get("test01") == null)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			client.setLoginotherclient("test01");
			client.setIpotherclient(client.getClientIps().get("test01"));
			while(client.getClientPorts().get("test01") == null)
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			client.setPortotherclient(Integer.valueOf(client.getClientPorts().get("test01")));
		}
		
		log.log(EventType.START, "Starting UDP sending discuss client handler");
		sending_client.start();
	}
	
	public Client getClient() {
		return client;
	}

}
