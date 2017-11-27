package com.ase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import Services.ShowOrCancelProgress;


public class CustomVideoCamera extends Activity {
    public static String seceretKey = "comcryptkey00000";
    private static boolean flag = false;
    public byte[] data_1;
    Button myButton, start_rec;
    ProgressBar progressBar;
    SurfaceHolder surfaceHolder;
    boolean recording;
    Button back, front, retake, usephoto, usevideo;
    FrameLayout myCameraPreview, newmyCameraPreview;
    VideoView mVideoView;
    // private String filename;
//    private String path;
    Chronometer chronometer;
    Context context = null;
    String path = Environment.getExternalStorageDirectory()
            + "/High Message/";
    private boolean isPhoto = false;
    private Camera myCamera = null;
    private MyCameraSurfaceView myCameraSurfaceView;
    private OrientationEventListener orientationEventListener;
    ProgressDialog progress;
    String phoneModel;
    String orientation = "90";
    String photo_taken_orientatio = "90";
    private ShowOrCancelProgress progressListener;
    private boolean photo_stored = false;
    Bitmap realImage;
    final SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss.SSS a zzz");

    PictureCallback jpegCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            data_1 = data;
            releaseCamera();
            usephoto.setVisibility(View.VISIBLE);
            retake.setVisibility(View.VISIBLE);
            myCameraPreview.setVisibility(View.GONE);
            myButton.setVisibility(View.GONE);
            front.setEnabled(false);
            back.setEnabled(false);
            newmyCameraPreview.setVisibility(View.VISIBLE);
            Log.i("name", "device name--->" + getDeviceName());
            if (getDeviceName() != null && getDeviceName().equalsIgnoreCase("motorola MotoG3")) {
                new SaveImageTask().execute(data_1);
            } else if (getDeviceName() != null && getDeviceName().toLowerCase().contains("samsung")) {
//                Bitmap realImage = BitmapFactory.decodeStream(stream);
//
//                ExifInterface exif=new ExifInterface(getRealPathFromURI(imagePath));
//
//                Log.d("EXIF value", exif.getAttribute(ExifInterface.TAG_ORIENTATION));
//                if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")){
//
//                    realImage=rotate(realImage, photo_taken_orientatio);
//                }else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")){
//                    realImage=rotate(realImage, photo_taken_orientatio);
//                }else if(exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")){
//                    realImage=rotate(realImage, photo_taken_orientatio);
//                }

                Date dn = new Date();
                Log.i("Performance", "befor bitmap Rotate " + ft.format(dn));
                progressListener.ShowProgress(context);
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                Log.i("Performance", "After bitmap Before Rotate " + ft.format(new Date()));
                realImage = rotate(bmp, photo_taken_orientatio);
                Log.i("Performance", "After  Rotate  Before Task " + ft.format(new Date()));
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                realImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//                new SaveImageTask().execute(byteArray);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ImageView image = (ImageView) findViewById(R.id.image_preview);
                        image.setImageBitmap(realImage);
                        newmyCameraPreview.removeView(image);
                        myCameraPreview.removeView(myCameraSurfaceView);
                        newmyCameraPreview.addView(image);
                        Date dn2 = new Date();
                        Log.i("Performance", "onUI " + ft.format(dn2));
                    }
                });

                progressListener.CancellProgress();
