package com.myapplication3;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.myapplication3.Bean.TaskDetailsBean;
import com.myapplication3.DB.VideoCallDataBase;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.WebServiceInterface;

public class TaskDateUpdate extends AppCompatActivity implements View.OnClickListener, WebServiceInterface {

    public static Runnable time;
    public static Typeface normal_type;
    private static String ampm = null;
    TextAdapter textAdapter;
    int lastpos = 0;
    ArrayList<SwipeBean> beanArrayList;
    GridView gridView;
    public String strIPath, fname, CurrentTaskid;
    public LinearLayout linear1, linear2, linear3, linear4, linear5, linear6, linear7, linear8;
    Switch repeat_frequency_switch, reminder_switch;
    EditText start_date1, end_date1, reminder_date1, Reminder_quote = null, reminder_freq1, duration, duration_unit, repeat_min, temp_min, repeat_frequency, reminder_freq_local;
    ImageView submit, cancel, play_button;
    TextView txt_time, re;
    Button audio;
    Context context;
    String[] dates;
    String today, strDATE, fromtime, endTime, enDate = null, userName, temp_value, dur_check, remfreq_mins, reminder_date, temp_duration, temp_durationunit, temp_timefreq;
    int yyyy, time1;
    ArrayList<TaskDetailsBean> dateList, taskList_4;
    TaskDetailsBean bean;
    boolean datecheck = false;
    String endDate, clockType;
    private String toas = null;
    private String strTime, enTime;
    private int SelectedIdx = -1;
    WeekdaysAdaper weekdaysAdaper;
    ArrayList<TaskDetailsBean> list_Array;
    ArrayList<WeekdayBean> weekdayBeanArrayList;
    String[] colorChange = {"false", "false", "false", "false", "false", "false", "false"};
    String[] week_days = {"Every Sunday", "Every Monday", "Every Tuesday", "Every Wednesday", "Every Thursday", "Every Friday", "Every Saturday"};
    RadioGroup radioGroup;
    RadioButton Week, Month;
    TextView repeat_enddates;
    RelativeLayout radiolayout, daysofWeekslayout, repeat_ends;
    LinearLayout rem_audio_play;
    ListView list_daysofWeeks;
    String taskStatus;
    private NumberPicker datePicker, hourPicker, minutePicker, am_pmPicker;
    private String[] mAmPmStrings = null;
    private boolean startTimeSet = false, endTimeSet = false, flag = false;
    private int start_date, start_time, start_minute, start_am_pm, end_date, end_time, end_minute, end_am_pm;
    private MediaPlayer mPlayer;
    private Handler mHandler = new Handler();
    private SeekBar seekbar = null;
    private int mPlayingPosition = -1;
    private PlaybackUpdater mProgressUpdater = new PlaybackUpdater();
    boolean conflict = false, is24format = false;
    int fromId, toUserId;
    String startdate, enddate, datepattern;
    static TaskDateUpdate taskDateUpdate;
    ProgressDialog progress;
    String weekdays = "";
    String taskType;
    String isOverdueClick="";

    boolean playtimer = false;
    private boolean isPlayCompleted = true;
    public static Runnable updatetime = null;
    private Handler handlerSeek = new Handler();


    public ArrayList<ConflictCheckBean> conflictobject = new ArrayList<ConflictCheckBean>();

    public static TaskDateUpdate getInstance() {
        return taskDateUpdate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_date_update);
        String Query = null;
        context = this;
        taskDateUpdate = this;
//        task_name1 = (EditText) findViewById(R.id.taskname);
        duration = (EditText) findViewById(R.id.duration);
        duration_unit = (EditText) findViewById(R.id.duration_unit);
        temp_min = (EditText) findViewById(R.id.temp_min);
        repeat_frequency = (EditText) findViewById(R.id.repeat_frequency);

        start_date1 = (EditText) findViewById(R.id.Start_Date);
        end_date1 = (EditText) findViewById(R.id.End_Date);
        reminder_date1 = (EditText) findViewById(R.id.Reminder_Date);
        repeat_min = (EditText) findViewById(R.id.repeat_min);
        reminder_freq1 = (EditText) findViewById(R.id.Reminder_freq);
        reminder_freq_local = (EditText) findViewById(R.id.Reminder_freq_local);

        play_button = (ImageView) findViewById(R.id.play_button);
        txt_time = (TextView) findViewById(R.id.txt_time);
        Reminder_quote = (EditText) findViewById(R.id.Reminder_quote);
        seekbar = (SeekBar) findViewById(R.id.seekBar1);
        linear1 = (LinearLayout) findViewById(R.id.ll_2);
        linear2 = (LinearLayout) findViewById(R.id.ll_3);
        linear3 = (LinearLayout) findViewById(R.id.ll_4);
        linear4 = (LinearLayout) findViewById(R.id.ll_5);
        linear5 = (LinearLayout) findViewById(R.id.ll_tmd);
        linear6 = (LinearLayout) findViewById(R.id.ll_tmu);
        linear7 = (LinearLayout) findViewById(R.id.ll_tmf);
        linear8 = (LinearLayout) findViewById(R.id.ll_8);
        rem_audio_play = (LinearLayout) findViewById(R.id.rem_audio_play);
        audio = (Button) findViewById(R.id.audio);
        radiolayout = (RelativeLayout) findViewById(R.id.l1_10);
        daysofWeekslayout = (RelativeLayout) findViewById(R.id.l1_11);
        list_daysofWeeks = (ListView) findViewById(R.id.list_daysofWeeks);
        radioGroup = (RadioGroup) findViewById(R.id.repeat_ends);
        Week = (RadioButton) findViewById(R.id.dayofWeeks);
        Month = (RadioButton) findViewById(R.id.daysofMonth);
        re = (TextView) findViewById(R.id.re);
        repeat_frequency_switch = (Switch) findViewById(R.id.repeat_frequency_switch);
        reminder_switch = (Switch) findViewById(R.id.reminder_switch);
        repeat_frequency_switch.setEnabled(true);
        reminder_switch.setEnabled(true);
        reminder_switch.setChecked(true);
        repeat_min.setEnabled(true);
        repeat_frequency_switch.setChecked(true);
        repeat_min.setEnabled(true);
        reminder_freq1.setEnabled(true);
        Reminder_quote.setEnabled(true);

        datepattern = getDeviceDatePattern(getApplicationContext());
        Week.setChecked(true);
        daysofWeekslayout.setVisibility(View.VISIBLE);
        Month.setChecked(false);
        try {
            clockType = android.provider.Settings.System.getString(getApplicationContext().getContentResolver(), android.provider.Settings.System.TIME_12_24);
            is24format = android.text.format.DateFormat.is24HourFormat(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TaskDateUpdate onCreate", "Exception " + e.getMessage(), "WARN", null);
        }
        temp_value = getIntent().getStringExtra("template");

        String taskId = getIntent().getExtras().getString("taskId");
        temp_duration = getIntent().getExtras().getString("duration");
        temp_durationunit = getIntent().getExtras().getString("durationUnit");
        temp_timefreq = getIntent().getExtras().getString("timefreq");
        CurrentTaskid = taskId;
        Log.d("TaskDateUpdate", "weekday from db *" + taskId);
        toUserId = Integer.parseInt(getIntent().getStringExtra("toUserIdConflict"));
        taskType = getIntent().getStringExtra("taskType");
        Log.d("TaskDateUpdate", "CurrentTaskid * " + taskId);
        Log.d("TaskDateUpdate", "temp_value * " + temp_value);
        Log.d("TaskDateUpdate", "toUserId *" + toUserId);
        Log.d("TaskDateUpdate", "taskType *" + taskType);
        list_Array = new ArrayList<TaskDetailsBean>();
        beanArrayList = new ArrayList<SwipeBean>();
        weekdayBeanArrayList = new ArrayList<WeekdayBean>();
        taskStatus = getIntent().getExtras().getString("taskStatus");

        if (taskId != null && !taskId.equalsIgnoreCase("")) {
            String[] value;
            WeekdayBean weekbean = new WeekdayBean();
            String qry = "select * from taskDetailsInfo where taskId = '" + taskId + "' and mimeType = 'date' and daysOfTheWeek is not null order by id Desc";
            list_Array = VideoCallDataBase.getDB(this).getTaskHistory(qry);
            Log.d("DBValue", "weekday from db *** " + list_Array);
            if (list_Array.size() > 0) {
                TaskDetailsBean taskdetailsbean = list_Array.get(0);
                Log.d("DBValue", " list array **** " + taskdetailsbean.getDaysOfTheWeek());
                value = taskdetailsbean.getDaysOfTheWeek().toString().split(",");
                Log.d("DBValue", "result " + value.length);
                for (int k = 0; k < value.length; k++) {
                    value[k] = value[k];
                }
                if (value.length > 0) {

                    for (int j = 0; j < week_days.length; j++) {
                        WeekdayBean bean = new WeekdayBean();
                        bean.setWeekday(week_days[j]);
                        bean.setSelected(false);


                        for (int i = 0; i < value.length; i++) {
                            if (week_days[j].toLowerCase().contains(value[i])) {
                                bean.setSelected(true);
                            }
                        }
                        weekdayBeanArrayList.add(bean);
                    }
                }
            } else {
                for (int i = 0; i < week_days.length; i++) {
                    WeekdayBean weekdayBean = new WeekdayBean();
                    weekdayBean.setWeekday(week_days[i]);
                    weekdayBean.setSelected(false);
                    Log.i("log", " add 01" + i + "  " + weekdayBean.isSelected());
                    weekdayBeanArrayList.add(weekdayBean);
                    Log.i("log", " add 01" + weekdayBeanArrayList);
                }
            }
        } else {
            for (int i = 0; i < week_days.length; i++) {
                WeekdayBean weekdayBean = new WeekdayBean();
                weekdayBean.setWeekday(week_days[i]);
                weekdayBean.setSelected(false);
                Log.i("log", " add 01" + i + "  " + weekdayBean.isSelected());
                weekdayBeanArrayList.add(weekdayBean);
                Log.i("log", " add 01" + weekdayBeanArrayList);
            }
        }
        for (int i = 1; i < 32; i++) {
            SwipeBean bean = new SwipeBean();
            bean.setAdapterValue(i);
            bean.setSelected(false);
            Log.i("log", " add 02" + i + "  " + bean.isSelected());
            beanArrayList.add(bean);
            Log.i("log", " add 02" + beanArrayList);
        }
        gridView = (GridView) findViewById(R.id.gridView1);
        gridView.setVisibility(View.GONE);
        textAdapter = new TextAdapter(beanArrayList, this);
        gridView.setAdapter(textAdapter);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        weekdaysAdaper = new WeekdaysAdaper(weekdayBeanArrayList, this);
        list_daysofWeeks.setAdapter(weekdaysAdaper);
        reminder_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    repeat_frequency_switch.setEnabled(true);
//                    repeat_min.setClickable(true);
//                    reminder_freq1.setClickable(true);
                    reminder_date1.setEnabled(true);
                    audio.setEnabled(true);
                    Reminder_quote.setEnabled(true);
                } else {
                    repeat_frequency_switch.setEnabled(false);
                    repeat_frequency_switch.setChecked(false);
                    reminder_date1.setEnabled(false);
                    audio.setEnabled(false);
//                    repeat_min.setClickable(false);
//                    reminder_freq1.setClickable(false);
                    Reminder_quote.setEnabled(false);
                }
            }
        });
        repeat_frequency_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    repeat_min.setEnabled(true);
                    reminder_freq1.setEnabled(true);
                    reminder_date1.setEnabled(true);
