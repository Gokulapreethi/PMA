package com.ase.gcm;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ase.AppSharedpreferences;
import com.ase.Appreference;
import com.ase.Bean.ProjectDetailsBean;
import com.ase.Bean.TaskDetailsBean;
import com.ase.ContactsFragment;
import com.ase.DB.VideoCallDataBase;
import com.ase.LoginActivity;
import com.ase.NewTaskConversation;
import com.ase.ProjectHistory;
import com.ase.R;
import com.ase.RandomNumber.Utility;
import com.ase.TaskHistory;
import com.ase.sketh.ProjectsFragment;

import org.json.JSONException;
import org.json.JSONObject;
import org.pjsip.pjsua2.app.MainActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

//import com.myapplication3.ScreenReceiver;

/**
 * Created by saravanakumar on 7/8/2016.
 */
public class GCMPushReceiverService extends FirebaseMessagingService {

    private static int notify_no;
    public static Handler mHandler;
    String FCMUserId, taskId, isRemainderRequired, taskStatus, completedPercentage, toUserId, taskRequestType, taskNo, timeFrequency, taskplanedoldEndDate, taskplanedLatestEndDate, toTaskName, FromtaskName;
    String fromUserId, taskPriority, taskName, mimeType, Description, plannedStartDateTime, plannedEndDateTime, remainderDateTime, taskOwner, parentId, dateTime, dateFrequency, toTaskId, projectId, fromTaskId;
    String demo1, remTask_Id, webParentTaskId, fromUser_Name, toUser_Name, reminder_quote, rem_firingTime, Ind_Grp, rem_taskName, task_overdue, reminder_tone, serverTime, rem_projectId, firing_Time, rem_taskNo, fromuser_Id, touser_Id;
    String taskCategory;
    ArrayList<TaskDetailsBean> taskList_1, taskList_2 = new ArrayList<>();
    Context context;
    String customCollapseKey, remainder_Frequency;
    long total_mins;
    String signalId = null;
    private Handler handler = new Handler();
    //    String fislast_name;
    String collapseKey = "";
    AppSharedpreferences appSharedpreferences;

