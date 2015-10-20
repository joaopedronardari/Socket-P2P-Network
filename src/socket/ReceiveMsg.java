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
	List<User> friends;
	
	public ReceiveMsg(User usr,List<User> friends){
		this.user = usr;
		conversas = new ArrayList<WindowTalk>();
		this.friends = friends;
	}
	
	public void add(WindowTalk t){
		conversas.add(t);
	}
	
	@Override
	public void run(){
		// FIXME - Refactor Needed
		try {
			ServerSocket welcome = new ServerSocket(User.SOCKET_PORT);
			while(true){
				Socket connection = welcome.accept();
				BufferedReader inFromOthers = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				DataOutputStream outToOthers = new DataOutputStream(connection.getOutputStream());
				String msg = inFromOthers.readLine();
				System.out.println(msg);
				String[]parse = msg.split("////");
				
				// Check if has window
				boolean hasWindow = false;
				
				for(int i = 0;i < conversas.size();i++){
					WindowTalk talk = conversas.get(i);
					if(talk.selected.getUserName().equals(parse[2])) { 
						talk.conversa.append(parse[1]);
						hasWindow = true;
					}
				}
				
				if (!hasWindow) {
					User compare = new User(parse[2]);
					for(int j = 0;j < friends.size();j++){
						User aux = friends.get(j);
						if(compare.compareTo(aux) == 0){
							WindowTalk newTalk = new WindowTalk(user,aux);
							newTalk.conversa.append(parse[1]);
							newTalk.setVisible(true);
							conversas.add(newTalk);
						}
					}
				}
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
