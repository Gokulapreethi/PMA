package com.ase;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by saravanakumar on 6/27/2016.
 */
public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    final public static String ONE_TIME = "onetime";


    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
            wl.acquire();

            //You can do the processing here.
            Bundle extras = intent.getExtras();
            StringBuilder msgStr = new StringBuilder();
            if(extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)){
                //Make sure this intent has been sent by the one-time timer button.
                msgStr.append("One time Timer : ");
            }
            Format formatter = new SimpleDateFormat("hh:mm:ss a");
            msgStr.append(formatter.format(new Date()));
            Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();
            //Release the lock
            wl.release();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AlarmManagerBroadcastReceiver onReceive method ","Exception "+e.getMessage(),"WARN",null);
        }

    }


    public void SetAlarm(Context context)
    {
        try {
            AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
            intent.putExtra(ONE_TIME, Boolean.FALSE);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
            //After after 5 seconds
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5 , pi);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AlarmManagerBroadcastReceiver setAlarm method ","Exception "+e.getMessage(),"WARN",null);
        }
    }


    public void CancelAlarm(Context context)
    {
        try {
            Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(sender);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AlarmManagerBroadcastReceiver cancelAlarm method ","Exception "+e.getMessage(),"WARN",null);
        }
    }

    public void setOnetimeTimer(Context context){
        try {
            AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
            intent.putExtra(ONE_TIME, Boolean.TRUE);
            PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AlarmManagerBroadcastReceiver setOnetimeTimer method ","Exception "+e.getMessage(),"WARN",null);
        }
    }


}
