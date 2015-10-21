package gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	JList<User> friendsList;
	DefaultListModel<User> listModel;
	JButton talkButton;
	JButton logoutButton;
	
	User selectedUser;
	User user;
	ReceiveMsg receiveMsgListener;
	
	public WindowListFriends(User usr) {
		super("Bem-Vindo " + usr.getUserName());
		this.user = usr;
		Container window = getContentPane();
		
		// Get ListFriends
		List<User> listFriends = usr.getListFriends();
		
		// Set WindowLayout
		window.setLayout(new GridLayout(listFriends.size()+2, 2,10,10));
		
		// Create Talk Button
		talkButton = new JButton("Conversar");
		talkButton.setEnabled(false);
		talkButton.addActionListener(this);

		// Create Logout Button
		logoutButton = new JButton("Desconectar");
		logoutButton.addActionListener(new Desconect());
		
		// Create List
		listModel = generateListModel(listFriends);
		friendsList = new JList<User>(listModel);
		friendsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		friendsList.addListSelectionListener(this);

		// Add Components
		window.add(friendsList);
		window.add(new JScrollPane(friendsList));
		window.add(talkButton);
		window.add(logoutButton);
		
		// Window Properties
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(400, 250);
		setVisible(true);
		
		// Create ReceiveMsg Thread
		receiveMsgListener = new ReceiveMsg(usr,listFriends);
		receiveMsgListener.start();
		
		// Create KeepAlive Thread
		KeepAlive keepAlive = new KeepAlive(usr, this);
		Thread t1 = new Thread(keepAlive);
		t1.start();
	}

	public DefaultListModel<User> generateListModel(List<User> listFriends) {
		DefaultListModel<User> listModel = new DefaultListModel<User>();
		for(int i = 0; i < user.getListFriends().size(); i++){
			listModel.addElement(listFriends.get(i));
		}
		return listModel;
	}

	public void updateFriendsList(List<User> listFriends) {
		listModel = generateListModel(listFriends);
		
		if (!friendsList.hasFocus()) {
			friendsList.setModel(listModel);
		}
	}
	
	private void socketLogoff(){
		try{
			Socket userSocket = new Socket(Server.ADDRES, Server.PORT);
			PrintWriter outServer = new PrintWriter(userSocket.getOutputStream());
			outServer.println("off");
			outServer.println(user.getUserName());
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

	/**
	 * Select item from friendsList action 
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if(!e.getValueIsAdjusting()){
			selectedUser = friendsList.getSelectedValue();
			if(selectedUser != null && !talkButton.isEnabled() && selectedUser.isConnect()) talkButton.setEnabled(true);
			else if (selectedUser == null || !selectedUser.isConnect()) talkButton.setEnabled(false); 
		}
	}
	
	/**
	 * Click in "Conversar"
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		WindowTalk chat = new WindowTalk(user,selectedUser);
		receiveMsgListener.add(chat);
		chat.setVisible(true);
	}
	
	/**
	 * Logout Action
	 */
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