//                    Reminder_quote.setEnabled(true);
                } else {
//                    reminder_date1.setEnabled(false);
                    repeat_min.setEnabled(false);
                    reminder_freq1.setEnabled(false);
//                    Reminder_quote.setEnabled(false);
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = checkedId;
                RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                if (id == Week.getId()) {
                    Log.i("id", "id name1" + checkedRadioButton.getText());
                    gridView.setVisibility(View.GONE);
                    list_daysofWeeks.setVisibility(View.VISIBLE);
                } else if (id == Month.getId()) {
                    Log.i("id", "id name2" + checkedRadioButton.getText());
                    gridView.setVisibility(View.VISIBLE);
                    list_daysofWeeks.setVisibility(View.GONE);
                }
            }
        });
        list_daysofWeeks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("pos", "1" + position);
                WeekdayBean weekbean = new WeekdayBean();
                weekbean = weekdayBeanArrayList.get(position);
                weekbean.setSelected(!weekbean.isSelected);
                weekdaysAdaper.notifyDataSetChanged();
            }
        });
        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int i = gridView.pointToPosition((int) motionEvent.getX(), (int) motionEvent.getY());
                Log.i("swipe ", "*" + i);
                if (i > -1 && i != lastpos) {
                    lastpos = i;
                    SwipeBean bean = beanArrayList.get(i);
                    bean.setSelected(!bean.isSelected());
                    textAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SwipeBean bean = new SwipeBean();
                switch (position) {
                    case 0:
                        Log.d("log", "888");
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 2:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 3:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 4:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 5:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 6:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 7:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 8:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 9:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 10:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 11:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 12:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 13:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 14:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 15:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 16:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 17:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 18:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 19:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 20:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 21:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 22:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 23:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 24:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 25:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 26:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 27:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        break;
                    case 28:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 29:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 30:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                    case 31:
                        bean = beanArrayList.get(position);
                        bean.setSelected(!bean.isSelected);
                        textAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });

        seekbar.setClickable(false);
//        play_button.setClickable(false);
        fromId = Appreference.loginuserdetails.getId();

        start_date1.setOnClickListener(this);
        end_date1.setOnClickListener(this);
        reminder_date1.setOnClickListener(this);
        mPlayer = new MediaPlayer();
        Log.i("task", "taskUpdates");
        reminder_freq1.setInputType(InputType.TYPE_NULL);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(reminder_freq1.getWindowToken(), 0);

        reminder_freq_local.setInputType(InputType.TYPE_NULL);
        InputMethodManager inputMethodManager1 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager1.hideSoftInputFromWindow(reminder_freq_local.getWindowToken(), 0);

        Log.d("conflict", "toUserId " + toUserId);
        if (temp_value.equalsIgnoreCase("success")) {
            Query = "select * from taskDetailsInfo where (mimeType = 'date' and taskId = '" + taskId + "');";
        } else {
            Query = "select * from taskDetailsInfo where (mimeType = 'dates' and taskId = '" + taskId + "');";
        }
        if (taskId != null) {
            Log.i("task", "taskId is " + taskId);
            Log.i("task", "Query" + Query);
            Log.i("task", "taskId is " + taskId + "and template value" + temp_value);

            dateList = VideoCallDataBase.getDB(context).getTaskHistory(Query);
            Log.i("task", "list Size is" + dateList.size());
            if (dateList.size() > 0) {
                try {
                    bean = dateList.get(dateList.size() - 1);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateforDate = dateFormat.format(new Date());
                    bean.setPlannedStartDateTime(dateforDate);
                    Calendar cl = Calendar.getInstance();
                    Log.i("task", "duration Unit" + bean.getDurationUnit().toLowerCase().trim());
                    if (bean.getDuration() != null && bean.getDurationUnit() != null) {

                        switch (bean.getDurationUnit().toLowerCase().trim()) {
                            case "\"hour\"":
                                Log.i("task", "end time added by every Hour");
                                cl.setTime(dateFormat.parse(dateforDate));
                                Log.i("task", "date before adding" + cl.getTime());
                                time1 = Integer.parseInt(bean.getDuration());
                                Log.i("task", "Adding hours" + time1);
                                cl.add(Calendar.HOUR, time1);
                                dur_check = "hrs";
                                Log.i("task", "After Adding hours " + time1 + " date was" + cl.getTime());
                                Log.i("task", "end time" + dateFormat.format(cl.getTime()));
//                                strIPath = bean.getTaskDescription();
                                bean.setPlannedEndDateTime(dateFormat.format(cl.getTime()));
                                if (bean.getTaskDescription().contains(".mp3")) {
                                    seekbar.setProgress(0);
//                                    play_button.setClickable(true);
                                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                                    mmr.setDataSource(bean.getTaskDescription());
                                    String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                                    mmr.release();
                                    String min, sec;
                                    min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration)));
                                    sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(duration)) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration))));
                                    if (Integer.parseInt(min) < 10) {
                                        min = 0 + String.valueOf(min);
                                    }
                                    if (Integer.parseInt(sec) < 10) {
                                        sec = 0 + String.valueOf(sec);
                                    }
                                    txt_time.setText(min + ":" + sec);
                                    strIPath = bean.getTaskDescription();

                                }
                                break;
                            case "\"day\"":
                                Log.i("task", "end time added by every Hour");
                                cl.setTime(dateFormat.parse(dateforDate));
                                Log.i("task", "date before adding" + cl.getTime());
                                time1 = Integer.parseInt(bean.getDuration());
                                Log.i("task", "Adding hours" + time1);
                                cl.add(Calendar.HOUR, (time1 * 24));
                                dur_check = "Days";
                                Log.i("task", "After Adding hours " + time1 * 24 + " date was" + cl.getTime());
                                Log.i("task", "end time" + dateFormat.format(cl.getTime()));
                                bean.setPlannedEndDateTime(dateFormat.format(cl.getTime()));
                                break;
                            default:
                                Log.i("task", "end time added by every minutes");
                                cl.setTime(dateFormat.parse(dateforDate));
                                time1 = Integer.parseInt(bean.getDuration());
                                cl.add(Calendar.MINUTE, time1);
                                dur_check = "mins";
                                Log.i("task", "end time" + dateFormat.format(cl.getTime()));
                                bean.setPlannedEndDateTime(dateFormat.format(cl.getTime()));
                                break;
                        }
                        Log.i("task", "remainder Quotes" + bean.getReminderQuote());
                        Log.i("task", "planed end time" + bean.getPlannedEndDateTime());
                        Log.i("task", "time Frequency" + bean.getTimeFrequency());
                        if (bean.getReminderQuote() != null && !bean.getReminderQuote().equalsIgnoreCase("") && !bean.getReminderQuote().equalsIgnoreCase(null) && !bean.getReminderQuote().equalsIgnoreCase("null")) {
                            Reminder_quote.setText(bean.getReminderQuote().replaceAll("\"", ""));
                        } else {
                            Reminder_quote.setText("Task Remainder");
                        }
                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                            end_date1.setText(Appreference.ChangeDevicePattern(true, bean.getPlannedEndDateTime(), datepattern));
                        } else {
                            Log.i("TaskDateUpdate", "end_date value is" + bean.getPlannedEndDateTime());
                            end_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, bean.getPlannedEndDateTime()), datepattern));
                        }
