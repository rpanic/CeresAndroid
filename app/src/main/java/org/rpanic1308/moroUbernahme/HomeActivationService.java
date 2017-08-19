package org.rpanic1308.moroUbernahme;

import android.app.Service;
import android.content.Intent;
import android.location.LocationProvider;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

/**
 * Created by Team_ on 24.06.2017.
 */

public class HomeActivationService extends Service{

    HomeActivationThread thread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int ret = super.onStartCommand(intent, flags, startId);

        thread = new HomeActivationThread(getBaseContext());
        thread.start();

        return ret;
    }
}
