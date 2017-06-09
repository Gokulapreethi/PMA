package com.myapplication3;

import android.app.Dialog;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Created by vadivel on 20-06-2016.
 */
public class TasksFragment extends Fragment {

    public View view;
    TextView  overall_task_name,my_task,over_all_task,tasks_name;
    Button addTask,filterBtn;
    LinearLayout view_tasks, view_overall_task;
    WebView task_webview;
    LinearLayout tab_tasks_feed, tab_overall_tasks_feed;
    Handler handler;
    private SwipeRefreshLayout swipeContainer;
    public static boolean checkFilterMenu;

    boolean task_webview_refresh = false;
    public  boolean isMyTask_is_current_page=true;
    boolean isTicked = false;
    String key="open";
    LinearLayout ll_networkUI=null;
    TextView tv_networkstate=null;

    public static TasksFragment newInstance(int sectionNumber) {
        TasksFragment fragment = new TasksFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.task_fragment_layout, container, false);
        Appreference.context_table.put("taskfragment",this);
        ll_networkUI=(LinearLayout)view.findViewById(R.id.ll_networkstate);
        tv_networkstate=(TextView)view.findViewById(R.id.tv_networksate);
        task_webview = (WebView) view.findViewById(R.id.tasks_web);
//        overall_tasks_web = (WebView) view.findViewById(R.id.overall_tasks_web);

        tab_tasks_feed = (LinearLayout) view.findViewById(R.id.tab_tasks_feed);
        tab_overall_tasks_feed = (LinearLayout) view.findViewById(R.id.tab_overall_tasks_feed1);

        tasks_name = (TextView) view.findViewById(R.id.tasks_name);
        view_tasks = (LinearLayout) view.findViewById(R.id.tab_tasks_feed);

        overall_task_name = (TextView) view.findViewById(R.id.overall_task);
        view_overall_task = (LinearLayout) view.findViewById(R.id.tab_overall_tasks_feed1);

        addTask = (Button) view.findViewById(R.id.addTsk);
        filterBtn = (Button) view.findViewById(R.id.filterBtn);
//        tasks_name = (TextView) view.findViewById(R.id.tasks_name);


        task_webview.getSettings().setJavaScriptEnabled(true);

        task_webview.setWebViewClient(new SSLTolerentWebViewClient());
        handler = new Handler();

