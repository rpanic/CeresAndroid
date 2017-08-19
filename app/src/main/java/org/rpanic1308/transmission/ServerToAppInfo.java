package org.rpanic1308.transmission;

import java.io.Serializable;

import org.rpanic1308.music.AudioInfo;

public class ServerToAppInfo implements Serializable{

		private static final long serialVersionUID = 2365460736216813846L;
		TransType type;
		Serializable data;
		
		public enum TransType{
			AusgabeText, AudioInfo, Request, FeedItems
		}
		
		public ServerToAppInfo(TransType type, Serializable data) {
			super();
			this.type = type;
			this.data = data;
		}
		
		public ServerToAppInfo(Serializable data) {
			super();
			this.type = TransType.AusgabeText;
			this.data = data;
		}

		public TransType getType() {
			return type;
		}

		public void setType(TransType type) {
			this.type = type;
		}

		public String getStringData() {
			return data instanceof String ? (String) data : null;
		}

		public void setData(String data) {
			this.data = data;
		}
		
		public AudioInfo getAudioInfo(){
			return data instanceof AudioInfo ? (AudioInfo) data : null;
		}

		public Serializable getData(){
			return data;
		}

		@Override
		public String toString() {
			return "ServerToAppInfo [type=" + type + ", data=" + data.toString() + "]";
		}

}
