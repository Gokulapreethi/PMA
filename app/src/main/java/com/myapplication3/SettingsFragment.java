package com.myapplication3;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.myapplication3.Bean.ProjectDetailsBean;
import com.myapplication3.DB.VideoCallDataBase;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.pjsip.pjsua2.PresenceStatus;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.app.SipRegisterBroadastReceiver;
import org.pjsip.pjsua2.pjsua_buddy_status;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.JsonRequestSender;
import json.WebServiceInterface;

//import com.myapplication3.RandomNumber.UpdateProfile;

/**
 * Created by Amuthan on 22/03/2016.
 */
public class SettingsFragment extends Fragment implements WebServiceInterface {
    static SettingsFragment fragment;
    TextView status;
    Handler handler = new Handler();
    String gender = null;
    Button version_no, show_log;
    TextView username, em, edit, signout, changepass, exclation_counter;
    ImageView imageview;
    LinearLayout files;
    static Context classContext;
    ImageLoader imageLoader;
    private ProgressDialog progress;
    static SettingsFragment settingsFragment;
    Switch OnOff, conflicttask;
    String imagename1;
    AppSharedpreferences appSharedpreferences;
    Handler timerHandler = new Handler();
    String need_to_call_listallmytask;
    boolean con;

    public static SettingsFragment getInstance() {
        return settingsFragment;
    }

