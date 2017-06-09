package com.myapplication3;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.myapplication3.Bean.TaskDetailsBean;
import com.myapplication3.DB.VideoCallDataBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.app.MyBuddy;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import java.util.Vector;

import json.EnumJsonWebservicename;
import xml.xmlcomposer;
import xml.xmlparser;

/**
 * Created by saravanakumar on 6/21/2016.
 */
public class NewTaskActivity extends Activity implements View.OnClickListener {

    Button save, cancel, photo, video, audio, doc, addTxt, calen_picker,sketh;
    EditText name, des;
    int toUserId, fromId;
    String[] dates;
    String today;
    String strDATE, enDate = null;
    private String[] mAmPmStrings = null;
    private String toas = null;
    int yyyy;
    private NumberPicker datePicker, hourPicker, minutePicker, am_pmPicker;
    private String strTime, strMin;
    private String enTime;
    private static String ampm = null;
    public static Typeface normal_type;
    private int start_date, start_time, start_minute, start_am_pm, end_date, end_time, end_minute, end_am_pm;

    private boolean startTimeSet = false, endTimeSet = false;
    Boolean blog_filter;
    ProgressDialog progress;
    private Handler handler = new Handler();
    String strIPath;
    String toUserName;
    private ListView list_all;
    public static ArrayList<TaskDetailsBean> mediaList;
    public static MediaListAdapter medialistadapter;
    final static int REQUESTCODE_RECORDING=99;
    public ArrayList<ContactBean> buddyList;
    TextView headerName;
    LinearLayout linearLayout;
    Spinner spinnerName;
    boolean check_spinner;
    VideoCallDataBase dataBase;
    String fromtime, endTime, fromdate1, todate1;
 boolean flag = false;

    Button btnDatePicker, btnTimePicker,add_btn;
    EditText fromDate, toDate,remainderDate;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Calendar myCalendar;

    CustomDateTimePicker fromDatePicker,toDatePicker,remainderDatePicker;

    private TaskDetailsBean common_taskDetailsBean = new TaskDetailsBean();
    private xmlcomposer xmlComposer = null;
    Context context;
    static NewTaskActivity newTaskActivity;

    public static NewTaskActivity getInstance() {
        return newTaskActivity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task_activity);
        Log.i("newtask","Newtask activity Oncreate");


        myCalendar = Calendar.getInstance();


        context = this;
        newTaskActivity = this;

        xmlComposer = new xmlcomposer();
        fromId = Appreference.loginuserdetails.getId();

        save = (Button) findViewById(R.id.submit);
        cancel = (Button) findViewById(R.id.cancel);
        photo = (Button) findViewById(R.id.photo);
        video = (Button) findViewById(R.id.video);
        audio = (Button) findViewById(R.id.audio);
        doc = (Button) findViewById(R.id.file);
		sketh = (Button) findViewById(R.id.write);
        addTxt = (Button) findViewById(R.id.txt_addBtn);
        calen_picker = (Button) findViewById(R.id.calender_picker);


	sketh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),HandSketchActivity2.class);
                startActivityForResult(i,423);
            }
        });
//        add_btn = (Button) findViewById(R.id.txt_addBtn) ;


        linearLayout = (LinearLayout) findViewById(R.id.ll);
        spinnerName = (Spinner) findViewById(R.id.sp_name);


        name = (EditText) findViewById(R.id.et_name);
        des = (EditText) findViewById(R.id.textvieww);

        headerName = (TextView) findViewById(R.id.txtView01);

//        btnDatePicker = (Button) findViewById(R.id.btn_date);
//        btnTimePicker = (Button) findViewById(R.id.btn_time);
        fromDate = (EditText) findViewById(R.id.txtDate);
        toDate = (EditText) findViewById(R.id.txtTime);
        remainderDate = (EditText) findViewById(R.id.reminder_date);

//        name.setSelection(2);


        list_all = (ListView) findViewById(R.id.list_all);


        if (getIntent() != null)

            if (!getIntent().getExtras().getString("toUserName").equalsIgnoreCase("")) {
                toUserId = getIntent().getExtras().getInt("toUserId");
                toUserName = getIntent().getExtras().getString("toUserName");
                linearLayout.setVisibility(View.GONE);
                check_spinner = false;
            } else {
                linearLayout.setVisibility(View.VISIBLE);
                check_spinner = true;
            }

        buddyList= VideoCallDataBase.getDB(context).getContact(Appreference.loginuserdetails.getUsername());

        ArrayList<String> list2=new ArrayList<String>();
        list2.add(0,"Please Select");
        if(buddyList!=null && buddyList.size()>0) {
            for (ContactBean a : buddyList) {
                list2.add(a.getFirstname()+" "+ a.getLastname());
            }
        }

        addTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*MediaListBean uIbean = new MediaListBean();
                uIbean.setMediaType("text");
                uIbean.setMediaPath(des.getText().toString());*/
                TaskDetailsBean uIbean = new TaskDetailsBean();
                uIbean.setMimeType("text");
                uIbean.setTaskDescription(des.getText().toString());
                Log.i("mp","mpath"+strIPath);
                mediaList.add(uIbean);
                medialistadapter.notifyDataSetChanged();

                des.setText("");
            }
        });


        ArrayAdapter<String> adp2=new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,list2);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerName.setAdapter(adp2);
//        spinnerName.setPopupBackgroundResource(R.drawable.spinner);


        if(check_spinner)
        {
            spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("position","position"+position);
                    if(position == 0)
                    {
                        headerName.setText("Task Entry");
                        name.setText("Task for "+"None");
                        toUserId = 0;
                        toUserName = "Please Select";

                    }else {
                        ContactBean contactBean = buddyList.get(position-1);

                        headerName.setText(contactBean.getFirstname() + " " + contactBean.getLastname());
                        name.setText("Task for " + contactBean.getFirstname() + " " + contactBean.getLastname());
                        toUserId = contactBean.getUserid();
                        toUserName = contactBean.getUsername();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        if(!check_spinner) {
            headerName.setText(toUserName);
            name.setText("Task for " + toUserName);
        }



         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fromDate.setText(sdf.format(new Date()));
        String fromdate1 = fromDate.getText().toString();
        fromdate1 = fromdate1.split(" ")[0] + " 23:55:00";
        toDate.setText(fromdate1);
        String todate1 = toDate.getText().toString();
        todate1 = todate1.split(" ")[0] + " 23:40:00";
        remainderDate.setText(todate1);


        mediaList = new ArrayList<TaskDetailsBean>();
        /*medialistadapter = new MediaListAdapter(NewTaskActivity.this, mediaList, "task"*//*, new MediaListAdapter.onClick() {
            @Override
            public void onClick(TaskDetailsBean gcBean, MediaListAdapter.ViewHolder v, int position, View view) {

            }

            @Override
            public void onLongClick(int position, View view) {
                
            }
        }*//*);*/
        list_all.setAdapter(medialistadapter);

//        fromDatePicker = new CustomDateTimePicker(this,
//                new CustomDateTimePicker.ICustomDateTimeListener() {
//
//                    @Override
//                    public void onSet(Dialog dialog, Calendar calendarSelected,
//                                      Date dateSelected, int year, String monthFullName,
//                                      String monthShortName, int monthNumber, int date,
//                                      String weekDayFullName, String weekDayShortName,
//                                      int hour24, int hour12, int min, int sec,
//                                      String AM_PM) {
//
//                        fromDate.setText(calendarSelected
//                                .get(Calendar.DAY_OF_MONTH)
//                                + "-" + (monthNumber + 1) + "-" + year
//                                + " " + hour24 + ":" + min + ":" + sec
//                                + " ");
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });
//        /**
//         * Pass Directly current time format it will return AM and PM if you set
//         * false
//         */
//        fromDatePicker.set24HourFormat(true);
//        /**
//         * Pass Directly current data and time to show when it pop up
//         */
//        fromDatePicker.setDate(Calendar.getInstance());


