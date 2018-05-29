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
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.Bean.Label;
import com.ase.Bean.checkListDetails;
import com.ase.DB.VideoCallDataBase;
import com.ase.RandomNumber.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.WebServiceInterface;

/**
 * Created by Preethi on 4/25/2018.
 */

public class CheckListActivity extends Activity implements WebServiceInterface {
    checkListDetails checklistBean;
    private TextView stickyView;
    private ListView listView;
    private View heroImageView;
    LinearLayout checklist_master;
    TableLayout listHeading;
    Context checkListContext;
    CheckListItemAdapter adapter;
    private View stickyViewSpacer;
    TextView custName, custAddress, checklist_date, machine_serial, machine_model, checklist_back, checklist_date_now, checklist_jobNo;
    String strIPath;
    Handler handler;
    ProgressDialog progress;
    boolean isCommentsImg, isAdviceImg, isTechnicianSign, isCustomerSign;
    String comments_path, advice_path, techSign_path, custSign_path;
    ImageView checklist_commands_img, checklist_advice_img, checklist_tech_sign_img, checklist_cust_sign_img, send_completion;
    Button checklist_tech_signature, checklist_cust_signature, checklist_date_btn;
    String ProjectId, TaskId;
    String PMS_Customer, PMS_Address, PMS_machine_serial, PMS_machine_model, PMS_machine_make;
    EditText checklist_HMReading, checklist_tech_name, checklist_client_name, checklist_advice_text;
    static CheckListActivity checkListActivity;
    boolean isReadOnlyView;

    public static CheckListActivity getInstance() {
        return checkListActivity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.servicelist);
        Intent intent = getIntent();
        checkListContext = this;
        handler = new Handler();
        checklistBean = (checkListDetails) intent.getSerializableExtra("checklistBean");
        Appreference.myOfflineCheckListDetails = checklistBean;
        ProjectId = intent.getStringExtra("PMSprojectId");
        TaskId = intent.getStringExtra("PMStaskId");
        checklistBean.setChecklist_projectId(ProjectId);
        checklistBean.setChecklist_taskId(TaskId);

        isReadOnlyView = intent.getBooleanExtra("isExistingView", false);
         /* Initialise list view, hero image, and sticky view */
        listView = (ListView) findViewById(R.id.listView);
//        heroImageView = findViewById(R.id.heroImageView);
          /* Inflate list header layout */
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listHeader = inflater.inflate(R.layout.checklist_header, null);
        checklist_master = (LinearLayout) listHeader.findViewById(R.id.checklist_master);
        stickyView = (TextView) listHeader.findViewById(R.id.stickyView);

        custName = (TextView) listHeader.findViewById(R.id.checklist_cust_name);
        custAddress = (TextView) listHeader.findViewById(R.id.checklist_cust_address);
        checklist_date = (TextView) listHeader.findViewById(R.id.checklist_date);
        machine_serial = (TextView) listHeader.findViewById(R.id.checklist_machineNo);
        machine_model = (TextView) listHeader.findViewById(R.id.checklist_machineMac);
        checklist_back = (TextView) listHeader.findViewById(R.id.checklist_back);
        checklist_jobNo = (TextView) listHeader.findViewById(R.id.checklist_jobNo);
        listHeading = (TableLayout) listHeader.findViewById(R.id.listHeading);
        send_completion = (ImageView) listHeader.findViewById(R.id.send_completion);
        checklist_HMReading = (EditText) listHeader.findViewById(R.id.checklist_HMReading);


        stickyView.setText(checklistBean.getCheckListName());

        String PMScustomerName_query = "select customerName from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'and projectId='" + ProjectId + "'";
        PMS_Customer = VideoCallDataBase.getDB(checkListContext).getprojectIdForOracleID(PMScustomerName_query);
        String PMScustomerAddr_query = "select address from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'and projectId='" + ProjectId + "'";
        PMS_Address = VideoCallDataBase.getDB(checkListContext).getprojectIdForOracleID(PMScustomerAddr_query);
        String PMSserialNo_query = "select mcSrNo  from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'and projectId='" + ProjectId + "'";
        PMS_machine_serial = VideoCallDataBase.getDB(checkListContext).getprojectIdForOracleID(PMSserialNo_query);
        String PMSmodel_query = "select mcModel from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'and projectId='" + ProjectId + "'";
        PMS_machine_model = VideoCallDataBase.getDB(checkListContext).getprojectIdForOracleID(PMSmodel_query);
        String PMSmake_query = "select machineMake from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'and projectId='" + ProjectId + "'";
        PMS_machine_make = VideoCallDataBase.getDB(checkListContext).getprojectIdForOracleID(PMSmake_query);
        String PMSOracleId_query = "select oracleProjectId from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'and projectId='" + ProjectId + "'";
        String PMS_oracle_projectId = VideoCallDataBase.getDB(checkListContext).getprojectIdForOracleID(PMSOracleId_query);

        String PMSOpenDate_query = "select openDate from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'and projectId='" + ProjectId + "'";
        String PMS_OpenDate = VideoCallDataBase.getDB(checkListContext).getprojectIdForOracleID(PMSOpenDate_query);


        custName.setText(PMS_Customer);
        checklistBean.setCustomer(PMS_Customer);
        custAddress.setText(PMS_Address);
        checklistBean.setCustomerAddress(PMS_Address);

        if (checklistBean.getDate() != null && !checklistBean.getDate().equalsIgnoreCase("")) {
            checklist_date.setText(checklistBean.getDate());
        } else {
            checklist_date.setText(PMS_OpenDate);
            checklistBean.setDate(PMS_OpenDate);
        }
        machine_serial.setText(PMS_machine_serial);
        checklistBean.setSerialNumber(PMS_machine_serial);

        machine_model.setText(PMS_machine_make + " / " + PMS_machine_model);
        checklistBean.setModel(PMS_machine_model);

        checklist_jobNo.setText(PMS_oracle_projectId);
        if (checklistBean.getHourMeter() != null && !checklistBean.getHourMeter().equalsIgnoreCase("")) {
            checklist_HMReading.setText(checklistBean.getHourMeter());
        }


