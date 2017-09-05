package com.ase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.WebServiceInterface;

/**
 * Created by prasanth on 9/1/2017.
 */
public class CaptionMedia extends Activity implements WebServiceInterface {
    public Context context;
    TaskDetailsBean taskbean;
    String tab = "Captionmedia";
    EditText name;
    String taskid, signalid, fileName;
    static CaptionMedia captionMedia;
    ProgressDialog progress;

    public static CaptionMedia getInstance() {
        return captionMedia;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caption_media);
        context = this;
        taskbean = (TaskDetailsBean) getIntent().getSerializableExtra("taskbean");
        TextView header = (TextView) findViewById(R.id.template_header);
        TextView yes = (TextView) findViewById(R.id.save);
        TextView no = (TextView) findViewById(R.id.no);
        name = (EditText) findViewById(R.id.remarks);
        taskid = taskbean.getTaskId();
        signalid = taskbean.getSignalid();
        fileName = taskbean.getServerFileName();
        header.setText("File Name : " + fileName);


        Log.i(tab, "onCreate ");
        try {
            hideKeyboard();
            String previous_caption = VideoCallDataBase.getDB(context).getProjectParentTaskId("select caption from taskDetailsInfo where taskid='" + taskbean.getTaskId() + "' and signalid='" + taskbean.getSignalid() + "'");
            if (previous_caption != null) {
                Log.i(tab, "previous_caption==> " + previous_caption);
                name.append(previous_caption);
            }else{
//                name.append("#");
                name.setText("# ");
                Selection.setSelection(name.getText(), name.getText().length());
            }
            name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    Log.i(tab, "beforeTextChanged==> "+ name.getText().toString().substring(0));
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.i(tab, "onTextChanged==> " + name.getText().toString().substring(0));
                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.i(tab, "afterTextChanged==> "+ name.getText().toString().substring(0) );
                    if(!s.toString().contains("# ")){
                        name.setText("# ");
                        Selection.setSelection(name.getText(), name.getText().length());

                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog(tab, "previous_caption : " + e.getMessage(), "WARN", null);
        }

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.i(tab, "save ==> "+ name.getText().toString().length());
                    if (name.getText().toString() != null && !name.getText().toString().equalsIgnoreCase("") && name.getText().toString().length()>2) {
                        String caption_details = name.getText().toString();
                        caption_webservice(caption_details);
                        hideKeyboard();
                    } else {
                        showToast("Please give caption");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog(tab, "caption_details : " + e.getMessage(), "WARN", null);
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                finish();
            }
        });
    }

    private void hideKeyboard() {
        try {
            Log.i(tab, "hideKeyboard ");
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(name.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog(tab, "hideKeyboard Exception : " + e.getMessage(), "WARN", null);
        }
    }

    private void caption_webservice(String caption_details) {
        try {
            showprogress();
            TaskDetailsBean detailsBean=new TaskDetailsBean();
            detailsBean.setCaption(caption_details);
            detailsBean.setTaskId(taskid);
            detailsBean.setSignalid(signalid);
            detailsBean.setServerFileName(fileName);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
            nameValuePairs.add(new BasicNameValuePair("taskId", taskid));
            nameValuePairs.add(new BasicNameValuePair("signalId", signalid));
            nameValuePairs.add(new BasicNameValuePair("fileName", fileName));
            nameValuePairs.add(new BasicNameValuePair("caption", caption_details));
            Log.i("group", "nameValuePairs " + nameValuePairs);
            if (isNetworkAvailable()) {
                Log.i("group", "isNetworkAvailable " + isNetworkAvailable());
                Appreference.jsonRequestSender.taskConversactionCaption(EnumJsonWebservicename.taskConversactionCaption, nameValuePairs, this, detailsBean);
            } else {
                cancelDialog();
                Log.i("group", "isNetworkAvailable else " + isNetworkAvailable());
                VideoCallDataBase.getDB(context).updateaccept("update taskDetailsInfo set caption='" + detailsBean.getCaption() + "' where taskid='" + detailsBean.getTaskId() + "' and signalid='" + detailsBean.getSignalid() + "'");
                VideoCallDataBase.getDB(context).updateaccept("update taskDetailsInfo set wscaptionstatus='222' where taskid='" + detailsBean.getTaskId() + "' and signalid='" + detailsBean.getSignalid() + "'");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog(tab, "hideKeyboard Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = null;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().getApplicationContext().CONNECTIVITY_SERVICE);
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog(tab, "isNetworkAvailable Exception : " + e.getMessage(), "WARN", null);
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showprogress() {
        try {
            Log.i("expand", "inside show progress--------->");
            if (progress == null || !progress.isShowing()) {
                progress = new ProgressDialog(CaptionMedia.this);
                progress.setCancelable(false);
                progress.setMessage("Sending...");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setProgress(0);
                progress.setMax(100);
                progress.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog(tab, "showprogress() Exception : " + e.getMessage(), "WARN", null);
        }
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
            Appreference.printLog(tab, "cancelDialog Exception " + e.getMessage(), "WARN", null);
        }
    }

    public void showToast(final String msg) {
        try {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog(tab, "showToast Exception : " + e.getMessage(), "WARN", null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideKeyboard();
        Log.i(tab, "onResume ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(tab, "onDestroy ");
        hideKeyboard();

    }

    @Override
    public void ResponceMethod(Object object) {
        try {
            cancelDialog();
            Log.i(tab, "inside webservice taskConversactionCaption Responce");
            CommunicationBean communicationBean = (CommunicationBean) object;
            String response = communicationBean.getEmail();
            String WebServiceEnum_Response = communicationBean.getFirstname();
            TaskDetailsBean detailsBean1 = null;
            if (WebServiceEnum_Response != null && WebServiceEnum_Response.equalsIgnoreCase(("taskConversactionCaption"))) {
                JsonElement jelement = new JsonParser().parse(response);
                if (jelement.getAsJsonObject() != null) {
                    JsonObject jobject = jelement.getAsJsonObject();
                    final JSONObject jsonObject = new JSONObject(communicationBean.getEmail());
                    String result =jsonObject.getString("result_text");
                    Log.i(tab, "result ==> " + result);
                    if (jsonObject.getString("result_code").equalsIgnoreCase("0")) {
                        detailsBean1 = communicationBean.getTaskDetailsBean();
                        Log.i(tab, "detailsBean1.getCaption() "+detailsBean1.getCaption());
                        Log.i(tab, "detailsBean1.getTaskId() "+detailsBean1.getTaskId());
                        Log.i(tab, "detailsBean1.getSignalid() "+detailsBean1.getSignalid());
                        VideoCallDataBase.getDB(context).updateaccept("update taskDetailsInfo set caption='" + detailsBean1.getCaption() + "' where taskid='" + detailsBean1.getTaskId() + "' and signalid='" + detailsBean1.getSignalid() + "'");
                    }else if(jsonObject.getString("result_code").equalsIgnoreCase("1")){
                        showToast(result);
                    }
                }
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Appreference.printLog(tab, "ResponceMethod Exception : " + e.getMessage(), "WARN", null);
        }
    }

    @Override
    public void ErrorMethod(Object object) {

    }
}
