package com.myapplication3;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.myapplication3.blog.BlogPostCommandActivity;

/**
 * Created by saravanakumar on 6/7/2016.
 */
public class BlogsFragment extends Fragment {


    public static BlogsFragment blogsFragment;
//    public static Context mainContext;
    public View view;
    EditText ed_search;
    private Button addPostBtn, clearAllBtn;
    private  Button filter;
    WebView blog_webview,blogMedia_webview;
    private SwipeRefreshLayout swipeContainer;
    Handler handler;
    boolean blog_webview_refresh,groupBlogFilter;
    LinearLayout tab_activity_feed;
    LinearLayout tab_media_feed;
//    ImageView media_icon,activity_icon;
    TextView media_name,activity_name, globalblog, groupblog;
    LinearLayout view_media,view_activity;

    public static boolean checkOncreate;
    public static boolean checkFilterMenu;
    LinearLayout ll_networkUI=null;
    TextView tv_networkstate=null;


    public static BlogsFragment newInstance(int sectionNumber) {
        BlogsFragment fragment = new BlogsFragment();
        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            view = inflater.inflate(R.layout.blog_fragment_layout, container, false);
            Appreference.context_table.put("blogsfragment",this);
            ll_networkUI=(LinearLayout)view.findViewById(R.id.ll_networkstate);
            tv_networkstate=(TextView)view.findViewById(R.id.tv_networksate);

            final Button addPost = (Button) view.findViewById(R.id.addBtn);
            filter = (Button) view.findViewById(R.id.filterBtn);

            blog_webview = (WebView) view.findViewById(R.id.activity_web);
            tab_activity_feed = (LinearLayout) view.findViewById(R.id.tab_activity_feed);
            tab_media_feed = (LinearLayout) view.findViewById(R.id.tab_media_feed1);

//            activity_icon = (ImageView) view.findViewById(R.id.activity_icon);
            activity_name = (TextView) view.findViewById(R.id.activity_name);
             view_activity = (LinearLayout) view.findViewById(R.id.tab_activity_feed);


//            media_icon = (ImageView) view.findViewById(R.id.media_icon);
             media_name = (TextView) view.findViewById(R.id.media_name);
            view_media = (LinearLayout) view.findViewById(R.id.tab_media_feed1);


            blog_webview.getSettings().setJavaScriptEnabled(true);

            blog_webview.setWebViewClient(new SSLTolerentWebViewClient());

        checkOncreate = true;
/*

            blog_webview.setWebChromeClient(new WebChromeClient());
            blog_webview.getSettings().setDomStorageEnabled(true);
            blog_webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            blog_webview.getSettings().setPluginState(WebSettings.PluginState.ON);
            blog_webview.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
*/

        Log.i("tab","default boolean value   = "+ Appreference.global_blog_filter);
        if(Appreference.global_blog_filter) {

            globalBlog();

        }else {
            // group blog load on oncreate
            groupBlog();
            activity_name.setText("Group Blog");

        }

            blogMedia_webview = (WebView) view.findViewById(R.id.activity_media_web);

            handler = new Handler();

            blog_webview_refresh = false;

            Log.i("activity", "loginuserID--->" + Appreference.loginuserdetails.getId());
            Log.i("activity", "https://122.165.92.171:8443/kamailioWeb/listBlog?userId=" + Appreference.loginuserdetails.getId());


            //  https://122.165.92.171:8443/kamailioWeb/listBlog?userId=1


            swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // Your code to refresh the list here.
                    // Make sure you call swipeContainer.setRefreshing(false)
                    // once the network request has completed successfully.
                    if (isNetworkAvailable()) {
                        if (!blog_webview_refresh) {
                            if(Appreference.global_blog_filter) {
                                blog_webview.loadUrl(getResources().getString(R.string.app_url)+"listBlog?userId=" + Appreference.loginuserdetails.getId());
                                Log.i("activity", "loginuserID--->" + "inside listblog");
                            }else{
                                blog_webview.loadUrl(getResources().getString(R.string.app_url)+"listGroupBlog?userId=" + Appreference.loginuserdetails.getId());
                                Log.i("activity", "loginuserID--->" + "inside listgroupblog");
                            }
                        } else {
                            if(Appreference.global_blog_filter) {
                                blog_webview.loadUrl(getResources().getString(R.string.app_url)+"listBlogFile");
                                Log.i("activity", "loginuserID--->" + "inside listblogfile");
                            }else
                            {
                                blog_webview.loadUrl(getResources().getString(R.string.app_url)+"listGroupBlogFile?userId="+Appreference.loginuserdetails.getId());
                                Log.i("activity", "loginuserID--->" + "inside listgroupblogfile");
                            }

                        }
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeContainer.setRefreshing(false);

                        }
                    }, 2000);
                }
            });





        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.filter_menu);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.horizontalMargin = 15;
                Window window = dialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setAttributes(lp);
                window.setGravity(Gravity.TOP);
                dialog.show();

                globalblog = (TextView) dialog.findViewById(R.id.global_blog);
                groupblog = (TextView) dialog.findViewById(R.id.group_blog);

                if(checkFilterMenu)
                {
                    groupblog.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }else
                {
                    globalblog.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }


                globalblog.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        try {
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        checkFilterMenu = true;
                        Appreference.global_blog_filter = true;
                        globalBlog();
                        activity_name.setText("Global Blog");


                        Log.i("tab", " inside global blog click event  = " + Appreference.global_blog_filter);

                            blog_webview.loadUrl(getResources().getString(R.string.app_url)+"listBlog?userId=" + Appreference.loginuserdetails.getId());
                           /* blog_webview.getSettings().setJavaScriptEnabled(true);
                            blog_webview.setWebViewClient(new RedirectWebview());*/


//                            activity_icon.setBackgroundResource(R.drawable.tab_activity_feed_active);
                            activity_name.setTextColor(Color.parseColor("#FFFFFF"));
                            activity_name.setText("Global Blog");
                            view_activity.setBackgroundColor(getResources().getColor(R.color.appcolor));

//                            media_icon.setBackgroundResource(R.drawable.tab_media_feed);
                            media_name.setTextColor(Color.parseColor("#000000"));
                            view_media.setBackgroundColor(getResources().getColor(R.color.lightgray));
                        }
					  });

                groupblog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        checkFilterMenu = false;
                        Appreference.global_blog_filter = false;
                            groupBlog();
                            activity_name.setText("Group Blog");
                            Log.i("tab"," inside group blog click event  = "+ Appreference.global_blog_filter);

                            blog_webview.loadUrl(getResources().getString(R.string.app_url)+"listGroupBlog?userId=" + Appreference.loginuserdetails.getId());
                            /*blog_webview.getSettings().setJavaScriptEnabled(true);
                            blog_webview.setWebViewClient(new RedirectWebview());*/


