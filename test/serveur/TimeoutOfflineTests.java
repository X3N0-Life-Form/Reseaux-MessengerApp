package serveur;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TimeoutOfflineTests {
	
	private Serveur serveur;
	private ServerTimeoutHandler timeoutHandler;
	
	@Before
	public void setup() throws IOException {
		serveur = new Serveur();
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