//                        end_date1.setText(Appreference.getDeviceTime24or12(context, bean.getPlannedEndDateTime()));
                        datecheck = true;
                        endDate = bean.getPlannedEndDateTime();
                        Log.i("task", "endDate first " + endDate + " " + datecheck);
                        if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
                            reminder_freq1.setText(bean.getTimeFrequency());
                        } else {
                            if (bean.getTimeFrequency() != null && bean.getTimeFrequency().contains(" ")) {
                                Log.i("TaskDateUpdate", "getTimeFrequency 3*** " + bean.getTimeFrequency());
                                repeat_min.setText(bean.getTimeFrequency().split(" ")[0]);
                                reminder_freq1.setText(bean.getTimeFrequency().split(" ")[1]);
                            } else {
                                Log.i("TaskDateUpdate", "getTimeFrequency 3*** else " + bean.getTimeFrequency());
                                repeat_min.setText("1");
                                reminder_freq1.setText("Minutes");
                            }
                        }
                    }

                    if (bean.getTaskDescription().contains(".mp3")) {
                        seekbar.setProgress(0);
//                        play_button.setClickable(true);
                        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                        mmr.setDataSource(bean.getTaskDescription());
                        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                        mmr.release();
                        String min, sec;
                        min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration)));
                        sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(duration)) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration))));
                        if (Integer.parseInt(min) < 10) {
                            min = 0 + String.valueOf(min);
                        }
                        if (Integer.parseInt(sec) < 10) {
                            sec = 0 + String.valueOf(sec);
                        }
                        txt_time.setText(min + ":" + sec);
                        strIPath = bean.getTaskDescription();

                    }

                } catch (Exception e) {
                    Log.i("exception", e.toString());
                    Appreference.printLog("TaskDateUpdate gridview.SetOnItemClickListener", "Exception " + e.getMessage(), "WARN", null);
                }
            }
        }


        if (temp_value.equalsIgnoreCase("success")) {
            linear1.setVisibility(View.GONE);
            linear2.setVisibility(View.GONE);
            linear3.setVisibility(View.GONE);
            linear4.setVisibility(View.GONE);
            linear8.setVisibility(View.GONE);
            re.setVisibility(View.GONE);
            radiolayout.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            daysofWeekslayout.setVisibility(View.GONE);
        } else {
            if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
                linear4.setVisibility(View.GONE);
                linear8.setVisibility(View.VISIBLE);
            } else {
                linear8.setVisibility(View.GONE);
                linear4.setVisibility(View.VISIBLE);
            }
            linear5.setVisibility(View.GONE);
            linear6.setVisibility(View.GONE);
            linear7.setVisibility(View.GONE);
            if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("1")) {
                Log.i("layout", "1");
                radiolayout.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
                re.setVisibility(View.VISIBLE);
                daysofWeekslayout.setVisibility(View.VISIBLE);
            } else {
                Log.i("layout", "2");
                radiolayout.setVisibility(View.GONE);
                re.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                daysofWeekslayout.setVisibility(View.GONE);
            }


        }
        cancel = (ImageView) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        reminder_freq1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(reminder_freq1.getWindowToken(), 0);
                Intent i = new Intent(TaskDateUpdate.this, ReminderFrequencySelection.class);
                i.putExtra("TimeFrequency", "1");
                startActivityForResult(i, 1);
            }
        });

        reminder_freq_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(reminder_freq_local.getWindowToken(), 0);
                Intent i = new Intent(TaskDateUpdate.this, ReminderFrequencyLocal.class);
                i.putExtra("TimeFrequency", "1");
                startActivityForResult(i, 1);
            }
        });

        repeat_frequency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
                    Intent i = new Intent(TaskDateUpdate.this, ReminderFrequencyLocal.class);
                    i.putExtra("TimeFrequency", 66);
                    startActivityForResult(i, 66);
                } else {
                    Intent i = new Intent(TaskDateUpdate.this, ReminderFrequencySelection.class);
                    i.putExtra("TimeFrequency", 66);
                    startActivityForResult(i, 66);
                }
            }
        });

        duration_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DurationUnit("duration");
            }
        });

        submit = (ImageView) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (reminder_switch.isChecked()) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    int reminder_count = 0;

                    try {
                        Date date1 = sdf.parse(sdf.format(new Date(start_date1.getText().toString().trim())));
                        Date date2 = sdf.parse(sdf.format(new Date(end_date1.getText().toString().trim())));
                        long diff = date2.getTime() - date1.getTime();
                        Log.d("TaskDateUpdate", " startdate String format == " + sdf.format(new Date(start_date1.getText().toString().trim())) + "  Date format == " + date1 + "\n"
                                + "endDate  String format == " + sdf.format(new Date(end_date1.getText().toString().trim())) + "  Date format == " + date2 + "\n"
                                + "long value is == " + diff + "  minutes value  ==  " + (int) ((diff / (1000 * 60 * 60))) + " reminder frequency == " + repeat_min.getText().toString().trim() + "\n"
                                + "calculation value is == " + (int) ((diff / (1000 * 60))) / Long.valueOf(repeat_min.getText().toString().trim()));
                        System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));

                        if (reminder_freq1.getText().toString().equalsIgnoreCase("Minutes")|| reminder_freq1.getText().toString().equalsIgnoreCase("Minutes")) {
                            reminder_count = (int) (((diff / (1000 * 60))) / Long.valueOf(repeat_min.getText().toString().trim()));
                        } else if (reminder_freq1.getText().toString().equalsIgnoreCase("hours")) {
                            reminder_count = (int) (((diff / (1000 * 60 * 60))) / Long.valueOf(repeat_min.getText().toString().trim()));
                        } else if (reminder_freq1.getText().toString().equalsIgnoreCase("days")) {
                            reminder_count = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                        }


                        if (reminder_count > 0 & reminder_count <= 1000) {

                        } else {
                            Toast.makeText(context, "Your Reminder count is " + reminder_count + "  please select between 0 to 1000 reminder", Toast.LENGTH_LONG).show();
                            return;

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("TaskDateUpdate submit.SetOnClickListener", "Exception " + e.getMessage(), "WARN", null);
                    }
                }


//                int remfreq_hr = Integer.parseInt(reminder_freq1.getText().toString());
//                remfreq_hr = remfreq_hr / 60;
//                int remfreq_mm = Integer.parseInt(reminder_freq1.getText().toString());
//                remfreq_mm = remfreq_mm % 60;
//                String remfreq_hr1 = String.valueOf(remfreq_hr);
//                if (remfreq_hr1.length() < 2) {
//                    remfreq_hr1 = "0" + remfreq_hr1;
//                }
//                String remfreq_mm1 = String.valueOf(remfreq_mm);
//                if (remfreq_mm1.length() < 2) {
//                    remfreq_mm1 = "0" + remfreq_mm1;
//                }
//                String remfreq_hrmm = remfreq_hr1 + " hrs " + remfreq_mm1 + " mins";
//                if ((repeat_min.getText().toString() != null && !repeat_min.getText().toString().equalsIgnoreCase("0") &&
//                        !repeat_min.getText().toString().equalsIgnoreCase("")) || (temp_min.getText().toString() != null && !temp_min.getText().toString().equalsIgnoreCase("0") &&
//                        !temp_min.getText().toString().equalsIgnoreCase(""))) {
                if (repeat_min.getText().toString().matches("0") || repeat_min.getText().toString().matches("") || temp_min.getText().toString().matches("0") || temp_min.getText().toString().matches("")) {
                    showToast("Unit must never be null or none");
                    return;
                } else {
                    weekdays = "";
                    for (int i = 0; i < weekdayBeanArrayList.size(); i++) {
                        Log.i("inside", " for" + i);
                        Log.i("inside", " isSel[i]" + weekdayBeanArrayList.get(i).isSelected);
                        if (weekdayBeanArrayList.get(i).isSelected) {
                            if (weekdayBeanArrayList.get(i).getWeekday().equalsIgnoreCase("Every Sunday")) {
                                weekdays = weekdays + "sun,";
                                Log.i("inside", " weekdays 1" + weekdays);
                            } else if (weekdayBeanArrayList.get(i).getWeekday().equalsIgnoreCase("Every Monday")) {
                                weekdays = weekdays + "mon,";
                                Log.i("inside", " weekdays 2" + weekdays);
                            } else if (weekdayBeanArrayList.get(i).getWeekday().equalsIgnoreCase("Every Tuesday")) {
                                weekdays = weekdays + "tue,";
                                Log.i("inside", " weekdays 3" + weekdays);
                            } else if (weekdayBeanArrayList.get(i).getWeekday().equalsIgnoreCase("Every Wednesday")) {
                                weekdays = weekdays + "wed,";
                                Log.i("inside", " weekdays 4" + weekdays);
                            } else if (weekdayBeanArrayList.get(i).getWeekday().equalsIgnoreCase("Every Thursday")) {
                                weekdays = weekdays + "thu,";
                                Log.i("inside", " weekdays 5" + weekdays);
                            } else if (weekdayBeanArrayList.get(i).getWeekday().equalsIgnoreCase("Every Friday")) {
                                weekdays = weekdays + "fri,";
                                Log.i("inside", " weekdays 6" + weekdays);
                            } else if (weekdayBeanArrayList.get(i).getWeekday().equalsIgnoreCase("Every Saturday")) {
                                weekdays = weekdays + "sat,";
                                Log.i("inside", " weekdays 7" + weekdays);
                            }
                        } else {

                        }
                    }
                    int len = weekdays.length();
                    if (weekdays.endsWith(",")) {
                        weekdays = weekdays.substring(0, len - 1);
                    }
                    if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
                        remfreq_mins = reminder_freq_local.getText().toString();
                    } else {
                        remfreq_mins = repeat_min.getText().toString() + " " + reminder_freq1.getText().toString();
                    }
                    String repeat_freq = "";

                    repeat_freq = temp_min.getText().toString() + " " + repeat_frequency.getText().toString();
                    String unit = duration_unit.getText().toString();
                    String dura = duration.getText().toString();

                    Log.i("Reminder quotes", "is " + Reminder_quote.getText().toString());