    public static SettingsFragment newInstance(int sectionNumber, Context context) {
//        SettingsFragment fragment = new SettingsFragment();
//        Bundle args = new Bundle();
////        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
//        return fragment;
        if (fragment == null) {
            fragment = new SettingsFragment();
            classContext = context;
            Bundle args = new Bundle();
            fragment.setArguments(args);
            Log.i("task", "checked int->" + sectionNumber);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.settings_fragment_layout, container, false);
        //TextView name = (TextView) rootView.findViewById(R.id.user_name);
        //name.setText(MainActivity.username);
        settingsFragment = this;


        appSharedpreferences = AppSharedpreferences.getInstance(classContext);
        show_log = (Button) rootView.findViewById(R.id.show_log);
        OnOff = (Switch) rootView.findViewById(R.id.switch1);
        conflicttask = (Switch) rootView.findViewById(R.id.conflictswitch);
        exclation_counter = (TextView) rootView.findViewById(R.id.exclation_counter);
        try {
            Log.i("SettingsFragment", "Result 0 " + appSharedpreferences.getBoolean("conflictTask"));
            con = appSharedpreferences.getBoolean("conflictTask");
            Appreference.conflicttask = con;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("SettingsFragment", "Result " + con);
//        con = true;
        if (con) {
            conflicttask.setChecked(true);
        }else {
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

        //status = (TextView) rootView.findViewById(R.id.reg_status);
        //status.setText(MainActivity.lastRegStatus);
//        ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView_round);
        username = (TextView) rootView.findViewById(R.id.username);
        em = (TextView) rootView.findViewById(R.id.em);
        edit = (TextView) rootView.findViewById(R.id.edit_profile);
        changepass = (TextView) rootView.findViewById(R.id.change_password);
        version_no = (Button) rootView.findViewById(R.id.version);
        signout = (TextView) rootView.findViewById(R.id.signout);
//        sync_btn = (TextView) rootView.findViewById(R.id.s_bt_sync);
        //textview3 = (TextView) rootView.findViewById(R.id.textView7);
        //textview4 = (TextView) rootView.findViewById(R.id.textView8);

        imageview = (ImageView) rootView.findViewById(R.id.imageView_round);

        String frt_lst = Appreference.loginuserdetails.getFirstName().trim() + " " + Appreference.loginuserdetails.getLastName().trim();
        final String ed1 = frt_lst;
        final String ed2 = Appreference.loginuserdetails.getEmail();
        final String ed3 = Appreference.loginuserdetails.getTitle();
        final String gdr = Appreference.loginuserdetails.getGender();
        String no = " ";
        String Appversion="";
        Appversion=getResources().getString(R.string.app_version);
        Calendar calender= Calendar.getInstance();
        SimpleDateFormat simple=new SimpleDateFormat("ddMMM");
        no=simple.format(calender.getTime());
        Log.i("settingsfragment","no value is"+no);
        version_no.setText(version_no.getText() + no + Appversion);
        Log.i("settingsfragment",version_no.getText().toString());

        try{
            String s = "select * from taskDetailsInfo where readStatus='1'";
            ArrayList<ProjectDetailsBean> projectDetailsBeen = VideoCallDataBase.getDB(getContext()).getExclationdetails(s);
            if(projectDetailsBeen.size() > 0)
                exclation_counter.setVisibility(View.VISIBLE);
            else
                exclation_counter.setVisibility(View.GONE);

        }catch (Exception e){
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
        username.setText(ed1);
        em.setText(ed2);


        conflicttask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("SettingsFragment"," state ONOFF "+appSharedpreferences.getBoolean("conflictTask"));
                if (appSharedpreferences.getBoolean("conflictTask")==true){
                    Appreference.conflicttask = false;
                    con=false;
                    appSharedpreferences.saveBoolean("conflictTask", false);
                    Log.i("SettingsFragment"," state ONOFF if "+appSharedpreferences.getBoolean("conflictTask"));
                }else{
                    con=true;
                    Appreference.conflicttask = true;
                    appSharedpreferences.saveBoolean("conflictTask", true);
                    Log.i("SettingsFragment"," state ONOFF else "+appSharedpreferences.getBoolean("conflictTask"));
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
                if (Appreference.loginuserdetails.getId() != 0) {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("userId", String.valueOf(Appreference.loginuserdetails.getId())));
                    Log.i("Request", "Message " + nameValuePairs);
                    Appreference.jsonRequestSender.logoutMobile(EnumJsonWebservicename.logoutMobile, nameValuePairs, SettingsFragment.this);
                    progress = new ProgressDialog(getActivity());
                    progress.setMessage("Loading");
                    progress.show();
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

        /*sync_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("KeyWithValues", "value--->" + appSharedpreferences.getString(Appreference.loginuserdetails.getUsername() + "AllTask"));

                if (appSharedpreferences.getString(Appreference.loginuserdetails.getUsername() + "AllTask") == null || appSharedpreferences.getString(Appreference.loginuserdetails.getUsername() + "AllTask").equalsIgnoreCase("")) {
                    Log.d("KeyWithValues", "value--->1");
                    listAllMyTaskwebservice();
                    showprogress("Loading Your Tasks");

                    appSharedpreferences.saveString(Appreference.loginuserdetails.getUsername() + "AllTask", Appreference.loginuserdetails.getUsername());
                }
            }
        });*/

        show_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLog();

            }
        });
        OnOff.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (OnOff.isChecked()) {
//                    Toast.makeText(getActivity(), "Switch is on", Toast.LENGTH_LONG).show();
                } else {
//                    Toast.makeText(getActivity(), "Switch is Off", Toast.LENGTH_LONG).show();
                }

            }
        });

//        if(Appreference.loginuserdetails.getGender() !=null) {
//            if (gdr.equals("M")) {
//                gender = "Male";
//            } else {
//                gender = "Female";
//            }
//        }
//        else{
//            gender = "select";
//        }


      /*  btn=(Button) rootView.findViewById(R.id.addBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MultipleFileUploadActivity.class);
                startActivity(intent);

            }

        });
*/

