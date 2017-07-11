package com.myapplication3;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by Preethi on 20-06-2017.
 */
public class WebViewActivity extends Activity {

    private WebView mWebview;
    String report_url;
    Activity activity; // instead of context we can use activity
    private ProgressDialog progDailog;  // loader
    String GoogleDocs="http://docs.google.com/gview?embedded=true&url=";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_webview);
//        mWebview  = new WebView(this);
        report_url = getIntent().getExtras().getString("ReportFileName");
        String pdfURL = getResources().getString(R.string.task_reminder) + report_url;
        activity = this;
        /*
        progDailog = ProgressDialog.show(activity, "Loading", "Please wait...", true);
        progDailog.setCancelable(false);
        mWebview = (WebView) findViewById(R.id.webview_compontent);
        Log.i("webview123", "PDFURL=================>" + pdfURL);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.getSettings().setLoadWithOverviewMode(true);
        mWebview.getSettings().setUseWideViewPort(true);*/
        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript

        final Activity activity = this;

//        mWebview.setWebViewClient(new WebViewClient() {
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
//            }
//        });

//        mWebview .loadUrl(pdfURL);
//        setContentView(mWebview );


        mWebview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimeType, long contentLength) {
                Log.i("report123","Url onDownloadStart webviewActivity=====>"+url);

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                request.setMimeType(mimeType);
                //------------------------COOKIE!!------------------------
                String cookies = CookieManager.getInstance().getCookie(url);
                request.addRequestHeader("cookie", cookies);
                //------------------------COOKIE!!------------------------
                request.addRequestHeader("User-Agent", userAgent);
                request.setDescription("Downloading file2222...");
                request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, URLUtil.guessFileName(url, contentDisposition, mimeType));
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
                Toast.makeText(getApplicationContext(), "Downloading File2222....", Toast.LENGTH_LONG).show();
            }
        });
        String url=GoogleDocs + pdfURL;
        Log.i("webview123", "PDFURL123=================>" + url);

//        mWebview.loadUrl(pdfURL);
    }
}
