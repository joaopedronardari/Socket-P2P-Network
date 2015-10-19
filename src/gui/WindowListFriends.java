package gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import entity.User;
import socket.KeepAlive;
import socket.ReceiveMsg;

public class WindowListFriends extends JFrame implements ActionListener,ListSelectionListener{

	private static final long serialVersionUID = 1L;
	JList<User> friends;
	JButton button;
	User selected;
	User usr;
	ReceiveMsg listener;
	
	public WindowListFriends(User usr) {
		super("Veja quem está online");
		this.usr = usr;
		Container window = getContentPane();
		window.setLayout(new GridLayout(usr.getListFriends().size(), 2,10,10));
		List<User> listFriends = usr.getListFriends();
		DefaultListModel<User> listModel = new DefaultListModel<User>();
		for(int i = 0; i < usr.getListFriends().size(); i++){
			listModel.addElement(listFriends.get(i));
		}
		
		listener = new ReceiveMsg(usr,listFriends);
		listener.start();
		
		button = new JButton("Conversar");
		button.setEnabled(false);
		button.addActionListener(this);

		friends = new JList<User>(listModel);
		friends.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		friends.addListSelectionListener(this);

		window.add(friends);
		window.add(new JScrollPane(friends));
		window.add(button); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 200);
		setVisible(true);
		
		KeepAlive keepAlive = new KeepAlive(usr);
		Thread t1 = new Thread(keepAlive);
		t1.start();
	}
	


	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting()){
			selected = friends.getSelectedValue();
			if(selected != null && !button.isEnabled() && selected.isConnect()) button.setEnabled(true);
			else if (selected == null || !selected.isConnect()) button.setEnabled(false); 
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		WindowTalk chat = new WindowTalk(usr,selected);
		listener.add(chat);
	}
}