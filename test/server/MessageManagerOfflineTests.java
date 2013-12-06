package server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import server.ServerMessageManager;
import server.Server;
import server.handling.TCPHandlerServer;
import server.handling.UDPHandlerServer;
import common.Message;
import common.MessageType;
import common.handling.HandlingException;

public class MessageManagerOfflineTests {
	
	class DatagramReciever implements Runnable {
		
		DatagramPacket packet;
		DatagramSocket socket;
		boolean stop;
		
		DatagramReciever(DatagramSocket socket) {
			this.socket = socket;
			packet = new DatagramPacket(new byte[500], 500);
			stop = false;
		}

		@Override
		public void run() {
			while (!stop) {
				try {
					socket.receive(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	private Server serveur;
	private UDPHandlerServer udpHandler;
	private ServerMessageManager udpMessageManager;
	private TCPHandlerServer tcpHandler;
	private ServerMessageManager tcpMessageManager;
	private Socket clientSocket;
	private DatagramSocket datagramSocket;
	
	private static int port;
	private DatagramReciever datagramReciever;
	private DatagramPacket datagramPacket;
	
	
	@BeforeClass
	public static void setupBeforeClass() {
		 port = Server.DEFAULT_PORT_TCP;
	}
	
	@Before
	public void setup() throws IOException {
		serveur = new Server(port++);
		clientSocket = new Socket();
		datagramSocket = new DatagramSocket();
		udpHandler = serveur.getUDPHandler();
		udpMessageManager = new ServerMessageManager(serveur, udpHandler);
		tcpHandler = new TCPHandlerServer(clientSocket, serveur);
		tcpMessageManager = new ServerMessageManager(serveur, tcpHandler);
	}

	@After
	public void teardown() {
		serveur.stop();
	}
	
	@Test(expected=HandlingException.class)
	public void test_giveDatagramToTCP() throws HandlingException, IOException, ClassNotFoundException {
		tcpMessageManager.handleMessage(new Message(MessageType.OK), datagramSocket, datagramPacket);
	}

	@Test(expected=HandlingException.class)
	public void test_giveSocketToUDP() throws HandlingException, IOException {
		udpMessageManager.handleMessage(new Message(MessageType.OK), clientSocket);
	}
	
	@Ignore
	@Test
	public void testUDP_requestListWithoutValidLogin() throws HandlingException, InterruptedException, IOException, ClassNotFoundException {
		Message message = new Message(MessageType.REQUEST_LIST);
		message.addInfo("login", "invalid");
		datagramReciever = new DatagramReciever(datagramSocket);
		Thread t = new Thread(datagramReciever);
		t.start();
		//udpMessageManager.handleMessage(message, datagramSocket);
		datagramReciever.stop = true;
		t.join();
		Message answer = udpHandler.getMessage(datagramReciever.packet);
		assertTrue(answer.getType() == MessageType.ERROR);
	}
	
	@Ignore
	@Test
	public void testTCP_authenticate_valid() throws IOException, HandlingException {
		Message message = new Message(MessageType.CONNECT);
		message.addInfo("login", "test01");
		message.addInfo("pass", "test");
		tcpMessageManager.handleMessage(message, clientSocket);
		
	}
	
	@Ignore
	@Test
	public void testTCP_authenticate_invalid() {
		
	}
	
	@Ignore
	@Test
	public void testTCP_authenticate_missingArg() {
		
	}
}
