package com.ase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by prasanth on 9/11/2017.
 */
public class EodScreen extends Activity {
    public Context context;
    ProgressDialog progress;
    static EodScreen eodScreen;
    TaskDetailsBean eodScreenbean;
    String projectId, webtaskId, taskName, JobCodeNo, ActivityCode, strIPath;
    ImageView signature_path, photo_path, tech_signature_path, observation_1, action_taken_1, remarks_complete_1, synopsis_img;
    String status_signature, photo_signature, tech_signature, observation_path, Action_Taken_path, customerRemarks_path, synopsis_path;
    String taskCompletedDate, completedate_display;
    String observationStatus, actiontakenStatus, custsignnameStatus, HMReadingStatus, machine_model, machine_serialno, machine_description, machion_make_edit, synopsis_status;
    boolean isobservationSketchselected, isactionSketchselected, isremarksSketchselected, isobservationtextselected, isactiontextselected, isRemarkstextselected, isSynopsistextselected, isSynopsisSketchselected;
    boolean isCustomerSign, isObservation, isActionTaken, isCustomerRemarks, isSynopsis;
    boolean istaskCompletebyUser = false;
    boolean isForOracleProject = false;

    public static EodScreen getInstance() {
        return eodScreen;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_complete);
        context = this;
        projectId = getIntent().getStringExtra("projectId");
        webtaskId = getIntent().getStringExtra("webtaskId");
        taskName = getIntent().getStringExtra("taskName");
        JobCodeNo = getIntent().getStringExtra("JobCodeNo");
        ActivityCode = getIntent().getStringExtra("ActivityCode");

        TextView back = (TextView) findViewById(R.id.back);

        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String taskcomplete_date = mdformat.format(calendar.getTime());
        Log.i("ws123", "taskcomplete_ddate $$--> " + taskcomplete_date);
        String TraveltimeQuery = "Select * from projectHistory where projectId ='" + projectId + "'";
        final boolean isTravelTaskAvailable = VideoCallDataBase.getDB(context).getTraveltaskExistsOrNot(TraveltimeQuery);
        Log.i("travel123", "gettravel Task available or not========> " + isTravelTaskAvailable);

        TaskDetailsBean detailsBean = new TaskDetailsBean();
        ArrayList<TaskDetailsBean> taskDetailsBean2 = new ArrayList<>();

        TextView project_id = (TextView) findViewById(R.id.project_id);
        TextView project_name = (TextView) findViewById(R.id.project_name);
        TextView task_id = (TextView) findViewById(R.id.task_id);
        TextView est_travel = (TextView) findViewById(R.id.estimatedTravelhrs);
        TextView est_activity = (TextView) findViewById(R.id.estimatedActivityhrs);
        TextView service_date = (TextView) findViewById(R.id.requested_date);
        TextView address = (TextView) findViewById(R.id.address);

        final TextView machine_make_tv = (TextView) findViewById(R.id.machine_make_tv);
        final TextView mac_model_tv = (TextView) findViewById(R.id.mac_model_tv);
        final TextView mac_no_tv = (TextView) findViewById(R.id.mac_no_tv);
        final TextView complete_date_tv = (TextView) findViewById(R.id.complete_date_tv);
        final TextView description_tv = (TextView) findViewById(R.id.description_tv);
        final TextView observation_tv = (TextView) findViewById(R.id.observation_tv);
        final TextView action_taken_tv = (TextView) findViewById(R.id.action_taken_tv);
        final TextView hour_meter_reading_tv = (TextView) findViewById(R.id.hour_meter_reading_tv);
        final TextView remarks_complete_tv = (TextView) findViewById(R.id.remarks_complete_tv);
        final TextView photo_custom1 = (TextView) findViewById(R.id.photo_custom1);
        final TextView tech_sign = (TextView) findViewById(R.id.tech_sign);
        final TextView synopsis_tv = (TextView) findViewById(R.id.synopsis_tv);


        final EditText observation = (EditText) findViewById(R.id.observation);
        Button observation_type = (Button) findViewById(R.id.observation_type);
        observation_1 = (ImageView) findViewById(R.id.observation_1);

        final TextView tx__cust_name = (TextView) findViewById(R.id.tx__cust_name);
        final TextView tx_sign = (TextView) findViewById(R.id.sign);
        if (!isTravelTaskAvailable) {
            tx__cust_name.setText("Customer Sign Name");
            tx_sign.setText("Customer Sign");
        }

