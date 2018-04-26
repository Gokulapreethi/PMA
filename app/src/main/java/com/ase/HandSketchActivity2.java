package com.ase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.sketh.DrawingView;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class HandSketchActivity2 extends Activity implements OnClickListener {

    private static int GALLERY_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;

    // custom drawing view
    private DrawingView drawView;
    // buttons
    private ImageView currPaint, drawBtn, eraseBtn, newBtn, saveBtn;
    // sizes
    private float smallBrush, mediumBrush, largeBrush, miniBrush, microBrush;

    private ImageView galleryButt, minBtn, maxBtn, redoButt, undoButt;

    private int current = 0;
    String[] app = {"Camera", "Gallery"};
    private int currentFormat = 0;
    Uri fileUri = null;
    String file = null;
    private Context context;
    private boolean send = false;
    private ImageView btn_cancel1, sizeButt;
    private Button btn_cmp, clearButt, addNew_sketch;
    private TextView title;
    private String folderPath = Environment.getExternalStorageDirectory() + "/High Message/";
    //	private CallDispatcher callDisp;
    private int size = 1, size5 = 5;
    private boolean isCleared = false;
    private int imageSelectedOption = 2;
    private String StoredFilepath, check = "pencil";
    private Uri SelectedGallaryImage;
    private LinearLayout show;
    private LinearLayout parentLinearLayout;
    private Handler handler;
    String filename;
    boolean isFromEod, isFromEodsign;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(R.layout.handsketch_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        myInit();
        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_linear_layout);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void myInit() {

        handler = new Handler();
        try {
            send = getIntent().getBooleanExtra("send", false);
            isFromEod = getIntent().getBooleanExtra("isFromEod", false);
            isFromEodsign = getIntent().getBooleanExtra("isFromEodsign", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        btn_cancel1 = (ImageView) findViewById(R.id.cancel_sketch);
        btn_cancel1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (isFromEod) {
                    alert_dialog();
                } else {
                    showAlert();
                }
                if (send)
                    finish();
            }
        });

        SeekBar brushSeek = (SeekBar) findViewById(R.id.brushSeek1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;

        drawView = (DrawingView) findViewById(R.id.drawing);

        Log.i("Sketch", "isFromEodsign==> " + isFromEodsign);
        //signature
        if (isFromEodsign) {
            Log.i("Sketch", "isFromEodsign ==> ## " + isFromEodsign);
            final LinearLayout.LayoutParams params_draw = (LinearLayout.LayoutParams) drawView.getLayoutParams();
            params_draw.height = 250;
            drawView.setLayoutParams(params_draw);
            drawView.requestLayout();
        }

        // get the palette and first color button
        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
        currPaint = (ImageView) paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        // draw button
        drawBtn = (ImageView) findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        show = (LinearLayout) findViewById(R.id.seek_show);

        // set initial size
        drawView.setBrushSize(size5);

        // erase button
        eraseBtn = (ImageView) findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

//		 new button;
//		 newBtn = (ImageButton) findViewById(R.id.new_btn);
//		 newBtn.setOnClickListener(this);

        // save button
        saveBtn = (ImageView) findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        galleryButt = (ImageView) findViewById(R.id.galleryButt);
        galleryButt.setOnClickListener(this);
        undoButt = (ImageView) findViewById(R.id.undoButt);
        undoButt.setOnClickListener(this);
//		redoButt = (ImageView) findViewById(R.id.redoButt);
//		redoButt.setOnClickListener(this);

        clearButt = (Button) findViewById(R.id.clearButt);
        addNew_sketch = (Button) findViewById(R.id.addSketch);
        if (isFromEod) {
            addNew_sketch.setVisibility(View.VISIBLE);
        }
        clearButt.setOnClickListener(this);
        addNew_sketch.setOnClickListener(this);

		/*sizeButt = (ImageView) findViewById(R.id.save_btn);
        sizeButt.setOnClickListener(this);*/

        brushSeek.setProgress(size5);
        brushSeek.setMax(30);
        OnSeekBarChangeListener seekListenerbrush = new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub
                if (check.equals("pencil")) {
                    drawView.setBrushSize(size5);
                    drawView.setLastBrushSize(size5);
                } else {
                    drawView.setBrushSize(size5);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub
                if (check.equals("pencil")) {
                    drawView.setBrushSize(size5);
                    drawView.setLastBrushSize(size5);
                } else {
                    drawView.setBrushSize(size5);
                }
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1,
                                          boolean arg2) {
                // TODO Auto-generated method stub
                size5 = arg1;
                if (arg1 == 0) {
                    size5 = 1;
//					arg1 = 1;
                }
            }
        };
        brushSeek.setOnSeekBarChangeListener(seekListenerbrush);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    // user clicked paint
    public void paintClicked(View view) {
        // use chosen color

        // set erase false
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());

        if (view != currPaint) {
            ImageView imgView = (ImageView) view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            // update ui
            imgView.setImageDrawable(getResources().getDrawable(
                    R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(
                    R.drawable.paint));
            currPaint = (ImageView) view;
        }
    }

    public boolean isCleared() {
        return isCleared;
    }

    public void setCleared(boolean isCleared) {
        this.isCleared = isCleared;
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.draw_btn) {
            check = "pencil";
            if (imageSelectedOption == 0) {
                if (StoredFilepath != null) {
                    File file = new File(StoredFilepath);
                    if (file.exists()) {
                        Log.i("handsketch123", "file name : " + file.getPath());
                        Bitmap bitMap = null;
                        bitMap = BitmapFactory.decodeFile(StoredFilepath);
                        int h = drawView.getHeight(); // 320; // Height in pixels
                        int w = drawView.getWidth();// 480; // Width in pixels
                        bitMap = Bitmap.createScaledBitmap(
                                BitmapFactory.decodeFile(StoredFilepath), w, h, true);
                        if (bitMap != null) {
                            Log.i("handsketch123", "bitmap not null");
                            drawView.setImage(bitMap);
                        }
                    }
                }
            } else if (imageSelectedOption == 1) {
                if (SelectedGallaryImage != null) {
                    Bitmap board;
                    String picturePath= getUriPath(this,SelectedGallaryImage);

//                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                    Cursor cursor = getContentResolver().query(SelectedGallaryImage,
//                            filePathColumn, null, null, null);
//                    cursor.moveToFirst();
//
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    String picturePath = cursor.getString(columnIndex);
//                    cursor.close();

                    if (picturePath != null) {
                        board = BitmapFactory.decodeFile(picturePath);
                        int h = drawView.getHeight(); // 320; // Height in pixels
                        int w = drawView.getWidth();// 480; // Width in pixels
                        board = Bitmap.createScaledBitmap(
                                BitmapFactory.decodeFile(picturePath), w, h, true);
                        drawView.setImage(board);
                    }
                }
            }

            drawView.setErase(false);

        } else if (view.getId() == R.id.erase_btn) {
            check = "erase";
            drawView.setErase(true);
        } else if (view.getId() == R.id.galleryButt) {
            if (Appreference.isImageSelected == true) {
                selectImage();
            } else if (drawView.count() == 0) {
                selectImage();
            }
        } else if (view.getId() == R.id.addSketch) {
            saveCurrentSketch();
        } else if (view.getId() == R.id.clearButt) {
            isCleared = true;
            drawView.startNew();
            Appreference.isImageSelected = true;
            SelectedGallaryImage = null;
            StoredFilepath = null;
            Log.i("handsketch123", "imageSelectedOption " + imageSelectedOption);
            if (imageSelectedOption == 0) {
//                File file = new File(StoredFilepath);
//                if (file.exists()) {
//                    Log.i("handsketch123", "file name : " + file.getPath());
//                    Bitmap bitMap = ResizeImage(StoredFilepath);
//                    if (bitMap != null) {
//                        Log.i("handsketch123", "bitmap not null");
////                        drawView.setImage(bitMap);
//                    }
//                }
                drawView.startNew();
            } else if (imageSelectedOption == 1) {
//                Bitmap board;
//
//                String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                Cursor cursor = getContentResolver().query(SelectedGallaryImage,
//                        filePathColumn, null, null, null);
//                assert cursor != null;
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                String picturePath = cursor.getString(columnIndex);
//                cursor.close();
//
//                // board=BitmapFactory.decodeFile(picturePath);
//                int h = drawView.getHeight(); // 320; // Height in pixels
//                int w = drawView.getWidth();// 480; // Width in pixels
//                board = Bitmap.createScaledBitmap(
//                        BitmapFactory.decodeFile(picturePath), w, h, true);
////                drawView.setImage(board);
                drawView.startNew();
            } else if (imageSelectedOption == 2) {
//                drawView.startNew();
            }
            if (drawView.count() > 0) {
                Toast.makeText(getApplicationContext(), "Cleared Successfully", Toast.LENGTH_SHORT).show();
            }

        } else if (view.getId() == R.id.save_btn) {
            if (drawView.count() > 0) {
                saveNote();
            } else {
                Toast.makeText(getApplicationContext(), "Please draw a sketch", Toast.LENGTH_SHORT).show();
            }
        } else if (view.getId() == R.id.undoButt) {
            if (drawView.count() == 0) {
                SelectedGallaryImage = null;
                StoredFilepath = null;
            }
            drawView.onClickUndo();
        }
    }

    private void selectImage() {
        final Dialog dialog = new Dialog(HandSketchActivity2.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_myacc_menu);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.horizontalMargin = 15;
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource((R.color.black));
        window.setAttributes(lp);
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
        TextView photo = (TextView) dialog.findViewById(R.id.delete_acc);
        photo.setText("Take Photo");
        TextView gallery = (TextView) dialog.findViewById(R.id.log_out);
        gallery.setText("Choose from Gallery");
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        photo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String file = folderPath + "Instruction_image"
                        + getFileName() + ".jpg";
                Intent intent = new Intent(HandSketchActivity2.this,
                        CustomVideoCamera.class);
                intent.putExtra("filePath", file);
                intent.putExtra("isPhoto", true);
                startActivityForResult(intent, CAMERA_REQUEST);
                dialog.dismiss();

            }
        });
        gallery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                if (Build.VERSION.SDK_INT < 19) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALLERY_REQUEST);
                } else {
                    Log.i("img", "sdk is above 19");
                    Log.i("clone", "====> inside gallery");
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, GALLERY_REQUEST);
                }
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,
//                        "Select Picture"), 1);


