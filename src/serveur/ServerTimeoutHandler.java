package serveur;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ServerTimeoutHandler extends Thread {
	
	private Map<InetAddress, Date> timeoutTable;
	private Serveur serveur;
	
	public ServerTimeoutHandler(Serveur serveur) {
		this.serveur = serveur;
		timeoutTable = new HashMap<InetAddress, Date>();
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
					removeClient(ip);
				}
			}
		}
	}

}
