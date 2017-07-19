package com.ase.activity;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.Appreference;
import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;
import com.ase.NewTaskConversation;
import com.ase.R;
import com.ase.RandomNumber.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.app.CallActivity;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.app.MyBuddy;
import org.pjsip.pjsua2.app.MyCall;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.JsonRequestSender;
import json.WebServiceInterface;
import xml.xmlcomposer;

public class SchedulerActivity extends AppCompatActivity implements WebServiceInterface {

    private boolean call_maker = false;
    private boolean isReassign_task = false;
    private boolean isObserver_task = false;
    private boolean isBuzz_task = false;
    private boolean isAband_task = false;
    private String task_type = "Individual";
    private String login_user_name;
    private String taskid;
    private String schedule_call_assigner = null;
    private String schedule_call_taker = null;
    Context context;
    ArrayList<String> callmembers;
    xmlcomposer xmlcomposer;
    ProgressDialog dialog = null;
    private Handler handler;
    private boolean check = true;
    private int groupid = 0;
    private String escalation_to_users = null;
    private VideoCallDataBase videoCallDataBase;
    String call_receiver_name, project_id;
    boolean escalation_call;
    int setid;
    private String quotes = "\"";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduleraltert);

        try {
            context = this;
            Button accept = (Button) findViewById(R.id.layout_accept);
            Button reject = (Button) findViewById(R.id.layout_decline);
            TextView buddyname = (TextView) findViewById(R.id.buddy_name);
            login_user_name = Appreference.loginuserdetails.getUsername();
            callmembers = new ArrayList<String>();
            xmlcomposer = new xmlcomposer();
            handler = new Handler();
            videoCallDataBase = VideoCallDataBase.getDB(context);
            reject.setText("Ok");
            if (getIntent() != null) {
                taskid = getIntent().getStringExtra("Taskid");
                setid = getIntent().getIntExtra("customsetid", 0);
                Log.i("Schedule", "SchedulerActivity taskid :" + taskid + " setid : " + setid);
                ArrayList<TaskDetailsBean> detailsBeanArrayList = VideoCallDataBase.getDB(context).getSchuduleDetalis(taskid, "" + setid);
                Log.i("Schedule", " " + detailsBeanArrayList.size());
                for (TaskDetailsBean taskDetailsBean : detailsBeanArrayList) {
                    project_id = taskDetailsBean.getProjectId();
                    Log.i("Escalation", " task TagName   " + taskDetailsBean.getTaskTagName());
                    if (taskDetailsBean.getTaskTagName().equalsIgnoreCase("Conference Host")) {
                        schedule_call_assigner = taskDetailsBean.getFromUserName();
                        schedule_call_taker = taskDetailsBean.getToUserName();

                        String Hostname = taskDetailsBean.getTaskDescription();
                        if (Hostname.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            buddyname.setText("Make scheduler call");
                            accept.setText("Make Call");
                            call_maker = true;
                            Log.i("Schedule", "services.ScreenReceiver.wasScreenOn : " + Services.ScreenReceiver.wasScreenOn
                                    + " isApplicationBroughtToBackground() :" + isApplicationBroughtToBackground());
                            if (!Services.ScreenReceiver.wasScreenOn || isApplicationBroughtToBackground()) {
                                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

//                                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
                                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                                                | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                                        "schedulecall");
                                wl.acquire();
                                sendNotification(0, "Make Schedule call", "Scheduler call");
                            }
                        } else {
                            buddyname.setText("You Receive Schedule call from " + videoCallDataBase.getname(Hostname));
                            accept.setText("View");
                            call_maker = false;

                            if (!Services.ScreenReceiver.wasScreenOn || isApplicationBroughtToBackground()) {
                                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

//                                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
                                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                                                | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                                        "schedulecall");
                                wl.acquire();
                                sendNotification(0, "View Schedule Details", "Scheduler call");
                            }
                        }
                        task_type = taskDetailsBean.getTaskType();
                        groupid = Integer.parseInt(taskDetailsBean.getToUserId());
                    } else if ((taskDetailsBean.getTaskTagName().equalsIgnoreCase("Action") && taskDetailsBean.getTaskDescription().equalsIgnoreCase("Audio Conference"))) {
                        call_receiver_name = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskDescription from taskDetailsinfo where taskId='" + taskDetailsBean.getTaskId() + "' and customSetId='" + taskDetailsBean.getCustomSetId() + "' and taskTagName='To'");
                        Log.i("Escalation", "call_receiver_name : " + call_receiver_name);
                        buddyname.setText("Make scheduler call");
                        accept.setText("Make Call");
                        call_maker = false;
                        escalation_call = true;
                        task_type = taskDetailsBean.getTaskType();
                        callmembers.clear();
                        if (!callmembers.contains(call_receiver_name))
                            callmembers.add(call_receiver_name);
                        Log.i("Schedule", "Services.ScreenReceiver.wasScreenOn : " + Services.ScreenReceiver.wasScreenOn
                                + " isApplicationBroughtToBackground() :" + isApplicationBroughtToBackground());
                        if (!Services.ScreenReceiver.wasScreenOn || isApplicationBroughtToBackground()) {
                            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

//                                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
                            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                                            | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                                    "schedulecall");
                            wl.acquire();
                            sendNotification(0, "Make Schedule call", "Scheduler call");
                        }


                    } else if ((taskDetailsBean.getTaskTagName().equalsIgnoreCase("Action") && taskDetailsBean.getTaskDescription().equalsIgnoreCase("Reassign"))) {
                        Log.i("Escalation", " task description   " + taskDetailsBean.getTaskDescription());
                        isReassign_task = true;
                        String Hostname = taskDetailsBean.getTaskDescription();
                        if (!Hostname.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            buddyname.setText("Make Reassign Task");
                            accept.setText("Reassign");
                            call_maker = false;
                            Log.i("Schedule", "Services.ScreenReceiver.wasScreenOn : " + Services.ScreenReceiver.wasScreenOn
                                    + " isApplicationBroughtToBackground() :" + isApplicationBroughtToBackground());
                            if (!Services.ScreenReceiver.wasScreenOn || isApplicationBroughtToBackground()) {
                                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

//                                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
                                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                                                | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                                        "schedulecall");
                                wl.acquire();
                                sendNotification(0, "Make Reassign Task", "Reassign Task");
                            }
                        } /*else {
                            buddyname.setText("You Receive Schedule call from " + videoCallDataBase.getname(Hostname));
                            accept.setText("View");
                            call_maker = false;

                            if (!Services.ScreenReceiver.wasScreenOn || isApplicationBroughtToBackground()) {
                                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

//                                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
                                PowerManager.WakeLock wl  = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                                                | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                                        "schedulecall");
                                wl.acquire();
                                sendNotification(0,"View Schedule Details","Scheduler call");
                            }
                        }*/
                        task_type = taskDetailsBean.getTaskType();
                        groupid = Integer.parseInt(taskDetailsBean.getToUserId());
                    } else if ((taskDetailsBean.getTaskTagName().equalsIgnoreCase("Action") && (taskDetailsBean.getTaskDescription().equalsIgnoreCase("Add observer") || taskDetailsBean.getTaskDescription().equalsIgnoreCase("Buzz") || taskDetailsBean.getTaskDescription().equalsIgnoreCase("Aband")))) {
                        Log.i("Escalation", " task description   " + taskDetailsBean.getTaskDescription());
                        isObserver_task = true;
                        isReassign_task = true;
                        String Hostname = taskDetailsBean.getTaskDescription();
                        if (!Hostname.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            if (taskDetailsBean.getTaskDescription().equalsIgnoreCase("Add observer")) {
                                buddyname.setText("Add Observer Task");
                                accept.setText("Observer");
                            } else if (taskDetailsBean.getTaskDescription().equalsIgnoreCase("Buzz")) {
                                buddyname.setText("Add Buzz Task");
                                accept.setText("Buzz");
                                isBuzz_task = true;
                            } else {
                                buddyname.setText("Aband Task");
                                accept.setText("Aband");
                                isAband_task = true;
                            }
                            call_maker = false;
                            Log.i("Schedule", "services.ScreenReceiver.wasScreenOn : " + Services.ScreenReceiver.wasScreenOn
                                    + " isApplicationBroughtToBackground() :" + isApplicationBroughtToBackground());
                            if (!Services.ScreenReceiver.wasScreenOn || isApplicationBroughtToBackground()) {
                                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

//                                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
                                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                                                | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                                        "schedulecall");
                                wl.acquire();
                                if (taskDetailsBean.getTaskDescription().equalsIgnoreCase("Add observer")) {
                                    sendNotification(0, "Add Observer Task", "Observer Task");
                                } else if (taskDetailsBean.getTaskDescription().equalsIgnoreCase("Buzz")) {
//                                    sendNotification(0, "Add Buzz Task", "Buzz Task");
                                }
                            }
                        } /*else {
                            buddyname.setText("You Receive Schedule call from " + videoCallDataBase.getname(Hostname));
                            accept.setText("View");
                            call_maker = false;

                            if (!services.ScreenReceiver.wasScreenOn || isApplicationBroughtToBackground()) {
                                PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

//                                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
                                PowerManager.WakeLock wl  = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                                                | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                                        "schedulecall");
                                wl.acquire();
                                sendNotification(0,"View Schedule Details","Scheduler call");
                            }
                        }*/
                        task_type = taskDetailsBean.getTaskType();
                        groupid = Integer.parseInt(taskDetailsBean.getToUserId());
                    } else if (taskDetailsBean.getTaskTagName() != null && taskDetailsBean.getTaskTagName().equalsIgnoreCase("To")) {
                        escalation_to_users = taskDetailsBean.getTaskDescription();
                    }
                }
                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isReassign_task) {
                            if (call_maker) {

                                if (schedule_call_assigner != null) {
                                    if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(schedule_call_assigner)) {
                                        callmembers.add(schedule_call_assigner);
                                    }
                                }
                                if (task_type.equalsIgnoreCase("Individual")) {
                                    if (schedule_call_taker != null) {
                                        if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(schedule_call_taker)) {
                                            if (!callmembers.contains(schedule_call_taker))
                                                callmembers.add(schedule_call_taker);
                                        }
                                    }
                                } else {
                                    ArrayList<String> arrayList = VideoCallDataBase.getDB(context).getGroupMembers(String.valueOf(groupid));
                                    for (String group_member : arrayList) {
                                        if (!callmembers.contains(group_member))
                                            callmembers.add(group_member);
                                    }

                                }
                                String query1 = "select * from taskDetailsInfo where loginuser ='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + taskid + "' and sendStatus = '3' order by id DESC LIMIT 1";
                                ArrayList<TaskDetailsBean> arrayList = VideoCallDataBase.getDB(context).getTaskHistory(query1);
                                Log.i("Schedule", "arrayList size :" + arrayList.size());
                                if (arrayList.size() > 0) {
                                    TaskDetailsBean taskDetailsBean = arrayList.get(0);
                                    String taskObservers = taskDetailsBean.getTaskObservers();
                                    Log.i("Schedule", "taskObservers :" + taskObservers);
                                    if (taskObservers != null && taskObservers.contains(",")) {
                                        String taskObservers_array[] = taskObservers.split(",");
                                        for (String observer : taskObservers_array) {
                                            Log.i("Schedule", "observer" + observer);
                                            if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(observer)) {
                                                if (!callmembers.contains(observer)) {
                                                    callmembers.add(observer);
                                                }
                                            }
                                        }
                                    } else if (taskObservers != null) {
                                        if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(taskObservers)) {
                                            if (!callmembers.contains(taskObservers)) {
                                                callmembers.add(taskObservers);
                                            }
                                        }
                                    }

                                }
                                if (callmembers.size() > 0) {
                                    MainActivity.isAudioCall = true;
                                    Appreference.broadcast_call = false;
                                    showprogress();
                                    check = false;
                                    ArrayList<Integer> group_list_id = new ArrayList<Integer>();
                                    for (String username : callmembers) {
                                        group_list_id.add(VideoCallDataBase.getDB(context).getUserid(username));
//                                        callNotification(VideoCallDataBase.getDB(context).getUserid(username), Appreference.loginuserdetails.getId());
                                    }


//                                    for (String groupId : grouplist) {
//                                        group_list_id.add(Integer.parseInt(groupId));
//                                    }
                                    if (group_list_id.size() > 0) {
                                        callNotification(group_list_id, Appreference.loginuserdetails.getId());
                                        Appreference.broadcast_call = false;
                                    }

                                }

                            } else if (escalation_call) {
                                MainActivity.isAudioCall = true;
                                Appreference.broadcast_call = false;
                                showprogress();
                                check = false;
                                ArrayList<Integer> group_list_id = new ArrayList<Integer>();
                                group_list_id.add(VideoCallDataBase.getDB(context).getUserid(call_receiver_name));
                                if (group_list_id.size() > 0) {
                                    callNotification(group_list_id, Appreference.loginuserdetails.getId());
                                    Appreference.broadcast_call = false;
                                }
//                                callNotification(VideoCallDataBase.getDB(context).getUserid(call_receiver_name), Appreference.loginuserdetails.getId());
//                                killActivity();
                            } else {
                                String query;
                                ArrayList<TaskDetailsBean> taskDetailsBeenlist;
                                if (task_type.equalsIgnoreCase("Individual")) {
                                    query = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserName='" + login_user_name + "' ) or ( toUserName='" + login_user_name + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + "Individual" + "') and taskId ='" + taskid + "' group by taskId";
                                    taskDetailsBeenlist = videoCallDataBase.getTaskHistory(query);
                                    Log.i("Schedule", "query :" + query + "\n Indiv list size : " + taskDetailsBeenlist.size());
                                } else {
                                    query = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserId='" + groupid + "' ) or ( toUserId='" + groupid + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + "Group" + "') and taskId ='" + taskid + "' group by taskId";
                                    taskDetailsBeenlist = videoCallDataBase.getTaskHistory(query);
                                    Log.i("Schedule", "query :" + query + " group list size : " + taskDetailsBeenlist.size());
                                }

                                if (taskDetailsBeenlist.size() > 0) {
                                    TaskDetailsBean taskDetailsBean = taskDetailsBeenlist.get(0);
                                    TaskDetailsBean taskDetailsBean1;
                                    if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase("null")) {
                                        taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent("select * from projectHistory where taskId='" + taskDetailsBean.getTaskId() + "'");
                                    } else {
                                        taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent("select * from taskHistoryInfo where taskId='" + taskDetailsBean.getTaskId() + "'");
                                    }
                                    taskDetailsBean.setOwnerOfTask(taskDetailsBean1.getOwnerOfTask());
                                    taskDetailsBean.setTaskName(taskDetailsBean1.getTaskName());
                                    taskDetailsBean.setTaskReceiver(taskDetailsBean1.getTaskReceiver());
                                    taskDetailsBean.setCatagory(taskDetailsBean1.getCatagory());
                                    Intent intent = new Intent(context, NewTaskConversation.class);
                                    intent.putExtra("Ownar", taskDetailsBean.getOwnerOfTask());
                                    intent.putExtra("userName", taskDetailsBean.getFromUserName());
                                    intent.putExtra("toUserName", taskDetailsBean.getToUserName());
                                    intent.putExtra("taskNo", taskDetailsBean.getTaskNo());
                                    intent.putExtra("webtaskNo", taskDetailsBean.getTaskId());
                                    Log.e("task", "to uesr id else" + taskDetailsBean.getToUserId());
                                    intent.putExtra("toUserId", taskDetailsBean.getToUserId());
                                    intent.putExtra("task", "taskhistory");
                                    intent.putExtra("taskname", taskDetailsBean.getTaskName());
                                    intent.putExtra("taskOwner", taskDetailsBean.getOwnerOfTask());
                                    intent.putExtra("taskType", taskDetailsBean.getTaskType());
                                    intent.putExtra("taskReceiver", taskDetailsBean.getTaskReceiver());
                                    intent.putExtra("chvalue", "1");
                                    intent.putExtra("taskHistoryBean", taskDetailsBean);
                                    String groupname;
                                    if (task_type.equalsIgnoreCase("Individual")) {
                                        groupname = login_user_name;
                                    } else {
                                        groupname = String.valueOf(groupid);
                                    }
                                    Log.i("Task1", "groupname" + groupname);
                                    intent.putExtra("groupname", groupname);
                                    startActivity(intent);
                                    killActivity();
                                }

                            }
                        } else if (isObserver_task && !isBuzz_task && !isAband_task) {
                            String query;
                            ArrayList<TaskDetailsBean> taskDetailsBeenlist;
                            if (task_type.equalsIgnoreCase("Individual")) {
                                query = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserName='" + login_user_name + "' ) or ( toUserName='" + login_user_name + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + "Individual" + "') and taskId ='" + taskid + "' group by taskId";
                                taskDetailsBeenlist = videoCallDataBase.getTaskHistory(query);
                                Log.i("Schedule", "query :" + query + "\n Indiv list size : " + taskDetailsBeenlist.size());
                            } else {
                                query = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserId='" + groupid + "' ) or ( toUserId='" + groupid + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and taskId ='" + taskid + "' group by taskId";
                                taskDetailsBeenlist = videoCallDataBase.getTaskHistory(query);
                                Log.i("Schedule", "query :" + query + " group list size : " + taskDetailsBeenlist.size());
                            }

                            if (taskDetailsBeenlist.size() > 0) {
                                TaskDetailsBean taskDetailsBean = taskDetailsBeenlist.get(0);
                                TaskDetailsBean taskDetailsBean1 = new TaskDetailsBean();
                                if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase("null")) {
                                    taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent("select * from projectHistory where taskId='" + taskDetailsBean.getTaskId() + "'");
                                } else {
                                    taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent("select * from taskHistoryInfo where taskId='" + taskDetailsBean.getTaskId() + "'");
                                }
                                taskDetailsBean.setOwnerOfTask(taskDetailsBean1.getOwnerOfTask());
                                taskDetailsBean.setTaskName(taskDetailsBean1.getTaskName());
                                taskDetailsBean.setTaskReceiver(taskDetailsBean1.getTaskReceiver());
                                Log.i("Escalation", "taskDetailsBean1.getCatagory() " + taskDetailsBean1.getCatagory());
                                taskDetailsBean.setCatagory(taskDetailsBean1.getCatagory());
                                if (task_type != null && task_type.equalsIgnoreCase("Individual")) {
                                    query = "select * from taskDetailsInfo where ( taskTagName = 'To' ) and ( ( fromUserName='" + login_user_name + "' ) or ( toUserName='" + login_user_name + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='Individual' or taskType='individual' ) and taskId ='" + taskid + "' and customSetId ='" + String.valueOf(setid) + "' order by id DESC LIMIT 1";
                                } else {
                                    query = "select * from taskDetailsInfo where ( taskTagName = 'To' ) and ( ( fromUserName='" + login_user_name + "' ) or ( toUserName='" + login_user_name + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='group' or taskType='Group') and taskId ='" + taskid + "' and customSetId ='" + String.valueOf(setid) + "' order by id DESC LIMIT 1";
                                }

                                ArrayList<TaskDetailsBean> taskDetailsBeenlist1 = videoCallDataBase.getTaskHistory(query);
                                Log.i("Schedule", "query observer :" + query + " observer list size : " + taskDetailsBeenlist1.size());
                                if (taskDetailsBeenlist1.size() > 0) {
                                    Log.i("Schedule", "query 111 :" + query + " group list size : " + taskDetailsBeenlist1.size());
                                    TaskDetailsBean taskDetailsBean2 = taskDetailsBeenlist1.get(0);
                                    taskDetailsBean.setTaskDescription(taskDetailsBean2.getTaskDescription());
                                }
                                Intent intent = new Intent(context, NewTaskConversation.class);
                                intent.putExtra("Ownar", taskDetailsBean.getOwnerOfTask());
                                intent.putExtra("userName", taskDetailsBean.getFromUserName());
                                intent.putExtra("toUserName", taskDetailsBean.getToUserName());
                                intent.putExtra("taskNo", taskDetailsBean.getTaskNo());
                                intent.putExtra("webtaskNo", taskDetailsBean.getTaskId());
                                Log.e("task", "to uesr id else" + taskDetailsBean.getToUserId());
                                intent.putExtra("toUserId", taskDetailsBean.getToUserId());
                                intent.putExtra("task", "SchedulerObserver");
                                intent.putExtra("taskname", taskDetailsBean.getTaskName());
                                intent.putExtra("taskOwner", taskDetailsBean.getOwnerOfTask());
                                intent.putExtra("taskType", taskDetailsBean.getTaskType());
                                intent.putExtra("taskReceiver", taskDetailsBean.getTaskReceiver());
                                intent.putExtra("chvalue", "1");
                                intent.putExtra("taskHistoryBean", taskDetailsBean);
                                String groupname;
                                if (task_type.equalsIgnoreCase("Individual")) {
                                    groupname = login_user_name;
                                } else {
                                    groupname = String.valueOf(groupid);
                                }
                                Log.i("Escalation", "Scheduler observer " + taskDetailsBean.getTaskDescription());
                                intent.putExtra("groupname", groupname);
                                startActivity(intent);
                                killActivity();
                            }
                        } else if (isBuzz_task) {
                            String query;
                            ArrayList<TaskDetailsBean> taskDetailsBeenlist;
                            if (task_type.equalsIgnoreCase("Individual")) {
                                query = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserName='" + login_user_name + "' ) or ( toUserName='" + login_user_name + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + "Individual" + "') and taskId ='" + taskid + "' group by taskId";
                                taskDetailsBeenlist = videoCallDataBase.getTaskHistory(query);
                                Log.i("Schedule", "query :" + query + "\n Indiv list size : " + taskDetailsBeenlist.size());
                            } else {
                                query = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserId='" + groupid + "' ) or ( toUserId='" + groupid + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + "Group" + "') and taskId ='" + taskid + "' group by taskId";
                                taskDetailsBeenlist = videoCallDataBase.getTaskHistory(query);
                                Log.i("Schedule", "query :" + query + " group list size : " + taskDetailsBeenlist.size());
                            }

                            if (taskDetailsBeenlist.size() > 0) {
                                TaskDetailsBean taskDetailsBean = taskDetailsBeenlist.get(0);
//                                TaskDetailsBean taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent(taskDetailsBean.getTaskId());
                                TaskDetailsBean taskDetailsBean1 = new TaskDetailsBean();
                                if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase("null")) {
                                    taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent("select * from projectHistory where taskId='" + taskDetailsBean.getTaskId() + "'");
                                } else {
                                    taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent("select * from taskHistoryInfo where taskId='" + taskDetailsBean.getTaskId() + "'");
                                }
                                taskDetailsBean.setOwnerOfTask(taskDetailsBean1.getOwnerOfTask());
                                taskDetailsBean.setTaskName(taskDetailsBean1.getTaskName());
                                taskDetailsBean.setTaskReceiver(taskDetailsBean1.getTaskReceiver());
                                Log.i("Escalation", "taskDetailsBean1.getCatagory() " + taskDetailsBean1.getCatagory());
                                taskDetailsBean.setCatagory(taskDetailsBean1.getCatagory());
                                query = "select * from taskDetailsInfo where ( taskTagName = 'To' ) and ( ( fromUserName='" + login_user_name + "' ) or ( toUserName='" + login_user_name + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='Individual' or taskType='individual' ) and taskId ='" + taskid + "' and customSetId ='" + String.valueOf(setid) + "' order by id DESC LIMIT 1";

                                ArrayList<TaskDetailsBean> taskDetailsBeenlist1 = videoCallDataBase.getTaskHistory(query);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String dateTime = dateFormat.format(new Date());
                                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                                String dateforrow = dateFormat.format(new Date());
                                String tasktime = dateTime;
                                tasktime = tasktime.split(" ")[1];
                                Log.i("task", "tasktime" + tasktime);
                                Log.i("UTC", "sendMessage utc time" + dateforrow);
                                Log.i("time", "value");
                                String taskUTCtime = dateforrow;
                                taskDetailsBean.setTaskUTCTime(taskUTCtime);
                                taskDetailsBean.setTaskUTCDateTime(dateforrow);
                                taskDetailsBean.setDateTime(dateforrow);
                                taskDetailsBean.setTasktime(tasktime);
                                if (taskDetailsBeenlist1.size() > 0) {
                                    Log.i("Schedule", "query 111 :" + query + " group list size : " + taskDetailsBeenlist1.size());
                                    TaskDetailsBean taskDetailsBean2 = taskDetailsBeenlist1.get(0);
                                    taskDetailsBean.setSignalid(Utility.getSessionID());
                                    taskDetailsBean.setTaskRequestType("buzzrequest");
                                    ArrayList<String> listOfBuzzNames = new ArrayList<String>();
                                    if (Appreference.context_table.get("mainactivity") != null) {
                                        taskDetailsBean.setTaskDescription(taskDetailsBean2.getTaskDescription());  // contains ToUserName's
                                        taskDetailsBean.setRemark(taskDetailsBean2.getRemark());    // contains Buzz message
                                        Intent intent = new Intent(context, NewTaskConversation.class);
//                                        intent.putExtra("Ownar", taskDetailsBean.getOwnerOfTask());
//                                        intent.putExtra("userName", taskDetailsBean.getFromUserName());
//                                        intent.putExtra("toUserName", taskDetailsBean.getToUserName());
//                                        intent.putExtra("taskNo", taskDetailsBean.getTaskNo());
//                                        intent.putExtra("webtaskNo", taskDetailsBean.getTaskId());
                                        Log.i("task", "toUserId else" + taskDetailsBean.getToUserId());
//                                        intent.putExtra("toUserId", taskDetailsBean.getToUserId());
                                        intent.putExtra("task", "SchedulerBuzz");
//                                        intent.putExtra("taskname", taskDetailsBean.getTaskName());
//                                        intent.putExtra("taskOwner", taskDetailsBean.getOwnerOfTask());
//                                        intent.putExtra("taskType", taskDetailsBean.getTaskType());
//                                        intent.putExtra("taskReceiver", taskDetailsBean.getTaskReceiver());
//                                        intent.putExtra("chvalue", "1");
                                        intent.putExtra("taskHistoryBean", taskDetailsBean);
                                        String groupname;
                                        if (task_type.equalsIgnoreCase("Individual")) {
                                            groupname = login_user_name;
                                        } else {
                                            groupname = String.valueOf(groupid);
                                        }
                                        Log.i("Escalation", "Scheduler buzz " + taskDetailsBean.getRemark());
                                        intent.putExtra("groupname", groupname);
                                        startActivity(intent);
                                    } else {
                                        Log.i("Escalation", "Scheduler buzz TaskDescription " + taskDetailsBean.getRemark());
                                        Log.i("Escalation", "Scheduler buzz ToUser's " + taskDetailsBean2.getTaskDescription());    // ToUserNames
                                        taskDetailsBean.setTaskDescription(taskDetailsBean2.getRemark());   // contains Buzz message
                                        taskDetailsBean.setRead_status(1);
                                        taskDetailsBean.setCustomTagVisible(true);
                                        if (taskDetailsBean2.getTaskDescription() != null && taskDetailsBean2.getTaskDescription().contains(",")) {
                                            int counter = 0;
                                            for (int i = 0; i < taskDetailsBean2.getTaskDescription().length(); i++) {
                                                if (taskDetailsBean2.getTaskDescription().charAt(i) == ',') {
                                                    counter++;
                                                }
                                            }
                                            for (int j = 0; j < counter + 1; j++) {
                                                Log.i("project_details", "Task Mem's and position == " + taskDetailsBean2.getTaskDescription().split(",")[j].trim() + " " + j);
                                                listOfBuzzNames.add(taskDetailsBean2.getTaskDescription().split(",")[j].trim());
                                            }
                                        } else {
                                            listOfBuzzNames.add(taskDetailsBean2.getTaskDescription());
                                        }
                                        if (taskDetailsBean.getProjectId() != null) {
                                            VideoCallDataBase.getDB(context).update_Project_history(taskDetailsBean);
                                        }
                                        VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                                        VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(taskDetailsBean);
                                        String xml_msg = composeChatXML(taskDetailsBean);
                                        sendMultiInstantMessage(xml_msg, listOfBuzzNames, 0);
                                    }
                                }
                                killActivity();
                            }
                        } else if (isAband_task) {
                            String query;
                            ArrayList<TaskDetailsBean> taskDetailsBeenlist;
                            if (task_type.equalsIgnoreCase("Individual")) {
                                query = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserName='" + login_user_name + "' ) or ( toUserName='" + login_user_name + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + "Individual" + "') and taskId ='" + taskid + "' group by taskId";
                                taskDetailsBeenlist = videoCallDataBase.getTaskHistory(query);
                                Log.i("Schedule", "query :" + query + "\n Indiv list size : " + taskDetailsBeenlist.size());
                            } else {
                                query = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserId='" + groupid + "' ) or ( toUserId='" + groupid + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + "Group" + "') and taskId ='" + taskid + "' group by taskId";
                                taskDetailsBeenlist = videoCallDataBase.getTaskHistory(query);
                                Log.i("Schedule", "query :" + query + " group list size : " + taskDetailsBeenlist.size());
                            }

                            if (taskDetailsBeenlist.size() > 0) {
                                TaskDetailsBean taskDetailsBean = taskDetailsBeenlist.get(0);
//                                TaskDetailsBean taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent(taskDetailsBean.getTaskId());
                                TaskDetailsBean taskDetailsBean1 = new TaskDetailsBean();
                                if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase("null")) {
                                    taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent("select * from projectHistory where taskId='" + taskDetailsBean.getTaskId() + "'");
                                } else {
                                    taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent("select * from taskHistoryInfo where taskId='" + taskDetailsBean.getTaskId() + "'");
                                }
                                taskDetailsBean.setOwnerOfTask(taskDetailsBean1.getOwnerOfTask());
                                taskDetailsBean.setTaskName(taskDetailsBean1.getTaskName());
                                taskDetailsBean.setTaskReceiver(taskDetailsBean1.getTaskReceiver());
                                Log.i("Escalation", "taskDetailsBean1.getCatagory() " + taskDetailsBean1.getCatagory());
                                taskDetailsBean.setCatagory(taskDetailsBean1.getCatagory());
