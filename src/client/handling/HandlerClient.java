package client.handling;

import java.io.IOException;
import java.net.DatagramSocket;

import common.Message;
import common.handling.Handler;
import common.handling.HandlingException;

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
