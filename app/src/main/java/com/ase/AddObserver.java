package com.ase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;
import com.ase.RandomNumber.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.app.MyBuddy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.WebServiceInterface;

public class AddObserver extends Activity implements View.OnClickListener, WebServiceInterface {

    static AddObserver addObserver;
    private final Handler handler = new Handler();
    Button submit, cancel;
    TextView title;
    ListView listView;
    ArrayList<ContactBean> contactList, listofProjectMembers;
    Context context;
    BuddyArrayAdapter buddyArrayAdapter;
    String taskId, taskNo, taskName, newtaskId, toId, toUserName, taskTime = null, dateforrow,/*newSubType,*/
            newTaskNo,/*newTaskDescription,*/
            dateforDate, percentage = "0";
    ArrayList<String> chatUsers, chatUsersName, listOfObservers, chatUsersid;
    String taskType, groupname, taskReceiver, projectid, listMembers, projectValue, parentTaskId;
    boolean isTemplate = false;
    ProgressDialog dialog;
    private String quotes = "\"";
    ArrayList<TaskDetailsBean> taskDetailsBean = new ArrayList<>();
    String[] observerList;
    AppSharedpreferences appSharedpreferences;

    public static AddObserver getInstance() {
        return addObserver;
    }

    public void showToast(final String result) {
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Unable to Assign..", Toast.LENGTH_SHORT).show();
                    Log.i("template", "Unable to Assign error responce **  ");
                    cancelDialog();
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddObserver  showToast ","Exception "+e.getMessage(),"WARN",null);
        }

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
            Appreference.printLog("AddObserver cancelDialog ","Exception "+e.getMessage(),"WARN",null);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_observer);
        try {
            contactList = new ArrayList<>();
            context = this;
            addObserver = this;
            appSharedpreferences = AppSharedpreferences.getInstance(context);
            submit = (Button) findViewById(R.id.submit);
            cancel = (Button) findViewById(R.id.cancel);
            listView = (ListView) findViewById(R.id.post_list);

            submit.setOnClickListener(this);
            cancel.setOnClickListener(this);
            taskId = getIntent().getExtras().getString("taskId");
            taskType = getIntent().getExtras().getString("taskType");
            projectValue = getIntent().getExtras().getString("Project");
            parentTaskId = getIntent().getExtras().getString("ProjectParentId");


            Log.i("addobserver", "task type " + taskType);
            if (taskType != null)
                if (taskType.equalsIgnoreCase("Group"))
                    groupname = getIntent().getExtras().getString("groupname");
                else
                    taskReceiver = getIntent().getExtras().getString("taskReceiver");
            if (getIntent().getExtras().getString("from") != null && getIntent().getExtras().getString("from").toString().equalsIgnoreCase("templatetask")) {
                Log.e("template", taskId + "assigned");
                taskName = getIntent().getExtras().getString("taskName");
                taskNo = getIntent().getExtras().getString("taskNo");
                newTaskNo = getFileName();
                //            newSubType=getIntent().getExtras().getString("subType");
                //            newTaskDescription=getIntent().getExtras().getString("taskDescription");
                title = (TextView) findViewById(R.id.txtView01);
                title.setText("Assign Task To");
                //submit.setBackgroundResource(android.R.drawable.btn_default);
                //submit.setText("Assign");
                isTemplate = true;

            }
            Log.i("addobserver", "group name --> 0 " + groupname);
        /*handler.post(new Runnable() {
            @Override
            public void run() {*/
            if (!isTemplate) {
                String Query = "Select * from taskHistoryInfo where taskId ='" + taskId + "';";
                taskDetailsBean = VideoCallDataBase.getDB(context).getTaskHistoryInfo(Query);
            } else {
                Log.i("addobserver", "group name --> 1 " + groupname);
                contactList = VideoCallDataBase.getDB(context).getContact(Appreference.loginuserdetails.getUsername());
            }
            if (taskType != null) {
                if (projectValue.equalsIgnoreCase("yes")) {
                    contactList.clear();
                    String listMembers = VideoCallDataBase.getDB(context).getProjectListMembers(parentTaskId);
                    String Query = "Select * from projectHistory where taskId ='" + taskId + "';";
                    taskDetailsBean = VideoCallDataBase.getDB(context).getProjectHistoryInfo(Query);
                    int counter1 = 0;
                    String names = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskMemberList from projectHistory where taskId ='" + taskId + "';");
                    Log.i("addobserver", "group name --> 2 " + names);
                    Log.i("addobserver", "group name --> 2 " + listMembers);
                    for (int i = 0; i < names.length(); i++) {
                        if (names.charAt(i) == ',')
                            counter1++;
                    }
                    if (listMembers != null) {
                        int counter = 0;
                        for (int i = 0; i < listMembers.length(); i++) {
                            if (listMembers.charAt(i) == ',') {
                                counter++;
                            }
                            Log.i("taskConversation", "project_details Task Mem's counter size is == " + counter);
                        }
                        //                    Log.i("taskConversation", "projectBean.getFromUserName() " + projectBean.getFromUserName());
                        for (int j = 0; j < counter + 1; j++) {
                            Log.i("taskConversation", "project_details Task Mem's and position == " + listMembers.split(",")[j] + " " + j);
                            if (counter == 0) {
                                contactList.add(VideoCallDataBase.getDB(context).getContactObject(listMembers));
                                //                                listOfObservers.add(listMembers);
                                //                                listObservers.add(listMembers);
                            } else {
                                if (taskType.equalsIgnoreCase("Group")) {

                                    Log.i("addobserver", "group name -> 2 " + counter1);
                                    boolean added = false;
                                    for (int k = 0; k < counter1 + 1; k++) {
                                        Log.i("addobserver", "Member name -> 2 " + listMembers.split(",")[j]);
                                        Log.i("addobserver", "Member name -> 2 " + names.split(",")[k]);
                                        if (counter1 == 0) {
                                            if(names.equalsIgnoreCase(listMembers.split(",")[j]))
                                                added = true;
//                                                contactList.add(VideoCallDataBase.getDB(context).getContactObject(listMembers.split(",")[j]));
                                        } else {
                                            if (listMembers.split(",")[j].equalsIgnoreCase(names.split(",")[k]) || listMembers.split(",")[j].equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                                added = true;
                                                break;
                                            }
                                        }
                                    }
                                    Log.i("addobserver", "group name --> 2 " + added);
                                    if (!added) {
                                        contactList.add(VideoCallDataBase.getDB(context).getContactObject(listMembers.split(",")[j]));
                                    }

                                } else {
                                    if (listMembers.split(",")[j].equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    } else {
                                        contactList.add(VideoCallDataBase.getDB(context).getContactObject(listMembers.split(",")[j]));
                                        //                                    listOfObservers.add(listMembers.split(",")[j]);
                                        //                                    listObservers.add(projectGroup_Mems.split(",")[j]);
                                    }
                                }
                            }
                        }
                    }
                } else if (taskType.equalsIgnoreCase("Group")) {
                    Log.i("add observer", "group name --> 2 " + groupname);
                    contactList = VideoCallDataBase.getDB(context).getGroupContact(Appreference.loginuserdetails.getUsername(), groupname);
                    Log.i("add observer", "Contact list --> 0 " + contactList.size());
                } else {
                    contactList = VideoCallDataBase.getDB(context).getContact(Appreference.loginuserdetails.getUsername());
                }
            }
            Log.i("taskReceiver", "taskReceiver  " + taskReceiver);

                for (int i = 0; i < contactList.size(); i++) {
                    ContactBean name = contactList.get(i);
                    Log.i("addobserver", "Member name -> 3 " + name.getUsername());
                    if(name.getUsername() != null && name.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())){
                        contactList.remove(i);
                    } else if (taskReceiver != null && name.getUsername() != null && name.getUsername().equalsIgnoreCase(taskReceiver)) {
                        contactList.remove(i);
                        Log.i("addobserver", "Contact list --> 1" + contactList.size());
                        break;
                    } else if(name.getUsername() == null){
                        contactList.remove(i);
                    }

                }
            Log.i("taskReceiver", "taskReceiver  " + contactList.size());
            //Log.i("add observer","task observerlist size"+ taskDetailsBean.size());
            //contactList = VideoCallDataBase.getDB(context).getContact(Appreference.loginuserdetails.getUsername());
            if (taskDetailsBean.size() > 0) {
                TaskDetailsBean taskDetailsBean1 = taskDetailsBean.get(taskDetailsBean.size() - 1);
                Log.i("taskobserver", "observerlist " + taskDetailsBean1);
                Log.i("taskobserver", "observerlist1 " + taskDetailsBean1.getTaskObservers());

                if (taskDetailsBean1.getTaskObservers() != null) {
                    observerList = taskDetailsBean1.getTaskObservers().split(",");
                    if (observerList != null) {
                        Log.i("addobserver", "task observer size" + observerList.length);
                        for (ContactBean contactBean : contactList) {

                            for (String taskDetailsBean2 : observerList) {
                                Log.i("taskobserver", contactBean.getUsername() + "is equal ?" + taskDetailsBean2);
                                if (taskDetailsBean2 != null && !taskDetailsBean2.equalsIgnoreCase("") && contactBean.getUsername().equalsIgnoreCase(taskDetailsBean2)) {
                                    Log.i("taskobserver", contactBean.getUsername() + "equal" + taskDetailsBean2);
                                    contactBean.setIscheck(true);
                                }
                            }
                        }
                    }
                }
            }
           /* }
        });*/
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateforrow = dateFormat.format(new Date());

            SimpleDateFormat dateFormatt = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
            taskTime = dateFormatt.format(new Date());
            taskTime = taskTime.split(" ")[1] + " " + taskTime.split(" ")[2];
