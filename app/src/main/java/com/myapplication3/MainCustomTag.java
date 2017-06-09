package com.myapplication3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.myapplication3.Bean.CustomBean;
import com.myapplication3.Bean.CustomTagBean;
import com.myapplication3.Bean.ListofFileds;
import com.myapplication3.Bean.ResultsetBean;
import com.myapplication3.Bean.TaskDetailsBean;
import com.myapplication3.DB.VideoCallDataBase;
import com.myapplication3.RandomNumber.Utility;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pjsip.pjsua2.Buddy;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.app.MyBuddy;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.Vector;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.WebServiceInterface;
import Services.ShowOrCancelProgress;

/**
 * Created by vignesh on 12/1/2016.
 */
public class MainCustomTag extends Activity implements DialogInterface.OnClickListener, WebServiceInterface {

    private static Context context;

    public LinearLayout fieldContainer = null, buttomlinear;

    public ArrayList<ListofFileds> profileFieldList;

    private Vector<CustomTagsBean> getFieldValuesList = new Vector<CustomTagsBean>();

    private HashMap<String, String> multimediaFieldValues = new HashMap<String, String>();

    private MediaPlayer profile_player;

    private MediaPlayer audio_player;

    private int position = 0;

    public String filedStrId;

    private String type = null;

    private String quotes = "\"";

    private String strIPath = null;

    private boolean isSelect;

    private Bitmap bitmap, img = null;

    private Handler handler = new Handler();

    private Button setting = null;

    // private Button btn_reset = null;
    private ShowOrCancelProgress progressListener;

    private HashMap<String, ListofFileds> fieldValuesMap = new HashMap<String, ListofFileds>();

    private int CAMERA = 32;

    private int VIDEO = 40;

//    private int AUDIO = 34;

    private SharedPreferences preferences;

    // private Button btn_im = null;

    // private Button btn_share = null;

    private boolean isProfileInServerSide;

    private HashMap<String, String> fileHmap = new HashMap<String, String>();


    // private Button save = null; // this button plus hide in this page,this
    // button create fragment xml

    // private Button reset = null;


    public View view;
    static String strTime = null;
    static String st_hour;
    static String strenddate;
    static String taskId;
    static String setId;
    ArrayList<ListofFileds> listofFiledses;
    ArrayList<String> listOfObservers;
    ArrayList<String> listOfUserNames = new ArrayList<String>();
    ArrayList<String> listOfUserUri = new ArrayList<String>();

    TextView send, back, NoData;
    ImageView forword, backword, added;
    Button deleted, print, saveBtn;
    ProgressDialog progress;
    ArrayList<CustomBean> rhsList = null;
    ArrayList<ListofFileds> lhsList = null;
    ArrayList<ListofFileds> lhsList1 = null;
    ArrayList<String> setIdlist = new ArrayList<>();
    ArrayList<TaskDetailsBean> taskDetailsBeanArrayList, beanList;
    ArrayList<ListofFileds> fields_list = new ArrayList<>();
    TaskDetailsBean taskDetailsBean;
    String check = "Second";
    int z = 0, a = 0;
    int timeLimit;
    //    static Dialog dialog;
    private boolean isgroup = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_custom_tag);
        beanList = new ArrayList<TaskDetailsBean>();
        profileFieldList = new ArrayList<>();
        fieldContainer = (LinearLayout) findViewById(R.id.fieldContainer);
        buttomlinear = (LinearLayout) findViewById(R.id.buttomlinear);
        taskDetailsBean = (TaskDetailsBean) getIntent().getExtras().getSerializable("TaskBean");

        send = (TextView) findViewById(R.id.send);
        NoData = (TextView) findViewById(R.id.NoData);
        back = (TextView) findViewById(R.id.back);
        forword = (ImageView) findViewById(R.id.forword);
        backword = (ImageView) findViewById(R.id.backword);
        added = (ImageView) findViewById(R.id.addnew);
        deleted = (Button) findViewById(R.id.deleted);
        print = (Button) findViewById(R.id.print);
        saveBtn = (Button) findViewById(R.id.save);

        if (taskDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
            isgroup = true;
        } else {
            isgroup = false;
        }

        progressListener = Appreference.main_Activity_context;
        context = this;
        listofFiledses = new ArrayList<>();
        listofFiledses.clear();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTextValue();

            }
        });


//        String query1 = "select * from taskDetailsInfo where taskId = " + taskId + " and subType = customeHeaderAttribute  and customSetId =" + taskDetailsBean.getCustomSetId() + " order by customTagId";


        added.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "CustomTagsActivity", Toast.LENGTH_SHORT).show();
                showprogressforpriority();
//                        Intent intent =new Intent(getApplicationContext(),CustomTagsActivity.class);
//                        startActivity(intent);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("", ""));
                Appreference.jsonRequestSender.getCustomTag(EnumJsonWebservicename.getCustomTag, nameValuePairs, MainCustomTag.this);
//                        Appreference.jsonRequestSender.getCustomTagValue(EnumJsonWebservicename.getCustomTagValue, nameValuePairs, NewTaskConversation.this);
            }
        });


        listofFiledses = (ArrayList<ListofFileds>) getIntent().getExtras().getSerializable("LeftList");
//        listOfObservers = (ArrayList<String>) getIntent().getExtras().getSerializable("listOfObservers");

        taskId = getIntent().getExtras().getString("Taskid");
        ArrayList<String> obs_rec = (ArrayList<String>) getIntent().getExtras().getSerializable("listOfObservers");

        HashMap<String, String> stringHashMap = new HashMap<>();
        for (String user_uri : obs_rec) {
            if (user_uri.contains("_") && !user_uri.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                stringHashMap.put(user_uri, user_uri);
            }
        }
        listOfObservers = new ArrayList<String>();
        listOfUserNames = new ArrayList<String>();
        Iterator iterator1 = stringHashMap.entrySet()
                .iterator();
        while (iterator1.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) iterator1.next();
            listOfObservers.add((String) mapEntry.getKey());
        }

        listOfUserUri = new ArrayList<String>();
        listOfUserUri = (ArrayList<String>) listOfObservers.clone();
        if (!listOfUserUri.contains(Appreference.loginuserdetails.getUsername())) {
            listOfUserUri.add(Appreference.loginuserdetails.getUsername());
        }

        for (String user_uri : listOfUserUri) {
            if (user_uri.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                listOfUserNames.add("Me");
            } else {
                String user_name = VideoCallDataBase.getDB(Appreference.mainContect).getName(user_uri);
                if (user_name != null) {
                    listOfUserNames.add(user_name);
                }
            }
        }
        profileFieldList = listofFiledses;

        String query = "select * from taskDetailsInfo where taskId = " + taskId + " and subType = 'customeAttribute' group by customSetId ";
        Log.i("Task", "ArrayList" + query);
        setIdlist = VideoCallDataBase.getDB(context).getSetid(query);
        Log.i("Task", "ArrayList" + setIdlist.size());

        try {
            if (setIdlist.size() > 0 && setIdlist != null && setIdlist.get(z) != null) {
                setId = setIdlist.get(z);
                if (setIdlist.size() > 0) {
                    forword.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (z == 0) {
            backword.setVisibility(View.INVISIBLE);
        } else {
            backword.setVisibility(View.VISIBLE);
        }

        forword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (z < setIdlist.size()) {
                        z++;
                        if (setIdlist.size() == z) {
                            forword.setVisibility(View.INVISIBLE);
                            backword.setVisibility(View.VISIBLE);
                        } else if (z == 0) {
                            forword.setVisibility(View.VISIBLE);
                            backword.setVisibility(View.INVISIBLE);
                        } else {
                            forword.setVisibility(View.VISIBLE);
                            backword.setVisibility(View.VISIBLE);

                        }

                        //                    String query = "select taskDesprition from taskDetailsInfo where taskId = " + taskId + " and customSetId = '" + setIdlist.get(z);
                        loadProfileFieldValues(setIdlist.get(z), "Second");
                        setId = setIdlist.get(z);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        backword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (z >= 0 && setIdlist != null) {
                        z--;

                        if (setIdlist.size() == z) {
                            forword.setVisibility(View.INVISIBLE);
                            backword.setVisibility(View.VISIBLE);
                        } else if (z == 0) {
                            forword.setVisibility(View.VISIBLE);
                            backword.setVisibility(View.INVISIBLE);
                        } else {
                            forword.setVisibility(View.VISIBLE);
                            backword.setVisibility(View.VISIBLE);
                        }


//                    String query = "select taskDesprition from taskDetailsInfo where taskId = " + taskId + " and customSetId = '" + setIdlist.get(z);


                        loadProfileFieldValues(setIdlist.get(z), "Second");
                        setId = setIdlist.get(z);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        deleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("json", "deletepart");
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("taskId", taskId));
                nameValuePairs.add(new BasicNameValuePair("setId", setId));
                showprogress();
                Appreference.jsonRequestSender.getDeleteCustomTag(EnumJsonWebservicename.deleteCustomMasterValue, nameValuePairs, MainCustomTag.this);


                for (int i = 0; i < fieldContainer.getChildCount(); i++) {
                    LinearLayout child_view = (LinearLayout) fieldContainer
                            .getChildAt(i);

                    ListofFileds bean = profileFieldList.get(i);


                    if (bean != null) {
                        if (bean.getDataType().equalsIgnoreCase("photo")
                                || bean.getDataType().equalsIgnoreCase("audio")
                                || bean.getDataType().equalsIgnoreCase("video") || bean.getDataType().equalsIgnoreCase("signature")) {
                            String type = bean.getDataType();
                            if (multimediaFieldValues.containsKey(bean.getClientId())) {

                                ListofFileds fieldTemplateBean = new ListofFileds();
                                fieldTemplateBean.setId(bean.getId());
                                fieldTemplateBean.setTask(bean.getName());
                                fieldTemplateBean.setDataType(bean.getDataType());
                                fieldTemplateBean.setName(multimediaFieldValues.get(bean.getClientId()));
                                fields_list.add(fieldTemplateBean);
                            }
                        } else if (bean.getDataType().equalsIgnoreCase("text") || bean.getDataType().equalsIgnoreCase("numeric")) {
                            EditText ed_fieldvalue = (EditText) child_view
                                    .findViewById(R.id.fieldvalue);
                            if (ed_fieldvalue.getText().toString().trim().length() != 0) {
                                ListofFileds fieldTemplateBean = new ListofFileds();
                                fieldTemplateBean.setId(bean.getId());

                                fieldTemplateBean.setTask(bean.getName());

                                fieldTemplateBean.setDataType(bean.getDataType());

                                if (ed_fieldvalue.getText().toString().trim()
                                        .length() > 0) {

                                    if (bean.getDataType().equalsIgnoreCase("numeric")) {
                                        fieldTemplateBean.setName(ed_fieldvalue
                                                .getText().toString().trim());
                                    } else {
                                        fieldTemplateBean.setName(ed_fieldvalue
                                                .getText().toString().trim());
                                    }
                                    fields_list.add(fieldTemplateBean);
                                }
                            }


                        } else if ((bean.getDataType().equalsIgnoreCase("datetime") || bean.getDataType().equalsIgnoreCase("date"))) {
                            String type = bean.getDataType();
                            Log.i("Update", "datatype" + bean.getDataType());
                            if (multimediaFieldValues.containsKey(bean.getClientId())) {

                                ListofFileds fieldTemplateBean = new ListofFileds();
                                fieldTemplateBean.setId(bean.getId());
                                fieldTemplateBean.setTask(bean.getName());
                                fieldTemplateBean.setDataType(bean.getDataType());
                                fieldTemplateBean.setName((multimediaFieldValues.get(bean.getClientId())));
                                fields_list.add(fieldTemplateBean);
                            }

                        }

                    }
                }
                Log.i("json", "deletepart  fields_list.size()  " + fields_list.size());

                for (int z = 0; z < fields_list.size(); z++) {

                    ListofFileds listofFileds = fields_list.get(z);
                    TaskDetailsBean chatBean = new TaskDetailsBean();
//            setId = Integer.valueOf(listofFileds.getCreatedBy()).toString();
                    Log.i("CustomTag", "setid" + setId);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateTime = dateFormat.format(new Date());
                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String dateforrow = dateFormat.format(new Date());

                    Log.i("task", "task1 login user " + String.valueOf(Appreference.loginuserdetails.getId()));
                    Log.i("task", "task1 toid " + taskDetailsBean.getToUserId());
                    Log.i("task", "task1 taskId " + taskId);
                    Log.i("task", "task1 userName " + taskDetailsBean.getFromUserName());
                    Log.i("task", "task1 toUserName " + taskDetailsBean.getToUserName());
                    Log.i("task", "task1 Appreference userName " + Appreference.loginuserdetails.getUsername());

                    chatBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
                    chatBean.setToUserId(String.valueOf(taskDetailsBean.getToUserId()));
                    chatBean.setTaskNo(taskDetailsBean.getTaskNo());
                    chatBean.setFromUserName(Appreference.loginuserdetails.getUsername());
                    chatBean.setToUserName(taskDetailsBean.getToUserName());
                    chatBean.setTaskReceiver(taskDetailsBean.getTaskReceiver());
                    /*if (reminderquote != null && !reminderquote.equalsIgnoreCase(null) && !reminderquote.equalsIgnoreCase("")) {
                        chatBean.setReminderQuote(reminderquote);
                    }*/
                    if ((listofFileds.getDataType().equalsIgnoreCase("datetime") || listofFileds.getDataType().equalsIgnoreCase("date")) || listofFileds.getDataType().equalsIgnoreCase("numeric")) {
                        chatBean.setTaskDescription(listofFileds.getTask() + "  :  " + listofFileds.getName());
                        chatBean.setMimeType("text");
                    } else if (listofFileds.getDataType().equalsIgnoreCase("photo") || listofFileds.getDataType().equalsIgnoreCase("image") || listofFileds.getDataType().equalsIgnoreCase("signature")) {
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("image");
                    } else if (listofFileds.getDataType().equalsIgnoreCase("video")) {
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("video");
                    } else if (listofFileds.getDataType().equalsIgnoreCase("audio")) {
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("audio");
                    } else {
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("text");
                    }

                    chatBean.setTaskName(taskDetailsBean.getTaskName());
                    chatBean.setTasktime(taskDetailsBean.getTasktime());
                    chatBean.setTaskUTCTime(dateforrow);
                    chatBean.setDateTime(dateTime);
                    chatBean.setTaskUTCDateTime(dateforrow);
                    chatBean.setPlannedStartDateTime(null);
                    chatBean.setUtcPlannedStartDateTime(Appreference.customLocalDateToUTC(null));
                    chatBean.setPlannedEndDateTime(null);
                    chatBean.setUtcplannedEndDateTime(Appreference.customLocalDateToUTC(null));
//                    chatBean.setRemainderFrequency("");
//                    chatBean.setUtcPemainderFrequency(Appreference.customLocalDateToUTC(reminderdate));
//                        Log.i("task", "utc for start date in request" + Appreference.utcToLocalTime(startdate));
//                    chatBean.setIsRemainderRequired("1");
                    chatBean.setDateFrequency("");
//                    chatBean.setTimeFrequency(reminderfreq);
                    chatBean.setSignalid(Utility.getSessionID());

                    chatBean.setParentId(getFileName());
                    chatBean.setTaskPriority("Medium");
                    chatBean.setCompletedPercentage("0");
                    chatBean.setOwnerOfTask(taskDetailsBean.getOwnerOfTask());
                    Log.i("task", "webtaskId " + taskId);
                    chatBean.setTaskId(taskId);
                    chatBean.setRequestStatus("requested");

                    chatBean.setSendStatus("0");   // send status 0 is send 1 is unsend
                    chatBean.setTaskType(taskDetailsBean.getTaskType());
                    chatBean.setMsg_status(0);
                    chatBean.setTaskStatus(taskDetailsBean.getTaskStatus());
                    chatBean.setRequestStatus("requested");
                    chatBean.setTaskRequestType("customeAttributeRequest");
                    chatBean.setSubType("customeAttribute");
                    chatBean.setTaskTagName(listofFileds.getTask());
                    chatBean.setShow_progress(1);
                    chatBean.setCustomTagId(Integer.valueOf(listofFileds.getId()));
                    chatBean.setCustomSetId(Integer.valueOf(setId));


                   /* if (!isTaskName) {

                        TaskDetailsBean taskDetailsBean = chatBean;
                        taskList.add(taskDetailsBean);
                        sortTaskMessage();
                        refresh();
                    }*/

                    if ((listofFileds.getDataType().equalsIgnoreCase("datetime") || listofFileds.getDataType().equalsIgnoreCase("date"))) {
                        // In future change the mimetype value.
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("text");
                    } else if (listofFileds.getDataType().equalsIgnoreCase("photo") || listofFileds.getDataType().equalsIgnoreCase("image") || listofFileds.getDataType().equalsIgnoreCase("signature")) {
                        chatBean.setTaskDescription(listofFileds.getIsInputRequired());
                        chatBean.setMimeType("image");
                    } else if (listofFileds.getDataType().equalsIgnoreCase("video")) {
                        chatBean.setTaskDescription(listofFileds.getIsInputRequired());
                        chatBean.setMimeType("video");
                    } else if (listofFileds.getDataType().equalsIgnoreCase("audio")) {
                        chatBean.setTaskDescription(listofFileds.getIsInputRequired());
                        chatBean.setMimeType("audio");
                    } else {
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("text");
                    }


//            VideoCallDataBase.getDB(context).insertORupdate_Task_history(chatBean);
                    VideoCallDataBase.getDB(context).deleteCustomTagEntry(taskId, setId);
                    chatBean.setTaskDescription("Message has been Removed");
                    chatBean.setTaskTagName("");
                    chatBean.setMimeType("text");
                    chatBean.setCustomSetId(0);
//                    VideoCallDataBase.getDB(context).UpdateOrInsert(chatBean);
                    Log.i("date", "date request bean" + chatBean);
                    Log.i("task", "Desc1 " + chatBean.getTaskDescription());

                    chatBean.setCustomSetId(Integer.valueOf(setId));
                    final String xml = composeCustomTagsDeleteXML(chatBean);

                    //                sendInstantMessage(xml);

                    Log.d("TaskObserver", "TaskObserver list size is == " + listOfObservers.size());
                    if (listOfObservers != null && listOfObservers.size() > 0) {

                        timeLimit = timeLimit + 2000;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sendMultiInstantMessage(xml, listOfObservers);
                            }
                        }, timeLimit);

                    }
                    if ((listofFileds.getDataType().equalsIgnoreCase("datetime") || listofFileds.getDataType().equalsIgnoreCase("date")) || listofFileds.getDataType().equalsIgnoreCase("numeric")) {
                        chatBean.setTaskDescription(listofFileds.getTask() + "  :  " + listofFileds.getName());
                        chatBean.setMimeType("text");
                    } else if (listofFileds.getDataType().equalsIgnoreCase("photo") || listofFileds.getDataType().equalsIgnoreCase("image") || listofFileds.getDataType().equalsIgnoreCase("signature")) {
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("image");
                    } else if (listofFileds.getDataType().equalsIgnoreCase("video")) {
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("video");
                    } else if (listofFileds.getDataType().equalsIgnoreCase("audio")) {
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("audio");
                    } else {
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("text");
                    }

                    break;

                }

            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showprogress();
                a = 1;
                Webservices();

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showprogress();
                a = 0;
                Webservices();

            }
        });




   /*     CustomTagsBean customTagsBean = new CustomTagsBean();
        customTagsBean.setFieldId("0");
        customTagsBean.setFieldName("Name");
        customTagsBean.setFieldType("text");


        CustomTagsBean customTagsBean1 = new CustomTagsBean();
        customTagsBean1.setFieldId("1");
        customTagsBean1.setFieldName("Date");
        customTagsBean1.setFieldType("time");

        CustomTagsBean customTagsBean2 = new CustomTagsBean();
        customTagsBean2.setFieldId("2");
        customTagsBean2.setFieldName("Picture");
        customTagsBean2.setFieldType("photo");

        CustomTagsBean customTagsBean3 = new CustomTagsBean();
        customTagsBean3.setFieldId("2");
        customTagsBean3.setFieldName("2016-11-30 11:08:32");
        customTagsBean3.setFieldType("date");*/


