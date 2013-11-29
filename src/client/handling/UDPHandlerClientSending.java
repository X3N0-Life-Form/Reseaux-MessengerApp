package client.handling;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import client.Client;
import client.ClientMessageManager;

import commun.Message;
import commun.MessageType;
import commun.logging.EventType;
import commun.logging.Log;

public class UDPHandlerClientSending extends Thread implements HandlerClient{
	
	private Client client;
	private DatagramSocket socket;
	private ClientMessageManager messageManager;
	private Log log;
	
	public UDPHandlerClientSending(Client client) throws SocketException{
		this.client = client;
		log = client.getLog();
		this.socket = new DatagramSocket();
		messageManager = new ClientMessageManager(client, this);
	}
	
	@Override
	public void run(){
		while(client.isRunning()) {
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