        adapter = new CheckListItemAdapter(checkListContext, checklistBean.getLabel());
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        View footerView = getLayoutInflater().inflate(R.layout.checklist_footer, null);
        stickyViewSpacer = listHeader.findViewById(R.id.stickyViewPlaceholder);

        /* Add list view header */
        listView.addHeaderView(listHeader);
        listView.addFooterView(footerView);
        final Button checklist_commands = (Button) footerView.findViewById(R.id.checklist_commands);
        final Button checklist_advice = (Button) footerView.findViewById(R.id.checklist_advice);
        checklist_tech_signature = (Button) footerView.findViewById(R.id.checklist_tech_sign);
        checklist_cust_signature = (Button) footerView.findViewById(R.id.checklist_cust_sign);
        final EditText checklist_commands_text = (EditText) footerView.findViewById(R.id.checklist_cmds_text);
        checklist_commands_img = (ImageView) footerView.findViewById(R.id.checklist_cmds_img);
        checklist_advice_text = (EditText) footerView.findViewById(R.id.checklist_advice_text);
        checklist_advice_img = (ImageView) footerView.findViewById(R.id.checklist_advice_img);
        checklist_cust_sign_img = (ImageView) footerView.findViewById(R.id.checklist_cust_img);
        checklist_tech_sign_img = (ImageView) footerView.findViewById(R.id.checklist_tech_img);
        checklist_date_btn = (Button) footerView.findViewById(R.id.checklist_date_btn);
        checklist_date_now = (TextView) footerView.findViewById(R.id.checklist_date_now);
        checklist_tech_name = (EditText) footerView.findViewById(R.id.checklist_tech_name);
        checklist_client_name = (EditText) footerView.findViewById(R.id.checklist_client_name);

        if (Appreference.loginuserdetails != null && Appreference.loginuserdetails.getUsername() != null) {
            checklist_tech_name.setText(Appreference.loginuserdetails.getFirstName() + " " + Appreference.loginuserdetails.getLastName());
            checklist_tech_name.setEnabled(false);
        }

        if (isReadOnlyView) {
            checklist_HMReading.setEnabled(false);
            checklist_advice.setEnabled(false);
            checklist_tech_name.setEnabled(false);
            checklist_tech_signature.setEnabled(false);
            checklist_cust_signature.setEnabled(false);
            checklist_client_name.setEnabled(false);
            checklist_date_btn.setEnabled(false);
            send_completion.setVisibility(View.GONE);
            checklist_advice_text.setEnabled(false);
        }


