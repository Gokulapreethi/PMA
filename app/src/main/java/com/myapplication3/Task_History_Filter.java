package com.myapplication3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by vignesh on 9/12/2016.
 */
public class Task_History_Filter extends Activity {

    TextView inpro, assigned,rejected, completed,onhold, closed, abandonod, draft, alltask, issue, both, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_histroy_fillter);

        inpro = (TextView) findViewById(R.id.inpro);
        assigned = (TextView) findViewById(R.id.assign);
//        rejected = (TextView) findViewById(R.id.reject);
//        completed = (TextView) findViewById(R.id.complete);
        onhold=(TextView)findViewById(R.id.onhold);
        closed = (TextView) findViewById(R.id.closed);
        abandonod = (TextView) findViewById(R.id.abandonod);
        draft = (TextView) findViewById(R.id.draft);
        alltask = (TextView) findViewById(R.id.alltask);
        cancel = (TextView) findViewById(R.id.ca);
        issue = (TextView) findViewById(R.id.issue);
        both = (TextView) findViewById(R.id.both_side);

        String taskType = getIntent().getExtras().getString("taskType");
        if (taskType != null && taskType.equalsIgnoreCase("Group")) {
            assigned.setVisibility(View.GONE);
//            rejected.setVisibility(View.GONE);
            draft.setVisibility(View.GONE);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        assigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("Query", "assigned");
                setResult(RESULT_OK, i);
                finish();
            }
        });

        /*rejected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("Query", "Rejected");
                setResult(RESULT_OK, i);
                finish();
            }
        });*/
        inpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("Query", "inprogress");
                setResult(RESULT_OK, i);
                finish();
            }
        });

        /*completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("Query", "Completed");
                setResult(RESULT_OK, i);
                finish();
            }
        });*/

        onhold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("Query", "onhold");
                setResult(RESULT_OK, i);
                finish();
            }
        });

        closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("Query", "closed");
                setResult(RESULT_OK, i);
                finish();
            }
        });

        abandonod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("Query", "abandoned");
                setResult(RESULT_OK, i);
                finish();
            }
        });

        draft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("Query", "Template");
                setResult(RESULT_OK, i);
                finish();
            }
        });

        alltask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("Query", "alltask");
                setResult(RESULT_OK, i);
                finish();
            }
        });

        issue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("Query", "issue");
                setResult(RESULT_OK, i);
                finish();
            }
        });

        both.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("Query", "Task&Issue");
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}
