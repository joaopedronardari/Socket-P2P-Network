package socket.client;

import java.io.BufferedReader;

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
		// FIXME - Close Welcome Socket
		try {
			ServerSocket welcome = new ServerSocket(user.getPort());
			System.out.println(user.getPort());
			while(true){
				Socket connection = welcome.accept();
				BufferedReader inFromOthers = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String msg = inFromOthers.readLine();
				String userName = inFromOthers.readLine();
				// Check if has window
				boolean hasWindow = false;
				
				for(int i = 0;i < conversas.size();i++){
					WindowTalk talk = conversas.get(i);
					if(talk.selected.getUserName().equals(userName)) {
						talk.conversa.append("\n");
						talk.conversa.append(msg);
						System.out.println(talk.conversa.getText());
						hasWindow = true;
					}
				}
				
				if (!hasWindow) {
					User compare = new User(userName);
					for(int j = 0;j < friends.size();j++){
						User aux = friends.get(j);
						if(compare.compareTo(aux) == 0){
							WindowTalk newTalk = new WindowTalk(user,aux);
							newTalk.setVisible(true);
							newTalk.conversa.append(msg);
							conversas.add(newTalk);
						}
					}
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
