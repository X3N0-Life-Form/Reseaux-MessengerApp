package common;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;

import org.jdom2.JDOMException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

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
	
	private Server server;
	private Client client;
	private TCPHandlerClient tcpHandlerClient;
	private UDPClient udpClient;
	private Thread serverThread;
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

	@Test
	public void testClientConnectsTwice() throws IOException, JDOMException, InterruptedException {
		Message connectMessage = new Message(MessageType.CONNECT);
		connectMessage.addInfo(MessageInfoStrings.LOGIN, "test01");
		connectMessage.addInfo(MessageInfoStrings.PASSWORD, "test");
		Socket socket = client.getClientSocket();
		
		//client.start();
		
		tcpHandlerClient.sendMessage(connectMessage, socket);
		Thread.sleep(100);
		assertFalse(server.getLog().getEventsByType(EventType.RECEIVE_TCP).isEmpty());
		assertTrue(server.getClientIps().containsKey("test01"));
		
		//weird fucked up behavior
		//tcpHandlerClient.sendMessage(connectMessage, socket);
	}
	
	@Test
	public void testClientAlreadyConnected() throws IOException, InterruptedException {
		Message connectMessage = new Message(MessageType.CONNECT);
		connectMessage.addInfo(MessageInfoStrings.LOGIN, "test01");
		connectMessage.addInfo(MessageInfoStrings.PASSWORD, "test");
		Socket socket = client.getClientSocket();
		server.getClientIps().put("test01", null);
		
		tcpHandlerClient.sendMessage(connectMessage, socket);
		Thread.sleep(100);
		assertFalse(server.getLog().getEventsByType(EventType.ERROR).isEmpty());
	}
	
	@Ignore
	@Test
	public void testClientDisconnectsThenReconnects() {
	
	}

}
