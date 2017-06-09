package com.myapplication3;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * Created by Preethi on 26-05-2017.
 */
public class ReportView extends Activity {
    TextView tv_back;
    Button btn_share,btn_view;
    boolean isForReportAll=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_view);
        Log.i("report","----Oncreate---");
        isForReportAll = getIntent().getBooleanExtra("fromContacts",false);
        tv_back=(TextView) findViewById(R.id.back);
        btn_share=(Button)findViewById(R.id.btn_share);
        btn_view=(Button)findViewById(R.id.btn_view);
        if(isForReportAll){
            btn_view.setText("View Excel");
            btn_share.setText("Share Excel File");
        }else{
            btn_view.setText("View Pdf");
            btn_share.setText("Share Pdf File");
        }
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File filelocation;
                if (isForReportAll)
                    filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/ShareAllTaskFiles.xls");
                else
                    filelocation = new File(Environment.getExternalStorageDirectory()
                            + "/High Message/Share.pdf");
                if(filelocation.exists()) {
                    String[] TO = {""};
                    String[] CC = {""};
                    Uri path = Uri.fromFile(filelocation);
                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    final PackageManager pm = getPackageManager();
                    final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
                    ResolveInfo best = null;
                    for (final ResolveInfo info : matches)
                        if (info.activityInfo.packageName.endsWith(".gm") ||
                                info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
                    if (best != null)
                        intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                    intent.putExtra(Intent.EXTRA_SUBJECT, "HM : Custom Report");
                    intent .putExtra(Intent.EXTRA_STREAM, path);
                    try {
                        startActivity(intent);
                    }catch (android.content.ActivityNotFoundException ex){
                        Toast.makeText(ReportView.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(isForReportAll){
                        Toast.makeText(ReportView.this, "No Excel Documentation", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(ReportView.this, "No Pdf Documentation", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file;
                if (isForReportAll)
                    file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/ShareAllTaskFiles.xls");
                else
                    file = new File(Environment.getExternalStorageDirectory()
                            + "/High Message/Share.pdf");
                if(file.exists()) {
                    Uri path = Uri.fromFile(file);
                    Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                    pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    if (isForReportAll) {
                        pdfOpenintent.setDataAndType(path, "application/vnd.ms-excel");
                    }
                    else {
                        pdfOpenintent.setDataAndType(path, "application/pdf");
                    }
                    try {
                        startActivity(pdfOpenintent);
                    } catch (ActivityNotFoundException e) {
                        if (isForReportAll){
                            Toast.makeText(ReportView.this, "Please Install MS-Excel app to view the file", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(ReportView.this, "Please Install Pdf app to view the file", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else{
                    Toast.makeText(ReportView.this, "No Pdf Documentation", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}