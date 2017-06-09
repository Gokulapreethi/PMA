package com.myapplication3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
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

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.myapplication3.Bean.ProjectDetailsBean;
import com.myapplication3.Bean.TaskDetailsBean;
import com.myapplication3.DB.VideoCallDataBase;
import com.myapplication3.RandomNumber.Utility;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.app.MyBuddy;

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
import json.WebServiceInterface;

/**
 * Created by saravanakumar on 7/13/2016.
 */
public class TaskHistory extends Activity implements WebServiceInterface {

    static TaskHistory taskHistory;
    public TaskDetailsBean taskDetailsBean;
    ArrayList<TaskDetailsBean> taskDetailsBeen, taskDetailsBeen1, non_activebean, taskDetailsBeen2, taskDetailsBeanArraylist, filterbuddy;
    String userName, taskType;
    String currentLoginUserName, currentLoginUserMail;
    String reopen_qry;
    String qury = "alltask";
    Context context;
    ImageView submit_icon, fill;
    int rem;
    TextView finish_page, active_task;
    LinearLayout History_Search;
    EditText Search_EditText;
    TextView Noresult;
    String catagory;
    public String[] catestring1, catestring2, catestring3;
    public ArrayAdapter<String> dataAdapter3;
    Spinner spin1, spin2, spin3;
    TextView cancelbutton, donebutton, clearbutton;
    int spin1pos, spin2pos, spin3pos;
    private HistoryFilter filter;
    TaskArrayAdapter buddyArrayAdapter;
    BuddyArrayAdapter buddyadapter;
    String quotes = "\"", query;
    ProgressDialog dialog;
    LinearLayout spinner_layout;
    String webTaskId, group_id;
    private Handler handler = new Handler();
    boolean check = false, isscrool = false;
    private SwipeMenuListView listView;
    VideoCallDataBase videoCallDataBase;
    private ShowOrCancelProgress progressListener;
    VideoCallDataBase dataBase;
    AppSharedpreferences appSharedpreferences;
    //For LazyLoading
    private int taskList_count = 0;
    boolean Scroll = false;
    boolean oncreate_query = false;

    public static TaskHistory getInstance() {
        return taskHistory;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void setActiveAdapter(int count) {
        try {
            taskDetailsBeen1.clear();
            taskDetailsBeen.clear();
            taskDetailsBeanArraylist.clear();
            Log.i("TaskHistory", "setActiveAdapter is == 0 " + userName);
            Log.i("TaskHistory", "taskType is == 1 " + taskType);
            Log.i("TaskHistory", " currentLoginUserName is == 2 " + currentLoginUserName + " \n currentLoginUserMail " + currentLoginUserMail);
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
            Log.i("TaskHistory", "lazyload db count--->" + count);
            Log.i("TaskHistory", "lazyload setActiveAdapter buddyOrgroup_count--->" + taskList_count);
            if (count == 0 && !countAboveten) {
                query = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' )) and ( loginuser='" + currentLoginUserMail + "') and ( taskType='" + taskType + "') and (category <> 'chat')";
            } else if (count < 10 && !countAboveten) {
                query = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' )) and ( loginuser='" + currentLoginUserMail + "') and ( taskType='" + taskType + "') and (category <> 'chat') order by dateTime ASC LIMIT " + count + " OFFSET 0";
            } else {
                query = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' )) and ( loginuser='" + currentLoginUserMail + "') and ( taskType='" + taskType + "') and (category <> 'chat') order by dateTime ASC LIMIT 10 OFFSET " + count + "";
            }
            Log.d("TaskHistory", "lazyload query is == 3 " + query);
            taskDetailsBeen1 = videoCallDataBase.getTaskHistoryInfo(query);
            Log.d("TaskHistory", "lazyload reopen_qry--> " + reopen_qry);
            Log.i("TaskHistory", "lazyload size of history 4 " + taskDetailsBeen1.size());
            Log.i("TaskHistory", "lazyload size of history 4 " + taskDetailsBeanArraylist.size());
            Log.i("TaskHistory", "lazyload userName 5 " + userName);
            taskDetailsBeen.clear();
            taskDetailsBeanArraylist.clear();
            if (taskDetailsBeen1.size() > 0) {
                for (TaskDetailsBean contactBean : taskDetailsBeen1) {
                    Log.i("TaskHistory", "project 3");
                    Log.i("TaskHistory", "setActiveAdapter 3" + " 1 " + " ********");
                    int unReadMsgCount = videoCallDataBase.getTaskUnReadMsgCount(contactBean.getTaskId());
                    Log.d("TaskHistory", "unread count " + unReadMsgCount);
                    Log.d("task1", "unread count " + unReadMsgCount);
                    Log.d("task1", "for each  issues Id  == " + contactBean.getIssueId());
                    rem = videoCallDataBase.getRemainderUnReadMsgCount(contactBean.getTaskId());
                    contactBean.setRead_status(unReadMsgCount);
                    Log.d("TaskHistory", "contactBean.getRead_status() " + contactBean.getRead_status());
                    contactBean.setMsg_status(rem);
                    Log.i("TaskHistory", "popup status 3 8 " + contactBean.getTaskStatus());
                    if (contactBean.getTaskStatus().equalsIgnoreCase("completed")) {
                        reopen_qry = "select taskId from taskHistoryInfo where (parentTaskId ='" + contactBean.getTaskId() + "') and taskStatus != 'closed'";
                        Log.d("DBQuery", "reopen_qry--> " + reopen_qry);
                        Log.d("DBQuery", "reopen_qry boolean --> " + videoCallDataBase.isAgendaRecordExists(reopen_qry));
                        if (videoCallDataBase.isAgendaRecordExists(reopen_qry)) {
                            contactBean.setTaskStatus("Reopen");
                        }
                    }
                    Log.i("popup", "status after query " + contactBean.getTaskStatus());
                    taskDetailsBeen.add(contactBean);
                    taskDetailsBeanArraylist.add(contactBean);
                }
            }
            taskDetailsBeen = new ArrayList<TaskDetailsBean>();
            taskDetailsBeen = (ArrayList<TaskDetailsBean>) taskDetailsBeen1.clone();
            taskDetailsBeanArraylist = new ArrayList<TaskDetailsBean>();
            taskDetailsBeanArraylist = (ArrayList<TaskDetailsBean>) taskDetailsBeen1.clone();
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
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        // It's expensive, if running turn it off.
        isscrool = false;
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(Search_EditText.getWindowToken(), 0);
        Search_EditText.setCursorVisible(false);
        Search_EditText.clearFocus();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Appreference.context_table.put("taskhistory", this);

        context = this;
        taskHistory = this;
        catagory = "task";
        oncreate_query = true;
        isscrool = true;
        setContentView(R.layout.task_history);
        progressListener = Appreference.main_Activity_context;
        listView = (SwipeMenuListView) findViewById(R.id.lv_taskHistory);
        finish_page = (TextView) findViewById(R.id.finish_page);
        submit_icon = (ImageView) findViewById(R.id.submit_icon);
        active_task = (TextView) findViewById(R.id.txtView01);
        fill = (ImageView) findViewById(R.id.fill);
        History_Search = (LinearLayout) findViewById(R.id.History_Search);
        Search_EditText = (EditText) findViewById(R.id.searchtext);
        Noresult = (TextView) findViewById(R.id.Noresult);
        Noresult.setVisibility(View.GONE);
        Search_EditText.setCursorVisible(false);
        spinner_layout = (LinearLayout) findViewById(R.id.spinner_layout);
        spin1 = (Spinner) findViewById(R.id.spinner1);
        spin2 = (Spinner) findViewById(R.id.spinner2);
        spin3 = (Spinner) findViewById(R.id.spinner3);
        cancelbutton = (TextView) findViewById(R.id.cancelbutton);
        donebutton = (TextView) findViewById(R.id.donebutton);
        clearbutton = (TextView) findViewById(R.id.clearbutton);
        dataBase = VideoCallDataBase.getDB(context);
        appSharedpreferences = AppSharedpreferences.getInstance(context);
        taskDetailsBeen = new ArrayList<>();
        taskDetailsBeen1 = new ArrayList<>();
        taskDetailsBeen2 = new ArrayList<>();
        taskDetailsBeanArraylist = new ArrayList<>();
        non_activebean = new ArrayList<>();
        spinner_layout.setVisibility(View.GONE);
        userName = getIntent().getExtras().getString("userId");
        taskType = getIntent().getExtras().getString("taskType");
        Log.i("TaskHistory", "oncreate username---> " + userName + "username " + Appreference.loginuserdetails.getUsername());
        group_id = userName;

        if (taskType != null && taskType.equalsIgnoreCase("group")) {
            userName = VideoCallDataBase.getDB(context).getProjectParentTaskId("select groupname from group1 where loginuser='" + Appreference.loginuserdetails.getUsername() + "' and groupid='" + userName + "'");
        }

        currentLoginUserName = appSharedpreferences.getString("loginUserName");
        currentLoginUserMail = Appreference.loginuserdetails.getEmail();
        if (currentLoginUserMail == null)
            currentLoginUserMail = appSharedpreferences.getString("mEmail");
        videoCallDataBase = VideoCallDataBase.getDB(context);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(Search_EditText.getWindowToken(), 0);
        String row_query = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' )) and ( loginuser='" + currentLoginUserMail + "') and ( taskType='" + taskType + "')";
        int row_count = videoCallDataBase.getTaskHistoryRowCount(row_query);
        Log.i("lazyload", "row count--->" + row_count);
        setActiveAdapter(row_count);

        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fill.setBackgroundResource(R.drawable.filterdisabled);
                listView.setEnabled(true);
                spinner_layout.setVisibility(View.GONE);
                Search_EditText.setVisibility(View.VISIBLE);
            }
        });

        clearbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spin1.setSelection(spin1.getCount() - 1);
                spin2.setSelection(spin2.getCount() - 1);
                spin3.setSelection(spin3.getCount() - 1);
//                spinner_layout.setVisibility(View.GONE);
                fill.setBackgroundResource(R.drawable.filterenabled);
//                Search_EditText.setVisibility(View.VISIBLE);
                String row_query = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' )) and ( loginuser='" + currentLoginUserMail + "') and ( taskType='" + taskType + "')";
                int row_count = videoCallDataBase.getTaskHistoryRowCount(row_query);
                Log.i("lazyload", "row count--->" + row_count);
                setActiveAdapter(row_count);
            }
        });

        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setEnabled(true);
                fill.setBackgroundResource(R.drawable.filterenabled);
                String spinner1 = spin1.getSelectedItem().toString();
                spin1pos = spin1.getSelectedItemPosition();
                String spinner2 = spin2.getSelectedItem().toString();
                spin2pos = spin2.getSelectedItemPosition();
                String spinner3 = spin3.getSelectedItem().toString();
                spin3pos = spin3.getSelectedItemPosition();
                Log.i("TaskHistorySpinner", spinner1);
                Log.i("TaskHistorySpinner", spinner2);
                Log.i("TaskHistorySpinner", spinner3);
                Log.i("TaskHistorySpinner", String.valueOf(spin1pos));
                Log.i("TaskHistorySpinner", String.valueOf(spin2pos));
                Log.i("TaskHistorySpinner", String.valueOf(spin3pos));
                Log.i("TaskHistorySpinner", String.valueOf(spin1.getCount() - 1));
                Log.i("TaskHistorySpinner", String.valueOf(spin2.getCount() - 1));
                Log.i("TaskHistorySpinner", String.valueOf(spin3.getCount() - 1));

                String issue_qry = " ";
                String spy = " ";
                if (!spinner3.equalsIgnoreCase("Me") && !spinner3.equalsIgnoreCase("All")) {
                    String firstname = spinner3.split(" ")[0];
                    String lastname = spinner3.split(" ")[1];
                    spy = VideoCallDataBase.getDB(context).getusername(firstname, lastname);
                }
                if (!spinner1.equalsIgnoreCase("All") && !spinner2.equalsIgnoreCase("All") && !spinner3.equalsIgnoreCase("All")) {
                    if (spinner3.equalsIgnoreCase("Me")) {
                        if (!spinner2.equalsIgnoreCase("reopen"))
                            issue_qry = "select * from taskHistoryInfo where ( taskStatus='" + spinner2 + "') and( category='" + spinner1 + "') and ( ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "')and ( taskType='" + taskType + "')";
                        Log.i("TaskHistory", spinner1);
                        Log.i("TaskHistory", taskType);
                        Log.i("TaskHistory", spinner2);
                    } else {
                        issue_qry = "select * from taskHistoryInfo where ( taskStatus='" + spinner2 + "') and( category='" + spinner1 + "') and ( ownerOfTask='" + spy + "')and ( taskType='" + taskType + "')";
                        Log.i("TaskHistory", "issue_qry value" + issue_qry);
                    }
                } else if (spinner1.equalsIgnoreCase("All")) {
                    if (spinner2.equalsIgnoreCase("All")) {
                        if (!spinner3.equalsIgnoreCase("All")) {
                            if (spinner3.equalsIgnoreCase("Me")) {
                                issue_qry = "select * from taskHistoryInfo where ( ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "')and ( taskType='" + taskType + "')";
                                Log.i("TaskHistory", spinner1);
                                Log.i("TaskHistory", taskType);
                                Log.i("TaskHistory", spinner2);
                                Log.i("TaskHistory", "issue_qry value" + issue_qry);
                            } else {
                                issue_qry = "select * from taskHistoryInfo where ( ownerOfTask='" + spy + "')and ( taskType='" + taskType + "')";
                                Log.i("TaskHistory", "issue_qry value" + issue_qry);
                            }
                        }
                    } else {
                        if (spinner3.equalsIgnoreCase("All")) {
                            issue_qry = "select * from taskHistoryInfo where ( taskStatus='" + spinner2 + "')and ( taskType='" + taskType + "')";
                        } else {
                            if (spinner3.equalsIgnoreCase("Me")) {
                                issue_qry = "select * from taskHistoryInfo where ( taskStatus='" + spinner2 + "') and ( ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "')and ( taskType='" + taskType + "')";
                                Log.i("TaskHistory", spinner1);
                                Log.i("TaskHistory", taskType);
                                Log.i("TaskHistory", spinner2);
                                Log.i("TaskHistory", "issue_qry value" + issue_qry);
                            } else {
                                issue_qry = "select * from taskHistoryInfo where ( taskStatus='" + spinner2 + "') and ( ownerOfTask='" + spy + "')and ( taskType='" + taskType + "')";
                                Log.i("TaskHistory", "issue_qry value" + issue_qry);
                            }
                        }
                    }
                } else {
                    if (spinner2.equalsIgnoreCase("All")) {
                        if (spinner3.equalsIgnoreCase("All")) {
                            issue_qry = "select * from taskHistoryInfo where ( category='" + spinner1 + "')and( taskType='" + taskType + "') ";
                        } else {
                            if (spinner3.equalsIgnoreCase("Me")) {
                                issue_qry = "select * from taskHistoryInfo where ( ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "')and ( category='" + spinner1 + "')and( taskType='" + taskType + "')";
                                Log.i("TaskHistory", spinner1);
                                Log.i("TaskHistory", taskType);
                                Log.i("TaskHistory", spinner2);
                                Log.i("TaskHistory", "issue_qry value" + issue_qry);
                            } else {
                                issue_qry = "select * from taskHistoryInfo where ( ownerOfTask='" + spy + "')and ( category='" + spinner1 + "')and( taskType='" + taskType + "')";
                                Log.i("TaskHistory", "issue_qry value" + issue_qry);
                            }
                        }
                    } else {
                        if (spinner3.equalsIgnoreCase("All")) {
                            issue_qry = "select * from taskHistoryInfo where ( taskStatus='" + spinner2 + "')and ( category='" + spinner1 + "')and ( taskType='" + taskType + "')";
                        } else {
                            if (spinner3.equalsIgnoreCase("Me")) {
                                issue_qry = "select * from taskHistoryInfo where ( taskStatus='" + spinner2 + "') and ( category='" + spinner1 + "')and ( ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "')and ( taskType='" + taskType + "')";
                                Log.i("TaskHistory", spinner1);
                                Log.i("TaskHistory", taskType);
                                Log.i("TaskHistory", spinner2);
                                Log.i("TaskHistory", "issue_qry value" + issue_qry);
                            } else {
                                issue_qry = "select * from taskHistoryInfo where ( taskStatus='" + spinner2 + "') and ( ownerOfTask='" + spy + "')and ( category='" + spinner1 + "')and ( taskType='" + taskType + "')";
                                Log.i("TaskHistory", "issue_qry value" + issue_qry);
                            }
                        }
                    }
                }
                taskDetailsBeanArraylist.clear();
                taskDetailsBeen.clear();
                Log.i("TaskHistoryFill", "issue_qry value" + issue_qry);
                try {
                    taskDetailsBeen1 = VideoCallDataBase.getDB(context).getTaskHistoryInfo(issue_qry);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("TaskHistoryFill ", "size of issue_task " + String.valueOf(taskDetailsBeen1.size()));
                Log.i("TaskHistoryFill ", "size of issue_task ** " + taskDetailsBeanArraylist.size());
                taskDetailsBeen.addAll(taskDetailsBeen1);
                Log.i("TaskHistoryFill ", "size of issue_task ** " + taskDetailsBeanArraylist.size());
                if (spinner1.equalsIgnoreCase("All") && spinner2.equalsIgnoreCase("All") && spinner3.equalsIgnoreCase("All")) {
                    String row_query = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' )) and ( loginuser='" + currentLoginUserMail + "') and ( taskType='" + taskType + "')";
                    int row_count = videoCallDataBase.getTaskHistoryRowCount(row_query);
                    Log.i("lazyload", "row count--->" + row_count);
                    setActiveAdapter(row_count);
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
                            Noresult.setText("No TaskHistories AvailablFe");
                        } else {
                            Noresult.setVisibility(View.GONE);
                            taskDetailsBeanArraylist.addAll(taskDetailsBeen1);
                            buddyArrayAdapter.notifyDataSetChanged();
                            listView.setAdapter(buddyArrayAdapter);
                        }
                    }
                });
                spinner_layout.setVisibility(View.GONE);
                Search_EditText.setVisibility(View.VISIBLE);
            }
        });

        fill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search_EditText.setVisibility(View.GONE);
                check = true;
                isscrool = false;
                if (spinner_layout.getVisibility() == View.GONE) {
                    fill.setBackgroundResource(R.drawable.filterenabled);
                    spinner_layout.setVisibility(View.VISIBLE);
                    Search_EditText.setVisibility(View.GONE);
                    listView.setEnabled(false);
                    listView.setClickable(false);
                } else {
                    fill.setBackgroundResource(R.drawable.filterdisabled);
                    spinner_layout.setVisibility(View.GONE);
                    Search_EditText.setVisibility(View.VISIBLE);
                    listView.setEnabled(true);
                    listView.setClickable(true);
                }
                List<String> categories = new ArrayList<String>();
                categories.add("Task");
                categories.add("issue");
                categories.add("note");
                categories.add("Template");
                categories.add("All");
                catestring1 = categories.toArray(new String[categories.size()]);
                spin1.setAdapter(new MyAdapter1(getApplicationContext(), R.layout.customspin, catestring1));
                spin1.setSelection(spin1.getCount() - 1);
                spin1.setPopupBackgroundResource(R.drawable.borderfordialog);
                List<String> status = new ArrayList<String>();
                status.add("inprogress");
                status.add("assigned");
                status.add("closed");
                status.add("completed");
                status.add("abandoned");
                status.add("reopen");
                status.add("rejected");
                status.add("All");
                catestring2 = status.toArray(new String[status.size()]);
                spin2.setAdapter(new MyAdapter2(getApplicationContext(), R.layout.customspin, catestring2));
                spin2.setSelection(spin2.getCount() - 1);
                spin2.setPopupBackgroundResource(R.drawable.borderfordialog);
                List<String> categories3 = new ArrayList<String>();
                String givername = " ";
                Log.i("TaskHistory", "loginuser detail" + Appreference.loginuserdetails.getEmail());
                givername = "select contactemail from contact where loginuser='" + Appreference.loginuserdetails.getEmail() + "'";
                Log.i("TaskHistory", "givername" + givername);
                /*ArrayList<ContactBean> buddyList = VideoCallDataBase.getDB(context).getContact(Appreference.loginuserdetails.getUsername());
                Log.i("TaskHistory","buddylist value is"+buddyList.size());
                for (int i=0;i<buddyList.size();i++){
                    String username=buddyList.get(i).getUsername();
                categories3.add(VideoCallDataBase.getDB(context).getname(username));
                }*/
                categories3.add(VideoCallDataBase.getDB(context).getname(userName));
                categories3.add("Me");
                categories3.add("All");
                catestring3 = categories3.toArray(new String[categories3.size()]);
                spin3.setAdapter(new MyAdapter3(getApplicationContext(), R.layout.customspin, catestring3));
                spin3.setSelection(spin3.getCount() - 1);
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
            }
        });
        Search_EditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String[] value = new String[count];
                isscrool = false;
                Log.d("constraint", "JNDSEJBJW  * " + s);
                TaskHistory.this.buddyArrayAdapter.getFilter().filter(s);
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
                Search_EditText.setCursorVisible(false);
                isscrool = false;
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(Search_EditText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                taskDetailsBean = taskDetailsBeen.get(position);
                Log.i("swipe", "issue id 1---->" + taskDetailsBean.getTaskId());
                String query;
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
                        String taskUTCtime = dateforrow;
                        taskDetailsBean.setDateTime(dateTime);
                        taskDetailsBean.setTaskUTCDateTime(dateforrow);
                        taskDetailsBean.setTaskUTCTime(taskUTCtime);
                        taskDetailsBean.setTasktime(tasktime);
                        taskDetailsBean.setMimeType("text");
                        taskDetailsBean.setSignalid(Utility.getSessionID());
                        if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("Activate")) {
                            Log.i("task", "case------>0" + menu.getMenuItem(index).getTitle());
                            taskDetailsBean.setTaskStatus("inprogress");
                            Log.i("task", "case------>1");
                            String query3 = "update taskDetailsInfo  set taskStatus = 'inprogress' where ('" + taskDetailsBean.getTaskId() + "'= taskId ) and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and (category <> 'chat');";
                            String taskHistoryquery = "update taskHistoryInfo  set taskStatus = 'inprogress' where ('" + taskDetailsBean.getTaskId() + "'= taskId ) and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and (category <> 'chat');";
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
                                String task_receiver_1 = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskReceiver from taskHistoryInfo where taskId='" + taskDetailsBean.getTaskId() + "' order by id LIMIT 1");
                                Log.i("TaskHistory", "Activated toUser_id " + task_receiver_1);
                                int toUser_id = VideoCallDataBase.getDB(context).getUserid(task_receiver_1);
                                jsonObject3.put("id", toUser_id);
                                jsonObject.put("to", jsonObject3);
                                jsonObject.put("signalId", taskDetailsBean.getSignalid());
                                jsonObject.put("parentId", taskDetailsBean.getParentId());
                                jsonObject.put("createdDate", taskDetailsBean.getDateTime());
                                if (taskDetailsBean.getCompletedPercentage() != null && !taskDetailsBean.getCompletedPercentage().equalsIgnoreCase("") && !taskDetailsBean.getCompletedPercentage().equalsIgnoreCase(null)) {
                                    jsonObject.put("percentageCompleted", taskDetailsBean.getCompletedPercentage());
                                } else {
                                    jsonObject.put("percentageCompleted", "0");
                                }
                                jsonObject.put("requestType", "percentageCompleted");
                                jsonObject.put("taskStatus", "inprogress");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("TaskHistory", "task status " + taskDetailsBean.getTaskStatus());
                            Log.i("jsonrequest", jsonObject.toString());
                            Appreference.jsonRequestSender.taskConversationEntry(EnumJsonWebservicename.taskConversationEntry, jsonObject, TaskHistory.this, null, taskDetailsBean);
                            Log.i("TaskHistory ", "webservice called.abandoned");
                            taskDetailsBeen.remove(position);
                            taskDetailsBeanArraylist.remove(position);
                            buddyArrayAdapter.notifyDataSetChanged();
                        } else if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("Abandon")) {
                            taskDetailsBean.setTaskStatus("abandoned");
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
                                String task_receiver_1 = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskReceiver from taskHistoryInfo where taskId='" + taskDetailsBean.getTaskId() + "' order by id LIMIT 1");
                                Log.i("TaskHistory", "Abandoned toUser_id " + task_receiver_1);
                                int toUser_id = VideoCallDataBase.getDB(context).getUserid(task_receiver_1);
                                jsonObject3.put("id", String.valueOf(toUser_id));
                                jsonObject.put("to", jsonObject3);
                                jsonObject.put("signalId", taskDetailsBean.getSignalid());
                                jsonObject.put("parentId", taskDetailsBean.getParentId());
                                jsonObject.put("createdDate", taskDetailsBean.getDateTime());
                                if (taskDetailsBean.getCompletedPercentage() != null && !taskDetailsBean.getCompletedPercentage().equalsIgnoreCase("") && !taskDetailsBean.getCompletedPercentage().equalsIgnoreCase(null)) {
                                    jsonObject.put("percentageCompleted", taskDetailsBean.getCompletedPercentage());
                                } else {
                                    jsonObject.put("percentageCompleted", "0");
                                }
                                jsonObject.put("requestType", "percentageCompleted");
                                jsonObject.put("taskStatus", "abandoned");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("beforewebcall", taskDetailsBean.getTaskStatus());
                            Log.i("jsonrequest", jsonObject.toString());
                            Appreference.jsonRequestSender.taskConversationEntry(EnumJsonWebservicename.taskConversationEntry, jsonObject, TaskHistory.this, null, taskDetailsBean);
                            Log.e("webservice ", "called.active ");
                            taskDetailsBeen.remove(position);
                            taskDetailsBeanArraylist.remove(position);
                            buddyArrayAdapter.notifyDataSetChanged();

                        } else if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("View Issues")) {
                            isscrool = false;
                            Log.d("swipe", "issue owner ***  ");
                            Log.d("swipe", "issue***---> owner  == " + taskDetailsBean.getTaskId());
                            String catagory = "issue";
                            String issue_qry = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and (category <> 'chat') and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + taskType + "') and (parentTaskId='" + taskDetailsBean.getTaskId() + "') group by taskId";
                            Log.d("swipe", "issue_qry owner  " + issue_qry);
                            taskDetailsBeen1.clear();
                            taskDetailsBeen1 = VideoCallDataBase.getDB(context).getTaskHistoryInfo(issue_qry);
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
                            String catagory = "issue";
                            String issue_qry = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and (category <> 'chat') and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + taskType + "') and (taskId='" + taskDetailsBean.getIssueId() + "') group by taskId";
                            Log.d("swipe", "issue_qry owner  " + issue_qry);
                            taskDetailsBeen1.clear();
                            taskDetailsBeen1 = VideoCallDataBase.getDB(context).getTaskHistoryInfo(issue_qry);
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
                            String query1 = "delete from taskDetailsInfo where('" + taskDetailsBean.getTaskId() + "'= taskId ) and (category <> 'chat') and loginuser='" + Appreference.loginuserdetails.getEmail() + "';";
                            VideoCallDataBase.getDB(context).getTaskHistory(query1);
                            String taskHistoryquery = "delete from taskHistoryInfo where('" + taskDetailsBean.getTaskId() + "'= taskId ) and (category <> 'chat') and loginuser='" + Appreference.loginuserdetails.getEmail() + "';";
                            VideoCallDataBase.getDB(context).getTaskHistoryInfo(taskHistoryquery);
                        }
                    } else {
                        Log.i("task", "case receiver " + menu.getMenuItem(index).getTitle());
                        Log.d("swipe", "issue$ ");
                        if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("View Issues")) {
                            Log.d("swipe", "issue***---> " + taskDetailsBean.getTaskId());
                            String catagory = "issue";
                            String issue_qry = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and (category <> 'chat') and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + taskType + "') and (parentTaskId='" + taskDetailsBean.getTaskId() + "') group by taskId";
                            Log.d("swipe", "issue_qry  " + issue_qry);
                            taskDetailsBeen1.clear();
                            taskDetailsBeen1 = VideoCallDataBase.getDB(context).getTaskHistoryInfo(issue_qry);
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
                            Log.d("swipe", "issue***---> owner  == " + taskDetailsBean.getTaskId());
                            Log.d("swipe", "issue***---> owner  == " + taskDetailsBean.getIssueId());
                            String catagory = "issue";
                            String issue_qry = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and (category <> 'chat') and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + taskType + "') and (taskId='" + taskDetailsBean.getIssueId() + "') group by taskId";
                            Log.d("swipe", "issue_qry owner  " + issue_qry);
                            taskDetailsBeen1.clear();
                            taskDetailsBeen1 = VideoCallDataBase.getDB(context).getTaskHistoryInfo(issue_qry);
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
                return false;
            }
        });
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
                    } else if (taskList_count != 0 && taskList_count < 10) {
                        Scroll = true;
                    } else {
                        Scroll = false;
                    }
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

        final SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
