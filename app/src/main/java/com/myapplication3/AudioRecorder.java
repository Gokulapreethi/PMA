package com.myapplication3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import json.WebServiceInterface;

public class AudioRecorder extends Activity implements WebServiceInterface {

    private ImageButton record, play, /*save_audio,*/
            send;
    TextView stop, heading;
    Button cancel, submit;
    public AudioManager audioManager;
    public static MediaPlayer m = null;
    private Chronometer cm;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    boolean save = false;
    private boolean chronometer_started = false;
    private SeekBar progress;
    private Context context = null;
    private Runnable notification = null;
    private Handler handlerSeek = new Handler();
    private MediaRecorder myAudioRecorder;
    private AudioRecord recorder = null;
    boolean isRecording = false, isPlaying = false;
    private int bufferSize = 0;
    private String outputFile = null;
    private static final String AUDIO_RECORDER_TEMP_FILE = "record_temp.mp3";
    private RelativeLayout audio_layout;
    private double timeElapsed = 0;
    private Handler durationHandler = new Handler();
    private static int[] mSampleRates = new int[]{8000, 11025, 22050, 44100};

    //    public static Vector<MediaListBean> mediaList;
    public static MediaListAdapter medialistadapter;
    MediaRecorder mRecorder;
    ImageButton rc_stop;
    boolean isdateset = false, custom = false;
    static AudioRecorder audioRecorder;

    boolean playtimer = false;
    private Handler mHandler = new Handler();
    private PlaybackUpdater mProgressUpdater = new PlaybackUpdater();
    private boolean isPlayCompleted = true;

    public static AudioRecorder getInstance() {
        return audioRecorder;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.audio_record_activity);
        heading = (TextView) findViewById(R.id.txtView01);
        cancel = (Button) findViewById(R.id.cancel);
        submit = (Button) findViewById(R.id.submit);
        cm = (Chronometer) findViewById(R.id.chronometer1);
//        save_audio = (ImageButton) findViewById(R.id.save_audio);
//        rc_stop=(ImageButton)findViewById(R.id.btn_stop);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        send=(ImageButton)findViewById(R.id.)

        bufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        progress = (SeekBar) findViewById(R.id.audio_seekbar);

        progress.setClickable(true);
//        progress.setMax(m.getDuration());

        audioRecorder = this;
        if (getIntent() != null) {
            if (getIntent().getExtras().getString("task") != null && getIntent().getExtras().getString("task").equalsIgnoreCase("date")) {
                isdateset = true;
                heading.setText("Reminder Tone");
            } else if (getIntent().getExtras().getString("task") != null && getIntent().getExtras().getString("task").equalsIgnoreCase("audio")) {
                isdateset = false;
            } else if (getIntent().getExtras().getString("task") != null && getIntent().getExtras().getString("task").equalsIgnoreCase("custom")) {
                isdateset = false;
                heading.setText("Custom Audio");
            }else if (getIntent().getExtras().getString("task") != null && getIntent().getExtras().getString("task").equalsIgnoreCase("chat")) {
                isdateset = false;
            } else {
                custom = true;
                isdateset = false;
                outputFile = getIntent().getExtras().getString("task");
                Log.i("task","filepath"+outputFile);
            }
        }

        play = (ImageButton) findViewById(R.id.play);
//        play.setTag(1);

        record = (ImageButton) findViewById(R.id.record);
//        record.setTag(0);

//        stop = (TextView) findViewById(R.id.setting_audiotxt);
//        stop.setTypeface(Appreference.normal_type);
//        cm.setBase(SystemClock.elapsedRealtime());
//        cm.start();
//        chronometer_started = true;
        progress.setVisibility(View.GONE);

