package com.ase;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Preethi on 10/26/2017.
 */

public class ShowEstimTimeupAlert extends Activity {
    Context context;
    private static Handler handler;
    TextView tv_relevant_id,tv_relevant_task_id;
    String OracleprojectId;
    String OracletaskId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showestimtimeup);
        Appreference.context_table.put("showalertestimtimeup", this);
        context = this;
        handler = new Handler();
        OracleprojectId=getIntent().getStringExtra("OracleprojectId");
        OracletaskId=getIntent().getStringExtra("OracletaskId");
        tv_relevant_id = (TextView) findViewById(R.id.project_id_alert);
        tv_relevant_task_id = (TextView) findViewById(R.id.taskid_alert);
        TextView tv_cancel_alert = (TextView) findViewById(R.id.dismiss_estim_alert);
        TextView tv_alert_Time= (TextView) findViewById(R.id.timeout_currentTime);
        tv_relevant_id.setText("Job Card No : " + OracleprojectId);
        tv_relevant_task_id.setText("Activity Code : " + OracletaskId);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm");
        String formattedDate = df.format(c.getTime());
        tv_alert_Time.setText(formattedDate);
        tv_cancel_alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
