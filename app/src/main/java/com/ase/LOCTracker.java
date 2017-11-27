package com.ase;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by prasanth on 11/18/2016.
 */
public class LOCTracker extends Service implements LocationListener {
    private final Context mContext;
    boolean IsNetworkEnabled = false;
    boolean isGPSEnabled=false;
    boolean canGetLocation = false;
    double track_latitude=0.0;
    double track_longitude=0.0;
    private String provider;
    Location location;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000;
//    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    private static final long MIN_TIME_BW_UPDATES = 3000;
    protected LocationManager mLocationManager;

    public LOCTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            mLocationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            IsNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            // getting GPS status
            isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!isGPSEnabled && !IsNetworkEnabled ) {
                /*GPS or network not enabled*/
            } else {
                this.canGetLocation = true;

                // if GPS Enabled get lat/long using GPS Services

                if (isGPSEnabled ) {
                    if (location == null) {
                        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("FireGps", "GPS Enabled ");
                        Log.d("FireGps", "GPS  mLocationManager===> "+mLocationManager);

                        if (mLocationManager != null) {
                            location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                            location=getLastKnownLocation();
                            Log.d("FireGps", "GPS Enabled location===> "+location);

                            if (location != null) {
                                track_latitude = location.getLatitude();
                                track_longitude = location.getLongitude();
                                Log.d("FireGps", "GPS Enabled track_latitude===> "+track_latitude);
                                Log.d("FireGps", "GPS Enabled track_longitude===> "+track_longitude);

                            }
                        }
                    }
                }
                Log.d("FireGps", "IsNetworkEnabled  ===> "+IsNetworkEnabled);

                if (IsNetworkEnabled && track_latitude == 0.0) {
                    mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("FireGps", "IsNetworkEnabled ");
                    if(mLocationManager!=null){
                        location=mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                        location=getLastKnownLocation();
                        Log.i("FireGps","IsNetworkEnabled location : "+location);
                        if (location != null) {
                            track_latitude = location.getLatitude();
                            track_longitude = location.getLongitude();
                            Log.d("FireGps", "IsNetworkEnabled track_latitude===> "+track_latitude);
                            Log.d("FireGps", "IsNetworkEnabled track_longitude===> "+track_longitude);

                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public void stopUsingGPS() {
        if (mLocationManager != null) {
            mLocationManager.removeUpdates(LOCTracker.this);

        }
    }
    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager)mContext.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l_accuracy = mLocationManager.getLastKnownLocation(provider);
            if (l_accuracy == null) {
                continue;
            }
            if (bestLocation == null || l_accuracy.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l_accuracy;
            }
        }
        return bestLocation;
    }
    public double getLatitude() {
        if (location != null) {
            track_latitude = location.getLatitude();

        }
        return track_latitude;
    }

    public double getLongitude() {
        if (location != null) {
            track_longitude = location.getLongitude();
        }
        return track_longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("FireGps","onLocationChanged location : "+location);

        if (location != null) {
            track_latitude = location.getLatitude();
            track_longitude = location.getLongitude();
        }
        Log.d("FireGps","onLocationChanged latitude==> "+track_latitude +" longitude==> "+track_longitude);
    }

    public double getDistance(double lat1, double lon1, double lat2, double lon2) {
        double latA = Math.toRadians(lat1);
        double lonA = Math.toRadians(lon1);
        double latB = Math.toRadians(lat2);
        double lonB = Math.toRadians(lon2);
        double cosAng = (Math.cos(latA) * Math.cos(latB) * Math.cos(lonB - lonA)) +
                (Math.sin(latA) * Math.sin(latB));
        double ang = Math.acos(cosAng);
        double dist = ang * 6371;
        return dist;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(mContext, "Gps turned off ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(mContext, "Gps turned on ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}
