package com.ase;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ase.Bean.User_Details;
import com.ase.DB.VideoCallDataBase;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.pjsip.AESCrypto;
import org.pjsip.pjsua2.app.MainActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.sephiroth.android.library.tooltip.Tooltip;
import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.JsonRequestSender;
import json.Loginuserdetails;
import json.NegativeValue;
import json.Queue;
import json.Usergroubdetails;
import json.WebServiceInterface;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, WebServiceInterface {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    Context context;
    private static Queue queue = new Queue();
    Handler handler = new Handler();
    String Answer, Username, user_id;
    private ProgressDialog progress;
    static LoginActivity loginActivity;
    final String USER_DETAILS = "USER_INFO";
    static int no_Of_Login = 1;
    List<User_Details> list_Of_User = new LinkedList<User_Details>();
    ImageView imageView;
    public Button mEmailSignInButton;
    public ProgressBar spinner_signin;

    public static LoginActivity getInstance() {
        return loginActivity;
    }

    AppSharedpreferences appSharedpreferences;
    private String need_to_call_listallmytask;

    //For Auto Login
    private SharedPreferences preferences;
    private boolean logoutSuccess = false;
    CheckBox checkeula;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        Appreference.context_table.put("loginactivity", this);
        TextView highmessage = (TextView) findViewById(R.id.highmessage);
        this.context = this;
        loginActivity = this;
        logoutSuccess = getIntent().getBooleanExtra("logout", false);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        appSharedpreferences = AppSharedpreferences.getInstance(context);
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        imageView = (ImageView) findViewById(R.id.logo);

        checkeula = (CheckBox) findViewById(R.id.check_eula);
        checkeula.setChecked(true);
        int value = VideoCallDataBase.getDB(getApplicationContext()).geteulavalue();
        checkeula.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    VideoCallDataBase.getDB(getApplicationContext()).insertvalueeula(true);
                } else {
                }
            }
        });

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        preferences = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());

        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        spinner_signin = (ProgressBar) findViewById(R.id.progressBar_signin);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("login1234", "mEmailSignInButton clickListener");
                if (checkeula.isChecked() == true) {
                    loginMethod(view);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Agree to Terms and Conditions", Toast.LENGTH_SHORT).show();
                }

                //attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        String username = preferences.getString("uname", null);
        String password = preferences.getString("pword", null);
        Log.i("autologin", "preferences login username-->" + username);
        Log.i("autologin", "preferences login password-->" + password);

        if (username != null && password != null && !logoutSuccess) {
            Log.i("autologin", "username ,password!=null && logoutSuccess=false");
            mEmailView.setText(username);
            mPasswordView.setText(password);
            if (isNetworkAvailable() && appSharedpreferences.getBoolean("login")) {
                Log.i("login1234", "autoLogin if case");

                if (!Appreference.isLoginRequestSent) {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("email", mEmailView.getText().toString()));
                    nameValuePairs.add(new BasicNameValuePair("password", mPasswordView.getText().toString()));
//                    nameValuePairs.add(new BasicNameValuePair("textId", AESCrypto.encrypt(Appreference.getIMEINumber(context))));
                    JsonRequestSender jsonRequestParser = new JsonRequestSender();
                    Appreference.jsonRequestSender = jsonRequestParser;
                    Log.i("login123", "Login webservice calling oncreate.......");

                    jsonRequestParser.login(EnumJsonWebservicename.loginMobile, nameValuePairs, LoginActivity.this);
                    jsonRequestParser.start();
                    mEmailSignInButton.setVisibility(View.GONE);
                    spinner_signin.setVisibility(View.VISIBLE);
                }

            } else if (appSharedpreferences.getBoolean("login")) {
                Log.i("login1234", "autoLogin else case");

                mEmailSignInButton.setVisibility(View.GONE);
                spinner_signin.setVisibility(View.VISIBLE);
                if (Appreference.jsonRequestSender == null) {
                    JsonRequestSender jsonRequestParser = new JsonRequestSender();
                    Appreference.jsonRequestSender = jsonRequestParser;
                    jsonRequestParser.start();
                }
                attemptLogin();
            }
            /*else {
                Toast.makeText(getApplicationContext(), "Please Check Your Internet", Toast.LENGTH_LONG).show();
            }*/
        } else {
            Log.i("login1234", "not an autoLogin else case");

            Log.i("autologin", "username ,password==null && logoutSuccess=true");
            mEmailView.setText("");
            mPasswordView.setText("");
            mEmailSignInButton.setVisibility(View.VISIBLE);
            spinner_signin.setVisibility(View.GONE);
        }
        appSharedpreferences.saveBoolean("login", false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("service123", "Receieved notification about offlineSendActivity Called");
//        startService(new Intent(getApplicationContext(), offlineSendService.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("LoginActivity", "onBackPressed ");
        try {
            finish();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("LoginActivity", "onBackPressed Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void loginMethod(View view) {
        mPasswordView.clearFocus();
        String em = mEmailView.getText().toString();
        String pa = mPasswordView.getText().toString();
        ImageView img_error_user = (ImageView) findViewById(R.id.error_info_email);
        ImageView img_error_pass = (ImageView) findViewById(R.id.error_info_pass);

        if (em.equals("") || em.equals(null)) {
            img_error_user.setVisibility(View.VISIBLE);
            mEmailSignInButton.setVisibility(View.VISIBLE);
            spinner_signin.setVisibility(View.GONE);
            Tooltip.make(this,
                    new Tooltip.Builder(102)
                            .anchor(img_error_user, Tooltip.Gravity.BOTTOM)
                            .closePolicy(new Tooltip.ClosePolicy()
                                    .insidePolicy(true, false)
                                    .outsidePolicy(true, false), 4000)
                            .activateDelay(900)
                            .showDelay(400)
                            .text("Please Enter Your Email ID")
                            .maxWidth(600)
                            .withArrow(true)
                            .withOverlay(true).build()
            ).show();
//            Toast.makeText(getApplicationContext(), "Please Enter Your Email ID", Toast.LENGTH_SHORT).show();
        } else if (!(em.contains("@") && em.contains("."))) {
            img_error_user.setVisibility(View.VISIBLE);
            mEmailSignInButton.setVisibility(View.VISIBLE);
            spinner_signin.setVisibility(View.GONE);
            Tooltip.make(this,
                    new Tooltip.Builder(102)
                            .anchor(img_error_user, Tooltip.Gravity.BOTTOM)
                            .closePolicy(new Tooltip.ClosePolicy()
                                    .insidePolicy(true, false)
                                    .outsidePolicy(true, false), 4000)
                            .activateDelay(900)
                            .showDelay(400)
                            .text("Please Enter Correct Email ID")
                            .maxWidth(600)
                            .withArrow(true)
                            .withOverlay(true).build()
            ).show();
//            Toast.makeText(getApplicationContext(), "Please Enter Correct Email ID", Toast.LENGTH_SHORT).show();
        } else if (pa.equals("") || pa.equals(null)) {
            img_error_pass.setVisibility(View.VISIBLE);
            img_error_user.setVisibility(View.GONE);
            mEmailSignInButton.setVisibility(View.VISIBLE);
            spinner_signin.setVisibility(View.GONE);
            Tooltip.make(this,
                    new Tooltip.Builder(102)
                            .anchor(img_error_pass, Tooltip.Gravity.BOTTOM)
                            .closePolicy(new Tooltip.ClosePolicy()
                                    .insidePolicy(true, false)
                                    .outsidePolicy(true, false), 4000)
                            .activateDelay(900)
                            .showDelay(400)
                            .text("Please Enter password")
                            .maxWidth(600)
                            .withArrow(true)
                            .withOverlay(true).build()
            ).show();
//            Toast.makeText(getApplicationContext(), "Please Enter password", Toast.LENGTH_SHORT).show();
        } else {
            if (isNetworkAvailable()) {
                try {
                    if (!Appreference.isLoginRequestSent) {
                        img_error_user.setVisibility(View.GONE);
                        img_error_pass.setVisibility(View.GONE);

                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                        nameValuePairs.add(new BasicNameValuePair("email", mEmailView.getText().toString()));
                        nameValuePairs.add(new BasicNameValuePair("password", mPasswordView.getText().toString()));
//                        nameValuePairs.add(new BasicNameValuePair("textId", AESCrypto.encrypt(Appreference.getIMEINumber(context))));
                        JsonRequestSender jsonRequestParser = new JsonRequestSender();

                        Appreference.jsonRequestSender = jsonRequestParser;
                        Log.i("login123", "Login webservice calling loginMethod.......");
                        mEmailSignInButton.setVisibility(View.GONE);
                        spinner_signin.setVisibility(View.VISIBLE);

                        jsonRequestParser.login(EnumJsonWebservicename.loginMobile, nameValuePairs, LoginActivity.this);
                        jsonRequestParser.start();
                        InputMethodManager imm = (InputMethodManager) loginActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        //Find the currently focused view, so we can grab the correct window token from it.
                        View view1 = loginActivity.getCurrentFocus();
                        //If no view currently has focus, create a new one, just so we can grab a window token from it
                        if (view1 == null) {
                            view1 = new View(loginActivity);
                        }
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                mEmailSignInButton.setVisibility(View.VISIBLE);
                spinner_signin.setVisibility(View.GONE);
//                Toast.makeText(getApplicationContext(), "Please Check Your Internet", Toast.LENGTH_LONG).show();
            }
        }

        //attemptLogin();
    }
    // check internet connection

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = Username;
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

//        // Check for a valid password, if the user entered one.
//        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            cancel = true;
//        }
//
//        // Check for a valid email address.
//        if (TextUtils.isEmpty(email)) {
//            mEmailView.setError(getString(R.string.error_field_required));
//            focusView = mEmailView;
//            cancel = true;
//        } else if (!isEmailValid(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            focusView = mEmailView;
//            cancel = true;
//        }
//
//        if (cancel) {
//            // There was an error; don't attempt login and focus the first
//            // form field with an error.
//            focusView.requestFocus();
//        } else {
        // Show a progress spinner, and kick off a background task to
        // perform the user login attempt.
        //showProgress(true);
        mAuthTask = new UserLoginTask(email, password);
        mAuthTask.execute((Void) null);
//        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
//        return email.contains("@");
        return true;
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    public void cancelDialog() {
        try {
            if (progress != null && progress.isShowing()) {
                Log.i("register", "--progress bar end-----");
                progress.dismiss();
                progress = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void showToast(final String result) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void ResponceMethod(final Object object) {

        handler.post(new Runnable() {
            @Override
            public void run() {
                CommunicationBean opr = (CommunicationBean) object;
                String s1 = opr.getEmail();
                try {
                    Gson g = new Gson();
                    String test = s1.toString();
                    JsonElement jelement = new JsonParser().parse(s1);
                    if (!jelement.getAsJsonObject().equals(null)) {
                        JsonObject jobject = jelement.getAsJsonObject();
                        if (jobject.has("result_code")) {
                            /*negative response might comes here*/
                            Log.i("login123", "Login webservice jobject.has(\"result_code\").......");
                            mEmailSignInButton.setVisibility(View.VISIBLE);
                            spinner_signin.setVisibility(View.GONE);
                            Appreference.isLoginRequestSent = false;
                            String result = jobject.get("result_text").toString();
                            Log.i("Responce", "demo" + result);
                            NegativeValue u = g.fromJson(test, NegativeValue.class);
                            Log.i("Value=-->", "" + u);
                            Log.i("Value=-->", "value" + u.getText());
                            Answer = result;
//                            progress.dismiss();
                        } else {
                            Log.i("login123", "Login webservice not jobject.has(\"result_code\").......");
                            /*Positive response might comes here*/

                            Gson g1 = new Gson();
                            String test1 = s1.toString();
                            Type collectionType = new TypeToken<Loginuserdetails>() {
                            }.getType();
                            Loginuserdetails u1 = new Gson().fromJson(s1, collectionType);
//                            Loginuserdetails u1 = g1.fromJson(test1, Loginuserdetails.class);
//                            String loginuserdetails = g1.toJson(test1);
                            Appreference.loginuserdetails = u1;
                            Appreference.isLoginRequestSent = false;
                            try {

                                String saved_username = appSharedpreferences.getString("loginUserName");
                                if (saved_username == null || saved_username.length() == 0) {
                                    need_to_call_listallmytask = "success";
                                    Log.i("Login", "Details first login " + need_to_call_listallmytask);
                                } else if (saved_username != null && saved_username.equalsIgnoreCase(u1.getUsername())) {
                                    need_to_call_listallmytask = "failure";
                                    Log.i("Login", "Details same " + need_to_call_listallmytask);
                                } else {
                                    need_to_call_listallmytask = "success";
                                    Log.i("Login", "Details different " + need_to_call_listallmytask);
                                }

                                appSharedpreferences.saveString("loginuserdetails", test1);
                                appSharedpreferences.saveString("loginId", String.valueOf(u1.getId()));
//                                appSharedpreferences.saveString(u1.getUsername(), u1.getUsername());
                                appSharedpreferences.saveString("loginUserName", u1.getUsername());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.i("User Id", "value" + "\n" + u1.getFirstName());
                            Log.i("u1.getListGroup() Size", "value-->" + u1.getListGroup().size());
                            ArrayList<Usergroubdetails> u2 = u1.getListGroup();
                            for (int i = 0; i < u2.size(); i++) {
                                Usergroubdetails u3 = u2.get(i);
                                Log.i("User group Id", "value-->" + u3.getGroupName());
                            }
                            Username = u1.getUsername();
                            int id = u1.getId();
                            user_id = String.valueOf(id);
                            Answer = "login";
                            SharedPreferences(Username);
                            if (preferences != null && mEmailView.getText().toString() != null && mPasswordView.getText().toString() != null) {
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("uname", mEmailView
                                        .getText().toString().trim());
                                editor.putString("pword", mPasswordView.getText()
                                        .toString().trim());
                                try {
                                    appSharedpreferences.saveString("mEmail", mEmailView.getText().toString().trim());
                                    appSharedpreferences.saveString("mPassword", mPasswordView.getText().toString().trim());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                editor.commit();
                            }

                        }
                    }
//                    else
//                    {
//                        Toast.makeText(getApplicationContext(), "Login Unsuccessfully", Toast.LENGTH_LONG).show();
//                    }
                    if (Answer != null && Answer.equals("login")) {
                        attemptLogin();
                       /* if (progress.isShowing())
                            progress.dismiss();*/
                    } else {
                        Toast.makeText(getApplicationContext(), Answer, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mEmailSignInButton.setVisibility(View.VISIBLE);
                    spinner_signin.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Unable to Login...", Toast.LENGTH_LONG).show();
                   /* if (progress != null && progress.isShowing())
                        progress.dismiss();*/
                }

            }
        });

    }

    @Override
    public void ErrorMethod(Object object) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEmailSignInButton.setVisibility(View.VISIBLE);
                spinner_signin.setVisibility(View.GONE);
            }
        });
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    // store user details
    void SharedPreferences(String u_name) {
        boolean added = false;

        SharedPreferences sharedPref = context.getSharedPreferences(USER_DETAILS, MODE_PRIVATE);
        Map<String, ?> allEntries = sharedPref.getAll();
        if (allEntries != null) {
            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
                User_Details u_D = new User_Details(entry.getKey(), (Integer) entry.getValue());
                list_Of_User.add(u_D);
            }
        }
        for (int i = 0; i < list_Of_User.size(); i++) {
            User_Details u_D = list_Of_User.get(i);
            if (u_D.getUser_Name().equals(u_name)) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(u_name, u_D.getNo_Of_Login() + 1);
                editor.commit();
                added = true;
                break;
            }
        }
        if (!added) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(u_name, no_Of_Login);
            editor.commit();
            appSharedpreferences.saveBoolean("conflictTask", true);
        }
        int login = sharedPref.getInt(u_name, 1);
        Log.e(u_name, String.valueOf(login));
        Appreference.conflicttask = appSharedpreferences.getBoolean("conflictTask");
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }


            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
//                SingleInstance.LoginUser=mEmail;
//                SingleInstance.Password=mPassword;

                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("mEmail", mEmail);
                intent.putExtra("mPassword", mPassword);
                intent.putExtra("mListallmytask", need_to_call_listallmytask);
                Log.i("Login", "Details Intent " + need_to_call_listallmytask);
//                Intent intent = new Intent(context,AppMainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.right_anim, R.anim.left_anim);
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


    //  check internet connection


}

