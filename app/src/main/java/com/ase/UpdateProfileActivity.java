package com.ase;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.JsonRequestSender;
import json.Loginuserdetails;
import json.WebServiceInterface;

/**
 * Created by vadivel on 06-06-2016.
 */
public class UpdateProfileActivity extends AppCompatActivity implements WebServiceInterface {

    static UpdateProfileActivity updateProfileActivity;
    EditText ed1, ed2, ed3, editText5, editText6, editText7, editText8,role,profession,specialisation,organisation;
    Spinner spn, spinner1;
    TextView textView9, textView10;
    ImageView imageView, imageView1, closeImageView, attached, video;
    ImageButton imageButton;
    Button button, button2;
    Handler handler = new Handler();
    ImageLoader imageLoader;
    String spinner = null;
    String title1 = null;
    String profileimage = null;
    String imgcontent = null;
    String fileContent = null;
    String fileExtension = null;
    String videoContent = null;
    String videoExtension = null;
    String fileExtn = null;
    Bitmap mBitmap = null;
    Context context;
    Boolean imagevalue = false;
    ArrayList<File> pdfList = new ArrayList<File>();
    ArrayList<File> videoList = new ArrayList<File>();
    int j = 0;
    int k = 0;
    String strIPath = null;
    private ProgressDialog progress;
    String profile;
    String ImageName;

    public static UpdateProfileActivity getInstance() {
        return updateProfileActivity;
    }

