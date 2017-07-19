package org.pjsip.pjsua2.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ase.Appreference;

public class SipRegisterBroadastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // TODO Auto-generated method stub
//        Toast.makeText(MainActivity.mainContext,"ReRegister Alarm",Toast.LENGTH_SHORT).show();
        Log.i("SipVideo", "Sip ReRegister BroadcastReceiver for every 900 sec");
        if(MainActivity.app != null && MainActivity.account != null && MainActivity.accCfg != null) {
            Appreference.printLog("SipRegister", "Sip Re-Register for every 900 sec", "DEBUG", null);
            if(MainActivity.isPresenceReregister) {
                MainActivity.reRegister();
            }
        }
 

    }
}