//                if (Reminder_quote.getText().toString().equals("") || Reminder_quote.getText().toString().equals(null)) {
//                    Toast.makeText(getApplicationContext(), "Please Give Quote For This Task", Toast.LENGTH_LONG).show();
//                } else {
//                if (remfreq_mins.length() < 2) {
//                    remfreq_mins = "0" + remfreq_mins;
//                }
//                remfreq_mins = remfreq_mins + " mins";
                    temp_value = getIntent().getStringExtra("template");
                    if (temp_value.equalsIgnoreCase("success")) {
                        Intent i = new Intent();
                        i.putExtra("Duration", dura);
                        i.putExtra("DurationUnit", unit);
                        i.putExtra("RepeatFrequency", repeat_freq);
                        i.putExtra("ReminderQuote", Reminder_quote.getText().toString());
                        i.putExtra("ReminderTone", strIPath);
                        setResult(RESULT_OK, i);
                        finish();
                    } else {
                        Log.i("task", "Appreference.conflicttask " + Appreference.conflicttask);
                        Log.i("task", "Appreference.conflicttask " + taskType);

                        if (Appreference.conflicttask && taskType.equalsIgnoreCase("Individual")) {
                            Appreference.isAssignLeave = false;
                            confictWebservice();
                            Log.i("Taskdateupdate", "Appreference.conflicttask if " + Appreference.conflicttask);
                            showprogress();
                        } else {
                            Log.i("Taskdateupdate", "Appreference.conflicttask else " + Appreference.conflicttask);
                            NewTaskConversation.conflict = false;
                            if (!android.text.format.DateFormat.is24HourFormat(context)) {
                                Log.i("Taskdateupdate", "Appreference.conflicttask else " + Appreference.conflicttask);
                                NewTaskConversation.conflict = false;
//                                if (!getResources().getString(R.string.proxyua).equalsIgnoreCase("enable")) {
//                                    Intent i = new Intent();
//                                    i.putExtra("StartDate",Appreference.setDateTime(true,Appreference.ChangeOriginalPattern(false,start_date1.getText().toString(),datepattern)));
//                                    i.putExtra("EndDate",Appreference.setDateTime(true,Appreference.ChangeOriginalPattern(false,end_date1.getText().toString(),datepattern)));
//                                    i.putExtra("ReminderDate", Appreference.setDateTime(true,Appreference.ChangeOriginalPattern(false,reminder_date1.getText().toString(),datepattern)));
//                                    i.putExtra("ReminderFrequency", remfreq_mins);
//                                    i.putExtra("ReminderQuote", Reminder_quote.getText().toString());
//                                    i.putExtra("ReminderTone", strIPath);
//                                    if (weekdays != null && !weekdays.equalsIgnoreCase("")) {
//                                        i.putExtra("Weekdays", weekdays);
//                                    }
//                                    i.putExtra("changedateheader", "assigned");
//                                    setResult(RESULT_OK, i);
//                                    finish();
//                                } else {
                                Intent i = new Intent();
                                i.putExtra("StartDate", Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern)));
                                i.putExtra("EndDate", Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, end_date1.getText().toString(), datepattern)));
//                                i.putExtra("ReminderDate", Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, reminder_date1.getText().toString(), datepattern)));
//                                i.putExtra("ReminderFrequency", remfreq_mins);
//                                i.putExtra("ReminderQuote", Reminder_quote.getText().toString());
                                i.putExtra("ReminderTone", strIPath);
                                if (reminder_switch.isChecked()) {
                                    Log.i("Taskdateupdate", "reminder_switch.isChecked() @@@ " + reminder_switch.isChecked());
                                    i.putExtra("ReminderDate", Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, reminder_date1.getText().toString(), datepattern)));
                                    i.putExtra("ReminderQuote", Reminder_quote.getText().toString());
                                } else {
                                    Log.i("Taskdateupdate", "reminder_switch.isChecked() @@@ " + reminder_switch.isChecked());
                                    i.putExtra("ReminderDate", "");
                                    i.putExtra("ReminderQuote", "");
                                }
                                if (repeat_frequency_switch.isChecked()) {
                                    Log.i("Taskdateupdate", "repeat_frequency_switch.isChecked() @@@ " + repeat_frequency_switch.isChecked());
                                    i.putExtra("ReminderFrequency", remfreq_mins);
                                } else {
                                    Log.i("Taskdateupdate", "repeat_frequency_switch.isChecked() @@@ " + repeat_frequency_switch.isChecked());
                                    i.putExtra("ReminderFrequency", "");
                                }
                                if (weekdays != null && !weekdays.equalsIgnoreCase("")) {
                                    i.putExtra("Weekdays", weekdays);
                                }
                                i.putExtra("changedateheader", "assigned");
                                setResult(RESULT_OK, i);
                                finish();
//                                }

                            } else {
//                                if (!getResources().getString(R.string.proxyua).equalsIgnoreCase("enable")) {
//                                    Intent i = new Intent();
//                                    i.putExtra("StartDate", Appreference.ChangeOriginalPattern(true,start_date1.getText().toString(),datepattern));
//                                    i.putExtra("EndDate", Appreference.ChangeOriginalPattern(true,end_date1.getText().toString(),datepattern));
//                                    i.putExtra("ReminderDate", Appreference.ChangeOriginalPattern(true,reminder_date1.getText().toString(),datepattern));
//                                    i.putExtra("ReminderFrequency", remfreq_mins);
//                                    i.putExtra("ReminderQuote", Reminder_quote.getText().toString());
//                                    i.putExtra("ReminderTone", strIPath);
//                                    if (weekdays != null && !weekdays.equalsIgnoreCase("")) {
//                                        i.putExtra("Weekdays", weekdays);
//                                    }
//                                    i.putExtra("changedateheader", "assigned");
//                                    setResult(RESULT_OK, i);
//                                    finish();
//                                } else {
                                Intent i = new Intent();
                                i.putExtra("StartDate", Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern));
                                i.putExtra("EndDate", Appreference.ChangeOriginalPattern(true, end_date1.getText().toString(), datepattern));
//                                i.putExtra("ReminderDate", Appreference.ChangeOriginalPattern(true, reminder_date1.getText().toString(), datepattern));
//                                i.putExtra("ReminderFrequency", remfreq_mins);
//                                i.putExtra("ReminderQuote", Reminder_quote.getText().toString());
                                i.putExtra("ReminderTone", strIPath);
                                if (reminder_switch.isChecked()) {
                                    Log.i("Taskdateupdate", "reminder_switch.isChecked() @@@ " + reminder_switch.isChecked());
                                    i.putExtra("ReminderDate", Appreference.ChangeOriginalPattern(true, reminder_date1.getText().toString(), datepattern));
                                    i.putExtra("ReminderQuote", Reminder_quote.getText().toString());
                                } else {
                                    Log.i("Taskdateupdate", "reminder_switch.isChecked() @@@ " + reminder_switch.isChecked());
                                    i.putExtra("ReminderDate", "");
                                    i.putExtra("ReminderQuote", "");
                                }
                                if (repeat_frequency_switch.isChecked()) {
                                    Log.i("Taskdateupdate", "repeat_frequency_switch.isChecked() @@@ " + repeat_frequency_switch.isChecked());
                                    i.putExtra("ReminderFrequency", remfreq_mins);
                                } else {
                                    Log.i("Taskdateupdate", "repeat_frequency_switch.isChecked() @@@ " + repeat_frequency_switch.isChecked());
                                    i.putExtra("ReminderFrequency", "");
                                }
                                if (weekdays != null && !weekdays.equalsIgnoreCase("")) {
                                    i.putExtra("Weekdays", weekdays);
                                }
                                i.putExtra("changedateheader", "assigned");
                                setResult(RESULT_OK, i);
                                finish();
                            }

//                            }
                        }
                    }
                }
//                } else {
//                    showToast("Unit must never be null or none");
//                }
            }
        });

        if (temp_value.equalsIgnoreCase("failure")) {
            Log.i("Taskdateupdate", "failure if ");
            String queryy = "select * from taskDetailsInfo where (fromUserName='" + Appreference.loginuserdetails.getUsername() + "' or toUserName='" + Appreference.loginuserdetails.getUsername() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "'and taskId='" + taskId + "' and mimeType='date' and (requestStatus='approved' or requestStatus='requested' or requestStatus='rejected' or requestStatus='assigned') order by id desc";
            taskList_4 = VideoCallDataBase.getDB(context).getTaskHistory(queryy);
            Log.i("task", "taskList_4 size " + taskList_4.size());
            if (taskList_4.size() > 0) {
                Log.i("task", "taskList_4 inside if  " + taskList_4.size());
                TaskDetailsBean taskDetailsBean = taskList_4.get(0);
                if (taskDetailsBean.getRequestStatus().equalsIgnoreCase("rejected")) {
                    queryy = "select * from taskDetailsInfo where (fromUserName='" + Appreference.loginuserdetails.getUsername() + "' or toUserName='" + Appreference.loginuserdetails.getUsername() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "'and taskId='" + taskId + "' and mimeType='date' and (requestStatus='approved' or requestStatus='assigned') order by id desc";
                    taskList_4 = VideoCallDataBase.getDB(context).getTaskHistory(queryy);
                    if (taskList_4.size() > 0) {
                        TaskDetailsBean taskDetailsBean1 = taskList_4.get(0);
                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                            start_date1.setText(Appreference.ChangeDevicePattern(true, taskDetailsBean1.getPlannedStartDateTime(), datepattern));
                            end_date1.setText(Appreference.ChangeDevicePattern(true, taskDetailsBean1.getPlannedEndDateTime(), datepattern));
                        } else {
                            start_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, taskDetailsBean1.getPlannedStartDateTime()), datepattern));
//                            start_date1.setText(Appreference.setTimeinDevicePattern(false,taskDetailsBean1.getPlannedStartDateTime(),datepattern));
//                            end_date1.setText(Appreference.setTimeinDevicePattern(false,taskDetailsBean1.getPlannedEndDateTime(),datepattern));
                            Log.i("TaskDateUpdate", "end_date1 value is" + taskDetailsBean1.getPlannedEndDateTime());
                            end_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, taskDetailsBean1.getPlannedEndDateTime()), datepattern));
                        }
