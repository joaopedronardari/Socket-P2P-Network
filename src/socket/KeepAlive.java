package socket;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import entity.User;

public class KeepAlive implements Runnable {

	User user;
	List<String> friends;
	public KeepAlive(User user){
		this.user = user;
	}
	
	@Override
	public void run() {
		while(true){
			try{
				Socket userSocket = new Socket(Server.ADDRES, Server.PORT);
				PrintWriter outServer = new PrintWriter(userSocket.getOutputStream());
				outServer.println("keepalive");
				outServer.println(user.getUserName());
				outServer.flush();
				Scanner msgServer = new Scanner(userSocket.getInputStream());
				int total = Integer.parseInt(msgServer.nextLine());
				this.friends = new ArrayList<String>();
				for(int i = 0; i < total; i++){
					friends.add(msgServer.nextLine());
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
