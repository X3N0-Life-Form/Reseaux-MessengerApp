package view;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

public class ChatPanel extends JPanel implements ActionListener, KeyListener {	

	private static final long serialVersionUID = 1L;
	
	private static final int FRAME_WIDTH = 520;
	 private String login;
	 private JEditorPane textArea = new JEditorPane();
	 private JScrollPane scrollPaneTop;
	 private JScrollPane scrollPaneBottom;
	 private JFrame cadre = new javax.swing.JFrame("");
	 private JEditorPane text = new JEditorPane();
	 
	 private Map<String, ChatPanel> discMap;
	 
	 public JFrame getFrame() {
		 return cadre;
	 }
	
	public ChatPanel(String login, Map<String, ChatPanel> discMap){
		this.login = login;
		this.cadre = new javax.swing.JFrame(login);
		this.discMap = discMap;
	}
	
	public void lancerAffichage() throws IOException {
		
		textArea.setEditable(false);
		scrollPaneTop = new JScrollPane(textArea);
		scrollPaneTop.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneTop.setBorder(new EmptyBorder(10,10,5,10));
		
		scrollPaneBottom = new JScrollPane(text);
		scrollPaneBottom.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneBottom.setBorder(new EmptyBorder(5,10,10,10));
		
		text.addKeyListener(this);
		
		scrollPaneTop.setPreferredSize(new Dimension(FRAME_WIDTH,400));
		scrollPaneBottom.setPreferredSize(new Dimension(FRAME_WIDTH, 100));
		
		JPanel panneauChat = new JPanel();
		panneauChat.setLayout(new BorderLayout());
		panneauChat.add(scrollPaneTop, BorderLayout.NORTH);
		panneauChat.add(scrollPaneBottom, BorderLayout.CENTER);

		cadre.setContentPane(panneauChat);
		cadre.setLocation(400, 200);
		cadre.setSize(FRAME_WIDTH, 500);
		cadre.setVisible(true);
		cadre.setResizable(false);
		//cadre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cadre.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				discMap.remove(login);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyChar() == '\n') {
			String textMessage = textArea.getText() + "\n"
					+ "TEST : "
					+ text.getText().trim()
					+ "\n\n\t\t\t" + new Date() + "\n";
			textArea.setText(textMessage);
			text.setText("");
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


}