//                ImageView image = (ImageView) findViewById(R.id.image_preview);
//                image.setImageBitmap(realImage);
//                newmyCameraPreview.removeView(image);
//                myCameraPreview.removeView(myCameraSurfaceView);
//                newmyCameraPreview.addView(image);
//                Date dn2 = new Date();
//                Log.i("Performance", "onUI " + ft.format(dn2));

            } else {
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                ImageView image = (ImageView) findViewById(R.id.image_preview);
                image.setImageBitmap(bmp);
                newmyCameraPreview.removeView(image);
                myCameraPreview.removeView(myCameraSurfaceView);
                newmyCameraPreview.addView(image);
            }


        }
    };
    private MediaRecorder mediaRecorder;
    private int camera_no;
    private String filepath;
    private final int STOPSPLASH = 0;
    private final int STARTSPLASH = 1;
    private boolean ispaused = false;
    private Handler handler = new Handler();
    private boolean saved_complated = true;

    public static String decryptFile(Context context, String filePath) {
        try {
            if (flag) {
                File file = new File(filePath);
                if (file.exists()) {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    byte[] bytes = new byte[(int) file.length()];
                    fileInputStream.read(bytes);
                    fileInputStream.close();

                    byte[] decrypted = decrypt(seceretKey, bytes);

                    String path = context.getCacheDir().getAbsolutePath() + "/tmpcache";
                    File newFile = new File(path);
                    if (newFile.exists() == false)
                        newFile.mkdir();

                    path = newFile.getAbsolutePath() + "/" + file.getName();

                    FileOutputStream fileOuputStream = new FileOutputStream(path);
                    fileOuputStream.write(decrypted);
                    fileOuputStream.close();
                    return path;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("CustomVideoCamera decryptFile","Exception "+e.getMessage(),"WARN",null);
        }
        return filePath;
    }

    public static byte[] decrypt(String seed, byte[] encrypted) throws Exception {
        byte[] rawKey = seed.getBytes();// getRawKey(seed.getBytes());
        return decrypt(rawKey, encrypted);
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(raw);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivParameterSpec);
        return cipher.doFinal(encrypted);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        context = this;
//        recording = false;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.customvideo);
        progressListener = Appreference.main_Activity_context;
        try {
            Appreference.context_table.put("customvideocallscreen", context);
            filepath = getIntent().getStringExtra("filePath");
            isPhoto = getIntent().getBooleanExtra("isPhoto", false);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("CustomVideoCamera oncreate", "Exception " + e.getMessage(), "WARN", null);
            Log.i("camera", "error" + filepath);
            Log.i("camera", "error" + isPhoto);
            isPhoto = true;
        }
        progressBar = (ProgressBar) findViewById(R.id.progress);
        myButton = (Button) findViewById(R.id.mybutton);
        start_rec = (Button) findViewById(R.id.mybutton1);
        front = (Button) findViewById(R.id.Button1);
        back = (Button) findViewById(R.id.Button2);
        retake = (Button) findViewById(R.id.retake);
        usephoto = (Button) findViewById(R.id.use_photo);
        usevideo = (Button) findViewById(R.id.use_video);
        myCameraPreview = (FrameLayout) findViewById(R.id.videoview);
        newmyCameraPreview = (FrameLayout) findViewById(R.id.videoview1);
        mVideoView = (VideoView) findViewById(R.id.play_video);
        mVideoView.setVisibility(View.GONE);

        phoneModel = android.os.Build.MODEL;
        Log.i("camera", "Device model-->" + phoneModel);

        String productname = android.os.Build.PRODUCT;
        Log.i("camera", "Device model-->" + productname);
        Log.i("camera", "1 Device Name :" + getDevice_Name());
        Log.i("camera", "2 Device Name :" + getDeviceName());

        if (phoneModel != null && phoneModel.equalsIgnoreCase("Nexus 5X") && camera_no == 0) {
            orientation = "270";
        } else {
            if (camera_no == 0) {
                orientation = "90";
            } else if (camera_no == 1) {
                orientation = "270";
            }
        }
        cameraoption();

        try {
            orientationEventListener = new OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
                @Override
                public void onOrientationChanged(int arg0) {
                    // TODO Auto-generated method stub

                    Log.i("orientation", "Orientation: " + String.valueOf(arg0));
                    if (phoneModel != null && phoneModel.equalsIgnoreCase("Nexus 5X") && camera_no == 0) {
                        if (arg0 > 250 && arg0 < 290) {
                            orientation = "180";
                            Log.i("orientation", "values 1-->" + orientation);
                        } else if (arg0 > 80 && arg0 < 100) {
                            orientation = "0";
                        } else if (arg0 > 170 && arg0 < 190) {
                            orientation = "90";
                        } else {
                            orientation = "270";

                        }
                    } else {
                        if (camera_no == 0) {
                            if (arg0 > 250 && arg0 < 290) {
                                orientation = "0";
                                Log.i("orientation", "values 1-->" + orientation);
                            } else if (arg0 > 80 && arg0 < 100) {
                                orientation = "180";
                            } else if (arg0 > 170 && arg0 < 190) {
                                orientation = "270";
                            } else {
                                orientation = "90";

                            }
                        } else if (camera_no == 1) {
                            if (arg0 > 250 && arg0 < 290) {
                                orientation = "0";
                                Log.i("orientation", "values 1-->" + orientation);
                            } else if (arg0 > 80 && arg0 < 100) {
                                orientation = "180";
                            } else if (arg0 > 170 && arg0 < 190) {
                                orientation = "90";
                            } else {
                                orientation = "270";

                            }
                        }
                    }

                    Log.i("orientation", "values 2-->" + orientation);
                }
            };

            if (orientationEventListener.canDetectOrientation()) {
                orientationEventListener.enable();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("CustomVideoCamera oncreate","Exception "+e.getMessage(),"WARN",null);
        }

        usephoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getDeviceName() != null && (getDeviceName().equalsIgnoreCase("motorola MotoG3"))) {
                    Log.i("customcamera", "MotoG3 or Samsung");
                    if (getDeviceName().toLowerCase().contains("samsung")) {
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                //TODO your background code
                                saved_complated = false;
                                FileOutputStream out = null;
                                try {

                                    File file = new File(filepath);
                                    if (file.exists()) {
                                        file.delete();
                                        file = new File(filepath);
                                    }
                                    out = new FileOutputStream(file);
                                    Log.i("Performance", "1 After  Rotate  Before Task " + ft.format(new Date()));
                                    realImage.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                                    // PNG is a lossless format, the compression factor (100) is ignored
                                    out.flush();
                                    out.close();
                                    Log.i("Performance", "2 After  Rotate  Before Task " + ft.format(new Date()));
                                    saved_complated = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("CustomVideoCamera usePhoto.setOnClickListener","Exception "+e.getMessage(),"WARN",null);
                                } finally {
                                    try {
                                        if (out != null) {
                                            out.close();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        Appreference.printLog("CustomVideoCamera usePhoto.setOnClickListener","Exception "+e.getMessage(),"WARN",null);
                                    }
                                }
                                Date dn1 = new Date();
                                Log.i("Performance", "After Rotate " + ft.format(dn1));
                            }
                        });
                    }
                    int pos = getIntent().getExtras().getInt("others", 0);
                    String path = getIntent().getStringExtra("filePath");
                    Intent intent = new Intent();
                    Log.i("customcamera", "path " + path);
                    intent.putExtra("others", pos);
                    intent.putExtra("filePath", path);
                    setResult(RESULT_OK, intent);
                    finish();
                } else if (getDeviceName().toLowerCase().contains("samsung")) {

                    //TODO your background code
//                    showToast("Compressing the Image");
//                    showToast("Compressing the Image");
//                    showToast("Compressing the Image");
                    Message msg = new Message();
                    msg.what = STARTSPLASH;
                    splashHandler.sendMessage(msg);

                    saved_complated = false;
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            FileOutputStream out = null;
                            try {

                                File file = new File(filepath);
                                if (file.exists()) {
                                    file.delete();
                                    file = new File(filepath);
                                }
                                out = new FileOutputStream(file);
                                Log.i("Performance", "01 After  Rotate  Before Task " + ft.format(new Date()));
//                                showToast("Compressing the Image");

                                realImage.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                                // PNG is a lossless format, the compression factor (100) is ignored
                                out.flush();
                                out.close();
                                Log.i("Performance", "02 After  Rotate  Before Task " + ft.format(new Date()));
                                saved_complated = true;

                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("CustomVideoCamera usePhoto.setOnClickListener", "Exception " + e.getMessage(), "WARN", null);
                            } finally {
                                try {
                                    if (out != null) {
                                        out.close();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Appreference.printLog("CustomVideoCamera usePhoto.setOnClickListener","Exception "+e.getMessage(),"WARN",null);
                                }
                                if(progress != null && progress.isShowing()) {
                                    progress.cancel();
                                }
                                Date dn1 = new Date();
                                Log.i("Performance", "After Rotate " + ft.format(dn1));
                                int pos = getIntent().getExtras().getInt("others", 0);
                                String path = getIntent().getStringExtra("filePath");
                                Intent intent = new Intent();
                                Log.i("customcamera", "path " + path);
                                intent.putExtra("others", pos);
                                intent.putExtra("filePath", path);
                                setResult(RESULT_OK, intent);
                                finish();

                            }
                        }
                    });
