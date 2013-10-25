package client;

public class Client {
	private String login;
	private String ip;
	private int port;
	
	public Client(String login, String ip, int port) {
		super();
		this.login = login;
		this.ip = ip;
		this.port = port;
	}

	public static void main(String args[]) {
		
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	
}
