package serveur;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import org.junit.Before;
import org.junit.Test;

import commun.Message;
import commun.MessageType;

import serveur.handling.HandlingException;
import serveur.handling.TCPHandler;
import serveur.handling.UDPHandler;

public class MessageManagerOfflineTests {
	
	private Serveur serveur;
	private UDPHandler udpHandler;
	private ServerMessageManager udpMessageManager;
	private TCPHandler tcpHandler;
	private ServerMessageManager tcpMessageManager;
	private Socket clientSocket;
	
	@Before
	public void setup() throws IOException {
		serveur = new Serveur();
		clientSocket = new Socket();
		udpHandler = new UDPHandler(serveur);
		udpMessageManager = new ServerMessageManager(serveur, udpHandler);
		tcpHandler = new TCPHandler(clientSocket, serveur);
		tcpMessageManager = new ServerMessageManager(serveur, tcpHandler);
	}

	@Test(expected=HandlingException.class)
	public void test_giveDatagramToTCP() throws SocketException, HandlingException {
		tcpMessageManager.handleMessage(new Message(MessageType.OK), new DatagramSocket());
	}

	@Test(expected=HandlingException.class)
	public void test_giveSocketToUDP() throws HandlingException, IOException {
		udpMessageManager.handleMessage(new Message(MessageType.OK), clientSocket);
	}
}
