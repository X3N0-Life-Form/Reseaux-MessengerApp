package controller;

public class ProcessResult {
	
	private boolean isOk;
	private String message;

	public boolean isOk() {
		return isOk;
	}
	
	public void setOk(boolean ok) {
		isOk = ok;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

}
