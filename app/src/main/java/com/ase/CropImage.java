package com.ase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;
import com.isseiaoki.simplecropview.util.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by prasanth on 9/18/2017.
 */
public class CropImage extends Activity {
    private CropImageView mCropView;
    private Uri sourceUri = null;
    private Uri saveUri = null;
    private Uri path_uri = null;
    ImageView sentBtn,preview_image;
    TextView back;
    String strpath;
    public static String folderPath = Environment.getExternalStorageDirectory() + "/High Message/";
    static String filename;
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;
    static String path_intent = null;
    private boolean isfromCONVcrop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.cropimage_basic);

        mCropView = (CropImageView) findViewById(R.id.cropImageView);
        sentBtn = (ImageView) findViewById(R.id.sentBtn);
        preview_image = (ImageView) findViewById(R.id.preview_image);
        back = (TextView) findViewById(R.id.back);
        try {
            strpath = getIntent().getStringExtra("path");
            isfromCONVcrop = getIntent().getBooleanExtra("fromConvCrop", false);
            Log.i("Crop", "sketch strpath==> " + strpath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(isfromCONVcrop)
            preview_image.setVisibility(View.VISIBLE);
        else
            preview_image.setVisibility(View.GONE);
        if (sourceUri == null) {
            // default data
//            sourceUri = getUriFromDrawableResId(this, R.drawable.sample5);
            sourceUri = Uri.fromFile(new File(strpath));
        }
        mCropView.setCropMode(CropImageView.CropMode.FREE);
        mCropView.load(sourceUri).execute(new LoadCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
        preview_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preview_croppedImage();
            }
        });
        sentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Crop", "sendbtn click");
                cropImage();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mCropView.load(sourceUri).executeAsCompletable();

        mCropView.crop(sourceUri)
                .execute(new CropCallback() {
                    @Override
                    public void onSuccess(Bitmap cropped) {
                        mCropView.save(cropped)
                                .execute(saveUri, new SaveCallback() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });

        mCropView.crop(sourceUri)
                .executeAsSingle()
                .flatMap(new Function<Bitmap, SingleSource<Uri>>() {
                    @Override
                    public SingleSource<Uri> apply(@io.reactivex.annotations.NonNull Bitmap bitmap)
                            throws Exception {
                        return mCropView.save(bitmap)
                                .executeAsSingle(saveUri);
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Uri uri) throws Exception {
                        // on success
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable)
                            throws Exception {
                        // on error
                    }
                });
    }

    public static Uri getUriFromDrawableResId(Context context, int drawableResId) {
        StringBuilder builder = new StringBuilder().append(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .append("://")
                .append(context.getResources().getResourcePackageName(drawableResId))
                .append("/")
                .append(context.getResources().getResourceTypeName(drawableResId))
                .append("/")
                .append(context.getResources().getResourceEntryName(drawableResId));
        return Uri.parse(builder.toString());
    }

    public void startResultActivity(Uri uri) {
        if (isFinishing()) return;
        try {
            Log.i("Crop", "filename path_intent ==> " + path_intent);
            if (path_intent != null) {
                Intent i = new Intent();
                i.putExtra("path", path_intent);
                setResult(RESULT_OK, i);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* public static Intent createIntent(Activity activity, Uri uri) {
        Intent intent = new Intent(activity, ResultActivity.class);
        intent.setData(uri);
        Log.i("Crop", "sketch uri Intent ==> " + uri);
        return intent;
    }*/

    public Uri createSaveUri() {
        return createNewUri(this, mCompressFormat);
    }

    public static String getDirPath() {
        String dirPath = "";
        File imageDir = null;
        File extStorageDir = Environment.getExternalStorageDirectory();
        if (extStorageDir.canWrite()) {
            imageDir = new File(extStorageDir.getPath() + "/simplecropview");
        }
        if (imageDir != null) {
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }
            if (imageDir.canWrite()) {
                dirPath = imageDir.getPath();
            }
        }
        return dirPath;
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


    public static Uri createNewUri(Context context, Bitmap.CompressFormat format) {
        long currentTimeMillis = System.currentTimeMillis();
        Date today = new Date(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String title = dateFormat.format(today);
//        String dirPath = getDirPath();
        String dirPath = folderPath;
        filename = "Sketch_file"
                + getFileName() + ".jpg";
//        String fileName = "scv" + title + "." + "jpeg";
//        String path = dirPath + "/" + filename;
        path_intent = folderPath + filename;
        File file = new File(path_intent);
        Log.i("Crop", "sketch path # ==> " + path_intent);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/" + "jpg");
        values.put(MediaStore.Images.Media.DATA, path_intent);
        long time = currentTimeMillis / 1000;
        values.put(MediaStore.MediaColumns.DATE_ADDED, time);
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, time);
        if (file.exists()) {
            values.put(MediaStore.Images.Media.SIZE, file.length());
        }

        ContentResolver resolver = context.getContentResolver();
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Logger.i("SaveUri = " + uri);
        Log.i("Crop", "sketch uri # ==> " + uri);
//        Uri uri = Uri.fromFile(new File(path));
        return uri;
    }

    private Disposable cropImage() {
        return mCropView.crop(sourceUri)
                .executeAsSingle()
                .flatMap(new Function<Bitmap, SingleSource<Uri>>() {
                    @Override
                    public SingleSource<Uri> apply(@io.reactivex.annotations.NonNull Bitmap bitmap)
                            throws Exception {
                        return mCropView.save(bitmap)
                                .compressFormat(mCompressFormat)
                                .executeAsSingle(createSaveUri());
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Disposable disposable)
                            throws Exception {
//                        showProgress();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
//                        dismissProgress();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Uri uri) throws Exception {
                        startResultActivity(uri);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable)
                            throws Exception {
                    }
                });
    }

    private Disposable preview_croppedImage() {
        return mCropView.crop(sourceUri)
                .executeAsSingle()
                .flatMap(new Function<Bitmap, SingleSource<Uri>>() {
                    @Override
                    public SingleSource<Uri> apply(@io.reactivex.annotations.NonNull Bitmap bitmap)
                            throws Exception {
                        return mCropView.save(bitmap)
                                .compressFormat(mCompressFormat)
                                .executeAsSingle(createSaveUri());
                    }
                })
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Disposable disposable)
                            throws Exception {
//                        showProgress();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
//                        dismissProgress();
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Uri uri) throws Exception {
                        startpreviewActivity(uri);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable)
                            throws Exception {
                    }
                });
    }

    private void startpreviewActivity(Uri uri) {
        if (isFinishing()) return;
        try {
            Log.i("Crop", "filename path_intent ==> " + path_intent);
            if (path_intent != null) {
                Intent intent=new Intent(getApplicationContext(),FullScreenImage.class);
                intent.putExtra("image",path_intent);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

