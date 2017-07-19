package com.ase;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;
import com.ase.RandomNumber.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.WebServiceInterface;

//import static com.myapplication3.MediaListAdapter.mPlayer;
//import static com.myapplication3.MediaListAdapter.updatetime;

/**
 * Created by vignesh on 9/28/2016.
 */
public class TemplateView extends Activity implements WebServiceInterface {
    ListView list_all;
    public static MediaListAdapter medialistadapter;
    public static MediaListAdapter.ViewHolder holder;
    public static MediaListAdapter.ViewHolder oldHolder;
    ArrayList<TaskDetailsBean> beanArrayList, templist;
    Context context;
    String mediapath;
    public String dir_path = Environment.getExternalStorageDirectory() + "/High Message/downloads/";
    private Handler handler = new Handler();
    TextView backLayout, headername;
    ImageView sendBtn;
    EditText des;
    static TemplateView templateView;
    String tasktime, taskUTCtime, taskName, taskId, webtaskId, toUserId = "0";
    String startdate, enddate, reminderdate, reminderfreq, reminderquote;
    ProgressDialog progress;
    int l = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.template_view1);
        Appreference.template_page = "TemplateView";
        templateView = this;
        context = this;
        list_all = (ListView) findViewById(R.id.list_all);
        backLayout = (TextView) findViewById(R.id.cancel);
        sendBtn = (ImageView) findViewById(R.id.submit);
        des = (EditText) findViewById(R.id.textvieww);
        headername = (TextView) findViewById(R.id.txtView01);
        taskId = getFileName();
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear1);
        linearLayout.setVisibility(View.GONE);
        LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.linear4);
        linearLayout1.setVisibility(View.GONE);
        Appreference.temconvert = false;

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("template", "template save clicked");

                final Dialog dialog1 = new Dialog(context);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setContentView(R.layout.dialogxml);

                TextView header = (TextView) dialog1.findViewById(R.id.template_header);
                TextView yes = (TextView) dialog1.findViewById(R.id.save);
                TextView no = (TextView) dialog1.findViewById(R.id.no);
                final EditText name = (EditText) dialog1.findViewById(R.id.name);


                header.setText("Set the Template Name ");
                name.setGravity(View.TEXT_ALIGNMENT_CENTER);
                name.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        name.setError(null);
                    }
                });

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (name.getText().toString().equalsIgnoreCase("") || name.getText().toString().equalsIgnoreCase(" ")) {
                            name.setError("Enter valid name");
                        } else {
                            Log.i("template", "task name setting " + name.getText().toString());
                            headername.setText(name.getText().toString());
                            VideoCallDataBase.getDB(context).templateNameUpdate(name.getText().toString(), webtaskId);
                            taskName = name.getText().toString();
                            dialog1.dismiss();
                            Intent intent = new Intent();
                            intent.putExtra("oldvalues", templist);
                            setResult(RESULT_OK, intent);
                            finish();
                        }

                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
//
                    }
                });
                dialog1.show();

            }
        });

