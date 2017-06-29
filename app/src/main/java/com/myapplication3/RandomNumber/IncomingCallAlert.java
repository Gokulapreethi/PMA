package com.myapplication3.RandomNumber;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.myapplication3.Appreference;
import com.myapplication3.DB.VideoCallDataBase;
import com.myapplication3.R;
import com.myapplication3.call_list.Call_ListBean;

import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.app.CallActivity;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.pjsip_status_code;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IncomingCallAlert extends Activity {

    TextView tv_buddyname;
    Button btn_accept, btn_reject;
    Context context;
    String hostname = null;
    String part;
    String call_strtime = null;
    private Handler handler;
    String callType;
    TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.incomingcallalert);
        context = this;
        this.setFinishOnTouchOutside(false);
        handler = new Handler();
        this.setFinishOnTouchOutside(false);
        Appreference.context_table.put("incomingcallalert", this);
        hostname = getIntent().getStringExtra("hostname");
        callType = getIntent().getStringExtra("callType");
        final String part1 = Appreference.loginuserdetails.getFirstName() + " " + Appreference.loginuserdetails.getLastName();
        Log.i("MainCall", "host " + hostname);
        Log.i("MainCall", "participant " + part1);
        part = hostname;
        part = VideoCallDataBase.getDB(context).getName(hostname);
        Log.i("MainCall", "host Name " + part);
        tv_buddyname = (TextView) findViewById(R.id.buddy_name);
        tv_title = (TextView) findViewById(R.id.tv_title);
        if (hostname != null) {
            tv_buddyname.setText(part);
        }
        tv_title.setText(" Incoming "+ callType);
        btn_reject = (Button) findViewById(R.id.layout_decline);
        btn_accept = (Button) findViewById(R.id.layout_accept);
        final String finalPart = part;
        Log.i("MainCall", "final part " + finalPart);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        try {
            Date strt_dt = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss");
            call_strtime = sdf.format(strt_dt);
        } catch (Exception e) {

        }

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.stopRingTone();
                Log.d("callhistory", "IncomingCall insertdb");
                for (int i = 0; i < MainActivity.currentCallArrayList.size(); i++) {
                    CallOpParam prm = new CallOpParam();
//                    prm.setStatusCode(pjsip_status_code.PJSIP_SC_DECLINE);
                    prm.setStatusCode(pjsip_status_code.PJSIP_SC_BUSY_HERE);
                    try {
                        MainActivity.currentCallArrayList.get(i).hangup(prm);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
                Call_ListBean call_listBean = new Call_ListBean();
                Log.i("incomingcallalert", "->1" + finalPart);
                call_listBean.setHost(finalPart);
                Log.i("incomingcallalert", "->2" + finalPart);
                call_listBean.setParticipant(part1);
                Log.i("incomingcallalert", "->3" + finalPart);
                call_listBean.setRecording_path(null);
                Log.i("incomingcallalert", "->4" + finalPart);
                call_listBean.setCall_duration("00:00");
                Log.i("incomingcallalert", "->5" + finalPart);
                call_listBean.setType("MissedCall");
                Log.i("incomingcallalert", "->6" + finalPart);
                call_listBean.setStart_time(call_strtime);
                Log.i("incomingcallalert", "->7" + finalPart);
                Log.i("call", "insertmissed" + hostname);
                Log.i("incomingcallalert", "->8" + finalPart);
                VideoCallDataBase.getDB(context).insertCall_history(call_listBean, Appreference.loginuserdetails.getUsername());
                Log.i("incomingcallalert", "->9" + finalPart);
                finish();
            }
        });

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
                MainActivity.stopRingTone();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CallOpParam prm = new CallOpParam();
                        prm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
                        try {
                            if (MainActivity.currentCall != null)
                                MainActivity.currentCall.answer(prm);
                            else
                                Toast.makeText(context,"Sorry Your call is Disconected",Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                }, 2000);

                Intent intent = new Intent(IncomingCallAlert.this, CallActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("incomingcall", true);
                intent.putExtra("host", part);
                startActivity(intent);

//                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Appreference.context_table.remove("incomingcallalert");
        Log.i("incomingcall", "IncomingCallAlert Ondestroy");
        Log.i("MainCall", "IncomingCallAlert");
        MainActivity.ReleaseWakeClock();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(12345);
    }
}
