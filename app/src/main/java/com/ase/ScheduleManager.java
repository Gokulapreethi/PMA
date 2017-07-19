package com.ase;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;
import com.ase.RandomNumber.Utility;

import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.app.MyBuddy;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import xml.xmlcomposer;

/**
 * Created by Dinesh on 12/22/2015.
 */
public class ScheduleManager extends BroadcastReceiver {
    public ScheduleManager() {
        super();
    }

    ArrayList<TaskDetailsBean> task_list, detailsBeanArrayList, taskList;

    Context context;
    String endTime = null, currTime = null, amOrpm = null, note = null, signal_id = null;
    ArrayList<String> listOfObservers;
//    NewTaskConversation newTaskConversation = new NewTaskConversation();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("schedulemanager", "-----schedule Time class  1");
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tm = time.format(new Date());

//        SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
//        String dt = date.format(new Date());
//            Toast.makeText(MainActivity.mainContext,"Repeat Alarm",Toast.LENGTH_SHORT).show();
        if (MainActivity.mainContext != null) {
            Log.i("schedulemanager", "Now Tome Is====>" + tm);
//            Log.i("schedulemanager", "Now date Is====>" + dt);

            VideoCallDataBase.getDB(MainActivity.mainContext).openDatabase();
//            String hour = tm.split(":")[0];
//            Log.i("schedulemanager", "hour time--->" + hour + "  hour length--->" + hour.length());
//            if (hour.startsWith("0")) {
//                StringBuffer buffer = new StringBuffer(tm);
//                tm = buffer.deleteCharAt(0).toString();
//            }
            Log.i("schedulemanager", "Remove 0 time--->" + tm);
            listOfObservers = new ArrayList<>();
            listOfObservers.clear();
            int id = intent.getExtras().getInt("id");
            int incre_id = intent.getExtras().getInt("Increment_id");
            Log.i("Task", "alrmtask id in int " + id);
            String alrmtask = String.valueOf(id);
            Log.i("Task", "alrmtask id in string " + alrmtask);
            endTime = intent.getExtras().getString("endTime");
            note = intent.getExtras().getString("note");
            signal_id = intent.getExtras().getString("signal_Id");
            String query = "select * from taskDetailsInfo where loginuser='" + Appreference.loginuserdetails.getEmail() + "'" + "and taskId='" + alrmtask + "'";
            ArrayList<TaskDetailsBean> group = VideoCallDataBase.getDB(
                    Appreference.mainContect).getTaskHistory(query);
            Log.i("Task", "query " + query);
            Log.i("Task", "group " + group.size());
            Calendar cal = Calendar.getInstance();
            int currHour = cal.get(Calendar.HOUR);
            Log.i("Task", "currHour " + currHour);
            int currMin = cal.get(Calendar.MINUTE);
            Log.i("Task", "currTime " + currMin);
            int ampm = cal.get(Calendar.AM_PM);
            Log.i("Task", "ampm " + ampm);
            if (ampm == 0) {
                amOrpm = "am";
            } else {
                amOrpm = "pm";
            }

            String currTime = String.valueOf(currHour) + ":" + String.valueOf(currMin);

            DateFormat selctedDateformate = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
            Date date1 = null;
            try {
                date1 = selctedDateformate.parse("1970-01-01" + " " + currTime + " " + amOrpm);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            SimpleDateFormat day1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String currdate = day1.format(date1);
            currTime = currdate.split(" ")[1];
            Log.i("taskdate", "currTime " + currTime);
            taskList = new ArrayList<>();
            Log.d("schedulemanager", "-----group size---->" + group.size());
            if (group.size() > 0) {
                Log.d("schedulemanager", "-----schedule call group.size>0");
                final TaskDetailsBean callbean = group.get(0);
                try {
                    Log.i("Task", "currTime " + currTime);
                    Log.i("Task", "endTime " + endTime);
                    Log.i("Task", "currTime.compareTo(endTime) " + currTime.compareTo(endTime));
                    Log.i("Task", "AlarmId scheduler " + id);
                    if (currTime.compareTo(endTime) >= 0) {
                        Appreference.is_alarmStop = true;
//                        cancellTask(callbean, context, id);
                        cancellAlarmManager(callbean, context, id);
                        String ownerof_task = VideoCallDataBase.getDB(context).getProjectParentTaskId("select ownerOfTask from taskHistoryInfo where taskId='" + id + "'");
                        callbean.setOwnerOfTask(ownerof_task);
                        Log.i("Task", "buzz value is " + note);
                        Log.i("Task", "buzz ownerof_task is " + ownerof_task);
                        if (Appreference.loginuserdetails.getUsername().equalsIgnoreCase(callbean.getOwnerOfTask()) && (note != null && !note.equalsIgnoreCase("buzz"))) {
                            Log.d("Task", "buzz if");
                            if (note != null && note.equalsIgnoreCase("note")) {
                                taskList = VideoCallDataBase.getDB(context).getTaskHistory("select * from taskDetailsInfo where (fromUserName='" + Appreference.loginuserdetails.getUsername() + "' or toUserName='" + Appreference.loginuserdetails.getUsername() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + id + "' and  mimeType='note'");
                            } else {
                                taskList = VideoCallDataBase.getDB(context).getTaskHistory("select * from taskDetailsInfo where (fromUserName='" + Appreference.loginuserdetails.getUsername() + "' or toUserName='" + Appreference.loginuserdetails.getUsername() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + id + "' and (taskStatus='inprogress' or taskStatus='Assigned' or taskStatus='assigned')");
                            }
                            Log.i("Task", "list size " + taskList.size());
                            if (taskList.size() > 0) {
                                TaskDetailsBean tskbeen = taskList.get(0);
                                Log.i("Task", "date_taskstatus " + tskbeen.getTaskStatus());
                                if (tskbeen.getTaskStatus() != null && !tskbeen.getTaskStatus().equalsIgnoreCase("abandoned")) {
                                    tskbeen.setOwnerOfTask(ownerof_task);
                                    ownerof_task = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskReceiver from taskHistoryInfo where taskId='" + id + "'");
                                    tskbeen.setTaskReceiver(ownerof_task);
                                    ownerof_task = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskName from taskHistoryInfo where taskId='" + id + "'");
                                    tskbeen.setTaskName(ownerof_task);
                                    sendTask(tskbeen, context, id);
                                }
                            }

                        } else if (Appreference.context_table.containsKey("taskcoversation")) {

                        } else if (note != null && note.equalsIgnoreCase("buzz")) {
                            Log.i("Task", "buzz else " + note);
                            Log.i("Task", "buzz else signal_id " + signal_id);
                            Intent intent_1 = new Intent(context, BuzzAlert.class);
                            intent_1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent_1.putExtra("buzz_signalId", signal_id);
                            context.startActivity(intent_1);
//                            intent.putExtra("chat_message", chat_message);
//                            intent.putExtra("messagetype", messagetype);
//                            intent.putExtra("datetime", bean.getChatname());
//                            intent.putExtra("chattype", "SecureChat");
//                            intent.putExtra("chatid", bean.getChatid());
//                            intent.putExtra("chatusers", bean.getChatmembers());
//                            intent.putExtra("fromname", bean.getFromUser());
                        }
                    } else {
//                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
//                TaskNotification
                        /*Intent intent1 = new Intent(context, TaskNotification.class);
                        intent1.putExtra("taskdetailsbean", callbean);
                        intent1.putExtra("from", "Alarm");
                        intent1.putExtra("id",id);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent1);*/
                        if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(callbean.getOwnerOfTask())) {
                            if (callbean.getTaskStatus() != null && !callbean.getTaskStatus().equalsIgnoreCase("abondoned")) {
                                cancellTask1(callbean, id);
                            }
                            Log.i("date", "not owner of the task");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                        if (Appreference.context_table.containsKey("MAIN")) {
//                            Log.d("schedulemanager", "AppReferences.contextTable.containsKey(MAIN)");
//                            Appreference.mainContect.ShowSheduleScreen(((HomeFragment) Appreference.context_table
//                                            .get("MAIN")).getCurrentContext(),
//                                    callbean);
//                        }
            }
        } else if (Appreference.context_table.get("mainactivity") == null && Appreference.context_table.get("loginactivity") == null) {
            Intent intent_2 = new Intent(context, Splash.class);
            PendingIntent pIntent = PendingIntent.getActivity(context, 54321, intent_2, 0);
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("High Message")
                    .setContentText("Buzz notification")
                    .setTicker("Buzz message")
                    .setAutoCancel(true)
                    .setSound(null)
                    .setContentIntent(pIntent);
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify((int) 54321, noBuilder.build()); //0 = ID of notification
        }
    }

    @Override
    public IBinder peekService(Context myContext, Intent service) {
        return super.peekService(myContext, service);
    }

    private void sendTask(TaskDetailsBean taskDetailsBean, Context context, int id) {
        try {
//        if (xmlComposer == null) {
            xmlcomposer xmlComposer = new xmlcomposer();
//        }
//            taskDetailsBean.setTaskDescription("This task is overdue");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = dateFormat.format(new Date());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateforrow = dateFormat.format(new Date());

            String dateforrow1 = dateFormat.format(new Date());

            String tasktime = dateTime;
            tasktime = tasktime.split(" ")[1];
            Log.i("task", "tasktime" + tasktime);
            Log.i("UTC", "sendMessage utc time" + dateforrow);

            Log.i("time", "value");

            String taskUTCtime = dateforrow;
            taskDetailsBean.setTasktime(tasktime);
            taskDetailsBean.setTaskUTCTime(taskUTCtime);
            taskDetailsBean.setDateTime(dateTime);
            taskDetailsBean.setTaskUTCDateTime(dateforrow);
            taskDetailsBean.setMimeType("text");
            /*String from = taskDetailsBean.getFromUserName();
            String to = taskDetailsBean.getToUserName();*/
            taskDetailsBean.setSignalid(Utility.getSessionID());
            Log.d("Note", "getReminderQuote " + taskDetailsBean.getReminderQuote());
            if (note != null && note.equalsIgnoreCase("note")) {
                if (taskDetailsBean.getReminderQuote() != null && !taskDetailsBean.getReminderQuote().equalsIgnoreCase("") && !taskDetailsBean.getReminderQuote().equalsIgnoreCase(null)) {
                    Log.d("Note", "getReminderQuote2 " + taskDetailsBean.getReminderQuote());
                    taskDetailsBean.setTaskDescription(taskDetailsBean.getReminderQuote());
                } else {
                    Log.d("Note", "getReminderQuote3 " + taskDetailsBean.getReminderQuote());
                    taskDetailsBean.setTaskDescription("Remind Me");
                }
                taskDetailsBean.setTaskStatus("reminder");
            } else {
                taskDetailsBean.setTaskDescription("This task is overdue");
                taskDetailsBean.setTaskStatus("overdue");
            }
            taskDetailsBean.setRead_status(2);
            taskDetailsBean.setCustomTagVisible(true);

//            cancellAlarmManager(taskDetailsBean, context, id);
            if (Appreference.context_table.containsKey("taskcoversation")) {
                Log.d("Note", "getReminderQuote1** " + taskDetailsBean.getTaskDescription());
                Log.d("Note", "getTaskStatus** " + taskDetailsBean.getTaskStatus());
//                VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                NewTaskConversation taskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                if (taskConversation != null) {
//                    taskConversation.TaskOverdueNotify(taskDetailsBean);
                    Log.d("Task", "ScheduleManager if fired");
                    taskDetailsBean.setRead_status(0);
                    taskConversation.taskOverdueMessage(taskDetailsBean, "text", taskDetailsBean.getTaskStatus(), 1);
                }
            } else if (Appreference.context_table.containsKey("taskhistory")) {
                Log.d("Note", "getReminderQuote1*** " + taskDetailsBean.getTaskDescription());
//                VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                TaskHistory taskHistory = (TaskHistory) Appreference.context_table.get("taskhistory");
                if (taskHistory != null) {
//                    taskConversation.TaskOverdueNotify(taskDetailsBean);

                    taskDetailsBean.setRead_status(2);
                    Log.d("Task", "ScheduleManager else fired");
                    VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                    VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(taskDetailsBean);
                    Log.d("Task", "ScheduleManager if fired");
                    taskHistory.refresh();


                }
            } else {
                Log.d("Note", "getReminderQuote1### " + taskDetailsBean.getReminderQuote());
                taskDetailsBean.setRead_status(2);
                Log.d("Task", "ScheduleManager else fired");
                VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(taskDetailsBean);
                if (!listOfObservers.contains(taskDetailsBean.getToUserName())) {
                    listOfObservers.add(taskDetailsBean.getToUserName());
                }
                if (taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
                    getTaskObservers(taskDetailsBean);
                    getGroupTaskMembers(taskDetailsBean);
                } else {
                    getTaskObservers(taskDetailsBean);
                }
                Log.i("task", "ScheduleManager else listOfObservers " + listOfObservers);
                if (note != null && note.equalsIgnoreCase("note")) {
                    String tasksipxml = xmlComposer.composeTaskCancellinfo(taskDetailsBean);
                    sendMultiInstantMessage(tasksipxml, listOfObservers, context);
                }
                /*Log.i("Task", "composed tasksipxml : " + tasksipxml + " \n to user " + from + "  to user" + to);
                String tousersipendpoint = "sip:" + from + "@" + context.getResources().getString(R.string.server_ip);
                for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
                    String name = MainActivity.account.buddyList.get(i).cfg.getUri();

                    if (name.equalsIgnoreCase(tousersipendpoint)) {
                        MyBuddy myBuddy = MainActivity.account.buddyList.get(i);
                        SendInstantMessageParam prm = new SendInstantMessageParam();
                        prm.setContent(tasksipxml);
                        boolean valid = myBuddy.isValid();
                        Log.i("Task", "valid =======> " + valid);
                        try {
                            myBuddy.sendInstantMessage(prm);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }*/
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getTaskObservers(TaskDetailsBean taskbeen_1) {
        String query1 = "select * from taskDetailsInfo where loginuser ='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + taskbeen_1.getTaskId() + "' and sendStatus = '3' order by id DESC LIMIT 1";

        Log.d("TaskObserver", "get Observer query  " + query1);

        ArrayList<TaskDetailsBean> arrayList;
        arrayList = VideoCallDataBase.getDB(context).getTaskHistory(query1);

        Log.d("TaskObserver", "Task Observer list size is == " + arrayList.size());
        if (arrayList.size() > 0) {
            TaskDetailsBean taskDetailsBean = arrayList.get(0);

            String taskObservers = taskDetailsBean.getTaskObservers();
            Log.d("TaskObserver", "Task Observer  == " + taskObservers);
            /*boolean check = false;
            if (taskObservers.contains(Appreference.loginuserdetails.getUsername())) {
                check = false;
            } else if (ownerOfTask.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                check = false;
            } else
                check = !taskReceiver.equalsIgnoreCase(Appreference.loginuserdetails.getUsername());*/

            int counter = 0;
            for (int i = 0; i < taskObservers.length(); i++) {
                if (taskObservers.charAt(i) == ',') {
                    counter++;
                }
            }
            Log.d("TaskObserver", "Task Observer counter size is == " + counter);

            for (int j = 0; j < counter + 1; j++) {
                if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(taskObservers.split(",")[j])) {
                    listOfObservers.add(taskObservers.split(",")[j]);
                    Log.d("TaskObserver", "Task Observer name not in same user== " + taskObservers.split(",")[j]);
                } else {
                    if (!taskbeen_1.getToUserName().equalsIgnoreCase(taskbeen_1.getOwnerOfTask())) {
                        if (!listOfObservers.contains(taskbeen_1.getOwnerOfTask())) {
                            listOfObservers.add(taskbeen_1.getOwnerOfTask());
                        }
                    }
                    if (!taskbeen_1.getToUserName().equalsIgnoreCase(taskbeen_1.getTaskReceiver())) {
                        if (!listOfObservers.contains(taskbeen_1.getTaskReceiver())) {
                            listOfObservers.add(taskbeen_1.getTaskReceiver());
                        }
                    }
                }
            }
            if (taskbeen_1.getTaskReceiver() != null && taskbeen_1.getTaskReceiver().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {

            }
            if (taskbeen_1.getTaskReceiver() != null && !taskbeen_1.getTaskReceiver().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                boolean ch = false;
                for (int i = 0; i < listOfObservers.size(); i++) {
                    String name = listOfObservers.get(i);
                    if (name.equalsIgnoreCase(taskbeen_1.getTaskReceiver())) {
                        ch = true;
                        break;
                    }
                }
                if (!ch)
                    listOfObservers.add(taskbeen_1.getTaskReceiver());
            }
            /*if (!ownerOfTask.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                addObserver.setVisibility(View.GONE);
                update.setVisibility(View.GONE);
            } else
                addObserver.setVisibility(View.VISIBLE);*/
            for (String ob : listOfObservers) {
                Log.i("TaskObserver", "observer12" + ob);
            }
            /*for(String observer:listOfObservers)

            {
                if(observer.equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))
                {
                    check=true;

                    Log.i("TaskObserver","observer123"+observer);
                    Log.i("TaskObserver ",observer+"       "+Appreference.loginuserdetails.getUsername());
                    break;
                }
            }*/
            /*if (check) {
                icons.setVisibility(View.GONE);
                linear1.setVisibility(View.GONE);
                listOfObservers.clear();
            }*/
           /* if(rejected!=null) {
                for (String name : listOfObservers) {
                    for (String rejectname : rejected) {
                        if (rejectname.equalsIgnoreCase(name)) {
                            listOfObservers.remove(name);
                        }
                    }



                *//*if(!name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && !ownerOfTask.equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && !taskReceiver.equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))
                {
                    bottom_layout.setVisibility(View.GONE);
                }*//*

                }


            }*/

        }
    }

    public void getGroupTaskMembers(TaskDetailsBean taskbeen_2) {
        String query1 = "select * from taskDetailsInfo where loginuser ='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + taskbeen_2.getTaskId() + "' and taskType = 'Group' order by id";

        Log.d("GroupTask", "get GroupMember query  " + query1);

        ArrayList<TaskDetailsBean> arrayList;
        arrayList = VideoCallDataBase.getDB(context).getTaskHistory(query1);

        Log.d("GroupTask", "Task group Member list size is == " + arrayList.size());
        if (arrayList.size() > 0) {
            TaskDetailsBean taskDetailsBean = arrayList.get(0);

            String groupTaskMembers = taskDetailsBean.getGroupTaskMembers();
            Log.d("GroupTask", "Task Group Member names  == " + groupTaskMembers);
            Log.d("GroupTask", "Task Group Member description  == " + taskDetailsBean.getTaskDescription());

            if (groupTaskMembers != null) {
                int counter = 0;
                for (int i = 0; i < groupTaskMembers.length(); i++) {
                    if (groupTaskMembers.charAt(i) == ',') {
                        counter++;
                    }
                }
                Log.d("GroupTask", "Task GroupTask counter size is == " + counter);

                for (int j = 0; j < counter + 1; j++) {
                    if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(groupTaskMembers.split(",")[j])) {
                        listOfObservers.add(groupTaskMembers.split(",")[j]);
                        Log.d("GroupTask", "Task GroupTask name not in same user== " + groupTaskMembers.split(",")[j]);
                    } else {
                        if (!taskbeen_2.getToUserName().equalsIgnoreCase(taskbeen_2.getOwnerOfTask())) {
                            if (!listOfObservers.contains(taskbeen_2.getOwnerOfTask())) {
                                listOfObservers.add(taskbeen_2.getOwnerOfTask());
                            }
                        }
                        if (!taskbeen_2.getToUserName().equalsIgnoreCase(taskbeen_2.getTaskReceiver())) {
                            if (!listOfObservers.contains(taskbeen_2.getTaskReceiver())) {
                                listOfObservers.add(taskbeen_2.getTaskReceiver());
                            }
                        }
                    }
                }
            }

            /*for (String name : listOfObservers) {

                *//*if(!name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && !ownerOfTask.equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && !taskReceiver.equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))
                {
                    bottom_layout.setVisibility(View.GONE);
                }*//*

            }*/

        }

    }

    public void sendMultiInstantMessage(String msgBody, ArrayList<String> userlist, Context context) {

//        String buddy_uri = "<"+number+">";
//        Log.i("chat", "buddy_uri======= " + buddy_uri);
//        BuddyConfig bCfg = new BuddyConfig();
//        bCfg.setUri(buddy_uri);
//        bCfg.setSubscribe(true);
        for (String name : userlist) {
            Log.i("task observer", "observer 1 " + name);
        }

        for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
            String name = MainActivity.account.buddyList.get(i).cfg.getUri();
            Log.i("task", "buddyname-->" + name);
            for (String username : userlist) {
                Log.i("task", "taskObservers Name-->" + username);
                String nn = "sip:" + username + "@" + context.getResources().getString(R.string.server_ip);
                Log.i("task", "selected user-->" + nn);
                if (nn.equalsIgnoreCase(name)) {
                    Log.i("task", "both users are same");
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

    private void cancellTask1(TaskDetailsBean taskDetailsBean, int id) {
        task_list = new ArrayList<>();
        try {
            if (taskDetailsBean.getFromUserId() != null && taskDetailsBean.getTaskId() != null && Appreference.loginuserdetails.getUsername() != null) {
                if (taskDetailsBean != null && taskDetailsBean.getFromUserId() != null && taskDetailsBean.getTaskId() != null && Appreference.loginuserdetails.getUsername() != null) {
                    String query = "select * from taskDetailsInfo where (fromUserId='" + taskDetailsBean.getFromUserId() + "' or toUserId='" + taskDetailsBean.getFromUserId() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + taskDetailsBean.getTaskId() + "' and mimeType='date'";

                    detailsBeanArrayList = VideoCallDataBase.getDB(context).getTaskHistory(query);
                    Log.d("taskTone", "List size" + detailsBeanArrayList.size());
                    ArrayList<String> list = null;
                    String remainderToneName = null;

                    if (detailsBeanArrayList != null && detailsBeanArrayList.size() > 0) {
                        for (TaskDetailsBean detailsBean : detailsBeanArrayList) {

                            if (detailsBean != null && (!detailsBean.getTaskDescription().equalsIgnoreCase("") || !detailsBean.getTaskDescription().equalsIgnoreCase(null)) && (detailsBean.getTaskDescription().contains(".mp3") || detailsBean.getTaskDescription().contains(".wav"))) {
                                list = new ArrayList<>();
                                list.add(detailsBean.getTaskDescription());
                            }

                        }
                    }

                    if (list != null && list.size() > 0) {

                        Log.d("taskTone", "List size " + list.size());
                        remainderToneName = list.get(list.size() - 1);
                        Log.d("taskTone", "remainderToneName " + remainderToneName);

                        String RootDir = Environment.getExternalStorageDirectory()
                                .getAbsolutePath()
                                + "/High Message/downloads/" + remainderToneName;

                        File file = new File(RootDir);

                        if (file.exists()) {
                            Log.d("taskTone", "play customTone " + remainderToneName);
                            MainActivity.setCustomRingTone(RootDir);
                        } else {
                            MainActivity.startAlarmRingTone();
                        }
                    }
                } else {
                    MainActivity.startAlarmRingTone();
                }
            }

            String query_1 = "select * from taskDetailsInfo where (fromUserId='" + taskDetailsBean.getFromUserId() + "' or toUserId='" + taskDetailsBean.getToUserId() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + id + "' and mimeType='date'";
            Log.i("task", "Query " + query_1);
            task_list = VideoCallDataBase.getDB(Appreference.mainContect).getTaskHistory(query_1);
            if (task_list.size() > 0) {
                TaskDetailsBean taskDetailsBean1 = task_list.get(task_list.size() - 1);
                Log.i("task", "Tone is " + taskDetailsBean1.getReminderQuote());
                taskDetailsBean.setTaskDescription(taskDetailsBean1.getReminderQuote());
                taskDetailsBean.setTaskName(taskDetailsBean1.getTaskName());
                taskDetailsBean.setTaskId(taskDetailsBean1.getTaskId());
                taskDetailsBean.setTaskNo(taskDetailsBean1.getTaskNo());
                taskDetailsBean.setPlannedStartDateTime(taskDetailsBean1.getPlannedStartDateTime());
                taskDetailsBean.setPlannedEndDateTime(taskDetailsBean1.getPlannedEndDateTime());
                taskDetailsBean.setRemainderFrequency(taskDetailsBean1.getRemainderFrequency());
                taskDetailsBean.setIsRemainderRequired(taskDetailsBean1.getIsRemainderRequired());
                taskDetailsBean.setTaskStatus(taskDetailsBean1.getTaskStatus());
//                taskDetailsBean.setFromUserName(taskDetailsBean1.getToUserName());
//                taskDetailsBean.setToUserName(taskDetailsBean1.getFromUserName());
                taskDetailsBean.setFromUserName(Appreference.loginuserdetails.getUsername());
                taskDetailsBean.setToUserName(taskDetailsBean1.getOwnerOfTask());
                taskDetailsBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
                taskDetailsBean.setToUserId(taskDetailsBean1.getFromUserId());
                taskDetailsBean.setTaskType(taskDetailsBean1.getTaskType());
//                taskDetailsBean.setMimeType(taskDetailsBean1.getMimeType());
                taskDetailsBean.setMsg_status(6);
                taskDetailsBean.setCompletedPercentage(taskDetailsBean1.getCompletedPercentage());
//                taskDetailsBean.
//                taskDetailsBean.setDateFrequency(taskDetailsBean1.setDateFrequency());
                VideoCallDataBase.getDB(context).updateTaskMsgPriority(taskDetailsBean.getTaskId());
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = dateFormat.format(new Date());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateforrow = dateFormat.format(new Date());

            String dateforrow1 = dateFormat.format(new Date());

            String tasktime = dateTime;
            tasktime = tasktime.split(" ")[1];
            Log.i("task", "tasktime" + tasktime);
            Log.i("UTC", "sendMessage utc time" + dateforrow);

            Log.i("time", "value");

            String taskUTCtime = dateforrow;
            taskDetailsBean.setTasktime(tasktime);
            taskDetailsBean.setTaskUTCTime(taskUTCtime);
            taskDetailsBean.setDateTime(dateTime);
            taskDetailsBean.setTaskUTCDateTime(dateforrow);
            taskDetailsBean.setTaskStatus("reminder");
            taskDetailsBean.setRead_status(2);
            taskDetailsBean.setSignalid(Utility.getSessionID());
            taskDetailsBean.setMimeType("text");
            taskDetailsBean.setTaskPriority("High");
            taskDetailsBean.setCustomTagVisible(true);
//            VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);

            if (Appreference.context_table.containsKey("taskcoversation")) {
                if (taskDetailsBean.getTaskDescription() != null && !taskDetailsBean.getTaskDescription().equalsIgnoreCase("") && !taskDetailsBean.getTaskDescription().equalsIgnoreCase(null)) {
                    taskDetailsBean.setRead_status(0);
                    NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                    if (newTaskConversation != null) {
                        newTaskConversation.taskReminderMessage(taskDetailsBean, null, "text", "reminder", "2");
                        Log.d("Popup", "if method fire");
                    }
//                    newTaskConversation.notifyTaskReceived(taskDetailsBean);
                }
            } else if (Appreference.context_table.containsKey("taskhistory")) {
//                VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                TaskHistory taskHistory = (TaskHistory) Appreference.context_table.get("taskhistory");
                if (taskHistory != null) {
//                    taskConversation.TaskOverdueNotify(taskDetailsBean);

                    taskDetailsBean.setRead_status(2);
                    Log.d("Task", "ScheduleManager else fired");
                    VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                    VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(taskDetailsBean);
                    if (taskDetailsBean.getProjectId() != null) {
                        taskDetailsBean.setTaskStatus("inprogress");
                        VideoCallDataBase.getDB(context).update_Project_history(taskDetailsBean);
                    }
                    Log.d("Task", "ScheduleManager if fired");
                    taskHistory.refresh();
                }
            } else {
                Appreference.printLog("Remainder", "TimeDetails" + taskDetailsBean.getDateTime(), "DEBUG", null);
                Log.i("Popup", "else method taskid " + taskDetailsBean.getTaskId());
                VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(taskDetailsBean);
                if (taskDetailsBean.getProjectId() != null) {
                    taskDetailsBean.setTaskStatus("inprogress");
                    VideoCallDataBase.getDB(context).update_Project_history(taskDetailsBean);
                }
                Log.d("Popup", "else method fire");
            }

            /*if(Appreference.context_table.containsKey("taskhistory")){
                TaskHistory taskHistory=(TaskHistory) Appreference.context_table.get("taskhistory");
                taskHistory.refresh();
            }*/
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void cancellAlarmManager(TaskDetailsBean taskDetailsBean, Context contex, int id) {
        try {
            Log.d("Task", "came to cancellAlarmManager in ScheduleManager");
            AlarmManager am = (AlarmManager) contex.getSystemService(Context.ALARM_SERVICE);
            Intent it = new Intent(contex, ScheduleManager.class);
            PendingIntent pi = PendingIntent.getBroadcast(contex, id, it, 0);
            if (pi != null) {
                Log.d("Task", "pi != null");
                am.cancel(pi);
                pi.cancel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