//                    Date dn1 = new Date();
//                    Log.i("Performance", "After Rotate " + ft.format(dn1));
//                    int pos = getIntent().getExtras().getInt("others", 0);
//                    String path = getIntent().getStringExtra("filePath");
//                    Intent intent = new Intent();
//                    Log.i("customcamera", "path " + path);
//                    intent.putExtra("others", pos);
//                    intent.putExtra("filePath", path);
//                    setResult(RESULT_OK, intent);
//                    finish();

                } else {
                    new SaveImageTask().execute(data_1);
                }
//                Toast.makeText(getApplicationContext(),"use photo",Toast.LENGTH_SHORT).show();
//                int pos = getIntent().getExtras().getInt("others", 0);
//                String path = getIntent().getStringExtra("filePath");
//                Intent intent = new Intent();
//                Log.i("customcamera", "path " + path);
//                intent.putExtra("others", pos);
//                intent.putExtra("filePath", path);
//                setResult(RESULT_OK, intent);
//                finish();
            }
        });

        usevideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"use video",Toast.LENGTH_SHORT).show();
                int pos = getIntent().getExtras().getInt("others", 0);
                String path = getIntent().getStringExtra("filePath");
                Intent intent = new Intent();
                intent.putExtra("others", pos);
                intent.putExtra("filePath", path);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

        retake.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"retake",Toast.LENGTH_SHORT).show();
