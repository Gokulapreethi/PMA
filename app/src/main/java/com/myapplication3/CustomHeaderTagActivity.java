package com.myapplication3;

/**
 * Created by saravanakumar on 11/30/2016.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.media.MediaPlayer;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.myapplication3.Bean.CustomTagBean;
import com.myapplication3.Bean.ListofFileds;
import com.myapplication3.Bean.TaskDetailsBean;
import com.myapplication3.DB.VideoCallDataBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.WebServiceInterface;


/**
 * Created by saravanakumar on 11/28/2016.
 */
public class CustomHeaderTagActivity extends Activity implements DialogInterface.OnClickListener, WebServiceInterface, AdapterView.OnItemSelectedListener {

    private static Context context;

    public LinearLayout fieldContainer = null;

    public ArrayList<ListofFileds> profileFieldList;

    private Vector<CustomTagsBean> getFieldValuesList = new Vector<CustomTagsBean>();

    private HashMap<String, String> multimediaFieldValues = new HashMap<String, String>();

    private HashMap<String, String> timeEntryValidation = new HashMap<String, String>();

    private MediaPlayer profile_player;

    private MediaPlayer audio_player;

    private int position = 0;

    public String filedStrId;

    private String type = null;

    private String strIPath = null;

    private boolean isSelect;

    private Bitmap bitmap, img = null;

    private Handler handler = new Handler();

    private Button setting = null;

    private HashMap<String, ListofFileds> fieldValuesMap = new HashMap<String, ListofFileds>();

    private int CAMERA = 32;

    private int VIDEO = 40;

//    private int AUDIO = 34;

    private SharedPreferences preferences;

    private HashMap<String, String> fileHmap = new HashMap<String, String>();

    ProgressDialog progress;

    public View view;

    static String strTime = null;

    static String st_hour;

    static String strenddate;

    static String taskId;

    static String group_owner;

    static String group_name;

    ArrayList<ListofFileds> listofFiledses;

    ArrayList<String> group_members;

    TextView send, back;

