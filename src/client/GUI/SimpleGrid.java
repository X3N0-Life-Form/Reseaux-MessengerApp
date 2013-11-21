package client.GUI;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

public class SimpleGrid extends JPanel {
 

  public SimpleGrid() {
     
	  setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
  
      // idée : contact panel à droite, bloc chat à gauche (bloc chat à définir dans une autre classe)
      ContactPanel contacts = new ContactPanel();
      ChatPanel chatComponent = new ChatPanel();

      //add(chatComponent, BorderLayout.WEST);
      //chatComponent.setPreferredSize(new Dimension(400, 650));
      //add(contacts, BorderLayout.EAST);
      add(contacts);
      contacts.setPreferredSize(new Dimension(200,650));
      
   
    }
}

class SeeSimpleGrid {
  public static void main(String[] arg) {
   	  new SimpleGrid();
    }
}
