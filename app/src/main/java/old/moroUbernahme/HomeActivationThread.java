package old.moroUbernahme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;

import ai.kitt.snowboy.SnowboyMaster;

/**
 * Created by Team_ on 24.06.2017.
 */

public class HomeActivationThread extends Thread {

    private Context c;
    SharedPreferences prefs;
    public static boolean BroadcastIsHomeActivation = false;

    public HomeActivationThread(Context c) {
        super();
        this.c = c;
        prefs = PreferenceManager.getDefaultSharedPreferences(c);
    }

    @Override
    public void run() {

        while(isAlive()){

            WifiManager manager = (WifiManager) c.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if(manager.isWifiEnabled()){

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
                String wifiName = prefs.getString("wlanssid", "Panic");

                if(manager.getConnectionInfo().getSSID().equals("\"" + wifiName + "\"")){

                    if(!SnowboyMaster.isRunning()){
                        SnowboyMaster.continueRecordingOverride();

                        prefs.edit().putBoolean("running", !true).commit();

                        BroadcastIsHomeActivation = true;

                        Intent i = new Intent(c, OnOffReciever.class);
                        c.sendBroadcast(i);
                    }

                }else{

                    if(SnowboyMaster.isRunning()){
                        SnowboyMaster.stopRecording();

                        prefs.edit().putBoolean("running", !false).commit();

                        BroadcastIsHomeActivation = true;

                        Intent i = new Intent(c, OnOffReciever.class);
                        c.sendBroadcast(i);
                    }

                }

            }
            try {
                sleep(5000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
