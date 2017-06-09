package com.myapplication3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Ramdhas on 14-09-2016.
 */
public class TaskConversationTagFilter extends Activity {
    TextView Audio,Image,Video,Reminder,Text,remove,Date,Completion,TaskDescription,Private,cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_filter_selection);

        Audio=(TextView)findViewById(R.id.filter_audio);
        Image=(TextView)findViewById(R.id.filter_image);
        Date=(TextView)findViewById(R.id.filter_date);
        Video=(TextView)findViewById(R.id.filter_video);
        Reminder=(TextView)findViewById(R.id.filter_reminder);
        Text=(TextView)findViewById(R.id.filter_text);
        Completion=(TextView)findViewById(R.id.filter_completion);
        TaskDescription=(TextView)findViewById(R.id.filter_TaskDescription);
        Private=(TextView)findViewById(R.id.filter_private);
        remove=(TextView)findViewById(R.id.removeAllTag);
        cancel=(TextView)findViewById(R.id.ca);

        Audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("tagFilter","Audio");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("tagFilter","Date");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        Video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("tagFilter","Video");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("tagFilter","Image");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        Reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("tagFilter","Reminder");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("tagFilter","Text");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        Completion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("tagFilter","Completion");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        TaskDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("tagFilter","TaskDescription");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        Private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("tagFilter", "Private");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("tagFilter","RemoveAll");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
