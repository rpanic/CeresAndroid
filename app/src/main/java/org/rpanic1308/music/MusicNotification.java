package org.rpanic1308.music;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import rpanic1308.ceres.R;

/**
 * Created by Team_ on 30.03.2017.
 */

public class MusicNotification extends Notification{

    private Context ctx;
    private NotificationManager mNotificationManager;

    @SuppressLint("NewApi")
    public MusicNotification(Context ctx){
        super();
        this.ctx=ctx;
        String ns = Context.NOTIFICATION_SERVICE;
        mNotificationManager = (NotificationManager) ctx.getSystemService(ns);
        CharSequence tickerText = "Shortcuts";
        long when = System.currentTimeMillis();
        Notification.Builder builder = new Notification.Builder(ctx);
        @SuppressWarnings("deprecation")
        Notification notification=builder.getNotification();
        notification.when=when;
        notification.tickerText=tickerText;
        notification.icon = android.R.drawable.ic_lock_idle_alarm;

        RemoteViews contentView = new RemoteViews(ctx.getPackageName(), R.layout.notification_music);

        //set the button listeners
        setListeners(contentView);

        notification.contentView = contentView;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        CharSequence contentTitle = "From Shortcuts";
        mNotificationManager.notify(548853, notification);
    }

    public void setListeners(RemoteViews view){
        //radio listener
        Intent radio=new Intent(ctx,MusicNotificationHelper.class);
        radio.putExtra("DO", "radio");
        PendingIntent pRadio = PendingIntent.getActivity(ctx, 0, radio, 0);
        view.setOnClickPendingIntent(R.id.radio, pRadio);

        /*//volume listener
        Intent volume=new Intent(ctx, MusicNotificationHelper.class);
        volume.putExtra("DO", "volume");
        PendingIntent pVolume = PendingIntent.getActivity(ctx, 1, volume, 0);
        view.setOnClickPendingIntent(R.id.volume, pVolume);

        //reboot listener
        Intent reboot=new Intent(ctx, MusicNotificationHelper.class);
        reboot.putExtra("DO", "reboot");
        PendingIntent pReboot = PendingIntent.getActivity(ctx, 5, reboot, 0);
        view.setOnClickPendingIntent(R.id.reboot, pReboot);*/





        //top listener
        /*Intent top=new Intent(ctx, MusicNotificationHelper.class);
        top.putExtra("DO", "top");
        PendingIntent pTop = PendingIntent.getActivity(ctx, 3, top, 0);
        view.setOnClickPendingIntent(R.id.top, pTop);

        //app listener
        Intent app=new Intent(ctx, MusicNotificationHelper.class);
        app.putExtra("DO", "app");
        PendingIntent pApp = PendingIntent.getActivity(ctx, 4, app, 0);
        view.setOnClickPendingIntent(R.id.btn1, pApp);*/
    }

}
