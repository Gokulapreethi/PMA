package com.ase;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.NetworkStatsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import org.json.JSONException;
import org.json.JSONObject;
import org.pjsip.pjsua2.app.MainActivity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import json.EnumJsonWebservicename;
import json.WebServiceInterface;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by Preethi on 7/2/2018.
 */

public class CheckRunningApplicationReceiver extends BroadcastReceiver implements WebServiceInterface {
    public final String TAG = "CRAR"; // CheckRunningApplicationReceiver
    ActivityManager am;

    @Override
    public void onReceive(Context aContext, Intent intent1) {
        // TODO Auto-generated method stub

        try {

            Log.i("locker124", "OnReceive Fired");

            am = (ActivityManager) aContext
                    .getSystemService(ACTIVITY_SERVICE);

            List<ActivityManager.RunningTaskInfo> alltasks = am
                    .getRunningTasks(1);

            ActivityManager.RunningTaskInfo ar = alltasks.get(0);

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            ResolveInfo defaultLauncher = aContext.getPackageManager()
                    .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            String nameOfLauncherPkg = defaultLauncher.activityInfo.packageName;


            ConnectivityManager connManager = (ConnectivityManager) aContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

            if (!mWifi.isConnected() && networkInfo != null && networkInfo.isConnected()) {
                // Do whatever
                Log.i("locker1234", "mobile data Connected ");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    List<AndroidAppProcess> MyAppList = null;
                    String topPackageName = "";
                    ArrayList<String> myForegroundApps = new ArrayList<>();
                    topPackageName = getForegroundAppList(aContext);
                    myForegroundApps.add(topPackageName);

                    for (int i = 0; i < myForegroundApps.size(); i++) {

                        Appreference.printLog("CheckRunningApplication", "Package name List : " + i + topPackageName, "WARN", null);

                        if (myForegroundApps.contains("com.ase")
                                || myForegroundApps.contains("com.android.launcher")
                                || myForegroundApps.contains("com.sec.android.app.launcher")
                                || myForegroundApps.contains("com.google.android.gm")
                                || myForegroundApps.contains("com.sec.android.app.popupcalculator")
                                || myForegroundApps.contains("com.sec.android.gallery3d")
                                || myForegroundApps.contains("com.android.settings")
                                || myForegroundApps.contains("com.android.contacts")
                                || myForegroundApps.contains(nameOfLauncherPkg)
                                || ar.topActivity.toString().contains(
                                "recent.RecentsActivity")) {
                            Log.i("locker124", "package Name to close above 5.0 " + topPackageName);


                        } else {
                            MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");

                            if (topPackageName != null && !topPackageName.toString().equalsIgnoreCase("com.estrongs.android.pop")) {
                                if (mainActivity != null) {
                                    mainActivity.killApp(0);
                                } else {
                                    Intent startHomescreen = new Intent(Intent.ACTION_MAIN);
                                    startHomescreen.addCategory(Intent.CATEGORY_HOME);
                                    startHomescreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startHomescreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    aContext.startActivity(startHomescreen);
                                }
                            }
                        }

                        if (topPackageName != null && topPackageName.equals("com.ase")) {


                            MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");

                            if (mainActivity != null) {
                                mainActivity.getDataUsage();
                            }
                        }
                    }
//                    sendDataUsageFromNetWorkStats(aContext);

                } else {
                    String packageNameList = am.getRunningAppProcesses().get(0).processName;

                    if (!packageNameList.contains("com.ase")
                            || !packageNameList.contains("com.android.launcher")
                            || !packageNameList.contains("com.sec.android.app.launcher")
                            || !packageNameList.contains("com.google.android.gm")
                            || !packageNameList.contains("com.android.settings")
                            || !packageNameList.contains("com.sec.android.app.popupcalculator")
                            || !packageNameList.contains("com.sec.android.gallery3d")
                            || !packageNameList.contains("com.android.contacts")
                            || !packageNameList.equals(nameOfLauncherPkg)
                            || !ar.topActivity.toString().contains(
                            "recent.RecentsActivity")) {
                        Log.i("locker124", "package Name to close below 5.0 " + packageNameList);
                        MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");

                        if (mainActivity != null) {
                            mainActivity.killApp(0);
                        } else {
                            Intent startHomescreen = new Intent(Intent.ACTION_MAIN);
                            startHomescreen.addCategory(Intent.CATEGORY_HOME);
                            startHomescreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startHomescreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            aContext.startActivity(startHomescreen);
                        }
                    }
                    if (packageNameList != null && packageNameList.equals("com.ase")) {
                        MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");

                        if (mainActivity != null) {
                            mainActivity.getDataUsage();
                        }
                    }
                }
            } else {
                Log.i("locker1234", "mWifi Connected ");
            }
        } catch (Exception t) {
            Appreference.printLog("CheckRunningApplication", "Forground app Packagelist  Exception : " + t.getMessage(), "WARN", null);
            Log.i(TAG, "Throwable caught: " + t.getMessage(), t);
            Log.i("locker1234", "Exception 1 " + t.getMessage());
        }

    }
    private String getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) +1;
        int day = c.get(Calendar.DATE);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//        String currentDate= "01"+"-"+month+"-"+year+" "+"00:00:00";
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
            Log.i("helper111", "SendDataMessage context" + CheckRunningApplicationReceiver.this);

            Appreference.jsonRequestSender.DataUsageSender(EnumJsonWebservicename.userDeviceManagementDetails, jsonObject, CheckRunningApplicationReceiver.this);

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
    private String getForegroundAppList(Context aContext) {
        String topPackageName = "";
        MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");

        if (mainActivity != null) {
            if (mainActivity.hasPermission()) {
                topPackageName = getAppList(aContext);
            } else {
                mainActivity.requestPermission();
            }
        } else {
            topPackageName = getAppList(aContext);
        }
        return topPackageName;
    }

    private String getAppList(Context aContext) {
        String topPackageName = "";

        UsageStatsManager usage = (UsageStatsManager) aContext.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        List<UsageStats> stats = usage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1), System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1));
