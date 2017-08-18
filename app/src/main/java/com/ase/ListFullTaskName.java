package com.ase;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;

/**
 * Created by Preethi on 8/18/2017.
 */

public class ListFullTaskName extends Activity{
     Context ctaskContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_task_description);
        String my_projectId=getIntent().getStringExtra("t_projectId");
        String my_taskId=getIntent().getStringExtra("t_taskId");
        WebView wv_my_taskName=(WebView)findViewById(R.id.my_taskName);
        TextView tx_task_back=(TextView)findViewById(R.id.task_back);
        String Query = "Select * from projectHistory where projectId ='" + my_projectId + "' and taskId = '" + my_taskId + "'";
        TaskDetailsBean taskBean = VideoCallDataBase.getDB(ctaskContext).getDetails_to_complete_project(Query);
        String youtContentStr = String.valueOf(Html
                .fromHtml("<![CDATA[<body style=\"text-align:justify;color:#222222; \">"
                        +taskBean.getTaskName()
                        + "</body>]]>"));

        wv_my_taskName.loadData(youtContentStr, "text/html", "utf-8");
        tx_task_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
