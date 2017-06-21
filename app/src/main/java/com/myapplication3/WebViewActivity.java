package com.myapplication3;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Preethi on 20-06-2017.
 */
public class WebViewActivity extends Activity {

    private WebView webView;
    String report_url;
    Activity activity; // instead of context we can use activity
    private ProgressDialog progDailog;  // loader
    String GoogleDocs="http://docs.google.com/gview?embedded=true&url=";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_webview);
        report_url = getIntent().getExtras().getString("ReportFileName");
        String pdfURL = getResources().getString(R.string.task_reminder) + report_url;
        activity = this;
        progDailog = ProgressDialog.show(activity, "Loading", "Please wait...", true);
        progDailog.setCancelable(false);
        webView = (WebView) findViewById(R.id.webview_compontent);
        Log.i("webview123", "PDFURL=================>" + pdfURL);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progDailog.show();
                view.loadUrl(url);

                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                progDailog.dismiss();
            }
        });
        String url=GoogleDocs + pdfURL;
        Log.i("webview123", "PDFURL123=================>" + url);

        webView.loadUrl(pdfURL);
    }
}
