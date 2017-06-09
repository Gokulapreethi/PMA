package com.myapplication3.chat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.transition.Fade;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.myapplication3.Appreference;
import com.myapplication3.AudioRecorder;
import com.myapplication3.Bean.TaskDetailsBean;
import com.myapplication3.CustomVideoCamera;
import com.myapplication3.DB.VideoCallDataBase;
import com.myapplication3.FilePicker;
import com.myapplication3.HandSketchActivity2;
import com.myapplication3.R;
import com.myapplication3.RandomNumber.Utility;

import org.json.JSONException;
import org.json.JSONObject;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.app.MyBuddy;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeSet;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.WebServiceInterface;


public class ChatActivity extends Activity implements WebServiceInterface {

    Button back, send, add_buddy, addMedia, scheduled, timetoLive;
    ListView chat_listview;
    EditText ed_message;
    public ArrayList<ChatBean> chatList;
    public ChatAdapter chatAdapter;
    Context context;
    Handler handler = new Handler();
    ArrayList<String> userlist;
    private String quotes = "\"";
    String chatType;
    String chatid;
    String chatname;
    String strIPath;
    private ChatBean cBean;
    private String file_extension;
    LinearLayout ll_networkUI = null;
    TextView tv_networkstate = null;


    //date for chat schdule


    String today,  end_date1, enDate = null;
    public static Typeface normal_type;
    private String strTime;
    private static String ampm = null;
    static String st_hour;
    static String strenddate;
    String time_check;
    String chatuser = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setupWindowAnimations();
        context = this;
        Appreference.context_table.put("chatactivity", this);
        ll_networkUI = (LinearLayout) findViewById(R.id.ll_networkstate);
        tv_networkstate = (TextView) findViewById(R.id.tv_networksate);
        Intent intent = getIntent();
        if (intent.getStringArrayListExtra("users") != null) {
            userlist = intent.getStringArrayListExtra("users");
        } else {
            userlist = new ArrayList<>();
        }
        chatType = getIntent().getExtras().getString("chattype");
        chatid = getIntent().getExtras().getString("chatid");
        chatname = getIntent().getExtras().getString("datetime");
        Log.i("chat", "CA chatid-->" + chatid);
        Log.i("chat", "CA chatName-->" + chatname);
        Log.i("chat", "CA oncreate ");
        Button photo_button = (Button) findViewById(R.id.photo);
        Button video_button = (Button) findViewById(R.id.video);
        Button audio_button = (Button) findViewById(R.id.audio);
        Button file_button = (Button) findViewById(R.id.file);
        Button write_button = (Button) findViewById(R.id.write);

        back = (Button) findViewById(R.id.btn_back);
        chat_listview = (ListView) findViewById(R.id.chatlist);
        send = (Button) findViewById(R.id.send);
        ed_message = (EditText) findViewById(R.id.edit_message);
        add_buddy = (Button) findViewById(R.id.btn_addbuddy);
        addMedia = (Button) findViewById(R.id.img_send);
        scheduled = (Button) findViewById(R.id.scheduled);
        timetoLive = (Button) findViewById(R.id.timetoLive);
        loadstoreddatas();


