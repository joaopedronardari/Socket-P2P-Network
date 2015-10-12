package socket;

import java.util.TreeMap;

import entity.User;

public class Server {

	public static final String ADDRES = "127.1.0.3";
	public static final int PORT = 10000;
	private static TreeMap<User, String> userConnect = new TreeMap<User, String>();
	
	public static void addUser(User user){
		userConnect.put(user, user.getIp());
		user.setConnect(true);
	}
	
	public static boolean isConnect(User user){
		return userConnect.containsKey(user);
	}
	
	public static String getIpUser(User user){
		return userConnect.get(user.getIp());
	}
}
