/* $Id: MainActivity.java 5022 2015-03-25 03:41:21Z nanang $ */
/*
 * Copyright (C) 2013 Teluu Inc. (http://www.teluu.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package org.pjsip.pjsua2.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
//import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.Bean.PdfManualListBean;
import com.ase.CustomVideoCamera;
import com.ase.DatePicker.CustomTravelPickerActivity;
import com.ase.ShowEstimTimeupAlert;
import com.ase.ShowTimeupAlert;
import com.ase.gcm.GCMPushReceiverService;
import com.ase.offlineSendService;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ase.AllTaskList;
import com.ase.AppSharedpreferences;
import com.ase.Appreference;
import com.ase.AudioRecorder;
import com.ase.Bean.ListAllTaskBean;
import com.ase.Bean.ProjectDetailsBean;
import com.ase.Bean.SipNotification;
import com.ase.Bean.TaskDetailsBean;
import com.ase.BlogsFragment;
import com.ase.CallerDetailsBean;
import com.ase.ChatFragment;
import com.ase.ContactBean;
import com.ase.ContactsFragment;
import com.ase.DB.VideoCallDataBase;
import com.ase.DatePicker.CustomTravelPickerActivity;
import com.ase.Forms.FormAccessBean;
import com.ase.Forms.FormEntryViewActivity;
import com.ase.LoginActivity;
import com.ase.MySSLSocketFactory;
import com.ase.NewTaskConversation;
import com.ase.ProjectHistory;
import com.ase.R;
import com.ase.RandomNumber.IncomingCallAlert;
import com.ase.ScheduleManager;
import com.ase.SettingsFragment;
import com.ase.ShowTimeupAlert;
import com.ase.TaskHistory;
import com.ase.TaskNotification;
import com.ase.TasksFragment;
import com.ase.TravelJobDetails;
import com.ase.activity.SchedulerActivity;
import com.ase.call_list.Call_ListBean;
import com.ase.chat.ChatActivity;
import com.ase.chat.ChatAlert;
import com.ase.chat.ChatBean;
import com.ase.gcm.GCMRegistrationIntentService;
import com.ase.gsm.PhoneStateChangeListener;
import com.ase.gsm.PhoneStateChangeNofication;
import com.ase.sketh.ProjectsFragment;
import com.ase.thread.DbEntryBean;
import com.ase.thread.MessageAccess;
import com.ase.thread.MessageEntryThread;

import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AccountPresConfig;
import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.AuthCredInfoVector;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallMediaInfo;
import org.pjsip.pjsua2.CallMediaInfoVector;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.PresenceStatus;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.StringVector;
import org.pjsip.pjsua2.VideoPreview;
import org.pjsip.pjsua2.VideoWindow;
import org.pjsip.pjsua2.pjmedia_srtp_use;
import org.pjsip.pjsua2.pjmedia_type;
import org.pjsip.pjsua2.pjrpid_activity;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_status_code;
import org.pjsip.pjsua2.pjsua2;
import org.pjsip.pjsua2.pjsua_buddy_status;
import org.pjsip.pjsua2.pjsua_call_media_status;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import Services.ScreenReceiver;
import Services.ShowOrCancelProgress;
import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.ExSSLSocketFactory;
import json.GroubDetails;
import json.JsonRequestSender;
import json.ListMember;
import json.ListUserGroupObject;
import json.Loginuserdetails;
import json.Queue;
import json.WebServiceInterface;
import receiver.ScheduleCallReceiver;
import receiver.ScheduleChatReceiver;
import xml.xmlcomposer;
import xml.xmlparser;

public class MainActivity extends AppCompatActivity implements Handler.Callback,
        MyAppObserver, WebServiceInterface, PhoneStateChangeNofication, ShowOrCancelProgress, MessageAccess {
    static MainActivity mainactivity;
    public static MyApp app = null;
    public static MyCall currentCall = null;
    public static MyAccount account = null;
    public static VideoPreview vidPreview;
    public static AccountConfig accCfg = null;
    String freqDate, query1 = null, percentage = "0";
    String profile, callerid, calledid;
    String call_strtime = null;
    String Hostname = null;
    private ArrayList<CallerDetailsBean> user_details;
    int alarmtask;
    ArrayList<TaskDetailsBean> taskDetailsBeen = new ArrayList<TaskDetailsBean>();
    TextView tab_tv, tab_text;
    ImageView tab_icon1;
    ArrayList<TaskDetailsBean> taskList_1, bean_list;
    public static ArrayList<MyCall> currentCallArrayList = new ArrayList<>();
    public static ArrayList<VideoWindow> totalvideoWindows = new ArrayList<>();
    VideoCallDataBase dataBase;

    public static HashMap<Integer, AudioMedia> audioMediaHashMap = new HashMap<>();
    public static Context mainContext;
    public static boolean isAudioCall = true;
    public static boolean medreceived = false;
    public static boolean isSignalidSame = false;
    private ListView buddyListView;
    Context context;
    String checker, taskStatus;
    // private SimpleAdapter buddyListAdapter;
    private BuddyArrayAdapter buddyArrayAdapter;
    //	public static ArrayList<Integer> positions = new ArrayList<>();
    private int buddyListSelectedIdx = -1;
    ArrayList<Map<String, String>> buddyList;
    public static String lastRegStatus = "";
    public static MediaPlayer player = null;
    private Handler handler;
    AppSharedpreferences appSharedpreferences;
    ArrayList<ContactBean> contactList;
    ArrayList<TaskDetailsBean> taskList, taskList_12;
    ArrayList<ListMember> listMembers_1;
    ArrayList<String> listMembers_2, listofProjectMembers;
    boolean isGrp_Percent = false;
    public static boolean isPresenceReregister = true;
    ProgressDialog progress;
    String Respond_Video, Respond_Files, Access_Forms, Respond_Audio, Video_Access, Admin_Access, Respond_DateChange, Respond_Location, Respond_ConfCall, Audio_Access, Chat_Access, Respond_Text, Respond_Private, Respond_Photo, Access_Reminder, Respond_Sketch, Respond_Task, Access_ScheduledCNF, Group_Task, ReassignTask, ChangeTaskName, TaskDescriptions, TemplateExistingTask, ApproveLeave, RemindMe, AddObserver, TaskPriority, Escalations;
    ////
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static String username = "";
    private boolean isFirstRegister = true;
    private boolean reregisterAlarm_started = false;
    String password = "";
    TabLayout tabLayout;
    String[] tab_name = {"Contacts", "Recents", "Settings", "Job Cards"};
    Integer[] tab_icon = {
            R.drawable.ic_contacts_100,
            R.drawable.recent2,
            R.drawable.ic_settings_2_128,
            R.drawable.ic_checklist_100
    };
    Integer[] tab_icon2 = {
            R.drawable.ic_contacts_1001,
            R.drawable.ic_checklist_1001,
            R.drawable.ic_chat_4_1282,
            R.drawable.ic_settings_2_1281
    };
    public static Handler handler1 = new Handler();
    Loginuserdetails loginuserdetails;

    public static MainActivity getIntance() {
        return mainactivity;
    }

    //Creating a broadcast receiver for gcm registration
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public static boolean nwstate = false;
    public static PowerManager.WakeLock mWakeLock;

    public String activityOnTop = "";
    public boolean openPinActivity = false;

    PhoneStateChangeListener phoneStateChangeListener = null;
    BroadcastReceiver mReceiver;
    public static int gsmCallState;
    Fragment fragment = null;
    public static ProgressDialog ui_progress;
    public static MessageEntryThread messageEntryThread;
    private Queue db_Queue;
    private Intent received_intent_value = null;

    @Override
    public void PhoneStateChangeDelegate(int state) {
        gsmCallState = state;
    }



    public class MSG_TYPE {
        public final static int INCOMING_CALL = 1;
        public final static int CALL_STATE = 2;
        public final static int REG_STATE = 3;
        public final static int BUDDY_STATE = 4;
        public final static int CALL_MEDIA_STATE = 5;
    }

    private HashMap<String, String> putData(String uri, String status) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("uri", uri);
        item.put("status", status);
        return item;
    }

    private void showCallActivity(MyCall call, String name) {
//        Intent intent = new Intent(this, CallActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        CallOpParam prm = new CallOpParam();
//        prm.setStatusCode(pjsip_status_code.PJSIP_SC_RINGING);
//        try {
//            Log.i("SipVideo", "answer before");
//            call.answer(prm);
//            Log.i("SipVideo", "answer after");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        if (Appreference.context_table.containsKey("audiorecorder")) {
            AudioRecorder audioRecorder = (AudioRecorder) Appreference.context_table.get("audiorecorder");
            if (audioRecorder != null)
                audioRecorder.stopRecordingWhileCall();
        }
       /* if (Appreference.context_table.containsKey("customvideocallscreen")) {
            CustomVideoCamera customVideoCamera = (CustomVideoCamera) Appreference.context_table.get("customvideocallscreen");
            if (customVideoCamera != null)
                customVideoCamera.stopRecording();
        }*/
        try {
            startCallRingTone();

            final Message msg = new Message();
            ringback_timer.sendMessageDelayed(msg, 30000);


            ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
            ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
            String activityOnTop = ar.topActivity.toString();
            KeyguardManager kgMgr =
                    (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            Log.i("incomingcall", "activityTop-->" + activityOnTop);
            if (!activityOnTop.contains("com.ase") || kgMgr.inKeyguardRestrictedInputMode()) {


                Intent intent = new Intent(this, IncomingCallAlert.class);
                PendingIntent pIntent = PendingIntent.getActivity(this, 12345, intent, 0);

                Notification n = new Notification.Builder(this)
                        .setContentTitle("High Message")
                        .setContentText("Incoming call:" + name)
                        .setTicker("Incoming call 1: " + name)
                        .setSmallIcon(R.drawable.logo)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true)
                        .build();

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                notificationManager.notify(12345, n);

            }

            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                            | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                    "incomingcall");

            mWakeLock.acquire();
            String callType = "Audio Call";
            try {

                CallInfo callInfo = call.getInfo();
                Log.i("SipVideo", "incomingcall differ is audio ---> callInfo.getRemAudioCount() ====  " + callInfo.getRemAudioCount() + "\n" +
                        "incomingcall differ is video---> callInfo.getRemVideoCount() ====  " + callInfo.getRemVideoCount());

                if (callInfo.getRemAudioCount() == 1 && callInfo.getRemVideoCount() == 1) {
                    callType = "Video Call";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(this, IncomingCallAlert.class);
            intent.putExtra("hostname", name);
            intent.putExtra("callType", callType);
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "showCallActivity Exception: " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "showCallActivity Exception: " + e.getMessage(), "WARN", null);
        }
    }

    Handler handler12 = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
//            afficher();
        }
    };

    private void init() {
//		String username ="amuthan1";
//		String password="amuthan2";
        try {
            String acc_id = "sip:" + username + "@" + getResources().getString(R.string.server_ip);
            String registrar = "sip:" + getResources().getString(R.string.server_ip);
//        String proxy = "sip:" + getResources().getString(R.string.server_ip);


            String proxy = "sip:" + getResources().getString(R.string.server_ip) + ";transport=tls;hide";
//            String proxy = "sip:" + getResources().getString(R.string.server_ip) +":8444"+ ";transport=tls;hide";


            Appreference.printLog("SipRegister", "Sip Registeration", "DEBUG", null);
            Appreference.printLog("SipRegister", "Sip Registeration username-->" + acc_id, "DEBUG", null);
            Appreference.printLog("SipRegister", "Sip Registeration resgister-->" + registrar, "DEBUG", null);
            Appreference.printLog("SipRegister", "Sip Registeration proxy-->" + proxy, "DEBUG", null);

            accCfg.setIdUri(acc_id);
            accCfg.getRegConfig().setRegistrarUri(registrar);
            AuthCredInfoVector creds = accCfg.getSipConfig().getAuthCreds();
            creds.clear();
            if (username.length() != 0) {
                creds.add(new AuthCredInfo("Digest", "*", username, 0, password));
            }
            StringVector proxies = accCfg.getSipConfig().getProxies();
            proxies.clear();
            if (proxy.length() != 0) {
                proxies.add(proxy);
            }

					/* Enable ICE */
            accCfg.getNatConfig().setIceEnabled(false);

					/* Finally */
            lastRegStatus = "";
            try {
                account.modify(accCfg);
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "init Exception: " + e.getMessage(), "WARN", null);
            }
            try {
                Message m = Message.obtain(handler, 10);
                m.sendToTarget();
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "init Exception: " + e.getMessage(), "WARN", null);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "init Exception: " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "init Exception: " + e.getMessage(), "WARN", null);
        }
    }

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        runnable.run();

        context = this;
        mainContext = this;
        Appreference.main_Activity_context = this;
        Appreference.mainContect = this;
        handler = new Handler(this);
        dataBase = VideoCallDataBase.getDB(mainContext);
        Appreference.context_table.put("mainactivity", mainContext);
        appSharedpreferences = AppSharedpreferences.getInstance(mainContext);
//        FirebaseCrash.report(new Exception("My first Android non-fatal error"));
        if (AppSharedpreferences.getInstance(mainContext) != null) {
            AppSharedpreferences.getInstance(mainContext).saveBoolean("login", true);
        }
        isPresenceReregister = true;
        isFirstRegister = true;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            try {
                Intent intent = new Intent();
                String packageName = mainContext.getPackageName();
//                PowerManager pm = (PowerManager) mainContext.getSystemService(Context.POWER_SERVICE);
               /* if (pm.isIgnoringBatteryOptimizations(packageName))
                    intent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                else {*/
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
//                }
                mainContext.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "onCreate Exception: " + e.getMessage(), "WARN", null);
            }
        }

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);

        phoneStateChangeListener = new PhoneStateChangeListener();
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        tm.listen(phoneStateChangeListener, PhoneStateListener.LISTEN_CALL_STATE);


        /*this is for location tracker */

//        startService(new Intent(getBaseContext(), LocationTrace.class));
//        stopService(new Intent(getBaseContext(), LocationTrace.class));

        contactList = new ArrayList<>();
        taskList = new ArrayList<>();
        if (isNetworkAvailable()) {
            Log.d("Presence", "Network Available is " + isNetworkAvailable());
            Appreference.networkState = true;
            nwstate = true;
        }

        /*JsonRequestSender jsonRequestParser = new JsonRequestSender();
        Appreference.jsonRequestSender = jsonRequestParser;*/
      /*  handler.postDelayed(new Runnable() {
            @Override
            public void run() {*/
        if (Appreference.context_table.containsKey("contactsfragment")) {
            ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
            contactsFragment.showOnlineState();
        }
           /* }
        },10);*/

        messageEntryThread = MessageEntryThread.getMessageEntryThread(true);
        db_Queue = new Queue();
        messageEntryThread.setQueue(db_Queue);
        messageEntryThread.setRunning(true);
//        messageEntryThread.start();

        Loginuserdetails loginuserdetails = null;
        try {
            Gson gson = new Gson();
            String json = appSharedpreferences.getString("loginuserdetails");
            loginuserdetails = gson.fromJson(json, Loginuserdetails.class);
            Appreference.loginuserdetails = loginuserdetails;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "onCreate Exception: " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "onCreate Exception: " + e.getMessage(), "WARN", null);
        }
//        Log.d("task1", "sharedPreference  " + loginuserdetails.getUsername());

//        Appreference.loginuserdetails = loginuserdetails;
        //Initializing our broadcast receiver
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {

            //When the broadcast received
            //We are sending the broadcast from GCMRegistrationIntentService

            @Override
            public void onReceive(Context context, Intent intent) {
                //If the broadcast has received with success
                //that means device is registered successfully
                try {
                    if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                        //Getting the registration token from the intent
                        String token = intent.getStringExtra("token");
                        //Displaying the token as toast
                        Toast.makeText(getApplicationContext(), "Registration token:" + token, Toast.LENGTH_LONG).show();

                        //if the intent is not with success then displaying error messages
                    } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                        Toast.makeText(getApplicationContext(), "GCM registration error!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MainActivity", "onCreate Exception: " + e.getMessage(), "WARN", null);
                }
            }
        };

        //Checking play service is available or not
        /*int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        //if play service is not available
        if (ConnectionResult.SUCCESS != resultCode) {
            //If play service is supported but not installed
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                //Displaying message that play service is not installed
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());

                //If play service is not supported
                //Displaying an error message
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }

            //If play service is available
        } else {
            //Starting intent to register device
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }*/

        Log.e("sip test", "inside oncreate--->");
        Appreference.sipRegister_flag = true;

        username = appSharedpreferences.getString("loginUserName");
        password = appSharedpreferences.getString("mPassword");
//        String is_Listallmytask = appSharedpreferences.getString("mListallmytask");
        String newText = username.replace('@', '_');
        username = newText;
        appSharedpreferences.saveString("loginUserUri", username);
//        LoadBackGroundWebService();
//        Log.i("Login", "Details mListallmytask " + getIntent().getExtras().getString("mListallmytask"));
        try {
            if (getIntent() != null) {
                if (getIntent().getExtras().getString("mListallmytask") != null && getIntent().getExtras().getString("mListallmytask").equalsIgnoreCase("success")) {
                    Log.d("Login", "Details first entry");
                    Log.i("Login", "Details Inside is_Listallmytask ");
                    listAllMyTaskwebservice();
                    //                showprogress("Loading Your Tasks");
                } else {
                    Log.i("Login", "Details Inside is_Listallmytask else ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "onCreate Exception: " + e.getMessage(), "WARN", null);
        }
//        Log.i("Login", "Details is_Listallmytask " + is_Listallmytask);

        if (app == null) {
            try {
                app = new MyApp();
                // Wait for GDB to init, for native debugging only
                if (false && (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }
                app.init(this, getFilesDir().getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "onCreate Exception: " + e.getMessage(), "WARN", null);
            }
        }

        if (app.accList.size() == 0) {
            accCfg = new AccountConfig();
//            accCfg.setIdUri("sip:localhost");
            String acc_id = "sip:" + username + "@" + getResources().getString(R.string.server_ip);
            accCfg.setIdUri(acc_id);
//            accCfg.getNatConfig().setIceEnabled(true);
            accCfg.getVideoConfig().setAutoTransmitOutgoing(true);
            accCfg.getVideoConfig().setAutoShowIncoming(true);
            accCfg.getRegConfig().setTimeoutSec(3600);
            accCfg.getRegConfig().setDelayBeforeRefreshSec(300);
            accCfg.getPresConfig().setPublishShutdownWaitMsec(5000);
            accCfg.getPresConfig().setPublishEnabled(true);
            accCfg.getNatConfig().setContactRewriteUse(0);
            accCfg.getNatConfig().setViaRewriteUse(1);
            accCfg.getNatConfig().setContactRewriteMethod(2);
            accCfg.getNatConfig().setIceEnabled(false);
//			accCfg.getVideoConfig().setAutoTransmitOutgoing(false);
//			accCfg.getVideoConfig().setAutoShowIncoming(false);
            /*
             Enable SRTP
              accCfg.getMediaConfig().setSrtpUse(pjmedia_srtp_use.PJMEDIA_SRTP_MANDATORY);
              accCfg.getMediaConfig().setSrtpSecureSignaling(1);
             */

            accCfg.getMediaConfig().setSrtpUse(pjmedia_srtp_use.PJMEDIA_SRTP_OPTIONAL);
            accCfg.getMediaConfig().setSrtpSecureSignaling(0);
            account = app.addAcc(accCfg);
        } else {
            account = app.accList.get(0);
            accCfg = account.cfg;
        }

        buddyList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < account.buddyList.size(); i++) {
            buddyList.add(putData(account.buddyList.get(i).cfg.getUri(),
                    account.buddyList.get(i).getStatusText()));
        }

        String[] from = {"uri", "status"};
        int[] to = {android.R.id.text1, android.R.id.text2};
        // buddyListAdapter = new SimpleAdapter(this, buddyList_adapter,
        // android.R.layout.simple_list_item_2, from, to);

        buddyArrayAdapter = new BuddyArrayAdapter(this, buddyList);
        buddyListView = (ListView) findViewById(R.id.listViewBuddy);

        // buddyListView.setAdapter(buddyListAdapter);
        buddyListView.setAdapter(buddyArrayAdapter);

        buddyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    final View view, int position, long id) {
                view.setSelected(true);
                buddyListSelectedIdx = position;
            }
        });
        init();
//        try {
//            for (int i = 0; i < 5; i++) {
//                tabLayout.getTabAt(i).setIcon(tab_Icon[i]);
//            }
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }


        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        Log.i("item", "count" + tabLayout.getTabCount());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(4);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
         /*   tabLayout.getTabAt(i).setIcon(tab_icon[i]);
            tabLayout.getTabAt(i).setText(tab_name[i]);*/
            Log.i("task", "checked ->" + i);
            tabLayout.getTabAt(i).setCustomView(setTabView(i));
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i("SipVideo", "tab.getPosition() : " + tab.getPosition());
//                tabLayout.getTabAt(tab.getPosition()).setCustomView(setTabView(tab.getPosition()));
//                mViewPager.setAdapter(mSectionsPagerAdapter);
                mViewPager.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // working code commented this code placed on notifyregstate method
       /* AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, SipRegisterBroadastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 2145,
                intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis(), 1 * 60000, pendingIntent);*/

        if (isNetworkAvailable()) {
            LoadBackGroundWebService();
        }

        if (AppSharedpreferences.getInstance(mainContext).getString("fcmTokenId") == null) {
            try {
                Log.d("fcm", "tokenId is   = " + "in side main class if");
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "onCreate Exception " + e.getMessage(), "WARN", null);
            }
        } else if (AppSharedpreferences.getInstance(mainContext).getString("fcmTokenId") != null || !AppSharedpreferences.getInstance(mainContext).getString("fcmTokenId").equalsIgnoreCase("") && !AppSharedpreferences.getInstance(mainContext).getBoolean("login")) {

            Log.d("fcm", "tokenId is   = " + "in side main class else");
            JSONObject jsonObject = null;
            if (isNetworkAvailable()) {
                try {

                    Appreference.isFCMRegister = false;

                    AppSharedpreferences.getInstance(mainContext).saveBoolean("login", true);
                    jsonObject = new JSONObject();

                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("id", String.valueOf(appSharedpreferences.getString("loginId")));

                    jsonObject.put("user", jsonObject1);
                    jsonObject.put("imeiNo", Appreference.getIMEINumber(getApplicationContext()));
                    jsonObject.put("tokenId", AppSharedpreferences.getInstance(mainContext).getString("fcmTokenId"));
                    jsonObject.put("osType", "Android");
                    jsonObject.put("osVersion", Appreference.getRELEASE(getApplicationContext()));
                    jsonObject.put("deviceModel", Appreference.getMANUFACTURER(getApplicationContext()));

                    Log.d("fcm", "tokenId is  imeiNo  = " + Appreference.getIMEINumber(getApplicationContext()));
                    Log.d("fcm", "tokenId is  tokenId = " + AppSharedpreferences.getInstance(mainContext).getString("fcmTokenId"));
                    Log.d("fcm", "tokenId is osType  = " + Appreference.getMODEL(getApplicationContext()));
                    Log.d("fcm", "tokenId is   = " + "osVersion  " + Appreference.getRELEASE(getApplicationContext()));
                    Log.d("fcm", "tokenId is   = " + "deviceModel   " + Appreference.getMANUFACTURER(getApplicationContext()));
                    Appreference.jsonRequestSender.userDeviceRegistration(EnumJsonWebservicename.userDeviceRegistration, jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Appreference.printLog("MainActivity", "onCreate Exception " + e.getMessage(), "WARN", null);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MainActivity", "onCreate Exception " + e.getMessage(), "WARN", null);
                }
            }
        }
        received_intent_value = getIntent();
       /* if (received_intent_value != null) {
            if (received_intent_value.getExtras().getString("from") != null && received_intent_value.getExtras().getString("from").equalsIgnoreCase("scheduler")) {
//                scheduleCallAlert(intent);
            }
        }*/

//        String hai = "<?xml version=\"1.0\"?><NotificationAlertinfo><NotificationDetails datetime=\"null\" signalid=\"13\" alerttype=\"Group change Notification\"> <groupchange groupid=\"29\" subtype=\"Member Added\" users=\"gp3_gp.com\"/></NotificationDetails></NotificationAlertinfo>";
//        notifyChatReceived("amuthan_hm.com", "s1_g.com", "text", hai);
    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            Log.e("sip test", "inside onresume--->");
            Appreference.sipRegister_flag = false;

            Log.w("MainActivity", "onResume");
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
            if (openPinActivity) {
                openPinActivity = false;
                reRegister_onAppResume();
            }
            if (!reregisterAlarm_started) {
                startAlarmManager();
            }

            if (phoneStateChangeListener != null) {
                phoneStateChangeListener.SetListener(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "onResume Exception " + e.getMessage(), "WARN", null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            Log.e("siptest", "inside onpause--->");
            Appreference.sipRegister_flag = false;
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "onPause Exception " + e.getMessage(), "WARN", null);
        }
    }

    @Override
    protected void onStop() {
        try {
            super.onStop();
            Log.e("sip test", "inside onstop--->");
            Appreference.sipRegister_flag = false;
            isApplicationBroughtToBackground();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "onStop Exception " + e.getMessage(), "WARN", null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar
        // if it is present.
        try {
            getMenuInflater().inflate(R.menu.main, menu);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "onCreateOptionsMenu Exception: " + e.getMessage(), "WARN", null);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.action_acc_config:
                    dlgAccountSetting();
                    break;

                case R.id.action_quit:
                    try {
                        Message m = Message.obtain(handler, 0);
                        m.sendToTarget();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "onOptionsItemSelected Exception: " + e.getMessage(), "WARN", null);
        }
        return true;
    }

    public class BuddyArrayAdapter extends ArrayAdapter<Map<String, String>> {

        ArrayList<Map<String, String>> arrayBuddyList;
        LayoutInflater inflater = null;
        Context adapContext;

        public BuddyArrayAdapter(Context context,
                                 ArrayList<Map<String, String>> buddyList1) {
            super(context, R.layout.buddy_adapter_row, buddyList1);
            // TODO Auto-generated constructor stub
            arrayBuddyList = buddyList1;
            adapContext = context;
        }

        public View getView(int pos, View conView, ViewGroup group) {

            try {
                if (conView == null) {
                    inflater = (LayoutInflater) adapContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    conView = inflater.inflate(R.layout.buddy_adapter_row, null,
                            false);
                }
                Map<String, String> map = arrayBuddyList.get(pos);
                String name = map.get("uri");
                name = name.substring(4);
                name = name.split("@")[0];
                String status = map.get("status");

                TextView userName = (TextView) conView.findViewById(R.id.username);
                userName.setText(name);
                TextView userStatus = (TextView) conView.findViewById(R.id.status);
                userStatus.setText(status);
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "getView Exception: " + e.getMessage(), "WARN", null);
            }
            return conView;
        }
    }

    public void updateDateRequestIcon(TaskDetailsBean taskDetailsBean) {
        try {
            if (taskDetailsBean.getMimeType() != null && taskDetailsBean.getMimeType().equalsIgnoreCase("date")) {
                bean_list = new ArrayList<>();
                String query_5 = "select * from taskDetailsInfo where (fromUserName='" + Appreference.loginuserdetails.getUsername() + "' or toUserName='" + Appreference.loginuserdetails.getUsername() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskNo='" + taskDetailsBean.getTaskNo() + "' and mimeType='date'";
                bean_list = VideoCallDataBase.getDB(context).getTaskHistory(query_5);
                Log.i("task", "dates" + bean_list.size());
                if (bean_list.size() > 0) {
                    for (int i = 0; i < bean_list.size(); i++) {
                        TaskDetailsBean taskDetailsBean_1 = bean_list.get(i);
                        Log.i("task1234", "RequestStatus --->" + taskDetailsBean_1.getRequestStatus());
                        //                            taskDetailsBeanArrayList1.get(i).setMsg_status(9);
                        for (TaskDetailsBean taskDetailsBean1 : bean_list) {
                            for (TaskDetailsBean taskDetailsBean2 : taskList) {
                                if (taskDetailsBean1.getSignalid().equalsIgnoreCase(taskDetailsBean2.getSignalid())) {
                                    Log.i("task1234", "taskDetailsBean_1.getRequestStatus() " + taskDetailsBean_1.getRequestStatus());
                                    taskDetailsBean2.setMsg_status(9);
                                }
                            }
                        }
                        VideoCallDataBase.getDB(context).taskMsg_StatusUpdate(taskDetailsBean_1.getSignalid());
                    }
                }
                //                }
                //                VideoCallDataBase.getDB(context).taskMsg_StatusUpdate("5", taskDetailsBean1.getTaskNo());
                taskDetailsBean.setMsg_status(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "updateDateRequestIcon Exception: " + e.getMessage(), "WARN", null);
        }

    }

    public void updateLeaveRequest_icon(TaskDetailsBean taskDetailsBean) {
        try {
            String query_5 = "";
            if (taskDetailsBean.getMimeType() != null && taskDetailsBean.getMimeType().equalsIgnoreCase("leaveRequest") && taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                bean_list = new ArrayList<>();
                if (taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
                    String group_id = taskDetailsBean.getToUserId();
                    query_5 = "select * from taskDetailsInfo where (fromUserId='" + group_id + "' or toUserId='" + group_id + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + taskDetailsBean.getTaskId() + "' and mimeType='leaveRequest'";
                } else {
                    query_5 = "select * from taskDetailsInfo where (fromUserName='" + Appreference.loginuserdetails.getUsername() + "' or toUserName='" + Appreference.loginuserdetails.getUsername() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskNo='" + taskDetailsBean.getTaskNo() + "' and mimeType='leaveRequest'";
                }
                //            String query_5 = "select * from taskDetailsInfo where (fromUserName='" + Appreference.loginuserdetails.getUsername() + "' or toUserName='" + Appreference.loginuserdetails.getUsername() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskNo='" + taskDetailsBean.getTaskNo() + "' and mimeType='leaveRequest'";
                bean_list = VideoCallDataBase.getDB(context).getTaskHistory(query_5);
                Log.i("task", "dates" + bean_list.size());
                if (bean_list.size() > 0) {
                    for (int i = 0; i < bean_list.size(); i++) {
                        TaskDetailsBean taskDetailsBean_1 = bean_list.get(i);
                        Log.i("task1234", "RequestStatus --->" + taskDetailsBean_1.getRequestStatus());
                        //                            taskDetailsBeanArrayList1.get(i).setMsg_status(9);
                        for (TaskDetailsBean taskDetailsBean1 : bean_list) {
                            for (TaskDetailsBean taskDetailsBean2 : taskList) {
                                if (taskDetailsBean1.getSignalid().equalsIgnoreCase(taskDetailsBean2.getSignalid())) {
                                    Log.i("task1234", "taskDetailsBean_1.getRequestStatus() ** " + taskDetailsBean_1.getRequestStatus());
                                    taskDetailsBean2.setMsg_status(9);
                                }
                            }
                        }
                        VideoCallDataBase.getDB(context).leaveMsg_Status(taskDetailsBean_1.getSignalid());
                    }
                }
                //                }
                //                VideoCallDataBase.getDB(context).leaveMsg_Status("5", taskDetailsBean1.getTaskNo());
                taskDetailsBean.setMsg_status(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "updateLeaveRequest_icon Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void updateOverdueMsg(TaskDetailsBean taskDetailsBean) {
        try {
            String query_5 = "";
            if (taskDetailsBean.getMimeType() != null && taskDetailsBean.getMimeType().equalsIgnoreCase("leaveRequest") && taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                bean_list = new ArrayList<>();
                if (taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
                    String group_id = taskDetailsBean.getToUserId();
                    query_5 = "select * from taskDetailsInfo where (fromUserId='" + group_id + "' or toUserId='" + group_id + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + taskDetailsBean.getTaskId() + "' and mimeType='overdue'";
                } else {
                    query_5 = "select * from taskDetailsInfo where (fromUserName='" + Appreference.loginuserdetails.getUsername() + "' or toUserName='" + Appreference.loginuserdetails.getUsername() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskNo='" + taskDetailsBean.getTaskNo() + "' and mimeType='overdue'";
                }
                //            String query_5 = "select * from taskDetailsInfo where (fromUserName='" + Appreference.loginuserdetails.getUsername() + "' or toUserName='" + Appreference.loginuserdetails.getUsername() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskNo='" + taskDetailsBean.getTaskNo() + "' and mimeType='leaveRequest'";
                bean_list = VideoCallDataBase.getDB(context).getTaskHistory(query_5);
                Log.i("task", "dates" + bean_list.size());
                if (bean_list.size() > 0) {
                    for (int i = 0; i < bean_list.size(); i++) {
                        TaskDetailsBean taskDetailsBean_1 = bean_list.get(i);
                        Log.i("task1234", "RequestStatus --->" + taskDetailsBean_1.getRequestStatus());
                        //                            taskDetailsBeanArrayList1.get(i).setMsg_status(9);
                        for (TaskDetailsBean taskDetailsBean1 : bean_list) {
                            for (TaskDetailsBean taskDetailsBean2 : taskList) {
                                if (taskDetailsBean1.getSignalid().equalsIgnoreCase(taskDetailsBean2.getSignalid())) {
                                    Log.i("task1234", "taskDetailsBean_1.getRequestStatus() ** " + taskDetailsBean_1.getRequestStatus());
                                    taskDetailsBean2.setMsg_status(9);
                                }
                            }
                        }
                        VideoCallDataBase.getDB(context).OverDueMsg_StatusUpdate(taskDetailsBean_1.getSignalid());
                    }
                }
                //                }
                //                VideoCallDataBase.getDB(context).leaveMsg_Status("5", taskDetailsBean1.getTaskNo());
                taskDetailsBean.setMsg_status(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "updateLeaveRequest_icon Exception: " + e.getMessage(), "WARN", null);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == 1000) {
                if (resultCode == Activity.RESULT_OK) {
                    mViewPager.setAdapter(mSectionsPagerAdapter);
                    mViewPager.setCurrentItem(0);
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    //Write your code if there's no result
                }
            }
            if (requestCode == 202) {
                Log.d("receive", "done");

                Fragment fragment1 = new TasksFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container, fragment1);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "onActivityResult Exception: " + e.getMessage(), "WARN", null);
        }
    }


    @Override
    public boolean handleMessage(Message m) {
        Log.i("SipVideo123", "came to MainAct handleMessage");

        try {
            if (m.what == 0) {

                app.deinit();
                finish();
                Runtime.getRuntime().gc();
                android.os.Process.killProcess(android.os.Process.myPid());

            } else if (m.what == MSG_TYPE.CALL_STATE) {
                Log.i("SipVideo", "m.what == MSG_TYPE.CALL_STATE in MainAvtivity");
                ArrayList<Object> objects = (ArrayList<Object>) m.obj;
                //            CallInfo ci = (CallInfo) m.obj;
                CallInfo ci = (CallInfo) objects.get(1);
                Log.d("VideoCall", "Inside the m.what == MSG_TYPE.CALL_STATE method ==  " + CallActivity.handler_);

                /* Forward the message to CallActivity */
                try {
                    if (CallActivity.handler_ != null) {
                        Message m2 = Message.obtain(CallActivity.handler_,
                                MSG_TYPE.CALL_STATE, objects);
                        m2.sendToTarget();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (ci != null && ci.getState() ==
                        pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
                    if (Appreference.context_table.containsKey("incomingcallalert")) {
                        MainActivity.stopRingTone();
                        IncomingCallAlert incomingCallAlert = (IncomingCallAlert) Appreference.context_table.get("incomingcallalert");
                        Log.i("MainCall", " 767" + Hostname);
                        insertDb("MissedCall", Hostname);
                        Log.i("MainCall", "after db 767" + Hostname);
                        incomingCallAlert.finish();
                    }
                }

            } else if (m.what == MSG_TYPE.CALL_MEDIA_STATE) {
                Log.d("VideoCall", "Inside the m.what == MSG_TYPE.CALL_MEDIA_STATE method ==  " + CallActivity.handler_);
                Log.i("SipVideo123", "came to MainAct MSG_TYPE.CALL_MEDIA_STATE");

                /* Forward the message to CallActivity */
                try {
                    if (CallActivity.handler_ != null) {
                        Message m2 = Message.obtain(CallActivity.handler_,
                                MSG_TYPE.CALL_MEDIA_STATE, null);
                        m2.sendToTarget();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (m.what == MSG_TYPE.BUDDY_STATE) {

                MyBuddy buddy = (MyBuddy) m.obj;
                if (buddy != null) {

                    try {
                        buddy.getInfo();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MainActivity", "handleMessage Exception: " + e.getMessage(), "WARN", null);
                        return false;
                    }
                    int idx = account.buddyList.indexOf(buddy);

                    //Log.i("buddystate", "handleMessage buddy state--->" + buddy.getStatusText());
                    try {
                        //                    if (ContactsFragment.newInstance(3, this).isAdded()) {
                        ContactsFragment.newInstance(3, this).notifybuddystatus(buddy);
                        //                    }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MainActivity", "handleMessage Exception: " + e.getMessage(), "WARN", null);
                    }
                /*
                 * Update buddy status text, if buddy is valid and the buddy lists
                 * in account and UI are sync-ed.
                 */
                    if (idx >= 0 && account.buddyList.size() == buddyList.size()) {
                        buddyList.get(idx).put("status", buddy.getStatusText());
                        // buddyListAdapter.notifyDataSetChanged();
                        buddyArrayAdapter.notifyDataSetChanged();
                        // TODO: selection color/mark is gone after this,
                        // dont know how to return it back.
                        // buddyListView.setSelection(buddyListSelectedIdx);
                        // buddyListView.performItemClick(buddyListView,
                        // buddyListSelectedIdx,
                        // buddyListView.
                        // getItemIdAtPosition(buddyListSelectedIdx));
                        Log.i("SipVideo", "m.what == MSG_TYPE.BUDDY_STATE in MainAvtivity");
                    /* Return back Call activity */
                        //                    notifyCallState(currentCall);
                    }
                }

            } else if (m.what == MSG_TYPE.REG_STATE) {

                String msg_str = (String) m.obj;
                lastRegStatus = msg_str;
                SettingsFragment.newInstance(3, mainContext).notifystatus(lastRegStatus);

            } else if (m.what == MSG_TYPE.INCOMING_CALL) {
                Log.i("SipVideo", "m.what == MSG_TYPE.INCOMING_CALL");
                /* Incoming call */
                try {
                    final MyCall call = (MyCall) m.obj;

                    CallInfo callInfo = null;
                    try {
                        callInfo = call.getInfo();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MainActivity", "handleMessage Exception: " + e.getMessage(), "WARN", null);
                        return false;
                    }

                    //                CallOpParam prm = new CallOpParam();

                /* Only one call at anytime */
                    //                currentCall = null;
                    Log.i("changehost", " INCOMING_CALL :  call_pause_received : " + Appreference.call_pause_received + " currentCall : " + currentCall + " callInfo :" + callInfo);
                    if ((currentCall != null && !Appreference.call_pause_received) || gsmCallState != TelephonyManager.CALL_STATE_IDLE) {
                        Log.i("SipVideo", "inside hang up mainAct");
                        CallOpParam prm = new CallOpParam();
                        prm.setStatusCode(pjsip_status_code.PJSIP_SC_BUSY_HERE);
                        try {
                            call.hangup(prm);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("MainActivity", "handleMessage Exception: " + e.getMessage(), "WARN", null);
                        }
                        // TODO: set status code
                        //                    CallInfo callInfo = call.getInfo();
                        String name = callInfo.getRemoteUri();
                        Log.i("changehost", "changehost incomingcall hosturi--->" + name);

                        Log.i("SipVideo", " delete: b5");
                        call.delete();
                        return true;
                    }

                    if (Appreference.call_pause_received) {
                        //                    CallInfo callInfo = call.getInfo();
                        String name = callInfo.getRemoteUri();
                        Log.i("changehost", "name : " + name + " Appreference.new_host : " + Appreference.new_host);
                        if (name.equalsIgnoreCase(Appreference.new_host)) {
                            answerNewHostCall(call);
                        }
                    }
                    Log.i("SipVideo", "pjsip_status_code.PJSIP_SC_RINGING");
                /* Answer with ringing */
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            CallOpParam prm = new CallOpParam();
                            prm.setStatusCode(pjsip_status_code.PJSIP_SC_RINGING);
                            try {
                                Log.i("SipVideo", "answer before");
                                call.answer(prm);
                                Log.i("SipVideo", "answer after");
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("MainActivity", "handleMessage Exception: " + e.getMessage(), "WARN", null);
                            }
                        }
                    });
                    currentCallArrayList.clear();
                    currentCallArrayList.add(call);
                    currentCall = call;
                    Log.i("SipVideo", "MainActivity handleMessage MainActivity.currentCall=call");
                    //                CallInfo callInfo = call.getInfo();
                    String name = callInfo.getRemoteUri();
                    calledid = callInfo.getRemoteUri();
                    Log.i("SipVideo", "incomingcall hosturi--->" + name);
                    if (name.contains("@")) {
                        name = name.substring(5);
                        name = name.split("@")[0];
                    }
                    Hostname = name;
                    Log.i("SipVideo", "incomingcall hosturi---> removed @ " + name);
                   /* String userName = VideoCallDataBase.getDB(mainContext).getName(name);
                    Log.i("SipVideo", "incomingcall hosturi---> userName first and last name :  " + userName);*/

                    showCallActivity(call, name);

                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MainActivity", "handleMessage Exception: " + e.getMessage(), "WARN", null);
                }
            } else if (m.what == 10) {
                app.saveDetails();
            } else {
                /* Message not handled */
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "handleMessage Exception: " + e.getMessage(), "WARN", null);
        }
        return true;
    }


    private void answerNewHostCall(MyCall call) {
        CallOpParam prm = new CallOpParam();
        prm.setStatusCode(pjsip_status_code.PJSIP_SC_RINGING);
        try {
            call.answer(prm);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "answerNewHostCall Exception: " + e.getMessage(), "WARN", null);
        }
        currentCallArrayList.clear();
        currentCallArrayList.add(call);
        currentCall = call;

        prm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
        try {
            MainActivity.currentCall.answer(prm);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "answerNewHostCall Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void groupChangeDeleteNotification(String group_id, String group_name, String deleted_username) {
        if (deleted_username.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
            try {
                Log.d("group", "loginuser delete notification ");
                ArrayList<String> grouplist = VideoCallDataBase.getDB(context).selectGroupmembers("select * from groupmember where groupid= '" + group_id + "'", "username");
                ArrayList<String> grouplist1 = VideoCallDataBase.getDB(context).selectGroupmembers("select * from groupmember where groupid!= '" + group_id + "'", "username");
                Log.i("group", "newlist size grouplist " + grouplist);
                Log.i("group", "newlist size grouplist1 " + grouplist1);
                ArrayList<String> newgroup = new ArrayList<>();
                if (grouplist.size() > 0 && grouplist1.size() > 0) {
                    for (int i = 0; i < grouplist.size(); i++) {
                        if (!grouplist1.contains(grouplist.get(i)))
                            newgroup.add(grouplist.get(i));
                    }
                }

                ArrayList<String> grouplist2 = VideoCallDataBase.getDB(context).selectGroupmembers("select * from contact", "username");
                Log.i("group", "newlist size is " + newgroup.size() + "new members  " + newgroup);
                Log.i("group", "newlist size is grouplist2 " + grouplist2);
                if (newgroup.size() > 0) {
                    for (int i = 0; i < newgroup.size(); i++) {
                        for (int j = 0; j < grouplist2.size(); j++) {
                            if (newgroup.get(i).equals(grouplist2.get(j))) {
                                grouplist2.remove(newgroup.get(i));
                                VideoCallDataBase.getDB(context).deleteContact(newgroup.get(i));
                                Log.i("group", "contact " + newgroup.get(i) + " was deleted sucessfully from contact table");
                            }
                        }
                    }
                }
                VideoCallDataBase.getDB(context).deleteGroupMembers(group_name);
                Log.i("group", group_name + " group members are deleted from groupmember table");
                VideoCallDataBase.getDB(context).deleteGroup(group_id);
                Log.i("group", "group name is " + group_name + " deleted from group1 table");
                if (Appreference.context_table.containsKey("contactsfragment")) {
                    Log.i("contacts", "Response Method 1");
                    ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
                    contactsFragment.referesh();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "groupChangeDeleteNotification Exception: " + e.getMessage(), "WARN", null);
            }
        } else {
            try {
                Log.d("group", "Not loginuser delect contact * ");
                ArrayList<String> grouplist1 = VideoCallDataBase.getDB(context).selectGroupmembers("select * from groupmember where groupid != '" + group_id + "'", "username");
                Log.i("group", "grouplist1 size is " + grouplist1.size());
                if (grouplist1.size() > 0) {
                    if (!grouplist1.contains(deleted_username)) {
                        VideoCallDataBase.getDB(context).deleteContact(deleted_username);
                        Log.i("group", "deleted contact name " + deleted_username + " was deleted sucessfully from contact table");
                    }
                } else {
                    VideoCallDataBase.getDB(context).deleteContact(deleted_username);
                    Log.i("group", "grouplist1 delete contact ");
                }
                VideoCallDataBase.getDB(context).deleteSingleGroupMember(deleted_username, group_id);
                Log.i("group", group_id + " group member " + deleted_username + " deleted from groupmember table");
                if (Appreference.context_table.containsKey("contactsfragment")) {
                    Log.i("contacts", "Response Method 1");
                    ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
                    contactsFragment.referesh();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "groupChangeDeleteNotification Exception: " + e.getMessage(), "WARN", null);
            }
        }
    }

    public void groupChangeAddNotification(String group_id, String group_name, String added_username) {

        GroubDetails groubDetails = new GroubDetails();
        Boolean isuser_exist = false;
        ArrayList<ListMember> u2 = groubDetails.getListMember();
        ArrayList<String> contactId_List = VideoCallDataBase.getDB(mainContext).selectContactList("select * from contact");
        Log.i("groupChangeAdd", "MA_Member!!  ");
        Log.i("groupChangeAdd", "MA_Member!!##  " + contactId_List);
        Log.i("groupChangeAdd", "MA_Member!!##  " + added_username);
//        if (contactId_List.contains(added_username)) {
        for (String str : contactId_List) {
            if (str.equalsIgnoreCase(added_username)) {
                try {
                    isuser_exist = true;
                    Log.i("groupChangeAdd", "MA_Member  ");
                    ListMember listMember1 = VideoCallDataBase.getDB(context).getnewAddedList(added_username);
                    Log.i("groupChangeAdd", "MA_Member--->1  " + listMember1.getEmail());
                    Log.i("groupChangeAdd", "MA_Member--->2  " + listMember1.getUserid());
                    Log.i("groupChangeAdd", "MA_Member--->3  " + listMember1.getUsername());
                    Log.i("groupChangeAdd", "MA_Member--->4  " + listMember1.getLoginuser());
                    Log.i("groupChangeAdd", "MA_Member--->5  " + listMember1.getUserType());
                    ListMember lM2 = new ListMember();
                    lM2.setEmail(listMember1.getEmail());
                    lM2.setId(listMember1.getUserid());
                    lM2.setUsername(listMember1.getUsername());
                    lM2.setFirstName(listMember1.getFirstName());
                    lM2.setLastName(listMember1.getFirstName());
                    lM2.setCode(listMember1.getCode());
                    lM2.setTitle(listMember1.getTitle());
                    lM2.setGender(listMember1.getGender());
                    lM2.setProfileImage(listMember1.getProfileImage());
                    lM2.setPersonalInfo(listMember1.getPersonalInfo());
                    lM2.setLoginuser(listMember1.getLoginuser());
                    lM2.setLoginStatus(listMember1.getLoginStatus());
//                lM2.setUserStatus(listMember1.getUserStatus());
//                lM2.setJobCategory1(listMember1.getJobCategory1());
//                lM2.setJobCategory2(listMember1.getJobCategory2());
//                lM2.setJobCategory3(listMember1.getJobCategory3());
//                lM2.setJobCategory4(listMember1.getJobCategory4());
//                lM2.setTextProfile(listMember1.getTextProfile());
//                lM2.setVideoProfile(listMember1.getVideoProfile());
                    lM2.setUserType(listMember1.getUserType());
                    lM2.setProfession(listMember1.getProfession());
                    lM2.setSpecialization(listMember1.getSpecialization());
                    lM2.setOrganization(listMember1.getOrganization());
                    Log.i("groupChangeAdd", "lM2_getId  " + lM2.getId());
                    Log.i("groupChangeAdd", "getLoginStatus  " + lM2.getLoginStatus());
                    VideoCallDataBase.getDB(mainContext).insertGroupMember_history(lM2, listMember1.getLoginuser(), Integer.parseInt(group_id));
                    Log.i("groupChangeAdd", "MA_Member inserted  ");

                    //    ArrayList<String> grouplist2 = VideoCallDataBase.getDB(context).selectGroupmembers("select * from contact", "username");
//                if (newgroup.size() > 0) {
//                    for (int i = 0; i < newgroup.size(); i++) {
//                        for (int j = 0; j < grouplist2.size(); j++) {
//                            if (newgroup.get(i).equals(grouplist2.get(j))) {
//                                grouplist2.remove(newgroup.get(i));
//                                VideoCallDataBase.getDB(context).deleteContact(newgroup.get(i));
//                                Log.i("group", "contact " + newgroup.get(i) + " was deleted sucessfully from contact table");
//                            }
//                        }
//                    }
//                }

                    if (Appreference.context_table.containsKey("contactsfragment")) {
                        Log.i("contacts", "Responce for member added ");
                        ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
                        contactsFragment.referesh();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MainActivity", "groupChangeAddNotification Exception: " + e.getMessage(), "WARN", null);
                }
            }
        }
        if (!isuser_exist) {
            Log.d("group", "NewGroup added ");
            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("groupId", group_id));
                Log.i("group", "nameValuePairs " + nameValuePairs);
                Appreference.jsonRequestSender.getGroup(EnumJsonWebservicename.getGroup, nameValuePairs, this);

                if (Appreference.context_table.containsKey("contactsfragment")) {
                    Log.i("contacts", "Responce for memberaddedgroup ");
                    ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
                    contactsFragment.referesh();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "groupChangeAddNotification Exception: " + e.getMessage(), "WARN", null);
            }
        }
    }

    public void groupChangeAddGroupNotification(String group_id) {
        try {
            Log.d("group", "NewGroup addedGroup ");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("groupId", group_id));
            Log.i("group", "nameValuePairs " + nameValuePairs);
            Appreference.jsonRequestSender.getGroup(EnumJsonWebservicename.getGroup, nameValuePairs, this);

            if (Appreference.context_table.containsKey("contactsfragment")) {
                Log.i("contacts", "Responce for memberaddedgroup ");
                ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
                contactsFragment.referesh();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "groupChangeAddGroupNotification Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void groupNameChangeNotification(String group_id, String newGroup_name) {
        try {
            Log.d("group", "Group Name Changed");
            ArrayList<String> groupname = VideoCallDataBase.getDB(getApplicationContext()).selectGroupName("select groupname from group1 where groupid= '" + group_id + "'"); // Display's single record(groupName) from "group1" table
            String oldGroupName = groupname.get(0); // Single record only
            VideoCallDataBase.getDB(getApplicationContext()).updateGroupNameInmembers(group_id, newGroup_name);  // Update newgroupname in "groupmember" table
            Log.i("group", "oldGroupName " + oldGroupName + " renamed to " + newGroup_name + " in groupmember table");
            VideoCallDataBase.getDB(getApplicationContext()).updateGroupName(group_id, newGroup_name);  // Update newgroupname in "group1" table
            Log.i("group", "groupId " + group_id + " renamed to " + newGroup_name + " in group1 table");

            if (Appreference.context_table.containsKey("contactsfragment")) {
                Log.i("contacts", "Response Method 1");
                ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
                contactsFragment.referesh();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "groupNameChangeNotification Exception: " + e.getMessage(), "WARN", null);
        }
    }

    private void dlgAccountSetting() {
        try {
            LayoutInflater li = LayoutInflater.from(this);
            View view = li.inflate(R.layout.dlg_account_config, null);

            if (lastRegStatus.length() != 0) {
                TextView tvInfo = (TextView) view.findViewById(R.id.textViewInfo);
                tvInfo.setText("Last status: " + lastRegStatus);
            }

            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setView(view);
            adb.setTitle("Account Settings");

            final EditText etId = (EditText) view.findViewById(R.id.editTextId);
            final EditText etReg = (EditText) view.findViewById(R.id.editTextRegistrar);
            final EditText etProxy = (EditText) view.findViewById(R.id.editTextProxy);
            final EditText etUser = (EditText) view.findViewById(R.id.editTextUsername);
            final EditText etPass = (EditText) view.findViewById(R.id.editTextPassword);

            etId.setText(accCfg.getIdUri());
            etReg.setText(accCfg.getRegConfig().getRegistrarUri());
            StringVector proxies = accCfg.getSipConfig().getProxies();
            if (proxies.size() > 0)
                etProxy.setText(proxies.get(0));
            else
                etProxy.setText("");
            AuthCredInfoVector creds = accCfg.getSipConfig().getAuthCreds();
            if (creds.size() > 0) {
                etUser.setText(creds.get(0).getUsername());
                etPass.setText(creds.get(0).getData());
            } else {
                etUser.setText("");
                etPass.setText("");
            }

            if (etUser.getText().toString().trim().equals("")) {
                // etId.setText("sip:2000@172.16.1.84");
                // etReg.setText("sip:172.16.1.84");
                // etUser.setText("2000");
                // etPass.setText("pass123");

                // etId.setText("sip:amuthan1@66.109.17.205");
                // etReg.setText("sip:66.109.17.205");
                // etProxy.setText("sip:66.109.17.205");
                // etUser.setText("amuthan1");
                // etPass.setText("password");
            }

            etId.setVisibility(View.GONE);
            etReg.setVisibility(View.GONE);
            etProxy.setVisibility(View.GONE);

            adb.setCancelable(false);
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // String acc_id = etId.getText().toString();
                    // String registrar = etReg.getText().toString();
                    // String proxy = etProxy.getText().toString();
                    String username = etUser.getText().toString();
                    String password = etPass.getText().toString();
                    if (username.length() > 0) {
                        String acc_id = "sip:" + username + "@" + getResources().getString(R.string.server_ip);
                        String registrar = "sip:" + getResources().getString(R.string.server_ip);
                        String proxy = "sip:" + getResources().getString(R.string.server_ip);
                        etId.setText(acc_id);

                        accCfg.setIdUri(acc_id);
                        accCfg.getRegConfig().setRegistrarUri(registrar);
                        AuthCredInfoVector creds = accCfg.getSipConfig()
                                .getAuthCreds();
                        creds.clear();
                        if (username.length() != 0) {
                            creds.add(new AuthCredInfo("Digest", "*", username, 0,
                                    password));
                        }
                        StringVector proxies = accCfg.getSipConfig().getProxies();
                        proxies.clear();
                        if (proxy.length() != 0) {
                            proxies.add(proxy);
                        }

                        /* Enable ICE */
                        accCfg.getNatConfig().setIceEnabled(true);

                        /* Finally */
                        lastRegStatus = "";
                        try {
                            account.modify(accCfg);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("MainActivity", "dlgAccountSetting Exception: " + e.getMessage(), "WARN", null);
                        }
                        try {
                            Message m = Message.obtain(handler, 10);
                            m.sendToTarget();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("MainActivity", "dlgAccountSetting Exception: " + e.getMessage(), "WARN", null);
                        }
                    }
                }
            });
            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog ad = adb.create();
            ad.show();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "dlgAccountSetting Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void makeCall(View view) {
//		if (buddyListSelectedIdx == -1)
//			return;
//
//		/* Only one call at anytime */
//		if (currentCall != null) {
//			return;
//		}
//		HashMap<String, String> item = (HashMap<String, String>) buddyListView
//				.getItemAtPosition(buddyListSelectedIdx);
//		String buddy_uri = item.get("uri");
//		MyCall call = new MyCall(account, -1);
//		CallOpParam prm = new CallOpParam(true);
///*
//CallSetting opt = prm.getOpt();
//		opt.setAudioCount(1);
//		if(audio_video.equals("audio")){
//			opt.setVideoCount(0);
//		} else {
//			opt.setVideoCount(1);
//		}
// */
//		try {
//			call.makeCall(buddy_uri, prm);
//		} catch (Exception e) {
//			call.delete();
//			return;
//		}
//
//		currentCall = call;
//		showCallActivity();
    }

    private void dlgAddEditBuddy(BuddyConfig initial) {
        try {
            final BuddyConfig cfg = new BuddyConfig();
            final BuddyConfig old_cfg = initial;
            final boolean is_add = initial == null;

            LayoutInflater li = LayoutInflater.from(this);
            View view = li.inflate(R.layout.dlg_add_buddy, null);

            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setView(view);

            final EditText etUri = (EditText) view.findViewById(R.id.editTextUri);
            final CheckBox cbSubs = (CheckBox) view
                    .findViewById(R.id.checkBoxSubscribe);

            if (is_add) {
                adb.setTitle("Add Buddy");
            } else {
                String edituserURI = initial.getUri();
                edituserURI = edituserURI.substring(4);
                edituserURI = edituserURI.split("@")[0];
                adb.setTitle("Edit Buddy");
                etUri.setText(edituserURI);
                cbSubs.setChecked(initial.getSubscribe());
            }

            adb.setCancelable(false);
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // cfg.setUri(etUri.getText().toString());
                    String b_uri = "sip:" + etUri.getText().toString()
                            + "@" + getResources().getString(R.string.server_ip);
                    cfg.setUri(b_uri);
                    cfg.setSubscribe(cbSubs.isChecked());

                    if (is_add) {
                        account.addBuddy(cfg);
                        buddyList.add(putData(cfg.getUri(), ""));
                        // buddyListAdapter.notifyDataSetChanged();
                        buddyArrayAdapter.notifyDataSetChanged();
                        buddyListSelectedIdx = -1;
                    } else {
                        if (!old_cfg.getUri().equals(cfg.getUri())) {
                            account.delBuddy(buddyListSelectedIdx);
                            account.addBuddy(cfg);
                            buddyList.remove(buddyListSelectedIdx);
                            buddyList.add(putData(cfg.getUri(), ""));
                            // buddyListAdapter.notifyDataSetChanged();
                            buddyArrayAdapter.notifyDataSetChanged();
                            buddyListSelectedIdx = -1;
                        } else if (old_cfg.getSubscribe() != cfg.getSubscribe()) {
                            MyBuddy bud = account.buddyList
                                    .get(buddyListSelectedIdx);
                            try {
                                bud.subscribePresence(cfg.getSubscribe());
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("MainActivity", "dlgAddEditBuddy Exception: " + e.getMessage(), "WARN", null);
                            }
                        }
                    }
                    try {
                        Message m = Message.obtain(handler, 10);
                        m.sendToTarget();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MainActivity", "dlgAddEditBuddy Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });
            adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });

            AlertDialog ad = adb.create();
            ad.show();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "dlgAddEditBuddy Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void addBuddy(View view) {
        dlgAddEditBuddy(null);
    }

    public void editBuddy(View view) {
        try {
            if (buddyListSelectedIdx == -1)
                return;

            BuddyConfig old_cfg = account.buddyList.get(buddyListSelectedIdx).cfg;
            dlgAddEditBuddy(old_cfg);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "editBuddy Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void delBuddy(View view) {
        try {
            if (buddyListSelectedIdx == -1)
                return;

            final HashMap<String, String> item = (HashMap<String, String>) buddyListView
                    .getItemAtPosition(buddyListSelectedIdx);
            String buddy_uri = item.get("uri");

            DialogInterface.OnClickListener ocl = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            account.delBuddy(buddyListSelectedIdx);
                            buddyList.remove(item);
                            // buddyListAdapter.notifyDataSetChanged();
                            buddyArrayAdapter.notifyDataSetChanged();
                            buddyListSelectedIdx = -1;
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };

            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle(buddy_uri);
            adb.setMessage("\nDelete this buddy?\n");
            adb.setPositiveButton("Yes", ocl);
            adb.setNegativeButton("No", ocl);
            adb.show();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "delBuddy Exception: " + e.getMessage(), "WARN", null);
        }
    }

	/*
     * === MyAppObserver ===
	 *
	 * As we cannot do UI from worker thread, the callbacks mostly just send a
	 * message to UI/main thread.
	 */

    public void notifyIncomingCall(MyCall call) {
        try {
            if (AppSharedpreferences.getInstance(mainContext).getBoolean("login")) {
                //            startCallRingTone();
                MainActivity.isAudioCall = true;
                Message m = Message.obtain(handler, MSG_TYPE.INCOMING_CALL, call);
                m.sendToTarget();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "notifyIncomingCall Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void notifyRegState(pjsip_status_code code, String reason, int expiration) {
        try {
            Log.i("userState", "notifyRegState Method");
            Log.i("register", "notifyRegState");
            Log.i("SipVideo", "came to notifyRegState -=- reason : " + reason + " expiration : " + expiration + " response value : " + code.swigValue());
            Appreference.printLog("sipregister", "SipRegistartion reason : " + reason + " expiration : " + expiration + " Response Code : " + code.swigValue(), "DEBUG", null);
            String msg_str = "";
            if (expiration == 0 || ((code.swigValue() / 100) != 2)) {
                msg_str += "Unregistration";
                Appreference.printLog("sipregister", "Registration Fails ", "DEBUG", null);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Appreference.sipRegistrationState = false;
                        ActiveScreens();
                        nwstate = true;
                    }
                });

                Log.i("SipVideo", "isPresenceReregister : " + isPresenceReregister);
                Log.i("SipVideo", "Appreference.signout_pressed : " + Appreference.signout_pressed);
                Log.i("SipVideo", "account : " + account + "   account is vaild==  " + account.isValid());
                if (isPresenceReregister) {
                    Log.d("SipVideo", "Inside the handler");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            reRegister();
                        }
                    }, 15000);
                }else if (Appreference.isPasswordChanged) {
                    Appreference.isPasswordChanged = false;
                    Log.i("SipReg", "changed_Password " + appSharedpreferences.getString("mPassword"));
                    if (appSharedpreferences.getString("mPassword") != null && !appSharedpreferences.getString("mPassword").equalsIgnoreCase("")) {
                        username = appSharedpreferences.getString("loginUserName");
                        password = appSharedpreferences.getString("mPassword");
                        init();
                    }
                } else {
                    if (Appreference.signout_pressed && account != null && account.isValid()) {
                        if (app != null) {
                            Log.i("SipVideo", "delAcc");
                            //                        app.delAcc(account);
                            Appreference.signout_pressed = false;
                            Intent logoutscreen = new Intent(context, LoginActivity.class);

                            logoutscreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            logoutscreen.putExtra("logout", true);
                            //                            loginscreen.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            startActivity(logoutscreen);
                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);

                            //                            Activity.class.getEnclosingClass();
                            finish();
                        }
                    }
                }
            } else {

                isPresenceReregister = true;
                Appreference.sipRegistrationState = true;
                Log.i("SipVideo", " Appreference.networkState  :" +
                        Appreference.networkState + " nwstate : " + nwstate);
                if (Appreference.sipRegistrationState && Appreference.networkState && nwstate) {
                    Log.i("Presence", "notifyRegState Appreference.networkState && nwstate");
                    nwstate = false;
                    ShowNetworkConnectedstate();
                }
                msg_str += "Registration";
                Appreference.printLog("sipregister", " Registration Successfully", "DEBUG", null);
                if (isFirstRegister) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            reRegister();
                            updateStatus();
                        }
                    });
                    isFirstRegister = false;
                } else {
                    updateStatus();
                }

                //            if (isPresenceState) {
                ContactCall();
                //            } else {
                ActiveScreens();
                //            }
                try {
                    if (received_intent_value != null && received_intent_value.getExtras() != null) {
                        if (received_intent_value.getExtras().getString("from") != null && received_intent_value.getExtras().getString("from").equalsIgnoreCase("scheduler")) {
                            scheduleCallAlert(received_intent_value);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            if (code.swigValue() / 100 == 2) {
                msg_str += " successful";
                Appreference.printLog("sipregister", "code.swigValue() / 100 == 2 successful ", "DEBUG", null);
            } else {
                msg_str += " failed: " + reason;
                Appreference.printLog("sipregister", " Registration failed ", "DEBUG", null);
            }

            Message m = Message.obtain(handler, MSG_TYPE.REG_STATE, msg_str);
            m.sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "notifyRegState Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void notifyCallState(MyCall call) {
        Log.i("SipVideo", "came to notifyCallState in MainAvtivity");
        Log.i("buddystate", "MainActivity notifyCallState");
        try {
            if (currentCall == null) {
                Log.i("SipVideo", "came to notifyCallState in current call null");

//                CallInfo ci = null;
//                try {
//                    ci = call.getInfo();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    ci = null;
//                }
//                Log.i("SipVideo", "2 came to notifyCallState in current call -- ci : "+ci);
//                if (ci != null && ci.getState() ==
//                        pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
//                    Log.i("SipVideo", " delete: b111");
//                    call.delete();
//                }
                return;
            }
                   /* if(( !audioMediaHashMap.containsKey(call.getId()))) {
                        Log.i("SipVideo", "came to notifyCallState in audioMediaHashMap call != null");
                        return;
                    }*/
            Log.i("SipVideo", "came to notifyCallState in MainAvtivity 1 : isActive : " + call.isActive());
            CallInfo ci;
            try {
                ci = call.getInfo();


                Log.i("SipVideo", "Call id is ==>>  " + ci.getId());
                Log.i("SipVideo", "Call state is ==>>  " + ci.getState());
                Log.i("SipVideo", "Call RemoteUri is ==>>  " + ci.getRemoteUri());
                Log.i("SipVideo", "Call CallIdString is ==>>  " + ci.getCallIdString());
                Log.i("SipVideo", "Call LocalUri is ==>>  " + ci.getLocalUri());
                Log.i("SipVideo", "Call RemVideoCount is ==>>  " + ci.getRemVideoCount());
//                Log.i("SipVideo", "Call LastReason is ==>>  " + ci.getLastReason());
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "notifyCallState Exception: " + e.getMessage(), "WARN", null);
                ci = null;
            }
//


         /*   if(ci != null && ci.getState().equals(pjsip_status_code.PJSIP_SC_NOT_FOUND)){
                Log.i("SipVideo", "came to user not found ");

                stopRingTone();
//                currentCall = null;
                Log.i("SipVideo", "came to notifyCallState in MainAvtivity 4 call.getId() " + call.getId());

                if (MainActivity.audioMediaHashMap.containsKey(call.getId())) {
                    MainActivity.audioMediaHashMap.remove(call.getId());
                }
                Log.i("SipVideo", "came to notifyCallState in MainAvtivity 5");
                ArrayList<MyCall> curCall = (ArrayList<MyCall>) currentCallArrayList.clone();
                for (int i = 0; i < curCall.size(); i++) {
                    if (curCall.get(i).getId() == call.getId()) {
                        MainActivity.currentCallArrayList.remove(i);
//                        break;
                    }
                }
                if (currentCallArrayList.size() == 0) {
                    Log.i("SipVideo", "delete all call end");
                    call.delete();
                    currentCall = null;
                    Log.i("SipVideo", "MainActivity notifyCallState MainActivity.currentCall=null");
                }
            }*/


//            Log.d("Last State", "last state is ===>>" + ci.getLastStatusCode() + "    last reson ==>>  " + ci.getLastReason() + "   state Text " + ci.getStateText());

           /* if (ci != null && (ci.getLastStatusCode() == pjsip_status_code.PJSIP_SC_NOT_FOUND)) {
                Log.d("VideoBroadCasrCall", "Iside the pjsip_status_code.PJSIP_SC_NOT_FOUND method");
            }
            if (ci != null && ci.getLastStatusCode() == pjsip_status_code.PJSIP_SC_NOT_FOUND) {
                Log.d("VideoBroadCasrCall", "Iside the pjsip_status_code.PJSIP_SC_NOT_FOUND method");
            }
            if (ci != null && ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
                Log.d("VideoBroadCasrCall", "Iside the pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED method");
            }
            if (ci != null && ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_NULL) {
                Log.d("VideoBroadCasrCall", "Iside the pjsip_inv_state.PJSIP_INV_STATE_NULL method");
            }
            if (ci != null && ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_CONNECTING) {
                Log.d("VideoBroadCasrCall", "Iside the pjsip_inv_state.PJSIP_INV_STATE_CONNECTING method");
            }

            if (ci != null && ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_CALLING) {
                Log.d("VideoBroadCasrCall", "Iside the pjsip_inv_state.PJSIP_INV_STATE_CONNECTING method");
            }

            if (ci != null && ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {
                Log.d("VideoBroadCasrCall", "Iside the pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED method");
            }

            if (ci != null && ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_EARLY) {
                Log.d("VideoBroadCasrCall", "Iside the pjsip_inv_state.PJSIP_INV_STATE_EARLY method");
            }

            if (ci != null && ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_INCOMING) {
                Log.d("VideoBroadCasrCall", "Iside the pjsip_inv_state.PJSIP_INV_STATE_INCOMING method");
            }
            if (ci != null && ci.getState().equals(pjsip_status_code.PJSIP_SC_BUSY_HERE)) {
                Log.d("VideoBroadCasrCall", "Iside the pjsip_status_code.PJSIP_SC_BUSY_HERE method");
            }*/
            Log.i("SipVideo", "currentCallArrayList.size() : " + currentCallArrayList.size());
            ArrayList<MyCall> curCallList = currentCallArrayList;
            boolean dont_have_id = true;
            for (int i = 0; i < curCallList.size(); i++) {

                CallInfo cur_call_info = null;
                try {
                    cur_call_info = curCallList.get(i).getInfo();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MainActivity", "notifyCallState Exception: " + e.getMessage(), "WARN", null);
                    continue;
                }

                if (ci != null && cur_call_info.getCallIdString().equalsIgnoreCase(ci.getCallIdString())) {

                    dont_have_id = false;
                    if (ci != null && ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
                        Log.i("SipVideo", "came to notifyCallState in MainAvtivity 3");
                        stopRingTone();
//                currentCall = null;
                        Log.i("SipVideo", "came to notifyCallState in MainAvtivity 4 call.getId() " + call.getId());

                        Log.i("SipVideo", "came to notifyCallState in MainAvtivity 5");
                        ArrayList<MyCall> curCall = (ArrayList<MyCall>) currentCallArrayList.clone();
                        for (int j = 0; j < curCall.size(); j++) {
                            if (curCall.get(j).getId() == call.getId() && cur_call_info.getCallIdString().equalsIgnoreCase(call.getInfo().getCallIdString())) {

//                                Log.i("SipVideo", " dump : ==  " + MainActivity.currentCallArrayList.get(i).dump(true, " "));
//                                MainActivity.currentCallArrayList.get(j).dump(true, " ");
                                String dump = MainActivity.currentCallArrayList.get(i).dump(true, " ");
                                Log.i("SipVideo", " dump: == " + dump);
                                Appreference.printLog(" Mainactivity Dump : ", "\n" + dump, "DEBUG", null);
                                MainActivity.currentCallArrayList.remove(j);
//                        break;
                                if (MainActivity.audioMediaHashMap.containsKey(call.getId())) {
                                    MainActivity.audioMediaHashMap.remove(call.getId());
                                }
                            }
                        }
                        if (currentCallArrayList.size() == 0) {
                            Log.i("SipVideo", "delete all call end");
//                            call.delete();
                            currentCall = null;
                            Log.i("SipVideo", "MainActivity notifyCallState MainActivity.currentCall=null");
                        }
              /*  if(call.getId() == currentCall.getId()) {
                    currentCall = null;
                }*/
                        Log.i("SipVideo", "came to notifyCallState in MainAvtivity 6");
                    } else if (ci != null && ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {
                        Log.i("audio", "Main Activity notify call connected ");
                        Log.i("audio", "call.getId-->" + call.getId());
                        Log.i("audio", "ci.getRemoteUri--->" + ci.getRemoteUri());
                        Log.i("audio", "ci.getId-->" + ci.getId());
                    }
                    ArrayList<Object> objects = new ArrayList<Object>();
                    objects.add(call);
                    objects.add(ci);
                    try {
                        Message m = Message.obtain(handler, MSG_TYPE.CALL_STATE, objects);
                        m.sendToTarget();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (ci != null && dont_have_id) {
                if (ci.getState() ==
                        pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
                    Log.i("SipVideo", " delete: b112");
                    call.delete();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "notifyCallState Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void notifyCallMediaState(MyCall call) {
        try {
            Log.d("VideoCall", "Inside the notifyCallMediaState method ==  " + handler);
          /*  try {
                CallInfo ci;

                ci = call.getInfo();
                CallMediaInfoVector cmiv = ci.getMedia();
                for (int i = 0; i < cmiv.size(); i++) {
                    CallMediaInfo cmi = cmiv.get(i);
                    if (cmi.getType() == pjmedia_type.PJMEDIA_TYPE_VIDEO &&
                            cmi.getStatus() ==
                                    pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE &&
                            cmi.getVideoIncomingWindowId() != pjsua2.INVALID_ID) {
                        Log.d("SipVideo123", "Mainact notifyCallMediaState Before: " + cmi.getVideoCapDev());

                        cmi.setVideoCapDev(0);
                        Log.d("SipVideo123", "Mainact notifyCallMediaState  after: " + cmi.getVideoCapDev());

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            CallInfo ci;

                ci = call.getInfo();
            ArrayList<Object> objects = new ArrayList<Object>();
            objects.add(call);
            objects.add(ci);
            Log.d("SipVideo123", "Mainact VideoPreview call: " + call);
            Log.d("SipVideo123", "Mainact VideoPreview ci: " + ci);

            Message m = Message.obtain(handler, MSG_TYPE.CALL_MEDIA_STATE, objects);
            m.sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "notifyCallMediaState Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void notifyBuddyState(MyBuddy buddy) {
        Log.i("buddystate", "MainActivity notifyBuddyState");
        try {
            if (isPresenceReregister) {
                Message m = Message.obtain(handler, MSG_TYPE.BUDDY_STATE, buddy);
                m.sendToTarget();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "notifyBuddyState Exception: " + e.getMessage(), "WARN", null);
        }
    }

	/* === end of MyAppObserver ==== */

    public static void reRegister() {
        if (app.accList.size() != 0) {
            Log.i("register", "reRegister method");
//            account = app.accList.get(0);
            accCfg = account.cfg;
            try {
                account.setRegistration(true);
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "reRegister Exception: " + e.getMessage(), "WARN", null);
            }
        }
    }

    public static void startCallRingTone() {
        try {
            if (player == null) {
                player = new MediaPlayer();
                AssetFileDescriptor descriptor;
                descriptor = mainContext.getAssets().openFd("NFCW.mp3");
                player.setDataSource(descriptor.getFileDescriptor(),
                        descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                player.setLooping(true);
                player.prepare();
                player.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "startCallRingTone Exception: " + e.getMessage(), "WARN", null);
        }
    }


    public static void earpieceRingTone() {

        try {
            if (player == null) {
                player = new MediaPlayer();
                player.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);


                AssetFileDescriptor descriptor;
                descriptor = mainContext.getAssets().openFd("NFCW.mp3");
                player.setDataSource(descriptor.getFileDescriptor(),
                        descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                player.setLooping(true);
                player.prepare();
                player.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "earpieceRingTone Exception: " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "earpieceRingTone Exception: " + e.getMessage(), "WARN", null);
        }

    }


    public static void setCustomRingTone(String customRingTone) {

        try {
            if (player != null)
                player = null;
            Log.d("localMessage", "palyer stopped");
            if (player == null) {
                player = new MediaPlayer();
                player = MediaPlayer.create(mainContext, Uri.parse(customRingTone));
                player.setLooping(false);
                player.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "setCustomRingTone Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public static void setCustomPushRingTone(String customRingTone) {

        try {
            if (player != null)
                player = null;
            Log.d("gcmMessage", "palyer stopped");
            if (player == null) {
                player = new MediaPlayer();
                player = MediaPlayer.create(mainContext, Uri.parse(customRingTone));
                player.setLooping(false);
                player.start();
                Log.d("gcmMessage", "palyer started");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "setCustomPushRingTone Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public static void startAlarmRingTone() {
        Log.i("tone123", "startAlarmRingTone===>");
        try {
            if (player != null && !player.isPlaying())
                player = null;
            if (player == null) {
                player = new MediaPlayer();
                AssetFileDescriptor descriptor;
                descriptor = mainContext.getAssets().openFd("ring.wav");
                player.setDataSource(descriptor.getFileDescriptor(),
                        descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                player.setLooping(false);
                player.prepare();
                player.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "startAlarmRingTone Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public static void startTaskRingTone() {
        try {
            if (player == null) {
                player = new MediaPlayer();
                AssetFileDescriptor descriptor;
                descriptor = mainContext.getAssets().openFd("NFCW.mp3");
                player.setDataSource(descriptor.getFileDescriptor(),
                        descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                player.setLooping(true);
                player.prepare();
                player.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "startTaskRingTone Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public static void stopRingTone() {
        try {
            if (player != null) {

                if (player.isPlaying()) {
                    player.stop();
                    player.release();
                }
                player = null;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Appreference.printLog("MainActivity", "stopRingTone Exception: " + e.getMessage(), "WARN", null);
        }

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            cancelAlarmManager();
            if (mReceiver != null) {
                unregisterReceiver(mReceiver);
            }
            if (messageEntryThread != null) {
                messageEntryThread.stopThread();
                messageEntryThread = null;
                db_Queue = null;
            }
//        unRegister();
            Log.i("destroy", "MainActivity OnDestroy");
            Appreference.sipRegistrationState = false;
            nwstate = false;
            Log.i("service123", "Receieved notification about offlineSendActivity MainActivity onDestroy");
//            stopService(new Intent(getBaseContext(), offlineSendService.class));
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "onDestroy Exception: " + e.getMessage(), "WARN", null);
        }
    }

    private void startAlarmManager() {
        try {
            reregisterAlarm_started = true;
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(MainActivity.this, SipRegisterBroadastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 2145,
                    intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(), 15 * 60000, pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "startAlarmManager Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void cancelAlarmManager() {
        try {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, SipRegisterBroadastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 2145,
                    intent, 0);
            alarmManager.cancel(pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "cancelAlarmManager Exception: " + e.getMessage(), "WARN", null);
        }
    }

    private void updateStatus() {
        if (app.accList.size() != 0) {
//            account = app.accList.get(0);
            accCfg = account.cfg;

            Log.i("status", "login user uri--->" + accCfg.getIdUri());
            Log.i("register", "updateStatus");
            try {

                PresenceStatus presenceStatus = new PresenceStatus();
                presenceStatus.setStatus(pjsua_buddy_status.PJSUA_BUDDY_STATUS_ONLINE);
//                Appreference.currentPresenceStateIs = "Online";
//                Log.i("register", "updateStatus Appreference.loginuser_status" +Appreference.loginuser_status);
                Log.i("register", "updateStatus Appreference.currentPresenceStateIs" + Appreference.currentPresenceStateIs);
                if (Appreference.loginuser_status != null && !Appreference.currentPresenceStateIs.equalsIgnoreCase("Offline")) {
                    if (Appreference.loginuser_status != null && Appreference.loginuser_status.equalsIgnoreCase("Online")) {
                        Appreference.currentPresenceStateIs = "Online";
                    } else if (Appreference.loginuser_status != null && Appreference.loginuser_status.equalsIgnoreCase("Away")) {
                        presenceStatus.setActivity(pjrpid_activity.PJRPID_ACTIVITY_AWAY);
                        Appreference.currentPresenceStateIs = "Away";
                    } else if (Appreference.loginuser_status != null && Appreference.loginuser_status.equalsIgnoreCase("Busy")) {
                        presenceStatus.setActivity(pjrpid_activity.PJRPID_ACTIVITY_BUSY);
                        Appreference.currentPresenceStateIs = "Busy";
                    } else {
                        Appreference.currentPresenceStateIs = "Online";
                    }
                } else if (Appreference.loginuser_status == null) {
                    Appreference.loginuser_status = "Online";
                    Appreference.currentPresenceStateIs = "Online";
                }
                Log.d("Presence", "state is  " + Appreference.currentPresenceStateIs);
                presenceStatus.setNote(Appreference.currentPresenceStateIs);
                account.setOnlineStatus(presenceStatus);
//                presenceStatus.setRpidId("Online");
                AccountPresConfig accountPresConfig = accCfg.getPresConfig();
                accountPresConfig.setPublishEnabled(true);

              /*  if(isPresenceState && isPresenceReregister) {
                    try {
                        PresenceStatus presenceStatus1 = new PresenceStatus();
                        presenceStatus1.setStatus(pjsua_buddy_status.PJSUA_BUDDY_STATUS_ONLINE);
//                Appreference.currentPresenceStateIs = "Online";
                        if(Appreference.currentPresenceStateIs.equalsIgnoreCase("Offline")){
                            Appreference.currentPresenceStateIs = Appreference.loginuser_status;
                        }
                        presenceStatus1.setNote(Appreference.currentPresenceStateIs);
                        account.setOnlineStatus(presenceStatus1);
//                presenceStatus.setRpidId("Online");
                        AccountPresConfig accountPresConfig1 = accCfg.getPresConfig();
                        accountPresConfig1.setPublishEnabled(true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/

                // call back method for buddy presence state
            /*    handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("subscribe","MyAccount class onInComingSubscribe accCfg.getIdUri ===== >>>>  "+accCfg.getIdUri());
                        OnIncomingSubscribeParam onIncomingSubscribeParam = new OnIncomingSubscribeParam();
                        onIncomingSubscribeParam.setFromUri(accCfg.getIdUri());
                        account.onIncomingSubscribe(onIncomingSubscribeParam);
                    }
                },6000);*/


                Log.i("register", "AccountPresConfig publish enable " + accountPresConfig.getPublishEnabled());

            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "updateStatus Exception: " + e.getMessage(), "WARN", null);
            }
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        int tabCount;

        public SectionsPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;

        }

        @Override
        public Fragment getItem(int position1) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);
            Log.i("item", "SectionsPagerAdapter getItem method" + position1);
//            Fragment fragment = null;
            try {
                switch (position1) {
                    case 0:
                        Log.i("item", "position--->0");
                        Appreference.isProject = false;
                        fragment = ContactsFragment.newInstance(1, mainContext);
                        break;
                    case 1:
                        Log.i("item", "position--->1");
                        Appreference.isProject = false;
                        fragment = ChatFragment.newInstance(2);
                        break;
                    case 2:
                        Log.i("item", "position--->2");
                        Appreference.isProject = false;
                        fragment = SettingsFragment.newInstance(3, mainContext);
                        break;

                    case 3:
                        Log.i("item", "position--->3");
                        Appreference.isProject = true;
                        fragment = ProjectsFragment.newInstance(4, mainContext);
                        break;
                    default:
                        fragment = null;
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "SectionsPagerAdapter Exception: " + e.getMessage(), "WARN", null);
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            try {
                switch (position) {
                    case 0:
                        return "SECTION 1";
                    case 1:
                        return "SECTION 2";
                    case 2:
                        return "SECTION 3";
                    case 3:
                        return "SECTION 4";
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "CharSequence Exception: " + e.getMessage(), "WARN", null);
            }
            return null;
        }
    }

   /* @Override
    public void onBackPressed() {
//		super.onBackPressed();
    }*/

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back once more to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public static void showToast(final String result) {
        handler1.post(new Runnable() {
            @Override
            public void run() {
                Context context = null;
                if (Appreference.context_table.containsKey("projecthistory")) {
                    ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                    context = projectHistory.context;
                } else if (Appreference.context_table.containsKey("taskcoversation")) {
                    NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                    context = newTaskConversation.context;
                } else if (Appreference.context_table.containsKey("settingsfragment")) {
                    SettingsFragment settingsfragment = (SettingsFragment) Appreference.context_table.get("settingsfragment");
                    context = settingsfragment.getContext();
                }

                if (context != null)
                    Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            }
        });

    }

    public void showAlarmAlert(final String status, final int taskId, final String jobcodeNo) {
        try {
            handler1.post(new Runnable() {
                @Override
                public void run() {
                    Context context = null;
                    if (Appreference.context_table.containsKey("projecthistory")) {
                        ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                        if (projectHistory != null) {
                            Log.i("tone123", "projectHistory context get if***********");
                            context = projectHistory.context;
                        }
                    } else if (Appreference.context_table.containsKey("taskcoversation")) {
                        NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                        Log.i("tone123", "newTaskConversation.webtaskId***********" + newTaskConversation.webtaskId);
                        Log.i("tone123", "receivedTaskId***********" + taskId);
//                        if(newTaskConversation!=null && !newTaskConversation.webtaskId.equalsIgnoreCase(String.valueOf(taskId))) {
                        context = newTaskConversation.context;
//                        }
                    } else if (Appreference.context_table.containsKey("settingsfragment")) {
                        SettingsFragment settingsfragment = (SettingsFragment) Appreference.context_table.get("settingsfragment");
                        if (settingsfragment != null) {
                            Log.i("tone123", "settingsfragment context get if***********");
                            context = settingsfragment.getContext();
                        }
                    } else if (Appreference.context_table.containsKey("traveljobdetails")) {
                        TravelJobDetails traveljobdetails = (TravelJobDetails) Appreference.context_table.get("traveljobdetails");
                        if (traveljobdetails != null) {
                            Log.i("tone123", "traveljobdetails context get if***********");
                            context = traveljobdetails.context;
                        }
                    } else if (Appreference.context_table.containsKey("customtravelpickeractivity")) {
                        CustomTravelPickerActivity customtravelpickeractivity = (CustomTravelPickerActivity) Appreference.context_table.get("customtravelpickeractivity");
                        if (customtravelpickeractivity != null) {
                            Log.i("tone123", "customtravelpickeractivity context get if***********");
                            context = customtravelpickeractivity.context;
                        }
                    } else {
                        Log.i("tone123", "mainActivity context get if***********");
                        context = mainContext;
                    }
                    String query = "select status from projectStatus where projectId='" + jobcodeNo + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + taskId + "'";
                    int timer_Alert_by_current_status = VideoCallDataBase.getDB(context).getCurrentStatus(query);
                    Log.i("alarm123", "timer_Alert_by_current_status Mainactivity ===>***** " + timer_Alert_by_current_status);

                    String alertQuery = "select taskPlannedLatestEndDate from taskDetailsInfo where (taskStatus='Hold' or taskStatus='Paused') and projectId='" + jobcodeNo + "'and taskId= '" + taskId + "'";
                    String isAlertShown = VideoCallDataBase.getDB(context).getAlertShownstatus(alertQuery,"taskPlannedLatestEndDate");

                    /*isAlertShown==1  --> Alert Not yet shown
                    * isAlertShown==0   -->Alert shown already*/
                    if (context != null) {
                        if ((isAlertShown != null && !isAlertShown.equalsIgnoreCase("") && !isAlertShown.equalsIgnoreCase(null) && isAlertShown.equalsIgnoreCase("1"))
                                && (timer_Alert_by_current_status == 1 || timer_Alert_by_current_status == 3)) {
                            //                        Appreference.isTimeUpshown=true;
                            String AlarmRingedUpdateQuery = "update taskDetailsInfo set taskPlannedLatestEndDate='0' where projectId='" + jobcodeNo + "'and taskId= '" + taskId + "'";
                            Log.i("tone123", "updateSnoozeTime_query***********" + AlarmRingedUpdateQuery);
                            VideoCallDataBase.getDB(context).updateaccept(AlarmRingedUpdateQuery);
                            Log.i("tone123", "MAinActivity From===>");
                            MainActivity.startAlarmRingTone();

                            String get_OracleprojectId_query = "select oracleProjectId from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'and projectId='" + jobcodeNo + "'";
                            String OracleIdForProjectId = VideoCallDataBase.getDB(context).getprojectIdForOracleID(get_OracleprojectId_query);
                            String quryActivity1 = "select oracleTaskId from projectHistory where projectId='" + jobcodeNo + "' and taskId= '" + taskId + "'";
                            String oracleTaskId = VideoCallDataBase.getDB(getApplication()).getProjectParentTaskId(quryActivity1);
                            Log.i("alarm123", "startHoldOrPauseAlarmManager started");
                            String my_status;
                            if (timer_Alert_by_current_status == 1) {
                                my_status = "hold";
                            } else
                                my_status = "pause";

                            Intent intent = new Intent(mainContext, ShowTimeupAlert.class);
                            intent.putExtra("projectId", jobcodeNo);
                            intent.putExtra("taskId", String.valueOf(taskId));
                            intent.putExtra("status", my_status);
                            intent.putExtra("OracleprojectId", OracleIdForProjectId);
                            intent.putExtra("OracletaskId", oracleTaskId);
                            startActivity(intent);
                        }else {
                            String taskQuery = "select EstimAlarm from taskDetailsInfo where (taskDescription='Task is Started') and projectId='" + jobcodeNo + "'and taskId= '" + taskId + "'";
                            String getEstimetedTimeAlertShownOrNot = VideoCallDataBase.getDB(context).getAlertShownstatus(taskQuery,"EstimAlarm");
                            if ((getEstimetedTimeAlertShownOrNot != null && !getEstimetedTimeAlertShownOrNot.equalsIgnoreCase("")
                                    && !getEstimetedTimeAlertShownOrNot.equalsIgnoreCase(null) && getEstimetedTimeAlertShownOrNot.equalsIgnoreCase("1"))){
                                String AlarmRingedUpdateQuery = "update taskDetailsInfo set EstimAlarm='0' where projectId='" + jobcodeNo + "'and taskId= '" + taskId + "'";
                                Log.i("estimtone123", "updateSnoozeTime_query***********" + AlarmRingedUpdateQuery);
                                VideoCallDataBase.getDB(context).updateaccept(AlarmRingedUpdateQuery);
                                MainActivity.startAlarmRingTone();

                                String get_OracleprojectId_query = "select oracleProjectId from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'and projectId='" + jobcodeNo + "'";
                                String OracleIdForProjectId = VideoCallDataBase.getDB(context).getprojectIdForOracleID(get_OracleprojectId_query);
                                String quryActivity1 = "select oracleTaskId from projectHistory where projectId='" + jobcodeNo + "' and taskId= '" + taskId + "'";
                                String oracleTaskId = VideoCallDataBase.getDB(getApplication()).getProjectParentTaskId(quryActivity1);
                                Log.i("alarm123", "startHoldOrPauseAlarmManager started");


                                Intent intent = new Intent(mainContext, ShowEstimTimeupAlert.class);
                                intent.putExtra("projectId", jobcodeNo);
                                intent.putExtra("taskId", String.valueOf(taskId));
                                intent.putExtra("OracleprojectId", OracleIdForProjectId);
                                intent.putExtra("OracletaskId", oracleTaskId);
                                startActivity(intent);
                            }
                        }
                    }
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "showAlarmAlert Exception: " + e.getMessage(), "WARN", null);
        }

    }

    public void templateImageDownload(TaskDetailsBean fileName) {
        Log.i("profiledownload", "fileName--1 " + fileName);
        Appreference.do_downloadForServiceManualPDF_Path=Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + "/High Message/downloads/";
        new DownloadImage(fileName).execute();

    }

    public void templateDownloadVideo(String fileName) {
        new DownloadVideo(getResources().getString(R.string.task_reminder) + fileName).execute();
    }

    public void notifyChatReceived(String FromUri, String toUri, String mimeType, String message) {
        try {
            Log.i("dbentry", "came to notifyChatReceived");
            DbEntryBean dbEntryBean = new DbEntryBean();
            dbEntryBean.setFromUri(FromUri);
            dbEntryBean.setToUri(toUri);
            dbEntryBean.setMimeType(mimeType);
            dbEntryBean.setMessage(message);
            dbEntryBean.setCall_back(MainActivity.this);
            if (db_Queue == null) {
                initializeThread();
            }
            db_Queue.addMsg(dbEntryBean);
            Log.i("dbentry", " queue :=> " + db_Queue.getSize());
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "notifyChatReceived Exception: " + e.getMessage(), "WARN", null);
        }

    }

    private void initializeThread() {
        try {
            db_Queue = new Queue();
            messageEntryThread = MessageEntryThread.getMessageEntryThread(true);
            messageEntryThread.setQueue(db_Queue);
            messageEntryThread.setRunning(true);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "initializeThread Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void notifyChat_Received(String FromUri, String toUri, String mimeType, String message) {
        Log.i("chat", "======== MainActivity notifychatReceived ========\n");
        Log.i("chat", "From     : " + FromUri);
        Log.i("chat", "To       : " + toUri);
        Log.i("chat", "Mimetype : " + mimeType);
        Log.i("chat", "Body     : " + message);
        try {
            callerid = FromUri;
            if (message.contains("<Buddychatinfo")) {
                if (Appreference.context_table.containsKey("chatactivity")) {
                    Log.i("chat ", "MA context table available ");
                    xmlparser xmlparser = new xmlparser();
                    ChatBean bean = xmlparser.parseChat(message);
                    //scheduled alaram manager start method

                    //                if (bean.getScheduled() != null && !bean.getScheduled().equalsIgnoreCase("") && !bean.getScheduled().equalsIgnoreCase("null")) {
                    //                    startSchedulechatManager(bean);
                    //                }
                    final ChatActivity chatActivity = (ChatActivity) Appreference.context_table.get("chatactivity");
                    if (chatActivity != null) {
                        Log.i("chat ", "MA chatActivity!=null ");
                        //                    xmlparser xmlparser = new xmlparser();
                        //                    ChatBean bean = xmlparser.parseChat(message);
                        if (bean.getMsgtype().equalsIgnoreCase("audio") || bean.getMsgtype().equalsIgnoreCase("video")
                                || bean.getMsgtype().equalsIgnoreCase("image") || bean.getMsgtype().equalsIgnoreCase("sketch")
                                || bean.getMsgtype().equalsIgnoreCase("file")) {
                            Appreference.do_downloadForServiceManualPDF_Path=Environment.getExternalStorageDirectory()
                                    .getAbsolutePath()
                                    + "/High Message/downloads/";
                            new DownloadImage(getResources().getString(R.string.file_upload) + bean.getMessage(), bean.getMessage()).execute();
                        }
                        Log.i("chat ", "MA images_type check if ");
                        chatActivity.notifychatReceived(bean);
                    }

                    if (isApplicationBroughtToBackground()) {
                        ChatBean cb = new ChatBean();
                        Intent intent = new Intent(MainActivity.this, ChatAlert.class);
                        intent.putExtra("hostname", "chat");
                        intent.putExtra("userName", cb.getUsername());
                        startActivity(intent);

                    }
                } else {
                    Log.i("chat", "MA chatActivity==null ");
                    xmlparser xmlparser = new xmlparser();
                    ChatBean bean = xmlparser.parseChat(message);
                    bean.setUsername(MainActivity.username);
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                    String currentDateandTime = sdf.format(new Date());
                    //                bean.setDatetime(bean.getFromUser() + " " + currentDateandTime);
                    bean.setOpened("1");
                    Log.i("chat", "MA opened mainActivity " + bean.getOpened());

                    if (bean.getScheduled() != null && !bean.getScheduled().equalsIgnoreCase("") && !bean.getScheduled().equalsIgnoreCase("null")) {
                        if (bean.getScheduled().split(",")[0].equalsIgnoreCase("scheduled")) {
                            bean.setOpened("2");
                        } else {
                            bean.setOpened("1");
                        }
                        VideoCallDataBase.getDB(mainContext).insertChat_history(bean);
                        startSchedulechatManager(bean);
                    } else {
                        VideoCallDataBase.getDB(mainContext).insertChat_history(bean);
                    }
                    if (bean.getMsgtype().equalsIgnoreCase("audio") || bean.getMsgtype().equalsIgnoreCase("video")
                            || bean.getMsgtype().equalsIgnoreCase("image") || bean.getMsgtype().equalsIgnoreCase("sketch")
                            || bean.getMsgtype().equalsIgnoreCase("file")) {
                        Appreference.do_downloadForServiceManualPDF_Path=Environment.getExternalStorageDirectory()
                                .getAbsolutePath()+ "/High Message/downloads/";
                        new DownloadImage(getResources().getString(R.string.file_upload) + bean.getMessage(), bean.getMessage()).execute();
                        Log.i("chatalert", "images_type check inside else ");
                    }
                    Log.i("chat", "MA images_type check else ");
                    Log.i("chat ", "MA openPinActivity ** " + openPinActivity);
                    String username, chat_message, messagetype;
                    username = bean.getUsername();
                    chat_message = bean.getMessage();
                    messagetype = bean.getMsgtype();
                    Log.i("chat ", "MA getUsername " + bean.getUsername());
                    Log.i("chat ", "MA chatBean.getMessage() " + bean.getMessage());

                    Intent intent = new Intent(MainActivity.this, ChatAlert.class);
                    intent.putExtra("hostname", "chat");
                    intent.putExtra("username", username);
                    intent.putExtra("chat_message", chat_message);
                    intent.putExtra("messagetype", messagetype);
                    intent.putExtra("datetime", bean.getChatname());
                    intent.putExtra("chattype", "SecureChat");
                    intent.putExtra("chatid", bean.getChatid());
                    intent.putExtra("chatusers", bean.getChatmembers());
                    intent.putExtra("fromname", bean.getFromUser());
                    startActivity(intent);
                    Log.i("chat ", "MA context table available openPinActivity  " + openPinActivity);
                }
            } else if (message.contains("<Buddychatmembersupdate")) {
                Log.i("chat", " ===BuddyChatmembersupdate=== ");
                xmlparser xmlParser = new xmlparser();
                ChatBean bean = xmlParser.parseAddPeopleOnChat(message);
                Log.i("chat", "Added members " + bean.getChatmembers());
                VideoCallDataBase.getDB(mainContext).updateChathistoryForAddMembers(bean.getChatid(), bean.getChatmembers());
            } else if (message.contains("<TaskDetailsAddObserver>")) {
                xmlparser xmlParser = new xmlparser();
                TaskDetailsBean taskDetailsBean = xmlParser
                        .parseObserverDetailsSIPMessage(message);
                String OwnerName = VideoCallDataBase.getDB(context).getName(taskDetailsBean.getOwnerOfTask());
                String RejectedObservers = "";
                if (taskDetailsBean.getRejectedObserver() != null && !taskDetailsBean.getRejectedObserver().equalsIgnoreCase("") && !taskDetailsBean.getRejectedObserver().equalsIgnoreCase("null") && !taskDetailsBean.getRejectedObserver().equalsIgnoreCase(null) && !taskDetailsBean.getRejectedObserver().equalsIgnoreCase("(null)")) {
                    int counter = 0;
                    for (int i = 0; i < taskDetailsBean.getRejectedObserver().length(); i++) {
                        if (taskDetailsBean.getRejectedObserver().charAt(i) == ',') {
                            counter++;
                        }

                        Log.d("project_details", "Task Mem's counter size is == " + counter);
                    }
                    for (int j = 0; j < counter + 1; j++) {
                        String Mem_name = taskDetailsBean.getRejectedObserver().split(",")[j];
                        Log.i("project_details", "Task Mem's and position == " + Mem_name + " " + j);
                        String Memberlist = "";
                        Memberlist = VideoCallDataBase.getDB(context).getname(Mem_name);
                        RejectedObservers = Memberlist + ",";
                    }
                    RejectedObservers = RejectedObservers.substring(0, RejectedObservers.length() - 1);
                }
                if (!taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && !taskDetailsBean.getTaskReceiver().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                    Log.i("SyncEnable", "Task Receiver " + taskDetailsBean.getTaskReceiver());
                    Log.i("SyncEnable", "Task OwnerofTask " + taskDetailsBean.getOwnerOfTask());
                    taskDetailsBean.setSyncEnable("enable");
                }
                Log.i("taskobserver", "taskDetailsBean.getTaskId()##  ---> " + taskDetailsBean.getTaskId());
                appSharedpreferences.saveBoolean("syncTask" + taskDetailsBean.getTaskId(), false);
                if (Appreference.context_table.containsKey("taskcoversation")) {
                    Log.e("taskobserver", "conversation page was open " + taskDetailsBean.getRejectedObserver() + " " + taskDetailsBean.getRejectedObserver().length());
                    if (taskDetailsBean.getRejectedObserver() != null && !taskDetailsBean.getRejectedObserver().equalsIgnoreCase(" ") && !taskDetailsBean.getRejectedObserver().equalsIgnoreCase(null) && !taskDetailsBean.getRejectedObserver().equalsIgnoreCase("(null)") && !taskDetailsBean.getRejectedObserver().equalsIgnoreCase("null") && taskDetailsBean.getRejectedObserver().length() > 4) {
                        Log.i("taskobserver", "rejected " + taskDetailsBean.getRejectedObserver());
                        Log.i("taskobserver", "was removed ** ---> " + taskDetailsBean.getTaskDescription());
                        if ((taskDetailsBean.getRejectedObserver().equalsIgnoreCase("(null)") || taskDetailsBean.getRejectedObserver().equalsIgnoreCase(null) || taskDetailsBean.getRejectedObserver().equalsIgnoreCase("null") || taskDetailsBean.getRejectedObserver().equalsIgnoreCase("")) && (taskDetailsBean.getTaskDescription() != null || taskDetailsBean.getTaskDescription().equalsIgnoreCase(""))) {
                            taskDetailsBean.setTaskDescription(taskDetailsBean.getTaskDescription() + "\n");
                            Log.i("taskobserver", "was removed ---> " + taskDetailsBean.getTaskDescription());
                        } else if ((!taskDetailsBean.getRejectedObserver().equalsIgnoreCase("(null)") || !taskDetailsBean.getRejectedObserver().equalsIgnoreCase(null) || !taskDetailsBean.getRejectedObserver().equalsIgnoreCase("")) && (taskDetailsBean.getTaskDescription() == null)) {
                            taskDetailsBean.setTaskDescription(OwnerName + " removed " + RejectedObservers + " from observers");
                            Log.i("taskobserver", "was removed 3 ---> " + taskDetailsBean.getTaskDescription());
                        } else {
                            taskDetailsBean.setTaskDescription(taskDetailsBean.getTaskDescription() + "\n" + OwnerName + " removed " + RejectedObservers + " from observers");
                            Log.i("taskobserver", "was removed1 ---> " + taskDetailsBean.getTaskDescription());
                        }
                        String[] reject = taskDetailsBean.getRejectedObserver().split(",");
                        for (String name : reject) {
                            Log.i("observer", name + "    " + Appreference.loginuserdetails.getUsername());
                            if (name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                Log.i("taskobserver", "was removed " + name);
                                Log.i("taskobserver", "was removed ** " + Appreference.loginuserdetails.getUsername());
                                taskDetailsBean.setYouRemoved(true);
                            }
                        }
                    }
                    NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                    taskDetailsBean.setSubType("normal");
                    newTaskConversation.notifyTaskReceived(taskDetailsBean);
                    newTaskConversation.refresh();
                } /*else if (Appreference.context_table.containsKey("traveljobdetails")) {
                    TravelJobDetails travelJobDetails = (TravelJobDetails) Appreference.context_table.get("traveljobdetails");
                    travelJobDetails.notifyTaskReceived(taskDetailsBean);
                    travelJobDetails.refresh();
                }*/ else if (Appreference.context_table.containsKey("taskhistory")) {
                    Log.i("taskobserver", "was obs1 ---> ");
                    if (taskDetailsBean.getRejectedObserver() != null && !taskDetailsBean.getRejectedObserver().equalsIgnoreCase(" ") && !taskDetailsBean.getRejectedObserver().equalsIgnoreCase(null) && !taskDetailsBean.getRejectedObserver().equalsIgnoreCase("null") && !taskDetailsBean.getRejectedObserver().equalsIgnoreCase("(null)") && !taskDetailsBean.getRejectedObserver().equalsIgnoreCase(null) && taskDetailsBean.getRejectedObserver().length() > 4) {
                        Log.i("task observer", "rejected" + taskDetailsBean.getRejectedObserver());
                        if (taskDetailsBean.getRejectedObserver().equalsIgnoreCase("(null)") || taskDetailsBean.getRejectedObserver().equalsIgnoreCase(null) || taskDetailsBean.getRejectedObserver().equalsIgnoreCase("") || taskDetailsBean.getRejectedObserver().equalsIgnoreCase("null") || taskDetailsBean.getRejectedObserver().equalsIgnoreCase(null)) {
                            Log.i("taskobserver", "was obs2 ---> ");
                            taskDetailsBean.setTaskDescription(taskDetailsBean.getTaskDescription() + "\n");
                        } else {
                            Log.i("taskobserver", "was obs3 ---> ");
                            taskDetailsBean.setTaskDescription(taskDetailsBean.getTaskDescription() + "\n" + OwnerName + " removed " + RejectedObservers + " from observers");
                        }
                    }
                    taskDetailsBean.setRead_status(1);
                    //                taskDetailsBean.setCatagory("Task");
                    if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("null") && !taskDetailsBean.getProjectId().equalsIgnoreCase(null) && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase("(null)")) {
                        dataBase.update_Project_history(taskDetailsBean);
                        Log.i("taskobserver", "was obs4 ---> ");
                    } else {

                        dataBase.insertORupdate_TaskHistoryInfo(taskDetailsBean);
                    }
                    dataBase.insertORupdate_Task_history(taskDetailsBean);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            TaskHistory taskHistory = (TaskHistory) Appreference.context_table.get("taskhistory");
                            taskHistory.refresh();
                        }
                    });
                } else if (Appreference.context_table.containsKey("alltasklist")) {
                    if (taskDetailsBean.getRejectedObserver() != null && !taskDetailsBean.getRejectedObserver().equalsIgnoreCase(" ") && !taskDetailsBean.getRejectedObserver().equalsIgnoreCase(null) && taskDetailsBean.getRejectedObserver().length() > 4) {
                        Log.i("task observer", "rejected" + taskDetailsBean.getRejectedObserver());
                        if (taskDetailsBean.getRejectedObserver().equalsIgnoreCase("(null)") || taskDetailsBean.getRejectedObserver().equalsIgnoreCase(null) || taskDetailsBean.getRejectedObserver().equalsIgnoreCase("")) {
                            taskDetailsBean.setTaskDescription(taskDetailsBean.getTaskDescription() + "\n");
                        } else {
                            taskDetailsBean.setTaskDescription(taskDetailsBean.getTaskDescription() + "\n" + OwnerName + " removed " + RejectedObservers + " from observers");
                        }
                    }
                    taskDetailsBean.setRead_status(1);
                    if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("null") && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase("(null)")) {
                        dataBase.update_Project_history(taskDetailsBean);
                    } else {
                        //                taskDetailsBean.setCatagory("Task");

                        dataBase.insertORupdate_TaskHistoryInfo(taskDetailsBean);
                    }
                    dataBase.insertORupdate_Task_history(taskDetailsBean);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            AllTaskList allTaskList = (AllTaskList) Appreference.context_table.get("alltasklist");
                            allTaskList.refresh();
                        }
                    });

                } else {
                    if (taskDetailsBean.getRejectedObserver() != null && !taskDetailsBean.getRejectedObserver().equalsIgnoreCase(" ") && !taskDetailsBean.getRejectedObserver().equalsIgnoreCase(null) && taskDetailsBean.getRejectedObserver().length() > 4) {
                        Log.i("task observer", "rejected" + taskDetailsBean.getRejectedObserver());
                        if (taskDetailsBean.getRejectedObserver().equalsIgnoreCase("(null)") || taskDetailsBean.getRejectedObserver().equalsIgnoreCase(null) || taskDetailsBean.getRejectedObserver().equalsIgnoreCase("")) {
                            taskDetailsBean.setTaskDescription(taskDetailsBean.getTaskDescription() + "\n");
                        } else {
                            taskDetailsBean.setTaskDescription(taskDetailsBean.getTaskDescription() + "\n" + OwnerName + " removed " + RejectedObservers + " observers");
                        }
                    }
                    taskDetailsBean.setRead_status(1);
                    if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("null") && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase("(null)")) {
                        dataBase.update_Project_history(taskDetailsBean);
                    } else {

                        dataBase.insertORupdate_TaskHistoryInfo(taskDetailsBean);
                    }
                    dataBase.insertORupdate_Task_history(taskDetailsBean);
                    Log.i("task ", "Complete percentage1 ");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
                            contactsFragment.refresh_contact();
                        }
                    });
                    if (Appreference.context_table.containsKey("projecthistory")) {
                        ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                        if (projectHistory != null) {
                            projectHistory.refresh();
                        }
                    }
                    //                showTaskNotification(taskDetailsBean);
                }
            } else if (message.contains("WithdrawTaskDetails")) {
                xmlparser xmlParser = new xmlparser();
                Log.i("Chat Message", "Received " + message);
                TaskDetailsBean taskDetailsBean = xmlParser
                        .parseWithDrawTaskDetailsSIPMessage(message);
                Log.i("withdrawtaskdetails", "value " + taskDetailsBean.getTaskDescription());
                if (Appreference.context_table.containsKey("taskcoversation")) {
                    NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                    newTaskConversation.notifyTaskWithDrawReceived(taskDetailsBean);
                    Log.d("TaskHistory", "Value true ");
                    newTaskConversation.refresh();
                    Log.i("popupmenu", "withdraw conversation ");
                } else if (Appreference.context_table.containsKey("taskhistory")) {
                    Log.i("popupmenu", "withdraw history ");
                    VideoCallDataBase.getDB(context).withdrawMsg_StatusUpdate(taskDetailsBean.getSignalid(), "Message has been Removed");
                    TaskHistory taskHistory = (TaskHistory) Appreference.context_table.get("taskhistory");
                    if (taskHistory != null) {
                        Log.d("TaskHistory", "Value true refreshed-2");
                        taskHistory.refresh();
                    }
                } else if (Appreference.context_table.containsKey("alltasklist")) {
                    Log.i("popupmenu", "withdraw history ");
                    VideoCallDataBase.getDB(context).withdrawMsg_StatusUpdate(taskDetailsBean.getSignalid(), "Message has been Removed");
                    AllTaskList allTaskList = (AllTaskList) Appreference.context_table.get("alltasklist");
                    if (allTaskList != null) {
                        Log.d("TaskHistory", "Value true refreshed-2");
                        allTaskList.refresh();
                    }
                } else {
                    VideoCallDataBase.getDB(context).withdrawMsg_StatusUpdate(taskDetailsBean.getSignalid(), "Message has been Removed");
                    ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
                    contactsFragment.refresh();
                    Log.i("popupmenu", "withdraw else ");
                }


            } else if (message.contains("removeCustomeTag")) {
                xmlparser xmlParser = new xmlparser();
                Log.i("Chat Message", "Received " + message);
                TaskDetailsBean taskDetailsBean = xmlParser
                        .parseCustomTagsDeleteSIPMessage(message);

                taskDetailsBean.setTaskDescription("Message has been Removed");
                Log.i("removeCustomeTag", "value " + taskDetailsBean.getTaskDescription());
                taskDetailsBean.setMimeType("text");
                taskDetailsBean.setSubType("");
                if (Appreference.context_table.containsKey("taskcoversation")) {
                    Log.i("removeCustomeTag", "taskcoversation " + taskDetailsBean.getTaskDescription());
                    NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                    taskDetailsBean.setTaskDescription("Message has been Removed");
                    newTaskConversation.notifyTaskDeleteReceived(taskDetailsBean);
                    Log.d("TaskHistory", "Value true ");
                }/*else if(Appreference.context_table.containsKey("taskhistory")){
                    Log.i("removeCustomeTag", "taskhistory " + taskDetailsBean.getTaskDescription());
    //                VideoCallDataBase.getDB(context).UpdateOrInsert(taskDetailsBean);
                    VideoCallDataBase.getDB(context).deleteCustomTagEntry(taskDetailsBean.getTaskId(), String.valueOf(taskDetailsBean.getCustomSetId()));
                    TaskHistory taskHistory = (TaskHistory) Appreference.context_table.get("taskhistory");
                    if (taskHistory != null) {
                        Log.d("TaskHistory", "Value true refreshed-2");
                        taskHistory.refresh();
                    }
                }*/ else {
                    Log.i("removeCustomeTag", "else " + taskDetailsBean.getTaskDescription());
                    //                VideoCallDataBase.getDB(context).UpdateOrInsert(taskDetailsBean);
                    VideoCallDataBase.getDB(context).deleteCustomTagEntry(taskDetailsBean.getTaskId(), String.valueOf(taskDetailsBean.getCustomSetId()));
                  /*  TaskHistory taskHistory = (TaskHistory) Appreference.context_tabl
                  e.get("taskhistory");
                    if (taskHistory != null) {
                        Log.d("TaskHistory", "Value true refreshed-2");
                        taskHistory.refresh();
                    }*/
                    ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
                    contactsFragment.refresh();
                }
            } else if (message.contains("NotificationAlertinfo")) {
                try {
                    xmlparser xmlParser = new xmlparser();
                    Log.i("ChatMessage", "Received " + message);
                    SipNotification notification = xmlParser
                            .parseSipNotificationSIPMessage(message);
                    //                Log.i("Groupchat", "MA newGroupName " + notification.getGroup_name());
                    //                Log.i("Groupchat", "MA newuseradded " + notification.getMember_added());
                    //                Log.i("Groupchat", "MA newGroupName " + notification.getAlert_sub_type());
                    //                Log.i("groupaccess", "MA_getRespondAudio---> " + notification.getGroupMemberAccess().getRespondAudio());

                    if (notification.getAlert_type() != null && notification.getAlert_type().equalsIgnoreCase("Group Change Notification")) {
                        if (notification.getAlert_sub_type().equals("Member Added")) {
                            //                GroupMembers_WebService(String.valueOf(notification.getSource_id()));
                            groupChangeAddNotification(String.valueOf(notification.getSource_id()), notification.getGroup_name(), notification.getMember_added());
                            Log.i("Groupchat", "MA_Member Added " + notification.getMember_added());
                        } else if (notification.getAlert_sub_type().equals("Member Removed") || notification.getAlert_sub_type().equals("Removed From Group")) {
                            Log.i("Groupchat", "MA_Member Removed* " + notification.getMember_added());
                            Log.i("Groupchat", "MA_Member Removed $** " + notification.getSource_id());
                            groupChangeDeleteNotification(String.valueOf(notification.getSource_id()), notification.getGroup_name(), notification.getMember_added());
                        } else if (notification.getAlert_sub_type().equals("Added To Group")) {
                            Log.i("Groupchat", "MA_Member Added another_group* " + notification.getMember_added());
                            Log.i("Groupchat", "MA_Member Added another_group $** " + notification.getSource_id());
                            groupChangeAddGroupNotification(String.valueOf(notification.getSource_id()));
                        } else if (notification.getAlert_sub_type().equalsIgnoreCase("Name Change")) {
                            VideoCallDataBase.getDB(context).GroupNameOrMemberChanges(String.valueOf(notification.getSource_id()), notification.getGroup_name());
                        }
                    } else if (notification.getAlert_type() != null && notification.getAlert_type().equalsIgnoreCase("Group Access Notification")) {
                        if (notification.getAlert_sub_type().equalsIgnoreCase("Access Restrictions")) {
                            Log.i("groupaccess", "MA_getRespondAudio " + notification.getGroupMemberAccess().getRespondAudio());
                            VideoCallDataBase.getDB(context).insertListMemberAccess_history(notification.getGroupMemberAccess(), username);
                        }
                    } else if (notification.getAlert_type() != null && notification.getAlert_type().equalsIgnoreCase("Project Notification")) {

                        if (notification.getAlert_sub_type() != null && notification.getAlert_sub_type().equalsIgnoreCase("New Project")) {
                            //                ProjectDetailsBean projectDetailsBean=new ProjectDetailsBean();
                            ProjectDetailsBean projectDetailsBean = notification.getProjectDetailsBean();
                            //                VideoCallDataBase.getDB(context).insertProject_history(projectDetailsBean);
                            //                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                            //                    nameValuePairs.add(new BasicNameValuePair("projectId", projectDetailsBean.getId()));
                            //                    Appreference.jsonRequestSender.getProject(EnumJsonWebservicename.getProject, nameValuePairs, MainActivity.this);

                            if (isNetworkAvailable()) {
                                List<NameValuePair> tagNameValuePairs = new ArrayList<NameValuePair>();
                                tagNameValuePairs.add(new BasicNameValuePair("userId", String.valueOf(Appreference.loginuserdetails.getId())));
                                //                Appreference.jsonRequestSender.listAllProject(EnumJsonWebservicename.listAllProject, tagNameValuePairs, this);
//                            Appreference.jsonRequestSender.listAllMyProject(EnumJsonWebservicename.listAllMyProject, tagNameValuePairs, this);
                                Log.i("wsTime123"," WS getAllJobDetails Request sent Time====>"+Appreference.getCurrentDateTime());
                                Appreference.jsonRequestSender.getAllJobDetails(EnumJsonWebservicename.getAllJobDetails, tagNameValuePairs, this);
                                Log.i("wsTime123"," WS getAllJobDetails Request sent Time====>"+Appreference.getCurrentDateTime());
                            } else
                                Toast.makeText(MainActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();

                        } else if (notification.getAlert_sub_type() != null && notification.getAlert_sub_type().equalsIgnoreCase("Member Added")) {
                            ProjectDetailsBean taskDetailsBean = notification.getProjectDetailsBean();
                            VideoCallDataBase.getDB(context).updateProjectMemberList(taskDetailsBean);
                            if (Appreference.context_table.containsKey("projecthistory")) {
                                ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                                if (projectHistory != null) {
                                    projectHistory.refresh();
                                }
                            }
                        } else if (notification.getAlert_sub_type() != null && notification.getAlert_sub_type().equalsIgnoreCase("Percentage update")) {
                            //                ProjectDetailsBean projectDetailsBean=notification.getProjectDetailsBean();
                            TaskDetailsBean taskDetailsBean = notification.getTaskDetailsBean();
                            VideoCallDataBase.getDB(context).update_Project_history(taskDetailsBean);
                            if (Appreference.context_table.containsKey("projecthistory")) {
                                ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                                if (projectHistory != null) {
                                    projectHistory.refresh();
                                }
                            }
                        } else if (notification.getAlert_sub_type() != null && notification.getAlert_sub_type().equalsIgnoreCase("Date Change")) {
                            TaskDetailsBean taskDetailsBean = notification.getTaskDetailsBean();
                            VideoCallDataBase.getDB(context).update_Project_history(taskDetailsBean);
                            if (Appreference.context_table.containsKey("projecthistory")) {
                                ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                                if (projectHistory != null) {
                                    projectHistory.refresh();
                                }
                            }
                        } else if (notification.getAlert_sub_type() != null && notification.getAlert_sub_type().equalsIgnoreCase("New Task") ||
                                notification.getAlert_sub_type().equalsIgnoreCase("Template") ||
                                notification.getAlert_sub_type().equalsIgnoreCase("taskDateChangedApproval")) {
                            TaskDetailsBean taskDetailsBean = notification.getTaskDetailsBean();
                            if (notification.getAlert_sub_type().equalsIgnoreCase("taskDateChangedApproval")) {
                                taskDetailsBean.setMsg_status(10);
                                if (taskDetailsBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    taskDetailsBean.setSendStatus("0");
                                } else {
                                    taskDetailsBean.setSendStatus("2");
                                }
                            } else {
                                if (taskDetailsBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    taskDetailsBean.setSendStatus("0");
                                    taskDetailsBean.setMsg_status(1);
                                } else {
                                    taskDetailsBean.setSendStatus("2");
                                }
                            }
                            VideoCallDataBase.getDB(context).insert_new_Project_history(taskDetailsBean);
                            VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                            //                        VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(taskDetailsBean);
                            if (Appreference.context_table.containsKey("projecthistory")) {
                                ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                                if (projectHistory != null) {
                                    projectHistory.refresh();
                                }
                            }
                            if (notification.getAlert_sub_type().equalsIgnoreCase("taskDateChangedApproval")) {
//                                if (taskDetailsBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                if (Appreference.context_table.containsKey("taskcoversation")) {
                                    NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                                    newTaskConversation.taskBeanListUpdate(taskDetailsBean);

                                }
//                                }
                            }
                        } else if (notification.getAlert_sub_type() != null && notification.getAlert_sub_type().equalsIgnoreCase("Project Deleted")) {
                            TaskDetailsBean taskDetailsBean = notification.getTaskDetailsBean();
                            VideoCallDataBase.getDB(context).deleteProject(taskDetailsBean.getProjectId());
                            if (Appreference.context_table.containsKey("projecthistory")) {
                                ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                                if (projectHistory != null) {
                                    projectHistory.refresh();
                                }
                            } else if (Appreference.context_table.containsKey("projectfragment")) {
                                ProjectsFragment projectfragment = (ProjectsFragment) Appreference.context_table.get("projectfragment");
                                if (projectfragment != null) {
                                    Log.d("TaskHistory", "Response Method MainActivity");
                                    projectfragment.notifyNewProjectReceived();
                                }
                            }
                        } else if (notification.getAlert_sub_type() != null && notification.getAlert_sub_type().equalsIgnoreCase("Draft delete")) {
                            TaskDetailsBean taskDetailsBean = notification.getTaskDetailsBean();
                            VideoCallDataBase.getDB(context).deleteProjectDraft(taskDetailsBean);
                            if (Appreference.context_table.containsKey("projecthistory")) {
                                ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                                if (projectHistory != null) {
                                    projectHistory.refresh();
                                }
                            } else if (Appreference.context_table.containsKey("projectfragment")) {
                                ProjectsFragment projectfragment = (ProjectsFragment) Appreference.context_table.get("projectfragment");
                                if (projectfragment != null) {
                                    Log.d("TaskHistory", "Response Method MainActivity");
                                    projectfragment.notifyNewProjectReceived();
                                }
                            }
                        } else if (notification.getAlert_sub_type() != null && notification.getAlert_sub_type().equalsIgnoreCase("Observer Added")) {
                            TaskDetailsBean taskDetailsBean = notification.getTaskDetailsBean();
                            String query = "select taskObservers from projectHistory where projectId='" + taskDetailsBean.getProjectId() + "' and taskId='" + taskDetailsBean.getTaskId() + "'";
                            String old_observers = VideoCallDataBase.getDB(context).getValuesForQuery(query);
                            if (old_observers != null && !old_observers.trim().equals("")) {
                                if (!old_observers.contains(taskDetailsBean.getTaskAddObservers())) {
                                    old_observers = old_observers + "," + taskDetailsBean.getTaskAddObservers();
                                }
                            } else {
                                old_observers = taskDetailsBean.getTaskAddObservers();
                            }
                            String update_query = "update projectHistory set taskObservers='" + old_observers + "' where projectId='" + taskDetailsBean.getProjectId() + "' and taskId='" + taskDetailsBean.getTaskId() + "'";
                            VideoCallDataBase.getDB(context).updateQuery(update_query);
                            if (Appreference.context_table.containsKey("projecthistory")) {
                                ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                                if (projectHistory != null) {
                                    projectHistory.refresh();
                                }
                            }
                        } else if (notification.getAlert_sub_type() != null && notification.getAlert_sub_type().equalsIgnoreCase("Observer Removed")) {
                            TaskDetailsBean taskDetailsBean = notification.getTaskDetailsBean();
                            String query = "select taskObservers from projectHistory where projectId='" + taskDetailsBean.getProjectId() + "' and taskId='" + taskDetailsBean.getTaskId() + "'";
                            String old_observers = VideoCallDataBase.getDB(context).getValuesForQuery(query);

                            if (old_observers != null && !old_observers.trim().equals("") && taskDetailsBean.getTaskRemoveObservers() != null) {
                                String new_observers = null;
                                List<String> old_observer_array = new ArrayList<String>();

                                List<String> removed_users = new ArrayList<String>();

                                if (taskDetailsBean.getTaskRemoveObservers().contains(",")) {
                                    removed_users.addAll(Arrays.asList(taskDetailsBean.getTaskRemoveObservers().split(",")));
                                } else {
                                    removed_users.add(taskDetailsBean.getTaskRemoveObservers());
                                }

                                if (old_observers.contains(",")) {
                                    old_observer_array.addAll(Arrays.asList(old_observers.split(",")));
                                } else {
                                    old_observer_array.add(old_observers);
                                }

                                for (int i = 0; i < old_observer_array.size(); i++) {
                                    boolean user_not_removed = true;
                                    for (int j = 0; j < removed_users.size(); j++) {
                                        if (old_observer_array.get(i).equalsIgnoreCase(removed_users.get(j))) {
                                            user_not_removed = false;
                                            break;
                                        }
                                    }
                                    if (user_not_removed) {
                                        if (new_observers == null) {
                                            new_observers = old_observer_array.get(i);
                                        } else {
                                            new_observers = new_observers + "," + old_observer_array.get(i);
                                        }
                                    }
                                }

                                String update_query = "update projectHistory set taskObservers='" + new_observers + "' where projectId='" + taskDetailsBean.getProjectId() + "' and taskId='" + taskDetailsBean.getTaskId() + "'";
                                VideoCallDataBase.getDB(context).updateQuery(update_query);

                                if (Appreference.context_table.containsKey("projecthistory")) {
                                    ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                                    if (projectHistory != null) {
                                        projectHistory.refresh();
                                    }
                                }

                            } else {
//                                old_observers = taskDetailsBean.getTaskAddObservers();
                            }

                        } else if (notification.getAlert_sub_type() != null && notification.getAlert_sub_type().equalsIgnoreCase("Issue")) {
                            TaskDetailsBean taskDetailsBean = notification.getTaskDetailsBean();
                            if (taskDetailsBean.getFromUserName() != null && taskDetailsBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                taskDetailsBean.setSendStatus("0");
                                taskDetailsBean.setMsg_status(1);
                            } else {
                                taskDetailsBean.setSendStatus("2");
                            }
                            VideoCallDataBase.getDB(context).insert_new_Project_history(taskDetailsBean);
                            VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                            //                        VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(taskDetailsBean);
                            if (Appreference.context_table.containsKey("projecthistory")) {
                                ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                                if (projectHistory != null) {
                                    projectHistory.refresh();
                                }
                            }
                        } else if (notification.getAlert_sub_type() != null && notification.getAlert_sub_type().equalsIgnoreCase("Reassign Task")) {
                            TaskDetailsBean taskDetailsBean = notification.getTaskDetailsBean();
                            String query = "select taskObservers from projectHistory where projectId='" + taskDetailsBean.getProjectId() + "' and taskId='" + taskDetailsBean.getTaskId() + "'";
                            String old_observers = VideoCallDataBase.getDB(context).getValuesForQuery(query);
                            ContentValues cv = new ContentValues();
                            if (old_observers != null && !old_observers.trim().equals("") && old_observers.contains(taskDetailsBean.getReAssignTo())) {
                                List<String> old_observer_array = new ArrayList<String>();
                                if (old_observers.contains(",")) {
                                    old_observer_array.addAll(Arrays.asList(old_observers.split(",")));
                                } else {
                                    old_observer_array.add(old_observers);
                                }
                                String new_observers = null;
                                for (int i = 0; i < old_observer_array.size(); i++) {
                                    if (!old_observer_array.get(i).equalsIgnoreCase(taskDetailsBean.getReAssignTo())) {
                                        if (new_observers == null) {
                                            new_observers = old_observer_array.get(i);
                                        } else {
                                            new_observers = new_observers + "," + old_observer_array.get(i);
                                        }
                                    }
                                }

                                if (new_observers != null) {
                                    cv.put("taskObservers", new_observers);
                                }

                            }
                            cv.put("taskMemberList", taskDetailsBean.getReAssignTo());
                            String update_query = "projectId='" + taskDetailsBean.getProjectId() + "' and taskId='" + taskDetailsBean.getTaskId() + "'";
                            VideoCallDataBase.getDB(context).updateContentValues(cv, update_query);
                            if (Appreference.context_table.containsKey("projecthistory")) {
                                ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                                if (projectHistory != null) {
                                    projectHistory.refresh();
                                }
                            }
                        }
                    }


                    if (Appreference.context_table.containsKey("taskcoversation")) {
                        NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                        //                newTaskConversation.notifyGroupNameOrMemberChanges(notification);
                        newTaskConversation.notifyGroupMemberAccess(notification);
                        Log.d("TaskHistory", "Value true *** ");
                    } else if (Appreference.context_table.containsKey("taskhistory")) {
                        TaskHistory taskHistory = (TaskHistory) Appreference.context_table.get("taskhistory");
                        if (taskHistory != null) {
                            Log.d("TaskHistory", "Value true refreshed-2 *** ");
                            taskHistory.refresh();
                        }
                        Log.i("Groupchat", "MA newuseradded ** " + notification.getGroup_name());
                        Log.i("Groupchat", "MA newGroupName ** " + notification.getMember_added());
                    } else if (Appreference.context_table.containsKey("alltasklist")) {
                        AllTaskList allTaskList = (AllTaskList) Appreference.context_table.get("alltasklist");
                        if (allTaskList != null) {
                            Log.d("TaskHistory", "Value true refreshed-2 *** ");
                            allTaskList.refresh();
                        }
                        Log.i("Groupchat", "MA newuseradded ** " + notification.getGroup_name());
                        Log.i("Groupchat", "MA newGroupName ** " + notification.getMember_added());
                    }
                    //                else if (Appreference.context_table.containsKey("projectfragment")) {
                    //                    ProjectsFragment projectfragment = (ProjectsFragment) Appreference.context_table.get("projectfragment");
                    //                    if (projectfragment != null) {
                    //                        Log.d("TaskHistory", "Value true refreshed-2 *** ");
                    //                        projectfragment.refresh();
                    //                    }
                    //                    Log.i("Groupchat", "MA newuseradded ** " + notification.getGroup_name());
                    //                    Log.i("Groupchat", "MA newGroupName ** " + notification.getMember_added());
                    //                }
                    else {
                        ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
                        if (contactsFragment != null) {
                            contactsFragment.refresh();
                        }
                        Log.d("e", "Value true refreshed-2 else  *** ");
                        Log.i("Groupchat", "MA newuseradded **# " + notification.getGroup_name());
                        Log.i("Groupchat", "MA newGroupName **# " + notification.getMember_added());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MainActivity", "notifyChat_Received Exception: " + e.getMessage(), "WARN", null);
                }
            } else if (message.contains("<TaskDetailsinfo>")) {
                try {

                  /*  if (message.contains("<TaskDetailsinfo>")) {
                        return;
                    } else {

                    }*/
                    xmlparser xmlParser = new xmlparser();
                    Log.i("Chat Message", "Received " + message);
                    //            TaskDetailsBean detailsBeanForRemind = null;
                    TaskDetailsBean taskDetailsBean = xmlParser
                            .parseTaskDetailsSIPMessage(message);

                    taskDetailsBean.setTaskPlannedLatestEndDate("1");
                    if (taskDetailsBean.getProjectId() != null && taskDetailsBean.getTaskId() != null) {
                        Log.i("notifyreceived", "status ==> $ " + Appreference.context_table.containsKey("projecthistory"));
                        Log.i("notifyreceived", "status ==> $ " + Appreference.context_table.containsKey("taskcoversation"));
                        if (Appreference.context_table.containsKey("projecthistory") && Appreference.context_table.containsKey("taskcoversation")) {
                            NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                            final ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                            if (newTaskConversation != null) {
                                Log.i("notifyreceived", "status ==  ");
                                if (newTaskConversation.webtaskId != null && newTaskConversation.projectId != null) {
                                    if (taskDetailsBean.getTaskId().equalsIgnoreCase(newTaskConversation.webtaskId) &&
                                            taskDetailsBean.getProjectId().equalsIgnoreCase(newTaskConversation.projectId)) {
                                        Log.i("notifyreceived123", "status " + taskDetailsBean.getTaskDescription());
                                        if (taskDetailsBean.getTaskDescription() != null) {
                                            if (taskDetailsBean.getTaskDescription().equalsIgnoreCase("Task is Started") ||
                                                    taskDetailsBean.getTaskDescription().equalsIgnoreCase("Task is hold") ||
                                                    taskDetailsBean.getTaskDescription().equalsIgnoreCase("Task is Resumed") ||
                                                    taskDetailsBean.getTaskDescription().equalsIgnoreCase("Task is Restarted") ||
                                                    taskDetailsBean.getTaskDescription().equalsIgnoreCase("Task is Paused") ||
                                                    taskDetailsBean.getTaskDescription().equalsIgnoreCase("Task is Completed") ||
                                                    taskDetailsBean.getMimeType().equalsIgnoreCase("assigntask") ||
                                                    taskDetailsBean.getTaskDescription().equalsIgnoreCase("Gathering Details...") ||
                                                    taskDetailsBean.getTaskDescription().equalsIgnoreCase("Hold Remarks :") ||
                                                    taskDetailsBean.getTaskDescription().equalsIgnoreCase("Pause Remarks :") ||
                                                    taskDetailsBean.getTaskDescription().contains("Completed Percentage") ||
                                                    taskDetailsBean.getSubType().equalsIgnoreCase("deassign")) {
                                                if (projectHistory != null) {
                                                    if (projectHistory.projectDetailsBeans != null && projectHistory.projectDetailsBeans.size() > 0 && projectHistory.buddyArrayAdapter != null) {
                                                        ProjectDetailsBean projectDetailsBean = projectHistory.projectDetailsBeans.get(projectHistory.ListViewCurrentPosition);
                                                        Log.i("notifyreceived123", "getTaskStatus ==>  " + taskDetailsBean.getTaskStatus());
                                                        Log.i("notifyreceived123", "getCompletedPercentage ==>  " + taskDetailsBean.getCompletedPercentage());
                                                        if (taskDetailsBean.getCompletedPercentage() != null && !taskDetailsBean.getCompletedPercentage().equalsIgnoreCase(""))
                                                            projectDetailsBean.setCompletedPercentage(taskDetailsBean.getCompletedPercentage());
                                                        if (taskDetailsBean.getTaskStatus() != null && !taskDetailsBean.getTaskStatus().equalsIgnoreCase(""))
                                                            projectDetailsBean.setTaskStatus(taskDetailsBean.getTaskStatus());
                                                        if ((taskDetailsBean.getMimeType() != null && taskDetailsBean.getMimeType().equalsIgnoreCase("assigntask")) &&
                                                                (taskDetailsBean.getTaskReceiver() != null && !taskDetailsBean.getTaskReceiver().equalsIgnoreCase(""))) {
                                                            projectDetailsBean.setTaskReceiver(taskDetailsBean.getTaskReceiver());
                                                            projectDetailsBean.setCatagory("Task");
                                                        } else if (taskDetailsBean.getSubType() != null && taskDetailsBean.getSubType().equalsIgnoreCase("deassign")) {
                                                            projectDetailsBean.setCatagory("Template");
                                                        }
                                                        handler1.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                projectHistory.buddyArrayAdapter.notifyDataSetChanged();
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (Appreference.context_table.containsKey("projecthistory") && Appreference.context_table.containsKey("traveljobdetails")) {
                            TravelJobDetails travelJobDetails = (TravelJobDetails) Appreference.context_table.get("traveljobdetails");
                            final ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                            if (travelJobDetails != null) {
                                Log.i("notifyreceived123", "traveljobdetails status ##  == else  ");
                                if (travelJobDetails.webtaskId != null && travelJobDetails.projectId != null) {
                                    if (taskDetailsBean.getTaskId().equalsIgnoreCase(travelJobDetails.webtaskId) &&
                                            taskDetailsBean.getProjectId().equalsIgnoreCase(travelJobDetails.projectId)) {
                                        Log.i("notifyreceived123", "traveljobdetails status $$ else " + taskDetailsBean.getTaskDescription());
                                        if (taskDetailsBean.getTaskDescription() != null) {
                                            if (taskDetailsBean.getTaskDescription().equalsIgnoreCase("Task is Started") ||
                                                    taskDetailsBean.getTaskDescription().equalsIgnoreCase("Task is Completed") ||
                                                    taskDetailsBean.getTaskDescription().equalsIgnoreCase("Gathering Details...") ||
                                                    taskDetailsBean.getTaskDescription().equalsIgnoreCase("Hold Remarks :") ||
                                                    taskDetailsBean.getTaskDescription().equalsIgnoreCase("Pause Remarks :") ||
                                                    taskDetailsBean.getMimeType().equalsIgnoreCase("assigntask") ||
                                                    taskDetailsBean.getTaskDescription().contains("Completed Percentage") ||
                                                    taskDetailsBean.getSubType().equalsIgnoreCase("deassign")) {
                                                if (projectHistory != null) {
                                                    if (projectHistory.projectDetailsBeans != null && projectHistory.projectDetailsBeans.size() > 0 && projectHistory.buddyArrayAdapter != null) {
                                                        ProjectDetailsBean projectDetailsBean = projectHistory.projectDetailsBeans.get(projectHistory.ListViewCurrentPosition);
                                                        Log.i("notifyreceived123", "traveljobdetails getTaskStatus==> else  " + taskDetailsBean.getTaskStatus());
                                                        Log.i("notifyreceived123", "traveljobdetails getCompletedPercentage==> else  " + taskDetailsBean.getCompletedPercentage());
                                                        if (taskDetailsBean.getCompletedPercentage() != null && !taskDetailsBean.getCompletedPercentage().equalsIgnoreCase(""))
                                                            projectDetailsBean.setCompletedPercentage(taskDetailsBean.getCompletedPercentage());
                                                        if (taskDetailsBean.getTaskStatus() != null && !taskDetailsBean.getTaskStatus().equalsIgnoreCase(""))
                                                            projectDetailsBean.setTaskStatus(taskDetailsBean.getTaskStatus());
                                                        if ((taskDetailsBean.getMimeType() != null && taskDetailsBean.getMimeType().equalsIgnoreCase("assigntask")) &&
                                                                (taskDetailsBean.getTaskReceiver() != null && !taskDetailsBean.getTaskReceiver().equalsIgnoreCase(""))) {
                                                            projectDetailsBean.setTaskReceiver(taskDetailsBean.getTaskReceiver());
                                                            projectDetailsBean.setCatagory("Task");
                                                        } else if (taskDetailsBean.getSubType() != null && taskDetailsBean.getSubType().equalsIgnoreCase("deassign")) {
                                                            projectDetailsBean.setCatagory("Template");
                                                        }
                                                        handler1.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                projectHistory.buddyArrayAdapter.notifyDataSetChanged();
                                                            }
                                                        });
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Log.i("Main--->", " Main 4 " + taskDetailsBean.getTaskDescription());
                    if (taskDetailsBean.getTaskTagName() != null && taskDetailsBean.getTaskTagName().equalsIgnoreCase("Conference Start Time")) {
                        Log.i("Main--->", " Main 5 " + taskDetailsBean.getTaskDescription());
                        taskDetailsBean.setTaskDescription(UTCToLocalTime(taskDetailsBean.getTaskDescription()));
                        startScheduleManager(taskDetailsBean);
                    }

                    if (taskDetailsBean.getSubType() != null && (taskDetailsBean.getSubType().equalsIgnoreCase("attendance_in") || taskDetailsBean.getSubType().equalsIgnoreCase("attendance_out") || taskDetailsBean.getSubType().equalsIgnoreCase("rate") || taskDetailsBean.getSubType().equalsIgnoreCase("work") || taskDetailsBean.getSubType().equalsIgnoreCase("overtime") || taskDetailsBean.getSubType().equalsIgnoreCase("m/c_breakdown"))) {
                        taskDetailsBean.setMsg_status(10);
                    }

                    Log.i("Chat Message", "task category " + taskDetailsBean.getCatagory());

                    // create Reminder Bean for first Entry in client side

                    /*if (taskDetailsBean.getTaskRequestType() != null && taskDetailsBean.getTaskRequestType().equalsIgnoreCase("taskDateChangedApproval")) {
        //                if(taskDetailsBean.getPlannedStartDateTime() != null && taskDetailsBean.getPlannedStartDateTime().equalsIgnoreCase("")){
                        try {
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Date date = dateFormat.parse(taskDetailsBean.getPlannedStartDateTime());
                            Date date1 = dateFormat.parse(taskDetailsBean.getDateTime());
                            Log.d("CustomReminder", "planned start Date == " + date);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);
                            long t = cal.getTimeInMillis();
                            Date afterAddingOneMins = new Date(t + (1 * 60000));
                            Date afterMinusOneMins = new Date(t - (1 * 60000));
                            Log.d("CustomReminder", "planned start Date afterAddingTenMins == " + afterAddingOneMins);
                            Log.d("CustomReminder", "planned start Date afterMinusOneMins == " + afterMinusOneMins);
                            if (date.equals(date1) || afterAddingOneMins.equals(date1) || afterMinusOneMins.equals(date1)) {
                                detailsBeanForRemind = new TaskDetailsBean();
                                detailsBeanForRemind.setTaskDescription(taskDetailsBean.getReminderQuote());

                                detailsBeanForRemind.setTaskNo(taskDetailsBean.getTaskNo());
                                if (taskDetailsBean.getTaskType().equalsIgnoreCase("group")) {
                                    Gson gson = new Gson();
                                    String json = appSharedpreferences.getString("loginuserdetails");
                                    Loginuserdetails loginuserdetails = gson.fromJson(json, Loginuserdetails.class);

                                    detailsBeanForRemind.setFromUserName(loginuserdetails.getUsername());
                                    detailsBeanForRemind.setFromUserId(String.valueOf(loginuserdetails.getId()));
                                } else {
                                    detailsBeanForRemind.setFromUserName(taskDetailsBean.getToUserName());
                                    detailsBeanForRemind.setFromUserId(taskDetailsBean.getToUserId());
                                }
                                detailsBeanForRemind.setToUserName(taskDetailsBean.getFromUserName());
                                detailsBeanForRemind.setToUserId(taskDetailsBean.getFromUserId());
                                detailsBeanForRemind.setTaskReceiver(taskDetailsBean.getTaskReceiver());
                                detailsBeanForRemind.setReminderQuote(taskDetailsBean.getReminderQuote());
                                detailsBeanForRemind.setMimeType("text");
                                detailsBeanForRemind.setTaskStatus("reminder");
                                if (taskDetailsBean.getCatagory() != null) {
                                    detailsBeanForRemind.setCatagory(taskDetailsBean.getCatagory());
                                }

                                detailsBeanForRemind.setTaskName(taskDetailsBean.getTaskName());
                                detailsBeanForRemind.setTasktime(taskDetailsBean.getTasktime());
                                detailsBeanForRemind.setTaskUTCTime(taskDetailsBean.getTaskUTCTime());
                                detailsBeanForRemind.setDateTime(taskDetailsBean.getDateTime());
                                detailsBeanForRemind.setTaskUTCDateTime(taskDetailsBean.getTaskUTCDateTime());
                                detailsBeanForRemind.setPlannedStartDateTime(null);
                                detailsBeanForRemind.setUtcPlannedStartDateTime(Appreference.customLocalDateToUTC(null));
                                detailsBeanForRemind.setPlannedEndDateTime(null);
                                detailsBeanForRemind.setUtcplannedEndDateTime(Appreference.customLocalDateToUTC(null));
                                detailsBeanForRemind.setDateFrequency("");
                                detailsBeanForRemind.setSignalid(Utility.getSessionID());

                                detailsBeanForRemind.setParentId(getFileName());
                                detailsBeanForRemind.setTaskPriority(taskDetailsBean.getTaskPriority());
                                detailsBeanForRemind.setCompletedPercentage(taskDetailsBean.getCompletedPercentage());
                                detailsBeanForRemind.setOwnerOfTask(taskDetailsBean.getOwnerOfTask());
                                detailsBeanForRemind.setCustomTagVisible(true);
                                Log.i("task", "webtaskId " + taskDetailsBean.getTaskId());
                                detailsBeanForRemind.setTaskId(taskDetailsBean.getTaskId());

                                detailsBeanForRemind.setRead_status(2);
                                detailsBeanForRemind.setMsg_status(1);
                                detailsBeanForRemind.setSendStatus("2");   // send status 0 is send 1 is unsend
                                detailsBeanForRemind.setTaskType(taskDetailsBean.getTaskType());
                                detailsBeanForRemind.setSubType("normal");

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
        //                    }
                        }

                    }*/

                    if (taskDetailsBean.getTaskReceiver() != null && taskDetailsBean.getTaskReceiver().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && taskDetailsBean.getTaskRequestType().equalsIgnoreCase("reassignTask")) {
                        Log.i("ChatMessage", "syncTask@@ ");
                        appSharedpreferences.saveBoolean("syncTask" + taskDetailsBean.getTaskId(), false);
                    }

                    Log.i("chat", "received " + taskDetailsBean.getMimeType());
                    if (taskDetailsBean.getMimeType().equalsIgnoreCase("image")) {
                        Appreference.taskMultimediaDownload.put(taskDetailsBean.getTaskDescription(), taskDetailsBean);
                        Log.i("profiledownload", "fileName--2 " + taskDetailsBean.getTaskDescription());
                        if (taskDetailsBean.getSubType() != null && taskDetailsBean.getSubType().equalsIgnoreCase("private")) {
                            Appreference.do_downloadForServiceManualPDF_Path=Environment.getExternalStorageDirectory()
                                    .getAbsolutePath()
                                    + "/High Message/downloads/";
                            new DownloadImage(getResources().getString(R.string.file_upload) + taskDetailsBean.getTaskDescription(), taskDetailsBean.getTaskDescription()).execute();
                        } else {
                            Log.i("task", "1 image" + taskDetailsBean.getTaskDescription());
                            Appreference.do_downloadForServiceManualPDF_Path=Environment.getExternalStorageDirectory()
                                    .getAbsolutePath()
                                    + "/High Message/downloads/";
                            new DownloadImage(getResources().getString(R.string.task_reminder) + taskDetailsBean.getTaskDescription(), taskDetailsBean.getTaskDescription()).execute();
                        }
                    } else if (taskDetailsBean.getMimeType().equalsIgnoreCase("sketch")) {
                        Appreference.taskMultimediaDownload.put(taskDetailsBean.getTaskDescription(), taskDetailsBean);
                        Log.i("profiledownload", "fileName--2 " + taskDetailsBean.getTaskDescription());
                        if (taskDetailsBean.getSubType() != null && taskDetailsBean.getSubType().equalsIgnoreCase("private")) {
                            Appreference.do_downloadForServiceManualPDF_Path=Environment.getExternalStorageDirectory()
                                    .getAbsolutePath()
                                    + "/High Message/downloads/";
                            new DownloadImage(getResources().getString(R.string.file_upload) + taskDetailsBean.getTaskDescription(), taskDetailsBean.getTaskDescription()).execute();
                        } else {
                            Log.i("task", "2 image" + taskDetailsBean.getTaskDescription());
                            Appreference.do_downloadForServiceManualPDF_Path=Environment.getExternalStorageDirectory()
                                    .getAbsolutePath()
                                    + "/High Message/downloads/";
                            new DownloadImage(getResources().getString(R.string.task_reminder) + taskDetailsBean.getTaskDescription(), taskDetailsBean.getTaskDescription()).execute();
                        }
                    } else if (taskDetailsBean.getMimeType().equalsIgnoreCase("video") || taskDetailsBean.getMimeType().equalsIgnoreCase("audio") || taskDetailsBean.getMimeType().equalsIgnoreCase("task") || taskDetailsBean.getMimeType().equalsIgnoreCase("document") || taskDetailsBean.getMimeType().equalsIgnoreCase("pdf") || taskDetailsBean.getMimeType().equalsIgnoreCase("txt")) {
                        Log.i("profiledownload", "fileName--2 " + taskDetailsBean.getTaskDescription());
                        Log.d("task", "video fileName" + taskDetailsBean.getTaskDescription());
                        Log.d("task", "video fileName" + taskDetailsBean.getTaskDescription().split("\\.")[1]);
                        if (taskDetailsBean.getTaskDescription().split("\\.")[1].equalsIgnoreCase("caf")) {
                            Log.d("task", "video fileName" + taskDetailsBean.getTaskDescription().split("\\.")[1].equalsIgnoreCase("caf"));
                            String audioName = taskDetailsBean.getTaskDescription().split("\\.")[0] + ".mp3";
                            taskDetailsBean.setTaskDescription(audioName);
                            Appreference.taskMultimediaDownload.put(taskDetailsBean.getTaskDescription(), taskDetailsBean);
                        } else {
                            Appreference.taskMultimediaDownload.put(taskDetailsBean.getTaskDescription(), taskDetailsBean);
                        }
                        if (taskDetailsBean.getSubType() != null && taskDetailsBean.getSubType().equalsIgnoreCase("private")) {
                            Appreference.do_downloadForServiceManualPDF_Path=Environment.getExternalStorageDirectory()
                                    .getAbsolutePath()
                                    + "/High Message/downloads/";
                            new DownloadImage(getResources().getString(R.string.file_upload) + taskDetailsBean.getTaskDescription(), taskDetailsBean.getTaskDescription()).execute();
                        } else {
                            Log.i("task", "3 image" + taskDetailsBean.getTaskDescription());
                            new DownloadVideo(getResources().getString(R.string.task_reminder) + taskDetailsBean.getTaskDescription()).execute();
                        }

                    } else if ((taskDetailsBean.getMimeType().equalsIgnoreCase("date")) && (taskDetailsBean.getTaskDescription() != null && !taskDetailsBean.getTaskDescription().equalsIgnoreCase(null) && !taskDetailsBean.getTaskDescription().equalsIgnoreCase(""))) {
                        if (taskDetailsBean.getTaskDescription().contains(".mp3") || taskDetailsBean.getTaskDescription().contains(".wav")) {
                            Log.d("task", "date fileName" + taskDetailsBean.getTaskDescription());
                            Appreference.taskMultimediaDownload.put(taskDetailsBean.getTaskDescription(), taskDetailsBean);
                            new DownloadVideo(getResources().getString(R.string.task_reminder) + taskDetailsBean.getTaskDescription()).execute();
                        }
                    } else if (taskDetailsBean.getMimeType().equalsIgnoreCase("textfile")) {
                        Log.d("task", "video fileName" + taskDetailsBean.getTaskDescription());
                        Log.d("task", "video fileName" + taskDetailsBean.getTaskDescription().split("\\.")[1]);
                        if (taskDetailsBean.getTaskDescription().contains(".txt")) {
                            Log.d("task", "date fileName" + taskDetailsBean.getTaskDescription());
                            Appreference.taskMultimediaDownload.put(taskDetailsBean.getTaskDescription(), taskDetailsBean);
                            new DownloadVideo(getResources().getString(R.string.task_reminder) + taskDetailsBean.getTaskDescription()).execute();
                        }
                        /*if (taskDetailsBean.getTaskDescription().split("\\.")[1].equalsIgnoreCase("caf")) {
                            Log.d("task", "video fileName" + taskDetailsBean.getTaskDescription().split("\\.")[1].equalsIgnoreCase("caf"));
                            String audioName = taskDetailsBean.getTaskDescription().split("\\.")[0] + ".mp3";
                            taskDetailsBean.setTaskDescription(audioName);
                            Appreference.taskMultimediaDownload.put(taskDetailsBean.getTaskDescription(), taskDetailsBean);
                        } else {
                            Appreference.taskMultimediaDownload.put(taskDetailsBean.getTaskDescription(), taskDetailsBean);
                        }
                        if (taskDetailsBean.getSubType() != null && taskDetailsBean.getSubType().equalsIgnoreCase("private")) {
                            new DownloadImage(getResources().getString(R.string.file_upload) + taskDetailsBean.getTaskDescription()).execute();
                        } else {
                            new DownloadVideo(getResources().getString(R.string.task_reminder) + taskDetailsBean.getTaskDescription()).execute();
                        }*/

                    }
                    if (taskDetailsBean.getTaskRequestType() != null && taskDetailsBean.getTaskRequestType().equalsIgnoreCase("buzzrequest")) {
                        startAlarmRingTone();
                        Log.d("Buzz", "Alarm played");
                    }
                    taskDetailsBean.setFromUserEmail(FromUri);
                    if (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("rejected")) {
                        if (Appreference.context_table.containsKey("taskcoversation")) {
                            NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                            newTaskConversation.counter_gone();
                        }
                        VideoCallDataBase.getDB(context).updatereject(taskDetailsBean.getTaskId());
                        Log.i("Mainactivity", "Task Reminder");
                    }/*else if (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("assigned")) {
                        VideoCallDataBase.getDB(context).updateaccept(taskDetailsBean.getTaskId());
        //                newTaskConversation.counter_gone();
                        Log.i("assigned","Task Reminder");
                    }*/
                    if (taskDetailsBean.getTaskDescription() != null && taskDetailsBean.getTaskDescription().equalsIgnoreCase("Task Name is Changed")) {
                        VideoCallDataBase.getDB(context).templateNameUpdate(taskDetailsBean.getTaskName(), taskDetailsBean.getTaskId());
                    }

                    if ((taskDetailsBean.getTaskObservers() != null && !taskDetailsBean.getTaskObservers().equalsIgnoreCase("") && !taskDetailsBean.getTaskObservers().equalsIgnoreCase("null")) && taskDetailsBean.getTaskObservers().contains(Appreference.loginuserdetails.getUsername())) {
                        appSharedpreferences.saveBoolean("syncTask" + taskDetailsBean.getTaskId(), false);
                        Log.i("Mainactivity", "appSharedpreferences.saveBoolean" + appSharedpreferences.getBoolean("syncTask" + taskDetailsBean.getTaskId()));
                    }

                    //            if (!VideoCallDataBase.getDB(context).DuplicatetaskTaskIdChecker(taskDetailsBean.getTaskId())) {
                    if (!taskDetailsBean.getMimeType().equalsIgnoreCase("Reassign")) {
                        //                    appSharedpreferences.saveBoolean("syncTask" + taskDetailsBean.getTaskId(), true);
                    } else {
                        String query1 = "select * from taskHistoryInfo where loginuser ='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + taskDetailsBean.getTaskId() + "' order by id";

                        Log.d("TaskObserver", "get Observer query $$ " + query1);

                        ArrayList<TaskDetailsBean> arrayList;
                        arrayList = VideoCallDataBase.getDB(context).getTaskHistoryInfo(query1);

                        Log.d("TaskObserver", "Task Observer list size is == $$ " + arrayList.size());
                        if (arrayList.size() > 0) {
                            TaskDetailsBean taskDetailsBean1 = arrayList.get(0);

                            String taskObservers = taskDetailsBean1.getTaskObservers();
                            int counter = 0;
                            Log.d("TaskObserver", "Task Observer  == $$ " + taskObservers);
                            if (taskObservers != null) {

                                for (int i = 0; i < taskObservers.length(); i++) {
                                    if (taskObservers.charAt(i) == ',') {
                                        counter++;
                                    }
                                }

                                ArrayList<String> listOfObservers = new ArrayList<>();

                                for (int j = 0; j < counter + 1; j++) {
                                    if (counter == 0) {
                                        if (!taskObservers.equalsIgnoreCase(taskDetailsBean.getTaskReceiver())) {
                                            listOfObservers.add(taskObservers);
                                            Log.i("conversation", "Final observer if " + listOfObservers);
                                        }
                                    } else {
                                        if (!taskObservers.split(",")[j].equalsIgnoreCase(taskDetailsBean.getTaskReceiver())) {
                                            listOfObservers.add(taskObservers.split(",")[j]);
                                            Log.i("conversation", "Final observer else " + listOfObservers);
                                        }
                                    }
                                }

                                String name = "";
                                for (String s : listOfObservers) {
                                    name = name.concat(s) + ",";
                                }
                                Log.i("conversation", "name @@ " + name);
                                if (name != null && !name.equalsIgnoreCase("") && name.contains(","))
                                    name = name.substring(0, name.length() - 1);
                                Log.i("conversation", "name @@@ " + name);
                                VideoCallDataBase.getDB(context).updateaccept("update taskHistoryInfo set taskObservers='" + name + "' where taskId='" + taskDetailsBean.getTaskId() + "'");
//                                if (name != null && !name.equalsIgnoreCase("") && !name.equalsIgnoreCase("null") && !name.equalsIgnoreCase(null) && !name.contains(Appreference.loginuserdetails.getUsername()) && !taskDetailsBean.getTaskReceiver().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
//                                    VideoCallDataBase.getDB(context).updateaccept("update taskHistoryInfo set taskObservers='" + name + "' where taskId='" + taskDetailsBean.getTaskId() + "'");
//                                    Log.i("conversation", "name ## @$$ " + name);
//                                }
                                Log.i("mainactivity", "taskObservers updated $$ " + name);

                            }
                        }
                    }
                    if ((taskDetailsBean.getTaskObservers() != null && !taskDetailsBean.getTaskObservers().equalsIgnoreCase("") && !taskDetailsBean.getTaskObservers().equalsIgnoreCase("null")) || (taskDetailsBean.getRejectedObserver() != null && !taskDetailsBean.getRejectedObserver().equalsIgnoreCase("") && !taskDetailsBean.getRejectedObserver().equalsIgnoreCase("null"))) {
                        String query2 = "";
                        ArrayList<TaskDetailsBean> arrayList;
                        if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase("null")) {
                            query2 = "select * from projectHistory where loginuser ='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + taskDetailsBean.getTaskId() + "' order by id";
                            arrayList = VideoCallDataBase.getDB(context).getProjectHistoryTasks(query2);
                        } else {
                            query2 = "select * from taskHistoryInfo where loginuser ='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + taskDetailsBean.getTaskId() + "' order by id";
                            arrayList = VideoCallDataBase.getDB(context).getTaskHistoryInfo(query2);
                        }

                        Log.d("TaskObserver", "get Observer query  " + query2);
                        Log.d("TaskObserver", "Task Observer list size is == " + arrayList.size());
                        ArrayList<String> listOfObsExist = new ArrayList<>();
                        ArrayList<String> listOfObsxml = new ArrayList<>();
                        if (arrayList.size() > 0) {
                            TaskDetailsBean taskDetailsBean1 = arrayList.get(0);
                            String taskObservers = taskDetailsBean1.getTaskObservers();
                            Log.d("TaskObserver", "Task Observer  ==> " + taskObservers);
                            if (taskObservers != null && !taskObservers.equalsIgnoreCase("") && !taskObservers.equalsIgnoreCase("null")) {
                                int counter = 0;
                                for (int i = 0; i < taskObservers.length(); i++) {
                                    if (taskObservers.charAt(i) == ',') {
                                        counter++;
                                    }
                                }
                                for (int j = 0; j < counter + 1; j++) {
                                    if (counter == 0) {
                                        listOfObsExist.add(taskObservers);
                                    } else {
                                        listOfObsExist.add(taskObservers.split(",")[j]);
                                        Log.d("TaskObserver", "Task Observer name not in same user== " + taskObservers.split(",")[j]);
                                    }
                                }
                                Log.d("TaskObserver", "Task Observer  == " + taskDetailsBean.getTaskObservers());
                                if (taskDetailsBean.getTaskObservers() != null) {
                                    int counter_1 = 0;
                                    for (int i = 0; i < taskDetailsBean.getTaskObservers().length(); i++) {
                                        if (taskDetailsBean.getTaskObservers().charAt(i) == ',') {
                                            counter_1++;
                                        }
                                    }
                                    for (int j = 0; j < counter_1 + 1; j++) {
                                        if (counter_1 == 0) {
                                            listOfObsxml.add(taskDetailsBean.getTaskObservers());
                                        } else {
                                            listOfObsxml.add(taskDetailsBean.getTaskObservers().split(",")[j]);
                                            Log.d("TaskObserver", "Task Observer name not in same user== " + taskDetailsBean.getTaskObservers().split(",")[j]);
                                        }
                                    }
                                }
                                String addObserv = "";
                                for (String obsNew : listOfObsExist) {
                                    if (!listOfObsxml.contains(obsNew)) {
                                        listOfObsxml.add(obsNew);
                                    }
                                }


                                // removed obseervers  removed from listOfObsxml list

                                if (taskDetailsBean.getRejectedObserver() != null) {
                                    int counter_2 = 0;
                                    for (int i = 0; i < taskDetailsBean.getRejectedObserver().length(); i++) {
                                        if (taskDetailsBean.getRejectedObserver().charAt(i) == ',') {
                                            counter_2++;
                                        }
                                    }
                                    for (int j = 0; j < counter_2 + 1; j++) {
                                        if (counter_2 == 0) {
                                            if (listOfObsxml.contains(taskDetailsBean.getRejectedObserver()))
                                                listOfObsxml.remove(taskDetailsBean.getRejectedObserver());
                                        } else {
                                            if (listOfObsxml.contains(taskDetailsBean.getRejectedObserver().split(",")[j]))
                                                listOfObsxml.remove(taskDetailsBean.getRejectedObserver().split(",")[j]);
                                            Log.d("TaskObserver", "Task Observer name not in same user== " + taskDetailsBean.getRejectedObserver().split(",")[j]);
                                        }
                                    }
                                }
//
                                Log.d("TaskObserver", "Task Observer name not in same user== " + listOfObsxml);


                                if (listOfObsxml != null && listOfObsxml.size() > 0) {
                                    for (String string_1 : listOfObsxml) {
                                        if (!addObserv.contains(string_1))
                                            addObserv = addObserv.concat(string_1) + ",";
                                    }
                                    addObserv = addObserv.substring(0, addObserv.length() - 1);
                                    taskDetailsBean.setTaskObservers(addObserv);
                                    Log.i("Mainactivity", "Added observers if " + taskDetailsBean.getTaskObservers());
                                } else {
                                    taskDetailsBean.setTaskObservers("");
                                }
                            } else {
                                if (taskDetailsBean.getToUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    taskDetailsBean.setTaskReceiver(taskDetailsBean.getToUserName());
                                }
                                Log.i("Mainactivity", "Added observers else " + taskDetailsBean.getTaskObservers());
                            }
                        }
                    }
                    if (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("template")) {
                        taskDetailsBean.setCustomTagVisible(false);
                    }
                    //            }
                    Log.i("Mainactivity", "syncTask" + appSharedpreferences.getBoolean("syncTask" + taskDetailsBean.getTaskId()));
                    if (Appreference.context_table.containsKey("taskcoversation") && taskDetailsBean.getCatagory() != null && !taskDetailsBean.getCatagory().equalsIgnoreCase("chat")) {
                        Log.i("Mainactivity", "taskcoversation reference ");

                        if (taskDetailsBean.getRejectedObserver() != null && !taskDetailsBean.getRejectedObserver().trim().equals("") && !taskDetailsBean.getRejectedObserver().equalsIgnoreCase(null) && taskDetailsBean.getRejectedObserver().length() > 4) {

                            String name = taskDetailsBean.getRejectedObserver();
                            Log.i("observer", name + "    " + Appreference.loginuserdetails.getUsername());
                            if (name.contains(Appreference.loginuserdetails.getUsername())) {
                                Log.i("taskobserver", "was removed " + name);
                                Log.i("taskobserver", "was removed ** " + Appreference.loginuserdetails.getUsername());
                                taskDetailsBean.setYouRemoved(true);
                            }

                        }

                        NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                        Log.i("Mainactivity", "fileName--2 " + taskDetailsBean.getTaskDescription());
                        if (newTaskConversation != null) {
                            String taskMemberList = "select taskMemberList from projectHistory where projectId='" + taskDetailsBean.getProjectId() + "' and taskId='" + taskDetailsBean.getTaskId() + "'";
                            String taskMemberList_1 = VideoCallDataBase.getDB(context).getValuesForQuery(taskMemberList);
                            Log.i("MainActivity", "notifytaskReceived taskMemberList_2 ----------> " + taskMemberList_1);
                            newTaskConversation.notifyTaskReceived(taskDetailsBean);
                        }
                        /*if (detailsBeanForRemind != null) {
                            newTaskConversation.notifyTaskReceived(detailsBeanForRemind);
                        }*/
                        Log.d("Mainactivity", "Value true ");
                    } else if (Appreference.context_table.containsKey("traveljobdetails")) {
                        Log.i("taskobserver", "traveljobdetails $$ ---> " + taskDetailsBean.getTaskId());
                        Log.i("taskobserver", "traveljobdetails.getTaskId()##  ---> " + taskDetailsBean.getTaskId());
                        TravelJobDetails travelJobDetails = (TravelJobDetails) Appreference.context_table.get("traveljobdetails");
                        travelJobDetails.notifyTaskReceived(taskDetailsBean);
                    } else if (Appreference.context_table.containsKey("taskhistory") && taskDetailsBean.getCatagory() != null && !taskDetailsBean.getCatagory().equalsIgnoreCase("chat")) {
                        Log.i("Mainactivity", "taskhistory reference ");
                        if (!VideoCallDataBase.getDB(context).DuplicateChecker(taskDetailsBean.getSignalid(), taskDetailsBean.getTaskId()) || (taskDetailsBean.getTaskRequestType() != null && taskDetailsBean.getTaskRequestType().equalsIgnoreCase("customeAttributeRequest"))) {
                            Log.d("Mainactivity", "Value true 1");
                            Log.i("Mainactivity", "isSignalidSame " + isSignalidSame);
                            updateDateRequestIcon(taskDetailsBean);
                            updateOverdueMsg(taskDetailsBean);
//                            updateOverDueDateRequestorApprovelIcon(taskDetailsBean);
                            updateLeaveRequest_icon(taskDetailsBean);
                            updateLongMessage(taskDetailsBean);
                            if (!isSignalidSame) {
                                Log.d("Mainactivity", "Value true 2");
                                taskDetailsBean.setRead_status(1);
                                if (taskDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
                                    if (taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                        if (taskDetailsBean.getTaskDescription().contains("Completed Percentage ") && (!taskDetailsBean.getTaskDescription().contains("Completed Percentage 100%") || !taskDetailsBean.getTaskDescription().contains("Completed Percentage 100"))) {
                                            taskDetailsBean.setTaskStatus("inprogress");
                                        }
                                    } else if (!taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && taskDetailsBean.getFromUserName().equalsIgnoreCase(taskDetailsBean.getOwnerOfTask())) {
                                        if (taskDetailsBean.getTaskDescription().contains("Completed Percentage ") && (!taskDetailsBean.getTaskDescription().contains("Completed Percentage 100%") || !taskDetailsBean.getTaskDescription().contains("Completed Percentage 100"))) {
                                            taskDetailsBean.setTaskStatus("inprogress");
                                        }
                                    }
                                    Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() group if " + taskDetailsBean.getTaskStatus());


                                    if (taskDetailsBean.getTaskStatus().equalsIgnoreCase("closed")) {
                                        VideoCallDataBase.getDB(context).updateGroupCloseTaskStatus(taskDetailsBean.getTaskId(), taskDetailsBean.getCompletedPercentage());
                                    } else {
                                        Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() else if  " + taskDetailsBean.getFromUserName() + "Task Owner name" + taskDetailsBean.getOwnerOfTask());

                                        if (taskDetailsBean.getFromUserName().equalsIgnoreCase(taskDetailsBean.getOwnerOfTask()) || taskDetailsBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                            if (taskDetailsBean.getTaskDescription().contains("Completed Percentage")) {
                                                percentage = taskDetailsBean.getCompletedPercentage();
                                                Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() else if ->  " + percentage);
                                            } else {
                                                percentage = VideoCallDataBase.getDB(context).getlastCompletedParcentage(taskDetailsBean.getTaskId());
                                                if (percentage != null && !percentage.equalsIgnoreCase(null) && !percentage.equalsIgnoreCase("") && !percentage.equalsIgnoreCase("null")) {
                                                    taskDetailsBean.setCompletedPercentage(percentage);
                                                }
                                                Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() else if --> " + percentage);
                                            }
                                        } else {
                                            if (!taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                                percentage = VideoCallDataBase.getDB(context).getlastCompletedParcentage(taskDetailsBean.getTaskId());
                                                if (percentage != null && !percentage.equalsIgnoreCase(null) && !percentage.equalsIgnoreCase("") && !percentage.equalsIgnoreCase("null")) {
                                                    taskDetailsBean.setCompletedPercentage(percentage);
                                                }
                                                Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() else if ---> " + percentage);
                                                if (taskDetailsBean.getTaskStatus().equalsIgnoreCase("Completed")) {
                                                    taskList_12 = new ArrayList<>();
                                                    taskList_12 = VideoCallDataBase.getDB(context).getTaskHistory("select * from taskDetailsInfo where fromUserName='" + Appreference.loginuserdetails.getUsername() + "' and taskId='" + taskDetailsBean.getTaskId() + "' and taskDescription = 'Completed Percentage 100%' order by id desc");
                                                    if (taskList_12.size() > 0) {
                                                        String completed_value = taskList_12.get(0).getCompletedPercentage();
                                                        if (completed_value.equalsIgnoreCase("100") || completed_value.equalsIgnoreCase("100%")) {
                                                            taskStatus = "Completed";
                                                            Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() notify 12 " + taskStatus);
                                                        } else {
                                                            taskStatus = "inprogress";
                                                            Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() notify 13 " + taskStatus);
                                                        }
                                                        taskDetailsBean.setTaskStatus(taskStatus);
                                                        Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() inside taker-group taskstatus is " + taskStatus);
                                                    } else {
                                                        taskDetailsBean.setTaskStatus("inprogress");
                                                        Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() not taker-group taskstatus is " + taskStatus);
                                                    }
                                                }
                                            }
                                        }
                                        Log.i("Mainactivity", "db update 1" + taskDetailsBean.getCompletedPercentage());
                                        Log.i("Mainactivity", "db update 1" + taskDetailsBean.getTaskStatus());
                                        Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() else else  " + taskDetailsBean.getTaskStatus());
                                        if (!taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                            //                                        VideoCallDataBase.getDB(context).updateGroupTaskStatus(taskDetailsBean.getTaskId(), taskDetailsBean.getCompletedPercentage());
                                            //                                        Log.i("conversation", "taskDetailsBean.getTaskStatus() else if ----> " + percentage);
                                        } else {
                                            if (taskDetailsBean.getTaskStatus().equalsIgnoreCase("Completed")) {
                                                if (Appreference.loginuserdetails.getUsername().equalsIgnoreCase(taskDetailsBean.getOwnerOfTask())) {
                                                    groupOwnerPercentCalculation(taskDetailsBean);
                                                    Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() isGrp_Percent " + isGrp_Percent);
                                                    if (isGrp_Percent) {
                                                        taskDetailsBean.setTaskStatus("Completed");
                                                        Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() final " + taskDetailsBean.getTaskStatus());
                                                    } else {
                                                        taskDetailsBean.setTaskStatus("inprogress");
                                                        Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() not final " + taskDetailsBean.getTaskStatus());
                                                    }
                                                }
                                            }
                                        }
                                        if (taskDetailsBean.getTaskDescription() != null && taskDetailsBean.getTaskDescription().equalsIgnoreCase("This task is overdue")) {
                                            taskDetailsBean.setTaskStatus("overdue");
                                            Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() else overdue???? " + taskDetailsBean.getTaskStatus());
                                        }

                                    }
                                }
                            }
                            Log.i("Mainactivity", "new task conversation 111");
                            if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("null") && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase("(null)")) {
                                Log.i("Mainactivity", "projectid 3");
//                                VideoCallDataBase.getDB(context).update_Project_history(taskDetailsBean);
//                                if (VideoCallDataBase.getDB(context).DuplicateProjectTaskIdChecker(taskDetailsBean.getTaskId())) {
                                VideoCallDataBase.getDB(context).insert_new_Project_history(taskDetailsBean);
                                VideoCallDataBase.getDB(context).updateBadgeStatus("1", taskDetailsBean.getProjectId());

//                                }
                            } else {
                                Log.i("Mainactivity", "RejectedObserver " + taskDetailsBean.getRejectedObserver());
                                Log.i("Mainactivity", "TaskObservers " + taskDetailsBean.getTaskObservers());
                                Log.i("Main--->", " Main 6 " + taskDetailsBean.getTaskDescription());
                                dataBase.insertORupdate_TaskHistoryInfo(taskDetailsBean);
                            }
                            dataBase.insertORupdate_Task_history(taskDetailsBean);
                            if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("null") && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase("(null)")) {
                            } else {
                                if (Appreference.loginuserdetails.getUsername().equalsIgnoreCase(taskDetailsBean.getOwnerOfTask())) {
                                    if ((taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Group"))) {
                                        int a = VideoCallDataBase.getDB(context).GroupPercentageChecker(taskDetailsBean.getToUserId(), taskDetailsBean.getTaskId(), taskDetailsBean.getOwnerOfTask());
                                        taskDetailsBean.setCompletedPercentage(String.valueOf(a));
                                        Log.i("conversation", "Inside project after DB update group percentage " + a);
                                    }
                                    VideoCallDataBase.getDB(context).updateaccept("update taskHistoryInfo set completedPercentage='" + taskDetailsBean.getCompletedPercentage() + "' where taskId='" + taskDetailsBean.getTaskId() + "'");
                                }
                            }
                            Log.i("Mainactivity ", "Complete percentage2 ");
                            isSignalidSame = false;
                            Log.i("Mainactivity", "taskDetailsBean .getOwnerOfTask() " + taskDetailsBean.getOwnerOfTask());
                            Log.i("Mainactivity", "taskDetailsBean .getTaskType() " + taskDetailsBean.getTaskType());
                            if (taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && taskDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
                                groupOwnerPercentCalculation(taskDetailsBean);
                                Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() isGrp_Percent " + isGrp_Percent);
                                if (isGrp_Percent) {
                                    taskDetailsBean.setTaskStatus("Completed");
                                    VideoCallDataBase.getDB(context).groupTask_StatusUpdate(taskDetailsBean.getSignalid(), taskDetailsBean.getTaskStatus());
                                    VideoCallDataBase.getDB(context).updateaccept("update taskHistoryInfo set taskStatus='" + taskDetailsBean.getTaskStatus() + "' where taskId='" + taskDetailsBean.getTaskId() + "'");
                                    if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("null") && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase(null) && !taskDetailsBean.getProjectId().equalsIgnoreCase("(null)")) {
                                        VideoCallDataBase.getDB(context).updateaccept("update projectHistory set taskStatus='" + taskDetailsBean.getTaskStatus() + "' where projectId='" + taskDetailsBean.getProjectId() + "' and taskId='" + taskDetailsBean.getTaskId() + "'");
                                    }
                                    Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() DB final update for group taskStatus is " + taskDetailsBean.getTaskStatus());
                                }
                            }

                            TaskHistory taskHistory = (TaskHistory) Appreference.context_table.get("taskhistory");
                            if (taskHistory != null) {
                                Log.d("Mainactivity", "Value true refreshed-2");
                                taskHistory.refresh();
                            }
                        } else {
                            Log.i("Mainactivity", " taskhistory reference  else -------- > > > ");
                            if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("")) {
                                VideoCallDataBase.getDB(context).insert_new_Project_history(taskDetailsBean);
                            } else {
                                VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(taskDetailsBean);
                                VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                            }

                        }
                    } else if (taskDetailsBean.getCatagory() != null && taskDetailsBean.getCatagory().equalsIgnoreCase("chat")) {

                        taskDetailsBean.setTaskStatus(taskDetailsBean.getTaskStatus());
                        taskDetailsBean.setProjectId(null);
                        taskDetailsBean.setRead_status(5);
                        taskDetailsBean.setSubType("normal");
                        Log.i("Mainactivity ", "chat taskstatus " + taskDetailsBean.getTaskStatus());
                        Log.i("Mainactivity ", "chat taskstatus " + taskDetailsBean.getCatagory());
                        if (Appreference.context_table.containsKey("taskcoversation")) {
                            Log.i("Mainactivity", "taskcoversation reference ");
                            NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                            Log.i("Mainactivity", "fileName--2 " + taskDetailsBean.getTaskDescription());
                            newTaskConversation.notifyTaskReceived(taskDetailsBean);
                        } /*else if (Appreference.context_table.containsKey("traveljobdetails")) {
                            TravelJobDetails travelJobDetails = (TravelJobDetails) Appreference.context_table.get("traveljobdetails");
                            travelJobDetails.notifyTaskReceived(taskDetailsBean);
                            travelJobDetails.refresh();
                        }*/ else if (Appreference.context_table.containsKey("chatfragment")) {
                            ChatFragment contactsFragment = (ChatFragment) Appreference.context_table.get("chatfragment");
                            dataBase.insertORupdate_Task_history(taskDetailsBean);
                            dataBase.insertORupdate_TaskHistoryInfo(taskDetailsBean);
                            if (taskDetailsBean.getMimeType().equalsIgnoreCase("text"))
                                VideoCallDataBase.getDB(context).withdrawMsg_StatusUpdate(taskDetailsBean.getSignalid(), taskDetailsBean.getTaskDescription());
                            contactsFragment.refresh();
                        } else {
                            dataBase.insertORupdate_Task_history(taskDetailsBean);
                            dataBase.insertORupdate_TaskHistoryInfo(taskDetailsBean);
                            Log.i("Mainactivity ", "chat taskstatus " + taskDetailsBean.getTaskStatus());
                            if (taskDetailsBean.getMimeType().equalsIgnoreCase("text"))
                                VideoCallDataBase.getDB(context).withdrawMsg_StatusUpdate(taskDetailsBean.getSignalid(), taskDetailsBean.getTaskDescription());
                            BadgeReferece();
                        }


                    } else {
                        Log.i("Mainactivity", "else reference ");
                        if (!VideoCallDataBase.getDB(context).DuplicateChecker(taskDetailsBean.getSignalid(), taskDetailsBean.getTaskId()) || (taskDetailsBean.getTaskRequestType() != null && taskDetailsBean.getTaskRequestType().equalsIgnoreCase("customeAttributeRequest"))) {
                            taskDetailsBean.setRead_status(1);
                            Log.d("Mainactivity", "Value true 3");
                            updateDateRequestIcon(taskDetailsBean);
                            updateLeaveRequest_icon(taskDetailsBean);
                            updateLongMessage(taskDetailsBean);
                            updateOverdueMsg(taskDetailsBean);
//                            updateOverDueDateRequestorApprovelIcon(taskDetailsBean);
                        /*
                         Insert DB Check with signal Id,If signal id are same we are not insert in the DB
                          */

                            String query = "select * from taskDetailsInfo where  (loginuser='" + Appreference.loginuserdetails.getEmail() + "') and (taskId='" + taskDetailsBean.getTaskId() + "');";
                            taskList = VideoCallDataBase.getDB(mainContext).getTaskHistory(query);
                            Log.i("Mainactivity", "query " + query);
                            for (TaskDetailsBean taskDetailsBean1 : taskList) {
                                if (taskDetailsBean1.getSignalid().equalsIgnoreCase(taskDetailsBean.getSignalid())) {
                                    isSignalidSame = true;
                                    break;
                                } else {
                                }
                            }
                            if (!isSignalidSame) {
                                if (taskDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
                                    checker = VideoCallDataBase.getDB(context).Statuscheker(taskDetailsBean.getTaskId());
                                    if (checker != null && checker.equalsIgnoreCase("closed")) {
                                        if (!taskDetailsBean.getTaskStatus().equalsIgnoreCase("overdue") && !taskDetailsBean.getTaskStatus().equalsIgnoreCase("reminder")) {
                                            taskDetailsBean.setTaskStatus(checker);
                                            Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() if " + taskDetailsBean.getTaskStatus());
                                        }
                                        if (taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                            if (taskDetailsBean.getTaskStatus().equalsIgnoreCase("closed") && taskDetailsBean.getTaskDescription().contains("Completed Percentage ") && (!taskDetailsBean.getTaskDescription().contains("Completed Percentage 100%") || !taskDetailsBean.getTaskDescription().contains("Completed Percentage 100"))) {
                                                taskDetailsBean.setTaskStatus("inprogress");
                                            }
                                        } else if (!taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && taskDetailsBean.getFromUserName().equalsIgnoreCase(taskDetailsBean.getOwnerOfTask())) {
                                            if (taskDetailsBean.getTaskStatus().equalsIgnoreCase("closed") && taskDetailsBean.getTaskDescription().contains("Completed Percentage ") && (!taskDetailsBean.getTaskDescription().contains("Completed Percentage 100%") || !taskDetailsBean.getTaskDescription().contains("Completed Percentage 100"))) {
                                                taskDetailsBean.setTaskStatus("inprogress");
                                            }
                                        }
                                        Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() if " + taskDetailsBean.getTaskStatus());
                                    } else {

                                        if (taskDetailsBean.getTaskStatus().equalsIgnoreCase("closed")) {
                                            taskDetailsBean.setTaskStatus("closed");
                                            Log.i("Mainactivity", "value 51** " + taskDetailsBean.getTaskStatus());
                                            Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() else closed  " + taskDetailsBean.getTaskStatus());
                                            if (!taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))
                                                VideoCallDataBase.getDB(context).updateGroupCloseTaskStatus(taskDetailsBean.getTaskId(), taskDetailsBean.getCompletedPercentage());
                                        } else {
                                            Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() else if  " + taskDetailsBean.getFromUserName() + "Task Owner name" + taskDetailsBean.getOwnerOfTask());
                                            if (taskDetailsBean.getFromUserName().equalsIgnoreCase(taskDetailsBean.getOwnerOfTask()) || taskDetailsBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                                if (taskDetailsBean.getTaskDescription().contains("Completed Percentage")) {
                                                    percentage = taskDetailsBean.getCompletedPercentage();
                                                    Log.i("conversation", "taskDetailsBean.getTaskStatus() else if ->  " + percentage);
                                                } else {
                                                    percentage = VideoCallDataBase.getDB(context).getlastCompletedParcentage(taskDetailsBean.getTaskId());
                                                    if (percentage != null && !percentage.equalsIgnoreCase(null) && !percentage.equalsIgnoreCase("") && !percentage.equalsIgnoreCase("null")) {
                                                        taskDetailsBean.setCompletedPercentage(percentage);
                                                    }
                                                    Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() else if --> " + percentage);
                                                }
                                            } else {
                                                if (!taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                                    if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("null") && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase(null) && !taskDetailsBean.getProjectId().equalsIgnoreCase("(null)")) {
                                                        percentage = VideoCallDataBase.getDB(context).getlastProjectCompletedPercentage(taskDetailsBean.getTaskId());
                                                        if (percentage != null && !percentage.equalsIgnoreCase(null) && !percentage.equalsIgnoreCase("") && !percentage.equalsIgnoreCase("null")) {
                                                            Log.i("conversation", "taskDetailsBean.getTaskStatus()  if  check ---> " + percentage);
                                                            taskDetailsBean.setCompletedPercentage(percentage);
                                                        }
                                                    } else {
                                                        percentage = VideoCallDataBase.getDB(context).getlastCompletedParcentage(taskDetailsBean.getTaskId());
                                                        if (percentage != null && !percentage.equalsIgnoreCase(null) && !percentage.equalsIgnoreCase("") && !percentage.equalsIgnoreCase("null")) {
                                                            taskDetailsBean.setCompletedPercentage(percentage);
                                                        }
                                                    }
                                                    Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() else if ---> " + percentage);
                                                    if (taskDetailsBean.getTaskStatus().equalsIgnoreCase("Completed")) {
                                                        taskList_12 = new ArrayList<>();
                                                        taskList_12 = VideoCallDataBase.getDB(context).getTaskHistory("select * from taskDetailsInfo where fromUserName='" + Appreference.loginuserdetails.getUsername() + "' and taskId='" + taskDetailsBean.getTaskId() + "' and taskDescription = 'Completed Percentage 100%' order by id desc");
                                                        if (taskList_12.size() > 0) {
                                                            String completed_value = taskList_12.get(0).getCompletedPercentage();
                                                            if (completed_value.equalsIgnoreCase("100") || completed_value.equalsIgnoreCase("100%")) {
                                                                taskStatus = "Completed";
                                                                Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() notify 12 " + taskStatus);
                                                            } else {
                                                                taskStatus = "inprogress";
                                                                Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() notify 13 " + taskStatus);
                                                            }
                                                            taskDetailsBean.setTaskStatus(taskStatus);
                                                            Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() inside taker-group taskstatus is " + taskStatus);
                                                        } else {
                                                            taskDetailsBean.setTaskStatus("inprogress");
                                                            Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() not taker-group taskstatus is " + taskStatus);
                                                        }
                                                    }
                                                }
                                            }
                                            Log.i("Mainactivity", "db update percentage " + taskDetailsBean.getCompletedPercentage());
                                            Log.i("Mainactivity", "db update status " + taskDetailsBean.getTaskStatus());
                                            Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() else else  " + taskDetailsBean.getTaskStatus());
                                            if (taskDetailsBean.getTaskStatus().equalsIgnoreCase("Completed")) {
                                                groupOwnerPercentCalculation(taskDetailsBean);
                                                Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() isGrp_Percent " + isGrp_Percent);
                                                if (isGrp_Percent) {
                                                    taskDetailsBean.setTaskStatus("Completed");
                                                    Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() final " + taskDetailsBean.getTaskStatus());
                                                } else if (!isGrp_Percent) {
                                                    taskDetailsBean.setTaskStatus("inprogress");
                                                    Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() not final " + taskDetailsBean.getTaskStatus());
                                                }
                                            }
                                            if (taskDetailsBean.getTaskDescription() != null && taskDetailsBean.getTaskDescription().equalsIgnoreCase("This task is overdue")) {
                                                taskDetailsBean.setTaskStatus("overdue");
                                                Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() else overdue???? " + taskDetailsBean.getTaskStatus());
                                            }
                                        }
                                    }
                                }
                                Log.i("Mainactivity", "new task conversation 111");
                                if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("null") && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase("(null)")) {
                                    Log.i("Mainactivity", "projectid 3");
                                    VideoCallDataBase.getDB(context).insert_new_Project_history(taskDetailsBean);
                                    Log.i("status", "new task $$$ ==>  " + taskDetailsBean.getProjectStatus());
                                    Log.i("status", "new task $$$$  " + taskDetailsBean.getTaskStatus());

                                    if (taskDetailsBean.getProjectStatus() != null) {
                                        if ((taskDetailsBean.getProjectStatus() != null && !taskDetailsBean.getProjectStatus().equalsIgnoreCase("")
                                                && !taskDetailsBean.getProjectStatus().equalsIgnoreCase("null") && !taskDetailsBean.getProjectStatus().equalsIgnoreCase(null)
                                                && taskDetailsBean.getProjectStatus().equalsIgnoreCase("9"))
                                                && taskDetailsBean.getTravelEndTime() != null && !taskDetailsBean.getTravelEndTime().equalsIgnoreCase("")
                                                && !taskDetailsBean.getTravelEndTime().equalsIgnoreCase(null)) {
                                            Log.i("mainActivity", "projectUpdate query if # ");
                                            String queryUpdate = "update projectStatus set travelEndTime='" + taskDetailsBean.getTravelEndTime() + "' where projectId='" + taskDetailsBean.getProjectId() + "' and taskId= '" + taskDetailsBean.getTaskId() + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";
                                            Log.i("mainActivity", "projectUpdate query " + queryUpdate);
                                            VideoCallDataBase.getDB(context).updateaccept(queryUpdate);
                                        } else {
                                            Log.i("mainActivity", "projectUpdate query else # ");
                                            VideoCallDataBase.getDB(context).insertORupdateStatus(taskDetailsBean);
                                        }
                                    } else if (taskDetailsBean.getMimeType().equalsIgnoreCase("assigntask")) {
                                        VideoCallDataBase.getDB(context).insertORupdateStatus(taskDetailsBean);
                                    }
                                } else {
                                    Log.i("Mainactivity", "projectid 4");
                                    taskDetailsBean.setProjectId(null);
                                    Log.i("Mainactivity ", "taskstatus " + taskDetailsBean.getTaskStatus());
                                    Log.i("Mainactivity ", "taskstatus " + taskDetailsBean.getCatagory());
                                    Log.i("Main--->", " Main 8 " + taskDetailsBean.getTaskDescription());

                                    dataBase.insertORupdate_TaskHistoryInfo(taskDetailsBean);
                                }
                                dataBase.insertORupdate_Task_history(taskDetailsBean);
                                Log.i("Mainactivity ", "taskstatus " + taskDetailsBean.getTaskStatus());
                                Log.i("Mainactivity ", "issues id " + taskDetailsBean.getIssueId());
                                if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("null") && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase("(null)")) {
                                    if (Appreference.loginuserdetails.getUsername().equalsIgnoreCase(taskDetailsBean.getOwnerOfTask())) {
                                        if ((taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Group"))) {
                                            String project_memList = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskMemberList from projectHistory where taskId='" + taskDetailsBean.getTaskId() + "'");
                                            int counter = 0;
                                            for (int i = 0; i < project_memList.length(); i++) {
                                                if (project_memList.charAt(i) == ',') {
                                                    counter++;
                                                }
                                                Log.d("project_details", "Task Mem's counter size is == " + counter);
                                            }
                                            Log.i("observer", "list clear 1");
                                            ArrayList<String> listOfObs = new ArrayList<>();
                                            listOfObs.clear();
                                            for (int j = 0; j < counter + 1; j++) {
                                                Log.i("project_details", "Task Mem's and position == " + project_memList.split(",")[j] + " " + j);
                                                if (counter == 0) {
                                                    listOfObs.add(project_memList);
                                                } else {
                                                    listOfObs.add(project_memList.split(",")[j]);
                                                }
                                            }
                                            int a = VideoCallDataBase.getDB(context).ProjectGroupPercentageChecker(listOfObs, taskDetailsBean.getTaskId(), taskDetailsBean.getOwnerOfTask());
                                            taskDetailsBean.setCompletedPercentage(String.valueOf(a));
                                            Log.i("conversation", "Inside project after DB update group percentage " + a);
                                        }

                                        VideoCallDataBase.getDB(context).updateaccept("update projectHistory set completedPercentage='" + taskDetailsBean.getCompletedPercentage() + "' where taskId='" + taskDetailsBean.getTaskId() + "'");
                                    }
                                } else {
                                    if (Appreference.loginuserdetails.getUsername().equalsIgnoreCase(taskDetailsBean.getOwnerOfTask())) {
                                        if ((taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Group"))) {
                                            int a = VideoCallDataBase.getDB(context).GroupPercentageChecker(taskDetailsBean.getToUserId(), taskDetailsBean.getTaskId(), taskDetailsBean.getOwnerOfTask());
                                            taskDetailsBean.setCompletedPercentage(String.valueOf(a));
                                            Log.i("conversation", "Inside project after DB update group percentage " + a);
                                        }
                                        VideoCallDataBase.getDB(context).updateaccept("update taskHistoryInfo set completedPercentage='" + taskDetailsBean.getCompletedPercentage() + "' where taskId='" + taskDetailsBean.getTaskId() + "'");
                                    }
                                }
                                Log.i("Mainactivity ", "Complete percentage3 ");
                                Log.i("Mainactivity ", "Value true not in history page");
                                isSignalidSame = false;
                                Log.i("Mainactivity", "taskDetailsBean .getOwnerOfTask() " + taskDetailsBean.getOwnerOfTask());
                                Log.i("Mainactivity", "taskDetailsBean .getTaskType() " + taskDetailsBean.getTaskType());
                                if (taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && taskDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
                                    groupOwnerPercentCalculation(taskDetailsBean);
                                    Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() isGrp_Percent " + isGrp_Percent);
                                    if (isGrp_Percent) {
                                        taskDetailsBean.setTaskStatus("Completed");
                                        VideoCallDataBase.getDB(context).groupTask_StatusUpdate(taskDetailsBean.getSignalid(), taskDetailsBean.getTaskStatus());
                                        VideoCallDataBase.getDB(context).updateaccept("update taskHistoryInfo set taskStatus='" + taskDetailsBean.getTaskStatus() + "' where taskId='" + taskDetailsBean.getTaskId() + "'");
                                        if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("null") && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase(null) && !taskDetailsBean.getProjectId().equalsIgnoreCase("(null)")) {
                                            VideoCallDataBase.getDB(context).updateaccept("update projectHistory set taskStatus='" + taskDetailsBean.getTaskStatus() + "' where projectId='" + taskDetailsBean.getProjectId() + "' and taskId='" + taskDetailsBean.getTaskId() + "'");
                                        }
                                        Log.i("Mainactivity", "taskDetailsBean.getTaskStatus() DB final update for group taskStatus is " + taskDetailsBean.getTaskStatus());
                                    }
                                }
                            }
                            final TaskHistory taskHistory = (TaskHistory) Appreference.context_table.get("taskhistory");
                            if (taskHistory != null) {
                                Log.d("Mainactivity", "Value true refreshed-3");
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        taskHistory.refresh();
                                    }
                                }, 3000);
                            }
                    /*
                        Insert DB function finished Before Code Line
                         */
                       /* if (taskDetailsBean.getMimeType().equalsIgnoreCase("date")) {
                            taskDetailsBean.setTaskDescription("TaskDate");
                        }*/
                            ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
                            if (contactsFragment != null) {
                                Log.d("Mainactivity", "Value true refreshed-5");
                                contactsFragment.refresh();
                            }
                        } else {
                            Log.i("Mainactivity", " else reference else  -------- > > > ");
                            if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("")) {
                                VideoCallDataBase.getDB(context).insert_new_Project_history(taskDetailsBean);
                            } else {
                                VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(taskDetailsBean);
                                VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                            }
                        }

                        final AllTaskList allTaskList = (AllTaskList) Appreference.context_table.get("alltasklist");
                        if (allTaskList != null) {
                            Log.i("popupmenu", "alltasklist @@1 ");
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    allTaskList.refresh();
                                }
                            }, 3000);
                        }
                    }

                    Log.i("Mainactivity", "else reference ** ");
                    Log.i("Mainactivity", "Abondoned " + taskDetailsBean.getTaskStatus());
                    if (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("abandoned")) {
                        String query_3 = "update taskDetailsInfo set taskStatus = 'abandoned' where taskId='" + taskDetailsBean.getTaskId() + "' and taskStatus <> 'reminder'";
                        VideoCallDataBase.getDB(context).getTaskHistory(query_3);
                        Log.i("Mainactivity ", "Value true inside update abandoned");
                    } else if (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("pause")) {
                        String query_5 = "update taskDetailsInfo set taskStatus = 'pause' where taskId='" + taskDetailsBean.getTaskId() + "' and taskStatus <> 'reminder'";
                        VideoCallDataBase.getDB(context).getTaskHistory(query_5);
                        Log.i("Mainactivity ", "Value true inside update pause");
                    } else if (taskDetailsBean.getTaskDescription() != null && taskDetailsBean.getTaskDescription().equalsIgnoreCase("This task is activated")) {
                        String query_4 = "update taskDetailsInfo  set taskStatus ='" + taskDetailsBean.getTaskStatus() + "' where taskId='" + taskDetailsBean.getTaskId() + "' and taskStatus = 'abandoned'";
                        Log.i("Mainactivity", "query_4 " + query_4);
                        VideoCallDataBase.getDB(context).getTaskHistory(query_4);
                        Log.i("Mainactivity ", "Value true inside update activated");
                    } else if (taskDetailsBean.getTaskDescription() != null && taskDetailsBean.getTaskDescription().equalsIgnoreCase("This task is resumed")) {
                        String query_6 = "update taskDetailsInfo  set taskStatus ='" + taskDetailsBean.getTaskStatus() + "' where taskId='" + taskDetailsBean.getTaskId() + "' and taskStatus = 'abandoned'";
                        Log.i("Mainactivity", "query_4 " + query_6);
                        VideoCallDataBase.getDB(context).getTaskHistory(query_6);
                        Log.i("Mainactivity ", "Value true inside update resumed");
                    }

                    /*if (taskDetailsBean.getTaskDescription() != null && taskDetailsBean.getTaskDescription().contains("Escalation added")) {
                        Log.i("Buzz", "Buzz escalation added" + taskDetailsBean.getRemainderFrequency());
                        if (taskDetailsBean.getRemainderFrequency() != null && !taskDetailsBean.getRemainderFrequency().equalsIgnoreCase("") && !taskDetailsBean.getRemainderFrequency().equalsIgnoreCase(null) && !taskDetailsBean.getRemainderFrequency().equalsIgnoreCase("null") && !taskDetailsBean.getRemainderFrequency().equalsIgnoreCase("(null)")) {
                            startSelfAlarmManager(taskDetailsBean, Integer.parseInt(taskDetailsBean.getTaskId()));
                        }
                    }*/

                    alarmtask = Integer.parseInt(taskDetailsBean.getTaskId());
                    Log.i("Mainactivity", "alarmtask id  " + alarmtask);
                    if (taskDetailsBean.getCompletedPercentage() != null && taskDetailsBean.getCompletedPercentage().equalsIgnoreCase("100")) {
                        if ((taskDetailsBean.getOwnerOfTask() != null && taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) || (taskDetailsBean.getTaskReceiver() != null && taskDetailsBean.getTaskReceiver().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) || (taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Group"))) {
                            if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
                                Log.i("Mainactivity", "Push Notification from client - value is " + getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER));
                                expiredtime(alarmtask);
                            }
                        }
                    }
                    if (taskDetailsBean.getMimeType().equalsIgnoreCase("date")) {
                        if (taskDetailsBean.getRemainderFrequency() != null && !taskDetailsBean.getRemainderFrequency().equalsIgnoreCase(null)) {
                            if (taskDetailsBean.getRequestStatus() != null && (taskDetailsBean.getRequestStatus().equalsIgnoreCase("assigned") || taskDetailsBean.getRequestStatus().equalsIgnoreCase("approved"))) {
                                Log.i("Mainactivity", "alarm manager request status--->  " + taskDetailsBean.getRequestStatus());
                                if ((taskDetailsBean.getOwnerOfTask() != null && taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) || (taskDetailsBean.getTaskReceiver() != null && taskDetailsBean.getTaskReceiver().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) || (taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Group"))) {
                                    if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
                                        Log.i("Mainactivity", "Push Notification from client - value is " + getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER));
                                        expiredtime(alarmtask);
                                    }
                                }
                            }
                            Log.d("Mainactivity", "for Reminder " + taskDetailsBean.getRemainderFrequency());
                            //if (insert_id != 0)
                            Log.i("Mainactivity", "alarm initial " + alarmtask);

                            Log.i("Mainactivity", "alarm manager started successfully for ==  " + alarmtask);
                            if (taskDetailsBean.getRequestStatus() != null && (taskDetailsBean.getRequestStatus().equalsIgnoreCase("assigned") || taskDetailsBean.getRequestStatus().equalsIgnoreCase("approved"))) {
                                Log.i("Mainactivity", "alarm manager request status--->  " + taskDetailsBean.getRequestStatus());
                                if ((taskDetailsBean.getOwnerOfTask() != null && taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) || (taskDetailsBean.getTaskReceiver() != null && taskDetailsBean.getTaskReceiver().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) || (taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Group"))) {
                                    if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
                                        Log.i("Mainactivity", "Push Notification from client - value is " + getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER));
                                        startAlarmManager(taskDetailsBean, alarmtask, true);
                                    }
                                }
                            }

                            Log.i("Mainactivity", "Completed percentage " + taskDetailsBean.getCompletedPercentage());
                            if (taskDetailsBean.getCompletedPercentage() != null && taskDetailsBean.getCompletedPercentage().equals("100")) {
                                Log.i("Mainactivity", "inside % completion " + alarmtask);
                                if ((taskDetailsBean.getOwnerOfTask() != null && taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) || (taskDetailsBean.getTaskReceiver() != null && taskDetailsBean.getTaskReceiver().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) || (taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Group"))) {
                                    if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
                                        Log.i("Mainactivity", "Push Notification from client - value is " + getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER));
                                        expiredtime(alarmtask);
                                    }
                                }
                            }
                        }
                    } else if (taskDetailsBean.getMimeType() != null && taskDetailsBean.getMimeType().equalsIgnoreCase("text")) {
                        if (taskDetailsBean.getTaskDescription() != null && taskDetailsBean.getTaskDescription().equalsIgnoreCase("This task is abandoned")) {
                            if ((taskDetailsBean.getOwnerOfTask() != null && taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) || (taskDetailsBean.getTaskReceiver() != null && taskDetailsBean.getTaskReceiver().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) || (taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Group"))) {
                                if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
                                    Log.i("Mainactivity", "Push Notification from client - value is " + getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER));
                                    expiredtime(alarmtask);
                                }
                            }
                            Log.d("Mainactivity", "Value true cancelled");
                            if (Appreference.context_table.containsKey("taskcoversation")) {
                                NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                                newTaskConversation.refresh();
                                Log.d("Mainactivity", "Value true Con-refreshed");
                            }
                        } else if (taskDetailsBean.getTaskDescription() != null && taskDetailsBean.getTaskDescription().equalsIgnoreCase("This task is activated")) {
                            taskList_1 = new ArrayList<>();
                            String queryy = "select * from taskDetailsInfo where (fromUserName='" + taskDetailsBean.getFromUserName() + "' or toUserName='" + taskDetailsBean.getToUserName() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "'and taskId='" + alarmtask + "' and mimeType='date' and (requestStatus='approved' or requestStatus='assigned') order by id desc";
                            taskList_1 = VideoCallDataBase.getDB(context).getTaskHistory(queryy);
                            Log.i("Mainactivity", "Activated listsize " + taskList_1.size());
                            if (taskList_1.size() > 0) {
                                TaskDetailsBean taskDetailsBean1 = taskList_1.get(0);
                                Log.i("Mainactivity", "Rescheduling the alarm once task get Activated " + taskDetailsBean1.getPlannedEndDateTime());
                                if ((taskDetailsBean.getOwnerOfTask() != null && taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) || (taskDetailsBean.getTaskReceiver() != null && taskDetailsBean.getTaskReceiver().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) || (taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Group"))) {
                                    if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
                                        Log.i("Mainactivity", "Push Notification from client - value is " + getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER));
                                        startAlarmManager(taskDetailsBean1, alarmtask, true);
                                    }
                                    Log.d("Mainactivity", "Activated inside");
                                    if (Appreference.context_table.containsKey("taskcoversation")) {
                                        final NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                newTaskConversation.reminderTimerDisplay();
                                                newTaskConversation.refresh();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                    if (Appreference.context_table.containsKey("projecthistory")) {
                        Log.i("notifyreceived", "projecthistory status == $$$$  ");
                        final ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                        if (projectHistory != null) {
                            Log.i("notifyreceived", "projecthistory  ==  " + taskDetailsBean.getTaskDescription());
                            if (taskDetailsBean.getTaskDescription() != null) {
                                if (taskDetailsBean.getTaskDescription().equalsIgnoreCase("Task is Started") ||
                                        taskDetailsBean.getTaskDescription().equalsIgnoreCase("Task is hold") ||
                                        taskDetailsBean.getTaskDescription().equalsIgnoreCase("Task is Resumed") ||
                                        taskDetailsBean.getTaskDescription().equalsIgnoreCase("Task is Restarted") ||
                                        taskDetailsBean.getTaskDescription().equalsIgnoreCase("Task is Paused") ||
                                        taskDetailsBean.getTaskDescription().equalsIgnoreCase("Task is Completed") ||
                                        taskDetailsBean.getTaskDescription().equalsIgnoreCase("Hold Remarks :") ||
                                        taskDetailsBean.getTaskDescription().equalsIgnoreCase("Gathering Details...") ||
                                        taskDetailsBean.getMimeType().equalsIgnoreCase("assigntask") ||
                                        taskDetailsBean.getSubType().equalsIgnoreCase("deassign")) {
                                    if (projectHistory.projectDetailsBeans != null && projectHistory.projectDetailsBeans.size() > 0 && projectHistory.buddyArrayAdapter != null) {
                                        Log.i("notifyreceived", "getTaskStatus==> 3 " + taskDetailsBean.getTaskStatus());
                                        if (taskDetailsBean.getTaskStatus() != null && !taskDetailsBean.getTaskStatus().equalsIgnoreCase("")) {
                                            for (ProjectDetailsBean detailsBean : projectHistory.projectDetailsBeans) {
                                                if (detailsBean.getId() != null && detailsBean.getTaskId() != null &&
                                                        detailsBean.getId().equalsIgnoreCase(taskDetailsBean.getProjectId()) &&
                                                        detailsBean.getTaskId().equalsIgnoreCase(taskDetailsBean.getTaskId())) {
                                                    Log.i("notifyreceived", "for getTaskStatus==> 4  " + taskDetailsBean.getTaskStatus());
                                                    detailsBean.setTaskType(taskDetailsBean.getTaskType());
                                                    detailsBean.setTaskStatus(taskDetailsBean.getTaskStatus());
                                                    detailsBean.setTaskReceiver(taskDetailsBean.getTaskReceiver());
                                                    detailsBean.setTaskMemberList(taskDetailsBean.getGroupTaskMembers());
                                                    Log.i("draft123", "Mainactivity before If" + taskDetailsBean.getTaskStatus());
                                                    Appreference.old_status.put(detailsBean.getTaskId(), taskDetailsBean.getTaskStatus());
                                                    Log.i("draft123", "Mainactivity Appreference added status " + taskDetailsBean.getTaskStatus());
                                                        Log.i("draft123", "Mainactivity Appreference added getTaskReceiver @@ " + taskDetailsBean.getTaskReceiver());
                                                    Log.i("draft123", "Mainactivity Appreference added ID" + detailsBean.getTaskId());
                                                    break;
                                                }
                                            }
                                        }
                                        handler1.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                projectHistory.buddyArrayAdapter.notifyDataSetChanged();
                                            }
                                        });
                                    }
                                }
                            }

                        }
                    }
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    Appreference.printLog("MainActivity", "notifyChat_Received Exception: " + e.getMessage(), "WARN", null);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Appreference.printLog("MainActivity", "notifyChat_Received Exception: " + e.getMessage(), "WARN", null);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MainActivity", "notifyChat_Received Exception: " + e.getMessage(), "WARN", null);
                }
            } else if (message.contains("<Conferencecallinfo")) {
                xmlparser xmlParser = new xmlparser();
                if (xmlParser.parseConferenceCallInfo(message) != null) {
                    if (xmlParser.parseConferenceCallInfo(message).equalsIgnoreCase("broadcastcall")) {
                        Log.i("broadcastcall", "Appreference.received_broadcastcall -->true");
                        Appreference.received_broadcastcall = true;
                    } else {
                        Log.i("broadcastcall", "Appreference.received_broadcastcall -->false");
                        Appreference.received_broadcastcall = false;
                    }

                    /*if (Appreference.context_table.containsKey("callactivity")) {
    //                    Log.i("CallActivity","callerid-->"+callerid);
    //                    Log.i("CallActivity","calledid-->"+calledid);
    //                    if(calledid != null && callerid != null && calledid.equalsIgnoreCase(callerid)) {
                        CallActivity callActivity = (CallActivity) Appreference.context_table.get("callactivity");
                        callActivity.BusyState();
    //                    }
                    }*/

                }

            } else if (message.contains("<FormsDetailsInfo")) {
                xmlparser xmlParser = new xmlparser();
                TaskDetailsBean taskDetailsBean = xmlParser
                        .parseFormsValueInSipMessage(message);
                if (Appreference.context_table.containsKey("formsEntryView")) {

                    FormEntryViewActivity formEntryViewActivity = (FormEntryViewActivity) Appreference.context_table.get("formsEntryView");
                    formEntryViewActivity.callFormsListWebSerices(taskDetailsBean);

                }


            } else if (message.contains("<FormAccess")) {
                xmlparser xmlParser = new xmlparser();
                ArrayList<FormAccessBean> formAccessBeen = xmlParser.parseFromAccess(message);
                for (int i = 0; i < formAccessBeen.size(); i++) {
                    VideoCallDataBase.getDB(context).UpdateOrInsertFormAccess(formAccessBeen.get(i));
                }

            } else if (Appreference.context_table.containsKey("callactivity")) {
                CallActivity callActivity = (CallActivity) Appreference.context_table.get("callactivity");
                callActivity.notifyOnpager(message);

            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "notifyChat_Received Exception: " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "notifyChat_Received Exception: " + e.getMessage(), "WARN", null);
        }
//        if (Appreference.context_table.containsKey("callactivity")) {
//            CallActivity callActivity = (CallActivity) Appreference.context_table.get("callactivity");
//            callActivity.BusyState();
//        }
    }

    private void updateLongMessage(TaskDetailsBean taskDetailsBean) {
        try {
            if (taskDetailsBean.getMimeType() != null && taskDetailsBean.getMimeType().equalsIgnoreCase("textfile")) {
                Log.i("profiledownload", "MimeType 123 MainActivity  " + taskDetailsBean.getMimeType() + " and  " + taskDetailsBean.getTaskDescription());
                VideoCallDataBase.getDB(context).task_LongmessageUpdateForReceiver(taskDetailsBean.getSignalid());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "updateLongMessage Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void groupOwnerPercentCalculation(TaskDetailsBean taskDetailsBean) {
        try {
            listMembers_1 = new ArrayList<>();
            listMembers_2 = new ArrayList<>();
            listofProjectMembers = new ArrayList<>();
            if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase(null) && !taskDetailsBean.getProjectId().equalsIgnoreCase("null") && !taskDetailsBean.getProjectId().equalsIgnoreCase("(null)")) {
                String pjt_ListMems = VideoCallDataBase.getDB(context).getProjectListMembers(taskDetailsBean.getTaskId());
                int counter = 0;
                for (int i = 0; i < pjt_ListMems.length(); i++) {
                    if (pjt_ListMems.charAt(i) == ',') {
                        counter++;
                    }
                    Log.d("project_details", "Task Mem's counter size is == " + counter);
                }
                for (int j = 0; j < counter + 1; j++) {
                    ListMember list_Mems = new ListMember();
                    Log.i("project_details", "Task Mem's and position == " + pjt_ListMems.split(",")[j] + " " + j);
                    if (counter == 0) {
                        list_Mems.setUsername(pjt_ListMems);
                        listMembers_1.add(list_Mems);
                        listofProjectMembers.add(pjt_ListMems);
                    } else {
                        list_Mems.setUsername(pjt_ListMems.split(",")[j]);
                        listMembers_1.add(list_Mems);
                        listofProjectMembers.add(pjt_ListMems.split(",")[j]);
                    }
                }
                /*if (listofProjectMembers != null) {
                    int a = VideoCallDataBase.getDB(context).ProjectGroupPercentageChecker(listofProjectMembers, taskDetailsBean.getTaskId(), taskDetailsBean.getOwnerOfTask());
                    taskDetailsBean.setCompletedPercentage(String.valueOf(a));
                    Log.i("project_details", "OwnerPercentage " + a);
                }*/
            } else {
                listMembers_1 = VideoCallDataBase.getDB(context).getGroupmemberHistory("select * from groupmember where groupid='" + taskDetailsBean.getToUserId() + "'");
            }
            Log.i("popup", "arrayList size is " + listMembers_1);
            if (listMembers_1.size() > 0) {
                Log.i("popup", "if inside --> " + listMembers_1 + " " + taskDetailsBean.getToUserName());
                for (ListMember listMember : listMembers_1) {
                    Log.i("popup", "if inside --> " + listMembers_1.size() + " " + listMember.getUsername());
                    if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(listMember.getUsername())) {
                        Log.i("popup", "before db line--> " + listMembers_1);
                        int percent_1 = VideoCallDataBase.getDB(context).groupPercentageStatus(listMember.getUsername(), taskDetailsBean.getTaskId());
                        Log.i("popup", "after db line--> " + percent_1 + " " + listMember.getUsername());
                        listMembers_2.add(String.valueOf(percent_1));
                    }
                }
            }
            for (int i = 0; i < listMembers_2.size(); i++) {
                Log.i("popup", "after add list  " + listMembers_2);
                if (listMembers_2.get(i).equalsIgnoreCase("100")) {
                    Log.i("popup", "isGrp_Percent=true if" + listMembers_1);
                    isGrp_Percent = true;
                    Log.i("popup", "isGrp_Percent=true if" + isGrp_Percent);
                } else {
                    Log.i("popup", "isGrp_Percent=false else" + isGrp_Percent);
                    isGrp_Percent = false;

                    Log.i("popup", "isGrp_Percent=false else" + isGrp_Percent);
                }
                if (!isGrp_Percent)
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "groupOwnerPercentCalculation Exception: " + e.getMessage(), "WARN", null);
        }
    }

    @Override
    public void notifySipMessageStatus(String reson, String toUri, String messagebody, int response_code) {
        try {
            Log.i("sipmessage", "Main Activity notifySipMessageStatus");
            xmlparser xmlparser = new xmlparser();
            if (messagebody != null && reson != null) {
                if (reson.equalsIgnoreCase("ok") || reson.equalsIgnoreCase("accepted")) {
                    if (messagebody.contains("<Buddychatinfo")) {
                        Log.i("sipmessage", "Main Activity notifySipMessageStatus <Buddychatinfo");
                        ChatBean bean = xmlparser.parseChat(messagebody);
                        if (bean != null && bean.getSignalid() != null) {
                            VideoCallDataBase.getDB(context).updateChatSentStatus(bean.getSignalid());
                            if (Appreference.context_table.containsKey("chatactivity")) {
                                final ChatActivity chatActivity = (ChatActivity) Appreference.context_table.get("chatactivity");
                                if (chatActivity != null) {
                                    chatActivity.updateChatStatus(bean);
                                }

                            }
                        }
                    } else if (messagebody.contains("<TaskDetailsinfo>")) {
                        TaskDetailsBean taskDetailsBean = xmlparser
                                .parseTaskDetailsSIPMessage(messagebody);
                        Log.i("sipmessage", "Main Activity notifySipMessage " + response_code);
                        if (taskDetailsBean != null && taskDetailsBean.getSignalid() != null) {
                            if (response_code == 200) {
                                VideoCallDataBase.getDB(context).updateallmessage(taskDetailsBean.getTaskId(), "1");
                                taskDetailsBean.setMsg_status(1);
                            } else if (response_code == 202) {
                                Log.i("sipmessage", "Main Activity notifySipMessage ==>  " + response_code);
                                Log.i("sipmessage", "getSignalid ==>  " + taskDetailsBean.getSignalid());
                                VideoCallDataBase.getDB(context).updateTaskSentStatus(taskDetailsBean.getSignalid(), "24");
                                taskDetailsBean.setMsg_status(24);
                            } else {
                                VideoCallDataBase.getDB(context).updateTaskSentStatus(taskDetailsBean.getSignalid(), "10");
                            }

                            if (taskDetailsBean.getMimeType() != null && taskDetailsBean.getMimeType().equalsIgnoreCase("date")) {
                                if (taskDetailsBean.getRequestStatus().equalsIgnoreCase("assigned") || taskDetailsBean.getRequestStatus().equalsIgnoreCase("approved")) {
                                    int sen_taskId = Integer.parseInt(taskDetailsBean.getTaskId());
                                    if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
                                        Log.i("Task", "Push Notification from client - value is " + getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER));
                                        expiredtime(sen_taskId);
                                        startAlarmManager(taskDetailsBean, sen_taskId, false);
                                    }
                                }
                            }
                            if (Appreference.context_table.containsKey("taskcoversation")) {
                                NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                                if (newTaskConversation != null) {
                                    newTaskConversation.updateMessageStatus(taskDetailsBean);
                                }
                            } else if (Appreference.context_table.containsKey("traveljobdetails")) {
                                TravelJobDetails travelJobDetails = (TravelJobDetails) Appreference.context_table.get("traveljobdetails");
                                if (travelJobDetails != null) {
                                    Log.i("travelJobDetails ", "updateMessageStatus==> ");
                                    travelJobDetails.updateMessageStatus(taskDetailsBean);
                                }
                            }
                        }
                    } else if (messagebody.contains("<TaskDetailsAddObserver>")) {
                        TaskDetailsBean taskDetailsBean = xmlparser
                                .parseObserverDetailsSIPMessage(messagebody);
                        if (taskDetailsBean != null && taskDetailsBean.getSignalid() != null) {
                            VideoCallDataBase.getDB(context).updateTaskSentStatus(taskDetailsBean.getSignalid());
                            if (Appreference.context_table.containsKey("taskcoversation")) {
                                NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                                if (newTaskConversation != null) {
                                    Log.d("TaskObserver", "Inside message recived area 2");
                                    newTaskConversation.updateMessageStatus(taskDetailsBean);
                                }
                            }
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "notifySipMessageStatus Exception: " + e.getMessage(), "WARN", null);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "notifySipMessageStatus Exception: " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "notifySipMessageStatus Exception: " + e.getMessage(), "WARN", null);
        }
    }

    private void showTaskNotification(TaskDetailsBean taskDetailsBean) {
        Intent intent = new Intent(this, TaskNotification.class);
        intent.putExtra("taskdetailsbean", taskDetailsBean);
        intent.putExtra("from", "NewTask");
        startActivityForResult(intent, 1000);
//        taskDetailsBeen = VideoCallDataBase.getDB(mainContext).getTaskHistory();
    }

    private void startSelfAlarmManager(TaskDetailsBean taskDetailsBean, int unicid) {
        SimpleDateFormat datefor = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date enddate = null, dateFor = null, endDate = null;
        String endTime = null, id_string;
        Log.d("TaskHistory", "Value true started");
        try {
            enddate = datefor.parse(taskDetailsBean.getRemainderFrequency());
            Log.i("task", "End.Date " + enddate);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "startSelfAlarmManager Exception: " + e.getMessage(), "WARN", null);
        }
        Log.d("Task", "for Reminder inside else  ");
        SimpleDateFormat timeforend = new SimpleDateFormat("HH:mm");
        SimpleDateFormat Datefor = new SimpleDateFormat("yyyy/MM/dd");
        try {
            endTime = timeforend.format(enddate);
            Log.i("Task", "End.Time " + endTime);
            endDate = Datefor.parse(Datefor.format(enddate));
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "startSelfAlarmManager Exception: " + e.getMessage(), "WARN", null);
        }
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ScheduleManager.class);
        Log.i("task", "taskDetailsBean.getTaskId() " + taskDetailsBean.getTaskId());
        Log.i("task", "taskDetailsBean.getSignalid() " + taskDetailsBean.getSignalid());
        unicid = Integer.parseInt(taskDetailsBean.getTaskId());
        id_string = VideoCallDataBase.getDB(context).getProjectParentTaskId("select id from taskDetailsInfo where signalid='" + taskDetailsBean.getSignalid() + "'");
        int incre_id = 0;
        if (id_string != null)
            incre_id = Integer.parseInt(id_string);
        intent.putExtra("id", unicid);
        intent.putExtra("Increment_id", incre_id);
        intent.putExtra("endTime", endTime);
        intent.putExtra("note", "buzz");
        intent.putExtra("signal_Id", taskDetailsBean.getSignalid());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), incre_id, intent, 0);
        Log.i("Task", "pendingIntent " + pendingIntent);
        Log.i("task", "incre_id " + incre_id);
        Calendar cal = Calendar.getInstance();
        Log.i("Task", "endDate " + endDate);
        Log.i("Task", "endTime " + endTime);
        Log.i("schedulemanager", "build version kitkat and below");
        Log.i("schedulemanager", "build version kitkat above");
        try {
            Log.i("Task", "have_repeat else " + endDate);
            cal.setTime(endDate);
            cal.set(Calendar.HOUR, Integer.parseInt(endTime.split(":")[0]));
            cal.set(Calendar.MINUTE, Integer.parseInt(endTime.split(":")[1]));
            Log.d("Task", "sender side alarm");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                Log.d("Task", "above kitkat");
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                Log.d("Task", "below kitkat");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "startSelfAlarmManager Exception: " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "startSelfAlarmManager Exception: " + e.getMessage(), "WARN", null);
        }
        Log.i("Task", "AlarmId start " + incre_id);
    }

    private void startAlarmManager(TaskDetailsBean taskDetailsBean, int unicid, boolean have_repeat) {
        SimpleDateFormat datefor = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date tmpDate = null, enddate = null, dateFor = null, endDate = null;
        String remainderFrequency = "None", rem_freq_min, rem_frq, strTime = null, endTime = null;
        int total_mins = 0;

        if (taskDetailsBean.getRemainderFrequency().equalsIgnoreCase("")) {

            Log.d("task", "for Reminder inside if  ");

//            Intent intent = new Intent(this, ScheduleManager.class);
//            intent.putExtra("id", unicid);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, unicid,
//                    intent, 0);

        } else {
            Log.d("TaskHistory", "Value true started");
            try {
                tmpDate = datefor.parse(taskDetailsBean.getRemainderFrequency());
                Log.i("task", "Rem.Date " + tmpDate);
                enddate = datefor.parse(taskDetailsBean.getPlannedEndDateTime());
                Log.i("task", "End.Date " + enddate);

                remainderFrequency = taskDetailsBean.getTimeFrequency();
//                rem_freq_min = remainderFrequency.split(" ")[0];
//                rem_frq = remainderFrequency.split(" ")[1];

                Log.d("TaskHistory", "Value true started");
                Log.i("task", "Reminder Freq Changed to Lower case " + remainderFrequency.toLowerCase());
                switch (remainderFrequency.toLowerCase()) {
                    case "none":
                        remainderFrequency = "0";
                        break;
                    case "every minute":
                        remainderFrequency = "1";
                        break;
                    case "every 10 min":
                        remainderFrequency = "10";
                        break;
                    case "hourly":
                        remainderFrequency = "60";
                        break;
                    case "daily":
                        remainderFrequency = "1440";
                        break;
                    case "week day":
                        remainderFrequency = "10080";
                        break;
                    case "monthly":
                        remainderFrequency = "43200";
                        break;
                    case "yearly":
                        remainderFrequency = "525600";
                        break;
                    default:
                        remainderFrequency = "0";
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "startAlarmManager Exception: " + e.getMessage(), "WARN", null);
            }

            Log.d("Task", "for Reminder inside else  ");

            SimpleDateFormat timeforrem = new SimpleDateFormat("HH:mm");
            SimpleDateFormat timeforend = new SimpleDateFormat("HH:mm");

            SimpleDateFormat Datefor = new SimpleDateFormat("yyyy/MM/dd");
//            Date tmpTime = null;
//            String amOrpm = null;

            try {
                strTime = timeforrem.format(tmpDate);
                Log.i("Task", "Rem.Time " + strTime);
                dateFor = Datefor.parse(Datefor.format(tmpDate));

//                Log.d("task", "for Reminder inside else  " + taskDetailsBean.getIsRemainderRequired());
//                String s1 = taskDetailsBean.getIsRemainderRequired().split(" ")[0];
//                String s2 = taskDetailsBean.getIsRemainderRequired().split(" ")[2];
//
//                Log.d("task", "for Reminder inside else  " + s1 + " : " + s2);
//
//                Date newdate = timeforend.parse(s1 + ":" + s2);
//                remainderFrequency = timeforend.format(newdate);
//
//
                Log.d("Task", "for Reminder--->> " + remainderFrequency);
//
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//
//                String inputString = remainderFrequency;
//
//                date = sdf.parse("1970-01-01 " + inputString);
//                Log.d("task", "for Reminder  ==   " + date.getTime());

                endTime = timeforend.format(enddate);
                Log.i("Task", "End.Time " + endTime);

                endDate = Datefor.parse(Datefor.format(enddate));
//            strTime =  timefor.format(tmpTime);
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "startAlarmManager Exception: " + e.getMessage(), "WARN", null);
            }

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, ScheduleManager.class);
            Log.i("task", "taskDetailsBean.getTaskId() " + taskDetailsBean.getTaskId());
            unicid = Integer.parseInt(taskDetailsBean.getTaskId());
            intent.putExtra("id", unicid);
            intent.putExtra("endTime", endTime);
//            intent.putExtra("currTime", currTime);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), unicid,
                    intent, 0);
            Log.i("Task", "pendingIntent " + pendingIntent);
            Calendar cal = Calendar.getInstance();

            Log.i("Task", "remdate " + dateFor);
            Log.i("Task", "endDate " + endDate);
            Log.i("Task", "remdtime " + strTime);
            Log.i("Task", "endTime " + endTime);
            Log.i("Task", "date compare " + dateFor.compareTo(endDate));
            Log.i("Task", "time compare " + strTime.compareTo(endTime));
            if (dateFor.compareTo(endDate) <= 0) {
                if (strTime.compareTo(endTime) < 0) {
                 /*   if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                        Log.i("schedulemanager", "build version kitkat and below");
//                        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

//                        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                        // 20 minutes.
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                                Long.parseLong(remainderFrequency) * 60000, pendingIntent);
                    } else {
                        Log.i("schedulemanager", "build version kitkat above");
//                        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
//                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                                Long.parseLong(remainderFrequency) * 60000, pendingIntent);
                    }*/

//                    if (Build.VERSION.SDK_INT < 23) {
//                        if (Build.VERSION.SDK_INT >= 19) {
//                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
//                                    Long.parseLong(remainderFrequency) * 60000, pendingIntent);
//
//                            Log.i("Task", "AlarmId start " + "above 19 =");
//                        } else {
//                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
//                                    Long.parseLong(remainderFrequency) * 60000, pendingIntent);
//
//                            Log.i("Task", "AlarmId start " + "below 19 =");
//                        }
//                    } else {
                    Log.i("Task", "have_repeat " + have_repeat);
                    if (have_repeat) {
                        Log.i("Task", "have_repeat if " + dateFor);
                        cal.setTime(dateFor);
                        cal.set(Calendar.HOUR, Integer.parseInt(strTime.split(":")[0]));
                        cal.set(Calendar.MINUTE, Integer.parseInt(strTime.split(":")[1]));
                        Log.d("Task", "receiver side alarm");
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                                Long.parseLong(remainderFrequency) * 60000, pendingIntent);
                    } else {
                        try {
                            Log.i("Task", "have_repeat else " + endDate);
                            cal.setTime(endDate);
                            cal.set(Calendar.HOUR, Integer.parseInt(endTime.split(":")[0]));
                            cal.set(Calendar.MINUTE, Integer.parseInt(endTime.split(":")[1]));
                            Log.d("Task", "sender side alarm");
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                                Log.d("Task", "above kitkat");
                            } else {
                                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                                Log.d("Task", "below kitkat");
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            Appreference.printLog("MainActivity", "startAlarmManager Exception: " + e.getMessage(), "WARN", null);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("MainActivity", "startAlarmManager Exception: " + e.getMessage(), "WARN", null);
                        }
                    }

//                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

//                        Log.i("Task", "AlarmId start " + "above 23+ =");
//                    }
                }
            }
            Log.i("Task", "Is_AlamStop" + Appreference.is_alarmStop);
            Log.i("Task", "AlarmId start " + unicid);
//            if ((Appreference.is_alarmStop == true) && (unicid != 0)) {
//                expiredtime(unicid);
//            }
        }
    }

    public void expiredtime(int id) {
        try {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent it = new Intent(this, ScheduleManager.class);
            PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, id,
                    it, PendingIntent.FLAG_CANCEL_CURRENT);
            if (pi != null) {
                Log.i("TaskHistory", "Value true alarm cancelled " + pi);
                am.cancel(pi);
                pi.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "expiredtime Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public String UTCToLocalTime(String time) {
        String formattedDate = null;
        Date myDate = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            TimeZone utcZone = TimeZone.getTimeZone("UTC");
            simpleDateFormat.setTimeZone(utcZone);
            try {
                if (time != null && !time.equalsIgnoreCase("(null)") && !time.equalsIgnoreCase("null") && !time.equalsIgnoreCase("") && !time.equalsIgnoreCase(null)) {
                    Log.i("gcmMessage", "time " + time);
                    myDate = simpleDateFormat.parse(time);
                    Log.i("gcmMessage", "myDate " + myDate);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "UTCToLocalTime Exception: " + e.getMessage(), "WARN", null);
            }
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            if (myDate != null) {
                formattedDate = simpleDateFormat.format(myDate);
                Log.i("gcmMessage", "formattedDate " + formattedDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "UTCToLocalTime Exception: " + e.getMessage(), "WARN", null);
        }
        if (formattedDate == null) {
            formattedDate = time;
        }
        Log.i("Main--->", " Main 6 " + formattedDate);
        return formattedDate;
    }


    public void startSchedulechatManager(ChatBean detailsBean) {
        try {
            Log.d("startSchedulechat", detailsBean.getScheduled());

            String id_string = detailsBean.getSignalid();
            String time = detailsBean.getScheduled().split(",")[1];
            if (time != null && !time.equalsIgnoreCase("") && !time.equalsIgnoreCase("null")) {
                id_string = VideoCallDataBase.getDB(context).getProjectParentTaskId("select autoincrement_id from chat where signalid='" + detailsBean.getSignalid() + "'");
                Log.d("startSchedulechat", "primaryid " + id_string);
            }
            String timearr[] = time.split(" ");
            int id = Integer.parseInt(id_string);
//        cancelScheduleChatManager(id);
            Log.i("Schedule", "snooze time--->" + timearr[1]);
            snooze_Time_Chat(time, timearr[1], id, detailsBean);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "startSchedulechatManager Exception: " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "startSchedulechatManager Exception: " + e.getMessage(), "WARN", null);
        }
    }


    private void cancelScheduleChatManager(int id) {
        try {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent it = new Intent(this, ScheduleChatReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(this, id,
                    it, PendingIntent.FLAG_CANCEL_CURRENT);
            if (pi != null) {
                am.cancel(pi);
                pi.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "cancelScheduleChatManager Exception: " + e.getMessage(), "WARN", null);
        }
    }


    public void snooze_Time_Chat(String date, String time, int id, ChatBean detailsBean) {
        try {
            SimpleDateFormat datefor = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date tmpDate, dateFor = null;
            tmpDate = datefor.parse(date);
            if (isScheduleCallTimeValid(tmpDate)) {
                SimpleDateFormat date_req = new SimpleDateFormat("yyyy/MM/dd");
                dateFor = date_req.parse(date_req.format(tmpDate));
                Log.i("Schedule", "snooze dateFor --->" + dateFor);
                Log.i("Schedule", "snooze detailsBean.getTaskId() --->" + detailsBean.getSignalid());
                Log.i("Schedule", "snooze detailsBean.getCustomSetId() --->" + detailsBean.getMessage());
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, ScheduleChatReceiver.class);
                intent.putExtra("Signalid", detailsBean.getSignalid());
                intent.putExtra("message", detailsBean.getScheduled());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), id,
                        intent, 0);
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateFor);
                cal.set(Calendar.HOUR, Integer.parseInt(time.split(":")[0]));
                cal.set(Calendar.MINUTE, Integer.parseInt(time.split(":")[1]));
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    Log.i("schedulemanager", "build version kitkat and below");
                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                } else {
                    Log.i("schedulemanager", "build version kitkat above");
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "snooze_Time_Chat Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void startScheduleManager(TaskDetailsBean detailsBean) {
        try {
            Log.d("startScheduleManager", detailsBean.getTaskDescription());
            Log.i("Main--->", " Main 7 " + detailsBean.getTaskDescription());
            String time = detailsBean.getTaskDescription();
            if (time != null && time.equalsIgnoreCase("Audio Conference")) {
                time = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskDescription from taskDetailsinfo where taskId='" + detailsBean.getTaskId() + "' and customSetId='" + detailsBean.getCustomSetId() + "' and taskTagName='Date'");
            }
//        String snoozeTime = null;
            String timearr[] = time.split(" ");
//        SimpleDateFormat dateFormat2 = new SimpleDateFormat(
//                "HH:mm:ss");
//        try {
//            Date dates = dateFormat2.parse(timearr[1]);
//            long newDate = dates.getTime()
//                    + (5 * 60 * 1000);
//            snoozeTime = dateFormat2.format(newDate);
//
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

            String id_string = detailsBean.getTaskId() + detailsBean.getCustomSetId();
            int id = Integer.parseInt(id_string);
            cancelScheduleManager(id);
            Log.i("Schedule", "snooze time--->" + timearr[1]);
            snooze_Time(time, timearr[1], id, detailsBean);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "startScheduleManager Exception: " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "startScheduleManager Exception: " + e.getMessage(), "WARN", null);
        }
    }

    private void cancelScheduleManager(int id) {
        try {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent it = new Intent(this, ScheduleCallReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(this, id,
                    it, PendingIntent.FLAG_CANCEL_CURRENT);
            if (pi != null) {
                am.cancel(pi);
                pi.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "cancelScheduleManager Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void snooze_Time(String date, String time, int id, TaskDetailsBean detailsBean) {
        try {
//            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//            Intent it = new Intent(this, ScheduleCallReceiver.class);
//            PendingIntent pi = PendingIntent.getBroadcast(this.getApplicationContext(), id,
//                    it, 0);
//            if (am != null) {
//                am.cancel(pi);
//            }
//        SimpleDateFormat datefor = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat datefor = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date tmpDate, dateFor = null;

            tmpDate = datefor.parse(date);
            if (isScheduleCallTimeValid(tmpDate)) {
                SimpleDateFormat date_req = new SimpleDateFormat("yyyy/MM/dd");
                dateFor = date_req.parse(date_req.format(tmpDate));
                Log.i("Schedule", "snooze dateFor --->" + dateFor);
                Log.i("Schedule", "snooze detailsBean.getTaskId() --->" + detailsBean.getTaskId());
                Log.i("Schedule", "snooze detailsBean.getCustomSetId() --->" + detailsBean.getCustomSetId());
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, ScheduleCallReceiver.class);
                intent.putExtra("Taskid", detailsBean.getTaskId());
                intent.putExtra("customsetid", detailsBean.getCustomSetId());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), id,
                        intent, 0);
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateFor);
                cal.set(Calendar.HOUR, Integer.parseInt(time.split(":")[0]));
                cal.set(Calendar.MINUTE, Integer.parseInt(time.split(":")[1]));
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
                    Log.i("schedulemanager", "build version kitkat and below");
                    alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                } else {
                    Log.i("schedulemanager", "build version kitkat above");
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "snooze_Time Exception: " + e.getMessage(), "WARN", null);
        }
    }

    private void scheduleCallAlert(Intent intent) {
        try {
            if (intent != null) {
                String taskid = intent.getStringExtra("Taskid");
                int setid = intent.getIntExtra("customsetid", 0);
                boolean percentage_value = false;
                Log.i("Schedule", "ScheduleCallReceiver == > taskid :" + taskid + " setid : " + setid);
                TaskDetailsBean taskDetailsBean = null, taskDetailsBean1 = null;

                String query = "select * from taskDetailsInfo where taskId = '" + taskid + "'  and subType = 'customeAttribute' and customSetId = '" + setid + "'";
                Log.i("Schedule", " query :=> " + query);
                ArrayList<TaskDetailsBean> detailsBeanArrayList = VideoCallDataBase.getDB(context).getTaskHistory(query);
                Log.i("Schedule", "ScheduleCallReceiver == > taskid :" + taskid + " size : " + detailsBeanArrayList.size());
                if (detailsBeanArrayList.size() > 0) {
                    for (TaskDetailsBean detailsBean : detailsBeanArrayList) {
                        Log.i("Schedule", "ScheduleCallReceiver == > detailsBean.getTaskDescription() :" + detailsBean.getTaskDescription() + " boolean value  : " + detailsBean.getTaskDescription().matches("[0-100]+"));
                        if (detailsBean.getTaskTagName().equalsIgnoreCase("Value") && detailsBean.getTaskDescription().length() < 4 && (0 < Integer.valueOf(detailsBean.getTaskDescription()) && Integer.valueOf(detailsBean.getTaskDescription()) < 100)) {
                            percentage_value = true;
                            taskDetailsBean = detailsBean;
                            Log.i("Schedule", "ScheduleCallReceiver percentage_value == > taskid :" + percentage_value + " size : " + taskDetailsBean.getTaskDescription());
                            break;
                        } else if (detailsBean.getTaskTagName().equalsIgnoreCase("Value") && (detailsBean.getTaskDescription().equalsIgnoreCase("assigned") || detailsBean.getTaskDescription().equalsIgnoreCase("overdue"))) {
                            percentage_value = false;
                            taskDetailsBean = detailsBean;
                            Log.i("Schedule", "ScheduleCallReceiver percentage_value == > taskid :" + percentage_value + " size : " + taskDetailsBean.getTaskDescription());
                            break;
                        } else if (detailsBean.getTaskTagName().equalsIgnoreCase("Conference Host")) {
                            percentage_value = false;
                            taskDetailsBean = detailsBean;
                        }
                    }
                }

                String query1 = "select * from taskHistoryInfo where taskId = '" + taskid + "'";
                ArrayList<TaskDetailsBean> detailsBeanArrayList1 = VideoCallDataBase.getDB(context).getTaskHistoryInfo(query1);
                Log.i("Schedule", "ScheduleCallReceiver == > taskid :" + taskid + " size 1 : " + detailsBeanArrayList1.size());

                if (detailsBeanArrayList1.size() > 0) {
                    taskDetailsBean1 = detailsBeanArrayList1.get(0);
                    Log.i("Schedule", "ScheduleCallReceiver taskHistoryInfo == > taskid :" + percentage_value + " size : " + taskDetailsBean1.getTaskStatus());
                }


                //            Log.i("Schedule", "ScheduleCallReceiver percentage == > taskstatus :" + taskDetailsBean.getTaskDescription() + "taskstatus 1 :" + taskDetailsBean1.getTaskStatus());
                //            Log.i("Schedule", "ScheduleCallReceiver status == > taskstatus :" + taskDetailsBean.getTaskStatus() + "taskstatus 1 :" + taskDetailsBean1.getTaskStatus() + " size 1 : " + taskDetailsBean.getTaskStatus().equalsIgnoreCase(taskDetailsBean1.getTaskStatus()));
                if (percentage_value) {
                    if (taskDetailsBean1.getCompletedPercentage() == null)
                        taskDetailsBean1.setCompletedPercentage("0");
                    if (taskDetailsBean != null && taskDetailsBean.getTaskDescription() != null && ((Integer.valueOf(taskDetailsBean.getTaskDescription()) > (Integer.valueOf(taskDetailsBean1.getCompletedPercentage()))))) {
                        Intent intent1 = new Intent(context, SchedulerActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.putExtra("Taskid", taskid);
                        intent1.putExtra("customsetid", setid);
                        context.startActivity(intent1);
                    }
                } else if ((taskDetailsBean != null && taskDetailsBean.getTaskDescription() != null &&
                        (taskDetailsBean1 != null && taskDetailsBean1.getTaskStatus() != null && taskDetailsBean.getTaskDescription().equalsIgnoreCase(taskDetailsBean1.getTaskStatus()))
                        || (taskDetailsBean.getTaskTagName() != null && taskDetailsBean.getTaskTagName().equalsIgnoreCase("Conference Host")))
                        || taskDetailsBean.getProjectId() != null) {
                    Log.i("Schedule", "ScheduleCallReceiver status1 == > taskstatus :" + taskDetailsBean.getTaskStatus() + "taskstatus 1 :" + taskDetailsBean1.getTaskStatus());

                    Intent intent1 = new Intent(context, SchedulerActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.putExtra("Taskid", taskid);
                    intent1.putExtra("customsetid", setid);
                    context.startActivity(intent1);
                }
            }
            received_intent_value = null;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "scheduleCallAlert Exception: " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "scheduleCallAlert Exception: " + e.getMessage(), "WARN", null);
        }
    }

    private boolean isScheduleCallTimeValid(Date date) {
        boolean valid_date = false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date strDate = sdf.parse(valid_until);
            String cur_date_str = sdf.format(new Date());
            Date curr_date = sdf.parse(cur_date_str);

            Log.i("Schedule", "curr_date :" + curr_date + " date : " + date);

            if (curr_date.after(date)) {
                Log.i("Schedule", "Received date is not valid");
                valid_date = false;
            } else {
                Log.i("Schedule", "Received date is valid");
                valid_date = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "isScheduleCallTimeValid Exception: " + e.getMessage(), "WARN", null);
        }
        return valid_date;
    }

    private void cancellTask(TaskDetailsBean taskDetailsBean) {
//        if (xmlComposer == null) {
        xmlcomposer xmlComposer = new xmlcomposer();
//        }
        taskDetailsBean.setTaskDescription("Update Status Reminder Cancelled");
        String tasksipxml = xmlComposer.composeTaskDetailsinfo(taskDetailsBean);
        Log.i("Task", "composed tasksipxml : " + tasksipxml + " \n to user " + taskDetailsBean.getFromUserId());
        String tousersipendpoint = "sip:" + taskDetailsBean.getFromUserId() + "@" + getResources().getString(R.string.server_ip);
        for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
            String name = MainActivity.account.buddyList.get(i).cfg.getUri();

            if (name.equalsIgnoreCase(tousersipendpoint)) {
                MyBuddy myBuddy = MainActivity.account.buddyList.get(i);
                SendInstantMessageParam prm = new SendInstantMessageParam();
                prm.setContent(tasksipxml);
                boolean valid = myBuddy.isValid();
                Log.i("Task", "valid ======= " + valid);
                try {
                    myBuddy.sendInstantMessage(prm);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MainActivity", "cancellTask Exception: " + e.getMessage(), "WARN", null);
                }
            }
        }
    }

   /* public void afficher()
    {
        Toast.makeText(getBaseContext(),
                "test",
                Toast.LENGTH_SHORT).show();


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        if(VideoCallDataBase.getDB(mainContext).getTaskHistory() != null)
        taskDetailsBeen = VideoCallDataBase.getDB(mainContext).getTaskHistory();
        for (TaskDetailsBean taskDetailsBean:taskDetailsBeen) {

             freqDate = taskDetailsBean.getRemainderFrequency();

            if(currentDateandTime.equalsIgnoreCase(freqDate))
            {
                showTaskNotification(taskDetailsBean);
            }
        }
        handler.postDelayed(runnable, 1000);
    }
*/

    Handler ringback_timer = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            Log.i("incomingcallalert", "ringback-timer hangup called");
            if (Appreference.context_table.containsKey("incomingcallalert")) {
                MainActivity.stopRingTone();
                for (int i = 0; i < MainActivity.currentCallArrayList.size(); i++) {
                    CallOpParam prm = new CallOpParam();
                    prm.setStatusCode(pjsip_status_code.PJSIP_SC_DECLINE);
                    try {
                        MainActivity.currentCallArrayList.get(i).hangup(prm);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MainActivity", "cancellTask Exception: " + e.getMessage(), "WARN", null);
                    }
                }
                IncomingCallAlert incomingCallAlert = (IncomingCallAlert) Appreference.context_table.get("incomingcallalert");
                Log.i("MainCall", " 2150 " + Hostname);
                insertDb("MissedCall", Hostname);
                Log.i("MainCall", "after db 2150 " + Hostname);
                incomingCallAlert.finish();
            }
        }

    };

    private void insertDb(String calltype, String hostname) {
        Log.d("callhistory", "MainActivity insertdb");
        try {
            Date strt_dt = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss");
            call_strtime = sdf.format(strt_dt);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "insertDb Exception: " + e.getMessage(), "WARN", null);
        }
        String part1 = Appreference.loginuserdetails.getFirstName() + " " + Appreference.loginuserdetails.getLastName();
        Log.i("MainCall", "host " + hostname);
        Log.i("MainCall", "participant " + part1);
        String part = hostname;
        part = VideoCallDataBase.getDB(context).getName(hostname);
        Log.i("MainCall", "db 1" + calltype);
        Call_ListBean call_listBean = new Call_ListBean();
        Log.i("MainCall", "db 2" + part);
        call_listBean.setHost(part);
        Log.i("MainCall", "db 3" + part1);
        call_listBean.setParticipant(part1);
        Log.i("MainCall", "db 4  >>>>");
        call_listBean.setRecording_path(null);
        Log.i("MainCall", "db 5 00:00");
        call_listBean.setCall_duration("00:00");
        Log.i("MainCall", "db 6" + calltype);
        call_listBean.setType(calltype);
        Log.i("MainCall", "db 7" + call_strtime);
        call_listBean.setStart_time(call_strtime);
        Log.i("MainCall", "db 8" + calltype);
        VideoCallDataBase.getDB(context).insertCall_history(call_listBean, Appreference.loginuserdetails.getUsername());
        Log.i("MainCall", "db 9" + calltype);
    }

    public void listAllMyTaskwebservice() {
        try {
            List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(1);
            nameValuePairs1.add(new BasicNameValuePair("userId", String.valueOf(Appreference.loginuserdetails.getId())));
            Log.i("listAllMyTaskwebservice", "userId " + Appreference.loginuserdetails.getId());
            showprogress("Loading Your Tasks");
            if (Appreference.jsonRequestSender == null) {
                JsonRequestSender jsonRequestParser = new JsonRequestSender();
                Appreference.jsonRequestSender = jsonRequestParser;
                jsonRequestParser.start();
            }
            Log.i("wsTime123"," Ws listAllMyTask Request sent   Time====>"+Appreference.getCurrentDateTime());
            Appreference.jsonRequestSender.listAllMyTask(EnumJsonWebservicename.listAllMyTask, nameValuePairs1, MainActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "listAllMyTaskwebservice Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void LoadBackGroundWebService() {
        try {
            showprogress("Please wait");
            if (appSharedpreferences != null && !appSharedpreferences.getString("loginId").equals("0")) {
                Log.d("test1", "appSharedpreferences.getString" + appSharedpreferences.getString("loginId"));
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("userId", String.valueOf(appSharedpreferences.getString("loginId"))));
                if (Appreference.jsonRequestSender == null) {
                    JsonRequestSender jsonRequestParser = new JsonRequestSender();
                    Appreference.jsonRequestSender = jsonRequestParser;
                    jsonRequestParser.start();
                }
                Appreference.jsonRequestSender.ListUser(EnumJsonWebservicename.listUser, nameValuePairs, this);
                Appreference.jsonRequestSender.GroupDetails(EnumJsonWebservicename.listGroupMember, nameValuePairs, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "LoadBackGroundWebService Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void ListUserGroupMemberAccess(GroubDetails groubDetails) {
        try {
            List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(2);
            Log.i("GroupMemberAccess", "userId " + Appreference.loginuserdetails.getId());
            Log.i("GroupMemberAccess", "groupId " + groubDetails.getId());
            nameValuePairs1.add(new BasicNameValuePair("userId", String.valueOf(Appreference.loginuserdetails.getId())));
            nameValuePairs1.add(new BasicNameValuePair("groupId", String.valueOf(groubDetails.getId())));
            Log.i("GroupMemberAccess", "userId " + Appreference.loginuserdetails.getId());
            if (Appreference.jsonRequestSender == null) {
                JsonRequestSender jsonRequestParser = new JsonRequestSender();
                Appreference.jsonRequestSender = jsonRequestParser;
                jsonRequestParser.start();
            }
            GroupMemberAccess groupMemberAccess = new GroupMemberAccess();
            groupMemberAccess.setGroupId(String.valueOf(groubDetails.getId()));
            groupMemberAccess.setGroupName(groubDetails.getGroupName());
            Appreference.jsonRequestSender.listUserGroupMemberAccess(EnumJsonWebservicename.listUserGroupMemberAccess, nameValuePairs1, MainActivity.this, groupMemberAccess);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "ListUserGroupMemberAccess Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void GetDownloadPMS_ServiceManualsPDF() {
        try {
            if (!Appreference.PDF_ManualsdownloadStart) {
                Appreference.PDF_ManualsdownloadStart = true;
                JSONObject jsonObjectPDf= new JSONObject();
                jsonObjectPDf.put("userId", Appreference.loginuserdetails.getId());

                ArrayList<Integer> manualList = new ArrayList<>();
                ArrayList<PdfManualListBean> listBeen = VideoCallDataBase.getDB(context).getPDFnameList("select * from userManual");

                if (listBeen != null) {
                    for (int i = 0; i < listBeen.size(); i++) {
                        PdfManualListBean pdfBean = listBeen.get(i);
                        String pdf_file_downloads = Environment
                                .getExternalStorageDirectory().getAbsolutePath()
                                + "/High Message/servicemanual/" + pdfBean.getPdfFilePathName();
                        File pdfFile = new File(pdf_file_downloads);
                        if (pdfFile.exists()) {
                            manualList.add(pdfBean.getId());
                        }
                    }
                }
                JSONArray Multi_array = new JSONArray();
                if (manualList != null) {
                    for (int i = 0; i < manualList.size(); i++) {
                        Multi_array.put(i, manualList.get(i));
                    }
                }
                jsonObjectPDf.put("listallPDF", Multi_array);
                Appreference.jsonRequestSender.getPDFList(EnumJsonWebservicename.getUploadServiceManualList, jsonObjectPDf, MainActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "ListUserGroupMemberAccess Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void GroupMembers_WebService(String Grp_id) {
        Log.i("Groupchat", "GroupMembers_WebService " + Grp_id);
        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("groupId", Grp_id));
            if (Appreference.jsonRequestSender == null) {
                JsonRequestSender jsonRequestParser = new JsonRequestSender();
                Appreference.jsonRequestSender = jsonRequestParser;
                jsonRequestParser.start();
            }
            Appreference.jsonRequestSender.getGroup(EnumJsonWebservicename.getGroup, nameValuePairs, this);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "GroupMembers_WebService Exception: " + e.getMessage(), "WARN", null);
        }
    }

    @Override
    public void ResponceMethod(final Object object) {
        handler1.post(new Runnable() {
            @Override
            public void run() {
                CommunicationBean opr = (CommunicationBean) object;
                String s1 = opr.getEmail();
                String s2 = opr.getFirstname();

                Log.i("Responce", "ValueGroup1 s1 ------------> " + s1);
                Log.i("Responce", "ValueGroup1 s2 ------------> " + s2);
                Log.i("MainActivity", "Responce NewContact ");
                try {
                    if (s2.equalsIgnoreCase("taskConversationEntry")) {
                        //                    cancelDialog();
                        Appreference.project_taskConversationEntry = false;
                        //                    Toast.makeText(MainActivity.this, "getProject date response", Toast.LENGTH_SHORT).show();
                        Log.i("Responce", "ValueGroup3--------------> " + s1);
                    } else if (s2.equalsIgnoreCase("getProject")) {
                        try {
                            Type collectionType = new TypeToken<ProjectDetailsBean>() {
                            }.getType();
                            ProjectDetailsBean pdb = new Gson().fromJson(s1, collectionType);
                            Log.i("Response", "MainActivity getProject name " + pdb.getProjectName());
//                            VideoCallDataBase.getDB(mainContext).insertProject_history(pdb);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("MainActivity getProject", "ResponceMethod Exception: " + e.getMessage(), "WARN", null);
                        }
                    }
                    if (s2.equalsIgnoreCase("listAllMyProject")) {
                        Log.i("listAllMyProject", "webservice response " + s1);
                        try {
                            JSONArray jr = new JSONArray(s1);
                            if (jr.length() > 0) {
                                JSONObject jb = jr.getJSONObject(0);
                                if (jb.has("projectName")) {
                                    Type collectionType = new TypeToken<List<ProjectDetailsBean>>() {
                                    }.getType();
                                    List<ProjectDetailsBean> pdb = new Gson().fromJson(s1, collectionType);
                                    for (int i = 0; i < pdb.size(); i++) {
                                        ProjectDetailsBean projectDetailsBean = pdb.get(i);
                                        VideoCallDataBase.getDB(mainContext).insertProject_Details(projectDetailsBean);
                                        Log.i("listAllMyProject", "projectDetailsBean DB inserted id is " + projectDetailsBean.getId());
                                    }
                                }
                            }
                            if (Appreference.context_table.containsKey("projectfragment")) {
                                ProjectsFragment projectfragment = (ProjectsFragment) Appreference.context_table.get("projectfragment");
                                if (projectfragment != null) {
                                    Log.d("TaskHistory", "Response Method MainActivity");
                                    projectfragment.notifyNewProjectReceived();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("MainActivity listAllMyProject", "ResponceMethod Exception: " + e.getMessage(), "WARN", null);
                        }
                    } else if (!s2.equalsIgnoreCase("getgroup") && !s2.equalsIgnoreCase("listUserGroupMemberAccess") && !s2.equalsIgnoreCase("listAllMyTask") && !s2.equalsIgnoreCase("getUploadServiceManualList")) {
                        cancelDialog();
                        try {
                            JSONArray jr = new JSONArray(s1);
                            if (jr.length() > 0) {
                                Log.i("MainActivity", "Responce NewContact 1 ");
                                JSONObject jb = jr.getJSONObject(0);
                                if (jb.has("groupName")) {
                                    Log.i("MainActivity", "Responce NewContact 2 ");
                                    Type collectionType = new TypeToken<List<GroubDetails>>() {
                                    }.getType();
                                    List<GroubDetails> lcs = new Gson().fromJson(s1, collectionType);
                                    for (int i = 0; i < lcs.size(); i++) {
                                        Log.i("MainActivity", "Responce NewContact 3 ");
                                        GroubDetails groubDetails = lcs.get(i);
                                        Log.i("GroupDetails", "value" + "\n" + groubDetails.getGroupName());
                                        Log.i("GroupDetails", "value-->" + groubDetails.getListMember().size());
                                        Log.i("GroupDetails", "logo-->" + groubDetails.getLogo());
                                        ArrayList<ListMember> u2 = groubDetails.getListMember();
                                        for (int j = 0; j < u2.size(); j++) {
                                            Log.i("MainActivity", "Responce NewContact 4 ");
                                            ListMember u31 = u2.get(j);
                                            Log.i("User group Id", "value-->" + u31.getFirstName());
                                            VideoCallDataBase.getDB(mainContext).insertGroupMember_history(u31, groubDetails.getGroupName(), groubDetails.getId());
                                        }
                                        Log.i("MainActivity", "Responce NewContact 5 ");
                                        Appreference.groupprofilePictures.add(groubDetails.getLogo());
                                        VideoCallDataBase.getDB(mainContext).insertGroup_history(groubDetails, username);
                                        ListUserGroupMemberAccess(groubDetails);
                                    }
                                } else {
                                    Log.i("MainActivity", "Responce NewContact 6 ");
                                    Type collectionType = new TypeToken<List<ListUserGroupObject>>() {
                                    }.getType();
                                    List<ListUserGroupObject> lcs = (List<ListUserGroupObject>) new Gson().fromJson(s1, collectionType);
                                    Log.i("String value", "Value--->" + lcs.size());
                                    for (int i = 0; i < lcs.size(); i++) {
                                        Log.i("MainActivity", "Responce NewContact 7 ");
                                        ListUserGroupObject listUserGroupObject = lcs.get(i);
                                        Log.i("GroubValue", "Value123" + "\n" + listUserGroupObject.getFirstName() + "\n" + listUserGroupObject.getEmail() + "\n" + listUserGroupObject.getJobCategory1());
                                        VideoCallDataBase.getDB(mainContext).insertContact_history(listUserGroupObject, username);
                                    }
                                    // create sip registeration


                                    Log.i("contacts", "Response Method");
                                    if (Appreference.context_table.containsKey("contactsfragment")) {
                                        Log.i("contacts", "Response Method 1");
                                        Log.i("updateContacts123", "show dialog......");
                                        ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
//                                        showprogress("Updating Contacts.....");
                                        contactsFragment.referesh();
                                        contactsFragment.ChangeLoginUserStatus("Online", "online");
                                    }
                                }
                                if (Appreference.loginuserdetails.getProfileImage() != null) {
                                    new Profile().execute("image");
                                }
                                if (Appreference.profilePictures != null && Appreference.profilePictures.size() > 0) {
                                    new DownloadProfile().execute("image");
                                }

                                if (Appreference.groupprofilePictures != null && Appreference.groupprofilePictures.size() > 0) {
                                    new DownloadGroupProfile().execute("image");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("MainActivity", "ResponceMethod Exception: " + e.getMessage(), "WARN", null);
                        }
                    } else if (!s2.equalsIgnoreCase("getgroup") && !s2.equalsIgnoreCase("listUserGroupMemberAccess") && s2.equalsIgnoreCase("listAllMyTask")) {
                        cancelDialog();
                        Log.i("task", "jelement.getAsJsonObject() != null all task");
                        Log.i("wsTime123"," Ws listAllMyTask Response received Time====>"+Appreference.getCurrentDateTime());

                        Gson gson = new Gson();
                        Type collectionType = new TypeToken<List<ListAllTaskBean>>() {
                        }.getType();
                        List<ListAllTaskBean> lcs = new Gson().fromJson(s1, collectionType);
                        //                    Log.d("listAllMyTask", "1 firstname  " + lcs.size());
                        Log.i("wsTime123"," Ws listAllMyTask After GSON parsing Time====>"+Appreference.getCurrentDateTime());
                        Log.i("wsTime123"," Ws listAllMyTask Before DB Entry Time====>"+Appreference.getCurrentDateTime());

                        try {
                            for (int i = 0; i < lcs.size(); i++) {
                                ListAllTaskBean listAllTaskbean = lcs.get(i);
                                Log.d("listAllMyTask", "2 firstname  " + i);
                                Log.d("listAllMyTask", "3 firstname  " + listAllTaskbean.getId());
                                VideoCallDataBase.getDB(context).insertORupdate_ListAllTaskDetails(listAllTaskbean);
                            }
                            Log.i("wsTime123"," Ws listAllMyTask After DB Entry Time====>"+Appreference.getCurrentDateTime());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("MainActivity listAllMyTask", "ResponceMethod Exception: " + e.getMessage(), "WARN", null);
                        }
                    } else if (!s2.equalsIgnoreCase("getgroup") && !s2.equalsIgnoreCase("listAllMyTask") && s2.equalsIgnoreCase("listUserGroupMemberAccess")) {
                        JsonElement jelement = new JsonParser().parse(s1);
                        Log.i("expand", " Responce listUserGroupMemberAccess " + s1);
                        try {
                            if (jelement.getAsJsonObject() != null) {
                                Log.i("expand", "ContactFragment -->1 if");
                                JsonObject jobject = jelement.getAsJsonObject();
                                if (jobject != null && jobject.has("Audio Access")) {
                                    Log.i("expand", "ContactFragment -->2 if");
                                    if (jobject.has("Respond Video"))
                                        Respond_Video = jobject.get("Respond Video").toString();
                                    if (jobject.has("Respond Files"))
                                        Respond_Files = jobject.get("Respond Files").toString();
                                    if (jobject.has("Access Forms"))
                                        Access_Forms = jobject.get("Access Forms").toString();
                                    if (jobject.has("Respond Audio"))
                                        Respond_Audio = jobject.get("Respond Audio").toString();
                                    if (jobject.has("Video Access"))
                                        Video_Access = jobject.get("Video Access").toString();
                                    if (jobject.has("Admin Access"))
                                        Admin_Access = jobject.get("Admin Access").toString();
                                    if (jobject.has("Respond DateChange"))
                                        Respond_DateChange = jobject.get("Respond DateChange").toString();
                                    if (jobject.has("Respond Location"))
                                        Respond_Location = jobject.get("Respond Location").toString();
                                    if (jobject.has("Respond ConfCall"))
                                        Respond_ConfCall = jobject.get("Respond ConfCall").toString();
                                    if (jobject.has("Audio Access"))
                                        Audio_Access = jobject.get("Audio Access").toString();
                                    if (jobject.has("Chat Access"))
                                        Chat_Access = jobject.get("Chat Access").toString();
                                    if (jobject.has("Respond Text"))
                                        Respond_Text = jobject.get("Respond Text").toString();
                                    if (jobject.has("Respond Private"))
                                        Respond_Private = jobject.get("Respond Private").toString();
                                    if (jobject.has("Respond Photo"))
                                        Respond_Photo = jobject.get("Respond Photo").toString();
                                    if (jobject.has("Access Reminder"))
                                        Access_Reminder = jobject.get("Access Reminder").toString();
                                    if (jobject.has("Respond Sketch"))
                                        Respond_Sketch = jobject.get("Respond Sketch").toString();
                                    if (jobject.has("Respond Task"))
                                        Respond_Task = jobject.get("Respond Task").toString();
                                    if (jobject.has("Access ScheduledCNF"))
                                        Access_ScheduledCNF = jobject.get("Access ScheduledCNF").toString();
                                    if (jobject.has("Group Task"))
                                        Group_Task = jobject.get("Group Task").toString();
                                    if (jobject.has("Change TaskName"))
                                        ChangeTaskName = jobject.get("Change TaskName").toString();
                                    if (jobject.has("Create Template Existing Task"))
                                        TemplateExistingTask = jobject.get("Create Template Existing Task").toString();
                                    if (jobject.has("Add Observer"))
                                        AddObserver = jobject.get("Add Observer").toString();
                                    if (jobject.has("Reassign Task"))
                                        ReassignTask = jobject.get("Reassign Task").toString();
                                    if (jobject.has("Task Priority"))
                                        TaskPriority = jobject.get("Task Priority").toString();
                                    if (jobject.has("Escalations"))
                                        Escalations = jobject.get("Escalations").toString();
                                    if (jobject.has("Approve Leave"))
                                        ApproveLeave = jobject.get("Approve Leave").toString();
                                    if (jobject.has("Remind Me"))
                                        RemindMe = jobject.get("Remind Me").toString();
                                    if (jobject.has("Task Descriptions"))
                                        TaskDescriptions = jobject.get("Task Descriptions").toString();

                                    GroupMemberAccess groupMemberAccess = new GroupMemberAccess();
                                    GroupMemberAccess groupMemberAccess1 = opr.getGroupMemberAccess();
                                    if (Access_Forms != null && !Access_Forms.equalsIgnoreCase("")) {
                                        groupMemberAccess.setAccessForms(Access_Forms);
                                    } else {
                                        groupMemberAccess.setAccessForms("1");
                                    }
                                    if (Access_Reminder != null && !Access_Reminder.equalsIgnoreCase("")) {
                                        groupMemberAccess.setAccessReminder(Access_Reminder);
                                    } else {
                                        groupMemberAccess.setAccessReminder("1");
                                    }
                                    if (Access_ScheduledCNF != null && !Access_ScheduledCNF.equalsIgnoreCase("")) {
                                        groupMemberAccess.setAccessScheduledCNF(Access_ScheduledCNF);
                                    } else {
                                        groupMemberAccess.setAccessScheduledCNF("1");
                                    }
                                    if (Admin_Access != null && !Admin_Access.equalsIgnoreCase("")) {
                                        groupMemberAccess.setAdminAccess(Admin_Access);
                                    } else {
                                        groupMemberAccess.setAdminAccess("1");
                                    }
                                    if (Audio_Access != null && !Audio_Access.equalsIgnoreCase("")) {
                                        groupMemberAccess.setAudioAccess(Audio_Access);
                                    } else {
                                        groupMemberAccess.setAudioAccess("1");
                                    }
                                    if (Chat_Access != null && !Chat_Access.equalsIgnoreCase("")) {
                                        groupMemberAccess.setChatAccess(Chat_Access);
                                    } else {
                                        groupMemberAccess.setChatAccess("1");
                                    }
                                    groupMemberAccess.setGroupId(groupMemberAccess1.getGroupId());
                                    groupMemberAccess.setGroupName(groupMemberAccess1.getGroupName());
                                    groupMemberAccess.setUserid(String.valueOf(Appreference.loginuserdetails.getId()));
                                    if (Respond_Audio != null && !Respond_Audio.equalsIgnoreCase("")) {
                                        groupMemberAccess.setRespondAudio(Respond_Audio);
                                    } else {
                                        groupMemberAccess.setRespondAudio("1");
                                    }
                                    if (Respond_ConfCall != null && !Respond_ConfCall.equalsIgnoreCase("")) {
                                        groupMemberAccess.setRespondConfCall(Respond_ConfCall);
                                    } else {
                                        groupMemberAccess.setRespondConfCall("1");
                                    }
                                    if (Respond_DateChange != null && !Respond_DateChange.equalsIgnoreCase("")) {
                                        groupMemberAccess.setRespondDateChange(Respond_DateChange);
                                    } else {
                                        groupMemberAccess.setRespondDateChange("1");
                                    }
                                    if (Respond_Location != null && !Respond_Location.equalsIgnoreCase("")) {
                                        groupMemberAccess.setRespondLocation(Respond_Location);
                                    } else {
                                        groupMemberAccess.setRespondLocation("1");
                                    }
                                    if (Respond_Photo != null && !Respond_Photo.equalsIgnoreCase("")) {
                                        groupMemberAccess.setRespondPhoto(Respond_Photo);
                                    } else {
                                        groupMemberAccess.setRespondPhoto("1");
                                    }
                                    if (Respond_Files != null && !Respond_Files.equalsIgnoreCase("")) {
                                        groupMemberAccess.setRespondFiles(Respond_Files);
                                    } else {
                                        groupMemberAccess.setRespondFiles("1");
                                    }
                                    if (Respond_Private != null && !Respond_Private.equalsIgnoreCase("")) {
                                        groupMemberAccess.setRespondPrivate(Respond_Private);
                                    } else {
                                        groupMemberAccess.setRespondPrivate("1");
                                    }
                                    if (Respond_Sketch != null && !Respond_Sketch.equalsIgnoreCase("")) {
                                        groupMemberAccess.setRespondSketch(Respond_Sketch);
                                    } else {
                                        groupMemberAccess.setRespondSketch("1");
                                    }
                                    if (Respond_Text != null && !Respond_Text.equalsIgnoreCase("")) {
                                        groupMemberAccess.setRespondText(Respond_Text);
                                    } else {
                                        groupMemberAccess.setRespondText("1");
                                    }
                                    if (Respond_Task != null && !Respond_Task.equalsIgnoreCase("")) {
                                        groupMemberAccess.setRespondTask(Respond_Task);
                                    } else {
                                        groupMemberAccess.setRespondTask("1");
                                    }
                                    if (Video_Access != null && !Video_Access.equalsIgnoreCase("")) {
                                        groupMemberAccess.setVideoAccess(Video_Access);
                                    } else {
                                        groupMemberAccess.setVideoAccess("1");
                                    }
                                    if (Respond_Video != null && !Respond_Video.equalsIgnoreCase("")) {
                                        groupMemberAccess.setRespondVideo(Respond_Video);
                                    } else {
                                        groupMemberAccess.setRespondVideo("1");
                                    }
                                    if (Group_Task != null && !Group_Task.equalsIgnoreCase("")) {
                                        groupMemberAccess.setGroup_Task(Group_Task);
                                    } else {
                                        groupMemberAccess.setGroup_Task("1");
                                    }
                                    if (ChangeTaskName != null && !ChangeTaskName.equalsIgnoreCase("")) {
                                        groupMemberAccess.setChangeTaskName(ChangeTaskName);
                                    } else {
                                        groupMemberAccess.setChangeTaskName("1");
                                    }
                                    if (TemplateExistingTask != null && !TemplateExistingTask.equalsIgnoreCase("")) {
                                        groupMemberAccess.setTemplateExistingTask(TemplateExistingTask);
                                    } else {
                                        groupMemberAccess.setTemplateExistingTask("1");
                                    }
                                    if (AddObserver != null && !AddObserver.equalsIgnoreCase("")) {
                                        groupMemberAccess.setAddObserver(AddObserver);
                                    } else {
                                        groupMemberAccess.setAddObserver("1");
                                    }
                                    if (ReassignTask != null && !ReassignTask.equalsIgnoreCase("")) {
                                        groupMemberAccess.setReassignTask(ReassignTask);
                                        Log.i("groupMemberAccess", "ReassignTask MA ** " + ReassignTask + groupMemberAccess.getGroupId());
                                    } else {
                                        groupMemberAccess.setReassignTask("1");
                                        Log.i("groupMemberAccess", "ReassignTask MA ## " + ReassignTask + groupMemberAccess.getGroupId());
                                    }
                                    Log.i("groupMemberAccess", "ReassignTask MA " + ReassignTask + groupMemberAccess.getGroupId());
                                    if (Escalations != null && !Escalations.equalsIgnoreCase("")) {
                                        groupMemberAccess.setEscalations(Escalations);
                                    } else {
                                        groupMemberAccess.setEscalations("1");
                                    }
                                    if (TaskPriority != null && !TaskPriority.equalsIgnoreCase("")) {
                                        groupMemberAccess.setTaskPriority(TaskPriority);
                                    } else {
                                        groupMemberAccess.setTaskPriority("1");
                                    }
                                    if (ApproveLeave != null && !ApproveLeave.equalsIgnoreCase("")) {
                                        groupMemberAccess.setApproveLeave(ApproveLeave);
                                    } else {
                                        groupMemberAccess.setApproveLeave("1");
                                    }
                                    if (RemindMe != null && !RemindMe.equalsIgnoreCase("")) {
                                        groupMemberAccess.setRemindMe(RemindMe);
                                    } else {
                                        groupMemberAccess.setRemindMe("1");
                                    }
                                    if (TaskDescriptions != null && !TaskDescriptions.equalsIgnoreCase("")) {
                                        groupMemberAccess.setTaskDescriptions(TaskDescriptions);
                                    } else {
                                        groupMemberAccess.setTaskDescriptions("1");
                                    }
                                    VideoCallDataBase.getDB(context).insertListMemberAccess_history(groupMemberAccess, username);
                                }
                            }
                            /*PDF Download
                            * Method To download ,users uploaded PMS Service manuals From Server
                            * call webservice to get list of pdf files to download
                            * */
                            GetDownloadPMS_ServiceManualsPDF();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("MainActivity listUserGroupMemberAccess", "ResponceMethod Exception: " + e.getMessage(), "WARN", null);
                        }
                    } else if (s2.equalsIgnoreCase("getUploadServiceManualList")) {
                        Log.i("pdf123", "getUploadServiceManualList reponse MAINACTIVITY ---->  " + s2);
                        try {
                            JSONArray jr = new JSONArray(s1);
                            if (jr.length() > 0) {
                                Type collectionType = new TypeToken<List<PdfManualListBean>>() {
                                }.getType();
                                List<PdfManualListBean> pdfManualListBeen = new Gson().fromJson(s1, collectionType);
                                Log.i("pdf123", "getUploadServiceManualList pdfManualListBeen size ---->  " + pdfManualListBeen.size());
//                                VideoCallDataBase.getDB(context).insertOrDelete_PDFmanual(pdfManualListBeen);

                                for (int i = 0; i < pdfManualListBeen.size(); i++) {
                                    PdfManualListBean pdfBean = pdfManualListBeen.get(i);

                                    if (pdfBean != null && pdfBean.getStatus() != null && !pdfBean.getStatus().toString().equalsIgnoreCase("")
                                            && pdfBean.getStatus().toString().equalsIgnoreCase("1")) {
                                        String pdf_file_downloads = Environment
                                                .getExternalStorageDirectory().getAbsolutePath()
                                                + "/High Message/servicemanual/" + pdfBean.getPdfFilePathName();
                                        File pdfFile = new File(pdf_file_downloads);
                                        if (pdfFile.exists())
                                            pdfFile.delete();
                                    } else {
                                        String pdf_file_downloads = Environment
                                                .getExternalStorageDirectory().getAbsolutePath()
                                                + "/High Message/servicemanual/" + pdfBean.getPdfFilePathName();
                                        File pdfFile = new File(pdf_file_downloads);
                                        if (!pdfFile.exists() && pdfBean.getPdfFilePathName() != null && !pdfBean.getPdfFilePathName().equalsIgnoreCase("")) {
                                            Appreference.do_downloadForServiceManualPDF_Path=Environment.getExternalStorageDirectory().getAbsolutePath()
                                                    + "/High Message/servicemanual/";
                                            new DownloadImage(getResources().getString(R.string.file_upload) + pdfBean.getPdfFilePathName(), pdfBean.getPdfFilePathName()).execute();
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        cancelDialog();
                        Log.i("Responce", "ValueGroup3  getGroup ---->  " + s1);
                        Log.i("MainActivity", "Responce NewContact 8* ");
                        try {
                            username = Appreference.loginuserdetails.getUsername();
                            Gson g1 = new Gson();
                            GroubDetails groubDetails = g1.fromJson(s1, GroubDetails.class);
                            ArrayList<ListMember> u2 = groubDetails.getListMember();
                            ArrayList<String> contactId_List = VideoCallDataBase.getDB(mainContext).selectContactList("select * from contact");
                            for (int j = 0; j < u2.size(); j++) {
                                Log.i("MainActivity", "Responce NewContact 9* ");
                                ListMember u31 = u2.get(j);
                                Log.i("User group Id", "value-->" + u31.getFirstName());
                                if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(u31.getUsername())) {
                                    Log.i("MainActivity", "Responce NewContact 10* ");
                                    VideoCallDataBase.getDB(mainContext).insertGroupMember_history(u31, groubDetails.getGroupName(), groubDetails.getId());
                                }
                                if (!contactId_List.contains(u31.getUsername())) {
                                    Log.i("MainActivity", "Responce NewContact 11* ");
                                    if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(u31.getUsername())) {
                                        if (!u31.getUsername().contains("king3_co")) {
                                            Log.i("MainActivity", "Responce NewContact 12* ");
                                            VideoCallDataBase.getDB(mainContext).insertNewContact_history(u31, username);
                                        }
                                    }

                                }
                            }
                            Appreference.groupprofilePictures.add(groubDetails.getLogo());
                            VideoCallDataBase.getDB(mainContext).insertGroup_history(groubDetails, username);
                            if (Appreference.context_table.containsKey("contactsfragment")) {
                                Log.i("contacts", "Response Method 1");
                                ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
                                contactsFragment.referesh();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("MainActivity getGroup", "ResponceMethod Exception: " + e.getMessage(), "WARN", null);
                        }
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    Appreference.printLog("MainActivity getGroup", "ResponceMethod Exception: " + e.getMessage(), "WARN", null);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MainActivity getGroup", "ResponceMethod Exception: " + e.getMessage(), "WARN", null);
                }
            }

        });
    }

    @Override
    public void ErrorMethod(Object object) {

    }


    public boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = null;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "isNetworkAvailable Exception: " + e.getMessage(), "WARN", null);
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public class DownloadProfile extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                Log.i("profiledownload", "profile download");
                if (isNetworkAvailable()) {
                    String url = getResources().getString(R.string.user_upload);

                    for (String profile : Appreference.profilePictures) {
                        if (profile != null) {
                            File extStore = Environment.getExternalStorageDirectory();
                            File myFile = new File(extStore.getAbsolutePath() + "/High Message/profilePic/" + profile);
                            Log.i("profiledownload", "profile download");
                            Log.i("profiledownload", "file " + myFile.toString());
                            if (!myFile.exists()) {
                                try {



                                  /*  HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                                        @Override
                                        public boolean verify(String hostname, SSLSession session) {
                                            HostnameVerifier hv =
                                                    HttpsURLConnection.getDefaultHostnameVerifier();
                                            return hv.verify("example.com", session);
                                        }
                                    };*/

// Tell the URLConnection to use our HostnameVerifier



                                   /* Log.i("profiledownload", "file not exit--->" + profile);
                                    URL bitmap = new URL(url + profile);
//                                    HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
//                                    HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
                                    HttpsURLConnection connection = (HttpsURLConnection) bitmap
                                            .openConnection();
//                                    connection.setDefaultHostnameVerifier(hostnameVerifier);
                                    connection.setDoInput(true);*/
                                    URL bitmap = new URL(url + profile);

                                    Log.i("profiledownload", "profile bitmap" + bitmap);
                                    HttpURLConnection connection =
                                            (HttpURLConnection) bitmap.openConnection();
//                                    connection.setHostnameVerifier(hostnameVerifier);
                                    InputStream in = connection.getInputStream();
//                                    copyInputStreamToOutputStream(in, System.out);
                                    connection.connect();
                                    if (connection.getInputStream() != null) {
                                        InputStream inputStream = connection.getInputStream();
                                        String dir_path = Environment.getExternalStorageDirectory()
                                                + "/High Message/profilePic";
                                        Log.i("profiledownload", "profile dir_path" + dir_path);
                                        File directory = new File(dir_path);
                                        if (!directory.exists()) {
                                            directory.mkdirs();
                                        }
                                        String filePath = Environment.getExternalStorageDirectory()
                                                .getAbsolutePath()
                                                + "/High Message/profilePic/"
                                                + profile;

                                        Log.d("profiledownload", "my file path is---->" + filePath);

                                        File file_name = new File(filePath);
                                        BufferedOutputStream bos = new BufferedOutputStream(
                                                new FileOutputStream(file_name));
                                        int inByte;
                                        while ((inByte = inputStream.read()) != -1) {
                                            bos.write(inByte);
                                        }
                                        bos.close();

                                        //** For  Avatar download
                                        //Start
                                        Bitmap btmap = BitmapFactory.decodeStream(inputStream);
                                        if (connection.getContentLength() <= 0 && btmap == null) {
                                            Log.i("downavatar", "avatar download faild contact name-->");

                                        } else {
                                            Log.i("downavatar", "avatar download success contact name-->");
                                        }
                                        //End
                                        Appreference.profile_image.put(profile, filePath);
                                    }
                                } catch (FileNotFoundException e) {
//                                    e.printStackTrace();
                                    Appreference.printLog("MainActivity", "DownloadProfile Exception: " + e.getMessage(), "WARN", null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("MainActivity", "DownloadProfile Exception: " + e.getMessage(), "WARN", null);
                                }
                            } else {
                                Appreference.profile_image.put(profile, myFile.toString());
                                Log.i("profiledownload", "file exit--->" + profile);
                            }
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "DownloadProfile Exception: " + e.getMessage(), "WARN", null);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (Appreference.context_table.containsKey("contacts_fragment_layout")) {


                    ContactsFragment contactsFragment = new ContactsFragment();
                    // contactsFragment.getRetainInstance();
                    contactsFragment.referesh();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "DownloadProfile Exception: " + e.getMessage(), "WARN", null);
            }

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }
    }


    public class DownloadGroupProfile extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                Log.i("groupload", "profile download");
                if (isNetworkAvailable()) {
                    String url = getResources().getString(R.string.group_profile);

                    for (String profile : Appreference.groupprofilePictures) {
                        if (profile != null) {
                            File extStore = Environment.getExternalStorageDirectory();
                            File myFile = new File(extStore.getAbsolutePath() + "/High Message/groupPic/" + profile);

                            Log.i("profiledownload", "file " + myFile.toString());
                            if (!myFile.exists()) {
                                try {



                                  /*  HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                                        @Override
                                        public boolean verify(String hostname, SSLSession session) {
                                            HostnameVerifier hv =
                                                    HttpsURLConnection.getDefaultHostnameVerifier();
                                            return hv.verify("example.com", session);
                                        }
                                    };*/

// Tell the URLConnection to use our HostnameVerifier



                                   /* Log.i("profiledownload", "file not exit--->" + profile);
                                    URL bitmap = new URL(url + profile);
//                                    HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
//                                    HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
                                    HttpsURLConnection connection = (HttpsURLConnection) bitmap
                                            .openConnection();
//                                    connection.setDefaultHostnameVerifier(hostnameVerifier);
                                    connection.setDoInput(true);*/
                                    URL bitmap = new URL(url + profile);
//                                    URL bitmap = new URL("http://122.165.92.171:8080/uploads/highmessaging/group/201607221534512592969.jpg");
                                    Log.i("groupdownload", "profile bitmap" + bitmap);
                                    HttpURLConnection connection = (HttpURLConnection) bitmap.openConnection();
//                                    connection.setHostnameVerifier(hostnameVerifier);
                                    Log.i("bc", "bc");
//                                    InputStream in = connection.getInputStream();
                                    Log.i("bcc", "bcc");
//                                    copyInputStreamToOutputStream(in, System.out);
                                    connection.connect();
                                    Log.i("ac", "ac");
                                    if (connection.getInputStream() != null) {
                                        InputStream inputStream = connection.getInputStream();
                                        String dir_path = Environment.getExternalStorageDirectory()
                                                + "/High Message/groupPic";
                                        Log.i("groupdownload", "profile dir_path" + dir_path);
                                        File directory = new File(dir_path);
                                        if (!directory.exists()) {
                                            Log.i("nogroup", "nogroup");
                                            directory.mkdirs();
                                            Log.i("yesgroup", "yesgroup");
                                        }
                                        String filePath = Environment.getExternalStorageDirectory()
                                                .getAbsolutePath()
                                                + "/High Message/groupPic/"
                                                + profile;

                                        Log.d("profiledownload", "my file path is---->" + filePath);

                                        File file_name = new File(filePath);
                                        BufferedOutputStream bos = new BufferedOutputStream(
                                                new FileOutputStream(file_name));
                                        int inByte;
                                        while ((inByte = inputStream.read()) != -1) {
                                            bos.write(inByte);
                                        }
                                        bos.close();

                                        //** For  Avatar download
                                        //Start
                                        Bitmap btmap = BitmapFactory.decodeStream(inputStream);
                                        if (connection.getContentLength() <= 0 && btmap == null) {
                                            Log.i("downavatar", "avatar download faild contact name-->");

                                        } else {
                                            Log.i("downavatar", "avatar download success contact name-->");
                                        }
                                        //End
                                        Appreference.profile_image.put(profile, filePath);
                                        //onPostExecute();

                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                    Appreference.printLog("MainActivity", "DownloadGroupProfile Exception: " + e.getMessage(), "WARN", null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("MainActivity", "DownloadGroupProfile Exception: " + e.getMessage(), "WARN", null);
                                }
                            } else {
                                Appreference.profile_image.put(profile, myFile.toString());
                                Log.i("profiledownload", "file exit--->" + profile);
                            }
                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "DownloadGroupProfile Exception: " + e.getMessage(), "WARN", null);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (Appreference.context_table.containsKey("contacts_fragment_layout")) {
                    ContactsFragment contactsFragment = new ContactsFragment();
                    // contactsFragment.getRetainInstance();
                    contactsFragment.referesh();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity onPostExecute", "DownloadGroupProfile Exception: " + e.getMessage(), "WARN", null);
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    public static HttpClient getHttpsClient(HttpURLConnection client) {
        try {
            X509TrustManager x509TrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, null);
            org.apache.http.conn.ssl.SSLSocketFactory sslSocketFactory = new ExSSLSocketFactory(sslContext);
            sslSocketFactory.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);


            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);


            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "getHttpsClient Exception: " + e.getMessage(), "WARN", null);
            return new DefaultHttpClient();
        }
    }


    public HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "getNewHttpClient Exception: " + e.getMessage(), "WARN", null);
            return new DefaultHttpClient();
        }
    }


    private HttpsURLConnection prepareConnection(final URL verifierURL)
            throws IOException {

        final HttpsURLConnection connection = (HttpsURLConnection) verifierURL
                .openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type",
                "application/json; charset=utf-8");
        connection.setRequestProperty("Accept-Charset", "utf-8");
        connection.setDoOutput(true);

        return connection;
    }


    public void downloadFile(String uRl) {
        try {
            File direct = new File(Environment.getExternalStorageDirectory()
                    + "/High Message/downloads");

            if (!direct.exists()) {
                direct.mkdirs();
                Log.d("task", "folder create done");
            }

            DownloadManager mgr = (DownloadManager) mainContext.getSystemService(Context.DOWNLOAD_SERVICE);

            Uri downloadUri = Uri.parse("https://edurev.in/newspaper/wp-content/uploads/2015/12/Low-Quality.jpg");
            String profile = uRl.split("chat/")[1];
            DownloadManager.Request request = new DownloadManager.Request(
                    downloadUri);

            Log.d("task", "profile create done" + profile);

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false).setTitle("Demo")
                    .setDescription("Something useful. No, really.")
                    .setDestinationInExternalPublicDir(direct.toString(), profile);

            mgr.enqueue(request);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "downloadFile Exception: " + e.getMessage(), "WARN", null);
        }

    }


    public class DownloadImage extends AsyncTask<String, Void, String> {
        String downloadImageurl;
        TaskDetailsBean detailsBean = null;
        String namevalue = "";

        public DownloadImage(String url, String name) {
            this.downloadImageurl = url;
            namevalue = name;
        }

        public DownloadImage(TaskDetailsBean name) {
            detailsBean = name;
            this.downloadImageurl = getResources().getString(R.string.task_reminder) + name.getTaskDescription();
            namevalue = name.getTaskDescription();
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                Log.i("profiledownload", "downloadImageurl " + downloadImageurl);
                if (isNetworkAvailable()) {
//                    String url ="http://122.165.92.171:8080/uploads/highmessaging/user/";
                    Log.i("profiledownload", "profile download ");
                    String profile1 = null;
                    if (downloadImageurl != null) {
                        if (downloadImageurl.contains("task"))
                            profile1 = downloadImageurl.split("task/")[1];
                        else
                            profile1 = downloadImageurl.split("chat/")[1];
                        Log.i("profiledownload", "profile1  " + profile1);
                        File extStore = Environment.getExternalStorageDirectory();
                        File myFile = new File(Appreference.do_downloadForServiceManualPDF_Path + profile1);
                        Log.i("profiledownload", "file " + myFile.toString());
                        if (!myFile.exists()) {
                            try {

                                Appreference.doTrustToCertificates();//
                                URL bitmap = new URL(downloadImageurl);

                                Log.i("profiledownload", "profile bitmap " + bitmap);
                                HttpURLConnection connection =
                                        (HttpURLConnection) bitmap.openConnection();
//                                    connection.setHostnameVerifier(hostnameVerifier);
                                InputStream in = connection.getInputStream();
//                                    copyInputStreamToOutputStream(in, System.out);
                                connection.connect();
                                if (connection.getInputStream() != null) {
                                    InputStream inputStream = connection.getInputStream();
                                    String dir_path =Appreference.do_downloadForServiceManualPDF_Path;
                                    Log.i("profiledownload", "profile dir_path" + dir_path);
                                    File directory = new File(dir_path);
                                    if (!directory.exists())
                                        directory.mkdir();
                                    String filePath = Appreference.do_downloadForServiceManualPDF_Path+ profile1;

                                    Log.d("profiledownload", "my file path is---->" + filePath);

                                    File file_name = new File(filePath);
                                    BufferedOutputStream bos = new BufferedOutputStream(
                                            new FileOutputStream(file_name));
                                    int inByte;
                                    while ((inByte = inputStream.read()) != -1) {
                                        bos.write(inByte);
                                    }
                                    bos.close();

                                    //** For  Avatar download
                                    //Start
                                    Log.d("profiledownload", "check ");
                                    Bitmap btmap = BitmapFactory.decodeStream(inputStream);
                                    if (connection.getContentLength() <= 0 && btmap == null) {
                                        Log.i("downavatar", "avatar download faild contact name-->");

                                    } else {
                                        Log.i("downavatar", "avatar download success contact name-->");
                                    }
                                    //End
//                                        Appreference.profile_image.put(profile, filePath);
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Appreference.printLog("MainActivity", "DownloadImage Exception: " + e.getMessage(), "WARN", null);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("MainActivity", "DownloadImage Exception: " + e.getMessage(), "WARN", null);
                            }
                        } else {
//                                Appreference.profile_image.put(profile, myFile.toString());
                            Log.i("profiledownload", "file exit--->" + profile);
                        }
                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "DownloadImage Exception: " + e.getMessage(), "WARN", null);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                Log.i("Template creation", "mm1 ** " + namevalue);
                Log.i("Template creation", "TaskDescription1 ** ");
                Log.i("Template creation", "TaskDescription1 ** " + Appreference.taskMultimediaDownload.size());

                if (Appreference.taskMultimediaDownload != null && Appreference.taskMultimediaDownload.size() > 0) {
                    Iterator iterator1 = Appreference.taskMultimediaDownload.entrySet()
                            .iterator();
                    Log.i("Template creation", "mm11 " + namevalue);
                    //                Log.i("Template creation", "TaskDescription1 " + detailsBean.getTaskDescription());
                    while (iterator1.hasNext()) {
                        Map.Entry mapEntry = (Map.Entry) iterator1.next();
                        if (namevalue.equalsIgnoreCase(((TaskDetailsBean) mapEntry.getValue()).getTaskDescription())) {
                            Log.i("Template creation", "mm " + namevalue);
                            //                        Log.i("Template creation", "TaskDescription " + detailsBean.getTaskDescription());
                            detailsBean = (TaskDetailsBean) mapEntry.getValue();
                            File imageFile = new File(Environment.getExternalStorageDirectory()
                                    + "/High Message/downloads/" + detailsBean.getTaskDescription());
                            if (imageFile.exists()) {
                                Log.i("Template creation", "TaskDescription11 " + detailsBean.getTaskDescription());
                                if (detailsBean.getSignalid() != null) {
                                    VideoCallDataBase.getDB(mainContext).updateTaskProgressStatus(detailsBean.getSignalid());
                                }
                            }
                        }
                    }
                }

                if (Appreference.context_table.containsKey("taskcoversation")) {

                    if (detailsBean != null) {
                        Log.i("Template creation", "TaskDescription111 ");
                        NewTaskConversation.getInstance().MMdownloadCompleted(detailsBean);
                    }
                    /*NewTaskConversation  newTaskConversation = new NewTaskConversation();
                    newTaskConversation.loadUI();*/
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity onPostExecute", "DownloadImage Exception: " + e.getMessage(), "WARN", null);
            }
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }
    }

    public class Profile extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                Log.i("personal", "profile download");
                if (isNetworkAvailable()) {
                    String url = getResources().getString(R.string.user_upload);
                    String profile = Appreference.loginuserdetails.getProfileImage();
//                    for (String profile : Appreference.profilePictures) {
                    if (profile != null) {
                        File extStore = Environment.getExternalStorageDirectory();
                        File myFile = new File(extStore.getAbsolutePath() + "/High Message/profilePic/" + profile);

                        Log.i("personal", "file " + myFile.toString());
                        if (!myFile.exists()) {
                            try {



                                  /*  HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                                        @Override
                                        public boolean verify(String hostname, SSLSession session) {
                                            HostnameVerifier hv =
                                                    HttpsURLConnection.getDefaultHostnameVerifier();
                                            return hv.verify("example.com", session);
                                        }
                                    };*/

// Tell the URLConnection to use our HostnameVerifier



                                   /* Log.i("profiledownload", "file not exit--->" + profile);
                                    URL bitmap = new URL(url + profile);
//                                    HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
//                                    HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
                                    HttpsURLConnection connection = (HttpsURLConnection) bitmap
                                            .openConnection();
//                                    connection.setDefaultHostnameVerifier(hostnameVerifier);
                                    connection.setDoInput(true);*/
                                URL bitmap = new URL(url + profile);

                                Log.i("personal", "profile bitmap" + bitmap);
                                HttpURLConnection connection =
                                        (HttpURLConnection) bitmap.openConnection();
//                                    connection.setHostnameVerifier(hostnameVerifier);
                                InputStream in = connection.getInputStream();
//                                    copyInputStreamToOutputStream(in, System.out);
                                connection.connect();
                                if (connection.getInputStream() != null) {
                                    InputStream inputStream = connection.getInputStream();
                                    String dir_path = Environment.getExternalStorageDirectory()
                                            + "/High Message/profilePic";
                                    Log.i("personal", "profile dir_path" + dir_path);
                                    File directory = new File(dir_path);
                                    if (!directory.exists()) {
                                        directory.mkdirs();
                                    }
                                    String filePath = Environment.getExternalStorageDirectory()
                                            .getAbsolutePath()
                                            + "/High Message/profilePic/"
                                            + profile;

                                    Log.d("personal", "my file path is---->" + filePath);

                                    File file_name = new File(filePath);
                                    BufferedOutputStream bos = new BufferedOutputStream(
                                            new FileOutputStream(file_name));
                                    int inByte;
                                    while ((inByte = inputStream.read()) != -1) {
                                        bos.write(inByte);
                                    }
                                    bos.close();

                                    //** For  Avatar download
                                    //Start
                                    Bitmap btmap = BitmapFactory.decodeStream(inputStream);
                                    if (connection.getContentLength() <= 0 && btmap == null) {
                                        Log.i("downavatar", "avatar download faild contact name-->");

                                    } else {
                                        Log.i("downavatar", "avatar download success contact name-->");
                                    }
                                    //End
                                    Appreference.loginuserdetails.setProfileImage(profile);
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                Appreference.printLog("MainActivity", "Profile Exception: " + e.getMessage(), "WARN", null);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("MainActivity", "Profile Exception: " + e.getMessage(), "WARN", null);
                            }
                        } else {
                            Appreference.loginuserdetails.setProfileImage(profile);
                            Log.i("personal", "file exit--->" + profile);
                        }
                    }

//                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "Profile Exception: " + e.getMessage(), "WARN", null);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                if (Appreference.context_table.containsKey("contacts_fragment_layout")) {

                    ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
                    //                ContactsFragment contactsFragment = new ContactsFragment();
                    // contactsFragment.getRetainInstance();
                    contactsFragment.callrefresh();
                    contactsFragment.referesh();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity onPostExecute", "Profile Exception: " + e.getMessage(), "WARN", null);
            }

        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }
    }

    class DownloadVideo extends AsyncTask<String, String, String> {

        String downloadVideoUrl;
        TaskDetailsBean detailsBean = null;

        public DownloadVideo(String url) {

            this.downloadVideoUrl = url;
            Log.d("task", "video uri is   " + downloadVideoUrl);
        }

        ProgressDialog PD;

        @Override
        protected void onPreExecute() {
//            PD= ProgressDialog.show(MainActivity.this,null, "Please Wait ...", true);
//            PD.setCancelable(true);
            try {
                File imageFileName = new File(Environment.getExternalStorageDirectory()
                        + "/High Message/downloads/");

                if (!imageFileName.exists()) {
                    imageFileName.mkdirs();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "DownloadVideo Exception: " + e.getMessage(), "WARN", null);
            }
        }

        @Override
        protected String doInBackground(String... arg0) {
            String response = null;
            try {
                response = "";
                Log.i("download", "uri");
                if (downloadVideoUrl.contains("chat")) {
                    profile = downloadVideoUrl.split("chat/")[1];
                    Log.i("download", "profile" + profile);

                } else {
                    profile = downloadVideoUrl.split("task/")[1];
                    Log.i("download", "profile" + profile);
                }
                DownloadVideoFile(downloadVideoUrl, profile);
                Log.i("download", "profile" + profile);
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "DownloadVideo Exception: " + e.getMessage(), "WARN", null);
            }

            return response;
        }

        protected void onPostExecute(String result) {
//            PD.dismiss();
            try {
                Log.i("downloadVideo", "OnPostExcute");
                if (Appreference.taskMultimediaDownload != null && Appreference.taskMultimediaDownload.size() > 0) {
                    Iterator iterator1 = Appreference.taskMultimediaDownload.entrySet()
                            .iterator();

                    while (iterator1.hasNext()) {

                        Map.Entry mapEntry = (Map.Entry) iterator1.next();

                        detailsBean = (TaskDetailsBean) mapEntry.getValue();
                        File imageFile = new File(Environment.getExternalStorageDirectory()
                                + "/High Message/downloads/" + detailsBean.getTaskDescription());
                        if (imageFile.exists()) {
                            if (detailsBean.getSignalid() != null) {
                                Log.i("downloadVideo", "OnPostExcute updatemethod");
                                VideoCallDataBase.getDB(mainContext).updateTaskProgressStatus(detailsBean.getSignalid());
                            }
                        }
                    }
                }

                if (Appreference.context_table.containsKey("taskcoversation")) {
                    NewTaskConversation.getInstance().MMdownloadCompleted(detailsBean);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity onPostExecute", "DownloadVideo Exception: " + e.getMessage(), "WARN", null);
            }
        }
    }


    public void DownloadVideoFile(String fileURL, String fileName) {
        try {
            String RootDir = Environment.getExternalStorageDirectory()
                    .getAbsolutePath()
                    + "/High Message/downloads/";
            Log.d("task", "video fileName" + RootDir);

            File RootFile = new File(RootDir);
            RootFile.mkdir();
            // File root = Environment.getExternalStorageDirectory();
            URL u = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(new File(RootFile,
                    fileName));
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;

            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();


        } catch (Exception e) {
            e.printStackTrace();
            Log.d("task....", e.toString());
            Appreference.printLog("MainActivity", "DownloadVideoFile Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public static void unRegister() {
        try {
            if (app.accList.size() != 0) {
                Log.i("unregister", "unRegister method");
                //            account = app.accList.get(0);
                //            accCfg = account.cfg;

                try {
                    account.setRegistration(false);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MainActivity", "unRegister Exception: " + e.getMessage(), "WARN", null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "unRegister Exception: " + e.getMessage(), "WARN", null);
        }

    }

    public View setTabView(int position) {
        View v = null;
        try {
            v = LayoutInflater.from(mainContext).inflate(R.layout.tab_custom_icon, null);
            int msgCount = VideoCallDataBase.getDB(getApplicationContext()).getAllContactsUnReadMsgCount();
            Log.d("task1", "overall Contact Msg count contactpage " + msgCount);
            int msgCount_1 = VideoCallDataBase.getDB(getApplicationContext()).getAllProjectsUnReadMsgCount();
            Log.d("task1", "overall project Msg count projectpage" + msgCount_1);
            int msgCount_2 = VideoCallDataBase.getDB(getApplicationContext()).getAllChatUnReadMsgCount();
            Log.d("task1", "overall project Msg count recent " + msgCount_2);
            tab_tv = (TextView) v.findViewById(R.id.tab_badge);
            tab_text = (TextView) v.findViewById(R.id.tab_text);
            tab_icon1 = (ImageView) v.findViewById(R.id.tab_icon);
            tab_icon1.setImageResource(tab_icon[position]);
//        tabLayout.getTabAt(0).setIcon(tab_icon[0]);
//        tabLayout.getTabAt(1).setIcon(tab_icon2[1]);
//        tabLayout.getTabAt(2).setIcon(tab_icon2[2]);
//        tabLayout.getTabAt(3).setIcon(tab_icon[3]);
        /*if (selected) {
            if (position == 0) {
                tab_icon1.setImageResource(R.drawable.ic_contacts_100);
            }
            if (position == 1) {
                tab_icon1.setImageResource(R.drawable.ic_checklist_100);
            }
            if (position == 2) {
                tab_icon1.setImageResource(R.drawable.ic_chat_4_128);
            }
            if (position == 3) {
                tabLayout.getTabAt(position).setIcon(R.drawable.ic_settings_2_128);
            }
        } else {
            tab_icon1.setImageResource(tab_icon2[position]);
        }*/
            tab_text.setText(tab_name[position]);
            if (position == 0 && msgCount != 0) {
                Log.d("task1", "overall Msg count if condition" + msgCount);
                tab_tv.setVisibility(View.VISIBLE);
                tab_tv.setText(String.valueOf(msgCount));
            } else if (position == 3 && msgCount_1 != 0) {
                tab_tv.setVisibility(View.VISIBLE);
                tab_tv.setText(String.valueOf(msgCount_1));
            } else if (position == 1 && msgCount_2 != 0) {
                tab_tv.setVisibility(View.VISIBLE);
                tab_tv.setText(String.valueOf(msgCount_2));
                Log.d("task1", "overall Msg count else part" + msgCount_2);
            } else {
                Log.d("task1", "overall Msg count else part" + msgCount);
                tab_tv.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "setTabView Exception: " + e.getMessage(), "WARN", null);
        }

//        if (position == 0 && selected == true) {
//            Integer[] tab_icon = {
//                    R.drawable.ic_contacts_100,
//                    R.drawable.ic_checklist_1001,
//                    R.drawable.ic_chat_4_1282,
////            R.drawable.tab_blog,
//                    R.drawable.ic_settings_2_1281
//            };
//            Log.d("task1", "overall pos 0" + position);
//            selected = false;
//            tab_icon1.setImageResource(tab_icon[position]);
//        } else if (position == 1 && selected == true) {
//            Integer[] tab_icon = {
//                    R.drawable.ic_contacts_1001,
//                    R.drawable.ic_checklist_100,
//                    R.drawable.ic_chat_4_1282,
////            R.drawable.tab_blog,
//                    R.drawable.ic_settings_2_1281
//            };
//            Log.d("task1", "overall pos 1" + position);
//            selected = false;
//            tab_icon1.setImageResource(tab_icon[position]);
//        } else if (position == 2 && selected == true) {
//            Integer[] tab_icon = {
//                    R.drawable.ic_contacts_1001,
//                    R.drawable.ic_checklist_1001,
//                    R.drawable.ic_chat_4_128,
////            R.drawable.tab_blog,
//                    R.drawable.ic_settings_2_1281
//            };
//            selected = false;
//            Log.d("task1", "overall pos 2" + position);
//            tab_icon1.setImageResource(tab_icon[position]);
//        } else if (position == 3) {
//            Integer[] tab_icon = {
//                    R.drawable.ic_contacts_1001,
//                    R.drawable.ic_checklist_1001,
//                    R.drawable.ic_chat_4_1282,
////            R.drawable.tab_blog,
//                    R.drawable.ic_settings_2_128
//            };
//            selected = false;
//            Log.d("task1", "overall pos 3" + position);
//            tab_icon1.setImageResource(tab_icon[position]);
//        }

        return v;
    }

    public void BadgeReferece() {
        try {
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.post(new Runnable() {
                @Override
                public void run() {
                    tabLayout.setupWithViewPager(mViewPager);

                    for (int i = 0; i < tabLayout.getTabCount(); i++) {
                        //           selected=true;
                        tabLayout.getTabAt(i).setCustomView(setTabView(i));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "BadgeReferece Exception: " + e.getMessage(), "WARN", null);
        }


    }

    public void showprogress(String name) {
        Log.i("updateContacts123", "showprogress==updating Contacts........");

        try {
            if (progress == null || !progress.isShowing()) {
                progress = new ProgressDialog(mainContext);
                progress.setCancelable(false);
                progress.setMessage(name);
                progress.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "showprogress Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void cancelDialog() {
        Log.i("updateContacts123", "==cancelDialog........");

        try {
            if (progress != null && progress.isShowing()) {
                Log.i("register", "--progress bar end-----");
                progress.dismiss();
                progress = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("MainActivity", "cancelDialog Exception: " + e.getMessage(), "WARN", null);
        }
    }


    public void ContactCall() {
        try {
            if (VideoCallDataBase.getDB(mainContext).getContact(appSharedpreferences.getString("loginUserName")) != null) {

                Log.i("Contact", "Database");
                ArrayList<ContactBean> buddyList;
                buddyList = VideoCallDataBase.getDB(mainContext).getContact(appSharedpreferences.getString("loginUserName"));
                Log.i("ContactValue", "Arraysize" + buddyList.size());
                Log.i("userState", "Response Method");
                if (MainActivity.account.buddyList != null && MainActivity.account.buddyList.size() > 0) {
                    Log.i("sipTest", "Buddy list size()--->" + MainActivity.account.buddyList.size());
                    Log.i("register", " MainActivity.account.buddyList.size()>0 && Buddy Add after register successfully");
                    HashMap<String, MyBuddy> tempvalue = new HashMap<String, MyBuddy>();
                    for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
                        String name = MainActivity.account.buddyList.get(i).cfg.getUri();
                        tempvalue.put(name, MainActivity.account.buddyList.get(i));
                        Log.i("sipTest", "tempvalue name and sipvalue " + tempvalue.get(name) + " " + name);
                    }

                    for (ContactBean contactBean : buddyList) {
                        if (tempvalue != null && contactBean.getUsername() != null) {
                            String name = "sip:" + contactBean.getUsername() + "@" + getResources().getString(R.string.server_ip);
                            if (!tempvalue.containsKey(name)) {
                                BuddyConfig cfg = new BuddyConfig();

                                String b_uri = "sip:" + contactBean.getUsername()
                                        + "@" + getResources().getString(R.string.server_ip);
                                Log.i("sipTest", "buddy name not in hashmap b_uri     " + b_uri);
                                cfg.setUri(b_uri);
                                cfg.setSubscribe(true);
                                account.addBuddy(cfg);
                                Appreference.printLog("sipregister", "buddy add in my accout-->" + b_uri, "DEBUG", null);
                            }
                            //                        else{
                            //                            if(tempvalue.containsKey(name)){
                            //                                MyBuddy myBuddy=tempvalue.get(name);
                            //                                account.delBuddy(myBuddy);
                            //                                BuddyConfig cfg = new BuddyConfig();
                            //
                            //                                String b_uri = "sip:" + contactBean.getUsername()
                            //                                        + "@" + getResources().getString(R.string.server_ip);
                            //                                Log.i("sipTest", "buddy name  in hashmap b_uri     " + b_uri);
                            //                                cfg.setUri(b_uri);
                            //                                cfg.setSubscribe(true);
                            //                                account.addBuddy(cfg);
                            //                                Appreference.printLog("sipregister", "buddy add in my accout-->"+b_uri, "DEBUG", null);
                            //                            }
                            //                        }
                        }
                        Appreference.buddy_details.put(contactBean.getUsername(), contactBean);
                    }

                } else {
                    Log.i("sipTest", "Buddy list size()--->" + 0);
                    Log.i("register", "Buddy Add after register successfully");
                    //            MainActivity.account.buddyList.clear();

                    Log.i("register", "Buddy Add after register successfully MainActivity.account.buddyList size" + MainActivity.account.buddyList.size());

                    for (ContactBean contactBean : buddyList) {

                        BuddyConfig cfg = new BuddyConfig();

                        String b_uri = "sip:" + contactBean.getUsername()
                                + "@" + getResources().getString(R.string.server_ip);
                        Log.i("sipTest", "b_uri     " + b_uri);
                        cfg.setUri(b_uri);
                        cfg.setSubscribe(true);
                        account.addBuddy(cfg);
                        Appreference.printLog("sipregister", "buddy add in my accout-->" + b_uri, "DEBUG", null);
                        Appreference.buddy_details.put(contactBean.getUsername(), contactBean);
                    }
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "ContactCall Exception: " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "ContactCall Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void NetworkState(boolean state) {
        try {
            if (state) {
                Appreference.printLog("network", "Internet Connected", "DEBUG", null);
                Appreference.networkState = true;
                Appreference.sipRegistrationState = false;
                ActiveScreens();
                nwstate = true;
                if (app != null && account != null && accCfg != null) {//
                    Appreference.printLog("network", "Sip Re-Registration send After NetWork Connected", "DEBUG", null);
                    Log.i("network", "netwokr state Re_Register");
                    reRegister();
                }
            } else {
                Appreference.printLog("network", "No Internet Connection", "DEBUG", null);
                Appreference.networkState = false;
                Appreference.sipRegistrationState = false;
                nwstate = false;
                ActiveScreens();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "NetworkState Exception: " + e.getMessage(), "WARN", null);
        }

    }

    public boolean isApplicationBroughtToBackground() {
        boolean openPinActivity = false;
        try {
            openPinActivity = false;
            ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
            ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
            activityOnTop = ar.topActivity.toString();
            Log.i("pin", "activityOnTop " + activityOnTop);
            Log.i("pin", "activityOnTop " + context.getPackageName());
            if (activityOnTop.contains("com.android.documentsui")) {
                Log.i("pin", "com.android.documentsui pakage");
                openPinActivity = false;
            } else if (!activityOnTop.contains(context.getPackageName())) {
                Log.i("pin", "MainActivity - COntains package Name");
                openPinActivity = true;
            } else {
                Log.i("pin", "MainActivity - dont have package Name");
                openPinActivity = false;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "isApplicationBroughtToBackground Exception: " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "isApplicationBroughtToBackground Exception: " + e.getMessage(), "WARN", null);
        }
        return openPinActivity;
    }

    public static void reRegister_onAppResume() {
        try {
            Appreference.sipRegistrationState = false;
            ActiveScreens();
            nwstate = true;
            if (Appreference.networkState) {
                //            if (Appreference.sipRegistrationState) {
                if (app != null && account != null && accCfg != null) {
                    Appreference.printLog("network", "Sip Re-Registration send on App Resume", "DEBUG", null);
                    Log.i("network", "netwokr state Re_Register");
                    reRegister();
                }
                //            } else if (!Appreference.sipRegistrationState) {
                //
                //            }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "reRegister_onAppResume Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public static void ActiveScreens() {
        try {
            if (Appreference.context_table != null) {
                if (Appreference.context_table.containsKey("contactsfragment")) {
                    ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
                    contactsFragment.showNetworkStateUI();
                    //                contactsFragment.showBusyState();
                }
                if (Appreference.context_table.containsKey("taskfragment")) {
                    TasksFragment tasksFragment = (TasksFragment) Appreference.context_table.get("taskfragment");
                    tasksFragment.showNetworkStateUI();
                }
                if (Appreference.context_table.containsKey("chatfragment")) {
                    ChatFragment chatFragment = (ChatFragment) Appreference.context_table.get("chatfragment");
                    chatFragment.showNetworkStateUI();
                }
                if (Appreference.context_table.containsKey("blogsfragment")) {
                    BlogsFragment blogsFragment = (BlogsFragment) Appreference.context_table.get("blogsfragment");
                    blogsFragment.showNetworkStateUI();
                }
                if (Appreference.context_table.containsKey("taskcoversation")) {
                    NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                    newTaskConversation.showNetworkStateUI();
                }
                if (Appreference.context_table.containsKey("traveljobdetails")) {
                    TravelJobDetails travelJobDetails = (TravelJobDetails) Appreference.context_table.get("traveljobdetails");
                    travelJobDetails.showNetworkStateUI();
                }
                if (Appreference.context_table.containsKey("chatactivity")) {
                    ChatActivity chatActivity = (ChatActivity) Appreference.context_table.get("chatactivity");
                    chatActivity.showNetworkStateUI();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "ActiveScreens Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void ShowNetworkConnectedstate() {
        try {
            if (Appreference.context_table != null) {
                if (Appreference.context_table.containsKey("contactsfragment")) {
                    ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
                    contactsFragment.showNetWorkConnectedState();
                    contactsFragment.showOnlineState();
                }
                if (Appreference.context_table.containsKey("taskfragment")) {
                    TasksFragment tasksFragment = (TasksFragment) Appreference.context_table.get("taskfragment");
                    tasksFragment.showNetWorkConnectedState();
                }
                if (Appreference.context_table.containsKey("chatfragment")) {
                    ChatFragment chatFragment = (ChatFragment) Appreference.context_table.get("chatfragment");
                    chatFragment.showNetWorkConnectedState();
                }
                if (Appreference.context_table.containsKey("blogsfragment")) {
                    BlogsFragment blogsFragment = (BlogsFragment) Appreference.context_table.get("blogsfragment");
                    blogsFragment.showNetWorkConnectedState();
                }
                if (Appreference.context_table.containsKey("taskcoversation")) {
                    NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                    newTaskConversation.showNetWorkConnectedState();
                }
                if (Appreference.context_table.containsKey("traveljobdetails")) {
                    TravelJobDetails travelJobDetails = (TravelJobDetails) Appreference.context_table.get("traveljobdetails");
                    travelJobDetails.showNetWorkConnectedState();
                }
                if (Appreference.context_table.containsKey("chatactivity")) {
                    ChatActivity chatActivity = (ChatActivity) Appreference.context_table.get("chatactivity");
                    chatActivity.showNetWorkConnectedState();
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "ShowNetworkConnectedstate Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void callDownloadgroupProfile(String profile) {
        new DownloadGroupProfile().execute();
    }

    public void callDownloadProfile(String profile) {
        new DownloadProfile().execute();
    }

    public void callprofile(String profile) {
        new Profile().execute();
    }

    public static void ReleaseWakeClock() {
        try {
            if (mWakeLock != null)
                if (mWakeLock.isHeld())
                    mWakeLock.release();

//        if (lock != null) {
//            Log.d("gsm in swn", "re enable keyguard");
//            lock.reenableKeyguard();
//        }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "ReleaseWakeClock Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public static void hideCallUI() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("CallUI", "MainActivity call ");
                    if (Appreference.context_table.containsKey("contactsfragment")) {
                        ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
                        Log.d("CallUI", "MainActivity Fragment ");
                        contactsFragment.hideCallIcon();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MainActivity", "hideCallUI Exception: " + e.getMessage(), "WARN", null);
                }
            }
        });

    }

    public static void callBusyAlertUI() {
        try {
            final AlertDialog alertDialog = new AlertDialog.Builder(
                    mainContext).create();

            // Setting Dialog Title
            alertDialog.setTitle("Call Alert");

            // Setting Dialog Message
            alertDialog.setMessage("User is busy");

            // Setting Icon to Dialog
//        alertDialog.setIcon(R.drawable.tick);

            // Setting OK Button
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Write your code here to execute after dialog closed
                    alertDialog.dismiss();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "callBusyAlertUI Exception: " + e.getMessage(), "WARN", null);
        }

    }

    public static String getFileName() {
        String strFilename = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
            Date date = new Date();
            strFilename = dateFormat.format(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("MainActivity", "getFileName Exception: " + e.getMessage(), "WARN", null);
        }
        return strFilename;
    }

    private void showUIProgress(final Context context) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    ui_progress = new ProgressDialog(context);
                    ui_progress.setCancelable(false);
                    ui_progress.setMessage("Loading Details");
                    ui_progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    ui_progress.setProgress(0);
                    ui_progress.setMax(100);
                    ui_progress.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MainActivity", "showUIProgress Exception: " + e.getMessage(), "WARN", null);
                }
            }
        });
    }

    private void cancelUIDialog() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (ui_progress != null && ui_progress.isShowing()) {
                        ui_progress.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MainActivity", "cancelUIDialog Exception: " + e.getMessage(), "WARN", null);
                }
            }
        });
    }


    public void datefileWebService(TaskDetailsBean taskBean) {
//        Log.i("task", taskBean.getTaskDescription());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String dateforrow = dateFormat.format(new Date());
        String rem_tone, rem_freq_min, rem_frq, remainder_Frequency, task_startdate = null, task_enddate = null, task_remindate = null;
        long total_mins;
        String reminderfreq = null;
        Log.i("task", "date datefilewebservice");

        {
            Log.i("Request", "timeFrequency " + taskBean.getTimeFrequency());  // 1 Minutes
            rem_freq_min = taskBean.getTimeFrequency().split(" ")[0];   // 1
            rem_frq = taskBean.getTimeFrequency().split(" ")[1];   // Minutes

            Log.i("task", "Reminder Freq Minutes " + rem_freq_min);
            switch (rem_frq.toLowerCase()) {
                case "minutes":
                    total_mins = Integer.parseInt(rem_freq_min) * 60000;
                    remainder_Frequency = String.valueOf(total_mins);
                    break;
                case "hours":
                    total_mins = Integer.parseInt(rem_freq_min) * 60 * 60000;
                    remainder_Frequency = String.valueOf(total_mins);
                    break;
                case "days":
                    total_mins = Integer.parseInt(rem_freq_min) * 1440 * 60000;
                    remainder_Frequency = String.valueOf(total_mins);
                    break;
                default:
                    remainder_Frequency = "60000";
                    break;
            }
        }
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("id", Integer.parseInt(taskBean.getTaskId()));
            jsonObject.put("task", jsonObject1);
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("id", Appreference.loginuserdetails.getId());
            jsonObject.put("from", jsonObject2);
            JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("id", taskBean.getToUserId());
            if (!taskBean.getTaskType().equalsIgnoreCase("Group")) {
                Log.i("task", "tasktype project " + taskBean.getTaskType());
                jsonObject.put("to", jsonObject3);
            }/* else if (!project) {
                if (taskType != null && taskType.equalsIgnoreCase("Group")) {
                    Log.i("task", "tasktype groupTask " + taskType);
                    jsonObject.put("group", jsonObject3);
                } else {
                    Log.i("task", "tasktype IndividualTask " + taskType);
                    jsonObject.put("to", jsonObject3);
                }
            }*/
            jsonObject.put("signalId", taskBean.getSignalid());
            jsonObject.put("parentId", getFileName());
            jsonObject.put("createdDate", taskBean.getTaskUTCTime());
            if (taskBean.getRequestStatus().equalsIgnoreCase("assigned")) {
                jsonObject.put("requestType", "taskDateChangedApproval");
                jsonObject.put("requestStatus", "approved");
            }
           /* if (taskBean.getRequestStatus().equalsIgnoreCase("requested")) {
                jsonObject.put("requestType", "taskDateChangedRequest");
                jsonObject.put("requestStatus", date_header);
            }
            if (taskBean.getRequestStatus().equalsIgnoreCase("rejected") || taskBean.getRequestStatus().equalsIgnoreCase("approved")) {
                jsonObject.put("requestType", "taskDateChangedApproval");
                jsonObject.put("requestStatus", date_header);
            }*/

          /*  if (isResend) {

                task_startdate = Appreference.customLocalDateToUTC(chatBean.getPlannedStartDateTime());
                task_enddate = Appreference.customLocalDateToUTC(chatBean.getPlannedEndDateTime());
                task_remindate = Appreference.customLocalDateToUTC(chatBean.getRemainderFrequency());
                chatBean.setUtcPlannedStartDateTime(task_startdate);
                chatBean.setUtcplannedEndDateTime(task_enddate);
                chatBean.setUtcPemainderFrequency(task_remindate);
                Log.i("task", "date chatBean local getPlannedStartDateTime() " + chatBean.getPlannedStartDateTime());
                Log.i("task", "date chatBean utc getPlannedStartDateTime() " + task_startdate);
                jsonObject.put("taskStartDateTime", task_startdate);
                jsonObject.put("taskEndDateTime", task_enddate);
                jsonObject.put("remainderDateTime", task_remindate);
            } else*/
            jsonObject.put("taskStartDateTime", taskBean.getUtcPlannedStartDateTime());
            jsonObject.put("taskEndDateTime", taskBean.getUtcplannedEndDateTime());
            jsonObject.put("remainderDateTime", taskBean.getUtcPemainderFrequency());


            jsonObject.put("taskNo", taskBean.getTaskNo());
            jsonObject.put("dateFrequency", "");
            jsonObject.put("isRemainderRequired", "Y");
            jsonObject.put("timeFrequency", remainder_Frequency);
            if (taskBean.getReminderQuote() != null && !taskBean.getReminderQuote().equalsIgnoreCase("") && !taskBean.getReminderQuote().equalsIgnoreCase(null)) {
//                String defaultquotes = "Update Status Reminder Cancelled";
                jsonObject.put("remainderQuotes", taskBean.getReminderQuote());
            } else {
                jsonObject.put("remainderQuotes", "Task Reminder");
            }

            jsonObject.put("remark", "");
            JSONObject jsonObject5 = new JSONObject();
            jsonObject5.put("id", Appreference.loginuserdetails.getId());
           /* JSONObject jsonObject4 = new JSONObject();
            jsonObject4.put("user", jsonObject5);

            jsonObject4.put("fileType", "audio");
            jsonObject4.put("taskFileExt", "mp3");
            Log.i("Request", "remainder" + remindertone);

            if (remindertone != null && !remindertone.equalsIgnoreCase(null) && !remindertone.equalsIgnoreCase("") && (remindertone.contains(".mp3") || remindertone.contains(".wav"))) {
                rem_tone = encodeAudioVideoToBase64(remindertone);
                jsonObject4.put("fileContent", rem_tone);
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(0, jsonObject4);
                jsonObject.put("listTaskConversationFiles", jsonArray);
            }*/

            Log.i("Request", "Task date update for giver is " + jsonObject);
            Log.i("task", "Desc2 " + taskBean.getTaskDescription());
            Appreference.jsonRequestSender.taskConversationEntry(EnumJsonWebservicename.taskConversationEntry, jsonObject, MainActivity.this, null, taskBean);
            Appreference.isRequested_date = true;
//            showprogress();

        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MainActivity", "datefileWebService Exception: " + e.getMessage(), "WARN", null);
        }
    }

    @Override
    public void ShowProgress(Context context) {
        showUIProgress(context);
    }

    @Override
    public void CancellProgress() {
        cancelUIDialog();
    }

    @Override
    public void StartAlarmManager(TaskDetailsBean customTagBean) {
        startScheduleManager(customTagBean);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }
}
