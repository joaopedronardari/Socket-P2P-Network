package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;







import socket.ReceiveMsg;
//import socket.SendMessage;
//import socket.Server;
import entity.User;

public class WindowTalk extends JFrame implements ActionListener{
	User usr;
	public User selected;
	JTextArea msg;
	Socket clientSocket;
	ReceiveMsg receber;
	DataOutputStream outToServer;
	BufferedReader inFromServer;
	public JTextArea conversa;

	public WindowTalk(User usr,User selected){
		super("Conversa com " +selected);
		conversa = new JTextArea();
		conversa.setEditable(false);
		msg = new JTextArea();
		JButton button = new JButton();
		button.setVisible(true);
		button.addActionListener(this);
		try {
			clientSocket = new Socket(selected.getIp(),2879);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Erro no estabelecimento da conexão com " + selected.getUserName());
			e.printStackTrace();
		}		
		
					
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if(!msg.getText().trim().isEmpty()){
						
				outToServer = new DataOutputStream(clientSocket.getOutputStream());
				inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				outToServer.writeBytes("msg////" + msg.getText() + "////" +usr.getUserName()+ "\n");
				conversa.append(msg.getText());
				msg.setText("");
				msg.requestFocus();
			}
			
			else{
				JOptionPane.showMessageDialog(this, "Sua mensagem não possui conteúdo");
				
			}
			
			//receber = new ReceiveMsg(usr,this);
			
			
		} catch (IOException e1) {
			System.out.println("Erro no envio da mensagem");
			e1.printStackTrace();
		}
		
		
	}
		
}