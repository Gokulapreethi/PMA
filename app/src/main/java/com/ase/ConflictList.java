package com.ase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.ase.DB.VideoCallDataBase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.ase.R.id.switch1;

/**
 * Created by Ramdhas on 25-10-2016.
 */
public class ConflictList extends Activity {
    DisplayMetrics metrics;
    int width = 0;
    int height = 0;
    LinearLayout linear;
    Handler handler;
    private static ConflictListAdapter.ViewHolderItem holder;
    SwipeMenuListView listView;
    Calendar cs, ce;
    String intentValue = "";
    public String Leave_Taskid, Start_Date, End_Date;
    ArrayList<ConflictCheckBean> LeaveList = new ArrayList<ConflictCheckBean>();
    ArrayList<ConflictCheckBean> taskList1 = new ArrayList<ConflictCheckBean>();
    //    int pos = -1;
    Context context;
    Date ex_date = null;
    String Last_Extend = null/*, to_user_id = null, tasktype = null*/;
    long diffDays;
    //    TextView assign, change, ok, details_conflict;
    TextView details_conflict;
    Button assign, change, ok, reject_leave, change_date;
    View view;

    Date Start = null, End = null;
    Date taskStart_Date = null, taskEnd_Date = null;
    ConflictCheckBean selectedbean;
    //    ConflictCheckBean checkBean;
    public ArrayList<ConflictCheckBean> conflictChecklist = new ArrayList<>();
    NewTaskConversation newTaskConversation;
    public TaskDateUpdate taskDateUpdate;
    boolean conflict_check = false;
//    boolean ischeckConflict = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conflictlist);
        context = this;
        listView = (SwipeMenuListView) findViewById(R.id.lv_taskHistory123);
        metrics = getResources().getDisplayMetrics();
        width = metrics.widthPixels;
        height = metrics.heightPixels / 2;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        linear = (LinearLayout) findViewById(R.id.main_layout);
        ViewGroup.LayoutParams layoutParams = linear.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;
        linear.setLayoutParams(layoutParams);
        assign = (Button) findViewById(R.id.save);
        reject_leave = (Button) findViewById(R.id.reject_leave);
        change_date = (Button) findViewById(R.id.change_date);
        change = (Button) findViewById(R.id.cancel);
        ok = (Button) findViewById(R.id.ok);
        details_conflict = (TextView) findViewById(R.id.details_conflict);
        view = (View) findViewById(R.id.view);
        String conversation_In = "";
        if (getIntent() != null) {
            intentValue = getIntent().getExtras().getString("Leave");
        }
        try {
            switch (intentValue) {
                case "NewTaskConversation":
                    Start_Date = getIntent().getExtras().getString("Start_Date");
                    Log.i("NewTaskConversation", "local ** " + Start_Date);
                    Log.i("NewTaskConversation", "conflict start > " + Start_Date);
                    End_Date = getIntent().getExtras().getString("End_Date");
//            End_Date=Appreference.utcToLocalTime(End_Date);
                    Log.i("NewTaskConversation", "utc *#*# " + End_Date);
                    Leave_Taskid = getIntent().getExtras().getString("Leave_Taskid");
                    /*tasktype = getIntent().getExtras().getString("taskType");*/
                    conflictChecklist = (ArrayList<ConflictCheckBean>) getIntent().getExtras().getSerializable("conflictobject");
                    taskList1.addAll(conflictChecklist);
                    break;
                case "TaskDateUpdate":
                    Log.i("TaskDateUpdate", "local ** ");
                    Leave_Taskid = getIntent().getExtras().getString("LeaveTaskId");
                    conflictChecklist = (ArrayList<ConflictCheckBean>) getIntent().getExtras().getSerializable("conflictobject");
                    Log.i("TaskDateUpdate", " Conflict Leave_Taskid ** "+Leave_Taskid);
                    for (ConflictCheckBean checkBean : conflictChecklist) {
                        if (checkBean.getTaskId().equalsIgnoreCase(Leave_Taskid)) {
                            conflictChecklist.remove(checkBean);
                            break;
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (Appreference.isconflicttaker || Appreference.isLeaveConflict) {
//            newTaskConversation = NewTaskConversation.getInstance();
//            conflictChecklist.addAll(newTaskConversation.conflictobject);
//            taskList1.addAll(newTaskConversation.conflictobject);
            Log.i("ConflictList", "isconflicttaker if " + Appreference.isconflicttaker);
//            Log.i("conflict","ischeckConflict " +Appreference.ischeckConflict);
        } else {
            Log.i("ConflictList", "isconflicttaker else " + Appreference.isconflicttaker);
//            Log.i("conflict","ischeckConflict " +Appreference.ischeckConflict);
//            Leave_Taskid = getIntent().getStringExtra("LeaveTaskId");
//            Log.i("conflict", "Leave_Taskid " + Leave_Taskid);
//            taskDateUpdate = TaskDateUpdate.getInstance();
//            conflictChecklist.addAll(taskDateUpdate.conflictobject);
//            for (ConflictCheckBean checkBean : conflictChecklist) {
//                if (checkBean.getTaskId().equalsIgnoreCase(Leave_Taskid)) {
//                    conflictChecklist.remove(checkBean);
//                    break;
//                }
//            }
        }
        if (Appreference.isAssignLeave) {
            ok.setVisibility(View.GONE);
            assign.setVisibility(View.GONE);
            change.setVisibility(View.GONE);
            reject_leave.setVisibility(View.VISIBLE);
            change_date.setVisibility(View.VISIBLE);
//            assign.setText("Approve\nLeave");
//            change.setText("Reject\nLeave");
        } /*else {
            Log.i("conflict", "isconflicttaker else " + Appreference.isconflicttaker);
            ok.setVisibility(View.VISIBLE);
//            details_conflict.setVisibility(View.VISIBLE);
//            view.setVisibility(View.VISIBLE);
            assign.setVisibility(View.GONE);
            change.setVisibility(View.GONE);
        }*/

        if (Appreference.isLeaveConflict) {
//            Bundle intent = getIntent().getExtras();
//            Start_Date = intent.getString("Start_Date");
//            Log.i("UTC", "utc * " + Start_Date);
////            Start_Date=Appreference.utcToLocalTime(Start_Date);
//            Log.i("UTC", "local ** " + Start_Date);
//            Log.i("extend", "conflict start > " + Start_Date);
//            End_Date = intent.getString("End_Date");
//            Log.i("UTC", "utc *# " + End_Date);
////            End_Date=Appreference.utcToLocalTime(End_Date);
//            Log.i("UTC", "utc *#*# " + End_Date);
//            Leave_Taskid = intent.getString("Leave_Taskid");
//            tasktype = intent.getString("taskType");

//            for (ConflictCheckBean checkBean : conflictChecklist) {
//                if (checkBean.getTaskId().equalsIgnoreCase(Leave_Taskid)) {
//                    conflictChecklist.remove(checkBean);
//                    break;
//                }
//            }
        }

        ConflictListAdapter conflictListAdapter = new ConflictListAdapter(getApplicationContext(), conflictChecklist);
        listView.setAdapter(conflictListAdapter);
        conflictListAdapter.notifyDataSetChanged();
        if (Appreference.isconflicttaker) {
//            Log.i("conflict", "ischeckConflict " + Appreference.ischeckConflict);
            Log.i("ConflictList", "isconflicttaker else " + Appreference.isconflicttaker);
            ok.setVisibility(View.VISIBLE);
//            details_conflict.setVisibility(View.VISIBLE);
//            view.setVisibility(View.VISIBLE);
            assign.setVisibility(View.GONE);
            change.setVisibility(View.GONE);
        }
        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appreference.isConflict = true;
                finish();
            }
        });
        change_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LeaveList.size() > 0) {
                    Log.i("ConflictList", "conflict 123" + Appreference.isAssignLeave);
                    if (LeaveList.size() > 0) {
                        Log.i("ConflictList", "last end date to conversation " + LeaveList.size());
                         /*checkBean.setPlannedEndDate(Last_Extend);
                        Log.i("Last_Extend", "task type" + tasktype);
                        checkBean.setTaskType(tasktype);
                        Log.i("Last_Extend", "task id " + checkBean.getTaskId());
                        Log.i("Last_Extend", "start date to conversation " + checkBean.getPlannedStartDate());
                        Log.i("Last_Extend", "end date to conversation " + checkBean.getPlannedEndDate());
                        Log.i("Last_Extend", " type " + checkBean.getTaskType());
                        Log.i("Last_Extend", " touser name " + checkBean.getToUserName());
                        Log.i("Last_Extend", "task name " + checkBean.getTaskName());
                        Log.i("Last_Extend", "status " + checkBean.getTaskStatus());
                        Log.i("Last_Extend", "priority " + checkBean.getTaskPriority());
                        Log.i("Last_Extend", "task no " + checkBean.getTaskNo());*/
//                    Log.i("Last_Extend","1"+checkBean.getFromUserName());
//                    Log.i("Last_Extend","1"+checkBean.getFromUserId());
                        Intent intent = new Intent();
//                    intent.putExtra("fromusername", checkBean.getFromUserName());

//                        intent.putExtra("taskid", checkBean.getTaskId());
//                        intent.putExtra("startdate", checkBean.getPlannedStartDate());
//                        intent.putExtra("enddate", checkBean.getPlannedEndDate());
//                    intent.putExtra( "touserid" , checkBean.getToUserId());
                        intent.putExtra("extend", "true");
                        intent.putExtra("Leave_Taskid", Leave_Taskid);
//                        intent.putExtra("taskno", checkBean.getTaskNo());
//                        intent.putExtra("tousername", checkBean.getToUserName());
//                    to_user_id = String.valueOf(VideoCallDataBase.getDB(context).getUserid(checkBean.getToUserName()));
//                        intent.putExtra("fromuserid", checkBean.getFromUserId());
//                        intent.putExtra("taskstatus", checkBean.getTaskStatus());
//                        intent.putExtra("taskpriority", checkBean.getTaskPriority());
//                        intent.putExtra("taskname", checkBean.getTaskName());
//                    intent.putExtra("to_user_id",checkBean.getToUserId());
//                        intent.putExtra("taskType", checkBean.getTaskType());
                        intent.putExtra("LeaveList", LeaveList);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
//                        Toast.makeText(getApplicationContext(),"Please select the Task to Extend",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("extend", "false");
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });
        reject_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("Leave_Taskid", Leave_Taskid);
                Appreference.isonlyLeaveApprove = true;
                Log.i("ConflictList", "only leave approve" + Appreference.isAssignLeave);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);//minus number would decrement the days
        return cal.getTime();
    }

    class ConflictListAdapter extends BaseAdapter {
        Context context;
        ArrayList<ConflictCheckBean> taskList;
        LayoutInflater inflater = null;

        ConflictListAdapter(Context context, ArrayList<ConflictCheckBean> taskList) {
            this.context = context;
            this.taskList = taskList;
        }

        @Override
        public int getCount() {
            return taskList.size();
        }

        @Override
        public Object getItem(int position) {
            return taskList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            holder = new ViewHolderItem();
            if (convertView == null) {
                inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.conflictlistrow, null,
                        false);
                holder.fromUser = (TextView) convertView.findViewById(R.id.fromUser);
                holder.extend = (Switch) convertView.findViewById(switch1);
                holder.taskGiver = (TextView) convertView.findViewById(R.id.task_giverval);
                holder.taskTaker = (TextView) convertView.findViewById(R.id.task_takerval);
                holder.taskStatus = (TextView) convertView.findViewById(R.id.status);
                holder.startDate = (TextView) convertView.findViewById(R.id.start_Date);
                holder.endDate = (TextView) convertView.findViewById(R.id.enddate);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolderItem) convertView.getTag();
            }
            handler = new Handler();
            final ConflictCheckBean conflictCheckBean = taskList.get(position);

            if (Appreference.isLeaveConflict) {
                if ((conflictCheckBean.getPlannedStartDate() != null && !conflictCheckBean.getPlannedStartDate().equalsIgnoreCase("") && !conflictCheckBean.getPlannedStartDate().equalsIgnoreCase(null) && !conflictCheckBean.getPlannedStartDate().equalsIgnoreCase("(null)")) && (conflictCheckBean.getPlannedEndDate() != null && !conflictCheckBean.getPlannedEndDate().equalsIgnoreCase("") && !conflictCheckBean.getPlannedEndDate().equalsIgnoreCase("(null)"))) {
                    holder.extend.setVisibility(View.VISIBLE);
                } else {
                    holder.extend.setVisibility(View.GONE);
                }
//                        extend.setVisibility(View.VISIBLE);
                holder.taskStatus.setVisibility(View.GONE);
//                Appreference.isLeaveConflict=false;
            }
