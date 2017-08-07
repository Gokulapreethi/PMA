package com.ase.DatePicker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ase.Appreference;
import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;
import com.ase.DateTimePicker;
import com.ase.NewTaskConversation;
import com.ase.R;
import com.ase.TravelJobDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CustomTravelPickerActivity extends Activity implements DateTimePicker.DateWatcher {

    private Context context;
    String taskNameshow, projectIDshow, taskIDshow,jobcodeno,activitycode;
    String StartDate, EndDate,StartDateFilled;
    boolean isTravel = false;
    boolean isStartEndFilled = false;
    boolean isStartOnlyFilled = false;
    boolean isValidDate = false;
    String currentDateTimeString;


    String date1, date2, date3,date4;
    Date date_from, date_to, date_from1,date_initial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_end_time_show);
        context = this;
        taskNameshow = getIntent().getStringExtra("taskName");
        projectIDshow = getIntent().getStringExtra("projectID");
        taskIDshow = getIntent().getStringExtra("taskID");
        jobcodeno = getIntent().getStringExtra("jobcodeno");
        activitycode = getIntent().getStringExtra("activitycode");
        isTravel = getIntent().getBooleanExtra("isTravel", false);
        final java.sql.Date todayDate = new java.sql.Date(System.currentTimeMillis());
        final Calendar c = Calendar.getInstance();

        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
        currentDateTimeString= df.format(c.getTime());
        Log.i("travel123", "currentTime=======>" + currentDateTimeString);
        Log.i("travel123", "todayDate=======>" + todayDate);
        final Button travelstart_sign = (Button) findViewById(R.id.travelstart_sign);
        travelstart_sign.setEnabled(true);
        Button travelend_sign = (Button) findViewById(R.id.travelend_sign);


        final TextView project_name = (TextView) findViewById(R.id.project_name);
        final TextView travel_start = (TextView) findViewById(R.id.travel_start);
        final TextView travel_end = (TextView) findViewById(R.id.travel_end);
        final TextView back = (TextView) findViewById(R.id.back);
        final ImageView send_travel = (ImageView) findViewById(R.id.send_travel_completion);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
            }
        });
