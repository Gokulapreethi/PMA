package com.ase.sketh;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.ase.Bean.ListallProjectBean;
import com.ase.Bean.ProjectDetailsBean;
import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;
import com.ase.ImageLoader;
import com.ase.ListAllgetTaskDetails;
import com.ase.ProjectHistory;
import com.ase.R;
import com.ase.SearchMediaWebView;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public class ProjectsFragment extends Fragment implements View.OnClickListener, WebServiceInterface {
    public View view;
    private SwipeMenuListView listview_project;
    TextView heading_project, exclation_counter, first_fsr, end_fsr, tna_middle;
    ImageView image_search, reportdetails, fsrDetails;
    public static Context classContext;
    static ProjectsFragment fragment;
    ProgressDialog progress;
    public ArrayList<ProjectDetailsBean> projectList, projectSearchList, filterbuddy;
    LinearLayout History_Search;
    EditText ProjectSearch;
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

    public static ProjectsFragment getInstance() {
        return projectsFragment;
    }

    public static ProjectsFragment newInstance(int sectionNumber, Context context) {
        if (fragment == null) {
            Log.i("task", "checked int" + sectionNumber);
            fragment = new ProjectsFragment();
            Bundle args = new Bundle();
            classContext = context;
            fragment.setArguments(args);
            Appreference.isProject = true;
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
        Log.i("LifeCycle", " projectFragment isVisibleToUser : " + isVisibleToUser);
        isCurrentlyActivie = isVisibleToUser;
        if (isVisibleToUser) {
            try {
                if (getNetworkState()) {
                    showprogress_1();
                    List<NameValuePair> tagNameValuePairs = new ArrayList<NameValuePair>();
                    tagNameValuePairs.add(new BasicNameValuePair("userId", String.valueOf(Appreference.loginuserdetails.getId())));
                    Log.i("ws123", "getAllJobDetails request");
                    Appreference.jsonRequestSender.getAllJobDetails(EnumJsonWebservicename.getAllJobDetails, tagNameValuePairs, this);
                } else {
                    Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_SHORT).show();
                    String query_1 = "select *,cast(oracleProjectId as unsigned) as t from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'and projectcompletedstatus NOT IN (select projectcompletedstatus where projectcompletedstatus like '1') order by t DESC";
                    projectList = new ArrayList<>();
                    projectSearchList = new ArrayList<>();
                    projectList = VideoCallDataBase.getDB(classContext).getProjectdetails(query_1);
                    projectSearchList = VideoCallDataBase.getDB(classContext).getProjectdetails(query_1);
                    NoResults.setVisibility(View.GONE);
                    projectArrayAdapter = new ProjectArrayAdapter(getActivity(), projectList);
                    listview_project.setAdapter(projectArrayAdapter);
                    projectArrayAdapter.notifyDataSetChanged();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Nullable
    @Override
    public View
    onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.project_fragment_layout, container, false);
        try {
            Appreference.context_table.put("projectfragment", this);
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            History_Search = (LinearLayout) view.findViewById(R.id.History_Search);
            ProjectSearch = (EditText) view.findViewById(R.id.searchtext);
            NoResults = (TextView) view.findViewById(R.id.Noresult);
            tna_middle = (TextView) view.findViewById(R.id.tna_middle);
            first_fsr = (TextView) view.findViewById(R.id.first_fsr);
            end_fsr = (TextView) view.findViewById(R.id.end_fsr);
            listview_project = (SwipeMenuListView) view.findViewById(R.id.listview_project);
            NoResults.setVisibility(View.GONE);
            heading_project = (TextView) view.findViewById(R.id.heading_project);
            image_search = (ImageView) view.findViewById(R.id.image_search);
            reportdetails = (ImageView) view.findViewById(R.id.reportdetails);
            fsrDetails = (ImageView) view.findViewById(R.id.fsrDetails);
            reportdetails.setOnClickListener(this);
            fsrDetails.setOnClickListener(this);

            if (Appreference.loginuserdetails != null && Appreference.loginuserdetails.getRoleId() != null
                    && Appreference.loginuserdetails.getRoleId().equalsIgnoreCase("2")) {
                image_search.setVisibility(View.VISIBLE);
                reportdetails.setVisibility(View.VISIBLE);
                fsrDetails.setVisibility(View.VISIBLE);
                tna_middle.setVisibility(View.VISIBLE);
                first_fsr.setVisibility(View.VISIBLE);
                end_fsr.setVisibility(View.VISIBLE);
            } else {
                image_search.setVisibility(View.GONE);
                reportdetails.setVisibility(View.GONE);
                fsrDetails.setVisibility(View.VISIBLE);
                tna_middle.setVisibility(View.GONE);
                first_fsr.setVisibility(View.GONE);
                end_fsr.setVisibility(View.VISIBLE);
                end_fsr.setText("FSR");
            }


            Log.i("task", "project");
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            projectsFragment = this;
            exclation_counter = (TextView) view.findViewById(R.id.exclation_counter);

            activity_start = (TextView) view.findViewById(R.id.activity_start);
            activity_end = (TextView) view.findViewById(R.id.activity_end);
            submit_button = (Button) view.findViewById(R.id.submit_button);
            all_report_title = (RelativeLayout) view.findViewById(R.id.all_report_title);

            try {
                String s = "select * from taskDetailsInfo where readStatus='1'";
                ArrayList<ProjectDetailsBean> projectDetailsBeen = VideoCallDataBase.getDB(getContext()).getExclationdetails(s);
                if (projectDetailsBeen.size() > 0)
                    exclation_counter.setVisibility(View.VISIBLE);
                else
                    exclation_counter.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }
            reportdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tna_count == 0) {
                        all_report_title.setVisibility(View.VISIBLE);
                        tna_count = 1;
                    } else {
                        all_report_title.setVisibility(View.GONE);
                        tna_count = 0;
                    }
                  /*  Intent intent = new Intent(getActivity(), SearchMediaWebView.class);
                    intent.putExtra("urlload","tnareport");
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_anim, R.anim.left_anim);*/
                }
            });

            fsrDetails.setOnClickListener(new View.OnClickListener() {
                 String fsr_jobId,fsr_date;

                @Override
                public void onClick(View v) {

                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View alertLayout = inflater.inflate(R.layout.fsr_report_view, null);
                    final Spinner job_spinner = (Spinner) alertLayout.findViewById(R.id.job_spinner);
                    final Spinner date_spinner = (Spinner) alertLayout.findViewById(R.id.date_spinner);
                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("FSR REPORT");
                    // this is set the view from XML inside AlertDialog
                    alert.setView(alertLayout);
                    // disallow cancel of AlertDialog on click of back button and outside touch
                    alert.setCancelable(false);
                    /*getting list of jobcards for the user from table*/
                    String list_query = "select *,cast(oracleProjectId as unsigned) as t from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "' order by t DESC";
                    ArrayList<String> My_Project = VideoCallDataBase.getDB(getActivity()).getOracleProjectIdlist(list_query);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, My_Project);
                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // attaching data adapter to spinner
                    job_spinner.setAdapter(dataAdapter);
                    job_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String selected_jobcard = String.valueOf(job_spinner.getSelectedItem());/*selected jobcard from spinner*/
                            /*getting projectid from table relevant to oracleprojectId*/
                            String get_projectId_query = "select projectId from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'and oracleProjectId='" + selected_jobcard + "'";
                            String fsr_ProjectId = VideoCallDataBase.getDB(getActivity()).getprojectIdForOracleID(get_projectId_query);
                            fsr_jobId=fsr_ProjectId;
                            /*getting list of Eod date regarding the selected jobcard*/
                            String list_query1 = "select distinct taskcompleteddate from projectStatus where projectId='" + fsr_ProjectId + "' and status= '10' order by taskcompleteddate";
                            ArrayList<String> My_date = VideoCallDataBase.getDB(getActivity()).getPerTaskcompletedDates(list_query1);
                            if(My_date.size()>0) {
                                ArrayAdapter<String> dataAdapter_Date = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, My_date);
                                // Drop down layout style - list view with radio button
                                dataAdapter_Date.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                // attaching data adapter to spinner
                                date_spinner.setAdapter(dataAdapter_Date);

                            }else
                            {
                                fsr_date="";
                                Toast.makeText(getActivity(), "No EOD Dates Found..", Toast.LENGTH_SHORT).show();
                                ArrayAdapter<String> dataAdapter_Date = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, My_date);
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
                            String selected_date = String.valueOf(date_spinner.getSelectedItem());/*selected date from spinner*/
                            try {
                                if(selected_date!=null)
                                    fsr_date=selected_date;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(fsr_date!=null && !fsr_date.equalsIgnoreCase("")) {
//                                dialog.dismiss();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                SimpleDateFormat dateParse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                                Date date = null;
                                Date date1 = null;
                                String FSRStartDateUTC = "";
                                String FSREndDateUTC = "";
                                String fsr_start_date=fsr_date+" "+"00:00:00";
                                String fsr_end_date=fsr_date+" "+"23:59:59";
                                if (fsr_start_date != null && !fsr_start_date.equalsIgnoreCase("")) {
                                    try {
                                        date = dateParse.parse(fsr_start_date);
                                        FSRStartDateUTC = dateFormat.format(date);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (fsr_end_date != null && !fsr_end_date.equalsIgnoreCase("")) {
                                    try {
                                        date1 = dateParse.parse(fsr_end_date);
                                        FSREndDateUTC = dateFormat.format(date1);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                                nameValuePairs.add(new BasicNameValuePair("projectId", fsr_jobId));
                                nameValuePairs.add(new BasicNameValuePair("taskCompletedStartDate", FSRStartDateUTC));
                                nameValuePairs.add(new BasicNameValuePair("taskCompletedEndDate", FSREndDateUTC));
                                showprogress("Downloading...");
                                Appreference.jsonRequestSender.OracleFSRJOBReport(EnumJsonWebservicename.fieldServiceReportJobWise, nameValuePairs, ProjectsFragment.this);
                            }else
                                Toast.makeText(getActivity(), "Please select any date...", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = alert.create();
                    dialog.show();
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
                                            TNAReportStart = start_date+" "+"00:00:00";
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
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
                                            String TnaStart[]=TNAReportStart.split(" ");
                                            String ReportStart=TnaStart[0];

                                            if ((end_date.compareTo(ReportStart) == 0) || ((cur_date.compareTo(end_date) >= 0) && (end_date.compareTo(ReportStart) > 0))) {
                                                activity_end.setText(end_date);
                                                TNAReportEnd = end_date+" "+"23:59:59";
                                            } else {
                                                Toast.makeText(getActivity(), "Kindly select above start date and below current date", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Toast.makeText(getActivity(), "Please select start date", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
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
                            }
                        }
                        if (TNAReportEnd != null && !TNAReportEnd.equalsIgnoreCase("")) {
                            try {
                                date1 = dateParse.parse(TNAReportEnd);
                                EndDateUTC = dateFormat.format(date1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        Log.i("report123", "-====>startdATE UTC====>" + StartDateUTC);
                        Log.i("report123", "-====>eNDdATE UTC====>" + EndDateUTC);
                        if ((date != null && date1 != null && !date.after(date1)) || (date.compareTo(date1)==0)) {
                            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                            nameValuePairs.add(new BasicNameValuePair("userId", String.valueOf(Appreference.loginuserdetails.getId())));
                            nameValuePairs.add(new BasicNameValuePair("travelStartDate", StartDateUTC));
                            nameValuePairs.add(new BasicNameValuePair("travelEndDate", EndDateUTC));
                            showprogress("Downloading....");
                            Appreference.jsonRequestSender.OracleTNAJobReport(EnumJsonWebservicename.tnaReportForDateWise, nameValuePairs, ProjectsFragment.this);
                        } else {
                            Toast.makeText(getActivity(), "Please Enter correct Start/End Date", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            final SwipeMenuCreator creator = new SwipeMenuCreator() {

                @Override
                public void create(SwipeMenu menu) {
    /*if(taskDetailsBean!=null) {
        if (taskDetailsBean.getFromUserEmail().equalsIgnoreCase(Appreference.loginuserdetails.getEmail())) {*/
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
       /* }
    }*/

                            /*// create "delete" item
                            SwipeMenuItem deleteItem = new SwipeMenuItem(
                                    classContext.getApplicationContext());
                            // set item background
                            deleteItem.setBackground(new ColorDrawable(Color.rgb(0xFf,
                                    0x00, 0x00)));
                            // set item width
                            deleteItem.setWidth(dp2px(90));
                            // set a icon
                            deleteItem.setTitle("Delete");
                            deleteItem.setTitleSize(18);
                            deleteItem.setTitleColor(Color.WHITE);
    //            deleteItem.setIcon(R.drawable.ic_delete_32);
                            // add to menu
                            menu.addMenuItem(deleteItem);*/
                            break;
                        case 0:

                            /*SwipeMenuItem deleteItem1 = new SwipeMenuItem(
                                    classContext.getApplicationContext());
                            // set item background
                            deleteItem1.setBackground(new ColorDrawable(Color.rgb(0xFf,
                                    0x00, 0x00)));
                            // set item width
                            deleteItem1.setWidth(dp2px(90));
                            // set a icon
                            deleteItem1.setTitle("Delete");
                            deleteItem1.setTitleSize(18);
                            deleteItem1.setTitleColor(Color.WHITE);
    //            deleteItem.setIcon(R.drawable.ic_delete_32);
                            // add to menu
                            menu.addMenuItem(deleteItem1);*/
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
       /* }
    }*/

                           /* // create "delete" item
                            SwipeMenuItem deleteItem2 = new SwipeMenuItem(
                                    classContext.getApplicationContext());
                            // set item background
                            deleteItem2.setBackground(new ColorDrawable(Color.rgb(0xFf,
                                    0x00, 0x00)));
                            // set item width
                            deleteItem2.setWidth(dp2px(90));
                            // set a icon
                            deleteItem2.setTitle("Delete");
                            deleteItem2.setTitleSize(18);
                            deleteItem2.setTitleColor(Color.WHITE);
    //            deleteItem.setIcon(R.drawable.ic_delete_32);
                            // add to menu
                            menu.addMenuItem(deleteItem2);*/
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


                           /* // create "delete" item
                            SwipeMenuItem deleteItems = new SwipeMenuItem(
                                    classContext.getApplicationContext());
                            // set item background
                            deleteItems.setBackground(new ColorDrawable(Color.rgb(0xFf,
                                    0x00, 0x00)));
                            // set item width
                            deleteItems.setWidth(dp2px(90));
                            // set a icon
                            deleteItems.setTitle("Delete");
                            deleteItems.setTitleSize(18);
                            deleteItems.setTitleColor(Color.WHITE);
    //            deleteItem.setIcon(R.drawable.ic_delete_32);
                            // add to menu
                            menu.addMenuItem(deleteItems);*/
                            break;

                    }
                }
            };
            listview_project.setMenuCreator(creator);
            listview_project.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

            listview_project.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                    ProjectDetailsBean projectDetailsBean = projectList.get(position);
                    //                taskDetailsBean = taskDetailsBeen.get(position);
                    //                String query;
                    //                if (taskDetailsBean.getOwnerOfTask() != null && taskDetailsBean.getToUserId() != null) {
                    //                    if (taskDetailsBean.getOwnerOfTask().equals(Appreference.loginuserdetails.getUsername())) {
                    //                        menu.getMenuItem(index).getTitle();
                    //                        Log.i("task", "case" + menu.getMenuItem(index).getTitle());
                    //                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //                        String dateTime = dateFormat.format(new Date());
                    //                        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    //                        String dateforrow = dateFormat.format(new Date());
                    //                        String tasktime = dateTime;
                    //                        tasktime = tasktime.split(" ")[1];
                    //                        Log.i("task", "tasktime" + tasktime);
                    //                        Log.i("UTC", "sendMessage utc time" + dateforrow);
                    //                        String taskUTCtime = dateforrow;
                    //                        taskDetailsBean.setDateTime(dateTime);
                    //                        taskDetailsBean.setTaskUTCDateTime(dateforrow);
                    //                        taskDetailsBean.setTaskUTCTime(taskUTCtime);
                    //                        taskDetailsBean.setTasktime(tasktime);
                    //                        taskDetailsBean.setMimeType("text");
                    //                        taskDetailsBean.setSignalid(Utility.getSessionID());
                    if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("Activate")) {
                        //                            Log.i("task", "case------>0" + menu.getMenuItem(index).getTitle());
                        //                            taskDetailsBean.setTaskStatus("inprogress");
                        //
                        //                            Log.i("task", "case------>1");
                        //                            String query3 = "update taskDetailsInfo  set taskStatus = 'inprogress' where ('" + taskDetailsBean.getTaskId() + "'= taskId ) and loginuser='" + Appreference.loginuserdetails.getEmail() + "';";
                        //                            Log.i("task", "case------>2");
                        //                            VideoCallDataBase.getDB(context).getTaskHistory(query3);
                        //                            Log.i("task", "case------>3");
                        //                            JSONObject jsonObject = new JSONObject();
                        //                            try {
                        //
                        //                                JSONObject jsonObject1 = new JSONObject();
                        //                                jsonObject1.put("id", Integer.parseInt(taskDetailsBean.getTaskId()));
                        //
                        //                                jsonObject.put("task", jsonObject1);
                        //
                        //                                JSONObject jsonObject2 = new JSONObject();
                        //                                jsonObject2.put("id", Appreference.loginuserdetails.getId());
                        //
                        //                                jsonObject.put("from", jsonObject2);
                        //
                        //                                JSONObject jsonObject3 = new JSONObject();
                        //                                jsonObject3.put("id", Integer.parseInt(taskDetailsBean.getToUserId()));
                        //
                        //                                jsonObject.put("to", jsonObject3);
                        //                                jsonObject.put("signalId", taskDetailsBean.getSignalid());
                        //                                jsonObject.put("parentId", taskDetailsBean.getParentId());
                        //                                jsonObject.put("createdDate", taskDetailsBean.getDateTime());
                        //                                jsonObject.put("percentageCompleted", taskDetailsBean.getCompletedPercentage());
                        //                                jsonObject.put("requestType", "percentageCompleted");
                        //                                jsonObject.put("taskStatus", "inprogress");
                        //                            } catch (JSONException e) {
                        //                                e.printStackTrace();
                        //                            }
                        //                            Log.i("beforewebcall", taskDetailsBean.getTaskStatus());
                        //                            Log.i("jsonrequest", jsonObject.toString());
                        //                            Appreference.jsonRequestSender.taskConversationEntry(EnumJsonWebservicename.taskConversationEntry, jsonObject, TaskHistory.this, null, taskDetailsBean);
                        //                            Log.i("webservice ", "called.abandoned");
                        //                            taskDetailsBeen.remove(position);
                        //                            taskDetailsBeanArraylist.remove(position);
                        //                            buddyArrayAdapter.notifyDataSetChanged();

                    } else if (menu.getMenuItem(index).getTitle().equalsIgnoreCase("Abandon")) {
                        //                            taskDetailsBean.setTaskStatus("abandoned");
                        //                            JSONObject jsonObject = new JSONObject();
                        //                            Log.i("task", "taskid" + taskDetailsBean.getTaskId());
                        //
                        //                            try {
                        //
                        //                                JSONObject jsonObject1 = new JSONObject();
                        //                                jsonObject1.put("id", Integer.parseInt(taskDetailsBean.getTaskId()));
                        //
                        //                                jsonObject.put("task", jsonObject1);
                        //
                        //                                JSONObject jsonObject2 = new JSONObject();
                        //                                jsonObject2.put("id", Appreference.loginuserdetails.getId());
                        //
                        //                                jsonObject.put("from", jsonObject2);
                        //
                        //                                JSONObject jsonObject3 = new JSONObject();
                        //                                jsonObject3.put("id", Integer.parseInt(taskDetailsBean.getToUserId()));
                        //
                        //                                jsonObject.put("to", jsonObject3);
                        //                                jsonObject.put("signalId", taskDetailsBean.getSignalid());
                        //                                jsonObject.put("parentId", taskDetailsBean.getParentId());
                        //                                jsonObject.put("createdDate", taskDetailsBean.getDateTime());
                        //                                jsonObject.put("percentageCompleted", taskDetailsBean.getCompletedPercentage());
                        //                                jsonObject.put("requestType", "percentageCompleted");
                        //                                jsonObject.put("taskStatus", "abandoned");
                        //                            } catch (JSONException e) {
                        //                                e.printStackTrace();
                        //                            }
                        //
                        //                            Log.i("beforewebcall", taskDetailsBean.getTaskStatus());
                        //                            Log.i("jsonrequest", jsonObject.toString());
                        //                            Appreference.jsonRequestSender.taskConversationEntry(EnumJsonWebservicename.taskConversationEntry, jsonObject, TaskHistory.this, null, taskDetailsBean);
                        //                            Log.e("webservice ", "called.active");
                        //
                        //                                VideoCallDataBase.getDB(context).insertORupdate_Task_history(taskDetailsBean);
                        //                            taskDetailsBeen.remove(position);
                        //                            taskDetailsBeanArraylist.remove(position);
                        //                            buddyArrayAdapter.notifyDataSetChanged();
                        //
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
                    return false;
                }
            });

            image_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), SearchMediaWebView.class);
                    intent.putExtra("urlload", "searchmedia");
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.right_anim, R.anim.left_anim);

                }
            });
            ProjectSearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(final CharSequence s, int start, int before, final int count) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] value = new String[count];
                            try {
                                Log.d("constraint", "JNDSEJBJW  * " + s);
                                //                buddyArrayAdapter.getFilter().filter(s);
                                if (s != null && s.length() > 0)
                                    ProjectsFragment.this.projectArrayAdapter.getFilter().filter(s);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        /*ProjectSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    Log.i("Focus"," 1 "+hasFocus);
                    ProjectSearch.setCursorVisible(true);
                }else {
                    Log.i("Focus"," 2 "+hasFocus);
                    ProjectSearch.setCursorVisible(false);
                }
            }
        });*/

            listview_project.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        ProjectSearch.setText("");
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
                            Appreference.jsonRequestSender.getTaskForJobID(EnumJsonWebservicename.getTaskForJobID, nameValuePairs, ProjectsFragment.this);
                        } else {
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
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            Log.i("task", "checkedProject" + Appreference.isProject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onClick(View v) {

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
                TextView project_status = (TextView) finalConView.findViewById(R.id.project_status);
                TextView percent_update = (TextView) finalConView.findViewById(R.id.percent_update);
                TextView msg_count = (TextView) finalConView.findViewById(R.id.item_counter);
                TextView project_type = (TextView) finalConView.findViewById(R.id.catagory);
                LinearLayout layoutcard = (LinearLayout) finalConView.findViewById(R.id.layoutcardview);
                project_type.setVisibility(View.GONE);
                ImageView project_icon = (ImageView) finalConView.findViewById(R.id.selected);
                View viewforparent = (View) finalConView.findViewById(R.id.viewforparent);
                ImageView dependency_icon = (ImageView) finalConView.findViewById(R.id.dependency_icon);
                final ImageView completed_status = (ImageView) finalConView.findViewById(R.id.completed_status);
                viewforparent.setVisibility(View.GONE);
                project_icon.setVisibility(View.GONE);
                project_status.setVisibility(View.GONE);
                percent_update.setVisibility(View.GONE);
                dependency_icon.setVisibility(View.GONE);
                completed_status.setVisibility(View.GONE);
                project_name.setVisibility(view.GONE);
                task_giver.setVisibility(View.GONE);
                Log.i("job123", "project name8*********** " + projectDetailsBean.getProjectName());
                Log.i("job123", "projct Status isActivestatus*********** " + projectDetailsBean.getProjectName() + "isAcvtiveStatus===>" + projectDetailsBean.getIsActiveStatus());

               /* if(projectDetailsBean.getIsActiveStatus()!=null && projectDetailsBean.getIsActiveStatus().equalsIgnoreCase("1")) {
                    proj_layout.setBackgroundResource(R.color.lightgray);
                }else
                    proj_layout.setBackgroundResource(R.color.white);*/

                project_id.setText("Job Card Number  : " + projectDetailsBean.getOracleProjectId());
//                project_name.setText("Job Card Number  : " +projectDetailsBean.getProjectName());
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
                task_giver.setText(pjt_owner);
                if (projectDetailsBean.getProjectCompletedPercentage() != null && projectDetailsBean.getProjectCompletedPercentage().equalsIgnoreCase("100")) {
                    project_status.setText("Closed");
                } else {
                    project_status.setText("Open");
                }
                if (projectDetailsBean.getProjectCompletedPercentage() != null) {
                    percent_update.setText(projectDetailsBean.getProjectCompletedPercentage() + "%");
                } else {
                    percent_update.setText("0%");
                }
                percent_update.setTextColor(RED);
                int project_unReadMsg_count = VideoCallDataBase.getDB(classContext).getProjectsUnReadMsgCount(projectDetailsBean.getId());
                if (project_unReadMsg_count == 0) {
                    Log.d("ProjectHistory", "unRead_project_count is 0");
                    msg_count.setVisibility(View.GONE);
                    project_status.setPadding(0, 0, 0, 0);
                } else {
                    Log.i("ProjectHistory", "unRead_project_count is " + project_unReadMsg_count);
                    msg_count.setVisibility(View.VISIBLE);
                    msg_count.setText(String.valueOf(project_unReadMsg_count));
                    project_status.setPadding(0, 0, 40, 0);
                }
                if (projectDetailsBean.getRequestStatus() != null && projectDetailsBean.getRequestStatus().equalsIgnoreCase("Open")) {
                    dependency_icon.setVisibility(View.VISIBLE);
                    Log.i("attention", "resolved 1 " + projectDetailsBean.getRequestStatus());
                } else {
                    dependency_icon.setVisibility(View.GONE);
                    Log.i("attention", "resolved 2 " + projectDetailsBean.getRequestStatus());
                }
                completed_status.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(getActivity(), completed_status);
                        popup.getMenuInflater().inflate(R.menu.project_pop_menu, popup.getMenu());
                      /*  if(Appreference.loginuserdetails != null && Appreference.loginuserdetails.getRoleId() != null
                                && Appreference.loginuserdetails.getRoleId().equalsIgnoreCase("2")){
                            popup.getMenu().getItem(1).setVisible(false);

                        }*/
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                /*Toast.makeText(getActivity(),
                                        "Clicked popup menu item " + item.getTitle(),
                                        Toast.LENGTH_SHORT).show();*/
                                if (item.getTitle().toString().equalsIgnoreCase("FSR Report")) {
                                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                                    nameValuePairs.add(new BasicNameValuePair("projectId", projectDetailsBean.getId()));
                                    showprogress("Downloading...");
                                    Appreference.jsonRequestSender.OracleFSRJOBReport(EnumJsonWebservicename.fieldServiceReportJobWise, nameValuePairs, ProjectsFragment.this);

                                } else if (item.getTitle().toString().equalsIgnoreCase("Complete")) {
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
                                                    Appreference.jsonRequestSender.projectCompleted(EnumJsonWebservicename.projectCompleted, nameValuePairs, ProjectsFragment.this);
                                                    showprogress("Please wait...");
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                //                                            Toast.makeText(getContext(), "Project Completed", Toast.LENGTH_LONG).show();
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
                        s.add(projectSearchList.get(i));
                        Log.i("constraint", " s 3 " + s.get(i).getTaskName());
                        //Log.i("sizes","sizes"+s.toString());
                        String s2 = s.get(i).getProjectName().toString().toLowerCase();
                        Log.i(" constraint ", " s2  4 " + s2.toString());

                        if (s2.contains(s1)) {
                            Log.i("constraint", "insideif 5 " + String.valueOf(constraint));
                            taskdetailsbeanlist.add(s.get(i));
                            Log.i("constraint", "List 6 " + taskdetailsbeanlist.toString());
                        }

                    }
                    result.values = taskdetailsbeanlist;
                    Log.i("constraint", "result 7 " + result.values.toString());
                    result.count = taskdetailsbeanlist.size();
                    Log.i("constraint", "resultcount 8 " + result.count);

                } else {
                    synchronized (this) {
                        result.values = projectSearchList;
                        Log.i("constraint", "resultelse 9 " + result.values);
                        result.count = projectSearchList.size();
                        Log.i("constraint", "resultelsecount 10 " + result.count);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

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
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            String query_1 = "select *,cast(oracleProjectId as unsigned) as t from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'and projectcompletedstatus NOT IN (select projectcompletedstatus where projectcompletedstatus like '1') order by t DESC";
            projectList = new ArrayList<>();
            projectSearchList = new ArrayList<>();
            projectList = VideoCallDataBase.getDB(classContext).getProjectdetails(query_1);
            projectSearchList = VideoCallDataBase.getDB(classContext).getProjectdetails(query_1);
            projectArrayAdapter = new ProjectArrayAdapter(getActivity(), projectList);
            listview_project.setAdapter(projectArrayAdapter);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    projectArrayAdapter.notifyDataSetChanged();
                }
            });
            if (Appreference.context_table.containsKey("mainactivity")) {
                Log.d("Project", "Inside if in projectFragment onResume");
                MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");
                mainActivity.BadgeReferece();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showprogress(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("expand", "inside show progress--------->");
                    if (progress == null) {
                        progress = new ProgressDialog(getActivity());
                        progress.setCancelable(false);
                        progress.setMessage(message);
                        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progress.setProgress(0);
                        progress.setMax(1000);
                        progress.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
                    }
                }
            });
        }
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

    public void showToast(final String msg) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG)
                        .show();
            }
        });
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
                }
            }
        });
    }

    private boolean getNetworkState() {
        boolean isNetwork = false;
        ConnectivityManager ConnectionManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (ConnectionManager != null) {
            NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected() == true)
                isNetwork = true;
            else
                isNetwork = false;
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
                            Log.i("Response", "getProject " + s1);
                            try {
                                Type collectionType = new TypeToken<ProjectDetailsBean>() {
                                }.getType();
                                ProjectDetailsBean pdb = new Gson().fromJson(s1, collectionType);
                                Log.i("Response", "getProject name " + pdb.getProjectName());
//                                VideoCallDataBase.getDB(classContext).insertProject_history(pdb);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            cancelDialog();
                            Intent intent = new Intent(getActivity(), ProjectHistory.class);
                            intent.putExtra("projectId", project_id);
                            intent.putExtra("projectName", project_name);
                            //                intent.putExtra("parentTaskId", parentTask_Id);
                            intent.putExtra("projectOwner", project_owner);
                            intent.putExtra("groupUserId", groupuser_Id);
                            startActivity(intent);
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
                            }
                        } else if (s2.equalsIgnoreCase("getAllJobDetails")) {
                            Log.i("ws123", "inside Response of json webservice**************************");
                            Log.i("ws123", "getAllJobDetails response-->" + s1);
                            try {
                                JSONArray jr = new JSONArray(s1);
                                if (jr.length() > 0) {
                                    JSONObject jb = jr.getJSONObject(0);
                                    if (jb.has("listallProject")) {
                                        Type collectionType = new TypeToken<List<ListallProjectBean>>() {
                                        }.getType();
                                        List<ListallProjectBean> pdb = new Gson().fromJson(s1, collectionType);
                                        for (int i = 0; i < pdb.size(); i++) {
                                            ListallProjectBean listallProjectBean = pdb.get(i);
                                            Log.i("listAllMyJob", "listallProject size2222====>" + listallProjectBean.getListallProject().size());
                                            if (listallProjectBean.getListallProject() != null && listallProjectBean.getListallProject().size() > 0) {
                                                for (int j = 0; j < listallProjectBean.getListallProject().size(); j++) {
                                                    ProjectDetailsBean projectDetailsBean1 = listallProjectBean.getListallProject().get(j);
                                                    VideoCallDataBase.getDB(classContext).insertProject_Details(projectDetailsBean1);
                                                }
                                            } else
                                                showToast("No JobCards Found..");
                                        }
                                    }
                                }
                                String query_1 = "select *,cast(oracleProjectId as unsigned) as t from projectDetails where loginuser = '" + Appreference.loginuserdetails.getEmail() + "'and projectcompletedstatus NOT IN (select projectcompletedstatus where projectcompletedstatus like '1') order by t DESC";
                                projectList = new ArrayList<>();
                                projectSearchList = new ArrayList<>();
                                projectList = VideoCallDataBase.getDB(classContext).getProjectdetails(query_1);
                                projectSearchList = VideoCallDataBase.getDB(classContext).getProjectdetails(query_1);
                                NoResults.setVisibility(View.GONE);
                                projectArrayAdapter = new ProjectArrayAdapter(getActivity(), projectList);
                                listview_project.setAdapter(projectArrayAdapter);
                                projectArrayAdapter.notifyDataSetChanged();
                                cancelDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (s2.equalsIgnoreCase("getTaskForJobID")) {
                            Log.i("ws123", "******getTaskForJobID********** Response String " + s1);
                            boolean Success = true;
                            try {
                                final JSONObject jsonObject = new JSONObject(opr.getEmail());
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

                                    VideoCallDataBase.getDB(classContext).insertProject_history(pdb);
                                    VideoCallDataBase.getDB(classContext).insert_updateProjectStatus(pdb);

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
                            }
                            cancelDialog();
                            if (Success) {
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
                                    Toast.makeText(getContext(), "JobCard Completed Successfully", Toast.LENGTH_LONG).show();
                                } else {
                                    String result = (String) jsonObject.get("result_text");
                                    Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (s2 != null && s2.equalsIgnoreCase(("fieldServiceReportJobWise"))) {
                            Log.i("output123", "projectFragment fieldServiceReportJobWise  Responce Received" + s1);
                            final JSONObject jsonObject = new JSONObject(opr.getEmail());
                            cancelDialog();
                            try {
                                if (((String) jsonObject.get("result_text")).equalsIgnoreCase("Field_servicce_report job successed")) {
                                    Log.i("output123", " Filename" + jsonObject.getString("filename"));
                                    String pdfURL = getResources().getString(R.string.task_reminder) + jsonObject.getString("filename");
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfURL));
                                    startActivity(browserIntent);
                                }else if(((String) jsonObject.get("result_text")).equalsIgnoreCase("Work_service_report job successed")){
                                    Log.i("output123", " Filename" + jsonObject.getString("filename"));
                                    String pdfURL = getResources().getString(R.string.task_reminder) + jsonObject.getString("filename");
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(pdfURL));
                                    startActivity(browserIntent);
                                } else {
                                    String result = (String) jsonObject.get("result_text");
                                    Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
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
                                    new DownloadImage(pdfURL, jsonObject.getString("filename")).execute();
                                } else {
                                    String result = (String) jsonObject.get("result_text");
                                    Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
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
                    showprogress("Downloading xls...");
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
                                e.printStackTrace();
                                Appreference.printLog("MainActivity", "DownloadImage Exception: " + e.getMessage(), "WARN", null);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Appreference.printLog("MainActivity", "DownloadImage Exception: " + e.getMessage(), "WARN", null);
                            }
                        }
                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity", "DownloadImage Exception: " + e.getMessage(), "WARN", null);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            cancelDialog();
            try {
                File file;
                file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/High Message/downloads/" + namevalue);
                if (file.exists()) {
                    Uri path = Uri.fromFile(file);
                    Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                    pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pdfOpenintent.setDataAndType(path, "application/vnd.ms-excel");
                    try {
                        startActivity(pdfOpenintent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(getActivity(), "Please Install MS-Excel app to view the file", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "No Pdf Documentation", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                Appreference.printLog("MainActivity onPostExecute", "DownloadImage Exception: " + e.getMessage(), "WARN", null);
            }
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }
    }

}
