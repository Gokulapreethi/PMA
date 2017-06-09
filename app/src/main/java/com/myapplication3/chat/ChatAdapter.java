package com.myapplication3.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.myapplication3.Audioplayer;
import com.myapplication3.FullScreenImage;
import com.myapplication3.R;
import com.myapplication3.VideoPlayer;

import org.pjsip.pjsua2.app.MainActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class ChatAdapter extends ArrayAdapter<ChatBean> {

    static ViewHolder holder;
    private final MediaPlayer mPlayer = new MediaPlayer();
    String dir_path = Environment.getExternalStorageDirectory()
            + "/High Message/downloads/";
    Runnable updatetime = null;
    Handler handler = new Handler();
    boolean play = false;
    long sTime;
    /*    static SeekBar seekBar;
        static ImageView play_button;
        static LinearLayout audio_play;
        TextView totaltime;*/
    String mediapath;
    int starttime;
    private Context context;
    private ArrayList<ChatBean> chatList;
    private LayoutInflater inflater = null;
    private int mPlayingPosition = -1;

    public ChatAdapter(Context context, ArrayList<ChatBean> chatList) {

        super(context, R.layout.chat_row, chatList);
        /********** Take passed liveCall_Values **********/
        this.context = context;
        this.chatList = chatList;

    }


    public ChatBean getItem(int position) {
        return chatList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView,
                        ViewGroup parent) {

        try {
            holder = new ViewHolder();
            if (convertView == null) {
                inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.chat_row, null);
                holder.senderLayout = (RelativeLayout) convertView
                        .findViewById(R.id.rl_right);
                holder.receiverLayout = (RelativeLayout) convertView
                        .findViewById(R.id.rl_left);
                holder.iv_status = null;
                holder.sender_multimedai_image = (ImageView) convertView.findViewById(R.id.sender_image_msg);
                holder.receiver_multimedai_image = (ImageView) convertView.findViewById(R.id.receiver_image_msg);
                holder.audio_play = (LinearLayout) convertView.findViewById(R.id.audio_play);
            /*totaltime = (TextView) audio_play.findViewById(R.id.txt_time);
            seekBar = (SeekBar) audio_play.findViewById(R.id.seekBar1);
            play_button=(ImageView)audio_play.findViewById(R.id.play_button);*/
                holder.txt_time = (TextView) convertView.findViewById(R.id.txt_time);
                holder.seekBar = (SeekBar) convertView.findViewById(R.id.seekBar1);
                holder.play_button = (ImageView) convertView.findViewById(R.id.play_button);
                holder.play_button.setEnabled(true);
                holder.ad_sent_time = (TextView) convertView.findViewById(R.id.ad_send_time);
                holder.message = (TextView) convertView .findViewById(R.id.sender_text_msg);
                holder.dateTime = (TextView) convertView .findViewById(R.id.tv_rec_time);
                holder.iv_status = (ImageView) convertView.findViewById(R.id.im_status);
                holder.receiver_message = (TextView) convertView.findViewById(R.id.receiver_text_msg);
                holder.receivtime = (TextView) convertView.findViewById(R.id.tv_send_time);
//            final SeekBar seekBar1=seekBar;
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            String time = null;
            // TextView message, dateTime;

            //if(!bean.getType().equalsIgnoreCase("audio"))


           /* holder.senderLayout.setVisibility(View.GONE);
            holder.receiverLayout.setVisibility(View.GONE);
            holder.audio_play.setVisibility(View.GONE);*/


            final ChatBean bean = chatList.get(position);

            /*if (bean.getFromUser()
                    .equalsIgnoreCase(MainActivity.username)) {
                Log.d("chat", "------entry Login user if------");
                holder.senderLayout.setVisibility(View.VISIBLE);
                holder.receiverLayout.setVisibility(View.GONE);
                holder.audio_play.setVisibility(View.GONE);

            } else {
                Log.e("receive amesssage", bean.getMsgtype() + bean.getMessage());
                holder.receiverLayout.setVisibility(View.VISIBLE);
                holder.senderLayout.setVisibility(View.GONE);
                holder.audio_play.setVisibility(View.GONE);

            }*/

            if ((bean.getMsgtype().equals("text")) && bean.getFromUser().equalsIgnoreCase(MainActivity.username)) {
                holder.senderLayout.setVisibility(View.VISIBLE);
                holder.receiverLayout.setVisibility(View.GONE);
                holder.audio_play.setVisibility(View.GONE);
                holder.message.setVisibility(View.VISIBLE);
                holder.message.setText(bean.getMessage());
                Log.e("log message","sender"+bean.getMessage());
                holder.sender_multimedai_image.setVisibility(View.GONE);
                holder.receiver_multimedai_image.setVisibility(View.GONE);
            } else if (bean.getMsgtype().equalsIgnoreCase("text")) {

                holder.receiverLayout.setVisibility(View.VISIBLE);
                holder.senderLayout.setVisibility(View.GONE);
                holder.audio_play.setVisibility(View.GONE);



                holder.receiver_message.setVisibility(View.VISIBLE);
                holder.receiver_message.setText(bean.getMessage());
                Log.e("log message","receiver"+bean.getMessage());
                holder.sender_multimedai_image.setVisibility(View.GONE);
                holder.receiver_multimedai_image.setVisibility(View.GONE);
            } else {
                Log.e("Send a message", bean.getMsgtype());
                int THUMBSIZE = 64;
                if (bean.getFromUser()
                        .equalsIgnoreCase(MainActivity.username)) {
                    holder.senderLayout.setVisibility(View.VISIBLE);
                    holder.sender_multimedai_image.setVisibility(View.VISIBLE);
                    holder.receiverLayout.setVisibility(View.GONE);
                    holder.message.setVisibility(View.GONE);
                    if (bean.getMsgtype().equalsIgnoreCase("image") || bean.getMsgtype().equalsIgnoreCase("sketch")) {
                        holder.audio_play.setVisibility(View.GONE);
                        holder.receiverLayout.setVisibility(View.GONE);
                        holder.sender_multimedai_image.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()
                                        + "/High Message/" + bean.getMessage()),
                                THUMBSIZE, THUMBSIZE));
                    } else if (bean.getMsgtype().equalsIgnoreCase("video")) {
                        holder.sender_multimedai_image.setImageBitmap(ThumbnailUtils.createVideoThumbnail(Environment.getExternalStorageDirectory()
                                + "/High Message/" + bean.getMessage(), MediaStore.Video.Thumbnails.MINI_KIND));
                    } else if (bean.getMsgtype().equalsIgnoreCase("audio")) {


                        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                        holder.senderLayout.setVisibility(View.GONE);
                        holder.receiverLayout.setVisibility(View.GONE);
                        holder.audio_play.setVisibility(View.VISIBLE);

                        LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) holder.audio_play.getLayoutParams();
                        lllp.gravity = Gravity.RIGHT;
                        holder.audio_play.setLayoutParams(lllp);
                        Log.e("Same User", "...........................");
                        Log.d("task", "download path is audio " + bean.getPath());
                        mmr.setDataSource(bean.getPath());

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

                       // holder.txt_time.setText(String.format(min + ":" + sec));
                        String datetime=bean.getDatetime();
                        holder.ad_sent_time.setText(datetime.substring(11));


                        // audio_play.setGravity(Gravity.RIGHT);
//                        sender_multimedai_image.setBackgroundResource(R.drawable.ic_audio_file_2_128);
                        // sender_multimedai_image.setImageResource(R.drawable.ic_audio_file_2_128);
                    } else if (bean.getMsgtype().equalsIgnoreCase("file")) {
//                        sender_multimedai_image.setBackgroundResource(R.drawable.ic_paper_clip_2_128);
                        holder.receiver_message.setVisibility(View.GONE);
                        holder.receiverLayout.setVisibility(View.GONE);
                        holder.audio_play.setVisibility(View.GONE);
                        holder.sender_multimedai_image.setImageResource(R.drawable.ic_paper_clip_2_128);
                    }

                } else {
                    Log.e("receive a message", bean.getType());
                    holder.receiverLayout.setVisibility(View.VISIBLE);
                    holder.receiver_multimedai_image.setVisibility(View.VISIBLE);
                    holder.senderLayout.setVisibility(View.GONE);
                    holder.receiver_message.setVisibility(View.GONE);
                    if (bean.getMsgtype().equalsIgnoreCase("image") || bean.getMsgtype().equalsIgnoreCase("sketch")) {
                        holder.receiver_multimedai_image.setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()
                                        + "/High Message/downloads/" + bean.getMessage()),
                                THUMBSIZE, THUMBSIZE));
                    } else if (bean.getMsgtype().equalsIgnoreCase("video")) {
                        holder.receiver_multimedai_image.setImageBitmap(ThumbnailUtils.createVideoThumbnail(Environment.getExternalStorageDirectory()
                                + "/High Message/downloads/" + bean.getMessage(), MediaStore.Video.Thumbnails.MINI_KIND));
                    } else if (bean.getMsgtype().equalsIgnoreCase("audio")) {


                        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                        holder.senderLayout.setVisibility(View.GONE);
                        holder.receiverLayout.setVisibility(View.GONE);
                        holder.audio_play.setVisibility(View.VISIBLE);
                        Log.e("Other User", ".....................");
                        LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) holder.audio_play.getLayoutParams();
                        lllp.gravity = Gravity.LEFT;
                        holder.audio_play.setLayoutParams(lllp);
                        Log.d("task", "download path is audio "+Environment.getExternalStorageDirectory()
                                + "/High Message/downloads/"+ bean.getMessage() );
                        mmr.setDataSource(Environment.getExternalStorageDirectory()
                                + "/High Message/downloads/"+ bean.getMessage());
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

                       // holder.txt_time.setText(String.format(min + ":" + sec));
                        String datetime=bean.getDatetime();
                        holder.ad_sent_time.setText(datetime.substring(11));


