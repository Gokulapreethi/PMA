package com.myapplication3;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.myapplication3.DB.VideoCallDataBase;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.JsonRequestSender;
import json.ListMember;
import json.Loginuserdetails;
import json.NegativeValue;
import json.Usergroubdetails;
import json.WebServiceInterface;

public class GroupPercentageStatus extends AppCompatActivity implements View.OnClickListener, WebServiceInterface {
    String taskid, group_Id, sub_type, isProject;
    ArrayList<ListMember> arrayList, arrayList_1;
    ListView listView;
    TextView back, private_heading;
    Button submit, private_cancel;
    LayoutInflater inflater = null;
    Context context;
    ProgressDialog progress;
    PrivateMemberAdapter buddyArrayAdapter;
    ArrayList<String> NameList;
    ArrayList<String> PercentageList;
    ArrayAdapter arrayadapter;
    AppSharedpreferences appSharedpreferences;
    private final Handler handler = new Handler();
    ArrayList<String> chatUsers, chatUsersName;
//    String name,percentagecomplete;
static GroupPercentageStatus groupPercentageStatus;

    public static GroupPercentageStatus getInstance() {
        return groupPercentageStatus;
    }

    @Override
    public void ResponceMethod(final Object object) {
        Log.i("GroupPercentage", "InsideResponceMethod");
        try {
            Log.i("GroupPercentage", String.valueOf(object));
            cancelDialog();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    CommunicationBean opr = (CommunicationBean) object;
                    String s1 = opr.getEmail();
                    Log.i("GroupPercentage", "s1 value is " + s1);
                    try {
                        Gson g = new Gson();
                        String test = s1.toString();
                        JsonElement jelement = new JsonParser().parse(s1);
                        JsonArray jarray = jelement.getAsJsonArray();
                        for (int i = 0; i < jarray.size(); i++) {
                            JsonObject jobject = jarray.get(i).getAsJsonObject();
                            String firstname = String.valueOf(jobject.get("firstName"));
                            String lastname = String.valueOf(jobject.get("lastName"));
                            firstname = firstname.split("\"")[1];
                            lastname = lastname.split("\"")[1];
                            String percentagecomplete = String.valueOf(jobject.get("percentageCompleted"));
                            String name = firstname + " " + lastname;
                            Log.i("GroupPercentage", firstname);
                            Log.i("GroupPercentage", lastname);
                            Log.i("GroupPercentage", name);
                            Log.i("GroupPercentage", percentagecomplete);
                            NameList.add(name);
                            PercentageList.add(percentagecomplete);
                            final MyAdapter buddyArrayAdapter = new MyAdapter(context, NameList, PercentageList);
                            listView.setAdapter(buddyArrayAdapter);
                            buddyArrayAdapter.notifyDataSetChanged();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Unable to Login...", Toast.LENGTH_LONG).show();
                        Appreference.printLog("GroupPercentageStatus ResponceMethod ","Exception "+e.getMessage(),"WARN",null);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("GroupPercentageStatus ResPonce Method Main ","Exception "+e.getMessage(),"WARN",null);
        }
    }

    @Override
    public void ErrorMethod(Object object) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_percentage_status);
        try {
            context = this;
            Appreference.context_table.put("grouppercentagestatus", this);
            Appreference.is_swipe = true;
            listView = (ListView) findViewById(R.id.list_grp_mems);
            back = (TextView) findViewById(R.id.cancel);
            NameList = new ArrayList<String>();
            PercentageList = new ArrayList<String>();
            submit = (Button) findViewById(R.id.submit);
            private_cancel = (Button) findViewById(R.id.private_cancel);
            private_heading = (TextView) findViewById(R.id.txtView02);
            private_cancel.setOnClickListener(this);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            if (getIntent() != null) {
                taskid = getIntent().getStringExtra("taskid");
                group_Id = getIntent().getStringExtra("groupId");
                sub_type = getIntent().getStringExtra("subtype");
                isProject = getIntent().getStringExtra("isProject");
            }
           /* if (sub_type != null && sub_type.equalsIgnoreCase("private")) {
                submit.setVisibility(View.VISIBLE);
            }*/

            if (sub_type != null && sub_type.equalsIgnoreCase("private")) {
                private_heading.setText("Private Member List");
                submit.setVisibility(View.VISIBLE);
            } else if (sub_type != null && sub_type.equalsIgnoreCase("normal")) {
                private_heading.setText("Percentage Completion");
            }
            submit.setOnClickListener(this);
            arrayList = new ArrayList<>();
            arrayList_1 = new ArrayList<>();
            if (sub_type != null && sub_type.equalsIgnoreCase("normal")) {
                LoadBackGroundWebService();
            }
            if (isProject.equalsIgnoreCase("yes")) {
//                private_heading.setText("Private Members List");
                String ListofMem = VideoCallDataBase.getDB(context).getProjectListMembers(taskid);
                String ListofObser = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskObservers from taskHistoryInfo where taskId='" + taskid + "'");
                /*while (ListofMem.contains(",")) {
                    ListofMem = ListofMem.substring(",".length());
                }
                if (ListofMem.contains(",")) {
                    Log.i("ListMembers", "length is " + ListofMem.split(",").length);
                }*/
                if (ListofObser != null && ListofObser.contains("_")) {
                    ListofMem = ListofMem.concat(",");
                    ListofMem = ListofMem.concat(ListofObser);
                    Log.i("project_details", "ListofMem and ListofObser are " + ListofMem);
                }
                int counter = 0;
                for (int i = 0; i < ListofMem.length(); i++) {
                    if (ListofMem.charAt(i) == ',') {
                        counter++;
                    }
                    Log.d("project_details", "Task Mem's counter size is == " + counter);
                }
                for (int j = 0; j < counter + 1; j++) {
                    ListMember listMemberr_1 = new ListMember();
                    String Mem_name = ListofMem.split(",")[j];
                    Log.i("project_details", "Task Mem's and position == " + Mem_name + " " + j);
                    listMemberr_1.setUsername(Mem_name);
                    if (!listMemberr_1.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        arrayList.add(listMemberr_1);
                    }
                }
            } else {
                Log.i("project_details", "Task group_Id == " + group_Id);
                arrayList = VideoCallDataBase.getDB(context).getGroupmemberHistory("select * from groupmember where groupId='" + group_Id + "'");
            }

            Log.i("project_details", "arrayList size is " + arrayList.toString());
            if (arrayList.size() > 0) {
                for (int k = 0; k < arrayList.size(); k++)
                    Log.i("project_details", "listmember frst_last_name_s " + arrayList.get(k).getUsername());
            }
            if (arrayList.size() > 0) {
                for (ListMember listMember : arrayList) {
                    if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(listMember.getUsername())) {
                        int percent_1 = VideoCallDataBase.getDB(context).groupPercentageStatus(listMember.getUsername(), taskid);
                        Log.i("groupstatus", "percent_1 " + percent_1);
                        listMember.setCode(String.valueOf(percent_1));
                        if (isProject.equalsIgnoreCase("yes")) {
                            Log.i("project_details", "frst_lst_name 1 " + listMember.getUsername());
                            if (listMember.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                Log.i("project_details", "frst_lst_name 2 " + listMember.getUsername());
                                listMember.setFirstName(Appreference.loginuserdetails.getFirstName());
                                listMember.setLastName(Appreference.loginuserdetails.getLastName());
                            } else {
//                                String frst_last_name = VideoCallDataBase.getDB(context).getName(listMember.getUsername());
//                                Log.i("project_details", "frst_lst_name " + frst_last_name);
//                                Log.i("project_details", "frst_lst_name 3 " + listMember.getUsername());
//                                if (frst_last_name != null) {
                                listMember.setFirstName(VideoCallDataBase.getDB(context).getProjectParentTaskId("select firstname from contact where username='" + listMember.getUsername() + "'"));
                                listMember.setLastName(VideoCallDataBase.getDB(context).getProjectParentTaskId("select lastname from contact where username='" + listMember.getUsername() + "'"));
//                                }
                            }
                        }
                        Log.i("project_details", "frst_lst_name 4 " + listMember.getUsername());
                        arrayList_1.add(listMember);
                    }
                }
            }

            Log.i("groupstatus", "arrayList_1" + arrayList_1.size());
            if (sub_type != null && sub_type.equalsIgnoreCase("private")) {
               buddyArrayAdapter = new PrivateMemberAdapter(context, arrayList_1);
                listView.setAdapter(buddyArrayAdapter);
                buddyArrayAdapter.notifyDataSetChanged();
            } /*else if (sub_type != null && sub_type.equalsIgnoreCase("normal")) {
                private_heading.setText("Percentage Completion");
            }*/

//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                Collections.sort(arrayList_1, new CustomComparator());
//                buddyArrayAdapter.notifyDataSetChanged();
//            }
//        });
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
            if (sub_type != null && sub_type.equalsIgnoreCase("private")) {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        try {
                            ListMember item = arrayList.get(position);
                            String buddy_uri = item.getUsername();
                            Log.i("sipTest 1", "size :" + arrayList.size());
                            for (ListMember listMember : arrayList) {
                                Log.i("sipTest 1", "List Values " + listMember.getUsername());
                            }
                            if (!item.getIscheck()) {
                                Log.i("sipTest 1", "Inside True" + position + "  " + arrayList.get(position).getUsername());
                                item.setIscheck(true);

                            } else {
                                Log.i("sipTest 1", "Inside False" + position + "  " + arrayList.get(position).getUsername());
                                item.setIscheck(false);
                            }
                        buddyArrayAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("GroupPercentageStatus listView Click","Exception "+e.getMessage(),"WARN",null);
                        }

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("GroupPercentageStatus onCreate ","Exception "+e.getMessage(),"WARN",null);
        }

    }

