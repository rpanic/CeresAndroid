package old.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import org.rpanic1308.Constants;

import rpanic1308.ceres.R;

/**
 * Created by morot on 08.06.2017.
 */

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String s = intent.getStringExtra(Constants.onOrOff);

        RemoteViews contentView = null;

        Intent volume = new Intent(context, NotiReceiver.class);

        if(s.equals("off"))
        {
            contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
            contentView.setImageViewResource(R.id.imageViewSwitch, R.drawable.switchon);
            volume.putExtra(Constants.onOrOff, "on");
        }
        else
        {
            contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
            contentView.setImageViewResource(R.id.imageViewSwitch, R.drawable.switchoff);
            volume.putExtra(Constants.onOrOff, "off");
        }

        PendingIntent pVolume = PendingIntent.getBroadcast(context, 1, volume, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context)
                //.setContentTitle("Hotword Detection")
                .setSmallIcon(android.R.color.transparent)
                .setCustomContentView(contentView)
                .setContentIntent(pVolume)
                .setPriority(Notification.PRIORITY_MAX)
                //.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setOngoing(true)
                .build();

        //NotificationManager
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(Constants.notificationId, notification);

        Log.d("NotificationReceiver", "after setBackgroundResource");
    }

}