        if (!custom) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message");
            if (!file.exists())
                file.mkdirs();
//        speaker = (ImageButton) findViewById(R.id.speaker);
            if (!audioRecorder.isRecording) {
                outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + getFileNameinAudio() + ".mp3";
                try {
                    if (mRecorder == null) {
                        mRecorder = new MediaRecorder();
                        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        mRecorder.setOutputFile(outputFile);
                        record.setBackgroundResource(R.drawable.record_red);
                        play.setVisibility(View.GONE);
                        isRecording = true;
                        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                        if (isdateset == true) {
                            mRecorder.setMaxDuration(10000);
                            mRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {

                                @Override
                                public void onInfo(MediaRecorder mr, int what, int extra) {
                                    // TODO Auto-generated method stub
                                    if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                                        // mr.stop();
                                        cm.stop();
                                        Toast.makeText(getApplicationContext(),
                                                "Duration limit reached", Toast.LENGTH_LONG)
                                                .show();
                                        mRecorder.stop();
                                        mRecorder.release();
                                        mRecorder = null;
                                        record.setVisibility(View.GONE);
                                        play.setVisibility(View.VISIBLE);
                                        progress.setVisibility(View.VISIBLE);
                                        submit.setVisibility(View.VISIBLE);
                                        submit.setText("save");
//                                        save_audio.setVisibility(View.VISIBLE);
                                        isRecording = false;
                                        if (cm != null) {
                                            cm.stop();
                                        }
                                        try {
                                            m.setDataSource(outputFile);
                                            m.setLooping(false);
                                            m.prepare();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            Appreference.printLog("AudioRecorder Oncreate","Exception "+e.getMessage(),"WARN",null);
                                        }
                                    }
                                }
                            });
                        }
                    }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    Appreference.printLog("AudioRecorder Oncreate","Exception "+e.getMessage(),"WARN",null);
                }
                try {
                    mRecorder.prepare();
                    mRecorder.start();
                    if (!chronometer_started) {
                        cm.setBase(SystemClock.elapsedRealtime());
                        cm.start();
                        chronometer_started = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("AudioRecorder Oncreate","Exception "+e.getMessage(),"WARN",null);
                }
            }
        } else {
//            save_audio.setVisibility(View.GONE);
//            send.setVisibility(View.GONE);
            record.setVisibility(View.GONE);
            play.setVisibility(View.VISIBLE);
        }
/*        save_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    Toast.makeText(getApplicationContext(), "Please Stop the Recoder", Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent();
                    i.putExtra("taskFileExt", outputFile);

                    setResult(RESULT_OK, i);
                    if (m.isPlaying()) {
                        m.stop();
                    }
                    m.release();
                    m = null;
                    Log.i("audio", "relesed");
                    finish();
                }
            }
        });*/

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appreference.webview_refresh = true;
                if (m.isPlaying()) {
                    m.stop();
                }
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isRecording) {
                        Toast.makeText(getApplicationContext(), "Please Stop the Recoder", Toast.LENGTH_LONG).show();
                    } else {
                        Intent i = new Intent();
                        i.putExtra("taskFileExt", outputFile);

                        setResult(RESULT_OK, i);
                        if (m.isPlaying()) {
                            m.stop();
                        }
                        m.release();
                        m = null;
                        Log.i("audio", "relesed");
                        finish();
                    }
                }catch (Exception e){e.printStackTrace();
                    Appreference.printLog("AudioRecorder Submit.setOnClickListener","Exception "+e.getMessage(),"WARN",null);}
            }
        });

      /*  submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent intent = getIntent();
                  *//*  String name = intent.getStringExtra("name");
                    String plannedStartDateTime = intent.getStringExtra("plannedStartDateTime");
                    String plannedEndDateTime = intent.getStringExtra("plannedEndDateTime");
                    String remainderFrequency = intent.getStringExtra("remainderFrequency");
                    String des = intent.getStringExtra("des");
                    String from = intent.getStringExtra("from");
                    String to = intent.getStringExtra("to");*//*

                    Appreference.isAudio_webservice = true;


                    JSONObject[] listpostfiles_object1 = new JSONObject[1];
                        int i = 0;
                        for (i= 0;i<=1;i++) {
                            listpostfiles_object1[i] = new JSONObject();
                            listpostfiles_object1[i].put("fileType", "audio");
                            File file = new File(outputFile);
                            String imgcontent = "";
                                listpostfiles_object1[i].put("taskFileExt", "mp3");
                                imgcontent = encodeAudioVideoToBase64(outputFile);

                            JSONObject jsonObject3 = new JSONObject();
                            jsonObject3.put("id", 1);

                            Log.i("AAA", "new post path " + outputFile);
                            listpostfiles_object1[i].put("fileContent", imgcontent);
                            listpostfiles_object1[i].put("fileSource", "task");
                            listpostfiles_object1[i].put("fileSourceId", 1);
                            listpostfiles_object1[i].put("user", jsonObject3);
                            i++;
                        }


                    JSONArray listpostfiles_object = new JSONArray();
                    for (int j = 0; j < listpostfiles_object1.length; j++) {
                        listpostfiles_object.put(listpostfiles_object1[j]);
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("name", name);
                    jsonObject.put("description", "");
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("id",from);

                    jsonObject.put("from", jsonObject1);

                    jsonObject.put("to", to);
                    jsonObject.put("taskNo", "T00003");
                    jsonObject.put("plannedStartDateTime", plannedStartDateTime);
                    jsonObject.put("plannedEndDateTime",plannedEndDateTime );
                    jsonObject.put("isRemainderRequired", "Y");
                    jsonObject.put("remainderFrequency",remainderFrequency );
                    jsonObject.put("status", "A");
                    jsonObject.put("listTaskFiles", listpostfiles_object);
                    Log.i("Task", "Task--->>>" + jsonObject);


                    Appreference.jsonRequestSender.taskEntry(EnumJsonWebservicename.taskEntry, jsonObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });*/

        m = new MediaPlayer();

