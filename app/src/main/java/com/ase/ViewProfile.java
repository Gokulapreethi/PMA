package com.ase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.DB.VideoCallDataBase;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by vignesh on 10/11/2016.
 */
public class ViewProfile extends Activity {

    TextView ed1, ed2, ed3, editText5, editText6, editText7, editText8 , spn,spinner1,spinner2,spinner3,spinner4,spinner5,textView9, textView10,role,profession,specialisatiion,organisation;
    Context context;
    ArrayList<String> list;
    ImageLoader imageLoader;
    //Button performance,currentTask;
    TextView performance,currentTask;
    ImageView viewback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewprofile);

        final String mail = getIntent().getStringExtra("value");

        ed1 = (TextView) findViewById(R.id.editText);
        ed2 = (TextView) findViewById(R.id.editText2);
        //ed3 = (EditText) findViewById(R.id.editText3);
        spn = (TextView) findViewById(R.id.spinner);
        spinner1=(TextView) findViewById(R.id.spinner1);
        spinner2=(TextView) findViewById(R.id.spinner2);
        spinner3=(TextView) findViewById(R.id.spinner3);
        spinner4=(TextView) findViewById(R.id.spinner4);
        spinner5=(TextView) findViewById(R.id.spinner5);

        viewback=(ImageView)findViewById(R.id.image);

        //performance=(Button)findViewById(R.id.performance);
        //currentTask=(Button)findViewById(R.id.currenttask);
        performance=(TextView)findViewById(R.id.performancetext);
        currentTask=(TextView)findViewById(R.id.currenttext);

        editText5 = (TextView) findViewById(R.id.editText5);
        editText6 = (TextView) findViewById(R.id.editText6);
        editText7 = (TextView) findViewById(R.id.editText7);
        editText8 = (TextView) findViewById(R.id.editText8);
        textView9 = (TextView) findViewById(R.id.editText9);
        textView10 = (TextView) findViewById(R.id.editText10);
        role = (TextView) findViewById(R.id.spinner2);
        profession = (TextView) findViewById(R.id.spinner3);
        specialisatiion = (TextView) findViewById(R.id.spinner4);
        organisation = (TextView) findViewById(R.id.spinner5);
        textView9.setBackground(null);
        textView10.setBackground(null);

        list = new ArrayList<String>();
        list = VideoCallDataBase.getDB(context).getcontactdetails(mail);

        ed1.setText(list.get(0));
        ed2.setText(list.get(1));
        spinner1.setText(list.get(2));
        spn.setText(list.get(3));
        editText5.setText(list.get(5));
        editText6.setText(list.get(6));
        editText7.setText(list.get(7));
        editText8.setText(list.get(8));
        textView9.setText(list.get(9));
        textView10.setText(list.get(10));
        role.setText(list.get(11));
        profession.setText(list.get(12));
        specialisatiion.setText(list.get(13));
        organisation.setText(list.get(14));
        viewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        performance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfile.this, Viewvideo.class);
                intent.putExtra("filename","Performance");
                intent.putExtra("username",mail);
                Log.i("profile","performance clicked");
                startActivity(intent);
            }
        });

        currentTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewProfile.this, Viewvideo.class);
                intent.putExtra("filename","Current Task");
                intent.putExtra("username",mail);
                Log.i("profile","currentTask Clicked");
                startActivity(intent);
            }
        });

        textView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textView10.getText().toString().equalsIgnoreCase("")) {
                    Intent intent = new Intent(ViewProfile.this, Viewvideo.class);
                    intent.putExtra("filename",textView10.getText().toString());
                    Log.i("profile",textView9.getText().toString());
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(),"No Video Profile",Toast.LENGTH_SHORT).show();

            }
        });
        textView9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!textView9.getText().toString().equalsIgnoreCase("")) {
                    Intent intent = new Intent(ViewProfile.this, Viewvideo.class);
                    intent.putExtra("filename",textView9.getText().toString());
                    Log.i("profile",textView9.getText().toString());
                    startActivity(intent);
                }
                else
                    Toast.makeText(getApplicationContext(),"No Text Profile",Toast.LENGTH_SHORT).show();

            }
        });
        ImageView imageView = (ImageView) findViewById(R.id.imageView_round);

        File myFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + list.get(4));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(4) != null) {
                    Intent intent=new Intent(getApplicationContext(),FullScreenImage.class);
                    intent.putExtra("image",Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + list.get(4));
                    startActivity(intent);
                }

            }
        });
        if (list.get(4) != null) {
            if (myFile.exists()) {
                imageLoader = new ImageLoader(context);
                Log.i("Appreference", "if-------->" + list.get(4));
                imageLoader.DisplayImage(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/profilePic/" + list.get(4), imageView, R.drawable.default_person_circle);
            } else {
                Picasso.with(getApplicationContext()).load(getResources().getString(R.string.user_upload) + list.get(4)).into(imageView);
            }

        }

    }
}
