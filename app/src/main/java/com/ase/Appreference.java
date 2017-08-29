package com.ase;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.gson.Gson;
import com.ase.Bean.TaskDetailsBean;
import com.ase.RandomNumber.Logger;

import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.app.MainActivity;

import java.io.File;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import json.CommunicationBean;
import json.JsonOfflineRequestSender;
import json.JsonRequestResponce;
import json.JsonRequestSender;
import json.Loginuserdetails;

/**
 * Created by dinesh on 4/30/2016.
 */
public class Appreference {

    public static final int DEFAULT_ANIMATION_DURATION = 750;
    public static Context mainContect = null;
    public static MainActivity main_Activity_context;
    public static String template_page = null;
    public static HashMap<String, Object> context_table = new HashMap<>();
    public static JsonRequestSender jsonRequestSender = null;
    public static JsonOfflineRequestSender jsonOfflineRequestSender = null;
    public static JsonRequestResponce jsonRequestResponce = null;
    public static Loginuserdetails loginuserdetails = null;
    public static Gson gson = null;
    public static boolean is_swipe = false;
    public static boolean is_Priority = false;
    public static boolean is_chat = false;
    public static CommunicationBean communicationBean = null;
    public static boolean webview_refresh = false;
    public static boolean isAudio_webservice = false;
    public static boolean taskId_webservice = false;
    public static boolean is_alarmStop = false;
    public static boolean is_reload = false;
    public static String fillter = null;
    public static boolean conflicttask = false;
    //For ASE
    public static boolean isPauseStartFrom=false;
    public static HashMap<String,String> old_status=new HashMap<>();
    public static boolean isremarksEntered=false;
    public static boolean isTimeUpshown=false;
    public static String HoldOrPauseTimervalue=" ";

    public static boolean isImageSelected=false;
    public static boolean isLocation = false;
    public static HashMap<String, Boolean> isRem_Enable_details = new HashMap<>();
    public static HashMap<String, Boolean> isOverdue = new HashMap<>();
    public static ArrayList<String> profilePictures = new ArrayList<String>();
    public static ArrayList<String> groupprofilePictures = new ArrayList<String>();
    public static HashMap<String, String> profile_image = new HashMap<>();
    public static HashMap<String, ContactBean> buddy_details = new HashMap<>();
    public static ArrayList<TaskDetailsBean> createdFormsList = new ArrayList<>();

    public static boolean play_call_dial_tone = false;

    public static boolean global_blog_filter;
    public static boolean open_task_filter;
    public static boolean sipRegister_flag = false;
    public static boolean isRequested_date = false;
    public static boolean new_task_page_filter = true;
    public static boolean isConflict = false;
    public static boolean isonlyLeaveApprove = false;
    public static boolean isconflicttaker = false;
    public static boolean isResponse_multifile = false;
    public static boolean isLeaveConflict = false;
    public static boolean isAssignLeave = false;
    public static boolean isProject = false;
    public static boolean isSyncDone = false;
    //    public static boolean isSelectforTemplate = false;
    public static String demo = "add";

    public static String currentPresenceStateIs = "Online";

    public static String conference_uri = null;
    public static String callStart_time = null;
    public static double latitude = 0.0;
    public static double longitude = 0.0;

    public static String webid = null;

    public static boolean isWriteInFile = true;
    public static boolean isAutoredialEnabled = false;
    public static Logger logger;
    public static Typeface normal_type;
    public static boolean isFCMRegister = false;
    public static String fcmTokenId = null;
    public static HashMap<String, TaskDetailsBean> taskMultimediaDownload = new HashMap<>();
    public static boolean broadcast_call = false;
    public static boolean received_broadcastcall = false;
    public static String loginuser_status = null;
    public static boolean networkState = false;
    public static boolean sipRegistrationState = false;
    public static boolean isAlreadyCalled = false;
    public static boolean isPN = false;
    //    public static boolean isTemplateAccept = false;
    public static boolean changehost_request_received = false;
    public static boolean call_pause_received = false;
    public static String new_host = "";
    public static String call_session_id = "";
    public static HashMap<Long, Call> call_swigCPtr = new HashMap<Long, Call>();
    public static boolean signout_pressed = false, temconvert = false;
    public static boolean project_taskConversationEntry = false;
    public static String reassign_users_scheduled = null;
    public static boolean isEscalate_Observer_WS = false;
    public static String isAlfhaOrOnline = "";
    public static Hashtable<String, String> hashtable_online_alfha = new Hashtable<String, String>();
    public static HashMap<String, String> contact_arrange = new HashMap(hashtable_online_alfha);
    public static Iterator<String> keySetIterator = contact_arrange.keySet().iterator();
    public static boolean isTemplateTaskProfile = false;

