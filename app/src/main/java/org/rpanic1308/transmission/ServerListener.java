package org.rpanic1308.transmission;

public interface ServerListener {

	public void onRecieve(ServerToAppInfo info, SocketHolder holder);
	
}
