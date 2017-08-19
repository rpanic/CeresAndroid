package org.rpanic1308.snowboyNotification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ai.kitt.snowboy.SnowboyMaster;

/**
 * Created by Team_ on 18.08.2017.
 */

public class SnowboyNotificationReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println(this.getClass().getName() + " Executed");

        SnowboyNotificationHandler.getInstance(context).switchSnowboyRunning();
        SnowboyNotificationHandler.getInstance(context).updateNotification();

    }

}
