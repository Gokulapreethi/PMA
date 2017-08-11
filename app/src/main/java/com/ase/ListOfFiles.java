package com.ase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Ramdhas on 15-09-2016.
 */
public class ListOfFiles extends Activity {
    TextView everyminute, daily, hourly, weekly, monthly, yearly, none, cancel;
    LinearLayout cancel_layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_frequency_selection);

        everyminute = (TextView) findViewById(R.id.everyminute);
        everyminute.setText("Chat File List");
        daily = (TextView) findViewById(R.id.daily);
        daily.setText("All File List");
        none = (TextView) findViewById(R.id.none);
        none.setText("Task File List");
        none.setVisibility(View.VISIBLE);
        hourly = (TextView) findViewById(R.id.hourly);
        hourly.setText("Other File List");
        weekly = (TextView) findViewById(R.id.weekly);
        weekly.setText("Remainder");
        weekly.setVisibility(View.GONE);
        monthly = (TextView) findViewById(R.id.monthly);
        monthly.setText("Text");
        monthly.setVisibility(View.GONE);
        yearly = (TextView) findViewById(R.id.yearly);
        yearly.setText("Remove All Tags");
        yearly.setVisibility(View.GONE);
        cancel = (TextView) findViewById(R.id.ca);
        cancel_layout = (LinearLayout) findViewById(R.id.l2);

        /*Window window = getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        param.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        window.setAttributes(param);*/


        everyminute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), FileListAdapter.class);
                intent.putExtra("list", "Chat File List");
                startActivity(intent);
                finish();
            }
        });
        none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), FileListAdapter.class);
                intent.putExtra("list", "Task File List");
                startActivity(intent);
                finish();
            }
        });
        hourly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), FileListAdapter.class);
                intent.putExtra("list", "Other File List");
                startActivity(intent);
                finish();
            }
        });
        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), FileListAdapter.class);
                intent.putExtra("list", "All File List");
                startActivity(intent);
                finish();
            }
        });
        /*weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("tagFilter","Remainder");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("tagFilter","Text");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        yearly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.putExtra("tagFilter","RemoveAll");
                setResult(RESULT_OK,intent);
                finish();
            }
        });*/
        cancel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
