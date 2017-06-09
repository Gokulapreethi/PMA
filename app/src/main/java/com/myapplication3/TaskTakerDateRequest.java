package com.myapplication3;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.myapplication3.Bean.TaskDetailsBean;
import com.myapplication3.DB.VideoCallDataBase;

import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.app.CallActivity;
import org.pjsip.pjsua2.app.GroupMemberAccess;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.app.MyCall;

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
import java.util.concurrent.TimeUnit;

public class TaskTakerDateRequest extends Activity implements View.OnClickListener {

    EditText start_date1, end_date1, reminder_date1, reminder_freq2, repeat_min, Reminder_quote = null, remarks, Reminder_freq_local;
    Button taker_audio, taker_photo, sketch, taker_video, taker_files, taker_call, taker_update_location;
    public SwipeMenuListView takers_listOfFiles;
    LinearLayout linear5, linear8, linear4;
    String remfreq_mins;
    ImageView submit, cancel, play_button;
    TextView txt_time;
    Button audio;
    Context context;
    String isRemainderRequired = "";
    String[] dates;
    String today, strDATE, fromtime, endTime, enDate = null, ch_remQuotes, ch_remtone, reminder_date, taskStatus;
    private String toas = null;
    private String strTime, enTime;
    private static String ampm = null;
    private NumberPicker datePicker, hourPicker, minutePicker, am_pmPicker;
    public static Typeface normal_type;
    public String strIPath, fname;
    public String datepattern;
    private String[] mAmPmStrings = null;
    int yyyy;
    String test = "sam";
    String filePath, Location, username;
    private boolean startTimeSet = false, endTimeSet = false, flag = false;
    private int start_date, start_time, start_minute, start_am_pm, end_date, end_time, end_minute, end_am_pm;
    public static ArrayList<TaskDetailsBean> mediaList;
    public static ListArrayAdapter medialistadapter;
    public GroupMemberAccess groupMember_Access;
    String toUserId, taskType, ownerOfTask;
    String isOverDue_Msg="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_taker_datereq);

        context = this;
//        task_name1 = (EditText) findViewById(R.id.taskname);
        start_date1 = (EditText) findViewById(R.id.Start_Date);
        end_date1 = (EditText) findViewById(R.id.End_Date);
        reminder_date1 = (EditText) findViewById(R.id.Reminder_Date);
        repeat_min = (EditText) findViewById(R.id.repeat_min);
        reminder_freq2 = (EditText) findViewById(R.id.Reminder_freq);
        Reminder_freq_local = (EditText) findViewById(R.id.Reminder_freq_local);
        remarks = (EditText) findViewById(R.id.remarks);
        taker_audio = (Button) findViewById(R.id.taker_audio);
        taker_photo = (Button) findViewById(R.id.taker_photo);
        sketch = (Button) findViewById(R.id.taker_write);
        taker_video = (Button) findViewById(R.id.taker_video);
        taker_files = (Button) findViewById(R.id.taker_files);
        taker_call = (Button) findViewById(R.id.taker_file);
        takers_listOfFiles = (SwipeMenuListView) findViewById(R.id.takers_listOfFiles);
        taker_update_location = (Button) findViewById(R.id.taker_update_location);
        mediaList = new ArrayList<TaskDetailsBean>();
        medialistadapter = new ListArrayAdapter(this, mediaList);
        takers_listOfFiles.setAdapter(medialistadapter);

