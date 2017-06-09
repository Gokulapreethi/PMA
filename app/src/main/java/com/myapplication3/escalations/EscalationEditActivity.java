package com.myapplication3.escalations;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
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
import com.google.gson.reflect.TypeToken;
import com.myapplication3.Appreference;
import com.myapplication3.AudioRecorder;
import com.myapplication3.Audioplayer;
import com.myapplication3.Bean.ListofFileds;
import com.myapplication3.Bean.TaskDetailsBean;
import com.myapplication3.CustomVideoCamera;
import com.myapplication3.DB.VideoCallDataBase;
import com.myapplication3.FullScreenImage;
import com.myapplication3.HandSketchActivity2;
import com.myapplication3.R;
import com.myapplication3.RandomNumber.Utility;
import com.myapplication3.VideoPlayer;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pjsip.pjsua2.Buddy;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.app.MyBuddy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

import Services.ShowOrCancelProgress;
import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.WebServiceInterface;

/**
 * Created by saravanan on 2/13/2017.
 */
public class EscalationEditActivity extends Activity implements WebServiceInterface {

    ArrayList<TaskDetailsBean> beanList;
    public LinearLayout fieldContainer = null, buttomlinear;
    ArrayList<ListofFileds> listofFiledses;
    ArrayList<String> listOfObservers;
    ArrayList<String> listOfUserNames = new ArrayList<String>();
    ArrayList<String> listOfUserUri = new ArrayList<String>();
    public ArrayList<ListofFileds> profileFieldList;
    ArrayList<String> setIdlist = new ArrayList<>();
    private HashMap<String, String> multimediaFieldValues = new HashMap<String, String>();
    private HashMap<String, ListofFileds> fieldValuesMap = new HashMap<String, ListofFileds>();
    ArrayList<ListofFileds> fields_list = new ArrayList<>();
    ArrayList<TaskDetailsBean> taskDetailsBeanArrayList;
    ArrayList<TaskDetailsBean> taskDetailsBeanArrayList_for_ui;
    private Handler handler = new Handler();
    String taskId;
    static String setId;
    public String filedStrId;
    static String st_hour;
    static String strenddate;
    String check = "Second";
    boolean entry_check = false;

    int z = 0;
    int timeLimit;
    private int position = 0;
    private String type = null;
    private String strIPath = null;
    static String strTime = null;
    ArrayList<ListofFileds> lhsList = null;
    private String quotes = "\"";
    private ShowOrCancelProgress progressListener;

