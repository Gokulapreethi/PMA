package com.ase;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.Bean.TaskDetailsBean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by saravanakumar on 6/9/2016.
 */
public class MediaViewAdapter extends ArrayAdapter<TaskDetailsBean> {

    /**
     * ******** Declare Used Variables ********
     */
    private Context context;
   // private ArrayList<MediaListBean> userList;
    private LayoutInflater inflater = null;
    private static int checkBoxCounter = 0;
    private static String path=null;
    private int checkboxcount;
    ArrayList<TaskDetailsBean> detailsBeanArrayList;
    NewTaskConversation newTaskConversation;

    String check;

    /**
     * ********** CustomAdapter Constructor ****************
     */
    public MediaViewAdapter(Context context, ArrayList<TaskDetailsBean> detailsBeanArrayList, String s1) {


        super(context, R.layout.media_view_file, detailsBeanArrayList);
        /********** Take passed values **********/
        this.context = context;
        this.detailsBeanArrayList = detailsBeanArrayList;
        this.check = s1;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /*********** Layout inflator to call external xml layout () ***********/

    }

    /*public MediaViewAdapter(Context context, ArrayList<MediaListBean> userList, String s1, ArrayList<TaskDetailsBean> detailsBeanArrayList) {


        super(context, R.layout.media_view_file, userList);
        *//********** Take passed values **********//*
        this.context = context;
        this.userList = userList;
        this.check = s1;
        this.detailsBeanArrayList = detailsBeanArrayList;
        Log.d("listSize","size  "+detailsBeanArrayList.size());
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        *//*********** Layout inflator to call external xml layout () ***********//*

    }
*/

    /**
     * ***
     * Depends upon data size called for each row , Create each ListView row
     * ***
     */
    public View getView(final int position, View convertView, ViewGroup parent) {

        try {
            ViewHolder holder;

            holder = new ViewHolder();
            if (convertView == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.media_view_file, null);
//                holder.filexbutton = (ImageView) convertView.findViewById(R.id.filexbutton);
                holder.xbutton = (ImageView) convertView.findViewById(R.id.xbutton);
                holder.buddyName = (TextView) convertView.findViewById(R.id.txt_time);
                holder.play_button = (ImageView) convertView.findViewById(R.id.play_button);
                holder.seekBar = (SeekBar)convertView.findViewById(R.id.seekBar1);
                holder.filelayout = (LinearLayout)convertView.findViewById(R.id.list_image);
                holder.ad_play = (RelativeLayout)convertView.findViewById(R.id.ad_play);
//                holder.tv_fname = (TextView) convertView.findViewById(R.id.tv_fname);
                holder.doc_icon = (TextView) convertView.findViewById(R.id.doc_icon);
                holder.ImageView = (ImageView) convertView.findViewById(R.id.thumb_image);
                holder.rec_des = (TextView) convertView.findViewById(R.id.rcv_des) ;
//                holder.VideoView = (VideoView) convertView.findViewById(R.id.thumb_video);
                holder.video_play_icon = (ImageView) convertView.findViewById(R.id.video_play_icon);
                holder.frameLayout = (FrameLayout) convertView.findViewById(R.id.frame_layout);
                holder.rl_add_txt = (RelativeLayout) convertView.findViewById(R.id.rl_add_txt);
                holder.ls_add_txt = (RelativeLayout) convertView.findViewById(R.id.ls_add_txt);
                holder.add_des = (TextView) convertView.findViewById(R.id.txt_des) ;
                holder.removeBtn = (ImageView) convertView.findViewById(R.id.remove_btn);
                holder.doc_iconview=(ImageView) convertView.findViewById(R.id.doc_iconview);
                convertView.setTag(holder);
                newTaskConversation = new NewTaskConversation();
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.xbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    GroupChatActivity.tempSendList(position);
                    if(!check.equalsIgnoreCase("task")) {
                        NewBlogActivity.clearMediaList(position);
                    }else {
                        NewTaskActivity.clearMediaList(position);
                    }

                }
            });

            holder.removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!check.equalsIgnoreCase("task")) {
                        NewBlogActivity.clearMediaList(position);
                    }else {
                        NewTaskConversation.clearMediaList(position);
                    }
                }
            });
           /* holder.rec_remove_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!check.equalsIgnoreCase("task")) {
                        NewBlogActivity.clearMediaList(position);
                    }else {
                        NewTaskConversation.clearMediaList(position);
                    }
                }
            });*/
