package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import controller.ContactListController;
import controller.Controller;

/**
 * View  allowing the user to select multiple contacts and start a group discussion.
 * @author etudiant
 * @see ContactListWindow
 * @see ChatPanel
 */
public class SelectMultiContactWindow extends JPanel implements ActionListener, View {
	

	public static final int DEFAULT_LOCATION_X = 600;
	public static final int DEFAULT_LOCATION_Y = 200;
	public static final int DEFAULT_WIDTH = 300;
	public static final int DEFAULT_HEIGHT = 400;

	/**
	 * 
	 */
	private static final long serialVersionUID = -1468670957626069617L;
	
	private JButton startButton = new JButton("START");
	private JFrame cadre = new javax.swing.JFrame("Sélectionner le groupe d'amis : ");
	
	private Vector<JCheckBox> listCheckBoxLog = new Vector<JCheckBox>();
	
	private JScrollPane scrollPane = new JScrollPane();
	
	private Map<Vector<String>, ChatPanel>  mapListChat;
	
	
	private Vector<String> listLogWithContact;
	private Vector<String> loginsWithMultiDiscuss ;	
	private ContactListController controller;
	
	/**
	 * Constructs a SelectMultiContactWindow with the specified list of logins.
	 * @param logins - List of logins.
	 * @param controller - ContactListController
	 * @param mapListChat - Map containing any existing group ChatPanels
	 */
	public SelectMultiContactWindow(List<String> logins, ContactListController controller, Map<Vector<String>, ChatPanel>  mapListChat) {
		this.mapListChat = mapListChat;
		this.loginsWithMultiDiscuss = new Vector<String>();
		this.listLogWithContact = new Vector<String>();
		for(String log : logins) {
			listCheckBoxLog.add(new JCheckBox(log, false));
		}
		for(JCheckBox box1 : listCheckBoxLog) {
			box1.setSelected(false);
		}
		this.controller = controller;
	}


	@Override
	public void lancerAffichage() throws IOException
	{
		JPanel panneauPrincipal = new JPanel();
		for(JCheckBox box2 : listCheckBoxLog) {
			box2.addActionListener(this);
			panneauPrincipal.add(box2);
		}
		startButton.addActionListener(this);
		panneauPrincipal.add(startButton, BorderLayout.SOUTH);
		
		scrollPane.getViewport().setView(panneauPrincipal);
		
		cadre.setContentPane(panneauPrincipal);
		cadre.setLocation(DEFAULT_LOCATION_X, DEFAULT_LOCATION_Y);
		cadre.pack();
		cadre.setResizable(false);
		cadre.setMaximumSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		cadre.setVisible(true);
		cadre.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		boolean allClient = false;
		
		if (e.getSource() == startButton) {
			
			//Je parcours toute mes checbox pour savoir si elles sont cochées
			for(JCheckBox box3 : listCheckBoxLog) {
				if (box3.isSelected()) {
					listLogWithContact.add(box3.getText());
					box3.setSelected(false);
				}
			}
			listCheckBoxLog.clear();
			
			for(String login : listLogWithContact) {
				loginsWithMultiDiscuss.add(login);
			}
			listLogWithContact.clear();			
			Collections.sort(loginsWithMultiDiscuss);
			
			if (loginsWithMultiDiscuss.size()>1){
				if(mapListChat.containsKey(loginsWithMultiDiscuss)){
					ChatPanel p = mapListChat.get(loginsWithMultiDiscuss);
					p.getFrame().toFront();
					loginsWithMultiDiscuss.clear();
				} else {
					if(controller.getClient().isServerUp()) {
						Vector<String> copy = new Vector<String>(loginsWithMultiDiscuss);
						loginsWithMultiDiscuss.clear();
						ChatPanel p1 = new ChatPanel(copy, mapListChat, controller);
						mapListChat.put(loginsWithMultiDiscuss, p1);
						try {
							p1.lancerAffichage();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else {
						for(String log : loginsWithMultiDiscuss) {
							if(controller.getClient().getClientIps().containsKey(log)) {
								allClient = true;
							} else {
								allClient = false;
								JOptionPane.showMessageDialog(null, "Server is down; user IPs can't all be retrieved", "Error", JOptionPane.INFORMATION_MESSAGE);
								break;
							}
						}
						if(allClient == true) {
							Vector<String> copy = new Vector<String>(loginsWithMultiDiscuss);
							loginsWithMultiDiscuss.clear();
							ChatPanel p1 = new ChatPanel(copy, mapListChat, controller);
							mapListChat.put(loginsWithMultiDiscuss, p1);
							try {
								p1.lancerAffichage();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					}						
				}
			} else {
				JOptionPane.showMessageDialog(null, "You must select at least 2 users", "Action impossible", JOptionPane.INFORMATION_MESSAGE);
			}
			
			cadre.setVisible(false);
		}
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = (ContactListController) controller;
	}
	
	public Map<Vector<String>, ChatPanel> getMapListChat() {
		return mapListChat;
	}

	public void setMapListChat(Map<Vector<String>, ChatPanel> mapListChat) {
		this.mapListChat = mapListChat;
	}
}
