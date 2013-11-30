package vue;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

public class SelectMultiContactWindow extends JPanel implements ActionListener {
	
	private Vector<JRadioButton> listRadioLog = new Vector<JRadioButton>();
	private Vector<JRadioButton> listRadioLogWithContact = new Vector<JRadioButton>();
	private JButton startButton = new JButton("START");
	private Vector<String> listLogWithContact = new Vector<String>();
	private JFrame cadre = new javax.swing.JFrame("Sélectionner le groupe d'amis : ");
	private Map<String, ChatPanel>  mapListChat;
	
	public SelectMultiContactWindow(Vector<String> lisLog, Map<String, ChatPanel> listChat) {
		mapListChat = listChat;
		for(String log : lisLog) {
			listRadioLog.add(new JRadioButton(log, false));
		}
	}
	
	public void lancerAffichage() throws IOException
	{
		JPanel panneauPrincipal = new JPanel();
		for(JRadioButton radio : listRadioLog) {
			radio.addActionListener(this);
			panneauPrincipal.add(radio);
			listLogWithContact.clear();
		}
		startButton.addActionListener(this);
		panneauPrincipal.add(startButton, BorderLayout.SOUTH);
		
		cadre.setContentPane(panneauPrincipal);
		cadre.setLocation(600, 200);
		cadre.pack();
		cadre.setResizable(false);
		cadre.setSize(300, 400);
		cadre.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for(JRadioButton button : listRadioLog) {
			if(e.getSource() == button) {
				if(listLogWithContact.contains(button.getText())) {
					listLogWithContact.remove(button.getText());
					listRadioLogWithContact.remove(button);
				} else {
					listLogWithContact.add(button.getText());
					listRadioLogWithContact.add(button);
				}
			}
		}		
		if (e.getSource() == startButton) {
			for(JRadioButton buttonWithContact : listRadioLogWithContact) {
				buttonWithContact.setSelected(false);
			}
			listRadioLogWithContact.clear();
			System.out.println("liste des amis avec qui on souhaite se connecter en groupe : " + listLogWithContact);

			String loginWithContact = "";
			for(String login : listLogWithContact) {
				loginWithContact = loginWithContact + " " + login;
			}
			listLogWithContact.clear();
			ChatPanel p1 = new ChatPanel(loginWithContact, mapListChat);
			mapListChat.put(loginWithContact, p1);
			try {
				p1.lancerAffichage();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			/*
			String login = (String) listLogWithContact.get;
			if (!discMap.containsKey(login)) {
				ChatPanel p1 = new ChatPanel(login, discMap);
				discMap.put(login, p1);
				try {
					p1.lancerAffichage();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else {
				ChatPanel p = discMap.get(login);
				p.getFrame().toFront();
			}*/
		}
	}

}
