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

public class UDPHandlerClientListening extends Thread implements Handler{
	
	private Client client;
	private DatagramSocket socket;
	private ClientMessageManager messageManager;
	
	public UDPHandlerClientListening(Client client){
		this.client = client;
		try {
			socket = new DatagramSocket(client.getPort());
			messageManager = new ClientMessageManager(client, this);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		while(client.isRunning()) {
			byte[] buf = new byte[10000]; //TODO: voir speech en dessous sur les valeurs en dur
			DatagramPacket p = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(p);
				Message message = getMessage(p);
				messageManager.handleMessage(message, socket);
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
			InetAddress ad = InetAddress.getLocalHost();
			//TODO:enregistrer port serveur et référencer ça ici (d'une manière générale, penser à changer String et autres valeurs écrites "en dur" en constantes dans la mesure du possible)
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