//        toDatePicker = new CustomDateTimePicker(this,
//                new CustomDateTimePicker.ICustomDateTimeListener() {
//
//                    @Override
//                    public void onSet(Dialog dialog, Calendar calendarSelected,
//                                      Date dateSelected, int year, String monthFullName,
//                                      String monthShortName, int monthNumber, int date,
//                                      String weekDayFullName, String weekDayShortName,
//                                      int hour24, int hour12, int min, int sec,
//                                      String AM_PM) {
//
//                        toDate.setText(calendarSelected
//                                .get(Calendar.DAY_OF_MONTH)
//                                + "-" + (monthNumber + 1) + "-" + year
//                                + " " + hour24 + ":" + min + ":" + sec
//                                + " ");
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });
        /**
         * Pass Directly current time format it will return AM and PM if you set
         * false
         */
//        toDatePicker.set24HourFormat(true);
        /**
         * Pass Directly current data and time to show when it pop up
         */
//        toDatePicker.setDate(Calendar.getInstance());


//        remainderDatePicker = new CustomDateTimePicker(this,
//                new CustomDateTimePicker.ICustomDateTimeListener() {
//
//                    @Override
//                    public void onSet(Dialog dialog, Calendar calendarSelected,
//                                      Date dateSelected, int year, String monthFullName,
//                                      String monthShortName, int monthNumber, int date,
//                                      String weekDayFullName, String weekDayShortName,
//                                      int hour24, int hour12, int min, int sec,
//                                      String AM_PM) {
//
//                        remainderDate.setText(calendarSelected
//                                .get(Calendar.DAY_OF_MONTH)
//                                + "-" + (monthNumber + 1) + "-" + year
//                                + " " + hour24 + ":" + min + ":" + sec
//                                + " ");
//                    }
//
//                    @Override
//                    public void onCancel() {
//
//                    }
//                });
        /**
         * Pass Directly current time format it will return AM and PM if you set
         * false
         */
//        remainderDatePicker.set24HourFormat(true);
        /**
         * Pass Directly current data and time to show when it pop up
         */
//        remainderDatePicker.setDate(Calendar.getInstance());

        fromDate.setOnClickListener(this);
        toDate.setOnClickListener(this);
        remainderDate.setOnClickListener(this);
//        fromDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                fromDatePicker.showDialog();
//
//            }
//
//        });

//        toDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                toDatePicker.showDialog();
//            }
//        });
//        remainderDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                remainderDatePicker.showDialog();
//            }
//        });


