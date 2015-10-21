package socket.client;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import entity.User;
import gui.WindowListFriends;
import socket.server.RequestType;
import socket.server.Server;

public class KeepAlive implements Runnable {

	User user;
	WindowListFriends windowListFriends;
	List<User> friends;
	
	public KeepAlive(User user, WindowListFriends windowListFriends){
		this.user = user;
		this.windowListFriends = windowListFriends;
	}
	
	@Override
	public void run() {
		// FIXME - Refactor needed Close socket and scanner
		while(true){
			try{
				Socket userSocket = new Socket(Server.ADDRESS, Server.PORT);
				PrintWriter outServer = new PrintWriter(userSocket.getOutputStream());
				outServer.println(RequestType.KEEPALIVE.name());
				outServer.println(user.getUserName());
				outServer.flush();
				Scanner msgServer = new Scanner(userSocket.getInputStream());
				
				// FIXME - Duplicated Code
				int size = Integer.parseInt(msgServer.nextLine());
				this.friends = new ArrayList<User>();
				for( int j = 0; j < size; j++){
					String nextLine = msgServer.nextLine();
					//System.out.println(nextLine);
					String[] parse = nextLine.split(",");
					User friend = new User(parse[0]);
					if(parse[1].equals("offline")){
						friend.setConnect(false);
					}else{
						friend.setConnect(true);
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
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public List<User> getFriends(){
		return this.friends;
	}
}
