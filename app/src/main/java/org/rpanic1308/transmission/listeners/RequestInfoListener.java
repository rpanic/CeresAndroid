package org.rpanic1308.transmission.listeners;

import java.io.IOException;

import org.rpanic1308.ceres.MainFeedActivity;
import org.rpanic1308.main.CeresController;
import org.rpanic1308.music.AudioInfo.AudioType;
import org.rpanic1308.music.MusicPlayer;
import org.rpanic1308.transmission.AppToServerInfo;
import org.rpanic1308.transmission.ServerListener;
import org.rpanic1308.transmission.ServerToAppInfo;
import org.rpanic1308.transmission.SocketHolder;

public class RequestInfoListener implements ServerListener {

	@Override
	public void onRecieve(ServerToAppInfo info, SocketHolder holder) {
		if(info.getType() == ServerToAppInfo.TransType.Request){
			String b = "false";
			if(info.getAudioInfo().getType() == AudioType.IS_INTERRUPTED){
				boolean temp = MusicPlayer.getInstance().isInterrupted();
				b = Boolean.toString(temp);
			}else if(info.getAudioInfo().getType() == AudioType.IS_RUNNING){
				boolean temp = MusicPlayer.getInstance().isRunning();
				b = Boolean.toString(temp);
			}else if(info.getAudioInfo().getType() == AudioType.GET_LINE_GAIN){
				b = Float.toString(MusicPlayer.getInstance().getLineGain(MainFeedActivity.mainActivity));
			}
			AppToServerInfo reply = new AppToServerInfo(org.rpanic1308.transmission.AppToServerInfo.TransType.REPLY, b, CeresController.CLIENTID);
			System.out.println("Writing RequestInfoListener: " + reply.toString());
			try {
				holder.getOut().writeObject(reply);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
