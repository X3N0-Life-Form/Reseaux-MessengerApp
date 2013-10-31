package client.handling;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import client.ClientMessageManager;
import client.Client;

import commun.Message;
import commun.MessageType;

public class UDPHandlerClientSending extends Thread implements Handler{
	
	private Client client;
	private DatagramSocket socket;
	private ClientMessageManager messageManager;
	
	public UDPHandlerClientSending(Client client){
		this.client = client;
		try {
			socket = new DatagramSocket();
			messageManager = new ClientMessageManager(client, this);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		while(client.isRunning()) {
			try{
			Message msgLive = new Message(MessageType.REQUEST_LIST, "hello");
			messageManager.handleMessage(msgLive, socket);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (HandlingException e) {
				e.printStackTrace();
			}
		}
	}

	public Message getMessage(DatagramPacket p) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(p.getData());
		ObjectInputStream ois = new ObjectInputStream(bis);
		Message message = (Message) ois.readObject();
		return message;
	}

	@Override
	public void sendMessage(Message message, DatagramSocket socket) throws IOException, ClassNotFoundException {
		try{
			ByteArrayOutputStream b = new ByteArrayOutputStream();
		    ObjectOutputStream o = new ObjectOutputStream(b);
		    o.writeObject(message.getObjects());
			byte[] buf = b.toByteArray();
			InetAddress ad = InetAddress.getLocalHost();//TODO: mettre addresse serveur			
			DatagramPacket p = new DatagramPacket(buf, buf.length, ad, client.getPort());
			socket.send(p);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Client getClient() {
		return client;
	}

	@Override
	public void sendMessage(Message message, Socket socket)
			throws HandlingException {
		throw new HandlingException("Can't handle a Socket.");
	}

}
