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

import commun.CommonConstants;
import commun.HandlingException;
import commun.MasterClass;
import commun.Message;
import commun.logging.EventType;
import commun.logging.Log;

public class UDPHandlerClientListening extends Thread implements HandlerClient{
	
	private Client client;
	private DatagramSocket socket;
	private ClientMessageManager messageManager;
	private Log log;
	
	public UDPHandlerClientListening(Client client) throws SocketException{
		this.client = client;
		this.log = client.getLog();
		this.socket = new DatagramSocket();
		client.setUDPMainListeningPort(socket.getLocalPort());
		log.log(EventType.INFO, "UDP port set to " + client.getUDPMainListeningPort());
		messageManager = new ClientMessageManager(client, this);
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
			InetAddress ad = InetAddress.getByName(client.getServerIp());
			//DatagramPacket p = new DatagramPacket(buf, buf.length, ad, client.getPort());
			DatagramPacket p = new DatagramPacket(buf, buf.length, ad, 8001);
			
			socket.send(p);
		
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public MasterClass getMasterClass() {
		return client;
	}

	@Override
	public void sendMessage(Message message, Socket socket)
			throws HandlingException {
		throw new HandlingException("Can't handle a Socket.");
	}

	@Override
	public void sendMessage(Message message, DatagramSocket socket,
			DatagramPacket paquet) throws HandlingException, IOException,
			ClassNotFoundException {
		sendMessage(message, socket);
	}

}