//                            activity_icon.setBackgroundResource(R.drawable.tab_activity_feed_active);
                            activity_name.setTextColor(Color.parseColor("#FFFFFF"));
                            activity_name.setText("Group Blog");
                            view_activity.setBackgroundColor(getResources().getColor(R.color.appcolor));

//                            media_icon.setBackgroundResource(R.drawable.tab_media_feed);
                            media_name.setTextColor(Color.parseColor("#000000"));
                            view_media.setBackgroundColor(getResources().getColor(R.color.lightgray));

                        }
                       /* Toast.makeText(getActivity().getApplicationContext(),"Button Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;
                    }*/
                });
//                popupMenu.show();//showing popup menu
            }

        });


            tab_activity_feed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.i("tab"," activity feed  = "+ Appreference.global_blog_filter);
                    if(Appreference.global_blog_filter) {
                        Log.i("tab", "global blog");
                        blog_webview.loadUrl(getResources().getString(R.string.app_url)+"listBlog?userId=" + Appreference.loginuserdetails.getId());
                        /*blog_webview.getSettings().setJavaScriptEnabled(true);
                        blog_webview.setWebViewClient(new RedirectWebview());*/

//                        activity_icon.setBackgroundResource(R.drawable.tab_activity_feed_active);
                        activity_name.setTextColor(Color.parseColor("#FFFFFF"));
                        activity_name.setText("Global Blog");
                        view_activity.setBackgroundColor(getResources().getColor(R.color.appcolor));

//                        media_icon.setBackgroundResource(R.drawable.tab_media_feed);
                        media_name.setTextColor(Color.parseColor("#000000"));
                        view_media.setBackgroundColor(getResources().getColor(R.color.lightgray));

                       /* blog_webview.setVisibility(View.VISIBLE);
                        blogMedia_webview.setVisibility(View.GONE);*/

                        cacheMemoryMaintenanceForBlog();

                        blog_webview_refresh = false;
                    }else
                    {

                        Log.i("tab", "group blog");
                        blog_webview.loadUrl(getResources().getString(R.string.app_url)+"listGroupBlog?userId=" + Appreference.loginuserdetails.getId());
                       /* blog_webview.getSettings().setJavaScriptEnabled(true);
                        blog_webview.setWebViewClient(new RedirectWebview());*/

//                        activity_icon.setBackgroundResource(R.drawable.tab_activity_feed_active);
                        activity_name.setTextColor(Color.parseColor("#FFFFFF"));
                        activity_name.setText("Group Blog");
                        view_activity.setBackgroundColor(getResources().getColor(R.color.appcolor));

//                        media_icon.setBackgroundResource(R.drawable.tab_media_feed);
                        media_name.setTextColor(Color.parseColor("#000000"));
                        view_media.setBackgroundColor(getResources().getColor(R.color.lightgray));

                        /*blog_webview.setVisibility(View.VISIBLE);
                        blogMedia_webview.setVisibility(View.GONE);*/

                        cacheMemoryMaintenanceForBlog();

                        blog_webview_refresh = false;
                    }

                }
            });


            tab_media_feed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Log.i("tab"," media feed  = "+ Appreference.global_blog_filter);
                    if (Appreference.global_blog_filter) {
                        Log.i("tab", "inside global blog file");
                 /*   blog_webview.setVisibility(View.GONE);
                    blogMedia_webview.setVisibility(View.VISIBLE);*/
                        // http://122.165.92.171:8080/kamailioWeb/listBlogFile
//
                        blog_webview.loadUrl(getResources().getString(R.string.app_url)+"listBlogFile");

                       /* blog_webview.getSettings().setJavaScriptEnabled(true);
                        blog_webview.setWebViewClient(new RedirectWebview());*/


//                        activity_icon.setBackgroundResource(R.drawable.tab_activity_feed);
                        activity_name.setTextColor(Color.parseColor("#000000"));
                        view_activity.setBackgroundColor(getResources().getColor(R.color.lightgray));

//                        media_icon.setBackgroundResource(R.drawable.tab_media_feed_active);
                        media_name.setTextColor(Color.parseColor("#FFFFFF"));
                        view_media.setBackgroundColor(getResources().getColor(R.color.appcolor));


//                        cacheMemoryMaintenanceForBlogMedia();

                        blog_webview_refresh = true;

                    }else
                    {

                        Log.i("tab", "inside group blog file");
                    /*blog_webview.setVisibility(View.GONE);
                    blogMedia_webview.setVisibility(View.VISIBLE);*/

                        blog_webview.loadUrl(getResources().getString(R.string.app_url)+"listGroupBlogFile?userId="+Appreference.loginuserdetails.getId());
                        blog_webview.getSettings().setJavaScriptEnabled(true);
                        blog_webview.setWebViewClient(new RedirectWebview());


//                        activity_icon.setBackgroundResource(R.drawable.tab_activity_feed);
                        activity_name.setTextColor(Color.parseColor("#000000"));
                        view_activity.setBackgroundColor(getResources().getColor(R.color.lightgray));

//                        media_icon.setBackgroundResource(R.drawable.tab_media_feed_active);
                        media_name.setTextColor(Color.parseColor("#FFFFFF"));
                        view_media.setBackgroundColor(getResources().getColor(R.color.appcolor));


//                        cacheMemoryMaintenanceForBlogMedia();

                        blog_webview_refresh = true;
                    }
                }

            });


            addPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // open new addPostActivity via intent

                    Intent intent = new Intent(getActivity().getApplicationContext(),NewBlogActivity.class);
                    intent.putExtra("blog filter",Appreference.global_blog_filter);
                    startActivity(intent);
