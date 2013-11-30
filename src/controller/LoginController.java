package controller;

import java.io.IOException;

import common.Message;

import view.LoginWindow;
import client.Client;

public class LoginController {
	
	private LoginWindow loginWindow;
	private Client client;
	private boolean stopWaiting = false;
	
	public LoginController(LoginWindow window) {
		this.loginWindow = window;
		loginWindow.setController(this);
	}
	
	public ProcessResult processLogin(String login, String pass, String ipServer) {
		ProcessResult res = new ProcessResult();
		if (login.isEmpty() || pass.isEmpty() || ipServer.isEmpty()) {
			res.setOk(false);
			res.setMessage("Veuillez remplir tous les champs.");
			return res;
		} else {
			try {
				client = new Client(login, pass, ipServer, 8001);
				client.setLoginController(this);
				client.start();
				while (!stopWaiting) {
					
				}
				loginWindow.createContactList(client.getClientLogins());
			} catch (IOException e) {
				e.printStackTrace();
			}
			res.setOk(true);
		}
		
		return res;
	}

	public void fireErrorMessage(Message message) {
		// TODO Auto-generated method stub
		
	}

	public synchronized void validated() {
		stopWaiting  = true;
	}

	public Client getClient() {
		return client;
	}

}
