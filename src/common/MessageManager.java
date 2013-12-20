package common;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

import common.handling.HandlingException;

/**
 * Message Manager interface. Both the Client and Server need a MessageManager capable
 * of handling incoming or outgoing {@link Message} objects.
 * @author etudiant
 * @see Message
 */
public interface MessageManager {
	
	/**
	 * Deals with Messages received through a regular Socket.
	 * @param message - Message that needs to be handled.
	 * @param socket - Socket handling the Message.
	 * @throws IOException
	 * @throws HandlingException If this Manager's Handler is unable to deal with Sockets.
	 */
	public void handleMessage(Message message, Socket socket) throws IOException, HandlingException;

	/**
	 * Deals with Messages received through a DatagramSocket. A DatagramPacket is sometimes required
	 * to properly send back a Message to the right sender.
	 * @param message - Message that needs to be handled.
	 * @param socket - DatagramSocket handling the Message.
	 * @param packet - DatagramPacket received through the specified socket.
	 * @throws IOException
	 * @throws HandlingException If this Manager's Handler is unable to deal with DatagramSockets.
	 */
	public void handleMessage(Message message, DatagramSocket socket, DatagramPacket packet) throws HandlingException, IOException, ClassNotFoundException;

	/**
	 * Deals with Messages received through a DatagramSocket.
	 * @param message - Message that needs to be handled.
	 * @param socket - DatagramSocket handling the Message.
	 * @throws IOException
	 * @throws HandlingException If this Manager's Handler is unable to deal with DatagramSockets.
	 */
	public void handleMessage(Message message, DatagramSocket socket) throws HandlingException, IOException, ClassNotFoundException;
}
