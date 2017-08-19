package org.rpanic1308.ceres;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Team_ on 30.01.2017.
 */

public class DisplaySpeechService extends Service{

    @Override
    public void onCreate() {
        super.onCreate();

        listen();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void listen(){



    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