        try {

            taskType = getIntent().getStringExtra("taskType");
            toUserId = getIntent().getStringExtra("toUserId");
            ownerOfTask = getIntent().getStringExtra("ownerOfTask");
            Log.i("Task1", "percentage " + ownerOfTask);
            Log.i("Task1", "percentage " + taskType);
            Log.i("Task1", "percentage " + toUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        groupMember_Access = VideoCallDataBase.getDB(context).getMemberAccessList(toUserId);
        Log.i("groupMemberAccess", "datechange toUserId ** " + toUserId);
        Log.i("groupMemberAccess", "datechange  taskType ## " + taskType);
        Log.i("groupMemberAccess", "datechange getRespondAudio ## " + groupMember_Access.getRespondAudio());
        Log.i("groupMemberAccess", "datechange getRespondTask ## " + groupMember_Access.getRespondTask());

        if ((taskType != null && taskType.equalsIgnoreCase("Group")) && !Appreference.loginuserdetails.getUsername().equalsIgnoreCase(ownerOfTask) && (groupMember_Access.getRespondTask() != null && groupMember_Access.getRespondTask().contains("0"))) {
            taker_audio.setVisibility(View.GONE);
            taker_photo.setVisibility(View.GONE);
            sketch.setVisibility(View.GONE);
            taker_video.setVisibility(View.GONE);
            taker_files.setVisibility(View.GONE);
            taker_update_location.setVisibility(View.GONE);
            Log.i("groupMemberAccess", "assingNew cant reply !!! ");
        }  else if ((taskType != null && taskType.equalsIgnoreCase("Group")) && !Appreference.loginuserdetails.getUsername().equalsIgnoreCase(ownerOfTask)) {
            if ((taskType != null && taskType.equalsIgnoreCase("Group")) && ((groupMember_Access.getRespondAudio() != null && groupMember_Access.getRespondAudio().equalsIgnoreCase("0")))) {
                taker_audio.setVisibility(View.GONE);
                Log.i("groupMemberAccess", "assingNew audio !!! ");
            }
            if ((taskType != null && taskType.equalsIgnoreCase("Group")) && ((groupMember_Access.getRespondPhoto() != null && groupMember_Access.getRespondPhoto().equalsIgnoreCase("0")))) {
                taker_photo.setVisibility(View.GONE);
                Log.i("groupMemberAccess", "assingNew photo !!! ");
            }
            if ((taskType != null && taskType.equalsIgnoreCase("Group")) && ((groupMember_Access.getRespondSketch() != null && groupMember_Access.getRespondSketch().equalsIgnoreCase("0")))) {
                sketch.setVisibility(View.GONE);
                Log.i("groupMemberAccess", "assingNew sketch !!! ");
            }
            if ((taskType != null && taskType.equalsIgnoreCase("Group")) && ((groupMember_Access.getRespondVideo() != null && groupMember_Access.getRespondVideo().equalsIgnoreCase("0")))) {
                taker_video.setVisibility(View.GONE);
                Log.i("groupMemberAccess", "assingNew video !!! ");
            }
            if ((taskType != null && taskType.equalsIgnoreCase("Group")) && ((groupMember_Access.getRespondFiles() != null && groupMember_Access.getRespondFiles().equalsIgnoreCase("0")))) {
                taker_files.setVisibility(View.GONE);
                Log.i("groupMemberAccess", "assingNew file !!! ");
            }
            if ((taskType != null && taskType.equalsIgnoreCase("Group")) && ((groupMember_Access.getRespondLocation() != null && groupMember_Access.getRespondLocation().equalsIgnoreCase("0")))) {
                taker_update_location.setVisibility(View.GONE);
                Log.i("groupMemberAccess", "assingNew location !!! ");
            }
        }
        if ((taskType != null && taskType.equalsIgnoreCase("Group")) && ((groupMember_Access.getRespondConfCall() != null && groupMember_Access.getRespondConfCall().equalsIgnoreCase("0")))) {
            taker_call.setVisibility(View.GONE);
            Log.i("groupMemberAccess", "assingNew call !!! ");
        }

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(context);
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xFf,
                        0x00, 0x00)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Delete");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

            }
        };
        takers_listOfFiles.setMenuCreator(creator);
        takers_listOfFiles.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        takers_listOfFiles.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                mediaList.remove(position);
                medialistadapter.notifyDataSetChanged();
                return true;
            }
        });


        takers_listOfFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskDetailsBean taskDetailsBean = mediaList.get(position);
                if (taskDetailsBean.getMimeType().equalsIgnoreCase("image")) {
                    Log.i("group123", "icon clicked image");
                    Intent intent = new Intent(context, FullScreenImage.class);
                    intent.putExtra("image", taskDetailsBean.getTaskDescription());
                    context.startActivity(intent);
                } else if (taskDetailsBean.getMimeType().equalsIgnoreCase("audio")) {
                    Intent intent = new Intent(context, Audioplayer.class);
                    intent.putExtra("audio", taskDetailsBean.getTaskDescription());
                    context.startActivity(intent);
                } else if (taskDetailsBean.getMimeType().equalsIgnoreCase("video")) {
                    Log.i("group123", "icon clicked video");
                    File directory = new File(taskDetailsBean.getTaskDescription());
                    if (directory.exists()) {
                        Log.i("update", "video  **" + taskDetailsBean.getTaskDescription());
                        Intent intent = new Intent(context, VideoPlayer.class);
                        intent.putExtra("video", taskDetailsBean.getTaskDescription());
                        context.startActivity(intent);
                    }
                } else if (taskDetailsBean.getMimeType().equalsIgnoreCase("document")) {
                    Log.i("update conversation ", "mm file " + taskDetailsBean.getMimeType());
                    Log.i("update conversation ", "description " + taskDetailsBean.getTaskDescription());
                    File url = new File(taskDetailsBean.getTaskDescription());
                    if (url.exists()) {
                        Uri uri = Uri.fromFile(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                            // Word document
                            intent.setDataAndType(uri, "application/msword");
                        } else if (url.toString().contains(".pdf")) {
                            // PDF file
                            intent.setDataAndType(uri, "application/pdf");
                        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                            // Powerpoint file
                            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                            // Excel file
                            intent.setDataAndType(uri, "application/vnd.ms-excel");
                        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                            // WAV audio file
                            intent.setDataAndType(uri, "application/x-wav");
                        } else if (url.toString().contains(".rtf")) {
                            // RTF file
                            intent.setDataAndType(uri, "application/rtf");
                        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                            // WAV audio file
                            intent.setDataAndType(uri, "audio/x-wav");
                        } else if (url.toString().contains(".gif")) {
                            // GIF file
                            intent.setDataAndType(uri, "image/gif");
                        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                            // JPG file
                            intent.setDataAndType(uri, "image/jpeg");
                        } else if (url.toString().contains(".txt")) {
                            // Text file
                            intent.setDataAndType(uri, "text/plain");
                        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                            // Video files
                            intent.setDataAndType(uri, "video");
                        } else {
                            //if you want you can also define the intent type for any other file

                            //additionally use else clause below, to manage other unknown extensions
                            //in this case, Android will show all applications installed on the device
                            //so you can choose which application to use
                            intent.setDataAndType(uri, "*/*");
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        try {
                            context.startActivity(intent);
                        } catch (Exception e) {
                            Log.e("error", "" + e);
                        }

                    } else {
                        Log.i("can't open", "file");
                        Toast.makeText(context, "can't open", Toast.LENGTH_SHORT).show();
                    }
                } else if (taskDetailsBean.getMimeType() != null && taskDetailsBean.getMimeType().equalsIgnoreCase("map")) {
                    if (taskDetailsBean.getMimeType() != null && taskDetailsBean.getMimeType().equalsIgnoreCase("map")) {
                        Log.i("Location", "icon clicked map");
                        Log.d("Location", "map " + taskDetailsBean.getTaskDescription());
                        Log.d("Location", "map " + taskDetailsBean.getMimeType());
                        Bundle bundle = new Bundle();
                        bundle.putString("map", taskDetailsBean.getTaskDescription());
                        bundle.putString("viewmap", "location");
                        Intent intent = new Intent(context, LocationFind.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                }

            }
        });


        linear4 = (LinearLayout) findViewById(R.id.ll_4);
        linear5 = (LinearLayout) findViewById(R.id.ll_5);
        linear8 = (LinearLayout) findViewById(R.id.ll_8);

        start_date1.setOnClickListener(this);
        end_date1.setOnClickListener(this);
        reminder_date1.setOnClickListener(this);
        datepattern = TaskDateUpdate.getDeviceDatePattern(context);


        reminder_freq2.setInputType(InputType.TYPE_NULL);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(reminder_freq2.getWindowToken(), 0);

        Reminder_freq_local.setInputType(InputType.TYPE_NULL);
        InputMethodManager inputMethodManager1 = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager1.hideSoftInputFromWindow(Reminder_freq_local.getWindowToken(), 0);

        if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
            linear5.setVisibility(View.GONE);
        } else {
            linear8.setVisibility(View.GONE);
        }

        cancel = (ImageView) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        reminder_freq2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(reminder_freq2.getWindowToken(), 0);
                Intent i = new Intent(TaskTakerDateRequest.this, ReminderFrequencySelection.class);
                i.putExtra("TimeFrequency", 100);
                startActivityForResult(i, 100);
            }
        });

        Reminder_freq_local.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(Reminder_freq_local.getWindowToken(), 0);
                Intent i = new Intent(TaskTakerDateRequest.this, ReminderFrequencyLocal.class);
                i.putExtra("TimeFrequency", 100);
                startActivityForResult(i, 100);
            }
        });

        taker_files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent i = new Intent(TaskTakerDateRequest.this, FilePicker.class);
                    startActivityForResult(i, 55);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        taker_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multimediaImage("call");
            }
        });
        taker_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multimediaImage("image");

            }
        });
        sketch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                startActivityForResult(i, 423);
            }
        });
        taker_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multimediaImage("video");
            }
        });
        taker_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(TaskTakerDateRequest.this, AudioRecorder.class);
                    i.putExtra("task", "audio");
                    startActivityForResult(i, 333);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        taker_update_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(TaskTakerDateRequest.this, LocationFind.class);
                    startActivityForResult(intent, 888);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        submit = (ImageView) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Reminder", "remdate " + reminder_date + reminder_freq2 + repeat_min);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                if (reminder_date != null && reminder_freq2 != null && repeat_min != null) {
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

                        if (reminder_freq2.getText().toString().equalsIgnoreCase("Minutes")) {
                            reminder_count = (int) (((diff / (1000 * 60))) / Long.valueOf(repeat_min.getText().toString().trim()));
                        } else if (reminder_freq2.getText().toString().equalsIgnoreCase("hours")) {
                            reminder_count = (int) (((diff / (1000 * 60 * 60))) / Long.valueOf(repeat_min.getText().toString().trim()));
                        } else if (reminder_freq2.getText().toString().equalsIgnoreCase("days")) {
                            reminder_count = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                        }


                        if (reminder_count > 0 & reminder_count <= 1000) {

                        } else {
                            Toast.makeText(context, "Your Reminder count is " + reminder_count + "  please select between 0 to 1000 reminder", Toast.LENGTH_LONG).show();
                            return;
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
//                int remfreq_hr = Integer.parseInt(reminder_freq2.getText().toString());
//                remfreq_hr = remfreq_hr / 60;
//                int remfreq_mm = Integer.parseInt(reminder_freq2.getText().toString());
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
                String startrem;
                String enddate = "";
                SimpleDateFormat sdf1 = null;
                if (android.text.format.DateFormat.is24HourFormat(context)) {
                    sdf1 = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
                } else {
                    sdf1 = new SimpleDateFormat("MM/dd/yy hh:mm:ss a");
                }
                try {
                    enddate = sdf.format(sdf1.parse(end_date1.getText().toString()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                if (android.text.format.DateFormat.is24HourFormat(context)) {
                startrem = sdf.format(new Date());
//                } else {
//                    startrem = Appreference.setDateTime(false, sdf.format(new Date()));
//                }
//                if (android.text.format.DateFormat.is24HourFormat(context)) {
//                    startrem = sdf.format(new Date());
//                } else {
//                    startrem = Appreference.setDateTime(false, sdf.format(new Date()));
//                }
                if (startrem.compareTo(enddate) >= 0) {
                    Log.i("Enddatecheck ", "startrem  !!! " + startrem + "    enddate    " + enddate);
                    Toast.makeText(context, "Please check end date ", Toast.LENGTH_LONG).show();
                } else if (repeat_min.getText().toString().matches("0") || repeat_min.getText().toString().matches("")) {
                    Log.i("Enddatecheck ", "startrem  ### " + startrem);
                    showToast("Unit must never be null or none");
                    return;
                } else {
                    Log.i("Enddatecheck ", "startrem  @@@ " + startrem);
                    if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
                        remfreq_mins = Reminder_freq_local.getText().toString();
                    } else {
                        remfreq_mins = repeat_min.getText().toString() + " " + reminder_freq2.getText().toString();
                    }
                    String ch_remark = remarks.getText().toString();

//                if (Reminder_quote.getText().toString().equals("") || Reminder_quote.getText().toString().equals(null)) {
//                    Toast.makeText(getApplicationContext(), "Please Give Quote For This Task", Toast.LENGTH_LONG).show();
//                } else {
//                if (remfreq_mins.length() < 2) {
//                    remfreq_mins = "0" + remfreq_mins;
//                }
//                remfreq_mins = remfreq_mins + " mins";

                    if (android.text.format.DateFormat.is24HourFormat(context)) {
                        Intent i = new Intent();
                        i.putExtra("StartDate", Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern));
                        i.putExtra("EndDate", Appreference.ChangeOriginalPattern(true, end_date1.getText().toString(), datepattern));
                        i.putExtra("ReminderDate", Appreference.ChangeOriginalPattern(true, reminder_date1.getText().toString(), datepattern));
//                        i.putExtra("ReminderFrequency", remfreq_mins);
//                        i.putExtra("StartDate", start_date1.getText().toString());
//                        i.putExtra("EndDate", end_date1.getText().toString());
//                        i.putExtra("ReminderDate", reminder_date1.getText().toString());
                        if (isRemainderRequired != null && !isRemainderRequired.equalsIgnoreCase("") && isRemainderRequired.equalsIgnoreCase("Y")) {
                            i.putExtra("ReminderFrequency", remfreq_mins);
                        } else {
                            i.putExtra("ReminderFrequency", "");
                        }
//                        i.putExtra("ReminderFrequency", remfreq_mins);
                        i.putExtra("ReminderQuote", ch_remQuotes);
                        i.putExtra("ReminderTone", ch_remtone);
                        i.putExtra("remark", ch_remark);
                        i.putExtra("isRemainderRequired", isRemainderRequired);
                        i.putExtra("changedateheader", "requested");
                        i.putExtra("mediaList", mediaList);
                        setResult(RESULT_OK, i);
                        finish();
                    } else {
                        Intent i = new Intent();
                        i.putExtra("StartDate", Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern)));
                        i.putExtra("EndDate", Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, end_date1.getText().toString(), datepattern)));
                        i.putExtra("ReminderDate", Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, reminder_date1.getText().toString(), datepattern)));
