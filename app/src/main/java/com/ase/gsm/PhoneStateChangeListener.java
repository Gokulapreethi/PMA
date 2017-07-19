package com.ase.gsm;

/**
 * Created by Amuthan on 22/12/2016.
 */

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneStateChangeListener extends PhoneStateListener {

    String LOG_TAG = "GSM";
    public static boolean wasRinging;

    PhoneStateChangeNofication phoneStateNotify = null;


    public void SetListener(PhoneStateChangeNofication listener) {
        this.phoneStateNotify = listener;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        // TODO Auto-generated method stub
//		super.onCallStateChanged(state, incomingNumber);
        if (phoneStateNotify != null)
            phoneStateNotify.PhoneStateChangeDelegate(state);
        else
            Log.i(LOG_TAG, "Listener ---> " + phoneStateNotify);
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                Log.i(LOG_TAG, "RINGING");

                wasRinging = true;
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                Log.i(LOG_TAG, "OFFHOOK");

                if (!wasRinging) {
                    // Start your new activity
                } else {
                    // Cancel your old activity
                }

                // this should be the last piece of code before the break
                wasRinging = true;
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                Log.i(LOG_TAG, "IDLE");
                // this should be the last piece of code before the break
                wasRinging = true;
                break;
        }
    }

}
