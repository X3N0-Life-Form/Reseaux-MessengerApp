package client.handling;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.Socket;

import client.Client;
import client.ClientMessageManager;

import commun.Message;
import commun.MessageType;

public class TCPHandlerClient extends Thread implements Handler {
	
	private Socket socket;
	private Client client;
	private ClientMessageManager messageManager;
	
	public TCPHandlerClient(Socket clientSocket, Client client) {
		socket = clientSocket;
		this.client = client;
		messageManager = new ClientMessageManager(client,this);
	}
	
	public void handleConnect() throws IOException, ClassNotFoundException, HandlingException {
		Message message = new Message(MessageType.CONNECT);
		message.addInfo("login",client.getLogin());
		message.addInfo("pass",client.getPass());
		this.sendMessage(message,socket);
	}

	public void handleDialog() throws IOException, ClassNotFoundException, HandlingException {
		Message message = getServeurMessage(socket);
		messageManager.handleMessage(message, socket);
		socket.close();
	}

	
	public void sendMessage(Message message, Socket socket) throws IOException {
		ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
		os.writeObject(message);
		os.flush();
	}

	public Message getServeurMessage(Socket socket) throws IOException, ClassNotFoundException {
		ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
		Message message = (Message) is.readObject();
		return message;
	}

	@Override
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

	public Client getClient() {
		return client;
	}

	@Override
	public void sendMessage(Message message, DatagramSocket socket) throws HandlingException{
		throw new HandlingException("Can't handle a DatagramSocket.");
	}

}
