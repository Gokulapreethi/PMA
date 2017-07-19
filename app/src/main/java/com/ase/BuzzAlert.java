package com.ase;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;

/**
 * Created by vadivel on 03/19/17.
 */
public class BuzzAlert extends Activity {

    TextView tv_buddyname, title;
    Button ok, view;
    Context context;
    int THUMBSIZE = 64;
    String signalId, buzz_message, chatType, chatid, chatname, messagetype, chatusers, fromname;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chatalert);
        context = this;
        this.setFinishOnTouchOutside(false);
        this.setFinishOnTouchOutside(false);
        Appreference.context_table.put("buzzalert", this);

//        chat_message = getIntent().getStringExtra("chat_message");
        signalId = getIntent().getStringExtra("buzz_signalId");
//        messagetype = getIntent().getStringExtra("messagetype");
//        chatType = getIntent().getExtras().getString("chattype");
//        chatid = getIntent().getExtras().getString("chatid");
//        chatname = getIntent().getExtras().getString("datetime");
//        chatusers=getIntent().getExtras().getString("chatusers");
//        fromname=getIntent().getExtras().getString("fromname");
        Log.i("BuzzAlert", "signalId " + signalId);
        String buzz_message = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskDescription from taskDetailsInfo where signalid='" + signalId + "'");
        Log.i("BuzzAlert", "buzz_message " + buzz_message);
//        Log.i("chatalert", "chatType " + chatType);
//        Log.i("chatalert", "chatname " + chatname);
//        Log.i("chatalert", "chatid " + chatid);
        handler = new Handler();

        tv_buddyname = (TextView) findViewById(R.id.buddy_name);
        view = (Button) findViewById(R.id.layout_decline);
        ok = (Button) findViewById(R.id.layout_accept);
        title = (TextView) findViewById(R.id.tv_title);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        if (buzz_message != null && !buzz_message.equalsIgnoreCase(null) && !buzz_message.equalsIgnoreCase("")) {
            tv_buddyname.setText("Message : " + buzz_message);
            Log.i("BuzzAlert", "text_type ");
        }
        title.setText("Buzz Alert");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TaskDetailsBean taskDetailsBean = VideoCallDataBase.getDB(context).getTaskContent("select * from taskDetailsInfo where signalid='" + signalId + "'");
                try {
                    if (taskDetailsBean != null) {
                        TaskDetailsBean taskDetailsBean_1 = VideoCallDataBase.getDB(context).getFormContent("select * from taskHistoryInfo where taskId='" + taskDetailsBean.getTaskId() + "'");
                        if (taskDetailsBean_1 != null) {
                            taskDetailsBean.setOwnerOfTask(taskDetailsBean_1.getOwnerOfTask());
                            taskDetailsBean.setTaskReceiver(taskDetailsBean_1.getTaskReceiver());
                            taskDetailsBean.setTaskName(taskDetailsBean_1.getTaskName());
                            taskDetailsBean.setTaskObservers(taskDetailsBean_1.getTaskObservers());
                            taskDetailsBean.setGroupTaskMembers(taskDetailsBean_1.getGroupTaskMembers());
                        }
                        Log.i("BuzzAlert", "taskDetailsBean ** " + taskDetailsBean.getTaskReceiver());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(BuzzAlert.this, NewTaskConversation.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("task", "taskhistory");
                intent.putExtra("taskHistoryBean", taskDetailsBean);
                startActivity(intent);
                Log.i("BuzzAlert", "taskDetailsBean contains ?? " + taskDetailsBean);
                Log.d("BuzzAlert", "Message viewed");
                finish();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Appreference.context_table.remove("buzzalert");
        Log.i("BuzzAlert", "BuzzAlert Ondestroy");
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(12345);
    }
}