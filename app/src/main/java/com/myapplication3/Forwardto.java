package com.myapplication3;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.myapplication3.Bean.TaskDetailsBean;
import com.myapplication3.DB.VideoCallDataBase;
import com.myapplication3.RandomNumber.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;

import json.EnumJsonWebservicename;

/**
 * Created by vignesh on 3/3/2017.
 */
public class Forwardto extends Activity {
    public Button cancel, submit;
    public ListView forwardlist;
    BuddyArrayAdapter buddyArrayAdapter;
    ArrayList<ContactBean> contactList;
    Handler handler = new Handler();
    TaskDetailsBean taskbean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forwardto);
        cancel = (Button) findViewById(R.id.cancel);
        submit = (Button) findViewById(R.id.submit);
        contactList = new ArrayList<>();
        taskbean = (TaskDetailsBean) getIntent().getSerializableExtra("taskbean");
        Log.i("NewTaskConversation", "inside 3848" + taskbean.getTaskDescription());
        contactList = VideoCallDataBase.getDB(getApplicationContext()).getContact(Appreference.loginuserdetails.getUsername(), taskbean.getFromUserName(), taskbean.getTaskReceiver());
        forwardlist = (ListView) findViewById(R.id.forward_list);
        if (taskbean.getTaskType().equalsIgnoreCase("Individual")) {
            for (int i = 0; i < contactList.size(); i++) {
                ContactBean contactBean = contactList.get(i);
                if(contactBean.getUsername().equalsIgnoreCase(taskbean.getFromUserName()) || contactBean.getUsername().equalsIgnoreCase(taskbean.getTaskReceiver()) || contactBean.getUsername().equalsIgnoreCase(taskbean.getToUserName())){
                    contactList.remove(i);
                }
            }
        } else {
            String names = VideoCallDataBase.getDB(getApplicationContext()).getProjectParentTaskId("select taskMemberList from taskHistoryInfo where taskId = '"+taskbean.getTaskId()+"';");
            String[] items = names.split(",");
            for (String item : items) {
                for (int i = 0; i < contactList.size(); i++) {
                    ContactBean contactBean = contactList.get(i);
                    if(contactBean.getUsername().equalsIgnoreCase(item)){
                        contactList.remove(i);
                    }
                }
            }
        }
        buddyArrayAdapter = new BuddyArrayAdapter(getApplicationContext(), contactList);
        forwardlist.setAdapter(buddyArrayAdapter);
        buddyArrayAdapter.notifyDataSetChanged();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Collections.sort(contactList, new CustomComparator());
                buddyArrayAdapter.notifyDataSetChanged();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        forwardlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (ContactBean contactBean : contactList) {
                    if (contactBean.getIscheck()) {
                        Log.e("sipTest", "Inside True 0");
                        String buddy_uri = contactBean.getUsername();
                        ArrayList<TaskDetailsBean> taskDetailsBean = VideoCallDataBase.getDB(getApplicationContext()).getChatnames(buddy_uri);
                        Intent i = new Intent();
                        if (taskDetailsBean != null && taskDetailsBean.size() > 0 && taskDetailsBean.get(0) != null) {
                            Log.i("chat", "Chatetails size--->" + taskDetailsBean.get(0).getToUserId());
                            Log.i("chat", "db datetime-->" + taskDetailsBean.get(0));
                            Log.i("chat", "db cahtid--->" + taskDetailsBean.get(0));
                            i.putExtra("chatid", taskDetailsBean.get(0).getTaskId());
                            i.putExtra("task", "chathistory");
                            i.putExtra("message", taskbean);
                            i.putExtra("buddyuri", buddy_uri);
                            i.putExtra("chatHistoryBean", taskDetailsBean.get(0));
                            i.putExtra("catagory", taskDetailsBean.get(0).getCatagory());
//                        } else {
//                            SimpleDateFormat dateformat = new SimpleDateFormat(
//                                    "yyyy-MM-dd hh:mm:ss");
//                            String dateforrow = dateformat.format(new Date()
//                                    .getTime());
//                            i.putExtra("task","chat");
//                            i.putExtra("type","individual");
//                            i.putExtra("touser",buddy_uri);
//                            i.putExtra("message",taskbean);
//                            i.putExtra("touserid",String.valueOf(contactBean.getUserid()));
//                            Log.i("chat", "contact fragment  chatName-->" + dateforrow);
//
                        } else {
                            i.putExtra("message", taskbean);
                            i.putExtra("buddyuri", buddy_uri);
                        }
                        setResult(RESULT_OK, i);
                        finish();
                    }
                }

            }
        });
    }

