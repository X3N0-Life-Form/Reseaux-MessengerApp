package server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.jdom2.JDOMException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import server.ServerMessageManager;
import server.Server;
import server.handling.TCPHandlerServer;
import server.handling.UDPHandlerServer;
import common.CommonConstants;
import common.Message;
import common.MessageInfoStrings;
import common.MessageType;
import common.handling.HandlingException;

public class MessageManagerOfflineTests {
	
	private Server server;
	private UDPHandlerServer udpHandler;
	private ServerMessageManager udpMessageManager;
	private TCPHandlerServer tcpHandler;
	private ServerMessageManager tcpMessageManager;
	private Socket clientSocket;
	private DatagramSocket datagramSocket;
	private Thread serverThread;
	
	private static int serverPort;
	private DatagramPacket datagramPacket;
	
	
	@BeforeClass
	public static void setupBeforeClass() {
		 serverPort = CommonConstants.DEFAULT_SERVER_PORT;
	}
	
	@Before
	public void setup() throws IOException, JDOMException {
		server = new Server(++serverPort);
		serverThread = new Thread(server);
		clientSocket = new Socket();
		datagramSocket = new DatagramSocket();
		udpHandler = server.getUDPHandler();
		udpMessageManager = new ServerMessageManager(server, udpHandler);
		tcpHandler = new TCPHandlerServer(clientSocket, server);
		tcpMessageManager = new ServerMessageManager(server, tcpHandler);
		
		clientSocket.connect(new InetSocketAddress("localhost", serverPort), 10000);
	}

	@After
	public void teardown() {
		server.stop();
	}
	
	@Test(expected=HandlingException.class)
	public void test_giveDatagramToTCP() throws HandlingException, IOException, ClassNotFoundException {
		tcpMessageManager.handleMessage(new Message(MessageType.OK), datagramSocket, datagramPacket);
	}

	@Test(expected=HandlingException.class)
	public void test_giveSocketToUDP() throws HandlingException, IOException {
		udpMessageManager.handleMessage(new Message(MessageType.OK), clientSocket);
	}
	

	@Test
	public void testUDP_requestListWithoutValidLogin() throws HandlingException, InterruptedException, IOException, ClassNotFoundException {
		Message message = new Message(MessageType.REQUEST_LIST);
		message.addInfo(MessageInfoStrings.LOGIN, "invalid");
		message.addInfo(MessageInfoStrings.PASSWORD, "not important");
		serverThread.start();
		Thread.sleep(100);
		udpMessageManager.handleMessage(message, datagramSocket, datagramPacket);;
		Thread.sleep(100);
		assertFalse(server.getClientIps().containsKey("test01"));
	}
	
	/**
	 * Note: throws an handling exception (the server probably sends OK back to himself).
	 * @throws IOException
	 * @throws HandlingException
	 * @throws InterruptedException
	 * @throws JDOMException
	 */
	@Test
	public void testTCP_authenticate_valid() throws IOException, HandlingException, InterruptedException, JDOMException {
		Message message = new Message(MessageType.CONNECT);
		message.addInfo(MessageInfoStrings.LOGIN, "test01");
		message.addInfo(MessageInfoStrings.PASSWORD, "test");
		serverThread.start();
		Thread.sleep(100);
		tcpMessageManager.handleMessage(message, clientSocket);
		Thread.sleep(100);
		assertTrue(server.getClientIps().containsKey("test01"));
	}
	
	/**
	 * Note: copied from above test.
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws HandlingException
	 */
	@Test
	public void testTCP_authenticate_invalid() throws InterruptedException, IOException, HandlingException {
		Message message = new Message(MessageType.CONNECT);
		message.addInfo(MessageInfoStrings.LOGIN, "test01");
		message.addInfo(MessageInfoStrings.PASSWORD, "nope");
		serverThread.start();
		Thread.sleep(100);
		tcpMessageManager.handleMessage(message, clientSocket);
		Thread.sleep(100);
		assertFalse(server.getClientIps().containsKey("test01"));
	}
	
	/**
	 * Obsolete: this is handled in the LoginController.
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws HandlingException
	 */
	@Ignore
	@Deprecated
	@Test
	public void testTCP_authenticate_missingArg() throws InterruptedException, IOException, HandlingException {
		Message message = new Message(MessageType.CONNECT);
		message.addInfo(MessageInfoStrings.LOGIN, "test01");
		serverThread.start();
		Thread.sleep(100);
		tcpMessageManager.handleMessage(message, clientSocket);
		Thread.sleep(100);
		assertFalse(server.getClientIps().containsKey("test01"));
	}
}
