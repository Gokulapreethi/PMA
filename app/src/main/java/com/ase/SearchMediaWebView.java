package com.ase;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;


public class SearchMediaWebView extends AppCompatActivity {
    TextView title_header;
    Context context;
    private ProgressDialog progress;
    WebView browser;
    ProgressBar pbar;
    LinearLayout back_linear;
    RelativeLayout relate_upper;
    Handler handler=new Handler();
    long loadstart, loadend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            Appreference.context_table.put("searchmedia", this);
            setContentView(R.layout.searchmedia_webview);
            context = this;
            title_header=(TextView)findViewById(R.id.tv_heading);
            relate_upper = (RelativeLayout) findViewById(R.id.upper_progress);
            browser = (WebView) findViewById(R.id.assets_webview);
            pbar = (ProgressBar) findViewById(R.id.progress);
            back_linear = (LinearLayout) findViewById(R.id.linear_back);
            progress = new ProgressDialog(SearchMediaWebView.this);

            browser.getSettings().setLoadsImagesAutomatically(true);
            browser.getSettings().setJavaScriptEnabled(true);
            browser.getSettings().setBuiltInZoomControls(true);
            browser.setInitialScale(25);
            browser.setWebViewClient(new SSLTolerentWebViewClient());
            browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            browser.getSettings().setDisplayZoomControls(false);
            browser.getSettings().setSupportZoom(true);
            browser.setFocusable(true);
            browser.getSettings().setLoadWithOverviewMode(true);
            browser.getSettings().setUseWideViewPort(true);
            browser.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress) {

                    pbar.setProgress(progress);
                    pbar.setSecondaryProgress(progress + 10);
//                tv_loading.setText(progress+" %");
                }
            });
            String url=null;
            if(getIntent()!=null && getIntent().getStringExtra("urlload")!=null &&
                    getIntent().getStringExtra("urlload").equalsIgnoreCase("searchmedia")) {
                url = getResources().getString(R.string.app_url)+"mediaView";
            }else if(getIntent()!=null && getIntent().getStringExtra("urlload")!=null &&
                    getIntent().getStringExtra("urlload").equalsIgnoreCase("tnareport")){
                url =  getResources().getString(R.string.app_url)+"tnaReport";
                title_header.setText("TNA Report");
            }
            Log.i("webview", "URL-->" + url);
            try {
                browser.loadUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Error e) {
                e.printStackTrace();
            }
            back_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }catch (Error e2){
            e2.printStackTrace();
        }
    }
    private class SSLTolerentWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        try{
            relate_upper.setVisibility(View.VISIBLE);
            /*progress.setCancelable(false);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setMessage("Loading...");
            progress.setIndeterminate(false);
            progress.show();*/
            loadstart = (new Date()).getTime();
            Log.i("webview","load start time-->"+loadstart);
        }catch(Exception e){
            e.printStackTrace();
        }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            progress.cancel();
            relate_upper.setVisibility(View.GONE);
            loadend = (new Date()).getTime();
            Log.i("webview","load end time-->"+loadend);
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

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("redirect","url--->"+url);
            return super.shouldOverrideUrlLoading(view, url);
        }

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Appreference.context_table.remove("searchmedia");
    }



    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.i("onPostResume","Assetwebview Activity Onpostresume method");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