    public static void renameFile(String oldName, String newName) {
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/");
        if (dir.exists()) {
            Log.i("old", "UserName >------>" + oldName);
            File from = new File(dir, oldName);
            Log.i("old", "UserName >" + from);
            Log.i("old", "UserName >------>" + newName);
            File to = new File(dir, newName);
            Log.i("new", "ResName >" + to);
            if (from.exists()) {
                from.renameTo(to);
                Appreference.loginuserdetails.setProfileImage(newName);
                Log.i("new", "rename success---->" + from);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editable);
        context = this;
        updateProfileActivity = this;
        Intent intent = this.getIntent();
        final String fn = intent.getStringExtra("firstname");
        final String ln = intent.getStringExtra("lastname");
        final String ttl = intent.getStringExtra("title");
        final String gdr = intent.getStringExtra("gender");
//        final String imageview1 = intent.getStringExtra("profileimage");

        ed1 = (EditText) findViewById(R.id.editText);
        ed2 = (EditText) findViewById(R.id.editText2);
        ed3 = (EditText) findViewById(R.id.editText3);
        spn = (Spinner) findViewById(R.id.spinner);
        //spinner1=(Spinner)findViewById(R.id.spinner1);
//Job categories
        editText5 = (EditText) findViewById(R.id.editText5);
        editText6 = (EditText) findViewById(R.id.editText6);
        editText7 = (EditText) findViewById(R.id.editText7);
        editText8 = (EditText) findViewById(R.id.editText8);
//        role=(EditText)findViewById(R.id.roles);
//        profession=(EditText)findViewById(R.id.UProfession);
//        specialisation=(EditText)findViewById(R.id.specialisation);
//        organisation=(EditText)findViewById(R.id.organization);


        textView9 = (TextView) findViewById(R.id.editText9);
        textView10 = (TextView) findViewById(R.id.editText10);

        imageView = (ImageView) findViewById(R.id.imageView_round);
        attached = (ImageView) findViewById(R.id.attached);
        video = (ImageView) findViewById(R.id.video);
        File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + Appreference.loginuserdetails.getProfileImage());
        if (Appreference.loginuserdetails.getProfileImage() != null) {
            if (myFile.exists()) {
                imageLoader = new ImageLoader(context);
                Log.i("Appreference", "if-------->" + Appreference.loginuserdetails.getProfileImage());
                imageLoader.DisplayImage(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + Appreference.loginuserdetails.getProfileImage(), imageView, R.drawable.default_person_circle);
            } else {
                Picasso.with(getApplicationContext()).load(getResources().getString(R.string.user_upload) + Appreference.loginuserdetails.getProfileImage()).into(imageView);
            }

        }
//        Picasso.with(getApplicationContext()).load("http://122.165.92.171:8080/uploads/highmessaging/user/" + imageview1).into(imageView);

        List<String> categories = new ArrayList<String>();
        categories.add("Male");
        categories.add("Female");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(dataAdapter);

        spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ed1.setText(fn);
        ed2.setText(ln);
        ed3.setText(ttl);
        editText5.setText(Appreference.loginuserdetails.getJobCategory1());
        Log.i("profile", "job1" + Appreference.loginuserdetails.getJobCategory1());
        editText6.setText(Appreference.loginuserdetails.getJobCategory2());
        editText7.setText(Appreference.loginuserdetails.getJobCategory3());
        editText8.setText(Appreference.loginuserdetails.getJobCategory4());
        textView10.setText(Appreference.loginuserdetails.getVideoProfile());
        textView9.setText(Appreference.loginuserdetails.getTextProfile());
//        role.setText(Appreference.loginuserdetails.getUserType());
//        profession.setText(Appreference.loginuserdetails.getProfession());
//        specialisation.setText(Appreference.loginuserdetails.getSpecialisation());
//        organisation.setText(Appreference.loginuserdetails.getOrganization());

        Log.i("task", "Gender" + gdr);
        if (gdr != null && gdr.equalsIgnoreCase("M")) {
            int spnpos = dataAdapter.getPosition("Male");
            spn.setSelection(spnpos);
        } else {
            int spnpos = dataAdapter.getPosition("Female");
            spn.setSelection(spnpos);
        }


// title spinner
        /*List<String> categories1 = new ArrayList<String>();
        categories1.add("Mr.");
        categories1.add("Miss.");
        categories1.add("Mrs.");
        categories1.add("Dr.");

        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories1);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter1);

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                title1 = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Log.i("task", "title" + ttl);
        if(ttl != null && (ttl.equalsIgnoreCase("Dr")||ttl.equalsIgnoreCase("Dr."))) {
            int spnpos = dataAdapter1.getPosition("Dr.");
            Log.i("profile","title" +"Dr."+spnpos);
            spinner1.setSelection(spnpos);
        }else
        if (ttl != null && (ttl.equalsIgnoreCase("Mr")||ttl.equalsIgnoreCase("Mr."))) {
            int spnpos = dataAdapter1.getPosition("Mr.");
            Log.i("profile","title" +"Mr."+spnpos);
            spinner1.setSelection(spnpos);
        } else if(ttl != null && (ttl.equalsIgnoreCase("Miss")||ttl.equalsIgnoreCase("Miss."))) {
            int spnpos = dataAdapter1.getPosition("Miss.");
            Log.i("profile","title" +"Miss."+spnpos);
            spinner1.setSelection(spnpos);
        }else
        {
            int spnpos = dataAdapter1.getPosition("Mrs.");
            spinner1.setSelection(spnpos);
            Log.i("profile","title" +"Mrs."+spnpos);
        }*/
        imageView1 = (ImageView) findViewById(R.id.imageView2);
        closeImageView = (ImageView) findViewById(R.id.imageView1);

        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String first = ed1.getText().toString();
                    String last = ed2.getText().toString();
                    String title = ed3.getText().toString();

                    String gen = spinner;
                    if (spinner.equalsIgnoreCase("Male")) {
                        spinner = "M";
                    } else {
                        spinner = "F";
                    }

                    if ((first.equals(fn)) && (last.equals(ln)) && (title.equals(ttl)) && (spinner.equals(gdr) && imagevalue == false && editText5.getText() != null && editText5.getText().toString().equalsIgnoreCase(Appreference.loginuserdetails.getJobCategory1())) && editText6.getText() != null && editText6.getText().toString().equalsIgnoreCase(Appreference.loginuserdetails.getJobCategory2()) && editText7.getText() != null && editText7.getText().toString().equalsIgnoreCase(Appreference.loginuserdetails.getJobCategory3()) && editText8.getText() != null && editText8.getText().toString().equalsIgnoreCase(Appreference.loginuserdetails.getJobCategory4()) && textView9.getText() != null && textView9.getText().toString().equalsIgnoreCase("") && textView10.getText() != null && textView10.getText().toString().equalsIgnoreCase("")) {
                        if (profileimage == null) {
                            Toast.makeText(getApplicationContext(), "Please update any value", Toast.LENGTH_LONG).show();
                        }
                    } else if (!first.equals("")) {
                        Log.e("Image", "profile" + imagevalue);
                        final int id1 = Appreference.loginuserdetails.getId();
                        final String email = Appreference.loginuserdetails.getEmail();
                        final String pwd = Appreference.loginuserdetails.getPassword();
                        final String code = Appreference.loginuserdetails.getCode();
                        final String gen1 = spinner;

/*

                    {
                       // "id":1,
                            //"email":"test@cognitivemobile.net",
                           // "password":"testtest",
                           // "code":"ANAPR",
                            //"firstName":"First Name",
                            //"lastName":"Last Name",
                            //"title":"Singer",
                            //"gender":"M",
                            //"jobCategory1":"doctor",
                            //"jobCategory2":"Neurologist",
                            //"jobCategory3":"Surgeon",
                            //"jobCategory4":"Children",
                           // "profileImageContent":"imageContent",
                            //"profileImageExt":"jpg",
                            //"textProfileContent":"fdsgdfs6g9sd",
                            //"textProfileExt":"pdf"
                        //"videoProfileContent":"kjsdhgfas76g",
                           // "videoProfileExt":"mp4"
                    }
*/

                        try {
                            Log.i("profile", "json preparing");
                            JSONObject message = new JSONObject();
                            message.put("id", id1);
                            Log.i("profile", "json id" + id1);
                            message.put("firstName", first);
                            Log.i("profile", "json first name" + first);
                            message.put("email", email);
                            Log.i("profile", "json email" + email);
                            message.put("password", pwd);
                            Log.i("profile", "json password" + pwd);
                            message.put("code", code);
                            Log.i("profile", "json code" + code);
                            message.put("lastName", last);
                            Log.i("profile", "json lastname" + last);
                            message.put("title", title);
                            Log.i("profile", "json title" + title);
                            message.put("gender", gen1);
                            Log.i("profile", "json gender" + gen1);
                            // message.put("profileImage", profileimage);
                            message.put("profileImageContent", imgcontent);
                            Log.i("profile", "json imagecontent" + imgcontent);
                            message.put("profileImageExt", fileExtn);
                            Log.i("profile", "json fileExtn" + fileExtn);
                            message.put("status", "A");
                            Log.i("profile", "json status A");
                            message.put("jobCategory1", editText5.getText().toString());
                            Log.i("profile", "json job1" + editText5.getText().toString());
                            message.put("jobCategory2", editText6.getText().toString());
                            Log.i("profile", "json job2" + editText6.getText().toString());
                            message.put("jobCategory3", editText7.getText().toString());
                            Log.i("profile", "json job3" + editText7.getText().toString());
                            message.put("jobCategory4", editText8.getText().toString());
                            Log.i("profile", "json job4" + editText8.getText().toString());
                            message.put("textProfileContent", fileContent);
                            Log.i("profile", "json textcontent" + fileContent);
                            message.put("textProfileExt", fileExtension);
                            Log.i("profile", "json textExt" + fileExtension);
                            message.put("videoProfileContent", videoContent);
                            Log.i("profile", "json videoContent" + videoContent);
                            message.put("videoProfileExt", videoExtension);
                            Log.i("profile", "videoProfileExt" + videoExtension);


//                        Log.i("Message", "is " + message);
                            CommunicationBean bean = new CommunicationBean();
                            bean.setMessage(message);
                            Log.i("profile", message.toString());
                            JsonRequestSender jsonRequesrSender = new JsonRequestSender();
                            jsonRequesrSender.updateUser(EnumJsonWebservicename.updateUser, message, UpdateProfileActivity.this);
                            jsonRequesrSender.start();
                            progress = new ProgressDialog(context);
                            progress.setMessage("Loading");
                            progress.show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Enter FirstName", Toast.LENGTH_LONG).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        attached.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(UpdateProfileActivity.this, FilePicker.class);
                startActivityForResult(in, 55);
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multimedia();
            }
        });

