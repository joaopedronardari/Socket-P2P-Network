package socket.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import entity.User;

public class Server {
	
	public static String ADDRESS;
	public static final int PORT = 10000;
	
	// Port used to use users in machine
	private static int portUser = 6000;

	private static Vector<User> userConnect;

	public Server() throws UnknownHostException{
		ADDRESS = InetAddress.getLocalHost().getHostAddress();
		
		userConnect = new Vector<User>();
		startConnection();
	}

	public static void startConnection() {
		PutUserInactive jobRemoveUser = new PutUserInactive(userConnect);
		Thread thread = new Thread(jobRemoveUser);
		thread.start();
		
		try{
			// When finish program close loginSocket
			ServerSocket loginSocket = new ServerSocket(Server.PORT);
			System.out.println("Server is running on " + ADDRESS + ":" + PORT);
			while(true){
				Socket connectionSocket = loginSocket.accept();
				Scanner inFromClient = new Scanner(connectionSocket.getInputStream());
				PrintWriter outToClient = new PrintWriter(connectionSocket.getOutputStream());

				RequestType requestTypeReceived = Enum.valueOf(RequestType.class, inFromClient.nextLine());
				
				switch (requestTypeReceived) {
				case LOGIN:
					processLoginRequest(inFromClient, outToClient);
					break;
				case KEEPALIVE:
					processKeepAliveRequest(inFromClient, outToClient);
					break;
				case LOGOUT:
					processLogoutRequest(inFromClient, outToClient);
					break;
				case DATA:
					processDataRequest(inFromClient, outToClient);
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Login
	 * @param inFromClient
	 * @param outToClient
	 */
	private static void processLoginRequest(Scanner inFromClient, PrintWriter outToClient) {
		String msgUser = inFromClient.nextLine();
		String[] parse = msgUser.split(",");
		User user = loginUser(parse[0], parse[1], parse[2]);

		if(user != null) {
			System.out.println(user.getUserName() + " login on " + user.getIp() + ":" + user.getPort());
			
			// Username and Password ok
			outToClient.println("1");

			// Send Friends List
			responseFriendList(user.getListFriends(), outToClient);

			// Port set to this user
			outToClient.println(portUser);

		} else{
			// Wrong user or password
			outToClient.println("0");
		}
		
		outToClient.flush();
	}
	
	/**
	 * KeepAlive
	 * @param inFromClient
	 * @param outToClient
	 */
	private static void processKeepAliveRequest(Scanner inFromClient, PrintWriter outToClient) {
		String msgUser = inFromClient.nextLine();
		User user = new User(msgUser);
		user = findUser(user);
		
		if(user != null){
			user.setLastKeepAlive(System.currentTimeMillis());
			responseFriendList(user.getListFriends(), outToClient);
		}
		
		outToClient.flush();
	}
	
	/**
	 * Logout
	 * @param inFromClient
	 * @param outToClient
	 */
	private static void processLogoutRequest(Scanner inFromClient, PrintWriter outToClient) {
		String msgUser = inFromClient.nextLine();
		User user = new User(msgUser);
		userConnect.remove(user);
		outToClient.println("-1");
		outToClient.flush();
		System.out.println(user.getUserName() + " logout");
	}
	
	/**
	 * Data
	 * @param inFromClient
	 * @param outToClient
	 */
	private static void processDataRequest(Scanner inFromClient, PrintWriter outToClient) {
		System.out.println("DATA");
		String msgUser = inFromClient.nextLine();
		User user = findUser(new User(msgUser));
		
		if(user!=null){
			outToClient.println(user.getIp()+","+user.getPort());
		} else { outToClient.println(""); }
		
		outToClient.flush();
	}
	
	private static void responseFriendList(List<User> friendsList, PrintWriter outToClient) {
		outToClient.println(friendsList.size());
		for(User us : friendsList){
			User updatedUser = findUser(us);
			if(updatedUser != null){
				outToClient.println(updatedUser.getUserName()+",online,"+updatedUser.getIp()+","+updatedUser.getPort());
			}else{
				outToClient.println(us.getUserName()+",offline,"+us.getIp()+","+us.getPort());
			}
		}
	}

	private static User loginUser(String username, String password, String ip){
		Scanner scanner = null;
		try{
			scanner = new Scanner(new File("usuarios.txt"));
			while(scanner.hasNextLine()){				
				String[] line = scanner.nextLine().split("_");
				if(line[0].equals(username) && line[1].equals(password)){
					User user = new User(username, password);
					user.setConnect(true);
					user.setIp(ip);
					
					// Control ports to use in same machine
					portUser += 1;

					// Set Port
					user.setPort(portUser);
					// Set KeepAlive
					user.setLastKeepAlive(System.currentTimeMillis());
					
					addUser(user);

					String[] friends = line[2].split(" ");

					for(int i = 0; i < friends.length; i++){
						User usr = new User(friends[i]);
						User aux = findUser(usr);
						if(aux != null){
							usr.setConnect(true);
							usr.setPort(aux.getPort());
						}
						user.getListFriends().add(usr);
					}
					return user;
				}
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		return null;
	}

	public static void addUser(User user){
		userConnect.addElement(user);
	}

	public static boolean isConnect(User user){
		for(int i = 0; i < userConnect.size(); i++){
			if(userConnect.get(i).getUserName().equals(user.getUserName())){
				return true;
			}
		}
		return false;
	}

	public static String getIpUser(User user){
		for(int i = 0; i < userConnect.size(); i++){
			if(userConnect.get(i).getUserName().equals(user.getUserName())){
				return userConnect.get(i).getIp();
			}
		}
		return "";
	} 

	public static User findUser(User user){
		for(int i = 0; i < userConnect.size(); i++){
			if(userConnect.get(i).getUserName().equals(user.getUserName())){
				return userConnect.get(i);
			}
		}
		return null;
	}

	public static void main(String[] args) throws UnknownHostException {
		new Server();
		
	}
}
