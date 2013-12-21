package common;

/**
 * Contains constants used by both Servers and Clients.
 * @author etudiant
 *
 */
public class CommonConstants {
	public static final int UDP_PACKET_SIZE = 10000;
	public static final int DEFAULT_SERVER_PORT = 8001;
	public static final long SLEEP_SMALL = 100;
	public static final int CLIENT_CONNECT_TIMEOUT = 5000;

	/**
	 * Do not instantiate.
	 */
	private CommonConstants() {}
	
	/**
	 * Extracts the port number off an IP string
	 * @param ipAddress - IPv4 address with port (ex: 127.0.0.1:8050)
	 * @return port number (ex: 8050)
	 */
	public static int extractPort(String ipAddress) {
		try {
			if (ipAddress.lastIndexOf(":") >= 0) {
				String string = ipAddress.substring(ipAddress.lastIndexOf(":") + 1);
				return Integer.parseInt(string);
			} else {
				return -1;
			}
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * Trims the port off an IPv4
	 * @param ipServer
	 * @return The provided IP minus the port (if it has one).
	 */
	public static String trimPort(String ipServer) {
		if (ipServer.lastIndexOf(":") >= 0) {
			return ipServer.substring(0, ipServer.lastIndexOf(":"));
		} else {
			return ipServer;
		}
	}
}
