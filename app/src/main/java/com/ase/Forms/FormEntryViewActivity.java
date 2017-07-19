package com.ase.Forms;

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
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ase.Appreference;
import com.ase.AudioRecorder;
import com.ase.Audioplayer;
import com.ase.Bean.TaskDetailsBean;
import com.ase.CustomVideoCamera;
import com.ase.DB.VideoCallDataBase;
import com.ase.FullScreenImage;
import com.ase.HandSketchActivity2;
import com.ase.LocationFind;
import com.ase.NewTaskConversation;
import com.ase.R;
import com.ase.RandomNumber.Utility;
import com.ase.VideoPlayer;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.app.MainActivity;
import org.pjsip.pjsua2.app.MyBuddy;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.WebServiceInterface;

/**
 * Created by saravanakumar on 12/19/2016.
 */
public class FormEntryViewActivity extends Activity implements WebServiceInterface {

    LinearLayout fieldContainer, buttomlinear;
    Context context;
    TextView send, back, heading;
    String formId, isTemplate;
    ArrayList<com.ase.Forms.List> listofFiledses;
    ProgressDialog progress;
    ArrayList<String> stringArrayList;
    static boolean isEditable;

    static String strTime = null;
    static String st_hour;
    static String strenddate;
    static String taskId, taskNo, taskName;

    private int position = 0;
    private String strIPath = null;
    public String filedStrId;
    private String type = null;
    Handler handler = new Handler();
    String entryMode, setId, entryWay;
    private String quotes = "\"";

    ImageView forword, backword;
    TextView tv_noData;

    Button bt_save, bt_delete;

    private HashMap<String, String> multimediaFieldValues = new HashMap<String, String>();
    HashMap<String, ArrayList<com.ase.Forms.List>> formFieldsBeenMap = new HashMap<String, ArrayList<com.ase.Forms.List>>();
    ArrayList<String> setIdValueList = new ArrayList<String>();

    ArrayList<String> setIdList;

    static FormEntryViewActivity formEntryViewActivity;

    int fwdIncrement = 0;
    String accesstype, touserid;
    boolean edited = false,checked = false;
    TaskDetailsBean taskDetailsBean;


    public static FormEntryViewActivity getInstance() {
        return formEntryViewActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_entry_view_activity);

