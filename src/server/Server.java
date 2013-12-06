package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.jdom2.JDOMException;

import common.CommonConstants;
import common.MasterClass;
import common.logging.EventType;
import common.logging.Log;
import server.handling.TCPHandlerServer;
import server.handling.UDPHandlerServer;

/**
 * Server-side master class. Contains the server's settings, data and handles the creation of every required
 * thread.
 * @author etudiant
 * 
 */
public class Server implements MasterClass {
	private ServerSocket serverSocket;
	private Map<String, InetAddress> clientIps;
	private Map<String, String> clientPorts;

	private int port;
	private ServerTimeoutHandler timeoutHandler;
	private boolean running;
	private long timeout;
	private UDPHandlerServer udpHandler;
	private LoginParser loginParser;
	private Log log;
	
	public static final long DEFAULT_TIMEOUT_TIME = 5000;
	public static final String DEFAULT_LOGIN_FILE_URL = "res/clientsSample.xml";
	
	/**
	 * Constructs a Server using the specified port.
	 * @param port - Port used by the server; must not be already in use.
	 * @throws IOException
	 */
	public Server(int port) throws IOException {
		this.port = port;
		serverSocket = new ServerSocket(port);
		clientIps = new HashMap<String, InetAddress>();
		clientPorts = new HashMap<String, String>();
		timeoutHandler = new ServerTimeoutHandler(this);
		running = false;
		timeout = DEFAULT_TIMEOUT_TIME;
		udpHandler = new UDPHandlerServer(this);
		loginParser = new LoginParser(DEFAULT_LOGIN_FILE_URL);
		log = new Log();
	}
	
	/**
	 * Constructs a Server using the default port.
	 * @throws IOException
	 */
	public Server() throws IOException {
		this(CommonConstants.DEFAULT_SERVER_PORT_TCP);
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		for (int i = 0; i < args.length; i++) {
			//TODO: handle args
		}
		try {
			Server serveur = new Server();
			serveur.loginParser.parse();
			serveur.printRecap();
			serveur.start();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JDOMException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Prints the Server's setting.
	 */
	public void printRecap() {
		log.log(EventType.INFO, "Starting the server:");
		log.log(EventType.INFO, "\tIp:\t\t" + serverSocket.getInetAddress());
		log.log(EventType.INFO, "\tPort:\t\t" + serverSocket.getLocalPort());
		log.log(EventType.INFO, "\tTimeout:\t" + timeout + " ms");
		log.log(EventType.INFO, "\tClient file:\t" + loginParser.getFile().getName());
	}

	/**
	 * Starts the server, launching the timeout and UDP handler threads, parsing
	 * the client login file and starts TCP handler threads upon receiving TCP
	 * requests.
	 * @throws JDOMException If the client login file could not be parsed.
	 * @throws IOException
	 */
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
				TCPHandlerServer tcph = new TCPHandlerServer(socket, this);
				
				log.log(EventType.START, "Starting TCP handler thread for " + socket.getInetAddress());
				tcph.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Authenticate the specified client. Note that the client's login file must
	 * have been parsed prior to calling this method.
	 * @param login - Client login.
	 * @param pass - Client password.
	 * @return True if the login and pass are valid.
	 */
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

	public UDPHandlerServer getUDPHandler() {
		return udpHandler;
	}

	public Log getLog() {
		return log;
	}
}
