/* $Id: CallActivity.java 5138 2015-07-30 06:23:35Z ming $ */
    /*
     * Copyright (C) 2013 Teluu Inc. (http://www.teluu.com)
	 *
	 * This program is free software; you can redistribute it and/or modify
	 * it under the terms of the GNU General Public License as published by
	 * the Free Software Foundation; either version 2 of the License, or
	 * (at your option) any later version.
	 *
	 * This program is distributed in the hope that it will be useful,
	 * but WITHOUT ANY WARRANTY; without even the implied warranty of
	 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	 * GNU General Public License for more details.
	 *
	 * You should have received a copy of the GNU General Public License
	 * along with this program; if not, write to the Free Software
	 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
	 */
package org.pjsip.pjsua2.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.Appreference;
import com.ase.CallerDetailsBean;
import com.ase.ChangeHostAdapter;
import com.ase.DB.VideoCallDataBase;
import com.ase.R;
import com.ase.call_list.Call_ListBean;
import com.ase.chat.ChatActivity;
import com.ase.chat.ChatBean;
import com.ase.gsm.PhoneStateChangeListener;
import com.ase.gsm.PhoneStateChangeNofication;

import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.AudioMediaRecorder;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallMediaInfo;
import org.pjsip.pjsua2.CallMediaInfoVector;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.Media;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.VideoPreviewOpParam;
import org.pjsip.pjsua2.VideoWindowHandle;
import org.pjsip.pjsua2.pjmedia_orient;
import org.pjsip.pjsua2.pjmedia_type;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_role_e;
import org.pjsip.pjsua2.pjsip_status_code;
import org.pjsip.pjsua2.pjsua_call_media_status;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import xml.xmlcomposer;
import xml.xmlparser;

class VideoPreviewHandler implements SurfaceHolder.Callback {
    public boolean videoPreviewActive = true;


    public void updateVideoPreview(SurfaceHolder holder) {
        Log.i("CallActivity", "updateVideoPreview method fire condition  not checked");
        if (MainActivity.currentCall != null &&

                MainActivity.vidPreview != null) {
            videoPreviewActive = true;
            Log.i("CallActivity", "updateVideoPreview method fire  true");
            Log.i("CallActivity", "updateVideoPreview method fire  boolean == " + videoPreviewActive);
            if (videoPreviewActive) {
                Log.i("CallActivity", "updateVideoPreview method fire  true");
                VideoWindowHandle vidWH = new VideoWindowHandle();
                vidWH.getHandle().setWindow(holder.getSurface());
                VideoPreviewOpParam vidPrevParam = new VideoPreviewOpParam();
                vidPrevParam.setWindow(vidWH);
                try {

//                    MainActivity.vidPreview.getVideoWindow().rotate(180);
                    MainActivity.vidPreview.start(vidPrevParam);
//                    MainActivity.vidPreview.getVideoWindow().rotate(180);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    MainActivity.vidPreview.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        updateVideoPreview(holder);
        Log.i("CallActivity", "surfaceChanged method fire  in preview side");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        try {
            if (MainActivity.vidPreview != null) {
                //                holder.removeCallback(this);
                MainActivity.vidPreview.stop();
                MainActivity.vidPreview = null;
                Log.i("CallActivity", "surfaceDestroyed method fire  in preview side");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

public class CallActivity extends Activity
        implements Handler.Callback, SurfaceHolder.Callback, PhoneStateChangeNofication {

    public static Handler handler_;
    private static VideoPreviewHandler previewHandler =
            new VideoPreviewHandler();

    private Handler handler;
    private static CallInfo lastCallInfo;
    CallerAdapter callerAdapter;
    private ArrayList<CallerDetailsBean> user_details;
    ImageView hang_up_audio_call, hang_up_audio_call_participant;
    ListView users_list;
    //    ImageView accept_audio_call;
    ImageView btn_record;
    AudioMedia audioMedia;
    AudioMediaRecorder audioMediaRecorder;
    Context context;
    public boolean recording_start = false;
    public boolean call_auto_end = true;
    private String recorded_path = null;

    AudioManager am = null;
    boolean incomingcall = false;
    String host;
    LinearLayout host_layout, participant_layout;
    ImageView speaker, micmute, participant_speaker, micmute_participant, host_speaker;
    RelativeLayout rl_mic_mute, rl_mic_mute_participant;
    TextView tv_coOrdinator;

    //    RelativeLayout videoCallScreen,audioCallScreen;

    //For ChangesHost
    String newhost = null;
    ImageView iv_changeHost;
    private Button chat;
    RelativeLayout linearLayout;
    private Chronometer chronometer;
    private boolean chronometer_started = false;
    xmlcomposer xmlComposer = null;
    private String oldhost = "";
    Handler ui_handler = new Handler();

    //For GSM
    PhoneStateChangeListener phoneStateChangeListener;

    //For Call
    String call_strtime = null;
    xmlparser xmlParser = null;
    RelativeLayout rl_co_ordinate = null;
    RelativeLayout listView_relativelayout1;
    public int rxCount =0;
    Timer rxTimer = new Timer();
    Timer rx_level_timer;
    CheckRxLevelTimerTask rxLevelTimerTask;

    // video call landscape mode

    RelativeLayout endcall_layout, headerc;
//    LinearLayout endcall_visiblelayout_land,paticipant_endcalllayout_land;
//    ImageView btn_hold_land,btn_record_land,btn_broadcast_land,btn_speaker_land,changehost_land,buttonHangup_land,participant_expand_iv_land,participant_buttonHangup_land,participant_speaker_land;
//    Chronometer chronometer_land;

    //    RelativeLayout mm;
    //    ImageView sp;
    boolean isHost = false;

    public void setHost(boolean isHost) {
        this.isHost = isHost;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.callscreen_new);

        try {
            //        Animation rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotation);
            //        LayoutAnimationController animController = new LayoutAnimationController(rotateAnim, 0);
            //        LinearLayout layout = (LinearLayout)findViewById(R.id.previewlay);
            //        layout.setLayoutAnimation(animController);
            context = this;
            recording_start = false;
            xmlComposer = new xmlcomposer();
            xmlParser = new xmlparser();
            handler = new Handler(this);
            handler_ = handler;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//            rxTimer.scheduleAtFixedRate(timerTaskObj,0,15000);
            rxTimer.schedule(callTimerTaskObj,0,10000);

//            rxLevelTimerTask = new CheckRxLevelTimerTask();
//            rx_level_timer = new Timer();
//            rx_level_timer.schedule(rxLevelTimerTask,0,15000);
//            handler.postDelayed(runnable, 15000);

            Log.d("CallActivity", "Inside the OnCreate method handler_ == " + handler_);
            Log.d("CallActivity", "Inside the OnCreate method");
            phoneStateChangeListener = new PhoneStateChangeListener();
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            telephonyManager.listen(phoneStateChangeListener,PhoneStateChangeListener.LISTEN_CALL_STATE);
            incomingcall = getIntent().getBooleanExtra("incomingcall", false);
            host = getIntent().getStringExtra("host");
            Appreference.context_table.put("callactivity", this);
            host_layout = (LinearLayout) findViewById(R.id.endcall_visiblelayout);
            participant_layout = (LinearLayout) findViewById(R.id.paticipant_endcalllayout);
            tv_coOrdinator = (TextView) findViewById(R.id.coordinator_name);
            iv_changeHost = (ImageView) findViewById(R.id.changehost);
            rl_co_ordinate = (RelativeLayout) findViewById(R.id.coordinator_layout);
            host_speaker = (ImageView) findViewById(R.id.btn_speaker);
            chat = (Button) findViewById(R.id.btn_back);
//            chat_land = (Button) findViewById(R.id.btn_back_land);

			  /*  videoCallScreen = (RelativeLayout) findViewById(R.id.video_call_screen);
				audioCallScreen = (RelativeLayout) findViewById(R.id.audio_call_screen);
	*/
            chronometer = (Chronometer) findViewById(R.id.chronometer);
//            chronometer_land = (Chronometer) findViewById(R.id.chronometer_land);
            Date strt_dt = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd hh:mm:ss");
            call_strtime = sdf.format(strt_dt);

            //            mm= (RelativeLayout)findViewById(R.id.mic_mute);
            //            sp= (ImageView) findViewById(R.id.speaker);
            participant_speaker = (ImageView) findViewById(R.id.participant_speaker);
            rl_mic_mute_participant = (RelativeLayout) findViewById(R.id.participant_mic_mute);
            micmute_participant = (ImageView) findViewById(R.id.participant_expand_iv);
            hang_up_audio_call_participant = (ImageView) findViewById(R.id.participant_buttonHangup);
            hang_up_audio_call = (ImageView) findViewById(R.id.buttonHangup);
            speaker = (ImageView) findViewById(R.id.speaker);
            rl_mic_mute = (RelativeLayout) findViewById(R.id.mic_mute);
            micmute = (ImageView) findViewById(R.id.expand_iv);

            //,,,,,,,,
            linearLayout = (RelativeLayout) findViewById(R.id.video_total_layouts);
//            endcall_layout_land = (RelativeLayout) findViewById(R.id.endcall_layout_land);
//            participant_mic_mute_land = (RelativeLayout) findViewById(R.id.participant_mic_mute_land);
            endcall_layout = (RelativeLayout) findViewById(R.id.endcall_layout);
//            headerc_land = (RelativeLayout) findViewById(R.id.headerc_land);
            headerc = (RelativeLayout) findViewById(R.id.headerc);

//            endcall_visiblelayout_land = (LinearLayout) findViewById(R.id.endcall_visiblelayout_land);
//            paticipant_endcalllayout_land = (LinearLayout) findViewById(R.id.paticipant_endcalllayout_land);

//            btn_hold_land = (ImageView) findViewById(R.id.btn_hold_land);
//            btn_record_land = (ImageView) findViewById(R.id.btn_record_land);
//            btn_broadcast_land = (ImageView) findViewById(R.id.btn_broadcast_land);
//            btn_speaker_land = (ImageView) findViewById(R.id.btn_speaker_land);
//            changehost_land = (ImageView) findViewById(R.id.changehost_land);
//            buttonHangup_land = (ImageView) findViewById(R.id.buttonHangup_land);
//            participant_expand_iv_land = (ImageView) findViewById(R.id.participant_expand_iv_land);
//            participant_buttonHangup_land = (ImageView) findViewById(R.id.participant_buttonHangup_land);
//            participant_speaker_land = (ImageView) findViewById(R.id.participant_speaker_land);


            if (incomingcall) {


                participant_layout.setVisibility(View.VISIBLE);
                host_layout.setVisibility(View.GONE);
                endcall_layout.setVisibility(View.GONE);
//                    endcall_layout_land.setVisibility(View.GONE);
                headerc.setVisibility(View.VISIBLE);
//                    headerc_land.setVisibility(View.GONE);

//                chronometer.setBase(SystemClock.elapsedRealtime());
//                chronometer.start();
//                chronometer_started = true;

                rl_co_ordinate.setVisibility(View.GONE);

                rl_mic_mute.setVisibility(View.GONE);
                micmute.setVisibility(View.GONE);
                setHost(false);
                Log.d("CallActivity", "This is a IncomingCall");

                if (Appreference.received_broadcastcall) {
                    previewHandler.videoPreviewActive = false;
                    Log.d("CallActivity", "This is a IncomingCall and broadcast call");
                }else{
                    previewHandler.videoPreviewActive = true;
                    Log.d("CallActivity", "This is a IncomingCall and not broadcast call");
                }

            } else {
                rl_co_ordinate.setVisibility(View.GONE);


//                MainActivity.earpieceRingTone();
                //                audioCallScreen.setVisibility(View.VISIBLE);
                //                videoCallScreen.setVisibility(View.GONE);

                host_layout.setVisibility(View.VISIBLE);
                participant_layout.setVisibility(View.GONE);
                endcall_layout.setVisibility(View.VISIBLE);
                headerc.setVisibility(View.VISIBLE);

                //                Date strt_dt = new Date();
                //                SimpleDateFormat sdf = new SimpleDateFormat(
                //                        "yyyy-MM-dd hh:mm:ss");
                //                call_strtime = sdf.format(strt_dt);
                setHost(true);
                Log.d("CallActivity", "This is a outgoingcall");

            }

            //            accept_audio_call = (ImageView) findViewById(R.id.buttonAccept);
            btn_record = (ImageView) findViewById(R.id.btn_record);

            listView_relativelayout1 = (RelativeLayout) findViewById(R.id.listView_relativelayout1);
            users_list = (ListView) findViewById(R.id.user_listview);

            //            final TextView reinvite=(TextView)findViewById(R.id.reinvite);
            //            reinvite.setOnClickListener(new View.OnClickListener() {
            //                @Override
            //                public void onClick(View v) {
            //                    reInvite();
            //                }
            //            });

            am = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            SurfaceView surfaceInVideo = (SurfaceView)
                    findViewById(R.id.surfaceIncomingVideo);
            //            SurfaceView surfaceInVideo2 = (SurfaceView)
            //                    findViewById(R.id.surfaceview4);
            //            SurfaceView surfaceInVideo3 = (SurfaceView)
            //                    findViewById(R.id.surfaceview2);


            SurfaceView surfacePreview = (SurfaceView)
                    findViewById(R.id.surfacePreviewCapture);


            speaker.setTag(0);
            participant_speaker.setTag(0);


            rl_mic_mute.setTag(0);

            speaker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (speaker.getTag().equals(0)) {
                        if (am != null) {
                            speaker.setTag(1);
                            speaker.setImageResource(R.drawable.speaker);
                            am.setSpeakerphoneOn(true);
                            Log.d("CallActivity", "This is a speakerphone on true");
                        }
                    } else if (speaker.getTag().equals(1)) {
                        if (am != null) {
                            speaker.setTag(0);
                            speaker.setImageResource(R.drawable.headset);
                            am.setSpeakerphoneOn(false);
                            Log.d("CallActivity", "This is a speakerphone off true");

                        }
                    }
                }
            });

            participant_speaker.setTag(0);
            participant_speaker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (participant_speaker.getTag().equals(0)) {
                        if (am != null) {
                            participant_speaker.setTag(1);
                            participant_speaker.setImageResource(R.drawable.speaker);
                            am.setSpeakerphoneOn(true);
                            Log.d("CallActivity", "This is a participant_speaker on true");
                        }
                    } else if (participant_speaker.getTag().equals(1)) {
                        if (am != null) {
                            participant_speaker.setTag(0);
                            participant_speaker.setImageResource(R.drawable.headset);
                            am.setSpeakerphoneOn(false);
                            Log.d("CallActivity", "This is a participant_speaker off true");
                        }
                    }
                }
            });

            host_speaker.setTag(1);

            host_speaker.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (host_speaker.getTag().equals(0)) {
                        if (am != null) {
                            host_speaker.setTag(1);
                            host_speaker.setImageResource(R.drawable.speaker);
                            am.setSpeakerphoneOn(true);
                            Log.d("CallActivity", "This is a host_speaker on true");
                        }
                    } else if (host_speaker.getTag().equals(1)) {
                        if (am != null) {
                            host_speaker.setTag(0);
                            host_speaker.setImageResource(R.drawable.headset);
                            am.setSpeakerphoneOn(false);
                            Log.d("CallActivity", "This is a host_speaker off true");

                        }
                    }
                }
            });
            rl_mic_mute_participant.setTag(0);
            rl_mic_mute_participant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (rl_mic_mute_participant.getTag().equals(0)) {
                                rl_mic_mute_participant.setTag(1);
                                mute(true);
                                micmute_participant.setImageResource(R.drawable.mic_mute);
                        } else if (rl_mic_mute_participant.getTag().equals(1)) {
                                rl_mic_mute_participant.setTag(0);
                                mute(false);
                                micmute_participant.setImageResource(R.drawable.mic_unmute);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            rl_mic_mute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rl_mic_mute.getTag().equals(0)) {
                        if (am != null) {
                            rl_mic_mute.setTag(1);
                            am.setMicrophoneMute(true);
                            am.setMode(AudioManager.MODE_IN_CALL);
                            am.setMode(AudioManager.STREAM_VOICE_CALL);
                            micmute.setImageResource(R.drawable.mic_mute);
                            Log.d("CallActivity", "This is a rl_mic_mute on true");

                        }
                    } else if (rl_mic_mute.getTag().equals(1)) {
                        if (am != null) {
                            rl_mic_mute.setTag(0);
                            am.setMicrophoneMute(false);
                            am.setMode(AudioManager.STREAM_VOICE_CALL);
                            am.setMode(AudioManager.MODE_IN_CALL);
                            micmute.setImageResource(R.drawable.mic_unmute);
                            Log.d("CallActivity", "This is a rl_mic_mute off true");
                        }
                    }
                }

            });






         /*   participant_speaker_land.setTag(0);
            participant_speaker_land.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (participant_speaker_land.getTag().equals(0)) {
                        if (am != null) {
                            participant_speaker_land.setTag(1);
                            participant_speaker_land.setImageResource(R.drawable.speaker);
                            am.setSpeakerphoneOn(true);
                            Log.d("CallActivity","This is a participant_speaker_land on true");
                        }
                    } else if (participant_speaker_land.getTag().equals(1)) {
                        if (am != null) {
                            participant_speaker_land.setTag(0);
                            participant_speaker_land.setImageResource(R.drawable.headset);
                            am.setSpeakerphoneOn(false);
                            Log.d("CallActivity","This is a participant_speaker_land off true");
                        }
                    }
                }
            });

            btn_speaker_land.setTag(0);

            btn_speaker_land.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (btn_speaker_land.getTag().equals(0)) {
                        if (am != null) {
                            btn_speaker_land.setTag(1);
                            btn_speaker_land.setImageResource(R.drawable.speaker);
                            am.setSpeakerphoneOn(true);
                            Log.d("CallActivity","This is a btn_speaker_land on true");
                        }
                    } else if (btn_speaker_land.getTag().equals(1)) {
                        if (am != null) {
                            btn_speaker_land.setTag(0);
                            btn_speaker_land.setImageResource(R.drawable.headset);
                            am.setSpeakerphoneOn(false);
                            Log.d("CallActivity","This is a btn_speaker_land off true");

                        }
                    }
                }
            });*/


            rl_mic_mute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rl_mic_mute.getTag().equals(0)) {
                        if (am != null) {
                            rl_mic_mute.setTag(1);
                            am.setMicrophoneMute(true);
                            am.setMode(AudioManager.MODE_IN_CALL);
                            am.setMode(AudioManager.STREAM_VOICE_CALL);
                            micmute.setImageResource(R.drawable.mic_mute);
                            Log.d("CallActivity", "This is a rl_mic_mute on true");

                        }
                    } else if (rl_mic_mute.getTag().equals(1)) {
                        if (am != null) {
                            rl_mic_mute.setTag(0);
                            am.setMicrophoneMute(false);
                            am.setMode(AudioManager.STREAM_VOICE_CALL);
                            am.setMode(AudioManager.MODE_IN_CALL);
                            micmute.setImageResource(R.drawable.mic_unmute);
                            Log.d("CallActivity", "This is a rl_mic_mute off true");
                        }
                    }
                }

            });

            //            Button buttonShowPreview = (Button)
            //                    findViewById(R.id.buttonShowPreview);

			 /*   chat.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v)
				});*/

            if (MainActivity.currentCall == null ||
                    MainActivity.currentCall.vidWin == null) {
                surfaceInVideo.setVisibility(View.GONE);
                Log.d("CallActivity", "This is a gone surfaceInVideo window ");
                //                surfaceInVideo2.setVisibility(View.GONE);
                //                surfaceInVideo3.setVisibility(View.GONE);
                //                buttonShowPreview.setVisibility(View.GONE);
            }
            //            setupVideoPreview(surfacePreview, buttonShowPreview);
            surfaceInVideo.getHolder().addCallback(this);
            //            surfaceInVideo2.getHolder().addCallback(this);
            //            surfaceInVideo3.getHolder().addCallback(this);
            if (!Appreference.received_broadcastcall) {
                surfacePreview.getHolder().addCallback(previewHandler);
                Log.d("CallActivity", "This is a NOT a received a broadcast call surfacePreview window ");
            } else {
                surfacePreview.setVisibility(View.GONE);
                Log.d("CallActivity", "This is a received a broadcast call surfacePreview window ");
            }


            // Dinesh
            //            previewHandler.videoPreviewActive = true;
            //            Log.i("preview","previewHandler.videoPreviewActive--->"+!previewHandler.videoPreviewActive);
            //            previewHandler.updateVideoPreview(surfacePreview.getHolder());
            //


            //            LinearLayout layout = (LinearLayout) findViewById(R.id.video_total_layouts);
            //            SurfaceView new_surfaceview = new SurfaceView(this);
            //            new_surfaceview.setLayoutParams(new LinearLayout.LayoutParams
            //                    (LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            //            layout.addView(new_surfaceview);
            //            new_surfaceview.getHolder().addCallback(this);


            user_details = new ArrayList<>();

            if (!incomingcall) {
                CallerDetailsBean cbBean = new CallerDetailsBean();
                cbBean.setUser_name(Appreference.loginuserdetails.getUsername());
                cbBean.setUser_id(Appreference.loginuserdetails.getUsername());
                cbBean.setRemote_uri("sip:" + Appreference.loginuserdetails.getUsername() + "@" + getResources().getString(R.string.server_ip));
                cbBean.setStatus("Coordinator");
                user_details.add(cbBean);
            }

            for (int i = 0; i < MainActivity.currentCallArrayList.size(); i++) {
                CallInfo callInfo;
                try {
                    callInfo = MainActivity.currentCallArrayList.get(i).getInfo();

                    String call_state = "";

                    if (callInfo.getState().swigValue() <
                            pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED.swigValue()) {
                        if (callInfo.getRole() == pjsip_role_e.PJSIP_ROLE_UAS) {
                            call_state = "Incoming call..";
                        } else {

                            call_state = callInfo.getStateText();
                            Log.i("CallActivity", "oncreate 1 PJSIP_INV_STATE_CONFIRMED-->" + callInfo.getStateText());
                            Log.i("CallActivity", "oncreate 1 PJSIP_INV_STATE_CONFIRMED getId-->" + callInfo.getId() + " CallID-->" + callInfo.getCallIdString());
                            if (call_state.equalsIgnoreCase("NULL")) {
                                call_state = "connecting";
                            } else if (call_state.equalsIgnoreCase("CALLING")) {
                                call_state = "connecting";
                            } else if (call_state.equalsIgnoreCase("EARLY")) {
                                call_state = "ringing";
                            } else if (call_state.equalsIgnoreCase("CONNECTING")) {
                                call_state = "connected";
                            }
                        }
                    } else if (callInfo.getState().swigValue() >=
                            pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED.swigValue()) {
                        call_state = callInfo.getStateText();
                        if (callInfo.getState() == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {
                            Log.i("CallActivity", "oncreate 2 PJSIP_INV_STATE_CONFIRMED-->" + callInfo.getStateText());
                            Log.i("CallActivity", "oncreate 1 PJSIP_INV_STATE_CONFIRMED getId-->" + callInfo.getId() + " CallID-->" + callInfo.getCallIdString());
                            if (call_state.equalsIgnoreCase("CONFIRMED")) {
                                call_state = "connected";
                            }
                        } else if (callInfo.getState() ==
                                pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
                            //                            call_state = "Call disconnected: " + callInfo.getLastReason();
                            call_state = "disconnected";
                            Log.i("SipVideo", "Call disconnected: " + callInfo.getLastReason());
                            //                            Log.i("callstatus","oncreate 1 PJSIP_INV_STATE_DISCONNECTED-->"+callInfo.getStateText());
                        }
                    }
                    CallerDetailsBean callerDetailsBean = new CallerDetailsBean();
                    if (callInfo.getRemoteUri().contains("@")) {
                        String name = null;
                        if (incomingcall) {
                            name = callInfo.getRemoteUri().substring(5);
                        } else {
                            name = callInfo.getRemoteUri().substring(4);
                        }
                        name = name.split("@")[0];
                        String call_name = VideoCallDataBase.getDB(context).getname(name);
                        Log.i("CallActivity", "name2 " + name);
                        Log.i("CallActivity", "name3 " + call_name);
                        callerDetailsBean.setUser_name(call_name);
                        callerDetailsBean.setUser_id(name);
                    }
                    callerDetailsBean.setStatus(call_state);
                    callerDetailsBean.setRemote_uri(callInfo.getRemoteUri());
                    callerDetailsBean.setCall_id(callInfo.getId());
                    callerDetailsBean.setPresence(callInfo.getCallIdString());
                    user_details.add(callerDetailsBean);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            callerAdapter = new CallerAdapter(this, R.layout.call_adapter_row, user_details);
            users_list.setAdapter(callerAdapter);
            hang_up_audio_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hangupCall(null);
                        }
                    }, 1);

                }
            });