//                        start_date1.setText(Appreference.getDeviceTime24or12(context, taskDetailsBean1.getPlannedStartDateTime()));
//                        end_date1.setText(Appreference.getDeviceTime24or12(context, taskDetailsBean1.getPlannedEndDateTime()));
                        if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
                            reminder_freq1.setText(taskDetailsBean1.getTimeFrequency());
                        } else {
                            if (taskDetailsBean1.getTimeFrequency() != null && taskDetailsBean1.getTimeFrequency().contains(" ")) {
                                Log.i("TaskDateUpdate", "getTimeFrequency 1*** " + taskDetailsBean1.getTimeFrequency());
                                repeat_min.setText(taskDetailsBean1.getTimeFrequency().split(" ")[0]);
                                reminder_freq1.setText(taskDetailsBean1.getTimeFrequency().split(" ")[1]);
                            } else {
                                Log.i("TaskDateUpdate", "getTimeFrequency 1*** else " + taskDetailsBean1.getTimeFrequency());
                                repeat_min.setText("1");
                                reminder_freq1.setText("Minutes");
                            }
                        }
                        if (taskDetailsBean1.getReminderQuote() != null && !taskDetailsBean1.getReminderQuote().equalsIgnoreCase("") && !taskDetailsBean1.getReminderQuote().equalsIgnoreCase(null) && !taskDetailsBean1.getReminderQuote().equalsIgnoreCase("null")) {
                            Reminder_quote.setText(taskDetailsBean1.getReminderQuote());
                        } else {
                            Reminder_quote.setText("Task Remainder");
                        }
                        if (taskDetailsBean.getServerFileName() != null && !taskDetailsBean.getServerFileName().equalsIgnoreCase(null) && !taskDetailsBean.getServerFileName().equalsIgnoreCase("")) {
                            strIPath = "/storage/emulated/0/High Message/" + taskDetailsBean1.getServerFileName();
                        }
                        Log.i("strIPath 1", "strIPath rejected Intent " + strIPath);
                    }
                } else {
                    if (android.text.format.DateFormat.is24HourFormat(context)) {
                        start_date1.setText(Appreference.ChangeDevicePattern(true, taskDetailsBean.getPlannedStartDateTime(), datepattern));
                        end_date1.setText(Appreference.ChangeDevicePattern(true, taskDetailsBean.getPlannedEndDateTime(), datepattern));
                    } else {
                        start_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, taskDetailsBean.getPlannedStartDateTime()), datepattern));
                        Log.i("TaskDateUpdate", "end_date2 value is" + taskDetailsBean.getPlannedEndDateTime());
                        end_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, taskDetailsBean.getPlannedEndDateTime()), datepattern));
                    }
//                    start_date1.setText(Appreference.getDeviceTime24or12(context, taskDetailsBean.getPlannedStartDateTime()));
//                    end_date1.setText(Appreference.getDeviceTime24or12(context, taskDetailsBean.getPlannedEndDateTime()));
                    if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
                        reminder_freq_local.setText(taskDetailsBean.getTimeFrequency());
                    } else {
                        if (taskDetailsBean.getTimeFrequency() != null && !taskDetailsBean.getTimeFrequency().equalsIgnoreCase("")
                                && taskDetailsBean.getTimeFrequency().contains(" ")) {
                            Log.i("TaskDateUpdate", "getTimeFrequency 2*** " + taskDetailsBean.getTimeFrequency());
                            repeat_min.setText(taskDetailsBean.getTimeFrequency().split(" ")[0]);
                            reminder_freq1.setText(taskDetailsBean.getTimeFrequency().split(" ")[1]);
                        } else {
                            Log.i("TaskDateUpdate", "getTimeFrequency 2*** else " + taskDetailsBean.getTimeFrequency());
                            repeat_min.setText("1");
                            reminder_freq1.setText("Minutes");
                        }
                    }
                    if (taskDetailsBean.getReminderQuote() != null && !taskDetailsBean.getReminderQuote().equalsIgnoreCase("") && !taskDetailsBean.getReminderQuote().equalsIgnoreCase(null) && !taskDetailsBean.getReminderQuote().equalsIgnoreCase("null")) {
                        Reminder_quote.setText(taskDetailsBean.getReminderQuote());
                    } else {
                        Reminder_quote.setText("Task Remainder");
                    }
//                    Reminder_quote.setText(taskDetailsBean.getReminderQuote());
                    if (taskDetailsBean.getServerFileName() != null && !taskDetailsBean.getServerFileName().equalsIgnoreCase(null) && !taskDetailsBean.getServerFileName().equalsIgnoreCase("")) {
                        strIPath = "/storage/emulated/0/High Message/" + taskDetailsBean.getServerFileName();
                    }
                    Log.i("strIPath 1", "strIPath Intent " + strIPath);
                }
            } else {
                Log.i("task", "taskList_4 inside else " + taskList_4.size());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                if (android.text.format.DateFormat.is24HourFormat(context)) {
                    start_date1.setText(Appreference.ChangeDevicePattern(true, sdf.format(date), datepattern));
                } else {
                    start_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, sdf.format(date)), datepattern));
//                    start_date1.setText("is hidden");
//                    start_date1.setText(Appreference.setDateTimepattern(false, String.valueOf(date)));
                }
//                start_date1.setText(Appreference.getDeviceTime24or12(context, sdf.format(new Date())));
                String fromdate1 = " ";
                if (android.text.format.DateFormat.is24HourFormat(context)) {
                    fromdate1 = Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern);
                } else {
                    String date_ptn = Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern);
                    fromdate1 = Appreference.setDateTime(true, date_ptn);
                    fromdate1 = Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern));
                }
                Log.i("Taskdateupdate", "fromdate1 value is" + fromdate1);
//                fromdate1 = Appreference.getEndOfDay(fromdate1);
                Log.i("Taskdateupdate", "fromdate1 value is" + fromdate1);
                fromdate1 = fromdate1.split(" ")[0] + " 23:55:00";
                Log.i("Dateand time", "fromdate1 value is " + fromdate1);
                if (android.text.format.DateFormat.is24HourFormat(context)) {
                    end_date1.setText(Appreference.ChangeDevicePattern(true, fromdate1, datepattern));
                } else {
                    end_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, fromdate1), datepattern));
//                    end_date1.setText(Appreference.setDateTimepattern(false,fromdate1));
//                    end_date1.setText("is hidden");
                    Log.i("TaskDateUpdate", "fromdate1 value is " + fromdate1);
//                    end_date1.setText(Appreference.setTaskTime(true,fromdate1));
//                Toast.makeText(getApplicationContext(),"enddate is hidden",Toast.LENGTH_SHORT).show();
                }
//                end_date1.setText(Appreference.getDeviceTime24or12(context, fromdate1));
                Log.i("task", "endDate second " + endDate + " " + datecheck);
                if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
                    reminder_freq_local.setText("Every minute");
                } else {
                    reminder_freq1.setText("Minutes");
                }
            }

        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            Log.i("TaskDateUpdate", "failure else ");
            Log.i("date and time", "sdf.format(date)" + sdf.format(date));
            if (android.text.format.DateFormat.is24HourFormat(context)) {
                start_date1.setText(Appreference.ChangeDevicePattern(true, sdf.format(date), datepattern));
            } else {
//                int hour=date.getHours();
//                int minute=date.getMinutes();
//                int seconds=date.getSeconds();
//                start_date1.setText(hour%12 + ":" + minute + ":"+seconds+" " + ((hour>=12) ? "PM" : "AM"));
                Log.i("TaskDateUpdate", "sdf.format" + sdf.format(date));
                start_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, sdf.format(new Date())), datepattern));
            }
//            Toast.makeText(getApplicationContext(), "startdate is 2 hidden", Toast.LENGTH_SHORT).show();
//            start_date1.setText(Appreference.getDeviceTime24or12(context, sdf.format(new Date())));
        }
        Log.i("task", "endDate three " + endDate + " " + datecheck);
        if (datecheck) {
            if (android.text.format.DateFormat.is24HourFormat(context)) {
                end_date1.setText(Appreference.ChangeDevicePattern(true, endDate, datepattern));
            } else {
                end_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, endDate), datepattern));
            }
//            end_date1.setText(Appreference.getDeviceTime24or12(context, endDate));
            Log.i("task", "endDate four " + endDate + " " + datecheck);
        }
//        String todate1 = end_date1.getText().toString();
//        todate1 = todate1.split(" ")[0] + " 23:40:00";
        if (!android.text.format.DateFormat.is24HourFormat(context)) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            Date ddate = null;
            try {
                Log.i("TaskDateUpdate", "reminder_date 2 " + start_date1.getText().toString());
                ddate = dateFormat.parse(Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern));
            } catch (ParseException e) {
                e.printStackTrace();
                Appreference.printLog("TaskDateUpdate tempValue is failure", "Exception " + e.getMessage(), "WARN", null);
            }
            Calendar cl = Calendar.getInstance();
            if (ddate != null) {
                Log.i("TaskDateUpdate", "reminder_date 1 " + ddate);
                cl.setTime(ddate);
                cl.add(Calendar.MINUTE, 1);
                reminder_date = dateFormat.format(cl.getTime());
//            reminder_date=Appreference.getEndOfDay(reminder_date);
                Log.i("TaskDateUpdate", "reminder_date 0 " + reminder_date);
                reminder_date1.setText(Appreference.ChangeDevicePattern(false, reminder_date, datepattern));
            }
        } else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date ddate = null;
            try {
                String ddd = "";
                if (android.text.format.DateFormat.is24HourFormat(context)) {
                    ddd = Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern);
                } else {
                    ddd = Appreference.ChangeOriginalPattern(false, Appreference.setDateTime(true, start_date1.getText().toString()), datepattern);
                }
                if (ddd != null && !ddd.equalsIgnoreCase(""))
                    ddate = dateFormat.parse(ddd);
            } catch (ParseException e) {
                e.printStackTrace();
                Appreference.printLog("TaskDateUpdate tempValue is failure", "Exception " + e.getMessage(), "WARN", null);
            }
            Calendar cl = Calendar.getInstance();
            if (ddate != null) {
                cl.setTime(ddate);
                cl.add(Calendar.MINUTE, 1);
                reminder_date = dateFormat.format(cl.getTime());
//            reminder_date=Appreference.getEndOfDay(reminder_date);
                Log.i("TaskDateUpdate", "reminder_date 00 " + reminder_date);
                if (android.text.format.DateFormat.is24HourFormat(context)) {
                    reminder_date1.setText(Appreference.ChangeDevicePattern(true, reminder_date, datepattern));
                } else {
                    Log.i("TaskDateUpdate", "reminder date2 value is" + reminder_date);
                    reminder_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, reminder_date), datepattern));