//                        i.putExtra("ReminderFrequency", remfreq_mins);
//                        i.putExtra("StartDate", Appreference.setDateTime(true, start_date1.getText().toString()));
//                        i.putExtra("EndDate", Appreference.setDateTime(true, end_date1.getText().toString()));
//                        i.putExtra("ReminderDate", Appreference.setDateTime(true, reminder_date1.getText().toString()));
                        if (isRemainderRequired != null && !isRemainderRequired.equalsIgnoreCase("") && isRemainderRequired.equalsIgnoreCase("Y")) {
                            i.putExtra("ReminderFrequency", remfreq_mins);
                        } else {
                            i.putExtra("ReminderFrequency", "");
                        }
//                        i.putExtra("ReminderFrequency", remfreq_mins);
                        i.putExtra("ReminderQuote", ch_remQuotes);
                        i.putExtra("ReminderTone", ch_remtone);
                        i.putExtra("remark", ch_remark);
                        i.putExtra("isRemainderRequired", isRemainderRequired);
                        i.putExtra("changedateheader", "requested");
                        i.putExtra("mediaList", mediaList);
                        setResult(RESULT_OK, i);
                        finish();
                    }
                }
            }
        });


        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        start_date1.setText(sdf.format(new Date()));
        String fromdate1 = start_date1.getText().toString();
        fromdate1 = fromdate1.split(" ")[0] + " 23:55:00";
        end_date1.setText(fromdate1);
//        String todate1 = end_date1.getText().toString();
//        todate1 = todate1.split(" ")[0] + " 23:40:00";
        reminder_date1.setText(start_date1.getText().toString());
        reminder_freq2.setText("1");*/

//        taskStatus = getIntent().getExtras().getString("taskStatus");

        String ch_startdate = getIntent().getStringExtra("startdate");
        String ch_enddate = getIntent().getStringExtra("enddate");
        String ch_remindate = getIntent().getStringExtra("reminderdate");
        String ch_remfreq = getIntent().getStringExtra("reminderfreq");
        ch_remQuotes = getIntent().getStringExtra("reminderquotes");
        ch_remtone = getIntent().getStringExtra("remindertone");
        isRemainderRequired = getIntent().getStringExtra("isRemainderRequired");
        username = getIntent().getStringExtra("username");
        Log.i("TaskDateChangeRequest", "isRemainderRequired 111 " + isRemainderRequired);
        Log.i("TaskDateChangeRequest", "username 111 " + username);
        Log.i("TaskDateChangeRequest", "ch_remindate 111 " + ch_remindate);
        Log.i("TaskDateChangeRequest", "ch_remfreq 111 " + ch_remfreq);
