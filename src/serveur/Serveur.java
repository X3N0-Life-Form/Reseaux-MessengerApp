package serveur;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import serveur.handling.DialogHandler;
import serveur.handling.UDPHandler;


public class Serveur {
	private ServerSocket serverSocket;
	private Map<String, InetAddress> clientIps;
	private int port;
	private ServerTimeoutHandler timeoutHandler;
	private boolean running;
	private long timeout;
	private UDPHandler udpHandler;
	
	public static final int DEFAULT_PORT = 8001;
	public static final long DEFAULT_TIMEOUT_TIME = 2000;
	
	public Serveur() throws IOException {
		port = DEFAULT_PORT;
		serverSocket = new ServerSocket(port);
		clientIps = new HashMap<String, InetAddress>();
		timeoutHandler = new ServerTimeoutHandler(this);
		running = false;
		timeout = DEFAULT_TIMEOUT_TIME;
		udpHandler = new UDPHandler(this);
	}
	
	public static void main(String args[]) {
		for (int i = 0; i < args.length; i++) {
			//TODO: handle args
		}
		try {
			Serveur serveur = new Serveur();
			serveur.printRecap();
			serveur.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void printRecap() {
		System.out.println("Starting the server:");//TODO:proper log handling (see SysExp)
		System.out.println("\tIp:\t" + serverSocket.getInetAddress());
		System.out.println("\tPort:\t" + serverSocket.getLocalPort());
	}

	public void start() {
		Thread timeoutThread = new Thread(timeoutHandler);
		timeoutThread.start();
		running = true;
		while (running) {
			try {
				Socket socket = serverSocket.accept();
				DialogHandler dh = new DialogHandler(socket, this);
				dh.start();
				udpHandler.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean authenticateClient(String login, String pass) {
		// TODO Auto-generated method stub
		return false;
	}

	public Map<String, InetAddress> getClientIps() {
		return clientIps;
	}

	public int getPort() {
		return port;
	}

	public boolean isRunning() {
		return running;
	}

	public ServerTimeoutHandler getTimeoutHandler() {
		return timeoutHandler;
	}

	public long getTimeoutTime() {
		return timeout;
	}
}
