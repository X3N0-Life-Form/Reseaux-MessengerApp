package vue;

import java.awt.BorderLayout;
import java.awt.CheckboxGroup;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

public class ContactListWindow extends JPanel implements ActionListener, MouseListener {
	
	private JButton disconnectButton = new JButton("Disconnect");
	private JButton multiChatButton = new JButton("Multi-Chat");
	private JScrollPane scrollPane = new JScrollPane();
	private JList loginList = new JList();
	private Map<String, InetAddress> clientIps = new HashMap<String, InetAddress>();
	private Vector<String> logins = new Vector<String>();
	private JFrame cadre = new javax.swing.JFrame("Liste des amis connectés : ");
	private Map<String, ChatPanel> discMap = new HashMap<String, ChatPanel>();
	
	public ContactListWindow(){
		for (int i = 0; i != 40; i++) {
			logins.add("test" + i);
		}
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
		multiChatButton.addActionListener(this);
		loginList.setListData(logins);
		
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
			for(String login : discMap.keySet()){
				discMap.get(login).getFrame().dispose();	
			}
			discMap.clear();
			System.out.println("verif si la map est vide: "+discMap);
			cadre.setVisible(false);
			LoginWindow lw = new LoginWindow();
			try {
				lw.lancerAffichage();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if (e.getSource() == multiChatButton) {
			SelectMultiContactWindow smlw = new SelectMultiContactWindow(logins, discMap);
			try {
				smlw.lancerAffichage();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		String login = (String) loginList.getSelectedValue();
		if (!discMap.containsKey(login)) {
			ChatPanel p1 = new ChatPanel(login, discMap);
			discMap.put(login, new ChatPanel(login, discMap));
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

}