/*        profileFieldList.add(customTagsBean1);
        profileFieldList.add(customTagsBean2);
        profileFieldList.add(customTagsBean3);*/

        try {
            if (setIdlist.size() == 0)
                loadProfileFieldValues("null", "Second");
            else
                loadProfileFieldValues(setIdlist.get(z), "Second");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Webservices() {
        try {

            for (int i = 0; i < fieldContainer.getChildCount(); i++) {
                LinearLayout child_view = (LinearLayout) fieldContainer
                        .getChildAt(i);

                ListofFileds bean = profileFieldList.get(i);


                if (bean != null) {
                    if (bean.getDataType().equalsIgnoreCase("photo")
                            || bean.getDataType().equalsIgnoreCase("audio")
                            || bean.getDataType().equalsIgnoreCase("video") || bean.getDataType().equalsIgnoreCase("signature")) {
                        String type = bean.getDataType();
                        if (multimediaFieldValues.containsKey(bean.getClientId())) {

                            ListofFileds fieldTemplateBean = new ListofFileds();
                            fieldTemplateBean.setId(bean.getId());
                            fieldTemplateBean.setTask(bean.getName());
                            fieldTemplateBean.setDataType(bean.getDataType());
                            fieldTemplateBean.setName(multimediaFieldValues.get(bean.getClientId()));
                            fieldTemplateBean.setCreatedBy(setId);
                            if (!check.equalsIgnoreCase("First"))
                                fields_list.add(fieldTemplateBean);
                        }
                    } else if (bean.getDataType().equalsIgnoreCase("text") || bean.getDataType().equalsIgnoreCase("numeric")) {
                        EditText ed_fieldvalue = (EditText) child_view
                                .findViewById(R.id.fieldvalue);
                        if (ed_fieldvalue.getText().toString().trim().length() != 0) {
                            ListofFileds fieldTemplateBean = new ListofFileds();
                            fieldTemplateBean.setId(bean.getId());

                            fieldTemplateBean.setTask(bean.getName());

                            fieldTemplateBean.setDataType(bean.getDataType());

                            if (ed_fieldvalue.getText().toString().trim()
                                    .length() > 0) {

                                if (bean.getDataType().equalsIgnoreCase("numeric")) {
                                    fieldTemplateBean.setName(ed_fieldvalue
                                            .getText().toString().trim());
                                } else {
                                    fieldTemplateBean.setName(ed_fieldvalue
                                            .getText().toString().trim());
                                }
                                fieldTemplateBean.setCreatedBy(setId);
                                if (!check.equalsIgnoreCase("First"))
                                    fields_list.add(fieldTemplateBean);
                            }
                        }


                    } else if ((bean.getDataType().equalsIgnoreCase("datetime") || bean.getDataType().equalsIgnoreCase("date"))) {
                        String type = bean.getDataType();
                        if (multimediaFieldValues.containsKey(bean.getClientId())) {

                            ListofFileds fieldTemplateBean = new ListofFileds();
                            fieldTemplateBean.setId(bean.getId());
                            fieldTemplateBean.setTask(bean.getName());
                            fieldTemplateBean.setDataType(bean.getDataType());
                            fieldTemplateBean.setName((multimediaFieldValues.get(bean.getClientId())));
                            fieldTemplateBean.setCreatedBy(setId);
                            if (!check.equalsIgnoreCase("First"))
                                fields_list.add(fieldTemplateBean);
                        }

                    }

                }
            }

            Log.i("CustomTag", "fieldslist" + fields_list.size());
            JSONObject[] filedlist = new JSONObject[fields_list.size()];
            int j = 0;
            for (int k = 0; k < fields_list.size(); k++) {
                Log.i("CustomTag", "fieldslist" + k);

                ListofFileds listofFileds = fields_list.get(k);

                Log.d("TagsValuesInWebService",
                        "Field ID : " + listofFileds.getId()
                                + ", Field Name : "
                                + listofFileds.getName() + ", Type :"
                                + listofFileds.getDataType() + " list size is :  " + fields_list.size());
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("id", listofFileds.getId());

                filedlist[k] = new JSONObject();


                filedlist[k].put("tags", jsonObject1);
                Log.i("CustomTag", "name" + listofFileds.getName());
                if (listofFileds.getDataType().equalsIgnoreCase("photo") || listofFileds.getDataType().equalsIgnoreCase("image") || listofFileds.getDataType().equalsIgnoreCase("signature")) {
                    filedlist[k].put("fileContent", encodeTobase64(BitmapFactory.decodeFile((listofFileds.getName()))));
                    filedlist[k].put("fileExt", "png");

                } else if (listofFileds.getDataType().equalsIgnoreCase("video")) {
                    filedlist[k].put("fileContent", encodeAudioVideoToBase64(listofFileds.getName()));
                    filedlist[k].put("fileExt", "mp4");

                } else if (listofFileds.getDataType().equalsIgnoreCase("audio")) {
                    filedlist[k].put("fileContent", encodeAudioVideoToBase64(listofFileds.getName()));
                    filedlist[k].put("fileExt", "mp3");
                } else {
                    filedlist[k].put("description", listofFileds.getName());
                }
                j++;
            }


            JSONArray listpostfiles_object = new JSONArray();
            for (int i1 = 0; i1 < filedlist.length; i1++) {
                listpostfiles_object.put(filedlist[i1]);
            }


            JSONObject jsonObject = new JSONObject();

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("id", taskId);
//                    jsonObject1.put("id", "14815");

            jsonObject.put("task", jsonObject1);
            jsonObject.put("setId", setId);

            jsonObject.put("valueList", listpostfiles_object);
            Log.i("json", "listsize" + fields_list.size());

            Appreference.jsonRequestSender.saveCustomHeaderTagValue(EnumJsonWebservicename.editCustomMasterValue, jsonObject, MainCustomTag.this, fields_list);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    public void updateCustomTagFields(List<ResultsetBean> resultsetBeen) {

        /*for(int t = 0; t <fields_list.size();t++){
            ListofFileds listofFileds = fields_list.get(t);
            if (listofFileds.getDataType().equalsIgnoreCase("photo") || listofFileds.getDataType().equalsIgnoreCase("image")) {
                if(listofFileds.getId().equalsIgnoreCase())
                    listofFileds.setIsInputRequired();
                }

        }*/


        for (int z = 0; z < fields_list.size(); z++) {

            ListofFileds listofFileds = fields_list.get(z);

            for (int j = 0; j < resultsetBeen.size(); j++) {
                ResultsetBean resultsetBean1 = new ResultsetBean();
                resultsetBean1 = resultsetBeen.get(j);
                if (listofFileds.getId().equalsIgnoreCase(resultsetBean1.getTagId())) {
                    if (listofFileds.getDataType().equalsIgnoreCase("photo") ||
                            listofFileds.getDataType().equalsIgnoreCase("image") ||
                            listofFileds.getDataType().equalsIgnoreCase("signature") ||
                            listofFileds.getDataType().equalsIgnoreCase("video") ||
                            listofFileds.getDataType().equalsIgnoreCase("audio")) {
                        listofFileds.setIsInputRequired(resultsetBean1.getValues());
                    } else {
                        listofFileds.setName(resultsetBean1.getValues());
                    }
                }
            }
            TaskDetailsBean chatBean = new TaskDetailsBean();
//            setId = Integer.valueOf(listofFileds.getCreatedBy()).toString();
            Log.i("CustomTag", "setid" + setId);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = dateFormat.format(new Date());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateforrow = dateFormat.format(new Date());

            Log.i("task", "task1 login user " + String.valueOf(Appreference.loginuserdetails.getId()));
            Log.i("task", "task1 toid " + taskDetailsBean.getToUserId());
            Log.i("task", "task1 taskId " + taskId);
            Log.i("task", "task1 userName " + taskDetailsBean.getFromUserName());
            Log.i("task", "task1 toUserName " + taskDetailsBean.getToUserName());
            Log.i("task", "task1 Appreference userName " + Appreference.loginuserdetails.getUsername());

            chatBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
            chatBean.setToUserId(String.valueOf(taskDetailsBean.getToUserId()));
            chatBean.setTaskNo(taskDetailsBean.getTaskNo());
            chatBean.setFromUserName(Appreference.loginuserdetails.getUsername());
            chatBean.setToUserName(taskDetailsBean.getToUserName());
            chatBean.setTaskReceiver(taskDetailsBean.getTaskReceiver());
                    /*if (reminderquote != null && !reminderquote.equalsIgnoreCase(null) && !reminderquote.equalsIgnoreCase("")) {
                        chatBean.setReminderQuote(reminderquote);
                    }*/
            if ((listofFileds.getDataType().equalsIgnoreCase("datetime") || listofFileds.getDataType().equalsIgnoreCase("date")) || listofFileds.getDataType().equalsIgnoreCase("numeric")) {
                chatBean.setTaskDescription(listofFileds.getTask() + "  :  " + listofFileds.getName());
                chatBean.setMimeType("text");
            } else if (listofFileds.getDataType().equalsIgnoreCase("photo") || listofFileds.getDataType().equalsIgnoreCase("image") || listofFileds.getDataType().equalsIgnoreCase("signature")) {
                chatBean.setTaskDescription(listofFileds.getName());
                chatBean.setMimeType("image");
            } else if (listofFileds.getDataType().equalsIgnoreCase("video")) {
                chatBean.setTaskDescription(listofFileds.getName());
                chatBean.setMimeType("video");
            } else if (listofFileds.getDataType().equalsIgnoreCase("audio")) {
                chatBean.setTaskDescription(listofFileds.getName());
                chatBean.setMimeType("audio");
            } else {
                chatBean.setTaskDescription(listofFileds.getName());
                chatBean.setMimeType("text");
            }

            chatBean.setTaskName(taskDetailsBean.getTaskName());
            chatBean.setTasktime(taskDetailsBean.getTasktime());
            chatBean.setTaskUTCTime(dateforrow);
            chatBean.setDateTime(dateTime);
            chatBean.setTaskUTCDateTime(dateforrow);
            chatBean.setPlannedStartDateTime(null);
            chatBean.setUtcPlannedStartDateTime(Appreference.customLocalDateToUTC(null));
            chatBean.setPlannedEndDateTime(null);
            chatBean.setUtcplannedEndDateTime(Appreference.customLocalDateToUTC(null));
//                    chatBean.setRemainderFrequency("");
//                    chatBean.setUtcPemainderFrequency(Appreference.customLocalDateToUTC(reminderdate));
//                        Log.i("task", "utc for start date in request" + Appreference.utcToLocalTime(startdate));
//                    chatBean.setIsRemainderRequired("1");
            chatBean.setDateFrequency("");
//                    chatBean.setTimeFrequency(reminderfreq);
            chatBean.setSignalid(Utility.getSessionID());

            chatBean.setParentId(getFileName());
            chatBean.setTaskPriority("Medium");
            chatBean.setCompletedPercentage("0");
            chatBean.setOwnerOfTask(taskDetailsBean.getOwnerOfTask());
            Log.i("task", "webtaskId " + taskId);
            chatBean.setTaskId(taskId);
            chatBean.setRequestStatus("requested");

            chatBean.setSendStatus("0");   // send status 0 is send 1 is unsend
            chatBean.setTaskType(taskDetailsBean.getTaskType());
            chatBean.setMsg_status(0);
            chatBean.setTaskStatus(taskDetailsBean.getTaskStatus());
            chatBean.setRequestStatus("requested");
            chatBean.setTaskRequestType("customeAttributeRequest");
            chatBean.setSubType("customeAttribute");
            chatBean.setTaskTagName(listofFileds.getTask());
            chatBean.setShow_progress(1);
            chatBean.setCustomTagId(Integer.valueOf(listofFileds.getId()));
            chatBean.setCustomSetId(Integer.valueOf(setId));


                   /* if (!isTaskName) {

                        TaskDetailsBean taskDetailsBean = chatBean;
                        taskList.add(taskDetailsBean);
                        sortTaskMessage();
                        refresh();
                    }*/

            if ((listofFileds.getDataType().equalsIgnoreCase("datetime") || listofFileds.getDataType().equalsIgnoreCase("date"))) {
                // In future change the mimetype value.
                chatBean.setTaskDescription(listofFileds.getName());
                chatBean.setMimeType("text");
            } else if (listofFileds.getDataType().equalsIgnoreCase("photo") || listofFileds.getDataType().equalsIgnoreCase("image") || listofFileds.getDataType().equalsIgnoreCase("signature")) {
                chatBean.setTaskDescription(listofFileds.getIsInputRequired());
                chatBean.setMimeType("image");
            } else if (listofFileds.getDataType().equalsIgnoreCase("video")) {
                chatBean.setTaskDescription(listofFileds.getIsInputRequired());
                chatBean.setMimeType("video");
            } else if (listofFileds.getDataType().equalsIgnoreCase("audio")) {
                chatBean.setTaskDescription(listofFileds.getIsInputRequired());
                chatBean.setMimeType("audio");
            } else {
                chatBean.setTaskDescription(listofFileds.getName());
                chatBean.setMimeType("text");
            }
            if (a == 1)
                chatBean.setCustomTagVisible(true);
            else
                chatBean.setCustomTagVisible(false);

            if (taskDetailsBean.getProjectId() != null) {
                chatBean.setProjectId(taskDetailsBean.getProjectId());
            }
            Log.i("date", "date taskDetailsBean.getProjectId() --> " + chatBean.getProjectId());
//            VideoCallDataBase.getDB(context).insertORupdate_Task_history(chatBean);
            if (z == 1)
                VideoCallDataBase.getDB(context).UpdateOrInsert(fields_list, setId, chatBean);

            Log.i("date", "date request bean" + chatBean);
            Log.i("task", "Desc1 " + chatBean.getTaskDescription());
            if (listofFileds.getDataType().equalsIgnoreCase("datetime") && listofFileds.getTask().equalsIgnoreCase("Conference Start Time")) {
                Log.i("Schedule", " 0 Time " + chatBean.getTaskDescription());
                chatBean.setTaskDescription(Appreference.customLocalDateToUTC(chatBean.getTaskDescription()));
                Log.i("Schedule", " 1 Time " + chatBean.getTaskDescription());

            }
            chatBean.setCatagory("Task");

            if (chatBean.getTaskDescription() != null) {
                chatBean.setTaskTagValue(chatBean.getTaskDescription());
            }
            final String xml = composeChatXML(chatBean);

            //                sendInstantMessage(xml);

            Log.d("TaskObserver", "TaskObserver list size is == " + listOfObservers.size());
            if (listOfObservers != null && listOfObservers.size() > 0) {

                timeLimit = timeLimit + 2000;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendMultiInstantMessage(xml, listOfObservers);
                    }
                }, timeLimit);

            }
            if ((listofFileds.getDataType().equalsIgnoreCase("datetime") || listofFileds.getDataType().equalsIgnoreCase("date")) || listofFileds.getDataType().equalsIgnoreCase("numeric")) {
                chatBean.setTaskDescription(listofFileds.getName());
                chatBean.setMimeType("text");
            } else if (listofFileds.getDataType().equalsIgnoreCase("photo") || listofFileds.getDataType().equalsIgnoreCase("image") || listofFileds.getDataType().equalsIgnoreCase("signature")) {
                chatBean.setTaskDescription(listofFileds.getName());
                chatBean.setMimeType("image");
            } else if (listofFileds.getDataType().equalsIgnoreCase("video")) {
                chatBean.setTaskDescription(listofFileds.getName());
                chatBean.setMimeType("video");
            } else if (listofFileds.getDataType().equalsIgnoreCase("audio")) {
                chatBean.setTaskDescription(listofFileds.getName());
                chatBean.setMimeType("audio");
            } else {
                chatBean.setTaskDescription(listofFileds.getName());
                chatBean.setMimeType("text");
            }
            beanList.add(chatBean);

            if (listofFileds.getDataType().equalsIgnoreCase("datetime") && listofFileds.getTask().equalsIgnoreCase("Conference Start Time")) {
                Log.i("Schedule", " 2 Time " + chatBean.getTaskDescription());
//                        chatBean.setTaskDescription(UTCToLocalTime(chatBean.getTaskDescription()));
                Log.i("Schedule", " 3 Time " + chatBean.getTaskDescription());
                progressListener.StartAlarmManager(chatBean);
            }
        }
        if (!check.equalsIgnoreCase("First")) {
            String query = "select * from taskDetailsInfo where taskId = " + taskId + " and subType = 'customeAttribute' group by customSetId ";
            setIdlist = VideoCallDataBase.getDB(context).getSetid(query);
            Log.i("Task", "ArrayList" + setIdlist.size());
            check = "Second";
            loadProfileFieldValues(setId, "Second");
        }
    }

    public void inflateFieldsExist(int mode, final ListofFileds bean, final TaskDetailsBean taskDetailsBean) {
        try {
            View view = getLayoutInflater().inflate(
                    R.layout.custom_tag_adapter, fieldContainer, false);
            view.setTag(bean.getClientId());
            final TextView fieldName = (TextView) view.findViewById(R.id.fieldname);
            if (bean.getName() != null) {
                fieldName.setText(bean.getName());
                Log.i("maincustom", "fieldname " + bean.getName());
            }

            if (bean.getDataType().equalsIgnoreCase("photo") || bean.getDataType().equalsIgnoreCase("image") || bean.getDataType().equalsIgnoreCase("signature")) {
                ImageView options = (ImageView) view.findViewById(R.id.imageButton);
                final TextView fileName = (TextView) view.findViewById(R.id.fieldText);
                fileName.setVisibility(View.VISIBLE);
                options.setVisibility(View.VISIBLE);
                options.setTag(fieldContainer.getChildCount());
                options.setContentDescription(bean.getDataType());
                if (taskDetailsBean != null) {
                    if (taskDetailsBean.getTaskDescription().contains("High Message")) {
//                        String name = taskDetailsBean.getTaskDescription().split("/")[5];
                        String name = taskDetailsBean.getTaskDescription().split("High Message/")[1];
                        multimediaFieldValues.put(bean.getId(), taskDetailsBean.getTaskDescription());
                        fileName.setText(name);
                    } else {
                        fileName.setText(taskDetailsBean.getTaskDescription());
                    }
                }
                fieldValuesMap.put(bean.getName(), bean);
                options.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        if (bean.getDataType().equalsIgnoreCase("signature")) {
                            position = Integer.parseInt(bean.getId());
                            Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                            startActivityForResult(i, 423);
                        } else {

                            int pos = (Integer) v.getTag();
                            Log.d("Profile", "On click of option menu");
                            showMultimediaOptions(pos, bean.getClientId(), v
                                    .getContentDescription().toString());
                        }

                    }
                });

                fileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ImageName = fileName.getText().toString();
                        Log.i("Task", "imagename" + ImageName);
                        File file = null;
                        if (bean.getDataType().equalsIgnoreCase("photo") || bean.getDataType().equalsIgnoreCase("signature")) {
                            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                        }
                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                        if (file.exists()) {
                            Log.d("Profile", "file path contains if" + file.toString());
                            Intent intent = new Intent(context, FullScreenImage.class);
                            intent.putExtra("image", file.toString());
                            context.startActivity(intent);
                        } else {
                            Log.d("Profile", "file path contains else");
                            File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + ImageName);
                            if (file1.exists()) {
                                Log.d("Profile", "file path contains else if" + file1.toString());
                                Intent intent = new Intent(context, FullScreenImage.class);
                                intent.putExtra("image", file1.toString());
                                context.startActivity(intent);
                            }

                        }

                    }
                });

                options.setVisibility(View.VISIBLE);
            }
