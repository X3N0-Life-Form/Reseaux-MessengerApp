package client;

import java.net.InetAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ClientTimeoutHandler implements Runnable { //TODO: mettre ça dans une classe commune + extraire interface de timeout de serveur et client
	
	private Map<InetAddress, Date> timeoutTable;
	private Client client;
	
	public ClientTimeoutHandler(Client client) {
		timeoutTable = new HashMap<InetAddress, Date>();
	}
	
	public synchronized void addClient(InetAddress ip) {
		timeoutTable.put(ip, new Date());
	}
	
	public synchronized void removeClient(InetAddress ip) {
		timeoutTable.remove(ip);
	}

	public synchronized void updateClient(InetAddress ip, Date time) {
		timeoutTable.put(ip, time);
	}

	@Override
	public void run() {
		while (client.isRunning()) {
			for (InetAddress ip : timeoutTable.keySet()) {
				Date now = new Date();
				if ((now.getTime() - timeoutTable.get(ip).getTime()) > client.getTimeoutTime()) {
					removeClient(ip);
				}
			}
		}
	}

}
