package view;

import java.io.IOException;

import controller.Controller;
import controller.LoginController;

/**
 * View classes are linked to a Controller, which processes the View's input
 * and calls the right methods on the Client.
 * @author etudiant
 * @see Controller
 */
public interface View {

	/**
	 * Displays this View.
	 */
	public void lancerAffichage() throws IOException;
	
	/**
	 * 
	 * @return Controller linked to this View.
	 */
	public Controller getController();

	/**
	 * Sets the Controller linked to this View.
	 * @param controller
	 */
	void setController(LoginController controller);
}