//
//                Intent intent = new Intent(
//                        Intent.ACTION_PICK,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, GALLERY_REQUEST);
                dialog.dismiss();
            }
        });
    }

    private void saveNote() {

        // save drawing

        // save drawing
        drawView.setDrawingCacheEnabled(true);
        // attempt to save
        File dir = new File(folderPath);
        Log.i("thgg", "#####################" + dir.exists());
        if (!dir.exists()) {
            dir.mkdir();
        }

        //File fle = new File(folderPath, "Sketch_file"+ getFileName() + ".jpg");
        filename = folderPath + "Sketch_file"
                + getFileName() + ".jpg";
        String imgSaved =
                MediaStore.Images.Media.insertImage(
                        getContentResolver(),
                        drawView.getDrawingCache(), filename, "drawing");
        Bitmap img = drawView.getDrawingCache();
        Log.i("sketch", "img==> " + img);
        Log.i("sketch", "filename==> " + filename);
        Log.i("Crop", "sketch getHeight==> " + img.getHeight());
        Log.i("Crop", "sketch getWidth==> " + img.getWidth());

        if (img != null) {
            BufferedOutputStream stream;
            try {
                stream = new BufferedOutputStream(new FileOutputStream(new File(filename)));
                img.compress(CompressFormat.JPEG, 75, stream);

                imgSaved = "saved";

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Appreference.printLog("HandSketchActivity2 saveNote", "Exception " + e.getMessage(), "WARN", null);
            }

        }
        // feedback
        if (imgSaved != null) {

        } else {
            Toast unsavedToast = Toast.makeText(
                    getApplicationContext(),
                    "Oops! Image could not be saved.",
                    Toast.LENGTH_SHORT);
            unsavedToast.show();
        }
        drawView.destroyDrawingCache();
        try {
            if (isFromEod) {
                try {
                    AlertDialog.Builder saveDialog = new AlertDialog.Builder(HandSketchActivity2.this);
                    saveDialog.setTitle("Hand sketch");
                    saveDialog.setCancelable(false);
                    saveDialog.setMessage("Do you want to crop this Image?");
                    saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog1, int which) {
                            Intent intent = new Intent(HandSketchActivity2.this, CropImage.class);
                            intent.putExtra("path", filename);
                            startActivityForResult(intent, 423);
                        }
                    });
                    saveDialog.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog1, int which) {
                                    Appreference.EodSketchList.add(filename);
                                    Log.i("check123", "Sketch File saveNote == No ===> " + Appreference.EodSketchList);
                                    finish();
                                }
                            });
                    saveDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("HandSketchActivity2 onKeyDown", "Exception " + e.getMessage(), "WARN", null);
                }
            } else {
                Intent i = new Intent();
                i.putExtra("path", filename);
                setResult(RESULT_OK, i);
                finish();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("HandSketchActivity2 saveNote", "Exception " + e.getMessage(), "WARN", null);
        }
    }


    public void saveCurrentSketch() {
        drawView.setDrawingCacheEnabled(true);
        // attempt to save
        File dir = new File(folderPath);
        Log.i("thgg", "#####################" + dir.exists());
        if (!dir.exists()) {
            dir.mkdir();
        }
        filename = folderPath + "Sketch_file"
                + getFileName() + ".jpg";
        String imgSaved =
                MediaStore.Images.Media.insertImage(
                        getContentResolver(),
                        drawView.getDrawingCache(), filename, "drawing");
        Bitmap img = drawView.getDrawingCache();
        Log.i("sketch", "img==> " + img);
        Log.i("sketch", "filename==> " + filename);
        Log.i("Crop", "sketch getHeight==> " + img.getHeight());
        Log.i("Crop", "sketch getWidth==> " + img.getWidth());

        if (img != null) {
            BufferedOutputStream stream;
            try {
                stream = new BufferedOutputStream(new FileOutputStream(new File(filename)));
                img.compress(CompressFormat.JPEG, 75, stream);

                imgSaved = "saved";

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Appreference.printLog("HandSketchActivity2 saveNote", "Exception " + e.getMessage(), "WARN", null);
            }

        }
//        Appreference.EodSketchList.add(filename);
//        Log.i("check123","Sketch File saveCurrentSketch ===> "+Appreference.EodSketchList);

        // feedback
        if (imgSaved != null) {

        } else {
            Toast unsavedToast = Toast.makeText(
                    getApplicationContext(),
                    "Oops! Image could not be saved.",
                    Toast.LENGTH_SHORT);
            unsavedToast.show();
        }
        try {
            if (isFromEod) {
                try {
                    AlertDialog.Builder saveDialog = new AlertDialog.Builder(HandSketchActivity2.this);
                    saveDialog.setTitle("Hand sketch");
                    saveDialog.setCancelable(false);
                    saveDialog.setMessage("Do you want to crop this Image?");
                    saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog1, int which) {
                            Intent intent = new Intent(HandSketchActivity2.this, CropImage.class);
                            intent.putExtra("path", filename);
                            startActivityForResult(intent, 424);
                        }
                    });
                    saveDialog.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog1, int which) {
                                    Appreference.EodSketchList.add(filename);
                                    Log.i("check123", "Sketch File saveCurrentSketch == No ===> " + Appreference.EodSketchList);
//                                    LayoutInflater layoutInflater =
//                                            (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                                    final View rowView = layoutInflater.inflate(R.layout.handsketch_layout_new, null,false);
//                                    // Add the new row before the add field button.
//                                    parentLinearLayout.addView(rowView);
                                    Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                                    i.putExtra("isFromEod", true);
                                    i.putExtra("isFromEodsign", false);
                                    startActivity(i);
                                    finish();
                                }
                            });
                    saveDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("HandSketchActivity2 onKeyDown", "Exception " + e.getMessage(), "WARN", null);
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("HandSketchActivity2 saveNote", "Exception " + e.getMessage(), "WARN", null);
        }

    }

    public Bitmap ResizeImage(String filePath) {
        try {

            DisplayMetrics displaymetrics = new DisplayMetrics();
            final WindowManager w = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            final Display d = w.getDefaultDisplay();
//			final DisplayMetrics m = new DisplayMetrics();
            d.getMetrics(displaymetrics);

            int noScrHeight = displaymetrics.heightPixels;
            int noScrWidth = displaymetrics.widthPixels;
            Log.i("HSketch", " noScrHeight : " + noScrHeight + " noScrWidth : " + noScrWidth);

            Bitmap bitMapImage = null;
            Options options = new Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
            if (bmp != null)
                bmp.recycle();
            double sampleSize = 0;
            Boolean scaleByHeight = Math.abs(options.outHeight - noScrHeight) >= Math
                    .abs(options.outWidth - noScrWidth);

            if (options.outHeight * options.outWidth * 2 >= 1638) {

                sampleSize = scaleByHeight ? options.outHeight / noScrHeight
                        : options.outWidth / noScrWidth;
                sampleSize = (int) Math.pow(2d,
                        Math.floor(Math.log(sampleSize) / Math.log(2d)));
            }

            options.inJustDecodeBounds = false;
            options.inTempStorage = new byte[128];
            while (true) {
                try {
                    System.gc();
                    options.inSampleSize = (int) sampleSize;
                    bitMapImage = BitmapFactory.decodeFile(filePath, options);
                    break;
                } catch (Exception ex) {
                    Appreference.printLog("HandSketchActivity2 ResizeImage", "Exception " + ex.getMessage(), "WARN", null);
                    try {
                        sampleSize = sampleSize * 2;
                    } catch (Exception ex1) {
                        Appreference.printLog("HandSketchActivity2 ResizeImage", "Exception " + ex1.getMessage(), "WARN", null);
                    }
                }
            }
            return bitMapImage;
        } catch (OutOfMemoryError e) {
            Log.e("bitmap", "===> " + e.getMessage());
            e.printStackTrace();
            Appreference.printLog("HandSketchActivity2 ResizeImage", "Exception " + e.getMessage(), "WARN", null);
            return null;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            Appreference.printLog("HandSketchActivity2 ResizeImage", "Exception " + e.getMessage(), "WARN", null);
            return null;
        }
    }

    public static String getFileName() {
        String strFilename = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
            Date date = new Date();
            strFilename = dateFormat.format(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
            Appreference.printLog("HandSketchActivity2 getFileName", "Exception " + e.getMessage(), "WARN", null);
        }
        return strFilename;
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        try {
            Appreference.isImageSelected = false;
            if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
                String filePath = data.getStringExtra("filePath");
                StoredFilepath = filePath;
                imageSelectedOption = 0;
                File file = new File(filePath);
                if (file.exists()) {


                    Log.i("handsketch123", "file name : " + file.getPath());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("handsketch123", "Delayed han :");
                            drawBtn.performClick();
                        }
                    }, 2000);
