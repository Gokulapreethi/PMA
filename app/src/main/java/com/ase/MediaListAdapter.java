package com.ase;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;
import com.ase.Forms.FormEntryViewActivity;
import com.ase.RandomNumber.Utility;
import com.ase.sketh.SwipeListview;
import com.squareup.picasso.Picasso;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import net.sf.andpdf.nio.ByteBuffer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.pjsip.pjsua2.app.GroupMemberAccess;
import org.pjsip.pjsua2.app.MainActivity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import Services.ShowOrCancelProgress;
import json.EnumJsonWebservicename;

/**
 * Created by saravanakumar on 6/9/2016.
 */
public class MediaListAdapter extends ArrayAdapter<TaskDetailsBean> {
    SwipeListview swipeDetector;
    private static ViewHolder holder;
    private final onClick listener;
    public static MediaPlayer mPlayer = new MediaPlayer();
    private ShowOrCancelProgress progressListener;
    ArrayList<TaskDetailsBean> detailsBeanArrayList, taskList_4;
    TaskDetailsBean bean;
    NewTaskConversation newTaskConversation;
    Handler handler;
    String StartDate, EndDate, FromId, ToId, Leave_Signal_Id, check, min, sec, category;
    public static Runnable updatetime = null;
    public static boolean stop = true;
    boolean play, isreceiver = false;
    String dir_path = Environment.getExternalStorageDirectory() + "/High Message/downloads/";
    int starttime, itemPosition = 0;
    public static ViewHolder oldHolder;
    SimpleDateFormat ft;
    private Context context;
    public GroupMemberAccess groupMemberAccess;
    private LayoutInflater inflater = null;
    private int mPlayingPosition = -1;
    private HashMap<String, Bitmap> cacheBitmap;
    public static String replyusername;
    public static String replytaskDescription;
    public static String mimetypeforreply = null;
    public String datepattern = getDeviceDatePattern(getContext());
    String action_name = null;


