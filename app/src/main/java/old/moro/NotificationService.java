package old.moro;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import org.rpanic1308.Constants;

import ai.kitt.snowboy.SnowboyMaster;
import old.moroUbernahme.Methods;

/**
 * Created by morot on 08.06.2017.
 */

public class NotificationService extends Service {

    public final int id = 99;
    SharedPreferences prefs;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        boolean boo = intent.getBooleanExtra("extra", false);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("running", false);
        editor.apply();

        //Notification
        Intent volume = new Intent(this, NotificationReceiver.class);
        PendingIntent pVolume = PendingIntent.getBroadcast(this, 1, volume, 0);

        if(boo)
        {
            Notification noti = Methods.displayNotification(getBaseContext(), pVolume);
            this.startForeground(Constants.notificationId, noti);
        }
        else
        {
            Notification noti = Methods.displayNotification(getBaseContext(), pVolume);
            this.startForeground(Constants.notificationId, noti);
        }


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SnowboyMaster.stopRecording();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("running", false);
        editor.apply();
    }
}