        key = "open";

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),NewTaskActivity.class);
                intent.putExtra("toUserName","");
                startActivity(intent);
            }
        });


        Log.i("tab-2", "boolean value-----> " + Appreference.global_blog_filter);
        if (!Appreference.open_task_filter) {
            myTasks();
        } else {
            overAllTask();
            tasks_name.setText("My Task");
        }

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable()) {
                    /*if (!task_webview_refresh)*/ /*{
                        if (Appreference.open_task_filter) {

                            task_webview.loadUrl(getResources().getString(R.string.app_url)+"listMyTask?userId="+ Appreference.loginuserdetails.getId()+"&type="+key );
                            Log.i("activity", "loginuserID--->" + "inside listblog");
                        } else {
                            task_webview.loadUrl(getResources().getString(R.string.app_url)+"listAllTask?userId="+ + Appreference.loginuserdetails.getId()+"&type="+key);
                            Log.i("activity", "loginuserID--->" + "inside listgroupblog");
                        }

//                    } else {
//                        if (Appreference.open_task_filter) {
//                            task_webview.loadUrl("https://122.165.92.171:8443/kamailioWeb/listMyTask");
//                            Log.i("activity", "loginuserID--->" + "inside listMyTaskFile");
//                        } else {
//                            task_webview.loadUrl("https://122.165.92.171:8443/kamailioWeb/listAllTask?userId=" + Appreference.loginuserdetails.getId());
//                            Log.i("activity", "loginuserID--->" + "inside listAllTaskFile");
//                        }
//
                    }*/

                    if(isMyTask_is_current_page)
                    {
                        Log.i("taskFragPage","RefreshMyTask with true   "+key);
                        task_webview.clearView();
                        task_webview.loadUrl(getResources().getString(R.string.app_url)+"listMyTask?userId="+ Appreference.loginuserdetails.getId()+"&type="+key );
                    }else
                    {
                        Log.i("taskFragPage","RefreshAllTask with true   "+key);
                        task_webview.clearView();
                        task_webview.loadUrl(getResources().getString(R.string.app_url)+"listAllTask?userId="+ Appreference.loginuserdetails.getId()+"&type="+key );
                    }
                }
                    else {
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

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.filter_taskmenu);
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

                my_task = (TextView) dialog.findViewById(R.id.my_task);
                over_all_task = (TextView) dialog.findViewById(R.id.over_all_task);

                if(checkFilterMenu)
                {
                    my_task.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }else
                {
                    over_all_task.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
//                if (isTicked)
//                {
//                    Log.i("taskFragPage","isTicked with true   "+isTicked);
//                    over_all_task.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                }
                my_task.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        try {
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        checkFilterMenu = false;
                        Appreference.open_task_filter = true;
//                        myTasks();

                        key = "open";

                        Log.i("taskFragPage","isMyTask_is_current_page with MyTask   "+isMyTask_is_current_page);

                        tasks_name.setText("My Task");

                        Log.i("tab", " inside global blog click event  = " + Appreference.global_blog_filter);

                        if(!isMyTask_is_current_page)
                        {
                            Log.e("taskFragPage","InsideOverAllTask with true   "+key);
                            task_webview.clearView();
                            task_webview.loadUrl(getResources().getString(R.string.app_url)+"listAllTask?userId="+Appreference.loginuserdetails.getId()+"&type="+key);
//                            task_webview.loadUrl(getResources().getString(R.string.app_url)+"listMyTask?userId=" +
//                                    "" + Appreference.loginuserdetails.getId()+"&type="+key);
                            tasks_name.setTextColor(Color.parseColor("#FFFFFF"));
                            tasks_name.setText("My Task");
//                            isMyTask_is_current_page=true;
                            Log.e("Url",getResources().getString(R.string.app_url)+"listMyTask?userId="+Appreference.loginuserdetails.getId()+"&type="+key);
                        }else
                            {
                                Log.e("taskFragPage","InsideMyTask with false   "+key);
                                task_webview.clearView();
                                task_webview.loadUrl(getResources().getString(R.string.app_url)+"listMyTask?userId="+Appreference.loginuserdetails.getId()+"&type="+key);
//                                task_webview.loadUrl(getResources().getString(R.string.app_url)+"listAllTask?userId=" +
//                                   "" + Appreference.loginuserdetails.getId()+"&type="+key);
                                tasks_name.setTextColor(Color.parseColor("#FFFFFF"));
                                tasks_name.setText("My Task");
//                                isMyTask_is_current_page=false;

                        } /*  blog_webview.getSettings().setJavaScriptEnabled(true);
                            blog_webview.setWebViewClient(new RedirectWebview());
*/
                       /* tasks_name.setTextColor(Color.parseColor("#FFFFFF"));
                        tasks_name.setText("My Task");*/

                    }
                });

                over_all_task.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            dialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        key = "all";
                        checkFilterMenu = true;
                        Appreference.open_task_filter = false;
