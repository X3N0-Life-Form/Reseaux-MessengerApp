package commun;

public class Message {
	
	private String login;
	private String pass = null;
	private String message;
	private MessageType type;
	
	public Message(String login, String message, MessageType type) {
		this.login = login;
		this.message = message;
		this.type = type;
	}

	public String getPass() {
		return pass;
	}
	
	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getLogin() {
		return login;
	}

	public MessageType getType() {
		return type;
	}
	
	public String getMessage() {
		return message;
	}

}