//        if (ch_remindate != null && !ch_remindate.equalsIgnoreCase("") && !ch_remindate.equalsIgnoreCase(null) && !ch_remindate.equalsIgnoreCase("N/A")) {
        if (android.text.format.DateFormat.is24HourFormat(context)) {
            start_date1.setText(Appreference.ChangeDevicePattern(true, ch_startdate, datepattern));
            end_date1.setText(Appreference.ChangeDevicePattern(true, ch_enddate, datepattern));
            if (isRemainderRequired != null && !isRemainderRequired.equalsIgnoreCase("N")) {
                Log.i("TaskDateChangeRequest", "isRemainderRequired 111 if  " + isRemainderRequired);
                reminder_date1.setText(Appreference.ChangeDevicePattern(true, ch_remindate, datepattern));
            } else {
                Log.i("TaskDateChangeRequest", "isRemainderRequired 111 else  " + isRemainderRequired);
                reminder_date1.setText("N/A");
                reminder_date1.setEnabled(false);
                repeat_min.setEnabled(false);
                reminder_freq2.setEnabled(false);
            }
        } else {
            start_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, ch_startdate), datepattern));
            end_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, ch_enddate), datepattern));
            if (isRemainderRequired != null && !isRemainderRequired.equalsIgnoreCase("N")) {
                Log.i("TaskDateChangeRequest", "isRemainderRequired 111 if 12hrs " + isRemainderRequired);
                reminder_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, ch_remindate), datepattern));
            } else {
                Log.i("TaskDateChangeRequest", "isRemainderRequired 111 else 12hrs " + isRemainderRequired);
                reminder_date1.setText("N/A");
                reminder_date1.setEnabled(false);
                repeat_min.setEnabled(false);
                reminder_freq2.setEnabled(false);
            }
        }
        if (ch_remfreq != null && !ch_remfreq.equalsIgnoreCase("") && ch_remfreq.contains(" ")) {
            repeat_min.setText(ch_remfreq.split(" ")[0]);
        } else {
            repeat_min.setText("1");
        }
        if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
            Reminder_freq_local.setText(ch_remfreq);
        } else {
            if (ch_remfreq != null && !ch_remfreq.equalsIgnoreCase("") && ch_remfreq.contains(" ")) {
                reminder_freq2.setText(ch_remfreq.split(" ")[1]);
            } else {
                reminder_freq2.setText("Minutes");
            }
        }
       /* } else {
            if (android.text.format.DateFormat.is24HourFormat(context)) {
                start_date1.setText(Appreference.ChangeDevicePattern(true, ch_startdate, datepattern));
                end_date1.setText(Appreference.ChangeDevicePattern(true, ch_enddate, datepattern));
            } else {
                start_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, ch_startdate), datepattern));
                end_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, ch_enddate), datepattern));
            }
            linear4.setVisibility(View.GONE);
            linear5.setVisibility(View.GONE);
        }*/

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
            if (type.equalsIgnoreCase("call")) {
                gallery.setText("audio call");
                camera.setText("video call");
            } else if (type.equalsIgnoreCase("image")) {
                gallery.setText("Gallery");
                camera.setText("Camera");
            } else {
                gallery.setText("Gallery");
                camera.setText("Video");
            }
            if (type.equalsIgnoreCase("image")) {
                camera.setText("Camera");
            } else {
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
                        if (type.equals("image")) {
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
                        } else if (type.equals("video")) {
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
                        } else {
                            makeCall(true);

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
                        if (type.equals("image")) {
                            final String path = Environment.getExternalStorageDirectory()
                                    + "/High Message/";
                            File directory = new File(path);
                            if (!directory.exists())
                                directory.mkdirs();
                            strIPath = path + getFileName() + ".jpg";
                            Intent intent = new Intent(context, CustomVideoCamera.class);
                            Uri imageUri = Uri.fromFile(new File(strIPath));
                            intent.putExtra("filePath", strIPath);
                            intent.putExtra("isPhoto", true);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, 132);
                        } else if (type.equals("video")) {
                            final String path = Environment.getExternalStorageDirectory()
                                    + "/High Message/";
                            File directory = new File(path);
                            if (!directory.exists())
                                directory.mkdirs();
                            strIPath = path + getFileName() + ".mp4";
                            Intent intent = new Intent(context, CustomVideoCamera.class);
                            intent.putExtra("filePath", strIPath);
                            intent.putExtra("isPhoto", false);
                            intent.putExtra("filePath", strIPath);
//                            Uri fileUri = Uri.fromFile(new File(strIPath));
//                            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                            startActivityForResult(intent, 111);
                        } else {
                            makeCall(false);

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

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public class ListArrayAdapter extends ArrayAdapter<TaskDetailsBean> {

        ArrayList<TaskDetailsBean> BuddyList;
        LayoutInflater inflater = null;
        Context adapContext;

        public ListArrayAdapter(Context context, ArrayList<TaskDetailsBean> buddyList1) {
            super(context, R.layout.list_items, mediaList);
            BuddyList = buddyList1;
            adapContext = context;
        }

        public View getView(int pos, View conView, ViewGroup group) {
            try {
                if (conView == null) {
                    inflater = (LayoutInflater) adapContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    conView = inflater.inflate(R.layout.list_items, null,
                            false);
                }

                final TaskDetailsBean contactBean = BuddyList.get(pos);
                TextView userName = (TextView) conView.findViewById(R.id.username);
                TextView count = (TextView) conView.findViewById(R.id.status);
                ImageView imageView = (ImageView) conView.findViewById(R.id.view4);


                File imageFile = new File(contactBean.getTaskDescription());
                Log.i("update", "value  " + contactBean.getMimeType());
                Log.i("update", "value  " + contactBean.getTaskDescription());
                Log.i("update", "value  " + imageFile);
                if (imageFile.exists()) {
                    Log.i("update", "value--->" + imageFile);
                    Log.i("update", "value--->" + count);
                    Log.i("update", "value---->" + userName);
                    Log.i("update", "value---->" + contactBean.getMimeType());
                    Log.i("update", "value----->  " + contactBean.getTaskDescription());
                    count.setText(contactBean.getTaskDescription().split("High Message/")[1]);
                    if (contactBean.getMimeType().equalsIgnoreCase("image")) {
                        if (contactBean.getTaskDescription().contains("Sketch")) {
                            userName.setText("Sketch");
                            Log.i("update", "value---->" + userName);
                        } else {
                            userName.setText("Image");
                            Log.i("update", "value---->" + userName);
                        }
                        if (imageFile.exists()) {
                            imageView.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
                        }
                    } else if (contactBean.getMimeType().equalsIgnoreCase("audio")) {
                        userName.setText("Audio");
                        Log.i("update", "value---->" + userName);
                        imageView.setBackgroundResource(R.drawable.audio1);
                    } else if (contactBean.getMimeType().equalsIgnoreCase("video")) {
                        userName.setText("Video");
                        Log.i("update", "value---->" + userName);
                        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(imageFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                        imageView.setImageBitmap(bMap);
                    } else if (contactBean.getMimeType().equalsIgnoreCase("document")) {
                        userName.setText("File");
                        Log.i("update", "value---->" + userName);
                        imageView.setBackgroundResource(R.drawable.ic_blank_file_xl);
                    } else {
                        userName.setText("Type");
                        Log.i("update", "value---->" + userName);
                    }
                } else if (contactBean.getMimeType().equalsIgnoreCase("map")) {
                    userName.setText("map");
                    count.setText(contactBean.getTaskDescription());
                    Log.i("update", "value---->" + userName);
                    Log.i("update", "value---->" + userName);
                    imageView.setBackgroundResource(R.drawable.ic_mapimage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return conView;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
//                                    strIPath = path;
                                }

                                if (bitmap != null) {
                                    bitmap = Bitmap.createScaledBitmap(
                                            bitmap, 200, 150, false);
                                }
                                File imageFile = new File(strIPath);
                                if (selected_file.exists()) {
                                    Log.i("imagefile", "image");
                                    test = "image";
                                    TaskDetailsBean taskBean = new TaskDetailsBean();
                                    taskBean.setMimeType("image");
                                    taskBean.setTaskDescription(path);
                                    Log.e("update", "value" + strIPath);
                                    Log.e("update", "value" + path);
                                    Log.e("update", "value" + taskBean.getTaskDescription());
                                    /*MediaListBean uIbean = new MediaListBean();

                                    uIbean.setMediaType("image");
                                    uIbean.setMediaPath(strIPath);
                                    mediaList.add(uIbean);*/
                                    mediaList.add(taskBean);
                                    medialistadapter.notifyDataSetChanged();
                                }
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
                    Log.d("task", "........ size is------------->" + length);
                    Log.e("update", "value  " + strIPath);
                    Log.e("update", "value  " + selectedImageUri);
                    Log.e("update", "value  " + getFileName());
                    if (length <= 2) {
                        new bitmaploader().execute(selectedImageUri);
                    } else {
                        showToast("Kindly Select someother image,this image is too large");
                    }

                }
            } else if (requestCode == 32) {
                if (data != null) {
                    Log.i("AAA", "New activity 32*************");
                    Uri selectedImageUri = data.getData();
                    strIPath = getRealPathFromURI(selectedImageUri);
                    final String path = Environment.getExternalStorageDirectory()
                            + "/High Message/" + getFileName() + ".mp4";

                    strIPath = path;
                    try {
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

                        Log.i("AAA", "New activity " + strIPath);
                        File imageFile = new File(strIPath);
                        if (imageFile.exists()) {
                            test = "video";
                       /* MediaListBean uIbean = new MediaListBean();
                        uIbean.setMediaType("video");
                        uIbean.setMediaPath(strIPath);*/
                            TaskDetailsBean taskBean = new TaskDetailsBean();
                            taskBean.setMimeType("video");
                            taskBean.setTaskDescription(path);
                            Log.e("update", "value" + path);
                            Log.e("update", "value" + taskBean.getTaskDescription());
                            mediaList.add(taskBean);
                            medialistadapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == 423) {
                strIPath = data.getStringExtra("path");
                Log.i("Task", "path" + strIPath);
                File new_file = new File(strIPath);
                test = "image";
               /* MediaListBean uIbean = new MediaListBean();
                uIbean.setMediaType("image");
                uIbean.setMediaPath(strIPath);*/
                TaskDetailsBean taskBean = new TaskDetailsBean();
                taskBean.setMimeType("image");
                taskBean.setTaskDescription(strIPath);
                mediaList.add(taskBean);
                medialistadapter.notifyDataSetChanged();
                Log.i("AAAA", "onactivity result $$$$$$$$$$$$$$$" + strIPath);
            } else if (requestCode == 132) {
                strIPath = data.getStringExtra("filePath");
                Log.i("task1", "onactivity result $$$$$$$$$$$$$$$" + strIPath);
                File new_file = new File(strIPath);
                if (new_file.exists()) {
                    Log.i("task1", "onactivity result " + strIPath);
                    test = "image";
                   /* MediaListBean uIbean = new MediaListBean();
                    uIbean.setMediaType("image");
                    uIbean.setMediaPath(strIPath);*/
                    TaskDetailsBean taskBean = new TaskDetailsBean();
                    taskBean.setMimeType("image");
                    taskBean.setTaskDescription(strIPath);
                    mediaList.add(taskBean);
                    medialistadapter.notifyDataSetChanged();
                }
            } else if (requestCode == 333) {
                Log.d("filePath", data.getStringExtra("taskFileExt"));
                strIPath = data.getStringExtra("taskFileExt");
                test = "audio";
               /* MediaListBean uIbean = new MediaListBean();
                uIbean.setMediaType("audio");
                uIbean.setMediaPath(strIPath);*/
                TaskDetailsBean taskBean = new TaskDetailsBean();
                taskBean.setMimeType("audio");
                taskBean.setTaskDescription(strIPath);
                Log.i("mp", "mpath" + strIPath);
                mediaList.add(taskBean);
                medialistadapter.notifyDataSetChanged();
//                seekBar.setProgress(0);
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
//                buddyName.setText(min + ":" + sec);

            } else if (requestCode == 55) {

                filePath = data.getStringExtra("fileExt");
                String fileName = data.getStringExtra("fileName");
                strIPath = data.getStringExtra("filePath");
//                    filePath = fileName.split(".")[1];
//                    fileExt=fileExt1.substring(fileExt1.lastIndexOf(1));
                Log.i("file", "fileExt" + strIPath);
                Log.i("file", "fm--->" + filePath);
//                Log.i("filename", "fm--->" + fileName);
                Log.i("file", "fm--->" + strIPath);

//                docfileWebService(filePath);
//                    strIPath = filePath;
               /* MediaListBean uIbean = new MediaListBean();
                uIbean.setMediaType("doc");
                uIbean.setMediaPath(strIPath);
                uIbean.setExt(filePath);*/
                TaskDetailsBean taskBean = new TaskDetailsBean();
                taskBean.setMimeType("document");
                taskBean.setTaskDescription(strIPath);
                Log.i("mp", "mpath" + strIPath);
                mediaList.add(taskBean);
                medialistadapter.notifyDataSetChanged();
            } else if (requestCode == 111) {
                strIPath = data.getStringExtra("filePath");
                Log.i("Avideo", "New activity ************* : " + strIPath);
                File new_file = new File(strIPath);
                if (new_file.exists()) {
                    test = "video";
                  /*  MediaListBean uIbean = new MediaListBean();
                    uIbean.setMediaType("video");
                    uIbean.setMediaPath(strIPath);*/
                    TaskDetailsBean taskBean = new TaskDetailsBean();
                    taskBean.setMimeType("video");
                    taskBean.setTaskDescription(strIPath);
                    Log.e("update", "value" + taskBean.getTaskDescription());
                    Log.i("mp", "mpath" + strIPath);
                    mediaList.add(taskBean);
                    medialistadapter.notifyDataSetChanged();
                }
            } else if (requestCode == 33) {
                if (data != null) {
                    try {
                        Log.i("AAA", "New activity 33*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + getFileName() + ".mp4";

                        Log.i("AAA", "New activity " + strIPath);
                        strIPath = path;
                        Log.i("AAA", "New activity " + strIPath);
                      /*  MediaListBean uIbean = new MediaListBean();
                        uIbean.setMediaType("video");
                        uIbean.setMediaPath(strIPath);*/
                        TaskDetailsBean taskBean = new TaskDetailsBean();
                        taskBean.setMimeType("video");
                        taskBean.setTaskDescription(strIPath);
                        Log.e("update", "value" + taskBean.getTaskDescription());
                        Log.i("mp", "mpath" + strIPath);
                        mediaList.add(taskBean);
                        medialistadapter.notifyDataSetChanged();
                        try {
                            FileInputStream fin = (FileInputStream) getContentResolver().openInputStream(selectedImageUri);
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                /*File imageFile = new File(strIPath);
                if (imageFile.exists()) {
                    test = "video";
                    frameLayout.setVisibility(View.VISIBLE);
                    ad_play.setVisibility(View.GONE);
                    video_play_icon.setVisibility(View.VISIBLE);
                    ImageView.setVisibility(View.VISIBLE);
                    list_image.setVisibility(View.VISIBLE);
                    Bitmap bMap = ThumbnailUtils.createVideoThumbnail(imageFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                    ImageView.setImageBitmap(bMap);
                }*/
            } else if (requestCode == 888) {
                Log.d("latitude", data.getStringExtra("loc_latitude"));
                Location = data.getStringExtra("loc_latitude");
                Log.i("location", "loc_latitude " + Location);
                Log.i("update", "extension");
                TaskDetailsBean taskBean = new TaskDetailsBean();
                taskBean.setMimeType("map");
                taskBean.setTaskDescription(Location);
                Log.e("update", "value" + taskBean.getTaskDescription());
                Log.i("update", "location" + Location);
                mediaList.add(taskBean);
                medialistadapter.notifyDataSetChanged();
//                String sig_id = Utility.getSessionID();
//                sendMessage(Location, null, "map", null, null, sig_id);
            } else if (requestCode == 100) {
                String s = data.getStringExtra("TimeFrequency");
                Log.i("time", "frequency" + s);
                if (getResources().getString(R.string.TASKNOTIFICATION_FROM_SERVER).equalsIgnoreCase("0")) {
                    Reminder_freq_local.setText(s);
                } else {
                    reminder_freq2.setText(s);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == start_date1.getId()) {
//            showdatetime_dialog(1);
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


    public class bitmaploader extends AsyncTask<Uri, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            try {
                super.onPostExecute(result);
                Log.d("image", "came to post execute for image");

                Bitmap img = null;
                if (strIPath != null)
                    img = convertpathToBitmap(strIPath);

                img = convertpathToBitmap(strIPath);
                Log.i("task", "extension" + strIPath);
                String pathExtn = strIPath.split("/")[5];
                Log.i("task", "spilting" + pathExtn);
                pathExtn = pathExtn.substring(pathExtn.length() - 3);
                Log.i("task", "root---->" + pathExtn);
//                        Log.i("task", "extension" + strIPath.split(".")[strIPath.split(".").length - 1]);
                imgfun("image", strIPath, pathExtn);
                if (img != null) {

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
            final Calendar rightNow = Calendar.getInstance(Locale.getDefault());
            int dd = rightNow.get(Calendar.HOUR_OF_DAY);
            Log.i("current", "hour" + dd);
            yyyy = rightNow.get(Calendar.YEAR);
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
            if (startOrEnd == 1) {

//                String dateInputString=String.valueOf(start_date1);
                String[] values1 = datePicker.getDisplayedValues();
//                String str = values1[datePicker.getValue()];
                Log.i("TaskTakerDateRequest", "start_date1 value is" + start_date1.getText().toString());
                String st_hour = start_date1.getText().toString();
                st_hour = st_hour.split(" ")[1];
                String st_date = start_date1.getText().toString();
                st_date = st_date.split(" ")[0];

                Log.i("start", "st_date" + st_date);
                Log.i("start", "st_hour" + st_hour);
                if (android.text.format.DateFormat.is24HourFormat(context)) {
                    st_hour = start_date1.getText().toString();
                    st_date = start_date1.getText().toString();
                    Log.i("TaskDateUpdate", "start_date1 click event 10 " + st_date + "  " + st_hour);
                    st_hour = st_hour.split(" ")[1];
                    st_date = st_date.split(" ")[0];
                    Log.i("start", "st_date " + st_date);
                    Log.i("start", "st_hour " + st_hour);
                    Log.i("TaskDateUpdate", "start_date1 click event 11 " + st_date + "  " + st_hour);
                    try {
                        // obtain date and time from initial string
                        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
                        DateFormat targetFormat = new SimpleDateFormat("MMM dd yyyy");
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
                    }
//                datePicker.setValue();
                    int hour = Integer.parseInt(st_hour.split(":")[0]);
                    Log.i("start", "hour " + hour);
                    Log.i("TaskDateUpdate", "start_date1 click event 12 " + hour);
                    hourPicker.setValue(hour);
                } else {
                    st_date = Appreference.setDateTime(true, start_date1.getText().toString());
                    st_hour = Appreference.setDateTime(true, start_date1.getText().toString());
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
                String[] values1 = datePicker.getDisplayedValues();
                String end_hour = end_date1.getText().toString();
                end_hour = end_hour.split(" ")[1];
                Log.i("end", "en_hour" + end_hour);
                String en_date = end_date1.getText().toString();
                en_date = en_date.split(" ")[0];
                if (android.text.format.DateFormat.is24HourFormat(context)) {
                    end_hour = end_date1.getText().toString();
                    en_date = end_date1.getText().toString();
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
                        DateFormat targetFormat = new SimpleDateFormat("MMM dd yyyy");
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
                    Log.i("TaskDateUpdate", "end_date1 click event 22 " + endhour);
                    Log.i("end", "is24format " + endhour);
                    hourPicker.setValue(endhour);
                } else {
                    en_date = Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, end_date1.getText().toString(), datepattern));
                    end_hour = Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, end_date1.getText().toString(), datepattern));
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
                    re_hour = Appreference.ChangeOriginalPattern(true, reminder_date1.getText().toString(), datepattern);
                    re_date = Appreference.ChangeOriginalPattern(true, reminder_date1.getText().toString(), datepattern);
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
                        DateFormat targetFormat = new SimpleDateFormat("MMM dd yyyy");
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
                    }

                    Log.i("TaskDateUpdate", "reminder_date1 click event 32 " + rehour);
                    hourPicker.setValue(rehour);
                } else {
                    re_date = Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, reminder_date1.getText().toString(), datepattern));
                    re_hour = Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, reminder_date1.getText().toString(), datepattern));
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
                String st_min = start_date1.getText().toString();
                st_min = st_min.split(" ")[1];
                Log.i("start", "st_min" + st_min);
                int min = Integer.parseInt(st_min.split(":")[1]);
                Log.i("start", "st_min" + min);
                minutePicker.setValue(min);
//            } else if ((startOrEnd == 1 && startTimeSet) || !endTimeSet) {
            } else if (startOrEnd == 2) {
                String en_min = end_date1.getText().toString();
                en_min = en_min.split(" ")[1];
                Log.i("end", "en_min" + en_min);
                int enmin = Integer.parseInt(en_min.split(":")[1]);
                Log.i("end", "en_min" + en_min);
                minutePicker.setValue(enmin);
//                minutePicker.setValue(start_minute);
//                datePicker.setValue(start_date);
            } else if (startOrEnd == 3) {
                String re_min = Appreference.ChangeOriginalPattern(true, reminder_date1.getText().toString(), datepattern);
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
//                  String am_pmpicker = "";
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
                        String strfromdate = Appreference.setDateTime(true, start_date1.getText().toString());
                        String strfromday = strfromdate.split(" ")[0];
                        DateFormat selctedDateformate = null;
                        Date date = null;
                        if (!android.text.format.DateFormat.is24HourFormat(context)) {
                            selctedDateformate = new SimpleDateFormat("MMM dd yyyy hh : mm a");
                            try {
                                date = selctedDateformate.parse(toas);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            selctedDateformate = new SimpleDateFormat("MMM dd yyyy HH : mm");
                            try {
                                date = selctedDateformate.parse(toas);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
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
                                    if (android.text.format.DateFormat.is24HourFormat(context)) {
                                        start_date1.setText(Appreference.setDateTime(true, strenddate));
                                    } else {
                                        start_date1.setText(Appreference.setDateTime(false, strenddate));
                                    }
                                    String startdate2 = "";
                                    if (android.text.format.DateFormat.is24HourFormat(context)) {
                                        startdate2 = start_date1.getText().toString();
                                    } else {
                                        startdate2 = Appreference.setDateTime(false, start_date1.getText().toString());
                                    }
//                                    String startdate2 = start_date1.getText().toString();
                                    String fromdate2 = startdate2.split(" ")[0] + " 23:55:00";
                                    if (android.text.format.DateFormat.is24HourFormat(context)) {
                                        end_date1.setText(Appreference.ChangeDevicePattern(true, fromdate2, datepattern));
                                    } else {
                                        end_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, fromdate2), datepattern));
                                    }
//

//                                    end_date1.setText(fromdate2);
//                                String todate2 = end_date1.getText().toString();
//                                String todate3 = todate2.split(" ")[0] + " 23:40:00";
//                                reminder_date1.setText(start_date1.getText().toString());
//                            String date_start_temp[] = strTime.split(":");
//                            start_date = Integer.parseInt(date_start_temp[0]);
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date ddate = null;
                                    try {
                                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                                            ddate = dateFormat.parse(start_date1.getText().toString());
                                        } else {
                                            ddate = dateFormat.parse(Appreference.setDateTime(true, start_date1.getText().toString()));
                                        }
//                                        ddate = dateFormat.parse(start_date1.getText().toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Calendar cl = Calendar.getInstance();
                                    if (ddate != null && (isRemainderRequired != null && !isRemainderRequired.equalsIgnoreCase("N"))) {
                                        cl.setTime(ddate);
                                        cl.add(Calendar.MINUTE, 1);
                                        reminder_date = dateFormat.format(cl.getTime());
                                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                                            reminder_date1.setText(Appreference.ChangeDevicePattern(true, reminder_date, datepattern));
                                        } else {
                                            reminder_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, reminder_date), datepattern));
                                        }
//                                        reminder_date1.setText(Appreference.getDeviceTime24or12(context,reminder_date));
                                    } else {
                                        reminder_date1.setText("N/A");
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
                                    if (android.text.format.DateFormat.is24HourFormat(context)) {
                                        start_date1.setText(Appreference.ChangeDevicePattern(true, strenddate, datepattern));
                                    } else {
                                        start_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, strenddate), datepattern));
                                    }
//                                    start_date1.setText(strenddate);
                                    String startdate2 = "";
                                    if (android.text.format.DateFormat.is24HourFormat(context)) {
                                        startdate2 = start_date1.getText().toString();
                                    } else {
                                        startdate2 = Appreference.setDateTime(true, start_date1.getText().toString());
                                    }
//                                    String startdate2 = start_date1.getText().toString();
                                    String fromdate2 = startdate2.split(" ")[0] + " 23:55:00";
                                    if (android.text.format.DateFormat.is24HourFormat(context)) {
                                        end_date1.setText(Appreference.ChangeDevicePattern(true, fromdate2, datepattern));
                                    } else {
                                        end_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, fromdate2), datepattern));
                                    }
//                                    end_date1.setText(fromdate2);
//                                String todate2 = end_date1.getText().toString();
//                                String todate3 = todate2.split(" ")[0] + " 23:40:00";
//                                reminder_date1.setText(start_date1.getText().toString());
//                            String date_start_temp[] = strTime.split(":");
//                            start_date = Integer.parseInt(date_start_temp[0]);
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date ddate = null;
                                    try {
                                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                                            ddate = dateFormat.parse(start_date1.getText().toString());
                                        } else {
                                            ddate = dateFormat.parse(Appreference.setDateTime(true, start_date1.getText().toString()));
                                        }
//                                        ddate = dateFormat.parse(start_date1.getText().toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Calendar cl = Calendar.getInstance();
                                    Log.i("reminder", "remdate " + reminder_date);
                                    if (ddate != null && (isRemainderRequired != null && !isRemainderRequired.equalsIgnoreCase("N"))) {
                                        cl.setTime(ddate);
                                        cl.add(Calendar.MINUTE, 1);
                                        reminder_date = dateFormat.format(cl.getTime());
                                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                                            reminder_date1.setText(Appreference.ChangeDevicePattern(true, reminder_date, datepattern));
                                        } else {
                                            reminder_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, reminder_date), datepattern));
                                        }
