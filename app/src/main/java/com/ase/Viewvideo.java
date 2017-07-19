package com.ase;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.DB.VideoCallDataBase;

/**
 * Created by Ramdhas on 17-10-2016.
 */
public class Viewvideo extends Activity {
    WebView webView;
    TextView textView;
    Handler handler;
    private SwipeRefreshLayout swipe;
    String nameofvideo;
    ImageView image;
    String username=null;
    ProgressBar pbar;
    LinearLayout progresslinear;
    ContactBean contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewvideo);

        webView=(WebView)findViewById(R.id.webview);
        textView=(TextView)findViewById(R.id.videotext);
        image=(ImageView)findViewById(R.id.image);
        pbar = (ProgressBar) findViewById(R.id.progress);
        progresslinear = (LinearLayout)findViewById(R.id.progressll);
        swipe=(SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        if(getIntent()!=null)
        nameofvideo=getIntent().getStringExtra("filename");
        switch (nameofvideo)
        {
            case "Performance":
                username=getIntent().getStringExtra("username");
                Log.i("Username","Performance task"+username);
                contact=VideoCallDataBase.getDB(getApplicationContext()).getContactObject(username);
                loadPerformance();
                break;
            case "Current Task":
                username=getIntent().getStringExtra("username");
                Log.i("Username","Current task"+username);
                contact=VideoCallDataBase.getDB(getApplicationContext()).getContactObject(username);
                loadCurrentTask();
                break;
            default:
                load();
                break;
        }
        textView.setText(nameofvideo);

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.setFocusable(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        handler=new Handler();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isNetworkAvailable()){
                        webView.clearView();
                    switch (nameofvideo)
                    {
                        case "Performance":
                            webView.loadUrl(getApplicationContext().getString(R.string.app_url) +"individualPerformancePieChart?userId=" + contact.getContact_id() );
                            webView.setWebChromeClient(new WebChromeClient(){
                                public void onProgressChanged(WebView view, int progress){
                                    pbar.setProgress(progress);
                                    pbar.setSecondaryProgress(progress+10);
                                }
                            });

                            break;
                        case "Current Task":
                            webView.loadUrl("http://122.165.92.171:8080/uploads/highmessaging/chart/201610041939168533679.jpg" );
                            webView.setWebChromeClient(new WebChromeClient(){
                                public void onProgressChanged(WebView view, int progress){
                                    pbar.setProgress(progress);
                                    pbar.setSecondaryProgress(progress+10);
                                }
                            });
                            break;
                        default:
                            webView.loadUrl(getResources().getString(R.string.user_upload)+nameofvideo );
                            webView.setWebChromeClient(new WebChromeClient(){
                                public void onProgressChanged(WebView view, int progress){
                                    pbar.setProgress(progress);
                                    pbar.setSecondaryProgress(progress+10);
                                }
                            });

                            break;
                    }
                    } else
                    Toast.makeText(getApplicationContext(),"No Network",Toast.LENGTH_LONG).show();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            swipe.setRefreshing(false);

                        }
                    }, 2000);}

            });
    }

    public void load()
    {
        if(isNetworkAvailable()) {


            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new SSLTolerentWebViewClient());
            webView.loadUrl(getResources().getString(R.string.user_upload)+nameofvideo );
            webView.setWebChromeClient(new WebChromeClient(){
                public void onProgressChanged(WebView view, int progress){
                    pbar.setProgress(progress);
                    pbar.setSecondaryProgress(progress+10);
                }
            });

        }else
            Toast.makeText(getApplicationContext(),"No Network",Toast.LENGTH_LONG).show();
    }
    public void loadPerformance()
    {
        if(isNetworkAvailable()) {


            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new SSLTolerentWebViewClient());
            webView.loadUrl(getApplicationContext().getString(R.string.app_url) +"individualPerformancePieChart?userId=" + contact.getContact_id());
            webView.setWebChromeClient(new WebChromeClient(){
                public void onProgressChanged(WebView view, int progress){
                    pbar.setProgress(progress);
                    pbar.setSecondaryProgress(progress+10);
                }
            });

        }else
            Toast.makeText(getApplicationContext(),"No Network",Toast.LENGTH_LONG).show();
    }
    public void loadCurrentTask()
    {
        if(isNetworkAvailable()) {


            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new SSLTolerentWebViewClient());
            webView.loadUrl("https://122.165.92.171:8443/highMessage/individualGanttChart?userId=1");
            webView.setWebChromeClient(new WebChromeClient(){
                public void onProgressChanged(WebView view, int progress){
                    pbar.setProgress(progress);
                    pbar.setSecondaryProgress(progress+10);
                }
            });

        }else
            Toast.makeText(getApplicationContext(),"No Network",Toast.LENGTH_LONG).show();
    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class SSLTolerentWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            progress.setMessage("Loading...");
//            progress.setIndeterminate(false);
//            progress.show();
            progresslinear.setVisibility(View.VISIBLE);

        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            progress.cancel();
//            view.setInitialScale((int)(100*view.getScale()));
            progresslinear.setVisibility(View.GONE);
        }

        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {

           /* AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            AlertDialog alertDialog = builder.create();*/
            /*String message = "SSL Certificate error.";
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
            }*/
            handler.proceed();


        }
    }
}
