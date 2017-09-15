package old.moroUbernahme;

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

import ai.kitt.snowboy.SnowboyMaster;
import old.moro.NotiReceiver;
import rpanic1308.ceres.R;

/**
 * Created by morot on 23.06.2017.
 */

public class Methods {
    static SharedPreferences prefs;

    public static Notification displayNotification(Context context, PendingIntent pVolume){
        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Notification notification = null;

        NotificationCompat.Action action = null;

        String s = "";
        if(prefs.getBoolean("running", false))//wenns eingeschaltet werden sollte
        {
            s = "Hotword detection läuft";
            action = new NotificationCompat.Action.Builder(android.R.drawable.sym_action_call, "AUSSCHALTEN", pVolume)
                    .build();


        }
        else if(!prefs.getBoolean("running", false))//wenns ausgeschaltet werden sollte
        {
            s = "Hotword detection läuft nicht";
            action = new NotificationCompat.Action.Builder(android.R.drawable.sym_action_call, "EINSCHALTEN", pVolume)
                    .build();

        }

        notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(Notification.PRIORITY_LOW)
                .setContentTitle("Hotword Detection")
                .setContentText(s)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_cerespurple2))
                .addAction(action)
                .setColor(Color.argb(100, 97, 59, 170))
                .build();




        //NotificationManager
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(Constants.notificationId, notification);

        return notification;
    }

    public static void destroyNotification(Context context){
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(Constants.notificationId);
    }

    //<weg>
    public static void turnRecordingServiceOnOrOff(Context context, boolean boo)
    {
        PendingIntent intent = Methods.makeStandardPendingIntent(context);
        Methods.displayNotification(context, intent);
    }

    public static void setRunningStatus(Context context, boolean boo)
    {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("running", boo);
        editor.apply();
    }

    public static PendingIntent makeStandardPendingIntent(Context context)
    {
        Intent volume = new Intent(context, NotiReceiver.class);
        PendingIntent pVolume = PendingIntent.getBroadcast(context, 1, volume, PendingIntent.FLAG_UPDATE_CURRENT);
        return pVolume;
    }

    public static void startRecording(Context context) {
        SnowboyMaster.continueRecording(context);
    }

    public static void stopRecording(Context context) {
        SnowboyMaster.stopRecording();
    }

    //</weg>
}
