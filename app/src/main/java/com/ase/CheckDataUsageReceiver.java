package com.ase;

import android.app.usage.NetworkStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import json.EnumJsonWebservicename;
import json.WebServiceInterface;

/**
 * Created by Preethi on 9/3/2018.
 */

public class CheckDataUsageReceiver extends BroadcastReceiver implements WebServiceInterface {
    @Override
    public void onReceive(Context aContext, Intent intent) {
        try {
            Log.i("helper111", "CheckDataUsageReceiver OnReceive Fired");

            ConnectivityManager connManager = (ConnectivityManager) aContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (mWifi.isConnected() && networkInfo != null && networkInfo.isConnected()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    sendDataUsageFromNetWorkStats(aContext);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) +1;
        int day = c.get(Calendar.DATE);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//        String currentDate= "01"+"-"+month+"-"+year+n " "+"00:00:00";
        return year+"-"+month+"-"+day+" "+"00:00:00";
    }
    private void sendDataUsageFromNetWorkStats(Context aContext) {
        int uid = PackageManagerHelper.getPackageUid(aContext, "com.ase");
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) aContext.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStatsHelper networkStatsHelper = new NetworkStatsHelper(networkStatsManager, uid);
        Long timevalue=networkStatsHelper.getDataInMilliSeconds(getCurrentMonth());
        Long StatsThisMonth=networkStatsHelper.getDataInMilliSeconds(getStart_this_Month());

        /*For Wifi data*/
        long WifiTxRx = networkStatsHelper.getAllRxBytesWifi(timevalue) + networkStatsHelper.getAllTxBytesWifi(timevalue);
        long WifiTxRx_Total_perMonth = networkStatsHelper.getAllRxBytesWifi(StatsThisMonth) + networkStatsHelper.getAllTxBytesWifi(StatsThisMonth);

        long wifiDataRx =  networkStatsHelper.getPackageRxBytesWifi(timevalue);
        long wifiDataTx =  networkStatsHelper.getPackageTxBytesWifi(timevalue);
        long totalHMData=wifiDataRx+wifiDataTx;
        String wifiDataResult;
        wifiDataResult=getFileSize(totalHMData);
        /* END*/

        /*For Mobile data*/
        long MobileTxRx = networkStatsHelper.getAllRxBytesMobile(aContext,timevalue) + networkStatsHelper.getAllTxBytesMobile(aContext,timevalue);
        long MobileTxRx_Total_perMonth = networkStatsHelper.getAllRxBytesMobile(aContext,StatsThisMonth) + networkStatsHelper.getAllTxBytesMobile(aContext,StatsThisMonth);

        long mobiledataRx = networkStatsHelper.getPackageRxBytesMobile(aContext,timevalue);
        long mobiledataTx = networkStatsHelper.getPackageTxBytesMobile(aContext,timevalue);
        long totalMobileData=mobiledataRx+mobiledataTx;
        String mobileDataResult;
        mobileDataResult=getFileSize(totalMobileData);
        /*END*/

        Log.i("helper111", "Wifi Data Percentage Com.ASE " + wifiDataResult);
        Log.i("helper111", "ALL Wifi Data  " + getFileSize(MobileTxRx));

        Log.i("helper111", "****  totalHMData  " + getFileSize(totalHMData));
        Log.i("helper111", "******* Total WifiTxRx  " + getFileSize(WifiTxRx));
        Log.i("helper111", "********** WifiTxRx_Total_perMonth " + getFileSize(WifiTxRx_Total_perMonth));

        SendDataMessage(totalHMData,WifiTxRx,WifiTxRx_Total_perMonth,aContext);
    }

    private String getStart_this_Month() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) +1;
        int day = c.get(Calendar.DATE);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return year+"-"+month+"-"+01+" "+"00:00:00";
    }

    private void SendDataMessage(long myAppUsage, long AllAppUsage, long Total_this_Month,Context aContext) {
        try {
            JSONObject jsonObject = new JSONObject();

          /*  TelephonyManager tm = (TelephonyManager) aContext.getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = tm.getDeviceId();*/
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("id", Appreference.loginuserdetails.getId());
            jsonObject.put("from", jsonObject2);


            jsonObject.put("startDate", getCurrentMonth());
            SimpleDateFormat simpleDateFormat_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String curr_date = simpleDateFormat_1.format(new Date());
            jsonObject.put("enddDate", curr_date);
            jsonObject.put("individual", myAppUsage);
            jsonObject.put("total",AllAppUsage);
            jsonObject.put("thisMonth",Total_this_Month);
            Log.i("helper111", "SendDataMessage Enum" + EnumJsonWebservicename.userDeviceManagementDetails);
            Log.i("helper111", "SendDataMessage json" + jsonObject);
            Log.i("helper111", "SendDataMessage context" + CheckDataUsageReceiver.this);

            Appreference.jsonRequestSender.DataUsageSender(EnumJsonWebservicename.userDeviceManagementDetails, jsonObject, CheckDataUsageReceiver.this);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";

        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    @Override
    public void ResponceMethod(Object object) {
        Log.i("checklist123", "ResponceMethod userDeviceManagementDetails Saved Successfully====> ");
    }

    @Override
    public void ErrorMethod(Object object) {

    }
}
