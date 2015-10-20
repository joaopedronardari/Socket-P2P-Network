package socket;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import entity.User;
//
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
		// FIXME - Refactor needed
		while(true){
			try{
				Socket userSocket = new Socket(Server.ADDRES, Server.PORT);
				PrintWriter outServer = new PrintWriter(userSocket.getOutputStream());
				outServer.println("keepalive");
				outServer.println(user.getUserName());
				outServer.flush();
				Scanner msgServer = new Scanner(userSocket.getInputStream());
				
				// FIXME - Duplicated Code
				int size = Integer.parseInt(msgServer.nextLine());
				this.friends = new ArrayList<User>();
				for( int j = 0; j < size; j++){
					String[] parse = msgServer.nextLine().split(" - ");
					User friend = new User(parse[0]);
					if(parse[1].equals("Offline")){
						friend.setConnect(false);
					}else{
						friend.setConnect(true);
					}
					friends.add(friend);
				}

				if (this.friends != null) {
					java.awt.EventQueue.invokeLater(new Runnable() {
						public void run() {
							windowListFriends.updateFriendsList(this.friends);
						}
					});
				}

				Thread.sleep(500);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public List<String> getFriends(){
		return this.friends;
	}
}
