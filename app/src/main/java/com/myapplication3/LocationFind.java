package com.myapplication3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

/**
 * Created by prasanth on 11/17/2016.
 */
public class LocationFind extends FragmentActivity {
    private GoogleMap mMap;
    LocationManager location_manager;
    Geocoder geocoder;
    List<Address> addresses;
    LOCTracker loc;
    double latitude;
    double longitude;
    ImageView send;
    TextView back;
    Handler handler;
    private boolean back_preesed = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locationfind);
        try {
            TextView back = (TextView) findViewById(R.id.back);
            ImageView send = (ImageView) findViewById(R.id.sentBtn);

            handler = new Handler();
//        setUpMapIfNeeded();

            try {
                MapsInitializer.initialize(getApplicationContext());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Appreference.printLog("LocationFind ","Exception MapsInitializer.initialize "+e.getMessage(),"WARN",null);
            }
            try {
                // Loading map
                setUpMapIfNeeded();
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("LocationFind ","Exception setUpMapIfNeeded "+e.getMessage(),"WARN",null);
            }
//        if (Appreference.isLocation) {

//        Log.i("Location", "after view map " + Appreference.isLocation);
            Bundle getBundle;
            getBundle = this.getIntent().getExtras();

            if (getBundle != null && getBundle.getString("viewmap").equalsIgnoreCase("location")) {

                String map = getBundle.getString("map");
                Log.d("Location", "latitude from sender " + map);
                String[] parts = map.split(",");
                if( (parts[0] != null && !parts[0].equalsIgnoreCase("")) && (parts[1] != null && !parts[1].equalsIgnoreCase(""))) {
                    Log.d("Location", "latitude from sender " + Double.parseDouble(parts[0]));
                    Log.d("Location", "latitude from sender " + Double.parseDouble(parts[1]));
                    latitude = Double.parseDouble(parts[0]);
                    longitude = Double.parseDouble(parts[1]);
                    Log.d("Location", "latitude from sender " + latitude);
                    Log.d("Location", "longitude from sender " + longitude);
                }
                send.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
    //            Appreference.isLocation = false;
                try {
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(
                            new LatLng(latitude, longitude)).zoom(12).build();

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    mMap.getUiSettings().setCompassEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("LocationFind ","Exception mMap else  "+e.getMessage(),"WARN",null);
                }

                showDetailedLocation();
            } else {
                loc = new LOCTracker(LocationFind.this);
                location_manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Toast.makeText(getApplicationContext(), "loading", Toast.LENGTH_SHORT).show();
                if (loc.canGetLocation()) {
                    latitude = loc.getLatitude();
                    longitude = loc.getLongitude();
                    Log.d("Location", "latitude " + latitude);
                    Log.d("Location", "longitude " + longitude);

                    CameraPosition cameraPosition = new CameraPosition.Builder().target(
                            new LatLng(latitude, longitude)).zoom(12).build();

                    try {
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        mMap.getUiSettings().setMyLocationButtonEnabled(true);
                        mMap.getUiSettings().setCompassEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("LocationFind ","Exception mMap else "+e.getMessage(),"WARN",null);
                    }

    //        mMap.setMyLocationEnabled(true);

                    showDetailedLocation();
    //                Toast.makeText(getApplicationContext(), "" + latitude + "\n" + longitude, Toast.LENGTH_LONG).show();
                } else {
    //                loc.showSettingsAlert();
                    try {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LocationFind.this);
                        alertDialog.setTitle("Gps setting");
                        alertDialog.setMessage("Unable to get current location. Change permissions");
                        alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent in = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(in, 101);
                            }
                        });
                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        alertDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("LocationFind ","Exception AlertDislog "+e.getMessage(),"WARN",null);
                    }
                }
            }
            // latitude and longitude
//        double latitude = 17.385044;
//        double longitude = 78.486671;

            // create marker

//        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("your Location");// adding marker
//        mMap.addMarker(marker);
//        CameraPosition cameraPosition = new CameraPosition.Builder().target(
//                new LatLng(latitude, longitude)).zoom(12).build();
//
//        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
////        mMap.setMyLocationEnabled(true);
//
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//
//        mMap.getUiSettings().setCompassEnabled(true);

//        mMap.getUiSettings().setZoomControlsEnabled(false);

//        mMap.getUiSettings().setZoomGesturesEnabled(false);

            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent();
                        i.putExtra("loc_latitude", String.valueOf(latitude + "," + longitude));
                        setResult(RESULT_OK, i);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("LocationFind ","Exception send.setonClickListener  "+e.getMessage(),"WARN",null);
                    }
                }
            });


            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    back_preesed = true;
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("LocationFind ","Exception Oncreate "+e.getMessage(),"WARN",null);
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            setUpMapIfNeeded();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("LocationFind ","Exception onResume "+e.getMessage(),"WARN",null);
        }
    }



    private void showDetailedLocation() {
        try {
            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
            if (addresses.isEmpty() && addresses != null && addresses.size() != 0) {
                Toast.makeText(getApplicationContext(), "Waiting for loacation", Toast.LENGTH_SHORT).show();
            } else {
                if (addresses.size() > 0) {
                    String con = addresses.get(0).getFeatureName() + "," + addresses.get(0).getAdminArea() + "," + addresses.get(0).getCountryCode() + "," + addresses.get(0).getCountryName() + "," + addresses.get(0).getLocality() + "," + addresses.get(0).getPostalCode() /*+ "," + addresses.get(0).getSubAdminArea()*/;
                    MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(con);// adding marker
                    mMap.addMarker(marker);

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(latitude, longitude), 16));
                    Log.i("Location", "latitude pin " + latitude);
                    Log.i("Location", "longitude pin " + longitude);

                    Log.i("Location", "red pin " + con);
                    con = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("LocationFind ","Exception showDetailedLocation "+e.getMessage(),"WARN",null);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Log.i("LocationFind", "requestCode : " + requestCode + " resultCode : " + resultCode);
            if (requestCode == 101) {
                if (location_manager == null) {
                    location_manager = (LocationManager) getSystemService(LOCATION_SERVICE);
                }
                if (!location_manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.i("LocationFind", "if requestCode : " + requestCode + " resultCode : " + resultCode);
                } else {
                    Log.i("LocationFind", "else requestCode : " + requestCode + " resultCode : " + resultCode);
                    loc = new LOCTracker(LocationFind.this);
                    location_manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (loc.canGetLocation()) {
                                while (true) {
                                    loc.getLocation();
                                    latitude = loc.getLatitude();
                                    longitude = loc.getLongitude();
                                    if (latitude == 0.0 && latitude == 0.0 && !back_preesed) {

                                    } else {
                                        break;
                                    }
                                }
                                Log.d("Location", "latitude " + latitude);
                                Log.d("Location", "longitude " + longitude);

                                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                                        new LatLng(latitude, longitude)).zoom(12).build();
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                                mMap.getUiSettings().setCompassEnabled(true);
                                showDetailedLocation();
                            }
                        }
                    }, 2000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("LocationFind onActivityResult ","Exception "+e.getMessage(),"WARN",null);
        }
    }

    private void setUpMapIfNeeded() {
        if (mMap != null) {
            return;
        }
//        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        try {
            mMap = ((SupportMapFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("LocationFind setUpMapIfNeeded ","Exception  1 "+e.getMessage(),"WARN",null);
        }

        /*
        (new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new
                        LatLng(latitude, longitude), 15));

                mMap = googleMap;
                // Rest of the stuff you need to do with the map
            }
        });

         */
        if (mMap == null) {
            return;
        }
        // Initialize map options. For example:
        // mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }
}