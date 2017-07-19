package com.ase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by thirumal on 07-09-2016.
 */
public class ReminderFrequencySelection extends Activity {
    TextView everyminute, every_10_minute, daily, hourly, weekly, monthly, yearly, none, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_frequency_selection);

        everyminute = (TextView) findViewById(R.id.everyminute);

        daily = (TextView) findViewById(R.id.daily);

        hourly = (TextView) findViewById(R.id.hourly);

        cancel = (TextView) findViewById(R.id.ca);



        everyminute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "Minutes");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        hourly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "Hours");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "Days");
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
