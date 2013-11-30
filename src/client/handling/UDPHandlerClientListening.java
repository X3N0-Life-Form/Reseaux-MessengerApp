package client.handling;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import client.Client;

import commun.CommonConstants;
import commun.Message;
import commun.handling.HandlingException;
import commun.logging.EventType;

public class UDPHandlerClientListening extends UDPHandlerClient {
	
	public UDPHandlerClientListening(Client client) throws SocketException{
		super(client);
		client.setUDPMainListeningPort(socket.getLocalPort());
		log.log(EventType.INFO, "UDP port set to " + client.getUDPMainListeningPort());
	}
	
	@Override
	public void run(){
		while(client.isRunning()) {
			byte[] buf = new byte[CommonConstants.UDP_PACKET_SIZE];
			DatagramPacket p = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(p);
				Message message = getMessage(p);
				switch (message.getType()) {
				case MSG_DISCUSS_CLIENT:
					System.out.println("******** msg : " + message.getInfo("msg") + "************");
					break;
				default:
					messageManager.handleMessage(message, socket);
					break;
			}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (HandlingException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void sendMessage(Message message, DatagramSocket socket) throws IOException, ClassNotFoundException {
		try{
			ByteArrayOutputStream b = new ByteArrayOutputStream();
		    ObjectOutputStream o = new ObjectOutputStream(b);
		    o.writeObject(message.getObjects());
			byte[] buf = b.toByteArray();
			InetAddress ad = InetAddress.getByName(client.getServerIp());
			//DatagramPacket p = new DatagramPacket(buf, buf.length, ad, client.getPort());
			DatagramPacket p = new DatagramPacket(buf, buf.length, ad, 8001);
			
			socket.send(p);
		
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

}
