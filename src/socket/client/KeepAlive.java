package socket.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import entity.User;
import gui.WindowListFriends;
import socket.server.RequestType;
//import socket.server.Server;

public class KeepAlive implements Runnable {

	User user;
	WindowListFriends windowListFriends;
	List<User> friends;
	String ipServer;
	
	boolean run = true;
	
	public KeepAlive(User user, WindowListFriends windowListFriends,String ipServer){
		this.user = user;
		this.windowListFriends = windowListFriends;
		this.ipServer = ipServer;
	}
	
	public void stopService() { 
		this.run = false;
	}
	
	@Override
	public void run() {
		Socket userSocket = null;
		run = true;
		while(run){
			Scanner msgServer = null;
			try{
				userSocket = new Socket(ipServer, 10000);
				PrintWriter outServer = new PrintWriter(userSocket.getOutputStream());
				outServer.println(RequestType.KEEPALIVE.name());
				outServer.println(user.getUserName());
				outServer.flush();
				msgServer = new Scanner(userSocket.getInputStream());
				
				int size = Integer.parseInt(msgServer.nextLine());
				this.friends = new ArrayList<User>();
				for( int j = 0; j < size; j++){
					String nextLine = msgServer.nextLine();
					System.out.println(nextLine);
					String[] parse = nextLine.split(",");
					User friend = new User(parse[0]);
					
					// Update Status Connected
					if(parse[1].equals("offline")){
						friend.setConnect(false);
					}else{
						friend.setConnect(true);
					}
					
					// Update IP
					if (parse[2] != null) {
						friend.setIp(parse[2]);
					}
					
					// Update Port
					if (parse[3] != null) {
						friend.setPort(Integer.parseInt(parse[3]));
					}
					
					friends.add(friend);
				}

				if (this.friends != null) {
					java.awt.EventQueue.invokeLater(new Runnable() {
						public void run() {
							windowListFriends.updateFriendsList(friends);
						}
					});
				}

				Thread.sleep(500);
				
			} catch (ConnectException e) {
				java.awt.EventQueue.invokeLater(new Runnable() {
					public void run() {
						windowListFriends.serverDown();
					}
				});
				break;
			} catch(Exception e){
				e.printStackTrace();
			} finally {
				if (msgServer != null) { msgServer.close(); }
			}
		}
		
		if (userSocket != null) {
			try {
				userSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<User> getFriends(){
		return this.friends;
	}
}