        textView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getPdfList(Environment.getExternalStorageDirectory());
               /* if (textView9.getText() != null) {

                } else {
                    Intent in = new Intent(UpdateProfileActivity.this, FilePicker.class);
                    startActivityForResult(in, 55);
                }*/
//                File url = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/"+textView9.getText().toString());
                File url;
                ImageName = textView9.getText().toString();
                if(strIPath != null) {
                    url = new File(strIPath);
                } else {
                    url = new File(ImageName);
                }
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
                        File file1;
                        file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + ImageName);
                        if (file1.exists()) {
                            Uri uri = Uri.fromFile(file1);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            if (file1.toString().contains(".doc") || file1.toString().contains(".docx")) {
                                // Word document
                                intent.setDataAndType(uri, "application/msword");
                            } else if (file1.toString().contains(".pdf")) {
                                // PDF file
                                intent.setDataAndType(uri, "application/pdf");
                            } else if (file1.toString().contains(".ppt") || file1.toString().contains(".pptx")) {
                                // Powerpoint file
                                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                            } else if (file1.toString().contains(".xls") || file1.toString().contains(".xlsx")) {
                                // Excel file
                                intent.setDataAndType(uri, "application/vnd.ms-excel");
                            } else if (file1.toString().contains(".zip") || file1.toString().contains(".rar")) {
                                // WAV audio file
                                intent.setDataAndType(uri, "application/x-wav");
                            } else if (file1.toString().contains(".rtf")) {
                                // RTF file
                                intent.setDataAndType(uri, "application/rtf");
                            } else if (file1.toString().contains(".wav") || file1.toString().contains(".mp3")) {
                                // WAV audio file
                                intent.setDataAndType(uri, "audio/x-wav");
                            } else if (file1.toString().contains(".gif")) {
                                // GIF file
                                intent.setDataAndType(uri, "image/gif");
                            } else if (file1.toString().contains(".jpg") || file1.toString().contains(".jpeg") || file1.toString().contains(".png")) {
                                // JPG file
                                intent.setDataAndType(uri, "image/jpeg");
                            } else if (file1.toString().contains(".txt")) {
                                // Text file
                                intent.setDataAndType(uri, "text/plain");
                            } else if (file1.toString().contains(".3gp") || file1.toString().contains(".mpg") || file1.toString().contains(".mpeg") || file1.toString().contains(".mpe") || file1.toString().contains(".mp4") || file1.toString().contains(".avi")) {
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
                        }else {
                            Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                            new DownloadVideo(getResources().getString(R.string.user_upload) + ImageName).execute();
                            Log.i("can't open", "file");
                        }
                    }
                }
        });
        textView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageName = textView10.getText().toString();
                Log.i("Task", "imagename" + ImageName);
                File file;

                file = new File(ImageName);
                if (file.exists()) {
                    Intent intent = new Intent(context, VideoPlayer.class);
                    intent.putExtra("video", file.getAbsolutePath());
                    context.startActivity(intent);
                } else {
                    File file1;
                    file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + ImageName);
                    if (file1.exists()) {
                        Intent intent = new Intent(context, VideoPlayer.class);
                        intent.putExtra("video", file1.getAbsolutePath());
                        context.startActivity(intent);
                    } else {
                        new DownloadVideo(getResources().getString(R.string.user_upload) + ImageName).execute();
                        Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        imageButton = (ImageButton)

                findViewById(R.id.button2);

        imageButton.setOnClickListener(new View.OnClickListener()

                                       {
                                           @Override
                                           public void onClick(View v) {
                                               PopupMenu popup = new PopupMenu(UpdateProfileActivity.this, imageButton);
                                               //Inflating the Popup using xml file
                                               popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                                               //registering popup with OnMenuItemClickListener
                                               popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                   public boolean onMenuItemClick(MenuItem item) {
                                                       if (item.getTitle().equals("Open Camera")) {
                                                           final String path = Environment.getExternalStorageDirectory()
                                                                   + "/High Message/profilePic/";
                                                           File directory = new File(path);
                                                           if (!directory.exists())
                                                               directory.mkdir();
                                                           String strIPath = path + getFileName() + ".jpg";
                                                           Intent intent = new Intent(context, CustomVideoCamera.class);
                                                           Uri imageUri = Uri.fromFile(new File(strIPath));
                                                           intent.putExtra("filePath", strIPath);
                                                           intent.putExtra("isPhoto", true);
                                                           intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                                           startActivityForResult(intent, 1);
                                                       } else if (item.getTitle().equals("Open Gallery")) {
                                                           Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                                           startActivityForResult(intent, 2);
                                                       }
                                                       return true;
                                                   }
                                               });
                                               popup.show();//showing popup menu
                                           }
                                       }

        );//closing the setOnClickListener method


    }


    private void showprogress() {

                try {
                    Log.i("login123", "inside showProgressDialog");

                    progress = new ProgressDialog(context);
                    progress.setCancelable(false);
                    progress.setMessage("Loading...");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setProgress(0);
                    progress.setMax(100);
                    progress.show();
                } catch (Exception e) {
//                        SingleInstance.printLog(null, e.getMessage(), null, e);
                }

    }

    class DownloadVideo extends AsyncTask<String, String, String> {

        String downloadVideoUrl;

        public DownloadVideo(String url) {

            this.downloadVideoUrl = url;
            Log.d("task", "video uri is   " + downloadVideoUrl);
        }

        @Override
        protected void onPreExecute() {

            File imageFileName = new File(Environment.getExternalStorageDirectory()
                    + "/High Message/downloads/");

            if (!imageFileName.exists()) {
                imageFileName.mkdirs();
                showprogress();
            }
        }

        @Override
        protected String doInBackground(String... arg0) {
            String response = "";
            Log.i("download", "uri");
            if (downloadVideoUrl.contains("user")) {
                profile = downloadVideoUrl.split("user/")[1];
                Log.i("download", "profile" + profile);

            } else {
                profile = downloadVideoUrl.split("task/")[1];
                Log.i("download", "profile" + profile);
            }
            DownloadVideoFile(downloadVideoUrl, profile);
            Log.i("download", "profile" + profile);

            return response;
        }

        protected void onPostExecute(String result) {
//            PD.dismiss();
            Log.i("downloadVideo", "OnPostExcute");
            if (Appreference.taskMultimediaDownload != null && Appreference.taskMultimediaDownload.size() > 0) {
                Iterator iterator1 = Appreference.taskMultimediaDownload.entrySet()
                        .iterator();

                while (iterator1.hasNext()) {

                    File imageFile = new File(Environment.getExternalStorageDirectory()
                            + "/High Message/downloads/" + ImageName);
                    if (imageFile.exists()) {
                            progress.cancel();
                        File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + ImageName);
                        if (file1.exists()) {
                            Intent intent = new Intent(context, VideoPlayer.class);
                            intent.putExtra("video", file1.getAbsolutePath());
                            context.startActivity(intent);
                        }
                    }
                }
            }
        }
    }

    public void DownloadVideoFile(String fileURL, String fileName) {
        try {
            String RootDir = Environment.getExternalStorageDirectory()
                    .getAbsolutePath()
                    + "/High Message/downloads/";
            Log.d("task", "video fileName" + RootDir);

            File RootFile = new File(RootDir);
            RootFile.mkdir();
            // File root = Environment.getExternalStorageDirectory();
            URL u = new URL(fileURL);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();
            FileOutputStream f = new FileOutputStream(new File(RootFile,
                    fileName));
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1;

            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();


        } catch (Exception e) {

            Log.d("task....", e.toString());
        }
    }
    public void multimedia() {
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
        TextView camera = (TextView) dialog.findViewById(R.id.log_out);
        TextView cancel1 = (TextView) dialog.findViewById(R.id.cancel);
        gallery.setText("Gallery");
        camera.setText("Video");
        final String type = "video";
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog.dismiss();

                            /*if (type.equals("image")) {
                                if (Build.VERSION.SDK_INT < 19) {
                                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                    intent.setType("image");
                                    startActivityForResult(intent, 30);
                                } else {
                                    Log.i("img", "sdk is above 19");
                                    Log.i("clone", "====> inside gallery");
                                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                                    intent.setType("image");
                                    startActivityForResult(intent, 31);
                                }
                            } else*/
                    if (type.equals("video")) {
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
            public void onClick(View v) {
                try {
                    dialog.dismiss();
                            /*if (type.equals("image")) {
                                final String path = Environment.getExternalStorageDirectory()
                                        + "/High Message/";
                                File directory = new File(path);
                                if (!directory.exists())
                                    directory.mkdir();
                                strIPath = path + getFileName() + ".jpg";
                                Intent intent = new Intent(context, CustomVideoCamera.class);
                                Uri imageUri = Uri.fromFile(new File(strIPath));
                                intent.putExtra("filePath", strIPath);
                                intent.putExtra("isPhoto", true);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intent, 132);
                            } else*/
                    if (type.equals("video")) {
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/";
                        File directory = new File(path);
                        if (!directory.exists())
                            directory.mkdir();
                        strIPath = path + getFileName() + ".mp4";
                        Intent intent = new Intent(context, CustomVideoCamera.class);
                        intent.putExtra("filePath", strIPath);
                        intent.putExtra("isPhoto", false);
                        startActivityForResult(intent, 111);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        cancel1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                try {
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
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

    public ArrayList<File> getPdfList(File dir) {
        if (dir.exists()) {
            File files[] = Environment.getExternalStorageDirectory().listFiles();
            if (files != null) {
                for (File file : files) {

                    if (file.isDirectory()) {
                        getPdfList(file);
                    } else {
                        if (file.getName().endsWith(".pdf") && file.exists()) {
                            pdfList.add(file);
                            Log.i("task", "fileName" + file.getName());
                        }
                    }
                }
            }
        }
        Log.i("task", "No of video files" + pdfList.size());
        j = 0;
        return pdfList;
    }

    public ArrayList<File> getVideoList(File dir) {
        if (dir.exists()) {
            File files[] = dir.listFiles();
            if (files != null) {
                for (File file : files) {

                    if (file.isDirectory()) {
                        getVideoList(file);
                    } else {
                        if (file.getName().endsWith(".mp4") && file.exists()) {
                            videoList.add(file);
                            Log.i("task", "fileName" + file.getName());
                        }
                    }
                }
            }
        }
        k = 0;
        Log.i("task", "No of video files" + videoList.size());
        return videoList;
    }

    public void showToast(final String result) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(UpdateProfileActivity.this, result, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                try {
                    if ((requestCode == 1) && (resultCode == RESULT_OK) && (null != data)) {

//                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                        String partFilename = data.getStringExtra("filePath");
//                        Log.i("onActivityResult", "1a---->" + partFilename);
//                        storeCameraPhotoInSDCard(bitmap, partFilename);

                        String storeFilename = partFilename.substring(partFilename.lastIndexOf("/") + 1);
                        Log.i("before", "storeFilename" + storeFilename);
                        storeFilename = storeFilename.substring(0, storeFilename.length() - 4);
                        Log.i("after", "storeFilename" + storeFilename);
                        Log.i("onActivityResult", "1b---->" + partFilename);
//                        Log.i("storeFilename", "is " + storeFilename);
                        mBitmap = getImageFileFromSDCard(storeFilename);
                        Log.i("onActivityResult", "1c---->" + partFilename);
                        imgcontent = encodeTobase64(mBitmap);
//                        Log.i("Decode", "imgcontent######" + imgcontent);
//                        Log.i("image", "path" + mBitmap);
                        fileExtn = "jpg";
                        profileimage = storeFilename + "." + fileExtn;
                        imageView.setImageBitmap(mBitmap);
                        imagevalue = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            case 2:
                try {
                    if ((requestCode == 2) && (resultCode == RESULT_OK) && (null != data)) {

                        Uri selectedImage = data.getData();
                        /*String[] filePath = {MediaStore.Images.Media.DATA};
                        Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                        if (c != null) {
                            c.moveToFirst();
                        }
                        int columnIndex = c.getColumnIndex(filePath[0]);
                        String picturePath = c.getString(columnIndex);*/
                        String picturePath = getRealPathFromURI(selectedImage);
                        Log.i("onActivityResult", "2---->" + picturePath);
                        String imagename = picturePath.substring(picturePath.lastIndexOf("/") + 1);
                        Log.i("gallery", "imagename ---->" + imagename);
                        File selected_file = new File(picturePath);
                        int length = (int) selected_file.length() / 1048576;
                        if (length <= 2) {
                            Bitmap img;
                            img = convertpathToBitmap(picturePath);

                            if (img != null) {
                                img = Bitmap.createScaledBitmap(img, 100, 75, false);

                                final String path = Environment.getExternalStorageDirectory()
                                        + "/High Message/profilePic/" + imagename;

                                BufferedOutputStream stream;
                                Bitmap bitmap;
                                try {
                                    bitmap = convertpathToBitmap(picturePath);
                                    if (bitmap != null) {
                                        stream = new BufferedOutputStream(
                                                new FileOutputStream(new File(path)));
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                        Log.d("filePath  1 ", picturePath);
                                        picturePath = path;
                                        Log.d("filePath  2 ", picturePath);
                                    }
                                    if (bitmap != null) {
                                        bitmap = Bitmap.createScaledBitmap(
                                                bitmap, 200, 150, false);
                                    }
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }
                            profileimage = imagename;
                            if (imagename.length() > 4) {
                                fileExtn = imagename.substring(imagename.length() - 3);
                            }
//                        Log.i("Gallery", "is " + fileExtn);
//                    fileExtn = ".jpg";
//                    /storage/sdcard0/Pictures/Screenshots/Screenshot_2016-06-06-14-47-25.png
//                    String result = imgpath.substring(imgpath.lastIndexOf("/") + 1);
//                        c.close();
                            mBitmap = (BitmapFactory.decodeFile(picturePath));
                            imgcontent = encodeTobase64(mBitmap);
//                        Log.i("Decode", "imgcontent######" + imgcontent);
                            imageView.setImageBitmap(mBitmap);
                            imagevalue = true;
                        }
                    }
                } catch (Exception e) {
//                    Log.i("Gallery file", "not stored");
                    e.printStackTrace();
                }
            case 55:
                //&& data.getStringExtra("fileExt").equalsIgnoreCase("pdf")
                if (data != null && (resultCode == RESULT_OK)) {
                    try {


                        String fileName = data.getStringExtra("fileName");
                        strIPath = data.getStringExtra("filePath");
                        fileExtension = data.getStringExtra("fileExt");
                        if(fileExtension.equalsIgnoreCase("mp3") || fileExtension.equalsIgnoreCase("mp4") ||
                                fileExtension.equalsIgnoreCase("jpg")){
                            Toast.makeText(getApplicationContext(),"File format error",Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i("profile", "file Extension" + fileExtension);
                            textView9.setText(fileName);
                            fileContent = encodeFileToBase64Binary(strIPath);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 32:
                if (data != null && (resultCode == RESULT_OK)) {
                    Log.i("AAA", "New activity 32*************");
                    Uri selectedImageUri = data.getData();
                    strIPath = getRealPathFromURI(selectedImageUri);
                    final String path = Environment.getExternalStorageDirectory()
                            + "/High Message/" + getFileName() + ".mp4";

                    strIPath = path;
                    textView10.setText(path);
                    videoContent = encodeAudioVideoToBase64(strIPath);
                    videoExtension = strIPath.substring(strIPath.lastIndexOf(".") + 1, strIPath.length());
                    Log.i("profile", "video Extension" + videoExtension);
                }
                break;
            case 33:
                try {


                    if (data != null && (resultCode == RESULT_OK)) {
                        Log.i("AAA", "New activity 33*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + getFileName() + ".mp4";

                        Log.i("AAA", "New activity " + strIPath);
                        strIPath = path;
                        Log.i("AAA", "New activity " + strIPath);
/*
                        medialistadapter.notifyDataSetChanged();*/
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
                        fin.close();
                        fout.write(bytes);
                        fout.flush();
                        fout.close();
                        //                      Log.i("task","extension"+strIPath.split(".")[strIPath.split(".").length-1]);
                        textView10.setText(path);
                        videoContent = encodeAudioVideoToBase64(strIPath);
                        videoExtension = strIPath.substring(strIPath.lastIndexOf(".") + 1, strIPath.length());
                        Log.i("profile", "video Extension" + videoExtension);


//                        videofileWebService();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 111:
                textView10.setText(strIPath);
                videoContent = encodeAudioVideoToBase64(strIPath);
                videoExtension = strIPath.substring(strIPath.lastIndexOf(".") + 1, strIPath.length());
                Log.i("profile", "video Extension" + videoExtension);
                break;
            case 14:
                if (requestCode == 14 ) {
                    if(resultCode == Activity.RESULT_OK){
                        Bundle extras = data.getExtras();
                        Bitmap selectedBitmap = extras.getParcelable("data");
                        // Set The Bitmap Data To ImageView
                        imageView.setImageBitmap(selectedBitmap);
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                }
        }
    }


    private String encodeFileToBase64Binary(String fileName)
            throws IOException {

        File file = new File(fileName);
        byte[] bytes = loadFile(file);
        byte[] encoded = org.apache.commons.codec.binary.Base64.encodeBase64(bytes);

        return new String(encoded);
    }

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
        }
//        Log.i("FileUpload", "audioVideoEncode========" + strFile);
        return strFile;
    }


    private static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
//        if (length > Integer.MAX_VALUE) {
            // File is too large
//        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    private Bitmap getImageFileFromSDCard(String storeFilename) {
        Bitmap bitmap = null;
        File imageFile = new File(Environment.getExternalStorageDirectory() + "/High Message/profilePic/" + storeFilename + ".jpg");
        try {
            FileInputStream fis = new FileInputStream(imageFile);
            Log.i("imagefile", "is " + imageFile);
//            Log.i("filepath", "is " + fis);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void storeCameraPhotoInSDCard(Bitmap bitmap, String partFilename) {
        File outputFile = new File(Environment.getExternalStorageDirectory(), "/High Message/profilePic/" + partFilename + ".jpg");
        Log.i("outputfile", "is " + outputFile);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            Log.i("fileoutputstream", "is" + fileOutputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String encodeTobase64(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public String getFileName() {
        String strFilename = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
            Date date = new Date();
            strFilename = dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strFilename;
    }

    @Override
    public void ResponceMethod(final Object object) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.i("profile", "response method executing");
                CommunicationBean bean1 = (CommunicationBean) object;
                String s1 = ((CommunicationBean) object).getEmail();
                Gson g1 = new Gson();

                Appreference.loginuserdetails = g1.fromJson(s1, Loginuserdetails.class);
                try {
                    String firstname = Appreference.loginuserdetails.getFirstName();
                    String lastname = Appreference.loginuserdetails.getLastName();
                    String title = Appreference.loginuserdetails.getTitle();
                    String gender = Appreference.loginuserdetails.getGender();
                    String imagename = Appreference.loginuserdetails.getProfileImage();

                    bean1.setFirstname(firstname);
                    bean1.setLastname(lastname);
                    bean1.setTitle(title);
                    bean1.setGender(gender);


                    Log.i("Res", "profileimage " + profileimage);
                    bean1.setOldprofileimage(profileimage);
                    bean1.setProfileimage(imagename);
                    Log.i("ResFileName", " " + imagename);
                    Appreference.communicationBean = bean1;
                    if (profileimage != null && !profileimage.equalsIgnoreCase("") && !profileimage.equalsIgnoreCase(null)) {
                        renameFile(profileimage, imagename);
                    }
                    SettingsFragment.fragment.updateresponse(bean1);
                    cancelDialog();
                    finish();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void ErrorMethod(Object object) {

    }

    @Override
    protected void onResume() {
        super.onResume();
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
            String value = cursor.getString(column_index);
            cursor.close();
            return value;
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            Log.e("profile", "====> " + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }

    public Bitmap convertpathToBitmap(String strIPath) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();

        return BitmapFactory.decodeFile((compressImage(strIPath)), bmOptions);

    }

    public String compressImage(String imageUri) {

        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(imageUri, options);

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
            bmp = BitmapFactory.decodeFile(imageUri, options);
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
            exif = new ExifInterface(imageUri);

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

        FileOutputStream out;
        try {
            out = new FileOutputStream(imageUri);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return imageUri;

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
    private void performCrop(String partFilename) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(partFilename);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, 14);
        }
        // respond to users whose devices do not                    the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}