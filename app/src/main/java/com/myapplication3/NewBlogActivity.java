package com.myapplication3;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.myapplication3.Bean.TaskDetailsBean;
import com.myapplication3.DB.VideoCallDataBase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import json.EnumJsonWebservicename;

public class NewBlogActivity extends Activity {


    private Context context;
    private EditText message;
    private TextView select_group;
    private Spinner group_spinner;
    String strIPath;
    private ListView list_all;
    public static ArrayList<TaskDetailsBean> mediaList;
    public static MediaListAdapter medialistadapter;
    private Handler handler = new Handler();
    ProgressDialog progress;
    static  NewBlogActivity newBlogActivity;
    Boolean blog_filter;
    String sessionid;
    final static int REQUESTCODE_RECORDING=99;

    public static NewBlogActivity getInstance()
    {
        return newBlogActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_blog_activity);


        context = this;
        newBlogActivity = this;
        message=(EditText)findViewById(R.id.et_msg);
        Button photo=(Button)findViewById(R.id.photo);
        Button video=(Button)findViewById(R.id.video);
        Button audio=(Button)findViewById(R.id.audio);
        Button cancel=(Button)findViewById(R.id.cancel);
        Button save=(Button)findViewById(R.id.save);
        list_all=(ListView)findViewById(R.id.list_all);
        select_group = (TextView) findViewById(R.id.tv_group);
        group_spinner = (Spinner) findViewById(R.id.spinner_select_group);

        mediaList=new ArrayList<TaskDetailsBean>();
        /*medialistadapter = new MediaListAdapter(NewBlogActivity.this, mediaList, "blog"*//*, new MediaListAdapter.onClick() {
            @Override
            public void onClick(TaskDetailsBean gcBean, MediaListAdapter.ViewHolder v, int position, View view) {

            }

            @Override
            public void onLongClick(int position, View view) {

            }
        }*//*);*/
        list_all.setAdapter(medialistadapter);

        blog_filter = getIntent().getExtras().getBoolean("blog filter");



