package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import client.Client;

import common.Message;

import controller.Controller;
import controller.LoginController;

/**
 * Login window, this is where a user enters its login, password and the address
 * of the server he wants to connect to.
 * @author etudiant
 * @see LoginController
 */
public class LoginWindow extends JPanel implements ActionListener, View {
	
	public static final int DEFAULT_LOCATION_X = 600;
	public static final int DEFAULT_LOCATION_Y = 250;

	/**
	 * Customized KeyListener that fires the processLogin() method when enter is pressed.
	 * @author etudiant
	 * @see KeyListener
	 */
	protected class LoginWindowKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {}
		
		@Override
		public void keyReleased(KeyEvent e) {}
		
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				processLogin();
			}
		}
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6789010216375385228L;
	
	public JLabel logLabel = new JLabel("Log   : "); 
	public JLabel passLabel = new JLabel("Pass : "); 
	public JLabel ipServerLabel = new JLabel("Ip Server : "); 
	public JButton connectButton = new JButton("CONNECT");
	public JTextField logField = new JTextField(15);
	public JPasswordField passField = new JPasswordField(15);
	public JTextField ipServerField = new JTextField(15);
	public JFrame cadre = new javax.swing.JFrame("Chat-Expert");
	
	private LoginController controller;
	
	@Override
	public Controller getController() {
		return controller;
	}
	
	@Override
	public void setController(Controller controller) {
		this.controller = (LoginController) controller;
	}
	
	@Override
	public void lancerAffichage() throws IOException
	{
		connectButton.addActionListener(this);
		connectButton.addKeyListener(new LoginWindowKeyListener());
		logField.addKeyListener(new LoginWindowKeyListener());
		passField.addKeyListener(new LoginWindowKeyListener());
		ipServerField.addKeyListener(new LoginWindowKeyListener());
		
		logField.setSize(30, 5);
		passField.setSize(30, 5);
		
		JPanel panneauLog = new JPanel();
		panneauLog.setLayout(new BorderLayout());
		panneauLog.add(logLabel, BorderLayout.WEST);
		panneauLog.add(logField, BorderLayout.CENTER);
		panneauLog.setBorder(new EmptyBorder(0,0,10,0));
		
		JPanel panneauPass = new JPanel();
		panneauPass.setLayout(new BorderLayout());
		panneauPass.add(passLabel, BorderLayout.WEST);
		panneauPass.add(passField, BorderLayout.CENTER);
		panneauPass.setBorder(new EmptyBorder(0,0,10,0));
		
		JPanel panneauIpServer = new JPanel();
		panneauIpServer.setLayout(new BorderLayout());
		panneauIpServer.add(ipServerLabel, BorderLayout.WEST);
		panneauIpServer.add(ipServerField, BorderLayout.CENTER);
		panneauIpServer.setBorder(new EmptyBorder(0,0,10,0));
		
		JPanel panneauLogPassIpServer = new JPanel();
		panneauLogPassIpServer.setLayout(new BorderLayout());
		panneauLogPassIpServer.add(panneauLog, BorderLayout.NORTH);
		panneauLogPassIpServer.add(panneauPass, BorderLayout.CENTER);
		panneauLogPassIpServer.add(panneauIpServer, BorderLayout.SOUTH);
		panneauLogPassIpServer.setBorder(new EmptyBorder(5,0,10,0));
		
		JPanel panneauConnect = new JPanel();
		panneauConnect.setLayout(new BorderLayout());
		panneauConnect.add(panneauLogPassIpServer, BorderLayout.NORTH);
		panneauConnect.add(connectButton, BorderLayout.CENTER);
		panneauConnect.setBorder(new EmptyBorder(5,5,5,5));
		
		cadre.setContentPane(panneauConnect);
		cadre.setLocation(DEFAULT_LOCATION_X, DEFAULT_LOCATION_Y);
		cadre.pack();
		cadre.setResizable(false);
		cadre.setVisible(true);
		cadre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == connectButton)	{
			processLogin();
		}
	}

	public void processLogin() {
		String login = logField.getText();
		String pass = new String(passField.getPassword());
		String ipServer = ipServerField.getText();
		controller.processLogin(login, pass, ipServer);
	}
	
	/**
	 * Displays an error dialog box with the specified message.
	 * @param message - Message object.
	 */
	public void fireErrorMessage(Message message) {
		JOptionPane.showMessageDialog(null, message.getMessage(), "Error", JOptionPane.INFORMATION_MESSAGE);
	}
	

	/**
	 * Creates a {@link ContactListWindow} for the specified {@link Client} and list
	 * of logins.
	 * @param clientLogins - List of connected clients.
	 * @param client
	 */
	public void createContactList(List<String> clientLogins, Client client) {
		clientLogins.remove(client.getLogin());
		cadre.setVisible(false);
		try {
			ChatMain.clw.setLogins(clientLogins);
			ChatMain.clw.lancerAffichage();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Refreshes the ContactListWindow.
	 * @param client
	 */
	public void refreshContactList(Client client) {
		try {
			ChatMain.clw.refresh(controller.getClient().getClientLogins(), client);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
