package gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import entity.User;
import socket.server.RequestType;
import socket.server.Server;


public class WindowLogin extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JTextField userField;
	private JPasswordField passwordField;
	private JLabel userLabel;
	private JLabel passwordLabel;
	private JButton buttonCancel;
	private JButton buttonConnect;
	private Container window;
	
	public WindowLogin() {
		super("Login");
		
		userLabel = new JLabel("Usuário: ");
		userLabel.setToolTipText("Digite nome do usuário");
		userField = new JTextField();
		
		passwordField = new JPasswordField(); 
		passwordLabel = new JLabel("Senha: ");
		passwordLabel.setToolTipText("Digite a senha");
		
		buttonCancel = new JButton("Limpar");
		buttonCancel.addActionListener(this);
		
		buttonConnect = new JButton("Conectar");
		buttonConnect.addActionListener(this);
		
		window = getContentPane();
		window.setLayout(new GridLayout(4, 2,10,10));
		window.add(userLabel);
		window.add(userField);
		window.add(passwordLabel);
		window.add(passwordField);
		window.add(buttonCancel);		
		window.add(buttonConnect);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 200);
		setVisible(true);
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Limpar")){
			userField.setText("");
			passwordField.setText("");
		} else{
			
			Socket userSocket = null;
			Scanner msgServer = null;
			
			try{
				userSocket = new Socket(Server.ADDRESS, Server.PORT);
				PrintWriter outServer = new PrintWriter(userSocket.getOutputStream());
				outServer.println(RequestType.LOGIN.name());
				outServer.println(userField.getText()+","+
				new String(passwordField.getPassword())+","+
				InetAddress.getLocalHost().getHostAddress());
				outServer.flush();
				
				msgServer = new Scanner(userSocket.getInputStream());

				User user = new User(userField.getText());
				
				// Server returns 1 (Login successful) and the friends list
				if(msgServer.nextLine().equals("1")){
					// FIXME - Duplicated Code
					int size = Integer.parseInt(msgServer.nextLine());
					user.setConnect(true);
					for( int j = 0; j < size; j++){
						String[] parse = msgServer.nextLine().split(" - ");
						User friend = new User(parse[0]);
						if(parse[1].equals("offline")){
							friend.setConnect(false);
						}else{
							friend.setConnect(true);
						}
						user.getListFriends().add(friend);
					}
					user.setPort(Integer.parseInt(msgServer.nextLine()));
					System.out.println(user.getPort());
					dispose();
					new WindowListFriends(user);
				}else{
					// Incorrect user or password
					JOptionPane.showMessageDialog(this, "Usuário ou senha incorreto");
				}
			}catch(Exception exception){
				if(exception instanceof ConnectException){
					JOptionPane.showMessageDialog(this, "Erro no estabelecimento da conexão, verifique o servidor");
				}
				else exception.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new WindowLogin();
	}
}
