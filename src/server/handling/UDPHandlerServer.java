package server.handling;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;

import server.ServerMessageManager;
import server.Server;

import common.CommonConstants;
import common.MasterClass;
import common.Message;
import common.handling.Handler;
import common.handling.HandlingException;
import common.handling.UDPHandler;
import common.logging.Log;

/**
 * Thread handling the reception and sending of UDP Messages.
 * @author etudiant
 * @see Server
 * @see Message
 * @see Handler
 */
public class UDPHandlerServer extends UDPHandler {
	
	private Server serveur;
	private DatagramSocket socket;
	private ServerMessageManager messageManager;
	private Log log;
	
	/**
	 * Constructs an UDPHandler Object for the specified Server.
	 * <br />Note that the handler's thread needs to be launched by the Server.
	 * @param serveur
	 */
	public UDPHandlerServer(Server serveur) {
		this.serveur = serveur;
		log = serveur.getLog();
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
			byte[] buf = new byte[CommonConstants.UDP_PACKET_SIZE];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(packet);
				InetAddress address = packet.getAddress();
				//log.log(EventType.RECEIVE_UDP, "Received an UDP packet from " + packet.getAddress() + ":" + packet.getPort());
				if (serveur.getClientIps().containsValue(address)) {
					serveur.getTimeoutHandler().updateClient(address, new Date());
					//log.log(EventType.RECEIVE_UDP, "Client known, updating timeout table");
				}
				Message message = getMessage(packet);
				messageManager.handleMessage(message, socket,packet);
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
	public void sendMessage(Message message, DatagramSocket socket, DatagramPacket packet) throws IOException, ClassNotFoundException {
		try{
			ByteArrayOutputStream b = new ByteArrayOutputStream();
		    ObjectOutputStream o = new ObjectOutputStream(b);
		    o.writeObject(message);
			byte[] buf = b.toByteArray();
			int senderPort = Integer.parseInt(message.getInfo("senderPort"));
			DatagramPacket p = new DatagramPacket(buf, buf.length, packet.getAddress(), senderPort);
			System.out.println("Sending  udp message to: " + p.getAddress() + ":" + p.getPort() + "; message="+ message);
			socket.send(p);
		
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public MasterClass getMasterClass() {
		return serveur;
	}

	@Override
	public void sendMessage(Message message, Socket socket)
			throws HandlingException {
		throw new HandlingException("Can't handle a Socket.");
	}

}
