package com.ase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.Bean.ProjectDetailsBean;
import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;
import com.ase.RandomNumber.Utility;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.app.MyBuddy;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import Services.ShowOrCancelProgress;
import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.JsonRequestSender;
import json.WebServiceInterface;

/**
 * Created by thirumal on 04-02-2017.
 */
public class AllTaskList extends Activity implements WebServiceInterface {
    static AllTaskList alltasklist;
    public TaskDetailsBean taskDetailsBean;
    ArrayList<TaskDetailsBean> taskDetailsBeen, taskDetailsBeen1, non_activebean, taskDetailsBeen2, taskDetailsBeanArraylist, filterbuddy;
    String userName, taskType;
    String currentLoginUserName, currentLoginUserMail;
    //    String reopen_qry;
    String qury = "alltask";
    Context context;
    ImageView submit_icon, fill;
    int rem;
    public String[] catestring1, catestring2, catestring3;
    LinearLayout spinner_layout;
    Spinner spin1, spin2, spin3;
    int spin1pos, spin2pos, spin3pos;
    TextView cancelbutton, donebutton, clearbutton;
    TextView finish_page, active_task;
    EditText Search_EditText;
    TextView Noresult;
    String catagory;
    //    Button Search_Button;
    private HistoryFilter filter;
    TaskArrayAdapter buddyArrayAdapter;
    String quotes = "\"", query;
    ProgressDialog dialog;
    String webTaskId;
    String group_id;
    private Handler handler = new Handler();
    boolean check = false,isscrool = false;
    private SwipeMenuListView listView;
    private ShowOrCancelProgress progressListener;
    VideoCallDataBase dataBase;
    AppSharedpreferences appSharedpreferences;
    private int taskList_count = 0;
    boolean Scroll = false;
    String User_First_Last_Name = "";

    public static AllTaskList getInstance() {
        return alltasklist;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void setActiveAdapter(int count) {
        try {
            Noresult.setVisibility(View.GONE);
            taskDetailsBeen1.clear();
            taskDetailsBeen.clear();
            taskDetailsBeanArraylist.clear();
            Log.i("TaskHistory", "project 3");
            boolean countAboveten = false;

            Log.i("lazyload", "row count--->" + count);
            if (count != 0 && count >= 10) {
                count = count - 10;
                taskList_count = count;
                countAboveten = true;
            } else if (count < 10) {
                count = count;
                taskList_count = 0;
            } else {
                count = 0;
                taskList_count = 0;
            }
//            Log.i("TaskHistory", "userName is == 0 " + userName);
//            Log.i("TaskHistory", "taskType is == 1 " + taskType);

//            String username = AppSharedpreferences.getInstance(context).getString("mEmail");
            if (count == 0 && !countAboveten) {
                query = "select * from taskHistoryInfo where taskStatus!='draft' and category!='chat' and ( loginuser='" + currentLoginUserMail + "') ";
            }
            else if (count < 10 && !countAboveten) {
                query = "select * from taskHistoryInfo where taskStatus!='draft' and category!='chat' and ( loginuser='" + currentLoginUserMail + "') order by dateTime ASC LIMIT " + count + " OFFSET 0";
            } else {
                query = "select * from taskHistoryInfo where taskStatus!='draft' and category!='chat' and ( loginuser='" + currentLoginUserMail + "') order by dateTime ASC LIMIT 10 OFFSET " + count + "";
            }
            Log.i("DBQuery", " currentLoginUserName is == 2 " + currentLoginUserName + " \n currentLoginUserMail " + currentLoginUserMail);
//            if (taskType != null && taskType.equalsIgnoreCase("Group")) {
//                query = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + currentLoginUserMail + "') and ( taskType='" + taskType + "')  group by taskId";
//            } else {
//                query = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + currentLoginUserMail + "') and ( taskType='" + taskType + "')  group by taskId";
//                query = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserName='black_test.com' ) or ( toUserName='black_test.com' )) and ( loginuser='black@test.com') and ( taskType='Individual') and projectId IS NULL group by taskId";
//            }
            Log.d("DBQuery", "select query is == 3 " + query);
            taskDetailsBeen1.addAll(dataBase.getTaskHistoryInfo(query));
            Log.d("DBQuery", "size of history 4 1 " + taskDetailsBeen1.size());
//            query = "select * from projectHistory where parentTaskId != taskId";
//            taskDetailsBeen1.addAll(dataBase.getProjectHistoryTasks(query));
          /*  for (TaskDetailsBean taskDetailsBean : taskDetailsBeen1) {
                reopen_qry = "select taskId from taskHistoryInfo where (parentTaskId ='" + taskDetailsBean.getTaskId() + "')";
                Log.d("DBQuery", "reopen_qry--> " + reopen_qry);
                Log.d("DBQuery", "reopen_qry boolean --> " + dataBase.isAgendaRecordExists(reopen_qry));
                if (dataBase.isAgendaRecordExists(reopen_qry)) {
                    taskDetailsBean.setTaskStatus("Reopen");
                    taskDetailsBean.setCompletedPercentage("100");
                }
            }*/
//            Log.d("DBQuery", "reopen_qry--> " + reopen_qry);
//            taskDetailsBeanArraylist=(ArrayList<TaskDetailsBean>) taskDetailsBeen1.clone();
            Log.i("DBQuery", "size of history 4 " + taskDetailsBeen1.size());
            Log.i("DBQuery", "size of history 4 " + taskDetailsBeanArraylist.size());
//            Log.i("DBQuery", "userName 5 " + userName);
            taskDetailsBeen.clear();
            taskDetailsBeanArraylist.clear();
            if (taskDetailsBeen1.size() > 0) {
                for (TaskDetailsBean contactBean : taskDetailsBeen1) {
                    Log.i("TaskHistory", "project 3");
                    int unReadMsgCount = dataBase.getTaskUnReadMsgCount(contactBean.getTaskId());

                    Log.d("TaskHistory", "unread count" + unReadMsgCount);
                    Log.d("task1", "unread count" + unReadMsgCount);
                    Log.d("task1", "for each  issues Id  == " + contactBean.getIssueId());
                    rem = dataBase.getRemainderUnReadMsgCount(contactBean.getTaskId());
                    contactBean.setRead_status(unReadMsgCount);
                    contactBean.setMsg_status(rem);

                    Log.i("TaskHistory", "popup status 3 8 " + contactBean.getTaskStatus());
                /*else{
                            Log.i("popup", "status" + contactBean.getTaskName());
                            Log.i("popup", "status 4" + contactBean.getTaskStatus());
                            contactBean.setTaskStatus("inprogress");
                        }*/
                  /*  String qry = "select taskDescription from taskDetailsInfo where (taskId ='" + contactBean.getTaskId() + "') and ((taskDescription ='Task accepted') or (taskDescription ='issue accepted')) and ((taskDescription !='Task Rejected') or (taskDescription ='issue Rejected'))group by taskId";
                    if(!dataBase.isAgendaRecordExists(qry)){
                        contactBean.setTaskStatus("Assigned");
                    }*/

                    /*if (contactBean.getTaskStatus().equalsIgnoreCase("completed")) {
                        reopen_qry = "select taskId from taskHistoryInfo where (parentTaskId ='" + contactBean.getTaskId() + "') and taskStatus != 'closed'";
                        Log.d("DBQuery", "reopen_qry--> " + reopen_qry);
                        Log.d("DBQuery", "reopen_qry boolean --> " + dataBase.isAgendaRecordExists(reopen_qry));
                        if (dataBase.isAgendaRecordExists(reopen_qry)) {
                            contactBean.setTaskStatus("Reopen");
                        }
                    }*/
                    //            Log.e("taskpercentage",contactBean.getCompletedPercentage());
                    Log.i("popup", "status after query " + contactBean.getTaskStatus());
                    taskDetailsBeen.add(contactBean);
                    taskDetailsBeanArraylist.add(contactBean);

                }


            }

            taskDetailsBeen = new ArrayList<>();
            taskDetailsBeen = (ArrayList<TaskDetailsBean>) taskDetailsBeen1.clone();
            taskDetailsBeanArraylist = new ArrayList<>();
            taskDetailsBeanArraylist = (ArrayList<TaskDetailsBean>) taskDetailsBeen1.clone();


            class StringDateComparator implements Comparator<TaskDetailsBean> {
                String date_lhs = null;
                String date_rhs = null;

//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                public int compare(TaskDetailsBean lhs, TaskDetailsBean rhs) {
                    try {
                        Log.i("TaskHistory", "DateFormat leftside 9 " + lhs.getDateTime());
                        Log.i("TaskHistory", "DateFormat rightside 10 " + rhs.getDateTime());
                        //                    date_lhs = dateFormat.parse(lhs.getDateTime());
                        //                    date_rhs = dateFormat.parse(rhs.getDateTime());
                        date_lhs = lhs.getDateTime();
                        date_rhs = rhs.getDateTime();
                        Log.i("TaskHistory", "DateFormat ListPosition 11 " + taskDetailsBeen.get(1).getTaskDescription());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return date_lhs.compareTo(date_rhs);
                }
            }

            Collections.sort(taskDetailsBeen, new StringDateComparator() );
            Collections.reverse(taskDetailsBeen);
            for (TaskDetailsBean taskDetailsBean : taskDetailsBeen) {
                if (getTaskObservers(taskDetailsBean.getTaskId())) {
                    taskDetailsBeen.remove(taskDetailsBean);
                }

            }
            buddyArrayAdapter = new TaskArrayAdapter(context, taskDetailsBeen);
            listView.setAdapter(buddyArrayAdapter);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    buddyArrayAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AllTaskList setActive Adapter ", "Exception " + e.getMessage(), "WARN", null);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (v instanceof EditText) {
                    EditText edit = ((EditText) v);
                    Rect outR = new Rect();
                    edit.getGlobalVisibleRect(outR);
                    Boolean isKeyboardOpen = !outR.contains((int) ev.getRawX(), (int) ev.getRawY());
                    System.out.print("Is Keyboard? " + isKeyboardOpen);
                    Log.i("TaskHistory", " cursor icon show 12 " + isKeyboardOpen);
                    if (isKeyboardOpen) {
                        System.out.print("Entro al IF");
                        edit.clearFocus();
                        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
                    }
                    edit.setCursorVisible(!isKeyboardOpen);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AllTaskList dispatchTOuchEvent ", "Exception " + e.getMessage(), "WARN", null);
        } finally {
            return super.dispatchTouchEvent(ev);
        }
    }

    @Override
    public void onBackPressed() {
        // It's expensive, if running turn it off.
        try {
            isscrool = false;
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(Search_EditText.getWindowToken(), 0);
            Search_EditText.setCursorVisible(false);
            Search_EditText.clearFocus();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AllTaskList onBackPressed ", "Exception " + e.getMessage(), "WARN", null);
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Appreference.context_table.put("alltasklist", this);
        context = this;
        alltasklist = this;
        isscrool = true;
        catagory = "task";
        setContentView(R.layout.task_history);
        progressListener = Appreference.main_Activity_context;
        listView = (SwipeMenuListView) findViewById(R.id.lv_taskHistory);
        finish_page = (TextView) findViewById(R.id.finish_page);
        submit_icon = (ImageView) findViewById(R.id.submit_icon);
        active_task = (TextView) findViewById(R.id.txtView01);
        fill = (ImageView) findViewById(R.id.fill);
        spinner_layout = (LinearLayout) findViewById(R.id.spinner_layout);
        spinner_layout.setVisibility(View.GONE);
        spin1 = (Spinner) findViewById(R.id.spinner1);
        spin2 = (Spinner) findViewById(R.id.spinner2);
        spin3 = (Spinner) findViewById(R.id.spinner3);
        cancelbutton = (TextView) findViewById(R.id.cancelbutton);
        donebutton = (TextView) findViewById(R.id.donebutton);
        clearbutton = (TextView) findViewById(R.id.clearbutton);
        Search_EditText = (EditText) findViewById(R.id.searchtext);
        Noresult = (TextView) findViewById(R.id.Noresult);
        Noresult.setVisibility(View.GONE);
        Search_EditText.setCursorVisible(false);
//        Search_Button=(Button)findViewById(R.id.search_button);
        dataBase = VideoCallDataBase.getDB(context);
        appSharedpreferences = AppSharedpreferences.getInstance(context);
        taskDetailsBeen = new ArrayList<>();
        taskDetailsBeen1 = new ArrayList<>();
        taskDetailsBeen2 = new ArrayList<>();
        taskDetailsBeanArraylist = new ArrayList<>();
        non_activebean = new ArrayList<>();
        if (getIntent().getStringExtra("AllTaskList") != null && getIntent().getStringExtra("AllTaskList").equalsIgnoreCase("") && getIntent().getStringExtra("AllTaskList").equalsIgnoreCase("true")) {
            Log.i("AllTaskList","userid");
        } else {
            userName = getIntent().getExtras().getString("userId");
            taskType = getIntent().getExtras().getString("taskType");
            group_id = userName;
        }
        submit_icon.setVisibility(View.GONE);
        User_First_Last_Name = "Task for "+Appreference.loginuserdetails.getFirstName() + "  " +Appreference.loginuserdetails.getLastName() + "(Me)";
        active_task.setText(User_First_Last_Name);
        currentLoginUserName = appSharedpreferences.getString("loginUserName");
        currentLoginUserMail = appSharedpreferences.getString("mEmail");
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(Search_EditText.getWindowToken(), 0);
        String row_query = "select * from taskHistoryInfo where taskStatus!='draft' and category!='chat' and ( loginuser='" + currentLoginUserMail + "') ";
        int count = dataBase.getTaskHistoryRowCount(row_query);
        setActiveAdapter(count);

        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    listView.setEnabled(true);
                    spinner_layout.setVisibility(View.GONE);
                    Search_EditText.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("AllTaskList cancelButten click ", "Exception " + e.getMessage(), "WARN", null);
                }
            }
        });

        clearbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spin1.setSelection(spin1.getCount() - 1);
                spin2.setSelection(spin2.getCount() - 1);
                spin3.setSelection(spin3.getCount() - 1);
                String row_query = "select * from taskHistoryInfo where taskStatus!='draft' and category!='chat' and ( loginuser='" + currentLoginUserMail + "') ";
                int count = dataBase.getTaskHistoryRowCount(row_query);
                setActiveAdapter(count);
                try {
                    buddyArrayAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("AllTaskList clearbutton click event ", "Exception " + e.getMessage(), "WARN", null);
                }
            }
        });

        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    listView.setEnabled(true);
                    listView.setClickable(true);
                    String spinner1 = spin1.getSelectedItem().toString();
                    spin1pos = spin1.getSelectedItemPosition();
                    String spinner2 = spin2.getSelectedItem().toString();
                    spin2pos = spin2.getSelectedItemPosition();
                    String spinner3 = spin3.getSelectedItem().toString();
                    spin3pos = spin3.getSelectedItemPosition();
                    Log.i("TaskHistory", spinner1);
                    Log.i("TaskHistory", spinner2);
                    Log.i("TaskHistory", spinner3);
                    String issue_qry = " ";
                    String spy;
                    if (!spinner3.equalsIgnoreCase("Me") && !spinner3.equalsIgnoreCase("All")&& spinner3.contains(" ")) {
                        String firstname = spinner3.split(" ")[0];
                        String lastname = spinner3.split(" ")[1];
                        spy = dataBase.getusername(firstname, lastname);
                    } else {
                        spy = dataBase.getusernameWithOutLast(spinner3);
                    }
                    if (!spinner1.equalsIgnoreCase("All") && !spinner2.equalsIgnoreCase("All") && !spinner3.equalsIgnoreCase("All")) {
                        if (spinner3.equalsIgnoreCase("Me")) {
                            issue_qry = "select * from taskHistoryInfo where ( taskStatus='" + spinner2 + "') and( category='" + spinner1 + "') and ( ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "')";
                            Log.i("TaskHistory", spinner1);
                            Log.i("TaskHistory", spinner2);
                        } else {
                            issue_qry = "select * from taskHistoryInfo where ( taskStatus='" + spinner2 + "') and( category='" + spinner1 + "') and ( ownerOfTask='" + spy + "')";
                            Log.i("TaskHistory", "issue_qry value" + issue_qry);
                        }
                    } else if (spinner1.equalsIgnoreCase("All")) {
                        if (spinner2.equalsIgnoreCase("All")) {
                            if (!spinner3.equalsIgnoreCase("All")) {
                                if (spinner3.equalsIgnoreCase("Me")) {
                                    issue_qry = "select * from taskHistoryInfo where ( ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "')";
                                    Log.i("TaskHistory", spinner1);
                                    Log.i("TaskHistory", spinner2);
                                    Log.i("TaskHistory", "issue_qry value" + issue_qry);
                                } else {
                                    issue_qry = "select * from taskHistoryInfo where ( ownerOfTask='" + spy + "')";
                                    Log.i("TaskHistory", "issue_qry value" + issue_qry);
                                }
                            }
                        } else {
                            if (spinner3.equalsIgnoreCase("All")) {
                                issue_qry = "select * from taskHistoryInfo where ( taskStatus='" + spinner2 + "')";
                            } else if (!spinner3.equalsIgnoreCase("All")) {
                                if (spinner3.equalsIgnoreCase("Me")) {
                                    issue_qry = "select * from taskHistoryInfo where ( taskStatus='" + spinner2 + "') and ( ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "')";
                                    Log.i("TaskHistory", spinner1);
                                    Log.i("TaskHistory", spinner2);
                                    Log.i("TaskHistory", "issue_qry value" + issue_qry);
                                } else {
                                    issue_qry = "select * from taskHistoryInfo where ( taskStatus='" + spinner2 + "') and ( ownerOfTask='" + spy + "')";
                                    Log.i("TaskHistory", "issue_qry value" + issue_qry);
                                }
                            } else {
                                issue_qry = "select * from taskHistoryInfo where ( taskStatus='" + spinner2 + "')";
                            }
                        }
                    } else {
                        Log.i("TaskHistory", "Inside category selected");
                        Log.i("TaskHistory", "Me name is" + Appreference.loginuserdetails.getUsername());
                        if (spinner2.equalsIgnoreCase("All")) {
                            Log.i("TaskHistory", "Inside category selected status is unselected");
                            if (spinner3.equalsIgnoreCase("All")) {
                                issue_qry = "select * from taskHistoryInfo where ( category='" + spinner1 + "')";
                            } else {
                                if (spinner3.equalsIgnoreCase("Me")) {
                                    issue_qry = "select * from taskHistoryInfo where ( category='" + spinner1 + "') and ( ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "')";
                                    Log.i("TaskHistory", spinner1);
                                    Log.i("TaskHistory", spinner2);
                                    Log.i("TaskHistory", "Me name is" + Appreference.loginuserdetails.getUsername());
                                    Log.i("TaskHistory", "issue_qry value" + issue_qry);
                                } else {
                                    issue_qry = "select * from taskHistoryInfo where  ( category='" + spinner1 + "') and ( ownerOfTask='" + spy + "')";
                                    Log.i("TaskHistory", "issue_qry value" + issue_qry);
                                }
                            }
                        } else {
                            Log.i("TaskHistory", "category selected and status is selected");
                            if (spinner3.equalsIgnoreCase("All")) {
                                issue_qry = "select * from taskHistoryInfo where ( taskStatus='" + spinner2 + "')and ( category='" + spinner1 + "')";
                            }
                        }
                    }
                    if(spinner1.equalsIgnoreCase("Me") && spinner3.equalsIgnoreCase("Me")){
                        issue_qry = "select * from taskHistoryInfo where category='Task' and ownerofTask='" + Appreference.loginuserdetails.getUsername() + "' and taskReceiver='" + Appreference.loginuserdetails.getUsername() + "' and loginuser='" + Appreference.loginuserdetails.getEmail() + "' group by taskId";
                    }
                    taskDetailsBeanArraylist.clear();
                    taskDetailsBeen.clear();
                    Log.i("TaskHistoryFill", "issue_qry value" + issue_qry);
                    try {
                        taskDetailsBeen1 = dataBase.getTaskHistoryInfo(issue_qry);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("TaskHistoryFill ", "size of issue_task " + String.valueOf(taskDetailsBeen1.size()));
                    Log.i("TaskHistoryFill ", "size of issue_task ** " + taskDetailsBeanArraylist.size());
                    taskDetailsBeen.addAll(taskDetailsBeen1);
                    taskDetailsBeanArraylist.addAll(taskDetailsBeen1);
                    Log.i("TaskHistoryFill ", "size of issue_task ** " + taskDetailsBeanArraylist.size());

                    if (spinner1.equalsIgnoreCase("All") && spinner2.equalsIgnoreCase("All") && spinner3.equalsIgnoreCase("All")) {
                        String row_query = "select * from taskHistoryInfo where taskStatus!='draft' and category!='chat' and ( loginuser='" + currentLoginUserMail + "') ";
                        int count = dataBase.getTaskHistoryRowCount(row_query);
                        setActiveAdapter(count);
                        try {
                            buddyArrayAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (listView.getCount() == 0) {
                        Noresult.setVisibility(View.VISIBLE);
                        Noresult.setText("No TaskHistories Available");
                    } else {
                        buddyArrayAdapter.notifyDataSetChanged();
                    }
                    listView.setAdapter(buddyArrayAdapter);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (listView.getCount() == 0) {
                                Noresult.setVisibility(View.VISIBLE);
                                Noresult.setText("No TaskHistories Available");
                            } else {
                                Noresult.setVisibility(View.GONE);
//                                taskDetailsBeanArraylist.addAll(taskDetailsBeen1);
                                buddyArrayAdapter.notifyDataSetChanged();
                                listView.setAdapter(buddyArrayAdapter);
                            }
                        }
                    });
                    spinner_layout.setVisibility(View.GONE);
                    Search_EditText.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("AllTaskList done buttonClick Event ", "Exception " + e.getMessage(), "WARN", null);
                }
            }
        });


        fill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    check = true;
                    isscrool = false;
                    if (spinner_layout.getVisibility() == View.GONE) {
                        spinner_layout.setVisibility(View.VISIBLE);
                        Search_EditText.setVisibility(View.GONE);
                        listView.setEnabled(false);
                        listView.setClickable(false);
                    } else {
                        spinner_layout.setVisibility(View.GONE);
                        Search_EditText.setVisibility(View.VISIBLE);
                        listView.setEnabled(true);
                        listView.setClickable(true);
                    }
                    List<String> categories = new ArrayList<String>();
                    categories.add("Task");
                    categories.add("issue");
                    categories.add("Me");
//                    categories.add("Template");
                    categories.add("All");
                    catestring1 = categories.toArray(new String[categories.size()]);
                    spin1.setAdapter(new MyAdapter1(getApplicationContext(), R.layout.customspin, catestring1));
                    spin1.setPopupBackgroundResource(R.drawable.borderfordialog);
                    List<String> status = new ArrayList<String>();
                    status.add("inprogress");
                    status.add("assigned");
                    status.add("closed");
                    status.add("completed");
                    status.add("abandoned");
                    status.add("pause");
                    status.add("overdue");
                    status.add("rejected");
                    status.add("All");
                    catestring2 = status.toArray(new String[status.size()]);
                    spin2.setAdapter(new MyAdapter2(getApplicationContext(), R.layout.customspin, catestring2));
                    spin2.setPopupBackgroundResource(R.drawable.borderfordialog);
                    List<String> categories3 = new ArrayList<String>();
                    String givername = " ";
                    Log.i("TaskHistory", "loginuser detail" + Appreference.loginuserdetails.getEmail());
                    Log.i("TaskHistory", "givername " + givername);
                    ArrayList<ContactBean> buddyList = dataBase.getAllContact(Appreference.loginuserdetails.getUsername());
                    Log.i("TaskHistory", "buddylist value is " + buddyList.size());
                    for (int i = 0; i < buddyList.size(); i++) {
                        if (buddyList.get(i).getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            categories3.add("Me");
                        } else {
                            String username = buddyList.get(i).getUsername();
                            categories3.add(dataBase.getname(username));
                        }
                    }
                    categories3.add("All");
                    catestring3 = categories3.toArray(new String[categories3.size()]);
                    spin3.setAdapter(new MyAdapter3(getApplicationContext(), R.layout.customspin, catestring3));
                    spin3.setPopupBackgroundResource(R.drawable.borderfordialog);
                    if (spin1pos == spin1.getCount() - 1 && spin2pos == spin2.getCount() - 1 && spin3pos == spin3.getCount() - 1) {
                        spin1.setSelection(spin1.getCount() - 1);
                        spin2.setSelection(spin2.getCount() - 1);
                        spin3.setSelection(spin3.getCount() - 1);
                    } else {
                        if (spin1pos != spin1.getCount() - 1) {
                            spin1.setSelection(spin1pos);
                        } else {
                            spin1.setSelection(spin1.getCount() - 1);
                        }
                        if (spin2pos != spin2.getCount() - 1) {
                            spin2.setSelection(spin2pos);
                        } else {
                            spin2.setSelection(spin2.getCount() - 1);
                        }
                        if (spin3pos != spin3.getCount() - 1) {
                            spin3.setSelection(spin3pos);
                        } else {
                            spin3.setSelection(spin3.getCount() - 1);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("AllTaskList Fill Button Click Event ", "Exception " + e.getMessage(), "WARN", null);
                }
            }
        });
        Search_EditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    Log.d("constraint", "JNDSEJBJW  * " + s);