/*
                stop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (stop.getText().equals("Save")) {

                                if (record.getTag().equals(1)) {
                                    record.setTag(0);
                                    if (null != recorder) {
                                        isRecording = false;

                                        recorder.stop();
                                        recorder.release();

                                        recorder = null;
                                    }

                                }

                                save = false;

                                play.setVisibility(View.VISIBLE);
                                record.setEnabled(true);
                                play.setEnabled(true);
                                SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
                                String currentDateandTime = sdf.format(new Date());
                                stop.setText(currentDateandTime);
                                record.setBackgroundResource(R.drawable.record_blue);

                                File source = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "HIGHMESSAGE/Media/Music/" + getFileNameinAudio() + ".mp3");

                                File destination = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "HIGHMESSAGE/Media/Music/" + getFileNameinAudio() + ".mp3");
                                if (source.exists()) {
                                    FileChannel src = new FileInputStream(source).getChannel();
                                    FileChannel dst = new FileOutputStream(destination).getChannel();
                                    dst.transferFrom(src, 0, src.size());
                                    src.close();
                                    dst.close();
                                    source.delete();

                                }


//                        Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_LONG).show();
                                showToast(getString(R.string.audio_recorded_successfully));
                            } else {
//                        AudioRecord();
                            }
                        } catch (Exception e) {
                            Appreference.printLog("App", "================================================", "DEBUG", null);
                            Appreference.printLog(null, e.getMessage(), null, e);
                            e.printStackTrace();
                        }
                    }
                    // }
                });
*/
        m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    play.setBackgroundResource(R.drawable.ic_playicon);

                    record.setEnabled(true);
                    isPlayCompleted = true;
                    progress.setProgress(0);
                    Log.i("valueof", "2 intvalue setOnCompletionListener");
                    if (m != null) {
                        m.reset();
                            m.setDataSource(outputFile);
                            m.prepare();

                    }

                    double tot_time = m.getDuration();
                    String hms = String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes((long) tot_time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours((long) tot_time)),
                            TimeUnit.MILLISECONDS.toSeconds((long) tot_time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) tot_time)));
                    cm.setText(hms);
                } catch (IOException e) {
                    e.printStackTrace();
                    Appreference.printLog("AudioRecorder m.setOncompletionListener","Exception "+e.getMessage(),"WARN",null);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Appreference.printLog("AudioRecorder m.setOnCompletionListener","Exception "+e.getMessage(),"WARN",null);
                }
            }
        });


        record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isRecording) {
                    stopRecording();
                    isRecording = false;
                    record.setBackgroundResource(R.drawable.record_blue);
                    record.setVisibility(View.GONE);
                    play.setVisibility(View.VISIBLE);
                    if (isdateset) {
                        submit.setText("save");
                    }
                    progress.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
//                    save_audio.setVisibility(View.VISIBLE);

                    if (cm != null) {
                        cm.stop();
                    }
                    try {
                        m.setDataSource(outputFile);
//                            }
                        m.setLooping(false);
                        m.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Appreference.printLog("AudioRecorder record.setOnClickListener","Exception "+e.getMessage(),"WARN",null);
                    }
                }
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!custom)
                        record.setEnabled(false);

                    if (outputFile != null) {
                        isPlayCompleted = false;
                        Log.d("audio", "outputFile " + outputFile);
                        Appreference.printLog("pstnrecord", "record playing", "DEBUG", null);
                        finalTime = m.getDuration();
                        startTime = m.getCurrentPosition();
                        progress.setProgress((int) startTime);
                        progress.setMax((int) finalTime);
//                            startPlayProgressUpdater();
                        if (!m.isPlaying()) {
                            Toast.makeText(getApplicationContext(), "Playing sound", Toast.LENGTH_SHORT).show();
                            int currentPosition = (progress.getProgress());
                            m.seekTo(currentPosition);
                            playtimer = false;
                            myHandler.postDelayed(UpdateSongTime, 100);
                            m.start();
                            playtimer = true;
//                            cm.start();
                            play.setBackgroundResource(R.drawable.pause_icon);
//                            myHandler.postDelayed(UpdateSongTime, 100);
                            startPlayProgressUpdater();
                        } else {

                            m.pause();
                            /*if (cm != null) {
                                cm.stop();
                            }*/
                            play.setBackgroundResource(R.drawable.ic_playicon);
//                            int currentPosition = (progress.getProgress());
//                            m.seekTo(currentPosition);
                        }
                    }
