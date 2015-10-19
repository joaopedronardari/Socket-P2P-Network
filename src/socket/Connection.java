//package socket;
//
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.InetAddress;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.Scanner;
//
//import entity.User;
//
//public class Connection {
//	
//	 public void starConnection() {
//		 User user = null;
//		 try{			
//			ServerSocket loginSocket = new ServerSocket(Server.PORT);
//			while(true){
//				Socket connectionSocket = loginSocket.accept();
//				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
//				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
//				String msgUser = inFromClient.readLine();
//				System.out.println(msgUser);
//				String[] parse = msgUser.split(" ");
//				if(loginUser(parse[0], parse[1])){
//					outToClient.writeByte(1);
//				}else{
//					outToClient.writeByte(0);
//				}
//				outToClient.flush();
//			}	
//		}catch(Exception e){
//				e.printStackTrace();
//		}
//	}
//	 
//	 private boolean loginUser(String username, String password){
//		 try{
//			 Scanner sc = new Scanner(new File("usuarios.txt"));
//			 while(sc.hasNextLine()){				
//					String[] line = sc.nextLine().split("_");
//					if(line[0].equals(username) && line[1].equals(password)){
//						User user = new User(username, password);
//						user.setConnect(true);
//						Server.addUser(user);
//						String[] friends = line[2].split(" ");
//						for(int i = 0; i < friends.length; i++){
//							User usr = new User(friends[i]);
//							//verificar se é necessário
//							if(Server.isConnect(usr)){
//								usr.setConnect(true);
//							}
//							user.getListFriends().add(usr);
//						}
//						return true;
//					}
//			 }
//		 }catch(FileNotFoundException e){
//			 e.printStackTrace();
//		 }
//		 return false;
//	 }
//	
//	public static void main(String[] args) throws IOException {
//		System.out.println(InetAddress.getLocalHost().getHostAddress());	
//	
//	}
//}
