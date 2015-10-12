package entity;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

public class User implements Comparable<User> {

	private String userName;
	private String password;
	private Socket signLife; 
	private boolean isConnect;
	private List<User> listFriends;
	private String ip;
	
	public User(String userName, String password){
		listFriends = new LinkedList<User>();
		setUserName(userName);
		setPassword(password);
		setConnect(false);
		setIp();
	}

	public User(String userName){
		this(userName, "");
	}

	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isConnect() {
		return isConnect;
	}

	public void setConnect(boolean isConnect) {
		this.isConnect = isConnect;
	}

	public String getIp() {
		return ip;
	}

	public void setIp() {
		try {
			this.ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("UnknownHost");
		}
	}
	
	public List<User> getListFriends(){
		return this.listFriends;
	}
	
	@Override
	public int compareTo(User user) {
		return this.userName.compareTo(user.getUserName());
	}
	
}
