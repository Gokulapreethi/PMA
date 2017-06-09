package com.myapplication3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.myapplication3.Bean.ListAssignedTemplate;
import com.myapplication3.Bean.TaskDetailsBean;
import com.myapplication3.Bean.tamplatetask;
import com.myapplication3.DB.VideoCallDataBase;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.ListFromDetails;
import json.WebServiceInterface;

/**
 * Created by thirumal on 23-09-2016.
 */
public class AssignedTemplates extends Activity implements WebServiceInterface {
    TextView back;
    ArrayList<ListAssignedTemplate> listAssignedTemplates;
    ArrayList<String> taskNo;
    ListView listView;
    TemplateArrayAdapter templateArrayAdapter;
    private android.os.Handler handler = new android.os.Handler();
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assignedtemplates);
        listView = (ListView) findViewById(R.id.list_all_templates);
        taskNo = new ArrayList<String>();
        back = (TextView) findViewById(R.id.cancel);
        Bundle extras = getIntent().getExtras();
        String name = extras.getString("taskid");
        Log.i("AssignedTemplate", "-----> 1 " + name);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListAssignedTemplate listAssignedTemplate = listAssignedTemplates.get(position);
                tamplatetask tamplatetask = new tamplatetask();
                tamplatetask = listAssignedTemplate.getTask();
                Log.i("AssignedTemplate", "tamplatetask.getId() " + tamplatetask.getId());
                TaskDetailsBean taskDetailsBean = VideoCallDataBase.getDB(getApplicationContext()).getTaskContent("select * from taskDetailsInfo where taskId='" + tamplatetask.getId() + "'");
                Log.i("AssignedTemplate", String.valueOf(position));
                Intent intent = new Intent(getApplicationContext(), NewTaskConversation.class);
                String name = VideoCallDataBase.getDB(getApplicationContext()).getTemplateTouserName(taskDetailsBean.getToUserId());
                if(name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                     taskDetailsBean.setFromUserName(name);
                     name = VideoCallDataBase.getDB(getApplicationContext()).getTemplateTouserName(taskDetailsBean.getFromUserId());
                }
                taskDetailsBean.setOwnerOfTask(Appreference.loginuserdetails.getUsername());
                taskDetailsBean.setTaskReceiver(name);
                taskDetailsBean.setToUserName(name);
                String task_name = VideoCallDataBase.getDB(getApplicationContext()).getProjectParentTaskId("select taskName from taskHistoryInfo where taskId='" + taskDetailsBean.getTaskId() + "'");
                taskDetailsBean.setTaskName(task_name);
                Log.i("AssignedTemplate", "Ownar " + taskDetailsBean.getOwnerOfTask());
                Log.i("AssignedTemplate", "getFromUserName " + taskDetailsBean.getFromUserName());
                Log.i("AssignedTemplate", "toUserName " + taskDetailsBean.getToUserName());
                Log.i("AssignedTemplate", "taskNo " + taskDetailsBean.getTaskNo());
                Log.i("AssignedTemplate", "WebtaskNo " + taskDetailsBean.getTaskId());
                Log.i("AssignedTemplate", "toUserId " + taskDetailsBean.getToUserId());
                Log.i("AssignedTemplate", "taskname " + taskDetailsBean.getTaskName());
                Log.i("AssignedTemplate", "TaskOwner " + taskDetailsBean.getOwnerOfTask());
                Log.i("AssignedTemplate", "TaskType " + taskDetailsBean.getTaskType());
                Log.i("AssignedTemplate", "Task Receiver " + taskDetailsBean.getTaskReceiver());
                intent.putExtra("Ownar", taskDetailsBean.getOwnerOfTask());
                intent.putExtra("userName", taskDetailsBean.getFromUserName());
                intent.putExtra("toUserName", taskDetailsBean.getToUserName());
                intent.putExtra("taskNo", taskDetailsBean.getTaskNo());
                intent.putExtra("webtaskNo", taskDetailsBean.getTaskId());
                intent.putExtra("toUserId", taskDetailsBean.getToUserId());
                intent.putExtra("task", "Templateview");
                intent.putExtra("taskname", taskDetailsBean.getTaskName());
                intent.putExtra("taskOwner", taskDetailsBean.getOwnerOfTask());
                intent.putExtra("taskType", taskDetailsBean.getTaskType());
                intent.putExtra("taskReceiver", taskDetailsBean.getTaskReceiver());
                intent.putExtra("chvalue", "1");
                intent.putExtra("taskHistoryBean", taskDetailsBean);
                startActivity(intent);

            }
        });
        listAssignedTemplates = new ArrayList<ListAssignedTemplate>();
        Appreference.is_swipe = true;
//
//        String query = "select * from taskDetailsInfo where (fromUserId='" + userName + "' or toUserId='" + userName + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "' group by taskNo";
//        Log.d("task", "query   " + query);
//
//        taskDetailsBeen = VideoCallDataBase.getDB(context).getTaskHistory(query);
//
//        Log.d("task","task list size = "+ taskDetailsBeen.size());

