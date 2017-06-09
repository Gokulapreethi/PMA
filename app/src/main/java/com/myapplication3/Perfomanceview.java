package com.myapplication3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Perfomanceview extends FragmentActivity {

    private Button individual, group;
    private TextView back;
    private WebView performance;
    Boolean button = true;
    private SwipeRefreshLayout swipe;
    Handler handler;
    String isProject, user_pjt_id;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfomance_view);
        individual = (Button) findViewById(R.id.individual);
        group = (Button) findViewById(R.id.group);
        back = (TextView) findViewById(R.id.back);
        performance = (WebView) findViewById(R.id.webview);
        progress=new ProgressDialog(this);
        performance.getSettings().setDisplayZoomControls(false);
        performance.setInitialScale(25);
        performance.getSettings().setBuiltInZoomControls(true);
        performance.getSettings().setSupportZoom(true);
        performance.setFocusable(true);
        performance.getSettings().setLoadWithOverviewMode(true);
        performance.getSettings().setUseWideViewPort(true);

        if (getIntent() != null) {
            isProject = getIntent().getStringExtra("is_project");
            user_pjt_id = getIntent().getStringExtra("User_Project_Id");
        }
        Log.i("Performence", "isProject " + isProject);
        Log.i("Performence", "user_pjt_id " + user_pjt_id);
        Log.i("Performence", "loginuser " + Appreference.loginuserdetails.getId());

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        handler = new Handler();
        if (isNetworkAvailable()) {
            individual.setBackgroundColor(Color.parseColor("#4682B4"));
            group.setBackgroundColor(Color.parseColor("#949191"));
            group.setTextColor(Color.parseColor("#FFFFFF"));
            individual.setTextColor(Color.parseColor("#ffffff"));

            performance.getSettings().setJavaScriptEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                performance.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            } else {
//                performance.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
            }
            performance.setWebViewClient(new SSLTolerentWebViewClient());
            if (isProject != null && isProject.equalsIgnoreCase("N")) {
                performance.loadUrl(getApplicationContext().getString(R.string.app_url) + "individualPerformancePieChart?userId=" + Appreference.loginuserdetails.getId());
            } else {
                group.setText("Dashboard");
                individual.setVisibility(View.GONE);
                performance.loadUrl(getApplicationContext().getString(R.string.app_url) + "getDashBoard?projectId=" + user_pjt_id);
            }

        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

        swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable()) {
                    if (button) {
                        performance.clearView();
                        if (isProject != null && isProject.equalsIgnoreCase("N")) {
                            performance.loadUrl(getApplicationContext().getString(R.string.app_url) + "individualPerformancePieChart?userId=" + Appreference.loginuserdetails.getId());
                        } else {
                            performance.loadUrl(getApplicationContext().getString(R.string.app_url) + "getDashBoard?projectId=" + user_pjt_id);
                        }
                    } else {
                        performance.clearView();
                        if (isProject != null && isProject.equalsIgnoreCase("N")) {
                            performance.loadUrl(getApplicationContext().getString(R.string.app_url) + "organizationalPerformancePieChart");
                        } else {
                            performance.loadUrl(getApplicationContext().getString(R.string.app_url) + "organizationalProjectPerformancePieChart");
                        }
                    }
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            swipe.setRefreshing(false);

                        }
                    }, 2000);
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        individual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    individual.setBackgroundColor(Color.parseColor("#4682B4"));
                    group.setBackgroundColor(Color.parseColor("#949191"));
                    group.setTextColor(Color.parseColor("#FFFFFF"));
                    individual.setTextColor(Color.parseColor("#ffffff"));
                    button = true;
                    performance.clearView();
                    if (isProject != null && isProject.equalsIgnoreCase("N")) {
                        performance.loadUrl(getApplicationContext().getString(R.string.app_url) + "individualPerformancePieChart?userId=" + Appreference.loginuserdetails.getId());
                    } else {
                        performance.loadUrl(getApplicationContext().getString(R.string.app_url) + "getDashBoard?projectId=" + user_pjt_id);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    group.setBackgroundColor(Color.parseColor("#4682B4"));
                    group.setTextColor(Color.parseColor("#ffffff"));
                    individual.setBackgroundColor(Color.parseColor("#949191"));
                    individual.setTextColor(Color.parseColor("#FFFFFF"));
                    performance.clearView();
                    if (isProject != null && isProject.equalsIgnoreCase("N")) {
                        performance.loadUrl(getApplicationContext().getString(R.string.app_url) + "organizationalPerformancePieChart");
                    } else {
                        performance.loadUrl(getApplicationContext().getString(R.string.app_url) + "getDashBoard?projectId=" + user_pjt_id);
                    }
                    button = false;
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
//        refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(button) {
////                    performance.loadUrl();
//                }
//                else {
//
////                performance.loadUrl();
//                }
//            }
//        });
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