//                                        reminder_date1.setText(reminder_date);
                                    } else {
                                        reminder_date1.setText("N/A");
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
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Kindly select future date", Toast.LENGTH_LONG).show();
                        }
                    } else if (startOrEnd == 2) {
                        String startrem = "";
//                        String startrem = start_date1.getText().toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        if (android.text.format.DateFormat.is24HourFormat(context)) {
//                            startrem = start_date1.getText().toString();
                            startrem = sdf.format(new Date());
                        } else {
//                            startrem = Appreference.setDateTime(true, start_date1.getText().toString());
                            startrem = Appreference.setDateTime(false, sdf.format(new Date()));
                        }
                        Log.i("TaskTakerDateRequest", "startrem1 value is" + startrem);
                        String startampm = checkAmorPmfunction(startrem);  // am/pm
                        Log.i("currentTime", "startrem " + startrem);
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
                        String strfromdate = "", strcurrdate_1 = "", strcurrday_1 = "";

                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                            strfromdate = Appreference.ChangeOriginalPattern(true, start_date1.getText().toString(), datepattern);
                        } else {
                            strfromdate = Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern));
                        }
//                        String strfromdate = start_date1.getText().toString();
//                        String strfrommonth = strfromdate.split("-")[1];
//                        int strfrommonth1 = Integer.parseInt(strfrommonth);
                        Log.i("TaskTakerDateRequest", "strfromdate value is" + strfromdate);
                        String strfromday = strfromdate.split(" ")[0];
                        int strfromday1 = Integer.parseInt(strfromday.split("-")[2]);
