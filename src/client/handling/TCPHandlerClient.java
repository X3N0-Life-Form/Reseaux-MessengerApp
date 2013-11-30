package client.handling;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import client.Client;
import client.ClientMessageManager;

import common.MasterClass;
import common.Message;
import common.MessageType;
import common.handling.HandlingException;

/**
 * Client side TCP handler class. Sends the Client's login and password to the Server for
 * authentication. Note that despite possessing a run() method, this is <b>not</b> a Thread.
 * @author etudiant
 * @see ClientMessageManager
 */
public class TCPHandlerClient implements HandlerClient {
	
	private Socket socket;
	private Client client;
	private ClientMessageManager messageManager;
	
	/**
	 * Constructs a TCPHandler Object for the specified client using the specified Socket. Note
	 * that the TCP dialog must be launched by the Client by calling the run() method.
	 * @param clientSocket
	 * @param client
	 */
	public TCPHandlerClient(Socket clientSocket, Client client) {
		socket = clientSocket;
		this.client = client;
		messageManager = new ClientMessageManager(client,this);
	}
	
	/**
	 * Sends a connection request to the Server, using the Client's login and password.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws HandlingException
	 */
	public void handleConnect() throws IOException, ClassNotFoundException, HandlingException {
		Message message = new Message(MessageType.CONNECT);
		message.addInfo("login",client.getLogin());
		message.addInfo("pass",client.getPass());
		this.sendMessage(message,socket);
	}

	/**
	 * Receives messages from the Server and passes them to the MessageManager. In effect,
	 * this should only be receiving answers to a connection request. 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws HandlingException
	 */
	public void handleDialog() throws IOException, ClassNotFoundException, HandlingException {
		Message message = getServeurMessage(socket);
		messageManager.handleMessage(message, socket);
		socket.close();
	}

	@Override
	public void sendMessage(Message message, Socket socket) throws IOException {
		ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
		os.writeObject(message);
		os.flush();
	}

	/**
	 * Gets a Message received through the specified socket.
	 * @param socket
	 * @return Message object.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Message getServeurMessage(Socket socket) throws IOException, ClassNotFoundException {
		ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
		Message message = (Message) is.readObject();
		return message;
	}

	/**
	 * Connects the Client to the Server.
	 */
	public void run() {
		try {
			handleConnect();
			handleDialog();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (HandlingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public MasterClass getMasterClass() {
		return client;
	}

	@Override
	public void sendMessage(Message message, DatagramSocket socket) throws HandlingException{
		throw new HandlingException("Can't handle a DatagramSocket.");
	}

	@Override
	public void sendMessage(Message message, DatagramSocket socket,
			DatagramPacket paquet) throws HandlingException, IOException,
			ClassNotFoundException {
		sendMessage(message, socket);
	}

}
