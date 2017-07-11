package com.myapplication3;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.myapplication3.Bean.TaskDetailsBean;

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

import json.WebServiceInterface;

public class NoteDateUpdate extends AppCompatActivity implements View.OnClickListener, WebServiceInterface {

    public static Runnable time;
//    public static Typeface normal_type;
    private static String ampm = null;
    ArrayList<SwipeBean> beanArrayList;
    public String strIPath, CurrentTaskid;
    public LinearLayout linear1, linear2, linear3;
    EditText start_date1, end_date1, reminder_date1, Reminder_quote = null, reminder_freq1, duration, duration_unit, repeat_min, temp_min, repeat_frequency, reminder_freq_local;
    ImageView submit, cancel, play_button;
    TextView txt_time, re;
    Button audio;
    Context context;
    String[] dates;
    String today, enDate = null, userName, remfreq_mins;
    int yyyy;
    TaskDetailsBean bean;
    boolean datecheck = false;
    String endDate, clockType;
    private String toas = null;
    private String strTime, enTime;
    ArrayList<TaskDetailsBean> list_Array;
    ArrayList<WeekdayBean> weekdayBeanArrayList;
    TextView textview1;
    RelativeLayout radiolayout;
    LinearLayout rem_audio_play;
    String taskStatus;
    private NumberPicker datePicker, hourPicker, minutePicker, am_pmPicker;
    private String[] mAmPmStrings = null;
    private boolean endTimeSet = false, flag = false;
    private int start_date, start_time, start_minute, start_am_pm, end_date, end_time, end_minute, end_am_pm;
    private MediaPlayer mPlayer;
    private Handler mHandler = new Handler();
    private SeekBar seekbar = null;
    private int mPlayingPosition = -1;
    private PlaybackUpdater mProgressUpdater = new PlaybackUpdater();
    boolean conflict = false, is24format = false;
    int fromId, toUserId;
    String startdate, enddate, datepattern;
    static NoteDateUpdate noteDateUpdate;
    ProgressDialog progress;
    String weekdays = "";
    String taskType;

    boolean playtimer = false;
    private boolean isPlayCompleted = true;
    public static Runnable updatetime = null;
    private Handler handlerSeek = new Handler();

    public static NoteDateUpdate getInstance() {
        return noteDateUpdate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_date_update);
        context = this;
        noteDateUpdate = this;
//        Typeface roboto_regular = Typeface.createFromAsset(getAssets(), "roboto.regular.ttf");
//        Typeface roboto_medium = Typeface.createFromAsset(getAssets(), "roboto.medium.ttf");
//        Typeface roboto_bold = Typeface.createFromAsset(getAssets(), "roboto.bold.ttf");
        end_date1 = (EditText) findViewById(R.id.End_Date);

        play_button = (ImageView) findViewById(R.id.play_button);
        txt_time = (TextView) findViewById(R.id.txt_time);
        textview1 = (TextView) findViewById(R.id.textview1);
        Reminder_quote = (EditText) findViewById(R.id.Reminder_quote);
        seekbar = (SeekBar) findViewById(R.id.seekBar1);
        linear2 = (LinearLayout) findViewById(R.id.ll_3);
//        textview1.setTypeface(roboto_medium);
        rem_audio_play = (LinearLayout) findViewById(R.id.rem_audio_play);
        radiolayout = (RelativeLayout) findViewById(R.id.l1_10);

        datepattern = getDeviceDatePattern(getApplicationContext());

        try {
            clockType = android.provider.Settings.System.getString(getApplicationContext().getContentResolver(), android.provider.Settings.System.TIME_12_24);
            is24format = android.text.format.DateFormat.is24HourFormat(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String taskId = getIntent().getExtras().getString("taskId");

        CurrentTaskid = taskId;
        Log.d("DBValue", "weekday from db *" + taskId);

        taskType = getIntent().getStringExtra("taskType");
        list_Array = new ArrayList<TaskDetailsBean>();
        beanArrayList = new ArrayList<SwipeBean>();
        weekdayBeanArrayList = new ArrayList<WeekdayBean>();

        seekbar.setClickable(false);

        fromId = Appreference.loginuserdetails.getId();
        audio = (Button) findViewById(R.id.audio);

        end_date1.setOnClickListener(this);

        mPlayer = new MediaPlayer();
        Log.i("task", "taskUpdates");


        Log.d("conflict", "toUserId " + toUserId);
        cancel = (ImageView) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            }
        });


        submit = (ImageView) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    boolean currenttime;

                    Log.i("task", "Appreference.conflicttask " + Appreference.conflicttask);
                    Log.i("task", "Appreference.conflicttask " + taskType);


                    SimpleDateFormat day1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                            String strenddate = multimediaFieldValues.get(bean.getClientId()).toString();
                    Date cur_date = new Date();
                    String Current_Time;
                    Current_Time = day1.format(cur_date);

                    String strdate1;

                    if (android.text.format.DateFormat.is24HourFormat(context)) {
                        strdate1 = Appreference.ChangeOriginalPattern(true, end_date1.getText().toString(), datepattern);
                    } else {
                        String ssss = Appreference.ChangeOriginalPattern(false, end_date1.getText().toString(), datepattern);
                        strdate1 = Appreference.setDateTime(true, ssss);
                    }