//                    else {
//                        m.reset();
//                        m.start();
//                        progress.setMax(m.getDuration());
//                        startPlayProgressUpdater();
//                    }
                } catch (Exception e) {
//                    Appreference.printLog(null, e.getMessage(), null, e);
                    e.printStackTrace();
                    Appreference.printLog("AudioRecorder play.setOnClickListener","Exception "+e.getMessage(),"WARN",null);
                }
//                Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();
            }
        });

    }


    private Runnable UpdateSongTime = new Runnable() {
        public void run() {

            if (m != null) {
                if (isPlayCompleted) {

                } else {
                    startTime = m.getCurrentPosition();
                    String hms = String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours((long) startTime)),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)));
                    cm.setText(hms);
                    Log.i("valueof", "1 intvalue" + hms);
                    progress.setProgress((int) startTime);
                    myHandler.postDelayed(this, 10);
                }
            }
        }
    };

    public String getFileNameinAudio() {
        String strFilename = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
            Date date = new Date();
            strFilename = dateFormat.format(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("AudioRecorder getFileNameinAudio","Exception "+e.getMessage(),"WARN",null);
        }
        return strFilename;
    }


    private void startPlayProgressUpdater() {
        try {
            if (m != null) {
                progress.setProgress(m.getCurrentPosition());
                Log.d("player", "?????????????????????" + m.isPlaying());
                if (m.isPlaying()) {
                    notification = new Runnable() {
                        public void run() {
                            try {
                                startPlayProgressUpdater();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("AudioRecorder startPlayProgressUpdater","Exception "+e.getMessage(),"WARN",null);
                            }
                        }
                    };
                    handlerSeek.postDelayed(notification, 100);
                } else {
                    progress.setProgress(m.getCurrentPosition());
//                m.pause();
                }
                progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                        handlerSeek.removeCallbacks(notification);
                        int currentPosition = (seekBar.getProgress());
                        m.seekTo(currentPosition);
                        startPlayProgressUpdater();
//                    m.start();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                        handlerSeek.removeCallbacks(notification);
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        // TODO Auto-generated method stub

                    }
                });
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("AudioRecorder startPlayProgressUpdater","Exception "+e.getMessage(),"WARN",null);
        }
    }

