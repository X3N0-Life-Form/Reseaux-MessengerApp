package common.handling;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;

import common.Message;

public abstract class UDPHandler extends Thread implements Handler {

	/**
	 * Extracts the message contained in a DatagramPacket.
	 * @param p The DatagramPacket supposedly containing a Message Object.
	 * @return Message object read off the packet.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Message getMessage(DatagramPacket p) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(p.getData());
		ObjectInputStream ois = new ObjectInputStream(bis);
		Message message = (Message) ois.readObject();
		return message;
	}
	
}
