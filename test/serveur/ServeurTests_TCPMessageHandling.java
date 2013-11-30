package serveur;

import static org.junit.Assert.*;

import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.Server;

@SuppressWarnings("unused")
public class ServeurTests_TCPMessageHandling {
	
	private Server serveur;
	
	private static Socket clientSocket;
	private static int port;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		port = Server.DEFAULT_PORT_TCP;
		clientSocket = new Socket();
	}

	@Before
	public void setUp() throws Exception {
		serveur = new Server(port++);
		serveur.start();
	}

	@After
	public void tearDown() throws Exception {
		serveur.stop();
	}

	@Test
	public void test() {
		
	}

}
