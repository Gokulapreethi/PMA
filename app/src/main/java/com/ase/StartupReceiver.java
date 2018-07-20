package com.ase;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Preethi on 7/2/2018.
 */

public class StartupReceiver extends BroadcastReceiver {
    static final String TAG = "SR";

    final int startupID = 1111111;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("locker1234", "StartupReceiver onReceive Fired");


        // Create AlarmManager from System Services
        final AlarmManager alarmManager = (AlarmManager) context
                .getSystemService(Context.ALARM_SERVICE);
        try{
            // Create pending intent for CheckRunningApplicationReceiver.class
            // it will call after each 5 seconds
            Calendar c = Calendar.getInstance();
            c.add(Calendar.SECOND, 10);
            long afterTenSeconds = c.getTimeInMillis();

            Intent i7 = new Intent(context, CheckRunningApplicationReceiver.class);
            PendingIntent ServiceManagementIntent = PendingIntent.getBroadcast(context,
                    startupID, i7, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    afterTenSeconds,
                    5000, ServiceManagementIntent);

        } catch (Exception e) {
            Log.i(TAG, "Exception : "+e);
        }

    }


}
