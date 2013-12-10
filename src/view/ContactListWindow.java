package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import client.Client;
import controller.ContactListController;

public class ContactListWindow extends JPanel implements ActionListener, MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1654591401132479730L;
	private JButton disconnectButton = new JButton("Disconnect");
	private JButton multiChatButton = new JButton("Multi-Chat");
	private JScrollPane scrollPane = new JScrollPane();
	private JList loginList = new JList();
	private List<String> logins = new Vector<String>();
	private JFrame cadre = new javax.swing.JFrame("Liste des amis connectés : ");
	private Map<String, ChatPanel> discMap = new HashMap<String, ChatPanel>();

	private ContactListController controller;
	
	public void setLogins(List<String> logins) {
		this.logins = logins;
	}
	
	public void refresh(List<String> listeClient, Client client) throws IOException {
		listeClient.remove(client.getLogin());
		loginList.removeAll();
		loginList.setListData(listeClient.toArray());
		cadre.validate();
	}
	
	
	public void lancerAffichage() throws IOException
	{
		disconnectButton.addActionListener(this);
		multiChatButton.addActionListener(this);
		loginList.setListData(logins.toArray());
		
		loginList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		loginList.addMouseListener(this);
		
		JPanel panneauListeContact = new JPanel();
		panneauListeContact.setLayout(new BorderLayout());
		panneauListeContact.add(loginList, BorderLayout.NORTH);
		
		scrollPane.getViewport().setView(loginList);
		
		panneauListeContact.add(scrollPane, BorderLayout.CENTER);
		panneauListeContact.setBorder(new EmptyBorder(5,5,10,15));
		
		JPanel panneauAction = new JPanel();
		panneauAction.setLayout(new BorderLayout());
		panneauAction.add(disconnectButton, BorderLayout.WEST);
		panneauAction.add(multiChatButton, BorderLayout.EAST);
		panneauAction.setBorder(new EmptyBorder(0,30,10,30));
		
		JPanel panneauPrincipal = new JPanel();
		panneauPrincipal.setLayout(new BorderLayout());
		panneauPrincipal.add(panneauListeContact, BorderLayout.CENTER);
		panneauPrincipal.add(panneauAction, BorderLayout.SOUTH);
		panneauListeContact.setBorder(new EmptyBorder(10,10,10,10));
		
		cadre.setContentPane(panneauPrincipal);
		cadre.setLocation(600, 200);
		cadre.pack();
		cadre.setResizable(false);
		cadre.setSize(300, 400);
		cadre.setVisible(true);
		cadre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == disconnectButton)
		{
			disconnect();
		} else if (e.getSource() == multiChatButton) {
			SelectMultiContactWindow smlw = new SelectMultiContactWindow(logins, discMap, controller);
			try {
				smlw.lancerAffichage();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Go back to the login window.
	 */
	public void disconnect() {
		for(String login : discMap.keySet()){
			discMap.get(login).getFrame().dispose();	
		}
		discMap.clear();
		System.out.println("verif si la map est vide: "+discMap);
		cadre.setVisible(false);
		LoginWindow lw = new LoginWindow();
		
		controller.disconnect();
		
		try {
			lw.lancerAffichage();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		String login = (String) loginList.getSelectedValue();
		if (login != null) {
			if (!discMap.containsKey(login)) {
				ChatPanel p1 = new ChatPanel(login, discMap, controller);
				discMap.put(login, p1);
				try {
					p1.lancerAffichage();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				ChatPanel p = discMap.get(login);
				p.getFrame().toFront();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void setController(ContactListController clc) {
		this.controller = clc;
	}
	
	public Map<String, ChatPanel> getDiscMap() {
		return discMap;
	}

	public void setDiscMap(Map<String, ChatPanel> discMap) {
		this.discMap = discMap;
	}

}
