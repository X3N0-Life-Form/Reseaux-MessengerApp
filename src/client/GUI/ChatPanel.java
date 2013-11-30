package client.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

@Deprecated
@SuppressWarnings({ "serial", "unused" })
public class ChatPanel extends JPanel {	
	
	public ChatPanel(){
		 setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		 
		 WritePanel wPanel = new WritePanel();
		 ReadPanel rPanel = new ReadPanel();
		 
		 add(rPanel, BorderLayout.NORTH);
		 rPanel.setPreferredSize(new Dimension(100,500));
		 add(wPanel, BorderLayout.SOUTH);
		 wPanel.setPreferredSize(new Dimension(100,150));
		
		setVisible(true);		
		
	}
}

class SeeChatPanel{	
	public static void main (String[] args){
		new ChatPanel();
	}
}