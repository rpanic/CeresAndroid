package org.rpanic1308.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import org.rpanic1308.ceres.MainFeedActivity;
import org.rpanic1308.transmission.ServerToAppInfo.TransType;
import org.rpanic1308.transmission.TransmissionHelper;
import org.rpanic1308.transmission.listeners.FeedResponseListener;
import org.rpanic1308.transmission.listeners.MusicListener;
import org.rpanic1308.transmission.listeners.RequestInfoListener;
import org.rpanic1308.transmission.listeners.TextListener;

public class CeresController {

    public static int SERVER_PORT = 1337;
    public static String baseDirectory = getDirectory();
    public static final String CLIENTID = getMacAddress();
    public static String SERVER = getServerAddress();

    /** Is being called when a new input message is spoken or typed and the message is forwarded to the server*/
    public static void inputMessage(String s){
        TransmissionHelper.sendSentenceToServer(s);
    }

    public static void init(){

        if(SERVER == null){
            Config.write("server_address", "10.0.0.23");
            SERVER = getServerAddress();
        }
        if(baseDirectory == null){
            baseDirectory = getDirectory();
        }

        TransmissionHelper.runCheckingLoop();

        TransmissionHelper.registerListener(TransType.AusgabeText, new TextListener());
        TransmissionHelper.registerListener(TransType.AudioInfo, new MusicListener());
        TransmissionHelper.registerListener(TransType.Request, new RequestInfoListener());
        TransmissionHelper.registerListener(TransType.FeedItems, new FeedResponseListener());
    }

    public static String getMacAddress(){

        if(Config.getString("mac_address") != null){
            return Config.getString("mac_address");
        }else{
            Config.write("mac_address", "13371337");
            return Config.getString("mac_address");
        }

        //Wegen nicht funktionieren der Mac-Addresse manuelles Eintragen erforderlich

        /*InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        }

        byte[] mac = null;
        try {
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            mac = network.getHardwareAddress();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        String macString = "";
        for(byte b : mac){
            macString += String.format("%02X", b);
        }

        Config.write("mac_address", macString);

        return macString;*/
    }

    public static String getServerAddress() {
        //return "10.0.0.49";
        //return "rpanic.ddns.net";
        //return Config.getString("server_address");

        //TODO WEGEN Debuging weg
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MainFeedActivity.mainActivity);
        return pref.getString("pref_server", Config.getString("server_address"));
    }

    private static String getDirectory(){

        if(Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
            String s = Environment.getExternalStorageDirectory() + "/Download/music/";
            System.out.println("getDirectory() " + s);
            return s;
        }else{
            String s = MainFeedActivity.mainActivity.getDir("music", MainFeedActivity.MODE_PRIVATE).getAbsolutePath();
            System.out.println("getDirectory() " + s);
            return s;
        }

    }

}