//                    reminder_date1.setText(Appreference.setTimeinDevicePattern(false,reminder_date,datepattern));
                }
            }
        }

        if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
            repeat_frequency.setText("Every minute");
        } else {
            repeat_frequency.setText("Minutes");
        }
        if (temp_value.equalsIgnoreCase("success")) {
            if (temp_durationunit != null) {
                duration_unit.setText(temp_durationunit.replaceAll("\"", " ").trim());
            } else {
                duration_unit.setText("Hours");
            }
            if (temp_duration != null) {
                duration.setText(temp_duration);
            } else {
                duration.setText("1");
            }
            if (temp_timefreq != null && temp_timefreq.contains(" ")) {
                temp_min.setText(temp_timefreq.split(" ")[0]);
                repeat_frequency.setText(temp_timefreq.split(" ")[1]);
            }
        }
        duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context, R.style.AppTheme);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.numberpicker);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.horizontalMargin = 15;
                Window window = dialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
                window.setAttributes(lp);
                window.setGravity(Gravity.CENTER);
                dialog.show();
                Button cancel = (Button) dialog.findViewById(R.id.button2);
                Button set = (Button) dialog.findViewById(R.id.button1);
                final NumberPicker np = (NumberPicker) dialog.findViewById(R.id.numberPicker1);
                np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
                np.setMaxValue(200);
                np.setMinValue(1);
                np.setWrapSelectorWheel(false);

                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        try {
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("TaskDateUpdate cancel.setOnClickListener", "Exception " + e.getMessage(), "WARN", null);
                        }
                    }
                });
                set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            dialog.dismiss();
                            duration.setText(String.valueOf(np.getValue()));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("TaskDateUpdate set.setOnclickListener", "Exception " + e.getMessage(), "WARN", null);
                        }

                    }
                });


            }
        });
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    rem_audio_play.setVisibility(View.VISIBLE);
                    Intent i = new Intent(TaskDateUpdate.this, AudioRecorder.class);
                    i.putExtra("task", "date");
                    startActivityForResult(i, 333);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("TaskDateUpdate audio.setOnclickListener", "Exception " + e.getMessage(), "WARN", null);
                }
            }
        });

        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("strIPath 1", "strIPath playbutton " + strIPath);

                try {
                    if (strIPath != null) {
                        Log.i("strIPath 1", "1 strIPath playbutton " + strIPath);
                        audio.setVisibility(View.INVISIBLE);
                        isPlayCompleted = false;
                        if (mPlayer.isPlaying()) {
                            mPlayer.pause();
                            play_button.setImageResource(R.drawable.play);
                        } else {
                            seekbar.setProgress(0);
                            Log.d("playing", "played.............");
                            play_button.setImageResource(R.drawable.ic_pause_xl);
                            playtimer = false;
                            Log.d("playing", "playtimer");
                            try {

                                mHandler.postDelayed(mProgressUpdater, 500);
                                mPlayer.start();
                                playtimer = true;
                                seekbar.setProgress(0);
                                seekbar.setMax(mPlayer.getDuration());
                                updatetime.run();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("TaskDateUpdate play.setOnclickListener", "Exception " + e.getMessage(), "WARN", null);
                                stopPlayback();
                            }
                        }
                    } else {
                        Log.i("strIPath 1", "2 strIPath playbutton " + strIPath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("TaskDateUpdate play.setOnclickListener", "Exception " + e.getMessage(), "WARN", null);
                    stopPlayback();
                }
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                               @Override
                                               public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {
                                               }

                                               @Override
                                               public void onStartTrackingTouch(SeekBar seekBar) {
                                                   mPlayer.seekTo(seekBar.getProgress());
                                                   updatetime.run();
                                               }

                                               @Override
                                               public void onStopTrackingTouch(SeekBar seekBar) {
                                                   if (!mPlayer.isPlaying())
                                                       mPlayer.pause();
                                                   int currentPosition = (seekBar.getProgress());

                                                   mPlayer.seekTo(currentPosition);
                                                   updatetime.run();
                                               }
                                           }
        );

        updatetime = new Runnable() {
            @Override
            public void run() {
                if (mPlayer.isPlaying()) {
                    if (isPlayCompleted) {

                    } else {
                        Log.i("valueof ", String.valueOf(mPlayer.getCurrentPosition() / 1000));
                        seekbar.setProgress(mPlayer.getCurrentPosition());
                        handlerSeek.postDelayed(updatetime, 100);
                    }
                }
            }
        };
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onComplete();
            }
        });
    }

    public static String getDeviceName() {
        String reqString = Build.MANUFACTURER
                + " " + Build.MODEL;
        return reqString;
    }

    public static String getDeviceDatePattern(Context applicationContext) {
        String date_pattern = null;
        try {
            Log.i("taskdateupdate", "111111 " + applicationContext);
            Format dateFormat = android.text.format.DateFormat.getDateFormat(applicationContext);
            date_pattern = ((SimpleDateFormat) dateFormat).toLocalizedPattern();
//            Toast.makeText(context,date_pattern,Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TaskDateUpdate getDevicePattern", "Exception " + e.getMessage(), "WARN", null);
        }
        Log.i("TaskDateUpdate", "datepattern value is-->" + date_pattern);
        return date_pattern;
    }


    private class TextAdapter extends BaseAdapter {

        Context context;
        ArrayList<SwipeBean> bean;

        public TextAdapter(Context context) {
            this.context = context;
        }

        public TextAdapter(ArrayList<SwipeBean> beanArrayList, TaskDateUpdate taskDateUpdate) {
            this.context = taskDateUpdate;
            this.bean = beanArrayList;
        }

        @Override
        public int getCount() {

            return bean.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub

            return bean.get(position);
        }

        @Override
        public long getItemId(int position) {

            // TODO Auto-generated method stub

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub


            TextView text = new TextView(this.context);
            text.setTextColor(getResources().getColor(R.color.black));
            text.setGravity(Gravity.CENTER);
//            text.setLayoutParams(new GridView.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, GridLayout.LayoutParams.MATCH_PARENT));
            text.setLayoutParams(new GridView.LayoutParams(115, 100));
            text.setText(String.valueOf(bean.get(position).getAdapterValue()));
            text.setGravity(Gravity.CENTER);
            if (bean.get(position).isSelected()) {
                text.setBackgroundColor(getResources().getColor(R.color.appcolor));
            } else {
                text.setBackgroundResource(R.drawable.shape);
            }
            return text;

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            clockType = android.provider.Settings.System.getString(getApplicationContext().getContentResolver(), android.provider.Settings.System.TIME_12_24);
            is24format = android.text.format.DateFormat.is24HourFormat(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TaskDateUpdate onresume", "Exception " + e.getMessage(), "WARN", null);
        }
        Log.d("conflict", "onResume called");
        if (Appreference.isConflict) {
            if (!android.text.format.DateFormat.is24HourFormat(context)) {
//                if (!getResources().getString(R.string.proxyua).equalsIgnoreCase("enable")) {
//                    Intent i = new Intent();
//                    i.putExtra("StartDate",Appreference.setDateTime(true,Appreference.ChangeOriginalPattern(false,start_date1.getText().toString(),datepattern)));
//                    i.putExtra("EndDate",Appreference.setDateTime(true,Appreference.ChangeOriginalPattern(false,end_date1.getText().toString(),datepattern)));
//                    i.putExtra("ReminderDate", Appreference.setDateTime(true,Appreference.ChangeOriginalPattern(false,reminder_date1.getText().toString(),datepattern)));
//                    i.putExtra("ReminderFrequency", remfreq_mins);
//                    i.putExtra("ReminderQuote", Reminder_quote.getText().toString());
//                    i.putExtra("ReminderTone", strIPath);
//                    if (weekdays != null && !weekdays.equalsIgnoreCase("")) {
//                        i.putExtra("Weekdays", weekdays);
//                    }
//                    i.putExtra("changedateheader", "assigned");
//                    Appreference.isConflict = false;
//                    setResult(RESULT_OK, i);
//                    finish();
//                } else {
                Intent i = new Intent();
                i.putExtra("StartDate", Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern)));
                i.putExtra("EndDate", Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, end_date1.getText().toString(), datepattern)));
                i.putExtra("ReminderTone", strIPath);
                if (reminder_switch.isChecked()) {
                    Log.i("Taskdateupdate", "reminder_switch.isChecked() @@@ " + reminder_switch.isChecked());
                    i.putExtra("ReminderDate", Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, reminder_date1.getText().toString(), datepattern)));
                    i.putExtra("ReminderQuote", Reminder_quote.getText().toString());
                } else {
                    Log.i("Taskdateupdate", "reminder_switch.isChecked() @@@ " + reminder_switch.isChecked());
                    i.putExtra("ReminderDate", "");
                    i.putExtra("ReminderQuote", "");
                }
                if (repeat_frequency_switch.isChecked()) {
                    Log.i("Taskdateupdate", "repeat_frequency_switch.isChecked() @@@ " + repeat_frequency_switch.isChecked());
                    i.putExtra("ReminderFrequency", remfreq_mins);
                } else {
                    Log.i("Taskdateupdate", "repeat_frequency_switch.isChecked() @@@ " + repeat_frequency_switch.isChecked());
                    i.putExtra("ReminderFrequency", "");
                }
                if (weekdays != null && !weekdays.equalsIgnoreCase("")) {
                    i.putExtra("Weekdays", weekdays);
                }
                i.putExtra("changedateheader", "assigned");
                setResult(RESULT_OK, i);
                Appreference.isConflict = false;
                finish();