    public MediaListAdapter(Context context, ArrayList<TaskDetailsBean> userList, String s1, String s2, onClick listener) {    /********** Take passed values **********/
        super(context, R.layout.media_list_adapter, userList);

        this.context = context;
        this.detailsBeanArrayList = userList;
        this.check = s1;
        this.category = s2;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = listener;
        handler = new Handler();
        swipeDetector = new SwipeListview();
        ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss.SSS a zzz");
        progressListener = Appreference.main_Activity_context;
        cacheBitmap = new HashMap<String, Bitmap>();
        initCacheBitmap();
        updatetime = new Runnable() {
            @Override
            public void run() {
                try {
                    if (mPlayer.isPlaying()) {
                        Log.i("audio", String.valueOf(mPlayer.getCurrentPosition() / 1000));
                        holder.seekBar.setProgress(mPlayer.getCurrentPosition());
                        handler.postDelayed(updatetime, 100);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MediaListAdapter", "updatetime Exception: " + e.getMessage(), "WARN", null);
                }
            }
        };  /*********** Layout inflator to call external xml layout () ***********/
    }

    public void initCacheBitmap() {
        try {
            for (TaskDetailsBean detailsBean : detailsBeanArrayList) {
                if (detailsBean != null && detailsBean.getMimeType() != null
                        && (detailsBean.getMimeType().equalsIgnoreCase("video") || detailsBean.getMimeType().equalsIgnoreCase("image") || detailsBean.getMimeType().equalsIgnoreCase("sketch"))
                        && detailsBean.getTaskDescription() != null) {
                    if (!cacheBitmap.containsKey(detailsBean.getTaskDescription())) {
                        if (detailsBean.getMimeType().equalsIgnoreCase("image") || detailsBean.getMimeType().equalsIgnoreCase("sketch")) {
                            if (detailsBean.getFromUserName() != null && detailsBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                File imageFile;
                                if (detailsBean.getTaskDescription() != null && detailsBean.getTaskDescription().contains("High Message")) {
                                    imageFile = new File(detailsBean.getTaskDescription());
                                } else {
                                    imageFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "High Message/" + detailsBean.getTaskDescription());
                                }
                                if (imageFile.exists()) {
                                    final int THUMBSIZE = 125;
                                    Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(detailsBean.getTaskDescription()), THUMBSIZE, THUMBSIZE);
                                    cacheBitmap.put(detailsBean.getTaskDescription(), ThumbImage);
                                }
                            } else {
                                File imageFile = new File(dir_path + detailsBean.getTaskDescription());
                                if (imageFile.exists()) {
                                    final int THUMBSIZE = 125;
                                    Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(dir_path + detailsBean.getTaskDescription()), THUMBSIZE, THUMBSIZE);
                                    cacheBitmap.put(detailsBean.getTaskDescription(), ThumbImage);
                                }
                            }
                        } else if (detailsBean.getMimeType() != null && detailsBean.getMimeType().equalsIgnoreCase("video")) {
                            if (detailsBean.getFromUserName() != null && detailsBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                File imageFile = new File(detailsBean.getTaskDescription());
                                if (imageFile.exists()) {
                                    cacheBitmap.put(detailsBean.getTaskDescription(), ThumbnailUtils.createVideoThumbnail(detailsBean.getTaskDescription(), MediaStore.Video.Thumbnails.MICRO_KIND));
                                }
                            } else {
                                File imageFile = new File(dir_path + detailsBean.getTaskDescription());
                                if (imageFile.exists()) {
                                    cacheBitmap.put(detailsBean.getTaskDescription(), ThumbnailUtils.createVideoThumbnail((dir_path + detailsBean.getTaskDescription()), MediaStore.Video.Thumbnails.MICRO_KIND));
                                }
                            }
                        }
                    }
                }
            }
            progressListener.CancellProgress();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MediaListAdapter", "initCacheBitmap Exception: " + e.getMessage(), "WARN", null);
        }
    }


    public static String getDeviceDatePattern(Context applicationContext) {
        String date_pattern = null;
        try {
            Log.i("taskdateupdate", "111111 " + applicationContext);
            Format dateFormat = android.text.format.DateFormat.getDateFormat(applicationContext);
            date_pattern = ((SimpleDateFormat) dateFormat).toLocalizedPattern();
//            Toast.makeText(context,date_pattern,Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MediaListAdapter ", "getDevicePattern Exception " + e.getMessage(), "WARN", null);
        }
        Log.i("MediaListAdapter", "datepattern value is-->" + date_pattern);
        return date_pattern;
    }

    public void notifyImageThumb(TaskDetailsBean detailsBean) {

        try {
            if (detailsBean != null && detailsBean.getTaskDescription() != null) {
                String map_key = "";
                Iterator iterator1 = cacheBitmap.entrySet().iterator();
                while (iterator1.hasNext()) {
                    Map.Entry mapEntry = (Map.Entry) iterator1.next();
                    String key = (String) mapEntry.getKey();
                    if (key.contains(detailsBean.getTaskDescription())) {
                        map_key = key;
                    }
                }
                if (cacheBitmap.containsKey(detailsBean.getTaskDescription())) {
                    cacheBitmap.remove(detailsBean.getTaskDescription());
                } else if (cacheBitmap.containsKey(map_key)) {
                    cacheBitmap.remove(map_key);
                }
            }
            holder.ls_thumb_image.setImageBitmap(null);
            if (detailsBean != null && detailsBean.getMimeType() != null
                    && (detailsBean.getMimeType().equalsIgnoreCase("video") || detailsBean.getMimeType().equalsIgnoreCase("image") || detailsBean.getMimeType().equalsIgnoreCase("sketch") || detailsBean.getMimeType().equalsIgnoreCase("audio"))
                    && detailsBean.getTaskDescription() != null) {
                if (detailsBean.getMimeType().equalsIgnoreCase("image") || detailsBean.getMimeType().equalsIgnoreCase("sketch")) {
                    if (detailsBean.getFromUserName() != null && detailsBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        File imageFile;
                        if (detailsBean.getTaskDescription().contains("High Message")) {
                            imageFile = new File(detailsBean.getTaskDescription());
                        } else {
                            imageFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "High Message/" + detailsBean.getTaskDescription());
                        }
                        if (imageFile.exists()) {
                            final int THUMBSIZE = 125;
                            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(detailsBean.getTaskDescription()), THUMBSIZE, THUMBSIZE);
                            cacheBitmap.put(detailsBean.getTaskDescription(), ThumbImage);
                        } else {
                            holder.ls_thumb_image.setBackgroundResource(R.drawable.icon404);
                            Log.d("MediaAdapter", "404 image 1");
                        }
                    } else {
                        File imageFile = new File(dir_path + detailsBean.getTaskDescription());
                        if (imageFile.exists()) {
                            final int THUMBSIZE = 125;
                            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(dir_path + detailsBean.getTaskDescription()), THUMBSIZE, THUMBSIZE);
                            cacheBitmap.put(detailsBean.getTaskDescription(), ThumbImage);
                        } else {
                            holder.ls_thumb_image.setBackgroundResource(R.drawable.icon404);
                            Log.d("MediaAdapter", "404 image 2");
                        }
                    }
                } else if (detailsBean.getMimeType().equalsIgnoreCase("video")) {
                    if (detailsBean.getFromUserName() != null && detailsBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        File imageFile = new File(detailsBean.getTaskDescription());
                        if (imageFile.exists()) {
                            cacheBitmap.put(detailsBean.getTaskDescription(), ThumbnailUtils.createVideoThumbnail(detailsBean.getTaskDescription(), MediaStore.Video.Thumbnails.MICRO_KIND));
                        } else {
                            holder.ls_thumb_image.setBackgroundResource(R.drawable.icon404);
                            Log.d("MediaAdapter", "404 image 3");
                        }
                    } else {
                        File imageFile = new File(dir_path + detailsBean.getTaskDescription());
                        if (imageFile.exists()) {
                            cacheBitmap.put(detailsBean.getTaskDescription(), ThumbnailUtils.createVideoThumbnail((dir_path + detailsBean.getTaskDescription()), MediaStore.Video.Thumbnails.MICRO_KIND));
                        } else {
                            holder.ls_thumb_image.setBackgroundResource(R.drawable.icon404);
                            Log.d("MediaAdapter", "404 image 4");
                        }
                    }
                } else if (detailsBean.getMimeType().equalsIgnoreCase("audio")) {
                    if (detailsBean.getFromUserName() != null && detailsBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        File audiofile = new File(detailsBean.getTaskDescription());
                        if (audiofile.exists()) {
                            holder.ls_thumb_image.setBackgroundResource(R.drawable.audio_icon_new);
                        } else {
                            holder.ls_thumb_image.setBackgroundResource(R.drawable.icon404);
                            Log.d("MediaAdapter", "404 image 5");
                        }
                    } else {
                        File audiofile1 = new File(dir_path + detailsBean.getTaskDescription());
                        if (audiofile1.exists()) {
                            holder.ls_thumb_image.setBackgroundResource(R.drawable.audio_icon_new);
                        } else {
                            holder.ls_thumb_image.setBackgroundResource(R.drawable.icon404);
                            Log.d("MediaAdapter", "404 image 6");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MediaListAdapter", "notifyImageThumb Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public View getView(final int position, View view, ViewGroup parent) {  //Depends upon data size called for each row , Create each ListView row

        try {
            final TaskDetailsBean gcBean = detailsBeanArrayList.get(position);
            holder = new ViewHolder();
            Log.i("conv123", "getView MediaListAdapter------->  " );

            if (view == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.media_list_adapter, null);
//                holder.radio_ninjas = (RadioButton) view.findViewById(R.id.radio_ninjas);
//                holder.radio_layout = (LinearLayout) view.findViewById(R.id.radio_layout);
                holder.send_syn_txt = (TextView) view.findViewById(R.id.send_syn_txt);
                holder.rcv_syn_txt = (TextView) view.findViewById(R.id.rcv_syn_txt);
                holder.rcv_syn_img = (TextView) view.findViewById(R.id.rcv_syn_img);
                holder.send_syn_img = (TextView) view.findViewById(R.id.send_syn_img);
                holder.expand_remove_btn = (ImageView) view.findViewById(R.id.expand_remove_btn);
                holder.expand_txtstatus = (ImageView) view.findViewById(R.id.expand_txtstatus);
                holder.expandabletext_views = (RelativeLayout) view.findViewById(R.id.expandabletext_views);
                holder.ll_expandable_text_view_left = (LinearLayout) view.findViewById(R.id.ll_expandable_text_view_left);
                holder.ll_expandable_text_view_right = (LinearLayout) view.findViewById(R.id.ll_expandable_text_view_right);
                holder.ll_expandable_text_view_dependency = (LinearLayout) view.findViewById(R.id.ll_expandable_text_view_dependency);
                holder.expandableTextView_right = (ExpandableTextView) view.findViewById(R.id.expandableTextView_right);
                holder.buttonToggle_right = (TextView) view.findViewById(R.id.button_toggle_right);
                holder.expandableTextView_left = (ExpandableTextView) view.findViewById(R.id.expandableTextView_left);
                holder.buttonToggle_left = (TextView) view.findViewById(R.id.button_toggle_left);
                holder.cm = (Chronometer) view.findViewById(R.id.chronometer1);
                holder.rec_readmore = (TextView) view.findViewById(R.id.rec_readmore);
                holder.send_readmore = (TextView) view.findViewById(R.id.send_readmore);
                holder.time = (TextView) view.findViewById(R.id.tv_send_time);
                holder.receiver_name = (TextView) view.findViewById(R.id.receiver_name);
                holder.receiver_name2 = (TextView) view.findViewById(R.id.receiver_name2);
                holder.time_under_text = (TextView) view.findViewById(R.id.tv_pathname1);
                holder.time_under_image_receiver = (TextView) view.findViewById(R.id.tv_pathname);
                holder.button_toggle_dependency = (TextView) view.findViewById(R.id.button_toggle_dependency);
                holder.time_under_text_receiver = (TextView) view.findViewById(R.id.tv_pathname2);
                holder.time_1 = (TextView) view.findViewById(R.id.tv_send_time1);
                holder.timeview = (LinearLayout) view.findViewById(R.id.timeview);
                holder.leave = (ImageView) view.findViewById(R.id.leave);
                holder.dependency_icon = (ImageView) view.findViewById(R.id.dependency_icon);
                holder.media = (LinearLayout) view.findViewById(R.id.meaia);
                holder.receiver_side_doc_iconview = (ImageView) view.findViewById(R.id.receiver_side_doc_iconview);
                holder.receiver_side_location_iconview = (ImageView) view.findViewById(R.id.receiver_side_location_iconview);
                holder.textselect = (ImageView) view.findViewById(R.id.textselect);
                holder.imageselect = (ImageView) view.findViewById(R.id.imageselect);
                holder.sender_side_doc_iconview = (ImageView) view.findViewById(R.id.sender_side_doc_iconview);
                holder.sender_side_location_iconview = (ImageView) view.findViewById(R.id.sender_side_location_iconview);
                holder.ls_video_play_icon = (ImageView) view.findViewById(R.id.ls_video_play_icon);
                holder.ls_thumb_image = (ImageView) view.findViewById(R.id.ls_thumb_image);
                holder.ls_thumb_audio = (ImageView) view.findViewById(R.id.ls_thumb_audio);
                holder.iv_mmstatus = (ImageView) view.findViewById(R.id.iv_mmstatus);
                holder.video_play_icon = (ImageView) view.findViewById(R.id.video_play_icon);
                holder.thumb_image = (ImageView) view.findViewById(R.id.thumb_image);
                holder.thumb_audio = (ImageView) view.findViewById(R.id.thumb_audio);
                holder.iv_txtstatus = (ImageView) view.findViewById(R.id.iv_txtstatus);
                holder.remove_btn = (ImageView) view.findViewById(R.id.remove_btn);
                holder.xbutton = (ImageView) view.findViewById(R.id.xbutton);
                holder.play_button = (ImageView) view.findViewById(R.id.play_button);
                holder.receiver_side_doc_icon = (TextView) view.findViewById(R.id.receiver_side_doc_icon);
                holder.sender_msg_type = (TextView) view.findViewById(R.id.sender_msg_type);
                holder.sender_msg_type_image = (TextView) view.findViewById(R.id.sender_msg_type_image);
                holder.receiver_msg_type_image = (TextView) view.findViewById(R.id.receiver_msg_type_image);
                holder.receiver_msg_type = (TextView) view.findViewById(R.id.receiver_msg_type);
                holder.sender_side_doc_icon = (TextView) view.findViewById(R.id.sender_side_doc_icon);
                holder.rcv_des = (TextView) view.findViewById(R.id.rcv_des);
                holder.txt_des = (TextView) view.findViewById(R.id.txt_des);
                holder.txt_time = (TextView) view.findViewById(R.id.txt_time);
                holder.seekBar = (SeekBar) view.findViewById(R.id.seekBar1);
                holder.receiver_side_list_image = (RelativeLayout) view.findViewById(R.id.receiver_side_list_image);
                holder.text_header = (RelativeLayout) view.findViewById(R.id.text_header);
                holder.sender_side_list_image = (RelativeLayout) view.findViewById(R.id.sender_side_list_image);
                holder.both_side_list_image = (RelativeLayout) view.findViewById(R.id.both_side_list_image);
                holder.receiver_side_add_txt = (RelativeLayout) view.findViewById(R.id.receiver_side_add_txt);
                holder.txt_des_under = (LinearLayout) view.findViewById(R.id.txt_des_under);
                holder.receiver_side_description_layout = (LinearLayout) view.findViewById(R.id.rcv_des_layout);
                holder.sender_side_add_txt = (RelativeLayout) view.findViewById(R.id.sender_side_add_txt);
                holder.text_views = (RelativeLayout) view.findViewById(R.id.text_views);
                holder.ls_frame_layout = (FrameLayout) view.findViewById(R.id.ls_frame_layout);
                holder.frame_layout = (FrameLayout) view.findViewById(R.id.frame_layout);
                holder.progress_download = (ProgressBar) view.findViewById(R.id.progress_download);
                holder.progress_upload = (ProgressBar) view.findViewById(R.id.progress_upload);
                holder.dateChangeRequest_icon = (ImageView) view.findViewById(R.id.dateChangeRequest);
                holder.exclation_counter = (TextView) view.findViewById(R.id.exclation_counter);
                holder.dateChangeApproval_icon = (ImageView) view.findViewById(R.id.dateChangeApproval);
                holder.date_header_text = (TextView) view.findViewById(R.id.date_header_text);
                holder.dateChangeApproval_sender = (ImageView) view.findViewById(R.id.dateChangeApproval_sender);
                holder.receiver_msg_type.setVisibility(View.GONE);
                holder.receiver_msg_type_image.setVisibility(View.GONE);
                holder.sender_msg_type.setVisibility(View.GONE);
                holder.sender_msg_type_image.setVisibility(View.GONE);
                holder.sender_multimediaLayout = (RelativeLayout) view.findViewById(R.id.rl_multimediaLayout);
                holder.receiver_multimediaLayout = (RelativeLayout) view.findViewById(R.id.include_ls_thumb);
                holder.reply_sender_name = (TextView) view.findViewById(R.id.replymessage);
                holder.reply_sender_msg = (TextView) view.findViewById(R.id.replymessageforsender);
                holder.reply_receiver_name = (TextView) view.findViewById(R.id.receiverreplymessage);
                holder.reply_receiver_msg = (TextView) view.findViewById(R.id.receiverreplymessageforforward);
                holder.reply_sender_image = (ImageView) view.findViewById(R.id.replyimage);
                holder.reply_receiver_image = (ImageView) view.findViewById(R.id.receiverreplyimage);
                holder.reply_receiver_image.setVisibility(View.GONE);
                holder.reply_receiver_linear = (LinearLayout) view.findViewById(R.id.receiverlinearforimage);
                holder.reply_receiver_linear.setVisibility(View.GONE);
                holder.reply_sender_linear = (LinearLayout) view.findViewById(R.id.linearforimage);
                holder.reply_sender_linear.setVisibility(View.GONE);
                holder.dateChangeApproval_sender.setVisibility(View.GONE);
                view.setTag(holder);
                newTaskConversation = new NewTaskConversation();
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.receiver_name_name = null;
            holder.receiver_name_name = VideoCallDataBase.getDB(context).getName(gcBean.getFromUserName());
            holder.text_views.setVisibility(View.GONE);
            holder.date_header_text.setVisibility(View.VISIBLE);
            holder.rcv_syn_txt.setVisibility(View.GONE);
            holder.send_syn_txt.setVisibility(View.GONE);
            holder.send_syn_img.setVisibility(View.GONE);
            holder.rcv_syn_img.setVisibility(View.GONE);
            holder.expand_txtstatus.setVisibility(View.GONE);
            holder.expand_remove_btn.setVisibility(View.GONE);
            holder.expandableTextView_left.setVisibility(View.GONE);
            holder.buttonToggle_left.setVisibility(View.GONE);
            holder.expandabletext_views.setVisibility(View.GONE);
            holder.ll_expandable_text_view_right.setVisibility(View.GONE);
            holder.ll_expandable_text_view_dependency.setVisibility(View.GONE);
            holder.ll_expandable_text_view_left.setVisibility(View.GONE);
            holder.buttonToggle_right.setVisibility(View.GONE);
            holder.buttonToggle_left.setVisibility(View.GONE);
            holder.expandableTextView_right.setVisibility(View.GONE);
            holder.expandableTextView_left.setVisibility(View.GONE);
            holder.send_readmore.setVisibility(View.GONE);
            holder.rec_readmore.setVisibility(View.GONE);
            holder.dateChangeApproval_icon.setVisibility(View.GONE);
            holder.exclation_counter.setVisibility(View.GONE);
            holder.dateChangeRequest_icon.setVisibility(View.GONE);
            holder.dateChangeApproval_sender.setVisibility(View.GONE);
            holder.both_side_list_image.setVisibility(View.GONE);
            holder.leave.setVisibility(View.GONE);
            holder.dependency_icon.setVisibility(View.GONE);
            holder.time_1.setVisibility(View.GONE);
            holder.time.setVisibility(View.GONE);
            holder.progress_upload.setVisibility(View.GONE);
            holder.sender_side_location_iconview.setVisibility(View.GONE);
            holder.receiver_side_location_iconview.setVisibility(View.GONE);
            holder.ls_thumb_audio.setVisibility(View.GONE);
            holder.thumb_audio.setVisibility(View.GONE);
            final ViewHolder finalHolder1 = holder;

            Date d1 = null;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                d1 = format.parse(gcBean.getDateTime().split(" ")[0]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String newdate = sdf.format(d1);
                if (position == 0) {
                    holder.text_header.setVisibility(View.VISIBLE);
                    holder.date_header_text.setText(newdate);
                }

                if (position > 0) {
                    final TaskDetailsBean bean = detailsBeanArrayList.get(position - 1);
                    if (bean.getDateTime().split(" ")[0].equals(gcBean.getDateTime().split(" ")[0])) {
                        holder.text_header.setVisibility(View.GONE);
                    } else {
                        holder.text_header.setVisibility(View.VISIBLE);
                        if (bean.getDateTime().split(" ")[0].equals(gcBean.getDateTime().split(" ")[0])) {
                            holder.date_header_text.setText("TODAY");
                        } else {
                            holder.date_header_text.setText(newdate);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MediaListAdapter", "DisplayDate Exception: " + e.getMessage(), "WARN", null);
            }

            /*try {
                WindowManager wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                DisplayMetrics metrics = new DisplayMetrics();
                display.getMetrics(metrics);
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;
                Log.i("DisplaySize"," -------- width --------- > "+width);
                Log.i("DisplaySize"," --------- height -------- > "+height);
                LinearLayout.LayoutParams lParams1 = (LinearLayout.LayoutParams) holder.txt_des_under.getLayoutParams();
                lParams1.width = width - 20;
                holder.txt_des_under.setLayoutParams(lParams1);
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            try {
                groupMemberAccess = VideoCallDataBase.getDB(context).getMemberAccessList(gcBean.getToUserId());
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MediaListAdapter", "groupMemberAccess Exception: " + e.getMessage(), "WARN", null);
            }
            try {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (swipeDetector.swipeDetected()) {
                            if (swipeDetector.getAction() == SwipeListview.Action.RL) {
                                NewTaskConversation.getInstance().moveGraph("R");
                            }
                        }
                        try {
                            listener.onClick(gcBean, finalHolder1, position, v);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            Appreference.printLog("MediaListAdapter", "swipeDetector Exception: " + e.getMessage(), "WARN", null);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("MediaListAdapter", "swipeDetector Exception: " + e.getMessage(), "WARN", null);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MediaListAdapter", "swipeDetector Exception: " + e.getMessage(), "WARN", null);
            }
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        listener.onLongClick(position, v);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "setOnLongClickListener Exception: " + e.getMessage(), "WARN", null);
                    }
                    return true;
                }
            });
            view.setLongClickable(true);
            view.setOnTouchListener(swipeDetector);
            try {
                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        onComplete();
                        oldHolder.play_button.setImageResource(R.drawable.play);
                        if (holder.cm != null) {
                            holder.cm.stop();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MediaListAdapter", "setOnCompletionListener Exception: " + e.getMessage(), "WARN", null);
            }
           /* if (Appreference.temconvert) {
                holder.radio_ninjas.setVisibility(View.VISIBLE);
            } else {
                holder.radio_ninjas.setVisibility(View.GONE);
            }
            holder.radio_ninjas.setChecked(gcBean.isSelect());*/
           /* holder.radio_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        boolean sel = !detailsBeanArrayList.get(position).isSelect();
                        detailsBeanArrayList.get(position).setSelect(sel);
                        notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "radio_layout Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });*/
            holder.send_syn_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        for (TaskDetailsBean detailsBean : detailsBeanArrayList) {
                            if (detailsBean.getSignalid() != null && detailsBean.getSignalid().equalsIgnoreCase(gcBean.getSignalid())) {
                                detailsBean.setSyncEnable("disable");
                                break;
                            }
                        }
                        VideoCallDataBase.getDB(context).sync_status(gcBean.getSignalid());
                        ((NewTaskConversation) context).gettaskwebservice();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "send_syn_txt Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });
            holder.rcv_syn_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        for (TaskDetailsBean detailsBean : detailsBeanArrayList) {
                            if (detailsBean.getSignalid() != null && detailsBean.getSignalid().equalsIgnoreCase(gcBean.getSignalid())) {
                                detailsBean.setSyncEnable("disable");
                                break;
                            }
                        }
                        VideoCallDataBase.getDB(context).sync_status(gcBean.getSignalid());
                        ((NewTaskConversation) context).gettaskwebservice();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "rcv_syn_txt Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });
            holder.send_syn_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        for (TaskDetailsBean detailsBean : detailsBeanArrayList) {
                            if (detailsBean.getSignalid() != null && detailsBean.getSignalid().equalsIgnoreCase(gcBean.getSignalid())) {
                                detailsBean.setSyncEnable("disable");
                                break;
                            }
                        }
                        VideoCallDataBase.getDB(context).sync_status(gcBean.getSignalid());
                        ((NewTaskConversation) context).gettaskwebservice();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "send_syn_img Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });
            holder.rcv_syn_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        for (TaskDetailsBean detailsBean : detailsBeanArrayList) {
                            if (detailsBean.getSignalid() != null && detailsBean.getSignalid().equalsIgnoreCase(gcBean.getSignalid())) {
                                detailsBean.setSyncEnable("disable");
                                break;
                            }
                        }
                        VideoCallDataBase.getDB(context).sync_status(gcBean.getSignalid());
                        ((NewTaskConversation) context).gettaskwebservice();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "rcv_syn_img Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });
            holder.dependency_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dependency_scheduling_alert);

                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        lp.horizontalMargin = 15;
                        Window window = dialog.getWindow();
                        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        window.setAttributes(lp);
                        window.setGravity(Gravity.CENTER);
                        dialog.show();
                        TextView heading = (TextView) dialog.findViewById(R.id.heading);
                        TextView content = (TextView) dialog.findViewById(R.id.content);
                        TextView dependency_resolve = (TextView) dialog.findViewById(R.id.dependency_resolve);
                        TextView dependency_cancel = (TextView) dialog.findViewById(R.id.dependency_cancel);
                        dependency_resolve.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    dialog.dismiss();
                                    Intent intent = new Intent(context, UpdateTaskActivity.class);
                                    intent.putExtra("Str", "conversation");
                                    intent.putExtra("task", "dependency");
                                    intent.putExtra("bean", gcBean);
                                    intent.putExtra("ownerOfTask", gcBean.getOwnerOfTask());
                                    intent.putExtra("category", category);
                                    Log.i("medialistAdapter", "dependency taskID " + gcBean.getTaskId());
                                    Log.i("medialistAdapter", "dependency signalId" + gcBean.getSignalid());
                                    ((Activity) context).startActivityForResult(intent, 212);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("MediaListAdapter", "dependency_resolve Exception: " + e.getMessage(), "WARN", null);
                                }
                            }
                        });
                        dependency_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("MediaListAdapter", "dependency_cancel Exception: " + e.getMessage(), "WARN", null);
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "dependency_icon Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });
            holder.leave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (gcBean.getSubType() != null && !gcBean.getSubType().equalsIgnoreCase("") && (gcBean.getSubType().equalsIgnoreCase("attendance_in") || gcBean.getSubType().equalsIgnoreCase("attendance_out") || gcBean.getSubType().equalsIgnoreCase("rate") || gcBean.getSubType().equalsIgnoreCase("work") || gcBean.getSubType().equalsIgnoreCase("overtime"))) {
                            final Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.leave_approval_or_reject);

                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(dialog.getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                            lp.horizontalMargin = 15;
                            Window window = dialog.getWindow();
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            window.setAttributes(lp);
                            window.setGravity(Gravity.CENTER);
                            dialog.show();
                            TextView heading = (TextView) dialog.findViewById(R.id.heading);
                            TextView content = (TextView) dialog.findViewById(R.id.content);
                            if (gcBean.getSubType().equalsIgnoreCase("attendance_in")) {
                                heading.setText("Approve Attendance In Time");
                                content.setText("Would you like to Approve the Attendance In Time?");
                            } else if (gcBean.getSubType().equalsIgnoreCase("attendance_out")) {
                                heading.setText("Approve Attendance Out Time");
                                content.setText("Would you like to Approve the Attendance Out Time?");
                            } else if (gcBean.getSubType().equalsIgnoreCase("rate")) {
                                heading.setText("Approve Rates");
                                content.setText("Would you like to Approve the Rates?");
                            } else if (gcBean.getSubType().equalsIgnoreCase("work")) {
                                heading.setText("Approve Work ");
                                content.setText("Would you like to Approve the Work Time?");
                            } else if (gcBean.getSubType().equalsIgnoreCase("overtime")) {
                                heading.setText("Approve OverTime");
                                content.setText("Would you like to Approve the OverTime?");
                            } else if (gcBean.getSubType().equalsIgnoreCase("m/c_breakdown")) {
                                heading.setText("Approve M/C Breakdown");
                                content.setText("Would you like to Approve the M/C Breakdown Time?");
                            }
                            TextView approve = (TextView) dialog.findViewById(R.id.approve);
                            approve.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        dialog.dismiss();
                                        for (TaskDetailsBean detailsBean : detailsBeanArrayList) {
                                            if (detailsBean.getSignalid() != null && detailsBean.getSignalid().equalsIgnoreCase(gcBean.getSignalid())) {
                                                detailsBean.setMsg_status(9);
                                                break;
                                            }
                                        }
                                        VideoCallDataBase.getDB(context).leaveMsg_Status(gcBean.getSignalid());
                                        String sig_id = Utility.getSessionID();
                                        if (!getContext().getResources().getString(R.string.proxyua).equalsIgnoreCase("enable")) {
                                            ((NewTaskConversation) context).PercentageWebService("text", gcBean.getSubType() + " Approved", "", sig_id, 0);
                                        } else {
                                            ((NewTaskConversation) context).sendMessage(gcBean.getSubType() + " Approved", null, "text", null, "", sig_id, null);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Appreference.printLog("MediaListAdapter", "leave Exception: " + e.getMessage(), "WARN", null);
                                    }
                                }
                            });
                            TextView reject = (TextView) dialog.findViewById(R.id.reject);
                            reject.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        dialog.dismiss();
                                        for (TaskDetailsBean detailsBean : detailsBeanArrayList) {
                                            if (detailsBean.getSignalid() != null && detailsBean.getSignalid().equalsIgnoreCase(gcBean.getSignalid())) {
                                                detailsBean.setMsg_status(9);
                                                break;
                                            }
                                        }
                                        VideoCallDataBase.getDB(context).leaveMsg_Status(gcBean.getSignalid());
                                        String sig_id = Utility.getSessionID();
                                        if (!getContext().getResources().getString(R.string.proxyua).equalsIgnoreCase("enable")) {
                                            ((NewTaskConversation) context).PercentageWebService("text", gcBean.getSubType() + " has been rejected", "", sig_id, 0);
                                        } else {
                                            ((NewTaskConversation) context).sendMessage(gcBean.getSubType() + " has been rejected", null, "text", null, "", sig_id, null);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Appreference.printLog("MediaListAdapter", "leave Exception: " + e.getMessage(), "WARN", null);
                                    }
                                }
                            });
                            TextView cancel1_req = (TextView) dialog.findViewById(R.id.cancel1);
                            cancel1_req.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        dialog.dismiss();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Appreference.printLog("MediaListAdapter", "leave Exception: " + e.getMessage(), "WARN", null);
                                    }
                                }
                            });
                        } else if (Appreference.conflicttask) {
                            Appreference.isLeaveConflict = true;
                            String queryy;
                            if (gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("Group")) {
                                queryy = "select * from taskDetailsInfo where (fromUserName='" + gcBean.getFromUserName() + "' or toUserName='" + Appreference.loginuserdetails.getUsername() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "'and taskId='" + gcBean.getTaskId() + "' and mimeType='leaveRequest' order by id desc";
                            } else {
                                queryy = "select * from taskDetailsInfo where (fromUserName='" + Appreference.loginuserdetails.getUsername() + "' or toUserName='" + Appreference.loginuserdetails.getUsername() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "'and taskId='" + gcBean.getTaskId() + "' and mimeType='leaveRequest' order by id desc";
                            }
                            taskList_4 = VideoCallDataBase.getDB(context).getTaskHistory(queryy);
                            if (taskList_4.size() > 0) {
                                bean = taskList_4.get(0);
                                Leave_Signal_Id = bean.getSignalid();
                                StartDate = bean.getPlannedStartDateTime();
                                FromId = bean.getFromUserId();
                                ToId = bean.getToUserId();
                                EndDate = bean.getPlannedEndDateTime();
                            }
                            Appreference.isAssignLeave = true;
                            ((NewTaskConversation) context).confictMedialistWebservice(StartDate, EndDate, FromId, ToId, Leave_Signal_Id, gcBean.getTaskType());
                        } else {
                            final Dialog dialog = new Dialog(context);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.leave_approval_or_reject);
                            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                            lp.copyFrom(dialog.getWindow().getAttributes());
                            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                            lp.horizontalMargin = 15;
                            Window window = dialog.getWindow();
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            window.setAttributes(lp);
                            window.setGravity(Gravity.CENTER);
                            dialog.show();
                            TextView approve = (TextView) dialog.findViewById(R.id.approve);
                            approve.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        dialog.dismiss();
                                        for (TaskDetailsBean detailsBean : detailsBeanArrayList) {
                                            if (detailsBean.getSignalid() != null && detailsBean.getSignalid().equalsIgnoreCase(gcBean.getSignalid())) {
                                                detailsBean.setMsg_status(9);
                                                break;
                                            }
                                        }
                                        VideoCallDataBase.getDB(context).leaveMsg_Status(gcBean.getSignalid());
                                        String sig_id = Utility.getSessionID();
                                        if (!getContext().getResources().getString(R.string.proxyua).equalsIgnoreCase("enable")) {
                                            ((NewTaskConversation) context).PercentageWebService("text", "Leave Approved", "", sig_id, 0);
                                        } else {
                                            ((NewTaskConversation) context).sendMessage("Leave Approved", null, "text", null, "", sig_id, null);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Appreference.printLog("MediaListAdapter", "leave Exception: " + e.getMessage(), "WARN", null);
                                    }
                                }
                            });
                            TextView reject = (TextView) dialog.findViewById(R.id.reject);
                            reject.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        dialog.dismiss();
                                        for (TaskDetailsBean detailsBean : detailsBeanArrayList) {
                                            if (detailsBean.getSignalid() != null && detailsBean.getSignalid().equalsIgnoreCase(gcBean.getSignalid())) {
                                                detailsBean.setMsg_status(9);
                                                break;
                                            }
                                        }
                                        VideoCallDataBase.getDB(context).leaveMsg_Status(gcBean.getSignalid());
                                        String sig_id = Utility.getSessionID();
                                        if (!getContext().getResources().getString(R.string.proxyua).equalsIgnoreCase("enable")) {
                                            ((NewTaskConversation) context).PercentageWebService("text", "leave has been rejected", "", sig_id, 0);
                                        } else {
                                            ((NewTaskConversation) context).sendMessage("leave has been rejected", null, "text", null, "", sig_id, null);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Appreference.printLog("MediaListAdapter", "leave Exception: " + e.getMessage(), "WARN", null);
                                    }
                                }
                            });
                            TextView cancel1_req = (TextView) dialog.findViewById(R.id.cancel1);
                            cancel1_req.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        dialog.dismiss();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Appreference.printLog("MediaListAdapter", "leave Exception: " + e.getMessage(), "WARN", null);
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "leave Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });

        /*    holder.rcv_des.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TaskDetailsBean taskDetailsBean = gcBean;
                    Log.d("Receiver","subType == "+gcBean.getSubType());
                    if (taskDetailsBean.getTaskPriority().equals("High")) {
                        if (gcBean.getTaskStatus().equals("reminder")) {
                            int a = VideoCallDataBase.getDB(context).percentagechecker(taskDetailsBean.getTaskId());
                            String percentage = String.valueOf(a);
                            if (taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
                                a = VideoCallDataBase.getDB(context).GroupPercentageChecker(taskDetailsBean.getToUserName(), taskDetailsBean.getTaskId(), taskDetailsBean.getOwnerOfTask());
                                percentage = String.valueOf(a);
                            }
                            Intent intent = new Intent(context, UpdateTaskActivity.class);
                            intent.putExtra("Str", "conversation");
                            intent.putExtra("level", percentage);
                            ((Activity) context).startActivityForResult(intent, 210);
                        }
                    }else if(gcBean.getSubType().equalsIgnoreCase("formDetailsChangeRequest")){

                        Intent formintent = new Intent(context, FormsListActivity.class);
                        formintent.putExtra("FormsList", new ArrayList<FormsListBean>());
                        formintent.putExtra("TaskId", gcBean.getTaskId());
                        formintent.putExtra("webformcheck", "false");
//                        formintent.putExtra("UserList", listOfObservers);
//                        formintent.putExtra("TaskBean", beanValue());
                        ((Activity) context).startActivity(formintent);

                    } else if (taskDetailsBean.getTaskPriority().equals("low")) {
                    }
                }
            });*/

            holder.receiver_side_description_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        TaskDetailsBean taskDetailsBean = gcBean;
                        Log.d("Receiver", "TaskStatus ##  == " + gcBean.getTaskStatus() + " priority " + taskDetailsBean.getTaskPriority());
                        String receiver_side_des_status = "";
                        if (gcBean.getProjectId() != null) {
                            receiver_side_des_status = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskStatus from projectHistory where taskId='" + gcBean.getTaskId() + "'");
                        } else {
                            receiver_side_des_status = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskStatus from taskHistoryInfo where taskId='" + gcBean.getTaskId() + "'");
                        }
                        if (taskDetailsBean.getTaskPriority() != null && taskDetailsBean.getTaskPriority().equals("High")) {
                            if (gcBean.getTaskStatus().equals("reminder")) {
                                if (receiver_side_des_status != null && !receiver_side_des_status.equalsIgnoreCase("") && !receiver_side_des_status.equalsIgnoreCase("abandoned")) {
                                    String query = "select signalId from taskDetailsInfo where taskId ='" + gcBean.getTaskId() + "' and mimeType = 'reminder' order by id DESC LIMIT 1";
                                    String signalId = VideoCallDataBase.getDB(context).getProjectParentTaskId(query);
                                    String query_for = "", taskMember_or_Receiver = "";
                                    if (gcBean.getProjectId() != null && !gcBean.getProjectId().equalsIgnoreCase("") && !gcBean.getProjectId().equalsIgnoreCase("null")) {
                                        query_for = "select taskMemberList from projectHistory where taskId='" + gcBean.getTaskId() + "'";
                                    } else {
                                        if (gcBean.getTaskType() != null && !gcBean.getTaskType().equalsIgnoreCase("Group")) {
                                            query_for = "select taskReceiver from taskHistoryInfo where taskId='" + gcBean.getTaskId() + "'";
                                        } else {
                                            query_for = "select taskMemberList from taskHistoryInfo where taskId='" + gcBean.getTaskId() + "'";
                                        }
                                    }
                                    try {
                                        taskMember_or_Receiver = VideoCallDataBase.getDB(context).getProjectParentTaskId(query_for);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Appreference.printLog("MediaListAdapter", "taskMember_or_Receiver Exception: " + e.getMessage(), "WARN", null);
                                    }
                                    if ((taskMember_or_Receiver != null && taskMember_or_Receiver.contains(Appreference.loginuserdetails.getUsername())) || gcBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                        if (signalId != null && signalId.equalsIgnoreCase(gcBean.getSignalid())) {
                                            int a = VideoCallDataBase.getDB(context).percentagechecker(taskDetailsBean.getTaskId());
                                            String percentage = String.valueOf(a);
                                            try {
                                                if (taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
                                                    a = VideoCallDataBase.getDB(context).GroupPercentageChecker(taskDetailsBean.getToUserName(), taskDetailsBean.getTaskId(), taskDetailsBean.getOwnerOfTask());
                                                    percentage = String.valueOf(a);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Appreference.printLog("MediaListAdapter", "GroupPercentageChecker Exception: " + e.getMessage(), "WARN", null);
                                            }
                                            if (receiver_side_des_status != null && !receiver_side_des_status.equalsIgnoreCase("") && !receiver_side_des_status.equalsIgnoreCase("Closed")) {
                                                Intent intent = new Intent(context, UpdateTaskActivity.class);
                                                intent.putExtra("Str", "conversation");
                                                intent.putExtra("level", percentage);
                                                intent.putExtra("ownerOfTask", taskDetailsBean.getOwnerOfTask());
                                                intent.putExtra("category", category);
                                                ((Activity) context).startActivityForResult(intent, 210);
                                            } else {
                                                Toast.makeText(context, "This " + category + " is already Closed ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } else {
                                    }
                                } else {
                                    Toast.makeText(context, "Unable to change percentage task is in abandoned state ", Toast.LENGTH_SHORT).show();
                                }

                            }
                        } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("formDetailsChangeRequest")) {
                            TaskDetailsBean taskDetailsBean1 = gcBean;
                            Intent intent = new Intent(context, FormEntryViewActivity.class);
                            //                intent.putExtra("FormsMapValue", formFieldsBeenMap);
                            //                intent.putExtra("FormsListValue", setIdValueList);
                            intent.putExtra("FormsId", String.valueOf(taskDetailsBean1.getCustomTagId()));
                            intent.putExtra("setId", String.valueOf(taskDetailsBean1.getCustomSetId()));
                            intent.putExtra("TaskId", taskDetailsBean1.getTaskId());
                            intent.putExtra("TaskNo", taskDetailsBean1.getTaskNo());
                            intent.putExtra("TaskName", taskDetailsBean1.getTaskName());
                            intent.putExtra("EntryMode", "multi");
                            intent.putExtra("EntryWay", "Adapter");
                            intent.putExtra("UserList", NewTaskConversation.listOfObservers);
                            intent.putExtra("TaskBean", taskDetailsBean1);
                            context.startActivity(intent);
                        } else if (taskDetailsBean.getTaskPriority() != null && taskDetailsBean.getTaskPriority().equals("low")) {
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "rcv_des Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });
            holder.rcv_des.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        TaskDetailsBean taskDetailsBean = gcBean;
                        Log.d("Receiver", "TaskStatus !! == " + gcBean.getTaskStatus() + " priority " + taskDetailsBean.getTaskPriority());
                        String rcv_des_status = "";
                        if (gcBean.getProjectId() != null && !gcBean.getProjectId().equalsIgnoreCase("") && !gcBean.getProjectId().equalsIgnoreCase("null")) {
                            rcv_des_status = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskStatus from projectHistory where taskId='" + gcBean.getTaskId() + "'");
                        } else {
                            rcv_des_status = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskStatus from taskHistoryInfo where taskId='" + gcBean.getTaskId() + "'");
                        }

                        if (taskDetailsBean.getTaskPriority() != null && taskDetailsBean.getTaskPriority().equals("High")) {
                            if (gcBean.getTaskStatus().equals("reminder")) {
                                if (rcv_des_status != null && !rcv_des_status.equalsIgnoreCase("") && !rcv_des_status.equalsIgnoreCase("abandoned")) {
                                    String query = "select signalId from taskDetailsInfo where taskId ='" + gcBean.getTaskId() + "' and mimeType = 'reminder' order by id DESC LIMIT 1";
                                    String signalId = VideoCallDataBase.getDB(context).getProjectParentTaskId(query);
                                    String query_for = "", taskMember_or_Receiver = "";
                                    if (gcBean.getProjectId() != null && !gcBean.getProjectId().equalsIgnoreCase("") && !gcBean.getProjectId().equalsIgnoreCase("null")) {
                                        query_for = "select taskMemberList from projectHistory where taskId='" + gcBean.getTaskId() + "'";
                                    } else {
                                        if (gcBean.getTaskType() != null && !gcBean.getTaskType().equalsIgnoreCase("Group")) {
                                            query_for = "select taskReceiver from taskHistoryInfo where taskId='" + gcBean.getTaskId() + "'";
                                        } else {
                                            query_for = "select taskMemberList from taskHistoryInfo where taskId='" + gcBean.getTaskId() + "'";
                                        }
                                    }
                                    try {
                                        taskMember_or_Receiver = VideoCallDataBase.getDB(context).getProjectParentTaskId(query_for);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Appreference.printLog("MediaListAdapter", "taskMember_or_Receiver Exception: " + e.getMessage(), "WARN", null);
                                    }
                                    if ((taskMember_or_Receiver != null && taskMember_or_Receiver.contains(Appreference.loginuserdetails.getUsername())) || gcBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                        if (signalId != null && signalId.equalsIgnoreCase(gcBean.getSignalid())) {
                                            int a = VideoCallDataBase.getDB(context).percentagechecker(taskDetailsBean.getTaskId());
                                            String percentage = String.valueOf(a);
                                            if (taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
                                                a = VideoCallDataBase.getDB(context).GroupPercentageChecker(taskDetailsBean.getToUserName(), taskDetailsBean.getTaskId(), taskDetailsBean.getOwnerOfTask());
                                                percentage = String.valueOf(a);
                                            }
                                            if (rcv_des_status != null && !rcv_des_status.equalsIgnoreCase("") && !rcv_des_status.equalsIgnoreCase("Closed")) {
                                                Intent intent = new Intent(context, UpdateTaskActivity.class);
                                                intent.putExtra("Str", "conversation");
                                                intent.putExtra("level", percentage);
                                                intent.putExtra("ownerOfTask", taskDetailsBean.getOwnerOfTask());
                                                intent.putExtra("category", category);
                                                ((Activity) context).startActivityForResult(intent, 210);
                                            } else {
                                                Toast.makeText(context, "This " + category + " is already Closed ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } else {

                                    }
                                } else {
                                    Toast.makeText(context, "Unable to change percentage task is in abandoned state ", Toast.LENGTH_SHORT).show();
                                }

                            }
                        } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("formDetailsChangeRequest")) {

                            TaskDetailsBean taskDetailsBean1 = gcBean;
                            Log.i("MediaListAdapter", "from UserName == " + taskDetailsBean1.getFromUserName() + " From userId== " + taskDetailsBean1.getFromUserId() + "\n" +
                                    "Touser Name == " + taskDetailsBean1.getToUserName() + " toUser Id == " + taskDetailsBean1.getToUserId() + "\n" +
                                    "  FormId == " + taskDetailsBean1.getCustomTagId() + "  setId == " + taskDetailsBean1.getCustomSetId());

                            Intent intent = new Intent(context, FormEntryViewActivity.class);
                            //                intent.putExtra("FormsMapValue", formFieldsBeenMap);
                            //                intent.putExtra("FormsListValue", setIdValueList);
                            intent.putExtra("FormsId", String.valueOf(taskDetailsBean1.getCustomTagId()));
                            intent.putExtra("setId", String.valueOf(taskDetailsBean1.getCustomSetId()));
                            intent.putExtra("TaskId", taskDetailsBean1.getTaskId());
                            intent.putExtra("TaskNo", taskDetailsBean1.getTaskNo());
                            intent.putExtra("TaskName", taskDetailsBean1.getTaskName());
                            intent.putExtra("EntryMode", "multi");
                            intent.putExtra("EntryWay", "Adapter");
                            intent.putExtra("UserList", NewTaskConversation.listOfObservers);
                            intent.putExtra("TaskBean", taskDetailsBean1);
                            ((Activity) context).startActivity(intent);

                        } else if (taskDetailsBean.getTaskPriority() != null && taskDetailsBean.getTaskPriority().equals("low")) {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "rcv_des Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });

            holder.txt_des.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        TaskDetailsBean taskDetailsBean = gcBean;
                        Log.d("Receiver", "TaskStatus **  == " + gcBean.getTaskStatus() + " priority " + taskDetailsBean.getTaskPriority());
                        String txt_des_status = "";
                        if (gcBean.getProjectId() != null && !gcBean.getProjectId().equalsIgnoreCase("") && !gcBean.getProjectId().equalsIgnoreCase("null")) {
                            txt_des_status = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskStatus from projectHistory where taskId='" + gcBean.getTaskId() + "'");
                        } else {
                            txt_des_status = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskStatus from taskHistoryInfo where taskId='" + gcBean.getTaskId() + "'");
                        }

                        if (taskDetailsBean.getTaskPriority() != null && taskDetailsBean.getTaskPriority().equals("High")) {
                            if (gcBean.getTaskStatus().equals("reminder")) {
                                if (txt_des_status != null && !txt_des_status.equalsIgnoreCase("") && !txt_des_status.equalsIgnoreCase("Closed")) {
                                    int a = VideoCallDataBase.getDB(context).percentagechecker(taskDetailsBean.getTaskId());
                                    String percentage = String.valueOf(a);
                                    if (taskDetailsBean.getTaskType() != null && taskDetailsBean.getTaskType().equalsIgnoreCase("Group")) {
                                        a = VideoCallDataBase.getDB(context).GroupPercentageChecker(taskDetailsBean.getToUserName(), taskDetailsBean.getTaskId(), taskDetailsBean.getOwnerOfTask());
                                        percentage = String.valueOf(a);
                                    }
                                    Intent intent = new Intent(context, UpdateTaskActivity.class);
                                    intent.putExtra("Str", "conversation");
                                    intent.putExtra("level", percentage);
                                    intent.putExtra("ownerOfTask", taskDetailsBean.getOwnerOfTask());
                                    intent.putExtra("category", category);
                                    ((Activity) context).startActivityForResult(intent, 210);
                                } else {
                                    Toast.makeText(context, "This " + category + " is already Closed ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "txt_des Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });
            holder.sender_side_doc_iconview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                itemPosition = position;
                                if (gcBean.getMsg_status() == 0 && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                                    ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                    TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media resend */
                                    editmenu(taskDetailsBeanclick, holder.sender_side_doc_iconview, context);
                                } else if (gcBean.getMsg_status() != 0 && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && !gcBean.getTaskDescription().equalsIgnoreCase("Message has been Removed")) {
                                    String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                                    ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                    TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media withdraw */
                                    editmenu(taskDetailsBeanclick, holder.sender_side_doc_iconview, context);
                                } else {
                                    String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                                    ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                    TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media delete */
                                    editmenu(taskDetailsBeanclick, holder.sender_side_doc_iconview, context);
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "sender_side_doc_iconview Exception: " + e.getMessage(), "WARN", null);
                    }
                    return false;
                }
            });
            holder.receiver_side_doc_iconview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                itemPosition = position;
                                isreceiver = true;
                                String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                                ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media delete */
                                editmenu(taskDetailsBeanclick, holder.receiver_side_doc_iconview, context);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "receiver_side_doc_iconview Exception: " + e.getMessage(), "WARN", null);
                    }
                    return false;
                }
            });
            holder.sender_side_location_iconview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            itemPosition = position;
                            try {
                                if (gcBean.getMsg_status() == 0 && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                                    ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                    TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media resend */
                                    editmenu(taskDetailsBeanclick, holder.sender_side_location_iconview, context);
                                } else if (gcBean.getMsg_status() != 0 && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && !gcBean.getTaskDescription().equalsIgnoreCase("Message has been Removed")) {
                                    String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                                    ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                    TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media withdraw */
                                    editmenu(taskDetailsBeanclick, holder.sender_side_location_iconview, context);
                                } else {
                                    String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                                    ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                    TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media delete */
                                    editmenu(taskDetailsBeanclick, holder.sender_side_location_iconview, context);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("MediaListAdapter", "sender_side_location_iconview Exception: " + e.getMessage(), "WARN", null);
                            }
                        }
                    });
                    return false;
                }
            });
            holder.receiver_side_location_iconview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                itemPosition = position;
                                isreceiver = true;
                                String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                                ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media delete */
                                editmenu(taskDetailsBeanclick, holder.receiver_side_location_iconview, context);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("MediaListAdapter", "receiver_side_location_iconview Exception: " + e.getMessage(), "WARN", null);
                            }
                        }
                    });
                    return false;
                }
            });
            holder.thumb_audio.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                itemPosition = position;
                                if (gcBean.getMsg_status() == 0 && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                                    ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                    TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media resend */
                                    editmenu(taskDetailsBeanclick, holder.thumb_audio, context);
                                } else if (gcBean.getMsg_status() != 0 && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && !gcBean.getTaskDescription().equalsIgnoreCase("Message has been Removed")) {
                                    String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                                    ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                    TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media withdraw */
                                    editmenu(taskDetailsBeanclick, holder.thumb_audio, context);
                                } else {
                                    String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                                    ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                    TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media delete */
                                    editmenu(taskDetailsBeanclick, holder.thumb_audio, context);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("MediaListAdapter", "thumb_audio Exception: " + e.getMessage(), "WARN", null);
                            }
                        }
                    });
                    return false;
                }
            });
            holder.ls_thumb_audio.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                itemPosition = position;
                                isreceiver = true;
                                String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                                ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media delete */
                                editmenu(taskDetailsBeanclick, holder.ls_thumb_audio, context);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("MediaListAdapter", "ls_thumb_audio Exception: " + e.getMessage(), "WARN", null);
                            }
                        }
                    });
                    return false;
                }
            });
            holder.thumb_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                itemPosition = position;
                                if (gcBean.getMsg_status() == 0 && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                                    ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                    TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media resend */
                                    editmenu(taskDetailsBeanclick, holder.thumb_image, context);
                                } else if (gcBean.getMsg_status() != 0 && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && !gcBean.getTaskDescription().equalsIgnoreCase("Message has been Removed")) {
                                    String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                                    ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                    TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media withdraw */
                                    editmenu(taskDetailsBeanclick, holder.thumb_image, context);
                                } else {
                                    String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                                    ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                    TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media delete */
                                    editmenu(taskDetailsBeanclick, holder.thumb_image, context);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("MediaListAdapter", "thumb_image Exception: " + e.getMessage(), "WARN", null);
                            }
                        }
                    });
                    return false;
                }
            });
            holder.sender_multimediaLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        itemPosition = position;
                        if (gcBean.getMsg_status() == 0 && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                            ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                            TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media resend */
                            editmenu(taskDetailsBeanclick, holder.sender_multimediaLayout, context);
                        } else if (gcBean.getMsg_status() != 0 && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && !gcBean.getTaskDescription().equalsIgnoreCase("Message has been Removed")) {
                            String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                            ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                            TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media withdraw */
                            editmenu(taskDetailsBeanclick, holder.sender_multimediaLayout, context);
                        } else {
                            String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                            ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                            TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media delete */
                            editmenu(taskDetailsBeanclick, holder.sender_multimediaLayout, context);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "sender_multimediaLayout Exception: " + e.getMessage(), "WARN", null);
                    }

                    return false;
                }
            });
            holder.ls_thumb_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                itemPosition = position;
                                isreceiver = true;
                                String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                                ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media delete */
                                editmenu(taskDetailsBeanclick, holder.ls_thumb_image, context);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("MediaListAdapter", "ls_thumb_image Exception: " + e.getMessage(), "WARN", null);
                            }
                        }
                    });
                    return false;
                }
            });
            holder.receiver_multimediaLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        itemPosition = position;
                        isreceiver = true;
                        String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                        ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                        TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);   /* media delete */
                        editmenu(taskDetailsBeanclick, holder.receiver_multimediaLayout, context);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "receiver_multimediaLayout Exception: " + e.getMessage(), "WARN", null);
                    }
                    return false;
                }
            });
            holder.rcv_des.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                itemPosition = position;
                                isreceiver = true;
                                if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDescription")) {

                                } else if (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("reminder")) {

                                } else {
                                    String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                                    ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                    TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media delete */
                                    editmenu(taskDetailsBeanclick, holder.rcv_des, context);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("MediaListAdapter", "rcv_des Exception: " + e.getMessage(), "WARN", null);
                            }
                        }
                    });
                    return false;
                }
            });
            holder.receiver_side_description_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    try {
                        itemPosition = position;
                        isreceiver = true;
                        if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDescription")) {

                        } else if (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("reminder")) {

                        } else {
                            String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                            ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                            TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media delete */
                            editmenu(taskDetailsBeanclick, holder.receiver_side_description_layout, context);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "receiver_side_description_layout Exception: " + e.getMessage(), "WARN", null);
                    }
                    return true;
                }
            });

            holder.txt_des.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("formDetailsChangeRequest")) {

                            TaskDetailsBean taskDetailsBean1 = gcBean;
                            Log.i("MediaListAdapter", "from UserName == " + taskDetailsBean1.getFromUserName() + " From userId== " + taskDetailsBean1.getFromUserId() + "\n" +
                                    "Touser Name == " + taskDetailsBean1.getToUserName() + " toUser Id == " + taskDetailsBean1.getToUserId() + "\n" +
                                    "  FormId == " + taskDetailsBean1.getCustomTagId() + "  setId == " + taskDetailsBean1.getCustomSetId());

                            Intent intent = new Intent(context, FormEntryViewActivity.class);
                            //                intent.putExtra("FormsMapValue", formFieldsBeenMap);
                            //                intent.putExtra("FormsListValue", setIdValueList);
                            intent.putExtra("FormsId", String.valueOf(taskDetailsBean1.getCustomTagId()));
                            intent.putExtra("setId", String.valueOf(taskDetailsBean1.getCustomSetId()));
                            intent.putExtra("TaskId", taskDetailsBean1.getTaskId());
                            intent.putExtra("TaskNo", taskDetailsBean1.getTaskNo());
                            intent.putExtra("TaskName", taskDetailsBean1.getTaskName());
                            intent.putExtra("EntryMode", "multi");
                            intent.putExtra("EntryWay", "Adapter");
                            intent.putExtra("UserList", NewTaskConversation.listOfObservers);
                            intent.putExtra("TaskBean", taskDetailsBean1);
                            ((Activity) context).startActivity(intent);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "txt_des Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });

            holder.txt_des.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                itemPosition = position;
                                if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDescription")) {

                                } else if (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("reminder")) {

                                } else if (gcBean.getMsg_status() == 0 && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                                    ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                    TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media resend */
                                    editmenu(taskDetailsBeanclick, holder.txt_des, context);
                                } else if (gcBean.getMsg_status() != 0 && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && !gcBean.getTaskDescription().equalsIgnoreCase("Message has been Removed")) {
                                    String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                                    ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                    TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media withdraw */
                                    editmenu(taskDetailsBeanclick, holder.txt_des, context);
                                } else {
                                    String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                                    ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                                    TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media delete */
                                    editmenu(taskDetailsBeanclick, holder.txt_des, context);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("MediaListAdapter", "txt_des Exception: " + e.getMessage(), "WARN", null);
                            }
                        }
                    });
                    return false;
                }
            });
            holder.txt_des_under.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    try {
                        itemPosition = position;
                        if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDescription")) {

                        } else if (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("reminder")) {

                        } else if (gcBean.getMsg_status() == 0 && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            String query = "select * from  taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                            ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                            TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media resend */
                            editmenu(taskDetailsBeanclick, holder.txt_des_under, context);
                        } else if (gcBean.getMsg_status() != 0 && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && !gcBean.getTaskDescription().equalsIgnoreCase("Message has been Removed")) {
                            String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                            ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                            TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media withdraw */
                            editmenu(taskDetailsBeanclick, holder.txt_des_under, context);
                        } else {
                            String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                            ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                            TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media delete */
                            editmenu(taskDetailsBeanclick, holder.txt_des_under, context);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "txt_des_under Exception: " + e.getMessage(), "WARN", null);
                    }
                    return false;
                }
            });
            holder.sender_side_location_iconview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (gcBean.getMimeType().toString().toLowerCase().trim().equalsIgnoreCase("map")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("map", gcBean.getTaskDescription());
                            bundle.putString("viewmap", "location");
                            Intent intent = new Intent(getContext(), LocationFind.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "sender_side_location_iconview Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });
            holder.thumb_audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (gcBean.getMimeType().toString().toLowerCase().trim().equalsIgnoreCase("audio")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("audio", gcBean.getTaskDescription());
                            Intent intent = new Intent(getContext(), Audioplayer.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "thumb_audio Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });
            holder.ls_thumb_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        switch (gcBean.getMimeType().toLowerCase().trim()) {
                            case "video":
                                File directory = new File(dir_path + gcBean.getTaskDescription());
                                if (directory.exists()) {
                                    Intent intent = new Intent(context, VideoPlayer.class);
                                    intent.putExtra("video", directory.getAbsolutePath());
                                    context.startActivity(intent);
                                } else
                                    Toast.makeText(context, "File not available", Toast.LENGTH_LONG).show();
                                break;
                            case "image":
                                Intent intent = new Intent(context, FullScreenImage.class);
                                intent.putExtra("image", dir_path + gcBean.getTaskDescription());
                                context.startActivity(intent);
                                break;
                            case "sketch":
                                Intent intentsketch = new Intent(context, FullScreenImage.class);
                                intentsketch.putExtra("image", dir_path + gcBean.getTaskDescription());
                                context.startActivity(intentsketch);
                                break;
                            case "audio":
                                String audio_path = dir_path + gcBean.getTaskDescription();
                                Bundle bundle = new Bundle();
                                bundle.putString("audio", audio_path);
                                Intent intentaudio = new Intent(getContext(), Audioplayer.class);
                                intentaudio.putExtras(bundle);
                                context.startActivity(intentaudio);
                                break;
                            case "map":
                                Bundle bundlemap = new Bundle();
                                bundlemap.putString("map", gcBean.getTaskDescription());
                                bundlemap.putString("viewmap", "location");
                                Intent intentmap = new Intent(getContext(), LocationFind.class);
                                intentmap.putExtras(bundlemap);
                                context.startActivity(intentmap);
                                break;
                            case "document":
                                File url = new File(dir_path + gcBean.getTaskDescription());
                                setDocumentDataType(url);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "ls_thumb_image Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });
            holder.sender_side_doc_iconview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (gcBean.getMimeType() != null && !gcBean.getMimeType().equalsIgnoreCase("") && (gcBean.getMimeType() != null)) {
                            File url = new File(gcBean.getTaskDescription());
                            setDocumentDataType(url);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "sender_side_doc_iconview Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });
            holder.receiver_side_doc_iconview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (gcBean.getMimeType() != null && !gcBean.getMimeType().equalsIgnoreCase("") && (gcBean.getTaskDescription() != null)) {
                            File url = new File(dir_path + gcBean.getTaskDescription());
                            setDocumentDataType(url);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "receiver_side_doc_iconview Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });
            holder.receiver_side_location_iconview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (gcBean.getMimeType().equalsIgnoreCase("map")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("map", gcBean.getTaskDescription());
                            bundle.putString("viewmap", "location");
                            Intent intent = new Intent(getContext(), LocationFind.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "receiver_side_location_iconview Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });
            holder.ls_thumb_audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (gcBean.getMimeType().equalsIgnoreCase("audio")) {
                            String audio_path = dir_path + gcBean.getTaskDescription();
                            Bundle bundle = new Bundle();
                            bundle.putString("audio", audio_path);
                            Intent intent = new Intent(getContext(), Audioplayer.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "ls_thumb_audio Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });
            holder.thumb_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (gcBean.getMimeType().equalsIgnoreCase("video")) {
                            File directory = new File(gcBean.getTaskDescription());
                            if (directory.exists()) {
                                Intent intent = new Intent(context, VideoPlayer.class);
                                intent.putExtra("video", gcBean.getTaskDescription());
                                context.startActivity(intent);
                            } else {
                                Toast.makeText(context, "File not available", Toast.LENGTH_LONG).show();
                            }
                        } else if (gcBean.getMimeType().equalsIgnoreCase("image")) {
                            Intent intent = new Intent(context, FullScreenImage.class);
                            intent.putExtra("image", gcBean.getTaskDescription());
                            context.startActivity(intent);
                        } else if (gcBean.getMimeType().toLowerCase().trim().equalsIgnoreCase("audio")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("audio", gcBean.getTaskDescription());
                            Intent intent = new Intent(getContext(), Audioplayer.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        } else if (gcBean.getMimeType().toLowerCase().trim().equalsIgnoreCase("map")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("map", gcBean.getTaskDescription());
                            bundle.putString("viewmap", "location");
                            Intent intent = new Intent(getContext(), LocationFind.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        } else if (gcBean.getMimeType() != null && !gcBean.getMimeType().equalsIgnoreCase("") && (gcBean.getMimeType() != null)) {
                            File url = new File(gcBean.getTaskDescription());
                            setDocumentDataType(url);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "thumb_image Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });
            holder.dateChangeRequest_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.d("Receiver", " status_dateChangeRequest_icon ### " + gcBean.getTaskStatus());
                        Log.d("Receiver", " ProjectId ----> ##&&&  " + gcBean.getProjectId());
                        String dateChangeRequest_status = "";
                        if (gcBean.getProjectId() != null && !gcBean.getProjectId().equalsIgnoreCase("") && !gcBean.getProjectId().equalsIgnoreCase("null")) {
                            dateChangeRequest_status = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskStatus from projectHistory where taskId='" + gcBean.getTaskId() + "'");
                        } else {
                            dateChangeRequest_status = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskStatus from taskHistoryInfo where taskId='" + gcBean.getTaskId() + "'");
                        }
                        Log.d("Receiver", " status_dateChangeRequest_icon ===> 1 " + dateChangeRequest_status);
                        if (dateChangeRequest_status != null && !dateChangeRequest_status.equalsIgnoreCase("") && !dateChangeRequest_status.equalsIgnoreCase("Closed")) {
                            if (!dateChangeRequest_status.equalsIgnoreCase("abandoned")) {
                                NewTaskConversation.calender = true;
                                final Dialog dialog = new Dialog(context);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.edit_reminder_date);
                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(dialog.getWindow().getAttributes());
                                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                lp.horizontalMargin = 15;
                                Window window = dialog.getWindow();
                                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                window.setAttributes(lp);
                                window.setGravity(Gravity.CENTER);
                                try {
                                    dialog.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("MediaListAdapter", "dateChangeRequest_icon Exception: " + e.getMessage(), "WARN", null);
                                }
                                final TaskDetailsBean alert_bean = detailsBeanArrayList.get(position);
                                TextView check_conflict = (TextView) dialog.findViewById(R.id.Check_conflict);
                                View view_conflict = (View) dialog.findViewById(R.id.view_conflict);
                                if (Appreference.conflicttask) {
                                    check_conflict.setVisibility(View.VISIBLE);
                                    view_conflict.setVisibility(View.VISIBLE);
                                }
                                TextView edit_remdr_date = (TextView) dialog.findViewById(R.id.edit);
                                edit_remdr_date.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            dialog.dismiss();
                                            String queryy = "";
                                            Intent intent = new Intent(context, TaskTakerDateRequest.class);
                                            if (alert_bean.getTaskType() != null && alert_bean.getTaskType().equalsIgnoreCase("Group")) {
                                                if (alert_bean.getProjectId() != null && !alert_bean.getProjectId().equalsIgnoreCase("")) {
                                                    queryy = "select * from taskDetailsInfo where (fromUserId='" + alert_bean.getFromUserId() + "' or toUserId='" + alert_bean.getFromUserId() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "'and taskId='" + alert_bean.getTaskId() + "' and mimeType='date' and (requestStatus='approved' or requestStatus='assigned') order by id desc";
                                                } else {
                                                    queryy = "select * from taskDetailsInfo where (fromUserId='" + alert_bean.getToUserId() + "' or toUserId='" + alert_bean.getToUserId() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "'and taskId='" + alert_bean.getTaskId() + "' and mimeType='date' and (requestStatus='approved' or requestStatus='assigned') order by id desc";
                                                }
                                            } else {
                                                queryy = "select * from taskDetailsInfo where (fromUserName='" + Appreference.loginuserdetails.getUsername() + "' or toUserName='" + Appreference.loginuserdetails.getUsername() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "'and taskId='" + alert_bean.getTaskId() + "' and mimeType='date' and (requestStatus='approved' or requestStatus='assigned') order by id desc";
                                            }
                                            ArrayList<TaskDetailsBean> taskList = null;
                                            try {
                                                taskList = VideoCallDataBase.getDB(context).getTaskHistory(queryy);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Appreference.printLog("MediaListAdapter", "taskList Exception: " + e.getMessage(), "WARN", null);
                                            }
                                            if (taskList.size() > 0) {
                                                TaskDetailsBean bean2 = taskList.get(0);
                                                intent.putExtra("startdate", bean2.getPlannedStartDateTime());
                                                intent.putExtra("enddate", bean2.getPlannedEndDateTime());
                                                intent.putExtra("reminderdate", bean2.getRemainderFrequency());
                                                intent.putExtra("reminderfreq", bean2.getTimeFrequency());
                                                intent.putExtra("reminderquotes", bean2.getReminderQuote());
                                                intent.putExtra("isRemainderRequired", bean2.getIsRemainderRequired());
                                                intent.putExtra("username", bean2.getFromUserName());
                                                intent.putExtra("remindertone", bean2.getServerFileName());
                                                intent.putExtra("taskType", bean2.getTaskType());
                                                intent.putExtra("toUserId", bean2.getToUserId());
                                                intent.putExtra("ownerOfTask", bean2.getOwnerOfTask());
                                                intent.putExtra("taskId", bean2.getTaskId());
                                                if (bean2.getProjectId() != null) {
                                                    intent.putExtra("projectId", bean2.getProjectId());
                                                }
                                                Log.i("groupMemberAccess", "medialist IsRemainderRequired ** " + bean2.getIsRemainderRequired());
                                                Log.i("groupMemberAccess", "medialist getTaskType ** " + bean2.getTaskType());
                                                Log.i("groupMemberAccess", "medialist getToUserId ** " + bean2.getToUserId());
                                                Log.i("groupMemberAccess", "medialist getOwnerOfTask ** " + bean2.getOwnerOfTask());
                                                ((Activity) context).startActivityForResult(intent, 337);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Appreference.printLog("MediaListAdapter", "dateChangeRequest_icon Exception: " + e.getMessage(), "WARN", null);
                                        }
                                    }
                                });
                                check_conflict.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            dialog.dismiss();
                                            if (Appreference.conflicttask) {
                                                Appreference.isconflicttaker = true;
                                                String queryy = "";
                                                if (alert_bean.getTaskType() != null && alert_bean.getTaskType().equalsIgnoreCase("Group")) {
                                                    queryy = "select * from taskDetailsInfo where (fromUserId='" + alert_bean.getToUserId() + "' or toUserId='" + alert_bean.getToUserId() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "'and taskId='" + alert_bean.getTaskId() + "' and mimeType='date' and (requestStatus='approved' or requestStatus='assigned') order by id desc";
                                                } else {
                                                    queryy = "select * from taskDetailsInfo where (fromUserName='" + Appreference.loginuserdetails.getUsername() + "' or toUserName='" + Appreference.loginuserdetails.getUsername() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "'and taskId='" + alert_bean.getTaskId() + "' and mimeType='date' and (requestStatus='approved' or requestStatus='assigned') order by id desc";
                                                }
                                                Log.i("DateClick", "check_conflict Click : queryy : " + queryy);
                                                ArrayList<TaskDetailsBean> taskList = VideoCallDataBase.getDB(context).getTaskHistory(queryy);
                                                if (taskList.size() > 0) {
                                                    TaskDetailsBean bean2 = taskList.get(0);
                                                    if (!bean2.getTaskType().equalsIgnoreCase("Group")) {
                                                        confictWebservice(bean2);
                                                        listener.showMedialistProgress();
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Appreference.printLog("MediaListAdapter", "dateChangeRequest_icon Exception: " + e.getMessage(), "WARN", null);
                                        }
                                    }
                                });
                                TextView cancel_req = (TextView) dialog.findViewById(R.id.cancel);
                                cancel_req.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            dialog.dismiss();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Appreference.printLog("MediaListAdapter", "dateChangeRequest_icon Exception: " + e.getMessage(), "WARN", null);
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(context, "This " + category + " is  Abandon ", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "This " + category + " is already Closed ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "dateChangeRequest_icon Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });

            holder.dateChangeApproval_sender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        Log.d("Receiver", " status_datechangeapp_status ===> !!! " + gcBean.getTaskStatus());
                        String datechange_status = "";
                        if (gcBean.getProjectId() != null && !gcBean.getProjectId().equalsIgnoreCase("") && !gcBean.getProjectId().equalsIgnoreCase("null")) {
                            datechange_status = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskStatus from projectHistory where taskId='" + gcBean.getTaskId() + "'");
                        } else {
                            datechange_status = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskStatus from taskHistoryInfo where taskId='" + gcBean.getTaskId() + "'");
                        }
                        Log.d("Receiver", " status_datechangeapp_status ===> 2 " + datechange_status);
                        if (datechange_status != null && !datechange_status.equalsIgnoreCase("") && !datechange_status.equalsIgnoreCase("Closed")) {
                            if (datechange_status != null && !datechange_status.equalsIgnoreCase("") && !datechange_status.equalsIgnoreCase("abandoned")) {
                                Intent intent = new Intent(context, TaskDateUpdate.class);
                                intent.putExtra("template", "failure");
                                intent.putExtra("taskId", gcBean.getTaskId());
                                intent.putExtra("taskType", gcBean.getTaskType());
                                intent.putExtra("taskStatus", gcBean.getTaskStatus());
                                intent.putExtra("toUserIdConflict", String.valueOf(gcBean.getToUserId()));
                                if (gcBean.getProjectId() != null) {
                                    intent.putExtra("projectId", gcBean.getProjectId());
                                }
                                ((Activity) context).startActivityForResult(intent, 336);
                            } else {
                                Toast.makeText(context, "Unable to sent Date task is in abandoned state ", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "This " + category + " is already Closed ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "dateChangeApproval_sender Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });

            holder.exclation_counter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String query = "select * from taskDetailsInfo where signalid='" + gcBean.getSignalid() + "'";
                        ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                        TaskDetailsBean taskDetailsBeanclick = taskDetailsBeans_list.get(0);    /* media delete */
                        exclationmenu(taskDetailsBeanclick, holder.txt_des_under, context);
                        Toast.makeText(getContext(), "exclation", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "exclation_counter Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });
            holder.dateChangeApproval_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.d("Receiver", " status_dateChangeApproval_status ----> &&&  " + gcBean.getTaskStatus());
                        Log.d("Receiver", " ProjectId ----> &&&  " + gcBean.getProjectId());
                        String dateChangeApprov_status = "";
                        if (gcBean.getProjectId() != null && !gcBean.getProjectId().equalsIgnoreCase("") && !gcBean.getProjectId().equalsIgnoreCase("null")) {
                            dateChangeApprov_status = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskStatus from projectHistory where taskId='" + gcBean.getTaskId() + "'");
                        } else {
                            dateChangeApprov_status = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskStatus from taskHistoryInfo where taskId='" + gcBean.getTaskId() + "'");
                            Log.d("Receiver", " status_dateChangeApproval_status ---> 33 " + dateChangeApprov_status);
                        }
                        Log.d("Receiver", " status_dateChangeApproval_status ---> 3 " + dateChangeApprov_status);
                        if (dateChangeApprov_status != null && !dateChangeApprov_status.equalsIgnoreCase("") && !dateChangeApprov_status.equalsIgnoreCase("Closed")) {
                            if (dateChangeApprov_status != null && !dateChangeApprov_status.equalsIgnoreCase("") && !dateChangeApprov_status.equalsIgnoreCase("abandoned")) {
                                NewTaskConversation.calender = true;
                                final TaskDetailsBean alert_bean = detailsBeanArrayList.get(position);
                                final Dialog dialog = new Dialog(context);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.date_change_approval);
                                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                                lp.copyFrom(dialog.getWindow().getAttributes());
                                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                                lp.horizontalMargin = 15;
                                Window window = dialog.getWindow();
                                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                window.setAttributes(lp);
                                window.setGravity(Gravity.CENTER);
                                dialog.show();
                                TextView approve = (TextView) dialog.findViewById(R.id.approve);
                                approve.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            dialog.dismiss();
                                            NewTaskConversation.calender = false;
                                            VideoCallDataBase.getDB(context).taskMsg_StatusUpdate(gcBean.getSignalid());
                                            for (TaskDetailsBean detailsBean : detailsBeanArrayList) {
                                                if (detailsBean.getSignalid() != null && detailsBean.getSignalid().equalsIgnoreCase(gcBean.getSignalid())) {
                                                    detailsBean.setMsg_status(9);
                                                    break;
                                                }
                                            }
                                            String queryy = "";
                                            if (alert_bean.getTaskType() != null && alert_bean.getTaskType().equalsIgnoreCase("Group")) {
                                                queryy = "select * from taskDetailsInfo where (fromUserId='" + alert_bean.getToUserId() + "' or toUserId='" + alert_bean.getToUserId() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + alert_bean.getTaskId() + "' and mimeType='date' and requestStatus='requested' order by id desc";
                                            } else {
                                                queryy = "select * from taskDetailsInfo where (fromUserName='" + Appreference.loginuserdetails.getUsername() + "' or toUserName='" + Appreference.loginuserdetails.getUsername() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + alert_bean.getTaskId() + "' and mimeType='date' and requestStatus='requested' order by id desc";
                                            }
                                            ArrayList<TaskDetailsBean> taskList_fromDB = VideoCallDataBase.getDB(context).getTaskHistory(queryy);
                                            if (taskList_fromDB.size() > 0) {
                                                TaskDetailsBean bean2 = taskList_fromDB.get(0);
                                                String date_header = "approved";
                                                listener.dateSendORApprovalORReject(bean2, date_header);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Appreference.printLog("MediaListAdapter", "dateChangeApproval_icon Exception: " + e.getMessage(), "WARN", null);
                                        }
                                    }
                                });
                                TextView reject = (TextView) dialog.findViewById(R.id.reject);
                                reject.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            dialog.dismiss();
                                            VideoCallDataBase.getDB(context).taskMsg_StatusUpdate(gcBean.getSignalid());
                                            for (TaskDetailsBean detailsBean : detailsBeanArrayList) {
                                                if (detailsBean.getSignalid() != null && detailsBean.getSignalid().equalsIgnoreCase(gcBean.getSignalid())) {
                                                    detailsBean.setMsg_status(9);
                                                    break;
                                                }
                                            }
                                            String queryy = "";
                                            if (alert_bean.getTaskType() != null && alert_bean.getTaskType().equalsIgnoreCase("Group")) {
                                                queryy = "select * from taskDetailsInfo where (fromUserId='" + alert_bean.getToUserId() + "' or toUserId='" + alert_bean.getToUserId() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + alert_bean.getTaskId() + "' and mimeType='date' and requestStatus='requested' order by id desc";
                                            } else {
                                                queryy = "select * from taskDetailsInfo where (fromUserName='" + Appreference.loginuserdetails.getUsername() + "' or toUserName='" + Appreference.loginuserdetails.getUsername() + "') and loginuser='" + Appreference.loginuserdetails.getEmail() + "' and taskId='" + alert_bean.getTaskId() + "' and mimeType='date' and requestStatus='requested' order by id desc";
                                            }
                                            ArrayList<TaskDetailsBean> taskList_fromDB = VideoCallDataBase.getDB(context).getTaskHistory(queryy);
                                            if (taskList_fromDB.size() > 0) {
                                                TaskDetailsBean bean2 = taskList_fromDB.get(taskList_fromDB.size() - 1);
                                                String date_header = "rejected";
                                                listener.dateSendORApprovalORReject(bean2, date_header);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Appreference.printLog("MediaListAdapter", "dateChangeApproval_icon Exception: " + e.getMessage(), "WARN", null);
                                        }
                                    }
                                });
                                TextView cancel1_req = (TextView) dialog.findViewById(R.id.cancel1);
                                cancel1_req.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            dialog.dismiss();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Appreference.printLog("MediaListAdapter", "dateChangeApproval_icon Exception: " + e.getMessage(), "WARN", null);
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(context, "Unable to Approve or Reject Requested date task is in abandoned state ", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "This " + category + " is already Closed ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "dateChangeApproval_icon Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });
            holder.send_readmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (gcBean.getLongmessage() != null && gcBean.getLongmessage().equalsIgnoreCase("0")) {
                                    gcBean.setLongmessage("1");
                                    VideoCallDataBase.getDB(context).task_LongmessageUpdate(gcBean.getSignalid());
                                }
                                notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("MediaListAdapter", "send_readmore Exception: " + e.getMessage(), "WARN", null);
                            }
                        }
                    });
                }
            });
            holder.rec_readmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (gcBean.getLongmessage() != null && gcBean.getLongmessage().equalsIgnoreCase("0")) {
                                    gcBean.setLongmessage("1");
                                    VideoCallDataBase.getDB(context).task_LongmessageUpdate(gcBean.getSignalid());
                                }
                                notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("MediaListAdapter", "rec_readmore Exception: " + e.getMessage(), "WARN", null);
                            }
                        }
                    });
                }
            });

            // set animation duration via code, but preferable in your layout files by using the animation_duration attribute
            holder.expandableTextView_left.setAnimationDuration(750L);

            // set interpolators for both expanding and collapsing animations
            holder.expandableTextView_left.setInterpolator(new OvershootInterpolator());

            // or set them separately
            holder.expandableTextView_left.setExpandInterpolator(new OvershootInterpolator());
            holder.expandableTextView_left.setCollapseInterpolator(new OvershootInterpolator());

            // toggle the ExpandableTextView
            holder.buttonToggle_left.setOnClickListener(new View.OnClickListener() {
                @SuppressWarnings("ConstantConditions")
                @Override
                public void onClick(final View v) {
                    try {
                        holder.expandableTextView_left.expand();
                        if (gcBean.getLongmessage() != null && gcBean.getLongmessage().equalsIgnoreCase("0")) {
                            VideoCallDataBase.getDB(context).task_LongmessageUpdate(gcBean.getSignalid());
                        }
                        holder.buttonToggle_left.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "buttonToggle_left Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });

            holder.buttonToggle_left.setOnClickListener(new View.OnClickListener() {    // but, you can also do the checks yourself
                @Override
                public void onClick(final View v) {
                    try {
                        if (holder.expandableTextView_left.isExpanded()) {
                            holder.expandableTextView_left.collapse();
                            holder.expandableTextView_left.setMaxLines(2);
                            holder.buttonToggle_left.setText("read more");
                        } else {
                            holder.expandableTextView_left.expand();
                            holder.expandableTextView_left.setMaxLines(Integer.MAX_VALUE);
                            holder.buttonToggle_left.setText("read less");
                        }
                        holder.expandableTextView_left.expand();
                        if (gcBean.getLongmessage() != null && gcBean.getLongmessage().equalsIgnoreCase("0")) {
                            VideoCallDataBase.getDB(context).task_LongmessageUpdate(gcBean.getSignalid());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "buttonToggle_left Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });

            holder.expandableTextView_left.setOnExpandListener(new ExpandableTextView.OnExpandListener() {  // listen for expand / collapse events
                @Override
                public void onExpand(final ExpandableTextView view) {
                    Log.d("Textview", "ExpandableTextView expanded");
                }

                @Override
                public void onCollapse(final ExpandableTextView view) {
                    Log.d("Textview", "ExpandableTextView collapsed");
                }
            });

            // set animation duration via code, but preferable in your layout files by using the animation_duration attribute
            holder.expandableTextView_right.setAnimationDuration(750L);

            // set interpolators for both expanding and collapsing animations
            holder.expandableTextView_right.setInterpolator(new OvershootInterpolator());

            // or set them separately
            holder.expandableTextView_right.setExpandInterpolator(new OvershootInterpolator());
            holder.expandableTextView_right.setCollapseInterpolator(new OvershootInterpolator());

            holder.buttonToggle_right.setOnClickListener(new View.OnClickListener() {   // toggle the ExpandableTextView
                @SuppressWarnings("ConstantConditions")
                @Override
                public void onClick(final View v) {
                    try {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                holder.expandableTextView_right.toggle();
                                if (gcBean.getLongmessage() != null && gcBean.getLongmessage().equalsIgnoreCase("0")) {
                                    VideoCallDataBase.getDB(context).task_LongmessageUpdate(gcBean.getSignalid());
                                }
                                holder.buttonToggle_right.setText(holder.expandableTextView_left.isExpanded() ? "read less" : "read more");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "buttonToggle_right Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });

            holder.buttonToggle_right.setOnClickListener(new View.OnClickListener() {   // but, you can also do the checks yourself
                @Override
                public void onClick(final View v) {
                    try {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (holder.expandableTextView_right.isExpanded()) {
                                    holder.expandableTextView_right.collapse();
                                    holder.expandableTextView_right.setMaxLines(2);
                                    holder.buttonToggle_right.setText("read more");
                                } else {
                                    holder.expandableTextView_right.expand();
                                    holder.expandableTextView_right.setMaxLines(Integer.MAX_VALUE);
                                    holder.buttonToggle_right.setText("read less");
                                }
                                if (gcBean.getLongmessage() != null && gcBean.getLongmessage().equalsIgnoreCase("0")) {
                                    VideoCallDataBase.getDB(context).task_LongmessageUpdate(gcBean.getSignalid());
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("MediaListAdapter", "buttonToggle_right Exception: " + e.getMessage(), "WARN", null);
                    }
                }
            });

            holder.expandableTextView_right.setOnExpandListener(new ExpandableTextView.OnExpandListener() { // listen for expand / collapse events
                @Override
                public void onExpand(final ExpandableTextView view) {
                    Log.d("Textview", "ExpandableTextView expanded");
                }

                @Override
                public void onCollapse(final ExpandableTextView view) {
                    Log.d("Textview", "ExpandableTextView collapsed");
                }
            });

//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MMM");
//            String date = simpleDateFormat1.format(simpleDateFormat.parse(gcBean.getDateTime().split(" ")[0]));
//            if (simpleDateFormat.format(new Date()).equalsIgnoreCase(simpleDateFormat.format(simpleDateFormat.parse(gcBean.getDateTime().split(" ")[0])))) {
//                date = "today";
//            }

            if (gcBean.getMimeType() != null && gcBean.getFromUserName() != null && gcBean.getFromUserName().equalsIgnoreCase(MainActivity.username)) {
                String dd = "";
                dd = setConversationTime(gcBean);
                if (gcBean.getMimeType().equalsIgnoreCase("dates") || gcBean.getMimeType().equalsIgnoreCase("textdate")) {
                    holder.time.setVisibility(View.GONE);
                } else {
                    holder.time.setVisibility(View.VISIBLE);
                    if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("private")) {
                        holder.time.setText(dd);
                        holder.time.setTextColor(Color.WHITE);
                        holder.sender_msg_type.setVisibility(View.VISIBLE);
                        holder.sender_msg_type_image.setVisibility(View.VISIBLE);
                        if (gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("group")) {
                            holder.sender_msg_type.setText("private" + " " + gcBean.getPrivate_Member());
                            holder.sender_msg_type_image.setText("private" + " " + gcBean.getPrivate_Member());
                        } else {
                            holder.sender_msg_type.setText("private" + " " + gcBean.getPrivate_Member());
                            holder.sender_msg_type_image.setText("private" + " " + gcBean.getPrivate_Member());
                        }
                        holder.sender_msg_type.setTextColor(Color.WHITE);
                        holder.sender_msg_type_image.setTextColor(Color.WHITE);
                    } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDescription")) {
                        holder.sender_msg_type.setVisibility(View.VISIBLE);
                        holder.sender_msg_type.setText("Task Description");
                        holder.sender_msg_type.setTextColor(Color.RED);
                        holder.sender_msg_type_image.setVisibility(View.VISIBLE);
                        holder.sender_msg_type_image.setText("Task Description");
                        holder.sender_msg_type_image.setTextColor(Color.RED);
                        holder.time.setText(dd);
                    } else if (gcBean != null && gcBean.getSubType() != null && (gcBean.getSubType().equalsIgnoreCase("customeAttribute") || gcBean.getSubType().equalsIgnoreCase("customeHeaderAttribute"))) {
                        holder.sender_msg_type.setVisibility(View.GONE);
                        holder.sender_msg_type_image.setVisibility(View.GONE);
                        holder.time_1.setText(gcBean.getTaskTagName());
                        holder.time.setText(dd);
                        Log.i("MediaListAdapter", "CustomeAttribute----> 1 " + gcBean.getTaskTagName());
                    } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("percentageCompleted")) {
                        holder.sender_msg_type.setVisibility(View.VISIBLE);
                        holder.sender_msg_type.setText("percentage Completion");
                        holder.sender_msg_type.setTextColor(Color.BLACK);
                        holder.sender_msg_type_image.setVisibility(View.VISIBLE);
                        holder.sender_msg_type_image.setText("percentage Completion");
                        holder.sender_msg_type_image.setTextColor(Color.BLACK);
                        holder.time.setText(/*date + " " +*/ dd);
                    } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDateChangedRequest")) {
                        /*holder.time.setText("Description ");
                        holder.time.setTextColor(Color.RED);*/
                        holder.sender_msg_type.setVisibility(View.VISIBLE);
                        holder.sender_msg_type.setText("DateChangedRequest");
                        holder.sender_msg_type.setTextColor(Color.BLACK);
                        // holder.time_1.setVisibility(View.VISIBLE);
                        holder.sender_msg_type_image.setVisibility(View.VISIBLE);
                        holder.sender_msg_type_image.setText("DateChangedRequest");
                        holder.sender_msg_type_image.setTextColor(Color.BLACK);
                        holder.time.setText(/*date + " " +*/ dd);
                    } else {
                        holder.time.setText(dd);
                        holder.sender_msg_type.setVisibility(View.GONE);
                        holder.sender_msg_type_image.setVisibility(View.GONE);
                    }
                }
            } else {
                String name = VideoCallDataBase.getDB(context).getName(gcBean.getFromUserName());
                String dd = "";
                dd = setConversationTime(gcBean);
                if (gcBean.getMimeType() != null && (gcBean.getMimeType().equalsIgnoreCase("dates") || gcBean.getMimeType().equalsIgnoreCase("textdate"))) {
                    holder.time.setVisibility(View.GONE);
                } else {
                    holder.time.setVisibility(View.VISIBLE);
                    holder.time.setText(name + " ");
                    if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("private")) {
                        holder.time.setText(gcBean.getTasktime());
                        holder.time.setTextColor(Color.WHITE);
                        holder.receiver_msg_type.setVisibility(View.VISIBLE);
                        holder.receiver_msg_type.setText("private");
                        holder.receiver_msg_type.setTextColor(Color.BLACK);
                        holder.receiver_msg_type_image.setVisibility(View.VISIBLE);
                        holder.receiver_msg_type_image.setText("private");
                        holder.receiver_msg_type_image.setTextColor(Color.BLACK);
                    } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDescription")) {
                        holder.receiver_msg_type.setVisibility(View.VISIBLE);
                        holder.receiver_msg_type.setText("Task Description");
                        holder.receiver_msg_type.setTextColor(Color.RED);
                        holder.receiver_msg_type_image.setVisibility(View.VISIBLE);
                        holder.receiver_msg_type_image.setText("Task Description");
                        holder.receiver_msg_type_image.setTextColor(Color.RED);
                        holder.time.setText(holder.time.getText() + " " + gcBean.getTasktime());
                    } else if (gcBean.getSubType() != null && (gcBean.getSubType().equalsIgnoreCase("customeAttribute") || gcBean.getSubType().equalsIgnoreCase("customeHeaderAttribute"))) {
                        holder.receiver_msg_type_image.setVisibility(View.GONE);
                        holder.receiver_msg_type.setVisibility(View.GONE);
                        holder.time_1.setText(gcBean.getTaskTagName());
                        holder.time.setText(dd);
                        Log.i("MediaListAdapter", "CustomeAttribute----> 2 " + gcBean.getTaskTagName());
                    } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("percentageCompleted")) {
                        holder.receiver_msg_type.setVisibility(View.VISIBLE);
                        holder.receiver_msg_type.setText("percentage Completion");
                        holder.receiver_msg_type.setTextColor(Color.BLACK);
                        holder.receiver_msg_type_image.setVisibility(View.VISIBLE);
                        holder.receiver_msg_type_image.setText("percentage Completion");
                        holder.receiver_msg_type_image.setTextColor(Color.BLACK);
                        holder.time.setText(holder.time.getText() /*+ date*/ + " " + gcBean.getTasktime());
                    } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDateChangedRequest")) {
                        /*holder.time.setText("Description ");
                        holder.time.setTextColor(Color.RED);*/
                        holder.receiver_msg_type.setVisibility(View.VISIBLE);
                        holder.receiver_msg_type.setText("DateChangedRequest");
                        holder.receiver_msg_type.setTextColor(Color.BLACK);
//                        holder.time_1.setVisibility(View.VISIBLE);
                        holder.receiver_msg_type_image.setVisibility(View.VISIBLE);
                        holder.receiver_msg_type_image.setText("DateChangedRequest");
                        holder.receiver_msg_type_image.setTextColor(Color.BLACK);
                        holder.time.setText(holder.time.getText() /*+ date*/ + " " + gcBean.getTasktime());
                    } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("buzz")) {
                        holder.exclation_counter.setVisibility(View.VISIBLE);
                        holder.time.setText(holder.time.getText() + /*date +*/ " " + gcBean.getTasktime());
                        holder.receiver_msg_type_image.setVisibility(View.GONE);
                        holder.receiver_msg_type.setVisibility(View.GONE);
                    } else {
                        holder.time.setText(holder.time.getText() + /*date +*/ " " + gcBean.getTasktime());
                        holder.receiver_msg_type_image.setVisibility(View.GONE);
                        holder.receiver_msg_type.setVisibility(View.GONE);
                    }
                }
            }
            Log.i("gcbean", " gcBean.getMsg_status() ==> " + gcBean.getMsg_status());
            if (gcBean.getMsg_status() != 0 && gcBean.getMsg_status() != 24) {
                if ((gcBean.getTaskStatus() != null && gcBean.getTaskStatus().equals("draft")) || (gcBean.getMimeType() != null && gcBean.getMimeType().equals("note"))) {
                    holder.iv_txtstatus.setImageResource(R.drawable.dark_bluetick);
                    holder.iv_mmstatus.setImageResource(R.drawable.dark_bluetick);
                } else {
                    holder.iv_txtstatus.setImageResource(R.drawable.on_line);
                    holder.iv_mmstatus.setImageResource(R.drawable.on_line);
                }
            } else if (gcBean.getMsg_status() == 0) {
                if ((gcBean.getTaskStatus() != null && gcBean.getTaskStatus().equals("draft")) || (gcBean.getMimeType() != null && gcBean.getMimeType().equals("note"))) {
                    holder.iv_txtstatus.setImageResource(R.drawable.high_importance_32);
                    holder.iv_mmstatus.setImageResource(R.drawable.high_importance_32);
                } else {
                    holder.iv_txtstatus.setImageResource(R.drawable.off_line);
                    holder.iv_mmstatus.setImageResource(R.drawable.off_line);
                }
            } else if (gcBean.getMsg_status() == 24) {
                holder.iv_txtstatus.setImageResource(R.drawable.retryimg);
                holder.iv_mmstatus.setImageResource(R.drawable.retryimg);
            }
            if (gcBean.getMimeType() != null && (!gcBean.getMimeType().equalsIgnoreCase("text") && !gcBean.getMimeType().equalsIgnoreCase("url") && !gcBean.getMimeType().equalsIgnoreCase("date") && !gcBean.getMimeType().equalsIgnoreCase("observer") && !gcBean.getMimeType().equalsIgnoreCase("Reassign") && !gcBean.getMimeType().equalsIgnoreCase("Remove")) && !gcBean.getMimeType().equalsIgnoreCase("assigntask")) {
                if (gcBean.getFromUserName() != null && !gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                    if (gcBean.getShow_progress() == 0) {
                        holder.ls_thumb_image.setBackgroundResource(R.drawable.icon404);
                        Log.d("MediaAdapter", "404 image 7");
                        holder.progress_upload.setVisibility(View.GONE);
                        holder.ls_thumb_image.setBackgroundColor(getContext().getResources().getColor(R.color.white));
                    } else if (gcBean.getShow_progress() == 2) {
                        holder.progress_upload.setVisibility(View.GONE);
                        holder.progress_download.setVisibility(View.GONE);
                        holder.ls_thumb_image.setBackgroundResource(R.drawable.icon404);
                        holder.ls_thumb_image.setBackgroundColor(getContext().getResources().getColor(R.color.white));
                        Log.d("MediaAdapter", "404 image 8");
                    } else {
                        holder.progress_download.setVisibility(View.GONE);
                        holder.progress_upload.setVisibility(View.GONE);
                    }
                } else if (gcBean.getShow_progress() == 0) {
                    holder.progress_upload.setVisibility(View.VISIBLE);
                    holder.iv_mmstatus.setVisibility(View.GONE);
                    holder.progress_download.setVisibility(View.GONE);
                } else {
                    holder.progress_upload.setVisibility(View.GONE);
                    holder.progress_download.setVisibility(View.GONE);
                    holder.ls_thumb_image.setBackgroundResource(R.drawable.icon404);
                    Log.d("MediaAdapter", "404 image 9");
                    holder.iv_mmstatus.setVisibility(View.VISIBLE);
                }
            } else {
                holder.progress_download.setVisibility(View.GONE);
            }
            if ((gcBean != null && gcBean.getTaskDescription() != null && !gcBean.getTaskDescription().equalsIgnoreCase(null) || (gcBean.getTaskDescription() != null && !gcBean.getTaskDescription().equalsIgnoreCase(""))) && (gcBean.getMimeType() != null && !gcBean.getMimeType().equalsIgnoreCase(null) || gcBean.getMimeType() != null && !gcBean.getMimeType().equalsIgnoreCase(""))) {
                if ((gcBean.getMimeType() != null && (gcBean.getMimeType().equalsIgnoreCase("image") || gcBean.getMimeType().equalsIgnoreCase("sketch"))) && (gcBean.getTaskDescription() != null)) {
                    holder.both_side_list_image.setVisibility(View.VISIBLE);
                    holder.text_views.setVisibility(View.GONE);
                    holder.video_play_icon.setVisibility(View.GONE);
                    holder.ls_video_play_icon.setVisibility(View.GONE);
                    holder.thumb_audio.setVisibility(View.GONE);
                    holder.ls_thumb_audio.setVisibility(View.GONE);
                    holder.date_header_text.setVisibility(View.VISIBLE);
                    holder.sender_side_doc_icon.setVisibility(View.GONE);
                    holder.sender_side_doc_iconview.setVisibility(View.GONE);
                    holder.sender_side_location_iconview.setVisibility(View.GONE);
                    holder.time_under_text.setVisibility(View.VISIBLE);
                    String dd1 = "";
                    dd1 = setConversationTime(gcBean);
                    holder.time_under_text.setText(dd1);
                    if (gcBean.getFromUserName() != null && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        holder.ls_frame_layout.setVisibility(View.GONE);
                        LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) holder.timeview.getLayoutParams();
                        lllp.gravity = Gravity.END;
                        holder.timeview.setLayoutParams(lllp);
                        holder.frame_layout.setVisibility(View.VISIBLE);
                        holder.sender_side_list_image.setVisibility(View.VISIBLE);

                        if (cacheBitmap.containsKey(gcBean.getTaskDescription())) {
                            holder.thumb_image.setVisibility(View.VISIBLE);
                            holder.thumb_image.setImageBitmap(cacheBitmap.get(gcBean.getTaskDescription()));
                            String dd20 = "";
                            dd20 = setConversationTime(gcBean);
                            holder.time_under_text.setText(dd20);
                        } else {
                            File imageFile;
                            if (gcBean.getTaskDescription().contains("High Message")) {
                                imageFile = new File(gcBean.getTaskDescription());
                            } else {
                                imageFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "High Message/" + gcBean.getTaskDescription());
                            }
                            if (Appreference.getDeviceName() != null && Appreference.getDeviceName().equalsIgnoreCase("motorola MotoG3")) {
                                if (imageFile.exists()) {
                                    notifyImageThumb(gcBean);
                                    int size = 10;
                                    holder.thumb_image.setVisibility(View.VISIBLE);

                                    try {
                                        Bitmap bitmapOriginal = BitmapFactory.decodeFile(imageFile.toString());
                                        Bitmap bitmapsimplesize = Bitmap.createScaledBitmap(bitmapOriginal, bitmapOriginal.getWidth() / size, bitmapOriginal.getHeight() / size, true);
                                        bitmapOriginal.recycle();
                                        holder.thumb_image.setImageBitmap(bitmapsimplesize);
                                        String dd21 = "";
                                        dd21 = setConversationTime(gcBean);
                                        holder.time_under_text.setText(dd21);
                                    } catch (OutOfMemoryError error) {
                                        error.printStackTrace();
                                        Appreference.printLog("MediaListAdapter", "expandableTextView_right Exception: " + error.getMessage(), "WARN", null);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Appreference.printLog("MediaListAdapter", "expandableTextView_right Exception: " + e.getMessage(), "WARN", null);
                                    }
                                }
                            } else {
                                holder.ls_thumb_image.setImageBitmap(null);
                                holder.ls_thumb_image.setBackgroundResource(R.drawable.icon404);
                                Log.d("MediaAdapter", "404 image 10");
                                if (imageFile.exists()) {
                                    notifyImageThumb(gcBean);
                                    holder.thumb_image.setVisibility(View.VISIBLE);
                                    Picasso.with(context).load(imageFile).into(holder.thumb_image);
                                } else {
                                    Log.d("MedisListAdapter", "image else-=-imageFile not exists");
                                }
                            }
                        }
                    } else {
                        holder.ls_thumb_image.setImageBitmap(null);
                        holder.frame_layout.setVisibility(View.GONE);
                        holder.ls_frame_layout.setVisibility(View.VISIBLE);
                        holder.receiver_side_list_image.setVisibility(View.VISIBLE);
//                        holder.text_header.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) holder.timeview.getLayoutParams();
                        lllp.gravity = Gravity.LEFT;
                        holder.timeview.setLayoutParams(lllp);
                        holder.receiver_side_doc_iconview.setVisibility(View.GONE);
                        holder.ls_thumb_audio.setVisibility(View.GONE);
                        holder.receiver_side_location_iconview.setVisibility(View.GONE);
                        holder.ls_thumb_image.setImageBitmap(null);
                        if (cacheBitmap.containsKey(gcBean.getTaskDescription())) {
                            holder.ls_thumb_image.setVisibility(View.VISIBLE);
                            String dd8 = "";
                            dd8 = setConversationTime(gcBean);
                            holder.time_under_image_receiver.setText(dd8);
                            holder.receiver_name2.setText(holder.receiver_name_name);
                            holder.ls_thumb_image.setImageBitmap(null);
                            holder.ls_thumb_image.setImageBitmap(cacheBitmap.get(gcBean.getTaskDescription()));
                            String dd17 = "";
                            dd17 = setConversationTime(gcBean);
                            holder.time_under_image_receiver.setText(dd17);
                            holder.receiver_name2.setText(holder.receiver_name_name);
                            holder.progress_download.setVisibility(View.GONE);
                        } else {
                            File imageFile = new File(dir_path + gcBean.getTaskDescription());
                            if (imageFile.exists()) {
                                notifyImageThumb(gcBean);
                                holder.ls_thumb_image.setVisibility(View.VISIBLE);
                                holder.ls_thumb_image.setBackgroundColor(getContext().getResources().getColor(R.color.white));
                                Log.d("MediaAdapter", "original image 1");
                                String dd7 = "";
                                dd7 = setConversationTime(gcBean);
                                holder.time_under_image_receiver.setText(dd7);
                                holder.receiver_name2.setText(holder.receiver_name_name);
                                holder.ls_thumb_image.setImageBitmap(null);
                                Picasso.with(context).load(imageFile).into(holder.ls_thumb_image);
                                holder.progress_download.setVisibility(View.GONE);
                            } else {
                                holder.ls_thumb_image.setBackgroundResource(R.drawable.icon404);
                                Log.d("MediaAdapter", "404 image 11");
                            }
                        }
                    }
                } else if (gcBean.getMimeType() != null && (gcBean.getMimeType().equalsIgnoreCase("document") || gcBean.getMimeType().equalsIgnoreCase("pdf")) && ((gcBean.getTaskDescription() != null))) {
                    holder.both_side_list_image.setVisibility(View.VISIBLE);
                    holder.text_views.setVisibility(View.GONE);
                    holder.video_play_icon.setVisibility(View.GONE);
                    holder.thumb_image.setVisibility(View.GONE);
                    holder.date_header_text.setVisibility(View.VISIBLE);
                    holder.ls_thumb_image.setVisibility(View.GONE);
                    holder.ls_video_play_icon.setVisibility(View.GONE);
                    holder.thumb_audio.setVisibility(View.GONE);
                    holder.ls_thumb_audio.setVisibility(View.GONE);
                    holder.sender_side_location_iconview.setVisibility(View.GONE);
                    String ext = gcBean.getTaskDescription().substring(gcBean.getTaskDescription().lastIndexOf(".") + 1, gcBean.getTaskDescription().length());
                    if (gcBean.getFromUserName() != null && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        File imageFile = new File(gcBean.getTaskDescription());
                        LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) holder.timeview.getLayoutParams();
                        lllp.gravity = Gravity.RIGHT;
                        holder.timeview.setLayoutParams(lllp);
                        holder.receiver_side_list_image.setVisibility(View.GONE);
//                        holder.text_header.setVisibility(View.GONE);
                        holder.sender_side_list_image.setVisibility(View.VISIBLE);
                        holder.frame_layout.setVisibility(View.VISIBLE);
                        holder.thumb_image.setVisibility(View.VISIBLE);
                        holder.sender_side_doc_icon.setVisibility(View.GONE);
                        if (imageFile.exists()) {
                            byte[] bytes;
                            try {
                                File file = new File(imageFile.getAbsolutePath());
                                FileInputStream is = new FileInputStream(file); // Get the size of the file
                                long length = file.length();
                                bytes = new byte[(int) length];
                                int offset = 0, numRead = 0;
                                while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                                    offset += numRead;
                                }
                                ByteBuffer buffer = ByteBuffer.NEW(bytes);
                                if (gcBean.getMimeType().equalsIgnoreCase("pdf")) {
                                    String data = Base64.encodeToString(bytes, Base64.DEFAULT);
                                    PDFFile pdf_file = new PDFFile(buffer);
                                    PDFPage page = pdf_file.getPage(1, true);
                                    RectF rect = new RectF(0, 0, (int) page.getBBox().width(), (int) page.getBBox().height());
                                    Bitmap image = page.getImage((int) rect.width(), (int) rect.height(), rect);
                                    FileOutputStream os = new FileOutputStream(Environment.getExternalStorageDirectory() + "/High Message/dummy.jpg");
                                    image.compress(Bitmap.CompressFormat.JPEG, 80, os);
                                    holder.thumb_image.setImageBitmap(image);
                                    String dd22 = "";
                                    dd22 = setConversationTime(gcBean);
                                    holder.time_under_text.setText(dd22);
                                } else {
                                    Bitmap bitmap = BitmapFactory.decodeFile(dir_path + gcBean.getTaskDescription());
                                    holder.ls_thumb_image.setImageBitmap(bitmap);
                                    String dd18 = "";
                                    dd18 = setConversationTime(gcBean);
                                    holder.time_under_image_receiver.setText(dd18);
                                    holder.receiver_name2.setText(holder.receiver_name_name);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("MediaListAdapter", "expandableTextView_right Exception: " + e.getMessage(), "WARN", null);
                            }
                        }
                        switch (ext) {
                            case "pdf":
                                holder.thumb_image.setImageResource(R.drawable.pdf12);
                                String dd23 = "";
                                dd23 = setConversationTime(gcBean);
                                holder.time_under_text.setText(dd23);
                                break;
                            case "doc":
                            case "docx":
                            case "txt":
                                holder.thumb_image.setImageResource(R.drawable.msword);
                                String dd24 = "";
                                dd24 = setConversationTime(gcBean);
                                holder.time_under_text.setText(dd24);
                                break;
                            case "xls":
                                holder.thumb_image.setImageResource(R.drawable.excel);
                                String dd25 = "";
                                dd25 = setConversationTime(gcBean);
                                holder.time_under_text.setText(dd25);
                                break;
                            case "ppt":
                                holder.thumb_image.setImageResource(R.drawable.ppt);
                                String dd26 = "";
                                dd26 = setConversationTime(gcBean);
                                holder.time_under_text.setText(dd26);
                                break;
                            default:
                                holder.thumb_image.setImageResource(R.drawable.unknown);
                                String dd27 = "";
                                dd27 = setConversationTime(gcBean);
                                holder.time_under_text.setText(dd27);
                                break;
                        }
                    } else {
                        File imageFile = new File(dir_path + gcBean.getTaskDescription());
                        holder.sender_side_list_image.setVisibility(View.GONE);
                        holder.receiver_side_list_image.setVisibility(View.VISIBLE);
//                        holder.text_header.setVisibility(View.GONE);
                        holder.ls_frame_layout.setVisibility(View.VISIBLE);
                        holder.ls_thumb_image.setVisibility(View.VISIBLE);
                        holder.time_under_image_receiver.setVisibility(View.VISIBLE);
                        String dd5 = "";
                        dd5 = setConversationTime(gcBean);
                        holder.time_under_image_receiver.setText(dd5);
                        holder.receiver_name2.setText(holder.receiver_name_name);
                        holder.receiver_side_doc_icon.setVisibility(View.GONE);
                        holder.ls_thumb_image.setVisibility(View.VISIBLE);
                        LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) holder.timeview.getLayoutParams();
                        lllp.gravity = Gravity.LEFT;
                        holder.timeview.setLayoutParams(lllp);
                        if (imageFile.exists()) {
                            byte[] bytes;
                            try {
                                File file = new File(imageFile.getAbsolutePath());
                                FileInputStream is = new FileInputStream(file);
                                long length = file.length();
                                bytes = new byte[(int) length];
                                int offset = 0, numRead = 0;
                                while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                                    offset += numRead;
                                }
                                ByteBuffer buffer = ByteBuffer.NEW(bytes);
                                if (gcBean.getMimeType().equalsIgnoreCase("pdf")) {
                                    String data = Base64.encodeToString(bytes, Base64.DEFAULT);
                                    PDFFile pdf_file = new PDFFile(buffer);
                                    PDFPage page = pdf_file.getPage(1, true);
                                    RectF rect = new RectF(0, 0, (int) page.getBBox().width(), (int) page.getBBox().height());
                                    Bitmap image = page.getImage((int) rect.width(), (int) rect.height(), rect);
                                    FileOutputStream os = new FileOutputStream(Environment.getExternalStorageDirectory() + "/High Message/dummy.jpg");
                                    image.compress(Bitmap.CompressFormat.JPEG, 80, os);
                                    holder.ls_thumb_image.setImageBitmap(image);
                                    String dd9 = "";
                                    dd9 = setConversationTime(gcBean);
                                    holder.time_under_image_receiver.setText(dd9);
                                    holder.receiver_name2.setText(holder.receiver_name_name);
                                } else {
                                    Bitmap bitmap = BitmapFactory.decodeFile(dir_path + gcBean.getTaskDescription());
                                    holder.ls_thumb_image.setImageBitmap(bitmap);
                                    String dd10 = "";
                                    dd10 = setConversationTime(gcBean);
                                    holder.time_under_image_receiver.setText(dd10);
                                    holder.receiver_name2.setText(holder.receiver_name_name);
                                }
                                switch (ext) {
                                    case "pdf":
                                        holder.ls_thumb_image.setImageResource(R.drawable.pdf12);
                                        String dd11 = "";
                                        dd11 = setConversationTime(gcBean);
                                        holder.time_under_image_receiver.setText(dd11);
                                        holder.receiver_name2.setText(holder.receiver_name_name);
                                        break;
                                    case "doc":
                                    case "docx":
                                    case "txt":
                                        holder.ls_thumb_image.setImageResource(R.drawable.msword);
                                        String dd12 = "";
                                        dd12 = setConversationTime(gcBean);
                                        holder.time_under_image_receiver.setText(dd12);
                                        holder.receiver_name2.setText(holder.receiver_name_name);
                                        break;
                                    case "xls":
                                        holder.ls_thumb_image.setImageResource(R.drawable.excel);
                                        String dd13 = "";
                                        dd13 = setConversationTime(gcBean);
                                        holder.time_under_image_receiver.setText(dd13);
                                        holder.receiver_name2.setText(holder.receiver_name_name);
                                        break;
                                    case "ppt":
                                        holder.ls_thumb_image.setImageResource(R.drawable.ppt);
                                        String dd14 = "";
                                        dd14 = setConversationTime(gcBean);
                                        holder.time_under_image_receiver.setText(dd14);
                                        holder.receiver_name2.setText(holder.receiver_name_name);
                                        break;
                                    default:
                                        holder.ls_thumb_image.setImageResource(R.drawable.unknown);
                                        String dd15 = "";
                                        dd15 = setConversationTime(gcBean);
                                        holder.time_under_image_receiver.setText(dd15);
                                        holder.receiver_name2.setText(holder.receiver_name_name);
                                        break;
                                }
                                holder.progress_download.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("MediaListAdapter", "expandableTextView_right Exception: " + e.getMessage(), "WARN", null);
                            }
                        }
                    }
                } else if (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("textdate")) {
                    Log.i("TodaytaskType", " ServerFileName is -------> " + gcBean.getServerFileName());
                    Log.i("TodaytaskType", " getDateTime is -------> " + gcBean.getDateTime());

                    if (gcBean.getServerFileName().contains(gcBean.getDateTime())) {
                        Log.i("TodaytaskType", " ServerFileName is -------> 2 " + gcBean.getServerFileName());
                        holder.audio_play1.setVisibility(View.GONE);
                        holder.both_side_list_image.setGravity(View.GONE);
                        holder.sender_side_list_image.setVisibility(View.GONE);
                        holder.receiver_side_list_image.setVisibility(View.GONE);
//                        holder.text_header.setVisibility(View.VISIBLE);
                        holder.text_views.setVisibility(View.GONE);
                        holder.sender_side_add_txt.setVisibility(View.GONE);
                        holder.date_header_text.setVisibility(View.VISIBLE);
                        String Datetype = "";
//                        Log.i("timingtask","simpleDateformat is "+simpleDateFormat.format(new Date()));
//                        Log.i("timingtask","simpleDateformat is "+simpleDateFormat.format(simpleDateFormat.parse(gcBean.getDateTime())));
//                        if (simpleDateFormat.format(new Date()).equalsIgnoreCase(simpleDateFormat.format(simpleDateFormat.parse(gcBean.getDateTime().split(" ")[0])))) {
//                            Datetype = "Today";
//                        }else if(!simpleDateFormat.format(new Date()).equalsIgnoreCase(simpleDateFormat.format(simpleDateFormat.parse(gcBean.getDateTime().split(" ")[0])))){
//                            Date datevalid=simpleDateFormat.parse(String.valueOf(new Date()));
//                            Date datevalid2=simpleDateFormat.parse(String.valueOf(gcBean.getDateTime()));
//                            if (datevalid.compareTo(datevalid2)==-1) {
//                                Datetype = "Yesterday";
//                            } else {
//                                Datetype=gcBean.getDateTime().split(" ")[0];
//                            }
//                        }
//                        holder.date_header_text.setText(Datetype);
                    }
                    /*holder.date_header_text.setVisibility(View.VISIBLE);
                    String Datetype="";
                    Log.i("timingtask","simpleDateformat is "+simpleDateFormat.format(new Date()));
                    Log.i("timingtask", "simpleDateformat is " + simpleDateFormat.format(simpleDateFormat.parse(gcBean.getServerFileName())));
                    if (simpleDateFormat.format(new Date()).equalsIgnoreCase(simpleDateFormat.format(simpleDateFormat.parse(gcBean.getServerFileName().split(" ")[0])))) {
                        Datetype = "Today";
                    }else if(!simpleDateFormat.format(new Date()).equalsIgnoreCase(simpleDateFormat.format(simpleDateFormat.parse(gcBean.getServerFileName().split(" ")[0])))){
                        try {
                            Date datevalid=simpleDateFormat.parse(simpleDateFormat.format(new Date()));
                            Date datevalid2=simpleDateFormat.parse(String.valueOf(gcBean.getServerFileName()));
                            if (datevalid.compareTo(datevalid2)==-1) {
                                Datetype = "Yesterday";
                            } else {
                                Datetype=gcBean.getDateTime().split(" ")[0];
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }*/
//                    holder.date_header_text.setText(Datetype);
                } else if (gcBean.getMimeType() != null && (gcBean.getMimeType().equalsIgnoreCase("text") || gcBean.getMimeType().equalsIgnoreCase("overdue") || gcBean.getMimeType().equalsIgnoreCase("url") || gcBean.getMimeType().equalsIgnoreCase("date") || gcBean.getMimeType().equalsIgnoreCase("observer") || gcBean.getMimeType().equalsIgnoreCase("Reassign") || gcBean.getMimeType().equalsIgnoreCase("Remove") || gcBean.getMimeType().equalsIgnoreCase("note")) || gcBean.getMimeType().equalsIgnoreCase("assigntask") || gcBean.getMimeType().equalsIgnoreCase("reminder")) {
                    String imageFile = gcBean.getTaskDescription();
                    holder.both_side_list_image.setGravity(View.GONE);
                    holder.sender_side_list_image.setVisibility(View.GONE);
                    holder.receiver_side_list_image.setVisibility(View.GONE);
//                    holder.text_header.setVisibility(View.VISIBLE);
                    holder.text_views.setVisibility(View.VISIBLE);
                    holder.sender_side_add_txt.setVisibility(View.GONE);
                    holder.date_header_text.setVisibility(View.VISIBLE);

                    Log.i("escalation", "demo 1" + gcBean.getTaskDescription());
                    if (imageFile != null && !imageFile.equalsIgnoreCase("")) {
                        holder.text_views.setVisibility(View.VISIBLE);
//                        if (gcBean.getTaskDescription() != null && gcBean.getTaskDescription().equalsIgnoreCase("Dependency Reminder")) {
                        if (gcBean.getMsg_status() != 0 && gcBean.getMsg_status() == 33) {
                            Log.i("mediaListAdapter ", "Dependency " + gcBean.getTaskDescription() + " " + gcBean.getTaskPlannedBeforeEndDate() + " " + gcBean.getToTaskName());
                            holder.ll_expandable_text_view_dependency.setVisibility(View.VISIBLE);
                            holder.dependency_icon.setVisibility(View.VISIBLE);
                            String dependency = "Scheduling alert : " + gcBean.getTaskPlannedBeforeEndDate() + " Dependent " + gcBean.getFromTaskName() + " end date changed to " + gcBean.getTaskPlannedLatestEndDate() + " Reschedule " + gcBean.getToTaskName() + " accordingly. ";
                            holder.button_toggle_dependency.setText(dependency);
                        } else if (gcBean.getMsg_status() != 0 && gcBean.getMsg_status() == 22) {
                            Log.i("mediaListAdapter", "Dependency else if " + gcBean.getMsg_status());
                            holder.ll_expandable_text_view_dependency.setVisibility(View.VISIBLE);
                            holder.ll_expandable_text_view_dependency.setBackgroundResource(R.drawable.rectangle_buble);
                            String dependency = "Scheduling alert : " + gcBean.getTaskPlannedBeforeEndDate() + " Dependent " + gcBean.getFromTaskName() + " end date changed to " + gcBean.getTaskPlannedLatestEndDate() + " Reschedule " + gcBean.getToTaskName() + " accordingly. ";
                            holder.button_toggle_dependency.setText(dependency);
                            holder.dependency_icon.setVisibility(View.GONE);
                        }
                        if (gcBean.getFromUserName() != null && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            holder.sender_side_add_txt.setVisibility(View.VISIBLE);
                            holder.receiver_side_add_txt.setVisibility(View.GONE);
                            LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) holder.timeview.getLayoutParams();
                            lllp.gravity = Gravity.RIGHT;
                            holder.timeview.setLayoutParams(lllp);
                            if (gcBean.getSender_reply() != null && gcBean.getReply_sender_name() != null) {
                                if (gcBean.getSender_reply().contains(".jpg")) {
                                    holder.reply_sender_linear.setVisibility(View.VISIBLE);
                                    holder.reply_sender_msg.setText("Photo");
                                    holder.reply_sender_msg.setGravity(Gravity.CENTER);
//                                  holder.reply_sender_msg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.camera_for_reply,0,0,0);
                                    holder.reply_sender_name.setText(gcBean.getReply_sender_name());
                                    holder.txt_des_under.setMinimumWidth(225);
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Bitmap bitmapfile = BitmapFactory.decodeFile(gcBean.getSender_reply());
                                            Drawable draw = new BitmapDrawable(bitmapfile);
//                                            Bitmap compressbitmap2=Bitmap.createScaledBitmap(bitmapfile,50,50,false);
//                                            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(gcBean.getSender_reply()), 40, 40);
//                                            holder.reply_sender_image.setImageBitmap(bitmapfile);
                                            holder.reply_sender_image.setImageDrawable(draw);
                                        }
                                    });
                                } else {
                                    holder.reply_sender_linear.setVisibility(View.VISIBLE);
                                    holder.txt_des_under.setMinimumWidth(185);
                                    holder.reply_sender_image.setVisibility(View.GONE);
                                    holder.reply_sender_msg.setText(gcBean.getSender_reply());
                                    holder.reply_sender_name.setText(gcBean.getReply_sender_name());
                                }
                            } else {
                                holder.reply_sender_linear.setVisibility(View.GONE);
                            }
                            Log.i("overdue", "gcBean.getRead_status() " + gcBean.getRead_status());
                            Log.i("overdue", "gcBean.getTaskDescription() " + gcBean.getTaskDescription());
                            if (gcBean.getTaskDescription().contains("Escalation added :") && gcBean.getMsg_status() == 12) {
                                holder.exclation_counter.setVisibility(View.VISIBLE);
                            } else {
                                holder.exclation_counter.setVisibility(View.GONE);
                            }
                            if ((gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("overdue") || (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("buzzrequest") || (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("reminder"))))) {
                                if (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("overdue") && gcBean.getTaskDescription().equalsIgnoreCase("this task is overdue")) {
                                    Log.i("overdue", "overdue -->1 ");
                                    if (gcBean.getMsg_status() == 10) {
                                        holder.dateChangeApproval_sender.setVisibility(View.VISIBLE);
                                    } else {
                                        holder.dateChangeApproval_sender.setVisibility(View.GONE);
                                    }
                                }
                                if (gcBean.getMimeType() != null && (gcBean.getMimeType().equalsIgnoreCase("overdue") || gcBean.getMimeType().equalsIgnoreCase("reminder")) || (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("buzzrequest"))) {
                                    if (gcBean.getMsg_status() == 15)
                                        holder.txt_des_under.setBackgroundResource(R.drawable.un_answer_bubble_9);
                                    else
                                        holder.txt_des_under.setBackgroundResource(R.drawable.redsender1);
                                } else {
                                    holder.txt_des_under.setBackgroundResource(R.drawable.in_message_bg);
                                }
//                                holder.txt_des_under.setBackgroundResource(R.drawable.redsender1);
                                holder.txt_des.setText(gcBean.getTaskDescription());
                            }  /*else if(gcBean.getTaskStatus() != null && gcBean.getReplyMsg() != null){
                                holder.reply_sender_linear.setVisibility(View.VISIBLE);
                                holder.reply_sender_image.setVisibility(View.GONE);
                                holder.reply_sender_msg.setText(VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskDescription from taskDetailsInfo where signalId = '"+gcBean.getReplyMsg()+"'"));
                                holder.txt_des.setText(gcBean.getTaskDescription());
                            }*/ else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDescription") && !gcBean.getMimeType().equalsIgnoreCase("date")) {
                                Log.i("overdue", "overdue --> .......>   --------> 1 ");
                                holder.txt_des_under.setBackgroundResource(R.drawable.yellowsender1);
                                holder.txt_des.setText(gcBean.getTaskDescription());
                                if (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("date")) {
                                    holder.txt_des.setTextColor(Color.BLACK);
                                    holder.time.setTextColor(Color.WHITE);
                                } else {
                                    holder.txt_des.setTextColor(Color.RED);
                                    holder.time.setTextColor(Color.RED);
                                }
                            } else if (gcBean.getSubType() != null && (gcBean.getSubType().equalsIgnoreCase("customeHeaderAttribute") || gcBean.getSubType().equalsIgnoreCase("customeAttribute"))) {
                                holder.txt_des_under.setBackgroundResource(R.drawable.in_message_bg);
                                String custom_tag;
                                if (gcBean.getTaskDescription() != null && gcBean.getTaskDescription().contains("_")) {
                                    custom_tag = gcBean.getTaskTagName() + " : " + VideoCallDataBase.getDB(context).getname(gcBean.getTaskDescription());
                                } else {
                                    custom_tag = gcBean.getTaskTagName() + " : " + gcBean.getTaskDescription();
                                }
//                                String custom_tag = gcBean.getTaskTagName() + " : " + gcBean.getTaskDescription();
                                Log.i("MediaListAdapter", "CustomeAttribute----> 3 " + custom_tag);
                                holder.txt_des.setText(custom_tag);
                                holder.txt_des.setTextColor(Color.BLACK);
                            } else if (gcBean.getTaskDescription() != null && gcBean.getTaskDescription().contains("Task Assigned to")) {
                                Log.i("MediaList ", "gcBean.getTaskDescription()==>  " + gcBean.getTaskDescription());
                                String list_mem = "";
                                String mem_list = gcBean.getTaskDescription().split(" ")[3];
                                Log.i("MediaList ", "mem_list ##==>  " + mem_list);
                                int counter = 0;
                                for (int i = 0; i < mem_list.length(); i++) {
                                    if (mem_list.charAt(i) == ',') {
                                        counter++;
                                    }
                                }
                                for (int j = 0; j < counter + 1; j++) {
                                    if (counter == 0) {
                                        if (!mem_list.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                            String Name = VideoCallDataBase.getDB(context).getName(mem_list);
//                                            list_mem = list_mem + Name + ",";
                                            list_mem = Name;
                                        } else {
//                                            list_mem = "Me," + list_mem;
                                            list_mem = "You";
                                        }
                                    } else {
                                        String Mem_name = mem_list.split(",")[j];
                                        Log.i("MediaList ", "value==>  " + Mem_name);
                                        if (!Mem_name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                            String Name = VideoCallDataBase.getDB(context).getName(Mem_name);
                                            list_mem = list_mem + Name + ",";
                                        } else {
                                            list_mem = "You," + list_mem;
                                        }
                                    }
                                }
                                if (list_mem != null && list_mem.contains(",")) {
                                    list_mem = list_mem.substring(0, list_mem.length() - 1);
                                }
                                Log.i("MediaList ", "list_mem==>  " + list_mem);
                                String dec_value = "Task Assigned to " + list_mem;
                                Log.i("MediaList ", "dec_value==>  " + dec_value);
                                holder.txt_des_under.setBackgroundResource(R.drawable.in_message_bg);
                                holder.txt_des.setText(dec_value);
                                String dd51 = "";
                                dd51 = setConversationTime(gcBean);
                                holder.time_under_text_receiver.setText(dd51);
                            } else if ((gcBean.getSubType() != null && gcBean.getSubType().contains("deassign")) && (gcBean.getTaskDescription() != null && gcBean.getTaskDescription().contains("Left"))) {
                                Log.i("MediaList ", "mem_list ### ==>  " + gcBean.getTaskDescription());
                                String mem_list = gcBean.getTaskDescription().split(" Left")[0];
                                Log.i("MediaList ", "mem_list ### ==>  " + mem_list);
                                if (mem_list != null && mem_list.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    mem_list = "You Left";
                                } else {
                                    String list1 = VideoCallDataBase.getDB(context).getName(mem_list);
                                    mem_list = list1 + " Left";
                                    Log.i("MediaList ", "Name ### ==>  " + list1 + " mem_list==> " + mem_list);
                                }
                                holder.txt_des_under.setBackgroundResource(R.drawable.in_message_bg);
                                holder.txt_des.setText(mem_list);
                                String dd51 = "";
                                dd51 = setConversationTime(gcBean);
                                holder.time_under_text_receiver.setText(dd51);
                            } else {
                                holder.txt_des_under.setBackgroundResource(R.drawable.in_message_bg);
                                holder.txt_des.setText(gcBean.getTaskDescription());
                            }
                            if (gcBean.getTaskStatus() != null && gcBean.getMimeType().equals("date") && gcBean.getTaskStatus().equalsIgnoreCase("draft")) {
                                String date_hdr = "Task Duration";
                                String duration = gcBean.getDuration();
                                String duration_units = gcBean.getDurationUnit();
                                String repeatfreq = gcBean.getTimeFrequency();
                                String task_duration = date_hdr + "\n" + "Duration : " + duration + "\n" + "Duration units : " + duration_units + "\n" + "Repeat Frequency : " + repeatfreq;
                                holder.txt_des.setText(task_duration);
                            } else if (gcBean.getMimeType() != null && gcBean.getMimeType().equals("note")) {
                                String date_hdr = "Reminder Me";
                                String reminderfreq = "";
                                if (DateFormat.is24HourFormat(context)) {
                                    reminderfreq = gcBean.getRemainderFrequency();
                                } else {
                                    reminderfreq = Appreference.setDateTime(false, gcBean.getRemainderFrequency());
                                }
                                String note_duration = date_hdr + "\n" + "Reminder Time : " + reminderfreq;
                                holder.txt_des.setText(note_duration);
                            } else if (gcBean.getMimeType().equals("date")) {
                                String alldate2 = "", startdate = "", enddate = "", remdate = "";
                                String date_hdr = gcBean.getRequestStatus();
                                if (DateFormat.is24HourFormat(context)) {
                                    Log.i("MediaListAdapter", "startdate is  " + gcBean.getPlannedStartDateTime());
                                    Log.i("MediaListAdapter", "enddate is  " + gcBean.getPlannedEndDateTime());
                                    Log.i("MediaListAdapter", "remdate is  " + gcBean.getRemainderFrequency());

                                    startdate = Appreference.ChangeDevicePattern(true, gcBean.getPlannedStartDateTime(), datepattern);
                                    enddate = Appreference.ChangeDevicePattern(true, gcBean.getPlannedEndDateTime(), datepattern);
                                    remdate = Appreference.ChangeDevicePattern(true, gcBean.getRemainderFrequency(), datepattern);

                                    Log.i("MediaListAdapter", "startdate is  " + startdate);
                                    Log.i("MediaListAdapter", "enddate is  " + enddate);
                                    Log.i("MediaListAdapter", "remdate is  " + remdate);

                                    if (remdate == null || remdate.equalsIgnoreCase("")) {
                                        remdate = "N/A";
                                    } else if (gcBean.getIsRemainderRequired() != null && gcBean.getIsRemainderRequired().equalsIgnoreCase("N")) {
                                        remdate = "N/A";
                                    }
                                } else {
                                    startdate = Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, gcBean.getPlannedStartDateTime()), datepattern);
                                    enddate = Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, gcBean.getPlannedEndDateTime()), datepattern);
                                    remdate = Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, gcBean.getRemainderFrequency()), datepattern);
                                    if (remdate == null || remdate.equalsIgnoreCase("")) {
                                        remdate = "N/A";
                                    } else if (gcBean.getIsRemainderRequired() != null && gcBean.getIsRemainderRequired().equalsIgnoreCase("N")) {
                                        remdate = "N/A";
                                    }
                                }
                                String remfreq = gcBean.getTimeFrequency();
                                Log.i("MediaListAdapter", "reminder frequency is " + remfreq);
                                if (remfreq == null || remfreq.equalsIgnoreCase("")) {
                                    remfreq = "N/A";
                                } else if (gcBean.getIsRemainderRequired() != null && (gcBean.getIsRemainderRequired().equalsIgnoreCase("N") || gcBean.getIsRemainderRequired().equalsIgnoreCase("R"))) {
                                    remfreq = "N/A";
                                }
                                if (date_hdr != null && date_hdr.equalsIgnoreCase("assigned")) {
                                    date_hdr = "Reminder Details";
                                } else if (date_hdr != null && date_hdr.equalsIgnoreCase("requested")) {
                                    date_hdr = "date change request";
                                } else if (date_hdr != null && date_hdr.equalsIgnoreCase("approved")) {
                                    date_hdr = "date change request approved";
                                } else if (date_hdr != null && date_hdr.equalsIgnoreCase("rejected")) {
                                    date_hdr = "date change request rejected";
                                }
                                Log.i("MediaListAdapter", "date Sender OwnerOftask,loginUser,fromUser,toUSer " + gcBean.getOwnerOfTask() + " " + Appreference.loginuserdetails.getUsername() + " " + gcBean.getFromUserName() + " " + gcBean.getToUserName());
                                if (remdate != null && !remdate.equalsIgnoreCase("") && !remdate.equalsIgnoreCase("null") && remfreq != null && !remfreq.equalsIgnoreCase("") && !remfreq.equalsIgnoreCase("null")) {
                                    alldate2 = date_hdr + "\n" + "Start Date : " + startdate + "\n" + "End Date : " + enddate + "\n" + "Reminder Time : " + remdate + "\n" + "Reminder Frequency : " + remfreq;
                                } else if (remdate == null && remdate.equalsIgnoreCase("") && remdate.equalsIgnoreCase("null")) {
                                    alldate2 = date_hdr + "\n" + "Start Date : " + startdate + "\n" + "End Date : " + enddate + "\n" + "Reminder Time : " + remdate;
                                } else {
                                    alldate2 = date_hdr + "\n" + "Start Date : " + startdate + "\n" + "End Date : " + enddate;
                                }
                                if (date_hdr != null && !date_hdr.equalsIgnoreCase("null") && !date_hdr.equalsIgnoreCase("") && !date_hdr.equalsIgnoreCase(null)) {
                                    holder.txt_des.setText(alldate2);
                                } else {
                                    holder.txt_des.setVisibility(View.GONE);
                                }
                            }
                            if (gcBean.getSendStatus() != null && gcBean.getSendStatus().equalsIgnoreCase("1")) {
                                holder.remove_btn.setVisibility(View.VISIBLE);
                                holder.iv_txtstatus.setVisibility(View.GONE);

                            } else {
                                holder.remove_btn.setVisibility(View.GONE);
                                holder.iv_txtstatus.setVisibility(View.VISIBLE);
                            }
                            if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("private")) {
                                holder.txt_des.setTextColor(Color.WHITE);
                            } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDescription")) {
                                if (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("date")) {
                                    holder.txt_des.setTextColor(Color.BLACK);
                                    holder.time.setTextColor(Color.WHITE);
                                } else {
                                    holder.txt_des.setTextColor(Color.RED);
                                    holder.time.setTextColor(Color.RED);
                                }
                            } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("percentageCompleted")) {
                                holder.txt_des.setTextColor(Color.BLACK);
                                holder.sender_msg_type.setText("percentage Completion");
                            } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDateChangedRequest")) {
                                holder.txt_des.setTextColor(Color.BLACK);
                                holder.sender_msg_type.setText("DateChangedRequest");
                            } else if (gcBean.getMimeType() != null && (gcBean.getMimeType().equalsIgnoreCase("reminder") || gcBean.getMimeType().equalsIgnoreCase("overdue"))) {
                                holder.txt_des.setTextColor(Color.WHITE);
                                holder.time.setTextColor(Color.WHITE);
                            } else {
                                holder.txt_des.setTextColor(Color.BLACK);
                                holder.time.setTextColor(Color.WHITE);
                            }
                        } else {
                            holder.sender_side_add_txt.setVisibility(View.GONE);
                            holder.receiver_side_add_txt.setVisibility(View.VISIBLE);
                            holder.receiver_side_description_layout.setBackgroundResource(R.drawable.grey_im);
                            LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) holder.timeview.getLayoutParams();
                            lllp.gravity = Gravity.LEFT;
                            holder.timeview.setLayoutParams(lllp);
