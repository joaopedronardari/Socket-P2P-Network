package gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


import entity.User;
import socket.KeepAlive;
import socket.ReceiveMsg;
import socket.Server;

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
		window.setLayout(new GridLayout(usr.getListFriends().size()+2, 2,10,10));
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
		JButton buttonOf = new JButton("Desconectar");
		buttonOf.addActionListener(new Desconect());
		window.add(buttonOf);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(400, 250);
		setVisible(true);
		
		KeepAlive keepAlive = new KeepAlive(usr);
		Thread t1 = new Thread(keepAlive);
		t1.start();
	
	}
	
	private void socketLogoff(){
		try{
			Socket userSocket = new Socket(Server.ADDRES, Server.PORT);
			PrintWriter outServer = new PrintWriter(userSocket.getOutputStream());
			outServer.println("off");
			outServer.println(usr.getUserName());
			outServer.flush();
			Scanner msgServer = new Scanner(userSocket.getInputStream());
			if(msgServer.nextLine().equals("-1")){
				new JOptionPane("Logoff feito com sucesso");
				dispose();
			}
		}catch(Exception e){
			e.printStackTrace();
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
		WindowTalk chat = new WindowTalk(usr,selected);
		listener.add(chat);
		chat.setVisible(true);
	}
	
	private class Desconect implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try{
				socketLogoff();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
}