//							Bitmap bitMap = ResizeImage(filePath);
//							if (bitMap != null) {
//								Log.i("handsketch123", "bitmap not null");
////						Bitmap output = bitMap.createBitmap(200,200,
////								Bitmap.Config.ARGB_8888);
//								Log.i("handsketch123", "bitmap name : " + bitMap);
////						drawView.setImage(bitMap);
////						int h = drawView.getHeight(); // 320; // Height in pixels
////						int w = drawView.getWidth();// 480; // Width in pixels
////						drawView.setImage(Bitmap.createScaledBitmap(bitMap, w, h, true));
//								drawView.setImage(bitMap);
//								drawView.setErase(false);
////								drawBtn.post(new Runnable() {
////									@Override
////									public void run() {
//										drawBtn.performClick();
////									}
////								});
//
//							}
                }
            } else if (requestCode == GALLERY_REQUEST
                    && resultCode == RESULT_OK && data != null) {
                imageSelectedOption = 1;
                SelectedGallaryImage = data.getData();
//                Uri selectedImageUri = data.getData();
//
//                String SelectedGallaryImage1 = getUriPath(this,selectedImageUri);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("handsketch123", "Delayed han :");
                        drawBtn.performClick();
                    }
                }, 2000);
            } else if (requestCode == 423) {
                String crop_image = data.getStringExtra("path");
                Appreference.EodSketchList.add(crop_image);
                Log.i("check123", "Sketch File requestCode == 423 ===> " + Appreference.EodSketchList);
//                EodScreen eodScreen=(EodScreen)Appreference.context_table.get("eodscreen");
//                if(eodScreen!=null){
//                    eodScreen.getMySketch_FileNames(Appreference.EodSketchList);
//                }
//                Log.i("Crop", "sketch crop_image ==> " + crop_image);
//                Intent i = new Intent();
//                i.putExtra("path", crop_image);
//                i.putExtra("sss", Appreference.EodSketchList);
//                setResult(RESULT_OK, i);
                finish();

            } else if (requestCode == 424) {
                String crop_image = data.getStringExtra("path");
                Appreference.EodSketchList.add(crop_image);
                Log.i("check123", "Sketch File requestCode == 424 ===> " + Appreference.EodSketchList);

                Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                i.putExtra("isFromEod", true);
                i.putExtra("isFromEodsign", false);
                startActivity(i);
                finish();

            }
