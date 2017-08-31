package com.ase;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.Bean.ProjectDetailsBean;
import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;
import com.ase.RandomNumber.Utility;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pjsip.pjsua2.PresenceStatus;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.app.MyBuddy;
import org.pjsip.pjsua2.app.SipRegisterBroadastReceiver;
import org.pjsip.pjsua2.pjsua_buddy_status;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.JsonOfflineRequestSender;
import json.JsonRequestSender;
import json.WebServiceInterface;

//import com.myapplication3.RandomNumber.UpdateProfile;

/**
 * Created by Amuthan on 22/03/2016.
 */
public class SettingsFragment extends Fragment implements WebServiceInterface {
    public View rootView;
    static SettingsFragment fragment;
    TextView status;
    Handler handler = new Handler();
    String gender = null;
    Button version_no, show_log, sync_btn,release_version;
    TextView username, em, edit, signout, changepass, exclation_counter;
    ImageView imageview;
    LinearLayout files;
    public static Context classContext;
    ImageLoader imageLoader;
    private ProgressDialog progress;
    static SettingsFragment settingsFragment;
    Switch OnOff, conflicttask;
    String imagename1;
    AppSharedpreferences appSharedpreferences;
    Handler timerHandler = new Handler();
    String need_to_call_listallmytask;
    boolean con;
    ArrayList<TaskDetailsBean> travel_date_details;
    public static ArrayList<String> listOfObservers;
    private String quotes = "\"";

    public static SettingsFragment getInstance() {
        return settingsFragment;
    }