//                buddyArrayAdapter.getFilter().filter(s);
                    isscrool = false;
                    AllTaskList.this.buddyArrayAdapter.getFilter().filter(s);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("AllTaskList Search_EditText.addTextChanged ", "Exception " + e.getMessage(), "WARN", null);
                }
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        Search_EditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                try {
                    Search_EditText.setCursorVisible(false);
                    isscrool = false;
                    if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(Search_EditText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("AllTaskList Search_EditText.setOnEditAction ", "Exception " + e.getMessage(), "WARN", null);
                }
                return false;
            }
        });


        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                try {
                    taskDetailsBean = taskDetailsBeen.get(position);
                    Log.i("AllTaskList", "issue id 1---->" + taskDetailsBean.getTaskId());
                    Log.i("AllTaskList", "issue id 1---->" + taskDetailsBean.getTaskType());
                    Log.i("AllTaskList", "issue id 1---->" + taskDetailsBean.getToUserId());
                    Log.i("AllTaskList", "issue id 1---->" + taskDetailsBean.getTaskStatus());
                    Log.i("AllTaskList", "issue id 1---->" + taskDetailsBean.getTaskReceiver());
                    Log.i("AllTaskList", "issue id 1---->" + taskDetailsBean.getGroupTaskMembers());

                    if (taskDetailsBean.getOwnerOfTask() != null) {
                        if (taskDetailsBean.getOwnerOfTask().equals(Appreference.loginuserdetails.getUsername())) {
                            menu.getMenuItem(index).getTitle();
                            Log.i("task", "case " + menu.getMenuItem(index).getTitle());
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String dateTime = dateFormat.format(new Date());
                            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                            String dateforrow = dateFormat.format(new Date());
                            String tasktime = dateTime;
                            tasktime = tasktime.split(" ")[1];
                            Log.i("task", "tasktime" + tasktime);
                            Log.i("UTC", "sendMessage utc time" + dateforrow);
                            taskDetailsBean.setDateTime(dateTime);
                            taskDetailsBean.setTaskUTCDateTime(dateforrow);
                            taskDetailsBean.setTaskUTCTime(dateforrow);
                            taskDetailsBean.setTasktime(tasktime);
                            taskDetailsBean.setMimeType("text");
                            taskDetailsBean.setSignalid(Utility.getSessionID());
                            if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("Activate")) {
                                Log.i("task", "case------>0" + menu.getMenuItem(index).getTitle());
                                String TaskId = taskDetailsBean.getTaskId();
                                Active_Task(TaskId,taskDetailsBean.getCompletedPercentage());
//                                taskDetailsBeen.remove(position);
//                                taskDetailsBeanArraylist.remove(position);
//                                buddyArrayAdapter.notifyDataSetChanged();

                                /*taskDetailsBean.setTaskStatus("inprogress");
//                                taskDetailsBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
//                                taskDetailsBean.setFromUserName(Appreference.loginuserdetails.getUsername());

                                Log.i("task", "case------>1");
                                String query3 = "update taskDetailsInfo  set taskStatus = 'inprogress' where ('" + taskDetailsBean.getTaskId() + "'= taskId ) and loginuser='" + Appreference.loginuserdetails.getEmail() + "';";
                                String taskHistoryquery = "update taskHistoryInfo  set taskStatus = 'inprogress' where ('" + taskDetailsBean.getTaskId() + "'= taskId ) and loginuser='" + Appreference.loginuserdetails.getEmail() + "';";
                                Log.i("task", "case------>2");
                                dataBase.getTaskHistory(query3);
                                dataBase.getTaskHistoryInfo(taskHistoryquery);
                                Log.i("task", "case------>3");
                                JSONObject jsonObject = new JSONObject();
                                try {

                                    JSONObject jsonObject1 = new JSONObject();
                                    jsonObject1.put("id", Integer.parseInt(taskDetailsBean.getTaskId()));

                                    jsonObject.put("task", jsonObject1);

                                    JSONObject jsonObject2 = new JSONObject();
                                    jsonObject2.put("id", Appreference.loginuserdetails.getId());

                                    jsonObject.put("from", jsonObject2);

                                    JSONObject jsonObject3 = new JSONObject();
//                                    jsonObject3.put("id", Integer.parseInt(taskDetailsBean.getToUserId()));
                                    if (taskDetailsBean.getProjectId() !=null && !taskDetailsBean.getProjectId().equalsIgnoreCase("")) {

                                    } else {
                                        if (taskDetailsBean.getTaskType()!= null && !taskDetailsBean.getTaskType().equalsIgnoreCase("") && !taskDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
                                            jsonObject3.put("id", Integer.parseInt(taskDetailsBean.getToUserId()));
                                            jsonObject.put("to", jsonObject3);
                                        } else {
                                            jsonObject3.put("id", Integer.parseInt(taskDetailsBean.getToUserId()));
                                            jsonObject.put("group", jsonObject3);
                                        }
                                    }

//                                    jsonObject.put("to", jsonObject3);
                                    jsonObject.put("signalId", taskDetailsBean.getSignalid());
                                    jsonObject.put("parentId", taskDetailsBean.getParentId());
                                    jsonObject.put("createdDate", taskDetailsBean.getDateTime());
                                    jsonObject.put("percentageCompleted", taskDetailsBean.getCompletedPercentage());
                                    jsonObject.put("requestType", "percentageCompleted");
                                    jsonObject.put("taskStatus", "inprogress");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.i("TaskHistory", "task status " + taskDetailsBean.getTaskStatus());
                                Log.i("jsonrequest", jsonObject.toString());
                                Appreference.jsonRequestSender.taskConversationEntry(EnumJsonWebservicename.taskConversationEntry, jsonObject, AllTaskList.this, null, taskDetailsBean);
                                Log.i("TaskHistory ", "webservice called.abandoned");
                                taskDetailsBeen.remove(position);
                                taskDetailsBeanArraylist.remove(position);
                                buddyArrayAdapter.notifyDataSetChanged();*/

                            } else if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("Resume")) {
                                Log.i("task", "case------=>*0 " + menu.getMenuItem(index).getTitle());
                                String TaskId = taskDetailsBean.getTaskId();
                                Resume_Task(TaskId, taskDetailsBean.getCompletedPercentage());
//                                taskDetailsBeen.remove(position);
//                                taskDetailsBeanArraylist.remove(position);
//                                buddyArrayAdapter.notifyDataSetChanged();
                            } else if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("Abandon")) {
                                String TaskId = taskDetailsBean.getTaskId();
                                Abanded_Task(TaskId,taskDetailsBean.getCompletedPercentage());
//                                taskDetailsBeen.remove(position);
//                                taskDetailsBeanArraylist.remove(position);
//                                buddyArrayAdapter.notifyDataSetChanged();
                                /*taskDetailsBean.setTaskStatus("abandoned");
//                                taskDetailsBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
//                                taskDetailsBean.setFromUserName(Appreference.loginuserdetails.getUsername());
                                JSONObject jsonObject = new JSONObject();
                                Log.i("task", "taskid " + taskDetailsBean.getTaskId());
                                try {

                                    JSONObject jsonObject1 = new JSONObject();
                                    jsonObject1.put("id", Integer.parseInt(taskDetailsBean.getTaskId()));

                                    jsonObject.put("task", jsonObject1);

                                    JSONObject jsonObject2 = new JSONObject();
                                    jsonObject2.put("id", Appreference.loginuserdetails.getId());

                                    jsonObject.put("from", jsonObject2);

                                    JSONObject jsonObject3 = new JSONObject();
//                                    jsonObject3.put("id", Integer.parseInt(taskDetailsBean.getToUserId()));
                                    if (taskDetailsBean.getProjectId() !=null && !taskDetailsBean.getProjectId().equalsIgnoreCase("")) {

                                    } else {
                                        if (taskDetailsBean.getTaskType()!= null && !taskDetailsBean.getTaskType().equalsIgnoreCase("") && !taskDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
                                            jsonObject3.put("id", Integer.parseInt(taskDetailsBean.getToUserId()));
                                            jsonObject.put("to", jsonObject3);
                                        } else {
                                            jsonObject3.put("id", Integer.parseInt(taskDetailsBean.getToUserId()));
                                            jsonObject.put("group", jsonObject3);
                                        }
                                    }
//                                    jsonObject.put("to", jsonObject3);
                                    jsonObject.put("signalId", taskDetailsBean.getSignalid());
                                    jsonObject.put("parentId", taskDetailsBean.getParentId());
                                    jsonObject.put("createdDate", taskDetailsBean.getDateTime());
                                    jsonObject.put("percentageCompleted", taskDetailsBean.getCompletedPercentage());
                                    jsonObject.put("requestType", "percentageCompleted");
                                    jsonObject.put("taskStatus", "abandoned");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                Log.i("beforewebcall", taskDetailsBean.getTaskStatus());
                                Log.i("jsonrequest", jsonObject.toString());
                                Appreference.jsonRequestSender.taskConversationEntry(EnumJsonWebservicename.taskConversationEntry, jsonObject, AllTaskList.this, null, taskDetailsBean);
                                Log.e("webservice ", "called.active ");

                                //                                dataBase.insertORupdate_Task_history(taskDetailsBean);
                                taskDetailsBeen.remove(position);
                                taskDetailsBeanArraylist.remove(position);
                                buddyArrayAdapter.notifyDataSetChanged();*/


                            }  else if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("Pause")) {
                                String TaskId = taskDetailsBean.getTaskId();
                                Pause_Task(TaskId, taskDetailsBean.getCompletedPercentage());
                                Log.e("webservice ", "called.active ");

                            } else if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("View Issues")) {
                                Log.d("swipe", "issue owner ***  ");
                                isscrool = false;
                                Log.d("swipe", "issue***---> owner  == " + taskDetailsBean.getTaskId());
                                catagory = "issue";
                                String issue_qry = "select * from taskHistoryInfo where  ( ( ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "' ) or ( taskReceiver='" + Appreference.loginuserdetails.getUsername() + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and (parentTaskId='" + taskDetailsBean.getTaskId() + "') group by taskId";
                                Log.d("swipe", "issue_qry owner  " + issue_qry);
                                //                            taskDetailsBeen.remove(position);
                                //                            taskDetailsBeanArraylist.remove(position);
                                taskDetailsBeen1.clear();
                                taskDetailsBeen1 = dataBase.getTaskHistoryInfo(issue_qry);
                                Log.d("size ", "size of issue_task " + String.valueOf(taskDetailsBeen1.size()));
                                Log.i("task ", "size of issue_task ** " + taskDetailsBeanArraylist.size());
                                if (taskDetailsBeen1.size() > 0) {
                                    taskDetailsBeanArraylist.clear();
                                    taskDetailsBeen.clear();
                                }
                                taskDetailsBeen.addAll(taskDetailsBeen1);
                                taskDetailsBeanArraylist.addAll(taskDetailsBeen1);
                                buddyArrayAdapter.notifyDataSetChanged();
                            } else if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("View task")) {
                                isscrool = false;
                                Log.d("swipe", "issue owner ***  ");
                                Log.d("swipe", "issue***---> owner  == " + taskDetailsBean.getTaskId());
                                catagory = "issue";
                                String issue_qry = "select * from taskHistoryInfo where ( ( ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "' ) or ( taskReceiver='" + Appreference.loginuserdetails.getUsername() + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and (taskId='" + taskDetailsBean.getIssueId() + "') group by taskId";
                                Log.d("swipe", "issue_qry owner  " + issue_qry);
                                //                            taskDetailsBeen.remove(position);
                                //                            taskDetailsBeanArraylist.remove(position);
                                taskDetailsBeen1.clear();
                                taskDetailsBeen1 = dataBase.getTaskHistoryInfo(issue_qry);
                                Log.d("size ", "size of issue_task " + String.valueOf(taskDetailsBeen1.size()));
                                Log.i("task ", "size of issue_task ** " + taskDetailsBeanArraylist.size());
                                if (taskDetailsBeen1.size() > 0) {
                                    taskDetailsBeanArraylist.clear();
                                    taskDetailsBeen.clear();
                                }
                                taskDetailsBeen.addAll(taskDetailsBeen1);
                                taskDetailsBeanArraylist.addAll(taskDetailsBeen1);
                                buddyArrayAdapter.notifyDataSetChanged();
                            } else {
                                Log.i("task", "cance1");
                                taskDetailsBeen.remove(position);
                                taskDetailsBeanArraylist.remove(position);
                                buddyArrayAdapter.notifyDataSetChanged();
                                String query1 = "delete from taskDetailsInfo where('" + taskDetailsBean.getTaskId() + "'= taskId ) and loginuser='" + Appreference.loginuserdetails.getEmail() + "';";
                                dataBase.getTaskHistory(query1);
                                String taskHistoryquery = "delete from taskHistoryInfo where('" + taskDetailsBean.getTaskId() + "'= taskId ) and loginuser='" + Appreference.loginuserdetails.getEmail() + "';";
                                dataBase.getTaskHistoryInfo(taskHistoryquery);
                            }
                        } else {
                            Log.i("task", "case receiver " + menu.getMenuItem(index).getTitle());
                            Log.d("swipe", "issue$ ");
                            if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("View Issues")) {
                                Log.d("swipe", "issue***---> " + taskDetailsBean.getTaskId());
                                catagory = "issue";
                                isscrool = false;
                                String issue_qry = "select * from taskHistoryInfo where  ( ( ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "' ) or ( taskReceiver='" + Appreference.loginuserdetails.getUsername() + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and (parentTaskId='" + taskDetailsBean.getTaskId() + "') group by taskId";
                                Log.d("swipe", "issue_qry  " + issue_qry);
                                //                            taskDetailsBeen.remove(position);
                                //                            taskDetailsBeanArraylist.remove(position);
                                taskDetailsBeen1.clear();
                                taskDetailsBeen1 = dataBase.getTaskHistoryInfo(issue_qry);
                                Log.d("size ", "size of issue_task@ " + String.valueOf(taskDetailsBeen1.size()));
                                Log.i("task ", "size of issue_task *** " + taskDetailsBeanArraylist.size());
                                if (taskDetailsBeen1.size() > 0) {
                                    taskDetailsBeanArraylist.clear();
                                    taskDetailsBeen.clear();
                                }

                                taskDetailsBeen.addAll(taskDetailsBeen1);
                                taskDetailsBeanArraylist.addAll(taskDetailsBeen1);
                                buddyArrayAdapter.notifyDataSetChanged();
                            } else if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("View task")) {
                                Log.d("swipe", "issue owner ***  ");
                                isscrool = false;
                                Log.d("swipe", "issue***---> owner  == " + taskDetailsBean.getTaskId());
                                catagory = "issue";
                                String issue_qry = "select * from taskHistoryInfo where  ( ( ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "' ) or ( taskReceiver='" + Appreference.loginuserdetails.getUsername() + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and (taskId='" + taskDetailsBean.getIssueId() + "') group by taskId";
                                Log.d("swipe", "issue_qry owner  " + issue_qry);
                                //                            taskDetailsBeen.remove(position);
                                //                            taskDetailsBeanArraylist.remove(position);
                                taskDetailsBeen1.clear();
                                taskDetailsBeen1 = dataBase.getTaskHistoryInfo(issue_qry);
                                Log.d("size ", "size of issue_task " + String.valueOf(taskDetailsBeen1.size()));
                                Log.i("task ", "size of issue_task ** " + taskDetailsBeanArraylist.size());
                                if (taskDetailsBeen1.size() > 0) {
                                    taskDetailsBeanArraylist.clear();
                                    taskDetailsBeen.clear();
                                }
                                taskDetailsBeen.addAll(taskDetailsBeen1);
                                taskDetailsBeanArraylist.addAll(taskDetailsBeen1);
                                buddyArrayAdapter.notifyDataSetChanged();
                            } else {
                                taskDetailsBeen.remove(position);
                                taskDetailsBeanArraylist.remove(position);
                                buddyArrayAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Appreference.printLog("AllTaskList listView MenuitemClick event ", "Exception " + e.getMessage(), "WARN", null);
                }
                return false;
            }
        });

        final SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
/*if(taskDetailsBean!=null) {
    if (taskDetailsBean.getFromUserEmail().equalsIgnoreCase(Appreference.loginuserdetails.getEmail())) {*/
                try {
                    switch (menu.getViewType()) {

                        case 1:
                            // create "open" item
                            SwipeMenuItem openItem = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            openItem.setBackground(new ColorDrawable(Color.rgb(0xff, 0x91, 0x11)));
                            // set item width
                            openItem.setWidth(dp2px(90));
                            // set item title
                            openItem.setTitle("Abandon");
                            // set item title fontsize
                            openItem.setTitleSize(18);
                            // set item title font color
                            openItem.setTitleColor(Color.WHITE);
                            // add to menu
                            menu.addMenuItem(openItem);

                            SwipeMenuItem pause = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            pause.setBackground(new ColorDrawable(Color.rgb(124, 252, 0)));
                            // set item width
                            pause.setWidth(dp2px(90));
                            // set item title
                            pause.setTitle("Pause");
                            // set item title fontsize
                            pause.setTitleSize(18);
                            // set item title font color
                            pause.setTitleColor(Color.WHITE);
                            // add to menu
                            menu.addMenuItem(pause);
       /* }
    }*/

                           /* // create "delete" item
                            SwipeMenuItem deleteItem = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xFf,
                                    0x00, 0x00)));
                            // set item width
                            deleteItem.setWidth(dp2px(90));
                            // set a icon
                            deleteItem.setTitle("Delete");
                            deleteItem.setTitleSize(18);
                            deleteItem.setTitleColor(Color.WHITE);
    //            deleteItem.setIcon(R.drawable.ic_delete_32);
                            // add to menu
                            menu.addMenuItem(deleteItem);*/

                            SwipeMenuItem issue = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            issue.setBackground(new ColorDrawable(Color.rgb(0x00, 0x00, 0xFf)));
                            // set item width
                            issue.setWidth(dp2px(90));
                            // set a icon
                            issue.setTitle("View Issues");
                            issue.setTitleSize(18);
                            issue.setTitleColor(Color.WHITE);
                            //            deleteItem.setIcon(R.drawable.ic_delete_32);
                            // add to menu
                            menu.addMenuItem(issue);
                            break;
                        case 0:

                           /* SwipeMenuItem deleteItem1 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            deleteItem1.setBackground(new ColorDrawable(Color.rgb(0xFf, 0x00, 0x00)));
                            // set item width
                            deleteItem1.setWidth(dp2px(90));
                            // set a icon
                            deleteItem1.setTitle("Delete");
                            deleteItem1.setTitleSize(18);
                            deleteItem1.setTitleColor(Color.WHITE);
    //            deleteItem.setIcon(R.drawable.ic_delete_32);
                            // add to menu
                            menu.addMenuItem(deleteItem1);*/
                            break;
                        case 2:
                            SwipeMenuItem openItem2 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            openItem2.setBackground(new ColorDrawable(Color.rgb(0x00, 0xff, 0x11)));
                            // set item width
                            openItem2.setWidth(dp2px(90));
                            // set item title
                            openItem2.setTitle("Assign");
                            // set item title fontsize
                            openItem2.setTitleSize(18);
                            // set item title font color
                            openItem2.setTitleColor(Color.WHITE);
                            // add to menu
                            menu.addMenuItem(openItem2);
       /* }
    }*/

                            /*// create "delete" item
                            SwipeMenuItem deleteItem2 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            deleteItem2.setBackground(new ColorDrawable(Color.rgb(0xFf, 0x00, 0x00)));
                            // set item width
                            deleteItem2.setWidth(dp2px(90));
                            // set a icon
                            deleteItem2.setTitle("Delete");
                            deleteItem2.setTitleSize(18);
                            deleteItem2.setTitleColor(Color.WHITE);
    //            deleteItem.setIcon(R.drawable.ic_delete_32);
                            // add to menu
                            menu.addMenuItem(deleteItem2);*/

                            SwipeMenuItem issue1 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            issue1.setBackground(new ColorDrawable(Color.rgb(0x00, 0x00, 0xFf)));
                            // set item width
                            issue1.setWidth(dp2px(90));
                            // set a icon
                            issue1.setTitle("View Issues");
                            issue1.setTitleSize(18);
                            issue1.setTitleColor(Color.WHITE);
                            //            deleteItem.setIcon(R.drawable.ic_delete_32);
                            // add to menu
                            menu.addMenuItem(issue1);
                            break;

                        case 3:
                            // create "open" item
                            SwipeMenuItem openItem1 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            openItem1.setBackground(new ColorDrawable(Color.rgb(0x00, 0xff, 0x11)));
                            // set item width
                            openItem1.setWidth(dp2px(90));
                            // set item title
                            openItem1.setTitle("Activate");
                            // set item title fontsize
                            openItem1.setTitleSize(18);
                            // set item title font color
                            openItem1.setTitleColor(Color.WHITE);
                            // add to menu
                            menu.addMenuItem(openItem1);

                            SwipeMenuItem issue2 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            issue2.setBackground(new ColorDrawable(Color.rgb(0x00, 0x00, 0xFf)));
                            // set item width
                            issue2.setWidth(dp2px(90));
                            // set a icon
                            issue2.setTitle("View Issues");
                            issue2.setTitleSize(18);
                            issue2.setTitleColor(Color.WHITE);
                            menu.addMenuItem(issue2);

                            /*// create "delete" item
                            SwipeMenuItem deleteItems = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            deleteItems.setBackground(new ColorDrawable(Color.rgb(0xFf, 0x00, 0x00)));
                            // set item width
                            deleteItems.setWidth(dp2px(90));
                            // set a icon
                            deleteItems.setTitle("Delete");
                            deleteItems.setTitleSize(18);
                            deleteItems.setTitleColor(Color.WHITE);
    //            deleteItem.setIcon(R.drawable.ic_delete_32);
                            // add to menu
                            menu.addMenuItem(deleteItems);*/


                            break;

                        case 4:

                            SwipeMenuItem openItem4 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            openItem4.setBackground(new ColorDrawable(Color.rgb(0xff, 0x91, 0x11)));
                            // set item width
                            openItem4.setWidth(dp2px(90));
                            // set item title
                            openItem4.setTitle("Abandon");
                            // set item title fontsize
                            openItem4.setTitleSize(18);
                            // set item title font color
                            openItem4.setTitleColor(Color.WHITE);
                            // add to menu
                            menu.addMenuItem(openItem4);

                            SwipeMenuItem pause1 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            pause1.setBackground(new ColorDrawable(Color.rgb(124, 252, 0)));
                            // set item width
                            pause1.setWidth(dp2px(90));
                            // set item title
                            pause1.setTitle("Pause");
                            // set item title fontsize
                            pause1.setTitleSize(18);
                            // set item title font color
                            pause1.setTitleColor(Color.WHITE);
                            // add to menu
                            menu.addMenuItem(pause1);
       /* }
    }*/

                            /*// create "delete" item
                            SwipeMenuItem deleteItem4 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            deleteItem4.setBackground(new ColorDrawable(Color.rgb(0xFf, 0x00, 0x00)));
                            // set item width
                            deleteItem4.setWidth(dp2px(90));
                            // set a icon
                            deleteItem4.setTitle("Delete");
                            deleteItem4.setTitleSize(18);
                            deleteItem4.setTitleColor(Color.WHITE);
    //            deleteItem.setIcon(R.drawable.ic_delete_32);
                            // add to menu
                            menu.addMenuItem(deleteItem4);*/

                            SwipeMenuItem issue4 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            issue4.setBackground(new ColorDrawable(Color.rgb(0x00, 0x00, 0xFf)));
                            // set item width
                            issue4.setWidth(dp2px(90));
                            // set a icon
                            issue4.setTitle("View task");
                            issue4.setTitleSize(18);
                            issue4.setTitleColor(Color.WHITE);
                            //            deleteItem.setIcon(R.drawable.ic_delete_32);
                            // add to menu
                            menu.addMenuItem(issue4);
                            break;

                        case 5:
                            SwipeMenuItem openItem5 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            openItem5.setBackground(new ColorDrawable(Color.rgb(0x00, 0xff, 0x11)));
                            // set item width
                            openItem5.setWidth(dp2px(90));
                            // set item title
                            openItem5.setTitle("Activate");
                            // set item title fontsize
                            openItem5.setTitleSize(18);
                            // set item title font color
                            openItem5.setTitleColor(Color.WHITE);
                            // add to menu
                            menu.addMenuItem(openItem5);
       /* }
    }*/

                            /*// create "delete" item
                            SwipeMenuItem deleteItem5 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            deleteItem5.setBackground(new ColorDrawable(Color.rgb(0xFf, 0x00, 0x00)));
                            // set item width
                            deleteItem5.setWidth(dp2px(90));
                            // set a icon
                            deleteItem5.setTitle("Delete");
                            deleteItem5.setTitleSize(18);
                            deleteItem5.setTitleColor(Color.WHITE);
    //            deleteItem.setIcon(R.drawable.ic_delete_32);
                            // add to menu
                            menu.addMenuItem(deleteItem5);*/

                            SwipeMenuItem issue5 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            issue5.setBackground(new ColorDrawable(Color.rgb(0x00, 0x00, 0xFf)));
                            // set item width
                            issue5.setWidth(dp2px(90));
                            // set a icon
                            issue5.setTitle("View task");
                            issue5.setTitleSize(18);
                            issue5.setTitleColor(Color.WHITE);
                            //            deleteItem.setIcon(R.drawable.ic_delete_32);
                            // add to menu
                            menu.addMenuItem(issue5);
                            break;
                        case 6:
                            SwipeMenuItem i = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            i.setBackground(new ColorDrawable(Color.rgb(0x00, 0x00, 0xFf)));
                            // set item width
                            i.setWidth(dp2px(90));
                            // set a icon
                            i.setTitle("View task");
                            i.setTitleSize(18);
                            i.setTitleColor(Color.WHITE);
                            //            deleteItem.setIcon(R.drawable.ic_delete_32);
                            // add to menu
                            menu.addMenuItem(i);
                            break;
                        case 7:
                            SwipeMenuItem i1 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            i1.setBackground(new ColorDrawable(Color.rgb(0x00, 0x00, 0xFf)));
                            // set item width
                            i1.setWidth(dp2px(90));
                            // set a icon
                            i1.setTitle("View Issues");
                            i1.setTitleSize(18);
                            i1.setTitleColor(Color.WHITE);
                            //            deleteItem.setIcon(R.drawable.ic_delete_32);
                            // add to menu
                            menu.addMenuItem(i1);
                            break;

                        case 8:
                            SwipeMenuItem openItem6 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            openItem6.setBackground(new ColorDrawable(Color.rgb(75, 0, 130)));
                            // set item width
                            openItem6.setWidth(dp2px(90));
                            // set item title
                            openItem6.setTitle("Resume");
                            // set item title fontsize
                            openItem6.setTitleSize(18);
                            // set item title font color
                            openItem6.setTitleColor(Color.WHITE);
                            // add to menu
                            menu.addMenuItem(openItem6);


                            SwipeMenuItem issue7 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            issue7.setBackground(new ColorDrawable(Color.rgb(0x00, 0x00, 0xFf)));
                            // set item width
                            issue7.setWidth(dp2px(90));
                            // set a icon
                            issue7.setTitle("View task");
                            issue7.setTitleSize(18);
                            issue7.setTitleColor(Color.WHITE);
                            menu.addMenuItem(issue7);
                            break;
                        case 9:
                            // create "open" item
                            SwipeMenuItem openItem8 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            openItem8.setBackground(new ColorDrawable(Color.rgb(75, 0, 130)));
                            // set item width
                            openItem8.setWidth(dp2px(90));
                            // set item title
                            openItem8.setTitle("Resume");
                            // set item title fontsize
                            openItem8.setTitleSize(18);
                            // set item title font color
                            openItem8.setTitleColor(Color.WHITE);
                            // add to menu
                            menu.addMenuItem(openItem8);

                            SwipeMenuItem issue3 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            issue3.setBackground(new ColorDrawable(Color.rgb(0x00, 0x00, 0xFf)));
                            // set item width
                            issue3.setWidth(dp2px(90));
                            // set a icon
                            issue3.setTitle("View Issues");
                            issue3.setTitleSize(18);
                            issue3.setTitleColor(Color.WHITE);
                            menu.addMenuItem(issue3);
                            break;


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("AllTaskList SwipeMenu Creater ", "Exception " + e.getMessage(), "WARN", null);
                }
            }
        };
        listView.setMenuCreator(creator);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.i("lazyload", "scroll count-->" + taskList_count);
                if (isscrool) {
                    if (taskList_count != 0 && taskList_count >= 10) {
                        Scroll = true;
                    } else Scroll = taskList_count != 0 && taskList_count < 10;
                    int lastIndexInScreen = visibleItemCount + firstVisibleItem;
                    Log.i("lazyload", "lastIndexInScreen-->" + lastIndexInScreen);
                    Log.i("lazyload", "Scroll-->" + Scroll);
                    Log.i("lazyload", "totalItemCount-->" + totalItemCount);
                    Log.i("lazyload", "taskDetailsBeen size-->" + taskDetailsBeen.size());
                    if (lastIndexInScreen >= totalItemCount && Scroll) {
                        Log.i("lazyload", "if condition lastIndexInScreen && Screoll");
                        getDataFromDBWhenScroll_the_List(taskList_count);
                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    taskDetailsBean = taskDetailsBeen.get(position);
                    Log.i("AllTaskList", "task Status  ----> " + taskDetailsBean.getTaskStatus());
                    Log.i("AllTaskList", "task Type  ----> " + taskDetailsBean.getTaskType());
                    Log.i("AllTaskList", " TaskReceiver  ----> " + taskDetailsBean.getTaskReceiver());
                    Log.i("AllTaskList", " GroupTaskMembers " + taskDetailsBean.getGroupTaskMembers());
                    if (taskDetailsBean.getProjectId() != null && taskDetailsBean.getParentTaskId() != null) {
                        Log.i("AllTaskList", "if Project  ----> ");
                        if (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("Template") || (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("draft"))) {
                            Log.i("AllTaskList", "if Template  ----> ");
                            if (!taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                showDialog();
                                webTaskId = taskDetailsBean.getTaskId();
                                gettaskwebservice(webTaskId);
                                check = true;

                            }
                        } else if (taskDetailsBean.getTaskType().equalsIgnoreCase("individual")) {
                            Log.i("AllTaskList", "if individual  ----> ");
                            if (taskDetailsBean.getTaskReceiver().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) || taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) || taskDetailsBean.getTaskObservers().contains(Appreference.loginuserdetails.getUsername())) {
                                Log.i("task", String.valueOf(position));
                                ProjectDetailsBean projectDetailsBean = dataBase.settaskDetailsBeantoProjectDetailsBean(taskDetailsBean);
                                Intent intent = new Intent(context, NewTaskConversation.class);
                                intent.putExtra("task", "projectHistory");
                                intent.putExtra("projectHistoryBean", projectDetailsBean);
                                check = true;
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "You are not in individual project subtask", Toast.LENGTH_SHORT).show();
                            }
                        } else if (taskDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
                            Log.i("AllTaskList", "if Group  ----> ");
                            Log.i("ListMembers", "projectDetailsBean.getTaskMemberList() " + taskDetailsBean.getListMemberProject());
                            if ((taskDetailsBean.getGroupTaskMembers() != null && taskDetailsBean.getGroupTaskMembers().contains(Appreference.loginuserdetails.getUsername())) || taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) || taskDetailsBean.getTaskObservers().contains(Appreference.loginuserdetails.getUsername())) {
                                Log.i("task", String.valueOf(position));
                                ProjectDetailsBean projectDetailsBean = dataBase.settaskDetailsBeantoProjectDetailsBean(taskDetailsBean);
                                Intent intent = new Intent(context, NewTaskConversation.class);
                                intent.putExtra("task", "projectHistory");
                                intent.putExtra("projectHistoryBean", projectDetailsBean);
                                check = true;
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "You are not in group project task", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Log.i("AllTaskList", "else Project  ----> ");
                        Log.d("Task1", "task stststts " + taskDetailsBean.getTaskStatus());
                        if (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("Template") || (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("draft"))) {
                            if (!taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                showDialog();
                                webTaskId = taskDetailsBean.getTaskId();
                                gettaskwebservice(webTaskId);
                                check = true;

                            }
                        } else {
                            progressListener.ShowProgress(context);
                            Log.i("task", String.valueOf(position));
                            Intent intent = new Intent(context, NewTaskConversation.class);
                            intent.putExtra("task", "taskhistory");
                            intent.putExtra("taskHistoryBean", taskDetailsBean);
                            intent.putExtra("catagory", taskDetailsBean.getCatagory());
                            Log.i("Task1", "groupname" + group_id);
                            intent.putExtra("groupname", group_id);
                            check = true;
                            startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("AllTaskList ListView.setonItemClickListener  ", "Exception " + e.getMessage(), "WARN", null);
                }
            }

        });


        finish_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Appreference.is_reload = true;
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("AllTaskList finish_page click event ", "Exception " + e.getMessage(), "WARN", null);
                }
            }
        });

        submit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(AllTaskList.this, Perfomanceview.class);
                    intent.putExtra("is_project", "N");
                    intent.putExtra("isAllTask","Y");
                    intent.putExtra("User_Project_Id", String.valueOf(Appreference.loginuserdetails.getId()));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("AllTaskList submit_icon click event ", "Exception " + e.getMessage(), "WARN", null);
                }
            }
        });

    }

    public void showDialog() {
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    dialog = new ProgressDialog(AllTaskList.this);
                    dialog.setMessage("Loading Task...");
                    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    dialog.setProgress(0);
                    dialog.setMax(100);
                    dialog.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AllTaskList showDialog ", "Exception " + e.getMessage(), "WARN", null);
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
            Appreference.printLog("AllTaskList cancelDialog ", "Exception " + e.getMessage(), "WARN", null);
        }

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
            Appreference.printLog("AllTaskList showToast ", "Exception " + e.getMessage(), "WARN", null);
        }
    }

