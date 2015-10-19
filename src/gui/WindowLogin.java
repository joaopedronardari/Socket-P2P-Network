package gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import entity.User;
import socket.Server;


public class WindowLogin extends JFrame implements ActionListener{

	private JTextField userField;
	private JPasswordField passwordField;
	private JLabel userLabel;
	private JLabel passwordLabel;
	private Container window;
	
	public WindowLogin() {
		super("Login");
		
		userLabel = new JLabel("Usuário: ");
		userLabel.setToolTipText("Digite nome do usuário");
		userField = new JTextField();
		
		passwordField = new JPasswordField(); 
		passwordLabel = new JLabel("Senha: ");
		passwordLabel.setToolTipText("Digite a senha");
		
		window = getContentPane();
		window.setLayout(new GridLayout(4, 2,10,10));
		window.add(userLabel);
		window.add(userField);
		window.add(passwordLabel);
		window.add(passwordField);
		JButton buttonCancel = new JButton("Cancelar");
		buttonCancel.addActionListener(this);
		
		window.add(buttonCancel);		
		JButton buttonConection = new JButton("Conectar");
		buttonConection.addActionListener(this);
		window.add(buttonConection);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 200);
		setVisible(true);
	}
		
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("Cancelar")){
			userField.setText("");
			passwordField.setText("");
		}else{
			try{
				Socket userSocket = new Socket(Server.ADDRES, Server.PORT);
				PrintWriter outServer = new PrintWriter(userSocket.getOutputStream());
				outServer.println("login");
				outServer.println(userField.getText()+" "+passwordField.getText());
				outServer.flush();
				Scanner msgServer = new Scanner(userSocket.getInputStream());

				User user = new User(userField.getText());
				
				// SERVIDOR RETORNA A PRIMEIRA LINHA COMO 1 (LOGIN FEITO COM SUCESSO)
				// E RETORNA A LISTA DE AMIGOS DO CLIENTE
				if(msgServer.nextLine().equals("1")){
					int size = Integer.parseInt(msgServer.nextLine());
					user.setConnect(true);
					for( int j = 0; j < size; j++){
						String[] parse = msgServer.nextLine().split(" - ");
						User friend = new User(parse[0]);
						if(parse[1].equals("Offline")){
							friend.setConnect(false);
						}else{
							friend.setConnect(true);
						}
						user.getListFriends().add(friend);
					}
					dispose();
					//AVISO PARA O GUILHERME APAGAR O COMENTÁRIO DEPOIS.
					//CARA O MÉTODO NECESSITA DE UM OBJETO DO TIPO USER
					//MAS EU NÃO CONSIGO SERIALIZAR O OBJETO PARA PASSAR PELO SOCKET
					//ENTAO CRII O OBJETO NO LADO CLIENTE COM BASE NA RESPOSTA DO SERVIDOR
					new WindowListFriends(user);
				}else{
					//SERVIDOR RETORNOU ZERO (SENHA OU USUÁRIO INCORRETO)
					new JOptionPane().showMessageDialog(this, "Usuário ou senha incorreto");
				}
			}catch(Exception exception){
				exception.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new WindowLogin();
	}
}
