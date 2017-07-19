package com.ase;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by prasanth on 12/29/2016.
 */
public class Audioplayer extends Activity {

    public static ImageView play_button;
    public static SeekBar seekBar;
    public TextView tvToUpdate;
    //    public static Chronometer cm;
    String filePath;
    private double startTime = 0;
    private double finalTime = 0;
    private Handler myHandler = new Handler();
    private Handler handlerSeek = new Handler();
    boolean save = false;
    private boolean chronometer_started = false;
    private SeekBar progress;
    MediaRecorder mRecorder;
    public static MediaPlayer mPlayer;
    public static Runnable updatetime = null;
    private int mPlayingPosition = -1;
    boolean playtimer = false;
    private boolean isPlayCompleted = false;

    String min, sec;
    private Handler mHandler = new Handler();
    private PlaybackUpdater mProgressUpdater = new PlaybackUpdater();

//    MediaPlayer mediaPlayer ;
//    MediaRecorder mediaRecorder ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialogforplayaudio);

        tvToUpdate = (TextView) findViewById(R.id.tvToUpdate);
        play_button = (ImageView) findViewById(R.id.play_button);
        seekBar = (SeekBar) findViewById(R.id.seekBar1);

        filePath = getIntent().getStringExtra("audio");
        Log.d("audio", "filePath for player " + filePath);

        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(filePath);
            mPlayer.prepare();

        } catch (IOException e) {
            e.printStackTrace();
            Appreference.printLog("Audioplayer oncreate","Exception "+e.getMessage(),"WARN",null);
        }

        double tot_time = mPlayer.getDuration();
        String hms = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) tot_time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours((long) tot_time)),
                TimeUnit.MILLISECONDS.toSeconds((long) tot_time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) tot_time)));
        tvToUpdate.setText(hms);

        play_button.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {

                                               Log.i("audio", "playing");
                                               Log.i("audio ", "mediapath " + filePath);

                                               isPlayCompleted = false;
                                               if (mPlayer.isPlaying()) {
                                                   try {
                                                       mPlayer.pause();
                                                   } catch (IllegalStateException e) {
                                                       e.printStackTrace();
                                                       Appreference.printLog("Audioplayer playButton.setOnSeekBarChangelistener","Exception "+e.getMessage(),"WARN",null);
                                                   }
                                                   play_button.setImageResource(R.drawable.play);
                                               } else {
                                                   seekBar.setProgress(0);
                                                   Log.d("playing", "played.............");
                                                   play_button.setImageResource(R.drawable.ic_pause_xl);
                                                   playtimer = false;
                                                   Log.d("playing", "playtimer");
                                                   try {

                                                       mHandler.postDelayed(mProgressUpdater, 500);
                                                       mPlayer.start();
                                                       playtimer = true;
//                                                       cm.setBase(SystemClock.elapsedRealtime());
//                                                       cm.start();
                                                       seekBar.setProgress(0);
                                                       seekBar.setMax(mPlayer.getDuration());
                                                       updatetime.run();
                                                   } catch (Exception e) {
                                                       e.printStackTrace();
                                                       Appreference.printLog("Audioplayer playButton.setOnSeekBarChangelistener","Exception "+e.getMessage(),"WARN",null);
                                                       stopPlayback();
                                                   }
                                               }

                                           }
                                       }
        );
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                               @Override
                                               public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {
                                               }

                                               @Override
                                               public void onStartTrackingTouch(SeekBar seekBar) {
                                                   try {
                                                       mPlayer.seekTo(seekBar.getProgress());
                                                   } catch (IllegalStateException e) {
                                                       e.printStackTrace();
                                                       Appreference.printLog("Audioplayer seekbar.setOnSeekBarChangelistener","Exception "+e.getMessage(),"WARN",null);
                                                   }
                                                   updatetime.run();
                                               }

                                               @Override
                                               public void onStopTrackingTouch(SeekBar seekBar) {
                                                   if (!mPlayer.isPlaying())
                                                       try {
                                                           mPlayer.pause();

                                                   int currentPosition = (seekBar.getProgress());

                                                   mPlayer.seekTo(currentPosition);
                                                   updatetime.run();
                                                       } catch (IllegalStateException e) {
                                                           e.printStackTrace();
                                                           Appreference.printLog("Audioplayer seekbar.setOnSeekBarChangelistener","Exception "+e.getMessage(),"WARN",null);
                                                       }
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
                        seekBar.setProgress(mPlayer.getCurrentPosition());
                        handlerSeek.postDelayed(updatetime, 100);
                    }
                }
            }
        };

        isPlayCompleted = false;
        seekBar.setProgress(0);
        Log.d("playing", "played.............");
        play_button.setImageResource(R.drawable.ic_pause_xl);
        playtimer = false;
        Log.d("playing", "playtimer");
        try {

            mHandler.postDelayed(mProgressUpdater, 500);
            mPlayer.start();
            playtimer = true;
//                                                       cm.setBase(SystemClock.elapsedRealtime());
//                                                       cm.start();
            seekBar.setProgress(0);
            seekBar.setMax(mPlayer.getDuration());
            updatetime.run();
        } catch (Exception e) {
            e.printStackTrace();
            stopPlayback();
        }


        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onComplete();
            }
        });
    }

    public void stopPlayback() {
        mPlayingPosition = -1;
        if (mPlayer != null && mPlayer.isPlaying())
            try {
                mPlayer.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Appreference.printLog("Audioplayer stopplayback","Exception "+e.getMessage(),"WARN",null);
            }
        playtimer = false;
    }

    public void onComplete() {
        finish();
        play_button.setImageResource(R.drawable.play);
        seekBar.setProgress(0);
        if (mPlayer != null) {
            mPlayer.reset();
            try {
                mPlayer.setDataSource(filePath);
                mPlayer.prepare();

            } catch (IOException e) {
                e.printStackTrace();
                Appreference.printLog("Audioplayer oncomplete","Exception "+e.getMessage(),"WARN",null);
            }
        }
        isPlayCompleted = true;
        double tot_time = mPlayer.getDuration();
        String hms = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) tot_time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours((long) tot_time)),
                TimeUnit.MILLISECONDS.toSeconds((long) tot_time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) tot_time)));
        tvToUpdate.setText(hms);
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
                tvToUpdate.setText(min + ":" + sec);
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
            try {
                mPlayer.reset();
                playtimer = false;
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("Audioplayer ondestroy","Exception "+e.getMessage(),"WARN",null);
            }
    }
}