//    public void cancelDialog() {
//        if (dialog.isShowing())
//            dialog.dismiss();
//    }

    public void gettaskwebservice(String webtaskId) {
        try {
            List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(1);
            nameValuePairs1.add(new BasicNameValuePair("taskId", webtaskId));

            Appreference.jsonRequestSender.getTask(EnumJsonWebservicename.getTask, nameValuePairs1, AllTaskList.this);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AllTaskList gettaskwebservice", "Exception " + e.getMessage(), "WARN", null);
        }

    }

    public void refresh() {
        try {
            String query1;
//            if (taskDetailsBeen != null)
            taskDetailsBeen.clear();
            taskDetailsBeanArraylist.clear();
            final int index = listView.getFirstVisiblePosition();
            View v = listView.getChildAt(0);
            final int top = (v == null) ? 0 : v.getTop();
//        String query1 = "select * from taskDetailsInfo where  ( ( taskStatus <> 'abandoned' ) or ( taskStatus is null )) and ( ( fromUserName='" + userName + "' ) or ( toUserName='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + taskType + "')  group by taskId";
//        if (taskType != null && taskType.equalsIgnoreCase("Group")) {
            if (currentLoginUserMail == null) {
                currentLoginUserMail = Appreference.loginuserdetails.getEmail();
            }
            query1 = "select * from taskHistoryInfo where taskStatus!='draft'  and (category <> 'chat') and ( loginuser='" + currentLoginUserMail + "')";
//        } else {
//        query2 = "select * from projectHistory where parentTaskId != taskId";
//            query1 = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserId='" + userName + "' ) or ( toUserId='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + "Individual" + "')  and (projectId IS NULL or projectId = 'null' or projectId = '(null)') and taskId ='" + taskid + "' group by taskId";
//            ArrayList<TaskDetailsBean> taskDetailsBeenlist = dataBase.getTaskHistory(query);
//        }
            Log.i("AllTaskList", "query1 " + query1);
            if (dataBase.getTaskHistoryInfo(query1) != null) {
//                taskDetailsBeen1.addAll(dataBase.getTaskHistoryInfo(query1));
                //            taskDetailsBeen1.addAll(dataBase.getProjectHistoryTasks(query2));
                //            taskDetailsBeanArraylist=(ArrayList<TaskDetailsBean>)taskDetailsBeen1.clone();
                Log.e("size", String.valueOf(taskDetailsBeen1.size()));
                taskDetailsBeen1 = dataBase.getTaskHistoryInfo(query1);
                Log.i("AllTaskList", "taskDetailsBeen1 " + taskDetailsBeen1.size());
                //        taskDetailsBeen.addAll(taskDetailsBeen1);
                for (TaskDetailsBean contactBean : taskDetailsBeen1) {
                   /* String qry = "select taskDescription from taskDetailsInfo where (taskId ='" + contactBean.getTaskId() + "') and ((taskDescription ='Task Accepted') or (taskDescription ='issue Accepted')) and ((taskDescription !='Task Rejected') or (taskDescription ='issue Rejected')) group by taskId";
                    if(dataBase.isAgendaRecordExists(qry)){
                        contactBean.setTaskStatus("Assigned");
                    }*/
                    Log.i("task", "project 2");
                    int unReadMsgCount = 0;
                    try {
                        unReadMsgCount = dataBase.getTaskUnReadMsgCount(contactBean.getTaskId());
                        rem = dataBase.getRemainderUnReadMsgCount(contactBean.getTaskId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("TaskHistory", "unread count 0 " + unReadMsgCount);
                    int percentage = 0;
                    if (contactBean.getTaskType() != null && contactBean.getTaskType().equalsIgnoreCase("Group")) {
                        Log.d("TaskHistory", "Group percent 1 " + contactBean.getTaskType());
                        Log.d("TaskHistory", "unread count 3 " + unReadMsgCount);
                        try {
                            percentage = dataBase.GroupPercentageChecker(contactBean.getToUserName(), contactBean.getTaskId(), contactBean.getOwnerOfTask());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            percentage = dataBase.percentagechecker(contactBean.getTaskId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    contactBean.setCompletedPercentage(String.valueOf(percentage));
                    contactBean.setRead_status(unReadMsgCount);
                    contactBean.setMsg_status(rem);
                    taskDetailsBeen.add(contactBean);
                    taskDetailsBeanArraylist.add(contactBean);
                }
                Log.d("TaskHistory", "task list size = 2 " + taskDetailsBeen.size());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        class StringDateComparator implements Comparator<TaskDetailsBean> {
                            String date_lhs = null;
                            String date_rhs = null;
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            public int compare(TaskDetailsBean lhs, TaskDetailsBean rhs) {
                                try {
                                    Log.i("DateFormat", "leftside" + lhs.getDateTime());
                                    Log.i("DateFormat", "rightside" + rhs.getDateTime());
                                    date_lhs = lhs.getDateTime();
                                    date_rhs = rhs.getDateTime();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return date_lhs.compareTo(date_rhs);
                            }
                        }
                        Collections.sort(taskDetailsBeen, new StringDateComparator());
                        Collections.reverse(taskDetailsBeen);
                        buddyArrayAdapter = new TaskArrayAdapter(context, taskDetailsBeen);
                        listView.setAdapter(buddyArrayAdapter);
                        buddyArrayAdapter.notifyDataSetChanged();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            listView.setSelectionFromTop(index, top);
                        }
                    }
                });
                buddyArrayAdapter = new TaskArrayAdapter(context, taskDetailsBeen);
                try {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(buddyArrayAdapter);
                            buddyArrayAdapter.notifyDataSetChanged();
                        }
                    });
//                    setActiveAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                buddyArrayAdapter = new TaskArrayAdapter(context, taskDetailsBeen);
//                listView.setAdapter(buddyArrayAdapter);
//                buddyArrayAdapter.notifyDataSetChanged();

//                buddyArrayAdapter = new TaskArrayAdapter(context, taskDetailsBeen);
//                listView.setAdapter(buddyArrayAdapter);
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        buddyArrayAdapter.notifyDataSetChanged();
//                    }
//                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AllTaskList refresh method ", "Exception " + e.getMessage(), "WARN", null);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Appreference.context_table.remove("taskhistory");
    }

    @Override
    protected void onResume() {
        super.onResume();


//        if (check) {
        Log.i("DBQuery", "check " + check);
        Log.i("DBQuery", "check1111 " + check);
//            setActiveAdapter();
//        }
        try {
            if (check) {
                Noresult.setVisibility(View.GONE);
                String query1;
                qury = active_task.getText().toString();
                //        active_task.setText(qury);
                qury = active_task.getText().toString();
                if (qury.equalsIgnoreCase(User_First_Last_Name)) {
                    qury = "Task&Issue";
                } else {
                    qury = active_task.getText().toString();
                }
                //        Log.i("task", "type" + taskType);

                //                if (taskType != null && taskType.equalsIgnoreCase("Individual")) {
                //                    query1 = "select * from taskDetailsInfo where (taskStatus  = '" + qury + "' ) and  (fromUserName='" + userName + "' or toUserName='" + userName + "') and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' )  group by taskNo";
                if (qury.equalsIgnoreCase("inprogress")) {
                    Log.i("task", "filtered was " + qury);
                    fill.setBackgroundResource(R.drawable.filterenabled);
                    query1 = "select * from taskHistoryInfo where taskStatus ='" + qury + "' and category!='chat' group by taskId";
//                    query2 = "select * from projectHistory where taskStatus ='" + qury + "' and parentTaskId != taskId";
                } else if (qury.equalsIgnoreCase("issue")) {
//                    active_task.setText("Issues for task");
                    catagory = "issue";
                    query1 = "select * from taskHistoryInfo where  category = '" + catagory + "' and category!='chat'  group by taskId";
                    //                    query2 = "select * from projectHistory where category = '" + catagory + "' and parentTaskId != taskId";
                    Log.i("task", "filtered was 1*** " + qury);
                } else if (qury.equalsIgnoreCase("Task&Issue")) {
                    query1 = "select * from taskHistoryInfo group by taskId";
//                    query2 = "select * from projectHistory where parentTaskId != taskId";
                    Log.i("task", "filtered was 2** " + qury);
                } else if (!qury.equalsIgnoreCase("alltask")) {
                    Log.i("task", "filtered was !qury.equalsIgnoreCase('alltask') " + qury);
//                    query2 = "select * from projectHistory where taskStatus ='" + qury + "' and parentTaskId != taskId";
                    query1 = "select * from taskHistoryInfo where taskStatus ='" + qury + "' and category!='chat' group by taskId";
                       /* else {
                            catagory = "issue";
                            query1 = "select * from taskDetailsInfo where ( ( fromUserId='" + userName + "' ) or ( toUserId='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + taskType + "')  and (projectId IS NULL or projectId = 'null' or projectId = '(null)') group by taskId";

                        }*/
                } else {
                    Log.i("task", "filtered was " + qury);
                    fill.setBackgroundResource(R.drawable.filterenabled);
                    catagory = "Task";
                    fill.setBackgroundResource(R.drawable.filterdisabled);
//                    query2 = "select * from projectHistory where parentTaskId != taskId";
                    query1 = "select * from taskHistoryInfo where category = '" + catagory + "' and category!='chat'  group by taskId";
                }
                //                } else if (taskType != null && taskType.equalsIgnoreCase("Group")) {

                //                    if (qury.equalsIgnoreCase("inprogress")) {
                //                        Log.i("task", "filtered was " + qury);
                //                        fill.setBackgroundResource(R.drawable.filterenabled);
                //                        query1 = "select * from taskHistoryInfo where (taskStatus ='" + qury + "') and  ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' ) and ( taskType='" + taskType + "')  group by taskId";
                //                    } else if (qury.equalsIgnoreCase("issue")) {
                //                        active_task.setText("Issues for task");
                //                        catagory = "issue";
                //                        query1 = "select * from taskHistoryInfo where ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' )  and category = '" + catagory + "' group by taskNo";
                //                        Log.i("task", "filtered was 1*** " + qury);
                //                    } else if (qury.equalsIgnoreCase("Task&Issue")) {
                //                        catagory = "issue";
                //                        query1 = "select * from taskHistoryInfo where ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' )  group by taskNo";
                //                        Log.i("task", "filtered was 2** " + qury);
                //                    } else if (!qury.equalsIgnoreCase("alltask"))
                //                        query1 = "select * from taskHistoryInfo where ( taskStatus = '" + qury + "' ) and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + taskType + "')  group by taskId";
                //                    else {
                //                        catagory = "issue";
                //                        query1 = "select * from taskHistoryInfo where ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + taskType + "')  and (projectId IS NULL or projectId = 'null' or projectId = '(null)') group by taskId";
                //
                //                    }
                //                }

                taskDetailsBeen.clear();
                taskDetailsBeen1.clear();
                taskDetailsBeen2.clear();
                taskDetailsBeanArraylist.clear();
                //                String query1 = "select * from taskDetailsInfo where (taskStatus <> '" + qury + "' or taskStatus is null ) and  (fromUserName='" + userName + "' or toUserName='" + userName + "') and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' )  group by taskNo";
                //                String query1 = "select * from taskDetailsInfo where (taskStatus <> '"+qury+"' or taskStatus is null ) and  (fromUserName='" + userName + "' or toUserName='" + userName + "') and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' )  group by taskNo";
                Log.d("task", "query   " + query1);

                taskDetailsBeen1.addAll(dataBase.getTaskHistoryInfo(query1));
//                if (qury != null && !qury.equalsIgnoreCase("issue")) {
//                    taskDetailsBeen1.addAll(dataBase.getProjectHistoryTasks(query2));
//                }
                //                taskDetailsBeanArraylist=(ArrayList<TaskDetailsBean>)taskDetailsBeen1.clone();
                Log.e("size", String.valueOf(taskDetailsBeen1.size()));
                Log.i("task", "size was " + taskDetailsBeanArraylist.size());
                //        taskDetailsBeen.addAll(taskDetailsBeen1);
                    /*if (!qury.equalsIgnoreCase("alltask")) {
                        if (taskDetailsBeen1.size() > 0) {
                            for (TaskDetailsBean taskDetailsBean_1 : taskDetailsBeen1) {
                                String lastTask_Status = dataBase.getLastStatus(taskDetailsBean_1.getTaskId());
                                if (lastTask_Status.equalsIgnoreCase(qury)) {
                                    Log.d("Last Status", "value " + lastTask_Status);
                                    taskDetailsBeen2.add(taskDetailsBean_1);
                                }
                            }
                        }
                    } else */
                {
                    taskDetailsBeen2.addAll(taskDetailsBeen1);
                }
                Log.d("Last Status", "value " + taskDetailsBeen2.size());

                for (TaskDetailsBean contactBean : taskDetailsBeen2) {
                    Log.i("task", "project 1");
                    int unReadMsgCount = dataBase.getTaskUnReadMsgCount(contactBean.getTaskId());

                    rem = dataBase.getRemainderUnReadMsgCount(contactBean.getTaskNo());

                    Log.d("task1", "unread count" + unReadMsgCount);

                    int percentage = 0;
                    Log.d("task1", "unread count" + unReadMsgCount + contactBean.getToUserName() + contactBean.getTaskId() + contactBean.getOwnerOfTask() + " " + contactBean.getTaskType());
                        /*if (contactBean.getTaskType() != null && contactBean.getTaskType().equalsIgnoreCase("Group")) {
                            percentage = dataBase.GroupPercentageChecker(contactBean.getToUserName(), contactBean.getTaskId(), contactBean.getOwnerOfTask());
                        } else {
                            percentage = dataBase.percentagechecker(contactBean.getTaskId());
                        }*/
                    if (contactBean.getTaskType() != null && contactBean.getTaskType().equalsIgnoreCase("Group")) {
                        Log.d("TaskHistory", "Group percent 11 " + contactBean.getTaskType());
                        if (contactBean.getOwnerOfTask() != null && contactBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            Log.d("TaskHistory", "unread count 21 " + unReadMsgCount);
                            //                            Log.d("TaskHistory", "contactBean.getFromUserName() " + contactBean.getFromUserName());
                            //                            Log.d("TaskHistory", "contactBean.getOwnerOfTask() " + contactBean.getOwnerOfTask());
                            if (contactBean.getFromUserName() != null && contactBean.getFromUserName().equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                Log.d("TaskHistory", "unread count 31 " + unReadMsgCount);
                                percentage = Integer.parseInt(dataBase.getlastCompletedParcentage(contactBean.getTaskId()));
                            } else {
                                Log.d("TaskHistory", "unread count 41 " + unReadMsgCount);
                                percentage = dataBase.GroupPercentageChecker(contactBean.getToUserName(), contactBean.getTaskId(), contactBean.getOwnerOfTask());
                            }
                        } else {
                            Log.d("TaskHistory", "getFromUserName count 41 " + contactBean.getFromUserName());
                            Log.d("TaskHistory", "getOwnerOfTask count 41 " + contactBean.getOwnerOfTask());
                            if (contactBean.getFromUserName() != null && contactBean.getFromUserName().equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                Log.d("TaskHistory", "unread count 51 " + unReadMsgCount);
                                percentage = Integer.parseInt(contactBean.getCompletedPercentage());
                            } else {
                                Log.d("TaskHistory", "unread count 61 " + unReadMsgCount);
                                String TakerPercent_value = null;
                                try {
                                    TakerPercent_value = dataBase.getTakerlastCompletedParcentage(contactBean.getTaskId());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (TakerPercent_value != null)
                                    percentage = Integer.parseInt(TakerPercent_value);
                            }
                        }

                    } else {
                        try {
                            percentage = dataBase.percentagechecker(contactBean.getTaskId());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    contactBean.setCompletedPercentage(String.valueOf(percentage));

                    contactBean.setRead_status(unReadMsgCount);

                    contactBean.setMsg_status(rem);

                    taskDetailsBeen.add(contactBean);
                    taskDetailsBeanArraylist.add(contactBean);

                }

                Log.d("task", "task list size =  3 " + taskDetailsBeen.size());

                //                class StringDateComparator implements Comparator<TaskDetailsBean> {
                //                    String date_lhs = null;
                //                    String date_rhs = null;
                //
                //                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //
                //                    public int compare(TaskDetailsBean lhs, TaskDetailsBean rhs) {
                //                        try {
                //                            Log.i("DateFormat", "leftside" + lhs.getDateTime());
                //                            Log.i("DateFormat", "rightside" + rhs.getDateTime());
                ////                    date_lhs = dateFormat.parse(lhs.getDateTime());
                ////                    date_rhs = dateFormat.parse(rhs.getDateTime());
                //                            date_lhs = lhs.getDateTime();
                //                            date_rhs = rhs.getDateTime();
                //                            Log.i("DateFormet", "ListPosition" + taskDetailsBeen.get(1).getTaskDescription());
                //                        } catch (Exception e) {
                //                            e.printStackTrace();
                //                        }
                //                        return date_lhs.compareTo(date_rhs);
                //                    }
                //                }
                //
                //                Collections.sort(taskDetailsBeen, new StringDateComparator());
                //                Collections.reverse(taskDetailsBeen);
                  /*  for (TaskDetailsBean taskDetailsBean:taskDetailsBeen) {
                        if(getTaskObservers(taskDetailsBean.getTaskId())){
                            taskDetailsBeen.remove(taskDetailsBean);
                        }

                    }*/
                buddyArrayAdapter = new TaskArrayAdapter(context, taskDetailsBeen);
                listView.setAdapter(buddyArrayAdapter);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        buddyArrayAdapter.notifyDataSetChanged();
                    }
                });
                check = false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Appreference.printLog("AllTaskList onResume ", "Exception " + e.getMessage(), "WARN", null);
        }
    }

    public void sendMultiInstantMessage(String msgBody, String[] userlist) {

        try {
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
                            Appreference.printLog("AllTaskList ", "Exception sentMultiinstantmessage" + e.getMessage(), "WARN", null);
                        }
                        break;
                    }
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            Appreference.printLog("AllTaskList ", "Exception sentMultiinstantmessage " + e.getMessage(), "WARN", null);
        }
    }

    /*
       */
    @Override
    public void ResponceMethod(final Object object) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.i("currentstatus", "Task Details Respons method");
                CommunicationBean communicationBean = (CommunicationBean) object;
                JSONObject jsonObject;
                try {
                    jsonObject = new JSONObject(communicationBean.getEmail());
                    Log.i("jsonwebservice", communicationBean.getEmail());
                    String str1 = communicationBean.getFirstname();
                    if (jsonObject.has("result_code") && (int) jsonObject.get("result_code") == 0 && (str1 != null && !str1.equalsIgnoreCase("getTask")&& !str1.equalsIgnoreCase("reactivateStatus"))) {
                        TaskDetailsBean task = communicationBean.getTaskDetailsBean();
//                        Log.e("current status","Task Status  == "+ task.getTaskStatus());
                        if (task != null && task.getTaskStatus().equalsIgnoreCase("abandoned")) {
                            Log.i("Status ", ">>0 " + task.getTaskStatus());
                            Log.e("Status Updated", "set abandoned");
                            if (task.getCatagory() != null && task.getCatagory().equalsIgnoreCase("issue")) {
                                task.setTaskDescription("This issue is abandoned");
                            } else {
                                task.setTaskDescription("This task is abandoned");
                            }
                            task.setTaskStatus("abandoned");
                            if (task.getFromUserName() != null && task.getToUserName() != null && (task.getFromUserName().equalsIgnoreCase(task.getToUserName()))) {
                                task.setMsg_status(1);
                            }
                            Log.i("Status ", ">>1");

//                            newTaskConversation.taskReminderMessage(task, null, "text", null, null);
                            if (task.getProjectId() != null && !task.getProjectId().equalsIgnoreCase("") && !task.getProjectId().equalsIgnoreCase("null")) {
                                dataBase.insert_new_Project_history(task);
                            } else {
                                dataBase.insertORupdate_TaskHistoryInfo(task);
                            }
                            dataBase.insertORupdate_Task_history(task);
//                            String xml = composeChatXML(task);
                            String xml = composeChatXML(task);
                            Log.i("AllTaskList ", ">>2");
                            String TaskMembers, taskObservers;
                            if (task.getTaskType().equalsIgnoreCase("Group")) {
                                TaskMembers = dataBase.getProjectParentTaskId("select taskMemberList from taskHistoryInfo where taskId='" + task.getTaskId() + "'");
                            } else {
                                TaskMembers = dataBase.getProjectParentTaskId("select taskReceiver from taskHistoryInfo where taskId='" + task.getTaskId() + "'");
                            }
                            taskObservers = dataBase.getProjectParentTaskId("select taskObservers from taskHistoryInfo where taskId='" + task.getTaskId() + "'");
                            if (taskObservers != null && !taskObservers.equalsIgnoreCase("")) {
                                TaskMembers = TaskMembers+","+taskObservers;
                            }
//                            String Query = "Select * from taskDetailsInfo where mimeType='observer' and taskId ='" + task.getTaskId() + "'";
//                            ArrayList<TaskDetailsBean> taskDetailsBean5 = dataBase.getTaskHistory(Query);
                            String[] observerList;
//                            sendInstantMessage(xml);
//                            if (taskDetailsBean5.size() > 0) {
//                                TaskDetailsBean taskDetailsBean1 = taskDetailsBean5.get(taskDetailsBean5.size() - 1);
                            observerList = TaskMembers.split(",");
                            sendMultiInstantMessage(xml, observerList,1);
//                            }

                            String query = "update taskDetailsInfo  set taskStatus = 'abandoned' where ('" + taskDetailsBean.getTaskId() + "'= taskId ) ;";
                            String query_1 = "update taskHistoryInfo  set taskStatus = 'abandoned' where ('" + taskDetailsBean.getTaskId() + "'= taskId ) ;";
                            dataBase.updateaccept(query_1);
                            dataBase.updateaccept(query);
                            Log.i("AllTaskList ", ">>4 " + query);
                            Log.i("AllTaskList ", ">>4 " + query_1);
//                            dataBase.getTaskHistoryInfo(query);
//                            buddyArrayAdapter.notifyDataSetChanged();
                            String row_query = "select * from taskHistoryInfo where taskStatus!='draft' and category!='chat' and ( loginuser='" + currentLoginUserMail + "') ";
                            int count = dataBase.getTaskHistoryRowCount(row_query);
                            setActiveAdapter(count);
                           /* taskDetailsBeen1.clear();
                            taskDetailsBeen1 = dataBase.getTaskHistoryInfo("select * from taskHistoryInfo where (( ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "' ) or ( taskReceiver='" + Appreference.loginuserdetails.getUsername() + "' ) or ( taskObservers='" + Appreference.loginuserdetails.getUsername() + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "')");
                            Log.d("size ", "size of issue_task==> " + String.valueOf(taskDetailsBeen1.size()));
                            Log.i("task ", "size of issue_task **==> " + taskDetailsBeanArraylist.size());
                            if (taskDetailsBeen1.size() > 0) {
                                taskDetailsBeanArraylist.clear();
                                taskDetailsBeen.clear();
                            }
                            Log.d("size ", "size of issue_task " + String.valueOf(taskDetailsBeen1.size()));

                            taskDetailsBeen.addAll(taskDetailsBeen1);
                            taskDetailsBeanArraylist.addAll(taskDetailsBeen1);
                            Log.i("task ", "size of issue_task ** " + taskDetailsBeanArraylist.size());
                            buddyArrayAdapter.notifyDataSetChanged();*/
                            Log.i("AllTaskList ", ">>5 ");
                        } else if (task != null && task.getTaskStatus().equalsIgnoreCase("pause")) {
                            Log.i("AllTaskList ", ">>0 " + task.getTaskStatus());
                            Log.e("AllTaskList", "set abandoned");
                            if (task.getCatagory() != null && task.getCatagory().equalsIgnoreCase("issue")) {
                                task.setTaskDescription("This issue is paused");
                            } else {
                                task.setTaskDescription("This task is paused");
                            }
                            task.setTaskStatus("pause");
                            if (task.getFromUserName() != null && task.getToUserName() != null && (task.getFromUserName().equalsIgnoreCase(task.getToUserName()))) {
                                task.setMsg_status(1);
                            }
                            Log.i("AllTaskList ", ">>1");

//                            newTaskConversation.taskReminderMessage(task, null, "text", null, null);
                            if (task.getProjectId() != null && !task.getProjectId().equalsIgnoreCase("") && !task.getProjectId().equalsIgnoreCase("null")) {
                                dataBase.insert_new_Project_history(task);
                            } else {
                                dataBase.insertORupdate_TaskHistoryInfo(task);
                            }
                            dataBase.insertORupdate_Task_history(task);
//                            String xml = composeChatXML(task);
                            String xml = composeChatXML(task);
                            Log.i("AllTaskList ", ">>2");
                            String TaskMembers, taskObservers;
                            if (task.getTaskType().equalsIgnoreCase("Group")) {
                                TaskMembers = dataBase.getProjectParentTaskId("select taskMemberList from taskHistoryInfo where taskId='" + task.getTaskId() + "'");
                            } else {
                                TaskMembers = dataBase.getProjectParentTaskId("select taskReceiver from taskHistoryInfo where taskId='" + task.getTaskId() + "'");
                            }
                            taskObservers = dataBase.getProjectParentTaskId("select taskObservers from taskHistoryInfo where taskId='" + task.getTaskId() + "'");
                            if (taskObservers != null && !taskObservers.equalsIgnoreCase("")) {
                                TaskMembers = TaskMembers+","+taskObservers;
                            }
//                            String Query = "Select * from taskDetailsInfo where mimeType='observer' and taskId ='" + task.getTaskId() + "'";
//                            ArrayList<TaskDetailsBean> taskDetailsBean5 = dataBase.getTaskHistory(Query);
                            String[] observerList;
//                            sendInstantMessage(xml);
//                            if (taskDetailsBean5.size() > 0) {
//                                TaskDetailsBean taskDetailsBean1 = taskDetailsBean5.get(taskDetailsBean5.size() - 1);
                            observerList = TaskMembers.split(",");
                            sendMultiInstantMessage(xml, observerList,1);
//                            }

                            Log.i("AllTaskList ", ">>4");
                            String query = "update taskDetailsInfo  set taskStatus = 'pause' where ('" + taskDetailsBean.getTaskId() + "'= taskId ) ;";
                            String query_1 = "update taskHistoryInfo  set taskStatus = 'pause' where ('" + taskDetailsBean.getTaskId() + "'= taskId ) ;";
                            dataBase.updateaccept(query_1);
                            dataBase.updateaccept(query);
                            Log.i("AllTaskList ", ">>4 " + query);
                            Log.i("AllTaskList ", ">>4 " + query_1);
//                            dataBase.getTaskHistoryInfo(query);
//                            buddyArrayAdapter.notifyDataSetChanged();
                            String row_query = "select * from taskHistoryInfo where taskStatus!='draft' and category!='chat' and ( loginuser='" + currentLoginUserMail + "') ";
                            int count = dataBase.getTaskHistoryRowCount(row_query);
                            setActiveAdapter(count);
                            /*taskDetailsBeen1.clear();
                            taskDetailsBeen1 = dataBase.getTaskHistoryInfo("select * from taskHistoryInfo where (( ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "' ) or ( taskReceiver='" + Appreference.loginuserdetails.getUsername() + "' ) or ( taskObservers='" + Appreference.loginuserdetails.getUsername() + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "')");
                            Log.d("size ", "size of issue_task==> " + String.valueOf(taskDetailsBeen1.size()));
                            Log.i("task ", "size of issue_task **==> " + taskDetailsBeanArraylist.size());
                            if (taskDetailsBeen1.size() > 0) {
                                taskDetailsBeanArraylist.clear();
                                taskDetailsBeen.clear();
                            }
                            Log.d("size ", "size of issue_task " + String.valueOf(taskDetailsBeen1.size()));

                            taskDetailsBeen.addAll(taskDetailsBeen1);
                            taskDetailsBeanArraylist.addAll(taskDetailsBeen1);
                            Log.i("task ", "size of issue_task ** " + taskDetailsBeanArraylist.size());
                            buddyArrayAdapter.notifyDataSetChanged();*/
                            Log.i("AllTaskList ", ">>5 ");
                        }
                        Log.i("Status Updated", "set " + communicationBean.getEmail());
                        Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
                    } else if (str1 != null && !str1.equalsIgnoreCase("getTask") && str1.equalsIgnoreCase("reactivateStatus")) {

                        Log.d("AllTaskList", "reactivateStatus  ==  " + str1);
                        Log.d("AllTasklist", "response value for reactivateStatus w/s   status is ==  " + jsonObject.getString("updatedStatus") + "\n"
                                + " and taskId is  = " + jsonObject.get("taskId") + " % value is  == " + jsonObject.get("percentageCompleted"));

                        TaskDetailsBean task = communicationBean.getTaskDetailsBean();
                        Log.e("AllTaskList", "set inprogress");
//                            taskDetailsBean.setTaskDescription("This task is activated");
//                        Log.i("Status ", "beforeTaskStatus " +jsonObject.getString("beforeTaskStatus"));
                        String taskId = String.valueOf(jsonObject.get("taskId"));
                        String task_status = dataBase.getProjectParentTaskId("select taskStatus from taskDetailsInfo where  taskId='" + taskId + "' order by id DESC LIMIT 1");
                        Log.d("AllTaskList", "taskId " + taskId + " task_status: " + task_status);
                        if (task_status != null && task_status.equalsIgnoreCase("pause")) {
                            if (task.getCatagory() != null && task.getCatagory().equalsIgnoreCase("issue")) {
                                task.setTaskDescription("This issue is resumed");
                            } else {
                                task.setTaskDescription("This task is resumed");
                            }
                        } else {
                            if (task.getCatagory() != null && task.getCatagory().equalsIgnoreCase("issue")) {
                                task.setTaskDescription("This issue is activated");
                            } else {
                                task.setTaskDescription("This task is activated");
                            }
                        }
                        String status = jsonObject.getString("updatedStatus");
                        task.setTaskStatus(status);
                        if (task.getFromUserName() != null && task.getToUserName() != null && (task.getFromUserName().equalsIgnoreCase(task.getToUserName()))) {
                            task.setMsg_status(1);
                        }
                        Log.i("AllTaskList ", "--->0");
//                            newTaskConversation.taskReminderMessage(task, null, "text", null, null);
//                            if (task.getProjectId() != null && !task.getProjectId().equalsIgnoreCase("") && !task.getProjectId().equalsIgnoreCase("null")) {
                        dataBase.insert_new_Project_history(task);
//                            } else {
//                                dataBase.insertORupdate_TaskHistoryInfo(task);
//                            }
                        dataBase.insertORupdate_Task_history(task);
//                            String xml = composeChatXML(task);
                        String xml = composeChatXML(task);
                        Log.i("AllTaskList ", ">>2");
                        String TaskMembers, taskObservers;
                        if (task.getTaskType().equalsIgnoreCase("Group")) {
                            TaskMembers = dataBase.getProjectParentTaskId("select taskMemberList from taskHistoryInfo where taskId='" + task.getTaskId() + "'");
                        } else {
                            TaskMembers = dataBase.getProjectParentTaskId("select taskReceiver from taskHistoryInfo where taskId='" + task.getTaskId() + "'");
                        }
                        taskObservers = dataBase.getProjectParentTaskId("select taskObservers from taskHistoryInfo where taskId='" + task.getTaskId() + "'");
                        if (taskObservers != null && !taskObservers.equalsIgnoreCase("")) {
                            TaskMembers = TaskMembers + "," + taskObservers;
                        }
//                            String Query = "Select * from taskDetailsInfo where mimeType='observer' and taskId ='" + task.getTaskId() + "'";
//                            ArrayList<TaskDetailsBean> taskDetailsBean5 = dataBase.getTaskHistory(Query);
                        String[] observerList;
//                            sendInstantMessage(xml);
//                            if (taskDetailsBean5.size() > 0) {
//                                TaskDetailsBean taskDetailsBean1 = taskDetailsBean5.get(taskDetailsBean5.size() - 1);
                        observerList = TaskMembers.split(",");
                        sendMultiInstantMessage(xml, observerList, 1);
//                            }

                        Log.i("AllTaskList ", "--->2");
//                            String query = "update taskDetailsInfo  set taskStatus = 'inprogress' where ('" + taskDetailsBean.getTaskId() + "'= taskId );";
                        String query_1 = "update taskHistoryInfo  set taskStatus = '" + status + "' where ('" + taskDetailsBean.getTaskId() + "'= taskId );";
//                        dataBase.updateaccept(query_1);
//                        String row_query = "update taskDetailsInfo  set taskStatus = '" + status + "' where ('" + taskDetailsBean.getTaskId() + "'= taskId );";
//                        dataBase.getTaskHistoryInfo(row_query);
//                        buddyArrayAdapter.notifyDataSetChanged();
//                        Log.i("AllTaskList ", ">>8 ");
                        dataBase.updateaccept(query_1);
//                        dataBase.updateaccept(row_query);
                        Log.i("AllTaskList ", ">>4 " + query);
                        Log.i("AllTaskList ", ">>4 " + query_1);
//                            dataBase.getTaskHistoryInfo(query);
//                            buddyArrayAdapter.notifyDataSetChanged();
                        String row_query = "select * from taskHistoryInfo where taskStatus!='draft' and category!='chat' and ( loginuser='" + currentLoginUserMail + "') ";
                        int count = dataBase.getTaskHistoryRowCount(row_query);
                        setActiveAdapter(count);
                       /* taskDetailsBeen1.clear();
                        taskDetailsBeen1 = dataBase.getTaskHistoryInfo("select * from taskHistoryInfo where (( ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "' ) or ( taskReceiver='" + Appreference.loginuserdetails.getUsername() + "' ) or ( taskObservers='" + Appreference.loginuserdetails.getUsername() + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "')");
                        Log.d("size ", "size of issue_task==> " + String.valueOf(taskDetailsBeen1.size()));
                        Log.i("task ", "size of issue_task **==> " + taskDetailsBeanArraylist.size());
                        if (taskDetailsBeen1.size() > 0) {
                            taskDetailsBeanArraylist.clear();
                            taskDetailsBeen.clear();
                        }
                        Log.d("size ", "size of issue_task " + String.valueOf(taskDetailsBeen1.size()));

                        taskDetailsBeen.addAll(taskDetailsBeen1);
                        taskDetailsBeanArraylist.addAll(taskDetailsBeen1);
                        Log.i("task ", "size of issue_task ** " + taskDetailsBeanArraylist.size());
                        buddyArrayAdapter.notifyDataSetChanged();*/
                        Log.i("AllTaskList ", ">>5 ");
                        Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.get("id").toString().equals(webTaskId) && (str1 != null && str1.equalsIgnoreCase("getTask") && !str1.equalsIgnoreCase("reactivateStatus"))) {
                        Gson gson = new Gson();
                        Log.i("AllTaskList", "getTask jelement.getAsJsonObject() != null");
                        ListAllgetTaskDetails listAllgetTaskDetails = gson.fromJson(jsonObject.toString(), ListAllgetTaskDetails.class);
                        Log.d("AllTaskList", "getTaskfirstname  " + listAllgetTaskDetails.getParentId());
                        Log.d("AllTaskList", "getTask firstname  " + listAllgetTaskDetails.getId());
                        Log.d("AllTaskList", "getTask firstname  " + listAllgetTaskDetails.getDescription());
                        Log.d("AllTaskList", "getTask firstname  " + listAllgetTaskDetails.getListObserver());
                        dataBase.deleteTemplateEntry(webTaskId);

                        dataBase.insertORupdate_ListAllgetTaskDetails(listAllgetTaskDetails);
                        if (getResources().getString(R.string.task_enable).equalsIgnoreCase("enable") && taskType != null && !taskType.equalsIgnoreCase("Group")) {
                            dataBase.taskStatusUpdate("assigned", webTaskId);
                            Log.d("AllTaskList", "Template accept");
                        } else {
                            dataBase.taskStatusUpdate("inprogress", webTaskId);
                            Log.d("AllTaskList", "Template inprogress");
                        }
                        load();
                        cancelDialog();
                    } else {
                        Toast.makeText(AllTaskList.this, "Not Updated network error", Toast.LENGTH_LONG).show();
                    }
//                    cancelDialog();

                } catch (JSONException e) {
                    Toast.makeText(AllTaskList.this, "Json Error response", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    Appreference.printLog("AllTaskList", "Exception Responce " + e.getMessage(), "WARN", null);
                }
            }
        });

    }

    @Override
    public void ErrorMethod(Object object) {

    }

    public void load() {
        try {
            Intent intent = new Intent(context, NewTaskConversation.class);
            intent.putExtra("task", "taskhistory");
            intent.putExtra("taskHistoryBean", taskDetailsBean);
            startActivity(intent);
            check = true;
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AllTaskList", "Exception Load " + e.getMessage(), "WARN", null);
        }
    }

    /*public String composeChatXML(TaskDetailsBean cmbean) {
        StringBuffer buffer = new StringBuffer();
        try {
            byte[] data = cmbean.getTaskDescription().trim().getBytes("UTF-8");
            Log.d("base64value", "base64 before " + cmbean.getTaskDescription());
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);

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
            }
            buffer.append(" taskDescription=" + quotes + base64.trim() + quotes);
            buffer.append(" fromUserId=" + quotes + cmbean.getFromUserId() + quotes);
            buffer.append(" fromUserName=" + quotes + cmbean.getFromUserName() + quotes);
            buffer.append(" toUserId=" + quotes + cmbean.getToUserId() + quotes);
            Log.i("task", cmbean.getToUserName());
            buffer.append(" toUserName=" + quotes + cmbean.getToUserName() + quotes);
            buffer.append(" taskNo=" + quotes + cmbean.getTaskNo() + quotes);
            buffer.append(" taskId=" + quotes + cmbean.getTaskId() + quotes);
            buffer.append(" taskType=" + quotes + cmbean.getTaskType() + quotes);
            buffer.append(" plannedStartDateTime=" + quotes + cmbean.getPlannedStartDateTime() + quotes);
            buffer.append(" plannedEndDateTime=" + quotes + cmbean.getPlannedEndDateTime() + quotes);
            buffer.append(" isRemainderRequired=" + quotes + cmbean.getIsRemainderRequired() + quotes);
            buffer.append(" remainderDateTime=" + quotes + cmbean.getRemainderFrequency() + quotes);
            buffer.append(" taskStatus=" + quotes + cmbean.getTaskStatus() + quotes);
            buffer.append(" signalid=" + quotes + cmbean.getSignalid() + quotes);
            buffer.append(" parentId=" + quotes + cmbean.getParentId() + quotes);
            buffer.append(" taskRequestType=" + quotes + cmbean.getTaskRequestType() + quotes);
            buffer.append(" dateFrequency=" + quotes + cmbean.getDateFrequency() + quotes);
            buffer.append(" timeFrequency=" + quotes + cmbean.getTimeFrequency() + quotes);
            buffer.append(" taskOwner=" + quotes + cmbean.getOwnerOfTask() + quotes);
            buffer.append(" taskReceiver=" + quotes + cmbean.getTaskReceiver() + quotes);
            buffer.append(" mimeType=" + quotes + cmbean.getMimeType() + quotes);
            buffer.append(" dateTime=" + quotes + cmbean.getTaskUTCDateTime() + quotes);
            buffer.append(" taskPriority=" + quotes + cmbean.getTaskPriority() + quotes);
            buffer.append(" completedPercentage=" + quotes + cmbean.getCompletedPercentage() + quotes);
            buffer.append(" remainderQuotes=" + quotes + cmbean.getReminderQuote() + quotes);
            buffer.append(" remark=" + quotes + cmbean.getRemark() + quotes);
            buffer.append(" />");
//            buffer.append("reminderTone="+quotes+cmbean.getReminderTone()+ quotes);
//            buffer.append(" parentId=" + quotes + cmbean.getMsgtype() + quotes);
//            if (cmbean.getType().equals("CallSingleChat") || cmbean.getType().equals("CallGroupChat")) {
//                buffer.append(" activeURI=" + quotes + cmbean.getConferenceuri() + quotes);
//            } else {
//                buffer.append(" activeURI=" + quotes + "" + quotes);
//            }
//            buffer.append(" chatmembers="+quotes+cmbean.getChatmembers()+quotes);

            *//*buffer.append("<message>" + "<![CDATA[" + cmbean.getMessage()
                    + "]]>");
            buffer.append("</message>");*//*
//            buffer.append("</>");
//            buffer.append("<SendingStatus signalid=" + quotes
//                    + cmbean.getSendingStatusid() + quotes + " />");
//            buffer.append("<session callid=" + quotes
//                    + cmbean.getSessionCallid() + quotes + " />");
            buffer.append("</TaskDetailsinfo>");
            Log.e("xml", "composed xml for chat======>" + buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AllTaskList", "Exception Compose" + e.getMessage(), "WARN", null);

        } finally {
            return buffer.toString();
        }
    }*/

    public void sendInstantMessage(String msgBody) {

//        String buddy_uri = "<"+number+">";
//        Log.i("chat", "buddy_uri======= " + buddy_uri);
//        BuddyConfig bCfg = new BuddyConfig();
//        bCfg.setUri(buddy_uri);
//        bCfg.setSubscribe(true);

        try {
            for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
                String name = MainActivity.account.buddyList.get(i).cfg.getUri();
                Log.i("task", "buddyname-->" + name);
                //            for(String username:userlist){
                String nn = "sip:" + userName + "@" + getResources().getString(R.string.server_ip);
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
                        Appreference.printLog("AllTaskList", "Exception sendInstant Message " + e.getMessage(), "WARN", null);
                    }
                    break;
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }


    public class TaskArrayAdapter extends BaseAdapter implements Filterable {

        ArrayList<TaskDetailsBean> arrayBuddyList;
        LayoutInflater inflater = null;
        Context adapContext;

        public TaskArrayAdapter(Context context, ArrayList<TaskDetailsBean> buddyList1) {
            // super(context, R.layout.buddy_adapter_row, buddyList1);
            // TODO Auto-generated constructor stub
            this.arrayBuddyList = buddyList1;
            adapContext = context;
            Log.d("TaskArrayAdapter", "task list size inside = " + arrayBuddyList.size());

        }

        @Override
        public int getCount() {
            return arrayBuddyList.size();
        }

        public TaskDetailsBean getItem(int position) {
            return arrayBuddyList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        public int getItemViewType(int position) {


            int value = 0;
            try {
                final TaskDetailsBean contactBean = arrayBuddyList.get(position);
                if (contactBean.getOwnerOfTask() != null && contactBean.getTaskStatus() != null && contactBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                    if (contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("Issue")) {
                        if (qury != null && qury.equalsIgnoreCase("abandoned")) {
                            value = 5;
                            Log.d("swipe", "abandoned if 4 ");
                        } else {
                            value = 4;
                            Log.d("swipe", "abandoned else 5 ");
                        }

                        if (contactBean.getTaskStatus().equalsIgnoreCase("abandoned")) {
                            value = 5;
                            Log.d("swipe", "abandoned if 5* ");
                        }else if (contactBean.getTaskStatus().equalsIgnoreCase("pause")) {
                            value = 8;
                            Log.d("swipe", "abandoned if 5* ");
                        } else if (contactBean.getTaskStatus().equalsIgnoreCase("inprogress") || contactBean.getTaskStatus().equalsIgnoreCase("overdue")) {
                            value = 4;
                            Log.d("swipe", "inprogress else 4* ");
                        }else if (contactBean.getTaskStatus().equalsIgnoreCase("closed") || contactBean.getTaskStatus().equalsIgnoreCase("Closed")) {
                            value = 6;
                            Log.d("swipe", "closed else 1* ");
                        }
                    } else {
                        if (qury != null && qury.equalsIgnoreCase("abandoned")) {
                            value = 3;
                            Log.d("swipe", "abandoned if 3 ");
                        } else {
                            value = 1;
                            Log.d("swipe", "abandoned else 1 ");
                        }

                        if (contactBean.getTaskStatus().equalsIgnoreCase("abandoned")) {
                            value = 3;
                            Log.d("swipe", "abandoned if 3* ");
                        } else if (contactBean.getTaskStatus().equalsIgnoreCase("pause")) {
                            value = 9;
                            Log.d("swipe", "abandoned if 3* ");
                        }else if (contactBean.getTaskStatus().equalsIgnoreCase("inprogress") || contactBean.getTaskStatus().equalsIgnoreCase("overdue")) {
                            value = 1;
                            Log.d("swipe", "inprogress else 1* ");
                        }else if (contactBean.getTaskStatus().equalsIgnoreCase("closed") || contactBean.getTaskStatus().equalsIgnoreCase("Closed")) {
                            value = 7;
                            Log.d("swipe", "closed else 1* ");
                        }
                    }
                } else if (contactBean.getOwnerOfTask() != null && !contactBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && contactBean.getTaskStatus() != null) {
                    if (contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("Issue")) {
                        value = 6;
                    } else {
                        value = 7;
                    }
                } else if (contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("Draft")) {
                    value = 2;
                } else {
                    value = 0;
                }

            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("AllTaskList", "Exception getItemView Type " + e.getMessage(), "WARN", null);
            }
            return value;
        }


        public int getViewTypeCount() {
            return 10;
        }

        public View getView(int pos, View conView, ViewGroup group) {
            try {
                if (conView == null) {
                    inflater = (LayoutInflater) adapContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    conView = inflater.inflate(R.layout.task_history_row, null,
                            false);
                }
                final TaskDetailsBean contactBean = arrayBuddyList.get(pos);
//                Typeface roboto_regular = Typeface.createFromAsset(getAssets(), "roboto.regular.ttf");
//                Typeface roboto_medium = Typeface.createFromAsset(getAssets(), "roboto.medium.ttf");
//                Typeface roboto_bold = Typeface.createFromAsset(getAssets(), "roboto.bold.ttf");
                TextView task_giver = (TextView) conView.findViewById(R.id.task_giver);
                ImageView imageView = (ImageView) conView.findViewById(R.id.state);
                LinearLayout lyt_color = (LinearLayout) conView.findViewById(R.id.lyt_color);
                TextView msg_count = (TextView) conView.findViewById(R.id.item_counter);
                TextView task_taker = (TextView) conView.findViewById(R.id.task_taker);
                TextView task_observer = (TextView) conView.findViewById(R.id.task_observer);
                TextView taskName = (TextView) conView.findViewById(R.id.task_name);
                TextView remain = (TextView) conView.findViewById(R.id.remain_count);
                TextView percen = (TextView) conView.findViewById(R.id.percent_update);
                TextView task_status = (TextView) conView.findViewById(R.id.task_status);
                TextView catagory = (TextView) conView.findViewById(R.id.catagory);
                ImageView selected = (ImageView) conView.findViewById(R.id.selected);
                TextView exclation_counter = (TextView) conView.findViewById(R.id.exclation_counter);
//                taskName.setTypeface(roboto_medium);
//                task_giver.setTypeface(roboto_regular);
//                task_taker.setTypeface(roboto_regular);
//                task_observer.setTypeface(roboto_regular);
//                task_status.setTypeface(roboto_regular);
//                catagory.setTypeface(roboto_regular);
                try {
                    String s = "select * from taskDetailsInfo where taskId='" + contactBean.getTaskId() + "' and msgstatus='12' and loginuser='"+Appreference.loginuserdetails.getEmail()+"'";
                    ArrayList<ProjectDetailsBean> projectDetailsBeen = dataBase.getExclationdetails(s);
                    if (projectDetailsBeen.size() > 0) {
                        exclation_counter.setVisibility(View.VISIBLE);
                        conView.setBackgroundResource(R.color.lgyellow);
                    } else
                        exclation_counter.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if ((contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("note")) && (contactBean.getTaskStatus() != null && !contactBean.getTaskStatus().equalsIgnoreCase("draft"))) {
                    if (contactBean.getTaskReceiver() != null && !contactBean.getTaskReceiver().equalsIgnoreCase("") && !contactBean.getTaskReceiver().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && !contactBean.getTaskReceiver().equalsIgnoreCase(null)) {
                        task_taker.setVisibility(View.VISIBLE);
                        String name = dataBase.getName(contactBean.getTaskReceiver());
                        task_taker.setText(name);
                    } else {
                        task_taker.setVisibility(View.VISIBLE);
                        task_taker.setText("Me");
                    }
                    if (contactBean.getTaskObservers() != null && !contactBean.getTaskObservers().equalsIgnoreCase("") && !contactBean.getTaskObservers().equalsIgnoreCase(null)) {
                        task_observer.setVisibility(View.VISIBLE);
                        int counter = 0;
                        String observernamelist = "", task_obsname;
                        for (int i = 0; i < contactBean.getTaskObservers().length(); i++) {
                            if (contactBean.getTaskObservers().charAt(i) == ',') {
                                counter++;
                            }
                            Log.d("TaskArrayAdapter", "Task Mem's counter size is == " + counter);
                        }
                        for (int j = 0; j < counter + 1; j++) {
                            Log.i("TaskArrayAdapter", "Task Mem's and position == " + contactBean.getTaskObservers().split(",")[j] + " " + j);
                            task_obsname = dataBase.getName(contactBean.getTaskObservers().split(",")[j]);
                            if (task_obsname != null) {
                                observernamelist = observernamelist + task_obsname + ",";
                            } else {
                                observernamelist = observernamelist + "Me,";
                            }
                        }
                        observernamelist = observernamelist.substring(0, observernamelist.length() - 1);
                        Log.i("TaskList", "observernamelist 1 " + observernamelist);
                        Log.i("TaskList", "str2 * " + contactBean.getTaskObservers());
//                            Log.i("getView","taker 13 ");
                        task_observer.setText(observernamelist);
//                        task_observer.setText("Observer : " + contactBean.getTaskObservers());
                    } else {
                        task_observer.setVisibility(View.GONE);
                    }
                } else {
                    task_taker.setVisibility(View.VISIBLE);
                    task_observer.setVisibility(View.VISIBLE);
                }

                if (contactBean.getCompletedPercentage() != null && !contactBean.getCompletedPercentage().equalsIgnoreCase("")) {
                    Log.i("TaskArrayAdapter", "percentage contactBean.getCompletedPercentage() " + contactBean.getCompletedPercentage());
                    if (contactBean.getCompletedPercentage().equalsIgnoreCase("100")) {
                        percen.setTextColor(getResources().getColor(R.color.green));
                        Log.d("TaskArrayAdapter", "percentage green");
                    } else {
                        percen.setTextColor(getResources().getColor(R.color.red));
                        Log.d("TaskArrayAdapter", "percentage red");
                    }
                }

//                String qurey = "select * from taskDetailsInfo where taskId ='" + contactBean.getTaskId() + "' and mimeType='text' and completedPercentage!='0' and completedPercentage!='' order by id Desc";
//                ArrayList<TaskDetailsBean> taskDetailsBean2 = dataBase.getTaskHistory(qurey);


                if (contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("overdue")) {
                    task_status.setText("Overdue");
//                    conView.setBackgroundResource(R.color.red_register);
                    lyt_color.setBackgroundColor(getResources().getColor(R.color.red_register));
                } else if (contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("Template")) {
                    task_status.setText("assigned");
                    Log.i("popup", "Reopen status " + contactBean.getTaskStatus());
                }
                /*else if (contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("Reopen")) {
                    task_status.setText("Reopen");
                    Log.i("popup", "Reopen status " + contactBean.getTaskStatus());
                }*/ else if ((contactBean.getCompletedPercentage() != null && (contactBean.getCompletedPercentage().equalsIgnoreCase("100%") || contactBean.getCompletedPercentage().equalsIgnoreCase("100")))) {
                    if (contactBean.getTaskStatus().equalsIgnoreCase("Closed")) {
                        task_status.setText("Closed");
                        percen.setTextColor(getResources().getColor(R.color.green));
                        Log.i("popup", "status" + contactBean.getTaskName());
                        Log.i("popup", "status 1" + contactBean.getTaskStatus());
                    } else if (contactBean.getTaskStatus().equalsIgnoreCase("Completed")) {
                        task_status.setText("Completed");
                        percen.setTextColor(getResources().getColor(R.color.green));
                        Log.i("popup", "status" + contactBean.getTaskName());
                        Log.i("popup", "status 1" + contactBean.getTaskStatus());
                    } /*else if (contactBean.getTaskStatus().equalsIgnoreCase("Reopen")) {
                        task_status.setText("Reopen");
                        Log.i("popup", "Reopen status **  " + contactBean.getTaskStatus());
                    }*/ else if (contactBean.getTaskStatus().equalsIgnoreCase("draft")) {
                        task_status.setText("Templates");
                        Log.i("popup", "Templates status **  " + contactBean.getTaskStatus());
                    } else {
                        Log.i("popup", "status 2" + contactBean.getTaskStatus());
                        task_status.setText("Inprogress");
                        percen.setTextColor(getResources().getColor(R.color.red));
                        Log.i("popup", "status" + contactBean.getTaskName());
                    }
                } else {
                    String task_stus = contactBean.getTaskStatus();
                    if (task_stus != null && (task_stus.equalsIgnoreCase("inprogress") || task_stus.equalsIgnoreCase("reminder"))) {
                        task_stus = "Inprogress";
                    }
                    task_status.setText(task_stus);
                    percen.setTextColor(getResources().getColor(R.color.red));
                }

                Log.d("AllTaskList", "status " + contactBean.getCatagory());
                Log.d("AllTaskList", "status " + contactBean.getTaskStatus());

                if ((contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("Issue")) && (contactBean.getTaskStatus() != null && !contactBean.getTaskStatus().equalsIgnoreCase("draft"))) {
                    selected.setBackgroundResource(R.drawable.issue_icon);
                    catagory.setText("Issue Id: " + contactBean.getTaskId());
                    taskName.setText("Issue Name: " + contactBean.getTaskName());
                } else if ((contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("Task")) && (contactBean.getTaskStatus() != null && !contactBean.getTaskStatus().equalsIgnoreCase("draft"))) {
                    selected.setBackgroundResource(R.drawable.task_icon);
                    catagory.setText("Task Id: " + contactBean.getTaskId());
                    taskName.setText("Task Name: " + contactBean.getTaskName());
                } else if ((contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("note")) && (contactBean.getTaskStatus() != null && !contactBean.getTaskStatus().equalsIgnoreCase("draft"))) {
                    selected.setBackgroundResource(R.drawable.task_icon);
                    catagory.setText("Task Id: " + contactBean.getTaskId());
                    taskName.setText("Task Name: " + contactBean.getTaskName());
                } else if (contactBean.getTaskStatus() != null && (contactBean.getTaskStatus().equalsIgnoreCase("draft") || contactBean.getTaskStatus().equalsIgnoreCase("Templates"))) {
                    selected.setBackgroundResource(R.drawable.template);
                    catagory.setText("Template Id: " + contactBean.getTaskId());
                    taskName.setText("Template Name: " + contactBean.getTaskName());
                } else {
                    catagory.setText("Task Id: " + contactBean.getTaskId());
                    taskName.setText("Task Name: " + contactBean.getTaskName());
                }
                /*else {
                    selected.setBackgroundResource(R.drawable.task_icon);
                    catagory.setText("Task");
                }*/
//                msg_count.setGravity(Gravity.RIGHT | Gravity.END);

                Log.d("TaskArrayAdapter", "unread count adapter" + contactBean.getRead_status());
                if (contactBean.getRead_status() == 0) {
                    Log.d("TaskArrayAdapter", "task Name =121 ");
                    msg_count.setVisibility(View.GONE);
                } else {
                    Log.d("TaskArrayAdapter", "task Name = 131");
                    msg_count.setVisibility(View.VISIBLE);
                    msg_count.setText(String.valueOf(contactBean.getRead_status()));
                }
                if (contactBean.getOwnerOfTask() != null &&
                        !contactBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) &&
                        contactBean.getMsg_status() == 0) {
                    Log.d("TaskArrayAdapter", "task Name =121 1 ");
                    remain.setVisibility(View.GONE);
                } else {
                    remain.setVisibility(View.VISIBLE);
                    remain.setText("Reminder :" + String.valueOf(contactBean.getMsg_status()));
                    Log.d("TaskArrayAdapter", "task Name =121 45");
                }


                Log.d("TaskArrayAdapter", "task Name = " + contactBean.getTaskName());
                Log.d("TaskArrayAdapter", "task owner = " + contactBean.getOwnerOfTask());
                Log.d("TaskArrayAdapter", "task Receiver = " + contactBean.getTaskReceiver());

//                String head_name = dataBase.getReminderTaskname(contactBean.getTaskId());

                String taskId = contactBean.getTaskId();
                taskId = dataBase.getOverdue(taskId);
                if (contactBean.getTaskType() != null && !contactBean.getTaskType().equalsIgnoreCase("Group")) {
                    if (contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("overdue")) {
                        task_status.setText("Overdue");
//                        conView.setBackgroundResource(R.color.red_register);
                        lyt_color.setBackgroundColor(getResources().getColor(R.color.red_register));
                    } /*else if (contactBean.getTaskStatus() != null && (contactBean.getTaskStatus().equalsIgnoreCase("closed") || contactBean.getTaskStatus().equalsIgnoreCase("Completed"))) {
                        if (contactBean.getTaskStatus().equalsIgnoreCase("Completed")) ;
                    }*/else if (contactBean.getTaskStatus() != null && (contactBean.getTaskStatus().equalsIgnoreCase("assigned") || contactBean.getTaskStatus().equalsIgnoreCase("Template"))) {
                        task_status.setText("assigned");
                        Log.i("Accept", "task_nistory " + task_status);
                    }
                    /*else if (contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("Reopen")) {
                        task_status.setText("Reopen");
                    }*/ /*else if (contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("draft")) {
                        task_status.setText("Templates");
                        Log.d("AllTaskList", "status ** " + contactBean.getTaskStatus());
                    }*/ else if (taskId != null && (taskId.equalsIgnoreCase("closed") || taskId.equalsIgnoreCase("Completed"))) {
                        if (taskId.equalsIgnoreCase("Completed"))
                            task_status.setText("Completed");
                        else
                            task_status.setText("Closed");
                        percen.setTextColor(getResources().getColor(R.color.green));
                    } else if (contactBean.getTaskStatus() != null && !contactBean.getTaskStatus().equalsIgnoreCase("")) {
                        task_status.setText(contactBean.getTaskStatus());
                        percen.setTextColor(getResources().getColor(R.color.red));
                    } else {
                        task_status.setText("Inprogress");
                        Log.i("TaskArrayAdapter", "taskStatus 4****" + task_status);
                        percen.setTextColor(getResources().getColor(R.color.red));
                    }
                } else {
                    if (contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("Overdue")) {
                        task_status.setText("Overdue");
//                        conView.setBackgroundResource(R.color.red_register);
                        lyt_color.setBackgroundColor(getResources().getColor(R.color.red_register));
                    } else if (contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("closed")) {
                        task_status.setText(contactBean.getTaskStatus());
                        percen.setTextColor(getResources().getColor(R.color.green));
                    } else {
                        Log.i("TaskArrayAdapter", "taskStatus 4**** else " + task_status);
                        task_status.setText(contactBean.getTaskStatus());
                        percen.setTextColor(getResources().getColor(R.color.red));
                    }

                }

                if (contactBean.getTaskType() != null && contactBean.getTaskType().equalsIgnoreCase("Group")) {
                    if (contactBean.getCompletedPercentage() != null && !contactBean.getCompletedPercentage().equalsIgnoreCase("") && !contactBean.getCompletedPercentage().equalsIgnoreCase(null) && !contactBean.getCompletedPercentage().equalsIgnoreCase("(null)")) {
                        Log.i("TaskArrayAdapter", "GroupPercentage " + contactBean.getTaskDescription());
                        if (contactBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            if (contactBean.getCompletedPercentage() != null && !contactBean.getCompletedPercentage().equalsIgnoreCase("null")) {
                                percen.setText(contactBean.getCompletedPercentage() + "%");
                            } else {
                                percen.setText("0%");
                            }
                            Log.e("TaskArrayAdapter", "GroupPercentage giver ==> " + contactBean.getCompletedPercentage());
                        } else {
                            if (contactBean.getCompletedPercentage() != null && !contactBean.getCompletedPercentage().equalsIgnoreCase("null")) {
                                percen.setText(contactBean.getCompletedPercentage() + "%");
                            } else {
                                percen.setText("0%");
                            }
                            Log.e("TaskArrayAdapter", "GroupPercentage taker ====>" + contactBean.getCompletedPercentage());

                            if (contactBean.getCompletedPercentage() != null && !contactBean.getCompletedPercentage().equalsIgnoreCase("") && !contactBean.getCompletedPercentage().equalsIgnoreCase(null) && !contactBean.getCompletedPercentage().equalsIgnoreCase("(null)") && contactBean.getCompletedPercentage().equalsIgnoreCase("100")) {
                                if (contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("Overdue")) {
                                    task_status.setText("Overdue");
//                                    conView.setBackgroundResource(R.color.red_register);
                                    lyt_color.setBackgroundColor(getResources().getColor(R.color.red_register));
                                } else if (contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("closed")) {
                                    task_status.setText("Closed");
                                    percen.setTextColor(getResources().getColor(R.color.green));
                                } else {
                                    task_status.setText("Completed");
                                    Log.i("popup", "else status " + contactBean.getTaskStatus());
                                }
                            }
                        }


                    } else {
                        Log.e("TaskArrayAdapter", "GroupPercentage==->" + contactBean.getCompletedPercentage());
//                        percen.setText(contactBean.getCompletedPercentage() + "%");
                        percen.setText("0%");
                    }
                } else {
                    if (contactBean.getCompletedPercentage() != null && !contactBean.getCompletedPercentage().equalsIgnoreCase("") && !contactBean.getCompletedPercentage().equalsIgnoreCase(null) && !contactBean.getCompletedPercentage().equalsIgnoreCase("(null)") && !contactBean.getCompletedPercentage().equalsIgnoreCase("null")) {
                        percen.setText(contactBean.getCompletedPercentage() + "%");
                    } else {
                        percen.setText("0%");
                    }
                }


                if (contactBean.getOwnerOfTask() != null) {
                    if (contactBean.getOwnerOfTask().equals(Appreference.loginuserdetails.getUsername())) {
                        Log.i("Alltasklist", "getview if owner " + contactBean.getTaskReceiver());
                        remain.setVisibility(View.GONE);
                        task_giver.setText("Me");
                        String str = contactBean.getTaskReceiver();
                        String name = dataBase.getname(str);
                        String str2 = contactBean.getTaskObservers();
                        Log.i("Alltasklist", " contactBean.getTaskReceiver() " + str);
                        Log.i("Alltasklist", "getcategory" + contactBean.getCatagory());
                        Log.i("Alltasklist", "loginuserdetail" + Appreference.loginuserdetails.getFirstName() + " " + Appreference.loginuserdetails.getLastName());
                        if (contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("note")) {
                            if (contactBean.getTaskReceiver() != null && !contactBean.getTaskReceiver().equalsIgnoreCase("") && !contactBean.getTaskReceiver().equalsIgnoreCase(null) && !contactBean.getTaskReceiver().equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                task_taker.setVisibility(View.VISIBLE);
                                String receiver = dataBase.getName(contactBean.getTaskReceiver());
                                task_taker.setText(receiver);
                            } else {
                                task_taker.setVisibility(View.VISIBLE);
                                task_taker.setText("Me");
                            }
                        } else {
                            if (name != null) {
                                Log.i("Alltasklist", " contactBean.getTaskReceiver()  1 " + name);
                                if (str != null && str.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    task_taker.setText("Me");
                                    Log.i("Taker ", "Log 5");
                                } else {
                                    if (contactBean.getTaskType().equalsIgnoreCase("group")) {
                                        name = dataBase.getProjectParentTaskId("select taskMemberList from taskHistoryInfo where taskId='" + contactBean.getTaskId() + "'");
                                        String observernamelist = "", task_obsname;
                                        int counter = 0;
                                        for (int i = 0; i < name.length(); i++) {
                                            if (name.charAt(i) == ',') {
                                                counter++;
                                            }
                                            Log.d("TaskArrayAdapter", " observer memer Task Mem's counter size is == " + counter);
                                        }
                                        for (int j = 0; j < counter + 1; j++) {
                                            String Mem_name = name.split(",")[j];
                                            Log.i("TaskArrayAdapter", "observer member Task Mem's and position == " + name.split(",")[j] + " " + j);
                                            task_obsname = dataBase.getName(Mem_name);
                                            if (task_obsname != null) {
                                                if (!Mem_name.equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                                    observernamelist = observernamelist + task_obsname + ",";
                                                }
                                            } else {
                                                observernamelist = observernamelist + "Me,";
                                            }
                                        }
                                        observernamelist = observernamelist.substring(0, observernamelist.length() - 1);
                                        task_taker.setText(observernamelist);
                                        Log.i("Taker", "correctlist" + observernamelist);
                                    } else {
                                        task_taker.setText(name);
                                        Log.i("Taker ", "Log 6" + name);
                                    }
                                }
                            }else {
                                Log.i("Alltasklist", " contactBean.getTaskReceiver() 2 " + null);
                                task_taker.setText(str);
                            }
                        }
                        if (str2 != null && !str2.equalsIgnoreCase("null") && !str2.equalsIgnoreCase("") && !str2.equalsIgnoreCase("(null)")) {
                            int counter = 0;
                            String observernamelist = "", task_obsname;
                            for (int i = 0; i < str2.length(); i++) {
                                if (str2.charAt(i) == ',') {
                                    counter++;
                                }
                                Log.d("TaskArrayAdapter", " observer memer Task Mem's counter size is == " + counter);
                            }
                            for (int j = 0; j < counter + 1; j++) {
                                String Mem_name = str2.split(",")[j];
                                Log.i("TaskArrayAdapter", "observer member Task Mem's and position == " + contactBean.getTaskObservers().split(",")[j] + " " + j);
                                task_obsname = dataBase.getName(Mem_name);
                                if (task_obsname != null) {
                                    if (!Mem_name.equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                        observernamelist = observernamelist + task_obsname + ",";
                                    }
                                } else {
                                    observernamelist = observernamelist + "Me,";
                                }
                            }
                            observernamelist = observernamelist.substring(0, observernamelist.length() - 1);
                            Log.i("TaskList", "observernamelist 1 " + observernamelist);
                            Log.i("TaskList", "str2 * " + str2);
                            task_observer.setText(observernamelist);
                            Log.i("TaskList", "observernamelist giverside * " + observernamelist);
                        } else {
                            task_observer.setText("NA");
                            Log.i("TaskList", "observernamelist  giverside ** ");
                        }
                        imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_arrow_18_16));
                    } else if (contactBean.getTaskReceiver() != null && contactBean.getTaskReceiver().equals(Appreference.loginuserdetails.getUsername())) {
                        Log.i("Alltasklist", "getview if receiver " + contactBean.getTaskReceiver());
                        String str1 = contactBean.getOwnerOfTask();
                        String name1 = dataBase.getname(str1);
                        String str2 = contactBean.getTaskObservers();
                        Log.i("Alltasklist", " contactBean.getOwnerOfTask() 3 " + name1);
                        task_taker.setText("Me");
                        task_giver.setText(name1);
                        Log.i("TaskList", "str2 ** " + str2);
                        if (str2 != null && !str2.equalsIgnoreCase("null") && !str2.equalsIgnoreCase("") && !str2.equalsIgnoreCase("(null)")) {
                            int counter = 0;
                            String observernamelist = "", task_obsname;
                            for (int i = 0; i < str2.length(); i++) {
                                if (str2.charAt(i) == ',') {
                                    counter++;
                                }
                                Log.d("TaskArrayAdapter", "Task Mem's counter size is == " + counter);
                            }
                            for (int j = 0; j < counter + 1; j++) {
                                String Mem_name = str2.split(",")[j];
                                Log.i("TaskArrayAdapter", "Task Mem's and position == " + contactBean.getTaskObservers().split(",")[j] + " " + j);
                                task_obsname = dataBase.getName(Mem_name);
                                if (task_obsname != null) {
                                    if (!Mem_name.equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                        if (!task_obsname.equalsIgnoreCase("")) {
                                            observernamelist = observernamelist + task_obsname + ",";
                                        } else {
                                            observernamelist = observernamelist + Mem_name + ",";
                                        }
//                                        observernamelist = observernamelist + task_obsname + ",";
                                    }
                                } else {
                                    observernamelist = observernamelist + "Me,";
                                }
                            }
                            observernamelist = observernamelist.substring(0, observernamelist.length() - 1);
                            Log.i("TaskList", "observernamelist 1 " + observernamelist);
                            Log.i("TaskList", "str2 * " + str2);
                            task_observer.setText(observernamelist);
                            Log.i("TaskList", "observernamelist takerside * " + observernamelist);
                        } else {
                            task_observer.setText("NA");
                            Log.i("TaskList", "observernamelist  takerside ** ");
                        }
                        imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_arrow_118_16));
                    } else if (contactBean.getTaskObservers() != null && contactBean.getTaskObservers().equals(Appreference.loginuserdetails.getUsername())) {
                        String str = contactBean.getTaskReceiver();
                        String name = dataBase.getname(str);
                        String str1 = contactBean.getOwnerOfTask();
                        String name1 = dataBase.getname(str1);
                        String str2 = contactBean.getTaskObservers();
                        task_giver.setText(name1);
                        Log.i("Alltasklist", " contactBean.getTaskReceiver() 4 " + name1);
                        Log.i("TaskList", "str2 *** " + str2);
                        if (contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("note")) {
                            if (contactBean.getTaskReceiver() != null && !contactBean.getTaskReceiver().equalsIgnoreCase("") && !contactBean.getTaskReceiver().equalsIgnoreCase(null) && !contactBean.getTaskReceiver().equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                task_taker.setVisibility(View.VISIBLE);
                                String receiver = dataBase.getName(contactBean.getTaskReceiver());
                                task_taker.setText(receiver);
                            } else {
                                task_taker.setVisibility(View.VISIBLE);
                                String receiver = dataBase.getName(contactBean.getTaskReceiver());
                                task_taker.setText(receiver);
                            }
                        } else {
                            if (name != null) {
                                Log.i("Alltasklist", " contactBean.getTaskReceiver() 5 " + name1);
                                task_taker.setText(name);
                            } else {
                                Log.i("Alltasklist", " contactBean.getTaskReceiver() 6 " + name1);
                                task_taker.setText(str);
                            }
                        }
                        if (str2 != null && !str2.equalsIgnoreCase("") && !str2.equalsIgnoreCase("null") && !str2.equalsIgnoreCase("(null)")) {
                            int counter = 0;
                            String observernamelist = "", task_obsname;
                            for (int i = 0; i < str2.length(); i++) {
                                if (str2.charAt(i) == ',') {
                                    counter++;
                                }
                                Log.d("TaskArrayAdapter", "observer name list ask Mem's counter size is == " + counter);
                            }
                            for (int j = 0; j < counter + 1; j++) {
                                String Mem_name = str2.split(",")[j];
                                Log.i("TaskArrayAdapter", "observer list Task Mem's and position == " + str2.split(",")[j] + " " + j);
                                task_obsname = dataBase.getName(Mem_name);
                                if (task_obsname != null) {
                                    if (!Mem_name.equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                        if (!task_obsname.equalsIgnoreCase("")) {
                                            observernamelist = observernamelist + task_obsname + ",";
                                        } else {
                                            observernamelist = observernamelist + Mem_name + ",";
                                        }
//                                        observernamelist = observernamelist + task_obsname + ",";
                                    }
                                } else {
                                    observernamelist = observernamelist + "Me,";
                                }
                            }
                            observernamelist = observernamelist.substring(0, observernamelist.length() - 1);

                            task_observer.setText("Me");
                            Log.i("TaskArrayAdapter", "observernamelist obverside * " + observernamelist);
                        } else {
                            task_observer.setText("NA");
                        }

                    } else {
                        String str = contactBean.getGroupTaskMembers();
                        String Owner = contactBean.getOwnerOfTask();
                        String name = dataBase.getname(str);
                        String str2 = contactBean.getTaskObservers();
                        Log.i("TaskArrayAdapter", "str2 **** " + str2);
                        if (str2 != null && !str2.equalsIgnoreCase("") && !str2.equalsIgnoreCase("null") && !str2.equalsIgnoreCase("(null)")) {
                            int counter = 0;
                            String observernamelist = "", task_obsname;
                            for (int i = 0; i < str2.length(); i++) {
                                if (str2.charAt(i) == ',') {
                                    counter++;
                                }
                                Log.d("TaskArrayAdapter", "Task Mem's counter size is == " + counter);
                            }
                            for (int j = 0; j < counter + 1; j++) {
                                String Observer;
                                Log.i("TaskArrayAdapter", "Task Mem's and position 1 == " + str2.split(",")[j] + " " + j + " Owner --->" + Owner);

                                if (str2.contains(",")) {
                                    Observer = str2.split(",")[j];
                                } else {
                                    Observer = str2;
                                }
                                if (str2.contains(",")) {
                                    task_obsname = dataBase.getName(str2.split(",")[j]);
                                } else {
                                    task_obsname = dataBase.getName(str2);
                                }
                                Log.i("TaskObserverList", "Observer  ==  " + Observer + "\n" + "login user name  == " + Appreference.loginuserdetails.getUsername() + "\n" + " task_obsname  == " + task_obsname);

                                if (task_obsname != null && Observer != null && !Observer.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
//                                    if (!Owner.equalsIgnoreCase(str.split(",")[j])) {
                                    observernamelist = observernamelist + task_obsname + ",";
                                    Log.i("TaskObserverList", "observernamelist  inside if " + observernamelist);
//                                    }
                                } else {
                                    if (Observer.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                        Log.i("TaskObserverList", "Observer  ==  1 " + Observer + "\n" + "login user name  == " + Appreference.loginuserdetails.getUsername() + "\n" + " task_obsname  == " + task_obsname);
                                        observernamelist = "Me," + observernamelist;
                                    } else {
                                        if (str2.contains(",")) {
                                            Log.i("TaskObserverList", "Observer  ==  2 " + Observer + "\n" + "login user name  == " + Appreference.loginuserdetails.getUsername() + "\n" + " task_obsname  == " + task_obsname + "\n" + " Str2  == " + str2);
                                            observernamelist = observernamelist + str2.split(",")[j] + ",";
                                        } else {
                                            Log.i("TaskObserverList", "Observer  ==  3 " + Observer + "\n" + "login user name  == " + Appreference.loginuserdetails.getUsername() + "\n" + " task_obsname  == " + task_obsname + "\n" + " str2   == " + str2);
                                            observernamelist = observernamelist + str2 + ",";
                                        }
                                    }
                                    Log.i("TaskObserverList", "observernamelist  inside else " + observernamelist);
                                }
                            }
                            observernamelist = observernamelist.substring(0, observernamelist.length() - 1);
//                            Log.i("getView","observer 22 ");
                            Log.i("TaskObserverList", "Else 1 ---------> 1 observerlist " + observernamelist);
                            Log.i("TaskObserverList", "Else 2 ---------> 2 str2 " + str2);
                            task_observer.setText(observernamelist);
                            Log.i("TaskArrayAdapter", "observernamelist else * " + observernamelist);
                        } /*else if ((contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("note")) || (contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("note"))) {
                            task_observer.setVisibility(View.GONE);
                        }*/ else {
                            task_observer.setText("NA");
                        }
/*                        if (contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("note")) {
                            if (contactBean.getTaskReceiver() != null) {
                                if (contactBean.getTaskReceiver() != null && !contactBean.getTaskReceiver().equalsIgnoreCase("") && !contactBean.getTaskReceiver().equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                    task_taker.setVisibility(View.GONE);
                                } else {
                                    task_taker.setVisibility(View.VISIBLE);
                                }
//                                task_taker.setVisibility(View.VISIBLE);
//                                Log.i("getView","observer 23 ");
                                String name_receiver = dataBase.getname(contactBean.getTaskReceiver());
                                if (name_receiver != null) {
//                                    Log.i("getView","observer 24 ");
                                    task_taker.setText("Taker : " + name_receiver);
                                } else {
//                                    Log.i("getView","observer 25 ");
                                    task_taker.setText("Taker : " + contactBean.getTaskReceiver());
                                }
                            } else {
                                task_taker.setVisibility(View.GONE);
                            }
                        } else*/
                        if (!contactBean.getTaskType().equalsIgnoreCase("group")) {

                            if (contactBean.getTaskReceiver() != null && !contactBean.getTaskReceiver().equalsIgnoreCase("") && contactBean.getTaskReceiver().equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                task_taker.setVisibility(View.VISIBLE);
                            } else {
                                task_taker.setVisibility(View.VISIBLE);
                            }
//                            task_taker.setVisibility(View.VISIBLE);
//                            Log.i("getView", "observer 26 ");
                            String receiver = contactBean.getTaskReceiver();
                            String name_receiver = dataBase.getname(str);
                            if (name != null && !name.equalsIgnoreCase("") && !name.equalsIgnoreCase(null)) {
//                                Log.i("getView", "observer 27 ");
                                task_taker.setText(name_receiver);
                            } else {
//                                Log.i("getView", "observer 28 ");
                                if (receiver != null && receiver.contains("_")) {
                                    receiver = dataBase.getname(receiver);
                                    task_taker.setText(receiver);
                                } else {
                                    task_taker.setText(receiver);
                                }

                            }
                        } else {
                            if (str != null && !str.equalsIgnoreCase("null") && !str.equalsIgnoreCase("") && !str.equalsIgnoreCase("(null)")) {
                                task_taker.setVisibility(View.VISIBLE);
                                Log.d("TaskArrayAdapter", " observer memer Task Mem's counter size is == " + str);
                                int counter = 0;
                                String observernamelist = "", task_obsname;
                                for (int i = 0; i < str.length(); i++) {
                                    if (str.charAt(i) == ',') {
                                        counter++;
                                    }
                                    Log.d("TaskArrayAdapter", " observer memer Task Mem's counter size is == " + counter);
                                }
                                for (int j = 0; j < counter + 1; j++) {
                                    String Taker = str.split(",")[j];
                                    Log.i("TaskArrayAdapter", "observer member Task Mem's and position == " + contactBean.getGroupTaskMembers().split(",")[j] + " " + j);
                                    task_obsname = dataBase.getName(str.split(",")[j]);
                                    if (task_obsname != null && Taker != null && !Taker.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                        if (Owner != null && Taker != null) {
                                            if (!Owner.equalsIgnoreCase(Taker)) {
                                                observernamelist = observernamelist + task_obsname + ",";
                                            }
                                        }
                                    } else {
                                        observernamelist = "Me," + observernamelist;
                                    }
                                }
                                observernamelist = observernamelist.substring(0, observernamelist.length() - 1);
                                Log.i("TaskList", "observernamelist 1 " + observernamelist);
                                Log.i("TaskList", "str2 * " + str);
                                Log.i("getView", "observer 29 ");
                                task_taker.setText(observernamelist);
                                imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_arrow_118_16));
                                Log.i("TaskList", "observernamelist giverside * " + observernamelist);
                            }
                        }
                        String str1 = contactBean.getOwnerOfTask();
                        String name1 = dataBase.getname(str1);
