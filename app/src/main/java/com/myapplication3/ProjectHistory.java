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
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.myapplication3.sketh.ProjectsFragment;

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

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.ListMember;
import json.WebServiceInterface;

/**
 * Created by saravanakumar on 7/13/2016.
 */
public class ProjectHistory extends Activity implements WebServiceInterface, SwipeRefreshLayout.OnRefreshListener {


    static ProjectHistory projectHistory;
    public TaskDetailsBean taskDetailsBean1;
    String listMembers;
    public ArrayList<ListMember> listofProjectMembers;
    public ProjectDetailsBean taskDetailsBean;
    NewTaskConversation newTaskConversation;
    // ListView listView;
    ArrayList<TaskDetailsBean> taskDetailsBeen, taskDetailsBeen1, non_activebean;
    ArrayList<ProjectDetailsBean> projectDetailsBeans, projectDetailsBeanslist_ForSearch, filterbuddy;
    private ProjectHistoryFilter filter;
    String userName, taskType, project_id, project_name, parent_TaskId, group_UserId, project_Owner;
    boolean isFromOracle;
    String qury = "alltask";
    Context context;
    int spin1pos, spin2pos, spin3pos;
    static TaskHistory taskHistory;
    int rem;
    TextView finish_page, active_task;
    TaskArrayAdapter buddyArrayAdapter, non_activetask;
    String quotes = "\"", query;
    ProgressDialog dialog;
    ImageView fill;
    public String[] catestring1, catestring2, catestring3;
    Spinner spin1, spin2, spin3;
    TextView cancelbutton, donebutton, clearbutton;
    LinearLayout spinner_layout;
    String webTaskId, groupname;
    private Handler handler = new Handler();
    boolean check = true;
    private SwipeMenuListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout History_Search, options;
    EditText Search_EditText;
    ImageView submit_icon, addsubtasks, addnote, view_ProjectMems;
    TextView Noresult, view_mems;
    VideoCallDataBase videoCallDataBase;

