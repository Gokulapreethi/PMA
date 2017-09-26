package com.ase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;
import com.ase.RandomNumber.Utility;
import com.ase.expand.Group;

import org.json.JSONArray;
import org.json.JSONObject;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.app.CallActivity;
import org.pjsip.pjsua2.app.GroupMemberAccess;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.app.MyBuddy;
import org.pjsip.pjsua2.app.MyCall;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.WebServiceInterface;
import xml.xmlcomposer;

/**
 * Created by vignesh on 2/27/2017.
 */
public class Contactlistpage extends Activity implements WebServiceInterface {

    TextView assingNew, Profile, listTask, chat, videocall, audiocall, leave, cancel;
    Context classContext;
    Handler handler = new Handler();
    ContactBean contactBean;
    String tasktype, getname, groupname;
    boolean contactTab = true;
    xmlcomposer xmlcomposer = null;
    ProgressDialog dialog;
    public Group group;
    boolean check = false;
    public ContactsFragment contactsfragment = (ContactsFragment) Appreference.context_table.get("contactsfragment");
    public GroupMemberAccess groupMemberAccess;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactlistpage);
        setFinishOnTouchOutside(false);
        xmlcomposer = new xmlcomposer();
        assingNew = (TextView) findViewById(R.id.assingNew);
        Profile = (TextView) findViewById(R.id.Profile);
        listTask = (TextView) findViewById(R.id.listTask);
        chat = (TextView) findViewById(R.id.chat);
        videocall = (TextView) findViewById(R.id.videocall);
        audiocall = (TextView) findViewById(R.id.audiocall);