//				}
//			});
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("HandSketchActivity2 onActivityResult", "Exception " + e.getMessage(), "WARN", null);
        }

    }

    public static String getUriPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    public void alert_dialog() {
        try {
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(HandSketchActivity2.this);
            saveDialog.setTitle("Hand sketch");
            saveDialog.setCancelable(false);
            saveDialog.setMessage("Are you sure want to go back?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog1, int which) {
                    Appreference.EodSketchList.clear();
                    finish();
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                }
            });
            saveDialog.setNegativeButton("No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog1, int which) {
                        }
                    });
            saveDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("HandSketchActivity2 onKeyDown", "Exception " + e.getMessage(), "WARN", null);
        }
    }

    private void showAlert() {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String message;
            if (drawView.count() > 0) {
                if (!send)
                    message = "Are you sure want to Save and Go Back?";
                else
                    message = "Are you sure want to Send and Go Back?";
            } else {
                message = "Are you sure want to Exit?";
            }
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    if (drawView.count() > 0) {
                                        saveNote();
                                        dialog.dismiss();
                                    } else {
                                        setCleared(true);
                                        finish();
//                                        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                                    }


                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.dismiss();
//                                    setCleared(true);
//                                    finish();
//                                    overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("HandSketchActivity2 showAlert", "Exception " + e.getMessage(), "WARN", null);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            alert_dialog();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    /*	client.connect();
        SwipeDetector.Action viewAction = SwipeDetector.Action.newAction(
				SwipeDetector.Action.TYPE_VIEW, // TODO: choose an action type.
				"HandSketchActivity2 Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://com.myapplication3/http/host/path")
		);
		AppIndex.AppIndexApi.start(client, viewAction);*/
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
    /*	// See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"HandSketchActivity2 Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://com.myapplication3/http/host/path")
		);
		AppIndex.AppIndexApi.end(client, viewAction);
		client.disconnect();*/
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
        float maxWidth = 816.0f;
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
        Canvas canvas = new Canvas(scaledBitmap != null ? scaledBitmap : null);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(imageUri);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
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

    public Bitmap convertpathToBitmap(String strIPath) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        return BitmapFactory.decodeFile((compressImage(strIPath)), bmOptions);
    }
}