//                            if (gcBean.getTaskDescription().contains("Escalation added :") && gcBean.getRead_status() != 5) {
//                                holder.exclation_counter.setVisibility(View.VISIBLE);
//                            } else {
//                                holder.exclation_counter.setVisibility(View.GONE);
//                            }
                            if (gcBean.getTaskRequestType() != null && gcBean.getTaskRequestType().equalsIgnoreCase("replyMessage")) {
                                final String replymessage = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskDescription from taskDetailsInfo where signalId='" + gcBean.getParentId() + "'");
                                Log.i("MedialistAdapter", "string replymessage is" + replymessage);
                                holder.reply_receiver_linear.setVisibility(View.VISIBLE);
                                if (replymessage.contains(".jpg")) {
                                    holder.reply_receiver_image.setVisibility(View.VISIBLE);
                                    holder.reply_receiver_msg.setText("Photo");
                                    holder.reply_receiver_msg.setGravity(Gravity.CENTER);
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Bitmap bitmapfile = BitmapFactory.decodeFile(replymessage);
                                            Drawable draw = new BitmapDrawable(bitmapfile);
//                                            Bitmap compressbitmap2=Bitmap.createScaledBitmap(bitmapfile,50,50,false);
//                                            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(gcBean.getSender_reply()), 40, 40);
//                                            holder.reply_sender_image.setImageBitmap(bitmapfile);
                                            holder.reply_receiver_image.setImageDrawable(draw);
                                        }
                                    });
                                } else {
                                    holder.reply_receiver_image.setVisibility(View.GONE);
                                    holder.reply_receiver_name.setText(VideoCallDataBase.getDB(context).getName(VideoCallDataBase.getDB(context).getfirstname("select fromUserName from taskDetailsInfo where signalId='" + gcBean.getParentId() + "'")));
                                    holder.reply_receiver_msg.setText(VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskDescription from taskDetailsInfo where signalId='" + gcBean.getParentId() + "'"));
                                    Log.i("MedialistAdapter", "fromusername is" + VideoCallDataBase.getDB(context).getfirstname("select fromUserName from taskDetailsInfo where signalId='" + gcBean.getParentId() + "'"));
                                    Log.i("MedialistAdapter", "taskdescription is" + VideoCallDataBase.getDB(context).getLastname("select taskDescription from taskDetailsInfo where signalId='" + gcBean.getParentId() + "'"));
                                    Log.i("MedialistAdapter", "username is" + VideoCallDataBase.getDB(context).getName(VideoCallDataBase.getDB(context).getfirstname("select fromUserName from taskDetailsInfo where signalId='" + gcBean.getParentId() + "'")));
//                                    VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskDescription from taskDetailsInfo where signalId='"+gcBean.getParentId()+"'");

                                }
                            } else if (gcBean.getTaskRequestType() == null || !gcBean.getTaskRequestType().equalsIgnoreCase("replyMessage")) {
                                holder.reply_receiver_linear.setVisibility(View.GONE);
                            }
                            Log.i("escalation", "demo" + gcBean.getTaskDescription());
                            if (gcBean.getMimeType().equals("date") && gcBean.getTaskStatus().equalsIgnoreCase("draft")) {
                                String date_hdr = "Task Duration";
                                String duration = gcBean.getDuration();
                                String duration_units = gcBean.getDurationUnit();
                                String repeatfreq = gcBean.getTimeFrequency();
                                String task_duration = date_hdr + "\n" + "Duration : " + duration + "\n" + "Duration units : " + duration_units + "\n" + "Repeat Frequency : " + repeatfreq;
                                holder.txt_des.setText(task_duration);
                            } else if (gcBean.getMimeType().equals("date")) {
                                holder.rcv_des.setTextColor(Color.BLACK);
                                String date_hdr, startdate = "", enddate = "", remdate = "";
                                if (DateFormat.is24HourFormat(context)) {
                                    Log.i("remindervalue", "gcBean.getPlannedStartDateTime() " + gcBean.getPlannedStartDateTime());
                                    Log.i("remindervalue", "gcBean.getPlannedEndDateTime() " + gcBean.getPlannedEndDateTime());
                                    startdate = Appreference.ChangeDevicePattern(true, gcBean.getPlannedStartDateTime(), datepattern);
                                    enddate = Appreference.ChangeDevicePattern(true, gcBean.getPlannedEndDateTime(), datepattern);
                                    remdate = Appreference.ChangeDevicePattern(true, gcBean.getRemainderFrequency(), datepattern);
                                    if (remdate == null || remdate.equalsIgnoreCase("")) {
                                        remdate = "N/A";
                                    } else if (gcBean.getIsRemainderRequired() != null && gcBean.getIsRemainderRequired().equalsIgnoreCase("N")) {
                                        remdate = "N/A";
                                    }
                                } else {
                                    Log.i("remindervalue", "gcBean.getPlannedStartDateTime() ** " + gcBean.getPlannedStartDateTime());
                                    Log.i("remindervalue", "gcBean.getPlannedEndDateTime() **  " + gcBean.getPlannedEndDateTime());
                                    startdate = Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, gcBean.getPlannedStartDateTime()), datepattern);
                                    enddate = Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, gcBean.getPlannedEndDateTime()), datepattern);
                                    remdate = Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, gcBean.getRemainderFrequency()), datepattern);
                                    if (remdate == null || remdate.equalsIgnoreCase("")) {
                                        remdate = "N/A";
                                    } else if (gcBean.getIsRemainderRequired() != null && gcBean.getIsRemainderRequired().equalsIgnoreCase("N")) {
                                        remdate = "N/A";
                                    }
                                }
                                String remfreq = gcBean.getTimeFrequency();
                                if (remfreq == null || remfreq.equalsIgnoreCase("")) {
                                    remfreq = "N/A";
                                } else if (gcBean.getIsRemainderRequired() != null && (gcBean.getIsRemainderRequired().equalsIgnoreCase("N") || gcBean.getIsRemainderRequired().equalsIgnoreCase("R"))) {
                                    remfreq = "N/A";
                                }
                                Log.i("MediaListAdapter", "date Receiver OwnerOftask,loginUser,fromUser,toUSer " + gcBean.getOwnerOfTask() + " " + Appreference.loginuserdetails.getUsername() + " " + gcBean.getFromUserName() + " " + gcBean.getToUserName());
                                if (gcBean.getRequestStatus() != null && !gcBean.getRequestStatus().equalsIgnoreCase("") && !gcBean.getRequestStatus().equalsIgnoreCase(null)) {
                                    if (gcBean.getRequestStatus().equalsIgnoreCase("requested") && !gcBean.getOwnerOfTask().equalsIgnoreCase("") && gcBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                        if (gcBean.getMsg_status() == 10) {
                                            Log.i("overdue", "overdue -->2 ");
                                            holder.dateChangeApproval_icon.setVisibility(View.VISIBLE);
                                        }
                                        date_hdr = "date change request";
                                        String alldate1 = date_hdr + "\n" + "Start Date : " + startdate + "\n" + "End Date : " + enddate + "\n" + "Reminder Time : " + remdate + "\n" + "Reminder Frequency : " + remfreq;
                                        holder.rcv_des.setText(alldate1);
                                        String dd32 = "";
                                        dd32 = setConversationTime(gcBean);
                                        holder.time_under_text_receiver.setText(dd32);
                                        holder.receiver_name.setText(holder.receiver_name_name);
                                    } else if (gcBean.getRequestStatus().equalsIgnoreCase("requested")) {
                                        Log.i("overdue", "overdue check  -->0 ");
                                        if ((gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("Group") && groupMemberAccess.getRespondDateChange() != null && groupMemberAccess.getRespondDateChange().equalsIgnoreCase("1") && gcBean.getMsg_status() == 10) || (gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("individual") && gcBean.getMsg_status() == 10) || (gcBean.getProjectId() != null && !gcBean.getProjectId().equalsIgnoreCase(null) && !gcBean.getProjectId().equalsIgnoreCase("") && !gcBean.getProjectId().equalsIgnoreCase("null") && gcBean.getMsg_status() == 10)) {
                                            Log.i("task", "date Requested not same user " + gcBean.getMsg_status());
                                            Log.i("overdue", "overdue -->4 ");
                                            String task_observers = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskObservers from taskHistoryInfo where taskId ='" + gcBean.getTaskId() + "'");
                                            if (task_observers != null && !task_observers.equalsIgnoreCase("") && !task_observers.equalsIgnoreCase("null") && task_observers.contains(Appreference.loginuserdetails.getUsername())) {
                                                holder.dateChangeRequest_icon.setVisibility(View.GONE);
                                                Log.i("overdue", "dateChangeRequest_icon -->gone ");
                                            } else {
                                                holder.dateChangeRequest_icon.setVisibility(View.VISIBLE);
                                                Log.i("overdue", "dateChangeRequest_icon -->visible ");
                                            }
                                        }
                                        date_hdr = "date change request";
                                        String alldate1;
                                        alldate1 = date_hdr + "\n" + "Start Date : " + startdate + "\n" + "End Date : " + enddate + "\n" + "Reminder Time : " + remdate + "\n" + "Reminder Frequency : " + remfreq;
                                        holder.rcv_des.setText(alldate1);
                                        holder.rcv_des.setText(alldate1);
                                        String dd33 = "";
                                        dd33 = setConversationTime(gcBean);
                                        holder.time_under_text_receiver.setText(dd33);
                                        holder.receiver_name.setText(holder.receiver_name_name);
                                    } else if (gcBean.getRequestStatus().equalsIgnoreCase("approved")) {
                                        date_hdr = "date change request approved";
                                        if ((gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("Group") && groupMemberAccess.getRespondDateChange() != null && groupMemberAccess.getRespondDateChange().equalsIgnoreCase("1") && gcBean.getMsg_status() == 10) || (gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("individual") && gcBean.getMsg_status() == 10) || (gcBean.getProjectId() != null && !gcBean.getProjectId().equalsIgnoreCase(null) && !gcBean.getProjectId().equalsIgnoreCase("") && !gcBean.getProjectId().equalsIgnoreCase("null") && gcBean.getMsg_status() == 10)) {
                                            Log.i("task", "date Approval " + gcBean.getMsg_status());
                                            Log.i("overdue", "overdue -->5 ");
                                            boolean isObserver;
                                            String task_observers = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskObservers from taskHistoryInfo where taskId ='" + gcBean.getTaskId() + "'");
                                            if (task_observers != null && !task_observers.equalsIgnoreCase("") && !task_observers.equalsIgnoreCase("null") && task_observers.contains(Appreference.loginuserdetails.getUsername())) {
                                                holder.dateChangeRequest_icon.setVisibility(View.GONE);
                                                Log.i("overdue", "dateChangeRequest_icon -->gone ");
                                            } else {
                                                holder.dateChangeRequest_icon.setVisibility(View.VISIBLE);
                                                Log.i("overdue", "dateChangeRequest_icon -->visible ");
                                            }
                                        }
                                        String alldate1 = date_hdr + "\n" + "Start Date : " + startdate + "\n" + "End Date : " + enddate + "\n" + "Reminder Time : " + remdate + "\n" + "Reminder Frequency : " + remfreq;
                                        holder.rcv_des.setText(alldate1);
                                        String dd34 = "";
                                        dd34 = setConversationTime(gcBean);
                                        holder.time_under_text_receiver.setText(dd34);
                                        holder.receiver_name.setText(holder.receiver_name_name);
                                    } else if (gcBean.getRequestStatus().equalsIgnoreCase("rejected")) {
                                        date_hdr = "date change request rejected";
                                        if ((gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("Group") && groupMemberAccess.getRespondDateChange() != null && groupMemberAccess.getRespondDateChange().equalsIgnoreCase("1") && gcBean.getMsg_status() == 10) || (gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("individual") && gcBean.getMsg_status() == 10) || (gcBean.getProjectId() != null && !gcBean.getProjectId().equalsIgnoreCase(null) && !gcBean.getProjectId().equalsIgnoreCase("") && !gcBean.getProjectId().equalsIgnoreCase("null") && gcBean.getMsg_status() == 10)) {
                                            Log.i("task", "date Rejected " + gcBean.getMsg_status());
                                            Log.i("overdue", "overdue -->6 ");
                                            holder.dateChangeRequest_icon.setVisibility(View.VISIBLE);
                                        }
                                        String alldate1 = date_hdr + "\n" + "Start Date : " + startdate + "\n" + "End Date : " + enddate + "\n" + "Reminder Time : " + remdate + "\n" + "Reminder Frequency : " + remfreq;
                                        holder.rcv_des.setText(alldate1);
                                        String dd35 = "";
                                        dd35 = setConversationTime(gcBean);
                                        holder.time_under_text_receiver.setText(dd35);
                                        holder.receiver_name.setText(holder.receiver_name_name);
                                    } else {
                                        date_hdr = "Reminder Details";
                                        Log.i("task", "date getRespondDateChange " + groupMemberAccess.getRespondDateChange());
                                        if ((gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("Group") && groupMemberAccess.getRespondDateChange() != null && groupMemberAccess.getRespondDateChange().equalsIgnoreCase("1") && gcBean.getMsg_status() == 10) || (gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("individual") && gcBean.getMsg_status() == 10 && gcBean.getToUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) || (gcBean.getProjectId() != null && !gcBean.getProjectId().equalsIgnoreCase(null) && !gcBean.getProjectId().equalsIgnoreCase("") && !gcBean.getProjectId().equalsIgnoreCase("null") && gcBean.getMsg_status() == 10)) {
                                            Log.i("task", "date Assigned " + gcBean.getMsg_status());
                                            Log.i("groupMemberAccess", "ML if  " + groupMemberAccess.getRespondDateChange() + " " + gcBean.getProjectId() + " " + gcBean.getTaskType());
                                            Log.i("overdue", "overdue -->7 ");
                                            holder.dateChangeRequest_icon.setVisibility(View.VISIBLE);
                                        } else {
                                            Log.i("groupMemberAccess", "ML else ");
                                            holder.dateChangeRequest_icon.setVisibility(View.GONE);
                                        }
                                        String alldate1 = date_hdr + "\n" + "Start Date : " + startdate + "\n" + "End Date : " + enddate + "\n" + "Reminder Time : " + remdate + "\n" + "Reminder Frequency : " + remfreq;
                                        holder.rcv_des.setText(alldate1);
                                        String dd30 = "";
                                        dd30 = setConversationTime(gcBean);
                                        holder.time_under_text_receiver.setText(dd30);
                                        holder.receiver_name.setText(holder.receiver_name_name);
                                    }
                                }
                            } else {
                                Log.i("Buzz", "receiver gcBean.getSubType() 1 " + gcBean.getSubType());
                                try {
                                    if ((gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("overdue")) || (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("reminder")) || (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("buzzrequest"))) {
                                        if ((gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("overdue")) || (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("reminder")) || (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("buzzrequest"))) {
                                            holder.receiver_side_description_layout.setBackgroundResource(R.drawable.redreceiver1);
                                        } else {
                                            Log.i("Buzz", "receiver if gcBean.getSubType() 4 " + gcBean.getSubType());
                                            holder.receiver_side_description_layout.setBackgroundResource(R.drawable.grey_im);
                                        }
                                        //                                    holder.receiver_side_description_layout.setBackgroundResource(R.drawable.redreceiver1);
                                        holder.rcv_des.setText(gcBean.getTaskDescription());
                                        String dd50 = "";
                                        dd50 = setConversationTime(gcBean);
                                        holder.time_under_text_receiver.setText(dd50);
                                        holder.receiver_name.setText(holder.receiver_name_name);
                                        if (gcBean.getTaskStatus() != null && gcBean.getTaskStatus().equalsIgnoreCase("overdue") && gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("overdue") && gcBean.getTaskDescription().equalsIgnoreCase("this task is overdue")) {
                                            if (gcBean.getMsg_status() == 10) {
                                                holder.dateChangeRequest_icon.setVisibility(View.VISIBLE);
                                            } else {
                                                holder.dateChangeRequest_icon.setVisibility(View.GONE);
                                            }
                                        } else {
                                            holder.dateChangeRequest_icon.setVisibility(View.GONE);
                                        }
                                        //                                    holder.dateChangeRequest_icon.setVisibility(View.GONE);
                                        holder.dateChangeApproval_icon.setVisibility(View.GONE);
                                    } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDescription") && !gcBean.getMimeType().equalsIgnoreCase("date")) {
                                        Log.i("MediaListAdapter", "taskDescription 1 ");
                                        holder.receiver_side_description_layout.setBackgroundResource(R.drawable.yellowreceiver1);
                                        holder.rcv_des.setText(gcBean.getTaskDescription());
                                        String dd51 = "";
                                        dd51 = setConversationTime(gcBean);
                                        holder.time_under_text_receiver.setText(dd51);
                                        holder.receiver_name.setText(holder.receiver_name_name);
                                        holder.dateChangeRequest_icon.setVisibility(View.GONE);
                                        holder.dateChangeApproval_icon.setVisibility(View.GONE);
                                    } else if (gcBean.getTaskDescription() != null && gcBean.getTaskDescription().contains("Task Assigned to ")) {
                                        Log.i("MediaList ", "gcBean.getTaskDescription()==> else  " + gcBean.getTaskDescription());
                                        String list_mem = "";
                                        String mem_list = gcBean.getTaskDescription().split(" ")[3];
                                        Log.i("MediaList ", "mem_list ##==> else  " + mem_list);
                                        int counter = 0;
                                        for (int i = 0; i < mem_list.length(); i++) {
                                            if (mem_list.charAt(i) == ',') {
                                                counter++;
                                            }
                                        }
                                        for (int j = 0; j < counter + 1; j++) {
                                            if (counter == 0) {
                                                if (!mem_list.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                                    String Name = VideoCallDataBase.getDB(context).getName(mem_list);
//                                            list_mem = list_mem + Name + ",";
                                                    list_mem = Name;
                                                } else {
//                                            list_mem = "Me," + list_mem;
                                                    list_mem = "You";
                                                }
                                            } else {
                                                String Mem_name = mem_list.split(",")[j];
                                                Log.i("MediaList ", "value==> else  " + Mem_name);
                                                if (!Mem_name.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                                    String Name = VideoCallDataBase.getDB(context).getName(Mem_name);
                                                    list_mem = list_mem + Name + ",";
                                                } else {
                                                    list_mem = "You," + list_mem;
                                                }
                                            }
                                        }
                                        if (list_mem != null && list_mem.contains(",")) {
                                            list_mem = list_mem.substring(0, list_mem.length() - 1);
                                        }
                                        Log.i("MediaList ", "list_mem==> else " + list_mem);
                                        String dec_value = "Task Assigned to " + list_mem;
                                        Log.i("MediaList ", "dec_value==>  " + dec_value);
                                        holder.receiver_side_description_layout.setBackgroundResource(R.drawable.grey_im);
                                        holder.rcv_des.setText(dec_value);
                                        holder.rcv_des.setTextColor(Color.BLACK);
                                        String dd51 = "";
                                        dd51 = setConversationTime(gcBean);
                                        holder.time_under_text_receiver.setText(dd51);
                                        holder.receiver_name.setText(holder.receiver_name_name);
                                        holder.dateChangeRequest_icon.setVisibility(View.GONE);
                                        holder.dateChangeApproval_icon.setVisibility(View.GONE);
                                    } else if ((gcBean.getSubType() != null && gcBean.getSubType().contains("deassign")) && (gcBean.getTaskDescription() != null && gcBean.getTaskDescription().contains("Left"))) {
                                        Log.i("MediaList ", "mem_list ### ==> @@ " + gcBean.getTaskDescription());
                                        String mem_list = gcBean.getTaskDescription().split(" Left")[0];
                                        Log.i("MediaList ", "mem_list ### ==> @@  " + mem_list);
                                        if (mem_list != null && mem_list.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                            mem_list = "You Left";
                                        } else {
                                            String list1 = VideoCallDataBase.getDB(context).getName(mem_list);
                                            mem_list = list1 + " Left";
                                            Log.i("MediaList ", "Name ### ==> @@ " + list1 + " mem_list==> " + mem_list);
                                        }
                                        holder.receiver_side_description_layout.setBackgroundResource(R.drawable.grey_im);
                                        holder.rcv_des.setText(mem_list);
                                        holder.rcv_des.setTextColor(Color.BLACK);
                                        String dd51 = "";
                                        dd51 = setConversationTime(gcBean);
                                        holder.time_under_text_receiver.setText(dd51);
                                        holder.receiver_name.setText(holder.receiver_name_name);
                                        holder.dateChangeRequest_icon.setVisibility(View.GONE);
                                        holder.dateChangeApproval_icon.setVisibility(View.GONE);
                                    } else {
                                        Log.i("MediaListAdapter", "taskDescription 2 ");
                                        holder.receiver_side_description_layout.setBackgroundResource(R.drawable.grey_im);
                                        holder.rcv_des.setText(gcBean.getTaskDescription());
                                        holder.rcv_des.setTextColor(Color.BLACK);
                                        holder.time_under_text_receiver.setVisibility(View.VISIBLE);
                                        String dd52 = "";
                                        dd52 = setConversationTime(gcBean);
                                        holder.time_under_text_receiver.setText(dd52);
                                        holder.receiver_name.setText(holder.receiver_name_name);
                                        holder.dateChangeRequest_icon.setVisibility(View.GONE);
                                        holder.dateChangeApproval_icon.setVisibility(View.GONE);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("MediaListAdapter", "Exception: " + e.getMessage(), "WARN", null);
                                }
                            }
                            if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("private")) {
                                holder.rcv_des.setTextColor(Color.BLACK);
                            } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDescription") && !gcBean.getMimeType().equalsIgnoreCase("date")) {
                                if (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("date")) {
                                    holder.rcv_des.setTextColor(Color.BLACK);
                                } else {
                                    Log.i("MediaListAdapter", "taskDescription 3 ");
                                    holder.rcv_des.setTextColor(Color.RED);
                                }
                            } else if (gcBean.getSubType() != null && (gcBean.getSubType().equalsIgnoreCase("customeHeaderAttribute") || gcBean.getSubType().equalsIgnoreCase("customeAttribute"))) {
                                String custom_tag;
                                if (gcBean.getTaskDescription() != null && gcBean.getTaskDescription().contains("_")) {
                                    custom_tag = gcBean.getTaskTagName() + " : " + VideoCallDataBase.getDB(context).getname(gcBean.getTaskDescription());
                                } else {
                                    custom_tag = gcBean.getTaskTagName() + " : " + gcBean.getTaskDescription();
                                }
                                Log.i("MediaListAdapter", "CustomeAttribute->--->> 4 " + custom_tag);
//                                String custom_tag = gcBean.getTaskTagName() + " : " + gcBean.getTaskDescription();
                                holder.rcv_des.setText(custom_tag);
                                holder.rcv_des.setTextColor(Color.WHITE);
                            } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("percentageCompleted")) {
                                holder.rcv_des.setTextColor(Color.BLACK);
                                holder.receiver_msg_type.setText("percentage Completion");
                            } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDateChangedRequest")) {
                                holder.rcv_des.setTextColor(Color.BLACK);
                                holder.receiver_msg_type.setText("DateChangedRequest");
                            } else if (gcBean.getSubType() != null && !gcBean.getSubType().equalsIgnoreCase("") && gcBean.getMsg_status() == 10 && (gcBean.getSubType().equalsIgnoreCase("attendance_in") || gcBean.getSubType().equalsIgnoreCase("attendance_out") || gcBean.getSubType().equalsIgnoreCase("rate") || gcBean.getSubType().equalsIgnoreCase("work") || gcBean.getSubType().equalsIgnoreCase("overtime"))) {
                                Log.d("MediaAdapter", "tick image visible == " + gcBean.getSubType());
                                Log.d("MediaAdapter", "tick image visible");
                                if (gcBean.getOwnerOfTask() != null && gcBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    holder.leave.setVisibility(View.VISIBLE);
                                } else {
                                    holder.leave.setVisibility(View.GONE);
                                }
                            } else if (gcBean.getMimeType() != null && (gcBean.getMimeType().equalsIgnoreCase("reminder") || gcBean.getMimeType().equalsIgnoreCase("overdue"))) {
                                holder.rcv_des.setTextColor(Color.WHITE);
                                holder.time.setTextColor(Color.WHITE);
                            } else {
                                holder.rcv_des.setTextColor(Color.BLACK);
                            }
                        }
                    }
                } else if (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("video") && (gcBean.getTaskDescription() != null)) {
                    holder.both_side_list_image.setVisibility(View.VISIBLE);
                    holder.text_views.setVisibility(View.GONE);
                    holder.video_play_icon.setVisibility(View.VISIBLE);
                    holder.date_header_text.setVisibility(View.VISIBLE);
                    String dd3;
                    dd3 = setConversationTime(gcBean);
                    holder.time_under_text.setText(dd3);
                    if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("private")) {
                        holder.sender_msg_type_image.setVisibility(View.VISIBLE);
                        holder.sender_msg_type_image.setText("private");
                        holder.sender_msg_type_image.setTextColor(Color.WHITE);
                    } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDescription")) {
                        holder.sender_msg_type_image.setVisibility(View.VISIBLE);
                        holder.sender_msg_type_image.setText("Task Description");
                        holder.sender_msg_type_image.setTextColor(Color.RED);
                    } else if (gcBean != null && gcBean.getSubType() != null && (gcBean.getSubType().equalsIgnoreCase("customeAttribute") || gcBean.getSubType().equalsIgnoreCase("customeHeaderAttribute"))) {
                        holder.sender_msg_type.setVisibility(View.GONE);
                        holder.sender_msg_type_image.setVisibility(View.GONE);
                        Log.i("MediaListAdapter", "CustomeAttribute----> 5 ");
                    } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("percentageCompleted")) {
                        holder.sender_msg_type_image.setVisibility(View.VISIBLE);
                        holder.sender_msg_type_image.setText("percentage Completion");
                        holder.sender_msg_type_image.setTextColor(Color.BLACK);
                    } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDateChangedRequest")) {
                        holder.sender_msg_type_image.setVisibility(View.VISIBLE);
                        holder.sender_msg_type_image.setText("DateChangedRequest");
                        holder.sender_msg_type_image.setTextColor(Color.BLACK);
                    } else {
                        holder.sender_msg_type.setVisibility(View.GONE);
                        holder.sender_msg_type_image.setVisibility(View.GONE);
                    }
                    if (gcBean.getFromUserName() != null && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        holder.sender_side_list_image.setVisibility(View.VISIBLE);
                        holder.frame_layout.setVisibility(View.VISIBLE);
                        holder.receiver_side_list_image.setVisibility(View.GONE);
//                        holder.text_header.setVisibility(View.VISIBLE);
                        holder.video_play_icon.setVisibility(View.VISIBLE);
                        holder.thumb_image.setVisibility(View.VISIBLE);
                        holder.thumb_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        holder.time.setVisibility(View.VISIBLE);
                        holder.time.setText(gcBean.getTasktime());
                        holder.thumb_audio.setVisibility(View.GONE);
                        holder.ls_thumb_audio.setVisibility(View.GONE);
                        holder.sender_side_doc_icon.setVisibility(View.GONE);
                        holder.sender_side_doc_iconview.setVisibility(View.GONE);
                        LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) holder.timeview.getLayoutParams();
                        lllp.gravity = Gravity.RIGHT;
                        holder.timeview.setLayoutParams(lllp);
                        if (cacheBitmap.containsKey(gcBean.getTaskDescription())) {
                            holder.thumb_image.setImageBitmap(cacheBitmap.get(gcBean.getTaskDescription()));
                            String dd28 = "";
                            dd28 = setConversationTime(gcBean);
                            holder.time_under_text.setText(dd28);
                        } else {
                            File imageFile = new File(gcBean.getTaskDescription());
                            if (imageFile.exists()) {
                                Bitmap bMap = ThumbnailUtils.createVideoThumbnail(imageFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                                holder.thumb_image.setImageBitmap(bMap);
                                String dd29 = "";
                                dd29 = setConversationTime(gcBean);
                                holder.time_under_text.setText(dd29);
                            }
                        }
                    } else {
                        holder.sender_side_list_image.setVisibility(View.GONE);
                        LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) holder.timeview.getLayoutParams();
                        lllp.gravity = Gravity.LEFT;
                        holder.timeview.setLayoutParams(lllp);
                        holder.receiver_side_list_image.setVisibility(View.VISIBLE);