    TextView send, back, NoData;
    ImageView forword, backword, added;
    Button saveBtn;
    ProgressDialog progress;
    Context context;
    TaskDetailsBean taskDetailsBean;
    String selectedValue, valuePosition, buzz_reminDate, projectValue, projectId;
    boolean isPer_selected;
    ArrayList<String> listOfBuddies, touserNames, toUserBuddies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.escalation_edit_activity);
        try {
            beanList = new ArrayList<TaskDetailsBean>();
//        profileFieldList = new ArrayList<>();
            taskDetailsBeanArrayList_for_ui = new ArrayList<>();
            fieldContainer = (LinearLayout) findViewById(R.id.fieldContainer);
            buttomlinear = (LinearLayout) findViewById(R.id.buttomlinear);
//        taskDetailsBean = (TaskDetailsBean) getIntent().getExtras().getSerializable("TaskBean");
            listOfBuddies = new ArrayList<>();
            listOfBuddies.clear();
            if (projectValue != null && projectValue.equalsIgnoreCase("yes")) {
                String parentId = VideoCallDataBase.getDB(Appreference.mainContect).getProjectParentTaskId("select parentTaskId from projectHistory where projectId='" + projectId + "' and taskId ='" + taskId + "'");
                String project_members = VideoCallDataBase.getDB(Appreference.mainContect).getProjectParentTaskId("select taskMemberList from projectHistory where projectId='" + projectId + "' and taskId ='" + parentId + "'");

                int counter = 0;
                for (int i = 0; i < project_members.length(); i++) {
                    if (project_members.charAt(i) == ',') {
                        counter++;
                    }
                }
                Log.d("TaskObserver", "Task Observer counter size is == " + counter);
                for (int j = 0; j < counter + 1; j++) {
                    if (counter == 0) {
                        if (!listOfBuddies.contains(project_members))
                            listOfBuddies.add(project_members);
                    } else if (project_members != null && !Appreference.loginuserdetails.getUsername().equalsIgnoreCase(project_members.split(",")[j])) {
                        if (!listOfBuddies.contains(project_members.split(",")[j]))
                            listOfBuddies.add(project_members.split(",")[j]);
                        Log.d("TaskObserver", "Task Observer name not in same user== " + project_members.split(",")[j]);
                    }
                }
            } else {
                listOfBuddies = VideoCallDataBase.getDB(Appreference.mainContect).selectContactName("select username from contact where loginuser='" + Appreference.loginuserdetails.getUsername() + "'");
            }


            send = (TextView) findViewById(R.id.send);
            NoData = (TextView) findViewById(R.id.NoData);
            back = (TextView) findViewById(R.id.back);
            forword = (ImageView) findViewById(R.id.forword);
            backword = (ImageView) findViewById(R.id.backword);
            added = (ImageView) findViewById(R.id.addnew);
            saveBtn = (Button) findViewById(R.id.save);

            context = this;
            progressListener = Appreference.main_Activity_context;

            listofFiledses = (ArrayList<ListofFileds>) getIntent().getExtras().getSerializable("LeftList");
            taskId = getIntent().getExtras().getString("TaskId");
            taskDetailsBean = (TaskDetailsBean) getIntent().getExtras().getSerializable("TaskBean");
            projectValue = getIntent().getExtras().getString("ProjectValue");
            projectId = getIntent().getExtras().getString("projectId");


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

            if (listOfBuddies.size() > 0) {
                listOfUserNames.clear();
                for (String user_uri : listOfBuddies) {
                    Log.i("EscalationEntryActivity", "listOfBuddies " + user_uri);
                    //            if (user_uri.equalsIgnoreCase("- select -")) {
                    //                if (!listOfUserNames.contains("- select -")) {
                    //                    listOfUserNames.add("- select -");
                    //                }
                    if (user_uri.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        listOfUserNames.add("Me");
                    } else {
                        String user_name = VideoCallDataBase.getDB(Appreference.mainContect).getName(user_uri);
                        if (user_name != null) {
                            listOfUserNames.add(user_name);
                        }
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

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent();
                    Log.i("Escalation", "ValuesFromEscalation " + taskDetailsBeanArrayList_for_ui.size());
                    if (taskDetailsBeanArrayList_for_ui.size() > 0) {
                        intent1.putExtra("ValuesFromEscalation", taskDetailsBeanArrayList_for_ui);
                    }
                    setResult(RESULT_OK, intent1);
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
//                    Toast.makeText(getApplicationContext(), "CustomTagsActivity", Toast.LENGTH_SHORT).show();
                    showprogressforpriority();
                    //                        Intent intent =new Intent(getApplicationContext(),CustomTagsActivity.class);
                    //                        startActivity(intent);
                    /*List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("", ""));
                    Appreference.jsonRequestSender.getCustomTag(EnumJsonWebservicename.getCustomTag, nameValuePairs, EscalationEditActivity.this);
    //*/
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    //                nameValuePairs.add(new BasicNameValuePair("taskId", taskId));
                    //                Appreference.jsonRequestSender.getCustomTag(EnumJsonWebservicename.getCustomTag, nameValuePairs, EscalationEditActivity.this);

                    Appreference.jsonRequestSender.getlistCustomHeaderTags(EnumJsonWebservicename.listCustomHeaderTags, nameValuePairs, EscalationEditActivity.this);
                }
            });

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
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveTextValue();
                }
            });


            try {
                if (setIdlist.size() == 0)
                    loadProfileFieldValues("null", "Second");
                else
                    loadProfileFieldValues(setIdlist.get(z), "Second");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                                if (taskDetailsBean1 != null) {
                                    Log.i("Escalation", "signalId " + taskDetailsBean1.getSignalid());
                                    listofFileds.setSignalId(taskDetailsBean1.getSignalid());
                                }
                                Log.d("My Profile",
                                        "Field ID : " + listofFileds.getId()
                                                + ", Field Name : "
                                                + listofFileds.getName() + ", Field TagName : "
                                                + listofFileds.getTagName() + ", Type :"
                                                + listofFileds.getDataType() + " Task Description " + taskDetailsBean1.getTaskDescription() + " tag id " + taskDetailsBean1.getCustomTagId() + " required  inputvalue " + listofFileds.getIsInputRequired() + " and signal id is " + listofFileds.getSignalId());

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

            Log.i("maincustom", "getSignalId " + taskDetailsBean.getSignalid());
            if (taskDetailsBean.getSignalid() != null) {
                bean.setSignalId(taskDetailsBean.getSignalid());
            }
            Log.i("maincustom", "bean getSignalId " + bean.getSignalId());

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
                            entry_check = true;
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
                        entry_check = true;
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
                    if (bean.getName().equalsIgnoreCase(" in Km")) {
                        fieldValue.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // TODO Auto-generated method stub
//                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
////                                    changeType(fieldValue);
//                                    return true;
//                                } else {
//                                    return false;
//                                }
                                entry_check = true;
                                return false;
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
                if (taskDetailsBean != null) {
                    fieldValue.setText(taskDetailsBean.getTaskDescription());
                }
                if (bean.getDataType() != null) {
                    if (bean.getName().equalsIgnoreCase("Birth Day")) {
                        fieldValue.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // TODO Auto-generated method stub
//                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
////                                    changeType(fieldValue);
//                                    return true;
//                                } else {
                                entry_check = true;
                                return false;
//                                }
                            }
                        });

                    }
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
                        entry_check = true;
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
                            entry_check = true;
                            Intent i = new Intent(EscalationEditActivity.this, AudioRecorder.class);
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
                        entry_check = true;
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
            } else if (bean.getDataType().equalsIgnoreCase("event")) {

                final TextView date = (TextView) view.findViewById(R.id.fieldText);
                date.setVisibility(View.VISIBLE);
                date.setText(bean.getName());
                date.setTag(fieldContainer.getChildCount());
                date.setContentDescription(bean.getDataType());
                fieldValuesMap.put(bean.getName(), bean);
                date.setText(taskDetailsBean.getTaskDescription());

                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(EscalationEditActivity.this, EscalationOptionsValuesActivity.class);
                        position = Integer.valueOf(bean.getClientId());
                        entry_check = true;
                        i.putExtra("valueType", "event");
                        startActivityForResult(i, 11);
//                        multimediaFieldValues.put(bean.getClientId(), currentDateandTime);
//                        date.setText(strenddate);
                    }
                });
            } else if (bean.getDataType().equalsIgnoreCase("value")) {
                final EditText fieldValue = (EditText) view
                        .findViewById(R.id.fieldvalue);
                final TextView date = (TextView) view.findViewById(R.id.fieldText);
                Log.i("Escalation", "Event value is " + taskDetailsBean.getTaskDescription());
                if (taskDetailsBean != null && (taskDetailsBean.getTaskDescription() != null && taskDetailsBean.getTaskDescription().equalsIgnoreCase("assigned") || taskDetailsBean.getTaskDescription().equalsIgnoreCase("overdue"))) {
                    date.setVisibility(View.VISIBLE);
                    fieldValue.setVisibility(View.GONE);
                } else {
                    fieldValue.setVisibility(View.VISIBLE);
                    date.setVisibility(View.GONE);
                }
                fieldValue.setTag(bean.getDataType());
                fieldValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                fieldValue.setContentDescription(bean.getClientId());
                fieldValuesMap.put(bean.getName(), bean);
                valuePosition = bean.getClientId();
                if (taskDetailsBean.getTaskDescription() != null) {
                    fieldValue.setText(taskDetailsBean.getTaskDescription());
                }
//                position = Integer.valueOf(bean.getClientId());
//                fieldValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                if (bean.getDataType() != null) {
                    if (bean.getName().equalsIgnoreCase(" in Km")) {
                        fieldValue.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // TODO Auto-generated method stub
                                entry_check = true;
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

                date.setText(bean.getName());
                date.setTag(fieldContainer.getChildCount());
                date.setContentDescription(bean.getDataType());
                fieldValuesMap.put(bean.getName(), bean);
                if (taskDetailsBean.getTaskDescription() != null) {
                    date.setText(taskDetailsBean.getTaskDescription());
                }
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        entry_check = true;
                        Intent i = new Intent(EscalationEditActivity.this, EscalationOptionsValuesActivity.class);
                        i.putExtra("projectValue", projectValue);
                        i.putExtra("valueType", "value");
                        startActivityForResult(i, 11);
//                        multimediaFieldValues.put(bean.getClientId(), currentDateandTime);
//                        date.setText(strenddate);
                    }
                });
            } else if (bean.getDataType().equalsIgnoreCase("action")) {

                final TextView date = (TextView) view.findViewById(R.id.fieldText);
                date.setVisibility(View.VISIBLE);
                date.setText(bean.getName());
                date.setTag(fieldContainer.getChildCount());
                date.setContentDescription(bean.getDataType());
                fieldValuesMap.put(bean.getName(), bean);
                multimediaFieldValues.put(String.valueOf(fieldContainer.getChildCount()), bean.getName());
                date.setText(taskDetailsBean.getTaskDescription());
                if (taskDetailsBean.getTaskDescription() != null && (taskDetailsBean.getTaskDescription().equalsIgnoreCase("audio conference") || taskDetailsBean.getTaskDescription().equalsIgnoreCase("buzz"))) {
                    AddConferenceBuzzUsers();
                    /*listOfUserNames.clear();
                    listOfUserNames.add(VideoCallDataBase.getDB(context).getname(taskDetailsBean.getTaskReceiver()));
                    Log.i("EscalationEntryActivity", "Result_value listOfUserNames " + listOfUserNames.get(0));
                    listOfBuddies.clear();
                    listOfBuddies.add(taskDetailsBean.getTaskReceiver());
                    Log.i("EscalationEntryActivity", "Result_value listOfBuddies " + listOfBuddies.get(0));*/
                } else {
                    AddReassignObserverUsers(taskDetailsBean.getTaskDescription());
                    /*listOfBuddies.clear();
                    listOfUserNames.clear();
                    listOfBuddies = VideoCallDataBase.getDB(Appreference.mainContect).selectContactName("select username from contact where loginuser='" + Appreference.loginuserdetails.getUsername() + "'");
                    for (String removeTo_users : listOfObservers) {
                        if (listOfBuddies.contains(removeTo_users)) {
                            Log.i("EscalationEntryActivity", "removedTo_user " + removeTo_users);
                            listOfBuddies.remove(removeTo_users);
                        }
                    }
                    for (String user_uri : listOfBuddies) {
                        Log.i("EscalationEntryActivity", "listOfBuddies " + user_uri);
                        if (user_uri.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
//                listOfUserNames.add("Me");
                        } else {
                            String user_name = VideoCallDataBase.getDB(Appreference.mainContect).getName(user_uri);
                            if (user_name != null) {
                                listOfUserNames.add(user_name);
                            }
                        }
                    }*/
                }
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        position = Integer.valueOf(bean.getClientId());
                        entry_check = true;
                        Intent i = new Intent(EscalationEditActivity.this, EscalationOptionsValuesActivity.class);
                        i.putExtra("projectValue", projectValue);
                        i.putExtra("valueType", "action");
                        startActivityForResult(i, 13);

//                        multimediaFieldValues.put(bean.getClientId(), currentDateandTime);
//                        date.setText(strenddate);
                    }
                });
            } else if (bean.getDataType().equalsIgnoreCase("user")) {
                Log.d("EscalationEntryActivity", "user");
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
                    if (taskDetailsBean.getTaskDescription() != null && taskDetailsBean.getTaskDescription().equalsIgnoreCase(listOfUserNames.get(i))) {
                        spinner.setSelection(i);
                    }
                }

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String selected_name = parent.getItemAtPosition(position).toString();
                        if (position == 0) {

                        } else {
                            if (multimediaFieldValues.containsKey(bean.getId())) {
                                multimediaFieldValues.remove(bean.getId());
                            }
//                        fieldValue.setText(selected_name);
                            String user_uri = listOfUserNames.get(position);
                            multimediaFieldValues.put(bean.getId(), user_uri);
                            entry_check = true;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            } else if (bean.getDataType().equalsIgnoreCase("contacts")) {
                Log.d("EscalationEntryActivity", "contacts");
                final TextView escalation_contactlist = (TextView) view.findViewById(R.id.fieldText);
                escalation_contactlist.setVisibility(View.VISIBLE);
                Log.i("Escalation", "contacts To value is " + bean.getName());
                Log.i("Escalation", "contacts To is " + taskDetailsBean.getTaskDescription());
                escalation_contactlist.setText(bean.getName());
                escalation_contactlist.setTag(fieldContainer.getChildCount());
                escalation_contactlist.setContentDescription(bean.getDataType());
                String name = VideoCallDataBase.getDB(context).getname(taskDetailsBean.getTaskDescription());
                if (name != null)
                    escalation_contactlist.setText(name);
                else
                    escalation_contactlist.setText(taskDetailsBean.getTaskDescription());
                fieldValuesMap.put(bean.getName(), bean);
                escalation_contactlist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        position = Integer.valueOf(bean.getClientId());
                        entry_check = true;
                        Intent intent = new Intent(context, AddEscalation.class);
                        intent.putStringArrayListExtra("contactList", listOfBuddies);
//                        Log.i("escalation ", "user list: " + listOfBuddies.get(0));
                        startActivityForResult(intent, 501);
                    }
                });
                /*final MultiSelectionSpinner spinner_1;
                spinner_1 = (MultiSelectionSpinner) view.findViewById(R.id.mySpinner1);
                spinner_1.setVisibility(View.VISIBLE);
                spinner_1.setTag(bean.getDataType());
                spinner_1.setContentDescription(bean.getId());
                fieldValuesMap.put(bean.getName(), bean);

//                if (multimediaFieldValues.get(String.valueOf(fieldContainer.getChildCount() - 1)).equalsIgnoreCase("audio conference")) {
//
//                    listOfUserNames.clear();
//                    listOfUserNames.add(VideoCallDataBase.getDB(context).getname(taskDetailsBean.getTaskReceiver()));
//                    listOfBuddies.clear();
//                    listOfBuddies.add(taskDetailsBean.getTaskReceiver());
//                }
                Log.i("EscalationEntryActivity", "listOfUserNames " + listOfUserNames.size());
                Log.i("EscalationEntryActivity", "listOfBuddies " + listOfBuddies.size());
                spinner_1.setItems(listOfUserNames, listOfBuddies);

                for (int i = 0; i < listOfBuddies.size(); i++) {
                    if (listOfBuddies.get(i) != null) {
                        Log.i("EscalationEntryActivity", "Result_value if " + i + " " + listOfBuddies.get(i) + " " + taskDetailsBean.getTaskDescription());
                        if (taskDetailsBean.getTaskDescription() != null && taskDetailsBean.getTaskDescription().equalsIgnoreCase(listOfBuddies.get(i))) {
                            spinner_1.setSelection(i);
                        }
                    } else {
                        Log.i("EscalationEntryActivity", "Result_value if else " + taskDetailsBean.getTaskDescription());
                        listOfUserNames.clear();
                        listOfBuddies.clear();
                        listOfUserNames.add(VideoCallDataBase.getDB(context).getname(taskDetailsBean.getTaskDescription()));
                        listOfBuddies.add(taskDetailsBean.getTaskDescription());
                        spinner_1.setItems(listOfUserNames, listOfBuddies);
                        spinner_1.setSelection(0);
                    }
                }


                spinner_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        spinner_1.getSelectedItemsAsString();
                        Log.i("EscalationEntryActivity", "Result_value " + spinner_1.getSelectedItemsAsString());
                       *//* String user_uri = listOfUserNames.get(position);
                        multimediaFieldValues.put(bean.getId(), user_uri);*//*
//                        multimediaFieldValues.put(bean.getId(), spinner_1.getSelectedItemsAsString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });*/
            }

            fieldContainer.addView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void AddReassignObserverUsers(String s) {
        listOfBuddies.clear();
        listOfUserNames.clear();
        Log.i("Escalation ", "projectValue  == " + projectValue);
        Log.i("Escalation ", "projectId  == " + projectId);
        if (projectValue.equalsIgnoreCase("yes")) {
            String parenttask;
            String query1 = "select parentTaskId from projectHistory where projectId='" + projectId + "' order by id desc limit 1";

            parenttask = VideoCallDataBase.getDB(this).getProjectParentTaskId(query1);
            Log.i("Escalation ", "parenttask  == " + parenttask);
            String listMembers = VideoCallDataBase.getDB(context).getProjectListMembers(parenttask);
            Log.i("Escalation ", "project_details Task Mem's  names  == " + listMembers);
//            if (s.equalsIgnoreCase("Reassign")) {
//                String receiver_list = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskObservers from projectHistory where taskId='" + taskId + "'");
//                if (receiver_list != null) {
//                    int counter = 0;
//                    for (int i = 0; i < receiver_list.length(); i++) {
//                        if (receiver_list.charAt(i) == ',') {
//                            counter++;
//                        }
//                        Log.i("taskConversation", "project_details Task Mem's counter size is == " + counter);
//                    }
//                    for (int j = 0; j < counter + 1; j++) {
//                        Log.i("taskConversation", "project_details Task Mem's and position == " + receiver_list.split(",")[j] + " " + j);
//                        if (counter == 0) {
//                            listOfUserNames.add(VideoCallDataBase.getDB(context).getname(receiver_list));
//                            listOfBuddies.add(receiver_list);
//
//                        } else {
//                            if (receiver_list.split(",")[j].equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
//                            } else {
//                                listOfUserNames.add(VideoCallDataBase.getDB(context).getname(receiver_list.split(",")[j]));
//                                listOfBuddies.add(receiver_list.split(",")[j]);
//
//                            }
//                        }
//
//                    }
//                }
//            }
            if (listMembers != null) {
                int counter = 0;
                for (int i = 0; i < listMembers.length(); i++) {
                    if (listMembers.charAt(i) == ',') {
                        counter++;
                    }
                    Log.i("taskConversation", "project_details Task Mem's counter size is == " + counter);
                }
                for (int j = 0; j < counter + 1; j++) {
                    Log.i("taskConversation", "project_details Task Mem's and position == " + listMembers.split(",")[j] + " " + j);
                    if (counter == 0) {
                        listOfUserNames.add(VideoCallDataBase.getDB(context).getname(listMembers));
                        listOfBuddies.add(listMembers);

                    } else {
                        if (listMembers.split(",")[j].equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        } else {
                            listOfUserNames.add(VideoCallDataBase.getDB(context).getname(listMembers.split(",")[j]));
                            listOfBuddies.add(listMembers.split(",")[j]);

                        }
                    }

                }
            }
            String receiver_list = VideoCallDataBase.getDB(context).getProjectListMembers(taskId);
            touserNames = new ArrayList<>();
            toUserBuddies = new ArrayList<>();
            touserNames.clear();
            toUserBuddies.clear();
            if (receiver_list != null) {
                int counter = 0;
                for (int i = 0; i < receiver_list.length(); i++) {
                    if (receiver_list.charAt(i) == ',') {
                        counter++;
                    }
                    Log.i("taskConversation", "project_details Task Mem's counter size is == " + counter);
                }
                for (int j = 0; j < counter + 1; j++) {
                    Log.i("taskConversation", "project_details Task Mem's and position == " + receiver_list.split(",")[j] + " " + j);
                    if (counter == 0) {
                        touserNames.add(VideoCallDataBase.getDB(context).getname(receiver_list));
                        toUserBuddies.add(receiver_list);

                    } else {
                        if (receiver_list.split(",")[j].equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        } else {
                            touserNames.add(VideoCallDataBase.getDB(context).getname(receiver_list.split(",")[j]));
                            toUserBuddies.add(receiver_list.split(",")[j]);

                        }
                    }

                }
            }
            Log.i("taskConversation", "project_details before remvUN " + listOfUserNames);
            if (s.equalsIgnoreCase("Add observer")) {
                String old_names = null;
                if (projectValue.equalsIgnoreCase("yes")) {
                    old_names = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskObservers from projectHistory where taskId='" + taskId + "'");
                }
                Log.i("escalation ", "old names --> : " + old_names);

                String[] observerList1;
                ArrayList<String> observernames = new ArrayList<String>();
                if (old_names.contains(",")) {
                    observerList1 = old_names.split(",");
                } else {
                    observerList1 = new String[1];
                    observerList1[0] = old_names;
                }

                for (int i = 0; i < observerList1.length; i++) {
                    observernames.add(VideoCallDataBase.getDB(context).getname(observerList1[i]));
                }

                if (observerList1 != null) {
                    for (String taskDetailsBean2 : observerList1) {
                        if (listOfBuddies.contains(taskDetailsBean2)) {
                            Log.i("taskobserver", "equal" + taskDetailsBean2);
                            listOfBuddies.remove(taskDetailsBean2);
                        }
                    }
                }

                if (observernames != null) {
                    for (String remvUN : observernames) {
                        if (listOfUserNames.contains(remvUN)) {
                            listOfUserNames.remove(remvUN);
                        }
                        Log.i("taskConversation", "project_details after remvUN " + listOfUserNames);
                    }
                }
            }
            for (String remvUN : touserNames) {
                if (listOfUserNames.contains(remvUN)) {
                    listOfUserNames.remove(remvUN);
                }
                Log.i("taskConversation", "project_details after remvUN " + listOfUserNames);
            }
            Log.i("taskConversation", "project_details before remv " + listOfBuddies);
            for (String remv : toUserBuddies) {
                if (listOfBuddies.contains(remv)) {
                    listOfBuddies.remove(remv);
                }
                Log.i("taskConversation", "project_details after remv " + listOfBuddies);
            }

        } else {
            if (projectValue != null && projectValue.equalsIgnoreCase("yes")) {
                String parentId = VideoCallDataBase.getDB(Appreference.mainContect).getProjectParentTaskId("select parentTaskId from projectHistory where projectId='" + projectId + "' and taskId ='" + taskId + "'");
                String project_members = VideoCallDataBase.getDB(Appreference.mainContect).getProjectParentTaskId("select taskMemberList from projectHistory where projectId='" + projectId + "' and taskId ='" + parentId + "'");

                int counter = 0;
                for (int i = 0; i < project_members.length(); i++) {
                    if (project_members.charAt(i) == ',') {
                        counter++;
                    }
                }
                Log.d("TaskObserver", "Task Observer counter size is == " + counter);
                for (int j = 0; j < counter + 1; j++) {
                    if (counter == 0) {
                        if (!listOfBuddies.contains(project_members))
                            listOfBuddies.add(project_members);
                    } else if (project_members != null && !Appreference.loginuserdetails.getUsername().equalsIgnoreCase(project_members.split(",")[j])) {
                        if (!listOfBuddies.contains(project_members.split(",")[j]))
                            listOfBuddies.add(project_members.split(",")[j]);
                        Log.d("TaskObserver", "Task Observer name not in same user== " + project_members.split(",")[j]);
                    }
                }
            } else {
                listOfBuddies = VideoCallDataBase.getDB(Appreference.mainContect).selectContactName("select username from contact where loginuser='" + Appreference.loginuserdetails.getUsername() + "'");
            }
            for (String removeTo_users : listOfObservers) {
                if (listOfBuddies.contains(removeTo_users)) {
                    Log.i("EscalationEntryActivity", "removedTo_user " + removeTo_users);
                    listOfBuddies.remove(removeTo_users);
                }
            }
            for (String user_uri : listOfBuddies) {
                Log.i("EscalationEntryActivity", "listOfBuddies " + user_uri);
                if (user_uri.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                    //                listOfUserNames.add("Me");
                } else {
                    String user_name = VideoCallDataBase.getDB(Appreference.mainContect).getName(user_uri);
                    if (user_name != null) {
                        listOfUserNames.add(user_name);
                    }
                }
            }
        }
    }

    private void AddConferenceBuzzUsers() {
        listOfBuddies.clear();
        listOfUserNames.clear();
        Log.i("Escalation ", "projectValue  == " + projectValue);
        Log.i("Escalation ", "projectId  == " + projectId);
        if (projectValue.equalsIgnoreCase("yes")) {
//            String parenttask;
//            String query1 = "select parentTaskId from projectHistory where projectId='" + projectId + "' order by id desc limit 1";
//
//            parenttask = VideoCallDataBase.getDB(this).getProjectParentTaskId(query1);
//            Log.i("Escalation ", "parenttask  == " + parenttask);
            String listMembers = VideoCallDataBase.getDB(context).getProjectListMembers(taskId);
            Log.i("Escalation ", "project_details Task Mem's  names  == " + listMembers);
            String old_names = null;
            if (projectValue.equalsIgnoreCase("yes")) {
                old_names = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskObservers from projectHistory where taskId='" + taskId + "'");
            }
            if (old_names != null && !old_names.trim().equals("")) {
                listMembers = listMembers + "," + old_names;

            }
            if (listMembers != null) {
                int counter = 0;
                for (int i = 0; i < listMembers.length(); i++) {
                    if (listMembers.charAt(i) == ',') {
                        counter++;
                    }
                    Log.i("taskConversation", "project_details Task Mem's counter size is == " + counter);
                }
                for (int j = 0; j < counter + 1; j++) {
                    Log.i("taskConversation", "project_details Task Mem's and position == " + listMembers.split(",")[j] + " " + j);
                    if (counter == 0) {
                        listOfUserNames.add(VideoCallDataBase.getDB(context).getname(listMembers));
                        listOfBuddies.add(listMembers);

                    } else {
                        if (listMembers.split(",")[j].equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        } else {
                            listOfUserNames.add(VideoCallDataBase.getDB(context).getname(listMembers.split(",")[j]));
                            listOfBuddies.add(listMembers.split(",")[j]);

                        }
                    }

                }
            }


        } else {

            listOfUserNames.clear();
            listOfBuddies.clear();
            listOfUserNames.add(VideoCallDataBase.getDB(context).getname(taskDetailsBean.getTaskReceiver()));
            listOfBuddies.add(taskDetailsBean.getTaskReceiver());
//                            listOfBuddies = VideoCallDataBase.getDB(Appreference.mainContect).selectContactName("select username from contact where loginuser='" + Appreference.loginuserdetails.getUsername() + "'");
//                            for (String removeTo_users : listOfObservers) {
//                                if (listOfBuddies.contains(removeTo_users)) {
//                                    Log.i("EscalationEntryActivity", "removedTo_user " + removeTo_users);
//                                    listOfBuddies.remove(removeTo_users);
//                                }
//                            }
//                            for (String user_uri : listOfBuddies) {
//                                Log.i("EscalationEntryActivity", "listOfBuddies " + user_uri);
//                                if (user_uri.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
//    //                listOfUserNames.add("Me");
//                                } else {
//                                    String user_name = VideoCallDataBase.getDB(Appreference.mainContect).getName(user_uri);
//                                    if (user_name != null) {
//                                        listOfUserNames.add(user_name);
//                                    }
//                                }
//                            }
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
//            hourPicker.setMinValue(0);
//            hourPicker.setMaxValue(11);
//            String[] displayedValues_hour = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
//            hourPicker.setDisplayedValues(displayedValues_hour);
//            } else {
//                hourPicker.setMinValue(00);
//                hourPicker.setMaxValue(23);
//                am_pmPicker.setVisibility(View.GONE);
//            }

            hourPicker.setMinValue(00);
            hourPicker.setMaxValue(23);
            String[] displayedValues_hour = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
            hourPicker.setDisplayedValues(displayedValues_hour);
            am_pmPicker.setVisibility(View.GONE);
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
//            if (hour >= 13) {
//                Log.i("start", "hour>=13 " + hour);
//                if (hour == 13) {
//                    strTime = "1";
//                } else if (hour == 14) {
//                    strTime = "2";
//                } else if (hour == 15) {
//                    strTime = "3";
//                } else if (hour == 16) {
//                    strTime = "4";
//                } else if (hour == 17) {
//                    strTime = "5";
//                } else if (hour == 18) {
//                    strTime = "6";
//                } else if (hour == 19) {
//                    strTime = "7";
//                } else if (hour == 20) {
//                    strTime = "8";
//                } else if (hour == 21) {
//                    strTime = "9";
//                } else if (hour == 22) {
//                    strTime = "10";
//                } else if (hour == 23) {
//                    strTime = "11";
//                }
//                hourPicker.setValue(Integer.parseInt(strTime) - 1);
//                am_pmPicker.setValue(1);
//            } else {
            hourPicker.setValue(hour);
//                am_pmPicker.setValue(0);
//            }
            set.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {
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
                            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
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

//                        toas = tdystr + " " + values2[hourPicker.getValue()] + " : "
//                                + values3[minutePicker.getValue()] + " "
//                                + values4[am_pmPicker.getValue()];
//                    }else{
                        toas = tdystr + " " + hourPicker.getValue() + " : "
                                + values3[minutePicker.getValue()];
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

                        DateFormat selctedDateformate = new SimpleDateFormat("MMM dd yyyy HH : mm");
                        Date date = null;
                        try {
                            date = selctedDateformate.parse(toas);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat day1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        strenddate = day1.format(date);


                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date1 = sdf.parse(strenddate);
                            Date date2 = new Date();
                            String current = sdf.format(date2);
                            Date Current_Date = sdf.parse(current);
                            System.out.println("date1 : " + sdf.format(date1));
                            System.out.println("date2 : " + sdf.format(date2));

                           /* if (date1.compareTo(Current_Date) > 0) {
                                Toast.makeText(context," Date 1 is after Date2 * ",Toast.LENGTH_SHORT).show();
                            } else if (date1.compareTo(Current_Date) < 0) {
                                Toast.makeText(context," Date 1 is before Date2 * ",Toast.LENGTH_SHORT).show();
                            } else if (date1.compareTo(Current_Date) == 0) {
                                Toast.makeText(context," Date 1 and Date2 are equals ==== * ",Toast.LENGTH_SHORT).show();
                            } else {
                                System.out.println("How to get here? * ");
                            }*/
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
        String today = "";
        List<String> dates = new ArrayList<String>();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
            dates.add("Today");
            today = dateFormat.format(c1.getTime());
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c.getTime());
            String split[] = formattedDate.split("-");
            String todaydate = split[0] + " " + split[1];
            for (int i = 0; i < 365; i++) {
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

            Log.i("Escalation", "Escalation signal Id is " + bean.getSignalId());
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
                        fieldTemplateBean.setSignalId(bean.getSignalId());
                        fields_list.add(fieldTemplateBean);
                    }
                } else if (bean.getDataType().equalsIgnoreCase("text") || bean.getDataType().equalsIgnoreCase("numeric")
                        || (bean.getDataType().equalsIgnoreCase("value") && isPer_selected)) {
                    EditText ed_fieldvalue = (EditText) child_view
                            .findViewById(R.id.fieldvalue);
                    if (ed_fieldvalue.getText().toString().trim().length() != 0) {
                        ListofFileds fieldTemplateBean = new ListofFileds();
                        fieldTemplateBean.setId(bean.getId());

                        fieldTemplateBean.setTask(bean.getName());
                        fieldTemplateBean.setSignalId(bean.getSignalId());
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


                } else if ((bean.getDataType().equalsIgnoreCase("datetime") || bean.getDataType().equalsIgnoreCase("event") || bean.getDataType().equalsIgnoreCase("action")
                        || bean.getDataType().equalsIgnoreCase("user") || (bean.getDataType().equalsIgnoreCase("value") && !isPer_selected))) {
                    String type = bean.getDataType();
                    if (multimediaFieldValues.containsKey(bean.getClientId())) {

                        ListofFileds fieldTemplateBean = new ListofFileds();
                        fieldTemplateBean.setId(bean.getId());
                        fieldTemplateBean.setTask(bean.getName());
                        fieldTemplateBean.setDataType(bean.getDataType());
                        fieldTemplateBean.setName((multimediaFieldValues.get(bean.getClientId())));
                        fieldTemplateBean.setSignalId(bean.getSignalId());
                        fields_list.add(fieldTemplateBean);
                    }

                } else if (bean.getDataType().equalsIgnoreCase("contacts")) {
                    String type1 = bean.getDataType();
//                    if (Appreference.reassign_users_scheduled != null) {

                    ListofFileds fieldTemplateBean = new ListofFileds();
                    fieldTemplateBean.setId(bean.getId());
                    fieldTemplateBean.setTask(bean.getName());
                    fieldTemplateBean.setDataType(bean.getDataType());
                    fieldTemplateBean.setName((multimediaFieldValues.get(bean.getClientId())));
                    fieldTemplateBean.setSignalId(bean.getSignalId());
                    fields_list.add(fieldTemplateBean);
//                    } else {
//                        Toast.makeText(EscalationEditActivity.this, "Please select To value", Toast.LENGTH_SHORT).show();
//                        return;
//                    }

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
                                + listofFileds.getDataType() + " list size is :  " + fields_list.size() + " signalId is :  " + listofFileds.getSignalId());
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("id", listofFileds.getId());

                filedlist[k] = new JSONObject();


                filedlist[k].put("headerTag", jsonObject1);
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
            jsonObject1.put("id", taskId);

//            jsonObject1.put("id", taskId);

            jsonObject.put("task", jsonObject1);
            jsonObject.put("setId", setId);

            jsonObject.put("valueList", listpostfiles_object);

            if (entry_check) {
                showprogressforpriority();
                Appreference.jsonRequestSender.customHeaderTagValueEntry(EnumJsonWebservicename.saveCustomHeaderTagValue, jsonObject, this, fields_list);
            } else {
                Toast.makeText(context, "Please enter value", Toast.LENGTH_SHORT).show();
            }
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

    private void showprogressforpriority() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("login123", "inside showProgressDialog");
                    if (progress == null || !progress.isShowing()) {
                        progress = new ProgressDialog(EscalationEditActivity.this);
                        progress.setCancelable(false);
                        progress.setMessage("loading…");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setProgress(0);
                        progress.setMax(100);
                        progress.show();
                    }
                } catch (Exception e) {
//                        SingleInstance.printLog(null, e.getMessage(), null, e);
                }
            }


        });
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Appreference.reassign_users_scheduled = null;
    }

    @Override
    public void ResponceMethod(final Object object) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                CommunicationBean bean = (CommunicationBean) object;
                cancelDialog();
                try {
                    String str = bean.getEmail();
                    Log.i("task", str);
                    String s2 = bean.getFirstname();
                    Log.d("Task2", "name==> " + s2);
                    if (lhsList == null) {
                        lhsList = new ArrayList<ListofFileds>();
                    }
                    if (str.contains("tagName")) {
                        Type collectionType = new TypeToken<List<ListofFileds>>() {
                        }.getType();
                        List<ListofFileds> lcs = new Gson().fromJson(str, collectionType);
                        Log.i("CustomTag", "Value--->" + lcs.size());
                        for (int i = 0; i < lcs.size(); i++) {
                            ListofFileds listUserGroupObject = lcs.get(i);
                            listUserGroupObject.setClientId(String.valueOf(i + 1));
                            lhsList.add(listUserGroupObject);
                            Log.i("CustomTag", "Value123" + "\n" + listUserGroupObject.getId() +
                                    "\n" + listUserGroupObject.getTagName() + "\n" + listUserGroupObject.getIsInputRequired());
                        }
                        if (lhsList != null) {
                            cancelDialog();
                            Intent intent = new Intent(EscalationEditActivity.this, EscalationEntryActivty.class);
                            intent.putExtra("LeftList", lhsList);
                            intent.putExtra("TaskId", taskId);
//                            intent.putExtra("TaskBean", beanValue());
                            intent.putExtra("listOfObservers", listOfObservers);
                            intent.putExtra("taskReceiver", taskDetailsBean.getTaskReceiver());
                            intent.putExtra("projectValue", projectValue);
                            intent.putExtra("projectId", projectId);
                            startActivityForResult(intent, 775);
//                            Toast.makeText(context,"Updated succesfully",Toast.LENGTH_LONG).show();
                            lhsList.clear();
                        }
                    } else {
                     /*   Type collectionType = new TypeToken<List<ListofFileds>>() {
                        }.getType();
                        List<ListofFileds> lcs = new Gson().fromJson(str, collectionType);
                        Log.i("CustomTag", "Value--->" + lcs.size());
                        for (int i = 0; i < lcs.size(); i++) {
                            ListofFileds listUserGroupObject = lcs.get(i);
                            listUserGroupObject.setClientId(String.valueOf(i + 1));
                            lhsList.add(listUserGroupObject);
                            Log.i("CustomTag", "Value123" + "\n" + listUserGroupObject.getId() + "\n" + listUserGroupObject.getTagName() + "\n" + listUserGroupObject.getIsInputRequired());
                        }*/

                        if (lhsList.size() > 0) {
                            lhsList.clear();
                        }
                        lhsList = bean.getFiledsArrayList();
                        Log.i("Checking", "Value---> Response method  " + lhsList.size());
                        showEscalationDetails(lhsList, true);
                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK) {

                if (requestCode == 775) {


                    ArrayList<ListofFileds> beanArrayList_1 = (ArrayList<ListofFileds>) data.getExtras().getSerializable("Values");
                    Log.i("Task", "Result " + beanArrayList_1.size());
                    //                Intent intent = new Intent();
                    //                intent.putExtra("Values", beanArrayList);
                    //                setResult(RESULT_OK, intent);
                    //                finish();


                    Log.i("Checking", "Value--->  onActivityResult   " + beanArrayList_1.size());
                    showEscalationDetails(beanArrayList_1, false);

                } else if (resultCode == RESULT_OK && requestCode == 501) {
                    ArrayList<String> userid = (ArrayList<String>) data.getExtras().getSerializable("userid");
                    ArrayList<String> UsersName = (ArrayList<String>) data.getExtras().getSerializable("UsersName");
                    String s = "";
                    String First_Last_Name = "";
                    for (String username : UsersName) {
                        s = s.concat(username) + ",";
                        First_Last_Name = First_Last_Name + VideoCallDataBase.getDB(context).getname(username) + ",";
                    }
                    s = s.substring(0, s.length() - 1);
                    First_Last_Name = First_Last_Name.substring(0, First_Last_Name.length() - 1);
                    Log.i("escalation", "userid--> " + userid + "UsersName " + UsersName);
                    Log.i("escalation", "userslist " + s);
                    multimediaFieldValues.put(String.valueOf(position), s);
                    LinearLayout parent_view_temp = (LinearLayout) fieldContainer
                            .getChildAt(position - 1);
                    Log.i("escalation", "userslist " + parent_view_temp + " position: " + position);
                    final TextView user_list = (TextView) parent_view_temp
                            .findViewById(R.id.fieldText);
                    user_list.setVisibility(View.VISIBLE);
                    String name = VideoCallDataBase.getDB(context).getname(s);
                    if (First_Last_Name != null) {
                        user_list.setText(First_Last_Name);
                    } else {
                        user_list.setText(s);
                    }
                } else if (resultCode == RESULT_OK && requestCode == 11) {
                    String s = data.getStringExtra("TimeFrequency");
                    String s1 = data.getStringExtra("valueType");
                    Log.i("time", "frequency" + s);
                    selectedValue = s;
                    multimediaFieldValues.put(String.valueOf(position), s);

                    LinearLayout parent_views = (LinearLayout) fieldContainer
                            .getChildAt(position - 1);
                    final TextView value_img_new = (TextView) parent_views
                            .findViewById(R.id.fieldText);
                    value_img_new.setVisibility(View.VISIBLE);
                    value_img_new.setText(s);


                    LinearLayout parent_view_temp = (LinearLayout) fieldContainer
                            .getChildAt(position);
                    final TextView value_picker = (TextView) parent_view_temp
                            .findViewById(R.id.fieldText);
                    final EditText fieldValue = (EditText) parent_view_temp
                            .findViewById(R.id.fieldvalue);

                    if (s1.equalsIgnoreCase("event") && s.equalsIgnoreCase("status")) {

                        isPer_selected = false;

                        value_picker.setVisibility(View.VISIBLE);
                        fieldValue.setVisibility(View.GONE);
                        value_picker.setText("Please Select Status");
                        value_picker.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent i = new Intent(EscalationEditActivity.this, EscalationOptionsValuesActivity.class);
                                i.putExtra("projectValue", projectValue);
                                i.putExtra("valueType", "value");
                                startActivityForResult(i, 12);
                            }
                        });
                        //                        multimediaFieldValues.put(fieldId, strenddate);
                    } else if (s1.equalsIgnoreCase("event") && s.equalsIgnoreCase("% completion")) {

                        isPer_selected = true;

                        fieldValue.setVisibility(View.VISIBLE);
                        value_picker.setVisibility(View.GONE);
                        fieldValue.setText("");
                        fieldValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                        //                        fieldValue.setContentDescription();
                        //                        fieldValuesMap.put();
                        //                fieldValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                           /* if (bean.getDataType() != null) {
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
    */

                    }
                } else if (resultCode == RESULT_OK && requestCode == 12) {
                    try {
                        String s = data.getStringExtra("TimeFrequency");
                        String s1 = data.getStringExtra("valueType");
                        Log.i("time", "frequency" + s);
                        selectedValue = s;
                        int value_position = Integer.valueOf(valuePosition);
                        multimediaFieldValues.put(String.valueOf(value_position), s);

                        LinearLayout parent_views = (LinearLayout) fieldContainer
                                .getChildAt(value_position - 1);
                        final TextView value_img_new = (TextView) parent_views
                                .findViewById(R.id.fieldText);
                        value_img_new.setVisibility(View.VISIBLE);
                        value_img_new.setText(s);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultCode == RESULT_OK && requestCode == 13) {
                    String s = data.getStringExtra("TimeFrequency");
                    String s1 = data.getStringExtra("valueType");
                    Log.i("time", "action members " + s);
                    selectedValue = s;
                    multimediaFieldValues.put(String.valueOf(position), s);

                    LinearLayout parent_views = (LinearLayout) fieldContainer
                            .getChildAt(position - 1);
                    final TextView value_img_new = (TextView) parent_views
                            .findViewById(R.id.fieldText);
                    value_img_new.setVisibility(View.VISIBLE);
                    value_img_new.setText(s);
                  /*  if (s.equalsIgnoreCase("audio conference") || s.equalsIgnoreCase("buzz")) {
                        listOfUserNames.clear();
                        listOfUserNames.add(VideoCallDataBase.getDB(context).getname(taskDetailsBean.getTaskReceiver()));
                        listOfBuddies.clear();
                        listOfBuddies.add(taskDetailsBean.getTaskReceiver());
                    } else {
                        listOfBuddies.clear();
                        listOfUserNames.clear();
                        listOfBuddies = VideoCallDataBase.getDB(Appreference.mainContect).selectContactName("select username from contact where loginuser='" + Appreference.loginuserdetails.getUsername() + "'");
                        for (String removeTo_users : listOfObservers) {
                            if (listOfBuddies.contains(removeTo_users)) {
                                Log.i("EscalationEntryActivity", "removedTo_user " + removeTo_users);
                                listOfBuddies.remove(removeTo_users);
                            }
                        }
                        for (String user_uri : listOfBuddies) {
                            Log.i("EscalationEntryActivity", "listOfBuddies " + user_uri);
                            if (user_uri.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
    //                listOfUserNames.add("Me");
                            } else {
                                String user_name = VideoCallDataBase.getDB(Appreference.mainContect).getName(user_uri);
                                if (user_name != null) {
                                    listOfUserNames.add(user_name);
                                }
                            }
                        }
                    }*/

                    if (s.equalsIgnoreCase("audio conference") || s.equalsIgnoreCase("buzz") || s.equalsIgnoreCase("Aband")) {
                       /* listOfUserNames.clear();
                        listOfBuddies.clear();
                        Log.i("Escalation ", "projectValue  == " + projectValue);
                        if (projectValue.equalsIgnoreCase("yes")) {


                            String listMembers = VideoCallDataBase.getDB(context).getProjectListMembers(taskId);
                            Log.i("Escalation ", "project_details Task Mem's  names  == " + listMembers);

                            if (listMembers != null) {
                                int counter = 0;
                                for (int i = 0; i < listMembers.length(); i++) {
                                    if (listMembers.charAt(i) == ',') {
                                        counter++;
                                    }
                                    Log.i("taskConversation", "project_details Task Mem's counter size is == " + counter);
                                }
                                for (int j = 0; j < counter + 1; j++) {
                                    Log.i("taskConversation", "project_details Task Mem's and position == " + listMembers.split(",")[j] + " " + j);
                                    if (counter == 0) {
                                        listOfUserNames.add(VideoCallDataBase.getDB(context).getname(listMembers));
                                        listOfBuddies.add(listMembers);

                                    } else {
                                        if (listMembers.split(",")[j].equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                        } else {
                                            listOfUserNames.add(VideoCallDataBase.getDB(context).getname(listMembers.split(",")[j]));
                                            listOfBuddies.add(listMembers.split(",")[j]);

                                        }
                                    }

                                }
                            }


                        } else {
                            listOfUserNames.add(VideoCallDataBase.getDB(context).getname(taskDetailsBean.getTaskReceiver()));
                            listOfBuddies.add(taskDetailsBean.getTaskReceiver());
                        }*/

                        AddConferenceBuzzUsers();
                    } else {
                        AddReassignObserverUsers(s);
                    }

//                    LinearLayout parent_view_spinner = (LinearLayout) fieldContainer
//                            .getChildAt(position);
//
//                    try {
//                        final MultiSelectionSpinner spinner_1;
//                        spinner_1 = (MultiSelectionSpinner) parent_view_spinner.findViewById(R.id.mySpinner1);
//                        spinner_1.setVisibility(View.VISIBLE);
//                        spinner_1.setItems(listOfUserNames, listOfBuddies);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ErrorMethod(Object object) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //do your stuff
            Intent intent1 = new Intent();
            Log.i("Escalation", "ValuesFromEscalation " + taskDetailsBeanArrayList_for_ui.size());
            if (taskDetailsBeanArrayList_for_ui.size() > 0) {
                intent1.putExtra("ValuesFromEscalation", taskDetailsBeanArrayList_for_ui);
            }
            setResult(RESULT_OK, intent1);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void showEscalationDetails(ArrayList<ListofFileds> tot_List, boolean isEditEntry) {
        String escaltionValue = "Escalation added : Task ";
        String escalate_buzz = "Task escalated to ";
        String escalate_buzz1 = "";
        String escalate_buzz2 = "";
        Log.i("Escalation", "tot_List size is  ---> " + tot_List.size());
        ArrayList<ListofFileds> beanArrayList = (ArrayList<ListofFileds>) tot_List.clone();
        TaskDetailsBean commonDetailsBean = null;
        for (int a = 0; a < beanArrayList.size(); a++) {
            Log.i("Escalation", "beanArrayList---> " + beanArrayList.get(a));
            ListofFileds listofFileds = beanArrayList.get(a);
            TaskDetailsBean chatBean = new TaskDetailsBean();

            fields_list.add(listofFileds);
            try {
                if (listofFileds.getCreatedBy() != null)
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
            Log.i("Escalation", "listofFileds.getDataType() " + listofFileds.getDataType());
            if ((listofFileds.getDataType().equalsIgnoreCase("datetime") || listofFileds.getDataType().equalsIgnoreCase("date")) || listofFileds.getDataType().equalsIgnoreCase("numeric")) {
                chatBean.setTaskDescription(listofFileds.getTask() + "  :  " + listofFileds.getName());
                chatBean.setMimeType("text");
                escaltionValue = escaltionValue.concat(" on " + listofFileds.getName() + ", ");
                escalate_buzz1 = escalate_buzz1.concat(listofFileds.getName() + ".");
            } else if (listofFileds.getDataType().equalsIgnoreCase("photo") || listofFileds.getDataType().equalsIgnoreCase("image") || listofFileds.getDataType().equalsIgnoreCase("signature")) {
                chatBean.setTaskDescription(listofFileds.getName());
                chatBean.setMimeType("image");
            } else if (listofFileds.getDataType().equalsIgnoreCase("video")) {
                chatBean.setTaskDescription(listofFileds.getName());
                chatBean.setMimeType("video");
            } else if (listofFileds.getDataType().equalsIgnoreCase("audio")) {
                chatBean.setTaskDescription(listofFileds.getName());
                chatBean.setMimeType("audio");
            } else if (listofFileds.getDataType().equalsIgnoreCase("Event")) {
                chatBean.setTaskDescription(listofFileds.getName());
                chatBean.setMimeType("text");
                escaltionValue = escaltionValue.concat(listofFileds.getName() + " is ");
            } else if (listofFileds.getDataType().equalsIgnoreCase("value")) {
                chatBean.setTaskDescription(listofFileds.getName());
                chatBean.setMimeType("text");
                escaltionValue = escaltionValue.concat(listofFileds.getName());
            } else if (listofFileds.getDataType().equalsIgnoreCase("Action")) {
                chatBean.setTaskDescription(listofFileds.getName());
                chatBean.setMimeType("text");
                escaltionValue = escaltionValue.concat(listofFileds.getName() + " to ");
                if (escaltionValue != null && escaltionValue.contains("%")) {
                    escalate_buzz2 = "Reason: % completion <50% on ";
                } else {
                    escalate_buzz2 = "Reason: assigned on ";
                }
            } else if (listofFileds.getDataType().equalsIgnoreCase("contacts")) {
                chatBean.setTaskDescription(listofFileds.getName());
                chatBean.setMimeType("text");
                if (listofFileds.getName().contains(",")) {
                    int counter = 0;
                    for (int i = 0; i < listofFileds.getName().length(); i++) {
                        if (listofFileds.getName().charAt(i) == ',') {
                            counter++;
                        }
                        Log.d("project_details", "Task Mem's counter size is == " + counter);
                    }
                    String escalate_mems = "", escalte_userName = "";
                    for (int j = 0; j < counter + 1; j++) {
                        Log.i("project_details", "Task Mem's and position == " + listofFileds.getName().split(",")[j] + " " + j);
                        escalate_mems = escalate_mems.concat(VideoCallDataBase.getDB(context).getname(listofFileds.getName().split(",")[j]).trim()) + ",";
                        escalte_userName = escalte_userName.concat(listofFileds.getName().split(",")[j].trim()) + ",";
                    }
                    escalate_mems = escalate_mems.substring(0, escalate_mems.length() - 1);
                    escaltionValue = escaltionValue.concat(escalate_mems);
                    if (escaltionValue.contains("Buzz")) {
                        escalate_buzz = escalate_buzz.concat(escalate_mems + ".");
                        escalate_buzz = escalate_buzz.concat(escalate_buzz2);
                        escalate_buzz = escalate_buzz.concat(escalate_buzz1);
                        chatBean.setRemark(escalate_buzz);
                    }
                    if (escaltionValue.contains("Add observer") || escaltionValue.contains("Buzz")) {
                        Log.i("Escalation", "matches if " + escalte_userName);
                        chatBean.setRequestStatus(escalte_userName.substring(0, escalte_userName.length() - 1));
                    }
                } else {
                    String esc_name = VideoCallDataBase.getDB(context).getname(listofFileds.getName());
                    escaltionValue = escaltionValue.concat(esc_name);
                    if (escaltionValue.contains("Buzz")) {
                        escalate_buzz = escalate_buzz.concat(esc_name + ". ");
                        escalate_buzz = escalate_buzz.concat(escalate_buzz2);
                        escalate_buzz = escalate_buzz.concat(escalate_buzz1);
                        chatBean.setRemark(escalate_buzz);
                    }
                    if (escaltionValue.contains("Add observer") || escaltionValue.contains("Buzz")) {
                        Log.i("Escalation", "matches else " + listofFileds.getName());
                        chatBean.setRequestStatus(listofFileds.getName());
                    }
                }
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
            if (isEditEntry) {
                chatBean.setSignalid(listofFileds.getSignalId());
            } else {
                chatBean.setSignalid(Utility.getSessionID());
            }

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
            if (listofFileds.getId() != null)
                chatBean.setCustomTagId(Integer.valueOf(listofFileds.getId()));
            if (listofFileds.getCreatedBy() != null)
                chatBean.setCustomSetId(Integer.valueOf(listofFileds.getCreatedBy()));
            chatBean.setCustomTagVisible(false);


                   /* if (!isTaskName) {

                        TaskDetailsBean taskDetailsBean = chatBean;
                        taskList.add(taskDetailsBean);
                        sortTaskMessage();
                        refresh();
                    }*/

            if (chatBean.getCustomSetId() == 0) {
                chatBean.setCustomSetId(Integer.valueOf(setId));
            }

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

//                    final String xml = composeChatXML(chatBean);

            //                sendInstantMessage(xml);

            Log.d("TaskObserver", "TaskObserver list size is == " + listOfObservers.size());
                   /* if (listOfObservers != null && listOfObservers.size() > 0) {

                        timeLimit = timeLimit + 2000;
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sendMultiInstantMessage(xml, listOfObservers);
                            }
                        }, timeLimit);

                    }*/
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
            Log.d("Schedule", "Data Type ==   " + listofFileds.getDataType());
            Log.d("Schedule", "Data Task ==   " + listofFileds.getTask());
            Log.d("Schedule", "Name Task ==   " + listofFileds.getName());
            Log.d("Schedule", "Tag Name Task ==   " + listofFileds.getTagName());

            if (listofFileds.getDataType().equalsIgnoreCase("datetime") && listofFileds.getTask().equalsIgnoreCase("Conference Start Time")) {
                Log.i("Schedule", " 2 Time " + chatBean.getTaskDescription());
//                        chatBean.setTaskDescription(UTCToLocalTime(chatBean.getTaskDescription()));
                Log.i("Schedule", " 3 Time " + chatBean.getTaskDescription());
                progressListener.StartAlarmManager(chatBean);
            } else if (listofFileds.getDataType().equalsIgnoreCase("datetime") && listofFileds.getTask().equalsIgnoreCase("date")) {
                Log.i("Schedule", " 2 Time " + chatBean.getTaskDescription());
//                        chatBean.setTaskDescription(UTCToLocalTime(chatBean.getTaskDescription()));
                Log.i("Schedule", " 3 Time " + chatBean.getTaskDescription());
                buzz_reminDate = chatBean.getTaskDescription();
                Log.i("Escalation", "buzz_reminDate " + buzz_reminDate);
//                progressListener.StartAlarmManager(chatBean);
            } else if (listofFileds.getDataType().equalsIgnoreCase("action") && listofFileds.getName().equalsIgnoreCase("audio conference")) {
                Log.i("Schedule", " 2 Time " + chatBean.getTaskDescription());
//                        chatBean.setTaskDescription(UTCToLocalTime(chatBean.getTaskDescription()));
                Log.i("Schedule", " 3 Time " + chatBean.getTaskDescription());

//                progressListener.StartAlarmManager(chatBean);
            }
            Log.i("Schedule", " Signalid is  " + chatBean.getSignalid() + " listofFileds Signalid" + listofFileds.getSignalId());
            commonDetailsBean = chatBean;
            multimediaFieldValues.put(listofFileds.getClientId(), listofFileds.getName());
            VideoCallDataBase.getDB(context).insertORupdate_Task_history(chatBean);
            VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(chatBean);
//            if(a == 4){
//                Log.i("Escalation", "beanArrayList size is  2222---> " + beanArrayList.size());
//                break;
//            }
        }
        Log.i("Escalation", "commonDetailsBean " + commonDetailsBean);
        if (commonDetailsBean != null) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("taskId", Integer.valueOf(taskId));
                jsonObject.put("signalId", Utility.getSessionID());
                jsonObject.put("category", "Escalation");
                jsonObject.put("reminderText", escaltionValue);
                jsonObject.put("reminderTone", "");
                jsonObject.put("firingTime", Appreference.customLocalDateToUTC(buzz_reminDate));
                jsonObject.put("requestType", "customHeader");
                jsonObject.put("setId", commonDetailsBean.getCustomSetId());

                Log.i("json", "Escalation object " + jsonObject);
                Appreference.jsonRequestSender.callMscReminders(EnumJsonWebservicename.callMscReminders, jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i("Escalation", "commonDetailsBean getTaskDescription " + commonDetailsBean.getTaskDescription());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = dateFormat.format(new Date());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateforrow = dateFormat.format(new Date());

            commonDetailsBean.setTaskDescription(escaltionValue);
            commonDetailsBean.setMimeType("text");
            commonDetailsBean.setTasktime(dateTime);
            commonDetailsBean.setTaskUTCTime(dateforrow);
            commonDetailsBean.setDateTime(dateTime);
            commonDetailsBean.setTaskUTCDateTime(dateforrow);
            commonDetailsBean.setSendStatus("0");
            commonDetailsBean.setMsg_status(1);
            commonDetailsBean.setCustomTagVisible(true);
            commonDetailsBean.setTaskTagName("");
            commonDetailsBean.setCustomTagId(0);
            commonDetailsBean.setCustomSetId(0);
            if (commonDetailsBean.getTaskDescription() != null && commonDetailsBean.getTaskDescription().contains("Buzz")) {
                commonDetailsBean.setSubType("buzz");
                commonDetailsBean.setTaskRequestType("buzz");
                if (buzz_reminDate != null)
                    commonDetailsBean.setUtcPemainderFrequency(Appreference.customLocalDateToUTC(buzz_reminDate));
                Log.i("Escalation", "buzz_reminDate in single user UTCdate " + Appreference.customLocalDateToUTC(commonDetailsBean.getRemainderFrequency()));
            } else {
                commonDetailsBean.setSubType("normal");
            }
            commonDetailsBean.setSignalid(Utility.getSessionID());
            if (commonDetailsBean.getRequestStatus() != null) {
                commonDetailsBean.setRequestStatus(commonDetailsBean.getRequestStatus());
            }

            taskDetailsBeanArrayList_for_ui.add(commonDetailsBean);
            Log.i("Escalation", "taskconversation list added size is  " + taskDetailsBeanArrayList_for_ui.size());

            VideoCallDataBase.getDB(context).insertORupdate_Task_history(commonDetailsBean);
            VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(commonDetailsBean);

            final String xml = composeChatXML(commonDetailsBean);

            //                sendInstantMessage(xml);
            Log.i("Escalation", "TaskObserver list size is == " + commonDetailsBean.getTaskReceiver());
            listOfObservers.clear();
            if (commonDetailsBean.getProjectId() != null && !commonDetailsBean.getProjectId().equalsIgnoreCase("") && !commonDetailsBean.getProjectId().equalsIgnoreCase("null")) {
                String tomems_value = VideoCallDataBase.getDB(context).getProjectListMembers(commonDetailsBean.getTaskId());
                if (tomems_value != null) {
                    int counter = 0;
                    for (int i = 0; i < tomems_value.length(); i++) {
                        if (tomems_value.charAt(i) == ',') {
                            counter++;
                        }
                        Log.i("taskConversation", "project_details Task Mem's counter size is == " + counter);
                    }
                    Log.i("taskConversation", "projectBean.getFromUserName() " + tomems_value);
                    for (int j = 0; j < counter + 1; j++) {
                        Log.i("taskConversation", "project_details Task Mem's and position == " + tomems_value.split(",")[j] + " " + j);
                        if (counter == 0) {
                            if (!listOfObservers.contains(tomems_value)) {
                                listOfObservers.add(tomems_value);
                            }
                        } else {
                            if (!listOfObservers.contains(tomems_value.split(",")[j])) {
                                listOfObservers.add(tomems_value.split(",")[j]);
                            }
                        }
                    }
                }
                String toobs_value = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskObservers from projectHistory where taskId='" + commonDetailsBean.getTaskId() + "'");
                if (toobs_value != null) {
                    int counter = 0;
                    for (int i = 0; i < toobs_value.length(); i++) {
                        if (toobs_value.charAt(i) == ',') {
                            counter++;
                        }
                        Log.i("taskConversation", "project_details Task Mem's counter size is == " + counter);
                    }
                    Log.i("taskConversation", "projectBean.getFromUserName() " + tomems_value);
                    for (int j = 0; j < counter + 1; j++) {
                        Log.i("taskConversation", "project_details Task Mem's and position == " + toobs_value.split(",")[j] + " " + j);
                        if (counter == 0) {
                            if (!listOfObservers.contains(toobs_value)) {
                                listOfObservers.add(toobs_value);
                            }
                        } else {
                            if (!listOfObservers.contains(toobs_value.split(",")[j])) {
                                listOfObservers.add(toobs_value.split(",")[j]);
                            }
                        }
                    }
                }
            } else {
                listOfObservers.add(commonDetailsBean.getTaskReceiver());
            }
            if (listOfObservers != null && listOfObservers.size() > 0) {
                timeLimit = 50;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendMultiInstantMessage(xml, listOfObservers, 1);
                    }
                }, timeLimit);
            }
        }
        String query = "select * from taskDetailsInfo where taskId = " + taskId + " and subType = 'customeAttribute' group by customSetId ";
        setIdlist = VideoCallDataBase.getDB(context).getSetid(query);
        Log.i("Escalation", "ArrayList" + setIdlist.size());
        check = "First";
        loadProfileFieldValues(setId, check);
    }

    /*public TaskDetailsBean beanValue() {
        final TaskDetailsBean chatBean = new TaskDetailsBean();
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

        Log.i("Accept", "value 5 " + chatBean.getTaskStatus());
        chatBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
        chatBean.setToUserId(String.valueOf(toUserId));
        chatBean.setTaskType(taskType);
        chatBean.setTaskId(webtaskId);
        chatBean.setTaskStatus(taskStatus);
        chatBean.setTasktime(tasktime);
        chatBean.setTaskName(taskName);
        chatBean.setOwnerOfTask(ownerOfTask);
        chatBean.setCustomTagVisible(true);
        chatBean.setTaskReceiver(taskReceiver);
        chatBean.setTaskNo(task_No);
        if (!template)
            chatBean.setCatagory(category);
        chatBean.setIssueId(issueId);
        chatBean.setSendStatus("0");
        chatBean.setToUserName(toUserName);
        chatBean.setUtcPlannedStartDateTime(null);
        chatBean.setUtcPlannedStartDateTime(null);
        chatBean.setUtcplannedEndDateTime(null);
        chatBean.setUtcPemainderFrequency(null);
        chatBean.setDuration(null);
        chatBean.setDurationUnit(null);
        chatBean.setTaskDescription(null);
        chatBean.setIsRemainderRequired(null);
        chatBean.setSignalid(null);
        chatBean.setFromUserName(Appreference.loginuserdetails.getUsername());
        chatBean.setCompletedPercentage("0");
        chatBean.setMimeType(null);
        chatBean.setTaskPriority(null);
        chatBean.setDateFrequency(null);
        chatBean.setTimeFrequency(null);
        chatBean.setShow_progress(1);
        chatBean.setRead_status(0);
        chatBean.setReminderQuote(null);
        chatBean.setRemark(null);
        chatBean.setTaskUTCTime(taskUTCtime);
        chatBean.setTaskObservers(null);
        chatBean.setServerFileName(null);
        chatBean.setMsg_status(1);
        chatBean.setRequestStatus(null);
        chatBean.setGroupTaskMembers(null);
        chatBean.setTaskUTCDateTime(dateforrow);
        chatBean.setSubType(null);
        chatBean.setDaysOfTheWeek(null);
        chatBean.setRepeatFrequency(null);
        if (project) {
            chatBean.setProjectId(projectId);
        }
        Log.i("custom project", "chatBean.getProjectId() " + chatBean.getProjectId());
        chatBean.setTaskTagName(null);
        chatBean.setCustomTagId(0);

        chatBean.setCustomTagVisible(true);
        chatBean.setCustomSetId(0);

        return chatBean;
    }*/

    public void sendMultiInstantMessage(String msgBody, ArrayList<String> userlist, int sendTo) {

//        String buddy_uri = "<"+number+">";
//        Log.i("chat", "buddy_uri======= " + buddy_uri);
//        BuddyConfig bCfg = new BuddyConfig();
//        bCfg.setUri(buddy_uri);
//        bCfg.setSubscribe(true);
        for (String name : userlist) {
            Log.i("task observer", "observer 1 " + name);
            Log.i("task observer", "MainActivity.account.buddyList.size()" + MainActivity.account.buddyList.size());
        }

        if (!getResources().getString(R.string.proxyua).equalsIgnoreCase("enable") || !taskDetailsBean.getTaskType().equalsIgnoreCase("Group") || sendTo == 1) {
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
                    buffer.append(" taskDescription=" + quotes + base64 + quotes);
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
                    buffer.append(" taskDescription=" + quotes + base64 + quotes);
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
            String remark_12 = cmbean.getRemark();
            if (remark_12 != null && remark_12.contains("<")) {
                remark_12 = remark_12.replaceAll("<", "&lt;");
                buffer.append(" remark=" + quotes + remark_12 + quotes);
                Log.i("URL", "remark_12 if " + remark_12);
            } else {
                buffer.append(" remark=" + quotes + remark_12 + quotes);
                Log.i("URL", "remark_12 else " + remark_12);
            }
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
}
