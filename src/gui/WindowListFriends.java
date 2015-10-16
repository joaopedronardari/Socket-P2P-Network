package gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import entity.User;
import socket.Server;

public class WindowListFriends extends JFrame implements ActionListener,ListSelectionListener{

	JList<User> friends;
	JButton button;
	User selected;
	User usr;
	
	public WindowListFriends(User usr) {
		super("Veja quem está online");
		this.usr = usr;
		Container window = getContentPane();
		window.setLayout(new GridLayout(usr.getListFriends().size(), 2,10,10));
		List<User> listFriends = usr.getListFriends();
		DefaultListModel<User> listModel = new DefaultListModel<User>();
		for(int i = 0; i < usr.getListFriends().size(); i++){
		/*	JLabel nameUsr = new JLabel(listFriends.get(i).getUserName());
			window.add(nameUsr);
			JButton button = new JButton("Iniciar conversa");
			if(listFriends.get(i).isConnect()){
				button.setEnabled(true);
			}else{
				button.setEnabled(false);
			}
			window.add(button);
		*/
			listModel.addElement(listFriends.get(i));
		}
		
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
	}
	
	public static void main(){
		try {
			Socket socket = new Socket(Server.ADDRES,  Server.PORT);
		} catch (UnknownHostException e) {
			System.out.println("Unknown Host");
		} catch (IOException e) {
			System.out.println("IOExecption");
		}
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
		new WindowTalk(usr,selected);
	}
}