        if (checklistBean.getRemarks() != null && !checklistBean.getRemarks().equalsIgnoreCase("")) {
            if (checklistBean.getRemarks().contains(".jpg")) {
                File imgFile = new File(checklistBean.getRemarks());
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    checklist_commands_img.setImageBitmap(myBitmap);
                }
                checklist_commands_img.setVisibility(View.VISIBLE);
            } else {
                checklist_commands_text.setVisibility(View.VISIBLE);
                checklist_commands_text.setText(checklistBean.getRemarks());
            }
        }


        if (checklistBean.getAdvicetoCustomer() != null && !checklistBean.getAdvicetoCustomer().equalsIgnoreCase("")) {
            if (checklistBean.getAdvicetoCustomer().contains(".jpg")) {
                File imgFile = new File(checklistBean.getAdvicetoCustomer());
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    checklist_advice_img.setImageBitmap(myBitmap);
                }
                checklist_advice_img.setVisibility(View.VISIBLE);
            } else {
                checklist_advice_text.setVisibility(View.VISIBLE);
                checklist_advice_text.setText(checklistBean.getAdvicetoCustomer());
            }
        }


        if (checklistBean.getTechnicianName() != null && !checklistBean.getTechnicianName().equalsIgnoreCase("")) {
            checklist_tech_name.setText(checklistBean.getTechnicianName());
        }
        if (checklistBean.getClientName() != null && !checklistBean.getClientName().equalsIgnoreCase("")) {
            checklist_client_name.setText(checklistBean.getClientName());
        }

        if (checklistBean.getTechnicianSignature() != null && !checklistBean.getTechnicianSignature().equalsIgnoreCase("")) {
            File imgFile = new File(checklistBean.getTechnicianSignature());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                checklist_tech_sign_img.setImageBitmap(myBitmap);
            }
            checklist_tech_sign_img.setVisibility(View.VISIBLE);
        }

        if (checklistBean.getClientSignature() != null && !checklistBean.getClientSignature().equalsIgnoreCase("")) {
            File imgFile = new File(checklistBean.getClientSignature());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                checklist_cust_sign_img.setImageBitmap(myBitmap);
            }
            checklist_cust_sign_img.setVisibility(View.VISIBLE);
        }

        if (checklistBean.getSignedDate() != null && !checklistBean.getSignedDate().equalsIgnoreCase("")) {
            checklist_date_now.setText(checklistBean.getSignedDate());
        }


        checklist_commands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(checkListContext);
                saveDialog.setTitle("Comments");
                saveDialog.setCancelable(false);
                saveDialog.setMessage("You want to type or draw via sketch " + checklistBean.getCheckListName());
                saveDialog.setPositiveButton("Text", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        checklist_commands_text.setVisibility(View.VISIBLE);
                        checklist_commands_img.setVisibility(View.GONE);
                        isCommentsImg = false;
                        dialog.cancel();
                    }
                });
                saveDialog.setNeutralButton("Sketch",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    checklist_commands_text.setVisibility(View.GONE);
                                    isCommentsImg = true;
                                    isAdviceImg = false;
                                    isTechnicianSign = false;
                                    isCustomerSign = false;
                                    Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                                    i.putExtra("isFromcheckList", true);
                                    startActivityForResult(i, 423);
                                    dialog.cancel();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("checklist123", "action_taken_type Exception : " + e.getMessage(), "WARN", null);
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

        checklist_advice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder saveDialog = new AlertDialog.Builder(checkListContext);
                saveDialog.setTitle("Advice to the Customer");
                saveDialog.setCancelable(false);
                saveDialog.setMessage("You want to type or draw via sketch " + checklistBean.getCheckListName());
                saveDialog.setPositiveButton("Text", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        checklist_advice_text.setVisibility(View.VISIBLE);
                        checklist_advice_img.setVisibility(View.INVISIBLE);
                        isAdviceImg = false;
                        dialog.cancel();
                    }
                });
                saveDialog.setNeutralButton("Sketch",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    checklist_advice_text.setVisibility(View.GONE);
                                    isAdviceImg = true;
                                    isCommentsImg = false;
                                    isTechnicianSign = false;
                                    isCustomerSign = false;
                                    Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                                    i.putExtra("isFromcheckList", true);
                                    startActivityForResult(i, 423);
                                    dialog.cancel();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("checklist123", "action_taken_type Exception : " + e.getMessage(), "WARN", null);
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
        checklist_tech_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    isTechnicianSign = true;
                    isAdviceImg = false;
                    isCommentsImg = false;
                    isCustomerSign = false;
                    Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                    i.putExtra("isFromchecList", false);
                    startActivityForResult(i, 423);
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    Appreference.printLog("checklist123", "skech_receiver clicklistener Exception : " + e.getMessage(), "WARN", null);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("checklist123", "skech_receiver clicklistener Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });

        checklist_cust_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    isCustomerSign = true;
                    isAdviceImg = false;
                    isCommentsImg = false;
                    isTechnicianSign = false;
                    Intent i = new Intent(getApplicationContext(), HandSketchActivity2.class);
                    i.putExtra("isFromchecList", false);
                    startActivityForResult(i, 423);
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    Appreference.printLog("checklist123", "skech_receiver clicklistener Exception : " + e.getMessage(), "WARN", null);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("checklist123", "skech_receiver clicklistener Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });

        checklist_commands_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ImageName = comments_path;
                    File file = null;
                    if (ImageName != null && !ImageName.equalsIgnoreCase("")) {
                        file = new File(ImageName);
                        if (file.exists()) {
                           /* Intent i = new Intent(CheckListActivity.this, FullScreenViewActivity.class);
                            i.putExtra("position", 0);
                            i.putExtra("pathSketch", ImageName);
                            startActivity(i);*/
                            Intent intent = new Intent(checkListContext, FullScreenImage.class);
                            intent.putExtra("image", file.toString());
                            checkListContext.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                            if (file1.exists()) {
                                Intent intent = new Intent(checkListContext, FullScreenImage.class);
                                intent.putExtra("image", file1.toString());
                                checkListContext.startActivity(intent);
                            }
                        }
                    } else
                        Toast.makeText(CheckListActivity.this, "Please Set any Image to View", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "action_taken_1 clicklistener Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });
        checklist_advice_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ImageName = advice_path;
                    File file = null;
                    if (ImageName != null && !ImageName.equalsIgnoreCase("")) {
                        file = new File(ImageName);
                        if (file.exists()) {
                            /*Intent i = new Intent(CheckListActivity.this, FullScreenViewActivity.class);
                            i.putExtra("position", 0);
                            i.putExtra("pathSketch", ImageName);
                            startActivity(i);*/
                            Intent intent = new Intent(checkListContext, FullScreenImage.class);
                            intent.putExtra("image", file.toString());
                            checkListContext.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                            if (file1.exists()) {
                                Intent intent = new Intent(checkListContext, FullScreenImage.class);
                                intent.putExtra("image", file1.toString());
                                checkListContext.startActivity(intent);
                            }
                        }
                    } else
                        Toast.makeText(CheckListActivity.this, "Please Set any Image to View", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "action_taken_1 clicklistener Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });
        checklist_tech_sign_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ImageName = techSign_path;
                    File file = null;
                    if (ImageName != null && !ImageName.equalsIgnoreCase("")) {
                        file = new File(ImageName);
                        if (file.exists()) {
                           /* Intent i = new Intent(CheckListActivity.this, FullScreenViewActivity.class);
                            i.putExtra("position", 0);
                            i.putExtra("pathSketch", ImageName);
                            startActivity(i);*/
                            Intent intent = new Intent(checkListContext, FullScreenImage.class);
                            intent.putExtra("image", file.toString());
                            checkListContext.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                            if (file1.exists()) {
                                Intent intent = new Intent(checkListContext, FullScreenImage.class);
                                intent.putExtra("image", file1.toString());
                                checkListContext.startActivity(intent);
                            }
                        }
                    } else
                        Toast.makeText(CheckListActivity.this, "Please Set any Image to View", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "action_taken_1 clicklistener Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });
        checklist_cust_sign_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String ImageName = custSign_path;
                    File file = null;
                    if (ImageName != null && !ImageName.equalsIgnoreCase("")) {
                        file = new File(ImageName);
                        if (file.exists()) {
                           /* Intent i = new Intent(CheckListActivity.this, FullScreenViewActivity.class);
                            i.putExtra("position", 0);
                            i.putExtra("pathSketch", ImageName);
                            startActivity(i);*/
                            Intent intent = new Intent(checkListContext, FullScreenImage.class);
                            intent.putExtra("image", file.toString());
                            checkListContext.startActivity(intent);
                        } else {
                            File file1 = null;
                            file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/" + ImageName);
                            if (file1.exists()) {
                                Intent intent = new Intent(checkListContext, FullScreenImage.class);
                                intent.putExtra("image", file1.toString());
                                checkListContext.startActivity(intent);
                            }
                        }
                    } else
                        Toast.makeText(CheckListActivity.this, "Please Set any Image to View", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("EodScreen", "action_taken_1 clicklistener Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });
        checklist_date_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(checkListContext,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                try {
                                    String curr_date = null;
                                    try {
                                        SimpleDateFormat simpleDateFormat_1 = new SimpleDateFormat("dd-MM-yyyy");
                                        curr_date = simpleDateFormat_1.format(new Date());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Appreference.printLog("ProjectFragment", "activity_start curr_date Exception : " + e.getMessage(), "WARN", null);
                                    }
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
                                    String start_date = days + "-" + months + "-" + year;
                                    Log.i("TNA", "start_date---> " + start_date);
                                    Log.i("TNA", "curr_date---> " + curr_date);
                                    if (curr_date != null && curr_date.compareTo(start_date) > 0) {
                                        Toast.makeText(checkListContext, "Kindly select valid date ", Toast.LENGTH_SHORT).show();
                                    } else {
                                        checklist_date_now.setText(start_date);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("ProjectFragment", "activity_start TNAReportStart  Exception : " + e.getMessage(), "WARN", null);
                                }
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                dpd.show();
            }
        });
        checklist_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back_alert_dialog();
                /*try {
                    String query = "select * from checklistDetails where projectId='" + checklistBean.getChecklist_projectId() + "'and taskId='" + checklistBean.getChecklist_taskId() + "'and userId='" + Appreference.loginuserdetails.getId() + "'";
                    checkListDetails checklistBean = VideoCallDataBase.getDB(checkListContext).getchecklistdetails(query);
                    if (checklistBean != null && checklistBean.getIsServiceDone() != null && checklistBean.getIsServiceDone().equalsIgnoreCase("0")) {
                        AutoSaveCheckList();
                    } else if (checklistBean.getIsServiceDone() == null) {
                        AutoSaveCheckList();
                    }
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();*/
            }
        });
        /* Handle list View scroll events*/
       /* listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                 *//*Check if the first item is already reached to top.*//*
                if (listView.getFirstVisiblePosition() == 0) {
                    View firstChild = listView.getChildAt(0);
                    int topY = 0;
                    if (firstChild != null) {
                        topY = firstChild.getTop();
                    }

                    int heroTopY = stickyViewSpacer.getTop();
                    int headerXY = checklist_master.getHeight();

                    stickyView.setY(Math.max(0, headerXY + topY+10));

*//*
                     Set the image to scroll half of the amount that of ListView*//*
                    checklist_master.setY(topY * 0.5f);
                    int listHeadingXY = listHeading.getTop();
                    listHeading.setY(Math.max(0, listHeadingXY+70));
                    Log.i("checklist123", "heroTopY====> " + heroTopY);
                    Log.i("checklist123", "checklist_master XY====> " + topY * 0.5f);
                    Log.i("checklist123", "headerXY====> " + headerXY);
                    Log.i("checklist123", "topY====> " + topY);
                }
            }
        });*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long id) {
//                listView.getChildAt(position).setBackgroundColor(Color.LTGRAY);
            }
        });
        send_completion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                composeChecklistXml();
            }
        });

    }

    private void sendCheckListAlert_Dialog(final JSONObject jsonObjectAll, final checkListDetails checklistAllBean) {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(CheckListActivity.this).create();
            alertDialog.setTitle(checklistBean.getCheckListName());
            alertDialog.setCancelable(false);
            alertDialog.setMessage("You are submitting the check list changes to Server! \n" +
                    "No more changes allowed after this Submit event! \n" +
                    "Are you sure of your action?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            /****************************/
                            SaveDB(checklistAllBean, 1);
                            /****************************/
                            if (isNetworkAvailable()) {
                                showprogress();
                                Appreference.jsonRequestSender.SaveCheckListForm(EnumJsonWebservicename.saveCheckListDataDetails, jsonObjectAll, CheckListActivity.this);
                            } else {
                                finish();
//                                Toast.makeText(CheckListActivity.this, "Network Not Available!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("EodScreen", "alert_dialog Exception : " + e.getMessage(), "WARN", null);
        }
    }

    private void sendCheckListMandatory_Alert(String Message) {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(CheckListActivity.this).create();
            alertDialog.setTitle(checklistBean.getCheckListName());
            alertDialog.setCancelable(false);
            alertDialog.setMessage(Message);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("EodScreen", "alert_dialog Exception : " + e.getMessage(), "WARN", null);
        }
    }

    private void composeChecklistXml() {
        try {
            checkListDetails checklistAllBean = new checkListDetails();
            JSONObject jsonObjectAll = new JSONObject();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("id", ProjectId);
            checklistAllBean.setChecklist_projectId(ProjectId);
            jsonObjectAll.put("project", jsonObject1);

            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("id", TaskId);
            checklistAllBean.setChecklist_taskId(TaskId);
            jsonObjectAll.put("task", jsonObject2);

            JSONObject jsonObject3 = new JSONObject();
            jsonObject3.put("id", Appreference.loginuserdetails.getId());
            checklistAllBean.setChecklist_fromId(String.valueOf(Appreference.loginuserdetails.getId()));
            jsonObjectAll.put("from", jsonObject3);

//                    jsonObjectAll.put("customerName", PMS_Customer);
            checklistAllBean.setCustomer(PMS_Customer);

//                    jsonObjectAll.put("customerAddress", PMS_Address);
            checklistAllBean.setCustomerAddress(PMS_Address);

//                    jsonObjectAll.put("machineSerial", PMS_machine_serial);
            checklistAllBean.setSerialNumber(PMS_machine_serial);

//                    jsonObjectAll.put("machineModal", PMS_machine_model);
            checklistAllBean.setModel(PMS_machine_model);

//                    jsonObjectAll.put("checklistDate", checklist_date.getText().toString());
            checklistAllBean.setDate(checklist_date.getText().toString());

            jsonObjectAll.put("hmReading", checklist_HMReading.getText().toString());
            checklistAllBean.setHourMeter(checklist_HMReading.getText().toString());

//                    jsonObjectAll.put("remarks", checklist_commands_text.getText().toString());
//                    checklistAllBean.setRemarks(checklist_commands_text.getText().toString());

            jsonObjectAll.put("advicetoCustomer", checklist_advice_text.getText().toString());
            checklistAllBean.setAdvicetoCustomer(checklist_advice_text.getText().toString());

//                    jsonObjectAll.put("technicianName", checklist_tech_name.getText().toString());
            checklistAllBean.setTechnicianName(checklist_tech_name.getText().toString());

            jsonObjectAll.put("clientName", checklist_client_name.getText().toString());
            checklistAllBean.setClientName(checklist_client_name.getText().toString());

            String signed_dateUTC = "";
            Date date = null;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat dateParse = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//            String signed_date = checklist_date_now.getText().toString() + " " + "00:00:00";
            SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
            String time = mdformat.format(Calendar.getInstance().getTime());
            String signed_date = checklist_date_now.getText().toString() +" " + time;
            if (signed_date != null && !signed_date.equalsIgnoreCase("")) {
                try {
                    date = dateParse.parse(signed_date);
                    signed_dateUTC = dateFormat.format(date);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("checkListActivity", "signed_dateUTC Exception : " + e.getMessage(), "WARN", null);
                }
            }
            jsonObjectAll.put("checklistEntryDate", signed_dateUTC);

            checklistAllBean.setSignedDate(checklist_date_now.getText().toString());

//                    jsonObjectAll.put("checkListName", stickyView.getText().toString());
            checklistAllBean.setCheckListName(stickyView.getText().toString());


            JSONObject jsonObject4 = new JSONObject();
            if (techSign_path != null && !techSign_path.equalsIgnoreCase(null) && !techSign_path.equalsIgnoreCase("")) {
                try {

                    jsonObject4.put("fileContent", encodeTobase64(BitmapFactory.decodeFile(techSign_path)));
                    jsonObject4.put("taskFileExt", "jpg");
                    jsonObjectAll.put("technicianSignatures", jsonObject4);
                    checklistAllBean.setTechnicianSignature(techSign_path);

                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("checkListActivity", "sendchecklist_webservice  tech_signature Exception : " + e.getMessage(), "WARN", null);
                }
            } else {
                if (checklistBean != null) {
                    try {
                        jsonObject4.put("fileContent", encodeTobase64(BitmapFactory.decodeFile(checklistBean.getTechnicianSignature())));
                        jsonObject4.put("taskFileExt", "jpg");
                        jsonObjectAll.put("technicianSignatures", jsonObject4);
                        checklistAllBean.setTechnicianSignature(checklistBean.getTechnicianSignature());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("checkListActivity", "sendchecklist_webservice  tech_signature Exception : " + e.getMessage(), "WARN", null);
                    }
                }

            }
            JSONObject jsonObject5 = new JSONObject();
            if (custSign_path != null && !custSign_path.equalsIgnoreCase(null) && !custSign_path.equalsIgnoreCase("")) {
                try {

                    jsonObject5.put("fileContent", encodeTobase64(BitmapFactory.decodeFile(custSign_path)));
                    jsonObject5.put("taskFileExt", "jpg");
                    checklistAllBean.setClientSignature(custSign_path);
                    jsonObjectAll.put("clientSignatures", jsonObject5);

                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("checkListActivity", "sendchecklist_webservice  custSign_path Exception : " + e.getMessage(), "WARN", null);
                }
            } else {
                if (checklistBean != null) {
                    try {
                        jsonObject5.put("fileContent", encodeTobase64(BitmapFactory.decodeFile(checklistBean.getClientSignature())));
                        jsonObject5.put("taskFileExt", "jpg");
                        checklistAllBean.setClientSignature(checklistBean.getClientSignature());
                        jsonObjectAll.put("clientSignatures", jsonObject5);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("checkListActivity", "sendchecklist_webservice  custSign_path Exception : " + e.getMessage(), "WARN", null);
                    }
                }
            }


            JSONObject jsonObject7 = new JSONObject();
            if (advice_path != null && !advice_path.equalsIgnoreCase(null) && !advice_path.equalsIgnoreCase("")) {
                try {
                    jsonObject7.put("fileContent", encodeTobase64(BitmapFactory.decodeFile(advice_path)));
                    jsonObject7.put("taskFileExt", "jpg");
                    jsonObjectAll.put("adviceImage", jsonObject7);
                    checklistAllBean.setAdvicetoCustomer(advice_path);

                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("checkListActivity", "sendchecklist_webservice  advice_path Exception : " + e.getMessage(), "WARN", null);
                }
            } else {
                if (checklistBean != null && checklistBean.getAdvicetoCustomer() != null && checklistBean.getAdvicetoCustomer().contains(".jpg")) {
                    try {
                        jsonObject7.put("fileContent", encodeTobase64(BitmapFactory.decodeFile(checklistBean.getAdvicetoCustomer())));
                        jsonObject7.put("taskFileExt", "jpg");
                        jsonObjectAll.put("adviceImage", jsonObject7);
                        checklistAllBean.setAdvicetoCustomer(checklistBean.getAdvicetoCustomer());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("checkListActivity", "sendchecklist_webservice  advice_path Exception : " + e.getMessage(), "WARN", null);
                    }
                }
            }
            JSONArray Multi_array = new JSONArray();

            if (checklistBean != null) {
                for (int i = 0; i < checklistBean.getLabel().size(); i++) {
                    JSONObject checklist_row = new JSONObject();
                    Label fieldvalues = checklistBean.getLabel().get(i);
                    if (fieldvalues.getIssueType().equalsIgnoreCase("M") && fieldvalues.getJobstatus() != null) {
                        Appreference.isMandatoryLabelNotFilled = false;
                    } else if (fieldvalues.getIssueType().equalsIgnoreCase("M")) {
                        Appreference.isMandatoryLabelNotFilled = true;
                        break;
                    }
                    if (fieldvalues.getId() != null && !fieldvalues.getId().equalsIgnoreCase("")
                            && !fieldvalues.getId().equalsIgnoreCase("null")
                            && !fieldvalues.getId().equalsIgnoreCase(null)) {
                        Appreference.ischecklistIDEmpty = false;
                        checklist_row.put("checkListDetailId", fieldvalues.getId());
                    } else {
                        Appreference.ischecklistIDEmpty = true;
                        break;
                    }
                    checklist_row.put("jobstatus", fieldvalues.getJobstatus());
                    checklist_row.put("quantity", fieldvalues.getQuantity());
                    Multi_array.put(i, checklist_row);
                }
            }
            checklistAllBean.setLabel(checklistBean.getLabel());
            jsonObjectAll.put("servicelist", Multi_array);

            if (!isNetworkAvailable()) {
                checklistAllBean.setWsSendStatus("00");
            }


            if (!Appreference.ischecklistIDEmpty) {
                if (!Appreference.isMandatoryLabelNotFilled) {
                    if ((checklistAllBean.getTechnicianName() != null && !checklistAllBean.getTechnicianName().equalsIgnoreCase("")
                            && !checklistAllBean.getTechnicianName().equalsIgnoreCase(null) && !checklistAllBean.getTechnicianName().equalsIgnoreCase("null"))
                            && (checklistAllBean.getClientName() != null && !checklistAllBean.getClientName().equalsIgnoreCase("")
                            && !checklistAllBean.getClientName().equalsIgnoreCase(null) && !checklistAllBean.getClientName().equalsIgnoreCase("null"))
                            && (checklistAllBean.getTechnicianSignature() != null && !checklistAllBean.getTechnicianSignature().equalsIgnoreCase("")
                            && !checklistAllBean.getTechnicianSignature().equalsIgnoreCase(null) && !checklistAllBean.getTechnicianSignature().equalsIgnoreCase("null"))
                            && (checklistAllBean.getClientSignature() != null && !checklistAllBean.getClientSignature().equalsIgnoreCase("")
                            && !checklistAllBean.getClientSignature().equalsIgnoreCase(null) && !checklistAllBean.getClientSignature().equalsIgnoreCase("null"))
                            && (checklistAllBean.getSignedDate() != null && !checklistAllBean.getSignedDate().equalsIgnoreCase("")
                            && !checklistAllBean.getSignedDate().equalsIgnoreCase(null) && !checklistAllBean.getSignedDate().equalsIgnoreCase("null"))) {

                        sendCheckListAlert_Dialog(jsonObjectAll, checklistAllBean);
                    } else {
                        sendCheckListMandatory_Alert("Please fill name and signature field and submit again! ");
                    }
                } else {
                    sendCheckListMandatory_Alert("Please fill all the Mandatory fields and submit again!");
                }
            } else {
                sendCheckListMandatory_Alert("EXCEPTION! checklistID NULL! ");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void AutoSaveCheckList() {
        try {
            checkListDetails checklistAllBean = new checkListDetails();
            checklistAllBean.setLabel(checklistBean.getLabel());

            checklistAllBean.setChecklist_projectId(checklistBean.getChecklist_projectId());
            checklistAllBean.setChecklist_taskId(checklistBean.getChecklist_taskId());
            checklistAllBean.setChecklist_fromId(String.valueOf(Appreference.loginuserdetails.getId()));
            checklistAllBean.setCustomer(checklistBean.getCustomer());
            checklistAllBean.setCustomerAddress(checklistBean.getCustomerAddress());
            checklistAllBean.setSerialNumber(checklistBean.getSerialNumber());
            checklistAllBean.setModel(checklistBean.getModel());
            checklistAllBean.setDate(checklistBean.getDate());


            if (checklist_HMReading != null && !checklist_HMReading.getText().toString().equalsIgnoreCase("")) {
                checklistAllBean.setHourMeter(checklist_HMReading.getText().toString());
            } else {
                checklistAllBean.setHourMeter("");
            }

            if (checklist_advice_text != null && !checklist_advice_text.getText().toString().equalsIgnoreCase("")) {
                checklistAllBean.setAdvicetoCustomer(checklist_advice_text.getText().toString());
            } else {
                checklistAllBean.setAdvicetoCustomer("");
            }

            if (checklist_tech_name != null && !checklist_tech_name.getText().toString().equalsIgnoreCase("")) {
                checklistAllBean.setTechnicianName(checklist_tech_name.getText().toString());
            } else {
                checklistAllBean.setTechnicianName("");
            }
            if (checklist_client_name != null && !checklist_client_name.getText().toString().equalsIgnoreCase("")) {
                checklistAllBean.setClientName(checklist_client_name.getText().toString());
            } else {
                checklistAllBean.setClientName("");
            }
            if (checklist_date_now != null && !checklist_date_now.getText().toString().equalsIgnoreCase("")) {
                checklistAllBean.setSignedDate(checklist_date_now.getText().toString());
            } else {
                checklistAllBean.setSignedDate("");
            }
            if (stickyView != null && !stickyView.getText().toString().equalsIgnoreCase("")) {
                checklistAllBean.setCheckListName(stickyView.getText().toString());
            } else {
                checklistAllBean.setCheckListName("");
            }

            if (advice_path != null && !advice_path.equalsIgnoreCase(null) && !advice_path.equalsIgnoreCase("")) {
                checklistAllBean.setAdvicetoCustomer(advice_path);
            }
            if (custSign_path != null && !custSign_path.equalsIgnoreCase(null) && !custSign_path.equalsIgnoreCase("")) {
                checklistAllBean.setClientSignature(custSign_path);
            }
            if (techSign_path != null && !techSign_path.equalsIgnoreCase(null) && !techSign_path.equalsIgnoreCase("")) {
                checklistAllBean.setTechnicianSignature(techSign_path);
            }
            /****************************/
            SaveDB(checklistAllBean, 0);
            /****************************/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            Log.i("EodScreen", "onKeyDown ");
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                Log.i("EodScreen", "onKeyDown");
                back_alert_dialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("EodScreen", "onKeyDown Exception : " + e.getMessage(), "WARN", null);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void back_alert_dialog() {
        try {
            AlertDialog alertDialog = new AlertDialog.Builder(CheckListActivity.this).create();
            alertDialog.setTitle(checklistBean.getCheckListName());
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
                            try {
                                String query = "select * from checklistDetails where projectId='" + checklistBean.getChecklist_projectId() + "'and taskId='" + checklistBean.getChecklist_taskId() + "'and userId='" + Appreference.loginuserdetails.getId() + "'";
                                checkListDetails checklistBean = VideoCallDataBase.getDB(checkListContext).getchecklistdetails(query);
                                if (checklistBean != null && checklistBean.getIsServiceDone() != null && checklistBean.getIsServiceDone().equalsIgnoreCase("0")) {
                                    AutoSaveCheckList();
                                } else if (checklistBean.getIsServiceDone() == null) {
                                    AutoSaveCheckList();
                                }
                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("NewTaskConversation", "onBackPressed Exception : " + e.getMessage(), "WARN", null);
                            }
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
    protected void onPause() {
        super.onPause();
        Log.i("checklist123", "onPause  ****====> ");

        try {
            String query = "select * from checklistDetails where projectId='" + checklistBean.getChecklist_projectId() + "'and taskId='" + checklistBean.getChecklist_taskId() + "'and userId='" + Appreference.loginuserdetails.getId() + "'";
            checkListDetails checklistBean = VideoCallDataBase.getDB(checkListContext).getchecklistdetails(query);
            if (checklistBean != null && checklistBean.getIsServiceDone() != null && checklistBean.getIsServiceDone().equalsIgnoreCase("0")) {
                AutoSaveCheckList();
            } else if (checklistBean.getIsServiceDone() == null) {
                AutoSaveCheckList();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("ChecklistActivity", "onPause Exception : " + e.getMessage(), "WARN", null);
        }
    }

    private void SaveDB(checkListDetails checklistAllBean, int isServiceDone) {
        VideoCallDataBase.getDB(checkListContext).insertORupdateCheckListDetails(checklistAllBean, isServiceDone);
        VideoCallDataBase.getDB(checkListContext).insertORupdateCheckListData(checklistAllBean.getLabel());
    }

    public void showToast(final String msg) {
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(CheckListActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("NewTaskConversation", "showToast() Exception : " + e.getMessage(), "WARN", null);
        }
    }

    private void showprogress() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("login123", "inside showProgressDialog");
                    if (progress == null)
                        progress = new ProgressDialog(checkListContext);
                    progress.setCancelable(false);
                    progress.setMessage("Saving.....");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setProgress(0);
                    progress.setMax(100);
                    progress.show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("NewTaskConversation", "showprogress() Exception : " + e.getMessage(), "WARN", null);
                }
            }


        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("checklist123", "onBackPressed====> ");

      /*  try {
            String query = "select * from checklistDetails where projectId='" + checklistBean.getChecklist_projectId() + "'and taskId='" + checklistBean.getChecklist_taskId() + "'and userId='" + Appreference.loginuserdetails.getId() + "'";
            checkListDetails checklistBean = VideoCallDataBase.getDB(checkListContext).getchecklistdetails(query);
            if (checklistBean != null && checklistBean.getIsServiceDone() != null && checklistBean.getIsServiceDone().equalsIgnoreCase("0")) {
                AutoSaveCheckList();
            } else if (checklistBean.getIsServiceDone() == null) {
                AutoSaveCheckList();
            }
            finish();
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("NewTaskConversation", "onBackPressed Exception : " + e.getMessage(), "WARN", null);
        }*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        try {
            Log.d("task", "inside activity result page" + RESULT_OK + " resultCode :" + resultCode + " requestCode :" + requestCode);
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK) {
                if (requestCode == 423) {
                    if (data != null) {
                        try {
                            strIPath = data.getStringExtra("path");
                            Log.i("checklist123", "path of sketch====> " + strIPath);
                            if (isCommentsImg) {
                                comments_path = strIPath;
                                if (checklist_commands_img != null) {
                                    File imgFile = new File(comments_path);
                                    if (imgFile.exists()) {
                                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                        checklist_commands_img.setImageBitmap(myBitmap);
                                    }
                                    checklist_commands_img.setVisibility(View.VISIBLE);
                                }
                            } else if (isAdviceImg) {
                                advice_path = strIPath;
                                if (checklist_advice_img != null) {
                                    File imgFile = new File(advice_path);
                                    if (imgFile.exists()) {
                                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                        checklist_advice_img.setImageBitmap(myBitmap);
                                    }
                                    checklist_advice_img.setVisibility(View.VISIBLE);
                                }
                            } else if (isCustomerSign) {
                                custSign_path = strIPath;
                                if (checklist_cust_sign_img != null) {
                                    File imgFile = new File(custSign_path);
                                    if (imgFile.exists()) {
                                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                        checklist_cust_sign_img.setImageBitmap(myBitmap);
                                    }
                                    checklist_cust_sign_img.setVisibility(View.VISIBLE);
                                }
                            } else if (isTechnicianSign) {
                                techSign_path = strIPath;
                                if (checklist_tech_sign_img != null) {
                                    File imgFile = new File(techSign_path);
                                    if (imgFile.exists()) {
                                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                        checklist_tech_sign_img.setImageBitmap(myBitmap);
                                    }
                                    checklist_tech_sign_img.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String encodeTobase64(Bitmap image) {
        String imageEncoded = null;
        try {
            Bitmap immagex = image;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            immagex.compress(Bitmap.CompressFormat.JPEG, 75, baos);
            byte[] b = baos.toByteArray();
            imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("NewTaskConversation", "encodeTobase64 Exception : " + e.getMessage(), "WARN", null);
        }
        return imageEncoded;
    }

    @Override
    public void ResponceMethod(final Object object) {
        Log.i("checklist123", "ResponceMethod CheckList Saved Successfully====> ");

        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.i("taskresponse123", "NewTaskConversation ResponceMethod");
                CommunicationBean communicationBean = (CommunicationBean) object;
                cancelDialog();
                try {
                    final String server_Response_string = String.valueOf(communicationBean.getEnumJsonWebservicename());
                    Log.d("Task2", "Response Email" + server_Response_string);
                    String WebServiceEnum_Response = communicationBean.getFirstname();
                    Log.d("Task2", "name   == " + WebServiceEnum_Response);
                    final JSONObject jsonObject = new JSONObject(communicationBean.getEmail());

                    if (server_Response_string != null && server_Response_string.equalsIgnoreCase("saveCheckListDataDetails")) {
                        if (((String) jsonObject.get("result_text")).equalsIgnoreCase("saveCheckListDataDetails saved successfully")) {
                            Toast.makeText(CheckListActivity.this, "CheckList Saved Successfully....", Toast.LENGTH_SHORT).show();
                        } else {
                            String result = (String) jsonObject.get("result_text");
                            Toast.makeText(checkListContext, result, Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void cancelDialog() {
        try {
            if (progress != null && progress.isShowing()) {
                Log.i("register", "--progress bar end-----");
                Log.i("getTask123", "getTask cancelDialog*************");
                progress.dismiss();
                Appreference.isRequested_date = false;
                progress = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("NewTaskConversation", "cancelDialog Exception : " + e.getMessage(), "WARN", null);
            Log.i("getTask123", "getTask cancelDialog Exception*************" + e.getMessage());
        }

    }

    @Override
    public void ErrorMethod(Object object) {

    }

    public class CheckListItemAdapter extends ArrayAdapter<Label> {

        List<Label> arrayCheckList;
        LayoutInflater inflater = null;
        Context adapContext;
        Label mylist;


        public CheckListItemAdapter(Context context, List<Label> CheckListDetails) {

            super(context, R.layout.checklist_row_new, CheckListDetails);
            arrayCheckList = CheckListDetails;
            adapContext = context;
        }

        @Override
        public int getCount() {
            return arrayCheckList.size();
        }


        @Override
        public long getItemId(int position) {

            return position;
        }

        public int getItemViewType(int position) {
            int value = 0;

            return value;
        }

        @Override
        public View getView(final int position, View view, ViewGroup arg2) {
            View row = view;

            try {
                final ViewHolder holder;
                if (row == null) {
                    holder = new ViewHolder();
                    LayoutInflater inflater = (LayoutInflater) adapContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    row = inflater.inflate(R.layout.checklist_row_new, null, false);
                    holder.Description = (TextView) row.findViewById(R.id.desc_list);
                    holder.checklist_menu = (TextView) row.findViewById(R.id.checklist_popup);
                    holder.checklist_item = (TextView) row.findViewById(R.id.checklist_item);
                    holder.checklist_issue_type = (TextView) row.findViewById(R.id.checklist_issue);
                    holder.layoutcard = (TableLayout) row.findViewById(R.id.tab_card_view);
                    holder.tab_quantity = (TableLayout) row.findViewById(R.id.tab_quantity);
                    holder.checklist_parts_qty = (EditText) row.findViewById(R.id.checklist_parts_qty);
                    row.setTag(holder);
                } else {
                    holder = (ViewHolder) row.getTag();
                }
                final Label pBean = (Label) arrayCheckList.get(position);
                holder.Description.setText(pBean.getJobDescription());
                holder.checklist_item.setText(pBean.getItem());
                holder.checklist_issue_type.setText(pBean.getIssueType());

                if (isReadOnlyView) {
                    holder.checklist_parts_qty.setEnabled(false);
                } else {
                    holder.checklist_parts_qty.setEnabled(true);
                }
                holder.checklist_parts_qty.setTag(position);
                holder.checklist_parts_qty.setText(arrayCheckList.get(position).getQuantity());

                if (pBean.getJobstatus() != null && !pBean.getJobstatus().equalsIgnoreCase("")) {
                    holder.checklist_menu.setText(pBean.getJobstatus().toString());
                    holder.layoutcard.setBackgroundColor(getResources().getColor(R.color.checklist_blue));
                } else {
                    holder.checklist_menu.setText("");
                    holder.layoutcard.setBackgroundColor(getResources().getColor(R.color.white));
                }

                holder.checklist_parts_qty.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                        // TODO Auto-generated method stub
                    }

                    @Override
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                                  int arg3) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {
                        // TODO Auto-generated method stub
                        int pos = (int) holder.checklist_parts_qty.getTag();
                        arrayCheckList.get(pos).setQuantity(arg0.toString());

                    }
                });

                holder.checklist_menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final PopupMenu popup = new PopupMenu(checkListContext, v);
                        MenuInflater inflater = popup.getMenuInflater();
                        inflater.inflate(R.menu.checklist_popmenu, popup.getMenu());
                        if (!isReadOnlyView) {
                            popup.show();
                        }
                        mylist = new Label();
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.checklist_ok:
                                        holder.checklist_menu.setText(item.getTitle().toString());
                                        pBean.setJobstatus(item.getTitle().toString());
                                        holder.layoutcard.setBackgroundColor(getResources().getColor(R.color.checklist_blue));
                                        return true;
                                    case R.id.checklist_NotOk:
                                        holder.checklist_menu.setText(item.getTitle().toString());
                                        pBean.setJobstatus(item.getTitle().toString());
                                        holder.layoutcard.setBackgroundColor(getResources().getColor(R.color.checklist_blue));
                                        return true;
                                    case R.id.checklist_NA:
                                        holder.checklist_menu.setText(item.getTitle().toString());
                                        pBean.setJobstatus(item.getTitle().toString());
                                        holder.layoutcard.setBackgroundColor(getResources().getColor(R.color.checklist_blue));
                                        return true;
                                    case R.id.checklist_cleaned:
                                        holder.checklist_menu.setText(item.getTitle().toString());
                                        pBean.setJobstatus(item.getTitle().toString());
                                        holder.layoutcard.setBackgroundColor(getResources().getColor(R.color.checklist_blue));
                                        return true;
                                    case R.id.checklist_lubricated:
                                        holder.checklist_menu.setText(item.getTitle().toString());
                                        pBean.setJobstatus(item.getTitle().toString());
                                        holder.layoutcard.setBackgroundColor(getResources().getColor(R.color.checklist_blue));
                                        return true;
                                    case R.id.checklist_adjusted:
                                        holder.checklist_menu.setText(item.getTitle().toString());
                                        pBean.setJobstatus(item.getTitle().toString());
                                        holder.layoutcard.setBackgroundColor(getResources().getColor(R.color.checklist_blue));
                                        return true;
                                    case R.id.checklist_replaced:
                                        holder.checklist_menu.setText(item.getTitle().toString());
                                        pBean.setJobstatus(item.getTitle().toString());
                                        holder.layoutcard.setBackgroundColor(getResources().getColor(R.color.checklist_blue));
                                        return true;
                                    case R.id.checklist_topUp:
                                        holder.checklist_menu.setText(item.getTitle().toString());
                                        pBean.setJobstatus(item.getTitle().toString());
                                        holder.layoutcard.setBackgroundColor(getResources().getColor(R.color.checklist_blue));
                                        return true;
                                    case R.id.checklist_repaired:
                                        holder.checklist_menu.setText(item.getTitle().toString());
                                        pBean.setJobstatus(item.getTitle().toString());
                                        holder.layoutcard.setBackgroundColor(getResources().getColor(R.color.checklist_blue));
                                        return true;
                                    case R.id.checklist_parts:
                                        holder.checklist_menu.setText(item.getTitle().toString());
                                        pBean.setJobstatus(item.getTitle().toString());
                                        holder.layoutcard.setBackgroundColor(getResources().getColor(R.color.checklist_blue));
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
            return row;
        }


        public class ViewHolder {
            TextView Description;
            TextView checklist_menu;
            TextView checklist_issue_type;
            TextView checklist_item;
            TableLayout layoutcard;
            EditText checklist_parts_qty;
            TableLayout tab_quantity;
        }
    }
}

