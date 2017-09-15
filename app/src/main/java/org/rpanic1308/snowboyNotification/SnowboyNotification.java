package org.rpanic1308.snowboyNotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import org.rpanic1308.Constants;
import org.rpanic1308.recordingActivity.RecordingActivity;

import ai.kitt.snowboy.SnowboyMaster;
import rpanic1308.ceres.R;

/**
 * Created by Team_ on 18.08.2017.
 */

public class SnowboyNotification {

    static SharedPreferences prefs;
    Notification notification = null;

    public Notification getNewNotification(Context context, PendingIntent pVolume, PendingIntent recordingIntent){
        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        NotificationCompat.Action action = null;

        String s = "";
        if(SnowboyMaster.isRunning())////wenns eingeschaltet werden sollte
        {
            s = "Hotword detection läuft";
            action = new NotificationCompat.Action.Builder(0, "AUSSCHALTEN", pVolume)
                    .build();


        }
        else if(!SnowboyMaster.isRunning())////wenns ausgeschaltet werden sollte
        {
            s = "Hotword detection läuft nicht";
            action = new NotificationCompat.Action.Builder(0, "EINSCHALTEN", pVolume)
                    .build();

        }

        NotificationCompat.Action recordAction = new NotificationCompat.Action.Builder(android.R.drawable.ic_btn_speak_now, null, recordingIntent).build();

        notification = new NotificationCompat.Builder(context)
                .setSmallIcon(getIcon(SnowboyMaster.isRunning()))
                .setPriority(Notification.PRIORITY_LOW)
                .setContentTitle("Hotword Detection")
                .setContentText(s)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_cerespurple2))
                .addAction(action)
                .addAction(recordAction)
                .setColor(Color.argb(100, 97, 59, 170))
                .setOngoing(true)
                .build();

        return notification;
    }

    private int getIcon(boolean running){
        if(running){
            return R.mipmap.ic_done_white_24dp;
        }else{
            return R.mipmap.ic_stop_white_24dp;
        }
    }

    private void displayNotification(Notification notification, Context context){

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(Constants.notificationId, notification);

    }

    public void setNotificationRunningStatus(boolean running, Context context){

        Notification notification = getNewNotification(context, makeStandardPendingIntent(context), makeRecordingActivityPendingIntent(context));
        displayNotification(notification, context);

    }

    public void destroyNotification(Context context){
        if(notification != null) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(Constants.notificationId);
        }
    }

    public static PendingIntent makeRecordingActivityPendingIntent(Context context){

        Intent volume = new Intent(context, RecordingActivity.class);
        PendingIntent pVolume = PendingIntent.getActivity(context, 2, volume, PendingIntent.FLAG_UPDATE_CURRENT);
        return pVolume;

    }

    public static PendingIntent makeStandardPendingIntent(Context context)
    {
        Intent volume = new Intent(context, SnowboyNotificationReciever.class);
        PendingIntent pVolume = PendingIntent.getBroadcast(context, 1, volume, PendingIntent.FLAG_UPDATE_CURRENT);
        return pVolume;
    }

}