//                        holder.text_header.setVisibility(View.VISIBLE);
                        holder.ls_video_play_icon.setVisibility(View.VISIBLE);
                        holder.video_play_icon.setVisibility(View.GONE);
                        holder.ls_frame_layout.setVisibility(View.VISIBLE);
                        holder.ls_thumb_image.setVisibility(View.VISIBLE);
                        holder.ls_thumb_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        String dd6 = "";
                        dd6 = setConversationTime(gcBean);
                        holder.time_under_image_receiver.setText(dd6);
                        holder.time_under_image_receiver.setVisibility(View.VISIBLE);
                        holder.time_under_image_receiver.setText(gcBean.getTasktime());
                        holder.receiver_name2.setText(holder.receiver_name_name);
                        holder.receiver_side_doc_iconview.setVisibility(View.GONE);
                        holder.ls_thumb_image.setImageBitmap(null);
                        if (cacheBitmap.containsKey(gcBean.getTaskDescription())) {
                            holder.ls_thumb_image.setImageBitmap(cacheBitmap.get(gcBean.getTaskDescription()));
                        } else {
                            File imageFile = new File(dir_path + gcBean.getTaskDescription());
                            if (imageFile.exists()) {
                                Bitmap bMap = ThumbnailUtils.createVideoThumbnail(dir_path + gcBean.getTaskDescription(), MediaStore.Video.Thumbnails.MICRO_KIND);
                                holder.ls_thumb_image.setImageBitmap(bMap);
                                holder.progress_download.setVisibility(View.GONE);
                            } else {
                                holder.ls_thumb_image.setBackgroundResource(R.drawable.icon404);
                                Log.d("MediaAdapter", "404 image 12");
                            }
                        }
                    }
                } else if (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("map") && (gcBean.getTaskDescription() != null)) {
                    holder.both_side_list_image.setVisibility(View.VISIBLE);
                    holder.text_views.setVisibility(View.GONE);
                    holder.date_header_text.setVisibility(View.VISIBLE);
                    if (gcBean.getFromUserName() != null && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        holder.sender_side_list_image.setVisibility(View.VISIBLE);
                        holder.frame_layout.setVisibility(View.VISIBLE);
                        holder.receiver_side_list_image.setVisibility(View.GONE);
//                        holder.text_header.setVisibility(View.VISIBLE);
                        holder.video_play_icon.setVisibility(View.GONE);
                        holder.thumb_audio.setVisibility(View.GONE);
                        holder.sender_side_doc_icon.setVisibility(View.GONE);
                        holder.sender_side_doc_iconview.setVisibility(View.GONE);
                        holder.thumb_image.setVisibility(View.VISIBLE);
                        holder.thumb_image.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.mapimage));
                        String dd30 = "";
                        dd30 = setConversationTime(gcBean);
                        holder.time_under_text.setText(dd30);
                        LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) holder.timeview.getLayoutParams();
                        lllp.gravity = Gravity.RIGHT;
                        holder.timeview.setLayoutParams(lllp);
                    } else {
                        holder.sender_side_list_image.setVisibility(View.GONE);
                        LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) holder.timeview.getLayoutParams();
                        lllp.gravity = Gravity.LEFT;
                        holder.timeview.setLayoutParams(lllp);
                        holder.receiver_side_list_image.setVisibility(View.GONE);
