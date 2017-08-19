package org.rpanic1308.music;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;

import org.rpanic1308.ceres.MainFeedActivity;
import org.rpanic1308.feed.FeedItem;
import org.rpanic1308.feed.FeedSaver;
import org.rpanic1308.loadingScreen.LoadingActivity;
import org.rpanic1308.spotify.SpotifyIntents;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Team_ on 07.07.2017.
 */

public class SpotifyMusicPlayer {

    MediaPlayer interrupt;
    static SpotifyMusicPlayer instance;

    public SpotifyMusicPlayer(){

    }

    public void playSong(AudioInfo info){
        String uri = SpotifyIntents.getTrackUri(info, SpotifyIntents.getToken());
        SpotifyIntents.getPlayer().playUri(null, uri, 0, 0);
    }

    public void interrupt(FileInputStream path){
        if(interrupt != null){
            return;
        }
        if(SpotifyIntents.getPlayer() != null) {
            SpotifyIntents.getPlayer().pause(null);
        }
        interrupt = new MediaPlayer();
        interrupt.reset();
        try {
            interrupt.setDataSource(path.getFD());
            interrupt.prepare();
            interrupt.start();
            interrupt.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    interrupt = null;
                    if(SpotifyIntents.getPlayer() != null) {
                        SpotifyIntents.getPlayer().resume(null);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            interrupt.stop();
            interrupt = null;
        }
    }

    public void endInterrupt(){
        if(interrupt.isPlaying()){
            interrupt.stop();
        }
        interrupt = null;
        if(SpotifyIntents.getPlayer() != null) {
            SpotifyIntents.getPlayer().resume(null);
        }
    }

    public void addSong(AudioInfo info){
        String uri = SpotifyIntents.getTrackUri(info, SpotifyIntents.getToken());
        if(isRunning()){
            SpotifyIntents.getPlayer().queue(null, uri);
        }else {
            SpotifyIntents.getPlayer().playUri(null, uri, 0, 0);
        }
        resume();
    }

    //public boolean isInterrupted(){
    //    return interrupt != null;
    //}

    public boolean isRunning(){
        if(SpotifyIntents.getPlayer() != null)
            return SpotifyIntents.getPlayer().getPlaybackState().isPlaying;
        else
            return false;
    }

    public void pause(){
        SpotifyIntents.getPlayer().pause(null);
        MainFeedActivity.mainActivity.addFeedItem(FeedSaver.buildRemover(13));
    }

    //public void start(){
    //    SpotifyIntents.getPlayer().start();
    //}

    public void resume(){
        SpotifyIntents.getPlayer().resume(null);
        FeedItem item = new FeedItem();
        item.setId("13");
        item.setContent("TestText");
        item.setTimeStamp(System.currentTimeMillis());
        item.setPriority(1);
        item.setType("text");
        MainFeedActivity.mainActivity.addFeedItem(item);
    }

    public boolean isPlaying(){
        return SpotifyIntents.getPlayer().getPlaybackState().isPlaying;
    }

    public void clear(){
        //TODO
        //interrupt.stop();  ??
    }

    public void skipTitle(){
        if(SpotifyIntents.getPlayer() != null){
            SpotifyIntents.getPlayer().skipToNext(null);
            //new SpotifyMusicPlayer.CompletionListener().onCompletion(player);
        }
    }

    public int getLineGain(Context c){
        AudioManager audio = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        return currentVolume;
    }

    public void setLineGain(int gain, Context c){
        AudioManager audio = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, gain, 0);
    }

    public void addLineGain(int add, Context c){
        setLineGain(getLineGain(c) + add ,c);
    }

    public static SpotifyMusicPlayer getInstance(){
        if(instance == null)
            instance = new SpotifyMusicPlayer();
        return instance;
    }

    /*private static class CompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            mp.reset();
            queue.remove(0);
            if(!queue.isEmpty()){
                //if(!getInstance().skipNext) {
                getInstance().playSong(queue.get(0));
                //}else{
                //getInstance().skipNext = false;
                //MusicPlayer.queue.remove(0);
                //getInstance().playSong(queue.get(0));
                //}
            }else{
                player = null;
            }
        }
    }*/
}
