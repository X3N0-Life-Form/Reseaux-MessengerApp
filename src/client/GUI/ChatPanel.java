package client.GUI;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

public class ChatPanel extends JPanel {
	
	public ChatPanel(){
		setBackground(Color.GREEN);
		
		setVisible(true);
	}
}

class SeeChatPanel{	
	public static void main (String[] args){
		new ChatPanel();
	}
}