    public static void printLog(String tag, String message, String type,
                                Throwable e) {
        try {
            message = tag+" : "+message;
            if (Appreference.isWriteInFile) {
                if (e == null) {
                    getLogger().info(message);
                } else {
                    getLogger().error(message, e);
                }
            } else {
                if (e == null) {
                    if (type != null) {
                        if (type.equalsIgnoreCase("DEBUG")) {
                            Log.d(tag, message);
                        } else if (type.equalsIgnoreCase("WARN")) {
                            Log.w(tag, message);
                        } else {
                            Log.i(tag, message);
                        }
                    } else
                        Log.i(tag, message);
                } else {
                    e.printStackTrace();
                }
            }
        } catch (Exception e2) {
            if (e != null)
                e.printStackTrace();
            e2.printStackTrace();
        }
    }

    public static synchronized Logger getLogger() {

        if (logger == null) {
            try {
                String dir_path = Environment.getExternalStorageDirectory()
                        + "/High Message";
                File directory = new File(dir_path);
                if (!directory.exists())
                    directory.mkdirs();

                String log_path = dir_path + "/HM_log.txt";
                File logFile = new File(log_path);
                if (!logFile.exists())
                    logFile.createNewFile();

                String logfilepath = Environment.getExternalStorageDirectory()
                        + "/High Message/HM_log.txt";
                logger = new Logger("File", logfilepath, true, true, true,
                        true, true);
                Appreference.logger = logger;

                directory = null;
                dir_path = null;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return logger;
    }


    public static String getEndOfDay(String date) {
        String enddate = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            enddate = sdf.format(new Date());
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);
            calendar.set(year, month, day, 23, 55, 00);
            enddate = calendar.getTime().toString();
//            enddate = sdf.format(calendar.getTime());
            Log.i("Appreference", "End_Date " + enddate);
        } catch (Exception e) {

        }
        return enddate;
    }