//        project_name.setText(taskNameshow);
        project_name.setText("Job Card No : "+ jobcodeno +"\nActivity Code : "+activitycode);
        String query = "select * from projectStatus where projectId='" + projectIDshow + "' and userId='" + Appreference.loginuserdetails.getId() + "' and taskId= '" + taskIDshow + "' and status = '7'";
        TaskDetailsBean bean = VideoCallDataBase.getDB(context).getActivityTimeFromStatus(query);
        if (bean != null) {
            if (bean.getTravelStartTime() != null  && !bean.getTravelStartTime().equalsIgnoreCase("")
                    && bean.getTravelEndTime() != null && !bean.getTravelEndTime().equalsIgnoreCase("")) {
                isStartEndFilled = true;
            } else if (bean.getTravelStartTime() != null && !bean.getTravelStartTime().equalsIgnoreCase("")) {
                isStartOnlyFilled = true;
                StartDate = bean.getTravelStartTime();
                StartDateFilled=bean.getTravelStartTime();
//                travel_start.setText(bean.getActivityStartTime());
                travelstart_sign.setEnabled(false);

                //new Code
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                    SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date dt_temp = originalFormat.parse(bean.getTravelStartTime());
                    travel_start.setText(dateFormat.format(dt_temp));

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }


        travelstart_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //New Code Start
                    final Dialog mDateTimeDialog = new Dialog(context);
                    // Inflate the root layout
                    final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater().inflate(R.layout.new_date_picker, null);

                    // Grab widget instance
                    final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);
                    mDateTimePicker.setDateChangedListener(CustomTravelPickerActivity.this);

                    // Update demo TextViews when the "OK" button is clicked
                    ((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            mDateTimePicker.clearFocus();
                            // TODO Auto-generated method stub
                            String result_string = mDateTimePicker.getMonth() + "-" + String.valueOf(mDateTimePicker.getDay()) + "-" + String.valueOf(mDateTimePicker.getYear())
                                    + "  " + String.valueOf(mDateTimePicker.getHour()) + ":" + String.valueOf(mDateTimePicker.getMinute());
                            Date date_from=null;
                            final Calendar c_date1 = Calendar.getInstance();

                            try {
                                date_from=new SimpleDateFormat("MMM-d-yyyy HH:mm").parse(result_string);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (date3 == null) {
                                try {
                                    date3 = sdf.format(c_date1.getTime());
                                    date_from = new SimpleDateFormat("MMM-d-yyyy HH:mm").parse(result_string);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            try {
                                date_to = sdf.parse(date3);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.i("Date123","Date arrange===>"+result_string);
                            Log.i("Date123","Date From===>"+date_from);
                            Log.i("Date123","Date to===>"+date_to);
                            if (date_from.compareTo(date_to) > 0) {
                                Toast.makeText(getApplicationContext(), "Please select Still today date", Toast.LENGTH_SHORT).show();
//                                totimepickerDialog.cancel();
                            } else if (date_from.compareTo(date_to) <= 0) {
                                String form=sdf.format(date_from);
                                Log.i("Trends","from date value is       "+form);

//                                Log.i("Trends","to_date is   "+to_date.getText().toString());
                                Log.i("Trends","sdf format to date is   "+sdf.format(date_to));
                                try {
                                    if(travel_end.getText().toString().length()>0) {
                                        date_initial = sdf.parse(travel_end.getText().toString());
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if(date_initial!=null && travel_end.getText().toString().length()>0) {
                                    if (date_from.compareTo(date_initial) > 0) {
                                        Toast.makeText(getApplicationContext(), "Can't set", Toast.LENGTH_SHORT).show();
                                    } else {
                                        travel_start.setText(form);
                                    }
                                }
                                else {
                                    travel_start.setText(form);
                                }
                            }
                            date3=null;
                            mDateTimeDialog.dismiss();
                        }
                    });

                    // Cancel the dialog when the "Cancel" button is clicked
                    ((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog)).setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            mDateTimeDialog.cancel();
                        }
                    });

                    // Reset Date and Time pickers when the "Reset" button is clicked

                    ((Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime)).setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            mDateTimePicker.reset();
                        }
                    });

                    // Setup TimePicker
                    // No title on the dialog window
                    mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    // Set the dialog content view
                    mDateTimeDialog.setContentView(mDateTimeDialogView);
                    // Display the dialog
                    mDateTimeDialog.show();
                    //New Code End
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        travelend_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    // Create the dialog
                    //New Code Start
                    final Dialog mDateTimeDialog = new Dialog(context);
                    // Inflate the root layout
                    final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater().inflate(R.layout.new_date_picker, null);

                    // Grab widget instance
                    final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView.findViewById(R.id.DateTimePicker);
                    mDateTimePicker.setDateChangedListener(CustomTravelPickerActivity.this);
                    mDateTimeDialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    // Update demo TextViews when the "OK" button is clicked
                    ((Button) mDateTimeDialogView.findViewById(R.id.SetDateTime)).setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            if(travel_start.getText().toString().trim().length()>0) {
                                mDateTimePicker.clearFocus();
                                // TODO Auto-generated method stub
                                String result_string = mDateTimePicker.getMonth() + "-" + String.valueOf(mDateTimePicker.getDay()) + "-" + String.valueOf(mDateTimePicker.getYear())
                                        + "  " + String.valueOf(mDateTimePicker.getHour()) + ":" + String.valueOf(mDateTimePicker.getMinute());
                                date1=null;
                                date4=null;
                                final Calendar c_date = Calendar.getInstance();

                                try {
                                    date_to = new SimpleDateFormat("MMM-d-yyyy HH:mm").parse(result_string);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
//                            to_date.setText(sdf.format(date_to));
                                if (date1 == null) {
                                    try {
                                        Log.i("datetime","start date-->"+travel_start.getText().toString());
                                            date1 = travel_start.getText().toString();
                                            date_from = sdf.parse(date1);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (date4 == null) {
                                    try {
                                        date4 = sdf.format(c_date.getTime());
                                        date_from1 = sdf.parse(date4);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                Log.i("Date123","date_from1.compareTo(date_to) < 0***********8");
                                Log.i("Date123","endDate arrange ===>"+result_string);
                                Log.i("Date123","endDate From ===>"+date_from);
                                Log.i("Date123","endDate to ===>"+date_to);
                                Log.i("Date123","endDate current date date_from1 ===>"+date_from1);
                                if (date_from!=null && date_to!=null && date_from.compareTo(date_to) > 0) {
                                    Toast.makeText(getApplicationContext(), "Please select above from date", Toast.LENGTH_SHORT).show();
//                                totimepickerDialog.cancel();
                                } else if (date_from1!=null && date_to!=null && date_from1.compareTo(date_to) < 0) {
                                    Toast.makeText(getApplicationContext(), "please Select date till today", Toast.LENGTH_SHORT).show();
                                } else {
                                    travel_end.setText(sdf.format(date_to));
                                }
                                mDateTimeDialog.dismiss();
                            }else{
                                Toast.makeText(getApplicationContext(), "Plese Fill Start Date Time", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    // Cancel the dialog when the "Cancel" button is clicked
                    ((Button) mDateTimeDialogView.findViewById(R.id.CancelDialog)).setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            mDateTimeDialog.cancel();
                        }
                    });

                    // Reset Date and Time pickers when the "Reset" button is clicked

                    ((Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime)).setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            mDateTimePicker.reset();
                        }
                    });

                    // Setup TimePicker
                    // No title on the dialog window
                    mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    // Set the dialog content view
                    mDateTimeDialog.setContentView(mDateTimeDialogView);
                    // Display the dialog
                    mDateTimeDialog.show();

                    //Code End
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
               try {

//                   if (getNetworkState()) {
                       //New Code Start
                       SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                       SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                       Date dt_temp;
                       if (travel_start.getText().toString().length() > 0) {
                           dt_temp = dateFormat.parse(travel_start.getText().toString());
                           StartDate=originalFormat.format(dt_temp);
                       }
                       if (travel_end.getText().toString().length() > 0) {
                           dt_temp = dateFormat.parse(travel_end.getText().toString());
                           EndDate=originalFormat.format(dt_temp);
                       }

                       //New Code End

                       if (StartDate != null || EndDate != null) {
                           NewTaskConversation newTaskConversation = (NewTaskConversation) Appreference.context_table.get("taskcoversation");
                           if (!isTravel && newTaskConversation != null) {
                               if (isStartOnlyFilled && EndDate != null && !EndDate.equalsIgnoreCase("")) {
                                   newTaskConversation.getEnteredTravelTime(StartDateFilled, EndDate,"9");
                                   finish();
                               } else if (!isStartOnlyFilled && StartDate != null && !StartDate.equalsIgnoreCase("") || EndDate != null && !EndDate.equalsIgnoreCase("")) {
                                   newTaskConversation.getEnteredTravelTime(StartDate, EndDate,"7");
                                   finish();
                               } else
                                   Toast.makeText(CustomTravelPickerActivity.this, "Please Enter DateTime to send", Toast.LENGTH_SHORT).show();

                           } else {
                               TravelJobDetails travelJobDetails = (TravelJobDetails) Appreference.context_table.get("traveljobdetails");
                               if (travelJobDetails != null && isTravel) {
                                   if (isStartOnlyFilled && EndDate != null && !EndDate.equalsIgnoreCase("")) {
                                       travelJobDetails.getEnteredTravelTime(StartDateFilled, EndDate,"9");
                                       finish();
                                   } else if (!isStartOnlyFilled && StartDate != null && !StartDate.equalsIgnoreCase("") || EndDate != null && !EndDate.equalsIgnoreCase("")) {
                                       travelJobDetails.getEnteredTravelTime(StartDate, EndDate,"7");
                                       finish();
                                   } else
                                       Toast.makeText(CustomTravelPickerActivity.this, "Please Enter DateTime to send", Toast.LENGTH_SHORT).show();
                               }
                           }
                       } else
                           Toast.makeText(CustomTravelPickerActivity.this, "Please Enter DateTime to send", Toast.LENGTH_SHORT).show();
//                   }else
//                       Toast.makeText(CustomTravelPickerActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();

               }catch (Exception e){
                   e.printStackTrace();
               }
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

    private boolean getNetworkState()
    {
        boolean isNetwork=false;
        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(ConnectionManager!=null) {
            NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected() == true)
                isNetwork= true;
            else
                isNetwork= false;
        }
        return isNetwork;
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
    }
    @Override
    public void onDateChanged(Calendar c) {
        Log.i("TrendsActivity","Month :" + c.get(Calendar.MONTH) + "Day :" + c.get(Calendar.DAY_OF_MONTH) + "Year :" + c.get(Calendar.YEAR));
    }
}
