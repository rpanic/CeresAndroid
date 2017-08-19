package org.rpanic1308.moroUbernahme;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import ai.kitt.snowboy.SnowboyMaster;

/**
 * Created by Team_ on 23.06.2017.
 */

public class OnOffReciever extends BroadcastReceiver{

    private static boolean turnedOffByUser = false;

    @Override
    public void onReceive(Context context, Intent intent) {


        Log.d("CERES", "OnOffReciever triggered");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        boolean running = prefs.getBoolean("running", false);//intent.getBooleanExtra("onoff", false);

        if(running != SnowboyMaster.isRunning()){
            Log.e("CERES", "Running and Running are not the same!!!");
        }

        if(HomeActivationThread.BroadcastIsHomeActivation){
            HomeActivationThread.BroadcastIsHomeActivation = false;

            if(turnedOffByUser && !running){
                return;
            }

        }else{
            if(running){
                turnedOffByUser = true;
            }else{
                if(turnedOffByUser){
                    turnedOffByUser = false;
                }
            }
        }

        editor.putBoolean("running", !running);
        editor.apply();
        //Wird eingeschaltet
        if(!running){
            try {
                SnowboyMaster.continueRecordingOverride();
            }catch(NullPointerException e){
                editor.putBoolean("running", false);
                editor.apply();
                Toast.makeText(context, "Snowboyservice läuft nicht", Toast.LENGTH_LONG).show();
            }
        //Wird ausgeschlatet
        }else{
            SnowboyMaster.stopRecording();
        }

        Intent i = new Intent(context, OnOffReciever.class);
        Methods.displayNotification(context, PendingIntent.getBroadcast(context, 1112, i, 0));

    }
}
