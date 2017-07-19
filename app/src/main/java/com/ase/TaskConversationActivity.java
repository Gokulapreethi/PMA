package com.ase;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by saravanakumar on 6/25/2016.
 */
public class TaskConversationActivity extends Activity {

    Button button1,button2,button3;
    private AlarmManagerBroadcastReceiver alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_conversation_activity);

        button1 = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button1);
        button3 = (Button) findViewById(R.id.button2);

        alarm = new AlarmManagerBroadcastReceiver();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Context context = getApplicationContext();
                    if(alarm != null){
                        alarm.SetAlarm(context);
                    }else{
                        Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
                    }
                }


        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = getApplicationContext();
                if(alarm != null){
                    alarm.CancelAlarm(context);
                }else{
                    Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
                }


            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = getApplicationContext();
                if(alarm != null){
                    alarm.setOnetimeTimer(context);
                }else{
                    Toast.makeText(context, "Alarm is null", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
