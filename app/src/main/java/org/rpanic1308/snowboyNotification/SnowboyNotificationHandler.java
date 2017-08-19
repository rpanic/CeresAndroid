package org.rpanic1308.snowboyNotification;

import android.app.Notification;
import android.content.Context;

import ai.kitt.snowboy.SnowboyMaster;

/**
 * Created by Team_ on 18.08.2017.
 */

public class SnowboyNotificationHandler {

    private final Context context;
    SnowboyNotification notification;
    private static SnowboyNotificationHandler instance;

    public static SnowboyNotificationHandler getInstance(Context c){
        if(instance == null){
            instance = new SnowboyNotificationHandler(c);
        }
        return instance;
    }

    public SnowboyNotificationHandler(Context c){

        this.notification = new SnowboyNotification();
        this.context = c;
        
    }

    public void initNotification(){

        notification.setNotificationRunningStatus(SnowboyMaster.isRunning(), context);

    }

    public void updateNotification(){

        notification.setNotificationRunningStatus(SnowboyMaster.isRunning(), context);

    }

    public void switchSnowboyRunning(){

        if(SnowboyMaster.isRunning()){
            SnowboyMaster.stopRecording();
        }else{
            SnowboyMaster.continueRecording(context);
        }

    }

    public void destroyNotification(){
        notification.destroyNotification(context);
    }

}
