package serveur;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;

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

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
