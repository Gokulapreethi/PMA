package com.ase.blog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ase.Appreference;
import com.ase.R;

import org.json.JSONObject;

import json.EnumJsonWebservicename;

/**
 * Created by saravanakumar on 6/7/2016.
 */
public class BlogPostCommandActivity extends Activity {

    private Context context;
    String url = null;
    private WebView blogPost_webview;
    Button cancel;
    Handler handler = new Handler();
    EditText addcomment;
    static  BlogPostCommandActivity blogPostCommandActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.blog_post_command);
        this.context = this;

        blogPostCommandActivity = this;

        Log.d("activity"," inside on create ---> url i    =="+ url);
        Intent intent = getIntent();
        url = intent.getStringExtra("url");

        Button sendBtn = (Button)findViewById(R.id.sendBtn);
        blogPost_webview = (WebView) findViewById(R.id.post_web);

        blogPost_webview.getSettings().setJavaScriptEnabled(true);
        blogPost_webview.setWebViewClient(new WebviewClickEvent());
        blogPost_webview.loadUrl(url);



        cancel=(Button)findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Appreference.webview_refresh = true;
               finish();
            }
        });
        addcomment=(EditText)findViewById(R.id.addcomment);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                try {
                    if(isNetworkAvailable()) {
                        Log.i("postcomment", "user id-->" + Appreference.loginuserdetails.getId());
                        Log.i("postcomment", "post id-->" + url);
                        Log.i("postcomment", "description-->" + addcomment.getText().toString());

                        if(Appreference.loginuserdetails.getId()!= 0 && url!=null && addcomment.getText().toString().trim().length()>0) {
                            if(url.contains("getBlog?blogId")) {
                                JSONObject user_object = new JSONObject();
                                user_object.put("id", Appreference.loginuserdetails.getId());
                                JSONObject post_object = new JSONObject();
                                String blogId =  url.split("=")[1];
                                post_object.put("id", blogId.split("&")[0]);
                                JSONObject final_object = new JSONObject();
                                final_object.put("user", user_object);
                                final_object.put("blog", post_object);
                                final_object.put("description", addcomment.getText().toString());
                                Log.i("postcomment", "user id-->" + Appreference.loginuserdetails.getId());
                                Log.i("postcomment", "blog id-->" + blogId.split("&")[0] );
                                Log.i("postcomment", "description-->" + addcomment.getText().toString());
                                addcomment.setText("");
                                Appreference.jsonRequestSender.BlogCommentEntry(EnumJsonWebservicename.blogCommentEntry, final_object);
                            }
                            // http://122.165.92.171:8080/kamailioWeb/getBlogFile?blogFileId=3
                            //http://122.165.92.171:8080/kamailioWeb/getBlogFile?blogId=79&blogFileId=143
                            else if(url.contains("getBlogFile?blogId")){
                                String ids=url.split("\\?")[1];
                                String splitid1=ids.split("&")[0];
                                String splitid2=ids.split("&")[1];
                                String postId=splitid1.split("=")[1];
                                String postFileid=splitid2.split("=")[1];

                                JSONObject user_object = new JSONObject();
                                user_object.put("id",Appreference.loginuserdetails.getId());
                                JSONObject post_object = new JSONObject();
                                post_object.put("id", Integer.parseInt(postId));
                                JSONObject post_fileid = new JSONObject();
                                post_fileid.put("id", Integer.parseInt(postFileid));
                                JSONObject final_object = new JSONObject();
                                final_object.put("user", user_object);
                                final_object.put("blog", post_object);
                                final_object.put("blogFiles", post_fileid);
                                final_object.put("description", addcomment.getText().toString());
                                Log.i("postcomment", "user id-->" + Appreference.loginuserdetails.getId());
                                Log.i("postcomment", "post id-->" + postId);
                                Log.i("postcomment", "post id-->" + postFileid);
                                Log.i("postcomment", "description-->" + addcomment.getText().toString());
                                addcomment.setText("");
                                Appreference.jsonRequestSender.BlogFileCommentEntry(EnumJsonWebservicename.blogFileCommentEntry, final_object);


                            }
                        }
                    }else{
                        Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }



            }
        });


    }

    public void notifypostCommentEntryRespone(final String values){
        Log.i("postCommentEntry", "postpoupactivtiy  notifypostCommentEntryResponse method");
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (values != null) {

                        Log.i("response","response value"+values);
                        JSONObject jsonObject = new JSONObject(values);
                        Log.i("response"," response value"+jsonObject.getString("result_text"));
                        if (jsonObject != null) {
                            Log.i("response"," response value"+url);
                            if(jsonObject.getString("result_text") != null)
                            {
                                if (url != null) {

                                    InputMethodManager imm = (InputMethodManager)
                                            getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(
                                            addcomment.getApplicationWindowToken(), 0);

                                    if(url.contains("Blog?")) {
                                        Log.i("response"," inside blog");
                                        Appreference.webview_refresh = true;
                                    }else if(url.contains("BlogFile?"))
                                    {
                                        Log.i("response"," inside blog file");
                                        Appreference.webview_refresh = false;
                                    }
                                    blogPost_webview.loadUrl(url);


/*

                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(getApplicationContext().getApplicationWindowToken(), 0);
*/

                                    Toast.makeText(context, "Post Comment Sent Success", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                    Log.i("response"," response value"+url);
                                    Log.i("response"," response value"+jsonObject.getString("result_text"));
                                Toast.makeText(context, "Post Comment Sent Failed", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE );
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static BlogPostCommandActivity getInstance()
    {
    return blogPostCommandActivity;
    }


    private  class WebviewClickEvent extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            Log.i("redirect", "url--->" + url);

            blogPost_webview.loadUrl(url);

            return true;
        }
    }

}