//        blog_filter = getIntent().getExtras().getBoolean("blog filter");

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multimediaImage("image");
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multimediaImage("video");
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appreference.webview_refresh = true;
                finish();
            }
        });
       doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent i = new Intent(NewTaskActivity.this, FilePicker.class);
                    startActivityForResult(i, 55);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        calen_picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(NewTaskActivity.this, TaskDateUpdate.class);
                    startActivityForResult(intent, 335);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                multimediaAudio("audio");
                try {

                    Intent i = new Intent(NewTaskActivity.this, AudioRecorder.class);
                    i.putExtra("name",name.getText().toString().trim());
                    i.putExtra("plannedStartDateTime",fromDate.getText().toString());
                    i.putExtra("plannedEndDateTime",toDate.getText().toString());
                    i.putExtra("remainderFrequency",remainderDate.getText().toString());
                    i.putExtra("des","");
                    i.putExtra("from",String.valueOf(fromId));
                    i.putExtra("to",String.valueOf(toUserId));

                    startActivityForResult(i, 333);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    JSONObject[] listpostfiles_object1 = new JSONObject[mediaList.size()];
                    if (mediaList != null && mediaList.size() > 0) {
                        int i = 0;
                        for (TaskDetailsBean bean : mediaList) {
                            listpostfiles_object1[i] = new JSONObject();
                            listpostfiles_object1[i].put("fileType", bean.getMimeType());
                            File file = new File(bean.getTaskDescription());
                            String imgcontent = "";
                            if (bean.getMimeType().equalsIgnoreCase("image")) {
                                listpostfiles_object1[i].put("taskFileExt", "jpg");
                                imgcontent = encodeTobase64(BitmapFactory.decodeFile((file.getPath())));
                            } else if (bean.getMimeType().equalsIgnoreCase("video")) {
                                listpostfiles_object1[i].put("taskFileExt", "mp4");
                                imgcontent = encodeAudioVideoToBase64(file.getPath());
                            }else if (bean.getMimeType().equalsIgnoreCase("audio")) {
                                listpostfiles_object1[i].put("taskFileExt", "mp3");
                                imgcontent = encodeAudioVideoToBase64(file.getPath());
                            }
                            JSONObject jsonObject3=new JSONObject();
                            jsonObject3.put("id", 1);

                            Log.i("AAA", "new post path " + bean.getTaskDescription());
                            listpostfiles_object1[i].put("fileContent", imgcontent);
                            listpostfiles_object1[i].put("fileSource", "task");
                            listpostfiles_object1[i].put("fileSourceId", 1);
                            listpostfiles_object1[i].put("user", jsonObject3);
                            i++;
                        }
                    }

                    JSONArray listpostfiles_object = new JSONArray();
                    for (int i = 0; i < listpostfiles_object1.length; i++) {
                        listpostfiles_object.put(listpostfiles_object1[i]);
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", name.getText().toString().trim());
                    jsonObject.put("description", des.getText().toString().trim());
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("id", fromId);
                    jsonObject.put("from", jsonObject1);
                 /*   JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put("id", toUserId);*/
                    jsonObject.put("to", toUserId);
//                    jsonObject.put("taskNo", "T00003");
                    jsonObject.put("plannedStartDateTime", fromDate.getText().toString());
                    jsonObject.put("plannedEndDateTime", toDate.getText().toString());
                    jsonObject.put("isRemainderRequired", "Y");
                    jsonObject.put("remainderDateTime", remainderDate.getText().toString());
                    jsonObject.put("status", "assigned");
                    jsonObject.put("listTaskFiles", listpostfiles_object);
                    Log.i("Task","Task--->>>"+jsonObject);


                    common_taskDetailsBean = new TaskDetailsBean();
                    common_taskDetailsBean.setTaskName(name.getText().toString().trim());
                    common_taskDetailsBean.setTaskDescription(des.getText().toString().trim());
                    common_taskDetailsBean.setFromUserId("" + fromId);
                    common_taskDetailsBean.setToUserId("" + toUserId);
//                    common_taskDetailsBean.setTaskNo("T00003");
                    common_taskDetailsBean.setPlannedStartDateTime(fromDate.getText().toString());
                    common_taskDetailsBean.setPlannedEndDateTime(toDate.getText().toString());
                    common_taskDetailsBean.setIsRemainderRequired("Y");
                    common_taskDetailsBean.setRemainderFrequency(remainderDate.getText().toString());
                    common_taskDetailsBean.setTaskStatus("assigned");

                    if (name.getText().toString().equalsIgnoreCase("Task for None") || name.getText().toString() == null) {

                        Toast.makeText(context, "Please Select Task Assign Person",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if( des.getText().toString().trim().equalsIgnoreCase("") || des.getText().toString() == null)
                    {
                        Toast.makeText(context, "Please Enter task Description",Toast.LENGTH_LONG).show();
                        return;
                    }

//                    Appreference.jsonRequestSender.taskEntry(EnumJsonWebservicename.taskEntry, jsonObject);

                    showprogress();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == fromDate.getId()) {
            showdatetime_dialog(1);
        } else if (v.getId() == toDate.getId()) {
            if (fromDate.getText().toString().length() > 0) {
                showdatetime_dialog(2);
            } else
                Toast.makeText(context, context.getString(R.string.kindly_set_Starttime),
                        Toast.LENGTH_LONG).show();
//                showToast(context.getString(R.string.kindly_set_Starttime));
        } else if (v.getId() == remainderDate.getId()) {
            showdatetime_dialog(3);
        }
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

    private String[] getDatesFromCalender() {
        Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+1"));
        Calendar c2 = Calendar.getInstance(TimeZone.getTimeZone("GMT+1"));

        List<String> dates = new ArrayList<String>();
        try {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("EE, dd MMM", Locale.ENGLISH);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
            // dates.add(dateFormat.format(c1.getTime()));
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
//            AppReferences.printLog("Calendar", "================================================","DEBUG", null);
//            AppReferences.printLog(null, e.getMessage(), null, e);
            e.printStackTrace();
        }

        return dates.toArray(new String[dates.size() - 1]);
    }

    private void showdatetime_dialog(final int startOrEnd) {
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
//			set.setOnClickListener(this);
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

            final Calendar rightNow = Calendar.getInstance(Locale.getDefault());
            int dd = rightNow.get(Calendar.HOUR_OF_DAY);
            yyyy = rightNow.get(Calendar.YEAR);
//            if (!DateFormat.is24HourFormat(this)) {
            hourPicker.setMinValue(0);
            hourPicker.setMaxValue(11);
            String[] displayedValues_hour = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
            hourPicker.setDisplayedValues(displayedValues_hour);
//            } else {
//                hourPicker.setMinValue(00);
//                hourPicker.setMaxValue(23);
//                am_pmPicker.setVisibility(View.GONE);
//            }

            if (startOrEnd == 1 && !startTimeSet) {
                hourPicker.setValue(dd);
            } else if ((startOrEnd == 1 && startTimeSet) || !endTimeSet) {
                hourPicker.setValue(start_time);
            } else if (endTimeSet) {
                hourPicker.setValue(end_time);
            }
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(11);
            minutePicker.setWrapSelectorWheel(true);
            // ArrayList<String> displayedValues = new ArrayList<String>();
            String[] sdisplayedValues = {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55"};
            List<String> ampmValues = new ArrayList<String>();
            String[] am_pm = {"AM", "PM", "AM", "PM"};
            // for (int i = 0; i < 60; i += 15) {
            // displayedValues.add(String.format("%02d", i));
            // }
            minutePicker.setDisplayedValues(sdisplayedValues);
            // ampmValues.add("AM");
            // ampmValues.add("PM");
            am_pmPicker.setMinValue(0);
            am_pmPicker.setMaxValue(3);
            am_pmPicker.setWrapSelectorWheel(true);
            int am = 0;
            am_pmPicker.setDisplayedValues(am_pm);
            if (rightNow.get(Calendar.AM_PM) == 0) {
                am = 0;
            } else {
                am = 1;
            }
            hourPicker.setOnValueChangedListener(new NumberPicker.
                    OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int
                        oldVal, int newVal) {
//                   if(newVal==12||newVal==1)
//                    int ampm = oldVal - newVal;
//                    ampm = 0;
                    if (newVal == 12 && oldVal == 11 || newVal == 11 && oldVal == 12) {

                        if (am_pmPicker.getValue() == 1) {
                            am_pmPicker.setValue(0);
                        } else {
                            am_pmPicker.setValue(1);
                        }
                    }
                }
            });

            if (startOrEnd == 1 && !startTimeSet) {
                am_pmPicker.setValue(am);
            } else if ((startOrEnd == 1 && startTimeSet) || !endTimeSet) {
                am_pmPicker.setValue(start_am_pm);
            } else if (endTimeSet) {
                am_pmPicker.setValue(end_am_pm);
            }
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

            if (startOrEnd == 1 && !startTimeSet) {
                minutePicker.setValue(nextMinute);
            } else if ((startOrEnd == 1 && startTimeSet) || !endTimeSet) {
                minutePicker.setValue(start_minute);
                datePicker.setValue(start_date);
            } else if (endTimeSet) {
                minutePicker.setValue(end_minute);
                datePicker.setValue(end_date);
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
                    }
//                    if (!DateFormat.is24HourFormat(context)) {
                    toas = tdystr + " " + values2[hourPicker.getValue()] + " : "
                            + values3[minutePicker.getValue()] + " "
                            + values4[am_pmPicker.getValue()];
//                    }else{
//                        toas = tdystr + " " + hourPicker.getValue() + " : "
//                                + values3[minutePicker.getValue()];
//                    }

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
//                    }


                    Log.i("SCHEDULECALL", "Now Time Is====>" + tm);

                    if (startOrEnd == 1) {
                        int startTime = Integer.parseInt(strDateTime
                                .split(":")[0]);
                        String startMin = strDateTime
                                .split(":")[1];

                        Log.i("utctime", "starMin--->" + startMin);
                        if (values4[am_pmPicker.getValue()]
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
//                            else if (startTime == 12) {
//                                strTime = "00" + ":"
//                                        + String.valueOf(startMin);
//                            }
                            else
                                strTime = strDateTime;

                        } else {
                            if (values4[am_pmPicker.getValue()]
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
                        String strfromdate = fromDate.getText().toString();
                        String strfromday = strfromdate.split(" ")[0];

                        DateFormat selctedDateformate = new SimpleDateFormat("yyyy MMM dd hh : mm a");
                        Date date = null;
                        try {
                            date = selctedDateformate.parse(yyyy + " " + toas);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat day1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String strenddate = day1.format(date);
//                        String strendmonth = strenddate.split("-")[1];
//                        int strendmonth1 = Integer.parseInt(strendmonth);
                        String strendday = strenddate.split(" ")[0];
                        int strendday1 = Integer.parseInt(strendday.split("-")[2]);
                        if (strfromday.compareTo(strendday) <= 0) {
                            if (CheckReminderIsValid(
                                    values4[am_pmPicker.getValue()], dateposition,
                                    strTime, tm, true)) {

                                fromDate.setText(strenddate);
                                String startdate2 = fromDate.getText().toString();
                                String fromdate2 = startdate2.split(" ")[0] + " 23:55:00";
                                toDate.setText(fromdate2);
                                String todate2 = toDate.getText().toString();
                                String todate3 = todate2.split(" ")[0] + " 23:40:00";
                                remainderDate.setText(todate3);
//                            String date_start_temp[] = strTime.split(":");
//                            start_date = Integer.parseInt(date_start_temp[0]);
                            start_date = dateposition;
                            start_time = hourPicker.getValue();
                            start_minute = minutePicker.getValue();
                            start_am_pm = am_pmPicker.getValue();
                            startTimeSet = true;

                            strDATE = values1[datePicker.getValue()];
                        } else {

//                            Toast.makeText(context,context.getString(R.string.Kindly_slct_future_time),
//                                    Toast.LENGTH_LONG).show();
                            showToast(context.getString(R.string.Kindly_slct_future_time));

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Kindly select future date", Toast.LENGTH_LONG).show();
                        }
                    } else if (startOrEnd == 2) {
//                                               strTime = String.valueOf(rightNow.get(Calendar.HOUR));
//                                               strMin = String.valueOf(rightNow.get(Calendar.MINUTE));
//                                               strTime = strTime + ":" + strMin;
//                                               Log.d("Current Minute:Second", strTime);
//                                               String starttime = strTime;
//                                               Log.i("starttime", "starttime " + starttime);

                        String startrem = fromDate.getText().toString();
                        String startampm = checkAmorPmfunction(startrem);  // am/pm
                        String startrem1 = startrem.split(":")[0];  // yyyy-mm-dd hh
                        String starttime = startrem1.split(" ")[1] + ":" + startrem.split(":")[1]; // hh:mm
                        Log.i("starttime", "starttime " + starttime);

                        int startTime = Integer.parseInt(strDateTime.split(":")[0]);
                        String startMin = strDateTime.split(":")[1];
                        if (values4[am_pmPicker.getValue()]
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

                            if (values4[am_pmPicker.getValue()]
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
                        String strfromdate = fromDate.getText().toString();
//                        String strfrommonth = strfromdate.split("-")[1];
//                        int strfrommonth1 = Integer.parseInt(strfrommonth);
                        String strfromday = strfromdate.split(" ")[0];
                        int strfromday1 = Integer.parseInt(strfromday.split("-")[2]);
//                        Log.i("schedule", "strfrommonth-->" + strfrommonth);
                        Log.i("schedule", "strfromday-->" + strfromday1);
                        Log.i("schedule", "toas-->" + toas);

                        DateFormat selctedDateformate = new SimpleDateFormat("yyyy MMM dd hh : mm a");
                        Date date = null;
                        try {
                            date = selctedDateformate.parse(yyyy + " " + toas);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat day1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String strenddate = day1.format(date);
//                        String strendmonth = strenddate.split("-")[1];
//                        int strendmonth1 = Integer.parseInt(strendmonth);
                        String strendday = strenddate.split(" ")[0];
                        int strendday1 = Integer.parseInt(strendday.split("-")[2]);
//                        Log.i("schedule", "strfrommonth-->" + strendmonth1);
                        Log.i("schedule", "strfromday-->" + strendday1);
//                        Log.i("day1","is "+day1);
//                        String strdate[] = fromDate.getText().toString().split(" ");
//                        int value = 0, value1 = 0;
//                        Log.i("schedule", "toas-->" + toas);
//                        Log.i("schedule", "strdate-->" + (strdate[0] + strdate[1]));
//                        Log.i("schedule", "toast time-->" + (toas.split(" ")[0] + toas.split(" ")[1]));
//                        int startingdate = Integer.parseInt(strdate[1]);
//                        int endingdate = Integer.parseInt(toas.split(" ")[1]);
//                        Log.i("schedule", "startingdate-->" + startingdate);
//                        Log.i("schedule", "endingdate-->" + endingdate);
//                        try {
//                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
//                            Date date = null;
//                            Date date1 = null;
//                            date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(strdate[0]);
//                            date1 = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(toas.split(" ")[0]);
//                            value = Integer.parseInt(dateFormat.format(date));
//                            value1 = Integer.parseInt(dateFormat.format(date1));
//                            Log.i("schedule", "value-->"+value);
//                            Log.i("schedule", "value1-->"+value1);
//                        } catch (Exception e) {
//                            System.out.println("failed");
//                            e.printStackTrace();
//                        }
//                        if((strdate[0].equalsIgnoreCase(toas.split(" ")[0]) && endingdate>=startingdate) || (value1>value))  {
//                        if (strfrommonth1 <= strendmonth1) {
                        if (strfromday.compareTo(strendday) <= 0) {
                            if (CheckReminderIsValid(
                                    values4[am_pmPicker.getValue()],
                                    dateposition, enTime, starttime, true)) {

                                Log.i("schedule", "entry if");
                                Log.i("End", "Date is " + strenddate);
                                toDate.setText(strenddate);
                                String endmin = values3[minutePicker.getValue()];
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
//                                selctedDateformate = new SimpleDateFormat("yyyy MMM dd hh : mm a");
//                                date = null;
//                                try {
//                                    date = selctedDateformate.parse(yyyy + " " + toas);
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                day1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                                selctedDateformate = new SimpleDateFormat("yyyy MMM dd hh : mm a");
                                date = null;
                                try {
                                    date = selctedDateformate.parse(yyyy + " " + toas);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                day1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String strremdate = day1.format(date);
                                remainderDate.setText(strremdate);

                                enDate = values1[datePicker.getValue()];

                                end_date = dateposition;
                                end_time = hourPicker.getValue();
                                end_minute = minutePicker.getValue();
                                end_am_pm = am_pmPicker.getValue();

                                endTimeSet = true;

//                            else{
//                                Toast.makeText(
//                                        context,
//                                        "Please select same date",
//                                        Toast.LENGTH_LONG).show();
//                            }

//                            ed_endtime.setText(toas);
//                            enDate = values1[datePicker.getValue()];

                            } else {

//                                Toast.makeText(
//                                        context,
//                                        context.getString(R.string.Kindly_slct_grt_than_value_from_start_time),
//                                        Toast.LENGTH_LONG).show();
                                showToast(context.getString(R.string.Kindly_slct_grt_than_value_from_start_time));

                            }
                        } else {
                            showToast(context.getString(R.string.Kindly_slct_grt_than_value_from_start_date));
                        }
//                        } else {
//                            showToast(context.getString(R.string.Kindly_slct_grt_than_value_from_start_time));
//                        }
//                        } else {
//
//                            Toast.makeText(
//                                    context,
//                                    "Kindly select greater than value from start time",
//                                    Toast.LENGTH_LONG).show();
//
//                        }

                    } else if (startOrEnd == 3) {


                        String startrem = fromDate.getText().toString();
                        String startampm = checkAmorPmfunction(startrem);  // am/pm
                        String startrem1 = startrem.split(":")[0];  // yyyy-mm-dd hh
                        fromtime = startrem1.split(" ")[1] + ":" + startrem.split(":")[1]; // hh:mm
                        Log.i("starttime", "starttime " + fromtime);

                        String endrem = toDate.getText().toString();
                        String endampm = checkAmorPmfunction(endrem);   // am/pm
                        String endrem1 = endrem.split(":")[0];  // yyyy-mm-dd hh
                        endTime = endrem1.split(" ")[1] + ":" + endrem.split(":")[1]; // hh:mm
                        Log.i("endTime", "endTime " + endTime);

                        int startTime = Integer.parseInt(strDateTime.split(":")[0]);
                        String startMin = strDateTime.split(":")[1];
                        if (values4[am_pmPicker.getValue()]
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

                            if (values4[am_pmPicker.getValue()]
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
                        String strdate = fromDate.getText().toString();
                        String strfromdate1 = strdate.split(" ")[0];
                        Log.i("schedule", "strfromdate1-->" + strfromdate1);

                        String strdate1 = toDate.getText().toString();
                        String strenddate1 = strdate1.split(" ")[0];
                        Log.i("schedule", "strenddate1-->" + strenddate1);

                        DateFormat selctedDateformate = new SimpleDateFormat("yyyy MMM dd hh : mm a");
                        Date date = null;
                        try {
                            date = selctedDateformate.parse(yyyy + " " + toas);
                        } catch (ParseException e) {
                            e.printStackTrace();
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
                                if (startampm.equalsIgnoreCase("AM") && endampm.equalsIgnoreCase("AM")) {
                                    //am
                                    if (remampm.equalsIgnoreCase("AM")) {
                                        int comp = endTime.compareTo(enTime);
                                        Log.i("comp", "comp" + comp);
                                        if ((endTime.compareTo(enTime) > 0) && (fromtime.compareTo(enTime) < 0))
                                            flag = true;
                                        else
                                            Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (startampm.equalsIgnoreCase("PM") && endampm.equalsIgnoreCase("PM")) {
                                    //pm
                                    if (remampm.equalsIgnoreCase("PM")) {
                                        int comp = endTime.compareTo(enTime);
                                        Log.i("comp", "comp" + comp);
                                        if ((endTime.compareTo(enTime) > 0) && (fromtime.compareTo(enTime) < 0))
                                            flag = true;
                                        else
                                            Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (startampm.equalsIgnoreCase("AM") && endampm.equalsIgnoreCase("PM")) {
                                    //pm
                                    if (remampm.equalsIgnoreCase("AM") || remampm.equalsIgnoreCase("PM")) {
                                        int comp = endTime.compareTo(enTime);
                                        Log.i("comp", "comp" + comp);
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
                                        Log.i("comp", "comp" + comp);
                                        if ((endTime.compareTo(enTime) > 0) && (fromtime.compareTo(enTime) > 0))
                                            flag = true;
                                        else
                                            Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            } else if (strfromdate1.compareTo(strenddate1) < 0) {
                                if (startampm.equalsIgnoreCase("AM") && endampm.equalsIgnoreCase("AM")) {
                                    //am
                                    if (remampm.equalsIgnoreCase("AM")) {
                                        int comp = endTime.compareTo(enTime);
                                        Log.i("comp", "comp" + comp);
                                        if ((endTime.compareTo(enTime) > 0) && (fromtime.compareTo(enTime) < 0))
                                            flag = true;
                                        else
                                            Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (startampm.equalsIgnoreCase("PM") && endampm.equalsIgnoreCase("PM")) {
                                    //pm
                                    if (remampm.equalsIgnoreCase("PM")) {
                                        int comp = endTime.compareTo(enTime);
                                        Log.i("comp", "comp" + comp);
                                        if ((endTime.compareTo(enTime) > 0) && (fromtime.compareTo(enTime) < 0))
                                            flag = true;
                                        else
                                            Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (startampm.equalsIgnoreCase("AM") && endampm.equalsIgnoreCase("PM")) {
                                    //pm
                                    if (remampm.equalsIgnoreCase("AM") || remampm.equalsIgnoreCase("PM")) {
                                        int comp = endTime.compareTo(enTime);
                                        Log.i("comp", "comp" + comp);
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
                                        Log.i("comp", "comp" + comp);
                                        if (strenddate1.compareTo(strremindate1) == 0) {
                                            if ((endTime.compareTo(enTime) > 0) && (fromtime.compareTo(enTime) > 0))
                                                flag = true;
                                            else
                                                Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (remampm.equalsIgnoreCase("PM")) {
                                        int comp = endTime.compareTo(enTime);
                                        int comp1 = fromtime.compareTo(enTime);
                                        Log.i("comp", "comp" + comp);
                                        if ((endTime.compareTo(enTime) > 0) && (fromtime.compareTo(enTime) > 0))
                                            flag = true;
                                        else
                                            Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
                                    }
                                }
//                                                       else {
//                                                           // am or pm
//                                                           if (remampm.equalsIgnoreCase("AM") || remampm.equalsIgnoreCase("PM")) {
//                                                               flag = true;
//                                                           }
//                                                       }
                            }
                            if (flag) {
                                remainderDate.setText(strremindate);
                                enDate = values1[datePicker.getValue()];

                                    end_date = dateposition;
                                    end_time = hourPicker.getValue();
                                    end_minute = minutePicker.getValue();
                                    end_am_pm = am_pmPicker.getValue();

                                    endTimeSet = true;

//                            else{
//                                Toast.makeText(
//                                        context,
//                                        "Please select same date",
//                                        Toast.LENGTH_LONG).show();
//                            }

//                            ed_endtime.setText(toas);
//                            enDate = values1[datePicker.getValue()];

                                }
//                                else {
//
////                                Toast.makeText(
////                                        context,
////                                        context.getString(R.string.Kindly_slct_grt_than_value_from_start_time),
////                                        Toast.LENGTH_LONG).show();
//                                    showToast(context.getString(R.string.Kindly_slct_grt_than_value_from_start_time));
//
//                                }
                        } else {
                            Toast.makeText(getApplicationContext(), "Kindly select between start and end date", Toast.LENGTH_LONG).show();
                        }
//                        } else {
//                            Toast.makeText(getApplicationContext(), "Kindly select between start and end date", Toast.LENGTH_LONG).show();
//                        }

                    }

                    dialog.dismiss();

                }
            });

//			btnSet = (Button) dialog.findViewById(R.id.btnSetDateTime);
//			dp = (DatePicker) dialog.findViewById(R.id.datePicker);
//			tp = (TimePicker) dialog.findViewById(R.id.timePicker);
//			Class<?> classForid = Class.forName("com.android.internal.R$id");
//			// Field timePickerField = classForid.getField("timePicker");
//
//			Field field = classForid.getField("minute");
//			Field timePickerField = classForid.getField("timePicker");
//			NumberPicker minutePicker = (NumberPicker) tp.findViewById(field
//					.getInt(null));
//			minutePicker.setMinValue(0);
//			minutePicker.setMaxValue(3);
//			displayedValues = new ArrayList<String>();
//			for (int i = 0; i < 60; i += 15) {
//				displayedValues.add(String.format("%02d", i));
//			}
//			// for (int i = 0; i < 60; i += 15) {
//			// displayedValues.add(String.format("%02d", i));
//			// }
//			minutePicker.setDisplayedValues(displayedValues
//					.toArray(new String[0]));
//			SimpleDateFormat sdf = new SimpleDateFormat("kk:mm");
//
//			Date date = null;
//			try {
//				String tm = sdf.format(new Date());
//
//				date = sdf.parse(tm);
//			} catch (ParseException e) {
//			}
//			Calendar c = Calendar.getInstance();
//			c.setTime(date);
//			tp.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
//
//			int minute = c.get(Calendar.MINUTE);
//			int nextMinute = 0;
//			if (minute >= 45 && minute <= 59)
//				nextMinute = 45;
//			else if (minute >= 30 && minute < 45)
//				nextMinute = 30;
//			else if (minute >= 15 && minute < 30)
//				nextMinute = 15;
//			else if (minute > 0&&minute < 15)
//				nextMinute = 0;
//			else {
//				nextMinute = 45;
//			}
//			final int min=nextMinute;
//			tp.setCurrentMinute(nextMinute);
//			// tp.setCurrentMinute(minute);
//
//			btnSet.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					int month = dp.getMonth() + 1;
//					String day = String.valueOf(tp.getCurrentMinute());
//					if (day.length() == 1) {
//						day = "0" + day;
//					}
//					String strDateTime = tp.getCurrentHour() + ":" + min;
//					SimpleDateFormat time = new SimpleDateFormat("kk:mm");
//					String tm = time.format(new Date());
//					Log.i("SCHEDULECALL", "Now Tome Is====>" + tm);
//					if (CheckReminderIsValid(tp.getCurrentHour() + ":" + day,
//							tm, true)) {
//						if (startOrEnd == 1) {
//							ed_starttime.setText(strDateTime);
//
//						} else {
//
//							String startTime = ed_starttime.getText()
//									.toString();
//
//							if (CheckReminderIsValid(strDateTime, startTime,
//									true)) {
//
//								ed_endtime.setText(strDateTime);
//
//							} else {
//
//								Toast.makeText(
//										context,
//										"Kindly select greater than value from start time",
//										Toast.LENGTH_LONG).show();
//
//							}
//
//						}
//					} else {
//
//						Toast.makeText(context, "Kindly select future time",
//								Toast.LENGTH_LONG).show();
//
//					}
//
//					dialog.dismiss();
//
//				}
//			});
            dialog.setTitle(context.getString(R.string.Date_and_Time));
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

//    private int DateTimeformat(String toas) {
//        int dateval = 0;
//        DateFormat selctedDateformate = new SimpleDateFormat("yyyy MMM dd hh : mm a");
//        Date date = null;
//        try {
//            date = selctedDateformate.parse(yyyy + " " + toas);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        SimpleDateFormat day1 = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
//        try {
//            dateval = Integer.parseInt(day1.format(date));
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//        return dateval;
//    }

    private boolean CheckReminderTimeIsValid(String am_pm, int day,
                                             String pickerDate, String fromdatee, String todatee, boolean start) {
        boolean isvalid = false;
        try {
            Calendar rightNow = Calendar.getInstance(Locale.getDefault());
            int amPM = rightNow.get(Calendar.AM_PM);

            int position = day;
            int pickerTime = Integer.parseInt(pickerDate.split(":")[0]);
            String fromTimee1 = fromdatee.split(":")[0];
            Log.i("fromTimee1", "fromTime-1           " + fromTimee1);
            int fromTimee = Integer.parseInt(fromTimee1.split(" ")[1]);
            String toTimee1 = todatee.split(":")[0];
            Log.i("toTimee1", "toTimee1-1           " + toTimee1);
            int toTimee = Integer.parseInt(toTimee1.split(" ")[1]);

            int pickerMin = Integer.parseInt(pickerDate.split(":")[1]);
            int fromMinn = Integer.parseInt(fromdatee.split(":")[1]);
            Log.i("fromMinn", "fromMinn-1           " + fromMinn);
            int toMinn = Integer.parseInt(todatee.split(":")[1]);
            Log.i("toMinn", "toMinn-1           " + toMinn);

            Log.i("SCHEDULECALL", "Start Date===>" + pickerTime);
            Log.i("SCHEDULECALL", "selected Date===>" + fromTimee);

            Log.i("SCHEDULECALL", "startMin ===>" + pickerMin);
            Log.i("SCHEDULECALL", "currentMin ===>" + fromMinn);
            if (start) {

                if (position > 0 && position < 364) {
                    isvalid = true;
                } else if (position == 0 && toTimee > pickerTime && pickerTime > fromTimee) {
                    isvalid = true;
                } else if (position == 0 && toTimee >= pickerTime && pickerTime >= fromTimee
                        && toMinn > pickerMin && pickerMin > fromMinn) {
                    isvalid = true;
                } else if (position == 0 && amPM == 0
                        && am_pm.equalsIgnoreCase("pm")) {
//                } else if (ampm.equalsIgnoreCase("pm") && am_pm.equalsIgnoreCase("pm")) {
//                    if (toTimee >= pickerTime && pickerTime >= fromTimee
//                            && toMinn > pickerMin && pickerMin > fromMinn) {
//                        isvalid = true;
//                    } else {
//                        isvalid = false;
//                    }

                    isvalid = true;

                } else {

                    isvalid = false;
                }

            } else {
                if (toTimee >= pickerTime || pickerTime >= fromTimee || toMinn > pickerMin || pickerMin > fromMinn) {
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

    private boolean CheckReminderIsValid(String am_pm, int day,
                                         String strDate, String date, boolean start) {
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
                } else if (am_pm.equalsIgnoreCase("pm")) {
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

            // try {
            // SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
            // Date date1 = dateFormat1.parse(strDate);
            //
            // Date date2 = dateFormat1.parse(date);
            // Log.i("SCHEDULECALL", "Start Date===>" + date1.toString());
            // Log.i("SCHEDULECALL", "selected Date===>" + date2.toString());
            // if (date1.after(date2) || date1.equals(date2)) {
            // isvalid = true;
            // } else {
            // isvalid = false;
            // }
            //
            // } catch (Exception e) {
            //
            // }
        } catch (Exception e) {
//            AppReferences.printLog("calendar", "================================================","DEBUG", null);
//            AppReferences.printLog(null, e.getMessage(), null, e);
            e.printStackTrace();
        }
        return isvalid;
    }

    private void multimediaImage(final String type) {
        try {
            Log.i("clone", "===> inside message response");
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_myacc_menu);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.horizontalMargin = 15;
            Window window = dialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM);
            dialog.show();
            TextView gallery = (TextView) dialog.findViewById(R.id.delete_acc);
            gallery.setText("Gallery");
            TextView camera = (TextView) dialog.findViewById(R.id.log_out);
            if(type.equalsIgnoreCase("image")) {
                camera.setText("Camera");
            }else{
                camera.setText("Video");
            }
            TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            gallery.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    try {
                        dialog.dismiss();

                        if(type.equals("image")) {
                            if (Build.VERSION.SDK_INT < 19) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent, 30);
                            } else {
                                Log.i("img", "sdk is above 19");
                                Log.i("clone", "====> inside gallery");
                                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("image/*");
                                startActivityForResult(intent, 31);
                            }
                        }else{
                            if (Build.VERSION.SDK_INT < 19) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("video/*");
                                startActivityForResult(intent, 32);
                            } else {
                                Log.i("img", "sdk is above 19");
                                Log.i("clone", "====> inside gallery");
                                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("video/*");
                                startActivityForResult(intent, 33);
                            }
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            camera.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    try {
                        dialog.dismiss();
                        if(type.equals("image")) {
                            final String path = Environment.getExternalStorageDirectory()
                                    + "/High Message/";
                            File directory = new File(path);
                            if (!directory.exists())
                                directory.mkdir();
                            strIPath = path + getFileName() + ".jpg";
                            Intent intent = new Intent(
                                    "android.media.action.IMAGE_CAPTURE");
                            Uri imageUri = Uri.fromFile(new File(strIPath));
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, 132);
                        }else{
                            final String path = Environment.getExternalStorageDirectory()
                                    + "/High Message/" ;
                            File directory = new File(path);
                            if (!directory.exists())
                                directory.mkdir();
                            strIPath = path+  getFileName() + ".mp4";
                            Intent intent = new Intent(context,CustomVideoCamera.class);
                            intent.putExtra("filePath", strIPath);
//                            Uri fileUri = Uri.fromFile(new File(strIPath));
//                            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                            startActivityForResult(intent, 111);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void multimediaAudio(final String type) {
        try {
            Log.i("clone", "===> inside message response");
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_myacc_menu);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.horizontalMargin = 15;
            Window window = dialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM);
            dialog.show();

            TextView audio = (TextView) dialog.findViewById(R.id.delete_acc);
            audio.setVisibility(View.GONE);
            TextView record_audio = (TextView) dialog.findViewById(R.id.log_out);
            if(type.equalsIgnoreCase("audio")){
//                audio.setText("audio track picker");
                record_audio.setText("record audio");
            }
            audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        dialog.dismiss();


                        if (Build.VERSION.SDK_INT < 19) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("*/*");
                            startActivityForResult(intent, 222);
                        } else {
                            Log.i("img", "sdk is above 19");
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("*/*");
                            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 223);
//                            startActivityForResult(intent, 223);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            record_audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {
                        dialog.dismiss();

                        Intent intent =
                                new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                        if (isAvailable(getApplicationContext(), intent)) {
                            startActivityForResult(intent,
                                    REQUESTCODE_RECORDING);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void notifytaskEntryResponse(final String values){
         cancelDialog();
        Appreference.webview_refresh = true;
        finish();
        try {
            JSONObject json = new JSONObject(values);
            if(values.contains("result_code")) {
                int resultCode = json.getInt("result_code");
                final String text = json.getString("result_text");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showToast(text);

                    }
                });
            }else {
                showToast("Post failed");
            }
        }catch (Exception e){
            e.printStackTrace();
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
        }
        return strFilename;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // TODO Auto-generated method stub
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK) {
                if (requestCode == 30) {
                    if (data != null) {
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        File selected_file = new File(strIPath);
                        int length = (int) selected_file.length() / 1048576;
                        if (length <= 2) {
                            Bitmap img = null;
                            img = convertpathToBitmap(strIPath);

                            if (img != null) {
                                img = Bitmap.createScaledBitmap(img, 100, 75, false);

                                final String path = Environment.getExternalStorageDirectory()
                                        + "/High Message/" + getFileName() + ".jpg";

                                BufferedOutputStream stream;
                                Bitmap bitmap = null;
                                try {
                                    bitmap = convertpathToBitmap(strIPath);
                                    if (bitmap != null) {
                                        stream = new BufferedOutputStream(
                                                new FileOutputStream(new File(path)));
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                        strIPath = path;
                                    }

                                    if (bitmap != null) {
                                        bitmap = Bitmap.createScaledBitmap(
                                                bitmap, 200, 150, false);
                                    }
                                    /*MediaListBean uIbean = new MediaListBean();
                                    uIbean.setMediaType("image");
                                    uIbean.setMediaPath(strIPath);*/
                                    TaskDetailsBean uIbean = new TaskDetailsBean();
                                    uIbean.setMimeType("image");
                                    uIbean.setTaskDescription(strIPath);
                                    mediaList.add(uIbean);
                                    medialistadapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else if (requestCode == 31) {
                    if (data != null) {
                        Uri selectedImageUri = data.getData();
                        final int takeFlags = data.getFlags()
                                & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        getContentResolver().takePersistableUriPermission(
                                selectedImageUri, takeFlags);
                        strIPath = Environment.getExternalStorageDirectory()
                                + "/High Message/"
                                + getFileName() + ".jpg";
                        File selected_file = new File(strIPath);
                        int length = (int) selected_file.length() / 1048576;
                        Log.d("busy", "........ size is------------->" + length);
                        if (length <= 2) {
                            new bitmaploader().execute(selectedImageUri);
                        } else {
                            showToast("Kindly Select someother image,this image is too large");
                        }
                    }
                }else if(requestCode==32){
                    if (data != null) {
                        Log.i("AAA","New activity 32*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + getFileName() + ".mp4";

                        strIPath = path;
                        Log.i("AAA","New activity "+strIPath);

                        TaskDetailsBean uIbean = new TaskDetailsBean();
                        uIbean.setMimeType("video");
                        uIbean.setTaskDescription(strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();
                    }
                }else if(requestCode==33){
                    if (data != null) {
                        Log.i("AAA","New activity 33*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + getFileName() + ".mp4";

                        Log.i("AAA","New activity "+strIPath);
                        strIPath = path;
                        Log.i("AAA","New activity "+strIPath);
                        TaskDetailsBean uIbean = new TaskDetailsBean();
                        uIbean.setMimeType("video");
                        uIbean.setTaskDescription(strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();
                        FileInputStream fin = (FileInputStream) getContentResolver()
                                .openInputStream(selectedImageUri);
                        ByteArrayOutputStream straam = new ByteArrayOutputStream();
                        byte[] content = new byte[1024];
                        int bytesread;
                        while ((bytesread = fin.read(content)) != -1) {
                            straam.write(content, 0, bytesread);
                        }
                        byte[] bytes = straam.toByteArray();
                        FileOutputStream fout = new FileOutputStream(strIPath);
                        straam.flush();
                        straam.close();
                        straam = null;
                        fin.close();
                        fin = null;
                        fout.write(bytes);
                        fout.flush();
                        fout.close();
                        fout = null;
                    }
                }else if (requestCode == 55) {

                    String filePath = data.getStringExtra("filePath");
                    String fileName = data.getStringExtra("fileName");
                    strIPath = data.getStringExtra("fileExt");
//                    fileExt=fileExt1.substring(fileExt1.lastIndexOf(1));
                    Log.i("fExt","fileExt"+strIPath);
                    Log.i("filePath", "fm--->" + filePath);
                    Log.i("filename", "fm--->" + fileName);
                    Log.i("fileExt", "fm--->" + strIPath);
                    strIPath = filePath;

                    TaskDetailsBean uIbean = new TaskDetailsBean();
                    uIbean.setMimeType("document");
                    uIbean.setTaskDescription(strIPath);
                    Log.i("mp","mpath"+strIPath);
                    mediaList.add(uIbean);
                    medialistadapter.notifyDataSetChanged();
                }
                else if (requestCode == 132) {
                    File new_file = new File(strIPath);
                    if (new_file.exists()) {
                        TaskDetailsBean uIbean = new TaskDetailsBean();
                        uIbean.setMimeType("image");
                        uIbean.setTaskDescription(strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();
                        Log.i("AAAA", "onactivity result $$$$$$$$$$$$$$$" + strIPath);
                    }
//                    showprogress();
//                    Log.i("AAAA","onactivity result ");
//                    new imageOrientation().execute("image");
                }
                else if (requestCode == 111) {
                    Log.i("Avideo","New activity 33************* : "+strIPath);
                    TaskDetailsBean uIbean = new TaskDetailsBean();
                    uIbean.setMimeType("video");
                    uIbean.setTaskDescription(strIPath);
                    mediaList.add(uIbean);
                    medialistadapter.notifyDataSetChanged();
//                    showprogress();
//                    Log.i("AAAA","onactivity result ");
//                    new imageOrientation().execute("image");
                }
                else if (requestCode == 99) {
                    if (data != null) {
                        Log.i("AAA","New activity 33*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + getFileName() + ".mp3";

                        Log.i("AAA","New activity "+strIPath);
                        strIPath = path;
                        Log.i("AAA","New activity "+strIPath);

                        TaskDetailsBean uIbean = new TaskDetailsBean();
                        uIbean.setMimeType("audio");
                        uIbean.setTaskDescription(strIPath);
                        Log.i("mp","mpath"+strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();

                        FileInputStream fin = (FileInputStream) getContentResolver()
                                .openInputStream(selectedImageUri);
                        ByteArrayOutputStream straam = new ByteArrayOutputStream();
                        byte[] content = new byte[1024];
                        int bytesread;
                        while ((bytesread = fin.read(content)) != -1) {
                            straam.write(content, 0, bytesread);
                        }
                        byte[] bytes = straam.toByteArray();
                        FileOutputStream fout = new FileOutputStream(strIPath);
                        straam.flush();
                        straam.close();
                        straam = null;
                        fin.close();
                        fin = null;
                        fout.write(bytes);
                        fout.flush();
                        fout.close();
                        fout = null;
                    }

                }else if (requestCode == 222) {
                    if (data != null) {
                        Log.i("AAA", "New activity 32*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + getFileName() + ".mp3";

                        strIPath = path;
                        Log.i("AAA", "New activity " + strIPath);
                        TaskDetailsBean uIbean = new TaskDetailsBean();
                        uIbean.setMimeType("audio");
                        uIbean.setTaskDescription(strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();
                    }
                }
                else if (requestCode == 223) {
                    if (data != null) {
                        Log.i("AAA","New activity 33*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + getFileName() + ".mp3";

                        Log.i("AAA","New activity "+strIPath);
                        strIPath = path;
                        Log.i("AAA","New activity "+strIPath);
                        TaskDetailsBean uIbean = new TaskDetailsBean();
                        uIbean.setMimeType("audio");
                        uIbean.setTaskDescription(strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();
                        FileInputStream fin = (FileInputStream) getContentResolver()
                                .openInputStream(selectedImageUri);
                        ByteArrayOutputStream straam = new ByteArrayOutputStream();
                        byte[] content = new byte[1024];
                        int bytesread;
                        while ((bytesread = fin.read(content)) != -1) {
                            straam.write(content, 0, bytesread);
                        }
                        byte[] bytes = straam.toByteArray();
                        FileOutputStream fout = new FileOutputStream(strIPath);
                        straam.flush();
                        straam.close();
                        straam = null;
                        fin.close();
                        fin = null;
                        fout.write(bytes);
                        fout.flush();
                        fout.close();
                        fout = null;
                    }
                }else if(requestCode == 333)
                {
                    Log.d("filePath",data.getStringExtra("taskFileExt"));
                    String path  = data.getStringExtra("taskFileExt");
                    TaskDetailsBean uIbean = new TaskDetailsBean();
                    uIbean.setMimeType("audio");
                    uIbean.setTaskDescription(strIPath);
                    mediaList.add(uIbean);
                    medialistadapter.notifyDataSetChanged();
                }else if (requestCode == 423) {
                    strIPath = data.getStringExtra("path");
                    Log.i("Task", "path" + strIPath);
                    File new_file = new File(strIPath);
                    if (new_file.exists()) {
                        TaskDetailsBean uIbean = new TaskDetailsBean();
                        uIbean.setMimeType("image");
                        uIbean.setTaskDescription(strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();
                        Log.i("AAAA", "onactivity result $$$$$$$$$$$$$$$" + strIPath);
                    }
                }
				else if (requestCode == 335) {
                    String startdate = data.getStringExtra("StartDate");
                    String enddate = data.getStringExtra("EndDate");
                    String reminderdate = data.getStringExtra("ReminderDate");
                    String reminderfreq = data.getStringExtra("ReminderFrequency");
                    Log.i("date", "startdate" + startdate);
                    Log.i("date", "enddate" + enddate);
                    Log.i("date", "reminderdate" + reminderdate);
                    Log.i("date", "reminderfreq" + reminderfreq);

                    String alldate = "Startdate : " + startdate + "\n" + "Enddate : " + enddate + "\n" + "Reminderdate : " + reminderdate + "\n" + "Reminderfreq : " + reminderfreq;
                    Log.i("alldate", "alldate" + alldate);
                    TaskDetailsBean uIbean = new TaskDetailsBean();
                    uIbean.setMimeType("text");
                    uIbean.setTaskDescription(strIPath);
                    mediaList.add(uIbean);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            medialistadapter.notifyDataSetChanged();
                        }
                    });
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block

        }

    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().getApplicationContext().CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isAvailable(Context ctx, Intent intent) {

        final PackageManager mgr = ctx.getPackageManager();

        List<ResolveInfo> list =
                mgr.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);

        return list.size() > 0;

    }

    public String getRealPathFromURI(Uri contentUri) {
        try {
            Log.i("profile", "===> inside getRealPathFromURI");
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(contentUri,
                    proj, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            Log.e("profile", "====> " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    public Bitmap convertpathToBitmap(String strIPath)
    {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile((compressImage(strIPath)),bmOptions);

        return  bitmap;

    }
    public  class bitmaploader extends AsyncTask<Uri, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            try {
                super.onPostExecute(result);
                Log.d("image", "came to post execute for image");

                Bitmap img = null;
                if (strIPath != null)
                    img = convertpathToBitmap(strIPath);
                if (img != null) {
                    TaskDetailsBean uIbean = new TaskDetailsBean();
                    uIbean.setMimeType("image");
                    uIbean.setTaskDescription(strIPath);
                    mediaList.add(uIbean);
                    medialistadapter.notifyDataSetChanged();
                   /* if (progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }*/
                    Log.d("OnActivity", "_____On Activity Called______");
                } else {
                    strIPath = null;
                }
            } catch (Exception e) {
                Log.e("profile", "====> " + e.getMessage());
               /* if (AppReference.isWriteInFile)
                    AppReference.logger.error(e.getMessage(), e);*/
              /*  else
                    e.printStackTrace();*/
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            try {
                super.onPreExecute();
                ProgressDialog dialog = new ProgressDialog(context);
//                callDisp.showprogress(dialog, context);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.e("profile", "====> " + e.getMessage());
              /*  if (AppReference.isWriteInFile)
                    AppReference.logger.error(e.getMessage(), e);
                else
                    e.printStackTrace();*/
            }
        }

        @Override
        protected Void doInBackground(Uri... params) {
            // TODO Auto-generated method stub
            try {
                for (Uri uri : params) {
                    Log.d("image", "came to doin backgroung for image");
                    FileInputStream fin = (FileInputStream) getContentResolver()
                            .openInputStream(uri);
                    ByteArrayOutputStream straam = new ByteArrayOutputStream();
                    byte[] content = new byte[1024];
                    int bytesread;
                    while ((bytesread = fin.read(content)) != -1) {
                        straam.write(content, 0, bytesread);
                    }
                    byte[] bytes = straam.toByteArray();
                    FileOutputStream fout = new FileOutputStream(strIPath);
                    straam.flush();
                    straam.close();
                    straam = null;
                    fin.close();
                    fin = null;
                    fout.write(bytes);
                    fout.flush();
                    fout.close();
                    fout = null;
                }
            } catch (Exception e) {
                /*if (AppReference.isWriteInFile)
                    AppReference.logger.error(e.getMessage(), e);
                else
                    e.printStackTrace();*/
            }

            return null;
        }

    }

    public void showToast(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NewTaskActivity.this, msg, Toast.LENGTH_LONG)
                        .show();
            }
        });
    }


    public static void clearMediaList(int position)
    {
        mediaList.remove(position);
        medialistadapter.notifyDataSetChanged();
        if(mediaList.size()==0) {
            mediaList.clear();
            medialistadapter.notifyDataSetChanged();
        }
    }



    private void showprogress() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("login123", "inside showProgressDialog");

                    progress = new ProgressDialog(NewTaskActivity.this);
                    progress.setCancelable(false);
                    progress.setMessage("Progress ...");
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
                Log.i("register", "--progress bar end-----");
                progress.dismiss();
                progress = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public void notifypostEntryResponse(final String values){
        Log.i("postEntry", "NewTaskactivity  notifypostEntryResponse method");
        cancelDialog();
        sendTaskInformationMessage(values);
//        Appreference.webview_refresh = true;
//        finish();

       /* if(Appreference.context_table.containsKey("contactsfragment")){
            ContactsFragment contactsFragment=(ContactsFragment)Appreference.context_table.get("contactsfragment");
            contactsFragment.replaceTaskFragment();
        }*/
        try {
            JSONObject json = new JSONObject(values);
            if(!values.contains("result_code")) {
               /* int resultCode = json.getInt("result_code");
                final String text = json.getString("result_text");*/
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showToast("success");
                        cancelDialog();
                    }
                });
            }else{

                cancelDialog();
            }

        //    VideoCallDataBase.getDB(context).insertORupdate_Task_history(common_taskDetailsBean);
//                showToast("Task failed");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendTaskInformationMessage(final String val) {
        handler.post(new Runnable() {
            @Override
            public void run() {


                xmlparser xmlParser = new xmlparser();
                TaskDetailsBean taskDetailsBean = xmlParser
                        .parsePositiveTaskResponse(val);
                if (xmlComposer == null) {
                    xmlComposer = new xmlcomposer();
                }

                String tasksipxml = xmlComposer.composeTaskDetailsinfo(common_taskDetailsBean);
                Log.i("Task", "composed tasksipxml : " + tasksipxml + " \n to user " + toUserName);
                String tousersipendpoint = "sip:" + toUserName + "@" + getResources().getString(R.string.server_ip);
                for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
                    String name = MainActivity.account.buddyList.get(i).cfg.getUri();

                    if (name.equalsIgnoreCase(tousersipendpoint)) {
                        MyBuddy myBuddy = MainActivity.account.buddyList.get(i);
                        SendInstantMessageParam prm = new SendInstantMessageParam();
                        prm.setContent(tasksipxml);
                        boolean valid = myBuddy.isValid();
                        Log.i("Task", "valid ======= " + valid);
                        try {
                            myBuddy.sendInstantMessage(prm);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                NewTaskActivity.this.finish();
            }
        });
    }

    private String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    private String encodeAudioVideoToBase64(String path){
        String strFile=null;
        File file=new File(path);
        try {
            FileInputStream file1=new FileInputStream(file);
            byte[] Bytearray=new byte[(int)file.length()];
            file1.read(Bytearray);
            strFile = Base64.encodeToString(Bytearray, Base64.DEFAULT);//Convert byte array into string

        } catch (IOException e) {
            e.printStackTrace();
        }
//        Log.i("FileUpload", "audioVideoEncode========" + strFile);
        return strFile;
    }



    private Bitmap reduceFileSize(File f){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_SIZE=70;

            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }


    private static String formatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");

        return sdf.format(date);
    }


    public String compressImage(String imageUri) {

        String filePath = imageUri;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {               imgRatio = maxHeight / actualHeight;                actualWidth = (int) (imgRatio * actualWidth);               actualHeight = (int) maxHeight;             } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = imageUri;
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }


    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      }       final float totalPixels = width * height;       final float totalReqPixelsCap = reqWidth * reqHeight * 2;       while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}