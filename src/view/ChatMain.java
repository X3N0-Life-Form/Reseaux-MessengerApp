package view;

import java.io.IOException;

import controller.LoginController;

/**
 * Class launching the messenger application (client-side).
 * @author etudiant
 *
 */
public class ChatMain {
	
	/**
	 * ContactListWindow shared by everyone.
	 */
	public static ContactListWindow clw = new ContactListWindow();
	
	/**
	 * Takes no arguments.
	 * @param arg
	 * @throws IOException
	 */
	public static void main(String[] arg) throws IOException
	{
		LoginWindow window = new LoginWindow();
		@SuppressWarnings("unused") // yes, it is used.
		LoginController lc = new LoginController(window);
		window.lancerAffichage();
	}

}
