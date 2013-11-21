package vue;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class LoginWindow extends JPanel implements ActionListener {
	
	public JLabel loglabel = new JLabel("Log   : "); 
	public JLabel passlabel = new JLabel("Pass : "); 
	public JLabel ipserverlabel = new JLabel("Ip Server : "); 
	public JButton connectbutton = new JButton("CONNECT");
	public JTextField logfield = new JTextField(15);
	public JTextField passfield = new JTextField(15);
	public JTextField ipserverfield = new JTextField(15);
	
	public LoginWindow(){}
	
	public void lancerAffichage() throws IOException
	{
		JFrame cadre = new javax.swing.JFrame("Chat-Expert");
		
		connectbutton.addActionListener(this);
		
		logfield.setSize(30, 5);
		passfield.setSize(30, 5);
		
		JPanel panneauLog = new JPanel();
		panneauLog.setLayout(new BorderLayout());
		panneauLog.add(loglabel, BorderLayout.WEST);
		panneauLog.add(logfield, BorderLayout.CENTER);
		panneauLog.setBorder(new EmptyBorder(0,0,10,0));
		
		
		JPanel panneauPass = new JPanel();
		panneauPass.setLayout(new BorderLayout());
		panneauPass.add(passlabel, BorderLayout.WEST);
		panneauPass.add(passfield, BorderLayout.CENTER);
		panneauPass.setBorder(new EmptyBorder(0,0,10,0));
		
		JPanel panneauIpServer = new JPanel();
		panneauIpServer.setLayout(new BorderLayout());
		panneauIpServer.add(ipserverlabel, BorderLayout.WEST);
		panneauIpServer.add(ipserverfield, BorderLayout.CENTER);
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
		panneauConnect.add(connectbutton, BorderLayout.CENTER);
		panneauConnect.setBorder(new EmptyBorder(5,5,5,5));
		
		cadre.setContentPane(panneauConnect);
		cadre.setLocation(500, 500);
		cadre.pack();
		cadre.setResizable(false);
		cadre.setVisible(true);
		cadre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == connectbutton)
		{
			System.out.println("je me connecte !!!!!");
		}
	}
}
