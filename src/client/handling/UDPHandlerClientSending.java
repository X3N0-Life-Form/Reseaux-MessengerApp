package client.handling;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import client.Client;

import common.Message;
import common.MessageType;
import common.handling.HandlingException;
import common.logging.EventType;

public class UDPHandlerClientSending extends UDPHandlerClient {
	
	public UDPHandlerClientSending(Client client) throws SocketException {
		super(client);
	}

	@Override
	public void run(){
		while(client.isRunning() && client.isConnected()) {
			try{
				Message msgLive = new Message(MessageType.REQUEST_LIST);
				msgLive.addInfo("login", client.getLogin());
				msgLive.addInfo("port", client.getUDPMainListeningPort() + "");
				messageManager.handleMessage(msgLive, socket);
				Thread.sleep(2000);//TODO: mettre ça dans une constante
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (HandlingException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void sendMessage(Message message, DatagramSocket socket) throws IOException, ClassNotFoundException {
		try{
			ByteArrayOutputStream b = new ByteArrayOutputStream();
		    ObjectOutputStream o = new ObjectOutputStream(b);
		    o.writeObject(message);
			byte[] buf = b.toByteArray();
			InetAddress ad = InetAddress.getByName(client.getServerIp());	
			DatagramPacket p = new DatagramPacket(buf, buf.length, ad, client.getServerPort());
			log.log(EventType.SEND_UDP, "Sending message to " + ad + ":" + client.getServerPort());
			socket.send(p);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