//        String var = extras.getString("taskid");
        showprogress();
        List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(1);
        nameValuePairs1.add(new BasicNameValuePair("tamplateId", name));
        Appreference.jsonRequestSender.listAssignTemplate(EnumJsonWebservicename.listAssignTemplate, nameValuePairs1, AssignedTemplates.this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("template ", "to new task conversation....1");
                Intent intent=new Intent(getApplication(),NewTaskConversation.class);

                Log.i("template ", "to new task conversation....2");
                startActivity(intent);
            }
        });*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Appreference.context_table.containsKey("assignedtemplates")) {
            Appreference.context_table.remove("assignedtemplates");
        }
    }

    private void showprogress() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("AssignedTemplate", "inside showProgressDialog");

                    progress = new ProgressDialog(AssignedTemplates.this);
                    progress.setCancelable(false);

                    progress.setMessage("List of Assigned templates...");

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

    public void cancelDialog() {
        try {
            if (progress != null && progress.isShowing()) {
                Log.i("AssignedTemplate", "--progress bar end-----");
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
                Log.i("AssignedTemplate respon", "Assigned Templates  ResponceMethod");
                CommunicationBean bean = (CommunicationBean) object;
                try {
                    cancelDialog();
                    String str = bean.getEmail();
                    Log.i("AssignedTemplate", "str   == " + str);
                    String s2 = bean.getFirstname();
                    Log.d("AssignedTemplate", "name   == " + s2);
                    Type collectionType = new TypeToken<List<ListAssignedTemplate>>() {
                    }.getType();
                    List<ListAssignedTemplate> lcs = (List<ListAssignedTemplate>) new Gson().fromJson(str, collectionType);
                    Log.i("AssignedTemplate String", "Value--->1" + lcs.size());
                    for (int i = 0; i < lcs.size(); i++) {
                        Log.i("AssignedTemplate", "Value--->2" + lcs.get(i));
                        ListAssignedTemplate listUserGroupObject = lcs.get(i);
                        Log.i("AssignedTemplate", "Value--->3" + listUserGroupObject.getTask().getId());
                        listAssignedTemplates.add(listUserGroupObject);
                        Log.i("AssignedTemplate", "Value--->4" + listAssignedTemplates);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("AssignedTemplate", "Value--->5" + listAssignedTemplates);
                templateArrayAdapter = new TemplateArrayAdapter(getApplicationContext(), listAssignedTemplates);
                Log.i("AssignedTemplate", "Value--->6" + listAssignedTemplates);
                listView.setAdapter(templateArrayAdapter);
                Log.i("AssignedTemplate", "Value--->7" + listAssignedTemplates);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("AssignedTemplate", "Value--->8" + listAssignedTemplates);
                        templateArrayAdapter.notifyDataSetChanged();
                        Log.i("AssignedTemplate", "Value--->9" + listAssignedTemplates);
                    }
                });
            }
        });

    }

    @Override
    public void ErrorMethod(Object object) {

    }

    public class TemplateArrayAdapter extends ArrayAdapter<ListAssignedTemplate> {

        ArrayList<ListAssignedTemplate> arrayBuddyList;
        LayoutInflater inflater = null;
        Context adapContext;

        public TemplateArrayAdapter(Context context, ArrayList<ListAssignedTemplate> buddyList1) {
            super(context, R.layout.template_history_row, buddyList1);
            // TODO Auto-generated constructor stub
            this.arrayBuddyList = buddyList1;
            Log.d("task", "task list size inside = " + arrayBuddyList.size());
            adapContext = context;
        }

        public View getView(int pos, View conView, ViewGroup group) {
            try {
                if (conView == null) {
                    inflater = (LayoutInflater) adapContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    conView = inflater.inflate(R.layout.template_history_row, null,
                            false);
                }
                final ListAssignedTemplate listAssignedTemplates = arrayBuddyList.get(pos);
                tamplatetask tamplatetask = new tamplatetask();
                tamplatetask = listAssignedTemplates.getTask();
                ListFromDetails listFromDetails = new ListFromDetails();
                listFromDetails = tamplatetask.getToUser();
                TextView templatename = (TextView) conView.findViewById(R.id.templatename);
                TextView task_giver = (TextView) conView.findViewById(R.id.task_giver);
                TextView task_taker = (TextView) conView.findViewById(R.id.task_taker);
                TextView status = (TextView) conView.findViewById(R.id.template_status);
                ImageView state1 = (ImageView) conView.findViewById(R.id.state1);
                templatename.setText(tamplatetask.getName());
                state1.setVisibility(View.VISIBLE);
                task_giver.setText("Task Giver  :Me");
                task_taker.setText("Task Taker  :" + listFromDetails.getFirstName() + " " + listFromDetails.getLastName());
                status.setVisibility(View.VISIBLE);
                status.setText(tamplatetask.getStatus());
//                Log.i("task","task Name = "+ listFromDetailses.getFirstName()+" "+listFromDetailses.getLastName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return conView;
        }
    }
}
