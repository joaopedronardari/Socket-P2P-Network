package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;





//import socket.SendMessage;
import socket.Server;
import entity.User;

class WindowTalk extends JFrame implements ActionListener{
	User usr;
	User selected;
	JTextArea msg;
	Socket clientSocket;

	public WindowTalk(User usr,User selected){
		super("Conversa com " +selected);
		JTextArea conversa = new JTextArea();
		conversa.setEditable(false);
		msg = new JTextArea();
		JButton button = new JButton();
		button.setVisible(false);
		button.addActionListener(this);
		try {
			clientSocket = new Socket(Server.ADDRES,Server.PORT);
		} catch (IOException e) {
			System.out.println("Erro no estabelecimento da conexão com " +selected.getUserName());
			e.printStackTrace();
		}
		
		while(true){
			if(!msg.getText().trim().isEmpty()) button.setVisible(true);
			else button.setVisible(false);
		}
			
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToServer.writeBytes(msg.getText());
			
		} catch (IOException e1) {
			System.out.println("Erro no envio da mensagem");
			e1.printStackTrace();
		}
		
		
	}

}