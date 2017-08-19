package org.rpanic1308.spotify;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.SpotifyPlayer;

import org.rpanic1308.ceres.MainFeedActivity;
import org.rpanic1308.loadingScreen.LoadingActivity;
import org.rpanic1308.music.AudioInfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;

/**
 * Created by Team_ on 06.07.2017.
 */

public class SpotifyIntents{

    public final static String ClientID = "c60e48997a584a2f832b0c43de5bc05c";
    public final static String CLIENTSECRET = "25867c056912475dbb5ca82505c95ce9";
    public static final String REDIRECT_URI = "ceresspotify://callback";
    private static Player player;
    private static String token;

    public static String getTrackUri(AudioInfo info, String accessToken){

        SpotifyApi api = new SpotifyApi();

        // Most (but not all) of the Spotify Web API endpoints require authorisation.
        // If you know you'll only use the ones that don't require authorisation you can skip this step
        api.setAccessToken(accessToken);

        SpotifyService spotify = api.getService();

        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.LIMIT, 20);

        String query = (info.getTitle() == null && info.getSinger() == null) ? "" : (info.getTitle() == null ? info.getSinger() : (info.getSinger() == null ? info.getTitle() : info.getTitle() + " " + info.getSinger()));

        if(query.equals("")){
            return "";
        }

        TracksPager pager = spotify.searchTracks(query, options);

        return pager.tracks.items.get(0).uri;

    }

    public static void setSpotifyPlayer(Player p){
        player = p;
    }

    public static void setAccessToken(String token){
        SpotifyIntents.token = token;
    }

    public static Player getPlayer() {
        return player;
    }

    public static String getToken() {
        return token;
    }
}
