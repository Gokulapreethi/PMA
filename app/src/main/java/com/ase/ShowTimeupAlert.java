package com.ase;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;

import org.pjsip.pjsua2.app.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Preethi on 9/2/2017.
 */

public  class ShowTimeupAlert extends Activity implements DateTimePicker.DateWatcher {
    TextView tv_relevant_id,tv_cancel_alert,tv_snooze;
    Context context;
     String my_taskId;
     String my_status;
    String my_projectId;
    String OracleprojectId;
    String OracletaskId;
    private static Handler handler;
    CounterClass counter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showtimeup);
        Appreference.context_table.put("showtimeralert", this);
        context=this;
        handler=new Handler();
        my_projectId=getIntent().getStringExtra("projectId");
        my_status=getIntent().getStringExtra("status");
        my_taskId=getIntent().getStringExtra("taskId");
        OracleprojectId=getIntent().getStringExtra("OracleprojectId");
        OracletaskId=getIntent().getStringExtra("OracletaskId");
        tv_relevant_id = (TextView) findViewById(R.id.relevant_id);
        TextView tv_cancel_alert = (TextView) findViewById(R.id.cancel_alert);
        final TextView tv_snooze = (TextView) findViewById(R.id.snooze);
        tv_relevant_id.setText("Job Card No :" + OracleprojectId + "\n" + "Activity Code : " + OracletaskId + "  is " + my_status);
        tv_cancel_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_snooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSnoozeTime(v);
            }
        });

    }
    public void startHoldOrPauseAlarmManager(String timer, String status_taskId, String currentTaskStatus, String jobCodeNo) {
        SimpleDateFormat datefor = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date enddate = null, dateFor = null, endDate = null;
        String endTime = null;
        int unicid = 0;
        Log.i("alarm123", "startHoldOrPauseAlarmManager started");
        Log.i("alarm123", "startHoldOrPauseAlarmManager timer===>" + timer);
        Log.i("alarm123", "startHoldOrPauseAlarmManager status_taskId===>" + status_taskId);
        Log.i("alarm123", "startHoldOrPauseAlarmManager currentTaskStatus===>" + currentTaskStatus);
        try {
            enddate = datefor.parse(timer);
            Log.i("task", "End.Date " + enddate);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("alarm123", "startSelfAlarmManager Exception: " + e.getMessage(), "WARN", null);
        }
        SimpleDateFormat timeforend = new SimpleDateFormat("HH:mm");
        SimpleDateFormat Datefor = new SimpleDateFormat("yyyy-MM-dd");
        try {
            endTime = timeforend.format(enddate);
            Log.i("alarm123", "End.Time========> " + endTime);
            endDate = Datefor.parse(Datefor.format(enddate));
            Log.i("alarm123", "endDate===========> " + endDate);

        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("alarm123", "timeforend Exception : " + e.getMessage(), "WARN", null);
        }
        try {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, StatusAlarmManager.class);
            unicid = Integer.parseInt(status_taskId);
            intent.putExtra("id", unicid);
            intent.putExtra("endTime", endTime);
            intent.putExtra("currentProjectId", jobCodeNo);
            intent.putExtra("currentStatus", currentTaskStatus);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, unicid, intent, 0);
            Calendar cal = Calendar.getInstance();
            cal.setTime(endDate);
            cal.set(Calendar.HOUR, Integer.parseInt(endTime.split(":")[0]));
            cal.set(Calendar.MINUTE, Integer.parseInt(endTime.split(":")[1]));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                Log.d("alarm123", "above kitkat");
                Log.d("alarm123", "above kitkat cal.getTimeInMillis()==>" + cal.getTimeInMillis());
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
                Log.d("alarm123", "below kitkat");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Appreference.printLog("NewTaskConversation", "AlarmManager Exception : " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("NewTaskConversation", "AlarmManager Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void ShowHoldOrPauseTimerDisplay() {
        ArrayList<TaskDetailsBean> getTimeBean = new ArrayList<>();
        String getTimerdetailsQuery = "select * from taskDetailsInfo where (loginuser='" + Appreference.loginuserdetails.getEmail() + "') and (taskId='" + my_taskId + "') and (taskStatus='Hold' or taskStatus='Paused')";
        Log.i("timer123", "Reminder Timer query " + getTimerdetailsQuery);
        getTimeBean = VideoCallDataBase.getDB(context).getTimerDateForHoldOrPause(getTimerdetailsQuery);
        Log.i("timer123", "getTimeBean size" + getTimeBean.size());
        if (getTimeBean.size() > 0) {
            final TaskDetailsBean MyTimerBean = getTimeBean.get(getTimeBean.size() - 1);
            Log.i("timer123", "MyTimerBean.getPlannedEndDateTime()" + MyTimerBean.getPlannedEndDateTime());

            if (MyTimerBean.getPlannedEndDateTime() != null) {
                final Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Log.d("timer123", "Current date " + c.getTime());
                String formattedDate = df.format(c.getTime());
                Log.d("timer123", "Formatted current date " + formattedDate);
                String enddate = MyTimerBean.getPlannedEndDateTime();
                Log.i("timer123", "End Date from DB " + MyTimerBean.getPlannedEndDateTime());
                try {
                    if (enddate != null && !enddate.equals(" ")) {
                        Date date2 = df.parse(enddate);
                        Date date1 = df.parse(formattedDate);
                        Log.i("timer123", "date" + date2 + " " + date1);
                        Log.i("timer123", "datetime" + date2.getTime() + " " + date1.getTime());
                        final long mills = date2.getTime() - date1.getTime();
                        final long seconds = 1000;
                      /*  if (isRem_time) {
                            counter.cancel();
                            isRem_time = false;
                        }*/
                        Log.i("timer123", "mills before " + mills);
                        Log.i("timer123", "seconds before " + seconds);
                        if (mills > 0) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    counter = null;
                                    Log.i("timer123", "StartTimer");
                                    counter = new CounterClass(mills, seconds);
                                    counter.taskOverdue(MyTimerBean);
                                    counter.start();
                                }
                            });
                        } else {
                            String query = "select status from projectStatus where projectId='" + my_projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + my_taskId + "'";
                            int timer_Alert_by_current_status = VideoCallDataBase.getDB(context).getCurrentStatus(query);
                            String StatusTask;
                            if (timer_Alert_by_current_status == 1 || timer_Alert_by_current_status == 3) {
                                String alertQuery = "select taskPlannedLatestEndDate from taskDetailsInfo where projectId='" + my_projectId + "'and taskId= '" + my_taskId + "'";
                                String isAlertShown = VideoCallDataBase.getDB(context).getAlertShownstatus(alertQuery);
                                if (isAlertShown.equalsIgnoreCase("1")) {
                                    String AlarmRingedUpdateQuery = "update taskDetailsInfo set taskPlannedLatestEndDate='0' where projectId='" + my_projectId + "'and taskId= '" + my_taskId + "'";
                                    Log.i("tone123", "updateSnoozeTime_query***********"+AlarmRingedUpdateQuery);
                                    VideoCallDataBase.getDB(context).updateaccept(AlarmRingedUpdateQuery);
                                    Log.i("tone123", "ShowHoldOrPauseTimerDisplay else===>");
                                    MainActivity.startAlarmRingTone();
                                    showTimerAlertUI();
                                }
                            }
                        }
                        Log.d("timer123", "counter started");
                    }
                } catch (Exception esx) {
                    esx.printStackTrace();
                    Appreference.printLog("NewTaskConversation", "ShowHoldOrPauseTimerDisplay() Exception : " + esx.getMessage(), "WARN", null);
                }
            }
        }
    }

    private void getSnoozeTime(View v){
  /*      final PopupMenu popup = new PopupMenu(ShowTimeupAlert.this, v);
        popup.getMenuInflater().inflate(R.menu.interval_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(ShowTimeupAlert.this,
                        "Clicked popup menu item " + item.getTitle(),
                        Toast.LENGTH_SHORT).show();
                String selecteditem[]=item.getTitle().toString().split(" ");
                int snoozeTime = 0;
                try {
                    if(selecteditem!=null) {
                        snoozeTime = Integer.parseInt(selecteditem[0]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Calendar now = Calendar.getInstance();
                now.add(Calendar.MINUTE, snoozeTime);
                // 24 hours format
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                final java.sql.Date todayDate = new java.sql.Date(System.currentTimeMillis());
                String SnoozeDateTime=todayDate+ " "+df.format(now.getTime());
                NewTaskConversation newTaskConversation=(NewTaskConversation)Appreference.context_table.get("taskcoversation");
                if(newTaskConversation!=null){
                    newTaskConversation.startHoldOrPauseAlarmManager(SnoozeDateTime, my_taskId, my_status,my_projectId);
                    newTaskConversation.ShowHoldOrPauseTimerDisplay();
                    finish();
                }else{
                    startHoldOrPauseAlarmManager(SnoozeDateTime, my_taskId, my_status,my_projectId);
                    ShowHoldOrPauseTimerDisplay();
                }

                return true;
            }
        });
        popup.show();*/
        try {
            //New Code Start
            final Dialog mDateTimeDialog = new Dialog(context);
            // Inflate the root layout
            final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater().inflate(R.layout.new_date_picker, null);

            // Grab widget instance
            final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);
            mDateTimePicker.setDateChangedListener(ShowTimeupAlert.this);

            // Update demo TextViews when the "OK" button is clicked
            ((Button) mDateTimePicker.findViewById(R.id.month_plus)).setVisibility(View.VISIBLE);
            ((Button) mDateTimePicker.findViewById(R.id.month_minus)).setVisibility(View.VISIBLE);
            ((Button) mDateTimePicker.findViewById(R.id.date_plus)).setVisibility(View.VISIBLE);
            ((Button) mDateTimePicker.findViewById(R.id.date_minus)).setVisibility(View.VISIBLE);
            ((Button) mDateTimePicker.findViewById(R.id.year_plus)).setVisibility(View.VISIBLE);
            ((Button) mDateTimePicker.findViewById(R.id.year_minus)).setVisibility(View.VISIBLE);
            ((Button) mDateTimePicker.findViewById(R.id.hour_plus)).setVisibility(View.VISIBLE);
            ((Button) mDateTimePicker.findViewById(R.id.hour_minus)).setVisibility(View.VISIBLE);
            ((Button) mDateTimePicker.findViewById(R.id.min_plus)).setVisibility(View.VISIBLE);
            ((Button) mDateTimePicker.findViewById(R.id.min_minus)).setVisibility(View.VISIBLE);
            TextView tv_snooze_header=((TextView) mDateTimeDialogView.findViewById(R.id.header_snooze));
            tv_snooze_header.setText("Snooze Time");
            ((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    mDateTimePicker.clearFocus();
                    // TODO Auto-generated method stub
                    String result_string = mDateTimePicker.getMonth() + "-" + String.valueOf(mDateTimePicker.getDay()) + "-" + String.valueOf(mDateTimePicker.getYear())
                            + "  " + String.valueOf(mDateTimePicker.getHour()) + ":" + String.valueOf(mDateTimePicker.getMinute());
                    Date date_from = null;
                    final Calendar c_date1 = Calendar.getInstance();

                    try {
                        date_from = new SimpleDateFormat("MMM-d-yyyy HH:mm").parse(result_string);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Appreference.printLog("NewTaskConversation", "showStatusPopupWindow Hold date Exception : " + e.getMessage(), "WARN", null);
                    }
                    String date3 = null, showholdTimerDate = null;
                    Date date_to = null, date_initial = null, dt_temp = null;
                    final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                    if (date3 == null) {
                        try {
                            date3 = sdf.format(c_date1.getTime());
                            date_from = new SimpleDateFormat("MMM-d-yyyy HH:mm").parse(result_string);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("NewTaskConversation", "showStatusPopupWindow Hold date Exception : " + e.getMessage(), "WARN", null);
                        }
                    }
                    try {
                        date_to = sdf.parse(date3);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("NewTaskConversation", "showStatusPopupWindow Hold date Exception : " + e.getMessage(), "WARN", null);
                    }
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String actual_timerdate = sdf.format(date_from);
                    Log.i("Date123", "actual_timerdate ===> ## " + actual_timerdate);
                    if (date_from.compareTo(date_to) > 0) {
                        try {
                            dt_temp = dateFormat.parse(actual_timerdate);
                            showholdTimerDate = originalFormat.format(dt_temp);
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Appreference.printLog("NewTaskConversation", "showStatusPopupWindow Hold date Exception : " + e.getMessage(), "WARN", null);
                        }
                        Appreference.isremarksEntered = false;
                        String updateSnoozeTime_query = "update taskDetailsInfo set plannedEndDateTime='" + showholdTimerDate + "'where projectId='" + my_projectId + "'and taskId= '" + my_taskId + "'";
                        VideoCallDataBase.getDB(context).updateaccept(updateSnoozeTime_query);

                        String AlarmRingedUpdateQuery = "update taskDetailsInfo set taskPlannedLatestEndDate='1' where projectId='" + my_projectId + "'and taskId= '" + my_taskId + "'";
                        VideoCallDataBase.getDB(context).updateaccept(AlarmRingedUpdateQuery);

                        NewTaskConversation newTaskConversation=(NewTaskConversation)Appreference.context_table.get("taskcoversation");
                        if(newTaskConversation!=null){
                            newTaskConversation.startHoldOrPauseAlarmManager(showholdTimerDate, my_taskId, my_status,my_projectId);
                            newTaskConversation.ShowHoldOrPauseTimerDisplay();
                            Toast.makeText(ShowTimeupAlert.this,"Time Snoozed....",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            startHoldOrPauseAlarmManager(showholdTimerDate, my_taskId, my_status,my_projectId);
                            ShowHoldOrPauseTimerDisplay();
                            Toast.makeText(ShowTimeupAlert.this,"Time Snoozed....",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        mDateTimeDialog.dismiss();

                    } else {
                        showToast("Please set Correct Timer...");
                    }
                }
            });
            // Cancel the dialog when the "Cancel" button is clicked
            ((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog)).setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    mDateTimeDialog.cancel();
//                                                    dialog1.dismiss();
                }
            });
            // Setup TimePicker
            // No title on the dialog window
            mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            // Set the dialog content view
            mDateTimeDialog.setContentView(mDateTimeDialogView);
            // Display the dialog
            mDateTimeDialog.show();
            //New Code End
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("NewTaskConversation", "showStatusPopupWindow Hold date Exception : " + e.getMessage(), "WARN", null);
        }
    }
    public void showToast(final String msg) {
        try {
            Toast.makeText(ShowTimeupAlert.this, msg, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("NewTaskConversation", "showToast() Exception : " + e.getMessage(), "WARN", null);
        }
    }
    private void showTimerAlertUI(){
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.showtimeup);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.horizontalMargin = 15;
            Window window = dialog.getWindow();
            window.setBackgroundDrawableResource((R.color.white));
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM);
            dialog.show();
            TextView tv_relevant_id_diag = (TextView) dialog.findViewById(R.id.relevant_id);
            TextView tv_cancel_alert_diag = (TextView) dialog.findViewById(R.id.cancel_alert);
            final TextView tv_snooze_diag = (TextView) dialog.findViewById(R.id.snooze);
            tv_relevant_id.setText("Job Card No :" + OracleprojectId + "\n" + "Activity Code : " + OracletaskId + "  is " + my_status);

            tv_snooze_diag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSnoozeTime(v);
                }
            });
            tv_cancel_alert_diag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateChanged(Calendar c) {

    }

    public class CounterClass extends CountDownTimer {
        String from_userName, date_taskId;

        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            Log.i("task", "mills after " + millisInFuture);
            Log.i("task", "seconds after " + countDownInterval);
        }

        public void taskOverdue(TaskDetailsBean taskDetailsBean) {
            from_userName = taskDetailsBean.getOwnerOfTask();
            date_taskId = taskDetailsBean.getTaskId();
        }

        @Override
        public void onFinish() {
            Log.i("timer123", "onFinish Timer======>");
            NewTaskConversation newTaskConversation=(NewTaskConversation)Appreference.context_table.get("taskcoversation");
            if(newTaskConversation!=null) {
                newTaskConversation.reminingtime.setVisibility(View.GONE);
            }
            String query = "select status from projectStatus where projectId='" + my_projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + my_taskId + "'";
            int timer_Alert_by_current_status = VideoCallDataBase.getDB(context).getCurrentStatus(query);
            String StatusTask;
            if (timer_Alert_by_current_status == 1 || timer_Alert_by_current_status == 3) {
                String alertQuery = "select taskPlannedLatestEndDate from taskDetailsInfo where projectId='" + my_projectId + "'and taskId= '" + my_taskId + "'";
                String isAlertShown = VideoCallDataBase.getDB(context).getAlertShownstatus(alertQuery);
                if (isAlertShown.equalsIgnoreCase("1")) {
                    Log.i("tone123", "onFinish ===>");
                    String AlarmRingedUpdateQuery = "update taskDetailsInfo set taskPlannedLatestEndDate='0' where projectId='" + my_projectId + "'and taskId= '" + my_taskId + "'";
                    Log.i("tone123", "updateSnoozeTime_query***********"+AlarmRingedUpdateQuery);
                    VideoCallDataBase.getDB(context).updateaccept(AlarmRingedUpdateQuery);
                    MainActivity.startAlarmRingTone();
                    showTimerAlertUI();
                }
            }
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Log.i("ontick", "ontick");
            try {
//                NewTaskConversation newTaskConversation=(NewTaskConversation)Appreference.context_table.get("taskcoversation");
                long millis = millisUntilFinished;
//                isRem_time = true;
                String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
//                if(newTaskConversation!=null) {
//                    newTaskConversation.reminingtime.setVisibility(View.VISIBLE);
//                    newTaskConversation.reminingtime.setText(hms);
//                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("NewTaskConversation", "CounterClass onTick Exception : " + e.getMessage(), "WARN", null);
            }
        }
    }

}
