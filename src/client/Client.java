package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import serveur.Serveur;

import client.handling.TCPHandlerClient;
import client.handling.UDPHandlerClient;

import client.ClientTimeoutHandler;

public class Client {
	private static final long DEFAULT_TIMEOUT_TIME = 0;
	private String login;
	private String pass;
	private InetAddress ip;
	private int port;
	private Socket clientSocket;
	private boolean running;
	private Map<String, InetAddress> clientIps;
	private ClientTimeoutHandler timeoutHandler;
	private long timeout;

	
	public Client(String login, String pass, int port) throws IOException{
		super();
		this.login = login;
		this.port = port;
		this.pass = pass;
		clientSocket = new Socket("localhost", port);
		this.ip=clientSocket.getInetAddress();
		clientIps = new HashMap<String, InetAddress>();
		timeoutHandler = new ClientTimeoutHandler(this);
		running = false;
		timeout = DEFAULT_TIMEOUT_TIME;

	}
	
	public void printRecap() {
		System.out.println("Starting the client:");
		System.out.println("\tIp:\t" + clientSocket.getInetAddress());
		System.out.println("\tPort:\t" + clientSocket.getLocalPort());
	}
	
	public void start() {
		TCPHandlerClient tcp = new TCPHandlerClient(clientSocket, this);
		UDPHandlerClient udp = new UDPHandlerClient(this);
		tcp.run();
		udp.start();
	}

	public static void main(String args[]) {
		String c_login=args[0];
		String c_pass=args[1];
		try {
			Client client = new Client(c_login, c_pass, Serveur.DEFAULT_PORT_TCP);
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

	public InetAddress getIp() {
		return ip;
	}

	public void setIp(InetAddress ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
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

}
