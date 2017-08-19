package org.rpanic1308.transmission;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketHolder implements Closeable, AutoCloseable{

	Socket s;
	ObjectInputStream in;
	ObjectOutputStream out;
	String ClientId;
	public Socket getS() {
		return s;
	}
	public void setS(Socket s) {
		this.s = s;
	}
	public ObjectInputStream getIn() {
		return in;
	}
	public void setIn(ObjectInputStream in) {
		this.in = in;
	}
	public ObjectOutputStream getOut() {
		return out;
	}
	public void setOut(ObjectOutputStream out) {
		this.out = out;
	}
	public String getClientId() {
		return ClientId;
	}
	public void setClientId(String clientId) {
		ClientId = clientId;
	}
	public SocketHolder(Socket s) {
		super();
		this.s = s;
		
		try {
			this.out = new ObjectOutputStream(s.getOutputStream());
			this.in = new ObjectInputStream(s.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void close() throws IOException {
		s.close();
		in.close();
		out.close();
	}
	
	
	
}
