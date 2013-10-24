package serveur;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import commun.Message;
import commun.MessageType;


public class Serveur {
	private ServerSocket serverSocket;
	private Map<String, InetAddress> clientIps;
	
	public static final int DEFAULT_PORT = 8001; 
	
	public static void main(String args[]) {
		int port = DEFAULT_PORT;
		for (int i = 0; i < args.length; i++) {
			//TODO: handle args
		}
		try {
			Serveur serveur = new Serveur(port);
			serveur.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void start() {
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				Message message = getClientMessage(socket);
				InetAddress ip = socket.getInetAddress();
				String login = message.getInfo("login");
				switch (message.getType()) {
				case CONNECT:
					String pass = message.getInfo("pass");
					if (authenticateClient(login, pass)) {
						clientIps.put(login, ip);
					} else {
						Message errorMsg = new Message(
								MessageType.ERROR,
								"Unable to authenticate client: wrong login or password.");
						sendMessage(errorMsg, socket);
					}
					break;
				case REQUEST_LIST: //TODO: move that to UDP handler
					if (clientIps.containsKey(login)) {
						Message clientListMsg = new Message(MessageType.CLIENT_LIST);
						clientListMsg.addObject("clientIps", clientIps);
						sendMessage(clientListMsg, socket);
					} else {
						Message errorMsg = new Message(
								MessageType.ERROR,
								"Client unknown, please reconnect.");
						sendMessage(errorMsg, socket);
					}
					break;
				case DISCONNECT:
					clientIps.remove(login);
					break;
				}
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private void sendMessage(Message message, Socket socket) throws IOException {
		ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
		os.writeObject(message);
		os.flush();
	}

	private Message getClientMessage(Socket socket) throws IOException, ClassNotFoundException {
		ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
		Message message = (Message) is.readObject();
		return message;
	}

	private boolean authenticateClient(String login, String pass) {
		// TODO Auto-generated method stub
		return false;
	}

	public Serveur(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		clientIps = new HashMap<String, InetAddress>();
	}

}
