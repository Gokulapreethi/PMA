package com.ase;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;
import com.ase.RandomNumber.Utility;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.WebServiceInterface;


/**
 * Created by thirumal on 09-11-2016.
 */
public class Leave_Request_dateSent extends AppCompatActivity implements WebServiceInterface {
    public TextView Start_time, End_time, Remarks;
    public ImageView Cancel;
    String[] dates;
    Intent resultIntent;
    Intent i;
    TaskDetailsBean taskDetailsBean;
    String ownerofTask;
    public static Typeface normal_type;
    private String[] mAmPmStrings = null;
    private NumberPicker datePicker, hourPicker, minutePicker, am_pmPicker;
    String today, strDATE, enDate = null;
    private Handler handler = new Handler();
    int yyyy, time1;
    private int start_date, start_time, start_minute, start_am_pm, end_date, end_time, end_minute, end_am_pm;
    boolean datecheck;
    private boolean startTimeSet = false, endTimeSet = false;
    private String toas = null;
    private String strTime, enTime, dur_check;
    public TextView Apply;
    SimpleDateFormat dateFormat;
    Context context;
    ProgressDialog progress;
    static Leave_Request_dateSent leave_request_dateSent;

    public static Leave_Request_dateSent getInstance() {
        return leave_request_dateSent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_request_date);
        context = this;
        leave_request_dateSent = this;
        Start_time = (EditText) findViewById(R.id.Start_Date);
        End_time = (EditText) findViewById(R.id.End_Date);
        Remarks = (EditText) findViewById(R.id.Remarks);
        Cancel = (ImageView) findViewById(R.id.cancel);
        Apply = (TextView) findViewById(R.id.Apply);
        i = getIntent();
        final int from_user_id = Appreference.loginuserdetails.getId();
        final String fromid = String.valueOf(from_user_id);
        final String taskid = getIntent().getStringExtra("Taskid");
        ownerofTask = getIntent().getStringExtra("ToUserName");
        final int to_user_id = VideoCallDataBase.getDB(context).getUserid(ownerofTask);
        final String toid = String.valueOf(to_user_id);
        try {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date stdate = Calendar.getInstance().getTime();
            String Stdate = dateFormat.format(stdate);
            Date etdate = getEndofDay(stdate);
            String Etdate = dateFormat.format(etdate);
            if (android.text.format.DateFormat.is24HourFormat(context)){
                Start_time.setText(Stdate);
                End_time.setText(Etdate);
            }else{
                Start_time.setText(Appreference.setDateTime(false,Stdate));
                End_time.setText(Appreference.setDateTime(false,Etdate));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showprogress("Requesting Leave");
                    String signalid = Utility.getSessionID();
                    String parentid = getFileName();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    taskDetailsBean = new TaskDetailsBean();
                    JSONObject jsonobject = new JSONObject();
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("id", taskid);
                    jsonobject.put("task", jsonObject1);
                    JSONObject jsonobject2 = new JSONObject();
                    jsonobject2.put("id", from_user_id);
                    JSONObject jsonobject3 = new JSONObject();
                    jsonobject3.put("id", to_user_id);
                    jsonobject.put("from", jsonobject2);
                    jsonobject.put("to", jsonobject3);
                    jsonobject.put("signalId", signalid);
                    jsonobject.put("parentId", parentid);
                    jsonobject.put("createdDate", dateFormat.format(new Date()));
                    jsonobject.put("requestType", "leaveRequestOrReject");
                    jsonobject.put("requestStatus", "requested");
                    Log.i("leave", "Json---->object" + jsonobject);
                    taskDetailsBean.setTaskId(taskid);
                    taskDetailsBean.setFromUserId(fromid);
                    taskDetailsBean.setOwnerOfTask(ownerofTask);
                    taskDetailsBean.setToUserId(toid);
                    taskDetailsBean.setMimeType("leaveRequest");
                    taskDetailsBean.setFromUserName(Appreference.loginuserdetails.getUsername());
                    taskDetailsBean.setToUserName(ownerofTask);
                    taskDetailsBean.setPlannedStartDateTime(Start_time.getText().toString());
                    taskDetailsBean.setPlannedEndDateTime(End_time.getText().toString());
                    taskDetailsBean.setRemark(Remarks.getText().toString());
                    taskDetailsBean.setSignalid(signalid);
                    taskDetailsBean.setParentId(parentid);
                    taskDetailsBean.setDateTime(dateFormat.format(new Date()));
                    taskDetailsBean.setTaskStatus("requested");
                    Appreference.jsonRequestSender.leaveRequestOrReject(EnumJsonWebservicename.leaveRequestOrReject, jsonobject, taskDetailsBean, Leave_Request_dateSent.this);
                } catch (Exception e) {
                }
            }
        });
        Start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDateandTIme(1);
            }
        });
        End_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDateandTIme(2);
            }
        });
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
        }
        return strFilename;
    }
    private Date getEndofDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);
        return calendar.getTime();
    }
    public void showprogress(final String toast) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("login123", "inside showProgressDialog");
                    progress = new ProgressDialog(Leave_Request_dateSent.this);
                    progress.setCancelable(false);
                    progress.setMessage(toast);
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setProgress(0);
                    progress.setMax(100);
                    progress.show();
                } catch (Exception e) {
                    e.printStackTrace();
//                        SingleInstance.printLog(null, e.getMessage(), null, e);
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
        }
    }
    private void ShowDateandTIme(final int Start_or_End) {
        {
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
                String[] sdisplayedValues = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"};
                List<String> ampmValues = new ArrayList<String>();
                String[] am_pm = {"AM", "PM", "AM", "PM"};
                minutePicker.setDisplayedValues(sdisplayedValues);
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
                if (Start_or_End == 1) {
                    Log.i("TaskDateUpdate", "start_date1 click event 1 ");
                    String[] values1 = datePicker.getDisplayedValues();
                    String st_hour = "";
                    String st_date = "";
                    if (android.text.format.DateFormat.is24HourFormat(context)) {
                        st_hour = Start_time.getText().toString();
                        st_date = Start_time.getText().toString();
                        Log.i("TaskDateUpdate", "start_date1 click event 10 " + st_date + "  " + st_hour);
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
                            }
                        } catch (Exception e) {
                        }
                        int hour = Integer.parseInt(st_hour.split(":")[0]);
                        Log.i("start", "hour " + hour);
                        Log.i("TaskDateUpdate", "start_date1 click event 12 " + hour);
                        hourPicker.setValue(hour);
                    } else {
                        st_date = Appreference.setDateTime(true, Start_time.getText().toString());
                        st_hour = Appreference.setDateTime(true, Start_time.getText().toString());
                        Log.i("TaskDateUpdate", "start_date1 click event 10 " + st_date + "  " + st_hour);
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
                            }
                        } catch (Exception e) {
                        }
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
                } else if (Start_or_End == 2) {
                    Log.i("TaskDateUpdate", "end_date1 click event 2 ");
                    String[] values1 = datePicker.getDisplayedValues();
                    String end_hour = "";
                    String en_date = "";
                    if (android.text.format.DateFormat.is24HourFormat(context)) {
                        end_hour = End_time.getText().toString();
                        en_date = End_time.getText().toString();
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
                            }
                        } catch (Exception e) {
                        }
                        Log.i("TaskDateUpdate", "end_date1 click event 22 " + endhour);
                        Log.i("end", "is24format " + endhour);
                        hourPicker.setValue(endhour);
                    } else {
                        en_date = Appreference.setDateTime(true, End_time.getText().toString());
                        end_hour = Appreference.setDateTime(true, End_time.getText().toString());
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
                            }
                        } catch (Exception e) {
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
                }
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

                if (Start_or_End == 1) {
                    String st_min = Start_time.getText().toString();
                    st_min = st_min.split(" ")[1];
                    Log.i("start", "st_min" + st_min);
                    int min = Integer.parseInt(st_min.split(":")[1]);
                    Log.i("start", "st_min" + min);
                    minutePicker.setValue(min);
                } else if (Start_or_End == 2) {
                    String en_min = End_time.getText().toString();
                    en_min = en_min.split(" ")[1];
                    Log.i("end", "en_min" + en_min);
                    int enmin = Integer.parseInt(en_min.split(":")[1]);
                    Log.i("end", "en_min" + en_min);
                    minutePicker.setValue(enmin);
                }
                set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                        Log.i("tdystr", "hour " + values2[hourPicker.getValue()]);
                        Log.i("tdystr", "min " + values3[minutePicker.getValue()]);
                        if (!android.text.format.DateFormat.is24HourFormat(context)) {
                            toas = tdystr + " " + values2[hourPicker.getValue()] + " : "
                                    + values3[minutePicker.getValue()] + " "
                                    + values4[am_pmPicker.getValue()];
                        } else {
                            toas = tdystr + " " + values2[hourPicker.getValue()] + " : "
                                    + values3[minutePicker.getValue()];
                        }

                        String day = values3[minutePicker.getValue()];// String.valueOf(tp.getCurrentMinute());
                        String strDateTime = values2[hourPicker.getValue()] + ":"
                                + values3[minutePicker.getValue()];// tp.getCurrentHour()
                        String tm = null;
                        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
                        tm = time.format(new Date());
                        Log.i("SCHEDULECALL", "Now Time Is====>" + tm);
                        if (Start_or_End == 1) {
                            int startTime = Integer.parseInt(strDateTime
                                    .split(":")[0]);
                            String startMin = strDateTime
                                    .split(":")[1];
                            Log.i("utctime", "starMin--->" + startMin);
                            if (values4 != null && am_pmPicker != null && values4[am_pmPicker.getValue()]
                                    .equalsIgnoreCase("pm")) {
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
                            String strfromdate="";
                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                strfromdate = Start_time.getText().toString();
                            } else {
                                strfromdate = Appreference.setDateTime(true, Start_time.getText().toString());
                            }
                            String strfromday = strfromdate.split(" ")[0];
                            DateFormat selctedDateformate = null;
                            Date date = null;
                            if (!android.text.format.DateFormat.is24HourFormat(context)) {
                                selctedDateformate = new SimpleDateFormat("yyyy MMM dd hh : mm a");
                                try {
                                    date = selctedDateformate.parse(yyyy + " " + toas);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                selctedDateformate = new SimpleDateFormat("yyyy MMM dd HH : mm");
                                try {
                                    date = selctedDateformate.parse(yyyy + " " + toas);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            SimpleDateFormat day1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String strenddate = day1.format(date);
                            String strendday = strenddate.split(" ")[0];
                            int strendday1 = Integer.parseInt(strendday.split("-")[2]);
                            if (strfromday.compareTo(strendday) <= 0) {
                                if (!android.text.format.DateFormat.is24HourFormat(context)) {
                                    if (CheckReminderIsValid(
                                            values4[am_pmPicker.getValue()], dateposition,
                                            strTime, tm, true)) {
                                        String startdate2 = "";
                                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                                            Start_time.setText(Appreference.setDateTime(true, strenddate));
                                        } else {
                                            Start_time.setText(Appreference.setDateTime(false, strenddate));
                                        }
                                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                                            startdate2 = Start_time.getText().toString();
                                        } else {
                                            startdate2 = Appreference.setDateTime(false, Start_time.getText().toString());
                                        }
                                        Log.i("time", "datecheck " + datecheck);
                                        Log.i("time", "time1 " + time1);
                                        Log.i("time", "dur_check " + dur_check);
                                        if (datecheck) {
                                            try {
                                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                Date ddate = null;
                                                try {
                                                    ddate = dateFormat.parse(startdate2);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
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
                                                    End_time.setText(startdate2);
                                                } else {
                                                    End_time.setText(Appreference.setDateTime(false, startdate2));
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            String fromdate2 = startdate2.split(" ")[0] + " 23:55:00";
                                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                                End_time.setText(fromdate2);
                                            } else {
                                                End_time.setText(Appreference.setDateTime(false, fromdate2));
                                            }
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
                                    if (CheckReminderIsValid(
                                            dt.split(" ")[2], dateposition,
                                            strTime, tm, true)) {
                                        String startdate2 = "";
                                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                                            Start_time.setText(strenddate);
                                        } else {
                                            Start_time.setText(Appreference.setDateTime(false, strenddate));
                                        }
                                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                                            startdate2 = Start_time.getText().toString();
                                        } else {
                                            startdate2 = Appreference.setDateTime(true, Start_time.getText().toString());
                                        }
                                        Log.i("time", "datecheck " + datecheck);
                                        Log.i("time", "time1 " + time1);
                                        Log.i("time", "dur_check " + dur_check);
                                        if (datecheck) {
                                            try {
                                                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                Date ddate = null;
                                                try {
                                                    ddate = dateFormat.parse(startdate2);
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
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
                                                    End_time.setText(startdate2);
                                                } else {
                                                    End_time.setText(Appreference.setDateTime(false, startdate2));
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        start_date = dateposition;
                                        start_time = hourPicker.getValue();
                                        start_minute = minutePicker.getValue();
                                        startTimeSet = true;
                                        strDATE = values1[datePicker.getValue()];
                                    } else {
                                        showToast(context.getString(R.string.Kindly_slct_future_time));
                                    }
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Kindly select future date", Toast.LENGTH_LONG).show();
                            }
                        } else if (Start_or_End == 2) {
                            String startrem = "";
                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                startrem = Start_time.getText().toString();
                            } else {
                                startrem = Appreference.setDateTime(true, Start_time.getText().toString());
                            }
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
                            String strfromdate = "";
                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                strfromdate = Start_time.getText().toString();
                            } else {
                                strfromdate = Appreference.setDateTime(true, Start_time.getText().toString());
                            }
                            String strfromday = strfromdate.split(" ")[0];
                            int strfromday1 = Integer.parseInt(strfromday.split("-")[2]);
                            Log.i("schedule", "strfromday-->" + strfromday1);
                            Log.i("schedule", "toas-->" + toas);
                            DateFormat selctedDateformate = null;
                            Date date = null;
                            if (!android.text.format.DateFormat.is24HourFormat(context)) {
                                selctedDateformate = new SimpleDateFormat("yyyy MMM dd hh : mm a");
                                try {
                                    date = selctedDateformate.parse(yyyy + " " + toas);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                selctedDateformate = new SimpleDateFormat("yyyy MMM dd HH : mm");
                                try {
                                    date = selctedDateformate.parse(yyyy + " " + toas);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            SimpleDateFormat day1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String strenddate = day1.format(date);

                            String strendday = strenddate.split(" ")[0];
                            int strendday1 = Integer.parseInt(strendday.split("-")[2]);
                            Log.i("schedule", "strfromday-->" + strendday1);
                            if (strfromday.compareTo(strendday) <= 0) {
                                if (!android.text.format.DateFormat.is24HourFormat(context)) {
                                    if (CheckReminderIsValid(
                                            values4[am_pmPicker.getValue()],
                                            dateposition, enTime, starttime, true)) {
                                        Log.i("schedule", "entry if");
                                        Log.i("End", "Date is " + strenddate);
                                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                                            End_time.setText(strenddate);
                                        } else {
                                            End_time.setText(Appreference.setDateTime(false, strenddate));
                                        }
                                        enDate = values1[datePicker.getValue()];
                                        end_date = dateposition;
                                        end_time = hourPicker.getValue();
                                        end_minute = minutePicker.getValue();
                                        end_am_pm = am_pmPicker.getValue();
                                        endTimeSet = true;
                                    } else {
                                        showToast(context.getString(R.string.Kindly_slct_grt_than_value_from_start_time));
                                    }
                                }else{
                                    String dt = Appreference.setDateTime(false, strenddate);
                                    if (CheckReminderIsValid(
                                            dt.split(" ")[2],
                                            dateposition, enTime, starttime, true)) {

                                        Log.i("schedule", "entry if");
                                        Log.i("End", "Date is " + strenddate);
                                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                                            End_time.setText(strenddate);
                                        } else {
                                            End_time.setText(Appreference.setDateTime(false, strenddate));
                                        }
                                        enDate = values1[datePicker.getValue()];
                                        end_date = dateposition;
                                        end_time = hourPicker.getValue();
                                        end_minute = minutePicker.getValue();
                                        end_am_pm = am_pmPicker.getValue();
                                        endTimeSet = true;
                                    } else {
                                        showToast(context.getString(R.string.Kindly_slct_grt_than_value_from_start_time));
                                    }
                                }
                            } else {
                                showToast(context.getString(R.string.Kindly_slct_grt_than_value_from_start_date));
                            }
                        }
                        dialog.dismiss();
                    }
                });
                dialog.setTitle(context.getString(R.string.Date_and_Time));
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void showToast(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Leave_Request_dateSent.this, msg, Toast.LENGTH_LONG)
                        .show();
            }
        });
    }
    private String checkAmorPmfunction(String amorpm) {
        DateFormat selctedDateformate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = selctedDateformate.parse(amorpm);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat day1 = new SimpleDateFormat("hh:mm a");
        String ammorpmm = day1.format(date);
        ammorpmm = ammorpmm.split(" ")[1];
        return ammorpmm;
    }
    private boolean CheckReminderIsValid(String am_pm, int day, String strDate, String date, boolean start) {
        boolean isvalid = false;
        try {
            Calendar rightNow = Calendar.getInstance(Locale.getDefault());
            int amPM = rightNow.get(Calendar.AM_PM);
            int position = day;
            int startTime = Integer.parseInt(strDate.split(":")[0]);
            int currentTime = Integer.parseInt(date.split(":")[0]);
            int startMin = Integer.parseInt(strDate.split(":")[1]);
            int currentMin = Integer.parseInt(date.split(":")[1]);
            Log.i("SCHEDULECALL", "Start Date===>" + startTime);
            Log.i("SCHEDULECALL", "selected Date===>" + currentTime);
            Log.i("SCHEDULECALL", "startMin ===>" + startMin);
            Log.i("SCHEDULECALL", "currentMin ===>" + currentMin);
            if (start) {
                if (position > 0 && position < 364) {
                    isvalid = true;
                } else if (position == 0 && startTime > currentTime) {
                    isvalid = true;
                } else if (position == 0 && startTime >= currentTime
                        && startMin > currentMin) {
                    isvalid = true;
                } else if (position == 0 && amPM == 0
                        && am_pm.equalsIgnoreCase("pm")) {
                } else if (am_pm.equalsIgnoreCase("pm") && am_pm.equalsIgnoreCase("pm")) {
                    if (startTime >= currentTime
                            && startMin > currentMin) {
                        isvalid = true;
                    } else {
                        isvalid = false;
                    }
                } else {
                    isvalid = false;
                }
            } else {
                if (currentTime >= startTime || currentMin > startMin) {
                    isvalid = true;
                } else {
                    isvalid = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isvalid;
    }
    private String[] getDatesFromCalender() {
        Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+1"));
        Calendar c2 = Calendar.getInstance(TimeZone.getTimeZone("GMT+1"));
        List<String> dates = new ArrayList<String>();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
            dates.add("Today");
            today = dateFormat.format(c1.getTime());
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c.getTime());
            String split[] = formattedDate.split("-");
            String todaydate = split[0] + " " + split[1];
            for (int i = 0; i < 360; i++) {
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
        }
        return dates.toArray(new String[dates.size() - 1]);
    }
    @Override
    public void ResponceMethod(final Object object) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.i("leave", " Responce Method   >>>");
                CommunicationBean bean = (CommunicationBean) object;
                try {
                    cancelDialog();
                    String str = bean.getEmail();
                    Log.i("leave", "Responce getMail() --" + str);
                    String s2 = bean.getFirstname();
                    Log.i("leave", "Responce getFirstName()  ---" + s2);
                    if (bean != null && bean.getTaskDetailsBean() != null) {
                        Log.i("leave", " bean != null && bean.getTaskDetailsBean() != null");
                        TaskDetailsBean taskDetailsBean = bean.getTaskDetailsBean();
                        Log.i("leave", "msg status in response" + taskDetailsBean.getMsg_status());

                        String test1 = str.toString();
                        String filename_2 = "";

                        JsonElement jelement = new JsonParser().parse(test1);
                        if (jelement.getAsJsonObject() != null) {
                            Log.i("leave", "jelement.getAsJsonObject() != null");
                            JsonObject jobject = jelement.getAsJsonObject();
                            String result = jobject.get("result_text").toString();
                                Log.i("leave", "jelement.getAsJsonObject() !" + result);
                            if (android.text.format.DateFormat.is24HourFormat(context)){
                                resultIntent = new Intent();
                                resultIntent.putExtra("Start_Time",Start_time.getText().toString());
                                resultIntent.putExtra("End_Time",End_time.getText().toString());
                                resultIntent.putExtra("Remarks",Remarks.getText().toString());
                                setResult(Activity.RESULT_OK, resultIntent);
                            }else{
                                resultIntent = new Intent();
                                resultIntent.putExtra("Start_Time",Appreference.setDateTime(true,Start_time.getText().toString()));
                                resultIntent.putExtra("End_Time",Appreference.setDateTime(true,End_time.getText().toString()));
                                resultIntent.putExtra("Remarks",Remarks.getText().toString());
                                setResult(Activity.RESULT_OK, resultIntent);
                            }
                        }
                        Log.i("leave", "Taskdetailbean()   >>> >>>" + bean.getTaskDetailsBean().toString());
                        Log.i("leave", " bean!=null   >>>");
                        finish();
                    }
                } catch (Exception e) {

                }
            }
        });
    }
    @Override
    public void ErrorMethod(Object object) {
    }
}