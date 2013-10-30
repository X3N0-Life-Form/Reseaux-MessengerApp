package serveur;

import static org.junit.Assert.*;

import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ServeurTests_TCPMessageHandling {
	
	private Serveur serveur;
	
	private static Socket clientSocket;
	private static int port;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		port = Serveur.DEFAULT_PORT;
		clientSocket = new Socket();
	}

	@Before
	public void setUp() throws Exception {
		serveur = new Serveur(port++);
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
