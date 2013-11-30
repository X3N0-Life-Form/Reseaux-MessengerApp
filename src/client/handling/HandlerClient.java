package client.handling;

import java.io.IOException;
import java.net.DatagramSocket;

import commun.Handler;
import commun.HandlingException;
import commun.Message;

/**
 * Client side specific handler interface.
 * @author etudiant
 *
 */
public interface HandlerClient extends Handler {
	
	/**
	 * Doesn't require a DatagramPacket.
	 * @param message
	 * @param socket
	 * @throws HandlingException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void sendMessage(Message message, DatagramSocket socket) throws HandlingException, IOException, ClassNotFoundException;
}
