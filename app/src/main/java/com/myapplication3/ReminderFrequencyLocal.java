package com.myapplication3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by thirumal on 07-09-2016.
 */
public class ReminderFrequencyLocal  extends Activity {

    TextView everyminute, every_10_minute, daily, hourly, weekly, monthly, yearly, none, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_frequency_local);
        //String title=getIntent().getStringExtra("TimeFrequency");
        everyminute = (TextView) findViewById(R.id.everyminute);
        every_10_minute = (TextView) findViewById(R.id.every_10_minute);
        daily = (TextView) findViewById(R.id.daily);
        none = (TextView) findViewById(R.id.none);
        hourly = (TextView) findViewById(R.id.hourly);
        weekly = (TextView) findViewById(R.id.weekly);
        monthly = (TextView) findViewById(R.id.monthly);
        yearly = (TextView) findViewById(R.id.yearly);
        cancel = (TextView) findViewById(R.id.ca);

        none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "None");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        everyminute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "Every minute");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        hourly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "Hourly");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "Daily");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        every_10_minute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "Every 10 min");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "Week day");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "Monthly");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "Yearly");
                setResult(RESULT_OK, intent);
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

