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
				String[] line = sc.nextLine().split("|");
				if(line[0].equals(user) && line[1].equals(password)){
					user = new User(username, password);
					user.setConnect(true);
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