//                    Toast.makeText(getActivity().getApplicationContext(), "ADD NEW POST", Toast.LENGTH_SHORT).show();
                }
            });



        return view;
    }



    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void globalBlog()
    {

        // global blog load on oncreate  https://122.165.92.171:8443/kamailioWeb/organizationalPerformancePieChart
        blog_webview.loadUrl(getResources().getString(R.string.app_url)+"listBlog?userId=" + Appreference.loginuserdetails.getId());
        cacheMemoryMaintenanceForBlog();
//        blog_webview.loadUrl("https://122.165.92.171:8443/kamailioWeb/organizationalPerformancePieChart");
        activity_name.setText("Global Blog");
        blog_webview.setVisibility(View.VISIBLE);
    }

    public void groupBlog()
    {
        // group  blog load on oncreate
        blog_webview.loadUrl(getResources().getString(R.string.app_url)+"listGroupBlog?userId=" + Appreference.loginuserdetails.getId());
//        blog_webview.loadUrl("https://122.165.92.171:8443/kamailioWeb/organizationalPerformancePieChart");
        cacheMemoryMaintenanceForBlog();
        activity_name.setText("Group Blog");
        tab_media_feed.setBackgroundColor(getResources().getColor(R.color.lightgray));
        blog_webview.setVisibility(View.VISIBLE);
        Appreference.global_blog_filter = false;

    }

    public void cacheMemoryMaintenanceForBlog() {
        //Used For webview load OFFLine
        //start
        blog_webview.getSettings().setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
        blog_webview.getSettings().setAppCachePath(getActivity().getApplicationContext().getCacheDir().getAbsolutePath());
        blog_webview.getSettings().setAllowFileAccess(true);
        blog_webview.getSettings().setAppCacheEnabled(true);
        blog_webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        if (!isNetworkAvailable()) { // loading offline
            blog_webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        //End

    }

   /* public void cacheMemoryMaintenanceForBlogMedia() {
        //Used For webview load OFFLine
        //start
        blogMedia_webview.getSettings().setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
        blogMedia_webview.getSettings().setAppCachePath(getActivity().getApplicationContext().getCacheDir().getAbsolutePath());
        blogMedia_webview.getSettings().setAllowFileAccess(true);
        blogMedia_webview.getSettings().setAppCacheEnabled(true);
        blogMedia_webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        if (!isNetworkAvailable()) { // loading offline
            blogMedia_webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        //End

    }*/



    private class RedirectWebview extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("redirect", "url--->" + url);
//            if (url.equals("http://122.165.92.171/SNAZEvents/getPost?postId=12")) {
            if (url.contains("getBlog?blogId")|| url.contains("getBlogFile?blogId")) {
                Log.i("redirect", "=====nextscreen go=====");
                Intent intent=new Intent(getActivity(),BlogPostCommandActivity.class);
                intent.putExtra("url", url);
                getActivity().startActivity(intent);
                return true;
            }
            return false;
        }

        @SuppressLint("NewApi")
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);

            // this will ignore the Ssl error and will go forward to your site
            handler.proceed();
            error.getCertificate();
        }
    }


    private class SSLTolerentWebViewClient extends WebViewClient {
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

           /* message += " Do you want to continue anyway?";
            alertDialog.setTitle("SSL Certificate Error");
            alertDialog.setMessage(message);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Ignore SSL certificate errors
                    handler.proceed();
                }
            });

            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    handler.cancel();
                }
            });
            alertDialog.show();*/
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("redirect", "url--->" + url);
//            if (url.equals("http://122.165.92.171/SNAZEvents/getPost?postId=12")) {
            if (url.contains("getBlog?blogId")|| url.contains("getBlogFile?blogId")) {
                Log.i("redirect", "=====nextscreen go=====");
                Intent intent=new Intent(getActivity(),BlogPostCommandActivity.class);
                intent.putExtra("url", url);
                getActivity().startActivity(intent);
                return true;
            }
            return false;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        blog_webview.onPause();
        checkOncreate = false;
//        blogMedia_webview.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        blog_webview.onResume();

        if (!checkOncreate) {
            if (Appreference.webview_refresh) {
                if (Appreference.global_blog_filter) {
                    blog_webview.loadUrl(getResources().getString(R.string.app_url)+"listBlog?userId=" + Appreference.loginuserdetails.getId());
                    Log.i("activity", "loginuserID--->" + "inside listblogfile");
                    activity_name.setTextColor(Color.parseColor("#FFFFFF"));
                    activity_name.setText("Global Blog");
                    view_activity.setBackgroundColor(getResources().getColor(R.color.appcolor));

//                        media_icon.setBackgroundResource(R.drawable.tab_media_feed);
                    media_name.setTextColor(Color.parseColor("#000000"));
                    view_media.setBackgroundColor(getResources().getColor(R.color.lightgray));
                    Log.i("activity", "loginuserID--->" + "inside listblog");
                } else {
                    blog_webview.loadUrl(getResources().getString(R.string.app_url)+"listGroupBlog?userId=" + Appreference.loginuserdetails.getId());
                    Log.i("activity", "loginuserID--->" + "inside listgroupblogfile");

                    activity_name.setTextColor(Color.parseColor("#FFFFFF"));
                    activity_name.setText("Group Blog");
                    view_activity.setBackgroundColor(getResources().getColor(R.color.appcolor));

//                        media_icon.setBackgroundResource(R.drawable.tab_media_feed);
                    media_name.setTextColor(Color.parseColor("#000000"));
                    view_media.setBackgroundColor(getResources().getColor(R.color.lightgray));
                }
            } else {
                if (Appreference.global_blog_filter) {
                    blog_webview.loadUrl(getResources().getString(R.string.app_url)+"listBlog?userId=" + Appreference.loginuserdetails.getId());
                    Log.i("activity", "loginuserID--->" + "inside listblogfile");
                    activity_name.setTextColor(Color.parseColor("#FFFFFF"));
                    activity_name.setText("Global Blog");
                    view_activity.setBackgroundColor(getResources().getColor(R.color.appcolor));

//                        media_icon.setBackgroundResource(R.drawable.tab_media_feed);
                    media_name.setTextColor(Color.parseColor("#000000"));
                    view_media.setBackgroundColor(getResources().getColor(R.color.lightgray));
                } else {
                    blog_webview.loadUrl(getResources().getString(R.string.app_url)+"listGroupBlog?userId=" + Appreference.loginuserdetails.getId());
                    Log.i("activity", "loginuserID--->" + "inside listgroupblogfile");

                    activity_name.setTextColor(Color.parseColor("#FFFFFF"));
                    activity_name.setText("Group Blog");
                    view_activity.setBackgroundColor(getResources().getColor(R.color.appcolor));

//                        media_icon.setBackgroundResource(R.drawable.tab_media_feed);
                    media_name.setTextColor(Color.parseColor("#000000"));
                    view_media.setBackgroundColor(getResources().getColor(R.color.lightgray));

                }

            }
        }

        showNetworkStateUI();
    }