//            Appreference.isLeaveConflict=false;
            holder.fromUser.setText(conflictCheckBean.getTaskName());
            if (conflictCheckBean.getFromUserName() != null && conflictCheckBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                holder.taskGiver.setText("Me");
            } else {
                String Name = VideoCallDataBase.getDB(context).getName(conflictCheckBean.getFromUserName());
                holder.taskGiver.setText(Name);
            }
            if (conflictCheckBean.getToUserName() != null && conflictCheckBean.getToUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                holder.taskTaker.setText("Me");
            } else {
                String Name = VideoCallDataBase.getDB(context).getName(conflictCheckBean.getToUserName());
                holder.taskTaker.setText(Name);
            }
            holder.taskStatus.setText(conflictCheckBean.getTaskStatus());
            if (conflictCheckBean.getPlannedStartDate() != null && !conflictCheckBean.getPlannedStartDate().equalsIgnoreCase("") && !conflictCheckBean.getPlannedStartDate().equalsIgnoreCase("(null)")) {
                holder.startDate.setText("From Date : " + Appreference.utcToLocalTime(conflictCheckBean.getPlannedStartDate()));
            } else {
                holder.startDate.setText("From Date : N/A");
            }
            if (conflictCheckBean.getPlannedEndDate() != null && !conflictCheckBean.getPlannedEndDate().equalsIgnoreCase("") && !conflictCheckBean.getPlannedEndDate().equalsIgnoreCase("(null)")) {
                holder.endDate.setText("To Date : " + Appreference.utcToLocalTime(conflictCheckBean.getPlannedEndDate()));
            } else {
                holder.endDate.setText("To Date : N/A");
            }
            Log.i("LastConflictObject", "BeanObject 3  ischecked() " + conflictCheckBean.ischecked() + " position " + position + "  LeaveList.Size() " + LeaveList.size());
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
            holder.extend.setOnCheckedChangeListener(null);
            if (conflictCheckBean.ischecked()) {
                holder.extend.setChecked(true);
//                        notifyDataSetChanged();
                Log.i("extend", "if if");
            } else {
                Log.i("extend", "else else");
                holder.extend.setChecked(false);
//                        notifyDataSetChanged();
            }
            holder.extend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = position;
                    ConflictCheckBean checkBean = conflictChecklist.get(pos);
                    Log.i("LastConflictObject", "BeanObject 00 " + isChecked + "  pos ");
                    if (isChecked == true) {
                        conflictCheckBean.setIschecked(true);
                        Log.i("ConflictList", "if");
                        holder.extend.setTag(1);
                        Log.i("extend", "pos" + pos);

                        Log.i("ConflictList", "from user name " + checkBean.getFromUserName());
                        Log.i("ConflictList", "task id " + checkBean.getTaskId());
                        Log.i("ConflictList", "start date " + checkBean.getPlannedStartDate());
                        Log.i("ConflictList", "end date " + checkBean.getPlannedEndDate());
                        Log.i("ConflictList", "to user id " + checkBean.getToUserId());
                        Log.i("ConflictList", "task no " + checkBean.getTaskNo());
                        Log.i("ConflictList", "to user name " + checkBean.getToUserName());
//                        to_user_id = String.valueOf(VideoCallDataBase.getDB(context).getUserid(checkBean.getToUserName()));
                        Log.i("ConflictList", "from user id " + checkBean.getFromUserId());
                        Log.i("ConflictList", "task status " + checkBean.getTaskStatus());
                        Log.i("ConflictList", "task priority " + checkBean.getTaskPriority());
                        Log.i("ConflictList", "task name " + checkBean.getTaskName());
                        Log.i("ConflictList", "task type " + checkBean.getTaskType());
                        Log.i("LastConflictObject", "BeanObject 1 " + taskList.get(pos).ischecked() + "  pos : " + pos + "    " + LeaveList.size());
                        conflictChecklist.get(pos).setIschecked(true);
                        if (checkBean.getPlannedEndDate() != null && !checkBean.getPlannedEndDate().equalsIgnoreCase("")) {
                            extendDate(pos, checkBean);
                        } else {

                        }

                        /*Bundle intent=getIntent().getExtras();
                        String Start_Date=intent.getString("Start_Date");
                        Log.i("extend","conflict start >"+Start_Date);
                        String End_Date=intent.getString("End_Date");
                        Log.i("extend","conflict end >"+End_Date);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        try {
                            Start = dateFormat.parse(Start_Date);
                            Log.i("extend","conflict start date >"+Start);
                            End=dateFormat.parse(End_Date);
                            Log.i("extend","conflict end date >"+End);
                            cs=Calendar.getInstance();
                            ce=Calendar.getInstance();
                            cs.setTime(Start);
                            ce.setTime(End);
                            long csTimeInMillis =cs.getTimeInMillis();
                            long ceTimeInMillis = ce.getTimeInMillis();
                            long diff = ceTimeInMillis-csTimeInMillis;
                            diffDays = diff / (24 * 60 * 60 * 1000);
                            String ex=String.valueOf(diffDays);
                            int dif=Integer.valueOf(ex);
                            Log.i("extend","different b/w start and end date >"+diffDays);
                            ex_date=addDays(End,dif);
                            Log.i("extend","ex_date..>..>"+ex_date);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }*/
//                        notifyDataSetChanged();
                    } else {
//                        taskList.get(pos).setIschecked(false);
//                        taskList1.get(pos).setIschecked(false);
                        conflictCheckBean.setIschecked(false);
                        conflictChecklist.get(pos).setIschecked(false);
                        holder.extend.setTag(0);
//                        checkBean.setIschecked(false);
//                        conflictCheckBean.setIschecked(false);
                        if (LeaveList.size() > 0) {
                            ConflictCheckBean checkBean1 = conflictChecklist.get(pos);
//                            ConflictCheckBean checkBean2=LeaveList.get(pos);
//                            for (ConflictCheckBean checkBean1 : taskList) {
//                                for (ConflictCheckBean conflictCheckBean1:LeaveList) {
//                                    Log.i("extend", "else for ");
//                                    if (conflictCheckBean1.getTaskId() == checkBean1.getTaskId()) {
//                                        Log.i("extend", "else for if");
//                                        LeaveList.remove(conflictCheckBean1);
//                                    }
//                                }
                            if (LeaveList.contains(checkBean1)) {
                                LeaveList.remove(checkBean1);
                            }
//                                }

//                            }
                        }
//                        checkBean = taskList1.get(pos);
                        Log.i("LastConflictObject", "BeanObject 2 " + taskList.get(pos).ischecked() + "   " + taskList.get(pos).ischecked() + "     " + LeaveList.size());
//                        checkBean.setIschecked(false);
//                        checkBean = null;
//                        pos = -1;
                        Log.i("extend", "else");
//                        notifyDataSetChanged();
                    }
                }
            });
            return convertView;
        }

        public class ViewHolderItem {
            TextView fromUser, taskGiver, taskTaker, taskStatus, startDate, endDate;
            //            final Switch extend = (Switch) convertView.findViewById(R.id.switch1);
//            TextView taskGiver = (TextView) convertView.findViewById(R.id.task_giverval);
//            TextView taskTaker = (TextView) convertView.findViewById(R.id.task_takerval);
//            final TextView taskStatus = (TextView) convertView.findViewById(R.id.status);
//            TextView startDate = (TextView) convertView.findViewById(R.id.start_Date);
//            TextView endDate = (TextView) convertView.findViewById(R.id.enddate);
            Switch extend;
        }
    }

    private void extendDate(int pos, ConflictCheckBean checkBean2) {
        if (pos > -1) {
//            Bundle intent = getIntent().getExtras();
//            String Start_Date = intent.getString("Start_Date");
//            Log.i("UTC", "utc * " + Start_Date);
////            Start_Date=Appreference.utcToLocalTime(Start_Date);
//            Log.i("UTC", "local ** " + Start_Date);
//            Log.i("extend", "conflict start > " + Start_Date);
//            String End_Date = intent.getString("End_Date");
//            Log.i("UTC", "utc *# " + End_Date);
////            End_Date=Appreference.utcToLocalTime(End_Date);
//            Log.i("UTC", "utc *#*# " + End_Date);
//            Leave_Taskid = intent.getString("Leave_Taskid");
//            tasktype = intent.getString("taskType");
//            Log.i("extend", "conflict end >" + End_Date);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                Start = dateFormat.parse(Start_Date);
                SimpleDateFormat day1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String Start_1 = day1.format(Start);
                Log.i("ConflictList", "date start date > " + Start_1);
                End = dateFormat.parse(End_Date);
                String End_1 = day1.format(End);
                Log.i("ConflictList", "date end date > " + End_1);
                String confilt_end = Appreference.utcToLocalTime(checkBean2.getPlannedEndDate());
                Log.i("ConflictList", "local confilt end date > " + confilt_end);
                taskEnd_Date = dateFormat.parse(confilt_end);
                cs = Calendar.getInstance();
                ce = Calendar.getInstance();
                cs.setTime(Start);
                ce.setTime(End);
                long csTimeInMillis = cs.getTimeInMillis();
                long ceTimeInMillis = ce.getTimeInMillis();
                long diff = ceTimeInMillis - csTimeInMillis;
                diffDays = diff / (24 * 60 * 60 * 1000);
                String ex = String.valueOf(diffDays);
                int dif = Integer.valueOf(ex);
                Log.i("ConflictList", "different b/w start and end date >" + diffDays);
                ex_date = addDays(taskEnd_Date, dif);
                String ex_date_1 = day1.format(ex_date);
                Log.i("ConflictList", "ex_date..>..>" + ex_date_1);
                Last_Extend = ex_date_1;
                Log.i("ConflictList", "Last extend utc *1 " + Last_Extend);
//                Last_Extend=Appreference.utcToLocalTime(Last_Extend);
                Log.i("ConflictList", "Last extend local *1*1 " + checkBean2.getPlannedStartDate());
                Log.i("ConflictList", "Last extend utc *1 " + Last_Extend);
                checkBean2.setPlannedStartDate(Appreference.utcToLocalTime(checkBean2.getPlannedStartDate()));
                checkBean2.setPlannedEndDate(Appreference.utcToLocalTime(Last_Extend));
                Log.i("ConflictList","Leave List To Username and To UserId "+checkBean2.getToUserId()+"   "+checkBean2.getToUserName());
                LeaveList.add(checkBean2);
                Log.i("ConflictList", "Last extend local *1*1 " + checkBean2.getPlannedStartDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Appreference.isLeaveConflict = false;
    }
}
