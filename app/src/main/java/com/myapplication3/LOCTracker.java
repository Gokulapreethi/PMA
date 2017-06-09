package com.myapplication3;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by prasanth on 11/18/2016.
 */
public class LOCTracker extends Service implements LocationListener {
    private final Context mContext;
    boolean IsNetworkEnabled = false;
    boolean isGPSEnabled=false;
    boolean canGetLocation = false;
    double latitude;
    double longitude;
    double lat1;
    double lng1;
    Location location;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 10;
    protected LocationManager locationManager;

    public LOCTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            IsNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!isGPSEnabled && !IsNetworkEnabled ) {

            } else {
                this.canGetLocation = true;
                if (IsNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if(locationManager!=null){
                        location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        Log.i("Location","location : "+location);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
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
        if (locationManager != null) {
            locationManager.removeUpdates(LOCTracker.this);

        }
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();

        }
        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    @Override
    public void onLocationChanged(Location location) {
        /*latitude = getLatitude();
        longitude = getLongitude();
        lat1 = Appreference.latitude;
        lng1 = Appreference.longitude;
        Double dist = getDistance(latitude, longitude, lat1, lng1);
        Log.d("Location", "distance " + dist);
        if (dist > 3) {
            Log.d("Location", "distance after getting current location " + dist);
        }*/
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

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}