//                        Log.i("schedule", "strfrommonth-->" + strfrommonth);
                        Log.i("schedule", "strfromday-->" + strfromday1);
                        Log.i("schedule", "toas-->" + toas);

                        DateFormat selctedDateformate = null;
                        Date date = null;
//                        try {
//                            date = selctedDateformate.parse(yyyy + " " + toas);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
                        if (!android.text.format.DateFormat.is24HourFormat(context)) {
                            selctedDateformate = new SimpleDateFormat("MMM dd yyyy hh : mm a");
                            try {
                                date = selctedDateformate.parse(toas);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            selctedDateformate = new SimpleDateFormat("MMM dd yyyy HH : mm");
                            try {
                                date = selctedDateformate.parse(toas);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        SimpleDateFormat day1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String strenddate = day1.format(date);

                        String strendday = strenddate.split(" ")[0];
                        int strendday1 = Integer.parseInt(strendday.split("-")[2]);
                        Log.i("schedule", "strendday1--> " + strendday1);

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
                                        end_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, strenddate), datepattern));
                                    }
//                                end_date1.setText(strenddate);
                                /*String endmin = values3[minutePicker.getValue()];
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
                                String strremdate = day1.format(date);*/
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date ddate = null;
                                    try {
                                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                                            ddate = dateFormat.parse(start_date1.getText().toString());
                                        } else {
                                            ddate = dateFormat.parse(Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern)));
                                        }
