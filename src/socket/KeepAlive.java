package socket;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import entity.User;

public class KeepAlive implements Runnable {

	User user;
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
				for(int i = 0; i < total; i++){
					System.out.println(msgServer.nextLine());
				}
				Thread.sleep(500);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
