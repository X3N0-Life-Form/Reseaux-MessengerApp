package client.GUI;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class WritePanel extends JPanel {
	
	JTextField text = new JTextField();
	JButton send = new JButton("Send");
	
	public WritePanel(){
		setBackground(Color.darkGray);
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(text);
		add(send);
		
		setVisible(true);
				
	}
}

class SeeWritePanel{
	
	public void main(String[] args){
		new WritePanel();
	}
}