    Spinner custom_spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_header_tag_activity);

        profileFieldList = new ArrayList<>();
        fieldContainer = (LinearLayout) findViewById(R.id.fieldContainer);

        send = (TextView) findViewById(R.id.send);
        back = (TextView) findViewById(R.id.back);
        custom_spinner = (Spinner) findViewById(R.id.custom_header_spinner);
        custom_spinner.setOnItemSelectedListener(this);

        context = this;
        listofFiledses = new ArrayList<>();
        listofFiledses.clear();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showprogressforpriority("Sending...");
                saveTextValue();
            }
        });
        listofFiledses = (ArrayList<ListofFileds>) getIntent().getExtras().getSerializable("LeftList");
        group_members = (ArrayList<String>) getIntent().getExtras().getSerializable("listOfObservers");
        taskId = getIntent().getExtras().getString("TaskId");
        group_owner = getIntent().getExtras().getString("group_ownerofTask");
        group_name = getIntent().getExtras().getString("group_taskReceiver");
        Log.i("group123", "group Task ID====>" + taskId);
        Log.i("group123", "group group_owner====>" + group_owner);
        Log.i("group123", "group group_name====>" + group_name);
        for (int i = 0; i < group_members.size(); i++) {
            Log.i("group123", "group group_members====>" + group_members.get(i));
        }
        if (group_owner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername()) && group_members.size() > 1) {
            if (group_members
                    .contains(group_name))
                group_members.remove(0);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, group_members);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.custom_spinner.setAdapter(dataAdapter);
        } else {
            ArrayList<String> groupReceiver = new ArrayList<>();
            groupReceiver.add(Appreference.loginuserdetails.getUsername());
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, groupReceiver);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.custom_spinner.setAdapter(dataAdapter);
        }

        profileFieldList = listofFiledses;

        loadProfileFieldValues();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView selectedText = (TextView) parent.getChildAt(0);
        Log.i("group123", "group spinner selected group_members====>" + selectedText.toString());
        loadProfileFieldValues();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showprogressforpriority(final String name) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (progress == null || !progress.isShowing()) {
                        Log.i("login123", "inside showProgressDialog");
                        progress = new ProgressDialog(CustomHeaderTagActivity.this);
                        progress.setCancelable(false);
                        progress.setMessage(name);
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setProgress(0);
                        progress.setMax(100);
                        progress.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && fieldContainer != null) {


            try {
                LinearLayout parent_view1 = (LinearLayout) fieldContainer
                        .getChildAt(position - 1);

                final TextView value_img;

                Log.i("CustomTag", "parentid" + parent_view1);

                value_img = (TextView) parent_view1.findViewById(R.id.fieldText);


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
                    if (data != null) {
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
                            Log.i("AAA", "New activity 33*************");
                            Uri selectedImageUri = data.getData();
                            strIPath = getRealPathFromURI(selectedImageUri);
                            final String path = Environment.getExternalStorageDirectory()
                                    + "/High Message/" + getFileName() + ".mp4";

                            Log.i("AAA", "New activity " + strIPath);
                            strIPath = path;
                            Log.i("AAA", "New activity " + strIPath);

                            Log.i("mp", "mpath" + strIPath);

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
                                multimediaFieldValues.put(String.valueOf(position), strIPath);
                                String fileName = strIPath.split("/")[5];
                                value_img.setVisibility(View.VISIBLE);
                                value_img.setText(fileName);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if (requestCode == 31) {
                    if (data != null) {

                        Uri selectedImageUri = data.getData();
                        Log.d("filePath  1 ", selectedImageUri.toString());
                        final int takeFlags = data.getFlags()
                                & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        getContentResolver().takePersistableUriPermission(
                                selectedImageUri, takeFlags);
                        strIPath = Environment.getExternalStorageDirectory()
                                + "/High Message/"
                                + getFileName() + ".jpg";
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
//                       Log.i("task","extension"+strIPath.split(".")[strIPath.split(".").length-1]);
                        multimediaFieldValues.put(String.valueOf(position), strIPath);
                        String fileName = strIPath.split("/")[5];
                        value_img.setVisibility(View.VISIBLE);
                        value_img.setText(fileName);
                        Log.d("fileName  1 ", "strIPath   " + fileName);
//                        fileWebService("image");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    @Override
    public void ResponceMethod(final Object object) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("task response", "NewTaskConversation ResponceMethod");
                    CommunicationBean communicationBean = (CommunicationBean) object;
                    String server_reponse_string = communicationBean.getEmail();
                    Log.i("output123", "Send custom1 server Response===> " + server_reponse_string);

                    JsonElement jelement = new JsonParser().parse(server_reponse_string);
                    ArrayList<ListofFileds> listValues = communicationBean.getFiledsArrayList();

                    Gson gson = new Gson();
                    Log.i("rsponse", "inside the conflict response");
                    Log.i("Response conflictTask", "response " + server_reponse_string);
                    JSONArray jsonArray = new JSONArray(server_reponse_string);
                    if (jsonArray.length() > 0) {
                        Log.i("response array", "response size" + jsonArray.length());
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Log.i("response ", "objects  " + jsonArray.get(i));
                            CustomTagBean conflictCheck = gson.fromJson(jsonArray.getString(i), CustomTagBean.class);
                            if (!listValues.get(i).getDataType().equalsIgnoreCase("Photo") &&
                                    !listValues.get(i).getDataType().equalsIgnoreCase("signature") &&
                                    !listValues.get(i).getDataType().equalsIgnoreCase("audio") &&
                                    !listValues.get(i).getDataType().equalsIgnoreCase("video")) {
                                listValues.get(i).setName(conflictCheck.getValue());
                            } else {
                                listValues.get(i).setIsInputRequired(conflictCheck.getValue());
                            }
                            listValues.get(i).setCreatedBy(conflictCheck.getSetId());
                        }
                    }
                    Intent intent = new Intent();
                    intent.putExtra("Values", listValues);
                    setResult(RESULT_OK, intent);
                    finish();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void ErrorMethod(Object object) {
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
                }
                if (img != null) {
                    Log.d("OnActivity", "_____On Activity Called______");
                } else {
                    strIPath = null;
                }
            } catch (Exception e) {
                Log.e("profile", "====> " + e.getMessage());
            }
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            try {
                super.onPreExecute();
                ProgressDialog dialog = new ProgressDialog(context);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.e("profile", "====> " + e.getMessage());
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
                e.printStackTrace();
            }
            return null;
        }

    }

    public void loadProfileFieldValues() {
        try {
            if (profileFieldList.size() > 0) {
//                if (fieldContainer != null)
                fieldContainer.removeAllViews();
                String custom_receiver_now = custom_spinner.getSelectedItem().toString();
                ArrayList<TaskDetailsBean> taskDetailsBeanArrayList_start = null;
                ArrayList<TaskDetailsBean> taskDetailsBeanArrayList_end = null;
                ArrayList<TaskDetailsBean> taskDetailsBeanArrayList = new ArrayList<>();


                if (group_members.size() > 1) {
                    /*if Group*/
                    String query_start = "select * from taskDetailsInfo where taskId = " + taskId + " " +
                            "and subType = 'customeHeaderAttribute' and taskType = 'Group' and fromUserName =  '" + group_owner + "' group by customSetId order by customSetId desc limit 1";
                    TaskDetailsBean taskDetailsBean_start = null;
                    ArrayList<TaskDetailsBean> listTaskDetailsBean_start = VideoCallDataBase.getDB(context).getTaskHistory(query_start);
                    if (listTaskDetailsBean_start.size() > 0) {
                        taskDetailsBean_start = listTaskDetailsBean_start.get(0);
                    }
                    if (taskDetailsBean_start != null && taskDetailsBean_start.getCustomSetId() != 0) {
                        String query1 = "select * from taskDetailsInfo where taskId = " + taskId +
                                " and subType = 'customeHeaderAttribute' and taskType = 'Group' and fromUserName  = '" + group_owner + "'  and customSetId =" + taskDetailsBean_start.getCustomSetId();
                        taskDetailsBeanArrayList_start = VideoCallDataBase.getDB(context).getTaskHistory(query1);
                        Log.i("group123", "Group custom_first part " + taskDetailsBeanArrayList_start.size());
                    }


                    String query_end = "select * from taskDetailsInfo where taskId = " + taskId + " " +
                            "and subType = 'customeHeaderAttribute' and taskType = 'Group' and fromUserName =  '" + custom_receiver_now + "' group by customSetId order by customSetId desc limit 1";
                    TaskDetailsBean taskDetailsBean_end = null;
                    ArrayList<TaskDetailsBean> listTaskDetailsBean_end = VideoCallDataBase.getDB(context).getTaskHistory(query_end);
                    if (listTaskDetailsBean_end.size() > 0) {
                        taskDetailsBean_end = listTaskDetailsBean_end.get(0);
                    }
                    if (taskDetailsBean_end != null && taskDetailsBean_end.getCustomSetId() != 0) {
                        String query1 = "select * from taskDetailsInfo where taskId = " + taskId +
                                " and subType = 'customeHeaderAttribute' and taskType = 'Group' and fromUserName  = '" + custom_receiver_now + "'  and customSetId =" + taskDetailsBean_end.getCustomSetId();
                        if (VideoCallDataBase.getDB(context).getTaskHistory(query1) != null)
                            taskDetailsBeanArrayList_end = VideoCallDataBase.getDB(context).getTaskHistory(query1);
                        Log.i("group123", "Group custom_second part " + taskDetailsBeanArrayList_end.size());
                    }
                    if (taskDetailsBeanArrayList_start == null)
                        taskDetailsBeanArrayList = null;
                    else if (taskDetailsBeanArrayList_start != null && taskDetailsBeanArrayList_end==null)
                        taskDetailsBeanArrayList.addAll(taskDetailsBeanArrayList_start);
                    if (taskDetailsBeanArrayList_end != null)
                        taskDetailsBeanArrayList.addAll(taskDetailsBeanArrayList_end);
                } else {
                    /*if individual*/
                    String query = "select * from taskDetailsInfo where taskId = " + taskId + " and subType = 'customeHeaderAttribute' group by customSetId order by customSetId desc limit 1";

                    TaskDetailsBean taskDetailsBean = null;
                    ArrayList<TaskDetailsBean> listTaskDetailsBeen = VideoCallDataBase.getDB(context).getTaskHistory(query);
                    if (listTaskDetailsBeen.size() > 0) {
                        taskDetailsBean = listTaskDetailsBeen.get(0);
//                    send.setVisibility(View.GONE);
                    }
                    Log.i("group123", "Group Id " + taskId);

                    if (taskDetailsBean != null && taskDetailsBean.getCustomSetId() != 0) {
                        String query1 = "select * from taskDetailsInfo where taskId = " + taskId + " and subType = 'customeHeaderAttribute'  and customSetId =" + taskDetailsBean.getCustomSetId();
                        taskDetailsBeanArrayList = VideoCallDataBase.getDB(context).getTaskHistory(query1);
                    }
                }
                if (taskDetailsBeanArrayList == null || taskDetailsBeanArrayList.size() == 0) {

                    int fied = 0;
                    for (ListofFileds fieldtemplate : profileFieldList) {
                        if (fieldtemplate.getName() == null) {
                            Log.i("group123", "Custom Fields ************-------------->>>>>>> " + fieldtemplate.getTagName());
                            fieldtemplate.setName(fieldtemplate.getTagName());
                        }

                        inflateFields(fied, fieldtemplate);
                        Log.d("My Profile",
                                "Field ID : " + fieldtemplate.getClientId()
                                        + ", Field Name : "
                                        + fieldtemplate.getName() + ", Field TagName : "
                                        + fieldtemplate.getTagName() + ", Type :"
                                        + fieldtemplate.getDataType() + " required  " + fieldtemplate.getIsInputRequired());
                        fied++;
                    }
                } else {
                    int j = 0;
                    for (int i = 0; i < profileFieldList.size(); i++) {
                        ListofFileds listofFileds = profileFieldList.get(i);

                        if (listofFileds.getName() == null) {
                            listofFileds.setName(listofFileds.getTagName());
                        }
                        int total = taskDetailsBeanArrayList.size();
                        if (j < total) {
                            TaskDetailsBean taskDetailsBean1 = taskDetailsBeanArrayList.get(j);
                            if (listofFileds.getId().equalsIgnoreCase(String.valueOf(taskDetailsBean1.getCustomTagId()))) {
                                Log.d("My Profile",
                                        "Exist Field ID : " + listofFileds.getId() + ", " +
                                                "Field Name : " + listofFileds.getName() + ", " +
                                                "Field TagName : " + listofFileds.getTagName() + "," +
                                                " Type :" + listofFileds.getDataType() + "," +
                                                " required ==> " + listofFileds.getIsInputRequired() + ","+
                                                " Task Description == " + taskDetailsBean1.getTaskDescription() +"'"+
                                                " tag id " + taskDetailsBean1.getCustomTagId());

                                if (taskDetailsBean1 != null) {
                                    inflateFieldsExist(i, listofFileds, taskDetailsBean1);
                                }
                                j++;
                            } else {
                                inflateFieldsExist(i, listofFileds, new TaskDetailsBean());
                            }
                        } else {
                            inflateFieldsExist(i, listofFileds, new TaskDetailsBean());
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void inflateFields(int mode, final ListofFileds bean) {
        try {
            View view = getLayoutInflater().inflate(
                    R.layout.custom_tag_adapter, fieldContainer, false);
            view.setTag(bean.getClientId());
            final TextView fieldName = (TextView) view.findViewById(R.id.fieldname);
            if (bean.getName() != null) {
                fieldName.setText(bean.getName());
            }

            if (bean.getDataType().equalsIgnoreCase("photo") || bean.getDataType().equalsIgnoreCase("image") || bean.getDataType().equalsIgnoreCase("signature")) {
                ImageView options = (ImageView) view.findViewById(R.id.imageButton);
                final TextView fileName = (TextView) view.findViewById(R.id.fieldText);
                fileName.setVisibility(View.VISIBLE);
                options.setVisibility(View.VISIBLE);

//                fileName.setText("image");
                options.setTag(fieldContainer.getChildCount());
                options.setContentDescription(bean.getDataType());
                fieldValuesMap.put(bean.getName(), bean);
                options.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        if (bean.getDataType().equalsIgnoreCase("signature")) {
                            position = Integer.parseInt(bean.getClientId());
                            Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                            startActivityForResult(i, 423);
                        } else {

                            int pos = (Integer) v.getTag();
                            Log.d("Profile", "On click of option menu");
                            showMultimediaOptions(pos, bean.getClientId(), v
                                    .getContentDescription().toString());
                        }
                    }
                });

                fileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ImageName = fileName.getText().toString();
                        Log.i("Task", "imagename" + ImageName);
                        File file = null;
                        if (bean.getDataType().equalsIgnoreCase("photo") || bean.getDataType().equalsIgnoreCase("signature")) {
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
                            if (bean.getDataType().equalsIgnoreCase("photo") || bean.getDataType().equalsIgnoreCase("signature")) {
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


                options.setVisibility(View.VISIBLE);
            } else if (bean.getDataType().equalsIgnoreCase("datetime") && bean.getIsInputRequired().equalsIgnoreCase("no")) {
                Log.i("output123", "Date Setting .......");
                final Button add = (Button) view.findViewById(R.id.fieldButton);
                final TextView fileName = (TextView) view.findViewById(R.id.fieldText);
                fileName.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);

             /*   LinearLayout Total_activity_view = (LinearLayout) fieldContainer
                        .getChildAt(20 - 1);
                EditText ed_Activity = (EditText) Total_activity_view
                        .findViewById(R.id.fieldvalue);
                ed_Activity.setEnabled(false);
                ed_Activity.setInputType(InputType.TYPE_NULL);
                ed_Activity.setFocusable(false);
                LinearLayout Total_travel_view = (LinearLayout) fieldContainer
                        .getChildAt(19 - 1);
                EditText ed_travel = (EditText) Total_travel_view
                        .findViewById(R.id.fieldvalue);
                ed_travel.setEnabled(false);
                ed_travel.setInputType(InputType.TYPE_NULL);
                ed_travel.setFocusable(false);
*/

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        boolean startTime1 = false, endTime1 = false, activitySTime = false, activityETime = false;
                        boolean startTime2 = false, endTime2 = false;

                        if (multimediaFieldValues != null && multimediaFieldValues.containsKey("10")) {
                            //Travel Start Time 1
                            startTime1 = true;
//                            Toast.makeText(context,"Please Fill Travel Start Time",Toast.LENGTH_LONG).show();
                        }
                        if (multimediaFieldValues != null && multimediaFieldValues.containsKey("11")) {
                            //Travel End Time 1
                            endTime1 = true;
//                            Toast.makeText(context,"Please Fill Travel Start Time",Toast.LENGTH_LONG).show();
                        }
                        if (multimediaFieldValues != null && multimediaFieldValues.containsKey("12")) {
                            //Activity Start Time
                            activitySTime = true;
//                            Toast.makeText(context,"Please Fill Travel Start Time",Toast.LENGTH_LONG).show();
                        }
                        if (multimediaFieldValues != null && multimediaFieldValues.containsKey("13")) {
                            //Activity End Time
                            activityETime = true;
//                            Toast.makeText(context,"Please Fill Travel Start Time",Toast.LENGTH_LONG).show();
                        }
                        if (multimediaFieldValues != null && multimediaFieldValues.containsKey("16")) {
                            //Travel Start Time 2
                            startTime2 = true;
//                            Toast.makeText(context,"Please Fill Travel Start Time",Toast.LENGTH_LONG).show();
                        }
                        if (multimediaFieldValues != null && multimediaFieldValues.containsKey("17")) {
                            //Travel Start Time 2
                            endTime2 = true;
//                            Toast.makeText(context,"Please Fill Travel Start Time",Toast.LENGTH_LONG).show();
                        }
                        if (bean.getClientId().equalsIgnoreCase("10")) {
                            if (!activitySTime) {
                                Log.i("setbutton", "inflateFields---10--- getid-->" + bean.getId());
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentDateandTime = sdf.format(new Date());
                                Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime);
                                fileName.setText(currentDateandTime);

                                add.setVisibility(View.INVISIBLE);

                                multimediaFieldValues.put(bean.getId(), currentDateandTime);
                            } else {
                                Toast.makeText(context, "You Already Start Activity Time", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (bean.getClientId().equalsIgnoreCase("11")) {
                            Log.i("setbutton", "inflateFields---11--- getid-->" + bean.getId());
                            if (startTime1) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentDateandTime = sdf.format(new Date());
                                Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime);
                                fileName.setText(currentDateandTime);

                                add.setVisibility(View.INVISIBLE);

                                multimediaFieldValues.put(bean.getId(), currentDateandTime);
                            } else {
                                Toast.makeText(context, "Please Fill Travel Start Time", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (bean.getClientId().equalsIgnoreCase("12")) {
                            Log.i("setbutton", "inflateFields---12---getid-->" + bean.getId());

                            if (startTime1 && endTime1) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentDateandTime = sdf.format(new Date());
                                Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime);
                                fileName.setText(currentDateandTime);

                                add.setVisibility(View.INVISIBLE);

                                multimediaFieldValues.put(bean.getId(), currentDateandTime);
                            } else {
                                Toast.makeText(context, "Please Fill Travel Start & End Time", Toast.LENGTH_LONG).show();
                            }
//                            }
                        }
                        if (bean.getClientId().equalsIgnoreCase("13")) {
                            Log.i("setbutton", "inflateFields---13---getid-->" + bean.getId());
                            if (startTime1 && endTime1 && activitySTime) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentDateandTime = sdf.format(new Date());
                                Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime);
                                fileName.setText(currentDateandTime);

                                add.setVisibility(View.INVISIBLE);

                                multimediaFieldValues.put(bean.getId(), currentDateandTime);

                                if (fieldContainer != null) {
                                    LinearLayout parent_view1 = (LinearLayout) fieldContainer
                                            .getChildAt(12 - 1);
                                    if (parent_view1 != null) {
                                        Log.i("compute", "click activitystart time parent_view1!=null");
                                        TextView fieldValue = (TextView) parent_view1
                                                .findViewById(R.id.fieldText);
                                        if (fieldValue != null && fieldValue.getText().toString().length() > 0) {
                                            Log.i("compute", "click activitystart time-->" + fieldValue.getText().toString());

                                            try {

                                                Date date1 = sdf.parse(fieldValue.getText().toString());
                                                Date date2 = sdf.parse(currentDateandTime);
                                                if (DayHourMinSec(date1, date2, null) != null) {
                                                    Log.i("compute", "click activitystart time DayHourMinSec(date1, date2) != null");
                                                    Log.i("compute", "click activitystart time DayHourMinSec(date1, date2)--->" + DayHourMinSec(date1, date2, null));
                                                    LinearLayout parent_view2 = (LinearLayout) fieldContainer
                                                            .getChildAt(20 - 1);
                                                    String Total_Activity_hrs = DayHourMinSec(date1, date2, null);
                                                    EditText fieldValue1 = (EditText) parent_view2
                                                            .findViewById(R.id.fieldvalue);

                                                    Log.i("compute", "******************");
                                                    Log.i("compute", "Activity Hours-->" + Total_Activity_hrs);
                                                    fieldValue1.setText(Total_Activity_hrs);
                                                    fieldValue1.setFocusable(false);
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }

                            } else {
                                Toast.makeText(context, "Please Fill Activity Start Time", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (bean.getClientId().equalsIgnoreCase("16")) {
                            Log.i("setbutton", "inflateFields---16---getid-->" + bean.getId());
                            if (startTime1 && endTime1 && activitySTime && activityETime) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentDateandTime = sdf.format(new Date());
                                Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime);
                                fileName.setText(currentDateandTime);

                                add.setVisibility(View.INVISIBLE);

                                multimediaFieldValues.put(bean.getId(), currentDateandTime);
                            } else {
                                Toast.makeText(context, "Please Fill Activity Start,End Time", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (bean.getClientId().equalsIgnoreCase("17")) {
                            Log.i("setbutton", "inflateFields---17---getid-->" + bean.getId());
                            if (startTime1 && endTime1 && activitySTime && activityETime && startTime2) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentDateandTime = sdf.format(new Date());
                                Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime);
                                fileName.setText(currentDateandTime);

                                add.setVisibility(View.INVISIBLE);

                                multimediaFieldValues.put(bean.getId(), currentDateandTime);

                                if (fieldContainer != null) {
                                    try {
                                        LinearLayout parent_view1 = (LinearLayout) fieldContainer
                                                .getChildAt(10 - 1);
                                        if (parent_view1 != null) {
                                            Log.i("compute", "click Travel End time parent_view1!=null");
                                            TextView fieldValue1 = (TextView) parent_view1
                                                    .findViewById(R.id.fieldText);
                                            LinearLayout parent_view2 = (LinearLayout) fieldContainer
                                                    .getChildAt(11 - 1);
                                            if (parent_view2 != null) {
                                                Log.i("compute", "click Travel End time parent_view2!=null");
                                                TextView fieldValue2 = (TextView) parent_view2
                                                        .findViewById(R.id.fieldText);
                                                if (fieldValue1 != null && fieldValue1.getText().length() > 0 && fieldValue2 != null &&
                                                        fieldValue2.getText().length() > 0) {
                                                    Log.i("compute", "click Travel End time fieldValue1!=null && fieldValue2!=null");
                                                    Date date1 = sdf.parse(fieldValue1.getText().toString());
                                                    Date date2 = sdf.parse(fieldValue2.getText().toString());
                                                    if (DayHourMinSec(date1, date2, null) != null) {
                                                        Log.i("compute", "click Travel End time DayHourMinSec(date1, date2) != null");
                                                        String firstDateTime = DayHourMinSec(date1, date2, null);
                                                        Log.i("compute", "firstDateTime-->" + firstDateTime);
                                                        LinearLayout parent_view3 = (LinearLayout) fieldContainer
                                                                .getChildAt(16 - 1);
                                                        if (parent_view3 != null) {
                                                            Log.i("compute", "click Travel End time parent_view3!=null");
                                                            TextView fieldValue3 = (TextView) parent_view3
                                                                    .findViewById(R.id.fieldText);
                                                            if (fieldValue3 != null && fieldValue3.getText().length() > 0) {
                                                                Log.i("compute", "click Travel End time fieldValue3!=null");
                                                                Date date3 = sdf.parse(fieldValue3.getText().toString());
                                                                Date date4 = sdf.parse(currentDateandTime);
                                                                if (DayHourMinSec(date3, date4, firstDateTime) != null) {
                                                                    Log.i("compute", "click Travel End time DayHourMinSec(date3, date4) != null");
                                                                    String secondDateTime = DayHourMinSec(date3, date4, firstDateTime);
                                                                    Log.i("compute", "SecondTime-->" + secondDateTime);

                                                                    LinearLayout parent_view4 = (LinearLayout) fieldContainer
                                                                            .getChildAt(19 - 1);
                                                                    EditText fieldValue4 = (EditText) parent_view4
                                                                            .findViewById(R.id.fieldvalue);
                                                                    fieldValue4.setText(secondDateTime);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }
                            } else {
                                Toast.makeText(context, "Please Fill Travel Start and Activity Start,End Time", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });
            } else if (bean.getDataType().equalsIgnoreCase("numeric")) {
                final EditText fieldValue = (EditText) view
                        .findViewById(R.id.fieldvalue);
                fieldValue.setVisibility(View.VISIBLE);
                fieldValue.setTag(bean.getDataType());
                fieldValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                fieldValue.setContentDescription(bean.getClientId());
                fieldValuesMap.put(bean.getName(), bean);
//                fieldValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                if (bean.getDataType() != null) {
                    if (bean.getName().equalsIgnoreCase(" in Km")) {
                        fieldValue.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // TODO Auto-generated method stub
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                                    changeType(fieldValue);
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });
                    }
                }
            } else if (bean.getDataType().equalsIgnoreCase("text")) {

                final EditText fieldValue = (EditText) view
                        .findViewById(R.id.fieldvalue);
                fieldValue.setVisibility(View.VISIBLE);
                fieldValue.setTag(bean.getDataType());
                fieldValue.setContentDescription(bean.getClientId());
                fieldValuesMap.put(bean.getName(), bean);
                fieldValue.setCursorVisible(false);

                fieldValue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fieldValue.setCursorVisible(true);
                    }
                });
//                fieldValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                if (bean.getDataType() != null) {
                    if (bean.getName().equalsIgnoreCase("Birth Day")) {
                        fieldValue.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // TODO Auto-generated method stub
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                                    changeType(fieldValue);
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });

                    } else if (bean.getDataType().equalsIgnoreCase("number"))
                        fieldValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                    else if (bean.getDataType().equalsIgnoreCase("email"))
                        fieldValue
                                .setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                }

            } else if (bean.getDataType().equalsIgnoreCase("datetime") && bean.getIsInputRequired().equalsIgnoreCase("yes")) {

                final TextView date = (TextView) view.findViewById(R.id.fieldText);
                date.setVisibility(View.VISIBLE);
                date.setText(bean.getName());
                date.setTag(fieldContainer.getChildCount());
                date.setContentDescription(bean.getDataType());
                fieldValuesMap.put(bean.getName(), bean);
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int integer = (Integer) v.getTag();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime + "position is ===   " + bean.getClientId());
                        showCustomDatePicker(currentDateandTime, bean.getClientId());


//                        multimediaFieldValues.put(bean.getClientId(), currentDateandTime);
//                        date.setText(strenddate);
                    }
                });
            } else if (bean.getDataType().equalsIgnoreCase("audio")) {

                ImageView options = (ImageView) view.findViewById(R.id.imageButton);
                final TextView fileName = (TextView) view.findViewById(R.id.fieldText);
                fileName.setVisibility(View.VISIBLE);
                options.setBackground(getResources().getDrawable(R.drawable.ic_audio_file_100));
                options.setVisibility(View.VISIBLE);
//                fileName.setText("image");
                options.setTag(fieldContainer.getChildCount());
                options.setContentDescription(bean.getDataType());
                fieldValuesMap.put(bean.getName(), bean);
                options.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        int pos = (Integer) v.getTag();
                        Log.d("Profile", "On click of option menu");
                        try {
                            position = Integer.parseInt(bean.getClientId());
                            Intent i = new Intent(CustomHeaderTagActivity.this, AudioRecorder.class);
                            i.putExtra("task", "audio");
                            startActivityForResult(i, 333);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

                fileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                options.setVisibility(View.VISIBLE);
            } else if (bean.getDataType().equalsIgnoreCase("video")) {

                ImageView options = (ImageView) view.findViewById(R.id.imageButton);
                final TextView fileName = (TextView) view.findViewById(R.id.fieldText);
                fileName.setVisibility(View.VISIBLE);
                options.setVisibility(View.VISIBLE);
//                fileName.setText("image");
                options.setTag(fieldContainer.getChildCount());
                options.setContentDescription(bean.getDataType());
                fieldValuesMap.put(bean.getName(), bean);
                options.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        int pos = (Integer) v.getTag();
                        Log.d("Profile", "On click of option menu");
                        showMultimediaOptions(pos, bean.getClientId(), v
                                .getContentDescription().toString());
                    }
                });

                fileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ImageName = fileName.getText().toString();
                        Log.i("Task", "imagename" + ImageName);
                        File file = null;

                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                        if (file.exists()) {
                            Intent intent = new Intent(context, VideoPlayer.class);
                            intent.putExtra("video", file.getAbsolutePath());
                            context.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + ImageName);
                            if (file1.exists()) {
                                Intent intent = new Intent(context, VideoPlayer.class);
                                intent.putExtra("video", file1.getAbsolutePath());
                                context.startActivity(intent);
                            }
                        }
                    }
                });


                options.setVisibility(View.VISIBLE);
            }

            fieldContainer.addView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void inflateFieldsExist(int mode, final ListofFileds bean, final TaskDetailsBean taskDetailsBean) {
        try {

            View view = getLayoutInflater().inflate(
                    R.layout.custom_tag_adapter, fieldContainer, false);
            view.setTag(bean.getClientId());
            final TextView fieldName = (TextView) view.findViewById(R.id.fieldname);
//            if (bean.getName() != null && taskDetailsBean.getTaskDescription() != null) {
            if (bean != null) {
                Log.i("task", "description-4" + bean.getName());
                fieldName.setText(bean.getName());
                Log.i("output123", "fieldName ******===>****" + bean.getName());
            }

            Log.d("My Profile",
                    "Field ID : " + bean.getClientId()
                            + ", Field Name : "
                            + bean.getName() + ", Field TagName : "
                            + bean.getTagName() + ", Type :"
                            + bean.getDataType() + " required  " + bean.getIsInputRequired() + " Task Description " + taskDetailsBean.getTaskDescription() + " tag id " + taskDetailsBean.getCustomTagId());


            if (bean.getDataType().equalsIgnoreCase("photo") ||
                    bean.getDataType().equalsIgnoreCase("image") ||
                    bean.getDataType().equalsIgnoreCase("signature")) {
                ImageView options = (ImageView) view.findViewById(R.id.imageButton);
                final TextView fileName = (TextView) view.findViewById(R.id.fieldText);
                fileName.setVisibility(View.VISIBLE);
                options.setVisibility(View.VISIBLE);
                fileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ImageName = fileName.getText().toString();
                        Log.i("Task", "imagename" + ImageName);
                        File file = null;
                        Log.i("output123", "path existing media=====================>" + ImageName);
                        if (bean.getDataType().equalsIgnoreCase("photo") || bean.getDataType().equalsIgnoreCase("signature")) {
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
                            if (bean.getDataType().equalsIgnoreCase("photo") || bean.getDataType().equalsIgnoreCase("signature")) {
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
                options.setContentDescription(bean.getDataType());
                Log.i("output123", "getFromUserName====>*****" + taskDetailsBean.getFromUserName());
                Log.i("output123", "getLoginusername====>*****" + Appreference.loginuserdetails.getUsername());

//                if (taskDetailsBean != null && taskDetailsBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
//                    fileName.setText(taskDetailsBean.getTaskDescription().split("/")[5]);
//                    multimediaFieldValues.put(bean.getClientId(), taskDetailsBean.getTaskDescription());
//                }
//                else {
                if (taskDetailsBean.getTaskDescription() != null) {
                    if (taskDetailsBean.getTaskDescription().contains(Environment.getExternalStorageDirectory().getAbsolutePath()))
                        fileName.setText(taskDetailsBean.getTaskDescription().split("/")[5]);
                    else
                        fileName.setText(taskDetailsBean.getTaskDescription());
//                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + taskDetailsBean.getTaskDescription();
                    multimediaFieldValues.put(bean.getClientId(), taskDetailsBean.getTaskDescription());
                }
                fieldValuesMap.put(bean.getName(), bean);
                options.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        if (bean.getDataType().equalsIgnoreCase("signature")) {
                            position = Integer.parseInt(bean.getClientId());
                            Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                            startActivityForResult(i, 423);
                        } else {

                            int pos = (Integer) v.getTag();
                            Log.d("Profile", "On click of option menu");
                            showMultimediaOptions(pos, bean.getClientId(), v
                                    .getContentDescription().toString());
                        }
                    }
                });


                options.setVisibility(View.VISIBLE);
            } else if (bean.getDataType().equalsIgnoreCase("datetime") && bean.getIsInputRequired().equalsIgnoreCase("no")) {
                final Button add = (Button) view.findViewById(R.id.fieldButton);
                final TextView fileName = (TextView) view.findViewById(R.id.fieldText);
                fileName.setVisibility(View.VISIBLE);
                add.setVisibility(View.VISIBLE);
                if (taskDetailsBean != null) {
                    fileName.setText(taskDetailsBean.getTaskDescription());
                    multimediaFieldValues.put(bean.getClientId(), taskDetailsBean.getTaskDescription());
                }

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean startTime1 = false, endTime1 = false, activitySTime = false, activityETime = false;
                        boolean startTime2 = false, endTime2 = false;
                        if (fieldContainer != null) {
                            LinearLayout parent_view10 = (LinearLayout) fieldContainer
                                    .getChildAt(10 - 1);
                            if (parent_view10 != null) {
                                TextView fieldValue10 = (TextView) parent_view10
                                        .findViewById(R.id.fieldText);
                                if (fieldValue10 != null && fieldValue10.getText().length() > 0) {
                                    startTime1 = true;
                                }
                            }

                            LinearLayout parent_view11 = (LinearLayout) fieldContainer
                                    .getChildAt(11 - 1);
                            if (parent_view11 != null) {
                                TextView fieldValue11 = (TextView) parent_view11
                                        .findViewById(R.id.fieldText);
                                if (fieldValue11 != null && fieldValue11.getText().toString().length() > 0) {
                                    endTime1 = true;
                                }
                            }

                            LinearLayout parent_view12 = (LinearLayout) fieldContainer
                                    .getChildAt(12 - 1);
                            if (parent_view12 != null) {
                                TextView fieldValue12 = (TextView) parent_view12
                                        .findViewById(R.id.fieldText);
                                if (fieldValue12 != null && fieldValue12.getText().length() > 0) {
                                    activitySTime = true;
                                }
                            }

                            LinearLayout parent_view13 = (LinearLayout) fieldContainer
                                    .getChildAt(13 - 1);
                            if (parent_view13 != null) {
                                TextView fieldValue13 = (TextView) parent_view13
                                        .findViewById(R.id.fieldText);
                                if (fieldValue13 != null && fieldValue13.getText().length() > 0) {
                                    activityETime = true;
                                }
                            }

                            LinearLayout parent_view14 = (LinearLayout) fieldContainer
                                    .getChildAt(16 - 1);
                            if (parent_view14 != null) {
                                TextView fieldValue14 = (TextView) parent_view14
                                        .findViewById(R.id.fieldText);
                                if (fieldValue14 != null && fieldValue14.getText().length() > 0) {
                                    startTime2 = true;
                                }
                            }

                            LinearLayout parent_view15 = (LinearLayout) fieldContainer
                                    .getChildAt(17 - 1);
                            if (parent_view15 != null) {
                                TextView fieldValue15 = (TextView) parent_view15
                                        .findViewById(R.id.fieldText);
                                if (fieldValue15 != null && fieldValue15.getText().length() > 0) {
                                    endTime2 = true;
                                }
                            }
                        }
                        Log.i("setbutton", "---currentClient ID---" + bean.getClientId());

                        if (bean.getClientId().equalsIgnoreCase("10")) {
                            /*Travel Start Time 1*/
                            Log.i("setbutton", "---11---");
                        }
                        if (bean.getClientId().equalsIgnoreCase("11")) {
                           /* Travel End Time 1*/
                            Log.i("setbutton", "---12---");
                        }
                        if (bean.getClientId().equalsIgnoreCase("12")) {
                            /*Activity start Time*/
                            Log.i("setbutton", "---13---");
                        }
                        if (bean.getClientId().equalsIgnoreCase("13")) {
                            /*Activity End Time*/
                            Log.i("setbutton", "---14---");
                        }
                        if (bean.getClientId().equalsIgnoreCase("16")) {
                            /*Travel Start Time 2*/
                            Log.i("setbutton", "---16---");
                        }
                        if (bean.getClientId().equalsIgnoreCase("17")) {
                           /* Travel End Time 2*/
                            Log.i("setbutton", "---17---");
                        }

                        if (timeEntryValidation != null && timeEntryValidation.containsKey("10")) {
                            startTime1 = true;
                        }
                        if (timeEntryValidation != null && timeEntryValidation.containsKey("11")) {
                            endTime1 = true;
                        }
                        if (timeEntryValidation != null && timeEntryValidation.containsKey("12")) {
                            activitySTime = true;
                        }
                        if (timeEntryValidation != null && timeEntryValidation.containsKey("13")) {
                            activityETime = true;
                        }
                        if (timeEntryValidation != null && timeEntryValidation.containsKey("16")) {
                            startTime2 = true;
                        }
                        if (timeEntryValidation != null && timeEntryValidation.containsKey("17")) {
                            endTime2 = true;
                        }
                        if (bean.getClientId().equalsIgnoreCase("10")) {
                            if (!activitySTime) {
                                Log.i("setbutton", "inflateFields---10--- getid-->" + bean.getId());
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentDateandTime = sdf.format(new Date());
                                Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime);
                                fileName.setText(currentDateandTime);

                                add.setVisibility(View.INVISIBLE);

                                multimediaFieldValues.put(bean.getId(), currentDateandTime);
                                timeEntryValidation.put(bean.getId(), currentDateandTime);
                            } else {
                                Toast.makeText(context, "You Already Start Activity Time", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (bean.getClientId().equalsIgnoreCase("11")) {
                            Log.i("setbutton", "inflateFields---11--- getid-->" + bean.getId());
                            if (startTime1) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentDateandTime = sdf.format(new Date());
                                Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime);
                                fileName.setText(currentDateandTime);

                                add.setVisibility(View.INVISIBLE);

                                multimediaFieldValues.put(bean.getId(), currentDateandTime);
                                timeEntryValidation.put(bean.getId(), currentDateandTime);
                            } else {
                                Toast.makeText(context, "Please Fill Travel Start Time", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (bean.getClientId().equalsIgnoreCase("12")) {
                            Log.i("setbutton", "inflateFields---12---getid-->" + bean.getId());
                            if (startTime1 && endTime1) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentDateandTime = sdf.format(new Date());
                                Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime);
                                fileName.setText(currentDateandTime);

                                add.setVisibility(View.INVISIBLE);

                                multimediaFieldValues.put(bean.getId(), currentDateandTime);
                                timeEntryValidation.put(bean.getId(), currentDateandTime);
                            } else {
                                Toast.makeText(context, "Please Fill Travel Start & End Time", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (bean.getClientId().equalsIgnoreCase("13")) {
                            Log.i("setbutton", "inflateFields---13---getid-->" + bean.getId());
                            if (startTime1 && endTime1 && activitySTime) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentDateandTime = sdf.format(new Date());
                                Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime);
                                fileName.setText(currentDateandTime);

                                add.setVisibility(View.INVISIBLE);

                                multimediaFieldValues.put(bean.getId(), currentDateandTime);
                                timeEntryValidation.put(bean.getId(), currentDateandTime);

                                if (fieldContainer != null) {
                                    LinearLayout parent_view1 = (LinearLayout) fieldContainer
                                            .getChildAt(12 - 1);
                                    if (parent_view1 != null) {
                                        Log.i("compute", "Field Exist click activitystart time parent_view1!=null");
                                        TextView fieldValue = (TextView) parent_view1
                                                .findViewById(R.id.fieldText);
                                        if (fieldValue != null && fieldValue.getText().length() > 0) {
                                            Log.i("compute", "Field Exist click activitystart time-->" + fieldValue.getText().toString());
                                            try {
                                                Date date1 = sdf.parse(fieldValue.getText().toString());
                                                Date date2 = sdf.parse(currentDateandTime);
                                                if (DayHourMinSec(date1, date2, null) != null) {
                                                    Log.i("compute", "Field Exist click activitystart time DayHourMinSec(date1, date2) != null");
                                                    Log.i("compute", "Field Exist click activitystart time DayHourMinSec(date1, date2)--->" + DayHourMinSec(date1, date2, null));
                                                    LinearLayout parent_view2 = (LinearLayout) fieldContainer
                                                            .getChildAt(20 - 1);
                                                    EditText fieldValue1 = (EditText) parent_view2
                                                            .findViewById(R.id.fieldvalue);
                                                    fieldValue1.setText(DayHourMinSec(date1, date2, null));
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Please Fill Activity Start Time", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (bean.getClientId().equalsIgnoreCase("16")) {
                            Log.i("setbutton", "inflateFields---16---getid-->" + bean.getId());
                            if (startTime1 && endTime1 && activitySTime && activityETime) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentDateandTime = sdf.format(new Date());
                                Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime);
                                fileName.setText(currentDateandTime);

                                add.setVisibility(View.INVISIBLE);

                                multimediaFieldValues.put(bean.getId(), currentDateandTime);
                                timeEntryValidation.put(bean.getId(), currentDateandTime);
                            } else {
                                Toast.makeText(context, "Please Fill Activity Start,End Time", Toast.LENGTH_LONG).show();
                            }
                        }
                        if (bean.getClientId().equalsIgnoreCase("17")) {
                            Log.i("setbutton", "inflateFields---17---getid-->" + bean.getId());
                            if (startTime1 && endTime1 && activitySTime && activityETime && startTime2) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String currentDateandTime = sdf.format(new Date());
                                Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime);
                                fileName.setText(currentDateandTime);

                                add.setVisibility(View.INVISIBLE);

                                multimediaFieldValues.put(bean.getId(), currentDateandTime);
                                timeEntryValidation.put(bean.getId(), currentDateandTime);

                                if (fieldContainer != null) {
                                    try {
                                        LinearLayout parent_view1 = (LinearLayout) fieldContainer
                                                .getChildAt(10 - 1);
                                        if (parent_view1 != null) {
                                            Log.i("compute", "Field Exist click Travel End time parent_view1!=null");
                                            TextView fieldValue1 = (TextView) parent_view1
                                                    .findViewById(R.id.fieldText);
                                            LinearLayout parent_view2 = (LinearLayout) fieldContainer
                                                    .getChildAt(11 - 1);
                                            if (parent_view2 != null) {
                                                Log.i("compute", "Field Exist click Travel End time parent_view2!=null");
                                                TextView fieldValue2 = (TextView) parent_view2
                                                        .findViewById(R.id.fieldText);
                                                if (fieldValue1 != null && fieldValue1.getText().length() > 0 && fieldValue2 != null &&
                                                        fieldValue2.getText().length() > 0) {
                                                    Log.i("compute", "Field Exist click Travel End time fieldValue1!=null && fieldValue2!=null");
                                                    Date date1 = sdf.parse(fieldValue1.getText().toString());
                                                    Date date2 = sdf.parse(fieldValue2.getText().toString());
                                                    if (DayHourMinSec(date1, date2, null) != null) {
                                                        Log.i("compute", "Field Exist click Travel End time DayHourMinSec(date1, date2) != null");
                                                        String firstDateTime = DayHourMinSec(date1, date2, null);
                                                        Log.i("compute", "Field Exist firstDateTime-->" + firstDateTime);
                                                        LinearLayout parent_view3 = (LinearLayout) fieldContainer
                                                                .getChildAt(16 - 1);
                                                        if (parent_view3 != null) {
                                                            Log.i("compute", "Field Exist click Travel End time parent_view3!=null");
                                                            TextView fieldValue3 = (TextView) parent_view3
                                                                    .findViewById(R.id.fieldText);
                                                            if (fieldValue3 != null && fieldValue3.getText().length() > 0) {
                                                                Log.i("compute", "Field Exist click Travel End time fieldValue3!=null");
                                                                Date date3 = sdf.parse(fieldValue3.getText().toString());
                                                                Date date4 = sdf.parse(currentDateandTime);
                                                                if (DayHourMinSec(date3, date4, firstDateTime) != null) {
                                                                    Log.i("compute", "Field Exist click Travel End time DayHourMinSec(date3, date4) != null");
                                                                    String secondDateTime = DayHourMinSec(date3, date4, firstDateTime);
                                                                    Log.i("compute", "Field Exist SecondTime-->" + secondDateTime);

                                                                    LinearLayout parent_view4 = (LinearLayout) fieldContainer
                                                                            .getChildAt(19 - 1);
                                                                    EditText fieldValue4 = (EditText) parent_view4
                                                                            .findViewById(R.id.fieldvalue);
                                                                    fieldValue4.setText(secondDateTime);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Please Fill Travel Start and Activity Start,End Time", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            } else if (bean.getDataType().equalsIgnoreCase("numeric")) {
                final EditText fieldValue = (EditText) view
                        .findViewById(R.id.fieldvalue);
                fieldValue.setVisibility(View.VISIBLE);
                fieldValue.setTag(bean.getDataType());
                fieldValue.setInputType(InputType.TYPE_CLASS_NUMBER);

                fieldValue.setContentDescription(bean.getClientId());
                fieldValuesMap.put(bean.getName(), bean);
//                fieldValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                if (taskDetailsBean != null) {
                    fieldValue.setText(taskDetailsBean.getTaskDescription());
                    multimediaFieldValues.put(bean.getClientId(), taskDetailsBean.getTaskDescription());
                }
                if (bean.getDataType() != null) {
                /*    if (bean.getName().equalsIgnoreCase(" in Km")) {
                        fieldValue.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // TODO Auto-generated method stub
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                                    changeType(fieldValue);
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });
                    }*/
                }
            } else if (bean.getDataType().equalsIgnoreCase("text")) {

                final EditText fieldValue = (EditText) view
                        .findViewById(R.id.fieldvalue);
                fieldValue.setVisibility(View.VISIBLE);
                fieldValue.setTag(bean.getDataType());
                fieldValue.setContentDescription(bean.getClientId());
                fieldValuesMap.put(bean.getName(), bean);
//                fieldValue.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                if (taskDetailsBean != null) {
                    fieldValue.setText(taskDetailsBean.getTaskDescription());
                    multimediaFieldValues.put(bean.getClientId(), taskDetailsBean.getTaskDescription());
                }
                if (bean.getDataType() != null) {
                    /*if (bean.getName().equalsIgnoreCase("Birth Day")) {
                        fieldValue.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                // TODO Auto-generated method stub
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                                    changeType(fieldValue);
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        });

                    } else*/
                    if (bean.getDataType().equalsIgnoreCase("number"))
                        fieldValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                    else if (bean.getDataType().equalsIgnoreCase("email"))
                        fieldValue
                                .setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                }

            } else if (bean.getDataType().equalsIgnoreCase("datetime") && bean.getIsInputRequired().equalsIgnoreCase("yes")) {

                final TextView date = (TextView) view.findViewById(R.id.fieldText);
                date.setVisibility(View.VISIBLE);
                date.setText(bean.getName());
                date.setTag(fieldContainer.getChildCount());
                date.setContentDescription(bean.getDataType());
                if (taskDetailsBean != null) {
                    date.setText(taskDetailsBean.getTaskDescription());
                    multimediaFieldValues.put(bean.getClientId(), taskDetailsBean.getTaskDescription());
                }
                fieldValuesMap.put(bean.getName(), bean);
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int integer = (Integer) v.getTag();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String currentDateandTime = sdf.format(new Date());
                        Log.d("TagValues", "currentDateandTime    :   " + currentDateandTime + " clientId ==  " + bean.getClientId());
                        showCustomDatePicker(currentDateandTime, bean.getClientId());

                        if (taskDetailsBean.getTaskDescription() != null) {
                            date.setText(taskDetailsBean.getTaskDescription());
                            multimediaFieldValues.put(bean.getClientId(), taskDetailsBean.getTaskDescription());
                        } else {
                            date.setText(currentDateandTime);
                            multimediaFieldValues.put(bean.getClientId(), currentDateandTime);
                        }

//                        multimediaFieldValues.put(bean.getClientId(), currentDateandTime);
//                        date.setText(strenddate);
                    }
                });
            } else if (bean.getDataType().equalsIgnoreCase("audio")) {
                ImageView options = (ImageView) view.findViewById(R.id.imageButton);
                final TextView fileName = (TextView) view.findViewById(R.id.fieldText);
                options.setBackground(getResources().getDrawable(R.drawable.ic_audio_file_100));
                fileName.setVisibility(View.VISIBLE);
                options.setVisibility(View.VISIBLE);
//                fileName.setText("image");


                fileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ImageName = fileName.getText().toString();
                        Log.i("Task", "imagename" + ImageName);
                        File file = null;
                        Intent intent = new Intent(context, Audioplayer.class);
//                        file = new File(ImageName);
                        Log.i("output123", "path existing media=====================>" + ImageName);
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
                options.setTag(fieldContainer.getChildCount());
                options.setContentDescription(bean.getDataType());
                Log.i("output123", "getFromUserName====>*****" + taskDetailsBean.getFromUserName());
                Log.i("output123", "getLoginusername====>*****" + Appreference.loginuserdetails.getUsername());
               /* if (taskDetailsBean != null && taskDetailsBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                    fileName.setText(taskDetailsBean.getTaskDescription().split("/")[5]);
                    multimediaFieldValues.put(bean.getClientId(), taskDetailsBean.getTaskDescription());
                }
                else {*/
                if (taskDetailsBean.getTaskDescription() != null) {
                    if (taskDetailsBean.getTaskDescription().contains(Environment.getExternalStorageDirectory().getAbsolutePath()))
                        fileName.setText(taskDetailsBean.getTaskDescription().split("/")[5]);
                    else
                        fileName.setText(taskDetailsBean.getTaskDescription());
                    //                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + taskDetailsBean.getTaskDescription();
                    multimediaFieldValues.put(bean.getClientId(), taskDetailsBean.getTaskDescription());
                }
                fieldValuesMap.put(bean.getName(), bean);
                options.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        int pos = (Integer) v.getTag();
                        Log.d("Profile", "On click of option menu");
                        try {
                            position = Integer.parseInt(bean.getClientId());
                            Intent i = new Intent(CustomHeaderTagActivity.this, AudioRecorder.class);
                            i.putExtra("task", "audio");
                            startActivityForResult(i, 333);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });


                options.setVisibility(View.VISIBLE);
            } else if (bean.getDataType().equalsIgnoreCase("video")) {
                ImageView options = (ImageView) view.findViewById(R.id.imageButton);
                final TextView fileName = (TextView) view.findViewById(R.id.fieldText);
                fileName.setVisibility(View.VISIBLE);
                options.setVisibility(View.VISIBLE);
//                fileName.setText("image");


                fileName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ImageName = fileName.getText().toString();
                        Log.i("Task", "imagename" + ImageName);
                        File file = null;
//                        file = new File(ImageName);
                        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                        Log.i("output123", "path existing media=====================>" + ImageName);
                        if (file.exists()) {
                            Intent intent = new Intent(context, VideoPlayer.class);
                            intent.putExtra("video", file.getAbsolutePath());
                            context.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + ImageName);
                            if (file1.exists()) {
                                Intent intent = new Intent(context, VideoPlayer.class);
                                intent.putExtra("video", file1.getAbsolutePath());
                                context.startActivity(intent);
                            }
                        }

                    }
                });
                options.setTag(fieldContainer.getChildCount());
                options.setContentDescription(bean.getDataType());
                Log.i("output123", "getFromUserName====>*****" + taskDetailsBean.getFromUserName());
                Log.i("output123", "getLoginusername====>*****" + Appreference.loginuserdetails.getUsername());

               /* if (taskDetailsBean != null && taskDetailsBean.getFromUserName().equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                    fileName.setText(taskDetailsBean.getTaskDescription().split("/")[5]);
                    multimediaFieldValues.put(bean.getClientId(), taskDetailsBean.getTaskDescription());
                }
                else {*/
                if (taskDetailsBean.getTaskDescription() != null) {
                    if (taskDetailsBean.getTaskDescription().contains(Environment.getExternalStorageDirectory().getAbsolutePath()))
                        fileName.setText(taskDetailsBean.getTaskDescription().split("/")[5]);
                    else
                        fileName.setText(taskDetailsBean.getTaskDescription());
                    // String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + taskDetailsBean.getTaskDescription();
                    multimediaFieldValues.put(bean.getClientId(), taskDetailsBean.getTaskDescription());
                }
                fieldValuesMap.put(bean.getName(), bean);
                options.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        int pos = (Integer) v.getTag();
                        Log.d("Profile", "On click of option menu");
                        showMultimediaOptions(pos, bean.getClientId(), v
                                .getContentDescription().toString());

                    }
                });


                options.setVisibility(View.VISIBLE);
            }

            fieldContainer.addView(view);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("output123", "InflateFieldsExist Custom1 Error====>*****" + taskDetailsBean.getFromUserName());
        }
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


                    final String path = Environment.getExternalStorageDirectory()
                            + "/High Message/";
                    File directory = new File(path);
                    if (!directory.exists())
                        directory.mkdir();
                    strIPath = path + getFileName() + ".jpg";
                    Uri imageUri = Uri.fromFile(new File(strIPath));
                    Intent intent = new Intent(context,
                            CustomVideoCamera.class);
                    intent.putExtra("filePath", strIPath);
                    intent.putExtra("isPhoto", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 132);

                } else {
                    Toast.makeText(context, "InSufficient Memory...", Toast.LENGTH_LONG).show();
                }
            } else if (strMMType.equalsIgnoreCase("Video")) {
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
            return android.os.Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

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
            hourPicker.setMinValue(0);
            hourPicker.setMaxValue(11);
            String[] displayedValues_hour = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
            hourPicker.setDisplayedValues(displayedValues_hour);
//            } else {
//                hourPicker.setMinValue(00);
//                hourPicker.setMaxValue(23);
//                am_pmPicker.setVisibility(View.GONE);
//            }


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
                DateFormat targetFormat = new SimpleDateFormat("MMM dd");
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
            if (hour >= 13) {
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
            } else {
                hourPicker.setValue(hour - 1);
                am_pmPicker.setValue(0);
            }
            set.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
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
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
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

                    toas = tdystr + " " + values2[hourPicker.getValue()] + " : "
                            + values3[minutePicker.getValue()] + " "
                            + values4[am_pmPicker.getValue()];
//                    }else{
//                        toas = tdystr + " " + hourPicker.getValue() + " : "
//                                + values3[minutePicker.getValue()];
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

                    DateFormat selctedDateformate = new SimpleDateFormat("yyyy MMM dd hh : mm a");
                    Date date = null;
                    try {
                        date = selctedDateformate.parse(yyyy + " " + toas);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat day1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    strenddate = day1.format(date);

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

        String today;

        List<String> dates = new ArrayList<String>();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
            dates.add("Today");
            today = dateFormat.format(c1.getTime());
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c.getTime());
            String split[] = formattedDate.split("-");
            String todaydate = split[0] + " " + split[1];
            for (int i = 0; i < 360; i++) {
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


    public void saveTextValue() {
        ArrayList<ListofFileds> fields_list = new ArrayList<>();
        for (int i = 0; i < fieldContainer.getChildCount(); i++) {
            LinearLayout child_view = (LinearLayout) fieldContainer
                    .getChildAt(i);

            ListofFileds bean = profileFieldList.get(i);

            Log.d("My Profile",
                    "After Send button click Field ID : " + bean.getClientId()
                            + ", Field Name : "
                            + bean.getName() + ", Field TagName : "
                            + bean.getTagName() + ", Type :"
                            + bean.getDataType() + " required  " + bean.getIsInputRequired() + " Task Description  == " + multimediaFieldValues.get(bean.getClientId()));


            if (bean != null) {
                if (bean.getDataType().equalsIgnoreCase("photo")
                        || bean.getDataType().equalsIgnoreCase("audio")
                        || bean.getDataType().equalsIgnoreCase("video")
                        || bean.getDataType().equalsIgnoreCase("signature")) {
                    String type = bean.getDataType();
                    if (multimediaFieldValues.containsKey(bean.getClientId())) {

                        ListofFileds fieldTemplateBean = new ListofFileds();
                        fieldTemplateBean.setId(bean.getId());
                        fieldTemplateBean.setTask(bean.getName());
                        fieldTemplateBean.setDataType(bean.getDataType());
                        fieldTemplateBean.setName(multimediaFieldValues.get(bean.getClientId()));
                        fields_list.add(fieldTemplateBean);
                    }
                } else if (bean.getDataType().equalsIgnoreCase("text") || bean.getDataType().equalsIgnoreCase("numeric")) {
                    EditText ed_fieldvalue = (EditText) child_view
                            .findViewById(R.id.fieldvalue);
                    if (ed_fieldvalue.getText().toString().trim().length() != 0) {
                        ListofFileds fieldTemplateBean = new ListofFileds();
                        fieldTemplateBean.setId(bean.getId());

                        fieldTemplateBean.setTask(bean.getName());

                        fieldTemplateBean.setDataType(bean.getDataType());

                        if (ed_fieldvalue.getText().toString().trim()
                                .length() > 0) {

                            if (bean.getDataType().equalsIgnoreCase("numeric")) {
                                fieldTemplateBean.setName(ed_fieldvalue
                                        .getText().toString().trim());
                            } else {
                                fieldTemplateBean.setName(ed_fieldvalue
                                        .getText().toString().trim());
                            }
                            fields_list.add(fieldTemplateBean);
                        }
                    }

                } else if (bean.getDataType().equalsIgnoreCase("datetime")) {
                    String type = bean.getDataType();
                    if (multimediaFieldValues.containsKey(bean.getClientId())) {

                        ListofFileds fieldTemplateBean = new ListofFileds();
                        fieldTemplateBean.setId(bean.getId());
                        fieldTemplateBean.setTask(bean.getName());
                        fieldTemplateBean.setDataType(bean.getDataType());
                        fieldTemplateBean.setName((multimediaFieldValues.get(bean.getClientId())));
                        fields_list.add(fieldTemplateBean);
                    }

                }

            }
        }

        try {
            JSONObject[] filedlist = new JSONObject[fields_list.size()];
            int j = 0;
            for (int k = 0; k < fields_list.size(); k++) {

                ListofFileds listofFileds = fields_list.get(k);

                Log.d("TagsValuesInWebService",
                        "Field ID : " + listofFileds.getClientId()
                                + ", Field Name : "
                                + listofFileds.getName() + ", Type :"
                                + listofFileds.getDataType() + " list size is :  " + fields_list.size());
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("id", listofFileds.getId());
                filedlist[k] = new JSONObject();
                filedlist[k].put("headerTag", jsonObject1);
                if (listofFileds.getDataType().equalsIgnoreCase("photo") || listofFileds.getDataType().equalsIgnoreCase("image") || listofFileds.getDataType().equalsIgnoreCase("signature")) {
                    filedlist[k].put("fileContent", encodeTobase64(BitmapFactory.decodeFile((listofFileds.getName()))));
                    filedlist[k].put("fileExt", "png");

                } else if (listofFileds.getDataType().equalsIgnoreCase("video")) {
                    filedlist[k].put("fileContent", encodeAudioVideoToBase64(listofFileds.getName()));
                    filedlist[k].put("fileExt", "mp4");

                } else if (listofFileds.getDataType().equalsIgnoreCase("audio")) {
                    filedlist[k].put("fileContent", encodeAudioVideoToBase64(listofFileds.getName()));
                    filedlist[k].put("fileExt", "mp3");
                } else {
                    filedlist[k].put("description", listofFileds.getName());
                }
                j++;
            }
            JSONArray listpostfiles_object = new JSONArray();
            for (int i1 = 0; i1 < filedlist.length; i1++) {
                listpostfiles_object.put(filedlist[i1]);
            }
            JSONObject jsonObject = new JSONObject();

            JSONObject jsonObject1 = new JSONObject();
//            jsonObject1.put("id", taskId);
            jsonObject1.put("id", taskId);

            jsonObject.put("task", jsonObject1);

            jsonObject.put("valueList", listpostfiles_object);

            Appreference.jsonRequestSender.customHeaderTagValueEntry(EnumJsonWebservicename.saveCustomHeaderTagValue, jsonObject, this, fields_list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String encodeTobase64(Bitmap image) {
        String imageEncoded = null;
        try {
            Bitmap immagex = image;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            immagex.compress(Bitmap.CompressFormat.JPEG, 75, baos);
            byte[] b = baos.toByteArray();
            imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        return strFile;
    }

    public String DayHourMinSec(Date startDate, Date endDate, String first_travelHour) {

        String day_hour_min_sec = null;
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        Log.i("compute", "startDate : " + startDate);
        Log.i("compute", "endDate : " + endDate);
        Log.i("compute", "different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        Log.i("compute", "elapsedDays--->" + elapsedDays);
        Log.i("compute", "elapsedHours--->" + elapsedHours);
        Log.i("compute", "elapsedMinutes--->" + elapsedMinutes);
        Log.i("compute", "elapsedSeconds--->" + elapsedSeconds);
        day_hour_min_sec = String.valueOf(elapsedHours) + ":" + String.valueOf(elapsedMinutes) + ":" + String.valueOf(elapsedSeconds);
        try {
            DateFormat df = new SimpleDateFormat("HH:mm:ss");
            Date date1 = df.parse(day_hour_min_sec);
            day_hour_min_sec = df.format(date1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (first_travelHour != null) {
            try {
                Calendar thatDay = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                Date date = dateFormat.parse(first_travelHour);
                thatDay.setTime(date);
                thatDay.add(Calendar.HOUR, Integer.parseInt(String.valueOf(elapsedHours)));
                thatDay.add(Calendar.MINUTE, Integer.parseInt(String.valueOf(elapsedMinutes)));
                thatDay.add(Calendar.SECOND, Integer.parseInt(String.valueOf(elapsedSeconds)));
                DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
                day_hour_min_sec = dateFormat1.format(thatDay.getTime());
                Log.i("compute", "day_hour_min_sec--->" + day_hour_min_sec);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return day_hour_min_sec;

    }
}