/*
            buttonHangup_land.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hangupCall(null);
                        }
                    },1000);

                }
            });
*/
            hang_up_audio_call_participant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hangupCall(null);

                        }
                    }, 1);

                }


            });
/*
            participant_buttonHangup_land.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hangupCall(null);


                        }
                    },1000);

                }
            });
*/
//            accept_audio_call.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    acceptCall(null);
//                }
//            });
            btn_record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("audio", "record button click");
                    try {
                        recording_start = true;
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/SipVideoCall/" + getfileName() + ".wav";
                        recorded_path = path;
                        audioMedia = MyApp.ep.audDevManager().
                                getCaptureDevMedia();
                        audioMediaRecorder = new AudioMediaRecorder();
                        audioMediaRecorder.createRecorder(path);
                        audioMedia.startTransmit(audioMediaRecorder);
                        for (int j = 0; j < MainActivity.currentCallArrayList.size(); j++) {
                            MyCall myCall = MainActivity.currentCallArrayList.get(j);
                            Log.i("audio", "myCall.getId() = " + myCall.getId());
                            Iterator iterator1 = MainActivity.audioMediaHashMap.entrySet()
                                    .iterator();

                            while (iterator1.hasNext()) {

                                Map.Entry mapEntry = (Map.Entry) iterator1.next();
                                AudioMedia audioMedia1 = (AudioMedia) mapEntry.getValue();
                                try {
                                    if (MainActivity.audioMediaHashMap.containsKey(myCall.getId())) {
                                        Log.i("audio", "containsKey myCall.getId() = " + myCall.getId());
                                        audioMedia1.startTransmit(audioMediaRecorder);
                                        //                                        MainActivity.audioMediaHashMap.get(myCall.getId()).startTransmit(audioMedia);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //                    AudioMedia cap_med = Endpoint::instance().audDevManager().getCaptureDevMedia();
                    //                    try {
                    //                        recorder.createRecorder("file.wav");
                    //                        cap_med.startTransmit(recorder);
                    //                    } catch(Exception err) {
                    //                        err.printStackTrace();
                    //                    }
                }
            });
            if (MainActivity.currentCall != null) {
                try {
                    lastCallInfo = MainActivity.currentCall.getInfo();
                    updateCallState(MainActivity.currentCall,lastCallInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
//                if (lastCallInfo != null) {
//                    updateCallState(lastCallInfo);
//                }
            }

            iv_changeHost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showChangeHostDialog();

                }
            });

            if (Appreference.received_broadcastcall) {
                Button buttonShowPreview = (Button)
                        findViewById(R.id.buttonShowPreview);
                //                buttonShowPreview.setVisibility(View.GONE);
                surfacePreview.setVisibility(View.GONE);
                surfaceInVideo.setVisibility(View.VISIBLE);
                Log.d("surfaceView", " a receiver broadcast 2");
            }


            if (Appreference.broadcast_call) {
                surfaceInVideo.setVisibility(View.GONE);
                //                surfaceInVideo2.setVisibility(View.GONE);
                //                surfaceInVideo3.setVisibility(View.GONE);
            }
            if (incomingcall) {

                if (!MainActivity.isAudioCall) {
                    if (participant_speaker.getTag().equals(0)) {
                        if (am != null) {
                            participant_speaker.setTag(1);
                            participant_speaker.setImageResource(R.drawable.speaker);
                            am.setSpeakerphoneOn(true);
                            Log.d("Speaker", "receiver side speaker true");
                        }
                    }


                }

            } else {

                if (!MainActivity.isAudioCall) {
					 /*   if (host_speaker.getTag().equals(0)) {
							if (am != null) {
								host_speaker.setTag(1);
								host_speaker.setImageResource(R.drawable.speaker);
								am.setSpeakerphoneOn(true);
							}
						}*/

                   /* if (btn_speaker_land.getTag().equals(0)) {
                        if (am != null) {
                            btn_speaker_land.setTag(1);
                            btn_speaker_land.setImageResource(R.drawable.speaker);
                            am.setSpeakerphoneOn(true);
                        }
                    }*/


                }
            }
		 /*       if(!MainActivity.isAudioCall) {
					setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					if (MyApp.ep != null && MainActivity.account != null) {
						try {
	//                MainActivity.currentCall.vidWin.rotate(90);
							AccountConfig cfg = MainActivity.account.cfg;
							int cap_dev = cfg.getVideoConfig().getDefaultCaptureDevice();
							MyApp.ep.vidDevManager().setCaptureOrient(cap_dev, orient,
									true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}else{
					setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				}*/

            //            if(Appreference.broadcast_call && !MainActivity.isAudioCall) {
            //                showMyPreview();
            //            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(phoneStateChangeListener != null) {
            phoneStateChangeListener.SetListener(this);
        }
    }


    private void mute(boolean mute) {

        for (int j = 0; j < MainActivity.currentCallArrayList.size(); j++) {

            Log.d("RxLevel", "currentCallArrayList size is  === > " + MainActivity.currentCallArrayList.size());
            try {
                MyCall myCall = MainActivity.currentCallArrayList.get(j);
                Log.d("RxLevel", "1 currentCallArrayList ");
                CallInfo ci = myCall.getInfo();
                Log.d("RxLevel", "2 currentCallArrayList ");
                if (ci.getState().swigValue() == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED.swigValue()) {
                    CallMediaInfoVector cmiv = ci.getMedia();

                    for (int i = 0; i < cmiv.size(); i++) {
                        CallMediaInfo cmi = cmiv.get(i);
                        if (cmi.getType() == pjmedia_type.PJMEDIA_TYPE_AUDIO &&
                                (cmi.getStatus() ==
                                        pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE ||
                                        cmi.getStatus() ==
                                                pjsua_call_media_status.PJSUA_CALL_MEDIA_REMOTE_HOLD)) {


                            Media m = myCall.getMedia(i);
                            AudioMedia am = AudioMedia.typecastFromMedia(m);
                            if(mute) {
                                MyApp.ep.audDevManager().getCaptureDevMedia().
                                        stopTransmit(am);

//                                    am.stopTransmit(MyApp.ep.audDevManager().
//                                            getPlaybackDevMedia());
                            } else {
                                MyApp.ep.audDevManager().getCaptureDevMedia().
                                        startTransmit(am);

//                                am.startTransmit(MyApp.ep.audDevManager().
//                                        getPlaybackDevMedia());
                            }

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void chatOnClick(View view) {
        {

            ArrayList<String> username_list = new ArrayList<String>();

            for (CallerDetailsBean chatUsers : user_details) {
                username_list.add(chatUsers.getUser_name());
            }
            if (!username_list.contains(MainActivity.username)) {
                username_list.add(MainActivity.username);
            }
            ArrayList<String> sorted_username_list = sortAlphabet(username_list);
            String chat_id = null;
            for (String name : sorted_username_list) {
                if (chat_id == null) {
                    chat_id = name;
                } else {
                    chat_id = chat_id + name;
                }
            }

            ArrayList<ChatBean> chatList = VideoCallDataBase.getDB(context).getChatHistoty(chat_id,
                    MainActivity.username, "CallGroupChat");

            ArrayList<String> chatUsers_list = new ArrayList<String>();
            for (CallerDetailsBean chatUsers : user_details) {
                Log.i("Chat", "chatUsers : " + chatUsers);
                chatUsers_list.add(chatUsers.getUser_name());
            }

            String dateforrow = null;
            if (chatList != null && chatList.size() > 0) {
                dateforrow = chatList.get(0).getChatname();
            } else {
                SimpleDateFormat dateformat = new SimpleDateFormat(
                        "yyyy-MM-dd hh:mm:ss");
                dateforrow = dateformat.format(new Date()
                        .getTime());
            }
            //                    String cid = Utility.getSessionID();
            Intent i = new Intent(context, ChatActivity.class);
            i.putExtra("users", chatUsers_list);
            i.putExtra("datetime", dateforrow);
            i.putExtra("chatid", chat_id);
            i.putExtra("chattype", "CallGroupChat");
            startActivity(i);
        }
    }


    private void rotateIconsForVideo() {
        Matrix matrix = new Matrix();
        hang_up_audio_call.setScaleType(ImageView.ScaleType.MATRIX);   //required
        matrix.postRotate((float) -90, 50, 50);
        hang_up_audio_call.setImageMatrix(matrix);

        speaker.setScaleType(ImageView.ScaleType.MATRIX);
        speaker.setImageMatrix(matrix);

        micmute.setScaleType(ImageView.ScaleType.MATRIX);
        micmute.setImageMatrix(matrix);
    }

    private ArrayList<String> sortAlphabet(ArrayList<String> list) {
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });
        return list;
    }

    private void tryDrawing(SurfaceHolder holder) {
        Log.i("SipVideo", "Trying to draw...");

        Canvas canvas = holder.lockCanvas();
        if (canvas == null) {
            Log.e("SipVideo", "Cannot draw onto the canvas as it's null");
        } else {
            drawMyStuff(canvas);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawMyStuff(final Canvas canvas) {
        Random random = new Random();
        Log.i("SipVideo", "Drawing...");
        canvas.drawRGB(255, 128, 128);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("SipVideo", "came to onConfigurationChanged");
        WindowManager wm;
        Display display;
        int rotation;
        pjmedia_orient orient;

        wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        rotation = display.getRotation();

        Log.i("SipVideo", "Device orientation changed: " + rotation);
        switch (rotation) {
            case Surface.ROTATION_0:   // Portrait
                orient = pjmedia_orient.PJMEDIA_ORIENT_ROTATE_270DEG;

//                if(!MainActivity.isAudioCall) {
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                }else {
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                }
                break;
            case Surface.ROTATION_90:  // Landscape, home button on the right
                orient = pjmedia_orient.PJMEDIA_ORIENT_NATURAL;
                /*if(!MainActivity.isAudioCall) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }*/
                break;
            case Surface.ROTATION_180:
                orient = pjmedia_orient.PJMEDIA_ORIENT_ROTATE_90DEG;

                break;
            case Surface.ROTATION_270: // Landscape, home button on the left
                orient = pjmedia_orient.PJMEDIA_ORIENT_ROTATE_180DEG;
                break;
            default:
                orient = pjmedia_orient.PJMEDIA_ORIENT_ROTATE_270DEG;
        }

        if (MyApp.ep != null && MainActivity.account != null) {
            try {
//                MainActivity.currentCall.vidWin.rotate(90);
                AccountConfig cfg = MainActivity.account.cfg;
                int cap_dev = cfg.getVideoConfig().getDefaultCaptureDevice();
                MyApp.ep.vidDevManager().setCaptureOrient(cap_dev, orient,
                        true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onConfigurationChangedForAndroid(Configuration newConfig) {
        Log.i("SipVideo", "came to onConfigurationChangedForAndroid");
        WindowManager wm;
        Display display;
        int rotation;
        pjmedia_orient orient;

        wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        rotation = display.getRotation();
        System.out.println("Device orientation changed: " + rotation);
        Log.i("SipVideo", "Device orientation changed: " + rotation);
//        switch (rotation) {
//            case Surface.ROTATION_0:   // Portrait
//                orient = pjmedia_orient.PJMEDIA_ORIENT_NATURAL;
//                orient = pjmedia_orient.PJMEDIA_ORIENT_ROTATE_270DEG;
//                break;
//            case Surface.ROTATION_90:  // Landscape, home button on the right
//                orient = pjmedia_orient.PJMEDIA_ORIENT_NATURAL;
//                break;
//            case Surface.ROTATION_180:
        if (getResources().getString(R.string.videocall_for_android).equalsIgnoreCase("enable")) {
            orient = pjmedia_orient.PJMEDIA_ORIENT_ROTATE_270DEG;
        } else {
            orient = pjmedia_orient.PJMEDIA_ORIENT_ROTATE_270DEG;
        }
//                break;
//            case Surface.ROTATION_270: // Landscape, home button on the left
//                orient = pjmedia_orient.PJMEDIA_ORIENT_ROTATE_180DEG;
//                break;
//            default:
//                orient = pjmedia_orient.PJMEDIA_ORIENT_UNKNOWN;
//        }

        if (MyApp.ep != null && MainActivity.account != null) {
            try {
                AccountConfig cfg = MainActivity.account.cfg;
                int cap_dev = cfg.getVideoConfig().getDefaultCaptureDevice();
                MyApp.ep.vidDevManager().setCaptureOrient(cap_dev, orient,
                        true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Log.d("CallActivity", "Inside the onDestroy method handler_ == " + handler_);
//        MainActivity.hideCallUI();
            if(rx_level_timer != null) {
                rx_level_timer.cancel();
            }
            Appreference.changehost_request_received = false;
            Appreference.call_pause_received = false;
            Appreference.new_host = "";
            oldhost = "";
            MainActivity.medreceived = false;
            handler_ = null;
            if (chronometer != null) {
                chronometer.stop();
            }/*else if(chronometer_land != null) {
                chronometer_land.stop();
            }*/

            if(call_auto_end){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.callBusyAlertUI();
                    }
                });
            }
            if (!incomingcall) {
                MainActivity.stopRingTone();
            }
            MainActivity.totalvideoWindows.clear();
            try {
                if (audioMedia != null && audioMediaRecorder != null) {
                    Log.i("audio", "Callactivity OnDestory Method audiomedia && audioMediaRecorder!=null");
                    audioMedia.stopTransmit(audioMediaRecorder);
                    audioMediaRecorder.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            recording_start = false;
            recorded_path = null;
            Appreference.context_table.remove("callactivity");
            Appreference.callStart_time = null;
            Appreference.conference_uri = null;
            Appreference.broadcast_call = false;
            Appreference.received_broadcastcall = false;

            if (am != null)
                am.setSpeakerphoneOn(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    //        MainActivity.currentCallArrayList.clear();
    //        MainActivity.currentCall = null;
    //        MainActivity.isAudioCall = true;
    //        MainActivity.positions.clear();

		/*    try {
				if(MainActivity.vidPreview != null) {
					MainActivity.vidPreview.stop();
					MainActivity.vidPreview.delete();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}*/




    private void updateVideoWindow(boolean show) {
        if (MainActivity.currentCall != null &&
                MainActivity.currentCall.vidWin != null ) {
            SurfaceView surfaceInVideo = (SurfaceView)
                    findViewById(R.id.surfaceIncomingVideo);
            //            SurfaceView surfaceInVideo2 = (SurfaceView)
            //                    findViewById(R.id.surfaceview4);
            //            SurfaceView surfaceInVideo3 = (SurfaceView)
            //                    findViewById(R.id.surfaceview2);
            if (Appreference.broadcast_call) {
                surfaceInVideo.setVisibility(View.GONE);
                //                surfaceInVideo2.setVisibility(View.VISIBLE);
                //                surfaceInVideo2.getLayoutParams().height = ViewGroup.LayoutParams.FILL_PARENT;
                //                surfaceInVideo2.getLayoutParams().width = ViewGroup.LayoutParams.FILL_PARENT;
                //                surfaceInVideo3.setVisibility(View.GONE);
            }


            VideoWindowHandle vidWH = new VideoWindowHandle();
            if (show) {

                vidWH.getHandle().setWindow(
                        surfaceInVideo.getHolder().getSurface());
            } else {
                vidWH.getHandle().setWindow(null);
            }
            try {
                //                MainActivity.currentCall.vidWin.rotate(90);
                //                MainActivity.currentCall.vidWin.setWindow(vidWH);
                if (MainActivity.totalvideoWindows.size() > 0) {
                    MainActivity.totalvideoWindows.get(0).setWindow(vidWH);
                }
                //                MainActivity.currentCall.vidWin.rotate(1);
            } catch (Exception e) {
                e.printStackTrace();

            }

            //            VideoWindowHandle vidWH2 = new VideoWindowHandle();
            //            if (show) {
            //
            //                vidWH2.getHandle().setWindow(
            //                        surfaceInVideo2.getHolder().getSurface());
            //            } else {
            //                vidWH2.getHandle().setWindow(null);
            //            }
            //            try {
            ////                MainActivity.currentCall.vidWin.setWindow(vidWH2);
            //                if (MainActivity.totalvideoWindows.size() > 1) {
            //                    MainActivity.totalvideoWindows.get(1).setWindow(vidWH2);
            //                }
            //            } catch (Exception e) {
            //                e.printStackTrace();
            //
            //            }
            //
            //
            //            VideoWindowHandle vidWH3 = new VideoWindowHandle();
            //            if (show) {
            //
            //                vidWH3.getHandle().setWindow(
            //                        surfaceInVideo3.getHolder().getSurface());
            //            } else {
            //                vidWH3.getHandle().setWindow(null);
            //            }
            //            try {
            ////                MainActivity.currentCall.vidWin.setWindow(vidWH3);
            //                if (MainActivity.totalvideoWindows.size() > 2) {
            //                    MainActivity.totalvideoWindows.get(2).setWindow(vidWH3);
            //                }
            //            } catch (Exception e) {
            //                e.printStackTrace();
            //
            //            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        //        tryDrawing(holder);
        if (!Appreference.broadcast_call) {
            updateVideoWindow(true);
            showPreview(null);
            Log.i("CallActivity", "surfaceChanged method fire  in INvideo side for 1 to 1");
        } else{
            showMyPreview();
            Log.i("CallActivity", "surfaceChanged method fire  in INvideo side for vbc");
        }

    }

    public void surfaceCreated(SurfaceHolder holder) {
        //        tryDrawing(holder);


    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        if (!Appreference.broadcast_call) {
            updateVideoWindow(false);
            Log.i("CallActivity", "surfaceDestroyed method fire  in Invideo side 1 to 1");
        }

    }

    public void acceptCall(View view) {
        MainActivity.stopRingTone();
        CallOpParam prm = new CallOpParam();
        prm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
        try {
            MainActivity.currentCall.answer(prm);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (view != null) {
            view.setVisibility(View.GONE);
        }
    }

    public void BusyState(){
        Log.e("CallActivity", "Rejected");
        handler.post(new Runnable() {
            @Override
            public void run() {
//                hangupCall(null);
//            }
//        },1000);


                for (int i = 0; i < MainActivity.currentCallArrayList.size(); i++) {
                    CallOpParam prm = new CallOpParam();
//                    prm.setStatusCode(pjsip_status_code.PJSIP_SC_DECLINE);
//                    prm.setStatusCode(pjsip_status_code.PJSIP_SC_BUSY_HERE);
                    try {
                        MainActivity.currentCallArrayList.get(i).hangup(prm);
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
                Toast.makeText(getApplicationContext(), "This User is Busy now", Toast.LENGTH_LONG).show();
                Log.d("CallActivity", "Inside the hangup call method handler_ == " + handler_);
                handler_ = null;
                insertdb();
                Log.e("CallActivity", "hangup2");
                finish();
            }
        });
    }

    public void hangupCall(View view) {


        try {
            Log.i("PJSIP_LOG", "changehost ---->> Call hangupCall method  :: member list size is --->> : "+MainActivity.currentCallArrayList.size());
            MainActivity.stopRingTone();
            for (int i = 0; i < MainActivity.currentCallArrayList.size(); i++) {
                CallOpParam prm = new CallOpParam();
                prm.setStatusCode(pjsip_status_code.PJSIP_SC_DECLINE);
                try {
                    Log.i("PJSIP_LOG", "changehost ---->>  hangupCall fired and before hanup the call --->> "+  MainActivity.currentCallArrayList.get(i).isActive());
                    MainActivity.currentCallArrayList.get(i).hangup(prm);
                    Log.i("PJSIP_LOG", "changehost ---->>  hangupCall fired and after hanup the call --->> "+  MainActivity.currentCallArrayList.get(i).isActive());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Log.i("changehost", "came to hangupCall 2");

            for (int i = 0; i < MainActivity.currentCallArrayList.size(); i++) {
                if(MainActivity.currentCallArrayList.get(i) != null) {
                    CallInfo callInfo = MainActivity.currentCallArrayList.get(i).getInfo();
                    Log.i("changehost", "3 hangupCall :" + MainActivity.currentCallArrayList.get(i).isActive() + " state : " + callInfo.getState());
                    if(callInfo.getState() != null && callInfo.getState().swigValue() >=
                            pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED.swigValue()) {
                        Log.i("changehost", "4 hangupCall :");
                        Log.i("SipVideo", " delete: b113");

                        Log.i("SipVideo", " dump: == " + MainActivity.currentCallArrayList.get(i).dump(true," "));
//                        MainActivity.currentCallArrayList.get(i).dump(true," ");
                        MainActivity.currentCallArrayList.get(i).delete();

                    }
//                    Log.i("changehost", "4 hangupCall :" + MainActivity.currentCallArrayList.get(i).isActive());
                }
            }
            Log.i("changehost", "5 hangupCall :");
            MainActivity.currentCallArrayList.clear();
            MainActivity.currentCall = null;
            MyApp.ep.hangupAllCalls();
            Log.d("changehost","Inside the hangup call method handler_ == "+handler_);
            handler_ = null;
            Log.e("CallActivity","hangup");
            insertdb();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void setupVideoPreview(SurfaceView surfacePreview) {
        //        if(!Appreference.broadcast_call) {
        //            surfacePreview.setVisibility(previewHandler.videoPreviewActive ?
        //                    View.VISIBLE : View.GONE);
        //        }

		   /* buttonShowPreview.setText(previewHandler.videoPreviewActive ?
					getString(R.string.hide_preview) :
					getString(R.string.show_preview));*/
        if (Appreference.broadcast_call) {
            //            surfacePreview.setVisibility(View.VISIBLE);
            //                    surfacePreview.getLayoutParams().height = ViewGroup.LayoutParams.FILL_PARENT;
            //            surfacePreview.getLayoutParams().width = ViewGroup.LayoutParams.FILL_PARENT;
            //            previewHandler.updateVideoPreview(surfacePreview.getHolder());
            //            surfacePreview.getHolder().addCallback(previewHandler);

            Log.d("CallActivity","This is broadcast call TRUE in host side");

        }else {
			  /*  surfacePreview.setVisibility(previewHandler.videoPreviewActive ?
						View.VISIBLE : View.GONE);*/
            surfacePreview.setVisibility(View.VISIBLE);
            Log.d("CallActivity","This is broadcast call FALSE in host side");
        }
    }

    public void showPreview(View view) {
        SurfaceView surfacePreview = (SurfaceView)
                findViewById(R.id.surfacePreviewCapture);

        Button buttonShowPreview = (Button)
                findViewById(R.id.buttonShowPreview);

        Log.d("CallActivity","This is showPreview method call TRUE");
        //        previewHandler.videoPreviewActive = !previewHandler.videoPreviewActive;
        surfacePreview.setVisibility(View.VISIBLE);
        setupVideoPreview(surfacePreview);
        //        previewHandler.updateVideoPreview(surfacePreview.getHolder());
    }

    public void showMyPreview() {

        SurfaceView surfacePreview = (SurfaceView)
                findViewById(R.id.surfacePreviewCapture);

        //            surfacePreview.getLayoutParams().height = 500;
        //            surfacePreview.getLayoutParams().width = 500;
        if(Appreference.broadcast_call) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0,0,0,0);
            surfacePreview.setLayoutParams(layoutParams);


            surfacePreview.setVisibility(View.VISIBLE);
            Log.d("CallActivity", "This is showMyPreview method call TRUE if part");
            //        previewHandler.videoPreviewActive = !previewHandler.videoPreviewActive;
            setupVideoPreview(surfacePreview);
            previewHandler.updateVideoPreview(surfacePreview.getHolder());
        }else {


            Log.d("CallActivity", "This is showMyPreview method call TRUE else part");
            //        previewHandler.videoPreviewActive = !previewHandler.videoPreviewActive;
            setupVideoPreview(surfacePreview);
            previewHandler.updateVideoPreview(surfacePreview.getHolder());

            surfacePreview.setVisibility(View.VISIBLE);
            surfacePreview.bringToFront();

        }

    }


    private void setupVideoSurface() {
        try {
            SurfaceView surfaceInVideo = (SurfaceView)
                    findViewById(R.id.surfaceIncomingVideo);

            //        SurfaceView surfaceInVideo2 = (SurfaceView)
            //                findViewById(R.id.surfaceview4);
            //        SurfaceView surfaceInVideo3 = (SurfaceView)
            //                findViewById(R.id.surfaceview2);

            //        int margin = getResources().getDimensionPixelSize(R.dimen.surface_view_margin);
            //        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            //        lp.setMargins(margin, 0, hand0, 0);
            //        surfaceView.setLayoutParams(lp);

            SurfaceView surfacePreview = (SurfaceView)
                    findViewById(R.id.surfacePreviewCapture);
            Button buttonShowPreview = (Button)
                    findViewById(R.id.buttonShowPreview);
            //            buttonShowPreview.setVisibility(View.VISIBLE);

			   /* if(incomingcall){
					surfacePreview.setVisibility(View.GONE);
					surfaceInVideo.setVisibility(View.VISIBLE);
					surfacePreview.setVisibility(View.VISIBLE);
				}else {*/
            surfaceInVideo.setVisibility(View.VISIBLE);
            setupVideoPreview(surfacePreview);

            surfacePreview.setVisibility(View.VISIBLE);

            //            }

            Log.d("CallActivity","This is setupVideoSurface method call TRUE");
            //        surfaceInVideo2.setVisibility(View.VISIBLE);
            //        surfaceInVideo3.setVisibility(View.VISIBLE);
		 /*   if (Appreference.received_broadcastcall) {
				buttonShowPreview.setVisibility(View.GONE);
			} else {
				buttonShowPreview.setVisibility(View.GONE);
			}*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean handleMessage(Message m) {
        try {
            if (m.what == MainActivity.MSG_TYPE.CALL_STATE) {
                Log.i("SipVideo", "handlemessage in CallAvtivity");
                ArrayList<Object> objects = (ArrayList<Object>) m.obj;
                MyCall myCall = (MyCall) objects.get(0);
                CallInfo callInfo= (CallInfo) objects.get(1);
                lastCallInfo = (CallInfo) objects.get(1);
//                lastCallInfo = (CallInfo) m.obj;
                updateCallState(myCall , callInfo);

            } else if (m.what == MainActivity.MSG_TYPE.CALL_MEDIA_STATE) {
                Log.i("SipVideo", "came to handleMessage \n MainActivity.currentCall : "+MainActivity.currentCall);
                if (MainActivity.currentCall != null){
                    Log.i("SipVideo", " MainActivity.currentCall.vidWin : "+MainActivity.currentCall.vidWin);
                    Log.i("SipVideo", " MainActivity.vidPreview : "+MainActivity.vidPreview);

                    Log.i("VideoCallLogs", "CALL_MEDIA_STATE method fired in video call ");
                }
                //            if (MainActivity.currentCall != null && MainActivity.currentCall.vidWin != null) {
                if (MainActivity.currentCall != null) {
				/* Set capture orientation according to current
				 * device orientation.
				 */
                    if (!MainActivity.isAudioCall) {
//                        endcall_visiblelayout_land.setVisibility(View.GONE);
                        endcall_layout.setVisibility(View.VISIBLE);
                        headerc.setVisibility(View.VISIBLE);
                        participant_layout.setVisibility(View.VISIBLE);
                        host_layout.setVisibility(View.GONE);
//                        headerc_land.setVisibility(View.VISIBLE);
//                        paticipant_endcalllayout_land.setVisibility(View.VISIBLE);
//                        endcall_layout_land.setVisibility(View.VISIBLE);
//
//                        chronometer_land.setBase(SystemClock.elapsedRealtime());
//                        chronometer_land.start();
                        if(!chronometer_started) {
                            chronometer.setBase(SystemClock.elapsedRealtime());
                            chronometer.start();
                            chronometer_started = true;
                        }

                    }else{


                        //                        audioCallScreen.setVisibility(View.VISIBLE);
                        //                        videoCallScreen.setVisibility(View.GONE);
                        participant_layout.setVisibility(View.VISIBLE);
                        host_layout.setVisibility(View.GONE);
                        endcall_layout.setVisibility(View.VISIBLE);
//                        endcall_layout_land.setVisibility(View.GONE);
                        headerc.setVisibility(View.VISIBLE);
//                        headerc_land.setVisibility(View.GONE);

                        if(!chronometer_started) {
                            chronometer.setBase(SystemClock.elapsedRealtime());
                            chronometer.start();
                            chronometer_started = true;
                        }
                    }
                    //                    onConfigurationChanged(getResources().getConfiguration());
                    onConfigurationChangedForAndroid(getResources().getConfiguration());
				/* If there's incoming video, display it. */

                    if(!Appreference.broadcast_call && !Appreference.received_broadcastcall) {
                        if(!isHost) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    setupVideoSurface();
                                }
                            }, 150);
                        }else{
                            setupVideoSurface();
                        }


                    }
                    if(!Appreference.received_broadcastcall) {
                        Log.d("surfaceView","inside handle message");
                        showMyPreview();
                    }
                }

            } else {

				/* Message not handled */
                return false;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private void updateCallState(MyCall myCall,CallInfo ci) {
        try {
            boolean disconnect_received_from_coordinator = false;
            Log.i("CallActivity", "updateCallState in CallAvtivity"+" user :"+ci.getRemoteUri()+" oldhost : "+oldhost);
            //            ListView users_list = (ListView) findViewById(R.id.users_listview);

            //            TextView hangup = (TextView) findViewById(R.id.hang_up_audio_call);

            //            TextView tvPeer = (TextView) findViewById(R.id.textViewPeer);
            //            TextView tvState = (TextView) findViewById(R.id.textViewCallState);
//            ImageView buttonHangup = (ImageView) findViewById(R.id.buttonHangup);
            //            ImageView buttonAccept = (ImageView) findViewById(R.id.buttonAccept);
            String call_state = "";
            String previous_call_state = "";

            //            if (ci.getRole() == pjsip_role_e.PJSIP_ROLE_UAC) {
            //                buttonAccept.setVisibility(View.GONE);
            //            }

            if (ci.getState().swigValue() <
                    pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED.swigValue()) {
                if (ci.getRole() == pjsip_role_e.PJSIP_ROLE_UAS) {
                    call_state = "Incoming call..";
				/* Default button texts are already 'Accept' & 'Reject' */
                } else {
                    call_state = ci.getStateText();
                    Log.i("CallActivity", "updateCallState 1 PJSIP_INV_STATE_CONFIRMED-->" + ci.getStateText());
                    Log.i("CallActivity", "updateCallState 1 PJSIP_INV_STATE_CONFIRMED getId-->" + ci.getId() + " CallID-->" + ci.getCallIdString());
                    if (call_state.equalsIgnoreCase("NULL")) {
                        call_state = "connecting";
                    } else if (call_state.equalsIgnoreCase("CALLING")) {
                        call_state = "connecting";
                        //                        beforeAcceptVideoCallUI();
                        Log.i("CallActivity", "updateCallState : "+Appreference.changehost_request_received);
                        if(Appreference.changehost_request_received) {
//                            if(!MainActivity.currentCallArrayList.contains(myCall)) {
                            Log.i("CallActivity", "!MainActivity.currentCallArrayList.contains(myCall) : ");
//                                MainActivity.currentCallArrayList.add(myCall);
//                            }
                        }
                    } else if (call_state.equalsIgnoreCase("EARLY")) {
//                        startCallDialingTone();
                        MainActivity.earpieceRingTone();
                        call_state = "ringing";
                        //                        beforeAcceptVideoCallUI();
                    } else if (call_state.equalsIgnoreCase("CONNECTING")) {
                        if(Appreference.play_call_dial_tone) {
                            MainActivity.stopRingTone();
                            Appreference.play_call_dial_tone = false;
                        }
                        call_state = "connected";
                        if(MainActivity.isAudioCall) {
                            //                            afterAcceptAudioCallUI();
                        }else{
                            afterAcceptVideoCallUI();
                        }
//                        if(Appreference.broadcast_call && !MainActivity.isAudioCall) {
//                            showMyPreview();
//                        }
                       /* if(!MainActivity.isAudioCall) {
                         *//*   if (!chronometer_started) {
                                chronometer_land.setBase(SystemClock.elapsedRealtime());
                                chronometer_land.start();
                                chronometer_started = true;
                            }*//*
                        }else{*/
                        if (!chronometer_started) {
                            chronometer.setBase(SystemClock.elapsedRealtime());
                            chronometer.start();
                            chronometer_started = true;
                        }
//                        }
                    }
                }
            } else if (ci.getState().swigValue() >=
                    pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED.swigValue()) {
                //                buttonAccept.setVisibility(View.GONE);
                call_state = ci.getStateText();
                if(Appreference.play_call_dial_tone) {
                    MainActivity.stopRingTone();
                    Appreference.play_call_dial_tone = false;
                }
                if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {
                    //                    buttonHangup.setText("Hangup");
                    Log.i("CallActivity", "updateCallState 2 PJSIP_INV_STATE_CONFIRMED-->" + ci.getStateText()+" user :"+ci.getRemoteUri());
                    Log.i("CallActivity", "updateCallState 2 PJSIP_INV_STATE_CONFIRMED getId-->" + ci.getId() + " CallID-->" + ci.getCallIdString());
                    if (call_state.equalsIgnoreCase("CONFIRMED")) {
                        call_state = "connected";

                        if(MainActivity.isAudioCall) {
                            //                            afterAcceptAudioCallUI();
                        }else{
                            Log.i("VideoCallLogs", "CALL_MEDIA_STATE method fired in video call UI part is changed ");
                            try {
                                afterAcceptVideoCallUI();
                                MainActivity.stopRingTone();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                       /* if(!MainActivity.isAudioCall) {
                            if (!chronometer_started) {
                                chronometer_land.setBase(SystemClock.elapsedRealtime());
                                chronometer_land.start();
                                chronometer_started = true;
                            }
                        }else{*/
                        if (!chronometer_started) {
                            chronometer.setBase(SystemClock.elapsedRealtime());
                            chronometer.start();
                            chronometer_started = true;
                        }
//                        }

                    }
                } else if (ci.getState() ==
                        pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
                    //                    buttonHangup.setText("OK");
                    //                    call_state = "Call disconnected: " + ci.getLastReason();
                    call_state = "disconnected";
                    //                    Log.i("callstatus","updateCallState  PJSIP_INV_STATE_DISCONNECTED-->"+ci.getStateText());
                    Log.i("CallActivity", "Call disconnected: " + ci.getLastReason()+" user :"+ci.getRemoteUri());
                    if(!Appreference.call_pause_received) {
                        for (CallerDetailsBean callerDetailsBean : user_details) {
                            String uri = ci.getRemoteUri().substring(1, ci.getRemoteUri().length() - 1);
                            Log.i("CallActivity", "calldisconnect  uri--->" + uri);
                            Log.i("CallActivity", "calldisconnect  userlist uri-->" + callerDetailsBean.getRemote_uri());
                            Log.i("CallActivity", "calldisconnect  status details-->" + callerDetailsBean.getStatus());
                            if (callerDetailsBean.getRemote_uri().equalsIgnoreCase(uri)) {
                                if (callerDetailsBean.getStatus().equalsIgnoreCase("Coordinator")) {
//                                    insertdb();
                                    Log.e("CallActivity", "hangup4");
//                                    finish();
                                    disconnect_received_from_coordinator = true;
                                    break;
                                }
                            }
                        }
                    }

//                    if(ci.getRemoteUri().contains(oldhost)) {
//                        makeCall(oldhost);
//                    }
                }
            }

            //            tvPeer.setText(ci.getRemoteUri());
            //            tvState.setText(call_state);
            boolean have_autoredial = false;
            if (MainActivity.isAudioCall) {
                //                accept_audio_call.setVisibility(View.VISIBLE);
                if (ci.getRole() == pjsip_role_e.PJSIP_ROLE_UAC) {
                    //                    accept_audio_call.setVisibility(View.GONE);
                }
                if (ci.getState().swigValue() <
                        pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED.swigValue()) {

                } else if (ci.getState().swigValue() >=
                        pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED.swigValue()) {
                    //                    accept_audio_call.setVisibility(View.GONE);

                }
				 /*   users_list.setVisibility(View.VISIBLE);
					hang_up_audio_call.setVisibility(View.VISIBLE);
					hang_up_audio_call_participant.setVisibility(View.VISIBLE);
					btn_record.setVisibility(View.VISIBLE);
	//                linearLayout.setVisibility(View.GONE);*/
                //                endcall_layout.setVisibility(View.VISIBLE);
                for (int i = 0; i < user_details.size(); i++) {
                    Log.i("SipVideo", "Call state is ==>> pjsip_status_code.PJSIP_SC_NOT_FOUND   0");
                    if (ci.getRemoteUri().equals(user_details.get(i).getRemote_uri())) {
                        if(user_details.get(i) != null && user_details.get(i).getStatus() != null) {
                            previous_call_state = user_details.get(i).getStatus();
                        }
                        Log.i("SipVideo", "Call state is ==>> pjsip_status_code.PJSIP_SC_NOT_FOUND   1  == "+ ci.getState() + "cccccccc  " +ci.getStateText());
                        if(call_state.equals("disconnected") && (user_details.get(i).getStatus() == null || !user_details.get(i).getStatus().equalsIgnoreCase("connected"))) {
                            Log.i("SipVideo", "Call state is ==>> pjsip_status_code.PJSIP_SC_NOT_FOUND   2");
                            if(user_details.get(i).getRetryCallCount() < 6){
                                Log.i("SipVideo", "Call state is ==>> pjsip_status_code.PJSIP_SC_NOT_FOUND   3");
                                user_details.get(i).setRetryCallCount(user_details.get(i).getRetryCallCount() + 1);


                                Log.d("SipVideo"," count is  == "+user_details.get(i).getRetryCallCount());
								  /*  if(ci.getState().equals(pjsip_status_code.PJSIP_SC_BUSY_HERE)) {
										user_details.get(i).setStatus(call_state);
										return;
									}*/
                                Log.d("Last State Inside","last state is ===>>"+ci.getLastStatusCode() +"    last reson ==>>  "+ci.getLastReason() +"   state Text "+ci.getStateText());

                                if(ci.getLastStatusCode()== pjsip_status_code.PJSIP_SC_NOT_FOUND){
                                    //                                    user_details.get(i).setStatus(call_state);
                                    retryCall(ci);
                                    Log.i("SipVideo", "Call state is ==>> pjsip_status_code.PJSIP_SC_NOT_FOUND ");
                                }else if(ci.getLastStatusCode()== pjsip_status_code.PJSIP_SC_BUSY_HERE) {

                                    call_auto_end = true;
                                    user_details.get(i).setStatus(call_state);
                                    Log.i("SipVideo", "Call state is ==>> pjsip_status_code.PJSIP_SC_BUSY_HERE ");
                                }else{
                                    //                                    retryCall(ci);
                                    user_details.get(i).setStatus(call_state);
                                    Log.i("SipVideo", "Call state is ==>> pjsip_status_code.PJSIP_SC_NOT_FOUND not matched   "+ci.getState());
                                }

								  /*  if(ci.getState().equals(pjsip_status_code.PJSIP_SC_BUSY_HERE)){
										user_details.get(i).setStatus(call_state);
										Log.i("SipVideo", "Call state is ==>> pjsip_status_code.PJSIP_SC_NOT_FOUND ");
										return;
									}*/


                                //


                            }else{
                                user_details.get(i).setStatus(call_state);
                            }
                        }else{
                            user_details.get(i).setStatus(call_state);
                        }

                        user_details.get(i).setCall_id(ci.getId());
                        user_details.get(i).setPresence(ci.getCallIdString());
                        if (user_details.get(i).isAuto_redial() && call_state.equals("disconnected") && user_details.get(i).getRetryCallCount() < 6) {
                            // working fn temporary commended 1st time redial fn(check)
                            //                            have_autoredial = true;
                        }
                    }
                }
                Log.i("CallActivity", "user_details : " + user_details.size());
                callerAdapter.notifyDataSetChanged();
                Log.i("CallActivity", "isHost--->" + isHost);
                if (isHost) {
                    send_dashboardrequest();
                }
            } else {
                if (ci.getRole() == pjsip_role_e.PJSIP_ROLE_UAC) {
                    //                    accept_audio_call.setVisibility(View.GONE);
                }
                if (ci.getState().swigValue() <
                        pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED.swigValue()) {

                } else if (ci.getState().swigValue() >=
                        pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED.swigValue()) {
                    //                    accept_audio_call.setVisibility(View.GONE);

                }

                for (int i = 0; i < user_details.size(); i++) {
                    if (ci.getRemoteUri().equals(user_details.get(i).getRemote_uri())) {
                        if(call_state.equals("disconnected") && (user_details.get(i).getStatus() == null || !user_details.get(i).getStatus().equalsIgnoreCase("connected"))) {

                            if(user_details.get(i).getRetryCallCount() < 6){
                                user_details.get(i).setRetryCallCount(user_details.get(i).getRetryCallCount() + 1);

                                Log.d("CallActivity"," count is  == "+user_details.get(i).getRetryCallCount());
							   /* if(ci.getState().equals(pjsip_status_code.PJSIP_SC_BUSY_HERE)) {
									user_details.get(i).setStatus(call_state);
									return;
								}*/
                                Log.d("Last State Inside else","last state is ===>>"+ci.getLastStatusCode() +"    last reson ==>>  "+ci.getLastReason() +"   state Text "+ci.getStateText());

                                if(ci.getLastStatusCode() == pjsip_status_code.PJSIP_SC_NOT_FOUND){
                                    retryCall(ci);
                                    Log.i("SipVideo", "Call state is for video==>> pjsip_status_code.PJSIP_SC_NOT_FOUND ");
                                }else if(ci.getLastStatusCode()== pjsip_status_code.PJSIP_SC_BUSY_HERE) {

                                    call_auto_end = true;
                                    user_details.get(i).setStatus(call_state);
                                    Log.i("SipVideo", "Call state is ==>> pjsip_status_code.PJSIP_SC_BUSY_HERE ");
                                }else{
                                    user_details.get(i).setStatus(call_state);
                                }
                                //                            retryCall(ci);

                            }else{
                                user_details.get(i).setStatus(call_state);
                            }
                        }else{
                            user_details.get(i).setStatus(call_state);
                        }
                        user_details.get(i).setCall_id(ci.getId());
                        user_details.get(i).setPresence(ci.getCallIdString());
                        if (user_details.get(i).isAuto_redial() && call_state.equals("disconnected")) {
                            // working fn temporary comented
                            //                            have_autoredial = true;
                        }
                    }
                }
                callerAdapter.notifyDataSetChanged();
                Log.i("CallActivity", "Videocall isHost--->" + isHost);
                if (isHost) {
                    send_dashboardrequest();
                }
				 /*   rl_co_ordinate.setVisibility(View.GONE);
					users_list.setVisibility(View.GONE);
	//                hang_up_audio_call.setVisibility(View.GONE);
					btn_record.setVisibility(View.GONE);
	//                accept_audio_call.setVisibility(View.GONE);
					listView_relativelayout1.setVisibility(View.GONE);
					linearLayout.setVisibility(View.VISIBLE);*/
            }

            if(!isHost && call_state.equalsIgnoreCase("disconnected")) {
                if(ci.getState() != null && ci.getState().swigValue() >=
                        pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED.swigValue()) {
                    Log.i("SipVideo", " delete: b114 \n previous_call_state : "+previous_call_state);
                    myCall.delete();
                }
            }

            if (have_autoredial && isHost && Appreference.call_pause_received) {
                //                autoRedial(ci);
                //                finish();
            } else {
                int connected_users = 0;
                for (int i = 0; i < user_details.size(); i++) {
                    if (user_details.get(i).getStatus().startsWith("disconnected")) {

                    } else if (user_details.get(i).getStatus().equalsIgnoreCase("Coordinator")) {

                    } else {
                        connected_users = connected_users + 1;
                    }
                }
                Log.i("CallActivity", "Value" + user_details.size()+ " connected_users :"+connected_users);
                if (disconnect_received_from_coordinator || connected_users == 0) {
//                    MyApp.ep.hangupAllCalls();
                    MainActivity.audioMediaHashMap.clear();
                    MainActivity.currentCallArrayList.clear();
                    MainActivity.currentCall = null;
                    Log.i("CallActivity", "CallActivity updateCallState MainActivity.currentCall=null");
                    insertdb();
                    Log.e("CallActivity","hangup3");
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeCall(final String remote_uri){
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < MainActivity.currentCallArrayList.size(); i++) {
                        MyCall myCall = MainActivity.currentCallArrayList.get(i);
                        if (myCall != null && myCall.getInfo() != null) {
                            Log.i("changehost", "makeCall : remote_uri" + remote_uri + " myCall.getInfo().getRemoteUri() :"+myCall.getInfo().getRemoteUri());
                            if (remote_uri.equalsIgnoreCase(myCall.getInfo().getRemoteUri())) {
                                MainActivity.currentCallArrayList.remove(i);
                                break;
                            }
                        }
                    }

                    MyCall call = new MyCall(MainActivity.account, -1);
                    CallOpParam prm = new CallOpParam(true);
                    CallSetting opt = prm.getOpt();
                    opt.setAudioCount(1);
                    if (MainActivity.isAudioCall) {
                        opt.setVideoCount(0);
                        MainActivity.isAudioCall = true;
                    } else {
                        opt.setVideoCount(1);
                        MainActivity.isAudioCall = false;
                    }
                    Log.i("changehost", "makeCall : buddy uri--->" + remote_uri);
                    try {
                        call.makeCall(remote_uri, prm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    MainActivity.currentCallArrayList.add(call);
                    MainActivity.currentCall = call;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void autoRedial(final CallInfo ci) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    for (int i = 0; i < MainActivity.currentCallArrayList.size(); i++) {
                        MyCall myCall = MainActivity.currentCallArrayList.get(i);
                        if (myCall != null && myCall.getInfo() != null) {
                            if (ci.getRemoteUri().equalsIgnoreCase(myCall.getInfo().getRemoteUri())) {
                                MainActivity.currentCallArrayList.remove(i);
                                break;
                            }
                        }
                    }

                    MyCall call = new MyCall(MainActivity.account, -1);
                    CallOpParam prm = new CallOpParam(true);
                    CallSetting opt = prm.getOpt();
                    opt.setAudioCount(1);
                    if (MainActivity.isAudioCall) {
                        opt.setVideoCount(0);
                        MainActivity.isAudioCall = true;
                    } else {
                        opt.setVideoCount(1);
                        MainActivity.isAudioCall = false;
                    }
                    Log.i("CallActivity", "buddy uri--->" + ci.getRemoteUri());
                    try {
                        call.makeCall(ci.getRemoteUri(), prm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    MainActivity.currentCallArrayList.add(call);
                    MainActivity.currentCall = call;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 6000);
    }


    // auto retail without time limt


    private void retryCall(final CallInfo ci) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    for (int i = 0; i < MainActivity.currentCallArrayList.size(); i++) {
                        MyCall myCall = MainActivity.currentCallArrayList.get(i);
                        if (myCall != null && myCall.getInfo() != null) {
                            if (ci.getRemoteUri().equalsIgnoreCase(myCall.getInfo().getRemoteUri())) {
                                MainActivity.currentCallArrayList.remove(i);
                                break;
                            }
                        }
                    }

                    MyCall call = new MyCall(MainActivity.account, -1);
                    CallOpParam prm = new CallOpParam(true);
                    CallSetting opt = prm.getOpt();
                    opt.setAudioCount(1);
                    if (MainActivity.isAudioCall) {
                        opt.setVideoCount(0);
                        MainActivity.isAudioCall = true;
                    } else {
                        opt.setVideoCount(1);
                        MainActivity.isAudioCall = false;
                    }
                    Log.i("CallActivity", "autoredial  buddy uri--->" + ci.getRemoteUri());
                    try {
                        call.makeCall(ci.getRemoteUri(), prm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    MainActivity.currentCallArrayList.add(call);
                    MainActivity.currentCall = call;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },5000);
    }

    //before accept the screen
    public void beforeAcceptVideoCallUI(){
        users_list.setVisibility(View.VISIBLE);
        hang_up_audio_call.setVisibility(View.VISIBLE);
        hang_up_audio_call_participant.setVisibility(View.VISIBLE);
        btn_record.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);

        Log.d("VideoCall","View video Call beforeAcceptVideoCallUI View");


    }

    //after accept the screen
    public void afterAcceptVideoCallUI()
    {


//            paticipant_endcalllayout_land.setVisibility(View.GONE);
        rl_co_ordinate.setVisibility(View.GONE);
        users_list.setVisibility(View.GONE);
        btn_record.setVisibility(View.GONE);
        listView_relativelayout1.setVisibility(View.GONE);
//            endcall_layout_land.setVisibility(View.VISIBLE);
//            endcall_visiblelayout_land.setVisibility(View.VISIBLE);
//            headerc_land.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        Log.d("VideoCall","View video Call afterAcceptVideoCallUI View");

    }

    @Override
    public void PhoneStateChangeDelegate(int state) {
        if (state != TelephonyManager.CALL_STATE_IDLE) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hangupCall(null);
                }
            }, 1);
        }
    }


    //after accept the screen



    public class CallerAdapter extends ArrayAdapter<CallerDetailsBean> {
        LayoutInflater layoutInflater;
        Context adapter_context;
        ArrayList<CallerDetailsBean> user_details;

        public CallerAdapter(Context context, int resource, ArrayList<CallerDetailsBean> user_details) {
            super(context, resource, user_details);
            Log.i("CallActivity", "CallerAdapter user_details : " + user_details.size());

            adapter_context = context;
            this.user_details = user_details;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.i("CallActivity", "getView CallerAdapter");
            View view = convertView;
            try {
                if (view == null) {
                    layoutInflater = (LayoutInflater) adapter_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = layoutInflater.inflate(R.layout.call_adapter_row, null, false);
                }
                RelativeLayout coordinator_layout = (RelativeLayout) view.findViewById(R.id.user_layout);
                TextView user_name = (TextView) view.findViewById(R.id.callername);
                ImageView individualChat = (ImageView) view.findViewById(R.id.individual_chat);
                ImageView redial = (ImageView) view.findViewById(R.id.redial);
                TextView presence = (TextView) view.findViewById(R.id.user_presence);
                final CallerDetailsBean callerDetailsBean = user_details.get(position);
                if (callerDetailsBean.getStatus() != null) {
                    presence.setText(callerDetailsBean.getStatus());
                }

                if (callerDetailsBean.getUser_id() != null && callerDetailsBean.getUser_id().equalsIgnoreCase(MainActivity.username)) {
                    user_name.setText("Me");
                    individualChat.setVisibility(View.GONE);
                } else {
                    user_name.setText(callerDetailsBean.getUser_name());
                    individualChat.setVisibility(View.GONE);
                }

                redial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (callerDetailsBean.getStatus().equalsIgnoreCase("disconnected")) {
                            MyCall call = new MyCall(MainActivity.account, -1);
                            CallOpParam prm = new CallOpParam(true);
                            CallSetting opt = prm.getOpt();
                            opt.setAudioCount(1);
                            if (MainActivity.isAudioCall) {
                                opt.setVideoCount(0);
                                MainActivity.isAudioCall = true;
                            } else {
                                opt.setVideoCount(1);
                                MainActivity.isAudioCall = false;
                            }
                            Log.i("redial", "buddy uri--->" + callerDetailsBean.getRemote_uri());
                            try {
                                call.makeCall(callerDetailsBean.getRemote_uri(), prm);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            MainActivity.currentCallArrayList.add(call);
                            MainActivity.currentCall = call;
                            Log.i("CallActivity", "CallerAdapter getView MainActivity.currentCall=call");
                        }
                    }
                });

                individualChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> username_list = new ArrayList<String>();
                        username_list.add(MainActivity.username);
                        username_list.add(callerDetailsBean.getUser_name());

                        ArrayList<String> sorted_username_list = sortAlphabet(username_list);
                        String chat_id = null;
                        for (String name : sorted_username_list) {
                            if (chat_id == null) {
                                chat_id = name;
                            } else {
                                chat_id = chat_id + name;
                            }
                        }

                        ArrayList<ChatBean> chatList = VideoCallDataBase.getDB(context).getChatHistoty(chat_id,
                                MainActivity.username, "CallIndivitualChat");

                        ArrayList<String> chatUsers_list = new ArrayList<String>();
                        for (CallerDetailsBean chatUsers : user_details) {
                            chatUsers_list.add(chatUsers.getUser_name());
                        }

                        String dateforrow;
                        if (chatList != null && chatList.size() > 0) {
                            dateforrow = chatList.get(0).getChatname();
                        } else {
                            SimpleDateFormat dateformat = new SimpleDateFormat(
                                    "yyyy-MM-dd hh:mm:ss");
                            dateforrow = dateformat.format(new Date()
                                    .getTime());
                        }
                        Intent i = new Intent(context, ChatActivity.class);
                        i.putExtra("users", chatUsers_list);
                        i.putExtra("datetime", dateforrow);
                        i.putExtra("chatid", chat_id);
                        i.putExtra("chattype", "CallIndivitualChat");
                        startActivity(i);
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }

            return view;
        }
    }

    public void reInvite() {

    }

    public void holdCall() {
        Log.i("SipVideo", "holdCall");
        CallOpParam prm = new CallOpParam(true);

        try {
            MainActivity.currentCallArrayList.get(0).setHold(prm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unHoldCall() {
        Log.i("SipVideo", "unHoldCall");
        CallOpParam prm = new CallOpParam(true);

        prm.getOpt().setFlag(1);
        try {
            MainActivity.currentCallArrayList.get(0).reinvite(prm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getfileName() {
        Date curDate = new Date();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("ddMMMyyHHmmss");
        return sdf.format(curDate);
    }

    public void Recording(int call_id, AudioMedia am) {
        try {
            Log.i("audio", "Call Activity Recording Method");
            if (audioMedia != null && audioMediaRecorder != null) {
                Log.i("audio", "Call Activity Recording Method audioMedia!=null");
                am.startTransmit(audioMediaRecorder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void showChangeHostDialog() {

        final Dialog chdialog = new Dialog(context);
        chdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        chdialog.setContentView(R.layout.changehostdialoglayout);
        chdialog.setCancelable(true);
        chdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        chdialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        ListView lv = (ListView) chdialog.findViewById(R.id.dialoglist);

        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        TextView tv_fttitle = (TextView) chdialog.findViewById(R.id.tv_fttitle);
        TextView cancel_tv = (TextView) chdialog.findViewById(R.id.Cancel);
        cancel_tv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                chdialog.dismiss();
                //                cancelDialog();

            }
        });

        ArrayList<CallerDetailsBean> cbList = new ArrayList();
        Log.i("changehostbug", "came to showChangeHostDialog where user_details :" + user_details);
        if (user_details != null && user_details.size() > 0) {
            Log.i("changehostbug", "user_details.size() : " + user_details.size());
            for (CallerDetailsBean cb : user_details) {
                Log.i("changehostbug", "cb.getPresence() :" + cb.getStatus());
                if (cb.getStatus() != null && (cb.getStatus().equalsIgnoreCase("Connected") || cb.getStatus().equalsIgnoreCase("Coordinator"))) {
                    if (!cb.getUser_name().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))
                        cbList.add(cb);
                }
            }
        }
        //        else {
        //            if (mySecondArrayAdapter != null) {
        //                for (CallerBean cb : mySecondArrayAdapter.getAllItems()) {
        //
        //                    if (cb.getisSwnUser() == 1
        //                            && (cb.getPresence().equals("Connected"))) {
        //                        if (cb.getToNnumber().contains(".ccglobal")) {
        //                            cbList.add(cb);
        //                        }
        //                    }
        //                }
        //            }
        //        }
        final ChangeHostAdapter clad = new ChangeHostAdapter(
                context, cbList);

        lv.setAdapter(clad);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // TODO Auto-generated method stub

                final int pos = position;
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        try {
                            //                            showprogress(getResources().getString(R.string.Change_host_Processing));
                            //                            AppReferences.printLog("CallScreen", "Result---> change host processing", "DEBUG", null);
                            CallerDetailsBean cb = clad.getItem(pos);
                            String[] changehost_info = new String[9];
                            ArrayList<String[]> userinfo = new ArrayList();
                            changehost_info[0] = MainActivity.username;
                            changehost_info[1] = MainActivity.username;
                            changehost_info[2] = "";
                            changehost_info[3] = "";
                            changehost_info[4] = "";
                            changehost_info[5] = "";
                            changehost_info[6] = "";
                            changehost_info[7] = "";
                            changehost_info[8] = "";


                            //                            if (AppReferences.isLine_1) {
                            //                                Log.i("participant", "Showdialog AppReferences.isLine_1");
                            //
                            //                                AppReferences.printLog("CallScreen", "AppReferences.conferenceURI_Line1 : " + AppReferences.conferenceURI_Line1, "DEBUG", null);
                            //                                if (participantBeanList != null && AppReferences.conferenceURI_Line1 != null) {
                            //                                    changehostbtnclicked = true;
                            //                                    Log.i("participant", "First call participantBeanList!=null");
                            //                                    Log.d("changehost", "inside if "
                            //                                            + participantBeanList);
                            //                                    for (ParticipantBean pb : participantBeanList) {
                            //                                        Log.d("changehost",
                            //                                                "inside if callerbean Tonumber " + cb.getToNnumber());
                            //                                        Log.d("changehost",
                            //                                                "inside if ParticipantBean SipEndPoint" + pb.getSipEndPoint());
                            //                                        AppReferences.printLog("CallScreen", " callerbean Tonumber :" + cb.getToNnumber() + " ParticipantBean SipEndPoint :" + pb.getSipEndPoint(), "DEBUG", null);
                            //                                        if (pb.getSipEndPoint() != null) {
                            //                                            if (pb.getSipEndPoint().equalsIgnoreCase(
                            //                                                    cb.getToNnumber())) {
                            //                                                Log.d("changehost",
                            //                                                        "inside if calling ws");
                            //                                                callConferenceCreateWS(pb);
                            newhost = cb.getRemote_uri();
                            Log.i("changehost", "newhost--->" + newhost);
                            for (CallerDetailsBean callerDetailsBean : user_details) {
                                if (!callerDetailsBean.getUser_name().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    String[] userdetails = new String[6];

                                    userdetails[0] = callerDetailsBean.getUser_name();
                                    userdetails[1] = callerDetailsBean.getUser_name();
                                    userdetails[2] = callerDetailsBean.getStatus();
                                    userdetails[3] = "";
                                    userdetails[4] = "";
                                    userdetails[5] = "";
                                    userinfo.add(userdetails);
                                }
                            }

                            for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
                                String name = MainActivity.account.buddyList.get(i).cfg.getUri();
                                Log.i("changehost", "buddyname-->" + name);
                                for (CallerDetailsBean detailsBean : user_details) {
                                    if (detailsBean.getRemote_uri().equalsIgnoreCase(name)) {
                                        Log.i("changehost", "both users are same");
                                        if (detailsBean.getRemote_uri().equalsIgnoreCase(newhost)) {

                                            String changehost_requestxml = xmlComposer
                                                    .composeChangeHostRequestXml(
                                                            changehost_info,
                                                            userinfo,
                                                            "changehost_request", null);

                                            MyBuddy myBuddy = MainActivity.account.buddyList.get(i);
                                            SendInstantMessageParam prm = new SendInstantMessageParam();
                                            prm.setContent(changehost_requestxml);
                                            boolean valid = myBuddy.isValid();
                                            Log.i("changehost", "valid ======= " + valid);
                                            try {
                                                myBuddy.sendInstantMessage(prm);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } else {

                                            String callpause_requestxml = xmlComposer
                                                    .composeChangeHostRequestXml(
                                                            changehost_info,
                                                            userinfo,
                                                            "callpause_request", null);

                                            MyBuddy myBuddy = MainActivity.account.buddyList.get(i);
                                            SendInstantMessageParam prm = new SendInstantMessageParam();
                                            prm.setContent(callpause_requestxml);
                                            boolean valid = myBuddy.isValid();
                                            Log.i("changehost", "valid ======= " + valid);
                                            try {
                                                myBuddy.sendInstantMessage(prm);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                            //                                                transferCallId = cb.getCall_id();
                            //                                                Log.i("CH", "transferCallId--->" + transferCallId);
                            //                                                Log.d("changehost",
                            //                                                        "new host " + cb.getUserName());
                            //
                            //                                                ArrayList<Integer> holdCallIds = new ArrayList<Integer>();
                            //                                                if(callerBean_Array != null) {
                            //                                                    for (int i = 0; i < callerBean_Array.size(); i++) {
                            //                                                        CallerBean calBean = callerBean_Array.get(i);
                            //                                                        if (calBean.getCall_id() != -1) {
                            //                                                            holdCallIds.add(calBean.getCall_id());
                            //                                                        }
                            //                                                    }
                            //                                                }
                            //                                                if (holdCallIds.size() > 0) {
                            //                                                    int[] holdCallidsArray = new int[holdCallIds.size()];
                            //                                                    for (int k = 0; k < holdCallIds.size(); k++) {
                            //                                                        holdCallidsArray[k] = holdCallIds.get(k);
                            //                                                    }
                            //                                                    CommunicationBean h_bean = new CommunicationBean();
                            //                                                    h_bean.setOperationType(sip_operation_types.HOLD_INDIVIDUAL);
                            //                                                    h_bean.setCall_ids(holdCallidsArray);
                            //                                                    AppReferences.sipQueue.addMsg(h_bean);
                            //
                            //                                                    CommunicationBean ho_bean = new CommunicationBean();
                            //                                                    ho_bean.setCall_ids(holdCallidsArray);
                            //                                                    ho_bean.setOperationType(sip_operation_types.HOLD_OWN);
                            //                                                    AppReferences.sipQueue
                            //                                                            .addMsg(ho_bean);
                            //
                            //                                                }
                            //
                            ////                                                CommunicationBean h_bean = new CommunicationBean();
                            ////                                                h_bean.setOperationType(sip_operation_types.HOLD_SESSION);
                            //////                                                h_bean.setCall_id(-1);
                            ////                                                AppReferences.sipQueue.addMsg(h_bean);
                            //
                            ////                                                CommunicationBean ho_bean = new CommunicationBean();
                            ////                                                ho_bean.setOperationType(sip_operation_types.PLAY_HOLDTONE);
                            ////                                                AppReferences.sipQueue
                            ////                                                        .addMsg(ho_bean);
                            //
                            //                                                break;
                            //                                            }
                            //                                        }
                            //                                    }
                            //
                            //                                    if (keepaliveScheduler_Line1 != null)
                            //                                        keepaliveScheduler_Line1.cancel();
                            //
                            //                                    chdialog.dismiss();
                            //
                            //                                } else {
                            //                                    AppReferences.printLog("CallScreen", getResources().getString(R.string.Change_Host_request_cannot_be_processed), "DEBUG", null);
                            //                                    changehostbtnclicked = false;
                            //                                    chdialog.dismiss();
                            //                                    cancelDialog();
                            ////                                    Toast.makeText(
                            ////                                            context,
                            ////                                            getResources().getString(R.string.Change_Host_request_cannot_be_processed),
                            ////                                            Toast.LENGTH_SHORT).show();
                            //                                    showToast(getResources().getString(R.string.Change_Host_request_cannot_be_processed));
                            //
                            //                                }
                            //                            } else {
                            //                                Log.i("participant", "Showdialog AppReferences.isLine_2");
                            //                                AppReferences.printLog("CallScreen", "change host--->2nd line ", "DEBUG", null);
                            //                                if (participantBeanList_secondcall != null && AppReferences.conferenceURI_Line2 != null) {
                            //                                    changehostbtnclicked = true;
                            //                                    Log.i("participant", "second call participantBeanList not null");
                            //                                    Log.d("changehost", "inside if "
                            //                                            + participantBeanList_secondcall);
                            //                                    for (ParticipantBean pb : participantBeanList_secondcall) {
                            //                                        Log.d("changehost",
                            //                                                "second call inside if " + cb.getToNnumber());
                            //                                        Log.d("changehost",
                            //                                                "second call inside if " + pb.getSipEndPoint());
                            //                                        if (pb.getSipEndPoint() != null) {
                            //                                            if (pb.getSipEndPoint().equalsIgnoreCase(
                            //                                                    cb.getToNnumber())) {
                            //                                                Log.d("changehost",
                            //                                                        "second call inside if calling ws");
                            //                                                callConferenceCreateWS(pb);
                            //                                                newhost = cb.getToNnumber();
                            //                                                Log.i("CH", "second call newhost--->" + newhost);
                            //                                                transferCallId = cb.getCall_id();
                            //                                                Log.i("CH", "second transferCallId--->" + transferCallId);
                            //                                                Log.d("changehost",
                            //                                                        "new host " + cb.getUserName());
                            //                                                ArrayList<Integer> holdCallIds = new ArrayList<Integer>();
                            //                                                if(callerBean_Array_secondcall != null) {
                            //                                                    for (int i = 0; i < callerBean_Array_secondcall.size(); i++) {
                            //                                                        CallerBean calBean = callerBean_Array_secondcall.get(i);
                            //                                                        if (calBean.getCall_id() != -1) {
                            //                                                            holdCallIds.add(calBean.getCall_id());
                            //                                                        }
                            //                                                    }
                            //                                                }
                            //                                                if (holdCallIds.size() > 0) {
                            //                                                    int[] holdCallidsArray = new int[holdCallIds.size()];
                            //                                                    for (int k = 0; k < holdCallIds.size(); k++) {
                            //                                                        holdCallidsArray[k] = holdCallIds.get(k);
                            //                                                    }
                            //                                                    CommunicationBean h_bean = new CommunicationBean();
                            //                                                    h_bean.setOperationType(sip_operation_types.HOLD_INDIVIDUAL);
                            //                                                    h_bean.setCall_ids(holdCallidsArray);
                            //                                                    AppReferences.sipQueue.addMsg(h_bean);
                            //
                            //                                                    CommunicationBean ho_bean = new CommunicationBean();
                            //                                                    ho_bean.setCall_ids(holdCallidsArray);
                            //                                                    ho_bean.setOperationType(sip_operation_types.HOLD_OWN);
                            //                                                    AppReferences.sipQueue
                            //                                                            .addMsg(ho_bean);
                            //                                                }
                            ////                                                CommunicationBean h_bean = new CommunicationBean();
                            ////                                                h_bean.setOperationType(sip_operation_types.HOLD_SESSION);
                            ////                                                h_bean.setCall_id(cb.getCall_id());
                            ////                                                AppReferences.sipQueue.addMsg(h_bean);
                            //
                            ////                                                CommunicationBean ho_bean = new CommunicationBean();
                            ////                                                ho_bean.setOperationType(sip_operation_types.PLAY_HOLDTONE);
                            ////                                                AppReferences.sipQueue
                            ////                                                        .addMsg(ho_bean);
                            //                                                break;
                            //                                            }
                            //                                        }
                            //                                    }
                            //                                    if (keepaliveScheduler_Line2 != null)
                            //                                        keepaliveScheduler_Line2.cancel();
                            //
                            //                                    chdialog.dismiss();
                            //
                            //                                } else {
                            //                                    AppReferences.printLog("CallScreen", getResources().getString(R.string.Change_Host_request_cannot_be_processed), "DEBUG", null);
                            //                                    changehostbtnclicked = false;
                            //                                    chdialog.dismiss();
                            //                                    cancelDialog();
                            ////                                    Toast.makeText(
                            ////                                            context,
                            ////                                            getResources().getString(R.string.Change_Host_request_cannot_be_processed),
                            ////                                            Toast.LENGTH_SHORT).show();
                            //                                    showToast(getResources().getString(R.string.Change_Host_request_cannot_be_processed));
                            //
                            //                                }
                            //                            }

                            chdialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });

            }
        });

        //        chdialog.setContentView(view);

        chdialog.show();

    }


    public void notifyOnpager(String xml) {
        if (xml.contains("changehost_response")) {
            Log.i("changehost", "changehost_response xml came: newhost :=> " + newhost);
            if (user_details.size() == 2) {
                for (CallerDetailsBean cbBean : user_details) {
                    if (cbBean.getUser_name().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        cbBean.setStatus("Connected");
                    } else {
                        cbBean.setStatus("Coordinator");
                    }
                }
                //                CallerDetailsBean cbBean = new CallerDetailsBean();
                //                cbBean.setUser_name(Appreference.loginuserdetails.getUsername());
                //                cbBean.setRemote_uri("sip:" + Appreference.loginuserdetails.getUsername() + "@" + getResources().getString(R.string.server_ip));
                //                cbBean.setStatus("Connected");
                //                user_details.add(cbBean);
                changeCondition(true);
            } else {
                for (CallerDetailsBean callerDetailsBean : user_details) {
                    Log.i("changehost", "callerDetailsBean.getRemote_uri()" + callerDetailsBean.getRemote_uri());
                    if (!callerDetailsBean.getRemote_uri().equalsIgnoreCase(newhost)) {
                        //                    MyCall call = new MyCall(MainActivity.account, -1);
                        CallOpParam prm = new CallOpParam(true);
                        //                    SipTxOption sipTxOption=new SipTxOption();
                        //                    sipTxOption.setTargetUri("<"+callerDetailsBean.getRemote_uri()+">");
                        //                    prm.setTxOption(sipTxOption);
                        //            CallSetting opt = prm.getOpt();
                        //            opt.setAudioCount(1);
                        //            if (MainActivity.isAudioCall) {
                        //                opt.setVideoCount(0);
                        //                MainActivity.isAudioCall = true;
                        //            } else {
                        //                opt.setVideoCount(1);
                        //                MainActivity.isAudioCall = false;
                        //            }
                        try {
                            Log.i("changehost", "MainActivity.currentCallArrayList.get(0) :" + MainActivity.currentCallArrayList.get(0) + callerDetailsBean.getRemote_uri());
                            MyCall call = MainActivity.currentCallArrayList.get(0);
                            call.xfer(callerDetailsBean.getRemote_uri(), prm);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } else if (xml.contains("changehost_request") || xml.contains("callpause_request")) {
            String message = xml.substring(xml
                            .lastIndexOf("<?xml version=\"1.0\"?>"
                                    + "<protostart>"),
                    xml.lastIndexOf("</protostart>")
                            + "</protostart>".length());
            Log.i("changehost", "message : " + message);
            xmlparser xmlParser = new xmlparser();
            ArrayList<String[]> message_list = xmlParser
                    .parseCallMessages(message);
            ArrayList<CallerDetailsBean> call_details = new ArrayList<CallerDetailsBean>();
            String requesttype = null;
            String changehost_confUri = "";
            String[] calldetails_info = new String[5];
            for (int i = 0; i < message_list.size(); i++) {
                String[] details = message_list.get(i);
                if (i == 0) {
                    CallerDetailsBean cbBean = new CallerDetailsBean();
//                    cbBean.setUser_name(details[0]);
                    cbBean.setUser_name(details[1]);
                    cbBean.setRemote_uri("sip:" + details[1] + "@" + getResources().getString(R.string.server_ip));
                    if (details.length > 3)
                        requesttype = details[4];
                    cbBean.setStatus("Coordinator");
                    oldhost = cbBean.getRemote_uri();
                    call_details.add(cbBean);
                } else {
                    if (details[0].equals("Callinfo")) {
                        changehost_confUri = details[1];
                        Appreference.conference_uri = changehost_confUri;
                        //
                        calldetails_info[0] = details[1];
                        calldetails_info[1] = details[2];
                        calldetails_info[2] = details[3];
                        calldetails_info[3] = details[4];
                        newhost = details[4];
                        Appreference.new_host = newhost;
                        calldetails_info[4] = details[5];
                        Appreference.call_session_id = details[5];
                        //
                        //                    if (wisperConfURI.equals(AppReferences.conferenceURI_Line1)) {
                        //                        activeParticipantWisper_line1 = false;
                        //                    } else {
                        //                        activeParticipantWisper_line2 = false;
                        //                    }
                    } else if (details[0].equals("pwhishper")) {

                    } else {
                        CallerDetailsBean cbBean = new CallerDetailsBean();
//                        cbBean.setUser_name(details[0]);
                        cbBean.setUser_name(details[1]);
                        cbBean.setRemote_uri("sip:" + details[1] + "@" + getResources().getString(R.string.server_ip));
                        cbBean.setStatus(details[2]);
                        call_details.add(cbBean);
                    }
                }
            }
            Log.i("changehost", "receive request 1  ");
            int callercount = 0;
            for (CallerDetailsBean cb : call_details) {
                Log.i("changehost", "receive request 1  cb.getStatus() :" + cb.getStatus());
                if (cb.getStatus()
                        .equalsIgnoreCase("Connected") || cb.getStatus().equalsIgnoreCase("Coordinator")) {
                    callercount++;
                    //  cBean1 = cb;
                }
                //                if (cb.getToNnumber() != null && cb.getToNnumber().equals(AppReferences.user_name)) {
                //                    callercount = callercount - 1;
                //                }
            }
            Log.i("changehost", "receive request 2  ");
            for (CallerDetailsBean bean : call_details) {

                if (!bean.getStatus()
                        .equalsIgnoreCase("call disconnected") && !bean.getStatus().equalsIgnoreCase("Unavailable") && !bean.getStatus().equalsIgnoreCase("User Busy") && !bean.getStatus().equalsIgnoreCase("Call Ended") && !bean.getStatus().equalsIgnoreCase("Offline")) {
                    if (callercount > 2) {
                        bean.setStatus("Connecting");
                    } else {
                        bean.setStatus("Connected");
                    }
                    if (bean.getUser_name().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        bean.setStatus("Coordinator");
                    }
                    //                    if(bean.getToNnumber().equals(oldhost)){
                    //                        Log.i("changehost","Set CallId for oldHost "+CallId);
                    //                        bean.setCall_id(CallId);
                    //                     }
                }
            }
            Log.i("changehost", "receive request 3 ");
            if (requesttype != null) {
                if (requesttype.equals("changehost_request")) {
                    Appreference.changehost_request_received = true;
                    String[] cdetails = new String[2];
                    cdetails[0] = calldetails_info[0];
                    cdetails[1] = calldetails_info[4];
                    String changehost_responsexml = xmlComposer.composeChangeHostResponseXml(cdetails);
                    Log.i("changehost", "changehost_responsexml : " + changehost_responsexml);
                    for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
                        String name = MainActivity.account.buddyList.get(i).cfg.getUri();
//                        String host = "sip:" + oldhost + "@" + getResources().getString(R.string.server_ip);
//                        Log.i("changehost"," name : "+name+" host : "+host);
                        if (oldhost.equalsIgnoreCase(name)) {
                            Log.i("changehost","name Equals");
                            MyBuddy myBuddy = MainActivity.account.buddyList.get(i);
                            SendInstantMessageParam prm = new SendInstantMessageParam();
                            prm.setContent(changehost_responsexml);
                            boolean valid = myBuddy.isValid();
                            try {
                                myBuddy.sendInstantMessage(prm);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    user_details.clear();
                    user_details.addAll(call_details);
                    ui_handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callerAdapter.notifyDataSetChanged();
                        }
                    });

                    changeCondition(false);
                } else if (requesttype
                        .equals("callpause_request")) {
                    Appreference.call_pause_received = true;
                }
            }
        } else if (xml.contains("dashboardrequest")) {
            Log.i("dashboard", "NotifyOnpager dashboardrequest ");
            String message = xml.substring(
                    xml.lastIndexOf("<?xml version=\"1.0\"?>"
                            + "<protostart>"),
                    xml.lastIndexOf("</protostart>")
                            + "</protostart>".length());
            Log.i("changehost", "dashboard message : " + message);
            ArrayList<String[]> message_list = xmlParser
                    .parseCallMessages(message);
            ArrayList<CallerDetailsBean> call_details = new ArrayList<CallerDetailsBean>();

            for (int i = 0; i < message_list.size(); i++) {
                String[] details = message_list.get(i);
                if (details != null) {
                    if (details[0].equals("Callinfo")) {
                        //                        wisperConfURI = details[1];
                        //                        Log.i("wisper", "Callinfo : confURI" + details[1]);
                        //                        if (wisperConfURI.equals(AppReferences.conferenceURI_Line1)) {
                        //                            activeParticipantWisper_line1 = false;
                        //                        } else {
                        //                            activeParticipantWisper_line2 = false;
                        //                        }
                        //                        call_strtime=details[2];
                    }
                    //                    else if (details[0].equals("pwhishper")) {
                    //                        pwishper = true;
                    //                        fromname = details[1];
                    //                        toNomes = details[2];
                    //                        Log.i("wisper", "pwhishper");
                    //
                    //                        List<String> tonameslist = Arrays.asList(toNomes.split(","));
                    //                        totallist.addAll(tonameslist);
                    //                        totallist.add(fromname);
                    //
                    //                        if (wisperUsers.containsKey(fromname)) {
                    //                            stringArrayList = wisperUsers.get(fromname);
                    //                            wisperUsers.remove(fromname);
                    //                        }
                    //                        if (stringArrayList.size() > 0) {
                    //                            stringArrayList.clear();
                    //                        }
                    //
                    //                        stringArrayList.add(fromname);
                    //                        stringArrayList.addAll(tonameslist);
                    //                        Log.i("wisper", "stringArrayList.size :" + stringArrayList.size());
                    //                        if (wisperConfURI.equals(AppReferences.conferenceURI_Line1)) {
                    //                            if (tonameslist.contains(AppReferences.user_name)) {
                    //                                activeParticipantWisper_line1 = true;
                    //                            }
                    //                        } else {
                    //                            if (tonameslist.contains(AppReferences.user_name)) {
                    //                                activeParticipantWisper_line2 = true;
                    //                            }
                    //                        }
                    //
                    //                        if (stringArrayList.size() > 0) {
                    //                            wisperUsers.put(fromname, stringArrayList);
                    //                        }
                    //                    }
                    else {
                        //                        if (details[1].equals(AppReferences.user_name)) {
                        //                            Log.d("muteonuser====", details[1]);
                        //                            mic = details[4];
                        //                        }

                        //for participant
                        if (i == 0) {
                            CallerDetailsBean cbBean = new CallerDetailsBean();
                            cbBean.setUser_name(details[0]);
                            cbBean.setUser_id(details[1]);
                            cbBean.setRemote_uri("sip:" + details[1] + "@" + getResources().getString(R.string.server_ip));
                            //                            cbBean.setToNnumber(details[1]);
                            //                            cbBean.setSwnId(details[2]);
                            //                                            received_sessioId = details[3];
                            cbBean.setStatus("Coordinator");
                            //                            cbBean.setisSwnUser(1);
                            //                            if (AppReferences.isLine_1) {
                            //                                if (partiCallid_line1 != -1)
                            //                                    cbBean.setCall_id(partiCallid_line1);
                            //                            } else {
                            //                                if (partiCallid_line2 != -1)
                            //                                    cbBean.setCall_id(partiCallid_line2);
                            //                            }
                            //                            Log.i("wisper", "swap_userid :" + swap_userid + " details[1]" + details[1] + " partiCallid_line1 :" + partiCallid_line1 + " partiCallid_line2 :" + partiCallid_line2);
                            //                            if (swap_userid != null && swap_userid.equals(details[1])) {
                            //                                if (swap_callid != -1) {
                            //                                    cbBean.setCall_id(swap_callid);
                            //                                }
                            //                            }
                            //                            Log.i("wisper", "cbBean.getCall_id() :" + cbBean.getCall_id());
                            //
                            //                            cbBean.setHost(false);
                            call_details.add(cbBean);
                        } else {
                            //                                            if (!details[1].equals(AppReferences.ownDetails.getSipEndpoint())) {

                            CallerDetailsBean cbBean = new CallerDetailsBean();
                            cbBean.setUser_name(details[0]);
                            cbBean.setUser_id(details[1]);
                            cbBean.setRemote_uri("sip:" + details[1] + "@" + getResources().getString(R.string.server_ip));
                            //                            cbBean.setToNnumber(details[1]);
                            //                            if (details[1] != null && details[1].contains("ccglobal")) {
                            //                                cbBean.setisSwnUser(1);
                            //                            }
                            cbBean.setStatus(details[2]);
                            //                            cbBean.setSwnId(details[3]);
                            //                            Log.i("mailid", "mailid--->" + details[5]);
                            //                            if (details[5] != null) {
                            //                                cbBean.setSipEndpoint(details[5]);
                            //                            }
                            //                            cbBean.setHost(false);
                            call_details.add(cbBean);
                            //                                            }
                        }
                    }
                }
            }

            if (call_details != null && call_details.size() > 0) {
                user_details.clear();
                user_details.addAll(call_details);
            }
            ui_handler.post(new Runnable() {
                @Override
                public void run() {
                    callerAdapter.notifyDataSetChanged();
                }
            });

        }
    }


    private void send_dashboardrequest() {
        try {

            //                Log.i("call12", "session id is------>" + session_id);
            String[] owner = new String[7];
            //                AppReferences.printLog("CallScreen", "came to sendCallMessage :=> current_line is :=>" + current_line + " session id is------>" + session_id, "DEBUG", null);
            //                String peoplename = AppReferences.ownDetails.getPeople_firstName();
            //                if (AppReferences.ownDetails.getPeople_LastName() != null) {
            //                    peoplename += " " + AppReferences.ownDetails.getPeople_LastName();
            //                }
            //                Log.d("changehost", "peoplename " + peoplename);
            //                Log.d("changehost", "swnusername " + AppReferences.SWNUser_name);
            //                AppReferences.printLog("CallScreen", "================================================", "DEBUG", null);

            owner[0] = Appreference.loginuserdetails.getFirstName() + " " + Appreference.loginuserdetails.getLastName();
            owner[1] = MainActivity.username;
            owner[2] = "Coordinator";
            owner[3] = "";

            //                if (current_line.equals("line1")) {
            //                    AppReferences.printLog("CallScreen", "notifyConfOrganizerSetResult-=-AppReferences.isLine_1-confuri" + AppReferences.conferenceURI_Line1, "DEBUG", null);
            //                    Log.i("chinp", "notifyConfOrganizerSetResult-=-AppReferences.isLine_1-confuri" + AppReferences.conferenceURI_Line1);
            if (Appreference.conference_uri != null)
                owner[4] = Appreference.conference_uri;
            //                } else {
            //                    AppReferences.printLog("CallScreen", "notifyConfOrganizerSetResult-=-AppReferences.IsSecondCallActive-conf uri" + AppReferences.conferenceURI_Line2, "DEBUG", null);
            //                    Log.i("chinp", "notifyConfOrganizerSetResult-=-AppReferences.IsSecondCallActive-conf uri" + AppReferences.conferenceURI_Line2);
            //                    owner[4] = AppReferences.conferenceURI_Line2;
            //                }

            if (Appreference.callStart_time != null)
                owner[5] = Appreference.callStart_time;
            if(Appreference.call_session_id != null) {
                owner[6] = Appreference.call_session_id;
            } else {
                owner[6] = "";
            }            if(Appreference.call_session_id != null) {
                owner[6] = Appreference.call_session_id;
            } else {
                owner[6] = "";
            }
            //                owner[7] = "";
            //                owner[8] = "";

            ArrayList<String[]> details = new ArrayList<String[]>();
            //                if (current_line.equals("line1") && myArrayAdapter != null && callerBean_Array != null) {
            for (CallerDetailsBean cbean : user_details) {
                if (!cbean.getUser_name().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                    String[] member = new String[4];
                    member[0] = cbean.getUser_name();
                    member[1] = cbean.getUser_id();
                    member[2] = cbean.getStatus();
                    //                        member[3] = cbean.getSwnId();
                    //                        member[4] = String.valueOf(cbean.getMute());
                    //                        if (cbean.getSipEndpoint() != null && cbean.getSipEndpoint().length() > 0) {
                    //                            Log.i("mailid", "if mailid--->" + cbean.getMail_id());
                    //                            member[5] = cbean.getSipEndpoint();
                    //                        } else {
                    //
                    //                            member[5] = "";
                    //                        }
                    //                        AppReferences.printLog("CallScreen", "user presence----->" + cbean.getPresence(), "DEBUG", null);

                    Log.d("xml", "user presence----->" + cbean.getPresence());
                    Log.d("xml", "user presence----->" + member[2]);
                    details.add(member);
                }
            }
            //                } else {
            //                    if (mySecondArrayAdapter != null) {
            //                        for (CallerBean cbean : mySecondArrayAdapter.getAllItems()) {
            //                            String[] member = new String[6];
            //                            member[0] = cbean.getUserName();
            //                            member[1] = cbean.getToNnumber();
            //                            member[2] = cbean.getPresence();
            //                            member[3] = cbean.getSwnId();
            //                            member[4] = String.valueOf(cbean.getMute());
            //
            //                            if (cbean.getSipEndpoint() != null && cbean.getSipEndpoint().length() > 0) {
            //                                member[5] = cbean.getSipEndpoint();
            //                            } else {
            //                                member[5] = "";
            //                            }
            //                            AppReferences.printLog("CallScreen", "adapter2 user presence----->" + cbean.getPresence(), "DEBUG", null);
            //                            Log.d("xml", "user presence----->" + cbean.getPresence());
            //                            Log.d("xml", "user presence----->" + member[2]);
            //                            details.add(member);
            //                        }
            //                    }
            //
            //                }

            //                HashMap<String, ArrayList<String>> listHashMap = new HashMap<String, ArrayList<String>>();
            //
            //                if (current_line.equals("line1") && AppReferences.conferenceURI_Line1 != null) {
            //                    if (totalWhisperUsers_Detail != null && totalWhisperUsers_Detail.containsKey(AppReferences.conferenceURI_Line1)) {
            //                        listHashMap = totalWhisperUsers_Detail.get(AppReferences.conferenceURI_Line1);
            //                    }
            //                } else {
            //                    if (AppReferences.conferenceURI_Line2 != null) {
            //                        if (totalWhisperUsers_Detail != null && totalWhisperUsers_Detail.containsKey(AppReferences.conferenceURI_Line2)) {
            //                            listHashMap = totalWhisperUsers_Detail.get(AppReferences.conferenceURI_Line2);
            //                        }
            //                    }
            //                }


            final String composedMessage = xmlComposer
                    .dashboardRequestXml(owner, details, "dashboardrequest");
            Log.e("xml", "composed xml===?" + composedMessage);
            Log.e("CallActivity", "composed xml===?" + composedMessage);

            ui_handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    //                        if (current_line.equals("line1") && myArrayAdapter != null) {

                    for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
                        String name = MainActivity.account.buddyList.get(i).cfg.getUri();
                        Log.i("dashboard", "buddyname-->" + name);
                        for (CallerDetailsBean detailsBean : user_details) {
                            if (detailsBean.getRemote_uri().equalsIgnoreCase(name)) {
                                Log.i("dashboard", "both users are same");
                                if (detailsBean.getStatus().equalsIgnoreCase("connected")) {
                                    MyBuddy myBuddy = MainActivity.account.buddyList.get(i);
                                    SendInstantMessageParam prm = new SendInstantMessageParam();
                                    prm.setContent(composedMessage);
                                    boolean valid = myBuddy.isValid();
                                    Log.i("dashboard", "valid ======= " + valid);
                                    try {
                                        myBuddy.sendInstantMessage(prm);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                    //                            for (CallerDetailsBean cl_bean : user_details) {
                    //
                    //                                if (cl_bean.getStatus().equals("connected")) {
                    //                                    if (cl_bean.getCall_id() != -1 && cl_bean.getToNnumber() != null && cl_bean.getToNnumber().contains("ccglobal")) {
                    //                                        CommunicationBean bean = new CommunicationBean();
                    //                                        bean.setCall_id(cl_bean.getCall_id());
                    //                                        bean.setOperationType(sip_operation_types.SEND_KEEPALIVERESPONSE);
                    //                                        bean.setXml(composedMessage);
                    //                                        AppReferences.sipQueue.addMsg(bean);
                    //                                    }
                    //                                }
                    //
                    //                            }
                    //                        } else {
                    //                            if (mySecondArrayAdapter != null) {
                    //                                for (CallerBean cl_bean : mySecondArrayAdapter
                    //                                        .getAllItems()) {
                    //
                    //                                    if (cl_bean.getPresence().equals("Connected")) {
                    //                                        if (cl_bean.getCall_id() != -1 && cl_bean.getToNnumber() != null && cl_bean.getToNnumber().contains("ccglobal")) {
                    //                                            CommunicationBean bean = new CommunicationBean();
                    //                                            bean.setCall_id(cl_bean.getCall_id());
                    //                                            bean.setOperationType(sip_operation_types.SEND_KEEPALIVERESPONSE);
                    //                                            bean.setXml(composedMessage);
                    //                                            AppReferences.sipQueue.addMsg(bean);
                    //                                        }
                    //                                    }
                    //
                    //                                }
                    //                            }
                    //
                    //                        }
                }
            }, 1000);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public void insertdb() {

        try {
            Log.d("callhistory","CallActivity insertdb");
            String hostName = null;
            String participants = null;
            for (CallerDetailsBean callerDetailsBean : user_details) {
                //            String uri=ci.getRemoteUri().substring(1,ci.getRemoteUri().length()-1);
                //            Log.i("calldisconnect","uri--->"+uri);
                Log.i("calldisconnect", "Appreference.loginuserdetails.getUsername() :" + Appreference.loginuserdetails.getUsername() + " userlist uri-->" + callerDetailsBean.getRemote_uri());
                Log.i("calldisconnect", "status -->" + callerDetailsBean.getStatus());
                //            if(callerDetailsBean.getRemote_uri().equalsIgnoreCase(uri)){
//                if (!callerDetailsBean.getRemote_uri().equalsIgnoreCase("sip:" + Appreference.loginuserdetails.getUsername() + "@" + getResources().getString(R.string.server_ip))) {
                    if (callerDetailsBean.getStatus().equalsIgnoreCase("Coordinator") || callerDetailsBean.getStatus().equalsIgnoreCase("Incoming call..")) {
                        hostName = callerDetailsBean.getRemote_uri();
                    } else {
                        if (participants == null) {
                            participants = callerDetailsBean.getRemote_uri();
                        } else {
                            participants = participants + "," + callerDetailsBean.getRemote_uri();
                        }

                    }
//                }
            }

//        CallerDetailsBean callerDetailsBean = new CallerDetailsBean();
            Call_ListBean call_listBean = new Call_ListBean();
//        Log.i("Bean", "Value" + callerDetailsBean.getRemote_uri());


            if (incomingcall) {
                call_listBean.setType("IncomingCall");
                Log.d("callhistory", "hostName : "+hostName+" host : "+host);
                if (hostName != null) {
                    String part = hostName;
                    part = part.substring(4);
                    String part1[] = part.split("@");
                    part = VideoCallDataBase.getDB(context).getName(part1[0]);
                    Log.d("callhistory", "1 hostName : "+part);
                    call_listBean.setHost(part);
                } else {
                    if(host != null) {
                        call_listBean.setHost(host);
                    }
                }
                String result = null;
                if (participants != null) {
                    if (participants.contains(",")) {
                        Log.i("callhistory", "Value" + participants);
                        String data = participants;
                        String[] items = data.split(",");
                        for (String item : items) {
                            data = item.substring(4);
                            String part1[] = data.split("@");
                            Log.i("callhistory","part1[0] : "+part1[0]);
                            Log.i("callhistory","login user : "+ Appreference.loginuserdetails.getUsername());

                            if(part1[0].equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                data = Appreference.loginuserdetails.getFirstName() + " " + Appreference.loginuserdetails.getLastName();
                            } else {
                                data = VideoCallDataBase.getDB(context).getName(part1[0]);
                            }
                            Log.i("callhistory", "item" + item+" data :"+data);
                            if (result != null) {
                                result = result + "," + data;
                            } else {
                                result = data;
                            }
                        }
                        Log.d("callhistory", "enter income if");
                        call_listBean.setParticipant(result);
                    } else {
                        String part = participants;
                        part = part.substring(5);
                        String part1[] = part.split("@");
                        part = VideoCallDataBase.getDB(context).getName(part1[0]);
                        hostName = Appreference.loginuserdetails.getFirstName() + " " + Appreference.loginuserdetails.getLastName();
                        Log.d("callhistory", "enter income else");
//                        call_listBean.setHost(part);
                        call_listBean.setParticipant(hostName);
                        Log.i("callhistory", "HOst : " + part);
                        Log.i("callhistory", "Participants : " + hostName);
                        Log.i("callhistory", "Value 1 " + participants);
                    }
                } else {
                    String par_name  = Appreference.loginuserdetails.getFirstName() + " " + Appreference.loginuserdetails.getLastName();
                    call_listBean.setParticipant(par_name);
                }
            } else {
                call_listBean.setType("OutgoingCall");

                //
                if (participants != null) {
                    String result1 = null;
                    if (participants.contains(",")) {
                        String data = participants;
                        String[] items = data.split(",");
                        for (String item : items) {
                            data = item.substring(4);
                            String part1[] = data.split("@");
                            data = VideoCallDataBase.getDB(context).getName(part1[0]);
                            Log.i("callhistory", "Value" + participants);
                            if (result1 != null) {
                                result1 = result1 + "," + data;
                            } else {
                                result1 = data;
                            }
                        }
                        Log.d("calling", "enter outgoing if");
                        call_listBean.setParticipant(result1);
                    } else {
                        String part = participants;
                        part = part.substring(4);
                        String part1[] = part.split("@");
                        part = VideoCallDataBase.getDB(context).getName(part1[0]);
                        call_listBean.setParticipant(part);
                        Log.d("calling", "enter outgoing else");
                        Log.i("callhistory", "Value" + participants);
                    }
                }
                if (Appreference.loginuserdetails.getFirstName() != null && Appreference.loginuserdetails.getLastName() != null) {
                    call_listBean.setHost(Appreference.loginuserdetails.getFirstName() + " " + Appreference.loginuserdetails.getLastName());
                } else if (Appreference.loginuserdetails.getUsername() != null) {
                    call_listBean.setHost(Appreference.loginuserdetails.getUsername());
                }
            }
       /* if(!MainActivity.isAudioCall) {
            call_listBean.setCall_duration(String.valueOf(chronometer_land.getText()));
        }else */
//            {
                call_listBean.setCall_duration(String.valueOf(chronometer.getText()));
//            }
            call_listBean.setStart_time(call_strtime);
            Log.i("insertdb", "value" + call_strtime);
            VideoCallDataBase videoCallDataBase = new VideoCallDataBase(context);
            call_listBean.setRecording_path(recorded_path);
            videoCallDataBase.insertCall_history(call_listBean, Appreference.loginuserdetails.getUsername());
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeCondition(boolean incoming_state) {
        try {
            Log.i("changehost", "came to changeCondition :where incoming_state :=> " + incoming_state);
            incomingcall = incoming_state;
            handler.post(new Runnable() {
                @Override
                public void run() {

                    if (incomingcall) {

                        participant_layout.setVisibility(View.VISIBLE);
                        host_layout.setVisibility(View.GONE);
                        //                        hang_up_audio_call = (ImageView)participant_layout. findViewById(R.id.participant_buttonHangup);
                        //                        speaker = (ImageView)participant_layout. findViewById(R.id.participant_speaker);
                        //                        rl_mic_mute = (RelativeLayout)participant_layout. findViewById(R.id.participant_mic_mute);
                        //                        micmute = (ImageView) findViewById(R.id.participant_expand_iv);
                        rl_co_ordinate.setVisibility(View.GONE);
                        rl_mic_mute.setVisibility(View.GONE);
                        micmute.setVisibility(View.GONE);
                        setHost(false);
                        Log.d("CheckCall","Inside participant 1");
                        //                        if(!MainActivity.isAudioCall) {
                        //
                        //
                        //                            if (speaker.getTag().equals(0)) {
                        //                                if (am != null) {
                        //                                    speaker.setTag(1);
                        //                                    speaker.setImageResource(R.drawable.speaker);
                        //                                    am.setSpeakerphoneOn(true);
                        //                                    Log.d("CheckCall","Inside participant 3");
                        //                                }
                        //                            }
                        //                        }

                    } else {
                        rl_co_ordinate.setVisibility(View.GONE);
                        participant_layout.setVisibility(View.GONE);
                        host_layout.setVisibility(View.VISIBLE);
                        Log.d("CheckCall","Inside host 1");
                        //                        hang_up_audio_call = (ImageView)host_layout.findViewById(R.id.buttonHangup);
                        //                        speaker = (ImageView)host_layout. findViewById(R.id.speaker);
                        //                        rl_mic_mute = (RelativeLayout)host_layout.findViewById(R.id.mic_mute);
                        //                        micmute = (ImageView) findViewById(R.id.expand_iv);
                        rl_mic_mute.setVisibility(View.VISIBLE);
                        micmute.setVisibility(View.VISIBLE);
                        setHost(true);
						   /* if(!MainActivity.isAudioCall) {

								if (host_speaker.getTag().equals(0)) {
									if (am != null) {
										host_speaker.setTag(1);
										host_speaker.setImageResource(R.drawable.speaker);
										am.setSpeakerphoneOn(true);
										Log.d("CheckCall","Inside host 3");
									}
								}
							}*/

                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TimerTask callTimerTaskObj = new TimerTask() {
        @Override
        public void run() {
            call_auto_end = false;
        }
    };

  /* public TimerTask timerTaskObj = new TimerTask() {
        public void run() {
            //perform your action here

            try {
                Log.d("RxLevel", "Timer task started" );
                if(lastCallInfo != null) {
                    Log.d("RxLevel", "Timer task started value != null" );
                    CallMediaInfoVector mediaInfoVector = lastCallInfo.getMedia();

                    for (int i = 0; i < mediaInfoVector.size(); i++) {
                        CallMediaInfo cmi = mediaInfoVector.get(i);
                        cmi.getAudioConfSlot();

                        Log.d("RxLevel", "Timer task started cmi  === > "+cmi +"     getAudioConfSlot value is == "+cmi.getAudioConfSlot() );

                        for (int j = 0; j < MainActivity.currentCallArrayList.size(); j++) {

                            Log.d("RxLevel", "currentCallArrayList size is  === > "+MainActivity.currentCallArrayList.size());
                            Log.d("RxLevel", "MainActivity.currentCallArrayList.get(j).getInfo().getId()  === > "+MainActivity.currentCallArrayList.get(j).getInfo().getId());
                            Log.d("RxLevel", "lastCallInfo.getId()  === > "+lastCallInfo.getId());
                            if (MainActivity.currentCallArrayList.get(j).getInfo().getId() == lastCallInfo.getId()) {
                                MyCall myCall = MainActivity.currentCallArrayList.get(j);

                                Media m = myCall.getMedia(i);
                                AudioMedia am = AudioMedia.typecastFromMedia(m);
                                Log.d("RxLevel", "RxLevel count is===>" + am.getRxLevel());

                                if (am.getRxLevel() <= 0) {
                                    Log.d("RxLevel", "RxLevel count increment is===>" + rxCount++);
                                    rxCount++;
                                } else {
                                    Log.d("RxLevel", "RxLevel count increment else part===>" + rxCount++);
                                    rxCount = 0;
                                }

                                if (rxCount >= 3) {

                                } else if (rxCount == 8) {
                                    CallOpParam prm = new CallOpParam();
                                    prm.setStatusCode(pjsip_status_code.PJSIP_SC_DECLINE);
                                    myCall.hangup(prm);
                                }
                            }
                        }


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };*/
  private Runnable runnable = new Runnable() {
      @Override
      public void run() {
      /* my set of codes for repeated work */
          for (int j = 0; j < MainActivity.currentCallArrayList.size(); j++) {

              Log.d("RxLevel", "currentCallArrayList size is  === > " + MainActivity.currentCallArrayList.size());
              try {
                  MyCall myCall = MainActivity.currentCallArrayList.get(j);
                  Log.d("RxLevel", "1 currentCallArrayList ");
                  CallInfo ci = myCall.getInfo();
                  Log.d("RxLevel", "2 currentCallArrayList ");
                  if (ci.getState().swigValue() == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED.swigValue()) {
                      CallMediaInfoVector cmiv = ci.getMedia();


                      for (int i = 0; i < cmiv.size(); i++) {
                          CallMediaInfo cmi = cmiv.get(i);
                          if (cmi.getType() == pjmedia_type.PJMEDIA_TYPE_AUDIO &&
                                  (cmi.getStatus() ==
                                          pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE ||
                                          cmi.getStatus() ==
                                                  pjsua_call_media_status.PJSUA_CALL_MEDIA_REMOTE_HOLD)) {


                              Media m = myCall.getMedia(i);
                              AudioMedia am = AudioMedia.typecastFromMedia(m);
                              Log.d("RxLevel", "RxLevel count is===>" + am.getRxLevel());

                              if (am.getRxLevel() <= 0) {
                                  rxCount++;
                                  Log.d("RxLevel", "RxLevel count increment is===>" + rxCount);
                              } else {
                                  rxCount = 0;
                                  Log.d("RxLevel", "RxLevel count increment else part===>" + rxCount);
                              }

                              if (rxCount >= 3) {

                              } else if (rxCount == 8) {
                                  CallOpParam prm = new CallOpParam();
                                  prm.setStatusCode(pjsip_status_code.PJSIP_SC_DECLINE);
                                  myCall.hangup(prm);
                              }
                          }
                      }
                  }
              } catch (Exception e) {
                  e.printStackTrace();
              }
          }
          handler.postDelayed(this, 15000);          // reschedule the handler
      }
  };

    public class CheckRxLevelTimerTask extends TimerTask {

        @Override
        public void run() {
//            new Handler().post(new Runnable() {
//                @Override
//                public void run() {

                    for (int j = 0; j < MainActivity.currentCallArrayList.size(); j++) {

                        Log.d("RxLevel", "currentCallArrayList size is  === > " + MainActivity.currentCallArrayList.size());
                        try {
                            MyCall myCall = MainActivity.currentCallArrayList.get(j);
                            Log.d("RxLevel", "1 currentCallArrayList ");
                            CallInfo ci = myCall.getInfo();
                            Log.d("RxLevel", "2 currentCallArrayList ");
                            if (ci.getState().swigValue() == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED.swigValue()) {
                                CallMediaInfoVector cmiv = ci.getMedia();


                                for (int i = 0; i < cmiv.size(); i++) {
                                    CallMediaInfo cmi = cmiv.get(i);
                                    if (cmi.getType() == pjmedia_type.PJMEDIA_TYPE_AUDIO &&
                                            (cmi.getStatus() ==
                                                    pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE ||
                                                    cmi.getStatus() ==
                                                            pjsua_call_media_status.PJSUA_CALL_MEDIA_REMOTE_HOLD)) {


                                        Media m = myCall.getMedia(i);
                                        AudioMedia am = AudioMedia.typecastFromMedia(m);
                                        Log.d("RxLevel", "RxLevel count is===>" + am.getRxLevel());

                                        if (am.getRxLevel() <= 0) {
                                            Log.d("RxLevel", "RxLevel count increment is===>" + rxCount++);
                                            rxCount++;
                                        } else {
                                            Log.d("RxLevel", "RxLevel count increment else part===>" + rxCount++);
                                            rxCount = 0;
                                        }

                                        if (rxCount >= 3) {

                                        } else if (rxCount == 8) {
                                            CallOpParam prm = new CallOpParam();
                                            prm.setStatusCode(pjsip_status_code.PJSIP_SC_DECLINE);
                                            myCall.hangup(prm);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
//                }
//            });
        }
    }
}