//                            String strendday = strenddate.split(" ")[0];
//                            int strendday1 = Integer.parseInt(strendday.split("-")[2]);
                    Log.i("schedule", "strfromday-->" + end_date1.getText().toString());
                    Log.i("schedule", "Current_Time-->" + Current_Time);
                    Log.i("schedule", "strdate1-->" + strdate1);
                    Log.i("schedule", "strfromday-->" + Current_Time.compareTo(strdate1));
//                        SimpleDateFormat sdf=new SimpleDateFormat(datepattern);
//                        try {
//                            Log.i("schedule","changed format is  "+day1.format(sdf.parse(end_date1.getText().toString())));
//                            Log.i("schedule","datepattern is  "+strdate1);
//                            exists_Time =day1.format(sdf.parse(end_date1.getText().toString()));
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
                    if (Current_Time.compareTo(strdate1) <= 0) {
                        currenttime = true;
                    } else {
                        currenttime = false;
                    }
                    if (!currenttime) {
                        Toast.makeText(context, "Please enter future date", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!android.text.format.DateFormat.is24HourFormat(context)) {
                            Intent i = new Intent();
                            i.putExtra("ReminderDate", Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, end_date1.getText().toString(), datepattern)));
                            i.putExtra("ReminderQuote", Reminder_quote.getText().toString());
                            i.putExtra("ReminderTone", strIPath);
                            setResult(RESULT_OK, i);
                            finish();
                        } else {
                            Intent i = new Intent();
                            i.putExtra("ReminderDate", Appreference.ChangeOriginalPattern(true, end_date1.getText().toString(), datepattern));
                            i.putExtra("ReminderQuote", Reminder_quote.getText().toString());
                            i.putExtra("ReminderTone", strIPath);
                            setResult(RESULT_OK, i);
                            finish();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        SimpleDateFormat sdf_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fromdate1 = sdf_1.format(new Date());

        Log.i("Dateand time", "fromdate1 value is " + fromdate1);
        if (android.text.format.DateFormat.is24HourFormat(context)) {
            end_date1.setText(Appreference.ChangeDevicePattern(true, fromdate1, datepattern));
        } else {
            end_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, fromdate1), datepattern));
            Log.i("TaskDateUpdate", "fromdate1 value is " + fromdate1);
        }
        Log.i("task", "endDate second ==>  " + end_date1 + " " + datecheck);

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    rem_audio_play.setVisibility(View.VISIBLE);
                    Intent i = new Intent(NoteDateUpdate.this, AudioRecorder.class);
                    i.putExtra("task", "date");
                    startActivityForResult(i, 333);
                } catch (Exception e) {
                    e.printStackTrace();
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
                                stopPlayback();
                            }
                        }
                    } else {
                        Log.i("strIPath 1", "2 strIPath playbutton " + strIPath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
        });

        updatetime = new Runnable() {
            @Override
            public void run() {
                if (mPlayer.isPlaying()) {
                    if (!isPlayCompleted) {
//                    } else {
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

    public static String getDeviceDatePattern(Context applicationContext) {
        String date_pattern = null;
        try {
            Format dateFormat = android.text.format.DateFormat.getDateFormat(applicationContext);
            date_pattern = ((SimpleDateFormat) dateFormat).toLocalizedPattern();
//            Toast.makeText(context,date_pattern,Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
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

        public TextAdapter(ArrayList<SwipeBean> beanArrayList, NoteDateUpdate taskDateUpdate) {
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
        }
        Log.d("conflict", "onResume called");
        if (Appreference.isConflict) {
            if (!android.text.format.DateFormat.is24HourFormat(context)) {
                Intent i = new Intent();
                i.putExtra("StartDate", Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern)));
                i.putExtra("EndDate", Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, end_date1.getText().toString(), datepattern)));
                i.putExtra("ReminderDate", Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, reminder_date1.getText().toString(), datepattern)));
                i.putExtra("ReminderFrequency", remfreq_mins);
                i.putExtra("ReminderQuote", Reminder_quote.getText().toString());
                i.putExtra("ReminderTone", strIPath);
                if (weekdays != null && !weekdays.equalsIgnoreCase("")) {
                    i.putExtra("Weekdays", weekdays);
                }
                i.putExtra("changedateheader", "assigned");
                setResult(RESULT_OK, i);
                Appreference.isConflict = false;
                finish();
//                overridePendingTransition( R.anim.slide_in_down, R.anim.slide_out_down );
            } else {
                Intent i = new Intent();
                i.putExtra("StartDate", start_date1.getText().toString());
                i.putExtra("EndDate", end_date1.getText().toString());
                i.putExtra("ReminderDate", reminder_date1.getText().toString());
                i.putExtra("ReminderFrequency", remfreq_mins);
                i.putExtra("ReminderQuote", Reminder_quote.getText().toString());
                i.putExtra("ReminderTone", strIPath);
                if (weekdays != null && !weekdays.equalsIgnoreCase("")) {
                    i.putExtra("Weekdays", weekdays);
                }
                i.putExtra("changedateheader", "assigned");
                setResult(RESULT_OK, i);
                Appreference.isConflict = false;
                finish();
//                overridePendingTransition( R.anim.slide_in_down, R.anim.slide_out_down );
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

        if (v.getId() == end_date1.getId()) {
            Log.i("NoteDateUpdate", "end_date1 onclick ");

            showdatetime_dialog(2);
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
//            set.setTypeface(normal_type);
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
                am_pmPicker.setDisplayedValues(am_pm);
            }

            final Calendar rightNow = Calendar.getInstance(Locale.getDefault());
            int dd = rightNow.get(Calendar.HOUR_OF_DAY);
            Log.i("current", "hour" + dd);
            yyyy = rightNow.get(Calendar.YEAR);

            if (startOrEnd == 2) {
                Log.i("NoteDateUpdate", "end_date1 click event 2 ");
                String[] values1 = datePicker.getDisplayedValues();
                String end_hour;
                String en_date;
                if (android.text.format.DateFormat.is24HourFormat(context)) {
                    if (end_date1.getText().toString() != null) {
                        String date_ptn = Appreference.ChangeOriginalPattern(true, end_date1.getText().toString(), datepattern);
//                    end_hour = end_date1.getText().toString();
//                    en_date = end_date1.getText().toString();
                        end_hour = date_ptn;
                        en_date = date_ptn;
                        Log.i("NoteDateUpdate", "end_date1 click event 20 * " + end_hour + "   " + en_date);
                        end_hour = end_hour.split(" ")[1];
                        Log.i("end", "en_hour" + end_hour);
                        int endhour = Integer.parseInt(end_hour.split(":")[0]);
                        Log.i("end", "en_hour" + endhour);
                        en_date = en_date.split(" ")[0];
                        Log.i("NoteDateUpdate", "end_date1 click event 21 " + end_hour + "   " + en_date);
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
                        }
                        Log.i("NoteDateUpdate", "end_date1 click event 22 " + endhour);
                        Log.i("end", "is24format " + endhour);
                        hourPicker.setValue(endhour);
                    }
                } else {
                    if (end_date1.getText().toString() != null) {
                        String date_ptn = Appreference.ChangeOriginalPattern(false, end_date1.getText().toString(), datepattern);
                        en_date = Appreference.setDateTime(true, date_ptn);
                        end_hour = Appreference.setDateTime(true, date_ptn);
                        Log.i("NoteDateUpdate", "end_date1 click event 20 " + end_hour + "   " + en_date);
                        end_hour = end_hour.split(" ")[1];
                        Log.i("end", "en_hour" + end_hour);
                        int endhour = Integer.parseInt(end_hour.split(":")[0]);
                        Log.i("end", "en_hour" + endhour);
                        en_date = en_date.split(" ")[0];
                        Log.i("NoteDateUpdate", "end_date1 click event 21 " + end_hour + "   " + en_date);
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
                            // 20120821
                        } catch (Exception e) {
                            // wrong input
                        }
                        Log.i("NoteDateUpdate", "end_date1 click event 22 " + endhour);
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

            }

            if (startOrEnd == 2) {
                String en_min;
                if (android.text.format.DateFormat.is24HourFormat(context)) {
                    en_min = Appreference.ChangeOriginalPattern(true, end_date1.getText().toString(), datepattern);
                } else {
                    String date_rem = Appreference.ChangeOriginalPattern(false, end_date1.getText().toString(), datepattern);
                    en_min = Appreference.setDateTime(true, date_rem);
                }
                en_min = en_min.split(" ")[1];
                Log.i("end", "en_min" + en_min);
                int enmin = Integer.parseInt(en_min.split(":")[1]);
                Log.i("end", "en_min" + en_min);
                minutePicker.setValue(enmin);
            }

            set.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //noinspection UnusedAssignment
                    String[] values1 = datePicker.getDisplayedValues();
                    String[] values2 = hourPicker.getDisplayedValues();
                    String[] values3 = minutePicker.getDisplayedValues();
                    String[] values4 = am_pmPicker.getDisplayedValues();
                    String tdystr;
                    int dateposition = datePicker.getValue();
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
                    //Log.i("tdystr", "am_pm " + values4[am_pmPicker.getValue()]);
                    if (!android.text.format.DateFormat.is24HourFormat(context)) {
                        toas = tdystr + " " + values2[hourPicker.getValue()] + " : "
                                + values3[minutePicker.getValue()] + " "
                                + values4[am_pmPicker.getValue()];
                    } else {
                        toas = tdystr + " " + values2[hourPicker.getValue()] + " : "
                                + values3[minutePicker.getValue()];
                    }

                    // int month = dp.getMonth() + 1;
                    // if (day.length() == 1) {
                    // day = "0" + day;
                    // }
                    String strDateTime = values2[hourPicker.getValue()] + ":"
                            + values3[minutePicker.getValue()];// tp.getCurrentHour()
                    // + ":" + day;
                    String tm;

                    SimpleDateFormat time = new SimpleDateFormat("HH:mm");
                    tm = time.format(new Date());

                    Log.i("SCHEDULECALL", "Now Time Is====>" + tm);


                    if (startOrEnd == 2) {
                        String startrem;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                            startrem = sdf.format(new Date());
                            Log.i("NoteDateUpdate", "startrem * " + startrem);
                        } else {
                            startrem = Appreference.setDateTime(false, sdf.format(new Date()));
                            Log.i("NoteDateUpdate", "startrem ** " + startrem);
                        }
                        Log.i("NoteDateUpdate", "startrem *** " + startrem);
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

                        String strfromday = startrem.split(" ")[0];
                        Log.i("schedule", "toas-->" + toas);
                        DateFormat selctedDateformate;
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
                        Date cur_date = new Date();
                        String Current_Time;
                        Current_Time = day1.format(cur_date);

                        String strendday = strenddate.split(" ")[0];
                        int strendday1 = Integer.parseInt(strendday.split("-")[2]);
                        Log.i("schedule", "strfromday-->" + strendday1);
                        if (Current_Time.compareTo(strenddate) < 0) {
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
                        } else {
                            showToast(context.getString(R.string.Kindly_slct_future_time));
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


    private void showToast(String msg) {
        Toast.makeText(NoteDateUpdate.this, msg, Toast.LENGTH_LONG)
                .show();
    }

    private String[] getDatesFromCalender() {
        Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+1"));
        Calendar c2 = Calendar.getInstance(TimeZone.getTimeZone("GMT+1"));

        List<String> dates = new ArrayList<String>();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
            dates.add("Today");
            today = dateFormat.format(c1.getTime());
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

    private boolean CheckReminderIsValid(String am_pm, int day, String strDate, String date, boolean start) {
        boolean isvalid = false;
        try {
            Calendar rightNow = Calendar.getInstance(Locale.getDefault());
            int amPM = rightNow.get(Calendar.AM_PM);

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

                if (day > 0 && day < 364) {
                    isvalid = true;
                } else if (day == 0 && startTime > currentTime) {
                    isvalid = true;
                } else if (day == 0 && startTime >= currentTime
                        && startMin > currentMin) {
                    isvalid = true;
                } else if (day == 0 && amPM == 0
                        && am_pm.equalsIgnoreCase("pm")) {
                    Log.i("note","user");
                } else if (ampm.equalsIgnoreCase("pm") && am_pm.equalsIgnoreCase("pm")) {
                    isvalid = startTime >= currentTime
                            && startMin > currentMin;

                } else {

                    isvalid = false;
                }

            } else {
                isvalid = currentTime >= startTime || currentMin > startMin;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isvalid;
    }


    @Override
    public void ResponceMethod(final Object object) {

    }

    @Override
    public void ErrorMethod(Object object) {

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
Log.i("note","user");
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