//                                    ddate = dateFormat.parse(start_date1.getText().toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Calendar cl = Calendar.getInstance();
                                    if (ddate != null && (isRemainderRequired != null && !isRemainderRequired.equalsIgnoreCase("N"))) {
                                        cl.setTime(ddate);
                                        cl.add(Calendar.MINUTE, 1);
                                        reminder_date = dateFormat.format(cl.getTime());
                                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                                            reminder_date1.setText(Appreference.ChangeDevicePattern(true, reminder_date, datepattern));
                                        } else {
                                            reminder_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, reminder_date), datepattern));
                                        }
//                                    reminder_date1.setText(reminder_date);
                                    } else {
                                        reminder_date1.setText("N/A");
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
                                /*String endmin = values3[minutePicker.getValue()];
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
                                String strremdate = day1.format(date);*/
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
                                    }
                                    Calendar cl = Calendar.getInstance();
                                    if (ddate != null && (isRemainderRequired != null && !isRemainderRequired.equalsIgnoreCase("N"))) {
                                        cl.setTime(ddate);
                                        cl.add(Calendar.MINUTE, 1);
                                        reminder_date = dateFormat.format(cl.getTime());
                                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                                            reminder_date1.setText(Appreference.ChangeDevicePattern(true, reminder_date, datepattern));
                                        } else {
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
                        String startampm = "";
                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                            startrem = start_date1.getText().toString();
                            startampm = checkAmorPmfunction(Appreference.ChangeOriginalPattern(true, startrem, datepattern));
                        } else {
                            startrem = Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern));
                            startampm = checkAmorPmfunction(Appreference.setDateTime(false, startrem));
                        }
                        //                        String startrem = start_date1.getText().toString();
                        Log.i("TaskTakerDateRequest", "startrem 2 value is" + startrem);
                        // am/pm
                        String startrem1 = startrem.split(":")[0];  // yyyy-mm-dd hh
                        fromtime = startrem1.split(" ")[1] + ":" + startrem.split(":")[1]; // hh:mm
                        Log.i("starttime", "starttime " + fromtime);
                        String endrem = "";
                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                            endrem = Appreference.ChangeOriginalPattern(true, end_date1.getText().toString(), datepattern);
                        } else {
                            endrem = Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, end_date1.getText().toString(), datepattern));
                        }
