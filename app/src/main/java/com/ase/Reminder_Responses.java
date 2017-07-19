package com.ase;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by thirumal on 10-02-2017.
 */
public class Reminder_Responses extends FragmentActivity {
    private TextView back, reminderhead;
    private WebView ReminderResponse;
    private SwipeRefreshLayout ReminderSwipe;
    Handler handler;
    String taskId, taskType;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_responses);
        reminderhead = (TextView) findViewById(R.id.reminderhead);
        back = (TextView) findViewById(R.id.back);
        ReminderResponse = (WebView) findViewById(R.id.webview);
        progress = new ProgressDialog(this);
//        performance.getSettings().setDisplayZoomControls(false);
//        performance.setInitialScale(25);
        ReminderResponse.getSettings().setBuiltInZoomControls(true);
        ReminderResponse.getSettings().setSupportZoom(true);
        ReminderResponse.setFocusable(true);
        ReminderResponse.getSettings().setLoadWithOverviewMode(true);
        ReminderResponse.getSettings().setUseWideViewPort(true);

        if (getIntent() != null) {
            taskId = getIntent().getStringExtra("taskId");
            taskType = getIntent().getStringExtra("taskType");
        }
        Log.i("Performence", "taskId " + taskId);
        Log.i("Performence", "taskType " + taskType);
        Log.i("Performence", "loginuser " + Appreference.loginuserdetails.getId());

        ReminderSwipe = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        handler = new Handler();
        if (isNetworkAvailable()) {

            ReminderResponse.getSettings().setJavaScriptEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ReminderResponse.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            } else {
//                performance.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
            }
            ReminderResponse.setWebViewClient(new SSLTolerentWebViewClient());
            if (taskType != null && !taskType.equalsIgnoreCase("Group")) {
                ReminderResponse.loadUrl(getApplicationContext().getString(R.string.app_url) + "missedReminderChart?taskId=" + taskId);
            } else {
                ReminderResponse.loadUrl(getApplicationContext().getString(R.string.app_url) + "missedReminderGroupChart?taskId=" + taskId);
            }
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

        ReminderSwipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        ReminderSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable()) {
                    ReminderResponse.clearView();
                    if (taskType != null && !taskType.equalsIgnoreCase("Group")) {
                        ReminderResponse.loadUrl(getApplicationContext().getString(R.string.app_url) + "missedReminderChart?taskId=" + taskId);
                    } else {
                        ReminderResponse.loadUrl(getApplicationContext().getString(R.string.app_url) + "missedReminderGroupChart?taskId=" + taskId);
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ReminderSwipe.setRefreshing(false);
                        }
                    }, 2000);
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class SSLTolerentWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progress.setMessage("Loading...");
            progress.setIndeterminate(false);
            progress.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progress.cancel();
//            view.setInitialScale((int)(100*view.getScale()));
        }

        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {

           /* AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            AlertDialog alertDialog = builder.create();*/
            String message = "SSL Certificate error.";
            switch (error.getPrimaryError()) {
                case SslError.SSL_UNTRUSTED:
                    message = "The certificate authority is not trusted.";
                    break;
                case SslError.SSL_EXPIRED:
                    message = "The certificate has expired.";
                    break;
                case SslError.SSL_IDMISMATCH:
                    message = "The certificate Hostname mismatch.";
                    break;
                case SslError.SSL_NOTYETVALID:
                    message = "The certificate is not yet valid.";
                    break;
            }
            handler.proceed();


        }
    }
}
