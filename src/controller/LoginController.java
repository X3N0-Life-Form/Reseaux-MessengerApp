package controller;

import java.io.IOException;

import common.Message;
import common.MessageType;

import view.ChatMain;
import view.LoginWindow;
import client.Client;

public class LoginController {
	
	private LoginWindow loginWindow;
	private Client client;
	
	public LoginController(LoginWindow window) {
		this.loginWindow = window;
		loginWindow.setController(this);
	}
	
	public void processLogin(String login, String pass, String ipServer) {
		if (login.isEmpty() || pass.isEmpty() || ipServer.isEmpty()) {
			fireErrorMessage(new Message(MessageType.ERROR, "Veuillez remplir tous les champs."));
		} else {
			try {
				client = new Client(login, pass, ipServer, 8001);
				client.setLoginController(this);
				
				ContactListController clc = new ContactListController(ChatMain.clw, client);
				ChatMain.clw.setController(clc);
				
				client.start();
				if (client.isConnected()) {
					loginWindow.createContactList(client.getClientLogins(), client);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void fireErrorMessage(Message message) {
		loginWindow.fireErrorMessage(message);
	}

	public Client getClient() {
		return client;
	}

	public LoginWindow getLoginWindow() {
		return loginWindow;
	}

	public void setLoginWindow(LoginWindow loginWindow) {
		this.loginWindow = loginWindow;
	}

}
