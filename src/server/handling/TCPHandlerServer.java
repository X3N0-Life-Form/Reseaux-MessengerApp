package server.handling;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

import server.Server;
import server.ServerMessageManager;

import common.MasterClass;
import common.Message;
import common.handling.Handler;
import common.handling.HandlingException;
import common.logging.Log;

/**
 * 
 * @author etudiant
 * @see Handler
 * @see ServerMessageManager
 */
public class TCPHandlerServer extends Thread implements Handler {
	
	private Socket socket;
	private Server serveur;
	@SuppressWarnings("unused")
	private Log log;
	private ServerMessageManager messageManager;
	
	/**
	 * Constructs a TCP handler linked to the specified Server and Socket.
	 * <br />Note that the thread must be started by the Server.
	 * @param clientSocket
	 * @param serveur
	 */
	public TCPHandlerServer(Socket clientSocket, Server serveur) {
		socket = clientSocket;
		this.serveur = serveur;
		log = serveur.getLog();
		messageManager = new ServerMessageManager(serveur, this);
	}
	
	/**
	 * Retrieves a Message, sends it to the MessageManager and closes the Socket.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws HandlingException
	 */
	public void handleDialog() throws IOException, ClassNotFoundException, HandlingException {
		Message message = getClientMessage(socket);
		messageManager.handleMessage(message, socket);
		socket.close();
	}
	
	@Override
	public void sendMessage(Message message, Socket socket) throws IOException {
		ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
		os.writeObject(message);
		//log.log(EventType.SEND_TCP, "Sending message: " + message);
		os.flush();
	}

	/**
	 * Retrieves a Message received by the specified Socket.
	 * @param socket - Socket receiving the Message.
	 * @return Message object read off the input stream.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Message getClientMessage(Socket socket) throws IOException, ClassNotFoundException {
		ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
		Message message = (Message) is.readObject();
		//log.log(EventType.RECEIVE_TCP, "Received message: " + message);
		return message;
	}

	@Override
	public void run() {
		try {
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
		return serveur;
	}

	/**
	 * Not implemented. Throws a {@link HandlingException}.
	 */
	@Override
	public void sendMessage(Message message, DatagramSocket socket, DatagramPacket paquet) throws HandlingException {
		throw new HandlingException("Can't handle a DatagramSocket.");
	}

}
