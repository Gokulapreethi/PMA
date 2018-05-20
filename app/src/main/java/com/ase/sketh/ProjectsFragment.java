package com.ase.sketh;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.Appreference;
import com.ase.Bean.FSRResultBean;
import com.ase.Bean.FSRSearchResults;
import com.ase.Bean.ListallProjectBean;
import com.ase.Bean.ProjectDetailsBean;
import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;
import com.ase.DateTimePicker;
import com.ase.ImageLoader;
import com.ase.ListAllgetTaskDetails;
import com.ase.ProjectHistory;
import com.ase.R;
import com.ase.SearchMediaWebView;
import com.ase.projectStatusComparator;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lowagie.text.pdf.hyphenation.TernaryTree;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pjsip.pjsua2.app.MainActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import json.CommunicationBean;
import json.EnumJsonWebservicename;
import json.ListMember;
import json.WebServiceInterface;

import static android.graphics.Color.RED;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.rgb;

/**
 * Created by prasanth on 12/7/2016.
 */
public class ProjectsFragment extends Fragment implements View.OnClickListener, WebServiceInterface, DateTimePicker.DateWatcher {
    public View view;
    private SwipeMenuListView listview_project;
    TextView heading_project, exclation_counter, first_fsr, end_fsr, tna_middle, checklist_fsr;
    ImageView image_search, reportdetails, fsrDetails, PMS_checklist;
    public static Context classContext;
    static ProjectsFragment fragment;
    ProgressDialog progress;
    public ArrayList<ProjectDetailsBean> projectList, projectSearchList, filterbuddy;
    LinearLayout History_Search;
    EditText ProjectSearch;
    ImageView typeFilter;
    TextView NoResults, activity_start, activity_end;
    private ProjectFilter filter;
    private ProjectArrayAdapter projectArrayAdapter;
    Handler handler = new Handler();
    String project_id, project_name, parentTask_Id, groupuser_Id, project_owner, oracleProjectId;
    boolean isCurrentlyActivie = false;
    static ProjectsFragment projectsFragment;
    int tna_count;
    RelativeLayout all_report_title;
    Button submit_button;
    String TNAReportStart, TNAReportEnd;
    String User_selected_mcsrNo, User_selected_startDate, User_selected_endDate;
    ArrayList<String> ar_My_machineSerialNo, pms_projectList;

    public static ProjectsFragment getInstance() {
        return projectsFragment;
    }