//    public void PercentageWebService(TaskDetailsBean detailsBean,TaskDetailsBean oldbean) {
//        if (!detailsBean.getTaskDescription().equals(null)) {
////            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////            String dateforrow = dateFormat.format(new Date());
//
////            SimpleDateFormat dateFormatt = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
////            tasktime = dateFormatt.format(new Date());
////            tasktime = tasktime.split(" ")[1] + " " + tasktime.split(" ")[2];
//            Log.i("textfile", "1" + detailsBean.getMimeType());
//            Log.i("textfile", "2" + detailsBean.getTaskDescription());
//            Log.i("textfile", "3" + detailsBean.getTaskStatus());
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String dateTime = dateFormat.format(new Date());
//            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//            String dateforrow = dateFormat.format(new Date());
//
//            String dateforrow1 = dateFormat.format(new Date());
//
//            final TaskDetailsBean chatBean = new TaskDetailsBean();
//            chatBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
//            chatBean.setFromUserName(Appreference.loginuserdetails.getUsername());
//            chatBean.setSelect(false);
//            chatBean.setToUserName(toUserName);
//            chatBean.setToUserId(String.valueOf(toUserId));
//            if (isTaskName) {
//                Log.i("taskconversation", "istaskName percentagewebservice ");
//                if (!template && !note) {
//                    chatBean.setTaskName("New Task");
//                    Log.i("taskconversation", "headername  6 ");
//                } else if (note) {
//                    chatBean.setTaskName("New Note");
//                    Log.i("taskconversation", "headername  7 ");
//                } else if (chat) {
//                } else {
//                    chatBean.setTaskName("New Template");
//                    Log.i("taskconversation", "headername  8 ");
//                }
//                chatBean.setSubType("taskDescription");
//                chatBean.setTaskRequestType("taskDescription");
//                Log.i("TaskConversation", "setting task name for template " + chatBean.getTaskName());
//                Log.i("task", "setting task name");
//            } else {
//                Log.i("taskconversation", "istaskName percentagewebservice else ");
//                chatBean.setSubType(subType);
//                chatBean.setTaskRequestType(subType);
//                chatBean.setTaskName(taskName);
//                Log.e("task", "taskname **" + taskName);
//            }
//            chatBean.setTaskDescription(getMediaPath);
////            chatBean.setSignalid(Utility.getSessionID());
//            chatBean.setSignalid(sig_id);
//            chatBean.setTaskNo(task_No);
//            if ((!template && !note) || chat)
//                chatBean.setCatagory(category);
//            chatBean.setIssueId(issueId);
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
//            chatBean.setTaskId(webtaskId);
//            chatBean.setOwnerOfTask(ownerOfTask);
//            if (getMediaType != null && getMediaType.equalsIgnoreCase("textfile")) {
//                Log.e("Accept", "update11243 " + isTaskAccept);
//                chatBean.setLongmessage("0");
//            }
//            chatBean.setCustomTagVisible(true);
//            if (!getMediaType.equalsIgnoreCase("text")) {
//                chatBean.setShow_progress(0);
//            }
////            chatBean.setSubType(subType);
//            if (project) {
//                chatBean.setProjectId(projectId);
//                if (projectGroup_Mems != null) {
//                    chatBean.setGroupTaskMembers(projectGroup_Mems);
//                }
//            }
//            if ((subType != null && subType.equalsIgnoreCase("taskDescription")) && !chatBean.getTaskDescription().contains("Completed Percentage ")) {
//                chatBean.setSubType(subType);
//                chatBean.setTaskRequestType(subType);
//            } else {
//                if (!isTaskName)
//                    chatBean.setTaskRequestType("taskConversation");
//            }
//            if (isTaskAccept && !ownerOfTask.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
//                chatBean.setTaskStatus(taskStatus);
//                Log.e("Accept", "update10 " + isTaskAccept);
//                Log.i("Accept", "value 9" + chatBean.getTaskStatus());
//            } else {
//                if (getMediaPath.equalsIgnoreCase("Completed Percentage 100%") || getMediaPath.equalsIgnoreCase("Completed Percentage 100")) {
////                    chatBean.setTaskStatus("closed");
//                    taskStatus = "closed";
//                    Log.i("Accept", "value 119" + chatBean.getTaskStatus());
//                } else if (template) {
////                    chatBean.setTaskStatus("draft");
//                    if (note) {
//                        taskStatus = "note";
//                    } else if (chat) {
//                        taskStatus = "chat";
//                    } else {
//                        taskStatus = "draft";
//                    }
//                    chatBean.setCustomTagVisible(true);
//                } else if (taskType != null && getResources().getString(R.string.task_enable).equalsIgnoreCase("enable") && !taskType.equalsIgnoreCase("Group")) {
//                    Log.i("Accept", "value $$ ---> " + isTaskAccept);
//                    taskList_11 = new ArrayList<>();
//                    taskList_12 = new ArrayList<>();
//                    taskList_11 = VideoCallDataBase.getDB(context).getTaskHistory("select * from taskDetailsInfo where taskId='" + webtaskId + "' and taskDescription = 'This " + category + " is closed' order by id desc");
//                    if (taskList_11.size() > 0) {
//                        taskList_12 = VideoCallDataBase.getDB(context).getTaskHistory("select * from taskDetailsInfo where fromUserName='" + Appreference.loginuserdetails.getUsername() + "' and ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "' and taskId='" + webtaskId + "' and taskDescription like '" + "Completed Percentage %" + "' order by id desc");
//                        if (taskList_12.size() > 0) {
//                            String completed_value = taskList_12.get(0).getCompletedPercentage();
//                            if (completed_value.equalsIgnoreCase("100") || completed_value.equalsIgnoreCase("100%")) {
//                                taskStatus = "closed";
//                                Log.i("Accept", "value 127" + chatBean.getTaskStatus());
//                            } else {
//                                taskStatus = "inprogress";
//                                Log.i("Accept", "value 128" + chatBean.getTaskStatus());
//                            }
//                        }
//                    } else if (isTaskAccept) {
//                        taskStatus = "inprogress";
//                        Log.i("Accept", "value 129 " + chatBean.getTaskStatus());
//                    } else {
//                        taskStatus = "assigned";
//                        Log.i("Accept", "value 139 " + chatBean.getTaskStatus());
//                    }
//                }
//                chatBean.setTaskStatus(taskStatus);
//                if (chat) {
//                    chatBean.setTaskStatus("chat");
//                }
//                Log.i("Accept", "value 03 " + chatBean.getTaskStatus());
//                Log.i("Accept", "value $$ --->> " + isTaskAccept);
//            }
//
//
//            // send status 0 is send 1 is unsend
//            chatBean.setSendStatus("0");
//            chatBean.setMsg_status(0);
//            Log.i("task", "image sent " + getMediaType);
////            taskList.add(chatBean);
//            Log.i("task", "image sent " + getMediaType);
//            // taskList.add(chatBean);
//            chatBean.setWs_send("0");
//
////            refresh();
//
//
//            if (!getExt.equalsIgnoreCase("message")) {
//              /*  MediaListBean uIbean = new MediaListBean();
//                uIbean.setMediaType(mediaListBean.getMediaType());
//                uIbean.setMediaPath(mediaListBean.getMediaPath());
//                uIbean.setUser(Appreference.loginuserdetails.getUsername());
//                uIbean.setFlag("0");
//                uIbean.setTasktime(tasktime);
//                uIbean.setSignalid(chatBean.getSignalid());
//                Log.i("mp", "mpath" + strIPath);
//                mediaList.add(uIbean);*/
//                if (!isTaskName) {
//                    if (project) {
//                        VideoCallDataBase.getDB(context).update_Project_history(chatBean);
//                        Log.i("concept of project ", "value db updated in projectHistory " + chatBean.getTaskDescription());
//                    }
//                    if (VideoCallDataBase.getDB(context).insertORupdate_Task_history(chatBean)) {
//
//                        if (chatBean.isCustomTagVisible()) {
//                            taskList.add(chatBean);
//                        }
//                        Log.i("task", "msg Status " + chatBean.getMsg_status());
//                        refresh();
//                    }
//                }
//            }
//            if (isTaskName) {
//                Log.i("taskconversation", "istaskName percentagewebservice **  ");
//                if (!template && !note) {
//                    chatBean.setTaskName("New Task");
//                    Log.i("taskconversation", "headername  9 ");
//                } else if (note) {
//                    chatBean.setTaskName("New Note");
//                    Log.i("taskconversation", "headername  10 ");
//                } else if (chat) {
//                } else {
//                    chatBean.setTaskName("New Template");
//                    Log.i("taskconversation", "headername  11 ");
//                }
//                Log.i("task", "setting task name ");
//                ownerOfTask = Appreference.loginuserdetails.getUsername();
//                taskReceiver = toUserName;
//                taskIdWebservice(chatBean);
////                isTaskName = false;
//                Log.i("task", "isTask Name is false ");
//            } else {
//                try {
//                    JSONObject jsonObject = new JSONObject();
//
//                    JSONObject jsonObject1 = new JSONObject();
//                    jsonObject1.put("id", Integer.parseInt(detailsBean.getTaskId()));
//                    jsonObject.put("task", jsonObject1);
//                    JSONObject jsonObject2 = new JSONObject();
//                    jsonObject2.put("id", Appreference.loginuserdetails.getId());
//                    jsonObject.put("from", jsonObject2);
//                        JSONObject jsonObject3 = new JSONObject();
//                        jsonObject3.put("id", detailsBean.getToUserId());
//                    String sig_id = Utility.getSessionID();
//                    jsonObject.put("signalId", sig_id);
//                    jsonObject.put("parentId", getFileName());
//                    jsonObject.put("createdDate", dateforrow);
//                    jsonObject.put("requestType", "taskConversation");
//
//                    jsonObject.put("requestStatus", "approved");
//                    jsonObject.put("taskEndDateTime", "");
//                    jsonObject.put("taskStartDateTime", "");
//                    jsonObject.put("remainderDateTime", "");
//                    jsonObject.put("dateFrequency", "");
//
//                    jsonObject.put("timeFrequency", "");
////                    jsonObject.put("remainderQuotes", reminderquote);
//                    jsonObject.put("remark", "");
//
//
//                    JSONObject jsonObject5 = new JSONObject();
//                    jsonObject5.put("id", Appreference.loginuserdetails.getId());
//                    JSONObject jsonObject4 = new JSONObject();
//                    jsonObject4.put("user", jsonObject5);
//                    switch (detailsBean.getMimeType().toLowerCase().trim()) {
//                        case "image":
//                            jsonObject4.put("fileType", "image");
//                            jsonObject4.put("fileContent", encodeTobase64(BitmapFactory.decodeFile(detailsBean.getMimeType())));
//                            jsonObject4.put("taskFileExt", "jpg");
////                            Appreference.printLog("Encoded image is ", encodeTobase64(BitmapFactory.decodeFile(getMediaPath)), "DEBUG", null);
//                            break;
//                        case "video":
//                            jsonObject4.put("fileType", "video");
//                            Log.i("task", "Video uploaded" + detailsBean.getMimeType());
//                            jsonObject4.put("fileContent", encodeAudioVideoToBase64(detailsBean.getMimeType()));
//                            jsonObject4.put("taskFileExt", "mp4");
//                            break;
//                        case "audio":
//                            jsonObject4.put("fileType", "audio");
//                            jsonObject4.put("fileContent", encodeAudioVideoToBase64(detailsBean.getMimeType()));
//                            jsonObject4.put("taskFileExt", "mp3");
//                            break;
//                        case "document":
//                            jsonObject4.put("fileType", detailsBean.getMimeType());
//                            jsonObject4.put("fileContent", encodeFileToBase64Binary(detailsBean.getMimeType()));
//                            jsonObject4.put("taskFileExt", detailsBean.getMimeType().split(".")[1]);
//                            break;
//                        case "text":
//                            jsonObject4.put("fileType", "text");
//                            jsonObject4.put("description", detailsBean.getMimeType());
//                            break;
//                        case "map":
//                            jsonObject4.put("fileType", "map");
//                            jsonObject4.put("description", detailsBean.getMimeType());
//                            break;
//                        case "textfile":
//                            Log.i("textfile", "getExt ==  " + detailsBean.getMimeType().split(".")[1] + "   getMediaType  == " + detailsBean.getMimeType());
//                            jsonObject4.put("fileType", detailsBean.getMimeType());
//                            jsonObject4.put("taskFileExt", "txt");
//                            jsonObject4.put("fileContent", encodeFileToBase64Binary(detailsBean.getMimeType()));
//                            break;
//
//                    }
//                    JSONArray jsonArray = new JSONArray();
//                    jsonArray.put(0, jsonObject4);
//                    jsonObject.put("listTaskConversationFiles", jsonArray);
//                    if (jsonObject != null) {
//                        Log.i("Request", "Task date update for giver is " + jsonObject);
//                        Log.i("fileuplad", "mpath" + detailsBean.getMimeType());
//                        Log.i("TaskEntry", "taskConversationEntry 2");
//                        Appreference.jsonRequestSender.taskConversationEntry(EnumJsonWebservicename.taskConversationEntry, jsonObject, this, null, chatBean);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

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
}