//        blogMedia_webview.onResume();


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(Appreference.context_table.containsKey("blogsfragment")){
            Appreference.context_table.remove("blogsfragment");
        }
    }

    public void showNetworkStateUI(){
        if(ll_networkUI!=null && tv_networkstate!=null){
            if(Appreference.networkState){
                if(Appreference.sipRegistrationState){
//                    ll_networkUI.setVisibility(View.GONE);
                }else if(!Appreference.sipRegistrationState){
                    ll_networkUI.setVisibility(View.VISIBLE);
                    ll_networkUI.setBackgroundColor(getResources().getColor(R.color.orange));
                    tv_networkstate.setText("Connecting...");
                }
            }else if(!Appreference.networkState){
                ll_networkUI.setVisibility(View.VISIBLE);
                ll_networkUI.setBackgroundColor(getResources().getColor(R.color.red_color));
                tv_networkstate.setText("No Internet Connection");
            }
        }

    }

    public void showNetWorkConnectedState(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(ll_networkUI!=null && tv_networkstate!=null) {
                    Log.i("network","ll_networkUI!=null");
                    if (Appreference.networkState) {
                        Log.i("network","Appreference.networkState");
                        if (Appreference.sipRegistrationState) {
                            Log.i("network","Appreference.sipRegistrationState");
                            ll_networkUI.setVisibility(View.VISIBLE);
                            ll_networkUI.setBackgroundColor(getResources().getColor(R.color.connected));
                            tv_networkstate.setText("Connected");
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ll_networkUI.setVisibility(View.GONE);
                                }
                            }, 2000);
                        }
                    }
                }
            }
        });
    }
}

