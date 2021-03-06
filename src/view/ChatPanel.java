package view;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import client.handling.UDPHandlerClientDiscuss;
import common.Message;
import common.MessageInfoStrings;
import common.MessageType;
import common.handling.HandlingException;
import controller.ContactListController;
import controller.Controller;

/**
 * Chat View: displays messages exchanged between one or more users.
 * @author etudiant
 *
 */
public class ChatPanel extends JPanel implements ActionListener, KeyListener, View {	

	public static final int FRAME_HEIGHT = 500;
	public static final int FRAME_WIDTH = 520;
	public static final int DEFAULT_LOCATION_X = 400;
	public static final int DEFAULT_LOCATION_Y = 200;

	private static final long serialVersionUID = 1L;

	
	private String otherLogin;
	private Vector<String> otherLoginMultiDiscuss;
	private JEditorPane textArea = new JEditorPane();
	private JScrollPane scrollPaneTop;
	private JScrollPane scrollPaneBottom;
	private JFrame cadre = new javax.swing.JFrame("");
	private JEditorPane text = new JEditorPane();
	private Map<String, ChatPanel> discMap;
	private Map<Vector<String>, ChatPanel> discMapMultiDiscuss;
    private Map<Integer, String> myMessages = new HashMap<Integer, String>();
	private ContactListController controller;
	private Map<String, InetAddress> clientIps;
    private UDPHandlerClientDiscuss UDPHCD;
    private boolean discussMultip = false;
    private int msgCount = 0;
    private int lastMsgIdReceived = 0;
	