//        leave = (TextView) findViewById(R.id.leave);
        cancel = (TextView) findViewById(R.id.cancel);

        contactBean = (ContactBean) getIntent().getSerializableExtra("contact");
        tasktype = getIntent().getStringExtra("taskType");
        if (tasktype.equalsIgnoreCase("Group")) {
            getname = getIntent().getStringExtra("userId");
            groupname = getIntent().getStringExtra("groupname");
            Log.i("contact", "groupname " + getname);
        }

        Log.i("contact", "groupname ");
        if (tasktype.equalsIgnoreCase("Individual")) {
            assingNew.setText("Assign New Task");
            Profile.setText("View Profile");
            listTask.setText("View Existing Tasks");
            chat.setText("Chat");
            videocall.setText("Video Call");
            audiocall.setText("Audio Call");
//            leave.setText("Leave Request");
        } else {
            assingNew.setText("Assign New Task");
            Profile.setText("Conference Call");
            listTask.setText("View Existing Tasks");
            chat.setText("Chat");
            videocall.setText("Broadcast Video Call");
            audiocall.setText("Broadcast Audio Call");
//            leave.setVisibility(View.GONE);
        }
        groupMemberAccess = VideoCallDataBase.getDB(getApplication()).getMemberAccessList(String.valueOf(getname));
        Log.i("contact", "groupname!!! " + groupMemberAccess.getGroup_Task());

        assingNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tasktype.equalsIgnoreCase("Individual")) {
                    Intent intent = new Intent(getApplicationContext(), NewTaskConversation.class);
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                    taskDetailsBean.setToUserId(String.valueOf(contactBean.getUserid()));
                    taskDetailsBean.setToUserName(contactBean.getUsername());
                    taskDetailsBean.setTaskType("Individual");
                    intent.putExtra("task", "Newtask");
                    intent.putExtra("newTaskBean", taskDetailsBean);
                    startActivity(intent);
                    finish();
                } else if (tasktype.equalsIgnoreCase("Group")) {
                    /*Intent i = new Intent(getApplicationContext(), TaskHistory.class);
                    i.putExtra("userId",getname);
                    i.putExtra("taskType", "Group");
                    startActivity(i);*/
                    if (groupMemberAccess != null && groupMemberAccess.getGroup_Task().equalsIgnoreCase("1")) {
                        Intent intent = new Intent(getApplicationContext(), NewTaskConversation.class);
//                    Log.d("task", "toUserId" + group.getId());
                        TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                        taskDetailsBean.setToUserId(getname);
                        taskDetailsBean.setToUserName(groupname);
                        taskDetailsBean.setTaskType("Group");
                        intent.putExtra("task", "Newtask");
                        intent.putExtra("newTaskBean", taskDetailsBean);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplication(), "You are not rights to GroupTaskAccess ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tasktype.equalsIgnoreCase("Individual")) {
                    Intent intent = new Intent(getApplicationContext(), ViewProfile.class);
                    intent.putExtra("value", contactBean.getEmail());
                    startActivity(intent);
                    finish();
                } else if (tasktype.equalsIgnoreCase("Group")) {
//                    group.setIscheck(true);
//                    contactsfragment.conferencecall();
//                    finish();
                    contactTab = false;
                    MainActivity.isAudioCall = true;
                    ArrayList<String> grouplist = VideoCallDataBase.getDB(getApplicationContext()).selectGroupmembers("select * from groupmember where groupid= '" + getname + "'", "userid");
                    ArrayList<Integer> group_list_id = new ArrayList<Integer>();
                    for (String groupId : grouplist) {
                        group_list_id.add(Integer.parseInt(groupId));
                    }
                    if(group_list_id.size() > 0) {
                        callNotification(group_list_id, Appreference.loginuserdetails.getId());
                        Appreference.broadcast_call = false;
                    }
                }
            }
        });

        listTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tasktype.equalsIgnoreCase("Individual")) {
                    Intent intent = new Intent(getApplicationContext(), TaskHistory.class);
                    intent.putExtra("contact", contactBean);
                    intent.putExtra("userId", contactBean.getUsername());
                    intent.putExtra("taskType", "Individual");
                    startActivity(intent);
                    finish();
                } else if (tasktype.equalsIgnoreCase("Group")) {
                    Intent i = new Intent(getApplicationContext(), TaskHistory.class);
                    i.putExtra("userId", getname);
                    i.putExtra("taskType", "Group");
                    startActivity(i);
                    finish();
                }
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), NewTaskConversation.class);
                if (tasktype.equalsIgnoreCase("Individual")) {
                    ContactBean item = contactBean;
                    String buddy_uri = item.getUsername();
                    Log.i("chat", "chat Selected user-->" + buddy_uri);
                    ArrayList<TaskDetailsBean> taskDetailsBean = VideoCallDataBase.getDB(classContext).getChatnames(buddy_uri);
                    if (taskDetailsBean != null && taskDetailsBean.size() > 0 && taskDetailsBean.get(0) != null) {
                        Log.i("chat", "Chatetails size--->" + taskDetailsBean.get(0).getToUserId());
                        Log.i("chat", "db datetime-->" + taskDetailsBean.get(0));
                        Log.i("chat", "db cahtid--->" + taskDetailsBean.get(0));
                        i.putExtra("chatid", taskDetailsBean.get(0).getTaskId());
                        i.putExtra("task", "chathistory");
                        i.putExtra("chatHistoryBean", taskDetailsBean.get(0));
                        i.putExtra("catagory", taskDetailsBean.get(0).getCatagory());
                    } else {
                        i.putExtra("task", "chat");
                        i.putExtra("type", "individual");
                        i.putExtra("touser", buddy_uri);
                        i.putExtra("touserid", String.valueOf(item.getUserid()));
                    }
                    finish();
                } else if (tasktype.equalsIgnoreCase("Group")) {

                    i.putExtra("task", "chat");
                    i.putExtra("type", "group");
                    i.putExtra("touser", groupname);
                    i.putExtra("touserid", getname);
                    /*TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                    taskDetailsBean.setToUserId(group.getId());
                    taskDetailsBean.setToUserName(group.getName());
                    taskDetailsBean.setTaskType("Group");
                    i.putExtra("task", "chat");
                    i.putExtra("newTaskBean", taskDetailsBean);*/
                    finish();
                }
                startActivity(i);
            }
        });

        videocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tasktype.equalsIgnoreCase("Individual")) {
                    ContactBean item = contactBean;
                    String buddy_uri = item.getUsername();
                    Log.i("swipe call ", "name video " + buddy_uri);
//                        for (ContactBean contactBean : buddyList) {
//                            ContactBean item1 = buddyList.get(position);
//                            if (contactBean.getIscheck()) {
                    MainActivity.isAudioCall = false;
                    videocall.setClickable(false);


                    ArrayList<Integer> group_list_id = new ArrayList<Integer>();
                    group_list_id.add(item.userid);
                    if(group_list_id.size() > 0) {
                        callNotification(group_list_id, Appreference.loginuserdetails.getId());
                        Appreference.broadcast_call = false;
                    }

//                    callNotification(item.userid, Appreference.loginuserdetails.getId());
//                    Appreference.broadcast_call = false;
                } else if (tasktype.equalsIgnoreCase("Group")) {
                    if (groupMemberAccess != null && groupMemberAccess.getVideoAccess().equalsIgnoreCase("1")) {
                        contactTab = false;
//                    group.setIscheck(true);
                        MainActivity.isAudioCall = false;
                        ArrayList<String> grouplist = VideoCallDataBase.getDB(getApplicationContext()).selectGroupmembers("select * from groupmember where groupid= '" + getname + "'", "userid");
//                        for (String groupId : grouplist) {
//                            callNotification(Integer.parseInt(groupId), Appreference.loginuserdetails.getId());
//                            Appreference.broadcast_call = true;
//                        }
                        ArrayList<Integer> group_list_id = new ArrayList<Integer>();
                        for (String groupId : grouplist) {
                            group_list_id.add(Integer.parseInt(groupId));
                        }
                        if(group_list_id.size() > 0) {
                            callNotification(group_list_id, Appreference.loginuserdetails.getId());
                            Appreference.broadcast_call = false;
                        }

//                    contactsfragment.broadcastvideocall();
//                    finish();
                    }
                } else {
                    Toast.makeText(getApplication(), "You are not rights to VideoAccess ", Toast.LENGTH_SHORT).show();
                }