    public static ProjectHistory getInstance() {
        return projectHistory;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    private void setActiveAdapter() {
        try {
            Log.i("ws123", "Refresh Request  received " );

            Log.i("DBQuery", "userName is == " + userName);
            Log.i("DBQuery", "taskType is == " + taskType);
            Log.i("DBQuery", "taskType is == " + project_id);
            query = "select * from projectHistory where projectId ='" + project_id + "' order by taskId ASC";
            Log.d("DBQuery", "query is == " + query);
            projectDetailsBeans = VideoCallDataBase.getDB(context).getProjectHistory(query);
            projectDetailsBeanslist_ForSearch = VideoCallDataBase.getDB(context).getProjectHistory(query);
            Log.i("DBQuery", "size of history " + projectDetailsBeans.size());

            Log.i("DBQuery", "userName " + userName);

            class StringDateComparator implements Comparator<ProjectDetailsBean> {
                String date_lhs = null;
                String date_rhs = null;

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                public int compare(ProjectDetailsBean lhs, ProjectDetailsBean rhs) {
                    try {
                        Log.i("DateFormat", "leftside" + lhs.getTaskId());
                        Log.i("DateFormat", "rightside" + rhs.getTaskId());
                        //                    date_lhs = dateFormat.parse(lhs.getDateTime());
                        //                    date_rhs = dateFormat.parse(rhs.getDateTime());
                        date_lhs = lhs.getTaskId();
                        date_rhs = rhs.getTaskId();
                        Log.i("DateFormet", "ListPosition" + projectDetailsBeans.get(1).getTaskId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return Integer.valueOf(date_rhs).compareTo(Integer.valueOf(date_lhs));
                }
            }
//
            Collections.sort(projectDetailsBeans, new StringDateComparator());
            Collections.reverse(projectDetailsBeans);
            for (ProjectDetailsBean projectDetailsBean : projectDetailsBeans) {
                Log.i("DBQuery", "taskId of history " + projectDetailsBean.getTaskId());
                Log.i("DBQuery", "taskId of history " + projectDetailsBean.getTaskStatus());
            }
            buddyArrayAdapter = new TaskArrayAdapter(context, projectDetailsBeans);
            listView.setAdapter(buddyArrayAdapter);
            swipeRefreshLayout.setRefreshing(false);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Appreference.context_table.put("projecthistory", this);

        try {
            context = this;
            projectHistory = this;
            setContentView(R.layout.project_history);

            listView = (SwipeMenuListView) findViewById(R.id.lv_taskHistory);
            swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
            swipeRefreshLayout.setOnRefreshListener(this);
          /*  swipeRefreshLayout.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            swipeRefreshLayout.setRefreshing(true);
                                            fetchTaskList();
                                        }
                                    }
            );*/

            finish_page = (TextView) findViewById(R.id.finish_page);
            submit_icon = (ImageView) findViewById(R.id.submit_icon);
            addsubtasks = (ImageView) findViewById(R.id.addsubtasks);
            addnote = (ImageView) findViewById(R.id.addnote);


            History_Search = (LinearLayout) findViewById(R.id.History_Search);
            Search_EditText = (EditText) findViewById(R.id.searchtext);
            Noresult = (TextView) findViewById(R.id.Noresult);
            active_task = (TextView) findViewById(R.id.txtView01);
            spinner_layout = (LinearLayout) findViewById(R.id.spinner_layout);
            spinner_layout.setVisibility(View.GONE);
            spin1 = (Spinner) findViewById(R.id.spinner1);
            spin2 = (Spinner) findViewById(R.id.spinner2);
            spin3 = (Spinner) findViewById(R.id.spinner3);
            cancelbutton = (TextView) findViewById(R.id.cancelbutton);
            donebutton = (TextView) findViewById(R.id.donebutton);
            clearbutton = (TextView) findViewById(R.id.clearbutton);
            view_mems = (TextView) findViewById(R.id.view_mems);
            options = (LinearLayout) findViewById(R.id.options);
            options.setVisibility(View.GONE);
            view_ProjectMems = (ImageView) findViewById(R.id.iv_view_mem);
            taskDetailsBeen = new ArrayList<>();
            taskDetailsBeen1 = new ArrayList<>();
            listMembers = new String();
            fill = (ImageView) findViewById(R.id.fill);
            listofProjectMembers = new ArrayList<>();
//        spin1pos=4;
            spin1.getId();
//        spin2pos=5;
//        spin3pos=2;
            spin2.setSelection(spin2.getBottom());
            spin3.setSelection(spin3.getBottom());
            non_activebean = new ArrayList<>();
            if (getIntent() != null) {
                project_id = getIntent().getExtras().getString("projectId");
                project_name = getIntent().getExtras().getString("projectName");
    //            parent_TaskId = getIntent().getExtras().getString("parentTaskId");
                group_UserId = getIntent().getExtras().getString("groupUserId");
                project_Owner = getIntent().getExtras().getString("projectOwner");
                isFromOracle=getIntent().getBooleanExtra("fromOracle",false);
                if(isFromOracle)
                    addsubtasks.setVisibility(View.GONE);
            }
           /* if (project_Owner != null && project_Owner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                addsubtasks.setVisibility(View.VISIBLE);
    //            addnote.setVisibility(View.VISIBLE);
            } else {
                addsubtasks.setVisibility(View.INVISIBLE);
    //            addnote.setVisibility(View.INVISIBLE);
            }*/
            if (project_name != null)
                active_task.setText(project_name);
//        userName = getIntent().getExtras().getString("userId");
//        taskType = getIntent().getExtras().getString("taskType");
//        groupname = userName;
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(Search_EditText.getWindowToken(), 0);
            videoCallDataBase = VideoCallDataBase.getDB(context);
            active_task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (options.getVisibility() == View.GONE) {
                        options.setVisibility(View.VISIBLE);
                    } else {
                        options.setVisibility(View.GONE);
                    }
                }
            });
            view_mems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Appreference.context_table.containsKey("grouppercentagestatus")) {
                        Intent intent = new Intent(ProjectHistory.this, ViewProjectMembers.class);
                        intent.putExtra("projectId", project_id);
                        intent.putExtra("projectOwner", project_Owner);
                        startActivity(intent);
                        options.setVisibility(View.GONE);
                    }
                }
            });
            setActiveAdapter();
            try {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        buddyArrayAdapter.notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


            Search_EditText.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String[] value = new String[count];
                    Log.d("constraint", "JNDSEJBJW  * " + s);
    //                buddyArrayAdapter.getFilter().filter(s);
                    ProjectHistory.this.buddyArrayAdapter.getFilter().filter(s);
                }


                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            fill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    check = true;
                    Log.i("ProjectSpinner", "Fill spin1pos value is" + spin1pos);
                    Log.i("ProjectSpinner", "Fill spin2pos value is" + spin2pos);
                    Log.i("ProjectSpinner", "Fill spin3pos value is" + spin3pos);
                    if (spinner_layout.getVisibility() == View.GONE) {
                        spinner_layout.setVisibility(View.VISIBLE);
                        Search_EditText.setVisibility(View.GONE);
                        listView.setEnabled(false);
                        listView.setClickable(false);
                        fill.setBackgroundResource(R.drawable.filterenabled);
                    } else {
                        spinner_layout.setVisibility(View.GONE);
                        Search_EditText.setVisibility(View.VISIBLE);
                        listView.setEnabled(true);
                        listView.setClickable(true);
                        fill.setBackgroundResource(R.drawable.filterdisabled);
                    }
                    List<String> categories = new ArrayList<String>();
                    categories.add("Task");
                    categories.add("issue");
                    categories.add("note");
                    categories.add("Template");
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
                    status.add("All");
                    catestring2 = status.toArray(new String[status.size()]);
                    spin2.setAdapter(new MyAdapter2(context, R.layout.customspin, catestring2));
                    spin2.setPopupBackgroundResource(R.drawable.borderfordialog);
                    List<String> categories3 = new ArrayList<String>();
                    String givername = " ";
                    Log.i("projectHistory", "loginuser detail" + Appreference.loginuserdetails.getEmail());
                    givername = "select contactemail from contact where loginuser='" + Appreference.loginuserdetails.getEmail() + "'";
                    Log.i("projectHistory", "givername " + givername);
                    String Memberlist = "", getmemberquery = "";
                    getmemberquery = "select taskMemberList from projectHistory where projectId='" + project_id + "'";
                    ArrayList<String> taskmemberlist = new ArrayList<String>();
                    Memberlist = VideoCallDataBase.getDB(context).getGroupMemberAccess(getmemberquery);
                    int counter = 0;
                    for (int i = 0; i < Memberlist.length(); i++) {
                        if (Memberlist.charAt(i) == ',') {
                            counter++;
                        }
                        Log.i("project_details", "Task Mem's counter size is == " + counter);
                    }
                    for (int j = 0; j < counter + 1; j++) {
                        String Mem_name = Memberlist.split(",")[j];
                        Log.i("project_details", "Task Mem's and position == " + Mem_name + " " + j);
                        taskmemberlist.add(Mem_name);
                    }

                    Log.i("projectHistory", "buddylist value is " + taskmemberlist.size());
                    categories3.add("Me");
                    for (int i = 0; i < taskmemberlist.size(); i++) {
                        Log.i("projectHistory", "buddylist value is 3 " + taskmemberlist.get(i));
                        String username = taskmemberlist.get(i);
                        categories3.add(VideoCallDataBase.getDB(context).getname(username));
                    }
                    categories3.add("All");
                    catestring3 = categories3.toArray(new String[categories3.size()]);
                    spin3.setAdapter(new MyAdapter3(getApplicationContext(), R.layout.customspin, catestring3));
                    spin3.setPopupBackgroundResource(R.drawable.borderfordialog);
                    if (spin1pos == 0 && spin2pos == 0 && spin3pos == 0) {
                        spin1.setSelection(spin1.getBottom());
                        spin2.setSelection(spin2.getBottom());
                        spin3.setSelection(spin3.getBottom());
                    }
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

            cancelbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listView.setEnabled(true);
                    spinner_layout.setVisibility(View.GONE);
                    Search_EditText.setVisibility(View.VISIBLE);
                    fill.setBackgroundResource(R.drawable.filterdisabled);
                }
            });
            clearbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spin1.setSelection(spin1.getCount() - 1);
                    spin2.setSelection(spin2.getCount() - 1);
                    spin3.setSelection(spin3.getCount() - 1);
                    spin1pos = spin1.getSelectedItemPosition();
                    spin2pos = spin2.getSelectedItemPosition();
                    spin3pos = spin3.getSelectedItemPosition();
                    fill.setBackgroundResource(R.drawable.filterdisabled);
    //                spinner_layout.setVisibility(View.GONE);
    //                Search_EditText.setVisibility(View.VISIBLE);
                    setActiveAdapter();
                    try {
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
            });

            donebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listView.setEnabled(true);
                    listView.setClickable(true);
                    spinner_layout.setVisibility(View.GONE);
                    fill.setBackgroundResource(R.drawable.filterenabled);
                    String spinner1 = spin1.getSelectedItem().toString();
                    spin1pos = spin1.getSelectedItemPosition();
                    Log.i("ProjectSpinner", "spin1pos value is" + spin1pos);
                    String spinner2 = spin2.getSelectedItem().toString();
                    spin2pos = spin2.getSelectedItemPosition();
                    Log.i("ProjectSpinner", "spin2pos value is" + spin2pos);
                    String spinner3 = spin3.getSelectedItem().toString();
                    spin3pos = spin3.getSelectedItemPosition();
                    Log.i("ProjectSpinner", "spin3pos value is" + spin3pos);
                    Log.i("projectHistory", spinner1);
                    Log.i("projectHistory", spinner2);
                    Log.i("projectHistory", spinner3);
                    String issue_qry = " ";
                    String spy = " ";
                    if (!spinner3.equalsIgnoreCase("Me") && !spinner3.equalsIgnoreCase("All")) {
                        String firstname = spinner3.split(" ")[0];
                        String lastname = spinner3.split(" ")[1];
                        spy = VideoCallDataBase.getDB(context).getusername(firstname, lastname);
                    }
                    if (!spinner1.equalsIgnoreCase("All") && !spinner2.equalsIgnoreCase("All") && !spinner3.equalsIgnoreCase("All")) {
                        Log.i("projectHistory", "Inside 3 are selected");
                        if (spinner3.equalsIgnoreCase("Me")) {
                            issue_qry = "select * from projectHistory where (projectId='" + project_id + "') and ( taskStatus='" + spinner2 + "') and( category='" + spinner1 + "') and ( ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "')";
                            Log.i("projectHistory", spinner1);
                            Log.i("projectHistory", spinner2);
                        } else {
                            issue_qry = "select * from projectHistory where (projectId='" + project_id + "') and ( taskStatus='" + spinner2 + "') and( category='" + spinner1 + "') and ( ownerOfTask='" + spy + "')";
                            Log.i("projectHistory", "issue_qry value" + issue_qry);
                        }
                    } else if (spinner1.equalsIgnoreCase("All")) {
                        Log.i("projectHistory", "Inside category is unselected");
                        if (spinner2.equalsIgnoreCase("All")) {
                            Log.i("projectHistory", "Inside status is unselected");
                            if (!spinner3.equalsIgnoreCase("All")) {
                                if (spinner3.equalsIgnoreCase("Me")) {
                                    issue_qry = "select * from projectHistory where (projectId='" + project_id + "') and ( ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "')";
                                    Log.i("projectHistory", spinner1);
                                    Log.i("projectHistory", spinner2);
                                    Log.i("projectHistory", "issue_qry value" + issue_qry);
                                } else {
                                    issue_qry = "select * from projectHistory where (projectId='" + project_id + "') and ( ownerOfTask='" + spy + "')";
                                    Log.i("projectHistory", "issue_qry value" + issue_qry);
                                }
                            }
                        } else {
                            if (!spinner3.equalsIgnoreCase("All")) {
                                if (spinner3.equalsIgnoreCase("Me")) {
                                    issue_qry = "select * from projectHistory where (projectId='" + project_id + "') and ( taskStatus='" + spinner2 + "') and ( ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "')";
                                    Log.i("projectHistory", spinner1);
                                    Log.i("projectHistory", spinner2);
                                    Log.i("projectHistory", "issue_qry value" + issue_qry);
                                } else {
                                    issue_qry = "select * from projectHistory where (projectId='" + project_id + "') and ( taskStatus='" + spinner2 + "') and ( ownerOfTask='" + spy + "')";
                                    Log.i("projectHistory", "issue_qry value" + issue_qry);
                                }
                            }
                        }
                    } else {
                        Log.i("projectHistory", "Inside category selected");
                        Log.i("projectHistory", "Me name is" + Appreference.loginuserdetails.getUsername());
                        if (spinner2.equalsIgnoreCase("All")) {
                            Log.i("projectHistory", "Inside category selected status is unselected");
                            if (spinner3.equalsIgnoreCase("All")) {
                                issue_qry = "select * from projectHistory where (projectId='" + project_id + "') and ( category='" + spinner1 + "')";
                            } else {
                                if (spinner3.equalsIgnoreCase("Me")) {
                                    issue_qry = "select * from projectHistory where (projectId='" + project_id + "')and ( category='" + spinner1 + "') and ( ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "')";
                                    Log.i("projectHistory", spinner1);
                                    Log.i("projectHistory", spinner2);
                                    Log.i("projectHistory", "Me name is" + Appreference.loginuserdetails.getUsername());
                                    Log.i("projectHistory", "issue_qry value" + issue_qry);
                                } else {
                                    issue_qry = "select * from projectHistory where (projectId='" + project_id + "')and ( category='" + spinner1 + "') and ( ownerOfTask='" + spy + "')";
                                    Log.i("projectHistory", "issue_qry value" + issue_qry);
                                }
                            }
                        } else {
                            Log.i("projectHistory", "category selected and status is selected");
                            if (spinner3.equalsIgnoreCase("All")) {
                                issue_qry = "select * from projectHistory where (projectId='" + project_id + "') and ( taskStatus='" + spinner2 + "')and ( category='" + spinner1 + "')";
                            }
                        }
                    }
                    projectDetailsBeans.clear();
                    Log.i("projectHistoryFill", "issue_qry value" + issue_qry);
                    try {
                        projectDetailsBeans = VideoCallDataBase.getDB(context).getProjectHistory(issue_qry);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.i("projectHistoryFill ", "size of issue_task ** " + projectDetailsBeans.size());
                    buddyArrayAdapter = new TaskArrayAdapter(context, projectDetailsBeans);
                    Log.i("projectHistoryFill ", "size of issue_task ** " + projectDetailsBeans.size());
                    buddyArrayAdapter.notifyDataSetChanged();
                    listView.setAdapter(buddyArrayAdapter);
                    if (spinner1.equalsIgnoreCase("All") && spinner2.equalsIgnoreCase("All") && spinner3.equalsIgnoreCase("All")) {
                        setActiveAdapter();
                        try {
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
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (projectDetailsBeans.size() == 0 || listView.getCount() == 0) {
                                Noresult.setVisibility(View.VISIBLE);
                                Noresult.setText("No ProjectHistories Available");
                            } else {
                                Noresult.setVisibility(View.GONE);
                                buddyArrayAdapter.notifyDataSetChanged();
                                listView.setAdapter(buddyArrayAdapter);
                            }
                        }
                    });
                    spinner_layout.setVisibility(View.GONE);
                    Search_EditText.setVisibility(View.VISIBLE);
                }
            });

            Search_EditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView v, int actionId,
                                              KeyEvent event) {
                    Search_EditText.setCursorVisible(false);
                    if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(Search_EditText.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    return false;
                }
            });
            addsubtasks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(ProjectHistory.this, Takerslist.class);
                    intent1.putExtra("projectid", project_id);
                    startActivity(intent1);
                }
            });
            addnote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProjectHistory.this, NewTaskConversation.class);
                    intent.putExtra("task", "newprojectnote");
                    intent.putExtra("type", "Individual");
                    intent.putExtra("projectid", project_id);
                    Log.i("newprojectnote", "project_id " + project_id);
                    startActivityForResult(intent, 404);
                }
            });
            final SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {
                    switch (menu.getViewType()) {

                        case 1:
                            // create "open" item
                            SwipeMenuItem openItem = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            openItem.setBackground(new ColorDrawable(Color.rgb(0xff, 0x91,
                                    0x11)));
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
                            break;
                        case 0:
                            break;
                        case 2:
                            SwipeMenuItem openItem2 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            openItem2.setBackground(new ColorDrawable(Color.rgb(0x00, 0xff,
                                    0x11)));
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
                            break;

                        case 3:
                            // create "open" item
                            SwipeMenuItem openItem1 = new SwipeMenuItem(
                                    getApplicationContext());
                            // set item background
                            openItem1.setBackground(new ColorDrawable(Color.rgb(0x00, 0xff,
                                    0x11)));
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
                            break;

                    }
                }
            };
            listView.setMenuCreator(creator);
            listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    try {
                        taskDetailsBean = projectDetailsBeans.get(position);


                        Log.d("ProjectHistory", "project id  ==  " + taskDetailsBean.getId() + "task id  == " + taskDetailsBean.getTaskId());
                        VideoCallDataBase.getDB(context).updateBadgeStatus("0", taskDetailsBean.getId());

                        Search_EditText.setText("");
                        Log.d("Task1", "task stststts " + taskDetailsBean.getTaskStatus());
                        if (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("Template") || (taskDetailsBean.getTaskStatus() != null && taskDetailsBean.getTaskStatus().equalsIgnoreCase("draft"))) {
                            //                    if (!taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            //                        showDialog();
                            //                        webTaskId = taskDetailsBean.getTaskId();
                            //                        gettaskwebservice(webTaskId);
                            //                        check = true;
                            //
                            //                    }
//                            if (taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                Log.i("task", String.valueOf(position));
                                Intent intent = new Intent(context, NewTaskConversation.class);
                                intent.putExtra("task", "ProjectTemplateview");
                                intent.putExtra("project_Temp", "ProjectTemplate");
                                intent.putExtra("projectHistoryBean", taskDetailsBean);
                                if (taskDetailsBean.getOracleProjectId() != null) {
                                    /*if task is template*/
                                    intent.putExtra("oracleProjectOwner", taskDetailsBean.getOwnerOfTask());
                                    intent.putExtra("ProjectFromOracle", true);
                                   /* intent.putExtra("projectName",taskDetailsBean.getProjectName());
                                    intent.putExtra("parentTaskID",taskDetailsBean.getParentTaskId());*/

                                }
                                check = true;
                                startActivity(intent);
//                            } else {
//                                Toast.makeText(getApplicationContext(), "You are not allowed for this Template", Toast.LENGTH_SHORT).show();
//                            }
                        } else if (taskDetailsBean.getTaskType().equalsIgnoreCase("individual")) {
                            if (taskDetailsBean.getTaskReceiver().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) || taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) || taskDetailsBean.getTaskObservers().contains(Appreference.loginuserdetails.getUsername())) {
                                Log.i("task", String.valueOf(position));
                                Intent intent = new Intent(context, NewTaskConversation.class);
                                intent.putExtra("task", "projectHistory");
                                intent.putExtra("projectHistoryBean", taskDetailsBean);
                                if (taskDetailsBean.getOracleProjectId() != null) {
                                    intent.putExtra("oracleProjectOwner", taskDetailsBean.getOwnerOfTask());
                                    intent.putExtra("ProjectFromOracle", true);
                                }
                                    check = true;
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "You are not in individual project subtask", Toast.LENGTH_SHORT).show();
                            }
                        } else if (taskDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
                            Log.i("ListMembers", "projectDetailsBean.getTaskMemberList() " + taskDetailsBean.getTaskMemberList());
                            if ((taskDetailsBean.getTaskMemberList() != null && taskDetailsBean.getTaskMemberList().contains(Appreference.loginuserdetails.getUsername())) || (taskDetailsBean.getOwnerOfTask() != null && taskDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) || (taskDetailsBean.getTaskObservers() != null && taskDetailsBean.getTaskObservers().contains(Appreference.loginuserdetails.getUsername()))) {
                                Log.i("task", String.valueOf(position));
                                Intent intent = new Intent(context, NewTaskConversation.class);
                                intent.putExtra("task", "projectHistory");
                                intent.putExtra("projectHistoryBean", taskDetailsBean);
                                if(taskDetailsBean.getOracleProjectId()!=null) {
                                    intent.putExtra("oracleProjectOwner", taskDetailsBean.getOwnerOfTask());
                                    intent.putExtra("ProjectFromOracle", true);
                                }
                                check = true;
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "You are not in group project task", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });


            finish_page.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Appreference.is_reload = true;
                    finish();
                }
            });

            submit_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProjectHistory.this, Perfomanceview.class);
                    intent.putExtra("is_project", "Y");
                    intent.putExtra("User_Project_Id", project_id);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showDialog() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                dialog = new ProgressDialog(ProjectHistory.this);
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

        Appreference.jsonRequestSender.getTask(EnumJsonWebservicename.getTask, nameValuePairs1, ProjectHistory.this);

    }

    public void refresh() {
        String query1;


        try {
            if (projectDetailsBeans != null) {
                projectDetailsBeans.clear();
            }

            if(projectDetailsBeanslist_ForSearch != null) {
                projectDetailsBeanslist_ForSearch.clear();
            }

            query1 = "select * from projectHistory where projectId='" + project_id + "' orderby parentTaskId";
            Log.d("DBQuery", "query is == " + query1);
            projectDetailsBeans = VideoCallDataBase.getDB(context).getProjectHistory(query);
            projectDetailsBeanslist_ForSearch = VideoCallDataBase.getDB(context).getProjectHistory(query);
            Log.i("DBQuery", "size of history " + projectDetailsBeans.size());
            Log.i("DBQuery", "userName " + userName);
            buddyArrayAdapter = new TaskArrayAdapter(context, projectDetailsBeans);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    listView.setAdapter(buddyArrayAdapter);
                    buddyArrayAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void onRefresh() {
        Log.i("ws123", "Refresh Request  received " );
        setActiveAdapter();
    }

    private void fetchTaskList() {
        Log.i("ws123", "Refresh Request  received fetchTaskList" );
        swipeRefreshLayout.setRefreshing(true);
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("projectId", project_id));
        nameValuePairs.add(new BasicNameValuePair("userId", String.valueOf(Appreference.loginuserdetails.getId())));
        Appreference.jsonRequestSender.getTaskForJobID(EnumJsonWebservicename.getTaskForJobID, nameValuePairs, ProjectsFragment.getInstance());
    }
    public void stopRefreshListener()
    {
        Log.i("ws123", "Refresh stop Request  received stopRefreshListener" );
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Appreference.context_table.remove("projecthistory");
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Noresult.setVisibility(View.GONE);
            if (check) {
                Log.i("DBQuery", "check " + check);
                Log.i("DBQuery", "check1111 " + check);
                setActiveAdapter();
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    /*
       */
    @Override
    public void ResponceMethod(final Object object) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.i("currentstatus", "Task Details Respons method");
                CommunicationBean communicationBean = (CommunicationBean) object;
                NewTaskConversation newTaskConversation = new NewTaskConversation();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(communicationBean.getEmail());
                    Log.i("jsonwebservice", communicationBean.getEmail());
                    if (jsonObject.has("result_code") && (int) jsonObject.get("result_code") == 0) {

                        TaskDetailsBean task = communicationBean.getTaskDetailsBean();
                        Log.e("current status", task.getTaskStatus());
                        if (task.getTaskStatus().equalsIgnoreCase("abandoned")) {
                            Log.i("Status ", ">>0 " + task.getTaskStatus());
                            Log.e("Status Updated", "set abandoned");
                            taskDetailsBean.setTaskDescription("This task is abandoned");
                            Log.i("Status ", ">>1");
                            newTaskConversation.taskReminderMessage(taskDetailsBean1, null, "text", null, null);
                            String xml = composeChatXML(taskDetailsBean1);
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
                            Log.i("Status ", ">>4");
                            VideoCallDataBase.getDB(context).getTaskHistory(query);
                            Log.i("Status ", ">>5");
                        } else {
                            Log.e("Status Updated", "set inprogress");
                            taskDetailsBean.setTaskDescription("This task is activated");
                            Log.i("Status ", "--->0");
                            newTaskConversation.taskReminderMessage(taskDetailsBean1, null, "text", null, null);
                            String xml = composeChatXML(taskDetailsBean1);
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
                            Log.i("Status ", "--->3");
                            VideoCallDataBase.getDB(context).getTaskHistory(query);
                            Log.i("Status ", "--->4");
                        }
                        Log.i("Status Updated", "set " + communicationBean.getEmail());
                        Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.get("id").toString().equals(webTaskId)) {
                        Gson gson = new Gson();
                        Log.i("getTask", "jelement.getAsJsonObject() != null");
                        ListAllgetTaskDetails listAllgetTaskDetails = gson.fromJson(jsonObject.toString(), ListAllgetTaskDetails.class);
                        Log.d("getTask", "firstname  " + listAllgetTaskDetails.getParentId());
                        Log.d("getTask", "firstname  " + listAllgetTaskDetails.getId());
                        Log.d("getTask", "firstname  " + listAllgetTaskDetails.getDescription());
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
                        Toast.makeText(ProjectHistory.this, "Not Updated network error", Toast.LENGTH_LONG).show();
                    }
//                    cancelDialog();

                } catch (JSONException e) {
                    Toast.makeText(ProjectHistory.this, "Json Error response", Toast.LENGTH_LONG).show();
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
//            buffer.append("reminderTone="+quotes+cmbean.getReminderTone()+ quotes);
//            buffer.append(" parentId=" + quotes + cmbean.getMsgtype() + quotes);
//            if (cmbean.getType().equals("CallSingleChat") || cmbean.getType().equals("CallGroupChat")) {
//                buffer.append(" activeURI=" + quotes + cmbean.getConferenceuri() + quotes);
//            } else {
//                buffer.append(" activeURI=" + quotes + "" + quotes);
//            }
//            buffer.append(" chatmembers="+quotes+cmbean.getChatmembers()+quotes);

            /*buffer.append("<message>" + "<![CDATA[" + cmbean.getMessage()
                    + "]]>");
            buffer.append("</message>");*/
//            buffer.append("</>");
//            buffer.append("<SendingStatus signalid=" + quotes
//                    + cmbean.getSendingStatusid() + quotes + " />");
//            buffer.append("<session callid=" + quotes
//                    + cmbean.getSessionCallid() + quotes + " />");
            buffer.append("</TaskDetailsinfo>");
            Log.e("xml", "composed xml for chat======>" + buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return buffer.toString();
        }
    }

    public void sendInstantMessage(String msgBody) {

//        String buddy_uri = "<"+number+">";
//        Log.i("chat", "buddy_uri======= " + buddy_uri);
//        BuddyConfig bCfg = new BuddyConfig();
//        bCfg.setUri(buddy_uri);
//        bCfg.setSubscribe(true);

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
                }
                break;
            }
        }

    }

    public String UTCToLocalTime(String time) {
        String formattedDate = null;
        Date myDate = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            TimeZone utcZone = TimeZone.getTimeZone("UTC");
            simpleDateFormat.setTimeZone(utcZone);
            try {
                if (time != null && !time.equalsIgnoreCase("(null)") && !time.equalsIgnoreCase("null") && !time.equalsIgnoreCase("") && !time.equalsIgnoreCase(null)) {
                    Log.i("gcmMessage", "time " + time);
                    myDate = simpleDateFormat.parse(time);
                    Log.i("gcmMessage", "myDate " + myDate);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            if (myDate != null) {
                formattedDate = simpleDateFormat.format(myDate);
                Log.i("gcmMessage", "formattedDate " + formattedDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    public class TaskArrayAdapter extends BaseAdapter implements Filterable {

        ArrayList<ProjectDetailsBean> arrayBuddyList;
        LayoutInflater inflater = null;
        Context adapContext;
//        ImageLoader imageLoader1;

        public TaskArrayAdapter(Context context, ArrayList<ProjectDetailsBean> buddyList1) {
            // super(context, R.layout.buddy_adapter_row, buddyList1);
            // TODO Auto-generated constructor stub
            this.arrayBuddyList = buddyList1;
            Log.d("task", "task list size inside = " + arrayBuddyList.size());
            adapContext = context;
//            imageLoader1 = new ImageLoader(adapContext);
            Log.d("task", "task list size inside = " + arrayBuddyList.size());

        }

        @Override
        public int getCount() {
            return arrayBuddyList.size();
        }

        public ProjectDetailsBean getItem(int position) {
            return arrayBuddyList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        public int getItemViewType(int position) {


            int value = 0;
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
            return value;
        }


        public int getViewTypeCount() {
            return 4;
        }

        public View getView(int pos, View conView, ViewGroup group) {
            try {
                if (conView == null) {
                    inflater = (LayoutInflater) adapContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    conView = inflater.inflate(R.layout.project_history_row, null,
                            false);
                }
                final ProjectDetailsBean projectDetailsBean = arrayBuddyList.get(pos);
                TextView task_giver = (TextView) conView.findViewById(R.id.task_giver);
//                ImageView imageView = (ImageView) conView.findViewById(R.id.state);

                TextView msg_count = (TextView) conView.findViewById(R.id.item_counter);
                TextView task_taker = (TextView) conView.findViewById(R.id.task_taker);
                TextView task_observer = (TextView) conView.findViewById(R.id.task_observer);
                TextView taskName = (TextView) conView.findViewById(R.id.project_name);
                TextView remain = (TextView) conView.findViewById(R.id.remain_count);
                TextView percentage_1 = (TextView) conView.findViewById(R.id.percent_update);
                TextView task_status = (TextView) conView.findViewById(R.id.project_status);
                TextView parent_startdate = (TextView) conView.findViewById(R.id.parent_startdate);
                View viewforparent = (View) conView.findViewById(R.id.viewforparent);
                viewforparent.setVisibility(View.GONE);
                TextView parent_enddate = (TextView) conView.findViewById(R.id.parent_enddate);
                TextView exclation_counter = (TextView) conView.findViewById(R.id.exclation_counter);

                ImageView selectedimage = (ImageView) conView.findViewById(R.id.selected);
                TextView catagory = (TextView) conView.findViewById(R.id.catagory);
                ImageView dependency_icon = (ImageView) conView.findViewById(R.id.dependency_icon);
                ImageView status_oracle = (ImageView) conView.findViewById(R.id.status_oracle);
                if(projectDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
                    status_oracle.setVisibility(View.VISIBLE);
                    task_status.setVisibility(View.GONE);
                }
                conView.setBackgroundResource(R.color.white);
                dependency_icon.setVisibility(View.GONE);
                try{
                    String s = "select * from taskDetailsInfo where taskId='"+projectDetailsBean.getTaskId()+"' and readStatus='1'";
                    ArrayList<ProjectDetailsBean> projectDetailsBeen = VideoCallDataBase.getDB(context).getExclationdetails(s);
                    if(projectDetailsBeen.size() > 0) {
                        exclation_counter.setVisibility(View.VISIBLE);
                        conView.setBackgroundResource(R.color.lgyellow);
                    }
                    else
                        exclation_counter.setVisibility(View.GONE);

                }catch (Exception e){
                    e.printStackTrace();
                }
//                Log.i("ws123","taskArrayAdapter values getParentTaskId---------->"+projectDetailsBean.getParentTaskId());
                status_oracle.setTag(pos);
                status_oracle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("ws123","taskArrayAdapter status_oracle---------->"+projectDetailsBean.getTaskType().equalsIgnoreCase("Group"));
                        if(projectDetailsBean.getTaskType().equalsIgnoreCase("Group"))
                        {
                            Intent intent = new Intent(ProjectHistory.this, GroupPercentageStatus.class);
                            intent.putExtra("taskid", projectDetailsBean.getTaskId());
                            intent.putExtra("groupId", group_UserId);
                            intent.putExtra("subtype", "normal");
                            if (isFromOracle) {
                                intent.putExtra("isProject", "yes");
                            } else {
                                intent.putExtra("isProject", "no");
                            }
                            if (isFromOracle)
                            intent.putExtra("isFromOracle",true);
                            startActivity(intent);
                        }
                    }
                });

                if (projectDetailsBean.getParentTaskId() != null && projectDetailsBean.getTaskId() != null) {
                    if (projectDetailsBean.getParentTaskId().equalsIgnoreCase(projectDetailsBean.getTaskId())) {

                        parent_startdate.setVisibility(View.VISIBLE);
                        parent_enddate.setVisibility(View.VISIBLE);
                        task_taker.setVisibility(View.GONE);
                        task_observer.setVisibility(View.GONE);
                        remain.setVisibility(View.GONE);
                        Log.i("ws123","taskArrayAdapter values taskID---------->"+projectDetailsBean.getTaskId());
                        Log.i("ws123","taskArrayAdapter values taskName---------->"+projectDetailsBean.getTaskName());

                        catagory.setText("Task Id: " + projectDetailsBean.getTaskId());
                        if (projectDetailsBean.getTaskName() != null) {
                            taskName.setText("Task Name: " + projectDetailsBean.getTaskName());
                            taskName.setTextColor(Color.BLUE);
                            viewforparent.setVisibility(View.VISIBLE);
                        } else {
                            viewforparent.setVisibility(View.VISIBLE);
                            taskName.setText("Task Name: " + "New Project");
                            taskName.setTextColor(Color.BLUE);
                        }
                        if (projectDetailsBean.getCompletedPercentage() != null && projectDetailsBean.getCompletedPercentage().equalsIgnoreCase("100")) {
                            percentage_1.setText(projectDetailsBean.getCompletedPercentage() + "%");
                            percentage_1.setTextColor(Color.GREEN);
                        } else if (projectDetailsBean.getCompletedPercentage() != null && !projectDetailsBean.getCompletedPercentage().equalsIgnoreCase(null) && !projectDetailsBean.getCompletedPercentage().equalsIgnoreCase("") && !projectDetailsBean.getCompletedPercentage().equalsIgnoreCase("null") && !projectDetailsBean.getCompletedPercentage().equalsIgnoreCase("(null)")) {
                            percentage_1.setText(projectDetailsBean.getCompletedPercentage() + "%");
                            percentage_1.setTextColor(Color.RED);
                        } else {
                            percentage_1.setText("0%");
                            percentage_1.setTextColor(Color.RED);
                        }
                        if (projectDetailsBean.getOwnerOfTask() != null) {
                            if (projectDetailsBean.getTaskStatus() != null && projectDetailsBean.getTaskStatus().equalsIgnoreCase("note")) {
                                if (projectDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    task_giver.setText("Me Giver : Me");
                                } else {
                                    if (VideoCallDataBase.getDB(context).getname(projectDetailsBean.getOwnerOfTask()) != null) {
                                        task_giver.setText("Me Giver : " + VideoCallDataBase.getDB(context).getname(projectDetailsBean.getOwnerOfTask()));
                                    } else {
                                        task_giver.setText("Me Giver : " + projectDetailsBean.getOwnerOfTask());
                                    }
                                }
                            } else {
                                if (projectDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    task_giver.setText("Task Giver : Me");
                                } else {
                                    if (VideoCallDataBase.getDB(context).getname(projectDetailsBean.getOwnerOfTask()) != null) {
                                        task_giver.setText("Task Giver : " + VideoCallDataBase.getDB(context).getname(projectDetailsBean.getOwnerOfTask()));
                                    } else {
                                        task_giver.setText("Task Giver : " + projectDetailsBean.getOwnerOfTask());
                                    }
                                }
                            }

                        } else {
                            if (projectDetailsBean.getTaskStatus() != null && projectDetailsBean.getTaskStatus().equalsIgnoreCase("note")) {
                                task_giver.setText("Me Giver : NA");
                            } else {
                                task_giver.setText("Task Giver : NA");
                            }
                        }
                        Log.i("project_details", "projectDetailsBean.getTaskStatus() " + projectDetailsBean.getTaskStatus());
                        if ((projectDetailsBean.getTaskStatus() != null && projectDetailsBean.getTaskStatus().equalsIgnoreCase("closed")) && (projectDetailsBean.getCompletedPercentage() != null && projectDetailsBean.getCompletedPercentage().equalsIgnoreCase("100"))) {
                            Log.i("ProjectHistory","projectDetailsBean.getTaskStatus() 1 "+projectDetailsBean.getTaskStatus());
                            task_status.setText(projectDetailsBean.getTaskStatus());
                            task_status.setTextColor(getResources().getColor(R.color.green));
                        } else if (projectDetailsBean.getTaskStatus() != null) {
                            Log.i("ProjectHistory","projectDetailsBean.getTaskStatus() 2 "+projectDetailsBean.getTaskStatus());
                            task_status.setText(projectDetailsBean.getTaskStatus());
                        } else {
                            Log.i("ProjectHistory","projectDetailsBean.getTaskStatus() 3 "+projectDetailsBean.getTaskStatus());
                            task_status.setText("NA");
                        }
                        Log.i("project_details", "projectDetailsBean getPlannedStartDateTime() " + projectDetailsBean.getPlannedStartDateTime());
                        if (projectDetailsBean.getPlannedStartDateTime() != null) {
                            String projectStart_date = UTCToLocalTime(projectDetailsBean.getPlannedStartDateTime().substring(0, 19));
                            parent_startdate.setText("Task Start date : " + projectStart_date);
                        } else {
                            parent_startdate.setText("Task Start date : NA");
                        }
                        Log.i("project_details", "projectDetailsBean getPlannedEndDateTime() " + projectDetailsBean.getPlannedEndDateTime());
                        if (projectDetailsBean.getPlannedEndDateTime() != null) {
                            String projectEnd_date = UTCToLocalTime(projectDetailsBean.getPlannedEndDateTime().substring(0, 19));
                            parent_enddate.setText("Task End date : " + projectEnd_date);
                        } else {
                            parent_enddate.setText("Task End date : NA");
                        }
                    } else{
                        task_taker.setVisibility(View.VISIBLE);
                        if (isFromOracle)
                            task_observer.setVisibility(View.GONE);
                        else
                            task_observer.setVisibility(View.VISIBLE);
                        parent_startdate.setVisibility(View.GONE);
                        parent_enddate.setVisibility(View.GONE);
                        taskName.setTextColor(Color.BLACK);
                        Log.i("task", "Project");
                        if(projectDetailsBean.getRequestStatus()!=null && projectDetailsBean.getRequestStatus().equalsIgnoreCase("Open")){
                            dependency_icon.setVisibility(View.VISIBLE);
                            Log.i("attention","resolved "+projectDetailsBean.getRequestStatus());
                        }else{
                            Log.i("attention","resolved else "+projectDetailsBean.getRequestStatus());
                            dependency_icon.setVisibility(View.GONE);
                        }
                        int project_unReadMsg_count = VideoCallDataBase.getDB(context).getTaskUnReadMsgCount(projectDetailsBean.getTaskId());
                        if (project_unReadMsg_count == 0) {
                            Log.d("ProjectHistory", "unRead_project_count is 0");
                            msg_count.setVisibility(View.GONE);
                            task_status.setPadding(0, 0, 0, 0);
                        } else {
                            Log.i("ProjectHistory", "unRead_project_count is " + project_unReadMsg_count);
                            msg_count.setVisibility(View.VISIBLE);
                            msg_count.setText(String.valueOf(project_unReadMsg_count));
                            task_status.setPadding(0, 0, 40, 0);
                        }

                        int project_reminder_count = videoCallDataBase.getRemainderUnReadMsgCount(projectDetailsBean.getTaskId());
                        if (project_reminder_count == 0) {
                            Log.d("ProjectHistory", "project_reminder_count is 0");
                            remain.setVisibility(View.GONE);
                        } else {
                            remain.setVisibility(View.VISIBLE);
                            remain.setText("Reminder : " + String.valueOf(project_reminder_count));
                            Log.i("ProjectHistory", "project_reminder_count is " + project_reminder_count);
                        }
                        Log.i("ProjectHistory", "project_members is " + projectDetailsBean.getTaskMemberList());
                        if (projectDetailsBean.getCompletedPercentage() != null && projectDetailsBean.getCompletedPercentage().equalsIgnoreCase("100")) {
                            percentage_1.setText(projectDetailsBean.getCompletedPercentage() + "%");
                            percentage_1.setTextColor(Color.GREEN);
                        } else if (projectDetailsBean.getCompletedPercentage() != null && !projectDetailsBean.getCompletedPercentage().equalsIgnoreCase(null) && !projectDetailsBean.getCompletedPercentage().equalsIgnoreCase("") && !projectDetailsBean.getCompletedPercentage().equalsIgnoreCase("null") && !projectDetailsBean.getCompletedPercentage().equalsIgnoreCase("(null)")) {
                            percentage_1.setText(projectDetailsBean.getCompletedPercentage() + "%");
                            percentage_1.setTextColor(Color.RED);
                        } else {
                            percentage_1.setText("0%");
                            percentage_1.setTextColor(Color.RED);
                        }
                        Log.i("ProjectHistory", "projectDetailsBean.getCatagory() " + projectDetailsBean.getCatagory());
                        if (projectDetailsBean.getCatagory() != null && (projectDetailsBean.getCatagory().equalsIgnoreCase("Task") || projectDetailsBean.getCatagory().equalsIgnoreCase("taskCreation"))) {
                            selectedimage.setBackgroundResource(R.drawable.task_icon);
                            catagory.setText("Task Id: " + projectDetailsBean.getTaskId());
                            taskName.setText("Task Name: " + projectDetailsBean.getTaskName());
                        } else if (projectDetailsBean.getCatagory() != null && projectDetailsBean.getCatagory().equalsIgnoreCase("issue")) {
                            selectedimage.setBackgroundResource(R.drawable.issue_icon);
                            catagory.setText("Issue Id: " + projectDetailsBean.getTaskId());
                            taskName.setText("Issue Name: " + projectDetailsBean.getTaskName());
                        } else if (projectDetailsBean.getCatagory() != null && projectDetailsBean.getCatagory().equalsIgnoreCase("note")) {
                            selectedimage.setBackgroundResource(R.drawable.ic_note_32_2);
                            catagory.setText("Note Id: " + projectDetailsBean.getTaskId());
                            taskName.setText("Note Name: " + projectDetailsBean.getTaskName());
                        } else if (projectDetailsBean.getCatagory() != null && projectDetailsBean.getCatagory().equalsIgnoreCase("Template")) {
                            selectedimage.setBackgroundResource(R.drawable.template);
                            catagory.setText("Template Id: " + projectDetailsBean.getTaskId());
                            taskName.setText("Template Name: " + projectDetailsBean.getTaskName());
                        }
                        Log.i("project_details", "projectDetailsBean getOwnerOfTask() " + projectDetailsBean.getOwnerOfTask());
                        if (projectDetailsBean.getOwnerOfTask() != null) {
                            if (projectDetailsBean.getTaskStatus() != null && projectDetailsBean.getTaskStatus().equalsIgnoreCase("note")) {
                                if (projectDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    task_giver.setText("Me Giver : Me");
                                } else {
                                    if (VideoCallDataBase.getDB(context).getname(projectDetailsBean.getOwnerOfTask()) != null) {
                                        task_giver.setText("Me Giver : " + VideoCallDataBase.getDB(context).getname(projectDetailsBean.getOwnerOfTask()));
                                    } else {
                                        task_giver.setText("Me Giver : " + projectDetailsBean.getOwnerOfTask());
                                    }
                                }
                            } else {
                                if (Appreference.loginuserdetails.getUsername()!=null && projectDetailsBean.getOwnerOfTask()!=null && projectDetailsBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    task_giver.setText("Task Giver : Me");
                                } else {

                                    if (VideoCallDataBase.getDB(context).getname(projectDetailsBean.getOwnerOfTask()) != null) {
                                        task_giver.setText("Task Giver : " + VideoCallDataBase.getDB(context).getname(projectDetailsBean.getOwnerOfTask()));
                                    } else {
                                        task_giver.setText("Task Giver : " + projectDetailsBean.getOwnerOfTask());
                                    }
                                }
                            }
                        } else {
                            if (projectDetailsBean.getTaskStatus() != null && projectDetailsBean.getTaskStatus().equalsIgnoreCase("note")) {
                                task_giver.setText("Me Giver : NA");
                            } else {
                                task_giver.setText("Task Giver : NA");
                            }
                        }

                        if (projectDetailsBean.getTaskStatus() != null && projectDetailsBean.getTaskStatus().equalsIgnoreCase("reminder")) {
                            Log.i("ProjectHistory","projectDetailsBean.getTaskStatus() 4 "+projectDetailsBean.getTaskStatus());
                            task_status.setText("inprogress");
                        } else if (projectDetailsBean.getTaskStatus() != null && projectDetailsBean.getTaskStatus().equalsIgnoreCase("overdue")) {
                            Log.i("ProjectHistory","projectDetailsBean.getTaskStatus() 5 "+projectDetailsBean.getTaskStatus());
                            task_status.setText(projectDetailsBean.getTaskStatus());
                            conView.setBackgroundResource(R.color.red_register);
                        } else if (projectDetailsBean.getTaskStatus() != null && projectDetailsBean.getTaskStatus().equalsIgnoreCase("closed")) {
                            Log.i("ProjectHistory","projectDetailsBean.getTaskStatus() 6 "+projectDetailsBean.getTaskStatus());
                            task_status.setText(projectDetailsBean.getTaskStatus());
                            task_status.setTextColor(getResources().getColor(R.color.green));
                        } else if (projectDetailsBean.getTaskStatus() != null && projectDetailsBean.getTaskStatus().equalsIgnoreCase("draft")) {
                            Log.i("ProjectHistory","projectDetailsBean.getTaskStatus() 7 "+projectDetailsBean.getTaskStatus());
                            task_status.setText("Template");
                        } else if (projectDetailsBean.getTaskStatus() != null) {
                            Log.i("ProjectHistory","projectDetailsBean.getTaskStatus() 8 "+projectDetailsBean.getTaskStatus());
                            task_status.setText(projectDetailsBean.getTaskStatus());
                        } else {
                            Log.i("ProjectHistory","projectDetailsBean.getTaskStatus() 9 "+projectDetailsBean.getTaskStatus());
                            task_status.setText("NA");
                        }
                        if (projectDetailsBean.getTaskStatus().equalsIgnoreCase("note")) {
                            selectedimage.setBackgroundResource(R.drawable.ic_note_32_2);
                            catagory.setText("Me : Name");
                        } else if (projectDetailsBean.getTaskReceiver() != null && projectDetailsBean.getOwnerOfTask() != null && projectDetailsBean.getTaskReceiver().equalsIgnoreCase(projectDetailsBean.getOwnerOfTask())) {
                            task_taker.setVisibility(View.VISIBLE);
                            task_taker.setText("Me");
                        }
                        Log.i("project_details", "projectDetailsBean getTaskReceiver() " + projectDetailsBean.getTaskReceiver());
                        if (projectDetailsBean.getTaskType() != null && projectDetailsBean.getTaskType().equalsIgnoreCase("individual")) {
                            if (projectDetailsBean.getTaskStatus() != null && projectDetailsBean.getTaskStatus().equalsIgnoreCase("note")) {
                                if (projectDetailsBean.getTaskReceiver() != null) {
                                    if (projectDetailsBean.getTaskReceiver().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                        task_taker.setText("Me Taker : Me");
                                    } else {
                                        if (VideoCallDataBase.getDB(context).getname(projectDetailsBean.getTaskReceiver()) != null) {
                                            task_taker.setText("Me Taker : " + VideoCallDataBase.getDB(context).getname(projectDetailsBean.getTaskReceiver()));
                                        } else {
                                            task_taker.setText("Me Taker : " + projectDetailsBean.getTaskReceiver());
                                        }
                                    }
                                } else {
                                    task_taker.setText("Me Taker : NA");
                                }
                            } else {
                                if (projectDetailsBean.getTaskReceiver() != null && !projectDetailsBean.getTaskReceiver().equalsIgnoreCase(null) && !projectDetailsBean.getTaskReceiver().equalsIgnoreCase("") && !projectDetailsBean.getTaskReceiver().equalsIgnoreCase("null") && !projectDetailsBean.getTaskReceiver().equalsIgnoreCase("(null)")) {
                                    if (projectDetailsBean.getTaskReceiver().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                        task_taker.setText("Task Taker : Me");
                                    } else {
                                        if (VideoCallDataBase.getDB(context).getname(projectDetailsBean.getTaskReceiver()) != null) {
                                            task_taker.setText("Task Taker : " + VideoCallDataBase.getDB(context).getname(projectDetailsBean.getTaskReceiver()));
                                        } else if (projectDetailsBean.getTaskReceiver() != null && !projectDetailsBean.getTaskReceiver().equalsIgnoreCase(null) && !projectDetailsBean.getTaskReceiver().equalsIgnoreCase("") && !projectDetailsBean.getTaskReceiver().equalsIgnoreCase("null") && !projectDetailsBean.getTaskReceiver().equalsIgnoreCase("(null)")) {
                                            task_taker.setText("Task Taker : " + projectDetailsBean.getTaskReceiver());
                                        } else {
                                            task_taker.setText("Task Taker : NA");
                                        }
                                    }
                                } else {
                                    task_taker.setText("Task Taker : NA");
                                }
                            }
                        } else {
                            int counter = 0;
                            String Pjt_mem = "", pjt_memName = "";
                            for (int i = 0; i < projectDetailsBean.getTaskMemberList().length(); i++) {
                                if (projectDetailsBean.getTaskMemberList().charAt(i) == ',') {
                                    counter++;
                                }
                                Log.d("project_details", "Task Mem's counter size is == " + counter);
                            }
                            for (int j = 0; j < counter + 1; j++) {
//                                    if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(projectDetailsBean.getTaskMemberList().split(",")[j])) {
//                                        listOfObservers.add(projectDetailsBean.getTaskMemberList().split(",")[j]);
                                Log.i("project_details", "Task Mem's and position == " + projectDetailsBean.getTaskMemberList().split(",")[j] + " " + j);
//                                    }
                                pjt_memName = VideoCallDataBase.getDB(context).getName(projectDetailsBean.getTaskMemberList().split(",")[j]);
                                if (pjt_memName != null) {
                                    Pjt_mem = Pjt_mem + pjt_memName + ",";
                                } else {
                                    Pjt_mem = Pjt_mem + "Me,";
                                }
                            }
                            Pjt_mem = Pjt_mem.substring(0, Pjt_mem.length() - 1);
                            if (projectDetailsBean.getTaskStatus() != null && projectDetailsBean.getTaskStatus().equalsIgnoreCase("note")) {
                                task_taker.setText("Me Taker : " + Pjt_mem);
                            } else {
                                task_taker.setText("Task Taker : " + Pjt_mem);
                                Log.i("project_details", "projectDetailsBean getTaskMem's() " + Pjt_mem);
                            }
                        }
                        Log.i("project_details", "projectDetailsBean getTaskObservers() " + projectDetailsBean.getTaskObservers());
                        if (projectDetailsBean.getTaskStatus() != null && projectDetailsBean.getTaskStatus().equalsIgnoreCase("note")) {
                            if (projectDetailsBean.getTaskObservers() != null && !projectDetailsBean.getTaskObservers().equalsIgnoreCase("") && !projectDetailsBean.getTaskObservers().equalsIgnoreCase(null)) {
                                if (projectDetailsBean.getTaskObservers() != null && projectDetailsBean.getTaskObservers().contains(",")) {
                                    int counter = 0;
                                    String Pjt_mem = "", pjt_memName = "";
                                    for (int i = 0; i < projectDetailsBean.getTaskObservers().length(); i++) {
                                        if (projectDetailsBean.getTaskObservers().charAt(i) == ',') {
                                            counter++;
                                        }
                                        Log.d("project_details", "Task Mem's counter size is == " + counter);
                                    }
                                    for (int j = 0; j < counter + 1; j++) {
//                                    if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(projectDetailsBean.getTaskMemberList().split(",")[j])) {
//                                        listOfObservers.add(projectDetailsBean.getTaskMemberList().split(",")[j]);
                                        Log.i("project_details", "Task Mem's and position == " + projectDetailsBean.getTaskObservers().split(",")[j] + " " + j);
//                                    }
                                        pjt_memName = VideoCallDataBase.getDB(context).getName(projectDetailsBean.getTaskObservers().split(",")[j]);
                                        if (pjt_memName != null) {
                                            Pjt_mem = Pjt_mem + pjt_memName + ",";
                                        } else {
                                            Pjt_mem = Pjt_mem + "Me,";
                                        }
                                    }
                                    Pjt_mem = Pjt_mem.substring(0, Pjt_mem.length() - 1);
                                    task_observer.setText("Me Observer : " + Pjt_mem);
                                    Log.i("project_details", "projectDetailsBean getTaskMem's() " + Pjt_mem);
                                } else {
                                    if (projectDetailsBean.getTaskObservers().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                        task_observer.setText("Me Observer : Me");
                                    } else {
                                        if (VideoCallDataBase.getDB(context).getname(projectDetailsBean.getTaskObservers()) != null) {
                                            task_observer.setText("Me Observer : " + VideoCallDataBase.getDB(context).getname(projectDetailsBean.getTaskObservers()));
                                        } else {
                                            task_observer.setText("Me Observer : " + projectDetailsBean.getTaskObservers());
                                        }
                                    }

                                }
                            } else {
                                task_observer.setText("Me Observer : NA");
                            }
                        } else {
                            if (projectDetailsBean.getTaskObservers() != null && !projectDetailsBean.getTaskObservers().equalsIgnoreCase("") && !projectDetailsBean.getTaskObservers().equalsIgnoreCase(null)) {
                                if (projectDetailsBean.getTaskObservers() != null && projectDetailsBean.getTaskObservers().contains(",")) {
                                    int counter = 0;
                                    String Pjt_mem = "", pjt_memName = "";
                                    for (int i = 0; i < projectDetailsBean.getTaskObservers().length(); i++) {
                                        if (projectDetailsBean.getTaskObservers().charAt(i) == ',') {
                                            counter++;
                                        }
                                        Log.d("project_details", "Task Mem's counter size is == " + counter);
                                    }
                                    for (int j = 0; j < counter + 1; j++) {
//                                    if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(projectDetailsBean.getTaskMemberList().split(",")[j])) {
//                                        listOfObservers.add(projectDetailsBean.getTaskMemberList().split(",")[j]);
                                        Log.i("project_details", "Task Mem's and position == " + projectDetailsBean.getTaskObservers().split(",")[j] + " " + j);
//                                    }
                                        pjt_memName = VideoCallDataBase.getDB(context).getName(projectDetailsBean.getTaskObservers().split(",")[j]);
                                        if (pjt_memName != null) {
                                            Pjt_mem = Pjt_mem + pjt_memName + ",";
                                        } else {
                                            Pjt_mem = Pjt_mem + "Me,";
                                        }
                                    }
                                    Pjt_mem = Pjt_mem.substring(0, Pjt_mem.length() - 1);
                                    task_observer.setText("Task Observer : " + Pjt_mem);
                                    Log.i("project_details", "projectDetailsBean getTaskMem's() " + Pjt_mem);
                                } else {
                                    if (projectDetailsBean.getTaskObservers().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                        task_observer.setText("Task Observer : Me");
                                    } else {
                                        if (VideoCallDataBase.getDB(context).getname(projectDetailsBean.getTaskObservers()) != null) {
                                            task_observer.setText("Task Observer : " + VideoCallDataBase.getDB(context).getname(projectDetailsBean.getTaskObservers()));
                                        } else {
                                            task_observer.setText("Task Observer : " + projectDetailsBean.getTaskObservers());
                                        }
                                    }
                                }
                            } else {
                                task_observer.setText("Task Observer : NA");
                            }
                        }
                    }
                }
                String s = null;
            } catch (Exception e) {
                e.printStackTrace();
            }


            return conView;

        }

        @Override
        public Filter getFilter() {
            if (filter == null) {
                Log.d("constraint", "JNDSEJBJW  ** ");
                filter = new ProjectHistoryFilter();
            }
            return filter;
        }
    }

    private class ProjectHistoryFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            //constraint = constraint.toString();
            String s1 = constraint.toString().toLowerCase();

            Log.d("constraint", "filter : 0 " + s1);

            Filter.FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                Log.i(" constraint ", "  1 " + constraint.toString().length());
                ArrayList<ProjectDetailsBean> taskdetailsbeanlist = new ArrayList<>();
                ArrayList<ProjectDetailsBean> s = new ArrayList<ProjectDetailsBean>();

                for (int i = 0, l = projectDetailsBeanslist_ForSearch.size(); i < l; i++) {
                    Log.i("constraint", "insidefor 2 " + projectDetailsBeanslist_ForSearch.size());
                    s.add(projectDetailsBeanslist_ForSearch.get(i));
                    Log.i("constraint", " s 3 " + s.get(i).getProjectName());
                    //Log.i("sizes","sizes"+s.toString());
                    String s2 = s.get(i).getTaskId().toString();
                    String s3 = s.get(i).getTaskName().toString().toLowerCase();
                    Log.i(" constraint ", " s2  4 " + s2.toString());

                    if (s2.contains(s1)) {
                        Log.i("constraint", "insideif 5 " + String.valueOf(constraint));
                        taskdetailsbeanlist.add(s.get(i));
                        Log.i("constraint", "List 6 " + taskdetailsbeanlist.toString());
                    } else if (s3.contains(s1)) {
                        taskdetailsbeanlist.add(s.get(i));
                    }
                }
                result.values = taskdetailsbeanlist;
                Log.i("constraint", "result 7 " + result.values.toString());
                result.count = taskdetailsbeanlist.size();
                Log.i("constraint", "resultcount 8 " + result.count);

            } else {
                synchronized (this) {
                    result.values = projectDetailsBeanslist_ForSearch;
                    Log.i("constraint", "resultelse 9 " + result.values);
                    result.count = projectDetailsBeanslist_ForSearch.size();
                    Log.i("constraint", "resultelsecount 10 " + result.count);

                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            Log.d("constraint", "JNDSEJBJW  11 " + results.count);
            projectDetailsBeans.clear();
            filterbuddy = (ArrayList<ProjectDetailsBean>) results.values;
//            Log.i("filterbuddy", "filterbuddy" + filterbuddy.toString());
            //            buddyList.clear();
            if (filterbuddy.size() > 0) {
                Noresult.setVisibility(View.GONE);
                for (int i = 0, l = filterbuddy.size(); i < l; i++) {
                    //Log.i("result","filter"+filterbuddy.get(i).toString());
                    // buddyList.add(filterbuddy.get(i));
                    Log.d("constraint", "JNDSEJBJW  12 " + results.count);
                    projectDetailsBeans.add(filterbuddy.get(i));
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
            LayoutInflater inflater = getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.customspin, parent,
                    false);
            TextView main_text = (TextView) mySpinner.findViewById(R.id.customtext);
            main_text.setText(catestring1[position]);
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
            LayoutInflater inflater = getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.customspin, parent,
                    false);
            TextView main_text = (TextView) mySpinner.findViewById(R.id.customtext);
            main_text.setText(catestring2[position]);
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
            LayoutInflater inflater = getLayoutInflater();
            View mySpinner = inflater.inflate(R.layout.customspin, parent,
                    false);
            TextView main_text = (TextView) mySpinner.findViewById(R.id.customtext);
            main_text.setText(catestring3[position]);
            return mySpinner;
        }
    }
}