//            holder.filexbutton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    GroupChatActivity.tempSendList(position);
//                    if(!check.equalsIgnoreCase("task")) {
//                        NewBlogActivity.clearMediaList(position);
//                    }else {
//                    NewTaskActivity.clearMediaList(position);
//                    }
//                }
//            });

            final TaskDetailsBean gcBean = detailsBeanArrayList.get(position);
            final ViewHolder finalHolder = holder;
            holder.play_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    finalHolder.ad_play.setVisibility(View.VISIBLE);
                    playAudio(gcBean.getTaskDescription(),position);
                }
            });
            if (gcBean != null) {

                holder.filelayout.setVisibility(View.GONE);
                holder.ad_play.setVisibility(View.VISIBLE);
                Log.i("MediaType","Value"+gcBean.getMimeType());
                if (gcBean.getMimeType()!=null && gcBean.getMimeType().equals(""))
                {

                }
                else{
                    holder.filelayout.setVisibility(View.VISIBLE);
                    holder.ad_play.setVisibility(View.GONE);
                    Log.d("HighMessage--->","Adapter MediaPath Image  "+gcBean.getTaskDescription());
//                    Bitmap bmp = BitmapFactory.decodeFile(gcBean.getMediaPath());
//                    holder.ImageView.setImageBitmap(bmp);

                    if(gcBean.getMimeType()!=null && gcBean.getMimeType().equalsIgnoreCase("image")) {
                        File imageFile = new File(gcBean.getTaskDescription());
                        if (imageFile.exists()) {

                            Log.i("imagefile","image");
                            holder.frameLayout.setVisibility(View.VISIBLE);
                            holder.rl_add_txt.setVisibility(View.GONE);
                            holder.video_play_icon.setVisibility(View.GONE);
                            holder.ImageView.setVisibility(View.VISIBLE);
//                            holder.filexbutton.setVisibility(View.VISIBLE);
                            holder.doc_icon.setVisibility(View.GONE);
                            holder.ad_play.setVisibility(View.GONE);
                            holder.doc_iconview.setVisibility(View.GONE);
//                            holder.VideoView.setVisibility(View.GONE);
                            holder.ImageView.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
//                            holder.tv_fname.setText(gcBean.getMediaPath().split("HighMessage/")[1]);
                        }
                    }else if (gcBean.getMimeType()!=null && gcBean.getMimeType().equalsIgnoreCase("doc")) {
                        File imageFile = new File(gcBean.getTaskDescription());
                        if (imageFile.exists()) {
                            holder.video_play_icon.setVisibility(View.GONE);
//                            holder.filexbutton.setVisibility(View.VISIBLE);
                            holder.ImageView.setVisibility(View.GONE);
//                            holder.add_des.setVisibility(View.GONE);
//                            holder.removeBtn.setVisibility(View.GONE);
                            holder.doc_icon.setVisibility(View.VISIBLE);
                            holder.frameLayout.setVisibility(View.VISIBLE);
                            holder.ad_play.setVisibility(View.GONE);
                            holder.doc_iconview.setVisibility(View.VISIBLE);
                            holder.rl_add_txt.setVisibility(View.GONE);


                            holder.doc_icon.setText(gcBean.getTaskDescription().split("High Message/")[1]);
//                            holder.ImageView.setImageBitmap(BitmapFactory.decodeFile(compressImage(imageFile.getAbsolutePath())));
//                            holder.tv_fname.setText(gcBean.getMediaPath().split("HighMessage/")[1]);
                        }
                    }

                    else if(gcBean.getMimeType()!=null && gcBean.getMimeType().equalsIgnoreCase("text")) {
                        String imageFile =  gcBean.getTaskDescription();
                        if (!imageFile.equalsIgnoreCase("")) {

                            holder.frameLayout.setVisibility(View.GONE);
                            holder.rl_add_txt.setVisibility(View.VISIBLE);
                            holder.video_play_icon.setVisibility(View.GONE);
                            holder.doc_iconview.setVisibility(View.GONE);
                            holder.doc_icon.setVisibility(View.GONE);
                            holder.ImageView.setVisibility(View.GONE);
//                            holder.filexbutton.setVisibility(View.GONE);
//                            holder.VideoView.setVisibility(View.GONE);

//                            holder.tv_fname.setText(gcBean.getMediaPath().split("HighMessage/")[1]);

                            if(gcBean.getFromUserName() != null)
                            {
                                holder.rl_add_txt.setVisibility(View.VISIBLE);
                                holder.ls_add_txt.setVisibility(View.GONE);
                                holder.add_des.setText(gcBean.getTaskDescription());
                                if(gcBean.getSendStatus() != null && gcBean.getSendStatus().equalsIgnoreCase("1"))
                                {
                                    holder.removeBtn.setVisibility(View.VISIBLE);
                                    holder.add_des.setOnLongClickListener(new View.OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View v) {
                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                            builder1.setMessage("DO you like send this message.");
                                            builder1.setCancelable(true);

                                            builder1.setPositiveButton(
                                                    "Send",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                         /*   TaskDetailsBean taskDetailsBean = detailsBeanArrayList.get(position);


                                                            SimpleDateFormat dateFormat = new SimpleDateFormat(
                                                                    "dd/MM/yyyy hh:mm:ss a");
                                                            String dateforrow = dateFormat.format(new Date());


                                                            final TaskDetailsBean chatBean = new TaskDetailsBean();
                                                            chatBean.setFromUserId(String.valueOf(taskDetailsBean.getFromUserId()));
                                                            chatBean.setToUserId(taskDetailsBean.getToUserId());
                                                            chatBean.setTaskName(taskDetailsBean.getTaskName());
                                                            chatBean.setTaskDescription(taskDetailsBean.getTaskDescription());
                                                            chatBean.setTaskNo(taskDetailsBean.getTaskNo());
                                                            chatBean.setParentId(taskDetailsBean.getParentId());
                                                            chatBean.setTaskPriority(taskDetailsBean.getTaskPriority());
                                                            chatBean.setIsRemainderRequired("");
                                                            chatBean.setCompletedPercentage(taskDetailsBean.getCompletedPercentage());
                                                            chatBean.setPlannedStartDateTime("");
                                                            chatBean.setPlannedEndDateTime("");
                                                            chatBean.setRemainderFrequency("");
                                                            chatBean.setSignalid(taskDetailsBean.getSignalid());
                                                            chatBean.setDateTime(dateforrow);
                                                            chatBean.setSendStatus("0");

                                                            String xml = newTaskConversation.composeChatXML(chatBean);
                                                            newTaskConversation.sendInstantMessage(xml);

                                                            VideoCallDataBase.getDB(context).update_Task_history(chatBean);
*/

                                                            dialog.cancel();
                                                        }
                                                    });


                                            builder1.setNegativeButton(
                                                    "Cancel",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            dialog.cancel();
                                                        }
                                                    });

                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();
                                            return false;

                                        }
                                    });

                                }else {
                                    holder.removeBtn.setVisibility(View.GONE);
                                }
                            }else
                            {
                                holder.ls_add_txt.setVisibility(View.VISIBLE);
                                holder.rl_add_txt.setVisibility(View.GONE);
                                holder.rec_des.setText(gcBean.getTaskDescription());
                            }
                        }
                    }
                    else  if(gcBean.getMimeType()!=null && gcBean.getMimeType().equalsIgnoreCase("video")) {
                        File imageFile = new File(gcBean.getTaskDescription());
                        if (imageFile.exists()) {

                            holder.frameLayout.setVisibility(View.VISIBLE);
                            holder.rl_add_txt.setVisibility(View.GONE);
                            holder.ad_play.setVisibility(View.GONE);
                            holder.doc_iconview.setVisibility(View.GONE);
                            holder.doc_icon.setVisibility(View.GONE);
                            holder.video_play_icon.setVisibility(View.VISIBLE);
                            holder.ImageView.setVisibility(View.VISIBLE);
//                            holder.filexbutton.setVisibility(View.VISIBLE);
                            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(imageFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                            holder.ImageView.setImageBitmap(bMap);
                            path = gcBean.getTaskDescription();
//                            holder.tv_fname.setText(gcBean.getMediaPath().split("HighMessage/")[1]);
                        }
                    }else if(gcBean.getMimeType()!=null && gcBean.getMimeType().equalsIgnoreCase("audio"))
                    {
                        if (position == mPlayingPosition) {
                            //pb.setVisibility(View.VISIBLE);
                            mProgressUpdater.mBarToUpdate = holder.seekBar;
                            mProgressUpdater.tvToUpdate = holder.buddyName;
                            mHandler.postDelayed(mProgressUpdater, 100);
                        } else {
                            //pb.setVisibility(View.GONE);
                            if (gcBean.getMimeType()!=null && gcBean.getMimeType().equals("audio")) {
                                try {

                                    holder.rl_add_txt.setVisibility(View.GONE);
                                    holder.video_play_icon.setVisibility(View.GONE);
                                    holder.frameLayout.setVisibility(View.GONE);
                                    holder.ImageView.setVisibility(View.GONE);
//                                holder.filelayout.setVisibility(View.GONE);
//                                holder.VideoView.setVisibility(View.GONE);
                                    holder.doc_iconview.setVisibility(View.GONE);
                                    holder.doc_icon.setVisibility(View.GONE);
                                    holder.ad_play.setVisibility(View.VISIBLE);
                                    holder.seekBar.setProgress(0);
                                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                                    mmr.setDataSource(gcBean.getTaskDescription());
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
                                    holder.buddyName.setText(min + ":" + sec);
//                            audio_tv.setText(duration);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                holder.seekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                                holder.seekBar.setProgress(0);
                                if (mProgressUpdater.mBarToUpdate == holder.seekBar) {
                                    //this progress would be updated, but this is the wrong position
                                    mProgressUpdater.mBarToUpdate = null;
                                }
                            }
                        }
                    }

                    holder.ImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (gcBean.getMimeType().equalsIgnoreCase("video")) {
                                Log.i("group123", "icon clicked video");
                                File directory = new File(gcBean.getTaskDescription());
                                if (directory.exists()) {
                                    Intent intent = new Intent(context, VideoPlayer.class);
                                    intent.putExtra("video", gcBean.getTaskDescription());
                                    context.startActivity(intent);
                                } else {
                                    Toast.makeText(context, "File not available", Toast.LENGTH_LONG).show();

                                }
                            } else if (gcBean.getMimeType()
                                    .equalsIgnoreCase("image")) {
                                Log.i("group123", "icon clicked image");
                                Intent intent = new Intent(context, FullScreenImage.class);
                                intent.putExtra("image", gcBean.getTaskDescription());
                                context.startActivity(intent);
                            } else {
                                Log.i("AAAA", "openFilesinExternalApp");

                            }
                        }
                    });
                }

            }
            return convertView;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    class ViewHolder {
        CheckBox selectUser;
        ImageView xbutton,ImageView,video_play_icon,doc_iconview;
        //        VideoView VideoView;
        ImageView play_button;
        TextView buddyName;
        TextView occupation;
        TextView header_title;
        LinearLayout cancel_lay;
        SeekBar seekBar;
        ImageView filexbutton,removeBtn;
        TextView tv_fname,add_des,rec_des;
        LinearLayout filelayout;
        RelativeLayout ad_play,rl_add_txt,ls_add_txt;
        FrameLayout frameLayout;
        TextView doc_icon;
    }
    private final MediaPlayer mPlayer = new MediaPlayer();
    private int mPlayingPosition = -1;
    private Handler mHandler = new Handler();
    private PlaybackUpdater mProgressUpdater = new PlaybackUpdater();
    private class PlaybackUpdater implements Runnable {
        public SeekBar mBarToUpdate = null;
        public TextView tvToUpdate = null;
        @Override
        public void run() {
            if ((mPlayingPosition != -1) && (null != mBarToUpdate)) {
                double tElapsed = mPlayer.getCurrentPosition();
                int fTime = mPlayer.getDuration();
                double timeRemaining = fTime - tElapsed;
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
                mBarToUpdate.setProgress((100 * mPlayer.getCurrentPosition() / mPlayer.getDuration()));
                mHandler.postDelayed(this, 500);

            } else {
                //not playing so stop updating
            }
        }
    }
    private void stopPlayback() {
        mPlayingPosition = -1;
        mProgressUpdater.mBarToUpdate = null;
        mProgressUpdater.tvToUpdate = null;
        if (mPlayer != null && mPlayer.isPlaying())
            mPlayer.stop();
    }
    private void playAudio(String fname, int position) {
        try {
            mPlayer.reset();
            mPlayer.setDataSource(fname);
            mPlayer.prepare();
            mPlayer.start();
            mPlayingPosition = position;
            mHandler.postDelayed(mProgressUpdater, 500);
            //trigger list refresh, this will make progressbar start updating if visible
//            this.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
            stopPlayback();
        }
    }


 /*   public String compressImage(String imageUri) {

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
            if (imgRatio < maxRatio) {               imgRatio = maxHeight / actualHeight;                actualWidth = (int) (imgRatio * actualWidth);               actualHeight = (int) maxHeight;             } else if (imgRatio > maxRatio) {
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
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
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
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;      }       final float totalPixels = width * height;       final float totalReqPixelsCap = reqWidth * reqHeight * 2;       while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }*/
}