package com.ase.escalations;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ase.R;

/**
 * Created by saravanan on 2/13/2017.
 */
public class EscalationOptionsValuesActivity extends Activity {
    TextView status, per_completion, daily, hourly, weekly, monthly, yearly, none, cancel, buzz,aband;
    String valueType, projectValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scalation_option_values_activity);
        //String title=getIntent().getStringExtra("TimeFrequency");
        status = (TextView) findViewById(R.id.everyminute);
        per_completion = (TextView) findViewById(R.id.every_10_minute);
        daily = (TextView) findViewById(R.id.daily);
//        none = (TextView) findViewById(R.id.none);
        hourly = (TextView) findViewById(R.id.hourly);
        weekly = (TextView) findViewById(R.id.weekly);
        monthly = (TextView) findViewById(R.id.monthly);
        yearly = (TextView) findViewById(R.id.yearly);
        buzz = (TextView) findViewById(R.id.buzzz);
        aband = (TextView) findViewById(R.id.aband_es);
        cancel = (TextView) findViewById(R.id.ca);

        valueType = getIntent().getExtras().getString("valueType");
        projectValue = getIntent().getExtras().getString("projectValue");

        if (valueType.equalsIgnoreCase("event")) {
            status.setVisibility(View.VISIBLE);
            per_completion.setVisibility(View.VISIBLE);
        } else if (valueType.equalsIgnoreCase("value")) {
            if (projectValue != null && projectValue.equalsIgnoreCase("yes")) {
                hourly.setVisibility(View.GONE);
            } else {
                hourly.setVisibility(View.VISIBLE);
            }
            daily.setVisibility(View.VISIBLE);
        } else if (valueType.equalsIgnoreCase("action")) {
            weekly.setVisibility(View.VISIBLE);
            monthly.setVisibility(View.VISIBLE);
            yearly.setVisibility(View.VISIBLE);
            buzz.setVisibility(View.VISIBLE);
            aband.setVisibility(View.VISIBLE);
        }


       /* none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "None");
                setResult(RESULT_OK, intent);
                finish();
            }
        });*/
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "status");
                intent.putExtra("valueType", valueType);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        hourly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "assigned");
                intent.putExtra("valueType", valueType);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "overdue");
                intent.putExtra("valueType", valueType);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        per_completion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "% completion");
                intent.putExtra("valueType", valueType);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "Reassign");
                intent.putExtra("valueType", valueType);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "Audio Conference");
                intent.putExtra("valueType", valueType);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "Add observer");
                intent.putExtra("valueType", valueType);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        buzz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "Buzz");
                intent.putExtra("valueType", valueType);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        aband.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("TimeFrequency", "Aband");
                intent.putExtra("valueType", valueType);
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