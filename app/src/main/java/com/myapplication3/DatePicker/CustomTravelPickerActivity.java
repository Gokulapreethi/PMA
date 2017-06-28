package com.myapplication3.DatePicker;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.myapplication3.Appreference;
import com.myapplication3.Bean.TaskDetailsBean;
import com.myapplication3.DB.VideoCallDataBase;
import com.myapplication3.NewTaskConversation;
import com.myapplication3.R;
import com.myapplication3.TravelJobDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CustomTravelPickerActivity extends Activity {

    private Context context;
    String taskNameshow, projectIDshow, taskIDshow;
    String StartDate, EndDate;
    boolean isTravel = false;
    boolean isStartEndFilled = false;
    boolean isStartOnlyFilled = false;
    boolean isValidDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_end_time_show);
        context = this;
        taskNameshow = getIntent().getStringExtra("taskName");
        projectIDshow = getIntent().getStringExtra("projectID");
        taskIDshow = getIntent().getStringExtra("taskID");
        isTravel = getIntent().getBooleanExtra("isTravel", false);
        final java.sql.Date todayDate = new java.sql.Date(System.currentTimeMillis());
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
        final String currentDateTimeString = df.format(c.getTime());
        Log.i("travel123", "currentTime=======>" + currentDateTimeString);
        Log.i("travel123", "todayDate=======>" + todayDate);
        Button travelstart_sign = (Button) findViewById(R.id.travelstart_sign);
        Button travelend_sign = (Button) findViewById(R.id.travelend_sign);


        final TextView project_name = (TextView) findViewById(R.id.project_name);
        final TextView travel_start = (TextView) findViewById(R.id.travel_start);
        final TextView travel_end = (TextView) findViewById(R.id.travel_end);
        final TextView back = (TextView) findViewById(R.id.back);
        final TextView send_travel = (TextView) findViewById(R.id.send_travel_completion);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        project_name.setText(taskNameshow);
        String query = "select * from projectStatus where projectId='" + projectIDshow + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + taskIDshow + "'";
        TaskDetailsBean bean = VideoCallDataBase.getDB(context).getActivityTimeFromStatus(query);
        if (bean != null) {
            if (bean.getActivityStartTime() != null && bean.getActivityEndTime() != null) {
                isStartEndFilled = true;
            } else if (bean.getActivityStartTime() != null && bean.getActivityEndTime() == null) {
                isStartOnlyFilled = true;
                StartDate = bean.getActivityStartTime();
                travel_start.setText(bean.getActivityStartTime());
                travelstart_sign.setEnabled(false);
            }
        }

        travelstart_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(context, true, new DatePickerPopWin.OnDatePickedListener() {

                        @Override
                        public void onDatePickCompleted(int month, int day, int year, int hour, int minute, String am, String dateDesc) {
                            Log.i("desc123", "inside onDatePickedCompleted");

                            String inputPattern = "M-dd-yyyy hh:mm aa";
                            String outputPattern = "yyyy-MM-dd HH:mm:ss";
                            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
                            SimpleDateFormat outputFormatUI = new SimpleDateFormat(outputPattern);
                            outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                            Date date123 = null;
                            String str = null;

                            try {
                                date123 = inputFormat.parse(dateDesc);
                                str = outputFormatUI.format(date123);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (str != null) {
                                if (date123 != null && (date123.compareTo(todayDate) < 0 || date123.compareTo(todayDate) == 0)) {
                                    travel_start.setText(str);
                                    StartDate = str;
                                }else
                                    Toast.makeText(CustomTravelPickerActivity.this, "Please Select Correct DateTime", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).textConfirm("DONE") //text of confirm button
                            .textCancel("CANCEL") //text of cancel button
                            .btnTextSize(16) // button text size
                            .viewTextSize(25) // pick view text size
                            .minYear(Calendar.getInstance().get(Calendar.YEAR)) //min year in loop
                            .maxYear(2550) // max year in loop
                            .dateChose(todayDate.toString()) // date chose when init popwindow
                            .currenttime(currentDateTimeString).build();
                    Log.i("desc123", "inside DatePickerPopWin.Builder");

                    pickerPopWin.showPopWin(CustomTravelPickerActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        travelend_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(context, true, new DatePickerPopWin.OnDatePickedListener() {

                        @Override
                        public void onDatePickCompleted(int month, int day, int year, int hour, int minute, String am, String dateDesc) {

                            Log.i("desc123", "inside onDatePickedCompleted");
                            String inputPattern = "M-dd-yyyy hh:mm aa";
                            String outputPattern = "yyyy-MM-dd HH:mm:ss";
                            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
                            SimpleDateFormat outputFormatUI = new SimpleDateFormat(outputPattern);
                            outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                            Date date123 = null;
                            String str = null;

                            try {
                                date123 = inputFormat.parse(dateDesc);
                                str = outputFormatUI.format(date123);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (StartDate != null && !StartDate.equalsIgnoreCase("")) {
                                    if (isValidDate(StartDate, str)) {
                                        travel_end.setText(str);
                                        EndDate = str;
                                    } else
                                        Toast.makeText(CustomTravelPickerActivity.this, "Please Select Correct DateTime", Toast.LENGTH_SHORT).show();
                            }else
                                Toast.makeText(CustomTravelPickerActivity.this, "Please Enter Start DateTime", Toast.LENGTH_SHORT).show();

                        }
                    }).textConfirm("DONE") //text of confirm button
                            .textCancel("CANCEL") //text of cancel button
                            .btnTextSize(16) // button text size
                            .viewTextSize(25) // pick view text size
                            .minYear(Calendar.getInstance().get(Calendar.YEAR)) //min year in loop
                            .maxYear(2550) // max year in loop
                            .dateChose(todayDate.toString()) // date chose when init popwindow
                            .currenttime(currentDateTimeString).build();
                    Log.i("desc123", "inside DatePickerPopWin.Builder");

                    pickerPopWin.showPopWin(CustomTravelPickerActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        send_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intentMessage=new Intent();
                if(StartDate!=null)
                intentMessage.putExtra("DateStart",StartDate);
                if(EndDate!=null)
                intentMessage.putExtra("DateEnd",EndDate);
                setResult(120,intentMessage);*/
                if(StartDate!=null || EndDate!=null) {
                    NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                    if (!isTravel && newTaskConversation != null) {
                        if (isStartOnlyFilled && EndDate!=null && !EndDate.equalsIgnoreCase("")) {
                            newTaskConversation.getEnteredTravelTime("", EndDate);
                            finish();
                        }else if(!isStartOnlyFilled && StartDate!=null && !StartDate.equalsIgnoreCase("") || EndDate!=null && !EndDate.equalsIgnoreCase("")) {
                            newTaskConversation.getEnteredTravelTime(StartDate, EndDate);
                            finish();
                        }else
                            Toast.makeText(CustomTravelPickerActivity.this, "Please Enter DateTime to send", Toast.LENGTH_SHORT).show();

                    } else {
                        TravelJobDetails travelJobDetails = (TravelJobDetails) Appreference.context_table.get("traveljobdetails");
                        if (travelJobDetails != null && isTravel) {
                            if (isStartOnlyFilled && EndDate!=null && !EndDate.equalsIgnoreCase("")) {
                                travelJobDetails.getEnteredTravelTime("", EndDate);
                                finish();
                            }else if( !isStartOnlyFilled  && StartDate!=null && !StartDate.equalsIgnoreCase("") || EndDate!=null && !EndDate.equalsIgnoreCase("")) {
                                travelJobDetails.getEnteredTravelTime(StartDate, EndDate);
                                finish();
                            } else
                                Toast.makeText(CustomTravelPickerActivity.this, "Please Enter DateTime to send", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else
                    Toast.makeText(CustomTravelPickerActivity.this, "Please Enter DateTime to send", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValidDate(String start, String UserGivenDate) {
        boolean isValid = false;
        final java.sql.Date todayDate = new java.sql.Date(System.currentTimeMillis());
        final SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = myFormat.parse(start);
            date2 = myFormat.parse(UserGivenDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (date2.compareTo(date1) > 0 && date2.compareTo(todayDate)<0) {
            isValid = true;
        } else if (date2.compareTo(date1) == 0 &&  date2.compareTo(todayDate)==0) {
            isValid = true;
        } else if (date2.compareTo(date1) < 0) {
            isValid = false;
        }
        return isValid;
    }
}
