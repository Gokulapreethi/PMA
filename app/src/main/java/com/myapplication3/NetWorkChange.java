package com.myapplication3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.pjsip.pjsua2.app.MainActivity;

/**
 * Created by dinesh on 8/17/2016.
 */
public class NetWorkChange extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("network", "NetworkChange Onreceived method");
        ConnectivityManager _connec = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo networkInfo = _connec.getActiveNetworkInfo();
        if(networkInfo!=null) {
            Log.d("network", "NetworkChange Onreceived method networkInfo!=null");
            int type = networkInfo.getType();
            boolean connected = networkInfo.isConnected();
            Log.d("network", "network connected-->"+connected);
            if (connected) {
                 if(Appreference.context_table!=null && Appreference.context_table.containsKey("mainactivity")){
                    MainActivity mainActivity=(MainActivity)Appreference.context_table.get("mainactivity");
                     mainActivity.NetworkState(true);

                 }
            }else{
                if(Appreference.context_table!=null && Appreference.context_table.containsKey("mainactivity")){
                    MainActivity mainActivity=(MainActivity)Appreference.context_table.get("mainactivity");
                    mainActivity.NetworkState(false);

                }
            }
        }else{
            Log.d("network", "NetworkChange Onreceived method networkInfo=null");
            if(Appreference.context_table!=null && Appreference.context_table.containsKey("mainactivity")){
                MainActivity mainActivity=(MainActivity)Appreference.context_table.get("mainactivity");
                mainActivity.NetworkState(false);

            }
        }
    }
}
