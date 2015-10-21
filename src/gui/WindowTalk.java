package gui;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import socket.client.ReceiveMsg;
import socket.server.RequestType;
import socket.server.Server;
import entity.User;

public class WindowTalk extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	User user;
	public User selected;
	JTextArea msg;
	JButton sendMessageButton;
	Socket clientSocket;
	ReceiveMsg receber;
	PrintWriter outServer;

	public JTextArea conversa;
	
	public WindowTalk(User user,User selected){
		super("Conversa com " + selected.getUserName());
		
		Container window = getContentPane();
		window.setLayout(new GridLayout(user.getListFriends().size(), 2,10,10));
		
		conversa = new JTextArea();
		conversa.setEditable(false);
		msg = new JTextArea();
		
		sendMessageButton = new JButton("Enviar");
		sendMessageButton.setVisible(true);
		sendMessageButton.addActionListener(this);
		
		window.add(conversa);
		window.add(msg);
		window.add(new JScrollPane(conversa));
		window.add(new JScrollPane(msg));
		window.add(sendMessageButton);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 200);
		setVisible(true);
		
		this.user     = user;
		this.selected = selected;
	}	
	
	/**
	 * Send Data Button action
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Scanner msgServer = null;
		try {
			Socket receiveData = new Socket(Server.ADDRESS,Server.PORT);
			PrintWriter writeToServer = new PrintWriter(receiveData.getOutputStream());
			writeToServer.println(RequestType.DATA.name());
			writeToServer.println(selected.getUserName());
			writeToServer.flush();
			msgServer = new Scanner(receiveData.getInputStream());
			String adress = msgServer.nextLine();
			String[] parse = adress.split(",");
			receiveData.close();
			if(parse.length != 2) JOptionPane.showMessageDialog(this, "Amigo não encontrado");
			else{
				selected.setIp(parse[0]);
				selected.setPort(Integer.parseInt(parse[1]));
				System.out.println(selected.getPort());
				clientSocket = new Socket(selected.getIp(),selected.getPort());
				outServer = new PrintWriter(clientSocket.getOutputStream());
				if(!msg.getText().trim().isEmpty()){
					outServer.println(msg.getText());
					outServer.println(user.getUserName());
					outServer.flush();
					conversa.append("\n");
					conversa.append(msg.getText());
				}
			}
			msg.setText("");
			msg.requestFocus();
			
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(this, "Erro no estabelecimento da conexão com " + selected.getUserName());
			e1.printStackTrace();
			dispose();
		}finally{
			if (msgServer != null) { msgServer.close(); }
		
			try {
				if(clientSocket != null) { clientSocket.close(); }
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
				
	}
		
}