package entity;

import java.util.LinkedList;
import java.util.List;

public class User implements Comparable<User> {

	public static final int SOCKET_PORT = 2878;
	
	private String userName;
	private String password; 
	private boolean isConnect;
	private List<User> listFriends;
	private String ip;
	private long lastKeepAlive;
	private int port;
	
	public User(String userName, String password){
		listFriends = new LinkedList<User>();
		setUserName(userName);
		setPassword(password);
		setConnect(false);
	}

	public User(String userName){
		this(userName, "");
	}
	
	public long getLastKeepAlive() {
		return lastKeepAlive;
	}

	public void setLastKeepAlive(long lastKeepAlive) {
		this.lastKeepAlive = lastKeepAlive;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPort(int port){
		this.port = port;
	}
	
	public int getPort(){
		return this.port;
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

	/*public void setIp() {
		try {
			this.ip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			System.out.println("UnknownHost");
		}
	}*/
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public List<User> getListFriends(){
		return this.listFriends;
	}
	
	public void setListFriends(List<User> listFriends){
		this.listFriends = listFriends;
	}
	
	@Override
	public int compareTo(User user) {
		return this.userName.compareTo(user.getUserName());
	}
	
	public String getStringStatus() {
		return isConnect() ? "online" : "offline";
	}
	
	@Override
	public String toString(){
		return this.getUserName()+" - "+getStringStatus();
	}

}