//            else if(bean.getDataType().equalsIgnoreCase("datetime") && bean.getName().equalsIgnoreCase("Conference Start Time")) {
//                final TextView date = (TextView) view.findViewById(R.id.fieldText);
//                date.setVisibility(View.VISIBLE);
//                date.setText(bean.getName());
//                date.setTag(Integer.parseInt(bean.getClientId()));
//                date.setContentDescription(bean.getDataType());
//                fieldValuesMap.put(bean.getName(), bean);
//                date.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int integer = (Integer) date.getTag();
//                        position = integer;
////                        position = Integer.parseInt(bean.getId());
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        String currentDateandTime = sdf.format(new Date());
//                        Log.d("TagValues","currentDateandTime    :   "+currentDateandTime);
//                        Log.d("TagValues","currentDateandTime    :   "+bean.getId());
//                        Log.d("TagValues","currentDateandTime    :   "+String.valueOf(fieldContainer.getChildCount()));
//
//                       /* for(int j=1;j<=listofFiledses.size();j++){
//                            if(bean.getId().equals(listofFiledses.get(j).getId()));{
//                                showCustomDatePicker(currentDateandTime, String.valueOf(j));
//                                break;
//                            }
//
//                        }*/
//                        showCustomDatePicker(currentDateandTime, bean.getId());
//
////                        multimediaFieldValues.put(bean.getId(), currentDateandTime);
////                        date.setText(strenddate);
//                    }
//                });
//            }
            else if ((bean.getDataType().equalsIgnoreCase("datetime") || bean.getDataType().equalsIgnoreCase("date")) && bean.getIsInputRequired().equalsIgnoreCase("no")) {
                final Button add = (Button) view.findViewById(R.id.fieldButton);
                final TextView fileName = (TextView) view.findViewById(R.id.fieldText);
                fileName.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                if (taskDetailsBean != null) {
                    fileName.setText(taskDetailsBean.getTaskDescription());
                    multimediaFieldValues.put(bean.getId(), taskDetailsBean.getTaskDescription());
                }

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime);
                        fileName.setText(currentDateandTime);
                        add.setVisibility(View.INVISIBLE);
                        multimediaFieldValues.put(bean.getClientId(), currentDateandTime);

                    }
                });
            } else if (bean.getDataType().equalsIgnoreCase("numeric")) {
                final EditText fieldValue = (EditText) view
                        .findViewById(R.id.fieldvalue);
                fieldValue.setVisibility(View.VISIBLE);
                fieldValue.setTag(bean.getDataType());
                fieldValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                fieldValue.setContentDescription(bean.getClientId());
                fieldValuesMap.put(bean.getName(), bean);
                fieldValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                if (taskDetailsBean != null) {
                    fieldValue.setText(taskDetailsBean.getTaskDescription());
                    multimediaFieldValues.put(bean.getId(), taskDetailsBean.getTaskDescription());
                }
                if (bean.getDataType() != null) {
                /*    if (bean.getName().equalsIgnoreCase(" in Km")) {
                        fieldValue.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // TODO Auto-generated method stub
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                                    changeType(fieldValue);
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });
                    }*/
                }
            } else if (bean.getDataType().equalsIgnoreCase("text")) {

                final EditText fieldValue = (EditText) view
                        .findViewById(R.id.fieldvalue);
                fieldValue.setVisibility(View.VISIBLE);
                fieldValue.setTag(bean.getDataType());
                fieldValue.setContentDescription(bean.getClientId());
                fieldValuesMap.put(bean.getName(), bean);
                fieldValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                if (taskDetailsBean != null) {
                    fieldValue.setText(taskDetailsBean.getTaskDescription());
                }
                if (bean.getDataType() != null) {
                    /*if (bean.getName().equalsIgnoreCase("Birth Day")) {
                        fieldValue.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // TODO Auto-generated method stub
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                                    changeType(fieldValue);
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });

                    } else*/
                    if (bean.getDataType().equalsIgnoreCase("number"))
                        fieldValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                    else if (bean.getDataType().equalsIgnoreCase("email"))
                        fieldValue
                                .setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                }

            } else if ((bean.getDataType().equalsIgnoreCase("datetime") || bean.getDataType().equalsIgnoreCase("date")) && bean.getIsInputRequired().equalsIgnoreCase("yes")) {

                final TextView date = (TextView) view.findViewById(R.id.fieldText);
                date.setVisibility(View.VISIBLE);
                date.setText(bean.getName());
                date.setTag(fieldContainer.getChildCount());
                date.setContentDescription(bean.getDataType());
                if (taskDetailsBean != null) {
                    date.setText(taskDetailsBean.getTaskDescription());
                    multimediaFieldValues.put(bean.getId(), taskDetailsBean.getTaskDescription());
                }
                fieldValuesMap.put(bean.getName(), bean);
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int integer = (Integer) v.getTag();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime);
                        showCustomDatePicker(taskDetailsBean.getTaskDescription(), bean.getClientId());

                        multimediaFieldValues.put(bean.getClientId(), currentDateandTime);
