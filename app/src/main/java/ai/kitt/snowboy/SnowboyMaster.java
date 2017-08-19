package ai.kitt.snowboy;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.rpanic1308.ceres.MainFeedActivity;
import org.rpanic1308.music.MusicPlayer;

import ai.kitt.snowboy.audio.AudioDataSaver;
import ai.kitt.snowboy.audio.RecordingThread;
import ai.kitt.snowboy.audio.RecordingThreadCopy;

/**
 * Created by Team_ on 05.06.2017.
 */

public class SnowboyMaster {

    private static RecordingThreadCopy thread;

    public static void startSnowBoy(final SnowboyListener listener, final MainFeedActivity context){

        if(thread != null){
            Toast.makeText(context, "Error - Snowboy is already started", Toast.LENGTH_SHORT).show();
            Log.w("CERES", "Error - Snowboy is already started");
            return;
        }

        AppResCopy.copyResFromAssetsToSD(context);

        SnowboyListener listener1 = new SnowboyListener() {
            @Override
            public void callback(Object o) {

                if(o instanceof Integer){
                    int i = Integer.parseInt(o.toString());
                    if(i < 0){
                        //TODO Error
                        Toast.makeText(context, "Error " + i + " in Snowboy Hotword detection", Toast.LENGTH_SHORT).show();
                    }else{

                        listener.callback(i);

                    }
                }

            }
        };

        thread = new RecordingThreadCopy(listener1, new AudioDataSaver());
        thread.startRecording();
    }

    public static void stopRecording(){
        thread.stopRecording();
        Log.d("CERES", "StopRecording1");
    }

    public static void stopRecording(SnowboyListener callback){
        if(thread == null)
            return;
        thread.stopRecording(callback);
        Log.d("CERES", "StopRecording2");
    }

    public static void continueRecording(Context c){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
        if(thread == null/* || !prefs.getBoolean("running", false)*/) {
            System.out.println("continueRecording not executed properly");
            return;

        }
        thread.startRecording();
        Log.d("CERES", "StartRecording1");
    }

    public static void continueRecordingOverride(){
        if(thread == null)
            return;
        thread.startRecording();
        Log.d("CERES", "StartRecrodingOverride");
    }

    public static boolean isRunning(){
        if(thread == null){
            return false;
        }
        return thread.isRunning();
    }

    private static boolean reduced = false;

    public static void reduceVolume(Context c){
        if(!reduced) {
            MusicPlayer.getInstance().addLineGain(-1 * MusicPlayer.getInstance().getLineGain(c) / 3, c);
            reduced = true;
        }
    }

    public static void increaseVolume(Context c){
        if(reduced) {
            MusicPlayer.getInstance().addLineGain(MusicPlayer.getInstance().getLineGain(c)/2, c);
            reduced = false;
        }
    }

}
