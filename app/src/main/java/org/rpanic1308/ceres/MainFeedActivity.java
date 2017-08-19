package org.rpanic1308.ceres;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rpanic1308.moroUbernahme.HomeActivationService;
import org.rpanic1308.moroUbernahme.Methods;
import org.rpanic1308.moroUbernahme.OnOffReciever;
import org.rpanic1308.notifications.NotificationService;
import org.rpanic1308.settings.SettingsActivity;
import org.rpanic1308.feed.FeedAdapter;
import org.rpanic1308.feed.FeedItem;
import org.rpanic1308.feed.FeedSaver;
import org.rpanic1308.main.CeresController;
import org.rpanic1308.snowboyNotification.SnowboyNotificationHandler;
import org.rpanic1308.spotify.SpotifyIntents;
import org.rpanic1308.transmission.TransmissionHelper;

import javax.net.ssl.HttpsURLConnection;

import ai.kitt.snowboy.Constants;
import ai.kitt.snowboy.SnowboyListener;
import ai.kitt.snowboy.SnowboyMaster;
import rpanic1308.ceres.R;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class MainFeedActivity extends AppCompatActivity implements SpotifyPlayer.NotificationCallback, ConnectionStateCallback {

    ImageButton speakButton;
    public static MainFeedActivity mainActivity;
    private List<FeedItem> feedItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_main);
        mainActivity = this;

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(getDrawable(android.R.drawable.ic_menu_manage));
        fab.setBackgroundColor(ContextCompat.getColor(mainActivity, R.color.colorFAB));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onManuellPressed();
            }
        });*/

