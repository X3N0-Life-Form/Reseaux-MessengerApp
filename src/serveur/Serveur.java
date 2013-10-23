package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import commun.MessageType;
import commun.Message;

public class Serveur {
	private InetAddress ip;
	private int port;
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
		} catch (IOException e) { //TODO: handle various exceptions
			e.printStackTrace();
		}
	}
	
	public void start() {
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				Message message = getClientMessage(socket);
				switch (message.getType()) {
				case CONNECT:
					String login = message.getLogin();
					String pass = message.getPass();
					if (authenticateClient(login, pass)) {
						InetAddress ip = socket.getInetAddress();
						clientIps.put(login, ip);
					}
					break;
				
				}
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private Message getClientMessage(Socket socket) throws IOException {
		InputStream is = socket.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;
		while ((line = br.readLine()) != null) {
			
		}
		
		return null;
	}

	private boolean authenticateClient(String login, String pass) {
		// TODO Auto-generated method stub
		return false;
	}

	public Serveur(int port) throws IOException {
		this.port = port;
		serverSocket = new ServerSocket(port);
		ip = serverSocket.getInetAddress();
		clientIps = new HashMap<String, InetAddress>();
	}
	
}
