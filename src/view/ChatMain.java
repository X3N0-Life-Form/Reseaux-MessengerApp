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
	public static ContactListWindow clw;
	public static LoginWindow lw;
	
	/**
	 * Takes no arguments.
	 * @param arg
	 * @throws IOException
	 */
	public static void main(String[] arg) throws IOException
	{
		clw = new ContactListWindow();
		lw = new LoginWindow();
		LoginController lc = new LoginController(lw);
		lw.lancerAffichage();
	}
	
	public static void demarre() throws IOException {
		clw = new ContactListWindow();
		lw = new LoginWindow();
		LoginController lc = new LoginController(lw);
		lw.lancerAffichage();
	}

}