//    private Runnable updateSeekBarTime = new Runnable() {
//        public void run() {
//            timeElapsed = m.getCurrentPosition();
//            progress.setProgress((int) timeElapsed);
//            durationHandler.postDelayed(this, 100);
//        }
//    };

//    private void showToast(String msg) {
//       /* LayoutInflater inflater = getLayoutInflater();
//        Toast toast = new Toast(getApplicationContext());
//        toast.setDuration(Toast.LENGTH_SHORT);
//        toast.show();*/
//    }


    private String encodeAudioVideoToBase64(String path) {
        String strFile = null;
        File file = new File(path);
        try {
            FileInputStream file1 = new FileInputStream(file);
            byte[] Bytearray = new byte[(int) file.length()];
            file1.read(Bytearray);
            strFile = Base64.encodeToString(Bytearray, Base64.DEFAULT);//Convert byte array into string

        } catch (IOException e) {
            e.printStackTrace();
            Appreference.printLog("AudioRecorder encodeAudioVideoToBase64","Exception "+e.getMessage(),"WARN",null);
        }

        return strFile;
    }


    private void stopRecording() {
        try {
            if (mRecorder != null) {
                record.setBackgroundResource(R.drawable.record_blue);
                mRecorder.stop();
                mRecorder.release();
                mRecorder = null;
            }
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("AudioRecorder StopRecording","Exception "+e.getMessage(),"WARN",null);
        } finally {

        }
    }


    @Override
    public void ResponceMethod(final Object object) {
//        CommunicationBean bean = (CommunicationBean) object;
    }

    @Override
    public void ErrorMethod(Object object) {

    }

    public void notifypostEntryResponse(final String values) {
        Log.i("postEntry", "NewTaskactivity  notifypostEntryResponse method");
//        cancelDialog();
//        Appreference.webview_refresh = true;
        finish();

       /* if(Appreference.context_table.containsKey("contactsfragment")){
            ContactsFragment contactsFragment=(ContactsFragment)Appreference.context_table.get("contactsfragment");
            contactsFragment.replaceTaskFragment();
        }*/
        try {
            JSONObject json = new JSONObject(values);
           /* if(values.contains("result_code")) {
                int resultCode = json.getInt("result_code");
                final String text = json.getString("result_text");
                *//*handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showToast(text);
                    }
                });*//*
            }else
                showToast("Task failed");
                }
        }*/
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("AudioRecorder notifypostEntryResponse","Exception "+e.getMessage(),"WARN",null);
        }
    }

    private class PlaybackUpdater implements Runnable {

        @Override
        public void run() {
            if (playtimer) {

                double sTime = m.getCurrentPosition();
                String min, sec;
                min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes((long) sTime));
                sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds((long) sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) sTime)));
                if (Integer.parseInt(min) < 10) {
                    min = 0 + String.valueOf(min);
                }
                if (Integer.parseInt(sec) < 10) {
                    sec = 0 + String.valueOf(sec);
                }
                cm.setText(min + ":" + sec);
                int result = 100 * m.getCurrentPosition() / m.getDuration();
                Log.i("valueof", "intvalue" + result);


                mHandler.postDelayed(this, 100);

            }  //not playing so stop updating
        }

    }

//    private Runnable UpdateSongTime = new Runnable() {
//        public void run() {
//            startTime = m.getCurrentPosition();
////            tx1.setText(String.format("%d min, %d sec",
//
//                       /*     TimeUnit.MILLISECONDS.toMinutes((long) startTime),
//                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
//                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
//                                            toMinutes((long) startTime)))
//            );*/
//            progress.setProgress((int) startTime);
//            myHandler.postDelayed(this, 100);
//        }
//    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isdateset) {
            if (m != null && m.isPlaying())
                m.reset();
            playtimer = false;
        }
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
//        if (m.isPlaying()) {
//            m.stop();
//        }
        if (m != null && m.isPlaying())
            m.reset();
        playtimer = false;
    }
}

