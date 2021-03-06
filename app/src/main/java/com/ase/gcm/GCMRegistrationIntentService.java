package com.ase.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by saravanakumar on 7/8/2016.
 */
public class GCMRegistrationIntentService extends IntentService{

    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";

        public GCMRegistrationIntentService() {
            super("");
        }

    @Override
    protected void onHandleIntent(Intent intent) {

        registerGCM();
    }



    private void registerGCM() {
        //Registration complete intent initially null
        Intent registrationComplete = null;

        //Register token is also null
        //we will get the token on successfull registration
        String token = null;
        try {
            //Creating an instanceid


           /* InstanceID instanceID = InstanceID.getInstance(this);
             token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);*/
            // [END get_token]
            Log.i("gcmMessage", "GCM Registration Token: " + token);
            //Displaying the token in the log so that we can copy it to send push notification
            //You can also extend the app by storing the token in to your server
//            Log.w("GCMRegIntentService", "token:" + token);

            //on registration complete creating intent with success
            registrationComplete = new Intent(REGISTRATION_SUCCESS);

//            Putting the token to the intent
            registrationComplete.putExtra("token", token);
        } catch (Exception e) {
            //If any error occurred
            Log.w("GCMRegIntentService", "Registration error");
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }

        //Sending the broadcast that registration is completed
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

}
