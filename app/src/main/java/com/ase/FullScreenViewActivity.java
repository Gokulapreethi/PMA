package com.ase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;

/**
 * Created by Preethi on 2/28/2018.
 */

public class FullScreenViewActivity extends Activity {

    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_view);

        viewPager = (ViewPager) findViewById(R.id.pager);


        Intent i = getIntent();
        int position = i.getIntExtra("position", 0);
        ArrayList<String> getFilePaths = i.getStringArrayListExtra("pathSketch");
//       ArrayList<String> getFilePaths = null;
//        getFilePaths.add("/storage/emulated/0/High Message/Sketch_file28022018185845.jpg");

        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this,
                getFilePaths);

        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(position);
    }
}