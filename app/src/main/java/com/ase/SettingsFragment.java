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
    static SettingsFragment fragment;
    TextView status;
    Handler handler = new Handler();
    String gender = null;
    Button version_no, show_log, sync_btn;
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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.settings_fragment_layout, container, false);
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
                if (Appreference.loginuserdetails.getId() != 0) {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("userId", String.valueOf(Appreference.loginuserdetails.getId())));
                    nameValuePairs.add(new BasicNameValuePair("deviceId", AppSharedpreferences.getInstance(getContext()).getString("fcmTokenId")));
                    Log.i("Request", "Message " + nameValuePairs);
                    progress = new ProgressDialog(getActivity());
                    progress.setMessage("Loading");
                    progress.show();
                    Appreference.jsonRequestSender.logoutMobile(EnumJsonWebservicename.logoutMobile, nameValuePairs, SettingsFragment.this);
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
        Log.i("Appreference", "OncreateView check sync size --=====>-------->" );

        String Query = "select * from projectStatus where wssendstatus= '000' order by datenow";
        ArrayList<TaskDetailsBean> AlltaskBean = VideoCallDataBase.getDB(classContext).getofflinesendlist(Query);

        String Query_media = "select * from taskDetailsInfo where wssendstatus= '000'";
        ArrayList<TaskDetailsBean> AlltaskMediaBean = VideoCallDataBase.getDB(classContext).getTaskDetailsInfo(Query_media);
        if (AlltaskBean!=null && AlltaskBean.size() > 0 || AlltaskMediaBean!=null && AlltaskMediaBean.size()>0 ) {
            sync_btn.setBackgroundResource(R.color.rednew);
        } else {
            sync_btn.setBackgroundResource(R.color.gray);

        }
      /*  if(Appreference.isSyncDone)
            sync_btn.setBackgroundResource(R.color.rednew);
        else
            sync_btn.setBackgroundResource(R.color.gray);
*/
        sync_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.i("offline123", "sync_btn clicklistener=====>");
                    sync_btn.setBackgroundResource(R.color.gray);

                    OfflineSendMessage offlineSendMessage = new OfflineSendMessage();
                    offlineSendMessage.sendOfflineMessages();

