package org.rpanic1308.ceres;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Team_ on 30.01.2017.
 */

public class DisplayReciever extends BroadcastReceiver {

    Context c;

    @Override
    public void onReceive(Context context, Intent intent) {
        c = context;
        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){

            Intent t = new Intent(context, DisplaySpeechService.class);
            c.startService(t);

        }else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){

            Intent t = new Intent(context, DisplaySpeechService.class);
            c.stopService(t);

        }

    }

}
