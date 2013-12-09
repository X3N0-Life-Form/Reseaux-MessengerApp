package view;

import java.io.IOException;

import controller.LoginController;

public class ChatMain {
	
	public static ContactListWindow clw = new ContactListWindow();
	
	public static void main(String[] arg) throws IOException
	{
		LoginWindow window = new LoginWindow();
		@SuppressWarnings("unused")
		LoginController lc = new LoginController(window);
		window.lancerAffichage();
	}

}
