package com.ase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ase.Bean.ListofFileds;
import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;
import com.ase.RandomNumber.Utility;

import org.json.JSONArray;
import org.json.JSONObject;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.app.MyBuddy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Vector;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.WebServiceInterface;

/**
 * Created by saravanan on 3/15/2017.
 */


public class MoreFieldsEntry extends Activity implements WebServiceInterface {


    private static Context context;

    public LinearLayout fieldContainer = null;

    public ArrayList<MoreFieldsBean> profileFieldList;

    private Vector<CustomTagsBean> getFieldValuesList = new Vector<CustomTagsBean>();

    private HashMap<String, String> multimediaFieldValues = new HashMap<String, String>();

    private MediaPlayer profile_player;

    private MediaPlayer audio_player;

    private int position = 0;

    public String filedStrId;

    private String type = null;

    private String strIPath = null;

    private boolean isSelect;

    private Bitmap bitmap, img = null;

    private Handler handler = new Handler();

    private Button setting = null;

    // private Button btn_reset = null;


    private HashMap<String, ListofFileds> fieldValuesMap = new HashMap<String, ListofFileds>();

    private int CAMERA = 32;

    private int VIDEO = 40;

    private String quotes = "\"";

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
    ArrayList<MoreFieldsBean> listofFiledses;
    ArrayList<String> listOfObservers;
    ArrayList<String> listOfUserNames = new ArrayList<String>();
    ArrayList<String> listOfUserUri = new ArrayList<String>();
    ProgressDialog progress;
    private boolean isgroup;
    TextView send, back;
    Boolean date_check = false, host_check = false;
    TaskDetailsBean taskDetailsBean;
    TextView heading;
    ArrayList<MoreFieldsBean> moreFieldsEntries;
    ArrayList<TaskDetailsBean> uiList = new ArrayList<>();
    ArrayList<TaskDetailsBean> ComposeBean = new ArrayList<>();
    int MediaLIstSize = 0;
    boolean project=false;
//    static Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cutom_tag_activity);
        heading = (TextView) findViewById(R.id.heading);
        heading.setText("More Fields");
        uiList.clear();

        profileFieldList = new ArrayList<>();
        fieldContainer = (LinearLayout) findViewById(R.id.fieldContainer);

        send = (TextView) findViewById(R.id.send);
        back = (TextView) findViewById(R.id.back);

        context = this;
        listofFiledses = new ArrayList<>();
        listofFiledses.clear();

        try {
            taskDetailsBean = (TaskDetailsBean) getIntent().getExtras().getSerializable("bean");
            listOfObservers = (ArrayList<String>) getIntent().getExtras().get("userList");
            moreFieldsEntries = (ArrayList<MoreFieldsBean>) getIntent().getExtras().get("beanList");
            project=getIntent().getExtras().getBoolean("isProject");

        } catch (Exception e) {
            e.printStackTrace();
        }

//        taskId = getIntent().getExtras().getString("Taskid");
//        isgroup = getIntent().getExtras().getBoolean("isgroup",false);


        //testing case

     /*   ListofFileds listofFileds = new ListofFileds();
        listofFileds.setDataType("Attendance_IN");
        listofFileds.setIsInputRequired("autodatetime");
        listofFileds.setId("1");
        listofFileds.setClientId("1");
        listofFileds.setTagName("Attendance_IN_TIME");
        listofFileds.setName("Attendance_IN_TIME");


        ListofFileds listofFileds1 = new ListofFileds();
        listofFileds1.setDataType("Attendance_IN");
        listofFileds1.setIsInputRequired("autolocation");
        listofFileds1.setId("2");
        listofFileds1.setClientId("2");
        listofFileds1.setTagName("Attendance_OUT_TIME");
        listofFileds1.setName("Attendance_OUT_TIME");

        ListofFileds listofFileds2 = new ListofFileds();
        listofFileds2.setDataType("m/cBreakdown");
        listofFileds2.setIsInputRequired("numeric");
        listofFileds2.setId("3");
        listofFileds2.setClientId("3");
        listofFileds2.setTagName("OverTime");
        listofFileds2.setName("OverTime");

        ListofFileds listofFileds3 = new ListofFileds();
        listofFileds3.setDataType("cost");
        listofFileds3.setIsInputRequired("numeric");
        listofFileds3.setId("4");
        listofFileds3.setClientId("4");
        listofFileds3.setTagName("Rates");
        listofFileds3.setName("Rates");

        listofFiledses.add(listofFileds);
        listofFiledses.add(listofFileds1);
        listofFiledses.add(listofFileds2);
        listofFiledses.add(listofFileds3);*/
        send.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("uiList", uiList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    saveTextValue();
                Toast.makeText(getApplicationContext(), "send button clicked", Toast.LENGTH_SHORT).show();

            }
        });