//                    sendOfflineMessages();
                } catch (Exception e) {
                    e.printStackTrace();
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

    private void multimedia() {
        String Query_media = "select * from taskDetailsInfo where wssendstatus= '000'";
        Log.i("offline123", "OfflineStatusSend query-=====> $$ " + Query_media);
        TaskDetailsBean mediaDetailsBean;
        ArrayList<TaskDetailsBean> AlltaskMediaBean;
        AlltaskMediaBean = VideoCallDataBase.getDB(classContext).getTaskDetailsInfo(Query_media);
        Log.i("offline123", "OfflineStatusSend query-=====> $$$ " + AlltaskMediaBean.size());
        if (AlltaskMediaBean != null && AlltaskMediaBean.size() > 0) {
            for (TaskDetailsBean detailsMediaBean : AlltaskMediaBean) {
                Log.i("conv123", "PercentageWebService taskStatus --------> 4 ");
                Log.i("conv123", "isDeassign is -------->  ");
                String desc_query = "Select * from projectHistory where projectId ='" + detailsMediaBean.getProjectId() + "' and taskId = '" + detailsMediaBean.getTaskId() + "'";
                mediaDetailsBean = VideoCallDataBase.getDB(classContext).getDetails_to_complete_project(desc_query);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateTime = dateFormat.format(new Date());
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                String dateforrow = dateFormat.format(new Date());
                String tasktime = "", taskUTCtime = "";
                tasktime = dateTime;
                tasktime = tasktime.split(" ")[1];
                taskUTCtime = dateforrow;
                final TaskDetailsBean chatBean = new TaskDetailsBean();

                chatBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
                chatBean.setFromUserName(Appreference.loginuserdetails.getUsername());
                chatBean.setSelect(false);
                String task_description = detailsMediaBean.getTaskDescription();
                String mime_type = detailsMediaBean.getMimeType();
                chatBean.setTaskDescription(task_description);
                chatBean.setSignalid(detailsMediaBean.getSignalid());
                chatBean.setTaskNo(detailsMediaBean.getTaskNo());
                chatBean.setTaskId(detailsMediaBean.getTaskId());
                chatBean.setTaskType(detailsMediaBean.getTaskType());
                chatBean.setTaskStatus(detailsMediaBean.getTaskStatus());
                chatBean.setToUserName(detailsMediaBean.getToUserName());
                chatBean.setToUserId(detailsMediaBean.getToUserId());
                chatBean.setIssueId("");
                chatBean.setParentId(getFileName());
                chatBean.setTaskPriority("Medium");
                chatBean.setIsRemainderRequired("");
                if (task_description.equalsIgnoreCase("Completed Percentage 100 %")) {
                    chatBean.setCompletedPercentage("100");
                } else {
                    chatBean.setCompletedPercentage("0");
                }
                chatBean.setPlannedStartDateTime("");
                chatBean.setPlannedEndDateTime("");
                chatBean.setRemainderFrequency("");
                chatBean.setTaskUTCDateTime(dateforrow);
                chatBean.setDateTime(dateTime);
                String project_deassignMems = "";
                chatBean.setOwnerOfTask(mediaDetailsBean.getOwnerOfTask());
                chatBean.setTaskReceiver(mediaDetailsBean.getTaskReceiver());
                chatBean.setTaskName(mediaDetailsBean.getTaskName());
                chatBean.setTasktime(tasktime);
                chatBean.setTaskUTCTime(taskUTCtime);
                chatBean.setMimeType(mime_type);
                // send status 0 is send 1 is unsend
                chatBean.setSendStatus("0");
                chatBean.setMsg_status(0);
                chatBean.setWs_send("0");
                chatBean.setCustomTagVisible(true);
                chatBean.setCatagory("Task");

                chatBean.setTaskRequestType("taskConversation");
                chatBean.setRequestStatus("normal");
                chatBean.setSubType("normal");
                if (mime_type != null && mime_type.equalsIgnoreCase("textfile")) {
                    chatBean.setLongmessage("0");
                }
                if (!mime_type.equalsIgnoreCase("text")) {
                    chatBean.setShow_progress(0);
                }
                chatBean.setProjectId(detailsMediaBean.getProjectId());
                chatBean.setGroupTaskMembers(mediaDetailsBean.getTaskMemberList());

                Log.i("conv123", "taskList will add ==>  " + isNetworkAvailable());
                if (!isNetworkAvailable()) {
                    Log.i("conv123", "isNetworkAvailable-------->  " + isNetworkAvailable());
                    chatBean.setWs_send("000");
                }
                Log.i("deassign123", "Deassgn flow=getSubType===>" + chatBean.getSubType());
                Log.i("deassign123", "Deassgn flow==getTaskStatus==>" + chatBean.getTaskStatus());
                Log.i("deassign123", "Deassgn flow===getTaskReceiver=>" + chatBean.getTaskReceiver());
                Log.i("deassign123", "Deassgn flow==getToUserName==>" + chatBean.getToUserName());
                Log.i("deassign123", "Deassgn flow==project_deassignMems==>" + project_deassignMems);

                try {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject jsonObject1 = new JSONObject();
                    if (detailsMediaBean.getTaskId() != null)
                        jsonObject1.put("id", Integer.parseInt(detailsMediaBean.getTaskId()));
                    jsonObject.put("task", jsonObject1);
                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put("id", Appreference.loginuserdetails.getId());
                    jsonObject.put("from", jsonObject2);

                    jsonObject.put("signalId", detailsMediaBean.getSignalid());
                    jsonObject.put("parentId", getFileName());
                    jsonObject.put("createdDate", dateforrow);
                    if (task_description != null && task_description.equalsIgnoreCase("Completed Percentage 100%")) {
                        jsonObject.put("requestType", "percentageCompleted");
                        jsonObject.put("taskStatus", "Completed");
                        jsonObject.put("percentageCompleted", "100");
                    } else {
                        jsonObject.put("requestType", "taskConversation");
                    }

                    jsonObject.put("requestStatus", "approved");
                    jsonObject.put("taskEndDateTime", "");
                    jsonObject.put("taskStartDateTime", "");
                    jsonObject.put("remainderDateTime", "");
                    jsonObject.put("dateFrequency", "");
                    jsonObject.put("timeFrequency", "");
                    jsonObject.put("remark", "");
                    JSONObject jsonObject5 = new JSONObject();
                    jsonObject5.put("id", Appreference.loginuserdetails.getId());
                    JSONObject jsonObject4 = new JSONObject();
                    jsonObject4.put("user", jsonObject5);
                    switch (mime_type.toLowerCase().trim()) {
                        case "text":
                            Log.i("taskconversation", "mediaListBean.getMimeType() --------> 17 " + mime_type);
                            jsonObject4.put("fileType", "text");
                            jsonObject4.put("description", task_description);
                            break;
                        case "assigntask":
                            Log.i("taskconversation", "mediaListBean.getMimeType() --------> 17 " + mime_type);
                            jsonObject4.put("fileType", "assigntask");
                            jsonObject4.put("description", task_description);
                            break;
                        case "image":
                            Log.i("taskconversation", "mediaListBean.getMimeType() --------> 13 " + mime_type);
                            jsonObject4.put("fileType", "image");
                            jsonObject4.put("fileContent", encodeTobase64(BitmapFactory.decodeFile(task_description)));
                            jsonObject4.put("taskFileExt", "jpg");
                            break;
                        case "video":
                            Log.i("taskconversation", "mediaListBean.getMimeType() --------> 14 " + mime_type);
                            jsonObject4.put("fileType", "video");
                            Log.i("task", "Video uploaded" + task_description);
                            jsonObject4.put("fileContent", encodeAudioVideoToBase64(task_description));
                            jsonObject4.put("taskFileExt", "mp4");
                            break;
                        case "audio":
                            Log.i("taskconversation", "mediaListBean.getMimeType() --------> 15 " + mime_type);
                            jsonObject4.put("fileType", "audio");
                            jsonObject4.put("fileContent", encodeAudioVideoToBase64(task_description));
                            jsonObject4.put("taskFileExt", "mp3");
                            break;
                       /* case "document":
                            Log.i("taskconversation", "mediaListBean.getMimeType() --------> 16 " + mime_type);
                            jsonObject4.put("fileType", mime_type);
                            jsonObject4.put("fileContent", encodeFileToBase64Binary(task_description));
                            jsonObject4.put("taskFileExt", getExt);
                            break;
                        case "map":
                            Log.i("taskconversation", "mediaListBean.getMimeType() --------> 18 " + mime_type);
                            jsonObject4.put("fileType", "map");
                            jsonObject4.put("description", task_description);
                            break;
                        case "textfile":
                            jsonObject4.put("fileType", mime_type);
                            jsonObject4.put("taskFileExt", "txt");
                            jsonObject4.put("fileContent", encodeFileToBase64Binary(task_description));
                            break;*/
                    }
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(0, jsonObject4);
                    jsonObject.put("listTaskConversationFiles", jsonArray);
                    Log.i("taskconversation", "mediaListBean.getMimeType() --------> 20 " + jsonArray);
                    if (jsonObject != null) {
                        if (Appreference.jsonOfflineRequestSender == null) {
                            JsonOfflineRequestSender jsonRequestofflineParser = new JsonOfflineRequestSender();
                            Appreference.jsonOfflineRequestSender = jsonRequestofflineParser;
                            jsonRequestofflineParser.start();
                        }
                        Appreference.jsonOfflineRequestSender.taskConversationEntry(EnumJsonWebservicename.taskConversationEntry, jsonObject, this, null, chatBean);
                        Appreference.printLog("Completed percentage", jsonObject.toString(), "Completed percentage", null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void sendOfflineMessages() {
        TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
        ArrayList<TaskDetailsBean> AlltaskBean;
        TaskDetailsBean machineDetailsBean;
        TaskDetailsBean traveldatebean;
        travel_date_details = null;
        travel_date_details = new ArrayList<>();
        try {
            if (isNetworkAvailable()) {
                showprogress("Please wait...");
                multimedia();
                String Query = "select * from projectStatus where wssendstatus= '000' order by datenow";
//                Log.i("offline123", "OfflineStatusSendActivity query-=====>" + Query);

                AlltaskBean = VideoCallDataBase.getDB(classContext).getofflinesendlist(Query);

                Log.i("offline123", "OfflineStatusSendActivity query-=====>" + AlltaskBean.size());
                if (AlltaskBean != null && AlltaskBean.size() > 0) {
                    for (TaskDetailsBean detailsBean : AlltaskBean) {
                        traveldatebean = new TaskDetailsBean();
                        String desc_query = "Select * from projectHistory where projectId ='" + detailsBean.getProjectId() + "' and taskId = '" + detailsBean.getTaskId() + "'";
                        machineDetailsBean = VideoCallDataBase.getDB(classContext).getDetails_to_complete_project(desc_query);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat dateParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat taskDateParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat taskDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                        taskDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Date date = null;
                        Date date1 = null;
                        Date date2 = null;
                        String StartDateUTC = "", EndDateUTC = "", taskCompletedDateUTC = "";
                        if (detailsBean.getTravelStartTime() != null && !detailsBean.getTravelStartTime().equalsIgnoreCase("")) {
                            try {
                                date = dateParse.parse(detailsBean.getTravelStartTime());
                                StartDateUTC = dateFormat.format(date);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (detailsBean.getTravelEndTime() != null && !detailsBean.getTravelEndTime().equalsIgnoreCase("")) {
                            try {
                                date1 = dateParse.parse(detailsBean.getTravelEndTime());
                                EndDateUTC = dateFormat.format(date1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (detailsBean.getProjectStatus() != null && detailsBean.getProjectStatus().equalsIgnoreCase("10")
                                && detailsBean.getTaskCompletedDate() != null && !detailsBean.getTaskCompletedDate().equalsIgnoreCase("")) {
                            try {
                                date2 = taskDateParse.parse(detailsBean.getTaskCompletedDate());
                                taskCompletedDateUTC = taskDateFormat.format(date2);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        JSONObject jsonObject = new JSONObject();
                        JSONObject jsonObject1 = new JSONObject();
                        TaskDetailsBean statusBean = new TaskDetailsBean();
                        String tasktime = dateFormat.format(new Date());
                        String dateforrow = dateFormat.format(new Date());
                        try {
                            jsonObject1.put("id", Integer.parseInt(detailsBean.getProjectId()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        jsonObject.put("project", jsonObject1);
                        statusBean.setProjectId(detailsBean.getProjectId());
                        JSONObject jsonObject2 = new JSONObject();
                        jsonObject2.put("id", Appreference.loginuserdetails.getId());
                        statusBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
                        jsonObject.put("from", jsonObject2);
                        JSONObject jsonObject3 = new JSONObject();
                        jsonObject3.put("id", Integer.parseInt(detailsBean.getTaskId()));
                        jsonObject.put("task", jsonObject3);
                        statusBean.setTaskId(detailsBean.getTaskId());
                        statusBean.setSignalid(detailsBean.getSignalid());

                        if (detailsBean.getTravelStartTime() != null && !detailsBean.getTravelStartTime().equalsIgnoreCase("")) {
                            jsonObject.put("travelStartTime", StartDateUTC);
                            statusBean.setTravelStartTime(detailsBean.getTravelStartTime());
                        } else {
                            jsonObject.put("travelStartTime", "");
                        }
                        if (detailsBean.getTravelEndTime() != null && !detailsBean.getTravelEndTime().equalsIgnoreCase("")) {
                            jsonObject.put("travelEndTime", EndDateUTC);
                            statusBean.setTravelEndTime(detailsBean.getTravelEndTime());
                        } else {
                            jsonObject.put("travelEndTime", "");
                        }
                        Log.i("offlilne", "travelstartdate ==> " + detailsBean.getTravelStartTime());
                        Log.i("offlilne", "travelEnddate ==> " + detailsBean.getTravelEndTime());

                        if (detailsBean.getTravelStartTime() != null && !detailsBean.getTravelStartTime().equalsIgnoreCase("")
                                && !detailsBean.getProjectStatus().equalsIgnoreCase("9")
                                && detailsBean.getEnd_dateStatus() != null && !detailsBean.getEnd_dateStatus().equalsIgnoreCase("")
                                && detailsBean.getEnd_dateStatus().equalsIgnoreCase("9")) {
                            Log.i("offlilne", "travelstartdate added==> # " + detailsBean.getTravelStartTime());
                        } else if (detailsBean.getTravelStartTime() != null && !detailsBean.getTravelStartTime().equalsIgnoreCase("")
                                && !detailsBean.getProjectStatus().equalsIgnoreCase("9")) {
//                            travel_date_details.add("StartTime : " + detailsBean.getTravelStartTime());
                            traveldatebean.setTravelStartTime(detailsBean.getTravelStartTime());
                            traveldatebean.setSignalid(detailsBean.getSignalid());
                            Log.i("offlilne", "travelstartdate added==> $" + detailsBean.getTravelStartTime());

                        }
                        if (detailsBean.getTravelEndTime() != null && !detailsBean.getTravelEndTime().equalsIgnoreCase("")) {
//                            travel_date_details.add("EndTime : " + detailsBean.getTravelEndTime());
                            traveldatebean.setTravelEndTime(detailsBean.getTravelEndTime());
                            traveldatebean.setSignalid(detailsBean.getSignalid());
                            Log.i("offlilne", "travelEnddate added ==> " + detailsBean.getTravelEndTime());
                            Log.i("offlilne", "getSignalid ==> " + detailsBean.getSignalid());
                        }
                        travel_date_details.add(traveldatebean);
                        jsonObject.put("activityStartTime", "");
                        jsonObject.put("activityEndTime", "");
                        jsonObject.put("toTravelStartDateTime", "");
                        jsonObject.put("toTravelEndDateTime", "");


                        if (detailsBean.getCustomerRemarks() != null && !detailsBean.getCustomerRemarks().equalsIgnoreCase("")) {
                            jsonObject.put("remarks", detailsBean.getCustomerRemarks());
                            statusBean.setCustomerRemarks(detailsBean.getCustomerRemarks());
                        } else {
                            jsonObject.put("remarks", "");
                        }
                        if (detailsBean.getProjectStatus() != null && !detailsBean.getProjectStatus().equalsIgnoreCase("")) {
                            jsonObject.put("status", detailsBean.getProjectStatus());
                            statusBean.setProjectStatus(detailsBean.getProjectStatus());
                        } else {
                            jsonObject.put("status", "");
                        }
                        if (detailsBean.getObservation() != null && !detailsBean.getObservation().equalsIgnoreCase("")) {
                            jsonObject.put("observation", detailsBean.getObservation());
                            statusBean.setObservation(detailsBean.getObservation());
                        } else {
                            jsonObject.put("observation", "");
                        }
                        if (detailsBean.getActionTaken() != null && !detailsBean.getActionTaken().equalsIgnoreCase("")) {
                            jsonObject.put("actionTaken", detailsBean.getActionTaken());
                            statusBean.setActionTaken(detailsBean.getActionTaken());
                        } else {
                            jsonObject.put("actionTaken", "");
                        }
                        if (detailsBean.getCustomerSignatureName() != null && !detailsBean.getCustomerSignatureName().equalsIgnoreCase("")) {
                            jsonObject.put("customerSignatureName", detailsBean.getCustomerSignatureName());
                            statusBean.setCustomerSignatureName(detailsBean.getCustomerSignatureName());
                        } else {
                            jsonObject.put("customerSignatureName", "");
                        }
                        if (detailsBean.getHMReading() != null && !detailsBean.getHMReading().equalsIgnoreCase("")) {
                            jsonObject.put("hourMeterReading", detailsBean.getHMReading());
                            statusBean.setHMReading(detailsBean.getHMReading());
                        } else {
                            jsonObject.put("hourMeterReading", "");
                        }
                        if (detailsBean.getTaskCompletedDate() != null && !detailsBean.getTaskCompletedDate().equalsIgnoreCase("")) {
                            jsonObject.put("taskcompletedDate", taskCompletedDateUTC);
                            statusBean.setTaskCompletedDate(detailsBean.getTaskCompletedDate());
                        } else {
                            jsonObject.put("taskcompletedDate", "");
                        }
                        jsonObject.put("mcModel", machineDetailsBean.getMcModel());
                        statusBean.setMcModel(machineDetailsBean.getMcModel());
                        jsonObject.put("mcSrNo", machineDetailsBean.getMcSrNo());
                        statusBean.setMcSrNo(machineDetailsBean.getMcSrNo());
                        jsonObject.put("mcDescription", machineDetailsBean.getMcDescription());
                        statusBean.setMcDescription(machineDetailsBean.getMcDescription());

                        statusBean.setTaskUTCDateTime(dateforrow);
                        statusBean.setDateTime(tasktime);
                        statusBean.setTasktime(tasktime);
                        statusBean.setTaskUTCTime(dateforrow);
                        statusBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
                        statusBean.setFromUserName(Appreference.loginuserdetails.getUsername());
                        if (detailsBean.getProjectStatus() != null && (detailsBean.getProjectStatus().equalsIgnoreCase("0"))) {
                            statusBean.setTaskStatus("Started");
                        } else if (detailsBean.getProjectStatus() != null && detailsBean.getProjectStatus().equalsIgnoreCase("1")) {
                            statusBean.setTaskStatus("Hold");
                        } else if (detailsBean.getProjectStatus() != null && (detailsBean.getProjectStatus().equalsIgnoreCase("2"))) {
                            statusBean.setTaskStatus("Resumed");
                        } else if (detailsBean.getProjectStatus() != null && (detailsBean.getProjectStatus().equalsIgnoreCase("3"))) {
                            statusBean.setTaskStatus("Paused");
                        } else if (detailsBean.getProjectStatus() != null && (detailsBean.getProjectStatus().equalsIgnoreCase("4"))) {
                            statusBean.setTaskStatus("Restarted");
                        } else if (detailsBean.getProjectStatus() != null && (detailsBean.getProjectStatus().equalsIgnoreCase("5"))) {
                            statusBean.setTaskStatus("Completed");
                        } else if (detailsBean.getProjectStatus() != null && detailsBean.getProjectStatus().equalsIgnoreCase("8")) {
                            statusBean.setTaskStatus("DeAssign");
                        }
                        String userQuery = "select * from taskDetailsInfo where taskId ='" + detailsBean.getTaskId() + "' and projectId ='" + detailsBean.getProjectId() + "'";
                        TaskDetailsBean memberBean = VideoCallDataBase.getDB(classContext).getUserdetails(userQuery);


                        statusBean.setGroupTaskMembers(machineDetailsBean.getTaskMemberList());
                        statusBean.setOwnerOfTask(machineDetailsBean.getOwnerOfTask());
                        statusBean.setTaskReceiver(machineDetailsBean.getTaskReceiver());
                        statusBean.setTaskName(machineDetailsBean.getTaskName());
//                        statusBean.setTaskDescription(memberBean.getTaskDescription());


                        statusBean.setToUserId(memberBean.getToUserId());
                        statusBean.setSendStatus(memberBean.getSendStatus());
                        statusBean.setTaskType(memberBean.getTaskType());
                        statusBean.setTaskNo(memberBean.getTaskNo());
                        statusBean.setToUserName(memberBean.getToUserName());
                        statusBean.setMimeType(memberBean.getMimeType());

                        statusBean.setPlannedStartDateTime("");
                        statusBean.setPlannedEndDateTime("");
                        statusBean.setTaskDescription(detailsBean.getTaskDescription());
                        Log.i("offlilne", "memberBean.getTaskDescription() ==> " + detailsBean.getTaskDescription());


                        statusBean.setUtcPlannedStartDateTime(Appreference.customLocalDateToUTC(null));
                        statusBean.setUtcplannedEndDateTime(Appreference.customLocalDateToUTC(null));
                        statusBean.setParentId(getFileName());
                        statusBean.setTaskPriority("Medium");
                        statusBean.setCompletedPercentage("");
                        statusBean.setRequestStatus("");
                        statusBean.setMsg_status(0);
                        statusBean.setShow_progress(1);


                        statusBean.setCatagory("Task");
                        statusBean.setSubType("normal");
                        statusBean.setTaskRequestType("normal");


                        if (detailsBean.getCustomerSignature() != null && !detailsBean.getCustomerSignature().equalsIgnoreCase(null) && !detailsBean.getCustomerSignature().equalsIgnoreCase("")) {
                            statusBean.setCustomerSignature(detailsBean.getCustomerSignature());
                        }
                        if (detailsBean.getPhotoPath() != null && !detailsBean.getPhotoPath().equalsIgnoreCase(null) && !detailsBean.getPhotoPath().equalsIgnoreCase("")) {
                            statusBean.setPhotoPath(detailsBean.getPhotoPath());
                        }
                        if (detailsBean.getTechnicianSignature() != null && !detailsBean.getTechnicianSignature().equalsIgnoreCase(null) && !detailsBean.getTechnicianSignature().equalsIgnoreCase("")) {
                            statusBean.setTechnicianSignature(detailsBean.getTechnicianSignature());
                        }

                        JSONObject jsonObject4 = new JSONObject();
                        if (detailsBean.getCustomerSignature() != null && !detailsBean.getCustomerSignature().equalsIgnoreCase(null) && !detailsBean.getCustomerSignature().equalsIgnoreCase("")) {
                            try {
                                TaskDetailsBean taskbean = (TaskDetailsBean) taskDetailsBean.clone();
                                Log.i("ws123", "taskDetailsBean1 reference signalID +++>>>   " + taskbean.getSignalid());
                                taskbean.setMimeType("image");
                                taskbean.setTaskDescription(detailsBean.getCustomerSignature());
                                taskbean.setTaskRequestType("signature");
                                jsonObject4.put("fileContent", encodeTobase64(BitmapFactory.decodeFile(detailsBean.getCustomerSignature())));
                                jsonObject4.put("taskFileExt", "jpg");
                                jsonObject.put("signatures", jsonObject4);
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                        }
                        JSONObject jsonObject5 = new JSONObject();
                        if (detailsBean.getPhotoPath() != null && !detailsBean.getPhotoPath().equalsIgnoreCase(null) && !detailsBean.getPhotoPath().equalsIgnoreCase("")) {
                            try {
                                TaskDetailsBean taskbean1 = (TaskDetailsBean) taskDetailsBean.clone();
                                taskbean1.setMimeType("image");
                                taskbean1.setTaskRequestType("photo");
                                taskbean1.setTaskDescription(detailsBean.getPhotoPath());
                                jsonObject5.put("fileContent", encodeTobase64(BitmapFactory.decodeFile(detailsBean.getPhotoPath())));
                                jsonObject5.put("taskFileExt", "jpg");
                                jsonObject.put("photos", jsonObject5);
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                        }
                        JSONObject jsonObject6 = new JSONObject();
                        if (detailsBean.getTechnicianSignature() != null && !detailsBean.getTechnicianSignature().equalsIgnoreCase(null) && !detailsBean.getTechnicianSignature().equalsIgnoreCase("")) {
                            try {
                                TaskDetailsBean taskbean2 = (TaskDetailsBean) taskDetailsBean.clone();
                                taskbean2.setMimeType("image");
                                taskbean2.setTaskRequestType("technicianSignature");
                                taskbean2.setTaskDescription(detailsBean.getTechnicianSignature());
                                jsonObject6.put("fileContent", encodeTobase64(BitmapFactory.decodeFile(detailsBean.getTechnicianSignature())));
                                jsonObject6.put("taskFileExt", "jpg");
                                jsonObject.put("technicianSignatures", jsonObject6);
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                        }
                        JSONObject jsonObject7 = new JSONObject();
                        if (detailsBean.getObservation() != null && !detailsBean.getObservation().equalsIgnoreCase(null)
                                && !detailsBean.getObservation().equalsIgnoreCase("") && detailsBean.getObservation().contains(".jpg")) {
                            try {
                                TaskDetailsBean taskbean2 = (TaskDetailsBean) taskDetailsBean.clone();
                                taskbean2.setMimeType("image");
                                taskbean2.setTaskRequestType("observation");
                                taskbean2.setTaskDescription(detailsBean.getObservation());
                                jsonObject7.put("fileContent", encodeTobase64(BitmapFactory.decodeFile(detailsBean.getObservation())));
                                jsonObject7.put("taskFileExt", "jpg");
                                jsonObject.put("observationImage", jsonObject7);
                                statusBean.setObservation(detailsBean.getObservation());
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                        }
                        JSONObject jsonObject8 = new JSONObject();
                        if (detailsBean.getActionTaken() != null && !detailsBean.getActionTaken().equalsIgnoreCase(null)
                                && !detailsBean.getActionTaken().equalsIgnoreCase("") && detailsBean.getActionTaken().contains(".jpg")) {
                            try {
                                TaskDetailsBean taskbean2 = (TaskDetailsBean) taskDetailsBean.clone();
                                taskbean2.setMimeType("image");
                                taskbean2.setTaskRequestType("actionTaken");
                                taskbean2.setTaskDescription(detailsBean.getActionTaken());
                                jsonObject8.put("fileContent", encodeTobase64(BitmapFactory.decodeFile(detailsBean.getActionTaken())));
                                jsonObject8.put("taskFileExt", "jpg");
                                jsonObject.put("actionTakenImage", jsonObject8);
                                statusBean.setActionTaken(detailsBean.getActionTaken());
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                        }
                        JSONObject jsonObject9 = new JSONObject();
                        if (detailsBean.getCustomerRemarks() != null && !detailsBean.getCustomerRemarks().equalsIgnoreCase(null)
                                && !detailsBean.getCustomerRemarks().equalsIgnoreCase("") && detailsBean.getCustomerRemarks().contains(".jpg")) {
                            try {
                                TaskDetailsBean taskbean2 = (TaskDetailsBean) taskDetailsBean.clone();
                                taskbean2.setMimeType("image");
                                taskbean2.setTaskRequestType("customerRemarks");
                                taskbean2.setTaskDescription(detailsBean.getCustomerRemarks());
                                jsonObject9.put("fileContent", encodeTobase64(BitmapFactory.decodeFile(detailsBean.getCustomerRemarks())));
                                jsonObject9.put("taskFileExt", "jpg");
                                jsonObject.put("remarksImage", jsonObject9);
                                statusBean.setCustomerRemarks(detailsBean.getCustomerRemarks());
                            } catch (CloneNotSupportedException e) {
                                e.printStackTrace();
                            }
                        }

//                        Log.i("offline123", "OfflineStatusSendActivity json==**-=====>" + jsonObject.toString());

                        if (Appreference.jsonOfflineRequestSender == null) {
                            JsonOfflineRequestSender jsonRequestofflineParser = new JsonOfflineRequestSender();
                            Appreference.jsonOfflineRequestSender = jsonRequestofflineParser;
                            jsonRequestofflineParser.start();
                        }
                        Appreference.jsonOfflineRequestSender.taskStatus(EnumJsonWebservicename.taskStatus, jsonObject, statusBean, SettingsFragment.this);
                    }
                } else {
                    cancelDialog();
                    showToast("No task to sync....");
                }

            } else {
                showToast("No Internet,Try again...");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String composeChatXML(TaskDetailsBean cmbean) {
        StringBuffer buffer = new StringBuffer();
        byte[] data_1;
        String base_64 = null;
        try {
            byte[] data = cmbean.getTaskDescription().trim().getBytes("UTF-8");
            Log.d("base64value", "base64 before " + cmbean.getTaskDescription());
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
            if (cmbean.getServerFileName() != null) {
                data_1 = cmbean.getServerFileName().trim().getBytes("UTF-8");
                Log.d("base64value", "base64 before " + cmbean.getServerFileName());
                base_64 = Base64.encodeToString(data_1, Base64.DEFAULT);
            }
            buffer.append("<?xml version=\"1.0\"?>" + "<TaskDetailsinfo><TaskDetails");

            if (cmbean.getTaskObservers() != null) {
                buffer.append(" taskAddObservers=" + quotes + cmbean.getTaskObservers() + quotes);
            }

            if (cmbean.getTaskRemoveObservers() != null) {
                buffer.append(" taskRemoveObservers=" + quotes + cmbean.getTaskRemoveObservers() + quotes);
            }

            if (cmbean.getTaskName() != null && !cmbean.getTaskName().equalsIgnoreCase("") && !cmbean.getTaskName().equalsIgnoreCase(null)) {
                String taskName = cmbean.getTaskName();
                if (taskName.contains("&") || taskName.contains("<") || taskName.contains("\"")) {
                    if (taskName.contains("<")) {
                        taskName = taskName.replaceAll("<", "&lt;");
                    }
                    if (taskName.contains("&")) {
                        taskName = taskName.replaceAll("&", "&amp;");
                    }
                    if (taskName.contains("\"")) {
                        taskName = taskName.replaceAll("\"", "&quot;");
                    }
                    buffer.append(" taskName=" + quotes + taskName + quotes);
                    Log.i("URL", "value " + taskName);
                } else {
                    buffer.append(" taskName=" + quotes + cmbean.getTaskName() + quotes);
                }
//                buffer.append(" taskName=" + quotes + cmbean.getTaskName() + quotes);
            } else {
                buffer.append(" taskName=" + quotes + "New Task" + quotes);
            }
            if (!cmbean.getMimeType().equalsIgnoreCase("text") && !cmbean.getMimeType().equalsIgnoreCase("url")) {
                if ((cmbean.getServerFileName() != null) && (!cmbean.getServerFileName().equalsIgnoreCase(null)) && (!cmbean.getServerFileName().equalsIgnoreCase(""))) {
                    Log.i("URL", "SaveFilename-->" + base_64.trim());
                    buffer.append(" taskDescription=" + quotes + base_64.trim() + quotes);
                } else {
                    Log.i("URL", "TaskDes-->" + base64);
                    buffer.append(" taskDescription=" + quotes + base64.trim() + quotes);
                }
            } else {
                Log.i("URL", "value * " + cmbean.getTaskDescription());
                if (base64 != null) {
                    String s = base64.trim();
                    if (s.contains("&") || s.contains("<") || s.contains("\"")) {
                        if (s.contains("<")) {
                            s = s.replaceAll("<", "&lt;");
                        }
                        if (s.contains("&")) {
                            s = s.replaceAll("&", "&amp;");
                        }
                        if (s.contains("\"")) {
                            s = s.replaceAll("\"", "&quot;");
                        }
                        buffer.append(" taskDescription=" + quotes + s + quotes);
                        Log.i("URL", "value " + s);
                    } else {
                        buffer.append(" taskDescription=" + quotes + base64.trim() + quotes);
                    }
                }
            }
            buffer.append(" fromUserId=" + quotes + cmbean.getFromUserId() + quotes);
            buffer.append(" fromUserName=" + quotes + cmbean.getFromUserName() + quotes);
            if (!getResources().getString(R.string.proxyua).equalsIgnoreCase("enable")) {
                if (cmbean.getTaskStatus() != null && cmbean.getTaskStatus().equalsIgnoreCase("draft")) {
                    buffer.append(" toUserId=" + quotes + "    " + quotes);
                    buffer.append(" toUserName=" + quotes + "        " + quotes);
                } else {
                    buffer.append(" toUserId=" + quotes + cmbean.getToUserId() + quotes);
                    buffer.append(" toUserName=" + quotes + cmbean.getToUserName() + quotes);
                }
            } else {
                if (cmbean.getProjectId() != null) {
                    buffer.append(" toUserId=" + quotes + "" + quotes);
                    buffer.append(" toUserName=" + quotes + "" + quotes);
                } else {
                    buffer.append(" toUserId=" + quotes + cmbean.getToUserId() + quotes);
                    buffer.append(" toUserName=" + quotes + cmbean.getToUserName() + quotes);
                }
            }
            buffer.append(" taskNo=" + quotes + cmbean.getTaskNo() + quotes);
            buffer.append(" taskId=" + quotes + cmbean.getTaskId() + quotes);
            buffer.append(" taskType=" + quotes + cmbean.getTaskType() + quotes);
            buffer.append(" plannedStartDateTime=" + quotes + cmbean.getUtcPlannedStartDateTime() + quotes);
            buffer.append(" plannedEndDateTime=" + quotes + cmbean.getUtcplannedEndDateTime() + quotes);
            buffer.append(" isRemainderRequired=" + quotes + cmbean.getIsRemainderRequired() + quotes);
            Log.i("newtaskconversation", "remainderDateTime " + cmbean.getUtcPemainderFrequency());
            if (cmbean.getUtcPemainderFrequency() == null || (cmbean.getUtcPemainderFrequency() != null && cmbean.getUtcPemainderFrequency().equalsIgnoreCase(""))) {
                cmbean.setUtcPemainderFrequency("");
            }
            buffer.append(" remainderDateTime=" + quotes + cmbean.getUtcPemainderFrequency() + quotes);
            if (cmbean.getCompletedPercentage() != null && !cmbean.getCompletedPercentage().equalsIgnoreCase("") && Integer.parseInt(cmbean.getCompletedPercentage()) == 100) {
                if (cmbean.getTaskStatus() != null && cmbean.getTaskStatus().equalsIgnoreCase("Closed")) {
                    Log.i("newtaskconversation", " compose taskStatus 1 " + cmbean.getTaskStatus());
                    buffer.append(" taskStatus=" + quotes + "Closed" + quotes);
                } else {
                    Log.i("newtaskconversation", " compose taskStatus 2 " + cmbean.getTaskStatus());
                    if (cmbean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
//                        if (isProjectFromOracle)
                        buffer.append(" taskStatus=" + quotes + "Completed" + quotes);
                        /*else
                            buffer.append(" taskStatus=" + quotes + "Closed" + quotes);*/
                    } else {
                        buffer.append(" taskStatus=" + quotes + "Completed" + quotes);
                    }
//                    buffer.append(" taskStatus=" + quotes + "Completed" + quotes);
                }
            } else {
                Log.i("newtaskconversation", " compose taskStatus 3 " + cmbean.getTaskStatus());
                Log.i("Accept", "value taskStatus after compose " + cmbean.getTaskStatus());
                buffer.append(" taskStatus=" + quotes + cmbean.getTaskStatus() + quotes);
            }
            buffer.append(" signalid=" + quotes + cmbean.getSignalid() + quotes);
            buffer.append(" parentId=" + quotes + cmbean.getParentId() + quotes);
            buffer.append(" taskRequestType=" + quotes + cmbean.getTaskRequestType() + quotes);
            buffer.append(" dateFrequency=" + quotes + cmbean.getDateFrequency() + quotes);
            String TimeFrequency = "", ReminderQuotes = "";
            if (cmbean.getFromUserName() != null && cmbean.getFromUserName().equalsIgnoreCase(cmbean.getOwnerOfTask())) {
                Log.i("newtaskconversation", "cmbean.getTimeFrequency() " + cmbean.getTimeFrequency() + " is reminderrequired " + cmbean.getIsRemainderRequired());
//                if (cmbean.getTimeFrequency() != null && (cmbean.getIsRemainderRequired() != null && cmbean.getIsRemainderRequired().equalsIgnoreCase("Y"))) {
//                    TimeFrequency = TimeFrequencyCalculation(cmbean.getTimeFrequency());
//                } else {
                TimeFrequency = "";
//                }
            } else {
//                if (cmbean.getTimeFrequency() != null && (cmbean.getIsRemainderRequired() != null && cmbean.getIsRemainderRequired().equalsIgnoreCase("Y"))) {
//                    TimeFrequency = TimeFrequencyCalculation(cmbean.getTimeFrequency());
//                } else {
                TimeFrequency = "";
//                }
            }
            buffer.append(" timeFrequency=" + quotes + TimeFrequency + quotes);
            buffer.append(" taskOwner=" + quotes + cmbean.getOwnerOfTask() + quotes);
            buffer.append(" mimeType=" + quotes + cmbean.getMimeType() + quotes);
            buffer.append(" dateTime=" + quotes + cmbean.getTaskUTCDateTime() + quotes);
            buffer.append(" taskPriority=" + quotes + cmbean.getTaskPriority() + quotes);
            buffer.append(" completedPercentage=" + quotes + cmbean.getCompletedPercentage() + quotes);
            buffer.append(" remainderQuotes=" + quotes + cmbean.getReminderQuote() + quotes);
            buffer.append(" remark=" + quotes + cmbean.getRemark() + quotes);
            buffer.append(" taskReceiver=" + quotes + cmbean.getTaskReceiver() + quotes);
            if (cmbean.getProjectId() != null) {
                if (cmbean.getOwnerOfTask() != null && !cmbean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                    buffer.append(" taskToUsersList=" + quotes + cmbean.getOwnerOfTask() + quotes);
                }
            } else {
                buffer.append(" taskToUsersList=" + quotes + cmbean.getGroupTaskMembers() + quotes);
            }
            buffer.append(" requestStatus=" + quotes + cmbean.getRequestStatus() + quotes);
            buffer.append(" subType=" + quotes + cmbean.getSubType() + quotes);
            buffer.append(" daysOfTheWeek=" + quotes + cmbean.getDaysOfTheWeek() + quotes);
            buffer.append(" repeatFrequency=" + quotes + cmbean.getRepeatFrequency() + quotes);
            buffer.append(" taskTagName=" + quotes + cmbean.getTaskTagName() + quotes);
            buffer.append(" taskTagId=" + quotes + cmbean.getCustomTagId() + quotes);
            buffer.append(" taskTagGroupId=" + quotes + cmbean.getCustomSetId() + quotes);
            buffer.append(" isShowOnUI=" + quotes + cmbean.isCustomTagVisible() + quotes);
            Log.i("taskconversation", "Leave Project id 3 " + cmbean.getProjectId());
            buffer.append(" projectId=" + quotes + cmbean.getProjectId() + quotes);
            buffer.append(" taskCategory=" + quotes + cmbean.getCatagory() + quotes);
            Log.i("taskconversation", "Leave Project id 4 " + cmbean.getProjectId());
            buffer.append(" parentTaskId=" + quotes + cmbean.getIssueId() + quotes);
            if (cmbean.getDaysOfTheWeek() != null && !cmbean.getDaysOfTheWeek().equalsIgnoreCase("") && !cmbean.getDaysOfTheWeek().equalsIgnoreCase(null)) {
                buffer.append(" isRepeatTask=" + quotes + "Y" + quotes);
            }
            buffer.append(" />");
            buffer.append("</TaskDetailsinfo>");
            Log.d("xml", "composed xml for chat======>" + buffer.toString());
            Log.d("xml", "composed xml for encode data======>" + Charset.forName("UTF-8").encode(":-)").toString());
//            Log.i("xml", "composed xml for listofabservers " + listOfObservers);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return buffer.toString();
        }
    }

    public void sendMultiInstantMessage(String msgBody, ArrayList<String> userlist, int sendTo) {
        for (String name : userlist) {
            Log.i("taskConversation", "sendMultiInstantMessage 1  ");
            Log.i("task observer", "observer 1 " + name);
            Log.i("task observer", "MainActivity.account.buddyList.size()" + MainActivity.account.buddyList.size());
        }
        Log.i("groupMemberAccess", "!group ");
        Log.i("taskConversation", "sendMultiInstantMessage 2  ");
//        if (!getResources().getString(R.string.proxyua).equalsIgnoreCase("enable") || !taskType.equalsIgnoreCase("Group")) {
        if (sendTo == 0) {
            Log.i("taskConversation", "sendMultiInstantMessage 3  ");
            for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
                Log.i("taskConversation", "sendMultiInstantMessage 4  ");
                String name = MainActivity.account.buddyList.get(i).cfg.getUri();
                Log.i("task", "buddyname-->  " + name);
                for (String username : userlist) {
                    Log.i("taskConversation", "sendMultiInstantMessage 5  ");
                    Log.i("task", "taskObservers Name--> " + username);
                    String nn = "sip:" + username + "@" + getResources().getString(R.string.server_ip);
                    Log.i("task", "selected user--> " + nn);
                    if (nn.equalsIgnoreCase(name)) {
                        Log.i("taskConversation", "sendMultiInstantMessage 6  ");
                        Log.i("task", "both users are same ");
                        Appreference.printLog("Sipmessage", msgBody, "DEBUG", null);
                        MyBuddy myBuddy = MainActivity.account.buddyList.get(i);
                        SendInstantMessageParam prm = new SendInstantMessageParam();
                        prm.setContent(msgBody);

                        boolean valid = myBuddy.isValid();
                        Log.i("task", "valid ======= " + valid);
                        try {
                            Log.i("taskConversation", "sendMultiInstantMessage 7  ");
                            myBuddy.sendInstantMessage(prm);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
    }

    public void sendMessage(String message, String pri, final String type1, final String imagename, final String remquotes_2, String sig_id, TaskDetailsBean taskDetailsBean) {
        try {
            Log.i("deassign123", "------sendMessage entry------");
            Log.i("taskConversation", "private sendMessage * 0 ");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = dateFormat.format(new Date());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateforrow = dateFormat.format(new Date());
            String tasktime = "", taskUTCtime = "";
            tasktime = dateTime;
            tasktime = tasktime.split(" ")[1];
            taskUTCtime = dateforrow;
            final TaskDetailsBean chatBean = new TaskDetailsBean();
            chatBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
            chatBean.setFromUserName(Appreference.loginuserdetails.getUsername());
            chatBean.setSelect(false);

            chatBean.setToUserId(taskDetailsBean.getToUserId());
            chatBean.setToUserName(taskDetailsBean.getToUserName());
            chatBean.setTaskReceiver(taskDetailsBean.getTaskReceiver());
            chatBean.setCatagory("Task");
            chatBean.setTaskNo(taskDetailsBean.getTaskNo());
            chatBean.setIssueId("");
            chatBean.setParentId(getFileName());
            chatBean.setPlannedStartDateTime("");
            chatBean.setPlannedEndDateTime("");
            chatBean.setRemainderFrequency("");
            chatBean.setDateFrequency("");
            chatBean.setTimeFrequency("");
            chatBean.setServerFileName("");
            chatBean.setTaskUTCDateTime(dateforrow);
            chatBean.setDateTime(dateTime);
            chatBean.setSendStatus("0");
            chatBean.setTaskType(taskDetailsBean.getTaskType());
            chatBean.setTaskId(taskDetailsBean.getTaskId());
            chatBean.setTasktime(tasktime);
            chatBean.setTaskUTCTime(taskUTCtime);
            chatBean.setSignalid(sig_id);
            chatBean.setCustomTagVisible(true);
            chatBean.setTaskStatus(taskDetailsBean.getTaskStatus());
            chatBean.setProjectId(taskDetailsBean.getProjectId());
            chatBean.setParentTaskId(taskDetailsBean.getParentTaskId());
            chatBean.setTaskName(taskDetailsBean.getTaskName());
            chatBean.setOwnerOfTask(taskDetailsBean.getOwnerOfTask());
            chatBean.setTaskRequestType("normal");
            if (taskDetailsBean.getGroupTaskMembers() != null) {
                chatBean.setGroupTaskMembers(taskDetailsBean.getGroupTaskMembers());
            }

            chatBean.setTaskDescription(message);
            chatBean.setMimeType(type1);
            chatBean.setTaskPriority("Medium");
            chatBean.setSubType("normal");
            String percentage = "";
            if (chatBean.getProjectId() != null) {
                percentage = VideoCallDataBase.getDB(classContext).getlastProjectCompletedPercentage(chatBean.getTaskId());
            }
            if (percentage != null && !percentage.equalsIgnoreCase(null) && !percentage.equalsIgnoreCase("") && !percentage.equalsIgnoreCase("null")) {
                chatBean.setCompletedPercentage(percentage);
            } else {
                chatBean.setCompletedPercentage("0");
            }

            if (chatBean.getTaskDescription() != null &&
                    (chatBean.getTaskDescription().equalsIgnoreCase("Completed Percentage 100%") || chatBean.getTaskDescription().equalsIgnoreCase("Task is Completed"))) {
                chatBean.setCompletedPercentage("100");
            }
            Log.i("deassign123", "percent -----> " + chatBean.getTaskDescription());

            if (remquotes_2 != null && !remquotes_2.equalsIgnoreCase("") && !remquotes_2.equalsIgnoreCase(null) && chatBean.getMimeType() != null) {
                Log.i("deassign123", "map -----> " + chatBean.getTaskDescription());
                if (remquotes_2.equalsIgnoreCase("map") && chatBean.getMimeType().equalsIgnoreCase("map")) {
                    chatBean.setSubType("percentageCompleted");
                    chatBean.setTaskStatus("completed");
                }
            }
            if (chatBean.getTaskId() != null && chatBean.getTaskId().equalsIgnoreCase(chatBean.getTaskId())) {
                if (chatBean.getProjectId() != null) {
                    VideoCallDataBase.getDB(classContext).update_Project_history(chatBean);
                }
            }
            listOfObservers.add(taskDetailsBean.getOwnerOfTask());
            Log.i("offline", "listOfObservers==> $$ " + listOfObservers.size());
            if (listOfObservers != null && listOfObservers.size() > 0) {
                Log.i("offline", "listOfObservers==> " + listOfObservers.size());
                if (listOfObservers.size() == 1) {
                    String xml = composeChatXML(chatBean);
                    sendMultiInstantMessage(xml, listOfObservers, 0);
                } else {
                    for (String buddy_username : listOfObservers) {
                        if (!chatBean.getTaskType().equalsIgnoreCase("group")) {
                            chatBean.setToUserName(buddy_username);
                            int ToUserid = VideoCallDataBase.getDB(classContext).getUserid(buddy_username);
                            chatBean.setToUserId(String.valueOf(ToUserid));
                        }
                    }
                    String xml = composeChatXML(chatBean);
                    sendMultiInstantMessage(xml, listOfObservers, 0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void PercentageWebService(String getMediaType, String getMediaPath, String getExt, String sig_id, int isDateorUpdateorNormal, TaskDetailsBean taskDetailsBean) {
        if (!getMediaPath.equals(null)) {
            Log.i("conv123", "PercentageWebService taskStatus --------> 4 " + taskDetailsBean.getTaskStatus());
            Log.i("conv123", "isDeassign is -------->  ");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = dateFormat.format(new Date());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateforrow = dateFormat.format(new Date());
            String tasktime = "", taskUTCtime = "";
            tasktime = dateTime;
            tasktime = tasktime.split(" ")[1];
            taskUTCtime = dateforrow;
            final TaskDetailsBean chatBean = new TaskDetailsBean();

            chatBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
            chatBean.setFromUserName(Appreference.loginuserdetails.getUsername());
            chatBean.setSelect(false);
            chatBean.setTaskDescription(getMediaPath);
            chatBean.setSignalid(sig_id);
            chatBean.setTaskNo(taskDetailsBean.getTaskNo());
            chatBean.setIssueId("");
            chatBean.setParentId(getFileName());
            chatBean.setTaskType(taskDetailsBean.getTaskType());
            chatBean.setTaskPriority("Medium");
            chatBean.setIsRemainderRequired("");
            if (getMediaPath.equalsIgnoreCase("Completed Percentage 100 %")) {
                chatBean.setCompletedPercentage("100");
            } else {
                chatBean.setCompletedPercentage("0");
            }
            chatBean.setPlannedStartDateTime("");
            chatBean.setPlannedEndDateTime("");
            chatBean.setRemainderFrequency("");
            chatBean.setTaskUTCDateTime(dateforrow);
            chatBean.setDateTime(dateTime);
            String project_deassignMems = "";
            chatBean.setOwnerOfTask(taskDetailsBean.getOwnerOfTask());
            chatBean.setTaskStatus(taskDetailsBean.getTaskStatus());
            chatBean.setTaskReceiver(taskDetailsBean.getTaskReceiver());
            chatBean.setToUserName(taskDetailsBean.getToUserName());
            chatBean.setToUserId(taskDetailsBean.getToUserId());
//            }
            chatBean.setTasktime(tasktime);
            chatBean.setTaskUTCTime(taskUTCtime);
            chatBean.setMimeType(getMediaType);
            chatBean.setTaskId(taskDetailsBean.getTaskId());
            chatBean.setProjectId(taskDetailsBean.getProjectId());
            chatBean.setTaskName(taskDetailsBean.getTaskName());
            chatBean.setCatagory("Task");
            chatBean.setSubType("normal");
            chatBean.setTaskRequestType("normal");
            chatBean.setRequestStatus("normal");

            // send status 0 is send 1 is unsend
            chatBean.setSendStatus("0");
            chatBean.setMsg_status(0);
            chatBean.setWs_send("0");
            chatBean.setCustomTagVisible(true);


            if (getMediaType != null && getMediaType.equalsIgnoreCase("textfile")) {
                chatBean.setLongmessage("0");
            }
            if (!getMediaType.equalsIgnoreCase("text")) {
                chatBean.setShow_progress(0);
            }
            if (taskDetailsBean.getGroupTaskMembers() != null) {
                chatBean.setGroupTaskMembers(taskDetailsBean.getGroupTaskMembers());
            }
            Log.i("deassign123", "Deassgn flow=getSubType===>" + chatBean.getSubType());
            Log.i("deassign123", "Deassgn flow==getTaskStatus==>" + chatBean.getTaskStatus());
            Log.i("deassign123", "Deassgn flow===getTaskReceiver=>" + chatBean.getTaskReceiver());
            Log.i("deassign123", "Deassgn flow==getToUserName==>" + chatBean.getToUserName());
            Log.i("deassign123", "Deassgn flow==project_deassignMems==>" + project_deassignMems);

            try {
                JSONObject jsonObject = new JSONObject();
                JSONObject jsonObject1 = new JSONObject();
                if (taskDetailsBean.getTaskId() != null)
                    jsonObject1.put("id", Integer.parseInt(taskDetailsBean.getTaskId()));
                jsonObject.put("task", jsonObject1);
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("id", Appreference.loginuserdetails.getId());
                jsonObject.put("from", jsonObject2);
                jsonObject.put("signalId", sig_id);
                jsonObject.put("parentId", getFileName());
                jsonObject.put("createdDate", dateforrow);
                jsonObject.put("requestType", "taskConversation");
                if (getMediaPath != null && getMediaPath.equalsIgnoreCase("Completed Percentage 100%")) {
                    jsonObject.put("requestType", "percentageCompleted");
                    jsonObject.put("taskStatus", "Completed");
                    jsonObject.put("percentageCompleted", "100");
                }
                if (getMediaPath != null && (getMediaPath.equalsIgnoreCase("This Task is closed") || getMediaPath.equalsIgnoreCase("This issue is closed"))) {
                    jsonObject.put("requestStatus", "");
                } else {
                    jsonObject.put("requestStatus", "approved");
                }
                jsonObject.put("taskEndDateTime", "");
                jsonObject.put("taskStartDateTime", "");
                jsonObject.put("remainderDateTime", "");
                jsonObject.put("dateFrequency", "");
                jsonObject.put("timeFrequency", "");
                jsonObject.put("remark", "");
                JSONObject jsonObject5 = new JSONObject();
                jsonObject5.put("id", Appreference.loginuserdetails.getId());
                JSONObject jsonObject4 = new JSONObject();
                jsonObject4.put("user", jsonObject5);
                switch (getMediaType.toLowerCase().trim()) {
                    case "text":
                        Log.i("taskconversation", "mediaListBean.getMimeType() --------> 17 " + getMediaType);
                        jsonObject4.put("fileType", "text");
                        jsonObject4.put("description", getMediaPath);
                        break;
                    case "assigntask":
                        Log.i("taskconversation", "mediaListBean.getMimeType() --------> 17 " + getMediaType);
                        jsonObject4.put("fileType", "assigntask");
                        jsonObject4.put("description", getMediaPath);
                        break;
                    case "image":
                        Log.i("taskconversation", "mediaListBean.getMimeType() --------> 13 " + getMediaType);
                        jsonObject4.put("fileType", "image");
                        jsonObject4.put("fileContent", encodeTobase64(BitmapFactory.decodeFile(getMediaPath)));
                        jsonObject4.put("taskFileExt", "jpg");
                        break;
                    case "video":
                        Log.i("taskconversation", "mediaListBean.getMimeType() --------> 14 " + getMediaType);
                        jsonObject4.put("fileType", "video");
                        Log.i("task", "Video uploaded" + getMediaPath);
                        jsonObject4.put("fileContent", encodeAudioVideoToBase64(getMediaPath));
                        jsonObject4.put("taskFileExt", "mp4");
                        break;
                    case "audio":
                        Log.i("taskconversation", "mediaListBean.getMimeType() --------> 15 " + getMediaType);
                        jsonObject4.put("fileType", "audio");
                        jsonObject4.put("fileContent", encodeAudioVideoToBase64(getMediaPath));
                        jsonObject4.put("taskFileExt", "mp3");
                        break;
                }
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(0, jsonObject4);
                jsonObject.put("listTaskConversationFiles", jsonArray);
                Log.i("taskconversation", "mediaListBean.getMimeType() --------> 20 " + jsonArray);
                if (jsonObject != null) {
                    Appreference.printLog("Completed percentage", jsonObject.toString(), "Completed percentage", null);
                    Log.i("taskconversation", "mediaListBean.getMimeType() --------> 21 " + getMediaType);
                    Appreference.jsonOfflineRequestSender.taskConversationEntry(EnumJsonWebservicename.taskConversationEntry, jsonObject, this, null, chatBean);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void oracle_percentage(String getMediaType, String getMediaPath, String getExt, String sig_id, TaskDetailsBean taskDetailsBean) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = dateFormat.format(new Date());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateforrow = dateFormat.format(new Date());
            String tasktime = "", taskUTCtime = "";
            tasktime = dateTime;
            tasktime = tasktime.split(" ")[1];
            taskUTCtime = dateforrow;
            final TaskDetailsBean chatBean = new TaskDetailsBean();
            chatBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
            chatBean.setFromUserName(Appreference.loginuserdetails.getUsername());
            chatBean.setSelect(false);
            chatBean.setTaskDescription(getMediaPath);
            chatBean.setSignalid(sig_id);
            chatBean.setTaskNo(taskDetailsBean.getTaskNo());
            chatBean.setIssueId("");
            chatBean.setParentId(getFileName());
            chatBean.setTaskType(taskDetailsBean.getTaskType());
            chatBean.setTaskPriority("Medium");
            chatBean.setIsRemainderRequired("");
            chatBean.setCompletedPercentage("100");
            chatBean.setPlannedStartDateTime("");
            chatBean.setPlannedEndDateTime("");
            chatBean.setRemainderFrequency("");
            chatBean.setTaskUTCDateTime(dateforrow);
            chatBean.setDateTime(dateTime);
            String project_deassignMems = "";
//            Log.i("selfassign", "Self_assign==> " + Self_assign + " oracleProjectOwner --> " + oracleProjectOwner);
            chatBean.setOwnerOfTask(taskDetailsBean.getOwnerOfTask());
            chatBean.setTaskStatus("completed");
//            taskStatus = "completed";
            chatBean.setTaskReceiver(taskDetailsBean.getTaskReceiver());
            chatBean.setToUserName(taskDetailsBean.getToUserName());
            chatBean.setToUserId(taskDetailsBean.getToUserId());
            chatBean.setTasktime(tasktime);
            chatBean.setTaskUTCTime(taskUTCtime);
            chatBean.setMimeType(getMediaType);
            chatBean.setTaskId(taskDetailsBean.getTaskId());


            // send status 0 is send 1 is unsend
            chatBean.setSendStatus("0");
            chatBean.setMsg_status(0);
            chatBean.setWs_send("0");
            chatBean.setCustomTagVisible(true);
            chatBean.setCatagory("Task");
            chatBean.setProjectId(taskDetailsBean.getProjectId());
            Log.i("offline ", "GroupTaskMembers==> " + taskDetailsBean.getGroupTaskMembers());
            if (taskDetailsBean.getGroupTaskMembers() != null) {
                chatBean.setGroupTaskMembers(taskDetailsBean.getGroupTaskMembers());
            }
            chatBean.setSubType("percentageCompleted");
            chatBean.setTaskName(taskDetailsBean.getTaskName());
            chatBean.setTaskRequestType("normal");
            chatBean.setRequestStatus("normal");
            chatBean.setSubType("normal");
            VideoCallDataBase.getDB(classContext).update_Project_history(chatBean);
            if (VideoCallDataBase.getDB(classContext).insertORupdate_Task_history(chatBean)) {
               /* if (chatBean.isCustomTagVisible()) {
                    taskList.add(chatBean);
                    cancelDialog();
                    Log.i("conv123", "" + getMediaPath + "added DB successfully=====>" + taskList.size());
                }*/
                Log.i("task", "msg Status " + chatBean.getMsg_status());
//                refresh();
            }
            String parent_id = "";
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("id", taskDetailsBean.getTaskId());
            jsonObject.put("task", jsonObject1);
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("id", taskDetailsBean.getFromUserId());
            jsonObject.put("from", jsonObject2);
            JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("id", taskDetailsBean.getToUserId());

            jsonObject.put("signalId", sig_id);
            jsonObject.put("parentId", parent_id);
            jsonObject.put("createdDate", dateFormat.format(new Date()));
            jsonObject.put("percentageCompleted", "100");
            jsonObject.put("taskNo", taskDetailsBean.getTaskNo());
            jsonObject.put("requestType", "percentageCompleted");
            jsonObject.put("taskStatus", "completed");
            Appreference.jsonRequestSender.taskConversationEntry(EnumJsonWebservicename.taskConversationEntry, jsonObject, SettingsFragment.this, null, chatBean);
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public String getFileName() {
        String strFilename = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
            Date date = new Date();
            strFilename = dateFormat.format(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("NewTaskConversation", "getFileName Exception " + e.getMessage(), "WARN", null);
        }
        return strFilename;
    }

    private String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    private String encodeAudioVideoToBase64(String path) {
        String strFile = null;
        File file = new File(path);
        try {
            FileInputStream file1 = new FileInputStream(file);
            byte[] Bytearray = new byte[(int) file.length()];
            file1.read(Bytearray);
            strFile = Base64.encodeToString(Bytearray, Base64.DEFAULT);//Convert byte array into string
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strFile;
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
        Log.i("Appreference", "onResume inside--=====>-------->" );

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
        if (AlltaskBean!=null && AlltaskBean.size() > 0 || AlltaskMediaBean!=null && AlltaskMediaBean.size()>0 ) {
            sync_btn.setBackgroundResource(R.color.rednew);
        } else {
            sync_btn.setBackgroundResource(R.color.gray);

        }
       /* if(Appreference.isSyncDone)
            sync_btn.setBackgroundResource(R.color.rednew);
        else
            sync_btn.setBackgroundResource(R.color.gray);*/
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
                        if (WebServiceEnum_Response != null && WebServiceEnum_Response.equalsIgnoreCase(("taskStatus"))) {
                            cancelDialog();
                            final JSONObject jsonObject1 = new JSONObject(s1);
                            if ((int) jsonObject1.get("result_code") == 0) {
                                Log.i("offline123", "taskStatus settingsFrag ResponceMethod");
                                try {
                                    final JSONObject json_Object = new JSONObject(s1);
                                    String projectCurrentStatus = "";
                                    if (((String) json_Object.get("result_text")).equalsIgnoreCase("task started")) {
                                    } else if (((String) json_Object.get("result_text")).equalsIgnoreCase("task hold")) {
                                        projectCurrentStatus = "hold";
                                    } else if (((String) json_Object.get("result_text")).equalsIgnoreCase("task resume")) {
                                        projectCurrentStatus = "resume";
                                    } else if (((String) json_Object.get("result_text")).equalsIgnoreCase("task pause")) {
                                        projectCurrentStatus = "pause";
                                    } else if (((String) json_Object.get("result_text")).equalsIgnoreCase("task restart")) {
                                        projectCurrentStatus = "restart";
                                    } else if (((String) json_Object.get("result_text")).equalsIgnoreCase("task completed")) {
                                        projectCurrentStatus = "Completed";
                                    } else if (((String) jsonObject.get("result_text")).equalsIgnoreCase("task deassign")) {
                                        projectCurrentStatus = "DeAssign";
                                    }
                                    final TaskDetailsBean detailsBean = bean1.getTaskDetailsBean();
                                    VideoCallDataBase.getDB(classContext).taskWSStatusUpdateINStatus(detailsBean.getSignalid(), "111");
                                    detailsBean.setMimeType("text");
                                    detailsBean.setCustomTagVisible(true);
                                    Log.i("offline123", " status w/s detailsBean.getSignalid()===>" + detailsBean.getSignalid());
                                    Log.i("offline123", " w/s getTaskDescription()===>" + detailsBean.getTaskDescription());
                                    Log.i("offline123", "w/s detailsBean.getCustomerRemarks()===>" + detailsBean.getCustomerRemarks());

                                    if (detailsBean.getProjectStatus() != null && detailsBean.getProjectStatus().equalsIgnoreCase("5")) {
                                        oracle_percentage("text", "Completed Percentage 100%", "", Utility.getSessionID(), detailsBean);
                                    }
                                    PercentageWebService("text", detailsBean.getTaskDescription(), "", detailsBean.getSignalid(), 0, detailsBean);

                                    if (detailsBean.getCustomerRemarks() != null && !detailsBean.getCustomerRemarks().equalsIgnoreCase("") && !detailsBean.getCustomerRemarks().equalsIgnoreCase("null")
                                            && !detailsBean.getProjectStatus().equalsIgnoreCase("10")) {
                                        if (projectCurrentStatus != null && !projectCurrentStatus.equalsIgnoreCase("Completed")) {
                                            Log.i("offline123", "webservice 3  ");
                                            PercentageWebService("text", detailsBean.getCustomerRemarks(), "", Utility.getSessionID(), 0, detailsBean);
                                        }

                                    }
                                    Log.i("offline123", "travel_date_details ==> " + travel_date_details);
                                    Log.i("offline123", "travel_date_details.size() ==> " + travel_date_details.size());
                                    /*if (travel_date_details != null && travel_date_details.size() > 0 && detailsBean.getTaskDescription().equalsIgnoreCase("Gathering Details...")) {
//                                        int sec = 0;
                                        for (final String travel : travel_date_details) {
//                                            sec = sec + 2000;
//                                            handler.postDelayed(new Runnable() {
//                                                @Override
//                                                public void run() {
                                            Log.i("offline123", "webservice 4 ");
                                            Log.i("offline123", "webservice_travel " + travel);
                                            Log.i("offline123", "webservice_travel ##  " + detailsBean.getTaskDescription());
                                            PercentageWebService("text", travel, "", Utility.getSessionID(), 0, detailsBean);
                                             *//*   }
                                            }, sec);*//*
                                        }
                                    }*/
                                    if (travel_date_details != null && travel_date_details.size() > 0 && detailsBean.getTaskDescription().equalsIgnoreCase("Gathering Details...")) {
                                        for (final TaskDetailsBean detailsBean1 : travel_date_details) {
                                            Log.i("offline123", "webservice 4 ");
                                            Log.i("offline123", "traveldatebean 1 "+detailsBean1.getTravelStartTime());
                                            Log.i("offline123", "traveldatebean 2 "+detailsBean1.getTravelEndTime());
                                            Log.i("offline123", "traveldatebean 3 "+detailsBean1.getSignalid());
                                            Log.i("offline123", "traveldatebean 4 "+detailsBean.getSignalid());
                                            if(detailsBean1.getSignalid()!=null && detailsBean1.getSignalid().equalsIgnoreCase(detailsBean.getSignalid())){
                                                if (detailsBean1.getTravelStartTime() != null) {
                                                    PercentageWebService("text", detailsBean1.getTravelStartTime(), "", Utility.getSessionID(), 0, detailsBean);
                                                }
                                                if (detailsBean1.getTravelEndTime() != null) {
                                                    Log.i("offline123", "traveldatebean === > "+detailsBean1.getTravelEndTime());
                                                    PercentageWebService("text", detailsBean1.getTravelEndTime(), "", Utility.getSessionID(), 0, detailsBean);
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.i("offline123", "NewTaskConv sip responce a jsonobject Exception*******" + e);
                                }
                            } else {
                                String result = (String) jsonObject1.get("result_text");
                                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                            }
                        } else if (WebServiceEnum_Response != null && WebServiceEnum_Response.equalsIgnoreCase("taskConversationEntry")) {
                            Log.i("response", "offline ===>  ");
                            TaskDetailsBean detailsBean = bean1.getTaskDetailsBean();
                            Log.i("taskConversationEntry", "signalId===> " + detailsBean.getSignalid());
                            VideoCallDataBase.getDB(classContext).taskWSStatusUpdate(detailsBean.getSignalid(), "111");
                            JSONObject jobject = new JSONObject(s1);
                            if (jobject.has("fileName")) {
                                String fileName = jobject.get("fileName").toString();
                                fileName = fileName.split("\"")[1];
                                detailsBean.setServerFileName(fileName);
                                detailsBean.setTaskDescription(fileName);
                                Log.i("taskConversationEntry", "fileName $$ ===> " + fileName);
                            }
                            if (detailsBean != null) {
                                Log.i("response", "Notes  18 ");
                                Log.i("taskConversationEntry", "value taskStatus before compose 3 " + detailsBean.getTaskDescription());
                                Log.i("taskConversationEntry", "getTaskStatus==> " + detailsBean.getTaskStatus());
                                Log.i("taskConversationEntry", "getProjectStatus==> " + detailsBean.getProjectStatus());
                                if (detailsBean.getTaskDescription() != null) {
                                    sendMessage(detailsBean.getTaskDescription(), null, detailsBean.getMimeType(), null, "",
                                            detailsBean.getSignalid(), detailsBean);
                                }
                            }
                        } else {
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
                        }
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
