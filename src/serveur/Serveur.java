package serveur;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.jdom2.JDOMException;

import commun.logging.EventType;
import commun.logging.Log;

import serveur.handling.TCPHandler;
import serveur.handling.UDPHandler;


public class Serveur {
	private ServerSocket serverSocket;
	private Map<String, InetAddress> clientIps;
	private Map<String, String> clientPorts;

	private int port;
	private int portClient;
	private ServerTimeoutHandler timeoutHandler;
	private boolean running;
	private long timeout;
	private UDPHandler udpHandler;
	private LoginParser loginParser;
	private Log log;
	
	public static final int DEFAULT_PORT_TCP = 8001;
	public static final long DEFAULT_TIMEOUT_TIME = 5000;
	public static final String DEFAULT_LOGIN_FILE_URL = "res/clientsSample.xml";
	
	public Serveur(int port) throws IOException {
		this.port = port;
		serverSocket = new ServerSocket(port);
		clientIps = new HashMap<String, InetAddress>();
		clientPorts = new HashMap<String, String>();
		timeoutHandler = new ServerTimeoutHandler(this);
		running = false;
		timeout = DEFAULT_TIMEOUT_TIME;
		udpHandler = new UDPHandler(this);
		loginParser = new LoginParser(DEFAULT_LOGIN_FILE_URL);
		log = new Log();
	}
	
	public Serveur() throws IOException {
		this(DEFAULT_PORT_TCP);
	}
	
	public static void main(String args[]) {
		for (int i = 0; i < args.length; i++) {
			//TODO: handle args
		}
		try {
			Serveur serveur = new Serveur();
			serveur.loginParser.parse();
			serveur.printRecap();
			serveur.start();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}
	}
	
	public void printRecap() {
		log.log(EventType.INFO, "Starting the server:");
		log.log(EventType.INFO, "\tIp:\t\t" + serverSocket.getInetAddress());
		log.log(EventType.INFO, "\tPort:\t\t" + serverSocket.getLocalPort());
		log.log(EventType.INFO, "\tTimeout:\t" + timeout + " ms");
		log.log(EventType.INFO, "\tClient file:\t" + loginParser.getFile().getName());
	}

	public void start() throws JDOMException, IOException {
		running = true;
		log.log(EventType.START, "Server is runnning");
		
		log.log(EventType.START, "Starting timeout handler thread");
		timeoutHandler.start();
		
		log.log(EventType.START, "Starting UDP handler thread");
		udpHandler.start();
		
		log.log(EventType.PARSING, "Parsing client file");
		loginParser.parse();
		
		while (running) {
			try {
				Socket socket = serverSocket.accept();
				
				log.log(EventType.RECEIVE_TCP, "Recieved TCP message" + socket.getInetAddress());
				TCPHandler tcph = new TCPHandler(socket, this);
				
				log.log(EventType.START, "Starting TCP handler thread for " + socket.getInetAddress());
				tcph.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean authenticateClient(String login, String pass) {
		return loginParser.validateLogin(login, pass);
	}

	public Map<String, InetAddress> getClientIps() {
		return clientIps;
	}
	
	public Map<String, String> getClientPorts() {
		return clientPorts;
	}

	public int getPort() {
		return port;
	}
	
	public void setPort(int port)
	{
		this.port = port;
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
	
	public void setTimeoutTime(long time) {
		this.timeout = time;
	}

	public void stop() {
		running = false;
		//TODO: stop everything
	}

	/**
	 * To be used for testing purposes.
	 * @param b
	 */
	public void setRunning(boolean b) {
		running = true;
	}

	public UDPHandler getUDPHandler() {
		return udpHandler;
	}

	public Log getLog() {
		return log;
	}
}
