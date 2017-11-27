package com.ase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;

import org.pjsip.pjsua2.app.MainActivity;

import java.util.ArrayList;

/**
 * Created by Preethi on 8/1/2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    private static final String LOG_TAG = "CheckNetworkStatus";
    private NetworkChangeReceiver receiver;
    private boolean isConnected = false;
    //    private TextView networkStatus;
    private int val;

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.i("service123", "Receieved notification about network status NetworkChangeReceiver");
        isNetworkAvailable(context);
    }

    private void isNetworkAvailable(Context context) {

        Log.d("network", "NetworkChange Onreceived method");
        ConnectivityManager _connec = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo networkInfo = _connec.getActiveNetworkInfo();
        if (networkInfo != null) {
            Log.i("travelcheck1234", "networkInfo Not null-=====>");
            int type = networkInfo.getType();
            boolean connected = networkInfo.isConnected();
            Log.d("network", "network connected-->" + connected);
            if (connected) {
                Log.i("travelcheck1234", "networkInfo connected=====>");
                if (!Appreference.isAlreadyCalled) {
                    Appreference.isAlreadyCalled = true;
                    OfflineSendMessage offlineSendMessage = new OfflineSendMessage();
                    offlineSendMessage.sendOfflineMessages();
                    offlineSendMessage.sendOfflinecaption();
                }
            } else {
                Appreference.isAlreadyCalled = false;
                Log.i("travelcheck1234", "networkInfo disconnected=====>");

            }
        } else {
            Appreference.isAlreadyCalled = false;
            Log.i("travelcheck1234", "networkInfo disconnected123=====>");
        }
    }
}
