package com.ase.Forms;

import android.app.ProgressDialog;
import android.content.Intent;
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

import com.ase.R;

import org.json.JSONObject;

import json.CommunicationBean;
import json.WebServiceInterface;


/**
 * Created by saravanakumar on 12/30/2016.
 */
public class AddFormsInWebActivity extends FragmentActivity implements WebServiceInterface {

    //    private Button individual, group;
    private TextView back;
    private WebView performance;
    Boolean button = true,isformview=false;
    private SwipeRefreshLayout swipe;
    Handler handler;
    String taskId;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_form_in_web);

//        individual = (Button) findViewById(R.id.individual);
//        group = (Button) findViewById(R.id.group);
        back = (TextView) findViewById(R.id.back);
        performance = (WebView) findViewById(R.id.webview);

        taskId = getIntent().getExtras().getString("taskId");
        Log.i("AddFormsInWebActivity","onCreate 1 "+taskId);
        progress = new ProgressDialog(this);
        performance.getSettings().setBuiltInZoomControls(true);
        performance.getSettings().setSupportZoom(true);
        performance.setFocusable(true);
        performance.getSettings().setLoadWithOverviewMode(true);
        performance.getSettings().setUseWideViewPort(true);


        swipe = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);


        handler = new Handler();
        if (isNetworkAvailable()) {
//            individual.setBackgroundColor(Color.parseColor("#4682B4"));
//            group.setBackgroundColor(Color.parseColor("#949191"));
//            group.setTextColor(Color.parseColor("#FFFFFF"));
//            individual.setTextColor(Color.parseColor("#ffffff"));

            performance.getSettings().setJavaScriptEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                performance.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            } else {
//                performance.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
            }
            performance.setWebViewClient(new SSLTolerentWebViewClient());
            performance.loadUrl(getApplicationContext().getString(R.string.app_url) + "exitingFormsTemplateClient?taskId=" + taskId);

        } else
            Toast.makeText(getApplicationContext(), "No Network", Toast.LENGTH_LONG).show();

        swipe.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable()) {
                   /* if (button) {
                        performance.clearView();
                        performance.loadUrl(getApplicationContext().getString(R.string.app_url) + "individualPerformancePieChart?userId=" + Appreference.loginuserdetails.getId());
                    } else {
                        performance.clearView();
                        performance.loadUrl(getApplicationContext().getString(R.string.app_url) + "organizationalPerformancePieChart");
                    }*/
                    performance.setWebViewClient(new SSLTolerentWebViewClient());
                    performance.loadUrl(getApplicationContext().getString(R.string.app_url) + "exitingFormsTemplateClient?taskId=" + taskId);
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            swipe.setRefreshing(false);

                        }
                    }, 2000);
                } else
                    Toast.makeText(getApplicationContext(), "No Network", Toast.LENGTH_LONG).show();
            }
        });

     /*   individual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    individual.setBackgroundColor(Color.parseColor("#4682B4"));
                    group.setBackgroundColor(Color.parseColor("#949191"));
                    group.setTextColor(Color.parseColor("#FFFFFF"));
                    individual.setTextColor(Color.parseColor("#ffffff"));
                    button = true;
                    performance.clearView();
                    performance.loadUrl(getApplicationContext().getString(R.string.app_url)+"individualPerformancePieChart?userId=" + Appreference.loginuserdetails.getId());
                }else
                    Toast.makeText(getApplicationContext(),"No Network",Toast.LENGTH_LONG).show();
            }
        });
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkAvailable()) {
                    group.setBackgroundColor(Color.parseColor("#4682B4"));
                    group.setTextColor(Color.parseColor("#ffffff"));
                    individual.setBackgroundColor(Color.parseColor("#949191"));
                    individual.setTextColor(Color.parseColor("#FFFFFF"));
                    performance.clearView();
                    performance.loadUrl(getApplicationContext().getString(R.string.app_url)+"organizationalPerformancePieChart");
                    button = false;
                }
                else
                    Toast.makeText(getApplicationContext(),"No Network",Toast.LENGTH_LONG).show();
            }
        });*/
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isformview=true;
//                java.util.List<NameValuePair> tagNameValuePairs = new ArrayList<NameValuePair>();
//                tagNameValuePairs.add(new BasicNameValuePair("taskId", taskId));
//                Appreference.jsonRequestSender.getlistFormsforTask(EnumJsonWebservicename.getAllFormsForTask, tagNameValuePairs,AddFormsInWebActivity.this);
                Intent intent = new Intent(AddFormsInWebActivity.this, FormsListActivity.class);
                intent.putExtra("webformcheck","true");
                Log.i("AddFormsInWebActivity","back.setOnClickListener 1 "+taskId);
                startActivity(intent);
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

    @Override
    public void ResponceMethod(final Object object) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                CommunicationBean communicationBean = (CommunicationBean) object;
                JSONObject jsonObject = null;
                String s1 = communicationBean.getEmail();
                String s2 = communicationBean.getFirstname();
//                if (s1 != null && s1.contains("entryMode")) {
//                    {
//                        try {
//                            ArrayList<FormsListBean> formsListBeen = new ArrayList<FormsListBean>();
//                            JSONArray jsonArray = new JSONArray(s1);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//
//                                Type collectionType = new TypeToken<List<FormsListBean>>() {
//                                }.getType();
//
//                                Log.i("response ", "objects  " + jsonArray.get(i));
//                                Log.i("response ", "objects  " + jsonArray.getString(i));
//                                FormsListBean listUserGroupObject = new Gson().fromJson(jsonArray.getString(i), FormsListBean.class);
////                                listUserGroupObject.setClientId(String.valueOf(i+1));
//                                formsListBeen.add(listUserGroupObject);
//                                Log.i("Forms", "Value123" + "\n" + listUserGroupObject.getFormId() + "\n" + listUserGroupObject.getFormName() + "\n" + listUserGroupObject.getFormDescription());
////                                listBeanArrayList = formsListBeen;
////                                handler.post(new Runnable() {
////                                    @Override
////                                    public void run() {
////                                        formsListAdapter.notifyDataSetChanged();
////                                    }
////                                });
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//
//                }

            }
        });
    }

    @Override
    public void ErrorMethod(Object object) {

    }


    private void showToast(final String s) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AddFormsInWebActivity.this, s, Toast.LENGTH_LONG)
                        .show();
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
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            showToast("Form Added Successfully ");
            progress.cancel();
            return super.shouldOverrideUrlLoading(view, url);

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

