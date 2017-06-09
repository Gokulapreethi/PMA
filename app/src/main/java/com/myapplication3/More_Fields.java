package com.myapplication3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.myapplication3.Bean.TaskDetailsBean;
import com.myapplication3.RandomNumber.Utility;

import org.pjsip.pjsua2.Buddy;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.app.CallActivity;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.app.MyBuddy;
import org.pjsip.pjsua2.app.MyCall;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import json.ListMember;
import json.WebServiceInterface;

/**
 * Created by thirumal on 14-03-2017.
 */
public class More_Fields extends Activity implements WebServiceInterface {
    Button cancel, submit, set, audio, photo, sketch, video, files, call, update_location;
    EditText Remarks;
    TextView heading;
    LocationManager locationManager;
    //    LOCTracker loc;
    public static ArrayList<TaskDetailsBean> mediaList;
    Context context;
    public static ListArrayAdapter medialistadapter;
    private Handler handler = new Handler();
    SwipeMenuListView listView;
    String strIPath = "sam";
    String filePath, Location;
    String requestType, mimeType, position;
    double latitude;
    double longitude;
    private boolean back_preesed = false;
    String test = "sam";
    private String quotes = "\"";
    MoreFieldsBean moreFieldsBean;
    LinearLayout mmContent;
    public Geocoder geo;

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 30) {
                if (data != null) {
                    Uri selectedImageUri = data.getData();
                    strIPath = getRealPathFromURI(selectedImageUri);
                    File selected_file = new File(strIPath);
                    int length = (int) selected_file.length() / 1048576;
                    if (length <= 2) {
                        Bitmap img = null;
                        img = convertpathToBitmap(strIPath);

                        if (img != null) {
                            img = Bitmap.createScaledBitmap(img, 100, 75, false);

                            final String path = Environment.getExternalStorageDirectory()
                                    + "/High Message/" + getFileName() + ".jpg";

                            BufferedOutputStream stream;
                            Bitmap bitmap = null;
                            try {
                                bitmap = convertpathToBitmap(strIPath);
                                if (bitmap != null) {
                                    stream = new BufferedOutputStream(
                                            new FileOutputStream(new File(path)));
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                                    strIPath = path;
                                }

                                if (bitmap != null) {
                                    bitmap = Bitmap.createScaledBitmap(
                                            bitmap, 200, 150, false);
                                }
                                File imageFile = new File(strIPath);
                                if (selected_file.exists()) {
                                    Log.i("imagefile", "image");
                                    test = "image";
                                    TaskDetailsBean taskBean = new TaskDetailsBean();
                                    taskBean.setMimeType("image");
                                    taskBean.setTaskDescription(path);
                                    taskBean.setTaskRequestType(requestType);
                                    taskBean.setSubType(requestType);
//                                    taskBean.setSignalid(Utility.getSessionID());
                                    Log.e("update", "value" + strIPath);
                                    Log.e("update", "value" + path);
                                    Log.e("update", "value" + taskBean.getTaskDescription());
                                    mediaList.add(taskBean);
                                    medialistadapter.notifyDataSetChanged();
                                }
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else if (requestCode == 31) {
                if (data != null) {
                    Uri selectedImageUri = data.getData();
                    final int takeFlags = data.getFlags()
                            & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    getContentResolver().takePersistableUriPermission(
                            selectedImageUri, takeFlags);
                    strIPath = Environment.getExternalStorageDirectory()
                            + "/High Message/"
                            + getFileName() + ".jpg";

                    File selected_file = new File(strIPath);
                    int length = (int) selected_file.length() / 1048576;
                    Log.d("task", "........ size is------------->" + length);
                    Log.e("update", "value  " + strIPath);
                    Log.e("update", "value  " + selectedImageUri);
                    Log.e("update", "value  " + getFileName());
                    if (length <= 2) {
                        new bitmaploader().execute(selectedImageUri);
                    } else {
                        showToast("Kindly Select someother image,this image is too large");
                    }

                }
            } else if (requestCode == 32) {
                if (data != null) {
                    Log.i("AAA", "New activity 32*************");
                    Uri selectedImageUri = data.getData();
                    strIPath = getRealPathFromURI(selectedImageUri);
                    final String path = Environment.getExternalStorageDirectory()
                            + "/High Message/" + getFileName() + ".mp4";

                    strIPath = path;
                    try {
                        FileInputStream fin = (FileInputStream) getContentResolver()
                                .openInputStream(selectedImageUri);
                        ByteArrayOutputStream straam = new ByteArrayOutputStream();
                        byte[] content = new byte[1024];
                        int bytesread;
                        while ((bytesread = fin.read(content)) != -1) {
                            straam.write(content, 0, bytesread);
                        }
                        byte[] bytes = straam.toByteArray();
                        FileOutputStream fout = new FileOutputStream(strIPath);
                        straam.flush();
                        straam.close();
                        straam = null;
                        fin.close();
                        fin = null;
                        fout.write(bytes);
                        fout.flush();
                        fout.close();

                        Log.i("AAA", "New activity " + strIPath);
                        File imageFile = new File(strIPath);
                        if (imageFile.exists()) {
                            test = "video";
                       /* MediaListBean uIbean = new MediaListBean();
                        uIbean.setMediaType("video");
                        uIbean.setMediaPath(strIPath);*/
                            TaskDetailsBean taskBean = new TaskDetailsBean();
                            taskBean.setMimeType("video");
                            taskBean.setTaskDescription(path);
                            taskBean.setTaskRequestType(requestType);
                            taskBean.setSubType(requestType);
//                            taskBean.setSignalid(Utility.getSessionID());
                            Log.e("update", "value" + path);
                            Log.e("update", "value" + taskBean.getTaskDescription());
                            mediaList.add(taskBean);
                            medialistadapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == 423) {
                strIPath = data.getStringExtra("path");
                Log.i("Task", "path" + strIPath);
                File new_file = new File(strIPath);
                test = "image";
               /* MediaListBean uIbean = new MediaListBean();
                uIbean.setMediaType("image");
                uIbean.setMediaPath(strIPath);*/
                TaskDetailsBean taskBean = new TaskDetailsBean();
                taskBean.setMimeType("image");
                taskBean.setTaskDescription(strIPath);
                taskBean.setTaskRequestType(requestType);
                taskBean.setSubType(requestType);
//                taskBean.setSignalid(Utility.getSessionID());
                mediaList.add(taskBean);
                medialistadapter.notifyDataSetChanged();
                Log.i("AAAA", "onactivity result $$$$$$$$$$$$$$$" + strIPath);
            } else if (requestCode == 132) {
                strIPath = data.getStringExtra("filePath");
                Log.i("task1", "onactivity result $$$$$$$$$$$$$$$" + strIPath);
                File new_file = new File(strIPath);
                if (new_file.exists()) {
                    Log.i("task1", "onactivity result " + strIPath);
                    test = "image";
                   /* MediaListBean uIbean = new MediaListBean();
                    uIbean.setMediaType("image");
                    uIbean.setMediaPath(strIPath);*/
                    TaskDetailsBean taskBean = new TaskDetailsBean();
                    taskBean.setMimeType("image");
                    taskBean.setTaskDescription(strIPath);
                    taskBean.setTaskRequestType(requestType);
                    taskBean.setSubType(requestType);
//                    taskBean.setSignalid(Utility.getSessionID());
                    mediaList.add(taskBean);
                    medialistadapter.notifyDataSetChanged();
                }
            } else if (requestCode == 333) {
                Log.d("filePath", data.getStringExtra("taskFileExt"));
                strIPath = data.getStringExtra("taskFileExt");
                test = "audio";
               /* MediaListBean uIbean = new MediaListBean();
                uIbean.setMediaType("audio");
                uIbean.setMediaPath(strIPath);*/
                TaskDetailsBean taskBean = new TaskDetailsBean();
                taskBean.setMimeType("audio");
                taskBean.setTaskDescription(strIPath);
                Log.i("mp", "mpath" + strIPath);
                taskBean.setTaskRequestType(requestType);
                taskBean.setSubType(requestType);
//                taskBean.setSignalid(Utility.getSessionID());
                mediaList.add(taskBean);
                medialistadapter.notifyDataSetChanged();
//                seekBar.setProgress(0);
                MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                mmr.setDataSource(strIPath);
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
//                buddyName.setText(min + ":" + sec);

            } else if (requestCode == 55) {

                filePath = data.getStringExtra("fileExt");
                String fileName = data.getStringExtra("fileName");
                strIPath = data.getStringExtra("filePath");
//                    filePath = fileName.split(".")[1];
//                    fileExt=fileExt1.substring(fileExt1.lastIndexOf(1));
                Log.i("file", "fileExt" + strIPath);
                Log.i("file", "fm--->" + filePath);
//                Log.i("filename", "fm--->" + fileName);
                Log.i("file", "fm--->" + strIPath);

//                docfileWebService(filePath);
//                    strIPath = filePath;
               /* MediaListBean uIbean = new MediaListBean();
                uIbean.setMediaType("doc");
                uIbean.setMediaPath(strIPath);
                uIbean.setExt(filePath);*/
                TaskDetailsBean taskBean = new TaskDetailsBean();
                taskBean.setMimeType("document");
                taskBean.setTaskDescription(strIPath);
                Log.i("mp", "mpath" + strIPath);
                taskBean.setTaskRequestType(requestType);
                taskBean.setSubType(requestType);
//                taskBean.setSignalid(Utility.getSessionID());
                mediaList.add(taskBean);
                medialistadapter.notifyDataSetChanged();
            } else if (requestCode == 111) {
                strIPath = data.getStringExtra("filePath");
                Log.i("Avideo", "New activity ************* : " + strIPath);
                File new_file = new File(strIPath);
                if (new_file.exists()) {
                    test = "video";
                  /*  MediaListBean uIbean = new MediaListBean();
                    uIbean.setMediaType("video");
                    uIbean.setMediaPath(strIPath);*/
                    TaskDetailsBean taskBean = new TaskDetailsBean();
                    taskBean.setMimeType("video");
                    taskBean.setTaskDescription(strIPath);
                    Log.e("update", "value" + taskBean.getTaskDescription());
                    Log.i("mp", "mpath" + strIPath);
                    taskBean.setTaskRequestType(requestType);
                    taskBean.setSubType(requestType);
//                    taskBean.setSignalid(Utility.getSessionID());
                    mediaList.add(taskBean);
                    medialistadapter.notifyDataSetChanged();
                }
            } else if (requestCode == 33) {
                if (data != null) {
                    try {
                        Log.i("AAA", "New activity 33*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + getFileName() + ".mp4";

                        Log.i("AAA", "New activity " + strIPath);
                        strIPath = path;
                        Log.i("AAA", "New activity " + strIPath);
                      /*  MediaListBean uIbean = new MediaListBean();
                        uIbean.setMediaType("video");
                        uIbean.setMediaPath(strIPath);*/
                        TaskDetailsBean taskBean = new TaskDetailsBean();
                        taskBean.setMimeType("video");
                        taskBean.setTaskDescription(strIPath);
                        Log.e("update", "value" + taskBean.getTaskDescription());
                        taskBean.setTaskRequestType(requestType);
                        taskBean.setSubType(requestType);
//                        taskBean.setSignalid(Utility.getSessionID());
                        Log.i("mp", "mpath" + strIPath);
                        mediaList.add(taskBean);
                        medialistadapter.notifyDataSetChanged();
                        try {
                            FileInputStream fin = (FileInputStream) getContentResolver().openInputStream(selectedImageUri);
                            ByteArrayOutputStream straam = new ByteArrayOutputStream();
                            byte[] content = new byte[1024];
                            int bytesread;
                            while ((bytesread = fin.read(content)) != -1) {
                                straam.write(content, 0, bytesread);
                            }
                            byte[] bytes = straam.toByteArray();
                            FileOutputStream fout = new FileOutputStream(strIPath);
                            straam.flush();
                            straam.close();
                            straam = null;
                            fin.close();
                            fin = null;
                            fout.write(bytes);
                            fout.flush();
                            fout.close();
                            fout = null;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                /*File imageFile = new File(strIPath);
                if (imageFile.exists()) {
                    test = "video";
                    frameLayout.setVisibility(View.VISIBLE);
                    ad_play.setVisibility(View.GONE);
                    video_play_icon.setVisibility(View.VISIBLE);
                    ImageView.setVisibility(View.VISIBLE);
                    list_image.setVisibility(View.VISIBLE);
                    Bitmap bMap = ThumbnailUtils.createVideoThumbnail(imageFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                    ImageView.setImageBitmap(bMap);
                }*/
            } else if (requestCode == 888) {
                Log.d("latitude", data.getStringExtra("loc_latitude"));
                Location = data.getStringExtra("loc_latitude");
                Log.i("location", "loc_latitude " + Location);
                Log.i("update", "extension");
                TaskDetailsBean taskBean = new TaskDetailsBean();
                taskBean.setMimeType("map");
                taskBean.setTaskDescription(Location);
                Log.e("update", "value" + taskBean.getTaskDescription());
                Log.i("update", "location" + Location);
                taskBean.setTaskRequestType(requestType);
                taskBean.setSubType(requestType);
//                taskBean.setSignalid(Utility.getSessionID());
                mediaList.add(taskBean);
                medialistadapter.notifyDataSetChanged();
//                String sig_id = Utility.getSessionID();
//                sendMessage(Location, null, "map", null, null, sig_id);
            } else if (requestCode == 101) {
                if (locationManager == null) {
                    locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                }
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.i("LocationFind", "if requestCode : " + requestCode + " resultCode : " + resultCode);
                } else {
                    Log.i("LocationFind", "else requestCode : " + requestCode + " resultCode : " + resultCode);
                    final LOCTracker loc = new LOCTracker(More_Fields.this);
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (loc.canGetLocation()) {
                                while (true) {
                                    loc.getLocation();
                                    latitude = loc.getLatitude();
                                    longitude = loc.getLongitude();
                                    if (latitude == 0.0 && latitude == 0.0 && !back_preesed) {

                                    } else {
                                        break;
                                    }
                                }
                                Log.d("'Location", "latitude " + latitude);
                                Log.d("Location", "longitude " + longitude);
                                String local = showDetailedLocation(latitude, longitude);
                                TaskDetailsBean taskBean = new TaskDetailsBean();
                                taskBean.setMimeType("Text");
                                taskBean.setTaskDescription(local);
                                Log.e("update", "value" + taskBean.getTaskDescription());
                                Log.i("update", "location" + local);
                                taskBean.setTaskRequestType(requestType);
                                taskBean.setSubType(requestType);
//                                taskBean.setSignalid(Utility.getSessionID());
                                mediaList.add(taskBean);
                                medialistadapter.notifyDataSetChanged();
                            }
                        }
                    }, 2000);
                }
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_fields);
        cancel = (Button) findViewById(R.id.cancel);
        submit = (Button) findViewById(R.id.save);
        set = (Button) findViewById(R.id.set);
        audio = (Button) findViewById(R.id.audio);
        video = (Button) findViewById(R.id.video);
        sketch = (Button) findViewById(R.id.write);
        files = (Button) findViewById(R.id.files);
        photo = (Button) findViewById(R.id.photo);
//        call = (Button) findViewById(R.id.file);
        heading = (TextView) findViewById(R.id.txtView01);
        update_location = (Button) findViewById(R.id.update_location);
        Remarks = (EditText) findViewById(R.id.edittext1);
        listView = (SwipeMenuListView) findViewById(R.id.listOfFiles);
        mediaList = new ArrayList<TaskDetailsBean>();
        medialistadapter = new ListArrayAdapter(this, mediaList);
        listView.setAdapter(medialistadapter);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mmContent = (LinearLayout) findViewById(R.id.content1);
//        loc = new LOCTracker(More_Fields.this);
        this.context = this;
        requestType = getIntent().getExtras().getString("RequestType");
        moreFieldsBean = (MoreFieldsBean) getIntent().getExtras().get("bean");
        position = getIntent().getExtras().getString("Position");


       /* if (!mimeType.equalsIgnoreCase("numeric")) {
            Remarks.setVisibility(View.GONE);
        } else if (mimeType.equalsIgnoreCase("numeric")) {
            set.setVisibility(View.GONE);
        }*/
        mmContent.setVisibility(View.GONE);
        set.setVisibility(View.GONE);
        Remarks.setVisibility(View.GONE);
        /*String MimeTypes="";
        for (String mimeType : moreFieldsBean.getMimeTypes()) {

            int counter = 0;
            for (int i = 0; i < mimeType.length(); i++) {
                if (mimeType.charAt(i) == ',') {
                    counter++;
                }
                Log.d("project_details", "Task Mem's counter size is == " + counter);
            }
            for (int j = 0; j < counter + 1; j++) {
                String Mem_name = mimeType.split(",")[j];
                Log.i("Custom", "moreFieldsBean.getMimeTypes() More_Fields == " + Mem_name + " " + j);
                MimeTypes=Mem_name+",";
                *//*if (Mem_name.equalsIgnoreCase("autolocation")){
                    set.setVisibility(View.VISIBLE);
                } else if (Mem_name.equalsIgnoreCase("autodate")){
                    set.setVisibility(View.VISIBLE);
                }else if(Mem_name.equalsIgnoreCase("numeric")){
                    Remarks.setVisibility(View.VISIBLE);
                }else if (Mem_name.equalsIgnoreCase("MM")){
                    set.setVisibility(View.GONE);
                    Remarks.setVisibility(View.GONE);
                    mmContent.setVisibility(View.VISIBLE);
                }*//*
            }
//            if (!mimeType.equalsIgnoreCase("numeric")) {
//                Remarks.setVisibility(View.GONE);
//            } else if (mimeType.equalsIgnoreCase("numeric")) {
//                set.setVisibility(View.GONE);
//            } else if (mimeType.equalsIgnoreCase("MM")) {
//                mmContent.setVisibility(View.VISIBLE);
//            }
        }
//        Log.i("Custom", "moreFieldsBean.getMimeTypes() More_Fields == "+);
        MimeTypes = MimeTypes.substring(0, MimeTypes.length() - 1);
        if (MimeTypes.equalsIgnoreCase("autolocation,autodate")){
            set.setVisibility(View.VISIBLE);
        }else if (MimeTypes.equalsIgnoreCase("numeric")){
            Remarks.setVisibility(View.VISIBLE);
        }else if (MimeTypes.equalsIgnoreCase("numeric,mm")){
            Remarks.setVisibility(View.VISIBLE);
            mmContent.setVisibility(View.VISIBLE);
        }*/
        if (requestType != null && (requestType.equalsIgnoreCase("attendance_in") || requestType.equalsIgnoreCase("attendance_out"))) {
            set.setVisibility(View.VISIBLE);
            if (requestType.equalsIgnoreCase("attendance_in")) {
                heading.setText("Attendance In Time");
            } else {
                heading.setText("Attendance Out Time");
            }
        } else if (requestType != null && (requestType.equalsIgnoreCase("rate") || requestType.equalsIgnoreCase("work") || requestType.equalsIgnoreCase("overtime"))) {
            Remarks.setVisibility(View.VISIBLE);
            Remarks.setHint("set Value");
            if (requestType.equalsIgnoreCase("rate")) {
                heading.setText("Rates($/hr)");
            } else if (requestType.equalsIgnoreCase("work")) {
                heading.setText("Work(hr)");
            } else {
                heading.setText("Overtime(hr)");
            }
        } else if (requestType != null && requestType.equalsIgnoreCase("m/c_breakdown")) {
            Remarks.setVisibility(View.VISIBLE);
            Remarks.setHint("set Value");
            mmContent.setVisibility(View.VISIBLE);
            heading.setText("M/C Breakdown(hr)");
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskDetailsBean taskDetailsBean = mediaList.get(position);
                if (taskDetailsBean.getMimeType().equalsIgnoreCase("image")) {
                    Log.i("group123", "icon clicked image");
                    Intent intent = new Intent(context, FullScreenImage.class);
                    intent.putExtra("image", taskDetailsBean.getTaskDescription());
                    context.startActivity(intent);
                } else if (taskDetailsBean.getMimeType().equalsIgnoreCase("audio")) {
                    Intent intent = new Intent(context, Audioplayer.class);
                    intent.putExtra("audio", taskDetailsBean.getTaskDescription());
                    context.startActivity(intent);
                } else if (taskDetailsBean.getMimeType().equalsIgnoreCase("video")) {
                    Log.i("group123", "icon clicked video");
                    File directory = new File(taskDetailsBean.getTaskDescription());
                    if (directory.exists()) {
                        Log.i("update", "video  **" + taskDetailsBean.getTaskDescription());
                        Intent intent = new Intent(context, VideoPlayer.class);
                        intent.putExtra("video", taskDetailsBean.getTaskDescription());
                        context.startActivity(intent);
                    }
                } else if (taskDetailsBean.getMimeType().equalsIgnoreCase("document")) {
                    Log.i("update conversation ", "mm file " + taskDetailsBean.getMimeType());
                    Log.i("update conversation ", "description " + taskDetailsBean.getTaskDescription());
                    File url = new File(taskDetailsBean.getTaskDescription());
                    if (url.exists()) {
                        Uri uri = Uri.fromFile(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                            // Word document
                            intent.setDataAndType(uri, "application/msword");
                        } else if (url.toString().contains(".pdf")) {
                            // PDF file
                            intent.setDataAndType(uri, "application/pdf");
                        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                            // Powerpoint file
                            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                            // Excel file
                            intent.setDataAndType(uri, "application/vnd.ms-excel");
                        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                            // WAV audio file
                            intent.setDataAndType(uri, "application/x-wav");
                        } else if (url.toString().contains(".rtf")) {
                            // RTF file
                            intent.setDataAndType(uri, "application/rtf");
                        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                            // WAV audio file
                            intent.setDataAndType(uri, "audio/x-wav");
                        } else if (url.toString().contains(".gif")) {
                            // GIF file
                            intent.setDataAndType(uri, "image/gif");
                        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                            // JPG file
                            intent.setDataAndType(uri, "image/jpeg");
                        } else if (url.toString().contains(".txt")) {
                            // Text file
                            intent.setDataAndType(uri, "text/plain");
                        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
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
                        Toast.makeText(context, "can't open", Toast.LENGTH_SHORT).show();
                    }
                } else if (taskDetailsBean.getMimeType() != null && taskDetailsBean.getMimeType().equalsIgnoreCase("map")) {
                    if (taskDetailsBean.getMimeType() != null && taskDetailsBean.getMimeType().equalsIgnoreCase("map")) {
                        Log.i("Location", "icon clicked map");
                        Log.d("Location", "map " + taskDetailsBean.getTaskDescription());
                        Log.d("Location", "map " + taskDetailsBean.getMimeType());
                        Bundle bundle = new Bundle();
                        bundle.putString("map", taskDetailsBean.getTaskDescription());
                        bundle.putString("viewmap", "location");
                        Intent intent = new Intent(context, LocationFind.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                }

            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(context);
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xFf,
                        0x00, 0x00)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Delete");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

            }
        };
        listView.setMenuCreator(creator);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                mediaList.remove(position);
                medialistadapter.notifyDataSetChanged();
                return true;
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!Remarks.getText().toString().trim().equalsIgnoreCase("")) {
                    TaskDetailsBean taskBean = new TaskDetailsBean();
                    taskBean.setMimeType("text");
                    Log.i("response", "submit.setOnClickListener( list size 1 -- " + mediaList.size());
                    taskBean.setTaskRequestType(requestType);
                    taskBean.setSubType(requestType);
                    String hr = "";
                    if (requestType.equalsIgnoreCase("rate")) {
                        hr = "($/hr)";
                    } else if (requestType.equalsIgnoreCase("work")) {
                        hr = "(hr)";
                    } else if (requestType.equalsIgnoreCase("m/c_breakdown")) {
                        hr = "(hr)";
                    } else {
                        hr = "(hr)";
                    }
                    taskBean.setTaskDescription(requestType + " " + hr + "\n" + Remarks.getText().toString().trim());
//                    taskBean.setSignalid(Utility.getSessionID());
                    mediaList.add(taskBean);
                    Log.i("response", "submit.setOnClickListener( mediaList size 2 -  " + mediaList.size());
                }
                if (mediaList.size() > 0) {
                    Intent intent1 = new Intent();
                    Log.i("Location", "set.setOnClickListener " + mediaList);
                    Log.i("response", "submit.setOnClickListener( mediaList size 3 -  " + mediaList.size());
                    intent1.putExtra("listValues", mediaList);
                    intent1.putExtra("test", "test");
                    setResult(RESULT_OK, intent1);
                    finish();
                } else {
                    showToast("Please Set any Value");
                }
            }
        });
        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOCTracker loc = new LOCTracker(More_Fields.this);

                if (moreFieldsBean.getMimeTypes().size() > 0) {
                    mediaList.clear();
                    for (String mimeType : moreFieldsBean.getMimeTypes()) {

                        Log.i("MoreFields", "MoreFields mimeType  " + mimeType);
                        if (mimeType.equalsIgnoreCase("autodate")) {
                            // current date showing on the list code
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date date = new Date();
                                TaskDetailsBean taskBean = new TaskDetailsBean();
                                taskBean.setMimeType("text");
                                taskBean.setTaskRequestType(requestType);
                                taskBean.setSubType(requestType);
//                                taskBean.setTaskDescription(sdf.format(date));
//                                taskBean.setSignalid(Utility.getSessionID());
                                taskBean.setTaskDescription(requestType + "\n" + sdf.format(date));
                                mediaList.add(taskBean);
                                medialistadapter.notifyDataSetChanged();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            // current location address on the list code
//                Remarks.setText(loc.getLatitude() +"   "+loc.getLongitude());


                        }
                        if (mimeType.equalsIgnoreCase("autolocation")) {
                            if (loc.canGetLocation()) {
                                Log.i("Location", "set.setOnClickListener getLatitude " + loc.getLatitude() + " getLongitude " + loc.getLongitude());
                                String local = showDetailedLocation(loc.getLatitude(), loc.getLongitude());
                                Log.i("Location", "set.setOnClickListener " + local);
                                TaskDetailsBean taskBean = new TaskDetailsBean();
                                taskBean.setMimeType("text");
                                taskBean.setTaskRequestType(requestType);
                                taskBean.setSubType(requestType);
//                                taskBean.setTaskDescription(local);
//                                taskBean.setSignalid(Utility.getSessionID());
                                taskBean.setTaskDescription(requestType + "\n" + local);
                                mediaList.add(taskBean);
                                medialistadapter.notifyDataSetChanged();

                            } else {
//                loc.showSettingsAlert();
                                try {
                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(More_Fields.this);
                                    alertDialog.setTitle("Gps setting");
                                    alertDialog.setMessage("Unable to get current location. Change permissions");
                                    alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent in = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivityForResult(in, 101);
                                        }
                                    });
                                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                                    alertDialog.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
//                Remarks.setText(local);

                    }
                }
            }

        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TaskDetailsBean taskDetailsBean = mediaList.get(position);
                if (taskDetailsBean.getMimeType().equalsIgnoreCase("image")) {
                    Log.i("group123", "icon clicked image");
                    Intent intent = new Intent(context, FullScreenImage.class);
                    intent.putExtra("image", taskDetailsBean.getTaskDescription());
                    context.startActivity(intent);
                } else if (taskDetailsBean.getMimeType().equalsIgnoreCase("audio")) {
                    Intent intent = new Intent(context, Audioplayer.class);
                    intent.putExtra("audio", taskDetailsBean.getTaskDescription());
                    context.startActivity(intent);
                } else if (taskDetailsBean.getMimeType().equalsIgnoreCase("video")) {
                    Log.i("group123", "icon clicked video");
                    File directory = new File(taskDetailsBean.getTaskDescription());
                    if (directory.exists()) {
                        Log.i("update", "video  **" + taskDetailsBean.getTaskDescription());
                        Intent intent = new Intent(context, VideoPlayer.class);
                        intent.putExtra("video", taskDetailsBean.getTaskDescription());
                        context.startActivity(intent);
                    }
                } else if (taskDetailsBean.getMimeType().equalsIgnoreCase("document")) {
                    Log.i("update conversation ", "mm file " + taskDetailsBean.getMimeType());
                    Log.i("update conversation ", "description " + taskDetailsBean.getTaskDescription());
                    File url = new File(taskDetailsBean.getTaskDescription());
                    if (url.exists()) {
                        Uri uri = Uri.fromFile(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                            // Word document
                            intent.setDataAndType(uri, "application/msword");
                        } else if (url.toString().contains(".pdf")) {
                            // PDF file
                            intent.setDataAndType(uri, "application/pdf");
                        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                            // Powerpoint file
                            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                            // Excel file
                            intent.setDataAndType(uri, "application/vnd.ms-excel");
                        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                            // WAV audio file
                            intent.setDataAndType(uri, "application/x-wav");
                        } else if (url.toString().contains(".rtf")) {
                            // RTF file
                            intent.setDataAndType(uri, "application/rtf");
                        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                            // WAV audio file
                            intent.setDataAndType(uri, "audio/x-wav");
                        } else if (url.toString().contains(".gif")) {
                            // GIF file
                            intent.setDataAndType(uri, "image/gif");
                        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                            // JPG file
                            intent.setDataAndType(uri, "image/jpeg");
                        } else if (url.toString().contains(".txt")) {
                            // Text file
                            intent.setDataAndType(uri, "text/plain");
                        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
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
                        Toast.makeText(context, "can't open", Toast.LENGTH_SHORT).show();
                    }
                } else if (taskDetailsBean.getMimeType() != null && taskDetailsBean.getMimeType().equalsIgnoreCase("map")) {
                    if (taskDetailsBean.getMimeType() != null && taskDetailsBean.getMimeType().equalsIgnoreCase("map")) {
                        Log.i("Location", "icon clicked map");
                        Log.d("Location", "map " + taskDetailsBean.getTaskDescription());
                        Log.d("Location", "map " + taskDetailsBean.getMimeType());
                        Bundle bundle = new Bundle();
                        bundle.putString("map", taskDetailsBean.getTaskDescription());
                        bundle.putString("viewmap", "location");
                        Intent intent = new Intent(context, LocationFind.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                }

            }
        });

        files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent i = new Intent(More_Fields.this, FilePicker.class);
                    startActivityForResult(i, 55);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        /*call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multimediaImage("call");
            }
        });*/
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multimediaImage("image");

            }
        });
        sketch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                startActivityForResult(i, 423);
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multimediaImage("video");
            }
        });
        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(More_Fields.this, AudioRecorder.class);
                    i.putExtra("task", "audio");
                    startActivityForResult(i, 333);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        update_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(More_Fields.this, LocationFind.class);
                    startActivityForResult(intent, 888);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    @Override
    public void ResponceMethod(Object object) {

    }

    @Override
    public void ErrorMethod(Object object) {

    }

    public class ListArrayAdapter extends ArrayAdapter<TaskDetailsBean> {

        ArrayList<TaskDetailsBean> BuddyList;
        LayoutInflater inflater = null;
        Context adapContext;

        public ListArrayAdapter(Context context, ArrayList<TaskDetailsBean> buddyList1) {
            super(context, R.layout.list_items, mediaList);
            BuddyList = buddyList1;
            adapContext = context;
        }

        public View getView(int pos, View conView, ViewGroup group) {
            try {
                if (conView == null) {
                    inflater = (LayoutInflater) adapContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    conView = inflater.inflate(R.layout.list_items, null,
                            false);
                }

                final TaskDetailsBean contactBean = BuddyList.get(pos);
                TextView userName = (TextView) conView.findViewById(R.id.username);
                TextView count = (TextView) conView.findViewById(R.id.status);
                ImageView imageView = (ImageView) conView.findViewById(R.id.view4);


                File imageFile = new File(contactBean.getTaskDescription());
                Log.i("update", "value  " + contactBean.getMimeType());
                Log.i("update", "value  " + contactBean.getTaskDescription());
                Log.i("update", "value  " + imageFile);
                if (imageFile.exists()) {
                    Log.i("update", "value--->" + imageFile);
                    Log.i("update", "value--->" + count);
                    Log.i("update", "value---->" + userName);
                    Log.i("update", "value---->" + contactBean.getMimeType());
                    Log.i("update", "value----->  " + contactBean.getTaskDescription());
                    count.setText(contactBean.getTaskDescription().split("High Message/")[1]);
                    if (contactBean.getMimeType().equalsIgnoreCase("image")) {
                        if (contactBean.getTaskDescription().contains("Sketch")) {
                            userName.setText("Sketch");
                            Log.i("update", "value---->" + userName);
                        } else {
                            userName.setText("Image");
                            Log.i("update", "value---->" + userName);
                        }
                        if (imageFile.exists()) {
                            imageView.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
                        }
                    } else if (contactBean.getMimeType().equalsIgnoreCase("audio")) {
                        userName.setText("Audio");
                        Log.i("update", "value---->" + userName);
                        imageView.setBackgroundResource(R.drawable.audio1);
                    } else if (contactBean.getMimeType().equalsIgnoreCase("video")) {
                        userName.setText("Video");
                        Log.i("update", "value---->" + userName);
                        Bitmap bMap = ThumbnailUtils.createVideoThumbnail(imageFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                        imageView.setImageBitmap(bMap);
                    } else if (contactBean.getMimeType().equalsIgnoreCase("document")) {
                        userName.setText("File");
                        Log.i("update", "value---->" + userName);
                        imageView.setBackgroundResource(R.drawable.ic_blank_file_xl);
                    } else {
                        userName.setText("Type");
                        Log.i("update", "value---->" + userName);
                    }
                } else if (contactBean.getMimeType().equalsIgnoreCase("map")) {
                    userName.setText("map");
                    count.setText(contactBean.getTaskDescription());
                    Log.i("update", "value---->" + userName);
                    Log.i("update", "value---->" + userName);
                    imageView.setBackgroundResource(R.drawable.ic_mapimage);
                } else if (contactBean.getMimeType().equalsIgnoreCase("date")) {
                    userName.setText("date");
                    count.setText(contactBean.getTaskDescription());
                    Log.i("update", "value---->" + userName);
                    Log.i("update", "value---->" + userName);
//                    imageView.setBackgroundResource(R.drawable.ic_mapimage);
                } else if (contactBean.getMimeType().equalsIgnoreCase("text")) {
                    userName.setText("Current Location");
                    Log.i("update", "value---->" + userName);
                    count.setText(contactBean.getTaskDescription());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return conView;
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        try {
            Log.i("profile", "===> inside getRealPathFromURI");
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri,
                    proj, null, null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            Log.e("profile", "====> " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    public Bitmap convertpathToBitmap(String strIPath) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile((compressImage(strIPath)), bmOptions);
        return bitmap;
    }

    public String compressImage(String imageUri) {

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
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
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
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
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
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String getFileName() {
        String strFilename = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
            Date date = new Date();
            strFilename = dateFormat.format(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strFilename;
    }

    public class bitmaploader extends AsyncTask<Uri, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            try {
                super.onPostExecute(result);
                Log.d("image", "came to post execute for image");

                Bitmap img = null;
                if (strIPath != null)
                    img = convertpathToBitmap(strIPath);

                img = convertpathToBitmap(strIPath);
                Log.i("task", "extension" + strIPath);
                String pathExtn = strIPath.split("/")[5];
                Log.i("task", "spilting" + pathExtn);
                pathExtn = pathExtn.substring(pathExtn.length() - 3);
                Log.i("task", "root---->" + pathExtn);
//                        Log.i("task", "extension" + strIPath.split(".")[strIPath.split(".").length - 1]);
                imgfun("image", strIPath, pathExtn);
                if (img != null) {

                    Log.d("OnActivity", "_____On Activity Called______");
                } else {
                    strIPath = null;
                }
            } catch (Exception e) {
                Log.e("profile", "====> " + e.getMessage());
               /* if (AppReference.isWriteInFile)
                    AppReference.logger.error(e.getMessage(), e);*/
              /*  else
                    e.printStackTrace();*/
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub

            try {
                super.onPreExecute();
                ProgressDialog dialog = new ProgressDialog(context);
//                callDisp.showprogress(dialog, context);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.e("profile", "====> " + e.getMessage());
              /*  if (AppReference.isWriteInFile)
                    AppReference.logger.error(e.getMessage(), e);
                else
                    e.printStackTrace();*/
            }
        }

        @Override
        protected Void doInBackground(Uri... params) {
            // TODO Auto-generated method stub
            try {
                for (Uri uri : params) {
                    Log.d("image", "came to doin backgroung for image");
                    FileInputStream fin = (FileInputStream) getContentResolver()
                            .openInputStream(uri);
                    ByteArrayOutputStream straam = new ByteArrayOutputStream();
                    byte[] content = new byte[1024];
                    int bytesread;
                    while ((bytesread = fin.read(content)) != -1) {
                        straam.write(content, 0, bytesread);
                    }
                    byte[] bytes = straam.toByteArray();
                    FileOutputStream fout = new FileOutputStream(strIPath);
                    straam.flush();
                    straam.close();
                    straam = null;
                    fin.close();
                    fin = null;
                    fout.write(bytes);
                    fout.flush();
                    fout.close();
                    fout = null;
                }
            } catch (Exception e) {
                /*if (AppReference.isWriteInFile)
                    AppReference.logger.error(e.getMessage(), e);
                else
                    e.printStackTrace();*/
            }

            return null;
        }

    }

    public void imgfun(String getMediaType, String getMediaPath, String getExt) {
        File imageFile = new File(strIPath);

        if (imageFile.exists()) {
            Log.i("imagefile", "image");
            test = "image";
                      /*  MediaListBean uIbean = new MediaListBean();
                        uIbean.setMediaType("image");
                        uIbean.setMediaPath(strIPath);*/


            TaskDetailsBean taskBean = new TaskDetailsBean();
            Log.e("update", "values--->" + taskBean.getTaskDescription());
            taskBean.setMimeType(getMediaType);
            taskBean.setTaskDescription(getMediaPath);
            taskBean.setTaskRequestType(requestType);
            taskBean.setSubType(requestType);
//            taskBean.setSignalid(Utility.getSessionID());
            Log.e("update", "values--->" + taskBean.getTaskDescription());
            mediaList.add(taskBean);
            medialistadapter.notifyDataSetChanged();
        }
    }

    private void showToast(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(More_Fields.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void multimediaImage(final String type) {
        try {
            Log.i("clone", "===> inside message response");
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_myacc_menu);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.horizontalMargin = 15;
            Window window = dialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM);
            dialog.show();
            TextView gallery = (TextView) dialog.findViewById(R.id.delete_acc);
            gallery.setText("Gallery");
            TextView camera = (TextView) dialog.findViewById(R.id.log_out);
            if (type.equalsIgnoreCase("call")) {
                gallery.setText("audio call");
                camera.setText("video call");
            } else if (type.equalsIgnoreCase("image")) {
                gallery.setText("Gallery");
                camera.setText("Camera");
            } else {
                gallery.setText("Gallery");
                camera.setText("Video");
            }
            if (type.equalsIgnoreCase("image")) {
                camera.setText("Camera");
            } else {
                camera.setText("Video");
            }
            TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            gallery.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    try {
                        dialog.dismiss();

                        if (type.equals("image")) {
                            if (Build.VERSION.SDK_INT < 19) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(intent, 30);
                            } else {
                                Log.i("img", "sdk is above 19");
                                Log.i("clone", "====> inside gallery");
                                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("image/*");
                                startActivityForResult(intent, 31);
                            }
                        } else if (type.equals("video")) {
                            if (Build.VERSION.SDK_INT < 19) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("video/*");
                                startActivityForResult(intent, 32);
                            } else {
                                Log.i("img", "sdk is above 19");
                                Log.i("clone", "====> inside gallery");
                                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("video/*");
                                startActivityForResult(intent, 33);
                            }
                        } else {
                            makeCall(true);

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            camera.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    try {
                        dialog.dismiss();
                        if (type.equals("image")) {
                            final String path = Environment.getExternalStorageDirectory()
                                    + "/High Message/";
                            File directory = new File(path);
                            if (!directory.exists())
                                directory.mkdirs();
                            strIPath = path + getFileName() + ".jpg";
                            Intent intent = new Intent(context, CustomVideoCamera.class);
                            Uri imageUri = Uri.fromFile(new File(strIPath));
                            intent.putExtra("filePath", strIPath);
                            intent.putExtra("isPhoto", true);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, 132);
                        } else if (type.equals("video")) {
                            final String path = Environment.getExternalStorageDirectory()
                                    + "/High Message/";
                            File directory = new File(path);
                            if (!directory.exists())
                                directory.mkdirs();
                            strIPath = path + getFileName() + ".mp4";
                            Intent intent = new Intent(context, CustomVideoCamera.class);
                            intent.putExtra("filePath", strIPath);
                            intent.putExtra("isPhoto", false);
                            intent.putExtra("filePath", strIPath);
//                            Uri fileUri = Uri.fromFile(new File(strIPath));
//                            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                            startActivityForResult(intent, 111);
                        } else {
                            makeCall(false);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void makeCall(boolean audiocall) {
        Intent intent = getIntent();
        String userName = intent.getStringExtra("username");
        String buddy_uri = "sip:" + userName + "@" + getResources().getString(R.string.server_ip);
        MyCall call = new MyCall(MainActivity.account, -1);
        CallOpParam prm = new CallOpParam(true);

        CallSetting opt = prm.getOpt();
        opt.setAudioCount(1);
        if (audiocall) {
            opt.setVideoCount(0);
            MainActivity.isAudioCall = true;
        } else {
            opt.setVideoCount(1);
            MainActivity.isAudioCall = false;
        }
        Log.i("SipVideo", "make call : " + buddy_uri);
        try {
            call.makeCall(buddy_uri, prm);

//                    OnCallSdpCreatedParam param = new OnCallSdpCreatedParam();
//                    SdpSession pot = param.getSdp();
//                    call.onCallSdpCreated(param);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("SipVideo", " delete: b23");
            call.delete();
            return;
        }
        MainActivity.currentCallArrayList.add(call);
//        currentCall = call;
        MainActivity.currentCall = call;
        Log.i("SipVideo", "ContactFragment Make Call MainActivity.currentCall");
        if (MainActivity.currentCallArrayList.size() > 0) {
            showCallActivity();
        }
    }

    private void showCallActivity() {
        Log.i("SipVideo", "showCallActivity method");
        Intent intent = new Intent(context, CallActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String par_name = Appreference.loginuserdetails.getFirstName() + " " + Appreference.loginuserdetails.getLastName();
        intent.putExtra("host", par_name);
        startActivity(intent);
    }

    private String showDetailedLocation(Double latitude, Double longitude) {
        String con = "";
        try {
            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(latitude, longitude, 1);
            if (addresses.isEmpty() && addresses != null && addresses.size() != 0) {
                Toast.makeText(getApplicationContext(), "Waiting for loacation", Toast.LENGTH_SHORT).show();
            } else {
                if (addresses.size() > 0) {
                    con = addresses.get(0).getSubLocality()+ "," + addresses.get(0).getFeatureName()+","+addresses.get(0).getLocality()+","/*+addresses.get(0).getSubAdminArea()+","*/+addresses.get(0).getAdminArea() + "," + addresses.get(0).getCountryCode() + "," + addresses.get(0).getCountryName();
                    /*+ "," + addresses.get(0).getFeatureName()*//* + "," + addresses.get(0).getLocality()*//* + "," + addresses.get(0).getSubLocality()*//* + "," + addresses.get(0).getThoroughfare()*//* + "\n" + addresses.get(0).getSubThoroughfare()*/;
//                    con = addresses.get(0).getFeatureName() + "," + addresses.get(0).getAdminArea() + "," + addresses.get(0).getCountryCode() + "," + addresses.get(0).getCountryName() + "," + addresses.get(0).getLocality() + "," + addresses.get(0).getPostalCode() /*+ "," + addresses.get(0).getSubAdminArea()*/;
//                    MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(con);// adding marker
//                    mMap.addMarker(marker);
                    Log.i("Location", "set.setOnClickListener  showDetailedLocation " + con);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }


}