//                }

            } else {
//                if (!getResources().getString(R.string.proxyua).equalsIgnoreCase("enable")) {
//                    Intent i = new Intent();
//                    i.putExtra("StartDate", Appreference.ChangeOriginalPattern(true,start_date1.getText().toString(),datepattern));
//                    i.putExtra("EndDate", Appreference.ChangeOriginalPattern(true,end_date1.getText().toString(),datepattern));
//                    i.putExtra("ReminderDate", Appreference.ChangeOriginalPattern(true,reminder_date1.getText().toString(),datepattern));
//                    i.putExtra("ReminderFrequency", remfreq_mins);
//                    i.putExtra("ReminderQuote", Reminder_quote.getText().toString());
//                    i.putExtra("ReminderTone", strIPath);
//                    if (weekdays != null && !weekdays.equalsIgnoreCase("")) {
//                        i.putExtra("Weekdays", weekdays);
//                    }
//                    i.putExtra("changedateheader", "assigned");
//                    Appreference.isConflict = false;
//                    setResult(RESULT_OK, i);
//                    finish();
//                } else {
                Intent i = new Intent();
                i.putExtra("StartDate", Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern));
                i.putExtra("EndDate", Appreference.ChangeOriginalPattern(true, end_date1.getText().toString(), datepattern));
                i.putExtra("ReminderTone", strIPath);
                if (reminder_switch.isChecked()) {
                    Log.i("Taskdateupdate", "reminder_switch.isChecked() @@@ " + reminder_switch.isChecked());
                    i.putExtra("ReminderDate", Appreference.ChangeOriginalPattern(true, reminder_date1.getText().toString(), datepattern));
                    i.putExtra("ReminderQuote", Reminder_quote.getText().toString());
                } else {
                    Log.i("Taskdateupdate", "reminder_switch.isChecked() @@@ " + reminder_switch.isChecked());
                    i.putExtra("ReminderDate", "");
                    i.putExtra("ReminderQuote", "");
                }
                if (repeat_frequency_switch.isChecked()) {
                    Log.i("Taskdateupdate", "repeat_frequency_switch.isChecked() @@@ " + repeat_frequency_switch.isChecked());
                    i.putExtra("ReminderFrequency", remfreq_mins);
                } else {
                    Log.i("Taskdateupdate", "repeat_frequency_switch.isChecked() @@@ " + repeat_frequency_switch.isChecked());
                    i.putExtra("ReminderFrequency", "");
                }
                if (weekdays != null && !weekdays.equalsIgnoreCase("")) {
                    i.putExtra("Weekdays", weekdays);
                }
                i.putExtra("changedateheader", "assigned");
                setResult(RESULT_OK, i);
                Appreference.isConflict = false;
                finish();