//                        Log.i("getView","observer 30 ");
                        task_giver.setText(name1);
                    }
                    /*else {
                        String str = contactBean.getTaskReceiver();
                        String name = dataBase.getname(str);
                        String str2 = contactBean.getTaskObservers();
                        Log.i("TaskArrayAdapter", "str2 **** " + str2);
                        if (str2 != null && !str2.equalsIgnoreCase("") && !str2.equalsIgnoreCase("null") && !str2.equalsIgnoreCase("(null)")) {
                            int counter = 0;
                            String observernamelist = "", task_obsname;
                            for (int i = 0; i < str2.length(); i++) {
                                if (str2.charAt(i) == ',') {
                                    counter++;
                                }
                                Log.d("TaskArrayAdapter", "Task Mem's counter size is == " + counter);
                            }
                            for (int j = 0; j < counter + 1; j++) {
                                String Mem_name = str2.split(",")[j];
                                Log.i("TaskArrayAdapter", "Task Mem's and position == " + str2.split(",")[j] + " " + j);
                                task_obsname = dataBase.getName(Mem_name);
                                if (task_obsname != null) {
                                    if (!Mem_name.equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                        if (!task_obsname.equalsIgnoreCase("")) {
                                            observernamelist = observernamelist + task_obsname + ",";
                                        } else {
                                            observernamelist = observernamelist + Mem_name + ",";
                                        }
//                                        observernamelist = observernamelist + task_obsname + ",";
                                    }
                                } else {
                                    observernamelist = observernamelist + "Me,";
                                }
                            }
                            observernamelist = observernamelist.substring(0, observernamelist.length() - 1);

                            task_observer.setText("Observer :" + observernamelist);
                            Log.i("TaskArrayAdapter", "observernamelist else * " + observernamelist);
                        } else {
                            task_observer.setText("Observer : " + "NA");
                        }
                        if(contactBean.getTaskType()!=null && contactBean.getTaskType().equalsIgnoreCase("group")){
                            ArrayList<String> group_mems = dataBase.getGroupMember()
                        }else {
                            Log.i("Alltasklist", " contactBean.getTaskReceiver() 7 " + name);
                        }
                        if (contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("note")) {
                            if (contactBean.getTaskReceiver() != null && !contactBean.getTaskReceiver().equalsIgnoreCase("") && !contactBean.getTaskReceiver().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && !contactBean.getTaskReceiver().equalsIgnoreCase(null)) {
                                task_taker.setVisibility(View.VISIBLE);
                                String receiver = dataBase.getName(contactBean.getTaskReceiver());
                                task_taker.setText("Tasker : " + receiver);
                            } else {
                                task_taker.setVisibility(View.VISIBLE);
                                String receiver = dataBase.getName(contactBean.getTaskReceiver());
                                task_taker.setText("Taker : " + receiver);
                            }
                        } else {
                            if (name != null) {
                                Log.i("Alltasklist", " contactBean.getTaskReceiver() 8 " + name);
                                task_taker.setText("Taker : " + name);
                            } else {
                                Log.i("Alltasklist", " contactBean.getTaskReceiver() 9 " + null);
                                task_taker.setText("Taker : " + str);
                            }
                        }
                        String str1 = contactBean.getOwnerOfTask();
                        String name1 = dataBase.getname(str1);
                        task_giver.setText("Giver : " + name1);
                    }*/
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("AllTaskList", "Exception getView " + e.getMessage(), "WARN", null);
            }
            return conView;
        }

        @Override
        public Filter getFilter() {
            if (filter == null) {
                Log.d("constraint", "JNDSEJBJW  ** ");
                filter = new HistoryFilter();
            }
            return filter;
        }
    }

    private class HistoryFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults result = null;

            try {
                //constraint = constraint.toString();
                String s1 = constraint.toString().toLowerCase();

                Log.d("HistoryFilter", "filter : 0 " + s1);

                result = new FilterResults();
                if (constraint.toString().length() > 0) {
                    Log.i(" HistoryFilter ", "  1 " + constraint.toString().length());
                    ArrayList<TaskDetailsBean> taskdetailsbeanlist = new ArrayList<>();
                    ArrayList<TaskDetailsBean> s = new ArrayList<TaskDetailsBean>();

                    for (int i = 0, l = taskDetailsBeanArraylist.size(); i < l; i++) {
                        Log.i("HistoryFilter", "insidefor 2 " + taskDetailsBeanArraylist.size());
                        s.add(taskDetailsBeanArraylist.get(i));
                        Log.i("HistoryFilter", " s 3 " + s.get(i).getTaskId());
                        //Log.i("sizes","sizes"+s.toString());
                        String s2 = s.get(i).getTaskId();
                        String s3 = s.get(i).getTaskName().toLowerCase();
                        Log.i(" constraint ", " s2  4 " + s2);

                        if (s2.contains(s1)) {
                            Log.i("HistoryFilter", "insideif 5 " + String.valueOf(constraint));
                            taskdetailsbeanlist.add(s.get(i));
                            Log.i("HistoryFilter", "List 6 " + taskdetailsbeanlist.toString());
                        } else if (s3.contains(s1)) {
                            taskdetailsbeanlist.add(s.get(i));
                        }

                    }
                    result.values = taskdetailsbeanlist;
                    Log.i("HistoryFilter", "result 7 " + result.values.toString());
                    result.count = taskdetailsbeanlist.size();
                    Log.i("HistoryFilter", "resultcount 8 " + result.count);

                } else {
                    synchronized (this) {
                        result.values = taskDetailsBeanArraylist;
                        Log.i("HistoryFilter", "resultelse 9 " + result.values);
                        result.count = taskDetailsBeanArraylist.size();
                        Log.i("HistoryFilter", "resultelsecount 10 " + result.count);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("AllTaskList", "Exception perform Filltering " + e.getMessage(), "WARN", null);
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            try {
                Log.d("HistoryFilter", "JNDSEJBJW  11 " + results.count);
                taskDetailsBeen.clear();
                filterbuddy = (ArrayList<TaskDetailsBean>) results.values;
                if (filterbuddy.size() > 0) {
                    Noresult.setVisibility(View.GONE);
                    for (int i = 0, l = filterbuddy.size(); i < l; i++) {
                        // buddyList.add(filterbuddy.get(i));
                        Log.d("HistoryFilter", "JNDSEJBJW  12 " + results.count);
                        taskDetailsBeen.add(filterbuddy.get(i));


                    }
                } else {
                    Noresult.setVisibility(View.VISIBLE);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("constraint", "JNDSEJBJW  13 ");
                        buddyArrayAdapter.notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("AllTaskList", "Exception Publish Result " + e.getMessage(), "WARN", null);
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == 981) {
                    Noresult.setVisibility(View.GONE);
                    String query1;
                    String query2 = null;
                    qury = data.getStringExtra("Query");
//                    active_task.setText(qury);
                    //                Log.i("AllTaskList", "type" + taskType);

                    //                if (taskType != null && taskType.equalsIgnoreCase("Individual")) {
                    //                    query1 = "select * from taskDetailsInfo where (taskStatus  = '" + qury + "' ) and  (fromUserName='" + userName + "' or toUserName='" + userName + "') and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' )  group by taskNo";
                    if (qury.equalsIgnoreCase("inprogress")) {
                        Log.i("AllTaskList", "filtered was inprogress " + qury);
                        fill.setBackgroundResource(R.drawable.filterenabled);
                        query1 = "select * from taskHistoryInfo where taskStatus ='" + qury + "' group by taskId";
                        query2 = "select * from projectHistory where taskStatus ='" + qury + "' and parentTaskId != taskId";
                    } else if (qury.equalsIgnoreCase("issue")) {
//                        active_task.setText("Issues for task");
                        catagory = "issue";
                        query1 = "select * from taskHistoryInfo where  category = '" + catagory + "' group by taskId";
                        //                    query2 = "select * from projectHistory where category = '" + catagory + "' and parentTaskId != taskId";
                        Log.i("AllTaskList", "filtered was 1*** Issues for task " + qury);
                    } else if (qury.equalsIgnoreCase("Task&Issue")) {
                        query1 = "select * from taskHistoryInfo group by taskId";
                        query2 = "select * from projectHistory where parentTaskId != taskId";
                        Log.i("AllTaskList", "filtered was 2** Task&Issue " + qury);
                    } else if (qury.equalsIgnoreCase("note")) {
                        query1 = "select * from taskHistoryInfo where category ='" + qury + "' group by taskId";
                        query2 = "select * from projectHistory where parentTaskId != taskId";
                        Log.i("AllTaskList", "filtered was 2** note " + qury);
                    } else if (!qury.equalsIgnoreCase("alltask")) {
                        Log.i("AllTaskList", "filtered was !qury.equalsIgnoreCase('alltask') " + qury);
                        query2 = "select * from projectHistory where taskStatus ='" + qury + "' and parentTaskId != taskId";
                        query1 = "select * from taskHistoryInfo where taskStatus ='" + qury + "' group by taskId";
                       /* else {
                            catagory = "issue";
                            query1 = "select * from taskDetailsInfo where ( ( fromUserId='" + userName + "' ) or ( toUserId='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + taskType + "')  and (projectId IS NULL or projectId = 'null' or projectId = '(null)') group by taskId";

                        }*/
                    } else {
                        Log.i("AllTaskList", "filtered was alltask " + qury);
                        fill.setBackgroundResource(R.drawable.filterenabled);
                        catagory = "Task";
                        fill.setBackgroundResource(R.drawable.filterdisabled);
                        query2 = "select * from projectHistory where parentTaskId != taskId";
                        query1 = "select * from taskHistoryInfo where category = '" + catagory + "'  group by taskId";
                    }
                    //                } else if (taskType != null && taskType.equalsIgnoreCase("Group")) {

                    //                    if (qury.equalsIgnoreCase("inprogress")) {
                    //                        Log.i("task", "filtered was " + qury);
                    //                        fill.setBackgroundResource(R.drawable.filterenabled);
                    //                        query1 = "select * from taskHistoryInfo where (taskStatus ='" + qury + "') and  ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' ) and ( taskType='" + taskType + "')  group by taskId";
                    //                    } else if (qury.equalsIgnoreCase("issue")) {
                    //                        active_task.setText("Issues for task");
                    //                        catagory = "issue";
                    //                        query1 = "select * from taskHistoryInfo where ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' )  and category = '" + catagory + "' group by taskNo";
                    //                        Log.i("task", "filtered was 1*** " + qury);
                    //                    } else if (qury.equalsIgnoreCase("Task&Issue")) {
                    //                        catagory = "issue";
                    //                        query1 = "select * from taskHistoryInfo where ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' )  group by taskNo";
                    //                        Log.i("task", "filtered was 2** " + qury);
                    //                    } else if (!qury.equalsIgnoreCase("alltask"))
                    //                        query1 = "select * from taskHistoryInfo where ( taskStatus = '" + qury + "' ) and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + taskType + "')  group by taskId";
                    //                    else {
                    //                        catagory = "issue";
                    //                        query1 = "select * from taskHistoryInfo where ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + taskType + "')  and (projectId IS NULL or projectId = 'null' or projectId = '(null)') group by taskId";
                    //
                    //                    }
                    //                }

                    taskDetailsBeen.clear();
                    taskDetailsBeen1.clear();
                    taskDetailsBeen2.clear();
                    taskDetailsBeanArraylist.clear();
                    //                String query1 = "select * from taskDetailsInfo where (taskStatus <> '" + qury + "' or taskStatus is null ) and  (fromUserName='" + userName + "' or toUserName='" + userName + "') and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' )  group by taskNo";
                    //                String query1 = "select * from taskDetailsInfo where (taskStatus <> '"+qury+"' or taskStatus is null ) and  (fromUserName='" + userName + "' or toUserName='" + userName + "') and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' )  group by taskNo";
                    Log.d("task", "query   " + query1);

                    taskDetailsBeen1.addAll(dataBase.getTaskHistoryInfo(query1));
                    if (qury != null && !qury.equalsIgnoreCase("issue")) {
                        taskDetailsBeen1.addAll(dataBase.getProjectHistoryTasks(query2));
                    }
                    //                taskDetailsBeanArraylist=(ArrayList<TaskDetailsBean>)taskDetailsBeen1.clone();
                    Log.e("size", String.valueOf(taskDetailsBeen1.size()));
                    Log.i("task", "size was " + taskDetailsBeanArraylist.size());
                    //        taskDetailsBeen.addAll(taskDetailsBeen1);
                    /*if (!qury.equalsIgnoreCase("alltask")) {
                        if (taskDetailsBeen1.size() > 0) {
                            for (TaskDetailsBean taskDetailsBean_1 : taskDetailsBeen1) {
                                String lastTask_Status = dataBase.getLastStatus(taskDetailsBean_1.getTaskId());
                                if (lastTask_Status.equalsIgnoreCase(qury)) {
                                    Log.d("Last Status", "value " + lastTask_Status);
                                    taskDetailsBeen2.add(taskDetailsBean_1);
                                }
                            }
                        }
                    } else */
                    {
                        taskDetailsBeen2.addAll(taskDetailsBeen1);
                    }
                    Log.d("Last Status", "value " + taskDetailsBeen2.size());

                    for (TaskDetailsBean contactBean : taskDetailsBeen2) {
                        Log.i("task", "project 1");
                        int unReadMsgCount = dataBase.getTaskUnReadMsgCount(contactBean.getTaskId());

                        rem = dataBase.getRemainderUnReadMsgCount(contactBean.getTaskNo());

                        Log.d("task1", "unread count" + unReadMsgCount);

                        int percentage = 0;
                        Log.d("task1", "unread count" + unReadMsgCount + contactBean.getToUserName() + contactBean.getTaskId() + contactBean.getOwnerOfTask() + " " + contactBean.getTaskType());
                        /*if (contactBean.getTaskType() != null && contactBean.getTaskType().equalsIgnoreCase("Group")) {
                            percentage = dataBase.GroupPercentageChecker(contactBean.getToUserName(), contactBean.getTaskId(), contactBean.getOwnerOfTask());
                        } else {
                            percentage = dataBase.percentagechecker(contactBean.getTaskId());
                        }*/
                        if (contactBean.getTaskType() != null && contactBean.getTaskType().equalsIgnoreCase("Group")) {
                            Log.d("TaskHistory", "Group percent 11 " + contactBean.getTaskType());
                            if (contactBean.getOwnerOfTask() != null && contactBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                Log.d("TaskHistory", "unread count 21 " + unReadMsgCount);
                                //                            Log.d("TaskHistory", "contactBean.getFromUserName() " + contactBean.getFromUserName());
                                //                            Log.d("TaskHistory", "contactBean.getOwnerOfTask() " + contactBean.getOwnerOfTask());
                                if (contactBean.getFromUserName() != null && contactBean.getFromUserName().equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                    Log.d("TaskHistory", "unread count 31 " + unReadMsgCount);
                                    percentage = Integer.parseInt(dataBase.getlastCompletedParcentage(contactBean.getTaskId()));
                                } else {
                                    Log.d("TaskHistory", "unread count 41 " + unReadMsgCount);
                                    percentage = dataBase.GroupPercentageChecker(contactBean.getToUserName(), contactBean.getTaskId(), contactBean.getOwnerOfTask());
                                }
                            } else {
                                Log.d("TaskHistory", "getFromUserName count 41 " + contactBean.getFromUserName());
                                Log.d("TaskHistory", "getOwnerOfTask count 41 " + contactBean.getOwnerOfTask());
                                if (contactBean.getFromUserName() != null && contactBean.getFromUserName().equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                    Log.d("TaskHistory", "unread count 51 " + unReadMsgCount);
                                    percentage = Integer.parseInt(contactBean.getCompletedPercentage());
                                } else {
                                    Log.d("TaskHistory", "unread count 61 " + unReadMsgCount);
                                    String TakerPercent_value = null;
                                    try {
                                        TakerPercent_value = dataBase.getTakerlastCompletedParcentage(contactBean.getTaskId());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (TakerPercent_value != null)
                                        percentage = Integer.parseInt(TakerPercent_value);
                                }
                            }

                        } else {
                            try {
                                percentage = dataBase.percentagechecker(contactBean.getTaskId());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        contactBean.setCompletedPercentage(String.valueOf(percentage));

                        contactBean.setRead_status(unReadMsgCount);

                        contactBean.setMsg_status(rem);

                        taskDetailsBeen.add(contactBean);
                        taskDetailsBeanArraylist.add(contactBean);

                    }

                    Log.d("task", "task list size = " + taskDetailsBeen.size());

                    //                class StringDateComparator implements Comparator<TaskDetailsBean> {
                    //                    String date_lhs = null;
                    //                    String date_rhs = null;
                    //
                    //                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //
                    //                    public int compare(TaskDetailsBean lhs, TaskDetailsBean rhs) {
                    //                        try {
                    //                            Log.i("DateFormat", "leftside" + lhs.getDateTime());
                    //                            Log.i("DateFormat", "rightside" + rhs.getDateTime());
                    ////                    date_lhs = dateFormat.parse(lhs.getDateTime());
                    ////                    date_rhs = dateFormat.parse(rhs.getDateTime());
                    //                            date_lhs = lhs.getDateTime();
                    //                            date_rhs = rhs.getDateTime();
                    //                            Log.i("DateFormet", "ListPosition" + taskDetailsBeen.get(1).getTaskDescription());
                    //                        } catch (Exception e) {
                    //                            e.printStackTrace();
                    //                        }
                    //                        return date_lhs.compareTo(date_rhs);
                    //                    }
                    //                }
                    //
                    //                Collections.sort(taskDetailsBeen, new StringDateComparator());
                    //                Collections.reverse(taskDetailsBeen);
                  /*  for (TaskDetailsBean taskDetailsBean:taskDetailsBeen) {
                        if(getTaskObservers(taskDetailsBean.getTaskId())){
                            taskDetailsBeen.remove(taskDetailsBean);
                        }

                    }*/
                    buddyArrayAdapter = new TaskArrayAdapter(context, taskDetailsBeen);
                    listView.setAdapter(buddyArrayAdapter);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            buddyArrayAdapter.notifyDataSetChanged();
                        }
                    });
                    check = false;

                }

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Appreference.printLog("AllTaskList", "Exception onActivity Result " + e.getMessage(), "WARN", null);
        } catch (Exception ex) {
            ex.printStackTrace();
            Appreference.printLog("AllTaskList", "Exception onactivity result 1 " + ex.getMessage(), "WARN", null);
        }

    }


    public boolean getTaskObservers(String taskId) {
        boolean observerCheck = false;
        try {
            String query1 = "select * from taskDetailsInfo where loginuser ='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + taskId + "' and sendStatus = '3' order by id DESC LIMIT 1";

            Log.d("TaskObserver", "get Observer query  " + query1);

            ArrayList<TaskDetailsBean> arrayList;
            observerCheck = false;
            arrayList = dataBase.getTaskHistory(query1);

            Log.d("TaskObserver", "Task Observer list size is == " + arrayList.size());
            if (arrayList.size() > 0) {
                TaskDetailsBean taskDetailsBean = arrayList.get(0);

                String taskObservers = taskDetailsBean.getTaskObservers();
                Log.d("TaskObserver", "Task Observer  == " + taskObservers);

                int counter = 0;
                if (taskObservers != null) {
                    for (int i = 0; i < taskObservers.length(); i++) {
                        if (taskObservers.charAt(i) == ',') {
                            counter++;
                        }
                    }

                    Log.d("TaskObserver", "Task Observer counter size is == " + counter);

                    for (int j = 0; j < counter + 1; j++) {
                        if (Appreference.loginuserdetails.getUsername().equalsIgnoreCase(taskObservers.split(",")[j])) {
                            observerCheck = !(taskDetailsBean.getOwnerOfTask() != null && taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(userName));
                            Log.d("TaskObserver", "Task Observer name not in same user== " + taskObservers.split(",")[j]);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AllTaskList", "Exception gettask Observer " + e.getMessage(), "WARN", null);
        }
        return observerCheck;
    }
    private void getDataFromDBWhenScroll_the_List(int count) {
        try {
            Log.i("lazyload", "listview " + count);
            boolean countAboveten = false;
            if (count != 0 && count >= 10) {
                count = count - 10;
                taskList_count = count;
                countAboveten = true;
            } else if (count < 10) {
                count = count;
                taskList_count = 0;
            } else {
                count = 0;
                taskList_count = 0;
            }
            Log.i("lazyload", "db count--->" + count);
            Log.i("lazyload", "getDataFromDBWhenScroll_the_List buddyOrgroup_count--->" + taskList_count);
            if (count == 0 && !countAboveten) {
                query = "select * from taskHistoryInfo where taskStatus!='draft' and category!='chat' and ( loginuser='" + currentLoginUserMail + "') ";
            }
            else if (count < 10 && !countAboveten) {
                query = "select * from taskHistoryInfo where taskStatus!='draft' and category!='chat' and ( loginuser='" + currentLoginUserMail + "') order by dateTime ASC LIMIT " + count + " OFFSET 0";
            } else {
                query = "select * from taskHistoryInfo where taskStatus!='draft' and category!='chat' and ( loginuser='" + currentLoginUserMail + "') order by dateTime ASC LIMIT 10 OFFSET " + count + "";
            }
            Log.i("DBQuery", " currentLoginUserName is == 2 " + currentLoginUserName + " \n currentLoginUserMail " + currentLoginUserMail);
//            if (taskType != null && taskType.equalsIgnoreCase("Group")) {
//                query = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + currentLoginUserMail + "') and ( taskType='" + taskType + "')  group by taskId";
//            } else {
//                query = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + currentLoginUserMail + "') and ( taskType='" + taskType + "')  group by taskId";
//                query = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserName='black_test.com' ) or ( toUserName='black_test.com' )) and ( loginuser='black@test.com') and ( taskType='Individual') and projectId IS NULL group by taskId";
//            }
            Log.d("DBQuery", "select query is == 3 " + query);
            taskDetailsBeen1.addAll(dataBase.getTaskHistoryInfo(query));
            Log.d("DBQuery", "size of history 4 1 " + taskDetailsBeen1.size());
//            query = "select * from projectHistory where parentTaskId != taskId";
//            taskDetailsBeen1.addAll(dataBase.getProjectHistoryTasks(query));
          /*  for (TaskDetailsBean taskDetailsBean : taskDetailsBeen1) {
                reopen_qry = "select taskId from taskHistoryInfo where (parentTaskId ='" + taskDetailsBean.getTaskId() + "')";
                Log.d("DBQuery", "reopen_qry--> " + reopen_qry);
                Log.d("DBQuery", "reopen_qry boolean --> " + dataBase.isAgendaRecordExists(reopen_qry));
                if (dataBase.isAgendaRecordExists(reopen_qry)) {
                    taskDetailsBean.setTaskStatus("Reopen");
                    taskDetailsBean.setCompletedPercentage("100");
                }
            }*/
//            Log.d("DBQuery", "reopen_qry--> " + reopen_qry);
//            taskDetailsBeanArraylist=(ArrayList<TaskDetailsBean>) taskDetailsBeen1.clone();
            Log.i("DBQuery", "size of history 4 " + taskDetailsBeen1.size());
            Log.i("DBQuery", "size of history 4 " + taskDetailsBeanArraylist.size());
//            Log.i("DBQuery", "userName 5 " + userName);
            taskDetailsBeen.clear();
            taskDetailsBeanArraylist.clear();
            if (taskDetailsBeen1.size() > 0) {
                for (TaskDetailsBean contactBean : taskDetailsBeen1) {
                    Log.i("TaskHistory", "project 3");
                    int unReadMsgCount = dataBase.getTaskUnReadMsgCount(contactBean.getTaskId());

                    Log.d("TaskHistory", "unread count" + unReadMsgCount);
                    Log.d("task1", "unread count" + unReadMsgCount);
                    Log.d("task1", "for each  issues Id  == " + contactBean.getIssueId());
                    rem = dataBase.getRemainderUnReadMsgCount(contactBean.getTaskId());
                    contactBean.setRead_status(unReadMsgCount);
                    contactBean.setMsg_status(rem);

                    Log.i("TaskHistory", "popup status 3 8 " + contactBean.getTaskStatus());
                /*else{
                            Log.i("popup", "status" + contactBean.getTaskName());
                            Log.i("popup", "status 4" + contactBean.getTaskStatus());
                            contactBean.setTaskStatus("inprogress");
                        }*/
                  /*  String qry = "select taskDescription from taskDetailsInfo where (taskId ='" + contactBean.getTaskId() + "') and ((taskDescription ='Task accepted') or (taskDescription ='issue accepted')) and ((taskDescription !='Task Rejected') or (taskDescription ='issue Rejected'))group by taskId";
                    if(!dataBase.isAgendaRecordExists(qry)){
                        contactBean.setTaskStatus("Assigned");
                    }*/

                    /*if (contactBean.getTaskStatus().equalsIgnoreCase("completed")) {
                        reopen_qry = "select taskId from taskHistoryInfo where (parentTaskId ='" + contactBean.getTaskId() + "') and taskStatus != 'closed'";
                        Log.d("DBQuery", "reopen_qry--> " + reopen_qry);
                        Log.d("DBQuery", "reopen_qry boolean --> " + dataBase.isAgendaRecordExists(reopen_qry));
                        if (dataBase.isAgendaRecordExists(reopen_qry)) {
                            contactBean.setTaskStatus("Reopen");
                        }
                    }*/
                    //            Log.e("taskpercentage",contactBean.getCompletedPercentage());
                    Log.i("popup", "status after query " + contactBean.getTaskStatus());
                    taskDetailsBeen.add(contactBean);
                    taskDetailsBeanArraylist.add(contactBean);

                }


            }

            class StringDateComparator implements Comparator<TaskDetailsBean> {
                String date_lhs = null;
                String date_rhs = null;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                public int compare(TaskDetailsBean lhs, TaskDetailsBean rhs) {
                    try {
                        Log.i("TaskHistory", "DateFormat leftside 9 " + lhs.getDateTime());
                        Log.i("TaskHistory", "DateFormat rightside 10 " + rhs.getDateTime());
                        date_lhs = lhs.getDateTime();
                        date_rhs = rhs.getDateTime();
                        Log.i("TaskHistory", "DateFormat ListPosition 11 " + taskDetailsBeen.get(1).getTaskDescription());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return date_lhs.compareTo(date_rhs);
                }
            }
            Collections.sort(taskDetailsBeen, new

                            StringDateComparator()

            );
            Collections.reverse(taskDetailsBeen);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    buddyArrayAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyAdapter1 extends ArrayAdapter<String> {
        public MyAdapter1(Context ctx, int txtViewResourceId, String[] objects) {
            super(ctx, txtViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);
        }

        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            View mySpinner = null;
            try {
                LayoutInflater inflater = getLayoutInflater();
                mySpinner = inflater.inflate(R.layout.customspin, parent,
                        false);
                TextView main_text = (TextView) mySpinner.findViewById(R.id.customtext);
                main_text.setText(catestring1[position]);
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("AllTaskList", "Exception getCustomView MyAdapter 1 " + e.getMessage(), "WARN", null);
            }
            return mySpinner;
        }
    }

    public class MyAdapter2 extends ArrayAdapter<String> {
        public MyAdapter2(Context ctx, int txtViewResourceId, String[] objects) {
            super(ctx, txtViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);
        }

        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            View mySpinner = null;
            try {
                LayoutInflater inflater = getLayoutInflater();
                mySpinner = inflater.inflate(R.layout.customspin, parent,
                        false);
                TextView main_text = (TextView) mySpinner.findViewById(R.id.customtext);
                main_text.setText(catestring2[position]);
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("AllTaskList", "Exception getCustomView My Adapter 2 " + e.getMessage(), "WARN", null);
            }
            return mySpinner;
        }
    }

    public class MyAdapter3 extends ArrayAdapter<String> {
        public MyAdapter3(Context ctx, int txtViewResourceId, String[] objects) {
            super(ctx, txtViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);
        }

        @Override
        public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            View mySpinner = null;
            try {
                LayoutInflater inflater = getLayoutInflater();
                mySpinner = inflater.inflate(R.layout.customspin, parent,
                        false);
                TextView main_text = (TextView) mySpinner.findViewById(R.id.customtext);
                main_text.setText(catestring3[position]);
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("AllTaskList", "Exception getCustomView MyAdapter 3 " + e.getMessage(), "WARN", null);
            }
            return mySpinner;
        }
    }

    public void Active_Task(String taskId, String completedPercentage) {
        ArrayList<TaskDetailsBean> taskDetail;
        String TaskId = taskDetailsBean.getTaskId();
        String TaskMemberList;
        taskDetail = dataBase.getTaskHistoryInfo("Select * from taskHistoryInfo where taskId ='" + TaskId + "'");
        if (taskDetail.size() > 0) {
            TaskDetailsBean taskbean = taskDetail.get(0);
            TaskDetailsBean chatBean = new TaskDetailsBean();
            String tasktime, taskUTCtime;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = dateFormat.format(new Date());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateforrow = dateFormat.format(new Date());
            tasktime = dateTime;
            tasktime = tasktime.split(" ")[1];
            Log.i("task", "tasktime" + tasktime);
            Log.i("UTC", "sendMessage utc time" + dateforrow);
            Log.i("time", "value");
            taskUTCtime = dateforrow;
            Log.i("Accept", "value 5 " + chatBean.getTaskStatus());
            chatBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
            chatBean.setFromUserName(Appreference.loginuserdetails.getUsername());
            if (taskbean.getTaskType().equalsIgnoreCase("Group")) {
                TaskMemberList = dataBase.getProjectParentTaskId("select taskMemberList from taskHistoryInfo where taskId='" + taskId + "'");
            } else {
                TaskMemberList = dataBase.getProjectParentTaskId("select taskReceiver from taskHistoryInfo where taskId='" + taskId + "'");
            }
            if (!taskbean.getTaskType().equalsIgnoreCase("Group")) {
                chatBean.setToUserId(taskbean.getToUserId());
                chatBean.setTaskReceiver(TaskMemberList);
                chatBean.setToUserName(taskbean.getToUserName());
            } else {
                chatBean.setToUserId(taskbean.getToUserId());
                chatBean.setTaskReceiver(TaskMemberList);
                chatBean.setToUserName(taskbean.getToUserName());
            }
            chatBean.setTaskType(taskbean.getTaskType());
            chatBean.setTaskId(taskId);
            chatBean.setTaskStatus("inprogress");
            Log.i("DataBase", " Bean Value Creating Bean -----> " + tasktime);
            chatBean.setTasktime(tasktime);
            chatBean.setTaskName(taskbean.getTaskName());
            chatBean.setOwnerOfTask(Appreference.loginuserdetails.getUsername());
            chatBean.setCustomTagVisible(true);
            chatBean.setTaskNo(taskbean.getTaskNo());
            chatBean.setCatagory(taskbean.getCatagory());
            if (taskbean.getCatagory() != null && taskbean.getCatagory().equalsIgnoreCase("issue")) {
                chatBean.setIssueId(taskbean.getIssueId());
                chatBean.setTaskDescription("This issue is activated"); // contains ToUserName's
            } else {
                chatBean.setTaskDescription("This task is activated"); // contains ToUserName's
            }
            chatBean.setSendStatus("0");
            chatBean.setUtcPlannedStartDateTime("");
            chatBean.setUtcPlannedStartDateTime("");
            chatBean.setUtcplannedEndDateTime("");
            chatBean.setUtcPemainderFrequency("");
            chatBean.setDuration("");
            chatBean.setDurationUnit("");
            chatBean.setIsRemainderRequired("");
            String Signal_id = Utility.getSessionID();
            chatBean.setSignalid(Signal_id);

            chatBean.setTaskRequestType("");
            if (completedPercentage != null && !completedPercentage.equalsIgnoreCase("")) {
                chatBean.setCompletedPercentage(completedPercentage);
            } else {
                chatBean.setCompletedPercentage("0");
            }
            chatBean.setMimeType("text");
            chatBean.setTaskPriority("Medium");
            chatBean.setDateFrequency("");
            chatBean.setTimeFrequency("");
            chatBean.setShow_progress(1);
            chatBean.setRead_status(0);
            chatBean.setReminderQuote("");
            chatBean.setRemark("");
            chatBean.setTaskUTCTime(taskUTCtime);
            chatBean.setTaskObservers("");
            chatBean.setServerFileName("");
            chatBean.setMsg_status(1);
            chatBean.setRequestStatus("");
            chatBean.setGroupTaskMembers(TaskMemberList);
            chatBean.setTaskUTCDateTime(dateforrow);
            chatBean.setDateTime(dateTime);
            chatBean.setSubType("normal");
            chatBean.setDaysOfTheWeek("");
            chatBean.setRepeatFrequency("");
            if (taskbean.getProjectId() != null && !taskbean.getProjectId().equalsIgnoreCase("")) {
                chatBean.setProjectId(taskbean.getProjectId());
            }
            Log.i("custom project", "chatBean.getProjectId() " + chatBean.getProjectId());
            chatBean.setTaskTagName("");
            chatBean.setCustomTagId(0);
            chatBean.setCustomTagVisible(true);
            chatBean.setCustomSetId(0);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(2);
                nameValuePairs.add(new BasicNameValuePair("fromId",String.valueOf(Appreference.loginuserdetails.getId())));
                nameValuePairs.add(new BasicNameValuePair("taskId",taskId));
                Log.i("json", nameValuePairs.toString());

                if (Appreference.jsonRequestSender == null) {
                    JsonRequestSender jsonRequestParser = new JsonRequestSender();
                    Appreference.jsonRequestSender = jsonRequestParser;
                    jsonRequestParser.start();
                }
                Appreference.jsonRequestSender.reactivateStatus(EnumJsonWebservicename.reactivateStatus, nameValuePairs, AllTaskList.this, chatBean);
            } catch (Exception e) {
                e.printStackTrace();
            }

            /*JSONObject jsonObject = new JSONObject();
            try {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("id", Integer.parseInt(chatBean.getTaskId()));
                jsonObject.put("task", jsonObject1);
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("id", Appreference.loginuserdetails.getId());
                jsonObject.put("from", jsonObject2);
                JSONObject jsonObject3 = new JSONObject();
                int toUser_id = 0;
                if ((chatBean.getProjectId() != null && !chatBean.getProjectId().equalsIgnoreCase("") && !chatBean.getProjectId().equalsIgnoreCase("null")) || chatBean.getTaskType().equalsIgnoreCase("group")) {

                } else {
//                    String task_receiver_1 = dataBase.getProjectParentTaskId("select taskReceiver from taskHistoryInfo where taskId='" + chatBean.getTaskId() + "' order by id LIMIT 1");
//                    Log.i("TaskHistory", "Abandoned toUser_id " + task_receiver_1);
//                    int toUser_id = dataBase.getUserid(task_receiver_1);
//                    jsonObject3.put("id", String.valueOf(toUser_id));
//                    jsonObject.put("to", jsonObject3);
//                    JSONObject jsonObject3 = new JSONObject();
                    if (taskbean.getTaskType() != null && taskbean.getTaskType().equalsIgnoreCase("Group")) {
                        jsonObject3.put("id", Integer.parseInt(taskbean.getToUserId()));
                        jsonObject.put("group", jsonObject3);
                    } else {
                        String task_receiver_1 = dataBase.getProjectParentTaskId("select taskReceiver from taskHistoryInfo where taskId='" + taskbean.getTaskId() + "' order by id LIMIT 1");
                        Log.i("TaskHistory", "Abandoned toUser_id " + task_receiver_1);
                        toUser_id = dataBase.getUserid(task_receiver_1);
                        jsonObject3.put("id", String.valueOf(toUser_id));
                        jsonObject.put("to", jsonObject3);
                    }
                }
                jsonObject.put("signalId", chatBean.getSignalid());
                jsonObject.put("parentId", chatBean.getParentId());
                jsonObject.put("createdDate", chatBean.getDateTime());
                if (chatBean.getCompletedPercentage() != null && !chatBean.getCompletedPercentage().equalsIgnoreCase("") && !chatBean.getCompletedPercentage().equalsIgnoreCase(null)) {
                    jsonObject.put("percentageCompleted", chatBean.getCompletedPercentage());
                } else {
                    jsonObject.put("percentageCompleted", "0");
                }
                jsonObject.put("requestType", "percentageCompleted");
                jsonObject.put("taskStatus", "inprogress");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("TaskHistory", "task status " + chatBean.getTaskStatus());
            Log.i("jsonrequest", jsonObject.toString());
            Appreference.jsonRequestSender.taskConversationEntry(EnumJsonWebservicename.taskConversationEntry, jsonObject, AllTaskList.this, null, chatBean);*/
        }
    }
    public void Pause_Task(String taskid, String completedPercentage) {

        ArrayList<TaskDetailsBean> taskDetail;
        String TaskId = taskDetailsBean.getTaskId();
        String TaskMemberList;
        taskDetail = dataBase.getTaskHistoryInfo("Select * from taskHistoryInfo where taskId ='" + TaskId + "'");
        if (taskDetail.size() > 0) {
            TaskDetailsBean taskbean = taskDetail.get(0);
            TaskDetailsBean chatBean = new TaskDetailsBean();
            String tasktime, taskUTCtime;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = dateFormat.format(new Date());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateforrow = dateFormat.format(new Date());
            tasktime = dateTime;
            tasktime = tasktime.split(" ")[1];
            Log.i("task", "tasktime" + tasktime);
            Log.i("UTC", "sendMessage utc time" + dateforrow);
            Log.i("time", "value");
            taskUTCtime = dateforrow;
            Log.i("Accept", "value 5 " + chatBean.getTaskStatus());
            chatBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
            chatBean.setFromUserName(Appreference.loginuserdetails.getUsername());
            if (taskbean.getTaskType().equalsIgnoreCase("Group")) {
                TaskMemberList = dataBase.getProjectParentTaskId("select taskMemberList from taskHistoryInfo where taskId='" + taskid + "'");
            } else {
                TaskMemberList = dataBase.getProjectParentTaskId("select taskReceiver from taskHistoryInfo where taskId='" + taskid + "'");
            }
            if (!taskbean.getTaskType().equalsIgnoreCase("Group")) {
                chatBean.setToUserId(taskbean.getToUserId());
                chatBean.setTaskReceiver(TaskMemberList);
                chatBean.setToUserName(taskbean.getToUserName());
            } else {
                chatBean.setToUserId(taskbean.getToUserId());
                chatBean.setTaskReceiver(TaskMemberList);
                chatBean.setToUserName(taskbean.getToUserName());
            }
            chatBean.setTaskType(taskbean.getTaskType());
            chatBean.setTaskId(taskid);
            chatBean.setTaskStatus("pause");
            Log.i("DataBase", " Bean Value Creating Bean -----> " + tasktime);
            chatBean.setTasktime(tasktime);
            chatBean.setTaskName(taskbean.getTaskName());
            chatBean.setOwnerOfTask(Appreference.loginuserdetails.getUsername());
            chatBean.setCustomTagVisible(true);
            chatBean.setTaskNo(taskbean.getTaskNo());
            chatBean.setCatagory(taskbean.getCatagory());
            if (taskbean.getCatagory() != null && taskbean.getCatagory().equalsIgnoreCase("issue")) {
                chatBean.setIssueId(taskbean.getIssueId());
                chatBean.setTaskDescription("This issue is paused");  // contains ToUserName's
            } else {
                chatBean.setTaskDescription("This task is paused");  // contains ToUserName's
            }
            chatBean.setSendStatus("0");
            chatBean.setUtcPlannedStartDateTime("");
            chatBean.setUtcPlannedStartDateTime("");
            chatBean.setUtcplannedEndDateTime("");
            chatBean.setUtcPemainderFrequency("");
            chatBean.setDuration("");
            chatBean.setDurationUnit("");
            chatBean.setIsRemainderRequired("");
            String Signal_id = Utility.getSessionID();
            chatBean.setSignalid(Signal_id);
            chatBean.setTaskRequestType("percentageCompleted");
            if (completedPercentage != null && !completedPercentage.equalsIgnoreCase("")) {
                chatBean.setCompletedPercentage(completedPercentage);
            } else {
                chatBean.setCompletedPercentage("0");
            }
            chatBean.setMimeType("text");
            chatBean.setTaskPriority("Medium");
            chatBean.setDateFrequency("");
            chatBean.setTimeFrequency("");
            chatBean.setShow_progress(1);
            chatBean.setRead_status(0);
            chatBean.setReminderQuote("");
            chatBean.setRemark("");
            chatBean.setTaskUTCTime(taskUTCtime);
            chatBean.setTaskObservers("");
            chatBean.setServerFileName("");
            chatBean.setMsg_status(1);
            chatBean.setRequestStatus("");
            chatBean.setGroupTaskMembers(TaskMemberList);
            chatBean.setTaskUTCDateTime(dateforrow);
            chatBean.setDateTime(dateTime);
            chatBean.setSubType("normal");
            chatBean.setDaysOfTheWeek("");
            chatBean.setRepeatFrequency("");
            if (taskbean.getProjectId() != null && !taskbean.getProjectId().equalsIgnoreCase("")) {
                chatBean.setProjectId(taskbean.getProjectId());
            }
            Log.i("custom project", "chatBean.getProjectId() " + chatBean.getProjectId());
            chatBean.setTaskTagName("");
            chatBean.setCustomTagId(0);
            chatBean.setCustomTagVisible(true);
            chatBean.setCustomSetId(0);
            JSONObject jsonObject = new JSONObject();
            Log.i("task", "taskid " + chatBean.getTaskId());
            try {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("id", Integer.parseInt(taskDetailsBean.getTaskId()));
                jsonObject.put("task", jsonObject1);
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("id", Appreference.loginuserdetails.getId());
                jsonObject.put("from", jsonObject2);
                JSONObject jsonObject3 = new JSONObject();
                int toUser_id;
                if ((chatBean.getProjectId() != null && !chatBean.getProjectId().equalsIgnoreCase("") && !chatBean.getProjectId().equalsIgnoreCase("null")) || chatBean.getTaskType().equalsIgnoreCase("group")) {
                    Log.i("AllTaskList","projectid");
                } else {
//                    String task_receiver_1 = dataBase.getProjectParentTaskId("select taskReceiver from taskHistoryInfo where taskId='" + chatBean.getTaskId() + "' order by id LIMIT 1");
//                    Log.i("TaskHistory", "Abandoned toUser_id " + task_receiver_1);
//                    int toUser_id = dataBase.getUserid(task_receiver_1);
//                    jsonObject3.put("id", String.valueOf(toUser_id));
//                    jsonObject.put("to", jsonObject3);
//                    JSONObject jsonObject3 = new JSONObject();
                    if (taskbean.getTaskType() != null && taskbean.getTaskType().equalsIgnoreCase("Group")) {
                        jsonObject3.put("id", Integer.parseInt(taskbean.getToUserId()));
                        jsonObject.put("group", jsonObject3);
                    } else {
                        String task_receiver_1 = dataBase.getProjectParentTaskId("select taskReceiver from taskHistoryInfo where taskId='" + taskbean.getTaskId() + "' order by id LIMIT 1");
                        Log.i("TaskHistory", "Abandoned toUser_id " + task_receiver_1);
                        toUser_id = dataBase.getUserid(task_receiver_1);
                       /* if (me_task != null && me_task.equalsIgnoreCase("note")) {
                            jsonObject3.put("id", String.valueOf(Appreference.loginuserdetails.getId()));
                        } else {*/
                        jsonObject3.put("id", String.valueOf(toUser_id));
//                        }
                        jsonObject.put("to", jsonObject3);
                    }
                }
                jsonObject.put("signalId", chatBean.getSignalid());
                jsonObject.put("parentId", chatBean.getParentId());
                jsonObject.put("createdDate", chatBean.getDateTime());
                if (chatBean.getCompletedPercentage() != null && !chatBean.getCompletedPercentage().equalsIgnoreCase("") && !chatBean.getCompletedPercentage().equalsIgnoreCase(null)) {
                    jsonObject.put("percentageCompleted", chatBean.getCompletedPercentage());
                } else {
                    jsonObject.put("percentageCompleted", "0");
                }
                jsonObject.put("requestType", "percentageCompleted");
                jsonObject.put("taskStatus", "pause");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("beforewebcall", chatBean.getTaskStatus());
            Log.i("jsonrequest", jsonObject.toString());
            Appreference.jsonRequestSender.taskConversationEntry(EnumJsonWebservicename.taskConversationEntry, jsonObject, AllTaskList.this, null, chatBean);
        }
    }
    public void Resume_Task(String taskId, String completedPercentage) {
        ArrayList<TaskDetailsBean> taskDetail;
        String TaskId = taskDetailsBean.getTaskId();
        String TaskMemberList;
        taskDetail = dataBase.getTaskHistoryInfo("Select * from taskHistoryInfo where taskId ='" + TaskId + "'");
        if (taskDetail.size() > 0) {
            TaskDetailsBean taskbean = taskDetail.get(0);
            TaskDetailsBean chatBean = new TaskDetailsBean();
            String tasktime, taskUTCtime;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = dateFormat.format(new Date());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateforrow = dateFormat.format(new Date());
            tasktime = dateTime;
            tasktime = tasktime.split(" ")[1];
            Log.i("task", "tasktime" + tasktime);
            Log.i("UTC", "sendMessage utc time" + dateforrow);
            Log.i("time", "value");
            taskUTCtime = dateforrow;
            Log.i("Accept", "value 5 " + chatBean.getTaskStatus());
            chatBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
            chatBean.setFromUserName(Appreference.loginuserdetails.getUsername());
            if (taskbean.getTaskType().equalsIgnoreCase("Group")) {
                TaskMemberList = dataBase.getProjectParentTaskId("select taskMemberList from taskHistoryInfo where taskId='" + taskId + "'");
            } else {
                TaskMemberList = dataBase.getProjectParentTaskId("select taskReceiver from taskHistoryInfo where taskId='" + taskId + "'");
            }
            if (!taskbean.getTaskType().equalsIgnoreCase("Group")) {
                chatBean.setToUserId(taskbean.getToUserId());
                chatBean.setTaskReceiver(TaskMemberList);
                chatBean.setToUserName(taskbean.getToUserName());
            } else {
                chatBean.setToUserId(taskbean.getToUserId());
                chatBean.setTaskReceiver(TaskMemberList);
                chatBean.setToUserName(taskbean.getToUserName());
            }
            chatBean.setTaskType(taskbean.getTaskType());
            chatBean.setTaskId(taskId);
            chatBean.setTaskStatus(taskbean.getTaskStatus());
            Log.i("DataBase", " Bean Value Creating Bean -----> " + tasktime);
            chatBean.setTasktime(tasktime);
            chatBean.setTaskName(taskbean.getTaskName());
            chatBean.setOwnerOfTask(Appreference.loginuserdetails.getUsername());
            chatBean.setCustomTagVisible(true);
            chatBean.setTaskNo(taskbean.getTaskNo());
            chatBean.setCatagory(taskbean.getCatagory());
            if (taskbean.getCatagory() != null && taskbean.getCatagory().equalsIgnoreCase("issue")) {
                chatBean.setIssueId(taskbean.getIssueId());
                chatBean.setTaskDescription("This issue is resumed"); // contains ToUserName's
            } else {
                chatBean.setTaskDescription("This task is resumed"); // contains ToUserName's
            }
            chatBean.setSendStatus("0");
            chatBean.setUtcPlannedStartDateTime("");
            chatBean.setUtcPlannedStartDateTime("");
            chatBean.setUtcplannedEndDateTime("");
            chatBean.setUtcPemainderFrequency("");
            chatBean.setDuration("");
            chatBean.setDurationUnit("");
            chatBean.setIsRemainderRequired("");
            String Signal_id = Utility.getSessionID();
            chatBean.setSignalid(Signal_id);
            chatBean.setTaskRequestType("");
            if (completedPercentage != null && !completedPercentage.equalsIgnoreCase("")) {
                chatBean.setCompletedPercentage(completedPercentage);
            } else {
                chatBean.setCompletedPercentage("0");
            }
            chatBean.setMimeType("text");
            chatBean.setTaskPriority("Medium");
            chatBean.setDateFrequency("");
            chatBean.setTimeFrequency("");
            chatBean.setShow_progress(1);
            chatBean.setRead_status(0);
            chatBean.setReminderQuote("");
            chatBean.setRemark("");
            chatBean.setTaskUTCTime(taskUTCtime);
            chatBean.setTaskObservers("");
            chatBean.setServerFileName("");
            chatBean.setMsg_status(1);
            chatBean.setRequestStatus("");
            chatBean.setGroupTaskMembers(TaskMemberList);
            chatBean.setTaskUTCDateTime(dateforrow);
            chatBean.setDateTime(dateTime);
            chatBean.setSubType("normal");
            chatBean.setDaysOfTheWeek("");
            chatBean.setRepeatFrequency("");
            if (taskbean.getProjectId() != null && !taskbean.getProjectId().equalsIgnoreCase("")) {
                chatBean.setProjectId(taskbean.getProjectId());
            }
            Log.i("custom project", "chatBean.getProjectId() " + chatBean.getProjectId());
            chatBean.setTaskTagName("");
            chatBean.setCustomTagId(0);
            chatBean.setCustomTagVisible(true);
            chatBean.setCustomSetId(0);

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<>(2);
                nameValuePairs.add(new BasicNameValuePair("fromId", String.valueOf(Appreference.loginuserdetails.getId())));
                nameValuePairs.add(new BasicNameValuePair("taskId", taskId));
                Log.i("json", nameValuePairs.toString());

                if (Appreference.jsonRequestSender == null) {
                    JsonRequestSender jsonRequestParser = new JsonRequestSender();
                    Appreference.jsonRequestSender = jsonRequestParser;
                    jsonRequestParser.start();
                }
                Appreference.jsonRequestSender.reactivateStatus(EnumJsonWebservicename.reactivateStatus, nameValuePairs, AllTaskList.this, chatBean);
            } catch (Exception e) {
                e.printStackTrace();
            }


            /*JSONObject jsonObject = new JSONObject();
            try {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("id", Integer.parseInt(chatBean.getTaskId()));
                jsonObject.put("task", jsonObject1);
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("id", Appreference.loginuserdetails.getId());
                jsonObject.put("from", jsonObject2);
                JSONObject jsonObject3 = new JSONObject();
                int toUser_id = 0;
                if ((chatBean.getProjectId() != null && !chatBean.getProjectId().equalsIgnoreCase("") && !chatBean.getProjectId().equalsIgnoreCase("null")) || chatBean.getTaskType().equalsIgnoreCase("group")) {

                } else {
//                    String task_receiver_1 = dataBase.getProjectParentTaskId("select taskReceiver from taskHistoryInfo where taskId='" + chatBean.getTaskId() + "' order by id LIMIT 1");
//                    Log.i("TaskHistory", "Abandoned toUser_id " + task_receiver_1);
//                    int toUser_id = dataBase.getUserid(task_receiver_1);
//                    jsonObject3.put("id", String.valueOf(toUser_id));
//                    jsonObject.put("to", jsonObject3);
//                    JSONObject jsonObject3 = new JSONObject();
                    if (taskbean.getTaskType() != null && taskbean.getTaskType().equalsIgnoreCase("Group")) {
                        jsonObject3.put("id", Integer.parseInt(taskbean.getToUserId()));
                        jsonObject.put("group", jsonObject3);
                    } else {
                        String task_receiver_1 = dataBase.getProjectParentTaskId("select taskReceiver from taskHistoryInfo where taskId='" + taskbean.getTaskId() + "' order by id LIMIT 1");
                        Log.i("TaskHistory", "Abandoned toUser_id " + task_receiver_1);
                        toUser_id = dataBase.getUserid(task_receiver_1);
                        jsonObject3.put("id", String.valueOf(toUser_id));
                        jsonObject.put("to", jsonObject3);
                    }
                }
                jsonObject.put("signalId", chatBean.getSignalid());
                jsonObject.put("parentId", chatBean.getParentId());
                jsonObject.put("createdDate", chatBean.getDateTime());
                if (chatBean.getCompletedPercentage() != null && !chatBean.getCompletedPercentage().equalsIgnoreCase("") && !chatBean.getCompletedPercentage().equalsIgnoreCase(null)) {
                    jsonObject.put("percentageCompleted", chatBean.getCompletedPercentage());
                } else {
                    jsonObject.put("percentageCompleted", "0");
                }
                jsonObject.put("requestType", "percentageCompleted");
                jsonObject.put("taskStatus", "inprogress");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("TaskHistory", "task status " + chatBean.getTaskStatus());
//            Log.i("jsonrequest", jsonObject.toString());
            Appreference.jsonRequestSender.taskConversationEntry(EnumJsonWebservicename.taskConversationEntry, jsonObject, TaskHistory.this, null, chatBean);*/
        }
    }

    public void Abanded_Task(String taskid, String completedPercentage) {

        ArrayList<TaskDetailsBean> taskDetail;
        String TaskId = taskDetailsBean.getTaskId();
        String TaskMemberList;
        taskDetail = dataBase.getTaskHistoryInfo("Select * from taskHistoryInfo where taskId ='" + TaskId + "'");
        if (taskDetail.size() > 0) {
            TaskDetailsBean taskbean = taskDetail.get(0);
            TaskDetailsBean chatBean = new TaskDetailsBean();
            String tasktime, taskUTCtime;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateTime = dateFormat.format(new Date());
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateforrow = dateFormat.format(new Date());
            tasktime = dateTime;
            tasktime = tasktime.split(" ")[1];
            Log.i("task", "tasktime" + tasktime);
            Log.i("UTC", "sendMessage utc time" + dateforrow);
            Log.i("time", "value");
            taskUTCtime = dateforrow;
            Log.i("Accept", "value 5 " + chatBean.getTaskStatus());
            chatBean.setFromUserId(String.valueOf(Appreference.loginuserdetails.getId()));
            chatBean.setFromUserName(Appreference.loginuserdetails.getUsername());
            if (taskbean.getTaskType().equalsIgnoreCase("Group")) {
                TaskMemberList = dataBase.getProjectParentTaskId("select taskMemberList from taskHistoryInfo where taskId='" + taskid + "'");
            } else {
                TaskMemberList = dataBase.getProjectParentTaskId("select taskReceiver from taskHistoryInfo where taskId='" + taskid + "'");
            }
            if (!taskbean.getTaskType().equalsIgnoreCase("Group")) {
                chatBean.setToUserId(taskbean.getToUserId());
                chatBean.setTaskReceiver(TaskMemberList);
                chatBean.setToUserName(taskbean.getToUserName());
            } else {
                chatBean.setToUserId(taskbean.getToUserId());
                chatBean.setTaskReceiver(TaskMemberList);
                chatBean.setToUserName(taskbean.getToUserName());
            }
            chatBean.setTaskType(taskbean.getTaskType());
            chatBean.setTaskId(taskid);
            chatBean.setTaskStatus("abandoned");
            Log.i("DataBase", " Bean Value Creating Bean -----> " + tasktime);
            chatBean.setTasktime(tasktime);
            chatBean.setTaskName(taskbean.getTaskName());
            chatBean.setOwnerOfTask(Appreference.loginuserdetails.getUsername());
            chatBean.setCustomTagVisible(true);
            chatBean.setTaskNo(taskbean.getTaskNo());
            chatBean.setCatagory(taskbean.getCatagory());
            if (taskbean.getCatagory() != null && taskbean.getCatagory().equalsIgnoreCase("issue")) {
                chatBean.setIssueId(taskbean.getIssueId());
                chatBean.setTaskDescription("This issue is abandoned");  // contains ToUserName's
            }else{
                chatBean.setTaskDescription("This task is abandoned");  // contains ToUserName's
            }
            chatBean.setSendStatus("0");
            chatBean.setUtcPlannedStartDateTime("");
            chatBean.setUtcPlannedStartDateTime("");
            chatBean.setUtcplannedEndDateTime("");
            chatBean.setUtcPemainderFrequency("");
            chatBean.setDuration("");
            chatBean.setDurationUnit("");
            chatBean.setIsRemainderRequired("");
            String Signal_id = Utility.getSessionID();
            chatBean.setSignalid(Signal_id);
            chatBean.setTaskRequestType("percentageCompleted");
            if (completedPercentage != null && !completedPercentage.equalsIgnoreCase("")) {
                chatBean.setCompletedPercentage(completedPercentage);
            } else {
                chatBean.setCompletedPercentage("0");
            }
            chatBean.setMimeType("text");
            chatBean.setTaskPriority("Medium");
            chatBean.setDateFrequency("");
            chatBean.setTimeFrequency("");
            chatBean.setShow_progress(1);
            chatBean.setRead_status(0);
            chatBean.setReminderQuote("");
            chatBean.setRemark("");
            chatBean.setTaskUTCTime(taskUTCtime);
            chatBean.setTaskObservers("");
            chatBean.setServerFileName("");
            chatBean.setMsg_status(1);
            chatBean.setRequestStatus("");
            chatBean.setGroupTaskMembers(TaskMemberList);
            chatBean.setTaskUTCDateTime(dateforrow);
            chatBean.setDateTime(dateTime);
            chatBean.setSubType("normal");
            chatBean.setDaysOfTheWeek("");
            chatBean.setRepeatFrequency("");
            if (taskbean.getProjectId() != null && !taskbean.getProjectId().equalsIgnoreCase("")) {
                chatBean.setProjectId(taskbean.getProjectId());
            }
            Log.i("custom project", "chatBean.getProjectId() " + chatBean.getProjectId());
            chatBean.setTaskTagName("");
            chatBean.setCustomTagId(0);
            chatBean.setCustomTagVisible(true);
            chatBean.setCustomSetId(0);
            JSONObject jsonObject = new JSONObject();
            Log.i("task", "taskid " + chatBean.getTaskId());
            try {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("id", Integer.parseInt(taskDetailsBean.getTaskId()));
                jsonObject.put("task", jsonObject1);
                JSONObject jsonObject2 = new JSONObject();
                jsonObject2.put("id", Appreference.loginuserdetails.getId());
                jsonObject.put("from", jsonObject2);
                JSONObject jsonObject3 = new JSONObject();
                int toUser_id;
                if ((chatBean.getProjectId() != null && !chatBean.getProjectId().equalsIgnoreCase("") && !chatBean.getProjectId().equalsIgnoreCase("null")) || chatBean.getTaskType().equalsIgnoreCase("group")) {
                    Log.i("AllTaskList","projectid");
                } else {
//                    String task_receiver_1 = dataBase.getProjectParentTaskId("select taskReceiver from taskHistoryInfo where taskId='" + chatBean.getTaskId() + "' order by id LIMIT 1");
//                    Log.i("TaskHistory", "Abandoned toUser_id " + task_receiver_1);
//                    int toUser_id = dataBase.getUserid(task_receiver_1);
//                    jsonObject3.put("id", String.valueOf(toUser_id));
//                    jsonObject.put("to", jsonObject3);
//                    JSONObject jsonObject3 = new JSONObject();
                    if (taskbean.getTaskType() != null && taskbean.getTaskType().equalsIgnoreCase("Group")) {
                        jsonObject3.put("id", Integer.parseInt(taskbean.getToUserId()));
                        jsonObject.put("group", jsonObject3);
                    } else {
                        String task_receiver_1 = dataBase.getProjectParentTaskId("select taskReceiver from taskHistoryInfo where taskId='" + taskbean.getTaskId() + "' order by id LIMIT 1");
                        Log.i("TaskHistory", "Abandoned toUser_id " + task_receiver_1);
                        toUser_id = dataBase.getUserid(task_receiver_1);
                        jsonObject3.put("id", String.valueOf(toUser_id));
                        jsonObject.put("to", jsonObject3);
                    }
                }
                jsonObject.put("signalId", chatBean.getSignalid());
                jsonObject.put("parentId", chatBean.getParentId());
                jsonObject.put("createdDate", chatBean.getDateTime());
                if (chatBean.getCompletedPercentage() != null && !chatBean.getCompletedPercentage().equalsIgnoreCase("") && !chatBean.getCompletedPercentage().equalsIgnoreCase(null)) {
                    jsonObject.put("percentageCompleted", chatBean.getCompletedPercentage());
                } else {
                    jsonObject.put("percentageCompleted", "0");
                }
                jsonObject.put("requestType", "percentageCompleted");
                jsonObject.put("taskStatus", "abandoned");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.i("beforewebcall", chatBean.getTaskStatus());
            Log.i("jsonrequest", jsonObject.toString());
            Appreference.jsonRequestSender.taskConversationEntry(EnumJsonWebservicename.taskConversationEntry, jsonObject, AllTaskList.this, null, chatBean);
        }
    }

    public String composeChatXML(TaskDetailsBean cmbean) {
        String TaskMemberList = dataBase.getProjectParentTaskId("select taskMemberList from projectHistory where taskId='" + cmbean.getTaskId() + "'");
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

//            if (cmbean.getTaskObservers() != null) {
//                buffer.append(" taskAddObservers=" + quotes + cmbean.getTaskObservers() + quotes);
//            }
//
//            if (cmbean.getTaskRemoveObservers() != null) {
//                buffer.append(" taskRemoveObservers=" + quotes + cmbean.getTaskRemoveObservers() + quotes);
//            }

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
                    buffer.append(" taskName=").append(quotes).append(taskName).append(quotes);
                    Log.i("URL", "value " + taskName);
                } else {
                    buffer.append(" taskName=").append(quotes).append(cmbean.getTaskName()).append(quotes);
                }
//                buffer.append(" taskName=" + quotes + cmbean.getTaskName() + quotes);
            } else {
                buffer.append(" taskName=").append(quotes).append("New Task").append(quotes);
            }
            if (!cmbean.getMimeType().equalsIgnoreCase("text") && !cmbean.getMimeType().equalsIgnoreCase("url")) {
                if ((cmbean.getServerFileName() != null) && (!cmbean.getServerFileName().equalsIgnoreCase(null)) && (!cmbean.getServerFileName().equalsIgnoreCase(""))) {
                    Log.i("URL", "SaveFilename-->" + (base_64 != null ? base_64.trim() : null));
                    buffer.append(" taskDescription=").append(quotes).append(base_64.trim()).append(quotes);
                } else {
                    Log.i("URL", "TaskDes-->" + base64);
                    buffer.append(" taskDescription=").append(quotes).append(base64.trim()).append(quotes);
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
                        buffer.append(" taskDescription=").append(quotes).append(s).append(quotes);
                        Log.i("URL", "value " + s);
                    } else {
                        buffer.append(" taskDescription=").append(quotes).append(base64.trim()).append(quotes);
                    }
                }
            }
            buffer.append(" fromUserId=").append(quotes).append(cmbean.getFromUserId()).append(quotes);
            buffer.append(" fromUserName=").append(quotes).append(cmbean.getFromUserName()).append(quotes);
            if (!getResources().getString(R.string.proxyua).equalsIgnoreCase("enable")) {
                buffer.append(" toUserId=").append(quotes).append(cmbean.getToUserId()).append(quotes);
                buffer.append(" toUserName=").append(quotes).append(cmbean.getToUserName()).append(quotes);
            } else {
                if (cmbean.getProjectId() != null && !cmbean.getProjectId().equalsIgnoreCase("")) {
                    buffer.append(" toUserId=").append(quotes).append("").append(quotes);
                    if (cmbean.getTaskType() != null && !cmbean.getTaskType().equalsIgnoreCase("Group")) {
                        buffer.append(" toUserName=").append(quotes).append(TaskMemberList).append(quotes);
                    } else {
                        buffer.append(" toUserName=").append(quotes).append("").append(quotes);
                    }
                } else {
                    buffer.append(" toUserId=").append(quotes).append(cmbean.getToUserId()).append(quotes);
                    buffer.append(" toUserName=").append(quotes).append(cmbean.getToUserName()).append(quotes);
                }
            }
            buffer.append(" taskNo=").append(quotes).append(cmbean.getTaskNo()).append(quotes);
            buffer.append(" taskId=").append(quotes).append(cmbean.getTaskId()).append(quotes);
            buffer.append(" taskType=").append(quotes).append(cmbean.getTaskType()).append(quotes);
            if (cmbean.getUtcPlannedStartDateTime() != null) {
                buffer.append(" plannedStartDateTime=").append(quotes).append(cmbean.getUtcPlannedStartDateTime()).append(quotes);
            } else {
                buffer.append(" plannedStartDateTime=").append(quotes).append("").append(quotes);
            }
            if (cmbean.getUtcplannedEndDateTime() != null) {
                buffer.append(" plannedEndDateTime=").append(quotes).append(cmbean.getUtcplannedEndDateTime()).append(quotes);
            } else {
                buffer.append(" plannedEndDateTime=").append(quotes).append("").append(quotes);
            }
            if (cmbean.getIsRemainderRequired() != null) {
                buffer.append(" isRemainderRequired=").append(quotes).append(cmbean.getIsRemainderRequired()).append(quotes);
            } else {
                buffer.append(" isRemainderRequired=").append(quotes).append("").append(quotes);
            }
            Log.i("newtaskconversation", "remainderDateTime " + cmbean.getUtcPemainderFrequency());
            if (cmbean.getUtcPemainderFrequency() == null || (cmbean.getUtcPemainderFrequency() != null && cmbean.getUtcPemainderFrequency().equalsIgnoreCase(""))) {
                cmbean.setUtcPemainderFrequency("");
            }
            buffer.append(" remainderDateTime=").append(quotes).append(cmbean.getUtcPemainderFrequency()).append(quotes);
            if (cmbean.getCompletedPercentage() != null && !cmbean.getCompletedPercentage().equalsIgnoreCase("") && Integer.parseInt(cmbean.getCompletedPercentage()) == 100) {
                if (cmbean.getTaskStatus() != null && cmbean.getTaskStatus().equalsIgnoreCase("Closed")) {
                    Log.i("newtaskconversation", " compose taskStatus 1 " + cmbean.getTaskStatus());
                    buffer.append(" taskStatus=").append(quotes).append("Closed").append(quotes);
                }else if (cmbean.getTaskStatus() != null && cmbean.getTaskStatus().equalsIgnoreCase("pause")) {
                    Log.i("newtaskconversation", " compose taskStatus 2 " + cmbean.getTaskStatus());
                    buffer.append(" taskStatus=").append(quotes).append("pause").append(quotes);
                }else if (cmbean.getTaskStatus() != null && cmbean.getTaskStatus().equalsIgnoreCase("abandoned")) {
                    Log.i("newtaskconversation", " compose taskStatus 3 " + cmbean.getTaskStatus());
                    buffer.append(" taskStatus=").append(quotes).append("abandoned").append(quotes);
                }else {
                    Log.i("newtaskconversation", " compose taskStatus 2 " + cmbean.getTaskStatus());
//                    if (cmbean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
//                        buffer.append(" taskStatus=" + quotes + "Closed" + quotes);
//                    } else {
//                        buffer.append(" taskStatus=" + quotes + "Completed" + quotes);
//                    }
//                    buffer.append(" taskStatus=" + quotes + "Completed" + quotes);
                    Log.i("newtaskconversation", " compose taskStatus 4 " + cmbean.getTaskStatus());
                    buffer.append(" taskStatus=").append(quotes).append(cmbean.getTaskStatus()).append(quotes);
                }
            } else {
                Log.i("newtaskconversation", " compose taskStatus 5 " + cmbean.getTaskStatus());
                Log.i("Accept", "value taskStatus after compose " + cmbean.getTaskStatus());
                buffer.append(" taskStatus=").append(quotes).append(cmbean.getTaskStatus()).append(quotes);
            }
            buffer.append(" signalid=").append(quotes).append(cmbean.getSignalid()).append(quotes);
            buffer.append(" parentId=").append(quotes).append(cmbean.getParentId()).append(quotes);
            if (cmbean.getTaskRequestType() != null) {
                buffer.append(" taskRequestType=").append(quotes).append(cmbean.getTaskRequestType()).append(quotes);
            } else {
                buffer.append(" taskRequestType=").append(quotes).append("").append(quotes);
            }
            if (cmbean.getDateFrequency() != null) {
                buffer.append(" dateFrequency=").append(quotes).append(cmbean.getDateFrequency()).append(quotes);
            } else {
                buffer.append(" dateFrequency=").append(quotes).append("").append(quotes);
            }
            String TimeFrequency;
            if (cmbean.getFromUserName() != null && cmbean.getFromUserName().equalsIgnoreCase(cmbean.getOwnerOfTask())) {
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
            }
            if (TimeFrequency != null) {
                buffer.append(" timeFrequency=").append(quotes).append(TimeFrequency).append(quotes);
            } else {
                buffer.append(" timeFrequency=").append(quotes).append("").append(quotes);
            }
            buffer.append(" taskOwner=").append(quotes).append(cmbean.getOwnerOfTask()).append(quotes);
            buffer.append(" mimeType=").append(quotes).append(cmbean.getMimeType()).append(quotes);
            buffer.append(" dateTime=").append(quotes).append(cmbean.getTaskUTCDateTime()).append(quotes);
            if (cmbean.getTaskPriority() != null) {
                buffer.append(" taskPriority=").append(quotes).append(cmbean.getTaskPriority()).append(quotes);
            } else {
                buffer.append(" taskPriority=").append(quotes).append("").append(quotes);
            }
            if (cmbean.getCompletedPercentage() != null) {
                buffer.append(" completedPercentage=").append(quotes).append(cmbean.getCompletedPercentage()).append(quotes);
            } else {
                buffer.append(" completedPercentage=").append(quotes).append("").append(quotes);
            }
            if (cmbean.getReminderQuote() != null) {
                buffer.append(" remainderQuotes=").append(quotes).append(cmbean.getReminderQuote()).append(quotes);
            } else {
                buffer.append(" remainderQuotes=").append(quotes).append("").append(quotes);
            }
            if (cmbean.getRemark() != null) {
                buffer.append(" remark=").append(quotes).append(cmbean.getRemark()).append(quotes);
            } else {
                buffer.append(" remark=").append(quotes).append("").append(quotes);
            }
            if (cmbean.getProjectId() != null && !cmbean.getProjectId().equalsIgnoreCase("")) {
                if (TaskMemberList != null && !TaskMemberList.contains(",")) {
                    buffer.append(" taskReceiver=").append(quotes).append(TaskMemberList).append(quotes);
                } else {
                    buffer.append(" taskReceiver=").append(quotes).append(cmbean.getTaskReceiver()).append(quotes);
                }
            } else {
                buffer.append(" taskReceiver=").append(quotes).append(cmbean.getTaskReceiver()).append(quotes);
            }
            if (cmbean.getProjectId() != null && !cmbean.getProjectId().equalsIgnoreCase("")) {
                buffer.append(" taskToUsersList=").append(quotes).append(TaskMemberList).append(quotes);
            } else {
                buffer.append(" taskToUsersList=").append(quotes).append(cmbean.getGroupTaskMembers()).append(quotes);
            }
            if (cmbean.getRequestStatus() != null) {
                buffer.append(" requestStatus=").append(quotes).append(cmbean.getRequestStatus()).append(quotes);
            } else {
                buffer.append(" requestStatus=").append(quotes).append("").append(quotes);
            }
            if (cmbean.getSubType() != null) {
                buffer.append(" subType=").append(quotes).append(cmbean.getSubType()).append(quotes);
            } else {
                buffer.append(" subType=").append(quotes).append("").append(quotes);
            }
            if (cmbean.getDaysOfTheWeek() != null) {
                buffer.append(" daysOfTheWeek=").append(quotes).append(cmbean.getDaysOfTheWeek()).append(quotes);
            } else {
                buffer.append(" daysOfTheWeek=").append(quotes).append("").append(quotes);
            }
            if (cmbean.getRepeatFrequency() != null) {
                buffer.append(" repeatFrequency=").append(quotes).append(cmbean.getRepeatFrequency()).append(quotes);
            } else {
                buffer.append(" repeatFrequency=").append(quotes).append("").append(quotes);
            }
            if (cmbean.getTaskTagName() != null) {
                buffer.append(" taskTagName=").append(quotes).append(cmbean.getTaskTagName()).append(quotes);
            } else {
                buffer.append(" taskTagName=").append(quotes).append("").append(quotes);
            }
            buffer.append(" taskTagId=").append(quotes).append(cmbean.getCustomTagId()).append(quotes);
            buffer.append(" taskTagGroupId=").append(quotes).append(cmbean.getCustomSetId()).append(quotes);
            buffer.append(" isShowOnUI=").append(quotes).append(cmbean.isCustomTagVisible()).append(quotes);
            Log.i("taskconversation", "Leave Project id 3 " + cmbean.getProjectId());
            if (cmbean.getProjectId() != null) {
                buffer.append(" projectId=").append(quotes).append(cmbean.getProjectId()).append(quotes);
            } else {
                buffer.append(" projectId=").append(quotes).append("").append(quotes);
            }
            if (cmbean.getCatagory() != null && cmbean.getCatagory().equalsIgnoreCase("task")) {
                buffer.append(" taskCategory=").append(quotes).append("taskCreation").append(quotes);
            } else {
                buffer.append(" taskCategory=").append(quotes).append(cmbean.getCatagory()).append(quotes);
            }
            Log.i("taskconversation", "Leave Project id 4 " + cmbean.getProjectId());
            if (cmbean.getProjectId() != null && !cmbean.getProjectId().equalsIgnoreCase("")) {
                if (cmbean.getCatagory() != null && cmbean.getCatagory().equalsIgnoreCase("task")) {
                    String pjt_parentId = dataBase.getProjectParentTaskId("select parentTaskId from projectHistory where taskId='" + cmbean.getTaskId() + "'");
                    Log.i("taskconversation", "parentTaskId ProjectId " + pjt_parentId);
                    if (pjt_parentId != null && !pjt_parentId.equalsIgnoreCase("null") && !pjt_parentId.equalsIgnoreCase("")) {
                        buffer.append(" parentTaskId=").append(quotes).append(pjt_parentId).append(quotes);
                    } else {
                        buffer.append(" parentTaskId=").append(quotes).append("").append(quotes);
                    }
                } else {
                    if (cmbean.getIssueId() != null) {
                        buffer.append(" parentTaskId=").append(quotes).append(cmbean.getIssueId()).append(quotes);
                    } else {
                        buffer.append(" parentTaskId=").append(quotes).append("").append(quotes);
                    }
                }
            } else {
                if (cmbean.getIssueId() != null) {
                    buffer.append(" parentTaskId=").append(quotes).append(cmbean.getIssueId()).append(quotes);
                } else {
                    buffer.append(" parentTaskId=").append(quotes).append("").append(quotes);
                }
            }
            if (cmbean.getDaysOfTheWeek() != null && !cmbean.getDaysOfTheWeek().equalsIgnoreCase("") && !cmbean.getDaysOfTheWeek().equalsIgnoreCase(null)) {
                buffer.append(" isRepeatTask=").append(quotes).append("Y").append(quotes);
            }
            buffer.append(" />");
            buffer.append("</TaskDetailsinfo>");
            Log.d("xml", "composed xml for chat======>" + buffer.toString());
            Log.d("xml", "composed xml for encode data======>" + Charset.forName("UTF-8").encode(":-)").toString());
//            Log.i("xml", "composed xml for listofabservers " + listOfObservers);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return buffer.toString();
        }
    }

    public String TimeFrequencyCalculation(String timeFrequency) {
        String remainder_Frequency, rem_freq_min, rem_frq;
        long total_mins;
        if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
//            Log.i("task", "Reminder Freq Local Changed to Lower case " + reminderfreq.toLowerCase());
            switch (timeFrequency.toLowerCase()) {
                case "none":
                    remainder_Frequency = "0";
                    break;
                case "every minute":
                    total_mins = 60000;
                    remainder_Frequency = String.valueOf(total_mins);
                    break;
                case "every 10 min":
                    total_mins = 10 * 60000;
                    remainder_Frequency = String.valueOf(total_mins);
                    break;
                case "hourly":
                    total_mins = 60 * 60000;
                    remainder_Frequency = String.valueOf(total_mins);
                    break;
                case "daily":
                    total_mins = 1440 * 60000;
                    remainder_Frequency = String.valueOf(total_mins);
                    break;
                case "week day":
                    total_mins = 10080 * 60000;
                    remainder_Frequency = String.valueOf(total_mins);
                    break;
                case "monthly":
                    total_mins = 43200 * 60000;
                    remainder_Frequency = String.valueOf(total_mins);
                    break;
                case "yearly":
                    total_mins = 525600 * 60000;
                    remainder_Frequency = String.valueOf(total_mins);
                    break;
                default:
                    remainder_Frequency = "0";
                    break;
            }
        } else {
            Log.i("Request", "timeFrequency " + timeFrequency);  // 1 Minute
            rem_freq_min = timeFrequency.split(" ")[0];   // 1
            rem_frq = timeFrequency.split(" ")[1];   // Minute
            Log.i("task", "Reminder Freq Minutes " + rem_freq_min);
            switch (rem_frq.toLowerCase()) {
                case "minutes":
                    total_mins = Long.parseLong(rem_freq_min) * 60000;
                    Log.i("task", "Reminder miniute Milliseconds " + total_mins);
                    remainder_Frequency = String.valueOf(total_mins);
                    break;
                case "hours":
                    total_mins = Long.parseLong(rem_freq_min) * (60 * 60000);
                    Log.i("task", "Reminder hour Milliseconds " + total_mins);
                    remainder_Frequency = String.valueOf(total_mins);
                    break;
                case "days":
                    long day_s = Long.parseLong(rem_freq_min);
                    total_mins = day_s * (1440 * 60000);
                    Log.i("task", "Reminder day Milliseconds " + day_s + " " + total_mins);
                    remainder_Frequency = String.valueOf(total_mins);
                    break;
                default:
                    remainder_Frequency = "0";
                    break;
            }
        }
        return remainder_Frequency;
    }

    public void sendMultiInstantMessage(String msgBody, String[] userlist, int sendTo) {

//        String buddy_uri = "<"+number+">";
//        Log.i("chat", "buddy_uri======= " + buddy_uri);
//        BuddyConfig bCfg = new BuddyConfig();
//        bCfg.setUri(buddy_uri);
//        bCfg.setSubscribe(true);

        Log.i("TaskHistory", "sendMultiInstantMessage -----> ");
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
        } /*else {
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
