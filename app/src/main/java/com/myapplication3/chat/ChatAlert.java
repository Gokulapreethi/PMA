package com.myapplication3.chat;

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

import com.myapplication3.Appreference;
import com.myapplication3.DB.VideoCallDataBase;
import com.myapplication3.R;

import java.util.ArrayList;

public class ChatAlert extends Activity {

    TextView tv_buddyname, title;
    Button ok, view;
    Context context;
    int THUMBSIZE = 64;
    String name, chat_message, chatType, chatid, chatname, messagetype,chatusers,fromname;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chatalert);
        context = this;
        this.setFinishOnTouchOutside(false);
        this.setFinishOnTouchOutside(false);
        Appreference.context_table.put("chatalert", this);

        chat_message = getIntent().getStringExtra("chat_message");
        name = getIntent().getStringExtra("username");
        messagetype = getIntent().getStringExtra("messagetype");
        chatType = getIntent().getExtras().getString("chattype");
        chatid = getIntent().getExtras().getString("chatid");
        chatname = getIntent().getExtras().getString("datetime");
        chatusers=getIntent().getExtras().getString("chatusers");
        fromname=getIntent().getExtras().getString("fromname");

        String username= VideoCallDataBase.getDB(context).getname(fromname);
        Log.i("chatalert", "userName " + username);
        Log.i("chatalert", "chatType " + chatType);
        Log.i("chatalert", "chatname " + chatname);
        Log.i("chatalert", "chatid " + chatid);
        handler = new Handler();

        tv_buddyname = (TextView) findViewById(R.id.buddy_name);
        view = (Button) findViewById(R.id.layout_decline);
        ok = (Button) findViewById(R.id.layout_accept);
        title = (TextView) findViewById(R.id.tv_title);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        if (messagetype != null && messagetype.equalsIgnoreCase("text")) {
            tv_buddyname.setText("Message : " + chat_message);
            Log.i("chatalert", "text_type ");
        } else if (messagetype != null && messagetype.equalsIgnoreCase("image") || messagetype.equalsIgnoreCase("sketch")) {
            tv_buddyname.setText(username + " has send a image");
        }  else if (messagetype != null && messagetype.equalsIgnoreCase("video")) {
            tv_buddyname.setText(username + " has send a video");
        } else if (messagetype != null && messagetype.equalsIgnoreCase("audio")) {
            tv_buddyname.setText(username + " has send a audio");
        } else if (messagetype != null && messagetype.equalsIgnoreCase("file")) {
            tv_buddyname.setText(username + " has send a file");
        } else {
            tv_buddyname.setText(username + " has send a message");
        }
        title.setText("Chat from " + username);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> chatUsers = new ArrayList<String>();
                if(chatUsers!=null && chatusers.contains(",")){
                    String users[]=chatusers.split(",");
                    for(String names:users){
                        chatUsers.add(names);
                    }
                }
                Intent intent = new Intent(ChatAlert.this, ChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("datetime", chatname);
                intent.putExtra("chattype", "SecureChat");
                intent.putExtra("chatid", chatid);
                intent.putStringArrayListExtra("users", chatUsers);

                Log.i("chat", "chatname ** " + chatname);
                Log.i("chat", "chatId ** " + chatid);
                Log.i("chat", "chatId ** " + chatUsers);
                startActivity(intent);
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
        Appreference.context_table.remove("chatalert");
        Log.i("incomingcall", "ChatAlert Ondestroy");
        Log.i("MainCall", "ChatAlert");
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(12345);
    }
}
