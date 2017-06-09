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
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.myapplication3.Bean.TaskDetailsBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import json.EnumJsonWebservicename;

/**
 * Created by prasanth on 6/18/2016.
 */
public class MultipleFileUploadActivity extends Activity{

    private Context context;


    String strIPath;
    private ListView list_all;
    public static ArrayList<TaskDetailsBean> mediaList;
    public static MediaListAdapter medialistadapter;
    private Handler handler = new Handler();
    ProgressDialog progress;
    String filepath = null;
    String fileOriginalName = null;
    String fileExt=null;
    final static int REQUESTCODE_RECORDING=99;
    static  MultipleFileUploadActivity multipleFileUploadActivity;

    public static MultipleFileUploadActivity getInstance()
    {
        return multipleFileUploadActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiplefile_upload_activity);


        context = this;
        multipleFileUploadActivity = this;

        Button photo=(Button)findViewById(R.id.photo);
        Button video=(Button)findViewById(R.id.video);
        Button doc=(Button)findViewById(R.id.doc);
        Button audio=(Button)findViewById(R.id.audio);
        list_all=(ListView)findViewById(R.id.list_all);
        Button cancel=(Button)findViewById(R.id.cancel);
        Button save=(Button)findViewById(R.id.save);


        /*mediaList=new ArrayList<TaskDetailsBean>();
       medialistadapter = new MediaListAdapter(MultipleFileUploadActivity.this, mediaList, "multiUpload"*//*, new MediaListAdapter.onClick() {
           @Override
           public void onClick(TaskDetailsBean gcBean, MediaListAdapter.ViewHolder v, int position, View view) {

           }

           @Override
           public void onLongClick(int position, View view) {

           }
       }*//*);*/
        list_all.setAdapter(medialistadapter);

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
        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Intent i = new Intent(MultipleFileUploadActivity.this, FilePicker.class);
                    startActivityForResult(i, 55);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multimediaAudio("audio");
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Appreference.webview_refresh = true;
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (isNetworkAvailable()) {
                        showprogress();

                        JSONObject[] listpostfiles_object1 = new JSONObject[mediaList.size()];
                        if (mediaList != null && mediaList.size() > 0) {
                            int i = 0;
                            for (TaskDetailsBean bean : mediaList) {
                                listpostfiles_object1[i] = new JSONObject();
                                int user_id = Appreference.loginuserdetails.getId();
                                Log.i("test", "user id   " + Appreference.loginuserdetails.getId());
                                Log.i("test", "Media Type   " + bean.getMimeType());
                                Log.i("test", "Media Path   " + bean.getTaskDescription());

                                listpostfiles_object1[i].put("userId", user_id);
                                listpostfiles_object1[i].put("fileType", bean.getMimeType());
                                File file = new File(bean.getTaskDescription());
                                String imgcontent = "";
                                if (bean.getMimeType().equalsIgnoreCase("image")) {
                                    listpostfiles_object1[i].put("fileExtention", "jpg");
                                    imgcontent = encodeTobase64(BitmapFactory.decodeFile(file.getPath()));
                                } else if (bean.getMimeType().equalsIgnoreCase("video")) {
                                    listpostfiles_object1[i].put("fileExtention", "mp4");
                                    imgcontent = encodeAudioVideoToBase64(file.getPath());
                                } else if (bean.getMimeType().equalsIgnoreCase("audio")) {
                                    Log.i("test", "file Path   " + file.getPath());
                                    listpostfiles_object1[i].put("fileExtention", "mp3");
                                    imgcontent = encodeAudioVideoToBase64(file.getPath());
                                } else if (bean.getMimeType().equalsIgnoreCase("doc")) {
                                    if(fileExt!=null)
                                        listpostfiles_object1[i].put("fileExtention", fileExt);
                                    imgcontent = encodeFileToBase64Binary(file.getPath());
                                }
                                filepath = bean.getTaskDescription();
                                fileOriginalName = filepath.substring(filepath.lastIndexOf("/") + 1);
                                listpostfiles_object1[i].put("fileOriginalName", fileOriginalName);
                                listpostfiles_object1[i].put("fileContent", imgcontent);
                                Log.i("test", "file Path   " + file.getPath());
                                Log.i("test", "image Path   " + imgcontent);
                                i++;
                            }
                        }


                        JSONArray listpostfiles_object = new JSONArray();
                        for (int i = 0; i < listpostfiles_object1.length; i++) {
                            listpostfiles_object.put(listpostfiles_object1[i]);
                        }

                        Log.i("JSONArray", "is " + listpostfiles_object);
                        //  JsonRequestSender jsonRequesrSender = new JsonRequestSender();
//                        Appreference.jsonRequestSender.multiFileUpload(EnumJsonWebservicename.multiFileUpload, listpostfiles_object);
                        // jsonRequesrSender.start();
                        //
                    } else {
                        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
            if(type.equalsIgnoreCase("image"))
            {
                gallery.setText("photo from gallery");
            }else if(type.equalsIgnoreCase("video"))
            {
                gallery.setText("video from gallery");
            }

            TextView camera = (TextView) dialog.findViewById(R.id.log_out);
            if(type.equalsIgnoreCase("image")) {
                camera.setText("take photo");
            }else if(type.equalsIgnoreCase("video"))
            {
                camera.setText("record video");
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
                                    + "/HighMessage/";
                            File directory = new File(path);
                            if (!directory.exists())
                                directory.mkdir();
                            strIPath = path + getFileName() + ".jpg";
                            Intent intent = new Intent(
                                    "android.media.action.IMAGE_CAPTURE");
                            Uri imageUri = Uri.fromFile(new File(strIPath));
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, 132);
                        }else{
                            final String path = Environment.getExternalStorageDirectory()
                                    + "/HighMessage/" ;
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
            TextView record_audio = (TextView) dialog.findViewById(R.id.log_out);
            if(type.equalsIgnoreCase("audio")){
                audio.setText("audio track picker");
                record_audio.setText("record audio");
            }
            audio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        dialog.dismiss();


                        if (Build.VERSION.SDK_INT < 19) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("audio/*");
                            startActivityForResult(intent, 222);
                        } else {
                            Log.i("img", "sdk is above 19");
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("audio/*");
                            //startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 223);
                            startActivityForResult(intent, 223);
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


    public void notifymultifileUploadResponse(final String values){
        Log.i("postEntry", "Newpostactivity  notifypostEntryResponse method");
        cancelDialog();
        Appreference.webview_refresh = true;
        try {
            JSONObject json = new JSONObject(values);
            if(values.contains("result_code")) {
                int resultCode = json.getInt("result_code");
                final String text = json.getString("result_text");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showToast(text);

                    }
                });
            }else {
                showToast("Post failed");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
//        Toast.makeText(getApplicationContext(), "file added successfull",
//                Toast.LENGTH_LONG).show();
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
                                        + "/HighMessage/" + getFileName() + ".jpg";

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
                                + "/HighMessage/"
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
                } else if (requestCode == 32) {
                    if (data != null) {
                        Log.i("AAA", "New activity 32*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/HighMessage/" + getFileName() + ".mp4";

                        strIPath = path;
                        Log.i("AAA", "New activity " + strIPath);
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
                else if (requestCode == 33) {
                    if (data != null) {
                        Log.i("AAA", "New activity 33*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/HighMessage/" + getFileName() + ".mp4";

                        Log.i("AAA", "New activity " + strIPath);
                        strIPath = path;
                        Log.i("AAA", "New activity " + strIPath);
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
                } else if (requestCode == 132) {
                    File new_file = new File(strIPath);
                    if (new_file.exists()) {
                        TaskDetailsBean uIbean = new TaskDetailsBean();
                        uIbean.setMimeType("video");
                        uIbean.setTaskDescription(strIPath);
                        mediaList.add(uIbean);
                        medialistadapter.notifyDataSetChanged();
                        Log.i("AAAA", "onactivity result $$$$$$$$$$$$$$$" + strIPath);
                    }
//                    showprogress();
//                    Log.i("AAAA","onactivity result ");
//                    new imageOrientation().execute("image");
                } else if (requestCode == 111) {
                    Log.i("Avideo", "New activity 33************* : " + strIPath);
                    TaskDetailsBean uIbean = new TaskDetailsBean();
                    uIbean.setMimeType("video");
                    uIbean.setTaskDescription(strIPath);
                    mediaList.add(uIbean);
                    medialistadapter.notifyDataSetChanged();
//                    showprogress();
//                    Log.i("AAAA","onactivity result ");
//                    new imageOrientation().execute("image");
                } else if (requestCode == 55) {

                    String filePath = data.getStringExtra("filePath");
                    String fileName = data.getStringExtra("fileName");
                    fileExt = data.getStringExtra("fileExt");
//                    fileExt=fileExt1.substring(fileExt1.lastIndexOf(1));
                    Log.i("fExt","fileExt"+fileExt);
                    Log.i("filePath", "fm--->" + filePath);
                    Log.i("filename", "fm--->" + fileName);
                    Log.i("fileExt", "fm--->" + fileExt);
                    strIPath = filePath;
                    TaskDetailsBean uIbean = new TaskDetailsBean();
                    uIbean.setMimeType("document");
                    uIbean.setTaskDescription(strIPath);
                    Log.i("mp","mpath"+strIPath);
                    mediaList.add(uIbean);
                    medialistadapter.notifyDataSetChanged();
                } else if (requestCode == 99) {
                    if (data != null) {
                        Log.i("AAA","New activity 33*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/HighMessage/" + getFileName() + ".mp3";

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
                                + "/HighMessage/" + getFileName() + ".mp3";

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
                                + "/HighMessage/" + getFileName() + ".mp3";

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
    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static boolean isAvailable(Context ctx, Intent intent) {

        final PackageManager mgr = ctx.getPackageManager();

        List<ResolveInfo> list =
                mgr.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);

        return list.size() > 0;

    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().getApplicationContext().CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
        Bitmap bitmap = BitmapFactory.decodeFile((strIPath), bmOptions);

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
        Toast.makeText(MultipleFileUploadActivity.this, msg, Toast.LENGTH_LONG)
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

                    progress = new ProgressDialog(MultipleFileUploadActivity.this);
                    progress.setCancelable(true);
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
            Log.i("file1", "file1========" + file1);
            file1.read(Bytearray);
            Log.i("file1", "file1========" + file1);
            strFile = Base64.encodeToString(Bytearray, Base64.DEFAULT);//Convert byte array into string
            Log.i("file1", "file1========" + file1);

        } catch (IOException e) {
            e.printStackTrace();
        }
//        Log.i("FileUpload", "audioVideoEncode========" + strFile);
        return strFile;
    }

    private String encodeFileToBase64Binary(String fileName)
            throws IOException {

        File file = new File(fileName);
        byte[] bytes = loadFile(file);
        byte[] encoded = org.apache.commons.codec.binary.Base64.encodeBase64(bytes);
        String encodedString = new String(encoded);

        return encodedString;
    }

    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int)length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }

        is.close();
        return bytes;
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
}






