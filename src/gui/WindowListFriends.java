package gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
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
import socket.client.KeepAlive;
import socket.client.ReceiveMsg;
import socket.server.RequestType;
import socket.server.Server;


public class WindowListFriends extends JFrame implements ActionListener,ListSelectionListener{

	private static final long serialVersionUID = 1L;
	JList<User> friendsList;
	DefaultListModel<User> listModel;
	JButton talkButton;
	JButton logoutButton;
	
	User selectedUser;
	User user;
	ReceiveMsg receiveMsg;
	KeepAlive keepAlive;
	List<User> listFriends;
	
	public WindowListFriends(User user) {
		super("Bem-Vindo " + user.getUserName());
		this.user = user;
		Container window = getContentPane();
		
		// Get ListFriends
		listFriends = user.getListFriends();
		
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
		receiveMsg = new ReceiveMsg(user,listFriends);
		receiveMsg.start();
		
		// Create KeepAlive Thread
		keepAlive = new KeepAlive(user, this);
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
		user.setListFriends(listFriends);
		listModel = generateListModel(listFriends);
		
		if (!friendsList.hasFocus()) {
			friendsList.setModel(listModel);
		}
	}
	
	public void serverDown() {
		receiveMsg.stopService();
		keepAlive.stopService();
		new WindowLogin();
		dispose();
	}
	
	private void socketLogoff(){
		Socket userSocket = null;
		Scanner msgServer = null;
		try{
			userSocket = new Socket(Server.ADDRESS, Server.PORT);
			PrintWriter outServer = new PrintWriter(userSocket.getOutputStream());
			outServer.println(RequestType.LOGOUT.name());
			outServer.println(user.getUserName());
			outServer.flush();
			msgServer = new Scanner(userSocket.getInputStream());
			String msg = msgServer.nextLine();
			if(msg.equals("-1")){
				new JOptionPane("Logoff feito com sucesso");
			}
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			if (msgServer != null) {
				msgServer.close();
			}
			
			if (userSocket != null) {
				try {
					userSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			System.exit(0);
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
		WindowTalk chat = new WindowTalk(user,selectedUser,windowListFriends);
		receiveMsg.add(chat);
		chat.setVisible(true);
	}

	public void removeWindowTalk(WindowTalk windowTalk) {
		receiveMsg.remove(windowTalk);
	}
	
	/**
	 * Logout Action
	 */
	private class Desconect implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			try{
				receiveMsg.stopService();
				keepAlive.stopService();
				socketLogoff();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
}