    public void LoadBackGroundWebService() {
        try {
            showprogress("Loading listGroupTaskUsers");
            Log.d("test1", "appSharedpreferences.getString" + taskid);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("taskId", taskid));
            if (Appreference.jsonRequestSender == null) {
                JsonRequestSender jsonRequestParser = new JsonRequestSender();
                Appreference.jsonRequestSender = jsonRequestParser;
                jsonRequestParser.start();
            }
            Appreference.jsonRequestSender.listGroupTaskUsers(EnumJsonWebservicename.listGroupTaskUsers, nameValuePairs, this);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("GroupPercentageStatus LoadBackGroundWebservice","Exception "+e.getMessage(),"WARN",null);
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
                    e.printStackTrace();
                    Appreference.printLog("GroupPercentageStatus showProgress","Exception "+e.getMessage(),"WARN",null);
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
            Appreference.printLog("GroupPercentageStatus cancelDialog","Exception "+e.getMessage(),"WARN",null);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (Appreference.context_table.containsKey("grouppercentagestatus")) {
                Appreference.context_table.remove("grouppercentagestatus");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("GroupPercentageStatus onDestroy","Exception "+e.getMessage(),"WARN",null);
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
            if (id == R.id.private_cancel) {
                cancelClilckEvent();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("GroupPercentageStatus onClick Event ","Exception "+e.getMessage(),"WARN",null);
        }
    }

    private void cancelClilckEvent() {
        try {
            Intent i = new Intent();
            chatUsers = new ArrayList<String>();
            chatUsersName = new ArrayList<String>();
            i.putStringArrayListExtra("chatUsers", chatUsers);
            i.putStringArrayListExtra("chatUsersName", chatUsersName);
            setResult(RESULT_OK, i);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("GroupPercentageStatus cancelClickEvent ","Exception "+e.getMessage(),"WARN",null);
        }
    }

    private void submitClickEvent() {
        try {
            chatUsers = new ArrayList<String>();
            chatUsersName = new ArrayList<>();
            for (int position = 0; position < arrayList.size(); position++) {
                ListMember item = arrayList.get(position);
                if (item.getIscheck()) {
                    String buddy_uri = String.valueOf(item.getUserid());
                    String buddy_Name = item.getUsername();
                    chatUsers.add(buddy_uri);
                    chatUsersName.add(buddy_Name);
                    Log.i("taskConversation", "chat Selected user--> " + buddy_uri);
                }
            }
            Log.i("taskConversation", "chatUsers " + chatUsers);
            Log.i("taskConversation", "chatUsersName " + chatUsersName);
            if (chatUsersName != null && chatUsersName.size() > 0) {
                Intent i = new Intent();
                //                chatUsers = new ArrayList<String>();
                //                chatUsers = i.getStringArrayListExtra("chatUsers");
                i.putStringArrayListExtra("chatUsers", chatUsers);
                i.putStringArrayListExtra("chatUsersName", chatUsersName);
                setResult(RESULT_OK, i);
                finish();
            } else {
                Toast.makeText(context, "Please select the user", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("GroupPercentageStatus subMitClickEvent ","Exception "+e.getMessage(),"WARN",null);
        }
    }

    public class MyAdapter extends ArrayAdapter<String> {

        private final Context context;
        private final ArrayList<String> NameList1;
        private final ArrayList<String> PercentageList1;


        public MyAdapter(Context context, ArrayList<String> NameList, ArrayList<String> PercentageList) {
            super(context, R.layout.group_percent_status, NameList);
//            super(context,R.layout.group_percent_status,0,listMembers_1);
            this.context = context;
            this.NameList1 = NameList;
            this.PercentageList1 = PercentageList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                if (convertView == null) {
                    // 1. Create inflater
                    inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    // 2. Get rowView from inflater
                    convertView = inflater.inflate(R.layout.group_percent_status, null, false);
                }
                // 3. Get the two text view from the rowView
                final String contactBean = NameList1.get(position);
                final String percentagecomplete = PercentageList1.get(position);
                final TextView frst_lst_name = (TextView) convertView.findViewById(R.id.frst_lst_name);
                final TextView percent_status = (TextView) convertView.findViewById(R.id.percent_status);
                Log.i("GroupPercent", "name is " + contactBean);
                Log.i("GroupPercent", "percentage complete is " + percentagecomplete);
//                LoadBackGroundWebService();
                Log.i("GroupPercent", "name is " + contactBean);
                Log.i("GroupPercent", "percentage complete is " + percentagecomplete);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (contactBean != null) {
                            frst_lst_name.setText(contactBean);
                        } else {
//                            Toast.makeText(getApplicationContext(),"sorry",Toast.LENGTH_SHORT).show();
                        }
//                        percent_status.setText(percentagecomplete);
                        if (percentagecomplete.equalsIgnoreCase("null")) {
                            percent_status.setText("0%");
                        } else {
                            percent_status.setText(percentagecomplete + "" + "%");
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("GroupPercentageStatus MyAdapter GetView ","Exception "+e.getMessage(),"WARN",null);
            }
            return convertView;
        }
    }


    public class PrivateMemberAdapter extends ArrayAdapter<ListMember> {

        private final Context context;
        private final ArrayList<ListMember> listMembers;

        public PrivateMemberAdapter(Context context, ArrayList<ListMember> listMembers_1) {
            super(context, R.layout.group_percent_status, listMembers_1);

//            super(context,R.layout.group_percent_status,0,listMembers_1);

            this.context = context;
            this.listMembers = listMembers_1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                if (convertView == null) {
                    // 1. Create inflater
                    inflater = (LayoutInflater) context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    // 2. Get rowView from inflater
                    convertView = inflater.inflate(R.layout.group_percent_status, null, false);
                }

                // 3. Get the two text view from the rowView
                final ListMember contactBean = listMembers.get(position);
                TextView frst_lst_name = (TextView) convertView.findViewById(R.id.frst_lst_name);
                TextView percent_status = (TextView) convertView.findViewById(R.id.percent_status);

                String ft_lt_name = contactBean.getFirstName() + " " + contactBean.getLastName();
                frst_lst_name.setText(ft_lt_name);
                if (sub_type != null && sub_type.equalsIgnoreCase("normal")) {
                    percent_status.setText(contactBean.getCode() + "%");
                } else {
                    percent_status.setVisibility(View.GONE);
                }
                if (contactBean.getIscheck()) {
                    frst_lst_name.setTextColor(getResources().getColor(R.color.appcolor));
                    Log.i("sipTest", "adapter true " + contactBean.getIscheck() + " pos :" + position);
                } else {
                    frst_lst_name.setTextColor(getResources().getColor(R.color.black));
                    Log.i("sipTest", "adapter false " + contactBean.getIscheck() + " pos :" + position);
                }
                // 5. return rowView
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("GroupPercentageStatus PrivateMemBerAdapter Getview ","Exception "+e.getMessage(),"WARN",null);
            }
            return convertView;
        }
    }
}
