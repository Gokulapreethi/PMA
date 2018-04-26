package com.ase;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.Log;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by thirumal on 22-09-2017.
 */
public class FormsView extends FragmentActivity {
    private TextView back, per;
    private WebView performance;
    private SwipeRefreshLayout swipe;
    Handler handler;
    String tempid;
    ProgressDialog progress;
    ProgressBar pBar;
    static Context context;
    String role, viewForms, formId, task_Name;
    private ValueCallback<Uri[]> mUploadMessage;
    private Uri[] results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formsview);

        context = this;
        Appreference.context_table.put("formsView", this);
        back = (TextView) findViewById(R.id.back);
        per = (TextView) findViewById(R.id.per);
        performance = (WebView) findViewById(R.id.webview);
        pBar = (ProgressBar) findViewById(R.id.progressbar);
        pBar.setVisibility(View.GONE);
        progress = new ProgressDialog(this);


        performance.getSettings().setDisplayZoomControls(true);
        performance.getSettings().setBuiltInZoomControls(true);
        performance.getSettings().setSupportZoom(true);
        performance.setFocusable(true);
        performance.getSettings().setLoadWithOverviewMode(true);
        performance.getSettings().setUseWideViewPort(true);
        performance.getSettings().setPluginState(WebSettings.PluginState.ON);
        performance.setBackgroundColor(Color.TRANSPARENT);
        performance.setScrollBarStyle(performance.SCROLLBARS_OUTSIDE_INSET);
        performance.getSettings().setLoadsImagesAutomatically(true);
        performance.getSettings().setJavaScriptEnabled(true);
        performance.getSettings().setDomStorageEnabled(true);
        performance.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        WebSettings ws = performance.getSettings();
        ws.setAllowFileAccess(true);
        ws.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            try {
                Log.d("ghf", "Enabling HTML5-Features");
                Method m1 = WebSettings.class.getMethod("setDomStorageEnabled", new Class[]{Boolean.TYPE});
                m1.invoke(ws, Boolean.TRUE);

                Method m2 = WebSettings.class.getMethod("setDatabaseEnabled", new Class[]{Boolean.TYPE});
                m2.invoke(ws, Boolean.TRUE);

                Method m3 = WebSettings.class.getMethod("setDatabasePath", new Class[]{String.class});
                m3.invoke(ws, "/data/data/" + getPackageName() + "/databases/");

                Method m4 = WebSettings.class.getMethod("setAppCacheMaxSize", new Class[]{Long.TYPE});
                m4.invoke(ws, 1024 * 1024 * 8);

                Method m5 = WebSettings.class.getMethod("setAppCachePath", new Class[]{String.class});
                m5.invoke(ws, "/data/data/" + getPackageName() + "/cache/");

                Method m6 = WebSettings.class.getMethod("setAppCacheEnabled", new Class[]{Boolean.TYPE});
                m6.invoke(ws, Boolean.TRUE);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            performance.getSettings().setDatabasePath("/data/data/" + performance.getContext().getPackageName() + "/databases/");
        }

        if (getIntent() != null) {
            tempid = getIntent().getStringExtra("taskid");
            role = getIntent().getStringExtra("role");
            viewForms = getIntent().getStringExtra("ConversationBubble");
            formId = getIntent().getStringExtra("formId");
            task_Name = getIntent().getStringExtra("taskName");
        }


        swipe = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        handler = new Handler();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isNetworkAvailable()) {

                    performance.getSettings().setJavaScriptEnabled(true);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        performance.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                    }
                    performance.setWebChromeClient(new WebChromeClient() {
                        @Override
                        public void onProgressChanged(WebView view, int newProgress) {
                            super.onProgressChanged(view, newProgress);
                            pBar.setProgress(newProgress);
                            pBar.setSecondaryProgress(newProgress + 20);
                            if (newProgress == 100) {
                                pBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                            // Double check that we don't have any existing callbacks
                            if (mUploadMessage != null) {
                                mUploadMessage.onReceiveValue(null);
                            }
                            mUploadMessage = filePathCallback;
                            Log.i("Url called", "Performence filePathCallback " + filePathCallback);
                            return true;
                        }
                    });
                    performance.setWebViewClient(new SSLTolerentWebViewClient());
                    String url = "";



                    String PMS_Name = getIntent().getStringExtra("desc");
                    performance.clearCache(true);
                    url = "http://server1.snowwood.com:3000/checkList;jobcard=PMS_TANA_250 HOURS SERVICE";
                    performance.loadUrl(url);
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable()) {
                    performance.clearView();
//                    performance.loadUrl(getApplicationContext().getString(R.string.app_url) + "durationTemplateChart?tempId=" + tempid);
                    if (viewForms != null && viewForms.equalsIgnoreCase("yes")) {
                        performance.reload();
                    } else {
                        performance.reload();
                    }
//                    performance.loadUrl("http://122.165.92.171:3000/myform-client;user_Id=1;task_id=208");
//                    performance.loadUrl("http://122.165.92.171:8080/highMessage/welcome");
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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        performance.onPause();
        performance.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        performance.resumeTimers();
        performance.onResume();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("webView", "currentm shouldOverrideUrlLoading is " + url);
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            pBar.setVisibility(View.VISIBLE);
            Log.i("webView", "currentm url is " + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            pBar.setVisibility(View.GONE);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            Log.i("webView", "currentm onLoadResource is " + url);
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
            Log.i("webView", "currentm onPageCommitVisible is " + url);
            super.onPageCommitVisible(view, url);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            Log.i("webView", "currentm shouldInterceptRequest is " + request);
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            Log.i("webView", "currentm onReceivedError is " + error);
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            Log.i("webView", "currentm onReceivedHttpError is " + errorResponse);
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            Log.i("webView", "currentm onFormResubmission is " + resend);
            super.onFormResubmission(view, dontResend, resend);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            Log.i("webView", "currentm doUpdateVisitedHistory is " + url);
            super.doUpdateVisitedHistory(view, url, isReload);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            Log.i("webView", "currentm onReceivedSslError is " + error);
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
            Log.i("webView", "currentm onReceivedClientCertRequest is " + request);
            super.onReceivedClientCertRequest(view, request);
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            Log.i("webView", "currentm onReceivedHttpAuthRequest is " + realm);
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            Log.i("webView", "currentm shouldOverrideKeyEvent is " + event);
            return super.shouldOverrideKeyEvent(view, event);
        }

        @Override
        public void onUnhandledInputEvent(WebView view, InputEvent event) {
            Log.i("webView", "currentm onUnhandledInputEvent is " + event);
            super.onUnhandledInputEvent(view, event);
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            Log.i("webView", "currentm onScaleChanged is " + newScale);
            super.onScaleChanged(view, oldScale, newScale);
        }

        @Override
        public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
            Log.i("webView", "currentm onReceivedLoginRequest is " + realm);
            super.onReceivedLoginRequest(view, realm, account, args);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.i("webView", "currentm onReceivedError is " + errorCode);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @Override
        public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
            Log.i("webView", "currentm onTooManyRedirects is " + cancelMsg);
            super.onTooManyRedirects(view, cancelMsg, continueMsg);
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            Log.i("webView", "currentm shouldInterceptRequest is " + url);
            return super.shouldInterceptRequest(view, url);
        }

//        @Override
//        public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
////            InputConnection ic = super.onCreateInputConnection(outAttrs);
//            outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;
//            return ic;
//        }
    }

    private class SSLTolerentWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("webView", "currentm override url is " + url);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//            progress.setMessage("Loading...");
//            progress.setIndeterminate(false);
//            progress.show();
            Log.i("webView", "currentm ssl url is " + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            progress.cancel();
//            view.setInitialScale((int)(100*view.getScale()));
        }

        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            String message;
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
                default:
                    message = "SSL Certificate error.";
                    break;
            }
            builder.setMessage(message + ", Would you like to continue...");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Appreference.context_table.remove("formsView");
    }

    @Override
    public void onBackPressed() {
        if (performance.canGoBack()) {
            performance.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
