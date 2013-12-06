package server;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import common.ConnectionManagement;

import server.ServerTimeoutHandler;
import server.Server;

/**
 * Tests basic timeout operation. More complex stuff gets tested by ConnectionManagement.
 * @author etudiant
 * @see ConnectionManagement
 */
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
	
	/////////////////
	// basic tests //
	/////////////////
	
	@Test
	public void testAddClient() throws UnknownHostException {
		timeoutHandler.addClient("test01");
		assertTrue(timeoutHandler.getTimeoutTable().containsKey("test01"));
	}

	@Test
	public void testRemoveClient() throws UnknownHostException {
		timeoutHandler.addClient("test01");
		timeoutHandler.removeClient("test01");
		assertFalse(timeoutHandler.getTimeoutTable().containsKey("test01"));
	}

	@Test
	public void testUpdateClient_normal() throws UnknownHostException {
		timeoutHandler.addClient("test01");
		Date firstDate = timeoutHandler.getTimeoutTable().get("test01");
		Date secondDate = new Date(0);
		timeoutHandler.updateClient("test01", secondDate);
		assertFalse(timeoutHandler.getTimeoutTable().get("test01").equals(firstDate));
		assertTrue(timeoutHandler.getTimeoutTable().get("test01").equals(secondDate));
	}
	
	/**
	 * What happens if we try to update a client that isn't in the table? 
	 * @throws UnknownHostException
	 */
	@Test
	public void testUpdateClient_unknown() throws UnknownHostException {
		timeoutHandler.updateClient("test01", new Date());
		assertFalse(timeoutHandler.getTimeoutTable().containsKey("test01"));
	}
	
	@Test
	public void testClientTimeout() throws UnknownHostException, InterruptedException {
		timeoutHandler.addClient("test01");
		serveur.setTimeoutTime(1);
		serveur.setRunning(true);
		timeoutHandler.start();
		Thread.sleep(50);
		assertFalse(timeoutHandler.getTimeoutTable().containsKey("test01"));
	}
}