//                        holder.text_header.setVisibility(View.VISIBLE);
                        holder.ls_video_play_icon.setVisibility(View.GONE);
                        holder.ls_frame_layout.setVisibility(View.GONE);
                        holder.receiver_side_doc_iconview.setVisibility(View.GONE);
                        holder.ls_frame_layout.setVisibility(View.VISIBLE);
                        holder.receiver_side_list_image.setVisibility(View.VISIBLE);
                        holder.ls_thumb_audio.setVisibility(View.GONE);
                        holder.ls_thumb_image.setVisibility(View.VISIBLE);
                        holder.ls_thumb_image.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.mapimage));
                        String dd16 = "";
                        dd16 = setConversationTime(gcBean);
                        holder.time_under_image_receiver.setText(dd16);
                        holder.receiver_name2.setText(holder.receiver_name_name);
                        holder.progress_download.setVisibility(View.GONE);
                    }
                } else if (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("audio") && (gcBean.getTaskDescription() != null)) {
                    holder.both_side_list_image.setVisibility(View.VISIBLE);
                    holder.text_views.setVisibility(View.GONE);
                    String dd4 = "";
                    dd4 = setConversationTime(gcBean);
                    holder.time_under_text.setText(dd4);
                    if (gcBean.getFromUserName() != null && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        holder.sender_side_list_image.setVisibility(View.VISIBLE);
                        holder.frame_layout.setVisibility(View.VISIBLE);
                        holder.receiver_side_list_image.setVisibility(View.GONE);
                        holder.video_play_icon.setVisibility(View.GONE);
                        holder.thumb_image.setVisibility(View.VISIBLE);
//                        holder.text_header.setVisibility(View.VISIBLE);
                        holder.ls_thumb_audio.setVisibility(View.GONE);
                        holder.sender_side_doc_icon.setVisibility(View.GONE);
                        holder.sender_side_doc_iconview.setVisibility(View.GONE);
                        holder.thumb_audio.setVisibility(View.GONE);
                        holder.thumb_image.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.audio_icon_new));
                        holder.thumb_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        holder.sender_side_location_iconview.setVisibility(View.GONE);
                        LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) holder.timeview.getLayoutParams();
                        lllp.gravity = Gravity.RIGHT;
                        holder.timeview.setLayoutParams(lllp);
                    } else {
                        holder.sender_side_list_image.setVisibility(View.GONE);
                        LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) holder.timeview.getLayoutParams();
                        lllp.gravity = Gravity.LEFT;
                        holder.timeview.setLayoutParams(lllp);
                        holder.receiver_side_list_image.setVisibility(View.GONE);
                        holder.ls_video_play_icon.setVisibility(View.GONE);
                        holder.ls_frame_layout.setVisibility(View.GONE);
                        holder.ls_thumb_image.setVisibility(View.VISIBLE);
                        holder.receiver_side_doc_iconview.setVisibility(View.GONE);
                        holder.ls_frame_layout.setVisibility(View.VISIBLE);