        chatAdapter = new ChatAdapter(context, chatList);
        chat_listview.setAdapter(chatAdapter);
        chat_listview.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        scheduled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showdatetime_dialog(2);
                time_check = "scheduled";
                if (end_date1 != null) {
                    showCustomDatePicker(end_date1);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentDateandTime = sdf.format(new Date());
                    Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime);
                    showCustomDatePicker(currentDateandTime);
                }
            }
        });

        timetoLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_check = "timetoLive";
                if (end_date1 != null) {
                    showCustomDatePicker(end_date1);
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentDateandTime = sdf.format(new Date());
                    Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime);
                    showCustomDatePicker(currentDateandTime);
                }
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (MainActivity.username != null) {
                        if (ed_message.getText().toString().trim().length() > 0) {
                            sendMessage(ed_message.getText().toString().trim(),
                                    null, "text", null, "text");
                            ed_message.setText("");
                        }
                    }
                    end_date1 = null;
                    time_check = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        addMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                multimediaImage("image");

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        add_buddy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BuddyAdd.class);
                intent.putExtra("userlist", userlist);
                startActivityForResult(intent, 1);
            }
        });

        photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multimediaImage("image");
            }
        });

        video_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multimediaImage("video");
            }
        });

        audio_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatActivity.this, AudioRecorder.class);
                i.putExtra("task", "chat");
                startActivityForResult(i, 333);
            }
        });

        file_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatActivity.this, FilePicker.class);
                startActivityForResult(i, 55);
            }
        });

        write_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                startActivityForResult(i, 423);
            }
        });
        VideoCallDataBase.getDB(context).updateaccept("update chat set opened='0' where chatid='" + chatid + "' and (opened='1' or opened='0')");

    }

    private void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Fade fade = new Fade();
            fade.setDuration(1000);
            getWindow().setEnterTransition(fade);
        }
    }

    public void sendMessage(final String message, final String localpath,
                            final String type, final String imagename, final String quality) {
        try {

            Log.d("chat", "------sendMessage entry------");

            String toUsers = "";

            for (String toUser : userlist) {
                toUsers += toUser + ",";
            }
            if (toUsers.equals("") == false) {
                toUsers = toUsers.substring(0, toUsers.length() - 1);
                Log.d("chat", "CA touser-->" + toUsers);
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        "dd/MM/yyyy hh:mm:ss a");
                String dateforrow = dateFormat.format(new Date());

                final ChatBean chatBean = new ChatBean();
                chatBean.setUsername(MainActivity.username);
                chatBean.setFromUser(MainActivity.username);
                chatBean.setMsgtype(type);
                chatBean.setMessage(message);
                chatBean.setType(chatType);
                chatBean.setChatname(chatname);
                chatBean.setChatid(chatid);
                chatBean.setDatetime(dateforrow);
                chatBean.setSignalid(Utility.getSessionID());
                chatBean.setToname(toUsers);
                chatBean.setOpened("0");
                if(time_check != null && time_check.equalsIgnoreCase("timetoLive")){
                    chatBean.setScheduled("timetoLive,"+end_date1);
                } else if(time_check!= null && time_check.equalsIgnoreCase("scheduled")){
                    chatBean.setScheduled("scheduled,"+end_date1);
                } else {
                    chatBean.setScheduled(end_date1);
                }
//                        chatBean.setChatmembers(toUsers+",sip:"+MainActivity.username+"@"+getResources().getString(R.string.server_ip));
                TreeSet<String> names = new TreeSet<>();
                if (toUsers != null && toUsers.contains(",")) {
                    String nm[] = toUsers.split(",");
                    for (String user : nm) {
                        names.add(user);
                    }
                    names.add(MainActivity.username);
                } else {
                    names.add(toUsers);
                    names.add(MainActivity.username);
                }
                String unames = null;
                for (String ur : names) {
                    if (unames == null) {
                        unames = ur;
                    } else {
                        unames = unames + "," + ur;
                    }
                }
//                        chatBean.setChatmembers(toUsers+","+MainActivity.username);
                chatBean.setChatmembers(unames);
//                        chatBean.setMessageStatus("0");
//                        chatBean.setOpened("1");
//                        chatBean.setConferenceuri(confURI);
                chatList.add(chatBean);
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        chatAdapter.notifyDataSetChanged();
                    }
                });
                if (userlist != null && userlist.size() > 0) {
                    VideoCallDataBase.getDB(context).insertChat_history(chatBean);
//                            for (String name : userlist) {
//                                Log.d("chat", "chat send name--->" + name + "message--->" + message);
                    String xml = composeChatXML(chatBean);
                    sendInstantMessage(xml);
//                            }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendInstantMessage(String msgBody) {

//        String buddy_uri = "<"+number+">";
//        Log.i("chat", "buddy_uri======= " + buddy_uri);
//        BuddyConfig bCfg = new BuddyConfig();
//        bCfg.setUri(buddy_uri);
//        bCfg.setSubscribe(true);
        Log.i("chat", "Buddy list size()---> " + MainActivity.account.buddyList.size());
        for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
            String name = MainActivity.account.buddyList.get(i).cfg.getUri();
            Log.i("chat", "buddyname--> " + name);
            for (String username : userlist) {
                String nn = "sip:" + username + "@" + getResources().getString(R.string.server_ip);
                Log.i("chat", "selected user--> " + nn);
                if (nn.equalsIgnoreCase(name)) {
                    Log.i("chat", "both users are same ");
                    Appreference.printLog("ChatMessage", msgBody, "DEBUG", null);
                    MyBuddy myBuddy = MainActivity.account.buddyList.get(i);
                    SendInstantMessageParam prm = new SendInstantMessageParam();
                    prm.setContent(msgBody);

                    boolean valid = myBuddy.isValid();
                    Log.i("chat", "valid ======= " + valid);
                    try {
                        myBuddy.sendInstantMessage(prm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

//        MyBuddy myBuddy = MainActivity.account.buddyList.get(0);
//        SendInstantMessageParam prm = new SendInstantMessageParam();
//        prm.setContent(msgBody);
//
//        boolean valid = myBuddy.isValid();
//        Log.i("chat", "valid ======= " + valid);
//
////        Account account=new Account();
//        try {
////            myBuddy.create(account, bCfg);
//            myBuddy.sendInstantMessage(prm);
////            myBuddy.delete();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
    }

    @Override
    protected void onDestroy() {
        Appreference.context_table.remove("chatactivity");
        super.onDestroy();
    }


    public void notifyTimeReferesh(ChatBean chatBean) {
        Log.i("chat", "CA chatscreen chat Received Schedule");
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentDateandTime = sdf.format(new Date());
        if (chatname.trim().equalsIgnoreCase(chatBean.getChatname().trim())) {
            Log.i("chat", "CA chatscreen chat name same ");
//            chatBean.setUsername(MainActivity.username);
            Log.i("chat", "CA opened chatActivity " + chatBean.getOpened());
//            chatBean.setDatetime(chatBean.getFromUser() + " " + currentDateandTime);
            Log.i("chat", "CA opened chatActivity " + chatList.size());
            for(int i=0;i<chatList.size();i++){
                if(chatBean.getSignalid().equalsIgnoreCase(chatList.get(i).getSignalid())){
                    chatList.remove(i);
                    break;
                }
            }
            Log.i("chat", "CA opened chatActivity " + chatList.size());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    chatAdapter.notifyDataSetChanged();
                }
            });
//            loadstoreddatas();
        }
    }

    public void notifyScheduleReferesh(ChatBean chatBean) {
        Log.i("chat", "CA chatscreen chat Received Schedule");
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentDateandTime = sdf.format(new Date());
        if (chatname.trim().equalsIgnoreCase(chatBean.getChatname().trim())) {
            Log.i("chat", "CA chatscreen chat name same ");
            chatBean.setUsername(MainActivity.username);
            Log.i("chat", "CA opened chatActivity " + chatBean.getOpened());
//            chatBean.setDatetime(chatBean.getFromUser() + " " + currentDateandTime);
            chatList.add(chatBean);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    chatAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    public void notifychatReceived(ChatBean chatBean) {

        Log.i("chat", "CA chatscreen chat Received ");
//            ChatBean bean = new ChatBean();
//            bean.setFromUser(FromUri);
//            bean.setMsgtype("text");
//            bean.setMessage(message);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String currentDateandTime = sdf.format(new Date());
        Log.i("chat", "CA chatscreen chat chatname "+chatBean.getChatname().trim());
        Log.i("chat", "CA chatscreen chat chatname : "+chatname.trim());
        if (chatname.trim().equalsIgnoreCase(chatBean.getChatname().trim())) {
            Log.i("chat", "CA chatscreen chat name same ");
            chatBean.setUsername(MainActivity.username);

            try {
                time_check=chatBean.getScheduled().split(",")[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (chatBean.getScheduled() != null && !chatBean.getScheduled().equalsIgnoreCase("") && !chatBean.getScheduled().equalsIgnoreCase("null") && time_check.equalsIgnoreCase("scheduled")) {
                chatBean.setOpened("2");
            } else {
                chatBean.setOpened("0");
            }
            Log.i("chat", "CA opened chatActivity " + chatBean.getOpened());
//            chatBean.setDatetime(chatBean.getFromUser() + " " + currentDateandTime);
            VideoCallDataBase.getDB(context).insertChat_history(chatBean);
            if (chatBean.getScheduled() != null && !chatBean.getScheduled().equalsIgnoreCase("") && !chatBean.getScheduled().equalsIgnoreCase("null")) {
                if(Appreference.context_table.containsKey("mainactivity")){
                    if (chatBean.getScheduled() != null && !chatBean.getScheduled().equalsIgnoreCase("") && !chatBean.getScheduled().equalsIgnoreCase("null") && !time_check.equalsIgnoreCase("scheduled")) {
                        chatList.add(chatBean);
                    }
                    Log.i("chat", "CA opened chatActivity " + chatBean.getScheduled());
                    MainActivity mainActivity=(MainActivity) Appreference.context_table.get("mainactivity");
                    mainActivity.startSchedulechatManager(chatBean);
                }
            } else {
                    chatList.add(chatBean);
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    chatAdapter.notifyDataSetChanged();
                }
            });
        } else {
            Log.i("chat", "CA chatscreen chat name not same ");
            chatBean.setUsername(MainActivity.username);
//            chatBean.setDatetime(chatBean.getFromUser() + " " + currentDateandTime);
            if (chatBean.getScheduled() != null && !chatBean.getScheduled().equalsIgnoreCase("") && !chatBean.getScheduled().equalsIgnoreCase("null") && chatBean.getScheduled().split(",")[0].equalsIgnoreCase("scheduled")) {
                chatBean.setOpened("2");
            } else {
                chatBean.setOpened("0");
            }
            Log.i("chat", "CA opened chatActivity else " + chatBean.getOpened());
            VideoCallDataBase.getDB(context).insertChat_history(chatBean);
            if (chatBean.getScheduled() != null && !chatBean.getScheduled().equalsIgnoreCase("") && !chatBean.getScheduled().equalsIgnoreCase("null")) {
                if(Appreference.context_table.containsKey("mainactivity")){
                    Log.i("chat", "CA opened chatActivity " + chatBean.getScheduled());
                    MainActivity mainActivity=(MainActivity) Appreference.context_table.get("mainactivity");
                    mainActivity.startSchedulechatManager(chatBean);
                }
            }
        }

    }


    public String composeChatXML(ChatBean cmbean) {
        StringBuffer buffer = new StringBuffer();
        try {
            buffer.append("<?xml version=\"1.0\"?>"
                    + "<Buddychatinfo><Buddychat");
            buffer.append(" type=" + quotes + cmbean.getType() + quotes);
            buffer.append(" chatname=" + quotes + cmbean.getChatname() + quotes);
            buffer.append(" chatid=" + quotes + cmbean.getChatid() + quotes);
            buffer.append(" fromname=" + quotes + cmbean.getFromUser() + quotes);
            buffer.append(" toname=" + quotes + cmbean.getToname() + quotes);
            buffer.append(" datetime=" + quotes + cmbean.getDatetime() + quotes);
            buffer.append(" signalid=" + quotes + cmbean.getSignalid() + quotes);
            buffer.append(" msgtype=" + quotes + cmbean.getMsgtype() + quotes);
            buffer.append(" scheduled=" + quotes + cmbean.getScheduled() + quotes);
//            if (cmbean.getType().equals("CallSingleChat") || cmbean.getType().equals("CallGroupChat")) {
//                buffer.append(" activeURI=" + quotes + cmbean.getConferenceuri() + quotes);
//            } else {
//                buffer.append(" activeURI=" + quotes + "" + quotes);
//            }
            buffer.append(" chatmembers=" + quotes + cmbean.getChatmembers() + quotes);
            buffer.append(">");
            buffer.append("<message>" + "<![CDATA[" + cmbean.getMessage()
                    + "]]>");
            buffer.append("</message>");
            buffer.append("</Buddychat>");
//            buffer.append("<SendingStatus signalid=" + quotes
//                    + cmbean.getSendingStatusid() + quotes + " />");
//            buffer.append("<session callid=" + quotes
//                    + cmbean.getSessionCallid() + quotes + " />");
            buffer.append("</Buddychatinfo>");
            Log.d("xml", "composed xml for chat======>" + buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            return buffer.toString();
        }

    }

    private void loadstoreddatas() {
        Log.i("chat", "CA loadstoreddatas() ");
        try {
            chatList = new ArrayList<>();

            TreeSet<String> names = new TreeSet<>();
            for (String name : userlist) {
                names.add(name);
            }
            names.add(MainActivity.username);

            for (String nm : names) {
                if (chatuser == null) {
                    chatuser = nm;
                } else {
                    chatuser = chatuser + "," + nm;
                }
            }

//            if (VideoCallDataBase.getDB(context).getChatHistoty(chatid,
//                    MainActivity.username,chatType) != null) {
            if (VideoCallDataBase.getDB(context).getChatHistoty(chatuser, MainActivity.username, chatType) != null) {
                Log.d("chat", "CHAT HISTORY NOT NULL");
//                if (chatType.equalsIgnoreCase("SecureChat")) {
                chatList = VideoCallDataBase.getDB(context).getChatHistoty(chatuser, MainActivity.username, chatType);
//                }
                if (chatList.size() > 0 && chatList != null) {
                    Log.i("chat", "chatlist size>0");
                    for (ChatBean bean : chatList) {
                        cBean = bean;
                    }
                }
            }
            if (cBean != null) {
                Log.d("chat", "loadstoreddatas cBean!=null");
                userlist.clear();
                if (chatType == null) {
                    chatType = cBean.getType();
                }

                // Add User name

                HashMap<String, String> userNameHashMap = new HashMap<String, String>();

                String str = cBean.getChatmembers();
                String[] tokens = str.split(",");
                for (String s : tokens) {
                    userNameHashMap.put(s, s);
                }

                String members;
                Iterator iterator = userNameHashMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry mapEntry = (Map.Entry) iterator.next();
                    members = (String) mapEntry.getValue();
//                    String loginuser="sip:"+MainActivity.username+"@"+getResources().getString(R.string.server_ip);
                    String loginuser = MainActivity.username;
                    if (!members.equalsIgnoreCase(loginuser)) {
                        userlist.add(members);
                    }
                }

                for (String s : userlist) {
                    Log.i("chat", "loadstoreddatas-arraylist member :" + s);
                }

            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    chatAdapter = new ChatAdapter(context, chatList);
                    chat_listview.setAdapter(chatAdapter);
                    chat_listview.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                }
            });
            sortTaskMessage();

//            chatAdapter = new ChatAdapter(context, chatList);
//            chat_listview
//                    .setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
//            chat_listview.setAdapter(chatAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String composeChatXMLForAddParticipant(ChatBean bean) {
        StringBuffer buffer = new StringBuffer();
        try {
            buffer.append("<?xml version=\"1.0\"?>"
                    + "<Buddychatmembersupdate><Buddychatupdate");
            buffer.append(" chatid=" + quotes + bean.getChatid() + quotes);
            buffer.append(" chatmembers=" + quotes + bean.getChatmembers()
                    + quotes);
//            buffer.append(" chatcoordinator=" + quotes
//                    + bean.getCoordinator() + quotes);
            buffer.append(" chatname=" + quotes + bean.getChatname() + quotes);
            buffer.append("/>");
//            buffer.append("<session callid=" + quotes
//                    + cmbean.getSessionCallid() + quotes + " />");
            buffer.append("</Buddychatmembersupdate>");
            Log.d("xml", "composed xml for chat extra participant======>"
                    + buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return buffer.toString();
        }

    }

    public void notifyAddedChat() {
        // TODO Auto-generated method stub
        userlist.clear();
        loadstoreddatas();
    }


    private void multimediaImage(final String type) {
        try {
            Log.i("clone", "===> inside message response");
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_myacc_menu);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.horizontalMargin = 15;
            Window window = dialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM);
            dialog.show();
            TextView gallery = (TextView) dialog.findViewById(R.id.delete_acc);
            gallery.setText("Gallery");
            TextView camera = (TextView) dialog.findViewById(R.id.log_out);
            if (type.equalsIgnoreCase("image")) {
                camera.setText("Camera");
            } else {
                camera.setText("Video");
            }
            TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            gallery.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    try {
                        dialog.dismiss();

                        if (type.equals("image")) {
                            if (Build.VERSION.SDK_INT < 19) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent, 30);
                            } else {
                                Log.i("img", "sdk is above 19");
                                Log.i("clone", "====> inside gallery");
                                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("image/*");
                                startActivityForResult(intent, 31);
                            }
                        } else {
                            if (Build.VERSION.SDK_INT < 19) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("video/*");
                                startActivityForResult(intent, 32);
                            } else {
                                Log.i("img", "sdk is above 19");
                                Log.i("clone", "====> inside gallery");
                                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("video/*");
                                startActivityForResult(intent, 33);
                            }
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            camera.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    try {
                        dialog.dismiss();
                        if (type.equals("image")) {
                            final String path = Environment.getExternalStorageDirectory()
                                    + "/High Message/";
                            File directory = new File(path);
                            if (!directory.exists())
                                directory.mkdir();
                            strIPath = path + getFileName() + ".jpg";
                            Intent intent = new Intent(context, CustomVideoCamera.class);
//                            Log.d("HighMessage--->","Compress Image  "+compressImage(strIPath));
//                            Uri imageUri = Uri.fromFile(new File(compressImage(strIPath)));
                            Uri imageUri = Uri.fromFile(new File(strIPath));
                            intent.putExtra("filePath", strIPath);
                            intent.putExtra("isPhoto", true);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, 132);
                        } else {
                            final String path = Environment.getExternalStorageDirectory()
                                    + "/High Message/";
                            File directory = new File(path);
                            if (!directory.exists())
                                directory.mkdir();
                            strIPath = path + getFileName() + ".mp4";
                            Intent intent = new Intent(context, CustomVideoCamera.class);
                            intent.putExtra("filePath", strIPath);
                            intent.putExtra("isPhoto", false);
//                            Uri fileUri = Uri.fromFile(new File(strIPath));
//                            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                            startActivityForResult(intent, 111);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
        }
        return strFilename;
    }


    public String getRealPathFromURI(Uri contentUri) {
        try {
            Log.i("profile", "===> inside getRealPathFromURI");
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri,
                    proj, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            Log.e("profile", "====> " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    private void showToast(String msg) {
        Toast.makeText(ChatActivity.this, msg, Toast.LENGTH_LONG)
                .show();
    }

    private void showToastWithHandler(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ChatActivity.this, msg, Toast.LENGTH_LONG)
                        .show();
            }
        });
    }


    public Bitmap convertpathToBitmap(String strIPath) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile((strIPath), bmOptions);

        return bitmap;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // TODO Auto-generated method stub
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
                ArrayList<String> newBuddyAdd = data.getStringArrayListExtra("RESULT");
                ArrayList<String> alreadyParticipant = (ArrayList<String>) userlist.clone();
                for (String newuser : newBuddyAdd) {
                    userlist.add(newuser);
                }
                String chatID;
                String chatName;

                if (cBean != null) {
                    chatID = cBean.getChatid();
                    chatName = cBean.getChatname();
                } else {
                    chatID = chatid;
                    chatName = chatname;
                }

                String toUsers = "";

                for (String toUser : userlist) {
                    toUsers += toUser + ",";
                }
                if (toUsers.equals("") == false) {
                    toUsers = toUsers.substring(0, toUsers.length() - 1);
                    ChatBean bean = new ChatBean();
                    bean.setChatname(chatName);
                    bean.setChatid(chatID);

                    TreeSet<String> names = new TreeSet<>();
                    if (toUsers != null && toUsers.contains(",")) {
                        String nm[] = toUsers.split(",");
                        for (String user : nm) {
                            names.add(user);
                        }
                        names.add(MainActivity.username);
                    } else {
                        names.add(toUsers);
                        names.add(MainActivity.username);
                    }
                    String unames = null;
                    for (String ur : names) {
                        if (unames == null) {
                            unames = ur;
                        } else {
                            unames = unames + "," + ur;
                        }
                    }
                    bean.setChatmembers(unames);


//                    bean.setChatmembers(toUsers + ",sip:" + MainActivity.username + "@"+getResources().getString(R.string.server_ip));
                    String xml = composeChatXMLForAddParticipant(bean);
//            for (String toUser : alreadyParticipant) {
                    for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
                        String name = MainActivity.account.buddyList.get(i).cfg.getUri();
                        Log.i("chat", "buddyname-->" + name);
                        for (String toUser : alreadyParticipant) {
                            Log.i("chat", "already user-->" + toUser);
                            if (toUser.equalsIgnoreCase(name)) {
                                Log.i("chat", "both users are same");
                                MyBuddy myBuddy = MainActivity.account.buddyList.get(i);
                                SendInstantMessageParam prm = new SendInstantMessageParam();
                                prm.setContent(xml);

                                boolean valid = myBuddy.isValid();
                                Log.i("chat", "valid ======= " + valid);
                                try {
                                    myBuddy.sendInstantMessage(prm);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
//            }
            }

            if (resultCode == RESULT_OK) {
                if (requestCode == 30) {
                    if (data != null) {
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        File selected_file = new File(strIPath);
                        int length = (int) selected_file.length() / 1048576;
                        if (length <= 2) {
                            Bitmap img = null;
                            img = convertpathToBitmap(strIPath);

                            if (img != null) {
                                img = Bitmap.createScaledBitmap(img, 100, 75, false);

                                final String path = Environment.getExternalStorageDirectory()
                                        + "/High Message/" + getFileName() + ".jpg";

                                BufferedOutputStream stream;
                                Bitmap bitmap = null;
                                try {
                                    bitmap = convertpathToBitmap(strIPath);
                                    if (bitmap != null) {
                                        stream = new BufferedOutputStream(
                                                new FileOutputStream(new File(path)));
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                        strIPath = path;
                                    }

                                    if (bitmap != null) {
                                        bitmap = Bitmap.createScaledBitmap(
                                                bitmap, 200, 150, false);
                                    }
                                    fileWebService("image");
                                /*    MediaListBean uIbean = new MediaListBean();
                                    uIbean.setMediaType("image");
                                    uIbean.setMediaPath(strIPath);
                                    mediaList.add(uIbean);
                                    medialistadapter.notifyDataSetChanged();*/
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else if (requestCode == 31) {
                    if (data != null) {
                        Uri selectedImageUri = data.getData();
                        final int takeFlags = data.getFlags()
                                & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        getContentResolver().takePersistableUriPermission(
                                selectedImageUri, takeFlags);
                        strIPath = Environment.getExternalStorageDirectory()
                                + "/High Message/"
                                + getFileName() + ".jpg";
                        File selected_file = new File(strIPath);
                        int length = (int) selected_file.length() / 1048576;
                        Log.d("busy", "........ size is------------->" + length);
                        if (length <= 2) {
                            new bitmaploader().execute(selectedImageUri);
//                            fileWebService("image");
                        } else {
                            showToast("Kindly Select someother image,this image is too large");
                        }
                    }
                } else if (requestCode == 32) {
                    if (data != null) {
                        Log.i("AAA", "New activity 32*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + getFileName() + ".mp4";

                        strIPath = path;
                        Log.i("AAA", "New activity " + strIPath);
                        fileWebService("video");
                       /* MediaListBean uIbean = new MediaListBean();
                        uIbean.setMediaType("video");
                        uIbean.setMediaPath(strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();*/
                    }
                } else if (requestCode == 33) {
                    if (data != null) {
                        Log.i("AAA", "New activity 33*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + getFileName() + ".mp4";

                        Log.i("AAA", "New activity " + strIPath);
                        strIPath = path;
                        Log.i("AAA", "New activity " + strIPath);
                       /* MediaListBean uIbean = new MediaListBean();
                        uIbean.setMediaType("video");
                        uIbean.setMediaPath(strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();*/
                        FileInputStream fin = (FileInputStream) getContentResolver()
                                .openInputStream(selectedImageUri);
                        ByteArrayOutputStream straam = new ByteArrayOutputStream();
                        byte[] content = new byte[1024];
                        int bytesread;
                        while ((bytesread = fin.read(content)) != -1) {
                            straam.write(content, 0, bytesread);
                        }
                        byte[] bytes = straam.toByteArray();
                        FileOutputStream fout = new FileOutputStream(strIPath);
                        straam.flush();
                        straam.close();
                        straam = null;
                        fin.close();
                        fin = null;
                        fout.write(bytes);
                        fout.flush();
                        fout.close();
                        fout = null;
                        fileWebService("video");
                    }
                } else if (requestCode == 132) {
                    File new_file = new File(strIPath);
                    if (new_file.exists()) {
                        fileWebService("image");
                       /* MediaListBean uIbean = new MediaListBean();
                        uIbean.setMediaType("image");
                        uIbean.setMediaPath(strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();*/
                        Log.i("AAAA", "onactivity result $$$$$$$$$$$$$$$" + strIPath);
                    }
//                    showprogress();
//                    Log.i("AAAA","onactivity result ");
//                    new imageOrientation().execute("image");
                } else if (requestCode == 111) {
                    Log.i("Avideo", "New activity 33************* : " + strIPath);
                    fileWebService("video");
                   /* MediaListBean uIbean = new MediaListBean();
                    uIbean.setMediaType("video");
                    uIbean.setMediaPath(strIPath);
                    mediaList.add(uIbean);
                    medialistadapter.notifyDataSetChanged();*/
//                    showprogress();
//                    Log.i("AAAA","onactivity result ");
//                    new imageOrientation().execute("image");
                } else if (requestCode == 99) {
                    if (data != null) {
                        Log.i("AAA", "New activity 33*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + getFileName() + ".mp3";

                        Log.i("AAA", "New activity " + strIPath);
                        strIPath = path;
                        Log.i("AAA", "New activity " + strIPath);

                      /*  MediaListBean uIbean = new MediaListBean();
                        uIbean.setMediaType("audio");
                        uIbean.setMediaPath(strIPath);
                        Log.i("mp","mpath"+strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();*/

                        FileInputStream fin = (FileInputStream) getContentResolver()
                                .openInputStream(selectedImageUri);
                        ByteArrayOutputStream straam = new ByteArrayOutputStream();
                        byte[] content = new byte[1024];
                        int bytesread;
                        while ((bytesread = fin.read(content)) != -1) {
                            straam.write(content, 0, bytesread);
                        }
                        byte[] bytes = straam.toByteArray();
                        FileOutputStream fout = new FileOutputStream(strIPath);
                        straam.flush();
                        straam.close();
                        straam = null;
                        fin.close();
                        fin = null;
                        fout.write(bytes);
                        fout.flush();
                        fout.close();
                        fout = null;
                    }

                } else if (requestCode == 222) {
                    if (data != null) {
                        Log.i("AAA", "New activity 32*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + getFileName() + ".mp3";

                        strIPath = path;
                        Log.i("AAA", "New activity " + strIPath);
                      /*  MediaListBean uIbean = new MediaListBean();
                        uIbean.setMediaType("audio");
                        uIbean.setMediaPath(strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();*/
                    }
                } else if (requestCode == 223) {
                    if (data != null) {
                        Log.i("AAA", "New activity 33*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + getFileName() + ".mp3";

                        Log.i("AAA", "New activity " + strIPath);
                        strIPath = path;
                        Log.i("AAA", "New activity " + strIPath);
                      /*  MediaListBean uIbean = new MediaListBean();
                        uIbean.setMediaType("audio");
                        uIbean.setMediaPath(strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();*/
                        FileInputStream fin = (FileInputStream) getContentResolver()
                                .openInputStream(selectedImageUri);
                        ByteArrayOutputStream straam = new ByteArrayOutputStream();
                        byte[] content = new byte[1024];
                        int bytesread;
                        while ((bytesread = fin.read(content)) != -1) {
                            straam.write(content, 0, bytesread);
                        }
                        byte[] bytes = straam.toByteArray();
                        FileOutputStream fout = new FileOutputStream(strIPath);
                        straam.flush();
                        straam.close();
                        straam = null;
                        fin.close();
                        fin = null;
                        fout.write(bytes);
                        fout.flush();
                        fout.close();
                        fout = null;
                    }
                } else if (requestCode == 333) {
                    Log.d("filePath", data.getStringExtra("taskFileExt"));
                    strIPath = data.getStringExtra("taskFileExt");
                    fileWebService("audio");
                   /* MediaListBean uIbean = new MediaListBean();
                    uIbean.setMediaType("audio");
                    uIbean.setMediaPath(path);
                    mediaList.add(uIbean);
                    medialistadapter.notifyDataSetChanged();*/
                } else if (requestCode == 423) {
                    strIPath = data.getStringExtra("path");
                    Log.i("Task", "path" + strIPath);
                    File new_file = new File(strIPath);
                    if (new_file.exists()) {
                       /* MediaListBean uIbean = new MediaListBean();
                        uIbean.setMediaType("image");
                        uIbean.setMediaPath(strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();*/
                        Log.i("AAAA", "onactivity result $$$$$$$$$$$$$$$" + strIPath);

                    }
                    fileWebService("sketch");
                } else if (requestCode == 55) {

                    file_extension = data.getStringExtra("fileExt");
                    String fileName = data.getStringExtra("fileName");
                    strIPath = data.getStringExtra("filePath");
//                    filePath = fileName.split(".")[1];
//                    fileExt=fileExt1.substring(fileExt1.lastIndexOf(1));
                    Log.i("fExt", "fileExt" + strIPath);
                    Log.i("filePath", "fm--->" + file_extension);
                    Log.i("filename", "fm--->" + fileName);
                    Log.i("fileExt", "fm--->" + strIPath);

                    fileWebService("file");
//                    strIPath = filePath;
//                    MediaListBean uIbean = new MediaListBean();
//                    uIbean.setMediaType("doc");
//                    uIbean.setMediaPath(filePath);
//                    Log.i("mp", "mpath" + strIPath);
//                    mediaList.add(uIbean);
//                    medialistadapter.notifyDataSetChanged();
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void fileWebService(String multimedia_type) {
        if (!strIPath.equals(null)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "dd/MM/yyyy hh:mm:ss a");
            String dateforrow = dateFormat.format(new Date());

//             String file_name = strIPath.split("/HighMessage/")[1];
            String[] file_names = strIPath.split("/");
            String file_name = file_names[file_names.length - 1];
//            for(String temp_file_name : file_names) {
//                file_name = temp_file_name;
//            }
            String toUsers = "";

            for (String toUser : userlist) {
                toUsers += toUser + ",";
            }
            toUsers = toUsers.substring(0, toUsers.length() - 1);
            Log.d("chat", "touser-->" + toUsers);

            ChatBean webchat_bean = new ChatBean();
            webchat_bean.setFromUser(MainActivity.username);
            webchat_bean.setToname(toUsers);
            webchat_bean.setMsgtype(multimedia_type);
            webchat_bean.setDatetime(dateforrow);
            webchat_bean.setPath(strIPath);
            webchat_bean.setFile_name(file_name);
            webchat_bean.setUsername(MainActivity.username);
            webchat_bean.setMessage(file_name);
            webchat_bean.setType(chatType);
            webchat_bean.setChatname(chatname);
            webchat_bean.setChatid(chatid);
            webchat_bean.setSignalid(Utility.getSessionID());

            TreeSet<String> names = new TreeSet<>();
            if (toUsers != null && toUsers.contains(",")) {
                String nm[] = toUsers.split(",");
                for (String user : nm) {
                    names.add(user);
                }
                names.add(MainActivity.username);
            } else {
                names.add(toUsers);
                names.add(MainActivity.username);
            }
            String unames = null;
            for (String ur : names) {
                if (unames == null) {
                    unames = ur;
                } else {
                    unames = unames + "," + ur;
                }
            }


//            webchat_bean.setChatmembers(toUsers + "," + MainActivity.username);
            webchat_bean.setChatmembers(unames);

            chatList.add(webchat_bean);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    chatAdapter.notifyDataSetChanged();
                }
            });

            VideoCallDataBase.getDB(context).insertChat_history(webchat_bean);

            JSONObject jsonObjects = new JSONObject();
            try {
//                Log.i("fileupload", "Base64--->" + encodeTobase64(BitmapFactory.decodeFile(strIPath)));
                if (multimedia_type.equalsIgnoreCase("image") || multimedia_type.equalsIgnoreCase("sketch")) {
                    jsonObjects.put("fileContent", encodeTobase64(BitmapFactory.decodeFile(strIPath)));
                    jsonObjects.put("fileExtention", "jpg");
                } else if (multimedia_type.equalsIgnoreCase("video")) {
                    jsonObjects.put("fileContent", encodeAudioVideoToBase64(strIPath));
                    jsonObjects.put("fileExtention", "mp4");
                } else if (multimedia_type.equalsIgnoreCase("audio")) {
                    jsonObjects.put("fileContent", encodeAudioVideoToBase64(strIPath));
                    jsonObjects.put("fileExtention", "mp3");
                } else if (multimedia_type.equalsIgnoreCase("file")) {
                    jsonObjects.put("fileContent", encodeFileToBase64Binary(strIPath));
                    jsonObjects.put("fileExtention", file_extension);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Appreference.jsonRequestSender.fileUploadfromChat(EnumJsonWebservicename.fileUpload, jsonObjects, this, webchat_bean);
        }

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
//        Log.i("FileUpload", "audioVideoEncode========" + strFile);
        return strFile;
    }

    private String encodeFileToBase64Binary(String fileName)
            throws IOException {

        File file = new File(fileName);
        byte[] bytes = loadFile(file);
        byte[] encoded = org.apache.commons.codec.binary.Base64.encodeBase64(bytes);
        String encodedString = new String(encoded);

        return encodedString;
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }


    @Override
    public void ResponceMethod(Object object) {
        Log.i("Chat", "came to ResponceMethod in ChatActivity");
        try {
            if (object != null) {
                CommunicationBean response = (CommunicationBean) object;
                String response_string = response.getEmail();
                JsonElement jelement = new JsonParser().parse(response_string);
                if (jelement.getAsJsonObject() != null) {
                    ChatBean responsebean = (ChatBean) response.getMessage_Details_Object();
                    final ChatBean response_bean;
                    response_bean = (ChatBean) responsebean.clone();
                    Log.i("task", "jelement.getAsJsonObject() != null");
                    JsonObject jobject = jelement.getAsJsonObject();
                    JSONObject jsonObject = new JSONObject(response_string);
                    Log.i("Chat", "json");
                    String result_code = jsonObject.getString("result_code");
                    if (jobject.has("result_code") && result_code.equals("0")) {
                        String result = jobject.get("result_text").toString();
                        Log.d("task", "fileName" + jobject.get("fileName").toString());
                        String fileName = jobject.get("fileName").toString().replaceAll("\"", "");
                        response_bean.setMessage(fileName);
                        response_bean.setMultimediaURL(getResources().getString(R.string.file_upload) + fileName);
                        Log.i("Responce", "demo" + result);

                        showToastWithHandler(result);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String xml = composeChatXML(response_bean);
                                sendInstantMessage(xml);
                            }
                        });

                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ErrorMethod(Object object) {

    }


    public class bitmaploader extends AsyncTask<Uri, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            try {
                super.onPostExecute(result);
                Log.d("image", "came to post execute for image");

                Bitmap img = null;
                if (strIPath != null)
//                    img = callDisp.ResizeImage(strIPath);
                    fileWebService("image");

            } catch (Exception e) {
                Log.e("profile", "====> " + e.getMessage());
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            try {
                super.onPreExecute();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Uri... params) {
            // TODO Auto-generated method stub
            try {
                for (Uri uri : params) {
                    Log.d("image", "came to doin backgroung for image");
                    FileInputStream fin = (FileInputStream) getContentResolver()
                            .openInputStream(uri);
                    ByteArrayOutputStream straam = new ByteArrayOutputStream();
                    byte[] content = new byte[1024];
                    int bytesread;
                    while ((bytesread = fin.read(content)) != -1) {
                        straam.write(content, 0, bytesread);
                    }
                    byte[] bytes = straam.toByteArray();
                    FileOutputStream fout = new FileOutputStream(strIPath);
                    straam.flush();
                    straam.close();
                    straam = null;
                    fin.close();
                    fin = null;
                    fout.write(bytes);
                    fout.flush();
                    fout.close();
                    fout = null;
                }
            } catch (Exception e) {
//                if (AppReference.isWriteInFile)
//                    AppReference.logger.error(e.getMessage(), e);
//                else
                e.printStackTrace();
            }

            return null;
        }

    }

    public void updateChatStatus(ChatBean bean) {
        if (bean != null) {
            for (ChatBean chatBean : chatList) {
                if (chatBean != null && chatBean.getSignalid() != null && bean.getSignalid() != null) {
                    if (chatBean.getSignalid().equalsIgnoreCase(bean.getSignalid())) {
                        chatBean.setMsg_status(1);
                        break;
                    }
                }

            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    chatAdapter.notifyDataSetChanged();
                }
            });
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

    @Override
    protected void onResume() {
        super.onResume();
        showNetworkStateUI();
    }

    public void showNetWorkConnectedState() {
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

    }

    private String[] getDatesFromCalender() {
        Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+1"));
        Calendar c2 = Calendar.getInstance(TimeZone.getTimeZone("GMT+1"));

        List<String> dates = new ArrayList<String>();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
            dates.add("Today");
            today = dateFormat.format(c1.getTime());
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c.getTime());
            String split[] = formattedDate.split("-");
            String todaydate = split[0] + " " + split[1];
            for (int i = 0; i < 360; i++) {
                c1.add(Calendar.DATE, 1);
                dates.add(dateFormat.format(c1.getTime()));

            }
            c2.add(Calendar.DATE, -60);

            for (int i = 0; i < 59; i++) {
                c2.add(Calendar.DATE, 1);
                dates.add(dateFormat.format(c2.getTime()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dates.toArray(new String[dates.size() - 1]);
    }

    private boolean CheckReminderIsValid(String am_pm, int day, String strDate, String date, boolean start) {
        boolean isvalid = false;
        try {
            Calendar rightNow = Calendar.getInstance(Locale.getDefault());
            int amPM = rightNow.get(Calendar.AM_PM);

            int position = day;
            int startTime = Integer.parseInt(strDate.split(":")[0]);
            int currentTime = Integer.parseInt(date.split(":")[0]);

            int startMin = Integer.parseInt(strDate.split(":")[1]);
            int currentMin = Integer.parseInt(date.split(":")[1]);
            Log.i("SCHEDULECALL", "Start Date===>" + startTime);
            Log.i("SCHEDULECALL", "selected Date===>" + currentTime);

            Log.i("SCHEDULECALL", "startMin ===>" + startMin);
            Log.i("SCHEDULECALL", "currentMin ===>" + currentMin);
            Log.i("SCHEDULECALL", "am_pm===>" + am_pm);
            Log.i("SCHEDULECALL", "amPM===>" + amPM);
            if (start) {

                if (position > 0 && position < 364) {
                    isvalid = true;
                } else if (position == 0 && startTime > currentTime) {
                    isvalid = true;
                } else if (position == 0 && startTime >= currentTime
                        && startMin > currentMin) {
                    isvalid = true;
                } else if (position == 0 && amPM == 0
                        && am_pm.equalsIgnoreCase("pm")) {
                } else if (ampm.equalsIgnoreCase("pm") && am_pm.equalsIgnoreCase("pm")) {
                    if (startTime >= currentTime
                            && startMin > currentMin) {
                        isvalid = true;
                    } else {
                        isvalid = false;
                    }

                } else {

                    isvalid = false;
                }

            } else {
                if (currentTime >= startTime || currentMin > startMin) {
                    isvalid = true;
                } else {

                    isvalid = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isvalid;
    }


    public void showCustomDatePicker(final String start_date1) {
        try {
            final NumberPicker datePicker, hourPicker, minutePicker, am_pmPicker;
            final String[] dates;
            String[] mAmPmStrings = null;
            final int yyyy;


            final Dialog dialog = new Dialog(context, R.style.AppTheme);
            dialog.setContentView(R.layout.dialogfragment_time_date_picker);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dates = getDatesFromCalender();

            datePicker = (NumberPicker) dialog
                    .findViewById(R.id.datePicker);
            hourPicker = (NumberPicker) dialog
                    .findViewById(R.id.hourPicker);
            minutePicker = (NumberPicker) dialog
                    .findViewById(R.id.minutePicker);
            am_pmPicker = (NumberPicker) dialog
                    .findViewById(R.id.am_pm);
            datePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            hourPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            minutePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            am_pmPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            Button set = (Button) dialog
                    .findViewById(R.id.setbtn);
//            set.setTypeface(normal_type);
//			set.setOnClickListener(this);
            Log.i("datePicker", "is " + datePicker);
            datePicker.setClickable(false);
            datePicker.setMinValue(0);
            datePicker.setMaxValue(dates.length - 1);
            datePicker.setFormatter(new NumberPicker.Formatter() {

                @Override
                public String format(int value) {
                    return dates[value];
                }
            });
            mAmPmStrings = new DateFormatSymbols().getAmPmStrings();

            datePicker.setDisplayedValues(dates);
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(59);
            minutePicker.setWrapSelectorWheel(true);
            // ArrayList<String> displayedValues = new ArrayList<String>();
            String[] sdisplayedValues = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"};
            List<String> ampmValues = new ArrayList<String>();
            String[] am_pm = {"AM", "PM", "AM", "PM"};
            // for (int i = 0; i < 60; i += 15) {
            // displayedValues.add(String.format("%02d", i));
            // }
            minutePicker.setDisplayedValues(sdisplayedValues);
//             ampmValues.add("AM");
            // ampmValues.add("PM");
            am_pmPicker.setMinValue(0);
            am_pmPicker.setMaxValue(3);
            am_pmPicker.setWrapSelectorWheel(true);
            int am = 0;
            am_pmPicker.setDisplayedValues(am_pm);

            final Calendar rightNow = Calendar.getInstance(Locale.getDefault());
            int dd = rightNow.get(Calendar.HOUR_OF_DAY);
            Log.i("current", "hour" + dd);
            yyyy = rightNow.get(Calendar.YEAR);
//            if (!DateFormat.is24HourFormat(this)) {
            hourPicker.setMinValue(0);
            hourPicker.setMaxValue(11);
            String[] displayedValues_hour = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
            hourPicker.setDisplayedValues(displayedValues_hour);
//            } else {
//                hourPicker.setMinValue(00);
//                hourPicker.setMaxValue(23);
//                am_pmPicker.setVisibility(View.GONE);
//            }


//                String dateInputString=String.valueOf(start_date1);
            String[] values1 = datePicker.getDisplayedValues();
//                String str = values1[datePicker.getValue()];
            Log.i("date", "st_date" + start_date1);
            st_hour = start_date1;
            st_hour = st_hour.split(" ")[1];
            String st_date = start_date1;
            st_date = st_date.split(" ")[0];

            Log.i("start", "st_date" + st_date);
            Log.i("start", "st_hour" + st_hour);
            try {
                // obtain date and time from initial string
                DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat targetFormat = new SimpleDateFormat("MMM dd");
                Date date = originalFormat.parse(st_date);
                Log.i("start", "original " + date);
                String formattedDate = targetFormat.format(date);
                Log.i("start", "target " + formattedDate);
                for (int i = 0; i < values1.length; i++) {
                    Log.i("start", "for" + values1[i]);
                    if (values1[i].equalsIgnoreCase(formattedDate)) {
                        Log.i("start", "if" + values1[i]);
                        datePicker.setValue(i);
                    }
//                        else
//                        {
//                            Log.i("start","else");
////                            datePicker.setValue(0);
//                        }
                }
            }
            // 20120821
            catch (Exception e) {
                e.printStackTrace();
            }
            int hour = Integer.parseInt(st_hour.split(":")[0]);
            minutePicker.setValue(Integer.parseInt(st_hour.split(":")[1]));
            Log.i("start", "hour " + hour);
            if (hour >= 13) {
                Log.i("start", "hour>=13 " + hour);
                if (hour == 13) {
                    strTime = "1";
                } else if (hour == 14) {
                    strTime = "2";
                } else if (hour == 15) {
                    strTime = "3";
                } else if (hour == 16) {
                    strTime = "4";
                } else if (hour == 17) {
                    strTime = "5";
                } else if (hour == 18) {
                    strTime = "6";
                } else if (hour == 19) {
                    strTime = "7";
                } else if (hour == 20) {
                    strTime = "8";
                } else if (hour == 21) {
                    strTime = "9";
                } else if (hour == 22) {
                    strTime = "10";
                } else if (hour == 23) {
                    strTime = "11";
                }
                hourPicker.setValue(Integer.parseInt(strTime) - 1);
                am_pmPicker.setValue(1);
            } else {
                hourPicker.setValue(hour - 1);
                am_pmPicker.setValue(0);
            }
            set.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String startDATE, endDate = null, today, toas;
                    Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+1"));
                    String[] values1 = datePicker.getDisplayedValues();
                    String[] values2 = hourPicker.getDisplayedValues();
                    String[] values3 = minutePicker.getDisplayedValues();
                    String[] values4 = am_pmPicker.getDisplayedValues();
                    String tdystr = values1[datePicker.getValue()];
                    int dateposition = datePicker.getValue();
                    int hourposition = hourPicker.getValue();
                    if (values1[datePicker.getValue()]
                            .equalsIgnoreCase("today")) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
//                        dates.add("Today");
                        today = dateFormat.format(c1.getTime());
                        tdystr = today;
                        Log.i("tdystr", "day " + tdystr);
                    } else {
                        tdystr = values1[datePicker.getValue()];
                        Log.i("tdystr", "day " + tdystr);
                    }
//                    if (!DateFormat.is24HourFormat(context)) {
                    Log.i("tdystr", "hour " + values2[hourPicker.getValue()]);
                    Log.i("tdystr", "min " + values3[minutePicker.getValue()]);
                    Log.i("tdystr", "am_pm " + values4[am_pmPicker.getValue()]);

                    toas = tdystr + " " + values2[hourPicker.getValue()] + " : "
                            + values3[minutePicker.getValue()] + " "
                            + values4[am_pmPicker.getValue()];
//                    }else{
//                        toas = tdystr + " " + hourPicker.getValue() + " : "
//                                + values3[minutePicker.getValue()];
//                    }

                    // int month = dp.getMonth() + 1;
                    String day = values3[minutePicker.getValue()];// String.valueOf(tp.getCurrentMinute());
                    // if (day.length() == 1) {
                    // day = "0" + day;
                    // }
                    String strDateTime = values2[hourPicker.getValue()] + ":"
                            + values3[minutePicker.getValue()];// tp.getCurrentHour()
                    // + ":" + day;
                    String tm = null;

                    SimpleDateFormat time = new SimpleDateFormat("HH:mm");
                    tm = time.format(new Date());

                    Log.i("SCHEDULECALL", "Now Time Is====>" + tm);

                    int startTime = Integer.parseInt(strDateTime
                            .split(":")[0]);
                    String startMin = strDateTime
                            .split(":")[1];

                    Log.i("utctime", "starMin--->" + startMin);
                    if (values4[am_pmPicker.getValue()]
                            .equalsIgnoreCase("pm")) {
                        Log.i("schedulecall", "pm picker");
                        if (startTime == 1) {
                            strTime = "13" + ":"
                                    + String.valueOf(startMin);
                        } else if (startTime == 2) {
                            strTime = "14" + ":"
                                    + String.valueOf(startMin);
                        } else if (startTime == 3) {
                            strTime = "15" + ":"
                                    + String.valueOf(startMin);
                        } else if (startTime == 4) {
                            strTime = "16" + ":"
                                    + String.valueOf(startMin);
                        } else if (startTime == 5) {
                            strTime = "17" + ":"
                                    + String.valueOf(startMin);
                        } else if (startTime == 6) {
                            strTime = "18" + ":"
                                    + String.valueOf(startMin);
                        } else if (startTime == 7) {
                            strTime = "19" + ":"
                                    + String.valueOf(startMin);
                        } else if (startTime == 8) {
                            strTime = "20" + ":"
                                    + String.valueOf(startMin);
                        } else if (startTime == 9) {
                            strTime = "21" + ":"
                                    + String.valueOf(startMin);
                        } else if (startTime == 10) {
                            strTime = "22" + ":"
                                    + String.valueOf(startMin);
                        } else if (startTime == 11) {
                            strTime = "23" + ":"
                                    + String.valueOf(startMin);
                        }
//                            else if (startTime == 12) {
//                                strTime = "00" + ":"
//                                        + String.valueOf(startMin);
//                            }
                        else
                            strTime = strDateTime;

                    } else {
                        if (values4[am_pmPicker.getValue()]
                                .equalsIgnoreCase("am")) {
                            Log.i("schedulecall", "am picker");
                            if (startTime == 12) {
                                Log.i("schedulecall", "am picker 12");
                                strTime = "00" + ":"
                                        + String.valueOf(startMin);
                            } else {
                                Log.i("schedulecall", "am picker !12");
                                strTime = strDateTime;
                            }
                        } else {
                            Log.i("schedulecall", "24 format");
                            strTime = strDateTime;
                        }
                    }
                    //        ampm = values4[am_pmPicker.getValue()];
                    String strfromdate = start_date1;
                    String strfromday = strfromdate.split(" ")[0];

                    DateFormat selctedDateformate = new SimpleDateFormat("yyyy MMM dd hh : mm a");
                    Date date = null;
                    try {
                        date = selctedDateformate.parse(yyyy + " " + toas);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat day1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    strenddate = day1.format(date);

                    end_date1 = strenddate;

//                        String strendmonth = strenddate.split("-")[1];
//                        int strendmonth1 = Integer.parseInt(strendmonth);
                    String strendday = strenddate.split(" ")[0];

                    int strendday1 = Integer.parseInt(strendday.split("-")[2]);
                    if (strfromday.compareTo(strendday) <= 0) {
                        if (CheckReminderIsValid(
                                values4[am_pmPicker.getValue()], dateposition,
                                strTime, tm, true)) {
//                                start_date1.setText(strenddate);

                               /* start_date = dateposition;
                                start_time = hourPicker.getValue();
                                start_minute = minutePicker.getValue();
                                start_am_pm = am_pmPicker.getValue();*/
//                                startTimeSet = true;
//
//                                strDATE = values1[datePicker.getValue()];
//                            } else {
//                                showToast(context.getString(R.string.Kindly_slct_future_time));
//                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Kindly select future date", Toast.LENGTH_LONG).show();
                        }


                        //                datePicker.setValue();
                        int hour = Integer.parseInt(st_hour.split(":")[0]);
                        Log.i("start", "hour " + hour);
                        if (hour >= 13)

                        {
                            Log.i("start", "hour>=13 " + hour);
                            if (hour == 13) {
                                strTime = "1";
                            } else if (hour == 14) {
                                strTime = "2";
                            } else if (hour == 15) {
                                strTime = "3";
                            } else if (hour == 16) {
                                strTime = "4";
                            } else if (hour == 17) {
                                strTime = "5";
                            } else if (hour == 18) {
                                strTime = "6";
                            } else if (hour == 19) {
                                strTime = "7";
                            } else if (hour == 20) {
                                strTime = "8";
                            } else if (hour == 21) {
                                strTime = "9";
                            } else if (hour == 22) {
                                strTime = "10";
                            } else if (hour == 23) {
                                strTime = "11";
                            }
                            hourPicker.setValue(Integer.parseInt(strTime) - 1);
                            am_pmPicker.setValue(1);
                        } else

                        {
                            hourPicker.setValue(hour - 1);
                            am_pmPicker.setValue(0);
                        }

                        Log.i("start", "am_pmPicker" + am_pmPicker.getValue());
//                hourPicker.setValue(hour - 1);
//                hourPicker.setValue(dd);


                    }
                    dialog.dismiss();
                }

            });
            dialog.setTitle(context.getString(R.string.Date_and_Time));
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sortTaskMessage() {

        try {
            Collections.sort(chatList, new Comparator<ChatBean>() {
                public int compare(ChatBean o1, ChatBean o2) {
                    if (o1.getDatetime() == null || o2.getDatetime() == null)
                        return 0;
                    Log.i("sortmessage", "TASK HISTORY List size  = " + chatList.size() + "  ==" + o2.getDatetime().compareTo(o1.getDatetime()));
                    Log.i("sortmessage", "TASK HISTORY List size  = " + chatList.size() + "  ==" + o1.getDatetime() + " ==1 " + " ==2 " + (o2.getDatetime()));
                    return o1.getDatetime().compareTo(o2.getDatetime());
                }
            });
//                Collections.reverse(taskList);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Sorting", "Sorting problem");
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                chatAdapter.notifyDataSetChanged();
            }
        });
    }
}
