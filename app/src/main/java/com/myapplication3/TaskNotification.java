package com.myapplication3;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.myapplication3.Bean.TaskDetailsBean;
import com.myapplication3.DB.VideoCallDataBase;
import com.myapplication3.RandomNumber.Utility;

import org.pjsip.pjsua2.app.MainActivity;

import java.io.File;
import java.util.ArrayList;

public class TaskNotification extends Activity {

    TaskDetailsBean taskDetailsBean, taskDetailsBean1;
    ArrayList<TaskDetailsBean> detailsBeanArrayList;
    String from;
    ArrayList<TaskDetailsBean> task_list;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_task_notification);
        detailsBeanArrayList = new ArrayList<>();

        if (getIntent().getSerializableExtra("taskdetailsbean") != null) {
            taskDetailsBean = (TaskDetailsBean) getIntent().getSerializableExtra("taskdetailsbean");
        }
        final int id = getIntent().getExtras().getInt("id");
        if (getIntent().getExtras().getString("from") != null && !getIntent().getExtras().getString("from").equalsIgnoreCase("")) {
            from = getIntent().getExtras().getString("from");
        }
        TextView title = (TextView) findViewById(R.id.tv_title);
        TextView textView = (TextView) findViewById(R.id.buddy_name);
        if (taskDetailsBean.getFromUserEmail() != null) {
            String part = taskDetailsBean.getFromUserEmail();
            Log.i("task", "string value" + part);
            part = part.substring(5);
            Log.i("task", "substring value" + part);
            String part1[] = part.split("@");
            part = VideoCallDataBase.getDB(context).getName(part1[0]);
            Log.i("task", "string value" + part1[0]);
            Log.i("task", "DBvalue" + part);
            textView.setText(part);
        }
        Button ok = (Button) findViewById(R.id.layout_decline);
        Button view = (Button) findViewById(R.id.layout_accept);
        if (taskDetailsBean.getFromUserId() != null && taskDetailsBean.getTaskId() != null && Appreference.loginuserdetails.getUsername() != null) {
            if (taskDetailsBean != null && taskDetailsBean.getFromUserId() != null && taskDetailsBean.getTaskId() != null && Appreference.loginuserdetails.getUsername() != null) {
                String query = "select * from taskDetailsInfo where (fromUserId='" + taskDetailsBean.getFromUserId() + "' or toUserId='" + taskDetailsBean.getFromUserId() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + taskDetailsBean.getTaskId() + "' and mimeType='date'";

                detailsBeanArrayList = VideoCallDataBase.getDB(getApplicationContext()).getTaskHistory(query);
                Log.d("taskTone", "List size" + detailsBeanArrayList.size());
                ArrayList<String> list = null;
                String remainderToneName = null;

                if (detailsBeanArrayList != null && detailsBeanArrayList.size() > 0) {
                    for (TaskDetailsBean detailsBean : detailsBeanArrayList) {

                        if (detailsBean != null && (!detailsBean.getTaskDescription().equalsIgnoreCase("") || !detailsBean.getTaskDescription().equalsIgnoreCase(null))) {
                            list = new ArrayList<>();
                            list.add(detailsBean.getTaskDescription());
                        }

                    }
                }
                if (list != null && list.size() > 0) {

                    Log.d("taskTone", "List size" + list.size());
                    remainderToneName = list.get(list.size() - 1);

                    Log.d("taskTone", "remainderToneName" + remainderToneName);

                    String RootDir = Environment.getExternalStorageDirectory()
                            .getAbsolutePath()
                            + "/High Message/downloads/" + remainderToneName;

                    File file = new File(RootDir);

                    if (file.exists()) {
                        Log.d("taskTone", "play customTone" + remainderToneName);
                        MainActivity.setCustomRingTone(RootDir);
                    } else {
                        MainActivity.startAlarmRingTone();
                    }
                }
            } else {
                MainActivity.startAlarmRingTone();
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.stopRingTone();
                    setResult(Activity.RESULT_OK);
                    TaskNotification.this.finish();

                    Intent intent = new Intent(TaskNotification.this, NewTaskConversation.class);
                    intent.putExtra("taskBean", taskDetailsBean);
                    intent.putExtra("task", "taskHistory");

                    startActivity(intent);
                    Log.i("Task", "AlarmId view " + id);
                }
            });

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.stopRingTone();
                    setResult(Activity.RESULT_CANCELED);
                    TaskNotification.this.finish();
                    Log.i("Task", "AlarmId ok " + id);
                    if (from.equalsIgnoreCase("Alarm")) {
                        Log.d("Task", "OK...cancelled");
                        cancellTask(taskDetailsBean, id);
                    }
                }
            });
        }
    }

    private void cancellTask(TaskDetailsBean taskDetailsBean, int id) {
        task_list = new ArrayList<>();
        try {
            String query_1 = "select * from taskDetailsInfo where (fromUserId='" + taskDetailsBean.getFromUserId() + "' or toUserId='" + taskDetailsBean.getToUserId() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + id + "' and mimeType='date'";
            Log.i("task", "Query " + query_1);
            task_list = VideoCallDataBase.getDB(Appreference.mainContect).getTaskHistory(query_1);
            if (task_list.size() > 0) {
                taskDetailsBean1 = task_list.get(task_list.size() - 1);
                Log.i("task", "Tone is " + taskDetailsBean1.getReminderQuote());
                taskDetailsBean.setTaskDescription(taskDetailsBean1.getReminderQuote());
                VideoCallDataBase.getDB(context).updateTaskMsgPriority(taskDetailsBean.getTaskId());
            }   NewTaskConversation newTaskConversation = new NewTaskConversation();
            if (taskDetailsBean != null || !taskDetailsBean.getTaskDescription().equals("") || !taskDetailsBean.getTaskDescription().equals(null)) {
                String sig_id = Utility.getSessionID();
                newTaskConversation.sendMessage(taskDetailsBean.getTaskDescription(), null, "text", "overdue", "2",sig_id,null);
            }
            taskDetailsBean.setTaskStatus("overdue");
            taskDetailsBean.setRead_status(2);
            taskDetailsBean.setMimeType("text");
            taskDetailsBean.setTaskPriority("High");
            VideoCallDataBase.getDB(this).insertORupdate_Task_history(taskDetailsBean);
            VideoCallDataBase.getDB(this).insertORupdate_TaskHistoryInfo(taskDetailsBean);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    private void cancellAlarmManager(TaskDetailsBean taskDetailsBean, int id) {
        try {

            Log.d("Task", "came to cancellAlarmManager in ScheduleManager");
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent it = new Intent(this, ScheduleManager.class);
            PendingIntent pi = PendingIntent.getBroadcast(this, id,
                    it, 0);
            if (pi != null) {
                Log.d("Task", "pi != null");
                am.cancel(pi);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