    public static String getDeviceTime24or12(Context context, String datetime) {
        String datetime_change = null;
        try {
            if (context != null && datetime != null) {
                SimpleDateFormat format24 = null;
                SimpleDateFormat format12 = null;
                Date date = null;
                format24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (DateFormat.is24HourFormat(context)) {
                    date = format24.parse(datetime);
                    datetime_change = format24.format(date);
                } else {
                    format12 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                    date = format24.parse(datetime);
                    datetime_change = format12.format(date);
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("Appref getdevicetime", "reminder_date 24-12 time " + datetime_change);
        return datetime_change;
    }


    public static String setDateTime(Boolean context, String datetime) {
        Log.i("Appref setDatetime", "datetime value is" + datetime);
        Log.i("Appref setDatetime", "context value is" + context);

        String datetime_change = null;
        try {
            SimpleDateFormat format24 = null;
            SimpleDateFormat format12 = null;
            if (context == true) {
                Date date = null;
                format12 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                format24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = format12.parse(datetime);
                datetime_change = format24.format(date);
                Log.i("Appreference", "reminder_date 0111 inside if " + datetime_change);
            } else {
                Date date = null;
                format12 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                format24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = format24.parse(datetime);
                datetime_change = format12.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("Appref setdatetime", "reminder_date 0111 " + datetime_change);
        return datetime_change;
    }

    public static String setDateTimepattern(Boolean context, String datetime, String datepat) {
        Log.i("Appref setDatetime", "datetime value is" + datetime);
        Log.i("Appref setDatetime", "context value is" + context);

        String datetime_change = null;
        try {
            SimpleDateFormat format24 = null;
            SimpleDateFormat format12 = null;
            if (context == true) {
                Date date = null;
                format12 = new SimpleDateFormat(datepat + " hh:mm:ss a");
                format24 = new SimpleDateFormat(datepat + " HH:mm:ss");
                date = format12.parse(datetime);
                datetime_change = format24.format(date);
                Log.i("Appreference", "reminder_date 0111 inside if " + datetime_change);
            } else {
                Date date = null;
                format12 = new SimpleDateFormat(datepat + " hh:mm:ss a");
                format24 = new SimpleDateFormat(datepat + " HH:mm:ss");
                date = format24.parse(datetime);
                datetime_change = format12.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("Appref setdatetime", "reminder_date 0111 " + datetime_change);
        return datetime_change;
    }


    public static String getTimeFormat(Boolean context, String datetime, String devicedatepattern) {
        Log.i("Appref getTimeformat", "datetime value is" + datetime);
        Log.i("Appref getTimeFormat", "context value is" + context);

        String datetime_change = null;
        try {
            SimpleDateFormat format24 = null;
            SimpleDateFormat format12 = null;
            if (context == true) {
                Date date = null;
                format12 = new SimpleDateFormat(devicedatepattern + " hh:mm:ss a");
                format24 = new SimpleDateFormat(devicedatepattern + " HH:mm:ss");
                date = format12.parse(datetime);
                datetime_change = format24.format(date);
                Log.i("Appref getTimeFormat", "reminder_date 0111 inside if " + datetime_change);
            } else {
                Date date = null;
                format12 = new SimpleDateFormat(devicedatepattern + " hh:mm:ss a");
                format24 = new SimpleDateFormat(devicedatepattern + " HH:mm:ss");
                date = format24.parse(datetime);
                datetime_change = format12.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("Appref getTimeFormat", "reminder_date 0111 " + datetime_change);
        return datetime_change;
    }

    public static String Changeoriginaldateonly(Boolean context, String datetime, String devicedatepattern) {
        Log.i("Appref ChgeOrglPattern", "datetime value is" + datetime);
        Log.i("Appref ChgeOrglPattern", "context value is" + context);
//        Log.i("Appref ChgeOrglPattern","datepattern value is"+datepattern);

        String datetime_change = null;
        try {
            SimpleDateFormat device_pattern = null;
            SimpleDateFormat original_pattern = null;
            if (context == true) {
                Date date = null;
                device_pattern = new SimpleDateFormat(devicedatepattern);
                original_pattern = new SimpleDateFormat("yyyy-MM-dd");
                date = device_pattern.parse(datetime);
                datetime_change = original_pattern.format(date);
                Log.i("Appref ChgeOrglPattern", "reminder_date 0111 inside if " + datetime_change);
            } else {
                Date date = null;
                original_pattern = new SimpleDateFormat("yyyy-MM-dd");
                device_pattern = new SimpleDateFormat(devicedatepattern);
                date = original_pattern.parse(datetime);
                datetime_change = device_pattern.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("Appref ChgeOrglPattern", "reminder_date 0111 " + datetime_change);
        return datetime_change;
    }

    public static String ChangeOriginalPattern(Boolean context, String datetime, String devicepat) {
        Log.i("Appref ChgeOrglPattern", "datetime value is" + datetime);
        String datetime_change = null;
        try {
            SimpleDateFormat device_pattern = null;
            SimpleDateFormat original_pattern = null;
            if (context == true) {
                Date date = null;
                device_pattern = new SimpleDateFormat(devicepat + " HH:mm:ss");
                original_pattern = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = device_pattern.parse(datetime);
                datetime_change = original_pattern.format(date);
                Log.i("Appref ChgeOrglPattern", "reminder_date 0111 inside if " + datetime_change);
            } else {
                Date date = null;
                original_pattern = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                device_pattern = new SimpleDateFormat(devicepat + " hh:mm:ss a");
                date = device_pattern.parse(datetime);
                datetime_change = original_pattern.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("Appref ChgeOrglPattern", "reminder_date 0111 " + datetime_change);
        return datetime_change;
    }

    public static String ChangeDevicePattern(Boolean context, String datetime, String devicepat) {
        Log.i("Appref ChgeOrglPattern", "datetime value is" + datetime);
        String datetime_change = null;
        try {
            SimpleDateFormat device_pattern = null;
            SimpleDateFormat original_pattern = null;
            if (context == true) {
                Date date = null;
                device_pattern = new SimpleDateFormat(devicepat + " HH:mm:ss");
                original_pattern = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date = original_pattern.parse(datetime);
                datetime_change = device_pattern.format(date);
                Log.i("Appref ChgeOrglPattern", "reminder_date 0111 inside if " + datetime_change);
            } else {
                Date date = null;
                original_pattern = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                device_pattern = new SimpleDateFormat(devicepat + " hh:mm:ss a");
                date = original_pattern.parse(datetime);
                datetime_change = device_pattern.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("Appref ChgeOrglPattern", "reminder_date 0111 " + datetime_change);
        return datetime_change;
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


    public static String setTaskTime(Boolean context, String datetime) {
        String datetime_change = null;
        try {
            SimpleDateFormat format24 = null;
            SimpleDateFormat format12 = null;
            if (context) {
                Date date = null;
                format12 = new SimpleDateFormat("hh:mm:ss a");
                format24 = new SimpleDateFormat("HH:mm:ss");
                date = format12.parse(datetime);
                datetime_change = format24.format(date);
            } else {
                Date date = null;
                format12 = new SimpleDateFormat("hh:mm:ss a");
                format24 = new SimpleDateFormat("HH:mm:ss");
                date = format24.parse(datetime);
                datetime_change = format12.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("Appref setTasktime", "reminder_date 0111 " + datetime_change);
        return datetime_change;
    }

    public static String getDeviceTime12(Context context, String datetime) {
        String datetime_change = null;
        try {
            if (context != null && datetime != null) {
                SimpleDateFormat format24 = null;
                SimpleDateFormat format12 = null;
                Date date = null;
                format24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                format12 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                date = format24.parse(datetime);
                date = format12.parse(datetime);
                datetime_change = format12.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("Appref getdevicetime", "reminder_date 0111 " + datetime_change);
        return datetime_change;
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getIMEINumber(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String device_id = tm.getDeviceId();

        return device_id;
    }

    public static String getMANUFACTURER(Context context) {

        String manufacturer = Build.MANUFACTURER;
        return manufacturer;
    }

    public static String getMODEL(Context context) {

        String model = Build.MODEL;
        return model;
    }


    public static String getRELEASE(Context context) {

        String versionRelease = Build.VERSION.RELEASE;
        return versionRelease;
    }

    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * UTC time to local time conversion
     */
    public static String utcToLocalTime(String time) {
        String formattedDate = null;
        Date myDate = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            TimeZone utcZone = TimeZone.getTimeZone("UTC");
            simpleDateFormat.setTimeZone(utcZone);
            try {
                if (time != null && !time.equalsIgnoreCase("(null)") && !time.equalsIgnoreCase("null") && !time.equalsIgnoreCase("") && !time.equalsIgnoreCase(null)) {
                    myDate = simpleDateFormat.parse(time);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            simpleDateFormat.setTimeZone(TimeZone.getDefault());
            if (myDate != null) {
                formattedDate = simpleDateFormat.format(myDate);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return formattedDate;
    }

    /**
     * @param date Pass the local date filed
     * @return current UTC time in String.
     */
    public static String localDatetoUTC(Date date) {

        String dateforrow = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            dateforrow = dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateforrow;

    }

    /**
     * @param date pass custom date in String
     * @return UTC time in String.
     */

    public static String customLocalDateToUTC(String date) {

        String resultDate = null;
        try {
            SimpleDateFormat utcDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
            utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            if (date != null)
                resultDate = utcDateFormat.format(sdf3.parse(sdf3.format(dateFormat.parse(date))));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return resultDate;
    }


    /**
     * @return random fileName in String
     */
    public static String getFileName() {
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

    public static String getDeviceName() {
        String reqString = Build.MANUFACTURER
                + " " + Build.MODEL;
        return reqString;
    }

    public static String TimeFrequencyConvertion(String timefrequency) {
        String timemillis = timefrequency;
        long milliseconds = Long.parseLong(timefrequency);
        long minutes = ((milliseconds / (1000 * 60)) % 60);
        long hours = ((milliseconds / (1000 * 60 * 60)) % 24);
        long days = ((milliseconds / (1000 * 60 * 60 * 24)) % 7);
        Log.i("Appreference", "TimeFrequencyConvertion :" + " minutes : " + minutes);
        Log.i("Appreference", "TimeFrequencyConvertion :" + " hours : " + hours);
        Log.i("Appreference", "TimeFrequencyConvertion :" + " days : " + days);
        long day = TimeUnit.MILLISECONDS.toDays(milliseconds);
        long hour = TimeUnit.MILLISECONDS.toHours(milliseconds);
        long min = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        Log.i("Appreference", "TimeFrequencyConvertion : *" + " minute : " + min);
        Log.i("Appreference", "TimeFrequencyConvertion : *" + " hour : " + hour);
        Log.i("Appreference", "TimeFrequencyConvertion : *" + " day : " + day);
//        if (day > 0 && hour <= 0 && min <= 0) {
//            timemillis = "" + day + " Days";
//        }
//        if (day <= 0 && hour > 0 && min <= 0) {
//            timemillis = "" + hour + " Hours";
//        }
//        if (day <= 0 && hour <= 0 && min > 0) {
//            timemillis = "" + min + " Minutes";
//        }
        if (day > 0) {
            timemillis = "" + day + " Days";
        } else if (hour > 0) {
            timemillis = "" + hour + " Hours";
        } else if (min > 0) {
            timemillis = "" + min + " Minutes";
        }
        return timemillis;
    }

}
