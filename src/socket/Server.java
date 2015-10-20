package socket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.TreeMap;

import entity.User;

public class Server {

	public static final String ADDRES = "127.0.0.1";
	public static final int PORT = 10000;
	private static TreeMap<User, String> userConnect;
	
	public Server(){
		 userConnect = new TreeMap<User, String>();
		 starConnection();
	}
	public static void addUser(User user){
		user.setConnect(true);
		userConnect.put(user, user.getIp());		
	}
	
	public static boolean isConnect(User user){
		return userConnect.containsKey(user);
	}
	
	public static String getIpUser(User user){
		return userConnect.get(user.getIp());
	} 
	
	public static String findUser(User user){
		return userConnect.get(user);
	}
	
	public static void starConnection() {
		//INICIA UM THREAD QUE FICA PEGANDO USUÁRIO INATIVO 
		PutUserInactive jobRemoveUser = new PutUserInactive(userConnect);
		Thread thread = new Thread(jobRemoveUser);
		thread.start();
		
		
		User user = null;
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
						outToClient.println(user.getListFriends().size());
						for(int i = 0; i < user.getListFriends().size(); i++){
							outToClient.println(user.getListFriends().get(i));
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
					if(userConnect.containsKey(user)){
						for(User u : userConnect.keySet()){
							if(u.getUserName().equals(user.getUserName())){
								u.setLastKeepAlive(System.currentTimeMillis());
								outToClient.println(u.getListFriends().size());
								for(User us : u.getListFriends()){
									if(userConnect.containsKey(us)){
										outToClient.println(us.getUserName()+" online");
									}else{
										outToClient.println(us.getUserName()+" offline");
									}
								}
							}
						}
						outToClient.flush();
					}
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
		 try{
			 Scanner sc = new Scanner(new File("usuarios.txt"));
			 while(sc.hasNextLine()){				
					String[] line = sc.nextLine().split("_");
					if(line[0].equals(username) && line[1].equals(password)){
						User user = new User(username, password);
						user.setConnect(true);
						addUser(user);
						String[] friends = line[2].split(" ");
						for(int i = 0; i < friends.length; i++){
							User usr = new User(friends[i]);
							if(findUser(usr) != null){
								usr.setConnect(true);
							}
							user.getListFriends().add(usr);
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
