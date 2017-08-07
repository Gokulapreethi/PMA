package com.ase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

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
    Context context;
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
    boolean isOracleStatusList;
    private String proxy_user = "proxyua_highmessaging.com";

    public static TravelJobDetails getInstance() {
        if (travelJobDetails == null) {
            travelJobDetails = new TravelJobDetails();
        }
        return travelJobDetails;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.traveldetails_conversation);
        Appreference.context_table.put("traveljobdetails", this);
        handler = new Handler();
        travelJobDetails = this;
        showMy_ID = (ImageView) findViewById(R.id.showMy_ID);
        status_job = (ImageView) findViewById(R.id.status_job);
        travel_job = (ImageView) findViewById(R.id.travel_job);
        drop_down = (ImageView) findViewById(R.id.drop_down);

        cancel = (TextView) findViewById(R.id.cancel);
        head = (TextView) findViewById(R.id.head);
        text11 = (TextView) findViewById(R.id.text11);
        text12 = (TextView) findViewById(R.id.text12);
        text13 = (TextView) findViewById(R.id.text13);
        tv_networksate = (TextView) findViewById(R.id.tv_networksate);
        updatingtask = (TextView) findViewById(R.id.updatingtask);
        item_counter = (TextView) findViewById(R.id.item_counter);
        tv_reassign = (TextView) findViewById(R.id.tv_reassign);
        linear1 = (LinearLayout) findViewById(R.id.linear1);
        header_menu = (LinearLayout) findViewById(R.id.header_menu);
        ll_networkstate = (LinearLayout) findViewById(R.id.ll_networkstate);
        updatingtask_layout = (LinearLayout) findViewById(R.id.updatingtask_layout);
        ll_2 = (RelativeLayout) findViewById(R.id.ll_2);

        progress_updating = (ProgressBar) findViewById(R.id.progress_updating);
        list_all = (ListView) findViewById(R.id.list_all);
        list_all.setAdapter(medialistadapter);
        context = this;
        appSharedpreferences = AppSharedpreferences.getInstance(context);
        custom_1MediaList = new ArrayList<>();
        listOfObservers = new ArrayList<>();
        dataBase = VideoCallDataBase.getDB(context);
        String conversation_In = "";
        OracleStatusList = new ArrayList<String>();
        Log.i(tab, "onCreate");

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

        if (conversation_In != null && conversation_In.equalsIgnoreCase("projectHistory")) {
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
            Log.i(tab, "project_toUsers ## " + projectDetailsBean.getTaskReceiver());
            Log.i(tab, "project_toUsers ## " + projectDetailsBean.getListTaskToUser());
            Log.i(tab, "project_toUsers ## " + projectDetailsBean.getListMemberProject());
            Log.i(tab, "TaskMemberList() ** " + projectDetailsBean.getTaskMemberList());
            Log.i(tab, "OwnerOfTask()() ** " + projectDetailsBean.getOwnerOfTask());
            ownerOfTask = projectDetailsBean.getOwnerOfTask();
            projectId = projectDetailsBean.getId();
            parentTaskId = projectDetailsBean.getParentTaskId();
            taskReceiver = projectDetailsBean.getTaskReceiver();
            category = "task";
            projectGroup_Mems = projectDetailsBean.getTaskMemberList();
            taskStatus = projectDetailsBean.getTaskStatus();
            oracleProjectOwner = projectDetailsBean.getOwnerOfTask();
            if (!appSharedpreferences.getBoolean("syncTask" + webtaskId)) {
                gettaskwebservice();
                Log.i(tab, "templatehistory - Gettaskwebservice called for taskId is " + webtaskId);
            }
            Log.i(tab, "taskStatus==> 1 " + taskStatus);
        } else if (conversation_In != null && conversation_In.equalsIgnoreCase("ProjectTemplateview")) {
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
        }

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
            }else{
                text12.setText(task_giver);
            }
        }
        Log.i(tab, "taskStatus **$  ==> " + taskStatus);
        if (taskStatus != null && (taskStatus.equalsIgnoreCase("draft") || taskStatus.equalsIgnoreCase("Unassigned"))) {
            if(Appreference.loginuserdetails!=null && Appreference.loginuserdetails.getRoleId()!=null
                    && Appreference.loginuserdetails.getRoleId().equalsIgnoreCase("2")
                    && !oracleProjectOwner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                ll_2.setVisibility(View.GONE);
            }else{
                ll_2.setVisibility(View.VISIBLE);
            }
            status_job.setVisibility(View.GONE);
            travel_job.setVisibility(View.GONE);
        } else {
            ll_2.setVisibility(View.GONE);
        }
        Log.i(tab, "oracleProjectOwner **  ==> " + oracleProjectOwner);
        if (taskStatus != null && (!taskStatus.equalsIgnoreCase("draft") && !taskStatus.equalsIgnoreCase("template") && !taskStatus.equalsIgnoreCase("unassigned"))) {
            tv_reassign.setVisibility(View.GONE);
        } else if (oracleProjectOwner != null && oracleProjectOwner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
            tv_reassign.setText("Assign Task");
            status_job.setVisibility(View.GONE);
            travel_job.setVisibility(View.GONE);
        } else {
            tv_reassign.setText("Assign to me");
            Self_assign = true;
        }
        if (isProjectFromOracle && (oracleProjectOwner != null && !oracleProjectOwner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))) {
            if (!isTaskCompleted && (!taskStatus.equalsIgnoreCase("draft") && !taskStatus.equalsIgnoreCase("template") && !taskStatus.equalsIgnoreCase("unassigned"))) {
                Log.i(tab, "isTaskCompleted $ **  ==> " + isTaskCompleted);
                status_job.setVisibility(View.VISIBLE);
                travel_job.setVisibility(View.VISIBLE);
            } else if (isTaskCompleted) {
                Log.i(tab, "isTaskCompleted else  $ **  ==> " + isTaskCompleted);
                status_job.setVisibility(View.GONE);
                travel_job.setVisibility(View.GONE);
            }
        } else if (isProjectFromOracle && (oracleProjectOwner != null && oracleProjectOwner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))) {
            if (!isTaskCompleted && (!taskStatus.equalsIgnoreCase("draft") && !taskStatus.equalsIgnoreCase("template") && !taskStatus.equalsIgnoreCase("unassigned"))) {
                Log.i("desc123", "is template=======> ");
                status_job.setVisibility(View.VISIBLE);
            } else if (isTaskCompleted) {
                status_job.setVisibility(View.GONE);
            }
        }
        if (taskStatus != null && (taskStatus.equalsIgnoreCase("completed") || taskStatus.equalsIgnoreCase("complete"))) {
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
                Log.i(tab, "tv_reassign " + Self_assign);
                if (!Self_assign)
                    addTaskReassignClickEvent();
                else {
                    sendAssignTask_webservice();
                }
            }
        });
        String qury = "select oracleProjectId from projectHistory where projectId='" + projectId + "' and taskId= '" + webtaskId + "'";
        String qury_1 = "select oracleTaskId from projectHistory where projectId='" + projectId + "' and taskId= '" + webtaskId + "'";
        JobCodeNo = VideoCallDataBase.getDB(getApplication()).getProjectParentTaskId(qury);
        ActivityCode = VideoCallDataBase.getDB(getApplication()).getProjectParentTaskId(qury_1);
        Log.i(tab, "JobCodeNo==> " + JobCodeNo);
        Log.i(tab, "ActivityCode==> " + ActivityCode);
        head.setText("Job Card No :" + JobCodeNo + "\nActivity Code :" + ActivityCode);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                if (projectHistory != null)
                    projectHistory.setProgressBarInvisible();
                Appreference.webview_refresh = true;
                Log.i(tab, "back layout  ");
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        showMy_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
        String query = "select status from projectStatus where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "'";
        int disable_by_current_status = VideoCallDataBase.getDB(context).getCurrentStatus(query);
        if (disable_by_current_status == 5)
            isTaskCompleted = true;
        if (isTaskCompleted) {
            status_job.setVisibility(View.GONE);
            travel_job.setVisibility(View.GONE);
        }

        status_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStatusPopupWindow(v);
            }

        });

        travel_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = "select status from projectStatus where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "'";
                int current_status = VideoCallDataBase.getDB(context).getCurrentStatus(query);
                if (current_status == -1) {
//                    travel_job.setEnabled(false);
                    Toast.makeText(TravelJobDetails.this, "The Task has not yet started...", Toast.LENGTH_SHORT).show();
                }else {
                    travel_job.setEnabled(true);
                    showtravelTimePopup(v);
                }
            }

        });
        Project_backgroundProcess();
    }

    public void Project_backgroundProcess() {

        String query_1 = "select * from taskDetailsInfo where (loginuser='" + Appreference.loginuserdetails.getEmail() + "') and (taskStatus!='note' and taskStatus!='draft') and (taskId='" + webtaskId + "') and (projectId='" + projectId + "') and customTagVisible = '1';";

        if (VideoCallDataBase.getDB(context).getTaskHistory(query_1) != null) {
            Log.d(tab, "PJ_BG TASK HISTORY NOT NULL");
            taskList = VideoCallDataBase.getDB(context).getTaskHistory(query_1);
            sortTaskMessage();
            Log.d(tab, "TASK HISTORY List size  = " + taskList.size());
            Log.d(tab, "ownerOfTask " + ownerOfTask);
            Log.d(tab, "taskName " + taskName);
            Log.d(tab, "taskReceiver " + taskReceiver);
            if (taskList.size() > 0) {
                for (TaskDetailsBean taskBean : taskList) {
                    taskBean.setOwnerOfTask(ownerOfTask);
                    taskBean.setTaskReceiver(taskReceiver);
                    taskBean.setTaskName(taskName);
                }
            }

            try {
                medialistadapter = new MediaListAdapter(TravelJobDetails.this, taskList, "task", category, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        list_all.setAdapter(medialistadapter);
        refresh();
    }

    public void projectBackgroundProcess() {
//        cancelDialog();
        Log.i("getTask123","TTT getTask projectBackgroundProcess*************");

        try {
            String query_1 = "select * from taskDetailsInfo where (loginuser='" + Appreference.loginuserdetails.getEmail() + "') and (taskStatus!='note' and taskStatus!='draft') and (taskId='" + webtaskId + "') and (projectId='" + projectId + "') and customTagVisible = '1';";

            if (VideoCallDataBase.getDB(context).getTaskHistory(query_1) != null) {
                taskList = VideoCallDataBase.getDB(context).getTaskHistory(query_1);
                sortTaskMessage();
                if (taskList.size() > 0) {
                    for (TaskDetailsBean taskBean : taskList) {
                        taskBean.setOwnerOfTask(ownerOfTask);
                        taskBean.setTaskReceiver(taskReceiver);
                        taskBean.setTaskName(taskName);
                    }
                }
            }
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void notifyTaskReceived(final TaskDetailsBean taskDetailsBean) {
        Log.i(tab, "notifyTaskReceived 0 ");
        Log.i(tab, "notifyTaskReceived " + taskStatus);
        // signal id check in this method for db insertion
        dbInsertCheckSignalId(taskDetailsBean);
        if (taskDetailsBean.getTaskId().equalsIgnoreCase(webtaskId)) {
            Log.i(tab, "notifyTaskReceived 1 " + taskDetailsBean.getTaskId());
            if (taskDetailsBean.getMimeType() != null && taskDetailsBean.getMimeType().equalsIgnoreCase("assigntask")) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (oracleProjectOwner != null && !oracleProjectOwner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            ll_2.setVisibility(View.GONE);
                            status_job.setVisibility(View.VISIBLE);
                            travel_job.setVisibility(View.VISIBLE);
                        } else {
                            ll_2.setVisibility(View.GONE);
                            status_job.setVisibility(View.VISIBLE);
                            travel_job.setVisibility(View.GONE);
                        }
                    }
                });
            } else if (taskDetailsBean.getSubType() != null && taskDetailsBean.getSubType().equalsIgnoreCase("deassign")) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(Appreference.loginuserdetails!=null && Appreference.loginuserdetails.getRoleId()!=null
                                && Appreference.loginuserdetails.getRoleId().equalsIgnoreCase("2")
                                && !oracleProjectOwner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            ll_2.setVisibility(View.GONE);
                            tv_reassign.setVisibility(View.GONE);
                        } else{
                            ll_2.setVisibility(View.VISIBLE);
                            tv_reassign.setVisibility(View.VISIBLE);
                        }

                        tv_reassign.setText("Assign Task");
                        status_job.setVisibility(View.GONE);
                        travel_job.setVisibility(View.GONE);
                    }
                });
            } else if (taskDetailsBean.getTaskStatus() != null && (taskDetailsBean.getTaskStatus().equalsIgnoreCase("completed") || taskDetailsBean.getTaskStatus().equalsIgnoreCase("complete"))) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ll_2.setVisibility(View.GONE);
                        status_job.setVisibility(View.GONE);
                        travel_job.setVisibility(View.GONE);
                    }
                });
            }
            Log.i("status", "status*** 1 " + taskDetailsBean.getTaskStatus());
            /*if (taskDetailsBean.getTaskStatus() != null && !taskDetailsBean.getTaskStatus().equalsIgnoreCase("")) {
                if (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("Started")) {
                    taskDetailsBean.setProjectStatus("0");
                } else {
                    taskDetailsBean.setProjectStatus(taskDetailsBean.getTaskStatus());
                }
            }*/
            VideoCallDataBase.getDB(context).update_Project_history(taskDetailsBean);
            VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
            VideoCallDataBase.getDB(context).insertORupdateStatus(taskDetailsBean);
            taskList.add(taskDetailsBean);
            sortTaskMessage();
            refresh();
        } else {
            Log.i(tab, "notifyTaskReceived 2 " + webtaskId);
            if (!isSignalidSame) {
                /*if (taskDetailsBean.getTaskStatus() != null && !taskDetailsBean.getTaskStatus().equalsIgnoreCase("")) {
                    if (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("Started")) {
                        taskDetailsBean.setProjectStatus("0");
                    } else {
                        taskDetailsBean.setProjectStatus(taskDetailsBean.getTaskStatus());
                    }
                }*/
                VideoCallDataBase.getDB(context).update_Project_history(taskDetailsBean);
                VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                VideoCallDataBase.getDB(context).insertORupdateStatus(taskDetailsBean);
                Log.i(tab, "notifyTaskReceived 3 " + taskDetailsBean.getTaskStatus());
                Log.i("status", "status*** 1 else " + taskDetailsBean.getTaskStatus());
            }
        }
    }

    public void dbInsertCheckSignalId(TaskDetailsBean taskDetailsBean) {
        String query = "select * from taskDetailsInfo where  (loginuser='" + Appreference.loginuserdetails.getEmail() + "') and (taskId='" + taskDetailsBean.getTaskId() + "');";
        ArrayList<TaskDetailsBean> beanArrayList = VideoCallDataBase.getDB(context).getTaskHistory(query);
        for (TaskDetailsBean taskDetailsBean1 : beanArrayList) {
            if (taskDetailsBean1.getSignalid().equalsIgnoreCase(taskDetailsBean.getSignalid())) {
                isSignalidSame = true;
                break;
            }
        }
    }


    private void showtravelTimePopup(View v) {
        Intent intent = new Intent(TravelJobDetails.this, CustomTravelPickerActivity.class);
        intent.putExtra("taskName", taskName);
        intent.putExtra("projectID", projectId);
        intent.putExtra("isTravel", true);
        intent.putExtra("taskID", webtaskId);
        intent.putExtra("jobcodeno", JobCodeNo);
        intent.putExtra("activitycode", ActivityCode);
        startActivityForResult(intent, 120);
    }

    public void getEnteredTravelTime(String startTime, String EndTime, String status) {
        Log.i(tab, "inside getEnteredTravelTime ========>" + startTime + "==>" + EndTime);

        ActivityStartdate = startTime;
        ActivityEnddate = EndTime;
        if (startTime != null && !startTime.equalsIgnoreCase(""))
            sendStatus_webservice(status, "", "", "", "");
        else
            sendStatus_webservice(status, "", "", "", "");

    }

    private void showStatusPopupWindow(View view) {
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
        }
        popup.getMenuInflater().inflate(R.menu.popup_job_status, popup.getMenu());
        popup.getMenu().getItem(1).setVisible(false);

        String query = "select status from projectStatus where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "'";
        final int current_status = VideoCallDataBase.getDB(context).getCurrentStatus(query);

        Log.i(tab, "project CurrentStatus from DB====>" + current_status);
        if (oracleProjectOwner != null && !oracleProjectOwner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
            if (current_status == -1) {
                popup.getMenu().getItem(0).setVisible(true);
                popup.getMenu().getItem(1).setVisible(false);
                popup.getMenu().getItem(2).setVisible(false);
                popup.getMenu().getItem(3).setVisible(false);
                popup.getMenu().getItem(4).setVisible(false);
                popup.getMenu().getItem(5).setVisible(false);
                popup.getMenu().getItem(6).setVisible(false);
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
                popup.getMenu().getItem(6).setVisible(false);
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
                    sendStatus_webservice("0", "", "", "Started", "start");
                }
                if (item.getTitle().toString().equalsIgnoreCase("Complete")) {
                    Log.i("ws123", "completed $$--> "+taskType);
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
                            AlertDialog.Builder saveDialog = new AlertDialog.Builder(context);
                            saveDialog.setTitle("Complete Task");
                            saveDialog.setCancelable(false);
                            saveDialog.setMessage("Are You sure want to complete this job " + taskName);
                            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (count != 0) {
                                        if (travelentry == 0) {
                                            sendStatus_webservice("5", "", "", "Completed", "complete");
                                        } else {
                                            Toast.makeText(TravelJobDetails.this, "Enter end date and time and then proceed to complete the task.", Toast.LENGTH_SHORT).show();
                                            dialog.cancel();
                                        }
                                    } else {
                                        Toast.makeText(TravelJobDetails.this, "No StartEndTime Found", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }
                                }
                            });
                            saveDialog.setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            saveDialog.show();
                        } else {
                            Toast.makeText(TravelJobDetails.this, "can't able to complete this task", Toast.LENGTH_SHORT).show();

                        }
                    }
                    Log.i("ws123", "OracleStatusList $$--> " + OracleStatusList.size());
                }
                if (item.getTitle().toString().equalsIgnoreCase("DeAssign")) {
                    if (isNetworkAvailable()) {
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
                                Log.i(tab, "remarks for DeAssign====>" + name.getText().toString());
                                if (name.getText().toString() != null && !name.getText().toString().equalsIgnoreCase("")) {
                                    dialog1.dismiss();
                                    sendStatus_webservice("8", "", name.getText().toString(), "DeAssign", "");
                                } else
                                    Toast.makeText(TravelJobDetails.this, "Please enter any Remarks", Toast.LENGTH_SHORT).show();
                            }
                        });
                        no.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog1.dismiss();
                            }
                        });
                        dialog1.show();
                    } else {
                        Toast.makeText(TravelJobDetails.this,"Not available when no internet connection",Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
        popup.show();
    }

    private void sendStatus_webservice(String status, String path, String remarks, String projectCurrentStatus, String statusUI) {
        try {
            showStatusprogress("Sending status...");
            ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
            if (projectHistory != null) {
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
                }
            }
            if (ActivityEnddate != null && !ActivityEnddate.equalsIgnoreCase("")) {
                try {
                    date1 = dateParse.parse(ActivityEnddate);
                    EndDateUTC = dateFormat.format(date1);
                } catch (Exception e) {
                    e.printStackTrace();
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
                            jsonObject.put("travelStartTime", StartDateUTC);
                        } else
                            jsonObject.put("travelStartTime", "");
                    jsonObject.put("travelEndTime", EndDateUTC);
                    taskDetailsBean.setTravelStartTime(ActivityStartdate);
                    taskDetailsBean.setTravelEndTime(ActivityEnddate);
                    travel_date_details = new ArrayList<>();
                    if (ActivityStartdate != null && !ActivityStartdate.equalsIgnoreCase("") && !status.equalsIgnoreCase("9")) {
                        travel_date_details.add("StartTime : " + ActivityStartdate);
                    }
                    if (ActivityEnddate != null && !ActivityEnddate.equalsIgnoreCase("")) {
                        travel_date_details.add("EndTime : " + ActivityEnddate);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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

//            taskDetailsBean.setToUserId(String.valueOf(toUserId));
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
            taskStatus = statusUI;

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
            if (projectGroup_Mems != null) {
                taskDetailsBean.setGroupTaskMembers(projectGroup_Mems);
            }
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
                taskDetailsBean.setTaskDescription(Appreference.loginuserdetails.getUsername() + " Left");
                taskDetailsBean.setSubType("deassign");
            } else if (status.equalsIgnoreCase("7") || status.equalsIgnoreCase("9")) {
                taskDetailsBean.setTaskDescription("Gathering Details...");
            } else {
                taskDetailsBean.setTaskDescription("Task is " + projectCurrentStatus);
            }

            jsonObject.put("hourMeterReading", "");
            listOfObservers.clear();
            if (ownerOfTask != null && !ownerOfTask.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                listOfObservers.add(ownerOfTask);
                project_toUsers = ownerOfTask;
            } else {
                listOfObservers.add(project_toUsers);
            }

            if (status.equalsIgnoreCase("9")) {
                String query = "select * from projectStatus where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and status = '7'";
                TaskDetailsBean bean = VideoCallDataBase.getDB(context).getActivityTimeFromStatus(query);
                if (bean != null) {
                    if (ActivityEnddate != null && !ActivityEnddate.equalsIgnoreCase("")) {
                        String queryUpdate = "update projectStatus set travelEndTime='" + ActivityEnddate + "' where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "' and travelStartTime IS NOT NULL and travelEndTime IS NULL";
                        VideoCallDataBase.getDB(context).updateaccept(queryUpdate);
                    }
                }
            } else {
                VideoCallDataBase.getDB(context).insertORupdateStatus(taskDetailsBean);
            }
            if(isNetworkAvailable()) {
                Appreference.jsonRequestSender.taskStatus(EnumJsonWebservicename.taskStatus, jsonObject, status_list, taskDetailsBean, TravelJobDetails.this);
            }else{
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
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void NOInternet_InsertConversaion(String getMediaType, String getMediaPath, String getExt, String sig_id, int isDateorUpdateorNormal) {
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
            chatBean.setTaskStatus("unassigned");
            taskStatus = "unassigned";
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
    }

    public void PercentageWebService(String getMediaType, String getMediaPath, String getExt, String sig_id, int isDateorUpdateorNormal) {
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
                chatBean.setTaskStatus("unassigned");
                taskStatus = "unassigned";
                chatBean.setTaskReceiver("");
                chatBean.setToUserName("");
                chatBean.setToUserId("");
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
                } else if (projectGroup_Mems != null) {
                    chatBean.setGroupTaskMembers(projectGroup_Mems);
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
            }

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
            Appreference.printLog("NewTaskConversation", "getFileName Exception " + e.getMessage(), "WARN", null);
        }
        return strFilename;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(tab, "onDestory ");
        try {
            handler = null;
            Appreference.context_table.remove("traveljobdetails");
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddTaskReassign onDestroy ", "Exception " + e.getMessage(), "WARN", null);
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

    public String TimeFrequencyCalculation(String timeFrequency) {
        String time = timeFrequency;
        String remainder_Frequency = "", rem_freq_min, rem_frq;
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
        return remainder_Frequency;
    }

    public void updateMessageStatus(TaskDetailsBean taskDetailsBean) {
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

    }


    public void sendMultiInstantMessage(String msgBody, ArrayList<String> userlist, int sendTo) {
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
                }

            }
        }
    }


    public void refresh() {
        try {
            Log.i("getTask123","TTT getTask refresh*************");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (medialistadapter != null) {
                        medialistadapter.notifyDataSetChanged();
                    }
                    Log.i("conv123", "Refresh medialistadapter done-----------");
                }
            });
           /* runOnUiThread(new Runnable() {
                public void run() {
                    if (medialistadapter != null) {
                        medialistadapter.notifyDataSetChanged();
                    }
                    Log.i("NewTaskConversation", "Refresh Handled here");
                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showNetworkStateUI() {
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
            Appreference.printLog("NewTaskConversation", "Exception " + e.getMessage(), "WARN", null);
        }
    }

    public void taskAssign(final TaskDetailsBean taskDetailsBean, String removeUser, String newReceiver, int isProject) {
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
            taskType="group";
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

    }

    public void sortTaskMessage() {
        try {
            Log.i("getTask123","TTT getTask sortTaskMessage*************");

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
            Log.d("Sorting", "Sorting problem");
        }
        refresh();
    }

    public void sendMessage(String message, String pri, final String type1, final String imagename, final String remquotes_2, String sig_id, TaskDetailsBean chatBean) {
        try {
            Log.d("chat", "------sendMessage entry------");
            Log.i("taskConversation", "private sendMessage * 0 ");

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
            chatBean.setTaskMemberList(project_toUsers);

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
            if (chatBean.getSubType() != null && chatBean.getSubType().equalsIgnoreCase("deassign")) {
                Log.i("taskConversation", "isDeassign 1 ===> " + isDeassign);
                Log.i("taskConversation", "taskReceiver  1 ==> " + taskReceiver);
                Log.i("taskConversation", "toUserName  1 ==> " + toUserName);
                Log.i("taskConversation", "toUserId  1 ==> " + toUserId);
                chatBean.setTaskReceiver("");
                chatBean.setToUserName("");
                chatBean.setToUserId("");

                chatBean.setSubType("deassign");
                chatBean.setTaskRequestType("nornal");
                chatBean.setTaskStatus("unassigned");
                chatBean.setCatagory("Template");
                taskStatus = "unassigned";
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
            } else {

                chatBean.setTaskReceiver(taskReceiver);
                chatBean.setToUserName(toUserName);
                chatBean.setToUserId(String.valueOf(toUserId));
                chatBean.setCatagory("task");
                chatBean.setSubType("normal");
            }
            if (project) {
                chatBean.setProjectId(projectId);
                if (category != null && category.equalsIgnoreCase("issue")) {
                    chatBean.setParentTaskId(issueId);
                } else {
                    chatBean.setParentTaskId(parentTaskId);
                }
                if (chatBean.getSubType() != null && chatBean.getSubType().equalsIgnoreCase("deassign")) {
                    Log.i("taskConversation", "project_deassignMems success");
                    chatBean.setGroupTaskMembers(project_deassignMems);
                } else if (projectGroup_Mems != null) {
                    chatBean.setGroupTaskMembers(projectGroup_Mems);
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
            }

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
        }
    }

    public void getgroupStatus() {
        try {
            showStatusprogress("Loading....");
            Log.d("test1", "appSharedpreferences.getString " + webtaskId);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("taskId", webtaskId));
            if (Appreference.jsonRequestSender == null) {
                JsonRequestSender jsonRequestParser = new JsonRequestSender();
                Appreference.jsonRequestSender = jsonRequestParser;
                jsonRequestParser.start();
            }
            Appreference.jsonRequestSender.listGroupTaskUsers(EnumJsonWebservicename.listGroupTaskUsers, nameValuePairs, this);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("GroupPercentageStatus LoadBackGroundWebservice", "Exception " + e.getMessage(), "WARN", null);
        }
    }


    public void updateTemplateStatus(TaskDetailsBean taskDetailsBean) {
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
    }

    public void gettaskwebservice() {
//        showprogressforpriority("sync task details inprogress...");
        Log.i("getTask123","TTT getTask gettaskwebservice*************");
        if(isNetworkAvailable()) {
            showStatusprogress("Sync Task.....");
            Log.i("gettask", "get task webservice " + webtaskId);
            List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(1);
            nameValuePairs1.add(new BasicNameValuePair("taskId", webtaskId));
            Appreference.jsonRequestSender.getTask(EnumJsonWebservicename.getTask, nameValuePairs1, TravelJobDetails.this);
        }
    }

    private void showStatusprogress(final String message) {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
                try {
                    Log.i("expand", "inside show progress--------->");
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
                }
//            }
//        });
    }

    public void cancelDialog() {
            Log.i("getTask123","TTT getTask cancelDialog*************");
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
                    try {
                        if (progress != null && progress.isShowing()) {
                            Log.i("register", "--progress bar end-----");
                            progress.dismiss();
                            Appreference.isRequested_date = false;
                            progress = null;
                        }
                    } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
//                }
//            });
        }

    public void complete_task_check() {
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
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(context);
            saveDialog.setTitle("Complete Task");
            saveDialog.setCancelable(false);
            saveDialog.setMessage("Are You sure want to complete this job " + taskName);
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (count != 0) {
                        if (travelentry == 0) {
                            sendStatus_webservice("5", "", "", "Completed", "complete");
                        } else {
                            Toast.makeText(TravelJobDetails.this, "Enter end date and time and then proceed to complete the task.", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    } else {
                        Toast.makeText(TravelJobDetails.this, "No StartEndTime Found", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                }
            });
            saveDialog.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            saveDialog.show();
        } else {
            Toast.makeText(TravelJobDetails.this, "can't able to complete this task", Toast.LENGTH_SHORT).show();

        }
    }


    private void sendAssignTask_webservice() {
        Log.i("AssignTask ", "isProjectFromOracle==> " + isProjectFromOracle);
        if (isProjectFromOracle) {
            showStatusprogress("Sending ...");

            ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
            if (projectHistory != null) {
                Log.i("ProjectHistory", "inside status projectHistory ========>" + projectHistory.projectDetailsBeans + "size bean " + projectHistory.projectDetailsBeans.size() + "buddayArrayAdapteer==>" + projectHistory.buddyArrayAdapter);

                if (projectHistory.projectDetailsBeans != null && projectHistory.projectDetailsBeans.size() > 0 && projectHistory.buddyArrayAdapter != null) {

                    ProjectDetailsBean projectDetailsBean = projectHistory.projectDetailsBeans.get(clickPosition);
                    projectDetailsBean.setTaskStatus("Assigned");
                    projectDetailsBean.setTaskReceiver(Appreference.loginuserdetails.getUsername());

                    Log.i("ProjectHistory", "inside status NewTaskConveratio ========>" + projectCurrentStatus);
                    projectHistory.buddyArrayAdapter.notifyDataSetChanged();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            Appreference.jsonRequestSender.OracleAssignTask(EnumJsonWebservicename.assignTask, oracleProject_object, taskDetailsBean, TravelJobDetails.this);
        }
    }

    public void gettaskmembers(String project_Id) {
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
        if (projectHistory != null)
            projectHistory.setProgressBarInvisible();
        finish();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(tab, "onResume ");
        showNetworkStateUI();
        if (Appreference.main_Activity_context.openPinActivity) {
            Appreference.main_Activity_context.reRegister_onAppResume();
        }
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("NewTaskConversation", "onActivityResult reassign Exception " + e.getMessage(), "WARN", null);
                }

            }
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
            try {
                final JSONObject jsonObject = new JSONObject(communicationBean.getEmail());
                if (((String) jsonObject.get("result_text")).equalsIgnoreCase("task started")) {
                    projectCurrentStatus = "start";
                } else if (((String) jsonObject.get("result_text")).equalsIgnoreCase("task completed")) {
                    projectCurrentStatus = "completed";
                } else if (((String) jsonObject.get("result_text")).equalsIgnoreCase("task draft")) {
                    projectCurrentStatus = "DeAssign";
                }
                TaskDetailsBean detailsBean = new TaskDetailsBean();
                detailsBean = communicationBean.getTaskDetailsBean();

                detailsBean.setMimeType("text");
                detailsBean.setCustomTagVisible(true);

                PercentageWebService("text", detailsBean.getTaskDescription(), "", detailsBean.getSignalid(), 0);

                if (detailsBean.getCustomerRemarks() != null && !detailsBean.getCustomerRemarks().equalsIgnoreCase("") && !detailsBean.getCustomerRemarks().equalsIgnoreCase("null")) {
                    if (projectCurrentStatus != null && !projectCurrentStatus.equalsIgnoreCase("Completed"))
                        PercentageWebService("text", detailsBean.getCustomerRemarks(), "", Utility.getSessionID(), 0);

                }
                if (travel_date_details != null && travel_date_details.size() > 0) {
                    int sec = 0;
                    for (final String travel : travel_date_details) {
                        sec = sec + 2000;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PercentageWebService("text", travel, "", Utility.getSessionID(), 0);
                            }
                        }, sec);
                    }
                }
                refresh();
                try {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            travel_job.setEnabled(true);
                            if (projectCurrentStatus != null && projectCurrentStatus.equalsIgnoreCase("completed")) {
                                status_job.setVisibility(View.GONE);
                                travel_job.setVisibility(View.GONE);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (isDeassign) {
                    isDeassign = false;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                            if (projectHistory != null) {
                                Log.i("conv123", "isDeassign ProgressBarInvisible  projectHistory not null....===>");
                                projectHistory.setProgressBarInvisible();
                            }
                            TravelJobDetails.this.finish();
                        }
                    });
                }
                cancelDialog();
            } catch (Exception e) {
                e.printStackTrace();
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
                    VideoCallDataBase.getDB(context).updateaccept("update taskDetailsInfo set taskStatus='inprogress' where taskid='" + detailsBean1.getTaskId() + "'");
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
                        }
                    }
                }
                PercentageWebService("assigntask", "Task Assigned to " + Appreference.loginuserdetails.getUsername(), "", Utility.getSessionID(), 0);
                Self_assign = false;
                refresh();
                cancelDialog();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ((WebServiceEnum_Response != null && WebServiceEnum_Response.equalsIgnoreCase("taskConversationEntry"))) {
            Log.i("response", "Notes  15 ");
            //Get Response for all text message
            TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
            taskDetailsBean = communicationBean.getTaskDetailsBean();
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
        } else if (WebServiceEnum_Response != null && WebServiceEnum_Response.equalsIgnoreCase("getTask")) {
            Log.i(tab, "Notes  31 ");
            Log.i("getTask123","TTT getTask Got W/S Response*************");

            String test1 = server_Response_string.toString();
            ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
            if (projectHistory != null)
                projectHistory.setProgressBarInvisible();
            Gson gson = new Gson();
            ListAllgetTaskDetails listAllgetTaskDetails = gson.fromJson(test1, ListAllgetTaskDetails.class);
            VideoCallDataBase.getDB(context).insertORupdate_ListAllgetTaskDetails(listAllgetTaskDetails);
//            Project_backgroundProcess();
            projectBackgroundProcess();
            if (WebServiceEnum_Response.equalsIgnoreCase("getTask")) {
                Log.i(tab, "appSharedpreferences.saveBoolean 1");
                appSharedpreferences.saveBoolean("syncTask" + webtaskId, true);
            }
            cancelDialog();
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
                        Appreference.printLog("GroupPercentageStatus ResponceMethod ", "Exception " + e.getMessage(), "WARN", null);
                    }
                }
            });
        } else {
            cancelDialog();
            Log.i(tab, "unhandleresponse " + WebServiceEnum_Response);
            Toast.makeText(getApplicationContext(),WebServiceEnum_Response,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void ErrorMethod(Object object) {

    }
}