    /**
     * Constructs a group discussion ChatPanel.
     * @param loginWithMultiDiscuss
     * @param discMapMultiDiscuss
     * @param controller
     */
	public ChatPanel(Vector<String> loginWithMultiDiscuss, Map<Vector<String>,ChatPanel> discMapMultiDiscuss, ContactListController controller) {
		
		this.otherLoginMultiDiscuss = loginWithMultiDiscuss;
		String listLoginText = "";
		for(String login : loginWithMultiDiscuss) {	listLoginText += " "+login; }
		this.cadre = new javax.swing.JFrame(controller.getClient().getLogin()+ ":   " +listLoginText);
		this.discMapMultiDiscuss = discMapMultiDiscuss;
		discMapMultiDiscuss.put(loginWithMultiDiscuss, this);
		this.controller = controller;
		this.clientIps = controller.getClient().getClientIps();
		this.discussMultip = true;
		
		try {
			UDPHCD = new UDPHandlerClientDiscuss(controller.getClient());
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
		for(String login : loginWithMultiDiscuss) {
			if (!(clientIps.containsKey(login))) {
				Message msg = new Message(MessageType.REQUEST_IP);
				msg.addInfo(MessageInfoStrings.LOGIN, login);
				msg.addInfo(MessageInfoStrings.PORT, controller.getClient().getUDPMainListeningPort() + "");
				try {
					controller.getClient().getUdpClient().getSend().getMessageManager().handleMessage(msg, controller.getClient().getUdpClient().getSend().getSocket());
				} catch (SocketException e) {
					e.printStackTrace();
				} catch (HandlingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Contructs a group discussion ChatPanel, but differently.
	 * @param login
	 * @param discMap
	 * @param controller
	 */
	public ChatPanel(String login, Map<String, ChatPanel> discMap, ContactListController controller){
		
		this.otherLogin = login;
		this.cadre = new javax.swing.JFrame(controller.getClient().getLogin()+ ":   " +login);
		this.discMap = discMap;
		this.controller = controller;
		this.clientIps = controller.getClient().getClientIps();
		
		try {
			UDPHCD = new UDPHandlerClientDiscuss(controller.getClient());
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
		if (!(clientIps.containsKey(login))) {
			Message msg = new Message(MessageType.REQUEST_IP);
			msg.addInfo(MessageInfoStrings.LOGIN, otherLogin);
			msg.addInfo(MessageInfoStrings.PORT, controller.getClient().getUDPMainListeningPort() + "");
			try {
				controller.getClient().getUdpClient().getSend().getMessageManager().handleMessage(msg, controller.getClient().getUdpClient().getSend().getSocket());
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (HandlingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void lancerAffichage() throws IOException {
		
		textArea.setEditable(false);
		scrollPaneTop = new JScrollPane(textArea);
		scrollPaneTop.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneTop.setBorder(new EmptyBorder(10,10,5,10));
		
		scrollPaneBottom = new JScrollPane(text);
		scrollPaneBottom.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneBottom.setBorder(new EmptyBorder(5,10,10,10));
		
		text.addKeyListener(this);
		
		scrollPaneTop.setPreferredSize(new Dimension(FRAME_WIDTH, 400));
		scrollPaneBottom.setPreferredSize(new Dimension(FRAME_WIDTH, 100));
		
		JPanel panneauChat = new JPanel();
		panneauChat.setLayout(new BorderLayout());
		panneauChat.add(scrollPaneTop, BorderLayout.NORTH);
		panneauChat.add(scrollPaneBottom, BorderLayout.CENTER);
		cadre.setContentPane(panneauChat);
		cadre.setLocation(DEFAULT_LOCATION_X, DEFAULT_LOCATION_Y);
		cadre.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		cadre.setVisible(true);
		cadre.setResizable(false);
		cadre.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(discussMultip){
					discMapMultiDiscuss.remove(otherLoginMultiDiscuss);
				} else {	
					discMap.remove(otherLogin);
				}
			}
		});
		
		scrollPaneTop.setBorder(new LineBorder(Color.darkGray, 5));
		scrollPaneBottom.setBorder(new LineBorder(Color.darkGray, 5));
		panneauChat.setBorder(new LineBorder(Color.darkGray, 5));
	}
	
	/**
	 * Add text to the discussion panel.
	 * @param msg
	 * @param msgId
	 * @param loginOriginMsg
	 */
	public void addText(String msg, int msgId, String loginOriginMsg) {
		if (msgId - lastMsgIdReceived != 0 && msgId != 0) {
			int lostMsg = msgId - lastMsgIdReceived;
			UDPHCD.run(MessageType.MISSING_MSG, lostMsg, otherLogin);
		}
		String textMessage = textArea.getText() 
							+ "\n"
							+ loginOriginMsg + ":  "
							+ msg
							+ "\n\n\t\t\t" 
							+ new Date() 
							+ "\n";
		textArea.setText(textMessage);
		textArea.setCaretPosition(textArea.getText().length());
		setLastMsgIdReceived(msgId);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == '\n') {
			if (discussMultip == true) {
				UDPHCD.run(text.getText().trim(), otherLoginMultiDiscuss, this, msgCount);
					String textMessage = textArea.getText() 
							+ "\n" 
							+ controller.getClient().getLogin() + ":  " 
							+ text.getText().trim() 
							+ "\n\n\t\t\t" 
							+ new Date() 
							+ "\n";			
					textArea.setText(textMessage);
					text.setText("");
					textArea.setCaretPosition(textArea.getText().length());
			} else {
				if(UDPHCD.run(text.getText().trim(), otherLogin, this, msgCount)) {
					myMessages.put(msgCount, text.getText().trim());
					msgCount++;
					String textMessage = textArea.getText() 
							+ "\n" 
							+ controller.getClient().getLogin() + ":  " 
							+ text.getText().trim() 
							+ "\n\n\t\t\t" 
							+ new Date() 
							+ "\n";			
					textArea.setText(textMessage);
					text.setText("");
					textArea.setCaretPosition(textArea.getText().length());
				} else { text.setText(""); }
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	/**
	 * Displays a missing message warning.
	 * @param lostMsg
	 */
	public void displayMissingMessage(int lostMsg) {
		String recoveredMsg = myMessages.get(lostMsg);
		String warningText = "*********************************************";
		warningText += "\nWARNING: the following message was never received";
		
		String textMessage = textArea.getText()
				+ "\n"
				+ warningText + "\n"
				+ recoveredMsg;
		textMessage += "*********************************************";
		
		textArea.setText(textMessage);
		textArea.setCaretPosition(textArea.getText().length());
	}
	
	/**
	 * Displays a message stating that the specified user has disconnected.
	 * @param login
	 */
	public void displayDisconnectedMessage(String login) {
		String warningText = "*********************************************";
		warningText += "\nWARNING: the user " + login + " has been disconnected.";
		
		String textMessage = textArea.getText()
				+ "\n"
				+ warningText + "\n";
		textMessage += "*********************************************";
		
		textArea.setText(textMessage);
		textArea.setCaretPosition(textArea.getText().length());
	}
	
	public JFrame getFrame() {
		 return cadre;
	 }

	@Override
	public Controller getController() {
		return controller;
	}

	@Override
	public void setController(Controller controller) {
		this.controller = (ContactListController) controller;
	}

	/**
	 * Displays a message stating that the specified user has reconnected.
	 * @param login
	 */
	public void displayReconnectedMessage(String login) {
		String warningText = "*********************************************";
		warningText += "\nINFO: the user " + login + " has reconnected.";
		
		String textMessage = textArea.getText()
				+ "\n"
				+ warningText + "\n";
		textMessage += "*********************************************";
		
		textArea.setText(textMessage);
		textArea.setCaretPosition(textArea.getText().length());
	}

	public int getLastMsgIdReceived() {
		return lastMsgIdReceived;
	}

	public void setLastMsgIdReceived(int lastMsgIdReceived) {
		this.lastMsgIdReceived = lastMsgIdReceived;
	}

}