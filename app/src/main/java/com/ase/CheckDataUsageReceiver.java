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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

    private String getCurrentMonth(Boolean isUTC) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DATE);
        Date Parse_date = null;
        String Result = null;
        String monthNow = "", dayOfMonth = "";
        if (month < 10) {
            monthNow = "0" + month;
        } else {
            monthNow = String.valueOf(month);
        }
        if (day < 10) {
            dayOfMonth = "0" + day;
        } else {
            dayOfMonth = String.valueOf(day);
        }
        if (isUTC) {
            /*SimpleDateFormat formatterIST = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat formatterUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Parse_date = formatterUTC.parse(year + "-" + monthNow + "-" + dayOfMonth + " " + "00:00:00");
                formatterUTC.setTimeZone(TimeZone.getTimeZone("UTC")); // UTC timezone
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Result = formatterUTC.format(Parse_date);*/

            Result = customLocalDateToUTC(year + "-" + monthNow + "-" + dayOfMonth + " " + "00:00:00");
        } else {
            Result = year + "-" + monthNow + "-" + dayOfMonth + " " + "00:00:00";
        }
        return Result;
    }

    public static String customLocalDateToUTC(String date) {

        String resultDate = null;
        try {
            SimpleDateFormat utcDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date dateString = null;
            if (date != null && !date.equalsIgnoreCase(""))
                 dateString =dateFormat.parse(date);
                resultDate = utcDateFormat.format(dateString);
//                resultDate = utcDateFormat.format(sdf3.parse(sdf3.format(dateFormat.parse(date))));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return resultDate;
    }

    private void sendDataUsageFromNetWorkStats(Context aContext) {
        int uid = PackageManagerHelper.getPackageUid(aContext, "com.ase");
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) aContext.getSystemService(Context.NETWORK_STATS_SERVICE);
        NetworkStatsHelper networkStatsHelper = new NetworkStatsHelper(networkStatsManager, uid);
        Long timevalue = networkStatsHelper.getDataInMilliSeconds(getCurrentMonth(false));
        Long StatsThisMonth = networkStatsHelper.getDataInMilliSeconds(getStart_this_Month());

        /*For Wifi data*/
        long WifiTxRx = networkStatsHelper.getAllRxBytesWifi(timevalue) + networkStatsHelper.getAllTxBytesWifi(timevalue);
        long WifiTxRx_Total_perMonth = networkStatsHelper.getAllRxBytesWifi(StatsThisMonth) + networkStatsHelper.getAllTxBytesWifi(StatsThisMonth);

        long wifiDataRx = networkStatsHelper.getPackageRxBytesWifi(timevalue);
        long wifiDataTx = networkStatsHelper.getPackageTxBytesWifi(timevalue);
        long Today_totalHMData = wifiDataRx + wifiDataTx;
        String wifiDataResult;
        wifiDataResult = getFileSize(Today_totalHMData);

        long wifiTotalHmDataRx = networkStatsHelper.getPackageRxBytesWifi(timevalue);
        long wifiTotalHmDataTx = networkStatsHelper.getPackageTxBytesWifi(timevalue);

        long Month_totalHMData = wifiTotalHmDataRx + wifiTotalHmDataTx;
        String MonthHMDataResult = getFileSize(Month_totalHMData);
        /* END*/

        /*For Mobile data*/
        long MobileTxRx = networkStatsHelper.getAllRxBytesMobile(aContext, timevalue) + networkStatsHelper.getAllTxBytesMobile(aContext, timevalue);
        long MobileTxRx_Total_perMonth = networkStatsHelper.getAllRxBytesMobile(aContext, StatsThisMonth) + networkStatsHelper.getAllTxBytesMobile(aContext, StatsThisMonth);

        long mobiledataRx = networkStatsHelper.getPackageRxBytesMobile(aContext, timevalue);
        long mobiledataTx = networkStatsHelper.getPackageTxBytesMobile(aContext, timevalue);
        long totalMobileData = mobiledataRx + mobiledataTx;
        String mobileDataResult;
        mobileDataResult = getFileSize(totalMobileData);
        /*END*/

        Log.i("helper111", "Wifi Data Percentage Com.ASE " + wifiDataResult);
        Log.i("helper111", "ALL Wifi Data  " + getFileSize(MobileTxRx));

        Log.i("helper111", "****  Today totalHMData  " + getFileSize(Today_totalHMData));
        Log.i("helper111", "****  Month totalHMData  " + getFileSize(Month_totalHMData));
        Log.i("helper111", "******* Total WifiTxRx  " + getFileSize(WifiTxRx));
        Log.i("helper111", "********** WifiTxRx_Total_perMonth " + getFileSize(WifiTxRx_Total_perMonth));

        SendDataMessage(Today_totalHMData, Month_totalHMData, WifiTxRx, WifiTxRx_Total_perMonth, aContext);
    }

    private String getStart_this_Month() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DATE);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return year + "-" + month + "-" + 01 + " " + "00:00:00";
    }

    private void SendDataMessage(long myAppUsage, long MyAppMonthUsage, long AllAppUsage, long Total_this_Month, Context aContext) {
        try {
            JSONObject jsonObject = new JSONObject();

          /*  TelephonyManager tm = (TelephonyManager) aContext.getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = tm.getDeviceId();*/
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("id", Appreference.loginuserdetails.getId());
            jsonObject.put("from", jsonObject2);


            jsonObject.put("startDate", getCurrentMonth(true));
            SimpleDateFormat simpleDateFormat_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat_1.setTimeZone(TimeZone.getTimeZone("UTC"));
            String curr_date = simpleDateFormat_1.format(new Date());
            jsonObject.put("enddDate", curr_date);
            jsonObject.put("individual", myAppUsage);
            jsonObject.put("total", AllAppUsage);
            jsonObject.put("thisMonth", Total_this_Month);
            jsonObject.put("individualMonth", MyAppMonthUsage);
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

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
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
