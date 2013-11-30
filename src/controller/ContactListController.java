package controller;

import java.io.IOException;

import view.ContactListWindow;
import client.Client;

public class ContactListController {
	
	private ContactListWindow clw;
	private Client client;

	public ContactListController(ContactListWindow clw, Client client) {
		this.clw = clw;
		this.client = client;
	}

	public void refresh() {
		try {
			clw.refresh(client.getClientLogins());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
