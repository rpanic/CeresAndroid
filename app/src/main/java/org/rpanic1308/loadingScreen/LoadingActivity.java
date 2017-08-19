package org.rpanic1308.loadingScreen;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.SpotifyPlayer;

import org.rpanic1308.ceres.MainFeedActivity;
import org.rpanic1308.spotify.SpotifyIntents;

import java.util.ArrayList;

import rpanic1308.ceres.R;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

public class LoadingActivity extends AppCompatActivity {

    private String spotifyToken;

    private boolean spotifyReady;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        //Transition
        Slide slide = new Slide(Gravity.RIGHT);
        getWindow().setExitTransition(slide);

        //Layout
        ((TextView)findViewById(R.id.title)).setText("Ceres v" + MainFeedActivity.VERSION);

        init();
        startActivityIfFinished();
    }

    public void init(){

        boolean alreadyGranted = initPermissions();

        if(alreadyGranted){
            initWhenPermissionGranted();
        }

        authenticate();

    }

    public void startActivityIfFinished(){

        final LoadingActivity activity = this;

        if(spotifyReady) { //evtl noch ergänzen falls es noch weitere asynchrone Tätigkeiten gibt
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent t = new Intent(activity, MainFeedActivity.class);
                            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(activity).toBundle();

                            t.putExtra("spotifyToken", spotifyToken);

                            startActivity(t, bundle);
                        }
                    });

                }
            }).start();

        }
    }

    //----Logic - Methods----

    //Permissions

    public boolean initPermissions(){
        String[] arr = new String[]{
                Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_WIFI_STATE

        };

        return initPermission(arr);
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
            initWhenPermissionGranted();
        }
    }

    public void initWhenPermissionGranted(){
        authenticate();
    }

    //Spotify

    public void authenticate(){

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(SpotifyIntents.ClientID,
                AuthenticationResponse.Type.TOKEN,
                SpotifyIntents.REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

    }

    SpotifyPlayer mPlayer;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                spotifyToken = response.getAccessToken();
            }
            spotifyReady = true;
        }

        startActivityIfFinished();
    }
}
