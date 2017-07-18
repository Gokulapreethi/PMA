package com.myapplication3;

/**
 * Created by saravanan on 2/1/2017.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.myapplication3.Bean.ProjectDetailsBean;
import com.myapplication3.Bean.TaskDetailsBean;
import com.myapplication3.DB.VideoCallDataBase;
import com.myapplication3.RandomNumber.Utility;

import org.json.JSONArray;
import org.json.JSONObject;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.app.MyBuddy;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.WebServiceInterface;

public class AddTaskReassign extends Activity implements View.OnClickListener, WebServiceInterface {

    static AddTaskReassign addObserver;
    private final Handler handler = new Handler();
    Button submit, cancel;
    String isProject = "";
    TextView title;
    ListView listView;
    ArrayList<ContactBean> contactList, listofProjectMembers;
    Context context;
    BuddyArrayAdapter buddyArrayAdapter;
    String taskId, taskNo, taskName, newtaskId, toId, toUserName, taskTime = null, dateforrow,/*newSubType,*/
            newTaskNo,/*newTaskDescription,*/
            dateforDate, percentage = "0";
    ArrayList<String> chatUsers, chatUsersName, listOfObservers;
    String taskType, groupname, taskReceiver, projectid, listMembers, RemoveUser, isReassignNote, jobcodeno, activitycode;
    boolean isTemplate = false, isProjectFromOracle, Self_Assign;
    String SelectedUserList = null, SelectedUserList1 = "";
    String SelectedUsersId = null;
    ProgressDialog dialog;
    int Clickposition;
    //For ASE
    String[] dates;
    private NumberPicker datePicker, hourPicker, minutePicker, am_pmPicker;

    private String quotes = "\"";
    ArrayList<TaskDetailsBean> taskDetailsBean = new ArrayList<>();
    String[] observerList;
    AppSharedpreferences appSharedpreferences;
    TaskDetailsBean detailsBean;
    String listPosition = null;

    public static AddTaskReassign getInstance() {
        return addObserver;
    }

    public void showToast(final String result) {
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddTaskReassign showToast ", "Exception " + e.getMessage(), "WARN", null);
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
            Appreference.context_table.put("AddTaskReassign", this);
            appSharedpreferences = AppSharedpreferences.getInstance(context);
            submit = (Button) findViewById(R.id.submit);
            cancel = (Button) findViewById(R.id.cancel);
            listView = (ListView) findViewById(R.id.post_list);

            submit.setOnClickListener(this);
            cancel.setOnClickListener(this);
            taskId = getIntent().getExtras().getString("taskId");
            taskType = getIntent().getExtras().getString("taskType");
            isReassignNote = getIntent().getExtras().getString("Note");
            isProjectFromOracle = getIntent().getBooleanExtra("isProjectFromOracle1", false);
            Self_Assign = getIntent().getBooleanExtra("selfAssign", false);
            jobcodeno = getIntent().getStringExtra("jobcodeno");
            activitycode = getIntent().getStringExtra("activitycode");
            Clickposition = getIntent().getIntExtra("Clickposition", 0);
            Log.i("ws123", "  AssignTask isProjectFromOracle ====>" + isProjectFromOracle);
            Log.i("add observer", "task type " + taskType);
            if (taskType != null)
                if (taskType.equalsIgnoreCase("Group"))
                    groupname = getIntent().getExtras().getString("groupname");
                else
                    taskReceiver = getIntent().getExtras().getString("taskReceiver");
            Log.i("add observer", "task type!**  " + taskReceiver);
            detailsBean = (TaskDetailsBean) getIntent().getSerializableExtra("taskBean");
            if (getIntent().getExtras().getString("from") != null && getIntent().getExtras().getString("from").toString().equalsIgnoreCase("templatetask")) {
                Log.e("template", taskId + "assigned");
                taskName = getIntent().getExtras().getString("taskName");
                taskNo = getIntent().getExtras().getString("taskNo");
                newTaskNo = getFileName();

                if (detailsBean != null)
                    Log.i("ws123", "Bean============>55 11" + detailsBean.getParentTaskId() + "getProjectId=========>" + detailsBean.getProjectId());
                else
                    Log.i("ws123", "Bean null============> 5511");


                //            newSubType=getIntent().getExtras().getString("subType");
                //            newTaskDescription=getIntent().getExtras().getString("taskDescription");
                title = (TextView) findViewById(R.id.txtView01);
                if (isProjectFromOracle)
                    title.setText("Job Card No : " + jobcodeno + "\nActivity Code : " + activitycode);
                else
                    title.setText("Assign Task To");
                //submit.setBackgroundResource(android.R.drawable.btn_default);
                //submit.setText("Assign");
                isTemplate = true;
            } else if (getIntent().getExtras().getString("Taker") != null && getIntent().getExtras().getString("Taker").toString().equalsIgnoreCase("Assigned Task")) {
                title = (TextView) findViewById(R.id.txtView01);
                isProject = getIntent().getExtras().getString("isProject");
                if (getIntent().getExtras().getString("isProject") != null && getIntent().getExtras().getString("isProject").toString().equalsIgnoreCase("Yes")) {
                    if (isProjectFromOracle)
                        title.setText("Job Card No : " + jobcodeno + "\nActivity Code : " + activitycode);
                    else
                        title.setText("Assign To");
                } else {
                    title.setText("Assign Task To");
                }
            }
            if (isProjectFromOracle)
                title.setText("Job Card No : " + jobcodeno + "\nActivity Code : " + activitycode);
            else
                title.setText("Assign To");
            Log.i("add observer", "group name --> 0 " + groupname);
        /*handler.post(new Runnable() {
            @Override
            public void run() {*/
            if (!isTemplate) {
                String Query = "Select * from taskHistoryInfo where taskId ='" + taskId + "';";
                taskDetailsBean = VideoCallDataBase.getDB(context).getTaskHistoryInfo(Query);
            } else {
                Log.i("add observer", "group name --> 1 " + groupname);
                contactList = VideoCallDataBase.getDB(context).getContact(Appreference.loginuserdetails.getUsername());
            }
            if (taskType != null) {
                if (taskType.equalsIgnoreCase("Group")) {
                    Log.i("add observer", "group name --> 2 " + groupname);
                    contactList = VideoCallDataBase.getDB(context).getGroupContact(Appreference.loginuserdetails.getUsername(), groupname);
                    Log.i("add observer", "Contact list --> 0 " + contactList.size());
                } else {
                    contactList = VideoCallDataBase.getDB(context).getContact(Appreference.loginuserdetails.getUsername());
                }
            }

            if (taskDetailsBean.size() > 0) {
                TaskDetailsBean taskDetailsBean1 = taskDetailsBean.get(taskDetailsBean.size() - 1);
                if (taskDetailsBean1.getTaskObservers() != null && taskDetailsBean1.getTaskObservers().contains(",")) {
                    observerList = taskDetailsBean1.getTaskObservers().split(",");
                    Log.i("add observer", "task observer size" + observerList.length);
                    for (ContactBean contactBean : contactList) {

                        for (String taskDetailsBean2 : observerList) {
                            Log.i("task observer", contactBean.getUsername() + "is equal ?" + taskDetailsBean2);
                            if (contactBean.getUsername().equalsIgnoreCase(taskDetailsBean2)) {
                                Log.i("task observer", contactBean.getUsername() + "equal" + taskDetailsBean2);
                                contactBean.setIscheck(true);
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
            String query = "select * from taskDetailsInfo where loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + taskId + "' and mimeType='observer' order by id DESC LIMIT 1";
            ArrayList<TaskDetailsBean> taskDetailsBeenList = VideoCallDataBase.getDB(context).getTaskHistory(query);
            if (isProjectFromOracle) {

                String SubTaskToUsers, ProjectToUsers;
                // remove task receiver and owner name in reassign task member list and add the owername in from note
//        Log.i("ReassignNote", "getOwnerOfTask " + detailsBean.getOwnerOfTask() + " " + detailsBean.getTaskReceiver());
                Log.i("ReassignTask", "isReassignNote ! " + isReassignNote);
                ArrayList<ContactBean> tempContactList = new ArrayList<>();
                if (isProjectFromOracle) {
                    contactList.clear();
                    String parentTaskId = VideoCallDataBase.getDB(context).getProjectParentTaskId("select parentTaskId from projectHistory where projectId='" + detailsBean.getProjectId() + "' and taskId='" + taskId + "'");
                    SubTaskToUsers = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskMemberList from projectHistory where taskId='" + taskId + "'");
                    Log.i("ReassignTask", "----------> parentTaskId " + parentTaskId);
                    ProjectToUsers = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskMemberList from projectHistory where taskId='" + parentTaskId + "'");
                    if (ProjectToUsers.contains(",")) {
                        int counter = 0;
                        for (int i = 0; i < ProjectToUsers.length(); i++) {
                            if (ProjectToUsers.charAt(i) == ',') {
                                counter++;
                            }
                            Log.d("ReassignTask", "Task Mem's counter size is == " + counter);
                        }
                        for (int j = 0; j < counter + 1; j++) {
                            ContactBean contactBean = new ContactBean();
                            String Mem_name = ProjectToUsers.split(",")[j];
                            if (Mem_name != null && !Mem_name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                contactBean = VideoCallDataBase.getDB(context).getContactObject(Mem_name);
                            } else {
                                ContactBean contactBean1 = new ContactBean();
                                contactBean1.setEmail(Appreference.loginuserdetails.getEmail());
                                contactBean1.setFirstname(Appreference.loginuserdetails.getFirstName());
                                contactBean1.setLastname(Appreference.loginuserdetails.getLastName());
                                contactBean1.setUserid(Appreference.loginuserdetails.getId());
                                contactBean1.setUsername(Appreference.loginuserdetails.getUsername());
                                if (contactBean.getUsername() != null)
                                    contactList.add(contactBean1);
                            }
                            Log.i("ReassignTask", "Task Mem's and position == " + Mem_name + " " + contactBean.getUsername());
                            if (contactBean.getUsername() != null)
                                contactList.add(contactBean);
                        }
                    } else {
                        ContactBean contactBean = null;
                        if (ProjectToUsers != null && !ProjectToUsers.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            contactBean = VideoCallDataBase.getDB(context).getContactObject(ProjectToUsers);
                            if (contactBean.getUsername() != null)
                                contactList.add(contactBean);
                        } else {
                            ContactBean contactBean1 = new ContactBean();
                            contactBean1.setEmail(Appreference.loginuserdetails.getEmail());
                            contactBean1.setFirstname(Appreference.loginuserdetails.getFirstName());
                            contactBean1.setLastname(Appreference.loginuserdetails.getLastName());
                            contactBean1.setUserid(Appreference.loginuserdetails.getId());
                            contactBean1.setUsername(Appreference.loginuserdetails.getUsername());
                            if (contactBean.getUsername() != null)
                                contactList.add(contactBean1);
                        }
//                     contactBean = VideoCallDataBase.getDB(context).getContactObject(ProjectToUsers);

                    }
                }
            }

            Log.i("ReassignNote", "isReassignNote ! " + isReassignNote);
            ArrayList<ContactBean> tempContactList = new ArrayList<>();
            if ((isReassignNote != null && isReassignNote.equalsIgnoreCase("reassignnote")) && (detailsBean.getTaskReceiver() != null && !detailsBean.getOwnerOfTask().equalsIgnoreCase(detailsBean.getTaskReceiver()))) {
                tempContactList.addAll(contactList);
                ContactBean contactBean1 = new ContactBean();
                contactBean1.setEmail(Appreference.loginuserdetails.getEmail());
                contactBean1.setFirstname(Appreference.loginuserdetails.getFirstName());
                contactBean1.setLastname(Appreference.loginuserdetails.getLastName());
                contactBean1.setUserid(Appreference.loginuserdetails.getId());
                contactBean1.setUsername(Appreference.loginuserdetails.getUsername());
                contactList.add(contactBean1);
            } else {
                tempContactList.addAll(contactList);

                for (ContactBean contactBean : tempContactList) {
                    Log.i("ReassignNote", "isReassignNote1 " + contactBean.getUsername());
                    if (taskReceiver != null && taskReceiver.equalsIgnoreCase(contactBean.getUsername())) {
                        contactList.remove(contactBean);
                    } else if (Appreference.loginuserdetails.getUsername().equalsIgnoreCase(contactBean.getUsername())) {
                        contactList.remove(contactBean);
                    }
                }
            }
            Collections.sort(contactList, new CustomComparator());
//            ArrayList<ContactBean> oracleProjectMembers;


            buddyArrayAdapter = new BuddyArrayAdapter(context, contactList);
            listView.setAdapter(buddyArrayAdapter);
            buddyArrayAdapter.notifyDataSetChanged();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Collections.sort(contactList, new CustomComparator());
                    buddyArrayAdapter.notifyDataSetChanged();
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ContactBean item = contactList.get(position);
                    String buddy_uri = item.getUsername();
                    if (isProjectFromOracle) {
                        if (!item.getIscheck()) {
                            Log.e("sipTest", "Inside True 0");
                            item.setIscheck(true);
                        } else {
                            Log.e("sipTest", "Inside False 0");
                            item.setIscheck(false);
                        }
                    } else {
                        if (listPosition != null && listPosition.equalsIgnoreCase(String.valueOf(position))) {
                            listPosition = String.valueOf(position);
                        } else {
                            listPosition = String.valueOf(position);
                        }

                        for (int i = 0; i < contactList.size(); i++) {
                            if (listPosition != null && !listPosition.equalsIgnoreCase(String.valueOf(i))) {
                                contactList.get(i).setIscheck(false);
                            }
                            Log.e("sipTest", "Inside True *");
                        }
                        if (!contactList.get(position).getIscheck())
                            contactList.get(position).setIscheck(true);
                        else
                            contactList.get(position).setIscheck(false);
                        Log.e("sipTest", "Inside True * *");
                    }
                    buddyArrayAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddTaskReassign OnCreate Method ", "Exception " + e.getMessage(), "WARN", null);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            isTemplate = false;
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddTaskReassign onDestroy ", "Exception " + e.getMessage(), "WARN", null);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            int id = v.getId();

            if (id == R.id.submit) {
                if (isProject != null && isProject.equalsIgnoreCase("yes")) {
                    String UserName = "";
                    for (int position = 0; position < contactList.size(); position++) {
                        ContactBean item = contactList.get(position);
                        if (item.getIscheck()) {
                            UserName = item.getFirstname() + " " + item.getLastname();
                            break;
                        }
                    }
                    submitClickEvent();

                  /*  AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
                    saveDialog.setTitle("Assign To");
                    saveDialog.setMessage("Do You want to assign this template to " + UserName);
                    saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            submitClickEvent();
                        }
                    });
                    saveDialog.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    saveDialog.show();*/
                } else {
                    submitClickEvent();
                }
                return;
            }
            if (id == R.id.cancel) {
                cancelClilckEvent();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddTaskReassign OnClick Event ", "Exception " + e.getMessage(), "WARN", null);
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
            Appreference.printLog("AddTaskReassign getFileName Method ", "Exception " + e.getMessage(), "WARN", null);
        }
        return strFilename;
    }

    public void showDialog() {
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dialog = new ProgressDialog(AddTaskReassign.this);
                    if (isProject != null && isProject.equalsIgnoreCase("Yes")) {
                        dialog.setMessage("Assigning...");
                    } else {
                        dialog.setMessage("ReAssigning...");
                    }
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setProgress(0);
                    dialog.setMax(100);
                    dialog.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddTaskReassign ShowDialog ", "Exception " + e.getMessage(), "WARN", null);
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
            Appreference.printLog("AddTaskReassign cancelDialog ", "Exception " + e.getMessage(), "WARN", null);
        }
    }


    private void AssignMmber_OracleProject() {
        if (getNetworkState()) {
            if (isProjectFromOracle) {
                Log.i("ws123", "inside wservice AssignTask request");
                JSONObject oracleProject_object = new JSONObject();
                JSONObject taskid = new JSONObject();
                TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                try {
                    taskid.put("id", Integer.valueOf(taskId));
                    taskDetailsBean.setTaskId(taskId);
                    oracleProject_object.put("task", taskid);
                    oracleProject_object.put("fromId", Appreference.loginuserdetails.getId());
                    taskDetailsBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
                    oracleProject_object.put("projectId", Integer.parseInt(detailsBean.getProjectId()));
                    taskDetailsBean.setProjectId(detailsBean.getProjectId());
                    oracleProject_object.put("estimatedTravelHours", "");
                    taskDetailsBean.setEstimatedTravel("");
                    taskDetailsBean.setEstimatedActivity("");
                    oracleProject_object.put("estimatedActivityHours", "");
                    JSONArray jsonArray = new JSONArray();
                    //                String SelectedUserList = null;
                    int checkPos = 0;
                    for (int position = 0; position < contactList.size(); position++) {
                        ContactBean item = contactList.get(position);
                        if (item.getIscheck()) {
                            JSONObject usersList = new JSONObject();
                            usersList.put("id", item.getUserid());
                            Log.i("Assign", "usersList ==> " + usersList);
                            jsonArray.put(checkPos, usersList);
                            checkPos++;
                            Log.i("ASE", " SelectedUserList before ====>  " + SelectedUserList);
                            if (SelectedUserList != null) {
                                SelectedUserList = SelectedUserList + "," + item.getUsername();
                                SelectedUsersId = SelectedUsersId + "," + item.getUserid();
                            } else {
                                SelectedUserList = item.getUsername();
                                SelectedUsersId = item.getUserid() + "";
                            }
                        }

                    }
                    Log.i("ws123", "AssignTask SelectedUserList=====>  " + SelectedUserList);
                    //                taskDetailsBean.setToUserName(SelectedUserList);
                    oracleProject_object.put("listUser", jsonArray);

                    ProjectHistory projectHistory = (ProjectHistory) Appreference.context_table.get("projecthistory");
                    if (projectHistory != null) {
                        Log.i("ProjectHistory", "inside refresh  status projectHistory ========>" + projectHistory.projectDetailsBeans + "size bean " + projectHistory.projectDetailsBeans.size() + "buddayArrayAdapteer==>" + projectHistory.buddyArrayAdapter);

                        if (projectHistory.projectDetailsBeans != null && projectHistory.projectDetailsBeans.size() > 0 && projectHistory.buddyArrayAdapter != null) {

                            ProjectDetailsBean projectDetailsBean = projectHistory.projectDetailsBeans.get(Clickposition);
                            projectDetailsBean.setTaskStatus("Assigned");
                            projectDetailsBean.setTaskReceiver(SelectedUserList);

                            projectHistory.buddyArrayAdapter.notifyDataSetChanged();
                        }
                    }
                    //                taskDetailsBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
                    taskDetailsBean.setFromUserName(Appreference.loginuserdetails.getUsername());
                    taskDetailsBean.setTaskName(detailsBean.getTaskName());
                    taskDetailsBean.setTaskNo(detailsBean.getTaskNo());
                    taskDetailsBean.setCatagory(detailsBean.getCatagory());
                    taskDetailsBean.setIssueId(detailsBean.getIssueId());
                    taskDetailsBean.setParentId(getFileName());
                    taskDetailsBean.setIsRemainderRequired("");
                    taskDetailsBean.setCompletedPercentage(detailsBean.getCompletedPercentage());
                    taskDetailsBean.setPlannedStartDateTime("");
                    taskDetailsBean.setPlannedEndDateTime("");
                    taskDetailsBean.setRemainderFrequency("");
                    taskDetailsBean.setSignalid(Utility.getSessionID());
                    taskDetailsBean.setDateTime(dateforrow);
                    taskDetailsBean.setSendStatus("0");
                    taskDetailsBean.setTaskStatus("inprogress");
                    taskDetailsBean.setOwnerOfTask(detailsBean.getOwnerOfTask());
                    Log.i("ASE", "Signalid " + taskDetailsBean.getSignalid());
                    Log.i("ASE", "SelectedUserList " + SelectedUserList);
                    if (SelectedUserList.contains(",")) {
                        taskDetailsBean.setTaskType("Group");
                        taskDetailsBean.setTaskMemberList(SelectedUserList);
                    } else {
                        taskDetailsBean.setTaskType("individual");
                        taskDetailsBean.setToUserName(SelectedUserList);
                        taskDetailsBean.setToUserId(SelectedUsersId);
                        taskDetailsBean.setTaskReceiver(SelectedUserList);
                    }
                    taskDetailsBean.setTaskPriority("medium");
                    taskDetailsBean.setParentTaskId(detailsBean.getParentTaskId());
                    taskDetailsBean.setSubType("normal");
                    taskDetailsBean.setRemark("");
                    taskDetailsBean.setReminderQuote("");
                    Log.i("ASE", "TaskType " + taskDetailsBean.getTaskType());
                    Log.i("ASE", "SelectedUserList ==> " + SelectedUserList);
                    if (SelectedUserList != null && !SelectedUserList.equalsIgnoreCase("")) {
                        int counter = 0;
                        for (int i = 0; i < SelectedUserList.length(); i++) {
                            if (SelectedUserList.charAt(i) == ',') {
                                counter++;
                            }

                            Log.d("project_details", "Task Mem's counter size is == " + counter);
                        }
                        for (int j = 0; j < counter + 1; j++) {
                            String Mem_name = SelectedUserList.split(",")[j];
                            Log.i("project_details", "Task Mem's and position == " + Mem_name + " " + j);
                            String Memberlist = "";
                            Memberlist = VideoCallDataBase.getDB(context).getname(Mem_name);
                            SelectedUserList1 = Memberlist + ",";
                        }
                        SelectedUserList1 = SelectedUserList1.substring(0, SelectedUserList1.length() - 1);
                    }
                    Log.i("ASE", "SelectedUserList1 ==> " + SelectedUserList1);
                    taskDetailsBean.setTaskDescription("Task Assigned to " + SelectedUserList1);
                    taskDetailsBean.setRepeatFrequency("");
                    taskDetailsBean.setTaskTagName("");
                    taskDetailsBean.setTaskUTCDateTime(dateforrow);
                    taskDetailsBean.setMimeType("assigntask");
                    taskDetailsBean.setCatagory("Task");
                    taskDetailsBean.setCustomTagVisible(true);

                    if (taskDetailsBean != null)
                        Log.i("ws123", "Bean============> 11" + detailsBean.getParentTaskId() + "getProjectId=========>" + taskDetailsBean.getProjectId());
                    else
                        Log.i("ws123", "Bean null============> 11");

                    Appreference.jsonRequestSender.OracleAssignTask(EnumJsonWebservicename.assignTask, oracleProject_object, taskDetailsBean, AddTaskReassign.this);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        } else
            Toast.makeText(AddTaskReassign.this, "Check your internet connection", Toast.LENGTH_SHORT).show();

    }

    private boolean getNetworkState() {
        boolean isNetwork = false;
        ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (ConnectionManager != null) {
            NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected() == true)
                isNetwork = true;
            else
                isNetwork = false;
        }
        return isNetwork;
    }

    public void submitClickEvent() {
        try {
            detailsBean.setSignalid(Utility.getSessionID());
            detailsBean.setParentId(getFileName());
            chatUsers = new ArrayList<String>();
            chatUsersName = new ArrayList<>();
            ArrayList<Integer> selectedUsersId = new ArrayList<>();

            showDialog();
//            boolean check = false;
            AssignMmber_OracleProject();
/*
            for (int position = 0; position < contactList.size(); position++) {
                ContactBean item = contactList.get(position);
                if (item.getIscheck()) {
                    selectedUsersId.add(item.getUserid());
                    if (item.getFirstname() != null && item.getLastname() != null) {
                        if (isProject.equalsIgnoreCase("Yes")) {
                            detailsBean.setTaskDescription("Task Reassigned to " + item.getFirstname() + " " + item.getLastname());
                        } else {
//                            detailsBean.setTaskDescription("Task Assigned to " + item.getFirstname()+" "+item.getLastname());
                            detailsBean.setTaskDescription("Task Assigned to " + SelectedUserList);
                        }

                    } else if (item.getFirstname() != null) {
                        if (isProject.equalsIgnoreCase("Yes")) {
                            detailsBean.setTaskDescription("Task Reassigned to " + item.getFirstname());
                        } else {
                            detailsBean.setTaskDescription("Task Assigned to " + item.getFirstname());
                        }
                    } else {
                        if (isProject.equalsIgnoreCase("Yes")) {
                            detailsBean.setTaskDescription("Task Reassigned to " + item.getUsername());
                        } else {
                            detailsBean.setTaskDescription("Task Assigned to " + item.getUsername());
                        }
                    }
                    JSONObject jsonObject = new JSONObject();
                    JSONObject id = new JSONObject();
                    JSONObject from = new JSONObject();
                    JSONObject to = new JSONObject();
                    try {
                        id.put("id", taskId);
                        jsonObject.put("task", id);

                        from.put("id", Appreference.loginuserdetails.getId());
                        jsonObject.put("from", from);
                        to.put("id", item.getUserid());
                        jsonObject.put("to", to);
                        jsonObject.put("requestType", "reassignTask");
                        if (getIntent().getExtras().getString("isProject") != null && getIntent().getExtras().getString("isProject").toString().equalsIgnoreCase("Yes")) {
                            jsonObject.put("taskStatus", "inprogress");
                        } else {
                            jsonObject.put("taskStatus", "assigned");
                        }
                        toUserName = item.getUsername();
                        check = true;
                        jsonObject.put("parentId", detailsBean.getParentId());
                        jsonObject.put("signalId", detailsBean.getSignalid());
                        JSONObject jsonObject4 = new JSONObject();
                        JSONObject jsonObject5 = new JSONObject();
                        jsonObject5.put("id", Appreference.loginuserdetails.getId());
                        jsonObject4.put("user", jsonObject5);
                        jsonObject4.put("fileType", "text");
                        jsonObject4.put("description", detailsBean.getTaskDescription());

                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(0, jsonObject4);
                        jsonObject.put("listTaskConversationFiles", jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Appreference.printLog("AddTaskReassign json ", "Exception " + e.getMessage(), "WARN", null);
                    }

                    Log.i("template", "Assign template" + jsonObject.toString());
                    Appreference.jsonRequestSender.taskConversationEntry(EnumJsonWebservicename.taskConversationEntry, jsonObject, AddTaskReassign.this, null, null);
                    break;

                }
            }
*/
//            if (!check) {
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        dialog.dismiss();
//                        Toast.makeText(getApplicationContext(), "Please select the contact", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }


        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddTaskReassign SubmitClickEvent ", "Exception " + e.getMessage(), "WARN", null);
        }
    }


    public void cancelClilckEvent() {
        try {
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddTaskReassign cancelClickEvent ", "Exception " + e.getMessage(), "WARN", null);
        }
    }


    public void notifypostEntryResponse(final String values) {
        Log.i("postEntry", "NewTaskactivity  notifypostEntryResponse method");
//
        try {
            JSONObject json = new JSONObject(values);
            ArrayList<String> rejected = new ArrayList<>();

            if (observerList != null) {
                for (String oldName : observerList) {
                    boolean check = false;
                    for (int i = 0; i < chatUsersName.size(); i++) {
                        String newName = chatUsersName.get(i);
                        if (oldName.equalsIgnoreCase(newName))
                            check = true;

                    }
                    if (!check) {
                        rejected.add(oldName);
                        Log.i("task", "rejected observer" + oldName);
                    }
                }
            }
//            if (!values.contains("result_code")) {
            int resultCode = json.getInt("result_code");
            final String text = json.getString("result_text");

            Intent intent = new Intent();
            intent.putStringArrayListExtra("observer", chatUsersName);
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
            Appreference.printLog("AddTaskReassign notifyPostEntry Responce ", "Exception " + e.getMessage(), "WARN", null);
        }
    }

    @Override
    public void ResponceMethod(Object object) {
        try {
            cancelDialog();
            Log.i("ws123", "inside wservice AssignTask Responce");

            CommunicationBean communicationBean = (CommunicationBean) object;
            String response = communicationBean.getEmail();
            String WebServiceEnum_Response = communicationBean.getFirstname();
            TaskDetailsBean detailsBean1 = null;

            Log.i("template", response);
            if (WebServiceEnum_Response != null && WebServiceEnum_Response.equalsIgnoreCase(("assignTask"))) {
                ArrayList<TaskDetailsBean> arrayList = new ArrayList<>();
                JsonElement jelement = new JsonParser().parse(response);
                if (jelement.getAsJsonObject() != null) {
                    JsonObject jobject = jelement.getAsJsonObject();
                    final JSONObject jsonObject = new JSONObject(communicationBean.getEmail());
                    if (jsonObject.getString("result_code").equalsIgnoreCase("0")) {

                        Log.i("ws123", "inside  AssignTask Responce Received");
                        detailsBean1 = communicationBean.getTaskDetailsBean();
                        // db insert method
//                        VideoCallDataBase.getDB(context).update_Project_history(detailsBean1);
//                        VideoCallDataBase.getDB(context).insertORupdate_Task_history(detailsBean1);
                        VideoCallDataBase.getDB(context).insertORupdateStatus(detailsBean1);

//                        RemoveUser = taskReceiver;
                        Log.i("ASE", "taskReceiver " + taskReceiver);
//                        detailsBean.setTaskReceiver(SelectedUserList);
                        final ArrayList<String> arrayList1 = new ArrayList<>();
                        String projectMembers = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskMemberList from projectHistory where projectId ='" + detailsBean1.getProjectId() + "' order by id ASC limit 1");

                        if (projectMembers != null) {
                            int counter_1 = 0;
                            for (int i = 0; i < projectMembers.length(); i++) {
                                if (projectMembers.charAt(i) == ',') {
                                    counter_1++;
                                }
                                Log.i("taskConversation", "project_details Task Mem's counter size is == " + counter_1);
                            }
                            Log.i("taskConversation", "projectBean.getFromUserName() " + projectMembers);
                            for (int j = 0; j < counter_1 + 1; j++) {
                                //                Log.i("taskConversation", "project_details Task Mem's and position == " + observers.split(",")[j] + " " + j);
                                if (counter_1 == 0) {
                                    if (!arrayList1.contains(projectMembers)) {
                                        arrayList1.add(projectMembers);
                                    }
                                } else {
                                    if (!arrayList1.contains(projectMembers.split(",")[j])) {
                                        arrayList1.add(projectMembers.split(",")[j]);
                                    }
                                }
                            }
                        }
                        if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(detailsBean1.getOwnerOfTask())) {
                            if (!arrayList1.contains(detailsBean1.getOwnerOfTask())) {
                                arrayList1.add(detailsBean1.getOwnerOfTask());
                            }
                        }
                        if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(taskReceiver)) {
                            if (!arrayList1.contains(taskReceiver)) {
                                arrayList1.add(taskReceiver);
                            }
                        }
/*
                        if (WebServiceEnum_Response != null && WebServiceEnum_Response.equalsIgnoreCase(("assignTask"))) {
                            detailsBean1.setMimeType("Reassign");
                            detailsBean1.setCustomTagVisible(true);
                            final String xml = composeChatXML(detailsBean1);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    sendMultiInstantMessage(xml, arrayList1, 1);
                                }
                            });

                        }
*/
                        Intent intent = new Intent();
                        Log.i("ASE", "RemoveUser " + RemoveUser + " Receiver==>  " + detailsBean1.getTaskReceiver());
                        Log.i("ASE", "RemoveUserllist " + detailsBean1.getTaskMemberList());
                        intent.putExtra("taskRemover", RemoveUser);
                        intent.putExtra("isProject", isProject);
                        intent.putExtra("taskBean", detailsBean1);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        String result = (String) jsonObject.get("result_text");
                        Toast.makeText(AddTaskReassign.this, result, Toast.LENGTH_LONG).show();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
//                            dialog.dismiss();
                            if (isProject != null && isProject.equalsIgnoreCase("Yes")) {
                                Toast.makeText(getApplicationContext(), "Task Assigned Succesfully.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Task ReAssigned Succesfully.", Toast.LENGTH_SHORT).show();
                            }
                            Log.i("template", "Re Assigned task Sucessfully");


                        }
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddTaskReassign Responce Method ", "Exception " + e.getMessage(), "WARN", null);
        }
    }

    @Override
    public void ErrorMethod(Object object) {

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
            buffer.append(" />");
            buffer.append("</TaskDetailsinfo>");
            Log.d("xml", "composed xml for chat======>" + buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AddTaskReassign ComposeObserverXMl method ", "Exception " + e.getMessage(), "WARN", null);

        } finally {
            return buffer.toString();
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
            buffer.append("<?xml version=\"1.0\"?>" + "<TaskDetailsinfo><TaskDetails");

            if (cmbean.getTaskObservers() != null) {
                buffer.append(" taskAddObservers=" + quotes + cmbean.getTaskObservers() + quotes);
            }

            if (cmbean.getTaskRemoveObservers() != null) {
                buffer.append(" taskRemoveObservers=" + quotes + cmbean.getTaskRemoveObservers() + quotes);
            }

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
            if (!getResources().getString(R.string.proxyua).equalsIgnoreCase("enable")) {
                buffer.append(" toUserId=" + quotes + cmbean.getToUserId() + quotes);
                buffer.append(" toUserName=" + quotes + cmbean.getToUserName() + quotes);
            } else {
                if (isProjectFromOracle) {
                    buffer.append(" toUserId=" + quotes + "" + quotes);
                    buffer.append(" toUserName=" + quotes + "" + quotes);
                } else {
                    buffer.append(" toUserId=" + quotes + cmbean.getToUserId() + quotes);
                    buffer.append(" toUserName=" + quotes + cmbean.getToUserName() + quotes);
                }
            }
            buffer.append(" taskNo=" + quotes + cmbean.getTaskNo() + quotes);
            buffer.append(" taskId=" + quotes + cmbean.getTaskId() + quotes);
            buffer.append(" taskType=" + quotes + cmbean.getTaskType() + quotes);
            buffer.append(" plannedStartDateTime=" + quotes + cmbean.getUtcPlannedStartDateTime() + quotes);
            buffer.append(" plannedEndDateTime=" + quotes + cmbean.getUtcplannedEndDateTime() + quotes);
            buffer.append(" isRemainderRequired=" + quotes + cmbean.getIsRemainderRequired() + quotes);
            Log.i("newtaskconversation", "remainderDateTime " + cmbean.getUtcPemainderFrequency());
            if (cmbean.getUtcPemainderFrequency() == null || (cmbean.getUtcPemainderFrequency() != null && cmbean.getUtcPemainderFrequency().equalsIgnoreCase(""))) {
                cmbean.setUtcPemainderFrequency("");
            }
            buffer.append(" remainderDateTime=" + quotes + cmbean.getUtcPemainderFrequency() + quotes);
            if (cmbean.getCompletedPercentage() != null && !cmbean.getCompletedPercentage().equalsIgnoreCase("") && Integer.parseInt(cmbean.getCompletedPercentage()) == 100) {
                if (cmbean.getTaskStatus() != null && cmbean.getTaskStatus().equalsIgnoreCase("Closed")) {
                    Log.i("newtaskconversation", " compose taskStatus 1 " + cmbean.getTaskStatus());
                    buffer.append(" taskStatus=" + quotes + "Closed" + quotes);
                } else {
                    Log.i("newtaskconversation", " compose taskStatus 2 " + cmbean.getTaskStatus());
                    if (cmbean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        buffer.append(" taskStatus=" + quotes + "Closed" + quotes);
                    } else {
                        buffer.append(" taskStatus=" + quotes + "Completed" + quotes);
                    }
//                    buffer.append(" taskStatus=" + quotes + "Completed" + quotes);
                }
            } else {
                Log.i("newtaskconversation", " compose taskStatus 3 " + cmbean.getTaskStatus());
                Log.i("Accept", "value taskStatus after compose " + cmbean.getTaskStatus());
                buffer.append(" taskStatus=" + quotes + cmbean.getTaskStatus() + quotes);
            }
            buffer.append(" signalid=" + quotes + cmbean.getSignalid() + quotes);
            buffer.append(" parentId=" + quotes + cmbean.getParentId() + quotes);
            buffer.append(" taskRequestType=" + quotes + cmbean.getTaskRequestType() + quotes);
            buffer.append(" dateFrequency=" + quotes + cmbean.getDateFrequency() + quotes);
            String TimeFrequency = "", ReminderQuotes = "";
           /* if (cmbean.getFromUserName() != null && cmbean.getFromUserName().equalsIgnoreCase(cmbean.getOwnerOfTask())) {
                Log.i("newtaskconversation", "cmbean.getTimeFrequency() " + cmbean.getTimeFrequency() + " is reminderrequired " + cmbean.getIsRemainderRequired());
                if (cmbean.getTimeFrequency() != null && (cmbean.getIsRemainderRequired() != null && cmbean.getIsRemainderRequired().equalsIgnoreCase("Y"))) {
                    TimeFrequency = TimeFrequencyCalculation(cmbean.getTimeFrequency());
                } else {
                    TimeFrequency = "";
                }
            } else {
                if (cmbean.getTimeFrequency() != null && (cmbean.getIsRemainderRequired() != null && cmbean.getIsRemainderRequired().equalsIgnoreCase("Y"))) {
                    TimeFrequency = TimeFrequencyCalculation(cmbean.getTimeFrequency());
                } else {
                    TimeFrequency = "";
                }
            }*/
            buffer.append(" timeFrequency=" + quotes + TimeFrequency + quotes);
            buffer.append(" taskOwner=" + quotes + cmbean.getOwnerOfTask() + quotes);
            buffer.append(" mimeType=" + quotes + cmbean.getMimeType() + quotes);
            buffer.append(" dateTime=" + quotes + cmbean.getTaskUTCDateTime() + quotes);
            buffer.append(" taskPriority=" + quotes + cmbean.getTaskPriority() + quotes);
            buffer.append(" completedPercentage=" + quotes + cmbean.getCompletedPercentage() + quotes);
            buffer.append(" remainderQuotes=" + quotes + cmbean.getReminderQuote() + quotes);
            buffer.append(" remark=" + quotes + cmbean.getRemark() + quotes);
            buffer.append(" taskReceiver=" + quotes + cmbean.getTaskReceiver() + quotes);
            if (isProjectFromOracle) {
                buffer.append(" taskToUsersList=" + quotes + SelectedUserList + quotes);
            } else {
                buffer.append(" taskToUsersList=" + quotes + cmbean.getGroupTaskMembers() + quotes);
            }
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
            if (cmbean.getDaysOfTheWeek() != null && !cmbean.getDaysOfTheWeek().equalsIgnoreCase("") && !cmbean.getDaysOfTheWeek().equalsIgnoreCase(null)) {
                buffer.append(" isRepeatTask=" + quotes + "Y" + quotes);
            }
            buffer.append(" />");
            buffer.append("</TaskDetailsinfo>");
            Log.d("xml", "composed xml for chat======>" + buffer.toString());
            Log.d("xml", "composed xml for encode data======>" + Charset.forName("UTF-8").encode(":-)").toString());
            Log.i("xml", "composed xml for listofabservers " + listOfObservers);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return buffer.toString();
        }
    }


    public void sendMultiInstantMessage(String msgBody, ArrayList<String> userlist, int sendTo) {
        for (String name : userlist) {
            Log.i("taskConversation", "sendMultiInstantMessage 1  ");
            Log.i("task observer", "observer 1 " + name);
            Log.i("task observer", "MainActivity.account.buddyList.size()" + MainActivity.account.buddyList.size());
        }
        if ((taskType != null && !taskType.equalsIgnoreCase("Group")) || isProjectFromOracle) {
            Log.i("groupMemberAccess", "!group ");
            Log.i("taskConversation", "sendMultiInstantMessage 2  ");
//        if (!getResources().getString(R.string.proxyua).equalsIgnoreCase("enable") || !taskType.equalsIgnoreCase("Group")) {
            if (!getResources().getString(R.string.proxyua).equalsIgnoreCase("enable") || sendTo == 1) {
                Log.i("taskConversation", "sendMultiInstantMessage 3  ");
                for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
                    Log.i("taskConversation", "sendMultiInstantMessage 4  ");
                    String name = MainActivity.account.buddyList.get(i).cfg.getUri();
                    Log.i("task", "buddyname-->  " + name);
                    for (String username : userlist) {
                        Log.i("taskConversation", "sendMultiInstantMessage 5  ");
                        Log.i("task", "taskObservers Name--> " + username);
                        String nn = "sip:" + username + "@" + getResources().getString(R.string.server_ip);
                        Log.i("task", "selected user--> " + nn);
                        if (nn.equalsIgnoreCase(name)) {
                            Log.i("taskConversation", "sendMultiInstantMessage 6  ");
                            Log.i("task", "both users are same ");
                            Appreference.printLog("Sipmessage", msgBody, "DEBUG", null);
                            MyBuddy myBuddy = MainActivity.account.buddyList.get(i);
                            SendInstantMessageParam prm = new SendInstantMessageParam();
                            prm.setContent(msgBody);

                            boolean valid = myBuddy.isValid();
                            Log.i("task", "valid ======= " + valid);
                            try {
                                Log.i("taskConversation", "sendMultiInstantMessage 7  ");
                                myBuddy.sendInstantMessage(prm);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                }
            }/* else {
                Log.i("taskConversation", "sendMultiInstantMessage 8  ");
                BuddyConfig bCfg = new BuddyConfig();
                bCfg.setUri("sip:" + proxy_user + "@" + getResources().getString(R.string.server_ip));
                bCfg.setSubscribe(false);
//            MainActivity.account.addBuddy(bCfg);
                Buddy myBuddy = new Buddy();
                try {
                    Log.i("taskConversation", "sendMultiInstantMessage 9  ");
                    myBuddy.create(MainActivity.account, bCfg);
//                MainActivity.account.addBuddy(myBuddy);
                    Log.i("task", "proxybuddy " + bCfg.getUri());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                SendInstantMessageParam prm = new SendInstantMessageParam();
                prm.setContent(msgBody);

                boolean valid = myBuddy.isValid();
                Log.i("task", "valid ======= " + valid);
                try {
                    Log.i("taskConversation", "sendMultiInstantMessage 10  ");
                    myBuddy.sendInstantMessage(prm);
                    myBuddy.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }*/
        }
    }


    public void sendInstantMessage(String msgBody) {
        try {
            Log.i("task", "ready to send");
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
                    }
                    break;
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            Appreference.printLog("AddTaskReassign sendinstantmessage ", "Exception " + e.getMessage(), "WARN", null);
        }
    }


   /* private String[] getDatesFromCalender() {
        Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+1"));
        Calendar c2 = Calendar.getInstance(TimeZone.getTimeZone("GMT+1"));

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
            Appreference.printLog("TaskDateUpdate getDatesfromCalender", "Exception " + e.getMessage(), "WARN", null);
        }

        return dates.toArray(new String[dates.size() - 1]);
    }*/

    /*private void showdatetime_dialog(final int startOrEnd) {
        try {
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
            set.setTypeface(normal_type);
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
            if (android.text.format.DateFormat.is24HourFormat(context)) {
                hourPicker.setMinValue(00);
                hourPicker.setMaxValue(23);
                String[] displayedValues_hour = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
                hourPicker.setDisplayedValues(displayedValues_hour);
                am_pmPicker.setVisibility(View.GONE);
            } else {
                hourPicker.setMinValue(0);
                hourPicker.setMaxValue(11);
                String[] displayedValues_hour = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
                hourPicker.setDisplayedValues(displayedValues_hour);
                am_pmPicker.setVisibility(View.VISIBLE);
                am_pmPicker.setMinValue(0);
                am_pmPicker.setMaxValue(3);
                am_pmPicker.setWrapSelectorWheel(true);
                int am = 0;
                am_pmPicker.setDisplayedValues(am_pm);
            }

            final Calendar rightNow = Calendar.getInstance(Locale.getDefault());
            int dd = rightNow.get(Calendar.HOUR_OF_DAY);
            Log.i("current", "hour" + dd);
            yyyy = rightNow.get(Calendar.YEAR);
            if (startOrEnd == 1) {
                Log.i("TaskDateUpdate", "start_date1 click event 1 ");
//                String dateInputString=String.valueOf(start_date1);
                String[] values1 = datePicker.getDisplayedValues();
//                String str = values1[datePicker.getValue()];
                String st_hour = "";
                String st_date = "";
                if (android.text.format.DateFormat.is24HourFormat(context)) {

                    //                   st_hour = start_date1.getText().toString();
//                    st_date = start_date1.getText().toString();
//                    st_hour=Appreference.ChangeOriginalPattern(true,start_date1.getText().toString(),datepattern);
                    st_hour = Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern);
                    st_date = Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern);
                    Log.i("TaskDateUpdate", "start_date1 click event 10 " + st_date + "  " + st_hour);
                    Log.i("TaskDateUpdate", "start_date1 click event 10* st date" + st_date);
                    Log.i("TaskDateUpdate", "start_date1 click event 10* st hour" + st_hour);
                    st_hour = st_hour.split(" ")[1];
                    st_date = st_date.split(" ")[0];
                    Log.i("start", "st_date " + st_date);
                    Log.i("start", "st_hour " + st_hour);
                    Log.i("TaskDateUpdate", "start_date1 click event 11* " + st_date + "  " + st_hour);
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
                        // 20120821
                    } catch (Exception e) {
                        // wrong input
                        Appreference.printLog("TaskDateUpdate showDateTimeDialog", "Exception " + e.getMessage(), "WARN", null);
                    }
//                datePicker.setValue();
                    int hour = Integer.parseInt(st_hour.split(":")[0]);
                    Log.i("start", "hour " + hour);
                    Log.i("TaskDateUpdate", "start_date1 click event 12 " + hour);
                    hourPicker.setValue(hour);
                } else {
                    String date_ptn = Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern);
                    st_date = Appreference.setDateTime(true, date_ptn);
                    st_hour = Appreference.setDateTime(true, date_ptn);
//                    st_date = Appreference.setTimeinDevicePattern(true, date_ptn,datepattern);
//                    st_hour = Appreference.setTimeinDevicePattern(true, date_ptn,datepattern);
                    Log.i("TaskDateUpdate", "start_date1 click event 10 start date" + st_date);
                    Log.i("TaskDateUpdate", "start_date1 click event 10 start hour" + st_hour);
                    st_hour = st_hour.split(" ")[1];
                    st_date = st_date.split(" ")[0];
                    Log.i("start", "st_date " + st_date);
                    Log.i("start", "st_hour " + st_hour);
                    Log.i("TaskDateUpdate", "start_date1 click event 11 " + st_date + "  " + st_hour);
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
                        // 20120821
                    } catch (Exception e) {
                        // wrong input
                        Appreference.printLog("TaskDateUpdate showDateTimeDialog", "Exception " + e.getMessage(), "WARN", null);
                    }
//                datePicker.setValue();
                    int hour = Integer.parseInt(st_hour.split(":")[0]);
                    Log.i("start", "hour " + hour);
                    Log.i("TaskDateUpdate", "start_date1 click event 12 " + hour);
                    if (hour >= 13) {
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
                }

                Log.i("start", "am_pmPicker" + am_pmPicker.getValue());
//                hourPicker.setValue(hour - 1);
//                hourPicker.setValue(dd);
            } else if (startOrEnd == 2) {
                Log.i("TaskDateUpdate", "end_date1 click event 2 ");
                String[] values1 = datePicker.getDisplayedValues();
                String end_hour = "";
                String en_date = "";
                if (android.text.format.DateFormat.is24HourFormat(context)) {
                    String date_ptn = Appreference.ChangeOriginalPattern(true, end_date1.getText().toString(), datepattern);
//                    end_hour = end_date1.getText().toString();
//                    en_date = end_date1.getText().toString();
                    end_hour = date_ptn;
                    en_date = date_ptn;
                    Log.i("TaskDateUpdate", "end_date1 click event 20 " + end_hour + "   " + en_date);
                    end_hour = end_hour.split(" ")[1];
                    Log.i("end", "en_hour" + end_hour);
                    int endhour = Integer.parseInt(end_hour.split(":")[0]);
                    Log.i("end", "en_hour" + endhour);
                    en_date = en_date.split(" ")[0];
                    Log.i("TaskDateUpdate", "end_date1 click event 21 " + end_hour + "   " + en_date);
                    try {
                        // obtain date and time from initial string
                        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat targetFormat = new SimpleDateFormat("MMM dd");
                        Date date = originalFormat.parse(en_date);
                        Log.i("end", "original " + date);
                        String formattedDate = targetFormat.format(date);
                        Log.i("end", "target " + formattedDate);
                        for (int i = 0; i < values1.length; i++) {
                            Log.i("end", "for" + values1[i]);
                            if (values1[i].equalsIgnoreCase(formattedDate)) {
                                Log.i("end", "if" + values1[i]);
                                datePicker.setValue(i);
                            }
//                        else
//                        {
//                            Log.i("start","else");
////                            datePicker.setValue(0);
//                        }
                        }
                        // 20120821
                    } catch (Exception e) {
                        // wrong input
                        Appreference.printLog("TaskDateUpdate showDateTimeDialog", "Exception " + e.getMessage(), "WARN", null);
                    }
                    Log.i("TaskDateUpdate", "end_date1 click event 22 " + endhour);
                    Log.i("end", "is24format " + endhour);
                    hourPicker.setValue(endhour);
                } else {
                    String date_ptn = Appreference.ChangeOriginalPattern(false, end_date1.getText().toString(), datepattern);
                    en_date = Appreference.setDateTime(true, date_ptn);
                    end_hour = Appreference.setDateTime(true, date_ptn);
//                    en_date = Appreference.setTimeinDevicePattern(true, date_ptn,datepattern);
//                    end_hour = Appreference.setTimeinDevicePattern(true, date_ptn,datepattern);
                    Log.i("TaskDateUpdate", "end_date1 click event 20 " + end_hour + "   " + en_date);
                    end_hour = end_hour.split(" ")[1];
                    Log.i("end", "en_hour" + end_hour);
                    int endhour = Integer.parseInt(end_hour.split(":")[0]);
                    Log.i("end", "en_hour" + endhour);
                    en_date = en_date.split(" ")[0];
                    Log.i("TaskDateUpdate", "end_date1 click event 21 " + end_hour + "   " + en_date);
                    try {
                        // obtain date and time from initial string
                        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat targetFormat = new SimpleDateFormat("MMM dd");
                        Date date = originalFormat.parse(en_date);
                        Log.i("end", "original " + date);
                        String formattedDate = targetFormat.format(date);
                        Log.i("end", "target " + formattedDate);
                        for (int i = 0; i < values1.length; i++) {
                            Log.i("end", "for" + values1[i]);
                            if (values1[i].equalsIgnoreCase(formattedDate)) {
                                Log.i("end", "if" + values1[i]);
                                datePicker.setValue(i);
                            }
//                        else
//                        {
//                            Log.i("start","else");
////                            datePicker.setValue(0);
//                        }
                        }
                        // 20120821
                    } catch (Exception e) {
                        // wrong input
                        Appreference.printLog("TaskDateUpdate showDatetimeDialog", "Exception " + e.getMessage(), "WARN", null);
                    }
                    Log.i("TaskDateUpdate", "end_date1 click event 22 " + endhour);
                    Log.i("end", "!is24format " + endhour);
                    if (endhour >= 13) {
                        if (endhour == 13) {
                            strTime = "1";
                        } else if (endhour == 14) {
                            strTime = "2";
                        } else if (endhour == 15) {
                            strTime = "3";
                        } else if (endhour == 16) {
                            strTime = "4";
                        } else if (endhour == 17) {
                            strTime = "5";
                        } else if (endhour == 18) {
                            strTime = "6";
                        } else if (endhour == 19) {
                            strTime = "7";
                        } else if (endhour == 20) {
                            strTime = "8";
                        } else if (endhour == 21) {
                            strTime = "9";
                        } else if (endhour == 22) {
                            strTime = "10";
                        } else if (endhour == 23) {
                            strTime = "11";
                        }
                        hourPicker.setValue(Integer.parseInt(strTime) - 1);
                        am_pmPicker.setValue(1);
                    } else {
                        hourPicker.setValue(endhour - 1);
                        am_pmPicker.setValue(0);
                    }
                }

                //Log.i("end","hour"+start_time);
//                hourPicker.setValue(start_time);
            } else if (startOrEnd == 3) {
                Log.i("TaskDateUpdate", "reminder_date1 click event 3 ");
                String[] values1 = datePicker.getDisplayedValues();
                String re_hour = "";
                String re_date = "";
                if (android.text.format.DateFormat.is24HourFormat(context)) {
                    String date_rem = Appreference.ChangeOriginalPattern(true, reminder_date1.getText().toString(), datepattern);
//                    re_hour = reminder_date1.getText().toString();
//                    re_date = reminder_date1.getText().toString();
                    re_hour = date_rem;
                    re_date = date_rem;
                    Log.i("TaskDateUpdate", "reminder_date1 click event 30 " + re_date + "  " + re_hour);
                    re_hour = re_hour.split(" ")[1];
                    Log.i("remin", "en_hour" + re_hour);
                    int rehour = Integer.parseInt(re_hour.split(":")[0]);
                    Log.i("remin", "en_hour" + rehour);
                    re_date = re_date.split(" ")[0];
                    Log.i("TaskDateUpdate", "reminder_date1 click event 31 " + re_date + "  " + re_hour);
                    try {
                        // obtain date and time from initial string
                        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat targetFormat = new SimpleDateFormat("MMM dd");
                        Date date = originalFormat.parse(re_date);
                        Log.i("remin", "original " + date);
                        String formattedDate = targetFormat.format(date);
                        Log.i("remin", "target " + formattedDate);
                        for (int i = 0; i < values1.length; i++) {
                            Log.i("remin", "for" + values1[i]);
                            if (values1[i].equalsIgnoreCase(formattedDate)) {
                                Log.i("remin", "if" + values1[i]);
                                datePicker.setValue(i);
                            }
//                        else
//                        {
//                            Log.i("start","else");
////                            datePicker.setValue(0);
//                        }
                        }
                        // 20120821
                    } catch (Exception e) {
                        // wrong input
                        Appreference.printLog("TaskDateUpdate showDateTimeDialog", "Exception " + e.getMessage(), "WARN", null);
                    }

                    Log.i("TaskDateUpdate", "reminder_date1 click event 32 " + rehour);
                    hourPicker.setValue(rehour);
                } else {
                    String date_rem = Appreference.ChangeOriginalPattern(false, reminder_date1.getText().toString(), datepattern);
                    re_date = Appreference.setDateTime(true, date_rem);
                    re_hour = Appreference.setDateTime(true, date_rem);
//                    re_date = Appreference.setTimeinDevicePattern(true,date_rem,datepattern);
//                    re_hour = Appreference.setTimeinDevicePattern(true,date_rem,datepattern);
                    Log.i("TaskDateUpdate", "reminder_date1 click event 30 " + re_date + "  " + re_hour);
                    re_hour = re_hour.split(" ")[1];
                    Log.i("remin", "en_hour" + re_hour);
                    int rehour = Integer.parseInt(re_hour.split(":")[0]);
                    Log.i("remin", "en_hour" + rehour);
                    re_date = re_date.split(" ")[0];
                    Log.i("TaskDateUpdate", "reminder_date1 click event 31 " + re_date + "  " + re_hour);
                    try {
                        // obtain date and time from initial string
                        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat targetFormat = new SimpleDateFormat("MMM dd");
                        Date date = originalFormat.parse(re_date);
                        Log.i("remin", "original " + date);
                        String formattedDate = targetFormat.format(date);
                        Log.i("remin", "target " + formattedDate);
                        for (int i = 0; i < values1.length; i++) {
                            Log.i("remin", "for" + values1[i]);
                            if (values1[i].equalsIgnoreCase(formattedDate)) {
                                Log.i("remin", "if" + values1[i]);
                                datePicker.setValue(i);
                            }
//                        else
//                        {
//                            Log.i("start","else");
////                            datePicker.setValue(0);
//                        }
                        }
                        // 20120821
                    } catch (Exception e) {
                        // wrong input
                        Appreference.printLog("TaskDateUpdate showDateTimeDialog", "Exception " + e.getMessage(), "WARN", null);
                    }

                    Log.i("TaskDateUpdate", "reminder_date1 click event 32 " + rehour);
                    if (rehour >= 13) {
                        if (rehour == 13) {
                            strTime = "1";
                        } else if (rehour == 14) {
                            strTime = "2";
                        } else if (rehour == 15) {
                            strTime = "3";
                        } else if (rehour == 16) {
                            strTime = "4";
                        } else if (rehour == 17) {
                            strTime = "5";
                        } else if (rehour == 18) {
                            strTime = "6";
                        } else if (rehour == 19) {
                            strTime = "7";
                        } else if (rehour == 20) {
                            strTime = "8";
                        } else if (rehour == 21) {
                            strTime = "9";
                        } else if (rehour == 22) {
                            strTime = "10";
                        } else if (rehour == 23) {
                            strTime = "11";
                        }
                        hourPicker.setValue(Integer.parseInt(strTime) - 1);
                        am_pmPicker.setValue(1);
                    } else {
                        hourPicker.setValue(rehour - 1);
                        am_pmPicker.setValue(0);
                    }
                }
//                hourPicker.setValue(end_time);
            }
//            minutePicker.setMinValue(0);
//            minutePicker.setMaxValue(59);
//            minutePicker.setWrapSelectorWheel(true);
//            // ArrayList<String> displayedValues = new ArrayList<String>();
//            String[] sdisplayedValues = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"};
//            List<String> ampmValues = new ArrayList<String>();
//            String[] am_pm = {"AM", "PM", "AM", "PM"};
//            // for (int i = 0; i < 60; i += 15) {
//            // displayedValues.add(String.format("%02d", i));
//            // }
//            minutePicker.setDisplayedValues(sdisplayedValues);
//            // ampmValues.add("AM");
//            // ampmValues.add("PM");
//            am_pmPicker.setMinValue(0);
//            am_pmPicker.setMaxValue(3);
//            am_pmPicker.setWrapSelectorWheel(true);
//            int am = 0;
//            am_pmPicker.setDisplayedValues(am_pm);
//            if (rightNow.get(Calendar.AM_PM) == 0) {
//                am = 0;
//            } else {
//                am = 1;
//            }
//            hourPicker.setOnValueChangedListener(new NumberPicker.
//                    OnValueChangeListener() {
//                @Override
//                public void onValueChange(NumberPicker picker, int
//                        oldVal, int newVal) {
////                   if(newVal==12||newVal==1)
////                    int ampm = oldVal - newVal;
////                    ampm = 0;
//                    if (newVal == 12 && oldVal == 11 || newVal == 11 && oldVal == 12) {
//
//                        if (am_pmPicker.getValue() == 1) {
//                            am_pmPicker.setValue(0);
//                        } else {
//                            am_pmPicker.setValue(1);
//                        }
//                    }
//                }
//            });

//            if (startOrEnd == 1) {
//                am_pmPicker.setValue(am);
//            } else if (startOrEnd == 2) {
//                am_pmPicker.setValue(start_am_pm);
//            } else if (startOrEnd == 3) {
//                am_pmPicker.setValue(end_am_pm);
//            }
            // if(rightNow.get(Calendar.MINUTE))

            int minute = rightNow.get(Calendar.MINUTE);
            int nextMinute = 0;
            if (minute >= 45 && minute <= 59)
                nextMinute = 3;
            else if (minute >= 30)
                nextMinute = 2;
            else if (minute >= 15)
                nextMinute = 1;
            else if (minute > 0 && minute < 15)
                nextMinute = 0;

            if (startOrEnd == 1) {
                String st_min = "";
                if (android.text.format.DateFormat.is24HourFormat(context)) {
                    String date_rem = Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern);
                    st_min = date_rem;
                } else {
                    String date_rem = Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern);
                    st_min = Appreference.setDateTime(true, date_rem);
                }
//                String st_min = start_date1.getText().toString();
                st_min = st_min.split(" ")[1];
                Log.i("start", "st_min" + st_min);
                int min = Integer.parseInt(st_min.split(":")[1]);
                Log.i("start", "st_min" + min);
                minutePicker.setValue(min);
//            } else if ((startOrEnd == 1 && startTimeSet) || !endTimeSet) {
            } else if (startOrEnd == 2) {
                String en_min = " ";
                if (android.text.format.DateFormat.is24HourFormat(context)) {
                    String date_rem = Appreference.ChangeOriginalPattern(true, end_date1.getText().toString(), datepattern);
                    en_min = date_rem;
                } else {
                    String date_rem = Appreference.ChangeOriginalPattern(false, end_date1.getText().toString(), datepattern);
                    en_min = Appreference.setDateTime(true, date_rem);
                }
                en_min = en_min.split(" ")[1];
                Log.i("end", "en_min" + en_min);
                int enmin = Integer.parseInt(en_min.split(":")[1]);
                Log.i("end", "en_min" + en_min);
                minutePicker.setValue(enmin);
//                minutePicker.setValue(start_minute);
//                datePicker.setValue(start_date);
            } else if (startOrEnd == 3) {
                String re_min = " ";
                if (android.text.format.DateFormat.is24HourFormat(context)) {
                    String date_rem = Appreference.ChangeOriginalPattern(true, reminder_date1.getText().toString(), datepattern);
                    re_min = date_rem;
                } else {
                    String date_rem = Appreference.ChangeOriginalPattern(false, reminder_date1.getText().toString(), datepattern);
                    re_min = Appreference.setDateTime(true, date_rem);
                }
                re_min = re_min.split(" ")[1];
                Log.i("end", "en_min" + re_min);
                int remin = Integer.parseInt(re_min.split(":")[1]);
                Log.i("end", "en_min" + re_min);
                minutePicker.setValue(remin);
//                minutePicker.setValue(end_minute);
//                datePicker.setValue(end_date);
            }

            set.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (!android.text.format.DateFormat.is24HourFormat(context) && getDeviceName().contains("samsung") && Build.MODEL.contains("J7")) {
                        Log.i("device name", "devicename is" + getDeviceName());
                        Toast.makeText(getApplicationContext(), "Please Change devicepattern for 24 Hour Format", Toast.LENGTH_SHORT).show();
                    } else {
                        String startDATE, endDate = null;
                        String[] values1 = datePicker.getDisplayedValues();
                        String[] values2 = hourPicker.getDisplayedValues();
                        String[] values3 = minutePicker.getDisplayedValues();
                        String[] values4 = am_pmPicker.getDisplayedValues();
                        String tdystr = values1[datePicker.getValue()];
                        int dateposition = datePicker.getValue();
                        int hourposition = hourPicker.getValue();
                        if (values1[datePicker.getValue()]
                                .equalsIgnoreCase("today")) {
                            tdystr = today;
                            Log.i("tdystr", "day " + tdystr);
                        } else {
                            tdystr = values1[datePicker.getValue()];
                            Log.i("tdystr", "day " + tdystr);
                        }
                        String am_pmpicker = "";
                        Log.i("tdystr", "hour " + values2[hourPicker.getValue()]);
                        Log.i("tdystr", "min " + values3[minutePicker.getValue()]);
                        //Log.i("tdystr", "am_pm " + values4[am_pmPicker.getValue()]);
                        if (!android.text.format.DateFormat.is24HourFormat(context)) {
                            toas = tdystr + " " + values2[hourPicker.getValue()] + " : " + values3[minutePicker.getValue()] + " " + values4[am_pmPicker.getValue()];
                        } else {
                            toas = tdystr + " " + values2[hourPicker.getValue()] + " : "
                                    + values3[minutePicker.getValue()];
                        }

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

                        if (startOrEnd == 1) {
                            int startTime = Integer.parseInt(strDateTime.split(":")[0]);
                            String startMin = strDateTime.split(":")[1];
                            Log.i("utctime", "starMin--->" + startMin);
                            if (values4 != null && am_pmPicker != null && values4[am_pmPicker.getValue()].equalsIgnoreCase("pm")) {
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
                                if (values4 != null && am_pmPicker != null && values4[am_pmPicker.getValue()]
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
                            String strfromdate = "";
                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                String date_rem = Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern);
                                strfromdate = date_rem;
                            } else {
                                String date_rem = Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern);
                                strfromdate = Appreference.setDateTime(true, date_rem);
                            }
//                        String strfromdate = Appreference.setDateTimepattern(true, start_date1.getText().toString());
                            String strfromday = strfromdate.split(" ")[0];
                            DateFormat selctedDateformate = null;
                            Date date = null;
                            if (!android.text.format.DateFormat.is24HourFormat(context)) {
                                try {
//                                if (getDeviceName().contains("samsung") && Build.MODEL.contains("J7")){
////                                    Log.i("device name","devicename is"+getDeviceName());
////                                    Toast.makeText(getApplicationContext(),"Please Change devicepattern for 24 Hour Format",Toast.LENGTH_SHORT).show();
//                                    selctedDateformate = new SimpleDateFormat("EEE MMM dd yyyy hh : mm a",Locale.ENGLISH);
//                                    date=selctedDateformate.parse(toas);
//                                }else {
                                    selctedDateformate = new SimpleDateFormat("MMM dd yyyy hh : mm a");
                                    Log.i("time123", "toas----->" + toas);
                                    date = selctedDateformate.parse(toas);
//                                }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Appreference.printLog("TaskDateUpdate set.setOnClickListener", "Exception " + e.getMessage(), "WARN", null);
                                }
                            } else {
                                selctedDateformate = new SimpleDateFormat("MMM dd yyyy HH : mm");
                                try {
                                    date = selctedDateformate.parse(toas);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Appreference.printLog("TaskDateUpdate set.setOnClickListener", "Exception " + e.getMessage(), "WARN", null);
                                }
                            }
//                        DateFormat selctedDateformate = new SimpleDateFormat("yyyy MMM dd hh : mm a");
//                        Date date = null;
//                        try {
//                            date = selctedDateformate.parse(yyyy + " " + toas);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
                            SimpleDateFormat day1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String strenddate = day1.format(date);
//                        String strendmonth = strenddate.split("-")[1];
//                        int strendmonth1 = Integer.parseInt(strendmonth);
                            String strendday = strenddate.split(" ")[0];
                            int strendday1 = Integer.parseInt(strendday.split("-")[2]);
                            if (strfromday.compareTo(strendday) <= 0) {
                                if (!android.text.format.DateFormat.is24HourFormat(context)) {
                                    if (CheckReminderIsValid(
                                            values4[am_pmPicker.getValue()], dateposition,
                                            strTime, tm, true)) {
                                        String startdate2 = "";
                                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                                            String dt = Appreference.ChangeDevicePattern(true, strenddate, datepattern);
                                            start_date1.setText(dt);
                                        } else {
                                            String ss = Appreference.setDateTime(false, strenddate);
                                            String dt = Appreference.ChangeDevicePattern(false, ss, datepattern);
                                            start_date1.setText(dt);
                                        }
                                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                                            String dt = Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern);
                                            startdate2 = dt;
                                        } else {
                                            String dt = Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern);
                                            startdate2 = Appreference.setDateTime(true, dt);
                                        }
//                                startdate2 = start_date1.getText().toString();
                                        Log.i("time", "datecheck " + datecheck);
                                        Log.i("time", "time1 " + time1);
                                        Log.i("time", "dur_check " + dur_check);
                                        if (datecheck) {
                                            try {
                                        *//*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                        String startdate_3 = sdf.format(startdate2);
                                        DateFormat formatter = DateFormat.getDateInstance();
                                        Date date_1 = formatter.parse(startdate2);*//*
                                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                Date ddate = null;
                                                try {
                                                    ddate = dateFormat.parse(startdate2);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                    Appreference.printLog("TaskDateUpdate set.setOnClickListener", "Exception " + e.getMessage(), "WARN", null);
                                                }
                                                Calendar cl = Calendar.getInstance();
                                                cl.setTime(ddate);
                                                if (dur_check.equalsIgnoreCase("hrs")) {
                                                    cl.add(Calendar.HOUR, time1);
                                                } else if (dur_check.equalsIgnoreCase("mins")) {
                                                    cl.add(Calendar.MINUTE, time1);
                                                }
                                                startdate2 = dateFormat.format(cl.getTime());
                                                Log.i("time", "startdate_3 after " + startdate2);
                                                if (android.text.format.DateFormat.is24HourFormat(context)) {
                                                    String aa = Appreference.ChangeDevicePattern(true, startdate2, datepattern);
                                                    end_date1.setText(aa);
                                                } else {
                                                    String bb = Appreference.setDateTime(false, startdate2);
                                                    end_date1.setText(Appreference.ChangeDevicePattern(false, bb, datepattern));
                                                }
//                                        end_date1.setText(startdate2);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Appreference.printLog("TaskDateUpdate set.setOnClickListener", "Exception " + e.getMessage(), "WARN", null);
                                            }
                                        } else {
                                            String fromdate2 = " ";
                                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                                fromdate2 = startdate2.split(" ")[0] + " 23:55:00";
                                                String aa = Appreference.ChangeDevicePattern(true, fromdate2, datepattern);
                                                end_date1.setText(aa);
                                            } else {
                                                fromdate2 = startdate2.split(" ")[0] + " 11:55:00 PM";
                                                String bb = Appreference.setDateTime(false, fromdate2);
                                                end_date1.setText(Appreference.ChangeDevicePattern(false, bb, datepattern));
                                            }
//                                    end_date1.setText(fromdate2);
                                        }

//                                String todate2 = end_date1.getText().toString();
//                                String todate3 = todate2.split(" ")[0] + " 23:40:00";
//                                reminder_date1.setText(start_date1.getText().toString());
//                            String date_start_temp[] = strTime.split(":");
//                            start_date = Integer.parseInt(date_start_temp[0]);
                                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date ddate = null;
                                        try {
                                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                                ddate = dateFormat.parse(Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern));
                                            } else {
                                                ddate = dateFormat.parse(Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern)));
                                            }
//                                    ddate = dateFormat.parse(start_date1.getText().toString());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                            Appreference.printLog("TaskDateUpdate set.setOnClickListener", "Exception " + e.getMessage(), "WARN", null);
                                        }
                                        Calendar cl = Calendar.getInstance();
                                        if (ddate != null) {
                                            cl.setTime(ddate);
                                            cl.add(Calendar.MINUTE, 1);
                                            reminder_date = dateFormat.format(cl.getTime());
                                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                                reminder_date1.setText(Appreference.ChangeDevicePattern(true, reminder_date, datepattern));
                                            } else {
                                                Log.i("TaskDateUpdate", "reminder3 value is " + reminder_date);
                                                reminder_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, reminder_date), datepattern));
                                            }
//                                    reminder_date1.setText(reminder_date);
                                        }
                                        start_date = dateposition;
                                        start_time = hourPicker.getValue();
                                        start_minute = minutePicker.getValue();
                                        start_am_pm = am_pmPicker.getValue();
                                        startTimeSet = true;

                                        strDATE = values1[datePicker.getValue()];
                                    } else {
                                        showToast(context.getString(R.string.Kindly_slct_future_time));
                                    }
                                } else {
                                    String dt = Appreference.setDateTime(false, strenddate);
                                    Log.i("TaskDateUpdate", "dt value is" + dt);
                                    Log.i("TaskDateUpdate", "dateposition is" + dateposition);
                                    Log.i("TaskdateUpdate", "strtime" + strTime);
                                    Log.i("TaskDateUpdate", "tm " + tm);
                                    if (CheckReminderIsValid(dt.split(" ")[2], dateposition, strTime, tm, true)) {
                                        String startdate2 = "";
                                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                                            String strdate = Appreference.ChangeDevicePattern(true, strenddate, datepattern);
                                            start_date1.setText(strdate);
                                        } else {
                                            String stdate = Appreference.setDateTime(false, strenddate);
                                            start_date1.setText(Appreference.ChangeDevicePattern(false, stdate, datepattern));
                                        }
                                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                                            startdate2 = Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern);
                                        } else {
                                            String stdat = Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern);
                                            startdate2 = Appreference.setDateTime(true, stdat);

                                        }
//                                startdate2 = start_date1.getText().toString();
                                        Log.i("time", "datecheck " + datecheck);
                                        Log.i("time", "time1 " + time1);
                                        Log.i("time", "dur_check " + dur_check);
                                        if (datecheck) {
                                            try {
                                        *//*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                        String startdate_3 = sdf.format(startdate2);
                                        DateFormat formatter = DateFormat.getDateInstance();
                                        Date date_1 = formatter.parse(startdate2);*//*
                                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                Date ddate = null;
                                                try {
                                                    ddate = dateFormat.parse(startdate2);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                    Appreference.printLog("TaskDateUpdate set.setOnClickListener", "Exception " + e.getMessage(), "WARN", null);
                                                }
                                                Calendar cl = Calendar.getInstance();
                                                cl.setTime(ddate);
                                                if (dur_check.equalsIgnoreCase("hrs")) {
                                                    cl.add(Calendar.HOUR, time1);
                                                } else if (dur_check.equalsIgnoreCase("mins")) {
                                                    cl.add(Calendar.MINUTE, time1);
                                                }
                                                startdate2 = dateFormat.format(cl.getTime());
                                                Log.i("time", "startdate_3 after " + startdate2);
                                                if (android.text.format.DateFormat.is24HourFormat(context)) {
                                                    end_date1.setText(Appreference.ChangeDevicePattern(true, startdate2, datepattern));
                                                } else {
                                                    end_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, startdate2), datepattern));
                                                }
//                                        end_date1.setText(startdate2);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Appreference.printLog("TaskDateUpdate set.setOnClickListener", "Exception " + e.getMessage(), "WARN", null);
                                            }
                                        } else {
                                            String fromdate2 = " ";
                                            Log.i("Datetimeformat", "fromdate2 value is " + fromdate2);
                                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                                fromdate2 = startdate2.split(" ")[0] + " 23:55:00";
                                                end_date1.setText(Appreference.ChangeDevicePattern(true, fromdate2, datepattern));
                                            } else {
                                                fromdate2 = startdate2.split(" ")[0] + " 11:55:00 PM";
                                                String vv = Appreference.setDateTime(false, fromdate2);
                                                end_date1.setText(Appreference.ChangeDevicePattern(false, vv, datepattern));
                                            }
//                                    end_date1.setText(fromdate2);
                                        }

//                                String todate2 = end_date1.getText().toString();
//                                String todate3 = todate2.split(" ")[0] + " 23:40:00";
//                                reminder_date1.setText(start_date1.getText().toString());
//                            String date_start_temp[] = strTime.split(":");
//                            start_date = Integer.parseInt(date_start_temp[0]);
                                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date ddate = null;
                                        try {
                                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                                ddate = dateFormat.parse(Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern));
                                            } else {
                                                String ff = Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern);
                                                ddate = dateFormat.parse(Appreference.setDateTime(true, ff));
                                            }
//                                    ddate = dateFormat.parse(start_date1.getText().toString());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                            Appreference.printLog("TaskDateUpdate set.setOnClickListener", "Exception " + e.getMessage(), "WARN", null);
                                        }
                                        Calendar cl = Calendar.getInstance();
                                        if (ddate != null) {
                                            cl.setTime(ddate);
                                            cl.add(Calendar.MINUTE, 1);
                                            reminder_date = dateFormat.format(cl.getTime());
                                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                                reminder_date1.setText(Appreference.ChangeDevicePattern(true, reminder_date, datepattern));
                                            } else {
                                                String vv = Appreference.setDateTime(false, reminder_date);
                                                Log.i("TaskDateUpdate", "vv" + vv);
                                                reminder_date1.setText(Appreference.ChangeDevicePattern(false, vv, datepattern));
                                            }
//                                    reminder_date1.setText(reminder_date);
                                        }
                                        start_date = dateposition;
                                        start_time = hourPicker.getValue();
                                        start_minute = minutePicker.getValue();
//                                    start_am_pm = am_pmPicker.getValue();
                                        startTimeSet = true;

                                        strDATE = values1[datePicker.getValue()];
                                    } else {
                                        showToast(context.getString(R.string.Kindly_slct_future_time));
                                    }
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Kindly select future date", Toast.LENGTH_LONG).show();
                            }
                        } else if (startOrEnd == 2) {
                            String startrem = "";
                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                startrem = Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern);
                            } else {
                                String sss = Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern);
                                startrem = Appreference.setDateTime(true, sss);
                            }
//                        String startrem = start_date1.getText().toString();
                            String startampm = checkAmorPmfunction(startrem);  // am/pm
                            String startrem1 = startrem.split(":")[0];  // yyyy-mm-dd hh
                            String starttime = startrem1.split(" ")[1] + ":" + startrem.split(":")[1]; // hh:mm
                            Log.i("starttime", "starttime " + starttime);

                            int startTime = Integer.parseInt(strDateTime.split(":")[0]);
                            String startMin = strDateTime.split(":")[1];
                            if (values4 != null && am_pmPicker != null && values4[am_pmPicker.getValue()]
                                    .equalsIgnoreCase("pm")) {
                                Log.i("schedulecall", "pm picker");
                                if (startTime == 1) {
                                    enTime = "13" + ":" + String.valueOf(startMin);
                                } else if (startTime == 2) {
                                    enTime = "14" + ":" + String.valueOf(startMin);
                                } else if (startTime == 3) {
                                    enTime = "15" + ":" + String.valueOf(startMin);
                                } else if (startTime == 4) {
                                    enTime = "16" + ":" + String.valueOf(startMin);
                                } else if (startTime == 5) {
                                    enTime = "17" + ":" + String.valueOf(startMin);
                                } else if (startTime == 6) {
                                    enTime = "18" + ":" + String.valueOf(startMin);
                                } else if (startTime == 7) {
                                    enTime = "19" + ":" + String.valueOf(startMin);
                                } else if (startTime == 8) {
                                    enTime = "20" + ":" + String.valueOf(startMin);
                                } else if (startTime == 9) {
                                    enTime = "21" + ":" + String.valueOf(startMin);
                                } else if (startTime == 10) {
                                    enTime = "22" + ":" + String.valueOf(startMin);
                                } else if (startTime == 11) {
                                    enTime = "23" + ":" + String.valueOf(startMin);
                                }
//                            else if (startTime == 12) {
//                                enTime = "00" + ":"
//                                        + String.valueOf(startMin);
//                            }
                                else
                                    enTime = strDateTime;
                            } else {

                                if (values4 != null && am_pmPicker != null && values4[am_pmPicker.getValue()]
                                        .equalsIgnoreCase("am")) {
                                    Log.i("schedulecall", "am picker");
                                    if (startTime == 12) {
                                        Log.i("schedulecall", "am picker 12");
                                        enTime = "00" + ":"
                                                + String.valueOf(startMin);
                                    } else {
                                        Log.i("schedulecall", "am picker !12");
                                        enTime = strDateTime;
                                    }
                                } else {
                                    Log.i("schedulecall", "24 format");
                                    enTime = strDateTime;
                                }
                            }
//                        if ((strDATE.equals(values1[datePicker.getValue()]) && values4[am_pmPicker
//                                .getValue()].equalsIgnoreCase("pm"))
//                                || (!strDATE.equals(values1[datePicker
//                                .getValue()]))) {
                            String strfromdate = "";
                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                strfromdate = Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern);
                            } else {
                                String ss = Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern);
                                strfromdate = Appreference.setDateTime(true, ss);
                            }

//                        String strfromdate = start_date1.getText().toString();
//                        String strfrommonth = strfromdate.split("-")[1];
//                        int strfrommonth1 = Integer.parseInt(strfrommonth);
                            String strfromday = strfromdate.split(" ")[0];
                            int strfromday1 = Integer.parseInt(strfromday.split("-")[2]);
//                        Log.i("schedule", "strfrommonth-->" + strfrommonth);
                            Log.i("schedule", "strfromday-->" + strfromday1);
                            Log.i("schedule", "toas-->" + toas);
                            DateFormat selctedDateformate = null;
                            Date date = null;
                            if (!android.text.format.DateFormat.is24HourFormat(context)) {
                                if (getDeviceName().contains("samsung") && Build.MODEL.contains("J7")) {
//                                    Log.i("device name","devicename is"+getDeviceName());
//                                    Toast.makeText(getApplicationContext(),"Please Change devicepattern for 24 Hour Format",Toast.LENGTH_SHORT).show();
                                    selctedDateformate = new SimpleDateFormat("EEE MMM dd yyyy hh : mm a");
                                    try {
                                        date = selctedDateformate.parse(toas);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        Appreference.printLog("TaskDateUpdate set.setOnClickListener", "Exception " + e.getMessage(), "WARN", null);
                                    }
                                } else {
                                    selctedDateformate = new SimpleDateFormat("MMM dd yyyy hh : mm a");
                                    try {
                                        date = selctedDateformate.parse((toas));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                        Appreference.printLog("TaskDateUpdate set.setOnClickListener", "Exception " + e.getMessage(), "WARN", null);
                                    }
                                }
                            } else {
                                selctedDateformate = new SimpleDateFormat("MMM dd yyyy HH : mm");
                                try {
                                    Log.i("schedule", "selecteddateformat is" + date);
                                    Log.i("schedule", "selecteddateformat is" + selctedDateformate.toString());
                                    Log.i("schedule", "selecteddateformat is" + toas);
                                    date = selctedDateformate.parse(toas.toString());
                                    Log.i("schedule", "selecteddateformat is" + date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Appreference.printLog("TaskDateUpdate set.setOnClickListener", "Exception " + e.getMessage(), "WARN", null);
                                }
                            }
                            SimpleDateFormat day1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String strenddate = " ";
                            if (date != null) {
                                strenddate = day1.format(date);
                            } else {
                                try {
                                    date = selctedDateformate.parse(toas);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                strenddate = day1.format(date);
                            }

                            String strendday = strenddate.split(" ")[0];
                            int strendday1 = Integer.parseInt(strendday.split("-")[2]);
                            Log.i("schedule", "strfromday-->" + strendday1);

                            // days count between startdate annd end date
                      *//*  SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
                        int daysCount = 0;

                        try {
                            Date date1 = myFormat.parse(strfromday);
                            Date date2 = myFormat.parse(strendday);
                            long diff = date2.getTime() - date1.getTime();
                            System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                            daysCount = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }*//*

                            if (strfromday.compareTo(strendday) <= 0) {
                                if (!android.text.format.DateFormat.is24HourFormat(context)) {
                                    if (CheckReminderIsValid(
                                            values4[am_pmPicker.getValue()],
                                            dateposition, enTime, starttime, true)) {

                                        Log.i("schedule", "entry if");
                                        Log.i("End", "Date is " + strenddate);
                                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                                            end_date1.setText(Appreference.ChangeDevicePattern(true, strenddate, datepattern));
                                        } else {
                                            String dddd = Appreference.setDateTime(false, strenddate);
                                            end_date1.setText(Appreference.ChangeDevicePattern(false, dddd, datepattern));
                                        }
//                                end_date1.setText(strenddate);
                                *//*String endmin = values3[minutePicker.getValue()];
                                Log.i("End", "endmin is " + endmin);
                                int endmin1 = Integer.parseInt(endmin);
                                if (endmin1 != 0) {
                                    endmin1 = endmin1 - 15;
                                    toas = tdystr + " " + values2[hourPicker.getValue()] + " : "
                                            + endmin1 + " "
                                            + values4[am_pmPicker.getValue()];
                                } else {
                                    Log.d("Hour is ", String.valueOf(hourPicker.getValue()) + " values2 :" + values2);
                                    String hourval = values2[hourPicker.getValue()];
                                    int hourval1 = Integer.parseInt(hourval);
                                    hourval1 = hourval1 - 1;
                                    if (hourval1 == 0) {
                                        toas = tdystr + " " + "12" + " : "
                                                + "45" + " "
                                                + values4[am_pmPicker.getValue()];
                                    } else {
                                        toas = tdystr + " " + hourval1 + " : "
                                                + "45" + " "
                                                + values4[am_pmPicker.getValue()];
                                    }
                                }
                                Log.i("Date", "is " + toas);
                                selctedDateformate = new SimpleDateFormat("yyyy MMM dd hh : mm a");
                                date = null;
                                try {
                                    date = selctedDateformate.parse(yyyy + " " + toas);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                day1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String strremdate = day1.format(date);*//*
                                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date ddate = null;
                                        try {
                                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                                ddate = dateFormat.parse(Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern));
                                            } else {
                                                String dddd = Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern);
                                                ddate = dateFormat.parse(Appreference.setDateTime(true, dddd));
                                            }
//                                    ddate = dateFormat.parse(start_date1.getText().toString());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                            Appreference.printLog("TaskDateUpdate set.setOnClickListener", "Exception " + e.getMessage(), "WARN", null);
                                        }
                                        Calendar cl = Calendar.getInstance();
                                        if (ddate != null) {
                                            cl.setTime(ddate);
                                            cl.add(Calendar.MINUTE, 1);
                                            reminder_date = dateFormat.format(cl.getTime());
                                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                                reminder_date1.setText(Appreference.ChangeDevicePattern(true, reminder_date, datepattern));
                                            } else {
                                                String aaa = Appreference.setDateTime(false, reminder_date);
                                                Log.i("TaskDateUpdate", "aaaa" + aaa);
                                                reminder_date1.setText(Appreference.ChangeDevicePattern(false, aaa, datepattern));
                                            }
//                                    reminder_date1.setText(reminder_date);
                                        }
//                                reminder_date1.setText(start_date1.getText().toString());

                                        enDate = values1[datePicker.getValue()];

                                        end_date = dateposition;
                                        end_time = hourPicker.getValue();
                                        end_minute = minutePicker.getValue();
                                        end_am_pm = am_pmPicker.getValue();

                                        endTimeSet = true;
                                    } else {
                                        showToast(context.getString(R.string.Kindly_slct_grt_than_value_from_start_time));
                                    }
                                } else {

                                    String dt = Appreference.setDateTime(false, strenddate);
                                    if (CheckReminderIsValid(
                                            dt.split(" ")[2],
                                            dateposition, enTime, starttime, true)) {

                                        Log.i("schedule", "entry if");
                                        Log.i("End", "Date is " + strenddate);
                                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                                            end_date1.setText(Appreference.ChangeDevicePattern(true, strenddate, datepattern));
                                        } else {
                                            end_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, strenddate), datepattern));
                                        }
//                                end_date1.setText(strenddate);
                                *//*String endmin = values3[minutePicker.getValue()];
                                Log.i("End", "endmin is " + endmin);
                                int endmin1 = Integer.parseInt(endmin);
                                if (endmin1 != 0) {
                                    endmin1 = endmin1 - 15;
                                    toas = tdystr + " " + values2[hourPicker.getValue()] + " : "
                                            + endmin1 + " "
                                            + values4[am_pmPicker.getValue()];
                                } else {
                                    Log.d("Hour is ", String.valueOf(hourPicker.getValue()) + " values2 :" + values2);
                                    String hourval = values2[hourPicker.getValue()];
                                    int hourval1 = Integer.parseInt(hourval);
                                    hourval1 = hourval1 - 1;
                                    if (hourval1 == 0) {
                                        toas = tdystr + " " + "12" + " : "
                                                + "45" + " "
                                                + values4[am_pmPicker.getValue()];
                                    } else {
                                        toas = tdystr + " " + hourval1 + " : "
                                                + "45" + " "
                                                + values4[am_pmPicker.getValue()];
                                    }
                                }
                                Log.i("Date", "is " + toas);
                                selctedDateformate = new SimpleDateFormat("yyyy MMM dd hh : mm a");
                                date = null;
                                try {
                                    date = selctedDateformate.parse(yyyy + " " + toas);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                day1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String strremdate = day1.format(date);*//*
                                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date ddate = null;
                                        try {
                                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                                ddate = dateFormat.parse(Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern));
                                            } else {
                                                ddate = dateFormat.parse(Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern)));
                                            }
//                                    ddate = dateFormat.parse(start_date1.getText().toString());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                            Appreference.printLog("TaskDateUpdate set.setOnClickListener", "Exception " + e.getMessage(), "WARN", null);
                                        }
                                        Calendar cl = Calendar.getInstance();
                                        if (ddate != null) {
                                            cl.setTime(ddate);
                                            cl.add(Calendar.MINUTE, 1);
                                            reminder_date = dateFormat.format(cl.getTime());
                                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                                reminder_date1.setText(Appreference.ChangeDevicePattern(true, reminder_date, datepattern));
                                            } else {
                                                Log.i("TaskDateupdate", "reminder date" + reminder_date);
                                                reminder_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, reminder_date), datepattern));
                                            }
//                                    reminder_date1.setText(reminder_date);
                                        }
//                                reminder_date1.setText(start_date1.getText().toString());

                                        enDate = values1[datePicker.getValue()];

                                        end_date = dateposition;
                                        end_time = hourPicker.getValue();
                                        end_minute = minutePicker.getValue();
                                        // end_am_pm = am_pmPicker.getValue();

                                        endTimeSet = true;
                                    } else {
                                        showToast(context.getString(R.string.Kindly_slct_grt_than_value_from_start_time));
                                    }
                                }
                            } else {
                                showToast(context.getString(R.string.Kindly_slct_grt_than_value_from_start_date));
                            }
                        } else if (startOrEnd == 3) {

                            String startrem = "";
                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                startrem = Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern);
                            } else {
                                String ss = Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern);
                                startrem = Appreference.setDateTime(true, ss);
                            }
//                        String startrem = start_date1.getText().toString();
                            String startampm = checkAmorPmfunction(startrem);  // am/pm
                            String startrem1 = startrem.split(":")[0];  // yyyy-mm-dd hh
                            fromtime = startrem1.split(" ")[1] + ":" + startrem.split(":")[1]; // hh:mm
                            Log.i("starttime", "starttime " + fromtime);

                            String endrem = "";
                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                endrem = Appreference.ChangeOriginalPattern(true, end_date1.getText().toString(), datepattern);
                            } else {
                                String aaq = Appreference.ChangeOriginalPattern(false, end_date1.getText().toString(), datepattern);
                                endrem = Appreference.setDateTime(true, aaq);
                            }

//                        String endrem = end_date1.getText().toString();
                            String endampm = checkAmorPmfunction(endrem);   // am/pm
                            String endrem1 = endrem.split(":")[0];  // yyyy-mm-dd hh
                            endTime = endrem1.split(" ")[1] + ":" + endrem.split(":")[1]; // hh:mm
                            Log.i("endTime", "endTime " + endTime);

                            int startTime = Integer.parseInt(strDateTime.split(":")[0]);
                            String startMin = strDateTime.split(":")[1];
                            if (values4 != null && am_pmPicker != null && values4[am_pmPicker.getValue()]
                                    .equalsIgnoreCase("pm")) {
                                Log.i("schedulecall", "pm picker");
                                if (startTime == 1) {
                                    enTime = "13" + ":" + String.valueOf(startMin);
                                } else if (startTime == 2) {
                                    enTime = "14" + ":" + String.valueOf(startMin);
                                } else if (startTime == 3) {
                                    enTime = "15" + ":" + String.valueOf(startMin);
                                } else if (startTime == 4) {
                                    enTime = "16" + ":" + String.valueOf(startMin);
                                } else if (startTime == 5) {
                                    enTime = "17" + ":" + String.valueOf(startMin);
                                } else if (startTime == 6) {
                                    enTime = "18" + ":" + String.valueOf(startMin);
                                } else if (startTime == 7) {
                                    enTime = "19" + ":" + String.valueOf(startMin);
                                } else if (startTime == 8) {
                                    enTime = "20" + ":" + String.valueOf(startMin);
                                } else if (startTime == 9) {
                                    enTime = "21" + ":" + String.valueOf(startMin);
                                } else if (startTime == 10) {
                                    enTime = "22" + ":" + String.valueOf(startMin);
                                } else if (startTime == 11) {
                                    enTime = "23" + ":" + String.valueOf(startMin);
                                }
//                            else if (startTime == 12) {
//                                enTime = "00" + ":"
//                                        + String.valueOf(startMin);
//                            }
                                else
                                    enTime = strDateTime;
                            } else {

                                if (values4 != null && am_pmPicker != null && values4[am_pmPicker.getValue()]
                                        .equalsIgnoreCase("am")) {
                                    Log.i("schedulecall", "am picker");
                                    if (startTime == 12) {
                                        Log.i("schedulecall", "am picker 12");
                                        enTime = "00" + ":"
                                                + String.valueOf(startMin);
                                    } else {
                                        Log.i("schedulecall", "am picker !12");
                                        enTime = strDateTime;
                                    }
                                } else {
                                    Log.i("schedulecall", "24 format");
                                    enTime = strDateTime;
                                }
                            }
                            String strdate = "";
                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                strdate = Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern);
                            } else {
                                String hh = Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern);
                                strdate = Appreference.setDateTime(true, hh);
                            }

//                        String strdate = start_date1.getText().toString();
                            String strfromdate1 = strdate.split(" ")[0];
                            Log.i("schedule", "strfromdate1-->" + strfromdate1);

                            String strdate1 = "";
                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                strdate1 = Appreference.ChangeOriginalPattern(true, end_date1.getText().toString(), datepattern);
                            } else {
                                String ssss = Appreference.ChangeOriginalPattern(false, end_date1.getText().toString(), datepattern);
                                strdate1 = Appreference.setDateTime(true, ssss);
                            }

//                        String strdate1 = end_date1.getText().toString();
                            String strenddate1 = strdate1.split(" ")[0];
                            Log.i("schedule", "strenddate1-->" + strenddate1);

                            DateFormat selctedDateformate = null;
                            Date date = null;
                            if (!android.text.format.DateFormat.is24HourFormat(context)) {
                                selctedDateformate = new SimpleDateFormat("MMM dd yyyy hh : mm a");
                                try {
                                    date = selctedDateformate.parse(toas);
                                    Log.i("if!24", "strenddate1-->" + date.toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Appreference.printLog("TaskDateUpdate set.setOnClickListener", "Exception " + e.getMessage(), "WARN", null);
                                }
                            } else {
                                selctedDateformate = new SimpleDateFormat("MMM dd yyyy HH : mm");
                                try {
                                    date = selctedDateformate.parse(toas);
                                    Log.i("if24", "strenddate1-->" + date.toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                    Appreference.printLog("TaskDateUpdate set.setOnClickListener", "Exception " + e.getMessage(), "WARN", null);
                                }
                            }
                            SimpleDateFormat day1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String strremindate = day1.format(date);
                            String strremindate1 = strremindate.split(" ")[0];
                            Log.i("schedule", "strremindate1-->" + strremindate1);

                            String remampm = checkAmorPmfunction(strremindate);   // am/pm
                            Log.i("Remhrmin", "enTime" + enTime);

                            Log.i("Time", "fromtime" + fromtime);
                            Log.i("Time", "endTime" + endTime);
                            Log.i("Time", "pickertime" + enTime);
//                        if (strdate[0].equalsIgnoreCase(toas.split(" ")[0]) && endingdate >= startingdate) {
                            if ((strfromdate1.compareTo(strremindate1) <= 0) && (strenddate1.compareTo(strremindate1) >= 0)) {
//                            int errer = strfromdate1.compareTo(strenddate1);
//                            Log.i("errer", "errer" + errer);
                                if (strfromdate1.compareTo(strenddate1) > 0) {
                                    if (strremindate1.equals(strenddate1)) {
                                        if (endampm.equalsIgnoreCase("PM")) {
// am or pm
                                            if (remampm.equalsIgnoreCase("AM") || remampm.equalsIgnoreCase("PM")) {
                                                flag = true;
                                            }
                                        } else if (endampm.equalsIgnoreCase("AM")) {
// am
                                            if (remampm.equalsIgnoreCase("AM")) {
                                                flag = true;
                                            }
                                        }
                                    } else if (!strremindate1.equals(strenddate1)) {
                                        if (strfromdate1.equals(strenddate1)) {
                                            if (startampm.equalsIgnoreCase("AM")) {
                                                // am or pm
                                                if (remampm.equalsIgnoreCase("AM") || remampm.equalsIgnoreCase("PM")) {
                                                    flag = true;
                                                }
                                            } else if (startampm.equalsIgnoreCase("PM")) {
                                                // pm
                                                if (remampm.equalsIgnoreCase("PM")) {
                                                    flag = true;
                                                } else {
                                                    Toast.makeText(context, "Please select appropriate time", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    }

                                } else if (strfromdate1.compareTo(strenddate1) == 0) {
                                    Log.i("comp", "reminderset ");
                                    if (startampm.equalsIgnoreCase("AM") && endampm.equalsIgnoreCase("AM")) {
                                        //am
                                        if (remampm.equalsIgnoreCase("AM")) {
                                            int comp = endTime.compareTo(enTime);
                                            Log.i("comp", "comp1 " + comp);
                                            if ((endTime.compareTo(enTime) > 0) && (fromtime.compareTo(enTime) < 0))
                                                flag = true;
                                            else
                                                Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (startampm.equalsIgnoreCase("PM") && endampm.equalsIgnoreCase("PM")) {
                                        //pm
                                        if (remampm.equalsIgnoreCase("PM") || remampm.equalsIgnoreCase("AM")) {
                                            int comp = endTime.compareTo(enTime);
                                            Log.i("comp", "comp2 " + comp);
                                            if ((endTime.compareTo(enTime) > 0) && (fromtime.compareTo(enTime) < 0))
                                                flag = true;
                                            else
                                                Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (startampm.equalsIgnoreCase("AM") && endampm.equalsIgnoreCase("PM")) {
                                        //pm
                                        if (remampm.equalsIgnoreCase("AM") || remampm.equalsIgnoreCase("PM")) {
                                            int comp = endTime.compareTo(enTime);
                                            Log.i("comp", "rem " + fromtime.compareTo(enTime));
                                            Log.i("comp", "comp3 " + comp);
                                            if ((endTime.compareTo(enTime) > 0) && (fromtime.compareTo(enTime) < 0))
                                                flag = true;
                                            else
                                                Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (startampm.equalsIgnoreCase("PM") && endampm.equalsIgnoreCase("AM")) {
                                        //pm
                                        if (remampm.equalsIgnoreCase("AM") || remampm.equalsIgnoreCase("PM")) {
                                            int comp = endTime.compareTo(enTime);
                                            int comp1 = fromtime.compareTo(enTime);
                                            Log.i("comp", "comp4 " + comp);
                                            if ((endTime.compareTo(enTime) > 0) && (fromtime.compareTo(enTime) > 0))
                                                flag = true;
                                            else
                                                Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                } else if (strfromdate1.compareTo(strenddate1) < 0) {
                                    Log.i("comp", "reminderset ");
                                    if (startampm.equalsIgnoreCase("AM") && endampm.equalsIgnoreCase("AM")) {
                                        //am
                                        if (remampm.equalsIgnoreCase("AM")) {
                                            int comp = endTime.compareTo(enTime);
                                            Log.i("comp", "comp5 " + comp);
                                            if ((endTime.compareTo(enTime) > 0) && (fromtime.compareTo(enTime) < 0))
                                                flag = true;
                                            else
                                                Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (startampm.equalsIgnoreCase("PM") && endampm.equalsIgnoreCase("PM")) {
                                        //pm
                                        if (remampm.equalsIgnoreCase("PM")) {
                                            int comp = endTime.compareTo(enTime);
                                            Log.i("comp", "comp6 " + comp);
                                            if ((endTime.compareTo(enTime) > 0) && (fromtime.compareTo(enTime) < 0))
                                                flag = true;
                                            else
                                                Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
                                        } else if (remampm.equalsIgnoreCase("AM")) {
                                            int comp = endTime.compareTo(enTime);
                                            Log.i("comp", "comp66 " + comp);
                                            if ((endTime.compareTo(enTime) > 0) && (fromtime.compareTo(enTime) > 0))
                                                flag = true;
                                            else
                                                Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (startampm.equalsIgnoreCase("AM") && endampm.equalsIgnoreCase("PM")) {
                                        //pm
                                        if (remampm.equalsIgnoreCase("AM") || remampm.equalsIgnoreCase("PM")) {
                                            int comp = endTime.compareTo(enTime);
                                            Log.i("comp", "comp7 " + comp);
                                            if ((endTime.compareTo(enTime) > 0) && (fromtime.compareTo(enTime) < 0))
                                                flag = true;
                                            else
                                                Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (startampm.equalsIgnoreCase("PM") && endampm.equalsIgnoreCase("AM")) {
                                        //pm
                                        if (remampm.equalsIgnoreCase("AM")) {
                                            int comp = endTime.compareTo(enTime);
                                            int comp1 = fromtime.compareTo(enTime);
                                            Log.i("comp", "comp8 " + comp);
//                                        if (strenddate1.compareTo(strremindate1) == 0) {
                                            if ((endTime.compareTo(enTime) > 0) && (fromtime.compareTo(enTime) > 0))
                                                flag = true;
                                            else
                                                Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
//                                    }
                                        } else if (remampm.equalsIgnoreCase("PM")) {
                                            int comp = endTime.compareTo(enTime);
                                            int comp1 = fromtime.compareTo(enTime);
                                            Log.i("comp", "comp9 " + comp);
                                            if ((endTime.compareTo(enTime) > 0) && (fromtime.compareTo(enTime) > 0))
                                                flag = true;
                                            else
                                                Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                Log.i("DATE", "flag value " + flag);
                                if (flag) {
                                    if (android.text.format.DateFormat.is24HourFormat(context)) {
                                        reminder_date1.setText(Appreference.ChangeDevicePattern(true, strremindate, datepattern));
                                    } else {
                                        String nnn = Appreference.setDateTime(false, strremindate);
                                        Log.i("TaskDateUpdate", "nnn" + nnn);
                                        reminder_date1.setText(Appreference.ChangeDevicePattern(false, nnn, datepattern));
                                    }
                                    enDate = values1[datePicker.getValue()];

                                    end_date = dateposition;
                                    end_time = hourPicker.getValue();
                                    end_minute = minutePicker.getValue();
                                    //end_am_pm = am_pmPicker.getValue();

                                    endTimeSet = true;
                                    flag = false;
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Kindly select between start and end date", Toast.LENGTH_LONG).show();
                            }
                        }

                        dialog.dismiss();
                    }
                }
            });
            dialog.setTitle(context.getString(R.string.Date_and_Time));
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TaskDateUpdate set.setOnClickListener", "Exception " + e.getMessage(), "WARN", null);
        }
    }*/
}