//                        date.setText(strenddate);
                    }
                });
            } else if (bean.getDataType().equalsIgnoreCase("audio")) {

                ImageView options = (ImageView) view.findViewById(R.id.imageButton);
                final TextView fileName = (TextView) view.findViewById(R.id.fieldText);
                fileName.setVisibility(View.VISIBLE);
                options.setBackground(getResources().getDrawable(R.drawable.ic_audio_file_100));
                options.setVisibility(View.VISIBLE);
                if (taskDetailsBean != null) {
                    if (taskDetailsBean.getTaskDescription().contains("High Message")) {
//                        String name = taskDetailsBean.getTaskDescription().split("/")[5];
                        String name = taskDetailsBean.getTaskDescription().split("High Message/")[1];
                        multimediaFieldValues.put(bean.getId(), taskDetailsBean.getTaskDescription());
                        fileName.setText(name);
                    } else {
                        fileName.setText(taskDetailsBean.getTaskDescription());
                    }
                }
                options.setTag(fieldContainer.getChildCount());
                options.setContentDescription(bean.getDataType());
                fieldValuesMap.put(bean.getName(), bean);
                options.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        int pos = (Integer) v.getTag();
                        Log.d("Profile", "On click of option menu");
                        try {
                            position = Integer.parseInt(bean.getId());
                            Intent i = new Intent(MainCustomTag.this, AudioRecorder.class);
                            i.putExtra("task", "audio");
                            startActivityForResult(i, 333);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

                fileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ImageName = fileName.getText().toString();
                        Log.i("Task", "imagename" + ImageName);
                        File file = null;
                        Intent intent = new Intent(context, Audioplayer.class);
                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                        if (file.exists()) {
                            intent.putExtra("audio", file.toString());
                            context.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + ImageName);
                            if (file1.exists()) {
                                intent.putExtra("audio", file1.toString());
                                context.startActivity(intent);
                            }

                        }
                    }
                });
                options.setVisibility(View.VISIBLE);
            } else if (bean.getDataType().equalsIgnoreCase("video")) {

                ImageView options = (ImageView) view.findViewById(R.id.imageButton);
                final TextView fileName = (TextView) view.findViewById(R.id.fieldText);
                fileName.setVisibility(View.VISIBLE);
                options.setVisibility(View.VISIBLE);
                if (taskDetailsBean != null) {
                    if (taskDetailsBean.getTaskDescription().contains("High Message")) {
//                        String name = taskDetailsBean.getTaskDescription().split("/")[5];
                        String name = taskDetailsBean.getTaskDescription().split("High Message/")[1];
                        multimediaFieldValues.put(bean.getId(), taskDetailsBean.getTaskDescription());
                        fileName.setText(name);
                    } else {
                        fileName.setText(taskDetailsBean.getTaskDescription());
                    }
                }
                options.setTag(fieldContainer.getChildCount());
                options.setContentDescription(bean.getDataType());
                fieldValuesMap.put(bean.getName(), bean);
                options.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        int pos = (Integer) v.getTag();
                        Log.d("Profile", "On click of option menu");
                        showMultimediaOptions(pos, bean.getId(), v
                                .getContentDescription().toString());
                    }
                });

                fileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ImageName = fileName.getText().toString();
                        Log.i("Task", "imagename" + ImageName);
                        File file = null;

                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                        if (file.exists()) {
                            Intent intent = new Intent(context, VideoPlayer.class);
                            intent.putExtra("video", file.getAbsolutePath());
                            context.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + ImageName);
                            if (file1.exists()) {
                                Intent intent = new Intent(context, VideoPlayer.class);
                                intent.putExtra("video", file1.getAbsolutePath());
                                context.startActivity(intent);
                            }
                        }
                    }
                });


                options.setVisibility(View.VISIBLE);
            } else if (bean.getDataType().equalsIgnoreCase("user")) {
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listOfUserNames);
                Spinner spinner = (Spinner) view
                        .findViewById(R.id.spinnerValue);
                spinner.setVisibility(View.VISIBLE);
