package serveur.handling;

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
import java.util.Date;

import serveur.ServerMessageManager;
import serveur.Serveur;
import serveur.logging.EventType;
import serveur.logging.Log;

import commun.Message;

public class UDPHandler extends Thread implements Handler {
	
	private Serveur serveur;
	private DatagramSocket socket;
	private ServerMessageManager messageManager;
	private Log log;
	
	public UDPHandler(Serveur serveur) {
		this.serveur = serveur;
		try {
			socket = new DatagramSocket(serveur.getPort());
			messageManager = new ServerMessageManager(serveur, this);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		log = serveur.getLog();
	}

	@Override
	public void run() {
		while(serveur.isRunning()) {
			byte[] buf = new byte[500];
			DatagramPacket p = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(p);
				InetAddress address = p.getAddress();
				if (serveur.getClientIps().containsValue(address)) {
					serveur.getTimeoutHandler().updateClient(address, new Date());
				}
				Message message = getMessage(p);
				messageManager.handleMessage(message, socket,p);
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
		log.log(EventType.RECEIVE_UDP, "Received message: " + message);
		return message;
	}

	@Override
	public void sendMessage(Message message, DatagramSocket socket, DatagramPacket c) throws IOException, ClassNotFoundException {
		try{
			ByteArrayOutputStream b = new ByteArrayOutputStream();
		    ObjectOutputStream o = new ObjectOutputStream(b);
		    o.writeObject(message.getObjects());
			byte[] buf = b.toByteArray();
			DatagramPacket p = new DatagramPacket(buf, buf.length, c.getAddress(), c.getPort());
			log.log(EventType.SEND_UDP, "Sending message: " + message);
			socket.send(p);
		
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public Serveur getServeur() {
		return serveur;
	}

	@Override
	public void sendMessage(Message message, Socket socket)
			throws HandlingException {
		throw new HandlingException("Can't handle a Socket.");
	}

}
