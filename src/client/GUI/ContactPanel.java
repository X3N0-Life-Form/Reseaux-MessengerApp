package client.GUI;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.util.Vector;

@Deprecated
@SuppressWarnings("serial")
public class ContactPanel extends JPanel{
	
	@SuppressWarnings("unused")
	private JButton refresh = new JButton("refresh");
	private JScrollPane scrollPane;
	private JList loginList = new JList();
	
	public ContactPanel(){
		setBackground(Color.LIGHT_GRAY);
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		scrollPane = new JScrollPane();
		add(scrollPane);
		
		//TODO : put real logins into loginList
		// ======= TEMP =======
		Vector<String> logins = new Vector<String>();
		logins.add("meow");
		logins.add("graouh");
		// =====================
		
		loginList.setListData(logins);
		scrollPane.getViewport().setView(loginList);
	
		setVisible(true);
	}
}

class SeeContactPanel{	
	public static void main (String[] args){
		new ContactPanel();
	}
}


