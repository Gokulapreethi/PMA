package com.ase;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ase.Bean.ProjectDetailsBean;
import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;
import com.ase.DatePicker.CustomTravelPickerActivity;
import com.ase.RandomNumber.Utility;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pjsip.pjsua2.Buddy;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.app.MyBuddy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.JsonRequestSender;
import json.WebServiceInterface;


/**
 * Created by prasanth on 6/24/2017.
 */
public class TravelJobDetails extends Activity implements View.OnClickListener, CommonDateTimePicker.DateWatcher, WebServiceInterface {
    ImageView showMy_ID, status_job, travel_job, drop_down;
    TextView cancel, head, text11, text12, text13, tv_networksate, updatingtask, item_counter, tv_reassign;
    LinearLayout linear1, header_menu, ll_networkstate, updatingtask_layout;
    ProgressBar progress_updating;
    ListView list_all;
    public String taskName, projectId, webtaskId, strIPath, mime_Type, projectCurrentStatus;
    public static MediaListAdapter medialistadapter;
    public Context context;
    TextView signature_path, photo_path, tech_signature_path;
    boolean isCustomerSign, isProjectFromOracle, isTemplate;
    static TravelJobDetails travelJobDetails;
    ProgressDialog progress;
    boolean Start_work;
    boolean hold_work;
    boolean resume_work;
    boolean pause_work;
    boolean restart_work;
    boolean completed_work;
    boolean Self_assign = false;
    boolean isLocation = false;
    boolean isTaskCompleted = false;
    boolean isForOracleProject = false;
    boolean isDivertedON = false;
    boolean isDeassign = false;
    boolean project;
    boolean isSignalidSame = false;
    ArrayList<String> custom_1MediaList;
    //Added for estimTravelTimer
    TextView tv_estim_travel_timer;
    CounterTravelClass estimTravelCounter;

    private HashMap<Integer, String> statusCompletedFieldValues = new HashMap<Integer, String>();
    String TravelStartdate, TravelEnddate, status_signature, photo_signature, tech_signature, PickDate, travel_endDate;
    String FromTravelStart, FromTravelEnd, ActivityStartdate, ActivityEnddate, TotravelStart, ToTravelEnd;
    String observationStatus, actiontakenStatus, custsignnameStatus, HMReadingStatus;
    ArrayList<String> travel_date_details;
    String tasktime = null, taskUTCtime, oracleProjectOwner = "", OracleParentTaskId, groupname;
    String toUserName, taskType, task_No, ownerOfTask, taskReceiver, category, issueId, parentTaskId, projectGroup_Mems, taskStatus, project_toUsers = "";
    private static Handler handler;
    private String quotes = "\"";
    public static ArrayList<String> listOfObservers;
    static ArrayList<TaskDetailsBean> taskList = new ArrayList<>();
    LinearLayout ll_networkUI = null;
    RelativeLayout ll_2;
    TextView tv_networkstate = null;
    int toUserId;
    VideoCallDataBase dataBase;
    String tab = "TravelJobDetails";
    AppSharedpreferences appSharedpreferences;
    public static MediaListAdapter.ViewHolder holder;
    String JobCodeNo, ActivityCode;
    int clickPosition;
    ArrayList<String> OracleStatusList;
    boolean isOracleStatusList, isgettask;
    private String proxy_user = "proxyua_highmessaging.com";
    static AlertDialog.Builder alertbox;
    static AlertDialog alertDialog;

    // for location
    LOCTracker loc;
    LocationManager locationManager;
    double latitude_global;
    double longitude_global;
    private boolean back_preesed = false;

    //    added for webservice resend
    Timer timer = null;
    TimerTask timerTask;

    public static TravelJobDetails getInstance() {
        /*try {
            if (travelJobDetails == null) {
                travelJobDetails = new TravelJobDetails();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "getInstance Exception : " + e.getMessage(), "WARN", null);
        }*/
        return travelJobDetails;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traveldetails_conversation);
        Appreference.context_table.put("traveljobdetails", this);
        handler = new Handler();
        context = this;
        travelJobDetails = this;
        showMy_ID = (ImageView) findViewById(R.id.showMy_ID);
        tv_estim_travel_timer = (TextView) findViewById(R.id.estim_travel_timer);
        status_job = (ImageView) findViewById(R.id.status_job);
        travel_job = (ImageView) findViewById(R.id.travel_job);
        drop_down = (ImageView) findViewById(R.id.drop_down);

        ll_networkUI = (LinearLayout) findViewById(R.id.ll_networkstate);
        tv_networkstate = (TextView) findViewById(R.id.tv_networksate);


        cancel = (TextView) findViewById(R.id.cancel);
        head = (TextView) findViewById(R.id.head);
        text11 = (TextView) findViewById(R.id.text11);
        text12 = (TextView) findViewById(R.id.text12);
        text13 = (TextView) findViewById(R.id.text13);

        updatingtask = (TextView) findViewById(R.id.updatingtask);
        item_counter = (TextView) findViewById(R.id.item_counter);
        tv_reassign = (TextView) findViewById(R.id.tv_reassign);
        linear1 = (LinearLayout) findViewById(R.id.linear1);
        header_menu = (LinearLayout) findViewById(R.id.header_menu);

        updatingtask_layout = (LinearLayout) findViewById(R.id.updatingtask_layout);
        ll_2 = (RelativeLayout) findViewById(R.id.ll_2);

        progress_updating = (ProgressBar) findViewById(R.id.progress_updating);
        list_all = (ListView) findViewById(R.id.list_all);
        list_all.setAdapter(medialistadapter);

