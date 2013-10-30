package client.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WritePanel extends JPanel implements ActionListener {
	
	JTextField text = new JTextField();
	JButton send = new JButton("Send");
	String toSend;
	
	public WritePanel(){
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(text);
		add(send);
		setVisible(true);
				
		send.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == send){
			// get text
			toSend = text.getText();
			// clear text
			text.setText("");			
		}
	}
}

class SeeWritePanel{
	
	public void main(String[] args){
		new WritePanel();
	}
}