//            TextView lTextView = (TextView) findViewById(R.id.usage_stats);

        if (stats != null) {
            SortedMap<Long, UsageStats> runningTask = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : stats) {
                runningTask.put(usageStats.getLastTimeUsed(), usageStats);
            }
            Log.i("locker124", "list Size NOUGAT===> " + runningTask.size());

            if (runningTask.isEmpty()) {
//                    return null;
            }
            topPackageName = runningTask.get(runningTask.lastKey()).getPackageName();
            Log.i("locker124", "package Name FOREGROUND NOUGAT===> " + topPackageName);

        }

        StringBuilder lStringBuilder = new StringBuilder();

        for (UsageStats lUsageStats : stats) {
            lStringBuilder.append(lUsageStats.getPackageName());
            lStringBuilder.append("\r\n");
        }
//            lTextView.setText(lStringBuilder.toString());
        return topPackageName;
    }


    private void forceClose(String app) {
        try {
            Process suProcess = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(suProcess.getOutputStream());

            os.writeBytes("adb shell" + "\n");

            os.flush();

            os.writeBytes("am force-stop " + app + "\n");

            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    String[] getActivePackagesCompat() {
        final List<ActivityManager.RunningTaskInfo> taskInfo = am
                .getRunningTasks(1);
        final ComponentName componentName = taskInfo.get(0).topActivity;
        final String[] activePackages = new String[1];
        activePackages[0] = componentName.getPackageName();
        return activePackages;
    }

    String[] getActivePackages() {
        final Set<String> activePackages = new HashSet<String>();
        final List<ActivityManager.RunningAppProcessInfo> processInfos = am
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfos) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                activePackages.addAll(Arrays.asList(processInfo.pkgList));
            }
        }
        return activePackages.toArray(new String[activePackages.size()]);
    }

    //API 21 and above
    private List<AndroidAppProcess> getProcessNew(Context aContext) throws Exception {
        String topApp = "Not Exist";
        List<AndroidAppProcess> processes = AndroidProcesses.getRunningForegroundApps(aContext);
        Collections.sort(processes, new AndroidProcesses.ProcessComparator());
        return processes;
    }

    @Override
    public void ResponceMethod(Object object) {
        Log.i("checklist123", "ResponceMethod userDeviceManagementDetails Saved Successfully====> ");

    }

    @Override
    public void ErrorMethod(Object object) {

    }
}

