package com.ase;

/**
 * Created by Ramdhas on 02-08-2016.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;
import com.squareup.picasso.Picasso;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import net.sf.andpdf.nio.ByteBuffer;

import org.pjsip.pjsua2.app.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    public static String mediapath = null;
    public String dir_path = Environment.getExternalStorageDirectory() + "/High Message/downloads/";
    static MediaPlayer mPlayer = new MediaPlayer();
    static Context context;
    //    private static Vector<MediaListBean> userList;
    ArrayList<TaskDetailsBean> detailsBeanArrayList;
    private LayoutInflater inflater = null;
    public static String check;
    Handler handler = new Handler();
    public Runnable updatetime = null;
    public static MyViewHolder holder = null;
    String tm = null;

    private final OnItemClickListener listener;

   /* public RecycleViewAdapter(Context context, Vector<MediaListBean> userList, String s1) {


        //super(context, R.layout.media_list_adapter, userList);
        *//********** Take passed values **********//*
        this.context = context;
        this.userList = userList;
        this.check = s1;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        *//*********** Layout inflator to call external xml layout () ***********//*

    }

    public RecycleViewAdapter(Context context, Vector<MediaListBean> userList, String s1, ArrayList<TaskDetailsBean> detailsBeanArrayList) {


        //super(context, R.layout.media_list_adapter, userList);
        *//********** Take passed values **********//*
        this.context = context;
        this.userList = userList;
        this.check = s1;
        this.detailsBeanArrayList = detailsBeanArrayList;
        Log.d("listSize", "size  " + detailsBeanArrayList.size());
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        */

    /***********
     * Layout inflator to call external xml layout ()
     ***********//*

    }
*/


    public interface OnItemClickListener {

        void onItemClick(TaskDetailsBean gcBean, View v);

        void onClick(TaskDetailsBean gcBean, RecycleViewAdapter.MyViewHolder v, int position, View view);
    }


    public RecycleViewAdapter(Context context, String s1, ArrayList<TaskDetailsBean> detailsBeanArrayList, OnItemClickListener listener) {
        this.context = context;
//        this.userList = mediaListBeanList;
        this.check = s1;
        this.detailsBeanArrayList = detailsBeanArrayList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.media_list_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        setSelectedItem(position);
        if(selectedItem == position)
            holder.itemView.setSelected(true);
        holder.binddata(detailsBeanArrayList.get(position), listener, position, holder);
    }

    public TaskDetailsBean getPosition(int position) {
        return detailsBeanArrayList.get(position);
    }

    @Override
    public int getItemCount() {
        return detailsBeanArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        LinearLayout media, audio_play;
        ImageView receiver_side_doc_iconview, sender_side_doc_iconview, ls_video_play_icon, ls_thumb_image, iv_mmstatus, video_play_icon, thumb_image, iv_txtstatus, remove_btn, xbutton, play_button, dateChangeRequest_icon, dateChangeApproval_icon;
        TextView receiver_side_doc_icon, sender_side_doc_icon, rcv_des, txt_des, txt_time;
        SeekBar seekBar;
        RelativeLayout receiver_side_list_image, sender_side_list_image, both_side_list_image, receiver_side_add_txt, sender_side_add_txt, text_views;
        FrameLayout ls_frame_layout, frame_layout;
        ProgressBar progress_download;


        TextView time;
        LinearLayout timeview;

        public MyViewHolder(View view) {
            super(view);
            time = (TextView) view.findViewById(R.id.tv_send_time);
            timeview = (LinearLayout) view.findViewById(R.id.timeview);
            media = (LinearLayout) view.findViewById(R.id.meaia);
            audio_play = (LinearLayout) view.findViewById(R.id.audio_play);
            receiver_side_doc_iconview = (ImageView) view.findViewById(R.id.receiver_side_doc_iconview);
            sender_side_doc_iconview = (ImageView) view.findViewById(R.id.sender_side_doc_iconview);
            ls_video_play_icon = (ImageView) view.findViewById(R.id.ls_video_play_icon);
            ls_thumb_image = (ImageView) view.findViewById(R.id.ls_thumb_image);
            iv_mmstatus = (ImageView) view.findViewById(R.id.iv_mmstatus);
            video_play_icon = (ImageView) view.findViewById(R.id.video_play_icon);
            thumb_image = (ImageView) view.findViewById(R.id.thumb_image);
            iv_txtstatus = (ImageView) view.findViewById(R.id.iv_txtstatus);
            remove_btn = (ImageView) view.findViewById(R.id.remove_btn);
            xbutton = (ImageView) view.findViewById(R.id.xbutton);
            play_button = (ImageView) view.findViewById(R.id.play_button);
            receiver_side_doc_icon = (TextView) view.findViewById(R.id.receiver_side_doc_icon);
            sender_side_doc_icon = (TextView) view.findViewById(R.id.sender_side_doc_icon);
            rcv_des = (TextView) view.findViewById(R.id.rcv_des);
            txt_des = (TextView) view.findViewById(R.id.txt_des);
            txt_time = (TextView) view.findViewById(R.id.txt_time);
            seekBar = (SeekBar) view.findViewById(R.id.seekBar1);
            receiver_side_list_image = (RelativeLayout) view.findViewById(R.id.receiver_side_list_image);
            sender_side_list_image = (RelativeLayout) view.findViewById(R.id.sender_side_list_image);
            both_side_list_image = (RelativeLayout) view.findViewById(R.id.both_side_list_image);
            receiver_side_add_txt = (RelativeLayout) view.findViewById(R.id.receiver_side_add_txt);
            sender_side_add_txt = (RelativeLayout) view.findViewById(R.id.sender_side_add_txt);
            text_views = (RelativeLayout) view.findViewById(R.id.text_views);
            ls_frame_layout = (FrameLayout) view.findViewById(R.id.ls_frame_layout);
            frame_layout = (FrameLayout) view.findViewById(R.id.frame_layout);
            progress_download = (ProgressBar) view.findViewById(R.id.progress_download);
            dateChangeRequest_icon = (ImageView) view.findViewById(R.id.dateChangeRequest);
            dateChangeApproval_icon = (ImageView) view.findViewById(R.id.dateChangeApproval);
        }


        public void binddata(final TaskDetailsBean gcBean, final OnItemClickListener listener, final int position, final MyViewHolder holder) {
            Log.i("date", "date view");

            audio_play.setVisibility(View.GONE);
            text_views.setVisibility(View.GONE);
            both_side_list_image.setVisibility(View.GONE);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    listener.onItemClick(gcBean, v);
                    return true;
                }


                /*@Override public void onClick(View v) {
                    listener.onItemClick(gcBean);
                }*/
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(gcBean, holder, position, v);
                }
            });

            remove_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!check.equalsIgnoreCase("task")) {
                        NewBlogActivity.clearMediaList(position);
                    } else {
                        NewTaskConversation.clearMediaList(position);
                    }
                }
            });


            ls_thumb_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("entered", "entered............");
                    switch (gcBean.getMimeType().toLowerCase().trim()) {
                        case "video":
                            Log.i("group123", "icon clicked video");
                            File directory = new File(dir_path + gcBean.getTaskDescription());
                            if (directory.exists()) {
                                Intent intent = new Intent(context, VideoPlayer.class);
                                intent.putExtra("video", directory.getAbsolutePath());
                                context.startActivity(intent);
                            } else
                                Toast.makeText(context, "File not available", Toast.LENGTH_LONG).show();
                            break;
                        case "image":
                            Log.i("group123", "icon clicked image");
                            Intent intent = new Intent(context, FullScreenImage.class);
                            //if(gcBean.getUser() != null && gcBean.getUser().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))
                            // intent.putExtra("image", gcBean.getMediaPath());
                            // else{
                            intent.putExtra("image", dir_path + gcBean.getTaskDescription());
                            context.startActivity(intent);
                            break;
                    }
                }


            });
            sender_side_doc_iconview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("document") || gcBean.getMimeType().equalsIgnoreCase("pdf") && (gcBean.getMimeType() != null)) {
                        Log.i("group123", "icon clicked doc" + gcBean.getMimeType());

                        File url = new File(gcBean.getTaskDescription());
                        if (url.exists()) {
                            Uri uri = Uri.fromFile(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                                // Word document
                                intent.setDataAndType(uri, "application/msword");
                            } else if(url.toString().contains(".pdf")) {
                                // PDF file
                                intent.setDataAndType(uri, "application/pdf");
                            } else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                                // Powerpoint file
                                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                            } else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                                // Excel file
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                            } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
                                // WAV audio file
                                intent.setDataAndType(uri, "application/x-wav");
                            } else if(url.toString().contains(".rtf")) {
                                // RTF file
                                intent.setDataAndType(uri, "application/rtf");
                            } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                                // WAV audio file
                                intent.setDataAndType(uri, "audio/x-wav");
                            } else if(url.toString().contains(".gif")) {
                                // GIF file
                                intent.setDataAndType(uri, "image/gif");
                            } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                                // JPG file
                                intent.setDataAndType(uri, "image/jpeg");
                            } else if(url.toString().contains(".txt")) {
                                // Text file
                                intent.setDataAndType(uri, "text/plain");
                            } else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                                // Video files
                                intent.setDataAndType(uri, "video");
                            } else {
                                //if you want you can also define the intent type for any other file

                                //additionally use else clause below, to manage other unknown extensions
                                //in this case, Android will show all applications installed on the device
                                //so you can choose which application to use
                                intent.setDataAndType(uri, "*/*");
                            }
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
            receiver_side_doc_iconview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("document") || gcBean.getMimeType().equalsIgnoreCase("pdf") && (gcBean.getTaskDescription() != null)) {
                        Log.i("group123", "icon clicked doc" + gcBean.getMimeType());
                        File directory;
                        // if(gcBean.getUser() != null && gcBean.getUser().equalsIgnoreCase(Appreference.loginuserdetails.getUsername()))
                        // directory = new File(gcBean.getMediaPath());
                        //else
                        String ext=gcBean.getTaskDescription().substring(gcBean.getTaskDescription().lastIndexOf(".") +1, gcBean.getTaskDescription().length());
                        File url = new File(dir_path+gcBean.getTaskDescription());
                        if (url.exists()) {
                            Uri uri = Uri.fromFile(url);
                            Intent intent = new Intent(Intent.ACTION_VIEW);

                            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                                // Word document
                                intent.setDataAndType(uri, "application/msword");
                                Log.i("document","doc or docx");
                            } else if(url.toString().contains(".pdf")) {
                                // PDF file
                                intent.setDataAndType(uri, "application/pdf");
                                Log.i("document","pdf");
                            } else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                                // Powerpoint file
                                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                                Log.i("document","ppt");
                            } else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                                // Excel file
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                                Log.i("document","excel");
                            } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
                                // WAV audio file
                                intent.setDataAndType(uri, "application/x-wav");
                                Log.i("document","zip");
                            } else if(url.toString().contains(".rtf")) {
                                // RTF file
                                intent.setDataAndType(uri, "application/rtf");
                                Log.i("document","rtf");
                            } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                                // WAV audio file
                                intent.setDataAndType(uri, "audio/x-wav");
                                Log.i("document","wav or mp3");
                            } else if(url.toString().contains(".gif")) {
                                // GIF file
                                intent.setDataAndType(uri, "image/gif");
                                Log.i("document","gif");
                            } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                                // JPG file
                                intent.setDataAndType(uri, "image/jpeg");
                                Log.i("document","jpeg");
                            } else if(url.toString().contains(".txt")) {
                                // Text file
                                intent.setDataAndType(uri, "text/plain");
                                Log.i("document","txt");
                            } else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                                // Video files
                                intent.setDataAndType(uri, "video/*");
                                Log.i("document","mp3 or mp4");
                            } else {
                                //if you want you can also define the intent type for any other file

                                //additionally use else clause below, to manage other unknown extensions
                                //in this case, Android will show all applications installed on the device
                                //so you can choose which application to use
                                intent.setDataAndType(uri, "*/*");
                                Log.i("document","unknown");
                            }
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
            thumb_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (gcBean.getMimeType()
                            .equalsIgnoreCase("video")) {
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

//            TaskDetailsBean tdb=detailsBeanArrayList.get(detailsBeanArrayList.size()-1);
            Log.i("gcBean", "value" + gcBean.getToUserEmail());
            Log.i("gcBean", "value" + MainActivity.username);
            if (gcBean.getMimeType() != null && gcBean.getFromUserName().equalsIgnoreCase(MainActivity.username)) {
                Log.i("gcBean", "value1" + gcBean.getFromUserName());
                String dd = gcBean.getTasktime();
                Log.i("gcBean", "value2" + gcBean.getTasktime());
                Log.i("gcBean", "value2" + dd);
//                for (String time1 : dd.split(" ", 2)) {
//                    Log.i("chat", "time-->" + time1);
//                    tm = time1;
//                }
                time.setText(dd);
            } else {
                Log.i("gcBean", "value3" + gcBean.getTasktime());
                Log.i("gcBean", "value3" + gcBean.getToUserName());
                String name = VideoCallDataBase.getDB(context).getName(gcBean.getFromUserName());
                time.setText(name + " ");
                time.setText(time.getText() + gcBean.getTasktime());
            }

            if (gcBean.getMsg_status() == 0) {
                iv_txtstatus.setImageResource(R.drawable.off_line);
                iv_txtstatus.setImageResource(R.drawable.off_line);
            } else {
                iv_txtstatus.setImageResource(R.drawable.on_line);
                iv_mmstatus.setImageResource(R.drawable.on_line);
            }
            if (gcBean.getMimeType() != null && (!gcBean.getMimeType().equalsIgnoreCase("text") || !gcBean.getMimeType().equalsIgnoreCase("date"))) {
                if (gcBean.getFromUserName() != null && !gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                    if (gcBean.getShow_progress() == 0) {
                        progress_download.setVisibility(View.VISIBLE);
                    } else {
                        progress_download.setVisibility(View.GONE);
                    }
                } else {
                    progress_download.setVisibility(View.GONE);
                }
            } else {
                progress_download.setVisibility(View.GONE);
            }
            Log.i("task", "taskdescription in adapter" + gcBean.getTaskDescription());
            Log.i("task", "taskdescription in adapter" + gcBean.getMimeType());
            if ((!gcBean.getTaskDescription().equalsIgnoreCase(null) || !gcBean.getTaskDescription().equalsIgnoreCase("")) && (!gcBean.getMimeType().equalsIgnoreCase(null) || !gcBean.getMimeType().equalsIgnoreCase(""))) {
                if ((gcBean.getMimeType() != null && (gcBean.getMimeType().equalsIgnoreCase("image") || gcBean.getMimeType().equalsIgnoreCase("sketch"))) && (gcBean.getTaskDescription() != null)) {
                    both_side_list_image.setVisibility(View.VISIBLE);
                    text_views.setVisibility(View.GONE);
                    audio_play.setVisibility(View.GONE);
                    video_play_icon.setVisibility(View.GONE);
                    ls_video_play_icon.setVisibility(View.GONE);
                    sender_side_doc_icon.setVisibility(View.GONE);
                    sender_side_doc_iconview.setVisibility(View.GONE);
                    iv_mmstatus.setVisibility(View.VISIBLE);
                    if (gcBean.getFromUserName() != null && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        File imageFile = new File(gcBean.getTaskDescription());
                        ls_frame_layout.setVisibility(View.GONE);
                        LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) timeview.getLayoutParams();
                        lllp.gravity = Gravity.RIGHT;
                        timeview.setLayoutParams(lllp);
                        frame_layout.setVisibility(View.VISIBLE);
                        sender_side_list_image.setVisibility(View.VISIBLE);
                        Log.d("senderside", "image file out");
                        if (imageFile.exists()) {
                            Log.i("senderside", "image file in");
                            thumb_image.setVisibility(View.VISIBLE);

                            Picasso.with(context).load(imageFile).into(thumb_image);
                        }
                    } else {
                        File imageFile = new File(dir_path + gcBean.getTaskDescription());
                        frame_layout.setVisibility(View.GONE);
                        ls_frame_layout.setVisibility(View.VISIBLE);
                        receiver_side_list_image.setVisibility(View.VISIBLE);
                        progress_download.setVisibility(View.GONE);
                        LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) timeview.getLayoutParams();
                        lllp.gravity = Gravity.LEFT;
                        timeview.setLayoutParams(lllp);
                        Log.d("receiverside", "image file out");
                        if (imageFile.exists()) {
                            Log.d("receiverside", "image file in");
                            ls_thumb_image.setVisibility(View.VISIBLE);
                            Log.d("task", "download path is" + dir_path + gcBean.getTaskDescription());
                            Picasso.with(context).load(imageFile).into(ls_thumb_image);
                        }
                    }
                } else if (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("document") || gcBean.getMimeType().equalsIgnoreCase("pdf") && (gcBean.getTaskDescription() != null) && gcBean.getMimeType().equalsIgnoreCase("txt")) {
                    both_side_list_image.setVisibility(View.VISIBLE);
                    text_views.setVisibility(View.GONE);
                    audio_play.setVisibility(View.GONE);
                    video_play_icon.setVisibility(View.GONE);
                    thumb_image.setVisibility(View.GONE);
                    ls_thumb_image.setVisibility(View.GONE);
                    ls_video_play_icon.setVisibility(View.GONE);
                    iv_mmstatus.setVisibility(View.VISIBLE);
                    String ext=gcBean.getTaskDescription().substring(gcBean.getTaskDescription().lastIndexOf(".") + 1, gcBean.getTaskDescription().length());
                    Log.i("task","extension"+ext);
                    if (gcBean.getFromUserName() != null && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        File imageFile = new File(gcBean.getTaskDescription());
                        LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) timeview.getLayoutParams();
                        lllp.gravity = Gravity.RIGHT;
                        timeview.setLayoutParams(lllp);
                        receiver_side_list_image.setVisibility(View.GONE);
                        sender_side_list_image.setVisibility(View.VISIBLE);
                        frame_layout.setVisibility(View.VISIBLE);
                        sender_side_doc_iconview.setVisibility(View.VISIBLE);
                        //name of file
                        sender_side_doc_icon.setVisibility(View.VISIBLE);
                        Log.d("senderside", "document file out");
                        if (imageFile.exists()) {
                            Log.i("docfile", "doc");
                            byte[] bytes;
                            try {

                                File file = new File(imageFile.getAbsolutePath());
                                FileInputStream is = new FileInputStream(file);

                                // Get the size of the file
                                long length = file.length();
                                bytes = new byte[(int) length];
                                int offset = 0;
                                int numRead = 0;
                                while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                                    offset += numRead;
                                }
                                ByteBuffer buffer = ByteBuffer.NEW(bytes);
                                if (gcBean.getMimeType().equalsIgnoreCase("pdf")) {
                                    String data = Base64.encodeToString(bytes, Base64.DEFAULT);
                                    PDFFile pdf_file = new PDFFile(buffer);
                                    PDFPage page = pdf_file.getPage(1, true);

                                    RectF rect = new RectF(0, 0, (int) page.getBBox().width(),
                                            (int) page.getBBox().height());

                                    Bitmap image = page.getImage((int) rect.width(), (int) rect.height(), rect);
                                    FileOutputStream os = new FileOutputStream(Environment.getExternalStorageDirectory() + "/High Message/dummy.jpg");
                                    image.compress(Bitmap.CompressFormat.JPEG, 80, os);
                                    Log.d("bitmap", "values.." + image);
                                    sender_side_doc_iconview.setImageBitmap(image);
                                } else {
                                    Bitmap bitmap = BitmapFactory.decodeFile(dir_path + gcBean.getTaskDescription());
                                    receiver_side_doc_iconview.setImageBitmap(bitmap);

                                }

                                String[] a = gcBean.getTaskDescription().split("/");
                                //sender_side_doc_icon.setText(a[a.length - 1]);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        switch(ext)
                        {
                            case  "pdf":
                                sender_side_doc_iconview.setImageResource(R.drawable.pdf12);
                                break;
                            case "doc":
                            case "docx":
                            case "txt":
                                sender_side_doc_iconview.setImageResource(R.drawable.msword);
                                break;
                            case "xls":
                                sender_side_doc_iconview.setImageResource(R.drawable.excel);
                                break;
                            case "ppt":
                                sender_side_doc_iconview.setImageResource(R.drawable.ppt);
                                break;
                            default:
                                sender_side_doc_iconview.setImageResource(R.drawable.unknown);
                                break;

                        }


                    } else {
                        File imageFile = new File(dir_path + gcBean.getTaskDescription());
                        Log.d("task", "image inside" + imageFile);
                        sender_side_list_image.setVisibility(View.GONE);
                        receiver_side_list_image.setVisibility(View.VISIBLE);
                        ls_frame_layout.setVisibility(View.VISIBLE);
                        ls_thumb_image.setVisibility(View.VISIBLE);
                        receiver_side_doc_icon.setVisibility(View.VISIBLE);
                        receiver_side_doc_iconview.setVisibility(View.VISIBLE);
                        progress_download.setVisibility(View.GONE);
                        LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) timeview.getLayoutParams();
                        lllp.gravity = Gravity.LEFT;
                        timeview.setLayoutParams(lllp);
                        if (imageFile.exists()) {
                            Log.d("task", "download path is" + dir_path + gcBean.getTaskDescription());
                            byte[] bytes;
                            try {
                                Log.d("task", "download" + dir_path + gcBean.getTaskDescription());
                                File file = new File(imageFile.getAbsolutePath());
                                FileInputStream is = new FileInputStream(file);

                                // Get the size of the file
                                long length = file.length();
                                bytes = new byte[(int) length];
                                int offset = 0;
                                int numRead = 0;
                                Log.d("bitmap1", "values..");
                                while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                                    offset += numRead;
                                }
                                Log.d("bitmap2", "values..");
                                ByteBuffer buffer = ByteBuffer.NEW(bytes);
                                if (gcBean.getMimeType().equalsIgnoreCase("pdf")) {
                                    String data = Base64.encodeToString(bytes, Base64.DEFAULT);
                                    Log.d("bitmap3", "values..");
                                    PDFFile pdf_file = new PDFFile(buffer);
                                    PDFPage page = pdf_file.getPage(1, true);

                                    RectF rect = new RectF(0, 0, (int) page.getBBox().width(),
                                            (int) page.getBBox().height());
                                    Log.d("bitmap3", "values..");
                                    Bitmap image = page.getImage((int) rect.width(), (int) rect.height(), rect);

                                    Log.d("bitmap3", "values.." + image);
                                    FileOutputStream os = new FileOutputStream(Environment.getExternalStorageDirectory() + "/High Message/dummy.jpg");
                                    image.compress(Bitmap.CompressFormat.JPEG, 80, os);
                                    Log.d("bitmap", "values.." + image);
                                    receiver_side_doc_iconview.setImageBitmap(image);
                                } else {
                                    Bitmap bitmap = BitmapFactory.decodeFile(dir_path + gcBean.getTaskDescription());
                                    receiver_side_doc_iconview.setImageBitmap(bitmap);
                                }
                                String[] a = gcBean.getTaskDescription().split("/");
                                //name of file
//                                receiver_side_doc_icon.setText(a[a.length - 1]);
                                switch(ext)
                                {
                                    case  "pdf":
                                        receiver_side_doc_iconview.setImageResource(R.drawable.pdf12);
                                        break;
                                    case "doc":
                                    case "docx":
                                    case "txt":
                                        receiver_side_doc_iconview.setImageResource(R.drawable.msword);
                                        break;
                                    case "xls":
                                        receiver_side_doc_iconview.setImageResource(R.drawable.excel);
                                        break;
                                    case "ppt":
                                        receiver_side_doc_iconview.setImageResource(R.drawable.ppt);
                                        break;
                                    default:
                                        receiver_side_doc_iconview.setImageResource(R.drawable.unknown);
                                        break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                } else if (gcBean.getMimeType() != null && (gcBean.getMimeType().equalsIgnoreCase("text") || gcBean.getMimeType().equalsIgnoreCase("date"))) {
                    String imageFile = gcBean.getTaskDescription();
                    audio_play.setVisibility(View.GONE);
                    both_side_list_image.setGravity(View.GONE);
                    sender_side_list_image.setVisibility(View.GONE);
                    receiver_side_list_image.setVisibility(View.GONE);
                    text_views.setVisibility(View.VISIBLE);
                    sender_side_add_txt.setVisibility(View.GONE);
                    if (imageFile != null && !imageFile.equalsIgnoreCase("")) {

                        text_views.setVisibility(View.VISIBLE);
                        if (gcBean.getFromUserName() != null && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            sender_side_add_txt.setVisibility(View.VISIBLE);
                            receiver_side_add_txt.setVisibility(View.GONE);
                            LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) timeview.getLayoutParams();
                            lllp.gravity = Gravity.RIGHT;
                            timeview.setLayoutParams(lllp);
                            if (gcBean.getTaskStatus() != null && gcBean.getTaskStatus().equalsIgnoreCase("overdue")) {
                                txt_des.setBackgroundResource(R.drawable.redsender1);
                                txt_des.setText(gcBean.getTaskDescription());
                            } else {
                                txt_des.setBackgroundResource(R.drawable.in_message_bg);
                                txt_des.setText(gcBean.getTaskDescription());
                            }
                            if (gcBean.getMimeType().equals("date")) {
                                // String sendate = gcBean.getTaskDescription();
                                Log.i("sendate", "sendate ");
                                String date_hdr = gcBean.getRequestStatus();
                                String startdate = gcBean.getPlannedStartDateTime();
                                String enddate = gcBean.getPlannedEndDateTime();
                                String remdate = gcBean.getRemainderFrequency();
                                String remfreq = gcBean.getTimeFrequency();
                                Log.i("task", "date header is " + gcBean.getRequestStatus());
                                if (date_hdr.equalsIgnoreCase("assigned")) {
                                    date_hdr = "Remainder Details";
                                } else if (date_hdr.equalsIgnoreCase("requested")) {
                                    date_hdr = "date change request";
                                } else if (date_hdr.equalsIgnoreCase("approved")) {
                                    date_hdr = "date change request approved";
                                } else if (date_hdr.equalsIgnoreCase("rejected")) {
                                    date_hdr = "date change request rejected";
                                }
                                Log.i("task", "date Sender Owner of the task" + gcBean.getOwnerOfTask());
                                Log.i("task", "date login user" + Appreference.loginuserdetails.getUsername());
                                Log.i("task", "date from user" + gcBean.getFromUserName());
                                Log.i("task", "date to user" + gcBean.getToUserName());
                                Log.i("task","reminderfreq"+remfreq);
//                                if (remfreq.equalsIgnoreCase("1")) {
//                                    remfreq = "1 Min";
//                                } else {
//                                    remfreq = remfreq + " Mins";
//                                }
                                String alldate2 = date_hdr + "\n" + "Start Date : " + startdate + "\n" + "End Date : " + enddate + "\n" + "Reminder Time : " + remdate + "\n" + "Reminder Frequency : " + remfreq;
                                txt_des.setText(alldate2);
                            } else {
                                txt_des.setText(gcBean.getTaskDescription());
                            }
                            if (gcBean.getSendStatus() != null && gcBean.getSendStatus().equalsIgnoreCase("1")) {
                                remove_btn.setVisibility(View.VISIBLE);
                                iv_txtstatus.setVisibility(View.GONE);

                            } else {
                                remove_btn.setVisibility(View.GONE);
                                iv_txtstatus.setVisibility(View.VISIBLE);
                            }
                        } else {
                            sender_side_add_txt.setVisibility(View.GONE);
                            receiver_side_add_txt.setVisibility(View.VISIBLE);
                            LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) timeview.getLayoutParams();
                            lllp.gravity = Gravity.LEFT;
                            timeview.setLayoutParams(lllp);
                            if (gcBean.getMimeType().equals("date")) {
                                String date_hdr;
                                String startdate = gcBean.getPlannedStartDateTime();
                                String enddate = gcBean.getPlannedEndDateTime();
                                String remdate = gcBean.getRemainderFrequency();
                                String remfreq = gcBean.getTimeFrequency();
//                                if (remfreq.equalsIgnoreCase("1")) {
//                                    remfreq = "1 Min";
//                                } else {
//                                    remfreq = remfreq + " Mins";
//                                }

                                Log.i("task", "date Receiver Owner of the task" + gcBean.getOwnerOfTask());
                                Log.i("task", "date login user" + Appreference.loginuserdetails.getUsername());
                                Log.i("task", "date from user" + gcBean.getFromUserName());
                                Log.i("task", "date to user" + gcBean.getToUserName());
                                Log.i("task","reminderfreq"+remfreq);
                                if (gcBean.getRequestStatus().equalsIgnoreCase("requested") && !gcBean.getOwnerOfTask().equalsIgnoreCase("") && gcBean.getOwnerOfTask().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                                    dateChangeApproval_icon.setVisibility(View.VISIBLE);
                                    date_hdr = "date change request";
                                    Log.i("task", "date Requested - same user " + date_hdr);
                                    String alldate1 = date_hdr + "\n" + "Start Date : " + startdate + "\n" + "End Date : " + enddate + "\n" + "Reminder Time : " + remdate + "\n" + "Reminder Frequency : " + remfreq;
                                    rcv_des.setText(alldate1);
                                } else if (gcBean.getRequestStatus().equalsIgnoreCase("requested")) {
                                    date_hdr = "date change request";
//                                    dateChangeRequest_icon.setVisibility(View.VISIBLE);
                                    Log.i("task", "date Requested " + date_hdr);
                                    String alldate1 = date_hdr + "\n" + "Start Date : " + startdate + "\n" + "End Date : " + enddate + "\n" + "Reminder Time : " + remdate + "\n" + "Reminder Frequency : " + remfreq;
                                    rcv_des.setText(alldate1);
                                } else if (gcBean.getRequestStatus().equalsIgnoreCase("approved")) {
                                    date_hdr = "date change request approved";
//                                    dateChangeRequest_icon.setVisibility(View.VISIBLE);
                                    Log.i("task", "date Approval " + date_hdr);
                                    String alldate1 = date_hdr + "\n" + "Start Date : " + startdate + "\n" + "End Date : " + enddate + "\n" + "Reminder Time : " + remdate + "\n" + "Reminder Frequency : " + remfreq;
                                    rcv_des.setText(alldate1);
                                } else if (gcBean.getRequestStatus().equalsIgnoreCase("rejected")) {
                                    date_hdr = "date change request rejected";
//                                    dateChangeRequest_icon.setVisibility(View.VISIBLE);
                                    Log.i("task", "date Rejected " + date_hdr);
                                    String alldate1 = date_hdr + "\n" + "Start Date : " + startdate + "\n" + "End Date : " + enddate + "\n" + "Reminder Time : " + remdate + "\n" + "Reminder Frequency : " + remfreq;
                                    rcv_des.setText(alldate1);
                                } else {
                                    date_hdr = "Reminder Details";
                                    if (gcBean.getMsg_status() == 6) {
                                        dateChangeRequest_icon.setVisibility(View.VISIBLE);
                                    }
                                    Log.i("task", "for Approval " + date_hdr);
                                    String alldate1 = date_hdr + "\n" + "Start Date : " + startdate + "\n" + "End Date : " + enddate + "\n" + "Reminder Time : " + remdate + "\n" + "Reminder Frequency : " + remfreq;
                                    rcv_des.setText(alldate1);
                                }
                            } else {
                                if (gcBean.getTaskStatus() != null && gcBean.getTaskStatus().equalsIgnoreCase("overdue")) {
                                    rcv_des.setBackgroundResource(R.drawable.redreceiver1);
                                    rcv_des.setText(gcBean.getTaskDescription());
                                    dateChangeRequest_icon.setVisibility(View.GONE);
                                } else {
                                    rcv_des.setBackgroundResource(R.drawable.grey_im);
                                    rcv_des.setText(gcBean.getTaskDescription());
                                    dateChangeRequest_icon.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                } else if (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("video") && (gcBean.getTaskDescription() != null)) {
                    both_side_list_image.setVisibility(View.VISIBLE);
                    audio_play.setVisibility(View.GONE);
                    text_views.setVisibility(View.GONE);
                    if (gcBean.getFromUserName() != null && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        sender_side_list_image.setVisibility(View.VISIBLE);
                        frame_layout.setVisibility(View.VISIBLE);
                        receiver_side_list_image.setVisibility(View.GONE);
                        video_play_icon.setVisibility(View.VISIBLE);
                        thumb_image.setVisibility(View.VISIBLE);
                        iv_mmstatus.setVisibility(View.VISIBLE);
                        sender_side_doc_icon.setVisibility(View.GONE);
                        sender_side_doc_iconview.setVisibility(View.GONE);
                        LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) timeview.getLayoutParams();
                        lllp.gravity = Gravity.RIGHT;
                        timeview.setLayoutParams(lllp);
                        File imageFile = new File(gcBean.getTaskDescription());
                        if (imageFile.exists()) {
                            Log.i("task", "thump exists");
                            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(imageFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                            thumb_image.setImageBitmap(bMap);
                        }
                    } else {
                        File imageFile = new File(dir_path + gcBean.getTaskDescription());
                        sender_side_list_image.setVisibility(View.GONE);
                        LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) timeview.getLayoutParams();
                        lllp.gravity = Gravity.LEFT;
                        timeview.setLayoutParams(lllp);
                        ;
                        receiver_side_list_image.setVisibility(View.VISIBLE);
                        ls_video_play_icon.setVisibility(View.VISIBLE);
                        ls_frame_layout.setVisibility(View.VISIBLE);
                        ls_thumb_image.setVisibility(View.VISIBLE);
                        progress_download.setVisibility(View.GONE);
                        Log.d("task", "image inside");
                        if (imageFile.exists()) {
                            Log.d("task", "download path is" + dir_path + gcBean.getTaskDescription());
                            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(dir_path + gcBean.getTaskDescription(), MediaStore.Video.Thumbnails.MICRO_KIND);
                            ls_thumb_image.setImageBitmap(bMap);
                        }
                    }
                } else if (gcBean.getMimeType() != null && gcBean.getMimeType().equalsIgnoreCase("audio") && (gcBean.getTaskDescription() != null)) {
                    try {
                        audio_play.setVisibility(View.VISIBLE);
                        text_views.setVisibility(View.GONE);
                        both_side_list_image.setVisibility(View.GONE);
                        seekBar.setProgress(0);
                        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                        itemView.performClick();
                        if (gcBean.getFromUserName() != null && gcBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                            Log.e("audiofilepath", gcBean.getTaskDescription());
                            mmr.setDataSource(gcBean.getTaskDescription());
                            LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) audio_play.getLayoutParams();
                            lllp.gravity = Gravity.RIGHT;
                            audio_play.setLayoutParams(lllp);
                            LinearLayout.LayoutParams lllp1 = (LinearLayout.LayoutParams) timeview.getLayoutParams();
                            lllp1.gravity = Gravity.RIGHT;
                            timeview.setLayoutParams(lllp1);
                        } else {
                            Log.d("task", "download path is audio " + dir_path + gcBean.getTaskDescription());
                            mmr.setDataSource(dir_path + gcBean.getTaskDescription());
                            LinearLayout.LayoutParams lllp = (LinearLayout.LayoutParams) audio_play.getLayoutParams();
                            lllp.gravity = Gravity.LEFT;
                            audio_play.setLayoutParams(lllp);
                            LinearLayout.LayoutParams lllp1 = (LinearLayout.LayoutParams) timeview.getLayoutParams();
                            lllp1.gravity = Gravity.LEFT;
                            timeview.setLayoutParams(lllp1);
                        }
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
                        String name = VideoCallDataBase.getDB(context).getName(gcBean.getToUserName());
                        txt_time.setText(name + " ");
                        txt_time.setText(String.format(min + ":" + sec));


//                            super.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    seekBar.getProgressDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

                }

            }


        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }






        /*@Override
        public void onClick(View v) {
            Log.e("jjjjjjjjjjjjjjjjjjjjjjj","kkkkkkkkkkkkkkkkkkk");
           if( v.getId()== R.id.play_button)
           {
               Log.e("Worked","working.....");
//               holder.seekBar=(SeekBar)v.findViewById(R.id.seekBar1);
           }
        }

        @Override
        public boolean onLongClick(View v) {
            Log.e("jjjjjjjjjjjjjjjjjjjjjjj","kkkkkkkkkkkkkkkkkkk");

            if (userList.get(getAdapterPosition()).getMsg_status() == 0) {
                String query = "select * from taskDetailsInfo where signalid='" + userList.get(getAdapterPosition()).getSignalid() + "'";

                ArrayList<TaskDetailsBean> taskDetailsBeans_list = VideoCallDataBase.getDB(context).getTaskHistory(query);
                Log.e("jjjjjjjjjjjjjjjjjjjjjjj","kkkkkkkkkkkkkkkkkkk");
                if (taskDetailsBeans_list.size() > 0) {
                    new NewTaskConversation().popupmenu(taskDetailsBeans_list.get(0), v);
                }
            }
            return true;
        }*/





    }
    private static int selectedItem = -1;

    public void setSelectedItem(int position)
    {
        selectedItem = position;
    }
    public void stopPlayback() {
//        mPlayingPosition = -1;
//        mProgressUpdater.mBarToUpdate = null;
//        mProgressUpdater.tvToUpdate = null;
        if (mPlayer != null && mPlayer.isPlaying())
            mPlayer.stop();
    }
}








