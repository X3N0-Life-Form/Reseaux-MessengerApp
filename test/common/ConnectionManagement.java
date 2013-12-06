package common;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import client.Client;
import server.Server;

/**
 * Tests high level connection management features.
 * @author etudiant
 *
 */
public class ConnectionManagement {
	
	private Server server;
	private Client client;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

	@Test
	public void testClientConnectsTwice() {
	
	
	}
	
	@Ignore
	@Test
	public void testClientDisconnectsThenReconnects() {
	
	}

}