//        final ImageButton imageButton = (ImageButton)rootView.findViewById(R.id.button2);
//        imageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PopupMenu popup = new PopupMenu(getActivity().getApplicationContext(), imageButton);
//                //Inflating the Popup using xml file
//                popup.getMenuInflater().inflate(R.menu.settings_menu, popup.getMenu());
//
//                //registering popup with OnMenuItemClickListener
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    public boolean onMenuItemClick(MenuItem item) {
//                        if (item.getTitle().equals("Edit")) {
//
//                            Intent intent = new Intent(getActivity(), UpdateProfileActivity.class);
//                            String ed1 = Appreference.loginuserdetails.getFirstName();
//                            String ed2 = Appreference.loginuserdetails.getLastName();
//                            String ed3 = Appreference.loginuserdetails.getTitle();
//                            String gdr = Appreference.loginuserdetails.getGender();
//                            String imagename1 = Appreference.loginuserdetails.getProfileImage();
//
//                            intent.putExtra("firstname", ed1);
//                            intent.putExtra("lastname", ed2);
//                            intent.putExtra("title", ed3);
//                            intent.putExtra("gender", gdr);
//                            intent.putExtra("profileimage", imagename1);
//                            startActivity(intent);
//
//                        } else if (item.getTitle().equals("Change Password")) {
//
//                            Intent intent=new Intent(getActivity(),ChangePassword.class);
//                            startActivity(intent);
//                        } else if (item.getTitle().equals("Logout")) {
//                            if (Appreference.loginuserdetails.getId() != 0) {
//                                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//                                nameValuePairs.add(new BasicNameValuePair("userId", String.valueOf(Appreference.loginuserdetails.getId())));
//                                Log.i("Request", "Message " + nameValuePairs);
//                                Appreference.jsonRequestSender.logoutMobile(EnumJsonWebservicename.logoutMobile, nameValuePairs, SettingsFragment.this);
//                                progress = new ProgressDialog(getActivity());
//                                progress.setMessage("Loading");
//                                progress.show();
//                            }
//                        }
//                        return true;
//                    }
//                });
//                popup.show();
//            }
//        });
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

    public void listAllMyTaskwebservice() {
        List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(1);
        nameValuePairs1.add(new BasicNameValuePair("userId", String.valueOf(Appreference.loginuserdetails.getId())));
        Log.i("listAllMyTaskwebservice", "userId " + Appreference.loginuserdetails.getId());
        showprogress("Loading Your Tasks");
        if (Appreference.jsonRequestSender == null) {
            JsonRequestSender jsonRequestParser = new JsonRequestSender();
            Appreference.jsonRequestSender = jsonRequestParser;
            jsonRequestParser.start();
        }
        Appreference.jsonRequestSender.listAllMyTask(EnumJsonWebservicename.listAllMyTask, nameValuePairs1, SettingsFragment.this);

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
//        final Vector files = og..getFiles();
            ArrayList<Uri> uris = new ArrayList<Uri>();
//        for (int i = 0; i < files.size(); i++) {
//            uris.add((Uri) files.elementAt(i));
//        }
            uris.add(Uri.fromFile(new File(Environment.getExternalStorageDirectory()
                    + "/High Message/HM_log.txt")));
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        }
        startActivityForResult(Intent.createChooser(intent, "Send mail..."), 1);
//    Intent intent = new Intent(context, SendLog.class);
//    startActivity(intent);


    }

    public void updateresponse(final Object obj) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                CommunicationBean communicationBean = (CommunicationBean) Appreference.communicationBean;
                Log.i("Update_method", "called--->" + communicationBean.getFirstname());
                String frt_lstName = communicationBean.getFirstname().trim() + " " + communicationBean.getLastname().trim();
                Log.i("settings", "first_last name1 " + frt_lstName);
                username.setText(frt_lstName);
                em.setText(Appreference.loginuserdetails.getEmail());
//                textview3.setText(communicationBean.getTitle());
//                String gen1 = communicationBean.getGender().toString();
//                if (gen1.equals("M")) {
//                    gen1 = "Male";
//                } else {
//                    gen1 = "Female";
//                }
//                textview4.setText(gen1);
                String oldImageName = communicationBean.getOldprofileimage();
                Log.i("old", "UserName---->" + oldImageName);
                String newImageName = communicationBean.getProfileimage();
                Log.i("new", "ResName---->" + newImageName);
                //imageLoader.DisplayImage(Environment.getExternalStorageDirectory().getAbsolutePath()+"/HighMessage/profilePic/"+  Appreference.loginuserdetails.getProfileImage(),imageview,R.drawable.default_person_circle);
                /*if (imagename != null && !imagename.equalsIgnoreCase(null) && !imagename.equalsIgnoreCase("")) {
                    Picasso.with(getContext()).load("http://122.165.92.171:8080/uploads/highmessaging/user/" + imagename).into(imageview);
                }*/
                //get file
                /*File photo = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/HighMessage/profilePic/"+imagename1);
                Log.i("old","image path----->"+photo.getName());
                //file name
                String oldpath = photo.getAbsolutePath();
                Log.i("old","file path------>"+oldpath);
                //resave file with new name
                File newFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/HighMessage/profilePic/"+imagename);
                Log.i("old","image path----->"+imagename);
                String newpath=newFile.getAbsolutePath();
                Log.i("new","file path name------>"+newFile);*/
                //photo.renameTo(newFile);
