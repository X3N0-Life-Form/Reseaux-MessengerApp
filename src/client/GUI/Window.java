package client.GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;




public class Window {
	
    static final int DEFAULT_WIDTH = 600;
    static final int DEFAULT_HEIGHT = 650;
	
    public static void main(String[] arg) {	
		JFrame frame = new javax.swing.JFrame("Messenger");
		SimpleGrid panel = new SimpleGrid();
		
		//size
		panel.setPreferredSize(new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT));
	
		frame.setContentPane(panel);
		//placement
		frame.setLocation(300, 100);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}