//        sendBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(),ContactsFragment.class);
//                startActivity(intent);
                finish();
            }
        });
        templist = new ArrayList<TaskDetailsBean>();
        medialistadapter = new MediaListAdapter(TemplateView.this, templist, "task","", null);
        list_all.setAdapter(medialistadapter);
        beanArrayList = (ArrayList<TaskDetailsBean>) getIntent().getExtras().getSerializable("media");

        Log.e("Template", "listsize" + beanArrayList.size());
        for (int i = 0; i < beanArrayList.size(); i++) {
            TaskDetailsBean taskDetailsBeen = beanArrayList.get(i);
            Log.i("Template:", "forselected" + taskDetailsBeen.isSelect());
            if (taskDetailsBeen.isSelect()) {
                Log.i("Template:", "description--->" + taskDetailsBeen.getTaskDescription());
                Log.i("Template:", "selected------>" + taskDetailsBeen.isSelect());
                taskDetailsBeen.setFromUserName(Appreference.loginuserdetails.getUsername());
                taskDetailsBeen.setMsg_status(0);
                taskDetailsBeen.setTaskStatus("draft");
                taskDetailsBeen.setSelect(false);
                taskDetailsBeen.setTaskDescription(taskDetailsBeen.getTaskDescription());
                templist.add(taskDetailsBeen);
            }
        }
        if (l == 0) {
            Log.e("Task", "for");
            try {
                if (templist != null) {
                    taskName = templist.get(l).getTaskDescription();
                    //            VideoCallDataBase.getDB(context).insertORupdate_Task_history(templist.get(l));
                    taskIdWebservice(templist.get(l));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
/*
    public void deletemenu(TaskDetailsBean mediaListBean, View view, final Context adapter_context) {
        Log.i("popupmenu", "delete " + adapter_context);
        Log.i("popupmenu", "delete " + view);
        Log.i("popupmenu", "view " + mediaListBean.getSignalid());
        Log.i("popupmenu", "view taskList.size " + templist.size());
        VideoCallDataBase.getDB(adapter_context).deletetask(mediaListBean.getTaskId(), mediaListBean.getSignalid());
        for (int i = 0; i < templist.size(); i++) {
            TaskDetailsBean taskDetailsBean_1 = templist.get(i);
            Log.i("popupmenu", "view inside list " + taskDetailsBean_1.getSignalid());
            if (mediaListBean.getSignalid().equalsIgnoreCase(taskDetailsBean_1.getSignalid())) {
                templist.remove(taskDetailsBean_1);
            }
        }
        Log.i("popupmenu", "view taskList.size after delete " + templist.size());
        refresh();
    }
*/


    public void PercentageWebService(String getMediaType, String getMediaPath, String getExt, String signalID) {
        if (!getMediaPath.equals(null)) {
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String dateforrow = dateFormat.format(new Date());

//            SimpleDateFormat dateFormatt = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
//            tasktime = dateFormatt.format(new Date());
//            tasktime = tasktime.split(" ")[1] + " " + tasktime.split(" ")[2];

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = dateFormat.format(new Date());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateforrow = dateFormat.format(new Date());

            String dateforrow1 = dateFormat.format(new Date());

            tasktime = dateTime;
            tasktime = tasktime.split(" ")[1];
            Log.i("task", "tasktime" + tasktime);
            Log.i("UTC", "sendMessage utc time" + dateforrow);

            taskUTCtime = dateforrow;


            final TaskDetailsBean chatBean = new TaskDetailsBean();
            chatBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
            chatBean.setFromUserName(Appreference.loginuserdetails.getUsername());
            chatBean.setSelect(false);
            chatBean.setToUserName(null);
            chatBean.setToUserId(String.valueOf(0));
            chatBean.setTaskName(taskName);
            chatBean.setTaskDescription(getMediaPath);
            chatBean.setSignalid(signalID);
            chatBean.setTaskNo(taskId);
            chatBean.setParentId(getFileName());
            chatBean.setTaskType(templist.get(1).getTaskType());
            chatBean.setTaskPriority("Medium");
            chatBean.setIsRemainderRequired("");
            chatBean.setCompletedPercentage("0");
            chatBean.setPlannedStartDateTime("");
            chatBean.setPlannedEndDateTime("");
            chatBean.setRemainderFrequency("");
            chatBean.setTaskUTCDateTime(dateforrow);
            chatBean.setDateTime(dateTime);
            chatBean.setTaskReceiver("");
            chatBean.setTasktime(tasktime);
            chatBean.setTaskUTCTime(taskUTCtime);
            chatBean.setMimeType(getMediaType);
            chatBean.setTaskId(webtaskId);
            chatBean.setOwnerOfTask(Appreference.loginuserdetails.getUsername());
            if (getMediaPath.equalsIgnoreCase("Completed Percentage 100%") || getMediaPath.equalsIgnoreCase("Completed Percentage :100")) {
                chatBean.setTaskStatus("closed");
            } else {
                chatBean.setTaskStatus("draft");
            }


            // send status 0 is send 1 is unsend
            chatBean.setSendStatus("0");
            chatBean.setMsg_status(0);
            Log.i("task", "image sent" + getMediaType);
//            taskList.add(chatBean);
            Log.i("task", "image sent" + getMediaType);
            // taskList.add(chatBean);
//            refresh();
            if (getExt != null) {
                if (!getExt.equalsIgnoreCase("message")) {
                    VideoCallDataBase.getDB(context).insertORupdate_Task_history(chatBean);
                    VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(chatBean);
//                    templist.add(chatBean);
                    Log.i("task", "msg Status" + chatBean.getMsg_status());
                    refresh();
                }
            }
            try {
                JSONObject jsonObject = new JSONObject();

                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("id", Integer.parseInt(webtaskId));
                jsonObject.put("task", jsonObject1);
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("id", Appreference.loginuserdetails.getId());
                jsonObject.put("from", jsonObject2);
//                    if (!template) {
//                        JSONObject jsonObject3 = new JSONObject();
//                        jsonObject3.put("id", 0);
//                        jsonObject.put("to", jsonObject3);
//                    }
                jsonObject.put("signalId", Utility.getSessionID());
                jsonObject.put("parentId", getFileName());
                jsonObject.put("createdDate", dateforrow);
                jsonObject.put("requestType", "taskConversation");
                jsonObject.put("requestStatus", "approved");
                jsonObject.put("taskEndDateTime", enddate);
                jsonObject.put("taskStartDateTime", startdate);
                jsonObject.put("remainderDateTime", reminderdate);
                jsonObject.put("dateFrequency", "");
                jsonObject.put("timeFrequency", reminderfreq);
                jsonObject.put("remainderQuotes", reminderquote);
                jsonObject.put("remark", "");


                JSONObject jsonObject5 = new JSONObject();
                jsonObject5.put("id", Appreference.loginuserdetails.getId());
                JSONObject jsonObject4 = new JSONObject();
                jsonObject4.put("user", jsonObject5);
                switch (getMediaType.toLowerCase().trim()) {
                    case "image":
                        jsonObject4.put("fileType", "image");
                        jsonObject4.put("fileContent", encodeTobase64(BitmapFactory.decodeFile(getMediaPath)));
                        jsonObject4.put("taskFileExt", "jpg");
                        break;
                    case "video":
                        jsonObject4.put("fileType", "video");
                        Log.i("task", "Video uploaded" + getMediaPath);
                        jsonObject4.put("fileContent", encodeAudioVideoToBase64(getMediaPath));
                        jsonObject4.put("taskFileExt", "mp4");
                        break;
                    case "audio":
                        jsonObject4.put("fileType", "audio");
                        jsonObject4.put("fileContent", encodeAudioVideoToBase64(getMediaPath));
                        jsonObject4.put("taskFileExt", "mp3");
                        break;
                    case "document":
                        jsonObject4.put("fileType", getMediaType);
                        jsonObject4.put("fileContent", encodeFileToBase64Binary(getMediaPath));
                        jsonObject4.put("taskFileExt", getExt);
                        break;
                    case "text":
                        jsonObject4.put("fileType", "text");
                        jsonObject4.put("description", getMediaPath);

                        break;

                }
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(0, jsonObject4);
                jsonObject.put("listTaskConversationFiles", jsonArray);

                Log.i("Request", "Task date update for giver is " + jsonObject);
                Log.i("fileupload", "mpath" + getMediaPath);
                //Log.i("fileupload", "Base64--->" + encodeTobase64(BitmapFactory.decodeFile(mediaListBean.getMediaPath())));


                Appreference.jsonRequestSender.taskConversationEntry(EnumJsonWebservicename.taskConversationEntry, jsonObject, TemplateView.this, null, chatBean);

            } catch (Exception e) {
                e.printStackTrace();
            }

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

    public void taskIdWebservice(TaskDetailsBean taskDetailsBean) {
        try {
            //second = true;
            final TaskDetailsBean chatBean = taskDetailsBean;

            chatBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
            chatBean.setToUserId(String.valueOf(0));

            Log.i("task", "to user id" + String.valueOf(toUserId));
            chatBean.setFromUserName(Appreference.loginuserdetails.getUsername());
            chatBean.setSelect(false);
            chatBean.setToUserName(null);

            chatBean.setTaskName(taskDetailsBean.getTaskDescription());
            chatBean.setOwnerOfTask(Appreference.loginuserdetails.getUsername());

            chatBean.setTaskDescription(taskDetailsBean.getTaskDescription());
            chatBean.setTaskReceiver(null);
            chatBean.setTaskNo(taskId);
            chatBean.setParentId(getFileName());
            chatBean.setTaskPriority("Medium");
            chatBean.setCompletedPercentage("0");
            chatBean.setPlannedStartDateTime(startdate);
            chatBean.setPlannedEndDateTime(enddate);
            chatBean.setRemainderFrequency(reminderdate);
            chatBean.setDateFrequency("");
            chatBean.setTimeFrequency(reminderfreq);
            chatBean.setToUserName(null);
            chatBean.setServerFileName("");
//                chatBean.setReminderQuote(remquotes_2);
            chatBean.setTaskUTCDateTime(taskDetailsBean.getTaskUTCDateTime());
            chatBean.setDateTime(taskDetailsBean.getDateTime());
            chatBean.setSendStatus("0");
            chatBean.setMimeType(taskDetailsBean.getMimeType());
            chatBean.setTaskType(taskDetailsBean.getTaskType());
            chatBean.setTaskId(webtaskId);
            chatBean.setTasktime(tasktime);
            chatBean.setTaskUTCTime(taskUTCtime);
            chatBean.setCustomTagVisible(true);
            chatBean.setTaskStatus("draft");
            chatBean.setSignalid(Utility.getSessionID());

            VideoCallDataBase.getDB(context).insertORupdate_Task_history(chatBean);
            VideoCallDataBase.getDB(context).insertORupdate_TaskHistoryInfo(chatBean);


            Log.i("Task", "template");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", taskDetailsBean.getTaskDescription());
            jsonObject.put("description", taskDetailsBean.getTaskDescription());
            jsonObject.put("taskNo", taskId);
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("id", Appreference.loginuserdetails.getId());
            jsonObject.put("from", jsonObject1);
            jsonObject.put("plannedStartDateTime", "");
            jsonObject.put("plannedEndDateTime", "");
            jsonObject.put("isRemainderRequired", "0");
            jsonObject.put("remainderDateTime", "");
            jsonObject.put("status", "draft");
            jsonObject.put("signalId", taskDetailsBean.getSignalid());
            JSONArray listpostfiles_object = new JSONArray();
            JSONObject jsonObject2 = new JSONObject();
            JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("id", Appreference.loginuserdetails.getId());
            jsonObject2.put("user", jsonObject3);
            switch (taskDetailsBean.getMimeType().toLowerCase().trim()) {
                case "image":
                    jsonObject2.put("fileType", "image");
                    jsonObject2.put("fileContent", encodeTobase64(BitmapFactory.decodeFile(taskDetailsBean.getTaskDescription())));
                    jsonObject2.put("taskFileExt", "jpg");
                    break;
                case "video":
                    jsonObject2.put("fileType", "video");
                    Log.i("task", "Video uploaded" + taskDetailsBean.getTaskDescription());
                    jsonObject2.put("fileContent", encodeAudioVideoToBase64(taskDetailsBean.getTaskDescription()));
                    jsonObject2.put("taskFileExt", "mp4");
                    break;
                case "audio":
                    jsonObject2.put("fileType", "audio");
                    jsonObject2.put("fileContent", encodeAudioVideoToBase64(taskDetailsBean.getTaskDescription()));
                    jsonObject2.put("taskFileExt", "mp3");
                    break;
                case "document":
                    String ext = taskDetailsBean.getTaskDescription().substring(taskDetailsBean.getTaskDescription().lastIndexOf(".") + 1, taskDetailsBean.getTaskDescription().length());
                    Log.i("task", "extension" + ext);
                    jsonObject2.put("fileType", taskDetailsBean.getTaskDescription());
                    jsonObject2.put("fileContent", encodeFileToBase64Binary(taskDetailsBean.getTaskDescription()));
                    jsonObject2.put("taskFileExt", ext);
                    break;
                case "text":
                    jsonObject2.put("fileType", "text");
                    jsonObject2.put("description", taskDetailsBean.getTaskDescription());

                    break;

            }
            listpostfiles_object.put(0, jsonObject2);
            jsonObject.put("listTaskFiles", listpostfiles_object);
            Log.i("Task", "Task--->>>" + jsonObject);
            Appreference.jsonRequestSender.taskEntry(EnumJsonWebservicename.taskEntry, jsonObject, taskDetailsBean, this);
            showprogress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ResponceMethod(final Object object) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("task response", "NewTaskConversation ResponceMethod");
                    CommunicationBean bean = (CommunicationBean) object;
                    if (bean != null && bean.getTaskDetailsBean() != null) {
                        Log.i("task", " bean != null && bean.getTaskDetailsBean() != null");
                        TaskDetailsBean taskDetailsBean = bean.getTaskDetailsBean();
                        Log.i("task", "msg status in response" + taskDetailsBean.getMsg_status());

                        String test1 = bean.getEmail();
//                   Toast.makeText(getApplicationContext(),test,Toast.LENGTH_SHORT).show();
                        JsonElement jelement = new JsonParser().parse(test1);


                        if (jelement.getAsJsonObject() != null) {
                            JsonObject jobject = jelement.getAsJsonObject();
                            if (jobject.has("result_code") && jobject.has("result_text")) {
                                updateTemplateStatus(taskDetailsBean);
                                Log.i("Task", "l value 1-->" + l);
                                l = l + 1;
                                Log.i("Task", "l value 2-->" + l);
                                int jj = templist.size();
                                Log.i("Task", "arraylist-->" + jj);
                                if (l < jj) {
                                    Log.i("Task", "percentagewebservice");
                                    medialistadapter.notifyDataSetChanged();
                                    PercentageWebService(templist.get(l).getMimeType(), templist.get(l).getTaskDescription(), "text", templist.get(l).getSignalid());
                                } else {
                                    Log.i("Task", "percentagewebservice" + l);
                                    medialistadapter.notifyDataSetChanged();
                                }
                            } else {
                                Log.i("task", "Task first entry");
                                webtaskId = jobject.get("id").toString();
                                String memberListValues = "";
                                String groupMemberValues = "";
                                Log.i("task", "webTaskId" + webtaskId);
                                Appreference.webid = webtaskId;
                                updateTemplateStatus(taskDetailsBean);
                                VideoCallDataBase.getDB(context).taskIdUpdate(webtaskId, taskId);
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void ErrorMethod(Object object) {

    }

    private void showprogress() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("login123", "inside showProgressDialog");

                    progress = new ProgressDialog(TemplateView.this);
                    progress.setCancelable(false);

                    progress.setMessage("Creating Template");

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

    public void notifypostEntryResponse(final String values) {
        Log.i("postEntry", "NewTaskactivity  notifypostEntryResponse method");

        try {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    // showToast("success");
                    cancelDialog();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelDialog() {
        try {
            if (progress != null && progress.isShowing()) {
                Log.i("register", "--progress bar end-----");
                progress.dismiss();
                progress = null;
                nextWebservice();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void nextWebservice() {
        l = l + 1;
        Log.i("Task", "l value 2-->" + l);
        int jj = templist.size();
        Log.i("Task", "arraylist-->" + jj);
        if (l < jj) {
            Log.i("Task", "percentagewebservice");
            PercentageWebService(templist.get(l).getMimeType(), templist.get(l).getTaskDescription(), "text", templist.get(l).getSignalid());
        } else {
            Log.i("Task", "percentagewebservice" + l);
        }
    }

    public static TemplateView getInstance() {
        return templateView;
    }

    public void updateTemplateStatus(TaskDetailsBean taskDetailsBean) {
        if (taskDetailsBean != null) {
            Log.d("template", "status");
            VideoCallDataBase.getDB(context).updateTaskSentStatus(taskDetailsBean.getSignalid());
            for (TaskDetailsBean detailsBean : templist) {
                if (detailsBean.getSignalid() != null && detailsBean.getSignalid().equalsIgnoreCase(taskDetailsBean.getSignalid())) {
                    detailsBean.setMsg_status(1);
                    break;
                }
            }
            refresh();
        }
    }

    public void refresh() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                medialistadapter.notifyDataSetChanged();
            }
        });
    }


}