//                renameFile(oldImageName, newImageName);

            }
        });
    }

    /*public static void renameFile(String oldName, String newName) {
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/HighMessage/profilePic/");
        if (dir.exists()) {
            Log.i("old", "UserName >------>" + oldName);
            File from = new File(dir, oldName);
            Log.i("old", "UserName >" + from);
            Log.i("old", "UserName >------>" + newName);
            File to = new File(dir, newName);
            Log.i("new", "ResName >" + to);
            if (from.exists()) {
                from.renameTo(to);
                Appreference.loginuserdetails.setProfileImage(newName);
//                imageLoader.DisplayImage(Environment.getExternalStorageDirectory().getAbsolutePath() + "/HighMessage/profilePic/" + Appreference.loginuserdetails.getProfileImage(), imageview, R.drawable.default_person_circle);
                Log.i("new", "rename success---->" + from);
            }
        }
    }*/

    @Override
    public void onResume() {
        super.onResume();
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
    }

    @Override
    public void ResponceMethod(final Object object) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    CommunicationBean bean1 = (CommunicationBean) object;
                    String s1 = ((CommunicationBean) object).getEmail();
                    String s2 = bean1.getFirstname();
                    Log.i("Response", "Logged out Successfully " + s1);
                    if (s1.contains("result_code")) {
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
//                        Intent logoutscreen = new Intent(getActivity(), LoginActivity.class);

                        Appreference.isFCMRegister = true;
                        Appreference.networkState = false;
                        Appreference.signout_pressed = true;
//                        logoutscreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        logoutscreen.putExtra("logout", true);
////                            loginscreen.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                        startActivity(logoutscreen);
////                            Activity.class.getEnclosingClass();
//                        getActivity().finish();
                        Toast.makeText(getActivity(), "Logged out Successfully", Toast.LENGTH_SHORT).show();
                    }
                    /*else if (s2.equalsIgnoreCase("listAllMyTask")) {
                        cancelDialog();
//                    Log.i("Responce", "ValueGroup2-------------->" + s2);
                        Log.i("task", "jelement.getAsJsonObject() != null all task");
                        Gson gson = new Gson();
                        Type collectionType = new TypeToken<List<ListAllTaskBean>>() {
                        }.getType();
                        List<ListAllTaskBean> lcs = new Gson().fromJson(s1, collectionType);
                        Log.d("listAllMyTask", "1 firstname  " + lcs.size());

                        for (int i = 0; i < lcs.size(); i++) {
                            ListAllTaskBean listAllTaskbean = lcs.get(i);
//                    ListAllgetTaskDetails listAllgetTaskDetails = gson.fromJson(s2, ListAllgetTaskDetails.class);
                            // Log.d("getTask", "firstname  " + listAllgetTaskDetails.getParentId());
                            // Log.d("getTask", "firstname  " + listAllgetTaskDetails.getId());
                            Log.d("listAllMyTask", "2 firstname  " + i);
                            Log.d("listAllMyTask", "3 firstname  " + listAllTaskbean.getId());
                            VideoCallDataBase.getDB(classContext).insertORupdate_ListAllTaskDetails(listAllTaskbean);
                        }

//                    ArrayList<ListAllTaskBean> listAllTaskBeans=new ArrayList<>();
//                    try {
//                        JSONArray jsonarray = new JSONArray(object);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
                    }*/
                    else {
                        Toast.makeText(getActivity(), "Log out failure", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
