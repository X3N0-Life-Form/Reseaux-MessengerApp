package serveur;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.ServerTimeoutHandler;
import server.Server;

public class TimeoutOfflineTests {
	
	private Server serveur;
	private ServerTimeoutHandler timeoutHandler;
	private static int port;
	
	@BeforeClass
	public static void setupBeforeClass() {
		 port = Server.DEFAULT_PORT_TCP;
	}
	
	@Before
	public void setup() throws IOException {
		serveur = new Server(port++);
		timeoutHandler = new ServerTimeoutHandler(serveur);
	}
	
	@After
	public void teardown() {
		serveur.stop();
	}

	@Test
	public void testAddClient() throws UnknownHostException {
		InetAddress localHost = InetAddress.getLocalHost();
		timeoutHandler.addClient(localHost);
		assertTrue(timeoutHandler.getTimeoutTable().containsKey(localHost));
	}

	@Test
	public void testRemoveClient() throws UnknownHostException {
		InetAddress localHost = InetAddress.getLocalHost();
		timeoutHandler.addClient(localHost);
		timeoutHandler.removeClient(localHost);
		assertFalse(timeoutHandler.getTimeoutTable().containsKey(localHost));
	}

	@Test
	public void testUpdateClient_normal() throws UnknownHostException {
		InetAddress localHost = InetAddress.getLocalHost();
		timeoutHandler.addClient(localHost);
		Date firstDate = timeoutHandler.getTimeoutTable().get(localHost);
		Date secondDate = new Date(0);
		timeoutHandler.updateClient(localHost, secondDate);
		assertFalse(timeoutHandler.getTimeoutTable().get(localHost).equals(firstDate));
		assertTrue(timeoutHandler.getTimeoutTable().get(localHost).equals(secondDate));
	}
	
	/**
	 * What happens if we try to update a client that isn't in the table? 
	 * @throws UnknownHostException
	 */
	@Test
	public void testUpdateClient_unknown() throws UnknownHostException {
		InetAddress localHost = InetAddress.getLocalHost();
		timeoutHandler.updateClient(localHost, new Date());
		assertFalse(timeoutHandler.getTimeoutTable().containsKey(localHost));
	}
	
	@Test
	public void testClientTimeout() throws UnknownHostException, InterruptedException {
		InetAddress localHost = InetAddress.getLocalHost();
		timeoutHandler.addClient(localHost);
		serveur.setTimeoutTime(1);
		serveur.setRunning(true);
		timeoutHandler.start();
		Thread.sleep(50);
		assertFalse(timeoutHandler.getTimeoutTable().containsKey(localHost));
	}

}
