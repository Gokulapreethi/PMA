package com.ase;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FullScreenImage extends Activity {
	ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.image_view);
		TextView cancel = (TextView) findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		try {
			String filePath = getIntent().getStringExtra("image");
			Log.i("Profile","path=>"+filePath);

			if (filePath != null) {
				File file = new File(filePath);

				if (file.exists()) {

					// start 07-10-15 changes //

					Bitmap bitmap = decryptBitmap(file.getAbsolutePath());
					if(Appreference.getDeviceName()!=null && Appreference.getDeviceName().equalsIgnoreCase("motorola MotoG3")){
						// start chages for image quality 04-10-2017

//						int size = 10;
//						Bitmap bitmapsimplesize = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / size, bitmap.getHeight() / size, true);
						int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
						Bitmap bitmapsimplesize = Bitmap.createScaledBitmap(bitmap, 512, nh, true);
                        // end 04-10-2017 changes //
						bitmap.recycle();
						LinearLayout layout = (LinearLayout) findViewById(R.id.layoutView);

						TouchImageView touch = new TouchImageView(this);
						touch.setLayoutParams(new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT,
								LinearLayout.LayoutParams.MATCH_PARENT));
						touch.setImageBitmap(bitmapsimplesize);
						touch.setMaxZoom(3f);

						layout.addView(touch);
					}else {
//					bitmap = ImageViewer.getResizedBitmap(bitmap,480,640);

						// end 07-10-15 changes //

//					Bitmap bitmap = ImageUtils.decodeScaledBitmapFromSdCard(
//							file.getAbsolutePath(), 640, 480);

						LinearLayout layout = (LinearLayout) findViewById(R.id.layoutView);

						TouchImageView touch = new TouchImageView(this);
						touch.setLayoutParams(new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.MATCH_PARENT,
								LinearLayout.LayoutParams.MATCH_PARENT));
						touch.setImageBitmap(bitmap);
						touch.setMaxZoom(3f);

						layout.addView(touch);
					}

				} else {
					showToastAndClose("Image file does not exist.");
				}
			} else {
				showToastAndClose("Image Filepath is Wrong.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			showToastAndClose("Unable to download the file.");
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			showToastAndClose("Unable to download the file.");
		}
	}

    @Override
    protected void onResume() {
        super.onResume();
//        AppMainActivity.inActivity = this;
    }

    private void showToastAndClose(String message) {

		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
		FullScreenImage.this.finish();
	}

	private static boolean flag = false;
	public static String seceretKey = "comcryptkey00000";
	public static Bitmap decryptBitmap(String filePath) {
		try {
			if(flag) {
				File file = new File(filePath);
				if (file.exists()) {
					FileInputStream fileInputStream = new FileInputStream(file);
					byte[] bytes = new byte[(int) file.length()];
					fileInputStream.read(bytes);
					fileInputStream.close();

					byte[] decrypted = decrypt(seceretKey, bytes);

					return BitmapFactory.decodeByteArray(decrypted, 0,
							decrypted.length);
				}
			}else{
				return BitmapFactory.decodeFile(filePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// may file not encrypt so we try this
			try {
				return BitmapFactory.decodeFile(filePath);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return null;
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
