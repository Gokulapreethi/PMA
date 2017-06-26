package json;

import android.util.Log;

import com.myapplication3.AddObserver;
import com.myapplication3.Appreference;
import com.myapplication3.AudioRecorder;
import com.myapplication3.ChangePassword;
import com.myapplication3.ContactsFragment;
import com.myapplication3.Forms.FormEntryViewActivity;
import com.myapplication3.Forms.FormsEntryActivity;
import com.myapplication3.GroupPercentageStatus;
import com.myapplication3.Leave_Request_dateSent;
import com.myapplication3.LoginActivity;
import com.myapplication3.MediaSearch;
import com.myapplication3.NewBlogActivity;
import com.myapplication3.NewTaskConversation;
import com.myapplication3.SettingsFragment;
import com.myapplication3.TaskDateUpdate;
import com.myapplication3.TemplateView;
import com.myapplication3.UpdateProfileActivity;
import com.myapplication3.blog.BlogPostCommandActivity;
import com.myapplication3.sketh.ProjectsFragment;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.pjsip.pjsua2.app.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by vignesh on 6/1/2016.
 */
public class JsonRequestResponce extends Thread {


    private Queue bean1;
    WebServiceInterface inter;
    boolean isRunning = false;
    String test2 = null;
    CommunicationBean obj;
    MainActivity mainActivity = (MainActivity) Appreference.context_table.get("mainactivity");

    public JsonRequestResponce(Queue queue) {
        bean1 = queue;
        isRunning = true;
        //inter=inter1;
    }

