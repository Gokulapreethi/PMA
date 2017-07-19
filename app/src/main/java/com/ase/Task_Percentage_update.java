package com.ase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class Task_Percentage_update extends Activity {

    WebView webView;
    String url;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task__percentage_update);
        button = (Button)   findViewById(R.id.button);

        webView = (WebView) findViewById(R.id.task_webview);
        url = getIntent().getExtras().getString("url");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webView.loadUrl(url);

        webView.setWebViewClient(new myWebViewClient());

    }

    public class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("url", "url--->" + url);

            if (url.contains("completedPercentage") && url.contains("getTaskDetail")) {

                Intent intent = new Intent(Task_Percentage_update.this,UpdateTaskActivity.class);
                intent.putExtra("url",url);
                startActivityForResult(intent,210);
                return true;
            } else {

            }

            return  false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 210)
        {
            Log.i("url", "url--->" + url);
            webView.loadUrl(url);
        }
    }
}