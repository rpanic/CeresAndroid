package org.rpanic1308.moro;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import org.rpanic1308.moroUbernahme.Methods;

/**
 * Created by morot on 20.06.2017.
 */

public class TurningOnOffService extends Service {

    Thread worker;
    Thread waiter;
    SharedPreferences prefs;
    final String wifiName = "\"moro39401\"";

    private Runnable mytask;

    boolean ifRecordingServiceIsRunning = false;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        final SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("running", false);
        editor.putBoolean("manually", false);
        editor.apply();

        mytask = new Runnable() {
            @Override
            public void run() {
                boolean boo = true;
                while (boo) {
                    if(ifRecordingServiceIsRunning)
                    {
                        if (!prefs.getBoolean("running", false)) //wenns nimma läuft
                        {
                            synchronized (worker) {
                                worker.notifyAll();
                                boo = false;

                            }
                        }
                    }
                    else
                    {
                        if (prefs.getBoolean("running", false)) //wenns dann läuft
                        {
                            synchronized (worker) {
                                worker.notifyAll();
                                boo = false;

                            }
                        }
                    }



                    synchronized (waiter) {
                        try {
                            waiter.wait(prefs.getInt("min", 1) * 60 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        };

        worker = new Thread(new Runnable() {
            @Override
            public void run() {

                while(true)
                {
                    WifiManager manager = (WifiManager) getBaseContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo info = null;
                    if(manager.isWifiEnabled())
                    {
                        info = manager.getConnectionInfo();
                    }

                    String s = "nop";
                    if(manager.isWifiEnabled() && info != null)
                    {
                        s = info.getSSID();
                    }

                    /**if(!prefs.getBoolean("running", false))
                    {
                        Methods.turnRecordingServiceOnOrOff(getBaseContext(), true);

                        synchronized (worker)
                        {
                            try {
                                worker.wait(30 * 60 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }**/


                    /**if(prefs.getBoolean("manually", false)) //wenn is manuell abgedreht habe --> warten
                    {
                        synchronized (worker)
                        {
                            try {
                                worker.wait(20 * 60 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    else
                     **/
                    if(s.equals(wifiName) && !prefs.getBoolean("running", false)) //zu meinem wlan verbunden --> einschalten
                    {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("isWasConnected", true);
                        editor.commit();

                        Log.d("TurningOnOffService", "wegen wifiName !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ");
                        Methods.turnRecordingServiceOnOrOff(getBaseContext(), true);

                        synchronized (worker)
                        {
                            try {
                                worker.wait(2*60*1000);//2 * 60 * 1000
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    else if(!(s.equals(wifiName)) && (!s.equals("nop")) && !s.equals("<unknown ssid>")) //zu anderem wlan verbunden --> warten
                    {
                        if(prefs.getBoolean("running", false))
                        {
                            Methods.turnRecordingServiceOnOrOff(getBaseContext(), false);
                        }

                        synchronized (worker)
                        {
                            try {
                                worker.wait(20 * 60 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    else if((s.equals("<unknown ssid>") && prefs.getBoolean("isWasConnected", false)) || (s.equals("nop") && prefs.getBoolean("running", false)))
                    //prüfen ob er nur wlan ausgeschaltet hat aber noch zuhause ist oder nicht --> eingeschaltet lassen oder ausschalten
                    //wlan deaktiviert und recording service aktiv = ich bin zu hause
                    {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("isWasConnected", false);
                        editor.commit();

                        Log.d("TurningOnOffService", "wegen Standort !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ");

                        //location ob innerhalb //service starten
                        ifRecordingServiceIsRunning = true;
                        if(s.equals("<unknown ssid>"))
                        {
                            startLocationListenerService(1);
                        }
                        else
                        {
                            startLocationListenerService(4);
                        }


                    }
                    else if(s.equals("<unknown ssid>") && !prefs.getBoolean("isWasConnected", false))//zu keinem wlan verbunden
                    {
                        synchronized (worker)
                        {
                            try {
                                worker.wait(5*60*1000);//5*60
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else if(s.equals("nop") && !prefs.getBoolean("running", false))//wlan deaktiviert und recording service ist deaktiviert --> schauen ob ich daheim bin
                    {
                        ifRecordingServiceIsRunning = false;
                        startLocationListenerService(20);
                    }

                }
            }
        });

        worker.start();
    }

    public void makeNewWaiterThread()
    {
        waiter = new Thread(mytask);
    }

    public void startLocationListenerService(int min)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("min", min);
        editor.apply();
        startService(new Intent(getBaseContext(), LocationService.class));

        makeNewWaiterThread();
        waiter.start();

        synchronized (worker) {
            try {
                worker.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        stopService(new Intent(getBaseContext(), LocationService.class));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
