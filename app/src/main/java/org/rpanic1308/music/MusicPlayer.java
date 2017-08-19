package org.rpanic1308.music;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Team_ on 27.03.2017.
 */

public class MusicPlayer {

    static MediaPlayer player;
    static MediaPlayer interrupt;
    private static List<FileInputStream> queue = new ArrayList<>();
    static MusicPlayer instance;
    //boolean skipNext = false;   //Überdenken ob Sinnhaftig


    /*static {
        if(player == null)
            player = new MediaPlayer();
    }*/

    public MusicPlayer(){

    }

    public void playSong(FileInputStream path){

        System.out.println("Song playing");

        if(player == null){
            player = new MediaPlayer();
            player.setOnCompletionListener(new CompletionListener());
        }
        player.reset();

        try {
            player.setDataSource(path.getFD());
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.prepare();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                path.close();
            } catch (IOException ignore) {
            }
        }
        player.start();
        System.out.println("Song played");
    }

    public void interrupt(FileInputStream path){
        if(interrupt != null){
            return;
        }
        if(player != null) {
            player.pause();
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
                    if(player != null) {
                        player.start();
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
        if(player != null) {
            player.start();
        }
    }

    public void addSong(FileInputStream path){
        queue.add(path);
        if(queue.size() == 1){
            playSong(path);
        }
    }

    public boolean isInterrupted(){
        return interrupt != null;
    }

    public boolean isRunning(){
        if(player != null)
            return player.isPlaying();
        else
            return false;
    }

    public void pause(){
        player.pause();
    }

    public void start(){
        player.start();
    }

    public void resume(){
        start();
    }

    @Deprecated
    public boolean isPlaying(){
        return player.isPlaying();
    }

    public void clear(){
        queue.clear();
        //interrupt.stop();  ??
    }

    public void skipTitle(){
        if(player != null){
            player.stop();
            new CompletionListener().onCompletion(player);
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

    public static MusicPlayer getInstance(){
        if(instance == null)
            instance = new MusicPlayer();
        return instance;
    }

    private static class CompletionListener implements MediaPlayer.OnCompletionListener {

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
    }

}
