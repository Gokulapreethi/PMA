package com.ase;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Preethi on 8/1/2017.
 */

public class offlineSendService extends Service {

    NetworkChangeReceiver myreciReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("service123", "Received notification about offlineSendActivity onBind");
        return null;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("service123", "Receieved notification about offlineSendActivity onCreate");

        try {
            myreciReceiver=new NetworkChangeReceiver();
            IntentFilter intentFilter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(myreciReceiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Log.i("service123", "Receieved notification about offlineSendActivity onStartCommand");
        myreciReceiver=new NetworkChangeReceiver();
        IntentFilter intentFilter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myreciReceiver, intentFilter);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("service123", "Receieved notification about offlineSendActivity onDestroy");
        unregisterReceiver(myreciReceiver);
    }
}