//            String query = "select * from taskDetailsInfo where loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + taskId + "' and mimeType='observer' order by id DESC LIMIT 1";
//            ArrayList<TaskDetailsBean> taskDetailsBeenList = VideoCallDataBase.getDB(context).getTaskHistory(query);


        /*String query = "select * from taskDetailsInfo where loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + taskId + "' and mimeType='observer' order by id DESC LIMIT 1";
        ArrayList<TaskDetailsBean> taskDetailsBeenList = VideoCallDataBase.getDB(context).getTaskHistory(query);

        if (taskDetailsBeenList.size() > 0) {
            TaskDetailsBean taskDetailsBean = taskDetailsBeenList.get(0);

            String taskObservers = taskDetailsBean.getTaskObservers();
            Log.d("TaskObserver", "Task Observer  == " + taskObservers);


            int counter = 0;
            for (int i = 0; i < taskObservers.length(); i++) {
                if (taskObservers.charAt(i) == ',') {
                    counter++;
                }
                Log.d("TaskObserver", "Task Observer counter size is == " + counter);

                for (int j = 0; j < counter + 1; j++) {
                    if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(taskObservers.split(",")[j])) {
                        listOfObservers.add(taskObservers.split(",")[j]);
                        Log.d("TaskObserver", "Task Observer name not in same user== " + taskObservers.split(",")[j]);
                    }
                }
            }*/


            // }
            Log.i("addObserver", "contactList 1 " + contactList.size());
            try {
                if (contactList != null && contactList.size() > 0) {
                    Collections.sort(contactList, new CustomComparator());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("AddObserver  Collections ","Exception "+e.getMessage(),"WARN",null);
            }

            buddyArrayAdapter = new BuddyArrayAdapter(context, contactList);
            listView.setAdapter(buddyArrayAdapter);
            buddyArrayAdapter.notifyDataSetChanged();
            handler.post(new Runnable() {
                @Override
                public void run() {
//                    Collections.sort(contactList, new CustomComparator());
                    buddyArrayAdapter.notifyDataSetChanged();
                }
            });
            if (!isTemplate) {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            ContactBean item = contactList.get(position);
                            String buddy_uri = item.getUsername();
                            if (!item.getIscheck()) {
                                Log.e("sipTest", "Inside True 0");
                                item.setIscheck(true);
                            } else {
                                Log.e("sipTest", "Inside False 0");
                                item.setIscheck(false);
                            }
                            buddyArrayAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("AddObserver  ListView item click if ","Exception "+e.getMessage(),"WARN",null);
                        }
                    }
                });
            } else {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            ContactBean item = contactList.get(position);
                            String buddy_uri = item.getUsername();

                            for (int i = 0; i < contactList.size(); i++) {
                                contactList.get(i).setIscheck(false);
                                Log.e("sipTest", "Inside True 1");
                            }

                            contactList.get(position).setIscheck(true);
                            Log.e("sipTest", "Inside True 2");
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i("addObserver", "contactList 2 " + contactList.size());
                                    buddyArrayAdapter.notifyDataSetChanged();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("AddObserver lictView itemclick else ","Exception "+e.getMessage(),"WARN",null);
                        }
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddObserver  onCreate","Exception "+e.getMessage(),"WARN",null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            isTemplate = false;
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddObserver  onDestroy ","Exception "+e.getMessage(),"WARN",null);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            int id = v.getId();
            if (id == R.id.submit) {
                submitClickEvent();
                return;
            }
            if (id == R.id.cancel) {
                cancelClilckEvent();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddObserver OnClick ","Exception "+e.getMessage(),"WARN",null);
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
            Appreference.printLog("AddObserver  getFileName ","Exception "+e.getMessage(),"WARN",null);
        }
        return strFilename;
    }

    public void showDialog() {
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dialog = new ProgressDialog(AddObserver.this);
                    dialog.setMessage("Assigning...");
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setProgress(0);
                    dialog.setMax(100);
                    dialog.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddObserver  showDialog","Exception "+e.getMessage(),"WARN",null);
        }
    }

    public void submitClickEvent() {
        try {
            chatUsers = new ArrayList<String>();
            chatUsersid = new ArrayList<String>();
            chatUsersName = new ArrayList<>();
            if (isTemplate) {
                showDialog();
                boolean check = false;
                for (int position = 0; position < contactList.size(); position++) {
                    ContactBean item = contactList.get(position);
                    if (item.getIscheck()) {
                        JSONObject jsonObject = new JSONObject();
                        JSONObject id = new JSONObject();
                        try {
                            id.put("id", taskId);
                            jsonObject.put("task", id);
                            jsonObject.put("fromId", Appreference.loginuserdetails.getId());
                            jsonObject.put("toId", item.getUserid());
                            jsonObject.put("taskName", taskName);
                            jsonObject.put("taskNo", newTaskNo);
                            toUserName = item.getUsername();
                            check = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Appreference.printLog("AddObserver  Submit ClickEvent Json add observer ","Exception "+e.getMessage(),"WARN",null);
                        }
                        Log.i("template", "Assign template" + jsonObject.toString());
                        Appreference.jsonRequestSender.templateTask(EnumJsonWebservicename.assignTemplate, jsonObject, AddObserver.this, null);
                   /* String query="select * from taskDetailsInfo where taskId='"+taskId+"'";
                    ArrayList<TaskDetailsBean> list=VideoCallDataBase.getDB(context).getTaskHistory(query);*/
                        break;

                    }

                }
                if (!check) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Please select the contact", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            } else {
                ArrayList<String> rejected = new ArrayList<>();

                for (int position = 0; position < contactList.size(); position++) {
                    ContactBean item = contactList.get(position);
                    if (item.getIscheck()) {
                        String buddy_uri = String.valueOf(item.getUserid());
                        String buddy_Name = item.getUsername();
                        chatUsers.add(buddy_uri);
                        chatUsersName.add(buddy_Name);
                        Log.i("task", "chat Selected user-->" + buddy_uri);

                    }
                }


                if (observerList != null) {
                    for (String oldName : observerList) {
                        boolean check = false;
                        for (int i = 0; i < chatUsersName.size(); i++) {
                            String newName = chatUsersName.get(i);
                            Log.i("taskobserver", "name " + newName);
                            Log.i("taskobserver", "name 1 " + oldName);
                            if (oldName.equalsIgnoreCase(newName))
                                check = true;
                        }
                        if (!check && !oldName.equalsIgnoreCase("")) {
                            rejected.add(oldName);
                            Log.i("task", "rejected observer" + oldName);
                        }
                    }
                }


                if (rejected.size() > 0) {
                    for (String str : rejected) {
                        int rej_id = VideoCallDataBase.getDB(context).getUserid(str);
                        chatUsersid.add(String.valueOf(rej_id));
                        Log.i("task", "chat Selected user-->" + chatUsersid);
                    }
                }
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                JSONArray jsonArray1 = new JSONArray();

                try {
                    jsonObject.put("taskId", Integer.valueOf(taskId));
                    for (String id : chatUsers) {
                        JSONObject object = new JSONObject();
                        Log.i("task", "chat Selected user-->  " + id);
                        object.put("id", Integer.valueOf(id));

                        jsonArray.put(object);
                    }

                    jsonObject.put("addObserver", jsonArray);

                    for (String id : chatUsersid) {
                        JSONObject object = new JSONObject();
                        Log.i("task", "chat Selected user-->  " + id);
                        object.put("id", Integer.valueOf(id));

                        jsonArray1.put(object);
                    }
                    jsonObject.put("removeObserver", jsonArray1);
                    Appreference.jsonRequestSender.taskObserverEntry(EnumJsonWebservicename.taskObserverEntry, jsonObject, AddObserver.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Appreference.printLog("AddObserver  SuBmitClickEvent Json remove Observer ","Exception "+e.getMessage(),"WARN",null);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddObserver  Submit Click Event","Exception "+e.getMessage(),"WARN",null);
        }
    }

//    public void PercentageWebService(String getMediaPath, String sig_id, int isDateorUpdateorNormal) {
//        if (!getMediaPath.equals(null)) {
//            /*
//            * Newly Added
//             */
//            String subType = "normal";
//            String getMediaType = "text";
//
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String dateTime = dateFormat.format(new Date());
//            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//            String dateforrow = dateFormat.format(new Date());
//            String tasktime = dateTime;
//            tasktime = tasktime.split(" ")[1];
//            String taskUTCtime = dateforrow;
//            Log.i("task", "tasktime" + tasktime);
//            Log.i("UTC", "sendMessage utc time" + dateforrow);
//            final TaskDetailsBean chatBean = new TaskDetailsBean();
//
//            chatBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
//            chatBean.setFromUserName(Appreference.loginuserdetails.getUsername());
//            chatBean.setSelect(false);
//            chatBean.setToUserName(toUserName);
//            chatBean.setToUserId(toId);
//            chatBean.setTaskDescription(getMediaPath);
//            chatBean.setSignalid(sig_id);
//            chatBean.setTaskNo(newTaskNo);
////            chatBean.setIssueId(newtaskId);
//            chatBean.setParentId(getFileName());
//            chatBean.setTaskType(taskType);
//            chatBean.setTaskPriority("Medium");
//            chatBean.setIsRemainderRequired("");
//            chatBean.setCompletedPercentage("0");
//            chatBean.setPlannedStartDateTime("");
//            chatBean.setPlannedEndDateTime("");
//            chatBean.setRemainderFrequency("");
//            chatBean.setTaskUTCDateTime(dateforrow);
//            chatBean.setDateTime(dateTime);
//            chatBean.setTaskReceiver(taskReceiver);
//            chatBean.setTasktime(tasktime);
//            chatBean.setTaskUTCTime(taskUTCtime);
//            chatBean.setMimeType(getMediaType);
//            chatBean.setTaskId(newtaskId);
//            chatBean.setOwnerOfTask(Appreference.loginuserdetails.getUsername());
////            chatBean.setTaskStatus(taskStatus);
//            if (getResources().getString(R.string.task_enable).equalsIgnoreCase("enable")) {
//                chatBean.setTaskStatus("assigned");
//            } else {
//                chatBean.setTaskStatus("inprogress");
//            }
//            // send status 0 is send 1 is unsend
//            chatBean.setSendStatus("0");
//            chatBean.setMsg_status(0);
//            chatBean.setWs_send("0");
//            chatBean.setCustomTagVisible(true);
//            if ((isTemplate && note) || chat) {
//                chatBean.setCatagory(category);
//            }
//            if (getMediaType != null && getMediaType.equalsIgnoreCase("textfile")) {
//                chatBean.setLongmessage("0");
//            }
//            if (!getMediaType.equalsIgnoreCase("text")) {
//                chatBean.setShow_progress(0);
//            }
//            if (projectValue.equalsIgnoreCase("yes")) {
//                chatBean.setProjectId(projectId);
//                if (projectGroup_Mems != null) {
//                    chatBean.setGroupTaskMembers(projectGroup_Mems);
//                }
//            }
//            chatBean.setSubType(subType);
//            chatBean.setTaskRequestType("taskDescription");
//            Log.i("taskconversation", "istaskName percentagewebservice else ");
//            chatBean.setSubType(subType);
//            if (isDateorUpdateorNormal == 1) {
//                chatBean.setSubType("percentageCompleted");
//                chatBean.setTaskRequestType("percentageCompleted");
//            }
//            if (isDateorUpdateorNormal == 2) {
//                chatBean.setSubType("taskDateChangedRequest");
//                chatBean.setTaskRequestType("taskDateChangedRequest");
//            } else {
//                chatBean.setTaskRequestType(subType);
//            }
//            chatBean.setTaskName(taskName);
//            Log.e("task", "taskname **" + taskName);
//            Log.i("taskconversation", "mediaListBean.getMimeType() --------> 10 " + getMediaType);
//            try {
//                JSONObject jsonObject = new JSONObject();
//                JSONObject jsonObject1 = new JSONObject();
//                jsonObject1.put("id", Integer.parseInt(newtaskId));
//                jsonObject.put("task", jsonObject1);
//                JSONObject jsonObject2 = new JSONObject();
//                jsonObject2.put("id", Appreference.loginuserdetails.getId());
//                jsonObject.put("from", jsonObject2);
//                if ((!isTemplate && !project) || chat || note) {
//                    Log.i("taskconversation", "toUserId " + toUserId);
//                    JSONObject jsonObject3 = new JSONObject();
//                    jsonObject3.put("id", toUserId);
//                    if (taskType.equalsIgnoreCase("group")) {
//                        jsonObject.put("group", jsonObject3);
//                    } else {
//                        jsonObject.put("to", jsonObject3);
//                    }
//                }
//                jsonObject.put("signalId", sig_id);
//                jsonObject.put("parentId", getFileName());
//                jsonObject.put("createdDate", dateforrow);
//                jsonObject.put("requestType", "taskConversation");
//
//                if (getMediaPath != null && (getMediaPath.equalsIgnoreCase("This Task is closed") || getMediaPath.equalsIgnoreCase("This issue is closed"))) {
//                    jsonObject.put("requestStatus", "");
//                    Log.i("completepercentage ", "requeststatus ***" + getMediaPath);
//                } else {
//                    jsonObject.put("requestStatus", "approved");
//                }
//                jsonObject.put("taskEndDateTime", "");
//                jsonObject.put("taskStartDateTime", "");
//                jsonObject.put("remainderDateTime", "");
//                jsonObject.put("dateFrequency", "");
//                jsonObject.put("timeFrequency", "");
//                jsonObject.put("remark", "");
//                JSONObject jsonObject5 = new JSONObject();
//                jsonObject5.put("id", Appreference.loginuserdetails.getId());
//                JSONObject jsonObject4 = new JSONObject();
//                jsonObject4.put("user", jsonObject5);
//                switch (getMediaType.toLowerCase().trim()) {
//                    case "text":
//                        Log.i("taskconversation", "mediaListBean.getMimeType() --------> 17 " + getMediaType);
//                        jsonObject4.put("fileType", "text");
//                        jsonObject4.put("description", getMediaPath);
//                        break;
//                }
//                JSONArray jsonArray = new JSONArray();
//                jsonArray.put(0, jsonObject4);
//                jsonObject.put("listTaskConversationFiles", jsonArray);
//
//                if (jsonObject != null) {
//
//                    Appreference.jsonRequestSender.taskConversationEntry(EnumJsonWebservicename.taskConversationEntry, jsonObject, this, null, chatBean);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private static byte[] loadFile(File file) throws IOException {
        byte[] bytes = new byte[0];
        try {
            InputStream is = new FileInputStream(file);
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                // File is too large
            }
            bytes = new byte[(int) length];
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
        } catch (IOException e) {
            e.printStackTrace();
            Appreference.printLog("AddObserver  LoadFile ","Exception "+e.getMessage(),"WARN",null);
        }
        return bytes;
    }

    private String encodeFileToBase64Binary(String fileName) throws IOException {
        String encodedString = null;
        try {
            File file = new File(fileName);
            byte[] bytes = loadFile(file);
            byte[] encoded = org.apache.commons.codec.binary.Base64.encodeBase64(bytes);
            encodedString = new String(encoded);
        } catch (IOException e) {
            e.printStackTrace();
            Appreference.printLog("AddObserver  encodeFileTOBase64Binary ","Exception "+e.getMessage(),"WARN",null);
        }
        return encodedString;
    }

    private String encodeTobase64(Bitmap image) {
        String imageEncoded = null;
        try {
            Bitmap immagex = image;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            immagex.compress(Bitmap.CompressFormat.JPEG, 75, baos);
            byte[] b = baos.toByteArray();
            imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddObserver encodeTobase64 ","Exception "+e.getMessage(),"WARN",null);
        }
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
            Appreference.printLog("AddObserver  encodeAudioVideoToBase64 ","Exception "+e.getMessage(),"WARN",null);
        }
        return strFile;
    }

    public void cancelClilckEvent() {
        try {
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddObserver  cancelClickevent  ","Exception "+e.getMessage(),"WARN",null);
        }
    }


    public void notifypostEntryResponse(final String values) {
        Log.i("postEntry", "NewTaskactivity  notifypostEntryResponse method");
//
        try {
            JSONObject json = new JSONObject(values);
            ArrayList<String> rejected = new ArrayList<>();
            ArrayList<String> newly_added = new ArrayList<>();
            if (observerList != null) {
                for (String oldName : observerList) {
                    boolean check = false;
                    for (int i = 0; i < chatUsersName.size(); i++) {
                        String newName = chatUsersName.get(i);
                        Log.i("taskobserver", "name " + newName);
                        Log.i("taskobserver", "name 1 " + oldName);
                        if (oldName.equalsIgnoreCase(newName))
                            check = true;

                    }
                    if (!check && !oldName.equalsIgnoreCase("")) {
                        rejected.add(oldName);
                        Log.i("task", "rejected observer " + oldName);
                    }
                }
            }

            if (chatUsersName != null) {
                for (String new_user : chatUsersName) {
                    if (observerList != null) {
                        boolean added_new_observer = true;
                        for (String old_user : observerList) {
                            if (new_user != null && old_user != null) {
                                if (new_user.trim().equalsIgnoreCase(old_user.trim())) {
                                    added_new_observer = false;
                                    break;
                                }
                            }
                        }
                        if (added_new_observer) {
                            newly_added.add(new_user);
                        }
                    } else {
                        newly_added.add(new_user);
                    }
                }

            }

//            if (!values.contains("result_code")) {
            int resultCode = json.getInt("result_code");
            final String text = json.getString("result_text");

            Intent intent = new Intent();
            intent.putStringArrayListExtra("observer", chatUsersName);
            intent.putStringArrayListExtra("added", newly_added);
            intent.putStringArrayListExtra("rejected", rejected);
            Log.i("task", "rejected" + rejected.size());
            setResult(RESULT_OK, intent);

            finish();


            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("task", "Response   == " + text);
                }
            });
