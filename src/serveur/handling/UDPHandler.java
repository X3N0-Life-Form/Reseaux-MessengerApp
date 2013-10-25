package serveur.handling;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

import serveur.ServerMessageManager;
import serveur.Serveur;

import commun.Message;

public class UDPHandler extends Thread implements Handler {
	
	private Serveur serveur;
	private DatagramSocket socket;
	private ServerMessageManager messageManager;
	
	public UDPHandler(Serveur serveur) {
		this.serveur = serveur;
		try {
			socket = new DatagramSocket(serveur.getPort());
			messageManager = new ServerMessageManager(serveur, this);
		} catch (SocketException e) {
			e.printStackTrace();
		}
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
	public void sendMessage(Message message, DatagramSocket socket) {
		//TODO:todo
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