        observation_path = "";
        Action_Taken_path = "";
        customerRemarks_path = "";
        synopsis_path = "";
        observationStatus = "";
        actiontakenStatus = "";
        synopsis_status = "";
        observation_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                observation_tv.setTextColor(getResources().getColor(R.color.black));
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(context);
                saveDialog.setTitle("Observation");
                saveDialog.setCancelable(false);
                saveDialog.setMessage("You want to type or draw  sketch in " + taskName);
                saveDialog.setPositiveButton("Text", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isobservationtextselected = true;
                        observation.setCursorVisible(true);
                        observation.setFocusableInTouchMode(true);
                        observation_1.setVisibility(View.GONE);
                        observation.setVisibility(View.VISIBLE);
                        observation_path = "";
                        dialog.cancel();
                    }
                });
                saveDialog.setNeutralButton("Sketch",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    isobservationtextselected = false;
                                    isobservationSketchselected = true;
                                    isObservation = true;
                                    isCustomerRemarks = false;
                                    isActionTaken = false;
                                    isCustomerSign = false;
                                    isSynopsis = false;
                                    isForOracleProject = true;
                                    observation.setVisibility(View.GONE);
                                    observation.getText().clear();
                                    observationStatus = "";
                                    observation_1.setVisibility(View.VISIBLE);
                                    Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                                    //                                            i.putExtra("observation","observation");
                                    i.putExtra("isFromEod", true);
                                    i.putExtra("isFromEodsign", false);
                                    startActivityForResult(i, 423);
                                    dialog.cancel();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("EodScreen", "observation_type Exception : " + e.getMessage(), "WARN", null);
                                }
                            }
                        });
                saveDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                saveDialog.show();

            }
        });

        observation.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (observation.getText().toString() != null && observation.getText().toString().endsWith(".jpg")) {
                    observation.getText().clear();
                    Toast.makeText(getApplicationContext(), "Don't text endswith .jpg", Toast.LENGTH_SHORT).show();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Log.i("oracle123", "onTextEnteredCount===>" + count);

            }
        });
        final EditText action_taken = (EditText) findViewById(R.id.action_taken);
        Button action_taken_type = (Button) findViewById(R.id.action_taken_type);
        action_taken_1 = (ImageView) findViewById(R.id.action_taken_1);

        action_taken_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action_taken_tv.setTextColor(getResources().getColor(R.color.black));
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(context);
                saveDialog.setTitle("Action Taken");
                saveDialog.setCancelable(false);
                saveDialog.setMessage("You want to type or draw via sketch " + taskName);
                saveDialog.setPositiveButton("Text", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isactionSketchselected = false;
                        isactiontextselected = true;
                        action_taken.setCursorVisible(true);
                        action_taken.setFocusableInTouchMode(true);

                        action_taken_1.setVisibility(View.GONE);
                        action_taken.setVisibility(View.VISIBLE);
                        Action_Taken_path = "";
                        dialog.cancel();
                    }
                });
                saveDialog.setNeutralButton("Sketch",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    isactionSketchselected = true;
                                    isactiontextselected = false;
                                    isActionTaken = true;
                                    isCustomerRemarks = false;
                                    isObservation = false;
                                    isCustomerSign = false;
                                    isSynopsis = false;
                                    isForOracleProject = true;
                                    action_taken.getText().clear();
                                    actiontakenStatus = "";
                                    action_taken.setVisibility(View.GONE);
                                    action_taken_1.setVisibility(View.VISIBLE);
                                    Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                                    i.putExtra("isFromEod", true);
                                    i.putExtra("isFromEodsign", false);
                                    startActivityForResult(i, 423);
                                    dialog.cancel();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("EodScreen", "action_taken_type Exception : " + e.getMessage(), "WARN", null);
                                }
                            }
                        });
                saveDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                saveDialog.show();

            }
        });
        action_taken.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (action_taken.getText().toString() != null && action_taken.getText().toString().endsWith(".jpg")) {
                    action_taken.getText().clear();
                    Toast.makeText(getApplicationContext(), "Don't text endswith .jpg", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final EditText remarks_completion = (EditText) findViewById(R.id.remarks_complete);
        Button CustomerRemarks_type = (Button) findViewById(R.id.CustomerRemarks_type);
        remarks_complete_1 = (ImageView) findViewById(R.id.remarks_complete_1);
        CustomerRemarks_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remarks_complete_tv.setTextColor(getResources().getColor(R.color.black));
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(context);
                saveDialog.setTitle("Customer Remarks");
                saveDialog.setCancelable(false);
                saveDialog.setMessage("You want to type or draw via sketch " + taskName);
                saveDialog.setPositiveButton("Text", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isremarksSketchselected = false;
                        isRemarkstextselected = true;
                        remarks_completion.setCursorVisible(true);
                        remarks_completion.setFocusableInTouchMode(true);
                        remarks_complete_1.setVisibility(View.GONE);
                        remarks_completion.setVisibility(View.VISIBLE);
                        customerRemarks_path = "";
                        dialog.cancel();
                    }
                });
                saveDialog.setNeutralButton("Sketch",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    isremarksSketchselected = true;
                                    isRemarkstextselected = false;
                                    isCustomerRemarks = true;
                                    isActionTaken = false;
                                    isObservation = false;
                                    isCustomerSign = false;
                                    isSynopsis = false;
                                    isForOracleProject = true;
                                    remarks_completion.getText().clear();
                                    remarks_completion.setVisibility(View.GONE);
                                    remarks_complete_1.setVisibility(View.VISIBLE);
                                    Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                                    i.putExtra("isFromEod", true);
                                    i.putExtra("isFromEodsign", false);
                                    startActivityForResult(i, 423);
                                    dialog.cancel();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("EodScreen", "CustomerRemarks_type Exception : " + e.getMessage(), "WARN", null);
                                }
                            }
                        });
                saveDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                saveDialog.show();

            }
        });

        remarks_completion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (remarks_completion.getText().toString() != null && remarks_completion.getText().toString().endsWith(".jpg")) {
                    remarks_completion.getText().clear();
                    Toast.makeText(getApplicationContext(), "Don't text endswith .jpg ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        final EditText synopsis_text = (EditText) findViewById(R.id.synopsis_text);
        final Button synopsis_type = (Button) findViewById(R.id.synopsis_type);
        synopsis_img = (ImageView) findViewById(R.id.synopsis_img);
        synopsis_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synopsis_tv.setTextColor(getResources().getColor(R.color.black));
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(context);
                saveDialog.setTitle("Synopsis");
                saveDialog.setCancelable(false);
                saveDialog.setMessage("You want to type or draw  sketch in " + taskName);
                saveDialog.setPositiveButton("Text", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        isSynopsistextselected = true;
                        synopsis_text.setCursorVisible(true);
                        synopsis_text.setFocusableInTouchMode(true);
                        synopsis_img.setVisibility(View.GONE);
                        synopsis_text.setVisibility(View.VISIBLE);
                        synopsis_path = "";
                        dialog.cancel();
                    }
                });
                saveDialog.setNeutralButton("Sketch",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    isSynopsistextselected = false;
                                    isSynopsisSketchselected = true;
                                    isSynopsis = true;
                                    isObservation = false;
                                    isCustomerRemarks = false;
                                    isActionTaken = false;
                                    isCustomerSign = false;
                                    isForOracleProject = true;
                                    synopsis_text.setVisibility(View.GONE);
                                    synopsis_text.getText().clear();
                                    synopsis_status = "";
                                    synopsis_img.setVisibility(View.VISIBLE);
                                    Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                                    //                                            i.putExtra("observation","observation");
                                    i.putExtra("isFromEod", true);
                                    i.putExtra("isFromEodsign", false);
                                    startActivityForResult(i, 423);
                                    dialog.cancel();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("EodScreen", "synopsis_type Exception : " + e.getMessage(), "WARN", null);
                                }
                            }
                        });
                saveDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                saveDialog.show();

            }
        });

        synopsis_text.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (synopsis_text.getText().toString() != null && synopsis_text.getText().toString().endsWith(".jpg")) {
                    synopsis_text.getText().clear();
                    Toast.makeText(getApplicationContext(), "Don't text endswith .jpg", Toast.LENGTH_SHORT).show();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Log.i("oracle123", "onTextEnteredCount===>" + count);

            }
        });

        final EditText cust_sign_name = (EditText) findViewById(R.id.cust_sign_name);
        final EditText HMReading = (EditText) findViewById(R.id.hour_meter_reading);
        final TextView task_completed_date = (TextView) findViewById(R.id.task_completed_date);
        TextView proj_activity = (TextView) findViewById(R.id.proj_activity);
        final TextView travel_start = (TextView) findViewById(R.id.travel_start);
        final TextView travel_end = (TextView) findViewById(R.id.travel_end);
        final TextView activity_start = (TextView) findViewById(R.id.activity_start);
        final TextView activity_end = (TextView) findViewById(R.id.activity_end);

        final EditText mcModel = (EditText) findViewById(R.id.mac_model);
        final EditText mcSrNo = (EditText) findViewById(R.id.mac_no);
        final EditText description = (EditText) findViewById(R.id.description);
        final EditText machine_make = (EditText) findViewById(R.id.machine_make);

        Button travelstart_sign = (Button) findViewById(R.id.travelstart_sign);
        Button activitystart_sign = (Button) findViewById(R.id.activitystart_sign);
        Button activityend_sign = (Button) findViewById(R.id.activityend_sign);
        Button complete_date_btn = (Button) findViewById(R.id.complete_date_btn);


        photo_path = (ImageView) findViewById(R.id.photo_path);
        signature_path = (ImageView) findViewById(R.id.signature_path);
        tech_signature_path = (ImageView) findViewById(R.id.tech_signature_path);

        ImageView send_completion = (ImageView) findViewById(R.id.send_completion);
        Button skech_receiver = (Button) findViewById(R.id.my_sign);
        Button photo_receiver = (Button) findViewById(R.id.my_photo);
        Button tech_sign_receiver = (Button) findViewById(R.id.tech_sign_btn);
        RadioGroup rg_task_complete_confirm = (RadioGroup) findViewById(R.id.radioyesNo);
        final LinearLayout ll_synopsis_layout = (LinearLayout) findViewById(R.id.synopsis_layout);

        machine_make.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                machine_make_tv.setTextColor(getResources().getColor(R.color.black));
                Log.i("machine_make_tv", "machine_make_tv===> ");
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                Log.i("machine_make_tv", "beforeTextChanged===> " + count);
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Log.i("machine_make_tv", "onTextChanged===> " + count);

            }
        });
        mcModel.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                mac_model_tv.setTextColor(getResources().getColor(R.color.black));
                Log.i("machine_make_tv", "machine_make_tv===> ");
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                Log.i("machine_make_tv", "beforeTextChanged===> " + count);
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Log.i("machine_make_tv", "onTextChanged===> " + count);

            }
        });
        mcSrNo.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                mac_no_tv.setTextColor(getResources().getColor(R.color.black));
                Log.i("machine_make_tv", "machine_make_tv===> ");
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                Log.i("machine_make_tv", "beforeTextChanged===> " + count);
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Log.i("machine_make_tv", "onTextChanged===> " + count);

            }
        });
        description.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                description_tv.setTextColor(getResources().getColor(R.color.black));
                Log.i("machine_make_tv", "machine_make_tv===> ");
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                Log.i("machine_make_tv", "beforeTextChanged===> " + count);
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Log.i("machine_make_tv", "onTextChanged===> " + count);

            }
        });
        cust_sign_name.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                tx__cust_name.setTextColor(getResources().getColor(R.color.black));
                Log.i("machine_make_tv", "machine_make_tv===> ");
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                Log.i("machine_make_tv", "beforeTextChanged===> " + count);
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Log.i("machine_make_tv", "onTextChanged===> " + count);

            }
        });
        HMReading.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                hour_meter_reading_tv.setTextColor(getResources().getColor(R.color.black));
                Log.i("machine_make_tv", "machine_make_tv===> ");
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                Log.i("machine_make_tv", "beforeTextChanged===> " + count);
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Log.i("machine_make_tv", "onTextChanged===> " + count);

            }
        });
        rg_task_complete_confirm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                synopsis_tv.setTextColor(getResources().getColor(R.color.black));
                if (group.getCheckedRadioButtonId() != -1) {
                    View radioButton = group.findViewById(group.getCheckedRadioButtonId());
                    int radioId = group.indexOfChild(radioButton);
                    Log.i("travel123", "radioId========> " + radioId);

                    RadioButton btn = (RadioButton) group.getChildAt(radioId);
                    String radio_selected_text = (String) btn.getText();
                    if (radio_selected_text.equalsIgnoreCase("yes")) {
                        ll_synopsis_layout.setVisibility(View.GONE);
                        istaskCompletebyUser = true;
                    } else {
                        ll_synopsis_layout.setVisibility(View.VISIBLE);
                        istaskCompletebyUser = false;
                    }
                }
            }
        });

        activitystart_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(getApplicationContext(), DisplayList.class);
                    i.putExtra("projectId", projectId);
                    i.putExtra("webtaskId", webtaskId);
                    i.putExtra("completedate_display", completedate_display);
                    i.putExtra("isFromcustom1", false);
                    i.putExtra("date_type", "travel_start");
                    startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "activitystart_sign Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });

        task_completed_date.setText(mdformat.format(calendar.getTime()));
        taskCompletedDate = task_completed_date.getText().toString() + " " + "00:00:00";
        Log.i("oracle123", "getCompleted default====>" + taskCompletedDate);
        completedate_display = mdformat.format(calendar.getTime());

        final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        final SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");

        complete_date_btn.setOnClickListener(new View.OnClickListener() {
            final Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {

                DatePickerDialog dpd = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String months = "";
                                if ((monthOfYear + 1) < 10) {
                                    months = "0" + (monthOfYear + 1);
                                } else {
                                    months = String.valueOf(monthOfYear + 1);
                                }
                                String days = "";
                                if (dayOfMonth < 10) {
                                    days = "0" + dayOfMonth;
                                } else {
                                    days = String.valueOf(dayOfMonth);
                                }
                                completedate_display = year + "-" + months + "-" + days;

                                String selected_date = completedate_display;

                                String curr_date = null;
                                try {
                                    SimpleDateFormat simpleDateFormat_1 = new SimpleDateFormat("yyyy-MM-dd");
                                    curr_date = simpleDateFormat_1.format(new Date());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("EodScreen", "complete_date_btn clicklistener Exception : " + e.getMessage(), "WARN", null);
                                }
                                Log.i("oracle123", "curr_date-==>" + curr_date);
                                Log.i("oracle123", "selected_date-==>" + selected_date);

                                if ((curr_date != null && curr_date.compareTo(selected_date) > 0) || (curr_date != null && curr_date.compareTo(selected_date) == 0)) {
                                    task_completed_date.setText(completedate_display);
                                    taskCompletedDate = completedate_display + " " + "00:00:00";

                                } else {
                                    Toast.makeText(getApplicationContext(), "please Select date till today", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                dpd.show();
            }
        });
        String Query = "Select * from projectHistory where projectId ='" + projectId + "' and taskId = '" + webtaskId + "'";
        detailsBean = VideoCallDataBase.getDB(context).getDetails_to_complete_project(Query);
        String getOpen_date_query="select openDate from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'and projectId='" + projectId + "'";
        String My_open_date = VideoCallDataBase.getDB(context).getValuesForQuery(getOpen_date_query);
        project_id.setText(detailsBean.getProjectId());
        project_name.setText("Job Card No :" + JobCodeNo + "\nActivity Code :" + ActivityCode);
        task_id.setText(ActivityCode);
        Log.i("ws123", "username or employee name===>" + Appreference.loginuserdetails.getEmail());
        try {
            if (detailsBean.getMcModel()!=null && !detailsBean.getMcModel().equalsIgnoreCase("") && !detailsBean.getMcModel().equalsIgnoreCase(null)) {
                mcModel.append(detailsBean.getMcModel());
            }
            if (detailsBean.getMcModel()!=null && !detailsBean.getMcModel().equalsIgnoreCase("") && !detailsBean.getMcModel().equalsIgnoreCase(null)) {
                mcSrNo.append(detailsBean.getMcSrNo());
            }
            if (detailsBean.getMcModel()!=null && !detailsBean.getMcModel().equalsIgnoreCase("") && !detailsBean.getMcModel().equalsIgnoreCase(null)) {
                machine_make.append(detailsBean.getMachineMake());
            }
            if (detailsBean.getMcModel()!=null && !detailsBean.getMcModel().equalsIgnoreCase("") && !detailsBean.getMcModel().equalsIgnoreCase(null)) {
                description.append(detailsBean.getMcDescription());
            }
            if (detailsBean.getMcModel()!=null && !detailsBean.getMcModel().equalsIgnoreCase("") && !detailsBean.getMcModel().equalsIgnoreCase(null)) {
                est_travel.setText(detailsBean.getEstimatedTravel());
            }
            if (detailsBean.getMcModel()!=null && !detailsBean.getMcModel().equalsIgnoreCase("") && !detailsBean.getMcModel().equalsIgnoreCase(null)) {
                est_activity.setText(detailsBean.getEstimatedTravel());
            }
            if (detailsBean.getMcModel()!=null && !detailsBean.getMcModel().equalsIgnoreCase("") && !detailsBean.getMcModel().equalsIgnoreCase(null)) {
//                service_date.setText(detailsBean.getDateTime());
                /*changing openDate format  dd-MM-yyyy to  yyyy-MM-dd */
                String JobOpenDate = "";
                if (My_open_date != null) {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date openDate;
                    try {
                        openDate = inputFormat.parse(My_open_date);
                        JobOpenDate = outputFormat.format(openDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                service_date.setText(JobOpenDate);
            }
            if (detailsBean.getMcModel()!=null && !detailsBean.getMcModel().equalsIgnoreCase("") && !detailsBean.getMcModel().equalsIgnoreCase(null)) {
                proj_activity.setText(detailsBean.getActivity());
            }
            if (detailsBean.getMcModel()!=null && !detailsBean.getMcModel().equalsIgnoreCase("") && !detailsBean.getMcModel().equalsIgnoreCase(null)) {
                address.setText(detailsBean.getAddress());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("EodScreen", "settext Exception : " + e.getMessage(), "WARN", null);
        }
        signature_path.setVisibility(View.GONE);
        photo_path.setVisibility(View.GONE);
        tech_signature_path.setVisibility(View.GONE);

        skech_receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tx_sign.setTextColor(getResources().getColor(R.color.black));
                    isCustomerSign = true;
                    isObservation = false;
                    isCustomerRemarks = false;
                    isActionTaken = false;
                    isSynopsis = false;
                    isForOracleProject = true;
                    Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                    i.putExtra("isFromEod", false);
                    i.putExtra("isFromEodsign", true);
                    startActivityForResult(i, 423);
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "skech_receiver clicklistener Exception : " + e.getMessage(), "WARN", null);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "skech_receiver clicklistener Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });
        tech_sign_receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tech_sign.setTextColor(getResources().getColor(R.color.black));
                    isCustomerSign = false;
                    isObservation = false;
                    isCustomerRemarks = false;
                    isActionTaken = false;
                    isSynopsis = false;
                    isForOracleProject = true;
                    Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                    i.putExtra("isFromEod", false);
                    i.putExtra("isFromEodsign", true);
                    startActivityForResult(i, 423);
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "tech_sign_receiver clicklistener Exception : " + e.getMessage(), "WARN", null);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "tech_sign_receiver clicklistener Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });
        photo_receiver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photo_custom1.setTextColor(getResources().getColor(R.color.black));
                try {
                    isForOracleProject = true;
                    final String path = Environment.getExternalStorageDirectory() + "/High Message/";
                    File directory = new File(path);
                    if (!directory.exists())
                        directory.mkdir();
                    strIPath = path + getFileName() + ".jpg";
                    Intent intent = new Intent(context, CustomVideoCamera.class);
                    Uri imageUri = Uri.fromFile(new File(strIPath));
                    intent.putExtra("filePath", strIPath);
                    intent.putExtra("isPhoto", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 132);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "photo_receiver clicklistener Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });

        observation_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ImageName = observation_path;
                    File file = null;
                    if (ImageName != null && !ImageName.equalsIgnoreCase("")) {
                        file = new File(ImageName);
                        if (file.exists()) {
                            Intent intent = new Intent(context, FullScreenImage.class);
                            intent.putExtra("image", file.toString());
                            context.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                            if (file1.exists()) {
                                Intent intent = new Intent(context, FullScreenImage.class);
                                intent.putExtra("image", file1.toString());
                                context.startActivity(intent);
                            }
                        }
                    } else
                        Toast.makeText(EodScreen.this, "Please Set any Image to View", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "observation_1 clicklistener Exception : " + e.getMessage(), "WARN", null);
                }

            }
        });


        action_taken_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ImageName = Action_Taken_path;
                    File file = null;
                    if (ImageName != null && !ImageName.equalsIgnoreCase("")) {
                        file = new File(ImageName);
                        if (file.exists()) {
                            Intent intent = new Intent(context, FullScreenImage.class);
                            intent.putExtra("image", file.toString());
                            context.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                            if (file1.exists()) {
                                Intent intent = new Intent(context, FullScreenImage.class);
                                intent.putExtra("image", file1.toString());
                                context.startActivity(intent);
                            }
                        }
                    } else
                        Toast.makeText(EodScreen.this, "Please Set any Image to View", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "action_taken_1 clicklistener Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });

        remarks_complete_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ImageName = customerRemarks_path;
                    File file = null;
                    if (ImageName != null && !ImageName.equalsIgnoreCase("")) {
                        file = new File(ImageName);
                        if (file.exists()) {
                            Intent intent = new Intent(context, FullScreenImage.class);
                            intent.putExtra("image", file.toString());
                            context.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                            if (file1.exists()) {
                                Intent intent = new Intent(context, FullScreenImage.class);
                                intent.putExtra("image", file1.toString());
                                context.startActivity(intent);
                            }
                        }
                    } else
                        Toast.makeText(EodScreen.this, "Please Set any Image to View", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "remarks_complete_1 clicklistener Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });

        signature_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ImageName = status_signature;
                    File file = null;
                    if (ImageName != null && !ImageName.equalsIgnoreCase("")) {
                        file = new File(ImageName);
                        if (file.exists()) {
                            Intent intent = new Intent(context, FullScreenImage.class);
                            intent.putExtra("image", file.toString());
                            context.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                            if (file1.exists()) {
                                Intent intent = new Intent(context, FullScreenImage.class);
                                intent.putExtra("image", file1.toString());
                                context.startActivity(intent);
                            }
                        }
                    } else
                        Toast.makeText(EodScreen.this, "Please Set any Image to View", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "signature_path clicklistener Exception : " + e.getMessage(), "WARN", null);
                }


            }
        });
        tech_signature_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ImageName = tech_signature;
                    File file = null;
                    if (ImageName != null && !ImageName.equalsIgnoreCase("")) {
                        file = new File(ImageName);
                        if (file.exists()) {
                            Intent intent = new Intent(context, FullScreenImage.class);
                            intent.putExtra("image", file.toString());
                            context.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                            if (file1.exists()) {
                                Intent intent = new Intent(context, FullScreenImage.class);
                                intent.putExtra("image", file1.toString());
                                context.startActivity(intent);
                            }
                        }
                    } else
                        Toast.makeText(EodScreen.this, "Please Set any Image to View", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "tech_signature_path clicklistener Exception : " + e.getMessage(), "WARN", null);
                }

            }
        });
        photo_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ImageName = photo_signature;
                    File file = null;
                    if (ImageName != null && !ImageName.equalsIgnoreCase("")) {
                        file = new File(ImageName);
                        if (file.exists()) {
                            Intent intent = new Intent(context, FullScreenImage.class);
                            intent.putExtra("image", file.toString());
                            context.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                            if (file1.exists()) {
                                Intent intent = new Intent(context, FullScreenImage.class);
                                intent.putExtra("image", file1.toString());
                                context.startActivity(intent);
                            }
                        }
                    } else
                        Toast.makeText(EodScreen.this, "Please Set any Image to View", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "photo_path clicklistener Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });

        synopsis_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ImageName = synopsis_path;
                    File file = null;
                    if (ImageName != null && !ImageName.equalsIgnoreCase("")) {
                        file = new File(ImageName);
                        if (file.exists()) {
                            Intent intent = new Intent(context, FullScreenImage.class);
                            intent.putExtra("image", file.toString());
                            context.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                            if (file1.exists()) {
                                Intent intent = new Intent(context, FullScreenImage.class);
                                intent.putExtra("image", file1.toString());
                                context.startActivity(intent);
                            }
                        }
                    } else
                        Toast.makeText(EodScreen.this, "Please Set any Image to View", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "synopsis_img clicklistener Exception : " + e.getMessage(), "WARN", null);
                }

            }
        });

        send_completion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String customer_remarksEntry = "";
                    if (isRemarkstextselected) {
                        Log.i("oracle123", "isRemarkstextselected====> " + isRemarkstextselected);
                        customer_remarksEntry = remarks_completion.getText().toString();
                    }
                    if (isSynopsistextselected)
                        synopsis_status = synopsis_text.getText().toString();
                    else
                        synopsis_status = "";

                    if (isobservationtextselected)
                        observationStatus = observation.getText().toString();
                    else
                        observationStatus = "";
                    if (isactiontextselected)
                        actiontakenStatus = action_taken.getText().toString();
                    else
                        actiontakenStatus = "";
                    if (cust_sign_name.getText().toString() != null)
                        custsignnameStatus = cust_sign_name.getText().toString();
                    else
                        custsignnameStatus = "";
                    if (HMReading.getText().toString() != null)
                        HMReadingStatus = HMReading.getText().toString();
                    else
                        HMReadingStatus = "";
                    if (mcModel.getText().toString() != null)
                        machine_model = mcModel.getText().toString();
                    else
                        machine_model = "";
                    if (mcSrNo.getText().toString() != null)
                        machine_serialno = mcSrNo.getText().toString();
                    else
                        machine_serialno = "";
                    if (description.getText().toString() != null)
                        machine_description = description.getText().toString();
                    else
                        machine_description = "";
                    if (machine_make.getText().toString() != null)
                        machion_make_edit = machine_make.getText().toString();
                    else
                        machion_make_edit = "";
                    Log.i("desc123", "machine_model @@========>" + machine_model);
                    Log.i("desc123", "machine_serialno @@========>" + machine_serialno);
                    Log.i("desc123", "machine_description @@========>" + machine_description);

                    String query_status = "select status from projectStatus where projectId='" + projectId + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + webtaskId + "'";
                    Log.i("EOD123", "tech_signature ==> " + tech_signature);
                    Log.i("EOD123", "status_signature ==> " + status_signature);
                    Log.i("EOD123", "photo_signature ==> " + photo_signature);
                    if ((taskCompletedDate != null && !taskCompletedDate.equalsIgnoreCase("") && !taskCompletedDate.equalsIgnoreCase(null))
                            && (HMReadingStatus != null && !HMReadingStatus.equalsIgnoreCase("") && !HMReadingStatus.equalsIgnoreCase(null))
                            && (machion_make_edit != null && !machion_make_edit.equalsIgnoreCase("") && !machion_make_edit.equalsIgnoreCase(null))
                            && (machine_model != null && !machine_model.equalsIgnoreCase("") && !machine_model.equalsIgnoreCase(null))
                            && (machine_serialno != null && !machine_serialno.equalsIgnoreCase("") && !machine_serialno.equalsIgnoreCase(null))
                            && (machine_description != null && !machine_description.equalsIgnoreCase("") && !machine_description.equalsIgnoreCase(null))
                            && ((isTravelTaskAvailable && (custsignnameStatus != null && !custsignnameStatus.equalsIgnoreCase("") && !custsignnameStatus.equalsIgnoreCase(null)
                            && (status_signature != null && !status_signature.equalsIgnoreCase("") && !status_signature.equalsIgnoreCase(null)))) || !isTravelTaskAvailable)
                            && (photo_signature != null && !photo_signature.equalsIgnoreCase("") && !photo_signature.equalsIgnoreCase(null))
                            && (tech_signature != null && !tech_signature.equalsIgnoreCase("") && !tech_signature.equalsIgnoreCase(null))
                            && ((isRemarkstextselected && !customer_remarksEntry.equalsIgnoreCase("") && customer_remarksEntry != null && !customer_remarksEntry.equalsIgnoreCase(null))
                            || (isremarksSketchselected && !customerRemarks_path.equalsIgnoreCase("") && customerRemarks_path != null && !customerRemarks_path.equalsIgnoreCase(null)))
                            && ((isobservationtextselected && !observationStatus.equalsIgnoreCase("") && observationStatus != null && !observationStatus.equalsIgnoreCase(null))
                            || (isobservationSketchselected && !observation_path.equalsIgnoreCase("") && observation_path != null && !observation_path.equalsIgnoreCase(null)))
                            && ((isactiontextselected && !actiontakenStatus.equalsIgnoreCase("") && actiontakenStatus != null && !actiontakenStatus.equalsIgnoreCase(null))
                            || (isactionSketchselected && !Action_Taken_path.equalsIgnoreCase("") && Action_Taken_path != null && !Action_Taken_path.equalsIgnoreCase(null)))
                            && ((!istaskCompletebyUser && (isSynopsistextselected && !synopsis_status.equalsIgnoreCase("") && synopsis_status != null && !synopsis_status.equalsIgnoreCase(null))
                            || (isSynopsisSketchselected && !synopsis_path.equalsIgnoreCase("") && synopsis_path != null && !synopsis_path.equalsIgnoreCase(null))) || istaskCompletebyUser)) {

                        Log.i("EOD", "customer_remarksEntry ==> " + customer_remarksEntry);
                        eodScreenbean = new TaskDetailsBean();
                        eodScreenbean.setTaskCompletedDate(taskCompletedDate);
                        eodScreenbean.setMachineMake(machion_make_edit);
                        eodScreenbean.setMcModel(machine_model);
                        eodScreenbean.setMcSrNo(machine_serialno);
                        eodScreenbean.setMcDescription(machine_description);
                        eodScreenbean.setHMReading(HMReadingStatus);
                        eodScreenbean.setCustomerSignatureName(custsignnameStatus);
                        eodScreenbean.setCustomerSignature(status_signature);
                        eodScreenbean.setTechnicianSignature(tech_signature);
                        eodScreenbean.setPhotoPath(photo_signature);
                        if (isobservationtextselected) {
                            eodScreenbean.setObservation(observationStatus);
                        } else {
                            eodScreenbean.setObservation(observation_path);
                        }
                        if (isactiontextselected) {
                            eodScreenbean.setActionTaken(actiontakenStatus);
                        } else {
                            eodScreenbean.setActionTaken(Action_Taken_path);
                        }
                        if (isRemarkstextselected) {
                            eodScreenbean.setCustomerRemarks(customer_remarksEntry);
                        } else {
                            eodScreenbean.setCustomerRemarks(customerRemarks_path);
                        }
                        if (isSynopsistextselected) {
                            eodScreenbean.setSynopsis(synopsis_status);
                        } else if (isSynopsisSketchselected) {
                            eodScreenbean.setSynopsis(synopsis_path);
                        }
                        Intent i = new Intent();
                        Log.i("EOD_submit", "getCustomerRemarks===> " + eodScreenbean.getCustomerRemarks());
                        Log.i("EOD_submit", "getObservation===> " + eodScreenbean.getObservation());
                        Log.i("EOD_submit", "getCustomerSignature===> " + eodScreenbean.getCustomerSignature());
                        Log.i("EOD_submit", "getPhotoPath===> " + eodScreenbean.getPhotoPath());
                        i.putExtra("eodBean", eodScreenbean);
                        setResult(RESULT_OK, i);
                        finish();
                    } else {
                        Log.i("EOD_submit", "HMReadingStatus===> " + HMReadingStatus);
                        Log.i("EOD_submit", "photo_signature===> " + photo_signature);
                        Log.i("EOD_submit", "tech_signature===> " + tech_signature);

                        Toast.makeText(EodScreen.this, "Please fill the field(s) marked in RED", Toast.LENGTH_SHORT).show();

                        if (machion_make_edit == null || machion_make_edit.equalsIgnoreCase("") || machion_make_edit.equalsIgnoreCase(null)) {
                            machine_make_tv.setTextColor(getResources().getColor(R.color.red));
                        }
                        if (machine_model == null || machine_model.equalsIgnoreCase("") || machine_model.equalsIgnoreCase(null)) {
                            mac_model_tv.setTextColor(getResources().getColor(R.color.red));
                        }
                        if (machine_serialno == null || machine_serialno.equalsIgnoreCase("") || machine_serialno.equalsIgnoreCase(null)) {
                            mac_no_tv.setTextColor(getResources().getColor(R.color.red));
                        }
                        if (taskCompletedDate == null || taskCompletedDate.equalsIgnoreCase("") || taskCompletedDate.equalsIgnoreCase(null)) {
                            complete_date_tv.setTextColor(getResources().getColor(R.color.red));
                        }
                        if (machine_description == null || machine_description.equalsIgnoreCase("") || machine_description.equalsIgnoreCase(null)) {
                            description_tv.setTextColor(getResources().getColor(R.color.red));
                        }
                        if ((isobservationtextselected && !observationStatus.equalsIgnoreCase("") && observationStatus != null && !observationStatus.equalsIgnoreCase(null))
                                || (isobservationSketchselected && !observation_path.equalsIgnoreCase("") && observation_path != null && !observation_path.equalsIgnoreCase(null))) {
                        } else {
                            observation_tv.setTextColor(getResources().getColor(R.color.red));
                        }
                        if ((isactiontextselected && !actiontakenStatus.equalsIgnoreCase("") && actiontakenStatus != null && !actiontakenStatus.equalsIgnoreCase(null))
                                || (isactionSketchselected && !Action_Taken_path.equalsIgnoreCase("") && Action_Taken_path != null && !Action_Taken_path.equalsIgnoreCase(null))) {
                        } else {
                            action_taken_tv.setTextColor(getResources().getColor(R.color.red));
                        }
                        if (HMReadingStatus == null || HMReadingStatus.equalsIgnoreCase("") || HMReadingStatus.equalsIgnoreCase(null)) {
                            hour_meter_reading_tv.setTextColor(getResources().getColor(R.color.red));
                        }
                        if ((isRemarkstextselected && !customer_remarksEntry.equalsIgnoreCase("") && customer_remarksEntry != null && !customer_remarksEntry.equalsIgnoreCase(null))
                                || (isremarksSketchselected && !customerRemarks_path.equalsIgnoreCase("") && customerRemarks_path != null && !customerRemarks_path.equalsIgnoreCase(null))) {
                        } else {
                            remarks_complete_tv.setTextColor(getResources().getColor(R.color.red));
                        }
                        if ((isTravelTaskAvailable && custsignnameStatus != null && !custsignnameStatus.equalsIgnoreCase("") && !custsignnameStatus.equalsIgnoreCase(null)) || !isTravelTaskAvailable) {
                        } else {
                            tx__cust_name.setTextColor(getResources().getColor(R.color.red));
                        }
                        if ((isTravelTaskAvailable && status_signature != null && !status_signature.equalsIgnoreCase("") && !status_signature.equalsIgnoreCase(null)) || !isTravelTaskAvailable) {
                        } else {
                            tx_sign.setTextColor(getResources().getColor(R.color.red));
                        }
                        if (photo_signature == null || photo_signature.equalsIgnoreCase("") || photo_signature.equalsIgnoreCase(null)) {
                            photo_custom1.setTextColor(getResources().getColor(R.color.red));
                        }
                        if (tech_signature == null || tech_signature.equalsIgnoreCase("") || tech_signature.equalsIgnoreCase(null)) {
                            tech_sign.setTextColor(getResources().getColor(R.color.red));
                        }
                        if ((!istaskCompletebyUser && (isSynopsistextselected && !synopsis_status.equalsIgnoreCase("") && synopsis_status != null && !synopsis_status.equalsIgnoreCase(null))
                                || (isSynopsisSketchselected && !synopsis_path.equalsIgnoreCase("") && synopsis_path != null && !synopsis_path.equalsIgnoreCase(null))) || istaskCompletebyUser) {
                        } else {
                            synopsis_tv.setTextColor(getResources().getColor(R.color.red));
                        }

                    }
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "EOD send_completion clicklistener Exception : " + e.getMessage(), "WARN", null);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "EOD send_completion clicklistener Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_dialog();
            }
        });
    }

    public String getFileName() {
        String strFilename = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
            Date date = new Date();
            strFilename = dateFormat.format(date);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("EodScreen", "getFileName Exception " + e.getMessage(), "WARN", null);
        }
        return strFilename;
    }

    public void alert_dialog() {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(EodScreen.this).create();
            alertDialog.setTitle("JobCode Completion");
            alertDialog.setCancelable(false);
            alertDialog.setMessage("Are you sure want to go back?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("EodScreen", "alert_dialog Exception : " + e.getMessage(), "WARN", null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 423) {
                try {
                    strIPath = data.getStringExtra("path");
                    Log.i("result_handsketch", "strIPath==> " + strIPath);
                    Log.i("result_handsketch", "isCustomerSign==> ## " + isCustomerSign);
                    Log.i("result_handsketch", "isObservation==> ## " + isObservation);
                    if (isCustomerSign) {
                        status_signature = strIPath;
                        if (signature_path != null) {
                            File imgFile = new File(status_signature);
                            if (imgFile.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                signature_path.setImageBitmap(myBitmap);
                            }
                            signature_path.setVisibility(View.VISIBLE);
                        }
                    } else if (isObservation) {
                        Log.i("result_handsketch", "isObservation==> " + strIPath);
                        observation_path = strIPath;
                        if (observation_1 != null) {
                            File imgFile = new File(observation_path);
                            if (imgFile.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                observation_1.setImageBitmap(myBitmap);
                            }
                            observation_1.setVisibility(View.VISIBLE);
                            Log.i("result_handsketch", "observation_1==>$$ !! " + strIPath);
                        }
                    } else if (isActionTaken) {
                        Log.i("result_handsketch", "isActionTaken==> " + strIPath);
                        Action_Taken_path = strIPath;
                        if (action_taken_1 != null) {
                            File imgFile = new File(Action_Taken_path);
                            if (imgFile.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                action_taken_1.setImageBitmap(myBitmap);
                            }
                            action_taken_1.setVisibility(View.VISIBLE);
                            Log.i("result_handsketch", "action_taken_1==>$$ !! " + strIPath);
                        }
                    } else if (isCustomerRemarks) {
                        Log.i("result_handsketch", "isCustomerRemarks==> " + strIPath);
                        customerRemarks_path = strIPath;
                        if (remarks_complete_1 != null) {
                            File imgFile = new File(customerRemarks_path);
                            if (imgFile.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                remarks_complete_1.setImageBitmap(myBitmap);
                            }
                            remarks_complete_1.setVisibility(View.VISIBLE);
                            Log.i("result_handsketch", "remarks_complete_1==>$$ !! " + strIPath);
                        }
                    } else if (isSynopsis) {
                        Log.i("result_handsketch", "isSynopsis==> " + strIPath);
                        synopsis_path = strIPath;
                        if (synopsis_img != null) {
                            File imgFile = new File(synopsis_path);
                            if (imgFile.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                synopsis_img.setImageBitmap(myBitmap);
                            }
                            synopsis_img.setVisibility(View.VISIBLE);
                            Log.i("result_handsketch", "synopsis_path==>$$ !! " + synopsis_path);
                        }
                    } else {
                        tech_signature = strIPath;
                        if (tech_signature_path != null) {
                            File imgFile = new File(tech_signature);
                            if (imgFile.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                tech_signature_path.setImageBitmap(myBitmap);
                            }
                            tech_signature_path.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "onActivityResult 423 sketch Exception " + e.getMessage(), "WARN", null);
                }
            } else if (requestCode == 132) {
                try {
                    if (photo_path != null) {
                        photo_signature = strIPath;
                        if (photo_path != null) {
                            File imgFile = new File(photo_signature);
                            if (imgFile.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                photo_path.setImageBitmap(myBitmap);
                            }
                            photo_path.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "onActivityResult 132 image Exception " + e.getMessage(), "WARN", null);
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            Log.i("EodScreen", "onKeyDown ");
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                Log.i("EodScreen", "onKeyDown");
                alert_dialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("EodScreen", "onKeyDown Exception : " + e.getMessage(), "WARN", null);
        }
        return super.onKeyDown(keyCode, event);
    }
}
