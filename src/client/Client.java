package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import commun.logging.EventType;
import commun.logging.Log;

import serveur.Serveur;

import client.handling.TCPHandlerClient;
import client.handling.UDPHandlerClient;

import client.ClientTimeoutHandler;

public class Client {
	private static final long DEFAULT_TIMEOUT_TIME = 0;
	private String login;
	private String pass;
	private String serverIp;
	private int serverPort;
	private Socket clientSocket;
	private boolean running;
	private Map<String, InetAddress> clientIps;
	private ClientTimeoutHandler timeoutHandler;
	private long timeout;
	private int mainUDPListeningPort;
	private Log log;

	
	public Client(String login, String pass, String serverIp, int port) throws IOException{
		super();
		this.login = login;
		this.serverPort = port;
		this.pass = pass;
		clientSocket = new Socket(serverIp, port);
		this.serverIp = serverIp;
		clientIps = new HashMap<String, InetAddress>();
		timeoutHandler = new ClientTimeoutHandler(this);
		running = false;
		timeout = DEFAULT_TIMEOUT_TIME;
		log = new Log();
	}
	
	public void printRecap() {
		System.out.println("Starting the client:");
		System.out.println("\tIp:\t" + clientSocket.getInetAddress());
		System.out.println("\tPort:\t" + clientSocket.getLocalPort());
	}
	
	public void start() {
		running = true;
		
		TCPHandlerClient tcp = new TCPHandlerClient(clientSocket, this);
		UDPHandlerClient udp = new UDPHandlerClient(this);
		
		tcp.run();
		
		log.log(EventType.START, "Starting UDP handler");
		udp.start();
	}

	public static void main(String args[]) {
		String c_login=args[0];
		String c_pass=args[1];
		String ip = args[2];
		try {
			Client client = new Client(c_login, c_pass, ip, Serveur.DEFAULT_PORT_TCP);
			client.printRecap();
			client.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String ip) {
		this.serverIp = ip;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int port) {
		this.serverPort = port;
	}
	
	public boolean isRunning() {
		return running;
	}

	public ClientTimeoutHandler getTimeoutHandler() {
		return timeoutHandler;
	}
	
	public long getTimeoutTime() {
		return timeout;
	}
	
	public Map<String, InetAddress> getClientIps() {
		return clientIps;
	}
	
	public void setClientIps(Map<String, InetAddress> o)
	{
		this.clientIps=o;
	}
	
	public int getUDPMainListeningPort() {
		return mainUDPListeningPort;
	}

	public void setUDPMainListeningPort(int listeningPort) {
		this.mainUDPListeningPort = listeningPort;
	}

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

}