//                final EditText fieldValue = (EditText) view
//                        .findViewById(R.id.fieldvalue);
//                fieldValue.setVisibility(View.VISIBLE);
                spinner.setTag(bean.getDataType());
                spinner.setContentDescription(bean.getId());
                fieldValuesMap.put(bean.getName(), bean);
                spinner.setAdapter(dataAdapter);
                for (int i = 0; i < listOfUserNames.size(); i++) {
                    if (taskDetailsBean.getTaskDescription().equalsIgnoreCase(listOfUserNames.get(i))) {
                        spinner.setSelection(i);
                    }
                }
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selected_name = parent.getItemAtPosition(position).toString();
                        if (multimediaFieldValues.containsKey(bean.getId())) {
                            multimediaFieldValues.remove(bean.getId());
                        }
                        String user_uri = listOfUserUri.get(position);
                        multimediaFieldValues.put(bean.getId(), user_uri);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            fieldContainer.addView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showprogressforpriority() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("login123", "inside showProgressDialog");
                    progress = new ProgressDialog(MainCustomTag.this);
                    progress.setCancelable(false);
                    progress.setMessage("loading…");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setProgress(0);
                    progress.setMax(100);
                    progress.show();
                } catch (Exception e) {
//                        SingleInstance.printLog(null, e.getMessage(), null, e);
                }
            }


        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && fieldContainer != null) {

            if (requestCode == 786) {


                ArrayList<ListofFileds> beanArrayList = (ArrayList<ListofFileds>) data.getExtras().getSerializable("Values");
                Log.i("Task", "Result " + beanArrayList.size());
//                Intent intent = new Intent();
//                intent.putExtra("Values", beanArrayList);
//                setResult(RESULT_OK, intent);
//                finish();


                for (int z = 0; z < beanArrayList.size(); z++) {

                    ListofFileds listofFileds = beanArrayList.get(z);
                    TaskDetailsBean chatBean = new TaskDetailsBean();

                    fields_list.add(listofFileds);
                    try {
                        setId = Integer.valueOf(listofFileds.getCreatedBy()).toString();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    Log.i("CustomTag", "setid" + setId);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateTime = dateFormat.format(new Date());
                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String dateforrow = dateFormat.format(new Date());

                    Log.i("task", "task1 login user " + String.valueOf(Appreference.loginuserdetails.getId()));
                    Log.i("task", "task1 toid " + taskDetailsBean.getToUserId());
                    Log.i("task", "task1 taskId " + taskId);
                    Log.i("task", "task1 userName " + taskDetailsBean.getFromUserName());
                    Log.i("task", "task1 toUserName " + taskDetailsBean.getToUserName());
                    Log.i("task", "task1 Appreference userName " + Appreference.loginuserdetails.getUsername());

                    chatBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
                    chatBean.setToUserId(String.valueOf(taskDetailsBean.getToUserId()));
                    chatBean.setTaskNo(taskDetailsBean.getTaskNo());
                    chatBean.setFromUserName(Appreference.loginuserdetails.getUsername());
                    chatBean.setToUserName(taskDetailsBean.getToUserName());
                    chatBean.setTaskReceiver(taskDetailsBean.getTaskReceiver());
                    /*if (reminderquote != null && !reminderquote.equalsIgnoreCase(null) && !reminderquote.equalsIgnoreCase("")) {
                        chatBean.setReminderQuote(reminderquote);
                    }*/
                    if ((listofFileds.getDataType().equalsIgnoreCase("datetime") || listofFileds.getDataType().equalsIgnoreCase("date")) || listofFileds.getDataType().equalsIgnoreCase("numeric")) {
                        chatBean.setTaskDescription(listofFileds.getTask() + "  :  " + listofFileds.getName());
                        chatBean.setMimeType("text");
                    } else if (listofFileds.getDataType().equalsIgnoreCase("photo") || listofFileds.getDataType().equalsIgnoreCase("image") || listofFileds.getDataType().equalsIgnoreCase("signature")) {
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("image");
                    } else if (listofFileds.getDataType().equalsIgnoreCase("video")) {
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("video");
                    } else if (listofFileds.getDataType().equalsIgnoreCase("audio")) {
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("audio");
                    } else {
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("text");
                    }

                    chatBean.setTaskName(taskDetailsBean.getTaskName());
                    chatBean.setTasktime(taskDetailsBean.getTasktime());
                    chatBean.setTaskUTCTime(dateforrow);
                    chatBean.setDateTime(dateTime);
                    chatBean.setTaskUTCDateTime(dateforrow);
                    chatBean.setPlannedStartDateTime(null);
                    chatBean.setUtcPlannedStartDateTime(Appreference.customLocalDateToUTC(null));
                    chatBean.setPlannedEndDateTime(null);
                    chatBean.setUtcplannedEndDateTime(Appreference.customLocalDateToUTC(null));
//                    chatBean.setRemainderFrequency("");
//                    chatBean.setUtcPemainderFrequency(Appreference.customLocalDateToUTC(reminderdate));
//                        Log.i("task", "utc for start date in request" + Appreference.utcToLocalTime(startdate));
//                    chatBean.setIsRemainderRequired("1");
                    chatBean.setDateFrequency("");
//                    chatBean.setTimeFrequency(reminderfreq);
                    chatBean.setSignalid(Utility.getSessionID());

                    chatBean.setParentId(getFileName());
                    chatBean.setTaskPriority("Medium");
                    chatBean.setCompletedPercentage("0");
                    chatBean.setOwnerOfTask(taskDetailsBean.getOwnerOfTask());
                    Log.i("task", "webtaskId " + taskId);
                    chatBean.setTaskId(taskId);
                    chatBean.setRequestStatus("requested");

                    chatBean.setSendStatus("0");   // send status 0 is send 1 is unsend
                    chatBean.setTaskType(taskDetailsBean.getTaskType());
                    chatBean.setMsg_status(0);
                    chatBean.setTaskStatus(taskDetailsBean.getTaskStatus());
                    chatBean.setRequestStatus("requested");
                    chatBean.setTaskRequestType("customeAttributeRequest");
                    chatBean.setSubType("customeAttribute");
                    chatBean.setTaskTagName(listofFileds.getTask());
                    chatBean.setShow_progress(1);
                    chatBean.setCustomTagId(Integer.valueOf(listofFileds.getId()));
                    chatBean.setCustomSetId(Integer.valueOf(listofFileds.getCreatedBy()));
                    chatBean.setCustomTagVisible(false);


                   /* if (!isTaskName) {

                        TaskDetailsBean taskDetailsBean = chatBean;
                        taskList.add(taskDetailsBean);
                        sortTaskMessage();
                        refresh();
                    }*/

                    if ((listofFileds.getDataType().equalsIgnoreCase("datetime") || listofFileds.getDataType().equalsIgnoreCase("date"))) {
                        // In future change the mimetype value.
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("text");
                    } else if (listofFileds.getDataType().equalsIgnoreCase("photo") || listofFileds.getDataType().equalsIgnoreCase("image") || listofFileds.getDataType().equalsIgnoreCase("signature")) {
                        chatBean.setTaskDescription(listofFileds.getIsInputRequired());
                        chatBean.setMimeType("image");
                    } else if (listofFileds.getDataType().equalsIgnoreCase("video")) {
                        chatBean.setTaskDescription(listofFileds.getIsInputRequired());
                        chatBean.setMimeType("video");
                    } else if (listofFileds.getDataType().equalsIgnoreCase("audio")) {
                        chatBean.setTaskDescription(listofFileds.getIsInputRequired());
                        chatBean.setMimeType("audio");
                    } else {
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("text");
                    }
                    if (taskDetailsBean.getProjectId() != null) {
                        chatBean.setProjectId(taskDetailsBean.getProjectId());
                    }
                    Log.i("date", "date taskDetailsBean.getProjectId() " + chatBean.getProjectId());
                    Log.i("date", "date request bean" + chatBean);
                    Log.i("task", "Desc1 " + chatBean.getTaskDescription());
//                    if(listofFileds.getDataType() != null && listofFileds.getDataType().equalsIgnoreCase("user")) {
//                        chatBean.setTaskTagValue(chatBean.getTaskDescription());
//                    }

                    if (listofFileds.getDataType().equalsIgnoreCase("datetime") && listofFileds.getTask().equalsIgnoreCase("Conference Start Time")) {
                        Log.i("Schedule", " 0 Time " + chatBean.getTaskDescription());
                        chatBean.setTaskDescription(Appreference.customLocalDateToUTC(chatBean.getTaskDescription()));
                        Log.i("Schedule", " 1 Time " + chatBean.getTaskDescription());

                    }

                    if (chatBean.getTaskDescription() != null) {
                        chatBean.setTaskTagValue(chatBean.getTaskDescription());
                    }

                    final String xml = composeChatXML(chatBean);

                    //                sendInstantMessage(xml);

                    Log.d("TaskObserver", "TaskObserver list size is == " + listOfObservers.size());
                    if (listOfObservers != null && listOfObservers.size() > 0) {

                        timeLimit = timeLimit + 2000;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sendMultiInstantMessage(xml, listOfObservers);
                            }
                        }, timeLimit);

                    }
                    if ((listofFileds.getDataType().equalsIgnoreCase("datetime") || listofFileds.getDataType().equalsIgnoreCase("date")) || listofFileds.getDataType().equalsIgnoreCase("numeric")) {
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("text");
                    } else if (listofFileds.getDataType().equalsIgnoreCase("photo") || listofFileds.getDataType().equalsIgnoreCase("image") || listofFileds.getDataType().equalsIgnoreCase("signature")) {
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("image");
                    } else if (listofFileds.getDataType().equalsIgnoreCase("video")) {
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("video");
                    } else if (listofFileds.getDataType().equalsIgnoreCase("audio")) {
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("audio");
                    } else if (listofFileds.getDataType().equalsIgnoreCase("confStartTime")) {
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("text");
//                        progressListener.StartAlarmManager(chatBean);
                    } else {
                        chatBean.setTaskDescription(listofFileds.getName());
                        chatBean.setMimeType("text");
                    }
                    if (listofFileds.getDataType().equalsIgnoreCase("datetime") && listofFileds.getTask().equalsIgnoreCase("Conference Start Time")) {
                        Log.i("Schedule", " 2 Time " + chatBean.getTaskDescription());
//                        chatBean.setTaskDescription(UTCToLocalTime(chatBean.getTaskDescription()));
                        Log.i("Schedule", " 3 Time " + chatBean.getTaskDescription());
                        progressListener.StartAlarmManager(chatBean);
                    }
                    multimediaFieldValues.put(listofFileds.getClientId(), listofFileds.getName());
                    VideoCallDataBase.getDB(context).insertORupdate_Task_history(chatBean);
                    VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(chatBean);
                }
                String query = "select * from taskDetailsInfo where taskId = " + taskId + " and subType = 'customeAttribute' group by customSetId ";
                setIdlist = VideoCallDataBase.getDB(context).getSetid(query);
                Log.i("Task", "ArrayList" + setIdlist.size());
                check = "First";
                loadProfileFieldValues(setId, check);

            } else {


                LinearLayout parent_view1 = (LinearLayout) fieldContainer
                        .getChildAt(position - 1);

                final TextView value_img;

                value_img = (TextView) parent_view1
                        .findViewById(R.id.fieldText);

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
                                        Log.d("filePath  1 ", strIPath);
                                        strIPath = path;
                                        Log.d("filePath  2 ", strIPath);
                                    }
                                    if (bitmap != null) {
                                        bitmap = Bitmap.createScaledBitmap(
                                                bitmap, 200, 150, false);
                                    }

                                    multimediaFieldValues.put(String.valueOf(position), strIPath);
//                                    String fileName = strIPath.split("/")[5];
                                    String fileName = strIPath.split("High Message/")[1];
                                    Log.d("fileName  1 ", "strIPath   " + fileName);
                                    value_img.setVisibility(View.VISIBLE);
                                    value_img.setText(fileName);