//               onCreate(savedInstanceState);
//                onRestart();
                try {
                    if (isPhoto) {
                        usephoto.setVisibility(View.GONE);
                        retake.setVisibility(View.GONE);
                        newmyCameraPreview.setVisibility(View.GONE);
                        myCameraPreview.setVisibility(View.VISIBLE);
                        myButton.setVisibility(View.VISIBLE);
                        myButton.setEnabled(true);
                        front.setEnabled(true);
                        back.setEnabled(true);
                        front.setVisibility(View.VISIBLE);
                        back.setVisibility(View.GONE);
                        cameraoption();
                    } else {
                        myCameraPreview.removeView(myCameraSurfaceView);
//                        newmyCameraPreview.removeView(mVideoView);
                        mVideoView.setVisibility(View.GONE);
                        File f = new File(filepath);
                        Boolean deleted = f.delete();
//                        filepath = path + getFileName() + ".mp4";
                        Log.i("Videofile", "file deleted " + deleted);
                        usevideo.setVisibility(View.GONE);
                        retake.setVisibility(View.GONE);
                        newmyCameraPreview.setVisibility(View.GONE);
                        myCameraPreview.setVisibility(View.VISIBLE);
                        myButton.setVisibility(View.GONE);
                        start_rec.setVisibility(View.VISIBLE);
                        myButton.setEnabled(true);
                        start_rec.setEnabled(true);
                        front.setEnabled(true);
                        back.setEnabled(true);
                        front.setVisibility(View.VISIBLE);
                        back.setVisibility(View.GONE);
                        //                    File file = new File(filepath);
                        //                    boolean deleted = file.delete();
                        //                   cameraoption();
                        //                    myCameraPreview.removeView(myCameraSurfaceView);
                        myCamera.stopPreview();
                        myCamera.release();
                        myCamera = null;
                        myCamera = Camera
                                .open(CameraInfo.CAMERA_FACING_BACK);
                        myCameraSurfaceView = new MyCameraSurfaceView(
                                CustomVideoCamera.this, myCamera);
                        myCameraPreview.addView(myCameraSurfaceView);
                        camera_no = 0;
                        chronometer.setBase(SystemClock.elapsedRealtime());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("CustomVideoCamera retake.setOnclickListener","Exception "+e.getMessage(),"WARN",null);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppMainActivity.inActivity = this;
    }

    private void showToast(final String message) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        },3000);

    }

    @SuppressLint("HandlerLeak")
    private Handler splashHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STARTSPLASH:
                    progressBar.setVisibility(View.VISIBLE);
                    showprogress("Compressing the Image");
                    break;
                case STOPSPLASH:
                    Log.i("startapp", "inside stop splash");
                    if(progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    if(progress != null && progress.isShowing()) {
                        progress.cancel();
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public String getFileName() {
        String strFilename = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
            Date date = new Date();
            strFilename = dateFormat.format(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("CustomVideoCamera getFileName","Exception "+e.getMessage(),"WARN",null);
        }
        return strFilename;
    }

    private void startRecording() {
        try {

            front.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
            // Release Camera before MediaRecorder start
            releaseCamera();

            if (!prepareMediaRecorder(filepath)) {
                Toast.makeText(CustomVideoCamera.this,
                        "Fail in prepareMediaRecorder()!\n - Ended -............", Toast.LENGTH_LONG).show();
                finish();
            }
            // v.setEnabled(false);
            //
            // handler.postDelayed(new Runnable() {
            //
            // @Override
            // public void run() {
            // v.setEnabled(true);
            //
            // }
            // }, 1000);
            mediaRecorder.start();
            recording = true;

            //For Change Camera front from back
            //start
//		   back.setVisibility(View.VISIBLE);
//		   front.setVisibility(View.GONE);
           /* back.setVisibility(View.GONE);
            front.setVisibility(View.VISIBLE);*/
            //end
            // myButton.setText("STOP");
            // myButton.setBackgroundResource(R.drawable.stop_recording);
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("CustomVideoCamera startRecording","Exception "+e.getMessage(),"WARN",null);
        }
    }

    public void stopRecording() {

        try {
            if (recording) {
                // stop recording and release camera
                if (mediaRecorder != null)
                    try {
                        mediaRecorder.stop();// stop the recording
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("CustomVideoCamera stopRecording","Exception "+e.getMessage(),"WARN",null);
                    }
                releaseMediaRecorder(); // release the MediaRecorder
                // object
                chronometer.stop();
                // myButton.setText("START");
                myButton.setBackgroundResource(R.drawable.start_recording);
                // Exit after saved
                usevideo.setVisibility(View.VISIBLE);
                retake.setVisibility(View.VISIBLE);
                myCameraPreview.setVisibility(View.GONE);
                start_rec.setVisibility(View.GONE);
                myButton.setVisibility(View.GONE);
                front.setEnabled(false);
                back.setEnabled(false);
                newmyCameraPreview.setVisibility(View.VISIBLE);


                Log.i("customcamera", "value..." + path);
                Log.i("customcamera", "value..." + filepath);

                final ImageView myImage = (ImageView) findViewById(R.id.image_preview);
                myImage.setVisibility(View.VISIBLE);
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(filepath, MediaStore.Images.Thumbnails.MINI_KIND);
                Matrix matrix = new Matrix();
                Bitmap bmThumbnail = Bitmap.createBitmap(thumb, 0, 0,
                        thumb.getWidth(), thumb.getHeight(), matrix, true);
                Log.i("customcamera", "value..." + bmThumbnail);
                myImage.setImageBitmap(bmThumbnail);
//                newmyCameraPreview=null;
//                newmyCameraPreview = (FrameLayout) findViewById(R.id.videoview1);
                newmyCameraPreview.removeView(myImage);
                myCameraPreview.removeView(myCameraSurfaceView);
                newmyCameraPreview.addView(myImage);
                newmyCameraPreview.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(getApplicationContext(), "play", Toast.LENGTH_SHORT).show();
                        Log.i("customcamera", "value...1" + filepath);

                        mVideoView.setVideoPath(filepath);
                        myImage.setVisibility(View.GONE);
                        mVideoView.setVisibility(View.VISIBLE);
                        front.setVisibility(View.GONE);
                        back.setVisibility(View.GONE);
                        MediaController mediaController = new MediaController(context);
                        mediaController.setAnchorView(mVideoView);

                        Log.i("customcamera", "value...1" + filepath);
                        if (filepath != null) {
                            try {
                                // start 07-10-15 changes

                                filepath = decryptFile(context, filepath);
                                Log.i("customcamera", "value...2" + filepath);
                                // ended 07-10-15 changes
                                Uri uri = Uri.parse(filepath);
                                mVideoView.setMediaController(mediaController);
                                mVideoView.setVideoURI(uri);
                                mVideoView.requestFocus();
                                //                            mVideoView.seekTo(msec);
                                /*mVideoView
                                        .setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                            @Override
                                            public void onCompletion(MediaPlayer mp) {
                                                Log.i("customcamera", "value...2" + filepath);
                                                CustomVideoCamera.this.finish();
                                            }
                                        });*/
                                mVideoView.start();
                                Log.i("customcamera", "value...2" + filepath);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("CustomVideoCamera stopRecording","Exception "+e.getMessage(),"WARN",null);
                                Toast.makeText(getApplicationContext(),
                                        "Unable to play video.", Toast.LENGTH_SHORT).show();
                                CustomVideoCamera.this.finish();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Video Filepath is Wrong.",
                                    Toast.LENGTH_SHORT).show();
                            CustomVideoCamera.this.finish();
                        }
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("CustomVideoCamera stopRecording","Exception "+e.getMessage(),"WARN",null);
        }
    }

    private void getCameraInstance(int front_back) {
        // TODO Auto-generated method stub
        try {

            CameraInfo camInfo = new CameraInfo();

            Camera.getCameraInfo(front_back, camInfo);
            if (front_back == 0) {

                myCamera = Camera.open(CameraInfo.CAMERA_FACING_BACK);
            } else if (front_back == 1) {

                myCamera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);

            }
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            e.printStackTrace();
            Appreference.printLog("CustomVideoCamera getcameraInstance","Exception "+e.getMessage(),"WARN",null);
        }
    }

    private boolean prepareMediaRecorder(String filename) {

        try {
            // String path = Environment.getExternalStorageDirectory()
            // .getAbsolutePath().toString()
            // + "/";
            // Date date = new Date();
            // filename = "rec" + date.toString().replace(" ", "_").replace(":",
            // "_")
            // + ".mp4";
            // create empty file it must use
            // File file = new File(path, filename);
            if (!filename.endsWith(".mp4")) {
                filename = filename + ".mp4";
            }
            Log.i("camera", " camera_no :" + camera_no);
            getCameraInstance(camera_no);

            Camera.Parameters parameters = myCamera.getParameters();
            List<Size> sizes = parameters.getSupportedPictureSizes();
            for (Size size : sizes) {

                Log.i("log", "Available resolution: " + size.width + " "
                        + size.height);
            }
            mediaRecorder = new MediaRecorder();
            myCamera.lock();
            if (phoneModel != null && phoneModel.equalsIgnoreCase("Nexus 5X")) {
                if (camera_no == 0) {
                    myCamera.setDisplayOrientation(270);
                } else {
                    myCamera.setDisplayOrientation(90);
                }
            } else {
                myCamera.setDisplayOrientation(90);
            }
            myCamera.unlock();
            mediaRecorder.setCamera(myCamera);
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

            // CamcorderProfile profile = CamcorderProfile
            // .get(CamcorderProfile.QUALITY_LOW);
            // profile.videoFrameWidth = 320;
            // profile.videoFrameHeight = 240;
            // profile.videoFrameWidth = 640;
            // profile.videoFrameHeight = 480;
            // profile.videoFrameWidth = 1280;
            // profile.videoFrameHeight = 720;
            // mediaRecorder.setProfile(profile);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

            int currentapiVersion = Build.VERSION.SDK_INT;
            if (currentapiVersion <= Build.VERSION_CODES.GINGERBREAD) {
                // Do something for froyo and above versions
                mediaRecorder
                        .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            } else {
                // do something for phones running an SDK before gingerbread
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            }
            mediaRecorder.setOrientationHint(Integer.parseInt(orientation));
            mediaRecorder.setVideoSize(320, 240);
            mediaRecorder.setOutputFile(filename);
            mediaRecorder.setMaxDuration(60000); // Set max duration 60 sec.
            mediaRecorder.setMaxFileSize(5000000); // Set max file size 5M
            mediaRecorder.setOnInfoListener(new OnInfoListener() {

                @Override
                public void onInfo(MediaRecorder mr, int what, int extra) {
                    // TODO Auto-generated method stub
                    if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                        // mr.stop();
                        chronometer.stop();
                        Toast.makeText(getApplicationContext(),
                                "Duration limit reached", Toast.LENGTH_LONG)
                                .show();
                        /*mediaRecorder.stop();
                        releaseMediaRecorder();*/
                        stopRecording();

//                        releaseCamera();
                        /*Intent intent = new Intent();
                        // intent.putExtra("MESSAGE", filename);
                        setResult(RESULT_OK, intent);*/
//                        finish();
                    }
                }
            });

            mediaRecorder.setPreviewDisplay(myCameraSurfaceView.getHolder()
                    .getSurface());
            try {
                mediaRecorder.prepare();
            } catch (IllegalStateException e) {
                Appreference.printLog("CustomVideoCamera setPreviewDisplay","Exception "+e.getMessage(),"WARN",null);
                releaseMediaRecorder();
                return false;
            } catch (IOException e) {
                Appreference.printLog("CustomVideoCamera setPreviewDisplay","Exception "+e.getMessage(),"WARN",null);
                releaseMediaRecorder();
                return false;
            }
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("CustomVideoCamera setPreviewDisplay","Exception "+e.getMessage(),"WARN",null);
            return false;
        }
    }

    @Override
    protected void onPause() {
        try {
            ispaused = true;
            super.onPause();
            if (isPhoto) {
                photo_taken_orientatio = orientation;
                myCamera.takePicture(null, null, jpegCallback);
            } else {
                stopRecording();
            }
//            releaseMediaRecorder();
            releaseCamera();

//            finish();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("CustomVideoCamera onPause","Exception "+e.getMessage(),"WARN",null);
        }
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset(); // clear recorder configuration
            mediaRecorder.release(); // release the recorder object
            mediaRecorder = null;
            myCamera.lock(); // lock camera for later use
            Log.i("customcamera", "releaseRecorder ");
        }
    }

    private void releaseCamera() {
        try {
            if (myCamera != null) {
                myCamera.setPreviewCallback(null);
                myCamera.setErrorCallback(null);
                myCamera.stopPreview();
                myCamera.release();
                myCamera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("CustomVideoCamera releaseCamera","Exception "+e.getMessage(),"WARN",null);
            Log.e("error", e.toString());
            myCamera = null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (mediaRecorder != null) {

                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                //alert.setTitle(SingleInstance.mainContext.getResources().getString(R.string.confirm_stop_recording));

                alert.setMessage(
                        "DO YOU WANT TO CANCEL RECORDING AND EXIT?")
                        .setCancelable(false)
                        .setNegativeButton("No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {

                                        dialog.cancel();
                                    }
                                })
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {

                                        // do your stuff
                                        releaseMediaRecorder(); // if you are
                                        // using
                                        // MediaRecorder,
                                        // release
                                        // it first
                                        releaseCamera(); // release the camera
                                        if (mediaRecorder != null)
                                            mediaRecorder.stop();
                                        releaseMediaRecorder();
                                        chronometer.stop();
                                        // immediately on
                                        // pause event
//										if (WebServiceReferences.contextTable
//												.containsKey("Component")) {
//											ComponentCreator comp = (ComponentCreator) WebServiceReferences.contextTable
//													.get("Component");
//											if (SingleInstance.notePicker) {
//												comp.finish();
//												SingleInstance.notePicker = false;
//											}
//
//										}


                                    }
                                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }

        }