//                                query = "select * from taskDetailsInfo where ( taskTagName = 'To' ) and ( ( fromUserName='" + login_user_name + "' ) or ( toUserName='" + login_user_name + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='Individual' or taskType='individual' ) and taskId ='" + taskid + "' and customSetId ='" + String.valueOf(setid) + "' order by id DESC LIMIT 1";

//                                ArrayList<TaskDetailsBean> taskDetailsBeenlist1 = videoCallDataBase.getTaskHistory(query);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String dateTime = dateFormat.format(new Date());
                                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                                String dateforrow = dateFormat.format(new Date());
                                String tasktime = dateTime;
                                tasktime = tasktime.split(" ")[1];
                                Log.i("task", "tasktime" + tasktime);
                                Log.i("UTC", "sendMessage utc time" + dateforrow);
                                Log.i("time", "value");
                                String taskUTCtime = dateforrow;
                                taskDetailsBean.setTaskUTCTime(taskUTCtime);
                                taskDetailsBean.setTaskUTCDateTime(dateforrow);
                                taskDetailsBean.setDateTime(dateforrow);
                                taskDetailsBean.setTasktime(tasktime);
                                taskDetailsBean.setTaskStatus("abandoned");
                                taskDetailsBean.setTaskDescription("This task is abandoned");  // contains ToUserName's
