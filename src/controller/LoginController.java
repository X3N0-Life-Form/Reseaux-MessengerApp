package controller;

import java.io.IOException;

import view.ChatMain;
import view.LoginWindow;
import client.Client;

import common.CommonConstants;
import common.Message;
import common.MessageType;

/**
 * Controller linked to a LoginWindow.
 * @author etudiant
 * @see LoginWindow
 *
 */
public class LoginController extends Controller {
	
	private LoginWindow loginWindow;
	
	/**
	 * Constructs a LoginController linked to the specified {@link LoginWindow}.
	 * Also sets itself as the LoginWindow's Controller.
	 * @param window - LoginWindow.
	 */
	public LoginController(LoginWindow window) {
		this.loginWindow = window;
		view = loginWindow;
		loginWindow.setController(this);
	}
	
	/**
	 * Processes login information, uses it to construct a {@link Client}, then starts
	 * it, sending the information to the specified Server.
	 * @param login - Fires an error message if empty.
	 * @param pass - Fires an error message if empty.
	 * @param ipServer - Fires an error message if empty.
	 */
	public void processLogin(String login, String pass, String ipServer) {
		if (login.isEmpty() || pass.isEmpty() || ipServer.isEmpty()) {
			fireErrorMessage(new Message(MessageType.ERROR, "Please fill up each field."));
		} else {
			try {
				int port = CommonConstants.extractPort(ipServer);
				if (port == -1) {
					port = CommonConstants.DEFAULT_SERVER_PORT;
				}
				ipServer = CommonConstants.trimPort(ipServer);
				client = new Client(login, pass, ipServer, port);
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

	/**
	 * Calls {@link LoginWindow}'s fireErrorMessage method.
	 * @param message - Error Message.
	 */
	public void fireErrorMessage(Message message) {
		loginWindow.fireErrorMessage(message);
	}

	/**
	 * Functionally identical to getView().
	 * @return The controller's LoginWindow.
	 */
	public LoginWindow getLoginWindow() {
		return loginWindow;
	}

	/**
	 * Sets this controller's View and LoginWindow.
	 * @param loginWindow
	 */
	public void setLoginWindow(LoginWindow loginWindow) {
		view = loginWindow;
		this.loginWindow = loginWindow;
	}

}
