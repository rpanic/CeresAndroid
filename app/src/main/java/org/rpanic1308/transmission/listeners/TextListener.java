package org.rpanic1308.transmission.listeners;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;

import org.rpanic1308.ceres.MainFeedActivity;
import org.rpanic1308.music.MusicPlayer;
import org.rpanic1308.music.SpotifyMusicPlayer;
import org.rpanic1308.transmission.Logic;
import org.rpanic1308.transmission.ServerListener;
import org.rpanic1308.transmission.ServerToAppInfo;
import org.rpanic1308.transmission.ServerToAppInfo.TransType;
import org.rpanic1308.transmission.SocketHolder;

public class TextListener implements ServerListener{

	@Override
	public void onRecieve(ServerToAppInfo info, SocketHolder holder) {
		
		if(info.getType() == TransType.AusgabeText){
			
			Serializable data = info.getData();
			FileInputStream fis = null;
			if(data instanceof String){

				String msg = (String)data;
				InputStream is = org.rpanic1308.music.TextToSpeech.getVoiceAudio(msg);
				fis = Logic.copy(is);

			}else if(data instanceof String[]){

				String[] arr = (String[]) data;
				FileInputStream tempInputStream = null;
				int fileCount = Logic.getNewCount();
				for(String msg : arr){
					InputStream is = org.rpanic1308.music.TextToSpeech.getVoiceAudio(msg);
					tempInputStream = Logic.copy(is, true, fileCount);
				}
				fis = tempInputStream;

			}

			//MainController.instance.outputSentence(msg);

			if(fis == null){
				return;
			}
			SpotifyMusicPlayer.getInstance().interrupt(fis);
		}
	}
	
}
