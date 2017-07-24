package com.ase.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.ase.AppSharedpreferences;
import com.ase.Appreference;
import com.ase.Bean.From;
import com.ase.Bean.Group;
import com.ase.Bean.ListAllTaskBean;
import com.ase.Bean.ListMembers;
import com.ase.Bean.ListObserver;
import com.ase.Bean.ListTaskFile;
import com.ase.Bean.ListTaskTransaction;
import com.ase.Bean.ListallProjectBean;
import com.ase.Bean.ListofFileds;
import com.ase.Bean.Project;
import com.ase.Bean.ProjectDetailsBean;
import com.ase.Bean.ProjectOrganisationBean;
import com.ase.Bean.Task;
import com.ase.Bean.TaskDetailsBean;
import com.ase.Bean.ToUser;
import com.ase.Bean.User;
import com.ase.ContactBean;
import com.ase.Forms.FormAccessBean;
import com.ase.ListAllgetTaskDetails;
import com.ase.ListTaskFiles;
import com.ase.call_list.Call_ListBean;
import com.ase.chat.ChatBean;

import org.pjsip.pjsua2.app.GroupMemberAccess;
import org.pjsip.pjsua2.app.MainActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

import json.GroubDetails;
import json.ListFromDetails;
import json.ListMember;
import json.ListTaskConversation;
import json.ListTaskConversationFiles;
import json.ListUserGroupObject;
import json.Loginuserdetails;

/**
 * Created by dinesh on 5/2/2016.
 */
public class VideoCallDataBase extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    /**
     * DB Inside COMMedia Folder //
     */
    public static final String DATABASE_NAME = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/High Message/sipvideocall.db";
    /**
     * DB inside application
     */
    /*public static final String DATABASE_NAME = Appreference.main_Activity_context
	 .getResources().getString(R.string.app_name) + ".db";
*/
    Context context;
    public static final String CREATE_TABLE_CHAT = "create table if not exists chat(autoincrement_id integer primary key autoincrement,chattype varchar(100),chatname varchar(100),chatid varchar(100),username varchar(100),fromname varchar(100),toname varchar(100),signalid varchar(100),messagetype varchar(100),message varchar(100),datetime varchar(100),msgstatus varchar(100),imagepath varchar(100),userid varchar(100),opened varchar(50),coordinator varchar(100),chatmembers varchar(500),conferenceuri varchar(100),scheduled varchar(50))";
    public static final String CREATE_TABLE_CONTACTS = "create table if not exists contact(autoincrement_id integer primary key autoincrement,contactemail varchar(100),userid interger,username varchar(100),firstname varchar(100),lastname varchar(100),code varchar(100),title varchar(100),gender varchar(100),profileImage varchar(100),personalInfo varchar(100),loginuser varchar(100),presence varchar(50),job1 varchar(100),job2 varchar(100),job3 varchar(100),job4 varchar(100),textprofile varchar(100),videoprofile varchar(100),userType varchar(100),profession varchar(100),specialization varchar(100),organization varchar(100),roleId varchar(100),roleName varchar(100))";
    public static final String CREATE_TABLE_GROUPS = "create table if not exists group1(groupid integer,groupname varchar(100),groupowner varchar(100),departmentid varchar(100),description varchar(100),logo varchar(100),grouplogo varchar(100),departmentref varchar(100),loginuser varchar(100))";
    public static final String CREATE_TABLE_GROUPMEMBERS = "create table if not exists groupmember(userid integer,email varchar(100),username varchar(100),code varchar(100),departmentId varchar(100),userStatus varchar(100),loginStatus varchar(100),firstName varchar(100),lastName varchar(100),title varchar(100),organization varchar(100),gender varchar(100),profileImage varcahr(100),mobileNo varchar(100),loginuser varchar(100),groupid varchar(100),userType varchar(100),profession varchar(100),specialization varchar(100))";
    public static final String CREATE_TABLE_CALLS = "create table if not exists call(callid INTEGER PRIMARY KEY AUTOINCREMENT,calltype varchar(100),host varchar(100),participant varchar(100),start_time varchar(100),call_duration varchar(100),recording_path varchar(100),loginuser varchar(100))";
    public static final String CREATE_TABLE_TASK = "create table if not exists taskDetailsInfo(id integer primary key autoincrement,loginuser varchar(100),fromUserId integer,toUserId integer,plannedStartDateTime varchar(100),plannedEndDateTime varchar(100),remainderFrequency varchar(100),duration varchar(100),durationunit varchar(100),taskDescription TEXT,isRemainderRequired varchar(100),taskStatus varchar(100),signalid varchar(100),fromUserName varchar(100),toUserName varchar(100),sendStatus varchar(100),completedPercentage varchar(100),taskType varchar(100),mimeType varchar(100),taskPriority varchar(100),dateFrequency varchar(100),timeFrequency varchar(100),taskNo varchar(100),taskId varchar(100),msgstatus varchar(5),showprogress varchar(5),readStatus varchar(5),reminderquotes varchar(100),remark varchar(100),tasktime varchar(100),serverFileName varchar(100),requestStatus varchar(100),dateTime varchar(100),subType varchar(20),daysOfTheWeek varchar(100),repeatFrequency varchar(5),taskTagName varchar(20),customTagId varchar(20),customTagVisible varchar(20),customSetId varchar(20),syncEnable varchar(100),wssendstatus varchar(20),projectId varchar(100),parentTaskId varchar(100),longmessage varchar(100),private_member varchar(200),replyMessage varchar(100),taskPlannedBeforeEndDate varchar(100),taskPlannedLatestEndDate varchar(100),fromTaskName varchar(100),toTaskName varchar(100),sender_reply varchar(100),reply_sendername varchar(100))";
    public static final String CREATE_TABLE_TASK_HISTORY = "create table if not exists taskHistoryInfo(id integer primary key autoincrement,loginuser varchar(100),taskNo varchar(100),taskName varchar(100),plannedStartDateTime varchar(100),plannedEndDateTime varchar(100),remainderFrequency varchar(100),taskDescription TEXT,taskObservers TEXT,taskStatus varchar(100),signalid varchar(100),completedPercentage varchar(100),taskType varchar(100),ownerOfTask varchar(100),mimeType varchar(100),parentTaskId varchar(100),taskId varchar(100),readStatus varchar(5),tasktime varchar(100),taskReceiver varchar(100),taskMemberList TEXT,dateTime varchar(100),badgeCount varchar(20),category vrchar(100),taskPlannedBeforeEndDate varchar(100),taskPlannedLatestEndDate varchar(100),fromTaskName varchar(100),toTaskName varchar(100),sender_reply varchar(100),reply_sendername varchar(100))";
    public static final String CREATE_TABLE_PROJECT = "create table if not exists projectDetails(id integer primary key autoincrement,loginuser varchar(100),projectId varchar(100),projectName varchar(100),projectDescription varchar(100),projectOrganisation varchar(100),projectOwner varchar(100),listOfMembers varchar(100),completedPercentage varchar(100),taskStatus varchar(100),readStatus varchar(5),requestStatus varchar(100),oracleProjectId varchar(100),customerName varchar(100),address varchar(100),mcModel varchar(100),mcSrNo varchar(100),serviceRequestDate varchar(100),chasisNo varchar(100),observation varchar(100),oracleCustomerId integer,activity varchar(100),processFlag varchar(100),projectcompletedstatus varchar(100),isActiveStatus varchar(100),jobCardType varchar(100),machineMake varchar(100))";
    public static final String CREATE_TABLE_PROJECT_HISTORY = "create table if not exists projectHistory(id integer primary key autoincrement,loginuser varchar(100),projectId varchar(100),parentTaskId varchar(100),projectOwner varchar(100),projectName varchar(100),fromUserId varchar(100),toUserId varchar(100),fromUserName varchar(100),toUserName varchar(100),projectDescription varchar(100),projectOrganisation varchar(100),plannedStartDateTime varchar(100),plannedEndDateTime varchar(100),taskMemberList varchar(100),taskStatus varchar(100),ownerOfTask varchar(100),taskReceiver varchar(100),taskObservers TEXT,taskNo varchar(100),taskName varchar(100),taskDescription varchar(100),taskType varchar(100),mimeType varchar(100),taskId varchar(100),signalId varchar(100),completedPercentage varchar(100),readStatus varchar(5),category varchar(10),isParentTask varchar(5),issueParentId varchar(100),requestStatus varchar(100),oracleTaskId varchar(100),estimatedTravelHrs integer,estimatedActivityHrs integer,activity varchar(100),oracleProjectId varchar(100),customerName varchar(100),address varchar(100),mcModel varchar(100),mcSrNo varchar(100),serviceRequestDate varchar(100),chasisNo varchar(100),observation varchar(100),oracleCustomerId integer,processFlag varchar(100),projectcompletedstatus varchar(100),isActiveStatus varchar(100),jobCardType varchar(100),machineMake varchar(100))";
    public static final String CREATE_TABLE_FORM_ACCESS = "create table if not exists FormAccess(id integer primary key autoincrement,taskId varchar(50),formId varchar(50),formAccessId varchar(50),taskGiver varchar(50),memberName varchar(50),accessMode varchar(50))";
    public static final String CREATE_TABLE_List_User_Group_Member_Access = "create table if not exists listUserGroupMemberAccess(userid integer,groupid integer,groupname varchar(100),loginuser varchar(100),respondVideo varchar(50),respondFiles varchar(50),accessForms varchar(50),respondAudio varchar(50),videoAccess varchar(50),adminAccess varchar(50),respondDateChange varchar(50),respondLocation varchar(50),respondConfCall varchar(50),audioAccess varchar(50),chatAccess varchar(50),respondText varchar(50),respondPrivate varchar(50),respondPhoto varchar(50),accessReminder varchar(50),respondSketch varchar(50),respondTask varchar(50),accessScheduledCNF varchar(50),GroupTask varchar(50),ReassignTask varchar(50),ChangeTaskName varchar(50),TaskDescriptions varchar(50),TemplateExistingTask varchar(50),ApproveLeave varchar(50),RemindMe varchar(50),AddObserver varchar(50),TaskPriority varchar(50),Escalations varchar(50))";
    public static final String CREATE_TABLE_PROJECT_STATUS="create table if not exists projectStatus(id integer primary key autoincrement,userId integer,projectId integer,taskId integer,taskDescription varchar(100),travelStartTime varchar(100),activityStartTime varchar(100),activityEndTime varchar(100),travelEndTime varchar(100),totravelstartdatetime varchar(100),totravelenddatetime varchar(100),remarks varchar(100),hourMeterReading varchar(100),status varchar(100),customersignaturename varchar(100),photo varchar(100),techniciansignature varchar(100),customersignature varchar(100),observation varchar(100),actionTaken varchar(100),taskcompleteddate varchar(100))";

    public static final String EULATABLE = "eulaagree";
    public static final String EULACREATE = "create table if not exists '" + EULATABLE + "'(id integer (1),selection varchar(1))";

    public static SQLiteDatabase db = null;
    private static VideoCallDataBase dbHelper = null;
    MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");

    public VideoCallDataBase(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public static VideoCallDataBase getDB(Context cntxt) {
        if (dbHelper == null) {
            dbHelper = new VideoCallDataBase(cntxt);
            dbHelper.openDatabase();
        }
        return dbHelper;
    }

    /**
     * Open writable database when database is in null.
     */
    public void openDatabase() {
        if (db == null) {

            try {
                db = dbHelper.getWritableDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Open Readable database when database is in null.
     */

    public void openReadableDatabase() {
        try {
            if (db == null)
                db = getReadableDatabase();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CHAT);
        db.execSQL(CREATE_TABLE_CONTACTS);
        db.execSQL(CREATE_TABLE_GROUPS);
        db.execSQL(CREATE_TABLE_GROUPMEMBERS);
        db.execSQL(CREATE_TABLE_CALLS);
        db.execSQL(CREATE_TABLE_TASK);
        db.execSQL(CREATE_TABLE_PROJECT);
        db.execSQL(CREATE_TABLE_PROJECT_HISTORY);
        db.execSQL(CREATE_TABLE_TASK_HISTORY);
        db.execSQL(CREATE_TABLE_FORM_ACCESS);
        db.execSQL(CREATE_TABLE_List_User_Group_Member_Access);
        db.execSQL(CREATE_TABLE_PROJECT_STATUS);
        db.execSQL(EULACREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public synchronized void close() {
        super.close();
        db.close();
        db = null;
    }


    public void insertChat_history(ChatBean bean) {
        try {
            int row_id = 0;
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            cv.put("chattype", bean.getType());
            cv.put("chatname", bean.getChatname());
            cv.put("chatid", bean.getChatid());
            cv.put("username", bean.getUsername());
            cv.put("fromname", bean.getFromUser());
            cv.put("toname", bean.getToname());
            cv.put("signalid", bean.getSignalid());
            cv.put("messagetype", bean.getMsgtype());
            cv.put("message", bean.getMessage());
            cv.put("datetime", bean.getDatetime());
            cv.put("msgstatus", bean.getMsg_status());
//            cv.put("imagepath", bean.getImagepath());
//            cv.put("userid", bean.getUserid());
            cv.put("opened", bean.getOpened());
//            cv.put("coordinator", bean.getCoordinator());
            cv.put("chatmembers", bean.getChatmembers());
            cv.put("scheduled", bean.getScheduled());
            row_id = (int) db.insert("chat", null, cv);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public ChatBean getChatBean(String str) {
        ChatBean chatBean = new ChatBean();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                Log.i("report", "Loginuser-->" + Appreference.loginuserdetails.getUsername());
                cur = db.rawQuery("select * from chat where username='" + Appreference.loginuserdetails.getUsername() + "' and signalid='" + str + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    chatBean.setType(cur.getString(cur.getColumnIndex("chattype")));
                    chatBean.setChatname(cur.getString(cur.getColumnIndex("chatname")));
                    chatBean.setChatid(cur.getString(cur.getColumnIndex("chatid")));
                    chatBean.setUsername(cur.getString(cur.getColumnIndex("username")));
                    chatBean.setFromUser(cur.getString(cur.getColumnIndex("fromname")));
                    chatBean.setToname(cur.getString(cur.getColumnIndex("toname")));
                    chatBean.setSignalid(cur.getString(cur.getColumnIndex("signalid")));
                    chatBean.setMsgtype(cur.getString(cur.getColumnIndex("messagetype")));
                    chatBean.setMessage(cur.getString(cur.getColumnIndex("message")));
                    chatBean.setDatetime(cur.getString(cur.getColumnIndex("datetime")));
                    chatBean.setMsg_status(Integer.parseInt(cur.getString(cur.getColumnIndex("msgstatus"))));
                    chatBean.setOpened(cur.getString(cur.getColumnIndex("opened")));
                    chatBean.setChatmembers(cur.getString(cur.getColumnIndex("chatmembers")));
                    chatBean.setScheduled(cur.getString(cur.getColumnIndex("scheduled")));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return chatBean;
        }
    }


    public void ChatMsg_StatusUpdate(String signalid, String date) {

        // String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "sendStatus = '" + query + "'WHERE " + "taskNo" + " = " + taskNo;

        ContentValues cv = new ContentValues();
        cv.put("opened", "0");
        cv.put("datetime", date);
        db.update("chat", cv, "signalid=?", new String[]{signalid});
        Log.d("chatdb", "ScheduleMessage DB updated");
    }

    public void ChatMsgDelete(String signalid) {

        // String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "sendStatus = '" + query + "'WHERE " + "taskNo" + " = " + taskNo;

        ContentValues cv = new ContentValues();
        cv.put("opened", "0");
        db.delete("chat", "signalid=?", new String[]{signalid});
        Log.d("chatdb", "ScheduleMessage DB deleted");
    }

    public void insertCall_history(Call_ListBean bean, String string) {
        try {
            int row_id = 0;
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
//            cv.put("callid", bean.getType());
            cv.put("calltype", bean.getType());
            cv.put("host", bean.getHost());
            cv.put("participant", bean.getParticipant());
            cv.put("start_time", bean.getStart_time());
            cv.put("call_duration", bean.getCall_duration());
            cv.put("recording_path", bean.getRecording_path());
            cv.put("loginuser", string);
            Log.i("insertcall", "History-->" + bean.getHost());
            row_id = (int) db.insert("call", null, cv);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public boolean isAgendaRecordExists(String Querry) {
        Cursor cur = null;
        boolean status = false;
        try {
            if (!db.isOpen())
                openDatabase();
            cur = db.rawQuery(Querry, null);
            cur.moveToFirst();
            if (cur.getCount() > 0)
                status = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (cur != null)
                cur.close();
            return status;
        }
    }


    public void insertContact_history(ListUserGroupObject bean, String name) {
        try {
            int row_id = 0;
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            Log.i("ContactTable", "Value--->" + row_id);
            cv.put("contactemail", bean.getEmail());
            cv.put("userid", bean.getId());
            cv.put("username", bean.getUsername());
            cv.put("firstname", bean.getFirstName());
            cv.put("lastname", bean.getLastName());
            cv.put("code", bean.getCode());
            cv.put("title", bean.getTitle());
            cv.put("gender", bean.getGender());
            cv.put("profileImage", bean.getProfileImage());
            Appreference.profilePictures.add(bean.getProfileImage());
            cv.put("personalInfo", bean.getPersonalInfo());
            cv.put("loginuser", name);
            cv.put("job1", bean.getJobCategory1());
            cv.put("job2", bean.getJobCategory2());
            cv.put("job3", bean.getJobCategory3());
            cv.put("job4", bean.getJobCategory4());
            cv.put("textprofile", bean.getTextProfile());
            cv.put("videoprofile", bean.getVideoProfile());
            cv.put("userType", bean.getUserType());
            cv.put("profession", bean.getProfession());
            cv.put("organization", bean.getOrganization());
            cv.put("specialization", bean.getSpecialization());
            if (isAgendaRecordExists("select * from contact where userid='" + bean.getId() + "'and loginuser='" + name + "'")) {
                Log.i("ContactTable", "UpdateQuery");
                Log.i("MainActivity", "insertContact_history Insert 1 ");
                row_id = (int) db.update("contact", cv, "userid='" + bean.getId() + "'and loginuser='" + name + "'", null);
            } else {
                Log.i("MainActivity", "insertContact_history Insert 2 ");
                Log.i("ContactTable", "Insert");
                row_id = (int) db.insert("contact", null, cv);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void UpdateOrInsert(ArrayList<ListofFileds> beans, String name, TaskDetailsBean bean) {
        int row_id = 0;
        String logginuser = AppSharedpreferences.getInstance(context).getString("loginUserName");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = dateFormat.format(new Date());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateforrow = dateFormat.format(new Date());

        logginuser = logginuser.replace('_', '@');
        Log.i("DB_Insert", "bean size" + beans.size());
        for (int i = 0; i < beans.size(); i++) {
            Log.i("DB_Insert", "logginuser" + logginuser);

            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            cv.put("loginuser", logginuser);
            cv.put("fromUserId", bean.getFromUserId());
            cv.put("toUserId", bean.getToUserId());
            cv.put("taskNo", bean.getTaskNo());
            cv.put("taskName", bean.getTaskName());
            cv.put("plannedStartDateTime", bean.getUtcPlannedStartDateTime());
            cv.put("plannedEndDateTime", bean.getUtcplannedEndDateTime());
            cv.put("remainderFrequency", bean.getUtcPemainderFrequency());
            cv.put("duration", bean.getDuration());
            cv.put("durationunit", bean.getDurationUnit());
            Log.i("task", "taskdescription value update " + beans.get(i).getName());
            cv.put("taskDescription", beans.get(i).getName());
            cv.put("isRemainderRequired", bean.getIsRemainderRequired());
            Log.i("Accept", "value UpdateOrInsert taskDetailsBean.getTaskStatus() " + bean.getTaskStatus());
            cv.put("taskStatus", bean.getTaskStatus());
            cv.put("signalid", bean.getSignalid());
            cv.put("fromUserName", bean.getFromUserName());
            cv.put("toUserName", bean.getToUserName());
            cv.put("sendStatus", bean.getSendStatus());
            cv.put("completedPercentage", bean.getCompletedPercentage());
            cv.put("taskType", bean.getTaskType());
            cv.put("ownerOfTask", bean.getOwnerOfTask());
            if (beans.get(i).getDataType().equalsIgnoreCase("datetime") || beans.get(i).getDataType().equalsIgnoreCase("numeric")) {

                cv.put("mimeType", "text");
            } else if (beans.get(i).getDataType().equalsIgnoreCase("photo") || beans.get(i).getDataType().equalsIgnoreCase("image")) {
                cv.put("mimeType", "image");

            } else if (beans.get(i).getDataType().equalsIgnoreCase("video")) {

            } else {

                cv.put("mimeType", "text");
            }
            cv.put("taskPriority", bean.getTaskPriority());
            cv.put("dateFrequency", bean.getDateFrequency());
            cv.put("timeFrequency", bean.getTimeFrequency());
            cv.put("taskId", bean.getTaskId());
            cv.put("showprogress", String.valueOf(bean.getShow_progress()));
            cv.put("readStatus", bean.getRead_status());
            cv.put("reminderquotes", bean.getReminderQuote());
            cv.put("remark", bean.getRemark());
            cv.put("tasktime", bean.getTaskUTCTime());
            cv.put("taskReceiver", bean.getTaskReceiver());
            cv.put("taskObservers", bean.getTaskObservers());
            Log.i("time", "value");
            cv.put("serverFileName", bean.getServerFileName());
            cv.put("tasktime", bean.getTaskUTCTime());
            cv.put("msgstatus", bean.getMsg_status());
            cv.put("requestStatus", bean.getRequestStatus());
            cv.put("taskMemberList", bean.getGroupTaskMembers());
            cv.put("dateTime", bean.getTaskUTCDateTime());
            cv.put("subType", bean.getSubType());
            cv.put("daysOfTheWeek", bean.getDaysOfTheWeek());
            cv.put("repeatFrequency", bean.getRepeatFrequency());
            cv.put("taskTagName", beans.get(i).getTask());
            cv.put("customTagId", beans.get(i).getId());
            cv.put("customTagVisible", bean.isCustomTagVisible());
            cv.put("customSetId", name);
            Log.i("CustomTag", "Updatequrey" + "select * from taskDetailsInfo where customSetId='" + name + "'and customTagId='" + beans.get(i).getId() + "'");
            if (isAgendaRecordExists("select * from taskDetailsInfo where customSetId='" + name + "'and customTagId='" + beans.get(i).getId() + "'")) {
                Log.i("ContactTable", "UpdateQuery");
                try {
                    Log.i("CustomTag", "Updatequrey" + "update taskDetailsInfo set taskDescription = '" + beans.get(i).getName() + "' where customSetId='" + name + "'and customTagId='" + beans.get(i).getId() + "'");
//                    String s = "update taskDetailsInfo set (taskDescription = '" + beans.get(i).getName() + "' and customTagVisible = '" + bean.isCustomTagVisible() + "')  where customSetId='" + name + "' and customTagId='" + beans.get(i).getId() + "'";
//                    db.execSQL(s);
                    ContentValues cv1 = new ContentValues();
                    cv1.put("taskDescription", beans.get(i).getName());
                    cv1.put("customTagVisible", bean.isCustomTagVisible());
                    db.update("taskDetailsInfo", cv1, "customSetId=? and customTagId=?", new String[]{name, String.valueOf(beans.get(i).getId())});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.i("ContactTable", "Insert");
                row_id = (int) db.insert("taskDetailsInfo", null, cv);
            }
        }
    }

    public void UpdateOrInsert(TaskDetailsBean bean) {
        int row_id = 0;
        String logginuser = AppSharedpreferences.getInstance(context).getString("loginUserName");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = dateFormat.format(new Date());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String dateforrow = dateFormat.format(new Date());

        logginuser = logginuser.replace('_', '@');
//        Log.i("DB_Insert", "bean size" + beans.size());
//        for(int i=0;i<beans.size();i++) {
        Log.i("DB_Insert", "logginuser" + logginuser);

        if (!db.isOpen())
            openDatabase();
        ContentValues cv = new ContentValues();
        cv.put("loginuser", logginuser);
        cv.put("fromUserId", bean.getFromUserId());
        cv.put("toUserId", bean.getToUserId());
        cv.put("taskNo", bean.getTaskNo());
//        cv.put("taskName", bean.getTaskName());
        cv.put("plannedStartDateTime", bean.getUtcPlannedStartDateTime());
        cv.put("plannedEndDateTime", bean.getUtcplannedEndDateTime());
        cv.put("remainderFrequency", bean.getUtcPemainderFrequency());
        cv.put("duration", bean.getDuration());
        cv.put("durationunit", bean.getDurationUnit());
        Log.i("task", "taskdescription value update " + bean.getTaskDescription());
        cv.put("taskDescription", bean.getTaskDescription());
        cv.put("isRemainderRequired", bean.getIsRemainderRequired());
        Log.i("Accept", "value UpdateOrInsert taskDetailsBean.getTaskStatus() " + bean.getTaskStatus());
        cv.put("taskStatus", bean.getTaskStatus());
        cv.put("signalid", bean.getSignalid());
        cv.put("fromUserName", bean.getFromUserName());
        cv.put("toUserName", bean.getToUserName());
        cv.put("sendStatus", bean.getSendStatus());
        cv.put("completedPercentage", bean.getCompletedPercentage());
        cv.put("taskType", bean.getTaskType());
//        cv.put("ownerOfTask", bean.getOwnerOfTask());
        cv.put("mimeType", bean.getMimeType());
        cv.put("taskPriority", bean.getTaskPriority());
        cv.put("dateFrequency", bean.getDateFrequency());
        cv.put("timeFrequency", bean.getTimeFrequency());
        cv.put("taskId", bean.getTaskId());
        cv.put("showprogress", String.valueOf(bean.getShow_progress()));
        cv.put("readStatus", bean.getRead_status());
        cv.put("reminderquotes", bean.getReminderQuote());
        cv.put("remark", bean.getRemark());
        cv.put("tasktime", bean.getTaskUTCTime());
//        cv.put("taskReceiver", bean.getTaskReceiver());
//        cv.put("taskObservers", bean.getTaskObservers());
        Log.i("time", "value");
        cv.put("serverFileName", bean.getServerFileName());
        cv.put("tasktime", bean.getTaskUTCTime());
        cv.put("msgstatus", bean.getMsg_status());
        cv.put("requestStatus", bean.getRequestStatus());
//        cv.put("taskMemberList", bean.getGroupTaskMembers());
        cv.put("dateTime", bean.getTaskUTCDateTime());
        cv.put("subType", bean.getSubType());
        cv.put("daysOfTheWeek", bean.getDaysOfTheWeek());
        cv.put("repeatFrequency", bean.getRepeatFrequency());
        cv.put("taskTagName", bean.getTaskTagName());
        cv.put("customTagId", bean.getCustomTagId());
        cv.put("customTagVisible", bean.isCustomTagVisible());
        cv.put("customSetId", bean.getCustomSetId());
        cv.put("projectId", bean.getProjectId());
        Log.i("conversation", "schedulecall DB " + bean.isCustomTagVisible());
        Log.i("CustomTag", "Updatequrey" + "select * from taskDetailsInfo where customSetId='" + bean.getCustomSetId() + "'and customTagId='" + bean.getCustomTagId() + "'");
        if (isAgendaRecordExists("select * from taskDetailsInfo where customSetId='" + bean.getCustomSetId() + "'and customTagId='" + bean.getCustomTagId() + "'")) {
            Log.i("ContactTable", "UpdateQuery");
            Log.i("conversation", "schedulecall DB if " + bean.isCustomTagVisible());
//                row_id = (int) db.update("taskDetailsInfo", cv, "customSetId='" + name + "'and customTagId='" + beans.get(i).getId() + "'", null);
            try {
                Log.i("CustomTag", "Updatequrey" + "update taskDetailsInfo set taskDescription = '" + bean.getCustomTagId() + "' where customSetId='" + bean.getCustomSetId() + "'and customTagId='" + bean.getCustomTagId() + "'");
                    /*String s = "update taskDetailsInfo set taskDescription = '" + bean.getTaskDescription() + "' where customSetId='" + bean.getCustomSetId() + "' and customTagId='" + bean.getCustomTagId() + "'";
                    db.execSQL(s);*/
                ContentValues cv1 = new ContentValues();
                cv1.put("taskDescription", bean.getTaskDescription());
                cv1.put("customTagVisible", bean.isCustomTagVisible());
                db.update("taskDetailsInfo", cv1, "customSetId=? and customTagId=?", new String[]{String.valueOf(bean.getCustomSetId()), String.valueOf(bean.getCustomTagId())});
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.i("ContactTable", "Insert");
            Log.i("conversation", "schedulecall DB else " + bean.isCustomTagVisible());
            row_id = (int) db.insert("taskDetailsInfo", null, cv);
        }

    }

    public void insertNewContact_history(ListMember bean, String name) {
        try {
            int row_id = 0;
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            Log.i("chat123","inside DB contact list");

            Log.i("ContactTable", "Value--->" + row_id);
            cv.put("contactemail", bean.getEmail());
            cv.put("userid", bean.getId());
            cv.put("username", bean.getUsername());
            cv.put("firstname", bean.getFirstName());
            cv.put("lastname", bean.getLastName());
            cv.put("code", bean.getCode());
            cv.put("title", bean.getTitle());
            cv.put("gender", bean.getGender());
            cv.put("profileImage", bean.getProfileImage());
            Appreference.profilePictures.add(bean.getProfileImage());
            cv.put("personalInfo", bean.getPersonalInfo());
            cv.put("loginuser", name);
            cv.put("userType", bean.getUserType());
            cv.put("profession", bean.getProfession());
            cv.put("organization", bean.getOrganization());
            cv.put("specialization", bean.getSpecialization());
            if(bean.getRoleId()!=null && !bean.getRoleId().equalsIgnoreCase("")) {
                cv.put("roleId", bean.getRoleId());
                cv.put("roleName", bean.getRoleName());
            }
            if (isAgendaRecordExists("select * from contact where userid='" + bean.getId() + "'and loginuser='" + name + "'")) {
                Log.i("ContactTable", "UpdateQuery");
                row_id = (int) db.update("contact", cv, "userid='" + bean.getId() + "'and loginuser='" + name + "'", null);
            } else {
                Log.i("ContactTable", "Insert");
                row_id = (int) db.insert("contact", null, cv);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.i("chat123","inside DB contact list exception==>"+e.getMessage());

        }
    }

    public void updateUserPresense(ContactBean bean, String name) {
        Log.i("buddystatus", "name : " + name + "\n userid" + bean.getUserid());
        int row_id = 0;
        if (!db.isOpen())
            openDatabase();
        ContentValues cv = new ContentValues();
        cv.put("presence", bean.getStatus());
        if (isAgendaRecordExists("select * from contact where userid='" + bean.getUserid() + "'and loginuser='" + name + "'")) {
            row_id = (int) db.update("contact", cv, "userid='" + bean.getUserid() + "'and loginuser='" + name + "'", null);
        }
    }

    public void insertGroupMember_history(ListMember bean, String name, int group_id) {
        try {
            int row_id = 0;
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            Log.i("GroupmemberTable", "Value--->" + row_id);
            cv.put("userid", bean.getId());
            cv.put("email", bean.getEmail());
            cv.put("username", bean.getUsername());
            cv.put("firstname", bean.getFirstName());
            cv.put("lastname", bean.getLastName());
            cv.put("code", bean.getCode());
            cv.put("title", bean.getTitle());
            cv.put("gender", bean.getGender());
            cv.put("departmentId", bean.getDepartmentId());
            cv.put("userStatus", bean.getUserStatus());
            cv.put("loginStatus", bean.getLoginStatus());
            Log.i("VideocallDb", "org 2 " + bean.getOrganization());
            cv.put("organization", bean.getOrganization());
            cv.put("profileImage", bean.getProfileImage());
            cv.put("groupid", group_id);
//            Appreference.groupprofilePictures.add(bean.getProfileImage());
            cv.put("mobileNo", bean.getMobileNo());
            cv.put("loginuser", name);
            if (isAgendaRecordExists("select * from groupmember where userid='" + bean.getId() + "'and groupid='" + group_id + "'")) {
                Log.i("GroupMemberTable", "UpdateQuery ## ");
                row_id = (int) db.update("groupmember", cv, "userid='" + bean.getId() + "'and groupid='" + group_id + "'", null);
            } else {
                Log.i("GroupMemberTable", "Insert ## ");
                row_id = (int) db.insert("groupmember", null, cv);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void insertGroup_history(GroubDetails bean, String name) {
        try {
            int row_id = 0;
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            Log.i("GroupTable", "Value--->" + row_id);
            cv.put("groupid", bean.getId());
            cv.put("groupname", bean.getGroupName());
            cv.put("groupowner", bean.getGroupOwner());
            cv.put("departmentid", bean.getDepartmentId());
            cv.put("description", bean.getDescription());
            cv.put("logo", bean.getLogo());
            cv.put("grouplogo", bean.getGroupLogo());
            cv.put("departmentref", bean.getDepartmentref());
            cv.put("loginuser", name);
            if (isAgendaRecordExists("select * from group1 where groupid='" + bean.getId() + "'and loginuser='" + name + "'")) {
                Log.i("GroupTable", "UpdateQuery");
                row_id = (int) db.update("group1", cv, "groupid='" + bean.getId() + "'and loginuser='" + name + "'", null);
            } else {
                Log.i("GroupTable", "Insert");
                row_id = (int) db.insert("group1", null, cv);
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /*public void insertListMemberAccess_history(GroupMemberAccess bean, String name) {
        try {
            int row_id = 0;
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            Log.i("GroupTable", "Value--->" + row_id);
            cv.put("userid", bean.getUserid());
            cv.put("groupid", bean.getGroupId());
            cv.put("groupname", bean.getGroupName());
            cv.put("loginuser", name);
            cv.put("accessForms", bean.getAccessForms());
            cv.put("accessReminder", bean.getAccessReminder());
            cv.put("adminAccess", bean.getAdminAccess());
            cv.put("audioAccess", bean.getAudioAccess());
            cv.put("chatAccess", bean.getChatAccess());
            cv.put("respondAudio", bean.getRespondAudio());
            cv.put("respondConfCall", bean.getRespondConfCall());
            cv.put("accessScheduledCNF", bean.getAccessScheduledCNF());
            cv.put("respondDateChange", bean.getRespondDateChange());
            cv.put("respondFiles", bean.getRespondFiles());
            cv.put("respondLocation", bean.getRespondLocation());
            cv.put("respondVideo", bean.getRespondVideo());
            cv.put("respondPhoto", bean.getRespondPhoto());
            cv.put("respondPrivate", bean.getRespondPrivate());
            cv.put("respondSketch", bean.getRespondSketch());
            cv.put("videoAccess", bean.getVideoAccess());
            cv.put("respondText", bean.getRespondText());
            cv.put("respondTask", bean.getRespondTask());
            cv.put("GroupTask", bean.getGroup_Task());
            cv.put("ReassignTask", bean.getReassignTask());
            cv.put("ChangeTaskName", bean.getChangeTaskName());
            cv.put("TaskDescriptions", bean.getTaskDescriptions());
            cv.put("TemplateExistingTask", bean.getTemplateExistingTask());
            cv.put("ApproveLeave", bean.getApproveLeave());
            cv.put("RemindMe", bean.getRemindMe());
            cv.put("AddObserver", bean.getAddObserver());
            cv.put("TaskPriority", bean.getTaskPriority());
            cv.put("Escalations", bean.getEscalations());

            Log.i("groupMemberAccess", "ReassignTask " + bean.getReassignTask() + bean.getGroupId());

            if (isAgendaRecordExists("select * from listUserGroupMemberAccess where groupid='" + bean.getGroupId() + "'and loginuser='" + name + "'")) {
                Log.i("GroupTable", "UpdateQuery");
                row_id = (int) db.update("listUserGroupMemberAccess", cv, "groupid='" + bean.getGroupId() + "'and loginuser='" + name + "'", null);
            } else {
                Log.i("GroupTable", "Insert");
                row_id = (int) db.insert("listUserGroupMemberAccess", null, cv);
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }*/
    public void insertListMemberAccess_history(GroupMemberAccess bean, String name) {
        try {
            int row_id = 0;
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            Log.i("GroupTable", "Value---> " + row_id);
            cv.put("userid", bean.getUserid());
            cv.put("groupid", bean.getGroupId());
            cv.put("groupname", bean.getGroupName());
            cv.put("loginuser", name);
            cv.put("accessForms", bean.getAccessForms());
            cv.put("accessReminder", bean.getAccessReminder());
            cv.put("adminAccess", bean.getAdminAccess());
            cv.put("audioAccess", bean.getAudioAccess());
            cv.put("chatAccess", bean.getChatAccess());
            cv.put("respondAudio", bean.getRespondAudio());
            cv.put("respondConfCall", bean.getRespondConfCall());
            cv.put("accessScheduledCNF", bean.getAccessScheduledCNF());
            cv.put("respondDateChange", bean.getRespondDateChange());
            cv.put("respondFiles", bean.getRespondFiles());
            cv.put("respondLocation", bean.getRespondLocation());
            cv.put("respondVideo", bean.getRespondVideo());
            cv.put("respondPhoto", bean.getRespondPhoto());
            cv.put("respondPrivate", bean.getRespondPrivate());
            cv.put("respondSketch", bean.getRespondSketch());
            cv.put("videoAccess", bean.getVideoAccess());
            cv.put("respondText", bean.getRespondText());
            cv.put("respondTask", bean.getRespondTask());
            cv.put("GroupTask", bean.getGroup_Task());
            cv.put("ReassignTask", bean.getReassignTask());
            cv.put("ChangeTaskName", bean.getChangeTaskName());
            cv.put("TaskDescriptions", bean.getTaskDescriptions());
            cv.put("TemplateExistingTask", bean.getTemplateExistingTask());
            cv.put("ApproveLeave", bean.getApproveLeave());
            cv.put("RemindMe", bean.getRemindMe());
            cv.put("AddObserver", bean.getAddObserver());
            cv.put("TaskPriority", bean.getTaskPriority());
            cv.put("Escalations", bean.getEscalations());

            Log.i("groupMemberAccess", "ReassignTask " + bean.getReassignTask() + bean.getGroupId());

            if (isAgendaRecordExists("select * from listUserGroupMemberAccess where groupid='" + bean.getGroupId() + "'and loginuser='" + name + "'")) {
                Log.i("GroupTable", "UpdateQuery");
                row_id = (int) db.update("listUserGroupMemberAccess", cv, "groupid='" + bean.getGroupId() + "'and loginuser='" + name + "'", null);
            } else {
                Log.i("GroupTable", "Insert");
                row_id = (int) db.insert("listUserGroupMemberAccess", null, cv);
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


    public boolean insertORupdate_Task_history(TaskDetailsBean bean) {
        int row_id = 0;
        boolean inserted = false;
        try {
            String logginuser = AppSharedpreferences.getInstance(context).getString("loginUserName");
            logginuser = logginuser.replace('_', '@');
            Log.i("DB_Insert", "logginuser" + logginuser + " bean.getTaskUTCTime() :" + bean.getTaskUTCTime());

            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            Log.i("DB_Insert", "Issues Id == " + bean.getIssueId());

            cv.put("loginuser", logginuser);
            cv.put("fromUserId", bean.getFromUserId());
            cv.put("toUserId", bean.getToUserId());
            cv.put("taskNo", bean.getTaskNo());
            cv.put("plannedStartDateTime", bean.getUtcPlannedStartDateTime());
            cv.put("plannedEndDateTime", bean.getUtcplannedEndDateTime());
            cv.put("remainderFrequency", bean.getUtcPemainderFrequency());
            cv.put("duration", bean.getDuration());
            cv.put("durationunit", bean.getDurationUnit());
            Log.i("task", "taskdescription value update " + bean.getTaskDescription());
            cv.put("taskDescription", bean.getTaskDescription());
            cv.put("isRemainderRequired", bean.getIsRemainderRequired());
            Log.i("DataBaseLog", "value DB taskDetailsBean.getTaskStatus() " + bean.getTaskStatus() + " Mime TYpe " + bean.getMimeType());
            cv.put("taskStatus", bean.getTaskStatus());
            cv.put("signalid", bean.getSignalid());
            cv.put("fromUserName", bean.getFromUserName());
            cv.put("toUserName", bean.getToUserName());
            cv.put("sendStatus", bean.getSendStatus());
            cv.put("completedPercentage", bean.getCompletedPercentage());
            cv.put("taskType", bean.getTaskType());
            cv.put("mimeType", bean.getMimeType());
            cv.put("taskPriority", bean.getTaskPriority());
            cv.put("dateFrequency", bean.getDateFrequency());
            cv.put("timeFrequency", bean.getTimeFrequency());
            cv.put("taskId", bean.getTaskId());
            cv.put("showprogress", String.valueOf(bean.getShow_progress()));
            cv.put("readStatus", bean.getRead_status());
            cv.put("reminderquotes", bean.getReminderQuote());
            cv.put("remark", bean.getRemark());
            Log.i("time", "value");
            cv.put("serverFileName", bean.getServerFileName());
            cv.put("tasktime", bean.getTaskUTCTime());
            Log.i("task", "taskdescription value getMsg_status " + bean.getMsg_status());
            cv.put("msgstatus", bean.getMsg_status());
            cv.put("requestStatus", bean.getRequestStatus());
            cv.put("syncEnable", bean.getSyncEnable());
            if (bean.getTaskUTCDateTime() != null && !bean.getTaskUTCDateTime().equalsIgnoreCase(null) && !bean.getTaskUTCDateTime().equalsIgnoreCase("")) {
                cv.put("dateTime", bean.getTaskUTCDateTime());
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                String dateforrow = dateFormat.format(new Date());
                Log.i("DB UTC", "UTC time dateforrow " + dateforrow);
                cv.put("dateTime", dateforrow);
            }
            Log.i("DataBaseLog", "value DB bean.getSubType() 1 =====> " + bean.getSubType() + " Mime TYpe " + bean.getMimeType() + " TaskName  " + bean.getTaskName() + " Desc " + bean.getTaskDescription());
            cv.put("subType", bean.getSubType());
            cv.put("private_member", bean.getPrivate_Member());
            Log.i("priority", "check subtype db " + bean.getSubType());
            cv.put("daysOfTheWeek", bean.getDaysOfTheWeek());
            cv.put("repeatFrequency", bean.getRepeatFrequency());
            cv.put("taskTagName", bean.getTaskTagName());
            cv.put("customTagId", bean.getCustomTagId());
            cv.put("longmessage", bean.getLongmessage());
            Log.i("taskconversation", "CusTome ---->>><<< VideoCallDb " + bean.isCustomTagVisible());
            cv.put("customTagVisible", bean.isCustomTagVisible());
            cv.put("customSetId", bean.getCustomSetId());
            if (bean.getWs_send() != null)
                cv.put("wssendstatus", bean.getWs_send());
            cv.put("projectId", bean.getProjectId());
            if (bean.getParentTaskId() != null && !bean.getParentTaskId().equalsIgnoreCase("")) {
                cv.put("parentTaskId", bean.getParentTaskId());
            }
            cv.put("sender_reply", bean.getSender_reply());
            cv.put("reply_sendername", bean.getReply_sender_name());
            if (bean.getTaskPlannedBeforeEndDate() != null)
                cv.put("taskPlannedBeforeEndDate", bean.getTaskPlannedBeforeEndDate());
            if (bean.getTaskPlannedLatestEndDate() != null)
                cv.put("taskPlannedLatestEndDate", bean.getTaskPlannedLatestEndDate());
            if (bean.getFromTaskName() != null)
                cv.put("fromTaskName", bean.getFromTaskName());
            if (bean.getToTaskName() != null)
                cv.put("toTaskName", bean.getToTaskName());
            Log.i("DB_Insert", "Issues Id  2 == " + bean.getIssueId());
            Log.i("TaskTable", "Insert");
            Log.i("TaskTable", "Insert" + bean.getSignalid());

            if (!DuplicateChecker(bean.getSignalid(), bean.getTaskId())) {
                row_id = (int) db.insert("taskDetailsInfo", null, cv);
                Appreference.printLog("sipregister", "dp insertion" + bean.getSignalid(), "DEBUG", null);
                Log.i("TaskTable", "getSignalid inserted " + bean.getSignalid());
                inserted = true;
            } else {
                row_id = (int) db.update("taskDetailsInfo", cv, "signalid ='" + bean.getSignalid() + "'", null);
                Log.i("TaskTable", "getSignalid updated " + bean.getSignalid());
                inserted = false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return inserted;
    }


    /**
     * Insert Or Update TaskHistory Page  for TaskHistoryInfo Table
     */


    public boolean insertORupdate_TaskHistoryInfo(TaskDetailsBean bean) {
        int row_id = 0;
        boolean inserted = false;
        try {
            String logginuser = AppSharedpreferences.getInstance(context).getString("loginUserName");
            logginuser = logginuser.replace('_', '@');
            Log.i("DB_Insert", "logginuser" + logginuser + " bean.getTaskUTCTime() :" + bean.getTaskUTCTime());

            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();

            cv.put("loginuser", logginuser);
            cv.put("taskNo", bean.getTaskNo());
            if (bean.getTaskName() != null && !bean.getTaskName().trim().equals("")) {
                cv.put("taskName", bean.getTaskName());
            }
            cv.put("plannedStartDateTime", bean.getUtcPlannedStartDateTime());
            cv.put("plannedEndDateTime", bean.getUtcplannedEndDateTime());
            cv.put("remainderFrequency", bean.getUtcPemainderFrequency());
            Log.i("task", "TaskHistoryInfo  taskdescription value update " + bean.getTaskDescription());
            cv.put("taskDescription", bean.getTaskDescription());
            cv.put("sender_reply", bean.getSender_reply());
            cv.put("reply_sendername", bean.getReply_sender_name());
            if (bean.getTaskPlannedBeforeEndDate() != null)
                cv.put("taskPlannedBeforeEndDate", bean.getTaskPlannedBeforeEndDate());
            if (bean.getTaskPlannedLatestEndDate() != null)
                cv.put("taskPlannedLatestEndDate", bean.getTaskPlannedLatestEndDate());
            if (bean.getFromTaskName() != null)
                cv.put("fromTaskName", bean.getFromTaskName());
            if (bean.getToTaskName() != null)
                cv.put("toTaskName", bean.getToTaskName());
            Log.i("Accept", "TaskHistoryInfo  value DB taskDetailsBean.getTaskStatus() " + bean.getTaskStatus());
            String task_statuss = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskStatus from taskHistoryInfo where taskId = '" + bean.getTaskId() + "'");
            if (bean.getTaskStatus() != null && bean.getTaskStatus().equalsIgnoreCase("reminder")) {
                cv.put("taskStatus", "inprogress");
            } else if (bean.getTaskStatus() != null && bean.getTaskStatus().equalsIgnoreCase("abandoned")) {
                cv.put("taskStatus", "abandoned");
            } else if (task_statuss != null && task_statuss.equalsIgnoreCase("inprogress") && bean.getTaskStatus().equalsIgnoreCase("assigned")) {
                cv.put("taskStatus", "inprogress");
            } else if (task_statuss != null && task_statuss.equalsIgnoreCase("overdue") && bean.getMimeType().equalsIgnoreCase("date") && bean.getFromUserName() != null && bean.getFromUserName().equalsIgnoreCase(bean.getOwnerOfTask())) {
                cv.put("taskStatus", "inprogress");
            } else if (!VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskStatus from taskHistoryInfo where taskId = '" + bean.getTaskId() + "'").equalsIgnoreCase("overdue") || bean.getTaskDescription().contains("Completed Percentage ")) {
                cv.put("taskStatus", bean.getTaskStatus());
            }
            cv.put("signalid", bean.getSignalid());

            cv.put("taskType", bean.getTaskType());

            Log.d("videocalldatabase", "group percentage check == " + bean.getCompletedPercentage());
            Log.d("videocalldatabase", "group percentage from == " + bean.getFromUserName());
            Log.d("videocalldatabase", "group percentage ownar == " + bean.getOwnerOfTask());
            if (Appreference.loginuserdetails != null && Appreference.loginuserdetails.getUsername() != null) {
                Log.d("videocalldatabase", "group percentage login == " + Appreference.loginuserdetails.getUsername());
            } else {
                Appreference.loginuserdetails = new Loginuserdetails();
                Appreference.loginuserdetails.setUsername(logginuser.replace("@", "_"));
                Log.d("videocalldatabase", "group percentage login  else == " + Appreference.loginuserdetails.getUsername());
            }
            Log.d("videocalldatabase", "group percentage TaskType == " + bean.getTaskType());
            Log.d("videocalldatabase", "group percentage login == " + Appreference.loginuserdetails.getUsername());


            if (bean.getTaskType() != null && bean.getTaskType().equalsIgnoreCase("group") && Appreference.loginuserdetails.getUsername().equalsIgnoreCase(bean.getOwnerOfTask())) {
                if (bean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && bean.getTaskDescription().contains("Completed Percentage ")) {
                    cv.put("completedPercentage", bean.getCompletedPercentage());
                    Log.i("videocalldatabase", "group percentage check 1 == " + bean.getCompletedPercentage());
                } else {
                    cv.put("completedPercentage", String.valueOf(VideoCallDataBase.getDB(context).GroupPercentageChecker(bean.getToUserName(), bean.getTaskId(), bean.getOwnerOfTask())));
                    Log.i("videocalldatabase", "group percentage check  2== " + bean.getCompletedPercentage());
                    Log.i("videocalldatabase", "group percentage check 2.1== " + String.valueOf(VideoCallDataBase.getDB(context).GroupPercentageChecker(bean.getToUserName(), bean.getTaskId(), bean.getOwnerOfTask())));
                    if (String.valueOf(VideoCallDataBase.getDB(context).GroupPercentageChecker(bean.getToUserName(), bean.getTaskId(), bean.getOwnerOfTask())).equalsIgnoreCase("100")) {
                        cv.put("taskStatus", "Completed");
                    }
                }
            } else if (bean.getTaskType().equalsIgnoreCase("group") && (Appreference.loginuserdetails.getUsername().equalsIgnoreCase(bean.getFromUserName()) || bean.getOwnerOfTask().equalsIgnoreCase(bean.getFromUserName()))) {
                if (bean.getCompletedPercentage() != null && !bean.getCompletedPercentage().equalsIgnoreCase("0")) {
                    cv.put("completedPercentage", bean.getCompletedPercentage());
                }
                Log.d("videocalldatabase", "group percentage check 3 == " + bean.getCompletedPercentage());
            } else if (!bean.getTaskType().equalsIgnoreCase("group")) {
                if (bean.getCompletedPercentage() != null && !bean.getCompletedPercentage().equalsIgnoreCase("0")) {
                    cv.put("completedPercentage", bean.getCompletedPercentage());
                }
                Log.d("videocalldatabase", "group percentage check 4 == " + bean.getCompletedPercentage());
            }


            Log.d("videocalldatabase", "group percentage check 5 == " + bean.getCompletedPercentage());

            cv.put("mimeType", bean.getMimeType());
            if (bean.getIssueId() != null && !bean.getIssueId().equalsIgnoreCase("")) {
                cv.put("parentTaskId", bean.getIssueId());
            }
            cv.put("taskId", bean.getTaskId());
            cv.put("readStatus", bean.getRead_status());
            cv.put("tasktime", bean.getTaskUTCTime());
            if (bean.getCatagory() != null && !bean.getCatagory().equalsIgnoreCase(null) && !bean.getCatagory().equalsIgnoreCase("") && !bean.getCatagory().equalsIgnoreCase("null") && !bean.getCatagory().equalsIgnoreCase("(null)")) {
                cv.put("category", bean.getCatagory());
            }
            Log.d("TaskCategory ", "Catagory 200.  " + bean.getCatagory());
            Log.i("videcalldatebase", "bean.getTaskStatus() " + bean.getTaskStatus());
            if (bean.getTaskStatus() != null && (!bean.getTaskStatus().equalsIgnoreCase("overdue") || bean.getTaskStatus().equalsIgnoreCase("reminder"))) {
                cv.put("ownerOfTask", bean.getOwnerOfTask());
                if (bean.getTaskType().equalsIgnoreCase("group")) {
                    String groupName = getProjectParentTaskId("select groupname from group1 where groupid='" + bean.getToUserId() + "'");
                    Log.d("TaskCategory ", "taskReceiver 200.  " + bean.getToUserId());
                    Log.d("TaskCategory ", "taskReceiver 200.  " + groupName);
                    if (groupName != null && !groupName.equalsIgnoreCase("") && !bean.getTaskStatus().equalsIgnoreCase("reminder"))
                        cv.put("taskReceiver", groupName);
                } else {
                    if (bean.getTaskReceiver() != null && !bean.getTaskReceiver().trim().equals("")) {
                        cv.put("taskReceiver", bean.getTaskReceiver());
                    }
                }
                Log.i("videcalldatebase", "bean.getTaskStatus() INSIDE " + bean.getTaskStatus());
            }
            Log.i("oberver", "TaskHistoryInfo  getTaskObservers " + bean.getTaskObservers());
            Log.i("oberver", "TaskHistoryInfo  getTaskRemoveObservers " + bean.getTaskRemoveObservers());
            if ((bean.getTaskObservers() != null && !bean.getTaskObservers().equalsIgnoreCase("(null)") && !bean.getTaskObservers().equalsIgnoreCase(null) && !bean.getTaskObservers().equalsIgnoreCase("null") && !bean.getTaskObservers().equalsIgnoreCase("")) || (bean.getRejectedObserver() != null && !bean.getRejectedObserver().equalsIgnoreCase("") && !bean.getRejectedObserver().equalsIgnoreCase("null"))) {
                cv.put("taskObservers", bean.getTaskObservers());
                Log.i("oberver", "TaskHistoryInfo  observer");
            } else if (bean.getTaskObservers() == null && bean.getTaskRemoveObservers() != null) {
                cv.put("taskObservers", "");
                Log.i("oberver", "TaskHistoryInfo  removed observer");
            }

            Log.i("time", "TaskHistoryInfo  value");
            Log.i("videcalldatebase", "bean.getGroupTaskMembers() " + bean.getGroupTaskMembers());
            Log.i("TaskArrayAdapter", "New DB OwnerOftask---------> 1 *  " + bean.getGroupTaskMembers());
            if (bean.getGroupTaskMembers() != null && !bean.getGroupTaskMembers().equalsIgnoreCase("(null)") && !bean.getGroupTaskMembers().equalsIgnoreCase(null) && !bean.getGroupTaskMembers().equalsIgnoreCase("null") ) {
                Log.i("videcalldatebase", "bean.getGroupTaskMembers() inside " + bean.getGroupTaskMembers());
                cv.put("taskMemberList", bean.getGroupTaskMembers());
            }
            if (bean.getTaskUTCDateTime() != null && !bean.getTaskUTCDateTime().equalsIgnoreCase(null) && !bean.getTaskUTCDateTime().equalsIgnoreCase("")) {
                cv.put("dateTime", bean.getTaskUTCDateTime());
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                String dateforrow = dateFormat.format(new Date());
                Log.i("DB UTC", "TaskHistoryInfo UTC time dateforrow " + dateforrow);
                cv.put("dateTime", dateforrow);
            }
            Log.i("TaskTable", "Insert");
            Log.i("TaskTable", "Insert" + bean.getSignalid());
            if (!DuplicateTaskIdChecker("select taskId from taskHistoryInfo where taskId='" + bean.getTaskId() + "'  ")) {
                row_id = (int) db.insert("taskHistoryInfo", null, cv);
                Appreference.printLog("sipregister", "dp insertion" + bean.getTaskId(), "DEBUG", null);
                Log.e("TaskTable", "getSignalid" + bean.getSignalid());
                Log.e("TaskTable", "getTaskId" + bean.getTaskId());
                inserted = true;
            } else {
                row_id = (int) db.update("taskHistoryInfo", cv, "taskId ='" + bean.getTaskId() + "'", null);
                inserted = false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return inserted;
    }


    public int insertORupdate_ListAllgetTaskDetails(ListAllgetTaskDetails bean) {
        int row_id = 0;
        try {
            Log.i("send_syn_txt", "000 " + bean.getDescription());
//            Log.i("send_syn_txt", "000 " + bean.getGroup());
//            String listAlluser = Appreference.listAlluserDetails.getEmail();
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            cv.put("loginuser", Appreference.loginuserdetails.getEmail());
            cv.put("taskId", bean.getId());
            ListFromDetails listFromDetails = new ListFromDetails();
            listFromDetails = bean.getFromlist();
            ListFromDetails listToDetails = new ListFromDetails();
            listToDetails = bean.getTolist();
            Group group = new Group();
//            group = bean.getGroup();
            ArrayList<ListFromDetails> listObservers = new ArrayList<ListFromDetails>();
            listObservers = bean.getListObserver();
            ArrayList<ListTaskFiles> listTaskFiles1;
            listTaskFiles1 = bean.getListTaskFiles();
            String finalobserver = "";
            if (listObservers.size() > 0) {
                Log.i("send_syn_txt", "1 " + listObservers.size());
                for (int i = 0; i < listObservers.size(); i++) {
                    Log.i("send_syn_txt", "2 " + listObservers.get(i));
                    finalobserver = finalobserver + listObservers.get(i).getUsername().toString() + ",";
                    Log.i("send_syn_txt", "3 " + finalobserver);
                }
                Log.i("send_syn_txt", "4 " + finalobserver);
            }
//            group = null;
            if (bean != null && bean.getIsGroupTask() != null && bean.getIsGroupTask().equalsIgnoreCase("Y")) {
//                Log.i("listAllMyTask", "value bean.getId() " + bean.getGroup().getGroupName());
                group = bean.getGroup();
            }
//            Log.i("listAllMyTask", "value bean.getGroup() " + bean.getGroup());

//            Log.i("toUserId", " 1 " + bean.getGroup().getId());
//            Log.i("toUserName", " 2 " + bean.getGroup().getGroupName());
//            Log.i("taskReceiver", " 3 " + bean.getGroup().getGroupName());
            if (finalobserver != null && !finalobserver.equalsIgnoreCase("") && !finalobserver.equalsIgnoreCase("(null)")) {
                finalobserver = finalobserver.substring(0, finalobserver.length() - 1);
            }
            Log.i("send_syn_txt", "5 " + finalobserver);
//            cv.put("taskObservers", finalobserver);
            Log.i("send_syn_txt", "6 " + finalobserver);
            ArrayList<String> name = new ArrayList<String>();
            ArrayList<ListTaskConversation> listTaskConversations = bean.getListTaskConversation();
            Log.d("listTaskConversations", "size ----> " + listTaskConversations.size());
            Log.d("listTaskFiles", "size ----> " + listTaskFiles1.size());
            cv.put("projectId", bean.getProjectId());
            String signal_id = bean.getSignalId();
            cv.put("signalid", signal_id);
            cv.put("taskNo", bean.getTaskNo());
            Log.i("VideoCallDB", "insertORupdate_ListAllgetTaskDetails **** " + bean.getParentTask());
//            cv.put("taskName", bean.getName());
            cv.put("showprogress", "1");
            cv.put("readStatus", "0");
            cv.put("msgstatus", "1");
            cv.put("sendStatus", "0");
            cv.put("customTagVisible", true);
            cv.put("fromUserId", listFromDetails.getId());
            cv.put("fromUserName", listFromDetails.getUsername());
          /*  if (bean.getTaskCategory() != null && bean.getTaskCategory().equalsIgnoreCase("taskCreation")) {
                cv.put("category", "Task");
            } else {
                cv.put("category", "issue");
            }*/
            if (bean.getIsGroupTask() != null && bean.getIsGroupTask().equalsIgnoreCase("Y")) {
//                    cv.put("toUserId", listToDetails.getId());
//                cv.put("toUserId", group.getId());
//                cv.put("toUserName", group.getGroupName());
//                cv.put("taskReceiver", group.getGroupName());
                if (bean.getGroup() != null) {
                    cv.put("toUserId", bean.getGroup().getId());
                    cv.put("toUserName", bean.getGroup().getGroupName());
                }
//                cv.put("taskReceiver", bean.getGroup().getGroupName());
                List<ListMembers> listMembers = new ArrayList<ListMembers>();
                if (group != null)
                    listMembers = group.getListMember();
                String listMember = "";
                if (listMembers != null)
                    for (int j = 0; j < listMembers.size(); j++) {
                        listMember = listMember.concat(listMembers.get(j).getUsername() + ",");
                    }
                if (listMember != null && listMember.trim().length() > 0)
                    listMember = listMember.substring(0, listMember.length() - 1);
//                cv.put("taskMemberList", listMember);
                cv.put("taskType", "Group");
            } else {
                if (bean.getStatus() != null && !bean.getStatus().equalsIgnoreCase("draft")) {
                    if (listToDetails != null) {
                        cv.put("toUserId", listToDetails.getId());
                        cv.put("toUserName", listToDetails.getUsername());
                    }
                }
//                cv.put("taskReceiver", bean.getToUser().getUsername());
                cv.put("taskType", "Individual");
            }
            if (bean.getCreatedDate() != null) {
                cv.put("dateTime", bean.getCreatedDate().substring(0, 19));
                cv.put("tasktime", bean.getCreatedDate().substring(0, 19));
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                String dateforrow = dateFormat.format(new Date());
                Log.i("DB UTC", "UTC time for subtask " + dateforrow);
                cv.put("dateTime", dateforrow);
                cv.put("taskTime", dateforrow);
            }
            if (bean.getStatus() != null && bean.getStatus().equalsIgnoreCase("overdue")) {
                cv.put("taskStatus", "inprogress");
            } else {
                cv.put("taskStatus", bean.getStatus());
            }
//            if (listTaskFiles1.size() == 0 && listTaskConversations.size() == 0) {
            /*if (listTaskFiles1.size() == 0) {
                if (bean.getName() != null && !bean.getName().equalsIgnoreCase("")) {
                    cv.put("taskDescription", bean.getName());
                }
                cv.put("mimeType", "text");
                if (!DuplicateChecker(signal_id, String.valueOf(bean.getId()))) {
                    if (bean.getTaskCategory().equalsIgnoreCase("chat")) {
                        cv.put("subType", "normal");
                    } else {
                        Log.d("subType_getTask", "value## ");
                        cv.put("subType", "taskDescription");
                    }
                    Log.i("getTask-date", "signalid 2 " + signal_id + "task description " + bean.getDescription() + "taskno " + bean.getTaskNo() + "subType ");
                    row_id = (int) db.insert("taskDetailsInfo", null, cv);
                    Appreference.printLog("sipregister", "dp insertion" + signal_id, "DEBUG", null);
                    Log.e("TaskTable", "getSignalid" + signal_id);
                } else {
                    if (bean.getTaskCategory().equalsIgnoreCase("chat")) {
                        cv.put("subType", "normal");
                    } else {
                        Log.d("subType_getTask", "value## else ");
                        cv.put("subType", "taskDescription");
                    }
                    Log.i("send_syn_txt", "signalid 1 " + signal_id + "task signalid " + bean.getDescription() + "parentId " + bean.getTaskNo());
                    row_id = (int) db.update("taskDetailsInfo", cv, "signalid='" + signal_id + "'", null);
                }
            }*/

/*
            if (bean.getStatus() != null && bean.getStatus().equalsIgnoreCase("draft")) {
                cv.put("duration", bean.getDuration());
                String duration_unit = "\"" + bean.getDurationWords() + "\"";
                cv.put("durationunit", duration_unit);
                signal_id = bean.getSignalId().concat(bean.getName());
                cv.put("signalId", signal_id);
                cv.put("taskDescription", "template");
                cv.put("mimeType", "date");

//                        cv.put("timeFrequency", listAllgetTaskDetailses1.getTimeFrequency());
                if (bean.getTimeFrequency() != null) {
                    String timefrequency = Appreference.TimeFrequencyConvertion(bean.getTimeFrequency());
                    cv.put("timeFrequency", timefrequency);
                    Log.i("getTask ", "timefrequency ---> " + timefrequency);
                }

                if (bean.getRemainderQuotes() != null && !bean.getRemainderQuotes().equalsIgnoreCase("")) {
                    String tempRem_quotes = "\"" + bean.getRemainderQuotes() + "\"";
                    cv.put("reminderquotes", tempRem_quotes);
                } else {
                    cv.put("reminderquotes", "\"" + "Task Reminder" + "\"");
                }
                if (!DuplicateChecker(signal_id, String.valueOf(bean.getId()))) {
                    cv.put("subType", "normal");
                    row_id = (int) db.insert("taskDetailsInfo", null, cv);
                    Appreference.printLog("sipregister", "dp insertion" + signal_id, "DEBUG", null);
                    Log.e("TaskTable templatedur ", "getSignalid " + signal_id);
                } else {
//                    if (listTaskFiles.getFileType().equalsIgnoreCase("image")||listTaskFiles.getFileType().equalsIgnoreCase("audio")||listTaskFiles.getFileType().equalsIgnoreCase("video")||listTaskFiles.getFileType().equalsIgnoreCase("document")){
//
//                    }else {
                    cv.put("subType", "normal");
                    row_id = (int) db.update("taskDetailsInfo", cv, "signalid='" + signal_id + "'", null);
                    Log.e("TaskTable templatedur ", "getSignalid " + signal_id);
//                    }
                }
            }
*/
            if (0 < listTaskFiles1.size()) {
                for (int i = 0; i < listTaskFiles1.size(); i++) {
                    ListTaskFiles listTaskFiles = listTaskFiles1.get(i);
                    signal_id = bean.getSignalId();
                    cv.put("signalid", signal_id);
                    if (listTaskFiles.getFileType().equals("text") || listTaskFiles.getFileType().equals("map")) {
//                        if (bean.getProjectId() != null) {
                        if (listTaskFiles.getDescription() != null && !listTaskFiles.getDescription().equalsIgnoreCase("") && !listTaskFiles.getDescription().equalsIgnoreCase(null)) {
                            cv.put("taskDescription", listTaskFiles.getDescription());
                        } else {
                            cv.put("taskDescription", bean.getName());
                        }
                        if (listTaskFiles1.size() > 1) {
                            signal_id = signal_id.concat(listTaskFiles.getDescription() + i);
                        } else {
                            signal_id = bean.getSignalId();
                        }
                        cv.put("signalid", signal_id);
                       /* } else {
                            cv.put("taskDescription", listTaskFiles.getDescription());
                        }*/
                        Log.i("send_syn_txt", "7 " + listTaskFiles.getDescription());
                    } else {
                        if (bean.getIsGroupTask() != null && bean.getIsGroupTask().equalsIgnoreCase("Y")) {
                            String path_1 = Environment.getExternalStorageDirectory() + "/High Message/downloads/" + String.valueOf(listTaskFiles.getFileName());
//                            if (bean.getProjectId() != null) {
                            if (listFromDetails.getUsername() != null && listFromDetails.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                cv.put("taskDescription", path_1);
                                if (listTaskFiles1.size() > 1) {
                                    signal_id = signal_id.concat(listTaskFiles.getFileName() + i);
                                } else {
                                    signal_id = bean.getSignalId();
                                }
                                cv.put("signalid", signal_id);
                            } else {
                                cv.put("taskDescription", listTaskFiles.getFileName());
                                if (listTaskFiles1.size() > 1) {
                                    signal_id = signal_id.concat(listTaskFiles.getFileName() + i);
                                } else {
                                    signal_id = bean.getSignalId();
                                }
                                cv.put("signalid", signal_id);
                            }

                            cv.put("serverFileName", listTaskFiles.getFileName());
                            Log.i("send_syn_txt", "8 " + listTaskFiles.getFileName());
                        } else {
                            String path_1 = Environment.getExternalStorageDirectory() + "/High Message/downloads/" + String.valueOf(listTaskFiles.getFileName());
//                            if (bean.getProjectId() != null) {
                            if (listFromDetails.getUsername() != null && listFromDetails.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                cv.put("taskDescription", path_1);
                                if (listTaskFiles1.size() > 1) {
                                    signal_id = signal_id.concat(listTaskFiles.getFileName() + i);
                                } else {
                                    signal_id = bean.getSignalId();
                                }
                                cv.put("signalid", signal_id);
                            } else {
                                cv.put("taskDescription", listTaskFiles.getFileName());
                                if (listTaskFiles1.size() > 1) {
                                    signal_id = signal_id.concat(listTaskFiles.getFileName() + i);
                                } else {
                                    signal_id = bean.getSignalId();
                                }
                                cv.put("signalid", signal_id);
                            }
                            Log.i("videocallDB", "signal gettask is " + signal_id);

                            cv.put("serverFileName", listTaskFiles.getFileName());
                            Log.i("send_syn_txt", "8 " + listTaskFiles.getFileName());
                        }
                    }
//                    cv.put("isRemainderRequired", bean.getIsRemainderRequired());
//                    cv.put("taskStatus", bean.getStatus());
                    Log.i("send_syn_txt", "signalid 4 " + signal_id + "task signalid " + signal_id + "parentId " + bean.getParentId());
//                    cv.put("fromUserName", listFromDetails.getUsername());
                    cv.put("sendStatus", "0");
                    if (bean.getCompletedPercentage() != null)
                        cv.put("completedPercentage", bean.getCompletedPercentage());
                    cv.put("taskType", "Individual");
//                cv.put("ownerOfTask", listFromDetails.getUsername());
                    Log.i("send_syn_txt", "9 " + listTaskFiles.getFileType());
                    cv.put("mimeType", listTaskFiles.getFileType());

                    if (bean.getTaskPriority() != null && bean.getTaskPriority().equals(2))
                        cv.put("taskPriority", "High");
                    else if (bean.getTaskPriority() != null && bean.getTaskPriority().equals(0))
                        cv.put("taskPriority", "Low");
                    else
                        cv.put("taskPriority", "Medium");
//                    cv.put("dateFrequency", bean.getDateFrequency());
//                    cv.put("timeFrequency", bean.getTimeFrequency());
                    cv.put("remark", bean.getRemark());
//                    cv.put("reminderquotes", bean.getRemainderQuotes());
                    Log.i("listall", "list of files date time to db entries " + bean.getCreatedDate() + " listoffile description " + listTaskFiles.getDescription());
//                    cv.put("dateTime", bean.getCreatedDate().substring(0, 19));
//                    cv.put("taskTime", bean.getCreatedDate().substring(0, 19));
//                    cv.put("customTagVisible", true);

                    Log.i("++", "Insert first entry for files ");
                    if (!DuplicateChecker(signal_id, String.valueOf(bean.getId()))) {
                        if (bean.getTaskCategory().equalsIgnoreCase("chat")) {
                            cv.put("subType", "normal");
                        } else {
                            cv.put("subType", "taskDescription");
                        }
                        row_id = (int) db.insert("taskDetailsInfo", null, cv);
                        Appreference.printLog("sipregister", "dp insertion" + signal_id, "DEBUG", null);
                        Log.e("TaskTable inside ", "getSignalid " + signal_id);
                    } else {
//                    if (listTaskFiles.getFileType().equalsIgnoreCase("image")||listTaskFiles.getFileType().equalsIgnoreCase("audio")||listTaskFiles.getFileType().equalsIgnoreCase("video")||listTaskFiles.getFileType().equalsIgnoreCase("document")){
//
//                    }else {
                        if (bean.getTaskCategory().equalsIgnoreCase("chat")) {
                            cv.put("subType", "normal");
                        } else {
                            cv.put("subType", "taskDescription");
                        }
                        row_id = (int) db.update("taskDetailsInfo", cv, "signalid='" + signal_id + "'", null);
                        Log.e("TaskTable else ", "getSignalid " + signal_id);
//                    }
                    }
//                row_id = (int) db.insert("taskDetailsInfo", null, cv);
                    if (listTaskFiles1.get(i).getFileName() != null) {
                        ListTaskConversationFiles file1 = new ListTaskConversationFiles();
                        Log.i("task", "FileType123 " + listTaskFiles1.get(i).getFileType());
                        file1.setFileType(listTaskFiles1.get(i).getFileType());
                        Log.i("task", "FileName " + listTaskFiles1.get(i).getFileName());
                        file1.setFileName(listTaskFiles1.get(i).getFileName());
                        downloads(file1);

                    }
                }
            }

            for (int j = 0; j < listTaskConversations.size(); j++) {

                ListTaskConversation listTaskConversation = listTaskConversations.get(j);
                ListFromDetails listFromDetails1 = new ListFromDetails();
                listFromDetails1 = listTaskConversation.getFrom();
                ListFromDetails listToDetails1 = new ListFromDetails();
                listToDetails1 = listTaskConversation.getTo();
                signal_id = listTaskConversation.getSignalId();
                cv.put("signalid", signal_id);
                cv.put("fromUserId", listTaskConversation.getFrom().getId());
                cv.put("fromUserName", listTaskConversation.getFrom().getUsername());
                cv.put("dateTime", listTaskConversation.getCreatedDate().substring(0, 19));
                cv.put("tasktime", listTaskConversation.getCreatedDate().substring(0, 19));
                if (listTaskConversation.getRequestType().equalsIgnoreCase("taskDescription")) {
                    cv.put("subType", "taskDescription");
                } else {
                    cv.put("subType", "normal");
                }
//                cv.put("subType", "normal");
//                cv.put("ownerOfTask", listTaskConversation.getFrom().getUsername());
                if (bean.getIsGroupTask() != null && bean.getIsGroupTask().equalsIgnoreCase("Y")) {
//                    cv.put("toUserId", listToDetails.getId());
                    if (group != null) {
                        cv.put("toUserId", group.getId());
                        cv.put("toUserName", group.getGroupName());
                    }
//                    cv.put("taskReceiver", group.getGroupName());
                    cv.put("taskType", "Group");
                } else {
                    if (bean.getStatus() != null && !bean.getStatus().equalsIgnoreCase("draft")) {
                        if (listToDetails1 != null) {
                            cv.put("toUserId", listToDetails1.getId());
                            cv.put("toUserName", listToDetails1.getUsername());
                        }
                    }
//                    cv.put("taskReceiver", listTaskConversation.getTo().getUsername());
                    cv.put("taskType", "Individual");
                }
                if (listTaskConversation.getTaskStartDateTime() != null && listTaskConversation.getTaskEndDateTime() != null) {
                    VideoCallDataBase.getDB(context).updateaccept("update taskDetailsInfo set msgstatus=9 where taskId='" + bean.getId() + "' and mimeType='date'");
                    cv.put("taskDescription", "assigned");
                    cv.put("plannedStartDateTime", listTaskConversation.getTaskStartDateTime());
                    cv.put("plannedEndDateTime", listTaskConversation.getTaskEndDateTime());
                    cv.put("remainderFrequency", listTaskConversation.getRemainderDateTime());
                    if (listTaskConversation.getIsRemainderRequired() != null)
                        cv.put("isRemainderRequired", listTaskConversation.getIsRemainderRequired());
                    else
                        cv.put("isRemainderRequired", "Y");
                    cv.put("mimeType", "date");
                    cv.put("requestStatus", "assigned");
                    cv.put("taskStatus", "inprogress");
                    cv.put("msgstatus", "10");
//                    if (bean.getProjectId() != null) {
//                        signal_id = signal_id.concat(bean.getName());
//                        cv.put("signalid", signal_id);
//                        cv.put("signalid", signal_id);
//                    }
                    cv.put("reminderquotes", listTaskConversation.getRemainderQuotes());
                    if (listTaskConversation.getTimeFrequency() != null) {
                        String time_frequ = Appreference.TimeFrequencyConvertion(listTaskConversation.getTimeFrequency());
                        cv.put("timeFrequency", time_frequ);
                    }
//                    else {
//                        int time_freq = Integer.parseInt(listTaskConversation.getTimeFrequency()) / 60000;
//                        cv.put("timeFrequency", time_freq + " Minutes");
//                    }
                    if (!DuplicateChecker(signal_id.trim(), String.valueOf(bean.getId()))) {
                        Log.i("getTask-date", "signalid 2 " + signal_id.trim() + "task description " + listTaskConversation.getDescription() + "taskno " + listTaskConversation.getTaskNo());
                        Log.d("subType_getTask", "value^^  ");
                        cv.put("subType", "normal");
                        row_id = (int) db.insert("taskDetailsInfo", null, cv);
                        Appreference.printLog("sipregister", "dp insertion" + signal_id, "DEBUG", null);
                        Log.e("TaskTable", "getSignalid" + signal_id);
                    }
//                    else {
//                        Log.i("send_syn_txt", "signalid 1 " + signal_id + "task signalid " + listTaskConversation.getDescription() + "parentId " + listTaskConversation.getTaskNo());
//                        row_id = (int) db.update("taskDetailsInfo", cv, "signalid='" + signal_id + "'", null);
//                    }
                }

                //Percentage completion cmd code
                //start
                /*if ((listTaskConversation.getRequestType() != null && (listTaskConversation.getRequestType().equalsIgnoreCase("percentageCompleted") || listTaskConversation.getRequestType().equalsIgnoreCase("taskDateChangedRequest")) && listTaskConversation.getPercentageCompleted() != null)) {
                    cv.put("taskDescription", "Completed Percentage " + listTaskConversation.getPercentageCompleted() + "%");
                    cv.put("mimeType", "text");
                    cv.put("taskStatus", listTaskConversation.getTaskStatus());
                    if (!DuplicateChecker(signal_id, String.valueOf(bean.getId()))) {
                        cv.put("subType", "normal");
                        Log.i("getTask-completed", "percentage " + signal_id + "task description " + listTaskConversation.getDescription() + "taskno " + listTaskConversation.getTaskNo());
                        row_id = (int) db.insert("taskDetailsInfo", null, cv);
                        Appreference.printLog("sipregister", "dp insertion" + signal_id, "DEBUG", null);
                        Log.e("TaskTable", "getSignalid" + signal_id);
                    } else {
                        cv.put("subType", "normal");
                        Log.i("send_syn_txt", "signalid 1 " + signal_id + "task signalid " + listTaskConversation.getDescription() + "parentId " + listTaskConversation.getTaskNo());
                        row_id = (int) db.update("taskDetailsInfo", cv, "signalid='" + signal_id + "'", null);
                    }
                }*/
                //Percentage completion cmd code
                //End

                ArrayList<ListTaskConversationFiles> listTaskConversationFiles = listTaskConversation.getListTaskConversationFiles();
               /* if (listTaskConversationFiles.size() == 0) {
                    cv.put("taskDescription", listTaskConversation.getName());
                    cv.put("mimeType", "text");
                    signal_id = listTaskConversation.getSignalId();
//                    cv.put("signalid", signal_id);
                    if (!DuplicateChecker(signal_id)) {
                        cv.put("subType", "normal");
                        Log.i("getTask-date", "signalid 2 " + signal_id + "task description " + bean.getDescription() + "taskno " + bean.getTaskNo());
                        row_id = (int) db.insert("taskDetailsInfo", null, cv);
                        Appreference.printLog("sipregister", "dp insertion" + signal_id, "DEBUG", null);
                        Log.e("TaskTable", "getSignalid" + signal_id);
                    } else {
                        cv.put("subType", "normal");
                        Log.i("send_syn_txt", "signalid 1 " + signal_id + "task signalid " + bean.getDescription() + "parentId " + bean.getTaskNo());
                        row_id = (int) db.update("taskDetailsInfo", cv, "signalid='" + signal_id + "'", null);
                    }
                }*/
                Log.i("send_syn_txt", "listTaskConversationFiles.size  ----> " + listTaskConversationFiles.size());
                if (listTaskConversationFiles.size() > 0) {
                    ListTaskConversationFiles listTaskConversationFile = new ListTaskConversationFiles();
                    listTaskConversationFile = listTaskConversationFiles.get(0);
                    cv.put("fromUserId", listFromDetails1.getId());
                    if (bean.getIsGroupTask() != null && bean.getIsGroupTask().equalsIgnoreCase("Y")) {
                        if (group != null && group.getId() > 0) {
                            cv.put("toUserId", group.getId());
                        }
                        if (group != null && group.getGroupName() != null) {
                            cv.put("toUserName", group.getGroupName());
                        }
//                        cv.put("taskReceiver", group.getGroupName());
                    } else {
                        if (bean.getStatus() != null && !bean.getStatus().equalsIgnoreCase("draft")) {
                            if (listToDetails1 != null) {
                                cv.put("toUserId", listToDetails1.getId());
                                cv.put("toUserName", listToDetails1.getUsername());
                            }
                        }
//                        cv.put("taskReceiver", listToDetails1.getUsername());
                    }
//                cv.put("toUserId", listToDetails1.getId());
//                    cv.put("signalid", signal_id);
                    if (listTaskConversationFile.getFileType().equals("text") || listTaskConversationFile.getFileType().equals("map") || listTaskConversationFile.getFileType().equals("date") || listTaskConversationFile.getFileType().equals("Reassign") || listTaskConversationFile.getFileType().equals("assigntask")) {
                        if (bean.getProjectId() != null) {
//                            signal_id = signal_id.concat(listTaskConversationFile.getDescription());
//                            cv.put("signalid", signal_id);
                            if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("percentageCompleted") && listTaskConversationFile.getFileType().equals("map")) {
                                cv.put("taskDescription", listTaskConversationFile.getDescription());
                                cv.put("subType", listTaskConversation.getRequestType());
                            } else if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDateChangedRequest") && listTaskConversationFile.getFileType().equals("map")) {
                                cv.put("taskDescription", listTaskConversationFile.getDescription());
                                cv.put("subType", listTaskConversation.getRequestType());
                            } else if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDescription") && listTaskConversationFile.getFileType().equals("map")) {
                                cv.put("taskDescription", listTaskConversationFile.getDescription());
                                cv.put("subType", listTaskConversation.getRequestType());
                            } else {
                                if (listTaskConversationFile.getDescription() != null && !listTaskConversationFile.getDescription().equalsIgnoreCase("")) {
                                    cv.put("taskDescription", listTaskConversationFile.getDescription());
                                }
                            }
                        } else {
                            if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("percentageCompleted") && listTaskConversationFile.getFileType().equals("map")) {
                                cv.put("taskDescription", listTaskConversationFile.getDescription());
                                cv.put("subType", listTaskConversation.getRequestType());
                            } else if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDateChangedRequest") && listTaskConversationFile.getFileType().equals("map")) {
                                cv.put("taskDescription", listTaskConversationFile.getDescription());
                                cv.put("subType", listTaskConversation.getRequestType());
                            } else if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDescription") && listTaskConversationFile.getFileType().equals("map")) {
                                cv.put("taskDescription", listTaskConversationFile.getDescription());
                                cv.put("subType", listTaskConversation.getRequestType());
                            } else {
                                if (listTaskConversationFile.getDescription() != null && !listTaskConversationFile.getDescription().equalsIgnoreCase("")) {
                                    cv.put("taskDescription", listTaskConversationFile.getDescription());
                                }
                            }
                        }
                    } else {
                        Log.i("send_syn_txt", " taskdescription " + listTaskConversationFile.getDescription());
                        if (bean.getIsGroupTask() != null && bean.getIsGroupTask().equalsIgnoreCase("Y")) {
                            String path_1 = Environment.getExternalStorageDirectory() + "/High Message/downloads/" + String.valueOf(listTaskConversationFile.getFileName());
                            if (bean.getProjectId() != null) {
                                if (listFromDetails1.getUsername() != null && listFromDetails1.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("percentageCompleted")) {
                                        cv.put("taskDescription", path_1);
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDateChangedRequest")) {
                                        cv.put("taskDescription", path_1);
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDescription")) {
                                        cv.put("taskDescription", path_1);
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else {
                                        cv.put("taskDescription", path_1);
                                    }
//                                    signal_id = signal_id.concat(listTaskConversationFile.getFileName());
//                                    cv.put("signalid", signal_id);
                                } else {
                                    if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("percentageCompleted")) {
                                        cv.put("taskDescription", listTaskConversationFile.getFileName());
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDateChangedRequest")) {
                                        cv.put("taskDescription", listTaskConversationFile.getFileName());
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDescription")) {
                                        cv.put("taskDescription", listTaskConversationFile.getFileName());
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else {
                                        cv.put("taskDescription", listTaskConversationFile.getFileName());
                                    }
//                                    signal_id = signal_id.concat(listTaskConversationFile.getFileName());
//                                    cv.put("signalid", signal_id);
                                }
                            } else {
                                if (listFromDetails1.getUsername() != null && listFromDetails1.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("percentageCompleted")) {
                                        cv.put("taskDescription", path_1);
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDateChangedRequest")) {
                                        cv.put("taskDescription", path_1);
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDescription")) {
                                        cv.put("taskDescription", path_1);
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else {
                                        cv.put("taskDescription", path_1);
                                    }
                                } else {
                                    if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("percentageCompleted")) {
                                        cv.put("taskDescription", listTaskConversationFile.getFileName());
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDateChangedRequest")) {
                                        cv.put("taskDescription", listTaskConversationFile.getFileName());
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDescription")) {
                                        cv.put("taskDescription", listTaskConversationFile.getFileName());
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else {
                                        cv.put("taskDescription", listTaskConversationFile.getFileName());
                                    }
                                }
                            }
                            cv.put("serverFileName", listTaskConversationFile.getFileName());
                            Log.i("send_syn_txt", "8 " + listTaskConversationFile.getFileName());
                        } else {
                            String path_1 = Environment.getExternalStorageDirectory() + "/High Message/downloads/" + String.valueOf(listTaskConversationFile.getFileName());
                            if (bean.getProjectId() != null) {
                                if (listFromDetails1.getUsername() != null && listFromDetails1.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("percentageCompleted")) {
                                        cv.put("taskDescription", path_1);
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDateChangedRequest")) {
                                        cv.put("taskDescription", path_1);
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDescription")) {
                                        cv.put("taskDescription", path_1);
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else {
                                        cv.put("taskDescription", path_1);
                                    }
//                                    signal_id = signal_id.concat(listTaskConversationFile.getFileName());
//                                    cv.put("signalid", signal_id);
                                } else {
                                    if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("percentageCompleted")) {
                                        cv.put("taskDescription", listTaskConversationFile.getFileName());
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDateChangedRequest")) {
                                        cv.put("taskDescription", listTaskConversationFile.getFileName());
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDescription")) {
                                        cv.put("taskDescription", listTaskConversationFile.getFileName());
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else {
                                        cv.put("taskDescription", listTaskConversationFile.getFileName());
                                    }
//                                    signal_id = signal_id.concat(listTaskConversationFile.getFileName());
//                                    cv.put("signalid", signal_id);
                                }
                            } else {
                                if (listFromDetails1.getUsername() != null && listFromDetails1.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("percentageCompleted")) {
                                        cv.put("taskDescription", path_1);
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDateChangedRequest")) {
                                        cv.put("taskDescription", path_1);
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDescription")) {
                                        cv.put("taskDescription", path_1);
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else {
                                        cv.put("taskDescription", path_1);
                                    }
                                } else {
                                    if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("percentageCompleted")) {
                                        cv.put("taskDescription", listTaskConversationFile.getFileName());
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDateChangedRequest")) {
                                        cv.put("taskDescription", listTaskConversationFile.getFileName());
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDescription")) {
                                        cv.put("taskDescription", listTaskConversationFile.getFileName());
                                        cv.put("subType", listTaskConversation.getRequestType());
                                    } else {
                                        cv.put("taskDescription", listTaskConversationFile.getFileName());
                                    }
                                }
                            }
                            cv.put("serverFileName", listTaskConversationFile.getFileName());
                        }
                    }
//                    cv.put("isRemainderRequired", bean.getIsRemainderRequired());
//                    cv.put("taskStatus", listTaskConversation.getTaskStatus());
                    Log.i("send_syn_txt", "signalid 3 " + bean.getSignalId() + "task signalid " + signal_id + "parentId " + listTaskConversation.getParentId());
                    cv.put("fromUserName", listFromDetails1.getUsername());
//                cv.put("toUserName", listToDetails1.getUsername());
                    cv.put("sendStatus", "0");
                    if (bean.getCompletedPercentage() != null)
                        cv.put("completedPercentage", bean.getCompletedPercentage());
//                    cv.put("taskType", "Individual");
//                    cv.put("ownerOfTask", listFromDetails1.getUsername());
                    cv.put("mimeType", listTaskConversationFile.getFileType());

                    if (bean.getTaskPriority() != null && bean.getTaskPriority().equals(2))
                        cv.put("taskPriority", "High");
                    else if (bean.getTaskPriority() != null && bean.getTaskPriority().equals(0))
                        cv.put("taskPriority", "Low");
                    else
                        cv.put("taskPriority", "Medium");
//                    cv.put("dateFrequency", bean.getDateFrequency());
//                    cv.put("timeFrequency", bean.getTimeFrequency());
                    cv.put("taskId", bean.getId());
                    cv.put("remark", listTaskConversation.getRemark());
//                    cv.put("reminderquotes", listTaskConversation.getRemainderQuotes());
                    cv.put("showprogress", "0");
                    cv.put("readStatus", "0");
                    if (listTaskConversationFile.getFileType() != null && listTaskConversationFile.getFileType().equalsIgnoreCase("date")) {
                        cv.put("msgstatus", "10");
                    } else {
                        cv.put("msgstatus", "1");
                    }
//                cv.put("taskReceiver", listToDetails1.getUsername());
                    Log.i("listall", " conversation date time to db entries " + bean.getCreatedDate() + " listTaskConversationFile.getDescription() " + listTaskConversationFile.getDescription());
//                    cv.put("dateTime", listTaskConversation.getCreatedDate());
//                    cv.put("taskTime", listTaskConversation.getCreatedDate());
                    if (!DuplicateChecker(signal_id, String.valueOf(bean.getId()))) {
                        Log.i("send_syn_txt", "signalid 2 " + signal_id + "task description " + listTaskConversation.getDescription() + "taskno " + listTaskConversation.getTaskNo());
                        if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDescription")) {
                            cv.put("subType", "taskDescription");
                        } else {
                            cv.put("subType", "normal");
                        }
                        row_id = (int) db.insert("taskDetailsInfo", null, cv);
                        Appreference.printLog("sipregister", "dp insertion" + signal_id, "DEBUG", null);
                        Log.e("TaskTable", "getSignalid" + signal_id);
                    } else {
                        if (listTaskConversation.getRequestType() != null && listTaskConversation.getRequestType().equalsIgnoreCase("taskDescription")) {
                            cv.put("subType", "taskDescription");
                        } else {
                            cv.put("subType", "normal");
                        }
                        Log.i("send_syn_txt", "signalid 1 " + signal_id + "task signalid " + listTaskConversation.getDescription() + "parentId " + listTaskConversation.getTaskNo());
                        row_id = (int) db.update("taskDetailsInfo", cv, "signalid='" + signal_id + "'", null);
                    }
                    Log.i("TaskTable", "Insert");
//                row_id = (int) db.insert("taskDetailsInfo", null, cv);
                    downloads(listTaskConversationFile);
                }

            }

            // fromUserName varchar(100),toUserName varchar(100),sendStatus varchar(100))  completedPercentage varchar(100),taskType varchar(100),ownerOfTask varchar(100),mimeType varchar(100))";";
//,dateFrequency varchar(100),timeFrequency varchar(100),taskId varchar(100))
/*            if (isAgendaRecordExists("select * from taskDetailsInfo where taskNo ='" + bean.getTaskNo() + "' and loginuser='" + logginuser + "'")) {
                Log.i("TaskTable", "UpdateQuery");
                row_id = (int) db.update("taskDetailsInfo", cv, "taskNo ='" + bean.getTaskNo() + "' and loginuser='" + logginuser + "'", null);
            } else {*/

//            Log.i("TaskTable", "Insert");
//            row_id = (int) db.insert("taskDetailsInfo", null, cv);
//            }
//            else
//            {
//                ContentValues cv = new ContentValues();
//                cv.put("taskStatus", bean.getTaskStatus());
//                Log.i("task","status"+bean.getTaskStatus());
//                row_id=(int) db.update("taskDetailsInfo",cv ,"taskId="+bean.getTaskId(),null);
//
//            }

//            row_id = (int) db.update("taskDetailsInfo",taskStatus,
// cv, "sendStatus ='1'", null);
//            }
        } catch (
                Exception e
                )

        {
            // TODO: handle exception
            e.printStackTrace();
        }
        return row_id;
    }

    public int insertORupdate_ListAllTaskDetails(ListAllTaskBean bean) {
        int row_id = 0;
        boolean inserted = false;
        try {

//            String listAlluser = Appreference.listAlluserDetails.getEmail();
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            ContentValues cv1 = new ContentValues();
            ContentValues cv2 = new ContentValues();
            cv.put("loginuser", Appreference.loginuserdetails.getEmail());

            Log.i("listAllMyTask", "Insert list task ");

            From from = new From();
            from = bean.getFrom();
            ToUser toUser = new ToUser();
            toUser = bean.getToUser();
            List<ListObserver> listObservers;
            listObservers = bean.getListObserver();
            List<ListTaskFile> listTaskFile;
            listTaskFile = bean.getListTaskFiles();
            Log.i("###", "task id " + bean.getId());
            cv.put("taskId", bean.getId());
            cv1.put("taskId", bean.getId());
            cv2.put("taskId", bean.getId());

            if (listObservers.size() > 0) {
                ListObserver listObserver = listObservers.get(0);
                cv.put("toUserId", listObserver.getId());
                cv2.put("toUserId", listObserver.getId());

                Log.d("listAllMyTask", "value1  " + listObserver.getUsername());
            }

            cv.put("projectId", bean.getProjectId());
            cv2.put("projectId", bean.getProjectId());
            cv.put("taskDescription", bean.getDescription());
            cv2.put("taskDescription", bean.getDescription());
            cv.put("signalId", bean.getSignalId());
//            cv.put("taskName", bean.getName());
            cv1.put("taskDescription", bean.getDescription());
            cv1.put("signalId", bean.getSignalId());
            cv1.put("taskName", bean.getName());
            cv2.put("taskName", bean.getName());
            if (bean.getTaskCategory() != null && bean.getTaskCategory().equalsIgnoreCase("taskCreation")) {
                if (bean.getStatus() != null && bean.getStatus().equalsIgnoreCase("draft")) {
                    cv1.put("category", "Template");
                } else {
                    cv1.put("category", "Task");
                }
            } else if (bean.getTaskCategory() != null && bean.getTaskCategory().equalsIgnoreCase("issue")) {
                cv1.put("category", "issue");
            } else if (bean.getTaskCategory() != null && bean.getTaskCategory().equalsIgnoreCase("note")) {
                cv1.put("category", "note");
            } else if (bean.getTaskCategory() != null && bean.getTaskCategory().equalsIgnoreCase("chat")) {
                cv1.put("category", "chat");
            }
            Log.i("VideoCallDB", "insertORupdate_ListAllTaskDetails 1 " + bean.getParentTask());
            cv1.put("parentTaskId", bean.getParentTask());
            Group group = null;
            if (bean.getGroup() != null) {
                Log.i("listAllMyTask", "value bean.getId() " + bean.getId());
                group = bean.getGroup();
            }
            Log.i("listAllMyTask", "value bean.getGroup() " + bean.getGroup());

            cv.put("fromUserId", from.getId());
            cv2.put("fromUserId", from.getId());
            cv.put("fromUserName", from.getUsername());
            cv2.put("fromUserName", from.getUsername());
//            cv.put("ownerOfTask", from.getUsername());
            cv1.put("ownerOfTask", from.getUsername());
            cv2.put("ownerOfTask", from.getUsername());
            cv.put("loginuser", Appreference.loginuserdetails.getEmail());
            cv1.put("loginuser", Appreference.loginuserdetails.getEmail());
            cv2.put("loginuser", Appreference.loginuserdetails.getEmail());
            Log.d("listAllMyTask", "value2  " + bean.getDescription());

            if (group != null) {
                cv.put("toUserId", group.getId());
                cv2.put("toUserId", group.getId());
                cv.put("toUserName", group.getGroupName());
                cv2.put("toUserName", group.getGroupName());
//                cv.put("taskReceiver", group.getGroupName());
                cv.put("taskDescription", group.getDescription());
                cv1.put("taskDescription", group.getDescription());
                cv2.put("taskDescription", group.getDescription());
                cv1.put("taskReceiver", group.getGroupName());
                cv2.put("taskReceiver", group.getGroupName());

                List<ListMembers> listMembers;
                listMembers = group.getListMember();
                String listMember = "";
                for (int j = 0; j < listMembers.size(); j++) {
                    listMember = listMember.concat(listMembers.get(j).getUsername() + ",");
                }
                listMember = listMember.substring(0, listMember.length() - 1);
//                cv.put("taskMemberList", listMember);
                cv1.put("taskMemberList", listMember);
                cv2.put("taskMemberList", listMember);
                cv.put("loginuser", Appreference.loginuserdetails.getEmail());
                cv1.put("loginuser", Appreference.loginuserdetails.getEmail());
                cv2.put("loginuser", Appreference.loginuserdetails.getEmail());
                cv.put("taskType", "Group");
                cv1.put("taskType", "Group");
                cv2.put("taskType", "Group");
                Log.i("listAllMyTask", "value taskMemberList  " + listMember);
            } else {
                Log.i("listAllMyTask", "toUser " + toUser);
                if (toUser != null) {
                    cv.put("toUserId", toUser.getId());
                    cv2.put("toUserId", toUser.getId());
                    cv.put("toUserName", toUser.getUsername());
                    cv2.put("toUserName", toUser.getUsername());
//                    cv.put("taskReceiver", toUser.getUsername());
                    cv.put("taskType", "Individual");
                    cv2.put("taskType", "Individual");
                    cv1.put("taskReceiver", toUser.getUsername());
                    cv2.put("taskReceiver", toUser.getUsername());
                    cv1.put("taskType", "Individual");
                    cv2.put("taskType", "Individual");
                }
            }
            Log.d("listAllMyTask", "value3  " + bean.getDescription());

//            cv.put("taskNo", bean.getTaskNo());
            cv.put("plannedStartDateTime", String.valueOf(bean.getPlannedStartDateTime()));
            cv1.put("taskNo", bean.getTaskNo());
            cv2.put("taskNo", bean.getTaskNo());
           /* if (bean.getTaskCategory() != null && bean.getTaskCategory().equalsIgnoreCase("taskCreation")) {
                cv.put("category", "Task");
            } else {
                cv.put("category", "issue");
            }*/
            cv1.put("plannedStartDateTime", String.valueOf(bean.getPlannedStartDateTime()));
            if (bean.getPlannedEndDateTime().contains("/") || bean.getPlannedEndDateTime().equalsIgnoreCase("NA")) {
                cv.put("plannedEndDateTime", "");
                cv1.put("plannedEndDateTime", "");
                cv2.put("plannedEndDateTime", "");
                Log.d("listAllMyTask", "value plannedEndDateTime inside");
            } else {
                cv.put("plannedEndDateTime", bean.getPlannedEndDateTime());
                cv1.put("plannedEndDateTime", bean.getPlannedEndDateTime());
                cv2.put("plannedEndDateTime", bean.getPlannedEndDateTime());
                Log.d("listAllMyTask", "value plannedEndDateTime inside else");
            }
//            cv.put("plannedEndDateTime", bean.getPlannedEndDateTime());
            cv1.put("remainderFrequency", "");
            cv.put("taskDescription", bean.getDescription());
            cv1.put("taskDescription", bean.getDescription());
            cv2.put("taskDescription", bean.getDescription());
            cv.put("taskStatus", bean.getStatus());
            cv1.put("taskStatus", bean.getStatus());
            cv2.put("taskStatus", bean.getStatus());
            cv.put("remainderFrequency", "");
            cv.put("isRemainderRequired", bean.getIsRemainderRequired());
            Log.i("task", "signalid" + bean.getSignalId() + "task signalid" + bean.getSignalId() + "parentId" + bean.getParentId());
            cv.put("signalid", bean.getSignalId());
            cv1.put("signalid", bean.getSignalId());
            cv2.put("signalid", bean.getSignalId());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateforrow = dateFormat.format(new Date());
            cv.put("dateTime", dateforrow);
            cv.put("taskTime", dateforrow);
            cv1.put("dateTime", dateforrow);
            cv1.put("taskTime", dateforrow);
            Log.i("listAllMyTask", "value dateTime " + dateforrow);
            cv.put("msgstatus", "1");
//            cv.put("customTagVisible", "1");
//
//
//            cv.put("syncEnable", "enable");
//            cv.put("showprogress", "120");
            cv.put("customTagVisible", "1");
            cv.put("syncEnable", "enable");

//            cv.put("fromUserName", from.getUsername());
//            cv.put("toUserName", toUser.getUsername());
//            cv.put("sendStatus", "0");
            cv1.put("completedPercentage", bean.getCompletedPercentage());
            cv2.put("completedPercentage", bean.getCompletedPercentage());
//            cv.put("ownerOfTask", from.getUsername());
            if (String.valueOf(bean.getTaskPriority()).equals(2))
                cv.put("taskPriority", "High");
            else if (String.valueOf(bean.getTaskPriority()).equals(0))
                cv.put("taskPriority", "Low");
            else
                cv.put("taskPriority", "Medium");


            if (0 < listTaskFile.size()) {
                ListTaskFile listTaskFiles = listTaskFile.get(0);
//                cv.put("taskId", listTaskFiles.getId());

                User user = new User();
                if (listTaskFile.size() > 0) {
                    user = listTaskFile.get(0).getUser();
                }
                cv.put("mimeType", listTaskFiles.getFileType());
                cv1.put("mimeType", listTaskFiles.getFileType());
                cv2.put("mimeType", listTaskFiles.getFileType());
                cv.put("fromUserId", user.getId());
                cv.put("loginuser", Appreference.loginuserdetails.getEmail());
                if (!listTaskFiles.getFileType().equalsIgnoreCase("text")) {
                    if (bean.getProjectId() != null) {
                        if (from.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            String path_1 = Environment.getExternalStorageDirectory() + "/High Message/downloads/" + String.valueOf(listTaskFiles.getFileName());
                            cv.put("taskDescription", path_1);
//                            cv.put("signalid", bean.getSignalId().concat(String.valueOf(listTaskFiles.getFileName())));
                            cv.put("signalid", bean.getSignalId());
                        } else {
                            cv.put("taskDescription", String.valueOf(listTaskFiles.getFileName()));
//                            cv.put("signalid", bean.getSignalId().concat(String.valueOf(listTaskFiles.getFileName())));
                            cv.put("signalid", bean.getSignalId());
                        }
                        Log.i("videocallDB", "signal listallmyTask is " + bean.getSignalId().concat(String.valueOf(listTaskFiles.getFileName())));
                        cv.put("serverFileName", String.valueOf(listTaskFiles.getFileName()));
                    } else {
                        if (from.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            String path_1 = Environment.getExternalStorageDirectory() + "/High Message/downloads/" + String.valueOf(listTaskFiles.getFileName());
                            cv.put("taskDescription", path_1);
                            cv1.put("taskDescription", path_1);
                            cv.put("serverFileName", String.valueOf(listTaskFiles.getFileName()));
                        } else {
                            cv.put("taskDescription", String.valueOf(listTaskFiles.getFileName()));
                            cv1.put("taskDescription", String.valueOf(listTaskFiles.getFileName()));
                            cv.put("serverFileName", String.valueOf(listTaskFiles.getFileName()));
                        }
                    }
                } else {
                    cv.put("taskDescription", listTaskFiles.getDescription());
                    cv1.put("taskDescription", listTaskFiles.getDescription());
                    if (listTaskFiles.getDescription() != null && !listTaskFiles.getDescription().equalsIgnoreCase("") && !listTaskFiles.getDescription().equalsIgnoreCase(null)) {
                        cv2.put("taskDescription", listTaskFiles.getDescription());
                    } else {
                        cv2.put("taskDescription", bean.getName());
                    }
                    if (bean.getProjectId() != null) {
                      /*  if (from.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            cv.put("taskDescription", listTaskFiles.getDescription());
                            cv.put("signalid", bean.getSignalId().concat(listTaskFiles.getDescription()));
                        } else {*/

//                        cv.put("signalid", bean.getSignalId().concat(listTaskFiles.getDescription()));
                        cv.put("signalid", bean.getSignalId());
//                        }
                    }
                }
                Log.d("listAllMyTask", "value4  " + user.getUsername());

//                Log.i("TaskTable", "Insert first entry");
//                row_id = (int) db.insert("taskDetailsInfo", null, cv);

                if (bean.getProjectId() != null) {
                    if (!DuplicateProjectTaskIdChecker(String.valueOf(bean.getId()))) {
                        row_id = (int) db.insert("projectHistory", null, cv2);
                        Appreference.printLog("sipregister", "projectHistory dp insertion" + bean.getSignalId(), "DEBUG", null);
                        Log.e("TaskTable", "getSignalid" + bean.getSignalId());
                        Log.i("TaskTable", "Insert projectHistory first entry 1");
                    } else {
                        row_id = (int) db.update("projectHistory", cv2, "taskId ='" + bean.getId() + "'", null);
                        Log.i("TaskTable", "Insert projectHistory first entry while duplicate ");
                        Log.d("listAllMyTask", "projectHistory db values 1 " + bean.getDescription() + "" + bean.getId());
                    }
                } else {
                   /* if (!DuplicateChecker(bean.getSignalId())) {
                        row_id = (int) db.insert("taskDetailsInfo", null, cv);
                        Appreference.printLog("sipregister", "dp insertion" + bean.getSignalId(), "DEBUG", null);
                        Log.e("TaskTable", "getSignalid" + bean.getSignalId());

                        Log.d("listAllMyTask", "db values " + bean.getDescription() + "" + bean.getId());
//                    Log.d("listAllMyTask", "db values user " + from.getId() + "" + toUser.getId());

                        Log.i("TaskTable", "Insert first entry 1");
                        inserted = true;
                    } else {
                        row_id = (int) db.update("taskDetailsInfo", cv, "signalid ='" + bean.getSignalId() + "'", null);
                        Log.i("TaskTable", "Insert first entry while duplicate ");

                        Log.d("listAllMyTask", "db values 1 " + bean.getDescription() + "" + bean.getId());
//                    Log.d("listAllMyTask", "db values user 1  " + from.getId() + "" + toUser.getId());
                        inserted = false;
                    }*/
                    if (!DuplicateTaskIdChecker("select taskId from taskHistoryInfo where taskId='" + bean.getId() + "'  ")) {
                        row_id = (int) db.insert("taskHistoryInfo", null, cv1);
                        Appreference.printLog("sipregister", "dp insertion" + bean.getId(), "DEBUG", null);
                        Log.e("TaskTable", "getSignalid" + bean.getSignalId());
                        Log.e("TaskHistoryTable", "if method getTaskId" + bean.getId());
//                    inserted = true;
                    } else {
                        row_id = (int) db.update("taskHistoryInfo", cv1, "taskId ='" + bean.getId() + "'", null);
//                row_id = (int) db.update("taskDetailsInfo", cv, "sendStatus ='1'", null);
//                    inserted = false;
                        Log.e("TaskHistoryTable", "else method getTaskId" + bean.getId());
                    }
                }
                if (bean.getProjectId() != null) {
                    Log.d("listAllMyTask", "projectHistory not updated");
                } else {
                 /*   if (!DuplicateTaskIdChecker("select taskId from taskHistoryInfo where taskId='" + bean.getId() + "'  ")) {
                        row_id = (int) db.insert("taskHistoryInfo", null, cv1);
                        Appreference.printLog("sipregister", "dp insertion" + bean.getId(), "DEBUG", null);
                        Log.e("TaskTable", "getSignalid" + bean.getSignalId());
                        Log.e("TaskHistoryTable", "if method getTaskId" + bean.getId());
//                    inserted = true;
                    } else {
                        row_id = (int) db.update("taskHistoryInfo", cv1, "taskId ='" + bean.getId() + "'", null);
//                row_id = (int) db.update("taskDetailsInfo", cv, "sendStatus ='1'", null);
//                    inserted = false;
                        Log.e("TaskHistoryTable", "else method getTaskId" + bean.getId());
                    }*/
                }

                if (listTaskFiles.getFileName() != null) {
                    ListTaskConversationFiles file1 = new ListTaskConversationFiles();
                    Log.i("taskfiles", "value FileType123 " + listTaskFiles.getFileType());
                    file1.setFileType(listTaskFiles.getFileType());
                    Log.i("taskfiles", "value FileName " + listTaskFiles.getFileName());
                    file1.setFileName(String.valueOf(listTaskFiles.getFileName()));
                    downloads(file1);
                }
            }
            Log.d("listAllMyTask", "inserted");

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return row_id;
    }


    public void insertProject_Details(ProjectDetailsBean projectDetailsBean) {
        try {
            int row_id = 0;
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            cv.put("loginuser", Appreference.loginuserdetails.getEmail());
            cv.put("projectId", projectDetailsBean.getId());
            cv.put("projectName", projectDetailsBean.getProjectName());
            cv.put("projectDescription", projectDetailsBean.getDescription());
            ProjectOrganisationBean projectOrganisationBean = projectDetailsBean.getOrganisation();
//            Log.i("Response", "projectDetailsBean projectOrganisationBean.getName() " + projectOrganisationBean.getName());
            if(projectOrganisationBean!=null)
            cv.put("projectOrganisation", projectOrganisationBean.getName());
            ListMember listMember1 = projectDetailsBean.getProjectOwner();
            if(listMember1!=null)
            cv.put("projectOwner", listMember1.getUsername());
            ArrayList<String> grouplist_2 = VideoCallDataBase.getDB(context).selectGroupmembers("select * from contact", "username");
            if (grouplist_2.size() > 0) {
                if (listMember1!=null && !listMember1.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                    if (!grouplist_2.contains(listMember1.getUsername())) {
                        ListMember listMember = new ListMember();
                        listMember.setEmail(listMember1.getEmail());
                        listMember.setId(listMember1.getId());
                        listMember.setUsername(listMember1.getUsername());
                        listMember.setFirstName(listMember1.getFirstName());
                        listMember.setLastName(listMember1.getLastName());
                        listMember.setCode(listMember1.getCode());
                        listMember.setGender(listMember1.getGender());
                        listMember.setProfileImage(listMember1.getProfileImage());
//                        listMember.setPersonalInfo(listMember1.getPersonalInfo());
                        listMember.setPersonalInfo("project_yes");
                        VideoCallDataBase.getDB(context).insertNewContact_history(listMember, Appreference.loginuserdetails.getUsername());
                    }
                }
            }
            cv.put("completedPercentage", projectDetailsBean.getProjectCompletedPercentage());
            int project_unReadMsg_count = VideoCallDataBase.getDB(context).getProjectsUnReadMsgCount(projectDetailsBean.getId());
            cv.put("readStatus", project_unReadMsg_count);
            ArrayList<ListMember> listMembers;
            listMembers = projectDetailsBean.getListMemberProject();
            if (listMembers!=null && listMembers.size() > 0) {
                ListMember listMember_1 = listMembers.get(0);
//                cv.put("", listMember_1.getUsername());
                Log.i("Response", "projectDetailsBean listMember_1.getUsername()" + listMember_1.getUsername());
            }
            ArrayList<ListAllgetTaskDetails> listAllgetTaskDetailses;
            listAllgetTaskDetailses = projectDetailsBean.getListSubTask();
            if (listAllgetTaskDetailses!=null && listAllgetTaskDetailses.size() > 0) {
                ListAllgetTaskDetails listAllgetTaskDetailses1 = listAllgetTaskDetailses.get(0);
                cv.put("taskStatus", listAllgetTaskDetailses1.getStatus());
                Log.i("Response", "projectDetailsBean listMember_1.getUsername()" + listAllgetTaskDetailses1.getCompletedPercentage());
            }

                cv.put("oracleProjectId", projectDetailsBean.getOracleProjectId());
                cv.put("customerName", projectDetailsBean.getCustomerName());
                cv.put("address", projectDetailsBean.getAddress());
                cv.put("mcModel", projectDetailsBean.getMcModel());
                cv.put("mcSrNo", projectDetailsBean.getMcSrNo());
                cv.put("serviceRequestDate", projectDetailsBean.getServiceRequestDate());
                cv.put("chasisNo", projectDetailsBean.getChasisNo());
                cv.put("observation", projectDetailsBean.getObservation());
                cv.put("oracleCustomerId", projectDetailsBean.getOracleCustomerId());
                cv.put("activity", projectDetailsBean.getActivity());
                cv.put("processFlag", projectDetailsBean.getProcessFlag());
                cv.put("projectcompletedstatus", projectDetailsBean.getProjectCompletedStatus());
                cv.put("isActiveStatus", projectDetailsBean.getIsActiveStatus());
                cv.put("jobCardType", projectDetailsBean.getJobCardType());
                cv.put("machineMake", projectDetailsBean.getMachineMake());

            if (!DuplicateProjectIdChecker(projectDetailsBean.getId())) {
                row_id = (int) db.insert("projectDetails", null, cv);
            } else {
                row_id = (int) db.update("projectDetails", cv, "projectId" + "='" + projectDetailsBean.getId() + "'", null);
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void insertProject_history(ListallProjectBean listallProjectBean) {
        try {
            int row_id = 0;
            String signal_id = null, task_id = null, owner_task = null;
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            ContentValues cv_1 = new ContentValues();
            ProjectDetailsBean projectDetailsBean=listallProjectBean.getProjectDTO();

            cv.put("loginuser", Appreference.loginuserdetails.getEmail());
            cv_1.put("loginuser", Appreference.loginuserdetails.getEmail());
            cv.put("projectId", projectDetailsBean.getId());
            cv_1.put("projectId", projectDetailsBean.getId());

            //Added For ASE
//            cv_1.put("oracleTaskId",projectDetailsBean.getOracleTaskId());
            cv_1.put("estimatedTravelHrs",projectDetailsBean.getEstimatedTravelHours());
            cv_1.put("estimatedActivityHrs",projectDetailsBean.getEstimatedActivityHrs());
            cv_1.put("activity",projectDetailsBean.getActivity());
            cv_1.put("oracleProjectId", projectDetailsBean.getOracleProjectId());
            cv_1.put("customerName", projectDetailsBean.getCustomerName());
            cv_1.put("address", projectDetailsBean.getAddress());
            cv_1.put("mcModel", projectDetailsBean.getMcModel());
            cv_1.put("mcSrNo", projectDetailsBean.getMcSrNo());
            cv_1.put("serviceRequestDate", projectDetailsBean.getServiceRequestDate());
            cv_1.put("chasisNo", projectDetailsBean.getChasisNo());
            cv_1.put("observation", projectDetailsBean.getObservation());
            cv_1.put("oracleCustomerId", projectDetailsBean.getOracleCustomerId());
            cv_1.put("activity", projectDetailsBean.getActivity());
            cv_1.put("processFlag", projectDetailsBean.getProcessFlag());
            cv_1.put("projectcompletedstatus", projectDetailsBean.getProjectCompletedStatus());
            cv_1.put("isActiveStatus", projectDetailsBean.getIsActiveStatus());
            cv_1.put("jobCardType", projectDetailsBean.getJobCardType());
            cv_1.put("machineMake", projectDetailsBean.getMachineMake());




//            cv.put("projectName", projectDetailsBean.getProjectName());
            cv_1.put("projectName", projectDetailsBean.getProjectName());
            ListMember listMember_2 = projectDetailsBean.getProjectOwner();
            cv_1.put("projectOwner", listMember_2.getUsername());
            cv.put("customTagVisible", true);
            cv.put("sendStatus", "0");
            cv.put("msgstatus", "1");
            cv.put("readStatus", "0");
            cv_1.put("readStatus", "0");
            cv.put("showprogress", "1");

            ArrayList<ListMember> listMembers;
            listMembers = projectDetailsBean.getListMemberProject();
            String pjt_members = "";

            Log.i("chat123","listMember size"+listMembers.size());
            if (listMembers.size() > 0) {
                ArrayList<String> grouplist2 = VideoCallDataBase.getDB(context).selectGroupmembers("select * from contact", "username");
                for (int i = 0; i < listMembers.size(); i++) {
                    ListMember listMember_1 = listMembers.get(i);
                    Log.i("Response", "projectDetailsBean listMember_1.getUsername()" + listMember_1.getUsername());
                    Log.i("chat123","DB contact list size"+grouplist2.size());

                    if (grouplist2.size() > 0) {
                        if (!listMember_1.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            Log.i("chat123","DB contact list notcontains new one===>"+listMember_1.getUsername());
                            Log.i("chat123","DB contact list notcontains new RoleId===>"+listMember_1.getRoleId());

                            if (!grouplist2.contains(listMember_1.getUsername())) {
                                ListMember listMember = new ListMember();
                                listMember.setEmail(listMember_1.getEmail());
                                listMember.setId(listMember_1.getId());
                                listMember.setUsername(listMember_1.getUsername());
                                listMember.setFirstName(listMember_1.getFirstName());
                                listMember.setLastName(listMember_1.getLastName());
                                listMember.setCode(listMember_1.getCode());
                                listMember.setGender(listMember_1.getGender());
                                listMember.setProfileImage(listMember_1.getProfileImage());
                                listMember.setPersonalInfo(listMember_1.getPersonalInfo());
                                listMember.setPersonalInfo("project_yes");
                                listMember.setRoleId(listMember_1.getRoleId());
                                listMember.setRoleName(listMember_1.getRoleName());
                                VideoCallDataBase.getDB(context).insertNewContact_history(listMember, Appreference.loginuserdetails.getUsername());
                            }else if(listMember_1.getRoleId()!=null && !listMember_1.getRoleId().equalsIgnoreCase(""))
                            {
                                Log.i("chat123","DB contact list already contains ===>"+listMember_1.getId());
                                Log.i("chat123","DB contact list already contains role ID ===>"+listMember_1.getRoleId());
                                VideoCallDataBase.getDB(context).contactRoleIdUpdate(listMember_1.getRoleId(),listMember_1.getRoleName(),listMember_1.getId());
                            }
                        }
                    }
                    pjt_members = pjt_members.concat(listMember_1.getUsername() + ",");
                }
//                pjt_members = pjt_members.concat(listMember_2.getUsername() + ",");
                pjt_members = pjt_members.substring(0, pjt_members.length() - 1);
            }
            ListAllgetTaskDetails parentlist;
            parentlist = projectDetailsBean.getParentTask();
            if (parentlist != null) {
                ListFromDetails listFromDetails1 = new ListFromDetails();
                listFromDetails1 = parentlist.getFrom();
                cv.put("fromUserId", listFromDetails1.getId());
                cv_1.put("fromUserId", listFromDetails1.getId());
                owner_task = listFromDetails1.getUsername();
                cv.put("fromUserName", listFromDetails1.getUsername());
                cv_1.put("fromUserName", listFromDetails1.getUsername());
//                cv.put("ownerOfTask", owner_task);
                cv_1.put("ownerOfTask", owner_task);
                cv.put("mimeType", "text");
                cv_1.put("mimeType", "text");

                ArrayList<ListFromDetails> listofobserver = new ArrayList<>();
                listofobserver = parentlist.getListObserver();
                if (listofobserver.size() > 0) {
                    String list_observer = "";
                    for (int i = 0; i < listofobserver.size(); i++) {
                        ListFromDetails listobservers = listofobserver.get(i);
                        list_observer = list_observer.concat(listobservers.getUsername() + ",");
                    }
                    list_observer = list_observer.substring(0, list_observer.length() - 1);
//                    cv.put("taskObservers", list_observer);
                    cv_1.put("taskObservers", list_observer);
                    Log.i("projectobserver", "list_observer parent task is " + list_observer);
                }

                ArrayList<String> grouplist2 = VideoCallDataBase.getDB(context).selectGroupmembers("select * from contact", "username");
                if (grouplist2.size() > 0) {
                    if (!listFromDetails1.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        if (!grouplist2.contains(listFromDetails1.getUsername())) {
                            ListMember listMember = new ListMember();
                            listMember.setEmail(listFromDetails1.getEmail());
                            listMember.setId(listFromDetails1.getId());
                            listMember.setUsername(listFromDetails1.getUsername());
                            listMember.setFirstName(listFromDetails1.getFirstName());
                            listMember.setLastName(listFromDetails1.getLastName());
                            listMember.setCode(listFromDetails1.getCode());
                            listMember.setGender(listFromDetails1.getGender());
                            listMember.setProfileImage(listFromDetails1.getProfileImage());
                            listMember.setPersonalInfo(listFromDetails1.getPersonalInfo());
                            listMember.setPersonalInfo("project_yes");
                            VideoCallDataBase.getDB(context).insertNewContact_history(listMember, Appreference.loginuserdetails.getUsername());
                        }
                    }
                }
//                cv.put("taskMemberList", pjt_members);
                cv_1.put("taskMemberList", pjt_members);
                cv.put("toUserId", parentlist.getId());
                cv_1.put("toUserId", parentlist.getId());
                cv.put("toUserName", parentlist.getName());
                cv_1.put("toUserName", parentlist.getName());
//                cv.put("taskReceiver", parentlist.getName());
                cv_1.put("taskReceiver", parentlist.getName());
//                cv.put("parentId", parentlist.getParentId());
                cv.put("taskNo", parentlist.getTaskNo());
                cv_1.put("taskNo", parentlist.getTaskNo());
                cv_1.put("isParentTask", "Y");
                if (parentlist.getPlannedStartDateTime() != null && parentlist.getPlannedEndDateTime() != null) {
                    cv.put("plannedStartDateTime", parentlist.getPlannedStartDateTime().substring(0, 19));
                    cv_1.put("plannedStartDateTime", parentlist.getPlannedStartDateTime().substring(0, 19));
                    cv.put("plannedEndDateTime", parentlist.getPlannedEndDateTime().substring(0, 19));
                    cv_1.put("plannedEndDateTime", parentlist.getPlannedEndDateTime().substring(0, 19));
                }
                if (parentlist.getRemainderDateTime() != null) {
                    cv.put("remainderFrequency", parentlist.getRemainderDateTime().substring(0, 19));
                }
                cv.put("timeFrequency", parentlist.getTimeFrequency());
                cv.put("reminderquotes", parentlist.getRemainderQuotes());
                cv.put("duration", parentlist.getDuration());
                cv.put("durationunit", parentlist.getDurationWords());
                signal_id = parentlist.getSignalId();
                Log.i("Response", "projectDetailsBean listparentdetails.getSignalId() parent " + parentlist.getSignalId());
                cv.put("signalId", parentlist.getSignalId());
                cv_1.put("signalId", parentlist.getSignalId());
//                cv.put("taskName", parentlist.getName());
                cv_1.put("taskName", parentlist.getName());
                cv.put("taskDescription", parentlist.getName());
                cv_1.put("taskDescription", parentlist.getName());
                cv.put("parentTaskId", parentlist.getId());
                Log.i("ws123","DB values under ParentTask ---------->"+parentlist.getId());

                cv_1.put("parentTaskId", parentlist.getId());
                if (parentlist.getCreatedDate() != null) {
                    String created_date = parentlist.getCreatedDate();
                    String date_time = created_date.substring(0, 19);
                    cv.put("dateTime", date_time);
                    cv.put("taskTime", date_time);
                } else {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String dateforrow = dateFormat.format(new Date());
                    Log.i("DB UTC", "UTC time for subtask " + dateforrow);
                    cv.put("dateTime", dateforrow);
                    cv.put("taskTime", dateforrow);
                }
                if (parentlist.getIsGroupTask().equalsIgnoreCase("N")) {
                    cv.put("taskType", "Individual");
                    cv_1.put("taskType", "Individual");
                } else {
                    cv.put("taskType", "Group");
                    cv_1.put("taskType", "Group");
                }
//                cv.put("mimeType", listAllgetTaskDetailses1.getCompletedPercentage());
                task_id = String.valueOf(parentlist.getId());
                cv.put("taskId", parentlist.getId());
                cv_1.put("taskId", parentlist.getId());
//                cv.put("completedPercentage", listAllgetTaskDetailses1.getCompletedPercentage());
//                cv.put("completedPercentage", listAllgetTaskDetailses1.getCompletedPercentage());
//                cv.put("completedPercentage", listAllgetTaskDetailses1.getCompletedPercentage());
//                cv.put("completedPercentage", listAllgetTaskDetailses1.getCompletedPercentage());
                if (parentlist.getCompletedPercentage() != null && !parentlist.getCompletedPercentage().equalsIgnoreCase(null) && !parentlist.getCompletedPercentage().equalsIgnoreCase("")) {
                    VideoCallDataBase.getDB(context).updateProjectCompletionDetails(projectDetailsBean.getId(), parentlist.getCompletedPercentage());
                }
                cv.put("completedPercentage", parentlist.getCompletedPercentage());
                cv_1.put("completedPercentage", parentlist.getCompletedPercentage());
                cv.put("taskStatus", parentlist.getStatus());
                cv_1.put("taskStatus", parentlist.getStatus());
                Log.i("Response", "projectDetailsBean parenttask CompletedPercentage " + parentlist.getCompletedPercentage());
                if (!DuplicateProjectTaskIdChecker(task_id)) {
                    row_id = (int) db.insert("projectHistory", null, cv_1);
                    Log.d("Response", "projectDetailsBean inside projectHistory parent");
                } else {
                    row_id = (int) db.update("projectHistory", cv_1, "taskId" + "='" + task_id + "'", null);
                    Log.d("Response", "projectDetailsBean inside else projectHistory parent");
                }
                if (!DuplicateChecker(signal_id, String.valueOf(parentlist.getId()))) {
//                    cv.put("syncEnable", "enable");
                    if (!DuplicatetaskTaskIdChecker(task_id) && !cv.get("mimeType").equals("date")) {
                        cv.put("subType", "taskDescription");
                    } else {
                        cv.put("subType", "normal");
                    }
                    row_id = (int) db.insert("taskDetailsInfo", null, cv);
                    Log.d("Response", "projectDetailsBean inside TaskHistory parent");
                } else {
                    row_id = (int) db.update("taskDetailsInfo", cv, "signalid" + "='" + signal_id + "'", null);
                    Log.d("Response", "projectDetailsBean inside else TaskHistory parent");
                }
//                else {
//                    Log.d("Response", "taskDetailsInfo inside else TaskHistory parent");
//                }

            }

            ArrayList<ListAllgetTaskDetails> listAllgetTaskDetailses;

            listAllgetTaskDetailses = projectDetailsBean.getListSubTask();
//            listAllgetTaskDetailses = listallProjectBean.getDto().getListSubTask();
            Log.i("ws123", "****** DB  projectHistory getListSubTask " + listAllgetTaskDetailses.size());

            if (listAllgetTaskDetailses.size() > 0) {
                for (int i = 0; i < listAllgetTaskDetailses.size(); i++) {
                    boolean is_value = false;
                    String list_observer_1 = "", oracle_status = "";
//                    cv.put("taskObservers", "");
                    cv_1.put("taskObservers", "");
                    ListAllgetTaskDetails listAllgetTaskDetailses1 = listAllgetTaskDetailses.get(i);
                    ListFromDetails listFromDetails1 = new ListFromDetails();
                    listFromDetails1 = listAllgetTaskDetailses1.getFrom();
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                    taskDetailsBean.setFromUserId(String.valueOf(listFromDetails1.getId()));
                    taskDetailsBean.setFromUserName(listFromDetails1.getUsername());

                    cv.put("fromUserId", listFromDetails1.getId());
                    cv_1.put("fromUserId", listFromDetails1.getId());
                    cv.put("fromUserName", listFromDetails1.getUsername());
                    cv_1.put("fromUserName", listFromDetails1.getUsername());
//                    cv.put("ownerOfTask", owner_task);
                    cv_1.put("ownerOfTask", listFromDetails1.getUsername());

                    ArrayList<ListFromDetails> listofobserverr = new ArrayList<>();
                    listofobserverr = listAllgetTaskDetailses1.getListObserver();
                    if (listofobserverr.size() > 0) {
                        for (int j = 0; j < listofobserverr.size(); j++) {
                            ListFromDetails listobservers = listofobserverr.get(j);
                            list_observer_1 = list_observer_1.concat(listobservers.getUsername() + ",");
                        }
                        list_observer_1 = list_observer_1.substring(0, list_observer_1.length() - 1);
//                        cv.put("taskObservers", list_observer_1);
                        cv_1.put("taskObservers", list_observer_1);
                        Log.i("projectobserver", "list_observer_1 subtask task is " + list_observer_1 + " " + listAllgetTaskDetailses1.getName());
                    }

                    ArrayList<String> grouplist2 = VideoCallDataBase.getDB(context).selectGroupmembers("select * from contact", "username");
                    if (grouplist2.size() > 0) {
                        if (!listFromDetails1.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            if (!grouplist2.contains(listFromDetails1.getUsername())) {
                                ListMember listMember = new ListMember();
                                listMember.setEmail(listFromDetails1.getEmail());
                                listMember.setId(listFromDetails1.getId());
                                listMember.setUsername(listFromDetails1.getUsername());
                                listMember.setFirstName(listFromDetails1.getFirstName());
                                listMember.setLastName(listFromDetails1.getLastName());
                                listMember.setCode(listFromDetails1.getCode());
                                listMember.setGender(listFromDetails1.getGender());
                                listMember.setProfileImage(listFromDetails1.getProfileImage());
                                listMember.setPersonalInfo(listFromDetails1.getPersonalInfo());
                                listMember.setPersonalInfo("project_yes");
                                VideoCallDataBase.getDB(context).insertNewContact_history(listMember, Appreference.loginuserdetails.getUsername());
                            }
                        }
                    }
                    if (listAllgetTaskDetailses1.getIsGroupTask() != null && listAllgetTaskDetailses1.getIsGroupTask().equalsIgnoreCase("N")) {
                        cv.put("taskType", "Individual");
                        cv_1.put("taskType", "Individual");
                        ListFromDetails listToDetails1 = new ListFromDetails();
                        listToDetails1 = listAllgetTaskDetailses1.getToUser();
                        if (listToDetails1 != null && listToDetails1.getId() > 0) {
                            cv.put("toUserId", listToDetails1.getId());
                            cv_1.put("toUserId", listToDetails1.getId());
                            taskDetailsBean.setToUserId(String.valueOf(listToDetails1.getId()));
                        }
                        taskDetailsBean.setTaskType("Individual");
                        cv.put("toUserName", listToDetails1.getUsername());
                        cv_1.put("toUserName", listToDetails1.getUsername());
//                        cv.put("taskReceiver", listToDetails1.getUsername());
                        oracle_status = listToDetails1.getOracleStatus();
                        Log.i("oracle_status", "values 3 ==> " + oracle_status);
                        cv_1.put("taskReceiver", listToDetails1.getUsername());
                        cv_1.put("taskMemberList", listToDetails1.getUsername());
                        Log.i("Response", "projectDetailsBean listToDetails1.getUsername() subtask " + listToDetails1.getUsername());
                        ArrayList<String> grouplist_2 = VideoCallDataBase.getDB(context).selectGroupmembers("select * from contact", "username");
                        if (grouplist_2.size() > 0) {
                            if (listToDetails1!=null && listToDetails1.getUsername()!=null && !listToDetails1.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                if (!grouplist_2.contains(listToDetails1.getUsername())) {
                                    ListMember listMember = new ListMember();
                                    listMember.setEmail(listToDetails1.getEmail());
                                    listMember.setId(listToDetails1.getId());
                                    listMember.setUsername(listToDetails1.getUsername());
                                    listMember.setFirstName(listToDetails1.getFirstName());
                                    listMember.setLastName(listToDetails1.getLastName());
                                    listMember.setCode(listToDetails1.getCode());
                                    listMember.setGender(listToDetails1.getGender());
                                    listMember.setProfileImage(listToDetails1.getProfileImage());
                                    listMember.setPersonalInfo(listToDetails1.getPersonalInfo());
                                    listMember.setPersonalInfo("project_yes");
                                    VideoCallDataBase.getDB(context).insertNewContact_history(listMember, Appreference.loginuserdetails.getUsername());
                                }
                            }
                        }
                    } else if (listAllgetTaskDetailses1.getIsGroupTask() != null && listAllgetTaskDetailses1.getIsGroupTask().equalsIgnoreCase("Y")) {
                        cv.put("taskType", "Group");
                        cv_1.put("taskType", "Group");
                        ArrayList<ListMember> listMembers_1 = new ArrayList<>();
                        listMembers_1 = listAllgetTaskDetailses1.getListTaskToUser();
                        String pjt_members_1 = "";

                        if (listMembers_1.size() > 0) {
//                            ArrayList<String> grouplist2_1 = VideoCallDataBase.getDB(context).selectGroupmembers("select * from contact", "username");
                            for (int j = 0; j < listMembers_1.size(); j++) {
                                ListMember listMember_3 = listMembers_1.get(j);
                                Log.i("Response", "projectDetailsBean getListTaskToUser's " + listMember_3.getUsername());
                                pjt_members_1 = pjt_members_1.concat(listMember_3.getUsername() + ",");
                                if (listMember_3.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    oracle_status = listMember_3.getOracleStatus();
                                    Log.i("oracle_status", "values 2 ==> " + oracle_status);
                                }
                            }
                            pjt_members_1 = pjt_members_1.substring(0, pjt_members_1.length() - 1);
                        }
//                        cv.put("taskMemberList", pjt_members_1);
                        cv_1.put("taskMemberList", pjt_members_1);
                        cv.put("toUserId", listAllgetTaskDetailses1.getId());
                        taskDetailsBean.setTaskType("Group");
                        cv_1.put("toUserId", listAllgetTaskDetailses1.getId());
                        cv.put("toUserName", listAllgetTaskDetailses1.getName());
                        cv_1.put("toUserName", listAllgetTaskDetailses1.getName());
//                        cv.put("taskReceiver", listAllgetTaskDetailses1.getName());
                        cv_1.put("taskReceiver", listAllgetTaskDetailses1.getName());
                    } else if (listAllgetTaskDetailses1.getStatus() != null && listAllgetTaskDetailses1.getStatus().equalsIgnoreCase("draft")) {
                        cv.put("taskType", "Individual");
                        cv_1.put("taskType", "Individual");
                        cv_1.put("taskMemberList", "");
                        cv.put("toUserId", "");
                        cv_1.put("toUserId", "");
                        cv.put("toUserName", "");
                        cv_1.put("toUserName", "");
//                        cv.put("taskReceiver", listAllgetTaskDetailses1.getName());
                        cv_1.put("taskReceiver", "");
                    }
//                    cv.put("parentId", listAllgetTaskDetailses1.getParentId());
                    cv.put("taskNo", listAllgetTaskDetailses1.getTaskNo());
                    taskDetailsBean.setTaskNo(listAllgetTaskDetailses1.getTaskNo());
                    cv_1.put("taskNo", listAllgetTaskDetailses1.getTaskNo());
                    cv_1.put("isParentTask", "N");
                    if (listAllgetTaskDetailses1.getTaskCategory() != null && listAllgetTaskDetailses1.getTaskCategory().equalsIgnoreCase("taskCreation")) {
                        if (listAllgetTaskDetailses1.getStatus() != null && listAllgetTaskDetailses1.getStatus().equalsIgnoreCase("draft")) {
                            cv_1.put("category", "Template");
                        } else {
                            cv_1.put("category", "Task");
                        }
                    } else if (listAllgetTaskDetailses1.getTaskCategory() != null && listAllgetTaskDetailses1.getTaskCategory().equalsIgnoreCase("issue")) {
                        cv_1.put("category", "issue");
                    } else if (listAllgetTaskDetailses1.getTaskCategory() != null && listAllgetTaskDetailses1.getTaskCategory().equalsIgnoreCase("note")) {
                        cv_1.put("category", "note");
                    }
//                    signal_id = listAllgetTaskDetailses1.getSignalId();
                    taskDetailsBean.setSignalid(listAllgetTaskDetailses1.getSignalId());
                    Log.i("Response", "projectDetailsBean listparentdetails.getSignalId() subtask " + listAllgetTaskDetailses1.getSignalId());
//                    cv.put("signalId", listAllgetTaskDetailses1.getSignalId());
//                    cv_1.put("signalId", listAllgetTaskDetailses1.getSignalId());
//                    cv.put("taskName", listAllgetTaskDetailses1.getName());
                    cv_1.put("taskName", listAllgetTaskDetailses1.getName());
//                    cv.put("taskDescription", listAllgetTaskDetailses1.getName());
//                    cv_1.put("taskDescription", listAllgetTaskDetailses1.getName());
//                    cv.put("parentTaskId", listAllgetTaskDetailses1.getParentTask());
//                    cv_1.put("parentTaskId", listAllgetTaskDetailses1.getParentTask());
                    cv.put("parentTaskId", parentlist.getId());
                    Log.i("ws123","DB values under ParentTask 2---------->"+parentlist.getId());

                    cv_1.put("parentTaskId", parentlist.getId());
//                cv.put("mimeType", listAllgetTaskDetailses1.getCompletedPercentage());
                    task_id = String.valueOf(listAllgetTaskDetailses1.getId());
                    cv.put("taskId", listAllgetTaskDetailses1.getId());
                    taskDetailsBean.setTaskId(String.valueOf(listAllgetTaskDetailses1.getId()));
                    cv_1.put("taskId", listAllgetTaskDetailses1.getId());
                    cv.put("completedPercentage", listAllgetTaskDetailses1.getCompletedPercentage());
                    cv_1.put("completedPercentage", listAllgetTaskDetailses1.getCompletedPercentage());
                    Log.i("Response", "projectDetailsBean subtask CompletedPercentage " + listAllgetTaskDetailses1.getCompletedPercentage());
                    Log.i("oracle_status", "oracle values 1 ==> " +"task Id----->"+task_id+"===>" +oracle_status);
                    Log.i("oracle_status", "normal values 1 ==> " +"task Id----->"+task_id+"===>" +listAllgetTaskDetailses1.getStatus());
                    Log.i("oracle_status", "oracleProjectId values 1===>" +projectDetailsBean.getOracleProjectId());

                    if (projectDetailsBean.getOracleProjectId() != null && !projectDetailsBean.getOracleProjectId().equalsIgnoreCase("") && !projectDetailsBean.getOracleProjectId().equalsIgnoreCase("null")) {

                        if (oracle_status!=null && !oracle_status.equalsIgnoreCase("")) {
                            cv.put("taskStatus", oracle_status);
                            cv_1.put("taskStatus", oracle_status);
                        } else {
                            cv.put("taskStatus", listAllgetTaskDetailses1.getStatus());
                            cv_1.put("taskStatus", listAllgetTaskDetailses1.getStatus());
                        }
                    } else {
                        if (listAllgetTaskDetailses1.getStatus() != null && listAllgetTaskDetailses1.getStatus().equalsIgnoreCase("overdue")) {
                            cv.put("taskStatus", "inprogress");
                        } else {
                            cv.put("taskStatus", listAllgetTaskDetailses1.getStatus());
                        }
                        cv_1.put("taskStatus", listAllgetTaskDetailses1.getStatus());
                    }
                    cv_1.put("oracleTaskId",listAllgetTaskDetailses1.getOracleTaskId());
                    cv_1.put("estimatedTravelHrs",listAllgetTaskDetailses1.getEstimatedTravelHours());
                    if (listAllgetTaskDetailses1.getCreatedDate() != null) {
                        String created_date = listAllgetTaskDetailses1.getCreatedDate();
                        String date_time = created_date.substring(0, 19);
                        cv.put("dateTime", date_time);
                        cv.put("taskTime", date_time);
                        taskDetailsBean.setTaskUTCTime(date_time);
                        Log.i("date_time", "UTC created_date " + created_date);
                    } else {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                        String dateforrow = dateFormat.format(new Date());
                        Log.i("DB UTC", "UTC time for subtask " + dateforrow);
                        cv.put("dateTime", dateforrow);
                        cv.put("taskTime", dateforrow);
                    }
                    if (listAllgetTaskDetailses1.getTaskPriority() != null && listAllgetTaskDetailses1.getTaskPriority().equalsIgnoreCase("2")) {
                        cv.put("taskPriority", "High");
                    } else if (listAllgetTaskDetailses1.getTaskPriority() != null && listAllgetTaskDetailses1.getTaskPriority().equalsIgnoreCase("0")) {
                        cv.put("taskPriority", "Low");
                    } else {
                        cv.put("taskPriority", "Medium");
                    }
                    ArrayList<ListTaskFiles> listTaskFile;
                    listTaskFile = listAllgetTaskDetailses1.getListTaskFiles();
                    Log.i("Response", "projectDetailsBean listTaskFile.size() in subtask is " + listTaskFile.size());
                    if (listTaskFile.size() > 0) {
                        for (int k = 0; k < listTaskFile.size(); k++) {
                            ListTaskFiles listTaskFiles = listTaskFile.get(k);
                            cv.put("serverFileName", "");
//                            Log.d("videocalldatabase", "is_value MM/Text file " + is_value);
//                cv.put("taskId", listTaskFiles.getId());
//                            cv.put("taskId", listTaskFiles.getId());
                            ListFromDetails listSub_FromDetails = listTaskFiles.getUser();
                            cv.put("mimeType", listTaskFiles.getFileType());
                            cv_1.put("mimeType", listTaskFiles.getFileType());
//                            cv.put("fromUserId", user.getId());
//                            cv.put("loginuser", Appreference.loginuserdetails.getEmail());
                            if (!listTaskFiles.getFileType().equalsIgnoreCase("text")) {
                                if (listSub_FromDetails.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    String path_1 = Environment.getExternalStorageDirectory() + "/High Message/downloads/" + String.valueOf(listTaskFiles.getFileName());
                                    if (listTaskFile.size() > 1) {
                                        signal_id = listAllgetTaskDetailses1.getSignalId().concat(listTaskFiles.getFileName() + k);
                                    } else {
                                        signal_id = listAllgetTaskDetailses1.getSignalId();
                                    }
                                    cv.put("signalId", signal_id);
                                    cv_1.put("signalId", signal_id);
                                    cv.put("taskDescription", path_1);
                                    cv_1.put("taskDescription", path_1);
                                    cv.put("serverFileName", String.valueOf(listTaskFiles.getFileName()));
                                } else {
                                    if (listTaskFile.size() > 1) {
                                        signal_id = listAllgetTaskDetailses1.getSignalId().concat(listTaskFiles.getFileName() + k);
                                    } else {
                                        signal_id = listAllgetTaskDetailses1.getSignalId();
                                    }
                                    cv.put("signalId", signal_id);
                                    cv_1.put("signalId", signal_id);
                                    cv.put("taskDescription", String.valueOf(listTaskFiles.getFileName()));
                                    cv_1.put("taskDescription", String.valueOf(listTaskFiles.getFileName()));
                                    cv.put("serverFileName", String.valueOf(listTaskFiles.getFileName()));
                                }
                                if (listTaskFiles.getFileName() != null) {
                                    ListTaskConversationFiles file1 = new ListTaskConversationFiles();
                                    Log.i("taskfiles", "value FileType123 " + listTaskFiles.getFileType());
                                    file1.setFileType(listTaskFiles.getFileType());
                                    Log.i("taskfiles", "value FileName " + listTaskFiles.getFileName());
                                    file1.setFileName(String.valueOf(listTaskFiles.getFileName()));
                                    downloads(file1);
                                }
                            } else {
                                if (listTaskFile.size() > 1) {
                                    signal_id = listAllgetTaskDetailses1.getSignalId().concat(listTaskFiles.getDescription() + k);
                                } else {
                                    signal_id = listAllgetTaskDetailses1.getSignalId();
                                }
                                cv.put("signalId", signal_id);
                                cv_1.put("signalId", signal_id);
                                if (listTaskFiles.getDescription() != null && !listTaskFiles.getDescription().equalsIgnoreCase("") && !listTaskFiles.getDescription().equalsIgnoreCase(null)) {
                                    cv.put("taskDescription", listTaskFiles.getDescription());
                                    cv_1.put("taskDescription", listTaskFiles.getDescription());
                                } else {
                                    cv.put("taskDescription", listAllgetTaskDetailses1.getName());
                                    cv_1.put("taskDescription", listAllgetTaskDetailses1.getName());
                                }
                            }
                            if (listAllgetTaskDetailses1.getTaskPriority() != null && listAllgetTaskDetailses1.getTaskPriority().equalsIgnoreCase("2")) {
                                cv.put("taskPriority", "High");
                            } else if (listAllgetTaskDetailses1.getTaskPriority() != null && listAllgetTaskDetailses1.getTaskPriority().equalsIgnoreCase("0")) {
                                cv.put("taskPriority", "Low");
                            } else {
                                cv.put("taskPriority", "Medium");
                            }

                            Log.i("Response", "projectDetailsBean listMember_1.getCompletedPercentage() is " + listAllgetTaskDetailses1.getCompletedPercentage());
                            if (!DuplicateProjectTaskIdChecker(task_id)) {
                                row_id = (int) db.insert("projectHistory", null, cv_1);
                                Log.d("Response", "projectDetailsBean inside projectHistory subtask");
                            } else {
                                row_id = (int) db.update("projectHistory", cv_1, "taskId" + "='" + task_id + "'", null);
                                Log.d("Response", "projectDetailsBean inside else projectHistory subtask");
                            }
                            Log.i("Response", "projectDetailsBean signalId for subtask " + signal_id + " " + task_id);
                            if (!DuplicateChecker(signal_id, task_id)) {
//                                cv.put("syncEnable", "enable");
//                                if (!DuplicatetaskTaskIdChecker(task_id) && !cv.get("mimeType").equals("date")) {
                                cv.put("subType", "taskDescription");
                                Log.d("Response", "projectDetailsBean inside TaskHistory subtask subType is taskDescription");
//                                } else {
//                                    cv.put("subType", "normal");
//                                }
                                row_id = (int) db.insert("taskDetailsInfo", null, cv);
                                Log.d("Response", "projectDetailsBean inside TaskHistory subtask");
                            } else {
                                cv.put("subType", "taskDescription");
                                row_id = (int) db.update("taskDetailsInfo", cv, "signalid" + "='" + signal_id + "'", null);
                                Log.d("Response", "projectDetailsBean inside else TaskHistory subtask");
                            }
                        }
                    }
                    /*if (listAllgetTaskDetailses1.getDuration() != null && listAllgetTaskDetailses1.getDurationWords() != null && (listAllgetTaskDetailses1.getTimeFrequency() != null)) {
                        cv.put("duration", listAllgetTaskDetailses1.getDuration());
                        String duration_unit = "\"" + listAllgetTaskDetailses1.getDurationWords() + "\"";
                        cv.put("durationunit", duration_unit);
                        signal_id = listAllgetTaskDetailses1.getSignalId().concat(listAllgetTaskDetailses1.getName());
                        cv.put("signalId", signal_id);
                        cv_1.put("signalId", signal_id);
                        cv.put("taskDescription", "template");
                        cv_1.put("taskDescription", "template");
                        if (listAllgetTaskDetailses1.getStatus() != null && listAllgetTaskDetailses1.getStatus().equalsIgnoreCase("draft")) {
                            cv.put("mimeType", "date");
                            cv_1.put("mimeType", "date");
                        } else {
                            cv.put("mimeType", "dates");
                            cv_1.put("mimeType", "dates");
                        }
//                        cv.put("timeFrequency", listAllgetTaskDetailses1.getTimeFrequency());
                        if (listAllgetTaskDetailses1.getTimeFrequency() != null && listAllgetTaskDetailses1.getTimeFrequency().contains("Minutes")) {
                            cv.put("timeFrequency", listAllgetTaskDetailses1.getTimeFrequency());
                            taskDetailsBean.setTimeFrequency(listAllgetTaskDetailses1.getTimeFrequency());
                        } else if (listAllgetTaskDetailses1.getTimeFrequency() != null) {
                            int total_mins = Integer.parseInt(listAllgetTaskDetailses1.getTimeFrequency()) / 60000;
                            String remainder_Frequency = total_mins + " Minutes";
                            Log.i("gcm", "remainder_Frequency " + remainder_Frequency);
                            cv.put("timeFrequency", remainder_Frequency);
                            taskDetailsBean.setTimeFrequency(remainder_Frequency);
                        }
                        if (listAllgetTaskDetailses1.getRemainderQuotes() != null && !listAllgetTaskDetailses1.getRemainderQuotes().equalsIgnoreCase("")) {
                            String tempRem_quotes = "\"" + listAllgetTaskDetailses1.getRemainderQuotes() + "\"";
                            cv.put("reminderquotes", tempRem_quotes);
                        } else {
                            cv.put("reminderquotes", "\"" + "Task Reminder" + "\"");
                        }
                        if (!DuplicateProjectTaskIdChecker(task_id)) {
//                            cv_1.put("subType", "normal");
                            row_id = (int) db.insert("projectHistory", null, cv_1);
                            Log.d("Response", "projectDetailsBean inside projectHistory subtask template date");
                        } else {
//                            cv_1.put("subType", "normal");
                            row_id = (int) db.update("projectHistory", cv_1, "taskId" + "='" + task_id + "'", null);
                            Log.d("Response", "projectDetailsBean inside else projectHistory subtask template date");
                        }
                        Log.i("Response", "projectDetailsBean signalId for subtask " + signal_id + " " + task_id);
                        if (!DuplicateChecker(signal_id, task_id)) {
//                                cv.put("syncEnable", "enable");
//                            if (!DuplicatetaskTaskIdChecker(task_id) && !cv.get("mimeType").equals("date")) {
//                                cv.put("subType", "taskDescription");
//                            } else {
                            cv.put("subType", "normal");
//                            }
                            row_id = (int) db.insert("taskDetailsInfo", null, cv);
                            Log.d("Response", "projectDetailsBean inside TaskHistory subtask template date");
                        } else {
                            cv.put("subType", "normal");
                            row_id = (int) db.update("taskDetailsInfo", cv, "signalid" + "='" + signal_id + "'", null);
                            Log.d("Response", "projectDetailsBean inside else TaskHistory subtask template date");
                        }
                    }*/
                    Log.i("videocalldatabase", "is_value  taskId and listTaskFile.size() before " + listTaskFile.size());
                    if (listTaskFile.size() == 0) {
//                        if (listAllgetTaskDetailses1.getPlannedStartDateTime() == null && listAllgetTaskDetailses1.getPlannedEndDateTime() == null && listAllgetTaskDetailses1.getRemainderDateTime() == null) {
//                            if (listAllgetTaskDetailses1.getDuration() == null || listAllgetTaskDetailses1.getDurationWords() == null || (listAllgetTaskDetailses1.getTimeFrequency() == null)) {
                        signal_id = listAllgetTaskDetailses1.getSignalId();
                        cv.put("signalId", signal_id);
                        cv_1.put("signalId", signal_id);
                        cv_1.put("isParentTask", "N");
                        if (listAllgetTaskDetailses1.getDescription() != null && !listAllgetTaskDetailses1.getDescription().equalsIgnoreCase("")) {
                            cv.put("taskDescription", listAllgetTaskDetailses1.getDescription());
                            cv_1.put("taskDescription", listAllgetTaskDetailses1.getDescription());
                        } else {
                            cv.put("taskDescription", listAllgetTaskDetailses1.getName());
                            cv_1.put("taskDescription", listAllgetTaskDetailses1.getName());
                        }
                        cv.put("mimeType", "text");
                        if (!DuplicateProjectTaskIdChecker(task_id)) {
                            row_id = (int) db.insert("projectHistory", null, cv_1);
                            Log.d("Response", "projectDetailsBean inside projectHistory empty task");
                        } else {
                            row_id = (int) db.update("projectHistory", cv_1, "taskId" + "='" + task_id + "'", null);
                            Log.d("Response", "projectDetailsBean inside else projectHistory empty task");
                        }
                        Log.i("Response", "projectDetailsBean signalId for empty task " + signal_id + " " + task_id);
                    }
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Log.i("ws123","DB values Exception ---------->"+e.getMessage());

        }

    }

    public void downloads(ListTaskConversationFiles listTaskConversationFile) {
        Log.d("Check nullable", "MainActivity  == " + mainActivity);
        if (mainActivity == null) {
            mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
        }
        if (listTaskConversationFile.getFileType().equalsIgnoreCase("image") || listTaskConversationFile.getFileType().equalsIgnoreCase("sketch")) {
            Log.i("template", "fileName" + listTaskConversationFile.getFileName());
            TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
            taskDetailsBean.setMimeType(listTaskConversationFile.getFileType());
            taskDetailsBean.setTaskDescription(listTaskConversationFile.getFileName());
            mainActivity.templateImageDownload(taskDetailsBean);

        } else if (listTaskConversationFile.getFileType().equalsIgnoreCase("document") || listTaskConversationFile.getFileType().equalsIgnoreCase("video") || listTaskConversationFile.getFileType().equalsIgnoreCase("textfile") || listTaskConversationFile.getFileType().equalsIgnoreCase("video") || listTaskConversationFile.getFileType().equalsIgnoreCase("pdf") || listTaskConversationFile.getFileType().equalsIgnoreCase("txt") || listTaskConversationFile.getFileType().equalsIgnoreCase("textfile") || listTaskConversationFile.getFileType().equalsIgnoreCase("audio")) {
            Log.i("template", "fileName" + listTaskConversationFile.getFileName());
            mainActivity.templateDownloadVideo(listTaskConversationFile.getFileName());
        } else if (listTaskConversationFile.getFileType().equalsIgnoreCase("date") && (listTaskConversationFile.getDescription() != null && !listTaskConversationFile.getDescription().equalsIgnoreCase(null) && !listTaskConversationFile.getDescription().equalsIgnoreCase(""))) {
            if (listTaskConversationFile.getDescription().contains(".mp3") || listTaskConversationFile.getDescription().contains(".wav")) {
                Log.i("template", "fileName" + listTaskConversationFile.getFileName());
                mainActivity.templateDownloadVideo(listTaskConversationFile.getFileName());
            }
        }
    }

    public void serverFileNameUpdate(String FileName, String signalId) {
        //String query1 = "UPDATE " + "taskDetailsInfo" + " SET  serverFileName= '"+ FileName +"'WHERE " + "signalid" + " = " + signalId;
        Log.e("task", "serverFilenameUpdated" + FileName);
        Log.e("response", "Notes ResponceMethod serverFileNameUpdate 1 " + FileName + " * >----> * " + signalId);
        ContentValues cv = new ContentValues();
        cv.put("serverFileName", FileName);
        cv.put("showprogress", 1);
        db.update("taskDetailsInfo", cv, "signalid" + "='" + signalId + "'", null);
    }

    public void taskSendUpdate(String query, String taskNo) {

        // String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "sendStatus = '" + query + "'WHERE " + "taskNo" + " = " + taskNo;

        ContentValues cv = new ContentValues();
        cv.put("sendStatus", query);
        db.update("taskDetailsInfo", cv, "taskNo" + "= ?", new String[]{taskNo});
    }

    public void taskMsg_StatusUpdate(String signalid) {

        // String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "sendStatus = '" + query + "'WHERE " + "taskNo" + " = " + taskNo;

        ContentValues cv = new ContentValues();
        cv.put("msgstatus", "9");
        db.update("taskDetailsInfo", cv, "signalid=? and mimeType=?", new String[]{signalid, "date"});
        Log.d("task1234", "RequestStatus DB updated");
    }

    public void taskMsg_StatusOverdueUpdate(String taskId) {

        // String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "sendStatus = '" + query + "'WHERE " + "taskNo" + " = " + taskNo;

        ContentValues cv = new ContentValues();
        cv.put("msgstatus", "9");
        db.update("taskDetailsInfo", cv, "taskId=? and mimeType=?", new String[]{taskId, "overdue"});
        Log.d("task1234", "RequestStatus DB updated");
    }

    public void OverDueMsg_StatusUpdate(String signalid) {

        // String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "sendStatus = '" + query + "'WHERE " + "taskNo" + " = " + taskNo;

        ContentValues cv = new ContentValues();
        cv.put("msgstatus", "9");
        db.update("taskDetailsInfo", cv, "signalid=? and mimeType=?", new String[]{signalid, "overdue"});
        Log.d("task1234", "RequestStatus DB updated");
    }

    public void updateProjectCompletionDetails(String project_Id, String comp_percent) {

        ContentValues cv = new ContentValues();
        cv.put("completedPercentage", comp_percent);
        db.update("projectDetails", cv, "projectId" + "= ?", new String[]{project_Id});
        Log.d("projectDetails", "Completed Percentage DB updated");
    }

    public void task_LongmessageUpdate(String signalid) {

        ContentValues cv = new ContentValues();
        cv.put("longmessage", "1");
        db.update("taskDetailsInfo", cv, "signalid" + "= ?", new String[]{signalid});
        Log.d("task1234", "RequestStatus DB updated");
    }

    public void task_OverdueMsgUpdate(String signalid, String Value) {

        ContentValues cv = new ContentValues();
        cv.put("overdue_Msg", Value);
        db.update("taskDetailsInfo", cv, "signalid" + "= ?", new String[]{signalid});
        Log.d("task1234", "RequestStatus DB updated");
    }

    public void task_LongmessageUpdateForReceiver(String signalid) {

        ContentValues cv = new ContentValues();
        cv.put("longmessage", "0");
        db.update("taskDetailsInfo", cv, "signalid" + "= ?", new String[]{signalid});
        Log.d("task1234", "RequestStatus DB updated");
    }

    public void withdrawMsg_StatusUpdate(String signalid, String task_description) {

        // String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "sendStatus = '" + query + "'WHERE " + "taskNo" + " = " + taskNo;

        ContentValues cv = new ContentValues();
        cv.put("taskDescription", task_description);
        cv.put("mimeType", "text");
        db.update("taskDetailsInfo", cv, "signalid=? ", new String[]{signalid});
        Log.d("task1234", "RequestStatus DB updated");
    }

    public void GroupNameOrMemberChanges(String groupid, String grp_name) {
        ContentValues cv = new ContentValues();
        cv.put("groupname", grp_name);
        db.update("group1", cv, "groupid=? ", new String[]{groupid});
        Log.d("Groupchat", "DB ");
    }

    public void groupTask_StatusUpdate(String signalid, String task_status) {

        // String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "sendStatus = '" + query + "'WHERE " + "taskNo" + " = " + taskNo;

        ContentValues cv = new ContentValues();
        cv.put("taskStatus", task_status);
        db.update("taskDetailsInfo", cv, "signalid" + "= ?", new String[]{signalid});
        Log.d("task1234", "RequestStatus DB updated");
    }

    public void groupProject_StatusUpdate(String taskid, String task_status) {

        // String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "sendStatus = '" + query + "'WHERE " + "taskNo" + " = " + taskNo;

        ContentValues cv = new ContentValues();
        cv.put("taskStatus", task_status);
        db.update("projectHistory", cv, "taskId" + "= ?", new String[]{taskid});
        Log.d("task1234", "RequestStatus DB updated");
    }

    public void groupProject_PercentUpdate(String taskid, String percentage) {

        // String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "sendStatus = '" + query + "'WHERE " + "taskNo" + " = " + taskNo;

        ContentValues cv = new ContentValues();
        cv.put("completedPercentage", percentage);
        db.update("projectHistory", cv, "taskId" + "= ?", new String[]{taskid});
        Log.d("task1234", "completedPercentage DB updated");
    }

    public void leaveMsg_Status(String signalid) {

        // String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "sendStatus = '" + query + "'WHERE " + "taskNo" + " = " + taskNo;

        ContentValues cv = new ContentValues();
        cv.put("msgstatus", "9");
        db.update("taskDetailsInfo", cv, "signalid=? and mimeType=?", new String[]{signalid, "leaveRequest"});
        Log.d("task1234", "RequestStatus DB updated");
    }

    public void sync_status(String signalid) {

        // String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "sendStatus = '" + query + "'WHERE " + "taskNo" + " = " + taskNo;

        ContentValues cv = new ContentValues();
        cv.put("syncEnable", "disable");
        db.update("taskDetailsInfo", cv, "signalid=? ", new String[]{signalid});
        Log.d("task1234", "RequestStatus DB updated");
    }


    public void leaveMsg_Status_Send(String taskid) {

        // String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "sendStatus = '" + query + "'WHERE " + "taskNo" + " = " + taskNo;

        ContentValues cv = new ContentValues();
        cv.put("msgstatus", "9");
        db.update("taskDetailsInfo", cv, "taskId=? and mimeType=?", new String[]{taskid, "leaveRequest"});
        Log.d("task1234", "RequestStatus DB updated");
    }

    public void Leaverequestorreject(TaskDetailsBean bean) {
        int row_id = 0;
        try {
            String logginuser = Appreference.loginuserdetails.getEmail();
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();


            cv.put("loginuser", logginuser);
            cv.put("fromUserId", bean.getFromUserId());
            cv.put("toUserId", bean.getToUserId());
            cv.put("taskNo", bean.getTaskNo());
            cv.put("taskName", bean.getTaskName());
            cv.put("plannedStartDateTime", bean.getUtcPlannedStartDateTime());
            cv.put("plannedEndDateTime", bean.getUtcplannedEndDateTime());
            cv.put("remainderFrequency", bean.getUtcPemainderFrequency());
            cv.put("duration", bean.getDuration());
            cv.put("durationunit", bean.getDurationUnit());
            Log.i("task", "taskdescription value update " + bean.getTaskDescription());
            cv.put("taskDescription", bean.getTaskDescription());
            cv.put("isRemainderRequired", bean.getIsRemainderRequired());
            cv.put("taskStatus", bean.getTaskStatus());
            cv.put("signalid", bean.getSignalid());
            cv.put("fromUserName", bean.getFromUserName());
            cv.put("toUserName", bean.getToUserName());
            cv.put("sendStatus", bean.getSendStatus());
            cv.put("completedPercentage", bean.getCompletedPercentage());
            cv.put("taskType", bean.getTaskType());
            cv.put("ownerOfTask", bean.getOwnerOfTask());
            cv.put("mimeType", bean.getMimeType());
            cv.put("taskPriority", bean.getTaskPriority());
            cv.put("dateFrequency", bean.getDateFrequency());
            cv.put("timeFrequency", bean.getTimeFrequency());
            cv.put("taskId", bean.getTaskId());
            cv.put("showprogress", String.valueOf(bean.getShow_progress()));
            cv.put("readStatus", bean.getRead_status());
            cv.put("reminderquotes", bean.getReminderQuote());
            cv.put("remark", bean.getRemark());
            cv.put("tasktime", bean.getTaskUTCTime());
            cv.put("taskReceiver", bean.getTaskReceiver());
            cv.put("taskObservers", bean.getTaskObservers());
            Log.i("time", "value");
            cv.put("serverFileName", bean.getServerFileName());
            cv.put("tasktime", bean.getTaskUTCTime());
            cv.put("msgstatus", bean.getMsg_status());
            cv.put("requestStatus", bean.getRequestStatus());
            cv.put("taskMemberList", bean.getGroupTaskMembers());
            cv.put("dateTime", bean.getTaskUTCDateTime());
            cv.put("subType", bean.getSubType());
            // fromUserName varchar(100),toUserName varchar(100),sendStatus varchar(100))  completedPercentage varchar(100),taskType varchar(100),ownerOfTask varchar(100),mimeType varchar(100))";";
//,dateFrequency varchar(100),timeFrequency varchar(100),taskId varchar(100))
/*            if (isAgendaRecordExists("select * from taskDetailsInfo where taskNo ='" + bean.getTaskNo() + "' and loginuser='" + logginuser + "'")) {
                Log.i("TaskTable", "UpdateQuery");
                row_id = (int) db.update("taskDetailsInfo", cv, "taskNo ='" + bean.getTaskNo() + "' and loginuser='" + logginuser + "'", null);
            } else {*/
            Log.i("TaskTable", "Insert");
            Log.i("TaskTable", "Insert" + bean.getSignalid());
            if (!DuplicateChecker(bean.getSignalid(), bean.getTaskId())) {
                row_id = (int) db.insert("taskDetailsInfo", null, cv);
                Appreference.printLog("sipregister", "dp insertion" + bean.getSignalid(), "DEBUG", null);
                Log.e("TaskTable", "getSignalid" + bean.getSignalid());
            }

        } catch (Exception e) {

        }
    }

    public int update_Task_history(TaskDetailsBean bean) {
        int row_id = 0;
        try {
            String logginuser = Appreference.loginuserdetails.getEmail();
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            cv.put("loginuser", logginuser);
            cv.put("fromUserId", bean.getFromUserId());
            cv.put("toUserId", bean.getToUserId());
            cv.put("taskNo", bean.getTaskNo());
            cv.put("category", bean.getCatagory());
//            cv.put("issueId", bean.getIssueId());
            cv.put("taskName", bean.getTaskName());
            cv.put("plannedStartDateTime", bean.getPlannedStartDateTime());
            cv.put("plannedEndDateTime", bean.getPlannedEndDateTime());
            cv.put("remainderFrequency", bean.getRemainderFrequency());
            cv.put("duration", bean.getDuration());
            cv.put("durationunit", bean.getDurationUnit());
            cv.put("taskDescription", bean.getTaskDescription());
            cv.put("isRemainderRequired", bean.getIsRemainderRequired());
            cv.put("taskStatus", bean.getTaskStatus());
            cv.put("signalid", bean.getSignalid());
            cv.put("fromUserName", bean.getFromUserName());
            cv.put("toUserName", bean.getToUserName());
            cv.put("sendStatus", bean.getSendStatus());
            cv.put("completedPercentage", bean.getCompletedPercentage());
            cv.put("taskType", bean.getTaskType());
            cv.put("ownerOfTask", bean.getOwnerOfTask());
            cv.put("mimeType", bean.getMimeType());
            cv.put("taskPriority", bean.getTaskPriority());
            cv.put("reminderquotes", bean.getReminderQuote());
            cv.put("remark", bean.getRemark());
            cv.put("tasktime", bean.getTasktime());
            cv.put("serverFileName", bean.getServerFileName());
            cv.put("taskReceiver", bean.getTaskReceiver());
            cv.put("dateTime", bean.getDateTime());
            cv.put("subType", bean.getSubType());
            cv.put("projectId", bean.getProjectId());
            cv.put("parentTaskId", bean.getParentTaskId());
            Log.i("TaskTable", "Insert");
            String qur = "update taskDetailsInfo set sendStatus=0 where taskDescription = '" + bean.getTaskDescription() + "' ";
            db.execSQL(qur);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return row_id;
    }

    public void updateProjectMemberList(ProjectDetailsBean taskDetailsBean) {
        try {
            if (!db.isOpen())
                openDatabase();

            String query1 = "select taskMemberList from projectHistory where projectId='" + taskDetailsBean.getId() + "' and isParentTask='Y' order by id desc limit 1";
            String taskmemberlist = VideoCallDataBase.getDB(context).getProjectParentTaskId(query1);
            Log.i("pro_notification", "taskmemberlist : " + taskmemberlist);
            String final_taskmemberlist = taskDetailsBean.getTaskMemberList();
            if (taskmemberlist != null && taskmemberlist.length() > 0) {
                final_taskmemberlist = final_taskmemberlist + "," + taskmemberlist;
            }
            Log.i("pro_notification", "final_taskmemberlist : " + final_taskmemberlist);
            ContentValues cv = new ContentValues();
            cv.put("taskMemberList", final_taskmemberlist);
            db.update("projectHistory", cv, "projectId='" + taskDetailsBean.getId() + "' and isParentTask='Y'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int update_Project_history(TaskDetailsBean bean) {
        int row_id = 0;
        try {
            Log.i("conv123","update_Project_history DBDBDB=getTaskReceiver=====>"+bean.getTaskReceiver());
            Log.i("conv123","update_Project_history DBDBDB=getSubType=====>"+bean.getSubType());
            String logginuser = Appreference.loginuserdetails.getEmail();
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            cv.put("loginuser", logginuser);
            if (bean.getTaskNo() != null) {
                cv.put("taskNo", bean.getTaskNo());
            }
            if (bean.getCatagory() != null && !bean.getCatagory().equalsIgnoreCase("") && !bean.getCatagory().equalsIgnoreCase(null) && !bean.getCatagory().equalsIgnoreCase("null")) {
                cv.put("category", bean.getCatagory());
            }
            if (bean.getTaskName() != null && !bean.getTaskName().equalsIgnoreCase("") && !bean.getTaskName().equalsIgnoreCase(null) && !bean.getTaskName().equalsIgnoreCase("null")) {
                cv.put("taskName", bean.getTaskName());
            }
            cv.put("readStatus", bean.getRead_status());
            if (bean.getPlannedStartDateTime() != null && !bean.getPlannedStartDateTime().equalsIgnoreCase("") && !bean.getPlannedStartDateTime().equalsIgnoreCase("null")) {
                cv.put("plannedStartDateTime", bean.getPlannedStartDateTime());
            }

            if (bean.getPlannedEndDateTime() != null && !bean.getPlannedEndDateTime().equalsIgnoreCase("") && !bean.getPlannedEndDateTime().equalsIgnoreCase("null")) {
                cv.put("plannedEndDateTime", bean.getPlannedEndDateTime());
            }
            if (bean.getTaskDescription() != null) {
                cv.put("taskDescription", bean.getTaskDescription());
            }
            Log.i("videocalldatebase", "bean.getTaskDescription() " + bean.getTaskDescription());
            Log.i("videocalldatebase", "bean.getTaskObservers() 1 " + bean.getTaskObservers());
            if (bean.getTaskObservers() != null && !bean.getTaskObservers().equalsIgnoreCase("") && !bean.getTaskObservers().equalsIgnoreCase("null") && !bean.getTaskObservers().equalsIgnoreCase(null)) {
                cv.put("taskObservers", bean.getTaskObservers());
                Log.i("videocalldatebase", "inside check  getTaskObservers " + bean.getTaskObservers());
            } else if (bean.getTaskObservers() == null && bean.getTaskRemoveObservers() != null) {
                cv.put("taskObservers", "");
            }
            if (bean.getTaskStatus() != null && !bean.getTaskStatus().equalsIgnoreCase("reminder")) {
                cv.put("taskStatus", bean.getTaskStatus());
            }
            if (bean.getSignalid() != null) {
                cv.put("signalid", bean.getSignalid());
            }
            Log.i("videocalldatebase", "projectHistory percentage update" + bean.getCompletedPercentage());
            if (bean.getCompletedPercentage() != null && !bean.getCompletedPercentage().equalsIgnoreCase("0")) {
                cv.put("completedPercentage", bean.getCompletedPercentage());
            }
            if (bean.getTaskType() != null) {
                cv.put("taskType", bean.getTaskType());
            }
            if (bean.getOwnerOfTask() != null)
                cv.put("ownerOfTask", bean.getOwnerOfTask());
            cv.put("mimeType", bean.getMimeType());
            if (bean.getTaskName() != null && !bean.getTaskName().equalsIgnoreCase("") && !bean.getTaskName().equalsIgnoreCase(null)) {
                cv.put("taskName", bean.getTaskName());
            }
            if (bean.getTaskReceiver() != null)
                cv.put("taskReceiver", bean.getTaskReceiver());
            if (bean.getGroupTaskMembers() != null  && !bean.getGroupTaskMembers().equalsIgnoreCase("null") && !bean.getGroupTaskMembers().equalsIgnoreCase(null)) {
                cv.put("taskMemberList", bean.getGroupTaskMembers());
            }
            Log.i("TaskTable", "Insert Task Id : " + bean.getTaskId());
            row_id = (int) db.update("projectHistory", cv, "taskId='" + bean.getTaskId() + "'", null);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return row_id;
    }

    public int deleteProjectDraft(TaskDetailsBean taskDetailsBean) {
        int row_id = 0;
        try {
            if (!db.isOpen())
                openDatabase();
            row_id = (int) db.delete("projectHistory", "projectId='" + taskDetailsBean.getProjectId() + "' and taskId='" + taskDetailsBean.getTaskId() + "'", null);
            row_id = (int) db.delete("taskDetailsInfo", "projectId='" + taskDetailsBean.getProjectId() + "' and taskId='" + taskDetailsBean.getTaskId() + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row_id;
    }

    public String getValuesForQuery(String query) {
        String return_value = "";
        try {
            if (db == null)
                openReadableDatabase();
            if (!db.isOpen())
                openDatabase();
            Cursor cur;
            cur = db.rawQuery(query, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                       /* ContactBean bean = new ContactBean();
                        bean.setFirstName(cur.getString(7));
                        bean.setLastName(cur.getString(8));*/
                return_value = cur.getString(0);
                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return return_value;
    }

    public void updateQuery(String query) {
        try {
            if (!db.isOpen())
                openDatabase();
            Log.d("sipnotofication", "update SQL => " + query);
            db.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateContentValues(ContentValues cv, String query) {
        try {
            if (!db.isOpen())
                openDatabase();
            db.update("projectHistory", cv, query, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int deleteProject(TaskDetailsBean taskDetailsBean) {
        int row_id = 0;
        try {
            if (!db.isOpen())
                openDatabase();

            row_id = (int) db.delete("projectHistory", "projectId='" + taskDetailsBean.getProjectId() + "'", null);
            row_id = (int) db.delete("projectDetails", "projectId='" + taskDetailsBean.getProjectId() + "'", null);
            row_id = (int) db.delete("taskDetailsInfo", "projectId='" + taskDetailsBean.getProjectId() + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row_id;
    }

    public int insert_new_Project_history(TaskDetailsBean bean) {
        int row_id = 0;
        try {
            Log.i("conv123","insert_new_Project_history DBDBDB=getTaskReceiver=====>"+bean.getTaskReceiver());
            Log.i("conv123","insert_new_Project_history DBDBDB=getSubType=====>"+bean.getSubType());
            String logginuser = Appreference.loginuserdetails.getEmail();
            String query1 = "select projectName from projectHistory where projectId='" + bean.getProjectId() + "' order by id desc limit 1";
            String projectname = VideoCallDataBase.getDB(context).getProjectParentTaskId(query1);
            String query_1 = "select parentTaskId from projectHistory where projectId='" + bean.getProjectId() + "' order by id desc limit 1";
            String parent_TaskId = VideoCallDataBase.getDB(context).getProjectParentTaskId(query_1);
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            cv.put("loginuser", logginuser);
            cv.put("fromUserId", bean.getFromUserId());
            cv.put("fromUserName", bean.getFromUserName());
            if (bean.getTaskType().equalsIgnoreCase("Group")) {
                cv.put("toUserId", bean.getTaskId());
                cv.put("toUserName", bean.getTaskName());
            } else {
                cv.put("toUserId", bean.getToUserId());
                cv.put("toUserName", bean.getToUserName());
            }

            cv.put("taskNo", bean.getTaskNo());
            if (bean.getTaskName() != null && !bean.getTaskName().equalsIgnoreCase("") && !bean.getTaskName().equalsIgnoreCase(null)) {
                cv.put("taskName", bean.getTaskName());
            }
            cv.put("readStatus", bean.getRead_status());
            if (bean.getPlannedStartDateTime() != null && !bean.getPlannedStartDateTime().equalsIgnoreCase("") && !bean.getPlannedStartDateTime().equalsIgnoreCase("null")) {
                cv.put("plannedStartDateTime", bean.getPlannedStartDateTime());
            }
            if (bean.getPlannedEndDateTime() != null && !bean.getPlannedEndDateTime().equalsIgnoreCase("") && !bean.getPlannedEndDateTime().equalsIgnoreCase("null")) {
                cv.put("plannedEndDateTime", bean.getPlannedEndDateTime());
            }
            cv.put("taskDescription", bean.getTaskDescription());
            if (bean.getTaskStatus() != null && !bean.getTaskStatus().equalsIgnoreCase("reminder")) {
                cv.put("taskStatus", bean.getTaskStatus());
            }
            if (bean.getMimeType() != null) {
                cv.put("signalid", bean.getSignalid());
            }
//            cv.put("taskMemberList", );
            Log.i("videocalldatebase", "projectHistory percentage update" + bean.getCompletedPercentage());
            if (bean.getCompletedPercentage() != null && !bean.getCompletedPercentage().equalsIgnoreCase("0")) {
                cv.put("completedPercentage", bean.getCompletedPercentage());
            }

            Log.i("videocalldatebase", "bean.getTaskObservers() 2 " + bean.getTaskObservers());
            if (bean.getTaskObservers() != null && !bean.getTaskObservers().equalsIgnoreCase("") && !bean.getTaskObservers().equalsIgnoreCase("null") && !bean.getTaskObservers().equalsIgnoreCase(null)) {
                cv.put("taskObservers", bean.getTaskObservers());
                Log.i("videocalldatebase", "inside check  getTaskObservers 2 " + bean.getTaskObservers());
            }
            cv.put("taskType", bean.getTaskType());
            if (bean.getOwnerOfTask() != null) {
                cv.put("ownerOfTask", bean.getOwnerOfTask());
            }
            cv.put("mimeType", bean.getMimeType());
            cv.put("readStatus", bean.getRead_status());
            cv.put("taskId", bean.getTaskId());
            if (bean.getTaskObservers() != null && !bean.getTaskObservers().trim().equalsIgnoreCase("") && !bean.getTaskObservers().equalsIgnoreCase("null") && !bean.getTaskObservers().equalsIgnoreCase(null)) {
                cv.put("taskObservers", bean.getTaskObservers());
            }
            if (bean.getTaskType() != null && !bean.getTaskType().equalsIgnoreCase("Group")) {
                cv.put("taskReceiver", bean.getTaskReceiver());
            } else if (bean.getTaskReceiver() != null || bean.getSubType().equalsIgnoreCase("deassign")) {
                cv.put("taskReceiver", bean.getTaskReceiver());
            }
            cv.put("projectName", projectname);
            cv.put("projectOwner", bean.getOwnerOfTask());
            cv.put("projectId", bean.getProjectId());
            cv.put("parentTaskId", parent_TaskId);
            cv.put("requestStatus", bean.getRequestStatus());
            if ((bean.getGroupTaskMembers() != null && !bean.getGroupTaskMembers().trim().equalsIgnoreCase("") && !bean.getGroupTaskMembers().equalsIgnoreCase("null") && !bean.getGroupTaskMembers().equalsIgnoreCase(null))|| bean.getSubType()!=null && bean.getSubType().equalsIgnoreCase("deassign")) {
                cv.put("taskMemberList", bean.getGroupTaskMembers());
            }
            if (bean.getCatagory() != null && !bean.getCatagory().trim().equalsIgnoreCase("") && !bean.getCatagory().equalsIgnoreCase("null") && !bean.getCatagory().equalsIgnoreCase(null)) {
                cv.put("category", bean.getCatagory());
            }
            cv.put("isParentTask", "N");
            if (bean.getIssueId() != null && (bean.getCatagory() != null && bean.getCatagory().equalsIgnoreCase("issue")))
                cv.put("issueParentId", bean.getIssueId());
            if (!DuplicateProjectTaskIdChecker(bean.getTaskId())) {
                row_id = (int) db.insert("projectHistory", null, cv);
            } else {
                row_id = (int) db.update("projectHistory", cv, "taskId" + "='" + bean.getTaskId() + "'", null);
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return row_id;
    }

    public int update_single_Task_history(TaskDetailsBean bean) {
        int row_id = 0;
        try {
            String logginuser = Appreference.loginuserdetails.getEmail();
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            cv.put("loginuser", logginuser);
            cv.put("fromUserId", bean.getFromUserId());
            cv.put("toUserId", bean.getToUserId());
            cv.put("taskNo", bean.getTaskNo());
            cv.put("category", bean.getCatagory());
            cv.put("issueId", bean.getIssueId());
//            cv.put("taskName", bean.getTaskName());
            cv.put("plannedStartDateTime", bean.getPlannedStartDateTime());
            cv.put("plannedEndDateTime", bean.getPlannedEndDateTime());
            cv.put("remainderFrequency", bean.getRemainderFrequency());
            cv.put("duration", bean.getDuration());
            cv.put("durationunit", bean.getDurationUnit());
            cv.put("taskDescription", bean.getTaskDescription());
            cv.put("isRemainderRequired", bean.getIsRemainderRequired());
            cv.put("taskStatus", bean.getTaskStatus());
            cv.put("signalid", bean.getSignalid());
            cv.put("fromUserName", bean.getFromUserName());
            cv.put("toUserName", bean.getToUserName());
            cv.put("sendStatus", bean.getSendStatus());
            cv.put("completedPercentage", bean.getCompletedPercentage());
            cv.put("taskType", bean.getTaskType());
//            cv.put("ownerOfTask", bean.getOwnerOfTask());
            cv.put("mimeType", bean.getMimeType());
            cv.put("taskPriority", bean.getTaskPriority());
            cv.put("reminderquotes", bean.getReminderQuote());
            cv.put("remark", bean.getRemark());
            cv.put("tasktime", bean.getTasktime());
//            cv.put("taskReceiver", bean.getTaskReceiver());
            cv.put("serverFileName", bean.getServerFileName());
            cv.put("subType", bean.getSubType());
            cv.put("projectId", bean.getProjectId());
            cv.put("parentTaskId", bean.getParentTaskId());
            Log.i("TaskTable", "Insert");
            row_id = (int) db.update("taskDetailsInfo", cv, "signalId=" + bean.getSignalid(), null);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return row_id;
    }

    public void taskStatusUpdate(String task_status, String taskid) {

//        String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "taskStatus = '" + query + "' WHERE " + "taskId" + " = " + taskid;

        ContentValues cv = new ContentValues();
        cv.put("taskStatus", task_status);
        db.update("taskDetailsInfo", cv, "taskId" + "= ?", new String[]{taskid});
        db.update("taskHistoryInfo", cv, "taskId" + "= ?", new String[]{taskid});
        Log.i("task", "status updated");
    }

    public void updateBadgeStatus(String task_status, String taskid) {

//        String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "taskStatus = '" + query + "' WHERE " + "taskId" + " = " + taskid;

        ContentValues cv = new ContentValues();
        cv.put("readStatus", task_status);
        db.update("projectDetails", cv, "projectId" + "= ?", new String[]{taskid});
        Log.i("task", "status updated");
    }

    public void updateEditText(TaskDetailsBean detailsBean) {

//        String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "taskStatus = '" + query + "' WHERE " + "taskId" + " = " + taskid;

        ContentValues cv = new ContentValues();
        cv.put("taskDescription", detailsBean.getTaskDescription());
        db.update("taskDetailsInfo", cv, "taskId" + "= ? and " + "signalId" + "= ?", new String[]{detailsBean.getTaskId(), detailsBean.getSignalid()});
        Log.i("task", "status updated");
    }

    public void updateEscalationText(TaskDetailsBean detailsBean) {

//        String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "taskStatus = '" + query + "' WHERE " + "taskId" + " = " + taskid;

        ContentValues cv = new ContentValues();
        cv.put("msgstatus", 11);
        db.update("taskDetailsInfo", cv, "taskId" + "= ? and " + "signalId" + "= ?", new String[]{detailsBean.getTaskId(), detailsBean.getSignalid()});
        Log.i("task", "status updated");
    }

    public void updateDependency(String signalId, String taskId) {
        ContentValues cv = new ContentValues();
        ContentValues cv1 = new ContentValues();
        cv.put("msgstatus", 22);
        cv.put("requeststatus", "Resolved");
        cv1.put("requeststatus", "Resolved");
        db.update("taskDetailsInfo", cv, "taskId" + "= ? and " + "signalId" + "= ?", new String[]{taskId, signalId});
        db.update("projectHistory", cv1, "taskId" + "= ?", new String[]{taskId});
        Log.i("task", "status updated");
    }

    public void taskIdUpdate(String query, String taskNo) {

        String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "taskId = '" + query + "' WHERE " + "taskNo" + " = " + taskNo;

        ContentValues cv = new ContentValues();
        cv.put("taskId", query);
        db.update("taskDetailsInfo", cv, "taskNo" + "= ?", new String[]{taskNo});
        Log.i("task", "status updated");
    }

    public void uploadPrivateMMFile(String signalid, String server_file) {

        String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "serverFileName = '" + server_file + "' WHERE " + "signalid" + " = " + signalid;

        ContentValues cv = new ContentValues();
        cv.put("serverFileName", server_file);
        db.update("taskDetailsInfo", cv, "signalid" + "= ?", new String[]{signalid});
        Log.i("privatemessage", "private serverFileName updated");
    }

    public void templateNameUpdate(String new_name, String taskId) {

//        String query1 = "UPDATE " + "taskHistoryInfo" + " SET " + "taskName = '" + query + "' WHERE " + "taskId" + " = " + taskId;

        ContentValues cv = new ContentValues();
        cv.put("taskName", new_name);
        db.update("taskHistoryInfo", cv, "taskId" + "= ?", new String[]{taskId});
        Log.i("task", "status updated");
    }

    public void deleteGroup(String groupId) {

//        String query1 = "Delete from group1 where groupid='" + groupId + "'";

        db.delete("group1", "groupid" + "= ?", new String[]{groupId});
    }

    public void deleteGroupMembers(String groupName) {

//        String query1 = "Delete from groupmember where loginuser='" + groupName + "'";

        db.delete("groupmember", "loginuser" + "= ?", new String[]{groupName});
    }

    public void deleteContact(String username) {

//        String query1 = "Delete from contact where username='" + username + "'";

        db.delete("contact", "username" + "= ?", new String[]{username});
    }

    public void deletetask(String task_Id, String signal_id, String type) {

//        String query1 = "Delete from taskDetailsInfo where signalId='" + signalid + "'";

//        db.delete("taskDetailsInfo", "signalId" + "= ?", new String[]{signalid});
        if (type.equalsIgnoreCase("delete"))
            db.delete("taskDetailsInfo", "taskId" + "= ? and " + "signalId" + "= ?", new String[]{task_Id, signal_id});
        else {
            db.delete("taskDetailsInfo", "taskId" + "= ?", new String[]{task_Id});
//            db.delete("taskHistoryInfo", "taskId" + "= ?", new String[]{task_Id});
        }
        Log.i("popupmenu", "value for db " + signal_id);
    }

    public void deleteSingleGroupMember(String del_username, String group_id) {

//        String query1 = "Delete from groupmember where username='" + del_username + "' AND loginuser='" + groupName + "'";

        db.delete("groupmember", "username" + "= ? and " + "groupid" + "= ?", new String[]{del_username, group_id});
    }

    public void deleteCustomTagEntry(String del_username, String groupName) {

//        String query1 = "Delete from groupmember where username='" + del_username + "' AND loginuser='" + groupName + "'";
        Log.i("CustomTag", "deleted11" + groupName + "\t" + del_username);
        db.delete("taskDetailsInfo", "taskId" + "= ? and " + "customSetId" + "= ?", new String[]{del_username, groupName});
    }

    public void updateGroupName(String group_id, String newGroup_name) {

//        String query1 = "UPDATE " + "group1" + " SET " + "groupname = '" + newGroup_name + "' WHERE " + "groupid" + " = " + group_id;

        ContentValues cv = new ContentValues();
        cv.put("groupname", newGroup_name);
        db.update("group1", cv, "groupid" + "= ?", new String[]{group_id});
    }

    public void updateOberver(String group_id, String newGroup_name) {

//        String query1 = "UPDATE " + "group1" + " SET " + "groupname = '" + newGroup_name + "' WHERE " + "groupid" + " = " + group_id;

        ContentValues cv = new ContentValues();
        cv.put("taskObservers", newGroup_name);
        db.update("taskHistoryInfo", cv, "taskId" + "= ?", new String[]{group_id});
    }

    public void updateGroupNameInmembers(String group_id, String newGroup_name) {

//        String query1 = "UPDATE " + "groupmember" + " SET " + "loginuser = '" + newGroup_name + "' WHERE " + "loginuser" + " = " + oldGroupName;

        ContentValues cv = new ContentValues();
        cv.put("loginuser", newGroup_name);
        db.update("groupmember", cv, "groupid" + "= ?", new String[]{group_id});
    }

    public void taskMembersUpdate(String query, String taskNo) {

//        String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "taskId = '" + query + "' WHERE " + "taskNo" + " = " + taskNo;

        ContentValues cv = new ContentValues();
        cv.put("taskMemberList", query);
        db.update("taskHistoryInfo", cv, "taskNo" + "= ?", new String[]{taskNo});
    }

    public void taskRemQuotesUpdate(String remTone, String remQuotes, String temp_duration, String duration_unit, String signalId, String ws_send_status) {

//        String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "taskDescription = '" + remTone + "'"+" , reminderquotes = '" + remTone + "' WHERE " + "taskNo" + " = " + taskNo;

        ContentValues cv = new ContentValues();
        cv.put("serverFileName", remTone);
        cv.put("reminderquotes", remQuotes);
        cv.put("duration", temp_duration);
        cv.put("durationunit", duration_unit);
        cv.put("wssendstatus", ws_send_status);
        db.update("taskDetailsInfo", cv, "signalid" + "= ?", new String[]{signalId});
    }

    public void taskWSStatusUpdate(String signalId, String ws_send_status) {

        ContentValues cv = new ContentValues();
        Log.i("response", "Notes  16 Db " + signalId);
        cv.put("wssendstatus", ws_send_status);
        db.update("taskDetailsInfo", cv, "signalid" + "= ?", new String[]{signalId});
    }

    @SuppressWarnings("finally")
    public int deleteTemplate(String Query) {
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                int bool = db.delete("taskDetailsInfo", "taskNo ='" + Query + "'", null);
                Log.i("template", "no of row deleted" + bool);
            }
        } catch (Exception e) {
            Log.i("task", "exception arised" + e);
        }
        return 0;
    }

    @SuppressWarnings("finally")
    public int deleteProjects(String Query) {
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                int bool = db.delete("projectDetails", "projectId ='" + Query + "'", null);
                Log.i("template", "no of row deleted" + bool);
            }
        } catch (Exception e) {
            Log.i("task", "exception arised" + e);
        }
        return 0;
    }


    public int deleteTemplateEntry(String Query) {
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                int bool = db.delete("taskDetailsInfo", "taskId ='" + Query + "'", null);
                Log.i("template", "no of row deleted" + bool);
            }
        } catch (Exception e) {
            Log.i("task", "exception arised" + e);
        }
        return 0;
    }

    public int deleteConversationEntry(String Query) {
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                int bool = db.delete("taskDtailsInfo", "signalid ='" + Query + "'", null);
                Log.i("template", "no of row deleted" + bool);
            }
        } catch (Exception e) {
            Log.i("task", "exception arised" + e);
        }
        return 0;
    }

    public ArrayList<TaskDetailsBean> getTaskHistory(String query) {
         ArrayList<TaskDetailsBean> arrayList = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
//                "select * from taskDetailsInfo where loginuser='" + ""+ "'"
                cur = db.rawQuery(query, null);
                cur.moveToFirst();

                while (!cur.isAfterLast()) {
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();

                    if (cur.getString(cur.getColumnIndex("dateTime")) != null) {
                        taskDetailsBean.setDateTime(Appreference.utcToLocalTime(cur.getString(cur.getColumnIndex("dateTime"))));
                        taskDetailsBean.setTaskUTCDateTime(cur.getString(cur.getColumnIndex("dateTime")));
                    }
                    if (cur.getString(cur.getColumnIndex("fromUserId")) != null)
                        taskDetailsBean.setFromUserId(cur.getString(cur.getColumnIndex("fromUserId")));
                    if (cur.getString(cur.getColumnIndex("toUserId")) != null)
                        taskDetailsBean.setToUserId(cur.getString(cur.getColumnIndex("toUserId")));
                    if (cur.getString(cur.getColumnIndex("taskNo")) != null)
                        taskDetailsBean.setTaskNo(cur.getString(cur.getColumnIndex("taskNo")));
//                    taskDetailsBean.setTaskName(cur.getString(cur.getColumnIndex("taskName")));
                    if (cur.getString(cur.getColumnIndex("plannedStartDateTime")) != null && !cur.getString(cur.getColumnIndex("plannedStartDateTime")).equalsIgnoreCase("(null)")) {
                        Log.i("VideoCallDB", "getTaskHistory plannedStartDateTime * " + cur.getString(cur.getColumnIndex("plannedStartDateTime")));
                        Log.i("VideoCallDB", "getTaskHistory plannedStartDateTime * * " + Appreference.utcToLocalTime(cur.getString(cur.getColumnIndex("plannedStartDateTime"))));
                        taskDetailsBean.setPlannedStartDateTime(Appreference.utcToLocalTime(cur.getString(cur.getColumnIndex("plannedStartDateTime"))));
//                        taskDetailsBean.setUtcPlannedStartDateTime(cur.getString(cur.getColumnIndex("plannedStartDateTime")));
                    }
                    Log.i("xmlparser", "taskDetailsBean getPlannedStartDateTime " + taskDetailsBean.getPlannedStartDateTime());
                    if (cur.getString(cur.getColumnIndex("plannedEndDateTime")) != null && !cur.getString(cur.getColumnIndex("plannedEndDateTime")).equalsIgnoreCase("(null)")) {
                        Log.i("VideoCallDB", "getTaskHistory plannedEndDateTime * " + cur.getString(cur.getColumnIndex("plannedEndDateTime")));
                        Log.i("VideoCallDB", "getTaskHistory plannedEndDateTime * * " + Appreference.utcToLocalTime(cur.getString(cur.getColumnIndex("plannedEndDateTime"))));
                        taskDetailsBean.setPlannedEndDateTime(Appreference.utcToLocalTime(cur.getString(cur.getColumnIndex("plannedEndDateTime"))));
//                        taskDetailsBean.setUtcplannedEndDateTime(cur.getString(cur.getColumnIndex("plannedEndDateTime")));
                    }
                    Log.i("xmlparser", "String value ---> " + cur.getString(cur.getColumnIndex("remainderFrequency")));
                    if (cur.getString(cur.getColumnIndex("remainderFrequency")) != null && !cur.getString(cur.getColumnIndex("remainderFrequency")).equalsIgnoreCase("(null)") && !cur.getString(cur.getColumnIndex("remainderFrequency")).equalsIgnoreCase("")) {
                        Log.i("VideoCallDB", "getTaskHistory remainderFrequency * " + cur.getString(cur.getColumnIndex("remainderFrequency")));
                        Log.i("VideoCallDB", "getTaskHistory remainderFrequency * * " + Appreference.utcToLocalTime(cur.getString(cur.getColumnIndex("remainderFrequency"))));
                        taskDetailsBean.setRemainderFrequency(Appreference.utcToLocalTime(cur.getString(cur.getColumnIndex("remainderFrequency"))));
//                        taskDetailsBean.setUtcPemainderFrequency(cur.getString(cur.getColumnIndex("remainderFrequency")));
                    }
                    if (cur.getString(cur.getColumnIndex("duration")) != null)
                        taskDetailsBean.setDuration(cur.getString(cur.getColumnIndex("duration")));
                    if (cur.getString(cur.getColumnIndex("durationunit")) != null)
                        taskDetailsBean.setDurationUnit(cur.getString(cur.getColumnIndex("durationunit")));
                    if (cur.getString(cur.getColumnIndex("taskDescription")) != null)
                        taskDetailsBean.setTaskDescription(cur.getString(cur.getColumnIndex("taskDescription")));
                    if (cur.getString(cur.getColumnIndex("isRemainderRequired")) != null)
                        taskDetailsBean.setIsRemainderRequired(cur.getString(cur.getColumnIndex("isRemainderRequired")));
                    if (cur.getString(cur.getColumnIndex("taskStatus")) != null)
                        taskDetailsBean.setTaskStatus(cur.getString(cur.getColumnIndex("taskStatus")));
                    if (cur.getString(cur.getColumnIndex("signalid")) != null)
                        taskDetailsBean.setSignalid(cur.getString(cur.getColumnIndex("signalid")));
                    if (cur.getString(cur.getColumnIndex("fromUserName")) != null)
                        taskDetailsBean.setFromUserName(cur.getString(cur.getColumnIndex("fromUserName")));
                    if (cur.getString(cur.getColumnIndex("toUserName")) != null)
                        taskDetailsBean.setToUserName(cur.getString(cur.getColumnIndex("toUserName")));
                    if (cur.getString(cur.getColumnIndex("sendStatus")) != null)
                        taskDetailsBean.setSendStatus(cur.getString(cur.getColumnIndex("sendStatus")));
                    if (cur.getString(cur.getColumnIndex("completedPercentage")) != null)
                        taskDetailsBean.setCompletedPercentage(cur.getString(cur.getColumnIndex("completedPercentage")));
                    if (cur.getString(cur.getColumnIndex("taskType")) != null)
                        taskDetailsBean.setTaskType(cur.getString(cur.getColumnIndex("taskType")));
//                    taskDetailsBean.setOwnerOfTask(cur.getString(cur.getColumnIndex("ownerOfTask")));
                    if (cur.getString(cur.getColumnIndex("mimeType")) != null)
                        taskDetailsBean.setMimeType(cur.getString(cur.getColumnIndex("mimeType")));
                    if (cur.getString(cur.getColumnIndex("taskPriority")) != null)
                        taskDetailsBean.setTaskPriority(cur.getString(cur.getColumnIndex("taskPriority")));
                    if (cur.getString(cur.getColumnIndex("dateFrequency")) != null)
                        taskDetailsBean.setDateFrequency(cur.getString(cur.getColumnIndex("dateFrequency")));
                    if (cur.getString(cur.getColumnIndex("timeFrequency")) != null)
                        taskDetailsBean.setTimeFrequency(cur.getString(cur.getColumnIndex("timeFrequency")));
                    if (cur.getString(cur.getColumnIndex("taskId")) != null)
                        taskDetailsBean.setTaskId(cur.getString(cur.getColumnIndex("taskId")));
                    if (cur.getString(cur.getColumnIndex("reminderquotes")) != null)
                        taskDetailsBean.setReminderQuote(cur.getString(cur.getColumnIndex("reminderquotes")));
                    if (cur.getString(cur.getColumnIndex("remark")) != null)
                        taskDetailsBean.setRemark(cur.getString(cur.getColumnIndex("remark")));
//                    taskDetailsBean.setTaskReceiver(cur.getString(cur.getColumnIndex("taskReceiver")));
                    taskDetailsBean.setTasktime(taskDetailsBean.getDateTime().split(" ")[1]);
                    if (cur.getString(cur.getColumnIndex("tasktime")) != null)
                        taskDetailsBean.setTaskUTCTime(cur.getString(cur.getColumnIndex("tasktime")));
//                    taskDetailsBean.setTaskObservers(cur.getString(cur.getColumnIndex("taskObservers")));
                    if (cur.getString(cur.getColumnIndex("serverFileName")) != null)
                        taskDetailsBean.setServerFileName(cur.getString(cur.getColumnIndex("serverFileName")));
                    if (cur.getString(cur.getColumnIndex("requestStatus")) != null)
                        taskDetailsBean.setRequestStatus(cur.getString(cur.getColumnIndex("requestStatus")));
//                    taskDetailsBean.setGroupTaskMembers(cur.getString(cur.getColumnIndex("taskMemberList")));
                    if (cur.getString(cur.getColumnIndex("duration")) != null)
                        taskDetailsBean.setDuration(cur.getString(cur.getColumnIndex("duration")));
                    if (cur.getString(cur.getColumnIndex("reminderquotes")) != null)
                        taskDetailsBean.setReminderQuote(cur.getString(cur.getColumnIndex("reminderquotes")));
                    if (cur.getString(cur.getColumnIndex("durationunit")) != null)
                        taskDetailsBean.setDurationUnit(cur.getString(cur.getColumnIndex("durationunit")));
                    if (cur.getString(cur.getColumnIndex("taskTagName")) != null)
                        taskDetailsBean.setTaskTagName(cur.getString(cur.getColumnIndex("taskTagName")));
                    if (cur.getString(cur.getColumnIndex("customTagId")) != null)
                        taskDetailsBean.setCustomTagId((cur.getInt(cur.getColumnIndex("customTagId"))));
                    if (cur.getString(cur.getColumnIndex("customSetId")) != null)
                        taskDetailsBean.setCustomSetId((cur.getInt(cur.getColumnIndex("customSetId"))));
                    if (cur.getString(cur.getColumnIndex("customTagVisible")) != null)
                        taskDetailsBean.setCustomTagVisible(Boolean.valueOf(cur.getString(cur.getColumnIndex("customTagVisible"))));
                    if (cur.getString(cur.getColumnIndex("subType")) != null)
                        taskDetailsBean.setSubType(cur.getString(cur.getColumnIndex("subType")));
                    if (cur.getString(cur.getColumnIndex("daysOfTheWeek")) != null)
                        taskDetailsBean.setDaysOfTheWeek(cur.getString(cur.getColumnIndex("daysOfTheWeek")));
                    if (cur.getString(cur.getColumnIndex("repeatFrequency")) != null)
                        taskDetailsBean.setRepeatFrequency(cur.getString(cur.getColumnIndex("repeatFrequency")));
                    if (cur.getString(cur.getColumnIndex("projectId")) != null)
                        taskDetailsBean.setProjectId(cur.getString(cur.getColumnIndex("projectId")));
                    if (cur.getString(cur.getColumnIndex("parentTaskId")) != null)
                        taskDetailsBean.setParentTaskId(cur.getString(cur.getColumnIndex("parentTaskId")));
                    if (cur.getString(cur.getColumnIndex("syncEnable")) != null)
                        taskDetailsBean.setSyncEnable(cur.getString(cur.getColumnIndex("syncEnable")));
//                    taskDetailsBean.setOverdue_Msg(cur.getString(cur.getColumnIndex("overdue_Msg")));
                    if (cur.getString(cur.getColumnIndex("longmessage")) != null)
                        taskDetailsBean.setLongmessage(cur.getString(cur.getColumnIndex("longmessage")));
                    if (cur.getString(cur.getColumnIndex("private_member")) != null)
                        taskDetailsBean.setPrivate_Member(cur.getString(cur.getColumnIndex("private_member")));
                    if (cur.getString(cur.getColumnIndex("sender_reply")) != null)
                       taskDetailsBean.setSender_reply(cur.getString(cur.getColumnIndex("sender_reply")));
                    if (cur.getString(cur.getColumnIndex("reply_sendername")) != null)
                        taskDetailsBean.setReply_sender_name(cur.getString(cur.getColumnIndex("reply_sendername")));
                    if (cur.getString(cur.getColumnIndex("taskPlannedBeforeEndDate")) != null)
                        taskDetailsBean.setTaskPlannedBeforeEndDate(cur.getString(cur.getColumnIndex("taskPlannedBeforeEndDate")));
                    if (cur.getString(cur.getColumnIndex("taskPlannedLatestEndDate")) != null)
                        taskDetailsBean.setTaskPlannedLatestEndDate(cur.getString(cur.getColumnIndex("taskPlannedLatestEndDate")));
                    if (cur.getString(cur.getColumnIndex("fromTaskName")) != null)
                        taskDetailsBean.setFromTaskName(cur.getString(cur.getColumnIndex("fromTaskName")));
                    if (cur.getString(cur.getColumnIndex("toTaskName")) != null)
                        taskDetailsBean.setToTaskName(cur.getString(cur.getColumnIndex("toTaskName")));
//                    taskDetailsBean.setCatagory(cur.getString(cur.getColumnIndex("category")));
//                    taskDetailsBean.setIssueId(cur.getString(cur.getColumnIndex("issueId")));
//                    Log.d("Db_Insert", " Get Issues Id   ==  " + cur.getString(cur.getColumnIndex("issueId")));
                    if (cur.getString(cur.getColumnIndex("msgstatus")) != null && !cur.getString(cur.getColumnIndex("msgstatus")).equalsIgnoreCase(""))
                        taskDetailsBean.setMsg_status(Integer.parseInt(cur.getString(cur.getColumnIndex("msgstatus"))));
                    if (cur.getString(cur.getColumnIndex("showprogress")) != null && !cur.getString(cur.getColumnIndex("showprogress")).equalsIgnoreCase("")) {
                        if (cur.getString(cur.getColumnIndex("mimeType")).equalsIgnoreCase("image") ||
                                cur.getString(cur.getColumnIndex("mimeType")).equalsIgnoreCase("video") ||
                                cur.getString(cur.getColumnIndex("mimeType")).equalsIgnoreCase("sketch") ||
                                cur.getString(cur.getColumnIndex("mimeType")).equalsIgnoreCase("audio")) {
                            File imageFile;
                            if (cur.getString(cur.getColumnIndex("taskDescription")).contains("High Message")) {
                                imageFile = new File(cur.getString(cur.getColumnIndex("taskDescription")));
                            } else {
                                imageFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "High Message/downloads/" + cur.getString(cur.getColumnIndex("taskDescription")));
                            }

                            if (imageFile.exists()) {
                                taskDetailsBean.setShow_progress(1);
                            } else {
                                taskDetailsBean.setShow_progress(2);
                            }
                        }
//                        taskDetailsBean.setShow_progress(Integer.parseInt(cur.getString(cur.getColumnIndex("showprogress"))));
                    }
                    Log.i("output123", "IN DB customField TaskHistory MEthod===>" + cur.getString(cur.getColumnIndex("fromUserName")));

                    if (taskDetailsBean.getSignalid() != null)
                        arrayList.add(taskDetailsBean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            Log.i("file", "size" + arrayList.size());
            return arrayList;
        }
    }


    /**
     * Get TaskHistory Page values in taskHistoryInfo Table
     */

    public ArrayList<TaskDetailsBean> getProjectHistoryInfo(String query) {
        ArrayList<TaskDetailsBean> arrayList = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
//                "select * from taskDetailsInfo where loginuser='" + ""+ "'"
                cur = db.rawQuery(query, null);
                cur.moveToFirst();

                while (!cur.isAfterLast()) {
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                    Log.i("observer", "value" + cur.getString(cur.getColumnIndex("taskObservers")));
                    taskDetailsBean.setTaskObservers(cur.getString(cur.getColumnIndex("taskObservers")));
                    arrayList.add(taskDetailsBean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            Log.i("file", "size" + arrayList.size());
            return arrayList;
        }
    }

    public ArrayList<TaskDetailsBean> getTaskHistoryInfo(String query) {
        ArrayList<TaskDetailsBean> arrayList = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
//                "select * from taskDetailsInfo where loginuser='" + ""+ "'"
                cur = db.rawQuery(query, null);
                cur.moveToFirst();

                while (!cur.isAfterLast()) {
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();

                    if (cur.getString(cur.getColumnIndex("dateTime")) != null) {
                        taskDetailsBean.setDateTime(Appreference.utcToLocalTime(cur.getString(cur.getColumnIndex("dateTime"))));
                        taskDetailsBean.setTaskUTCDateTime(cur.getString(cur.getColumnIndex("dateTime")));
                    }
                    taskDetailsBean.setTaskNo(cur.getString(cur.getColumnIndex("taskNo")));
                    taskDetailsBean.setTaskName(cur.getString(cur.getColumnIndex("taskName")));
                    if (cur.getString(cur.getColumnIndex("plannedStartDateTime")) != null && !cur.getString(cur.getColumnIndex("plannedStartDateTime")).equalsIgnoreCase("(null)")) {
                        Log.i("VideoCallDB", "getTaskHistoryInfo plannedStartDateTime $ " + cur.getString(cur.getColumnIndex("plannedStartDateTime")));
                        Log.i("VideoCallDB", "getTaskHistoryInfo plannedStartDateTime $ $ " + Appreference.utcToLocalTime(cur.getString(cur.getColumnIndex("plannedStartDateTime"))));
                        taskDetailsBean.setPlannedStartDateTime(Appreference.utcToLocalTime(cur.getString(cur.getColumnIndex("plannedStartDateTime"))));
//                        taskDetailsBean.setUtcPlannedStartDateTime(cur.getString(cur.getColumnIndex("plannedStartDateTime")));
                    }
                    if (cur.getString(cur.getColumnIndex("plannedEndDateTime")) != null && !cur.getString(cur.getColumnIndex("plannedEndDateTime")).equalsIgnoreCase("(null)")) {
                        Log.i("VideoCallDB", "getTaskHistoryInfo plannedEndDateTime $ " + cur.getString(cur.getColumnIndex("plannedEndDateTime")));
                        Log.i("VideoCallDB", "getTaskHistoryInfo plannedEndDateTime $ $ " + Appreference.utcToLocalTime(cur.getString(cur.getColumnIndex("plannedEndDateTime"))));
                        taskDetailsBean.setPlannedEndDateTime(Appreference.utcToLocalTime(cur.getString(cur.getColumnIndex("plannedEndDateTime"))));
//                        taskDetailsBean.setUtcplannedEndDateTime(cur.getString(cur.getColumnIndex("plannedEndDateTime")));
                    }
                    Log.i("xmlparser", "String value ---> " + cur.getString(cur.getColumnIndex("remainderFrequency")));
                    if (cur.getString(cur.getColumnIndex("remainderFrequency")) != null && !cur.getString(cur.getColumnIndex("remainderFrequency")).equalsIgnoreCase("(null)") && !cur.getString(cur.getColumnIndex("remainderFrequency")).equalsIgnoreCase("")) {
                        Log.i("VideoCallDB", "getTaskHistoryInfo remainderFrequency $ " + cur.getString(cur.getColumnIndex("remainderFrequency")));
                        Log.i("VideoCallDB", "getTaskHistoryInfo remainderFrequency $ $ " + Appreference.utcToLocalTime(cur.getString(cur.getColumnIndex("remainderFrequency"))));
                        taskDetailsBean.setRemainderFrequency(Appreference.utcToLocalTime(cur.getString(cur.getColumnIndex("remainderFrequency"))));
//                        taskDetailsBean.setUtcPemainderFrequency(cur.getString(cur.getColumnIndex("remainderFrequency")));
                    }
                    taskDetailsBean.setTaskDescription(cur.getString(cur.getColumnIndex("taskDescription")));
                    taskDetailsBean.setTaskStatus(cur.getString(cur.getColumnIndex("taskStatus")));
                    taskDetailsBean.setSignalid(cur.getString(cur.getColumnIndex("signalid")));
                    taskDetailsBean.setCompletedPercentage(cur.getString(cur.getColumnIndex("completedPercentage")));
                    taskDetailsBean.setTaskType(cur.getString(cur.getColumnIndex("taskType")));
                    taskDetailsBean.setOwnerOfTask(cur.getString(cur.getColumnIndex("ownerOfTask")));
                    taskDetailsBean.setMimeType(cur.getString(cur.getColumnIndex("mimeType")));
                    taskDetailsBean.setTaskId(cur.getString(cur.getColumnIndex("taskId")));
                    taskDetailsBean.setIssueId(cur.getString(cur.getColumnIndex("parentTaskId")));
                    taskDetailsBean.setCatagory(cur.getString(cur.getColumnIndex("category")));
                    taskDetailsBean.setTaskReceiver(cur.getString(cur.getColumnIndex("taskReceiver")));
                    taskDetailsBean.setTasktime(taskDetailsBean.getDateTime().split(" ")[1]);
                    taskDetailsBean.setTaskUTCTime(cur.getString(cur.getColumnIndex("tasktime")));
                    taskDetailsBean.setTaskObservers(cur.getString(cur.getColumnIndex("taskObservers")));
                    taskDetailsBean.setGroupTaskMembers(cur.getString(cur.getColumnIndex("taskMemberList")));


                    if (taskDetailsBean.getTaskType() != null) {
                        if (taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Individual")) {
                            if (taskDetailsBean.getOwnerOfTask() != null && Appreference.loginuserdetails.getUsername().equalsIgnoreCase(taskDetailsBean.getOwnerOfTask())) {
                                ContactBean contactBean = VideoCallDataBase.getDB(context).getContactObject(taskDetailsBean.getTaskReceiver());
                                taskDetailsBean.setToUserId(String.valueOf(contactBean.getContact_id()));
                                taskDetailsBean.setToUserName(contactBean.getUsername());
                            } else {
                                ContactBean contactBean = VideoCallDataBase.getDB(context).getContactObject(taskDetailsBean.getOwnerOfTask());
                                taskDetailsBean.setToUserId(String.valueOf(contactBean.getContact_id()));
                                taskDetailsBean.setToUserName(contactBean.getUsername());
                            }
                        } else {
                            taskDetailsBean.setToUserId(String.valueOf(VideoCallDataBase.getDB(context).getGroupId(taskDetailsBean.getTaskReceiver())));
                        }
                    }
//                        taskDetailsBean.setShow_progress(Integer.parseInt(cur.getString(cur.getColumnIndex("showprogress"))));

                    arrayList.add(taskDetailsBean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            Log.i("file", "size" + arrayList.size());
            return arrayList;
        }
    }


    public ArrayList<ProjectDetailsBean> getProjectHistory(String query) {
        ArrayList<ProjectDetailsBean> arrayList = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
//                "select * from taskDetailsInfo where loginuser='" + ""+ "'"
                cur = db.rawQuery(query, null);
                cur.moveToFirst();

                while (!cur.isAfterLast()) {
                    ProjectDetailsBean projectDetailsBean = new ProjectDetailsBean();

                    projectDetailsBean.setId(cur.getString(cur.getColumnIndex("projectId")));
                    projectDetailsBean.setProjectName(cur.getString(cur.getColumnIndex("projectName")));
                    projectDetailsBean.setDescription(cur.getString(cur.getColumnIndex("projectDescription")));
//                    projectDetailsBean.setOrganisation(cur.getString(cur.getColumnIndex("projectOrganisation")));
                    projectDetailsBean.setParentTaskId(cur.getString(cur.getColumnIndex("parentTaskId")));
                    if (cur.getString(cur.getColumnIndex("readStatus")) != null && !cur.getString(cur.getColumnIndex("readStatus")).equalsIgnoreCase(null) && !cur.getString(cur.getColumnIndex("readStatus")).equalsIgnoreCase("") && !cur.getString(cur.getColumnIndex("readStatus")).equalsIgnoreCase("null") && !cur.getString(cur.getColumnIndex("readStatus")).equalsIgnoreCase("(null)")) {
                        projectDetailsBean.setRead_status(Integer.valueOf(cur.getString(cur.getColumnIndex("readStatus"))));
                    }
                    projectDetailsBean.setProject_ownerName(cur.getString(cur.getColumnIndex("projectOwner")));
                    projectDetailsBean.setParentTaskId(cur.getString(cur.getColumnIndex("parentTaskId")));
                    projectDetailsBean.setFromUserId(cur.getString(cur.getColumnIndex("fromUserId")));
                    projectDetailsBean.setToUserId(cur.getString(cur.getColumnIndex("toUserId")));
                    projectDetailsBean.setFromUserName(cur.getString(cur.getColumnIndex("fromUserName")));
                    projectDetailsBean.setToUserName(cur.getString(cur.getColumnIndex("toUserName")));
                    projectDetailsBean.setTaskStatus(cur.getString(cur.getColumnIndex("taskStatus")));
                    projectDetailsBean.setCompletedPercentage(cur.getString(cur.getColumnIndex("completedPercentage")));
                    projectDetailsBean.setOwnerOfTask(cur.getString(cur.getColumnIndex("ownerOfTask")));
                    projectDetailsBean.setTaskReceiver(cur.getString(cur.getColumnIndex("taskReceiver")));
                    projectDetailsBean.setTaskObservers(cur.getString(cur.getColumnIndex("taskObservers")));
                    projectDetailsBean.setTaskMemberList(cur.getString(cur.getColumnIndex("taskMemberList")));
                    projectDetailsBean.setPlannedStartDateTime(cur.getString(cur.getColumnIndex("plannedStartDateTime")));
                    projectDetailsBean.setPlannedEndDateTime(cur.getString(cur.getColumnIndex("plannedEndDateTime")));
//                    projectDetailsBean.setListMemberProject(cur.getString(cur.getColumnIndex("listOfMembers")));
                    projectDetailsBean.setTaskNo(cur.getString(cur.getColumnIndex("taskNo")));
                    projectDetailsBean.setTaskName(cur.getString(cur.getColumnIndex("taskName")));
                    projectDetailsBean.setTaskDescription(cur.getString(cur.getColumnIndex("taskDescription")));
                    projectDetailsBean.setTaskType(cur.getString(cur.getColumnIndex("taskType")));
                    projectDetailsBean.setMimeType(cur.getString(cur.getColumnIndex("mimeType")));
                    projectDetailsBean.setTaskId(cur.getString(cur.getColumnIndex("taskId")));
                    projectDetailsBean.setCatagory(cur.getString(cur.getColumnIndex("category")));
                    projectDetailsBean.setIsParentTask(cur.getString(cur.getColumnIndex("isParentTask")));
                    projectDetailsBean.setIssueParentId(cur.getString(cur.getColumnIndex("issueParentId")));
                    projectDetailsBean.setRequestStatus(cur.getString(cur.getColumnIndex("requestStatus")));
                    projectDetailsBean.setOracleTaskId((cur.getString(cur.getColumnIndex("oracleTaskId"))));
                    projectDetailsBean.setEstimatedTravelHours((cur.getString(cur.getColumnIndex("estimatedTravelHrs"))));
                    projectDetailsBean.setEstimatedActivityHrs((cur.getString(cur.getColumnIndex("estimatedActivityHrs"))));
                    projectDetailsBean.setOracleProjectId(cur.getString(cur.getColumnIndex("oracleProjectId")));
                    projectDetailsBean.setCustomerName(cur.getString(cur.getColumnIndex("customerName")));
                    projectDetailsBean.setAddress(cur.getString(cur.getColumnIndex("address")));
                    projectDetailsBean.setMcModel(cur.getString(cur.getColumnIndex("mcModel")));
                    projectDetailsBean.setMcSrNo(cur.getString(cur.getColumnIndex("mcSrNo")));
                    projectDetailsBean.setServiceRequestDate(cur.getString(cur.getColumnIndex("serviceRequestDate")));
                    projectDetailsBean.setChasisNo(cur.getString(cur.getColumnIndex("chasisNo")));
                    projectDetailsBean.setObservation(cur.getString(cur.getColumnIndex("observation")));
                    projectDetailsBean.setOracleCustomerId(cur.getInt(cur.getColumnIndex("oracleCustomerId")));
                    projectDetailsBean.setActivity(cur.getString(cur.getColumnIndex("activity")));
                    projectDetailsBean.setProcessFlag(cur.getString(cur.getColumnIndex("processFlag")));
                    projectDetailsBean.setProjectCompletedStatus(cur.getString(cur.getColumnIndex("projectcompletedstatus")));
                    projectDetailsBean.setIsActiveStatus(cur.getString(cur.getColumnIndex("isActiveStatus")));
                    projectDetailsBean.setJobCardType(cur.getString(cur.getColumnIndex("jobCardType")));
                    projectDetailsBean.setMachineMake(cur.getString(cur.getColumnIndex("machineMake")));
                    Log.i("draft123", "DB before If" + projectDetailsBean.getTaskStatus());

                    if(projectDetailsBean.getTaskId()!=null && projectDetailsBean.getTaskStatus()!=null && !projectDetailsBean.getTaskStatus().equalsIgnoreCase("")){
//                            &&!projectDetailsBean.getTaskStatus().equalsIgnoreCase("draft") ){
                        Appreference.old_status.put(projectDetailsBean.getTaskId(),projectDetailsBean.getTaskStatus());
                        Log.i("draft123", "DB Appreference added status " + projectDetailsBean.getTaskStatus());
                        Log.i("draft123", "DB Appreference added ID" +projectDetailsBean.getTaskId());

                    }
                    arrayList.add(projectDetailsBean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.i("getProjectHistory", "size " + arrayList.size());
            return arrayList;
        }
    }


    public ArrayList<TaskDetailsBean> getProjectHistoryTasks(String query) {
        ArrayList<TaskDetailsBean> arrayList = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
//                "select * from taskDetailsInfo where loginuser='" + ""+ "'"
                cur = db.rawQuery(query, null);
                cur.moveToFirst();

                while (!cur.isAfterLast()) {
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                    taskDetailsBean.setProjectId(cur.getString(cur.getColumnIndex("projectId")));
                    taskDetailsBean.setTaskName(cur.getString(cur.getColumnIndex("projectName")));
                    taskDetailsBean.setDescription(cur.getString(cur.getColumnIndex("projectDescription")));
                    taskDetailsBean.setOrganisation(cur.getString(cur.getColumnIndex("projectOrganisation")));
                    taskDetailsBean.setParentTaskId(cur.getString(cur.getColumnIndex("parentTaskId")));
//                    if (cur.getString(cur.getColumnIndex("readStatus")) != null && !cur.getString(cur.getColumnIndex("readStatus")).equalsIgnoreCase("")) {
//                        projectDetailsBean.setRead_status(Integer.valueOf(cur.getString(cur.getColumnIndex("readStatus"))));
//                    } else {
                    taskDetailsBean.setRead_status(0);
//                    }
//                    projectDetailsBean.setRead_status(0);
                    taskDetailsBean.setProject_ownerName(cur.getString(cur.getColumnIndex("projectOwner")));
                    taskDetailsBean.setParentTaskId(cur.getString(cur.getColumnIndex("parentTaskId")));
                    taskDetailsBean.setFromUserId(cur.getString(cur.getColumnIndex("fromUserId")));
                    taskDetailsBean.setToUserId(cur.getString(cur.getColumnIndex("toUserId")));
                    taskDetailsBean.setFromUserName(cur.getString(cur.getColumnIndex("fromUserName")));
                    taskDetailsBean.setToUserName(cur.getString(cur.getColumnIndex("toUserName")));
                    taskDetailsBean.setTaskStatus(cur.getString(cur.getColumnIndex("taskStatus")));
                    taskDetailsBean.setCompletedPercentage(cur.getString(cur.getColumnIndex("completedPercentage")));
                    taskDetailsBean.setOwnerOfTask(cur.getString(cur.getColumnIndex("ownerOfTask")));
                    Log.i("VideocallDatabase", "getProjectHistoryTasks taskreceiver " + cur.getString(cur.getColumnIndex("taskReceiver")));
                    taskDetailsBean.setTaskReceiver(cur.getString(cur.getColumnIndex("taskReceiver")));
                    taskDetailsBean.setTaskObservers(cur.getString(cur.getColumnIndex("taskObservers")));
                    taskDetailsBean.setGroupTaskMembers(cur.getString(cur.getColumnIndex("taskMemberList")));
                    taskDetailsBean.setPlannedStartDateTime(cur.getString(cur.getColumnIndex("plannedStartDateTime")));
                    taskDetailsBean.setPlannedEndDateTime(cur.getString(cur.getColumnIndex("plannedEndDateTime")));
//                    projectDetailsBean.setListMemberProject(cur.getString(cur.getColumnIndex("listOfMembers")));
                    taskDetailsBean.setTaskNo(cur.getString(cur.getColumnIndex("taskNo")));
                    taskDetailsBean.setTaskName(cur.getString(cur.getColumnIndex("taskName")));
                    taskDetailsBean.setTaskDescription(cur.getString(cur.getColumnIndex("taskDescription")));
                    taskDetailsBean.setTaskType(cur.getString(cur.getColumnIndex("taskType")));
                    taskDetailsBean.setMimeType(cur.getString(cur.getColumnIndex("mimeType")));
                    taskDetailsBean.setTaskId(cur.getString(cur.getColumnIndex("taskId")));
                    taskDetailsBean.setCatagory(cur.getString(cur.getColumnIndex("category")));
                    /*taskDetailsBean.setOracleProjectId(cur.getString(cur.getColumnIndex("oracleProjectId")));
                    taskDetailsBean.setCustomerName(cur.getString(cur.getColumnIndex("customerName")));
                    taskDetailsBean.setAddress(cur.getString(cur.getColumnIndex("address")));
                    taskDetailsBean.setMcModel(cur.getString(cur.getColumnIndex("mcModel")));
                    taskDetailsBean.setMcSrNo(cur.getString(cur.getColumnIndex("mcSrNo")));
                    taskDetailsBean.setServiceRequestDate(cur.getString(cur.getColumnIndex("serviceRequestDate")));
                    taskDetailsBean.setChasisNo(cur.getString(cur.getColumnIndex("chasisNo")));
                    taskDetailsBean.setObservation(cur.getString(cur.getColumnIndex("observation")));
                    taskDetailsBean.setOracleCustomerId(cur.getInt(cur.getColumnIndex("oracleCustomerId")));
                    taskDetailsBean.setActivity(cur.getString(cur.getColumnIndex("activity")));
                    taskDetailsBean.setProcessFlag(cur.getString(cur.getColumnIndex("processFlag")));*/
                    arrayList.add(taskDetailsBean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            Log.i("getProjectHistory", "size " + arrayList.size());
            return arrayList;
        }
    }


    public ProjectDetailsBean settaskDetailsBeantoProjectDetailsBean(TaskDetailsBean taskDetailsBean) {
        ProjectDetailsBean projectBean = new ProjectDetailsBean();
        projectBean.setParentTaskId(taskDetailsBean.getParentTaskId());
        projectBean.setTaskName(taskDetailsBean.getTaskName());
        projectBean.setTaskNo(taskDetailsBean.getTaskNo());
        projectBean.setFromUserId(taskDetailsBean.getFromUserId());
        projectBean.setFromUserName(taskDetailsBean.getFromUserName());
        projectBean.setToUserId(taskDetailsBean.getToUserId());
        projectBean.setToUserName(taskDetailsBean.getToUserName());
        projectBean.setOwnerOfTask(taskDetailsBean.getOwnerOfTask());
        projectBean.setTaskId(taskDetailsBean.getTaskId());
        projectBean.setId(taskDetailsBean.getProjectId());
        projectBean.setTaskType(taskDetailsBean.getTaskType());
        projectBean.setTaskStatus(taskDetailsBean.getTaskStatus());
        projectBean.setTaskReceiver(taskDetailsBean.getTaskReceiver());
        projectBean.setTaskMemberList(taskDetailsBean.getGroupTaskMembers());
        return projectBean;
    }

    public ArrayList<ListMember> getGroupmemberHistory(String query) {
        ArrayList<ListMember> arrayList = new ArrayList<>();
        Log.d("videocalldatabase", "getGroupmemberHistory query  == " + query);
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(query, null);
                cur.moveToFirst();

                while (!cur.isAfterLast()) {
                    ListMember listMember = new ListMember();

                    listMember.setId(Integer.parseInt(cur.getString(cur.getColumnIndex("userid"))));
                    listMember.setEmail(cur.getString(cur.getColumnIndex("email")));
                    listMember.setUsername(cur.getString(cur.getColumnIndex("username")));
                    listMember.setCode(cur.getString(cur.getColumnIndex("code")));
                    listMember.setDepartmentId(cur.getString(cur.getColumnIndex("departmentId")));
                    listMember.setUserStatus(cur.getString(cur.getColumnIndex("userStatus")));
                    listMember.setLoginStatus(cur.getString(cur.getColumnIndex("loginStatus")));
                    listMember.setFirstName(cur.getString(cur.getColumnIndex("firstName")));
                    listMember.setLastName(cur.getString(cur.getColumnIndex("lastName")));
                    listMember.setTitle(cur.getString(cur.getColumnIndex("title")));
                    Log.i("VideocallDb", "org 1 " + cur.getString(cur.getColumnIndex("organization")));
                    listMember.setOrganization(cur.getString(cur.getColumnIndex("organization")));
                    listMember.setGender(cur.getString(cur.getColumnIndex("gender")));
                    listMember.setProfileImage(cur.getString(cur.getColumnIndex("profileImage")));
                    listMember.setMobileNo(cur.getString(cur.getColumnIndex("mobileNo")));
                    listMember.setLoginuser(cur.getString(cur.getColumnIndex("loginuser")));
                    listMember.setGroupId(cur.getString(cur.getColumnIndex("groupid")));
//                    listMember.setUserType(cur.getString(cur.getColumnIndex("userType")));
//                    listMember.setProfession(cur.getString(cur.getColumnIndex("profession")));
//                    listMember.setSpecialization(cur.getString(cur.getColumnIndex("specialization")));
                    if (!listMember.getUsername().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        arrayList.add(listMember);
                    }
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            Log.i("groupmembers", "arraylist size is " + arrayList.size());
            return arrayList;
        }
    }

//    public ArrayList<String> select

    public int getUserIdForUserName(String username) {
        Cursor cur = null;
        int from_userId=0;
        try {
            if (!db.isOpen())
                openDatabase();
            String query = "select userid from contact where username='" + username + "'";
            cur = db.rawQuery(query, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                from_userId = cur.getInt(0);
                cur.moveToNext();
            }
            cur.close();
            return from_userId;
        } catch (Exception e) {
            return from_userId;
        }
    }

    @SuppressWarnings("finally")
    public ArrayList<String> selectGroupmembers(String query, String value) {
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
//                "select * from taskDetailsInfo where loginuser='" + ""+ "'"
                cur = db.rawQuery(query, null);
                cur.moveToFirst();

                while (!cur.isAfterLast()) {
                    arrayList.add(cur.getString(cur.getColumnIndex(value)));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            Log.i("file", "size" + arrayList.size());
            return arrayList;
        }
    }

    @SuppressWarnings("finally")
    public ArrayList<String> selectGroupName(String query) {
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
//                "select * from taskDetailsInfo where loginuser='" + ""+ "'"
                cur = db.rawQuery(query, null);
                cur.moveToFirst();

                while (!cur.isAfterLast()) {
                    arrayList.add(cur.getString(cur.getColumnIndex("groupname")));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            Log.i("file", "size" + arrayList.size());
            return arrayList;
        }
    }

    @SuppressWarnings("finally")
    public ArrayList<String> selectContactList(String query) {
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
//                "select * from taskDetailsInfo where loginuser='" + ""+ "'"
                cur = db.rawQuery(query, null);
                cur.moveToFirst();

                while (!cur.isAfterLast()) {
                    arrayList.add(cur.getString(cur.getColumnIndex("username")));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            Log.i("file", "size " + arrayList.size());
            return arrayList;
        }
    }

    /*
        public int getchatUnReadMsgCount(String chatid) {
            ArrayList<ChatBean> arrayList = new ArrayList<>();
            Cursor cur;
            Log.i("chat", "opened-2 ** ");
            if (db == null)
                db = getReadableDatabase();
            try {
                if (db != null) {
                    if (!db.isOpen())
                        openDatabase();
                    cur = db.rawQuery("select * from taskDetailsInfo where taskId='" + chatid + "'", null);
                    cur.moveToFirst();
                    while (!cur.isAfterLast()) {
                        ChatBean chatBean = new ChatBean();
                        Log.i("chat", "opened-2 ");
                        chatBean.setOpened(cur.getString(cur.getColumnIndex("readStatus")));
                        arrayList.add(chatBean);
                        cur.moveToNext();
                    }
                    cur.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                return arrayList.size();
            }
        }
    */
    public int getchatUnReadMsgCount(String chatid) {
        ArrayList<ChatBean> arrayList = new ArrayList<>();
        Cursor cur;
        Log.i("chat", "opened-2 ** ");
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from taskDetailsInfo where taskId='" + chatid + "' and readStatus!='0'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    ChatBean chatBean = new ChatBean();
                    Log.i("chat", "opened-2 ");
                    chatBean.setOpened(cur.getString(cur.getColumnIndex("readStatus")));
                    arrayList.add(chatBean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return arrayList.size();
        }
    }


    @SuppressWarnings("finally")
    public ArrayList<String> selectContactName(String query) {
        ArrayList<String> arrayList = new ArrayList<>();
//        arrayList.add("- select -");
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
//                "select * from taskDetailsInfo where loginuser='" + ""+ "'"
                cur = db.rawQuery(query, null);
                cur.moveToFirst();

                while (!cur.isAfterLast()) {
                    if (!Appreference.loginuserdetails.getUsername().equalsIgnoreCase(cur.getString(cur.getColumnIndex("username")))) {
                        arrayList.add(cur.getString(cur.getColumnIndex("username")));
                    }
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            Log.i("videocalldatabase", "contactName size is " + arrayList.size());
            return arrayList;
        }
    }

    public int getContactsUnReadMsgCount(String username, String taskType) {
        ArrayList<TaskDetailsBean> arrayList = new ArrayList<>();
        Cursor cur;
        Log.i("contact", "msgcount-1");
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from taskDetailsInfo where (fromUserId='" + username + "' or toUserId='" + username + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and  (readStatus='1' or readStatus='2') and  taskType='" + taskType + "' and (projectId IS NULL or projectId=='null' or projectId=='') group by taskId", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                    Log.i("contact", "msgcount-2");
                    Log.i("contact", "description " + cur.getString(cur.getColumnIndex("taskDescription")));
                       /* ContactBean bean = new ContactBean();
                        bean.setFirstName(cur.getString(7));
                        bean.setLastName(cur.getString(8));*/
                    taskDetailsBean.setRead_status(cur.getInt(cur.getColumnIndex("readStatus")));
                    arrayList.add(taskDetailsBean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return arrayList.size();
        }
    }

    public int getProjectsUnReadMsgCount(String project_Id) {
        ArrayList<TaskDetailsBean> arrayList = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from projectHistory where loginuser='" + Appreference.loginuserdetails.getEmail() + "'and  readStatus!='0' and projectId=='" + project_Id + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                    taskDetailsBean.setRead_status(cur.getInt(cur.getColumnIndex("readStatus")));
                    arrayList.add(taskDetailsBean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return arrayList.size();
        }
    }

    public int getAllProjectsUnReadMsgCount() {
        ArrayList<TaskDetailsBean> arrayList = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from projectDetails where loginuser='" + Appreference.loginuserdetails.getEmail() + "' and  readStatus != '0'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                    taskDetailsBean.setRead_status(cur.getInt(cur.getColumnIndex("readStatus")));
                    arrayList.add(taskDetailsBean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return arrayList.size();
        }
    }

       public int getAllChatUnReadMsgCount() {
        ArrayList<ChatBean> arrayList = new ArrayList<>();
        Cursor cur, cur1;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                Log.i("chat", "DB name " + MainActivity.username);
//                cur = db.rawQuery("select * from chat where username='" + MainActivity.username + "' and  opened='1'", null);
                cur1 = db.rawQuery("select * from taskHistoryInfo where loginuser='" + Appreference.loginuserdetails.getEmail() + "' and category = 'chat'", null);
                cur1.moveToFirst();
                while (!cur1.isAfterLast()) {
                    ChatBean chatBean = new ChatBean();
                    String chatid = cur1.getString(cur1.getColumnIndex("taskId"));
                    cur = db.rawQuery("select * from taskDetailsInfo where taskId='" + chatid + "' and readStatus!='0' group by taskId", null);
                    cur.moveToFirst();
                    Log.i("chat", "DB name ## ==> ");
                    while (!cur.isAfterLast()) {
                        chatBean.setOpened(cur.getString(cur.getColumnIndex("readStatus")));
                        Log.i("chat", "DB name ## " + chatBean.getOpened());
                        arrayList.add(chatBean);
                        cur.moveToNext();
                    }
                    cur.close();
                    cur1.moveToNext();
                }
                cur1.close();
                Log.i("chat", "DB name @# " + arrayList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return arrayList.size();
        }
    }

    public int getAllContactsUnReadMsgCount() {
        ArrayList<TaskDetailsBean> arrayList = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from taskDetailsInfo where loginuser='" + Appreference.loginuserdetails.getEmail() + "' and  readStatus='1' and projectId IS NULL group by fromUserId", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                       /* ContactBean bean = new ContactBean();
                        bean.setFirstName(cur.getString(7));
                        bean.setLastName(cur.getString(8));*/
                    taskDetailsBean.setRead_status(cur.getInt(cur.getColumnIndex("readStatus")));
                    arrayList.add(taskDetailsBean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return arrayList.size();
        }
    }

    public ArrayList<ProjectDetailsBean> getProjectdetails(String query) {
        ArrayList<ProjectDetailsBean> arrayList = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(query, null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    ProjectDetailsBean projectDetailsBean = new ProjectDetailsBean();
                    projectDetailsBean.setId(cur.getString(cur.getColumnIndex("projectId")));
                    projectDetailsBean.setProjectName(cur.getString(cur.getColumnIndex("projectName")));
                    projectDetailsBean.setDescription(cur.getString(cur.getColumnIndex("projectDescription")));
                    projectDetailsBean.setRead_status(cur.getInt(cur.getColumnIndex("readStatus")));
                    projectDetailsBean.setProject_ownerName(cur.getString(cur.getColumnIndex("projectOwner")));
                    projectDetailsBean.setTaskStatus(cur.getString(cur.getColumnIndex("taskStatus")));
                    projectDetailsBean.setProjectCompletedPercentage(cur.getString(cur.getColumnIndex("completedPercentage")));
                    projectDetailsBean.setOracleProjectId(cur.getString(cur.getColumnIndex("oracleProjectId")));
                    projectDetailsBean.setIsActiveStatus(cur.getString(cur.getColumnIndex("isActiveStatus")));
                    arrayList.add(projectDetailsBean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("DB123","Error====>getProjectdetails===>"+e.getMessage());
        } finally {
            return arrayList;
        }
    }
    public ArrayList<TaskDetailsBean> getTravelDetails(String query) {
        ArrayList<TaskDetailsBean> arrayList = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(query, null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                    taskDetailsBean.setTravelStartTime(cur.getString(cur.getColumnIndex("travelStartTime")));
                    taskDetailsBean.setTravelEndTime(cur.getString(cur.getColumnIndex("travelEndTime")));
//                    taskDetailsBean.setActivityStartTime(cur.getString(cur.getColumnIndex("activityStartTime")));
//                    taskDetailsBean.setActivityEndTime(cur.getString(cur.getColumnIndex("activityEndTime")));
//                    taskDetailsBean.setToTravelStartTime(cur.getString(cur.getColumnIndex("totravelstartdatetime")));
//                    taskDetailsBean.setToTravelEndTime(cur.getString(cur.getColumnIndex("totravelenddatetime")));
                    Log.i("travel123","");
//                    if(taskDetailsBean.getTravelStartTime()!=null && !taskDetailsBean.getTravelStartTime().equalsIgnoreCase("")
//                            && taskDetailsBean.getTravelEndTime()!=null && !taskDetailsBean.getTravelEndTime().equalsIgnoreCase(""))
                    arrayList.add(taskDetailsBean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return arrayList;
        }
    }

    public ArrayList<ProjectDetailsBean> getExclationdetails(String query) {
        ArrayList<ProjectDetailsBean> arrayList = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(query, null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    ProjectDetailsBean projectDetailsBean = new ProjectDetailsBean();
                    if (cur.getString(cur.getColumnIndex("taskDescription")).contains("Escalation added")) {
                        if (cur.getString(cur.getColumnIndex("projectId")) != null) {
                            projectDetailsBean.setId(cur.getString(cur.getColumnIndex("projectId")));
                            projectDetailsBean.setProjectName("Yes");
                        } else {
                            projectDetailsBean.setId(cur.getString(cur.getColumnIndex("taskId")));
                            projectDetailsBean.setProjectName("NO");
                        }
                        arrayList.add(projectDetailsBean);
                    }
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return arrayList;
        }
    }

    public int getTaskUnReadMsgCount(String taskId) {
        ArrayList<TaskDetailsBean> arrayList = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                Log.i("task", "taskid " + taskId);
                Log.i("TaskHistory", "setActiveAdapter 3" + " 1***** " + " ********");
                cur = db.rawQuery("select * from taskDetailsInfo where  loginuser='" + Appreference.loginuserdetails.getEmail() + "'and  (readStatus='1' or readStatus='2') and taskId='" + taskId + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                       /* ContactBean bean = new ContactBean();
                        bean.setFirstName(cur.getString(7));
                        bean.setLastName(cur.getString(8));*/
                    Log.i("TaskHistory", "setActiveAdapter 3" + " 1***** " + " 2******** " + cur.getInt(cur.getColumnIndex("readStatus")));
                    taskDetailsBean.setRead_status(cur.getInt(cur.getColumnIndex("readStatus")));
                    arrayList.add(taskDetailsBean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.i("TaskHistory", "setActiveAdapter 3" + " 1***** " + " 2.1******** " + arrayList.size());
            return arrayList.size();
        }
    }

    public int getRemainderUnReadMsgCount(String taskId) {
        ArrayList<TaskDetailsBean> arrayList = new ArrayList<>();
        Log.i("Reminder", "arrayList.size() " + arrayList.size());
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from taskDetailsInfo where  loginuser='" + Appreference.loginuserdetails.getEmail() + "'and  readStatus='2' and taskId='" + taskId + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                       /* ContactBean bean = new ContactBean();
                        bean.setFirstName(cur.getString(7));
                        bean.setLastName(cur.getString(8));*/
                    taskDetailsBean.setRead_status(cur.getInt(cur.getColumnIndex("readStatus")));
                    arrayList.add(taskDetailsBean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Log.i("Reminder", "arrayList.size() final " + arrayList.size());
            return arrayList.size();
        }
    }


//    public boolean getTaskIsOverDue(String taskId) {
//        Cursor cur;
//        boolean b = false;
//        if (db == null)
//            db = getReadableDatabase();
//        try {
//            if (db != null) {
//                if (!db.isOpen())
//                    openDatabase();
//                cur = db.rawQuery("select * from taskDetailsInfo where  loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + taskId + "' and taskStatus='overdue'", null);
//                cur.moveToFirst();
//                while (!cur.isAfterLast()) {
//
//                     b = (cur.getString(cur.getColumnIndex("taskStatus"))).equalsIgnoreCase("overdue");
//                    Log.d("OverDue"," check overdue 1 inside db is" + b);
//                    cur.moveToNext();
//                }
//                cur.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//        return b;
//    }
//
//
//
//    public boolean getTaskIsClosed(String taskId) {
//        Cursor cur;
//        boolean b = false;
//        if (db == null)
//            db = getReadableDatabase();
//        try {
//            if (db != null) {
//                if (!db.isOpen())
//                    openDatabase();
//                cur = db.rawQuery("select * from taskDetailsInfo where  loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + taskId + "' and taskStatus='closed'", null);
//                cur.moveToFirst();
//                while (!cur.isAfterLast()) {
//
//                    b = (cur.getString(cur.getColumnIndex("taskStatus"))).equalsIgnoreCase("closed");
//                    Log.d("OverDue"," check closed 1 inside db is" + b);
//                    cur.moveToNext();
//                }
//                Log.d("OverDue"," check closed 1 outside db is" + b);
//                cur.close();
//                Log.d("OverDue"," check closed 1 outside 2 db is" + b);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Log.d("OverDue"," check closed 1 return 2 db is" + b);
//        return b;
//    }


    @SuppressWarnings("finally")
    public ArrayList<ChatBean> getChatHistoty(String chatuser, String username, String chatType) {
        ArrayList<ChatBean> beans = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from chat where username='" + username + "' and (opened='0' or opened='1') and chattype='" + chatType
                        + "' and chatmembers='" + chatuser + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    ChatBean bean = new ChatBean();
                    bean.setType(cur.getString(cur.getColumnIndex("chattype")));
                    bean.setChatname(cur.getString(cur.getColumnIndex("chatname")));
                    bean.setChatid(cur.getString(cur.getColumnIndex("chatid")));
                    bean.setUsername(cur.getString(cur.getColumnIndex("username")));
                    bean.setFromUser(cur.getString(cur.getColumnIndex("fromname")));
                    bean.setToname(cur.getString(cur.getColumnIndex("toname")));
                    bean.setSignalid(cur.getString(cur.getColumnIndex("signalid")));
                    bean.setMsgtype(cur.getString(cur.getColumnIndex("messagetype")));
                    bean.setMessage(cur.getString(cur.getColumnIndex("message")));
                    bean.setDatetime(cur.getString(cur.getColumnIndex("datetime")));
                    bean.setChatmembers(cur.getString(cur.getColumnIndex("chatmembers")));
                    bean.setMsg_status(Integer.parseInt(cur.getString(cur.getColumnIndex("msgstatus"))));
//                    bean.setStatus(cur.getString(10));
//                    bean.setImagepath(cur.getString(11));
//                    bean.setUserid(cur.getString(12));
//                    bean.setCoordinator(cur.getString(14));
                    beans.add(bean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            return beans;
        }
    }

    public String getSessionid(String sessionName) {
        Cursor cur = null;
        String sessionId = new String();
        try {
            if (!db.isOpen())
                openDatabase();
            String query = "Select groupid from group1 where groupname='" + sessionName + "'";
            cur = db.rawQuery(query, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                sessionId = cur.getString(0);
                cur.moveToNext();
            }
            cur.close();
            return sessionId;
        } catch (Exception e) {
            return null;
        }
    }

    public String getProjectListMembers(String taskId) {
        Cursor cur = null;
        String ListofMem = new String();
        try {
            if (!db.isOpen())
                openDatabase();
            String query = "select taskMemberList from projectHistory where taskId='" + taskId + "'";
            cur = db.rawQuery(query, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                ListofMem = cur.getString(0);
                cur.moveToNext();
            }
            cur.close();
            return ListofMem;
        } catch (Exception e) {
            return null;
        }
    }

    public String getReminderId(String user_name) {
        Cursor cur = null;
        String from_userId = new String();
        try {
            if (!db.isOpen())
                openDatabase();
            String query = "select userid from contact where username='" + user_name + "'";
            cur = db.rawQuery(query, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                from_userId = cur.getString(0);
                cur.moveToNext();
            }
            cur.close();
            return from_userId;
        } catch (Exception e) {
            return null;
        }
    }

    public String getGroupMemberAccess(String user_name) {
        Cursor cur = null;
        String from_userId = new String();
        try {
            if (!db.isOpen())
                openDatabase();
            String query = user_name;
            cur = db.rawQuery(query, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                from_userId = cur.getString(0);
                cur.moveToNext();
            }
            cur.close();
            Log.i("GroupAccess", " Access Value " + from_userId);
            return from_userId;
        } catch (Exception e) {
            return null;
        }
    }

    public String getReminderTaskname(String taskId) {
        Cursor cur = null;
        String rem_taskname = new String();
        try {
            if (!db.isOpen())
                openDatabase();
            String query = "select taskName from taskHistoryInfo where taskId='" + taskId + "'";
            cur = db.rawQuery(query, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                rem_taskname = cur.getString(0);
                cur.moveToNext();
            }
            cur.close();
            return rem_taskname;
        } catch (Exception e) {
            return null;
        }
    }

    public String getLastStatus(String taskId) {
        Cursor cur = null;
        String task_status = new String();
        try {
            if (!db.isOpen())
                openDatabase();
            String query = "select taskStatus from taskDetailsInfo where taskId='" + taskId + "'";
            cur = db.rawQuery(query, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                task_status = cur.getString(0);
                cur.moveToNext();
            }
            cur.close();
            return task_status;
        } catch (Exception e) {
            return null;
        }
    }

    public String getReminderTaskNo(String taskId) {
        Cursor cur = null;
        String rem_taskno = new String();
        try {
            if (!db.isOpen())
                openDatabase();
            String query = "select taskNo from taskDetailsInfo where taskId='" + taskId + "'";
            cur = db.rawQuery(query, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                rem_taskno = cur.getString(0);
                cur.moveToNext();
            }
            cur.close();
            return rem_taskno;
        } catch (Exception e) {
            return null;
        }
    }

    public String getReminderProjectId(String taskId) {
        Cursor cur = null;
        String rem_projectId = new String();
        try {
            if (!db.isOpen())
                openDatabase();
            String query = "select projectId from projectHistory where taskId='" + taskId + "'";
            cur = db.rawQuery(query, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                rem_projectId = cur.getString(0);
                cur.moveToNext();
            }
            cur.close();
            return rem_projectId;
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<ChatBean> getChatHistoty(String Query) {
        ArrayList<ChatBean> beans = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(Query, null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    ChatBean bean = new ChatBean();
                    bean.setType(cur.getString(0));
                    bean.setChatname(cur.getString(1));
                    bean.setChatid(cur.getString(2));
                    bean.setUsername(cur.getString(3));
                    bean.setFromUser(cur.getString(4));
                    bean.setToname(cur.getString(5));
                    bean.setSignalid(cur.getString(6));
                    bean.setMsgtype(cur.getString(7));
                    bean.setMessage(cur.getString(8));
                    bean.setDatetime(cur.getString(9));
                    bean.setChatmembers(cur.getString(15));
                    bean.setMsg_status(Integer.parseInt(cur.getString(10)));
                    bean.setPath(cur.getString(11));
                    bean.setMultimediaURL(cur.getString(16));
//                    bean.setStatus(cur.getString(10));
//                    bean.setImagepath(cur.getString(11));
//                    bean.setUserid(cur.getString(12));
//                    bean.setCoordinator(cur.getString(14));
                    beans.add(bean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            Log.i("file", "size " + beans.size());
            return beans;
        }
    }
/*
    @SuppressWarnings("finally")
    public String getName(String username) {
        String name = null;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from contact where username='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    name=cur.getString(4)+" "+cur.getString(5);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        finally

        {
            return name;
        }
    }*/


    @SuppressWarnings("finally")
    public ArrayList<Call_ListBean> getCallHistoty(String string) {
        ArrayList<Call_ListBean> beans = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from call where loginuser='" + string + "' ", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    Call_ListBean bean = new Call_ListBean();
                    bean.setType(cur.getString(cur.getColumnIndex("calltype")));
                    bean.setHost(cur.getString(cur.getColumnIndex("host")));
                    bean.setParticipant(cur.getString(cur.getColumnIndex("participant")));
                    bean.setStart_time(cur.getString(cur.getColumnIndex("start_time")));
                    bean.setCall_duration(cur.getString(cur.getColumnIndex("call_duration")));
                    bean.setRecording_path(cur.getString(cur.getColumnIndex("recording_path")));
                    beans.add(bean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return beans;
        }
    }

    public Vector<ChatBean> getChatname(String username) {

        // Vector<String> chatlist = new Vector<String>();
        // Cursor cur;
        // try {
        // if (!db.isOpen())
        // openDatabase();
        //
        // cur = db.rawQuery(
        // "select distinct chatname from chat where username='"
        // + username + "'", null);
        // cur.moveToFirst();
        // while (!cur.isAfterLast()) {
        // chatlist.add(cur.getString(0));
        // cur.moveToNext();
        //
        // }
        // cur.close();
        // } catch (Exception e) {
        // e.printStackTrace();
        // } finally {
        // return chatlist;
        // }
        Vector<ChatBean> chatlist = new Vector<ChatBean>();
        HashMap<String, ChatBean> chatlistmap = new HashMap<String, ChatBean>();

        Cursor cur;
        String open;
        try {
            Log.i("chat", "DB-getChatname");
            if (!db.isOpen())
                openDatabase();
            cur = db.rawQuery(
                    "select distinct chatname,chatid,chatmembers from chat where (fromname IS NOT NULL  and fromname!='') and username='"
                            + username + "'", null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                ChatBean bean = new ChatBean();
                bean.setChatname(cur.getString(0));
                bean.setChatid(cur.getString(1));
                bean.setChatmembers(cur.getString(2));
                if (cur.getString(2) != null) {
                    if ((cur.getString(2).split(",")[0]).equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))
                        bean.setChathistory_name(getName(cur.getString(2).split(",")[1]));
                    else
                        bean.setChathistory_name(getName(cur.getString(2).split(",")[0]));
                }
//                bean.setOpened(getopened(cur.getString(0)));
                if (!chatlistmap.containsKey(bean.getChatid())) {
                    chatlistmap.put(bean.getChatid(), bean);
                    chatlist.add(0, bean);
                }
//                Vector<ChatBean> chatlist2 = new Vector<ChatBean>();
//                chatlist2=chatlist;
//                for(ChatBean bn:chatlist2)
//                {
//                    if(bean.getChatid().equals(bn.getChatid()))
//                    {
//                        chatlist.remove(bn);
//                    }
//                }
//                chatlist.add(0, bean);
                cur.moveToNext();
            }
            cur.close();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        } finally {
//            return chatlistmap;
            return chatlist;
        }
    }


    public ArrayList<TaskDetailsBean> getChatnames() {
        ArrayList<TaskDetailsBean> chatlist = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                Log.i("chat", "DB-getChatname");
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from taskHistoryInfo where loginuser='" + Appreference.loginuserdetails.getEmail() + "' and category='chat'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    Log.i("chat", "while loop " + (cur.getString(cur.getColumnIndex("taskId"))));
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
//                    bean.setTaskId(cur.getString(cur.getColumnIndex("taskId")));
//                    bean.setOwnerOfTask(cur.getString(cur.getColumnIndex("ownerOfTask")));
//                    bean.setTaskReceiver(cur.getString(cur.getColumnIndex("taskReceiver")));
                    if (cur.getString(cur.getColumnIndex("dateTime")) != null) {
                        taskDetailsBean.setDateTime(Appreference.utcToLocalTime(cur.getString(cur.getColumnIndex("dateTime"))));
                        taskDetailsBean.setTaskUTCDateTime(cur.getString(cur.getColumnIndex("dateTime")));
                    }
                    taskDetailsBean.setTaskNo(cur.getString(cur.getColumnIndex("taskNo")));
                    taskDetailsBean.setTaskName(cur.getString(cur.getColumnIndex("taskName")));
                    taskDetailsBean.setTaskDescription(cur.getString(cur.getColumnIndex("taskDescription")));
                    taskDetailsBean.setTaskStatus(cur.getString(cur.getColumnIndex("taskStatus")));
                    taskDetailsBean.setSignalid(cur.getString(cur.getColumnIndex("signalid")));
                    taskDetailsBean.setCompletedPercentage(cur.getString(cur.getColumnIndex("completedPercentage")));
                    taskDetailsBean.setTaskType(cur.getString(cur.getColumnIndex("taskType")));
                    taskDetailsBean.setOwnerOfTask(cur.getString(cur.getColumnIndex("ownerOfTask")));
                    taskDetailsBean.setMimeType(cur.getString(cur.getColumnIndex("mimeType")));
                    taskDetailsBean.setTaskId(cur.getString(cur.getColumnIndex("taskId")));
                    taskDetailsBean.setCatagory(cur.getString(cur.getColumnIndex("category")));
                    taskDetailsBean.setTaskReceiver(cur.getString(cur.getColumnIndex("taskReceiver")));
                    taskDetailsBean.setTasktime(taskDetailsBean.getDateTime().split(" ")[1]);
                    taskDetailsBean.setTaskUTCTime(cur.getString(cur.getColumnIndex("tasktime")));
//                    taskDetailsBean.setTaskObservers(cur.getString(cur.getColumnIndex("taskObservers")));
//                    taskDetailsBean.setGroupTaskMembers(cur.getString(cur.getColumnIndex("taskMemberList")));


                    if (taskDetailsBean.getTaskType() != null) {
                        if (taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Individual")) {
                            if (taskDetailsBean.getOwnerOfTask() != null && Appreference.loginuserdetails.getUsername().equalsIgnoreCase(taskDetailsBean.getOwnerOfTask())) {
                                ContactBean contactBean = VideoCallDataBase.getDB(context).getContactObject(taskDetailsBean.getTaskReceiver());
                                taskDetailsBean.setToUserId(String.valueOf(contactBean.getUserid()));
                                taskDetailsBean.setToUserName(contactBean.getUsername());
                            } else {
                                ContactBean contactBean = VideoCallDataBase.getDB(context).getContactObject(taskDetailsBean.getOwnerOfTask());
                                taskDetailsBean.setToUserId(String.valueOf(contactBean.getUserid()));
                                taskDetailsBean.setToUserName(contactBean.getUsername());
                            }
                        } else {
                            taskDetailsBean.setToUserId(String.valueOf(VideoCallDataBase.getDB(context).getGroupId(taskDetailsBean.getTaskReceiver())));
                        }
                    }
                    chatlist.add(taskDetailsBean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return chatlist;
        }
    }

    public ArrayList<TaskDetailsBean> getChatnames(String str) {
        ArrayList<TaskDetailsBean> chatlist = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                Log.i("chat", "DB-getChatname");
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from taskHistoryInfo where loginuser='" + Appreference.loginuserdetails.getEmail() + "' and ((ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "' or taskReceiver='" + Appreference.loginuserdetails.getUsername() + "') or taskType='group' or taskType='Group') and (ownerOfTask='" + str + "' or taskReceiver='" + str + "') and category='chat'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    Log.i("chat", "while loop " + (cur.getString(cur.getColumnIndex("taskId"))));
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
//                    bean.setTaskId(cur.getString(cur.getColumnIndex("taskId")));
//                    bean.setOwnerOfTask(cur.getString(cur.getColumnIndex("ownerOfTask")));
//                    bean.setTaskReceiver(cur.getString(cur.getColumnIndex("taskReceiver")));
                    if (cur.getString(cur.getColumnIndex("dateTime")) != null) {
                        taskDetailsBean.setDateTime(Appreference.utcToLocalTime(cur.getString(cur.getColumnIndex("dateTime"))));
                        taskDetailsBean.setTaskUTCDateTime(cur.getString(cur.getColumnIndex("dateTime")));
                    }
                    taskDetailsBean.setTaskNo(cur.getString(cur.getColumnIndex("taskNo")));
                    taskDetailsBean.setTaskName(cur.getString(cur.getColumnIndex("taskName")));
                    taskDetailsBean.setTaskDescription(cur.getString(cur.getColumnIndex("taskDescription")));
                    taskDetailsBean.setTaskStatus(cur.getString(cur.getColumnIndex("taskStatus")));
                    taskDetailsBean.setSignalid(cur.getString(cur.getColumnIndex("signalid")));
                    taskDetailsBean.setCompletedPercentage(cur.getString(cur.getColumnIndex("completedPercentage")));
                    taskDetailsBean.setTaskType(cur.getString(cur.getColumnIndex("taskType")));
                    taskDetailsBean.setOwnerOfTask(cur.getString(cur.getColumnIndex("ownerOfTask")));
                    taskDetailsBean.setMimeType(cur.getString(cur.getColumnIndex("mimeType")));
                    taskDetailsBean.setTaskId(cur.getString(cur.getColumnIndex("taskId")));
                    taskDetailsBean.setCatagory(cur.getString(cur.getColumnIndex("category")));
                    taskDetailsBean.setTaskReceiver(cur.getString(cur.getColumnIndex("taskReceiver")));
                    taskDetailsBean.setTasktime(taskDetailsBean.getDateTime().split(" ")[1]);
                    taskDetailsBean.setTaskUTCTime(cur.getString(cur.getColumnIndex("tasktime")));
//                    taskDetailsBean.setTaskObservers(cur.getString(cur.getColumnIndex("taskObservers")));
//                    taskDetailsBean.setGroupTaskMembers(cur.getString(cur.getColumnIndex("taskMemberList")));


                    if (taskDetailsBean.getTaskType() != null) {
                        if (taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Individual")) {
                            if (taskDetailsBean.getOwnerOfTask() != null && Appreference.loginuserdetails.getUsername().equalsIgnoreCase(taskDetailsBean.getOwnerOfTask())) {
                                ContactBean contactBean = VideoCallDataBase.getDB(context).getContactObject(taskDetailsBean.getTaskReceiver());
                                taskDetailsBean.setToUserId(String.valueOf(contactBean.getUserid()));
                                taskDetailsBean.setToUserName(contactBean.getUsername());
                            } else {
                                ContactBean contactBean = VideoCallDataBase.getDB(context).getContactObject(taskDetailsBean.getOwnerOfTask());
                                taskDetailsBean.setToUserId(String.valueOf(contactBean.getUserid()));
                                taskDetailsBean.setToUserName(contactBean.getUsername());

                            }
                        } else {
                            taskDetailsBean.setToUserId(String.valueOf(VideoCallDataBase.getDB(context).getGroupId(taskDetailsBean.getTaskReceiver())));
                        }
                    }
                    chatlist.add(taskDetailsBean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return chatlist;
        }
    }
 public ArrayList<TaskDetailsBean> getChatnames(String str, String tasktype) {
        ArrayList<TaskDetailsBean> chatlist = new ArrayList<>();
        Cursor cur = null;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                Log.i("chat", "DB-getChatname");
                if (!db.isOpen())
                    openDatabase();
                if (tasktype !=null && !tasktype.equalsIgnoreCase("") && tasktype.equalsIgnoreCase("group")) {
                    cur = db.rawQuery("select * from taskHistoryInfo where loginuser='" + Appreference.loginuserdetails.getEmail() + "' and ((ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "' or taskReceiver='" + Appreference.loginuserdetails.getUsername() + "') and (taskType='group' or taskType='Group')) and (ownerOfTask='" + str + "' or taskReceiver='" + str + "') and category='chat'", null);
                } else if (tasktype.equalsIgnoreCase("individual")) {
                    cur = db.rawQuery("select * from taskHistoryInfo where loginuser='" + Appreference.loginuserdetails.getEmail() + "' and ((ownerOfTask='" + Appreference.loginuserdetails.getUsername() + "' or taskReceiver='" + Appreference.loginuserdetails.getUsername() + "') and (taskType='individual' or taskType='Individual')) and (ownerOfTask='" + str + "' or taskReceiver='" + str + "') and category='chat'", null);
                }
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    Log.i("chat", "while loop " + (cur.getString(cur.getColumnIndex("taskId"))));
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
//                    bean.setTaskId(cur.getString(cur.getColumnIndex("taskId")));
//                    bean.setOwnerOfTask(cur.getString(cur.getColumnIndex("ownerOfTask")));
//                    bean.setTaskReceiver(cur.getString(cur.getColumnIndex("taskReceiver")));
                    if (cur.getString(cur.getColumnIndex("dateTime")) != null) {
                        taskDetailsBean.setDateTime(Appreference.utcToLocalTime(cur.getString(cur.getColumnIndex("dateTime"))));
                        taskDetailsBean.setTaskUTCDateTime(cur.getString(cur.getColumnIndex("dateTime")));
                    }
                    taskDetailsBean.setTaskNo(cur.getString(cur.getColumnIndex("taskNo")));
                    taskDetailsBean.setTaskName(cur.getString(cur.getColumnIndex("taskName")));
                    taskDetailsBean.setTaskDescription(cur.getString(cur.getColumnIndex("taskDescription")));
                    taskDetailsBean.setTaskStatus(cur.getString(cur.getColumnIndex("taskStatus")));
                    taskDetailsBean.setSignalid(cur.getString(cur.getColumnIndex("signalid")));
                    taskDetailsBean.setCompletedPercentage(cur.getString(cur.getColumnIndex("completedPercentage")));
                    taskDetailsBean.setTaskType(cur.getString(cur.getColumnIndex("taskType")));
                    taskDetailsBean.setOwnerOfTask(cur.getString(cur.getColumnIndex("ownerOfTask")));
                    taskDetailsBean.setMimeType(cur.getString(cur.getColumnIndex("mimeType")));
                    taskDetailsBean.setTaskId(cur.getString(cur.getColumnIndex("taskId")));
                    taskDetailsBean.setCatagory(cur.getString(cur.getColumnIndex("category")));
                    taskDetailsBean.setTaskReceiver(cur.getString(cur.getColumnIndex("taskReceiver")));
                    taskDetailsBean.setTasktime(taskDetailsBean.getDateTime().split(" ")[1]);
                    taskDetailsBean.setTaskUTCTime(cur.getString(cur.getColumnIndex("tasktime")));
//                    taskDetailsBean.setTaskObservers(cur.getString(cur.getColumnIndex("taskObservers")));
//                    taskDetailsBean.setGroupTaskMembers(cur.getString(cur.getColumnIndex("taskMemberList")));


                    if (taskDetailsBean.getTaskType() != null) {
                        if (taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Individual")) {
                            if (taskDetailsBean.getOwnerOfTask() != null && Appreference.loginuserdetails.getUsername().equalsIgnoreCase(taskDetailsBean.getOwnerOfTask())) {
                                ContactBean contactBean = VideoCallDataBase.getDB(context).getContactObject(taskDetailsBean.getTaskReceiver());
                                taskDetailsBean.setToUserId(String.valueOf(contactBean.getUserid()));
                                taskDetailsBean.setToUserName(contactBean.getUsername());
                            } else {
                                ContactBean contactBean = VideoCallDataBase.getDB(context).getContactObject(taskDetailsBean.getOwnerOfTask());
                                taskDetailsBean.setToUserId(String.valueOf(contactBean.getUserid()));
                                taskDetailsBean.setToUserName(contactBean.getUsername());

                            }
                        } else {
                            taskDetailsBean.setToUserId(String.valueOf(VideoCallDataBase.getDB(context).getGroupId(taskDetailsBean.getTaskReceiver())));
                        }
                    }
                    chatlist.add(taskDetailsBean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return chatlist;
        }
    }

    public int updateChathistoryForAddMembers(String chatid, String members) {
        Cursor cur;
        int row_id = 0;
        try {
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            if (members != null) {
                cv.put("chatmembers", members);
            }
            row_id = (int) db.update("chat", cv, "chatid = '"
                    + chatid + "'", null);

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

        } finally {
            return row_id;
        }
    }


    @SuppressWarnings("finally")
    public ArrayList<ContactBean> getContact(String username) {
        ArrayList<ContactBean> beans = new ArrayList<>();
        ArrayList<ContactBean> duplicate_beans = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {

            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from contact where loginuser='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    if (!username.equalsIgnoreCase(cur.getString(cur.getColumnIndex("username")))) {

                        ContactBean bean = new ContactBean();
                        bean.setEmail(cur.getString(cur.getColumnIndex("contactemail")));
                        bean.setUserid(cur.getInt(cur.getColumnIndex("userid")));
                        bean.setUsername(cur.getString(cur.getColumnIndex("username")));
                        bean.setFirstname(cur.getString(cur.getColumnIndex("firstname")));
                        bean.setLastname(cur.getString(cur.getColumnIndex("lastname")));
                        bean.setCode(cur.getString(cur.getColumnIndex("code")));
                        bean.setTitle(cur.getString(cur.getColumnIndex("title")));
                        bean.setGender(cur.getString(cur.getColumnIndex("gender")));
                        bean.setProfileImage(cur.getString(cur.getColumnIndex("profileImage")));
                        bean.setPersonalInfo(cur.getString(cur.getColumnIndex("personalInfo")));
                        bean.setLoginuser(cur.getString(cur.getColumnIndex("loginuser")));
                        bean.setStatus(cur.getString(cur.getColumnIndex("presence")));
                        bean.setUserType(cur.getString(cur.getColumnIndex("userType")));
                        bean.setProfession(cur.getString(cur.getColumnIndex("profession")));
                        Log.i("VideocallDB", " organization value " + cur.getString(cur.getColumnIndex("organization")));
                        bean.setOrganization(cur.getString(cur.getColumnIndex("organization")));
                        bean.setSpecialization(cur.getString(cur.getColumnIndex("specialization")));
                        if (bean.getPersonalInfo() != null && bean.getPersonalInfo().equalsIgnoreCase("project_yes")) {
                            duplicate_beans.add(bean);
                        } else {
                            beans.add(bean);
                        }
                    }
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            return beans;
        }
    }


    @SuppressWarnings("finally")
    public ArrayList<ContactBean> getAllContact(String username) {
        ArrayList<ContactBean> beans = new ArrayList<>();
        ArrayList<ContactBean> duplicate_beans = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {

            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from contact where loginuser='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    ContactBean bean = new ContactBean();
                    bean.setEmail(cur.getString(cur.getColumnIndex("contactemail")));
                    bean.setUserid(cur.getInt(cur.getColumnIndex("userid")));
                    bean.setUsername(cur.getString(cur.getColumnIndex("username")));
                    bean.setFirstname(cur.getString(cur.getColumnIndex("firstname")));
                    bean.setLastname(cur.getString(cur.getColumnIndex("lastname")));
                    bean.setCode(cur.getString(cur.getColumnIndex("code")));
                    bean.setTitle(cur.getString(cur.getColumnIndex("title")));
                    bean.setGender(cur.getString(cur.getColumnIndex("gender")));
                    bean.setProfileImage(cur.getString(cur.getColumnIndex("profileImage")));
                    bean.setPersonalInfo(cur.getString(cur.getColumnIndex("personalInfo")));
                    bean.setLoginuser(cur.getString(cur.getColumnIndex("loginuser")));
                    bean.setStatus(cur.getString(cur.getColumnIndex("presence")));
                    bean.setUserType(cur.getString(cur.getColumnIndex("userType")));
                    bean.setProfession(cur.getString(cur.getColumnIndex("profession")));
                    Log.i("VideocallDB", " organization value " + cur.getString(cur.getColumnIndex("organization")));
                    bean.setOrganization(cur.getString(cur.getColumnIndex("organization")));
                    bean.setSpecialization(cur.getString(cur.getColumnIndex("specialization")));
                    if (bean.getPersonalInfo() != null && bean.getPersonalInfo().equalsIgnoreCase("project_yes")) {
                        duplicate_beans.add(bean);
                    } else {
                        beans.add(bean);
                    }
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            return beans;
        }
    }

    @SuppressWarnings("finally")
    public ArrayList<ContactBean> getContact(String username, String from, String to) {
        ArrayList<ContactBean> beans = new ArrayList<>();
        ArrayList<ContactBean> duplicate_beans = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {

            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from contact where loginuser='" + username + "' and (username != '" + from + "' or username != '" + to + "')", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    if (!username.equalsIgnoreCase(cur.getString(cur.getColumnIndex("username")))) {
                        ContactBean bean = new ContactBean();
                        bean.setEmail(cur.getString(cur.getColumnIndex("contactemail")));
                        bean.setUserid(cur.getInt(cur.getColumnIndex("userid")));
                        bean.setUsername(cur.getString(cur.getColumnIndex("username")));
                        bean.setFirstname(cur.getString(cur.getColumnIndex("firstname")));
                        bean.setLastname(cur.getString(cur.getColumnIndex("lastname")));
                        bean.setCode(cur.getString(cur.getColumnIndex("code")));
                        bean.setTitle(cur.getString(cur.getColumnIndex("title")));
                        bean.setGender(cur.getString(cur.getColumnIndex("gender")));
                        bean.setProfileImage(cur.getString(cur.getColumnIndex("profileImage")));
                        bean.setPersonalInfo(cur.getString(cur.getColumnIndex("personalInfo")));
                        bean.setLoginuser(cur.getString(cur.getColumnIndex("loginuser")));
                        bean.setStatus(cur.getString(cur.getColumnIndex("presence")));
                        bean.setUserType(cur.getString(cur.getColumnIndex("userType")));
                        bean.setProfession(cur.getString(cur.getColumnIndex("profession")));
                        Log.i("VideocallDB", " organization value " + cur.getString(cur.getColumnIndex("organization")));
                        bean.setOrganization(cur.getString(cur.getColumnIndex("organization")));
                        bean.setSpecialization(cur.getString(cur.getColumnIndex("specialization")));
                        if (bean.getPersonalInfo() != null && bean.getPersonalInfo().equalsIgnoreCase("project_yes")) {
                            duplicate_beans.add(bean);
                        } else {
                            beans.add(bean);
                        }
                    }
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            return beans;
        }
    }

    public ContactBean getContactObject(String username) {
        ContactBean bean = new ContactBean();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                Log.d("Task1", "query is " + "select * from contact where username=" + username);
                cur = db.rawQuery("select * from contact where username='" + username + "'and roleId NOT IN (select roleId from contact where roleId like '2')", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    Log.i("admin123","username====>"+cur.getString(cur.getColumnIndex("username")));
                    Log.i("admin123","RoleId====>"+cur.getString(cur.getColumnIndex("roleId")));
                    bean.setEmail(cur.getString(cur.getColumnIndex("contactemail")));
                    bean.setUserid(cur.getInt(cur.getColumnIndex("userid")));
                    bean.setUsername(cur.getString(cur.getColumnIndex("username")));
                    bean.setFirstname(cur.getString(cur.getColumnIndex("firstname")));
                    bean.setLastname(cur.getString(cur.getColumnIndex("lastname")));
                    bean.setCode(cur.getString(cur.getColumnIndex("code")));
                    bean.setTitle(cur.getString(cur.getColumnIndex("title")));
                    bean.setGender(cur.getString(cur.getColumnIndex("gender")));
                    bean.setProfileImage(cur.getString(cur.getColumnIndex("profileImage")));
                    bean.setPersonalInfo(cur.getString(cur.getColumnIndex("personalInfo")));
                    bean.setLoginuser(cur.getString(cur.getColumnIndex("loginuser")));
                    bean.setRoleId(cur.getString(cur.getColumnIndex("roleId")));
                    bean.setRoleName(cur.getString(cur.getColumnIndex("roleName")));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return bean;
        }
    }

    @SuppressWarnings("finally")
    public ArrayList<ContactBean> getGroup(String username) {
        ArrayList<ContactBean> beans = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from group1 where loginuser='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    ContactBean bean = new ContactBean();
                    bean.setContact_id(cur.getInt(cur.getColumnIndex("groupid")));
                    bean.setGroupName(cur.getString(cur.getColumnIndex("groupname")));
                    bean.setGroupOwner(cur.getString(cur.getColumnIndex("groupowner")));
                    bean.setDepartmentId(cur.getString(cur.getColumnIndex("departmentid")));
                    bean.setDescription(cur.getString(cur.getColumnIndex("description")));
                    bean.setLogo(cur.getString(cur.getColumnIndex("logo")));
                    bean.setGroupLogo(cur.getString(cur.getColumnIndex("grouplogo")));
                    bean.setDepartmentref(cur.getString(cur.getColumnIndex("departmentref")));
                    bean.setLoginuser(cur.getString(cur.getColumnIndex("loginuser")));
                    beans.add(bean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return beans;
        }
    }

    public String selectGroupTaskListMembers(String group_Id) {
        String groupTaskMembers = "";
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from taskDetailsInfo where taskId='" + group_Id + "' order by id ASC LIMIT 1", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    groupTaskMembers = cur.getString(cur.getColumnIndex("taskMemberList"));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return groupTaskMembers;
        }
    }

    @SuppressWarnings("finally")
    public ArrayList<String> getGroupMember(String username) {


        username = username;
        ArrayList<String> beans = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from groupmember where groupid='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                       /* ContactBean bean = new ContactBean();
                        bean.setFirstName(cur.getString(7));
                        bean.setLastName(cur.getString(8));*/
                    beans.add(cur.getString(cur.getColumnIndex("firstName")) + " " + cur.getString(cur.getColumnIndex("lastName")));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return beans;
        }
    }

    @SuppressWarnings("finally")
    public String getProjectParentTaskId(String username) {

        Log.i("Escalation", "username : " + username);
        username = username;
        String parenttaskid = new String();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(username, null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                       /* ContactBean bean = new ContactBean();
                        bean.setFirstName(cur.getString(7));
                        bean.setLastName(cur.getString(8));*/
                    parenttaskid = cur.getString(0);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return parenttaskid;
        }
    }


    public Integer getParentTaskId(String username) {

        Integer parenttaskid = 0;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(username, null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    parenttaskid = cur.getInt(0);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return parenttaskid;
        }
    }

    public String getfirstname(String username) {

        String parenttaskid = null;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(username, null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    parenttaskid = cur.getString(0);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return parenttaskid;
        }
    }

    public Integer getLastname(String username) {

        Integer parenttaskid = 0;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(username, null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    parenttaskid = cur.getInt(0);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return parenttaskid;
        }
    }

    @SuppressWarnings("finally")
    public ArrayList<String> getProjectTaskId(String username) {


        username = username;
        ArrayList<String> beans = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select taskId from projectHistory where projectId='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                       /* ContactBean bean = new ContactBean();
                        bean.setFirstName(cur.getString(7));
                        bean.setLastName(cur.getString(8));*/
                    beans.add(cur.getString(0));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return beans;
        }
    }

    @SuppressWarnings("finally")
    public ArrayList<Integer> getGroups(String username) {
        ArrayList<Integer> beans = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from group1 where loginuser='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                   /* ContactBean bean = new ContactBean();
                    bean.setFirstName(cur.getString(7));
                    bean.setLastName(cur.getString(8));*/
//                    beans.add(cur.getString(cur.getColumnIndex("groupid")));
//                    beans.add(cur.getString(cur.getColumnIndex("groupname")));
                    beans.add(cur.getInt(cur.getColumnIndex("groupid")));
//                    beans.add(cur.getString(cur.getColumnIndex("logo")));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            return beans;
        }
    }

    @SuppressWarnings("finally")
    public String getGroupName(String username) {
        String beans = "";
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from group1 where groupid='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                   /* ContactBean bean = new ContactBean();
                    bean.setFirstName(cur.getString(7));
                    bean.setLastName(cur.getString(8));*/
//                    beans.add(cur.getString(cur.getColumnIndex("groupid")));
//                    beans.add(cur.getString(cur.getColumnIndex("groupname")));
                    beans = cur.getString(cur.getColumnIndex("groupname"));
//                    beans.add(cur.getString(cur.getColumnIndex("logo")));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            return beans;
        }
    }

    @SuppressWarnings("finally")
    public String getGroupImage(String username) {
        String beans = new String();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select logo from group1 where groupid='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    beans = cur.getString(cur.getColumnIndex("logo"));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return beans;
        }
    }

    @SuppressWarnings("finally")
    public int getGroupId(String username) {
        int i = 0;
        String beans = new String();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select groupid from group1 where groupname='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    i = cur.getInt(cur.getColumnIndex("groupid"));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return i;
        }
    }


    @SuppressWarnings("finally")
    public ArrayList<String> getMemberImage(String username) {
        ArrayList<String> beans = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select profileImage from groupmember where groupid='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    beans.add(cur.getString(0));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            return beans;
        }
    }

    @SuppressWarnings("finally")
    public String getName(String username) {
        String name = null;
        Log.i("task", "string value " + username);
        Cursor cur;

        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from contact where username='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    Log.i("task", "string value " + cur.getString(cur.getColumnIndex("firstname")) + " " + cur.getString(cur.getColumnIndex("lastname")));
                    name = cur.getString(cur.getColumnIndex("firstname")) + " " + cur.getString(cur.getColumnIndex("lastname"));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return name;
        }
    }

    @SuppressWarnings("finally")
    public int getUserid(String username) {
        int name = 0;
        Log.i("task", "string value" + username);
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from contact where username='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    Log.i("task", "string value" + cur.getString(cur.getColumnIndex("userid")));
                    name = Integer.parseInt(cur.getString(cur.getColumnIndex("userid")));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return name;
        }
    }


    public void updateChatSentStatus(String signalID) {
        try {
            String s = "update chat set msgstatus=1 where signalid='" + signalID + "'";
            Log.d("abcdef", "SQL => " + s);
            db.execSQL(s);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("abcdef", "novalue in DB for " + signalID + " returns with error " + e.toString());
        }
    }

    public void updateTaskSentStatus(String signalID) {
        try {
            String s = "update taskDetailsInfo set msgstatus=1 where signalid='" + signalID + "'";
            db.execSQL(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateallmessage(String taskID, String msgstatus) {
        ContentValues cv = new ContentValues();
        cv.put("msgstatus", msgstatus);
        Log.i("database", "updateallmessage " + msgstatus);
        db.update("taskDetailsInfo", cv, "taskId" + "= ? and " + "(msgstatus" + "= ? or " + "msgstatus= ? )", new String[]{taskID, "24", "0"});
    }

    public void updateTaskSentStatus(String signalID, String msgstatus) {
//        try {
//            String s = "update taskDetailsInfo set msgstatus=1 where signalid='" + signalID + "'";
//            db.execSQL(s);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Log.i("sipmessage", "DB "+msgstatus);
        ContentValues cv = new ContentValues();
        cv.put("msgstatus", msgstatus);
        db.update("taskDetailsInfo", cv, "signalid" + "= ?", new String[]{signalID});
    }

    public void updateTaskNoteStatus(String signalID, String msgstatus) {
        ContentValues cv = new ContentValues();
        cv.put("msgstatus", msgstatus);
        db.update("taskDetailsInfo", cv, "taskId" + "= ?", new String[]{signalID});
    }

    public void updateTaskMsgReadStatus(String taskId) {
        try {
            String s = "update taskDetailsInfo set readStatus=0 where taskId='" + taskId + "'";
            db.execSQL(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateprojectMsgReadStatus(String taskId) {
        try {
            String s = "update taskDetailsInfo set readStatus=0 where taskId='" + taskId + "'";
            db.execSQL(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTaskMsgPriority(String taskNo) {
        try {
            String s1 = "Medium";
            String s = "update taskDetailsInfo set taskPriority = '" + s1 + "' where taskNo='" + taskNo + "'";
            db.execSQL(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTaskRemainderPriority(String taskNo) {
        try {
            String s1 = "Medium";
            String s = "update taskDetailsInfo set taskPriority = '" + s1 + "' where taskNo='" + taskNo + "'";
            db.execSQL(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTaskProgressStatus(String signalID) {
        try {
            Log.i("downloadVideo", "OnPostExcute DB");
            String s = "update taskDetailsInfo set showprogress=1 where signalid='" + signalID + "'";
            db.execSQL(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatetaskIndiv_Owner(String taskid) {
        try {
//            String s = "update taskDetailsInfo set completedPercentage='100' where taskId='" + signalID + "'";
            String s = "update taskDetailsInfo set taskStatus='Completed' , completedPercentage='100' where taskId='" + taskid + "' and taskStatus = 'inprogress'";
            db.execSQL(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatetaskstatus(String taskid) {
        try {
//            String s = "update taskDetailsInfo set completedPercentage='100' where taskId='" + signalID + "'";
            String s = "update taskDetailsInfo set taskStatus='closed' , completedPercentage='100' where taskId='" + taskid + "' and (taskStatus = 'inprogress' or taskStatus = 'Completed')";
            db.execSQL(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getlastCompletedParcentage(String taskid) {

        String value = null;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select completedPercentage from taskHistoryInfo where taskId='" + taskid + "' order by id desc limit 1", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    Log.i("VideoCallDataBase", "getlastCompletedParcentage Method -- completedPercentage value is ==   " + cur.getString(0));
                    if (cur.getString(0) != null) {
                        value = cur.getString(0);
                    } else {
                        value = "0";
                    }
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return value;
        }
    }

    public String getlastProjectCompletedPercentage(String taskid) {

        String value = null;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select completedPercentage from projectHistory where taskId='" + taskid + "' order by id desc limit 1", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    Log.i("VideoCallDataBase", "getlastCompletedParcentage projectHistoryMethod -- completedPercentage value is ==   " + cur.getString(0));
                    if (cur.getString(0) != null) {
                        value = cur.getString(0);
                    } else {
                        value = "0";
                    }
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return value;
        }
    }

    public String getlastCompletedParcentageproject(String taskid) {

        String value = null;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select completedPercentage from taskDetailsInfo where taskId='" + taskid + "' order by id desc limit 1", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    Log.i("VideoCallDataBase", "getlastCompletedParcentage Method -- completedPercentage value is ==   " + cur.getString(0));
                    if (cur.getString(0) != null) {
                        value = cur.getString(0);
                    } else {
                        value = "0";
                    }
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return value;
        }
    }

    public String getlastCompletedParcentagesender(String taskid) {

        String value = null;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select fromUserName from taskDetailsInfo where completedPercentage!='0' and taskId='" + taskid + "' order by id desc limit 1", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    Log.i("VideoCallDataBase", "getlastCompletedParcentage Method -- completedPercentage value is ==   " + cur.getString(0));
                    if (cur.getString(0) != null) {
                        value = cur.getString(0);
                    } else {
                        value = "0";
                    }
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return value;
        }
    }

    public String getTakerlastCompletedParcentage(String taskid) {

        String value = null;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select completedPercentage from taskDetailsInfo where fromUserName='" + Appreference.loginuserdetails.getUsername() + "' and taskId='" + taskid + "' and taskDescription like '" + "Completed Percentage %" + "' order by id desc LIMIT 1", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    Log.i("task", "string value" + cur.getString(0));
                    value = cur.getString(0);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return value;
        }
    }


    public String ClosedChecker(String taskid) {

        String value = null;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select taskDescription from taskDetailsInfo where taskId='" + taskid + "' and taskDescription='This task is closed' order by id desc limit 1", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    Log.i("videocalldatabase", "string value " + cur.getString(0));
                    value = cur.getString(0);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return value;
        }
    }

    public void updateGroupTaskStatus(String taskid, String completed) {
        try {
//            String s = "update taskDetailsInfo set completedPercentage='100' where taskId='" + signalID + "'";
            String s = "update taskDetailsInfo set taskStatus='inprogress' , completedPercentage='" + completed + "' where taskId='" + taskid + "' and taskStatus != 'reminder'";
//            String s = "update taskDetailsInfo set taskStatus='inprogress' , completedPercentage='" + completed + "' where taskId='" + taskid + "' and taskStatus = 'closed'";
            db.execSQL(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void updateReminderTaskStatus(String taskid, int count) {
        try {
            String s = "update taskDetailsInfo set taskStatus='reminder'  where taskId='" + taskid + "' and msgstatus = '" + count + "'";
            db.execSQL(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    public void updateReminderTaskStatus(String taskid) {

        // String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "sendStatus = '" + query + "'WHERE " + "taskNo" + " = " + taskNo;

        ContentValues cv = new ContentValues();
        cv.put("taskStatus", "reminder");
        db.update("taskDetailsInfo", cv, "showprogress=? and taskId=?", new String[]{"12", taskid});
        Log.d("task1234", "RequestStatus DB updated");
    }

    /*public void updateReminderLastStatus(String signalid) {

        // String query1 = "UPDATE " + "taskDetailsInfo" + " SET " + "sendStatus = '" + query + "'WHERE " + "taskNo" + " = " + taskNo;

        ContentValues cv = new ContentValues();
        cv.put("showprogress", "11");
        db.update("taskDetailsInfo", cv, "signalid=?", new String[]{signalid});
        Log.d("task1234", "RequestStatus DB updated");
    }*/

    public void updateGroupCloseTaskStatus(String taskid, String completed) {
        try {
//            String s = "update taskDetailsInfo set completedPercentage='100' where taskId='" + signalID + "'";
            String s = "update taskDetailsInfo set taskStatus='closed' , completedPercentage='" + completed + "' where taskId='" + taskid + "' and (taskStatus = 'inprogress' or taskStatus = 'Completed')";
//            String s = "update taskDetailsInfo set taskStatus='inprogress' , completedPercentage='" + completed + "' where taskId='" + taskid + "' and taskStatus = 'closed'";
            db.execSQL(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updategrouptaskstatus(String query) {
        try {
//            String s = "update taskDetailsInfo set taskStatus='Closed' where taskId='" + taskid + "' and (taskStatus = 'Completed' or taskStatus ='inprogress')";
            db.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updategrouptaskstatus1(String taskid) {
        try {
            String s = "update taskDetailsInfo set taskStatus='inprogress' where taskId='" + taskid + "' and (taskStatus = 'Closed')";
            db.execSQL(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateaccept(String query) {
        try {
//            String s = "update taskDetailsInfo set taskStatus='inprogress' where taskId='" + taskid + "'";
            db.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStatus(String taskid) {
        try {
            String s = "update taskHistoryInfo set taskStatus='overdue' where taskId='" + taskid + "'";
            db.execSQL(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateOverdueStatus(String taskid) {
        try {
            String s = "update taskDetailsInfo set msgstatus=10 where taskStatus='overdue' and taskId='" + taskid + "' ";
            db.execSQL(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updatereject(String taskid) {
        try {
            String s = "update taskDetailsInfo set taskStatus='rejected' where taskId='" + taskid + "' and taskStatus != 'reminder'";
            db.execSQL(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("finally")
    public boolean GroupPercentage(String namelist, String idvalue) {
        int p = 0;
        int s = 0;
        boolean list = false;
        Cursor cur;
        String name = null;
        Log.i("Task1", "DBpercentage" + namelist);
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();

                cur = db.rawQuery("select completedPercentage from taskDetailsInfo where fromUserName='" + namelist + "' and taskId = '" + idvalue + "' ", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {

                    Log.i("Task1", "DBDescription" + cur.getString(0));
                    Log.e("Task1", "DBpercentage" + name);
                    name = cur.getString(0);
                    if (name != null && !name.equalsIgnoreCase("")) {
                        Log.e("Task1", "DBpercentage" + name);
                        s = Integer.parseInt(name);
                        if (s == 100) {
                            list = true;
                        }
                    }
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }


    @SuppressWarnings("finally")
    public int percentagechecker(String username) {
        int p = 0;
        int s = 0;
        Cursor cur;
        String name = null;
        Log.i("Task1", "DBpercentage" + username);
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();

                cur = db.rawQuery("select completedPercentage,taskDescription from taskDetailsInfo where taskId='" + username + "' ", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    name = cur.getString(1);
                    Log.i("Task1", "DBDescription-->" + cur.getString(1));
                    if (name != null && name.contains("Completed Percentage ")) {
                        Log.e("Task1", "DBpercentage--->" + name);
                        name = cur.getString(0);
                        if (name != null && !name.equalsIgnoreCase("") && !name.equalsIgnoreCase(null)) {
                            Log.e("Task1", "DBpercentage---->" + name);
                            s = Integer.parseInt(name);
                            if (s > 0) {
                                p = Integer.parseInt(name);
                            }
                        }
                    }
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return p;
        }
    }


    @SuppressWarnings("finally")
    public int GroupMemberpercentagechecker(String username) {
        int p = 0;
        int s = 0;
        Cursor cur;
        String name = null;
        Log.i("Task1", "DBpercentage" + username);
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();

                cur = db.rawQuery("select completedPercentage,taskDescription from taskDetailsInfo where taskId='" + username + "' and fromUserName='" + Appreference.loginuserdetails.getUsername().toString() + "' ", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    name = cur.getString(1);
                    Log.i("Task1", "DBDescription-->" + cur.getString(1));
                    if (name != null && name.contains("Completed Percentage ")) {
                        Log.e("Task1", "DBpercentage--->" + name);
                        name = cur.getString(0);
                        if (name != null && !name.equalsIgnoreCase("")) {
                            Log.e("Task1", "DBpercentage---->" + name);
                            s = Integer.parseInt(name);
                            if (s > 0) {
                                p = Integer.parseInt(name);
                            }
                        }
                    }
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return p;
        }
    }


    @SuppressWarnings("finally")
    public int getownartask(String ste, String username) {
        int p = 0;
        int s = 0;
        Cursor cur;
        String name = null;
        Log.i("Task1", "DBpercentage" + username);
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();

                cur = db.rawQuery("select completedPercentage,taskDescription from taskDetailsInfo where taskId='" + username + "' and ownerOfTask = '" + ste + "' ", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    name = cur.getString(1);
                    Log.i("Task1", "DBDescription-->" + cur.getString(1));
                    if (name.contains("Completed Percentage ")) {
                        Log.e("Task1", "DBpercentage--->" + name);
                        name = cur.getString(0);
                        if (name != null && !name.equalsIgnoreCase("")) {
                            Log.e("Task1", "DBpercentage---->" + name);
                            s = Integer.parseInt(name);
                            if (s > 0) {
                                p = Integer.parseInt(name);
                            }
                        }
                    }
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return p;
        }
    }

    @SuppressWarnings("finally")
    public String getname(String username) {
        String name = null;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from contact where username='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    name = cur.getString(cur.getColumnIndex("firstname")) + " " + cur.getString(cur.getColumnIndex("lastname"));
                    cur.moveToNext();
                }
                cur.close();
            }
            if (name == null) {
                name = username;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (name == null) {
                name = username;
            }
        } finally {
            return name;
        }
    }

    public String getusername(String firstname, String lastname) {
        String name = null;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from contact where firstname='" + firstname + "' and lastname='" + lastname + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    name = cur.getString(cur.getColumnIndex("username"));
                    cur.moveToNext();
                }
                cur.close();
            }
            if (name == null) {
                name = firstname + " " + lastname;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (name == null) {
                name = firstname + " " + lastname;
            }
        } finally {
            return name;
        }
    }
	 public String getusernameWithOutLast(String firstname) {
        String name = null;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from contact where firstname='" + firstname + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    name = cur.getString(cur.getColumnIndex("username"));
                    cur.moveToNext();
                }
                cur.close();
            }
            if (name == null) {
                name = firstname;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (name == null) {
                name = firstname;
            }
        } finally {
            return name;
        }
    }


    @SuppressWarnings("finally")
    public String getTemplateTouserName(String username) {
        String name = null;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select username from contact where userid='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    name = cur.getString(0);
                    cur.moveToNext();
                }
                cur.close();
            }
            if (name == null) {
                name = username;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (name == null) {
                name = username;
            }
        } finally {
            return name;
        }
    }
/*
    public String getcallname(String username) {
        String name = null;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from contact where username='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    name = cur.getString(4) + " " + cur.getString(5);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return name;
        }
    }
*/


    @SuppressWarnings("finally")
    public ArrayList<String> getcontactdetails(String username) {
        ArrayList<String> beans = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from contact where contactemail='" + username + "'and loginUser ='" + Appreference.loginuserdetails.getUsername() + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    beans.add(cur.getString(cur.getColumnIndex("firstname")));
                    beans.add(cur.getString(cur.getColumnIndex("lastname")));
                    beans.add(cur.getString(cur.getColumnIndex("title")));
                    beans.add(cur.getString(cur.getColumnIndex("gender")));
                    beans.add(cur.getString(cur.getColumnIndex("profileImage")));
                    beans.add(cur.getString(cur.getColumnIndex("job1")));
                    beans.add(cur.getString(cur.getColumnIndex("job2")));
                    beans.add(cur.getString(cur.getColumnIndex("job3")));
                    beans.add(cur.getString(cur.getColumnIndex("job4")));
                    beans.add(cur.getString(cur.getColumnIndex("textprofile")));
                    beans.add(cur.getString(cur.getColumnIndex("videoprofile")));
                    beans.add(cur.getString(cur.getColumnIndex("userType")));
                    beans.add(cur.getString(cur.getColumnIndex("profession")));
                    beans.add(cur.getString(cur.getColumnIndex("specialization")));
                    beans.add(cur.getString(cur.getColumnIndex("organization")));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return beans;
        }
    }

    @SuppressWarnings("finally")
    public ArrayList<String> getGroupMembers(String groupId) {
        ArrayList<String> members = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from groupmember where  groupid ='" + groupId + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    if (!cur.getString(cur.getColumnIndex("username")).equalsIgnoreCase(MainActivity.username)) {
                        members.add(cur.getString(cur.getColumnIndex("username")));
                    }
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return members;
        }
    }

    @SuppressWarnings("finally")
    public ArrayList<String> getAllGroupMembers(String groupId) {
        ArrayList<String> members = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from groupmember where  groupid ='" + groupId + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    members.add(cur.getString(cur.getColumnIndex("username")));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return members;
        }
    }


    @SuppressWarnings("finally")
    public ArrayList<String> getSetid(String groupId) {
        ArrayList<String> beans = new ArrayList<>();
        Log.i("Task", "ArrayList" + groupId);
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();

                cur = db.rawQuery(groupId, null);
                Log.i("Task", "Database");
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    Log.i("Task", "ArrayList" + (cur.getString(cur.getColumnIndex("customSetId"))));
                    if (!cur.getString(cur.getColumnIndex("customSetId")).equalsIgnoreCase("0"))
                        beans.add(cur.getString(cur.getColumnIndex("customSetId")));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return beans;
        }
    }

    /**
     * Group percentage value is converted to average
     */

    @SuppressWarnings("finally")
    public int GroupPercentageChecker(String username, String taskid, String ownerOfTask) {
        int p = 0;
        int s = 0;
        int total = 0, i = 0;
        Cursor cur, cur1;
        String name = null;
        Log.i("videocalldatabase", "DBpercentage loginuser -> " + username);
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();

                cur1 = db.rawQuery("select username from groupmember where groupid = '" + username + "'", null);
                cur1.moveToFirst();
//                outerloop:
                while (!cur1.isAfterLast()) {
                    name = cur1.getString(0);
                    Log.i("videocalldatabase", "group member's name and owneroftask " + name + " " + ownerOfTask);
                    if (!name.equalsIgnoreCase(ownerOfTask)) {
                        i = i + 1;
                        cur = db.rawQuery("select completedPercentage,taskDescription from taskDetailsInfo where fromUserName = '" + name + "' and taskId='" + taskid + "' ", null);
                        cur.moveToFirst();
                        while (!cur.isAfterLast()) {
                            name = cur.getString(1);
                            Log.i("videocalldatabase", "DBDescription taskDescription --> " + cur.getString(1));
//                            if (name.equalsIgnoreCase("This task is closed")) {
//                                Appreference.isclosed = true;
//                                Log.i("Task1", "Appreference.isclosed " + Appreference.isclosed);
//                                break outerloop;
//                            } else {
                            if (name.contains("Completed Percentage ")) {
                                Log.i("videocalldatabase", "DBpercentage task completed percentage ---> " + name);
                                name = cur.getString(0);
                                if (name != null && !name.equalsIgnoreCase("")) {
                                    Log.i("videocalldatabase", "DBpercentage percentage ----> " + name);
                                    s = Integer.parseInt(name);
                                    if (s > 0) {
                                        p = Integer.parseInt(name);
                                    }
                                }
                            }
//                            }
                            cur.moveToNext();
                        }

                        Log.i("videocalldatabase", "DBpercentage total mem " + i);
                        total = total + p;
                        p = 0;
                        Log.i("videocalldatabase", "DBpercentage total " + total);
                        cur.close();
                        cur.close();
                    } else {
                        cur = db.rawQuery("select completedPercentage,taskDescription from taskDetailsInfo where fromUserName = '" + name + "' and taskId='" + taskid + "' ", null);
                    }
                    cur1.moveToNext();
                }
                cur1.close();
                try {
                    if (i != 0) {
                        Log.i("videocalldatabase", "DBpercentage total " + total / i);
                        p = total / i;
                        Log.i("videocalldatabase", "DBpercentage final " + p);
                    }
                } catch (ArithmeticException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return p;
        }
    }

    @SuppressWarnings("finally")
    public int ProjectGroupPercentageChecker(ArrayList<String> Tousernames, String taskid, String ownerOfTask) {
        int p = 0;
        int s = 0;
        int total = 0, i = 0;
        Cursor cur, cur1;
        String name = null;
        Log.i("videocalldatabase", "ProjectGroupMembers-> " + Tousernames);
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();

               /* cur1 = db.rawQuery("select username from groupmember where groupid = '" + username + "'", null);
                cur1.moveToFirst();
//                outerloop:
                while (!cur1.isAfterLast()) {*/
                for (int j = 0; j < Tousernames.size(); j++) {
                    name = Tousernames.get(j);
                    if (!name.equalsIgnoreCase(ownerOfTask)) {
                        i = i + 1;
                        cur = db.rawQuery("select completedPercentage,taskDescription from taskDetailsInfo where fromUserName = '" + name + "' and taskId='" + taskid + "' ", null);
                        cur.moveToFirst();
                        while (!cur.isAfterLast()) {
                            name = cur.getString(1);
                            Log.i("videocalldatabase", "taskDescription--> " + cur.getString(1));
//                            if (name.equalsIgnoreCase("This task is closed")) {
//                                Appreference.isclosed = true;
//                                Log.i("Task1", "Appreference.isclosed " + Appreference.isclosed);
//                                break outerloop;
//                            } else {
                            if (name.contains("Completed Percentage ")) {
                                Log.e("videocalldatabase", "CompletedPercentage---> " + name);
                                name = cur.getString(0);
                                if (name != null && !name.equalsIgnoreCase("")) {
                                    Log.e("videocalldatabase", "Percentage----> " + name);
                                    s = Integer.parseInt(name);
                                    if (s > 0) {
                                        p = Integer.parseInt(name);
                                    }
                                }

                            }
//                            }
                            cur.moveToNext();
                        }

                        Log.i("videocalldatabase", "ProjectGroup total mem " + i);
                        total = total + p;
                        p = 0;
                        Log.i("videocalldatabase", "ProjectGroup total " + total);
                        cur.close();
                    } else {
                        cur = db.rawQuery("select completedPercentage,taskDescription from taskDetailsInfo where fromUserName = '" + name + "' and taskId='" + taskid + "' ", null);
                    }
//                    cur1.moveToNext();
//                }
                }
//                cur1.close();
                Log.i("videocalldatabase", "ProjectGroup total/mem's " + total / i);
                p = total / i;
                Log.i("videocalldatabase", "ProjectGroup final percentage " + p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return p;
        }
    }

    @SuppressWarnings("finally")
    public int groupPercentageStatus(String username, String taskid) {
        int p = 0;
        int s = 0;
        Cursor cur;
        String name = null;
        Log.i("Task1", "DBpercentage " + username);
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();

                cur = db.rawQuery("select completedPercentage,taskDescription from taskDetailsInfo where fromUserName = '" + username + "' and taskId='" + taskid + "' ", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    name = cur.getString(1);
                    Log.i("Task1", "DBDescription " + cur.getString(1));
                    if (name.contains("Completed Percentage ")) {
                        Log.i("Task1", "DBpercentage " + name);
                        name = cur.getString(0);
                        if (name != null && !name.equalsIgnoreCase("") && !name.equalsIgnoreCase(null)) {
                            Log.i("Task1", "groupPercentage last --> " + name);
                            s = Integer.parseInt(name);
                            if (s > 0) {
                                p = Integer.parseInt(name);
                            }
                        }
                    }
                    cur.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return p;
        }
    }


    @SuppressWarnings("finally")
    public ArrayList<ContactBean> getGroupContact(String username, String groupname) {
        ArrayList<ContactBean> beans = new ArrayList<>();
        ArrayList<String> groupmembers = new ArrayList<>();
        ArrayList<String> contactdetails = new ArrayList<>();
        Cursor cur, cur1;
        boolean checker = true;
        if (db == null)
            db = getReadableDatabase();
        try {

            if (db != null) {
                if (!db.isOpen())
                    openDatabase();

                cur1 = db.rawQuery("select username from groupmember where groupid = '" + groupname + "'", null);
                cur1.moveToFirst();
                while (!cur1.isAfterLast()) {
                    groupmembers.add(cur1.getString(0));
                    cur1.moveToNext();
                }
                cur1.close();

                cur = db.rawQuery("select * from contact where loginuser='" + username + "'", null);
                cur.moveToFirst();
                Log.i("Username", "member list " + groupmembers.size());
                Log.i("Username", "Contact list ");
                while (!cur.isAfterLast()) {
                    contactdetails.add(cur.getString(cur.getColumnIndex("username")));
                    Log.i("Username", "contact --> " + contactdetails.toString());
                    Log.i("Username", "value--> " + cur.getString(cur.getColumnIndex("username")));
                    for (int i = 0; i < groupmembers.size(); i++) {
                        if (groupmembers.get(i).equalsIgnoreCase(cur.getString(cur.getColumnIndex("username")))) {
                            checker = false;
                        }
                    }
                    if (checker == true) {
                        ContactBean bean = new ContactBean();
                        bean.setEmail(cur.getString(cur.getColumnIndex("contactemail")));
                        bean.setUserid(cur.getInt(cur.getColumnIndex("userid")));
                        bean.setUsername(cur.getString(cur.getColumnIndex("username")));
                        bean.setFirstname(cur.getString(cur.getColumnIndex("firstname")));
                        bean.setLastname(cur.getString(cur.getColumnIndex("lastname")));
                        bean.setCode(cur.getString(cur.getColumnIndex("code")));
                        bean.setTitle(cur.getString(cur.getColumnIndex("title")));
                        bean.setGender(cur.getString(cur.getColumnIndex("gender")));
                        bean.setProfileImage(cur.getString(cur.getColumnIndex("profileImage")));
                        bean.setPersonalInfo(cur.getString(cur.getColumnIndex("personalInfo")));
                        bean.setLoginuser(cur.getString(cur.getColumnIndex("loginuser")));
                        bean.setStatus(cur.getString(cur.getColumnIndex("presence")));
                        beans.add(bean);
                    }
                    checker = true;
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            return beans;
        }
    }


    @SuppressWarnings("finally")
    public String getOverdue(String username) {
        String name = null;
        Log.i("task", "string value " + username);
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select taskDescription,taskStatus from taskDetailsInfo where taskId='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    if (cur.getString(1) != null && (cur.getString(1).equalsIgnoreCase("closed") || cur.getString(1).equalsIgnoreCase("Completed"))) {
                        name = cur.getString(1);
                        Log.i("task", "taskStatus 1 " + name);
//                        break;
                    } else if (cur.getString(0) != null && cur.getString(0).contains("This task is overdue")) {
                        if (cur.getString(1) != null && cur.getString(1).equalsIgnoreCase("assigned")) {
                            name = "overdue";
                        } else {
                            if (cur.getString(1) != null)
                                name = cur.getString(1);
                        }
                        Log.i("task", "taskStatus 2--> " + name);
                    } else if (cur.getString(0) != null && cur.getString(0).equalsIgnoreCase("Task accepted")) {
                        if (cur.getString(1) != null)
                            name = cur.getString(1);
                        Log.i("task**", "taskStatus 3---> " + name);
                    } else if (cur.getString(0) != null && cur.getString(0).equalsIgnoreCase("Task Rejected")) {
                        if (cur.getString(1) != null)
                            name = cur.getString(1);
                        Log.i("task***", "taskStatus 4---> " + name);
                    } else if (cur.getString(1) != null && cur.getString(1).equalsIgnoreCase("assigned")) {
                        name = cur.getString(1);
                        Log.i("task***", "taskStatus 5---> " + name);
                    } else {
                        if (cur.getString(1) != null)
                            name = cur.getString(1);
                        Log.i("task", "taskStatus---> " + name);
                    }
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally

        {
            return name;
        }

    }

    @SuppressWarnings("finally")
    public String Statuscheker(String username) {
        String name = null;
        Log.i("task", "string value" + username);
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select taskStatus from taskDetailsInfo where taskId='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    Log.i("task", "status received in db " + cur.getString(0));
                    name = cur.getString(0);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return name;
        }
    }

    @SuppressWarnings("finally")
    public boolean DuplicateChecker(String namelist, String taskId) {
        boolean list = false;
        Cursor cur;
        String name = null;
        Log.i("TaskTable", "DBpercentage" + namelist);
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();

                cur = db.rawQuery("select signalid from taskDetailsInfo where signalid='" + namelist.trim() + "' and taskId='" + taskId + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {

                    Log.i("TaskTable", "DBDescription signalid " + cur.getString(0));
                    list = true;
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    @SuppressWarnings("finally")
    public boolean giverTaskCompletion(String taskId, String owneroftask) {
        boolean list = false;
        Cursor cur;
        String name = null;
        Log.i("TaskTable", "giverTaskCompletion " + taskId);
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();

                cur = db.rawQuery("select taskDescription from taskDetailsInfo where taskId='" + taskId + "' and fromUserName='" + Appreference.loginuserdetails.getUsername() + "' and taskDescription like '" + "Completed Percentage %" + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {

                    Log.i("TaskTable", "giverTaskCompletion " + cur.getString(0));
                    list = true;
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    @SuppressWarnings("finally")
    public boolean DuplicateTaskIdChecker(String query) {
        boolean list = false;
        Cursor cur;
        String name = null;
        Log.i("TaskTable", "DBpercentage" + query);
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();

                cur = db.rawQuery(query, null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {

                    Log.i("TaskTable", "DBDescription" + cur.getString(0));
                    list = true;
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    @SuppressWarnings("finally")
    public boolean DuplicateProjectIdChecker(String projectid) {
        boolean list = false;
        Cursor cur;
        Log.i("ProjectDetailsTable", "DB projectid " + projectid);
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();

                cur = db.rawQuery("select projectId from projectDetails where projectId='" + projectid + "'  ", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {

                    Log.i("ProjectTable", "DBDescription" + cur.getString(0));
                    list = true;
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    @SuppressWarnings("finally")
    public boolean DuplicateProjectTaskIdChecker(String taskId) {
        boolean list = false;
        Cursor cur;
        Log.i("ProjectHistoryTable", "DB taskId " + taskId);
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();

                cur = db.rawQuery("select taskId from projectHistory where taskId='" + taskId + "'  ", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {

                    Log.i("ProjectTable", "DBDescription" + cur.getString(0));
                    list = true;
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    public boolean IstaskIdExist(String taskId, String project_Id) {
        boolean list = false;
        Cursor cur;
        Log.i("ProjectHistoryTable", "DB taskId " + taskId);
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();

                cur = db.rawQuery("select taskId from projectStatus where taskId='" + taskId + "' and projectId='" + project_Id + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {

                    Log.i("ProjectTable", "DBDescription" + cur.getString(0));
                    list = true;
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    @SuppressWarnings("finally")
    public boolean DuplicatetaskTaskIdChecker(String taskId) {
        boolean list = false;
        Cursor cur;
        Log.i("taskDetailsInfoTable", "DB taskId " + taskId);
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();

                cur = db.rawQuery("select taskId from taskDetailsInfo where taskId='" + taskId + "'  ", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {

                    Log.i("taskDetailsInfoTable", "taskid is " + cur.getString(0));
                    list = true;
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    @SuppressWarnings("finally")
    public boolean getAccept(String namelist) {
        boolean list = false;
        Cursor cur;
        String name = null;
        Log.i("TaskTable", "DBpercentage" + namelist);
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();

                cur = db.rawQuery("select taskDescription from taskDetailsInfo where taskId='" + namelist + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {

                    Log.i("TaskTable", "DBDescription" + cur.getString(0));
                    if (cur.getString(0) != null && (cur.getString(0).contains("Task Rejected") || cur.getString(0).contains("Task accepted"))) {
                        list = true;
                    }
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    @SuppressWarnings("finally")
    public String GroupIdCheck(String username) {
        String name = null;
        Log.i("task", "string value==>" + username);
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select groupid from group1 where groupname='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    Log.i("task", "string value" + cur.getString(0));
                    name = cur.getString(0);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return name;
        }
    }


    @SuppressWarnings("finally")
    public boolean getOverdueMsg(String task_id) {
        boolean list = false;
        Cursor cur;
        String name = null;
        Log.i("TaskTable", "overdue" + task_id);
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();

                cur = db.rawQuery("select taskStatus from taskDetailsInfo where taskId='" + task_id + "'  ", null);
                Log.i("taskMessage", "overdue description " + cur);
                if (cur != null) {
                    cur.moveToFirst();
                    while (!cur.isAfterLast()) {

                        Log.i("taskMessage", "overdue description ->" + cur.getString(0));
                        if (cur.getString(0) != null && cur.getString(0).equalsIgnoreCase("closed") || cur.getString(0).equalsIgnoreCase("completed") || cur.getString(0).equalsIgnoreCase("overdue")) {
                            Log.i("TaskTable", "boolean true for overdue taskid " + task_id);
                            Log.i("TaskTable", "boolean true for overdue taskid " + cur.getString(0));
                            list = true;
//                        break;
                        } else {
                            Log.i("TaskTable", "boolean true for overdue taskid " + task_id);
                            Log.i("TaskTable", "boolean true for overdue taskid " + cur.getString(0));
                            list = false;
                        }
                        cur.moveToNext();
                    }
                    cur.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }

    @SuppressWarnings("finally")
    public TaskDetailsBean getTaskContent(String query) {
        TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
        Log.i("task", "query is " + query);
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(query, null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    taskDetailsBean.setTaskId(cur.getString(cur.getColumnIndex("taskId")));
                    taskDetailsBean.setFromUserId(cur.getString(cur.getColumnIndex("fromUserId")));
                    taskDetailsBean.setToUserId(cur.getString(cur.getColumnIndex("toUserId")));
                    taskDetailsBean.setTaskNo(cur.getString(cur.getColumnIndex("taskNo")));
//                    taskDetailsBean.setTaskName(cur.getString(cur.getColumnIndex("taskName")));
                    taskDetailsBean.setPlannedStartDateTime(cur.getString(cur.getColumnIndex("plannedStartDateTime")));
                    taskDetailsBean.setPlannedEndDateTime(cur.getString(cur.getColumnIndex("plannedEndDateTime")));
                    taskDetailsBean.setRemainderFrequency(cur.getString(cur.getColumnIndex("remainderFrequency")));
                    taskDetailsBean.setDuration(cur.getString(cur.getColumnIndex("duration")));
                    taskDetailsBean.setDurationUnit(cur.getString(cur.getColumnIndex("durationunit")));
                    taskDetailsBean.setTaskDescription(cur.getString(cur.getColumnIndex("taskDescription")));
//                    taskDetailsBean.setTaskObservers(cur.getString(cur.getColumnIndex("duration")));
                    taskDetailsBean.setIsRemainderRequired(cur.getString(cur.getColumnIndex("isRemainderRequired")));
                    taskDetailsBean.setTaskStatus(cur.getString(cur.getColumnIndex("taskStatus")));
                    taskDetailsBean.setSignalid(cur.getString(cur.getColumnIndex("signalid")));
                    taskDetailsBean.setFromUserName(cur.getString(cur.getColumnIndex("fromUserName")));
                    taskDetailsBean.setToUserName(cur.getString(cur.getColumnIndex("toUserName")));
                    taskDetailsBean.setSendStatus(cur.getString(cur.getColumnIndex("sendStatus")));
                    taskDetailsBean.setCompletedPercentage(cur.getString(cur.getColumnIndex("completedPercentage")));
                    taskDetailsBean.setTaskType(cur.getString(cur.getColumnIndex("taskType")));
//                    taskDetailsBean.setOwnerOfTask(cur.getString(cur.getColumnIndex("ownerOfTask")));
                    taskDetailsBean.setMimeType(cur.getString(cur.getColumnIndex("mimeType")));
                    taskDetailsBean.setTaskPriority(cur.getString(cur.getColumnIndex("taskPriority")));
                    taskDetailsBean.setDateFrequency(cur.getString(cur.getColumnIndex("dateFrequency")));
                    taskDetailsBean.setTimeFrequency(cur.getString(cur.getColumnIndex("timeFrequency")));
                    taskDetailsBean.setTaskId(cur.getString(cur.getColumnIndex("taskId")));
                    taskDetailsBean.setMsg_status(Integer.parseInt(cur.getString(cur.getColumnIndex("msgstatus"))));
                    taskDetailsBean.setShow_progress(Integer.parseInt(cur.getString(cur.getColumnIndex("showprogress"))));
                    taskDetailsBean.setRead_status(Integer.parseInt(cur.getString(cur.getColumnIndex("readStatus"))));
                    taskDetailsBean.setReminderQuote(cur.getString(cur.getColumnIndex("reminderquotes")));
                    taskDetailsBean.setRemark(cur.getString(cur.getColumnIndex("remark")));
                    taskDetailsBean.setTasktime(cur.getString(cur.getColumnIndex("tasktime")));
//                    taskDetailsBean.setTaskReceiver(cur.getString(cur.getColumnIndex("duration")));
                    taskDetailsBean.setServerFileName(cur.getString(cur.getColumnIndex("serverFileName")));
                    taskDetailsBean.setRequestStatus(cur.getString(cur.getColumnIndex("requestStatus")));
//                    taskDetailsBean.setGroupTaskMembers(cur.getString(cur.getColumnIndex("duration")));
                    taskDetailsBean.setDateTime(cur.getString(cur.getColumnIndex("dateTime")));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return taskDetailsBean;
        }
    }

    @SuppressWarnings("finally")
    public ProjectDetailsBean getProjectContent(String query) {
        ProjectDetailsBean taskDetailsBean = new ProjectDetailsBean();
        Log.i("task", "query is " + query);
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(query, null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {

                    taskDetailsBean.setId(cur.getString(cur.getColumnIndex("projectId")));
                    taskDetailsBean.setProjectName(cur.getString(cur.getColumnIndex("projectName")));
                    taskDetailsBean.setDescription(cur.getString(cur.getColumnIndex("projectDescription")));
//                    taskDetailsBean.setOrganisation(cur.getString(cur.getColumnIndex("projectOrganisation")));
                    taskDetailsBean.setParentTaskId(cur.getString(cur.getColumnIndex("parentTaskId")));
                    if (cur.getString(cur.getColumnIndex("readStatus")) != null && !cur.getString(cur.getColumnIndex("readStatus")).equalsIgnoreCase(null) && !cur.getString(cur.getColumnIndex("readStatus")).equalsIgnoreCase("") && !cur.getString(cur.getColumnIndex("readStatus")).equalsIgnoreCase("null") && !cur.getString(cur.getColumnIndex("readStatus")).equalsIgnoreCase("(null)")) {
                        taskDetailsBean.setRead_status(Integer.valueOf(cur.getString(cur.getColumnIndex("readStatus"))));
                    }
                    taskDetailsBean.setProject_ownerName(cur.getString(cur.getColumnIndex("projectOwner")));
                    taskDetailsBean.setParentTaskId(cur.getString(cur.getColumnIndex("parentTaskId")));
                    taskDetailsBean.setFromUserId(cur.getString(cur.getColumnIndex("fromUserId")));
                    taskDetailsBean.setToUserId(cur.getString(cur.getColumnIndex("toUserId")));
                    taskDetailsBean.setFromUserName(cur.getString(cur.getColumnIndex("fromUserName")));
                    taskDetailsBean.setToUserName(cur.getString(cur.getColumnIndex("toUserName")));
                    taskDetailsBean.setTaskStatus(cur.getString(cur.getColumnIndex("taskStatus")));
                    taskDetailsBean.setCompletedPercentage(cur.getString(cur.getColumnIndex("completedPercentage")));
                    taskDetailsBean.setOwnerOfTask(cur.getString(cur.getColumnIndex("ownerOfTask")));
                    taskDetailsBean.setTaskReceiver(cur.getString(cur.getColumnIndex("taskReceiver")));
                    taskDetailsBean.setTaskObservers(cur.getString(cur.getColumnIndex("taskObservers")));
                    taskDetailsBean.setTaskMemberList(cur.getString(cur.getColumnIndex("taskMemberList")));
                    taskDetailsBean.setPlannedStartDateTime(cur.getString(cur.getColumnIndex("plannedStartDateTime")));
                    taskDetailsBean.setPlannedEndDateTime(cur.getString(cur.getColumnIndex("plannedEndDateTime")));
//                    taskDetailsBean.setListMemberProject(cur.getString(cur.getColumnIndex("listOfMembers")));
                    taskDetailsBean.setTaskNo(cur.getString(cur.getColumnIndex("taskNo")));
                    taskDetailsBean.setTaskName(cur.getString(cur.getColumnIndex("taskName")));
                    taskDetailsBean.setTaskDescription(cur.getString(cur.getColumnIndex("taskDescription")));
                    taskDetailsBean.setTaskType(cur.getString(cur.getColumnIndex("taskType")));
                    taskDetailsBean.setMimeType(cur.getString(cur.getColumnIndex("mimeType")));
                    taskDetailsBean.setTaskId(cur.getString(cur.getColumnIndex("taskId")));
                    taskDetailsBean.setCatagory(cur.getString(cur.getColumnIndex("category")));
                    taskDetailsBean.setIsParentTask(cur.getString(cur.getColumnIndex("isParentTask")));
                    taskDetailsBean.setRequestStatus(cur.getString(cur.getColumnIndex("requestStatus")));
                    taskDetailsBean.setOracleProjectId(cur.getString(cur.getColumnIndex("oracleProjectId")));
                    taskDetailsBean.setCustomerName(cur.getString(cur.getColumnIndex("customerName")));
                    taskDetailsBean.setAddress(cur.getString(cur.getColumnIndex("address")));
                    taskDetailsBean.setMcModel(cur.getString(cur.getColumnIndex("mcModel")));
                    taskDetailsBean.setMcSrNo(cur.getString(cur.getColumnIndex("mcSrNo")));
                    taskDetailsBean.setServiceRequestDate(cur.getString(cur.getColumnIndex("serviceRequestDate")));
                    taskDetailsBean.setChasisNo(cur.getString(cur.getColumnIndex("chasisNo")));
                    taskDetailsBean.setObservation(cur.getString(cur.getColumnIndex("observation")));
                    taskDetailsBean.setOracleCustomerId(cur.getInt(cur.getColumnIndex("oracleCustomerId")));
                    taskDetailsBean.setActivity(cur.getString(cur.getColumnIndex("activity")));
                    taskDetailsBean.setProcessFlag(cur.getString(cur.getColumnIndex("processFlag")));
                    taskDetailsBean.setProjectCompletedStatus(cur.getString(cur.getColumnIndex("projectcompletedstatus")));
                    taskDetailsBean.setIsActiveStatus(cur.getString(cur.getColumnIndex("isActiveStatus")));
                    taskDetailsBean.setJobCardType(cur.getString(cur.getColumnIndex("jobCardType")));
                    taskDetailsBean.setMachineMake(cur.getString(cur.getColumnIndex("machineMake")));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return taskDetailsBean;
        }
    }


    public ListMember getnewAddedList(String username) {
        ListMember listMember = new ListMember();
        Log.i("task", "string value " + username);
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from contact where username='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    listMember.setEmail(cur.getString(cur.getColumnIndex("contactemail")));
                    listMember.setUserid(cur.getInt(cur.getColumnIndex("userid")));
                    listMember.setUsername(cur.getString(cur.getColumnIndex("username")));
                    listMember.setFirstName(cur.getString(cur.getColumnIndex("firstname")));
                    listMember.setLastName(cur.getString(cur.getColumnIndex("lastname")));
                    listMember.setCode(cur.getString(cur.getColumnIndex("code")));
                    listMember.setTitle(cur.getString(cur.getColumnIndex("title")));
                    listMember.setGender(cur.getString(cur.getColumnIndex("gender")));
                    listMember.setProfileImage(cur.getString(cur.getColumnIndex("profileImage")));
                    listMember.setPersonalInfo(cur.getString(cur.getColumnIndex("personalInfo")));
                    listMember.setLoginuser(cur.getString(cur.getColumnIndex("loginuser")));
                    listMember.setUserStatus(cur.getString(cur.getColumnIndex("presence")));
                    listMember.setJobCategory1(cur.getString(cur.getColumnIndex("job1")));
                    listMember.setJobCategory2(cur.getString(cur.getColumnIndex("job2")));
                    listMember.setJobCategory3(cur.getString(cur.getColumnIndex("job3")));
                    listMember.setJobCategory4(cur.getString(cur.getColumnIndex("job4")));
                    listMember.setTextProfile(cur.getString(cur.getColumnIndex("textprofile")));
                    listMember.setVideoProfile(cur.getString(cur.getColumnIndex("videoprofile")));
                    listMember.setUserType(cur.getString(cur.getColumnIndex("userType")));
                    listMember.setProfession(cur.getString(cur.getColumnIndex("profession")));
                    listMember.setSpecialization(cur.getString(cur.getColumnIndex("specialization")));
                    listMember.setOrganization(cur.getString(cur.getColumnIndex("organization")));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return listMember;
        }
    }

    @SuppressWarnings("finally")
    public TaskDetailsBean getFormContent(String query) {
        TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
        Log.i("task", "query taskhistoryInfo is " + query);
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(query, null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    taskDetailsBean.setTaskNo(cur.getString(cur.getColumnIndex("taskNo")));
                    taskDetailsBean.setTaskName(cur.getString(cur.getColumnIndex("taskName")));
                    taskDetailsBean.setTaskDescription(cur.getString(cur.getColumnIndex("taskDescription")));
                    taskDetailsBean.setTaskObservers(cur.getString(cur.getColumnIndex("taskObservers")));
                    taskDetailsBean.setTaskStatus(cur.getString(cur.getColumnIndex("taskStatus")));
                    taskDetailsBean.setSignalid(cur.getString(cur.getColumnIndex("signalid")));
                    taskDetailsBean.setCompletedPercentage(cur.getString(cur.getColumnIndex("completedPercentage")));
                    taskDetailsBean.setTaskType(cur.getString(cur.getColumnIndex("taskType")));
                    taskDetailsBean.setOwnerOfTask(cur.getString(cur.getColumnIndex("ownerOfTask")));
                    taskDetailsBean.setMimeType(cur.getString(cur.getColumnIndex("mimeType")));
                    taskDetailsBean.setTaskId(cur.getString(cur.getColumnIndex("taskId")));
                    taskDetailsBean.setTaskReceiver(cur.getString(cur.getColumnIndex("taskReceiver")));
                    taskDetailsBean.setGroupTaskMembers(cur.getString(cur.getColumnIndex("taskMemberList")));
                    taskDetailsBean.setDateTime(cur.getString(cur.getColumnIndex("dateTime")));
                    taskDetailsBean.setCatagory(cur.getString(cur.getColumnIndex("category")));

                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return taskDetailsBean;
        }
    }

    public String getAccessType(String taskid, String formid, String membername) {
        Cursor cur;
        String name = null;
        Log.i("VideoCallDataBase", "taskid" + taskid);
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();

                cur = db.rawQuery("select accessMode from FormAccess where taskId='" + taskid + "' and formId = '" + formid + "' and memberName = '" + membername + "' ", null);

                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    Log.i("VideoCallDataBase", "AccessType " + cur.getString(0));
                    name = cur.getString(0);
                    cur.moveToNext();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    @SuppressWarnings("finally")
    public boolean getDatecheck(String task_id, String customtag, String datevalue) {
        boolean list = false;
        Cursor cur;
        String name = null;
        Log.i("TaskTable", "overdue" + task_id);
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();

                cur = db.rawQuery("select taskDescription from taskDetailsInfo where taskId='" + task_id + "' and customTagId = '" + customtag + "' ", null);
                Log.i("taskMessage", "overdue description " + cur);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {

                    Log.i("taskMessage", "overdue description ->" + cur.getString(0));
                    Log.i("taskMessage", "overdue description ->" + datevalue);
                    if (cur.getString(0).equalsIgnoreCase(datevalue)) {
                        Log.i("taskMessage", "boolean true for overdue taskid " + task_id);
                        Log.i("taskMessage", "boolean true for overdue taskid " + cur.getString(0));
                        list = true;
                        break;
                    }
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return list;
        }
    }


    @SuppressWarnings("finally")
    public ArrayList<TaskDetailsBean> getSchuduleDetalis(String username, String tagid) {
        ArrayList<TaskDetailsBean> taskDetailsBeanArrayList = new ArrayList<TaskDetailsBean>();
        Log.i("task", "string value" + username);
        Cursor cur, cur1;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from taskDetailsInfo where taskId='" + username + "' and customSetId = '" + tagid + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    Log.i("task", "string value 1" + cur.getString(cur.getColumnIndex("taskTagName")));
                    Log.i("task", "string value 1" + cur.getString(cur.getColumnIndex("taskDescription")));
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                    taskDetailsBean.setTaskId(cur.getString(cur.getColumnIndex("taskId")));
                    taskDetailsBean.setFromUserId(cur.getString(cur.getColumnIndex("fromUserId")));
                    taskDetailsBean.setToUserId(cur.getString(cur.getColumnIndex("toUserId")));
                    taskDetailsBean.setTaskNo(cur.getString(cur.getColumnIndex("taskNo")));
//                    taskDetailsBean.setTaskName(cur.getString(5));
                    taskDetailsBean.setPlannedStartDateTime(cur.getString(cur.getColumnIndex("plannedStartDateTime")));
                    taskDetailsBean.setPlannedEndDateTime(cur.getString(cur.getColumnIndex("plannedEndDateTime")));
                    taskDetailsBean.setRemainderFrequency(cur.getString(cur.getColumnIndex("remainderFrequency")));
                    taskDetailsBean.setDuration(cur.getString(cur.getColumnIndex("duration")));
                    taskDetailsBean.setDurationUnit(cur.getString(cur.getColumnIndex("durationunit")));
                    taskDetailsBean.setTaskDescription(cur.getString(cur.getColumnIndex("taskDescription")));
//                    taskDetailsBean.setTaskObservers(cur.getString(12));
                    taskDetailsBean.setIsRemainderRequired(cur.getString(cur.getColumnIndex("isRemainderRequired")));
                    taskDetailsBean.setTaskStatus(cur.getString(cur.getColumnIndex("taskStatus")));
                    taskDetailsBean.setSignalid(cur.getString(cur.getColumnIndex("signalid")));
                    taskDetailsBean.setFromUserName(cur.getString(cur.getColumnIndex("fromUserName")));
                    taskDetailsBean.setToUserName(cur.getString(cur.getColumnIndex("toUserName")));
                    taskDetailsBean.setSendStatus(cur.getString(cur.getColumnIndex("sendStatus")));
                    taskDetailsBean.setCompletedPercentage(cur.getString(cur.getColumnIndex("completedPercentage")));
                    taskDetailsBean.setTaskType(cur.getString(cur.getColumnIndex("taskType")));
//                    taskDetailsBean.setOwnerOfTask(cur.getString(21));
                    taskDetailsBean.setMimeType(cur.getString(cur.getColumnIndex("mimeType")));
                    taskDetailsBean.setTaskPriority(cur.getString(cur.getColumnIndex("taskPriority")));
                    taskDetailsBean.setDateFrequency(cur.getString(cur.getColumnIndex("dateFrequency")));
                    taskDetailsBean.setTimeFrequency(cur.getString(cur.getColumnIndex("timeFrequency")));

                    taskDetailsBean.setMsg_status(Integer.parseInt(cur.getString(cur.getColumnIndex("msgstatus"))));
                    taskDetailsBean.setShow_progress(Integer.parseInt(cur.getString(cur.getColumnIndex("showprogress"))));
                    taskDetailsBean.setRead_status(Integer.parseInt(cur.getString(cur.getColumnIndex("readStatus"))));
                    taskDetailsBean.setReminderQuote(cur.getString(cur.getColumnIndex("reminderquotes")));
                    taskDetailsBean.setRemark(cur.getString(cur.getColumnIndex("remark")));
                    taskDetailsBean.setTasktime(cur.getString(cur.getColumnIndex("tasktime")));
//                    taskDetailsBean.setTaskReceiver(cur.getString(33));
                    taskDetailsBean.setServerFileName(cur.getString(cur.getColumnIndex("serverFileName")));
                    taskDetailsBean.setRequestStatus(cur.getString(cur.getColumnIndex("requestStatus")));
//                    taskDetailsBean.setGroupTaskMembers(cur.getString(36));
                    taskDetailsBean.setDateTime(cur.getString(cur.getColumnIndex("dateTime")));
//                    taskDetailsBean.setTaskTagName(cur.getString(37));
//                    taskDetailsBean.setCustomTagId();
//                    taskDetailsBean.setCustomSetId();
                    taskDetailsBean.setTaskTagName(cur.getString(cur.getColumnIndex("taskTagName")));
                    taskDetailsBean.setCustomTagId((cur.getInt(cur.getColumnIndex("customTagId"))));
                    taskDetailsBean.setCustomSetId((cur.getInt(cur.getColumnIndex("customSetId"))));
                    taskDetailsBeanArrayList.add(taskDetailsBean);
                    cur.moveToNext();

                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return taskDetailsBeanArrayList;
        }
    }


    public void UpdateOrInsertFormAccess(FormAccessBean bean) {
        int row_id = 0;
        String logginuser = AppSharedpreferences.getInstance(context).getString("loginUserName");

        Log.i("DB_Insert", "logginuser" + logginuser);

        if (!db.isOpen())
            openDatabase();
        ContentValues cv = new ContentValues();
        cv.put("taskId", bean.getTaskId());
        cv.put("formId", bean.getFormId());
        cv.put("formAccessId", bean.getFromAccessId());
        cv.put("taskGiver", bean.getGiver());
        if (bean.getType() != null)
            cv.put("memberName", bean.getType());
        else
            cv.put("memberName", bean.getMemberName());
        cv.put("accessMode", bean.getAccessMode());
        Log.i("Accept", "value DB Formaccessbean.getTaskType() " + bean.getType());
        Log.i("Database", "Updatequrey" + "select * from FormAccess where memberName='" + bean.getMemberName() + "'and formId='" + bean.getFormId() + "'");
        if (isAgendaRecordExists("select * from FormAccess where memberName='" + bean.getMemberName() + "'and formId='" + bean.getFormId() + "'")) {
            Log.i("ContactTable", "UpdateQuery");
//                row_id = (int) db.update("taskDetailsInfo", cv, "customSetId='" + name + "'and customTagId='" + beans.get(i).getId() + "'", null);
            try {
                ContentValues cv1 = new ContentValues();
                cv1.put("accessMode", bean.getAccessMode());
                db.update("FormAccess", cv1, "memberName=? and formId=?", new String[]{String.valueOf(bean.getMemberName()), String.valueOf(bean.getFormId())});
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.i("ContactTable", "Insert");
            row_id = (int) db.insert("FormAccess", null, cv);
        }
    }

    @SuppressWarnings("finally")
    public TaskDetailsBean getTaskcallContent(String query) {
        TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
        Log.i("task", "string value" + query);
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(query, null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    taskDetailsBean.setTaskName(cur.getString(cur.getColumnIndex("taskName")));
                    taskDetailsBean.setOwnerOfTask(cur.getString(cur.getColumnIndex("ownerOfTask")));
                    taskDetailsBean.setTaskReceiver(cur.getString(cur.getColumnIndex("taskReceiver")));
                    taskDetailsBean.setCatagory(cur.getString(cur.getColumnIndex("category")));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return taskDetailsBean;
        }
    }

    public GroupMemberAccess getMemberAccessList(String groupid) {
        GroupMemberAccess groupMemberAccess = new GroupMemberAccess();
        Log.i("groupMemberAccess", "string value " + groupid);
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select * from listUserGroupMemberAccess where groupid='" + groupid + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    groupMemberAccess.setAccessForms(cur.getString(cur.getColumnIndex("accessForms")));
                    groupMemberAccess.setAccessReminder(cur.getString(cur.getColumnIndex("accessReminder")));
                    groupMemberAccess.setAccessScheduledCNF(cur.getString(cur.getColumnIndex("accessScheduledCNF")));
                    groupMemberAccess.setAdminAccess(cur.getString(cur.getColumnIndex("adminAccess")));
                    groupMemberAccess.setAudioAccess(cur.getString(cur.getColumnIndex("audioAccess")));
                    groupMemberAccess.setChatAccess(cur.getString(cur.getColumnIndex("chatAccess")));
                    groupMemberAccess.setRespondAudio(cur.getString(cur.getColumnIndex("respondAudio")));
                    groupMemberAccess.setRespondConfCall(cur.getString(cur.getColumnIndex("respondConfCall")));
                    groupMemberAccess.setRespondDateChange(cur.getString(cur.getColumnIndex("respondDateChange")));
                    groupMemberAccess.setRespondFiles(cur.getString(cur.getColumnIndex("respondFiles")));
                    groupMemberAccess.setRespondLocation(cur.getString(cur.getColumnIndex("respondLocation")));
                    groupMemberAccess.setRespondPhoto(cur.getString(cur.getColumnIndex("respondPhoto")));
                    groupMemberAccess.setRespondPrivate(cur.getString(cur.getColumnIndex("respondPrivate")));
                    groupMemberAccess.setRespondSketch(cur.getString(cur.getColumnIndex("respondSketch")));
                    groupMemberAccess.setRespondText(cur.getString(cur.getColumnIndex("respondText")));
                    groupMemberAccess.setRespondTask(cur.getString(cur.getColumnIndex("respondTask")));
                    groupMemberAccess.setVideoAccess(cur.getString(cur.getColumnIndex("videoAccess")));
                    groupMemberAccess.setRespondVideo(cur.getString(cur.getColumnIndex("respondVideo")));
                    groupMemberAccess.setGroup_Task(cur.getString(cur.getColumnIndex("GroupTask")));
                    groupMemberAccess.setReassignTask(cur.getString(cur.getColumnIndex("ReassignTask")));
                    groupMemberAccess.setChangeTaskName(cur.getString(cur.getColumnIndex("ChangeTaskName")));
                    groupMemberAccess.setTaskDescriptions(cur.getString(cur.getColumnIndex("TaskDescriptions")));
                    groupMemberAccess.setTemplateExistingTask(cur.getString(cur.getColumnIndex("TemplateExistingTask")));
                    groupMemberAccess.setApproveLeave(cur.getString(cur.getColumnIndex("ApproveLeave")));
                    groupMemberAccess.setRemindMe(cur.getString(cur.getColumnIndex("RemindMe")));
                    groupMemberAccess.setAddObserver(cur.getString(cur.getColumnIndex("AddObserver")));
                    groupMemberAccess.setTaskPriority(cur.getString(cur.getColumnIndex("TaskPriority")));
                    groupMemberAccess.setEscalations(cur.getString(cur.getColumnIndex("Escalations")));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return groupMemberAccess;
        }
    }

    public String[] getChatHistoryAvailabeUser(String username) {
        String name[] = name = new String[2];
        ;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
//                String Loginuser = Appreference.loginuserdetails.getUsername().replace('_', '@');
                Log.i("report", "Loginuser-->" + Appreference.loginuserdetails.getUsername());
                cur = db.rawQuery("select * from chat where username='" + Appreference.loginuserdetails.getUsername() + "' and chatmembers='" + username + "'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    name[0] = cur.getString(cur.getColumnIndex("chattype"));
                    name[1] = cur.getString(cur.getColumnIndex("chatname"));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return name;
        }
    }


    public int getTaskHistoryRowCount(String query) {
        int count = 0;
        try {
            Cursor cur = null;
            if (!db.isOpen())
                openDatabase();
            cur = db.rawQuery(query, null);
            count = cur.getCount();
            cur.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public String getReportUserName() {
        String name = null;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                String Loginuser = Appreference.loginuserdetails.getUsername().replace('_', '@');
                Log.i("report", "Loginuser-->" + Loginuser);
                cur = db.rawQuery("select * from taskDetailsInfo where loginuser='" + Loginuser + "' and subType='customeHeaderAttribute' and sendStatus='0'", null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    name = cur.getString(17);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return name;

        }
    }

    public ArrayList<TaskDetailsBean> getAllReport(String taskid,String target_user,boolean isGroup) {
        Cursor cur;
        ArrayList<TaskDetailsBean> taskDetailsBean = new ArrayList<TaskDetailsBean>();
        if (db == null)
            db = getReadableDatabase();
        try {
            if (!db.isOpen())
                openDatabase();
            String Loginuser = Appreference.loginuserdetails.getUsername().replace('_', '@');
            String owner = Appreference.loginuserdetails.getUsername();
//            cur = db.rawQuery("select taskNo,taskName,taskDescription,fromUserName,toUserName,taskId,taskTagName from taskDetailsInfo where loginuser='" + Loginuser + "'and ownerOfTask='"+owner+"'and subType='customeHeaderAttribute'", null);
            if(target_user!=null)
            cur = db.rawQuery("select * from taskDetailsInfo where loginuser='" + Loginuser + "'and fromUserName='" + target_user + "'and subType='customeHeaderAttribute' and taskId='" + taskid + "'", null);
            else
                cur = db.rawQuery("select * from taskDetailsInfo where loginuser='" + Loginuser + "'and subType='customeHeaderAttribute' and taskId='" + taskid + "'", null);

            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                TaskDetailsBean taskdetailsbean = new TaskDetailsBean();
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Job No")) {
                    taskdetailsbean.setTaskNo(cur.getString(cur.getColumnIndex("taskDescription")));
                    if (cur.getString(cur.getColumnIndex("toUserName")) != null &&
                            cur.getString(cur.getColumnIndex("toUserName")).equalsIgnoreCase(owner)) {
                        String username = VideoCallDataBase.getDB(context).getName(cur.getString(cur.getColumnIndex("fromUserName")));
                        if (username != null)
                            taskdetailsbean.setToUserName(username);
                        else
                            taskdetailsbean.setToUserName(cur.getString(cur.getColumnIndex("fromUserName")));
                    } else {
                        String username = VideoCallDataBase.getDB(context).getName(cur.getString(cur.getColumnIndex("toUserName")));
                        if (username != null)
                            taskdetailsbean.setToUserName(username);
                        else
                            taskdetailsbean.setToUserName(cur.getString(cur.getColumnIndex("toUserName")));
                    }

                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Address")) {
                    taskdetailsbean.setAddress(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("M/c Model")) {
                    taskdetailsbean.setMcModel(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("M/c Sr.No")) {
                    taskdetailsbean.setMcSrNo(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Service Requested Date")) {
                    taskdetailsbean.setDateTime(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Reported By")) {
                    taskdetailsbean.setReportedBy(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Task Id")) {
                    taskdetailsbean.setTaskId(cur.getString(cur.getColumnIndex("taskDescription")));
                    if (cur.getString(cur.getColumnIndex("toUserName")) != null &&
                            cur.getString(cur.getColumnIndex("toUserName")).equalsIgnoreCase(owner)) {
                        String username = VideoCallDataBase.getDB(context).getName(cur.getString(cur.getColumnIndex("fromUserName")));
                        if (username != null)
                            taskdetailsbean.setToUserName(username);
                        else
                            taskdetailsbean.setToUserName(cur.getString(cur.getColumnIndex("fromUserName")));
                    } else {
                        String username = VideoCallDataBase.getDB(context).getName(cur.getString(cur.getColumnIndex("toUserName")));
                        if (username != null)
                            taskdetailsbean.setToUserName(username);
                        else
                            taskdetailsbean.setToUserName(cur.getString(cur.getColumnIndex("toUserName")));
                    }
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Task Description")) {
                    taskdetailsbean.setTaskDescription(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Estimated Travel Hrs")) {
                    taskdetailsbean.setEstimatedTravel(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Estimated Activity Hrs")) {
                    taskdetailsbean.setEstimatedActivity(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Total Travel Hrs")) {
                    taskdetailsbean.setTotalTravel(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Total Activity Hrs")) {
                    taskdetailsbean.setTotalActivity(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Travel Start Time")
                        && cur.getString(cur.getColumnIndex("customTagId")) != null && cur.getString(cur.getColumnIndex("customTagId")).equalsIgnoreCase("10")) {
                    taskdetailsbean.setStartDate(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Travel End Time")
                        && cur.getString(cur.getColumnIndex("customTagId")) != null && cur.getString(cur.getColumnIndex("customTagId")).equalsIgnoreCase("17")) {
                    taskdetailsbean.setEndDate(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                taskDetailsBean.add(taskdetailsbean);

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Log.i("string123","report of the owner ***************"+TotalList);

        return taskDetailsBean;
    }

    public ArrayList<TaskDetailsBean> getFSRReport(String taskid) {
        Cursor cur;
        ArrayList<TaskDetailsBean> taskDetailsBean = new ArrayList<TaskDetailsBean>();
        if (db == null)
            db = getReadableDatabase();
        try {
            if (!db.isOpen())
                openDatabase();
            String Loginuser = Appreference.loginuserdetails.getUsername().replace('_', '@');
            String owner = Appreference.loginuserdetails.getUsername();
            String Query="select * from taskDetailsInfo where loginuser='" + Loginuser + "'and subType='customeHeaderAttribute' and taskId='"+taskid+"'";
            cur = db.rawQuery(Query, null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                TaskDetailsBean taskdetailsbean = new TaskDetailsBean();
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Job No")) {
                    taskdetailsbean.setTaskNo(cur.getString(cur.getColumnIndex("taskDescription")));
                    if(cur.getString(cur.getColumnIndex("toUserName"))!=null &&
                            cur.getString(cur.getColumnIndex("toUserName")).equalsIgnoreCase(owner)) {
                        String username = VideoCallDataBase.getDB(context).getName(cur.getString(cur.getColumnIndex("fromUserName")));
                        if (username != null)
                            taskdetailsbean.setToUserName(username);
                        else
                            taskdetailsbean.setToUserName(cur.getString(cur.getColumnIndex("fromUserName")));
                    }else {
                        String username = VideoCallDataBase.getDB(context).getName(cur.getString(cur.getColumnIndex("toUserName")));
                        if (username != null)
                            taskdetailsbean.setToUserName(username);
                        else
                            taskdetailsbean.setToUserName(cur.getString(cur.getColumnIndex("toUserName")));
                    }
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Customer Name")) {
                    taskdetailsbean.setTaskName(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Task Id")) {
                    taskdetailsbean.setTaskId(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Task Description")) {
                    taskdetailsbean.setTaskDescription(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Estimated Travel Hrs")) {
                    taskdetailsbean.setEstimatedTravel(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Estimated Activity Hrs")) {
                    taskdetailsbean.setEstimatedActivity(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Total Travel Hrs")) {
                    taskdetailsbean.setTotalTravel(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Total Activity Hrs")) {
                    taskdetailsbean.setTotalActivity(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Travel Start Time")
                        && cur.getString(cur.getColumnIndex("customTagId"))!=null && cur.getString(cur.getColumnIndex("customTagId")).equalsIgnoreCase("12")) {
                    taskdetailsbean.setStartDate(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Travel End Time")
                        && cur.getString(cur.getColumnIndex("customTagId"))!=null && cur.getString(cur.getColumnIndex("customTagId")).equalsIgnoreCase("19")) {
                    taskdetailsbean.setEndDate(cur.getString(cur.getColumnIndex("taskDescription")));
                }

                //FSR
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Address")) {
                    taskdetailsbean.setAddress(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("M/c Model")) {
                    taskdetailsbean.setMcModel(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("M/c Sr. No")) {
                    taskdetailsbean.setMcSrNo(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Reported By")) {
                    taskdetailsbean.setReportedBy(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Hour Meter Reading")) {
                    taskdetailsbean.setHMReading(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Observation")) {
                    taskdetailsbean.setObservation(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Activity")) {
                    taskdetailsbean.setActivity(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Customer Remarks")) {
                    taskdetailsbean.setCustomerRemarks(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                if (cur.getString(cur.getColumnIndex("taskTagName")) != null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Custom Signature")) {
                    taskdetailsbean.setCustomerSignature(cur.getString(cur.getColumnIndex("taskDescription")));
                }
                taskDetailsBean.add(taskdetailsbean);

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskDetailsBean;
    }

    public ArrayList<String> getJobNo() {
        Cursor cur;
        ArrayList<String> total_id = new ArrayList<String>();
        if (db == null)
            db = getReadableDatabase();
        try {
            if (!db.isOpen())
                openDatabase();
            String Loginuser = Appreference.loginuserdetails.getUsername().replace('_', '@');
            String owner = Appreference.loginuserdetails.getUsername();
            cur = db.rawQuery("select * from taskDetailsInfo where loginuser='" + Loginuser + "'and subType='customeHeaderAttribute' and taskTagName like '%Task ID%' order by taskDescription", null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
//                TaskDetailsBean bean=new TaskDetailsBean();
//                if(cur.getString(cur.getColumnIndex("taskTagName"))!=null && cur.getString(cur.getColumnIndex("taskTagName")).equalsIgnoreCase("Job No")) {
//                   bean.setTaskNo(cur.getString(cur.getColumnIndex("taskDescription")));
//                }

                if(cur.getString(cur.getColumnIndex("taskId"))!=null) {
//                    bean.setTaskId(cur.getString(cur.getColumnIndex("taskId")));
                    if(!total_id.contains(cur.getString(cur.getColumnIndex("taskId")))) {
                        total_id.add(cur.getString(cur.getColumnIndex("taskId")));
                    }
                }

                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total_id;
    }

    public int insertORupdateStatus(TaskDetailsBean taskDetailsBean) {
        int row_id = 0;
        try {
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
//            cv.put("callid", bean.getType());
            cv.put("userId", taskDetailsBean.getFromUserId());
            cv.put("projectId", taskDetailsBean.getProjectId());
            cv.put("taskId", taskDetailsBean.getTaskId());
            cv.put("taskDescription", taskDetailsBean.getTaskDescription());
            cv.put("travelStartTime", taskDetailsBean.getTravelStartTime());
            cv.put("activityStartTime", taskDetailsBean.getActivityStartTime());
            cv.put("activityEndTime", taskDetailsBean.getActivityEndTime());
            Log.i("conv123","insertORupdateStatus===>"+taskDetailsBean.getTravelEndTime());
            cv.put("travelEndTime", taskDetailsBean.getTravelEndTime());
            cv.put("totravelstartdatetime", taskDetailsBean.getToTravelStartTime());
            cv.put("totravelenddatetime", taskDetailsBean.getToTravelEndTime());
            cv.put("remarks", taskDetailsBean.getCustomerRemarks());
            cv.put("observation", taskDetailsBean.getObservation());
            cv.put("hourMeterReading", taskDetailsBean.getHMReading());
            cv.put("status", taskDetailsBean.getProjectStatus());
            cv.put("customersignaturename", taskDetailsBean.getCustomerSignatureName());
            cv.put("actionTaken", taskDetailsBean.getActionTaken());
            cv.put("taskcompleteddate", taskDetailsBean.getTaskCompletedDate());

//            cv.put("activity", taskDetailsBean.getActivity());
//            cv.put("customerName", taskDetailsBean.getCustomerName());
//            cv.put("address", taskDetailsBean.getAddress());
//            cv.put("McModel", taskDetailsBean.getMcModel());
//            cv.put("mcSrNo", taskDetailsBean.getMcSrNo());
//            cv.put("estimatedTravelHrs", taskDetailsBean.getEstimatedTravel());
//            cv.put("estimatedActivityHrs", taskDetailsBean.getEstimatedActivity());
            cv.put("customersignature", taskDetailsBean.getCustomerSignature());
            cv.put("photo", taskDetailsBean.getPhotoPath());
            cv.put("techniciansignature", taskDetailsBean.getTechnicianSignature());
            row_id = (int) db.insert("projectStatus", null, cv);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return row_id;
    }
    public int insert_updateProjectStatus(ListallProjectBean listallProjectBean) {
        int row_id = 0;
        String task_id = "", project_id = "";
        try {
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
//            ProjectDetailsBean taskDetailsBean = listallProjectBean.getProjectDTO();
//            List<ListTaskTransaction> listTaskTransactions;
//            cv.put("callid", bean.getType());
//            cv.put("projectId", taskDetailsBean.getId());
//            cv.put("taskId", taskDetailsBean.getTaskId());
//            cv.put("fileName", taskDetailsBean.getf());
            ProjectDetailsBean projectDetailsBean = listallProjectBean.getProjectDTO();
//            if (listProjectBean.size() > 0) {
//                for (int i = 0; i < listProjectBean.size(); i++) {
//                    ProjectDetailsBean projectDetailsBean = listProjectBean.get(i);

            ArrayList<ListAllgetTaskDetails> listAllgetTaskDetailses;
            listAllgetTaskDetailses = projectDetailsBean.getListSubTask();
            if (listAllgetTaskDetailses.size() > 0) {
                for (int j = 0; j < listAllgetTaskDetailses.size(); j++) {
                    ListAllgetTaskDetails listAllgetTaskDetails = listAllgetTaskDetailses.get(j);
                    Boolean is_delete = false;
                    ArrayList<ListTaskTransaction> listAlltaskTransactions;
                    listAlltaskTransactions = listAllgetTaskDetails.getListTaskTransaction();
                    Log.i("conv123","TaskTrasaction Size===>"+listAlltaskTransactions.size());
                    if (listAlltaskTransactions.size() > 0) {
                        for (int k = 0; k < listAlltaskTransactions.size(); k++) {
                            ListTaskTransaction listTaskTransaction = listAlltaskTransactions.get(k);
                            Log.i("conv123","TaskTrasaction currentBean position===>"+k);
                            Task task_trans = listTaskTransaction.getTask();
                            cv.put("taskId", task_trans.getId());
                            task_id = String.valueOf(task_trans.getId());
                            From from_trans = listTaskTransaction.getFrom();
                            cv.put("userId", from_trans.getId());
                            Log.i("Listtransation", "listTaskTransaction " + from_trans.getId());
                            Project project_trans = listTaskTransaction.getProject();
                            cv.put("projectId", project_trans.getId());
                            project_id = String.valueOf(project_trans.getId());
//                            cv.put("travelStartTime", listTaskTransaction.getTravelStartTime());
//                            cv.put("activityStartTime", listTaskTransaction.getActivityStartTime());
//                            cv.put("activityEndTime", listTaskTransaction.getActivityEndTime());
//                            cv.put("travelEndTime", listTaskTransaction.getTravelEndTime());
//                            cv.put("totravelstartdatetime", listTaskTransaction.getTravelEndTime());
//                            cv.put("totravelenddatetime", listTaskTransaction.getTravelEndTime());
                            Log.i("conv123","================================================"+k);
                            Log.i("conv123","travelStartTime====>"+Appreference.utcToLocalTime(listTaskTransaction.getTravelStartTime()));
                            Log.i("conv123","travelEndTime====>"+Appreference.utcToLocalTime(listTaskTransaction.getTravelEndTime()));
                            Log.i("conv123","================================================="+k);

                                cv.put("travelStartTime", Appreference.utcToLocalTime(listTaskTransaction.getTravelStartTime()));
                                cv.put("activityStartTime", listTaskTransaction.getActivityStartTime());
                                cv.put("activityEndTime", listTaskTransaction.getActivityEndTime());
                                cv.put("travelEndTime", Appreference.utcToLocalTime(listTaskTransaction.getTravelEndTime()));
                                cv.put("totravelstartdatetime", listTaskTransaction.getToTravelStartDateTime());
                                cv.put("totravelenddatetime", listTaskTransaction.getToTravelEndDateTime());

                          /*  if(listTaskTransaction.getTravelStartTime()!=null && !listTaskTransaction.getTravelStartTime().equalsIgnoreCase("") && Appreference.utcToLocalTime(listTaskTransaction.getTravelStartTime())!=null){
                                cv.put("travelStartTime", Appreference.utcToLocalTime(listTaskTransaction.getTravelStartTime()));
                            }
                            if(listTaskTransaction.getActivityStartTime()!=null && !listTaskTransaction.getActivityStartTime().equalsIgnoreCase("") && Appreference.utcToLocalTime(listTaskTransaction.getActivityStartTime())!=null) {
                                cv.put("activityStartTime", listTaskTransaction.getActivityStartTime());
                            }
                            if(listTaskTransaction.getActivityEndTime()!=null && !listTaskTransaction.getActivityEndTime().equalsIgnoreCase("") && Appreference.utcToLocalTime(listTaskTransaction.getActivityEndTime())!=null) {
                                cv.put("activityEndTime", listTaskTransaction.getActivityEndTime());
                            }
                            if(listTaskTransaction.getTravelEndTime()!=null  && !listTaskTransaction.getTravelEndTime().equalsIgnoreCase("") && Appreference.utcToLocalTime(listTaskTransaction.getTravelEndTime())!=null) {
                                cv.put("travelEndTime", Appreference.utcToLocalTime(listTaskTransaction.getTravelEndTime()));
                            }
                            if(listTaskTransaction.getToTravelStartDateTime()!=null && !listTaskTransaction.getToTravelStartDateTime().equalsIgnoreCase("") && Appreference.utcToLocalTime(listTaskTransaction.getToTravelStartDateTime())!=null) {
                                cv.put("totravelstartdatetime", listTaskTransaction.getToTravelStartDateTime());
                            }
                            if(listTaskTransaction.getToTravelEndDateTime()!=null && !listTaskTransaction.getToTravelEndDateTime().equalsIgnoreCase("") && Appreference.utcToLocalTime(listTaskTransaction.getToTravelEndDateTime())!=null) {
                                cv.put("totravelenddatetime", listTaskTransaction.getToTravelEndDateTime());
                            }*/
                            cv.put("status", listTaskTransaction.getStatus());
                            cv.put("observation", listTaskTransaction.getObservation());
                            cv.put("actionTaken", listTaskTransaction.getActionTaken());
                            cv.put("taskcompleteddate", listTaskTransaction.getTaskcompletedDate());
                            cv.put("customersignaturename", listTaskTransaction.getCustomerSignatureName());
                            cv.put("hourMeterReading", listTaskTransaction.getHourMeterReading());
                            cv.put("remarks", listTaskTransaction.getRemarks());
                            cv.put("customersignature", listTaskTransaction.getSignature());
                            cv.put("photo", listTaskTransaction.getPhoto());
                            cv.put("techniciansignature", listTaskTransaction.getTechnicianSignature());




                            Log.i("Listtransation", "value===> " + cv);
                            if (IstaskIdExist(task_id, project_id) && !is_delete) {
                                row_id = (int) db.delete("projectStatus", "taskId" + "='" + task_id + "'", null);
                                is_delete = true;
                            }
                            row_id = (int) db.insert("projectStatus", null, cv);
                            Log.d("Response", " update projectStatus ");
                        }
                    }
                }
            }
//                }
//            }

//            listAllgetTaskDetailses = listallProjectBean.getDto().getListSubTask();
//            Log.i("ws123", "****** DB  projectHistory getListSubTask " + listAllgetTaskDetailses.size());


           /* ListAllgetTaskDetails parentlist;
            parentlist = taskDetailsBean.getParentTask();
            if (parentlist != null) {
                ListFromDetails listFromDetails1 = new ListFromDetails();
                listFromDetails1 = parentlist.getFrom();
                cv.put("userId", listFromDetails1.getId());
                cv.put("status", parentlist.getStatus());
                task_id = String.valueOf(parentlist.getId());
                cv.put("taskId", parentlist.getId());
            }
            cv.put("activity", taskDetailsBean.getActivity());
            cv.put("address", taskDetailsBean.getAddress());
            cv.put("McModel", taskDetailsBean.getMcModel());
            cv.put("mcSrNo", taskDetailsBean.getMcSrNo());
            cv.put("estimatedTravelHrs", taskDetailsBean.getEstimatedTravelHrs());
            cv.put("estimatedActivityHrs", taskDetailsBean.getEstimatedActivityHrs());
            cv.put("taskDescription", "");*/
//            row_id = (int) db.insert("projectStatus", null, cv);


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return row_id;
    }

    public int getCurrentStatus(String query) {
        int row_id = -1;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(query, null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    row_id = cur.getInt(cur.getColumnIndex("status"));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return row_id;
        }
    }
    public String getStatus_FromProject_history(String query) {
        int row_id = 0;
        String status = null;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(query, null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    status = cur.getString(cur.getColumnIndex("taskStatus"));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return status;
        }
    }
    public ArrayList<TaskDetailsBean> getDetails_to_complete_project(String query) {
        ArrayList<TaskDetailsBean> arrayList = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(query, null);
                cur.moveToFirst();

                while (!cur.isAfterLast()) {
                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
                    taskDetailsBean.setProjectId(cur.getString(cur.getColumnIndex("oracleProjectId")));
                    taskDetailsBean.setProjectName(cur.getString(cur.getColumnIndex("projectName")));
                    taskDetailsBean.setTaskId(cur.getString(cur.getColumnIndex("oracleTaskId")));
                    taskDetailsBean.setMcModel(cur.getString(cur.getColumnIndex("mcModel")));
                    taskDetailsBean.setMcSrNo(cur.getString(cur.getColumnIndex("mcSrNo")));
                    taskDetailsBean.setEstimatedTravel(cur.getString(cur.getColumnIndex("estimatedTravelHrs")));
                    taskDetailsBean.setEstimatedActivity(cur.getString(cur.getColumnIndex("estimatedActivityHrs")));
                    taskDetailsBean.setTaskMemberList(cur.getString(cur.getColumnIndex("taskMemberList")));
                    taskDetailsBean.setDateTime(cur.getString(cur.getColumnIndex("serviceRequestDate")));
                    taskDetailsBean.setAddress(cur.getString(cur.getColumnIndex("address")));
                    taskDetailsBean.setTaskName(cur.getString(cur.getColumnIndex("taskName")));
                    taskDetailsBean.setObservation(cur.getString(cur.getColumnIndex("observation")));
                    taskDetailsBean.setActivity(cur.getString(cur.getColumnIndex("activity")));

                    arrayList.add(taskDetailsBean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            Log.i("file", "size" + arrayList.size());
            return arrayList;
        }
    }
    public TaskDetailsBean getStatusCompletedProjectDetails(String query) {
//        ArrayList<TaskDetailsBean> arrayList = new ArrayList<>();
        TaskDetailsBean taskDetailsBean = new TaskDetailsBean();

        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(query, null);
                cur.moveToFirst();

                while (!cur.isAfterLast()) {
//                    TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
//                    taskDetailsBean.setProjectId(cur.getString(cur.getColumnIndex("projectId")));
//                    taskDetailsBean.setProjectName(cur.getString(cur.getColumnIndex("projectName")));
//                    taskDetailsBean.setTaskId(cur.getString(cur.getColumnIndex("taskId")));
//                    taskDetailsBean.setMcModel(cur.getString(cur.getColumnIndex("McModel")));
//                    taskDetailsBean.setMcSrNo(cur.getString(cur.getColumnIndex("mcSrNo")));
//                    taskDetailsBean.setEstimatedTravel(cur.getString(cur.getColumnIndex("estimatedTravelHrs")));
//                    taskDetailsBean.setEstimatedActivity(cur.getString(cur.getColumnIndex("estimatedActivityHrs")));
//                    taskDetailsBean.setTaskMemberList(cur.getString(cur.getColumnIndex("taskMemberList")));
//                    taskDetailsBean.setDateTime(cur.getString(cur.getColumnIndex("serviceRequestDate")));
//                    taskDetailsBean.setAddress(cur.getString(cur.getColumnIndex("address")));
//                    taskDetailsBean.setTaskDescription(cur.getString(cur.getColumnIndex("taskDescription")));
//                    taskDetailsBean.setObservation(cur.getString(cur.getColumnIndex("observation")));
//                    taskDetailsBean.setActivity(cur.getString(cur.getColumnIndex("activity")));
                    taskDetailsBean.setRemark(cur.getString(cur.getColumnIndex("remarks")));
                    taskDetailsBean.setCustomerSignature(cur.getString(cur.getColumnIndex("customersignature")));
                    taskDetailsBean.setPhotoPath(cur.getString(cur.getColumnIndex("photo")));
                    taskDetailsBean.setTechnicianSignature(cur.getString(cur.getColumnIndex("techniciansignature")));
                    taskDetailsBean.setObservation(cur.getString(cur.getColumnIndex("observation")));
                    taskDetailsBean.setCustomerSignatureName(cur.getString(cur.getColumnIndex("customersignaturename")));
                    taskDetailsBean.setHMReading(cur.getString(cur.getColumnIndex("hourMeterReading")));
                    taskDetailsBean.setActionTaken(cur.getString(cur.getColumnIndex("actionTaken")));
                    taskDetailsBean.setTaskCompletedDate(cur.getString(cur.getColumnIndex("taskcompleteddate")));
//                    taskDetailsBean.setTravelStartTime(cur.getString(cur.getColumnIndex("travelStartTime")));
//                    taskDetailsBean.setTravelEndTime(cur.getString(cur.getColumnIndex("travelEndTime")));
//                    taskDetailsBean.setActivityStartTime(cur.getString(cur.getColumnIndex("activityStartTime")));
//                    taskDetailsBean.setActivityEndTime(cur.getString(cur.getColumnIndex("activityEndTime")));

//                    arrayList.add(taskDetailsBean);
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
//            Log.i("file", "size" + arrayList.size());
            return taskDetailsBean;
        }
    }

    public String getOracleProjMembers(String query) {
       String Members = null;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(query, null);
                cur.moveToFirst();

                while (!cur.isAfterLast()) {
                    Members=(cur.getString(cur.getColumnIndex("taskMemberList")));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {

            return Members;
        }
    }

    public String getDivertedProjId(String query) {
        String project_id=null;
        Cursor cur;
        if(db==null)
            db=getReadableDatabase();
        try{
            if(db!=null){
                if(!db.isOpen())
                    openDatabase();
                cur=db.rawQuery(query,null);
                cur.moveToFirst();
                while(!cur.isAfterLast()){
                    project_id=(cur.getString(cur.getColumnIndex("projectId")));
                    cur.moveToNext();
                }
                    cur.close();
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return project_id;
    }

    public TaskDetailsBean getActivityTimeFromStatus(String query) {
        TaskDetailsBean taskDetailsBean = new TaskDetailsBean();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(query, null);
                cur.moveToFirst();

                while (!cur.isAfterLast()) {
                    taskDetailsBean.setTravelStartTime(cur.getString(cur.getColumnIndex("travelStartTime")));
                    taskDetailsBean.setTravelEndTime(cur.getString(cur.getColumnIndex("travelEndTime")));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            return taskDetailsBean;
        }
    }
    public ArrayList<String> getAllCurrentStatus(String Query) {
        Cursor cur;
        ArrayList<String> status_all = new ArrayList<String>();
        if (db == null)
            db = getReadableDatabase();
        try {
            if (!db.isOpen())
                openDatabase();
            cur = db.rawQuery(Query,null);
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                if(cur.getString(cur.getColumnIndex("status"))!=null) {
                        status_all.add(cur.getString(cur.getColumnIndex("status")));
                }
                cur.moveToNext();
            }
            cur.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status_all;
    }

    public int  CheckTravelEntryDetails(String query) {
        Cursor cur;
        int row=0;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(query, null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    row++;
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return row;
        }
    }

    public int getCountForTravelEntry(String query) {
        Cursor cur;
        int row=0;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(query, null);
                 row = cur.getCount();
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return row;
        }
    }

    public void insertvalueeula(boolean booleanvalue) {
        try {
            int row_id = 0;
            if (!db.isOpen())
                openDatabase();
            ContentValues cv = new ContentValues();
            cv.put("id",1);
            cv.put("selection", booleanvalue);
            if (isRecordExists("select selection from '" + EULATABLE + "'")) {
                row_id = (int) db.update(EULATABLE, cv, "id='" + 1 + "'", null);
            } else {
                row_id = (int) db.insert(EULATABLE, null, cv);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public int geteulavalue() {
        int newvalue = 0;
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery("select selection from eulaagree where id='" + 1 + "'", null);

                while(cur.moveToNext()){
                    newvalue=Integer.parseInt(cur.getString(cur.getColumnIndex("selection")));
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newvalue;

    }

    public boolean isRecordExists(String Querry) {
        Cursor cur = null;
        boolean status = false;
        try {
            if (!db.isOpen())
                openDatabase();
            cur = db.rawQuery(Querry, null);
            cur.moveToFirst();
            if (cur.getCount() > 0)
                status = true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (cur != null)
                cur.close();
            return status;
        }
    }
    public void contactRoleIdUpdate(String roleId,String roleName,int userId) {
        Log.i("chat123","contactRoleIdUpdate==>"+roleId+roleName+userId);
        ContentValues cv = new ContentValues();
        cv.put("roleId", roleId);
        cv.put("roleName", roleName);
        db.update("contact", cv, "userid" + "='" + userId + "'", null);
    }
    public ArrayList<String> getOracleProjectIdlist(String query) {
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(query, null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    if(cur.getString(cur.getColumnIndex("oracleProjectId"))!=null && !cur.getString(cur.getColumnIndex("oracleProjectId")).equalsIgnoreCase("")) {
                        arrayList.add(cur.getString(cur.getColumnIndex("oracleProjectId")));
                        Log.i("oracle123", "Error====>oracleProjectId list===>" + cur.getString(cur.getColumnIndex("oracleProjectId")));
                    }

                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("DB123","Error====>getProjectdetails===>"+e.getMessage());
        } finally {
            return arrayList;
        }
    }
    public ArrayList<String> getPerTaskcompletedDates(String query) {
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cur;
        if (db == null)
            db = getReadableDatabase();
        try {
            if (db != null) {
                if (!db.isOpen())
                    openDatabase();
                cur = db.rawQuery(query, null);
                cur.moveToFirst();
                while (!cur.isAfterLast()) {
                    arrayList.add(cur.getString(cur.getColumnIndex("taskcompleteddate")));
                    Log.i("oracle123", "Error====>taskcompleteddate list===>" + cur.getString(cur.getColumnIndex("taskcompleteddate")));
                    cur.moveToNext();
                }
                cur.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("DB123","Error====>getProjectdetails===>"+e.getMessage());
        } finally {
            return arrayList;
        }
    }
}