//                                refresh();

                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                } else if (requestCode == 111) {
                    strIPath = data.getStringExtra("filePath");
                    Log.i("Avideo", "New activity ************* : " + strIPath);
                    File new_file = new File(strIPath);
                    if (new_file.exists()) {
                        Log.i("mp", "mpath" + strIPath);
                        multimediaFieldValues.put(String.valueOf(position), strIPath);
//                        String fileName = strIPath.split("/")[5];
                        String fileName = strIPath.split("High Message/")[1];
                        Log.d("fileName  1 ", "strIPath   " + fileName);

                        value_img.setVisibility(View.VISIBLE);
                        value_img.setText(fileName);
                    }
                } else if (requestCode == 333) {
                    if (data != null) {
                        Log.d("filePath", data.getStringExtra("taskFileExt"));
                        strIPath = data.getStringExtra("taskFileExt");
                        Log.i("task", "extension");

                        multimediaFieldValues.put(String.valueOf(position), strIPath);
//                        String fileName = strIPath.split("/")[5];
                        String fileName = strIPath.split("High Message/")[1];
                        Log.d("fileName  1 ", "strIPath   " + fileName);

                        value_img.setVisibility(View.VISIBLE);
                        value_img.setText(fileName);
                    }

                } else if (requestCode == 33) {
                    if (data != null) {
                        try {
                            Log.i("AAA", "New activity 33*************");
                            Uri selectedImageUri = data.getData();
                            strIPath = getRealPathFromURI(selectedImageUri);
                            final String path = Environment.getExternalStorageDirectory()
                                    + "/High Message/" + getFileName() + ".mp4";

                            Log.i("AAA", "New activity " + strIPath);
                            strIPath = path;
                            Log.i("AAA", "New activity " + strIPath);

                            Log.i("mp", "mpath" + strIPath);

                            try {
                                FileInputStream fin = (FileInputStream) getContentResolver().openInputStream(selectedImageUri);
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
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (requestCode == 423) {
                    strIPath = data.getStringExtra("path");
                    Log.i("Task", "path" + strIPath);
                    File new_file = new File(strIPath);
                    if (new_file.exists()) {

                        Log.i("AAAA", "onactivity result $$$$$$$$$$$$$$$" + strIPath);
                        multimediaFieldValues.put(String.valueOf(position), strIPath);

                    }
                    value_img.setVisibility(View.VISIBLE);
//                    String fileName = strIPath.split("/")[5];
                    String fileName = strIPath.split("High Message/")[1];
                    value_img.setText(fileName);
                    Log.i("task", "extension");

//                    fileWebService("image");
                } else if (requestCode == 31) {
                    if (data != null) {

                        Uri selectedImageUri = data.getData();
                        Log.d("filePath  1 ", selectedImageUri.toString());
                        final int takeFlags = data.getFlags()
                                & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        getContentResolver().takePersistableUriPermission(
                                selectedImageUri, takeFlags);
                        strIPath = Environment.getExternalStorageDirectory()
                                + "/High Message/"
                                + getFileName() + ".jpg";
                        Log.d("filePath  1 ", "strIPath   " + strIPath);

                        File selected_file = new File(strIPath);

                        Log.d("filePath  1 ", "selected_file" +
                                "   " + selected_file.toString());

                        int length = (int) selected_file.length() / 1048576;
                        Log.d("task", "........ size is------------->" + strIPath);
                        Log.d("task", "........ size is------------->" + selected_file);
                        Log.d("task", "........ size is------------->" + length);
                        Log.d("task", "........ size is------------->" + selectedImageUri);
                        Log.d("task", "........ size is------------->" + length);
                        if (length <= 2) {
                            new bitmaploader().execute(selectedImageUri);
                            Log.d("task", "........ size is------------->" + selectedImageUri);
                        } else {
//                        showToast("Kindly Select someother image,this image is too large");
                            Toast.makeText(getApplicationContext(), "Kindly Select someother image,this image is too large", Toast.LENGTH_SHORT).show();
                        }

                        multimediaFieldValues.put(String.valueOf(position), strIPath);

//                        String fileName = strIPath.split("/")[5];
                        String fileName = strIPath.split("High Message/")[1];
                        Log.d("fileName  1 ", "strIPath   " + fileName);

                        value_img.setVisibility(View.VISIBLE);
                        value_img.setText(fileName);

                    }

                } else if (requestCode == 32) {
                    if (data != null) {
                        Log.i("AAA", "New activity 32*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + getFileName() + ".mp4";

                        strIPath = path;
                        try {
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
                            Log.i("AAA", "New activity " + strIPath);
                            Log.i("task", "video loaded");

                            multimediaFieldValues.put(String.valueOf(position), strIPath);
//                            String fileName = strIPath.split("/")[5];
                            String fileName = strIPath.split("High Message/")[1];
                            Log.d("fileName  1 ", "strIPath   " + fileName);
//                        medialistadapter.notifyDataSetChanged();
                            // Log.i("task","extension"+strIPath.split(".")[strIPath.split(".").length-1]);
//                        PercentageWebService("video", strIPath, "mp4");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        videofileWebService();
                    }
                } else if (requestCode == 132) {
                    File new_file = new File(strIPath);
                    if (new_file.exists()) {
//                       Log.i("task","extension"+strIPath.split(".")[strIPath.split(".").length-1]);
                        multimediaFieldValues.put(String.valueOf(position), strIPath);
//                        String fileName = strIPath.split("/")[5];
                        String fileName = strIPath.split("High Message/")[1];
                        value_img.setVisibility(View.VISIBLE);
                        value_img.setText(fileName);
                        Log.d("fileName  1 ", "strIPath   " + fileName);
//                        fileWebService("image");
                    }
                }
            }
        }

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

    public Bitmap convertpathToBitmap(String strIPath) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile((compressImage(strIPath)), bmOptions);

        return bitmap;

    }

    public String compressImage(String imageUri) {

        String filePath = imageUri;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = imageUri;
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }


    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
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


    public void cancelDialog() {
        try {
            if (progress != null && progress.isShowing()) {
                Log.i("register", "--progress bar end-----");
                progress.dismiss();
                Appreference.isRequested_date = false;
                progress = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    @Override
    public void ResponceMethod(final Object object) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    cancelDialog();
                    Log.i("task response", "NewTaskConversation ResponceMethod");
                    CommunicationBean bean = (CommunicationBean) object;
                    String values = bean.getEmail();
                    JsonElement jelement = new JsonParser().parse(values);


                    Gson gson = new Gson();

                    if (values != null && values.contains("isInputRequired")) {

//                        Log.i("rsponse", "3 inside the conflict response"+lhsList1.size());
                        if (rhsList == null) {
                            rhsList = new ArrayList<CustomBean>();
                        }
                        if (lhsList == null) {
                            lhsList = new ArrayList<ListofFileds>();
                        }
                        if (values.contains("master")) {
                            Type collectionType = new TypeToken<List<CustomBean>>() {
                            }.getType();
                            List<CustomBean> lcs = new Gson().fromJson(values, collectionType);
                            Log.i("CustomTag", "Value--->" + lcs.size());
                            for (int i = 0; i < lcs.size(); i++) {
                                CustomBean customBean = lcs.get(i);
                                ListofFileds listofFileds = customBean.getMaster();
                                rhsList.add(customBean);
                                Log.i("CustomTag", "Value123" + "\n" + customBean.getId() + "\n" + listofFileds.getName() + "\n" + customBean.getValue());
                            }
                        } else {
                            Type collectionType = new TypeToken<List<ListofFileds>>() {
                            }.getType();
                            List<ListofFileds> lcs = new Gson().fromJson(values, collectionType);
                            Log.i("CustomTag", "Value--->" + lcs.size());
                            for (int i = 0; i < lcs.size(); i++) {
                                ListofFileds listUserGroupObject = lcs.get(i);
                                listUserGroupObject.setClientId(String.valueOf(i + 1));
                                lhsList.add(listUserGroupObject);
                                Log.i("CustomTag", "Value123" + "\n" + listUserGroupObject.getId() + "\n" + listUserGroupObject.getName() + "\n" + listUserGroupObject.getIsInputRequired());
//                                    VideoCallDataBase.getDB(mainContext).insertContact_history(listUserGroupObject, username);

                            }

                            if (lhsList != null) {
                                cancelDialog();
                                Intent intent = new Intent(MainCustomTag.this, CustomTagsActivity.class);
                                intent.putExtra("listOfObservers", listOfUserUri);
                                intent.putExtra("LeftList", lhsList);
                                intent.putExtra("Taskid", taskId);
                                intent.putExtra("isgroup", isgroup);
                                startActivityForResult(intent, 786);
                                lhsList.clear();
                            }
                        }
                    } else if (values != null && values.contains("result_text")) {

                        ArrayList<ListofFileds> listValues = bean.getFiledsArrayList();
                        if (lhsList1 == null) {
                            lhsList1 = new ArrayList<ListofFileds>();
                            lhsList1 = (ArrayList<ListofFileds>) bean.getFiledsArrayList().clone();
                        }

                        //JsonArray jsonArray=new JsonArray();
                        //JsonElement jelement = new JsonParser().parse(str);
                        Log.i("rsponse", "1 inside the conflict response" + listValues.size());
                        Log.i("Response conflictTask", "response " + values);
                        Log.i("rsponse", "2 inside the conflict response" + lhsList1.size());

//                        Log.i("rsponse", "4 inside the conflict response"+lhsList1.size());
                        if (values.contains("updated successfully")) {
                            Type collectionType = new TypeToken<List<ResultsetBean>>() {
                            }.getType();
                            List<ResultsetBean> lcs = new Gson().fromJson(values, collectionType);
                            Log.i("CustomTag", "updated" + lcs.size());
                            taskDetailsBean.setSubType("customeAttribute");
                            taskDetailsBean.setTaskRequestType("customeAttributeRequest");
                            updateCustomTagFields(lcs);

                        } else {

//                        String deleteQuery = "Delete * from taskDetailsInfo where taskId = "+taskId +" and customSetId = "+setId +"";
                            JsonObject jobject = jelement.getAsJsonObject();
                            String result = jobject.get("result_text").toString();

                            Log.i("CustomTag", "deleted11" + result);
                            if (result.contains("deleted successfully")) {
//                            VideoCallDataBase.getDB(context).deleteCustomTagEntry(taskId, setId);
                                cancelDialog();
                                Log.i("CustomTag", "deleted");
                            } else if (result.contains("updated successfully")) {
                                Type collectionType = new TypeToken<List<ResultsetBean>>() {
                                }.getType();
                                List<ResultsetBean> lcs = new Gson().fromJson(values, collectionType);
                                Log.i("CustomTag", "updated" + result);
                                taskDetailsBean.setSubType("customeAttribute");
                                taskDetailsBean.setTaskRequestType("customeAttributeRequest");
                                updateCustomTagFields(lcs);
//                            VideoCallDataBase.getDB(context).UpdateOrInsert(bean.getFiledsArrayList(), setId,taskDetailsBean);
                            }
                        }
                        Log.i("rsponse", "3 inside the conflict response" + beanList.size());
                        if (progress != null && progress.isShowing()) {
                            Log.i("register", "--progress bar end-----");
                            progress.dismiss();
                            progress = null;
                        }
//                        progress.cancel();
                        Intent intent = new Intent();
                        intent.putExtra("values", beanList);
                        setResult(RESULT_OK, intent);
                        finish();

                    }
                    //if(jelement.getAsJsonArray().size()>0)
                    //{
                    //jsonArray=jelement.getAsJsonArray();
                    /*JSONArray jsonArray = new JSONArray(values);
                    if (jsonArray.length() > 0) {
                        // jsonArray=jelement.getAsJsonArray();
                        Log.i("response array", "response size" + jsonArray.length());

                        for (int i = 0; i < jsonArray.length(); i++) {

                            //JSONObject jsonObject1= (JSONObject) jsonArray.getJSONObject(i);
                            Log.i("response ", "objects  " + jsonArray.get(i));
                            CustomTagBean conflictCheck = gson.fromJson(jsonArray.getString(i), CustomTagBean.class);
                            if(!listValues.get(i).getDataType().equalsIgnoreCase("Photo")){
                                listValues.get(i).setName(conflictCheck.getValue());
                            }
                            else {
                                listValues.get(i).setIsInputRequired(conflictCheck.getValue());
                            }

                        }
                    }*/


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void ErrorMethod(Object object) {

        try {
            cancelDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class bitmaploader extends AsyncTask<Uri, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            try {
                super.onPostExecute(result);
                Log.d("image", "came to post execute for image");

                Bitmap img = null;
                if (strIPath != null) {
                    img = convertpathToBitmap(strIPath);
                    Log.i("task", "extension" + strIPath);
//                    String pathExtn = strIPath.split("/")[5];
                    String pathExtn = strIPath.split("High Message/")[1];
                    Log.i("task", "spilting" + pathExtn);
                    pathExtn = pathExtn.substring(pathExtn.length() - 3);
                    Log.i("task", "root---->" + pathExtn);
//                        Log.i("task", "extension" + strIPath.split(".")[strIPath.split(".").length - 1]);
//                    PercentageWebService("image", strIPath, pathExtn);
                }
                if (img != null) {

                   /* if (progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }*/
                    Log.d("OnActivity", "_____On Activity Called______");
                } else {
                    strIPath = null;
                }
            } catch (Exception e) {
                Log.e("profile", "====> " + e.getMessage());
               /* if (AppReference.isWriteInFile)
                    AppReference.logger.error(e.getMessage(), e);*/
              /*  else
                    e.printStackTrace();*/
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            try {
                super.onPreExecute();
                ProgressDialog dialog = new ProgressDialog(context);
//                callDisp.showprogress(dialog, context);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.e("profile", "====> " + e.getMessage());
              /*  if (AppReference.isWriteInFile)
                    AppReference.logger.error(e.getMessage(), e);
                else
                    e.printStackTrace();*/
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
                /*if (AppReference.isWriteInFile)
                    AppReference.logger.error(e.getMessage(), e);
                else
                    e.printStackTrace();*/
            }

            return null;
        }

    }

    public void loadProfileFieldValues(String value, String value1) {
        try {
          /*  if (profileFieldList.size() > 0) {
//                if (fieldContainer != null)
                fieldContainer.removeAllViews();
                int fied = 0;
                for (ListofFileds fieldtemplate : profileFieldList) {

                    inflateFields(fied, fieldtemplate);
                    Log.d("My Profile",
                            "Field ID : " + fieldtemplate.getId()
                                    + ", Field Name : "
                                    + fieldtemplate.getName() + ", Type :"
                                    + fieldtemplate.getDataType() + " required  "+ fieldtemplate.getIsInputRequired());
                    fied++;
                }
            } else {


            }*/

            if (value1 != null && !value1.equalsIgnoreCase("First")) {
                Log.i("CustomTag", "list Clear" + value1);
                fields_list.clear();
            }
            if (profileFieldList.size() > 0) {
//                if (fieldContainer != null)
                fieldContainer.removeAllViews();
                int fied = 0;
                if (setIdlist.size() == 0) {
                    taskDetailsBeanArrayList = new ArrayList<>();
                } else {
                    if (setIdlist != null && setIdlist.get(0) != null) {
                        String query1 = "select * from taskDetailsInfo where taskId = " + taskId + " and subType = 'customeAttribute'  and customSetId =" + value;
                        Log.d("My Profile", "query " + query1);
                        taskDetailsBeanArrayList = VideoCallDataBase.getDB(context).getTaskHistory(query1);
                        Log.d("My Profile", "query " + taskDetailsBeanArrayList.size());

                    }
                }

                if (taskDetailsBeanArrayList == null || taskDetailsBeanArrayList.size() == 0) {
                    Log.d("My Profile", "query if ");
                    buttomlinear.setVisibility(View.GONE);
                    fieldContainer.setVisibility(View.GONE);
                    NoData.setVisibility(View.VISIBLE);

                   /* for (ListofFileds fieldtemplate : profileFieldList) {
                        if (fieldtemplate.getName() == null) {
                            fieldtemplate.setName(fieldtemplate.getTagName());
                        }

                        inflateFields(fied, fieldtemplate);
                        Log.d("My Profile",
                                "Field ID ggfhgf : " + fieldtemplate.getId()
                                        + ", Field Name : "
                                        + fieldtemplate.getName() + ", Field TagName : "
                                        + fieldtemplate.getTagName() + ", Type :"
                                        + fieldtemplate.getDataType() + " required  " + fieldtemplate.getIsInputRequired());
                        fied++;
                    }*/
                } else {

                    buttomlinear.setVisibility(View.VISIBLE);
                    fieldContainer.setVisibility(View.VISIBLE);
                    NoData.setVisibility(View.GONE);
                    Log.d("My Profile", "query elsed ");
                    Log.d("My Profile", "query elsed " + profileFieldList.size());
                    int j = 0;
                    for (int i = 0; i < profileFieldList.size(); i++) {
                        ListofFileds listofFileds = profileFieldList.get(i);
                        int total = taskDetailsBeanArrayList.size();
                        if (j < total) {
//
//  for (int j = 0; j < profileFieldList.size(); j++) {
                            TaskDetailsBean taskDetailsBean1 = taskDetailsBeanArrayList.get(j);


                            if (listofFileds.getId().equalsIgnoreCase(String.valueOf(taskDetailsBean1.getCustomTagId()))) {


                                if (listofFileds.getName() == null) {
                                    listofFileds.setName(listofFileds.getTagName());
                                }

                                Log.d("My Profile",
                                        "Field ID : " + listofFileds.getId()
                                                + ", Field Name : "
                                                + listofFileds.getName() + ", Field TagName : "
                                                + listofFileds.getTagName() + ", Type :"
                                                + listofFileds.getDataType() + " Task Description " + taskDetailsBean1.getTaskDescription() + " tag id " + taskDetailsBean1.getCustomTagId() + " required  cxfgfghg" + listofFileds.getIsInputRequired());

                                if (taskDetailsBean1 != null) {
                                    inflateFieldsExist(i, listofFileds, taskDetailsBean1);
//                                    fields_list.add(listofFileds);
                                    multimediaFieldValues.put(String.valueOf(i), taskDetailsBean1.getTaskDescription());
                                }
                                j++;

                            } else {
                                inflateFieldsExist(i, listofFileds, new TaskDetailsBean());
                            }
                        } else {
                            inflateFieldsExist(i, listofFileds, new TaskDetailsBean());
                        }
//                        }
//                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void inflateFields(int mode, final ListofFileds bean) {
        try {
            View view = getLayoutInflater().inflate(
                    R.layout.custom_tag_adapter, fieldContainer, false);
            view.setTag(bean.getClientId());
            final TextView fieldName = (TextView) view.findViewById(R.id.fieldname);
            if (bean.getName() != null) {
                fieldName.setText(bean.getName());
            }

            if (bean.getDataType().equalsIgnoreCase("photo") ||
                    bean.getDataType().equalsIgnoreCase("image") || bean.getDataType().equalsIgnoreCase("signature")) {
                ImageView options = (ImageView) view.findViewById(R.id.imageButton);
                final TextView fileName = (TextView) view.findViewById(R.id.fieldText);
                fileName.setVisibility(View.VISIBLE);
                options.setVisibility(View.VISIBLE);
                options.setTag(fieldContainer.getChildCount());
                options.setContentDescription(bean.getDataType());
                fieldValuesMap.put(bean.getName(), bean);
                options.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        int pos = (Integer) v.getTag();
                        Log.d("Profile", "On click of option menu");
                        showMultimediaOptions(pos, bean.getClientId(), v
                                .getContentDescription().toString());

                    }
                });

                fileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ImageName = fileName.getText().toString();
                        Log.i("Task", "imagename" + ImageName);
                        File file = null;
                        if (bean.getDataType().equalsIgnoreCase("photo") || bean.getDataType().equalsIgnoreCase("signature")) {
                            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                        } else {
                            file = new File(ImageName);
                        }
                        if (file.exists()) {
                            Intent intent = new Intent(context, FullScreenImage.class);
                            intent.putExtra("image", file.toString());
                            context.startActivity(intent);
                        } else {
                            File file1 = null;
                            if (bean.getDataType().equalsIgnoreCase("photo") || bean.getDataType().equalsIgnoreCase("signature")) {
                                file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + ImageName);
                            } else {
                                file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                            }
                            if (file1.exists()) {
                                Intent intent = new Intent(context, FullScreenImage.class);
                                intent.putExtra("image", file1.toString());
                                context.startActivity(intent);
                            }

                        }

                    }
                });


                options.setVisibility(View.VISIBLE);
            } else if ((bean.getDataType().equalsIgnoreCase("datetime") || bean.getDataType().equalsIgnoreCase("date")) && bean.getIsInputRequired().equalsIgnoreCase("no")) {
                final Button add = (Button) view.findViewById(R.id.fieldButton);
                final TextView fileName = (TextView) view.findViewById(R.id.fieldText);
                fileName.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime);
                        fileName.setText(currentDateandTime);

                        add.setVisibility(View.INVISIBLE);

                        multimediaFieldValues.put(bean.getClientId(), currentDateandTime);

                    }
                });
            } else if (bean.getDataType().equalsIgnoreCase("numeric")) {
                final EditText fieldValue = (EditText) view
                        .findViewById(R.id.fieldvalue);
                fieldValue.setVisibility(View.VISIBLE);
                fieldValue.setTag(bean.getDataType());
                fieldValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                fieldValue.setContentDescription(bean.getClientId());
                fieldValuesMap.put(bean.getName(), bean);
                fieldValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                if (bean.getDataType() != null) {
                    if (bean.getName().equalsIgnoreCase(" in Km")) {
                        fieldValue.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // TODO Auto-generated method stub
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                                    changeType(fieldValue);
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });
                    }
                }
            } else if (bean.getDataType().equalsIgnoreCase("text")) {

                final EditText fieldValue = (EditText) view
                        .findViewById(R.id.fieldvalue);
                fieldValue.setVisibility(View.VISIBLE);
                fieldValue.setTag(bean.getDataType());
                fieldValue.setContentDescription(bean.getClientId());
                fieldValuesMap.put(bean.getName(), bean);
                fieldValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                if (bean.getDataType() != null) {
                    if (bean.getName().equalsIgnoreCase("Birth Day")) {
                        fieldValue.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // TODO Auto-generated method stub
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                                    changeType(fieldValue);
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });

                    } else if (bean.getDataType().equalsIgnoreCase("number"))
                        fieldValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                    else if (bean.getDataType().equalsIgnoreCase("email"))
                        fieldValue
                                .setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                }

            } else if ((bean.getDataType().equalsIgnoreCase("datetime") || bean.getDataType().equalsIgnoreCase("date")) && bean.getIsInputRequired().equalsIgnoreCase("yes")) {

                final TextView date = (TextView) view.findViewById(R.id.fieldText);
                date.setVisibility(View.VISIBLE);
                date.setText(bean.getName());
                date.setTag(fieldContainer.getChildCount());
                date.setContentDescription(bean.getDataType());
                fieldValuesMap.put(bean.getName(), bean);
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int integer = (Integer) v.getTag();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime);
                        showCustomDatePicker(currentDateandTime, bean.getClientId());

                        multimediaFieldValues.put(bean.getClientId(), currentDateandTime);
