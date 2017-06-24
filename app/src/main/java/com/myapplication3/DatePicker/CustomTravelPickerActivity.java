package com.myapplication3.DatePicker;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.myapplication3.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CustomTravelPickerActivity extends Activity {

    private Context context;
    String taskNameshow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_end_time_show);
        context=this;
        taskNameshow=getIntent().getStringExtra("taskName");
        final java.sql.Date todayDate = new java.sql.Date(System.currentTimeMillis());
        Button travelstart_sign = (Button) findViewById(R.id.travelstart_sign);
        Button travelend_sign = (Button) findViewById(R.id.travelend_sign);


        final TextView project_name = (TextView) findViewById(R.id.project_name);
        final TextView travel_start = (TextView) findViewById(R.id.travel_start);
        final TextView travel_end = (TextView)findViewById(R.id.travel_end);
        final TextView back = (TextView) findViewById(R.id.back);
        final TextView send_travel = (TextView)findViewById(R.id.send_travel_completion);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });
        project_name.setText(taskNameshow);
        travelstart_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        try {
                            DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(context, true, new DatePickerPopWin.OnDatePickedListener() {

                        @Override
                        public void onDatePickCompleted(int month, int day, int year, int hour, int minute, String am, String dateDesc) {
                            Log.i("desc123","inside onDatePickedCompleted");
                            String inputPattern = "M-dd-yyyy HH:mm aa";
                            String outputPattern = "yyyy-MM-dd HH:mm aa";
                            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                            Date date = null;
                            String str = null;

                            try {
                                date = inputFormat.parse(dateDesc);
                                str = outputFormat.format(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            travel_start.setText(str);
                        }
                    }).textConfirm("DONE") //text of confirm button
                            .textCancel("CANCEL") //text of cancel button
                            .btnTextSize(16) // button text size
                            .viewTextSize(25) // pick view text size
                            .minYear(Calendar.getInstance().get(Calendar.YEAR)) //min year in loop
                            .maxYear(2550) // max year in loop
                            .dateChose(todayDate.toString()) // date chose when init popwindow
                            .build();
                    Log.i("desc123","inside DatePickerPopWin.Builder");

                    pickerPopWin.showPopWin(CustomTravelPickerActivity.this);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });


        travelend_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(context, true, new DatePickerPopWin.OnDatePickedListener() {

                        @Override
                        public void onDatePickCompleted(int month, int day, int year, int hour, int minute, String am, String dateDesc) {

                            Log.i("desc123","inside onDatePickedCompleted");
                            travel_end.setText(dateDesc);
                        }
                    }).textConfirm("DONE") //text of confirm button
                            .textCancel("CANCEL") //text of cancel button
                            .btnTextSize(16) // button text size
                            .viewTextSize(25) // pick view text size
                            .minYear(Calendar.getInstance().get(Calendar.YEAR)) //min year in loop
                            .maxYear(2550) // max year in loop
                            .dateChose(todayDate.toString()) // date chose when init popwindow
                            .build();
                    Log.i("desc123","inside DatePickerPopWin.Builder");

                    pickerPopWin.showPopWin(CustomTravelPickerActivity.this);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        send_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
