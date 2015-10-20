package socket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.PortUnreachableException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Vector;

import entity.User;

public class Server {

	public static final String ADDRES = "127.0.0.1";
	public static final int PORT = 10000;
	//PORTA UTILIZADA PARA CONVERSAR COM OUTRO USUÁRIO LOCALMENTE
	private static int portUser = 6000;
	//private static TreeMap<User, String> userConnect;
	
	private static Vector<User> userConnect;

	
	public Server(){
		 //userConnect = new TreeMap<User, String>();
		userConnect = new Vector<User>();
		 starConnection();
	}
	public static void addUser(User user){
		user.setConnect(true);
		System.out.println("Porta ao adicionar usuario"+user.getPort());
		//userConnect.put(user, user.getIp());
		userConnect.addElement(user);
	}
	
	public static boolean isConnect(User user){
		//return userConnect.containsKey(user);
		for(int i = 0; i < userConnect.size(); i++){
			if(userConnect.get(i).getUserName().equals(user.getUserName())){
				return true;
			}
		}
		return false;
	}
	
	public static String getIpUser(User user){
		//return userConnect.get(user.getIp());
		for(int i = 0; i < userConnect.size(); i++){
			if(userConnect.get(i).getUserName().equals(user.getUserName())){
				return userConnect.get(i).getIp();
			}
		}
		return null;
	} 
	
	public static User findUser(User user){
		//return userConnect.get(user);
		for(int i = 0; i < userConnect.size(); i++){
			if(userConnect.get(i).getUserName().equals(user.getUserName())){
				return userConnect.get(i);
			}
		}
		return null;
	}
	
	public static void starConnection() {
		//INICIA UM THREAD QUE FICA PEGANDO USUÁRIO INATIVO 
		PutUserInactive jobRemoveUser = new PutUserInactive(userConnect);
		Thread thread = new Thread(jobRemoveUser);
		thread.start();
		
		
		User user = null;
		// FIXME - Refactor Needed
		 try{			
			ServerSocket loginSocket = new ServerSocket(Server.PORT);
			while(true){
				Socket connectionSocket = loginSocket.accept();
				Scanner inFromClient = new Scanner(connectionSocket.getInputStream());
				PrintWriter outToClient = new PrintWriter(connectionSocket.getOutputStream());
				String msgUser = inFromClient.nextLine();
				//System.out.println("SERVIDOR OPERCAO: "+msgUser);
				if(msgUser.equals("login")){
					msgUser = inFromClient.nextLine();
					String[] parse = msgUser.split(" ");
					user = loginUser(parse[0], parse[1]); 
					if(user != null){
						//MENSAGEM IGUAL 1 LOGIN FEITO COM SUCESSO
						//CONCATENA A LISTA DE AMIGOS
						user.setLastKeepAlive(System.currentTimeMillis());
						outToClient.println("1");
						outToClient.println(portUser);
						outToClient.println(user.getListFriends().size());
						for(int i = 0; i < user.getListFriends().size(); i++){
							User us = user.getListFriends().get(i);
							System.out.println("Estaos no servidor usuario"+us.getUserName()+" porta"+us.getPort()+" "+us.isConnect());
							if(findUser(us) != null){
								outToClient.println(us.getUserName()+",online,"+us.getIp()+","+us.getPort());
							}else{
								outToClient.println(us.getUserName()+",offline,"+us.getIp()+","+us.getPort());
							}
						}
					}else{
						
						//SENHA OU USERNAME INCORRETO
						outToClient.println("0");
					}
					outToClient.flush();
				}	
				else if(msgUser.equals("keepalive")){
					msgUser = inFromClient.nextLine();
					User usr = new User(msgUser);
					usr = findUser(usr);
					if(usr != null){
						usr.setLastKeepAlive(System.currentTimeMillis());
						outToClient.println(usr.getListFriends().size());
						for(User us : usr.getListFriends()){
							if(findUser(us) != null){
								outToClient.println(us.getUserName()+",online,"+us.getIp()+","+us.getPort());
							}else{
								outToClient.println(us.getUserName()+",offline,"+us.getIp()+","+us.getPort());
							}
						}
					}
					outToClient.flush();
				}else if (msgUser.equals("off")){
					System.out.println("OPERACAO: OFF");
					msgUser = inFromClient.nextLine();
					User usr = new User(msgUser);
					userConnect.remove(usr);
					outToClient.println("-1");
					outToClient.flush();
				}
			}
		}catch(Exception e){
				e.printStackTrace();
		}
	}
	
	 private static User loginUser(String username, String password){
		 // FIXME - Refactor needed
		 try{
			 Scanner sc = new Scanner(new File("usuarios.txt"));
			 while(sc.hasNextLine()){				
					String[] line = sc.nextLine().split("_");
					if(line[0].equals(username) && line[1].equals(password)){
						User user = new User(username, password);
						//FAZ O CONTROLE DA PORTA PARA CONVERSAR LOCALMENTE
						portUser += 1;
						user.setPort(portUser);
						user.setConnect(true);
						user.setLastKeepAlive(System.currentTimeMillis());
						addUser(user);
						String[] friends = line[2].split(" ");
						for(int i = 0; i < friends.length; i++){
							User usr = findUser(new User(friends[i]));
							if(usr != null){
								usr.setConnect(true);
								user.getListFriends().add(usr);
							}else{
								user.getListFriends().add(new User(friends[i]));
							}
						}
						System.out.println("USUARIO FEZ LOGGIN");
						return user;
					}
			 }
		 }catch(FileNotFoundException e){
			 e.printStackTrace();
		 }
		 return null;
	 }
	 
	 public static void main(String[] args) {
		new Server();
		
	}
}
