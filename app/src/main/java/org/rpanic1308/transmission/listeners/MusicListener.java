package org.rpanic1308.transmission.listeners;

import android.content.Intent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;

import org.rpanic1308.ceres.MainFeedActivity;
import org.rpanic1308.music.AudioInfo;
import org.rpanic1308.music.MusicPlayer;
import org.rpanic1308.music.SpotifyMusicPlayer;
import org.rpanic1308.spotify.SpotifyIntents;
import org.rpanic1308.transmission.Logic;
import org.rpanic1308.transmission.ServerListener;
import org.rpanic1308.transmission.ServerToAppInfo;
import org.rpanic1308.transmission.SocketHolder;

public class MusicListener implements ServerListener {

	@Override
	public void onRecieve(ServerToAppInfo info, SocketHolder holder) {
		ObjectInputStream in = holder.getIn();
		if(info.getType() == org.rpanic1308.transmission.ServerToAppInfo.TransType.AudioInfo){
			
			AudioInfo audio = info.getAudioInfo();
			SpotifyMusicPlayer player = SpotifyMusicPlayer.getInstance();
			
			switch(audio.getType()){
			case CLEARQUEUE:
				player.clear();
				break;
			case END_ALARM:
				player.endInterrupt();
				break;
			case PAUSE:
				player.pause();
				break;
			case RESUME:
				player.resume();
				break;
			case SKIP_TRACK:
				player.skipTitle();
				break;
			case START:
				player.resume(); //TODO Schauen ob start noch einen Sinn macht beim neuen Player
				break;
			case TRACKINFO_ADDTOQUEUE:
            case TRACKINFO:    //TODO Wegen Addtoqueue und normalem Abspielen
				player.addSong(audio);
				/*Logic.copy(in, info.getAudioInfo(), new Invokation() {
					public void action(Object o) {
						try {
							MusicPlayer.getInstance().addSong(new FileInputStream((File)o));
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}});*/
				break;
			case TRACKINFO_ALARM:
				/*Logic.copy(in, info.getAudioInfo(), new Invokation() {
					public void action(Object o) {
						try {
							MusicPlayer.getInstance().interrupt(new FileInputStream((File)o));
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}});*/
				break;
			case ADD_LINE_GAIN:
				player.addLineGain(Double.valueOf(info.getAudioInfo().getTitle()).intValue(), MainFeedActivity.mainActivity);
				break;
			case SET_LINE_GAIN:
				player.setLineGain(Double.valueOf(info.getAudioInfo().getTitle()).intValue(), MainFeedActivity.mainActivity);
				break;
			default:
				break;
			}
		}
		
	}

}