//                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("time", "frequency");
        if (resultCode == RESULT_OK && requestCode == 333) {
            Log.d("filePath", data.getStringExtra("taskFileExt"));
            strIPath = data.getStringExtra("taskFileExt");
            Log.i("strIPath 1", "strIPath OnActivity " + strIPath);
            seekbar.setProgress(0);

            try {
                if (mPlayer != null) {
                    mPlayer.reset();
                }
                if (strIPath != null) {
                    mPlayer.setDataSource(strIPath);
                    mPlayer.prepare();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Appreference.printLog("TaskDateUpdate onActivityResult", "Exception " + e.getMessage(), "WARN", null);
            }
//            play_button.setClickable(true);
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(strIPath);
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            mmr.release();
            String min, sec;
            min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration)));
            sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(duration)) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(duration))));
            if (Integer.parseInt(min) < 10) {
                min = 0 + String.valueOf(min);
            }
            if (Integer.parseInt(sec) < 10) {
                sec = 0 + String.valueOf(sec);
            }
            txt_time.setText(min + ":" + sec);

                   /* MediaListBean uIbean = new MediaListBean();
                    uIbean.setMediaType("audio");
                    uIbean.setMediaPath(path);
                    mediaList.add(uIbean);
                    medialistadapter.notifyDataSetChanged();*/
        } else if (resultCode == RESULT_OK && requestCode == 1) {
            String s = data.getStringExtra("TimeFrequency");
            Log.i("time", "frequency" + s);
            if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
                reminder_freq_local.setText(s);
            } else {
                reminder_freq1.setText(s);
            }
        } else if (resultCode == RESULT_OK && requestCode == 66) {
            String s = data.getStringExtra("TimeFrequency");
            Log.i("time", "frequency" + s);
            repeat_frequency.setText(s);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == start_date1.getId()) {
            if (taskStatus != null && taskStatus.equalsIgnoreCase("assigned"))
                showdatetime_dialog(1);
        } else if (v.getId() == end_date1.getId()) {
            if (start_date1.getText().toString().length() > 0) {
                showdatetime_dialog(2);
            } else
                Toast.makeText(context, context.getString(R.string.kindly_set_Starttime),
                        Toast.LENGTH_LONG).show();
        } else if (v.getId() == reminder_date1.getId()) {
            showdatetime_dialog(3);
        }
    }

    public void DurationUnit(final String type) {
        try {
            if (temp_value.equalsIgnoreCase("success")) {
                Log.i("clone", "===> inside message response");
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_myacc_menu);
                dialog.setCancelable(false);
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
                TextView hour = (TextView) dialog.findViewById(R.id.delete_acc);
                TextView day = (TextView) dialog.findViewById(R.id.log_out);

                if (type.equalsIgnoreCase("duration")) {
                    day.setText("Days");
                    hour.setText("Hours");
                }
                TextView cancel1 = (TextView) dialog.findViewById(R.id.cancel);
                cancel1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        try {
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("TaskDateUpdate cancel1.setOnclickListener", "Exception " + e.getMessage(), "WARN", null);
                        }
                    }
                });
                day.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        duration_unit.setText("Days");
                        dialog.dismiss();
                    }
                });
                hour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        duration_unit.setText("Hours");
                        dialog.dismiss();
                    }
                });
            } else {
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
                TextView minute = (TextView) dialog.findViewById(R.id.delete_acc);
                TextView hour = (TextView) dialog.findViewById(R.id.log_out);

                if (type.equalsIgnoreCase("duration")) {
                    minute.setText("Every minute");
                    hour.setText("Hours");
                }
                TextView cancel1 = (TextView) dialog.findViewById(R.id.cancel);
                cancel1.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        try {
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("TaskDateUpdate cancel1.setOnclickListener", "Exception " + e.getMessage(), "WARN", null);
                        }
                    }
                });
                minute.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        duration_unit.setText("Every minute");
                        dialog.dismiss();
                    }
                });
                hour.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        duration_unit.setText("Hours");
                        dialog.dismiss();
                    }
                });

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("TaskDateUpdate DurationUnit", "Exception " + e.getMessage(), "WARN", null);
        }
    }

    public void confictWebservice() {
        try {
            conflict = true;
            if (android.text.format.DateFormat.is24HourFormat(context)) {
                startdate = Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern);
                enddate = Appreference.ChangeOriginalPattern(true, end_date1.getText().toString(), datepattern);
            } else {
                startdate = Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern));
                enddate = Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, end_date1.getText().toString(), datepattern));
            }
            Log.i("date", "from user ID " + String.valueOf(fromId));
            Log.i("date", "To user ID " + String.valueOf(toUserId));
            Log.i("date", "start date " + startdate);
            Log.i("date", "end date " + enddate);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("fromUserId", String.valueOf(fromId)));
            nameValuePairs.add(new BasicNameValuePair("toUserId", String.valueOf(toUserId)));
            nameValuePairs.add(new BasicNameValuePair("plannedStartDate", startdate));
            nameValuePairs.add(new BasicNameValuePair("plannedEndDate", enddate));
            Appreference.jsonRequestSender.checkConflicts(EnumJsonWebservicename.checkConflicts, nameValuePairs, TaskDateUpdate.this);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("TaskDateUpdate conflictWebService", "Exception " + e.getMessage(), "WARN", null);
        }

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
                                        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                        String startdate_3 = sdf.format(startdate2);
                                        DateFormat formatter = DateFormat.getDateInstance();
                                        Date date_1 = formatter.parse(startdate2);*/
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
                                        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                        String startdate_3 = sdf.format(startdate2);
                                        DateFormat formatter = DateFormat.getDateInstance();
                                        Date date_1 = formatter.parse(startdate2);*/
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

                                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        Date ddate = null;
                                        try {
                                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                                ddate = dateFormat.parse(Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern));
                                            } else {
                                                ddate = dateFormat.parse(Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern)));
                                            }
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
                                        }

                                        enDate = values1[datePicker.getValue()];

                                        end_date = dateposition;
                                        end_time = hourPicker.getValue();
                                        end_minute = minutePicker.getValue();

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
                            String startampm = checkAmorPmfunction(startrem);  // am/pm
                            String startrem1 = startrem.split(":")[0];  // yyyy-mm-dd hh
                            fromtime = startrem1.split(" ")[1] + ":" + startrem.split(":")[1]; // hh:mm
                            Log.i("starttime", "starttime " + fromtime);
                            if (startampm.equalsIgnoreCase("p.m.")) {
                                startampm = "PM";
                            } else if (startampm.equalsIgnoreCase("a.m.")) {
                                startampm = "AM";
                            }
                            String endrem = "";
                            if (android.text.format.DateFormat.is24HourFormat(context)) {
                                endrem = Appreference.ChangeOriginalPattern(true, end_date1.getText().toString(), datepattern);
                            } else {
                                String aaq = Appreference.ChangeOriginalPattern(false, end_date1.getText().toString(), datepattern);
                                endrem = Appreference.setDateTime(true, aaq);
                            }

                            String endampm = checkAmorPmfunction(endrem);   // am/pm
                            String endrem1 = endrem.split(":")[0];  // yyyy-mm-dd hh
                            endTime = endrem1.split(" ")[1] + ":" + endrem.split(":")[1]; // hh:mm
                            Log.i("endTime", "endTime " + endTime);
                            if (endampm.equalsIgnoreCase("p.m.")) {
                                endampm = "PM";
                            } else if (endampm.equalsIgnoreCase("a.m.")) {
                                endampm = "AM";
                            }
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
                            if (remampm.equalsIgnoreCase("p.m.")) {
                                remampm = "PM";
                            } else if (remampm.equalsIgnoreCase("a.m.")) {
                                remampm = "AM";
                            }
                            Log.i("Remhrmin", "enTime" + enTime);

                            Log.i("Time", "fromtime" + fromtime);
                            Log.i("Time", "endTime" + endTime);
                            Log.i("Time", "pickertime" + enTime);
                            if ((strfromdate1.compareTo(strremindate1) <= 0) && (strenddate1.compareTo(strremindate1) >= 0)) {
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
                                    Log.i("comp", "reminderset==> $ ");
                                    Log.i("comp", "startampm 1==>  " + startampm);
                                    Log.i("comp", "endampm 1==> " + endampm);
                                    Log.i("comp", "remampm 1==> " + remampm);
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
                                    Log.i("comp", "reminderset ==> ");
                                    Log.i("comp", "startampm ==>  " + startampm);
                                    Log.i("comp", "endampm ==> " + endampm);
                                    Log.i("comp", "remampm ==> " + remampm);
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
    }

    private String checkAmorPmfunction(String amorpm) {
        DateFormat selctedDateformate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = selctedDateformate.parse(amorpm);
        } catch (ParseException e) {
            e.printStackTrace();
            Appreference.printLog("TaskDateUpdate checkAmorPmfunction", "Exception " + e.getMessage(), "WARN", null);
        }
        SimpleDateFormat day1 = new SimpleDateFormat("hh:mm a");
        String ammorpmm = day1.format(date);
        ammorpmm = ammorpmm.split(" ")[1];
        return ammorpmm;
    }

    private void showToast(String msg) {
        Toast.makeText(TaskDateUpdate.this, msg, Toast.LENGTH_LONG)
                .show();
    }

    private String[] getDatesFromCalender() {
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
            Log.i("SCHEDULECALL", "am_pm===>" + am_pm);
            Log.i("SCHEDULECALL", "amPM===>" + amPM);
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
                } else if (ampm!=null && ampm.equalsIgnoreCase("pm") && am_pm.equalsIgnoreCase("pm")) {
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
            Appreference.printLog("TaskDateUpdate checkReminderisValid", "Exception " + e.getMessage(), "WARN", null);
        }
        return isvalid;
    }


    @Override
    public void ResponceMethod(final Object object) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                CommunicationBean bean = (CommunicationBean) object;
                try {
                    String str = bean.getEmail();
                    String s2 = bean.getFirstname();
                    String test1 = str.toString();
                    Log.d("conflict", "value " + bean.getFirstname());
                    Log.d("conflict", "value " + bean.getEmail());
                    Log.d("conflict", "value " + test1);
                    if (bean.getFirstname() != null && bean.getFirstname().equalsIgnoreCase("checkConflicts")) {
                        Gson gson = new Gson();
                        //JsonArray jsonArray=new JsonArray();
                        //JsonElement jelement = new JsonParser().parse(str);
                        Log.i("response", "inside the conflict response");
                        Log.i("Response conflictTask ", "response " + str);

                        if (!str.trim().equalsIgnoreCase("[]")) {
                            Log.i("Response","conflict ==> "+str);
                            JSONArray jsonArray = new JSONArray(str);
                            if (jsonArray.length() > 0) {
                                // jsonArray=jelement.getAsJsonArray();
                                Log.i("response array", "response size" + jsonArray.length());
                                conflictobject.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    //JSONObject jsonObject1= (JSONObject) jsonArray.getJSONObject(i);
                                    Log.i("response ", "objects  " + jsonArray.get(i));
                                    ConflictCheckBean conflictCheck = gson.fromJson(jsonArray.getString(i), ConflictCheckBean.class);
                                    conflictobject.add(conflictCheck);
                                }
                                Log.i("size of conflict", "size " + conflictobject.size());
                                Log.i("TaskDateUpdate ", "conflictobject.size @ " + conflictobject.size());
                                Intent intent = new Intent(TaskDateUpdate.this, ConflictList.class);

                                //                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                //                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("Leave", "TaskDateUpdate");
                                intent.putExtra("conflictobject", conflictobject);
                                intent.putExtra("LeaveTaskId", CurrentTaskid);
                                startActivity(intent);
                            }
                        } else {
                            Log.i("Response","conflict else ==> "+str);
                            Intent intent = new Intent(TaskDateUpdate.this, ConflictList.class);
                            intent.putExtra("Leave", "TaskDateUpdate");
                            intent.putExtra("conflictobject", conflictobject);
                            intent.putExtra("LeaveTaskId", CurrentTaskid);
                            startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("TaskDateUpdate ResponceMethod", "Exception " + e.getMessage(), "WARN", null);
                }
            }
        });
    }

    @Override
    public void ErrorMethod(Object object) {

    }

    private void showprogress() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("login123", "inside showProgressDialog ");
                    progress = new ProgressDialog(TaskDateUpdate.this);
                    progress.setCancelable(false);
                    if (conflict) {
                        progress.setMessage("Checking Conflict.");
                        conflict = false;
                    }
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setProgress(0);
                    progress.setMax(100);
                    progress.show();
                } catch (Exception e) {
                    Appreference.printLog("TaskDateUpdate showprogress", "Exception " + e.getMessage(), "WARN", null);
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
            Appreference.printLog("TaskDateUpdate cancelDiallog", "Exception " + e.getMessage(), "WARN", null);
        }

    }


    private class WeekdaysAdaper extends BaseAdapter {
        ArrayList<WeekdayBean> exdayslist;
        LayoutInflater inflater = null;
        Context adapContext;

        public WeekdaysAdaper(TaskDateUpdate context) {
            this.adapContext = context;
        }

        public WeekdaysAdaper(ArrayList<WeekdayBean> weekdayBeanArrayList, TaskDateUpdate taskDateUpdate) {
            this.exdayslist = weekdayBeanArrayList;
            this.adapContext = taskDateUpdate;
        }

        @Override
        public int getCount() {
            return exdayslist.size();
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public String getItem(int position) {
            return getItem(position);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        public int getItemViewType(int position) {
            int value = 0;

            return value;
        }

        public View getView(int pos, View conView, ViewGroup group) {
            try {
                if (conView == null) {
                    inflater = (LayoutInflater) adapContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    conView = inflater.inflate(R.layout.weekdays_list, null,
                            false);
                }
                TextView days = (TextView) conView.findViewById(R.id.label);
                days.setText(String.valueOf(exdayslist.get(pos).getWeekday()));
                Log.i("pos", "6" + exdayslist.get(pos).isSelected);
                if (exdayslist.get(pos).isSelected) {
                    Log.i("pos", "7");
                    days.setTextColor(getResources().getColor(R.color.appcolor));
                } else {
                    Log.i("pos", "8");
                    days.setTextColor(getResources().getColor(R.color.black));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("TaskDateUpdate getview", "Exception " + e.getMessage(), "WARN", null);
            }

            return conView;

        }
    }


    public void stopPlayback() {
        mPlayingPosition = -1;
        if (mPlayer != null && mPlayer.isPlaying())
            mPlayer.stop();
        playtimer = false;
    }

    public void onComplete() {
        play_button.setImageResource(R.drawable.play);
        seekbar.setProgress(0);
        if (mPlayer != null) {
            mPlayer.reset();
            try {
                if (strIPath != null) {
                    mPlayer.setDataSource(strIPath);
                    mPlayer.prepare();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Record audio", Toast.LENGTH_SHORT);
                }

            } catch (IOException e) {
                e.printStackTrace();
                Appreference.printLog("TaskDateUpdate oncomplete", "Exception " + e.getMessage(), "WARN", null);
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    audio.setVisibility(View.VISIBLE);
                }
            });
        }
        isPlayCompleted = true;
        double tot_time = mPlayer.getDuration();
        if (strIPath != null) {
            String hms = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes((long) tot_time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours((long) tot_time)),
                    TimeUnit.MILLISECONDS.toSeconds((long) tot_time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) tot_time)));
            txt_time.setText(hms);
        } else {
            txt_time.setText("00:00");
        }
    }

    private class PlaybackUpdater implements Runnable {

        @Override
        public void run() {
            if (isPlayCompleted) {

            } else if (playtimer) {
                double sTime = mPlayer.getCurrentPosition();
                String min, sec;
                min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) sTime));
                sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) sTime)));
                if (Integer.parseInt(min) < 10) {
                    min = 0 + String.valueOf(min);
                }
                if (Integer.parseInt(sec) < 10) {
                    sec = 0 + String.valueOf(sec);
                }
                txt_time.setText(min + ":" + sec);
                int result = 100 * mPlayer.getCurrentPosition() / mPlayer.getDuration();
                Log.i("valueof", "intvalue" + result);
                mHandler.postDelayed(this, 100);

            }
            //not playing so stop updating
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPlayer != null && mPlayer.isPlaying())
            mPlayer.reset();
        playtimer = false;
    }

}
