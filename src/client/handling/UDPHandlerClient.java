package client.handling;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import client.Client;
import client.ClientMessageManager;

import common.MasterClass;
import common.Message;
import common.handling.HandlingException;
import common.handling.UDPHandler;
import common.logging.Log;

/**
 * Abstract class containing methods common to Listening, Discuss and Sending subclasses.
 * @author etudiant
 * @see UDPHandlerClientDiscuss
 * @see UDPHandlerClientListening
 * @see UDPHandlerClientSending
 */
public abstract class UDPHandlerClient extends UDPHandler implements HandlerClient {
	
	protected Client client;
	protected DatagramSocket socket;
	private ClientMessageManager messageManager;
	protected Log log;
	
	
	public UDPHandlerClient(Client client) throws SocketException{
		this.client = client;
		log = client.getLog();
		this.socket = new DatagramSocket();
		setMessageManager(new ClientMessageManager(client, this));
	}

	@Override
	public void sendMessage(Message message, Socket socket) throws IOException,
			HandlingException {
		throw new HandlingException("Can't handle a Socket.");
	}

	@Override
	public MasterClass getMasterClass() {
		return client;
	}

	@Override
	public void sendMessage(Message message, DatagramSocket socket,
			DatagramPacket paquet) throws HandlingException, IOException,
			ClassNotFoundException {
		sendMessage(message, socket);
	}

	public ClientMessageManager getMessageManager() {
		return messageManager;
	}

	public void setMessageManager(ClientMessageManager messageManager) {
		this.messageManager = messageManager;
	}
	
	public DatagramSocket getSocket() {
		return socket;
	}

	public void setSocket(DatagramSocket socket) {
		this.socket = socket;
	}

}
