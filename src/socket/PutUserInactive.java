package socket;

import java.util.TreeMap;

import entity.User;

public class PutUserInactive implements Runnable {

	TreeMap<User, String> listUser;

	public PutUserInactive(TreeMap<User, String> listUser) {
		this.listUser = listUser;
	}

	@Override
	public void run() {
		try {
			System.out.println("remover usuários inativos");
			if (!listUser.isEmpty()) {
				for (User user : listUser.keySet()) {
					if ((System.currentTimeMillis() - user.getLastKeepAlive()) > 5000) {
						listUser.remove(user);
					}
				}
			}
			Thread.sleep(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
