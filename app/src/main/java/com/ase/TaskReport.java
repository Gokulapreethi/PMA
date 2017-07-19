package com.ase;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.ase.Bean.TaskDetailsBean;
import com.ase.DB.VideoCallDataBase;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import json.CommunicationBean;
import json.WebServiceInterface;

/**
 * Created by Preethi on 22-05-2017.
 */
public class  TaskReport extends Activity implements WebServiceInterface {
    LinearLayout fieldContainer;
    private Handler handler = new Handler();
    private HashMap<String, TaskReportBean> fieldValuesMap = new HashMap<String, TaskReportBean>();
    TextView back,share,jobno,customername,taskId,taskDescription,estimatedtravelhrs,
            estimatedactivityhrs,starttime,endtime,actualtotalhrs,activitytotalhrs,employeename,cust_address,mac_model,mac_no;
    Context context=this;
    ProgressDialog progress;
    HashMap<String, TaskReportBean> avoidDuplicateValues=null;
    HashMap<String,String> clientReport=null;
    static TaskReport taskReport;
    public static TaskReport getInstance() {
        return taskReport;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_report);
//        showprogress();
        taskReport = this;
        String taskid = getIntent().getExtras().getString("taskid");
        String current_user_show = getIntent().getExtras().getString("user_name");
        boolean isGrouptype = getIntent().getBooleanExtra("isGroup",false);
        Log.i("group123","isGroup*****====>> "+isGrouptype);
        back = (TextView) findViewById(R.id.back);
        share = (TextView) findViewById(R.id.share_mail);
//        jobno=(TextView)findViewById(R.id.jobno);
//        customername=(TextView)findViewById(R.id.customername);
        cust_address=(TextView)findViewById(R.id.cust_address);
        mac_model=(TextView)findViewById(R.id.mac_model);
        mac_no=(TextView)findViewById(R.id.mac_no);
        taskId=(TextView)findViewById(R.id.TaskId);
        taskDescription=(TextView)findViewById(R.id.taskDescription);
        estimatedtravelhrs=(TextView)findViewById(R.id.estimatedTravelhrs);
        estimatedactivityhrs=(TextView)findViewById(R.id.estimatedActivityhrs);
        starttime=(TextView)findViewById(R.id.starttime);
        endtime=(TextView)findViewById(R.id.endtime);
        actualtotalhrs=(TextView)findViewById(R.id.actualTotalhrs);
        activitytotalhrs=(TextView)findViewById(R.id.activityTotalhrs);
        employeename=(TextView)findViewById(R.id.employeeName);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("Send email", "");
//                if(avoidDuplicateValues!=null && avoidDuplicateValues.size()>0) {
                if(clientReport!=null && clientReport.size()>0) {
                    createPDF(clientReport);
                    File filelocation = new File(Environment.getExternalStorageDirectory()
                            + "/High Message/Report.pdf");
                    if(filelocation.exists()) {
                        String[] TO = {""};
                        String[] CC = {""};

                        Uri path = Uri.fromFile(filelocation);
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setData(Uri.parse("mailto:cognitivemobile.net"));
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                        emailIntent.putExtra(Intent.EXTRA_CC, CC);
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "HM : Custom Report");
                        emailIntent.putExtra(Intent.EXTRA_STREAM, path);

                        try {
                            startActivity(emailIntent);
                            finish();
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(TaskReport.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(TaskReport.this, "No Pdf Documentation", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        if(taskid != null) {
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//            nameValuePairs.add(new BasicNameValuePair("taskId", taskid));
//            Appreference.jsonRequestSender.getCustomHeaderValue(EnumJsonWebservicename.getCustomHeaderValue, nameValuePairs, TaskReport.this);
        }
        fieldContainer = (LinearLayout) findViewById(R.id.fieldContainer);


        //Show Report in Client Side UI AND Db Values
        if(taskid!=null) {
            Log.i("report123","TnA Report id ======>"+taskid);
            ArrayList<TaskDetailsBean> report = new ArrayList<>();
            TaskDetailsBean bean = new TaskDetailsBean();
            ArrayList<TaskDetailsBean> Report_List = VideoCallDataBase.getDB(context).getAllReport(taskid,current_user_show,isGrouptype);
            if (Report_List != null && Report_List.size() > 0) {
                Log.i("getreport", "Report_List size-->" + Report_List.size());
                for (TaskDetailsBean taskDetailsBean : Report_List) {
//                    if (taskDetailsBean.getTaskNo() != null && !taskDetailsBean.getTaskNo().equalsIgnoreCase("") && !taskDetailsBean.getTaskNo().equalsIgnoreCase("null")
//                            && taskDetailsBean.getTaskNo().length() > 0) {
//                        bean.setTaskNo(taskDetailsBean.getTaskNo());
//                    }
                    if (taskDetailsBean.getTaskName() != null && !taskDetailsBean.getTaskName().equalsIgnoreCase("") && !taskDetailsBean.getTaskName().equalsIgnoreCase("null")
                            && taskDetailsBean.getTaskName().length() > 0) {
                        bean.setTaskName(taskDetailsBean.getTaskName());
                    }
                    if (taskDetailsBean.getAddress() != null && !taskDetailsBean.getAddress().equalsIgnoreCase("") && !taskDetailsBean.getAddress().equalsIgnoreCase("null")
                            && taskDetailsBean.getAddress().length() > 0) {
                        bean.setAddress(taskDetailsBean.getAddress());
                    }
                    if (taskDetailsBean.getMcModel() != null && !taskDetailsBean.getMcModel().equalsIgnoreCase("") && !taskDetailsBean.getMcModel().equalsIgnoreCase("null")
                            && taskDetailsBean.getMcModel().length() > 0) {
                        bean.setMcModel(taskDetailsBean.getMcModel());
                    }
                    if (taskDetailsBean.getMcSrNo() != null && !taskDetailsBean.getMcSrNo().equalsIgnoreCase("") && !taskDetailsBean.getMcSrNo().equalsIgnoreCase("null")
                            && taskDetailsBean.getMcSrNo().length() > 0) {
                        bean.setMcSrNo(taskDetailsBean.getMcSrNo());
                    }
                    if (taskDetailsBean.getTaskId() != null && !taskDetailsBean.getTaskId().equalsIgnoreCase("") && !taskDetailsBean.getTaskId().equalsIgnoreCase("null")
                            && taskDetailsBean.getTaskId().length() > 0) {
                        bean.setTaskId(taskDetailsBean.getTaskId());
                    }
                    if (taskDetailsBean.getTaskDescription() != null && !taskDetailsBean.getTaskDescription().equalsIgnoreCase("") && !taskDetailsBean.getTaskDescription().equalsIgnoreCase("null")
                            && taskDetailsBean.getTaskDescription().length() > 0) {
                        bean.setTaskDescription(taskDetailsBean.getTaskDescription());
                    }
                    if (taskDetailsBean.getToUserName() != null && !taskDetailsBean.getToUserName().equalsIgnoreCase("") && !taskDetailsBean.getToUserName().equalsIgnoreCase("null")
                            && taskDetailsBean.getToUserName().length() > 0) {
                        bean.setToUserName(taskDetailsBean.getToUserName());
                    }
                    if (taskDetailsBean.getDateTime() != null && !taskDetailsBean.getDateTime().equalsIgnoreCase("") && !taskDetailsBean.getDateTime().equalsIgnoreCase("null")
                            && taskDetailsBean.getDateTime().length() > 0) {
                        String[] DateTask = taskDetailsBean.getDateTime().split(" ");
                        Log.i("string12", "Date and time-->" + DateTask[0]);
                        bean.setDateTime(DateTask[0]);
                    }
                    if (taskDetailsBean.getEstimatedTravel() != null && !taskDetailsBean.getEstimatedTravel().equalsIgnoreCase("") && !taskDetailsBean.getEstimatedTravel().equalsIgnoreCase("null")
                            && taskDetailsBean.getEstimatedTravel().length() > 0) {
                        Log.i("string12", "travel-->" + taskDetailsBean.getEstimatedTravel());
                        bean.setEstimatedTravel(taskDetailsBean.getEstimatedTravel());
                    }
                    if (taskDetailsBean.getEstimatedActivity() != null && !taskDetailsBean.getEstimatedActivity().equalsIgnoreCase("") && !taskDetailsBean.getEstimatedActivity().equalsIgnoreCase("null")
                            && taskDetailsBean.getEstimatedActivity().length() > 0) {
                        Log.i("string12", "Activity-->" + taskDetailsBean.getEstimatedActivity());
                        bean.setEstimatedActivity(taskDetailsBean.getEstimatedActivity());
                    }
                    if (taskDetailsBean.getTotalTravel() != null && !taskDetailsBean.getTotalTravel().equalsIgnoreCase("") && !taskDetailsBean.getTotalTravel().equalsIgnoreCase("null")
                            && taskDetailsBean.getTotalTravel().length() > 0) {
                        Log.i("string12", "total travel-->" + taskDetailsBean.getTotalTravel());
                        bean.setTotalTravel(taskDetailsBean.getTotalTravel());
                    }
                    if (taskDetailsBean.getTotalActivity() != null && !taskDetailsBean.getTotalActivity().equalsIgnoreCase("") && !taskDetailsBean.getTotalActivity().equalsIgnoreCase("null")
                            && taskDetailsBean.getTotalActivity().length() > 0) {
                        Log.i("string12", "total activity-->" + taskDetailsBean.getTotalActivity());
                        bean.setTotalActivity(taskDetailsBean.getTotalActivity());
                    }
                    if (taskDetailsBean.getStartDate() != null && !taskDetailsBean.getStartDate().equalsIgnoreCase("") && !taskDetailsBean.getStartDate().equalsIgnoreCase("null")
                            && taskDetailsBean.getStartDate().length() > 0) {
                        String[] startTime = taskDetailsBean.getStartDate().split(" ");
                        Log.i("string12", "start date-->" + startTime[1]);
                        bean.setStartDate(startTime[1]);
                    }
                    if (taskDetailsBean.getEndDate() != null && !taskDetailsBean.getEndDate().equalsIgnoreCase("") && !taskDetailsBean.getEndDate().equalsIgnoreCase("null")
                            && taskDetailsBean.getEndDate().length() > 0) {
                        String[] endTime = taskDetailsBean.getEndDate().split(" ");
                        Log.i("string12", "end date-->" + endTime[1]);
                        bean.setEndDate(endTime[1]);
                    }

                }
                report.add(bean);
            }
            if (report != null && report.size() > 0) {
                clientReport=new HashMap<>();
                for(TaskDetailsBean detailsBean:report){
//                    if(detailsBean.getTaskNo()!=null){
//                        clientReport.put("Job No",detailsBean.getTaskNo());
//                    }
                    if(detailsBean.getToUserName()!=null){
                        clientReport.put("Employee Name",detailsBean.getToUserName());
                    }
//                    if(detailsBean.getTaskName()!=null){
//                        clientReport.put("Customer Name",detailsBean.getTaskName());
//                    }
                    if(detailsBean.getAddress()!=null){
                        clientReport.put("Address",detailsBean.getAddress());
                    }if(detailsBean.getMcModel()!=null){
                        clientReport.put("M/c Model",detailsBean.getMcModel());
                    }if(detailsBean.getMcSrNo()!=null){
                        clientReport.put("M/c Sr.No",detailsBean.getMcSrNo());
                    }if(detailsBean.getTaskId()!=null){
                        clientReport.put("Task Id",detailsBean.getTaskId());
                    }if(detailsBean.getTaskDescription()!=null){
                        clientReport.put("Task Description",detailsBean.getTaskDescription());
                    }if(detailsBean.getEstimatedTravel()!=null){
                        clientReport.put("Estimated Travel Hrs",detailsBean.getEstimatedTravel());
                    }if(detailsBean.getEstimatedActivity()!=null){
                        clientReport.put("Estimated Activity Hrs",detailsBean.getEstimatedActivity());
                    }
                    if(detailsBean.getTotalTravel()!=null){
                        clientReport.put("ActualTotalHrs",detailsBean.getTotalTravel());
                    }if(detailsBean.getTotalActivity()!=null){
                        clientReport.put("ActivityTotalHrs",detailsBean.getTotalActivity());
                    }if(detailsBean.getStartDate()!=null){
                        clientReport.put("Start Time",detailsBean.getStartDate());
                    }if(detailsBean.getEndDate()!=null){
                        clientReport.put("End Time",detailsBean.getEndDate());
                    }
                }

                if(clientReport.containsKey("Job No")){
                    jobno.setText(clientReport.get("Job No"));
                }
                if(clientReport.containsKey("Customer Name")){
                    customername.setText(clientReport.get("Customer Name"));
                } if(clientReport.containsKey("Address")){
                    cust_address.setText(clientReport.get("Address"));
                }if(clientReport.containsKey("M/c Model")){
                    mac_model.setText(clientReport.get("M/c Model"));
                }if(clientReport.containsKey("M/c Sr.No")){
                    mac_no.setText(clientReport.get("M/c Sr.No"));
                }if(clientReport.containsKey("Task Id")){
                    taskId.setText(clientReport.get("Task Id"));
                }
                if(clientReport.containsKey("Task Description")){
                    taskDescription.setText(clientReport.get("Task Description"));
                }
                if(clientReport.containsKey("Estimated Travel Hrs")){
                    estimatedtravelhrs.setText(clientReport.get("Estimated Travel Hrs"));
                }if(clientReport.containsKey("Estimated Activity Hrs")){
                    estimatedactivityhrs.setText(clientReport.get("Estimated Activity Hrs"));
                }if(clientReport.containsKey("Start Time")){
                    starttime.setText(clientReport.get("Start Time"));
                }if(clientReport.containsKey("End Time")){
                    endtime.setText(clientReport.get("End Time"));
                }if(clientReport.containsKey("ActualTotalHrs")){
                    actualtotalhrs.setText(clientReport.get("ActualTotalHrs"));
                }if(clientReport.containsKey("ActivityTotalHrs")){
                    activitytotalhrs.setText(clientReport.get("ActivityTotalHrs"));
                }if(clientReport.containsKey("Employee Name")){
                    employeename.setText(clientReport.get("Employee Name"));
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showprogress() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("login123", "inside showProgressDialog");

                    progress = new ProgressDialog(context);
                    progress.setCancelable(false);
                    progress.setMessage("Getting Report");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setProgress(0);
                    progress.setMax(100);
                    progress.show();
                } catch (Exception e) {
//                        SingleInstance.printLog(null, e.getMessage(), null, e);
                }
            }


        });
    }


    public void inflateFields(int mode, final TaskReportBean bean) {
        try {
            View view = getLayoutInflater().inflate(
                    R.layout.task_report_headers, fieldContainer, false);
            view.setTag(bean.getHeaderId());
            final TextView fieldName = (TextView) view.findViewById(R.id.fieldname);
            if (bean.getHeaderTagName() != null) {
                fieldName.setText(bean.getHeaderTagName());
            }
                final TextView fieldValue = (TextView) view
                        .findViewById(R.id.fieldvalue);
                fieldValue.setVisibility(View.VISIBLE);
//                fieldValue.setTag(bean.getDataType());
                if(bean.getValue()!=null && bean.getValue().length()>0){
                    fieldValue.setText(bean.getValue());
                }
            fieldContainer.addView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ResponceMethod(final Object object) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    cancelDialog();
                    CommunicationBean bean = (CommunicationBean) object;
                    String values = bean.getEmail();
                    if(values!=null && values.equalsIgnoreCase("")){
                        showToast("Unable to Generate Report,Please Try Again Later");
                    }

                    JSONArray jsonArray = new JSONArray(values);
                    if (jsonArray.length() > 0) {
                        // jsonArray=jelement.getAsJsonArray();
                        Log.i("response array", "response size" + jsonArray.length());
                        avoidDuplicateValues = new HashMap<String, TaskReportBean>();
                        boolean travelEndtime = false;

                        for (int i = 0; i < jsonArray.length(); i++) {

                            //JSONObject jsonObject1= (JSONObject) jsonArray.getJSONObject(i);
                            Log.i("response ", "objects  " + jsonArray.get(i));
//                            if(i==0 || i==1 || i==7 || i==8 || i==9 || i==10 || i==24 || i==25) {
                            TaskReportBean taskReportBean = new Gson().fromJson(jsonArray.getString(i), TaskReportBean.class);
                            if(taskReportBean.getHeaderTagName() != null &&
                                    (taskReportBean.getHeaderTagName().equalsIgnoreCase("Job No") ||
                                            (taskReportBean.getHeaderTagName().equalsIgnoreCase("Customer Name") ||
                                                    (taskReportBean.getHeaderTagName().equalsIgnoreCase("Task ID")||
                                                            (taskReportBean.getHeaderTagName().equalsIgnoreCase("Task Description")||
                                                                    (taskReportBean.getHeaderTagName().equalsIgnoreCase("Estimated Travel Hrs") ||
                                                                            (taskReportBean.getHeaderTagName().equalsIgnoreCase("Estimated Activity Hrs")||
                                                                                    (taskReportBean .getHeaderTagName().equalsIgnoreCase("ActualTotalHrs")||
                                                                                            (taskReportBean.getHeaderTagName().equalsIgnoreCase("ActivityTotalHrs") ||
                                                                                                    (taskReportBean.getHeaderTagName().equalsIgnoreCase("Travel Start Time") ||
                                                                                                            (taskReportBean.getHeaderTagName().equalsIgnoreCase("Travel End Time")))))))))))) {

                                if (!avoidDuplicateValues.containsKey(taskReportBean.getHeaderTagName()) && !taskReportBean.getHeaderTagName().equalsIgnoreCase("Travel End Time")) {
                                    avoidDuplicateValues.put(taskReportBean.getHeaderTagName(), taskReportBean);
                                    if(taskReportBean.getHeaderTagName().equalsIgnoreCase("Estimated Travel Hrs")) {
                                        taskReportBean.setValue(taskReportBean.getValue()+" H");
                                        avoidDuplicateValues.put(taskReportBean.getHeaderTagName(), taskReportBean);
                                    }
                                    if(taskReportBean.getHeaderTagName().equalsIgnoreCase("Estimated Activity Hrs")) {
                                        taskReportBean.setValue(taskReportBean.getValue()+" H");
                                        avoidDuplicateValues.put(taskReportBean.getHeaderTagName(), taskReportBean);
                                    }
                                    if(taskReportBean.getHeaderTagName().equalsIgnoreCase("ActualTotalHrs")) {
                                        if(taskReportBean.getValue()!=null && taskReportBean.getValue().contains(":")) {
                                            taskReportBean.setValue(taskReportBean.getValue().split(":")[0] + " H " + taskReportBean.getValue().split(":")[1]+" M");
                                        }
                                        avoidDuplicateValues.put(taskReportBean.getHeaderTagName(), taskReportBean);
                                    }
                                    if(taskReportBean.getHeaderTagName().equalsIgnoreCase("ActivityTotalHrs")) {
                                        if(taskReportBean.getValue()!=null && taskReportBean.getValue().contains(":")) {
                                            taskReportBean.setValue(taskReportBean.getValue().split(":")[0] + " H " + taskReportBean.getValue().split(":")[1]+" M");
                                        }
                                        avoidDuplicateValues.put(taskReportBean.getHeaderTagName(), taskReportBean);
                                    }
                                    if (taskReportBean.getHeaderTagName().equalsIgnoreCase("Travel Start Time")) {
                                        if (taskReportBean.getValue() != null) {
                                            taskReportBean.setValue(Appreference.utcToLocalTime(taskReportBean.getValue()));
                                        }
                                        taskReportBean.setHeaderTagName("Start Time");
                                        avoidDuplicateValues.put(taskReportBean.getHeaderTagName(), taskReportBean);
                                    }
//
                                    if (!taskReportBean.getHeaderTagName().equalsIgnoreCase("Travel End Time")) {
                                        inflateFields(0, taskReportBean);
                                    }
                                }
                                if (taskReportBean.getHeaderTagName().equalsIgnoreCase("Travel End Time")) {
                                    Log.i("report", "Travel End Date contains key-->" + taskReportBean.getHeaderTagName());
                                    if (travelEndtime && !avoidDuplicateValues.containsKey(taskReportBean.getHeaderTagName())) {
                                        if (taskReportBean.getValue() != null) {
                                            taskReportBean.setValue(Appreference.utcToLocalTime(taskReportBean.getValue()));
                                        }
                                        taskReportBean.setHeaderTagName("End Time");
                                        avoidDuplicateValues.put("Travel End Time", taskReportBean);
                                        inflateFields(0, taskReportBean);
                                    }
                                    if (!travelEndtime) {
                                        travelEndtime = true;
                                    }
//                                    if(avoidDuplicateValues.containsKey("Travel End Date")) {
//                                        avoidDuplicateValues.remove("Travel End Date");
//
//                                    }

                                }
                            }
//                            }


                        }

                        String usernameEmailId=VideoCallDataBase.getDB(context).getReportUserName();
                        if(usernameEmailId!=null){
                            String username = VideoCallDataBase.getDB(context).getName(usernameEmailId);
                            TaskReportBean taskReportBean=new TaskReportBean();
                            taskReportBean.setHeaderTagName("Employee Name");
                            if(username!=null){
                                taskReportBean.setValue(username);
                            }else{
                                taskReportBean.setValue(usernameEmailId);
                            }
                            avoidDuplicateValues.put(taskReportBean.getHeaderTagName(), taskReportBean);
                            inflateFields(0, taskReportBean);
                        }

//                        if(avoidDuplicateValues!=null && avoidDuplicateValues.size()>0){
//                            Iterator it = avoidDuplicateValues.entrySet().iterator();
//                            while (it.hasNext()) {
//                                Map.Entry pair = (Map.Entry)it.next();
//                                String key=(String) pair.getKey();
//                                TaskReportBean taskReportBean=(TaskReportBean) pair.getValue();
//                                inflateFields(0, taskReportBean);
//                            }
//                        }

                    }
//                    progress.cancel();



//                    Type collectionType = new TypeToken<List<TaskReportBean>>() {
//                    }.getType();
//                    List<TaskReportBean> lcs = new Gson().fromJson(values, collectionType);
//                    for(int i=0;i<lcs.size();i++){
//                        TaskReportBean taskReport = lcs.get(i);
//                        inflateFields(0,taskReport);
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

   /* public void createandDisplayPdf(HashMap<String,TaskReportBean> reportDetails) {

        Document doc = new Document();

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PDFFILE";

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, "newFile.pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter.getInstance(doc, fOut);

            //open the document
            doc.open();
            doc.addTitle("Report Details");



            if(reportDetails!=null && reportDetails.size()>0){
                            Iterator it = reportDetails.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry pair = (Map.Entry)it.next();
                                String key=(String) pair.getKey();
                                TaskReportBean taskReportBean=(TaskReportBean) pair.getValue();
                                if(taskReportBean!=null && taskReportBean.getHeaderTagName()!=null && taskReportBean.getValue()!=null) {
                                    Paragraph p1 = new Paragraph(taskReportBean.getHeaderTagName()+"   "+taskReportBean.getValue());
                                    Font paraFont = new Font(Font.COURIER);
                                    p1.setAlignment(Paragraph.TABLE);
                                    p1.setFont(paraFont);

                                    //add paragraph to document
                                    doc.add(p1);
                                }
                            }
                        }



        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
            de.printStackTrace();
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
            e.printStackTrace();
        }
        finally {
            doc.close();
        }

//        viewPdf("newFile.pdf", "Dir");
    }*/


    private void createPDF (HashMap<String,String> reportDetails){

        Document doc = new Document();
        PdfWriter docWriter = null;

        DecimalFormat df = new DecimalFormat("0.00");

        try {

            //special font sizes
            Font font=new Font();
            Font bfBold12 = new Font(font.TIMES_ROMAN, 12);
            Font bf12 = new Font(font.TIMES_ROMAN, 12);
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "//High Message";

            File dir = new File(path);
            if(!dir.exists())
                dir.mkdirs();

            File file = new File(dir, "Report.pdf");
            //file path
//            String path = "docs/" + pdfFilename;
            docWriter = PdfWriter.getInstance(doc , new FileOutputStream(file));

            //document header attributes
            doc.addAuthor("betterThanZero");
            doc.addCreationDate();
            doc.addProducer();
            doc.addCreator("MySampleCode.com");
            doc.addTitle("Report with Column Headings");
            doc.setPageSize(PageSize.LETTER);

            //open document
            doc.open();
            //create a paragraph
            InputStream inputStream = getAssets().open("tna_report.png");
            Bitmap bmp = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image companyLogo = Image.getInstance(stream.toByteArray());
//            companyLogo.setAbsolutePosition(0,0);
            companyLogo.scalePercent(43);
//            companyLogo.setAlignment(Image.MIDDLE);
            doc.add(companyLogo);

            //create a paragraph
//            Paragraph paragraph = new Paragraph();
            Paragraph paragraph = new Paragraph("TNA Report");
            Font paraFont2= new Font(Font.BOLD,30);
            paragraph.setAlignment(Paragraph.ALIGN_CENTER);
            paragraph.setFont(paraFont2);

            //specify column widths
            float[] columnWidths = {1.5f, 2f};
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            // set table width a percentage of the page width
            table.setWidthPercentage(90f);

            if(reportDetails!=null && reportDetails.size()>0){

                //Show UI in Client Side And DB
                insertCell(table, "Job No", Element.ALIGN_LEFT, 1, bf12);
                if(reportDetails.containsKey("Job No")){
                    insertCell(table, reportDetails.get("Job No"), Element.ALIGN_LEFT, 1, bf12);
                }else{
                    insertCell(table, "", Element.ALIGN_LEFT, 1, bf12);
                }
                insertCell(table, "Customer Name", Element.ALIGN_LEFT, 1, bf12);
                if(reportDetails.containsKey("Customer Name")){
                    insertCell(table, reportDetails.get("Customer Name"), Element.ALIGN_LEFT, 1, bf12);
                }else{
                    insertCell(table, "", Element.ALIGN_LEFT, 1, bf12);
                }
                 insertCell(table, "Address", Element.ALIGN_LEFT, 1, bf12);
                if(reportDetails.containsKey("Address")){
                    insertCell(table, reportDetails.get("Address"), Element.ALIGN_LEFT, 1, bf12);
                }else{
                    insertCell(table, "", Element.ALIGN_LEFT, 1, bf12);
                }
                insertCell(table, "M/c Model", Element.ALIGN_LEFT, 1, bf12);
                if(reportDetails.containsKey("M/c Model")){
                    insertCell(table, reportDetails.get("M/c Model"), Element.ALIGN_LEFT, 1, bf12);
                }else{
                    insertCell(table, "", Element.ALIGN_LEFT, 1, bf12);
                }
                insertCell(table, "M/c Sr.No", Element.ALIGN_LEFT, 1, bf12);
                if(reportDetails.containsKey("M/c Sr.No")){
                    insertCell(table, reportDetails.get("M/c Sr.No"), Element.ALIGN_LEFT, 1, bf12);
                }else{
                    insertCell(table, "", Element.ALIGN_LEFT, 1, bf12);
                }
                insertCell(table, "Task Id", Element.ALIGN_LEFT, 1, bf12);
                if(reportDetails.containsKey("Task Id")){
                    insertCell(table, reportDetails.get("Task Id"), Element.ALIGN_LEFT, 1, bf12);
                }else{
                    insertCell(table, "", Element.ALIGN_LEFT, 1, bf12);
                }
                insertCell(table, "Task Description", Element.ALIGN_LEFT, 1, bf12);
                if(reportDetails.containsKey("Task Description")){
                    insertCell(table, reportDetails.get("Task Description"), Element.ALIGN_LEFT, 1, bf12);
                }else{
                    insertCell(table, "", Element.ALIGN_LEFT, 1, bf12);
                }
                insertCell(table, "Start Time", Element.ALIGN_LEFT, 1, bf12);
                if(reportDetails.containsKey("Start Time")){
                    insertCell(table, reportDetails.get("Start Time"), Element.ALIGN_LEFT, 1, bf12);
                }else{
                    insertCell(table, "", Element.ALIGN_LEFT, 1, bf12);
                }
                insertCell(table, "End Time", Element.ALIGN_LEFT, 1, bf12);
                if(reportDetails.containsKey("End Time")){
                    insertCell(table, reportDetails.get("End Time"), Element.ALIGN_LEFT, 1, bf12);
                }else{
                    insertCell(table, "", Element.ALIGN_LEFT, 1, bf12);
                }
                insertCell(table, "Estimated Travel Hrs", Element.ALIGN_LEFT, 1, bf12);
                if(reportDetails.containsKey("Estimated Travel Hrs")){
                    insertCell(table, reportDetails.get("Estimated Travel Hrs"), Element.ALIGN_LEFT, 1, bf12);
                }else{
                    insertCell(table, "", Element.ALIGN_LEFT, 1, bf12);
                }
                insertCell(table, "Estimated Activity Hrs", Element.ALIGN_LEFT, 1, bf12);
                if(reportDetails.containsKey("Estimated Activity Hrs")){
                    insertCell(table, reportDetails.get("Estimated Activity Hrs"), Element.ALIGN_LEFT, 1, bf12);
                }else{
                    insertCell(table, "", Element.ALIGN_LEFT, 1, bf12);
                }
                insertCell(table, "ActualTotalHrs", Element.ALIGN_LEFT, 1, bf12);
                if(reportDetails.containsKey("ActualTotalHrs")){
                    insertCell(table, reportDetails.get("ActualTotalHrs"), Element.ALIGN_LEFT, 1, bf12);
                }else{
                    insertCell(table, "", Element.ALIGN_LEFT, 1, bf12);
                }
                insertCell(table, "ActivityTotalHrs", Element.ALIGN_LEFT, 1, bf12);
                if(reportDetails.containsKey("ActivityTotalHrs")){
                    insertCell(table, reportDetails.get("ActivityTotalHrs"), Element.ALIGN_LEFT, 1, bf12);
                }else{
                    insertCell(table, "", Element.ALIGN_LEFT, 1, bf12);
                }
                insertCell(table, "Employee Name", Element.ALIGN_LEFT, 1, bf12);
                if(reportDetails.containsKey("Employee Name")){
                    insertCell(table, reportDetails.get("Employee Name"), Element.ALIGN_LEFT, 1, bf12);
                }else{
                    insertCell(table, "", Element.ALIGN_LEFT, 1, bf12);
                }

            }
            //add the PDF table to the paragraph
            paragraph.add(table);
            // add the paragraph to the document
            doc.add(paragraph);

        }
        catch (DocumentException dex)
        {
            dex.printStackTrace();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            if (doc != null){
                //close the document
                doc.close();
            }
            if (docWriter != null){
                //close the writer
                docWriter.close();
            }
        }
    }

    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font){

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        cell.setFixedHeight(50f);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

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
                Toast.makeText(TaskReport.this, msg, Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    public void ErrorMethod(Object object) {

    }
}