    @Override
    public void run() {
        super.run();
        while (isRunning) {
            try {

                obj = (CommunicationBean) bean1.getMsg();
//                inter = obj.getAccess();

                if (obj != null) {
                    inter = obj.getAccess();
                    Log.i("JsonLeave", "inside json request responce");
                    HttpParams httpParameters = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParameters, 120000);
                    HttpConnectionParams.setSoTimeout(httpParameters, 120000);
//                    DefaultHttpClient httpclient = new DefaultHttpClient(httpParameters);
                      /*  HttpClient httpclient = new DefaultHttpClient();
                    HttpResponse response;
                    String responseString = null;*/

                    HttpResponse response;
                    String responseString = "";
                    StringBuilder builder = new StringBuilder();
                    HttpClient httpclient = getHttpsClient(new DefaultHttpClient(httpParameters));
                    HttpParams params = new BasicHttpParams();
                    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                    HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);


                    try {

                        Log.i("JsonLeave", "Try catch 1 ");
                        //https://122.165.92.171:8443/kamailioWeb/      https://66.109.17.204/kamailioWeb/
                        //              https://www.snazevents.com/SNAZEvents/
//                        Log.d("url",Resources.getSystem().getString(R.string.app_url));

//                        HttpPost httppost = new HttpPost("https://122.165.92.171:8443/kamailioWeb/" + obj.getEnumJsonWebservicename());
//                        HttpPost httppost = new HttpPost("https://66.109.17.204/highMessage/" + obj.getEnumJsonWebservicename());//

//                          HttpPost httppost = new HttpPost("https://192.168.1.3:8443/ASE/" + obj.getEnumJsonWebservicename());
                          HttpPost httppost = new HttpPost("http://151.253.12.203/ASE/" + obj.getEnumJsonWebservicename());

//                        HttpPost httppost = new HttpPost("https://66.109.17.204/highMessage/" + obj.getEnumJsonWebservicename());//
//                        HttpPost httppost = new HttpPost("https://66.109.17.205/highMessage/" + obj.getEnumJsonWebservicename());//
//                        HttpPost httppost = new HttpPost("https://122.165.92.171:8443/highMessage/" + obj.getEnumJsonWebservicename());
//                        HttpPost httppost = new HttpPost("https://35.162.171.171/highMessage/" + obj.getEnumJsonWebservicename());

                        Log.i("json", obj.getEnumJsonWebservicename().toString());
                        Appreference.printLog("jsonwebservice", "webservice request name-->" + obj.getEnumJsonWebservicename().toString(), "DEBUG", null);

                        String servname = obj.getEnumJsonWebservicename().toString();
                        if (servname.equals("updateUser")) {
                            Log.i("JsonLeave", "Try catch 2 ");
                            StringEntity se = new StringEntity(obj.getMessage().toString(), HTTP.UTF_8);
                            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                            httppost.setEntity(se);
                        } else if (obj.getNameValuePairs() != null) {
                            httppost.setEntity(new UrlEncodedFormEntity(obj.getNameValuePairs()));
                            Log.i("JsonLeave", "Try catch 3 ");
                            Log.i("jsonwebservice", "list size for nameValuePair" + obj.getNameValuePairs().size());
                            for (NameValuePair nameValuePair : obj.getNameValuePairs()) {

                                Log.i("jsonwebservice", " nameValuePair   " + nameValuePair.getName() + " : " + nameValuePair.getValue());
                                Appreference.printLog("jsonwebservice", "webservice request values-->" + nameValuePair.getName() + " : " + nameValuePair.getValue(), "DEBUG", null);
//                            Log.i("jsonwebservice", " nameValuePair Values"+nameValuePair.getValue());
                            }
                        }

                        if (obj.getJsonObject() != null || obj.getJsonArray() != null) {
                            Log.i("JsonLeave", "Try catch 4 ");
//                            Log.i("jsonwebservice", "inside jsonobject if--->");
                            String json = "";
                            InputStream inputStream = null;
                            if (obj.getJsonObject() != null) {
                                json = obj.getJsonObject().toString();
                                Log.i("jsonwebservice", "******Request*****"+obj.getEnumJsonWebservicename().toString() +"------>" + json);
                                Log.i("JsonLeave", "Try catch 5 ");
                            }
                            if (obj.getJsonArray() != null) {
                                json = obj.getJsonArray().toString();
                                Log.i("JsonLeave", "Try catch 6 ");
                            }

                            // 5. set json to StringEntity
                            StringEntity se = new StringEntity(json, HTTP.UTF_8);

                            // 6. set httpPost Entity
                            httppost.setEntity(se);

                            // 7. Set some headers to inform server about the type of the content
                            httppost.setHeader("Accept", "application/json");
                            httppost.setHeader("Content-type", "application/json");

                            // 8. Execute POST request to the given URL
                            HttpResponse httpResponse = httpclient.execute(httppost);

                            // 9. receive response as inputStream
                            inputStream = httpResponse.getEntity().getContent();

                            // 10. convert inputstream to string
                            if (inputStream != null) {
                                responseString = convertInputStreamToString(inputStream);
                                Log.i("JsonLeave", "Try catch 7 ");
                            } else {
                                responseString = "Did not work!";
                                Log.i("JsonLeave", "Try catch 8 ");
                                Log.i("JsonLeave", "Try catch 9 ");
                            }
                            Log.i("jsonwebservice", "******Response*****"+obj.getEnumJsonWebservicename().toString() +"------>" + responseString);

                            Log.d("jsonwebservice", obj.getEnumJsonWebservicename().toString());
                            if (obj.getEnumJsonWebservicename().toString().equalsIgnoreCase("taskEntry")
                                    || obj.getEnumJsonWebservicename().toString().equalsIgnoreCase("customTagValueEntry")
                                    || obj.getEnumJsonWebservicename().toString().equalsIgnoreCase("saveCustomHeaderTagValue")
                                    || obj.getEnumJsonWebservicename().toString().equalsIgnoreCase("deleteCustomMasterValue")
                                    || obj.getEnumJsonWebservicename().toString().equalsIgnoreCase("editCustomMasterValue")) {

                                Log.d("jsonwebservice", "Inside if    " +
                                        "" + obj.getEnumJsonWebservicename().toString());
                                obj.setEmail(responseString);
                                Log.i("output123","Send custom1 ->server Response is===> "+responseString);

                                obj.setFirstname(obj.getEnumJsonWebservicename().toString());
                                Log.i("getTask 1", "enum for check ");
                                inter.ResponceMethod(obj);
                                Log.i("JsonLeave", "Try catch 9 ");
                            } /*else if (obj.getEnumJsonWebservicename().toString().equalsIgnoreCase("multiFileUpload")) {
                                Log.d("jsonwebservice", "Inside if " +
                                        "" + obj.getEnumJsonWebservicename().toString() + " response received");
                                obj.setEmail(responseString);
                                obj.setFirstname("multiFileUpload");
                                inter.ResponceMethod(obj);
                            }*/
                        } else {
                            Log.i("JsonLeave", "Try catch 10 ");
                            Log.i("jsonwebservice", "inside jsonobject else--->");
                            // Execute HTTP Post Request
                            response = httpclient.execute(httppost);

                            StatusLine statusLine = response.getStatusLine();
                            // Use this status line for getting the response and parse the json
                            // respectively..

                            Log.i("jsonwebservice", "inside jsonobject else---> getStatusCode   == " + response.getStatusLine().getStatusCode());

//
                            if (response.getStatusLine().getStatusCode() == 200) {
                                HttpEntity httpEntity = response.getEntity();
                                InputStream is = httpEntity.getContent();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                                StringBuilder sb = new StringBuilder();
                                String line = null;
                                while ((line = reader.readLine()) != null) {
                                    sb.append(line + "\n");
                                }
                                is.close();
                                responseString = sb.toString();
                                Log.i("JsonLeave", "Try catch 11 ");
                                Log.i("jsonwebservice", "Result  OK   " + responseString);
                                Appreference.printLog("jsonwebservice", "webservice Response-->" + responseString, "DEBUG", null);

                            } else {
//                            errorResponse(obj.getEnumJsonWebservicename(), "Unsuccessfull Response");
                                System.out.println("Result Not OK " + statusLine.getReasonPhrase());
                                Appreference.printLog("jsonwebservice", "Result Not OK", "DEBUG", null);
                                Log.i("jsonwebservice", "Result Not OK");
                                errorResponse(obj.getEnumJsonWebservicename(), "Result Not OK ");
                                Log.i("JsonLeave", "Try catch 12 ");
                            }

                            if (obj.getEnumJsonWebservicename().toString().equals("changePercentageCompleted")) {
                                obj.setFirstname("percent");
                            } else if (obj.getEnumJsonWebservicename().toString().equals("getGroup")) {
                                obj.setFirstname("getgroup");
                            } else if ((obj.getEnumJsonWebservicename().toString().equals("listUser"))
                                    || (obj.getEnumJsonWebservicename().toString().equals("listGroupMember"))) {
                                obj.setFirstname("contactgroup");
                            } else if (obj.getEnumJsonWebservicename().toString().equals("listUserGroupMemberAccess")) {
                                obj.setFirstname("listUserGroupMemberAccess");
                            } else if (obj.getEnumJsonWebservicename().toString().equals("listAllMyTask")) {
                                obj.setFirstname("listAllMyTask");
                            } else if (obj.getEnumJsonWebservicename().toString().equals("callNotification")) {
                                obj.setFirstname("callNotification");
                            } else if (obj.getEnumJsonWebservicename().toString().equals("listAllMyProject")) {
                                obj.setFirstname("listAllMyProject");
                            } else if (obj.getEnumJsonWebservicename().toString().equals("getAllJobDetails")) {
                                obj.setFirstname("getAllJobDetails");
                            } else if (obj.getEnumJsonWebservicename().toString().equals("getProject")) {
                                obj.setFirstname("getProject");
                            }  else if (obj.getEnumJsonWebservicename().toString().equals("getTaskForJobID")) {
                                obj.setFirstname("getTaskForJobID");
                            } else if (obj.getEnumJsonWebservicename().toString().equals("getAllFormsForTask")) {
                                obj.setFirstname("getAllFormsForTask");
                            } else if (obj.getEnumJsonWebservicename().toString().equals("getCustomHeaderValue")) {
                                obj.setFirstname("getCustomHeaderValue");
                            } else if (obj.getEnumJsonWebservicename().toString().equals("getFormAccessForTaskId")) {
                                obj.setFirstname("getFormAccessForTaskId");
                            } else if (obj.getEnumJsonWebservicename().toString().equals("taskEntry")) {
                                obj.setFirstname("taskEntry");
                            } else if (obj.getEnumJsonWebservicename().toString().equals("checkConflicts")) {
                                obj.setFirstname("checkConflicts");
                            } else if (obj.getEnumJsonWebservicename().toString().equals("taskStatus")) {
                                obj.setFirstname("taskStatus");
                            }else if (obj.getEnumJsonWebservicename().toString().equals("assignTask")) {
                                obj.setFirstname("assignTask");
                            }else if (obj.getEnumJsonWebservicename().toString().equals("searchMedia")) {
                                obj.setFirstname("searchMedia");
                            }else if (obj.getEnumJsonWebservicename().toString().equals("taskNeedAssessmentReport")) {
                                obj.setFirstname("taskNeedAssessmentReport");
                            }else if (obj.getEnumJsonWebservicename().toString().equals("fieldServiceReport")) {
                                obj.setFirstname("fieldServiceReport");
                            } else if (obj.getEnumJsonWebservicename().toString().equals("getTask")) {
                                obj.setFirstname("getTask");
                            } else if (obj.getEnumJsonWebservicename().toString().equals("getRequestType")) {
                                obj.setFirstname("getRequestType");
                            } else if (obj.getEnumJsonWebservicename().toString().equals("listGroupTaskUsers")) {
                                obj.setFirstname("listGroupTaskUsers");
                            }else if (obj.getEnumJsonWebservicename().toString().equals("projectCompleted")) {
                                obj.setFirstname("projectCompleted");
                            }

                            obj.setEmail(responseString);
                            Log.i("getTask 2", "enum for check  ");
                            inter.ResponceMethod(obj);
                            Log.i("JsonLeave", "Try catch 13 ");
                        }


                    } catch (UnknownHostException e) {
                        e.printStackTrace();
//                        errorResponse(serviceName,e.getMessage());
                        errorResponse(obj.getEnumJsonWebservicename(), e.getMessage());
                        Log.e("Error", e.toString());
                    } catch (ConnectTimeoutException e) {
                        e.printStackTrace();
//                        errorResponse(serviceName,e.getMessage());
                        errorResponse(obj.getEnumJsonWebservicename(), e.getMessage());
                        Log.e("Error", e.toString());
                    } catch (ClientProtocolException e) {
                        // TODO Handle problems..
                        e.printStackTrace();
                        errorResponse(obj.getEnumJsonWebservicename(), e.getMessage());
                        //errorResponse(serviceName, e.getMessage());
                    } catch (IOException e) {
                        // TODO Handle problems..
                        e.printStackTrace();
                        errorResponse(obj.getEnumJsonWebservicename(), e.getMessage());
                        //errorResponse(serviceName, e.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                        errorResponse(obj.getEnumJsonWebservicename(), e.getMessage());
                        //errorResponse(serviceName,e.getMessage());
                    }


//
                    if (!obj.getEnumJsonWebservicename().toString().equals("listAllProject")
                            && !obj.getEnumJsonWebservicename().toString().equals("getProject")
                            && !obj.getEnumJsonWebservicename().toString().equals("getTaskForJobID")
                            && !obj.getEnumJsonWebservicename().toString().equals("getCustomHeaderValue")
                            && !obj.getEnumJsonWebservicename().toString().equals("getTask")
                            && !obj.getEnumJsonWebservicename().toString().equals("projectCompleted")
                            && !obj.getEnumJsonWebservicename().toString().equals("searchMedia")) {
                        Log.i("jsonwebservice", "enum       " + obj.getEnumJsonWebservicename().toString());
                        Log.i("getTask 3", "enum for check ");
                        Log.i("jsonwebservice", "enum    responseString   " + responseString);
                        webserviceResponse(obj.getEnumJsonWebservicename(), responseString);
                        Log.i("JsonLeave", "Try catch 14 ");
                    }
                }
            } catch (InterruptedException e) {
                Log.i("jsonwebservice", "InterruptedException");
                e.printStackTrace();
            }
        }
    }

    public void errorResponse(final EnumJsonWebservicename serviceName, String result) {
        Log.i("jsonwebservice", "Json error Response " + result);
        switch (serviceName) {
            case loginMobile:
                LoginActivity.getInstance().cancelDialog();
                LoginActivity.getInstance().showToast(result);
                inter.ErrorMethod(obj);
                break;
            case taskEntry:
                NewTaskConversation.getInstance().cancelDialog();
                NewTaskConversation.getInstance().showToast("Task creation failed. Try again later");
                inter.ErrorMethod(obj);
                break;
            case taskConversationEntry:
                if (Appreference.project_taskConversationEntry) {
                    MainActivity.getIntance().cancelDialog();
//                    MainActivity.getIntance().showToast("Task creation failed. Try again later");
                } else {
                    NewTaskConversation.getInstance().cancelDialog();
                    NewTaskConversation.getInstance().showToast(" Try again later");
                }

                inter.ErrorMethod(obj);
                break;
            case changePasswordEvent:
                ChangePassword.getInstance().canceProgress();
                ChangePassword.getInstance().showToast(result);
                inter.ErrorMethod(obj);
                break;
            case updateUser:
                UpdateProfileActivity.getInstance().cancelDialog();
                UpdateProfileActivity.getInstance().showToast(result);
                inter.ErrorMethod(obj);
                break;
            case multiFileUpload:
                NewTaskConversation.getInstance().cancelDialog();
                NewTaskConversation.getInstance().showToast(result);
                inter.ErrorMethod(obj);
                break;
            case blogEntry:
                NewBlogActivity.getInstance().cancelDialog();
                NewBlogActivity.getInstance().showToast(result);
                inter.ErrorMethod(obj);
                break;
            case logoutMobile:
                SettingsFragment.getInstance().cancelDialog();
                SettingsFragment.getInstance().showToast(result);
                inter.ErrorMethod(obj);
                break;
            case callNotification:
                ContactsFragment.getInstance().cancelDialog();
                ContactsFragment.getInstance().showToast(result);
                inter.ErrorMethod(obj);
                break;
            case assignTemplate:
                AddObserver.getInstance().cancelDialog();
                AddObserver.getInstance().showToast(result);
                inter.ErrorMethod(obj);
                break;
            case remainderForTemplate:
                NewTaskConversation.getInstance().cancelDialog();
                NewTaskConversation.getInstance().showToast("Template date request creation failed. Try again later");
                inter.ErrorMethod(obj);
                break;
            case getTask:
//                NewTaskConversation.getInstance().cancelDialog();
//                TaskHistory.getInstance().showToast(result);
//                TaskHistory.getInstance().cancelDialog();
                inter.ErrorMethod(obj);
                break;
            case listAssignTemplate:
                inter.ErrorMethod(obj);
                break;
            case checkConflicts:
                NewTaskConversation.getInstance().cancelDialog();
                NewTaskConversation.getInstance().showToast("Conflict checking error . Try again later");
                inter.ErrorMethod(obj);
                break;
             case taskStatus:
                NewTaskConversation.getInstance().cancelDialog();
                NewTaskConversation.getInstance().showToast("taskStatus error . Try again later");
                inter.ErrorMethod(obj);
                break;
            case assignTask:
                /*AddTaskReassign.getInstance().cancelDialog();
                AddTaskReassign.getInstance().showToast("assignTask error . Try again later");
                inter.ErrorMethod(obj);*/
                break;
            case searchMedia:
                MediaSearch.getInstance().cancelDialog();
                MediaSearch.getInstance().showToast("searchMedia error . Try again later");
                inter.ErrorMethod(obj);
                break;
           case taskNeedAssessmentReport:
               NewTaskConversation.getInstance().cancelDialog();
               NewTaskConversation.getInstance().showToast("taskNeedAssessmentReport error . Try again later");
                inter.ErrorMethod(obj);
                break;
          case fieldServiceReport:
               NewTaskConversation.getInstance().cancelDialog();
               NewTaskConversation.getInstance().showToast("fieldServiceReport error . Try again later");
                inter.ErrorMethod(obj);
                break;
            case leaveRequestOrReject:
                Leave_Request_dateSent.getInstance().cancelDialog();
                Leave_Request_dateSent.getInstance().showToast("Leave Request Error . Try again later.");
                inter.ErrorMethod(obj);
                break;
            case getAllFormsForTask:
                inter.ErrorMethod(obj);
                break;

            case getFormFieldAndValues:
                FormEntryViewActivity.getInstance().cancelDialog();
                FormEntryViewActivity.getInstance().showToast("Forms values  error . Try again later " + result);
                inter.ErrorMethod(obj);
                break;

            case deleteFieldValue:
                FormEntryViewActivity.getInstance().cancelDialog();
                FormEntryViewActivity.getInstance().showToast("Forms values  error . Try again later " + result);
                inter.ErrorMethod(obj);
                break;

            case setFormFieldValuesForForm:
                FormsEntryActivity.getInstance().cancelDialog();
                FormsEntryActivity.getInstance().showToast("Forms values  error . Try again later " + result);
                inter.ErrorMethod(obj);
                break;

            case listAllMyProject:
                ProjectsFragment.getInstance().cancelDialog();
                ProjectsFragment.getInstance().showToast(result);
                inter.ErrorMethod(obj);
                break;
            case getAllJobDetails:
                ProjectsFragment.getInstance().cancelDialog();
                ProjectsFragment.getInstance().showToast(result);
                inter.ErrorMethod(obj);
                break;

            case getProject:
                ProjectsFragment.getInstance().cancelDialog();
                ProjectsFragment.getInstance().showToast(result);
                inter.ErrorMethod(obj);
                break;
           case getTaskForJobID:
                ProjectsFragment.getInstance().cancelDialog();
                ProjectsFragment.getInstance().showToast(result);
                inter.ErrorMethod(obj);
                break;
            case listGroupTaskUsers:
                GroupPercentageStatus.getInstance().cancelDialog();
                inter.ErrorMethod(obj);
                break;
            case projectCompleted:
                ProjectsFragment.getInstance().cancelDialog();
                ProjectsFragment.getInstance().showToast(result);
                inter.ErrorMethod(obj);
                break;
        }
    }

    private void webserviceResponse(EnumJsonWebservicename enumJsonWebservicename, String responseString) {
        try {
            if (responseString != null) {
                Log.i("JsonLeave", "Try catch 15 ");
                Log.i("jsonwebservice", "enum       " + enumJsonWebservicename);
                switch (enumJsonWebservicename) {
                    case loginMobile:
                        Log.i("jsonwebservice", "CASE loginMobile Responce------>" + responseString);
                        break;
                    case blogCommentEntry:
                        Log.i("jsonwebservice", "CASE Response blogCommentEntry---------->>>" + responseString);
                        BlogPostCommandActivity.getInstance().notifypostCommentEntryRespone(responseString);
                        break;
                    case blogFileCommentEntry:
                        Log.i("jsonwebservice", "CASE postEntry webserviceResponse--->" + responseString);
                        BlogPostCommandActivity.getInstance().notifypostCommentEntryRespone(responseString);
                        break;
                    case blogEntry:
                        Log.i("jsonwebservice", "CASE BlogEntry webserviceResponse--->" + responseString);
                        NewBlogActivity.getInstance().notifypostEntryResponse(responseString);
                        break;
                    case taskEntry:
                        Log.i("jsonwebservice", " CASE TaskEntry webserviceResponse--->" + responseString);
                        if (Appreference.isAudio_webservice) {
                            Appreference.isAudio_webservice = false;
                            AudioRecorder.getInstance().notifypostEntryResponse(responseString);
                        }
    //                    else if(Appreference.taskId_webservice) {
    //                        NewTaskConversation.getInstance().notifypostEntryResponse(responseString);
    //                    }
                        else {
    //                            NewTaskActivity.getInstance().notifypostEntryResponse(responseString);
                            if (Appreference.template_page.equalsIgnoreCase("NewTaskConversation"))
                                NewTaskConversation.getInstance().notifypostEntryResponse(responseString);
                            else
                                TemplateView.getInstance().notifypostEntryResponse(responseString);
                        }
                        break;
                    case fileUpload:
                        Log.i("jsonwebservice", "CASE fileupload Response" + responseString);
                        obj.setEmail(responseString);
                        inter.ResponceMethod(obj);
                        break;

                    case userDeviceRegistration:
                        Log.i("jsonwebservice", "CASE userDeviceRegistration Response" + responseString);

                        Log.d("jsonwebservice", "fcm   = =  " + responseString.toString());

                        break;

                    case changePercentageCompleted:
                        Log.i("jsonwebservice", "CASE changePercentageCompleted  webserviceResponse--->" + responseString);
                        if (obj.getEnumJsonWebservicename().toString().equals("changePercentageCompleted")) {
                            obj.setFirstname("percent");
                        }
                        obj.setEmail(responseString);
                        inter.ResponceMethod(obj);
                        break;

                    case taskConversationEntry:
                        obj.setEmail(responseString);
                        obj.setFirstname("taskConversationEntry");
                        inter.ResponceMethod(obj);
                        Log.i("Response", "CASE Appreference.project_taskConversationEntry response " + Appreference.project_taskConversationEntry);

                        try {
                            if (Appreference.project_taskConversationEntry) {
                                if (mainActivity == null) {
                                    MainActivity mainActivity_1 = (MainActivity) Appreference.context_table.get("mainactivity");
                                    //                            mainActivity_1.getIntance().cancelDialog();
                                } else {
                                    //                            mainActivity.getIntance().cancelDialog();
                                }
                            } else {
    //                            NewTaskConversation.getInstance().cancelDialog();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case taskObserverEntry:
                        if (!Appreference.isEscalate_Observer_WS) {
                            Log.i("json","taskObserverEntry "+Appreference.isEscalate_Observer_WS);
                            AddObserver.getInstance().notifypostEntryResponse(responseString);
                        } else {
                            obj.setEmail(responseString);
                            obj.setFirstname("taskObserverEntry");
                            inter.ResponceMethod(obj);
                            Appreference.isEscalate_Observer_WS = false;
                        }
                        break;
                    case getTask:
                        Log.i("jsonwebservice", "Responce------>" + responseString);
                        obj.setEmail(responseString);
                        obj.setFirstname("getTask");
                        Log.i("jsonwebservice", "getTask " + obj.getFirstname());
                        break;
                    case callNotification:
                        obj.setEmail(responseString);
                        obj.setFirstname("callNotification");
                        inter.ResponceMethod(obj);
                        break;
                    case assignTemplate:
                        obj.setEmail(responseString);
                        obj.setFirstname("assignTemplate");
                        inter.ResponceMethod(obj);
                        break;
                    case remainderForTemplate:
                        obj.setEmail(responseString);
                        obj.setFirstname("remainderForTemplate");
                        inter.ResponceMethod(obj);
                        Log.i("jsonwebservice", "remainderForTemplate " + obj.getFirstname());
                        break;
                    case listAssignTemplate:
                        Log.i("jsonwebservice", "Responce------>" + responseString);
                        obj.setEmail(responseString);
                        obj.setFirstname("getTask");
                        Log.i("jsonwebservice", "getTask " + obj.getFirstname());
                        break;
                    case updateUser:
                        obj.setEmail(responseString);
                        obj.setFirstname("updateUser");
                        inter.ResponceMethod(obj);
                        UpdateProfileActivity.getInstance().cancelDialog();
                        UpdateProfileActivity.getInstance().showToast("success");
                        break;
                    case checkConflicts:
                        Log.i("JsonLeave", "Try catch 16 ");
    //                    obj.setEmail(responseString);
    //                    obj.setFirstname("checkConflicts");
                        Log.d("conflict", "obj " + obj.getEmail());
                        Log.d("conflict", "obj " + obj.getFirstname());
                        if (Appreference.isconflicttaker || Appreference.isLeaveConflict) {
                            NewTaskConversation.getInstance().cancelDialog();
                            Log.i("JsonLeave", "Try catch 17 ");
                        } else {
                            TaskDateUpdate.getInstance().cancelDialog();
                            Log.i("JsonLeave", "Try catch 18 ");
    //                        inter.ResponceMethod(obj);
                        }

    //                    inter.ResponceMethod(obj);
                        Log.i("JsonLeave", "Try catch 19 ");
                        break;
                    case leaveRequestOrReject:
                        obj.setEmail(responseString);
                        obj.setFirstname("LeaveRequest");
                        inter.ResponceMethod(obj);
                        Leave_Request_dateSent.getInstance().cancelDialog();
                        Log.i("jsonwebservice", "LeaveRequest " + obj.getFirstname());
                        break;
                    case multiFileUpload:
                        Appreference.isResponse_multifile = true;
                        obj.setEmail(responseString);
                        obj.setFirstname("multiFileUpload");
                        inter.ResponceMethod(obj);
                        Log.d("jsonwebservice", "response " +
                                "" + obj.getEnumJsonWebservicename().toString() + " received");
                        break;

                    case setFormFieldValuesForForm:
                        obj.setEmail(responseString);
                        inter.ResponceMethod(obj);
                        break;

                    case editFieldValues:
                        obj.setEmail(responseString);
                        inter.ResponceMethod(obj);
                        break;
                    case deleteFieldValue:
                        obj.setEmail(responseString);
                        inter.ResponceMethod(obj);
                        break;

                    case getFormFieldAndValues:
                        obj.setEmail(responseString);
                        obj.setFirstname("getFormFieldsForForm");
                        inter.ResponceMethod(obj);
                        break;

                    case getAllFormsForTask:
                        obj.setEmail(responseString);
                        obj.setFirstname("getAllFormsForTask");
                        inter.ResponceMethod(obj);
                        break;

                    case editCustomHeaderValue:
                        obj.setEmail(responseString);
                        obj.setFirstname("editCustomHeaderValue");
                        inter.ResponceMethod(obj);
                        break;

                    case listGroupTaskUsers:
//                        GroupPercentageStatus.getInstance().cancelDialog();
                        break;
                    case assignTask:
                        obj.setEmail(responseString);
                        obj.setFirstname("assignTask");
                        inter.ResponceMethod(obj);
                        break;
                   case searchMedia:
                        obj.setEmail(responseString);
                        obj.setFirstname("searchMedia");
                        inter.ResponceMethod(obj);
                        break;
                    case taskStatus:
                        obj.setEmail(responseString);
                        obj.setFirstname("taskStatus");
                        inter.ResponceMethod(obj);
                        break;
                    case taskNeedAssessmentReport:
                        obj.setEmail(responseString);
                        obj.setFirstname("taskNeedAssessmentReport");
                        inter.ResponceMethod(obj);
                        break;
                   case fieldServiceReport:
                        obj.setEmail(responseString);
                        obj.setFirstname("fieldServiceReport");
                        inter.ResponceMethod(obj);
                        break;
                  /*  case getRequestType:
                        obj.setEmail(responseString);
                        obj.setFirstname("getRequestType");
                        inter.ResponceMethod(obj);
                        break;*/
                    /*case getCustomHeaderValue:
                        obj.setEmail(responseString);
                        obj.setFirstname("getCustomHeaderValue");
                        inter.ResponceMethod(obj);
                        break;*/

                    default:
                        Log.i("jsonwebservice", "Response received for " + obj.getEnumJsonWebservicename().toString());
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public static HttpClient getHttpsClient(HttpClient client) {
        try {
            X509TrustManager x509TrustManager = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, null);
            SSLSocketFactory sslSocketFactory = new ExSSLSocketFactory(sslContext);
            sslSocketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager clientConnectionManager = client.getConnectionManager();
            SchemeRegistry schemeRegistry = clientConnectionManager.getSchemeRegistry();
            schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
            return new DefaultHttpClient(clientConnectionManager, client.getParams());
        } catch (Exception ex) {
            return null;
        }
    }
}
