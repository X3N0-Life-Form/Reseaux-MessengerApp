package common;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.jdom2.JDOMException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import common.handling.HandlingException;
import common.logging.EventType;
import client.Client;
import client.handling.TCPHandlerClient;
import client.handling.UDPClient;
import server.Server;

/**
 * Tests high level connection management features.
 * @author etudiant
 *
 */
public class ConnectionManagement {
	
	
	private Client client;
	private TCPHandlerClient tcpHandlerClient;
	private UDPClient udpClient;
	
	private Client client_2;
	private TCPHandlerClient tcpHandlerClient_2;
	
	private Server server;
	private Thread serverThread;
	@SuppressWarnings("unused")
	private Thread clientThread;
	private static int serverPort;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		serverPort = CommonConstants.DEFAULT_SERVER_PORT_TCP;
	}

	@Before
	public void setUp() throws Exception {
		server = new Server(++serverPort);
		client = new Client("test01", "test", "localhost", serverPort);
		serverThread = new Thread(server);
		clientThread = new Thread(client);
		
		client.tryToConnect(false);
		client.setupHandlers();
		serverThread.start();
		//clientThread.start();
		
		tcpHandlerClient = client.getTcpHandlerClient();
		udpClient = client.getUdpClient();
	}

	@After
	public void tearDown() throws Exception {
		server.stop();
	}

	/**
	 * Clients shouldn't be able to connect twice.
	 * Manual test result (10/12/13 - 14h33): OK
	 * @throws IOException
	 * @throws JDOMException
	 * @throws InterruptedException
	 */
	@Test
	public void testClientConnectsTwice() throws IOException, JDOMException, InterruptedException {
		Message connectMessage = new Message(MessageType.CONNECT);
		connectMessage.addInfo(MessageInfoStrings.LOGIN, "test01");
		connectMessage.addInfo(MessageInfoStrings.PASSWORD, "test");
		Socket socket = client.getClientSocket();
		
		socket.connect(new InetSocketAddress(client.getServerIp(), serverPort), 10000);//copied from TCPHandlerClient.run()
		
		tcpHandlerClient.sendMessage(connectMessage, socket);
		Thread.sleep(100);
		assertFalse(server.getLog().getEventsByType(EventType.RECEIVE_TCP).isEmpty());
		assertTrue(server.getClientIps().containsKey("test01"));
		
		//Note: copy-paste client_2 stuff from here
		client_2 = new Client("test01", "test", "localhost", serverPort);
		client_2.setupHandlers();
		tcpHandlerClient_2 = client_2.getTcpHandlerClient();
		Socket socket_2 = client_2.getClientSocket();
		socket_2.connect(new InetSocketAddress(client_2.getServerIp(), serverPort), 10000);
		tcpHandlerClient_2.sendMessage(connectMessage, socket_2);
		
		//not working; for now, manually check standard output
		//assertFalse(server.getLog().getEventsByType(EventType.ERROR).isEmpty());
		//assertFalse(client_2.getLog().getEventsByType(EventType.ERROR).isEmpty());
	}
	
	/**
	 * A client must be able to disconnect and reconnect without issues.
	 * Manual test result (10/12/13 17h11): FAILED
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws HandlingException
	 * @throws ClassNotFoundException
	 */
	@Test
	public void testClientDisconnectsThenReconnects() throws IOException, InterruptedException, HandlingException, ClassNotFoundException {
		Message connectMessage = new Message(MessageType.CONNECT);
		connectMessage.addInfo(MessageInfoStrings.LOGIN, "test01");
		connectMessage.addInfo(MessageInfoStrings.PASSWORD, "test");
		Socket socket = client.getClientSocket();
		
		socket.connect(new InetSocketAddress(client.getServerIp(), serverPort), 10000);//copied from TCPHandlerClient.run()
		
		tcpHandlerClient.sendMessage(connectMessage, socket);
		Thread.sleep(100);
		assertFalse(server.getLog().getEventsByType(EventType.RECEIVE_TCP).isEmpty());
		assertTrue(server.getClientIps().containsKey("test01"));
		
		Message disconnectMessage = new Message(MessageType.DISCONNECT);
		disconnectMessage.addInfo(MessageInfoStrings.LOGIN, "test01");
		udpClient.getSend().sendMessage(disconnectMessage, udpClient.getSend().getSocket());
		Thread.sleep(100);
		assertFalse(server.getClientIps().containsKey("test01"));
		
		client_2 = new Client("test01", "test", "localhost", serverPort);
		client_2.setupHandlers();
		tcpHandlerClient_2 = client_2.getTcpHandlerClient();
		Socket socket_2 = client_2.getClientSocket();
		socket_2.connect(new InetSocketAddress(client_2.getServerIp(), serverPort), 10000);
		tcpHandlerClient_2.sendMessage(connectMessage, socket_2);
		Thread.sleep(100);
		assertTrue(server.getClientIps().containsKey("test01"));
	}

}
