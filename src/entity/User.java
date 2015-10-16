package entity;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import socket.Server;


public class User implements Comparable<User>, Runnable {

	private String userName;
	private String password; 
	private boolean isConnect;
	private List<User> listFriends;
	private String ip;
	private final int TIMEWAIT = 1000;
	private long lastSignLife;
	
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
	
	@Override
	public String toString(){
		String status = "Offline";
		if(this.isConnect()) status = "Online";
		return this.getUserName() +" - " +status;
	}
	
	public void signLife(){
		try{
			Socket socket = new Socket(Server.ADDRES, Server.PORT);
			DataOutputStream outServer = new DataOutputStream(socket.getOutputStream());
			outServer.writeInt(1);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		BufferedReader msgReturnServer;
		while(true){
			try{
				Socket socket = new Socket(Server.ADDRES, Server.PORT);
				DataOutputStream outServer = new DataOutputStream(socket.getOutputStream());
				outServer.writeInt(1);
				msgReturnServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				this.lastSignLife = System.currentTimeMillis();
				Thread.sleep(TIMEWAIT);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
}