//                        String endrem = end_date1.getText().toString();
                        Log.i("TaskTakerDateRequest", "endrem 1 value is" + endrem);
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
                            strdate = start_date1.getText().toString();
                        } else {
                            strdate = Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, start_date1.getText().toString(), datepattern));
                        }
//                        String strdate = start_date1.getText().toString();
                        String strfromdate1 = strdate.split(" ")[0];
                        Log.i("schedule", "strfromdate1-->" + strfromdate1);

                        String strdate1 = "";
                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                            strdate1 = Appreference.ChangeOriginalPattern(true, end_date1.getText().toString(), datepattern);
                        } else {
                            strdate1 = Appreference.setDateTime(true, Appreference.ChangeOriginalPattern(false, end_date1.getText().toString(), datepattern));
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
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            selctedDateformate = new SimpleDateFormat("MMM dd yyyy HH : mm");
                            try {
                                date = selctedDateformate.parse(toas);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        SimpleDateFormat day1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String strremindate = day1.format(date);
                        String strremindate1 = strremindate.split(" ")[0];
                        Log.i("schedule", "strremindate1-->" + strremindate1);
                        Log.i("TaskTakerDateRequest", "strreminddate is" + strremindate);
                        String remampm = checkAmorPmfunction(strremindate);   // am/pm
                        Log.i("Remhrmin", "enTime" + enTime);

                        Log.i("Time", "fromtime" + fromtime);
                        Log.i("Time", "endTime" + endTime);
                        Log.i("Time", "pickertime" + enTime);
                        Log.i("TaskTakerDateRequest", "strfromdate1 is" + strfromdate1);
                        Log.i("TaskTakerDateRequest", "strremindate1 is" + strremindate1);
                        Log.i("TaskTakerDateRequest", "strenddate1 is" + strenddate1);
                        String strfromdate2 = "";
                        String strenddate2 = "";
                        if (android.text.format.DateFormat.is24HourFormat(context)) {
                            strfromdate2 = Appreference.Changeoriginaldateonly(true, strfromdate1, datepattern);
                            strenddate2 = strenddate1;
                        } else {
                            strfromdate2 = strfromdate1;
                            strenddate2 = strenddate1;
                        }
//                        if (strdate[0].equalsIgnoreCase(toas.split(" ")[0]) && endingdate >= startingdate) {
                        if ((strfromdate2.compareTo(strremindate1) <= 0) && (strenddate2.compareTo(strremindate1) >= 0)) {
//                            int errer = strfromdate1.compareTo(strenddate1);
//                            Log.i("errer", "errer" + errer);
                            if (strfromdate2.compareTo(strenddate2) > 0) {
                                if (strremindate1.equals(strenddate2)) {
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
                                } else if (!strremindate1.equals(strenddate2)) {
                                    if (strfromdate2.equals(strenddate2)) {
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

                            } else if (strfromdate2.compareTo(strenddate2) == 0) {
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
                            } else if (strfromdate2.compareTo(strenddate2) < 0) {
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
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please select appropriate time", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            if (flag) {
//                                reminder_date1.setText(strremindate);
                                if (android.text.format.DateFormat.is24HourFormat(context)) {
                                    reminder_date1.setText(Appreference.ChangeDevicePattern(true, strremindate, datepattern));
                                } else {
                                    reminder_date1.setText(Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, strremindate), datepattern));
                                }
                                enDate = values1[datePicker.getValue()];

                                end_date = dateposition;
                                end_time = hourPicker.getValue();
                                end_minute = minutePicker.getValue();
//                                end_am_pm = am_pmPicker.getValue();
                                endTimeSet = true;
                                flag = false;
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Kindly select between start and end date", Toast.LENGTH_LONG).show();
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

    private String checkAmorPmfunction(String amorpm) {
        DateFormat selctedDateformate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        Log.i("TaskTakerDateRequest", "Check AmorPmFunction");
        Log.i("TaskTakerDateRequest", "datepattern value is" + datepattern);
        Log.i("TaskTakerDateRequest", "amorpm value is" + amorpm);
        try {
            // String sans=Appreference.ChangeOriginalPattern(true,amorpm,datepattern);
            //\\og.i("TaskTakerDateRequest","sans value is"+sans);
            date = selctedDateformate.parse(amorpm);

            Log.i("TaskTakerDateRequest", "date value is" + selctedDateformate.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        SimpleDateFormat simpledateform=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String datesample=simpledateform.format(date);
        SimpleDateFormat day1 = new SimpleDateFormat("hh:mm a");
        String ammorpmm = day1.format(date);
        ammorpmm = ammorpmm.split(" ")[1];
        return ammorpmm;
    }

    private void showToast(String msg) {
        Toast.makeText(TaskTakerDateRequest.this, msg, Toast.LENGTH_SHORT)
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
        }

        return dates.toArray(new String[dates.size() - 1]);
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
            Log.i("SCHEDULECALL", "am_pm===>" + am_pm);
            Log.i("SCHEDULECALL", "amPM===>" + amPM);
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
                } else if (ampm.equalsIgnoreCase("pm") && am_pm.equalsIgnoreCase("pm")) {
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


    public String getRealPathFromURI(Uri contentUri) {
        try {
            Log.i("profile", "===> inside getRealPathFromURI");
            String[] proj = {MediaStore.Images.Media.DATA};
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

    public Bitmap convertpathToBitmap(String strIPath) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile((compressImage(strIPath)), bmOptions);
        return bitmap;
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
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
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
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
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
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public void imgfun(String getMediaType, String getMediaPath, String getExt) {
        File imageFile = new File(strIPath);

        if (imageFile.exists()) {
            Log.i("imagefile", "image");
            test = "image";
                      /*  MediaListBean uIbean = new MediaListBean();
                        uIbean.setMediaType("image");
                        uIbean.setMediaPath(strIPath);*/


            TaskDetailsBean taskBean = new TaskDetailsBean();
            Log.e("update", "values--->" + taskBean.getTaskDescription());
            taskBean.setMimeType(getMediaType);
            taskBean.setTaskDescription(getMediaPath);
            Log.e("update", "values--->" + taskBean.getTaskDescription());
            mediaList.add(taskBean);
            medialistadapter.notifyDataSetChanged();
        }
    }

    public void makeCall(boolean audiocall) {
        String userName = username;
        String buddy_uri = "sip:" + userName + "@" + getResources().getString(R.string.server_ip);
        MyCall call = new MyCall(MainActivity.account, -1);
        CallOpParam prm = new CallOpParam(true);

        CallSetting opt = prm.getOpt();
        opt.setAudioCount(1);
        if (audiocall) {
            opt.setVideoCount(0);
            MainActivity.isAudioCall = true;
        } else {
            opt.setVideoCount(1);
            MainActivity.isAudioCall = false;
        }
        Log.i("SipVideo", "make call : " + buddy_uri);
        try {
            call.makeCall(buddy_uri, prm);

//                    OnCallSdpCreatedParam param = new OnCallSdpCreatedParam();
//                    SdpSession pot = param.getSdp();
//                    call.onCallSdpCreated(param);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("SipVideo", " delete: b23");
            call.delete();
            return;
        }
        MainActivity.currentCallArrayList.add(call);
//        currentCall = call;
        MainActivity.currentCall = call;
        Log.i("SipVideo", "ContactFragment Make Call MainActivity.currentCall");
        if (MainActivity.currentCallArrayList.size() > 0) {
            showCallActivity();
        }
    }

    private void showCallActivity() {
        Log.i("SipVideo", "showCallActivity method");
        Intent intent = new Intent(context, CallActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String par_name = Appreference.loginuserdetails.getFirstName() + " " + Appreference.loginuserdetails.getLastName();
        intent.putExtra("host", par_name);
        startActivity(intent);
    }


}