//                        date.setText(strenddate);
                    }
                });
            } else if (bean.getDataType().equalsIgnoreCase("audio")) {

                ImageView options = (ImageView) view.findViewById(R.id.imageButton);
                final TextView fileName = (TextView) view.findViewById(R.id.fieldText);
                fileName.setVisibility(View.VISIBLE);
                options.setVisibility(View.VISIBLE);
                options.setBackground(getResources().getDrawable(R.drawable.ic_audio_file_100));
//                fileName.setText("image");
                options.setTag(fieldContainer.getChildCount());
                options.setContentDescription(bean.getDataType());
                fieldValuesMap.put(bean.getName(), bean);
                options.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        int pos = (Integer) v.getTag();
                        Log.d("Profile", "On click of option menu");
                        try {
                            position = Integer.parseInt(bean.getId());
                            Intent i = new Intent(MainCustomTag.this, AudioRecorder.class);
                            i.putExtra("task", "audio");
                            startActivityForResult(i, 333);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

                fileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ImageName = fileName.getText().toString();
                        Log.i("Task", "imagename" + ImageName);
                        File file = null;
                        Intent intent = new Intent(context, Audioplayer.class);
                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                        if (file.exists()) {
                            intent.putExtra("audio", file.toString());
                            context.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + ImageName);
                            if (file1.exists()) {
                                intent.putExtra("audio", file1.toString());
                                context.startActivity(intent);
                            }

                        }
                    }
                });
                options.setVisibility(View.VISIBLE);
            } else if (bean.getDataType().equalsIgnoreCase("video")) {

                ImageView options = (ImageView) view.findViewById(R.id.imageButton);
                final TextView fileName = (TextView) view.findViewById(R.id.fieldText);
                fileName.setVisibility(View.VISIBLE);
                options.setVisibility(View.VISIBLE);
//                fileName.setText("image");
                options.setTag(fieldContainer.getChildCount());
                options.setContentDescription(bean.getDataType());
                fieldValuesMap.put(bean.getName(), bean);
                options.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        int pos = (Integer) v.getTag();
                        Log.d("Profile", "On click of option menu");
                        showMultimediaOptions(pos, bean.getId(), v
                                .getContentDescription().toString());
                    }
                });

                fileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ImageName = fileName.getText().toString();
                        Log.i("Task", "imagename" + ImageName);
                        File file = null;

                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                        if (file.exists()) {
                            Intent intent = new Intent(context, VideoPlayer.class);
                            intent.putExtra("video", file.getAbsolutePath());
                            context.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + ImageName);
                            if (file1.exists()) {
                                Intent intent = new Intent(context, VideoPlayer.class);
                                intent.putExtra("video", file1.getAbsolutePath());
                                context.startActivity(intent);
                            }
                        }
                    }
                });


                options.setVisibility(View.VISIBLE);
            }

            fieldContainer.addView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showMultimediaOptions(final int tag, String fieldId, String rtype) {
        try {
            if (rtype != null) {
                filedStrId = fieldId;
                Log.d("clone", "----came to showresponse dialog---- tag = "
                        + tag + " rtype = " + rtype);
                if (rtype.equalsIgnoreCase("photo")) {
                    if (fieldId != null) {
                        position = Integer.valueOf(fieldId);
                        type = "photo";
                        photochat(type);
                    }

                } else if (rtype.equalsIgnoreCase("video")) {

                    Log.i("result", "===video entry====");
//                    type = "video";
                    if (fieldId != null) {
                        position = Integer.valueOf(fieldId);
                        type = "video";
                        photochat(type);
                    }
                    /*strIPath = Environment.getExternalStorageDirectory()
                            + "/COMMedia/" + "MVD_"
                            + MainActivity.getFileName() + ".mp4";
                    Log.i("result", "---video File path----" + strIPath);


                    Intent intent = new Intent(context,
                            CustomVideoCamera.class);
                    intent.putExtra("filePath", strIPath);

                    startActivityForResult(intent, 40);*/

                }
            }


        } catch (Exception e) {
            Log.e("profile", "==> " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void photochat(String type1) {
        try {
            final String[] items = new String[]{"Gallery", type1};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0)
                        multimediaType(items[0], items[1]);
                    else if (which == 1)
                        multimediaType(items[1], null);
                }

            });

            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.cancel();
                }
            });
            builder.show();
        } catch (Exception e) {
            Log.e("profile", "===> " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void multimediaType(String strMMType, String gtype) {
        Log.i("clone", "===> inside message response");

        try {
            if (strMMType.equalsIgnoreCase("Gallery")) {
                Log.i("clone", "====> inside gallery");
                if (gtype != null && gtype.equalsIgnoreCase("photo")) {
                    Log.i("clone", "====> inside gallery");
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
                } else if (gtype != null && gtype.equalsIgnoreCase("video")) {
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

            } else if (strMMType.equalsIgnoreCase("Photo")) {
                Log.i("result", "====> inside photo");
                Long free_size = getExternalMemorySize();
                Log.d("IM", free_size.toString());
                if (free_size > 0 && free_size >= 5120) {
                    // Intent i = new Intent(context, Custom_Camara.class);
                    // strIPath = Environment.getExternalStorageDirectory()
                    // + "/COMMedia/MPD_" + callDisp.getFileName()
                    // + ".jpg";
                    // i.putExtra("Image_Name", strIPath);
                    // Log.d("File Path", strIPath);
                    // startActivityForResult(i, 32);

                    final String path = Environment.getExternalStorageDirectory()
                            + "/High Message/";
                    File directory = new File(path);
                    if (!directory.exists())
                        directory.mkdir();
                    strIPath = path + getFileName() + ".jpg";
                    Uri imageUri = Uri.fromFile(new File(strIPath));
                    Intent intent = new Intent(context,
                            CustomVideoCamera.class);
                    intent.putExtra("filePath", strIPath);
                    intent.putExtra("isPhoto", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 132);
                    // Intent intent = new Intent(mainContext,
                    // MultimediaUtils.class);
                    // intent.putExtra("filePath", strIPath);
                    // intent.putExtra("requestCode", CAMERA);
                    // intent.putExtra("action",
                    // MediaStore.ACTION_IMAGE_CAPTURE);
                    // intent.putExtra("createOrOpen", "create");
                    // Log.i("result", "-------photo");
                    // fragment.startActivityForResult(intent, 32);

                } else {
                    Toast.makeText(context, "InSufficient Memory...", Toast.LENGTH_LONG).show();
                }
            } else if (strMMType.equalsIgnoreCase("Video")) {
                final String path = Environment.getExternalStorageDirectory()
                        + "/High Message/";
                File directory = new File(path);
                if (!directory.exists())
                    directory.mkdirs();
                strIPath = path + getFileName() + ".mp4";
                Intent intent = new Intent(context, CustomVideoCamera.class);
                intent.putExtra("filePath", strIPath);
                intent.putExtra("isPhoto", false);
                intent.putExtra("filePath", strIPath);
//                            Uri fileUri = Uri.fromFile(new File(strIPath));
//                            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, 111);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("clone", "=======>" + e.getMessage());
            e.printStackTrace();
        }
    }


    public long getExternalMemorySize() {
        try {
            if (externalMemoryAvailable()) {
                File path = Environment.getExternalStorageDirectory();
                Log.d("Im", path.toString());
                StatFs stat = new StatFs(path.getPath());
                long blockSize = stat.getBlockSize();
                long availableBlocks = stat.getAvailableBlocks();
                return (availableBlocks * blockSize);

            } else {
                return -1;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }

    }

    private boolean externalMemoryAvailable() {
        try {
            return android.os.Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }


    public void showCustomDatePicker(final String start_date1, final String fieldId) {
        try {
            final NumberPicker datePicker, hourPicker, minutePicker, am_pmPicker;
            final String[] dates;
            String[] mAmPmStrings = null;
            final int yyyy;
            position = Integer.valueOf(fieldId);


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


                    LinearLayout parent_view1 = (LinearLayout) fieldContainer
                            .getChildAt(position - 1);
                    final TextView value_img;

                    value_img = (TextView) parent_view1
                            .findViewById(R.id.fieldText);

                    value_img.setVisibility(View.VISIBLE);
                    value_img.setText(strenddate);

                    multimediaFieldValues.put(fieldId, strenddate);

//                        String strendmonth = strenddate.split("-")[1];
//                        int strendmonth1 = Integer.parseInt(strendmonth);
                    String strendday = strenddate.split(" ")[0];
                    multimediaFieldValues.put(String.valueOf(position), strenddate);
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

    private String[] getDatesFromCalender() {
        Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+1"));
        Calendar c2 = Calendar.getInstance(TimeZone.getTimeZone("GMT+1"));

        String today;

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

    private boolean CheckReminderIsValid(String am_pm, int day,
                                         String strDate, String date, boolean start) {
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
                } /*else if (ampm.equalsIgnoreCase("pm") && am_pm.equalsIgnoreCase("pm")) {
                    if (startTime >= currentTime
                            && startMin > currentMin) {
                        isvalid = true;
                    } else {
                        isvalid = false;
                    }

                }*/ else {

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


    public void saveTextValue() {

        fields_list.clear();

        for (int i = 0; i < fieldContainer.getChildCount(); i++) {
            LinearLayout child_view = (LinearLayout) fieldContainer
                    .getChildAt(i);

            ListofFileds bean = profileFieldList.get(i);


            if (bean != null) {
                if (bean.getDataType().equalsIgnoreCase("photo")
                        || bean.getDataType().equalsIgnoreCase("audio")
                        || bean.getDataType().equalsIgnoreCase("video") || bean.getDataType().equalsIgnoreCase("signature")) {
                    String type = bean.getDataType();
                    if (multimediaFieldValues.containsKey(bean.getClientId())) {

                        ListofFileds fieldTemplateBean = new ListofFileds();
                        fieldTemplateBean.setId(bean.getId());
                        fieldTemplateBean.setTask(bean.getName());
                        fieldTemplateBean.setDataType(bean.getDataType());
                        fieldTemplateBean.setName(multimediaFieldValues.get(bean.getClientId()));
                        fields_list.add(fieldTemplateBean);
                    }
                } else if (bean.getDataType().equalsIgnoreCase("text") || bean.getDataType().equalsIgnoreCase("numeric")) {
                    EditText ed_fieldvalue = (EditText) child_view
                            .findViewById(R.id.fieldvalue);
                    if (ed_fieldvalue.getText().toString().trim().length() != 0) {
                        ListofFileds fieldTemplateBean = new ListofFileds();
                        fieldTemplateBean.setId(bean.getId());

                        fieldTemplateBean.setTask(bean.getName());

                        fieldTemplateBean.setDataType(bean.getDataType());

                        if (ed_fieldvalue.getText().toString().trim()
                                .length() > 0) {

                            if (bean.getDataType().equalsIgnoreCase("numeric")) {
                                fieldTemplateBean.setName(ed_fieldvalue
                                        .getText().toString().trim());
                            } else {
                                fieldTemplateBean.setName(ed_fieldvalue
                                        .getText().toString().trim());
                            }
                            fields_list.add(fieldTemplateBean);
                        }
                    }


                } else if ((bean.getDataType().equalsIgnoreCase("datetime") || bean.getDataType().equalsIgnoreCase("date"))) {
                    String type = bean.getDataType();
                    if (multimediaFieldValues.containsKey(bean.getClientId())) {

                        ListofFileds fieldTemplateBean = new ListofFileds();
                        fieldTemplateBean.setId(bean.getId());
                        fieldTemplateBean.setTask(bean.getName());
                        fieldTemplateBean.setDataType(bean.getDataType());
                        fieldTemplateBean.setName((multimediaFieldValues.get(bean.getClientId())));
                        fields_list.add(fieldTemplateBean);
                    }

                }

            }
        }


        try {
            JSONObject[] filedlist = new JSONObject[fields_list.size()];

            int j = 0;
            for (int k = 0; k < fields_list.size(); k++) {


                ListofFileds listofFileds = fields_list.get(k);

                Log.d("TagsValuesInWebService",
                        "Field ID : " + listofFileds.getId()
                                + ", Field Name : "
                                + listofFileds.getName() + ", Type :"
                                + listofFileds.getDataType() + " list size is :  " + fields_list.size());
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("id", listofFileds.getId());

                filedlist[k] = new JSONObject();


                filedlist[k].put("tags", jsonObject1);
                if (listofFileds.getDataType().equalsIgnoreCase("photo") || listofFileds.getDataType().equalsIgnoreCase("image") || listofFileds.getDataType().equalsIgnoreCase("signature")) {
                    filedlist[k].put("fileContent", encodeTobase64(BitmapFactory.decodeFile((listofFileds.getName()))));
                    filedlist[k].put("fileExt", "png");

                } else if (listofFileds.getDataType().equalsIgnoreCase("video")) {
                    filedlist[k].put("fileContent", encodeAudioVideoToBase64(listofFileds.getName()));
                    filedlist[k].put("fileExt", "mp4");

                } else if (listofFileds.getDataType().equalsIgnoreCase("audio")) {
                    filedlist[k].put("fileContent", encodeAudioVideoToBase64(listofFileds.getName()));
                    filedlist[k].put("fileExt", "mp3");
                } else {
                    filedlist[k].put("description", listofFileds.getName());
                }
                j++;
            }


            JSONArray listpostfiles_object = new JSONArray();
            for (int i1 = 0; i1 < filedlist.length; i1++) {
                listpostfiles_object.put(filedlist[i1]);
            }


            JSONObject jsonObject = new JSONObject();

            JSONObject jsonObject1 = new JSONObject();
//            jsonObject1.put("id", taskId);
            jsonObject1.put("id", "14815");

            jsonObject.put("task", jsonObject1);

            jsonObject.put("valueList", listpostfiles_object);

            Appreference.jsonRequestSender.customTagValueEntry(EnumJsonWebservicename.customTagValueEntry, jsonObject, this, fields_list);
        } catch (JSONException e) {
            e.printStackTrace();
        }


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
        }
//        Log.i("FileUpload", "audioVideoEncode========" + strFile);
        return strFile;
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

            buffer.append("<?xml version=\"1.0\"?>"
                    + "<TaskDetailsinfo><TaskDetails");
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
                    buffer.append(" taskDescription=" + quotes + base_64.trim() + quotes);
                } else {
                    buffer.append(" taskDescription=" + quotes + base64.trim() + quotes);
                }
            } else {
                Log.i("URL", "value * " + cmbean.getTaskDescription());
                String s = cmbean.getTaskDescription();
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
            buffer.append(" fromUserId=" + quotes + cmbean.getFromUserId() + quotes);
            buffer.append(" fromUserName=" + quotes + cmbean.getFromUserName() + quotes);
            buffer.append(" toUserId=" + quotes + cmbean.getToUserId() + quotes);
//            Log.i("task", cmbean.getToUserName());
            buffer.append(" toUserName=" + quotes + cmbean.getToUserName() + quotes);
            buffer.append(" taskNo=" + quotes + cmbean.getTaskNo() + quotes);
            buffer.append(" taskId=" + quotes + cmbean.getTaskId() + quotes);
            buffer.append(" taskType=" + quotes + cmbean.getTaskType() + quotes);
            buffer.append(" plannedStartDateTime=" + quotes + cmbean.getUtcPlannedStartDateTime() + quotes);
            buffer.append(" plannedEndDateTime=" + quotes + cmbean.getUtcplannedEndDateTime() + quotes);
            buffer.append(" isRemainderRequired=" + quotes + cmbean.getIsRemainderRequired() + quotes);
            buffer.append(" remainderDateTime=" + quotes + cmbean.getUtcPemainderFrequency() + quotes);
            if (cmbean.getCompletedPercentage() != null && Integer.parseInt(cmbean.getCompletedPercentage()) == 100) {
                if (cmbean.getTaskStatus() != null && cmbean.getTaskStatus().equalsIgnoreCase("Closed")) {
                    buffer.append(" taskStatus=" + quotes + "Closed" + quotes);
                } else {
                    buffer.append(" taskStatus=" + quotes + "Completed" + quotes);
                }
            } else {
                Log.i("Accept", "value taskStatus after compose " + cmbean.getTaskStatus());
                buffer.append(" taskStatus=" + quotes + cmbean.getTaskStatus() + quotes);
            }
            buffer.append(" signalid=" + quotes + cmbean.getSignalid() + quotes);
            buffer.append(" parentId=" + quotes + cmbean.getParentId() + quotes);
            buffer.append(" taskRequestType=" + quotes + cmbean.getTaskRequestType() + quotes);
            buffer.append(" dateFrequency=" + quotes + cmbean.getDateFrequency() + quotes);
            buffer.append(" timeFrequency=" + quotes + cmbean.getTimeFrequency() + quotes);
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
            if (cmbean.getTaskTagValue() != null) {
                buffer.append(" taskTagValue=" + quotes + cmbean.getTaskTagValue() + quotes);
            }
            buffer.append(" taskTagId=" + quotes + cmbean.getCustomTagId() + quotes);
            buffer.append(" taskTagGroupId=" + quotes + cmbean.getCustomSetId() + quotes);
            buffer.append(" isShowOnUI=" + quotes + cmbean.isCustomTagVisible() + quotes);
            buffer.append(" projectId=" + quotes + cmbean.getProjectId() + quotes);
            buffer.append(" projectId=" + quotes + cmbean.getProjectId() + quotes);
            buffer.append(" taskCategory=" + quotes + cmbean.getCatagory() + quotes);
//            buffer.append("reminderTone="+quotes+cmbean.getReminderTone()+ quotes);
//            buffer.append(" parentId=" + quotes + cmbean.getMsgtype() + quotes);
//            if (cmbean.getType().equals("CallSingleChat") || cmbean.getType().equals("CallGroupChat")) {
//                buffer.append(" activeURI=" + quotes + cmbean.getConferenceuri() + quotes);
//            } else {
//                buffer.append(" activeURI=" + quotes + "" + quotes);
//            }
//            buffer.append(" chatmembers="+quotes+cmbean.getChatmembers()+quotes);
            buffer.append(" />");
            /*buffer.append("<message>" + "<![CDATA[" + cmbean.getMessage()
                    + "]]>");
            buffer.append("</message>");*/
//            buffer.append("</>");
//            buffer.append("<SendingStatus signalid=" + quotes
//                    + cmbean.getSendingStatusid() + quotes + " />");
//            buffer.append("<session callid=" + quotes
//                    + cmbean.getSessionCallid() + quotes + " />");
            buffer.append("</TaskDetailsinfo>");
            Log.d("xml", "composed xml for chat======>" + buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            return buffer.toString();
        }
    }

    public void sendMultiInstantMessage(String msgBody, ArrayList<String> userlist) {

//        String buddy_uri = "<"+number+">";
//        Log.i("chat", "buddy_uri======= " + buddy_uri);
//        BuddyConfig bCfg = new BuddyConfig();
//        bCfg.setUri(buddy_uri);
//        bCfg.setSubscribe(true);
        for (String name : userlist) {
            Log.i("task observer", "observer 1 " + name);
            Log.i("task observer", "MainActivity.account.buddyList.size()" + MainActivity.account.buddyList.size());
        }

        if (!getResources().getString(R.string.proxyua).equalsIgnoreCase("enable") || !taskDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
            for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
                String name = MainActivity.account.buddyList.get(i).cfg.getUri();
                Log.i("task", "buddyname-->" + name);
                for (String username : userlist) {
                    Log.i("task", "taskObservers Name-->" + username);
                    String nn = "sip:" + username + "@" + getResources().getString(R.string.server_ip);
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
        } else {

            BuddyConfig bCfg = new BuddyConfig();
            bCfg.setUri("sip:proxyua_highmessaging.com@" + getResources().getString(R.string.server_ip));
            bCfg.setSubscribe(false);
//            MainActivity.account.addBuddy(bCfg);
            Buddy myBuddy = new Buddy();
            try {
                myBuddy.create(MainActivity.account, bCfg);
//                MainActivity.account.addBuddy(myBuddy);
                Log.i("task", "proxybuddy" + bCfg.getUri());
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

        }
    }

    private void showprogress() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("login123", "inside showProgressDialog");

                    progress = new ProgressDialog(context);
                    progress.setCancelable(false);

                    progress.setMessage("Loading");

                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setProgress(0);
                    progress.setMax(100);
                    progress.show();
                } catch (Exception e) {
//                        SingleInstance.printLog(null, e.getMessage(), null, e);
                }
            }


        });
    }

    public String composeCustomTagsDeleteXML(TaskDetailsBean cmbean) {
        StringBuffer buffer = new StringBuffer();
        try {
            buffer.append("<?xml version=\"1.0\"?>"
                    + "<TaskDetailsinfo><removeCustomeTag");

            buffer.append(" fromUserName=" + quotes + cmbean.getFromUserName() + quotes);
            buffer.append(" toUserName=" + quotes + cmbean.getToUserName() + quotes);
            buffer.append(" taskNo=" + quotes + cmbean.getTaskNo() + quotes);
            buffer.append(" taskId=" + quotes + cmbean.getTaskId() + quotes);
            buffer.append(" taskTagGroupId=" + quotes + cmbean.getCustomSetId() + quotes);
            buffer.append(" taskRequestType=" + quotes + cmbean.getTaskRequestType() + quotes);
            buffer.append(" subType=" + quotes + cmbean.getSubType() + quotes);
            buffer.append(" />");
            buffer.append("</TaskDetailsinfo>");
            Log.d("xml", "composed xml for chat======>" + buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            return buffer.toString();
        }
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
            }
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            if (myDate != null) {
                formattedDate = simpleDateFormat.format(myDate);
                Log.i("gcmMessage", "formattedDate " + formattedDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (formattedDate == null) {
            formattedDate = time;
        }

        return formattedDate;
    }
}
