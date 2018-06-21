package com.ase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by Preethi on 2/28/2018.
 */

public class FullScreenViewActivity extends Activity {

    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;
    Button btnCrop,btnClose_pdf;
    boolean isfromCONVcrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);
        Appreference.PDFCropPageImageList.clear();
        viewPager = (ViewPager) findViewById(R.id.pager);

        btnCrop = (Button) findViewById(R.id.btnCrop);

        btnClose_pdf = (Button) findViewById(R.id.btnClose_pdf);


        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        boolean isfromPDFcrop = i.getBooleanExtra("fromPDFcrop", false);
         isfromCONVcrop = i.getBooleanExtra("fromConvCrop", false);
        final ArrayList<String> getFilePaths = i.getStringArrayListExtra("pathSketch");

        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
                getFilePaths);

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);

        if(isfromPDFcrop){
            btnCrop.setVisibility(View.VISIBLE);
        }else{
            btnCrop.setVisibility(View.GONE);
        }
        btnCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewPager.getCurrentItem();
                Intent intent = new Intent(FullScreenViewActivity.this, CropImage.class);
                intent.putExtra("path", getFilePaths.get(position));
                if(isfromCONVcrop){
                    intent.putExtra("fromConvCrop", true);
                }
                startActivityForResult(intent, 433);
            }
        });
        btnClose_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putStringArrayListExtra("pathpdf", Appreference.PDFCropPageImageList);
//                i.putExtra("path", Appreference.PDFCropPageImageList);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==433){
            try{
                String path = data.getStringExtra("path");
                Log.i("pdf123", "ful screen view requestCode==> 433  !! " +path);
                Appreference.PDFCropPageImageList.add(path);
                if(isfromCONVcrop){
                    Intent i = new Intent();
                    i.putStringArrayListExtra("pathpdf", Appreference.PDFCropPageImageList);
                    setResult(RESULT_OK, i);
                    finish();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