    public static ProjectsFragment newInstance(int sectionNumber, Context context) {
        try {
            if (fragment == null) {
                Log.i("task", "checked int" + sectionNumber);
                fragment = new ProjectsFragment();
                Bundle args = new Bundle();
                classContext = context;
                fragment.setArguments(args);
                Appreference.isProject = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("ProjectFragment", "newInstance Exception : " + e.getMessage(), "WARN", null);
        }
        return fragment;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
            Log.i("LifeCycle", " projectFragment isVisibleToUser : " + isVisibleToUser);
            isCurrentlyActivie = isVisibleToUser;
            if (isVisibleToUser) {
                try {
                    if (getView() != null) {
                        hideKeyboard();
                        ProjectSearch.setText("");
                    }
                    if (getNetworkState()) {
                        try {
                            showprogress_1();
                            List<NameValuePair> tagNameValuePairs = new ArrayList<NameValuePair>();
                            tagNameValuePairs.add(new BasicNameValuePair("userId", String.valueOf(Appreference.loginuserdetails.getId())));
                            Log.i("ws123", "getAllJobDetails request");
                            Log.i("wsTime123", " WS getAllJobDetails Request sent Time====>" + Appreference.getCurrentDateTime());
                            Appreference.jsonRequestSender.getAllJobDetails(EnumJsonWebservicename.getAllJobDetails, tagNameValuePairs, this);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Appreference.printLog("ProjectFragment", "setUserVisibleHint getAllJobDetails Exception : " + e.getMessage(), "WARN", null);
                        }
                    } else {
                        Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                        notifyNewProjectReceived();
                        Log.i("listview", "adapter size ==> " + projectList.size());
                        Log.i("listview", "adapter size ==> " + projectSearchList.size());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("ProjectFragment", "setUserVisibleHint Exception : " + e.getMessage(), "WARN", null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("ProjectFragment", "setUserVisibleHint Exception : " + e.getMessage(), "WARN", null);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.project_fragment_layout, container, false);
        try {
            Appreference.context_table.put("projectfragment", this);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            History_Search = (LinearLayout) view.findViewById(R.id.History_Search);
            ProjectSearch = (EditText) view.findViewById(R.id.searchtext);
            Appreference.setlatestFilteredOption = 1;
            typeFilter = (ImageView) view.findViewById(R.id.my_filter_type);
            NoResults = (TextView) view.findViewById(R.id.Noresult);
            tna_middle = (TextView) view.findViewById(R.id.tna_middle);
            checklist_fsr = (TextView) view.findViewById(R.id.checklist_fsr);
            first_fsr = (TextView) view.findViewById(R.id.first_fsr);
            end_fsr = (TextView) view.findViewById(R.id.end_fsr);
            listview_project = (SwipeMenuListView) view.findViewById(R.id.listview_project);
            NoResults.setVisibility(View.GONE);
            heading_project = (TextView) view.findViewById(R.id.heading_project);
            image_search = (ImageView) view.findViewById(R.id.image_search);
            reportdetails = (ImageView) view.findViewById(R.id.reportdetails);
            fsrDetails = (ImageView) view.findViewById(R.id.fsrDetails);
            PMS_checklist = (ImageView) view.findViewById(R.id.PMS_checklist);
            reportdetails.setOnClickListener(this);
            fsrDetails.setOnClickListener(this);
            PMS_checklist.setOnClickListener(this);

            try {
                if (Appreference.loginuserdetails != null && Appreference.loginuserdetails.getRoleId() != null
                        && Appreference.loginuserdetails.getRoleId().equalsIgnoreCase("2")) {
                    image_search.setVisibility(View.VISIBLE);
                    reportdetails.setVisibility(View.VISIBLE);
                    fsrDetails.setVisibility(View.VISIBLE);
                    PMS_checklist.setVisibility(View.VISIBLE);

                    tna_middle.setVisibility(View.VISIBLE);
                    first_fsr.setVisibility(View.VISIBLE);
                    end_fsr.setVisibility(View.VISIBLE);
                    checklist_fsr.setVisibility(View.VISIBLE);
                } else {
                    image_search.setVisibility(View.GONE);
                    reportdetails.setVisibility(View.GONE);
                    fsrDetails.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) fsrDetails.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                    fsrDetails.setLayoutParams(params); //causes layout update
                    PMS_checklist.setVisibility(View.VISIBLE);
                    checklist_fsr.setVisibility(View.GONE);

                    first_fsr.setVisibility(View.GONE);
                    end_fsr.setVisibility(View.VISIBLE);
                    end_fsr.setText("FSR");
                    tna_middle.setVisibility(View.VISIBLE);
                    tna_middle.setText("checklist");


                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("ProjectFragment", "onCreateView icons Exception : " + e.getMessage(), "WARN", null);
            }


            try {
                Log.i("task", "project");
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                projectsFragment = this;
                exclation_counter = (TextView) view.findViewById(R.id.exclation_counter);

                activity_start = (TextView) view.findViewById(R.id.activity_start);
                activity_end = (TextView) view.findViewById(R.id.activity_end);
                submit_button = (Button) view.findViewById(R.id.submit_button);
                all_report_title = (RelativeLayout) view.findViewById(R.id.all_report_title);
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("ProjectFragment", "onCreateView softkeyListener Exception : " + e.getMessage(), "WARN", null);
            }

            try {
                String s = "select * from taskDetailsInfo where readStatus='1'";
                ArrayList<ProjectDetailsBean> projectDetailsBeen = VideoCallDataBase.getDB(getContext()).getExclationdetails(s);
                if (projectDetailsBeen.size() > 0)
                    exclation_counter.setVisibility(View.VISIBLE);
                else
                    exclation_counter.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("ProjectFragment", "onCreateView exclation_counter Exception : " + e.getMessage(), "WARN", null);
            }
            reportdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        hideKeyboard();
                        if (tna_count == 0) {
                            all_report_title.setVisibility(View.VISIBLE);
                            tna_count = 1;
                        } else {
                            all_report_title.setVisibility(View.GONE);
                            tna_count = 0;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("ProjectFragment", "reportdetails click Exception : " + e.getMessage(), "WARN", null);
                    }
                }
            });
            typeFilter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFilterPopupWindow(v);
                }
            });
            fsrDetails.setOnClickListener(new View.OnClickListener() {
                String fsr_jobId, fsr_date, selected_mcSrNo = "", search_startDate = "", search_endDate = "";

                @Override
                public void onClick(View v) {

                    try {
                        hideKeyboard();
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View alertLayout = inflater.inflate(R.layout.fsr_request_view, null);
//                    final Spinner machine_no_spinner = (Spinner) alertLayout.findViewById(R.id.machine_no_spinner);
                        final AutoCompleteTextView ac_machine_no_spinner = (AutoCompleteTextView) alertLayout.findViewById(R.id.auto_complete);
                    /*final Button fsr_start_btn = (Button) alertLayout.findViewById(R.id.fsr_start_btn);
                    final Button fsr_end_btn = (Button) alertLayout.findViewById(R.id.fsr_end_btn);*/
                        final LinearLayout ll_second_layout = (LinearLayout) alertLayout.findViewById(R.id.second_layout);
                        ll_second_layout.setVisibility(View.GONE);
                        final TextView tx_fst_start = (TextView) alertLayout.findViewById(R.id.fst_start);
                        final TextView tx_fst_end = (TextView) alertLayout.findViewById(R.id.fst_end);
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setTitle("FSR REPORT");

                        // this is set the view from XML inside AlertDialog
                        alert.setView(alertLayout);
                        alert.setPositiveButton("Submit", null);
                        alert.setNegativeButton("Cancel", null);
                        // disallow cancel of AlertDialog on click of back button and outside touch
                        alert.setCancelable(false);
                        ar_My_machineSerialNo = new ArrayList<>();
                        ar_My_machineSerialNo.clear();
                    /*getting list of jobcards for the user from table*/
                        String list_query = "select *,cast(mcSrNo as unsigned) as t from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "' order by t DESC";
                        ar_My_machineSerialNo = VideoCallDataBase.getDB(getActivity()).getOracleProjectIdlist(list_query, "mcSrNo");
//                    Collections.sort(ar_My_machineSerialNo);

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, ar_My_machineSerialNo) {

                            @Override
                            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;
                                if (position == 0) {
                                    // Set the hint text color gray
                                    tv.setTextColor(Color.GRAY);
                                } else {
                                    tv.setTextColor(Color.BLACK);
                                }
                                return view;
                            }
                        };
                        // Drop down layout style - list view with radio button
//                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        ac_machine_no_spinner.setThreshold(1);
                        // attaching data adapter to spinner
                        ac_machine_no_spinner.setAdapter(dataAdapter);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        final String dateTime = dateFormat.format(new Date());
                        search_startDate = "";
                        search_endDate = "";
                        selected_mcSrNo = "";
                        ac_machine_no_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                                    String selectedItemText = (String) parent.getItemAtPosition(position);
                                    Log.i("FSR", "ac_machine_no_spinner selectedItemText ==> " + selectedItemText);
                                    Log.i("FSR", "String.valueOf(ac_machine_no_spinner.getAdapter().getItem(position)) ==> " + String.valueOf(ac_machine_no_spinner.getAdapter().getItem(position)));
                                    /*selected_mcSrNo = String.valueOf(ac_machine_no_spinner.getAdapter().getItem(position));*//*selected jobcard from spinner*//*
                                    User_selected_mcsrNo = selected_mcSrNo;*/


                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("ProjectFragment", "fsrDetails ac_machine_no_spinner Exception : " + e.getMessage(), "WARN", null);
                                }
                            }
                        });

                        tx_fst_start.setOnClickListener(new View.OnClickListener() {
                            final Calendar c = Calendar.getInstance();

                            @Override
                            public void onClick(View v) {
                                try {
                                    if (ac_machine_no_spinner.getText() != null) {
                                        selected_mcSrNo = ac_machine_no_spinner.getText().toString();/*selected jobcard from spinner*/
                                        Log.i("FSR", "selected_mcSrNo==> " + selected_mcSrNo);
                                        User_selected_mcsrNo = selected_mcSrNo;
                                    }
                                    if (selected_mcSrNo != null && !selected_mcSrNo.equalsIgnoreCase("") && !selected_mcSrNo.equalsIgnoreCase(null)) {
                                        hideKeyboard_fsr(ac_machine_no_spinner);
//                                        hideKeyboard();
                                        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
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
                                                        String completedate_display = year + "-" + months + "-" + days;
                                                        search_startDate = completedate_display + " " + "00:00:00";
                                                        Log.i("FSR", "completedate_display ==> " + completedate_display);
                                                        Log.i("FSR", "dateTime ==> " + completedate_display.compareTo(dateTime));

                                                        if (completedate_display.compareTo(dateTime) <= 0) {
                                                            //
                                                            tx_fst_start.setText(completedate_display);
                                                            User_selected_startDate = completedate_display;
                                                            //
                                                        } else {
                                                            Toast.makeText(ProjectsFragment.classContext, "Dont pick future date", Toast.LENGTH_SHORT).show();
                                                            Log.i("FSR", "tx_fst_start ==> " + tx_fst_start.getText().toString());
                                                            search_startDate = tx_fst_start.getText().toString();
                                                            User_selected_startDate = tx_fst_start.getText().toString();
                                                        }
                                                    }
                                                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                                        dpd.show();
                                    } else {
                                        Toast.makeText(getActivity(), "Please set Machine Serial No ", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("ProjectFragment", "fsrDetails tx_fst_start Exception : " + e.getMessage(), "WARN", null);
                                }
                            }
                        });
                        tx_fst_end.setOnClickListener(new View.OnClickListener() {
                            final Calendar c = Calendar.getInstance();

                            @Override
                            public void onClick(View v) {
                                if (search_startDate != null && !search_startDate.equalsIgnoreCase("") && !search_startDate.equalsIgnoreCase(null)) {
                                    try {
                                        hideKeyboard_fsr(ac_machine_no_spinner);
//                                        hideKeyboard();
                                        DatePickerDialog dpd = new DatePickerDialog(getActivity(),
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
                                                        String completedate_display = year + "-" + months + "-" + days;
                                                        search_endDate = completedate_display + " " + "23:59:59";

                                                        Log.i("FSR", "completedate_display ==> # " + completedate_display);
                                                        Log.i("FSR", "dateTime ==> # " + completedate_display.compareTo(dateTime));

                                                        if (completedate_display.compareTo(dateTime) <= 0) {
                                                            String start_date = search_startDate.split(" ")[0];
                                                            Log.i("FSR", "start_date ==> " + start_date);
                                                            Log.i("FSR", "diff ==> " + completedate_display.compareTo(start_date));
                                                            if (completedate_display.compareTo(start_date) >= 0) {
                                                                tx_fst_end.setText(completedate_display);
                                                                User_selected_endDate = completedate_display;
                                                            } else {
                                                                Toast.makeText(ProjectsFragment.classContext, "Please select end date after start date ", Toast.LENGTH_SHORT).show();
                                                                Log.i("FSR", "tx_fst_end ==> " + tx_fst_end.getText().toString());
                                                                search_endDate = tx_fst_end.getText().toString();
                                                                User_selected_endDate = tx_fst_end.getText().toString();
                                                            }
                                                        } else {
                                                            Toast.makeText(ProjectsFragment.classContext, "Dont pick future date", Toast.LENGTH_SHORT).show();
                                                            Log.i("FSR", "tx_fst_end ==> " + tx_fst_end.getText().toString());
                                                            search_endDate = tx_fst_end.getText().toString();
                                                            User_selected_endDate = tx_fst_end.getText().toString();
                                                        }

                                                    }
                                                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                                        dpd.show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Appreference.printLog("ProjectFragment", "fsrDetails tx_fst_end Exception : " + e.getMessage(), "WARN", null);
                                    }
                                } else {
                                    Toast.makeText(ProjectsFragment.classContext, "Please select start date ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        final AlertDialog mAlertDialog = alert.create();
                        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                            @Override
                            public void onShow(final DialogInterface dialog) {

                                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                b.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        // TODO Do something
                                        try {
                                            hideKeyboard_fsr(ac_machine_no_spinner);
//                                            hideKeyboard();
                                            if (selected_mcSrNo != null && !selected_mcSrNo.equalsIgnoreCase("") && !selected_mcSrNo.equalsIgnoreCase(null)
                                                    && search_startDate != null && !search_startDate.equalsIgnoreCase("") && !search_startDate.equalsIgnoreCase(null)
                                                    && search_endDate != null && !search_endDate.equalsIgnoreCase("") && !search_endDate.equalsIgnoreCase(null)) {
                                                if (search_startDate.compareTo(search_endDate) <= 0) {
                                                    dialog.dismiss();
                                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                    SimpleDateFormat dateParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                                                    Date date = null;
                                                    Date date1 = null;
                                                    String FSRStartDateUTC = "";
                                                    String FSREndDateUTC = "";

                                                    if (search_startDate != null && !search_startDate.equalsIgnoreCase("")) {
                                                        try {
                                                            date = dateParse.parse(search_startDate);
                                                            FSRStartDateUTC = dateFormat.format(date);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            Appreference.printLog("ProjectFragment", "fsrDetails search_startDate Exception : " + e.getMessage(), "WARN", null);
                                                        }
                                                    }
                                                    if (search_endDate != null && !search_endDate.equalsIgnoreCase("")) {
                                                        try {
                                                            date1 = dateParse.parse(search_endDate);
                                                            FSREndDateUTC = dateFormat.format(date1);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            Appreference.printLog("ProjectFragment", "fsrDetails search_endDate Exception : " + e.getMessage(), "WARN", null);
                                                        }
                                                    }
                                                    try {
                                                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                                                        nameValuePairs.add(new BasicNameValuePair("mcSrNo", selected_mcSrNo));
                                                        nameValuePairs.add(new BasicNameValuePair("taskCompletedStartDate", FSRStartDateUTC));
                                                        nameValuePairs.add(new BasicNameValuePair("taskCompletedEndDate", FSREndDateUTC));
                                                        showprogress("searching....");
                                                        Appreference.jsonRequestSender.OraclefieldServiceSearch(EnumJsonWebservicename.fieldServiceSearch, nameValuePairs, ProjectsFragment.this);
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                        Appreference.printLog("ProjectFragment", "OraclefieldServiceSearch Exception : " + e.getMessage(), "WARN", null);
                                                    }
                                                } else {
                                                    Toast.makeText(getActivity(), "Please pick end date after start date", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(getActivity(), "Please Fill All values", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Appreference.printLog("ProjectFragment", "fsrDetails mAlertDialog_showListener Exception : " + e.getMessage(), "WARN", null);
                                        }

                                    }
                                });
                                Button a = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                                a.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        // TODO Do something
                                        hideKeyboard_fsr(ac_machine_no_spinner);
//                                        hideKeyboard();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                        if (!mAlertDialog.isShowing()) {
                            mAlertDialog.show();
                        }
//                    mAlertDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("ProjectFragment", "fsrDetails onclick Exception : " + e.getMessage(), "WARN", null);
                    }
                }
            });


            PMS_checklist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        hideKeyboard();
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View alertLayout = inflater.inflate(R.layout.checklist_request_view, null);
                        final AutoCompleteTextView checklist_spinner = (AutoCompleteTextView) alertLayout.findViewById(R.id.auto_complete_pms);

                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setTitle("CHECKLIST REPORT");

                        // this is set the view from XML inside AlertDialog
                        alert.setView(alertLayout);
                        alert.setPositiveButton("Submit", null);
                        alert.setNegativeButton("Cancel", null);
                        // disallow cancel of AlertDialog on click of back button and outside touch
                        alert.setCancelable(false);
                        pms_projectList = new ArrayList<>();
                        pms_projectList.clear();
                    /*getting list of jobcards for the user from table*/
                        String PMSServiceType_query = "select * from projectDetails where loginuser ='" + Appreference.loginuserdetails.getEmail() + "' and isActiveStatus='0'";
                        pms_projectList = VideoCallDataBase.getDB(getActivity()).getOracleProjectIdlist(PMSServiceType_query, "oracleProjectId");

//                    Collections.sort(ar_My_machineSerialNo);

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, pms_projectList) {

                            @Override
                            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;
                                if (position == 0) {
                                    // Set the hint text color gray
                                    tv.setTextColor(Color.GRAY);
                                } else {
                                    tv.setTextColor(Color.BLACK);
                                }
                                return view;
                            }
                        };
                        // Drop down layout style - list view with radio button
//                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        checklist_spinner.setThreshold(1);
                        // attaching data adapter to spinner
                        checklist_spinner.setAdapter(dataAdapter);

                        checklist_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                try {
                                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                                    String selectedItemText = (String) parent.getItemAtPosition(position);
                                    Log.i("FSR", "ac_machine_no_spinner selectedItemText ==> " + selectedItemText);
                                    Log.i("FSR", "String.valueOf(ac_machine_no_spinner.getAdapter().getItem(position)) ==> " + String.valueOf(checklist_spinner.getAdapter().getItem(position)));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("ProjectFragment", "checkList checklist_spinner Exception : " + e.getMessage(), "WARN", null);
                                }
                            }
                        });
                        final AlertDialog mAlertDialog = alert.create();
                        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                            @Override
                            public void onShow(final DialogInterface dialog) {

                                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                b.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        // TODO Do something
                                        try {
                                            if (getNetworkState()) {
                                                try {
                                                    showprogress("Getting checkList...");
                                                    List<NameValuePair> tagNameValuePairs = new ArrayList<NameValuePair>();
                                                    tagNameValuePairs.add(new BasicNameValuePair("jobCardNumber", checklist_spinner.getText().toString()));
                                                    Appreference.jsonRequestSender.checkListReport(EnumJsonWebservicename.checkListReport, tagNameValuePairs, ProjectsFragment.this);
                                                    dialog.dismiss();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    Appreference.printLog("ProjectFragment", "setUserVisibleHint getAllJobDetails Exception : " + e.getMessage(), "WARN", null);
                                                }
                                            } else {
                                                Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Appreference.printLog("ProjectFragment", "checkList mAlertDialog_showListener Exception : " + e.getMessage(), "WARN", null);
                                        }

                                    }
                                });
                                Button a = mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                                a.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {
                                        // TODO Do something
                                        hideKeyboard_fsr(checklist_spinner);
//                                        hideKeyboard();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                        if (!mAlertDialog.isShowing()) {
                            mAlertDialog.show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("ProjectFragment", "checkList onclick Exception : " + e.getMessage(), "WARN", null);
                    }

                }
            });


            activity_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();

                    DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    try {
                                        String curr_date = null;
                                        try {
                                            SimpleDateFormat simpleDateFormat_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                                        String start_date = year + "-" + months + "-" + days;
                                        Log.i("TNA", "start_date---> " + start_date);
                                        Log.i("TNA", "curr_date---> " + curr_date);
                                        if (curr_date != null && curr_date.compareTo(start_date) < 0) {
                                            Toast.makeText(getActivity(), "Kindly select current date ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            activity_start.setText(start_date);
                                            TNAReportStart = start_date + " " + "00:00:00";
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

            activity_end.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();

                    DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    try {
                                        String cur_date = null;
                                        try {
                                            SimpleDateFormat simpleDateFormat_1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            cur_date = simpleDateFormat_1.format(new Date());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Appreference.printLog("ProjectFragment", "activity_end cur_date Exception : " + e.getMessage(), "WARN", null);
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
                                        String end_date = year + "-" + months + "-" + days;
                                        if (TNAReportStart != null && !TNAReportStart.equalsIgnoreCase("")) {
                                            String TnaStart[] = TNAReportStart.split(" ");
                                            String ReportStart = TnaStart[0];

                                            if ((end_date.compareTo(ReportStart) == 0) || ((cur_date.compareTo(end_date) >= 0) && (end_date.compareTo(ReportStart) > 0))) {
                                                activity_end.setText(end_date);
                                                TNAReportEnd = end_date + " " + "23:59:59";
                                            } else {
                                                Toast.makeText(getActivity(), "Kindly select above start date and below current date", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "Please select start date", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Appreference.printLog("ProjectFragment", "activity_end TNAReportEnd Exception : " + e.getMessage(), "WARN", null);
                                    }
                                }
                            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                    dpd.show();

                }
            });
            submit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Log.i("report123", "-====>startdATE====>" + TNAReportStart);
                        Log.i("report123", "-====>eNDdATE====>" + TNAReportEnd);
                        all_report_title.setVisibility(View.GONE);
                        tna_count = 0;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat dateParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                        Date date = null;
                        Date date1 = null;
                        String StartDateUTC = "", EndDateUTC = "";
                        if (TNAReportStart != null && !TNAReportStart.equalsIgnoreCase("")) {
                            try {
                                date = dateParse.parse(TNAReportStart);
                                StartDateUTC = dateFormat.format(date);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "submit_button TNAReportStart Exception : " + e.getMessage(), "WARN", null);
                            }
                        }
                        if (TNAReportEnd != null && !TNAReportEnd.equalsIgnoreCase("")) {
                            try {
                                date1 = dateParse.parse(TNAReportEnd);
                                EndDateUTC = dateFormat.format(date1);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "submit_button TNAReportEnd Exception : " + e.getMessage(), "WARN", null);
                            }
                        }
                        Log.i("report123", "-====>startdATE UTC====>" + StartDateUTC);
                        Log.i("report123", "-====>eNDdATE UTC====>" + EndDateUTC);
                        if ((date != null && date1 != null && !date.after(date1)) || (date.compareTo(date1) == 0)) {
                            try {
                                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                                nameValuePairs.add(new BasicNameValuePair("userId", String.valueOf(Appreference.loginuserdetails.getId())));
                                nameValuePairs.add(new BasicNameValuePair("travelStartDate", StartDateUTC));
                                nameValuePairs.add(new BasicNameValuePair("travelEndDate", EndDateUTC));
                                showprogress("Downloading....");
                                Appreference.jsonRequestSender.OracleTNAJobReport(EnumJsonWebservicename.tnaReportForDateWise, nameValuePairs, ProjectsFragment.this);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "submit_button OracleTNAJobReport Exception : " + e.getMessage(), "WARN", null);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please Enter correct Start/End Date", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("ProjectFragment", "submit_button Exception : " + e.getMessage(), "WARN", null);
                    }
                }
            });
            final SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {
                    switch (menu.getViewType()) {

                        case 1:
                            // create "open" item
                            SwipeMenuItem openItem = new SwipeMenuItem(
                                    classContext.getApplicationContext());
                            // set item background
                            openItem.setBackground(new ColorDrawable(rgb(0xff, 0x91,
                                    0x11)));
                            // set item width
                            openItem.setWidth(dp2px(90));
                            // set item title
                            openItem.setTitle("Abandon");
                            // set item title fontsize
                            openItem.setTitleSize(18);
                            // set item title font color
                            openItem.setTitleColor(WHITE);
                            // add to menu
                            menu.addMenuItem(openItem);
                            break;
                        case 0:
                            break;
                        case 2:
                            SwipeMenuItem openItem2 = new SwipeMenuItem(
                                    classContext.getApplicationContext());
                            // set item background
                            openItem2.setBackground(new ColorDrawable(rgb(0x00, 0xff,
                                    0x11)));
                            // set item width
                            openItem2.setWidth(dp2px(90));
                            // set item title
                            openItem2.setTitle("Assign");
                            // set item title fontsize
                            openItem2.setTitleSize(18);
                            // set item title font color
                            openItem2.setTitleColor(WHITE);
                            // add to menu
                            menu.addMenuItem(openItem2);
                            break;

                        case 3:
                            // create "open" item
                            SwipeMenuItem openItem1 = new SwipeMenuItem(
                                    classContext.getApplicationContext());
                            // set item background
                            openItem1.setBackground(new ColorDrawable(rgb(0x00, 0xff,
                                    0x11)));
                            // set item width
                            openItem1.setWidth(dp2px(90));
                            // set item title
                            openItem1.setTitle("Activate");
                            // set item title fontsize
                            openItem1.setTitleSize(18);
                            // set item title font color
                            openItem1.setTitleColor(WHITE);
                            // add to menu
                            menu.addMenuItem(openItem1);

                            break;

                    }
                }
            };
            listview_project.setMenuCreator(creator);
            listview_project.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

            listview_project.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                    try {
                        ProjectDetailsBean projectDetailsBean = projectList.get(position);
                        if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("Activate")) {

                        } else if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("Abandon")) {
                        } else {
                            Log.i("task", "cance1 ");
                            projectList.remove(position);
                            Log.i("task", "cance1 ! ");

                            Log.i("task", "cance1 !! " + projectDetailsBean.getId());
                            projectSearchList.remove(position);
                            projectArrayAdapter.notifyDataSetChanged();
                            //                            String query1 = "delete from projectDetails where('" + projectDetailsBean.getId() + "'= projectId ) and loginuser='" + Appreference.loginuserdetails.getEmail() + "';";
                            VideoCallDataBase.getDB(classContext.getApplicationContext()).deleteProjects(projectDetailsBean.getId());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("ProjectFragment", "listview_project menuItem Exception : " + e.getMessage(), "WARN", null);
                    }
                    return false;
                }
            });

            image_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        hideKeyboard();
                        Intent intent = new Intent(getActivity(), SearchMediaWebView.class);
                        intent.putExtra("urlload", "searchmedia");
                        startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.right_anim, R.anim.left_anim);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("ProjectFragment", "image_search Exception : " + e.getMessage(), "WARN", null);
                    }

                }
            });

            ProjectSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(final CharSequence s, int start, int before, final int count) {
                    ProjectSearch.setCursorVisible(true);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] value = new String[count];
                            try {
                                Log.d("addTextChangedListener", "onTextChanged " + s.length());
                                Log.d("constraint", "JNDSEJBJW  * " + s);
                                //                buddyArrayAdapter.getFilter().filter(s);
                                if (s != null && s.length() > 0) {
                                    Log.d("addTextChangedListener", "onTextChanged if " + s.length());
                                    ProjectsFragment.this.projectArrayAdapter.getFilter().filter(s);
                                } else {
                                    Log.d("addTextChangedListener", "onTextChanged else " + s.length());
                                    notifyNewProjectReceived();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "ProjectSearch TextchangeListener Exception : " + e.getMessage(), "WARN", null);
                            }
                        }
                    });
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    ProjectSearch.setCursorVisible(false);
                }

            });


            listview_project.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        Log.i("progress123", "inside listview_project OnClick--------->");
                        Log.i("progress123", "===============================> ");

                        ProjectSearch.setText("");
                        hideKeyboard();
                        ProjectDetailsBean projectDetailsBean = projectList.get(position);
                        project_id = projectDetailsBean.getId();
                        project_name = projectDetailsBean.getProjectName();
                        //                parentTask_Id = projectDetailsBean.getParentTaskId();
                        project_owner = projectDetailsBean.getProject_ownerName();
                        oracleProjectId = projectDetailsBean.getOracleProjectId();
                        groupuser_Id = projectDetailsBean.getToUserId();

                        if (getNetworkState()) {
                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                            nameValuePairs.add(new BasicNameValuePair("projectId", projectDetailsBean.getId()));
                            nameValuePairs.add(new BasicNameValuePair("userId", String.valueOf(Appreference.loginuserdetails.getId())));
//                        Appreference.jsonRequestSender.getProject(EnumJsonWebservicename.getProject, nameValuePairs, ProjectsFragment.this);
                            Log.i("ws123", " projectDetailsBean.getOracleProjectId()=========>" + projectDetailsBean.getId());
                            Log.i("ws123", " projectDetailsBean.userId()=========>" + String.valueOf(Appreference.loginuserdetails.getId()));
                            showprogress("Getting ActivityCode...");
                            try {
                                Log.i("wsTime123", " Ws getTaskForJobID Request sent====>" + Appreference.getCurrentDateTime());
                                Appreference.jsonRequestSender.getTaskForJobID(EnumJsonWebservicename.getTaskForJobID, nameValuePairs, ProjectsFragment.this);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "listview_project getTaskForJobID Exception : " + e.getMessage(), "WARN", null);
                            }
                        } else {
                            try {
                                Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), ProjectHistory.class);
                                intent.putExtra("projectId", project_id);
                                intent.putExtra("projectName", project_name);
                                //                intent.putExtra("parentTaskId", parentTask_Id);
                                if (Appreference.isPauseStartFrom) {
                                    Appreference.isPauseStartFrom = false;
                                    intent.putExtra("isFromNewTaskConv", true);
                                } else {
                                    if (Appreference.context_table.containsKey("projecthistory")) {
                                        Appreference.context_table.remove("projecthistory");
                                    }
                                    intent.putExtra("isFromNewTaskConv", false);
                                }
                                intent.putExtra("projectOwner", project_owner);
                                intent.putExtra("groupUserId", groupuser_Id);
                                if (oracleProjectId != null)
                                    intent.putExtra("fromOracle", true);
                                else
                                    intent.putExtra("fromOracle", false);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.right_anim, R.anim.left_anim);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "listview_project ProjectHistory Exception : " + e.getMessage(), "WARN", null);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("ProjectFragment", "listview_project ItemClick Exception : " + e.getMessage(), "WARN", null);
                    }
                }
            });
            Log.i("task", "checkedProject" + Appreference.isProject);
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("ProjectFragment", "onCreateView Exception : " + e.getMessage(), "WARN", null);
        }
        return view;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDateChanged(Calendar c) {

    }


    public class ProjectArrayAdapter extends ArrayAdapter<ProjectDetailsBean> {

        ArrayList<ProjectDetailsBean> arrayBuddyList;
        LayoutInflater inflater = null;
        Context adapContext;
        ImageLoader imageLoader1;

        public ProjectArrayAdapter(Context context, ArrayList<ProjectDetailsBean> buddyList1) {
            super(context, R.layout.project_history_row, buddyList1);
            // TODO Auto-generated constructor stub
            arrayBuddyList = buddyList1;
            adapContext = context;
            imageLoader1 = new ImageLoader(adapContext);
        }

        @Override
        public int getCount() {
            return arrayBuddyList.size();
        }

        public ProjectDetailsBean getItem(int position) {
            return projectList.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        public int getItemViewType(int position) {
            int value = 0;

            return value;
        }

        public View getView(final int pos, View conView, ViewGroup group) {
            try {

                if (conView == null) {
                    inflater = (LayoutInflater) adapContext
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    conView = inflater.inflate(R.layout.project_details_row, null,
                            false);
                }

                final View finalConView = conView;

                final ProjectDetailsBean projectDetailsBean = arrayBuddyList.get(pos);
                LinearLayout proj_layout = (LinearLayout) finalConView.findViewById(R.id.project_layout);
                TextView project_name = (TextView) finalConView.findViewById(R.id.project_name);
                TextView project_id = (TextView) finalConView.findViewById(R.id.project_id);
                TextView task_giver = (TextView) finalConView.findViewById(R.id.task_giver);
                TextView msg_count = (TextView) finalConView.findViewById(R.id.item_counter);
                TextView job_date = (TextView) finalConView.findViewById(R.id.job_date);
                LinearLayout layoutcard = (LinearLayout) finalConView.findViewById(R.id.layoutcardview);
                View viewforparent = (View) finalConView.findViewById(R.id.viewforparent);
                final ImageView completed_status = (ImageView) finalConView.findViewById(R.id.completed_status);
                viewforparent.setVisibility(View.GONE);
                completed_status.setVisibility(View.GONE);

                try {
                    if (projectDetailsBean.getIsActiveStatus() != null && projectDetailsBean.getIsActiveStatus().equalsIgnoreCase("1")) {
                        //IsActive status : 1 its a Monthly JobCard
                        layoutcard.setBackgroundResource(R.color.appcolor);
                        project_id.setTextColor(getResources().getColor(android.R.color.white));
                        project_name.setTextColor(getResources().getColor(android.R.color.white));
                        task_giver.setTextColor(getResources().getColor(android.R.color.white));
                        job_date.setTextColor(getResources().getColor(android.R.color.white));
                    } else if (projectDetailsBean.getIsActiveStatus() != null && projectDetailsBean.getIsActiveStatus().equalsIgnoreCase("0")) {
                        //IsActive status : 0 its a PMS JobCard
                        layoutcard.setBackgroundResource(R.color.yellownew);
                        project_id.setTextColor(getResources().getColor(android.R.color.white));
                        project_name.setTextColor(getResources().getColor(android.R.color.white));
                        task_giver.setTextColor(getResources().getColor(android.R.color.white));
                        job_date.setTextColor(getResources().getColor(android.R.color.white));
                    } else {
                        //IsActive status : null  its a Normal JobCard
                        layoutcard.setBackgroundResource(R.color.white);
                        project_id.setTextColor(getResources().getColor(android.R.color.black));
                        project_name.setTextColor(getResources().getColor(android.R.color.black));
                        task_giver.setTextColor(getResources().getColor(android.R.color.black));
                        job_date.setTextColor(getResources().getColor(android.R.color.black));
                    }

                    project_id.setText("Job Card Number : " + projectDetailsBean.getOracleProjectId());
                    project_name.setText("Customer Name   : " + projectDetailsBean.getCustomerName());
                    task_giver.setText("Description            : " + projectDetailsBean.getJobDescription());
                    job_date.setText("Date                         : " + projectDetailsBean.getOpenDate());
                    String pjt_owner = null;
                    Log.i("Fragment", "projectDetailsBean getProject_ownerName() " + projectDetailsBean.getProject_ownerName());
                    if (projectDetailsBean.getProject_ownerName() != null && projectDetailsBean.getProject_ownerName().contains("@")) {
                        pjt_owner = projectDetailsBean.getProject_ownerName().replace("@", "_");
                    } else {
                        pjt_owner = projectDetailsBean.getProject_ownerName();
                    }
                    Log.i("Fragment", "pjt_owner ==> " + pjt_owner);
                    if (pjt_owner != null && pjt_owner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())
                            || Appreference.loginuserdetails != null && Appreference.loginuserdetails.getRoleId() != null
                            && Appreference.loginuserdetails.getRoleId().equalsIgnoreCase("2")) {
                        completed_status.setVisibility(View.VISIBLE);
                    }
                    if (pjt_owner != null && pjt_owner.equalsIgnoreCase(Appreference.loginuserdetails.getUsername())) {
                        pjt_owner = "Project Owner : Me";
                    } else {
                        pjt_owner = VideoCallDataBase.getDB(classContext).getname(pjt_owner);
                        if (pjt_owner != null) {
                            pjt_owner = "Project Owner : " + pjt_owner;
                        } else {
                            pjt_owner = "Project Owner : " + projectDetailsBean.getProject_ownerName();
                        }
                    }

                    int project_unReadMsg_count = VideoCallDataBase.getDB(classContext).getProjectsUnReadMsgCount(projectDetailsBean.getId());
                    if (project_unReadMsg_count == 0) {
                        Log.d("ProjectHistory", "unRead_project_count is 0");
                        msg_count.setVisibility(View.GONE);
                    } else {
                        Log.i("ProjectHistory", "unRead_project_count is " + project_unReadMsg_count);
                        msg_count.setVisibility(View.VISIBLE);
                        msg_count.setText(String.valueOf(project_unReadMsg_count));
                    }
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    Appreference.printLog("ProjectFragment", "ProjectArrayAdapter icons Exception : " + e.getMessage(), "WARN", null);
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("ProjectFragment", "ProjectArrayAdapter icons Exception : " + e.getMessage(), "WARN", null);
                }
                completed_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(getActivity(), completed_status);
                        popup.getMenuInflater().inflate(R.menu.project_pop_menu, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
/*
                                if (item.getTitle().toString().equalsIgnoreCase("FSR Report")) {
                                    try {
                                        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                                        nameValuePairs.add(new BasicNameValuePair("projectId", projectDetailsBean.getId()));
                                        showprogress("Downloading...");
                                        Appreference.jsonRequestSender.OracleFSRJOBReport(EnumJsonWebservicename.fieldServiceReportJobWise, nameValuePairs, ProjectsFragment.this);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Appreference.printLog("ProjectFragment", "ProjectArrayAdapter OracleFSRJOBReport Exception : " + e.getMessage(), "WARN", null);
                                    }

                                } else */
                                if (item.getTitle().toString().equalsIgnoreCase("Complete")) {
                                    try {
                                        AlertDialog.Builder saveDialog = new AlertDialog.Builder(getActivity());
                                        saveDialog.setTitle("Project Completion");
                                        saveDialog.setMessage("Are you sure want to Complete this Project?");
                                        saveDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                try {
                                                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                                                    nameValuePairs.add(new BasicNameValuePair("projectId", projectDetailsBean.getId()));
                                                    nameValuePairs.add(new BasicNameValuePair("userId", String.valueOf(Appreference.loginuserdetails.getId())));
                                                    Log.i("completed_status", "ProjectId()====>" + projectDetailsBean.getId());
                                                    Log.i("completed_status", " userId()=====>" + String.valueOf(Appreference.loginuserdetails.getId()));
                                                    showprogress("Please wait...");
                                                    Appreference.jsonRequestSender.projectCompleted(EnumJsonWebservicename.projectCompleted, nameValuePairs, ProjectsFragment.this);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    Appreference.printLog("ProjectFragment", "ProjectArrayAdapter projectCompleted Exception : " + e.getMessage(), "WARN", null);
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
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Appreference.printLog("ProjectFragment", "ProjectArrayAdapter cancel Exception : " + e.getMessage(), "WARN", null);
                                    }
                                }
                                return true;
                            }
                        });
                        popup.show();
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("ProjectFragment", "ProjectArrayAdapter getView Exception : " + e.getMessage(), "WARN", null);
            }
            return conView;
        }


        @Override
        public Filter getFilter() {
            try {
                if (filter == null) {
                    Log.d("constraint", "JNDSEJBJW  ** ");
                    filter = new ProjectFilter();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("ProjectFragment", "getFilter Exception : " + e.getMessage(), "WARN", null);
            }
            return filter;
        }
    }

    private class ProjectFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            //constraint = constraint.toString();
            FilterResults result = null;
            try {
                String s1 = constraint.toString().toLowerCase();

                Log.d("constraint", "filter : 0 " + s1);

                result = new FilterResults();
                if (constraint != null && constraint.toString().length() > 0) {
                    Log.i(" constraint ", "  1 " + constraint.toString().length());
                    ArrayList<ProjectDetailsBean> taskdetailsbeanlist = new ArrayList<>();
                    ArrayList<ProjectDetailsBean> s = new ArrayList<ProjectDetailsBean>();

                    for (int i = 0, l = projectSearchList.size(); i < l; i++) {
                        Log.i("constraint", "insidefor 2 " + projectSearchList.size());
                        Log.i("check123", "insidefor 2 " + projectSearchList.get(i).getJobDescription());

                        try {
//                            if (Appreference.setlatestFilteredOption == 3 && !(projectSearchList.get(i).getJobDescription()).equalsIgnoreCase(null)
//                                    && !(projectSearchList.get(i).getJobDescription()).equalsIgnoreCase("null")
//                                    && !(projectSearchList.get(i).getJobDescription()).equalsIgnoreCase("")) {
//                                s.add(projectSearchList.get(i));
//                            } else {
//                                if (Appreference.setlatestFilteredOption != 3) {
//                                    s.add(projectSearchList.get(i));
//                                }
//                            }
                            s.add(projectSearchList.get(i));

                            if (Appreference.setlatestFilteredOption == 1) {
                                String s2 = s.get(i).getProjectName().toString().toLowerCase();

                                if (s2.contains(s1)) {
                                    Log.i("constraint", "insideif 5 " + String.valueOf(constraint));
                                    taskdetailsbeanlist.add(s.get(i));
                                    Log.i("constraint", "List 6 " + taskdetailsbeanlist.toString());
                                }
                            } else if (Appreference.setlatestFilteredOption == 2) {
                                String s2 = s.get(i).getCustomerName().toString().toLowerCase();

                                if (s2.contains(s1)) {
                                    Log.i("constraint", "insideif 5 " + String.valueOf(constraint));
                                    taskdetailsbeanlist.add(s.get(i));
                                    Log.i("constraint", "List 6 " + taskdetailsbeanlist.toString());
                                }
                            } else if (Appreference.setlatestFilteredOption == 3 && !s.get(i).getJobDescription().toString().equalsIgnoreCase(null) && s.get(i).getJobDescription().toString() != null && s.get(i).getJobDescription().toString() != "") {
                                String s2 = s.get(i).getJobDescription().toString().toLowerCase();

                                Log.i("check123", "Description---->  " + i + "===========>" + s2);
                                if (s2.contains(s1)) {
                                    Log.i("constraint", "insideif 5 " + String.valueOf(constraint));
                                    taskdetailsbeanlist.add(s.get(i));
                                    Log.i("constraint", "List 6 " + taskdetailsbeanlist.toString());
                                }
                            } else if (Appreference.setlatestFilteredOption == 4) {
                                String s2 = s.get(i).getOpenDate().toString().toLowerCase();

                                if (s2.contains(s1)) {
                                    Log.i("constraint", "insideif 5 " + String.valueOf(constraint));
                                    taskdetailsbeanlist.add(s.get(i));
                                    Log.i("constraint", "List 6 " + taskdetailsbeanlist.toString());
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    result.values = taskdetailsbeanlist;
                    Log.i("constraint", "result 7 " + result.values.toString());
                    result.count = taskdetailsbeanlist.size();
                    Log.i("constraint", "resultcount 8 " + result.count);

                } else {
                    synchronized (this) {
                        result.values = projectList;
                        Log.i("constraint", "resultelse 9 " + result.values);
                        result.count = projectList.size();
                        Log.i("constraint", "resultelsecount 10 " + result.count);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("ProjectFragment", "ProjectFilter Exception : " + e.getMessage(), "WARN", null);
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            try {
                Log.d("constraint", "JNDSEJBJW  11 " + results.count);
                projectList.clear();
                filterbuddy = (ArrayList<ProjectDetailsBean>) results.values;
//            Log.i("filterbuddy", "filterbuddy" + filterbuddy.toString());
                //            buddyList.clear();
                if (filterbuddy.size() > 0) {
                    NoResults.setVisibility(View.GONE);
                    for (int i = 0, l = filterbuddy.size(); i < l; i++) {
                        Log.d("constraint", "JNDSEJBJW  12 " + results.count);
                        projectList.add(filterbuddy.get(i));
                    }
                } else {
                    NoResults.setVisibility(View.VISIBLE);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("constraint", "JNDSEJBJW  13 ");
                        projectArrayAdapter.notifyDataSetChanged();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("ProjectFragment", "publishResults Exception : " + e.getMessage(), "WARN", null);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("progress123", "inside onResume--------->");
        try {
            hideKeyboard();
            notifyNewProjectReceived();
            if (Appreference.context_table.containsKey("mainactivity")) {
                Log.d("Project", "Inside if in projectFragment onResume");
                MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
                mainActivity.BadgeReferece();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("ProjectFragment", "onResume Exception : " + e.getMessage(), "WARN", null);
        }

    }

    private void showprogress(final String message) {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
        try {
            Log.i("progress123", "inside show progress--------->");
            Log.i("progress123", "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//                    if (progress == null) {
//                        Log.i("progress123", "inside show progress=====. progress NULL--------->");
            progress = new ProgressDialog(getActivity());
            progress.setCancelable(false);
            progress.setMessage(message);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setProgress(0);
            progress.setMax(1000);
            progress.show();
            Log.i("progress123", "inside show progress showing--------->");

//                    }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("tnareport123", "EXCEPTION show progress showing- error-------->");
            Appreference.printLog("ProjectFragment", "showprogress Exception : " + e.getMessage(), "WARN", null);
            Log.i("progress123", "EXCEPTION show progress showing- error-------->" + e.getMessage());

        }
//            }
//        });
    }

    private void showprogress_1() {
        if (handler != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.i("expand", "inside show progress--------->");
                        if (classContext != null) {
                            progress = new ProgressDialog(classContext);
                            progress.setCancelable(false);
                            progress.setMessage("Listing JobCards...");
                            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            progress.setProgress(0);
                            progress.setMax(1000);
                            progress.show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("ProjectFragment", "showprogress_1 Exception : " + e.getMessage(), "WARN", null);
                    }
                }
            });
        }
    }

    public void cancelDialog() {
        try {
            if (progress != null && progress.isShowing()) {
                Log.i("register", "--progress bar end-----");
                Log.i("progress123", "inside cancelDialog--------->");
                progress.dismiss();
                progress = null;
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Appreference.printLog("ProjectFragment", "cancelDialog Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void showToast(final String msg) {
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), msg, Toast.LENGTH_LONG)
                            .show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("ProjectFragment", "showToast Exception : " + e.getMessage(), "WARN", null);
        }
    }

    public void refresh() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    projectArrayAdapter.notifyDataSetChanged();
                    Log.i("NewTaskConversation", "Refresh Handled here");
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("ProjectFragment", "refresh() Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });
    }

    public void notifyNewProjectReceived() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    String query_1 = "select *,cast(oracleProjectId as unsigned) as t from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'and projectcompletedstatus NOT IN (select projectcompletedstatus where projectcompletedstatus like '1') order by t DESC";
                    projectList = new ArrayList<>();
                    projectSearchList = new ArrayList<>();
                    projectList = VideoCallDataBase.getDB(classContext).getProjectdetails(query_1);
                    projectSearchList = VideoCallDataBase.getDB(classContext).getProjectdetails(query_1);
                    NoResults.setVisibility(View.GONE);
                    projectArrayAdapter = new ProjectArrayAdapter(getActivity(), projectList);
                    listview_project.setAdapter(projectArrayAdapter);
                    projectArrayAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    Appreference.printLog("ProjectFragment", "notifyNewProjectReceived Exception : " + e.getMessage(), "WARN", null);
                }
            }
        });
    }

    private boolean getNetworkState() {
        boolean isNetwork = false;
        try {
            ConnectivityManager ConnectionManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (ConnectionManager != null) {
                NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected() == true)
                    isNetwork = true;
                else
                    isNetwork = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("ProjectFragment", "getNetworkState Exception : " + e.getMessage(), "WARN", null);
        }
        return isNetwork;
    }

    //    public void notifyNewProjectReceived() {
//        try {
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    String query_1 = "select * from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'";
//                    projectList = new ArrayList<>();
//                    projectSearchList = new ArrayList<>();
//                    projectList = VideoCallDataBase.getDB(classContext).getProjectdetails(query_1);
//                    projectSearchList = VideoCallDataBase.getDB(classContext).getProjectdetails(query_1);
//                    NoResults.setVisibility(View.GONE);
//                    projectArrayAdapter = new ProjectArrayAdapter(getActivity(), projectList);
//                    listview_project.setAdapter(projectArrayAdapter);
//                    projectArrayAdapter.notifyDataSetChanged();
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
    private void hideKeyboard() {
        try {
            if (view != null) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("ProjectFragment", "hideKeyboard Exception : " + e.getMessage(), "WARN", null);
        }
    }

    private void hideKeyboard_fsr(AutoCompleteTextView ac_machine_no_spinner) {
        try {
            if (view != null) {
                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(ac_machine_no_spinner.getApplicationWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("ProjectFragment", "hideKeyboard_fsr Exception : " + e.getMessage(), "WARN", null);
        }
    }

    private void showFilterPopupWindow(View view) {
        Log.i("check123", "showFilterPopupWindow");
        final PopupMenu popup = new PopupMenu(getActivity(), view);

        popup.getMenuInflater().inflate(R.menu.popup_filter, popup.getMenu());
        popup.getMenu().getItem(0).setVisible(true);
        popup.getMenu().getItem(1).setVisible(true);
        popup.getMenu().getItem(2).setVisible(true);
        popup.getMenu().getItem(3).setVisible(true);
        if (Appreference.setlatestFilteredOption == 1) {
            popup.getMenu().getItem(1).setChecked(true);
        } else if (Appreference.setlatestFilteredOption == 2) {
            popup.getMenu().getItem(2).setChecked(true);
        } else if (Appreference.setlatestFilteredOption == 3) {
            popup.getMenu().getItem(3).setChecked(true);
        } else if (Appreference.setlatestFilteredOption == 4) {
            popup.getMenu().getItem(4).setChecked(true);
        } else {
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().equalsIgnoreCase("JobCard")) {
                    Appreference.setlatestFilteredOption = 1;
//                   popup.getMenu().getItem(0).setChecked(true);
                    ProjectSearch.setHint("Filter by Jobcard");
                } else if (item.getTitle().toString().equalsIgnoreCase("CustomerName")) {
//                   popup.getMenu().getItem(1).setChecked(true);
                    Appreference.setlatestFilteredOption = 2;
                    ProjectSearch.setHint("Filter by CustomerName");
                } else if (item.getTitle().toString().equalsIgnoreCase("Description")) {
//                   popup.getMenu().getItem(2).setChecked(true);
                    Appreference.setlatestFilteredOption = 3;
                    ProjectSearch.setHint("Filter by Description");
                } else if (item.getTitle().toString().equalsIgnoreCase("Date")) {
//                   popup.getMenu().getItem(3).setChecked(true);
                    ProjectSearch.setHint("Filter by date");
                    Appreference.setlatestFilteredOption = 4;
                }
                return false;
            }
        });
        popup.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            hideKeyboard();
            Log.i("Fragmentstate", "onDestroyView  ProjectFragment");
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("ProjectFragment", "onDestroyView Exception : " + e.getMessage(), "WARN", null);
        }
    }

    @Override
    public void ResponceMethod(final Object object) {
//        NoResults.setVisibility(View.GONE);
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        CommunicationBean opr = (CommunicationBean) object;
                        String s1 = opr.getEmail();
                        String s2 = opr.getFirstname();
                        if (s2.equalsIgnoreCase("getProject")) {
                            try {
                                Log.i("Response", "getProject " + s1);
                                try {
                                    Type collectionType = new TypeToken<ProjectDetailsBean>() {
                                    }.getType();
                                    ProjectDetailsBean pdb = new Gson().fromJson(s1, collectionType);
                                    Log.i("Response", "getProject name " + pdb.getProjectName());
                                    //                                VideoCallDataBase.getDB(classContext).insertProject_history(pdb);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("ProjectFragment", "ResponceMethod getProject Exception : " + e.getMessage(), "WARN", null);
                                }
                                cancelDialog();
                                Intent intent = new Intent(getActivity(), ProjectHistory.class);
                                intent.putExtra("projectId", project_id);
                                intent.putExtra("projectName", project_name);
                                //                intent.putExtra("parentTaskId", parentTask_Id);
                                intent.putExtra("projectOwner", project_owner);
                                intent.putExtra("groupUserId", groupuser_Id);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "ResponceMethod getProject_ProjectHistory Exception : " + e.getMessage(), "WARN", null);
                            }
                        } else if (s2.equalsIgnoreCase("listAllMyProject")) {
                            cancelDialog();
                            Log.i("listAllMyProject", "webservice response " + s1);
                            try {
                                JSONArray jr = new JSONArray(s1);
                                if (jr.length() > 0) {
                                    JSONObject jb = jr.getJSONObject(0);
                                    if (jb.has("projectName")) {
                                        Type collectionType = new TypeToken<List<ProjectDetailsBean>>() {
                                        }.getType();
                                        List<ProjectDetailsBean> pdb = new Gson().fromJson(s1, collectionType);
                                        for (int i = 0; i < pdb.size(); i++) {
                                            ProjectDetailsBean projectDetailsBean = pdb.get(i);
                                            VideoCallDataBase.getDB(classContext).insertProject_Details(projectDetailsBean);
                                            Log.i("listAllMyProject", "projectDetailsBean DB inserted id is " + projectDetailsBean.getId());
                                            Log.i("listAllMyProject", "projectDetailsBean DB inserted id is " + projectDetailsBean.getProjectName());
                                        }
                                    }
                                }
                                notifyNewProjectReceived();
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "ResponceMethod listAllMyProject Exception : " + e.getMessage(), "WARN", null);
                            }
                        } else if (s2.equalsIgnoreCase("getAllJobDetails")) {
                            Log.i("ws123", "inside Response of json webservice**************************");
                            Log.i("ws123", "getAllJobDetails response-->" + s1);
                            try {
                                JSONArray jr = new JSONArray(s1);
                                Collection listOfProjectsfrom_server = new ArrayList();
                                if (jr.length() > 0) {
                                    JSONObject jb = jr.getJSONObject(0);
                                    if (jb.has("listallProject")) {
                                        Log.i("wsTime123", "WS getAllJobDetails Response Received Time====>" + Appreference.getCurrentDateTime());
                                        Type collectionType = new TypeToken<List<ListallProjectBean>>() {
                                        }.getType();
                                        List<ListallProjectBean> pdb = new Gson().fromJson(s1, collectionType);
                                        Log.i("wsTime123", " WS getAllJobDetails Response After GSON ParsingTime====>" + Appreference.getCurrentDateTime());

                                        for (int i = 0; i < pdb.size(); i++) {
                                            ListallProjectBean listallProjectBean = pdb.get(i);
                                            Log.i("listAllMyJob", "listallProject size2222====>" + listallProjectBean.getListallProject().size());
                                            Log.i("wsTime123", "WS getAllJobDetails Response Before Inserting DB ====>" + Appreference.getCurrentDateTime());

                                            if (listallProjectBean.getListallProject() != null && listallProjectBean.getListallProject().size() > 0) {
                                                for (int j = 0; j < listallProjectBean.getListallProject().size(); j++) {
                                                    ProjectDetailsBean projectDetailsBean1 = listallProjectBean.getListallProject().get(j);
                                                    listOfProjectsfrom_server.add(projectDetailsBean1.getId());
                                                    VideoCallDataBase.getDB(classContext).insertProject_Details(projectDetailsBean1);
                                                }
                                            } else
                                                showToast("No JobCards Found..");
                                        }
                                        Log.i("wsTime123", " Ws getAllJobDetails Response After DB Insertion====>" + Appreference.getCurrentDateTime());
                                    }
                                }
                                String GetOracleIdQuery = "select * from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'order by projectId DESC";
                                Collection my_listOfProject_db = VideoCallDataBase.getDB(classContext).getOracleProjectIdlist(GetOracleIdQuery, "projectId");
                                my_listOfProject_db.removeAll(listOfProjectsfrom_server);
                                if (my_listOfProject_db.size() > 0) {
                                    Log.i("deleteproject123", "listOfProjectsfrom_server======>" + listOfProjectsfrom_server.toString());
                                    Log.i("deleteproject123", "*****************************************************************");
                                    Log.i("deleteproject123", "my_listOfProject_db.iterator().next(======>" + my_listOfProject_db.iterator().next());
                                    VideoCallDataBase.getDB(classContext).deleteProject(my_listOfProject_db.iterator().next().toString());
                                }
                                notifyNewProjectReceived();
                                cancelDialog();
                                Log.i("wsTime123", " Ws getAllJobDetails UI Notify Time====>" + Appreference.getCurrentDateTime());

                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "ResponceMethod getAllJobDetails Exception : " + e.getMessage(), "WARN", null);
                            }
                        } else if (s2.equalsIgnoreCase("getTaskForJobID")) {
                            Log.i("ws123", "******getTaskForJobID********** Response String " + s1);
                            boolean Success = true;
                            try {
                                final JSONObject jsonObject = new JSONObject(opr.getEmail());
                                Log.i("wsTime123", " Ws getTaskForJobID Response Received====>" + Appreference.getCurrentDateTime());

                                if (((String) jsonObject.get("result_text")).equalsIgnoreCase("pls send ur details")) {
                                    showToast("Please Send Correct Details!....");
                                    Appreference.isPauseStartFrom = false;
                                    Success = false;
                                } else {
                                    Success = true;
                                    Log.i("ws123", "getTaskForJobID Response String---------->" + s1);

                                    Type collectionType = new TypeToken<ListallProjectBean>() {
                                    }.getType();
                                    ListallProjectBean pdb = new Gson().fromJson(s1, collectionType);
                                    Log.i("wsTime123", " Ws getTaskForJobID Response After Parsing Time====>" + Appreference.getCurrentDateTime());

                                    Log.i("wsTime123", " Ws getTaskForJobID Response Before DB ENtry Time====>" + Appreference.getCurrentDateTime());

                                    VideoCallDataBase.getDB(classContext).insertProject_history(pdb);
                                    VideoCallDataBase.getDB(classContext).insert_updateProjectStatus(pdb);
                                    VideoCallDataBase.getDB(classContext).insert_updateCheckListTemplates(pdb);
                                    Log.i("wsTime123", " Ws getTaskForJobID Response After DB ENtry Time====>" + Appreference.getCurrentDateTime());

                                    ProjectDetailsBean projectDetailsBean = pdb.getProjectDTO();
                                    project_id = projectDetailsBean.getId();
                                    project_name = projectDetailsBean.getProjectName();
                                    ListMember listMember_2 = projectDetailsBean.getProjectOwner();
                                    project_owner = listMember_2.getUsername();

                                    ArrayList<ListAllgetTaskDetails> listAllgetTaskDetailses;

                                    listAllgetTaskDetailses = projectDetailsBean.getListSubTask();

                                    if (listAllgetTaskDetailses.size() > 0) {
                                        for (int i = 0; i < listAllgetTaskDetailses.size(); i++) {
                                            ListAllgetTaskDetails listAllgetTaskDetailses1 = listAllgetTaskDetailses.get(i);
                                            groupuser_Id = String.valueOf(listAllgetTaskDetailses1.getId());
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "ResponceMethod getTaskForJobID Exception : " + e.getMessage(), "WARN", null);
                            }
                            cancelDialog();
                            if (Success) {
                                MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
                                if (mainActivity != null) {
                                    mainActivity.cancelDialog();
                                }
                                try {
                                    Log.i("wsTime123", " Ws getTaskForJobID Response UI Notify   Time====>" + Appreference.getCurrentDateTime());

                                    Intent intent = new Intent(getActivity(), ProjectHistory.class);
                                    intent.putExtra("projectId", project_id);
                                    intent.putExtra("projectName", project_name);
                                    //                intent.putExtra("parentTaskId", parentTask_Id);
                                    if (Appreference.isPauseStartFrom) {
                                        Appreference.isPauseStartFrom = false;
                                        intent.putExtra("isFromNewTaskConv", true);
                                    } else {
                                        if (Appreference.context_table.containsKey("projecthistory")) {
                                            Appreference.context_table.remove("projecthistory");
                                        }
                                        intent.putExtra("isFromNewTaskConv", false);
                                    }
                                    intent.putExtra("projectOwner", project_owner);
                                    intent.putExtra("groupUserId", groupuser_Id);
                                    if (oracleProjectId != null)
                                        intent.putExtra("fromOracle", true);
                                    else
                                        intent.putExtra("fromOracle", false);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.right_anim, R.anim.left_anim);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("ProjectFragment", "ResponceMethod ProjectHistory Exception : " + e.getMessage(), "WARN", null);
                                }
                            }
                        } else if (s2.equalsIgnoreCase("projectCompleted")) {
                            Log.i("projectCompleted", "******projectCompleted********** Response String " + s1);
                            final JSONObject jsonObject = new JSONObject(opr.getEmail());
                            cancelDialog();
                            try {
                                if (((String) jsonObject.get("result_text")).equalsIgnoreCase("project task not completed")) {
                                    String result = (String) jsonObject.get("result_text");
                                    Toast.makeText(getContext(), "JobCards Tasks Not Completed", Toast.LENGTH_LONG).show();
                                } else if (((String) jsonObject.get("result_text")).equalsIgnoreCase("Job Completed")) {
                                    String result = (String) jsonObject.get("result_text");
//                                    VideoCallDataBase.getDB(classContext).deleteProject(jsonObject.getString("projectId"));
//                                    VideoCallDataBase.getDB(classContext.getApplicationContext()).deleteProjects(jsonObject.getString("projectId"));
                                    Toast.makeText(getContext(), "JobCard Completed Successfully", Toast.LENGTH_LONG).show();
                                } else {
                                    String result = (String) jsonObject.get("result_text");
                                    Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "ResponceMethod projectCompleted Exception : " + e.getMessage(), "WARN", null);
                            }
                        } else if (s2 != null && s2.equalsIgnoreCase(("fieldServiceReportJobWise"))) {
                            Log.i("output123", "projectFragment fieldServiceReportJobWise  Responce Received" + s1);
                            final JSONObject jsonObject = new JSONObject(opr.getEmail());
                            cancelDialog();
                            try {
                                if (((String) jsonObject.get("result_text")).equalsIgnoreCase("Field_servicce_report job successed")) {
                                    Log.i("output123", " Filename" + jsonObject.getString("filename"));
                                    String pdfURL = getResources().getString(R.string.task_reminder) + jsonObject.getString("filename");
                                   /* Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfURL));
                                    startActivity(browserIntent);*/
                                    String fileName = jsonObject.getString("filename");
                                    new DownloadImage(pdfURL, jsonObject.getString("filename")).execute();
                                } else if (((String) jsonObject.get("result_text")).equalsIgnoreCase("Work_service_report job successed")) {
                                    Log.i("output123", " Filename" + jsonObject.getString("filename"));
                                    String pdfURL = getResources().getString(R.string.task_reminder) + jsonObject.getString("filename");
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfURL));
                                    startActivity(browserIntent);
                                   /* String fileName = jsonObject.getString("filename");
                                    new DownloadImage(pdfURL, jsonObject.getString("filename")).execute();*/
                                } else {
                                    String result = (String) jsonObject.get("result_text");
                                    Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "ResponceMethod fieldServiceReportJobWise Exception : " + e.getMessage(), "WARN", null);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "ResponceMethod fieldServiceReportJobWise Exception : " + e.getMessage(), "WARN", null);
                            }
                        } else if (s2 != null && s2.equalsIgnoreCase(("tnaReportForDateWise"))) {
                            Log.i("report123", "projectFragment tnaReportForDateWise  Responce Received===>" + s1);
                            final JSONObject jsonObject = new JSONObject(opr.getEmail());
                            cancelDialog();
                            try {
                                if (((String) jsonObject.get("result_text")).equalsIgnoreCase("TaskNeedAssigment Report Created successfully")) {
                                    Log.i("output123", " Filename" + jsonObject.getString("filename"));

                                    String pdfURL = getResources().getString(R.string.task_reminder) + jsonObject.getString("filename");
                                    String fileName = jsonObject.getString("filename");
                                    Appreference.isTNAReport = true;
                                    Log.i("tnareport123", "Webservice response received================>");
                                    new DownloadImage(pdfURL, jsonObject.getString("filename")).execute();
                                } else {
                                    String result = (String) jsonObject.get("result_text");
                                    Log.i("tnareport123", "Webservice response received================>" + result);
                                    Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "ResponceMethod tnaReportForDateWise Exception : " + e.getMessage(), "WARN", null);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "ResponceMethod tnaReportForDateWise Exception : " + e.getMessage(), "WARN", null);
                            }
                        } else if (s2 != null && s2.equalsIgnoreCase(("fieldServiceSearch"))) {
                            Log.i("output123", "projectFragment fieldServiceSearch  Responce Received" + s1);
                            final JSONObject jsonObject = new JSONObject(opr.getEmail());
                            cancelDialog();
                            try {
                                if (((String) jsonObject.get("result_text")).equalsIgnoreCase("fetch data loaded successed")) {
                                    HashMap<String, List<String>> fsr_key_value = new HashMap<>();
                                    HashMap<String, String> projectIdForOracleId = new HashMap<>();
                                    ArrayList<String> allJobcardlist = new ArrayList<String>();
                                    Type collectionType = new TypeToken<FSRResultBean>() {
                                    }.getType();
                                    FSRResultBean resultBean = new Gson().fromJson(s1, collectionType);
                                    if (resultBean.getListProject().size() > 0) {
                                        for (int i = 0; i < resultBean.getListProject().size(); i++) {
                                            FSRSearchResults fsrSearchResults = (FSRSearchResults) resultBean.getListProject().get(i);
                                            fsr_key_value.put(fsrSearchResults.getOracleProjectId(), fsrSearchResults.getTaskcompleteddates());
                                            allJobcardlist.add(fsrSearchResults.getOracleProjectId());
                                            projectIdForOracleId.put(fsrSearchResults.getOracleProjectId(), fsrSearchResults.getProjectId());
                                        }
                                    }
                                    Log.i("fsrReport123", "fsrReport123=========>" + fsr_key_value.size());
                                    show_FSRJobList(fsr_key_value, allJobcardlist, projectIdForOracleId);

                                } else {
                                    String result = (String) jsonObject.get("result_text");
                                    Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "ResponceMethod fieldServiceSearch Exception : " + e.getMessage(), "WARN", null);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "ResponceMethod fieldServiceSearch Exception : " + e.getMessage(), "WARN", null);
                            }
                        } else if (s2 != null && s2.equalsIgnoreCase(("checkListReport"))) {
                            final JSONObject jsonObject = new JSONObject(opr.getEmail());
                            cancelDialog();
                            try {
                                if (((String) jsonObject.get("result_text")).equalsIgnoreCase("Job Card Number CheckList Report Send successfully")) {
                                    Log.i("output123", " Filename" + jsonObject.getString("fileName"));
                                    String pdfURL = getResources().getString(R.string.task_reminder) + jsonObject.getString("fileName");
                                    String fileName = jsonObject.getString("fileName");
                                    new DownloadImage(pdfURL, jsonObject.getString("fileName")).execute();
                                }  else {
                                    String result = (String) jsonObject.get("result_text");
                                    Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "ResponceMethod fieldServiceReportJobWise Exception : " + e.getMessage(), "WARN", null);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "ResponceMethod fieldServiceReportJobWise Exception : " + e.getMessage(), "WARN", null);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("ProjectFragment", "ResponceMethod Exception : " + e.getMessage(), "WARN", null);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("ProjectFragment", "ResponceMethod Exception : " + e.getMessage(), "WARN", null);
        }
    }

    String fsr_jobId, fsr_date;

    private void show_FSRJobList(final HashMap<String, List<String>> fsr_key_value, ArrayList showAllJobCard, final HashMap<String, String> projectIdForOracleId) {

        try {
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.fsr_request_view, null);
            final LinearLayout ll_second_layout = (LinearLayout) alertLayout.findViewById(R.id.second_layout);
            ll_second_layout.setVisibility(View.VISIBLE);
//        final Spinner machine_no_spinner = (Spinner) alertLayout.findViewById(R.id.machine_no_spinner);
            final AutoCompleteTextView machine_no_spinner = (AutoCompleteTextView) alertLayout.findViewById(R.id.auto_complete);

            final TextView fst_start = (TextView) alertLayout.findViewById(R.id.fst_start);
            final TextView fst_end = (TextView) alertLayout.findViewById(R.id.fst_end);
            final Spinner job_spinner = (Spinner) alertLayout.findViewById(R.id.job_spinner);
            final Spinner date_spinner = (Spinner) alertLayout.findViewById(R.id.date_spinner);
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("FSR REPORT");
            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);


            machine_no_spinner.setText(User_selected_mcsrNo);
            machine_no_spinner.setEnabled(false);
            fst_start.setText(User_selected_startDate);
            fst_end.setText(User_selected_endDate);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, showAllJobCard);
            // Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // attaching data adapter to spinner
            job_spinner.setAdapter(dataAdapter);
            hideKeyboard();


            job_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selected_jobcard = String.valueOf(job_spinner.getSelectedItem());


                    if (projectIdForOracleId.containsKey(selected_jobcard)) {
                        fsr_jobId = projectIdForOracleId.get(selected_jobcard);
                    }
                    if (fsr_key_value.containsKey(selected_jobcard)) {

                        List<String> myList = new ArrayList<String>(Arrays.asList(fsr_key_value.get(selected_jobcard).get(0).split(",")));

                        ArrayList<String> MyDate = new ArrayList<String>();
                        if (myList.size() > 0) {
                            for (int j = 0; j < myList.size(); j++) {
                                String list = Appreference.utcToLocalTime(myList.get(j));
                                MyDate.add(list.substring(0, list.indexOf(' ')));
                            }
                        }
                        ArrayAdapter<String> dataAdapter_Date = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, MyDate);
                        // Drop down layout style - list view with radio button
                        dataAdapter_Date.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        // attaching data adapter to spinner
                        date_spinner.setAdapter(dataAdapter_Date);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            date_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selected_date = String.valueOf(date_spinner.getSelectedItem());
                    //                selected date from spinner
                    try {
                        if (selected_date != null)
                            fsr_date = selected_date;
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("ProjectFragment", "show_FSRJobList date_spinner Exception : " + e.getMessage(), "WARN", null);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        if (fsr_date != null && !fsr_date.equalsIgnoreCase("")) {
                            //                                dialog.dismiss();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            SimpleDateFormat dateParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date date = null;
                            Date date1 = null;
                            String FSRStartDateUTC = "";
                            String FSREndDateUTC = "";
                            String fsr_start_date = fsr_date + " " + "00:00:00";
                            String fsr_end_date = fsr_date + " " + "23:59:59";
                            if (fsr_start_date != null && !fsr_start_date.equalsIgnoreCase("")) {
                                try {
                                    date = dateParse.parse(fsr_start_date);
                                    FSRStartDateUTC = dateFormat.format(date);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("ProjectFragment", "show_FSRJobList Submit Exception : " + e.getMessage(), "WARN", null);
                                }
                            }
                            if (fsr_end_date != null && !fsr_end_date.equalsIgnoreCase("")) {
                                try {
                                    date1 = dateParse.parse(fsr_end_date);
                                    FSREndDateUTC = dateFormat.format(date1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Appreference.printLog("ProjectFragment", "show_FSRJobList FSREndDateUTC Exception : " + e.getMessage(), "WARN", null);
                                }
                            }

                            try {
                                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                                nameValuePairs.add(new BasicNameValuePair("projectId", fsr_jobId));
                                nameValuePairs.add(new BasicNameValuePair("taskCompletedStartDate", FSRStartDateUTC));
                                nameValuePairs.add(new BasicNameValuePair("taskCompletedEndDate", FSREndDateUTC));
                                showprogress("Downloading...");
                                Appreference.jsonRequestSender.OracleFSRJOBReport(EnumJsonWebservicename.fieldServiceReportJobWise, nameValuePairs, ProjectsFragment.this);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "show_FSRJobList OracleFSRJOBReport Exception : " + e.getMessage(), "WARN", null);
                            }
                        } else
                            Toast.makeText(getActivity(), "Please select any date...", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Appreference.printLog("ProjectFragment", "show_FSRJobList OracleFSRJOBReport Exception : " + e.getMessage(), "WARN", null);
                    }
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = alert.create();
            if (!dialog.isShowing()) {
                dialog.show();
            }
//        dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Appreference.printLog("ProjectFragment", "show_FSRJobList Exception : " + e.getMessage(), "WARN", null);
        }
    }

    @Override
    public void ErrorMethod(Object object) {
    }

    public class DownloadImage extends AsyncTask<String, Void, String> {
        String downloadImageurl;
        TaskDetailsBean detailsBean = null;
        String namevalue = "";

        public DownloadImage(String url, String name) {
            this.downloadImageurl = url;
            namevalue = name;
        }

        public DownloadImage(TaskDetailsBean name) {
            detailsBean = name;
            this.downloadImageurl = getResources().getString(R.string.task_reminder) + name.getTaskDescription();
            namevalue = name.getTaskDescription();
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                if (getNetworkState()) {
                    Log.i("tnareport123", "Download Image doInBackground================>");
//                    showprogress("Downloading xls...");
                    String profile1 = null;
                    if (downloadImageurl != null) {
                        if (downloadImageurl.contains("task"))
                            profile1 = downloadImageurl.split("task/")[1];
                        else
                            profile1 = downloadImageurl.split("chat/")[1];
                        File extStore = Environment.getExternalStorageDirectory();
                        File myFile = new File(extStore.getAbsolutePath() + "/High Message/downloads/" + profile1);
                        if (!myFile.exists()) {
                            try {

                                URL bitmap = new URL(downloadImageurl);

                                HttpURLConnection connection =
                                        (HttpURLConnection) bitmap.openConnection();
                                InputStream in = connection.getInputStream();
                                connection.connect();
                                if (connection.getInputStream() != null) {
                                    InputStream inputStream = connection.getInputStream();
                                    String dir_path = Environment.getExternalStorageDirectory()
                                            + "/High Message/downloads";
                                    Log.i("profiledownload", "profile dir_path" + dir_path);
                                    File directory = new File(dir_path);
                                    if (!directory.exists())
                                        directory.mkdir();
                                    String filePath = Environment.getExternalStorageDirectory()
                                            .getAbsolutePath()
                                            + "/High Message/downloads/"
                                            + profile1;
                                    File file_name = new File(filePath);
                                    BufferedOutputStream bos = new BufferedOutputStream(
                                            new FileOutputStream(file_name));
                                    int inByte;
                                    while ((inByte = inputStream.read()) != -1) {
                                        bos.write(inByte);
                                    }
                                    bos.close();

                                    //** For  Avatar download
                                    //Start
                                    Log.d("profiledownload", "check ");
                                    Bitmap btmap = BitmapFactory.decodeStream(inputStream);
                                    //End
                                }
                            } catch (FileNotFoundException e) {
                                Log.i("tnareport123", "Download Image doInBackground FileNotFoundException exception================>" + e.getMessage());
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "DownloadImage doInBackground Exception : " + e.getMessage(), "WARN", null);
                            } catch (Exception e) {
                                Log.i("tnareport123", "Download Image doInBackground exception================>" + e.getMessage());
                                e.printStackTrace();
                                Appreference.printLog("ProjectFragment", "DownloadImage doInBackground Exception : " + e.getMessage(), "WARN", null);
                            }
                        }
                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("ProjectFragment", "DownloadImage Exception : " + e.getMessage(), "WARN", null);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            cancelDialog();
            try {
                Log.i("tnareport123", "Download Image onPostExecute ================>");
                File file;
                file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + namevalue);
                if (file.exists()) {
                    Uri path = Uri.fromFile(file);
                    Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                    pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    if (Appreference.isTNAReport) {
                        pdfOpenintent.setDataAndType(path, "application/vnd.ms-excel");
                    } else {
                        pdfOpenintent.setDataAndType(path, "application/pdf");
                    }
                    try {
                        Log.i("tnareport123", "Download Image onPostExecute startActivity Appreference.isTNAReport=================>" + Appreference.isTNAReport);
                        startActivity(pdfOpenintent);
                        Appreference.isTNAReport = false;
                    } catch (ActivityNotFoundException e) {
                        Log.i("tnareport123", "Download Image onPostExecute ActivityNotFoundException =================>" + e.getMessage());
                        Appreference.printLog("ProjectFragment", "DownloadImage onPostExecute Exception : " + e.getMessage(), "WARN", null);
                        if (Appreference.isTNAReport)
                            Toast.makeText(getActivity(), "Please Install MS-Excel or WPS Office app to view the file", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity(), "Please Install WPS Office or Adobe Reader app to view the file", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.i("tnareport123", "Download Image onPostExecute Exception =================>" + e.getMessage());
                        Appreference.printLog("ProjectFragment", "DownloadImage onPostExecute Exception : " + e.getMessage(), "WARN", null);
                        if (Appreference.isTNAReport)
                            Toast.makeText(getActivity(), "Please Install MS-Excel or WPS Office app to view the file", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity(), "Please Install WPS Office or Adobe Reader app to view the file", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Pdf Documentation", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("tnareport123", "Download Image onPostExecute Exception 111 =================>" + e.getMessage());
                Appreference.printLog("ProjectFragment", "DownloadImage Exception : " + e.getMessage(), "WARN", null);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }
}
