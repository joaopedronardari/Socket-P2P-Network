package gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import entity.User;
import socket.Connection;

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
			Connection conection = new Connection();
			User user = conection.starConnection(userField.getText(), passwordField.getText());
			if(user == null){
				new JOptionPane().showMessageDialog(this, "Usuário ou senha incorreto");
			}else{
				dispose();
				new WindowListFriends(user);
			}
		}		
	}
	
	public static void main(String[] args) {
		new WindowLogin();
	}
}