        try {
            fieldContainer = (LinearLayout) findViewById(R.id.fieldContainer);
            buttomlinear = (LinearLayout) findViewById(R.id.buttomlinear);
            forword = (ImageView) findViewById(R.id.forword);
            heading = (TextView) findViewById(R.id.heading);
            backword = (ImageView) findViewById(R.id.backword);
            bt_save = (Button) findViewById(R.id.bt_save);
            bt_delete = (Button) findViewById(R.id.bt_delete);


            formEntryViewActivity = this;
            Appreference.context_table.put("formsEntryView", this);

            context = this;

            send = (TextView) findViewById(R.id.send);
            back = (TextView) findViewById(R.id.back);

            tv_noData = (TextView) findViewById(R.id.NoData);
            Log.i("FormEntryViewActivity", " 0 ");
            listofFiledses = new ArrayList<com.ase.Forms.List>();
//        listofFiledses = (ArrayList<FormFieldsBean>) getIntent().getExtras().getSerializable("LeftList");
//        formFieldsBeenMap = (HashMap<String, ArrayList<com.myapplication3.Forms.List>>) getIntent().getExtras().getSerializable("FormsMapValue");
//        setIdValueList = getIntent().getStringArrayListExtra("FormsListValue");
            if (getIntent() != null) {
                isTemplate = getIntent().getExtras().getString("isTemplate");
            }
            Log.i("TemplateList", "template_form.setOnClickListener 212 " + isTemplate);
            formId = getIntent().getExtras().getString("FormsId");
            Log.i("FormEntryView", "get form Id == " + formId);
            taskId = getIntent().getExtras().getString("TaskId");
            taskNo = getIntent().getExtras().getString("TaskNo");
            taskName = getIntent().getExtras().getString("TaskName");
            entryMode = getIntent().getExtras().getString("EntryMode");
            stringArrayList = getIntent().getStringArrayListExtra("UserList");
            taskDetailsBean = (TaskDetailsBean) getIntent().getExtras().getSerializable("TaskBean");

            setId = getIntent().getExtras().getString("setId");
            entryWay = getIntent().getExtras().getString("EntryWay");

            if (isTemplate != null && isTemplate.equalsIgnoreCase("Yes")) {
                send.setVisibility(View.GONE);
                heading.setText("Form Fields");
            }
            Log.i("FormEntryView", "onCreate == " + formId);
            accesstype = VideoCallDataBase.getDB(context).getAccessType(taskId, formId, Appreference.loginuserdetails.getUsername());
            if (accesstype != null) {
                Log.i("form", "accesstype client");
                Log.i("FormEntryViewActivity", " 1 ");
                touserid = String.valueOf(Appreference.loginuserdetails.getId());
                callFormsListWebSerices();
            } else {
                Log.i("FormEntryViewActivity", " 2 ");
                Log.i("form", "accesstype server");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            showprogress();
                            Log.i("FormEntryViewActivity", " showprogress 1 ");
                            List<NameValuePair> tagNameValuePairs = new ArrayList<NameValuePair>();
                            tagNameValuePairs.add(new BasicNameValuePair("formId", formId));
                            tagNameValuePairs.add(new BasicNameValuePair("taskId", taskId));
                            Log.i("FormEntryViewActivity", " 3 ");
                            Log.i("FormEntryView", "call w/s form Id  1== " + formId);

                            Appreference.jsonRequestSender.getFormFieldAndValues(EnumJsonWebservicename.getFormAccessForTaskId, tagNameValuePairs, FormEntryViewActivity.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    //            callFormsListWebSerices();
            }
            Log.i("form", "taskid 1" + taskId);
            Log.i("form", "formid 1" + formId);
            Log.i("form", "membername 1" + Appreference.loginuserdetails.getFirstName());

            Log.i("FormEntryViewActivity", " 4 ");
            Log.i("form", "access 4" + accesstype);
            Log.i("form", "access 4" + edited);
            if (entryMode != null && entryMode.equalsIgnoreCase("single")) {
                buttomlinear.setVisibility(View.GONE);
                tv_noData.setVisibility(View.GONE);
                send.setBackground(null);
                send.setText("send");
                Log.i("FormEntryViewActivity", " 5 ");
            } else {
                tv_noData.setVisibility(View.GONE);
                buttomlinear.setVisibility(View.GONE);
                Log.i("FormEntryViewActivity", " 6 ");
            }


            forword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ++fwdIncrement;

                    Log.d("Forword", "list size is == " + setIdValueList.size() + " position is " + fwdIncrement);
                    if (setIdValueList.size() == fwdIncrement + 1) {
                        forword.setVisibility(View.INVISIBLE);
                    }

                    if (fwdIncrement > 0) {
                        backword.setVisibility(View.VISIBLE);
                    } else if (fwdIncrement == 0) {
                        backword.setVisibility(View.INVISIBLE);
                    }
                    loadProfileFieldValue(setIdValueList.get(fwdIncrement));
                    Log.i("FormEntryViewActivity", " 7 ");
                }
            });

            backword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    --fwdIncrement;

                    Log.d("Forword", "list size is == " + setIdValueList.size() + " position is " + fwdIncrement);
                    if (fwdIncrement > 0) {
                        backword.setVisibility(View.VISIBLE);
                    } else if (fwdIncrement == 0) {
                        backword.setVisibility(View.INVISIBLE);
                    }

    //                if (setIdValueList.size() > 1 && fwdIncrement == 0)
                    {
                        forword.setVisibility(View.VISIBLE);
                    }
                    loadProfileFieldValue(setIdValueList.get(fwdIncrement));
                    Log.i("FormEntryViewActivity", " 8 ");
                }
            });


            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Log.i("FormEntryViewActivity", " 9 ");
                }
            });

            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
    //                saveTextValue();
                    if (entryMode.equalsIgnoreCase("single")) {
                        showprogress();
                        Log.i("FormEntryViewActivity", " showprogress 2 ");
                        Log.i("FormEntryViewActivity", " 10 ");
                        saveTextValue();
                    } else {
                        showprogress();
                        Log.i("FormEntryViewActivity", " showprogress 3 ");
                        List<NameValuePair> tagNameValuePairs = new ArrayList<NameValuePair>();
                        tagNameValuePairs.add(new BasicNameValuePair("formId", formId));
                        Log.i("FormEntryViewActivity", " 11 ");
                        Appreference.jsonRequestSender.getFormFieldsForForm(EnumJsonWebservicename.getFormFieldsForForm, tagNameValuePairs, FormEntryViewActivity.this);
                    }


                }
            });

            bt_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!entryMode.equalsIgnoreCase("single")) {
                        Log.i("FormEntryViewActivity", " 12 ");
                        if(!isEditable){
                            showToast("Please Update any Fields ");
                        }else {
                            saveTextValue();
                        }
                    }
                }
            });

            bt_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if (setId != null) {
                            JSONObject jsonObject = new JSONObject();

    //            JSONObject jsonObject1 = new JSONObject();

                            JSONObject taskIdJson = new JSONObject();
                            taskIdJson.put("id", taskId);

                            JSONObject formIdJson = new JSONObject();
                            formIdJson.put("id", formId);
    //            jsonObject1.put("id", taskId);
                            jsonObject.put("form", formIdJson);

                            if (setId != null) {
                                jsonObject.put("setId", setId);
                            }
                            jsonObject.put("task", taskIdJson);

                            Log.i("FormEntryViewActivity", " 13 ");
                            Appreference.jsonRequestSender.setFormFieldValuesForForm(EnumJsonWebservicename.deleteFieldValue, jsonObject, FormEntryViewActivity.this);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && fieldContainer != null) {

            Log.i("FormEntryViewActivity", " 14 ");
            try {
                LinearLayout parent_view1 = (LinearLayout) fieldContainer
                        .getChildAt(position - 1);

                final TextView value_img;

                Log.i("CustomTag", "parentid" + parent_view1);

                value_img = (TextView) parent_view1.findViewById(R.id.fieldText);


                if (requestCode == 30) {
                    Log.i("FormEntryViewActivity", " 15 ");
                    if (data != null) {
                        Log.i("FormEntryViewActivity", " 16 ");
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
                                        + "/High Message/" + Appreference.getFileName() + ".jpg";

                                BufferedOutputStream stream;
                                Bitmap bitmap = null;
                                try {
                                    bitmap = convertpathToBitmap(strIPath);
                                    if (bitmap != null) {
                                        stream = new BufferedOutputStream(
                                                new FileOutputStream(new File(path)));
                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                        Log.d("filePath  1 ", strIPath);
                                        strIPath = path;
                                        Log.d("filePath  2 ", strIPath);
                                    }
                                    if (bitmap != null) {
                                        bitmap = Bitmap.createScaledBitmap(
                                                bitmap, 200, 150, false);
                                    }

                                    multimediaFieldValues.put(String.valueOf(position), strIPath);
                                    String fileName = strIPath.split("/")[5];
                                    Log.d("fileName  1 ", "strIPath   " + fileName);
                                    value_img.setVisibility(View.VISIBLE);
                                    value_img.setText(fileName);
//                                refresh();

                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                } else if (requestCode == 333) {
                    Log.i("FormEntryViewActivity", " 17 ");
                    if (data != null) {
                        Log.i("FormEntryViewActivity", " 18 ");
                        Log.d("filePath", data.getStringExtra("taskFileExt"));
                        strIPath = data.getStringExtra("taskFileExt");
                        Log.i("task", "extension");

                        multimediaFieldValues.put(String.valueOf(position), strIPath);
                        String fileName = strIPath.split("/")[5];
                        Log.d("fileName  1 ", "strIPath   " + fileName);

                        value_img.setVisibility(View.VISIBLE);
                        value_img.setText(fileName);
                    }

                } else if (requestCode == 32) {
                    if (data != null) {
                        Log.i("FormEntryViewActivity", " 19 ");
                        Log.i("AAA", "New activity 32*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + Appreference.getFileName() + ".mp4";

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

                                Log.e("update", "value" + path);
                                multimediaFieldValues.put(String.valueOf(position), strIPath);
                                String fileName = strIPath.split("/")[5];
                                Log.d("fileName  1 ", "strIPath   " + fileName);

                                value_img.setVisibility(View.VISIBLE);
                                value_img.setText(fileName);

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (requestCode == 111) {
                    Log.i("FormEntryViewActivity", " 20 ");
                    strIPath = data.getStringExtra("filePath");
                    Log.i("Avideo", "New activity ************* : " + strIPath);
                    File new_file = new File(strIPath);
                    if (new_file.exists()) {
                        Log.i("mp", "mpath" + strIPath);
                        multimediaFieldValues.put(String.valueOf(position), strIPath);
                        String fileName = strIPath.split("/")[5];
                        Log.d("fileName  1 ", "strIPath   " + fileName);

                        value_img.setVisibility(View.VISIBLE);
                        value_img.setText(fileName);
                    }
                } else if (requestCode == 33) {
                    if (data != null) {
                        try {
                            Log.i("FormEntryViewActivity", " 21 ");
                            Log.i("AAA", "New activity 33*************");
                            Uri selectedImageUri = data.getData();
                            strIPath = getRealPathFromURI(selectedImageUri);
                            final String path = Environment.getExternalStorageDirectory()
                                    + "/High Message/" + Appreference.getFileName() + ".mp4";

                            Log.i("AAA", "New activity " + strIPath);
                            strIPath = path;
                            Log.i("AAA", "New activity " + strIPath);


                            Log.i("mp", "mpath" + strIPath);
                            multimediaFieldValues.put(String.valueOf(position), strIPath);

                            String fileName = strIPath.split("/")[5];
                            Log.d("fileName  1 ", "strIPath   " + fileName);

                            value_img.setVisibility(View.VISIBLE);
                            value_img.setText(fileName);
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
                } else if (requestCode == 31) {
                    if (data != null) {
                        Log.i("FormEntryViewActivity", " 22 ");
                        Uri selectedImageUri = data.getData();
                        Log.d("filePath  1 ", selectedImageUri.toString());
                        final int takeFlags = data.getFlags()
                                & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        getContentResolver().takePersistableUriPermission(
                                selectedImageUri, takeFlags);
                        strIPath = Environment.getExternalStorageDirectory()
                                + "/High Message/"
                                + Appreference.getFileName() + ".jpg";
                        Log.d("filePath  1 ", "strIPath   " + strIPath);

                        File selected_file = new File(strIPath);

                        Log.d("filePath  1 ", "selected_file" +
                                "   " + selected_file.toString());

                        int length = (int) selected_file.length() / 1048576;
                        Log.d("task", "........ size is------------->" + strIPath);
                        Log.d("task", "........ size is------------->" + selected_file);
                        Log.d("task", "........ size is------------->" + length);
                        Log.d("task", "........ size is------------->" + selectedImageUri);
                        Log.d("task", "........ size is------------->" + length);
                        if (length <= 2) {
                            new bitmaploader().execute(selectedImageUri);
                            Log.d("task", "........ size is------------->" + selectedImageUri);
                        } else {
//                        showToast("Kindly Select someother image,this image is too large");
                            Toast.makeText(getApplicationContext(), "Kindly Select someother image,this image is too large", Toast.LENGTH_SHORT).show();
                        }

                        multimediaFieldValues.put(String.valueOf(position), strIPath);

                        String fileName = strIPath.split("/")[5];
                        Log.d("fileName  1 ", "strIPath   " + fileName);

                        value_img.setVisibility(View.VISIBLE);
                        value_img.setText(fileName);

                    }

                } else if (requestCode == 423) {
                    Log.i("FormEntryViewActivity", " 23 ");
                    strIPath = data.getStringExtra("path");
                    Log.i("Task", "path" + strIPath);
                    File new_file = new File(strIPath);
                    if (new_file.exists()) {

                        Log.i("AAAA", "onactivity result $$$$$$$$$$$$$$$" + strIPath);
                        multimediaFieldValues.put(String.valueOf(position), strIPath);

                    }
                    value_img.setVisibility(View.VISIBLE);
                    String fileName = strIPath.split("/High Message/")[1];
                    Log.i("Task", "path" + fileName);
                    value_img.setText(fileName);
                    Log.i("task", "extension");

//                    fileWebService("image");
                } else if (requestCode == 32) {
                    if (data != null) {
                        Log.i("FormEntryViewActivity", " 24 ");
                        Log.i("AAA", "New activity 32*************");
                        Uri selectedImageUri = data.getData();
                        strIPath = getRealPathFromURI(selectedImageUri);
                        final String path = Environment.getExternalStorageDirectory()
                                + "/High Message/" + Appreference.getFileName() + ".mp4";

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
                            fout = null;
                            Log.i("AAA", "New activity " + strIPath);
                            Log.i("task", "video loaded");

                            multimediaFieldValues.put(String.valueOf(position), strIPath);
                            String fileName = strIPath.split("/")[5];
                            Log.d("fileName  1 ", "strIPath   " + fileName);
//                        medialistadapter.notifyDataSetChanged();
                            // Log.i("task","extension"+strIPath.split(".")[strIPath.split(".").length-1]);
//                        PercentageWebService("video", strIPath, "mp4");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                        videofileWebService();
                    }
                } else if (requestCode == 132) {
                    File new_file = new File(strIPath);
                    if (new_file.exists()) {
                        Log.i("FormEntryViewActivity", " 25 ");
//                       Log.i("task","extension"+strIPath.split(".")[strIPath.split(".").length-1]);
                        multimediaFieldValues.put(String.valueOf(position), strIPath);
                        String fileName = strIPath.split("/")[5];
                        value_img.setVisibility(View.VISIBLE);
                        value_img.setText(fileName);
                        Log.d("fileName  1 ", "strIPath   " + fileName);
//                        fileWebService("image");
                    }
                } else if (requestCode == 222) {
                    Log.i("FormEntryViewActivity", " 26 ");
                    Log.d("FormEntry latitude", data.getStringExtra("loc_latitude"));
                    String location_point = data.getStringExtra("loc_latitude");
                    Log.i("FormEntry location", "loc_latitude " + location_point);
                    if (location_point != null) {
                        Log.d("FormEntry Location", "location_point   " + location_point);
                        value_img.setVisibility(View.VISIBLE);
                        value_img.setText(location_point);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("FormEntryViewActivity", " 27 ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("FormEntryViewActivity", " 28 ");
    }


    public void loadProfileFieldValue(String values) {

        if (setIdValueList != null || setIdValueList.size() != 0) {
            Log.i("FormEntryViewActivity", " 29 ");
            fieldContainer.removeAllViews();

            setId = values;
            Log.i("loadMethod", "value is  == " + values + "  load ui  == " + formFieldsBeenMap.get(values));


            ArrayList<com.ase.Forms.List> listValues = formFieldsBeenMap.get(values);
           /* if(listofFiledses != null){
                listofFiledses.clear();
            }*/
            listofFiledses = listValues;


            int fied = 0;
            int index = 0;
            for (com.ase.Forms.List fieldtemplate : listValues) {
                Log.i("FormEntryViewActivity", " 30 ");
                Log.i("form", "createdby " + fieldtemplate.getCreatedBy());
                if (accesstype != null && accesstype.equalsIgnoreCase("Edit My Values")) {
                    if (fieldtemplate.getCreatedBy() == Appreference.loginuserdetails.getId()) {
                        Log.i("FormEntryViewActivity", " 1111+ ");
                        fieldtemplate.setClientId(String.valueOf(++index));
                        Log.i("form", "access s " + edited);
                        Log.i("FormEntryViewActivity", " 1111 + " + fieldtemplate.getClientId());
                        inflateFieldsExist(fied, fieldtemplate);
                        Log.d("My Profile",
                                "Field ID : "
                                        + ", Field Name : " + fieldtemplate.getClientId()
                                        + fieldtemplate.getFieldName() + ", Field TagName : "
                                        + fieldtemplate.getFieldName() + ", Type :"
                                        + fieldtemplate.getFieldType() + " required  ");
                        fied++;
                    } else {

                    }
                } else {
                    Log.i("FormEntryViewActivity", " 31 ");
                    fieldtemplate.setClientId(String.valueOf(++index));
                    Log.i("FormEntryViewActivity", " 1111 ++++ " + fieldtemplate.getClientId());
                    Log.i("form", "access s " + edited);
                    inflateFieldsExist(fied, fieldtemplate);
                    Log.d("My Profile",
                            "Field ID : "
                                    + ", Field Name : " + fieldtemplate.getClientId()
                                    + fieldtemplate.getFieldName() + ", Field TagName : "
                                    + fieldtemplate.getFieldName() + ", Type :"
                                    + fieldtemplate.getFieldType() + " required  ");
                    fied++;
                }

            }
            if (fied == 0 && accesstype.equalsIgnoreCase("Edit My Values")) {
                bt_save.setVisibility(View.GONE);
                bt_delete.setVisibility(View.GONE);
                forword.setVisibility(View.GONE);
                backword.setVisibility(View.GONE);
                tv_noData.setVisibility(View.VISIBLE);
                tv_noData.setText("You should not enter any values");
                Log.i("FormEntryViewActivity", " 32 ");
            }
        }
    }


    public void inflateFieldsExist(int mode, final com.ase.Forms.List bean) {
        try {
            View view = getLayoutInflater().inflate(
                    R.layout.custom_tag_adapter, fieldContainer, false);
            view.setTag(bean.getClientId());
            final TextView fieldName = (TextView) view.findViewById(R.id.fieldname);
            if (bean.getFieldName() != null) {
                fieldName.setText(bean.getFieldName());
            }
            Log.i("FormEntryViewActivity", " 33 ");
            Log.i("FormEntryViewActivity", " inflateFieldsExist 1 ");
            Log.d("My Profile",
                    "Field ID : " + bean.getClientId()
                            + ", Field Name : "
                            + bean.getFieldName() + ", Field value : "
                            + bean.getFieldValue() + ", Type :"
                            + bean.getFieldType() + " required  " + bean.getIsInputRequired());


            if (bean.getFieldType().equalsIgnoreCase("photo") || bean.getFieldType().equalsIgnoreCase("image") || bean.getFieldType().equalsIgnoreCase("signature") || bean.getFieldType().equalsIgnoreCase("location")) {
                ImageView options = (ImageView) view.findViewById(R.id.imageButton);
                ImageView options_1 = (ImageView) view.findViewById(R.id.locateButton);
                final TextView fileName = (TextView) view.findViewById(R.id.fieldText);
                if (isTemplate != null && isTemplate.equalsIgnoreCase("Yes")) {
                    options.setVisibility(View.GONE);
                    options_1.setVisibility(View.GONE);
                    fileName.setVisibility(View.GONE);
                }
                Log.i("FormEntryViewActivity", " 34 ");
                Log.i("FormEntryViewActivity", " inflateFieldsExist 2 ");
                if (bean.getFieldType().equalsIgnoreCase("location")) {
                    Log.i("FormEntryViewActivity", " 35 ");
                    position = Integer.parseInt(bean.getClientId());
                    if (isTemplate != null && isTemplate.equalsIgnoreCase("Yes")) {
                        options.setVisibility(View.GONE);
                        options_1.setVisibility(View.GONE);
                        fileName.setVisibility(View.GONE);
                    } else {
                        fileName.setVisibility(View.VISIBLE);
                    }
                    if (edited == false) {
                        if (isTemplate != null && isTemplate.equalsIgnoreCase("Yes")) {
                            options_1.setVisibility(View.GONE);
                        } else {
                            options_1.setVisibility(View.VISIBLE);
                        }
                    }
//                        options_1.setVisibility(View.VISIBLE);
                    options.setVisibility(View.GONE);

                } else {
                    Log.i("FormEntryViewActivity", " 36 ");
                    if (isTemplate != null && isTemplate.equalsIgnoreCase("Yes")) {
                        fileName.setVisibility(View.GONE);
                    } else {
                        fileName.setVisibility(View.VISIBLE);
                    }
//                    fileName.setVisibility(View.VISIBLE);
                    if (edited == false) {
                        if (isTemplate != null && isTemplate.equalsIgnoreCase("Yes")) {
                            options.setVisibility(View.GONE);
                        } else {
                            options.setVisibility(View.VISIBLE);
                        }
//                        options.setVisibility(View.VISIBLE);
                    }
                }
//                fileName.setText("image");
                Log.i("FormEntryViewActivity", " inflateFieldsExist 3 ");
                if (bean.getFieldValue() != null && !bean.getFieldValue().equalsIgnoreCase("-")) {
                    Log.i("FormEntryViewActivity", " 37 ");
                    fileName.setText(bean.getFieldValue());
                    multimediaFieldValues.put(bean.getClientId(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + bean.getFieldValue());
                }

                fileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isEditable = true;
                        String ImageName = fileName.getText().toString();
                        Log.i("Task", "imagename" + ImageName);
                        File file = null;
                        if (bean.getFieldType().equalsIgnoreCase("location")) {
                            try {
                                Log.i("FormEntryViewActivity", " 38 ");
                                Bundle bundle = new Bundle();
                                bundle.putString("map", ImageName);
                                bundle.putString("viewmap", "location");
                                Intent intent = new Intent(context, LocationFind.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (bean.getFieldType().equalsIgnoreCase("photo") || bean.getFieldType().equalsIgnoreCase("signature")) {
                            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                        } else {
                            file = new File(ImageName);
                        }
                        if (file.exists()) {
                            Intent intent = new Intent(context, FullScreenImage.class);
                            intent.putExtra("image", file.toString());
                            context.startActivity(intent);
                        } else {
                            File file1 = null;
                            if (bean.getFieldType().equalsIgnoreCase("photo") || bean.getFieldType().equalsIgnoreCase("signature")) {
                                file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + ImageName);
                            } else {
                                file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                            }
                            if (file1.exists()) {
                                Intent intent = new Intent(context, FullScreenImage.class);
                                intent.putExtra("image", file1.toString());
                                context.startActivity(intent);
                            }

                        }

                    }
                });
                options.setTag(fieldContainer.getChildCount());
                options.setContentDescription(bean.getFieldType());
               /* if(taskDetailsBean != null) {
                    fileName.setText(taskDetailsBean.getTaskDescription());
                    multimediaFieldValues.put(bean.getClientId(),taskDetailsBean.getTaskDescription());
                }*/
                options.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Log.i("FormEntryViewActivity", " 39 ");
                        isEditable = true;
                        if (bean.getFieldType().equalsIgnoreCase("signature")) {
                            position = Integer.parseInt(bean.getClientId());
                            Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                            startActivityForResult(i, 423);
                        } else {

                            int pos = (Integer) v.getTag();
                            Log.d("Profile", "On click of option menu");
                            showMultimediaOptions(pos, bean.getClientId(), v
                                    .getContentDescription().toString());
                        }
                        checked = true;
                    }
                });

                options_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("FormEntryViewActivity", " 40 ");
                        isEditable = true;
                        if (bean.getFieldType().equalsIgnoreCase("location")) {
                            try {
                                Intent intent = new Intent(context, LocationFind.class);
                                startActivityForResult(intent, 222);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        checked = true;
                    }
                });

//                options.setVisibility(View.VISIBLE);
            } else if (bean.getFieldType().equalsIgnoreCase("datetime") && bean.getIsInputRequired().equalsIgnoreCase("no")) {
                final Button add = (Button) view.findViewById(R.id.fieldButton);
                final TextView fileName = (TextView) view.findViewById(R.id.fieldText);
                fileName.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                if (isTemplate != null && isTemplate.equalsIgnoreCase("Yes")) {
                    fileName.setVisibility(View.GONE);
                    add.setVisibility(View.GONE);
                } else {
                    fileName.setVisibility(View.VISIBLE);
                    add.setVisibility(View.VISIBLE);
                }
                Log.i("FormEntryViewActivity", " 41 ");
                if (bean.getFieldValue() != null && !bean.getFieldValue().equalsIgnoreCase("-")) {
                    fileName.setText(bean.getFieldValue());
                    multimediaFieldValues.put(bean.getClientId(), bean.getFieldValue());
                }
               /* if(taskDetailsBean != null) {
                    fileName.setText(taskDetailsBean.getTaskDescription());
                    multimediaFieldValues.put(bean.getClientId(),taskDetailsBean.getTaskDescription());
                }*/

                if (edited == false) {
                    if (isTemplate != null && isTemplate.equalsIgnoreCase("Yes")) {
                        add.setVisibility(View.GONE);
                    } else {
                        add.setVisibility(View.VISIBLE);
                    }
                } else {
                    add.setVisibility(View.GONE);
                }


                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isEditable = true;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime);
                       /* if(bean.getFieldValue() != null){
                            fileName.setText(bean.getFieldValue());
                            multimediaFieldValues.put(bean.getClientId(),bean.getFieldValue());
                        }else*/
                        Log.i("FormEntryViewActivity", " 42 ");
                        {
                            fileName.setText(currentDateandTime);
                            multimediaFieldValues.put(bean.getClientId(), currentDateandTime);
                        }
                        add.setVisibility(View.INVISIBLE);
                        checked = true;

//                        multimediaFieldValues.put(bean.getClientId(), currentDateandTime);

                    }
                });
            } else if (bean.getFieldType().equalsIgnoreCase("numeric")) {
                final EditText fieldValue = (EditText) view
                        .findViewById(R.id.fieldvalue);
//                fieldValue.setVisibility(View.VISIBLE);
                if (isTemplate != null && isTemplate.equalsIgnoreCase("Yes")) {
                    fieldValue.setVisibility(View.GONE);
                } else {
                    fieldValue.setVisibility(View.VISIBLE);
                }
                fieldValue.setTag(bean.getFieldType());
                fieldValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                fieldValue.setContentDescription(bean.getClientId());
                fieldValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                Log.i("FormEntryViewActivity", " 43 ");
                if (bean.getFieldValue() != null && !bean.getFieldValue().equalsIgnoreCase("-")) {
                    fieldValue.setText(bean.getFieldValue());
                    multimediaFieldValues.put(bean.getClientId(), bean.getFieldValue());
                }

                if (edited == true) {
                    fieldValue.setEnabled(false);
                    fieldValue.setClickable(false);
                }

               /* if(taskDetailsBean != null) {
                    fieldValue.setText(taskDetailsBean.getTaskDescription());
                    multimediaFieldValues.put(bean.getClientId(),taskDetailsBean.getTaskDescription());


                }*/
                if (bean.getFieldType() != null) {
                        fieldValue.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // TODO Auto-generated method stub
                                isEditable = true;
                               return false;
                            }
                        });

                }
            } else if (bean.getFieldType().equalsIgnoreCase("text")) {

                final EditText fieldValue = (EditText) view
                        .findViewById(R.id.fieldvalue);
//                fieldValue.setVisibility(View.VISIBLE);
                if (isTemplate != null && isTemplate.equalsIgnoreCase("Yes")) {
                    fieldValue.setVisibility(View.GONE);
                } else {
                    fieldValue.setVisibility(View.VISIBLE);
                }
                fieldValue.setTag(bean.getFieldType());
                fieldValue.setContentDescription(bean.getClientId());
                fieldValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                Log.i("FormEntryViewActivity", " 44 ");
                if (bean.getFieldValue() != null && !bean.getFieldValue().equalsIgnoreCase("-")) {
                    fieldValue.setText(bean.getFieldValue());
                    multimediaFieldValues.put(bean.getClientId(), bean.getFieldValue());
                }

                if (edited == true) {
                    fieldValue.setEnabled(false);
                    fieldValue.setClickable(false);
                }
               /* if(taskDetailsBean != null) {
                    fieldValue.setText(taskDetailsBean.getTaskDescription());
                    multimediaFieldValues.put(bean.getClientId(),taskDetailsBean.getTaskDescription());
                }*/
                if (bean.getFieldType() != null) {

                        fieldValue.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // TODO Auto-generated method stub
                                isEditable = true;
                                /*if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                                    changeType(fieldValue);
                                    isEditable = true;
                                    return true;
                                } else {
                                    return false;
                                }*/
                                return  false;
                            }
                        });


                    if (bean.getFieldType().equalsIgnoreCase("number"))
                        fieldValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                    else if (bean.getFieldType().equalsIgnoreCase("email"))
                        fieldValue
                                .setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                }

            } else if (bean.getFieldType().equalsIgnoreCase("datetime") && bean.getIsInputRequired().equalsIgnoreCase("yes")) {

                final TextView date = (TextView) view.findViewById(R.id.fieldText);
//                date.setVisibility(View.VISIBLE);
                if (isTemplate != null && isTemplate.equalsIgnoreCase("Yes")) {
                    date.setVisibility(View.GONE);
                } else {
                    date.setVisibility(View.VISIBLE);
                }
                date.setText(bean.getFieldName());
//                date.setTag(fieldContainer.getChildCount());
//                date.setContentDescription(bean.getFieldType());
                /*if(taskDetailsBean != null) {
                    date.setText(taskDetailsBean.getTaskDescription());
                    multimediaFieldValues.put(bean.getClientId(),taskDetailsBean.getTaskDescription());
                }*/
                Log.i("FormEntryViewActivity", " 45 ");
                if (bean.getFieldValue() != null && !bean.getFieldValue().equalsIgnoreCase("-")) {
                    date.setText(bean.getFieldValue());
                    multimediaFieldValues.put(bean.getClientId(), bean.getFieldValue());
                }

                if (edited == false)
                    date.setClickable(true);
                else
                    date.setClickable(false);
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        int integer = (Integer) v.getTag();
                        isEditable = true;
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime + " clientId ==  " + bean.getClientId());
                        showCustomDatePicker(currentDateandTime, bean.getClientId());

                      /*  if(taskDetailsBean.getTaskDescription() != null){
                            date.setText(taskDetailsBean.getTaskDescription());
                            multimediaFieldValues.put(bean.getClientId(),taskDetailsBean.getTaskDescription());
                        }else*/
                        if (bean.getFieldValue() != null) {
                            date.setText(bean.getFieldValue());
                            multimediaFieldValues.put(bean.getClientId(), bean.getFieldValue());
                        } else {
                            date.setText(currentDateandTime);
                            multimediaFieldValues.put(bean.getClientId(), currentDateandTime);
                        }
                        checked = true;
                        Log.i("FormEntryViewActivity", " 46 ");
