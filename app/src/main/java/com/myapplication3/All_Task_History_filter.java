package com.myapplication3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by thirumal on 07-02-2017.
 */
public class All_Task_History_filter extends Activity {

    TextView inpro, assigned,rejected, completed,onhold, closed, abandonod, draft, alltask, issue, both, cancel,notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_task_histroy_filter);

        try {
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
            notes=(TextView)findViewById(R.id.notes);

//        String taskType = getIntent().getExtras().getString("taskType");
//        if (taskType != null && taskType.equalsIgnoreCase("Group")) {
//            assigned.setVisibility(View.GONE);
//            rejected.setVisibility(View.GONE);
            draft.setVisibility(View.GONE);
//        }

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            assigned.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent();
                        i.putExtra("Query", "assigned");
                        setResult(RESULT_OK, i);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("All_Task_History_filter assigned ","Exception "+e.getMessage(),"WARN",null);
                    }
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
                    try {
                        Intent i = new Intent();
                        i.putExtra("Query", "inprogress");
                        setResult(RESULT_OK, i);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("All_Task_History_filter inpro ","Exception "+e.getMessage(),"WARN",null);
                    }
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
                    try {
                        Intent i = new Intent();
                        i.putExtra("Query", "onhold");
                        setResult(RESULT_OK, i);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("All_Task_History_filter onhold ","Exception "+e.getMessage(),"WARN",null);
                    }
                }
            });

            closed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent();
                        i.putExtra("Query", "closed");
                        setResult(RESULT_OK, i);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("All_Task_History_filter closed ","Exception "+e.getMessage(),"WARN",null);
                    }
                }
            });

            abandonod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent();
                        i.putExtra("Query", "abandoned");
                        setResult(RESULT_OK, i);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("All_Task_History_filter abandoned ","Exception "+e.getMessage(),"WARN",null);
                    }
                }
            });

            draft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent();
                        i.putExtra("Query", "draft");
                        setResult(RESULT_OK, i);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("All_Task_History_filter draft ","Exception "+e.getMessage(),"WARN",null);
                    }
                }
            });

            alltask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent();
                        i.putExtra("Query", "alltask");
                        setResult(RESULT_OK, i);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("All_Task_History_filter alltask ","Exception "+e.getMessage(),"WARN",null);
                    }
                }
            });

            issue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent();
                        i.putExtra("Query", "issue");
                        setResult(RESULT_OK, i);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("All_Task_History_filter issues ","Exception "+e.getMessage(),"WARN",null);
                    }
                }
            });

            both.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i = new Intent();
                        i.putExtra("Query", "Task&Issue");
                        setResult(RESULT_OK, i);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("All_Task_History_filter both ","Exception "+e.getMessage(),"WARN",null);
                    }
                }
            });
            notes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent i=new Intent();
                        i.putExtra("Query","note");
                        setResult(RESULT_OK, i);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("All_Task_History_filter notes  ","Exception "+e.getMessage(),"WARN",null);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("All_Task_History_filter ","Exception "+e.getMessage(),"WARN",null);
        }
    }
}
