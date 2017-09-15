package old.moro;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import old.moroUbernahme.Methods;

/**
 * Created by morot on 08.06.2017.
 */

public class NotificationReceiver extends BroadcastReceiver {

    SharedPreferences prefs;

    @Override
    public void onReceive(Context context, Intent intent) {

        PendingIntent pVolume = Methods.makeStandardPendingIntent(context);

        Methods.displayNotification(context, pVolume);
    }






}
