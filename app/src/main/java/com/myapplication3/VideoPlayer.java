package com.myapplication3;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;



import org.pjsip.pjsua2.app.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class VideoPlayer extends Activity {
	VideoView mVideoView;
	ImageView mBack;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.video_player_new);
		context = this;
//		WebServiceReferences.contextTable.put("customvideoplayer", context);
		mVideoView = (VideoView) findViewById(R.id.video_view);
		mBack=(ImageView)findViewById(R.id.back);

		mBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		MediaController mediaController = new MediaController(this);
		mediaController.setAnchorView(mVideoView);
		 int msec = getIntent().getExtras().getInt("timevideo",0);
		String filePath = getIntent().getStringExtra("video");
		Log.i("Task","imagename"+"Inside onCreate videoplayer == "+filePath);
		if (filePath == null)
			filePath = getIntent().getStringExtra("File_Path");

		if (filePath != null) {
			try {
				// start 07-10-15 changes

				filePath = decryptFile(context,filePath);

				// ended 07-10-15 changes


				Uri uri = Uri.parse(filePath);
				mVideoView.setMediaController(mediaController);
				mVideoView.setVideoURI(uri);
				mVideoView.requestFocus();
				mVideoView.seekTo(msec);
				mVideoView
						.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
							@Override
							public void onCompletion(MediaPlayer mp) {
								VideoPlayer.this.finish();
							}
						});
				mVideoView.start();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(),
						"Unable to play video.", Toast.LENGTH_SHORT).show();
				VideoPlayer.this.finish();
			}
		} else {
			Toast.makeText(getApplicationContext(), "Video Filepath is Wrong.",
					Toast.LENGTH_SHORT).show();
			VideoPlayer.this.finish();
		}
	}

    @Override
    protected void onResume() {
        super.onResume();
//		MainActivity.inActivity = this;
    }

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
			Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		WebServiceReferences.contextTable.remove("customvideoplayer");
		super.onDestroy();
	}
	private static boolean flag = false;
    public static String seceretKey = "comcryptkey00000";

    public static String decryptFile(Context context,String filePath) {
		try {
			if(flag) {
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
		}
		return filePath;
	}

	public static byte[] decrypt(String seed, byte[] encrypted) throws Exception {
		byte[] rawKey =seed.getBytes();// getRawKey(seed.getBytes());
		byte[] result = decrypt(rawKey, encrypted);
		return result;
	}

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(raw);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec,ivParameterSpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }
}
