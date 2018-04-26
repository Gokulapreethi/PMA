package com.ase.gcm;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ase.Appreference;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import json.EnumJsonWebservicename;
import json.WebServiceInterface;

/**
 * Created by Preethi on 4/13/2018.
 */

public class NotificationActivity extends Activity implements WebServiceInterface {

    public static final String NOTIFICATION_ID = "NOTIFICATION_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(getIntent().getIntExtra(NOTIFICATION_ID, -1));
        finish(); // since finish() is called in onCreate(), onDestroy() will be called immediately
        onNewIntent(getIntent());
    }

    public static PendingIntent getDismissIntent(String notificationId, Context context) {
        Log.i("pms123","getDismissIntent=======>notificationId  "+notificationId);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(Integer.parseInt(notificationId));
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(NOTIFICATION_ID, notificationId);
        PendingIntent dismissIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        return dismissIntent;
//        Intent intent = new Intent(context, NotificationActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        intent.putExtra(NOTIFICATION_ID, notificationId);
//        PendingIntent dismissIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        return dismissIntent;
    }
    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("NotificationMessage")) {

                String key = extras.getString("NotificationKey");
                String response = extras.getString("NotificationMessage");
                int notifyId = extras.getInt("notifyId");
                cancelNotification(this, notifyId);

                JSONObject PMS_jsonObject = new JSONObject();
                try {
                    PMS_jsonObject.put("id",key);
                    PMS_jsonObject.put("sendStatus",intent.getAction());
                    PMS_jsonObject.put("message",response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
/*
                List<NameValuePair> tagNameValuePairs = new ArrayList<NameValuePair>();
                tagNameValuePairs.add(new BasicNameValuePair("id", key));
                tagNameValuePairs.add(new BasicNameValuePair("sendStatus",intent.getAction()));
                tagNameValuePairs.add(new BasicNameValuePair("message",response));*/
                Appreference.jsonRequestSender.getPMSNotificationDetails(EnumJsonWebservicename.updatePMSNotificationDetails, PMS_jsonObject,this);
                Log.i("pms123", " intent.getAction();=======>Action  " +  intent.getAction());
                Log.i("pms123", "getDismissIntent=======>key  " + key);
                Log.i("pms123", "notifyID=======>notifyId  " + notifyId);
                Log.i("pms123", "getDismissIntent=======>response  " + response);

            }
        }
    }
    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }
    @Override
    public void ResponceMethod(Object object) {
        Log.i("pms123", "NotificationActivity Response methos=======>");
    }

    @Override
    public void ErrorMethod(Object object) {

    }
}