        appSharedpreferences = AppSharedpreferences.getInstance(context);
        custom_1MediaList = new ArrayList<>();
        listOfObservers = new ArrayList<>();
        dataBase = VideoCallDataBase.getDB(context);
        String conversation_In = "";
        OracleStatusList = new ArrayList<String>();
        Log.i(tab, "onCreate");
        loc = new LOCTracker(TravelJobDetails.this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {
            if (getIntent() != null) {
                if (getIntent().getExtras() != null) {
                    conversation_In = getIntent().getExtras().getString("task");
                    oracleProjectOwner = getIntent().getExtras().getString("oracleProjectOwner");
                    isProjectFromOracle = getIntent().getBooleanExtra("ProjectFromOracle", false);
                    isTemplate = getIntent().getBooleanExtra("isTemplate", false);
                    clickPosition = getIntent().getIntExtra("position", 0);
                    Log.i(tab, "isProjectFromOracle " + isProjectFromOracle);
                    Log.i(tab, "isProjectFromOracle " + isTemplate);
                    Log.i(tab, "oracleProjectOwner===>" + oracleProjectOwner + " logged in by===>" + Appreference.loginuserdetails.getUsername());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "getIntent() Exception : " + e.getMessage(), "WARN", null);
        }

        if (conversation_In != null && conversation_In.equalsIgnoreCase("projectHistory")) {
            try {
                final ProjectDetailsBean projectDetailsBean = (ProjectDetailsBean) getIntent().getSerializableExtra("projectHistoryBean");
                Log.i(tab, "projectHistory ");
                project = true;
                OracleParentTaskId = projectDetailsBean.getParentTaskId();
                groupname = projectDetailsBean.getToUserName();
                webtaskId = projectDetailsBean.getTaskId();
                task_No = projectDetailsBean.getTaskNo();
                taskName = projectDetailsBean.getTaskName();
                taskType = projectDetailsBean.getTaskType();
                if (taskType != null && (!taskType.equalsIgnoreCase("Group") || !taskType.equalsIgnoreCase("group"))) {
                    if (projectDetailsBean.getToUserId() != null && !projectDetailsBean.getToUserId().equalsIgnoreCase("")) {
                        toUserId = Integer.parseInt(projectDetailsBean.getToUserId());
                    }
                    toUserName = projectDetailsBean.getToUserName();
                }
                project_toUsers = projectDetailsBean.getTaskMemberList();
                Log.i(tab, "project_toUsers ## " + project_toUsers);
                Log.i(tab, "taskType ## " + taskType);
                Log.i(tab, "getTaskReceiver ## " + projectDetailsBean.getTaskReceiver());
                Log.i(tab, "getTaskMemberList() ** " + projectDetailsBean.getTaskMemberList());
                Log.i(tab, "OwnerOfTask()() ** " + projectDetailsBean.getOwnerOfTask());
                Log.i(tab, "taskstatus()() ** " + projectDetailsBean.getTaskStatus());
                Log.i(tab, "touserName()() ** " + projectDetailsBean.getToUserName());
                ownerOfTask = projectDetailsBean.getOwnerOfTask();
                projectId = projectDetailsBean.getId();
                parentTaskId = projectDetailsBean.getParentTaskId();
                taskReceiver = projectDetailsBean.getTaskReceiver();
                category = "task";
                projectGroup_Mems = projectDetailsBean.getTaskMemberList();
                taskStatus = projectDetailsBean.getTaskStatus();
                oracleProjectOwner = projectDetailsBean.getOwnerOfTask();
                Log.i(tab, "templatehistory sync ==> " + appSharedpreferences.getBoolean("syncTask" + webtaskId));
                if (isNetworkAvailable()) {
                    if (!appSharedpreferences.getBoolean("syncTask" + webtaskId)) {
                        gettaskwebservice();
                        isgettask = true;
                        Log.i(tab, "templatehistory - Gettaskwebservice called for taskId is " + webtaskId);
                    }
                }

                Log.i(tab, "taskStatus==> 1 " + taskStatus);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Appreference.printLog("TravelJobDetails", "projectHistory intent Exception : " + e.getMessage(), "WARN", null);
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("TravelJobDetails", "projectHistory intent Exception : " + e.getMessage(), "WARN", null);
            }
        } else if (conversation_In != null && conversation_In.equalsIgnoreCase("ProjectTemplateview")) {
            try {
                final ProjectDetailsBean projectDetailsBean = (ProjectDetailsBean) getIntent().getSerializableExtra("projectHistoryBean");
                Log.i(tab, "ProjectTemplateview ");
                Log.i(tab, "project_toUsers ## ==>  " + project_toUsers);
                Log.i(tab, "project_toUsers ## ==> " + projectDetailsBean.getListTaskToUser());
                Log.i(tab, "project_toUsers ## ==> " + projectDetailsBean.getListMemberProject());
                Log.i(tab, "TaskMemberList() ** ==> " + projectDetailsBean.getTaskMemberList());
                Log.i(tab, "OwnerOfTask()() ** ==> " + projectDetailsBean.getOwnerOfTask());
                project = true;
                OracleParentTaskId = projectDetailsBean.getParentTaskId();
                groupname = projectDetailsBean.getToUserName();
                taskType = projectDetailsBean.getTaskType();
                webtaskId = projectDetailsBean.getTaskId();
                task_No = projectDetailsBean.getTaskNo();
                taskName = projectDetailsBean.getTaskName();
                ownerOfTask = projectDetailsBean.getOwnerOfTask();
                projectId = projectDetailsBean.getId();
                parentTaskId = projectDetailsBean.getParentTaskId();
//            taskReceiver = projectDetailsBean.getTaskReceiver();
                category = "task";
                projectGroup_Mems = projectDetailsBean.getTaskMemberList();
                taskStatus = projectDetailsBean.getTaskStatus();
                oracleProjectOwner = projectDetailsBean.getOwnerOfTask();
                Log.i(tab, "taskStatus==> " + taskStatus);
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("TravelJobDetails", "ProjectTemplateview intent Exception : " + e.getMessage(), "WARN", null);
            }
        }

        try {
            if (Appreference.loginuserdetails.getUsername().equalsIgnoreCase(ownerOfTask)) {
                text12.setText("Me");
            } else {
                Log.i(tab, "ownerOfTask -->  ");
                String task_giver = VideoCallDataBase.getDB(context).getname(ownerOfTask);
                if (taskReceiver != null) {
                    Log.i(tab, "task_giver -->  " + task_giver);
                    Log.i(tab, "taskReceiver -->  " + taskReceiver);
                    if (task_giver == null || task_giver.equalsIgnoreCase("") || task_giver.equals(null)) {
                        text12.setText(taskReceiver.split("_")[0]);
                    } else {
                        text12.setText(task_giver);
                    }
                } else {
                    text12.setText(task_giver);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "text12 Exception : " + e.getMessage(), "WARN", null);
        }
        Log.i(tab, "taskStatus **$  ==> " + taskStatus);
        String GroupAdmin_observer = getGroupAdmin_observer_DB();
        try {
            if (taskStatus != null && (taskStatus.equalsIgnoreCase("draft") || taskStatus.equalsIgnoreCase("Unassigned"))) {
                if ((Appreference.loginuserdetails != null && Appreference.loginuserdetails.getRoleId() != null
                        && Appreference.loginuserdetails.getRoleId().equalsIgnoreCase("2")
                        && !oracleProjectOwner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) &&
                        (GroupAdmin_observer != null && !GroupAdmin_observer.contains(Appreference.loginuserdetails.getUsername()))) {
                    ll_2.setVisibility(View.GONE);
                } else {
                    ll_2.setVisibility(View.VISIBLE);
                }
                status_job.setVisibility(View.GONE);
                travel_job.setVisibility(View.GONE);
            } else {
                ll_2.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "ll_2 Exception : " + e.getMessage(), "WARN", null);
        }
        try {
            Log.i(tab, "oracleProjectOwner **  ==> " + oracleProjectOwner);
            /*Code Added for GroupAdmin-Observer Start*/

            if (taskStatus != null && (!taskStatus.equalsIgnoreCase("draft") && !taskStatus.equalsIgnoreCase("template") && !taskStatus.equalsIgnoreCase("Unassigned"))) {
                tv_reassign.setVisibility(View.GONE);
            } else if ((oracleProjectOwner != null && oracleProjectOwner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) ||
                    (GroupAdmin_observer != null && GroupAdmin_observer.contains(Appreference.loginuserdetails.getUsername()))) {
                 /*Code Added for GroupAdmin-Observer End*/
                tv_reassign.setText("Assign Task");
                status_job.setVisibility(View.GONE);
                travel_job.setVisibility(View.GONE);
            } else {
                tv_reassign.setText("Assign to me");
                Self_assign = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "tv_reassign Exception : " + e.getMessage(), "WARN", null);
        }
        try {
            if (isProjectFromOracle && (oracleProjectOwner != null && !oracleProjectOwner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())
                    && (Appreference.loginuserdetails != null && Appreference.loginuserdetails.getRoleId() != null
                    && !Appreference.loginuserdetails.getRoleId().equalsIgnoreCase("2")))) {
                if (!isTaskCompleted && (!taskStatus.equalsIgnoreCase("draft") && !taskStatus.equalsIgnoreCase("template") && !taskStatus.equalsIgnoreCase("Unassigned"))) {
                    Log.i(tab, "isTaskCompleted $ **  ==> " + isTaskCompleted);
                    status_job.setVisibility(View.VISIBLE);
                    travel_job.setVisibility(View.VISIBLE);
                } else if (isTaskCompleted) {
                    Log.i(tab, "isTaskCompleted else  $ **  ==> " + isTaskCompleted);
                    status_job.setVisibility(View.GONE);
                    travel_job.setVisibility(View.GONE);
                }
            } else if (isProjectFromOracle && (oracleProjectOwner != null && oracleProjectOwner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))) {
                if (!isTaskCompleted && (!taskStatus.equalsIgnoreCase("draft") && !taskStatus.equalsIgnoreCase("template") && !taskStatus.equalsIgnoreCase("Unassigned"))) {
                    Log.i("desc123", "is template=======> ");
                    status_job.setVisibility(View.VISIBLE);
                } else if (isTaskCompleted) {
                    status_job.setVisibility(View.GONE);
                }
            /*added for GroupAdmin observer start*/
            } else if (isProjectFromOracle && (Appreference.loginuserdetails != null && Appreference.loginuserdetails.getRoleId() != null
                    && Appreference.loginuserdetails.getRoleId().equalsIgnoreCase("2"))) {
            /*added for GroupAdmin observer*/
                if (taskStatus != null && (!taskStatus.equalsIgnoreCase("draft") && !taskStatus.equalsIgnoreCase("template") && !taskStatus.equalsIgnoreCase("Unassigned"))) {
                    status_job.setVisibility(View.VISIBLE);
                    travel_job.setVisibility(View.GONE);
                }
            }
        /*added for GroupAdmin observerm end*/
            if (taskStatus != null && (taskStatus.equalsIgnoreCase("Completed") || taskStatus.equalsIgnoreCase("complete"))) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ll_2.setVisibility(View.GONE);
                        status_job.setVisibility(View.GONE);
                        travel_job.setVisibility(View.GONE);
                    }
                });
            }
            ll_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isNetworkAvailable()) {
                        try {
                            Log.i(tab, "tv_reassign " + Self_assign);
                            if (!Self_assign)
                                addTaskReassignClickEvent();
                            else {
                                sendAssignTask_webservice();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("TravelJobDetails", "ll_2.setOnClick Exception : " + e.getMessage(), "WARN", null);
                        }
                    } else {
                        Toast.makeText(TravelJobDetails.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "oncreate UI Exception : " + e.getMessage(), "WARN", null);
        }
        try {
            String qury = "select oracleProjectId from projectHistory where projectId='" + projectId + "' and taskId= '" + webtaskId + "'";
            String qury_1 = "select oracleTaskId from projectHistory where projectId='" + projectId + "' and taskId= '" + webtaskId + "'";
            JobCodeNo = VideoCallDataBase.getDB(getApplication()).getProjectParentTaskId(qury);
            ActivityCode = VideoCallDataBase.getDB(getApplication()).getProjectParentTaskId(qury_1);
            Log.i(tab, "JobCodeNo==> " + JobCodeNo);
            Log.i(tab, "ActivityCode==> " + ActivityCode);
            head.setText("Job Card No :" + JobCodeNo + "\nActivity Code :" + ActivityCode);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "head.setText Exception : " + e.getMessage(), "WARN", null);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                    if (projectHistory != null)
                        projectHistory.setProgressBarInvisible();
                    Appreference.webview_refresh = true;
                    Log.i(tab, "back layout  ");
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("TravelJobDetails", "cancel click Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });

        showMy_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final Dialog dialog = new Dialog(TravelJobDetails.this);
                    dialog.setContentView(R.layout.project_task_id);
                    dialog.setTitle(taskName);
                    TextView project_id_show = (TextView) dialog.findViewById(R.id.project_id_show);
                    TextView task_id_show = (TextView) dialog.findViewById(R.id.task_id_show);
                    Button dialogButtonOK = (Button) dialog.findViewById(R.id.dialogButtonOK);
                    project_id_show.setText(projectId);
                    task_id_show.setText(webtaskId);
                    dialogButtonOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("TravelJobDetails", "showMy_ID click Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });
        try {
            String query_status = "select status from projectStatus where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "'";
            int disable_by_current_status = VideoCallDataBase.getDB(context).getCurrentStatus(query_status);
            if (disable_by_current_status == 5)
                isTaskCompleted = true;
            if (isTaskCompleted) {
                status_job.setVisibility(View.GONE);
                travel_job.setVisibility(View.GONE);
            }
            if (disable_by_current_status != 8) {
                String taskQuery = "select * from taskDetailsInfo where projectId='" + projectId + "'and taskId= '" + webtaskId + "' and estimCompletion='1'";
                int getEstimatedTimerCompleted = VideoCallDataBase.getDB(context).getCountForTravelEntry(taskQuery);
                if (getEstimatedTimerCompleted == 0) {
                    Show_travel_EstimTimerDisplay();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "isTaskCompleted Exception : " + e.getMessage(), "WARN", null);
        }

        status_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showStatusPopupWindow(v);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("TravelJobDetails", "status_job click Exception : " + e.getMessage(), "WARN", null);
                }
            }

        });

        travel_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String query = "select status from projectStatus where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "'";
                    int current_status = VideoCallDataBase.getDB(context).getCurrentStatus(query);
                    if (current_status == -1 || current_status == 8) {
                        //                    travel_job.setEnabled(false);
                        Toast.makeText(TravelJobDetails.this, "The Task has not yet started...", Toast.LENGTH_SHORT).show();
                    } else {
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Log.i("Location", "Latitude ## ==> " + loc.getLatitude() + " Longitude " + loc.getLongitude());
                            travel_job.setEnabled(true);
                            showtravelTimePopup(v);
                        } else {
                            showSettingsAlert("Unable to get current location. Change permissions ");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("TravelJobDetails", "travel_job click Exception : " + e.getMessage(), "WARN", null);
                }
            }

        });
        Project_backgroundProcess();
    }

    public void Project_backgroundProcess() {
        Log.i(tab, "Project_backgroundProcess # ");
        Log.i(tab, "taskList 3 " + taskList.size());
        try {
            if (webtaskId != null && !webtaskId.equalsIgnoreCase("")) {
                Log.d(tab, "Updated Task Read status");
                VideoCallDataBase.getDB(context).updateprojectMsgReadStatus(webtaskId);
            }
            String query_1 = "select * from taskDetailsInfo where (loginuser='" + Appreference.loginuserdetails.getEmail() + "') and (taskStatus!='note' and taskStatus!='draft') and (taskId='" + webtaskId + "') and (projectId='" + projectId + "') and customTagVisible = '1';";
            taskList.clear();
            if (VideoCallDataBase.getDB(context).getTaskHistory(query_1) != null) {
                Log.d(tab, "PJ_BG TASK HISTORY NOT NULL");
                taskList = VideoCallDataBase.getDB(context).getTaskHistory(query_1);
                sortTaskMessage();
                Log.d(tab, "TASK HISTORY List size  = " + taskList.size());
                Log.d(tab, "ownerOfTask " + ownerOfTask);
                Log.d(tab, "taskName " + taskName);
                Log.d(tab, "taskReceiver " + taskReceiver);
                Log.d(tab, "isgettask " + isgettask);
                /*if (taskList.size() > 0) {
                    for (TaskDetailsBean taskBean : taskList) {
                        taskBean.setOwnerOfTask(ownerOfTask);
                        taskBean.setTaskReceiver(taskReceiver);
                        taskBean.setTaskName(taskName);
                    }
                }*/

                if (!isgettask) {
                    try {
                        medialistadapter = new MediaListAdapter(TravelJobDetails.this, taskList, "task", category, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("TravelJobDetails", "Project_backgroundProcess() notify  Exception : " + e.getMessage(), "WARN", null);
                    }
                }
            }

            list_all.setAdapter(medialistadapter);
            refresh();
            Log.i(tab, "taskList 4 " + taskList.size());
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "Project_backgroundProcess() Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void projectBackgroundProcess() {
        Log.i(tab, "getTask projectBackgroundProcess ");
        try {
            taskList.clear();
            String query_1 = "select * from taskDetailsInfo where (loginuser='" + Appreference.loginuserdetails.getEmail() + "') and (taskStatus!='note' and taskStatus!='draft') and (taskId='" + webtaskId + "') and (projectId='" + projectId + "') and customTagVisible = '1';";

            if (VideoCallDataBase.getDB(context).getTaskHistory(query_1) != null) {
                taskList = VideoCallDataBase.getDB(context).getTaskHistory(query_1);
                sortTaskMessage();
//                if (taskList.size() > 0) {
//                    for (TaskDetailsBean taskBean : taskList) {
//                        taskBean.setOwnerOfTask(ownerOfTask);
//                        taskBean.setTaskReceiver(taskReceiver);
//                        taskBean.setTaskName(taskName);
//                    }
//                }
            }
            Log.i(tab, "medialistadapter ==> " + medialistadapter);

            try {
                medialistadapter = new MediaListAdapter(TravelJobDetails.this, taskList, "task", category, null);
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("TravelJobDetails", "Project_backgroundProcess() notify  Exception : " + e.getMessage(), "WARN", null);
            }
            list_all.setAdapter(medialistadapter);
            refresh();
            cancelDialog();
            Log.i(tab, "taskList 2 ** " + taskList.size());
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "projectBackgroundProcess() Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void notifyTaskReceived(final TaskDetailsBean taskDetailsBean) {
        try {
            Log.i(tab, "notifyTaskReceived 0 ");
            Log.i(tab, "notifyTaskReceived " + taskStatus);
            // signal id check in this method for db insertion
            dbInsertCheckSignalId(taskDetailsBean);
            if (taskDetailsBean.getTaskId().equalsIgnoreCase(webtaskId)) {
                Log.i(tab, "notifyTaskReceived 1 " + taskDetailsBean.getTaskId());
                if (taskDetailsBean.getMimeType() != null && taskDetailsBean.getMimeType().equalsIgnoreCase("assigntask")) {
                    try {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                /*code Added for groupAdmin-observer Start*/

                                if (oracleProjectOwner != null && !oracleProjectOwner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())
                                        && (Appreference.loginuserdetails != null && Appreference.loginuserdetails.getRoleId() != null
                                        && !Appreference.loginuserdetails.getRoleId().equalsIgnoreCase("2"))) {
                                    /*code Added for groupAdmin-observer End*/
                                    ll_2.setVisibility(View.GONE);
                                    status_job.setVisibility(View.VISIBLE);
                                    travel_job.setVisibility(View.VISIBLE);
                                } else {
                                    ll_2.setVisibility(View.GONE);
                                    status_job.setVisibility(View.VISIBLE);
                                    travel_job.setVisibility(View.GONE);
                                }
                                taskType = taskDetailsBean.getTaskType();
                                Log.i("observer", "taskType ==> " + taskDetailsBean.getTaskType());
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("TravelJobDetails", "notifyTaskReceived assignTask Exception : " + e.getMessage(), "WARN", null);
                    }
                } else if (taskDetailsBean.getSubType() != null && taskDetailsBean.getSubType().equalsIgnoreCase("deassign")) {
                    try {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (Appreference.loginuserdetails != null && Appreference.loginuserdetails.getRoleId() != null
                                        && Appreference.loginuserdetails.getRoleId().equalsIgnoreCase("2")
                                        && !oracleProjectOwner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    ll_2.setVisibility(View.GONE);
                                    tv_reassign.setVisibility(View.GONE);
                                } else {
                                    String taskMemberList_qry = "select taskMemberList from projectHistory where projectId='" + projectId + "' and taskId='" + webtaskId + "'";
                                    String taskMemberList_1 = VideoCallDataBase.getDB(context).getValuesForQuery(taskMemberList_qry);
                                    Log.i("assignTask", "taskMemberList_1==> " + taskMemberList_1);

                                    if (taskMemberList_1 != null && !taskMemberList_1.equalsIgnoreCase("") && !taskMemberList_1.equalsIgnoreCase(null)) {
                                        tv_reassign.setVisibility(View.GONE);
                                        ll_2.setVisibility(View.GONE);
                                    } else {
                                        ll_2.setVisibility(View.VISIBLE);
                                        tv_reassign.setVisibility(View.VISIBLE);
                                        if (taskDetailsBean.getOwnerOfTask() != null && taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                            tv_reassign.setText("Assign Task");
                                        } else {
                                            tv_reassign.setText("Assign to me");
                                            status_job.setVisibility(View.GONE);
                                            travel_job.setVisibility(View.GONE);
                                        }
                                    }
                                }

                                listOfObservers.clear();
                                String project_deassignMems = remove_TaskMembers(taskDetailsBean.getFromUserName());
                                Log.i("userlist", "project_deassignMems " + project_deassignMems);
                                if (project_deassignMems != null && !project_deassignMems.equalsIgnoreCase("") && !project_deassignMems.equalsIgnoreCase(null)) {
                                    if (!listOfObservers.contains(project_deassignMems)) {
                                        listOfObservers.add(project_deassignMems);
                                    }
                                }
                                Log.i("userlist", "------listOfObservers $$$ ==> " + listOfObservers);
                                VideoCallDataBase.getDB(context).updateaccept("update projectHistory set taskMemberList='" + project_deassignMems + "' where projectId='" + taskDetailsBean.getProjectId() + "' and taskId='" + taskDetailsBean.getTaskId() + "'");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("TravelJobDetails", "notifyTaskReceived deassign Exception : " + e.getMessage(), "WARN", null);
                    }
                } else if (taskDetailsBean.getTaskStatus() != null && (taskDetailsBean.getTaskStatus().equalsIgnoreCase("Completed") || taskDetailsBean.getTaskStatus().equalsIgnoreCase("Complete"))) {
                    try {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ll_2.setVisibility(View.GONE);
                                status_job.setVisibility(View.GONE);
                                travel_job.setVisibility(View.GONE);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("TravelJobDetails", "notifyTaskReceived complete Exception : " + e.getMessage(), "WARN", null);
                    }
                }
                if (taskDetailsBean.getTaskStatus() != null && !taskDetailsBean.getTaskStatus().equalsIgnoreCase("") && !taskDetailsBean.getTaskStatus().equalsIgnoreCase("null") && !taskDetailsBean.getTaskStatus().equalsIgnoreCase(null)) {
                    taskStatus = taskDetailsBean.getTaskStatus();
                }
                Log.i("status", "status*** 1 " + taskDetailsBean.getTaskStatus());
                VideoCallDataBase.getDB(context).update_Project_history(taskDetailsBean);
                VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                if ((taskDetailsBean.getProjectStatus() != null && !taskDetailsBean.getProjectStatus().equalsIgnoreCase("")
                        && !taskDetailsBean.getProjectStatus().equalsIgnoreCase("null") && !taskDetailsBean.getProjectStatus().equalsIgnoreCase(null)
                        && taskDetailsBean.getProjectStatus().equalsIgnoreCase("9"))
                        && taskDetailsBean.getTravelEndTime() != null && !taskDetailsBean.getTravelEndTime().equalsIgnoreCase("")
                        && !taskDetailsBean.getTravelEndTime().equalsIgnoreCase(null)) {
                    Log.i(tab, "projectUpdate query if # ");
                    String queryUpdate = "update projectStatus set travelEndTime='" + taskDetailsBean.getTravelEndTime() + "' where projectId='" + projectId + "' and taskId= '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";
                    Log.i(tab, "projectUpdate query " + queryUpdate);
                    VideoCallDataBase.getDB(context).updateaccept(queryUpdate);
                } else {
                    Log.i(tab, "projectUpdate query else # ");
                    VideoCallDataBase.getDB(context).insertORupdateStatus(taskDetailsBean);
                }
                taskList.add(taskDetailsBean);
                sortTaskMessage();
                refresh();
                listLastposition();
            } else {
                Log.i(tab, "notifyTaskReceived 2 " + webtaskId);
                if (!isSignalidSame) {
                    try {
                        VideoCallDataBase.getDB(context).update_Project_history(taskDetailsBean);
                        VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                        if ((taskDetailsBean.getProjectStatus() != null && !taskDetailsBean.getProjectStatus().equalsIgnoreCase("")
                                && !taskDetailsBean.getProjectStatus().equalsIgnoreCase("null") && !taskDetailsBean.getProjectStatus().equalsIgnoreCase(null)
                                && taskDetailsBean.getProjectStatus().equalsIgnoreCase("9"))
                                && taskDetailsBean.getTravelEndTime() != null && !taskDetailsBean.getTravelEndTime().equalsIgnoreCase("")
                                && !taskDetailsBean.getTravelEndTime().equalsIgnoreCase(null)) {
                            String queryUpdate = "update projectStatus set travelEndTime='" + taskDetailsBean.getTravelEndTime() + "' where projectId='" + projectId + "' and taskId= '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";
                            Log.i(tab, "projectUpdate query else " + queryUpdate);
                            Log.i(tab, "projectUpdate query if * ");
                            VideoCallDataBase.getDB(context).updateaccept(queryUpdate);
                        } else {
                            Log.i(tab, "projectUpdate query else * ");
                            VideoCallDataBase.getDB(context).insertORupdateStatus(taskDetailsBean);
                        }
                        Log.i(tab, "notifyTaskReceived 3 " + taskDetailsBean.getTaskStatus());
                        Log.i("status", "status*** 1 else " + taskDetailsBean.getTaskStatus());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("TravelJobDetails", "notifyTaskReceived else Exception : " + e.getMessage(), "WARN", null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "notifyTaskReceived Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void dbInsertCheckSignalId(TaskDetailsBean taskDetailsBean) {
        try {
            String query = "select * from taskDetailsInfo where  (loginuser='" + Appreference.loginuserdetails.getEmail() + "') and (taskId='" + taskDetailsBean.getTaskId() + "');";
            ArrayList<TaskDetailsBean> beanArrayList = VideoCallDataBase.getDB(context).getTaskHistory(query);
            for (TaskDetailsBean taskDetailsBean1 : beanArrayList) {
                if (taskDetailsBean1.getSignalid().equalsIgnoreCase(taskDetailsBean.getSignalid())) {
                    isSignalidSame = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "dbInsertCheckSignalId Exception : " + e.getMessage(), "WARN", null);
        }
    }


    private void showtravelTimePopup(View v) {
        try {
            Intent intent = new Intent(TravelJobDetails.this, CustomTravelPickerActivity.class);
            intent.putExtra("taskName", taskName);
            intent.putExtra("projectID", projectId);
            intent.putExtra("isTravel", true);
            intent.putExtra("taskID", webtaskId);
            intent.putExtra("jobcodeno", JobCodeNo);
            intent.putExtra("activitycode", ActivityCode);
            startActivityForResult(intent, 120);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "showtravelTimePopup Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void getEnteredTravelTime(String startTime, String EndTime, String status) {
        try {
            Log.i(tab, "inside getEnteredTravelTime ========>" + startTime + "==>" + EndTime);

            ActivityStartdate = startTime;
            ActivityEnddate = EndTime;
            getLocationForTravel();
            if (latitude_global == 0.0 && longitude_global == 0.0) {
                Appreference.printLog("TravelJobDetails", "getlanlat Location " + latitude_global + "," + longitude_global, "WARN", null);
                showSettingsAlert("Unable to get current location. Change permissions again");
            } else {
                if (startTime != null && !startTime.equalsIgnoreCase(""))
                    sendStatus_webservice(status, "", "", "", "");
                else
                    sendStatus_webservice(status, "", "", "", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "getEnteredTravelTime Exception : " + e.getMessage(), "WARN", null);
        }

    }

    private void showStatusPopupWindow(View view) {
        try {
            final PopupMenu popup = new PopupMenu(TravelJobDetails.this, view);
            try {
                Field[] fields = popup.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("mPopup".equals(field.getName())) {
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popup);
                        Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                        Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                        setForceIcons.invoke(menuPopupHelper, true);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("TravelJobDetails", "showStatusPopupWindow popup  Exception : " + e.getMessage(), "WARN", null);
            }
            popup.getMenuInflater().inflate(R.menu.popup_job_status, popup.getMenu());
            popup.getMenu().getItem(1).setVisible(false);

            String query = "select status from projectStatus where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "'";
            int current_status = VideoCallDataBase.getDB(context).getCurrentStatus(query);
            if (current_status == 7 || current_status == 9) {
                String status_info = "select status from projectStatus where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and status!='7' and status!= '9'order by id DESC";
                ArrayList<String> status_all = VideoCallDataBase.getDB(context).getAllCurrentStatus(status_info);
                Log.i("output123", "project CurrentStatus from DB====>" + status_all.size());
                if (status_all.size() > 0) {
                    current_status = Integer.parseInt(status_all.get(0));
                    Log.i("output123", "project CurrentStatus from current_status " + current_status);
                }
            }
            /*added for GroupAdmin Observer Start*/
            String get_groupAdminobserver_query = "select groupAdminobserver from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'and projectId='" + projectId + "'";
            String OraclegroupAdminObserver = VideoCallDataBase.getDB(context).getprojectIdForOracleID(get_groupAdminobserver_query);
            /*added for GroupAdmin Observer End*/
            Log.i(tab, "project CurrentStatus from DB====>" + current_status);
            if (oracleProjectOwner != null && !oracleProjectOwner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) &&
                    (OraclegroupAdminObserver != null && !OraclegroupAdminObserver.contains(Appreference.loginuserdetails.getUsername()))) {
                if (current_status == -1) {
                    popup.getMenu().getItem(0).setVisible(true);
                    popup.getMenu().getItem(1).setVisible(false);
                    popup.getMenu().getItem(2).setVisible(false);
                    popup.getMenu().getItem(3).setVisible(false);
                    popup.getMenu().getItem(4).setVisible(false);
                    popup.getMenu().getItem(5).setVisible(false);
                    popup.getMenu().getItem(6).setVisible(true);
                    popup.getMenu().getItem(7).setVisible(false);
                }
//        popup.getMenu().getItem(6).setVisible(true);
                if (current_status == 0) {
                    //            Start_work = true;
                    popup.getMenu().getItem(0).setVisible(false);
                    popup.getMenu().getItem(1).setVisible(false);
                    popup.getMenu().getItem(2).setVisible(false);
                    popup.getMenu().getItem(3).setVisible(false);
                    popup.getMenu().getItem(4).setVisible(false);
                    popup.getMenu().getItem(5).setVisible(false);
                    popup.getMenu().getItem(6).setVisible(true);
                    popup.getMenu().getItem(7).setVisible(false);
                } else if (current_status == 5) {
                    completed_work = true;
                    popup.getMenu().getItem(0).setVisible(false);
                    popup.getMenu().getItem(1).setVisible(false);
                    popup.getMenu().getItem(2).setVisible(false);
                    popup.getMenu().getItem(3).setVisible(false);
                    popup.getMenu().getItem(4).setVisible(false);
                    popup.getMenu().getItem(5).setVisible(false);
                    popup.getMenu().getItem(6).setVisible(false);
                    popup.getMenu().getItem(7).setVisible(false);
                    Toast.makeText(getApplicationContext(), "Task has been Completed", Toast.LENGTH_SHORT).show();
                } else if (current_status == 7) {
                    popup.getMenu().getItem(0).setVisible(false);
                    popup.getMenu().getItem(1).setVisible(false);
                    popup.getMenu().getItem(2).setVisible(false);
                    popup.getMenu().getItem(3).setVisible(false);
                    popup.getMenu().getItem(4).setVisible(false);
                    popup.getMenu().getItem(5).setVisible(false);
                    popup.getMenu().getItem(6).setVisible(true);
                    popup.getMenu().getItem(7).setVisible(false);
                } else if (current_status == 8) {
                    popup.getMenu().getItem(0).setVisible(true);
                    popup.getMenu().getItem(1).setVisible(false);
                    popup.getMenu().getItem(2).setVisible(false);
                    popup.getMenu().getItem(3).setVisible(false);
                    popup.getMenu().getItem(4).setVisible(false);
                    popup.getMenu().getItem(5).setVisible(false);
                    popup.getMenu().getItem(6).setVisible(true);
                    popup.getMenu().getItem(7).setVisible(false);
                }
            } else {
                popup.getMenu().getItem(0).setVisible(false);
                popup.getMenu().getItem(1).setVisible(false);
                popup.getMenu().getItem(2).setVisible(false);
                popup.getMenu().getItem(3).setVisible(false);
                popup.getMenu().getItem(4).setVisible(false);
                popup.getMenu().getItem(5).setVisible(false);
                popup.getMenu().getItem(6).setVisible(false);
                if (current_status != 5) {
                    popup.getMenu().getItem(7).setVisible(true);
                } else {
                    popup.getMenu().getItem(7).setVisible(false);
                }
            }

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getTitle().toString().equalsIgnoreCase("Start")) {
                        String timerToSet = getEstimatedTime_to_set(true);
                        sendStatus_webservice("0", timerToSet, "", "Started", "Started");
                    }
                    if (item.getTitle().toString().equalsIgnoreCase("Complete")) {
                        try {
                            Log.i("ws123", "completed $$--> " + taskType);
                            if (taskType != null && taskType.equalsIgnoreCase("group")) {
                                getgroupStatus();
                            } else {
                                String query_status = "select * from projectStatus where projectId='" + projectId + "'  and taskId= '" + webtaskId + "' and status = '0'";
                                final int count_status = VideoCallDataBase.getDB(context).getCountForTravelEntry(query_status);
                                if (count_status != 0) {
                                    final int travelentry = VideoCallDataBase.getDB(context).CheckTravelEntryDetails("select * from projectStatus where projectId ='" + projectId + "' and taskId = '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL");
                                    Log.i("conv123", "TravelEntry==>" + travelentry);
                                    String query = "select * from projectStatus where projectId='" + projectId + "'  and taskId= '" + webtaskId + "' and status = '7'";
                                    final int count = VideoCallDataBase.getDB(context).getCountForTravelEntry(query);
                                    if (count != 0) {
                                        if (travelentry == 0) {
                                            showAlertDialog("Complete Task", "Are You sure want to complete this job " + taskName, context);
                                        } else {
                                            Toast.makeText(TravelJobDetails.this, "Enter end date and time then proceed to complete the task.", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(TravelJobDetails.this, "No StartEndTime Found", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(TravelJobDetails.this, "can't able to complete this task", Toast.LENGTH_SHORT).show();
                                }
                            }
                            Log.i("ws123", "OracleStatusList $$--> " + OracleStatusList.size());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("TravelJobDetails", "showStatusPopupWindow complete click Exception : " + e.getMessage(), "WARN", null);
                        }
                    }
                    if (item.getTitle().toString().equalsIgnoreCase("DeAssign")) {
                        try {
                            if (isNetworkAvailable()) {
                                int travelentry = VideoCallDataBase.getDB(context).CheckTravelEntryDetails("select * from projectStatus where projectId ='" + projectId + "' and taskId = '" + webtaskId + "'and userId='" + Appreference.loginuserdetails.getId() + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL");

                                if (travelentry == 0) {
                                    final Dialog dialog1 = new Dialog(context);
                                    dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog1.setContentView(R.layout.task_remarks);
                                    dialog1.setCanceledOnTouchOutside(false);
                                    TextView header = (TextView) dialog1.findViewById(R.id.template_header);
                                    TextView yes = (TextView) dialog1.findViewById(R.id.save);
                                    TextView no = (TextView) dialog1.findViewById(R.id.no);
                                    final EditText name = (EditText) dialog1.findViewById(R.id.remarks);
                                    header.setText("Enter Remarks ");
                                    yes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            try {
                                                Log.i(tab, "remarks for DeAssign====>" + name.getText().toString());
                                                if (name.getText().toString() != null && !name.getText().toString().equalsIgnoreCase("")) {
                                                    dialog1.dismiss();
                                                    if (Appreference.isTravelRem_time && estimTravelCounter != null) {
                                                        estimTravelCounter.cancel();
                                                    }
                                                    String EstimatedTimeQuery = "update taskDetailsInfo set estimCompletion='1' where (taskDescription='Task is Started') and projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
                                                    Log.i("tone123", "updateSnoozeTime_query***********" + EstimatedTimeQuery);
                                                    VideoCallDataBase.getDB(context).updateaccept(EstimatedTimeQuery);
                                                    EstimTimerstop();
                                                    sendStatus_webservice("8", "", "DeAssign Remarks : " + name.getText().toString(), "DeAssign", "");
                                                } else
                                                    Toast.makeText(TravelJobDetails.this, "Please enter any Remarks", Toast.LENGTH_SHORT).show();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Appreference.printLog("TravelJobDetails", "showStatusPopupWindow DeAssign submit Exception : " + e.getMessage(), "WARN", null);
                                            }
                                        }
                                    });
                                    no.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            try {
                                                dialog1.dismiss();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Appreference.printLog("TravelJobDetails", "showStatusPopupWindow DeAssign dismiss Exception : " + e.getMessage(), "WARN", null);
                                            }
                                        }
                                    });
                                    dialog1.show();
                                } else {
                                    Toast.makeText(TravelJobDetails.this, "Enter End DateTime to Deassign the task.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(TravelJobDetails.this, "Not available when no internet connection", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("TravelJobDetails", "showStatusPopupWindow DeAssign Exception : " + e.getMessage(), "WARN", null);
                        }
                    }
                    return true;
                }
            });
            popup.show();
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "showStatusPopupWindow Exception : " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "showStatusPopupWindow Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void showSettingsAlert(String message) {
        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(TravelJobDetails.this);
            alertDialog.setTitle("Gps setting");
            alertDialog.setMessage(message);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent in = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(in);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getLocationForTravel() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            for (int i = 0; i < 3; i++) {
                Log.d("FireGps", "getLocationForTravel location : " + loc);
                Log.d("FireGps", "Loop ==> " + i);
                loc.getLocation();
                latitude_global = loc.getLatitude();
                longitude_global = loc.getLongitude();
                Log.i("FireGps", "Traveljobdetails latitude $ ==> " + latitude_global);
                if (latitude_global == 0.0 && !back_preesed) {
                    Log.i("Location", "user ");
                } else {
                    break;
                }
            }
        }
    }

    private String getEstimatedTime_to_set(boolean isTravelStartNow) {
        String timer = "";
        try {
            int timer_hour = 0, timer_minutes = 0;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateandTime = sdf.format(new Date());
            Log.i("estim123", "Calculated Timer details===>currentDateandTime*****" + currentDateandTime);

            Date date = null;
            try {
                date = formatter.parse(currentDateandTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (isTravelStartNow) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                String estim_timeQuery = "Select * from projectHistory where projectId ='" + projectId + "' and taskId = '" + webtaskId + "'";
                String timeToAdd = VideoCallDataBase.getDB(context).getEstimHoursForTaskId(estim_timeQuery);
                Log.i("estim123", "estimated Time added===>*****" + timeToAdd);

                if (timeToAdd != null && !timeToAdd.equalsIgnoreCase("0") && !timeToAdd.equalsIgnoreCase("0.0")) {
//                            timeToAdd= String.valueOf(0.10);
                    double d_timeToAdd = Double.parseDouble(timeToAdd);

                    Log.i("checktime123", "estimated Time timeToAdd===>*****" + timeToAdd);

                    if (timeToAdd != null && !(d_timeToAdd == (Math.ceil(d_timeToAdd)))) {
                        //            String timer_result[] = timeToAdd.split("\\.");
                        double d_minutes = d_timeToAdd % 1;
                        int i_hour = (int) d_timeToAdd;
                        Log.i("checktime123", "Integer.parseInt(timer_result[0])===>*****" + i_hour);
                        Log.i("checktime123", "(Integer.parseInt(timer_result[1])======" + d_minutes);
                        Log.i("checktime123", "(Integer.parseInt(timer_result[1]*60)======" + d_minutes * 60);

                        try {
                            timer_hour = i_hour;
                            timer_minutes = (int) (d_minutes * 60);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        calendar.add(Calendar.HOUR, timer_hour);
                        calendar.add(Calendar.MINUTE, timer_minutes);
                    } else {
                        try {
                            calendar.add(Calendar.HOUR, Integer.parseInt(timeToAdd));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    final SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    timer = mdformat.format(calendar.getTime());
                    Log.i("estim123", "Calculated Timer details===>timer******" + timer);
                }
            } else {
                try {
                    String taskQueryRemainingTime = "select estimRemainingTime from taskDetailsInfo where (taskDescription='Task is Started') and projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
                    String getLastEndTime = VideoCallDataBase.getDB(context).getValuefromDBEntry(taskQueryRemainingTime, "estimRemainingTime");
                    String taskQuery = "select estimTime from taskDetailsInfo where (taskDescription='Task is Started') and projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
                    String getEstimatedTimer = VideoCallDataBase.getDB(context).getValuefromDBEntry(taskQuery, "estimTime");
                    Log.i("remain123", " getEstimatedTime_to_set getEstimatedTime========>" + getEstimatedTimer);
                    Log.i("remain123", " getEstimatedTime_to_set getLastEndTime FROM DB========>" + getLastEndTime);

                    if (getLastEndTime != null) {
                        String dateTime_fromDB[] = getLastEndTime.split(" ");
                        Date datefromDB = null;
                        Log.i("remain123", "getEstimatedTime_to_set split Time From DB dateTime_fromDB[0]*****" + dateTime_fromDB[0]);
                        Log.i("remain123", "getEstimatedTime_to_set split Time From DB dateTime_fromDB[1]*****" + dateTime_fromDB[1]);


                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);

                        String timeToAdd = null;
                        if (dateTime_fromDB != null) {
                            timeToAdd = dateTime_fromDB[1];
                        }

                        if (timeToAdd != null && !timeToAdd.equalsIgnoreCase("")) {
                            String HMS_fromDB[] = timeToAdd.split(":");
                            if (HMS_fromDB != null) {
                                int hour_DB = Integer.parseInt(HMS_fromDB[0]);
                                int minutes_DB = Integer.parseInt(HMS_fromDB[1]);
                                int seconds_DB = Integer.parseInt(HMS_fromDB[2]);
                                calendar.add(Calendar.HOUR, hour_DB);
                                calendar.add(Calendar.MINUTE, minutes_DB);
                                calendar.add(Calendar.SECOND, seconds_DB);


                              /*  calendar.add(Calendar.HOUR, 0);
                                calendar.add(Calendar.MINUTE, 4);
                                calendar.add(Calendar.SECOND, seconds_DB);*/
                                final SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                timer = mdformat.format(calendar.getTime());
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "getEstimatedTime_to_set Traveltask Exception : " + e.getMessage(), "WARN", null);
        }
        return timer;
    }

    public void sendStatus_webservice(String status, String timer, String remarks, String projectCurrentStatus, String statusUI) {
        try {
            if (isNetworkAvailable()) {
                showStatusprogress("Sending status...");
            } else {
                Toast.makeText(TravelJobDetails.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
            ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
            if (projectHistory != null) {
                try {
                    Log.i(tab, "inside status projectHistory ========>" + projectHistory.projectDetailsBeans + "size bean " + projectHistory.projectDetailsBeans.size() + "buddayArrayAdapteer==>" + projectHistory.buddyArrayAdapter);

                    if (projectHistory.projectDetailsBeans != null && projectHistory.projectDetailsBeans.size() > 0 && projectHistory.buddyArrayAdapter != null) {

                        ProjectDetailsBean projectDetailsBean = projectHistory.projectDetailsBeans.get(clickPosition);
                        if (projectCurrentStatus.equalsIgnoreCase("DeAssign")) {
                            projectDetailsBean.setTaskStatus("Unassigned");
                            projectDetailsBean.setTaskReceiver("NA");

                        } else if (!projectCurrentStatus.equalsIgnoreCase("travel"))
                            projectDetailsBean.setTaskStatus(statusUI);

                        Log.i(tab, "inside status NewTaskConveratio ========>" + projectCurrentStatus);
                        projectHistory.buddyArrayAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("TravelJobDetails", "sendStatus_webservice projectHistory.buddyArrayAdapter Exception : " + e.getMessage(), "WARN", null);
                }
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat dateParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = dateFormat.format(new Date());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateforrow = dateFormat.format(new Date());
            tasktime = dateTime;
            taskUTCtime = dateforrow;
            travel_date_details = null;
            tasktime = tasktime.split(" ")[1];
            Date date = null;
            Date date1 = null;
            String StartDateUTC = "", EndDateUTC = "";
            if (ActivityStartdate != null && !ActivityStartdate.equalsIgnoreCase("")) {
                try {
                    date = dateParse.parse(ActivityStartdate);
                    StartDateUTC = dateFormat.format(date);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("TravelJobDetails", "sendStatus_webservice ActivityStartdate Exception : " + e.getMessage(), "WARN", null);
                }
            }
            if (ActivityEnddate != null && !ActivityEnddate.equalsIgnoreCase("")) {
                try {
                    date1 = dateParse.parse(ActivityEnddate);
                    EndDateUTC = dateFormat.format(date1);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("TravelJobDetails", "sendStatus_webservice ActivityEnddate Exception : " + e.getMessage(), "WARN", null);
                }
            }
            ArrayList<TaskDetailsBean> status_list = new ArrayList<>();
            TaskDetailsBean taskDetailsBean = new TaskDetailsBean();

            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("id", Integer.parseInt(projectId));
            taskDetailsBean.setProjectId(projectId);
            jsonObject.put("project", jsonObject1);

            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("id", Appreference.loginuserdetails.getId());
            taskDetailsBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
            jsonObject.put("from", jsonObject2);

            JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("id", Integer.parseInt(webtaskId));
            taskDetailsBean.setTaskId(webtaskId);
            jsonObject.put("task", jsonObject3);


            if (status.equalsIgnoreCase("7") || status.equalsIgnoreCase("9")) {
                try {
                    if (ActivityStartdate != null && !ActivityStartdate.equalsIgnoreCase(""))
                        if (!status.equalsIgnoreCase("9")) {
                            Log.i("location", "status @@ " + status);
                            jsonObject.put("travelStartTime", StartDateUTC);
                            jsonObject.put("startDateLatitude", latitude_global);
                            jsonObject.put("startDateLongitude", longitude_global);
                            taskDetailsBean.setStartDateLatitude(String.valueOf(latitude_global));
                            taskDetailsBean.setStartDateLongitude(String.valueOf(longitude_global));
                        } else {
                            Log.i("location", "status @@@ " + status);
                            jsonObject.put("travelStartTime", "");
                            jsonObject.put("startDateLatitude", "");
                            jsonObject.put("startDateLongitude", "");
                        }

                    if (ActivityEnddate != null && !ActivityEnddate.equalsIgnoreCase("")) {
                        Log.i("location", "status @@@@ " + status);
                        jsonObject.put("travelEndTime", EndDateUTC);
                        jsonObject.put("endDateLatitude", latitude_global);
                        jsonObject.put("endDateLongitude", longitude_global);
                        taskDetailsBean.setEndDateLatitude(String.valueOf(latitude_global));
                        taskDetailsBean.setEndDateLongitude(String.valueOf(longitude_global));
                    } else {
                        Log.i("location", "status @@@@@ " + status);
                        jsonObject.put("travelEndTime", "");
                        jsonObject.put("endDateLatitude", "");
                        jsonObject.put("endDateLongitude", "");
                    }


                    if (!isNetworkAvailable()) {
                        taskDetailsBean.setEnd_dateStatus("7");
                    }
                    travel_date_details = new ArrayList<>();
                    if (ActivityStartdate != null && !ActivityStartdate.equalsIgnoreCase("") && !status.equalsIgnoreCase("9")) {
                        travel_date_details.add("StartTime : " + ActivityStartdate);
                        Log.i("location", "status @@@@@ " + taskDetailsBean.getStartDateLatitude());
                        travel_date_details.add("Latitude : " + taskDetailsBean.getStartDateLatitude());
                        travel_date_details.add("Longitude : " + taskDetailsBean.getStartDateLongitude());
                        taskDetailsBean.setTravelStartTime(ActivityStartdate);
                    }
                    if (ActivityEnddate != null && !ActivityEnddate.equalsIgnoreCase("")) {
                        travel_date_details.add("EndTime : " + ActivityEnddate);
                        Log.i("location", "status @@@@@ " + taskDetailsBean.getEndDateLatitude());
                        travel_date_details.add("Latitude : " + taskDetailsBean.getEndDateLatitude());
                        travel_date_details.add("Longitude : " + taskDetailsBean.getEndDateLongitude());
                        taskDetailsBean.setTravelEndTime(ActivityEnddate);
                    }

                    if (status.equalsIgnoreCase("7")) {
                        String restartTimer = getEstimatedTime_to_set(false);
                        Log.i("remain123", "Newtask conv restart restartTimer value***********" + restartTimer);

                        if (restartTimer != null && !restartTimer.equalsIgnoreCase("")) {


                              /*update query : set alarm toned*/
                            String AlarmRingedUpdateQuery = "update taskDetailsInfo set EstimAlarm='1' where projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
                            Log.i("estimtone123", "updateSnoozeTime_query***********" + AlarmRingedUpdateQuery);
                            VideoCallDataBase.getDB(context).updateaccept(AlarmRingedUpdateQuery);

                /*update Query : set Task completed before the EstimatedHours*/
                            String EstimatedTimeQuery = "update taskDetailsInfo set estimCompletion='0' where (taskDescription='Task is Started') and projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
                            Log.i("tone123", "updateSnoozeTime_query***********" + EstimatedTimeQuery);
                            VideoCallDataBase.getDB(context).updateaccept(EstimatedTimeQuery);


                            String EstimTimeQuery = "update taskDetailsInfo set estimTime='" + restartTimer + "' where (taskDescription='Task is Started') and projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
                            Log.i("remain123", "estimTime restart time set query***********" + EstimatedTimeQuery);
                            VideoCallDataBase.getDB(context).updateaccept(EstimTimeQuery);

                            String taskQuery = "select estimTime from taskDetailsInfo where (taskDescription='Task is Started') and projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
                            String getEstimatedTimer = VideoCallDataBase.getDB(context).getValuefromDBEntry(taskQuery, "estimTime");
                            Log.i("remain123", "estimTime restart EnteredValue***********" + getEstimatedTimer);
                            Appreference.Estim_travel_TimerValue = restartTimer;
                          /*  startHoldOrPauseAlarmManager(restartTimer, webtaskId, projectCurrentStatus, taskDetailsBean.getProjectId(), "EstimTimer");
                            ShowEstimTimerDisplay(false);*/
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("TravelJobDetails", "sendStatus_webservice travel_date_details Exception : " + e.getMessage(), "WARN", null);
                }
            } else {
                jsonObject.put("travelStartTime", "");
                jsonObject.put("activityStartTime", "");
                jsonObject.put("activityEndTime", "");
                jsonObject.put("travelEndTime", "");
                jsonObject.put("toTravelStartDateTime", "");
                jsonObject.put("toTravelEndDateTime", "");
            }
            if (remarks != null && !remarks.equalsIgnoreCase("")) {
                jsonObject.put("remarks", remarks);
                taskDetailsBean.setCustomerRemarks(remarks);
            } else
                jsonObject.put("remarks", "");
            jsonObject.put("status", status);

            taskDetailsBean.setProjectStatus(status);
            taskDetailsBean.setTaskUTCDateTime(dateforrow);
            taskDetailsBean.setDateTime(dateTime);
            taskDetailsBean.setTasktime(tasktime);
            taskDetailsBean.setTaskUTCTime(taskUTCtime);
            taskDetailsBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
            taskDetailsBean.setFromUserName(Appreference.loginuserdetails.getUsername());

            taskDetailsBean.setToUserId("");
            taskDetailsBean.setToUserName("");
            taskDetailsBean.setTaskReceiver(taskReceiver);

            taskDetailsBean.setSignalid(Utility.getSessionID());
            taskDetailsBean.setTaskNo(task_No);
            taskDetailsBean.setPlannedStartDateTime("");
            taskDetailsBean.setPlannedEndDateTime("");
            taskDetailsBean.setSendStatus("0");
            taskDetailsBean.setTaskType(taskType);
            taskDetailsBean.setMimeType("text");
            if (status != null && !status.equalsIgnoreCase("7") && !status.equalsIgnoreCase("9")) {
                taskStatus = statusUI;
            }
            taskDetailsBean.setUtcPlannedStartDateTime(Appreference.customLocalDateToUTC(null));
            taskDetailsBean.setUtcplannedEndDateTime(Appreference.customLocalDateToUTC(null));
            taskDetailsBean.setParentId(getFileName());
            taskDetailsBean.setTaskPriority("Medium");
            taskDetailsBean.setCompletedPercentage("");
            taskDetailsBean.setRequestStatus("requested");
            taskDetailsBean.setMsg_status(0);
            taskDetailsBean.setShow_progress(1);

            taskDetailsBean.setOwnerOfTask(ownerOfTask);
            taskDetailsBean.setTaskReceiver(taskReceiver);
            taskDetailsBean.setTaskName(taskName);
            taskDetailsBean.setCatagory(category);
            taskDetailsBean.setProjectId(projectId);
            taskDetailsBean.setParentTaskId(parentTaskId);
            taskDetailsBean.setSubType("normal");
            taskDetailsBean.setTaskRequestType("normal");
            taskDetailsBean.setTaskStatus(projectCurrentStatus);
            if (!isNetworkAvailable()) {
                SimpleDateFormat simpleDateFormat_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String curr_date = simpleDateFormat_1.format(new Date());
                taskDetailsBean.setWssendstatus("000");
                taskDetailsBean.setDatenow(curr_date);
            }

            if (projectCurrentStatus != null && projectCurrentStatus.equalsIgnoreCase("DeAssign")) {
                isDeassign = true;
                taskDetailsBean.setTaskDescription(Appreference.loginuserdetails.getFirstName() + " " + Appreference.loginuserdetails.getLastName() + " Left");
                taskDetailsBean.setSubType("deassign");
            } else if (status.equalsIgnoreCase("7") || status.equalsIgnoreCase("9")) {
                taskDetailsBean.setTaskDescription("Gathering Details...");
            } else {
                taskDetailsBean.setTaskDescription("Task is " + projectCurrentStatus);
            }

            jsonObject.put("hourMeterReading", "");
            listOfObservers.clear();
            String taskMemberList_qry = "select taskMemberList from projectHistory where projectId='" + projectId + "'  and taskId= '" + webtaskId + "'";
            String taskMemberList = VideoCallDataBase.getDB(context).getProjectParentTaskId(taskMemberList_qry);
            Log.i(tab, "taskMemberList==> " + taskMemberList);
            Log.i(tab, "ownerOfTask==> " + ownerOfTask);
            if (ownerOfTask != null && !ownerOfTask.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                listOfObservers.add(ownerOfTask);
//                project_toUsers = taskMemberList;
                if (status != null && status.equalsIgnoreCase("8")) {
                    project_toUsers = "";
                    project_toUsers = listTaskMembers();
                    taskDetailsBean.setGroupTaskMembers(project_toUsers);
                    Log.i("userlist", "project_toUsers 1 " + project_toUsers);
                    if (taskMemberList != null) {
                        VideoCallDataBase.getDB(context).updateaccept("update projectHistory set taskMemberList='" + project_toUsers + "' where projectId='" + projectId + "' and taskId='" + webtaskId + "'");
                    }
                } else {
                    project_toUsers = taskMemberList;
                    taskDetailsBean.setGroupTaskMembers(taskMemberList);
                }
            } else {
                Log.i(tab, "project_toUsers==>$% " + project_toUsers);
                listOfObservers.add(project_toUsers);
                taskDetailsBean.setGroupTaskMembers(taskMemberList);
            }

            Log.i("estim1234", "timer=========>" + timer);

            /*code started for Estim Timer*/
            if (status.equalsIgnoreCase("0")) {
                Appreference.Estim_travel_TimerValue = timer;
            }

            String taskQuery = "select estimTime from taskDetailsInfo where (taskDescription='Task is Started') and projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
            String getEstimatedTimer = VideoCallDataBase.getDB(context).getValuefromDBEntry(taskQuery, "estimTime");
            Log.i("estim123", " getEstimatedTimer started===============>" + getEstimatedTimer);
            if (!status.equalsIgnoreCase("0") && !status.equalsIgnoreCase("8") && !status.equalsIgnoreCase("9")) {
                Log.i("estim123", " getEstimatedTimer from DB added===============>" + getEstimatedTimer);
                Appreference.Estim_travel_TimerValue = getEstimatedTimer;
            }
                /*code ended for Estim Timer*/

            if (status.equalsIgnoreCase("9")) {
                Log.i("location", "status $$ " + status);
                String query = "select * from projectStatus where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and status = '7'";
                TaskDetailsBean bean = VideoCallDataBase.getDB(context).getActivityTimeFromStatus(query);
                if (bean != null) {
                    if (ActivityEnddate != null && !ActivityEnddate.equalsIgnoreCase("")) {
                        if (!isNetworkAvailable()) {
                            try {
                                if (bean.getWssendstatus() != null && bean.getWssendstatus().equalsIgnoreCase("000")) {
                                    SimpleDateFormat simpleDateFormat_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String curr_date = simpleDateFormat_1.format(new Date());
                                    Log.i("travel123", "setWssendstatus set successfully===>" + isNetworkAvailable());
                                    String date_new = curr_date;
                                    String signal_Id = Utility.getSessionID();
                                    String task_description = "Gathering Details...";
                                    String queryUpdate_1 = "update projectStatus set travelEndTime='" + ActivityEnddate + "' , wssendstatus='000' where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";
                                    String queryUpdate_2 = "update projectStatus set dateStatus='7' where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";
                                    String queryUpdate_3 = "update projectStatus set datenow='" + date_new + "' where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";
                                    String queryUpdate_4 = "update projectStatus set signalId='" + signal_Id + "' where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";
                                    String queryUpdate_5 = "update projectStatus set taskDescription='" + task_description + "' where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";
                                    Log.i("output123", "projectUpdate queryUpdate_1 " + queryUpdate_1);
                                    Log.i("output123", "projectUpdate queryUpdate_2 " + queryUpdate_2);

                                    String queryUpdate_6 = "update projectStatus set endDateLatitude='" + taskDetailsBean.getEndDateLatitude() + "' where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";
                                    String queryUpdate_7 = "update projectStatus set endDateLongitude='" + taskDetailsBean.getEndDateLongitude() + "' where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";

                                    Log.i("output123", "projectUpdate query_1 " + queryUpdate_6);
                                    Log.i("output123", "projectUpdate query_1 " + queryUpdate_7);
                                    VideoCallDataBase.getDB(context).updateaccept(queryUpdate_6);
                                    VideoCallDataBase.getDB(context).updateaccept(queryUpdate_7);

                                    VideoCallDataBase.getDB(context).updateaccept(queryUpdate_2);
                                    VideoCallDataBase.getDB(context).updateaccept(queryUpdate_3);
                                    VideoCallDataBase.getDB(context).updateaccept(queryUpdate_4);
                                    VideoCallDataBase.getDB(context).updateaccept(queryUpdate_5);
                                    VideoCallDataBase.getDB(context).updateaccept(queryUpdate_1);
                                    //                            VideoCallDataBase.getDB(context).update_enddate_status(ActivityEnddate,projectId,webtaskId,String.valueOf(Appreference.loginuserdetails.getId()));
                                } else {
                                    SimpleDateFormat simpleDateFormat_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String curr_date = simpleDateFormat_1.format(new Date());
                                    Log.i("travel123", "setWssendstatus set successfully===>" + isNetworkAvailable());
                                    String date_new = curr_date;
                                    String signal_Id = Utility.getSessionID();
                                    String task_description = "Gathering Details...";
                                    String queryUpdate_1 = "update projectStatus set travelEndTime='" + ActivityEnddate + "' , wssendstatus='000' where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";
                                    String queryUpdate_2 = "update projectStatus set dateStatus='9' where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";
                                    String queryUpdate_3 = "update projectStatus set datenow='" + date_new + "' where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";
                                    String queryUpdate_4 = "update projectStatus set signalId='" + signal_Id + "' where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";
                                    String queryUpdate_5 = "update projectStatus set taskDescription='" + task_description + "' where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";
                                    String queryUpdate_6 = "update projectStatus set endDateLatitude='" + taskDetailsBean.getEndDateLatitude() + "' where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";
                                    String queryUpdate_7 = "update projectStatus set endDateLongitude='" + taskDetailsBean.getEndDateLongitude() + "' where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";

                                    Log.i("output123", "projectUpdate query_1 " + queryUpdate_6);
                                    Log.i("output123", "projectUpdate query_1 " + queryUpdate_7);
                                    VideoCallDataBase.getDB(context).updateaccept(queryUpdate_6);
                                    VideoCallDataBase.getDB(context).updateaccept(queryUpdate_7);

                                    Log.i("output123", "projectUpdate queryUpdate_1 " + queryUpdate_1);
                                    Log.i("output123", "projectUpdate queryUpdate_2 " + queryUpdate_2);
                                    VideoCallDataBase.getDB(context).updateaccept(queryUpdate_2);
                                    VideoCallDataBase.getDB(context).updateaccept(queryUpdate_3);
                                    VideoCallDataBase.getDB(context).updateaccept(queryUpdate_4);
                                    VideoCallDataBase.getDB(context).updateaccept(queryUpdate_5);
                                    VideoCallDataBase.getDB(context).updateaccept(queryUpdate_1);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("TravelJobDetails", "sendStatus_webservice update enddate Exception : " + e.getMessage(), "WARN", null);
                            }
                        } else {
                            try {
                                String queryUpdate_1 = "update projectStatus set endDateLatitude='" + taskDetailsBean.getEndDateLatitude() + "' where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";
                                String queryUpdate_2 = "update projectStatus set endDateLongitude='" + taskDetailsBean.getEndDateLongitude() + "' where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";

                                Log.i("output123", "projectUpdate query_1 " + queryUpdate_1);
                                Log.i("output123", "projectUpdate query_1 " + queryUpdate_2);
                                VideoCallDataBase.getDB(context).updateaccept(queryUpdate_1);
                                VideoCallDataBase.getDB(context).updateaccept(queryUpdate_2);

                                String queryUpdate = "update projectStatus set travelEndTime='" + ActivityEnddate + "' where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";
                                Log.i("output123", "projectUpdate query " + queryUpdate);
                                VideoCallDataBase.getDB(context).updateaccept(queryUpdate);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("TravelJobDetails", "sendStatus_webservice updateQuery Exception : " + e.getMessage(), "WARN", null);
                            }
                        }
                    }
                }
            } else {
                Log.i("location", "status $$$ " + status);
                try {
                    VideoCallDataBase.getDB(context).insertORupdateStatus(taskDetailsBean);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("TravelJobDetails", "sendStatus_webservice insertORupdateStatus Exception : " + e.getMessage(), "WARN", null);
                }
            }

            if (isNetworkAvailable()) {
                try {
                    Appreference.jsonRequestSender.taskStatus(EnumJsonWebservicename.taskStatus, jsonObject, status_list, taskDetailsBean, TravelJobDetails.this);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("TravelJobDetails", "sendStatus_webservice taskStatus  Exception : " + e.getMessage(), "WARN", null);
                }
            } else {
                try {
                    cancelDialog();
                    Toast.makeText(TravelJobDetails.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    VideoCallDataBase.getDB(context).update_Project_history(taskDetailsBean);

                    NOInternet_InsertConversaion("text", taskDetailsBean.getTaskDescription(), "", taskDetailsBean.getSignalid(), 0);

                    if (travel_date_details != null && travel_date_details.size() > 0) {
                        int sec = 0;
                        for (final String travel : travel_date_details) {
                            sec = sec + 2000;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    NOInternet_InsertConversaion("text", travel, "", Utility.getSessionID(), 0);
                                }
                            }, sec);
                        }
                    }
                    if (status.equalsIgnoreCase("0")) {
                      /*  String AlarmRingedUpdateQuery = "update taskDetailsInfo set taskPlannedLatestEndDate='11' where projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
                        Log.i("tone123", "updateSnoozeTime_query***********" + AlarmRingedUpdateQuery);
                        VideoCallDataBase.getDB(context).updateaccept(AlarmRingedUpdateQuery);*/
                        String EstimatedTimeQuery = "update taskDetailsInfo set estimCompletion='0' where projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
                        Log.i("tone123", "updateSnoozeTime_query***********" + EstimatedTimeQuery);
                        VideoCallDataBase.getDB(context).updateaccept(EstimatedTimeQuery);
                    }
                    if (!status.equalsIgnoreCase("8") && !status.equalsIgnoreCase("9")) {
                        Log.i("estim1234", "task started response ************");
                        String estim_taskQuery = "select * from taskDetailsInfo where projectId='" + projectId + "'and taskId= '" + webtaskId + "' and estimCompletion='1'";
                        int getEstimatedTimerCompleted = VideoCallDataBase.getDB(context).getCountForTravelEntry(estim_taskQuery);
                        Log.i("estim1234", "getEstimatedTimerCompleted  query************" + estim_taskQuery);

                        Log.i("estim1234", "task started response getEstimatedTimerCompleted count************" + getEstimatedTimerCompleted);
                        if (getEstimatedTimerCompleted == 0) {
                            startTravelAlarmManager(Appreference.Estim_travel_TimerValue, webtaskId, projectCurrentStatus, taskDetailsBean.getProjectId(), "EstimTimer");
                            Show_travel_EstimTimerDisplay();
                        }
                    }
                    /*code started for EstimTimer :positive Alert after DB Entry*/
                    if (status.equalsIgnoreCase("9")) {
                        Appreference.isEstimPositiveAlertShown = false;
                        ShowEstimWishAlert(projectId, webtaskId);
                    }
                  /*code Ended for EstimTimer*/
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("TravelJobDetails", "sendStatus_webservice NoNetworkAvailable Exception : " + e.getMessage(), "WARN", null);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "sendStatus_webservice Exception : " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "sendStatus_webservice Exception : " + e.getMessage(), "WARN", null);
        }
    }

    private void ShowEstimWishAlert(String projectId, String taskId) {
        try {
            Log.i("reminder123", "ShowEstimWishAlert=====>");

            String taskQuery = "select EstimAlarm from taskDetailsInfo where (taskDescription='Task is Started') and projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
            String getEstimetedTimeAlertShownOrNot = VideoCallDataBase.getDB(context).getAlertShownstatus(taskQuery, "EstimAlarm");

            Log.i("estimtone123", "getEstimetedTimeAlertShownOrNot taskQuery==>" + taskQuery);
            Log.i("estimtone123", "getEstimetedTimeAlertShownOrNot ==>" + getEstimetedTimeAlertShownOrNot);


            /********************************************/
            /*******************added 7thMar18 ********/
            /********************************************/

            /*start for RemainingTime estim timer*/
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String getLastEndTime = sdf.format(new Date());

            /*getLastEndTime interval details*/
            String Expected_HMS;
//            String taskQueryRemainingTime = "select estimRemainingTime from taskDetailsInfo where (taskStatus='Started') and projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
//            String getLastEndTime = VideoCallDataBase.getDB(context).getValuefromDBEntry(taskQueryRemainingTime, "estimRemainingTime");
            String initialEstimTime = "select estimTime from taskDetailsInfo where (taskDescription='Task is Started') and projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
            String getEstimatedTimer = VideoCallDataBase.getDB(context).getValuefromDBEntry(initialEstimTime, "estimTime");
            Log.i("remain123", " showEstimWishAlert  getEstimatedTime==============>" + getEstimatedTimer);
            Date date2 = null;
            Date date1 = null;
            try {
                date1 = sdf.parse(getLastEndTime);
                date2 = sdf.parse(getEstimatedTimer);
            } catch (Exception e) {
                Log.i("remain123", " showEstimWishAlert  data parse Exception==============>" + e.getMessage());
                e.printStackTrace();
            }
            final long mills = date2.getTime() - date1.getTime();
            final long seconds = 1000;
            Log.i("remain123", " showEstimWishAlert  mills==============>" + mills);

            if (mills > 0) {
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(mills), TimeUnit.MILLISECONDS.toMinutes(mills) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(mills)), TimeUnit.MILLISECONDS.toSeconds(mills) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mills)));
                Log.i("remain123", "Remaining Time" + hms);
//                    timer = hms;
                Expected_HMS = hms;
            } else
                Expected_HMS = "";
            Log.i("remain123", " showEstimWishAlert  Expected_HMS==============>" + Expected_HMS);

            if (Expected_HMS != null & !Expected_HMS.equalsIgnoreCase("")) {
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = mdformat.format(calendar.getTime());
                Expected_HMS = strDate + " " + Expected_HMS;
                String EstimatedRemainingTimeQuery = "update taskDetailsInfo set estimRemainingTime='" + Expected_HMS + "' where (taskDescription='Task is Started') and projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
                Log.i("remain123", "updateSnoozeTime_query***********" + EstimatedRemainingTimeQuery);
                VideoCallDataBase.getDB(context).updateaccept(EstimatedRemainingTimeQuery);
            }
            Log.i("remain123", "showEstimWishAlert  Expected_HMS DB Updated==============>");

            /********************************************/
            /*end for RemainingTime estim timer*/
            /********************************************/


            if ((getEstimetedTimeAlertShownOrNot != null && !getEstimetedTimeAlertShownOrNot.equalsIgnoreCase("")
                    && !getEstimetedTimeAlertShownOrNot.equalsIgnoreCase(null) && getEstimetedTimeAlertShownOrNot.equalsIgnoreCase("1"))) {

                    /*update query : set alarm toned*/
                String AlarmRingedUpdateQuery = "update taskDetailsInfo set EstimAlarm='0' where projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
                Log.i("estimtone123", "updateSnoozeTime_query***********" + AlarmRingedUpdateQuery);
                VideoCallDataBase.getDB(context).updateaccept(AlarmRingedUpdateQuery);

                /*update Query : set Task completed before the EstimatedHours*/
                String EstimatedTimeQuery = "update taskDetailsInfo set estimCompletion='1' where (taskDescription='Task is Started') and projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
                Log.i("tone123", "updateSnoozeTime_query***********" + EstimatedTimeQuery);
                VideoCallDataBase.getDB(context).updateaccept(EstimatedTimeQuery);


                String get_OracleprojectId_query = "select oracleProjectId from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'and projectId='" + projectId + "'";
                final String OracleIdForProjectId = VideoCallDataBase.getDB(context).getprojectIdForOracleID(get_OracleprojectId_query);
                String quryActivity1 = "select oracleTaskId from projectHistory where projectId='" + projectId + "' and taskId= '" + taskId + "'";
                final String oracleTaskId = VideoCallDataBase.getDB(getApplication()).getProjectParentTaskId(quryActivity1);


                handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                if (!Appreference.isEstimPositiveAlertShown) {
                                    Appreference.isEstimPositiveAlertShown = true;
                                    EstimTimerstop();

                                    MainActivity.startAlarmRingTone();
                                    final Dialog dialog = new Dialog(TravelJobDetails.this);
                                    dialog.setContentView(R.layout.conngrzalert);
                                    dialog.setTitle(taskName);
                                    dialog.setCancelable(false);
                                    Button dialogButtonOK = (Button) dialog.findViewById(R.id.dismiss_congrz);
                                    TextView Oracle_Project_ID = (TextView) dialog.findViewById(R.id.project_id_alert_new);
                                    TextView Oracle_Task_ID = (TextView) dialog.findViewById(R.id.taskid_alert_new);
                                    Oracle_Project_ID.setText("Job Card No :" + OracleIdForProjectId);
                                    Oracle_Task_ID.setText("Activity Code : " + oracleTaskId);
                                    TextView alert_Time = (TextView) dialog.findViewById(R.id.wish_currentTime);
                                    Calendar c = Calendar.getInstance();
                                    SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm");
                                    String formattedDate = df.format(c.getTime());
                                    alert_Time.setText(formattedDate);
                                    dialogButtonOK.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                }
                            }
                        }, 2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("NewTaskConversation", "ShowMyId clickListener Exception : " + e.getMessage(), "WARN", null);
        }
    }


    private void EstimTimerstop() {
        try {
            if (Appreference.isTravelRem_time) {
                Log.i("estim123", "EstimTimerstop isRem_time===>" + Appreference.isTravelRem_time);
                estimTravelCounter.cancel();
                tv_estim_travel_timer.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("NewTaskConversation", "timerstop Exception : " + e.getMessage(), "WARN", null);
        }
    }


    public String remove_TaskMembers(String from_UserName) {
        String project_deassignMems = "";
        String taskMemberList_qry = "select taskMemberList from projectHistory where projectId='" + projectId + "' and taskId='" + webtaskId + "'";
        String taskMemberList_1 = VideoCallDataBase.getDB(context).getValuesForQuery(taskMemberList_qry);
        Log.i("deassign123", "taskMemberList_1 ==> " + taskMemberList_1);
        if (taskMemberList_1 != null) {
            int counter = 0;
            for (int i = 0; i < taskMemberList_1.length(); i++) {
                if (taskMemberList_1.charAt(i) == ',') {
                    counter++;
                }
            }
            for (int j = 0; j < counter + 1; j++) {
                if (counter == 0) {
                    if (!taskMemberList_1.equalsIgnoreCase(from_UserName)) {
                        project_deassignMems = taskMemberList_1;
                    }
                } else {
                    if (taskMemberList_1.split(",")[j].equalsIgnoreCase(from_UserName)) {
                    } else {
                        project_deassignMems = project_deassignMems.concat(taskMemberList_1.split(",")[j] + ",");
                    }
                }
            }
            if (project_deassignMems != null && project_deassignMems.contains(",")) {
                project_deassignMems = project_deassignMems.substring(0, project_deassignMems.length() - 1);
            }
        }
        return project_deassignMems;
    }


    public String grouptaskmember() {
        String taskMemberList = "";
        String taskMemberList_qry = null;
        if (projectId != null) {
            taskMemberList_qry = "select taskMemberList from projectHistory where projectId='" + projectId + "'  and taskId= '" + webtaskId + "'";
        }
        taskMemberList = VideoCallDataBase.getDB(context).getProjectParentTaskId(taskMemberList_qry);
        return taskMemberList;
    }

    public String listTaskMembers() {
        String project_deassignMems = "";
        String taskMemberList_qry = "select taskMemberList from projectHistory where projectId='" + projectId + "' and taskId='" + webtaskId + "'";
        String taskMemberList_1 = VideoCallDataBase.getDB(context).getValuesForQuery(taskMemberList_qry);
        Log.i("deassign123", "taskMemberList_1 ==> " + taskMemberList_1);
        if (taskMemberList_1 != null) {
            int counter = 0;
            for (int i = 0; i < taskMemberList_1.length(); i++) {
                if (taskMemberList_1.charAt(i) == ',') {
                    counter++;
                }
            }
            for (int j = 0; j < counter + 1; j++) {
                if (counter == 0) {
                    if (!taskMemberList_1.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        project_deassignMems = taskMemberList_1;
                    }
                } else {
                    if (taskMemberList_1.split(",")[j].equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                    } else {
                        project_deassignMems = project_deassignMems.concat(taskMemberList_1.split(",")[j] + ",");
                    }
                }
            }
            if (project_deassignMems != null && project_deassignMems.contains(",")) {
                project_deassignMems = project_deassignMems.substring(0, project_deassignMems.length() - 1);
            }
        }
        return project_deassignMems;
    }

    public void NOInternet_InsertConversaion(String getMediaType, String getMediaPath, String getExt, String sig_id, int isDateorUpdateorNormal) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = dateFormat.format(new Date());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateforrow = dateFormat.format(new Date());
            tasktime = dateTime;
            tasktime = tasktime.split(" ")[1];
            taskUTCtime = dateforrow;
            final TaskDetailsBean chatBean = new TaskDetailsBean();

            chatBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
            chatBean.setFromUserName(Appreference.loginuserdetails.getUsername());
            chatBean.setSelect(false);
            chatBean.setTaskDescription(getMediaPath);
            chatBean.setSignalid(sig_id);
            chatBean.setTaskNo(task_No);
            chatBean.setIssueId(issueId);
            chatBean.setParentId(getFileName());
            chatBean.setTaskType(taskType);
            chatBean.setTaskPriority("Medium");
            chatBean.setIsRemainderRequired("");
            if (isProjectFromOracle && getMediaPath.equalsIgnoreCase("Completed Percentage 100 %")) {
                chatBean.setCompletedPercentage("100");
            } else {
                chatBean.setCompletedPercentage("0");
            }
            chatBean.setPlannedStartDateTime("");
            chatBean.setPlannedEndDateTime("");
            chatBean.setRemainderFrequency("");
            chatBean.setTaskUTCDateTime(dateforrow);
            chatBean.setEstimTimeForTimer(Appreference.Estim_travel_TimerValue);
            chatBean.setEstimAlarm("1");
            chatBean.setDateTime(dateTime);
            String project_deassignMems = "";
            Log.i("selfassign", "Self_assign==> " + Self_assign + " oracleProjectOwner --> " + oracleProjectOwner);
            if (Self_assign && oracleProjectOwner != null && !oracleProjectOwner.equalsIgnoreCase("")) {
                chatBean.setOwnerOfTask(oracleProjectOwner);
                chatBean.setTaskStatus("Assigned");
                ownerOfTask = oracleProjectOwner;
            } else {
                chatBean.setOwnerOfTask(ownerOfTask);
                chatBean.setTaskStatus(taskStatus);
                Log.i("NoInternet", "taskStatus===> ## " + taskStatus);
            }
            if (isDeassign) {
                chatBean.setTaskStatus("Unassigned");
                taskStatus = "Unassigned";
                chatBean.setTaskReceiver("");
                chatBean.setToUserName("");
                chatBean.setToUserId("");
                chatBean.setCompletedPercentage("0");
                if (projectGroup_Mems != null) {
                    int counter = 0;
                    for (int i = 0; i < projectGroup_Mems.length(); i++) {
                        if (projectGroup_Mems.charAt(i) == ',') {
                            counter++;
                        }
                    }
                    for (int j = 0; j < counter + 1; j++) {
                        if (counter == 0) {
                            if (!projectGroup_Mems.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                project_deassignMems = projectGroup_Mems;
                            }
                        } else {
                            if (projectGroup_Mems.split(",")[j].equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            } else {
                                project_deassignMems = project_deassignMems.concat(projectGroup_Mems.split(",")[j] + ",");
                            }
                        }
                    }
                    if (project_deassignMems != null && project_deassignMems.contains(",")) {
                        project_deassignMems = project_deassignMems.substring(0, project_deassignMems.length() - 1);
                    }
                }
                Log.i("taskConversation", "project_deassignMems " + project_deassignMems);
            } else {
                chatBean.setTaskReceiver(taskReceiver);
                chatBean.setToUserName(toUserName);
                chatBean.setToUserId(String.valueOf(toUserId));
            }
            chatBean.setTasktime(tasktime);
            chatBean.setTaskUTCTime(taskUTCtime);
            chatBean.setMimeType(getMediaType);
            chatBean.setTaskId(webtaskId);


            // send status 0 is send 1 is unsend
            chatBean.setSendStatus("0");
            chatBean.setMsg_status(0);
//        chatBean.setWs_send("000");
            chatBean.setCustomTagVisible(true);
            chatBean.setCatagory(category);
            chatBean.setSubType("normal");
            chatBean.setTaskRequestType("taskConversation");
            if (getMediaType != null && getMediaType.equalsIgnoreCase("textfile")) {
                chatBean.setLongmessage("0");
            }
            if (!getMediaType.equalsIgnoreCase("text")) {
                chatBean.setShow_progress(0);
            }
            if (project) {
                chatBean.setProjectId(projectId);
                if (isDeassign) {
                    Log.i("taskConversation", "project_deassignMems success");
                    chatBean.setGroupTaskMembers(project_deassignMems);
                } else if (projectGroup_Mems != null) {
                    chatBean.setGroupTaskMembers(projectGroup_Mems);
                }
            }
            Log.i("conv123", "taskList will add-------->  ");
            if (!getExt.equalsIgnoreCase("message")) {
                VideoCallDataBase.getDB(context).update_Project_history(chatBean);
                Log.i("NoInternet", "DBINsert");
                if (VideoCallDataBase.getDB(context).insertORupdate_Task_history(chatBean)) {
                    if (chatBean.isCustomTagVisible()) {
                        taskList.add(chatBean);
                        cancelDialog();
                        Log.i("NoInternet", "taskList ==> " + taskList.size());
                    }
                    Log.i("NoInternet", "DBINsert ==> ");
                    Log.i("task", "msg Status " + chatBean.getMsg_status());
                    refresh();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "NOInternet_InsertConversaion Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void PercentageWebService(String getMediaType, String getMediaPath, String getExt, String sig_id, int isDateorUpdateorNormal, TaskDetailsBean bean) {
        Log.i("reminder123", "PercentageWebService=====>");

        try {
            if (!getMediaPath.equals(null)) {
                String subType = "normal";
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateTime = dateFormat.format(new Date());
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                String dateforrow = dateFormat.format(new Date());
                tasktime = dateTime;
                tasktime = tasktime.split(" ")[1];
                taskUTCtime = dateforrow;
                final TaskDetailsBean chatBean = new TaskDetailsBean();

                chatBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
                chatBean.setFromUserName(Appreference.loginuserdetails.getUsername());
                chatBean.setSelect(false);
                chatBean.setTaskDescription(getMediaPath);
                chatBean.setSignalid(sig_id);
                chatBean.setTaskNo(task_No);
                chatBean.setIssueId(issueId);
                chatBean.setParentId(getFileName());
                chatBean.setTaskType(taskType);
                chatBean.setTaskPriority("Medium");
                chatBean.setIsRemainderRequired("");
                chatBean.setCompletedPercentage("0");
                chatBean.setPlannedStartDateTime("");
                chatBean.setPlannedEndDateTime("");
                chatBean.setRemainderFrequency("");
                chatBean.setTaskUTCDateTime(dateforrow);
                chatBean.setDateTime(dateTime);
                chatBean.setTasktime(tasktime);
                chatBean.setTaskUTCTime(taskUTCtime);
                chatBean.setMimeType(getMediaType);
                chatBean.setTaskId(webtaskId);

                // send status 0 is send 1 is unsend
                chatBean.setSendStatus("0");
                chatBean.setMsg_status(0);
                chatBean.setWs_send("0");
                chatBean.setCustomTagVisible(true);
                chatBean.setCatagory(category);


                chatBean.setTaskName(taskName);
                String project_deassignMems = "";
                if (bean != null && bean.getProjectStatus() != null && !bean.getProjectStatus().equalsIgnoreCase("") && !bean.getProjectStatus().equalsIgnoreCase(null)) {
                    chatBean.setProjectStatus(bean.getProjectStatus());
                }
                if (bean != null && bean.getTravelStartTime() != null && !bean.getTravelStartTime().equalsIgnoreCase("") && !bean.getTravelStartTime().equalsIgnoreCase(null)) {
                    chatBean.setTravelStartTime(bean.getTravelStartTime());
                }
                if (bean != null && bean.getTravelEndTime() != null && !bean.getTravelEndTime().equalsIgnoreCase("") && !bean.getTravelEndTime().equalsIgnoreCase(null)) {
                    chatBean.setTravelEndTime(bean.getTravelEndTime());
                }
                if (Self_assign && oracleProjectOwner != null && !oracleProjectOwner.equalsIgnoreCase("")) {
                    chatBean.setOwnerOfTask(oracleProjectOwner);
                    chatBean.setTaskStatus("Assigned");
                    ownerOfTask = oracleProjectOwner;
                    chatBean.setCatagory("task");
                } else {
                    chatBean.setOwnerOfTask(ownerOfTask);
                    chatBean.setTaskStatus(taskStatus);
                }

                if (isDeassign) {
                    chatBean.setSubType("deassign");
                    chatBean.setTaskRequestType(subType);
                    chatBean.setTaskStatus("Unassigned");
                    taskStatus = "Unassigned";
                    chatBean.setTaskReceiver("");
                    chatBean.setToUserName("");
                    chatBean.setToUserId("");
                    project_deassignMems = listTaskMembers();
                } else {
                    chatBean.setSubType(subType);
                    chatBean.setTaskRequestType(subType);
                    chatBean.setTaskReceiver(taskReceiver);
                    chatBean.setToUserName(toUserName);
                    Log.d(tab, "==============>>>>>>. 2" + chatBean.getToUserName());
                    chatBean.setToUserId(String.valueOf(toUserId));
                    chatBean.setCatagory("task");
                }
                if (!getMediaType.equalsIgnoreCase("text") && !getMediaType.equalsIgnoreCase("assigntask")) {
                    chatBean.setShow_progress(0);
                }
                if (project) {
                    chatBean.setProjectId(projectId);
                    if (isDeassign) {
                        chatBean.setGroupTaskMembers(project_deassignMems);
                    } else if (grouptaskmember() != null) {
                        chatBean.setGroupTaskMembers(grouptaskmember());
                    }
                }
                try {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject jsonObject1 = new JSONObject();
                    if (webtaskId != null)
                        jsonObject1.put("id", Integer.parseInt(webtaskId));
                    jsonObject.put("task", jsonObject1);
                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put("id", Appreference.loginuserdetails.getId());
                    jsonObject.put("from", jsonObject2);
                    jsonObject.put("signalId", sig_id);
                    jsonObject.put("parentId", getFileName());
                    jsonObject.put("createdDate", dateforrow);
                    jsonObject.put("requestType", "taskConversation");
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
                    switch (getMediaType.toLowerCase().trim()) {
                        case "text":
                            jsonObject4.put("fileType", "text");
                            jsonObject4.put("description", getMediaPath);
                            break;
                        case "assigntask":
                            jsonObject4.put("fileType", "assigntask");
                            jsonObject4.put("description", getMediaPath);
                            break;
                    }
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.put(0, jsonObject4);
                    jsonObject.put("listTaskConversationFiles", jsonArray);
                    if (jsonObject != null) {
                        Appreference.printLog("Completed percentage", jsonObject.toString(), "Completed percentage", null);
                        Appreference.jsonRequestSender.taskConversationEntry(EnumJsonWebservicename.taskConversationEntry, jsonObject, this, null, chatBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("TravelJobDetails", "PercentageWebService taskConversationEntry Exception : " + e.getMessage(), "WARN", null);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "PercentageWebService Exception : " + e.getMessage(), "WARN", null);
        }
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
            Appreference.printLog("TravelJobDetails", "getFileName Exception : " + e.getMessage(), "WARN", null);
        }
        return strFilename;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(tab, "onDestory ");
        try {
            stoptimertask();
            handler = null;
            Appreference.context_table.remove("traveljobdetails");
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "onDestroy Exception : " + e.getMessage(), "WARN", null);
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
                if (project) {
                    buffer.append(" toUserId=" + quotes + "" + quotes);
                    buffer.append(" toUserName=" + quotes + "" + quotes);
                } else {
                    buffer.append(" toUserId=" + quotes + cmbean.getToUserId() + quotes);
                    buffer.append(" toUserName=" + quotes + cmbean.getToUserName() + quotes);
                }
            } else {
                if (project) {
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
                        buffer.append(" taskStatus=" + quotes + "Closed" + quotes);
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
            if (cmbean.getFromUserName() != null && cmbean.getFromUserName().equalsIgnoreCase(ownerOfTask)) {
                Log.i("newtaskconversation", "cmbean.getTimeFrequency() " + cmbean.getTimeFrequency() + " is reminderrequired " + cmbean.getIsRemainderRequired());
                if (cmbean.getTimeFrequency() != null && (cmbean.getIsRemainderRequired() != null && cmbean.getIsRemainderRequired().equalsIgnoreCase("Y"))) {
                    TimeFrequency = TimeFrequencyCalculation(cmbean.getTimeFrequency());
                } else {
                    TimeFrequency = "";
                }
            } else {
                if (cmbean.getTimeFrequency() != null && (cmbean.getIsRemainderRequired() != null && cmbean.getIsRemainderRequired().equalsIgnoreCase("Y"))) {
                    TimeFrequency = TimeFrequencyCalculation(cmbean.getTimeFrequency());
                } else {
                    TimeFrequency = "";
                }
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
            Log.i("compose", "value TaskReceiver() --9 " + cmbean.getTaskReceiver());
            Log.i("compose", "value project_toUsers --9 " + project_toUsers);
            Log.i("compose", "value cmbean.getTaskMemberList --9 " + cmbean.getTaskMemberList());
            Log.i("compose", "value  cmbean.getGroupTaskMembers() --9 " + cmbean.getGroupTaskMembers());
            Log.i("compose", "value project " + project);
            if (project) {
                buffer.append(" taskToUsersList=" + quotes + project_toUsers + quotes);
            } else {
                buffer.append(" taskToUsersList=" + quotes + project_toUsers + quotes);
            }
            buffer.append(" requestStatus=" + quotes + cmbean.getRequestStatus() + quotes);
            buffer.append(" subType=" + quotes + cmbean.getSubType() + quotes);
            buffer.append(" daysOfTheWeek=" + quotes + cmbean.getDaysOfTheWeek() + quotes);
            buffer.append(" repeatFrequency=" + quotes + cmbean.getRepeatFrequency() + quotes);
            buffer.append(" taskTagName=" + quotes + cmbean.getTaskTagName() + quotes);
            buffer.append(" taskTagId=" + quotes + cmbean.getCustomTagId() + quotes);
            buffer.append(" taskTagGroupId=" + quotes + cmbean.getCustomSetId() + quotes);
            buffer.append(" isShowOnUI=" + quotes + cmbean.isCustomTagVisible() + quotes);
            Log.i("taskconversation", "Leave Project id 3 " + projectId);
            Log.i("taskconversation", "cmbean.getProjectId() " + cmbean.getProjectId());
            buffer.append(" projectId=" + quotes + projectId + quotes);
            buffer.append(" taskCategory=" + quotes + cmbean.getCatagory() + quotes);
            Log.i("taskconversation", "Leave Project id 4 " + projectId);
            buffer.append(" parentTaskId=" + quotes + cmbean.getIssueId() + quotes);
            if (cmbean.getDaysOfTheWeek() != null && !cmbean.getDaysOfTheWeek().equalsIgnoreCase("") && !cmbean.getDaysOfTheWeek().equalsIgnoreCase(null)) {
                buffer.append(" isRepeatTask=" + quotes + "Y" + quotes);
            }
            if (cmbean.getProjectStatus() != null && !cmbean.getProjectStatus().equalsIgnoreCase("") && !cmbean.getProjectStatus().equalsIgnoreCase(null)) {
                buffer.append(" projectStatus=" + quotes + cmbean.getProjectStatus() + quotes);
            }
            if (cmbean.getTravelStartTime() != null && !cmbean.getTravelStartTime().equalsIgnoreCase("") && !cmbean.getTravelStartTime().equalsIgnoreCase(null)) {
                buffer.append(" travelStartTime=" + quotes + cmbean.getTravelStartTime() + quotes);
            }
            if (cmbean.getTravelEndTime() != null && !cmbean.getTravelEndTime().equalsIgnoreCase("") && !cmbean.getTravelEndTime().equalsIgnoreCase(null)) {
                buffer.append(" travelEndTime=" + quotes + cmbean.getTravelEndTime() + quotes);
            }
            buffer.append(" />");
            buffer.append("</TaskDetailsinfo>");
            Log.d("xml", "composed xml for chat======>" + buffer.toString());
            Log.d("xml", "composed xml for encode data======>" + Charset.forName("UTF-8").encode(":-)").toString());
//            Log.i("xml", "composed xml for listofabservers " + listOfObservers);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "composeChatXML Exception : " + e.getMessage(), "WARN", null);
        } finally {
            return buffer.toString();
        }
    }

    public String TimeFrequencyCalculation(String timeFrequency) {
        String remainder_Frequency = null, rem_freq_min, rem_frq;
        try {
            String time = timeFrequency;
            remainder_Frequency = "";
            long total_mins;
            if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
                //            Log.i("task", "Reminder Freq Local Changed to Lower case " + reminderfreq.toLowerCase());
                switch (time.toLowerCase()) {
                    case "none":
                        remainder_Frequency = "0";
                        break;
                    case "every minute":
                        total_mins = 60000;
                        remainder_Frequency = String.valueOf(total_mins);
                        break;
                    case "every 10 min":
                        total_mins = 10 * 60000;
                        remainder_Frequency = String.valueOf(total_mins);
                        break;
                    case "hourly":
                        total_mins = 60 * 60000;
                        remainder_Frequency = String.valueOf(total_mins);
                        break;
                    case "daily":
                        total_mins = 1440 * 60000;
                        remainder_Frequency = String.valueOf(total_mins);
                        break;
                    case "week day":
                        total_mins = 10080 * 60000;
                        remainder_Frequency = String.valueOf(total_mins);
                        break;
                    case "monthly":
                        total_mins = 43200 * 60000;
                        remainder_Frequency = String.valueOf(total_mins);
                        break;
                    case "yearly":
                        total_mins = 525600 * 60000;
                        remainder_Frequency = String.valueOf(total_mins);
                        break;
                    default:
                        remainder_Frequency = "0";
                        break;
                }
            } else {
                Log.i("Request", "timeFrequency " + time);  // 1 Minute
                rem_freq_min = time.split(" ")[0];   // 1
                rem_frq = time.split(" ")[1];   // Minute
                Log.i("task", "Reminder Freq Minutes " + rem_freq_min);
                switch (rem_frq.toLowerCase()) {
                    case "minutes":
                        total_mins = Long.parseLong(rem_freq_min) * 60000;
                        Log.i("task", "Reminder miniute Milliseconds " + total_mins);
                        remainder_Frequency = String.valueOf(total_mins);
                        break;
                    case "hours":
                        total_mins = Long.parseLong(rem_freq_min) * (60 * 60000);
                        Log.i("task", "Reminder hour Milliseconds " + total_mins);
                        remainder_Frequency = String.valueOf(total_mins);
                        break;
                    case "days":
                        long day_s = Long.parseLong(rem_freq_min);
                        total_mins = day_s * (1440 * 60000);
                        Log.i("task", "Reminder day Milliseconds " + day_s + " " + total_mins);
                        remainder_Frequency = String.valueOf(total_mins);
                        break;
                    default:
                        remainder_Frequency = "0";
                        break;
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "TimeFrequencyCalculation Exception : " + e.getMessage(), "WARN", null);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "TimeFrequencyCalculation Exception : " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "TimeFrequencyCalculation Exception : " + e.getMessage(), "WARN", null);
        }
        return remainder_Frequency;
    }

    public void updateMessageStatus(TaskDetailsBean taskDetailsBean) {
        try {
            if (taskDetailsBean != null) {
                Log.i("travelJobDetails ", "updateMessageStatus==> @@ ");
                for (TaskDetailsBean detailsBean : taskList) {
                    if (detailsBean.getSignalid() != null && detailsBean.getSignalid().equalsIgnoreCase(taskDetailsBean.getSignalid())) {
                        Log.i("TaskObserver", "task " + detailsBean.getMsg_status());
                        Log.i("TaskObserver", "getSignalid ==>  " + taskDetailsBean.getSignalid());
                        Log.i("TaskObserver", "getSignalid ==>  " + detailsBean.getSignalid());
                        if (detailsBean.getMsg_status() <= 0 && taskDetailsBean.getMsg_status() != 24) {
                            detailsBean.setMsg_status(1);
                            Log.i("TaskObserver", "task if " + detailsBean.getMsg_status());
                        } else if (taskDetailsBean.getMsg_status() == 24) {
                            detailsBean.setMsg_status(24);
                        }
                        break;
                    } else if (taskDetailsBean.getMsg_status() == 1) {
                        if (detailsBean.getMsg_status() == 24) {
                            detailsBean.setMsg_status(1);
                        }
                    }
                }
                refresh();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "updateMessageStatus Exception : " + e.getMessage(), "WARN", null);
        }

    }


    public void sendMultiInstantMessage(String msgBody, ArrayList<String> userlist, int sendTo) {
        try {
            for (String name : userlist) {
                Log.i("taskConversation", "sendMultiInstantMessage 1  ");
                Log.i("task observer", "observer 1 " + name);
                Log.i("task observer", "MainActivity.account.buddyList.size()" + MainActivity.account.buddyList.size());
            }
            if ((taskType != null && !taskType.equalsIgnoreCase("Group")) || project) {
                Log.i("groupMemberAccess", "!group ");
                Log.i("taskConversation", "sendMultiInstantMessage 2  ");
                //        if (!getResources().getString(R.string.proxyua).equalsIgnoreCase("enable") || !taskType.equalsIgnoreCase("Group")) {
                if (!getResources().getString(R.string.proxyua).equalsIgnoreCase("enable") || sendTo == 1) {
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
                                final MyBuddy myBuddy = MainActivity.account.buddyList.get(i);
                                final SendInstantMessageParam prm = new SendInstantMessageParam();
                                prm.setContent(msgBody);

                                boolean valid = myBuddy.isValid();
                                Log.i("task", "valid ======= " + valid);
                                Log.i("taskConversation", "sendMultiInstantMessage 7  ");
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            myBuddy.sendInstantMessage(prm);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Appreference.printLog("TravelJobDetails", "sendMultiInstantMessage individual Exception : " + e.getMessage(), "WARN", null);
                                        }
                                    }
                                });
                                break;
                            }
                        }
                    }
                }
            } else {
                Log.i("groupMemberAccess", "getRespondTask==1 ");
                Log.i("taskConversation", "sendMultiInstantMessage 19  ");
                if (!getResources().getString(R.string.proxyua).equalsIgnoreCase("enable")) {
                    Log.i("taskConversation", "sendMultiInstantMessage 20  ");
                    for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
                        String name = MainActivity.account.buddyList.get(i).cfg.getUri();
                        Log.i("task", "buddyname-->  " + name);
                        Log.i("taskConversation", "sendMultiInstantMessage 21  ");
                        for (String username : userlist) {
                            Log.i("taskConversation", "sendMultiInstantMessage 22  ");
                            Log.i("task", "taskObservers Name--> " + username);
                            String nn = "sip:" + username + "@" + getResources().getString(R.string.server_ip);
                            Log.i("task", "selected user--> " + nn);
                            if (nn.equalsIgnoreCase(name)) {
                                Log.i("taskConversation", "sendMultiInstantMessage 23  ");
                                Log.i("task", "both users are same ");
                                Appreference.printLog("Sipmessage", msgBody, "DEBUG", null);
                                MyBuddy myBuddy = MainActivity.account.buddyList.get(i);
                                SendInstantMessageParam prm = new SendInstantMessageParam();
                                prm.setContent(msgBody);

                                boolean valid = myBuddy.isValid();
                                Log.i("task", "valid ======= " + valid);
                                try {
                                    Log.i("taskConversation", "sendMultiInstantMessage 24  ");
                                    myBuddy.sendInstantMessage(prm);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("TravelJobDetails", "sendMultiInstantMessage group Exception : " + e.getMessage(), "WARN", null);
                                }
                                break;
                            }
                        }
                    }
                } else {
                    Log.i("taskConversation", "sendMultiInstantMessage 25  ");
                    BuddyConfig bCfg = new BuddyConfig();
                    bCfg.setUri("sip:" + proxy_user + "@" + getResources().getString(R.string.server_ip));
                    bCfg.setSubscribe(false);
                    //            MainActivity.account.addBuddy(bCfg);
                    Buddy myBuddy = new Buddy();
                    try {
                        Log.i("taskConversation", "sendMultiInstantMessage 26  ");
                        myBuddy.create(MainActivity.account, bCfg);
                        //                MainActivity.account.addBuddy(myBuddy);
                        Log.i("task", "proxybuddy " + bCfg.getUri());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("TravelJobDetails", "sendMultiInstantMessage proxy Exception : " + e.getMessage(), "WARN", null);
                    }
                    SendInstantMessageParam prm = new SendInstantMessageParam();
                    prm.setContent(msgBody);

                    boolean valid = myBuddy.isValid();
                    Log.i("task", "valid ======= " + valid);
                    try {
                        Log.i("taskConversation", "sendMultiInstantMessage 27  ");
                        myBuddy.sendInstantMessage(prm);
                        myBuddy.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("TravelJobDetails", "sendMultiInstantMessage proxy mybuddy Exception : " + e.getMessage(), "WARN", null);
                    }

                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "sendMultiInstantMessage Exception : " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "sendMultiInstantMessage Exception : " + e.getMessage(), "WARN", null);
        }
    }


    public void refresh() {
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (medialistadapter != null) {
                        Log.i(tab, "refresh ");
                        medialistadapter.notifyDataSetChanged();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "refresh() Exception : " + e.getMessage(), "WARN", null);
        }

    }

    public void showNetworkStateUI() {
        try {
            if (ll_networkUI != null && tv_networkstate != null) {
                if (Appreference.networkState) {
                    if (Appreference.sipRegistrationState) {
                    } else if (!Appreference.sipRegistrationState) {
                        ll_networkUI.setVisibility(View.VISIBLE);
                        ll_networkUI.setBackgroundColor(getResources().getColor(R.color.orange));
                        tv_networkstate.setText("Connecting...");
                    }
                } else if (!Appreference.networkState) {
                    ll_networkUI.setVisibility(View.VISIBLE);
                    ll_networkUI.setBackgroundColor(getResources().getColor(R.color.red_color));
                    tv_networkstate.setText("No Internet Connection");
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "showNetworkStateUI Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void showNetWorkConnectedState() {
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (ll_networkUI != null && tv_networkstate != null) {
                        Log.i("network", "ll_networkUI!=null");
                        if (Appreference.networkState) {
                            Log.i("network", "Appreference.networkState");
                            if (Appreference.sipRegistrationState) {
                                Log.i("network", "Appreference.sipRegistrationState");
                                ll_networkUI.setVisibility(View.VISIBLE);
                                ll_networkUI.setBackgroundColor(getResources().getColor(R.color.connected));
                                tv_networkstate.setText("Connected");
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ll_networkUI.setVisibility(View.GONE);
                                    }
                                }, 2000);
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "showNetWorkConnectedState Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void addTaskReassignClickEvent() {
        try {
            Intent intent = new Intent(context, AddTaskReassign.class);
            intent.putExtra("taskId", webtaskId);
            intent.putExtra("taskType", taskType);
            intent.putExtra("Taker", "Assigned Task");
            intent.putExtra("jobcodeno", JobCodeNo);
            intent.putExtra("activitycode", ActivityCode);
            intent.putExtra("Clickposition", clickPosition);
            if (isProjectFromOracle)
                intent.putExtra("isProjectFromOracle1", true);
            if (Self_assign)
                intent.putExtra("selfAssign", true);

            if (taskType != null && taskType.equalsIgnoreCase("Group")) {
                TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                taskDetailsBean.setOwnerOfTask(ownerOfTask);
                taskDetailsBean.setTaskReceiver(taskReceiver);
                taskDetailsBean.setTaskName(taskName);
                taskDetailsBean.setTaskNo(task_No);
                taskDetailsBean.setTaskType(taskType);
                taskDetailsBean.setCatagory(category);
                taskDetailsBean.setTaskId(webtaskId);
                taskDetailsBean.setTaskStatus(taskStatus);
                taskDetailsBean.setParentTaskId(OracleParentTaskId);

                if (toUserId > -1) {
                    intent.putExtra("groupname", String.valueOf(toUserId));
                } else {
                    intent.putExtra("groupname", groupname);
                }
               /* if (project) {*/
                intent.putExtra("isProject", "Yes");
                taskDetailsBean.setProjectId(projectId);
                /*} else {
                    intent.putExtra("isProject", "No");
                }*/
                intent.putExtra("taskReceiver", taskReceiver);
                intent.putExtra("taskBean", taskDetailsBean);
            } else {
                Log.i("taskConversation", "projectId reassign else");
                TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                taskDetailsBean.setOwnerOfTask(ownerOfTask);
                taskDetailsBean.setTaskReceiver(taskReceiver);
                taskDetailsBean.setTaskName(taskName);
                taskDetailsBean.setTaskNo(task_No);
                taskDetailsBean.setTaskType(taskType);
                taskDetailsBean.setCatagory(category);
                taskDetailsBean.setTaskId(webtaskId);
                taskDetailsBean.setTaskStatus(taskStatus);
                taskDetailsBean.setParentTaskId(OracleParentTaskId);
                /*if (project) {*/
                taskDetailsBean.setProjectId(projectId);
                intent.putExtra("isProject", "Yes");
                /*} else {
                    intent.putExtra("isProject", "No");
                }*/
                intent.putExtra("taskReceiver", taskReceiver);
                if (taskDetailsBean != null)
                    Log.i("ws123", "Bean============4411" + taskDetailsBean.getParentTaskId() + "getProjectId=========>" + taskDetailsBean.getProjectId());
                else
                    Log.i("ws123", "Bean null============> 4411");

                intent.putExtra("taskBean", taskDetailsBean);
                Log.i("taskConversation", "projectId " + taskDetailsBean.getProjectId());
            }
            startActivityForResult(intent, 105);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "addTaskReassignClickEvent Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void taskAssign(final TaskDetailsBean taskDetailsBean, String removeUser, String newReceiver, int isProject) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = dateFormat.format(new Date());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateforrow = dateFormat.format(new Date());
            tasktime = dateTime;
            tasktime = tasktime.split(" ")[1];
            Log.i("task", "tasktime" + tasktime);
            Log.i("ASE", "sendMessage utc time" + dateforrow);
            Log.i("ASE", "removeUser " + removeUser);
            Log.i("ASE", "newReceiver " + newReceiver);
            Log.i("ASE", "isProject " + isProject);
            taskUTCtime = dateforrow;
            taskDetailsBean.setRead_status(0);
            taskDetailsBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
            taskDetailsBean.setFromUserName(Appreference.loginuserdetails.getUsername());
            taskDetailsBean.setSendStatus("0");
            taskDetailsBean.setWs_send("1");
            taskDetailsBean.setMsg_status(0);
            taskDetailsBean.setTaskUTCDateTime(dateforrow);
            taskDetailsBean.setDateTime(dateTime);
            taskDetailsBean.setTasktime(tasktime);
            taskDetailsBean.setTaskUTCTime(taskUTCtime);
            taskDetailsBean.setCustomTagVisible(false);

            String reassignUser1 = "";
            Log.i("ASE", "reassignUser1 ==> " + reassignUser1);

            if (taskDetailsBean.getSignalid() == null || taskDetailsBean.getSignalid().trim().equals("")) {
                taskDetailsBean.setSignalid(Utility.getSessionID());
            }
            Log.i("ASE_NTC ", "reassignUser " + taskDetailsBean.getSignalid());

            if (!listOfObservers.contains(removeUser)) {
                listOfObservers.add(removeUser);
            }

            Log.i("taskconversation", "Reassign task removed user " + removeUser + " " + listOfObservers);

            taskDetailsBean.setCustomTagVisible(true);
            Log.i("ASE", "project_toUsers " + taskDetailsBean.getTaskMemberList());
            String Mem_name = "";
            String reassignUser = "";
            project_toUsers = "";
            if (!taskDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
//            reassignUser = VideoCallDataBase.getDB(context).getname(taskDetailsBean.getTaskReceiver());
                Mem_name = taskDetailsBean.getTaskReceiver();
                project_toUsers = taskDetailsBean.getTaskReceiver();
            } else if (taskDetailsBean.getTaskMemberList() != null) {
                Mem_name = taskDetailsBean.getTaskMemberList();
                project_toUsers = taskDetailsBean.getTaskMemberList();
                taskType = "group";
            }
            Log.i("ASE", "reassignUser " + reassignUser);
            Log.i("ASE", "project_toUsers ?? " + project_toUsers);
            Log.i("ASE", "Mem_name ?? " + Mem_name);

            if (isProject == 0) {
                Log.i("ASE_NTC ", "reassignUser " + reassignUser);
                taskDetailsBean.setTaskDescription("Task Reassigned to " + reassignUser);
            } else {
                taskDetailsBean.setTaskDescription("Task Assigned to " + Mem_name);
            }

            if (taskDetailsBean.getTaskMemberList() != null && taskDetailsBean.getTaskMemberList().contains(",")) {
                Log.i("ASE_NTC ", "reassignUser ** ");
                taskDetailsBean.setTaskType("Group");
                taskDetailsBean.setTaskMemberList(taskDetailsBean.getTaskMemberList());
                taskDetailsBean.setToUserId("");
                taskDetailsBean.setTaskReceiver("");
            } else {
                Log.i("ASE_NTC ", "reassignUser ** ");
                taskDetailsBean.setTaskReceiver(newReceiver);
                taskDetailsBean.setToUserName(taskDetailsBean.getTaskReceiver());
                taskDetailsBean.setTaskType("individual");
                taskDetailsBean.setToUserId(VideoCallDataBase.getDB(getApplication()).getProjectParentTaskId("select userid from contact where username = '" + taskDetailsBean.getTaskReceiver() + "'"));
            }

            taskDetailsBean.setMimeType("assigntask");
            taskDetailsBean.setTaskRequestType("normal");
            taskDetailsBean.setSubType("normal");

            if (taskDetailsBean.getCatagory() != null && taskDetailsBean.getCatagory().equalsIgnoreCase("note")) {
                if (taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(taskDetailsBean.getTaskReceiver())) {
                    taskDetailsBean.setMsg_status(1);
                }
            }
            if (taskDetailsBean.getCatagory() != null && taskDetailsBean.getCatagory().equalsIgnoreCase("note")) {
                taskDetailsBean.setCatagory("note");
            } else {
                taskDetailsBean.setCatagory("Task");
            }
//        taskDetailsBean.setCatagory("Task");
            Log.i("taskconversation", "db updated projectId " + taskDetailsBean.getProjectId());
            if (taskDetailsBean.getProjectId() != null) {
                taskDetailsBean.setTaskStatus("Assigned");
                if (taskDetailsBean.getTaskMemberList() != null && taskDetailsBean.getTaskMemberList().contains(",")) {
                    taskDetailsBean.setGroupTaskMembers(taskDetailsBean.getTaskMemberList());
                } else {
                    taskDetailsBean.setGroupTaskMembers(taskDetailsBean.getTaskReceiver());
                }
                dataBase.update_Project_history(taskDetailsBean);
                Log.i("taskconversation", "db updated 2");
            } else {
                dataBase.insertORupdate_TaskHistoryInfo(taskDetailsBean);
            }
            dataBase.insertORupdate_Task_history(taskDetailsBean);
//        dataBase.insertORupdate_TaskHistoryInfo(taskDetailsBean);


            if (!listOfObservers.contains(newReceiver)) {
                listOfObservers.add(newReceiver);
            }
            String query1 = "select * from taskHistoryInfo where loginuser ='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + taskDetailsBean.getTaskId() + "' order by id";
            Log.d("TaskObserver", "get Observer query  " + query1);
            ArrayList<TaskDetailsBean> arrayList;
            arrayList = VideoCallDataBase.getDB(context).getTaskHistoryInfo(query1);
            Log.d("TaskObserver", "Task Observer list size is == " + arrayList.size());
            if (arrayList.size() > 0) {
                TaskDetailsBean taskDetailsBean1 = arrayList.get(0);
                String taskObservers = taskDetailsBean1.getTaskObservers();
                int counter = 0;
                Log.d("TaskObserver", "2 Task Observer  == " + taskObservers);
                if (taskObservers != null) {
                    for (int i = 0; i < taskObservers.length(); i++) {
                        if (taskObservers.charAt(i) == ',') {
                            counter++;
                        }
                    }
                    ArrayList<String> listOfObservers = new ArrayList<>();
                    for (int j = 0; j < counter + 1; j++) {
                        if (counter == 0) {
                            if (Appreference.loginuserdetails.getUsername().equalsIgnoreCase(taskObservers) || newReceiver.equalsIgnoreCase(taskObservers)) {
                                listOfObservers.remove(taskObservers);
                                Log.d("TaskRemovedObserver", "Task Removed  Observer name 1 == " + taskObservers);
                            }
                        }
                        if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(taskObservers.split(",")[j]) && !newReceiver.equalsIgnoreCase(taskObservers.split(",")[j])) {
                            if (!listOfObservers.contains(taskObservers.split(",")[j]))
                                listOfObservers.add(taskObservers.split(",")[j]);
                            Log.d("TaskObserver", "Task Observer name not in same user== " + taskObservers.split(",")[j]);
                        }
                    }
                    String name = "";
                    for (String s : listOfObservers) {
                        name = name.concat(s) + ",";
                    }
                    if (name != null && !name.equalsIgnoreCase(""))
                        name = name.substring(0, name.length() - 1);

                    VideoCallDataBase.getDB(context).updateaccept("update taskHistoryInfo set taskObservers='" + name + "' where taskId='" + taskDetailsBean.getTaskId() + "'");
                    Log.i("mainactivity", "taskObservers updated " + name);
                }
            }
            Log.i("Rassign", "values " + taskDetailsBean.getToUserId() + " " + taskDetailsBean.getToUserName());
            if (taskDetailsBean.getToUserId() != null && !taskDetailsBean.getToUserId().equalsIgnoreCase("")) {
                toUserId = Integer.parseInt(taskDetailsBean.getToUserId());
            }
            if (taskDetailsBean.getToUserName() != null && !taskDetailsBean.getToUserName().equalsIgnoreCase("")) {
                toUserName = taskDetailsBean.getToUserName();
                taskReceiver = taskDetailsBean.getToUserName();
            } else {
                taskReceiver = project_toUsers;
            }
            if (project) {
                VideoCallDataBase.getDB(context).updateaccept("update taskDetailsInfo set mimeType='dates' where taskid='" + taskDetailsBean.getTaskId() + "' and mimeType='date' and duration!='' and durationunit!=''");
                VideoCallDataBase.getDB(context).updateaccept("update taskDetailsInfo set taskStatus='Assigned' where taskid='" + taskDetailsBean.getTaskId() + "'");
                Log.i("Rassign", "project template updated" + taskDetailsBean.getTaskId());
            }

            Log.i("TaskConversation", " Task Reassign View listOfObservers -----><----- " + listOfObservers);

            // get project total members


            final ArrayList<String> arrayList1 = new ArrayList<>();
            String projectMembers = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskMemberList from projectHistory where projectId ='" + taskDetailsBean.getProjectId() + "' and parentTaskId==taskId");

            if (projectMembers != null) {
                int counter_1 = 0;
                for (int i = 0; i < projectMembers.length(); i++) {
                    if (projectMembers.charAt(i) == ',') {
                        counter_1++;
                    }
                    Log.i("taskConversation", "project_details Task Mem's counter size is == " + counter_1);
                }
                Log.i("taskConversation", "projectBean.getFromUserName() " + projectMembers);
                for (int j = 0; j < counter_1 + 1; j++) {
                    //                Log.i("taskConversation", "project_details Task Mem's and position == " + observers.split(",")[j] + " " + j);
                    if (counter_1 == 0) {
                        if (!arrayList1.contains(projectMembers)) {
                            arrayList1.add(projectMembers);
                        }
                    } else {
                        if (!arrayList1.contains(projectMembers.split(",")[j])) {
                            arrayList1.add(projectMembers.split(",")[j]);
                        }
                    }
                }
            }
            if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(taskDetailsBean.getOwnerOfTask())) {
                if (!arrayList1.contains(taskDetailsBean.getOwnerOfTask())) {
                    arrayList1.add(taskDetailsBean.getOwnerOfTask());
                }
            }
            if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(taskReceiver)) {
                if (!arrayList1.contains(taskReceiver)) {
                    arrayList1.add(taskReceiver);
                }
            }
            if (arrayList1.size() > 0) {
                listOfObservers = arrayList1;
            }
            taskList.add(taskDetailsBean);
            sortTaskMessage();
            refresh();
            final String xml1 = composeChatXML(taskDetailsBean);

            Log.i("ASE", "arrayList1 ==> " + arrayList1.size() + " listOfObservers ==>" + listOfObservers.size());
            int timeLimit = 2000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (taskDetailsBean.getCatagory() != null && taskDetailsBean.getCatagory().equalsIgnoreCase("note")) {
                        if (!taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(taskDetailsBean.getTaskReceiver())) {
                            sendMultiInstantMessage(xml1, listOfObservers, 0);
                        }
                    } else {
                        if (getResources().getString(R.string.proxyua).equalsIgnoreCase("enable")) {
                            sendMultiInstantMessage(xml1, listOfObservers, 1);
                        } else {
//                sendMultiInstantMessage(xml1, listOfObservers, 0);
                            Log.i("ASE", "project total meber list size in Reassign Method is == " + arrayList1.size());
                            sendMultiInstantMessage(xml1, arrayList1, 1);
                        }
                    }
                }
            }, timeLimit);


        /*if (listOfObservers.contains(removeUser)) {
            Log.d("TaskRemovedObserver", "Task Removed  Observer name 2 == " + removeUser);
            listOfObservers.remove(removeUser);
//            listObservers.remove(removeUser);
        }*/
            Log.i("taskconversation", "Reassign task newly added user " + newReceiver + " " + listOfObservers);

            ll_2.setVisibility(View.GONE);
            status_job.setVisibility(View.VISIBLE);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "taskAssign Exception : " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "taskAssign Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void sortTaskMessage() {
        try {
            Log.i(tab, "getTask sortTaskMessage ");

            Collections.sort(taskList, new Comparator<TaskDetailsBean>() {
                public int compare(TaskDetailsBean o1, TaskDetailsBean o2) {
                    if (o1.getDateTime() == null || o2.getDateTime() == null)
                        return 0;
                    Log.i("sortmessage", "TASK HISTORY List size  = " + taskList.size() + "  ==" + o2.getDateTime().compareTo(o1.getDateTime()));
                    Log.i("sortmessage", "TASK HISTORY List size  = " + taskList.size() + "  ==" + o1.getDateTime() + " ==1 " + " ==2 " + (o2.getDateTime()));
                    return o1.getDateTime().compareTo(o2.getDateTime());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "sortTaskMessage Exception : " + e.getMessage(), "WARN", null);
            Log.d(tab, "Sorting problem");
        }
        refresh();
    }

    public void sendMessage(String message, String pri, final String type1, final String imagename, final String remquotes_2, String sig_id, TaskDetailsBean chatBean) {
        try {
            Log.d("chat", "------sendMessage entry------");
            Log.i("taskConversation", "private sendMessage * 0 ");
            Log.i("reminder123", "PercentageWebService=====>");


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = dateFormat.format(new Date());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateforrow = dateFormat.format(new Date());
            tasktime = dateTime;
            tasktime = tasktime.split(" ")[1];
            taskUTCtime = dateforrow;
            Log.i("Sendmessage", "SubType * 0 " + chatBean.getSubType());
            if (chatBean == null) {
                Log.i("taskConversation", "private sendMessage * 0 ");
                chatBean = new TaskDetailsBean();
            }
            Log.i("Sendmessage", "SubType * 1 " + chatBean.getSubType());
            chatBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
            chatBean.setToUserId(String.valueOf(toUserId));
            chatBean.setFromUserName(Appreference.loginuserdetails.getUsername());
            chatBean.setSelect(false);
            chatBean.setTaskNo(task_No);
            chatBean.setIssueId(issueId);
            chatBean.setParentId(getFileName());
            chatBean.setPlannedStartDateTime("");
            chatBean.setPlannedEndDateTime("");
            chatBean.setRemainderFrequency("");
            chatBean.setDateFrequency("");
            chatBean.setTimeFrequency("");
            chatBean.setServerFileName("");
            chatBean.setTaskUTCDateTime(dateforrow);
            chatBean.setEstimTimeForTimer(Appreference.Estim_travel_TimerValue);
            chatBean.setEstimAlarm("1");
            chatBean.setDateTime(dateTime);
            chatBean.setSendStatus("0");
            chatBean.setTaskType(taskType);
            chatBean.setTaskId(webtaskId);
            chatBean.setTasktime(tasktime);
            chatBean.setTaskUTCTime(taskUTCtime);
            chatBean.setSignalid(sig_id);
            chatBean.setCustomTagVisible(true);
            chatBean.setTaskStatus(taskStatus);
            Log.i("template", "taskStatus==> @ " + taskStatus);

            chatBean.setTaskDescription(message);
            Log.i("template", "message2 " + chatBean.getTaskDescription());
            chatBean.setTaskPriority("Medium");


            if (chatBean.getTaskDescription() != null && (chatBean.getTaskDescription().contains("www.") || chatBean.getTaskDescription().contains("https:") || chatBean.getTaskDescription().contains("http:"))) {
                chatBean.setMimeType("url");
            } else {
                chatBean.setMimeType(type1);
            }
            chatBean.setTaskName(taskName);
            chatBean.setOwnerOfTask(ownerOfTask);
            String project_deassignMems = "";
            if (Self_assign && oracleProjectOwner != null && !oracleProjectOwner.equalsIgnoreCase("")) {
                Log.i("taskConversation", "Self_assign ==>  " + Self_assign);
                chatBean.setOwnerOfTask(oracleProjectOwner);
                chatBean.setTaskStatus("Assigned");
                ownerOfTask = oracleProjectOwner;
                chatBean.setCatagory("task");
            } else {
                chatBean.setOwnerOfTask(ownerOfTask);
                chatBean.setTaskStatus(taskStatus);
            }
            chatBean.setTaskMemberList(grouptaskmember());
            if (chatBean.getSubType() != null && chatBean.getSubType().equalsIgnoreCase("deassign")) {
                Log.i("taskConversation", "isDeassign 1 ===> " + isDeassign);
                Log.i("taskConversation", "taskReceiver  1 ==> " + taskReceiver);
                Log.i("taskConversation", "toUserName  1 ==> " + toUserName);
                Log.i("taskConversation", "toUserId  1 ==> " + toUserId);
                chatBean.setTaskReceiver("");
                chatBean.setToUserName("");
                chatBean.setToUserId("");

                chatBean.setSubType("deassign");
                chatBean.setTaskRequestType("normal");
                /*chatBean.setTaskStatus("Unassigned");
                chatBean.setCatagory("Template");
                taskStatus = "Unassigned";*/

                project_deassignMems = getAllProjectMembersList();
                Log.i("deassign123", "project_deassignMems **  " + project_deassignMems);
                Log.i("deassign123", "------listOfObservers ==> " + listOfObservers);
                Log.i("deassign123", "------ownerOfTask ==> " + ownerOfTask);

                if (project_deassignMems != null && !project_deassignMems.equalsIgnoreCase("") && !project_deassignMems.equalsIgnoreCase(null)) {
                    chatBean.setGroupTaskMembers(listTaskMembers());
                    listOfObservers.clear();

                    if (project_deassignMems != null) {
                        String members_deassign[] = project_deassignMems.split(",");
                        for (int i = 0; i < members_deassign.length; i++) {
                            listOfObservers.add(members_deassign[i]);
                        }
                    }
//                    listOfObservers.add(ownerOfTask);
                }
                String status_info = "select status from projectStatus where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and status!='7' and status!= '9'and status!= '10' and status!= '8' order by id DESC";
                ArrayList<String> status_all = VideoCallDataBase.getDB(context).getAllCurrentStatus(status_info);
                Log.i("output123", "project CurrentStatus from DB====>" + status_all.size());
                String current_status_now = "";
                if (status_all.size() > 0) {
                    current_status_now = status_all.get(0);
                    Log.i("output123", "project CurrentStatus from current_status " + current_status_now);
                }
                String status_now = getStatusForNumber(current_status_now);
                if (listTaskMembers().length() > 0) {
                    if (listTaskMembers().length() == 1) {
                        chatBean.setTaskStatus(status_now);
                        chatBean.setCatagory("Task");
                        chatBean.setTaskType("individual");
                        taskStatus = status_now;
                    } else {
                        chatBean.setTaskStatus(status_now);
                        chatBean.setCatagory("Task");
                        chatBean.setTaskType("Group");
                        taskStatus = status_now;
                    }
                } else {
                    chatBean.setTaskStatus("Unassigned");
                    chatBean.setCatagory("Template");
                    chatBean.setTaskType(taskType);
                    taskStatus = "Unassigned";
                }
            } else {

                chatBean.setTaskReceiver(taskReceiver);
                chatBean.setToUserName(toUserName);
                chatBean.setToUserId(String.valueOf(toUserId));
                chatBean.setCatagory("task");
                chatBean.setSubType("normal");
                if (grouptaskmember() != null) {
                    chatBean.setGroupTaskMembers(grouptaskmember());
                }
            }
            if (project) {
                chatBean.setProjectId(projectId);
                if (category != null && category.equalsIgnoreCase("issue")) {
                    chatBean.setParentTaskId(issueId);
                } else {
                    chatBean.setParentTaskId(parentTaskId);
                }
            }

            if (chatBean.getTaskId() != null && chatBean.getTaskId().equalsIgnoreCase(webtaskId)) {
                if (project) {
                    VideoCallDataBase.getDB(context).update_Project_history(chatBean);
                }
                Log.i("send_status", "isCustomTagVisible ==> " + chatBean.isCustomTagVisible());
                if (VideoCallDataBase.getDB(context).insertORupdate_Task_history(chatBean)) {
                    if (chatBean.isCustomTagVisible()) {
                        Log.i("send_status", "isCustomTagVisible $$ => " + chatBean.isCustomTagVisible());
                        taskList.add(chatBean);
                    }
                    sortTaskMessage();
                }

                refresh();
                listLastposition();
                Log.i("reminder123", "project status in sendMessage=END ====>" + chatBean.getProjectStatus());
            }
            Log.i("estim1234", "=============================");
            Log.i("estim1234", "listOfObservers******" + listOfObservers);
            Log.i("estim1234", "listOfObservers size******" + listOfObservers.size());
            Log.i("estim1234", "=============================");

                /*added for groupAdmin-Observer Start*/

            if (ownerOfTask.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                listOfObservers.clear();
            }

            String groupAdmin_observer = getGroupAdmin_observer_DB();
            if (groupAdmin_observer != null && !groupAdmin_observer.equalsIgnoreCase("") && !groupAdmin_observer.equalsIgnoreCase(null)
                    && groupAdmin_observer.contains(",")) {
                String members_groupAdmin[] = groupAdmin_observer.split(",");
                for (int i = 0; i < members_groupAdmin.length; i++) {
                    if (!members_groupAdmin[i].equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        listOfObservers.add(members_groupAdmin[i]);
                    }
                }
            } else {
                listOfObservers.add(groupAdmin_observer);
            }
            if (taskReceiver!=null && !taskReceiver.equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && !listOfObservers.contains(taskReceiver)) {
                listOfObservers.add(taskReceiver);
            }
            /*added for groupAdmin-Observer End*/

            if (listOfObservers != null && listOfObservers.size() > 0) {
                if (getResources().getString(R.string.proxyua).equalsIgnoreCase("enable")) {
                    String xml = composeChatXML(chatBean);
                    sendMultiInstantMessage(xml, listOfObservers, 1);
                } else {
                    if (listOfObservers.size() == 1) {
                        String xml = composeChatXML(chatBean);
                        sendMultiInstantMessage(xml, listOfObservers, 1);
//                                    sendMultiInstantMessage(xml, listOfObservers, 0);
                    } else {
                        for (String buddy_username : listOfObservers) {
                            Log.i("ListObserver", "buddy_username " + buddy_username);
                            if (!chatBean.getTaskType().equalsIgnoreCase("group")) {
                                Log.i("taskConversation", "private sendMessage * 13 ");
                                chatBean.setToUserName(buddy_username);
                                int ToUserid = VideoCallDataBase.getDB(context).getUserid(buddy_username);
                                Log.i("ListObserver", "buddy_username Id" + ToUserid);
                                chatBean.setToUserId(String.valueOf(ToUserid));
                            }
                        }
                        String xml = composeChatXML(chatBean);

                        if (taskReceiver != null && taskReceiver.equalsIgnoreCase(ownerOfTask)) {
                            if (listOfObservers.contains(taskReceiver) && taskReceiver.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                Log.d("TaskRemovedObserver", "Task Removed  Observer name  5 == " + taskReceiver);
                                listOfObservers.remove(taskReceiver);
                                updateTemplateStatus(chatBean);
                            }
                        }
                        sendMultiInstantMessage(xml, listOfObservers, 1);
                        if (!listOfObservers.contains(taskReceiver)) {
                            listOfObservers.add(taskReceiver);
                        }

//                                    sendMultiInstantMessage(xml, listOfObservers, 0);s
                    }
                }
            } else {
                Log.i("chat", "-----------------> 2 ");
                updateTemplateStatus(chatBean);
            }
            Log.i("chat", "-----------------> 1 ");


        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "sendMessage Exception : " + e.getMessage(), "WARN", null);
        }
    }

    private String getGroupAdmin_observer_DB() {
        String get_groupAdminobserver_query = "select groupAdminobserver from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'and projectId='" + projectId + "'";
        String OraclegroupAdminObserver = VideoCallDataBase.getDB(context).getprojectIdForOracleID(get_groupAdminobserver_query);
        Log.i("observer123", "OraclegroupAdminObserver ====> " + OraclegroupAdminObserver);
        return OraclegroupAdminObserver;
    }

    private String getStatusForNumber(String status) {
        String currentStatus_now = "";
        if (status.equalsIgnoreCase("0")) {
            currentStatus_now = "Started";
        } else if (status.equalsIgnoreCase("1")) {
            currentStatus_now = "Hold";
        } else if (status.equalsIgnoreCase("2")) {
            currentStatus_now = "Resumed";
        } else if (status.equalsIgnoreCase("3")) {
            currentStatus_now = "Paused";
        } else if (status.equalsIgnoreCase("4")) {
            currentStatus_now = "Restarted";
        } else if (status.equalsIgnoreCase("Assigned")) {
            currentStatus_now = "Assigned";
        }
        return currentStatus_now;
    }

    private String getAllProjectMembersList() {
        String project_deassignMems = "";
        String getProjectNameQuery = "select projectName from projectHistory where projectId='" + projectId + "'  and taskId= '" + webtaskId + "'";
        String my_project_name = VideoCallDataBase.getDB(context).getValuesForQuery(getProjectNameQuery);
        String taskMemberList_qry = "select taskMemberList from projectHistory where projectId='" + projectId + "'and taskName = '" + my_project_name + "'";
        String total_members_list = VideoCallDataBase.getDB(context).getValuesForQuery(taskMemberList_qry);
        if (total_members_list != null) {
            int counter = 0;
            for (int i = 0; i < total_members_list.length(); i++) {
                if (total_members_list.charAt(i) == ',') {
                    counter++;
                }
            }
            for (int j = 0; j < counter + 1; j++) {
                if (counter == 0) {
                    if (!total_members_list.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        project_deassignMems = total_members_list;
                    }
                } else {
                    if (total_members_list.split(",")[j].equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                    } else {
                        project_deassignMems = project_deassignMems.concat(total_members_list.split(",")[j] + ",");
                    }
                }
            }
            if (project_deassignMems != null && project_deassignMems.contains(",")) {
                project_deassignMems = project_deassignMems.substring(0, project_deassignMems.length() - 1);
            }
        }
        return project_deassignMems;
    }

    public void getgroupStatus() {
        try {
            showStatusprogress("Loading....");
            Log.d("test1", "appSharedpreferences.getString " + webtaskId);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("taskId", webtaskId));

            Appreference.jsonRequestSender.listGroupTaskUsers(EnumJsonWebservicename.listGroupTaskUsers, nameValuePairs, this);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "getgroupStatus Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void listLastposition() {
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (list_all != null)
                        list_all.setSelection(list_all.getAdapter().getCount() - 1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("NewTaskConversation", "listLastposition Exception : " + e.getMessage(), "WARN", null);
        }
    }


    public void updateTemplateStatus(TaskDetailsBean taskDetailsBean) {
        try {
            if (taskDetailsBean != null) {
                Log.d("template", "status for template ");
                VideoCallDataBase.getDB(context).updateTaskSentStatus(taskDetailsBean.getSignalid());
                for (TaskDetailsBean detailsBean : taskList) {
                    if (detailsBean.getSignalid() != null && detailsBean.getSignalid().equalsIgnoreCase(taskDetailsBean.getSignalid())) {
                        detailsBean.setMsg_status(1);
                        break;
                    }
                }
                refresh();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "updateTemplateStatus Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void gettaskwebservice() {
//        showprogressforpriority("sync task details inprogress...");
        try {
            Log.i(tab, "gettaskwebservice ");
            if (isNetworkAvailable()) {
                showStatusprogress("Sync Task.....");
                Log.i("gettask", "get task webservice " + webtaskId);
                List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(1);
                nameValuePairs1.add(new BasicNameValuePair("taskId", webtaskId));
                Appreference.jsonRequestSender.getTask(EnumJsonWebservicename.getTask, nameValuePairs1, TravelJobDetails.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "gettaskwebservice()  Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void startTravelAlarmManager(String timer, String status_taskId, String currentTaskStatus, String jobCodeNo, String TimerType) {
        SimpleDateFormat datefor = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date enddate = null, dateFor = null, endDate = null;
        String endTime = null;
        int unicid = 0;
        Log.i("alarm123", "startHoldOrPauseAlarmManager started");
        Log.i("alarm123", "startHoldOrPauseAlarmManager timer===>" + timer);
        Log.i("alarm123", "startHoldOrPauseAlarmManager status_taskId===>" + status_taskId);
        Log.i("alarm123", "startHoldOrPauseAlarmManager currentTaskStatus===>" + currentTaskStatus);
        try {
            enddate = datefor.parse(timer);
            Log.i("task", "End.Date " + enddate);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("alarm123", "startSelfAlarmManager Exception: " + e.getMessage(), "WARN", null);
        }
        SimpleDateFormat timeforend = new SimpleDateFormat("HH:mm");
        SimpleDateFormat Datefor = new SimpleDateFormat("yyyy-MM-dd");
        try {
            endTime = timeforend.format(enddate);
            Log.i("alarm123", "End.Time========> " + endTime);
            endDate = Datefor.parse(Datefor.format(enddate));
            Log.i("alarm123", "endDate===========> " + endDate);

        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("alarm123", "timeforend Exception : " + e.getMessage(), "WARN", null);
        }
        try {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, StatusAlarmManager.class);
            unicid = Integer.parseInt(status_taskId);
            intent.putExtra("id", unicid);
            intent.putExtra("endTime", endTime);
            intent.putExtra("currentProjectId", jobCodeNo);
            intent.putExtra("currentStatus", currentTaskStatus);
            intent.putExtra("timerType", TimerType);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, unicid, intent, 0);
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            cal.set(Calendar.HOUR, Integer.parseInt(endTime.split(":")[0]));
            cal.set(Calendar.MINUTE, Integer.parseInt(endTime.split(":")[1]));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                Log.d("alarm123", "above kitkat");
                Log.d("alarm123", "above kitkat cal.getTimeInMillis()==>" + cal.getTimeInMillis());
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                Log.d("alarm123", "below kitkat");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Appreference.printLog("NewTaskConversation", "AlarmManager Exception : " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("NewTaskConversation", "AlarmManager Exception : " + e.getMessage(), "WARN", null);
        }
    }

    private void showStatusprogress(final String message) {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
        try {
            Log.i(tab, "inside show progress--------->");
            if (progress == null) {
                progress = new ProgressDialog(context);
                progress.setCancelable(false);
                progress.setMessage(message);
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setProgress(0);
                progress.setMax(1000);
                progress.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "showStatusprogress() Exception : " + e.getMessage(), "WARN", null);
        }
//            }
//        });
    }

    public void cancelDialog() {
        Log.i(tab, "cancelDialog  ");
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
        try {
            if (progress != null && progress.isShowing()) {
                Log.i(tab, "--progress bar end-----");
                progress.dismiss();
                progress = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "cancelDialog() Exception : " + e.getMessage(), "WARN", null);
        }
//            }
//        });
    }

    public void complete_task_check() {
        try {
            if (OracleStatusList.size() > 0) {
                for (int i = 0; i < OracleStatusList.size(); i++) {
                    if (OracleStatusList.get(i).equalsIgnoreCase("Hold") || OracleStatusList.get(i).equalsIgnoreCase("Paused") || OracleStatusList.get(i).equalsIgnoreCase("Assigned")) {
                        isOracleStatusList = false;
                        break;
                    } else if (OracleStatusList.get(i).equalsIgnoreCase("Started")) {
                        isOracleStatusList = true;
                    } else {
                        isOracleStatusList = true;
                    }
                }
            }
            Log.i("ws123", "id $$--> " + isOracleStatusList);
            Log.i("ws123", "id $$--> " + projectId);
            Log.i("ws123", "id $$--> " + Appreference.loginuserdetails.getId());
            Log.i("ws123", "id $$--> " + webtaskId);
            if (isOracleStatusList) {
                final int travelentry = VideoCallDataBase.getDB(context).CheckTravelEntryDetails("select * from projectStatus where projectId ='" + projectId + "' and taskId = '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL");
                Log.i("conv123", "TravelEntry==>" + travelentry);
                String query = "select * from projectStatus where projectId='" + projectId + "'  and taskId= '" + webtaskId + "' and status = '7'";
                final int count = VideoCallDataBase.getDB(context).getCountForTravelEntry(query);
                if (count != 0) {
                    if (travelentry == 0) {
                        showAlertDialog("Complete Task", "Are You sure want to complete this job " + taskName, context);
                    } else {
                        Toast.makeText(TravelJobDetails.this, "Enter end date and time and then proceed to complete the task.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TravelJobDetails.this, "No StartEndTime Found", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(TravelJobDetails.this, "can't able to complete this task", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "complete_task_check Exception : " + e.getMessage(), "WARN", null);
        }
    }


    private void sendAssignTask_webservice() {
        try {
            Log.i("AssignTask ", "isProjectFromOracle==> " + isProjectFromOracle);
            if (isProjectFromOracle) {
                showStatusprogress("Sending ...");

                ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                if (projectHistory != null) {
                    try {
                        Log.i("ProjectHistory", "inside status projectHistory ========>" + projectHistory.projectDetailsBeans + "size bean " + projectHistory.projectDetailsBeans.size() + "buddayArrayAdapteer==>" + projectHistory.buddyArrayAdapter);

                        if (projectHistory.projectDetailsBeans != null && projectHistory.projectDetailsBeans.size() > 0 && projectHistory.buddyArrayAdapter != null) {

                            ProjectDetailsBean projectDetailsBean = projectHistory.projectDetailsBeans.get(clickPosition);
                            projectDetailsBean.setTaskStatus("Assigned");
                            projectDetailsBean.setTaskReceiver(Appreference.loginuserdetails.getUsername());

                            Log.i("ProjectHistory", "inside status NewTaskConveratio ========>" + projectCurrentStatus);
                            projectHistory.buddyArrayAdapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("TravelJobDetails", "sendAssignTask_webservice buddyArrayAdapter Exception : " + e.getMessage(), "WARN", null);
                    }
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateTime = dateFormat.format(new Date());
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                String dateforrow = dateFormat.format(new Date());
                Log.i("ws123", "inside wservice AssignTask request");
                JSONObject oracleProject_object = new JSONObject();
                JSONObject taskid = new JSONObject();
                TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                try {
                    taskid.put("id", webtaskId);
                    taskDetailsBean.setTaskId(webtaskId);
                    oracleProject_object.put("task", taskid);
                    int getProjOwnerId = VideoCallDataBase.getDB(context).getUserIdForUserName(ownerOfTask);
                    oracleProject_object.put("fromId", getProjOwnerId);
                    taskDetailsBean.setFromUserId(String.valueOf(getProjOwnerId));
                    oracleProject_object.put("projectId", projectId);
                    taskDetailsBean.setProjectId(projectId);
                    oracleProject_object.put("estimatedTravelHours", "");
                    taskDetailsBean.setEstimatedTravel("");
                    taskDetailsBean.setEstimatedActivity("");
                    oracleProject_object.put("estimatedActivityHours", "");

                    JSONArray jsonArray = new JSONArray();
                    JSONObject usersList = new JSONObject();
                    usersList.put("id", String.valueOf(Appreference.loginuserdetails.getId()));
                    jsonArray.put(0, usersList);
                    oracleProject_object.put("listUser", jsonArray);

                    taskDetailsBean.setFromUserName(ownerOfTask);
                    taskDetailsBean.setToUserName(Appreference.loginuserdetails.getUsername());
                    taskDetailsBean.setToUserId(String.valueOf(Appreference.loginuserdetails.getId()));
                    taskDetailsBean.setTaskName(taskName);
                    taskDetailsBean.setTaskNo(task_No);
                    taskDetailsBean.setCatagory("");
                    taskDetailsBean.setIssueId("");
                    taskDetailsBean.setParentId(getFileName());
                    taskDetailsBean.setIsRemainderRequired("");
                    taskDetailsBean.setCompletedPercentage("");
                    taskDetailsBean.setPlannedStartDateTime("");
                    taskDetailsBean.setPlannedEndDateTime("");
                    taskDetailsBean.setRemainderFrequency("");
                    taskDetailsBean.setSignalid(Utility.getSessionID());
                    taskDetailsBean.setDateTime(dateforrow);
                    taskDetailsBean.setSendStatus("0");
                    taskDetailsBean.setTaskStatus("Assigned");
//                taskDetailsBean.setOwnerOfTask(detailsBean.getOwnerOfTask());

                    taskDetailsBean.setTaskType("individual");
                    taskDetailsBean.setTaskPriority("medium");
                    taskDetailsBean.setParentTaskId(parentTaskId);
                    taskDetailsBean.setSubType("normal");
                    taskDetailsBean.setTaskMemberList(projectGroup_Mems);
                    Log.i(tab, "projectGroup_Mems " + projectGroup_Mems);
                    taskDetailsBean.setTaskReceiver(Appreference.loginuserdetails.getUsername());
                    taskDetailsBean.setRemark("");
                    taskDetailsBean.setReminderQuote("");
                    Log.i("ASE", "userName" + Appreference.loginuserdetails.getFirstName() + " last_name" + Appreference.loginuserdetails.getLastName());
//                taskDetailsBean.setTaskDescription("Task Assigned to " + Appreference.loginuserdetails.getFirstName() + Appreference.loginuserdetails.getLastName());
                    taskDetailsBean.setTaskDescription("Task Assigned to " + Appreference.loginuserdetails.getUsername());
                    taskDetailsBean.setRepeatFrequency("");
                    taskDetailsBean.setTaskTagName("");
                    taskDetailsBean.setTaskUTCDateTime(dateforrow);
                    taskDetailsBean.setMimeType("assigntask");
                    taskDetailsBean.setCatagory("Task");
                    taskDetailsBean.setCustomTagVisible(true);

                    toUserName = Appreference.loginuserdetails.getUsername();
                    taskReceiver = Appreference.loginuserdetails.getUsername();
                    taskType = "individual";
                    category = "Task";
                    taskStatus = "Assigned";
                    toUserId = Appreference.loginuserdetails.getId();
                    Appreference.jsonRequestSender.OracleAssignTask(EnumJsonWebservicename.assignTask, oracleProject_object, taskDetailsBean, TravelJobDetails.this);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("TravelJobDetails", "sendAssignTask_webservice OracleAssignTask Exception : " + e.getMessage(), "WARN", null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "sendAssignTask_webservice Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void gettaskmembers(String project_Id) {
        try {
            Log.i(tab, "project_Id # ==> " + project_Id);
            String projectMembers = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskMemberList from projectHistory where projectId ='" + project_Id + "' and parentTaskId==taskId");

            if (projectMembers != null) {
                int counter_1 = 0;
                for (int i = 0; i < projectMembers.length(); i++) {
                    if (projectMembers.charAt(i) == ',') {
                        counter_1++;
                    }
                }
                for (int j = 0; j < counter_1 + 1; j++) {
                    if (counter_1 == 0) {
                        if (!listOfObservers.contains(projectMembers)) {
                            listOfObservers.add(projectMembers);
                        }
                    } else {
                        if (!listOfObservers.contains(projectMembers.split(",")[j])) {
                            listOfObservers.add(projectMembers.split(",")[j]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "gettaskmembers Exception : " + e.getMessage(), "WARN", null);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
            if (projectHistory != null)
                projectHistory.setProgressBarInvisible();
            finish();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "onBackPressed Exception : " + e.getMessage(), "WARN", null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Log.i(tab, "onResume ");
            showNetworkStateUI();
            if (Appreference.main_Activity_context.openPinActivity) {
                Appreference.main_Activity_context.reRegister_onAppResume();
            }
            startTimer();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "onResume Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = null;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().getApplicationContext().CONNECTIVITY_SERVICE);
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "isNetworkAvailable Exception : " + e.getMessage(), "WARN", null);
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onDateChanged(Calendar c) {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == 105) {
                    try {
                        TaskDetailsBean taskDetailsBean = (TaskDetailsBean) data.getSerializableExtra("taskBean");
                        String RemoveUser = data.getStringExtra("taskRemover");
                        String isProject = data.getStringExtra("isProject");
                        if (isProject != null && isProject.equalsIgnoreCase("Yes")) {
                            if (isProjectFromOracle) {
                                Log.d("output123", "task isProjectFromOracle inside  == " + isProjectFromOracle);
                                taskAssign(taskDetailsBean, RemoveUser, taskDetailsBean.getTaskReceiver(), 1);
                            }
                        }

                        try {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    ll_2.setVisibility(View.GONE);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("TravelJobDetails", "onActivityResult runOnUiThread Exception : " + e.getMessage(), "WARN", null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("TravelJobDetails", "onActivityResult 105 Exception : " + e.getMessage(), "WARN", null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TravelJobDetails", "onActivityResult Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void showToast(final String msg) {
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TravelJobDetails.this, msg, Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("NewTaskConversation", "showToast() Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void showAlertDialog(final String title, String message,
                                final Context context) {
        if (alertDialog != null && alertDialog.isShowing()) {
            // A dialog is already open, wait for it to be dismissed, do nothing
        } else {
            alertbox = new AlertDialog.Builder(context);
            alertbox.setMessage(message);
            alertbox.setTitle(title);
           /* alertbox.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    alertDialog.dismiss();
                }
            });*/
            alertbox.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    sendStatus_webservice("5", "", "", "Completed", "Completed");
                    dialog.dismiss();
                }
            });
            alertbox.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });


            alertDialog = alertbox.create();
            alertDialog.show();
        }
    }

    @Override
    public void ResponceMethod(Object object) {
        CommunicationBean communicationBean = (CommunicationBean) object;
        final String server_Response_string = communicationBean.getEmail();
        Log.d(tab, "Response Email" + server_Response_string);
        String WebServiceEnum_Response = communicationBean.getFirstname();
        boolean isAssigned = false;
        if (WebServiceEnum_Response != null && WebServiceEnum_Response.equalsIgnoreCase(("taskStatus"))) {
            Log.i(tab, "NewTaskConverstaion taskStatus ResponceMethod");
            Log.i("reminder123", "taskStatus response=====>");
            try {
                final JSONObject jsonObject = new JSONObject(communicationBean.getEmail());
                if (((String) jsonObject.get("result_text")).equalsIgnoreCase("task started")) {
                    projectCurrentStatus = "start";
                } else if (((String) jsonObject.get("result_text")).equalsIgnoreCase("task completed")) {
                    projectCurrentStatus = "Completed";
                } else if (((String) jsonObject.get("result_text")).equalsIgnoreCase("task draft")) {
                    projectCurrentStatus = "DeAssign";
                }
                TaskDetailsBean detailsBean = new TaskDetailsBean();
                detailsBean = communicationBean.getTaskDetailsBean();

                detailsBean.setMimeType("text");
                detailsBean.setCustomTagVisible(true);
                Log.i("taskStatus", "Signalid ==> # " + detailsBean.getSignalid());
                VideoCallDataBase.getDB(context).taskWSStatusUpdateINStatus(detailsBean.getSignalid(), "1");
                PercentageWebService("text", detailsBean.getTaskDescription(), "", detailsBean.getSignalid(), 0, detailsBean);
                try {
                    if (detailsBean.getCustomerRemarks() != null && !detailsBean.getCustomerRemarks().equalsIgnoreCase("") && !detailsBean.getCustomerRemarks().equalsIgnoreCase("null")) {
                        if ((detailsBean.getProjectStatus() != null && !detailsBean.getProjectStatus().equalsIgnoreCase("5"))
                                && (detailsBean.getProjectStatus() != null && !detailsBean.getProjectStatus().equalsIgnoreCase("10"))) {
                            PercentageWebService("text", detailsBean.getCustomerRemarks(), "", Utility.getSessionID(), 0, null);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("TravelJobDetails", "ResponceMethod Remarks Exception : " + e.getMessage(), "WARN", null);
                }

                try {
                    if (travel_date_details != null && travel_date_details.size() > 0) {
                        int sec = 0;
                        for (final String travel : travel_date_details) {
                            sec = sec + 2000;
                            final TaskDetailsBean finalDetailsBean = detailsBean;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    PercentageWebService("text", travel, "", Utility.getSessionID(), 0, finalDetailsBean);
                                }
                            }, sec);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("TravelJobDetails", "ResponceMethod travel date Exception : " + e.getMessage(), "WARN", null);
                }
                refresh();
                try {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            travel_job.setEnabled(true);
                            if (projectCurrentStatus != null && projectCurrentStatus.equalsIgnoreCase("Completed")) {
                                status_job.setVisibility(View.GONE);
                                travel_job.setVisibility(View.GONE);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("TravelJobDetails", "ResponceMethod runOnUiThread complete Exception : " + e.getMessage(), "WARN", null);
                }

                if (detailsBean.getProjectStatus() != null && detailsBean.getProjectStatus().equalsIgnoreCase("9")) {
                    Appreference.isEstimPositiveAlertShown = false;
                    ShowEstimWishAlert(projectId, webtaskId);
                }

                if (isDeassign) {
                    try {
                        isDeassign = false;

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                                if (projectHistory != null) {
                                    Log.i("conv123", "isDeassign ProgressBarInvisible  projectHistory not null....===>");
                                    projectHistory.setProgressBarInvisible();
                                }
                                TravelJobDetails.this.finish();
                            }
                        }, 5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("TravelJobDetails", "ResponceMethod isdeassign Exception : " + e.getMessage(), "WARN", null);
                    }
                }
                cancelDialog();
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("TravelJobDetails", "ResponceMethod taskstatus Exception : " + e.getMessage(), "WARN", null);
                Log.i("output123", "NewTaskConv sip responce a jsonobject Exception*******" + e);
            }
        } else if (WebServiceEnum_Response != null && WebServiceEnum_Response.equalsIgnoreCase(("assignTask"))) {
            Log.i(tab, "NewTaskConv AssignTask Responce Received" + server_Response_string);
            try {
                isAssigned = true;
                TaskDetailsBean detailsBean1 = new TaskDetailsBean();
                detailsBean1 = communicationBean.getTaskDetailsBean();
//                ll_2.setVisibility(View.GONE);
                // db insert method
                VideoCallDataBase.getDB(context).insertORupdateStatus(detailsBean1);
                listOfObservers.clear();
                String project_id = detailsBean1.getProjectId();
                Log.i(tab, "project_id ==> " + project_id);
                gettaskmembers(project_id);
                Log.i(tab, "listOfObservers-->>>> " + listOfObservers.size());
//                listOfObservers.add(ownerOfTask);
                if (project) {
                    VideoCallDataBase.getDB(context).updateaccept("update taskDetailsInfo set mimeType='dates' where taskid='" + detailsBean1.getTaskId() + "' and mimeType='date' and duration!='' and durationunit!=''");
                    VideoCallDataBase.getDB(context).updateaccept("update taskDetailsInfo set taskStatus='Assigned' where taskid='" + detailsBean1.getTaskId() + "'");
                }
                if (oracleProjectOwner != null && !oracleProjectOwner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                    if (isTemplate || isAssigned) {
                        isTemplate = false;
                        try {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    ll_2.setVisibility(View.GONE);
                                    status_job.setVisibility(View.VISIBLE);
                                    travel_job.setVisibility(View.VISIBLE);
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("TravelJobDetails", "ResponceMethod assignTask runOnUiThread Exception : " + e.getMessage(), "WARN", null);
                        }
                    }
                }
                try {
                    PercentageWebService("assigntask", "Task Assigned to " + Appreference.loginuserdetails.getUsername(), "", Utility.getSessionID(), 0, null);
                    Self_assign = false;
                    refresh();
                    cancelDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("TravelJobDetails", "ResponceMethod assignTask webservice Exception : " + e.getMessage(), "WARN", null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("TravelJobDetails", "ResponceMethod assignTask Exception : " + e.getMessage(), "WARN", null);
            }
        } else if ((WebServiceEnum_Response != null && WebServiceEnum_Response.equalsIgnoreCase("taskConversationEntry"))) {
            try {
                Log.i("response", "Notes  15 ");
                Log.i("reminder123", "taskConversationEntry response=====>");

                //Get Response for all text message
                TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                taskDetailsBean = communicationBean.getTaskDetailsBean();
                Log.i("status123", "taskConversationEntry Response getProjectStatus***********" + taskDetailsBean.getProjectStatus());

                VideoCallDataBase.getDB(context).taskWSStatusUpdate(taskDetailsBean.getSignalid(), "1");
                if (communicationBean.getTaskDetailsBean() != null) {

                    if (communicationBean.getTaskDetailsBean().getFromUserName().equalsIgnoreCase(communicationBean.getTaskDetailsBean().getToUserName())) {
                        updateTemplateStatus(taskDetailsBean);
                    }
                    if (communicationBean.getTaskDetailsBean().getTaskDescription() != null) {
                        sendMessage(communicationBean.getTaskDetailsBean().getTaskDescription(), null, communicationBean.getTaskDetailsBean().getMimeType(), null, "",
                                communicationBean.getTaskDetailsBean().getSignalid(), communicationBean.getTaskDetailsBean());
                    }

                }
                   /*code for estimTimer*/

                if (taskDetailsBean.getProjectStatus() != null && taskDetailsBean.getProjectStatus().equalsIgnoreCase("0")) {
                    /*String AlarmRingedUpdateQuery = "update taskDetailsInfo set taskPlannedLatestEndDate='11' where projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
                    Log.i("tone123", "updateSnoozeTime_query***********" + AlarmRingedUpdateQuery);
                    VideoCallDataBase.getDB(context).updateaccept(AlarmRingedUpdateQuery);*/
                    String EstimatedTimeQuery = "update taskDetailsInfo set estimCompletion='0' where projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
                    Log.i("tone123", "updateSnoozeTime_query***********" + EstimatedTimeQuery);
                    VideoCallDataBase.getDB(context).updateaccept(EstimatedTimeQuery);
                }
                if (taskDetailsBean.getProjectStatus() != null) {

                    Log.i("estim1234", "task started response ************");
                    String taskQuery = "select * from taskDetailsInfo where projectId='" + projectId + "'and taskId= '" + webtaskId + "' and estimCompletion='1'";
                    int getEstimatedTimerCompleted = VideoCallDataBase.getDB(context).getCountForTravelEntry(taskQuery);
                    Log.i("estim1234", "getEstimatedTimerCompleted  query************" + taskQuery);

                    Log.i("estim1234", "task started response getEstimatedTimerCompleted count************" + getEstimatedTimerCompleted);

                    if (getEstimatedTimerCompleted == 0) {
                        startTravelAlarmManager(Appreference.Estim_travel_TimerValue, webtaskId, taskDetailsBean.getTaskStatus(), taskDetailsBean.getProjectId(), "EstimTimer");
                        Show_travel_EstimTimerDisplay();
                    }
                }

                    /*code started for EstimTimer :positive Alert after DB Entry*/
                if (taskDetailsBean.getProjectStatus() != null && taskDetailsBean.getProjectStatus().equalsIgnoreCase("9")) {
//                    ShowEstimWishAlert(projectId, webtaskId);
                    String taskQuery = "select EstimAlarm from taskDetailsInfo where (taskDescription='Task is Started') and projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
                    String getEstimetedTimeAlertShownOrNot = VideoCallDataBase.getDB(context).getAlertShownstatus(taskQuery, "EstimAlarm");

                    Log.i("estimtone123", "getEstimetedTimeAlertShownOrNot taskQuery==>" + taskQuery);
                    Log.i("estimtone123", "getEstimetedTimeAlertShownOrNot ==>" + getEstimetedTimeAlertShownOrNot);


                    if ((getEstimetedTimeAlertShownOrNot != null && !getEstimetedTimeAlertShownOrNot.equalsIgnoreCase("")
                            && !getEstimetedTimeAlertShownOrNot.equalsIgnoreCase(null) && getEstimetedTimeAlertShownOrNot.equalsIgnoreCase("1"))) {

                    /*update query : set alarm toned*/
                        String AlarmRingedUpdateQuery = "update taskDetailsInfo set EstimAlarm='0' where projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
                        Log.i("estimtone123", "updateSnoozeTime_query***********" + AlarmRingedUpdateQuery);
                        VideoCallDataBase.getDB(context).updateaccept(AlarmRingedUpdateQuery);

                /*update Query : set Task completed before the EstimatedHours*/
                        String EstimatedTimeQuery = "update taskDetailsInfo set estimCompletion='1' where (taskDescription='Task is Started')and projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
                        Log.i("tone123", "updateSnoozeTime_query***********" + EstimatedTimeQuery);
                        VideoCallDataBase.getDB(context).updateaccept(EstimatedTimeQuery);
                    }
                }

                /*code ended for estimTimer*/
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("TravelJobDetails", "ResponceMethod taskConversationEntry Exception : " + e.getMessage(), "WARN", null);
            }
        } else if (WebServiceEnum_Response != null && WebServiceEnum_Response.equalsIgnoreCase("getTask")) {
            try {
                Log.i(tab, "Notes  31 ");
                Log.i(tab, "getTask Got W/S Response ");

                String test1 = server_Response_string.toString();
                Gson gson = new Gson();
                ListAllgetTaskDetails listAllgetTaskDetails = gson.fromJson(test1, ListAllgetTaskDetails.class);
                VideoCallDataBase.getDB(context).insertORupdate_ListAllgetTaskDetails(listAllgetTaskDetails);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        projectBackgroundProcess();
                    }
                }, 800);

                if (WebServiceEnum_Response.equalsIgnoreCase("getTask")) {
                    Log.i(tab, "appSharedpreferences.saveBoolean 1");
                    appSharedpreferences.saveBoolean("syncTask" + webtaskId, true);
                }
                ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                if (projectHistory != null)
                    projectHistory.setProgressBarInvisible();
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                Appreference.printLog("TravelJobDetails", "ResponceMethod getTask Exception : " + e.getMessage(), "WARN", null);
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("TravelJobDetails", "ResponceMethod getTask Exception : " + e.getMessage(), "WARN", null);
            }
        } else if (WebServiceEnum_Response != null && WebServiceEnum_Response.equalsIgnoreCase(("listGroupTaskUsers"))) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        JsonElement jelement = new JsonParser().parse(server_Response_string);
                        JsonArray jarray = jelement.getAsJsonArray();
                        Log.i("GroupPercentage", "jarray size====> " + jarray.size());
                        OracleStatusList.clear();
                        if (jarray.size() > 0) {
                            for (int i = 0; i < jarray.size(); i++) {
                                String jobject1 = jarray.get(i).toString();
                                JSONObject jobject = new JSONObject(jobject1);
                                String username = jobject.getString("username");
                                String percentagecomplete = String.valueOf(jobject.get("percentageCompleted"));
                                String oracleStatus = String.valueOf(jobject.get("oracleStatus"));
                                Log.i("GroupPercentage-->", percentagecomplete);
                                Log.i("GroupPercentage-->", oracleStatus);
                                Log.i("GroupPercentage-->", username);
                                OracleStatusList.add(oracleStatus);
                                Log.i("GroupPercentage", "OracleStatusList ====> " + OracleStatusList.size());
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "No Result Found...", Toast.LENGTH_SHORT).show();
                        }
                        cancelDialog();
                        complete_task_check();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Unable to Login...", Toast.LENGTH_SHORT).show();
                        Appreference.printLog("TravelJobDetails", "ResponceMethod listGroupTaskUsers Exception : " + e.getMessage(), "WARN", null);
                    }
                }
            });
        } else {
            try {
                cancelDialog();
                Log.i(tab, "unhandleresponse " + WebServiceEnum_Response);
                Toast.makeText(getApplicationContext(), WebServiceEnum_Response, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("TravelJobDetails", "ResponceMethod else Exception : " + e.getMessage(), "WARN", null);
            }
        }
    }

    private void Show_travel_EstimTimerDisplay() {
        Log.i("estim1234", "=================================");
        Log.i("estim1234", "Show_travel_EstimTimerDisplay");
        Log.i("estim1234", "=================================");
        String enddate;
        ArrayList<TaskDetailsBean> getTimeBean = new ArrayList<>();
        String getTimerdetailsQuery = "select * from taskDetailsInfo where (taskDescription='Task is Started') and (loginuser='" + Appreference.loginuserdetails.getEmail() + "') and (taskId='" + webtaskId + "')";
        Log.i("estim1234", "Reminder Timer query " + getTimerdetailsQuery);
        getTimeBean = VideoCallDataBase.getDB(context).getTimerDateForHoldOrPause(getTimerdetailsQuery);
        Log.i("estim1234", "getTimeBean size" + getTimeBean.size());
        if (getTimeBean.size() > 0) {
            final TaskDetailsBean MyTimerBean = getTimeBean.get(getTimeBean.size() - 1);
            Log.i("estim1234", "MyTimerBean.getEstimTimeForTimer()" + MyTimerBean.getEstimTimeForTimer());

            if (MyTimerBean.getEstimTimeForTimer() != null) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Log.d("estim1234", "Current date " + c.getTime());
                String formattedDate = df.format(c.getTime());
                Log.d("estim1234", "Formatted current date " + formattedDate);
                enddate = MyTimerBean.getEstimTimeForTimer();
                Log.i("estim1234", "End Date from DB " + MyTimerBean.getEstimTimeForTimer());
                try {
                    String query = "select status from projectStatus where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "'";
                    int timer_Alert_by_current_status = VideoCallDataBase.getDB(context).getCurrentStatus(query);
                    Log.i("estim1234", "timer_Alert_by_current_status DB " + timer_Alert_by_current_status);

                    if (enddate != null && !enddate.equals(" ") && timer_Alert_by_current_status != 1 && timer_Alert_by_current_status != 3) {
                        Date date2 = df.parse(enddate);
                        Date date1 = df.parse(formattedDate);
                        Log.i("estim1234", "date" + date2 + " " + date1);
                        Log.i("estim1234", "datetime" + date2.getTime() + " " + date1.getTime());
                        final long mills = date2.getTime() - date1.getTime();
                        final long seconds = 1000;
                        if (Appreference.isTravelRem_time && estimTravelCounter != null) {
                            estimTravelCounter.cancel();
                            Appreference.isTravelRem_time = false;
                        }
                        Log.i("estim1234", "mills before " + mills);
                        Log.i("estim1234", "seconds before " + seconds);
                        if (mills > 0) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    estimTravelCounter = null;
                                    Log.i("estim1234", "StartTimer");
                                    estimTravelCounter = new CounterTravelClass(mills, seconds);
                                    estimTravelCounter.start();
                                }
                            });
                        } else {
                            Estim_Travel_Timeup_Alert_Show();
                        }
                        Log.d("estim1234", "counter started");
                    }
                } catch (Exception esx) {
                    esx.printStackTrace();
                    Appreference.printLog("NewTaskConversation", "ShowHoldOrPauseTimerDisplay() Exception : " + esx.getMessage(), "WARN", null);
                }
            }
        }
    }

    private void Estim_Travel_Timeup_Alert_Show() {
        try {
            String query = "select status from projectStatus where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "'";
            int timer_Alert_by_current_status = VideoCallDataBase.getDB(context).getCurrentStatus(query);
            Log.i("estimtone123", "timer_Alert_by_current_status ==>" + timer_Alert_by_current_status);

//            if (timer_Alert_by_current_status != 1 || timer_Alert_by_current_status != 3) {

            String taskQuery = "select EstimAlarm from taskDetailsInfo where (taskDescription='Task is Started') and projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
            String getEstimetedTimeAlertShownOrNot = VideoCallDataBase.getDB(context).getAlertShownstatus(taskQuery, "EstimAlarm");

            Log.i("estimtone123", "getEstimetedTimeAlertShownOrNot taskQuery==>" + taskQuery);
            Log.i("estimtone123", "getEstimetedTimeAlertShownOrNot ==>" + getEstimetedTimeAlertShownOrNot);


            if ((getEstimetedTimeAlertShownOrNot != null && !getEstimetedTimeAlertShownOrNot.equalsIgnoreCase("")
                    && !getEstimetedTimeAlertShownOrNot.equalsIgnoreCase(null) && getEstimetedTimeAlertShownOrNot.equalsIgnoreCase("1"))) {

                    /*update query : set alarm toned*/
                String AlarmRingedUpdateQuery = "update taskDetailsInfo set EstimAlarm='0' where projectId='" + projectId + "'and taskId= '" + webtaskId + "'";
                Log.i("estimtone123", "updateSnoozeTime_query***********" + AlarmRingedUpdateQuery);
                VideoCallDataBase.getDB(context).updateaccept(AlarmRingedUpdateQuery);


                MainActivity.startAlarmRingTone();
                String get_OracleprojectId_query = "select oracleProjectId from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'and projectId='" + projectId + "'";
                String OracleIdForProjectId = VideoCallDataBase.getDB(context).getprojectIdForOracleID(get_OracleprojectId_query);
                Intent intent = new Intent(TravelJobDetails.this, ShowEstimTimeupAlert.class);
                intent.putExtra("projectId", projectId);
                intent.putExtra("taskId", webtaskId);
                intent.putExtra("OracleprojectId", OracleIdForProjectId);
                intent.putExtra("OracletaskId", getOracleTaskIdForProjectId());
                startActivity(intent);
            }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getOracleTaskIdForProjectId() {
        String quryActivity1 = "select oracleTaskId from projectHistory where projectId='" + projectId + "' and taskId= '" + webtaskId + "'";
        String oracleTaskId = VideoCallDataBase.getDB(getApplication()).getProjectParentTaskId(quryActivity1);
        return oracleTaskId;
    }

    @Override
    public void ErrorMethod(Object object) {
        try {
            CommunicationBean bean = (CommunicationBean) object;
            if (bean != null && bean.getTaskDetailsBean() != null) {
                final TaskDetailsBean taskDetailsBean = bean.getTaskDetailsBean();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        VideoCallDataBase.getDB(context).taskWSStatusUpdateINStatus(taskDetailsBean.getSignalid(), "000");
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("NewTaskConversation", "ErrorMethod Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void startTimer() {
        try {
            if (timer == null) {
                //set a new Timer
                timer = new Timer();
                //initialize the TimerTask's job
                initializeTimerTask();
                //schedule the timer, after the first 15000ms the TimerTask will run every 15000ms
                timer.schedule(timerTask, 30000, 30000);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("NewTaskConversation", "startTimer Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void stoptimertask() {
        try {
            //stop the timer, if it's not already null
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("NewTaskConversation", "stoptimertask Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void initializeTimerTask() {

        try {
            timerTask = new TimerTask() {
                public void run() {
                    //use a handler to run a toast that shows the current timestamp
                    handler.post(new Runnable() {
                        public void run() {
                            //get the current timeStamp
                            try {
                                String query = "select * from taskDetailsInfo where taskId='" + webtaskId + "' and msgstatus='" + 0 + "' and wssendstatus='1' and fromUserId='" + Appreference.loginuserdetails.getId() + "'";
                                ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                Log.i("Timertask", "resend_webservice " + taskDetailsBeans_list.size());
                                for (TaskDetailsBean detailsBean : taskDetailsBeans_list) {
                                    VideoCallDataBase.getDB(context).updateTaskSentStatus(detailsBean.getSignalid(), "0");
                                    Log.i("Timertask", "mimeType " + detailsBean.getMimeType());
                                    if (detailsBean.getMimeType() != null && !detailsBean.getMimeType().equalsIgnoreCase("date")) {
                                        sendMessage(detailsBean.getTaskDescription(), null, detailsBean.getMimeType(), null, "", detailsBean.getSignalid(), null);
                                    }
                                }

                                String query1 = "select * from projectStatus where taskId='" + webtaskId + "' and wssendstatus='000' and userId='" + Appreference.loginuserdetails.getId() + "'";
                                ArrayList<TaskDetailsBean> taskDetailsBeans_list1 = VideoCallDataBase.getDB(context).getofflinesendlist(query1);
                                Log.i("Timertask", "resend_webservice 1 " + taskDetailsBeans_list1.size());
                                for (TaskDetailsBean detailsBean : taskDetailsBeans_list1) {
                                    VideoCallDataBase.getDB(context).updateTaskSentStatus(detailsBean.getSignalid(), "0");
                                    Log.i("Timertask", "mimeType 1 " + detailsBean.getMimeType());
                                    String newQuery = null;
                                    if (detailsBean.getProjectId() != null && !detailsBean.getProjectId().equalsIgnoreCase("")) {
                                        newQuery = "select * from projectHistory where taskId='" + detailsBean.getTaskId() + "'";
                                    }
                                    ArrayList<TaskDetailsBean> taskDetailsBeans_list11 = VideoCallDataBase.getDB(context).getTaskHistoryInfo(newQuery);
                                    Log.i("Timertask", "newQuery ## " + newQuery + " size is  " + taskDetailsBeans_list11.size());
                                    TaskDetailsBean taskDetailsBean = taskDetailsBeans_list11.get(0);
                                    Log.i("Timertask", "OwnerOfTask ## " + taskDetailsBean.getOwnerOfTask() + " TaskReceiver " + taskDetailsBean.getTaskReceiver());
                                    detailsBean.setOwnerOfTask(taskDetailsBean.getOwnerOfTask());
                                    detailsBean.setTaskReceiver(taskDetailsBean.getTaskReceiver());
                                    detailsBean.setTaskObservers(taskDetailsBean.getTaskObservers());
                                    resendstatus_MessageInwebservice(detailsBean);

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("NewTaskConversation", "initializeTimerTask Exception : " + e.getMessage(), "WARN", null);
                            }
                        }
                    });
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("NewTaskConversation", "initializeTimerTask Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void resendstatus_MessageInwebservice(TaskDetailsBean bean) {
        try {
            TaskDetailsBean machineDetailsBean;
            String desc_query = "Select * from projectHistory where projectId ='" + bean.getProjectId() + "' and taskId = '" + bean.getTaskId() + "'";
            machineDetailsBean = VideoCallDataBase.getDB(MainActivity.mainContext).getDetails_to_complete_project(desc_query);
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
            if (bean.getTravelStartTime() != null && !bean.getTravelStartTime().equalsIgnoreCase("")) {
                try {
                    date = dateParse.parse(bean.getTravelStartTime());
                    StartDateUTC = dateFormat.format(date);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("OfflineSendMessage", "detailsBean.getTravelStartTime() parse Exception : " + e.getMessage(), "WARN", null);
                }
            }
            if (bean.getTravelEndTime() != null && !bean.getTravelEndTime().equalsIgnoreCase("")) {
                try {
                    date1 = dateParse.parse(bean.getTravelEndTime());
                    EndDateUTC = dateFormat.format(date1);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("OfflineSendMessage", "detailsBean.getTravelEndTime() parse Exception : " + e.getMessage(), "WARN", null);
                }
            }
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonObject1 = new JSONObject();
            TaskDetailsBean statusBean = new TaskDetailsBean();
            String tasktime = dateFormat.format(new Date());
            String dateforrow = dateFormat.format(new Date());
            try {
                jsonObject1.put("id", Integer.parseInt(bean.getProjectId()));
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("OfflineSendMessage", "detailsBean.getProjectId() numberformat Exception : " + e.getMessage(), "WARN", null);
            }
            jsonObject.put("project", jsonObject1);
            statusBean.setProjectId(bean.getProjectId());
            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("id", Appreference.loginuserdetails.getId());
            statusBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
            jsonObject.put("from", jsonObject2);
            JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("id", Integer.parseInt(bean.getTaskId()));
            jsonObject.put("task", jsonObject3);
            statusBean.setTaskId(bean.getTaskId());
            statusBean.setSignalid(bean.getSignalid());

            if (bean.getTravelStartTime() != null && !bean.getTravelStartTime().equalsIgnoreCase("")) {
                jsonObject.put("travelStartTime", StartDateUTC);
                jsonObject.put("startDateLatitude", bean.getStartDateLatitude());
                jsonObject.put("startDateLongitude", bean.getStartDateLongitude());
                //                            statusBean.setTravelStartTime(detailsBean.getTravelStartTime());

            } else {
                jsonObject.put("travelStartTime", "");
                jsonObject.put("startDateLatitude", "");
                jsonObject.put("startDateLongitude", "");
            }
            if (bean.getTravelEndTime() != null && !bean.getTravelEndTime().equalsIgnoreCase("")) {
                jsonObject.put("travelEndTime", EndDateUTC);
                jsonObject.put("endDateLatitude", bean.getEndDateLatitude());
                jsonObject.put("endDateLongitude", bean.getEndDateLongitude());
                //                            statusBean.setTravelEndTime(detailsBean.getTravelEndTime());
            } else {
                jsonObject.put("travelEndTime", "");
                jsonObject.put("endDateLatitude", "");
                jsonObject.put("endDateLongitude", "");
            }

          /*  if (bean.getTravelStartTime() != null && !bean.getTravelStartTime().equalsIgnoreCase("")
                    && bean.getEnd_dateStatus() != null && !bean.getEnd_dateStatus().equalsIgnoreCase("")
                    && bean.getEnd_dateStatus().equalsIgnoreCase("9")) {

            } else*/
            if (bean.getTravelStartTime() != null && !bean.getTravelStartTime().equalsIgnoreCase("")) {
                statusBean.setTravelStartTime(bean.getTravelStartTime());
                statusBean.setStartDateLatitude(bean.getStartDateLatitude());
                statusBean.setStartDateLongitude(bean.getStartDateLongitude());
                Log.i("travelcheck123", "start================> added ==> " + bean.getTravelStartTime());
            }
            if (bean.getTravelEndTime() != null && !bean.getTravelEndTime().equalsIgnoreCase("")) {
                statusBean.setTravelEndTime(bean.getTravelEndTime());
                statusBean.setEndDateLatitude(bean.getEndDateLatitude());
                statusBean.setEndDateLongitude(bean.getEndDateLongitude());
                Log.i("travelcheck123", "end================> added ==> " + bean.getTravelStartTime());
            }

            jsonObject.put("activityStartTime", "");
            jsonObject.put("activityEndTime", "");
            jsonObject.put("toTravelStartDateTime", "");
            jsonObject.put("toTravelEndDateTime", "");

            if (bean.getProjectStatus() != null && !bean.getProjectStatus().equalsIgnoreCase("")) {
                if (bean.getEnd_dateStatus() != null && bean.getEnd_dateStatus().equalsIgnoreCase("9")) {
                    jsonObject.put("status", "9");
                    statusBean.setProjectStatus("9");
                } else {
                    jsonObject.put("status", bean.getProjectStatus());
                    statusBean.setProjectStatus(bean.getProjectStatus());
                }
            } else {
                jsonObject.put("status", "");
            }
            jsonObject.put("hourMeterReading", "");
            statusBean.setTaskUTCDateTime(dateforrow);
            statusBean.setDateTime(tasktime);
            statusBean.setTasktime(tasktime);
            statusBean.setTaskUTCTime(dateforrow);
            statusBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
            statusBean.setFromUserName(Appreference.loginuserdetails.getUsername());

            if (bean.getProjectStatus() != null && bean.getProjectStatus().equalsIgnoreCase("7")) {

                String status_info = "select status from projectStatus where projectId='" + bean.getProjectId() + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + bean.getTaskId() + "' and status!='7' and status!= '9'and status!= '10'order by id DESC";
                ArrayList<String> status_all = VideoCallDataBase.getDB(MainActivity.mainContext).getAllCurrentStatus(status_info);
                Log.i("output123", "project CurrentStatus from DB====>" + status_all.size());
                if (status_all.size() > 0) {
                    bean.setProjectStatus(status_all.get(0));
                }
            }
            if (bean.getProjectStatus() != null && (bean.getProjectStatus().equalsIgnoreCase("0"))) {
                statusBean.setTaskStatus("Started");
            } else if (bean.getProjectStatus() != null && bean.getProjectStatus().equalsIgnoreCase("1")) {
                statusBean.setTaskStatus("Hold");
            } else if (bean.getProjectStatus() != null && (bean.getProjectStatus().equalsIgnoreCase("2"))) {
                statusBean.setTaskStatus("Resumed");
            } else if (bean.getProjectStatus() != null && (bean.getProjectStatus().equalsIgnoreCase("3"))) {
                statusBean.setTaskStatus("Paused");
            } else if (bean.getProjectStatus() != null && (bean.getProjectStatus().equalsIgnoreCase("4"))) {
                statusBean.setTaskStatus("Restarted");
            } else if (bean.getProjectStatus() != null && (bean.getProjectStatus().equalsIgnoreCase("5"))) {
                statusBean.setTaskStatus("Completed");
            } else if (bean.getProjectStatus() != null && bean.getProjectStatus().equalsIgnoreCase("8")) {
                statusBean.setTaskStatus("DeAssign");
            }

            String userQuery = "select * from taskDetailsInfo where taskId ='" + bean.getTaskId() + "' and projectId ='" + bean.getProjectId() + "'";
            TaskDetailsBean memberBean = VideoCallDataBase.getDB(MainActivity.mainContext).getUserdetails(userQuery);


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
            statusBean.setMimeType("text");

            statusBean.setPlannedStartDateTime("");
            statusBean.setPlannedEndDateTime("");
            statusBean.setTaskDescription(bean.getTaskDescription());
            Log.i("offlilne", "memberBean.getTaskDescription() ==> " + bean.getTaskDescription());


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

            Appreference.printLog("taskStatus", jsonObject.toString(), "Webservice failure", null);
            Appreference.jsonRequestSender.taskStatus(EnumJsonWebservicename.taskStatus, jsonObject, null, statusBean, TravelJobDetails.this);
        } catch (JSONException e) {

        }

    }

    public  void isCameFromOffline(boolean isofflineData_Exist){
        if (isofflineData_Exist) {
            Log.i("online123", "isofflineData_Exist********"+isofflineData_Exist);

            Appreference.isAlreadyLoadedofflineTravelData=false;
            VideoCallDataBase.getDB(context).DB_converstion_Delete(webtaskId,projectId);
        }

        Log.i("online123", "isCameFromOffline********"+webtaskId);
        if (webtaskId != null) {
            appSharedpreferences.saveBoolean("syncTask" + webtaskId, true);
        }
        gettaskwebservice();
    }

    private class CounterTravelClass extends CountDownTimer {
        String from_userName, date_taskId;

        public CounterTravelClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            Log.i("task", "mills after " + millisInFuture);
            Log.i("task", "seconds after " + countDownInterval);
        }

        @Override
        public void onFinish() {
            Log.i("timer123", "onFinish Timer======>");
            tv_estim_travel_timer.setVisibility(View.GONE);
            Estim_Travel_Timeup_Alert_Show();
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.i("ontick", "ontick");
            try {
                tv_estim_travel_timer.setVisibility(View.VISIBLE);
                long millis = millisUntilFinished;
                Appreference.isTravelRem_time = true;
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                tv_estim_travel_timer.setText(hms);

            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("TravelJobDetails", "CounterClass onTick Exception : " + e.getMessage(), "WARN", null);
            }
        }
    }
}
