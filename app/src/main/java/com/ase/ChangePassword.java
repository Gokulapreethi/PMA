package com.ase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//import com.cg.commonclass.CallDispatcher;
//import com.cg.hostedconf.AppReference;
//import com.cg.snazevent.R;
//import com.util.SingleInstance;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.NegativeValue;
import json.WebServiceInterface;

public class ChangePassword extends Activity implements WebServiceInterface {

    EditText et_oldpassword,et_newpassword,et_repeatpassword;
    Button savepswd;
    Context context;
    public ProgressDialog progressDialog;
    Handler handler=new Handler();
    private String blockCharacterSet = "/n";
    Handler handler1 = new Handler();
    static ChangePassword changePassword;

    public static ChangePassword getInstance() {
        return changePassword;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.changepassword);
        context = this;
        changePassword = this;
        et_oldpassword = (EditText) findViewById(R.id.et_oldpassword);
        et_newpassword = (EditText) findViewById(R.id.et_newpassword);
        et_repeatpassword = (EditText) findViewById(R.id.et_repeatpassword);
        InputFilter filter = new InputFilter() {

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                if (source != null && blockCharacterSet.contains(("" + source))) {
                    return "";
                }
                return null;
            }
        };
        et_newpassword.setFilters(new InputFilter[] { filter });
        et_oldpassword.setFilters(new InputFilter[]{filter});
        et_repeatpassword.setFilters(new InputFilter[]{filter});
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(11); //Filter to 11 characters
        et_repeatpassword .setFilters(filters);
        et_oldpassword.setFilters(filters);
        et_newpassword .setFilters(filters);



        //AppReference.snazevent_AllContext.put("changepassword",context);
        savepswd=(Button)findViewById(R.id.savepswd);
        savepswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_oldpassword.getText().toString().trim().length()>0){
                    if(et_newpassword.getText().toString().trim().length()>0){
                        if(et_repeatpassword.getText().toString().trim().length()>0){
                            if(et_newpassword.getText().toString().trim().equalsIgnoreCase(et_repeatpassword.getText().toString().trim())){
                               // if(CallDispatcher.isConnected){
                                    showprogress("Progress...");
                                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                                    nameValuePairs.add(new BasicNameValuePair("userId",String.valueOf(Appreference.loginuserdetails.getId())));
                                    nameValuePairs.add(new BasicNameValuePair("oldPassword", et_oldpassword.getText().toString().trim()));
                                    nameValuePairs.add(new BasicNameValuePair("newPassword", et_newpassword.getText().toString().trim()));
                                    Appreference.jsonRequestSender.Changepassword(EnumJsonWebservicename.changePasswordEvent, nameValuePairs,ChangePassword.this);
//                                }else{
//                                    showToast("Network Not Available");
//                                }
                            }else{
                                showToast("Doesn't Match the Confirm password");
                            }
                        }else{
                            showToast("Please Enter The Confirm Password");
                        }
                    }else{
                        showToast("Please Enter The New Password");
                    }

                }else{
                    showToast("Please Enter The Old Password");
                }
            }
        });

        ImageView cancel = (ImageView)findViewById(R.id.cancel);
//        Button cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void showToast(String values) {
        Toast.makeText(context, values, Toast.LENGTH_SHORT).show();
    }

    public void showprogress(final String title) {
    handler.post(new Runnable() {
        @Override
        public void run() {
            try {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(context);
                    if (progressDialog != null) {
                        progressDialog.setCancelable(true);
                        progressDialog.setMessage(title);
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setProgress(0);
                        progressDialog.setMax(100);
                        progressDialog.show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
    }

    public void canceProgress() {
        try {

            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showAlert(final String value){
        handler.post(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
                // set dialog messagealertDialogBuilder
                alertDialogBuilder.setMessage(value)
                        .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

    }


    public void notifyChangepasswordResponse(String result) {
        try {
            if (result != null) {
                canceProgress();
                JSONObject json = new JSONObject(result);
                if (json != null) {
                    if(json.getString("result_text")!=null) {
                        String response = json.getString("result_text");
                        showAlert(response);
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
//        AppReference.snazevent_AllContext.remove("changepassword");
    }

    public void notitfyError(final String reponse){
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    canceProgress();
                    AlertDialog.Builder alertCall = new AlertDialog.Builder(context);
                    alertCall
                            .setMessage(reponse)
                            .setCancelable(false)
                            .setNegativeButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            try {
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                    alertCall.show();
                } catch (Exception e) {
                    e.printStackTrace();
//                    SingleInstance.printLog(null, e.getMessage(), null, e);
                }

            }
        });
    }

    @Override
    public void ResponceMethod(final Object object) {
        handler1.post(new Runnable() {
            @Override
            public void run() {
                CommunicationBean opr = (CommunicationBean) object;
                String s1 = ((CommunicationBean) object).getEmail();
                Log.i("ChangePassword","ValueGroup-------------->"+s1);
                Gson gson=new Gson();
                String test1=s1.toString();
                NegativeValue negativeValue= gson.fromJson(test1,NegativeValue.class);
                test1=negativeValue.getText();
                if(test1.equals("wrong Old Password")){
                    Toast.makeText(getApplicationContext(),test1,Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),test1,Toast.LENGTH_SHORT).show();
                    finish();
                }
                progressDialog.dismiss();

            }
        });
    }

    @Override
    public void ErrorMethod(Object object) {

    }
}
