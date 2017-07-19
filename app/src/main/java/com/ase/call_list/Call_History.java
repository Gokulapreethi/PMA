package com.ase.call_list;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ase.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by vignesh on 6/28/2016.
 */
public class Call_History extends Activity {

    TextView type, host, participant, start, callduration, back, runningtime, totaltime;
    ImageButton play;
    SeekBar audio;
    MediaPlayer mediaPlayer;
    Handler seekhandler;
    private Runnable notification = null;
    Handler durationhandler = null;
    float startTime, finalTime;

    @Override
    protected void onCreate(Bundle SavedInstancestate) {
        super.onCreate(SavedInstancestate);
        try {
            setContentView(R.layout.callhistory_row);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

            final String record_path = getIntent().getStringExtra("recordedpath");
            LinearLayout audio_layout = (LinearLayout) findViewById(R.id.recordedfilelayout);
            if (record_path != null) {
                audio_layout.setVisibility(View.VISIBLE);
            } else {
                audio_layout.setVisibility(View.GONE);
            }

            type = (TextView) findViewById(R.id.calltype);
            durationhandler = new Handler();
            host = (TextView) findViewById(R.id.call_host);
            participant = (TextView) findViewById(R.id.call_participant);
            start = (TextView) findViewById(R.id.call_start);
            callduration = (TextView) findViewById(R.id.call_duration);
            back = (TextView) findViewById(R.id.back);
            play = (ImageButton) findViewById(R.id.play);
            runningtime = (TextView) findViewById(R.id.runningtime);
            totaltime = (TextView) findViewById(R.id.totaltime);
            audio = (SeekBar) findViewById(R.id.seekbar);
            seekhandler = new Handler();

            mediaPlayer = new MediaPlayer();
            if (record_path != null) {
                mediaPlayer.setDataSource(record_path);
                mediaPlayer.setLooping(false);
                mediaPlayer.prepare();
//            }

//            mediaPlayer = MediaPlayer.create(this, R.raw.kalimba);
                audio.setMax(mediaPlayer.getDuration());
            }
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finalTime = mediaPlayer.getDuration();
                    startTime = mediaPlayer.getCurrentPosition();
                    totaltime.setText(String.format("%d min, %d sec",
                                    TimeUnit.MILLISECONDS.toMinutes((long) finalTime),
                                    TimeUnit.MILLISECONDS.toSeconds((long) finalTime) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) finalTime)))
                    );

                    runningtime.setText(String.format("%d min, %d sec",
                                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) startTime)))
                    );
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        play.setBackgroundResource(R.drawable.ic_playicon);
                    } else {
                        mediaPlayer.start();
                        startPlayProgressUpdater();
                        UpdateSongTime.run();
                        audio.setProgress((int) startTime);
                        durationhandler.postDelayed(UpdateSongTime, 100);
                        play.setBackgroundResource(R.drawable.ic_pause_xl);
                    }
//                    try {
////                        mediaPlayer.prepare();
//
//
//                    }catch(IOException e)
//                    {
//                        Log.d("IOError","Finding media"+e);
//                    }
//                }


                }

            });


            String calltype = null;
            calltype = getIntent().getStringExtra("calltype");
            type.setText(calltype);
            host.setText(getIntent().getStringExtra("host"));
            String part = getIntent().getStringExtra("participant");
        /*if (calltype.equals("IncomingCall")) {
            part = part.substring(4);
            String part1[] = part.split("@");
            part = VideoCallDataBase.getDB(context).getName(part1[0]);
        }
        else if(calltype.equals("OutgoingCall")){
            part = part.substring(4);
            String part1[] = part.split("@");
            part = VideoCallDataBase.getDB(context).getName(part1[0]);
        }*/
            participant.setText(part);
            start.setText(getIntent().getStringExtra("start_time"));
            callduration.setText(getIntent().getStringExtra("duration") + " secs");
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    try {
                        if(record_path != null) {
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(record_path);
                            mediaPlayer.setLooping(false);
                            mediaPlayer.prepare();
                            play.setBackgroundResource(R.drawable.ic_playicon);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void startPlayProgressUpdater() {
        try {
            audio.setProgress(mediaPlayer.getCurrentPosition());
            Log.d("player", "?????????????????????" + mediaPlayer.isPlaying());
            if (mediaPlayer.isPlaying()) {
                notification = new Runnable() {
                    public void run() {
                        try {
                            startPlayProgressUpdater();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                seekhandler.postDelayed(notification, 500);
            } else {
                audio.setProgress(mediaPlayer.getCurrentPosition());
//                m.pause();
            }
            audio.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                    seekhandler.removeCallbacks(notification);
                    int currentPosition = (seekBar.getProgress());

                    mediaPlayer.seekTo(currentPosition);

                    startPlayProgressUpdater();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                    seekhandler.removeCallbacks(notification);
                }

                @Override
                public void onProgressChanged(SeekBar seekBar,
                                              int progress, boolean fromUser) {
                    // TODO Auto-generated method stub

                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private Runnable UpdateSongTime = new Runnable() {
        public void run() {
            startTime = mediaPlayer.getCurrentPosition();
            runningtime.setText(String.format("%d min, %d sec",
                            TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) startTime)))
            );
            audio.setProgress((int) startTime);
            durationhandler.postDelayed(this, 100);
        }
    };


}


