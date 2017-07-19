package com.ase.Forms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.Appreference;
import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;
import com.ase.R;
import com.ase.RandomNumber.Utility;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.app.MyBuddy;

import java.text.SimpleDateFormat;
import java.util.*;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.WebServiceInterface;

/**
 * Created by vignesh on 2/2/2017.
 */
public class FormAccessControl extends Activity implements WebServiceInterface{

    ListView list1, observer;
    String formId, taskId , touserid, accesscode;
    String takerlist[], observerlist[];
    ArrayList<String> userlist = new ArrayList<>();
    private Handler handler = new Handler();
    Context context;
    ProgressDialog progress;
    ArrayList<FormAccessBean> ListofMembers = new ArrayList<FormAccessBean>();
    ArrayList<FormAccessBean> strings = new ArrayList<FormAccessBean>();
    ArrayList<FormAccessBean> strings1 = new ArrayList<FormAccessBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fileaccesscontrol);
        list1 = (ListView) findViewById(R.id.list1);
        observer = (ListView) findViewById(R.id.observer);


        TextView formname = (TextView) findViewById(R.id.headername);
        TextView back = (TextView) findViewById(R.id.back);
        TextView send = (TextView) findViewById(R.id.send);
        formId = getIntent().getExtras().getString("formid");
        taskId = getIntent().getExtras().getString("taskid");
        touserid = getIntent().getExtras().getString("touserid");
        formname.setText(getIntent().getExtras().getString("formname"));

        handler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    java.util.List<NameValuePair> tagNameValuePairs = new ArrayList<NameValuePair>();
                    tagNameValuePairs.add(new BasicNameValuePair("formId", formId));
                    tagNameValuePairs.add(new BasicNameValuePair("taskId", taskId));

                    Appreference.jsonRequestSender.getFormFieldAndValues(EnumJsonWebservicename.getFormAccessForTaskId, tagNameValuePairs, FormAccessControl.this);
                }catch (Exception e){ e.printStackTrace(); }
            }
        });

        TaskDetailsBean taskDetailsBean = VideoCallDataBase.getDB(getApplicationContext()).getFormContent("select * from taskHistoryInfo where taskId='" + taskId + "'");

        Log.i("Forms", "type " + taskDetailsBean.getTaskType());
        Log.i("Forms", "receiver " + taskDetailsBean.getTaskReceiver());
        takerlist = new String[500];
        observerlist = new String[500];
        if (taskDetailsBean.getTaskType().equalsIgnoreCase("Individual")) {
            takerlist[0] = taskDetailsBean.getTaskReceiver();
        } else {
            takerlist = taskDetailsBean.getGroupTaskMembers().split(",");
        }

        if (taskDetailsBean.getTaskObservers() != null) {
            if (taskDetailsBean.getTaskObservers().contains(",")) {
                observerlist = taskDetailsBean.getTaskObservers().split(",");
            } else {
                observerlist[0] = taskDetailsBean.getTaskObservers();
            }
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        for (int i = 0; i < takerlist.length; i++) {
            if(takerlist[i] != null) {
                FormAccessBean formAccessBean = new FormAccessBean();
                formAccessBean.setFormId(formId);
                formAccessBean.setTaskId(taskId);
                formAccessBean.setGiver(taskDetailsBean.getOwnerOfTask());
                formAccessBean.setMemberName(VideoCallDataBase.getDB(context).getName(takerlist[i]));
                formAccessBean.setTouserId(String.valueOf(VideoCallDataBase.getDB(context).getParentTaskId("select userid from contact where username='" + takerlist[i] + "'")));
                formAccessBean.setType(takerlist[i]);
                if(VideoCallDataBase.getDB(context).getAccessType(taskId,formId,takerlist[i]) != null)
                    formAccessBean.setAccessMode(VideoCallDataBase.getDB(context).getAccessType(taskId,formId,takerlist[i]));
                else
                    formAccessBean.setAccessMode(accesscode);
                strings.add(formAccessBean);
                userlist.add(takerlist[i]);
            }
        }

        for (int i = 0; i < observerlist.length; i++) {
            if(observerlist[i] != null) {
                FormAccessBean formAccessBean = new FormAccessBean();
                formAccessBean.setFormId(formId);
                formAccessBean.setTaskId(taskId);
                formAccessBean.setGiver(taskDetailsBean.getOwnerOfTask());
                formAccessBean.setMemberName(VideoCallDataBase.getDB(context).getName(observerlist[i]));
                formAccessBean.setType(observerlist[i]);
                formAccessBean.setTouserId(String.valueOf(VideoCallDataBase.getDB(context).getParentTaskId("select userid from contact where username='" + takerlist[i] + "'")));
                if(VideoCallDataBase.getDB(context).getAccessType(taskId,formId,observerlist[i]) != null)
                    formAccessBean.setAccessMode(VideoCallDataBase.getDB(context).getAccessType(taskId,formId,observerlist[i]));
                else
                    formAccessBean.setAccessMode(accesscode);
                strings1.add(formAccessBean);
                userlist.add(observerlist[i]);
            }
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Log.i("Forms", "Send size " + strings.size());
                        showprogress();
                        JSONObject jsonObject = new JSONObject();

                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("id", Integer.parseInt(taskId));
//                    jsonObject1.put("id", "14815");

                        jsonObject.put("task", jsonObject1);

                        JSONObject jsonObject2 = new JSONObject();
                        jsonObject2.put("id", Integer.parseInt(formId));
//                    jsonObject1.put("id", "14815");

                        jsonObject.put("form", jsonObject2);

                        JSONObject jsonObject3 = new JSONObject();
                        jsonObject3.put("id", Appreference.loginuserdetails.getId());
//                    jsonObject1.put("id", "14815");

                        jsonObject.put("from", jsonObject3);
                    int j=0;
                    JSONObject[] filedlist = new JSONObject[strings.size()+strings1.size()];
                    for (int i = 0; i < strings.size(); i++) {
                        Toast.makeText(getApplicationContext(), strings.get(i).getAccessMode(), Toast.LENGTH_SHORT).show();
                        VideoCallDataBase.getDB(context).UpdateOrInsertFormAccess(strings.get(i));
                        filedlist[i] = new JSONObject();
                        filedlist[i].put("accessMode", strings.get(i).getAccessMode());
                        filedlist[i].put("to", Integer.parseInt(strings.get(i).getTouserId()));
                        j++;
                    }
                    for (int i = 0; i < strings1.size(); i++) {
                        Toast.makeText(getApplicationContext(), strings1.get(i).getAccessMode(), Toast.LENGTH_SHORT).show();
                        VideoCallDataBase.getDB(context).UpdateOrInsertFormAccess(strings1.get(i));
                        filedlist[i+strings.size()] = new JSONObject();
                        filedlist[i].put("accessMode", strings1.get(i).getAccessMode());
                        filedlist[i].put("to", Integer.parseInt(strings1.get(i).getTouserId()));
                        j++;
                    }
                    JSONArray listpostfiles_object = new JSONArray();
                    for (int i1 = 0; i1 < j; i1++) {
                        listpostfiles_object.put(filedlist[i1]);
                    }
                    jsonObject.put("toUsers", listpostfiles_object);
                        Appreference.jsonRequestSender.SaveFormAccessRestrictions(EnumJsonWebservicename.saveFormAccessRestrictions,jsonObject,FormAccessControl.this);

                    String result = composeChatXML(strings, strings1);
                    sendMultiInstantMessage(result,userlist);

                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        TextView notaker = (TextView) findViewById(R.id.notaker);
        TextView noobserver = (TextView) findViewById(R.id.noobserver);

        Log.i("Forms", "taker size " + strings.size());
        Log.i("Forms", "observer size " + strings1.size());
        if (strings.size() == 0) {
            notaker.setVisibility(View.VISIBLE);
            list1.setVisibility(View.GONE);
        }
        if (strings1.size() == 0) {
            noobserver.setVisibility(View.VISIBLE);
            observer.setVisibility(View.GONE);
        }
        CustomListAdapter formsListAdapter = new CustomListAdapter(this, strings);
        list1.setAdapter(formsListAdapter);
        formsListAdapter.notifyDataSetChanged();

        CustomListAdapter formsListAdapter1 = new CustomListAdapter(this, strings1);
        observer.setAdapter(formsListAdapter1);
        formsListAdapter1.notifyDataSetChanged();

    }


    public class CustomListAdapter extends ArrayAdapter<FormAccessBean> {

        private final Activity context;
        private final ArrayList<FormAccessBean> itemname;

        public CustomListAdapter(Activity context, ArrayList<FormAccessBean> itemname) {
            super(context, R.layout.accesstype, itemname);
            // TODO Auto-generated constructor stub

            this.context = context;
            this.itemname = itemname;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.accesstype, null, true);
            Log.i("Forms", "size 1 " + itemname.size());


            TextView txtTitle = (TextView) rowView.findViewById(R.id.username);
            final Spinner access = (Spinner) rowView.findViewById(R.id.spinner6);

            txtTitle.setText(itemname.get(position).getMemberName());
            if(itemname.get(position).getAccessMode() != null) {
                if (itemname.get(position).getAccessMode().equalsIgnoreCase("Read Only")) {
                    access.setSelection(0);
                } else if (itemname.get(position).getAccessMode().equalsIgnoreCase("Add New")) {
                    access.setSelection(1);
                } else if (itemname.get(position).getAccessMode().equalsIgnoreCase("Edit My Values")) {
                    access.setSelection(2);
                } else if (itemname.get(position).getAccessMode().equalsIgnoreCase("Edit All Values")) {
                    access.setSelection(3);
                } else if (itemname.get(position).getAccessMode().equalsIgnoreCase("Complete Access")) {
                    access.setSelection(4);
                } else {
                    access.setSelection(4);
                }
            } else {
                access.setSelection(4);
            }

            access.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position1, long id) {
                    String text = access.getSelectedItem().toString();
                    itemname.get(position).setAccessMode(text);
                    Log.i("Forms", "selectedtext" + text);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            return rowView;
        }
    }

    public String composeChatXML(ArrayList<FormAccessBean> s1, ArrayList<FormAccessBean> s2) {
        StringBuffer buffer = new StringBuffer();
        String quotes = "\"";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = dateFormat.format(new Date());
        FormAccessBean cmbean = s1.get(0);
        try {
            buffer.append("<?xml version=\"1.0\"?>"
                    + "<FormAccess><FormAccessInfo signalId=" + quotes + Utility.getSessionID() + quotes + " dateTime=" + quotes + dateTime + quotes
                    + " taskId=" + quotes + cmbean.getTaskId() + quotes +
                    " formId=" + quotes + cmbean.getFormId() + quotes +
                    " taskGiver=" + quotes + cmbean.getGiver() + quotes + ">");

            for (int i = 0; i < s1.size(); i++) {
                buffer.append("<MemberAccess formAccessId=" + quotes + i + quotes);
                buffer.append(" memberName=" + quotes + s1.get(i).getType() + quotes);
                buffer.append(" accessMode=" + quotes + s1.get(i).getAccessMode() + quotes + "/>");
            }

            for (int i = 0; i < s2.size(); i++) {
                buffer.append("<MemberAccess formAccessId=" + quotes + i + quotes);
                buffer.append(" memberName=" + quotes + s2.get(i).getType() + quotes);
                buffer.append(" accessMode=" + quotes + s2.get(i).getAccessMode() + quotes + "/>");
            }

            buffer.append("</FormAccessInfo></FormAccess>");


        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            return buffer.toString();
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

    public void sendMultiInstantMessage(String msgBody, ArrayList<String> userlist) {

        for (String name : userlist) {
            Log.i("task observer", "observer 1 " + name);
            Log.i("task observer", "MainActivity.account.buddyList.size()" + MainActivity.account.buddyList.size());
        }

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
                    Log.i("task response", "NewTaskConversation ResponceMethod");
                    CommunicationBean bean = (CommunicationBean) object;
                    String values = bean.getEmail();
                    JSONObject jsonObject = new JSONObject(bean.getEmail());
                    cancelDialog();
                    if (values.contains("values has been updated")) {
                        Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i("response","vlaue "+values);
                        accesscode = (String) jsonObject.get("accessMode");
                    }
                } catch (Exception e){ e.printStackTrace(); }
            }
        });
    }

    @Override
    public void ErrorMethod(Object object) {

    }
}
