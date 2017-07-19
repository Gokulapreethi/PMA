package com.ase.gcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.ase.AppSharedpreferences;
import com.ase.Appreference;

import org.json.JSONException;
import org.json.JSONObject;

import json.EnumJsonWebservicename;

/**
 * Created by saravanakumar on 7/8/2016.
 */
public class GCMTokenRefreshListenerService extends FirebaseInstanceIdService {


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is also called
     * when the InstanceID token is initially generated, so this is where
     * you retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("gcmMessage", "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
          sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project

        Appreference.fcmTokenId = token;
        Appreference.isFCMRegister = true;


        AppSharedpreferences.getInstance(getApplicationContext()).saveString("fcmTokenId",token);

        Log.d("fcm","tokenId is   = " + token);
        Log.d("fcm","tokenId is Appreference.fcmTokenId  = " + Appreference.fcmTokenId);
        {
            if (Appreference.isFCMRegister && AppSharedpreferences.getInstance(this).getBoolean("login")) {
                try {

                    JSONObject jsonObject = new JSONObject();


                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("id", String.valueOf(Appreference.loginuserdetails.getId()));


                    jsonObject.put("user", jsonObject1);
                    jsonObject.put("imeiNo", Appreference.getIMEINumber(getApplicationContext()));
                    jsonObject.put("tokenId", token);
                    jsonObject.put("osType", Appreference.getMODEL(getApplicationContext()));
                    jsonObject.put("osVersion", Appreference.getRELEASE(getApplicationContext()));
                    jsonObject.put("deviceModel", Appreference.getMANUFACTURER(getApplicationContext()));

                    Appreference.jsonRequestSender.userDeviceRegistration(EnumJsonWebservicename.userDeviceRegistration, jsonObject);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }else{
                Log.d("fcm","w/s call failed bcoz login state is = "+ AppSharedpreferences.getInstance(this).getBoolean("login") +"  and tokenId is " +Appreference.fcmTokenId );
            }
        }
    }

}