//                        overAllTask();
                        tasks_name.setText("Over All Task");
                        Log.i("tab"," inside group blog click event  = "+ Appreference.global_blog_filter);

                        Log.i("taskFragPage","isMyTask_is_current_page with OverAll   "+isMyTask_is_current_page);
                        if(!isMyTask_is_current_page)
                        {
                            Log.e("taskFragPage","InsideOverAllTask with true   "+key);
                            task_webview.clearView();
                            task_webview.loadUrl(getResources().getString(R.string.app_url)+"listAllTask?userId="+Appreference.loginuserdetails.getId()+"&type="+key);
//                            task_webview.loadUrl(getResources().getString(R.string.app_url)+"listMyTask?userId=" +
//                                   "" +Appreference.loginuserdetails.getId()+"&type="+key);
                            tasks_name.setTextColor(Color.parseColor("#FFFFFF"));
                            tasks_name.setText("My Task");
                        }else
                        {

                            Log.e("taskFragPage","InsideMyTask with false   "+key);
                            task_webview.clearView();
                            task_webview.loadUrl(getResources().getString(R.string.app_url)+"listMyTask?userId="+Appreference.loginuserdetails.getId()+"&type="+key);
//                            task_webview.loadUrl(getResources().getString(R.string.app_url)+"listAllTask?userId=" +
//                                   "" + Appreference.loginuserdetails.getId()+"&type="+key);
Log.e("Url",getResources().getString(R.string.app_url)+"listMyTask?userId="+Appreference.loginuserdetails.getId()+"&type="+key);
                            tasks_name.setTextColor(Color.parseColor("#FFFFFF"));
                            tasks_name.setText("My Task");
                        }

                        task_webview.loadUrl(getResources().getString(R.string.app_url)+"listAllTask?userId=" +
                                Appreference.loginuserdetails.getId()+"&type="+key);
//                            blog_webview.getSettings().setJavaScriptEnabled(true);
//                            blog_webview.setWebViewClient(new RedirectWebview());

//                            activity_icon.setBackgroundResource(R.drawable.tab_activity_feed_active);
                       /* tasks_name.setTextColor(Color.parseColor("#FFFFFF"));
                        tasks_name.setText("Over All Task");*/
//                        view_activity.setBackgroundColor(getResources().getColor(R.color.appcolor));
////
////                            media_icon.setBackgroundResource(R.drawable.tab_media_feed);
//                        media_name.setTextColor(Color.parseColor("#cccccc"));
//                        view_media.setBackgroundColor(getResources().getColor(R.color.lightgray));

                    }
                       /* Toast.makeText(getActivity().getApplicationContext(),"Button Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;
*/
                });
//                popupMenu.show();//showing popup menu
        }


        });

        tab_tasks_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key="open";
                checkFilterMenu=false;
                isMyTask_is_current_page = true;
//                over_all_task.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                task_webview.clearView();
                task_webview.loadUrl(getResources().getString(R.string.app_url)+"listMyTask?userId="+Appreference.loginuserdetails.getId()+"&type="+key);
                        /*blog_webview.getSettings().setJavaScriptEnabled(true);
                        blog_webview.setWebViewClient(new RedirectWebview());*/

//                        activity_icon.setBackgroundResource(R.drawable.tab_activity_feed_active);

                Log.i("taskFragPage","InsideMyTabClicked with true   "+key);

                tasks_name.setTextColor(Color.parseColor("#FFFFFF"));
                tasks_name.setText("My Task");
                view_tasks.setBackgroundColor(getResources().getColor(R.color.appcolor));

                if(over_all_task != null) {
                    Log.i("taskFragPage","Inside  IF MyTask with true   "+key);
                    isTicked = true;
                }

//                        media_icon.setBackgroundResource(R.drawable.tab_media_feed);
                overall_task_name.setTextColor(Color.parseColor("#000000"));
                view_overall_task.setBackgroundColor(getResources().getColor(R.color.lightgray));

                       /* blog_webview.setVisibility(View.VISIBLE);
                        blogMedia_webview.setVisibility(View.GONE);*/

                cacheMemoryMaintenanceForTask();



//                task_webview_refresh = false;
            }
        });

        tab_overall_tasks_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tab", "inside global blog file");
                 /*   blog_webview.setVisibility(View.GONE);
                    blogMedia_webview.setVisibility(View.VISIBLE);*/
                // http://122.165.92.171:8080/kamailioWeb/listBlogFile
                isMyTask_is_current_page = false;
                key="open";
                checkFilterMenu=false;
