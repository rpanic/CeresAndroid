package org.rpanic1308.transmission;

import java.io.Serializable;
import java.util.Arrays;

public class AppToServerInfo implements Serializable{

	private static final long serialVersionUID = -5792855631575686938L;
	TransType type;
	Object data;
	private String[] splitted;
	String clientId;
	
	public enum TransType{
		TEXT, ACTION, CHECKFORUPDATES, REPLY, FEEDACTION
	}
	
	public AppToServerInfo(TransType type, String data, String clientId) {
		super();
		this.type = type;
		this.data = data;
		this.clientId = clientId;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public TransType getType() {
		return type;
	}

	public void setType(TransType type) {
		this.type = type;
	}

	public String getData() {
		return data.toString();
	}
	
	public Object getObjectData(){
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public Action getAction(){
		if(type == TransType.ACTION){
			split();
			Object o = null;
			try {
				o = Class.forName(splitted[0]).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			if(o != null && o instanceof Action){
				return (Action)o;
			}
		}
		return null;
	}
	
	private void split(){
		if(splitted == null){
			splitted = getData().split(";");
		}
	}
	
	public String getActionSentence(){
		if(type == TransType.ACTION){
			split();
			return splitted[1];
		}
		return null;
	}

	@Override
	public String toString() {
		return "AppToServerInfo [type=" + type + ", data=" + data + ", splitted=" + Arrays.toString(splitted)
				+ ", clientId=" + clientId + "]";
	}
	
	
}
