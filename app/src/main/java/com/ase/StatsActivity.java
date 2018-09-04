package com.ase;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StatsActivity extends Activity implements DateTimePicker.DateWatcher {
    private static final int READ_PHONE_STATE_REQUEST = 37;
    public static final String EXTRA_PACKAGE = "ExtraPackage";

    AppCompatImageView ivIcon;
    Toolbar toolbar;
    Context context;

    TextView trafficStatsAllRx;
    TextView network_stats_mobile_data_title;
    TextView network_stats_wifi_data_title;
    TextView since_wifi_mobile;
    TextView selectedSince_wifiMobile;
    ImageView since_date_select;
    ProgressBar Wifi_ProgressBar;
    ProgressBar mobiledata_ProgressBar;
    TextView trafficStatsAllTx;
    TextView trafficStatsPackageRx;
    TextView trafficStatsPackageTx;

    TextView networkStatsAllRx;
    TextView networkStatsAllTx;
    TextView networkStatsPackageRx;
    TextView networkStatsPackageTx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_stats);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        context=this;
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ivIcon = (AppCompatImageView) findViewById(R.id.avatar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermissions();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    protected void onResume() {
        super.onResume();
        if (!hasPermissions()) {
            return;
        }
        initTextViews();

        checkIntent(getCurrentMonth());
    }

    private String getCurrentMonth() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) +1;
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//        String currentDate= "01"+"-"+month+"-"+year+" "+"00:00:00";
        return year+"-"+month+"-"+01+" "+"00:00:00";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkIntent(String dateToShow) {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        Bundle extras = intent.getExtras();
        if (extras == null) {
            return;
        }
        String packageName = extras.getString(EXTRA_PACKAGE);
        if (packageName == null) {
            return;
        }
        try {
            ivIcon.setImageDrawable(getPackageManager().getApplicationIcon(packageName));
            toolbar.setTitle(getPackageManager().getApplicationLabel(
                    getPackageManager().getApplicationInfo(
                            packageName, PackageManager.GET_META_DATA)));
            toolbar.setSubtitle(packageName + ":" + PackageManagerHelper.getPackageUid(this, packageName));
//            setSupportActionBar(toolbar);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (!PackageManagerHelper.isPackage(StatsActivity.this, packageName)) {
            return;
        }
        since_wifi_mobile.setText("since "+ dateToShow);

        fillData(packageName,dateToShow);
    }

    private void requestPermissions() {
        if (!hasPermissionToReadNetworkHistory()) {
            return;
        }
        if (!hasPermissionToReadPhoneStats()) {
            requestPhoneStateStats();
        }
    }

    private boolean hasPermissions() {
        return hasPermissionToReadNetworkHistory() && hasPermissionToReadPhoneStats();
    }

    private void initTextViews() {
        network_stats_mobile_data_title = (TextView) findViewById(R.id.network_stats_package_data_title);
        network_stats_wifi_data_title = (TextView) findViewById(R.id.textView2);
        since_wifi_mobile = (TextView) findViewById(R.id.since_wifi_mobile);
        selectedSince_wifiMobile = (TextView) findViewById(R.id.selectedSince);
        since_date_select = (ImageView) findViewById(R.id.clockDataTime);
        Wifi_ProgressBar = (ProgressBar) findViewById(R.id.Wifi_ProgressBar);
        mobiledata_ProgressBar = (ProgressBar) findViewById(R.id.mobiledata_ProgressBar);

//        trafficStatsAllRx = (TextView) findViewById(R.id.traffic_stats_all_rx_value);
//        trafficStatsAllTx = (TextView) findViewById(R.id.traffic_stats_all_tx_value);
//        trafficStatsPackageRx = (TextView) findViewById(R.id.traffic_stats_package_rx_value);
//        trafficStatsPackageTx = (TextView) findViewById(R.id.traffic_stats_package_tx_value);
//        networkStatsAllRx = (TextView) findViewById(R.id.network_stats_all_rx_value);
//        networkStatsAllTx = (TextView) findViewById(R.id.network_stats_all_tx_value);
//        networkStatsPackageRx = (TextView) findViewById(R.id.network_stats_package_rx_value);
//        networkStatsPackageTx = (TextView) findViewById(R.id.network_stats_package_tx_value);
        since_date_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //New Code Start
                    final Dialog mDateTimeDialog = new Dialog(context);
                    mDateTimeDialog.setCanceledOnTouchOutside(false);
                    // Inflate the root layout
                    final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater().inflate(R.layout.new_date_picker, null);

                    // Grab widget instance
                    final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);
                    mDateTimePicker.setDateChangedListener(StatsActivity.this);

                    // Update demo TextViews when the "OK" button is clicked
                    ((Button) mDateTimePicker.findViewById(R.id.month_plus)).setVisibility(View.VISIBLE);
                    ((Button) mDateTimePicker.findViewById(R.id.month_minus)).setVisibility(View.VISIBLE);
                    ((Button) mDateTimePicker.findViewById(R.id.date_plus)).setVisibility(View.VISIBLE);
                    ((Button) mDateTimePicker.findViewById(R.id.date_minus)).setVisibility(View.VISIBLE);
                    ((Button) mDateTimePicker.findViewById(R.id.year_plus)).setVisibility(View.VISIBLE);
                    ((Button) mDateTimePicker.findViewById(R.id.year_minus)).setVisibility(View.VISIBLE);
                    ((Button) mDateTimePicker.findViewById(R.id.hour_plus)).setVisibility(View.VISIBLE);
                    ((Button) mDateTimePicker.findViewById(R.id.hour_minus)).setVisibility(View.VISIBLE);
                    ((Button) mDateTimePicker.findViewById(R.id.min_plus)).setVisibility(View.VISIBLE);
                    ((Button) mDateTimePicker.findViewById(R.id.min_minus)).setVisibility(View.VISIBLE);
                    ((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            mDateTimePicker.clearFocus();
                            // TODO Auto-generated method stub
                            String result_string = mDateTimePicker.getMonth() + "-" + String.valueOf(mDateTimePicker.getDay()) + "-" + String.valueOf(mDateTimePicker.getYear())
                                    + "  " + String.valueOf(mDateTimePicker.getHour()) + ":" + String.valueOf(mDateTimePicker.getMinute());
                            Date date_from = null;
                            final Calendar c_date1 = Calendar.getInstance();

                            try {
                                date_from = new SimpleDateFormat("MMM-d-yyyy HH:mm").parse(result_string);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                Appreference.printLog("NewTaskConversation", "showStatusPopupWindow Hold date Exception : " + e.getMessage(), "WARN", null);
                            }
                            String date3 = null, showholdTimerDate = null;
                            Date date_to = null, date_initial = null, dt_temp = null;
                            final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                            if (date3 == null) {
                                try {
                                    date3 = sdf.format(c_date1.getTime());
                                    date_from = new SimpleDateFormat("MMM-d-yyyy HH:mm").parse(result_string);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("NewTaskConversation", "showStatusPopupWindow Hold date Exception : " + e.getMessage(), "WARN", null);
                                }
                            }
                            try {
                                date_to = sdf.parse(date3);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("NewTaskConversation", "showStatusPopupWindow Hold date Exception : " + e.getMessage(), "WARN", null);
                            }
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Log.i("Date123", "Date arrange===> ## " + result_string);
                            Log.i("Date123", "Date From===> ## " + date_from);
                            Log.i("Date123", "Date to===> ## " + date_to);
                            Log.i("Date123", "date3 ===> ## " + date3);
                            Log.i("Date123", "compareTo ===> ## " + date_from.compareTo(date_to));

                            String actual_timerdate = sdf.format(date_from);
                            Log.i("Date123", "actual_timerdate ===> ## " + actual_timerdate);
                            if (date_from.compareTo(date_to) < 0) {
                                try {
                                    dt_temp = dateFormat.parse(actual_timerdate);
                                    showholdTimerDate = originalFormat.format(dt_temp);
                                    Log.i("Date123", "result_string ===> ## " + result_string);
                                    Log.i("Date123", "dt_temp ===> &  ## " + dt_temp);
                                    Log.i("Date123", "showholdTimerDate ===> & ## " + showholdTimerDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Appreference.printLog("NewTaskConversation", "showStatusPopupWindow Hold date Exception : " + e.getMessage(), "WARN", null);
                                }
                                Log.i("Date123", "compareTo ===> if " + date_from.compareTo(date_to));
                                selectedSince_wifiMobile.setText(showholdTimerDate);
                                checkIntent(showholdTimerDate);
                                mDateTimeDialog.dismiss();
                            } else {
                                Toast.makeText(StatsActivity.this, "Please set Correct Timer...", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    // Cancel the dialog when the "Cancel" button is clicked
                    ((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog)).setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            mDateTimeDialog.cancel();
                            //                                                    dialog1.dismiss();
                        }
                    });
                    // Setup TimePicker
                    // No title on the dialog window
                    mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    // Set the dialog content view
                    mDateTimeDialog.setContentView(mDateTimeDialogView);
                    // Display the dialog
                    mDateTimeDialog.show();
                    //New Code End
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("NewTaskConversation", "showStatusPopupWindow Hold date Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });
    }

    private void fillData(String packageName,String startDateShow) {
        int uid = PackageManagerHelper.getPackageUid(this, packageName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkStatsManager networkStatsManager = (NetworkStatsManager) getApplicationContext().getSystemService(Context.NETWORK_STATS_SERVICE);
            NetworkStatsHelper networkStatsHelper = new NetworkStatsHelper(networkStatsManager, uid);
            Long timevalue=networkStatsHelper.getDataInMilliSeconds(startDateShow);
            fillNetworkStatsAll(networkStatsHelper,timevalue);
            fillNetworkStatsPackage(uid, networkStatsHelper,timevalue);
        }else {
            fillTrafficStatsAll();
            fillTrafficStatsPackage(uid);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void fillNetworkStatsAll(NetworkStatsHelper networkStatsHelper, Long timevalue) {
        long mobileWifiRx = networkStatsHelper.getAllRxBytesMobile(this, timevalue) + networkStatsHelper.getAllRxBytesWifi(timevalue);
        long mobileWifiTx = networkStatsHelper.getAllTxBytesMobile(this, timevalue) + networkStatsHelper.getAllTxBytesWifi(timevalue);

        long WifiTxRx = networkStatsHelper.getAllRxBytesWifi(timevalue) + networkStatsHelper.getAllTxBytesWifi(timevalue);

        Log.i("helper123", "ALL  WifiTxRx + Wifi: " + WifiTxRx);

        Log.i("helper123", "ALL  getAllRxBytesMobile + mobile: " + mobileWifiRx);
        Log.i("helper123", "ALL  getAllTxBytesMobile + Wifi: " + mobileWifiTx);



        long wifiDataRx =  networkStatsHelper.getPackageRxBytesWifi(timevalue);
        long wifiDataTx =  networkStatsHelper.getPackageTxBytesWifi(timevalue);
//        trafficStatsAllRx.setText(wifiDataRx + " Bx");
//        networkStatsAllRx.setText(wifiDataTx + " Bx");
        long totalWifiData=wifiDataRx+wifiDataTx;
        String wifiDataResult;
        wifiDataResult=getFileSize(totalWifiData);
        int fileLength= 0;
        try {
            fileLength = (int) (totalWifiData*100/(WifiTxRx/2));
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }
        Log.i("helper123", "Wifi Data Percentage===> **** : " + fileLength);
        Wifi_ProgressBar.setMax(100);
        Wifi_ProgressBar.setProgress(fileLength);

        network_stats_wifi_data_title.setText(wifiDataResult + "  and Total => "+getFileSize(WifiTxRx));
       /* if(wifiDataRx <=0 && wifiDataTx <=0){
            fillTrafficStatsAll();
        }*/
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";

        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
    @TargetApi(Build.VERSION_CODES.M)
    private void fillNetworkStatsPackage(int uid, NetworkStatsHelper networkStatsHelper, Long timevalue) {
        long mobileWifiRx = networkStatsHelper.getPackageRxBytesMobile(this,timevalue);
//        trafficStatsPackageRx.setText(mobileWifiRx + " By");
        long mobileWifiTx = networkStatsHelper.getPackageTxBytesMobile(this,timevalue);
//        networkStatsPackageRx.setText(mobileWifiTx + " By");

        long MobileTxRx = networkStatsHelper.getAllRxBytesMobile(this,timevalue) + networkStatsHelper.getAllTxBytesMobile(this,timevalue);

        long totalMobileData=mobileWifiRx+mobileWifiTx;
        String mobileDataResult;
        mobileDataResult=getFileSize(totalMobileData);
        int fileLength= 0;

        try {
            fileLength = (int) (totalMobileData*100/(MobileTxRx/2));
        } catch (ArithmeticException e) {
            e.printStackTrace();
        }


        Log.i("helper123", "Wifi Data Percentage===> **** : " + fileLength);

        mobiledata_ProgressBar.setMax(100);
        mobiledata_ProgressBar.setProgress(fileLength);
        network_stats_mobile_data_title.setText(mobileDataResult + "  and Total => "+getFileSize(MobileTxRx));
        /*if(mobileWifiRx <=0 && mobileWifiTx <=0){
            fillTrafficStatsPackage(uid);
        }*/
    }

    private void fillTrafficStatsAll() {
//        trafficStatsAllRx.setText(TrafficStatsHelper.getAllRxBytes() + " B");
//        networkStatsAllRx.setText(TrafficStatsHelper.getAllTxBytes() + " B");
    }


    private void fillTrafficStatsPackage(int uid) {
//        trafficStatsPackageRx.setText(TrafficStatsHelper.getPackageRxBytes(uid) + " B");
//        networkStatsPackageRx.setText(TrafficStatsHelper.getPackageTxBytes(uid) + " B");
    }

    private boolean hasPermissionToReadPhoneStats() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
            return false;
        } else {
            return true;
        }
    }

    private void requestPhoneStateStats() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_REQUEST);
    }

    private boolean hasPermissionToReadNetworkHistory() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        final AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(), getPackageName());
        if (mode == AppOpsManager.MODE_ALLOWED) {
            return true;
        }
        appOps.startWatchingMode(AppOpsManager.OPSTR_GET_USAGE_STATS,
                getApplicationContext().getPackageName(),
                new AppOpsManager.OnOpChangedListener() {
                    @Override
                    @TargetApi(Build.VERSION_CODES.M)
                    public void onOpChanged(String op, String packageName) {
                        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                                android.os.Process.myUid(), getPackageName());
                        if (mode != AppOpsManager.MODE_ALLOWED) {
                            return;
                        }
                        appOps.stopWatchingMode(this);
                        Intent intent = new Intent(StatsActivity.this, StatsActivity.class);
                        if (getIntent().getExtras() != null) {
                            intent.putExtras(getIntent().getExtras());
                        }
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(intent);
                    }
                });
        requestReadNetworkHistoryAccess();
        return false;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void requestReadNetworkHistoryAccess() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        startActivity(intent);
    }

    @Override
    public void onDateChanged(Calendar c) {

    }
}
