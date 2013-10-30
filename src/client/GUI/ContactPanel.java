package client.GUI;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;

public class ContactPanel extends JPanel{
	
	JButton refresh = new JButton("refresh");
	public ContactPanel(){
		setBackground(Color.LIGHT_GRAY);
		setVisible(true);
	}

	//TODO : afficher liste contacts, bouton refresh, tout ça tout ça


}

class SeeContactPanel{	
	public static void main (String[] args){
		new ContactPanel();
	}
}