//
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddObserver notifyPostEntryResponce ","Exception "+e.getMessage(),"WARN",null);
        }
    }

    @Override
    public void ResponceMethod(Object object) {
        try {
            CommunicationBean communicationBean = (CommunicationBean) object;
            String response = communicationBean.getEmail();
            Log.i("template", response);
            ArrayList<TaskDetailsBean> arrayList;
            JsonElement jelement = new JsonParser().parse(response);
            if (jelement.getAsJsonObject() != null) {
                JsonObject jobject = jelement.getAsJsonObject();
                newtaskId = jobject.get("id").toString();
                Log.i("template", "new task Id " + newtaskId);
                toId = jobject.get("to").toString();
                String query = "select * from taskDetailsInfo where taskId='" + taskId + "';";
                arrayList = VideoCallDataBase.getDB(context).getTaskHistory(query);
                Log.i("template", "taskId " + taskId);
                Log.i("template", "query " + query);
                Log.i("template", "arrayList " + arrayList.size());
//                for (TaskDetailsBean bean : arrayList) {
                for (int i = 0; i < arrayList.size(); i++) {
                    TaskDetailsBean bean = arrayList.get(i);
                    if (bean.getMimeType().equalsIgnoreCase("date")) {
                        bean.setMimeType("dates");
                    }
                    bean.setToUserId(toId);
                    bean.setMsg_status(1);
                    bean.setToUserName(toUserName);
                    bean.setTaskReceiver(toUserName);
                    if (getResources().getString(R.string.task_enable).equalsIgnoreCase("enable")) {
                        bean.setTaskStatus("assigned");
                    } else {
                        bean.setTaskStatus("inprogress");
                    }
                    bean.setSignalid(Utility.getSessionID());
                    bean.setTaskId(newtaskId);
                    bean.setTaskNo(newTaskNo);
                    bean.setDateTime(dateforrow);
                    bean.setTasktime(taskTime);
                    bean.setTaskName(taskName);
                    bean.setOwnerOfTask(Appreference.loginuserdetails.getUsername());
                    bean.setCatagory("Task");
                    if (taskType != null && !taskType.equalsIgnoreCase("Group"))
                        bean.setTaskRequestType("Individual");
                    else if (taskType != null && taskType.equalsIgnoreCase("Group"))
                        bean.setTaskRequestType("group");
//                    percentage=VideoCallDataBase.getDB(context).getlastCompletedParcentage(taskId);
                    bean.setCompletedPercentage("0");
                    bean.setCustomTagVisible(false);
                    if (i == 0) {
                        bean.setSubType("taskDescription");
                    } else {
                        bean.setSubType("normal");
                    }
                    VideoCallDataBase.getDB(context).insertORupdate_Task_history(bean);
                    VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(bean);
//                    appSharedpreferences.saveBoolean("syncTask" + bean.getTaskId(), true);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Task Assigned Succesfully.", Toast.LENGTH_SHORT).show();
                        Log.i("template", "Assigned task Sucessfully");
                    }
                });
                arrayList.get(0).setTaskName(taskName);
                arrayList.get(0).setOwnerOfTask(Appreference.loginuserdetails.getUsername());
                final String xml = composeObserverXml(arrayList.get(0));

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("task", "xml file" + xml);
                        sendInstantMessage(xml);
                    }
                });

                if (dialog.isShowing())
                    dialog.dismiss();
                finish();
            } else {
                cancelDialog();
                Toast.makeText(getApplicationContext(), "Unable to Assign.", Toast.LENGTH_SHORT).show();
                Log.i("template", "Unable to Assign -ve ");
                finish();
            }
        } catch (Exception e) {
            if (dialog.isShowing())
                dialog.dismiss();
            e.printStackTrace();
            Appreference.printLog("AddObserver Responce Method","Exception "+e.getMessage(),"WARN",null);
        }
    }

    @Override
    public void ErrorMethod(Object object) {
        try {
            showToast("Unable to Assign.");
            Log.i("template", "Unable to Assign error responce ");
            cancelDialog();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddObserver Error Method","Exception "+e.getMessage(),"WARN",null);
        }
    }

    public String composeObserverXml(TaskDetailsBean cmbean) {
        StringBuffer buffer = new StringBuffer();
        try {
            buffer.append("<?xml version=\"1.0\"?>"
                    + "<TaskDetailsinfo><TaskDetails");
            buffer.append(" taskName=" + quotes + cmbean.getTaskName() + quotes);
            buffer.append(" taskDescription=" + quotes + cmbean.getTaskDescription() + quotes);
            Log.i("template", "taskType" + cmbean.getTaskDescription());
            buffer.append(" fromUserId=" + quotes + cmbean.getFromUserId() + quotes);
            buffer.append(" fromUserName=" + quotes + cmbean.getFromUserName() + quotes);
            buffer.append(" toUserId=" + quotes + cmbean.getToUserId() + quotes);
            buffer.append(" toUserName=" + quotes + cmbean.getToUserName() + quotes);
            buffer.append(" taskNo=" + quotes + cmbean.getTaskNo() + quotes);
            buffer.append(" taskType=" + quotes + cmbean.getTaskType() + quotes);
            Log.i("template", "taskType" + cmbean.getTaskType());
            buffer.append(" plannedStartDateTime=" + quotes + cmbean.getPlannedStartDateTime() + quotes);
            buffer.append(" plannedEndDateTime=" + quotes + cmbean.getPlannedEndDateTime() + quotes);
            buffer.append(" isRemainderRequired=" + quotes + cmbean.getIsRemainderRequired() + quotes);
            buffer.append(" remainderDateTime=" + quotes + cmbean.getRemainderFrequency() + quotes);
            buffer.append(" taskStatus=" + quotes + "Template" + quotes);
            buffer.append(" signalid=" + quotes + cmbean.getSignalid() + quotes);
            buffer.append(" parentId=" + quotes + cmbean.getParentId() + quotes);
            buffer.append(" mimeType=" + quotes + "template" + quotes);
            buffer.append(" dateTime=" + quotes + cmbean.getDateTime() + quotes);
            buffer.append(" taskPriority=" + quotes + cmbean.getTaskPriority() + quotes);
            buffer.append(" dateFrequency=" + quotes + cmbean.getDateFrequency() + quotes);
            buffer.append(" timeFrequency=" + quotes + cmbean.getTimeFrequency() + quotes);
            buffer.append(" taskRequestType=" + quotes + cmbean.getTaskRequestType() + quotes);
            buffer.append(" requestStatus=" + quotes + cmbean.getRequestStatus() + quotes);
            buffer.append(" taskId=" + quotes + cmbean.getTaskId() + quotes);
            buffer.append(" taskOwner=" + quotes + cmbean.getOwnerOfTask() + quotes);
            buffer.append(" taskReceiver=" + quotes + cmbean.getToUserName() + quotes);
            buffer.append(" completedPercentage=" + quotes + cmbean.getCompletedPercentage() + quotes);
            buffer.append(" remark=" + quotes + cmbean.getRemark() + quotes);
            buffer.append(" taskCategory=" + quotes + cmbean.getCatagory() + quotes);
            buffer.append(" />");
            buffer.append("</TaskDetailsinfo>");
            Log.d("xml", "composed xml for chat======>" + buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddObserver ComposeChatXml Method","Exception "+e.getMessage(),"WARN",null);

        } finally {
            return buffer.toString();
        }

    }

    public void sendInstantMessage(String msgBody) {
        Log.i("task", "ready to send");
        try {
            for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
                String name = MainActivity.account.buddyList.get(i).cfg.getUri();
                Log.i("task", "buddyname-->" + name);
                String nn = "sip:" + toUserName + "@" + getResources().getString(R.string.server_ip);
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
                        Appreference.printLog("AddObserver sendInstantMessage Method","Exception "+e.getMessage(),"WARN",null);
                    }
                    break;
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            Appreference.printLog("AddObserver sendinstantmessage ","Exception "+e.getMessage(),"WARN",null);
        }
    }
}
