package socket.server;

import java.util.Vector;
import entity.User;

// FIXME Refactor Needed
public class PutUserInactive implements Runnable {

	Vector<User> listUser;

	public PutUserInactive(Vector<User>listUser) {
		this.listUser = listUser;
	}

	@Override
	public void run() {
		try {
			while(true){
				removeUserInactive();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeUserInactive() throws Exception{
		System.out.println("remover usuários inativos");
		if (!listUser.isEmpty()) {
			for (int i = 0; i < listUser.size(); i++) {
				if ((System.currentTimeMillis() - listUser.get(i).getLastKeepAlive()) > 5000) {
					listUser.remove(i);
				}
			}
		}
		Thread.sleep(5000);
	}
}
