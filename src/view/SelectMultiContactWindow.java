package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import controller.ContactListController;

@SuppressWarnings("serial")
public class SelectMultiContactWindow extends JPanel implements ActionListener {
	
	private JButton startButton = new JButton("START");
	private JFrame cadre = new javax.swing.JFrame("Sélectionner le groupe d'amis : ");
	private Vector<JRadioButton> listRadioLog = new Vector<JRadioButton>();
	private Vector<JRadioButton> listRadioLogWithContact = new Vector<JRadioButton>();
	
	private Map<Vector<String>, ChatPanel>  mapListChat;
	
	
	private List<String> listLogWithContact = new Vector<String>();
	private Vector<String> loginsWithMultiDiscuss = new Vector<String>();	
	private ContactListController controller;
	
	
	public SelectMultiContactWindow(List<String> logins, ContactListController controller, Map<Vector<String>, ChatPanel>  mapListChat) {
		this.mapListChat = mapListChat;
		this.listLogWithContact = logins;
		for(String log : logins) {
			listRadioLog.add(new JRadioButton(log, false));
		}
		this.controller = controller;
	}
	
	
	
	public Map<Vector<String>, ChatPanel> getMapListChat() {
		return mapListChat;
	}



	public void setMapListChat(Map<Vector<String>, ChatPanel> mapListChat) {
		this.mapListChat = mapListChat;
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
		cadre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
			for(String login : listLogWithContact) {
				loginsWithMultiDiscuss.add(login);
			}
			listLogWithContact.clear();			
			
			Collections.sort(loginsWithMultiDiscuss);
			if(mapListChat.containsKey(loginsWithMultiDiscuss)){
				ChatPanel p = mapListChat.get(loginsWithMultiDiscuss);
				p.getFrame().toFront();
				loginsWithMultiDiscuss.clear();
			}else {
				ChatPanel p1 = new ChatPanel(new Vector<String>(loginsWithMultiDiscuss), mapListChat, controller);
				//mapListChat.put(loginsWithMultiDiscuss, p1);
				loginsWithMultiDiscuss.clear();
				try {
					p1.lancerAffichage();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			cadre.setVisible(false);
		}
	}
}
