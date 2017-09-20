package com.ase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.pjsip.pjsua2.app.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Preethi on 8/30/2017.
 */

public class StatusAlarmManager extends BroadcastReceiver {
    public StatusAlarmManager() {
        super();
    }
    Context context;
    String endTime,jobcodeNo;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("alarm123", "StatusAlarmManager=onReceive==========> " );
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tm = time.format(new Date());
        Log.i("alarm123", "Now Time Is====>" + tm);
        int id = intent.getExtras().getInt("id");
        endTime = intent.getExtras().getString("endTime");
        String currentStatus = intent.getStringExtra("currentStatus");
        Log.i("alarm123", "StatusAlarmManager Now status Is====>" + currentStatus);
        jobcodeNo = intent.getExtras().getString("currentProjectId");
        String alrmtask = String.valueOf(id);

        MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
        if(mainActivity!=null)
        mainActivity.showAlarmAlert(currentStatus,id,jobcodeNo);


    }


}
