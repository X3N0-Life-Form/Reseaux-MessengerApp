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
	public static LoginWindow lw = new LoginWindow();
	
	/**
	 * Takes no arguments.
	 * @param arg
	 * @throws IOException
	 */
	public static void main(String[] arg) throws IOException
	{
		LoginController lc = new LoginController(lw);
		lw.lancerAffichage();
	}

}
