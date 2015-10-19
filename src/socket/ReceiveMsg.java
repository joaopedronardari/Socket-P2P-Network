package socket;

import java.awt.Event;
import java.awt.print.Printable;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import entity.User;
import gui.WindowTalk;
public class ReceiveMsg extends Thread{
	User user;
	List<WindowTalk> conversas;
	String msg;
	
	public ReceiveMsg(User usr){
		this.user = usr;
		conversas = new ArrayList<WindowTalk>();
	}
	
	public void add(WindowTalk t){
		conversas.add(t);
	}
	
	@Override
	public void run(){
		try {
			ServerSocket welcome = new ServerSocket(2879);
			while(true){
				Socket connection = welcome.accept();
				BufferedReader inFromOthers = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				DataOutputStream outToOthers = new DataOutputStream(connection.getOutputStream());
				String msg = inFromOthers.readLine();
				System.out.println(msg);
				String[]parse = msg.split("////");
				for(int i = 0;i < conversas.size();i++){
					WindowTalk aux = conversas.get(i);
					if(aux.selected.getUserName().equals(parse[2])) aux.conversa.append(parse[0]);
					//connection.
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