        if(!blog_filter)
        {
            select_group.setVisibility(View.VISIBLE);
            group_spinner.setVisibility(View.VISIBLE);

            final ArrayList<ContactBean> list = VideoCallDataBase.getDB(context).getGroup(Appreference.loginuserdetails.getUsername());

            ArrayList<String> list2=new ArrayList<String>();
            list2.add(0,"Please Select");
            if(list!=null && list.size()>0) {
                for (ContactBean a : list) {
                    list2.add(a.getGroupName());
                }
            }


            ArrayAdapter<String> adp2=new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item,list2);
            adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            group_spinner.setAdapter(adp2);
            group_spinner.setPopupBackgroundResource(R.drawable.spinner);

        }


        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multimediaImage("image");
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multimediaImage("video");
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appreference.webview_refresh = true;
                finish();
            }
        });

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multimediaAudio("audio");
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(message.getText().toString()!=null && message.getText().toString().trim().length()>0) {
                    try {
                        if(isNetworkAvailable()) {


                            JSONObject user_id = new JSONObject();
                            user_id.put("id", Appreference.loginuserdetails.getId());

                            JSONObject[] listpostfiles_object1 = new JSONObject[mediaList.size()];
                            if (mediaList != null && mediaList.size() > 0) {
                                int i = 0;
                                for (TaskDetailsBean bean : mediaList) {
                                    listpostfiles_object1[i] = new JSONObject();
                                    listpostfiles_object1[i].put("fileType", bean.getMimeType());
                                    File file = new File(bean.getTaskDescription());
                                    Log.d("HighMessage--->","MediaPath Image  "+bean.getTaskDescription());
                                    String imgcontent = "";
                                    if (bean.getMimeType().equalsIgnoreCase("image")) {
                                        listpostfiles_object1[i].put("blogFileExt", "jpg");
                                        Log.d("HighMessage--->","FilePath Image  "+file.getPath());
                                        Log.d("HighMessage--->","Compress Image  "+compressImage(file.getPath()));
                                        imgcontent = encodeTobase64(BitmapFactory.decodeFile(compressImage(file.getPath())));
//                                        imgcontent = encodeTobase64(BitmapFactory.decodeFile(file.getPath()));
                                    } else if (bean.getMimeType().equalsIgnoreCase("video")) {
                                        listpostfiles_object1[i].put("blogFileExt", "mp4");
                                        imgcontent = encodeAudioVideoToBase64(file.getPath());
                                    }else if (bean.getMimeType().equalsIgnoreCase("audio")) {
                                        listpostfiles_object1[i].put("blogFileExt", "mp3");
                                        imgcontent = encodeAudioVideoToBase64(file.getPath());
                                    }
                                    Log.i("AAA", "new post path " + bean.getTaskDescription());
                                    listpostfiles_object1[i].put("fileContent", imgcontent);
                                    i++;
                                }
                            }

                            JSONArray listpostfiles_object = new JSONArray();
                            for (int i = 0; i < listpostfiles_object1.length; i++) {
                                listpostfiles_object.put(listpostfiles_object1[i]);
                            }


                            JSONObject reqparam = new JSONObject();
                            reqparam.put("user", user_id);


                            if(!blog_filter ) {

                                if (  !group_spinner.getSelectedItem().toString().equalsIgnoreCase("Please Select")) {
                                    sessionid = VideoCallDataBase.getDB(context).
                                            getSessionid(group_spinner.getSelectedItem().toString().trim());

                                    showprogress();

                                    if(sessionid!=null && !sessionid.equalsIgnoreCase("") && sessionid.length()>0) {
                                        JSONObject session_id = new JSONObject();
                                        session_id.put("id", sessionid);
                                        Log.i("group","inside group");
                                        reqparam.put("group", session_id);
                                    }
                                    reqparam.put("description", message.getText().toString().trim());
                                    reqparam.put("listBlogFiles", listpostfiles_object);
//                        JSONRequestResponse requestResponse=JSONRequestResponse.getJsonRequestResponse();
//                            Log.d("Post", "---->postEntry composed object" + reqparam.toString());

                                    Appreference.jsonRequestSender.BlogEntry(EnumJsonWebservicename.blogEntry, reqparam);

                                    showprogress();

                                }else
                                {
                                    Toast.makeText(context,"Please select group",Toast.LENGTH_LONG).show();

                                }


                            }else
                            {
                                showprogress();

                                reqparam.put("description", message.getText().toString().trim());
                                reqparam.put("listBlogFiles", listpostfiles_object);
//                        JSONRequestResponse requestResponse=JSONRequestResponse.getJsonRequestResponse();
//                            Log.d("Post", "---->postEntry composed object" + reqparam.toString());

                                Appreference.jsonRequestSender.BlogEntry(EnumJsonWebservicename.blogEntry, reqparam);

                            }

/*

                            if(sessionid!=null && !sessionid.equalsIgnoreCase("") && sessionid.length()>0) {
                                JSONObject session_id = new JSONObject();
                                session_id.put("id", sessionid);
                                Log.i("group","inside group");
                                reqparam.put("group", session_id);
                            }
                            reqparam.put("description", message.getText().toString().trim());
                            reqparam.put("listBlogFiles", listpostfiles_object);
//                        JSONRequestResponse requestResponse=JSONRequestResponse.getJsonRequestResponse();
//                            Log.d("Post", "---->postEntry composed object" + reqparam.toString());

                                    Appreference.jsonRequestSender.BlogEntry(EnumJsonWebservicename.blogEntry, reqparam);
*/

                            //
                        }else{
                            Toast.makeText(context,"No Internet Connection",Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else
                    showToast("Please enter message to proceed");
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
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM);
            dialog.show();
            TextView gallery = (TextView) dialog.findViewById(R.id.delete_acc);
            gallery.setText("Gallery");
            TextView camera = (TextView) dialog.findViewById(R.id.log_out);
            if(type.equalsIgnoreCase("image")) {
                camera.setText("Camera");
            }else{
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

                        if(type.equals("image")) {
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
                        }else{
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
                        if(type.equals("image")) {
                            final String path = Environment.getExternalStorageDirectory()
                                    + "/High Message/";
                            File directory = new File(path);
                            if (!directory.exists())
                                directory.mkdir();
                            strIPath = path + getFileName() + ".jpg";
                            Intent intent = new Intent(
                                    "android.media.action.IMAGE_CAPTURE");
//                            Log.d("HighMessage--->","Compress Image  "+compressImage(strIPath));
//                            Uri imageUri = Uri.fromFile(new File(compressImage(strIPath)));
                            Uri imageUri = Uri.fromFile(new File(strIPath));
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, 132);
                        }else{
                            final String path = Environment.getExternalStorageDirectory()
                                    + "/High Message/" ;
                            File directory = new File(path);
                            if (!directory.exists())
                                directory.mkdir();
                            strIPath = path+  getFileName() + ".mp4";
                            Intent intent = new Intent(context,CustomVideoCamera.class);
                            intent.putExtra("filePath", strIPath);
//                            Uri fileUri = Uri.fromFile(new File(strIPath));
//                            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                            startActivityForResult(intent, 111);
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

    private void multimediaAudio(final String type) {
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
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            window.setAttributes(lp);
            window.setGravity(Gravity.BOTTOM);
            dialog.show();

            TextView audio = (TextView) dialog.findViewById(R.id.delete_acc);
            audio.setVisibility(View.GONE);
            TextView record_audio = (TextView) dialog.findViewById(R.id.log_out);
            if(type.equalsIgnoreCase("audio")){
//                audio.setText("audio track picker");
                record_audio.setText("record audio");
            }
            audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        dialog.dismiss();


                        if (Build.VERSION.SDK_INT < 19) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("*/*");
                            startActivityForResult(intent, 222);
                        } else {
                            Log.i("img", "sdk is above 19");
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("*/*");
                            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 223);
//                            startActivityForResult(intent, 223);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            record_audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try
                    {
                        dialog.dismiss();

                        Intent intent =
                                new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                        if (isAvailable(getApplicationContext(), intent)) {
                            startActivityForResult(intent,
                                    REQUESTCODE_RECORDING);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
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

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }



    public void notifypostEntryResponse(final String values){
        Log.i("postEntry", "Newpostactivity  notifypostEntryResponse method");
        cancelDialog();
        Appreference.webview_refresh = true;
        finish();
        try {
            JSONObject json = new JSONObject(values);
            if(values.contains("result_code")) {
                int resultCode = json.getInt("result_code");
                final String text = json.getString("result_text");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showToast(text);
                        cancelDialog();
                    }
                });
            }else
                showToast("Post failed");
            cancelDialog();
        }catch (Exception e){
            e.printStackTrace();
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // TODO Auto-generated method stub
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
                                        strIPath = path;
                                    }

                                    if (bitmap != null) {
                                        bitmap = Bitmap.createScaledBitmap(
                                                bitmap, 200, 150, false);
                                    }
                                    TaskDetailsBean uIbean = new TaskDetailsBean();
                                    uIbean.setMimeType("image");
                                    uIbean.setTaskDescription(strIPath);
                                    mediaList.add(uIbean);
                                    medialistadapter.notifyDataSetChanged();
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
                        Log.d("busy", "........ size is------------->" + length);
                        if (length <= 2) {
                            new bitmaploader().execute(selectedImageUri);
                        } else {
                            showToast("Kindly Select someother image,this image is too large");
                        }
                    }
                }else if(requestCode==32){
                    if (data != null) {
                        Log.i("AAA","New activity 32*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + getFileName() + ".mp4";

                        strIPath = path;
                        Log.i("AAA","New activity "+strIPath);
                        TaskDetailsBean uIbean = new TaskDetailsBean();
                        uIbean.setMimeType("video");
                        uIbean.setTaskDescription(strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();
                    }
                }else if(requestCode==33){
                    if (data != null) {
                        Log.i("AAA","New activity 33*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + getFileName() + ".mp4";

                        Log.i("AAA","New activity "+strIPath);
                        strIPath = path;
                        Log.i("AAA","New activity "+strIPath);
                        TaskDetailsBean uIbean = new TaskDetailsBean();
                        uIbean.setMimeType("video");
                        uIbean.setTaskDescription(strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();
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
                        fout = null;
                    }
                }
                else if (requestCode == 132) {
                    File new_file = new File(strIPath);
                    if (new_file.exists()) {
                        TaskDetailsBean uIbean = new TaskDetailsBean();
                        uIbean.setMimeType("image");
                        uIbean.setTaskDescription(strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();
                        Log.i("AAAA", "onactivity result $$$$$$$$$$$$$$$" + strIPath);
                    }
//                    showprogress();
//                    Log.i("AAAA","onactivity result ");
//                    new imageOrientation().execute("image");
                }
                else if (requestCode == 111) {
                    Log.i("Avideo","New activity 33************* : "+strIPath);
                    TaskDetailsBean uIbean = new TaskDetailsBean();
                    uIbean.setMimeType("video");
                    uIbean.setTaskDescription(strIPath);
                    mediaList.add(uIbean);
                    medialistadapter.notifyDataSetChanged();
//                    showprogress();
//                    Log.i("AAAA","onactivity result ");
//                    new imageOrientation().execute("image");
                }
                else if (requestCode == 99) {
                    if (data != null) {
                        Log.i("AAA","New activity 33*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + getFileName() + ".mp3";

                        Log.i("AAA","New activity "+strIPath);
                        strIPath = path;
                        Log.i("AAA","New activity "+strIPath);

                        TaskDetailsBean uIbean = new TaskDetailsBean();
                        uIbean.setMimeType("audio");
                        uIbean.setTaskDescription(strIPath);
                        Log.i("mp","mpath"+strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();

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
                        fout = null;
                    }

                }else if (requestCode == 222) {
                    if (data != null) {
                        Log.i("AAA", "New activity 32*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + getFileName() + ".mp3";

                        strIPath = path;
                        Log.i("AAA", "New activity " + strIPath);
                        TaskDetailsBean uIbean = new TaskDetailsBean();
                        uIbean.setMimeType("audio");
                        uIbean.setTaskDescription(strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();
                    }
                }
                else if (requestCode == 223) {
                    if (data != null) {
                        Log.i("AAA","New activity 33*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + getFileName() + ".mp3";

                        Log.i("AAA","New activity "+strIPath);
                        strIPath = path;
                        Log.i("AAA","New activity "+strIPath);
                        TaskDetailsBean uIbean = new TaskDetailsBean();
                        uIbean.setMimeType("audio");
                        uIbean.setTaskDescription(strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();
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
                        fout = null;
                    }
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block

        }

    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().getApplicationContext().CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isAvailable(Context ctx, Intent intent) {

        final PackageManager mgr = ctx.getPackageManager();

        List<ResolveInfo> list =
                mgr.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);

        return list.size() > 0;

    }

    public String getRealPathFromURI(Uri contentUri) {
        try {
            Log.i("profile", "===> inside getRealPathFromURI");
            String[] proj = { MediaStore.Images.Media.DATA };
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

    public Bitmap convertpathToBitmap(String strIPath)
    {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile((strIPath),bmOptions);

        return  bitmap;

    }
    public  class bitmaploader extends AsyncTask<Uri, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            try {
                super.onPostExecute(result);
                Log.d("image", "came to post execute for image");

                Bitmap img = null;
                if (strIPath != null)
                    img = convertpathToBitmap(strIPath);
                if (img != null) {
                    TaskDetailsBean uIbean = new TaskDetailsBean();
                    uIbean.setMimeType("image");
                    uIbean.setTaskDescription(strIPath);
                    mediaList.add(uIbean);
                    medialistadapter.notifyDataSetChanged();
                   /* if (progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }*/
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

        }@Override
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

    public void showToast(String msg) {
        Toast.makeText(NewBlogActivity.this, msg, Toast.LENGTH_LONG)
                .show();
    }



    public static void clearMediaList(int position)
    {
        mediaList.remove(position);
        medialistadapter.notifyDataSetChanged();
        if(mediaList.size()==0) {
            mediaList.clear();
            medialistadapter.notifyDataSetChanged();
        }
    }



        private void showprogress() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.i("login123", "inside showProgressDialog");

                        progress = new ProgressDialog(NewBlogActivity.this);
                        progress.setCancelable(false);
                        progress.setMessage("Progress ...");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setProgress(0);
                        progress.setMax(100);
                        progress.show();
                    } catch (Exception e) {
//                        SingleInstance.printLog(null, e.getMessage(), null, e);
                    }
                }


            });
        }
        public void cancelDialog() {
            try {
                if (progress != null && progress.isShowing()) {
                    Log.i("register", "--progress bar end-----");
                    progress.dismiss();
                    progress = null;
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }


    private String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imageEncoded;
    }

    private String encodeAudioVideoToBase64(String path){
        String strFile=null;
        File file=new File(path);
        try {
            FileInputStream file1=new FileInputStream(file);
            byte[] Bytearray=new byte[(int)file.length()];
            file1.read(Bytearray);
            strFile = Base64.encodeToString(Bytearray, Base64.DEFAULT);//Convert byte array into string

        } catch (IOException e) {
            e.printStackTrace();
        }
//        Log.i("FileUpload", "audioVideoEncode========" + strFile);
        return strFile;
    }



    private Bitmap reduceFileSize(File f){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_SIZE=70;

            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
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
    }


}