//        finish();
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (orientationEventListener != null) {
            orientationEventListener.disable();
        }
    /*	Locale locale = null;
        SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(Appreference.mainContect);
		String locale_string = sharedPreferences.getString("locale",
				Appreference.mainContect.getResources().getString(R.string.english_langugage));

		if(Appreference.context_table.containsKey("customvideocallscreen")){
			Appreference.context_table.remove("customvideocallscreen");
		}

		int pos = 0;
		if (locale_string.equalsIgnoreCase("English")) {
			locale = new Locale("en");
		}
		 else if (locale_string.equalsIgnoreCase(Appreference.mainContect
		 .getResources().getString(R.string.tamil_langugage))) {
		 pos = 1;
		 }
		else if (locale_string.equalsIgnoreCase("Chinese")) {
			locale = new Locale("zh");
		}


		Locale.setDefault(locale);
		Configuration config = Appreference.mainContect
				.getResources().getConfiguration();
		config.locale = locale;
		Appreference.mainContect.getResources()
				.updateConfiguration(
						config,
						Appreference.mainContect
								.getResources()

								.getDisplayMetrics());


		onConfigurationChanged(config);*/

    }

    public static Bitmap rotate(Bitmap bitmap, String taken_degree) {
        int degree = Integer.parseInt(taken_degree);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }


    public static String getDevice_Name() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