//                        holder.text_header.setVisibility(View.VISIBLE);
                        holder.receiver_side_list_image.setVisibility(View.VISIBLE);
                        holder.thumb_audio.setVisibility(View.GONE);
                        holder.ls_thumb_audio.setVisibility(View.GONE);
                        holder.ls_thumb_image.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(), R.drawable.audio_icon_new));
                        holder.ls_thumb_image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        String dd9 = "";
                        dd9 = setConversationTime(gcBean);
                        holder.time_under_image_receiver.setText(dd9);
                        holder.receiver_name2.setText(holder.receiver_name_name);
                        holder.receiver_side_location_iconview.setVisibility(View.GONE);
                        holder.progress_download.setVisibility(View.GONE);
                    }
                } else if (gcBean.getMimeType() != null && !gcBean.getMimeType().equalsIgnoreCase("") && gcBean.getMimeType().equals("leaveRequest")) {
                    String imageFile = gcBean.getTaskDescription();
                    holder.both_side_list_image.setGravity(View.GONE);
                    holder.sender_side_list_image.setVisibility(View.GONE);
                    holder.receiver_side_list_image.setVisibility(View.GONE);
//                    holder.text_header.setVisibility(View.VISIBLE);
                    holder.text_views.setVisibility(View.VISIBLE);
                    holder.date_header_text.setVisibility(View.VISIBLE);
                    holder.sender_side_add_txt.setVisibility(View.GONE);

                    if (imageFile != null && !imageFile.equalsIgnoreCase("")) {
                        holder.text_views.setVisibility(View.VISIBLE);
                        if (gcBean.getFromUserName() != null && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            holder.sender_side_add_txt.setVisibility(View.VISIBLE);
                            holder.receiver_side_add_txt.setVisibility(View.GONE);
                            LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) holder.timeview.getLayoutParams();
                            lllp.gravity = Gravity.RIGHT;
                            holder.timeview.setLayoutParams(lllp);
                            if ((gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("overdue")) || (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("reminder"))) {
                                holder.txt_des_under.setBackgroundResource(R.drawable.redsender1);
                                holder.txt_des.setText(gcBean.getTaskDescription());
                            } else {
                                holder.txt_des_under.setBackgroundResource(R.drawable.in_message_bg);
                                holder.txt_des.setText(gcBean.getTaskDescription());
                            }
                            if (gcBean.getTaskStatus() != null && gcBean.getMimeType().equals("leaveRequest")) {
                                String date_hdr = "Leave Request";
                                String startdate = "";
                                String enddate = "";
                                if (DateFormat.is24HourFormat(context)) {
                                    startdate = Appreference.ChangeDevicePattern(true, gcBean.getPlannedStartDateTime(), datepattern);
                                    enddate = Appreference.ChangeDevicePattern(true, gcBean.getPlannedEndDateTime(), datepattern);
                                } else {
                                    startdate = Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, gcBean.getPlannedStartDateTime()), datepattern);
                                    enddate = Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, gcBean.getPlannedEndDateTime()), datepattern);
                                }
                                Log.i("MediaListAdapter", "leave Sender OwnerOftask,loginUser,fromUser,toUSer " + gcBean.getOwnerOfTask() + " " + Appreference.loginuserdetails.getUsername() + " " + gcBean.getFromUserName() + " " + gcBean.getToUserName());
                                String alldate2 = date_hdr + "\n" + "From Date : " + startdate + "\n" + "To Date : " + enddate;
                                holder.txt_des.setText(alldate2);
                            } else {
                                holder.txt_des.setText(gcBean.getTaskDescription());
                            }
                            if (gcBean.getSendStatus() != null && gcBean.getSendStatus().equalsIgnoreCase("1")) {
                                holder.remove_btn.setVisibility(View.VISIBLE);
                                holder.iv_txtstatus.setVisibility(View.GONE);
                            } else {
                                holder.remove_btn.setVisibility(View.GONE);
                                holder.iv_txtstatus.setVisibility(View.VISIBLE);
                            }
                            if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("private")) {
                                holder.txt_des.setTextColor(Color.WHITE);
                            } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDescription")) {
                                holder.txt_des.setTextColor(Color.RED);
                            } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("percentageCompleted")) {
                                holder.txt_des.setTextColor(Color.BLACK);
                            } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDateChangedRequest")) {
                                holder.txt_des.setTextColor(Color.BLACK);
                            } else {
                                holder.txt_des.setTextColor(Color.BLACK);
                                holder.time.setTextColor(Color.WHITE);
                            }
                        } else {
                            holder.sender_side_add_txt.setVisibility(View.GONE);
                            holder.receiver_side_add_txt.setVisibility(View.VISIBLE);
                            LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) holder.timeview.getLayoutParams();
                            lllp.gravity = Gravity.LEFT;
                            holder.timeview.setLayoutParams(lllp);
                            if (gcBean.getMimeType().equals("leaveRequest")) {
                                if (gcBean.getTaskStatus() != null && (gcBean.getTaskStatus().equalsIgnoreCase("overdue") || gcBean.getTaskStatus().equalsIgnoreCase("reminder"))) {
                                    holder.receiver_side_description_layout.setBackgroundResource(R.drawable.redreceiver1);
                                } else {
                                    holder.receiver_side_description_layout.setBackgroundResource(R.drawable.grey_im);
                                }
                                String date_hdr = "Leave Request", startdate = "", enddate = "";
                                if (DateFormat.is24HourFormat(context)) {
                                    startdate = Appreference.ChangeDevicePattern(true, gcBean.getPlannedStartDateTime(), datepattern);
                                    enddate = Appreference.ChangeDevicePattern(true, gcBean.getPlannedEndDateTime(), datepattern);
                                } else {
                                    startdate = Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, gcBean.getPlannedStartDateTime()), datepattern);
                                    enddate = Appreference.ChangeDevicePattern(false, Appreference.setDateTime(false, gcBean.getPlannedEndDateTime()), datepattern);
                                }
                                Log.i("MediaListAdapter", "leave Receiver OwnerOftask,loginUser,fromUser,toUSer " + gcBean.getOwnerOfTask() + " " + Appreference.loginuserdetails.getUsername() + " " + gcBean.getFromUserName() + " " + gcBean.getToUserName());
                                groupMemberAccess = VideoCallDataBase.getDB(context).getMemberAccessList(gcBean.getToUserId());
                                if (!gcBean.getOwnerOfTask().equalsIgnoreCase("") && gcBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    if ((gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("Group") && groupMemberAccess.getApproveLeave() != null && groupMemberAccess.getApproveLeave().equalsIgnoreCase("1") && gcBean.getMsg_status() == 10) || (gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("individual") && gcBean.getMsg_status() == 10) || (gcBean.getProjectId() != null && !gcBean.getProjectId().equalsIgnoreCase(null) && !gcBean.getProjectId().equalsIgnoreCase("") && !gcBean.getProjectId().equalsIgnoreCase("null") && gcBean.getMsg_status() == 10)) {
                                        holder.leave.setVisibility(View.VISIBLE);
                                    } else {
                                        holder.leave.setVisibility(View.GONE);
                                    }
                                    date_hdr = "Leave Request";
                                    String alldate1 = date_hdr + "\n" + "From Date : " + startdate + "\n" + "To Date : " + enddate;
                                    holder.rcv_des.setText(alldate1);
                                    String dd41 = "";
                                    dd41 = setConversationTime(gcBean);
                                    holder.time_under_text_receiver.setText(dd41);
                                    holder.receiver_name.setText(holder.receiver_name_name);
                                } else if (!gcBean.getOwnerOfTask().equalsIgnoreCase("") && !gcBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    date_hdr = "Leave Request";
                                    String alldate1 = date_hdr + "\n" + "From Date : " + startdate + "\n" + "To Date : " + enddate;
                                    holder.rcv_des.setText(alldate1);
                                    String dd42 = "";
                                    dd42 = setConversationTime(gcBean);
                                    holder.time_under_text_receiver.setText(dd42);
                                    holder.receiver_name.setText(holder.receiver_name_name);
                                }
                            } else {
                                if (gcBean.getTaskStatus() != null && gcBean.getTaskStatus().equalsIgnoreCase("overdue")) {
                                    holder.receiver_side_description_layout.setBackgroundResource(R.drawable.redreceiver1);
                                    holder.rcv_des.setText(gcBean.getTaskDescription());
                                    String dd53 = "";
                                    dd53 = setConversationTime(gcBean);
                                    holder.time_under_text_receiver.setText(dd53);
                                    holder.receiver_name.setText(holder.receiver_name_name);
                                    holder.dateChangeRequest_icon.setVisibility(View.GONE);
                                    holder.dateChangeApproval_icon.setVisibility(View.GONE);
                                } else {
                                    holder.receiver_side_description_layout.setBackgroundResource(R.drawable.grey_im);
                                    holder.rcv_des.setText(gcBean.getTaskDescription());
                                    String dd54 = "";
                                    dd54 = setConversationTime(gcBean);
                                    holder.time_under_text_receiver.setText(dd54);
                                    holder.receiver_name.setText(holder.receiver_name_name);
                                    holder.dateChangeRequest_icon.setVisibility(View.GONE);
                                    holder.dateChangeApproval_icon.setVisibility(View.GONE);
                                }
                            }
                            if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("private")) {
                                holder.rcv_des.setTextColor(Color.BLACK);
                            } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDescription")) {
                                holder.rcv_des.setTextColor(Color.RED);
                            } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("percentageCompleted")) {
                                holder.rcv_des.setTextColor(Color.BLACK);
                            } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDateChangedRequest")) {
                                holder.rcv_des.setTextColor(Color.BLACK);
                            } else {
                                holder.rcv_des.setTextColor(Color.BLACK);
                                holder.time.setTextColor(Color.BLACK);
                            }

                            if (gcBean.getMimeType().equalsIgnoreCase("leaveRequest")) {
                                holder.rcv_des.setTextColor(Color.BLACK);
                            }
                        }
                    }

                } else if (gcBean.getMimeType() != null && (gcBean.getMimeType().equalsIgnoreCase("textfile"))) {
                    String imageFile = gcBean.getTaskDescription();
                    holder.both_side_list_image.setGravity(View.GONE);
                    holder.sender_side_list_image.setVisibility(View.GONE);
                    holder.receiver_side_list_image.setVisibility(View.GONE);
//                    holder.text_header.setVisibility(View.VISIBLE);
                    holder.text_views.setVisibility(View.VISIBLE);
                    holder.date_header_text.setVisibility(View.VISIBLE);
                    holder.sender_side_add_txt.setVisibility(View.GONE);

                    if (imageFile != null && !imageFile.equalsIgnoreCase("")) {
                        holder.text_views.setVisibility(View.VISIBLE);
                        if (gcBean.getFromUserName() != null && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            holder.sender_side_add_txt.setVisibility(View.VISIBLE);
                            holder.receiver_side_add_txt.setVisibility(View.GONE);
                            holder.send_readmore.setVisibility(View.VISIBLE);
                            LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) holder.timeview.getLayoutParams();
                            lllp.gravity = Gravity.RIGHT;
                            holder.timeview.setLayoutParams(lllp);
                            if (gcBean.getTaskStatus() != null && gcBean.getSender_reply() != null) {
                                Log.i("ReplyTask", "getsender_reply() is--->" + gcBean.getSender_reply());
                                Log.i("ReplyTask", "getSignalId is--->" + gcBean.getSignalid());
                                Log.i("ReplyTask", "getTaskId is--->" + gcBean.getTaskId());
                                Log.i("ReplyTask", "getReplySenderName is--->" + gcBean.getReply_sender_name());
                                if (gcBean.getSender_reply().contains(".jpg")) {
                                    holder.reply_sender_linear.setVisibility(View.VISIBLE);
//                                    Bitmap bitmapfile=BitmapFactory.decodeFile(gcBean.getSender_reply());
//                                    Bitmap compressbitmap=ThumbnailUtils.extractThumbnail(bitmapfile,60,60);
//                                    holder.reply_sender_image.setImageBitmap(compressbitmap);
                                    holder.reply_sender_msg.setText("Photo");
                                    holder.reply_sender_msg.setGravity(Gravity.CENTER);
//                                    holder.reply_sender_msg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.camera_for_reply,0,0,0);
                                    holder.reply_sender_name.setText(gcBean.getReply_sender_name());
                                    holder.txt_des_under.setMinimumWidth(225);
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Bitmap bitmapfile = BitmapFactory.decodeFile(gcBean.getSender_reply());
//                                            Bitmap compressbitmap2=Bitmap.createScaledBitmap(bitmapfile,50,50,false);
                                            Drawable drawable = new BitmapDrawable(bitmapfile);
//                                            Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(gcBean.getSender_reply()), 40, 40);
//                                            holder.reply_sender_image.setImageBitmap(bitmapfile);
                                            holder.reply_sender_image.setImageDrawable(drawable);
                                        }
                                    });
                                } else {
                                    holder.reply_sender_linear.setVisibility(View.VISIBLE);
                                    holder.txt_des_under.setMinimumWidth(185);
                                    holder.reply_sender_image.setVisibility(View.GONE);
                                    holder.reply_sender_msg.setText(gcBean.getSender_reply());
                                    holder.reply_sender_name.setText(gcBean.getReply_sender_name());
                                }
                            }
                            if ((gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("overdue")) || (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("reminder"))) {
                                holder.txt_des_under.setBackgroundResource(R.drawable.redsender1);
                                holder.txt_des.setText(gcBean.getTaskDescription());
                            } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDescription") && !gcBean.getMimeType().equalsIgnoreCase("date")) {
                                holder.txt_des_under.setBackgroundResource(R.drawable.yellowsender1);
                                holder.txt_des.setTextColor(Color.RED);
                                holder.time_under_text.setTextColor(Color.RED);
                                holder.txt_des.setText(gcBean.getTaskDescription());
                            } else if (gcBean.getSubType() != null && (gcBean.getSubType().equalsIgnoreCase("customeHeaderAttribute") || gcBean.getSubType().equalsIgnoreCase("customeAttribute"))) {
                                holder.txt_des_under.setBackgroundResource(R.drawable.in_message_bg);
                                String custom_tag = gcBean.getTaskTagName() + " : " + gcBean.getTaskDescription();
                                holder.txt_des.setText(custom_tag);
                                Log.i("MediaListAdapter", "CustomeAttribute----> 6 " + custom_tag);
                            } else {
                                holder.txt_des_under.setBackgroundResource(R.drawable.in_message_bg);
                                String myData = "";
                                try {
                                    FileInputStream fis = new FileInputStream(gcBean.getTaskDescription());
                                    DataInputStream in = new DataInputStream(fis);
                                    BufferedReader br =
                                            new BufferedReader(new InputStreamReader(in));
                                    String strLine;
                                    while ((strLine = br.readLine()) != null) {
                                        myData = myData + strLine;
                                    }
                                    in.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Appreference.printLog("MediaListAdapter", "txt_des_under Exception: " + e.getMessage(), "WARN", null);
                                }
                                holder.txt_des.setText(myData);
                            }

                            if (gcBean.getTaskStatus() != null && gcBean.getMimeType().equals("date") && gcBean.getTaskStatus().equalsIgnoreCase("draft")) {
                                String date_hdr = "Task Duration";
                                String duration = gcBean.getDuration();
                                String duration_units = gcBean.getDurationUnit();
                                String repeatfreq = gcBean.getTimeFrequency();
                                String task_duration = date_hdr + "\n" + "Duration : " + duration + "\n" + "Duration units : " + duration_units + "\n" + "Repeat Frequency : " + repeatfreq;
                                holder.txt_des.setText(task_duration);
                            } else if (gcBean.getMimeType().equals("date")) {
                                String alldate2 = "";
                                String date_hdr = gcBean.getRequestStatus();
                                String startdate = gcBean.getPlannedStartDateTime();
                                String enddate = gcBean.getPlannedEndDateTime();
                                String remdate = gcBean.getRemainderFrequency();
                                String remfreq = gcBean.getTimeFrequency();
                                Log.i("MediaListAdapter", "date Sender OwnerOftask,loginUser,fromUser,toUSer " + gcBean.getOwnerOfTask() + " " + Appreference.loginuserdetails.getUsername() + " " + gcBean.getFromUserName() + " " + gcBean.getToUserName());
                                if (date_hdr.equalsIgnoreCase("assigned")) {
                                    date_hdr = "Reminder Details";
                                } else if (date_hdr.equalsIgnoreCase("requested")) {
                                    date_hdr = "date change request";
                                } else if (date_hdr.equalsIgnoreCase("approved")) {
                                    date_hdr = "date change request approved";
                                } else if (date_hdr.equalsIgnoreCase("rejected")) {
                                    date_hdr = "date change request rejected";
                                }
                                alldate2 = date_hdr + "\n" + "Start Date : " + startdate + "\n" + "End Date : " + enddate + "\n" + "Reminder Time : " + remdate + "\n" + "Reminder Frequency : " + remfreq;
                                holder.txt_des.setText(alldate2);
                            }
                            if (gcBean.getSendStatus() != null && gcBean.getSendStatus().equalsIgnoreCase("1")) {
                                holder.remove_btn.setVisibility(View.VISIBLE);
                                holder.send_readmore.setVisibility(View.VISIBLE);
                                holder.iv_txtstatus.setVisibility(View.GONE);
                            } else {
                                holder.send_readmore.setVisibility(View.VISIBLE);
                                holder.remove_btn.setVisibility(View.GONE);
                                holder.iv_txtstatus.setVisibility(View.VISIBLE);
                            }
                            if (gcBean.getLongmessage() != null && gcBean.getLongmessage().equalsIgnoreCase("0")) {
                                holder.txt_des.setMaxLines(2);
                                holder.send_readmore.setVisibility(View.VISIBLE);
                            } else if (gcBean.getLongmessage() != null && gcBean.getLongmessage().equalsIgnoreCase("1")) {
                                holder.txt_des.setMaxLines(Integer.MAX_VALUE);
                                holder.send_readmore.setVisibility(View.GONE);
                            }
                            if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("private")) {
                                holder.txt_des.setTextColor(Color.WHITE);
                            } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDescription")) {
                                holder.txt_des.setTextColor(Color.RED);
                            } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("percentageCompleted")) {
                                holder.txt_des.setTextColor(Color.BLACK);
                            } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDateChangedRequest")) {
                                holder.txt_des.setTextColor(Color.BLACK);
                            } else {
                                holder.txt_des.setTextColor(Color.BLACK);
                                holder.time.setTextColor(Color.WHITE);
                            }
                        } else {
                            holder.sender_side_add_txt.setVisibility(View.GONE);
                            holder.rec_readmore.setVisibility(View.VISIBLE);
                            holder.receiver_side_add_txt.setVisibility(View.VISIBLE);
                            LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) holder.timeview.getLayoutParams();
                            lllp.gravity = Gravity.LEFT;
                            holder.timeview.setLayoutParams(lllp);
                            if (gcBean.getMimeType().equals("date") && gcBean.getTaskStatus().equalsIgnoreCase("draft")) {
                                String date_hdr = "Task Duration";
                                String duration = gcBean.getDuration();
                                String duration_units = gcBean.getDurationUnit();
                                String repeatfreq = gcBean.getTimeFrequency();
                                String task_duration = date_hdr + "\n" + "Duration : " + duration + "\n" + "Duration units : " + duration_units + "\n" + "Repeat Frequency : " + repeatfreq;
                                holder.txt_des.setText(task_duration);
                            } else if (gcBean.getMimeType().equals("date")) {
                                String date_hdr;
                                String startdate = gcBean.getPlannedStartDateTime();
                                String enddate = gcBean.getPlannedEndDateTime();
                                String remdate = gcBean.getRemainderFrequency();
                                String remfreq = gcBean.getTimeFrequency();

                                if (gcBean.getIsRemainderRequired().equalsIgnoreCase("N")) {
                                    remdate = "N/A";
                                    remfreq = "N/A";
                                } else if (gcBean.getIsRemainderRequired().equalsIgnoreCase("R")) {
                                    remfreq = "N/A";
                                }
                                Log.i("MediaListAdapter", "date Receiver 1 OwnerOftask,loginUser,fromUser,toUSer " + gcBean.getOwnerOfTask() + " " + Appreference.loginuserdetails.getUsername() + " " + gcBean.getFromUserName() + " " + gcBean.getToUserName());
                                if (gcBean.getRequestStatus() != null && !gcBean.getRequestStatus().equalsIgnoreCase("") && !gcBean.getRequestStatus().equalsIgnoreCase(null)) {
                                    if (gcBean.getRequestStatus().equalsIgnoreCase("requested") && !gcBean.getOwnerOfTask().equalsIgnoreCase("") && gcBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                        if ((gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("Group") && groupMemberAccess.getRespondDateChange() != null && groupMemberAccess.getRespondDateChange().equalsIgnoreCase("1") && gcBean.getMsg_status() == 10) || (gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("individual") && gcBean.getMsg_status() == 10) || (gcBean.getProjectId() != null && !gcBean.getProjectId().equalsIgnoreCase(null) && !gcBean.getProjectId().equalsIgnoreCase("") && !gcBean.getProjectId().equalsIgnoreCase("null") && gcBean.getMsg_status() == 10)) {
                                            Log.i("overdue", "overdue -->3 ");
                                            holder.dateChangeApproval_icon.setVisibility(View.VISIBLE);
                                        }
                                        date_hdr = "date change request";
                                        String alldate1 = date_hdr + "\n" + "Start Date : " + startdate + "\n" + "End Date : " + enddate + "\n" + "Reminder Time : " + remdate + "\n" + "Reminder Frequency : " + remfreq;
                                        holder.rcv_des.setText(alldate1);
                                        String dd36 = "";
                                        dd36 = setConversationTime(gcBean);
                                        holder.time_under_text_receiver.setText(dd36);
                                        holder.receiver_name.setText(holder.receiver_name_name);
                                    } else if (gcBean.getRequestStatus().equalsIgnoreCase("requested") && gcBean.getTaskType().equalsIgnoreCase("Group")) {
                                        if ((gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("Group") && groupMemberAccess.getRespondDateChange() != null && groupMemberAccess.getRespondDateChange().equalsIgnoreCase("1") && gcBean.getMsg_status() == 10) || (gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("individual") && gcBean.getMsg_status() == 10) || (gcBean.getProjectId() != null && !gcBean.getProjectId().equalsIgnoreCase(null) && !gcBean.getProjectId().equalsIgnoreCase("") && !gcBean.getProjectId().equalsIgnoreCase("null") && gcBean.getMsg_status() == 10)) {
                                            Log.i("task", "date Requested not same user " + gcBean.getMsg_status());
                                            Log.i("overdue", "overdue -->9 ");
                                            holder.dateChangeRequest_icon.setVisibility(View.VISIBLE);
                                        }
                                        date_hdr = "date change request";
                                        String alldate1 = date_hdr + "\n" + "Start Date : " + startdate + "\n" + "End Date : " + enddate + "\n" + "Reminder Time : " + remdate + "\n" + "Reminder Frequency : " + remfreq;
                                        holder.rcv_des.setText(alldate1);
                                        String dd37 = "";
                                        dd37 = setConversationTime(gcBean);
                                        holder.time_under_text_receiver.setText(dd37);
                                        holder.receiver_name.setText(holder.receiver_name_name);
                                    } else if (gcBean.getRequestStatus().equalsIgnoreCase("approved")) {
                                        date_hdr = "date change request approved";
                                        if ((gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("Group") && groupMemberAccess.getRespondDateChange() != null && groupMemberAccess.getRespondDateChange().equalsIgnoreCase("1") && gcBean.getMsg_status() == 10) || (gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("individual") && gcBean.getMsg_status() == 10) || (gcBean.getProjectId() != null && !gcBean.getProjectId().equalsIgnoreCase(null) && !gcBean.getProjectId().equalsIgnoreCase("") && !gcBean.getProjectId().equalsIgnoreCase("null") && gcBean.getMsg_status() == 10)) {
                                            Log.i("task", "date Approval " + gcBean.getMsg_status());
                                            Log.i("overdue", "overdue -->10 ");
                                            holder.dateChangeRequest_icon.setVisibility(View.VISIBLE);
                                        }
                                        String alldate1 = date_hdr + "\n" + "Start Date : " + startdate + "\n" + "End Date : " + enddate + "\n" + "Reminder Time : " + remdate + "\n" + "Reminder Frequency : " + remfreq;
                                        holder.rcv_des.setText(alldate1);
                                        String dd38 = "";
                                        dd38 = setConversationTime(gcBean);
                                        holder.time_under_text_receiver.setText(dd38);
                                        holder.receiver_name.setText(holder.receiver_name_name);
                                    } else if (gcBean.getRequestStatus().equalsIgnoreCase("rejected")) {
                                        date_hdr = "date change request rejected";
                                        if ((gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("Group") && groupMemberAccess.getRespondDateChange() != null && groupMemberAccess.getRespondDateChange().equalsIgnoreCase("1") && gcBean.getMsg_status() == 10) || (gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("individual") && gcBean.getMsg_status() == 10) || (gcBean.getProjectId() != null && !gcBean.getProjectId().equalsIgnoreCase(null) && !gcBean.getProjectId().equalsIgnoreCase("") && !gcBean.getProjectId().equalsIgnoreCase("null") && gcBean.getMsg_status() == 10)) {
                                            Log.i("task", "date Rejected " + gcBean.getMsg_status());
                                            Log.i("overdue", "overdue -->11 ");
                                            holder.dateChangeRequest_icon.setVisibility(View.VISIBLE);
                                        }
                                        String alldate1 = date_hdr + "\n" + "Start Date : " + startdate + "\n" + "End Date : " + enddate + "\n" + "Reminder Time : " + remdate + "\n" + "Reminder Frequency : " + remfreq;
                                        holder.rcv_des.setText(alldate1);
                                        String dd39 = "";
                                        dd39 = setConversationTime(gcBean);
                                        holder.time_under_text_receiver.setText(dd39);
                                        holder.receiver_name.setText(holder.receiver_name_name);
                                    } else {
                                        date_hdr = "Reminder Details";
                                        if ((gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("Group") && groupMemberAccess.getRespondDateChange() != null && groupMemberAccess.getRespondDateChange().equalsIgnoreCase("1") && gcBean.getMsg_status() == 10) || (gcBean.getTaskType() != null && gcBean.getTaskType().equalsIgnoreCase("individual") && gcBean.getMsg_status() == 10) || (gcBean.getProjectId() != null && !gcBean.getProjectId().equalsIgnoreCase(null) && !gcBean.getProjectId().equalsIgnoreCase("") && !gcBean.getProjectId().equalsIgnoreCase("null") && gcBean.getMsg_status() == 10)) {
                                            Log.i("task", "date Assigned " + gcBean.getMsg_status());
                                            Log.i("overdue", "overdue -->12 ");
                                            holder.dateChangeRequest_icon.setVisibility(View.VISIBLE);
                                            Log.i("groupMemberAccess", "ML if1  " + groupMemberAccess.getRespondDateChange());
                                        } else {
                                            holder.dateChangeRequest_icon.setVisibility(View.GONE);
                                            Log.i("groupMemberAccess", "ML else1  ");
                                        }
                                        String alldate1 = date_hdr + "\n" + "Start Date : " + startdate + "\n" + "End Date : " + enddate + "\n" + "Reminder Time : " + remdate + "\n" + "Reminder Frequency : " + remfreq;
                                        holder.rcv_des.setText(alldate1);
                                        String dd40 = "";
                                        dd40 = setConversationTime(gcBean);
                                        holder.time_under_text_receiver.setText(dd40);
                                        holder.receiver_name.setText(holder.receiver_name_name);
                                    }
                                }
                            } else {
                                if ((gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("overdue")) || (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("reminder"))) {
                                    holder.receiver_side_description_layout.setBackgroundResource(R.drawable.redreceiver1);
                                    holder.rcv_des.setText(gcBean.getTaskDescription());
                                    String dd55 = "";
                                    dd55 = setConversationTime(gcBean);
                                    holder.time_under_text_receiver.setText(dd55);
                                    holder.receiver_name.setText(holder.receiver_name_name);
                                    holder.dateChangeRequest_icon.setVisibility(View.GONE);
                                    holder.dateChangeApproval_icon.setVisibility(View.GONE);
                                } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDescription") && !gcBean.getMimeType().equalsIgnoreCase("date")) {
                                    holder.receiver_side_description_layout.setBackgroundResource(R.drawable.yellowreceiver1);
                                    holder.rcv_des.setText(gcBean.getTaskDescription());
                                    String dd56 = "";
                                    dd56 = setConversationTime(gcBean);
                                    holder.time_under_text_receiver.setText(dd56);
                                    holder.receiver_name.setText(holder.receiver_name_name);
                                    holder.dateChangeRequest_icon.setVisibility(View.GONE);
                                    holder.dateChangeApproval_icon.setVisibility(View.GONE);
                                } else {
                                    holder.receiver_side_description_layout.setBackgroundResource(R.drawable.grey_im);
                                    String myData = "", filepath = dir_path + gcBean.getTaskDescription();
                                    try {
                                        FileInputStream fis = new FileInputStream(filepath);
                                        DataInputStream in = new DataInputStream(fis);
                                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                                        String strLine;
                                        while ((strLine = br.readLine()) != null) {
                                            myData = myData + strLine;
                                        }
                                        in.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Appreference.printLog("MediaListAdapter", "receiver_side_description_layout Exception: " + e.getMessage(), "WARN", null);
                                    }
                                    holder.rcv_des.setText(myData);
                                    holder.dateChangeRequest_icon.setVisibility(View.GONE);
                                    holder.dateChangeApproval_icon.setVisibility(View.GONE);
                                    if (gcBean.getLongmessage() != null && gcBean.getLongmessage().equalsIgnoreCase("0")) {
                                        holder.rcv_des.setMaxLines(2);
                                        holder.rec_readmore.setVisibility(View.VISIBLE);
                                    } else if (gcBean.getLongmessage() != null && gcBean.getLongmessage().equalsIgnoreCase("1")) {
                                        holder.rcv_des.setMaxLines(Integer.MAX_VALUE);
                                        holder.rec_readmore.setVisibility(View.GONE);
                                    }
                                }
                            }
                            if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("private")) {
                                holder.rcv_des.setTextColor(Color.BLACK);
                            } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDescription") && !gcBean.getMimeType().equalsIgnoreCase("date")) {
                                holder.rcv_des.setTextColor(Color.RED);
                            } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("percentageCompleted") && !gcBean.getMimeType().equalsIgnoreCase("date")) {
                                holder.rcv_des.setTextColor(Color.BLACK);
                            } else if (gcBean.getSubType() != null && gcBean.getSubType().equalsIgnoreCase("taskDateChangedRequest") && !gcBean.getMimeType().equalsIgnoreCase("date")) {
                                holder.rcv_des.setTextColor(Color.BLACK);
                            } else if (gcBean.getSubType() != null && (gcBean.getSubType().equalsIgnoreCase("customeHeaderAttribute") || gcBean.getSubType().equalsIgnoreCase("customeAttribute"))) {
                                String custom_tag = gcBean.getTaskTagName() + " : " + gcBean.getTaskDescription();
                                holder.rcv_des.setText(custom_tag);
                                holder.rcv_des.setTextColor(Color.BLACK);
                                Log.i("MediaListAdapter", "CustomeAttribute----> 7 " + custom_tag);
                            } else {
                                holder.rcv_des.setTextColor(Color.BLACK);
                            }
                        }
                    }
                } else {
                    Log.i("task", " Nothing matched - else method");
                }
                String observeres = null;
                if (gcBean.getProjectId() != null) {
                    observeres = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskObservers from projectHistory where taskId='" + gcBean.getTaskId() + "'");
                } else {
                    observeres = VideoCallDataBase.getDB(context).getProjectParentTaskId("select taskObservers from taskHistoryInfo where taskId='" + gcBean.getTaskId() + "'");
                }
                Log.i("task", " Nothing observeres " + observeres);
                if (observeres != null) {
                    if (observeres.contains(",")) {
                        String nm[] = observeres.split(",");
                        for (String user : nm) {
                            if (!user.equalsIgnoreCase(gcBean.getTaskReceiver())) {
                                holder.dateChangeRequest_icon.setVisibility(View.GONE);
                            } else {
                                Log.i("overdue", "overdue -->13 ");
                                holder.dateChangeRequest_icon.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        if (observeres.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            holder.dateChangeRequest_icon.setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MediaListAdapter", "observers Exception: " + e.getMessage(), "WARN", null);
            return null;
        }
        return view;
    }

    private String setConversationTime(TaskDetailsBean gcBean) {
        String dd = null;
        try {
            dd = "";
            if (DateFormat.is24HourFormat(context)) {
                dd = gcBean.getTasktime();
            } else {
                if (gcBean.getTasktime() != null)
                    dd = Appreference.setTaskTime(false, gcBean.getTasktime());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MediaListAdapter", "setConversationTime Exception: " + e.getMessage(), "WARN", null);
        }
        return dd;
    }

    private void setDocumentDataType(File url) {
        try {
            if (url.exists()) {
                Uri uri = Uri.fromFile(url);
                Intent intentdocument = new Intent(Intent.ACTION_VIEW);
                if (url.toString().contains(".doc") || url.toString().contains(".docx")) {  // Word document
                    intentdocument.setDataAndType(uri, "application/msword");
                } else if (url.toString().contains(".pdf")) {   // PDF file
                    intentdocument.setDataAndType(uri, "application/pdf");
                } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {   // Powerpoint file
                    intentdocument.setDataAndType(uri, "application/vnd.ms-powerpoint");
                } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {   // Excel file
                    intentdocument.setDataAndType(uri, "application/vnd.ms-excel");
                } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {    // WAV audio file
                    intentdocument.setDataAndType(uri, "application/x-wav");
                } else if (url.toString().contains(".rtf")) {   // RTF file
                    intentdocument.setDataAndType(uri, "application/rtf");
                } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {    // WAV audio file
                    intentdocument.setDataAndType(uri, "audio/x-wav");
                } else if (url.toString().contains(".gif")) {   // GIF file
                    intentdocument.setDataAndType(uri, "image/gif");
                } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {    // JPG file
                    intentdocument.setDataAndType(uri, "image/jpeg");
                } else if (url.toString().contains(".txt")) {   // Text file
                    intentdocument.setDataAndType(uri, "text/plain");
                } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {   // Video files
                    intentdocument.setDataAndType(uri, "video/*");
                } else {
                    //if you want you can also define the intent type for any other file
                    //additionally use else clause below, to manage other unknown extensions
                    //in this case, Android will show all applications installed on the device
                    //so you can choose which application to use
                    intentdocument.setDataAndType(uri, "*/*");
                }
                intentdocument.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                try {
                    context.startActivity(intentdocument);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MediaListAdapter", "setDocumentDataType Exception: " + e.getMessage(), "WARN", null);
                }
            } else {
                Toast.makeText(getContext(), "can't open", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MediaListAdapter", "setDocumentDataType Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void stopPlayback() {
        try {
            mPlayingPosition = -1;
            if (mPlayer != null && mPlayer.isPlaying())
                mPlayer.stop();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Appreference.printLog("MediaListAdapter", "stopPlayback Exception: " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MediaListAdapter", "stopPlayback Exception: " + e.getMessage(), "WARN", null);
        }
    }

    private void playAudio(String fname, int position) {
        try {
            mPlayer.reset();
            mPlayer.setDataSource(fname);
            mPlayer.prepare();
            mPlayer.start();
            play = true;
            starttime = mPlayer.getCurrentPosition();
            mPlayingPosition = position;
            holder.seekBar.setMax(mPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
            stopPlayback();
            Appreference.printLog("MediaListAdapter", "playAudio Exception: " + e.getMessage(), "WARN", null);
        } catch (Exception e) {
            e.printStackTrace();
            stopPlayback();
            Appreference.printLog("MediaListAdapter", "playAudio Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public static void getTaskbeanforreply(TaskDetailsBean mediaListBean) {
        try {
            Log.i("ReplyTask", mediaListBean.getMimeType());
            Log.i("ReplyTask", mediaListBean.getTaskDescription());
            Log.i("ReplyTask", mediaListBean.getToUserName());
            replyusername = mediaListBean.getToUserName();
            replytaskDescription = mediaListBean.getTaskDescription();
            mimetypeforreply = mediaListBean.getMimeType();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MediaListAdapter", "getTaskbeanforreply Exception: " + e.getMessage(), "WARN", null);
        }

    }

    public static enum Action {
        LR, // Left to Right
        RL, // Right to Left
        TB, // Top to bottom
        BT, // Bottom to Top
        None // when no action was detected
    }

    public interface onClick {

        void onClick(TaskDetailsBean gcBean, ViewHolder v, int position, View view);

        void onLongClick(int position, View view);

        void showMedialistProgress();

        void dateSendORApprovalORReject(TaskDetailsBean detailsBean, String accOrReject);
    }

    public void confictWebservice(TaskDetailsBean detailsBean) {
        try {
            NewTaskConversation.conflict = Appreference.conflicttask;
            Log.i("(MediaListAdapter", "confictWebservice fromUserId,toUserId,startdate,enddate " + String.valueOf(detailsBean.getFromUserId()) + " " + String.valueOf(detailsBean.getToUserId()) + " " + detailsBean.getPlannedStartDateTime() + " " + detailsBean.getPlannedEndDateTime());
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
            nameValuePairs.add(new BasicNameValuePair("fromUserId", String.valueOf(detailsBean.getFromUserId())));
            nameValuePairs.add(new BasicNameValuePair("toUserId", String.valueOf(detailsBean.getToUserId())));
            nameValuePairs.add(new BasicNameValuePair("plannedStartDate", detailsBean.getPlannedStartDateTime()));
            nameValuePairs.add(new BasicNameValuePair("plannedEndDate", detailsBean.getPlannedEndDateTime()));
            Appreference.jsonRequestSender.checkConflicts(EnumJsonWebservicename.checkConflicts, nameValuePairs, (NewTaskConversation) context);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MediaListAdapter", "confictWebservice Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public void exclationmenu(final TaskDetailsBean mediaListBean, final View view, final Context adapter_context) {
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialogexclation);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog1.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.horizontalMargin = 15;
        Window window = dialog1.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setAttributes(lp);
        window.setGravity(Gravity.CENTER);
        LinearLayout Resolve = (LinearLayout) dialog1.findViewById(R.id.Resolve);
        LinearLayout Ignore = (LinearLayout) dialog1.findViewById(R.id.Ignore);
        final LinearLayout cancel_ll = (LinearLayout) dialog1.findViewById(R.id.cancel_ll);
        TextView okk = (TextView) dialog1.findViewById(R.id.okk);
        TextView action = (TextView) dialog1.findViewById(R.id.action);
        TextView head = (TextView) dialog1.findViewById(R.id.head);
        head.setText(mediaListBean.getTaskDescription().replace("Escalation Added :", "Initiate Escalation for condition :"));

        if (mediaListBean.getTaskDescription().contains("Buzz")) {
            action.setText("Buzz");
            action_name = "Buzz";
        } else if (mediaListBean.getTaskDescription().contains("Reassign")) {
            action.setText("Reassign");
            action_name = "Reassign";
        } else if (mediaListBean.getTaskDescription().contains("Audio Conference")) {
            action.setText("Audio Conference");
            action_name = "Audio Conference";
        } else if (mediaListBean.getTaskDescription().contains("Add observer")) {
            action.setText("Add observer");
            action_name = "Add observer";
        } else if (mediaListBean.getTaskDescription().contains("Aband")) {
            action.setText("Abandon");
            action_name = "Aband";
        }

        okk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    VideoCallDataBase.getDB(context).updateEscalationText(mediaListBean);
                    dialog1.dismiss();
                    ((NewTaskConversation) context).refresh();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MediaListAdapter", "edit_ll Exception: " + e.getMessage(), "WARN", null);
                }
            }
        });

        action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog1.dismiss();
                    VideoCallDataBase.getDB(context).updateEscalationText(mediaListBean);
                    if (action_name != null && action_name.equalsIgnoreCase("Reassign")) {

                        String query1;
                        ArrayList<TaskDetailsBean> taskDetailsBeenlist1;
                        if (mediaListBean.getTaskType().equalsIgnoreCase("Individual")) {
                            query1 = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserName='" + Appreference.loginuserdetails.getUsername() + "' ) or ( toUserName='" + Appreference.loginuserdetails.getUsername() + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='Individual' or taskType='individual' ) and taskId ='" + mediaListBean.getTaskId() + "' group by taskId";
                            taskDetailsBeenlist1 = VideoCallDataBase.getDB(context).getTaskHistory(query1);
                            Log.i("Schedule", "query1 :" + query1 + "\n Indiv list size : " + taskDetailsBeenlist1.size());
                        } else {
                            query1 = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserId='" + mediaListBean.getToUserId() + "' ) or ( toUserId='" + mediaListBean.getToUserId() + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='Group' or taskType='group' ) and taskId ='" + mediaListBean.getTaskId() + "' group by taskId";
                            taskDetailsBeenlist1 = VideoCallDataBase.getDB(context).getTaskHistory(query1);
                            Log.i("Schedule", "query1 :" + query1 + " group list size : " + taskDetailsBeenlist1.size());
                        }

                        if (taskDetailsBeenlist1.size() > 0) {
                            TaskDetailsBean taskDetailsBean = taskDetailsBeenlist1.get(0);
//                                TaskDetailsBean taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent(taskDetailsBean.getTaskId());
                            TaskDetailsBean taskDetailsBean1 = new TaskDetailsBean();
                            if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase("null")) {
                                taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent("select * from projectHistory where taskId='" + taskDetailsBean.getTaskId() + "'");
                            } else {
                                taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent("select * from taskHistoryInfo where taskId='" + taskDetailsBean.getTaskId() + "'");
                            }
                            taskDetailsBean.setOwnerOfTask(taskDetailsBean1.getOwnerOfTask());
                            taskDetailsBean.setTaskName(taskDetailsBean1.getTaskName());
                            taskDetailsBean.setTaskReceiver(taskDetailsBean1.getTaskReceiver());
                            query1 = "select * from taskDetailsInfo where ( taskTagName = 'To' ) and ( ( fromUserName='" + Appreference.loginuserdetails.getUsername() + "' ) or ( toUserName='" + Appreference.loginuserdetails.getUsername() + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='Individual' or taskType='individual') and taskId ='" + mediaListBean.getTaskId() + "' and customSetId ='" + String.valueOf(mediaListBean.getCustomSetId()) + "' order by id DESC LIMIT 1";
                            ArrayList<TaskDetailsBean> taskDetailsBeenlist2 = VideoCallDataBase.getDB(context).getTaskHistory(query1);
                            Log.i("Schedule", "query1 tag To query1: " + query1 + " and size is " + taskDetailsBeenlist2.size());
                            String query_1 = null;
                            if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase("null")) {
                                query_1 = "select category from projectHistory where taskId='" + mediaListBean.getTaskId() + "'";
                            } else {
                                query_1 = "select category from taskHistoryInfo where taskId='" + mediaListBean.getTaskId() + "'";
                            }
                            taskDetailsBean.setCatagory(VideoCallDataBase.getDB(context).getProjectParentTaskId(query_1));
                            if (taskDetailsBeenlist2.size() > 0) {
                                Log.i("Schedule", "query1 111 :" + query1 + " group list size : " + taskDetailsBeenlist2.size());
                                TaskDetailsBean taskDetailsBean2 = taskDetailsBeenlist2.get(0);
                                taskDetailsBean.setTaskDescription(taskDetailsBean2.getTaskDescription());
                            }
                            ((NewTaskConversation) context).EscalationMethod(taskDetailsBean, view, context, mediaListBean);
                        }

                    } else if (action_name != null && action_name.equalsIgnoreCase("Audio Conference")) {
                        ((NewTaskConversation) context).EscalationMethod(mediaListBean, view, context, mediaListBean);

                    } else if (action_name != null && action_name.equalsIgnoreCase("Add observer")) {

                        String query;
                        ArrayList<TaskDetailsBean> taskDetailsBeenlist;
                        if (mediaListBean.getTaskType().equalsIgnoreCase("Individual")) {
                            query = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserName='" + Appreference.loginuserdetails.getUsername() + "' ) or ( toUserName='" + Appreference.loginuserdetails.getUsername() + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + "Individual" + "') and taskId ='" + mediaListBean.getTaskId() + "' group by taskId";
                            taskDetailsBeenlist = VideoCallDataBase.getDB(context).getTaskHistory(query);
                            Log.i("Schedule", "query :" + query + "\n Indiv list size : " + taskDetailsBeenlist.size());
                        } else {
                            query = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserId='" + mediaListBean.getToUserId() + "' ) or ( toUserId='" + mediaListBean.getToUserId() + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and taskId ='" + mediaListBean.getTaskId() + "' group by taskId";
                            taskDetailsBeenlist = VideoCallDataBase.getDB(context).getTaskHistory(query);
                            Log.i("Schedule", "query :" + query + " group list size : " + taskDetailsBeenlist.size());
                        }

                        if (taskDetailsBeenlist.size() > 0) {
                            TaskDetailsBean taskDetailsBean = taskDetailsBeenlist.get(0);
                            TaskDetailsBean taskDetailsBean1 = new TaskDetailsBean();
                            if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase("null")) {
                                taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent("select * from projectHistory where taskId='" + taskDetailsBean.getTaskId() + "'");
                            } else {
                                taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent("select * from taskHistoryInfo where taskId='" + taskDetailsBean.getTaskId() + "'");
                            }
                            taskDetailsBean.setOwnerOfTask(taskDetailsBean1.getOwnerOfTask());
                            taskDetailsBean.setTaskName(taskDetailsBean1.getTaskName());
                            taskDetailsBean.setTaskReceiver(taskDetailsBean1.getTaskReceiver());
                            Log.i("Escalation", "taskDetailsBean1.getCatagory() " + taskDetailsBean1.getCatagory());
                            taskDetailsBean.setCatagory(taskDetailsBean1.getCatagory());
                            if (mediaListBean.getTaskType() != null && mediaListBean.getTaskType().equalsIgnoreCase("Individual")) {
                                query = "select * from taskDetailsInfo where ( taskTagName = 'To' ) and ( ( fromUserName='" + Appreference.loginuserdetails.getUsername() + "' ) or ( toUserName='" + Appreference.loginuserdetails.getUsername() + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='Individual' or taskType='individual' ) and taskId ='" + mediaListBean.getTaskId() + "' and customSetId ='" + String.valueOf(mediaListBean.getCustomSetId()) + "' order by id DESC LIMIT 1";
                            } else {
                                query = "select * from taskDetailsInfo where ( taskTagName = 'To' ) and ( ( fromUserName='" + Appreference.loginuserdetails.getUsername() + "' ) or ( toUserName='" + Appreference.loginuserdetails.getUsername() + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='group' or taskType='Group') and taskId ='" + mediaListBean.getTaskId() + "' and customSetId ='" + String.valueOf(mediaListBean.getCustomSetId()) + "' order by id DESC LIMIT 1";
                            }

                            ArrayList<TaskDetailsBean> taskDetailsBeenlist1 = VideoCallDataBase.getDB(context).getTaskHistory(query);
                            Log.i("Schedule", "query observer :" + query + " observer list size : " + taskDetailsBeenlist1.size());
                            if (taskDetailsBeenlist1.size() > 0) {
                                Log.i("Schedule", "query 111 :" + query + " group list size : " + taskDetailsBeenlist1.size());
                                TaskDetailsBean taskDetailsBean2 = taskDetailsBeenlist1.get(0);
                                taskDetailsBean.setTaskDescription(taskDetailsBean2.getTaskDescription());
                            }
                            ((NewTaskConversation) context).EscalationMethod(taskDetailsBean, view, context, mediaListBean);
                        }

                    } else if (action_name != null && action_name.equalsIgnoreCase("Aband")) {

                        String query;
                        ArrayList<TaskDetailsBean> taskDetailsBeenlist;
                        if (mediaListBean.getTaskType().equalsIgnoreCase("Individual")) {
                            query = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserName='" + Appreference.loginuserdetails.getUsername() + "' ) or ( toUserName='" + Appreference.loginuserdetails.getUsername() + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + "Individual" + "') and taskId ='" + mediaListBean.getTaskId() + "' group by taskId";
                            taskDetailsBeenlist = VideoCallDataBase.getDB(context).getTaskHistory(query);
                            Log.i("Schedule", "query :" + query + "\n Indiv list size : " + taskDetailsBeenlist.size());
                        } else {
                            query = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserId='" + mediaListBean.getToUserId() + "' ) or ( toUserId='" + mediaListBean.getToUserId() + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + "Group" + "') and taskId ='" + mediaListBean.getTaskId() + "' group by taskId";
                            taskDetailsBeenlist = VideoCallDataBase.getDB(context).getTaskHistory(query);
                            Log.i("Schedule", "query :" + query + " group list size : " + taskDetailsBeenlist.size());
                        }

                        if (taskDetailsBeenlist.size() > 0) {
                            TaskDetailsBean taskDetailsBean = taskDetailsBeenlist.get(0);
//                                TaskDetailsBean taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent(taskDetailsBean.getTaskId());
                            TaskDetailsBean taskDetailsBean1 = new TaskDetailsBean();
                            if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase("null")) {
                                taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent("select * from projectHistory where taskId='" + taskDetailsBean.getTaskId() + "'");
                            } else {
                                taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent("select * from taskHistoryInfo where taskId='" + taskDetailsBean.getTaskId() + "'");
                            }
                            taskDetailsBean.setOwnerOfTask(taskDetailsBean1.getOwnerOfTask());
                            taskDetailsBean.setTaskName(taskDetailsBean1.getTaskName());
                            taskDetailsBean.setTaskReceiver(taskDetailsBean1.getTaskReceiver());
                            Log.i("Escalation", "taskDetailsBean1.getCatagory() " + taskDetailsBean1.getCatagory());
                            taskDetailsBean.setCatagory(taskDetailsBean1.getCatagory());
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String dateTime = dateFormat.format(new Date());
                            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                            String dateforrow = dateFormat.format(new Date());
                            String tasktime = dateTime;
                            tasktime = tasktime.split(" ")[1];
                            Log.i("task", "tasktime" + tasktime);
                            Log.i("UTC", "sendMessage utc time" + dateforrow);
                            Log.i("time", "value");
                            String taskUTCtime = dateforrow;
                            taskDetailsBean.setTaskUTCTime(taskUTCtime);
                            taskDetailsBean.setTaskUTCDateTime(dateforrow);
                            taskDetailsBean.setDateTime(dateforrow);
                            taskDetailsBean.setTasktime(tasktime);
                            taskDetailsBean.setTaskStatus("abandoned");
                            taskDetailsBean.setTaskDescription("This task is abandoned");  // contains ToUserName's
                            taskDetailsBean.setSignalid(Utility.getSessionID());
                            taskDetailsBean.setTaskRequestType("abandTask");
                            ((NewTaskConversation) context).EscalationMethod(taskDetailsBean, view, context, mediaListBean);
                        }

                    } else if (action_name != null && action_name.equalsIgnoreCase("Buzz")) {
                        String query;
                        ArrayList<TaskDetailsBean> taskDetailsBeenlist;
                        if (mediaListBean.getTaskType().equalsIgnoreCase("Individual")) {
                            query = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserName='" + Appreference.loginuserdetails.getUsername() + "' ) or ( toUserName='" + Appreference.loginuserdetails.getUsername() + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + "Individual" + "') and taskId ='" + mediaListBean.getTaskId() + "' group by taskId";
                            taskDetailsBeenlist = VideoCallDataBase.getDB(context).getTaskHistory(query);
                            Log.i("Schedule", "query :" + query + "\n Indiv list size : " + taskDetailsBeenlist.size());
                        } else {
                            query = "select * from taskDetailsInfo where ( taskStatus <> 'abandoned' ) and ( ( fromUserId='" + mediaListBean.getToUserId() + "' ) or ( toUserId='" + mediaListBean.getToUserId() + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='" + "Group" + "') and taskId ='" + mediaListBean.getTaskId() + "' group by taskId";
                            taskDetailsBeenlist = VideoCallDataBase.getDB(context).getTaskHistory(query);
                            Log.i("Schedule", "query :" + query + " group list size : " + taskDetailsBeenlist.size());
                        }

                        if (taskDetailsBeenlist.size() > 0) {
                            TaskDetailsBean taskDetailsBean = taskDetailsBeenlist.get(0);
//                                TaskDetailsBean taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent(taskDetailsBean.getTaskId());
                            TaskDetailsBean taskDetailsBean1 = new TaskDetailsBean();
                            if (taskDetailsBean.getProjectId() != null && !taskDetailsBean.getProjectId().equalsIgnoreCase("") && !taskDetailsBean.getProjectId().equalsIgnoreCase("null")) {
                                taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent("select * from projectHistory where taskId='" + taskDetailsBean.getTaskId() + "'");
                            } else {
                                taskDetailsBean1 = VideoCallDataBase.getDB(context).getTaskcallContent("select * from taskHistoryInfo where taskId='" + taskDetailsBean.getTaskId() + "'");
                            }
                            taskDetailsBean.setOwnerOfTask(taskDetailsBean1.getOwnerOfTask());
                            taskDetailsBean.setTaskName(taskDetailsBean1.getTaskName());
                            taskDetailsBean.setTaskReceiver(taskDetailsBean1.getTaskReceiver());
                            Log.i("Escalation", "taskDetailsBean1.getCatagory() " + taskDetailsBean1.getCatagory());
                            taskDetailsBean.setCatagory(taskDetailsBean1.getCatagory());
                            query = "select * from taskDetailsInfo where ( taskTagName = 'To' ) and ( ( fromUserName='" + Appreference.loginuserdetails.getUsername() + "' ) or ( toUserName='" + Appreference.loginuserdetails.getUsername() + "' )) and ( loginuser='" + Appreference.loginuserdetails.getEmail() + "') and ( taskType='Individual' or taskType='individual' ) and taskId ='" + mediaListBean.getTaskId() + "' and customSetId ='" + String.valueOf(mediaListBean.getCustomSetId()) + "' order by id DESC LIMIT 1";
                            ArrayList<TaskDetailsBean> taskDetailsBeenlist1 = VideoCallDataBase.getDB(context).getTaskHistory(query);
                            TaskDetailsBean taskDetailsBean2 = taskDetailsBeenlist1.get(0);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String dateTime = dateFormat.format(new Date());
                            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                            String dateforrow = dateFormat.format(new Date());
                            String tasktime = dateTime;
                            tasktime = tasktime.split(" ")[1];
                            Log.i("task", "tasktime" + tasktime);
                            Log.i("UTC", "sendMessage utc time" + dateforrow);
                            Log.i("time", "value");
                            String taskUTCtime = dateforrow;
                            taskDetailsBean.setTaskUTCTime(taskUTCtime);
                            taskDetailsBean.setTaskUTCDateTime(dateforrow);
                            taskDetailsBean.setDateTime(dateforrow);
                            taskDetailsBean.setTasktime(tasktime);
                            taskDetailsBean.setSignalid(Utility.getSessionID());
                            taskDetailsBean.setTaskRequestType("buzzrequest");
                            taskDetailsBean.setTaskDescription(taskDetailsBean2.getTaskDescription());  // contains ToUserName's
                            taskDetailsBean.setRemark(taskDetailsBean2.getRemark());
                            ((NewTaskConversation) context).EscalationMethod(taskDetailsBean, view, context, mediaListBean);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MediaListAdapter", "edit_ll Exception: " + e.getMessage(), "WARN", null);
                }
            }
        });

        Resolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                try {
                    Intent intent = new Intent(context, UpdateTaskActivity.class);
                    intent.putExtra("Str", "conversation");
                    intent.putExtra("task", "escalation");
                    intent.putExtra("bean", mediaListBean);
                    ((Activity) context).startActivityForResult(intent, 212);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MediaListAdapter", "withdraw_ll Exception: " + e.getMessage(), "WARN", null);
                }
            }
        });

        Ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                try {
                    ((NewTaskConversation) context).ignoremenu(mediaListBean, view, context);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MediaListAdapter", "withdraw_ll Exception: " + e.getMessage(), "WARN", null);
                }
            }
        });
        cancel_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        try {
            dialog1.show();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MediaListAdapter", "edit_ll Exception: " + e.getMessage(), "WARN", null);
        }
    }


    public void editmenu(final TaskDetailsBean mediaListBean, final View view, final Context adapter_context) {

        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialogforedit);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog1.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.horizontalMargin = 15;
        Window window = dialog1.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setAttributes(lp);
        window.setGravity(Gravity.CENTER);
        LinearLayout comment_tag_ll = (LinearLayout) dialog1.findViewById(R.id.comment_tag_ll);
        LinearLayout resend_ll = (LinearLayout) dialog1.findViewById(R.id.resend_ll);
        final LinearLayout withdraw_ll = (LinearLayout) dialog1.findViewById(R.id.withdraw_ll);
        final LinearLayout delete_ll = (LinearLayout) dialog1.findViewById(R.id.delete_ll);
        LinearLayout cancel_ll = (LinearLayout) dialog1.findViewById(R.id.cancel_ll);
        TextView comment_tag = (TextView) dialog1.findViewById(R.id.comment_tag);
        TextView resend_tv = (TextView) dialog1.findViewById(R.id.resend_tv);
        TextView withdraw_tv = (TextView) dialog1.findViewById(R.id.withdraw_tv);
        TextView delete_tv = (TextView) dialog1.findViewById(R.id.delete_tv);
        LinearLayout delete_all = (LinearLayout) dialog1.findViewById(R.id.delete_all);
        LinearLayout copy_ll = (LinearLayout) dialog1.findViewById(R.id.copy_ll);
        LinearLayout forword_ll = (LinearLayout) dialog1.findViewById(R.id.forword_ll);
        LinearLayout reply_ll = (LinearLayout) dialog1.findViewById(R.id.reply_ll);
        LinearLayout edit_ll = (LinearLayout) dialog1.findViewById(R.id.edit_ll);


        if (isreceiver) {
//            comment_tag_ll.setVisibility(View.GONE);
            resend_ll.setVisibility(View.GONE);
            withdraw_ll.setVisibility(View.GONE);
            cancel_ll.setVisibility(View.GONE);
            edit_ll.setVisibility(View.GONE);
            reply_ll.setVisibility(View.VISIBLE);
            isreceiver = false;
        }
        if (category != null && !category.equalsIgnoreCase("chat")) {
            delete_all.setVisibility(View.GONE);
            forword_ll.setVisibility(View.GONE);
            copy_ll.setVisibility(View.GONE);
            reply_ll.setVisibility(View.VISIBLE);
            edit_ll.setVisibility(View.GONE);
        }
        if (mediaListBean.getMimeType().equalsIgnoreCase("video") ||
                mediaListBean.getMimeType().equalsIgnoreCase("image") ||
                mediaListBean.getMimeType().equalsIgnoreCase("sketch") ||
                mediaListBean.getMimeType().equalsIgnoreCase("audio")) {
            copy_ll.setVisibility(View.GONE);
            edit_ll.setVisibility(View.GONE);
            reply_ll.setVisibility(View.VISIBLE);
            comment_tag_ll.setVisibility(View.VISIBLE);
        }
        if (mediaListBean.getMsg_status() == 1 || (mediaListBean.getTaskStatus() != null && mediaListBean.getMsg_status() == 6 && mediaListBean.getTaskStatus().equalsIgnoreCase("reminder"))) {
            resend_ll.setVisibility(View.GONE);
        }
        if (mediaListBean.getTaskDescription() != null && mediaListBean.getTaskDescription().equalsIgnoreCase("Message has been Removed")) {
            withdraw_ll.setVisibility(View.GONE);
        }
        if (mediaListBean.getProjectId() != null) {
            delete_ll.setVisibility(View.GONE);
            reply_ll.setVisibility(View.GONE);
        }
        cancel_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        comment_tag_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CaptionMedia.class);
                intent.putExtra("taskbean", mediaListBean);
                ((Activity) context).startActivityForResult(intent, 3847);
                dialog1.dismiss();
            }
        });
        resend_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                if (mediaListBean.getMsg_status() == 1) {
                    Toast.makeText(context, "Message already sent", Toast.LENGTH_LONG).show();
                }
                try {
                    ((NewTaskConversation) context).popupmenu(mediaListBean, view, context);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MediaListAdapter", "resend_ll Exception: " + e.getMessage(), "WARN", null);
                }
            }
        });
        withdraw_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                try {
                    ((NewTaskConversation) context).popupwithdrawmenu(mediaListBean, view, context);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MediaListAdapter", "withdraw_ll Exception: " + e.getMessage(), "WARN", null);
                }
            }
        });
        forword_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Forwardto.class);
                intent.putExtra("taskbean", mediaListBean);
                ((Activity) context).startActivityForResult(intent, 3847);
                dialog1.dismiss();
            }
        });
        delete_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                try {
                    ((NewTaskConversation) context).deletemenu(mediaListBean, "delete", view, context);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MediaListAdapter", "delete_ll Exception: " + e.getMessage(), "WARN", null);
                }
            }
        });
        copy_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(mediaListBean.getTaskDescription());
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText("Your OTP", mediaListBean.getTaskDescription());
                    clipboard.setPrimaryClip(clip);
                }
                Toast toast = Toast.makeText(getContext(), "Message Copied", Toast.LENGTH_SHORT);
                toast.show();
                dialog1.dismiss();
            }
        });
        delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                try {
                    ((NewTaskConversation) context).deletemenu(mediaListBean, "deleteall", view, context);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MediaListAdapter", "delete_all Exception: " + e.getMessage(), "WARN", null);
                }
            }
        });

        reply_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                try {
                    ((NewTaskConversation) context).replymenu(mediaListBean, view, context);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MediaListAdapter", "reply_ll Exception: " + e.getMessage(), "WARN", null);
                }
            }
        });
        edit_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                try {
                    ((NewTaskConversation) context).editmenu(mediaListBean, view, context);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("MediaListAdapter", "edit_ll Exception: " + e.getMessage(), "WARN", null);
                }
            }
        });
        try {
            dialog1.show();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MediaListAdapter", "edit_ll Exception: " + e.getMessage(), "WARN", null);
        }
    }

    public static void onComplete() {
        try {
            holder.play_button.setImageResource(R.drawable.play);
            holder.seekBar.setProgress(0);
            holder.cm.stop();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("MediaListAdapter", "onComplete Exception: " + e.getMessage(), "WARN", null);
        }
    }

    class ViewHolder {
        LinearLayout media, audio_play1, txt_des_under, receiver_side_description_layout, timeview, ll_expandable_text_view_left, ll_expandable_text_view_right,/* radio_layout,*/ ll_expandable_text_view_dependency;
        ImageView receiver_side_doc_iconview, sender_side_doc_iconview, sender_side_location_iconview, receiver_side_location_iconview, ls_video_play_icon, ls_thumb_image, ls_thumb_audio, iv_mmstatus, video_play_icon, locatio_icon, thumb_image, thumb_audio, iv_txtstatus, iv_audiostatus, remove_btn, xbutton, play_button, dateChangeRequest_icon, dateChangeApproval_icon, imageselect, textselect, expand_remove_btn, expand_txtstatus, leave, dependency_icon;
        TextView sender_msg_type_image, receiver_msg_type_image, sender_msg_type, receiver_msg_type, receiver_side_doc_icon, sender_side_doc_icon, rcv_des, txt_des, txt_time, time, time_1, time_under_text, time_under_text_receiver, time_under_image_receiver, button_toggle_dependency;
        SeekBar seekBar;
        TextView receiver_name, receiver_name2, send_syn_txt, rcv_syn_txt, rcv_syn_img, send_syn_img, rec_readmore, send_readmore;
        RelativeLayout receiver_side_list_image, expandabletext_views, sender_side_list_image, both_side_list_image, receiver_side_add_txt, sender_side_add_txt, text_views, text_header, sender_multimediaLayout, receiver_multimediaLayout;
        FrameLayout ls_frame_layout, frame_layout;
        ExpandableTextView expandableTextView_left, expandableTextView_right;
        TextView buttonToggle_left, buttonToggle_right, date_header_text;
        Chronometer cm;
        ProgressBar progress_download, progress_upload;
//        RadioButton radio_ninjas;
        String receiver_name_name;
        TextView reply_sender_name, reply_receiver_name, reply_receiver_msg, reply_sender_msg, exclation_counter;
        ImageView dateChangeApproval_sender;
        ImageView reply_sender_image, reply_receiver_image;
        LinearLayout reply_receiver_linear, reply_sender_linear;
    }
}
