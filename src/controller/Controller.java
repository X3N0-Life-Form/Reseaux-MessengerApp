package controller;

import view.View;
import client.Client;

/**
 * Controller classes link {@link View} classes to the {@link Client} engine.
 * It processes input from its View and transmits it to the Client, and then
 * updates the View accordingly.
 * @author etudiant
 * @see View
 * @see Client
 */
public abstract class Controller {

	protected Client client;
	protected View view;
	
	/**
	 * 
	 * @return Client linked to this Controller.
	 */
	public Client getClient() {
		return client;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	/**
	 * 
	 * @return View linked to this Controller.
	 */
	public View getView() {
		return view;
	}
	
	public void setView(View view) {
		this.view = view;
	}
}
