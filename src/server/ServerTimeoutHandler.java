package server;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import common.logging.EventType;
import common.logging.Log;

/**
 * This class handles client timeouts, keeping track of the date at which each client's
 * message was received. Clients will be removed from the timeout table according to the
 * Server's timeout time.
 * @author etudiant
 * @see Server
 */
public class ServerTimeoutHandler extends Thread {
	
	private Map<InetAddress, Date> timeoutTable;
	private Server serveur;
	private Log log;
	
	/**
	 * Constructs a TimeoutHandler for the specified Server with an empty timeout table.
	 * <br />Note: the thread must be started by the Server.
	 * @param serveur
	 */
	public ServerTimeoutHandler(Server serveur) {
		this.serveur = serveur;
		timeoutTable = new HashMap<InetAddress, Date>();
		log = serveur.getLog();
	}
	
	public Map<InetAddress, Date> getTimeoutTable() {
		return timeoutTable;
	}

	public void setTimeoutTable(Map<InetAddress, Date> timeoutTable) {
		this.timeoutTable = timeoutTable;
	}

	public Server getServeur() {
		return serveur;
	}

	/**
	 * Synchronized method adding a client to the timeout table,
	 * with its time stamp set to the current time.
	 * @param ip - Client's IP address.
	 */
	public synchronized void addClient(InetAddress ip) {
		timeoutTable.put(ip, new Date());
	}
	
	/**
	 * Synchronized method removing the specified client from the
	 * timeout table.
	 * @param ip - Client's IP address.
	 */
	public synchronized void removeClient(InetAddress ip) {
		timeoutTable.remove(ip);
	}
	
	/**
	 * Updates a client's time stamp.
	 * @param ip - Client's IP address.
	 * @param time - When the last message from this client was received.
	 */
	public synchronized void updateClient(InetAddress ip, Date time) {
		if (timeoutTable.containsKey(ip)) {
			timeoutTable.put(ip, time);
		}
	}

	@Override
	public void run() {
		while (serveur.isRunning()) {
			for (InetAddress ip : timeoutTable.keySet()) {
				Date now = new Date();
				if ((now.getTime() - timeoutTable.get(ip).getTime()) > serveur.getTimeoutTime()) {
					//log.log(EventType.TIMEOUT, "Client timed out: "	+ timeoutTable.get(ip) + "(" + ip + ")");
					removeClient(ip);
				}
			}
		}
	}

}
