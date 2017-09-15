package old.moro;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import old.moroUbernahme.Methods;

public class LocationService extends Service {

    private LocationManager locationManager;

    private SharedPreferences prefs;
    int min = 0;


    private final double lat = 48.218746;
    private final double lon = 13.483486;
    private final double radius = 35;

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            checkNewLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    public void checkNewLocation(Location location1) {
        //Log.d("LocationService", "checkNewLocation()");


        float[] distance = new float[3];
        float[] distance1 = new float[3];

        Location.distanceBetween(lat, lon, location1.getLatitude(), location1.getLongitude(), distance);
        Location.distanceBetween(lat, lon, location1.getLongitude(), location1.getLatitude(), distance1);
        //Location.distanceBetween(lat, lon, 48.213938, 13.483060, distance); //#crazy

        float d;

        if (distance[0] > distance1[0]) {
            d = distance1[0];
        } else {
            d = distance[0];
        }

        if (d > radius && prefs.getBoolean("running", false)) {
            //outside
            Methods.turnRecordingServiceOnOrOff(getBaseContext(), false);
        } else if (d < radius && !prefs.getBoolean("running", false)) {
            //inside
            Methods.turnRecordingServiceOnOrOff(getBaseContext(), true);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();


        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());


        Log.d("LocationService", "onCreate");

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long) (1000 * 60 * prefs.getInt("min", 1)), (float) (radius * 0.9), locationListener);


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
