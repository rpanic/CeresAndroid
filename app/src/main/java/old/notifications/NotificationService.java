package old.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import org.rpanic1308.Constants;

import rpanic1308.ceres.R;

/**
 * Created by morot on 08.06.2017.
 */

public class NotificationService extends Service {

    public final int id = 99;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Notification
        RemoteViews contentView = new RemoteViews(getBaseContext().getPackageName(), R.layout.notification_layout);


        Intent volume = new Intent(this, NotificationReceiver.class);
        volume.putExtra(Constants.onOrOff, "off");

        PendingIntent pVolume = PendingIntent.getBroadcast(this, 1, volume, 0);

        Notification notification = new NotificationCompat.Builder(this)
                //.setContentTitle("Hotword Detection")
                .setSmallIcon(android.R.color.transparent)
                .setCustomContentView(contentView)
                .setContentIntent(pVolume)
                .setPriority(Notification.PRIORITY_MAX)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setOngoing(true)
                .build();

        this.startForeground(id, notification);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(Constants.notificationId, notification);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(Constants.notificationId);
    }
}
