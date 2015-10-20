package gui;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.jws.soap.SOAPBinding.Use;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;










import socket.ReceiveMsg;
//import socket.SendMessage;
//import socket.Server;
import entity.User;

public class WindowTalk extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	User user;
	public User selected;
	JTextArea msg;
	Socket clientSocket;
	ReceiveMsg receber;
	DataOutputStream outToServer;
	BufferedReader inFromServer;
	public JTextArea conversa;
	//
	public WindowTalk(User user,User selected){
		super("Conversa com " +selected);
		Container window = getContentPane();
		window.setLayout(new GridLayout(user.getListFriends().size(), 2,10,10));
		conversa = new JTextArea();
		conversa.setEditable(false);
		msg = new JTextArea();
		JButton button = new JButton("Enviar");
		button.setVisible(true);
		button.addActionListener(this);
		window.add(conversa);
		window.add(msg);
		window.add(new JScrollPane(conversa));
		window.add(new JScrollPane(msg));
		window.add(button);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 200);
		setVisible(true);
		
		this.user     = user;
		this.selected = selected;
		
		try {
			clientSocket = new Socket(selected.getIp(),selected.getPort());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Erro no estabelecimento da conexão com " + selected.getUserName()+selected.getIp()+" "+selected.getPort());
			e.printStackTrace();
		}
	}	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if(!msg.getText().trim().isEmpty()){
						
				outToServer = new DataOutputStream(clientSocket.getOutputStream());
				inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				outToServer.writeBytes("msg////" + msg.getText() + "////" +user.getUserName()+ "\n");
				conversa.append(msg.getText());
				outToServer.close();
				inFromServer.close();
			}
			
			msg.setText("");
			msg.requestFocus();
			
		} catch (IOException e1) {
			System.out.println("Erro no envio da mensagem");
			e1.printStackTrace();
		}
		
		
	}
		
}