/*if(taskDetailsBean!=null) {
    if (taskDetailsBean.getFromUserEmail().equalsIgnoreCase(Appreference.loginuserdetails.getEmail())) {*/
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
   /* }
}*/

                        /*// create "delete" item
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

                        /*SwipeMenuItem deleteItem1 = new SwipeMenuItem(
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
                        menu.addMenuItem(deleteItem1);
                        break;*/
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
                        openItem4.setBackground(new ColorDrawable(Color.rgb(0x00, 0xff, 0x11)));
                        // set item width
                        openItem4.setWidth(dp2px(90));
                        // set item title
                        openItem4.setTitle("Abonded");
                        // set item title fontsize
                        openItem4.setTitleSize(18);
                        // set item title font color
                        openItem4.setTitleColor(Color.WHITE);
                        // add to menu
                        menu.addMenuItem(openItem4);
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
                        openItem5.setTitle("Assign");
                        // set item title fontsize
                        openItem5.setTitleSize(18);
                        // set item title font color
                        openItem5.setTitleColor(Color.WHITE);
                        // add to menu
                        menu.addMenuItem(openItem5);
   /* }
}*/

                       /* // create "delete" item
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
                        menu.addMenuItem(issue5);
                        break;
                }
            }
        };
        listView.setMenuCreator(creator);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                taskDetailsBean = taskDetailsBeen.get(position);
                Log.d("Task1", "task stststts " + taskDetailsBean.getTaskStatus());
                if (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("Template") || (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("draft"))) {
                    if (!taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        showDialog();
                        webTaskId = taskDetailsBean.getTaskId();
                        gettaskwebservice(webTaskId);
                        check = true;
                    }
                } else {
                    Log.i("task", String.valueOf(position));
                    Intent intent = new Intent(context, NewTaskConversation.class);
                    intent.putExtra("task", "taskhistory");
                    intent.putExtra("taskHistoryBean", taskDetailsBean);
                    intent.putExtra("catagory", taskDetailsBean.getCatagory());
                    Log.i("Task1", "groupname" + group_id);
                    intent.putExtra("groupname", group_id);
                    Log.i("task", "taskId is == " + taskDetailsBean.getTaskId());
                    check = true;
                    startActivity(intent);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Search_EditText.setText("");
                    }
                }, 1000);
            }

        });

        finish_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(Search_EditText.getWindowToken(), 0);
                Appreference.is_reload = true;
                finish();
            }
        });

        submit_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TaskHistory.this, Perfomanceview.class);
                intent.putExtra("is_project", "N");
                intent.putExtra("User_Project_Id", String.valueOf(Appreference.loginuserdetails.getId()));
                startActivity(intent);
            }
        });
    }

    public void showDialog() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                dialog = new ProgressDialog(TaskHistory.this);
                dialog.setMessage("Loading Task...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                dialog.setProgress(0);
                dialog.setMax(100);
                dialog.show();
            }
        });
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
        }
    }

    public void showToast(final String result) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void gettaskwebservice(String webtaskId) {
        List<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>(1);
        nameValuePairs1.add(new BasicNameValuePair("taskId", webtaskId));
        Appreference.jsonRequestSender.getTask(EnumJsonWebservicename.getTask, nameValuePairs1, TaskHistory.this);
    }

    public void refresh() {
        try {
            isscrool = false;
            String query1;
            if (taskDetailsBeen != null)
                taskDetailsBeen.clear();
            taskDetailsBeanArraylist.clear();
            oncreate_query = true;
            String count_query = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + currentLoginUserMail + "') and ( taskType='" + taskType + "')";
            int count = videoCallDataBase.getTaskHistoryRowCount(count_query);
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
            Log.i("lazyload", "db count--->" + taskList_count);
            Log.i("lazyload", "refresh buddyOrgroup_count--->" + taskList_count);
            if (taskList_count == 0 && !countAboveten) {
                query1 = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and (category <> 'chat') and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' )) and ( loginuser='" + currentLoginUserMail + "') and ( taskType='" + taskType + "')";
            } else if (taskList_count < 10 && !countAboveten) {
                query1 = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and (category <> 'chat') and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' )) and ( loginuser='" + currentLoginUserMail + "') and ( taskType='" + taskType + "') order by dateTime ASC LIMIT " + count + " OFFSET 0";
            } else {
                query1 = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and (category <> 'chat') and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' )) and ( loginuser='" + currentLoginUserMail + "') and ( taskType='" + taskType + "') order by dateTime ASC LIMIT 10 OFFSET " + count + "";
            }
            Log.d("TaskHistory", "query1 " + query1);
            if (VideoCallDataBase.getDB(context).getTaskHistoryInfo(query1) != null) {
                taskDetailsBeen1 = VideoCallDataBase.getDB(context).getTaskHistoryInfo(query1);
                Log.e("size", String.valueOf(taskDetailsBeen1.size()));
                for (TaskDetailsBean contactBean : taskDetailsBeen1) {
                    Log.i("task", "project 2");
                    Log.i("TaskHistory", "refresh 3" + " 1 " + " ********");
                    int unReadMsgCount = VideoCallDataBase.getDB(context).getTaskUnReadMsgCount(contactBean.getTaskId());
                    rem = VideoCallDataBase.getDB(context).getRemainderUnReadMsgCount(contactBean.getTaskId());
                    String taskFrom_name = VideoCallDataBase.getDB(context).getProjectParentTaskId("select fromUserName from taskDetailsInfo where taskId = '" + contactBean.getTaskId() + "' order by id desc LIMIT 1");
                    contactBean.setFromUserName(taskFrom_name);
                    Log.i("TaskHistory", "fromUserName " + contactBean.getFromUserName());
                    String taskTo_name = VideoCallDataBase.getDB(context).getProjectParentTaskId("select toUserName from taskDetailsInfo where taskId = '" + contactBean.getTaskId() + "' order by id desc LIMIT 1");
                    contactBean.setToUserName(taskTo_name);
                    Log.i("TaskHistory", "toUserName " + contactBean.getToUserName());
                    Log.d("TaskHistory", "unread count 0 " + unReadMsgCount);
                    int percentage = 0;
                    if (contactBean.getTaskType() != null && contactBean.getTaskType().equalsIgnoreCase("Group")) {
                        Log.d("TaskHistory", "Group percent 1 " + contactBean.getTaskType());
                        Log.d("TaskHistory", "unread count 3 " + unReadMsgCount);
                        if (contactBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            int a = 0;
                            String percent_sender = VideoCallDataBase.getDB(context).getlastCompletedParcentagesender(contactBean.getTaskId());
                            Log.i("TaskHistory", " groupname 31 " + percent_sender);
                            if (percent_sender != null && percent_sender.equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                if (VideoCallDataBase.getDB(context).getlastCompletedParcentage(taskDetailsBean.getTaskId()) != null)
                                    a = Integer.parseInt(VideoCallDataBase.getDB(context).getlastCompletedParcentage(taskDetailsBean.getTaskId()));
                                Log.i("TaskHistory", " groupname 32 " + a);
                            } else {
//                                Log.i("TaskHistory","Refresh **---->1  "+taskDetailsBean);
//                                Log.i("TaskHistory","Refresh **---->2  "+taskDetailsBean.getToUserId());
//                                Log.i("TaskHistory","Refresh **---->3 "+taskDetailsBean.getTaskId());
//                                Log.i("TaskHistory","Refresh **---->4 "+taskDetailsBean.getOwnerOfTask());
                                if (taskDetailsBean != null && taskDetailsBean.getToUserId() != null && !taskDetailsBean.getToUserId().equalsIgnoreCase("") && taskDetailsBean.getTaskId() != null && !taskDetailsBean.getTaskId().equalsIgnoreCase("") && taskDetailsBean.getOwnerOfTask() != null && !taskDetailsBean.getOwnerOfTask().equalsIgnoreCase("")) {
                                    a = VideoCallDataBase.getDB(context).GroupPercentageChecker(taskDetailsBean.getToUserId(), taskDetailsBean.getTaskId(), taskDetailsBean.getOwnerOfTask());
                                } else {
                                    a = 0;
                                }
                                Log.i("TaskHistory", " groupname 33 " + a);
                            }
                            percentage = a;
                        } else if (contactBean.getOwnerOfTask().equalsIgnoreCase(contactBean.getFromUserName())) {
                            Log.i("TaskHistory", " groupname 33 " + taskDetailsBean.getTaskId());
                            if (VideoCallDataBase.getDB(context).getlastCompletedParcentage(taskDetailsBean.getTaskId()) != null)
                                percentage = Integer.parseInt(VideoCallDataBase.getDB(context).getlastCompletedParcentage(taskDetailsBean.getTaskId()));

                        } else {
                            percentage = VideoCallDataBase.getDB(context).percentagechecker(contactBean.getTaskId());
                        }
                    } else {
                        percentage = VideoCallDataBase.getDB(context).percentagechecker(contactBean.getTaskId());
                    }
                    contactBean.setCompletedPercentage(String.valueOf(percentage));
                    contactBean.setRead_status(unReadMsgCount);
//                    if (taskDetailsBean != null && taskDetailsBean.getTaskStatus() != null) {
//                        contactBean.setTaskStatus(taskDetailsBean.getTaskStatus());
//                    }
                    contactBean.setMsg_status(rem);
                    Log.i("TaskHistory", "Refresh contactBean TaskName " + contactBean.getTaskName());
                    Log.i("TaskHistory", "Refresh contactBean fromUserName " + contactBean.getFromUserName());
                    Log.i("TaskHistory", "Refresh contactBean getOwnerOfTask " + contactBean.getOwnerOfTask());
                    Log.i("TaskHistory", "Refresh contactBean getToUserName " + contactBean.getToUserName());
                    Log.i("TaskHistory", "Refresh contactBean getTaskObservers " + contactBean.getTaskObservers());
                    Log.i("TaskHistory", "Refresh contactBean getTaskStatus " + contactBean.getTaskStatus());
                    taskDetailsBeen.add(contactBean);
                    taskDetailsBeanArraylist.add(contactBean);
                }
                Log.d("TaskHistory", "task list size = " + taskDetailsBeen.size());
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
                    }
                });
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
        Log.i("TaskHistory", "onResume is == 0 ");
        Log.i("DBQuery", "check " + check);
        Log.i("DBQuery", "check1111 " + check);
        if (check) {
            String query1 = "";
            qury = active_task.getText().toString();
            if (qury != null && qury.equalsIgnoreCase("Active Task List")) {
                qury = "Task&Issue";
            } else {
                qury = active_task.getText().toString();
            }
            Log.i("DBQuery", "qury " + qury);
            if (taskType != null && taskType.equalsIgnoreCase("Individual")) {
                if (qury.equalsIgnoreCase("inprogress")) {
                    Log.i("task", "filtered was onResume " + qury);
                    fill.setBackgroundResource(R.drawable.filterenabled);
                    query1 = "select * from taskHistoryInfo where (taskStatus ='" + qury + "') and  (( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' ) and ( taskType='" + taskType + "')  group by taskId";
                } else if (qury.equalsIgnoreCase("issue")) {
                    active_task.setText("Issues for task");
                    catagory = "issue";
                    query1 = "select * from taskHistoryInfo where (( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' )  and category = '" + catagory + "' and ( taskType='" + taskType + "')group by taskId";
                    Log.i("task", "filtered was 1*** onResume " + qury);
                } else if (qury.equalsIgnoreCase("Task&Issue")) {
                    oncreate_query = false;
                    String count_query = "select * from taskHistoryInfo where (( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' ) and ( taskType='" + taskType + "')";
                    int count = videoCallDataBase.getTaskHistoryRowCount(count_query);
                    Log.i("lazyload", "onresume count---> onResume " + count);
                    boolean countAboveten = false;
                    isscrool = true;
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
                    Log.i("lazyload", "onresume db count---> onResume " + taskList_count);
                    Log.i("lazyload", "onresume buddyOrgroup_count---> onResume " + taskList_count);
                    if (taskList_count == 0 && !countAboveten) {
                        query1 = "select * from taskHistoryInfo where (( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' ) and ( taskType='" + taskType + "')";
                    } else if (taskList_count < 10 && !countAboveten) {
                        query1 = "select * from taskHistoryInfo where (( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' ) and ( taskType='" + taskType + "') order by dateTime ASC LIMIT " + count + " OFFSET 0";
                    } else {
                        query1 = "select * from taskHistoryInfo where (( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' ) and ( taskType='" + taskType + "') order by dateTime ASC LIMIT 10 OFFSET " + count + "";
                    }
                    Log.i("task", "filtered was 2** onResume " + qury);
                } else if (!qury.equalsIgnoreCase("alltask")) {
                    Log.i("task", "filtered was 2*** onResume " + qury);
                    query1 = "select * from taskHistoryInfo where ( taskStatus = '" + qury + "' ) and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + taskType + "')  group by taskId";
                } else {
                    Log.i("task", "filtered was onResume " + qury);
                    fill.setBackgroundResource(R.drawable.filterenabled);
                    catagory = "Task";
                    fill.setBackgroundResource(R.drawable.filterdisabled);
                    query1 = "select * from taskHistoryInfo where ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )  or ( taskObservers='" + userName + "' ) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' ) and category = '" + catagory + "' and ( taskType='" + taskType + "')  group by taskId";
                }
            } else if (taskType != null && taskType.equalsIgnoreCase("Group")) {
                if (qury.equalsIgnoreCase("inprogress")) {
                    Log.i("task", "filtered was onResume " + qury);
                    fill.setBackgroundResource(R.drawable.filterenabled);
                    query1 = "select * from taskHistoryInfo where (taskStatus ='" + qury + "') and  ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' ) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' ) and ( taskType='" + taskType + "')  group by taskId";
                } else if (qury.equalsIgnoreCase("issue")) {
                    active_task.setText("Issues for task");
                    catagory = "issue";
                    query1 = "select * from taskHistoryInfo where ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' ) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' )  and category = '" + catagory + "' group by taskNo";
                    Log.i("task", "filtered was 1*** onResume " + qury);
                } else if (qury.equalsIgnoreCase("Task&Issue")) {
                    catagory = "issue";
                    oncreate_query = false;
                    String count_query = "select * from taskHistoryInfo where ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' ) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' )";
                    int count = videoCallDataBase.getTaskHistoryRowCount(count_query);
                    Log.i("lazyload", "onresume count--->" + count);
                    boolean countAboveten = false;
                    isscrool = true;
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
                    Log.i("lazyload", "onresume db count---> onResume " + taskList_count);
                    Log.i("lazyload", "onresume buddyOrgroup_count---> onResume " + taskList_count);
                    if (taskList_count == 0 && !countAboveten) {
                        query1 = "select * from taskHistoryInfo where ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' ) and (category <> 'chat') and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' )";
                    } else if (taskList_count < 10 && !countAboveten) {
                        query1 = "select * from taskHistoryInfo where ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' ) and (category <> 'chat') and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' ) order by dateTime ASC LIMIT " + count + " OFFSET 0";
                    } else {
                        query1 = "select * from taskHistoryInfo where ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' ) and (category <> 'chat') and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' ) order by dateTime ASC LIMIT 10 OFFSET " + count + "";
                    }
                    Log.i("task", "filtered was 2** onResume " + qury);
                } else if (!qury.equalsIgnoreCase("alltask"))
                    query1 = "select * from taskHistoryInfo where ( taskStatus = '" + qury + "' ) and (category <> 'chat') and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + taskType + "')  group by taskId";
                else {
                    catagory = "issue";
                    query1 = "select * from taskHistoryInfo where ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' ) or ( taskObservers='" + userName + "' )) and (category <> 'chat') and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + taskType + "')  and (projectId IS NULL or projectId = 'null' or projectId = '(null)') group by taskId";
                }
            }

            taskDetailsBeen.clear();
            taskDetailsBeen1.clear();
            taskDetailsBeen2.clear();
            taskDetailsBeanArraylist.clear();
            Log.d("task", "query onResume " + query1);
            taskDetailsBeen1 = VideoCallDataBase.getDB(context).getTaskHistoryInfo(query1);
            Log.e("size", "onResume " + String.valueOf(taskDetailsBeen1.size()));
            Log.i("task", "size was onResume " + taskDetailsBeanArraylist.size());
            taskDetailsBeen2.addAll(taskDetailsBeen1);
            taskDetailsBeanArraylist.addAll(taskDetailsBeen1);
            Log.d("Last Status", "value onResume " + taskDetailsBeen2.size());
            Log.d("Last Status", "value onResume " + taskDetailsBeanArraylist.size());
            for (TaskDetailsBean contactBean : taskDetailsBeen2) {
                Log.i("task", "project 1 onResume ");
                Log.i("TaskHistory", "onResume 3" + " 1  onResume " + " ********");
                int unReadMsgCount = VideoCallDataBase.getDB(context).getTaskUnReadMsgCount(contactBean.getTaskId());
                rem = VideoCallDataBase.getDB(context).getRemainderUnReadMsgCount(contactBean.getTaskNo());
                Log.d("task1", "unread count onResume " + unReadMsgCount);
                int percentage = 0;
                Log.d("task1", "unread count onResume " + unReadMsgCount + contactBean.getToUserName() + contactBean.getTaskId() + contactBean.getOwnerOfTask() + " " + contactBean.getTaskType());
                String taskFrom_name = VideoCallDataBase.getDB(context).getProjectParentTaskId("select fromUserName from taskDetailsInfo where taskId = '" + contactBean.getTaskId() + "' order by id desc LIMIT 1");
                contactBean.setFromUserName(taskFrom_name);
                Log.i("TaskHistory", "fromUserName  onResume " + contactBean.getFromUserName());
                String taskTo_name = VideoCallDataBase.getDB(context).getProjectParentTaskId("select toUserName from taskDetailsInfo where taskId = '" + contactBean.getTaskId() + "' order by id desc LIMIT 1");
                contactBean.setToUserName(taskTo_name);
                Log.i("TaskHistory", "toUserName onResume " + contactBean.getToUserName());
                Log.d("TaskHistory", "TaskName onResume " + contactBean.getTaskName());
                if (contactBean.getTaskType() != null && contactBean.getTaskType().equalsIgnoreCase("Group")) {
                    Log.d("TaskHistory", "Group percent 11 onResume " + contactBean.getTaskType());
                    if (contactBean.getOwnerOfTask() != null && contactBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        Log.d("TaskHistory", "unread count 21 onResume " + unReadMsgCount);
                        if (contactBean.getFromUserName() != null && contactBean.getFromUserName().equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                            Log.d("TaskHistory", "unread count 31 onResume " + unReadMsgCount);
                            percentage = Integer.parseInt(VideoCallDataBase.getDB(context).getlastCompletedParcentage(contactBean.getTaskId()));
                        } else {
                            percentage = VideoCallDataBase.getDB(context).GroupPercentageChecker(contactBean.getToUserName(), contactBean.getTaskId(), contactBean.getOwnerOfTask());
                            Log.i("TaskHistory", "unread count 41 onResume " + percentage);
                        }
                    } else {
                        if (contactBean.getFromUserName() != null && contactBean.getFromUserName().equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                            Log.d("TaskHistory", "unread count 51 onResume " + unReadMsgCount);
                            if (contactBean.getCompletedPercentage() != null && !contactBean.getCompletedPercentage().equalsIgnoreCase(""))
                                percentage = Integer.parseInt(contactBean.getCompletedPercentage());
                            else
                                percentage = 0;
                        } else {
                            Log.d("TaskHistory", "unread count 61 onResume " + unReadMsgCount);
                            String TakerPercent_value = VideoCallDataBase.getDB(context).getTakerlastCompletedParcentage(contactBean.getTaskId());
                            if (TakerPercent_value != null)
                                percentage = Integer.parseInt(TakerPercent_value);
                        }
                    }
                } else {
                    percentage = VideoCallDataBase.getDB(context).percentagechecker(contactBean.getTaskId());
                }
                contactBean.setCompletedPercentage(String.valueOf(percentage));
                contactBean.setRead_status(unReadMsgCount);
                contactBean.setMsg_status(rem);
                if (contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("completed")) {
                    reopen_qry = "select taskId from taskHistoryInfo where (parentTaskId ='" + contactBean.getTaskId() + "') and taskStatus != 'closed'";
                    Log.d("DBQuery", "reopen_qry--> onResume " + reopen_qry);
                    Log.d("DBQuery", "reopen_qry boolean --> onResume" + videoCallDataBase.isAgendaRecordExists(reopen_qry));
                    if (videoCallDataBase.isAgendaRecordExists(reopen_qry)) {
                        contactBean.setTaskStatus("Reopen");
                    }
                }
                taskDetailsBeen.add(contactBean);
                taskDetailsBeanArraylist.add(contactBean);
            }
            Log.d("task", "task list size = onResume " + taskDetailsBeen.size());
            class StringDateComparator implements Comparator<TaskDetailsBean> {
                String date_lhs = null;
                String date_rhs = null;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                public int compare(TaskDetailsBean lhs, TaskDetailsBean rhs) {
                    try {
                        Log.i("DateFormat", "leftside onResume " + lhs.getDateTime());
                        Log.i("DateFormat", "rightside onResume" + rhs.getDateTime());
                        date_lhs = lhs.getDateTime();
                        date_rhs = rhs.getDateTime();
                        Log.i("DateFormet", "ListPosition onResume " + taskDetailsBeen.get(1).getTaskDescription());
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
            handler.post(new Runnable() {
                @Override
                public void run() {
                    buddyArrayAdapter.notifyDataSetChanged();
                }
            });
            check = false;
        }
    }

    public void sendMultiInstantMessage(String msgBody, String[] userlist) {
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

    @Override
    public void ResponceMethod(final Object object) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.i("currentstatus", "Task Details Respons method");
                CommunicationBean communicationBean = (CommunicationBean) object;
                NewTaskConversation newTaskConversation = new NewTaskConversation();
                JSONObject jsonObject = null;
                String str1 = communicationBean.getFirstname();
                try {
                    jsonObject = new JSONObject(communicationBean.getEmail());
                    Log.i("jsonwebservice", communicationBean.getEmail());
                    if ((jsonObject.has("result_code") && (int) jsonObject.get("result_code") == 0) && (str1 != null && !str1.equalsIgnoreCase("getTask"))) {
                        TaskDetailsBean task = communicationBean.getTaskDetailsBean();
                        Log.e("current status", task.getTaskStatus());
                        if (task.getTaskStatus() != null && task.getTaskStatus().equalsIgnoreCase("abandoned")) {
                            Log.i("Status ", ">>0 " + task.getTaskStatus());
                            Log.e("Status Updated", "set abandoned");
                            taskDetailsBean.setTaskDescription("This task is abandoned");
                            Log.i("Status ", ">>1");
                            newTaskConversation.taskReminderMessage(taskDetailsBean, null, "text", null, null);
                            String xml = composeChatXML(taskDetailsBean);
                            Log.i("Status ", ">>2");
                            String Query = "Select * from taskDetailsInfo where mimeType='observer' and taskId ='" + task.getTaskId() + "'";
                            ArrayList<TaskDetailsBean> taskDetailsBean5 = VideoCallDataBase.getDB(context).getTaskHistory(Query);
                            String[] observerList = null;
                            sendInstantMessage(xml);
                            if (taskDetailsBean5.size() > 0) {
                                TaskDetailsBean taskDetailsBean1 = taskDetailsBean5.get(taskDetailsBean5.size() - 1);
                                observerList = taskDetailsBean1.getTaskObservers().split(",");
                                sendMultiInstantMessage(xml, observerList);
                            }
                            Log.i("Status ", ">>3");
                            String query = "update taskDetailsInfo  set taskStatus = 'abandoned' where ('" + taskDetailsBean.getTaskId() + "'= taskId ) ;";
                            String query_1 = "update taskHistoryInfo  set taskStatus = 'abandoned' where ('" + taskDetailsBean.getTaskId() + "'= taskId ) ;";
                            VideoCallDataBase.getDB(context).updateaccept(query_1);
                            Log.i("Status ", ">>4");
                            VideoCallDataBase.getDB(context).getTaskHistory(query);
                            buddyArrayAdapter.notifyDataSetChanged();
                            Log.i("Status ", ">>5");
                        } else {
                            Log.e("Status Updated", "set inprogress");
                            taskDetailsBean.setTaskDescription("This task is activated");
                            Log.i("Status ", "--->0");
                            newTaskConversation.taskReminderMessage(taskDetailsBean, null, "text", null, null);
                            String xml = composeChatXML(taskDetailsBean);
                            Log.i("Status ", "--->1");
                            String Query = "select * from taskDetailsInfo where mimeType='observer' and taskId ='" + task.getTaskId() + "'";
                            ArrayList<TaskDetailsBean> taskDetailsBean5 = VideoCallDataBase.getDB(context).getTaskHistory(Query);
                            String[] observerList = null;
                            sendInstantMessage(xml);
                            if (taskDetailsBean5.size() > 0) {
                                TaskDetailsBean taskDetailsBean1 = taskDetailsBean5.get(taskDetailsBean5.size() - 1);
                                observerList = taskDetailsBean1.getTaskObservers().split(",");
                                sendMultiInstantMessage(xml, observerList);
                            }
                            Log.i("Status ", "--->2");
                            String query = "update taskDetailsInfo  set taskStatus = 'inprogress' where ('" + taskDetailsBean.getTaskId() + "'= taskId );";
                            String query_1 = "update taskHistoryInfo  set taskStatus = 'inprogress' where ('" + taskDetailsBean.getTaskId() + "'= taskId );";
                            VideoCallDataBase.getDB(context).updateaccept(query_1);
                            Log.i("Status ", "--->3");
                            VideoCallDataBase.getDB(context).getTaskHistory(query);
                            Log.i("Status ", "--->4");
                            buddyArrayAdapter.notifyDataSetChanged();
                        }
                        Log.i("Status Updated", "set " + communicationBean.getEmail());
                        Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.get("id").toString().equals(webTaskId) && (str1 != null && str1.equalsIgnoreCase("getTask"))) {
                        Gson gson = new Gson();
                        Log.i("getTask", "jelement.getAsJsonObject() != null");
                        ListAllgetTaskDetails listAllgetTaskDetails = gson.fromJson(jsonObject.toString(), ListAllgetTaskDetails.class);
                        Log.d("getTask", "firstname  " + listAllgetTaskDetails.getParentId());
                        Log.d("getTask", "firstname  " + listAllgetTaskDetails.getId());
                        Log.d("getTask", "firstname  " + listAllgetTaskDetails.getDescription());
                        Log.d("++", "firstname  " + listAllgetTaskDetails.getListObserver());
                        VideoCallDataBase.getDB(context).deleteTemplateEntry(webTaskId);
                        VideoCallDataBase.getDB(context).insertORupdate_ListAllgetTaskDetails(listAllgetTaskDetails);
                        if (getResources().getString(R.string.task_enable).equalsIgnoreCase("enable") && !taskType.equalsIgnoreCase("Group")) {
                            VideoCallDataBase.getDB(context).taskStatusUpdate("assigned", webTaskId);
                            Log.d("Template", "accept");
                        } else {
                            VideoCallDataBase.getDB(context).taskStatusUpdate("inprogress", webTaskId);
                            Log.d("Template", "inprogress");
                        }
                        load();
                        cancelDialog();
                    } else {
                        Toast.makeText(TaskHistory.this, "Not Updated network error", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(TaskHistory.this, "Json Error response", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void ErrorMethod(Object object) {
    }

    public void load() {
        Intent intent = new Intent(context, NewTaskConversation.class);
        intent.putExtra("task", "taskhistory");
        intent.putExtra("taskHistoryBean", taskDetailsBean);
        Log.i("TaskHistory", "Load()-----> Task Status " + taskDetailsBean.getTaskStatus());
        startActivity(intent);
        check = true;
    }

    public String composeChatXML(TaskDetailsBean cmbean) {
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
            buffer.append("</TaskDetailsinfo>");
            Log.e("xml", "composed xml for chat======>" + buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return buffer.toString();
        }
    }

    public void sendInstantMessage(String msgBody) {

        for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
            String name = MainActivity.account.buddyList.get(i).cfg.getUri();
            Log.i("task", "buddyname-->" + name);
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
                }
                break;
            }
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
                if (contactBean.getOwnerOfTask() != null && contactBean.getTaskStatus() != null) {
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
                        } else if (contactBean.getTaskStatus().equalsIgnoreCase("inprogress")) {
                            value = 4;
                            Log.d("swipe", "inprogress else 4* ");
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
                        } else if (contactBean.getTaskStatus().equalsIgnoreCase("inprogress")) {
                            value = 1;
                            Log.d("swipe", "inprogress else 1* ");
                        }
                    }
                } else if (contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("Draft")) {
                    value = 2;
                } else {
                    value = 0;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return value;
        }

        public int getViewTypeCount() {
            return 6;
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
                TextView task_giver = (TextView) conView.findViewById(R.id.task_giver);
                ImageView imageView = (ImageView) conView.findViewById(R.id.state);
                TextView msg_count = (TextView) conView.findViewById(R.id.item_counter);
                TextView task_taker = (TextView) conView.findViewById(R.id.task_taker);
                TextView task_observer = (TextView) conView.findViewById(R.id.task_observer);
                TextView taskName = (TextView) conView.findViewById(R.id.task_name);
                TextView remain = (TextView) conView.findViewById(R.id.remain_count);
                TextView percen = (TextView) conView.findViewById(R.id.percent_update);
                ImageView dependency_icon = (ImageView) conView.findViewById(R.id.dependency_icon);
                TextView task_status = (TextView) conView.findViewById(R.id.task_status);
                TextView catagory = (TextView) conView.findViewById(R.id.catagory);
                ImageView selected = (ImageView) conView.findViewById(R.id.selected);
                TextView exclation_counter = (TextView) conView.findViewById(R.id.exclation_counter);
                conView.setBackgroundResource(R.color.white);
                task_observer.setVisibility(View.VISIBLE);
                task_taker.setVisibility(View.VISIBLE);

                try {
                    String s = "select * from taskDetailsInfo where taskId='" + contactBean.getTaskId() + "' and readStatus='1'";
                    ArrayList<ProjectDetailsBean> projectDetailsBeen = VideoCallDataBase.getDB(context).getExclationdetails(s);
                    if (projectDetailsBeen.size() > 0) {
                        exclation_counter.setVisibility(View.VISIBLE);
                        conView.setBackgroundResource(R.color.lgyellow);
                    } else
                        exclation_counter.setVisibility(View.GONE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("TaskArrayAdapter", "listname ** " + contactBean.getTaskStatus() + "," + contactBean.getCatagory() + "," + contactBean.getTaskName());
                if (contactBean.getCompletedPercentage() != null && !contactBean.getCompletedPercentage().equalsIgnoreCase("") && !contactBean.getCompletedPercentage().equalsIgnoreCase(null)) {
                    Log.i("TaskArrayAdapter", "percentage contactBean.getCompletedPercentage() " + contactBean.getCompletedPercentage());
                    if (contactBean.getCompletedPercentage().equalsIgnoreCase("100")) {
                        percen.setTextColor(getResources().getColor(R.color.green));
                        Log.d("TaskArrayAdapter", "percentage green");
                    } else {
                        percen.setTextColor(getResources().getColor(R.color.red));
                        Log.d("TaskArrayAdapter", "percentage red");
                    }
                }
                if (contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("overdue")) {
                    task_status.setText("Overdue");
                    conView.setBackgroundResource(R.color.red_register);
                } else if (contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("Reopen")) {
                    task_status.setText("Reopen");
                    Log.i("popup", "Reopen status " + contactBean.getTaskStatus());
                } else if ((contactBean.getCompletedPercentage() != null && (contactBean.getCompletedPercentage().equalsIgnoreCase("100%") || contactBean.getCompletedPercentage().equalsIgnoreCase("100")))) {
                    if (contactBean.getTaskStatus().equalsIgnoreCase("Closed")) {
                        task_status.setText("Closed");
                        percen.setTextColor(getResources().getColor(R.color.green));
                        Log.i("popup", "status" + contactBean.getTaskName());
                        Log.i("popup", "status 1" + contactBean.getTaskStatus());
                    } else if (contactBean.getTaskStatus().equalsIgnoreCase("Completed")) {
                        if (contactBean.getTaskStatus().equalsIgnoreCase("completed")) {
                            reopen_qry = "select taskId from taskHistoryInfo where (parentTaskId ='" + contactBean.getTaskId() + "') and taskStatus != 'closed'";
                            Log.d("DBQuery", "reopen_qry--> " + reopen_qry);
                            Log.d("DBQuery", "reopen_qry boolean --> " + videoCallDataBase.isAgendaRecordExists(reopen_qry));
                            if (videoCallDataBase.isAgendaRecordExists(reopen_qry)) {
                                task_status.setText("Reopen");
//                                contactBean.setTaskStatus("Reopen");
                            } else {
                                task_status.setText("Completed");
                            }
                        }
                        percen.setTextColor(getResources().getColor(R.color.green));
                        Log.i("popup", "status" + contactBean.getTaskName());
                        Log.i("popup", "status 1" + contactBean.getTaskStatus());
                    } else if (contactBean.getTaskStatus().equalsIgnoreCase("Reopen")) {
                        task_status.setText("Reopen");
                        Log.i("popup", "Reopen status **  " + contactBean.getTaskStatus());
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
                Log.i("TaskHistory", "listname " + contactBean.getTaskStatus() + "," + contactBean.getCatagory() + "," + contactBean.getTaskName());
                if (contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("Issue")) {
                    selected.setBackgroundResource(R.drawable.issue_icon);
                    catagory.setText("Issue Id: " + contactBean.getTaskId());
                    taskName.setText("Issue Name: " + contactBean.getTaskName());
                } else if (contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("Task")) {
                    selected.setBackgroundResource(R.drawable.task_icon);
                    catagory.setText("Task Id: " + contactBean.getTaskId());
                    taskName.setText("Task Name: " + contactBean.getTaskName());
                } else if (contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("note")) {
                    selected.setBackgroundResource(R.drawable.ic_note_32_2);
                    catagory.setText("Me Id: " + contactBean.getTaskId());
                    taskName.setText("Me Name: " + contactBean.getTaskName());
                } else {
                    selected.setBackgroundResource(R.drawable.task_icon);
                    catagory.setText("Task Id: " + contactBean.getTaskId());
                    taskName.setText("Task Name: " + contactBean.getTaskName());
                }
                Log.d("TaskArrayAdapter", "contactBean.getRead_status()  adapter" + contactBean.getRead_status());
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
                String taskId = contactBean.getTaskId();
                taskId = VideoCallDataBase.getDB(context).getOverdue(taskId);
                if (contactBean.getTaskType() != null && !contactBean.getTaskType().equalsIgnoreCase("Group")) {
                    if (contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("overdue")) {
                        task_status.setText("Overdue");
                        conView.setBackgroundResource(R.color.red_register);
                    } else if (contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("assigned")) {
                        task_status.setText("assigned");
                        Log.i("Accept", "task_nistory " + task_status);
                    } else if (contactBean.getTaskStatus() != null && (contactBean.getTaskStatus().equalsIgnoreCase("closed") || contactBean.getTaskStatus().equalsIgnoreCase("Completed"))) {
                        if (contactBean.getTaskStatus().equalsIgnoreCase("Completed")) ;
                    } else if (contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("Reopen")) {
                        task_status.setText("Reopen");
                    } else if (taskId != null && (taskId.equalsIgnoreCase("closed") || taskId.equalsIgnoreCase("Completed"))) {
                        if (taskId.equalsIgnoreCase("Completed")) {
                            if (taskId.equalsIgnoreCase("completed")) {
                                reopen_qry = "select taskId from taskHistoryInfo where (parentTaskId ='" + contactBean.getTaskId() + "') and taskStatus != 'closed'";
                                Log.d("DBQuery", "reopen_qry--> " + reopen_qry);
                                Log.d("DBQuery", "reopen_qry boolean --> " + videoCallDataBase.isAgendaRecordExists(reopen_qry));
                                if (videoCallDataBase.isAgendaRecordExists(reopen_qry)) {
                                    task_status.setText("Reopen");
                                } else {
                                    task_status.setText("Completed");
                                }
                            }
                        } else
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
                        conView.setBackgroundResource(R.color.red_register);
                    } else if (contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("closed")) {
                        task_status.setText(contactBean.getTaskStatus());
                        percen.setTextColor(getResources().getColor(R.color.green));
                    } else {
                        task_status.setText(contactBean.getTaskStatus());
                        percen.setTextColor(getResources().getColor(R.color.red));
                    }
                }
                if (contactBean.getTaskType() != null && contactBean.getTaskType().equalsIgnoreCase("Group")) {
                    if (contactBean.getCompletedPercentage() != null && !contactBean.getCompletedPercentage().equalsIgnoreCase("") && !contactBean.getCompletedPercentage().equalsIgnoreCase(null) && !contactBean.getCompletedPercentage().equalsIgnoreCase("(null)")) {
                        Log.i("TaskArrayAdapter", "GroupPercentage " + contactBean.getTaskDescription());
                        if (contactBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            int a;
                            if (VideoCallDataBase.getDB(context).getProjectParentTaskId("select fromUserName from taskDetailsInfo where signalid='" + contactBean.getSignalid() + "'").equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                a = VideoCallDataBase.getDB(context).groupPercentageStatus(Appreference.loginuserdetails.getUsername(), contactBean.getTaskId());
                            } else {
                                a = VideoCallDataBase.getDB(context).GroupPercentageChecker(group_id, contactBean.getTaskId(), contactBean.getOwnerOfTask());
                            }
                            Log.i("TaskArrayAdapter", "GroupPercentage getCompletedPercentage " + contactBean.getCompletedPercentage());
                            if (contactBean.getCompletedPercentage() != null && !contactBean.getCompletedPercentage().equalsIgnoreCase("") && !contactBean.getCompletedPercentage().equalsIgnoreCase(null)) {
                                percen.setText(contactBean.getCompletedPercentage() + "%");
                            } else {
                                percen.setText("0%");
                            }
                            Log.e("TaskArrayAdapter", "GroupPercentage giver taskName and percentage ==> " + contactBean.getTaskName() + " " + contactBean.getCompletedPercentage());
                        } else {
                            int a = VideoCallDataBase.getDB(context).groupPercentageStatus(Appreference.loginuserdetails.getUsername(), contactBean.getTaskId());
                            if (a == 0) {
                                a = VideoCallDataBase.getDB(context).groupPercentageStatus(contactBean.getOwnerOfTask(), contactBean.getTaskId());
                            }
                            if (contactBean.getCompletedPercentage() != null && !contactBean.getCompletedPercentage().equalsIgnoreCase("null")) {
                                percen.setText(contactBean.getCompletedPercentage() + "%");
                            } else {
                                percen.setText("0%");
                            }
                            Log.e("TaskArrayAdapter", "GroupPercentage taker ====>" + contactBean.getCompletedPercentage());
                            if (contactBean.getCompletedPercentage() != null && !contactBean.getCompletedPercentage().equalsIgnoreCase("") && !contactBean.getCompletedPercentage().equalsIgnoreCase(null) && !contactBean.getCompletedPercentage().equalsIgnoreCase("(null)") && contactBean.getCompletedPercentage().equalsIgnoreCase("100")) {
                                if (contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("Overdue")) {
                                    task_status.setText("Overdue");
                                    conView.setBackgroundResource(R.color.red_register);
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
                        percen.setText("0%");
                    }
                } else {
                    if (contactBean.getCompletedPercentage() != null && !contactBean.getCompletedPercentage().equalsIgnoreCase("") && !contactBean.getCompletedPercentage().equalsIgnoreCase(null) && !contactBean.getCompletedPercentage().equalsIgnoreCase("(null)") && !contactBean.getCompletedPercentage().equalsIgnoreCase("null")) {
                        percen.setText(contactBean.getCompletedPercentage() + "%");
                    } else {
                        percen.setText("0%");
                    }
                }
                String s = null;
                if (contactBean.getOwnerOfTask() != null) {
                    if (contactBean.getOwnerOfTask().equals(Appreference.loginuserdetails.getUsername())) {
                        remain.setVisibility(View.GONE);
                        Log.i("TaskArrayAdapter", "OwnerOftask---------> 1 *  " + contactBean.getGroupTaskMembers());
                        task_giver.setText("Giver : " + "Me");
                        String str = contactBean.getGroupTaskMembers();
                        String name = VideoCallDataBase.getDB(context).getname(str);
                        String str2 = contactBean.getTaskObservers();
                        if (contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("note")) {
                            if (contactBean.getTaskReceiver() != null && !contactBean.getTaskReceiver().equalsIgnoreCase(contactBean.getOwnerOfTask()) && contactBean.getTaskObservers() != null && !contactBean.getTaskObservers().equalsIgnoreCase("")) {
                                task_taker.setVisibility(View.VISIBLE);
                                String name_receiver = VideoCallDataBase.getDB(context).getname(contactBean.getTaskReceiver());
                                Log.i("TaskArrayAdapter", "OwnerOftask---------> 1 note " + contactBean.getGroupTaskMembers());
                                if (name_receiver != null) {
                                    task_taker.setText("Taker : " + name_receiver);
                                } else {
                                    task_taker.setText("Taker : " + contactBean.getTaskReceiver());
                                }
                            } else {
                                task_taker.setVisibility(View.GONE);
                            }
                        } else if (!contactBean.getTaskType().equalsIgnoreCase("group")) {
                            task_taker.setVisibility(View.VISIBLE);
                            String receiver = contactBean.getTaskReceiver();
                            String name_receiver = VideoCallDataBase.getDB(context).getname(receiver);
                            Log.i("TaskArrayAdapter", "OwnerOftask---------> 1 individual  " + contactBean.getGroupTaskMembers());
                            if (name_receiver != null) {
                                task_taker.setText("Taker : " + name_receiver);
                            } else {
                                task_taker.setText("Taker : " + receiver);
                            }
                        } else {
                            if (str != null && !str.equalsIgnoreCase("null") && !str.equalsIgnoreCase("") && !str.equalsIgnoreCase("(null)")) {
                                task_taker.setVisibility(View.VISIBLE);
                                String observernamelist = "", task_obsname = "";
                                int counter = 0;
                                for (int i = 0; i < str.length(); i++) {
                                    if (str.charAt(i) == ',') {
                                        counter++;
                                        Log.i("TaskArrayAdapter", "OwnerOftask---------> 1 group ** " + counter);
                                    }
                                }
                                for (int j = 0; j < counter + 1; j++) {
                                    String Mem_name = str.split(",")[j];
                                    Log.i("TaskArrayAdapter", "OwnerOftask---------> 1 group !#! " + Mem_name);
                                    if (!Mem_name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                        String Name = VideoCallDataBase.getDB(context).getName(Mem_name);
                                        observernamelist = observernamelist + Name + ",";
                                    } else {
                                        observernamelist = observernamelist;
                                    }
                                }
                                observernamelist = observernamelist.substring(0, observernamelist.length() - 1);
                                Log.i("TaskArrayAdapter", "OwnerOftask---------> 1 group () " + observernamelist);
                                task_taker.setText("Taker :" + observernamelist);
                            }
                        }
                        if (str2 != null && !str2.equalsIgnoreCase("null") && !str2.equalsIgnoreCase("") && !str2.equalsIgnoreCase("(null)")) {
                            int counter = 0;
                            String observernamelist = "", task_obsname = "";
                            for (int i = 0; i < str2.length(); i++) {
                                if (str2.charAt(i) == ',') {
                                    counter++;
                                    Log.i("TaskArrayAdapter", "OwnerOftask---------> 1 group ** " + counter);
                                }
                            }
                            for (int j = 0; j < counter + 1; j++) {
                                String Mem_name = str2.split(",")[j];
                                Log.i("TaskArrayAdapter", "OwnerOftask---------> 1 group !#! " + Mem_name);
                                if (!Mem_name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    String Name = VideoCallDataBase.getDB(context).getName(Mem_name);
                                    observernamelist = observernamelist + Name + ",";
                                } else {
                                    observernamelist = observernamelist;
                                }
                            }
                            observernamelist = observernamelist.substring(0, observernamelist.length() - 1);
                            Log.i("TaskList", "str2 * " + str2);
                            Log.i("TaskArrayAdapter", "OwnerOftask---------> 1 observerlist " + observernamelist);
                            Log.i("TaskObserverList", "OwnerOftask---------> 1 observerlist " + observernamelist);
                            Log.i("TaskObserverList", "OwnerOftask---------> 2 str2 " + str2);
                            task_observer.setText("Observer :" + observernamelist);
                        } else if ((contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("note")) || (contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("note"))) {
                            task_observer.setVisibility(View.GONE);
                        } else {
                            Log.i("getView", "owner 11 ");
                            task_observer.setText("Observer :" + " NA");
                            Log.i("TaskList", "observernamelist  giverside ** ");
                        }
                        imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_arrow_18_16));
                    } else if (contactBean.getTaskReceiver() != null && contactBean.getTaskReceiver().equals(Appreference.loginuserdetails.getUsername())) {
                        String str1 = contactBean.getOwnerOfTask();
                        String name1 = VideoCallDataBase.getDB(context).getname(str1);
                        String str2 = contactBean.getTaskObservers();
                        task_taker.setText("Taker : " + " Me");
                        Log.i("TaskArrayAdapter", "TaskReceiver---------> 2 " + str2);

//                        Log.i("getView","taker 12 ");
                        if (name1 != null && !name1.equalsIgnoreCase("")) {
                            task_giver.setText("Giver : " + name1);
                        } else {
                            task_giver.setText("Giver : " + str1);
                        }
                        Log.i("TaskList", "str2 ** " + str2);
                        if (str2 != null && !str2.equalsIgnoreCase("null") && !str2.equalsIgnoreCase("") && !str2.equalsIgnoreCase("(null)")) {
                            int counter = 0;
                            String observernamelist = "", task_obsname = "";
                            for (int i = 0; i < str2.length(); i++) {
                                if (str2.charAt(i) == ',') {
                                    counter++;
                                    Log.i("TaskArrayAdapter", "OwnerOftask---------> 1 group ** " + counter);
                                }
                            }
                            for (int j = 0; j < counter + 1; j++) {
                                String Mem_name = str2.split(",")[j];
                                Log.i("TaskArrayAdapter", "OwnerOftask---------> 1 group !#! " + Mem_name);
                                if (!Mem_name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    if (!Mem_name.equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                        String Name = VideoCallDataBase.getDB(context).getName(Mem_name);
                                        observernamelist = observernamelist + Name + ",";
                                    }
                                } else {
                                    observernamelist = "Me," + observernamelist;
                                }
                            }
                            observernamelist = observernamelist.substring(0, observernamelist.length() - 1);
                            Log.i("TaskList", "observernamelist 1 " + observernamelist);
                            Log.i("TaskList", "str2 * " + str2);
//                            Log.i("getView","taker 13 ");
                            Log.i("TaskArrayAdapter", "TaskReceiver---------> 2 observernamelist " + observernamelist);
                            Log.i("TaskObserverList", "TaskReceiver---------> 1 observerlist " + observernamelist);
                            Log.i("TaskObserverList", "TaskReceiver---------> 2 str2 " + str2);
                            task_observer.setText("Observer :" + observernamelist);
                            Log.i("TaskList", "observernamelist takerside * " + observernamelist);
                        } else if ((contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("note")) || (contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("note"))) {
                            task_observer.setVisibility(View.GONE);
                        } else {
//                            Log.i("getView","taker 14 ");
                            task_observer.setText("Observer :" + " NA");
                            Log.i("TaskList", "observernamelist  takerside ** ");
                        }
                        imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_arrow_118_16));
                    } else if (contactBean.getTaskObservers() != null && contactBean.getTaskObservers().equals(Appreference.loginuserdetails.getUsername())) {
                        String str = contactBean.getGroupTaskMembers();
//                        Log.i("getView","observer 15 ");
                        Log.i("TaskArrayAdapter", "TaskObservers---------> 3 " + str);
                        String name = VideoCallDataBase.getDB(context).getname(str);
                        String str1 = contactBean.getOwnerOfTask();
                        String name1 = VideoCallDataBase.getDB(context).getname(str1);
                        String str2 = contactBean.getTaskObservers();
                        task_giver.setText("Giver : " + name1);
                        Log.i("TaskList", "str2 *** " + str2);
                        if (contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("note") && contactBean.getOwnerOfTask() != null && contactBean.getOwnerOfTask().equalsIgnoreCase(contactBean.getTaskReceiver())) {
                            task_taker.setVisibility(View.GONE);
                        } else if (!contactBean.getTaskType().equalsIgnoreCase("group")) {
//                            Log.i("getView","observer 16 ");
                            if (contactBean.getTaskReceiver() != null && !contactBean.getTaskReceiver().equalsIgnoreCase("") && contactBean.getTaskReceiver().equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                task_taker.setVisibility(View.GONE);
                            } else {
                                task_taker.setVisibility(View.VISIBLE);
                            }
//                            task_taker.setVisibility(View.VISIBLE);
                            String receiver = contactBean.getTaskReceiver();
                            String name_receiver = VideoCallDataBase.getDB(context).getname(receiver);
                            Log.i("TaskArrayAdapter", "TaskObservers---------> 3 individual " + receiver);
                            if (receiver != null) {
//                                Log.i("getView","observer 17 ");
                                task_taker.setText("Taker : " + name_receiver);
                            } else {
//                                Log.i("getView","observer 18 ");
                                task_taker.setText("Taker : " + receiver);
                            }
                        } else {
                            if (str != null && !str.equalsIgnoreCase("null") && !str.equalsIgnoreCase("") && !str.equalsIgnoreCase("(null)")) {
                                task_taker.setVisibility(View.VISIBLE);
                                int counter = 0;
                                String observernamelist = "", task_obsname = "";
                                for (int i = 0; i < str.length(); i++) {
                                    if (str.charAt(i) == ',') {
                                        counter++;
                                        Log.i("TaskArrayAdapter", "OwnerOftask---------> 1 group ** " + counter);
                                    }
                                }
                                for (int j = 0; j < counter + 1; j++) {
                                    String Mem_name = str.split(",")[j];
                                    Log.i("TaskArrayAdapter", "OwnerOftask---------> 1 group !#! " + Mem_name);
                                    if (!Mem_name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                        if (!Mem_name.equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                            String Name = VideoCallDataBase.getDB(context).getName(Mem_name);
                                            observernamelist = observernamelist + Name + ",";
                                        }
                                    } else {
                                        observernamelist = "Me," + observernamelist;
                                    }
                                }
                                observernamelist = observernamelist.substring(0, observernamelist.length() - 1);
                                Log.i("TaskList", "observernamelist 1 " + observernamelist);
                                Log.i("TaskList", "str2 * " + str);
//                                Log.i("getView","observer 19 ");
                                Log.i("TaskArrayAdapter", "TaskObservers---------> 3 group " + observernamelist);
                                task_taker.setText("Taker :" + observernamelist);
                                Log.i("TaskList", "observernamelist giverside * " + observernamelist);
                            }
                        }
                        if (str2 != null && !str2.equalsIgnoreCase("") && !str2.equalsIgnoreCase("null") && !str2.equalsIgnoreCase("(null)")) {
                            int counter = 0;
                            String observernamelist = "", task_obsname = "";
                            for (int i = 0; i < str2.length(); i++) {
                                if (str2.charAt(i) == ',') {
                                    counter++;
                                    Log.i("TaskArrayAdapter", "OwnerOftask---------> 1 group ** " + counter);
                                }
                            }
                            for (int j = 0; j < counter + 1; j++) {
                                String Mem_name = str2.split(",")[j];
                                Log.i("TaskArrayAdapter", "OwnerOftask---------> 1 group !#! " + Mem_name);
                                if (!Mem_name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    if (!Mem_name.equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                        String Name = VideoCallDataBase.getDB(context).getName(Mem_name);
                                        observernamelist = observernamelist + Name + ",";
                                    }
                                } else {
                                    observernamelist = "Me," + observernamelist;
                                }
                            }
                            observernamelist = observernamelist.substring(0, observernamelist.length() - 1);
                            Log.i("TaskObserverList", "TaskObserver---------> 1 observerlist " + observernamelist);
                            Log.i("TaskObserverList", "TaskObserver---------> 2 str2 " + str2);
//                            Log.i("getView","observer 20 ");
                            task_observer.setText("Observer :" + " Me");
                            Log.i("TaskArrayAdapter", "observernamelist obverside * " + observernamelist);
                        } else if ((contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("note")) || (contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("note"))) {
                            task_observer.setVisibility(View.GONE);
                        } else {
//                            Log.i("getView","observer 21 ");
                            task_observer.setText("Observer :" + " NA");
                        }
                    } else {
                        String str = contactBean.getGroupTaskMembers();
                        String Owner = contactBean.getOwnerOfTask();
                        String name = VideoCallDataBase.getDB(context).getname(str);
                        String str2 = contactBean.getTaskObservers();
                        Log.i("TaskArrayAdapter", "str2 **** " + str2);
                        if (str2 != null && !str2.equalsIgnoreCase("") && !str2.equalsIgnoreCase("null") && !str2.equalsIgnoreCase("(null)")) {
                            int counter = 0;
                            String observernamelist = "", task_obsname = "";
                            for (int i = 0; i < str2.length(); i++) {
                                if (str2.charAt(i) == ',') {
                                    counter++;
                                }
                                Log.d("TaskArrayAdapter", "Task Mem's counter size is == " + counter);
                            }
                            for (int j = 0; j < counter + 1; j++) {
                                String Observer = "";
                                Log.i("TaskArrayAdapter", "Task Mem's and position 1 == " + str2.split(",")[j] + " " + j + " Owner --->" + Owner);

                                if (str2.contains(",")) {
                                    Observer = str2.split(",")[j];
                                } else {
                                    Observer = str2;
                                }
                                if (str2.contains(",")) {
                                    task_obsname = VideoCallDataBase.getDB(context).getName(str2.split(",")[j]);
                                } else {
                                    task_obsname = VideoCallDataBase.getDB(context).getName(str2);
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
                            task_observer.setText("Observer :" + observernamelist);
                            Log.i("TaskArrayAdapter", "observernamelist else * " + observernamelist);
                        } else if ((contactBean.getTaskStatus() != null && contactBean.getTaskStatus().equalsIgnoreCase("note")) || (contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("note"))) {
                            task_observer.setVisibility(View.GONE);
                        } else {
                            task_observer.setText("Observer : " + "NA");
                        }
                        if (contactBean.getCatagory() != null && contactBean.getCatagory().equalsIgnoreCase("note")) {
                            if (contactBean.getTaskReceiver() != null) {
                                if (contactBean.getTaskReceiver() != null && !contactBean.getTaskReceiver().equalsIgnoreCase("") && !contactBean.getTaskReceiver().equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                    task_taker.setVisibility(View.GONE);
                                } else {
                                    task_taker.setVisibility(View.VISIBLE);
                                }
//                                task_taker.setVisibility(View.VISIBLE);
//                                Log.i("getView","observer 23 ");
                                String name_receiver = VideoCallDataBase.getDB(context).getname(contactBean.getTaskReceiver());
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
                        } else if (!contactBean.getTaskType().equalsIgnoreCase("group")) {

                            if (contactBean.getTaskReceiver() != null && !contactBean.getTaskReceiver().equalsIgnoreCase("") && !contactBean.getTaskReceiver().equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                task_taker.setVisibility(View.GONE);
                            } else {
                                task_taker.setVisibility(View.VISIBLE);
                            }
//                            task_taker.setVisibility(View.VISIBLE);
//                            Log.i("getView", "observer 26 ");
                            String receiver = contactBean.getTaskReceiver();
                            String name_receiver = VideoCallDataBase.getDB(context).getname(str);
                            if (name != null) {
//                                Log.i("getView", "observer 27 ");
                                task_taker.setText("Taker : " + name_receiver);
                            } else {
//                                Log.i("getView", "observer 28 ");
                                if (receiver != null && receiver.contains("_")) {
                                    receiver = VideoCallDataBase.getDB(context).getname(receiver);
                                    task_taker.setText("Taker : " + receiver);
                                } else {
                                    task_taker.setText("Taker : " + receiver);
                                }

                            }
                        } else {
                            if (str != null && !str.equalsIgnoreCase("null") && !str.equalsIgnoreCase("") && !str.equalsIgnoreCase("(null)")) {
                                task_taker.setVisibility(View.VISIBLE);
                                Log.d("TaskArrayAdapter", " observer memer Task Mem's counter size is == " + str);
                                int counter = 0;
                                String observernamelist = "", task_obsname = "";
                                for (int i = 0; i < str.length(); i++) {
                                    if (str.charAt(i) == ',') {
                                        counter++;
                                    }
                                    Log.d("TaskArrayAdapter", " observer memer Task Mem's counter size is == " + counter);
                                }
                                for (int j = 0; j < counter + 1; j++) {
                                    String Taker = str.split(",")[j];
                                    Log.i("TaskArrayAdapter", "observer member Task Mem's and position == " + contactBean.getGroupTaskMembers().split(",")[j] + " " + j);
                                    task_obsname = VideoCallDataBase.getDB(context).getName(str.split(",")[j]);
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
                                task_taker.setText("Taker :" + observernamelist);
                                imageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_arrow_118_16));
                                Log.i("TaskList", "observernamelist giverside * " + observernamelist);
                            }
                        }
                        String str1 = contactBean.getOwnerOfTask();
                        String name1 = VideoCallDataBase.getDB(context).getname(str1);
//                        Log.i("getView","observer 30 ");
                        task_giver.setText("Giver : " + name1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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
            String s1 = constraint.toString().toLowerCase();
            Log.d("HistoryFilter", "filter : 0 " + s1);
            Filter.FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                Log.i(" HistoryFilter ", "  1 " + constraint.toString().length());
                ArrayList<TaskDetailsBean> taskdetailsbeanlist = new ArrayList<>();
                ArrayList<TaskDetailsBean> s = new ArrayList<TaskDetailsBean>();
                for (int i = 0, l = taskDetailsBeanArraylist.size(); i < l; i++) {
                    Log.i("HistoryFilter", "insidefor 2 " + taskDetailsBeanArraylist.size());
                    s.add(taskDetailsBeanArraylist.get(i));
                    Log.i("HistoryFilter", " s 3 " + s.get(i).getTaskName());
                    String s2 = s.get(i).getTaskId().toString();
                    String s3 = s.get(i).getTaskName().toString().toLowerCase();
                    Log.i(" constraint ", " s2  4 " + s2.toString());
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
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            Log.d("HistoryFilter", "JNDSEJBJW  11 " + results.count);
            taskDetailsBeen.clear();
            filterbuddy = (ArrayList<TaskDetailsBean>) results.values;
            if (filterbuddy.size() > 0) {
                Noresult.setVisibility(View.GONE);
                for (int i = 0, l = filterbuddy.size(); i < l; i++) {
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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 981) {
                try {
                    Log.i("TaskHistory", "onActivityResult is == 0 ");
                    String query1 = null;
                    qury = data.getStringExtra("Query");
                    active_task.setText(qury);
                    Log.i("task", "type" + taskType);
                    if (taskType != null && taskType.equalsIgnoreCase("Individual")) {
                        if (qury.equalsIgnoreCase("inprogress")) {
                            Log.i("task", "filtered was " + qury);
                            fill.setBackgroundResource(R.drawable.filterenabled);
                            query1 = "select * from taskHistoryInfo where (taskStatus ='" + qury + "') and  (( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' ) and ( taskType='" + taskType + "')  group by taskId";
                        } else if (qury.equalsIgnoreCase("issue")) {
                            active_task.setText("Issues for task");
                            catagory = "issue";
                            query1 = "select * from taskHistoryInfo where (( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' )  and category = '" + catagory + "' and ( taskType='" + taskType + "')group by taskId";
                            Log.i("task", "filtered was 1*** " + qury);
                        } else if (qury.equalsIgnoreCase("Task&Issue")) {
                            query1 = "select * from taskHistoryInfo where (( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' ) and ( taskType='" + taskType + "') group by taskId";
                            Log.i("task", "filtered was 2** " + qury);
                        } else if (!qury.equalsIgnoreCase("alltask")) {
                            Log.i("task", "filtered was 2*** --> " + qury);
                            query1 = "select * from taskHistoryInfo where ( taskStatus = '" + qury + "' ) and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + taskType + "')  group by taskId";
                        } else {
                            Log.i("task", "filtered was " + qury);
                            fill.setBackgroundResource(R.drawable.filterenabled);
                            catagory = "Task";
                            fill.setBackgroundResource(R.drawable.filterdisabled);
                            query1 = "select * from taskHistoryInfo where (ownerOfTask='" + userName + "' or taskReceiver='" + userName + "') and (category <> 'chat') and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and category = '" + catagory + "' and taskType='" + taskType + "' group by taskId";
                        }
                    } else if (taskType != null && taskType.equalsIgnoreCase("Group")) {
                        if (qury.equalsIgnoreCase("inprogress")) {
                            Log.i("task", "filtered was " + qury);
                            fill.setBackgroundResource(R.drawable.filterenabled);
                            query1 = "select * from taskHistoryInfo where (taskStatus ='" + qury + "') and (category <> 'chat') and  (ownerOfTask='" + userName + "' or taskReceiver='" + userName + "') and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' ) and ( taskType='" + taskType + "')  group by taskId";
                        } else if (qury.equalsIgnoreCase("issue")) {
                            active_task.setText("Issues for task");
                            catagory = "issue";
                            query1 = "select * from taskHistoryInfo where (ownerOfTask='" + userName + "' or taskReceiver='" + userName + "') and (category <> 'chat') and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' )  and category = '" + catagory + "' group by taskNo";
                            Log.i("task", "filtered was 1*** " + qury);
                        } else if (qury.equalsIgnoreCase("Task&Issue")) {
                            catagory = "issue";
                            query1 = "select * from taskHistoryInfo where (ownerOfTask='" + userName + "' or taskReceiver='" + userName + "') and (category <> 'chat') and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' )  group by taskNo";
                            Log.i("task", "filtered was 2** " + qury);
                        } else if (!qury.equalsIgnoreCase("alltask"))
                            query1 = "select * from taskHistoryInfo where ( taskStatus = '" + qury + "' ) and (category <> 'chat') and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + taskType + "')  group by taskId";
                        else {
                            catagory = "issue";
                            query1 = "select * from taskHistoryInfo where ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and (category <> 'chat') and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + taskType + "')  group by taskId";
                        }
                    }
                    taskDetailsBeen.clear();
                    taskDetailsBeen1.clear();
                    taskDetailsBeen2.clear();
                    taskDetailsBeanArraylist.clear();
                    Log.d("task", "query   " + query1);
                    taskDetailsBeen1 = VideoCallDataBase.getDB(context).getTaskHistoryInfo(query1);
                    Log.e("size", "onActivity " + String.valueOf(taskDetailsBeen1.size()));
                    Log.i("task", "size was " + taskDetailsBeanArraylist.size());
                    taskDetailsBeen2.addAll(taskDetailsBeen1);
                    Log.d("Last Status", "value " + taskDetailsBeen2.size());
                    Log.d("Last Status", "value ## " + taskDetailsBeanArraylist.size());
                    for (TaskDetailsBean contactBean : taskDetailsBeen2) {
                        Log.i("task", "project 1");
                        Log.i("TaskHistory", "onActivityResult 3" + " 1 " + " ********");
                        int unReadMsgCount = VideoCallDataBase.getDB(context).getTaskUnReadMsgCount(contactBean.getTaskId());
                        rem = VideoCallDataBase.getDB(context).getRemainderUnReadMsgCount(contactBean.getTaskNo());
                        Log.d("task1", "unread count" + unReadMsgCount);
                        int percentage = 0;
                        Log.d("task1", "unread count" + unReadMsgCount + contactBean.getToUserName() + contactBean.getTaskId() + contactBean.getOwnerOfTask() + " " + contactBean.getTaskType());
                        String taskFrom_name = VideoCallDataBase.getDB(context).getProjectParentTaskId("select fromUserName from taskDetailsInfo where taskId = '" + contactBean.getTaskId() + "' order by id desc LIMIT 1");
                        contactBean.setFromUserName(taskFrom_name);
                        Log.i("TaskHistory", "fromUserName " + contactBean.getFromUserName());
                        String taskTo_name = VideoCallDataBase.getDB(context).getProjectParentTaskId("select toUserName from taskDetailsInfo where taskId = '" + contactBean.getTaskId() + "' order by id desc LIMIT 1");
                        contactBean.setToUserName(taskTo_name);
                        Log.i("TaskHistory", "toUserName " + contactBean.getToUserName());

                        if (contactBean.getTaskType() != null && contactBean.getTaskType().equalsIgnoreCase("Group")) {
                            Log.d("TaskHistory", "Group percent 11 " + contactBean.getTaskType());
                            if (contactBean.getOwnerOfTask() != null && contactBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                Log.d("TaskHistory", "unread count 21 " + unReadMsgCount);
                                if (contactBean.getFromUserName() != null && contactBean.getFromUserName().equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                    Log.d("TaskHistory", "unread count 31 " + unReadMsgCount);
                                    percentage = Integer.parseInt(VideoCallDataBase.getDB(context).getlastCompletedParcentage(contactBean.getTaskId()));
                                } else {
                                    Log.d("TaskHistory", "unread count 41 " + unReadMsgCount);
                                    percentage = VideoCallDataBase.getDB(context).GroupPercentageChecker(contactBean.getToUserName(), contactBean.getTaskId(), contactBean.getOwnerOfTask());
                                }
                            } else {
                                if (contactBean.getFromUserName() != null && contactBean.getFromUserName().equalsIgnoreCase(contactBean.getOwnerOfTask())) {
                                    Log.d("TaskHistory", "unread count 51 " + unReadMsgCount);
                                    percentage = Integer.parseInt(contactBean.getCompletedPercentage());
                                } else {
                                    Log.d("TaskHistory", "unread count 61 " + unReadMsgCount);
                                    String TakerPercent_value = VideoCallDataBase.getDB(context).getTakerlastCompletedParcentage(contactBean.getTaskId());
                                    if (TakerPercent_value != null)
                                        percentage = Integer.parseInt(TakerPercent_value);
                                }
                            }
                        } else {
                            percentage = VideoCallDataBase.getDB(context).percentagechecker(contactBean.getTaskId());
                        }
                        contactBean.setCompletedPercentage(String.valueOf(percentage));

                        contactBean.setRead_status(unReadMsgCount);

                        contactBean.setMsg_status(rem);

                        taskDetailsBeen.add(contactBean);
                        taskDetailsBeanArraylist.add(contactBean);
                    }
                    Log.d("task", "task list size = " + taskDetailsBeen.size());
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
                                Log.i("DateFormet", "ListPosition" + taskDetailsBeen.get(1).getTaskDescription());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return date_lhs.compareTo(date_rhs);
                        }
                    }
                    Collections.sort(taskDetailsBeen, new StringDateComparator());
                    Log.d("task", "1 task list size = " + taskDetailsBeen.size());
                    Collections.reverse(taskDetailsBeen);
                    Log.d("task", "2 task list size = " + taskDetailsBeen.size());
                    buddyArrayAdapter = new TaskArrayAdapter(context, taskDetailsBeen);
                    listView.setAdapter(buddyArrayAdapter);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            buddyArrayAdapter.notifyDataSetChanged();
                        }
                    });
                    check = false;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean getTaskObservers(String taskId) {
        String query1 = "select * from taskDetailsInfo where loginuser ='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + taskId + "' and sendStatus = '3' order by id DESC LIMIT 1";
        Log.d("TaskObserver", "get Observer query  " + query1);
        ArrayList<TaskDetailsBean> arrayList;
        ArrayList<String> listOfObservers = null;
        String value = null;
        boolean observerCheck = false;
        arrayList = VideoCallDataBase.getDB(context).getTaskHistory(query1);
        Log.d("TaskObserver", "Task Observer list size is == " + arrayList.size());
        if (arrayList.size() > 0) {
            TaskDetailsBean taskDetailsBean = arrayList.get(0);
            String taskObservers = taskDetailsBean.getTaskObservers();
            Log.d("TaskObserver", "Task Observer  == " + taskObservers);
            boolean check = false;
            if (taskObservers.contains(Appreference.loginuserdetails.getUsername())) {
                check = false;
            } else if (taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                check = false;
            } else
                check = !taskDetailsBean.getTaskReceiver().equalsIgnoreCase(Appreference.loginuserdetails.getUsername());
            int counter = 0;
            for (int i = 0; i < taskObservers.length(); i++) {
                if (taskObservers.charAt(i) == ',') {
                    counter++;
                }
            }
            Log.d("TaskObserver", "Task Observer counter size is == " + counter);
            for (int j = 0; j < counter + 1; j++) {
                if (Appreference.loginuserdetails.getUsername().equalsIgnoreCase(taskObservers.split(",")[j])) {
                    value = taskObservers.split(",")[j];
                    if (taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(userName)) {
                        observerCheck = false;
                    } else {
                        observerCheck = true;
                    }
                    Log.d("TaskObserver", "Task Observer name not in same user== " + taskObservers.split(",")[j]);
                }
            }
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
                if (oncreate_query)
                    query = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and (category <> 'chat') and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + currentLoginUserMail + "') and ( taskType='" + taskType + "')";
                else
                    query = "select * from taskHistoryInfo where (( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and (category <> 'chat') and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' ) and ( taskType='" + taskType + "')";
            } else if (count < 10 && !countAboveten) {
                if (oncreate_query)
                    query = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and (category <> 'chat') and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + currentLoginUserMail + "') and ( taskType='" + taskType + "') order by dateTime ASC LIMIT " + count + " OFFSET 0";
                else
                    query = "select * from taskHistoryInfo where (( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and (category <> 'chat') and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' ) and ( taskType='" + taskType + "') order by dateTime ASC LIMIT " + count + " OFFSET 0";
            } else {
                if (oncreate_query)
                    query = "select * from taskHistoryInfo where ( taskStatus <> 'abandoned' ) and (category <> 'chat') and ( ( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and ( loginuser='" + currentLoginUserMail + "') and ( taskType='" + taskType + "') order by dateTime ASC LIMIT 10 OFFSET " + count + "";
                else
                    query = "select * from taskHistoryInfo where (( ownerOfTask='" + userName + "' ) or ( taskReceiver='" + userName + "' )) and (category <> 'chat') and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "' ) and ( taskType='" + taskType + "') order by dateTime ASC LIMIT 10 OFFSET " + count + "";
            }
            Log.d("lazyload", "query is == 3 " + query);
            ArrayList<TaskDetailsBean> taskDetailsBeen1 = videoCallDataBase.getTaskHistoryInfo(query);
            Log.i("DBQuery", "size of history 4 " + taskDetailsBeen1.size());
            if (taskDetailsBeen1.size() > 0) {
                for (TaskDetailsBean contactBean : taskDetailsBeen1) {
                    Log.i("TaskHistory", "project 3");
                    Log.i("TaskHistory", "setActiveAdapter 3" + " 1 " + " ********");
                    int unReadMsgCount = videoCallDataBase.getTaskUnReadMsgCount(contactBean.getTaskId());
                    Log.d("TaskHistory", "unread count " + unReadMsgCount);
                    Log.d("task1", "unread count " + unReadMsgCount);
                    Log.d("task1", "for each  issues Id  == " + contactBean.getIssueId());
                    rem = videoCallDataBase.getRemainderUnReadMsgCount(contactBean.getTaskId());
                    contactBean.setRead_status(unReadMsgCount);
                    Log.d("TaskHistory", "contactBean.getRead_status() " + contactBean.getRead_status());
                    contactBean.setMsg_status(rem);
                    Log.i("TaskHistory", "popup status 3 8 " + contactBean.getTaskStatus());
                    if (contactBean.getTaskStatus().equalsIgnoreCase("completed")) {
                        reopen_qry = "select taskId from taskHistoryInfo where (parentTaskId ='" + contactBean.getTaskId() + "') and taskStatus != 'closed'";
                        Log.d("DBQuery", "reopen_qry--> " + reopen_qry);
                        Log.d("DBQuery", "reopen_qry boolean --> " + videoCallDataBase.isAgendaRecordExists(reopen_qry));
                        if (videoCallDataBase.isAgendaRecordExists(reopen_qry)) {
                            contactBean.setTaskStatus("Reopen");
                        }
                    }
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
            handler.post(new

                                 Runnable() {
                                     @Override
                                     public void run() {
                                         buddyArrayAdapter.notifyDataSetChanged();
                                     }
                                 }

            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyAdapter1 extends ArrayAdapter<String> {
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
            LayoutInflater inflater = getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.customspin, parent,
                    false);
            TextView main_text = (TextView) mySpinner.findViewById(R.id.customtext);
            main_text.setText(catestring1[position]);
            return mySpinner;
        }
    }

    private class MyAdapter2 extends ArrayAdapter<String> {
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
            LayoutInflater inflater = getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.customspin, parent,
                    false);
            TextView main_text = (TextView) mySpinner.findViewById(R.id.customtext);
            main_text.setText(catestring2[position]);
            return mySpinner;
        }
    }

    private class MyAdapter3 extends ArrayAdapter<String> {
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
            LayoutInflater inflater = getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.customspin, parent,
                    false);
            TextView main_text = (TextView) mySpinner.findViewById(R.id.customtext);
            main_text.setText(catestring3[position]);
            return mySpinner;
        }
    }
}
