class WindowTalk{
	User usr;
	User selected;

	public WindowTalk(User usr,User selected){
		super("Conversa com " +selected);
		JTextArea conversa = new JTextArea();
		conversa.setEditable(false);
		JTextArea msg = new JTextArea();

	}

}