package gui;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import entity.User;

class WindowTalk extends JFrame{
	User usr;
	User selected;

	public WindowTalk(User usr,User selected){
		super("Conversa com " +selected);
		JTextArea conversa = new JTextArea();
		conversa.setEditable(false);
		JTextArea msg = new JTextArea();

	}

}