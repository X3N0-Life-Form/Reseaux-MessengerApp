package serveur;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import commun.logging.EventType;
import commun.logging.Log;


public class ServerTimeoutHandler extends Thread {
	
	private Map<InetAddress, Date> timeoutTable;
	private Serveur serveur;
	private Log log;
	
	public ServerTimeoutHandler(Serveur serveur) {
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

	public Serveur getServeur() {
		return serveur;
	}

	public synchronized void addClient(InetAddress ip) {
		timeoutTable.put(ip, new Date());
	}
	
	public synchronized void removeClient(InetAddress ip) {
		timeoutTable.remove(ip);
	}
	
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
