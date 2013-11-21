package vue;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class ContactListWindow extends JPanel implements ActionListener {
	
	private JButton disconnectButton = new JButton("disconnect");
	private JScrollPane scrollPane = new JScrollPane();
	private JList loginList = new JList();
	private Map<String, InetAddress> clientIps = new HashMap<String, InetAddress>();
	private Vector<String> logins = new Vector<String>();
	private JFrame cadre = new javax.swing.JFrame("Liste des amis connectés : ");
	
	public ContactListWindow(){
		logins.add("test");
		logins.add("testBis");
	}
	
	public void refresh(Map<String, InetAddress> listeClient) throws IOException {
		for (String login : listeClient.keySet()) {
			logins.add(login);
		}
		lancerAffichage();
	}
	
	public void lancerAffichage() throws IOException
	{
		disconnectButton.addActionListener(this);
		loginList.setListData(logins);
		
		JPanel panneauListeContact = new JPanel();
		panneauListeContact.setLayout(new BorderLayout());
		panneauListeContact.add(loginList, BorderLayout.NORTH);
		
		scrollPane.getViewport().setView(loginList);
		
		panneauListeContact.add(scrollPane, BorderLayout.CENTER);
		panneauListeContact.setBorder(new EmptyBorder(5,5,10,15));
		
		JPanel panneauPrincipal = new JPanel();
		panneauPrincipal.setLayout(new BorderLayout());
		panneauPrincipal.add(panneauListeContact, BorderLayout.CENTER);
		panneauPrincipal.add(disconnectButton, BorderLayout.SOUTH);
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
			cadre.setVisible(false);
			LoginWindow lw = new LoginWindow();
			try {
				lw.lancerAffichage();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
