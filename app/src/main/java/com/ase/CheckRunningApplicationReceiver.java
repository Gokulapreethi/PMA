package com.ase;

import android.app.ActivityManager;
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
import android.util.Log;

import com.jaredrummler.android.processes.AndroidProcesses;
import com.jaredrummler.android.processes.models.AndroidAppProcess;

import org.pjsip.pjsua2.app.MainActivity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by Preethi on 7/2/2018.
 */

public class CheckRunningApplicationReceiver extends BroadcastReceiver {
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

            String[] activePackages;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                activePackages = getActivePackages();
            } else {
                activePackages = getActivePackagesCompat();
            }

            if (activePackages != null) {
                for (String activePackage : activePackages) {
                    if (activePackage.equals("com.google.android.calendar")) {
                        // Calendar app is launched, do something
                    }
                }
            }

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
                    List<AndroidAppProcess> MyAppList = getProcessNew(aContext);
                    ArrayList<String> myForegroundApps = new ArrayList<>();
                    for (int i = 0; i < MyAppList.size(); i++) {
                        myForegroundApps.add(MyAppList.get(i).getPackageName());
                    }
                    for (int i = 0; i < MyAppList.size(); i++) {
                        String packageNameList = MyAppList.get(i).getPackageName();
                        String packageNameList1 = MyAppList.get(i).name;
                        Log.i("locker124", "package Name FOREGROUND===> " + packageNameList);
                        Log.i("locker124", "package Name FOREGROUND name===> " + packageNameList1);
                        Appreference.printLog("CheckRunningApplication", "Package name List : " + i + packageNameList, "WARN", null);

                   /*     if (packageNameList.equals("com.android.chrome")
                                || packageNameList.contains("com.android.vending")
                                || packageNameList.contains("com.google.android.youtube")
                                || packageNameList.contains("com.google.android.apps.maps")
                                || packageNameList.contains("com.sec.android.app.samsungapps")
                                || packageNameList.contains("com.xuecs.sqlitemanager")
                                || packageNameList.contains("com.google.android.videos")
                                || packageNameList.contains("com.google.android.apps.plus")
                                || packageNameList.contains("com.android.browser")
                                || packageNameList.contains("com.dropbox.android")
                                || packageNameList.contains("com.utorrent.client")
                                || packageNameList.contains("com.google.android.googlequicksearchbox:search")
                                || packageNameList.contains("com.google.android.apps.magazines")
                                || packageNameList.contains("com.google.android.talk")
                                || packageNameList.equals(nameOfLauncherPkg)) {
                            Log.i("locker124", "package Name to close above 5.0 " + packageNameList);

                            MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");

                            if (mainActivity != null) {
                                mainActivity.killApp(0);
                            }else{
                                Intent startHomescreen = new Intent(Intent.ACTION_MAIN);
                                startHomescreen.addCategory(Intent.CATEGORY_HOME);
                                startHomescreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startHomescreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                aContext.startActivity(startHomescreen);
                            }
                        }
                        if(packageNameList.equals("com.ase")){
                            MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");

                            if (mainActivity != null) {
                                mainActivity.getDataUsage();
                            }
                        }
*/
                        Log.i("locker124", "package Name contains myForegroundApps.contains(\"com.ase\")==>" + myForegroundApps.contains("com.ase"));

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
                            Log.i("locker124", "package Name to close above 5.0 " + packageNameList);


                        } else {
                            MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");

                            if (!packageNameList.toString().equalsIgnoreCase("com.estrongs.android.pop")) {
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
                        if (packageNameList.equals("com.ase")) {
                            MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");

                            if (mainActivity != null) {
                                mainActivity.getDataUsage();
                            }
                        }
                    }
                } else {
                    String packageNameList = am.getRunningAppProcesses().get(0).processName;
                  /*   if (packageNameList.equals("com.android.chrome")
                            || packageNameList.contains("com.android.vending")
                            || packageNameList.contains("com.google.android.youtube")
                            || packageNameList.contains("com.google.android.apps.maps")
                            || packageNameList.contains("com.sec.android.app.samsungapps")
                            || packageNameList.contains("com.xuecs.sqlitemanager")
                            || packageNameList.contains("com.google.android.videos")
                            || packageNameList.contains("com.google.android.apps.plus")
                            || packageNameList.contains("com.android.browser")
                            || packageNameList.contains("com.dropbox.android")
                            || packageNameList.contains("com.utorrent.client")
                            || packageNameList.contains("com.google.android.googlequicksearchbox:search")
                            || packageNameList.contains("com.google.android.apps.magazines")
                            || packageNameList.contains("com.google.android.talk")
                            || packageNameList.equals(nameOfLauncherPkg)) {
                        Log.i("locker124", "package Name to close above 5.0 " + packageNameList);
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

                    if (packageNameList.equals("com.ase")) {
                        MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");

                        if (mainActivity != null) {
                            mainActivity.getDataUsage();
                        }
                }*/
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
                    if (packageNameList.equals("com.ase")) {
                        MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");

                        if (mainActivity != null) {
                            mainActivity.getDataUsage();
                        }
                    }
                }
            } else {
                Log.i("locker1234", "mWifi Connected ");
            }
          /*  if (mWifi.isConnected()) {
                Log.i("locker1234", "mWifi Connected ");
            }
            if (networkInfo == null) {
                Log.i("locker1234", "No Network Connected ");

            }*/

        } catch (Exception t) {
            Appreference.printLog("CheckRunningApplication", "Forground app Packagelist  Exception : " + t.getMessage(), "WARN", null);
            Log.i(TAG, "Throwable caught: " + t.getMessage(), t);
            Log.i("locker1234", "Exception 1 " + t.getMessage());
        }

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


        UsageStatsManager usage = (UsageStatsManager) aContext.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        List<UsageStats> stats = usage.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
        if (stats != null) {
            SortedMap<Long, UsageStats> runningTask = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : stats) {
                runningTask.put(usageStats.getLastTimeUsed(), usageStats);
            }
            Log.i("locker124", "package Name FOREGROUND NOUGAT===> " + runningTask.);

        }

        return processes;
    }
}

