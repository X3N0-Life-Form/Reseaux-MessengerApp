package serveur.handling;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.DatagramPacket;

import serveur.Serveur;

import commun.Message;

/**
 * Server-side handler interface.
 * @author etudiant
 * @see TCPHandler
 * @see UDPHandler
 */
public interface HandlerServeur { //TODO: make this a common interface

	/**
	 * 
	 * @param message - Message to be send.
	 * @param socket - Socket that must send the message.
	 * @throws IOException
	 * @throws HandlingException If this method is not supported by this Handler.
	 */
	public void sendMessage(Message message, Socket socket) throws IOException, HandlingException;
	
	/**
	 * 
	 * @param message - Message to be send.
	 * @param socket - DatagramSocket that must send the message.
	 * @param paquet - DatagramPacket used as a base to form the outgoing packet.
	 * @throws HandlingException If this method is not supported by this Handler.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void sendMessage(Message message, DatagramSocket socket, DatagramPacket paquet) throws HandlingException, IOException, ClassNotFoundException;
	
	/**
	 * Gets the Server.
	 * @return Server associated with this Handler.
	 */
	public Serveur getServeur();

}