//                        multimediaFieldValues.put(bean.getClientId(), currentDateandTime);
//                        date.setText(strenddate);
                    }
                });
            } else if (bean.getFieldType().equalsIgnoreCase("audio")) {
                Log.i("FormEntryViewActivity", " 47 ");
                ImageView options = (ImageView) view.findViewById(R.id.imageButton);
                final TextView fileName = (TextView) view.findViewById(R.id.fieldText);
//                fileName.setVisibility(View.VISIBLE);
//                options.setVisibility(View.VISIBLE);
                if (isTemplate != null && isTemplate.equalsIgnoreCase("Yes")) {
                    fileName.setVisibility(View.GONE);
                    options.setVisibility(View.GONE);
                } else {
                    fileName.setVisibility(View.VISIBLE);
                    options.setVisibility(View.VISIBLE);
                }
                options.setBackground(getResources().getDrawable(R.drawable.ic_audio_file_100));
//                fileName.setText("image");
                options.setTag(fieldContainer.getChildCount());
                options.setContentDescription(bean.getFieldType());
//                fieldValuesMap.put(bean.getName(), bean);
                if (bean.getFieldValue() != null && !bean.getFieldValue().equalsIgnoreCase("-")) {
                    fileName.setText(bean.getFieldValue());
                    multimediaFieldValues.put(bean.getClientId(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + bean.getFieldValue());
                }
                fileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isEditable = true;
                        String ImageName = fileName.getText().toString();
                        Log.i("Task", "imagename" + ImageName);
                        File file = null;
                        Intent intent = new Intent(context, Audioplayer.class);
                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                        if (file.exists()) {
                            intent.putExtra("audio", file.toString());
                            context.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + ImageName);
                            if (file1.exists()) {
                                intent.putExtra("audio", file1.toString());
                                context.startActivity(intent);
                            }

                        }

                    }
                });
                options.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        isEditable = true;
                        int pos = (Integer) v.getTag();
                        Log.d("Profile", "On click of option menu");
                        try {
                            position = Integer.parseInt(bean.getClientId());
                            Intent i = new Intent(FormEntryViewActivity.this, AudioRecorder.class);
                            i.putExtra("task", "audio");
                            startActivityForResult(i, 333);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        checked = true;

                    }
                });

               /* fileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ImageName = fileName.getText().toString();
                        Log.i("Task","imagename"+ImageName);
                        File file = null;
                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                        if(file.exists()){
                            Intent intent = new Intent(context, FullScreenImage.class);
                            intent.putExtra("image", file.toString());
                            context.startActivity(intent);
                        }else{
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads" + ImageName);
                            if (file1.exists()) {
                                Intent intent = new Intent(context, FullScreenImage.class);
                                intent.putExtra("image", file1.toString());
                                context.startActivity(intent);
                            }
                        }
                    }
                });*/
                if (edited == false) {
                    if (isTemplate != null && isTemplate.equalsIgnoreCase("Yes")) {
                        options.setVisibility(View.GONE);
                    } else {
                        options.setVisibility(View.VISIBLE);
                    }
//                    options.setVisibility(View.VISIBLE);
                } else {
                    options.setVisibility(View.GONE);
                }
            } else if (bean.getFieldType().equalsIgnoreCase("video")) {
                Log.i("FormEntryViewActivity", " 48 ");
                ImageView options = (ImageView) view.findViewById(R.id.imageButton);
                final TextView fileName = (TextView) view.findViewById(R.id.fieldText);
//                fileName.setVisibility(View.VISIBLE);
//                options.setVisibility(View.VISIBLE);
                if (isTemplate != null && isTemplate.equalsIgnoreCase("Yes")) {
                    options.setVisibility(View.GONE);
                    fileName.setVisibility(View.GONE);
                } else {
                    fileName.setVisibility(View.VISIBLE);
                    options.setVisibility(View.VISIBLE);
                }
//                fileName.setText("image");
                options.setTag(fieldContainer.getChildCount());
                options.setContentDescription(bean.getFieldType());
                if (bean.getFieldValue() != null && !bean.getFieldValue().equalsIgnoreCase("-")) {
                    fileName.setText(bean.getFieldValue());
                    multimediaFieldValues.put(bean.getClientId(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + bean.getFieldValue());
                }
//                fieldValuesMap.put(bean.getName(), bean);
                options.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        isEditable = true;
                        int pos = (Integer) v.getTag();
                        Log.d("Profile", "On click of option menu");
                        showMultimediaOptions(pos, bean.getClientId(), v
                                .getContentDescription().toString());
                        checked = true;
                    }
                });

                fileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isEditable = true;
                        String ImageName = fileName.getText().toString();
                        Log.i("Task", "imagename" + ImageName);
                        File file = null;

                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                        if (file.exists()) {
                            Log.i("Task", "imagename  folder == " + ImageName);
                            Intent intent = new Intent(context, VideoPlayer.class);
                            intent.putExtra("video", file.getAbsolutePath());
                            context.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + ImageName);
                            if (file1.exists()) {
                                Log.i("Task", "imagename downloads folder == " + ImageName);
                                Intent intent = new Intent(context, VideoPlayer.class);
                                intent.putExtra("video", file1.getAbsolutePath());
                                context.startActivity(intent);
                            }
                        }
                    }
                });


                if (edited == false) {
                    if (isTemplate != null && isTemplate.equalsIgnoreCase("Yes")) {
                        options.setVisibility(View.GONE);
                    } else {
                        options.setVisibility(View.VISIBLE);
                    }
//                    options.setVisibility(View.VISIBLE);
                } else {
                    options.setVisibility(View.GONE);
                }
            } else if (bean.getFieldType().equalsIgnoreCase("switch")) {
                Log.i("FormEntryViewActivity", " inflateFieldsExist 5 ");
                final Switch fieldValue = (Switch) view
                        .findViewById(R.id.fieldSwitch);
                fieldValue.setVisibility(View.VISIBLE);
                if (isTemplate != null && isTemplate.equalsIgnoreCase("Yes")) {
                    fieldValue.setVisibility(View.GONE);
                } else {
                    fieldValue.setVisibility(View.VISIBLE);
                }

                fieldValue.setTag(bean.getFieldType());
                fieldValue.setContentDescription(bean.getClientId());
                fieldValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                Log.i("FormEntryViewActivity", " inflateFieldsExist 6 " + bean.getFieldValue() +  "  client id  "+bean.getClientId());
                if (bean.getFieldValue() != null && !bean.getFieldValue().equalsIgnoreCase("-")) {
                    fieldValue.setText(bean.getFieldValue());
                    if (bean.getFieldValue() != null && bean.getFieldValue().equalsIgnoreCase("Yes")) {
                        fieldValue.setChecked(true);
                        Log.i("FormEntryViewActivity", " inflateFieldsExist 6  if " + bean.getFieldValue() +  "  client id  "+bean.getClientId());
                        multimediaFieldValues.put(bean.getClientId(), "Yes");
                    } else {
                        fieldValue.setChecked(false);
                        Log.i("FormEntryViewActivity", " inflateFieldsExist 6 else " + bean.getFieldValue() +  "  client id  "+bean.getClientId());
                        multimediaFieldValues.put(bean.getClientId(), "No");
                    }
//                    multimediaFieldValues.put(bean.getClientId(), bean.getFieldValue());
                }
//                if (taskDetailsBean != null) {
//                    fieldValue.setText(taskDetailsBean.getTaskDescription());
////                    fieldValue.setChecked(false);
//                    multimediaFieldValues.put(bean.getClientId(), taskDetailsBean.getTaskDescription());
//                }
                Log.i("FormEntryViewActivity", " inflateFieldsExist 7 ");
                fieldValue.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        isEditable = true;
                        if (isChecked) {
                            Log.i("FormEntryViewActivity", " fieldValue.setOnCheckedChangeListener( 6 " + bean.getClientId());
                            multimediaFieldValues.put(bean.getClientId(), "Yes");
                        } else {
                            Log.i("FormEntryViewActivity", " fieldValue.setOnCheckedChangeListener( 61 " + bean.getClientId());
                            multimediaFieldValues.put(bean.getClientId(), "No");
                        }
                        checked = true;
                    }
                });

              /*  if (bean.getFieldType() != null) {
                        fieldValue.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // TODO Auto-generated method stub
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                                    changeType(fieldValue);
                                    isEditable = true;
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });


                }*/
                Log.i("FormEntryViewActivity", " inflateFieldsExist 8 ");
            }

            fieldContainer.addView(view);
        } catch (Exception e) {
            e.printStackTrace();
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

    private void showMultimediaOptions(final int tag, String fieldId, String rtype) {
        try {
            if (rtype != null) {
                filedStrId = fieldId;
                Log.d("clone", "----came to showresponse dialog---- tag = "
                        + tag + " rtype = " + rtype);
                if (rtype.equalsIgnoreCase("photo")) {
                    position = Integer.valueOf(fieldId);
                    type = "photo";
                    photochat(type);

                } else if (rtype.equalsIgnoreCase("video")) {

                    Log.i("result", "===video entry====");
//                    type = "video";
                    position = Integer.valueOf(fieldId);
                    type = "video";
                    photochat(type);
                    /*strIPath = Environment.getExternalStorageDirectory()
                            + "/COMMedia/" + "MVD_"
                            + MainActivity.getFileName() + ".mp4";
                    Log.i("result", "---video File path----" + strIPath);


                    Intent intent = new Intent(context,
                            CustomVideoCamera.class);
                    intent.putExtra("filePath", strIPath);

                    startActivityForResult(intent, 40);*/

                }
            }


        } catch (Exception e) {
            Log.e("profile", "==> " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void photochat(String type1) {
        try {
            final String[] items = new String[]{"Gallery", type1};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0)
                        multimediaType(items[0], items[1]);
                    else if (which == 1)
                        multimediaType(items[1], null);
                }

            });

            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.cancel();
                }
            });
            builder.show();
        } catch (Exception e) {
            Log.e("profile", "===> " + e.getMessage());
            e.printStackTrace();
        }
    }

   /* private void multimediaType(String strMMType) {
        Log.i("clone", "===> inside message response");

        try {
            if (strMMType.equalsIgnoreCase("Gallery")) {
                Log.i("clone", "====> inside gallery");
                if (Build.VERSION.SDK_INT < 19) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image*//*");
                    startActivityForResult(intent, 30);
                } else {
                    Log.i("img", "sdk is above 19");
                    Log.i("clone", "====> inside gallery");
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image*//*");
                    startActivityForResult(intent, 31);
                }

            } else if (strMMType.equalsIgnoreCase("Photo")) {
                Log.i("result", "====> inside photo");
                Long free_size = getExternalMemorySize();
                Log.d("IM", free_size.toString());
                if (free_size > 0 && free_size >= 5120) {
                    // Intent i = new Intent(context, Custom_Camara.class);
                    // strIPath = Environment.getExternalStorageDirectory()
                    // + "/COMMedia/MPD_" + callDisp.getFileName()
                    // + ".jpg";
                    // i.putExtra("Image_Name", strIPath);
                    // Log.d("File Path", strIPath);
                    // startActivityForResult(i, 32);

                    final String path = Environment.getExternalStorageDirectory()
                            + "/High Message/";
                    File directory = new File(path);
                    if (!directory.exists())
                        directory.mkdir();
                    strIPath = path + Appreference.getFileName() + ".jpg";
                    Uri imageUri = Uri.fromFile(new File(strIPath));
                    Intent intent = new Intent(context,
                            CustomVideoCamera.class);
                    intent.putExtra("filePath", strIPath);
                    intent.putExtra("isPhoto", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 132);
                    // Intent intent = new Intent(mainContext,
                    // MultimediaUtils.class);
                    // intent.putExtra("filePath", strIPath);
                    // intent.putExtra("requestCode", CAMERA);
                    // intent.putExtra("action",
                    // MediaStore.ACTION_IMAGE_CAPTURE);
                    // intent.putExtra("createOrOpen", "create");
                    // Log.i("result", "-------photo");
                    // fragment.startActivityForResult(intent, 32);

                } else {
                    Toast.makeText(context, "InSufficient Memory...", Toast.LENGTH_LONG).show();
                }
            } else if (strMMType.equalsIgnoreCase("Video")) {

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("clone", "=======>" + e.getMessage());
            e.printStackTrace();
        }
    }*/


    private void multimediaType(String strMMType, String gtype) {
        Log.i("clone", "===> inside message response");

        try {
            if (strMMType.equalsIgnoreCase("Gallery")) {

                if (gtype != null && gtype.equalsIgnoreCase("photo")) {
                    Log.i("clone", "====> inside gallery");
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
                } else if (gtype != null && gtype.equalsIgnoreCase("video")) {
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

            } else if (strMMType.equalsIgnoreCase("Photo")) {
                Log.i("result", "====> inside photo");
                Long free_size = getExternalMemorySize();
                Log.d("IM", free_size.toString());
                if (free_size > 0 && free_size >= 5120) {
                    // Intent i = new Intent(context, Custom_Camara.class);
                    // strIPath = Environment.getExternalStorageDirectory()
                    // + "/COMMedia/MPD_" + callDisp.getFileName()
                    // + ".jpg";
                    // i.putExtra("Image_Name", strIPath);
                    // Log.d("File Path", strIPath);
                    // startActivityForResult(i, 32);

                    final String path = Environment.getExternalStorageDirectory()
                            + "/High Message/";
                    File directory = new File(path);
                    if (!directory.exists())
                        directory.mkdir();
                    strIPath = path + Appreference.getFileName() + ".jpg";
                    Uri imageUri = Uri.fromFile(new File(strIPath));
                    Intent intent = new Intent(context,
                            CustomVideoCamera.class);
                    intent.putExtra("filePath", strIPath);
                    intent.putExtra("isPhoto", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 132);
                    // Intent intent = new Intent(mainContext,
                    // MultimediaUtils.class);
                    // intent.putExtra("filePath", strIPath);
                    // intent.putExtra("requestCode", CAMERA);
                    // intent.putExtra("action",
                    // MediaStore.ACTION_IMAGE_CAPTURE);
                    // intent.putExtra("createOrOpen", "create");
                    // Log.i("result", "-------photo");
                    // fragment.startActivityForResult(intent, 32);

                } else {
                    Toast.makeText(context, "InSufficient Memory...", Toast.LENGTH_LONG).show();
                }
            } else if (strMMType.equalsIgnoreCase("Video")) {
                final String path = Environment.getExternalStorageDirectory()
                        + "/High Message/";
                File directory = new File(path);
                if (!directory.exists())
                    directory.mkdirs();
                strIPath = path + Appreference.getFileName() + ".mp4";
                Intent intent = new Intent(context, CustomVideoCamera.class);
                intent.putExtra("filePath", strIPath);
                intent.putExtra("isPhoto", false);
                intent.putExtra("filePath", strIPath);
//                            Uri fileUri = Uri.fromFile(new File(strIPath));
//                            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//                            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                startActivityForResult(intent, 111);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("clone", "=======>" + e.getMessage());
            e.printStackTrace();
        }
    }

    public long getExternalMemorySize() {
        try {
            if (externalMemoryAvailable()) {
                File path = Environment.getExternalStorageDirectory();
                Log.d("Im", path.toString());
                StatFs stat = new StatFs(path.getPath());
                long blockSize = stat.getBlockSize();
                long availableBlocks = stat.getAvailableBlocks();
                return (availableBlocks * blockSize);

            } else {
                return -1;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return -1;
        }

    }

    private boolean externalMemoryAvailable() {
        try {
            return Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }


    public void showCustomDatePicker(final String start_date1, final String fieldId) {
        try {
            final NumberPicker datePicker, hourPicker, minutePicker, am_pmPicker;
            final String[] dates;
            String[] mAmPmStrings = null;
            final int yyyy;
            position = Integer.valueOf(fieldId);


            final Dialog dialog = new Dialog(context, R.style.AppTheme);
            dialog.setContentView(R.layout.dialogfragment_time_date_picker);
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dates = getDatesFromCalender();

            datePicker = (NumberPicker) dialog
                    .findViewById(R.id.datePicker);
            hourPicker = (NumberPicker) dialog
                    .findViewById(R.id.hourPicker);
            minutePicker = (NumberPicker) dialog
                    .findViewById(R.id.minutePicker);
            am_pmPicker = (NumberPicker) dialog
                    .findViewById(R.id.am_pm);
            datePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            hourPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            minutePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            am_pmPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
            Button set = (Button) dialog
                    .findViewById(R.id.setbtn);
//            set.setTypeface(normal_type);
//			set.setOnClickListener(this);
            Log.i("datePicker", "is " + datePicker);
            datePicker.setClickable(false);
            datePicker.setMinValue(0);
            datePicker.setMaxValue(dates.length - 1);
            datePicker.setFormatter(new NumberPicker.Formatter() {

                @Override
                public String format(int value) {
                    return dates[value];
                }
            });
            mAmPmStrings = new DateFormatSymbols().getAmPmStrings();

            datePicker.setDisplayedValues(dates);
            minutePicker.setMinValue(0);
            minutePicker.setMaxValue(59);
            minutePicker.setWrapSelectorWheel(true);
            // ArrayList<String> displayedValues = new ArrayList<String>();
            String[] sdisplayedValues = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"};
            List<String> ampmValues = new ArrayList<String>();
            String[] am_pm = {"AM", "PM", "AM", "PM"};
            // for (int i = 0; i < 60; i += 15) {
            // displayedValues.add(String.format("%02d", i));
            // }
            minutePicker.setDisplayedValues(sdisplayedValues);
//             ampmValues.add("AM");
            // ampmValues.add("PM");
            am_pmPicker.setMinValue(0);
            am_pmPicker.setMaxValue(3);
            am_pmPicker.setWrapSelectorWheel(true);
            int am = 0;
            am_pmPicker.setDisplayedValues(am_pm);

            final Calendar rightNow = Calendar.getInstance(Locale.getDefault());
            int dd = rightNow.get(Calendar.HOUR_OF_DAY);
            Log.i("current", "hour" + dd);
            yyyy = rightNow.get(Calendar.YEAR);
//            if (!DateFormat.is24HourFormat(this)) {
//            hourPicker.setMinValue(0);
//            hourPicker.setMaxValue(11);
//            String[] displayedValues_hour = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
//            hourPicker.setDisplayedValues(displayedValues_hour);
//            } else {
//                hourPicker.setMinValue(00);
//                hourPicker.setMaxValue(23);
//                am_pmPicker.setVisibility(View.GONE);
//            }
            hourPicker.setMinValue(00);
            hourPicker.setMaxValue(23);
            String[] displayedValues_hour = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};
            hourPicker.setDisplayedValues(displayedValues_hour);
            am_pmPicker.setVisibility(View.GONE);

//                String dateInputString=String.valueOf(start_date1);
            String[] values1 = datePicker.getDisplayedValues();
//                String str = values1[datePicker.getValue()];
            st_hour = start_date1;
            st_hour = st_hour.split(" ")[1];
            String st_date = start_date1;
            st_date = st_date.split(" ")[0];

            Log.i("start", "st_date" + st_date);
            Log.i("start", "st_hour" + st_hour);
            try {
                // obtain date and time from initial string
                DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat targetFormat = new SimpleDateFormat("yyyy MMM dd");
                Date date = originalFormat.parse(st_date);
                Log.i("start", "original " + date);
                String formattedDate = targetFormat.format(date);
                Log.i("start", "target " + formattedDate);
                for (int i = 0; i < values1.length; i++) {
                    Log.i("start", "for" + values1[i]);
                    if (values1[i].equalsIgnoreCase(formattedDate)) {
                        Log.i("start", "if" + values1[i]);
                        datePicker.setValue(i);
                    }
//                        else
//                        {
//                            Log.i("start","else");
////                            datePicker.setValue(0);
//                        }
                }
            }
            // 20120821
            catch (Exception e) {
                e.printStackTrace();
            }
            int hour = Integer.parseInt(st_hour.split(":")[0]);
            int min = Integer.parseInt(st_hour.split(":")[1]);
            Log.i("start", "st_min" + min);
            minutePicker.setValue(min);
            Log.i("start", "hour " + hour);
//            if (hour >= 13) {
//                Log.i("start", "hour>=13 " + hour);
//                if (hour == 13) {
//                    strTime = "1";
//                } else if (hour == 14) {
//                    strTime = "2";
//                } else if (hour == 15) {
//                    strTime = "3";
//                } else if (hour == 16) {
//                    strTime = "4";
//                } else if (hour == 17) {
//                    strTime = "5";
//                } else if (hour == 18) {
//                    strTime = "6";
//                } else if (hour == 19) {
//                    strTime = "7";
//                } else if (hour == 20) {
//                    strTime = "8";
//                } else if (hour == 21) {
//                    strTime = "9";
//                } else if (hour == 22) {
//                    strTime = "10";
//                } else if (hour == 23) {
//                    strTime = "11";
//                }
//                hourPicker.setValue(Integer.parseInt(strTime) - 1);
//                am_pmPicker.setValue(1);
//            } else {
                hourPicker.setValue(hour);
//                am_pmPicker.setValue(0);
//            }
            set.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    try {
                        String startDATE, endDate = null, today, toas;
                        Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+1"));
                        String[] values1 = datePicker.getDisplayedValues();
                        String[] values2 = hourPicker.getDisplayedValues();
                        String[] values3 = minutePicker.getDisplayedValues();
                        String[] values4 = am_pmPicker.getDisplayedValues();
                        String tdystr = values1[datePicker.getValue()];
                        int dateposition = datePicker.getValue();
                        int hourposition = hourPicker.getValue();
                        if (values1[datePicker.getValue()]
                                .equalsIgnoreCase("today")) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
    //                        dates.add("Today");
                            today = dateFormat.format(c1.getTime());
                            tdystr = today;
                            Log.i("tdystr", "day " + tdystr);
                        } else {
                            tdystr = values1[datePicker.getValue()];
                            Log.i("tdystr", "day " + tdystr);
                        }
//                    if (!DateFormat.is24HourFormat(context)) {
                        Log.i("tdystr", "hour " + values2[hourPicker.getValue()]);
                        Log.i("tdystr", "min " + values3[minutePicker.getValue()]);
                        Log.i("tdystr", "am_pm " + values4[am_pmPicker.getValue()]);

//                        toas = tdystr + " " + values2[hourPicker.getValue()] + " : "
//                                + values3[minutePicker.getValue()] + " "
//                                + values4[am_pmPicker.getValue()];
//                    }else{
                        toas = tdystr + " " + hourPicker.getValue() + " : "
                                + values3[minutePicker.getValue()];
//                    }

                        // int month = dp.getMonth() + 1;
                        String day = values3[minutePicker.getValue()];// String.valueOf(tp.getCurrentMinute());
                        // if (day.length() == 1) {
                        // day = "0" + day;
                        // }
                        String strDateTime = values2[hourPicker.getValue()] + ":"
                                + values3[minutePicker.getValue()];// tp.getCurrentHour()
                        // + ":" + day;
                        String tm = null;

                        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
                        tm = time.format(new Date());

                        Log.i("SCHEDULECALL", "Now Time Is====>" + tm);

                        int startTime = Integer.parseInt(strDateTime
                                .split(":")[0]);
                        String startMin = strDateTime
                                .split(":")[1];

                        Log.i("utctime", "starMin--->" + startMin);
                        if (values4[am_pmPicker.getValue()]
                                .equalsIgnoreCase("pm")) {
                            Log.i("schedulecall", "pm picker");
                            if (startTime == 1) {
                                strTime = "13" + ":"
                                        + String.valueOf(startMin);
                            } else if (startTime == 2) {
                                strTime = "14" + ":"
                                        + String.valueOf(startMin);
                            } else if (startTime == 3) {
                                strTime = "15" + ":"
                                        + String.valueOf(startMin);
                            } else if (startTime == 4) {
                                strTime = "16" + ":"
                                        + String.valueOf(startMin);
                            } else if (startTime == 5) {
                                strTime = "17" + ":"
                                        + String.valueOf(startMin);
                            } else if (startTime == 6) {
                                strTime = "18" + ":"
                                        + String.valueOf(startMin);
                            } else if (startTime == 7) {
                                strTime = "19" + ":"
                                        + String.valueOf(startMin);
                            } else if (startTime == 8) {
                                strTime = "20" + ":"
                                        + String.valueOf(startMin);
                            } else if (startTime == 9) {
                                strTime = "21" + ":"
                                        + String.valueOf(startMin);
                            } else if (startTime == 10) {
                                strTime = "22" + ":"
                                        + String.valueOf(startMin);
                            } else if (startTime == 11) {
                                strTime = "23" + ":"
                                        + String.valueOf(startMin);
                            }
    //                            else if (startTime == 12) {
    //                                strTime = "00" + ":"
    //                                        + String.valueOf(startMin);
    //                            }
                            else
                                strTime = strDateTime;

                        } else {
                            if (values4[am_pmPicker.getValue()]
                                    .equalsIgnoreCase("am")) {
                                Log.i("schedulecall", "am picker");
                                if (startTime == 12) {
                                    Log.i("schedulecall", "am picker 12");
                                    strTime = "00" + ":"
                                            + String.valueOf(startMin);
                                } else {
                                    Log.i("schedulecall", "am picker !12");
                                    strTime = strDateTime;
                                }
                            } else {
                                Log.i("schedulecall", "24 format");
                                strTime = strDateTime;
                            }
                        }
                        //        ampm = values4[am_pmPicker.getValue()];
                        String strfromdate = start_date1;
                        String strfromday = strfromdate.split(" ")[0];

                        DateFormat selctedDateformate = new SimpleDateFormat("MMM dd yyyy HH : mm");
                        Date date = null;
                        try {
                            date = selctedDateformate.parse(toas);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        SimpleDateFormat day1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        strenddate = day1.format(date);

                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date1 = sdf.parse(strenddate);
                            Date date2 = new Date();
                            String current = sdf.format(date2);
                            Date Current_Date=sdf.parse(current);
                            System.out.println("date1 : " + sdf.format(date1));
                            System.out.println("date2 : " + sdf.format(date2));

                            /*if (date1.compareTo(Current_Date) > 0) {
                                Toast.makeText(context," Date 1 is after Date2 & ",Toast.LENGTH_SHORT).show();
                            } else if (date1.compareTo(Current_Date) < 0) {
                                Toast.makeText(context," Date 1 is before Date2 & ",Toast.LENGTH_SHORT).show();
                            } else if (date1.compareTo(Current_Date) == 0) {
                                Toast.makeText(context," Date 1 and Date2 are equals ==== & ",Toast.LENGTH_SHORT).show();
                            } else {
                                System.out.println("How to get here? & ");
                            }*/
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Log.d("TagValues", "Dtae is ==  " + strenddate + " position is  ===  " + position);

                        LinearLayout parent_view1 = (LinearLayout) fieldContainer
                                .getChildAt(position - 1);
                        final TextView value_img;

                        value_img = (TextView) parent_view1
                                .findViewById(R.id.fieldText);

                        value_img.setVisibility(View.VISIBLE);
                        value_img.setText(strenddate);

                        multimediaFieldValues.put(fieldId, strenddate);

//                        String strendmonth = strenddate.split("-")[1];
//                        int strendmonth1 = Integer.parseInt(strendmonth);
                        String strendday = strenddate.split(" ")[0];
                        multimediaFieldValues.put(String.valueOf(position), strenddate);
                        int strendday1 = Integer.parseInt(strendday.split("-")[2]);
                        if (strfromday.compareTo(strendday) <= 0) {
                            if (CheckReminderIsValid(
                                    values4[am_pmPicker.getValue()], dateposition,
                                    strTime, tm, true)) {
    //                                start_date1.setText(strenddate);

                                   /* start_date = dateposition;
                                    start_time = hourPicker.getValue();
                                    start_minute = minutePicker.getValue();
                                    start_am_pm = am_pmPicker.getValue();*/
    //                                startTimeSet = true;
    //
    //                                strDATE = values1[datePicker.getValue()];
    //                            } else {
    //                                showToast(context.getString(R.string.Kindly_slct_future_time));
    //                            }
                            } else {
    //                            Toast.makeText(getApplicationContext(), "Kindly select future date", Toast.LENGTH_LONG).show();
                            }


                            //                datePicker.setValue();
                            int hour = Integer.parseInt(st_hour.split(":")[0]);
                            Log.i("start", "hour " + hour);
                            if (hour >= 13)

                            {
                                Log.i("start", "hour>=13 " + hour);
                                if (hour == 13) {
                                    strTime = "1";
                                } else if (hour == 14) {
                                    strTime = "2";
                                } else if (hour == 15) {
                                    strTime = "3";
                                } else if (hour == 16) {
                                    strTime = "4";
                                } else if (hour == 17) {
                                    strTime = "5";
                                } else if (hour == 18) {
                                    strTime = "6";
                                } else if (hour == 19) {
                                    strTime = "7";
                                } else if (hour == 20) {
                                    strTime = "8";
                                } else if (hour == 21) {
                                    strTime = "9";
                                } else if (hour == 22) {
                                    strTime = "10";
                                } else if (hour == 23) {
                                    strTime = "11";
                                }
                                hourPicker.setValue(Integer.parseInt(strTime) - 1);
                                am_pmPicker.setValue(1);
                            } else

                            {
                                hourPicker.setValue(hour - 1);
                                am_pmPicker.setValue(0);
                            }

                            Log.i("start", "am_pmPicker" + am_pmPicker.getValue());
    //                hourPicker.setValue(hour - 1);
    //                hourPicker.setValue(dd);


                        }
                        dialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
            dialog.setTitle(context.getString(R.string.Date_and_Time));
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] getDatesFromCalender() {
        Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+1"));
        Calendar c2 = Calendar.getInstance(TimeZone.getTimeZone("GMT+1"));
        String today = "";
        List<String> dates = new ArrayList<String>();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
            dates.add("Today");
            today = dateFormat.format(c1.getTime());
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c.getTime());
            String split[] = formattedDate.split("-");
            String todaydate = split[0] + " " + split[1];
            for (int i = 0; i < 365; i++) {
                c1.add(Calendar.DATE, 1);
                dates.add(dateFormat.format(c1.getTime()));

            }
            c2.add(Calendar.DATE, -60);

            for (int i = 0; i < 59; i++) {
                c2.add(Calendar.DATE, 1);
                dates.add(dateFormat.format(c2.getTime()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dates.toArray(new String[dates.size() - 1]);
    }

    private boolean CheckReminderIsValid(String am_pm, int day,
                                         String strDate, String date, boolean start) {
        boolean isvalid = false;
        try {
            Calendar rightNow = Calendar.getInstance(Locale.getDefault());
            int amPM = rightNow.get(Calendar.AM_PM);

            int position = day;
            int startTime = Integer.parseInt(strDate.split(":")[0]);
            int currentTime = Integer.parseInt(date.split(":")[0]);

            int startMin = Integer.parseInt(strDate.split(":")[1]);
            int currentMin = Integer.parseInt(date.split(":")[1]);
            Log.i("SCHEDULECALL", "Start Date===>" + startTime);
            Log.i("SCHEDULECALL", "selected Date===>" + currentTime);

            Log.i("SCHEDULECALL", "startMin ===>" + startMin);
            Log.i("SCHEDULECALL", "currentMin ===>" + currentMin);
            if (start) {

                if (position > 0 && position < 364) {
                    isvalid = true;
                } else if (position == 0 && startTime > currentTime) {
                    isvalid = true;
                } else if (position == 0 && startTime >= currentTime
                        && startMin > currentMin) {
                    isvalid = true;
                } else if (position == 0 && amPM == 0
                        && am_pm.equalsIgnoreCase("pm")) {
                } /*else if (ampm.equalsIgnoreCase("pm") && am_pm.equalsIgnoreCase("pm")) {
                    if (startTime >= currentTime
                            && startMin > currentMin) {
                        isvalid = true;
                    } else {
                        isvalid = false;
                    }

                }*/ else {

                    isvalid = false;
                }

            } else {
                if (currentTime >= startTime || currentMin > startMin) {
                    isvalid = true;
                } else {

                    isvalid = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isvalid;
    }

    @Override
    public void ResponceMethod(final Object object) {


        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.i("currentstatus", "Task Details Respons method");
                CommunicationBean communicationBean = (CommunicationBean) object;

                String s1 = communicationBean.getEmail();
                String s2 = communicationBean.getFirstname();


                Log.i("form", "responce" + s1);


                try {
                    cancelDialog();

                    if (s1.contains("setId") && !s1.contains("createdDate")) {
//                        cancelDialog();


                        JSONArray jr = new JSONArray(s1);
                        if (jr.length() > 0) {
                            Log.i("FormEntryViewActivity", " ResponceMethod 1 ");
                            Type collectionType = new TypeToken<List<FormEntryValueBean>>() {
                            }.getType();
                            List<FormEntryValueBean> lcs = new Gson().fromJson(s1, collectionType);
                            if (setIdValueList.size() > 0) {
                                setIdValueList.clear();
                                fwdIncrement = 0;
                            }
                            if (formFieldsBeenMap.size() > 0) {
                                formFieldsBeenMap.clear();
                            }
                            for (int i = 0; i < lcs.size(); i++) {
                                Log.i("FormEntryViewActivity", " 51 ");
                                Log.i("FormEntryViewActivity", " ResponceMethod 1 ");
                                FormEntryValueBean groubDetails = lcs.get(i);
                                formFieldsBeenMap.put(String.valueOf(groubDetails.getSetId()), (ArrayList<com.ase.Forms.List>) groubDetails.getList());

                                for (com.ase.Forms.List list : groubDetails.getList()) {
                                    if (accesstype != null && accesstype.equalsIgnoreCase("Edit My Values")) {
                                        if (Appreference.loginuserdetails.getId() == list.getCreatedBy()) {
                                            if (!setIdValueList.contains(String.valueOf(groubDetails.getSetId())))
                                                setIdValueList.add(String.valueOf(groubDetails.getSetId()));
                                            Log.i("FormEntryViewActivity", " ResponceMethod 2 ");
                                        }
                                    } else {
                                        if (!setIdValueList.contains(String.valueOf(groubDetails.getSetId())))
                                            setIdValueList.add(String.valueOf(groubDetails.getSetId()));
                                        Log.i("FormEntryViewActivity", " ResponceMethod 3 ");
                                    }
                                    if ( (list.getFieldType() != null && (list.getFieldType().equalsIgnoreCase("photo") || list.getFieldType().equalsIgnoreCase("image") || list.getFieldType().equalsIgnoreCase("signature") || list.getFieldType().equalsIgnoreCase("audio") || list.getFieldType().equalsIgnoreCase("video"))) && (( list.getFieldValue() != null && (!list.getFieldValue().equalsIgnoreCase("-") || !list.getFieldValue().equalsIgnoreCase(""))))) {

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                            new DownloadImage(getResources().getString(R.string.task_reminder) + list.getFieldValue()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                        } else {
                                            new DownloadImage(getResources().getString(R.string.task_reminder) + list.getFieldValue()).execute();
                                        }
                                        Log.i("FormEntryViewActivity", " ResponceMethod 4 ");
                                    }
                                }


                                Log.d("FormREsponse", "list size is  ==  " + setIdValueList.size() + "   map size is == " + formFieldsBeenMap.size());
                            }
                            loadUI();

                        }
                    } else if (s1.contains("formTemp")) {
                        cancelDialog();
                        Log.i("FormEntryViewActivity", " 52 ");
                        Log.i("jsonwebservice", communicationBean.getEmail());
                        Gson gson = new Gson();
//                        jsonObject = new JSONObject(communicationBean.getEmail());

                        JSONArray jsonArray = new JSONArray(communicationBean.getEmail());
                        if (jsonArray.length() > 0) {
                            ArrayList<FormFieldsBean> formFieldsBeenList = new ArrayList<FormFieldsBean>();
                            // jsonArray=jelement.getAsJsonArray();
                            Log.i("response array", "response size" + jsonArray.length());
                            Log.i("FormEntryViewActivity", " 54 ");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Log.i("FormEntryViewActivity", " ResponceMethod 5 ");
                                //JSONObject jsonObject1= (JSONObject) jsonArray.getJSONObject(i);
                                Log.i("response ", "objects  " + jsonArray.get(i));
                                FormFieldsBean formFieldsBean = gson.fromJson(jsonArray.getString(i), FormFieldsBean.class);
                                formFieldsBean.setClientId(String.valueOf(i + 1));
                                Log.i("FormEntryViewActivity", " 1111 ++++ +++ " + formFieldsBean.getClientId());
                                formFieldsBeenList.add(formFieldsBean);
                                Log.i("FormEntryViewActivity", " 55 " + formFieldsBean.getFieldType() + "  " + formFieldsBean.getFieldValue() + "  " + formFieldsBean.getFieldName() + "  " + formFieldsBean.getClientId() + "  " + formFieldsBean.getIsInputRequired() + "  " + formFieldsBean.getCreatedBy() + "  " + formFieldsBean.getFormTemp() + "  ");

                            }
                            if (formFieldsBeenList.size() > 0) {
                                Intent intent = new Intent(FormEntryViewActivity.this, FormsEntryActivity.class);
                                intent.putExtra("FormsEntryValue", formFieldsBeenList);
                                intent.putExtra("FormsId", formId);
                                intent.putExtra("TaskId", taskId);
                                intent.putExtra("TaskNo", taskNo);
                                intent.putExtra("TaskName", taskName);
                                intent.putExtra("EntryMode", entryMode);
                                intent.putExtra("UserList", stringArrayList);
                                intent.putExtra("TaskBean", taskDetailsBean);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                Log.i("FormEntryViewActivity", " 56 ");
                                Log.i("FormEntryViewActivity", " ResponceMethod 6 ");
                                startActivity(intent);
                                finish();
                            }
                        }
                    } else if (s1.contains("updated successfull")) {
                        cancelDialog();
                        showToast("Updated Successfully");
                        taskDetailsBean.setTaskDescription("Forms has been changed");
                        taskDetailsBean.setMimeType("text");
                        String xml = composeFormsXml();
                        String xml_ui = composeChatXML(taskDetailsBean);
                        sendMultiInstantMessage(xml, stringArrayList);
                        sendMultiInstantMessage(xml_ui, stringArrayList);

                        VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                        Appreference.createdFormsList.add(taskDetailsBean);
                        Log.i("FormEntryViewActivity", " 57 ");
                        callFormsListWebSerices();
                        Log.i("FormEntryViewActivity", " ResponceMethod 7 ");
                        cancelDialog();
                        isEditable = false;
                    } else if (s1.contains("updated failed")) {
                        cancelDialog();
                        showToast("Updated Failed");
                        Log.i("FormEntryViewActivity", " 58 ");
                        callFormsListWebSerices();
                        Log.i("FormEntryViewActivity", " ResponceMethod 8 ");
                    } else if (s1.contains("deleted")) {
                        cancelDialog();
                        Log.i("FormEntryViewActivity", " 59 ");
                        callFormsListWebSerices();
                        Log.i("FormEntryViewActivity", " ResponceMethod 9 ");
                    } else if (s1.contains("accessMode")) {
                        cancelDialog();
                        JSONObject jsonObject = new JSONObject(communicationBean.getEmail());
                        accesstype = (String) jsonObject.get("accessMode");
                        callFormsListWebSerices();
                        Log.i("FormEntryViewActivity", " 60 ");
                        touserid = (String) String.valueOf(jsonObject.get("to"));
                        Log.i("FormEntryViewActivity", " ResponceMethod 10 ");
                    } else if (s1.contains("setId") && s2.contains("getFormFieldAndValues")) {
                        cancelDialog();
                        Log.i("FormEntryViewActivity", " ResponceMethod 11 ");
                        List<NameValuePair> tagNameValuePairs = new ArrayList<NameValuePair>();
                        tagNameValuePairs.add(new BasicNameValuePair("formId", formId));
                        Appreference.jsonRequestSender.getFormFieldsForForm(EnumJsonWebservicename.getFormFieldsForForm, tagNameValuePairs, FormEntryViewActivity.this);
                        Log.i("FormEntryViewActivity", " 61 ");
                        taskDetailsBean.setTaskDescription("Forms has been changed");
                        String xml = composeFormsXml();
                        String xml_ui = composeChatXML(taskDetailsBean);
                        sendMultiInstantMessage(xml, stringArrayList);
                        sendMultiInstantMessage(xml_ui, stringArrayList);

                        VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                        Appreference.createdFormsList.add(taskDetailsBean);

                        Log.i("FormEntryViewActivity", " 62 ");
                        Log.i("FormEntryViewActivity", " ResponceMethod 12 ");
                    } else if (s1.contains("[]") && s2.equalsIgnoreCase("getFormAccessForTaskId")) {
                        cancelDialog();
                        callFormsListWebSerices();
                        Log.i("FormEntryViewActivity", " 63 ");
                        Log.i("FormEntryViewActivity", " ResponceMethod 13 ");
                    } else if (!entryMode.equalsIgnoreCase("single") && s1.contains("[]")) {
                        cancelDialog();
                        fieldContainer.setVisibility(View.GONE);
                        buttomlinear.setVisibility(View.GONE);
                        tv_noData.setVisibility(View.VISIBLE);
                        Log.i("FormEntryViewActivity", " ResponceMethod 14 ");
                    } else if (!entryMode.equalsIgnoreCase("single") && s2.equalsIgnoreCase("getFormFieldsForForm") && s1.equalsIgnoreCase("[]")) {
                        cancelDialog();
                        Log.i("FormEntryViewActivity", " 64 ");
                        Log.i("FormEntryViewActivity", " ResponceMethod 15 ");
//                        tv_noData.setVisibility(View.VISIBLE);
                    } else {
                        Log.i("form", "else");
                        if (entryMode.equalsIgnoreCase("single")) {
                            cancelDialog();
                            Log.i("FormEntryViewActivity", " 65 ");
                            List<NameValuePair> tagNameValuePairs = new ArrayList<NameValuePair>();
                            tagNameValuePairs.add(new BasicNameValuePair("formId", formId));
                            Appreference.jsonRequestSender.getFormFieldsForForm(EnumJsonWebservicename.getFormFieldsForForm, tagNameValuePairs, FormEntryViewActivity.this);
                            Log.i("FormEntryViewActivity", " ResponceMethod 16 ");
                            String xml = composeFormsXml();
                            String xml_ui = composeChatXML(taskDetailsBean);
                            sendMultiInstantMessage(xml, stringArrayList);
                            sendMultiInstantMessage(xml_ui, stringArrayList);

                        } else {
                            cancelDialog();
                            Log.i("FormEntryViewActivity", " 66 ");
                            Log.i("form", "else if else");
                            Log.i("FormEntryViewActivity", " ResponceMethod 17 ");
                            tv_noData.setVisibility(View.VISIBLE);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void ErrorMethod(Object object) {

    }

    public void callFormsListWebSerices() {
        Log.i("FormEntryView", "callFormsListWebSerices == " + formId);
        showprogress();
        Log.i("FormEntryViewActivity", " showprogress  4 ");
        List<NameValuePair> tagNameValuePairs = new ArrayList<NameValuePair>();
        tagNameValuePairs.add(new BasicNameValuePair("formId", formId));
        tagNameValuePairs.add(new BasicNameValuePair("taskId", taskId));
        Log.i("FormEntryViewActivity", " 67 ");
        Log.i("FormEntryView", "call w/s form Id  2== " + formId);

        Appreference.jsonRequestSender.getFormFieldAndValues(EnumJsonWebservicename.getFormFieldAndValues, tagNameValuePairs, FormEntryViewActivity.this);

    }

    public void callFormsListWebSerices(TaskDetailsBean taskDetailsBean) {
        Log.i("FormEntryView", "callFormsListWebSerices == " + formId);
        showprogress();
        Log.i("FormEntryViewActivity", " showprogress 5 ");
        Log.i("FormEntryViewActivity", " 68 ");
        List<NameValuePair> tagNameValuePairs = new ArrayList<NameValuePair>();
        tagNameValuePairs.add(new BasicNameValuePair("formId", taskDetailsBean.getRemark()));
        tagNameValuePairs.add(new BasicNameValuePair("taskId", taskDetailsBean.getTaskId()));

        Log.i("FormEntryView", "call w/s form Id  3== " + formId + " bean Id " + taskDetailsBean.getRemark());

        Appreference.jsonRequestSender.getFormFieldAndValues(EnumJsonWebservicename.getFormFieldAndValues, tagNameValuePairs, FormEntryViewActivity.this);

    }


    public String composeFormsXml() {
        StringBuffer buffer = new StringBuffer();
        try {
            Log.i("FormEntryViewActivity", " 69 ");
            buffer.append("<?xml version=\"1.0\"?>"
                    + "<FormsDetailsInfo><FormsDetails");
            buffer.append(" taskName=" + quotes + taskName + quotes);
            buffer.append(" taskNo=" + quotes + taskNo + quotes);
            buffer.append(" taskId=" + quotes + taskId + quotes);
            buffer.append(" formId=" + quotes + formId + quotes);
            buffer.append(" fromUserId=" + quotes + Appreference.loginuserdetails.getId() + quotes);
            buffer.append(" fromUserName=" + quotes + Appreference.loginuserdetails.getUsername() + quotes);

            buffer.append(" />");

            buffer.append("</FormsDetailsInfo>");
            Log.d("xml", "composed xml for chat======>" + buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            return buffer.toString();
        }

    }

    public String composeChatXML(TaskDetailsBean cmbean) {
        StringBuffer buffer = new StringBuffer();
        byte[] data_1;
        String base_64 = null,base64=null;
        try {
            Log.i("FormEntryViewActivity", " 70 ");
            if (cmbean.getTaskDescription() != null) {
                byte[] data = cmbean.getTaskDescription().getBytes("UTF-8");
                Log.d("base64value", "base64 before " + cmbean.getTaskDescription());
                base64 = Base64.encodeToString(data, Base64.DEFAULT);
            }
            if (cmbean.getServerFileName() != null) {
                data_1 = cmbean.getServerFileName().trim().getBytes("UTF-8");
                Log.d("base64value", "base64 before " + cmbean.getServerFileName());
                base_64 = Base64.encodeToString(data_1, Base64.DEFAULT);
            }
            buffer.append("<?xml version=\"1.0\"?>" + "<TaskDetailsinfo><TaskDetails");
            if (cmbean.getTaskName() != null && !cmbean.getTaskName().equalsIgnoreCase("") && !cmbean.getTaskName().equalsIgnoreCase(null)) {
                String taskName = cmbean.getTaskName();
                if (taskName.contains("&") || taskName.contains("<") || taskName.contains("\"")) {
                    if (taskName.contains("<")) {
                        taskName = taskName.replaceAll("<", "&lt;");
                    }
                    if (taskName.contains("&")) {
                        taskName = taskName.replaceAll("&", "&amp;");
                    }
                    if (taskName.contains("\"")) {
                        taskName = taskName.replaceAll("\"", "&quot;");
                    }
                    buffer.append(" taskName=" + quotes + taskName + quotes);
                    Log.i("URL", "value " + taskName);
                } else {
                    buffer.append(" taskName=" + quotes + cmbean.getTaskName() + quotes);
                }
//                buffer.append(" taskName=" + quotes + cmbean.getTaskName() + quotes);
            } else {
                buffer.append(" taskName=" + quotes + "New Task" + quotes);
            }
            if (!cmbean.getMimeType().equalsIgnoreCase("text") && !cmbean.getMimeType().equalsIgnoreCase("url")) {
                if ((cmbean.getServerFileName() != null) && (!cmbean.getServerFileName().equalsIgnoreCase(null)) && (!cmbean.getServerFileName().equalsIgnoreCase(""))) {
                    Log.i("URL", "SaveFilename-->" + base_64.trim());
                    buffer.append(" taskDescription=" + quotes + base_64.trim() + quotes);
                } else {
                    if(base64 != null) {
                        Log.i("URL", "TaskDes-->" + base64);
                        buffer.append(" taskDescription=" + quotes + base64.trim() + quotes);
                    }
                }
            } else {
                Log.i("URL", "value * " + cmbean.getTaskDescription());
                if (base64 != null) {
                    String s = base64.trim();
                    if (s.contains("&") || s.contains("<") || s.contains("\"")) {
                        if (s.contains("<")) {
                            s = s.replaceAll("<", "&lt;");
                        }
                        if (s.contains("&")) {
                            s = s.replaceAll("&", "&amp;");
                        }
                        if (s.contains("\"")) {
                            s = s.replaceAll("\"", "&quot;");
                        }
                        buffer.append(" taskDescription=" + quotes + s + quotes);
                        Log.i("URL", "value " + s);
                    } else {
                        buffer.append(" taskDescription=" + quotes + base64.trim() + quotes);
                    }
                }
            }
            buffer.append(" fromUserId=" + quotes + Appreference.loginuserdetails.getId() + quotes);
            buffer.append(" fromUserName=" + quotes + Appreference.loginuserdetails.getUsername() + quotes);
            buffer.append(" toUserId=" + quotes + cmbean.getToUserId() + quotes);
            buffer.append(" toUserName=" + quotes + cmbean.getToUserName() + quotes);
            buffer.append(" taskNo=" + quotes + taskNo + quotes);
            buffer.append(" taskId=" + quotes + taskId + quotes);
            buffer.append(" taskType=" + quotes + cmbean.getTaskType() + quotes);
            buffer.append(" plannedStartDateTime=" + quotes + cmbean.getUtcPlannedStartDateTime() + quotes);
            buffer.append(" plannedEndDateTime=" + quotes + cmbean.getUtcplannedEndDateTime() + quotes);
            buffer.append(" isRemainderRequired=" + quotes + cmbean.getIsRemainderRequired() + quotes);
            Log.i("newtaskconversation", "remainderDateTime " + cmbean.getUtcPemainderFrequency());
            if (cmbean.getUtcPemainderFrequency() == null || (cmbean.getUtcPemainderFrequency() != null && cmbean.getUtcPemainderFrequency().equalsIgnoreCase(""))) {
                cmbean.setUtcPemainderFrequency("");
            }
            buffer.append(" remainderDateTime=" + quotes + cmbean.getUtcPemainderFrequency() + quotes);
            if (cmbean.getCompletedPercentage() != null && !cmbean.getCompletedPercentage().equalsIgnoreCase("") && Integer.parseInt(cmbean.getCompletedPercentage()) == 100) {
                if (cmbean.getTaskStatus() != null && cmbean.getTaskStatus().equalsIgnoreCase("Closed")) {
                    buffer.append(" taskStatus=" + quotes + "Closed" + quotes);
                } else {
                    buffer.append(" taskStatus=" + quotes + "Completed" + quotes);
                }
            } else {
                Log.i("Accept", "value taskStatus after compose " + cmbean.getTaskStatus());
                buffer.append(" taskStatus=" + quotes + cmbean.getTaskStatus() + quotes);
            }
            buffer.append(" signalid=" + quotes + Utility.getSessionID() + quotes);
            buffer.append(" parentId=" + quotes + cmbean.getParentId() + quotes);
            buffer.append(" taskRequestType=" + quotes + "formDetailsChangeRequest" + quotes);
            buffer.append(" dateFrequency=" + quotes + cmbean.getDateFrequency() + quotes);
            String TimeFrequency = "", ReminderQuotes = "";

            buffer.append(" timeFrequency=" + quotes + "" + quotes);
            buffer.append(" taskOwner=" + quotes + cmbean.getOwnerOfTask() + quotes);
            buffer.append(" mimeType=" + quotes + cmbean.getMimeType() + quotes);
            buffer.append(" dateTime=" + quotes + cmbean.getTaskUTCDateTime() + quotes);
            buffer.append(" taskPriority=" + quotes + cmbean.getTaskPriority() + quotes);
            buffer.append(" completedPercentage=" + quotes + cmbean.getCompletedPercentage() + quotes);
            buffer.append(" remainderQuotes=" + quotes + cmbean.getReminderQuote() + quotes);
            buffer.append(" remark=" + quotes + cmbean.getRemark() + quotes);
            buffer.append(" taskReceiver=" + quotes + cmbean.getTaskReceiver() + quotes);

            buffer.append(" taskToUsersList=" + quotes + cmbean.getGroupTaskMembers() + quotes);
            buffer.append(" requestStatus=" + quotes + cmbean.getRequestStatus() + quotes);
            buffer.append(" subType=" + quotes + "formDetailsChangeRequest" + quotes);
            buffer.append(" daysOfTheWeek=" + quotes + cmbean.getDaysOfTheWeek() + quotes);
            buffer.append(" repeatFrequency=" + quotes + cmbean.getRepeatFrequency() + quotes);
            buffer.append(" taskTagName=" + quotes + taskName + quotes);
            buffer.append(" taskTagId=" + quotes + formId + quotes);
            buffer.append(" taskTagGroupId=" + quotes + setId + quotes);
            buffer.append(" isShowOnUI=" + quotes + cmbean.isCustomTagVisible() + quotes);
            buffer.append(" taskTagValue=" + quotes + cmbean.getTaskDescription() + quotes);
            buffer.append(" projectId=" + quotes + cmbean.getProjectId() + quotes);
            buffer.append(" taskCategory=" + quotes + "taskCreation" + quotes);
            buffer.append(" parentTaskId=" + quotes + cmbean.getIssueId() + quotes);
            buffer.append(" />");
            buffer.append("</TaskDetailsinfo>");
            Log.d("xml", "composed xml for chat======>" + buffer.toString());
            Log.d("xml", "composed xml for encode data======>" + Charset.forName("UTF-8").encode(":-)").toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return buffer.toString();
        }
    }

    public void sendMultiInstantMessage(String msgBody, ArrayList<String> userlist) {
        Log.i("FormEntryViewActivity", " 70 ");
//        String buddy_uri = "<"+number+">";
//        Log.i("chat", "buddy_uri======= " + buddy_uri);
//        BuddyConfig bCfg = new BuddyConfig();
//        bCfg.setUri(buddy_uri);
//        bCfg.setSubscribe(true);
        for (String name : userlist) {
            Log.i("task observer", "observer 1 " + name);
        }
        for (int i = 0; i < MainActivity.account.buddyList.size(); i++) {
            String name = MainActivity.account.buddyList.get(i).cfg.getUri();
            Log.i("task", "buddyname-->" + name);
            for (String username : userlist) {
                Log.i("task", "taskObservers Name-->" + username);
                String nn = "sip:" + username + "@" + getResources().getString(R.string.server_ip);
                Log.i("task", "selected user-->" + nn);
                if (nn.equalsIgnoreCase(name)) {
                    Log.i("task", "both users are same");
                    Appreference.printLog("Sipmessage", msgBody, "DEBUG", null);
                    MyBuddy myBuddy = MainActivity.account.buddyList.get(i);
                    SendInstantMessageParam prm = new SendInstantMessageParam();
                    prm.setContent(msgBody);

                    boolean valid = myBuddy.isValid();
                    Log.i("task", "valid ======= " + valid);
                    try {
                        myBuddy.sendInstantMessage(prm);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    private void showprogress() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("expand", "inside show progress--------->");
                    Log.i("FormEntryViewActivity", " 71 ");
                    if(progress == null || !progress.isShowing()) {
                        progress = new ProgressDialog(context);
                        progress.setCancelable(false);
                        progress.setMessage("Fetching form details");
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setProgress(0);
                        progress.setMax(1000);
                        progress.show();
                    }
                } catch (Exception e) {
                }
            }


        });
    }

    public void cancelDialog() {
        try {
            if (progress != null && progress.isShowing()) {
                Log.i("register", "--progress bar end-----");
                progress.dismiss();
                Appreference.isRequested_date = false;
                progress = null;
                Log.i("FormEntryViewActivity", " 72 ");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void showToast(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(FormEntryViewActivity.this, msg, Toast.LENGTH_LONG)
                        .show();
                Log.i("FormEntryViewActivity", " 73 ");
            }
        });
    }


    public void loadUI() {

        if (touserid != null && touserid.equalsIgnoreCase(String.valueOf(Appreference.loginuserdetails.getId()))) {
            Log.i("FormEntryViewActivity", " 53 ");
            if (accesstype != null && accesstype.equalsIgnoreCase("Read Only")) {
                Log.i("form", "access 1" + accesstype);
                bt_save.setVisibility(View.GONE);
                bt_delete.setVisibility(View.GONE);
                send.setVisibility(View.GONE);
                edited = true;
            } else if (accesstype != null && accesstype.equalsIgnoreCase("Add new")) {
                Log.i("FormEntryViewActivity", " 74 ");
                Log.i("form", "access 2" + accesstype);
                bt_save.setVisibility(View.GONE);
                bt_delete.setVisibility(View.GONE);
                edited = true;
            } else if (accesstype != null && accesstype.equalsIgnoreCase("Edit All Values")) {
                Log.i("form", "access 3" + accesstype);
                Log.i("FormEntryViewActivity", " 75 ");
                bt_delete.setVisibility(View.GONE);
                send.setVisibility(View.GONE);
                edited = false;
            } else if (accesstype != null && accesstype.equalsIgnoreCase("Edit My Values")) {
                Log.i("FormEntryViewActivity", " 76 ");
                send.setVisibility(View.GONE);
                edited = false;
            }
        }
        if (formFieldsBeenMap.size() == 0) {
            tv_noData.setVisibility(View.VISIBLE);
        } else {
            tv_noData.setVisibility(View.GONE);
        }

        if (!entryMode.equalsIgnoreCase("single"))
            buttomlinear.setVisibility(View.VISIBLE);

        forword.setVisibility(View.INVISIBLE);
        backword.setVisibility(View.INVISIBLE);

        if (setIdValueList.size() > 1) {
            forword.setVisibility(View.VISIBLE);
        }
        Log.d("FormREsponse", " value list size is  ==  " + setIdValueList.size() + "   map size is == " + formFieldsBeenMap.size() + "\n"
                + "   set id 0th position is == " + setIdValueList.get(0) + "   entity " + entryWay);
        if (setIdValueList.size() > 0 && entryWay != null && entryWay.equalsIgnoreCase("Adapter")) {
            loadProfileFieldValue(setId);
            Log.i("FormEntryViewActivity", " 54 ");
        } else if (setIdValueList.size() > 0) {
            loadProfileFieldValue(String.valueOf(setIdValueList.get(0)));
            Log.i("FormEntryViewActivity", " 55 ");
        }

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public class DownloadImage extends AsyncTask<String, Void, String> {
        String downloadImageurl;
        TaskDetailsBean detailsBean = null;

        public DownloadImage(String url) {
            this.downloadImageurl = url;
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                Log.i("profiledownload", "downloadImageurl " + downloadImageurl);
                Log.i("profiledownload", "profile download");
                if (isNetworkAvailable()) {
//                    String url ="http://122.165.92.171:8080/uploads/highmessaging/user/";

                    String profile1 = null;
                    if (downloadImageurl != null) {
                        if (downloadImageurl.contains("task"))
                            profile1 = downloadImageurl.split("task/")[1];
                        else
                            profile1 = downloadImageurl.split("chat/")[1];
                        File extStore = Environment.getExternalStorageDirectory();
                        File myFile = new File(extStore.getAbsolutePath() + "/High Message/downloads/" + profile1);
                        Log.i("profiledownload", "file " + myFile.toString());
                        if (!myFile.exists()) {
                            try {


                                URL bitmap = new URL(downloadImageurl);

                                Log.i("profiledownload", "profile bitmap " + bitmap);
                                HttpURLConnection connection =
                                        (HttpURLConnection) bitmap.openConnection();
//                                    connection.setHostnameVerifier(hostnameVerifier);
                                InputStream in = connection.getInputStream();
//                                    copyInputStreamToOutputStream(in, System.out);
                                connection.connect();
                                if (connection.getInputStream() != null) {
                                    InputStream inputStream = connection.getInputStream();
                                    String dir_path = Environment.getExternalStorageDirectory()
                                            + "/High Message/downloads";
                                    Log.i("profiledownload", "profile dir_path" + dir_path);
                                    File directory = new File(dir_path);
                                    if (!directory.exists())
                                        directory.mkdir();
                                    String filePath = Environment.getExternalStorageDirectory()
                                            .getAbsolutePath()
                                            + "/High Message/downloads/"
                                            + profile1;

                                    Log.d("profiledownload", "my file path is---->" + filePath);

                                    File file_name = new File(filePath);
                                    BufferedOutputStream bos = new BufferedOutputStream(
                                            new FileOutputStream(file_name));
                                    int inByte;
                                    while ((inByte = inputStream.read()) != -1) {
                                        bos.write(inByte);
                                    }
                                    bos.close();

                                    //** For  Avatar download
                                    //Start
                                    Bitmap btmap = BitmapFactory.decodeStream(inputStream);
                                    if (connection.getContentLength() <= 0 && btmap == null) {
                                        Log.i("downavatar", "avatar download faild contact name-->");

                                    } else {
                                        Log.i("downavatar", "avatar download success contact name-->");
                                    }
                                    //End
//                                        Appreference.profile_image.put(profile, filePath);
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
//                                Appreference.profile_image.put(profile, myFile.toString());
                            Log.i("profiledownload", "file exit--->");
                        }
                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

            if (Appreference.taskMultimediaDownload != null && Appreference.taskMultimediaDownload.size() > 0) {
                Iterator iterator1 = Appreference.taskMultimediaDownload.entrySet()
                        .iterator();

                while (iterator1.hasNext()) {

                    Map.Entry mapEntry = (Map.Entry) iterator1.next();

                    detailsBean = (TaskDetailsBean) mapEntry.getValue();
                    File imageFile = new File(Environment.getExternalStorageDirectory()
                            + "/High Message/downloads/" + detailsBean.getTaskDescription());
                    if (imageFile.exists()) {
                        if (detailsBean.getSignalid() != null) {
                        }
                    }
                }
            }

            if (Appreference.context_table.containsKey("taskcoversation")) {


                NewTaskConversation.getInstance().MMdownloadCompleted(detailsBean);
                /*NewTaskConversation  newTaskConversation = new NewTaskConversation();
                newTaskConversation.loadUI();*/
            }
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }
    }


   /* public void saveTextValue() {
        ArrayList<FormFieldsBean> fields_list = new ArrayList<>();
        for (int i = 0; i < fieldContainer.getChildCount(); i++) {
            LinearLayout child_view = (LinearLayout) fieldContainer
                    .getChildAt(i);

            com.myapplication3.Forms.List bean = listofFiledses.get(i);

            Log.d("My Profile",
                    "After Send button click Field ID : " + bean.getClientId()
                            + ", Field Name : "
                            + bean.getFieldName() + ", Field TagName : "
                            + bean.getFieldName() + ", Type :"
                            + bean.getFieldType() + " required  " + bean.getIsInputRequired() + " Task Description  == " + multimediaFieldValues.get(bean.getClientId()));


            if (bean != null) {
                if (bean.getFieldType().equalsIgnoreCase("photo")
                        || bean.getFieldType().equalsIgnoreCase("audio")
                        || bean.getFieldType().equalsIgnoreCase("video")
                        || bean.getFieldType().equalsIgnoreCase("signature")) {
                    String type = bean.getFieldType();
                    if (multimediaFieldValues.containsKey(bean.getClientId())) {

                        FormFieldsBean fieldTemplateBean = new FormFieldsBean();
                        fieldTemplateBean.setId(bean.getFieldId());
                        fieldTemplateBean.setFieldName(bean.getFieldName());
                        fieldTemplateBean.setFieldType(bean.getFieldType());
                        fieldTemplateBean.setFieldValue(multimediaFieldValues.get(bean.getClientId()));
                        fields_list.add(fieldTemplateBean);
                    }
                } else if (bean.getFieldType().equalsIgnoreCase("text") || bean.getFieldType().equalsIgnoreCase("numeric")) {
                    EditText ed_fieldvalue = (EditText) child_view
                            .findViewById(R.id.fieldvalue);
                    if (ed_fieldvalue.getText().toString().trim().length() != 0) {
                        FormFieldsBean fieldTemplateBean = new FormFieldsBean();
                        fieldTemplateBean.setId(bean.getFieldId());

                        fieldTemplateBean.setFieldName(bean.getFieldName());

                        fieldTemplateBean.setFieldType(bean.getFieldType());

                        if (ed_fieldvalue.getText().toString().trim()
                                .length() > 0) {

                            if (bean.getFieldType().equalsIgnoreCase("numeric")) {
                                fieldTemplateBean.setFieldValue(ed_fieldvalue
                                        .getText().toString().trim());
                            } else {
                                fieldTemplateBean.setFieldValue(ed_fieldvalue
                                        .getText().toString().trim());
                            }
                            fields_list.add(fieldTemplateBean);
                        }
                    }


                } else if (bean.getFieldType().equalsIgnoreCase("datetime")) {
                    String type = bean.getFieldType();
                    if (multimediaFieldValues.containsKey(bean.getClientId())) {

                        FormFieldsBean fieldTemplateBean = new FormFieldsBean();
                        fieldTemplateBean.setId(bean.getFieldId());
                        fieldTemplateBean.setFieldName(bean.getFieldName());
                        fieldTemplateBean.setFieldType(bean.getFieldType());
                        fieldTemplateBean.setFieldValue((multimediaFieldValues.get(bean.getClientId())));
                        fields_list.add(fieldTemplateBean);
                    }

                }

            }
        }


        showprogress();
        try {
            JSONObject[] filedlist = new JSONObject[fields_list.size()];

            int j = 0;
            for (int k = 0; k < fields_list.size(); k++) {

                FormFieldsBean listofFileds = fields_list.get(k);

                Log.d("TagsValuesInWebService",
                        "Field ID : " + listofFileds.getClientId()
                                + ", Field Name : "
                                + listofFileds.getFieldName() + ", Type :"
                                + listofFileds.getFieldType() + " list size is :  " + fields_list.size());


                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("id", listofFileds.getId());

                filedlist[k] = new JSONObject();

                filedlist[k].put("filed", jsonObject1);
                if (listofFileds.getFieldType().equalsIgnoreCase("photo") || listofFileds.getFieldType().equalsIgnoreCase("image") || listofFileds.getFieldType().equalsIgnoreCase("signature")) {
                    filedlist[k].put("fileContent", encodeTobase64(BitmapFactory.decodeFile((listofFileds.getFieldValue()))));
                    filedlist[k].put("fileExt", "png");

                } else {
                    filedlist[k].put("description", listofFileds.getFieldValue());
                }
                j++;
            }


            JSONArray listpostfiles_object = new JSONArray();
            for (int i1 = 0; i1 < filedlist.length; i1++) {
                listpostfiles_object.put(filedlist[i1]);
            }


            JSONObject jsonObject = new JSONObject();

//            JSONObject jsonObject1 = new JSONObject();

            JSONObject taskIdJson = new JSONObject();
            taskIdJson.put("id", taskId);

            JSONObject formIdJson = new JSONObject();
            formIdJson.put("id", formId);
//            jsonObject1.put("id", taskId);
            jsonObject.put("form", formIdJson);

            if (setId != null) {
                jsonObject.put("setId", setId);
            }
            jsonObject.put("task", taskIdJson);

            jsonObject.put("listValue", listpostfiles_object);

            Appreference.jsonRequestSender.setFormFieldValuesForForm(EnumJsonWebservicename.editFieldValues, jsonObject, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }*/


    public void saveTextValue() {

        ArrayList<FormFieldsBean> fields_list = new ArrayList<>();
        for (int i = 0; i < fieldContainer.getChildCount(); i++) {
            LinearLayout child_view = (LinearLayout) fieldContainer
                    .getChildAt(i);

            Log.d("Update", "listofFiledses size  is == " +listofFiledses.size());
            com.ase.Forms.List bean = listofFiledses.get(i);
            Log.d("Update", "client Id is == " + bean.getClientId());
            Log.d("Update", "Field TagName  is == " + bean.getFieldName());
            Log.d("Update", "Type is == " + bean.getFieldType());
            Log.d("Update", "value is == " + bean.getFieldValue());
            Log.d("Update", "map value is == 1 " + multimediaFieldValues.get(bean.getClientId()));

            Log.d("My Profile",
                    "After Send button click Field ID : " + bean.getClientId()
                            + ", Field Name : "
                            + bean.getFieldName() + ", Field TagName : "
                            + bean.getFieldName() + ", Type :"
                            + bean.getFieldType() + " required  " + bean.getIsInputRequired() + " Task Description  == " + multimediaFieldValues.get(bean.getClientId()));


            if (bean != null) {
                if (bean.getFieldType().equalsIgnoreCase("photo")
                        || bean.getFieldType().equalsIgnoreCase("audio")
                        || bean.getFieldType().equalsIgnoreCase("video")
                        || bean.getFieldType().equalsIgnoreCase("signature")
                        || bean.getFieldType().equalsIgnoreCase("location")) {
                    String type = bean.getFieldType();
                    if (multimediaFieldValues.containsKey(bean.getClientId()) && (!bean.getFieldType().equalsIgnoreCase("location"))) {
                        Log.i("FormsEntryViewActivity", "location save if");
                        FormFieldsBean fieldTemplateBean = new FormFieldsBean();
                        fieldTemplateBean.setId(bean.getFieldId());
                        fieldTemplateBean.setFieldName(bean.getFieldName());
                        fieldTemplateBean.setFieldType(bean.getFieldType());
                        fieldTemplateBean.setFieldValue(multimediaFieldValues.get(bean.getClientId()));
                        fields_list.add(fieldTemplateBean);
                    } else if (bean.getFieldType().equalsIgnoreCase("location")) {
                        TextView locate_name = (TextView) child_view.findViewById(R.id.fieldText);
                        Log.i("FormsEntryViewActivity", "location save if" + locate_name.getText().toString().trim());
                        FormFieldsBean fieldTemplateBean = new FormFieldsBean();
                        fieldTemplateBean.setId(bean.getFieldId());
                        fieldTemplateBean.setFieldName(bean.getFieldName());
                        fieldTemplateBean.setFieldType(bean.getFieldType());
                        fieldTemplateBean.setFieldValue(locate_name.getText().toString().trim());
                        fields_list.add(fieldTemplateBean);
                    } else {
                        Log.i("FormsEntryViewActivity", "location save if else");
                        FormFieldsBean fieldTemplateBean = new FormFieldsBean();
                        fieldTemplateBean.setId(bean.getFieldId());
                        fieldTemplateBean.setFieldName(bean.getFieldName());
                        fieldTemplateBean.setFieldType(bean.getFieldType());
                        fieldTemplateBean.setFieldValue("");
                        fields_list.add(fieldTemplateBean);
                    }
                } else if (bean.getFieldType().equalsIgnoreCase("text") || bean.getFieldType().equalsIgnoreCase("numeric")) {
                    EditText ed_fieldvalue = (EditText) child_view
                            .findViewById(R.id.fieldvalue);
                    if (ed_fieldvalue.getText().toString().trim().length() != 0) {
                        FormFieldsBean fieldTemplateBean = new FormFieldsBean();
                        fieldTemplateBean.setId(bean.getFieldId());

                        fieldTemplateBean.setFieldName(bean.getFieldName());

                        fieldTemplateBean.setFieldType(bean.getFieldType());

                        if (ed_fieldvalue.getText().toString().trim()
                                .length() > 0) {

                            if (bean.getFieldType().equalsIgnoreCase("numeric")) {
                                fieldTemplateBean.setFieldValue(ed_fieldvalue
                                        .getText().toString().trim());
                            } else {
                                fieldTemplateBean.setFieldValue(ed_fieldvalue
                                        .getText().toString().trim());
                            }
                            if(!fieldTemplateBean.getFieldValue().equalsIgnoreCase(multimediaFieldValues.get(bean.getClientId()))){
                                checked = true;
                                Log.i("forms","check .> "+checked);
                            }
                            fields_list.add(fieldTemplateBean);
                        }
                    } else {
                        FormFieldsBean fieldTemplateBean = new FormFieldsBean();
                        fieldTemplateBean.setId(bean.getFieldId());
                        fieldTemplateBean.setFieldName(bean.getFieldName());
                        fieldTemplateBean.setFieldType(bean.getFieldType());
                        fieldTemplateBean.setFieldValue("");
                        fields_list.add(fieldTemplateBean);
                    }


                } else if (bean.getFieldType().equalsIgnoreCase("datetime")) {
                    String type = bean.getFieldType();
                    Log.d("Update", "date time  is == " +multimediaFieldValues.get(bean.getClientId()));
                    if (multimediaFieldValues.containsKey(bean.getClientId())) {
                        FormFieldsBean fieldTemplateBean = new FormFieldsBean();
                        fieldTemplateBean.setId(bean.getFieldId());
                        fieldTemplateBean.setFieldName(bean.getFieldName());
                        fieldTemplateBean.setFieldType(bean.getFieldType());
                        fieldTemplateBean.setFieldValue((multimediaFieldValues.get(bean.getClientId())));
                        fields_list.add(fieldTemplateBean);
                    } else {
                        FormFieldsBean fieldTemplateBean = new FormFieldsBean();
                        fieldTemplateBean.setId(bean.getFieldId());
                        fieldTemplateBean.setFieldName(bean.getFieldName());
                        fieldTemplateBean.setFieldType(bean.getFieldType());
                        fieldTemplateBean.setFieldValue("");
                        fields_list.add(fieldTemplateBean);
                    }

                } else if (bean.getFieldType().equalsIgnoreCase("switch")) {
                    String type = bean.getFieldType();
                    if (multimediaFieldValues.containsKey(bean.getClientId())) {
                        Log.i("FormsEntryViewActivity", "saveTextValue 1 " + multimediaFieldValues.get(bean.getClientId()) + "  bean.getClientId() " + bean.getClientId());
                        FormFieldsBean fieldTemplateBean = new FormFieldsBean();
                        fieldTemplateBean.setId(bean.getFieldId());
                        fieldTemplateBean.setFieldName(bean.getFieldName());
                        fieldTemplateBean.setFieldType(bean.getFieldType());
                        fieldTemplateBean.setFieldValue((multimediaFieldValues.get(bean.getClientId())));
                        fields_list.add(fieldTemplateBean);
                    } else {
                        Log.i("FormsEntryViewActivity", "saveTextValue 2 " + multimediaFieldValues.get(bean.getFieldValue()) + "  bean.getClientId() " + bean.getClientId());
                        FormFieldsBean fieldTemplateBean = new FormFieldsBean();
                        fieldTemplateBean.setId(bean.getFieldId());
                        fieldTemplateBean.setFieldName(bean.getFieldName());
                        fieldTemplateBean.setFieldType(bean.getFieldType());
                        fieldTemplateBean.setFieldValue("No");
                        fields_list.add(fieldTemplateBean);

                    }

                }
            }
        }

            Log.i("LOOOG", "fields_list.size " + fields_list.size());
            int jjj = 0;
            for (int k = 0; k < fields_list.size(); k++) {
                FormFieldsBean listofFileds = fields_list.get(k);
                Log.i("Update", "listofFileds  map value is == 1 " + listofFileds.getFieldValue() + "   map client == " + listofFileds.getClientId());
                if (listofFileds.getFieldValue() == null || listofFileds.getFieldValue().equalsIgnoreCase("")) {
                    jjj++;
                }
                Log.i("LOOOG", "fields_list.size 1 " + fields_list.size());

            }
            Log.i("LOOOG", "fields_list.size " + jjj);

            if (fields_list.size() != jjj && checked) {
            showprogress();
                Log.i("FormEntryViewActivity", " showprogress 6 ");
                try {
                    JSONObject[] filedlist = new JSONObject[fields_list.size()];

                    int j = 0;
                    for (int k = 0; k < fields_list.size(); k++) {

                        FormFieldsBean listofFileds = fields_list.get(k);
                        Log.d("Update", "listofFileds   client Id is == 45 " + listofFileds.getFieldValue());
                        if (listofFileds.getFieldValue() != null) {

                            Log.d("Update", "listofFileds   client Id is == " + listofFileds.getClientId());
                            Log.d("Update", "listofFileds  Field TagName  is == " + listofFileds.getFieldName());
                            Log.d("Update", "listofFileds  Type is == " + listofFileds.getFieldType());
                            Log.d("Update", "listofFileds  value is == " + listofFileds.getFieldValue());
                            Log.d("Update", "listofFileds  map value is == " + multimediaFieldValues.get(listofFileds.getClientId()));

                            Log.d("TagsValuesInWebService",
                                    "Field ID : " + listofFileds.getClientId()
                                            + ", Field Name : "
                                            + listofFileds.getFieldName() + ", Type :"
                                            + listofFileds.getFieldType() + " list size is :  " + fields_list.size());


                            JSONObject jsonObject1 = new JSONObject();
                            jsonObject1.put("id", listofFileds.getId());

                            filedlist[k] = new JSONObject();

                            filedlist[k].put("filed", jsonObject1);
                            if (listofFileds.getFieldType().equalsIgnoreCase("photo") || listofFileds.getFieldType().equalsIgnoreCase("image") || listofFileds.getFieldType().equalsIgnoreCase("signature")) {
                                if (listofFileds.getFieldValue() != null && !listofFileds.getFieldValue().equalsIgnoreCase("") && !listofFileds.getFieldValue().equalsIgnoreCase("-")) {
                                    filedlist[k].put("fileContent", encodeTobase64(BitmapFactory.decodeFile((listofFileds.getFieldValue()))));
                                    filedlist[k].put("fileExt", "png");
                                } else {
                       /* filedlist[k].put("fileContent", "");
                        filedlist[k].put("fileExt", "");*/
                                }

                            } else if (listofFileds.getFieldType().equalsIgnoreCase("video")) {
                                if (listofFileds.getFieldValue() != null && !listofFileds.getFieldValue().equalsIgnoreCase("") && !listofFileds.getFieldValue().equalsIgnoreCase("-")) {
                                    filedlist[k].put("fileContent", encodeAudioVideoToBase64(listofFileds.getFieldValue()));
                                    filedlist[k].put("fileExt", "mp4");
                                } else {
                      /*  filedlist[k].put("fileContent", "");
                        filedlist[k].put("fileExt", "");*/
                                }

                            } else if (listofFileds.getFieldType().equalsIgnoreCase("audio")) {
                                if (listofFileds.getFieldValue() != null && !listofFileds.getFieldValue().equalsIgnoreCase("") && !listofFileds.getFieldValue().equalsIgnoreCase("-")) {
                                    filedlist[k].put("fileContent", encodeAudioVideoToBase64(listofFileds.getFieldValue()));
                                    filedlist[k].put("fileExt", "mp3");
                                } else {
                        /*filedlist[k].put("fileContent", "");
                        filedlist[k].put("fileExt", "");*/
                                }
                            } else {
                                filedlist[k].put("description", listofFileds.getFieldValue());
                                Log.i("FormsEntryViewActivity", "location json save" + listofFileds.getFieldValue());
                            }

                            j++;
                        }
                    }


                    JSONArray listpostfiles_object = new JSONArray();
                    for (int i1 = 0; i1 < filedlist.length; i1++) {
                        if (filedlist[i1] != null)
                            listpostfiles_object.put(filedlist[i1]);
                    }


                    JSONObject jsonObject = new JSONObject();

//            JSONObject jsonObject1 = new JSONObject();

                    JSONObject taskIdJson = new JSONObject();
                    taskIdJson.put("id", taskId);

                    JSONObject formIdJson = new JSONObject();
                    formIdJson.put("id", formId);
//            jsonObject1.put("id", taskId);
                    jsonObject.put("form", formIdJson);


                    jsonObject.put("updatedBy", Appreference.loginuserdetails.getId());
                    jsonObject.put("task", taskIdJson);

                    if (setId != null) {
                        jsonObject.put("setId", setId);
                    }

                    jsonObject.put("listValue", listpostfiles_object);
                    checked = false;
                    Appreference.jsonRequestSender.setFormFieldValuesForForm(EnumJsonWebservicename.editFieldValues, jsonObject, this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Please enter the value ", Toast.LENGTH_SHORT).show();
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

    public class bitmaploader extends AsyncTask<Uri, Void, Void> {

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            try {
                super.onPostExecute(result);
                Log.d("image", "came to post execute for image");

                Bitmap img = null;
                if (strIPath != null) {
                    img = convertpathToBitmap(strIPath);
                    Log.i("task", "extension" + strIPath);
                    String pathExtn = strIPath.split("/")[5];
                    Log.i("task", "spilting" + pathExtn);
                    pathExtn = pathExtn.substring(pathExtn.length() - 3);
                    Log.i("task", "root---->" + pathExtn);
//                        Log.i("task", "extension" + strIPath.split(".")[strIPath.split(".").length - 1]);
//                    PercentageWebService("image", strIPath, pathExtn);
                }
                if (img != null) {

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

}