//        Button b = (Button) findViewById(R.id.fab);
//        b.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               onManuellPressed();
//           }
//       });

        speakButton = (ImageButton)findViewById(R.id.imageButton);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        // finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorFABDark));
        //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        //String[] arr = new String[]{"asdasd", "adsgfsadg","asdasd", "adsgfsadg","asdasd", "adsgfsadg","asdasd", "adsgfsadg","asdasd", "adsgfsadg"};
        //((ListView)findViewById(R.id.list)).setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arr));
        addDefaultListContent();

        //new SpeechTest().s(this);

        boolean alreadyGranted = initPermissions();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
       // SharedPreferences.Editor editor = prefs.edit();
        //editor.putBoolean("running", true);
        //editor.apply();
        //Intent i = new Intent(this, OnOffReciever.class);
        //Methods.displayNotification(this, PendingIntent.getBroadcast(this, 1111, i, 0));


        Constants.sensitivity = prefs.getString("snowboySensitivity", Constants.sensitivity);

        if(alreadyGranted){
            CeresController.init();
        }

        //onManuellPressed(null);

        SnowboyMaster.startSnowBoy(new SnowboyListener() {
            @Override
            public void callback(Object o) {

                SnowboyMaster.stopRecording(new SnowboyListener(){
                    @Override
                    public void callback(Object o) {
                        onSpeakClick(null);
                    }
                });

            }
        }, this);

        SnowboyNotificationHandler.getInstance(this).initNotification();

        prefs.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                onPreferenceChanged(sharedPreferences, key);
            }
        });

        SpotifyIntents.authenticate(this);

        //TODO Notification für abschalten von Snowboy  - vllt im SnowboyMaster   X

        //Intent service = new Intent(this, HomeActivationService.class);
        //startService(service);
        //TODO Service wieder aktivieren aber vllt noch verbessern

    }

    public void onPreferenceChanged(SharedPreferences prefs, String key){

        if(key.equals("pref_server")){
            CeresController.SERVER = prefs.getString(key, CeresController.SERVER);
        }

    }

    public void onSpeakClick(final View v) {

        if(SnowboyMaster.isRunning()){
            SnowboyMaster.stopRecording(new SnowboyListener(){
                @Override
                public void callback(Object o) {
                    onInput();
                }
            });
        }else{
            onInput();
        }

    }

     public void initSpeekRecognizer(){
        final SpeechRecognizer sr = SpeechRecognizer.createSpeechRecognizer(this, ComponentName.unflattenFromString("com.google.android.googlequicksearchbox/com.google.android.voicesearch.serviceapi.GoogleRecognitionService"));

        final Intent in = new Intent();
        in.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "de-DE");
        in.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        in.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 30 * 1000);

        sr.setRecognitionListener(new RecognitionListener() {
            public void onReadyForSpeech(Bundle params)
            {
                System.out.println("onReadyForSpeech");
            }
            public void onBeginningOfSpeech()
            {
                System.out.println("onBeginningOfSpeech");
            }
            public void onRmsChanged(float rmsdB)
            {
                System.out.println("onRmsChanged");
            }
            public void onBufferReceived(byte[] buffer)
            {
                System.out.println("onBufferReceived");
            }
            public void onEndOfSpeech()
            {
                System.out.println("onEndofSpeech");
                SnowboyMaster.continueRecording(MainFeedActivity.this);
            }
            public void onError(int error)
            {
                System.out.println("error " +  error);

                if(error == SpeechRecognizer.ERROR_NO_MATCH || error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT){
                    sr.stopListening();
                }
                sr.destroy();
                SnowboyMaster.continueRecording(MainFeedActivity.this);
                SnowboyMaster.increaseVolume(MainFeedActivity.this);

                speakButton.getBackground().setColorFilter(ContextCompat.getColor(MainFeedActivity.this, R.color.colorAccent), PorterDuff.Mode.SRC);
            }
            public void onResults(Bundle results)
            {
                String str = "";
                System.out.println("onResults " + results);
                final ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                for (int i = 0; i < data.size(); i++)
                {
                    System.out.println("result " + data.get(i));
                    str += data.get(i); //Rendundant
                }
                str = (String)data.get(0);
                System.out.println("STR: " +str);

                final String str2 = str;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(str2.equals("")){
                            System.out.println("INITSPeechRecognizer | Result string is null");
                            return;
                        }
                        speechResult(str2);
                    }
                }).start();

                sr.stopListening();
                speakButton.getBackground().setColorFilter(ContextCompat.getColor(MainFeedActivity.this, R.color.colorAccent), PorterDuff.Mode.SRC);
                sr.destroy();
                SnowboyMaster.continueRecording(MainFeedActivity.this);
                SnowboyMaster.increaseVolume(MainFeedActivity.this);

            }
            public void onPartialResults(Bundle partialResults)
            {
                System.out.println("onPartialResults");
                String str = "";
                ArrayList data = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                for (int i = 0; i < data.size(); i++)
                {
                    System.out.println("Partial result " + data.get(i));
                    str += data.get(i);
                }
                TextView tv = (TextView)mainActivity.findViewById(R.id.textView);
                tv.setText(str);
            }
            public void onEvent(int eventType, Bundle params)
            {
                System.out.println("onEvent " + eventType);
            }
        });


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //speakButton.setBackgroundResource(R.color.colorSpeechActive/*ContextCompat.getColor(mainActivity, R.color.colorSpeechActive)*/);
                speakButton.getBackground().setColorFilter(0x993F51B5, PorterDuff.Mode.MULTIPLY);
                sr.startListening(in);
            }
        });


    }

    public void onInput(){

        final EditText v = (EditText)findViewById(R.id.editTextInput);
        System.out.println("onInput EditText:" + v.getText());
        if(v.getText().toString().equals("")){

            SnowboyMaster.reduceVolume(this);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initSpeekRecognizer();
                }
            });
            if(!Looper.getMainLooper().equals(Looper.myLooper())){
                try{
                    Thread.sleep(5000L);
                }catch(InterruptedException e){e.printStackTrace();}
            }

        }else {
            String s = v.getText().toString();
            CeresController.inputMessage(s);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    v.setText("");
                }
            });
        }
        TransmissionHelper.checkForUpdates();

    }

    public void addFeedItem(FeedItem item){

        if(item.getType().equals("remove")){

            for(int i = 0 ; i < feedItems.size() ; i++){

                if(feedItems.get(i).getId().equals(item.getContent())){
                    feedItems.remove(i);
                    return;
                }
            }
        }

        if(feedItems.contains(item)){
            return;
        }

        feedItems.add(0, item);
        Collections.sort(feedItems, new FeedItem.FeedComparator());
        final ListView lv = (ListView) findViewById(R.id.list);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lv.setAdapter(new FeedAdapter(mainActivity, android.R.layout.simple_list_item_1, feedItems));
            }
        });


    }

    public FeedItem getLastFeedItem() {
        if(feedItems.size() < 1){
            return null;
        }

        FeedItem ret = feedItems.get(0);
        for(FeedItem item : feedItems){
            if(item.getTimeStamp() > ret.getTimeStamp()){
                ret = item;
            }
        }
        return ret;
    }

    public void addDefaultListContent(){

        List<FeedItem> list = FeedSaver.loadList();

        ListView lv = (ListView) findViewById(R.id.list);
        /*List<FeedItem> list = new ArrayList();

            FeedItem item = new FeedItem();
            item.setContent("Hallo;Hallo mein Name ist Raphael, Das ist ein Text\nNeue Zeile xDxDxD\nHallo mein Name ist Raphael, Das ist ein Text\n" +
                    "Neue Zeile xDxDxD\nHallo mein Name ist Raphael, Das ist ein Text\n" +
                    "Neue Zeile xDxDxD");
            item.setType("text");
            list.add(item);

            item = new FeedItem();
            item.setContent("Hallo;Das ist der zweite text\nNeue Zeile xDxDxD");
            item.setType("text");
            list.add(item);*/

       /*FeedItem item = new FeedItem();
        item.setContent("Image;Image2");
        item.setType("img");
        list.add(item);*/

        feedItems.addAll(list);
        Collections.sort(feedItems, new FeedItem.FeedComparator());

        lv.setAdapter(new FeedAdapter(this, android.R.layout.simple_list_item_1, list));
    }

    public void addTextView(){

        ListView lv = (ListView) findViewById(R.id.list);
        List list = new ArrayList();

        list.add(new Object());
        list.add(new Object());

        lv.setAdapter(new FeedListAdapter(this, android.R.layout.expandable_list_content, list));

    }

    public void speechResult(final String s){

        System.out.println("SpeechResult");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((TextView)findViewById(R.id.textView)).setText(s);
            }
        });
        //Transmit
        CeresController.inputMessage(s);

    }

    public void onManuellPressed(final View view){
        //Intent i = new Intent(this, ManuellActivity.class);
       /* new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("onManuellPressed");
                new Recorder().startRecording();
            }
        }).start();*/


        //TODO Debug entfernt
        Intent i = new Intent(MainFeedActivity.this, SettingsActivity.class);
        startActivity(i);
    }

    public boolean initPermissions(){
        String[] arr = new String[]{
            Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE

        };

        return initPermission(arr);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FeedSaver.saveList(feedItems);
        //MusicNotification.cancel(this);
        stopService(new Intent(this, NotificationService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Methods.destroyNotification(this);
        SnowboyNotificationHandler.getInstance(this).destroyNotification();
        SnowboyMaster.stopRecording();
    }

    private boolean initPermission(String[] arr){

        ArrayList<String> requests = new ArrayList<>();
        for(String s : arr){
            if (ContextCompat.checkSelfPermission(this,
                    s)
                    != PackageManager.PERMISSION_GRANTED) {
                requests.add(s);
            }
        }

        if(!requests.isEmpty()){
            ActivityCompat.requestPermissions(this,
                    requests.toArray(new String[requests.size()]),
                    1337);
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1337){
            System.out.println("Permissions granted");
            CeresController.init();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.menu_settings){
                    Intent t = new Intent(MainFeedActivity.this, SettingsActivity.class);
                    startActivity(t);
                    return true;
                }
                return false;
            }
        });
        return true;
    }

    //**************************
    //        Spotify
    //**************************


    private Player mPlayer;
    private String token;

//AQDb2Bw6X8ft1KHdaCLkw8BdjbtnD0ZyT4avyWI9h3xKeWx7MC4II03Qi-OFbgAFpRKszz0y9-vqTjb3eRDipIQF8NZum0Mq3uMFy4oCOVjYBVa5O9Co5RKA-qyT5hqLXVOPUtaXYyiz4aFYEZMiIjEeskbW6yms_eRTzhvERV8Om9nk4lp4oKxhXMI

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), SpotifyIntents.ClientID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(MainFeedActivity.this);
                        mPlayer.addNotificationCallback(MainFeedActivity.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });
                token = playerConfig.oauthToken;
                SpotifyIntents.setAccessToken(token);

            }
        }
    }

    public void authenticate(){

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(SpotifyIntents.ClientID,
                AuthenticationResponse.Type.TOKEN,
                SpotifyIntents.REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);


    }



    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d("MainActivity", "Playback event received: " + playerEvent.name());
        switch (playerEvent) {
            // Handle event type as necessary
            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d("MainActivity", "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary
            default:
                break;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
        SpotifyIntents.setSpotifyPlayer(mPlayer);
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

}
