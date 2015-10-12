package socket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Scanner;

import entity.User;

public class Connection {
	
	 public User starConnection(String username, String password) {
		 User user = null;
		 try{
			Scanner sc = new Scanner(new File("usuarios.txt"));
			while(sc.hasNextLine()){
				String[] line = sc.nextLine().split("_");
				if(line[0].equals(username) && line[1].equals(password)){
					user = new User(username, password);
					user.setConnect(true);
					Server.addUser(user);
					String[] friends = line[2].split(" ");
					for(int i = 0; i < friends.length; i++){
						User usr = new User(friends[i]);
						//verificar se é necessário
						if(Server.isConnect(usr)){
							usr.setConnect(true);
						}
						user.getListFriends().add(usr);
					}
					return user;
				}
			}
		}catch(FileNotFoundException e){
			System.out.println("File not found!");
		}
		return user;
	}
	 
	
	public static void main(String[] args) throws IOException {
	System.out.println(InetAddress.getLocalHost().getHostAddress());	
	
	}
}