//        String phrase = "";
        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
//                phrase += Character.toUpperCase(c);
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
//            phrase += c;
            phrase.append(c);
        }

        return phrase.toString();
    }

    public void cameraoption() {
        OnClickListener myButtonOnClickListener = new OnClickListener() {

            @Override
            public void onClick(final View v) {
                myButton.setEnabled(false);

                try {


                    if (isPhoto) {
                        photo_taken_orientatio = orientation;
                        myCamera.takePicture(null, null, jpegCallback);
                    } else {
                        stopRecording();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("CustomVideoCamera cameraOption","Exception "+e.getMessage(),"WARN",null);
                    finish();
                    Log.i("Costom video ", "error occured ");
                }

            }
        };

        try {
            start_rec.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    myButton.setVisibility(View.VISIBLE);
                    start_rec.setVisibility(View.GONE);
                    front.setEnabled(false);
                    back.setEnabled(false);
                    startRecording();
                }
            });
            if (!isPhoto) {
                myButton.setVisibility(View.GONE);
                start_rec.setVisibility(View.VISIBLE);
            }


            // Get Camera for preview
            int numOfCam = Camera.getNumberOfCameras();
            Log.i("log", "" + numOfCam);
            //myButton.setEnabled(false);
            if (myCamera == null) {
                if (numOfCam > 1) {
                    //For Change Camera front from back
                    //start
//					camera_no = 1;
//					myCamera = Camera
//							.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
                    camera_no = 0;
                    myCamera = Camera
                            .open(CameraInfo.CAMERA_FACING_BACK);
                    //end
                } else {
                    //For Change Camera front from back
                    //start
//					camera_no = 0;
//					myCamera = Camera
//							.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                    camera_no = 1;
                    myCamera = Camera
                            .open(CameraInfo.CAMERA_FACING_FRONT);
                    //end
                }

                myCameraSurfaceView = new MyCameraSurfaceView(this, myCamera);
                myCameraPreview.addView(myCameraSurfaceView);
                newmyCameraPreview.setVisibility(View.GONE);
            }

            if (myCamera == null) {
                Toast.makeText(CustomVideoCamera.this, "Fail to get Camera",
                        Toast.LENGTH_LONG).show();
            }


            if (!(numOfCam == 1)) {
                //For Change Camera front from back
                //start
//				back.setVisibility(View.VISIBLE);
                front.setVisibility(View.VISIBLE);
                //end
            }
            chronometer = (Chronometer) findViewById(R.id.chronometer1);
            if (isPhoto) {
                chronometer.setVisibility(View.GONE);
            }

            myButton.setOnClickListener(myButtonOnClickListener);
            front.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    try {
                        // TODO Auto-generated method stub
                        Log.i("camera", "front click");
                        myCameraPreview.removeView(myCameraSurfaceView);
                        myCamera.stopPreview();
                        myCamera.release();
                        myCamera = null;
                        myCamera = Camera
                                .open(CameraInfo.CAMERA_FACING_FRONT);
                        myCameraSurfaceView = new MyCameraSurfaceView(
                                CustomVideoCamera.this, myCamera);
                        myCameraPreview.addView(myCameraSurfaceView);
                        camera_no = 1;
                        front.setVisibility(View.GONE);
                        back.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Appreference.printLog("CustomVideoCamera front.setOnClickListener","Exception "+e.getMessage(),"WARN",null);
                    }
                }
            });
            back.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    try {
                        // TODO Auto-generated method stub
                        Log.i("camera", "back click");
                        myCameraPreview.removeView(myCameraSurfaceView);
                        myCamera.stopPreview();
                        myCamera.release();
                        myCamera = null;
                        myCamera = Camera
                                .open(CameraInfo.CAMERA_FACING_BACK);
                        myCameraSurfaceView = new MyCameraSurfaceView(
                                CustomVideoCamera.this, myCamera);
                        myCameraPreview.addView(myCameraSurfaceView);
                        camera_no = 0;
                        back.setVisibility(View.GONE);
                        front.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Appreference.printLog("CustomVideoCamera back.setOnClickListener","Exception "+e.getMessage(),"WARN",null);
                    }
                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("CustomVideoCamera back.setOnClickListener","Exception "+e.getMessage(),"WARN",null);
            camera_no = 0;
            try {
                myCamera = Camera
                        .open(CameraInfo.CAMERA_FACING_BACK);
            } catch (Exception e1) {
                e1.printStackTrace();
                Appreference.printLog("CustomVideoCamera back.setOnClickListener","Exception "+e.getMessage(),"WARN",null);
                Toast.makeText(CustomVideoCamera.this, "Camera not Available", Toast.LENGTH_SHORT).show();
                finish();
            }
            // finish();
        }
    }

    public class MyCameraSurfaceView extends SurfaceView implements
            SurfaceHolder.Callback {

        private SurfaceHolder mHolder;
        private Camera mCamera;

        @SuppressWarnings("deprecation")
        public MyCameraSurfaceView(Context context, Camera camera) {
            super(context);
            mCamera = camera;
            Log.i("log", "mycamSurfaceView");
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format,
                                   int weight, int height) {
            if (mHolder.getSurface() == null) {
                // preview surface does not exist

                return;
            }
            try {
                mCamera.stopPreview();
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("CustomVideoCamera surfaceChanged","Exception "+e.getMessage(),"WARN",null);
            }
            try {
                if (phoneModel != null && phoneModel.equalsIgnoreCase("Nexus 5X")) {
//                    myCamera.setDisplayOrientation(270);
                    if (camera_no == 0) {
                        myCamera.setDisplayOrientation(270);
                    } else {
                        myCamera.setDisplayOrientation(90);
                    }
                } else {
                    mCamera.setDisplayOrientation(90);
                }
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.set("orientation", "portrait");
                if (camera_no == 0) {
                    if (phoneModel != null && phoneModel.equalsIgnoreCase("Nexus 5X")) {
                        parameters.setRotation(270);
                    } else {
                        parameters.setRotation(90);
                    }
                } else {
                    parameters.setRotation(270);
                }
                parameters.setPreviewSize(320, 240);
                mCamera.setParameters(parameters);
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();

            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("CustomVideoCamera surfaceChanged", "Exception " + e.getMessage(), "WARN", null);
            }
//            if (!isPhoto) {
//			    startRecording();
//            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            if (ispaused) {
                mCamera = null;
                getCameraInstance(camera_no);
                mCamera = myCamera;
            }
            try {
                if (phoneModel != null && phoneModel.equalsIgnoreCase("Nexus 5X")) {
//                    myCamera.setDisplayOrientation(270);
                    if (camera_no == 0) {
                        myCamera.setDisplayOrientation(270);
                    } else {
                        myCamera.setDisplayOrientation(90);
                    }
                } else {
                    mCamera.setDisplayOrientation(90);
                }
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.set("orientation", "portrait");
                if (camera_no == 0) {
                    if (phoneModel != null && phoneModel.equalsIgnoreCase("Nexus 5X")) {
                        parameters.setRotation(270);
                    } else {
                        parameters.setRotation(90);
                    }
                } else {
                    parameters.setRotation(270);
                }
                parameters.setPreviewSize(320, 240);
                mCamera.setParameters(parameters);
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
                Appreference.printLog("CustomVideoCamera SurfaceCreated", "Exception " + e.getMessage(), "WARN", null);
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub

        }
    }

    private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

        @Override
        protected Void doInBackground(byte[]... data) {
            FileOutputStream outStream;

            // Write to SD Card
            try {
                File outFile = new File(filepath);
                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("CustomVideoCamera SaveImageTask", "Exception " + e.getMessage(), "WARN", null);
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showprogress("Loading");
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.cancel();
            if (getDeviceName().equalsIgnoreCase("motorola MotoG3")) {
                File file = new File(filepath);
                if (file.exists()) {
//                    Toast.makeText(CustomVideoCamera.this, "File Exits", Toast.LENGTH_LONG).show();
                    ImageView image = (ImageView) findViewById(R.id.image_preview);//
                    int size = 10;
                    try {
                        if (filepath != null) {
                            Bitmap bitmapOriginal = BitmapFactory.decodeFile(filepath);
                            Bitmap bitmapsimplesize = Bitmap.createScaledBitmap(bitmapOriginal, bitmapOriginal.getWidth() / size, bitmapOriginal.getHeight() / size, true);
                            bitmapOriginal.recycle();
                            image.setImageBitmap(bitmapsimplesize);
                            Log.i("CustomCamera","bitmapsimplesize==> "+bitmapsimplesize);
                        }
                    } catch (OutOfMemoryError error) {
                        error.printStackTrace();
                        Appreference.printLog("CustomVideoCamera onPostExecute", "Exception " + error.getMessage(), "WARN", null);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("CustomVideoCamera onPostExecute", "Exception " + e.getMessage(), "WARN", null);
                    }

                    newmyCameraPreview.removeView(image);
                    myCameraPreview.removeView(myCameraSurfaceView);
                    newmyCameraPreview.addView(image);
                }
            } else {
                int pos = getIntent().getExtras().getInt("others", 0);
                String path = getIntent().getStringExtra("filePath");
                Intent intent = new Intent();
                Log.i("customcamera", "path " + path);
                intent.putExtra("others", pos);
                intent.putExtra("filePath", path);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/


//    @Override
//    public void onBackPressed() {
//        Log.d("CDA", "onBackPressed Called");
//
//       /* Intent setIntent = new Intent(Intent.ACTION_MAIN);
//        setIntent.addCategory(Intent.CATEGORY_HOME);
//        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(setIntent);*/
////        moveTaskToBack(true);
//        myCameraPreview.removeView(myCameraSurfaceView);
//        newmyCameraPreview.removeView(mVideoView);
//        File f = new File(filepath);
//        Boolean deleted = f.delete();
//        filepath = path + getFileName() + ".mp4";
//        Log.i("Videofile", "file deleted " + deleted);
//        usevideo.setVisibility(View.GONE);
//        retake.setVisibility(View.GONE);
//        newmyCameraPreview.setVisibility(View.GONE);
//        myCameraPreview.setVisibility(View.VISIBLE);
//        myButton.setVisibility(View.GONE);
//        start_rec.setVisibility(View.VISIBLE);
//        myButton.setEnabled(true);
//        start_rec.setEnabled(true);
//        front.setEnabled(true);
//        back.setEnabled(true);
//        cameraoption();
//        myCamera.stopPreview();
//        myCamera.release();
//        myCamera = null;
//        myCamera = Camera
//                .open(Camera.CameraInfo.CAMERA_FACING_BACK);
//        myCameraSurfaceView = new MyCameraSurfaceView(
//                CustomVideoCamera.this, myCamera);
//        myCameraPreview.addView(myCameraSurfaceView);
//        camera_no = 0;
//        chronometer.setBase(SystemClock.elapsedRealtime());
//
//    }


    private void showprogress(String message) {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
        try {
            Log.i("login123", "inside showProgressDialog");

            progress = new ProgressDialog(context);
            progress.setCancelable(false);

            progress.setMessage(message);

            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setProgress(0);
            progress.setMax(100);
            progress.show();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("CustomVideoCamera showProgress", "Exception " + e.getMessage(), "WARN", null);
//                        SingleInstance.printLog(null, e.getMessage(), null, e);
        }
//            }
//
//
//        });
    }

    public static String getDeviceName() {
        return Build.MANUFACTURER
                + " " + Build.MODEL;
    }
}