//                        receiver_multimedai_image.setBackgroundResource(R.drawable.ic_audio_file_2_128);
                        //receiver_multimedai_image.setImageResource(R.drawable.ic_audio_file_2_128);
                    } else if (bean.getMsgtype().equalsIgnoreCase("file")) {
//                        receiver_multimedai_image.setBackgroundResource(R.drawable.ic_paper_clip_2_128);
                        holder.receiver_multimedai_image.setImageResource(R.drawable.ic_paper_clip_2_128);
                        holder.receiverLayout.setVisibility(View.GONE);
                        holder.audio_play.setVisibility(View.GONE);
                    }
                }
            }
            if (bean.getFromUser()
                    .equalsIgnoreCase(MainActivity.username)) {
                String dd = bean.getDatetime();
                for (String time1 : dd.split(" ", 2)) {
                    Log.i("chat", "time-->" + time1);
                    time = time1;
                }
                holder.dateTime.setText(time);
            } else {
                String dd = bean.getDatetime();
                for (String time1 : dd.split(" ", 2)) {
                    Log.i("chat", "time-->" + time1);
                    time = time1;
                }
                holder.receivtime .setText(bean.getFromUser()+" "+time);
            }

            if (bean.getMsg_status() == 0) {
                if (holder.iv_status != null)
                    holder.iv_status.setImageResource(R.drawable.msg_notsent);
            } else {
                if (holder.iv_status != null)
                    holder.iv_status.setImageResource(R.drawable.msgsent);
            }

            holder.sender_multimedai_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(bean.getMsgtype().equalsIgnoreCase("image") || bean.getMsgtype().equalsIgnoreCase("sketch")) {
                        Intent intent = new Intent(context,FullScreenImage.class);
                        intent.putExtra("image", Environment.getExternalStorageDirectory()
                                + "/High Message/" +bean.getMessage());
                        context.startActivity(intent);
                    } else if(bean.getMsgtype().equalsIgnoreCase("video")) {
                        Intent intent = new Intent(context, VideoPlayer.class);
                        intent.putExtra("video", Environment.getExternalStorageDirectory()
                                + "/High Message/" +bean.getMessage());
                        context.startActivity(intent);
                    } else if(bean.getMsgtype().equalsIgnoreCase("audio")) {
                    } else if(bean.getMsgtype().equalsIgnoreCase("file")) {
                    }
                }
            });

            holder.receiver_multimedai_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(bean.getMsgtype().equalsIgnoreCase("image") || bean.getMsgtype().equalsIgnoreCase("sketch")) {
                        Intent intent = new Intent(context,FullScreenImage.class);
                        intent.putExtra("image",Environment.getExternalStorageDirectory()
                                + "/High Message/downloads/" +bean.getMessage());
                        context.startActivity(intent);
                    } else if(bean.getMsgtype().equalsIgnoreCase("video")) {
                        Intent intent = new Intent(context, VideoPlayer.class);
                        intent.putExtra("video", Environment.getExternalStorageDirectory()
                                + "/High Message/downloads/" +bean.getMessage());
                        context.startActivity(intent);
                    } else if (bean.getMsgtype().equalsIgnoreCase("audio")) {
                    } else if (bean.getMsgtype().equalsIgnoreCase("file")) {

                            Log.i("group123", "icon clicked doc");
                            File directory;
                            // if(gcBean.getUser() != null && gcBean.getUser().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))
                            // directory = new File(gcBean.getMediaPath());
                            //else
                            directory = new File(bean.getPath());

                            if (directory.exists()) {
                                Uri filepath = Uri.fromFile(directory);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(filepath, "application/pdf");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                try {
                                    context.startActivity(intent);
                                } catch (Exception e) {
                                    Log.e("error", "" + e);
                                }

                            } else {
                                Log.i("can't open", "file");

                            }
                    }
                }
            });


           /* convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final LinearLayout finalaudio_play=audio_play;
                    seekBar = (SeekBar)finalaudio_play.findViewById(R.id.seekBar1);
                    totaltime=(TextView)finalaudio_play.findViewById(R.id.txt_time);
                }
            });*/
            final ChatBean bean1 = chatList.get(position);
            final ViewHolder finalHolder = holder;
            holder.play_button.setOnClickListener(new Button.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(getContext(),Audioplayer.class);
                    i.putExtra("audio",bean.getPath());
                    Log.i("path","pathvalue"+bean.getPath());
                    context.startActivity(i);

                }
            });
            /*holder.play_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.seekBar = (SeekBar) finalHolder.audio_play.findViewById(R.id.seekBar1);
                    //holder.txt_time = (TextView) finalHolder.audio_play.findViewById(R.id.txt_time);

                    boolean click = true;
//                seek_playing = seekBar;
                    finalHolder.audio_play.setVisibility(View.VISIBLE);

                    if (bean.getPath() == mediapath) {
                        if (mPlayer.isPlaying()) {
                            mPlayer.pause();
                            finalHolder.play_button.setImageResource(R.drawable.play);
//                        holder.play_button.setImageResource(R.drawable.play);
                        } else if (mPlayer.getCurrentPosition() >= 0) {
                            mPlayer.start();
                            finalHolder.play_button.setImageResource(R.drawable.ic_pause_xl);
//                            holder.play_button.setImageResource(R.drawable.pause_8);
                        }
                    } else {
                        if (mPlayer.getCurrentPosition() > 1) {
                            holder.seekBar.setProgress(0);
                            mPlayer.stop();
                        }
                        mediapath = bean.getPath();
                        Log.d("playing", "played.............");
                        if (bean.getFromUser().equalsIgnoreCase(MainActivity.username)){
                        finalHolder.play_button.setImageResource(R.drawable.ic_pause_xl);
                        playAudio(bean.getPath(), position);}
                        else
                            playAudio(dir_path+bean.getPath(),position);
                    }


                    mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {

                            finalHolder.play_button.setImageResource(R.drawable.play);
                            holder.seekBar.setProgress(0);


                        }
                    });





                }
            });

            holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {

//
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                    //handler.removeCallbacks(updatetime);
//                                        notification.run();
                    mPlayer.seekTo(seekBar.getProgress());
//                                            startPlayProgressUpdater();
                    updatetime.run();
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (!mPlayer.isPlaying())
                        mPlayer.pause();
                    //handler.removeCallbacks(updatetime);
                    int currentPosition = (holder.seekBar.getProgress());

                    mPlayer.seekTo(currentPosition);
                                           *//* holder.buddyName.setText(String.format("%l min, %l sec",
                                                    TimeUnit.MILLISECONDS.toMinutes((long) Integer.parseInt(duration)),
                                                    TimeUnit.MILLISECONDS.toSeconds((long) Integer.parseInt(duration)) -
                                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) Integer.parseInt(duration)))));*//*
//                                            startPlayProgressUpdater();
                    updatetime.run();
//                                            holder.seekBar.setProgress(0);
                }
            });*/
            /*final Runnable updatetime = new Runnable() {
                public void run() {

                    if (mPlayer != null) {
                        int startTime = mPlayer.getCurrentPosition();
                        if (mPlayer != null) {
                            startTime = mPlayer.getCurrentPosition();
//            tx1.setText(String.format("%d min, %d sec",

                       *//*     TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                            TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                            toMinutes((long) startTime)))
            );*//*
                            holder.seekBar.setProgress(startTime);
                            //myHandler.postDelayed(this, 100);
                        }
                    }
                }
            };*/



            updatetime = new Runnable() {
                @Override
                public void run() {
                    if (mPlayer.isPlaying()) {
                        String min, sec;
                        holder.seekBar.setProgress(mPlayer.getCurrentPosition());
                        sTime = mPlayer.getCurrentPosition();
                        min = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(sTime));
                        sec = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime)));
                        if (Integer.parseInt(min) < 10) {
                            min = 0 + String.valueOf(min);
                        }
                        if (Integer.parseInt(sec) < 10) {
                            sec = 0 + String.valueOf(sec);
                        }

                        finalHolder.txt_time.setText(String.format(min+":"+sec));
                    } else {
                        play = false;

                    }

                    if (mPlayer.isPlaying())
                        handler.postDelayed(updatetime, 100);
                    else
                        mPlayer.stop();


                }

            };

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public void stopPlayback() {
        mPlayingPosition = -1;
//        mProgressUpdater.mBarToUpdate = null;
//        mProgressUpdater.tvToUpdate = null;
        if (mPlayer != null && mPlayer.isPlaying())
            mPlayer.stop();
    }

    private void playAudio(String fname, int position) {
        try {
            mPlayer.reset();
            mPlayer.setDataSource(fname);
            mPlayer.prepare();
            mPlayer.start();
            play = true;

            holder.seekBar.setMax(mPlayer.getDuration());
//            st=1;
            starttime = mPlayer.getCurrentPosition();
            updatetime.run();
//            startPlayProgressUpdater();
            /*handler.postDelayed(notification,500);*/
            handler.postDelayed(updatetime, 500);
            mPlayingPosition = position;
            holder.seekBar.setMax(mPlayer.getDuration());

            this.notifyDataSetChanged();


        } catch (IOException e) {
            e.printStackTrace();
            stopPlayback();
        }
    }


    class ViewHolder {

        ImageView play_button, x_button, receiver_multimedai_image, sender_multimedai_image, im_status, iv_status;
        LinearLayout over_chat_view, audio_play, ly_sendtime;
        RelativeLayout chat_view, receiverLayout, rl_message, rl_send_arrow, senderLayout, rl_rec_message, rl_rec_arrow, sender_text_msg;
        TextView txt_time, receivtime,message,receiver_message, dateTime, ad_sent_time;
        SeekBar seekBar;
    }


}