//        ArrayList<String> obs_rec = (ArrayList<String>) getIntent().getExtras().getSerializable("listOfObservers");


        profileFieldList = moreFieldsEntries;

        loadProfileFieldValues();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            Log.d("ActivityResult", "Inside Activity Result " + requestCode + "  resultCode   " + resultCode + "  data  " + data);
            super.onActivityResult(requestCode, resultCode, data);

            Log.d("ActivityResult", "Inside Activity Result " + requestCode + "  resultCode   " + resultCode + "  data  " + data);

            TaskDetailsBean taskDetailsBean2 = null;
            ArrayList<TaskDetailsBean> detailsBeanArrayList = null;

            if (requestCode == 11) {

                String s = data.getExtras().getString("test");
//                showprogress("Send More Fields");
                detailsBeanArrayList = (ArrayList<TaskDetailsBean>) data.getExtras().get("listValues");
                MediaLIstSize = detailsBeanArrayList.size();
                Log.i("MoreFieldsEntry", "Notes uiList.size()  1  * >----> * >>> <<<------------  "+MediaLIstSize);
                for (TaskDetailsBean taskDetailsBean1 : detailsBeanArrayList) {

                    String tasktime, taskUTCtime;

                    TaskDetailsBean taskBean = new TaskDetailsBean();
                    taskBean.setTaskId(taskDetailsBean.getTaskId());
                    taskBean.setToUserId(taskDetailsBean.getToUserId());
                    taskBean.setTaskDescription(taskDetailsBean1.getTaskDescription());
//                    taskDetailsBean.setTaskDescription(taskDetailsBean1.getTaskDescription());
                    taskBean.setSignalid(Utility.getSessionID());
                    Log.i("MoreFieldsEntry", "task Bean Signal id --------->><< 1 " + taskBean.getSignalid());
                    taskBean.setTaskRequestType(taskDetailsBean1.getTaskRequestType());
                    taskBean.setSubType(taskDetailsBean1.getSubType());
                    taskBean.setMimeType(taskDetailsBean1.getMimeType());
                    taskBean.setTaskPriority("Medium");
                    Log.i("MoreFieldsEntry", "Notes ResponceMethod . 00 ---> " + taskBean.getTaskDescription());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateTime = dateFormat.format(new Date());
                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String dateforrow = dateFormat.format(new Date());
                    tasktime = dateTime;
                    tasktime = tasktime.split(" ")[1];
                    Log.i("MoreFieldsEntry", "tasktime" + tasktime);
                    Log.i("MoreFieldsEntry", "sendMessage utc time" + dateforrow);
                    taskUTCtime = dateforrow;
                    taskBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
                    taskBean.setToUserId(taskDetailsBean.getToUserId());
                    taskBean.setTaskType(taskDetailsBean.getTaskType());
                    taskBean.setTaskId(taskDetailsBean.getTaskId());
                    taskBean.setTaskStatus(taskDetailsBean.getTaskStatus());
                    taskBean.setTaskName(taskDetailsBean.getTaskName());
                    taskBean.setOwnerOfTask(taskDetailsBean.getOwnerOfTask());
                    taskBean.setTaskReceiver(taskDetailsBean.getTaskReceiver());
                    taskBean.setTaskNo(taskDetailsBean.getTaskNo());
                    taskBean.setCatagory(taskDetailsBean.getCatagory());
                    taskBean.setIssueId(taskDetailsBean.getIssueId());
                    taskBean.setSendStatus("0");
                    taskBean.setSendStatus("0");
                    taskBean.setToUserName(taskDetailsBean.getToUserName());
                    taskBean.setFromUserName(Appreference.loginuserdetails.getUsername());
                    taskBean.setCompletedPercentage("0");
                    taskBean.setShow_progress(1);
                    taskBean.setRead_status(0);
                    taskBean.setMsg_status(1);
                    taskBean.setUtcPlannedStartDateTime("");
                    taskBean.setUtcPlannedStartDateTime("");
                    taskBean.setUtcplannedEndDateTime("");
                    taskBean.setUtcPemainderFrequency("");
                    taskBean.setDuration(null);
                    taskBean.setDurationUnit(null);
                    taskBean.setTaskUTCDateTime(dateforrow);
                    taskBean.setDateTime(dateTime);
                    taskBean.setTasktime(tasktime);
                    taskBean.setTaskUTCTime(taskUTCtime);
                    taskBean.setCustomTagId(0);
                    taskBean.setCompletedPercentage("0");
                    taskBean.setCustomTagVisible(true);
                    taskBean.setCustomSetId(0);

                    try {
                        JSONObject jsonObject = new JSONObject();
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("id", Integer.parseInt(taskBean.getTaskId()));
                        jsonObject.put("task", jsonObject1);
                        JSONObject jsonObject2 = new JSONObject();
                        jsonObject2.put("id", Appreference.loginuserdetails.getId());
                        jsonObject.put("from", jsonObject2);
                        Log.i("MoreFieldsEntry", "toUserId " + taskBean.getToUserId());
                        if (!project) {
                            JSONObject jsonObject3 = new JSONObject();
                            jsonObject3.put("id", taskBean.getToUserId());
                            {
                                jsonObject.put("to", jsonObject3);
                            }
                        }
                        Log.i("MoreFieldsEntry", "task Bean Signal id --------->><< 2 " + taskBean.getSignalid());
                        jsonObject.put("signalId", taskBean.getSignalid());
                        jsonObject.put("parentId", Appreference.getFileName());
                        jsonObject.put("createdDate", taskBean.getDateTime());

                        jsonObject.put("requestStatus", taskBean.getRequestStatus());

                        jsonObject.put("taskEndDateTime", taskBean.getPlannedEndDateTime());
                        jsonObject.put("taskStartDateTime", taskBean.getPlannedStartDateTime());
                        jsonObject.put("remainderDateTime", taskBean.getIsRemainderRequired());
                        jsonObject.put("dateFrequency", "");
                        jsonObject.put("timeFrequency", "");
                        jsonObject.put("remark", "");
                        JSONObject jsonObject5 = new JSONObject();
                        jsonObject5.put("id", Appreference.loginuserdetails.getId());
                        JSONObject jsonObject4 = new JSONObject();
                        jsonObject4.put("user", jsonObject5);
                        switch (taskBean.getMimeType().toLowerCase().trim()) {
                            case "image":
                                Log.i("MoreFieldsEntry", "mediaListBean.getMimeType() --------> 13 " + taskBean.getMimeType());
                                jsonObject4.put("fileType", "image");
                                jsonObject4.put("fileContent", encodeTobase64(BitmapFactory.decodeFile(taskBean.getTaskDescription())));
                                jsonObject4.put("taskFileExt", "jpg");
                                break;
                            case "video":
                                Log.i("MoreFieldsEntry", "mediaListBean.getMimeType() --------> 14 " + taskBean.getMimeType());
                                jsonObject4.put("fileType", "video");
                                Log.i("task", "Video uploaded" + taskBean.getTaskDescription());
                                jsonObject4.put("fileContent", encodeAudioVideoToBase64(taskBean.getTaskDescription()));
                                jsonObject4.put("taskFileExt", "mp4");
                                break;
                            case "audio":
                                Log.i("MoreFieldsEntry", "mediaListBean.getMimeType() --------> 15 " + taskBean.getMimeType());
                                jsonObject4.put("fileType", "audio");
                                jsonObject4.put("fileContent", encodeAudioVideoToBase64(taskBean.getTaskDescription()));
                                jsonObject4.put("taskFileExt", "mp3");
                                break;
                            case "document":
                                Log.i("MoreFieldsEntry", "mediaListBean.getMimeType() --------> 16 " + taskBean.getMimeType());
                                jsonObject4.put("fileType", taskBean.getMimeType());
                                jsonObject4.put("fileContent", encodeFileToBase64Binary(taskBean.getTaskDescription()));
                                jsonObject4.put("taskFileExt", "pdf");
                                break;
                            case "text":
                                Log.i("MoreFieldsEntry", "mediaListBean.getMimeType() --------> 17 " + taskBean.getMimeType());
                                jsonObject4.put("fileType", "text");
                                jsonObject4.put("description", taskBean.getTaskDescription());
                                break;
                            case "map":
                                Log.i("MoreFieldsEntry", "mediaListBean.getMimeType() --------> 18 " + taskBean.getMimeType());
                                jsonObject4.put("fileType", "map");
                                jsonObject4.put("description", taskBean.getTaskDescription());
                                break;
                            case "textfile":
                                Log.i("MoreFieldsEntry", "mediaListBean.getMimeType() --------> 19 " + taskBean.getMimeType());
                                jsonObject4.put("fileType", taskBean.getMimeType());
                                jsonObject4.put("taskFileExt", "txt");
                                jsonObject4.put("fileContent", encodeFileToBase64Binary(taskBean.getMimeType()));
                                break;
                        }
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(0, jsonObject4);
                        jsonObject.put("listTaskConversationFiles", jsonArray);
                        Log.i("MoreFieldsEntry", "mediaListBean.getMimeType() --------> 20 " + jsonArray);
                        ComposeBean.add(taskBean);
                        VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskBean);
                        if (jsonObject != null) {
                            Log.i("MoreFieldsEntry", "Task date update for giver is " + jsonObject);
                            Appreference.printLog("Completed percentage", jsonObject.toString(), "Completed percentage", null);
                            Log.i("MoreFieldsEntry", "taskConversationEntry 2");
                            Log.i("MoreFieldsEntry", "Notes ResponceMethod . 000 " + taskBean.getTaskDescription());
                            Log.i("MoreFieldsEntry", "mediaListBean.getMimeType() --------> 21 " + taskBean.getMimeType());
                            Log.i("MoreFieldsEntry", "mediaListBean.getSignalid() --------> *** " + taskBean.getSignalid());
                            Appreference.jsonRequestSender.taskConversationEntry(EnumJsonWebservicename.taskConversationEntry, jsonObject, this, null, taskBean);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                    VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                    Log.i("MoreFieldsEntry", "listOfObservers.size() --------> 21 " + listOfObservers.size());

//                    String xml = composeChatXML(taskDetailsBean);
//                    sendMultiInstantMessage(xml, listOfObservers);
//                    uiList.add(taskDetailsBean);
                }
                for (int i = 0; i <ComposeBean.size(); i++) {
                    TaskDetailsBean task = ComposeBean.get(i);
                    Log.i("MoreFieldsEntry", "task signal id --> end Responce Method" + task.getSignalid());
//                    if (task.getSignalid().equalsIgnoreCase(SignalId)) {
//                        String xml = composeChatXML(task);
//                        sendMultiInstantMessage(xml, listOfObservers);
//                        uiList.add(task);
//                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra("uiList", uiList);
        setResult(RESULT_OK, intent);
        finish();
//        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    public void loadProfileFieldValues() {
        try {
            if (profileFieldList.size() > 0) {
//                if (fieldContainer != null)
                fieldContainer.removeAllViews();
                int fied = 0;
                for (MoreFieldsBean fieldtemplate : profileFieldList) {
                    Log.i("Custom", "MoreFieldsEntry 2 " + fieldtemplate.getMimeTypes());
                    inflateFields(fied, fieldtemplate);
                    Log.d("My Profile",
                            "Field ID : " + fieldtemplate.getRequestType()
                                    + ", Field Name : ");
                    fied++;
                }
            } else {


            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public void inflateFields(int mode, final MoreFieldsBean bean) {
        try {
            View view = getLayoutInflater().inflate(
                    R.layout.custom_tag_adapter, fieldContainer, false);
            view.setTag(bean.getClientId());
            final TextView fieldName = (TextView) view.findViewById(R.id.fieldname);
            if (bean.getRequestType() != null) {
                fieldName.setText(bean.getRequestType());
            }

            if (bean.getRequestType().equalsIgnoreCase("attendance_in") || bean.getRequestType().equalsIgnoreCase("attendance_out") || bean.getRequestType().equalsIgnoreCase("rate") || bean.getRequestType().equalsIgnoreCase("work") || bean.getRequestType().equalsIgnoreCase("overtime") || bean.getRequestType().equalsIgnoreCase("m/c_breakdown")) {
                final Button date = (Button) view.findViewById(R.id.fieldButton);
                date.setVisibility(View.VISIBLE);
                date.setBackgroundResource(R.drawable.curves);
                date.setText("Enter");
                date.setTextColor(getResources().getColor(R.color.appcolor));
                date.setTag(Integer.parseInt(bean.getClientId()));
                date.setContentDescription(bean.getRequestType());
//                fieldValuesMap.put(bean.getRequestType(), bean);
                date.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Log.i("CustomTag", "MoreFieldsEntry 0 " + bean.getRequestType());
                        Log.i("CustomTag", "MoreFieldsEntry 1 " + bean.getMimeTypes());
                        position = Integer.parseInt(bean.getClientId());
                        Intent i = new Intent(getApplicationContext(), More_Fields.class);
                        i.putExtra("RequestType", bean.getRequestType());
                        i.putExtra("bean", bean);
                        i.putExtra("position", bean.getClientId());
                        startActivityForResult(i, 11);
                           /* position = Integer.parseInt(bean.getId());
                            Intent i = new Intent(getApplicationContext(), More_Fields.class);
                            i.putExtra("RequestType",bean.getDataType());
                            i.putExtra("MimeType",bean.getIsInputRequired());
                            i.putExtra("position",bean.getClientId());
                            startActivityForResult(i, 11);
                        }else{

                            int pos = (Integer) v.getTag();
                            Log.d("Profile", "On click of option menu");
                        }*/

                    }
                });


            } /*else if (bean.getDataType().equalsIgnoreCase("m/cBreakdown")) {
                final TextView date = (TextView) view.findViewById(R.id.fieldText);
                date.setVisibility(View.VISIBLE);
                date.setText(bean.getName());
                date.setTag(Integer.parseInt(bean.getClientId()));
                date.setContentDescription(bean.getDataType());
                fieldValuesMap.put(bean.getName(), bean);
                date.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub


                        if (bean.getIsInputRequired().equalsIgnoreCase("numeric")) {
                            position = Integer.parseInt(bean.getId());
                            Intent i = new Intent(getApplicationContext(), More_Fields.class);
                            i.putExtra("RequestType",bean.getDataType());
                            i.putExtra("MimeType",bean.getIsInputRequired());
                            startActivityForResult(i, 11);
                        } else if (bean.getIsInputRequired().equalsIgnoreCase("MM")) {
                            position = Integer.parseInt(bean.getId());
                            Intent i = new Intent(getApplicationContext(), More_Fields.class);
                            i.putExtra("RequestType",bean.getDataType());
                            i.putExtra("MimeType",bean.getIsInputRequired());
                            i.putExtra("position",bean.getClientId());
                            startActivityForResult(i, 11);
                        }else{

                            int pos = (Integer) v.getTag();
                            Log.d("Profile", "On click of option menu");
                        }

                    }
                });


            } else if (bean.getDataType().equalsIgnoreCase("cost")) {
                final TextView date = (TextView) view.findViewById(R.id.fieldText);
                date.setVisibility(View.VISIBLE);
                date.setText(bean.getName());
                date.setTag(Integer.parseInt(bean.getClientId()));
                date.setContentDescription(bean.getDataType());
                fieldValuesMap.put(bean.getName(), bean);
                date.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub


                        if (bean.getIsInputRequired().equalsIgnoreCase("numeric")) {
                            position = Integer.parseInt(bean.getId());
                            Intent i = new Intent(getApplicationContext(), More_Fields.class);
                            i.putExtra("RequestType",bean.getDataType());
                            i.putExtra("MimeType",bean.getIsInputRequired());
                            i.putExtra("position",bean.getClientId());
                            startActivityForResult(i, 11);
                        } else{

                            int pos = (Integer) v.getTag();
                            Log.d("Profile", "On click of option menu");
                        }

                    }
                });
            }
*/
            fieldContainer.addView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ResponceMethod(final Object object) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.i("json", "MoreFields ResponceMethod");
                CommunicationBean bean = (CommunicationBean) object;
//                cancelDialog();
                try {
                    String SignalId = "";
                    String fileName = "";
                    String str = bean.getEmail();
                    Log.i("MoreFieldsEntry", "Notes ResponceMethod str " + str);
                    String test = str.toString();
                    String s2 = bean.getFirstname();
                    JsonElement jelement = new JsonParser().parse(test);
                    if (jelement.getAsJsonObject() != null) {
                        JsonObject jobject = jelement.getAsJsonObject();
                        if (jobject.has("fileName")) {
                            Log.i("MoreFieldsEntry", "Notes  10 ");
                            JsonArray fileName_1 = jobject.getAsJsonArray("fileName");
                            fileName = jobject.get("fileName").toString();
                            fileName = fileName.split("\"")[1];
                        }

                        SignalId = jobject.get("signalId").toString();
                        SignalId = SignalId.split("\"")[1];
                        Log.i("MoreFieldsEntry", " Responce Signal id --> " + SignalId);
                        if (fileName != null && !fileName.equalsIgnoreCase("")) {
                            VideoCallDataBase.getDB(context).serverFileNameUpdate(fileName, SignalId);
                        }
                        for (int i = 0; i < ComposeBean.size(); i++) {
                            TaskDetailsBean task = ComposeBean.get(i);
                            if (!task.getMimeType().equalsIgnoreCase("text")){
                                if (fileName != null && !fileName.equalsIgnoreCase("")) {
                                    task.setServerFileName(fileName);
                                }
                            }
                            Log.i("MoreFieldsEntry", " Responce Signal id --> " + SignalId + "   task signal id --> " + task.getSignalid());
                            if (task.getSignalid().equalsIgnoreCase(SignalId)) {
                                String xml = composeChatXML(task);
                                sendMultiInstantMessage(xml, listOfObservers);
                                uiList.add(task);
                            }
                        }
                    }
                    Log.i("MoreFieldsEntry", "Notes uiList.size()  1  * >----> * " + ComposeBean.size() + "  >>> <<<  "+MediaLIstSize);

                    if (MediaLIstSize > 0) {
                        MediaLIstSize = MediaLIstSize - 1;
                    }
                    if (MediaLIstSize == 0) {
                        Log.i("response", "Notes uiList.size()  2   * >----> * " + uiList.size());
                        Intent intent = new Intent();
                        intent.putExtra("uiList", uiList);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                } catch (Exception e) {

                }
            }

        });
    }


    @Override
    public void ErrorMethod(Object object) {

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
        return strFile;
    }

    private String encodeFileToBase64Binary(String fileName) throws IOException {
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


    public String composeChatXML(TaskDetailsBean cmbean) {
        StringBuffer buffer = new StringBuffer();
        byte[] data_1;
        String base_64 = null;
        try {
            Log.i("response", "Notes ResponceMethod taskDetailsBean1.getTaskDescription() 5 " + cmbean.getTaskDescription());
            byte[] data = cmbean.getTaskDescription().trim().getBytes("UTF-8");
            Log.d("base64value", "base64 before " + cmbean.getTaskDescription());
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
            if (cmbean.getServerFileName() != null) {
                data_1 = cmbean.getServerFileName().trim().getBytes("UTF-8");
                Log.d("base64value", "base64 before " + cmbean.getServerFileName());
                base_64 = Base64.encodeToString(data_1, Base64.DEFAULT);
            }
            buffer.append("<?xml version=\"1.0\"?>" + "<TaskDetailsinfo><TaskDetails");
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
                    Log.i("URL", "SaveFilename-->" + base_64.trim());
                    buffer.append(" taskDescription=" + quotes + base_64.trim() + quotes);
                } else {
                    Log.i("URL", "TaskDes-->" + base64);
                    buffer.append(" taskDescription=" + quotes + base64.trim() + quotes);
                }
            } else {
                Log.i("URL", "value * " + cmbean.getTaskDescription());
                if (base64 != null) {
                    String s = base64.trim();
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
            }
            buffer.append(" fromUserId=" + quotes + cmbean.getFromUserId() + quotes);
            buffer.append(" fromUserName=" + quotes + cmbean.getFromUserName() + quotes);
            buffer.append(" toUserId=" + quotes + cmbean.getToUserId() + quotes);
            buffer.append(" toUserName=" + quotes + cmbean.getToUserName() + quotes);
            buffer.append(" taskNo=" + quotes + cmbean.getTaskNo() + quotes);
            buffer.append(" taskId=" + quotes + cmbean.getTaskId() + quotes);
            buffer.append(" taskType=" + quotes + cmbean.getTaskType() + quotes);
            buffer.append(" plannedStartDateTime=" + quotes + cmbean.getUtcPlannedStartDateTime() + quotes);
            buffer.append(" plannedEndDateTime=" + quotes + cmbean.getUtcplannedEndDateTime() + quotes);
            buffer.append(" isRemainderRequired=" + quotes + cmbean.getIsRemainderRequired() + quotes);
            Log.i("MoreFieldsEntry", "remainderDateTime " + cmbean.getUtcPemainderFrequency());
            if (cmbean.getUtcPemainderFrequency() == null || (cmbean.getUtcPemainderFrequency() != null && cmbean.getUtcPemainderFrequency().equalsIgnoreCase(""))) {
                cmbean.setUtcPemainderFrequency("");
            }
            buffer.append(" remainderDateTime=" + quotes + cmbean.getUtcPemainderFrequency() + quotes);
            if (cmbean.getCompletedPercentage() != null && !cmbean.getCompletedPercentage().equalsIgnoreCase("") && Integer.parseInt(cmbean.getCompletedPercentage()) == 100) {
                if (cmbean.getTaskStatus() != null && cmbean.getTaskStatus().equalsIgnoreCase("Closed")) {
                    Log.i("MoreFieldsEntry", " compose taskStatus 1 " + cmbean.getTaskStatus());
                    buffer.append(" taskStatus=" + quotes + "Closed" + quotes);
                } else {
                    Log.i("MoreFieldsEntry", " compose taskStatus 2 " + cmbean.getTaskStatus());
                    if (cmbean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        buffer.append(" taskStatus=" + quotes + "Closed" + quotes);
                    } else {
                        buffer.append(" taskStatus=" + quotes + "Completed" + quotes);
                    }
//                    buffer.append(" taskStatus=" + quotes + "Completed" + quotes);
                }
            } else {
                Log.i("MoreFieldsEntry", " compose taskStatus 3 " + cmbean.getTaskStatus());
                Log.i("Accept", "value taskStatus after compose " + cmbean.getTaskStatus());
                buffer.append(" taskStatus=" + quotes + cmbean.getTaskStatus() + quotes);
            }
            Log.i("MoreFieldsEntry", " compose taskStatus 3 " + cmbean.getSignalid());
            buffer.append(" signalid=" + quotes + cmbean.getSignalid() + quotes);
            buffer.append(" parentId=" + quotes + cmbean.getParentId() + quotes);
            buffer.append(" taskRequestType=" + quotes + cmbean.getTaskRequestType() + quotes);
            buffer.append(" dateFrequency=" + quotes + cmbean.getDateFrequency() + quotes);

            buffer.append(" timeFrequency=" + quotes + "" + quotes);
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
            buffer.append(" taskTagId=" + quotes + cmbean.getCustomTagId() + quotes);
            buffer.append(" taskTagGroupId=" + quotes + cmbean.getCustomSetId() + quotes);
            buffer.append(" isShowOnUI=" + quotes + cmbean.isCustomTagVisible() + quotes);
            buffer.append(" projectId=" + quotes + cmbean.getProjectId() + quotes);
            buffer.append(" taskCategory=" + quotes + cmbean.getCatagory() + quotes);
            buffer.append(" parentTaskId=" + quotes + cmbean.getIssueId() + quotes);
            buffer.append(" />");
            buffer.append("</TaskDetailsinfo>");
            Log.d("xml", "composed xml for chat======>" + buffer.toString());
            Log.d("xml", "composed xml for encode data======>" + Charset.forName("UTF-8").encode(":-)").toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return buffer.toString();
        }
    }

    public void sendMultiInstantMessage(String msgBody, ArrayList<String> userlist) {
        for (String name : userlist) {
            Log.i("task observer", "observer 1 " + name);
            Log.i("task observer", "MainActivity.account.buddyList.size()" + MainActivity.account.buddyList.size());
        }
        Log.i("groupMemberAccess", "!group ");
//        if (!getResources().getString(R.string.proxyua).equalsIgnoreCase("enable") || !taskType.equalsIgnoreCase("Group")) {
        for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
            String name = MainActivity.account.buddyList.get(i).cfg.getUri();
            Log.i("task", "buddyname-->  " + name);
            for (String username : userlist) {
                Log.i("task", "taskObservers Name--> " + username);
                String nn = "sip:" + username + "@" + getResources().getString(R.string.server_ip);
                Log.i("task", "selected user--> " + nn);
                if (nn.equalsIgnoreCase(name)) {
                    Log.i("task", "both users are same ");
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
    private void showprogress(final String name) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("login123", "inside showProgressDialog");
                    if (progress == null)
                        progress = new ProgressDialog(context);
                    progress.setCancelable(false);
                    progress.setMessage(name);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setProgress(0);
                    progress.setMax(100);
                    progress.show();
                } catch (Exception e) {
                }
            }


        });
    }


    public void cancelDialog() {
        try {
            if (progress != null && progress.isShowing()) {
                Log.i("register", "--progress bar end-----");
                progress.dismiss();
                progress = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}