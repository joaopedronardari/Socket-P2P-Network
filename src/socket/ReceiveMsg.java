package socket;

import java.awt.print.Printable;
import java.io.IOException;
import java.net.ServerSocket;

import entity.User;

public class ReceiveMsg extends Thread{
	User user;
	
	public ReceiveMsg(User user){
		this.user = user;
	}
	
	public void setUser(User user){
		this.user = user;
	}
	
	public User getUser(){
		return user;
	}
	
	@Override
	public void run(){
		while(true){
			
			try {
				ServerSocket hello = new ServerSocket();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
		}
	}

}