//                           }
            }
        });

        audiocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tasktype.equalsIgnoreCase("Individual")) {
                    ContactBean item = contactBean;
                    String buddy_uri_audio = item.getUsername();
                    Log.i("swipe call ", "name audio ** " + buddy_uri_audio);
                    Log.i("swipe call ", "name audio call---->  " + buddy_uri_audio);
                    if (MainActivity.gsmCallState == TelephonyManager.CALL_STATE_IDLE) {
                        MainActivity.isAudioCall = true;
//                        callNotification(item.userid, Appreference.loginuserdetails.getId());
//                        Log.i("swipe call ", "name audio item1.userid " + item.userid);
//                        Appreference.broadcast_call = false;
                        ArrayList<Integer> group_list_id = new ArrayList<Integer>();
                        group_list_id.add(item.userid);
                        if(group_list_id.size() > 0) {
                            callNotification(group_list_id, Appreference.loginuserdetails.getId());
                            Appreference.broadcast_call = false;
                        }
                    }
                } else if (tasktype.equalsIgnoreCase("Group")) {
//                    group.setIscheck(true);
//                    contactsfragment.broadcastaudiocall();
//                    finish();
                    if (groupMemberAccess != null && groupMemberAccess.getAudioAccess().equalsIgnoreCase("1")) {
                        contactTab = false;
                        MainActivity.isAudioCall = true;
                        ArrayList<String> grouplist = VideoCallDataBase.getDB(getApplicationContext()).selectGroupmembers("select * from groupmember where groupid= '" + getname + "'", "userid");
//                        for (String groupId : grouplist) {
//                            callNotification(Integer.parseInt(groupId), Appreference.loginuserdetails.getId());
//                            Appreference.broadcast_call = true;
//                        }
                        ArrayList<Integer> group_list_id = new ArrayList<Integer>();
                        for (String groupId : grouplist) {
                            group_list_id.add(Integer.parseInt(groupId));
                        }
                        if(group_list_id.size() > 0) {
                            callNotification(group_list_id, Appreference.loginuserdetails.getId());
                            Appreference.broadcast_call = false;
                        }
                    }
                } else {
                    Toast.makeText(getApplication(), "You are not rights to AudioAccess ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void callNotification(ArrayList<Integer> toIds, int fromId) {
        try {
            Log.i("callNotification", "Contactlistpage : fromId " + String.valueOf(fromId));
//            Log.i("callNotification", "toid " + String.valueOf(toIds));
           /* List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("fromId", String.valueOf(fromId)));
            nameValuePairs.add(new BasicNameValuePair("toId", String.valueOf(toId)));*/
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("fromId",String.valueOf(fromId));
            JSONObject jsonObject1=new JSONObject();
            JSONArray jsonArray=new JSONArray();
            for(int i = 0; i<toIds.size();i++) {
                int toId = toIds.get(i);
                jsonObject1.put("id", String.valueOf(toId));
//            JSONArray jsonArray=new JSONArray();
                jsonArray.put(i, jsonObject1);
            }
            jsonObject.put("to",jsonArray);
            Appreference.jsonRequestSender.callNotification(EnumJsonWebservicename.callNotification, jsonObject, Contactlistpage.this);
            showprogressforpriority("Call Connecting");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
        Log.d("App", "finished");
    }

    @Override
    public void ResponceMethod(Object object) {
        CommunicationBean communicationBean = (CommunicationBean) object;
        String s1 = communicationBean.getEmail();
        String s2 = communicationBean.getFirstname();

        try {
            if (s2 != null && s2.equalsIgnoreCase("callNotification")) {
                final JSONObject jsonObject = new JSONObject(communicationBean.getEmail());
                if (((int) jsonObject.get("result_code") == 0)) {
                    cancelDialog();
                    String result = (String) jsonObject.get("result_text");
                    Log.i("response", "result_text in response " + result);
                    //Toast.makeText(getContext(),result,Toast.LENGTH_LONG).show();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (((String) jsonObject.get("result_text")).equalsIgnoreCase("Your Call Notification Send Successfully.")) {
                                    Log.d("Remove", "cancel dialog");
                                    activateCall();
                                    Log.d("Remove", "active call dialog");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    Toast.makeText(classContext, "call conecting failure", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void ErrorMethod(Object object) {

    }

    public void activateCall() {
        Log.d("Remove", "inside active call dialog ");
        Log.i("Call Response", "activecall ");
        if (MainActivity.isAudioCall) {
            makeCall(null, true);
        } else {
            makeCall(null, false);
        }
    }

    public void makeCall(View view, boolean audio_call) {
        Log.i("SipVideo", "ContactlistPage makecall ");

        Log.i("SipVideo", "Came to makeCallMainActivity ");
        /* Only one call at anytime */
//        MainActivity.currentCall = null;
        if (MainActivity.currentCall != null) {
            Log.i("SipVideo", "Came to makeCallMain : Activity.currentCall != nul ");
        }
        MainActivity.currentCallArrayList.clear();

        if (contactTab) {
            sendConferencecallInfomessage();
            if (contactBean.getUsername() != null) {
                String buddy_uri = "sip:" + contactBean.getUsername() + "@" + getResources().getString(R.string.server_ip);
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
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("SipVideo", " delete: b2");
                    call.delete();
                    return;
                }
                MainActivity.currentCallArrayList.add(call);

                MainActivity.currentCall = call;
                Log.i("SipVideo", "ContactFragment Make Call MainActivity.currentCall");
            }
        } else {
            sendConferencecallInfomessage();
//                            Log.d("Remove", "name === " + group.getName());
            ArrayList<String> grouplist = VideoCallDataBase.getDB(classContext).selectGroupmembers("select * from groupmember where groupid= '" + getname + "'", "username");
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
                        return;
                    }
                    MainActivity.currentCallArrayList.add(call);
//        currentCall = call;
                    MainActivity.currentCall = call;
                    Log.i("SipVideo", "ContactFragment Make Call MainActivity.currentCall");
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

    private void showCallActivity() {
        Log.i("SipVideo", "showCallActivity method");
        Intent intent = new Intent(getApplicationContext(), CallActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String par_name = Appreference.loginuserdetails.getFirstName() + " " + Appreference.loginuserdetails.getLastName();
        intent.putExtra("host", par_name);
        startActivity(intent);
        finish();
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
            if (contactTab) {

                noofparticipants = z;

            } else {
                ArrayList<String> grouplist = VideoCallDataBase.getDB(classContext).selectGroupmembers("select * from groupmember where groupid= '" + getname + "'", "username");
                for (String Name : grouplist) {
                    noofparticipants = z;
                    z++;
                }
            }
            participantInfo = new String[noofparticipants];

            int j = 0;
            if (contactTab) {

                participantInfo[j] = contactBean.getUsername();
                Log.i("FT", "callerBean_Array" + contactBean.getUsername());
            } else {
                ArrayList<String> grouplist = VideoCallDataBase.getDB(classContext).selectGroupmembers("select * from groupmember where groupid= '" + getname + "'", "username");
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

            if (contactTab) {
                Log.i("call", "buddyList!=null && buddyList.size()>0");
                for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
                    String name = MainActivity.account.buddyList.get(i).cfg.getUri();
                    Log.i("confinfo", "buddyname-->" + name);
                    if (contactBean.getUsername() != null) {
                        String nn = "sip:" + contactBean.getUsername() + "@" + getResources().getString(R.string.server_ip);
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
            } else {

                for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
                    String name = MainActivity.account.buddyList.get(i).cfg.getUri();
                    Log.i("confinfo", "buddyname-->" + name);
                    ArrayList<String> grouplist = VideoCallDataBase.getDB(classContext).selectGroupmembers("select * from groupmember where groupid= '" + getname + "'", "username");
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

    private void showprogressforpriority(final String name) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("login123", "inside showProgressDialog");
                    dialog = new ProgressDialog(Contactlistpage.this);
                    dialog.setCancelable(false);
                    dialog.setMessage(name);
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setProgress(0);
                    dialog.setMax(100);
                    dialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void cancelDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                Log.i("register", "--progress bar end-----");
                dialog.dismiss();
                Appreference.isRequested_date = false;
                dialog = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