    public static SettingsFragment newInstance(int sectionNumber, Context context) {
        if (fragment == null) {
            fragment = new SettingsFragment();
            classContext = context;
            Bundle args = new Bundle();
            fragment.setArguments(args);
            Log.i("task", "checked int->" + sectionNumber);
        }
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("LifeCycle", " projectFragment isVisibleToUser : " + isVisibleToUser);
        try {
            String s = "select * from taskDetailsInfo where msgstatus='12' and loginuser='" + Appreference.loginuserdetails.getEmail() + "'";
            ArrayList<ProjectDetailsBean> projectDetailsBeen = VideoCallDataBase.getDB(getContext()).getExclationdetails(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (getView() != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        rootView = inflater.inflate(R.layout.settings_fragment_layout, container, false);
        settingsFragment = this;


        appSharedpreferences = AppSharedpreferences.getInstance(classContext);
        show_log = (Button) rootView.findViewById(R.id.show_log);
        OnOff = (Switch) rootView.findViewById(R.id.switch1);
        conflicttask = (Switch) rootView.findViewById(R.id.conflictswitch);
        exclation_counter = (TextView) rootView.findViewById(R.id.exclation_counter);
        listOfObservers = new ArrayList<>();
        Appreference.context_table.put("settingsfragment", this);

        try {
            Log.i("SettingsFragment", "Result 0 " + appSharedpreferences.getBoolean("conflictTask"));
            con = appSharedpreferences.getBoolean("conflictTask");
            Appreference.conflicttask = con;
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("SettingsFragment", "Result " + con);
        if (con) {
            conflicttask.setChecked(true);
        } else {
            conflicttask.setChecked(false);
        }
        files = (LinearLayout) rootView.findViewById(R.id.ll_file);
        files.setVisibility(View.VISIBLE);
        files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ListOfFiles.class);
                startActivity(intent);
            }
        });


        final Runnable timerRunnable = new Runnable() {

            @Override
            public void run() {
                show_log.setVisibility(View.GONE);
            }
        };
        username = (TextView) rootView.findViewById(R.id.username);
        em = (TextView) rootView.findViewById(R.id.em);
        edit = (TextView) rootView.findViewById(R.id.edit_profile);
        changepass = (TextView) rootView.findViewById(R.id.change_password);
        version_no = (Button) rootView.findViewById(R.id.version);
        release_version = (Button) rootView.findViewById(R.id.release_version);
        sync_btn = (Button) rootView.findViewById(R.id.sync_btn);

        signout = (TextView) rootView.findViewById(R.id.signout);

        imageview = (ImageView) rootView.findViewById(R.id.imageView_round);

        String frt_lst = Appreference.loginuserdetails.getFirstName().trim() + " " + Appreference.loginuserdetails.getLastName().trim();
        final String ed1 = frt_lst;
        final String ed2 = Appreference.loginuserdetails.getEmail();
        final String ed3 = Appreference.loginuserdetails.getTitle();
        final String gdr = Appreference.loginuserdetails.getGender();
        String no = " ";
        String Appversion = "";
        Appversion = getResources().getString(R.string.app_version);
        Calendar calender = Calendar.getInstance();
        SimpleDateFormat simple = new SimpleDateFormat("ddMMM");
        no = simple.format(calender.getTime());
        Log.i("settingsfragment", "no value is" + no);
        version_no.setText(getResources().getString(R.string.app_versionDate));
        release_version.setText(getResources().getString(R.string.app_releaseDate));
        Log.i("settingsfragment", version_no.getText().toString());

        try {
            String s = "select * from taskDetailsInfo where msgstatus='12' and loginuser='" + Appreference.loginuserdetails.getEmail() + "'";
            ArrayList<ProjectDetailsBean> projectDetailsBeen = VideoCallDataBase.getDB(getContext()).getExclationdetails(s);
            if (projectDetailsBeen.size() > 0)
                exclation_counter.setVisibility(View.VISIBLE);
            else
                exclation_counter.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        imageview = (ImageView) rootView.findViewById(R.id.imageView_round);

        File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + Appreference.loginuserdetails.getProfileImage());
        if (Appreference.loginuserdetails.getProfileImage() != null) {
            if (myFile.exists()) {
                imageLoader = new ImageLoader(classContext);
                Log.i("Appreference", "getprofileimage-------->" + Appreference.loginuserdetails.getProfileImage());
                imageLoader.DisplayImage(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + Appreference.loginuserdetails.getProfileImage(), imageview, R.drawable.default_person_circle);
            } else {
                Picasso.with(getContext()).load(getResources().getString(R.string.user_upload) + Appreference.loginuserdetails.getProfileImage()).into(imageview);
                MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
                mainActivity.callprofile(getResources().getString(R.string.user_upload) + Appreference.loginuserdetails.getProfileImage());
            }
        }
        Log.i("settings", "first_last name " + ed1);
        username.setText(Appreference.loginuserdetails.getFirstName().trim() + " " + Appreference.loginuserdetails.getLastName().trim());
        em.setText(ed2);


        conflicttask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SettingsFragment", " state ONOFF " + appSharedpreferences.getBoolean("conflictTask"));
                if (appSharedpreferences.getBoolean("conflictTask") == true) {
                    Appreference.conflicttask = false;
                    con = false;
                    appSharedpreferences.saveBoolean("conflictTask", false);
                    Log.i("SettingsFragment", " state ONOFF if " + appSharedpreferences.getBoolean("conflictTask"));
                } else {
                    con = true;
                    Appreference.conflicttask = true;
                    appSharedpreferences.saveBoolean("conflictTask", true);
                    Log.i("SettingsFragment", " state ONOFF else " + appSharedpreferences.getBoolean("conflictTask"));
                }

            }
        });
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePassword.class);
                startActivity(intent);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
                String ed1 = Appreference.loginuserdetails.getFirstName();
                String ed2 = Appreference.loginuserdetails.getLastName();
                String ed3 = Appreference.loginuserdetails.getTitle();
                String gdr = Appreference.loginuserdetails.getGender();
                imagename1 = Appreference.loginuserdetails.getProfileImage();

                intent.putExtra("firstname", ed1);
                intent.putExtra("lastname", ed2);
                intent.putExtra("title", ed3);
                intent.putExtra("gender", gdr);
                intent.putExtra("profileimage", imagename1);
                startActivity(intent);
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isNetworkAvailable()) {
                        if (Appreference.loginuserdetails.getId() != 0) {
                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

                            nameValuePairs.add(new BasicNameValuePair("userId", String.valueOf(Appreference.loginuserdetails.getId())));
                            nameValuePairs.add(new BasicNameValuePair("deviceId", AppSharedpreferences.getInstance(getActivity()).getString("fcmTokenId")));
                            showprogress("Loading");
                            Appreference.jsonRequestSender.logoutMobile(EnumJsonWebservicename.logoutMobile, nameValuePairs, SettingsFragment.this);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Check Your Internet", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("login123", "Exception..logoutMobile ....." + e.getMessage());
                }
            }
        });

        version_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_log.setVisibility(View.VISIBLE);
                timerHandler.postDelayed(timerRunnable, 5000);
            }
        });
        Log.i("Appreference", "OncreateView check sync size --=====>-------->");

        String Query = "select * from projectStatus where wssendstatus= '000' order by datenow";
        ArrayList<TaskDetailsBean> AlltaskBean = VideoCallDataBase.getDB(classContext).getofflinesendlist(Query);

        String Query_media = "select * from taskDetailsInfo where wssendstatus= '000'";
        ArrayList<TaskDetailsBean> AlltaskMediaBean = VideoCallDataBase.getDB(classContext).getTaskDetailsInfo(Query_media);
        if (AlltaskBean != null && AlltaskBean.size() > 0 || AlltaskMediaBean != null && AlltaskMediaBean.size() > 0) {
            sync_btn.setBackgroundResource(R.color.rednew);
        } else {
            sync_btn.setBackgroundResource(R.color.gray);

        }

        sync_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    try {
                        Log.i("offline123", "sync_btn clicklistener=====>");
                        sync_btn.setBackgroundResource(R.color.gray);

                        String Query_media = "select * from taskDetailsInfo where wssendstatus= '000'";
                        ArrayList<TaskDetailsBean> AlltaskMediaBean = VideoCallDataBase.getDB(MainActivity.mainContext).getTaskDetailsInfo(Query_media);

                        String Query = "select * from projectStatus where wssendstatus= '000' order by datenow";
                        ArrayList<TaskDetailsBean> AlltaskBean = VideoCallDataBase.getDB(MainActivity.mainContext).getofflinesendlist(Query);

                        if (AlltaskBean != null && AlltaskBean.size() > 0 || AlltaskMediaBean != null && AlltaskMediaBean.size() > 0) {
                            final ProgressDialog dialog1 = ProgressDialog.show(getActivity(), "", "Loading...",
                                    true);
                            dialog1.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    dialog1.dismiss();
                                }
                            }, 1000);
                            OfflineSendMessage offlineSendMessage = new OfflineSendMessage();
                            offlineSendMessage.sendOfflineMessages();
                        } else {
                            showToast("No task to sync....");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    showToast("No Internet,Try again Later...");
                }
            }
        });
        show_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLog();

            }
        });
        OnOff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
        Log.i("task", "checkedSetting" + Appreference.isProject);
        return rootView;

    }

    public void notifystatus(String state) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (status != null) {
                    status.setText(MainActivity.lastRegStatus);
                }
            }
        });
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) classContext.getSystemService(classContext.getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void cancelDialog() {
        try {
            if (progress != null && progress.isShowing()) {
                Log.i("register", "--progress bar end-----");
                progress.dismiss();
                progress = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void showprogress(final String value) {
        if (progress == null) {
            progress = new ProgressDialog(getContext());
            progress.setCancelable(false);
            progress.setMessage(value);
            progress.show();
        }
    }


    public void showToast(final String result) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void sendLog()

    {
        Log.d("LogSend", "sendlog inside");
        boolean withLogs = true;

        Intent intent = new Intent(withLogs ? Intent.ACTION_SEND_MULTIPLE
                : Intent.ACTION_SEND);

        intent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[]{"hm@devteamsupport.com"});// StringUtilsImpl.getString(STRING_FEEDBACK_ADDRESS)});

        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject: HM Logfile");
        intent.putExtra(Intent.EXTRA_TEXT, "Thanks again for your message.The ideas and suggestions of innovative,thinking people are always welcome.");
        intent.setType("vnd.android.cursor.dir/email");

        if (withLogs) {
            ArrayList<Uri> uris = new ArrayList<Uri>();
            uris.add(Uri.fromFile(new File(Environment.getExternalStorageDirectory()
                    + "/High Message/HM_log.txt")));
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        }
        startActivityForResult(Intent.createChooser(intent, "Send mail..."), 1);


    }

    public void updateresponse(final Object obj) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                CommunicationBean communicationBean = Appreference.communicationBean;
                Log.i("Update_method", "called--->" + communicationBean.getFirstname());
                String frt_lstName = communicationBean.getFirstname().trim() + " " + communicationBean.getLastname().trim();
                Log.i("settings", "first_last name1 " + frt_lstName);
                username.setText(frt_lstName);
                em.setText(Appreference.loginuserdetails.getEmail());
                String oldImageName = communicationBean.getOldprofileimage();
                Log.i("old", "UserName---->" + oldImageName);
                String newImageName = communicationBean.getProfileimage();
                Log.i("new", "ResName---->" + newImageName);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("Appreference", "onResume inside--=====>-------->");

        File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + Appreference.loginuserdetails.getProfileImage());
        if (Appreference.loginuserdetails.getProfileImage() != null) {
            if (myFile.exists()) {

                imageLoader = new ImageLoader(classContext);
                Log.i("Appreference", "onResume-------->" + Appreference.loginuserdetails.getProfileImage());
                imageLoader.DisplayImage(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + Appreference.loginuserdetails.getProfileImage(), imageview, R.drawable.default_person_circle);
            } else {
                Picasso.with(getContext()).load(getResources().getString(R.string.user_upload) + Appreference.loginuserdetails.getProfileImage()).into(imageview);
                MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
                mainActivity.callprofile(getResources().getString(R.string.user_upload) + Appreference.loginuserdetails.getProfileImage());
            }
        }
        String Query = "select * from projectStatus where wssendstatus= '000' order by datenow";
        ArrayList<TaskDetailsBean> AlltaskBean = VideoCallDataBase.getDB(classContext).getofflinesendlist(Query);

        String Query_media = "select * from taskDetailsInfo where wssendstatus= '000'";
        ArrayList<TaskDetailsBean> AlltaskMediaBean = VideoCallDataBase.getDB(classContext).getTaskDetailsInfo(Query_media);
        if (AlltaskBean != null && AlltaskBean.size() > 0 || AlltaskMediaBean != null && AlltaskMediaBean.size() > 0) {
            sync_btn.setBackgroundResource(R.color.rednew);
        } else {
            sync_btn.setBackgroundResource(R.color.gray);

        }
    }

    @Override
    public void ResponceMethod(final Object object) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    CommunicationBean bean1 = (CommunicationBean) object;
                    Log.i("offline123", " settingsFrag ResponceMethod123******");

                    String s1 = ((CommunicationBean) object).getEmail();
                    String WebServiceEnum_Response = ((CommunicationBean) object).getFirstname();
                    Log.d("Task2", "name   == " + WebServiceEnum_Response);
                    JSONObject jsonObject = new JSONObject(s1);
                    if ((jsonObject.has("result_code") && jsonObject.getInt("result_code") == 0)) {
                        progress.dismiss();
                        Appreference.sipRegistrationState = false;
                        MainActivity.isPresenceReregister = false;
//                        Appreference.currentPresenceStateIs = "Online";
                        PresenceStatus presenceStatus = new PresenceStatus();
                        presenceStatus.setStatus(pjsua_buddy_status.PJSUA_BUDDY_STATUS_OFFLINE);
                        presenceStatus.setNote("Offline");
                        MainActivity.account.setOnlineStatus(presenceStatus);

                        AppSharedpreferences.getInstance(getContext()).saveBoolean("login", false);
                        MainActivity.unRegister();
                        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(getContext(), SipRegisterBroadastReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 2145,
                                intent, 0);
                        alarmManager.cancel(pendingIntent);
                        Appreference.isFCMRegister = true;
                        Appreference.networkState = false;
                        Appreference.signout_pressed = true;
                        Toast.makeText(getActivity(), "Logged out Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Log out failure", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("offline123", "ResponseMethod Exception=====> " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("SipVideo", "SettingsFragment onDestroy");
    }

    @Override
    public void ErrorMethod(Object object) {

    }
}