//                over_all_task.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                if(over_all_task != null) {
                    Log.i("taskFragPage","Inside  IF MyTask with false   "+key);
                    isTicked = true;
                }
//
                Log.e("Url",getResources().getString(R.string.app_url)+"listMyTask?userId="+Appreference.loginuserdetails.getId()+"&type="+key);
                task_webview.clearView();
                task_webview.loadUrl(getResources().getString(R.string.app_url)+"listAllTask?userId="+Appreference.loginuserdetails.getId()+"&type="+key);

                Log.i("taskFragPage","InsideOverAllTabClicked with true   "+key);
                       /* blog_webview.getSettings().setJavaScriptEnabled(true);
                        blog_webview.setWebViewClient(new RedirectWebview());*/


//                        activity_icon.setBackgroundResource(R.drawable.tab_activity_feed);
                tasks_name.setTextColor(Color.parseColor("#000000"));
                view_tasks.setBackgroundColor(getResources().getColor(R.color.lightgray));

//                        media_icon.setBackgroundResource(R.drawable.tab_media_feed_active);
                overall_task_name.setTextColor(Color.parseColor("#FFFFFF"));
                view_overall_task.setBackgroundColor(getResources().getColor(R.color.appcolor));


//                        cacheMemoryMaintenanceForBlogMedia();

//                task_webview_refresh = true;

            }
        });
        return view;
    }



    private void myTasks() {

        isMyTask_is_current_page = true;
        task_webview.loadUrl(getResources().getString(R.string.app_url)+"listMyTask?userId=" + Appreference.loginuserdetails.getId()+"&type="+key);
        cacheMemoryMaintenanceForTask();
        tasks_name.setText("MyTask");
        view_overall_task.setBackgroundColor(getResources().getColor(R.color.lightgray));
        task_webview.setVisibility(View.VISIBLE);
        Appreference.open_task_filter=false;

    }

    private void overAllTask() {
        isMyTask_is_current_page = false;
        task_webview.loadUrl(getResources().getString(R.string.app_url)+"listAllTask?userId="+Appreference.loginuserdetails.getId()+"&type="+key);
        cacheMemoryMaintenanceForTask();
        overall_task_name.setText("OverAll Task");
        task_webview.setVisibility(View.VISIBLE);

    }

    private void cacheMemoryMaintenanceForTask() {
        task_webview.getSettings().setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
        task_webview.getSettings().setAppCachePath(getActivity().getApplicationContext().getCacheDir().getAbsolutePath());
        task_webview.getSettings().setAllowFileAccess(true);
        task_webview.getSettings().setAppCacheEnabled(true);
        task_webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        if (!isNetworkAvailable()) { // loading offline
            task_webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(getActivity().getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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


        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i("url", "url--->" + url);
//            if (url.equals("http://122.165.92.171/SNAZEvents/getPost?postId=12")) {
            if (url.contains("getTaskDetail")&& url.contains("taskId")) {
                Log.i("redirect", "=====nextscreen go=====");
                Intent intent=new Intent(getActivity(),Task_Percentage_update.class);
                intent.putExtra("url", url);
                getActivity().startActivity(intent);
                return true;
            }else
            {
                Log.i("url", "url--else ->" + url);
                task_webview.loadUrl(url);

            }
            return false;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(Appreference.context_table.containsKey("taskfragment")) {
            Appreference.context_table.remove("taskfragment");
        }
    }

    public void showNetworkStateUI(){
        if(ll_networkUI!=null && tv_networkstate!=null){
                if(Appreference.networkState){
                    if(Appreference.sipRegistrationState){
//                        ll_networkUI.setVisibility(View.GONE);
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

    @Override
    public void onResume() {
        super.onResume();
        showNetworkStateUI();
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