//                                if (taskDetailsBeenlist1.size() > 0) {
//                                    Log.i("Schedule", "query 111 :" + query + " group list size : " + taskDetailsBeenlist1.size());
//                                    TaskDetailsBean taskDetailsBean2 = taskDetailsBeenlist1.get(0);
                                taskDetailsBean.setSignalid(Utility.getSessionID());
                                taskDetailsBean.setTaskRequestType("abandTask");
//                                    ArrayList<String> listOfBuzzNames = new ArrayList<String>();
                                if (Appreference.context_table.get("mainactivity") != null) {

//                                        taskDetailsBean.setRemark(taskDetailsBean2.getRemark());    // contains Buzz message
                                    Intent intent = new Intent(context, NewTaskConversation.class);
//                                        intent.putExtra("Ownar", taskDetailsBean.getOwnerOfTask());
//                                        intent.putExtra("userName", taskDetailsBean.getFromUserName());
//                                        intent.putExtra("toUserName", taskDetailsBean.getToUserName());
//                                        intent.putExtra("taskNo", taskDetailsBean.getTaskNo());
//                                        intent.putExtra("webtaskNo", taskDetailsBean.getTaskId());
                                    Log.i("task", "toUserId else" + taskDetailsBean.getToUserId());
//                                        intent.putExtra("toUserId", taskDetailsBean.getToUserId());
                                    intent.putExtra("task", "SchedulerAband");
//                                        intent.putExtra("taskname", taskDetailsBean.getTaskName());
//                                        intent.putExtra("taskOwner", taskDetailsBean.getOwnerOfTask());
//                                        intent.putExtra("taskType", taskDetailsBean.getTaskType());
//                                        intent.putExtra("taskReceiver", taskDetailsBean.getTaskReceiver());
//                                        intent.putExtra("chvalue", "1");
                                    intent.putExtra("taskHistoryBean", taskDetailsBean);
                                    String groupname;
                                    if (task_type.equalsIgnoreCase("Individual")) {
                                        groupname = login_user_name;
                                    } else {
                                        groupname = String.valueOf(groupid);
                                    }
//                                    Log.i("Escalation", "Scheduler buzz " + taskDetailsBean.getRemark());
                                    intent.putExtra("groupname", groupname);
                                    startActivity(intent);
//                                    }
                                    /*else {
                                        Log.i("Escalation", "Scheduler buzz TaskDescription " + taskDetailsBean.getRemark());
                                        Log.i("Escalation", "Scheduler buzz ToUser's " + taskDetailsBean2.getTaskDescription());    // ToUserNames
                                        taskDetailsBean.setTaskDescription(taskDetailsBean2.getRemark());   // contains Buzz message
                                        taskDetailsBean.setRead_status(1);
                                        taskDetailsBean.setCustomTagVisible(true);
                                        if (taskDetailsBean2.getTaskDescription() != null && taskDetailsBean2.getTaskDescription().contains(",")) {
                                            int counter = 0;
                                            for (int i = 0; i < taskDetailsBean2.getTaskDescription().length(); i++) {
                                                if (taskDetailsBean2.getTaskDescription().charAt(i) == ',') {
                                                    counter++;
                                                }
                                            }
                                            for (int j = 0; j < counter + 1; j++) {
                                                Log.i("project_details", "Task Mem's and position == " + taskDetailsBean2.getTaskDescription().split(",")[j].trim() + " " + j);
                                                listOfBuzzNames.add(taskDetailsBean2.getTaskDescription().split(",")[j].trim());
                                            }
                                        } else {
                                            listOfBuzzNames.add(taskDetailsBean2.getTaskDescription());
                                        }
                                        if (taskDetailsBean.getProjectId() != null) {
                                            VideoCallDataBase.getDB(context).update_Project_history(taskDetailsBean);
                                        }
                                        VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                                        VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(taskDetailsBean);
                                        String xml_msg = composeChatXML(taskDetailsBean);
                                        sendMultiInstantMessage(xml_msg, listOfBuzzNames, 0);
                                    }*/
                                }
                                killActivity();
                            }
                        } else {
                            String query;
                            ArrayList<TaskDetailsBean> taskDetailsBeenlist;
                            if (task_type.equalsIgnoreCase("Individual")) {
                                query = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserName='" + login_user_name + "' ) or ( toUserName='" + login_user_name + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='Individual' or taskType='individual' ) and taskId ='" + taskid + "' group by taskId";
                                taskDetailsBeenlist = videoCallDataBase.getTaskHistory(query);
                                Log.i("Schedule", "query :" + query + "\n Indiv list size : " + taskDetailsBeenlist.size());
                            } else {
                                query = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserId='" + groupid + "' ) or ( toUserId='" + groupid + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='Group' or taskType='group' ) and taskId ='" + taskid + "' group by taskId";
                                taskDetailsBeenlist = videoCallDataBase.getTaskHistory(query);
                                Log.i("Schedule", "query :" + query + " group list size : " + taskDetailsBeenlist.size());
                            }

                            if (taskDetailsBeenlist.size() > 0) {
                                TaskDetailsBean taskDetailsBean = taskDetailsBeenlist.get(0);
//                                TaskDetailsBean taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent(taskDetailsBean.getTaskId());
                                TaskDetailsBean taskDetailsBean1 = new TaskDetailsBean();
                                if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase("null")) {
                                    taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent("select * from projectHistory where taskId='" + taskDetailsBean.getTaskId() + "'");
                                } else {
                                    taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent("select * from taskHistoryInfo where taskId='" + taskDetailsBean.getTaskId() + "'");
                                }
                                taskDetailsBean.setOwnerOfTask(taskDetailsBean1.getOwnerOfTask());
                                taskDetailsBean.setTaskName(taskDetailsBean1.getTaskName());
                                taskDetailsBean.setTaskReceiver(taskDetailsBean1.getTaskReceiver());
                                query = "select * from taskDetailsInfo where ( taskTagName = 'To' ) and ( ( fromUserName='" + login_user_name + "' ) or ( toUserName='" + login_user_name + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='Individual' or taskType='individual') and taskId ='" + taskid + "' and customSetId ='" + String.valueOf(setid) + "' order by id DESC LIMIT 1";
                                ArrayList<TaskDetailsBean> taskDetailsBeenlist1 = videoCallDataBase.getTaskHistory(query);
                                Log.i("Schedule", "query tag To query: " + query + " and size is " + taskDetailsBeenlist1.size());
                                String query_1 = null;
                                if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase("null")) {
                                    query_1 = "select category from projectHistory where taskId='" + taskid + "'";
                                } else {
                                    query_1 = "select category from taskHistoryInfo where taskId='" + taskid + "'";
                                }
                                taskDetailsBean.setCatagory(VideoCallDataBase.getDB(context).getProjectParentTaskId(query_1));
                                if (taskDetailsBeenlist1.size() > 0) {
                                    Log.i("Schedule", "query 111 :" + query + " group list size : " + taskDetailsBeenlist1.size());
                                    TaskDetailsBean taskDetailsBean2 = taskDetailsBeenlist1.get(0);
                                    taskDetailsBean.setTaskDescription(taskDetailsBean2.getTaskDescription());
                                }
                                Intent intent = new Intent(context, NewTaskConversation.class);
                                intent.putExtra("Ownar", taskDetailsBean.getOwnerOfTask());
                                intent.putExtra("userName", taskDetailsBean.getFromUserName());
                                intent.putExtra("toUserName", taskDetailsBean.getToUserName());
                                intent.putExtra("taskNo", taskDetailsBean.getTaskNo());
                                intent.putExtra("webtaskNo", taskDetailsBean.getTaskId());
                                Log.e("task", "to uesr id else" + taskDetailsBean.getToUserId());
                                intent.putExtra("toUserId", taskDetailsBean.getToUserId());
                                intent.putExtra("task", "SchedulerReassign");
                                intent.putExtra("taskname", taskDetailsBean.getTaskName());
                                intent.putExtra("taskOwner", taskDetailsBean.getOwnerOfTask());
                                intent.putExtra("taskType", taskDetailsBean.getTaskType());
                                intent.putExtra("taskReceiver", taskDetailsBean.getTaskReceiver());
                                intent.putExtra("chvalue", "1");
                                intent.putExtra("taskHistoryBean", taskDetailsBean);
                                String groupname;
                                if (task_type.equalsIgnoreCase("Individual")) {
                                    groupname = login_user_name;
                                } else {
                                    groupname = String.valueOf(groupid);
                                }
                                Log.i("Task1", "groupname" + groupname);
                                intent.putExtra("groupname", groupname);
                                startActivity(intent);
                                killActivity();
                            }
                        }
                    }
                });

                reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SchedulerActivity.this.finish();
                    }
                });


            }
        } catch (Exception e) {
            Log.i("Schedule ", "Exception");
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
            buffer.append(" toUserId=" + quotes + cmbean.getToUserId() + quotes);
            buffer.append(" toUserName=" + quotes + cmbean.getToUserName() + quotes);
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
            buffer.append(" timeFrequency=" + quotes + TimeFrequency + quotes);
            buffer.append(" taskOwner=" + quotes + cmbean.getOwnerOfTask() + quotes);
            buffer.append(" mimeType=" + quotes + cmbean.getMimeType() + quotes);
            buffer.append(" dateTime=" + quotes + cmbean.getTaskUTCDateTime() + quotes);
            buffer.append(" taskPriority=" + quotes + cmbean.getTaskPriority() + quotes);
            buffer.append(" completedPercentage=" + quotes + cmbean.getCompletedPercentage() + quotes);
            buffer.append(" remainderQuotes=" + quotes + cmbean.getReminderQuote() + quotes);
            buffer.append(" remark=" + quotes + cmbean.getRemark() + quotes);
            buffer.append(" taskReceiver=" + quotes + cmbean.getTaskReceiver() + quotes);
            buffer.append(" taskToUsersList=" + quotes + cmbean.getGroupTaskMembers() + quotes);
            buffer.append(" requestStatus=" + quotes + cmbean.getRequestStatus() + quotes);
            buffer.append(" subType=" + quotes + cmbean.getSubType() + quotes);
            buffer.append(" daysOfTheWeek=" + quotes + cmbean.getDaysOfTheWeek() + quotes);
            buffer.append(" repeatFrequency=" + quotes + cmbean.getRepeatFrequency() + quotes);
            buffer.append(" taskTagName=" + quotes + cmbean.getTaskTagName() + quotes);
            buffer.append(" taskTagId=" + quotes + cmbean.getCustomTagId() + quotes);
            buffer.append(" taskTagGroupId=" + quotes + cmbean.getCustomSetId() + quotes);
            buffer.append(" isShowOnUI=" + quotes + cmbean.isCustomTagVisible() + quotes);
            buffer.append(" projectId=" + quotes + cmbean.getProjectId() + quotes);
            buffer.append(" taskCategory=" + quotes + cmbean.getCatagory() + quotes);
            buffer.append(" parentTaskId=" + quotes + cmbean.getIssueId() + quotes);
            if (cmbean.getDaysOfTheWeek() != null && !cmbean.getDaysOfTheWeek().equalsIgnoreCase("") && !cmbean.getDaysOfTheWeek().equalsIgnoreCase(null)) {
                buffer.append(" isRepeatTask=" + quotes + "Y" + quotes);
            }
            buffer.append(" />");
            buffer.append("</TaskDetailsinfo>");
            Log.d("xml", "composed xml for buzz===>" + buffer.toString());
            Log.d("xml", "composed xml for encode data======>" + Charset.forName("UTF-8").encode(":-)").toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return buffer.toString();
        }
    }

    public void sendMultiInstantMessage(String msgBody, ArrayList<String> userlist, int sendTo) {
        for (String name : userlist) {
            Log.i("task observer", "observer 1 " + name);
            Log.i("task observer", "MainActivity.account.buddyList.size()" + MainActivity.account.buddyList.size());
        }

        if (!getResources().getString(R.string.proxyua).equalsIgnoreCase("enable") || sendTo == 1) {
            for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
                String name = MainActivity.account.buddyList.get(i).cfg.getUri();
                Log.i("task", "buddyname-->  " + name);
                for (String username : userlist) {
                    Log.i("task", "taskObservers Name--> " + username);
                    String nn = "sip:" + username + "@" + getResources().getString(R.string.server_ip);
                    Log.i("task", "selected user--> " + nn);
                    if (nn.equalsIgnoreCase(name)) {
                        Log.i("task", "both users are same ");
                        Appreference.printLog("Sipmessage", msgBody, "DEBUG", null);
                        MyBuddy myBuddy = MainActivity.account.buddyList.get(i);
                        SendInstantMessageParam prm = new SendInstantMessageParam();
                        prm.setContent(msgBody);

                        boolean valid = myBuddy.isValid();
                        Log.i("task", "valid ======= " + valid);
                        try {
                            myBuddy.sendInstantMessage(prm);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
            /*else {

                BuddyConfig bCfg = new BuddyConfig();
                bCfg.setUri("sip:" + proxy_user + "@" + getResources().getString(R.string.server_ip));
                bCfg.setSubscribe(false);
//            MainActivity.account.addBuddy(bCfg);
                Buddy myBuddy = new Buddy();
                try {
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
                    myBuddy.sendInstantMessage(prm);
                    myBuddy.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }*/
    }

    private void sendNotification(long when, String message, String title) {
        Log.i("Schedule", "sendNotification");

        try {
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(this, 123, intent, 0);

            Notification n = new Notification.Builder(this)
                    .setContentTitle("High Message")
                    .setContentText(message)
                    .setTicker(message)
                    .setSmallIcon(R.drawable.logo)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .build();

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.notify(123, n);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Intent intent = new Intent(this, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        int requestCode = 0;
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        if (AppSharedpreferences.getInstance(this) != null) {
////            if (AppSharedpreferences.getInstance(this).getBoolean(demo1)) {
////                NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
////                        .setSmallIcon(R.drawable.logo)
////                        .setContentTitle(message)
////                        .setContentText(title)
////                        .setAutoCancel(true)
////                        .setSound(null)
////                        .setContentIntent(pendingIntent);
////                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
////                notificationManager.notify((int) when, noBuilder.build()); //0 = ID of notification
////            } else {
//
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
//
////                AudioManager am =
////                        (AudioManager) getSystemService(Context.AUDIO_SERVICE);
////                int vol_mus = am.getStreamVolume(AudioManager.STREAM_MUSIC);
////                int vol_not = am.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
////                int vol_ring = am.getStreamVolume(AudioManager.STREAM_RING);
////                int vol_sys = am.getStreamVolume(AudioManager.STREAM_SYSTEM);
////Log.i("Volume","vol_mus : "+vol_mus+" vol_not :"+vol_not+" vol_ring :"+vol_ring+" vol_sys :"+vol_sys);
////                am.setStreamVolume(
////                        AudioManager.STREAM_MUSIC,
////                        vol_ring,
////                        0);
//                NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.logo)
//                        .setContentTitle(message)
//                        .setContentText(title)
//                        .setAutoCancel(true)
//
//                        .setContentIntent(pendingIntent);
//                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.notify((int) when, noBuilder.build());
//
//            }
    }

    public boolean isApplicationBroughtToBackground() {
        boolean openPinActivity = false;
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
        String activityOnTop = ar.topActivity.toString();
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
        return openPinActivity;
    }

    public void callNotification(ArrayList<Integer> toIds, int fromId) {
        try {
            Log.i("call", String.valueOf(fromId));
//            Log.i("call", String.valueOf(toId));
           /* List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("fromId", String.valueOf(fromId)));
            nameValuePairs.add(new BasicNameValuePair("toId", String.valueOf(toId)));*/
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fromId", String.valueOf(fromId));
            JSONObject jsonObject1 = new JSONObject();
//            jsonObject1.put("id",toId);
//            JSONArray jsonArray=new JSONArray();
//            jsonArray.put(0,jsonObject1);
//            jsonObject.put("to",jsonArray);
//            Appreference.jsonRequestSender.callNotification(EnumJsonWebservicename.callNotification, jsonObject, SchedulerActivity.this);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < toIds.size(); i++) {
                int toId = toIds.get(i);
                jsonObject1.put("id", String.valueOf(toId));
//            JSONArray jsonArray=new JSONArray();
                jsonArray.put(i, jsonObject1);
            }
            jsonObject.put("to", jsonArray);
            if (Appreference.jsonRequestSender == null) {
                JsonRequestSender jsonRequestParser = new JsonRequestSender();
                Appreference.jsonRequestSender = jsonRequestParser;
                jsonRequestParser.start();
            }
            Appreference.jsonRequestSender.callNotification(EnumJsonWebservicename.callNotification, jsonObject, SchedulerActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeCall(boolean audio_call) {
        Log.i("SipVideo", "ContactsFrgament makecall");
        Log.i("SipVideo", "Came to makeCallMainActivity");
        /* Only one call at anytime */
        if (MainActivity.currentCall != null) {

            Log.i("SipVideo", "Came to makeCallMain : Activity.currentCall != nul");
        }

        MainActivity.currentCallArrayList.clear();

        if (task_type.equalsIgnoreCase("Individual")) {
            if (callmembers != null && callmembers.size() > 0) {

                sendConferencecallInfomessage();
                if(callmembers != null && callmembers.size() > 4){
                    Toast.makeText(context,"Call Not allowed for More than 4 members",Toast.LENGTH_LONG).show();
                } else {
                    for (String users : callmembers) {
                        if (users != null) {
                            String buddy_uri = "sip:" + users + "@" + getResources().getString(R.string.server_ip);
                            MyCall call = new MyCall(MainActivity.account, -1);
                            CallOpParam prm = new CallOpParam(true);
                            CallSetting opt = prm.getOpt();
                            opt.setAudioCount(1);
                            if (audio_call) {
                                opt.setVideoCount(0);
                                MainActivity.isAudioCall = true;
                            } else {
                                opt.setVideoCount(1);
                                MainActivity.isAudioCall = false;
                            }
                            Log.i("SipVideo", "make call : " + buddy_uri);
                            try {
                                call.makeCall(buddy_uri, prm);
//                    OnCallSdpCreatedParam param = new OnCallSdpCreatedParam();
//                    SdpSession pot = param.getSdp();
//                    call.onCallSdpCreated(param);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.i("SipVideo", " delete: b2");
                                call.delete();
                                return;
                            }
                            MainActivity.currentCallArrayList.add(call);
//        currentCall = call;
                            MainActivity.currentCall = call;
                            Log.i("SipVideo", "ContactFragment Make Call MainActivity.currentCall");
                        }

                    }
                }
            }
        } else {
            List<String> grouplist = new ArrayList<>();
            sendConferencecallInfomessage();
            if (escalation_to_users != null) {
                if (escalation_to_users.contains(",")) {
                    grouplist = new ArrayList<String>(Arrays.asList(escalation_to_users.split(",")));
                } else {
                    grouplist.add(escalation_to_users);
                }
            } else {
                grouplist = VideoCallDataBase.getDB(context).selectGroupmembers("select * from groupmember where groupid= '" + groupid + "'", "username");
            }
            if(grouplist != null && grouplist.size() > 4){
                Toast.makeText(context,"Call Not allowed for More than 4 members",Toast.LENGTH_LONG).show();
            } else {
                for (String buddyName : grouplist) {


                    if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(buddyName)) {
                        String buddy_uri = "sip:" + buddyName + "@" + getResources().getString(R.string.server_ip);
                        MyCall call = new MyCall(MainActivity.account, -1);
                        CallOpParam prm = new CallOpParam(true);
                        CallSetting opt = prm.getOpt();
                        opt.setAudioCount(1);
                        if (audio_call) {
                            opt.setVideoCount(0);
                            MainActivity.isAudioCall = true;
                        } else {
                            opt.setVideoCount(1);
                            MainActivity.isAudioCall = false;
                        }
                        Log.i("SipVideo", "make group call : " + buddy_uri);
                        try {
                            call.makeCall(buddy_uri, prm);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.i("SipVideo", " delete: b21");
                            call.delete();
//                        return;
                        }
                        MainActivity.currentCallArrayList.add(call);
                        MainActivity.currentCall = call;
                        Log.i("SipVideo", "ContactFragment Make Call MainActivity.currentCall");
                    }
                }
            }
        }
        Log.i("SipVideo", "MainActivity.currentCallArrayList.size()--->" + MainActivity.currentCallArrayList.size());
        if (MainActivity.currentCallArrayList.size() > 0) {
            Appreference.play_call_dial_tone = true;
            showCallActivity();
        } else {

        }

    }

    public void sendConferencecallInfomessage() {
        try {

            Date strt_dt = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss");

            Appreference.callStart_time = sdf.format(strt_dt);

            String conf_callinfo[] = new String[5];
            conf_callinfo[0] = MainActivity.username;
            if (Appreference.callStart_time != null)
                conf_callinfo[1] = Appreference.callStart_time;
            conf_callinfo[2] = "";
            conf_callinfo[3] = Utility.getSessionID() + "@testconferenceuri";
            if (Appreference.broadcast_call) {
                conf_callinfo[4] = "broadcastcall";
            } else {
                conf_callinfo[4] = "normalcall";
            }
            Appreference.conference_uri = conf_callinfo[3];
            String[] participantInfo;
            int noofparticipants = 0;
            int z = 1;
            if (task_type.equalsIgnoreCase("Individual")) {
                for (String bean : callmembers) {
                    noofparticipants = z;
                    z++;
                }
            } else {
                ArrayList<String> grouplist = new ArrayList<>();
                if (escalation_to_users != null) {
                    if (escalation_to_users.contains(",")) {
                        String[] users = escalation_to_users.split(",");
                        grouplist = new ArrayList<String>(Arrays.asList(users));
                    } else {
                        grouplist.add(escalation_to_users);
                    }
                } else {
                    grouplist = VideoCallDataBase.getDB(context).selectGroupmembers("select * from groupmember where groupid= '" + groupid + "'", "username");
                }
                for (String Name : grouplist) {
                    noofparticipants = z;
                    z++;

                }

            }
            participantInfo = new String[noofparticipants];

            int j = 0;
            if (task_type.equalsIgnoreCase("Individual")) {
                for (String bean : callmembers) {
                    participantInfo[j] = bean;
                    Log.i("FT", "callerBean_Array" + bean);

                    j++;

                }
            } else {
                ArrayList<String> grouplist = new ArrayList<>();
                if (escalation_to_users != null) {
                    if (escalation_to_users.contains(",")) {
                        grouplist = new ArrayList<String>(Arrays.asList(escalation_to_users.split(",")));
                    } else {
                        grouplist.add(escalation_to_users);
                    }
                } else {
                    grouplist = VideoCallDataBase.getDB(context).selectGroupmembers("select * from groupmember where groupid= '" + groupid + "'", "username");
                }

                for (String Name : grouplist) {
                    participantInfo[j] = Name;
                    Log.i("FT", "callerBean_Array" + Name);
                    j++;
                }
            }

            String chatinfo[] = new String[9];
            chatinfo[0] = "CallGroupChat";
            chatinfo[1] = "";
            chatinfo[2] = "";
            chatinfo[3] = "";
            chatinfo[4] = "";

            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String dateforrow = dateformat.format(new Date().getTime());
            chatinfo[5] = dateforrow;

            chatinfo[6] = "";

            String confURI = Appreference.conference_uri;
            chatinfo[1] = "";
            chatinfo[1] = "";
            if (chatinfo[2] == null || chatinfo[2].equalsIgnoreCase("")) {
                chatinfo[2] = Utility.getSessionID();
            }
            chatinfo[3] = "";
            chatinfo[4] = MainActivity.username;
            chatinfo[6] = "callchat";
            chatinfo[7] = Appreference.conference_uri;
            chatinfo[8] = "";
            Log.i("offlinecrash", "sendconfrenceCall method 1 line");

            String confInfoXml = xmlcomposer.composeConferenceInfoXML(
                    conf_callinfo, participantInfo, chatinfo);

            if (task_type.equalsIgnoreCase("Individual")) {
                if (callmembers != null && callmembers.size() > 0) {
                    Log.i("call", "buddyList!=null && buddyList.size()>0");
                    for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
                        String name = MainActivity.account.buddyList.get(i).cfg.getUri();
                        Log.i("confinfo", "buddyname-->" + name);
                        for (String buddyname : callmembers) {
                            if (buddyname != null) {
                                String nn = "sip:" + buddyname + "@" + getResources().getString(R.string.server_ip);
                                Log.i("confinfo", "selected user-->" + nn);
                                if (nn.equalsIgnoreCase(name)) {
                                    Log.i("call", "both users are same");
                                    Log.i("confinfo", "both users are same");
                                    MyBuddy myBuddy = MainActivity.account.buddyList.get(i);
                                    SendInstantMessageParam prm = new SendInstantMessageParam();
                                    prm.setContent(confInfoXml);

                                    boolean valid = myBuddy.isValid();
                                    Log.i("confinfo", "valid ======= " + valid);
                                    try {
                                        myBuddy.sendInstantMessage(prm);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                        }
                    }
                }
            } else {
                Log.i("call", "buddyList!=null && buddyList.size()>0");
                for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
                    String name = MainActivity.account.buddyList.get(i).cfg.getUri();
                    Log.i("confinfo", "buddyname-->" + name);
                    ArrayList<String> grouplist = new ArrayList<>();
                    if (escalation_to_users != null) {
                        if (escalation_to_users.contains(",")) {
                            grouplist = new ArrayList<String>(Arrays.asList(escalation_to_users.split(",")));
                        } else {
                            grouplist.add(escalation_to_users);
                        }
                    } else {
                        grouplist = VideoCallDataBase.getDB(context).selectGroupmembers("select * from groupmember where loginuser= '" + Appreference.loginuserdetails.getEmail() + "'", "username");
                    }

                    for (String Name : grouplist) {
                        String nn = "sip:" + Name + "@" + getResources().getString(R.string.server_ip);
                        Log.i("confinfo", "selected user-->" + nn);
                        if (nn.equalsIgnoreCase(name)) {
                            Log.i("call", "both users are same" + nn + "Name ==>>" + Name);
                            Log.i("call", "both users are same and xml is == >" + confInfoXml);
                            Log.i("confinfo", "both users are same");
                            MyBuddy myBuddy = MainActivity.account.buddyList.get(i);
                            SendInstantMessageParam prm = new SendInstantMessageParam();
                            prm.setContent(confInfoXml);

                            boolean valid = myBuddy.isValid();
                            Log.i("confinfo", "valid ======= " + valid);
                            try {
                                myBuddy.sendInstantMessage(prm);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCallActivity() {
        Log.i("SipVideo", "showCallActivity method");
        Intent intent = new Intent(context, CallActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String par_name = Appreference.loginuserdetails.getFirstName() + " " + Appreference.loginuserdetails.getLastName();
        intent.putExtra("host", par_name);
        startActivity(intent);
//        SchedulerActivity.this.finish();
        killActivity();
    }

    private void killActivity() {
        Log.i("SipVideo", "killActivity");
        finish();
    }

    @Override
    public void ResponceMethod(Object object) {
        try {
            cancelDialog();
            CommunicationBean communicationBean = (CommunicationBean) object;
            String s1 = communicationBean.getEmail();
            String s2 = communicationBean.getFirstname();
            if (s2 != null && s2.equalsIgnoreCase("callNotification")) {
                final JSONObject jsonObject = new JSONObject(communicationBean.getEmail());
                if (((int) jsonObject.get("result_code") == 0)) {
                    String result = (String) jsonObject.get("result_text");
                    Log.i("response", "result_text in response " + result);
                    //Toast.makeText(getContext(),result,Toast.LENGTH_LONG).show();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (((String) jsonObject.get("result_text")).equalsIgnoreCase("Your Call Notification Send Successfully.") && !check) {

                                    Log.d("Remove", "cancel dialog");
//                                    activateCall();
                                    makeCall(true);
                                    Log.d("Remove", "active call dialog");
                                    check = true;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    Toast.makeText(context, "call conecting failure", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ErrorMethod(Object object) {
        cancelDialog();
    }

    private void showprogress() {
        dialog = new ProgressDialog(context);
        dialog.setMessage("Call Connecting...");
        dialog.setCancelable(false);
        dialog.show();
    }

    public void cancelDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                Log.i("register", "--progress bar end-----");
                dialog.dismiss();
                dialog = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