    //This method will be called on every new message received
    @Override
    public void onMessageReceived(RemoteMessage message) {

        try {
            Log.d("gcmMessage", "Notification Received");
            Log.d("gcmMessage", "Notification Received  data is " + message.getData().get("message").toString());
            Log.d("gcmMessage", "getCollapseKey  == " + message.getCollapseKey());


//        Toast.makeText(this,"Notification Received",Toast.LENGTH_LONG).show();

            String from = null;
            String title = null;

            context = getApplicationContext();
            appSharedpreferences = AppSharedpreferences.getInstance(this);


//            Log.d("gcmMessage", message.getCollapseKey());
            Log.d("gcmMessage", message.getFrom());


            if (AppSharedpreferences.getInstance(this).getBoolean("login")) {

                //        from = message.getFrom();
                Map data = message.getData();


                //        // REGISTER RECEIVER THAT HANDLES SCREEN ON AND SCREEN OFF LOGIC

                if (Services.ScreenReceiver.wasScreenOn) {
                    // THIS IS THE CASE WHEN ONPAUSE() IS CALLED BY THE SYSTEM DUE TO A SCREEN STATE CHANGE
                    Log.d("SCreen State", "SCREEN TURNED ON");
                } else {
                    // THIS IS WHEN ONPAUSE() IS CALLED WHEN THE SCREEN STATE HAS NOT CHANGED
                    Log.d("SCreen State", "SCREEN TURNED OFF");
                }

                try {
                    if (data != null && data.get("message").toString() != null && !data.get("message").toString().equalsIgnoreCase(null) && !data.get("message").toString().contains("{}")) {
                        Log.i("gcm", "mesage1 ## " + message.getCollapseKey());
                        Log.i("gcm", "mesage1  ## ====  " + data.get("message").toString());
                        demo1 = parseObject(message.getCollapseKey(), data.get("message").toString());
                        Log.i("gcm", "value1 " + demo1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("GCMPushReceiver", "Exception " + e.getMessage(), "WARN", null);
                }

                if (data != null && data.get("message") != null && data.get("message").toString() != null && !data.get("message").equals(null)) {
                    Log.d("gcmMessage1", data.get("message").toString());
                    if (message.getCollapseKey() == null || !message.getCollapseKey().equalsIgnoreCase("Sip Registration") || !collapseKey.equalsIgnoreCase("Sip Registration")) {
                        try {
                            Log.d("gcmMessage1", "New Task & Reminder notification received *   login state is ==  " + AppSharedpreferences.getInstance(this).getBoolean("login"));
                            Log.d("gcmMessage1", "New Task & Reminder notification received *   login userName is ==  " + AppSharedpreferences.getInstance(this).getString("loginUserName"));
                            Log.d("gcmMessage1", "New Task & Reminder notification received *   receiver userName is ==  " + toUser_Name);
                            Log.d("gcmMessage1", "New Task & Reminder notification received *   collapseKey is ==  " + collapseKey);

                            if (AppSharedpreferences.getInstance(this).getBoolean("login") && AppSharedpreferences.getInstance(this).getString("loginUserName").equalsIgnoreCase(toUser_Name)) {
                                Log.d("gcmMessage1", "App Login State and loginUserName == gcm Receiver Name");
                                if (Appreference.context_table.get("mainactivity") != null && Appreference.context_table.get("loginactivity") != null) {
                                    Log.d("gcmMessage1", "New Task & Reminder notification received * *");
                                    if (message.getCollapseKey() != null && (message.getCollapseKey().equalsIgnoreCase("New Task") || message.getCollapseKey().equalsIgnoreCase("New Conversation"))
                                            || (collapseKey.equalsIgnoreCase("New Task") || collapseKey.equalsIgnoreCase("New Conversation"))) {
                                        Log.i("gcmMessage1", "New Task & Reminder notification received  * * *");
                                        if ((Services.ScreenReceiver.wasScreenOn || !Services.ScreenReceiver.wasScreenOn) && isApplicationBroughtToBackground())
                                            sendNotification(message.getSentTime(), message.getCollapseKey(), data.get("message").toString());
                                    } else if (message.getCollapseKey() == null || (message.getCollapseKey().equalsIgnoreCase("Task Reminder") || message.getCollapseKey().equalsIgnoreCase("do_not_collapse"))
                                            || (collapseKey.equalsIgnoreCase("Task Reminder") || collapseKey.equalsIgnoreCase("do_not_collapse"))) {
                                        if (!VideoCallDataBase.getDB(context).getOverdueMsg(remTask_Id)) {
                                            Log.i("gcmMessage1", "New Task & Reminder notification received for reminder * * *");
                                            if ((Services.ScreenReceiver.wasScreenOn || !Services.ScreenReceiver.wasScreenOn) && isApplicationBroughtToBackground())
                                                sendNotification(message.getSentTime(), message.getCollapseKey(), data.get("message").toString());
                                        }
                                    } else if (!Appreference.context_table.containsKey("taskcoversation")) {
                                        if (message.getCollapseKey() != null && (message.getCollapseKey().equalsIgnoreCase("Task Reminder") || message.getCollapseKey().equalsIgnoreCase("do_not_collapse"))
                                                || (collapseKey.equalsIgnoreCase("Task Reminder") || collapseKey.equalsIgnoreCase("do_not_collapse"))) {
                                            if (!VideoCallDataBase.getDB(context).getOverdueMsg(remTask_Id)) {
                                                if ((Services.ScreenReceiver.wasScreenOn || !Services.ScreenReceiver.wasScreenOn) && isApplicationBroughtToBackground())
                                                    sendNotification(message.getSentTime(), message.getCollapseKey(), data.get("message").toString());
                                            }
                                        } else {
                                            if ((Services.ScreenReceiver.wasScreenOn || !Services.ScreenReceiver.wasScreenOn) && isApplicationBroughtToBackground())
                                                sendNotification(message.getSentTime(), message.getCollapseKey(), data.get("message").toString());
                                        }
                                    }

                                } else {
                                    if (message.getCollapseKey() != null && (message.getCollapseKey().equalsIgnoreCase("Task Reminder") || message.getCollapseKey().equalsIgnoreCase("do_not_collapse"))
                                            || (collapseKey.equalsIgnoreCase("Task Reminder") || collapseKey.equalsIgnoreCase("do_not_collapse"))) {
                                        if (!VideoCallDataBase.getDB(context).getOverdueMsg(remTask_Id)) {
                                            if ((Services.ScreenReceiver.wasScreenOn || !Services.ScreenReceiver.wasScreenOn) && isApplicationBroughtToBackground())
                                                sendNotification(message.getSentTime(), message.getCollapseKey(), data.get("message").toString());
                                        }
                                    } else if (message.getCollapseKey() != null && (message.getCollapseKey().equalsIgnoreCase("Call Notification") || collapseKey.equalsIgnoreCase("Call Notification"))) {
                                        if ((Services.ScreenReceiver.wasScreenOn || !Services.ScreenReceiver.wasScreenOn) && isApplicationBroughtToBackground()) {
                                            String username = VideoCallDataBase.getDB(context).getname(data.get("message").toString());
                                            sendNotification(message.getSentTime(), message.getCollapseKey(), username);
                                        }
                                    } else {
                                        if ((!Services.ScreenReceiver.wasScreenOn) && isApplicationBroughtToBackground())
                                            sendNotification(message.getSentTime(), message.getCollapseKey(), data.get("message").toString());
                                    }
                                }
                            } else if (AppSharedpreferences.getInstance(this).getBoolean("login")) {
                                JSONObject jsonObject_check = null;
                                String PMS_collapse_key = null;
                                long PMS_DueDate = 0;
                                try {
                                    if (data.get("message").toString() != null && data.get("message").toString().contains("{") && data.get("message").toString().contains("}")) {
                                        jsonObject_check = new JSONObject(data.get("message").toString());
                                        if (jsonObject_check.has("serverKey")) {
                                            PMS_collapse_key = jsonObject_check.getString("serverKey");
                                        }
                                        if (PMS_collapse_key != null && PMS_collapse_key.contains("PMA-Alert")) {
                                            ShowNotification(PMS_DueDate, jsonObject_check, jsonObject_check.getString("Technician_Name"));
                                        } else if (PMS_collapse_key != null && PMS_collapse_key.contains("HMR-Alert")) {
                                            ShowHMR_Notification(PMS_DueDate, jsonObject_check, jsonObject_check.getString("Technician_Name"));
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Log.d("gcmMessage1", "App Logout state * *");
                                if (AppSharedpreferences.getInstance(this).getBoolean("login") && (message.getCollapseKey() != null && (message.getCollapseKey().equalsIgnoreCase("Call Notification") || collapseKey.equalsIgnoreCase("Call Notification")))) {
                                    if (Appreference.context_table.get("mainactivity") == null && Appreference.context_table.get("loginactivity") == null) {
                                        if ((Services.ScreenReceiver.wasScreenOn || !Services.ScreenReceiver.wasScreenOn) && isApplicationBroughtToBackground()) {
                                            String username = VideoCallDataBase.getDB(context).getname(data.get("message").toString());
                                            sendNotification(message.getSentTime(), message.getCollapseKey(), username);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("GCMPushReceiver", "Exception " + e.getMessage(), "WARN", null);
                        }


                    }
                    String remTask_Receiver = null, remTask_Mems = null;
                    if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("1")) {
                        Log.i("gcmMessage2", "Push Notification from server - value is " + getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER));
                        Log.i("gcmMessage10", "message.getCollapseKey() 012 " + message.getCollapseKey());
                        if (message.getCollapseKey() == null || message.getCollapseKey().equalsIgnoreCase("Task Reminder") || message.getCollapseKey().equalsIgnoreCase("do_not_collapse")) {
                            Log.i("gcmMessage3", "message.getCollapseKey() " + message.getCollapseKey());
//                            parseObject(message.getCollapseKey(), data.get("message").toString());
                            Log.d("gcmMessage3", "parsed sucessfully");
                            if (rem_projectId != null) {
                                remTask_Receiver = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskReceiver from projectHistory where taskId='" + remTask_Id + "'");
                                remTask_Mems = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskMemberList from ProjectHistory where taskId='" + remTask_Id + "'");
                            } else {
                                remTask_Receiver = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskReceiver from taskhistoryInfo where taskId='" + remTask_Id + "'");
                                remTask_Mems = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskMemberList from taskhistoryInfo where taskId='" + remTask_Id + "'");
                            }
                            Log.i("Gcm", " From User Name ------- >  . " + fromUser_Name);
                            Log.i("Gcm", " To User Name ------- >  . " + toUser_Name);
                            Log.i("Gcm", " remTask_Receiver ------- >  . " + remTask_Receiver);
                            Log.i("Gcm", " remTask_Mems ------- >  . " + remTask_Mems);
                            if ((remTask_Id != null && !VideoCallDataBase.getDB(context).getOverdueMsg(remTask_Id)
                                    && ((toUser_Name != null && toUser_Name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) || (remTask_Receiver != null && remTask_Receiver.equalsIgnoreCase(toUser_Name)) || (remTask_Mems != null && remTask_Mems.contains(toUser_Name))))
                                    || (task_overdue != null && task_overdue.equalsIgnoreCase("Y"))) {
                                if (Appreference.context_table.get("mainactivity") != null) {
                                    try {
                                        NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                                        taskList_1 = new ArrayList<>();
                                        String queryy;
                                        if ((fromUser_Name != null && fromUser_Name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) && (toUser_Name != null && toUser_Name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))) {
                                            queryy = "select * from taskDetailsInfo where (fromUserName='" + fromUser_Name + "' or toUserName='" + toUser_Name + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "'and taskId='" + remTask_Id + "' and mimeType='note' and (requestStatus='assigned' or requestStatus='approved') and (taskDescription like '" + "%.mp3" + "' or taskDescription like '" + "%.wav" + "') order by id desc";
                                        } else {
                                            queryy = "select * from taskDetailsInfo where (fromUserName='" + fromUser_Name + "' or toUserName='" + toUser_Name + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "'and taskId='" + remTask_Id + "' and mimeType='date' and requestStatus='assigned' and (taskDescription like '" + "%.mp3" + "' or taskDescription like '" + "%.wav" + "') order by id desc";
                                        }
                                        //                                        String queryy = "select * from taskDetailsInfo where (fromUserName='" + fromUser_Name + "' or toUserName='" + toUser_Name + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "'and taskId='" + remTask_Id + "' and mimeType='date' and requestStatus='assigned' order by id desc";
                                        taskList_1 = VideoCallDataBase.getDB(context).getTaskHistory(queryy);
                                        if (taskList_1.size() > 0) {
                                            TaskDetailsBean taskbean = taskList_1.get(0);
                                            if ((fromUser_Name != null && fromUser_Name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) && (toUser_Name != null && toUser_Name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))) {
                                                reminder_tone = taskbean.getTaskDescription().split("/")[5];
                                                Log.i("gcmMessage5", "reminder_tone same " + reminder_tone);
                                            } else if (fromUser_Name != null && fromUser_Name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                                reminder_tone = taskbean.getServerFileName();
                                                Log.i("gcmMessage5", "reminder_tone fromUser " + reminder_tone);
                                            } else if (toUser_Name != null && toUser_Name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                                reminder_tone = taskbean.getTaskDescription();
                                                Log.i("gcmMessage5", "reminder_tone toUser " + reminder_tone);
                                            }
                                            //                                            reminder_tone = taskbean.getServerFileName();
                                            Log.i("gcmMessage5", "reminder_tone " + reminder_tone);
                                        }
                                        String RootDir = "";
                                        if ((fromUser_Name != null && fromUser_Name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) && (toUser_Name != null && toUser_Name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))) {
                                            if (reminder_tone != null && (reminder_tone.contains(".mp3") || reminder_tone.contains(".wav"))) {
                                                RootDir = Environment.getExternalStorageDirectory()
                                                        .getAbsolutePath()
                                                        + "/High Message/" + reminder_tone;
                                            }
                                        } else {
                                            if (reminder_tone != null && (reminder_tone.contains(".mp3") || reminder_tone.contains(".wav"))) {
                                                RootDir = Environment.getExternalStorageDirectory()
                                                        .getAbsolutePath()
                                                        + "/High Message/downloads/" + reminder_tone;
                                            }
                                        }

                                        File file = new File(RootDir);

                                        if (file.exists()) {
                                            Log.d("gcmMessage taskTone5", "play customTone" + reminder_tone);
                                            MainActivity.setCustomPushRingTone(RootDir);
                                        } else {
                                            MainActivity.startAlarmRingTone();
                                        }
                                        if (toUser_Name != null && toUser_Name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                            try {
                                                //                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                //                                    String dateTime = dateFormat.format(new Date());
                                                //                                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                                                //                                    String dateforrow = dateFormat.format(new Date());

                                                //                                    String dateforrow1 = dateFormat.format(new Date());
                                                Log.i("gcmMessage", "firingTime before " + firing_Time);
                                                if (firing_Time != null) {
                                                    firing_Time = firing_Time.substring(0, 19);
                                                }
                                                Log.i("gcmMessage", "firingTime after " + firing_Time);
                                                Log.i("gcmMessage", "Current UTC Time " + TimeZone.getTimeZone("UTC"));
                                                String dateTime = UTCToLocalTime(firing_Time);
                                                Log.i("gcmMessage", "LocalTime " + dateTime);
                                                String tasktime = dateTime;
                                                if (tasktime != null) {
                                                    tasktime = tasktime.split(" ")[1];
                                                }
                                                Log.i("gcmMessage", "tasktime " + tasktime);
                                                //                                    Log.i("UTC", "sendMessage utc time" + dateforrow);

                                                Log.i("time", "value");
                                                String dateforrow = firing_Time;
                                                String taskUTCtime = firing_Time;
                                                Log.i("gcmMessage6", "data " + data);
                                                TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                                                taskDetailsBean.setTaskName(rem_taskName);
                                                Log.i("gcmMessage6", "rem_taskName " + rem_taskName);
                                                //                                  rem_taskNo = VideoCallDataBase.getDB(context).getReminderTaskNo(remTask_Id);
                                                taskDetailsBean.setTaskNo(rem_taskNo);
                                                taskDetailsBean.setCustomTagVisible(true);
                                                Log.i("gcmMessage6", "rem_taskNo " + rem_taskNo);
                                                Log.i("gcmMessage6", "taskId " + remTask_Id);
                                                taskDetailsBean.setTaskId(remTask_Id);
                                                taskDetailsBean.setReminderQuote(reminder_quote);
                                                taskDetailsBean.setTasktime(tasktime);
                                                taskDetailsBean.setTaskUTCTime(taskUTCtime);
                                                taskDetailsBean.setDateTime(dateTime);
                                                taskDetailsBean.setTaskUTCDateTime(dateforrow);
                                                if (rem_projectId != null) {
                                                    taskDetailsBean.setProjectId(rem_projectId);
                                                    Log.i("gcmMessage6.1", "projectId " + rem_projectId);
                                                } else {
                                                    taskDetailsBean.setProjectId(null);
                                                    Log.i("gcmMessage6.1", "Not projectId " + rem_projectId);
                                                }
                                                Log.i("TaskCategory", "before bean " + taskCategory);
                                                taskDetailsBean.setCatagory(taskCategory);
                                                taskDetailsBean.setIssueId(webParentTaskId);
                                                if (task_overdue != null && task_overdue.equalsIgnoreCase("Y")) {
                                                    taskDetailsBean.setTaskStatus("overdue");
                                                    taskDetailsBean.setMimeType("overdue");
                                                    taskDetailsBean.setTaskDescription("This task is overdue");
                                                    Log.i("gcmMessage", "fromUser overdue " + fromUser_Name);
                                                    taskDetailsBean.setFromUserName(fromUser_Name);
                                                    Log.i("gcmMessage", "toUser overdue " + toUser_Name);
                                                    taskDetailsBean.setToUserName(toUser_Name);
                                                    VideoCallDataBase.getDB(this).updateOverdueStatus(remTask_Id);
//                                                    updateOverdueMsg(taskDetailsBean);
                                                    //                                        String fromuserid = VideoCallDataBase.getDB(this).getReminderId(fromUser_Name);
                                                    taskDetailsBean.setFromUserId(fromuser_Id);
                                                    Log.i("gcmMessage", "toUser overdue " + touser_Id);
                                                    taskDetailsBean.setToUserId(touser_Id);
//                                                    taskDetailsBean.setOverdue_Msg("1");
                                                    taskDetailsBean.setMsg_status(10);
                                                } else {
                                                    taskDetailsBean.setMimeType("text");
                                                    taskDetailsBean.setTaskStatus("reminder");
                                                    Appreference.printLog("Remainder", "Reminder TimeDetails App Active State   Server Time is == " + rem_firingTime + "   ServerFiringTime is == " + serverTime + "   client FiringTime is == " + taskDetailsBean.getDateTime() + "\n  taskId is ==  " + taskDetailsBean.getTaskId() + "   signal Id is == " + signalId, "DEBUG", null);
                                                    Log.i("gcmMessage", "reminderText " + reminder_quote);
                                                    taskDetailsBean.setTaskDescription(reminder_quote);
                                                    Log.i("gcmMessage", "fromUser " + toUser_Name);
                                                    taskDetailsBean.setFromUserName(fromUser_Name);
                                                    Log.i("gcmMessage", "toUser " + fromUser_Name);
                                                    taskDetailsBean.setToUserName(toUser_Name);
                                                    //                                        String fromuserid = VideoCallDataBase.getDB(this).getReminderId(fromUser_Name);
                                                    Log.i("gcmMessage", "fromuserid " + fromuser_Id);
                                                    taskDetailsBean.setFromUserId(fromuser_Id);
                                                    taskDetailsBean.setToUserId(touser_Id);
                                                    taskDetailsBean.setMsg_status(6);
                                                }
                                                taskDetailsBean.setRead_status(2);
                                                taskDetailsBean.setShow_progress(12);
//                                                taskDetailsBean.setMimeType("text");
                                                taskDetailsBean.setTaskPriority("High");
                                                if (Ind_Grp != null && Ind_Grp.equalsIgnoreCase("N")) {
                                                    taskDetailsBean.setTaskType("Individual");
                                                    taskDetailsBean.setTaskReceiver(toUser_Name);
                                                } else if (Ind_Grp != null && Ind_Grp.equalsIgnoreCase("Y")) {
                                                    taskDetailsBean.setTaskType("Group");
                                                }
                                                if (signalId != null && !signalId.equalsIgnoreCase("")) {
                                                    taskDetailsBean.setSignalid(signalId);
                                                } else {
                                                    taskDetailsBean.setSignalid(Utility.getSessionID());
                                                }
                                                taskDetailsBean.setOwnerOfTask(fromUser_Name);


                                                if (Appreference.context_table.containsKey("taskcoversation")) {
                                                    if (taskDetailsBean.getTaskDescription() != null && !taskDetailsBean.getTaskDescription().equalsIgnoreCase("") && !taskDetailsBean.getTaskDescription().equalsIgnoreCase(null)) {
                                                        taskDetailsBean.setRead_status(0);
                                                        if (task_overdue != null && task_overdue.equalsIgnoreCase("Y")) {
                                                            newTaskConversation.taskReminderMessage(taskDetailsBean, null, "overdue", "overdue", "2");
                                                        } else {
                                                            newTaskConversation.taskReminderMessage(taskDetailsBean, null, "reminder", "reminder", "2");
                                                        }
                                                        Log.d("gcmMessage7", "if method fire");
                                                    }
                                                } else if (Appreference.context_table.containsKey("taskhistory")) {
                                                    if (taskDetailsBean.getCatagory() != null && !taskDetailsBean.getCatagory().equalsIgnoreCase("chat")) {
                                                        VideoCallDataBase.getDB(this).insertORupdate_Task_history(taskDetailsBean);
                                                        //                                            VideoCallDataBase.getDB(this).insertORupdate_TaskHistoryInfo(taskDetailsBean);
//                                                        VideoCallDataBase.getDB(this).updateStatus(taskDetailsBean.getTaskId());
                                                        if (rem_projectId != null) {
                                                            if (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("overdue")) {
                                                                VideoCallDataBase.getDB(this).updategrouptaskstatus("update projectHistory set taskStatus='overdue' where taskId='" + taskDetailsBean.getTaskId() + "'");
                                                            } else {
//                                                                taskDetailsBean.setTaskStatus("inprogress");
                                                                VideoCallDataBase.getDB(context).update_Project_history(taskDetailsBean);
                                                            }
                                                        } else {
                                                            if (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("overdue")) {
                                                                VideoCallDataBase.getDB(this).updategrouptaskstatus("update taskHistoryInfo set taskStatus='overdue' where taskId='" + taskDetailsBean.getTaskId() + "'");
                                                            } else {
//                                                                String qry = "select taskDescription from taskDetailsInfo where (taskId ='" + taskDetailsBean.getTaskId() + "') and ((taskDescription ='Task Accepted') or (taskDescription ='issue Accepted')) group by taskId";
//                                                                if (!VideoCallDataBase.getDB(context).isAgendaRecordExists(qry)) {
//                                                                    taskDetailsBean.setTaskStatus("Assigned");
//                                                                }
                                                                VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(taskDetailsBean);
                                                            }
                                                        }
                                                        TaskHistory taskHistory = (TaskHistory) Appreference.context_table.get("taskhistory");
                                                        if (taskHistory != null) {
                                                            Log.d("TaskHistory", "Value true refreshed-2");
                                                            taskHistory.refresh();
                                                        }
                                                    }

                                                } else {
                                                    taskDetailsBean.setRead_status(2);
                                                    taskDetailsBean.setShow_progress(12);
                                                    //                                    Appreference.printLog("Remainder", "TimeDetails" + taskDetailsBean.getDateTime(), "DEBUG", null);
                                                    if (!taskDetailsBean.getCatagory().equalsIgnoreCase("chat")) {
                                                        VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                                                        //                                            VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(taskDetailsBean);
                                                        //                                            VideoCallDataBase.getDB(this).updateStatus(taskDetailsBean.getTaskId());
                                                        if (rem_projectId != null) {
                                                            if (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("overdue")) {
                                                                VideoCallDataBase.getDB(this).updategrouptaskstatus("update projectHistory set taskStatus='overdue' where taskId='" + taskDetailsBean.getTaskId() + "'");
                                                            } else {
//                                                                taskDetailsBean.setTaskStatus("inprogress");
                                                                VideoCallDataBase.getDB(context).update_Project_history(taskDetailsBean);
                                                            }
                                                        } else {
                                                            if (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("overdue")) {
                                                                VideoCallDataBase.getDB(this).updategrouptaskstatus("update taskHistoryInfo set taskStatus='overdue' where taskId='" + taskDetailsBean.getTaskId() + "'");
                                                            } else {
//                                                                String qry = "select taskDescription from taskDetailsInfo where (taskId ='" + taskDetailsBean.getTaskId() + "') and ((taskDescription ='Task Accepted') or (taskDescription ='issue Accepted')) group by taskId";
//                                                                if (!VideoCallDataBase.getDB(context).isAgendaRecordExists(qry) && rem_projectId == null) {
//                                                                    taskDetailsBean.setTaskStatus("Assigned");
//                                                                }
                                                                VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(taskDetailsBean);
                                                            }
                                                        }
                                                        Log.d("gcmMessage8", "else method fire");
                                                        if (Appreference.context_table.containsKey("contactsfragment")) {
                                                            ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
                                                            if (contactsFragment != null) {
                                                                Log.d("gcmMessage", "Reminder contactsfragment refreshed");
                                                                contactsFragment.refresh();
                                                            }
                                                        } else if (Appreference.context_table.containsKey("taskhistory")) {
                                                            TaskHistory taskHistory = (TaskHistory) Appreference.context_table.get("taskhistory");
                                                            if (taskHistory != null) {
                                                                Log.d("gcmMessage", "Reminder taskhistory refreshed");
                                                                taskHistory.refresh();
                                                            }
                                                        } else if (Appreference.context_table.containsKey("projecthistory")) {
                                                            ProjectHistory project_History = (ProjectHistory) Appreference.context_table.get("projecthistory");
                                                            if (project_History != null) {
                                                                Log.d("gcmMessage", "Reminder projecthistory refreshed");
                                                                project_History.refresh();
                                                            }
                                                        } else if (Appreference.context_table.containsKey("projectfragment")) {
                                                            ProjectsFragment project_fragment = (ProjectsFragment) Appreference.context_table.get("projectfragment");
                                                            if (project_fragment != null) {
                                                                Log.d("gcmMessage", "Reminder projectfragment refreshed");
                                                                project_fragment.refresh();
                                                            }
                                                        }
                                                    }
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Appreference.printLog("GCMPushReceiver", "Exception " + e.getMessage(), "WARN", null);
                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Appreference.printLog("GCMPushReceiver", "Exception " + e.getMessage(), "WARN", null);
                                    }

                                } else {
                                    Log.d("gcmMessage9", "Not in MainActivity");
                                    try {

                                        String dateforrow = null, taskUTCtime = null, tasktime = null;
                                        if (firing_Time != null) {
                                            Log.i("gcmMessage", "firingTime before " + firing_Time);
                                            firing_Time = firing_Time.substring(0, 19);
                                            Log.i("gcmMessage", "firingTime after " + firing_Time);
                                            Log.i("gcmMessage", "Current UTC Time " + TimeZone.getTimeZone("UTC"));
                                            String dateTime = UTCToLocalTime(firing_Time);
                                            Log.i("gcmMessage", "LocalTime " + dateTime);
                                            tasktime = dateTime;

                                            tasktime = tasktime.split(" ")[1];
                                            Log.i("gcmMessage", "tasktime " + tasktime);
                                            //                                    Log.i("UTC", "sendMessage utc time" + dateforrow);

                                            Log.i("time", "value");
                                            dateforrow = firing_Time;
                                            taskUTCtime = firing_Time;
                                        }
                                        final TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                                        taskDetailsBean.setTaskName(rem_taskName);
                                        Log.i("gcmMessage", "rem_taskName " + rem_taskName);
                                    /*handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            String rem_taskNo = VideoCallDataBase.getDB(getApplicationContext()).getReminderTaskNo(remTask_Id);
                                            taskDetailsBean.setTaskNo(rem_taskNo);
                                            Log.i("gcmMessage", "rem_taskNo " + rem_taskNo);
                                        }
                                    });*/
                                        taskDetailsBean.setTaskNo(rem_taskNo);
                                        Log.i("gcmMessage", "taskId " + remTask_Id);
                                        taskDetailsBean.setTaskId(remTask_Id);
                                        taskDetailsBean.setReminderQuote(reminder_quote);
                                        taskDetailsBean.setTasktime(tasktime);
                                        taskDetailsBean.setTaskUTCTime(taskUTCtime);
                                        taskDetailsBean.setDateTime(dateTime);
                                        taskDetailsBean.setTaskUTCDateTime(dateforrow);
                                        if (task_overdue != null && task_overdue.equalsIgnoreCase("Y")) {
                                            taskDetailsBean.setMimeType("overdue");
                                            taskDetailsBean.setTaskStatus("overdue");
                                            taskDetailsBean.setTaskDescription("This task is overdue");
                                            Log.i("gcmMessage", "fromUser overdue " + fromUser_Name);
                                            taskDetailsBean.setFromUserName(fromUser_Name);
                                            Log.i("gcmMessage", "toUser overdue " + toUser_Name);
                                            taskDetailsBean.setToUserName(toUser_Name);
                                            taskDetailsBean.setFromUserId(fromuser_Id);
                                            taskDetailsBean.setToUserId(touser_Id);
                                        /*handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                String fromuserid = VideoCallDataBase.getDB(getApplicationContext()).getReminderId(fromUser_Name);
                                                taskDetailsBean.setFromUserId(fromuserid);
    //                                        taskDetailsBean.setToUserId(String.valueOf(Appreference.loginuserdetails.getId()));
                                            }
                                        });*/

                                        } else {
                                            taskDetailsBean.setMimeType("reminder");
                                            taskDetailsBean.setTaskStatus("reminder");
                                            Log.i("gcmMessage", "reminderText " + reminder_quote);
                                            taskDetailsBean.setTaskDescription(reminder_quote);
                                            Log.i("gcmMessage", "fromUser " + toUser_Name);
                                            taskDetailsBean.setFromUserName(fromUser_Name);
                                            Log.i("gcmMessage", "1 toUser " + fromUser_Name);
                                            taskDetailsBean.setToUserName(toUser_Name);
                                            Appreference.printLog("Remainder", "Reminder TimeDetails App Kill State    \n Server Time is == " + rem_firingTime + "   ServerFiringTime is == " + serverTime + "   client FiringTime is == " + taskDetailsBean.getDateTime() + "\n  taskId is ==  " + taskDetailsBean.getTaskId() + "   signal Id is == " + signalId, "DEBUG", null);
                                            taskDetailsBean.setToUserId(touser_Id);
                                            taskDetailsBean.setFromUserId(fromuser_Id);
                                        /*handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                String fromuserid = VideoCallDataBase.getDB(getApplicationContext()).getReminderId(fromUser_Name);
                                                taskDetailsBean.setToUserId(fromuserid);
                                                Log.i("gcmMessage", "touserid" + fromuserid);
    //                                        taskDetailsBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
                                            }
                                        });*/

                                        }
                                        taskDetailsBean.setRead_status(2);
                                        taskDetailsBean.setMsg_status(6);
                                        taskDetailsBean.setTaskPriority("High");
                                        if (Ind_Grp != null && Ind_Grp.equalsIgnoreCase("N")) {
                                            taskDetailsBean.setTaskType("Individual");
                                        } else if (Ind_Grp != null && Ind_Grp.equalsIgnoreCase("Y")) {
                                            taskDetailsBean.setTaskType("Group");
                                        }
                                        if (signalId != null && !signalId.equalsIgnoreCase("")) {
                                            taskDetailsBean.setSignalid(signalId);
                                        } else {
                                            taskDetailsBean.setSignalid(Utility.getSessionID());
                                        }
                                        taskDetailsBean.setOwnerOfTask(fromUser_Name);
                                        taskDetailsBean.setTaskReceiver(toUser_Name);
                                        taskDetailsBean.setCatagory(taskCategory);
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    Log.d("gcmReceiver", " category == " + taskDetailsBean.getCatagory() + "taskDetailsBean.getTaskStatus()  ==   " + taskDetailsBean.getTaskStatus());
                                                    if (taskDetailsBean.getCatagory() != null && !taskDetailsBean.getCatagory().equalsIgnoreCase("chat")) {
                                                        VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);

                                                        if (rem_projectId != null) {
//                                                            taskDetailsBean.setTaskStatus("inprogress");
                                                            if (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("overdue")) {
                                                                VideoCallDataBase.getDB(context).updategrouptaskstatus("update projectHistory set taskStatus='overdue' where taskId='" + taskDetailsBean.getTaskId() + "'");
                                                            } else {
                                                                VideoCallDataBase.getDB(context).update_Project_history(taskDetailsBean);
                                                            }
                                                        } else {
                                                            if (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("overdue")) {
                                                                VideoCallDataBase.getDB(context).updategrouptaskstatus("update taskHistoryInfo set taskStatus='overdue' where taskId='" + taskDetailsBean.getTaskId() + "'");
                                                            } else {
                                                                VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(taskDetailsBean);
                                                            }
                                                        }
                                                        Log.d("gcmMessage", "DB updated");
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    Appreference.printLog("GCMPushReceiver", "Exception " + e.getMessage(), "WARN", null);
                                                }
                                            }
                                        });
                                        //                                if (Services.ScreenReceiver.wasScreenOn)
                                        //                                    sendNotification(message.getSentTime(), message.getCollapseKey(), data.get("message").toString());

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Appreference.printLog("GCMPushReceiver", "Exception " + e.getMessage(), "WARN", null);
                                    }
                                }
                            }
                        }
                        //                    else if (message.getCollapseKey().equalsIgnoreCase("New Task")) {
                        //                        Log.i("gcmMessage10", "message.getCollapseKey()12 " + message.getCollapseKey());
                        //                        if (Appreference.context_table.containsKey("taskcoversation")) {
                        //
                        //                        } else {
                        ////                        if (Services.ScreenReceiver.wasScreenOn)
                        ////                            sendNotification(message.getSentTime(), message.getCollapseKey(), data.get("message").toString());
                        //
                        //                        }
                        ////                        parseObject(data.get("message").toString());
                        //
                        //
                        //                        Log.d("gcmMessage10", "parsed sucessfully  12**");
                        //
                        //                    }
                    }
                } else if (data != null && !data.get("UserName").toString().equalsIgnoreCase(null) || !data.get("GroupName").toString().equalsIgnoreCase(null) || !data.get("message").equals(null)) {
                    try {
                        Log.d("gcmMessage", data.get("UserName").toString());
                        Log.d("gcmMessage", data.get("GroupName").toString());
                        Log.d("gcmMessage", data.get("DateAndTime").toString());
                        if (!message.getCollapseKey().equalsIgnoreCase("Group Change Notification")) {
                            if (!Services.ScreenReceiver.wasScreenOn || isApplicationBroughtToBackground())
                                sendNotification(message.getSentTime(), message.getCollapseKey(), data.get("UserName").toString());
                        }
                        if (message.getCollapseKey().equalsIgnoreCase("Group Change Notification")) {
                            if (data.get("Type").toString().equalsIgnoreCase("Deleted")) {
                                String group_id = data.get("GroupId").toString();
                                String group_name = data.get("GroupName").toString();
                                String deleted_username = data.get("UserName").toString();
                                if (Appreference.context_table.get("mainactivity") != null) {
                                    Log.d("group", "Inside MainActivity Group Delete Notification");
                                    MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
                                    mainActivity.groupChangeDeleteNotification(group_id, group_name, deleted_username);
                                }
                            } else if (data.get("Type").toString().equalsIgnoreCase("Added")) {
                                String group_id = data.get("GroupId").toString();
                                String group_name = data.get("GroupName").toString();
                                String added_username = data.get("UserName").toString();
                                if (Appreference.context_table.get("mainactivity") != null) {
                                    Log.d("group", "Inside MainActivity Group Add Notification");
                                    MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
                                    mainActivity.groupChangeAddNotification(group_id, group_name, added_username);
                                }
                            } else if (data.get("Type").toString().equalsIgnoreCase("group Name Changed")) {
                                String group_id = data.get("GroupId").toString();
                                String newGroup_name = data.get("GroupName").toString();
                                //                    String added_username = data.get("UserName").toString();
                                if (Appreference.context_table.get("mainactivity") != null) {
                                    Log.d("group", "Inside MainActivity Group Name Change");
                                    MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
                                    mainActivity.groupNameChangeNotification(group_id, newGroup_name);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("GCMPushReceiver", "Exception " + e.getMessage(), "WARN", null);
                    }

                }
                //        if(message.getCollapseKey().equalsIgnoreCase("Sip Registration")) {
                //            MainActivity.reRegister();
                //        }
                //        sendMultiNotification(getApplicationContext(),message.getSentTime(),message.getCollapseKey(),data.get("message").toString());

            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("GCMPushReceiver", "Exception " + e.getMessage(), "WARN", null);
        }
    }

    private void ShowNotification(long when, JSONObject content, String technician_name) {
        /*Notification notification;
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        remoteViews.setImageViewResource(R.id.notifimage, R.drawable.logo);
        remoteViews.setTextViewText(R.id.notiftitle, technician_name);
        remoteViews.setTextViewText(R.id.notiftext, Message);

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (!appSharedpreferences.getBoolean("muteConversations")) {
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), uri);
            r.play();
        }

        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("PMS-Service-Due-Alert")
                .setContentText("PMS_Notification")
                .setAutoCancel(true)
                .setPriority(1)
                .setContent(remoteViews);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) when, noBuilder.build());
        notification = noBuilder.build();
        startForeground(309, notification);*/


//build notification
        int notificationAcceptId = new Random().nextInt(); // just use a counter in some util class...
        Log.i("pms123", "notification notifyID=======>notifyId  " + notificationAcceptId);

        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        NotificationCompat.Builder builder = null;

        int dismissId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        /*Dismiss Intent Start*/
        Intent notificationIntent = new Intent(getApplicationContext(), NotificationActivity.class);
        notificationIntent.putExtra("NotificationMessage", "Dismiss");
        notificationIntent.putExtra("notifyId", notificationAcceptId);
        notificationIntent.setAction("0");
        try {
            notificationIntent.putExtra("NotificationKey", content.getString("NotificationKey"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent dismissIntent_new = PendingIntent.getActivity(getApplicationContext(), dismissId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        /*Dismiss Intent End*/

        /************************************************************/

        /*Accept Intent Start*/
        int AcceptId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        Intent AcceptIntent = new Intent(getApplicationContext(), NotificationActivity.class);
        AcceptIntent.putExtra("NotificationMessage", "Accept");
        AcceptIntent.putExtra("notifyId", notificationAcceptId);
        AcceptIntent.setAction("1");
        try {
            AcceptIntent.putExtra("NotificationKey", content.getString("NotificationKey"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AcceptIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent acceptIntent_new = PendingIntent.getActivity(getApplicationContext(), AcceptId, AcceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        /*Accept Intent End*/


        try {
            builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.logo)
                    .setColor(getResources().getColor(R.color.bluenew))
                    .setContentTitle(content.getString("serverKey"))
                    .setAutoCancel(true)
                    .setOngoing(true)   //Prevent user from dismissing notification
                    .setContentText(content.getString("result_text"))
                    .setDefaults(Notification.DEFAULT_ALL) // must requires VIBRATE permission
                    .setPriority(NotificationCompat.PRIORITY_HIGH) //must give priority to High, Max which will considered as heads-up notification
                    .addAction(R.drawable.accept_notify, "ACCEPT", acceptIntent_new)
                    .addAction(R.drawable.dismiss_notify, "Dismiss", dismissIntent_new);

        } catch (JSONException e) {
            e.printStackTrace();
        }


       /* String msgText = "A notification's big view appears only when the notification is expanded, " +
                "which happens when the notification is at the top of the notification drawer, or when the user " +
                "expands the notification " + "with a gesture. Expanded notifications are available starting with Android 4.1.";*/

// Gets an instance of the NotificationManager service
        /*Notification notification = new NotificationCompat.BigTextStyle(builder)
                .bigText(msgText).build();*/
        Notification notification = null;
        try {
            notification = new NotificationCompat.BigTextStyle(builder)
                    .bigText(content.getString("Message")).build();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
//to post your notification to the notification bar with a id. If a notification with same id already exists, it will get replaced with updated information.
//        notificationManager.notify(m, builder.build());
        notificationManager.notify(notificationAcceptId, notification);


    }

    private void ShowHMR_Notification(long when, JSONObject content, String technician_name) {

        int notificationAcceptId = new Random().nextInt(); // just use a counter in some util class...
        Log.i("pms123", "notification notifyID=======>notifyId  " + notificationAcceptId);

        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        NotificationCompat.Builder builder = null;

        int dismissId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);


        try {
            builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.logo)
                    .setColor(getResources().getColor(R.color.bluenew))
                    .setContentTitle(content.getString("serverKey"))
                    .setAutoCancel(true)
                    .setContentText(content.getString("result_text"))
                    .setDefaults(Notification.DEFAULT_ALL) // must requires VIBRATE permission
                    .setPriority(NotificationCompat.PRIORITY_HIGH); //must give priority to High, Max which will considered as heads-up notification

        } catch (JSONException e) {
            e.printStackTrace();
        }

// Gets an instance of the NotificationManager service
        /*Notification notification = new NotificationCompat.BigTextStyle(builder)
                .bigText(msgText).build();*/
        Notification notification = null;
        try {
            notification = new NotificationCompat.BigTextStyle(builder)
                    .bigText(content.getString("Message")).build();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        builder.build().flags |= Notification.FLAG_AUTO_CANCEL;
//to post your notification to the notification bar with a id. If a notification with same id already exists, it will get replaced with updated information.
//        notificationManager.notify(m, builder.build());
        notificationManager.notify(notificationAcceptId, notification);
    }

    public boolean isApplicationBroughtToBackground() {
        boolean openPinActivity = false;
        try {
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
        } catch (SecurityException e) {
            e.printStackTrace();
            Appreference.printLog("GCMPushReceiver", "Exception " + e.getMessage(), "WARN", null);
        }
        return openPinActivity;
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
                Appreference.printLog("GCMPushReceiver", "Exception " + e.getMessage(), "WARN", null);
            }
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            if (myDate != null) {
                formattedDate = simpleDateFormat.format(myDate);
                Log.i("gcmMessage", "formattedDate " + formattedDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("GCMPushReceiver", "Exception " + e.getMessage(), "WARN", null);
        }

        return formattedDate;
    }

    private void sendNotification(long when, String message, String title) {
        ProjectDetailsBean projectDetailsBean = null;
        TaskDetailsBean taskDetailsBean = null;
        try {
//            String msg = null;
//            String tle = null;
            Log.d("gcm", message + " == parms message ");
            Log.d("gcm", collapseKey + " == collapseKey");
            Log.d("gcmMessage", title + "");
            if (message == null || message.equalsIgnoreCase(null) || message.equalsIgnoreCase("null")) {
                message = collapseKey;
            }
            Log.d("gcm", message + " == parms messages ");
            if (Appreference.jsonRequestSender == null && message != null && (message.equalsIgnoreCase("Call Notification") || collapseKey.equalsIgnoreCase("Call Notification")) && AppSharedpreferences.getInstance(this).getBoolean("login")) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
            Intent intent;

            if (Appreference.context_table.get("mainactivity") != null) {
                intent = new Intent(this, NewTaskConversation.class);

                String query = "select * from taskHistoryInfo where taskId ='" + remTask_Id + "'";
                String query1 = "select * from projectHistory where taskId ='" + remTask_Id + "'";
                Log.d("gcm ", "query   " + query + " query 1    " + query1);


                try {
                    String projectId = VideoCallDataBase.getDB(this).getProjectParentTaskId("select projectId from projectHistory where taskId ='" + remTask_Id + "'");

                    if (projectId != null && !projectId.equalsIgnoreCase("")) {
                        if (VideoCallDataBase.getDB(this).getProjectHistory(query1).size() > 0) {
                            Log.d("gcm ", "project  query 1 size   " + VideoCallDataBase.getDB(this).getProjectHistory(query1).size());
                            projectDetailsBean = VideoCallDataBase.getDB(this).getProjectHistory(query1).get(0);
                        }
                    } else if (VideoCallDataBase.getDB(this).getTaskHistoryInfo(query).size() > 0) {
                        Log.d("gcm ", "task query size  " + VideoCallDataBase.getDB(this).getTaskHistoryInfo(query).size());
                        taskDetailsBean = VideoCallDataBase.getDB(this).getTaskHistoryInfo(query).get(0);
                    }

                    intent = new Intent(getApplicationContext(), NewTaskConversation.class);
                    if (projectId != null && !projectId.equalsIgnoreCase("")) {

                        if ((projectDetailsBean != null ? projectDetailsBean.getId() : null) != null && !projectDetailsBean.getId().equalsIgnoreCase("")) {
                            intent.putExtra("task", "projectHistory");
                            intent.putExtra("projectHistoryBean", projectDetailsBean);
                        }
                    } else {
                        intent.putExtra("task", "taskhistory");
                        intent.putExtra("taskHistoryBean", taskDetailsBean);
                        intent.putExtra("catagory", taskDetailsBean != null ? taskDetailsBean.getCatagory() : null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("taskId", remTask_Id);
                intent.putExtra("value", "fcm");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


            int requestCode = 0;
            Uri sound;
            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            if (Appreference.context_table.get("mainactivity") != null) {
                Log.d("gcmMessage", "App Active custom sound");
            } else {
                sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Log.d("gcmMessage", "App killed default sound");
            }
            if (message == null || (message.equalsIgnoreCase("Task Reminder") || message.equalsIgnoreCase("do_not_collapse"))) {
                message = "Reminder Received in '" + rem_taskName + "'";
                if (projectDetailsBean != null && projectDetailsBean.getId() != null && !projectDetailsBean.getId().equalsIgnoreCase("")) {
                    title = "Reminder Received in Task : '" + rem_taskName + "' In project : '" + projectDetailsBean.getProjectName() + "'";
                    Log.d("gcmMessageReminder", "If project Reminder message == " + message + "\n" + " title" + title);
                } else {
                    String fstlst_name = VideoCallDataBase.getDB(context).getName(fromUser_Name);
                    if (fstlst_name != null) {
                        title = "Reminder Received in Task : '" + rem_taskName + "' Id :" + remTask_Id + " From :" + fstlst_name;
                    } else if (fromUser_Name != null) {
                        title = "Reminder Received in Task : '" + rem_taskName + "' Id :" + remTask_Id + " From :" + fromUser_Name;
                    } else {
                        title = "Reminder Received";
                    }
                    Log.d("gcmMessageReminder", "If normal task Reminder message == " + message + "\n" + " title" + title);
                }
            } else if (message != null && (message.equalsIgnoreCase("New Task"))) {
                message = "New Task";
                String fstlst_name = VideoCallDataBase.getDB(context).getName(fromUser_Name);
                title = "New Task Received ";
            } else if (message != null && message.equalsIgnoreCase("New Conversation")) {
                message = "New Conversation ";
                title = "New Conversation Received ";
            } else if (message != null && message.equalsIgnoreCase("Misc Reminder")) {
                message = "Misc Reminder ";
                title = "Escalation Received ";
            } else {
                title = "New Reminder Received";
            }
            /*else if (message != null && message.equalsIgnoreCase("Task Reminder")) {
                message = "HiMessage";
                title = "Update task status " + rem_taskName;
            }
    */
            if (AppSharedpreferences.getInstance(this) != null)
                if (AppSharedpreferences.getInstance(this).getBoolean(demo1)) {
                    NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.logo)
                            .setContentTitle(message)
                            .setContentText(title)
                            .setAutoCancel(true)
                            .setSound(null)
                            .setPriority(1)
                            .setContentIntent(pendingIntent);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify((int) when, noBuilder.build()); //0 = ID of notification
                } else {

                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Notification notificationTet;
                    Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                    r.play();

                    //                AudioManager am =
                    //                        (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                    //                int vol_mus = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                    //                int vol_not = am.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
                    //                int vol_ring = am.getStreamVolume(AudioManager.STREAM_RING);
                    //                int vol_sys = am.getStreamVolume(AudioManager.STREAM_SYSTEM);
                    //Log.i("Volume","vol_mus : "+vol_mus+" vol_not :"+vol_not+" vol_ring :"+vol_ring+" vol_sys :"+vol_sys);
                    //                am.setStreamVolume(
                    //                        AudioManager.STREAM_MUSIC,
                    //                        vol_ring,
                    //                        0);


//                    NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
//                            .setSmallIcon(R.drawable.logo)
//                            .setContentTitle(message)
//                            .setContentText(title)
//                            .setAutoCancel(true)
//                            .setPriority(2)
//                            .setContentIntent(pendingIntent);
//                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                    notificationManager.notify((int) when, noBuilder.build());

                    NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.logo)
                            .setContentTitle(message)
                            .setContentText(title)
                            .setAutoCancel(true)
                            .setPriority(1)
                            .setContentIntent(pendingIntent);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify((int) when, noBuilder.build());
                    notificationTet = noBuilder.build();
                    startForeground(309, notificationTet);


                }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("GCMPushReceiver", "Exception " + e.getMessage(), "WARN", null);
        }


    }

    private static void generateNotification(Context context, String message,
                                             String key) {
        try {
            int icon = R.drawable.logo;
            long when = System.currentTimeMillis();
            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            String title = context.getString(R.string.app_name);
            Intent notificationIntent = new Intent(context,
                    LoginActivity.class);
            final int not_nu = generateRandom();

            Notification notification = new Notification(icon, message, when);

            int currentapiVersion = android.os.Build.VERSION.SDK_INT;

      /*  if (currentapiVersion < android.os.Build.VERSION_CODES.HONEYCOMB) {

            notification.setLatestEventInfo(context, title, message, notification); // This method is removed from the Android 6.0
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify(NOTIFICATION, notification);
        } else {*/
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(message)
                    .setContentText(title)
                    .setAutoCancel(true)
                    .setSound(sound)
                    .setContentIntent(pendingIntent);

            notificationManager.notify(0, notification);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("GCMPushReceiver", "Exception " + e.getMessage(), "WARN", null);
        }
    }

    public static int generateRandom() {
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }

    private static void sendMultiNotification(Context context, long when,
                                              String title, String message) {
        try {
            NotificationCompat.Builder nBuilder;
            Uri alarmSound = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            nBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(title)
                    .setLights(Color.BLUE, 500, 500).setContentText(message)
                    .setAutoCancel(true).setTicker("Notification from s")
                    .setVibrate(new long[]{100, 250, 100, 250, 100, 250})
                    .setSound(alarmSound);
            // write your click event here
            Intent resultIntent = new Intent(context, LoginActivity.class);

            PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
                    notify_no, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            // Show the max number of notifications here
            if (notify_no < 9) {
                notify_no = notify_no + 1;
            } else {
                notify_no = 0;
            }
            nBuilder.setContentIntent(resultPendingIntent);
            NotificationManager nNotifyMgr = (NotificationManager) context
                    .getSystemService(NOTIFICATION_SERVICE);
            nNotifyMgr.notify(notify_no + 2, nBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("GCMPushReceiver", "Exception " + e.getMessage(), "WARN", null);
        }
    }


    public String parseObject(String collapse_key, String objectValue) {
        String value = "";
        Log.i("gcm push notification", "check === " + collapse_key);
        Log.i("gcm push notification", "objectValue === " + objectValue);
        try {

            JSONObject jsonObject_check = null;
            try {
                if (objectValue != null && objectValue.contains("{") && objectValue.contains("}")) {
                    jsonObject_check = new JSONObject(objectValue);
                    if (jsonObject_check.has("serverKey")) {
                        collapse_key = jsonObject_check.getString("serverKey");
                        collapseKey = collapse_key;
                    } else if (jsonObject_check.has("collapseKey")) {
                        collapse_key = jsonObject_check.getString("collapseKey");
                        collapseKey = collapse_key;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (objectValue != null && objectValue.contains("firingTime") && (collapse_key == null || collapse_key.equalsIgnoreCase("Task Reminder") || collapse_key.equalsIgnoreCase("do_not_collapse"))) {
                Log.i("gcm push notification", "reminder text ");
                JSONObject jsonObject1 = new JSONObject(objectValue);
                task_overdue = null;
                remTask_Id = jsonObject1.getString("taskId");
                if (jsonObject1.has("parentTask")) {
                    webParentTaskId = jsonObject1.getString("parentTask");
                }
                fromUser_Name = jsonObject1.getString("fromUser");
                serverTime = jsonObject1.getString("serverTime");
                firing_Time = jsonObject1.getString("firingTime");
                toUser_Name = jsonObject1.getString("toUser");
                if (jsonObject1.has("reminderText")) {
                    if (jsonObject1.getString("reminderText").equalsIgnoreCase("") || jsonObject1.getString("reminderText").equalsIgnoreCase("null")) {
                        reminder_quote = "Task Reminder";
                    } else {
                        reminder_quote = jsonObject1.getString("reminderText");
                    }
                } else {
                    reminder_quote = "Task Reminder";
                }

                rem_firingTime = jsonObject1.getString("firingTime");
                Ind_Grp = jsonObject1.getString("isGroup");
                rem_taskName = jsonObject1.getString("taskName");
                signalId = jsonObject1.getString("signalId");
                if (jsonObject1.has("taskNo"))
                    rem_taskNo = jsonObject1.getString("taskNo");
                fromuser_Id = jsonObject1.getString("fromId");
                touser_Id = jsonObject1.getString("toUserId");
                Log.i("gcmMessage", "rem_taskName & rem_taskNo & fromuser_Id & touser_Id " + rem_taskName + " " + rem_taskNo + " " + fromuser_Id + " " + touser_Id + " " + remTask_Id);
                if (objectValue.contains("overDue")) {
                    task_overdue = jsonObject1.getString("overDue");
                }
                if (objectValue.contains("reminderTone")) {
                    reminder_tone = jsonObject1.getString("reminderTone");
                }
                if (objectValue.contains("projectId")) {
                    rem_projectId = jsonObject1.getString("projectId");
                }
//                String taskCategory = null;
                try {
                    if (jsonObject1.has("taskCategory")) {
                        if (jsonObject1.getString("taskCategory").equalsIgnoreCase("taskCreation")) {
                            taskCategory = "Task";
                            Log.d("TaskCategory ", "task category 1 == " + taskCategory);
                        } else if (jsonObject1.getString("taskCategory").equalsIgnoreCase("note")) {

                            taskCategory = "note";
                            Log.d("TaskCategory ", "task category 2 == " + taskCategory);
                        } else if (jsonObject1.getString("taskCategory").equalsIgnoreCase("chat")) {
                            taskCategory = "chat";
                            Log.d("TaskCategory ", "task category 4 == " + taskCategory);
                        } else {
                            taskCategory = "issue";
                            Log.d("TaskCategory ", "task category 3 == " + taskCategory);
                        }
                    } else {
                        taskCategory = VideoCallDataBase.getDB(this).getProjectParentTaskId("select category from taskHistoryInfo where taskId ='" + remTask_Id + "' order by id");
                        Log.d("TaskCategory ", "task category 4 == " + taskCategory);
                        Log.d("TaskCategory ", "task category 5 ==  select category from taskHistoryInfo where taskId ='" + remTask_Id + "' order by id ASC LIMIT 1");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (collapse_key != null && collapse_key.equalsIgnoreCase("Dependency Reminder")) {
                Log.i("gcm ", "Dependency_Reminder === 1 " + jsonObject_check.getString("serverKey"));
                JSONObject jsonObject = new JSONObject(objectValue);
                if (jsonObject.has("serverKey")) {
                    Description = jsonObject.getString("serverKey");
                }
                if (jsonObject.has("parentId")) {
                    parentId = jsonObject.getString("parentId");
                }
                if (jsonObject.has("toTaskId")) {
                    toTaskId = jsonObject.getString("toTaskId");
                }
                if (jsonObject.has("projectId")) {
                    projectId = jsonObject.getString("projectId");
                }
                if (jsonObject.has("fromTaskId")) {
                    fromTaskId = jsonObject.getString("fromTaskId");
                }
                if (jsonObject.has("signalId")) {
                    signalId = jsonObject.getString("signalId");
                }
                if (jsonObject.has("taskPlannedStartDate")) {
                    plannedStartDateTime = jsonObject.getString("taskPlannedStartDate");
                }
                if (jsonObject.has("taskPlannedBeforeEndDate")) {
                    taskplanedoldEndDate = jsonObject.getString("taskPlannedBeforeEndDate");
                }
                if (jsonObject.has("taskPlannedLatestEndDate")) {
                    taskplanedLatestEndDate = jsonObject.getString("taskPlannedLatestEndDate");
                }
                if (jsonObject.has("fromTaskName")) {
                    FromtaskName = jsonObject.getString("fromTaskName");
                }
                if (jsonObject.has("toTaskName")) {
                    toTaskName = jsonObject.getString("toTaskName");
                }
                String date_time = "";
                if (jsonObject.has("fromTaskCreatedDate")) {
                    date_time = jsonObject.getString("fromTaskCreatedDate");
                }
                String to_dateTime = "";
                if (jsonObject.has("toTaskCreatedDate")) {
                    to_dateTime = jsonObject.getString("toTaskCreatedDate");
                }
                String parenttaskId = "";
                if (jsonObject.has("parentTaskId")) {
                    parenttaskId = jsonObject.getString("parentTaskId");
                }
                String fromtaskNumber = "";
                if (jsonObject.has("fromTaskNumber")) {
                    fromtaskNumber = jsonObject.getString("fromTaskNumber");
                }
                String toTaskNumber = "";
                if (jsonObject.has("toTaskNumber")) {
                    toTaskNumber = jsonObject.getString("toTaskNumber");
                }

                TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                taskDetailsBean.setMimeType("text");
                taskDetailsBean.setTaskStatus("inprogress");
                taskDetailsBean.setSignalid(signalId);
                taskDetailsBean.setTaskDescription(Description);
                taskDetailsBean.setCustomTagVisible(true);
                taskDetailsBean.setSendStatus("0");
//                taskDetailsBean.setMsg_status(33);
                taskDetailsBean.setRead_status(1);
                taskDetailsBean.setShow_progress(1);
                taskDetailsBean.setTasktime(date_time);
                taskDetailsBean.setDateTime(date_time);
                taskDetailsBean.setRequestStatus("Open");
//                taskDetailsBean.setTaskRequestType("Open");
                taskDetailsBean.setParentTaskId(parenttaskId);

                taskDetailsBean.setPlannedStartDateTime(plannedStartDateTime);
                taskDetailsBean.setTaskPlannedBeforeEndDate(taskplanedoldEndDate);
                taskDetailsBean.setTaskPlannedLatestEndDate(taskplanedLatestEndDate);
                taskDetailsBean.setFromTaskName(FromtaskName);
                taskDetailsBean.setToTaskName(toTaskName);

                Log.i("gcm ", "Dependency_Reminder to_taskid " + toTaskId);
                Log.i("gcm ", "Dependency_Reminder from_taskid " + fromTaskId);

                if (projectId != null) {
                    Log.i("gcm ", "Dependency_Reminder project 4 --> " + taskDetailsBean.getSignalid());
                    ProjectDetailsBean taskDetailsBean2 = VideoCallDataBase.getDB(context).getProjectContent("select * from projectHistory where taskId='" + toTaskId + "'");
                    taskDetailsBean.setProjectId(projectId);
                    taskDetailsBean.setProject_ownerName(taskDetailsBean2.getProject_ownerName());
                    taskDetailsBean.setFromUserId(taskDetailsBean2.getFromUserId());
                    taskDetailsBean.setToUserId(taskDetailsBean2.getToUserId());
                    taskDetailsBean.setFromUserName(taskDetailsBean2.getFromUserName());
                    taskDetailsBean.setToUserName(taskDetailsBean2.getToUserName());
                    taskDetailsBean.setOwnerOfTask(taskDetailsBean2.getOwnerOfTask());
                    taskDetailsBean.setTaskReceiver(taskDetailsBean2.getTaskReceiver());
                    taskDetailsBean.setTaskNo(taskDetailsBean2.getTaskNo());
                    taskDetailsBean.setTaskName(taskDetailsBean2.getTaskName());
                    taskDetailsBean.setTaskType(taskDetailsBean2.getTaskType());
                    taskDetailsBean.setTaskId(taskDetailsBean2.getTaskId());
                    Log.i("gcm ", "Dependency_Reminder taskid " + taskDetailsBean.getTaskId());
                    taskDetailsBean.setCatagory(taskDetailsBean2.getCatagory());
                    taskDetailsBean.setIsParentTask(taskDetailsBean2.getIsParentTask());
                    taskDetailsBean.setParentId(parentId);
                } else {
                    Log.i("gcm ", "Dependency_Reminder Task 3 --> " + taskDetailsBean.getTaskId());
                    TaskDetailsBean taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskContent("select * from taskHistoryInfo where taskId='" + toTaskId + "'");
                    taskDetailsBean.setFromUserId(taskDetailsBean1.getFromUserId());
                    taskDetailsBean.setToUserId(taskDetailsBean1.getToUserId());
                    taskDetailsBean.setFromUserName(taskDetailsBean1.getFromUserName());
                    taskDetailsBean.setToUserName(taskDetailsBean1.getToUserName());
                    taskDetailsBean.setTaskType(taskDetailsBean1.getTaskType());
                    taskDetailsBean.setTaskPriority(taskDetailsBean1.getTaskPriority());
                    taskDetailsBean.setTaskNo(taskDetailsBean1.getTaskNo());
                    taskDetailsBean.setTaskId(taskDetailsBean1.getTaskId());
                }
                Log.i("gcm ", "Dependency_Reminder **  ");
                taskDetailsBean.setMsg_status(33);
                if (projectId != null) {
                    Log.i("gcm ", "Dependency_Reminder --> 1 " + taskDetailsBean.getTaskId());
                    VideoCallDataBase.getDB(context).insert_new_Project_history(taskDetailsBean);
                    VideoCallDataBase.getDB(context).updateaccept("update projectDetails set requestStatus='Open' where projectId='" + projectId + "'");
                } else {
                    Log.i("gcm ", "Dependency_Reminder --> 2 " + taskDetailsBean.getTaskId());
                    VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(taskDetailsBean);
                }
                VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                Log.i("gcm ", "Dependency_Reminder --> ## " + taskDetailsBean.getTaskId());
            } else if (collapse_key != null && collapse_key.equalsIgnoreCase("New Task")) {
                Log.i("gcm", "New task value from server " + objectValue);
                JSONObject jsonObject = new JSONObject(objectValue);
                if (jsonObject != null && jsonObject.has("taskId")) {
                    Log.i("gcm", "New value from server ");
                    value = jsonObject.getString("taskId");
                    Log.i("gcm", "New value from server " + value);
//                    Log.i("gcm", "New value from server " + jsonObject.getString("parentId"));
                    String dateTime = jsonObject.getString("dateTime");
                    String completedPercentage = jsonObject.getString("completedPercentage");
                    String isGroupTask = "N";
                    if (jsonObject.has("isGroupTask")) {
                        isGroupTask = jsonObject.getString("isGroupTask");
                    }
                    String Description_1 = null;
                    if (jsonObject.has("Description")) {
                        Description_1 = jsonObject.getString("Description");
                    }
                    String project_Id = null;
                    if (jsonObject.has("projectId")) {
                        project_Id = jsonObject.getString("projectId");
                    }
                    if (jsonObject.has("parentTask")) {
                        webParentTaskId = jsonObject.getString("parentTask");
                    }
                    fromuser_Id = jsonObject.getString("fromUserId");
                    String fromUserId = jsonObject.getString("fromUserId");
                    String toUserId = jsonObject.getString("toUserId");
                    String fromUserName = jsonObject.getString("fromUserName");
                    String toUserName = jsonObject.getString("toUserName");
                    toUser_Name = jsonObject.getString("toUserName");
                    String taskOwner = jsonObject.getString("taskOwner");
                    String taskReceiver = jsonObject.getString("taskReceiver");
//                    String taskCategory = "";
                    String is_RemainderRequired = "Y", remainder_DateTime = null;
                    if (jsonObject.has("taskCategory")) {
                        if (jsonObject.getString("taskCategory").equalsIgnoreCase("taskCreation")) {
                            taskCategory = "Task";
                            Log.d("TaskCategory ", "task category 1 = 1 = " + taskCategory);
                        } else if (jsonObject.getString("taskCategory").equalsIgnoreCase("note")) {
                            taskCategory = "note";
                            Log.d("TaskCategory ", "task category 2 = 2 = " + taskCategory);
                        } else if (jsonObject.getString("taskCategory").equalsIgnoreCase("chat")) {
                            taskCategory = "chat";
                            Log.d("TaskCategory ", "task category 4 == " + taskCategory);
                        } else {
                            taskCategory = "issue";
                            Log.d("TaskCategory ", "task category 3 = 3 = " + taskCategory);
                        }
                    }

                    if (jsonObject.has("isRemainderRequired")) {
                        is_RemainderRequired = jsonObject.getString("isRemainderRequired");
                    }
                    if (jsonObject.has("remainderDateTime")) {
                        remainder_DateTime = jsonObject.getString("remainderDateTime");
                    }
                    String taskPriority;
                    if (jsonObject.has("taskPriority")) {
                        taskPriority = jsonObject.getString("taskPriority");
                    } else {
                        taskPriority = "1";
                    }

                    String mimeType = jsonObject.getString("mimeType");
                    String taskRequestType = jsonObject.getString("taskRequestType");
                    Log.d("TaskCategory ", "taskRequestType  " + taskRequestType);

//                    String parentId = jsonObject.getString("parentId");
                    String timeFrequency_1 = null;
                    if (jsonObject.has("timeFrequency")) {
                        timeFrequency_1 = jsonObject.getString("timeFrequency");
                    }
//                    String timeFrequency = jsonObject.getString("timeFrequency");
                    String signalId = jsonObject.getString("signalId");
                    String taskNo = jsonObject.getString("taskNo");
                    Log.d("TaskCategory ", "NEW task gcm signalId   = = " + signalId);
//                    String remainderDateTime = jsonObject.getString("remainderDateTime");
                    String taskName = jsonObject.getString("taskName");
                    if (taskName != null)
                        rem_taskName = taskName;
                    String planned_StartDateTime = null;
                    if (jsonObject.has("plannedStartDateTime")) {
                        planned_StartDateTime = jsonObject.getString("plannedStartDateTime");
                    }
                    String planned_EndDateTime = null;
                    if (jsonObject.has("plannedEndDateTime")) {
                        planned_EndDateTime = jsonObject.getString("plannedEndDateTime");
                    }
                    String task_Status = null;
                    if (jsonObject.has("taskStatus")) {
                        task_Status = jsonObject.getString("taskStatus");
                    }
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                    taskDetailsBean.setTaskId(value);
                    taskDetailsBean.setCompletedPercentage(completedPercentage);
                    if (Description_1 != null) {
                        taskDetailsBean.setTaskDescription(Description_1);
                    } else {
                        taskDetailsBean.setTaskDescription(taskName);
                    }
                    if (project_Id != null) {
                        taskDetailsBean.setProjectId(project_Id);
                    }
                    taskDetailsBean.setSignalid(signalId);

                    taskDetailsBean.setDateTime(dateTime);
                    taskDetailsBean.setTasktime(dateTime);
                    taskDetailsBean.setTaskUTCDateTime(dateTime);
                    taskDetailsBean.setTaskUTCTime(dateTime);
                    taskDetailsBean.setFromUserId(fromuser_Id);
                    taskDetailsBean.setToUserId(toUserId);
                    taskDetailsBean.setFromUserName(fromUserName);
                    taskDetailsBean.setToUserName(toUserName);
                    taskDetailsBean.setOwnerOfTask(taskOwner);
                    taskDetailsBean.setTaskReceiver(taskReceiver);
                    taskDetailsBean.setIssueId(webParentTaskId);
                    if (taskPriority != null && taskPriority.equalsIgnoreCase("2")) {
                        taskDetailsBean.setTaskPriority("High");
                    } else if (taskPriority != null && taskPriority.equalsIgnoreCase("0")) {
                        taskDetailsBean.setTaskPriority("Low");
                    } else if (taskPriority != null && taskPriority.equalsIgnoreCase("1")) {
                        taskDetailsBean.setTaskPriority("Medium");
                    }
                    taskDetailsBean.setMimeType(mimeType);
                    if (taskRequestType.equalsIgnoreCase("taskCreation") && (taskCategory != null && taskCategory.equalsIgnoreCase("Task"))) {
                        taskRequestType = "Task";
                        Log.d("TaskCategory ", "Catagory 1  " + taskRequestType);
                        taskDetailsBean.setCatagory("Task");
                    } else if (taskCategory != null && taskCategory.equalsIgnoreCase("issue")) {
                        taskRequestType = "issue";
                        Log.d("TaskCategory ", "Catagory 2  " + taskRequestType);
                        taskDetailsBean.setCatagory("issue");
                    } else if (taskCategory != null && taskCategory.equalsIgnoreCase("chat")) {
                        taskRequestType = "chat";
                        Log.d("TaskCategory ", "Catagory 2  " + taskRequestType);
                        taskDetailsBean.setCatagory("chat");
                        Log.d("TaskCategory ", "task category 4 == " + taskCategory);
                    }
                    taskDetailsBean.setTaskRequestType(taskRequestType);
                    if (taskCategory == null && taskRequestType.equalsIgnoreCase("taskCreation")) {
                        taskCategory = "Task";

                    }
                    Log.d("TaskCategory ", "Catagory 111  " + taskDetailsBean.getCatagory());
                    Log.d("TaskCategory ", "Catagory 100 " + taskDetailsBean.getTaskRequestType());
//                    taskDetailsBean.setParentId(parentId);
                    taskDetailsBean.setTaskNo(taskNo);
//                    taskDetailsBean.setUtcPemainderFrequency(plannedStartDateTime.substring(0, 19));
                    taskDetailsBean.setTaskName(taskName);
//                    taskDetailsBean.setUtcPlannedStartDateTime(plannedStartDateTime.substring(0, 19));
//                    taskDetailsBean.setUtcplannedEndDateTime(plannedEndDateTime.substring(0, 19));
                    taskDetailsBean.setTaskStatus(task_Status);
                    Log.i("gcm", "taskDetailsBean_planned 111  " + taskDetailsBean.getPlannedEndDateTime());
                    Log.i("gcm", "taskDetailsBean_planned 100  " + plannedStartDateTime);
                    Log.i("gcm", "taskDetailsBean getIssueId " + taskDetailsBean.getTaskName());
                    if (isGroupTask.equalsIgnoreCase("N")) {
                        taskDetailsBean.setTaskType("Individual");
                    } else {
                        taskDetailsBean.setTaskType("Group");
                    }
                    taskDetailsBean.setCustomTagVisible(true);
                    taskDetailsBean.setSendStatus("0");
                    taskDetailsBean.setMsg_status(1);
                    taskDetailsBean.setRead_status(1);
                    taskDetailsBean.setShow_progress(1);
                    taskDetailsBean.setSubType("taskDescription");
                    Log.i("gcm", "taskDetailsBean.getMimeType " + taskDetailsBean.getMimeType());
                    if (taskDetailsBean.getCatagory() != null && !taskDetailsBean.getCatagory().equalsIgnoreCase("chat")) {
                        if (project_Id != null) {
                            VideoCallDataBase.getDB(context).insert_new_Project_history(taskDetailsBean);
                        } else {
//                            VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                            VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(taskDetailsBean);
                        }
                    }
                    if (objectValue != null && objectValue.contains("plannedStartDateTime") && objectValue.contains("plannedEndDateTime") && objectValue.contains("remainderDateTime")) {
                        if (Description_1 != null) {
                            taskDetailsBean.setTaskDescription(Description_1);
                        } else {
                            taskDetailsBean.setTaskDescription(taskName);
                        }
                        taskDetailsBean.setSubType("normal");
                        if (project_Id != null) {
                            taskDetailsBean.setSignalid(signalId.concat(taskName));
                            Log.d("TaskCategory ", "NEW task gcm signalId  2 = = " + signalId);
                        } else {
                            taskDetailsBean.setSignalid(Utility.getSessionID());
                            Log.d("TaskCategory ", "NEW task gcm signalId  3 = = " + signalId);
                        }
                        taskDetailsBean.setMimeType("date");
                        taskDetailsBean.setMsg_status(10);
                        if (project_Id != null && !project_Id.equalsIgnoreCase("") && !project_Id.equalsIgnoreCase(null)) {
                            if (timeFrequency_1 != null && timeFrequency_1.contains("Minutes")) {
                                taskDetailsBean.setTimeFrequency(timeFrequency_1);
                            } else if (timeFrequency_1 != null) {
                                total_mins = Integer.parseInt(timeFrequency_1) / 60000;
                                remainder_Frequency = total_mins + " Minutes";
                                Log.i("gcm", "remainder_Frequency " + remainder_Frequency);
                                taskDetailsBean.setTimeFrequency(remainder_Frequency);
                            }
                        } else {
                            if (timeFrequency_1 != null && timeFrequency_1.contains("Minutes")) {
                                taskDetailsBean.setTimeFrequency(timeFrequency_1);
                            } else if (timeFrequency_1 != null) {
                                total_mins = Integer.parseInt(timeFrequency_1) / 60000;
                                remainder_Frequency = total_mins + " Minutes";
                                Log.i("gcm", "remainder_Frequency " + remainder_Frequency);
                                taskDetailsBean.setTimeFrequency(remainder_Frequency);
                            }
                        }
                        if (is_RemainderRequired != null) {
                        } else {
                            is_RemainderRequired = "Y";
                        }
                        taskDetailsBean.setIsRemainderRequired(is_RemainderRequired);
                        taskDetailsBean.setUtcPlannedStartDateTime(planned_StartDateTime.substring(0, 19));
                        taskDetailsBean.setUtcplannedEndDateTime(planned_EndDateTime.substring(0, 19));
                        taskDetailsBean.setUtcPemainderFrequency(remainder_DateTime.substring(0, 19));
                        taskDetailsBean.setRequestStatus("assigned");
                        Log.i("gcm", "New value from server 1 " + taskDetailsBean.getMimeType());
                        Log.i("gcm", "taskDetailsBean_planned 1  " + taskDetailsBean.getPlannedEndDateTime());
                        if (!taskDetailsBean.getCatagory().equalsIgnoreCase("chat")) {
                            if (taskDetailsBean.getProjectId() != null) {
                                if (taskDetailsBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    if (Appreference.context_table.get("mainactivity") != null) {
                                        Log.i("gcm", "project if mainactivity active? " + Appreference.context_table.get("mainactivity"));
                                        MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
                                        mainActivity.datefileWebService(taskDetailsBean);
                                        Appreference.project_taskConversationEntry = true;
                                        Log.d("gcm", "project inside gcm datewebservice called");
                                    }
                                }
                                VideoCallDataBase.getDB(context).insert_new_Project_history(taskDetailsBean);
                            } else {
//                            VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                                VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(taskDetailsBean);
                            }
                        }
                    }
                    if (Appreference.context_table.containsKey("taskhistory")) {
                        TaskHistory taskHistory = (TaskHistory) Appreference.context_table.get("taskhistory");
                        if (taskHistory != null) {
                            Log.d("TaskHistory", "Value true refreshed-2");
                            taskHistory.refresh();
                        }
                    }
                    ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
                    if (contactsFragment != null) {
                        contactsFragment.refresh();
                    }

                   /* fromUser_Name = jsonObject.getString("fromUserId");
                    Log.i("gcm", "New value from server " + fromUser_Name);
                    fromUser_Name = fromUser_Name.replace("@", "_");
                    Log.i("gcm", "New value from server  " + fromUser_Name);
                    fromUser_Name = VideoCallDataBase.getDB(this).getName(fromUser_Name);
                    Log.i("gcm", "New value from server " + fromUser_Name);*/
                }

            } else if (collapse_key != null && collapse_key.equalsIgnoreCase("New Conversation")) {
                Log.i("gcm", "New task value from server " + objectValue);
                JSONObject jsonObject = new JSONObject(objectValue);
                String taskOwner = null, taskReceiver = null, mimeType = null, taskRequestType = null, taskNo = null, signalId = null, dateTime = null, taskName = null;
                if (jsonObject != null && jsonObject.has("taskId")) {
                    Log.i("gcm", "New value from server ");
                    value = jsonObject.getString("taskId");
                    Log.i("gcm", "New value from server " + value);
//                    Log.i("gcm", "New value from server " + jsonObject.getString("parentId"));
                    if (jsonObject.has("createdDate")) {
                        dateTime = jsonObject.getString("createdDate");
                    }
                    String completedPercentage = "";
                    if (jsonObject.has("completedPercentage")) {
                        completedPercentage = jsonObject.getString("completedPercentage");
                    }
                    String isGroupTask = "N";
                    if (jsonObject.has("isGroupTask")) {
                        isGroupTask = jsonObject.getString("isGroupTask");
                    }
                    String Description_1 = null;
                    if (jsonObject.has("Description")) {
                        Description_1 = jsonObject.getString("Description");
                    }
                    String project_Id = null;
                    if (jsonObject.has("projectId")) {
                        project_Id = jsonObject.getString("projectId");
                    }
                    if (jsonObject.has("parentTask")) {
                        webParentTaskId = jsonObject.getString("parentTask");
                    }
                    fromuser_Id = jsonObject.getString("fromUserId");
                    String fromUserId = jsonObject.getString("fromUserId");
                    String toUserId = jsonObject.getString("toUserId");
                    String fromUserName = jsonObject.getString("fromUser");
                    String toUserName = jsonObject.getString("toUser");
                    toUser_Name = jsonObject.getString("toUser");

                    if (jsonObject.has("taskOwner")) {
                        taskOwner = jsonObject.getString("taskOwner");
                    } else {
                        if (project_Id != null && !project_Id.equalsIgnoreCase("") && !project_Id.equalsIgnoreCase("null")) {
                            taskOwner = VideoCallDataBase.getDB(context).getProjectParentTaskId("select ownerOfTask from projectHistory where taskId='" + value + "'");
                        } else {
                            taskOwner = VideoCallDataBase.getDB(context).getProjectParentTaskId("select ownerOfTask from taskHistoryInfo where taskId='" + value + "'");
                        }
                    }
                    if (jsonObject.has("taskReceiver")) {
                        taskReceiver = jsonObject.getString("taskReceiver");
                    }
//                    String taskCategory = "";
                    String is_RemainderRequired = "Y", remainder_DateTime = null, requestStatus = null;
                    if (jsonObject.has("taskCategory")) {
                        if (jsonObject.getString("taskCategory").equalsIgnoreCase("taskCreation")) {
                            taskCategory = "Task";
                            Log.d("TaskCategory ", "task category 1 = 1 = " + taskCategory);
                        } else if (jsonObject.getString("taskCategory").equalsIgnoreCase("note")) {
                            taskCategory = "note";
                            Log.d("TaskCategory ", "task category 2 = 2 = " + taskCategory);
                        } else if (jsonObject.getString("taskCategory").equalsIgnoreCase("chat")) {
                            taskCategory = "chat";
                            Log.d("TaskCategory ", "task category 4 == " + taskCategory);
                        } else {
                            taskCategory = "issue";
                            Log.d("TaskCategory ", "task category 3 = 3 = " + taskCategory);
                        }
                    }

                    if (jsonObject.has("isRemainderRequired")) {
                        is_RemainderRequired = jsonObject.getString("isRemainderRequired");
                    }
                    if (jsonObject.has("remainderDateTime")) {
                        remainder_DateTime = jsonObject.getString("remainderDateTime");
                    }
                    if (jsonObject.has("requestStatus")) {
                        requestStatus = jsonObject.getString("requestStatus");
                    }
                    String taskPriority = null;
                    if (jsonObject.has("taskPriority")) {
                        taskPriority = jsonObject.getString("taskPriority");
                    } else {
                        taskPriority = "1";
                    }
                    if (jsonObject.has("mimeType")) {
                        mimeType = jsonObject.getString("mimeType");
                    }
                    if (jsonObject.has("taskRequestType")) {
                        taskRequestType = jsonObject.getString("taskRequestType");
                    }
                    Log.d("TaskCategory ", "taskRequestType  " + taskRequestType);

//                    String parentId = jsonObject.getString("parentId");
                    String timeFrequency_1 = null;
                    if (jsonObject.has("timeFrequency")) {
                        timeFrequency_1 = jsonObject.getString("timeFrequency");
                    }
//                    String timeFrequency = jsonObject.getString("timeFrequency");
                    if (jsonObject.has("signalId")) {
                        signalId = jsonObject.getString("signalId");
                    }
                    if (jsonObject.has("taskNo")) {
                        taskNo = jsonObject.getString("taskNo");
                    }
                    Log.d("TaskCategory ", "NEW task gcm signalId   = = " + signalId);
//                    String remainderDateTime = jsonObject.getString("remainderDateTime");
                    if (jsonObject.has("taskName")) {
                        taskName = jsonObject.getString("taskName");
                    }
                    String planned_StartDateTime = null;
                    if (jsonObject.has("taskStartDateTime")) {
                        planned_StartDateTime = jsonObject.getString("taskStartDateTime");
                    }
                    String planned_EndDateTime = null;
                    if (jsonObject.has("taskEndDateTime")) {
                        planned_EndDateTime = jsonObject.getString("taskEndDateTime");
                    }
                    String task_Status = null;
                    if (jsonObject.has("taskStatus")) {
                        task_Status = jsonObject.getString("taskStatus");
                    }
                    String reminderQuotes = null;
                    if (jsonObject.has("reminderQuotes")) {
                        reminderQuotes = jsonObject.getString("reminderQuotes");
                    }

                    final TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                    taskDetailsBean.setTaskId(value);
                    taskDetailsBean.setCompletedPercentage(completedPercentage);
                    if (Description_1 != null) {
                        taskDetailsBean.setTaskDescription(Description_1);
                    } else {
                        taskDetailsBean.setTaskDescription(taskName);
                    }
                    if (project_Id != null) {
                        taskDetailsBean.setProjectId(project_Id);
                    }
                    taskDetailsBean.setSignalid(signalId);
                    String dateTime_1 = Appreference.utcToLocalTime(dateTime.substring(0, 19));
                    taskDetailsBean.setDateTime(dateTime_1);
                    taskDetailsBean.setTasktime(dateTime_1.split(" ")[1]);
                    taskDetailsBean.setTaskUTCDateTime(dateTime.substring(0, 19));
                    taskDetailsBean.setTaskUTCTime(dateTime.substring(0, 19));
                    taskDetailsBean.setFromUserId(fromuser_Id);
                    taskDetailsBean.setToUserId(toUserId);
                    taskDetailsBean.setFromUserName(fromUserName);
                    taskDetailsBean.setToUserName(toUserName);
                    taskDetailsBean.setOwnerOfTask(taskOwner);
                    taskDetailsBean.setTaskReceiver(taskReceiver);
                    taskDetailsBean.setIssueId(webParentTaskId);
                    if (taskPriority != null && taskPriority.equalsIgnoreCase("2")) {
                        taskDetailsBean.setTaskPriority("High");
                    } else if (taskPriority != null && taskPriority.equalsIgnoreCase("0")) {
                        taskDetailsBean.setTaskPriority("Low");
                    } else if (taskPriority != null && taskPriority.equalsIgnoreCase("1")) {
                        taskDetailsBean.setTaskPriority("Medium");
                    }
                    taskDetailsBean.setMimeType(mimeType);
                    if ((taskRequestType != null && taskRequestType.equalsIgnoreCase("taskCreation")) && (taskCategory != null && taskCategory.equalsIgnoreCase("Task"))) {
                        taskRequestType = "Task";
                        Log.d("TaskCategory ", "Catagory 1  " + taskRequestType);
                        taskDetailsBean.setCatagory("Task");
                    } else if (taskCategory != null && taskCategory.equalsIgnoreCase("issue")) {
                        taskRequestType = "issue";
                        Log.d("TaskCategory ", "Catagory 2  " + taskRequestType);
                        taskDetailsBean.setCatagory("issue");
                    } else if (taskCategory != null && taskCategory.equalsIgnoreCase("chat")) {
                        taskRequestType = "chat";
                        Log.d("TaskCategory ", "Catagory 2  " + taskRequestType);
                        taskDetailsBean.setCatagory("chat");
                        Log.d("TaskCategory ", "task category 4 == " + taskCategory);
                    }
                    taskDetailsBean.setTaskRequestType(taskRequestType);
                    if (taskCategory == null && (taskRequestType != null && taskRequestType.equalsIgnoreCase("taskCreation"))) {
                        taskCategory = "Task";

                    }
                    Log.d("TaskCategory ", "Catagory 111  " + taskDetailsBean.getCatagory());
                    Log.d("TaskCategory ", "Catagory 100 " + taskDetailsBean.getTaskRequestType());
//                    taskDetailsBean.setParentId(parentId);
                    taskDetailsBean.setTaskNo(taskNo);
//                    taskDetailsBean.setUtcPemainderFrequency(plannedStartDateTime.substring(0, 19));
                    taskDetailsBean.setTaskName(taskName);
//                    taskDetailsBean.setUtcPlannedStartDateTime(plannedStartDateTime.substring(0, 19));
//                    taskDetailsBean.setUtcplannedEndDateTime(plannedEndDateTime.substring(0, 19));
                    taskDetailsBean.setTaskStatus(task_Status);
                    Log.i("gcm", "taskDetailsBean_planned 111  " + taskDetailsBean.getPlannedEndDateTime());
                    Log.i("gcm", "taskDetailsBean_planned 100  " + plannedStartDateTime);
                    Log.i("gcm", "taskDetailsBean getIssueId " + taskDetailsBean.getTaskName());
                    if (isGroupTask.equalsIgnoreCase("N")) {
                        taskDetailsBean.setTaskType("individual");
                    } else if (isGroupTask.equalsIgnoreCase("Y")) {
                        taskDetailsBean.setTaskType("group");
                    }
                    taskDetailsBean.setCustomTagVisible(true);
                    taskDetailsBean.setSendStatus("0");
                    taskDetailsBean.setMsg_status(10);
                    taskDetailsBean.setRead_status(1);
                    taskDetailsBean.setShow_progress(1);
                    taskDetailsBean.setSubType("normal");
                    Log.i("gcm", "taskDetailsBean.getMimeType " + taskDetailsBean.getMimeType());
//                    if (taskDetailsBean.getCatagory() != null && !taskDetailsBean.getCatagory().equalsIgnoreCase("chat")) {
//                        if (project_Id != null) {
//                            VideoCallDataBase.getDB(context).insert_new_Project_history(taskDetailsBean);
//                        } else {
//                            VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
//                            VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(taskDetailsBean);
//                        }
//                    }
                    if (objectValue != null && objectValue.contains("taskStartDateTime") && objectValue.contains("taskEndDateTime") && objectValue.contains("remainderDateTime")) {
                        if (requestStatus != null) {
                            taskDetailsBean.setTaskDescription(requestStatus);
                            taskDetailsBean.setRequestStatus(requestStatus);
                        } else {
                            taskDetailsBean.setTaskDescription("assigned");
                            taskDetailsBean.setRequestStatus("assigned");
                        }
                        taskDetailsBean.setSubType("normal");
                        if (project_Id != null) {
                            taskDetailsBean.setSignalid(signalId);
                            Log.d("TaskCategory ", "NEW task gcm signalId  2 = = " + signalId);
                        }
                        taskDetailsBean.setMimeType("date");
                        taskDetailsBean.setMsg_status(10);
                        if (project_Id != null && !project_Id.equalsIgnoreCase("") && !project_Id.equalsIgnoreCase(null)) {
                            if (timeFrequency_1 != null && timeFrequency_1.contains("Minutes")) {
                                taskDetailsBean.setTimeFrequency(timeFrequency_1);
                            } else if (timeFrequency_1 != null) {
                                total_mins = Integer.parseInt(timeFrequency_1) / 60000;
                                remainder_Frequency = total_mins + " Minutes";
                                Log.i("gcm", "remainder_Frequency " + remainder_Frequency);
                                taskDetailsBean.setTimeFrequency(remainder_Frequency);
                            }
                        } else {
                            if (timeFrequency_1 != null && timeFrequency_1.contains("Minutes")) {
                                taskDetailsBean.setTimeFrequency(timeFrequency_1);
                            } else if (timeFrequency_1 != null) {
                                total_mins = Integer.parseInt(timeFrequency_1) / 60000;
                                remainder_Frequency = total_mins + " Minutes";
                                Log.i("gcm", "remainder_Frequency " + remainder_Frequency);
                                taskDetailsBean.setTimeFrequency(remainder_Frequency);
                            }
                        }
                        if (is_RemainderRequired != null) {

                        } else {
                            is_RemainderRequired = "Y";
                        }
                        taskDetailsBean.setIsRemainderRequired(is_RemainderRequired);
                        taskDetailsBean.setPlannedStartDateTime(planned_StartDateTime.substring(0, 19));
                        taskDetailsBean.setPlannedEndDateTime(planned_EndDateTime.substring(0, 19));
                        taskDetailsBean.setRemainderFrequency(remainder_DateTime.substring(0, 19));
                        taskDetailsBean.setUtcPlannedStartDateTime(planned_StartDateTime.substring(0, 19));
                        taskDetailsBean.setUtcplannedEndDateTime(planned_EndDateTime.substring(0, 19));
                        taskDetailsBean.setUtcPemainderFrequency(remainder_DateTime.substring(0, 19));
                        taskDetailsBean.setRequestStatus(requestStatus);
                        taskDetailsBean.setReminderQuote(reminderQuotes);
                        Log.i("gcm", "New value from server 1 " + taskDetailsBean.getMimeType());
                        Log.i("gcm", "taskDetailsBean_planned 1  " + taskDetailsBean.getPlannedEndDateTime());
                        NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                        if (Appreference.context_table.get("mainactivity") != null) {
                            if (Appreference.context_table.containsKey("taskcoversation")) {
                                newTaskConversation.taskOverdueMessage(taskDetailsBean, taskDetailsBean.getMimeType(), taskDetailsBean.getTaskStatus(), 0);
                            } else {
                                taskDetailsBean.setRead_status(2);
                                if (taskDetailsBean.getProjectId() != null) {
                                    VideoCallDataBase.getDB(context).insert_new_Project_history(taskDetailsBean);
                                } else {
                                    VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(taskDetailsBean);
                                }
                            }
                        } else {
                            taskDetailsBean.setRead_status(2);
                            if (taskDetailsBean.getProjectId() != null) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        VideoCallDataBase.getDB(context).insert_new_Project_history(taskDetailsBean);
                                    }
                                });
                            } else {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(taskDetailsBean);
                                    }
                                });
                            }
                        }
                    }
                    if (Appreference.context_table.containsKey("taskhistory")) {
                        TaskHistory taskHistory = (TaskHistory) Appreference.context_table.get("taskhistory");
                        if (taskHistory != null) {
                            Log.d("TaskHistory", "Value true refreshed-2");
                            taskHistory.refresh();
                        }
                    }
                    ProjectHistory project_History = (ProjectHistory) Appreference.context_table.get("projecthistory");
                    if (project_History != null) {
                        Log.d("TaskHistory", "Value true refreshed-3");
                        project_History.refresh();
                    }
                    ProjectsFragment project_fragment = (ProjectsFragment) Appreference.context_table.get("projectfragment");
                    if (project_fragment != null) {
                        Log.d("TaskHistory", "Value true refreshed-4");
                        project_fragment.refresh();
                    }
                    ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
                    if (contactsFragment != null) {
                        contactsFragment.refresh();
                    }

                   /* fromUser_Name = jsonObject.getString("fromUserId");
                    Log.i("gcm", "New value from server " + fromUser_Name);
                    fromUser_Name = fromUser_Name.replace("@", "_");
                    Log.i("gcm", "New value from server  " + fromUser_Name);
                    fromUser_Name = VideoCallDataBase.getDB(this).getName(fromUser_Name);
                    Log.i("gcm", "New value from server " + fromUser_Name);*/
                }

            } else if (collapse_key != null && collapse_key.equalsIgnoreCase("Misc Reminder")) {
                JSONObject jsonObject1 = new JSONObject(objectValue);
                String taskId = "", fire_time = "", task_status = "", task_percentage = "", str_value = "", setid = "", esc_Msg = "";
                if (jsonObject1 != null && jsonObject1.has("taskId")) {
                    taskId = jsonObject1.getString("taskId");
                }
                if (jsonObject1.has("firingTime")) {
                    fire_time = jsonObject1.getString("firingTime");
                }
                if (jsonObject1.has("taskStatus")) {
                    task_status = jsonObject1.getString("taskStatus");
                }
                if (jsonObject1.has("taskPercentage")) {
                    task_percentage = jsonObject1.getString("taskPercentage");
                }
                if (jsonObject1.has("reminderText")) {
                    esc_Msg = jsonObject1.getString("reminderText");
                }
//                if(jsonObject1.has("value")){
//                    str_value =  jsonObject1.getString("value");
//                    Type collectionType = new TypeToken<List<EscalationBean>>() {
//                    }.getType();
//                    List<EscalationBean> lcs = new Gson().fromJson(str_value, collectionType);
//                }
                if (jsonObject1.has("setId")) {
                    setid = jsonObject1.getString("setId");
                }
                final TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                taskDetailsBean.setTaskId(taskId);
                taskDetailsBean.setCompletedPercentage(task_percentage);
                TaskDetailsBean taskDetailsBean1 = new TaskDetailsBean();
                taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskContent("select * from taskDetailsInfo where taskId='" + taskId + "' order by id DESC LIMIT 1");
                if (taskDetailsBean1 != null) {
                    taskDetailsBean.setTaskDescription(esc_Msg);
                    taskDetailsBean.setSignalid(Utility.getSessionID());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateTime = dateFormat.format(new Date());
                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String dateforrow = dateFormat.format(new Date());
                    taskDetailsBean.setDateTime(dateTime);
                    taskDetailsBean.setTasktime(dateTime.split(" ")[1]);
                    taskDetailsBean.setTaskUTCDateTime(dateforrow);
                    taskDetailsBean.setTaskUTCTime(dateforrow);
                    taskDetailsBean.setFromUserId(taskDetailsBean1.getFromUserId());
                    taskDetailsBean.setToUserId(taskDetailsBean1.getToUserId());
                    taskDetailsBean.setFromUserName(taskDetailsBean1.getFromUserName());
                    taskDetailsBean.setToUserName(taskDetailsBean1.getToUserName());
//                    taskDetailsBean.setOwnerOfTask(taskDetailsBean1.getOwnerOfTask());
//                    taskDetailsBean.setTaskReceiver(taskDetailsBean1.getTaskReceiver());
                    taskDetailsBean.setMimeType("text");
                    taskDetailsBean.setTaskNo(taskDetailsBean1.getTaskNo());
//                    taskDetailsBean.setTaskName(taskDetailsBean1.getTaskName());
                    taskDetailsBean.setTaskStatus(taskDetailsBean1.getTaskStatus());
                    taskDetailsBean.setTaskType(taskDetailsBean1.getTaskType());
                    taskDetailsBean.setCustomTagVisible(true);
                    taskDetailsBean.setSendStatus("0");
                    taskDetailsBean.setMsg_status(12);
                    taskDetailsBean.setRead_status(0);
                    taskDetailsBean.setShow_progress(1);
                    taskDetailsBean.setSubType("normal");
                    if (setid != null)
                        taskDetailsBean.setCustomSetId(Integer.parseInt(setid));
                }
                NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                String task_Event = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskDescription from taskDetailsInfo where taskId='" + taskId + "' and taskTagName='Event' and customSetId='" + setid + "'");
                if (task_Event != null && task_Event.equalsIgnoreCase("status")) {
                    String task_value = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskDescription from taskDetailsInfo where taskId='" + taskId + "' and taskTagName='Value' and customSetId='" + setid + "'");
                    if (task_value != null && task_value.equalsIgnoreCase(task_status)) {
                        if (Appreference.context_table.get("mainactivity") != null) {
                            if (Appreference.context_table.containsKey("taskcoversation")) {
                                newTaskConversation.taskBeanListUpdate(taskDetailsBean);
                                Log.i("gcm message", "Escalte status if " + task_value);
                            } else {
                                taskDetailsBean.setRead_status(1);
                                VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                                Log.i("gcm message", "Escalte status else " + task_value);
                            }
                        }
                    }
                } else if (task_Event != null && task_Event.equalsIgnoreCase("% completion")) {
                    String task_value_1 = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskDescription from taskDetailsInfo where taskId='" + taskId + "' and taskTagName='Value' and customSetId='" + setid + "'");
                    int percentage = 0, task_per = 0;
                    if (task_value_1 != null)
                        percentage = Integer.parseInt(task_value_1);
                    if (task_percentage != null)
                        task_per = Integer.parseInt(task_percentage);
                    if (percentage > task_per) {
                        if (Appreference.context_table.get("mainactivity") != null) {
                            if (Appreference.context_table.containsKey("taskcoversation")) {
                                newTaskConversation.taskBeanListUpdate(taskDetailsBean);
                                Log.i("gcm message", "Escalte percentage if " + task_value_1);
                            } else {
                                taskDetailsBean.setRead_status(1);
                                VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                                Log.i("gcm message", "Escalte percentage else " + task_value_1);
                            }
                        }
                    }
                }
                ContactsFragment contactsFragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
                if (contactsFragment != null) {
                    contactsFragment.refresh();
                }
                if (Appreference.context_table.containsKey("taskhistory")) {
                    TaskHistory taskHistory = (TaskHistory) Appreference.context_table.get("taskhistory");
                    if (taskHistory != null) {
                        Log.d("TaskHistory", "Value true refreshed-2");
                        taskHistory.refresh();
                    }
                }
                ProjectHistory project_History = (ProjectHistory) Appreference.context_table.get("projecthistory");
                if (project_History != null) {
                    Log.d("TaskHistory", "Value true refreshed-3");
                    project_History.refresh();
                }
                ProjectsFragment project_fragment = (ProjectsFragment) Appreference.context_table.get("projectfragment");
                if (project_fragment != null) {
                    Log.d("TaskHistory", "Value true refreshed-4");
                    project_fragment.refresh();
                }
            } else if (objectValue != null && (objectValue.contains("taskConversation") || objectValue.contains("taskDateChangedApproval") || objectValue.contains("percentageCompleted") || objectValue.contains("taskAcceptedOrRejected"))) {
                JSONObject jsonObject = new JSONObject(objectValue);
                if (jsonObject != null && jsonObject.has("taskId")) {
                    value = jsonObject.getString("taskId");
                    fromUser_Name = jsonObject.getString("fromUser");
                    Log.i("gcm", "New Task 12** ** ** " + fromUser_Name + "    " + value);
                    fromUser_Name = fromUser_Name.replace("@", "_");
                    Log.i("gcm12", "New Task 12 ** ** *  " + fromUser_Name);
                    fromUser_Name = VideoCallDataBase.getDB(this).getName(fromUser_Name);
                    Log.i("gcm12", "New Task 12 ** * * ** ** " + fromUser_Name);
                }
            } else if (objectValue != null && (objectValue.contains("GiverTask Reminder"))) {

                JSONObject jsonObject = new JSONObject(objectValue);


                String tasktime = null;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateTime = dateFormat.format(new Date());
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                String dateforrow = dateFormat.format(new Date());
                tasktime = dateTime;
                tasktime = tasktime.split(" ")[1];
                Log.i("task", "tasktime" + tasktime);
                Log.i("UTC", "sendMessage utc time" + dateforrow);
                Log.i("time", "value");
//                taskUTCtime = dateforrow;


                if (jsonObject != null && jsonObject.has("taskId")) {
                    TaskDetailsBean bean = null;
                    ArrayList<TaskDetailsBean> beanArrayList = null;
                    try {
                        beanArrayList = VideoCallDataBase.getDB(context).getTaskHistoryInfo("Select * from taskHistoryInfo where taskId ='" + jsonObject.getString("taskId") + "';");
                        if (beanArrayList != null && beanArrayList.size() > 0)
                            bean = beanArrayList.get(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    TaskDetailsBean detailsBean = null;
                    if (bean != null) {
                        detailsBean = new TaskDetailsBean();
                        fromUser_Name = jsonObject.getString("fromUserId");
                        String to_userId = jsonObject.getString("toUserId");
                        value = jsonObject.getString("taskId");
                        detailsBean.setTaskId(value);
                        detailsBean.setTaskDescription("Task Reminder sent.unAnswered Reminder count is : " + jsonObject.getInt("unanswered remidner"));
                        detailsBean.setSignalid(Utility.getSessionID());
                        Log.e("filter", "filter gcm " + detailsBean.getSignalid());
                        detailsBean.setTaskNo(bean.getTaskNo());
                        detailsBean.setMimeType("reminder");
                        detailsBean.setFromUserName(jsonObject.getString("fromUserName"));
                        detailsBean.setToUserName(jsonObject.getString("toUserName"));
                        detailsBean.setToUserId(to_userId);
                        detailsBean.setFromUserId(fromUser_Name);
                        detailsBean.setCustomTagVisible(true);
                        detailsBean.setSendStatus("2");
                        detailsBean.setCatagory(bean.getCatagory());
                        detailsBean.setDateTime(dateTime);
                        detailsBean.setTasktime(dateTime);
                        detailsBean.setTaskUTCDateTime(dateforrow);
                        detailsBean.setTaskUTCTime(dateforrow);
                        detailsBean.setMsg_status(15);
                        detailsBean.setRead_status(0);
                        detailsBean.setShow_progress(0);
                        detailsBean.setTaskType(bean.getTaskType());
                        detailsBean.setTaskStatus(bean.getTaskStatus());
                        detailsBean.setTaskName(bean.getTaskName());
                    }

                    NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                    if (Appreference.context_table.get("mainactivity") != null && detailsBean != null) {
                        if (Appreference.context_table.containsKey("taskcoversation")) {
                            newTaskConversation.taskReminderMessage(detailsBean, null, "text", "reminder", "2");
                        } else {
                            detailsBean.setRead_status(2);
                            VideoCallDataBase.getDB(context).insertORupdate_Task_history(detailsBean);
                        }
                    }


                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("GCMPushReceiver", "Exception " + e.getMessage(), "WARN", null);
        }
        return value;
    }

     /*public void updateOverdueMsg(TaskDetailsBean taskDetailsBean) {
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
    }*/

    private String getParentId() {
        String strFilename = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
            Date date = new Date();
            strFilename = dateFormat.format(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("GCMPushReceiver", "Exception " + e.getMessage(), "WARN", null);
        }
        return strFilename;
    }
}
