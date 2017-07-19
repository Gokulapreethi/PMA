package json;

import android.util.Log;

import com.ase.Appreference;
import com.ase.Bean.ListofFileds;
import com.ase.Bean.TaskDetailsBean;
import com.ase.chat.ChatBean;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pjsip.pjsua2.app.GroupMemberAccess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vignesh on 6/1/2016.
 */
public class JsonRequestSender {

    public Queue queue = new Queue();
    JsonRequestResponce jsonRequestResponce = null;


//    public void JsonRequestParser(){
//        queue=new Queue();
//    }

    public void start() {
        jsonRequestResponce = new JsonRequestResponce(queue);
        Appreference.jsonRequestResponce = jsonRequestResponce;
        jsonRequestResponce.start();
    }

    public void login(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void listGroupTaskUsers(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void ListUser(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void listAllMyTask(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void getCustomTag(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void getlistCustomHeaderTags(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }


    public void getFormFieldsForForm(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }


    public void getFormFieldAndValues(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }


    public void getCustomTagValue(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }


    public void getlistFormsforTask(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void Changepassword(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void GroupDetails(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void BlogCommentEntry(EnumJsonWebservicename servicename, JSONObject jsonObject) {
        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(servicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        queue.addMsg(communicationBean);
    }

    public void callMscReminders(EnumJsonWebservicename servicename, JSONObject jsonObject) {
        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(servicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        queue.addMsg(communicationBean);
    }


    public void BlogFileCommentEntry(EnumJsonWebservicename servicename, JSONObject jsonObject) {
        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(servicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        queue.addMsg(communicationBean);
    }

    public void BlogEntry(EnumJsonWebservicename servicename, JSONObject jsonObject) {
        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(servicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        queue.addMsg(communicationBean);
    }

    public void updateUser(EnumJsonWebservicename webservicename, Object valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setMessage(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void multiFileUpload(EnumJsonWebservicename webservicename, JSONArray jsonArray, TaskDetailsBean taskDetailsBean, WebServiceInterface serviceInterface) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setAccess(serviceInterface);
        if (jsonArray != null) {
            communicationBean.setJsonArray(jsonArray);
        }
        if (taskDetailsBean != null) {
            communicationBean.setTaskDetailsBean(taskDetailsBean);
        }
        queue.addMsg(communicationBean);
    }

    public void taskEntry(EnumJsonWebservicename servicename, JSONObject jsonObject, TaskDetailsBean taskDetailsBean, WebServiceInterface serviceInterface) {
        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(servicename);
        communicationBean.setAccess(serviceInterface);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        if (taskDetailsBean != null) {
            communicationBean.setTaskDetailsBean(taskDetailsBean);
        }
        queue.addMsg(communicationBean);
    }

    public void logoutMobile(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void changePercentageCompleted(EnumJsonWebservicename webservicename, JSONObject jsonObject, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void leaveRequestOrReject(EnumJsonWebservicename servicename, JSONObject jsonObject, TaskDetailsBean taskDetailsBean, WebServiceInterface serviceInterface) {
        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(servicename);
        communicationBean.setAccess(serviceInterface);
        if (jsonObject != null) {
            Log.i("leave", "inside json");
            communicationBean.setJsonObject(jsonObject);
        }
        if (taskDetailsBean != null) {
            Log.i("leave", "inside json");
            communicationBean.setTaskDetailsBean(taskDetailsBean);
        }
        queue.addMsg(communicationBean);
    }

    public void fileUpload(EnumJsonWebservicename servicename, JSONObject jsonObject, WebServiceInterface access, TaskDetailsBean taskDetailsBean) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(servicename);
        communicationBean.setAccess(access);
        if (taskDetailsBean != null) {
            communicationBean.setTaskDetailsBean(taskDetailsBean);
        }
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        queue.addMsg(communicationBean);
    }

    public void fileUploadfromChat(EnumJsonWebservicename servicename, JSONObject jsonObject, WebServiceInterface access, ChatBean taskDetailsBean) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(servicename);
        communicationBean.setAccess(access);
        if (taskDetailsBean != null) {
            communicationBean.setMessage_Details_Object(taskDetailsBean);
        }
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        queue.addMsg(communicationBean);
    }

    public void taskConversationEntry(EnumJsonWebservicename webservicename, JSONObject jsonObject, WebServiceInterface access, ArrayList<TaskDetailsBean> detailsBeanArrayList, TaskDetailsBean taskDetailsBean) {
        Log.i("TaskEntry", "taskConversationEntry Main");
        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        if (detailsBeanArrayList != null) {
            communicationBean.setGetTaskListforPercentage(detailsBeanArrayList);
        }
        if (taskDetailsBean != null) {
            communicationBean.setTaskDetailsBean(taskDetailsBean);
        }
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void taskObserverEntry(EnumJsonWebservicename webservicename, JSONObject jsonObject, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void taskStatus(EnumJsonWebservicename webservicename, JSONObject jsonObject, ArrayList<TaskDetailsBean> detailsBeanArrayList, TaskDetailsBean taskDetailsBean,WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        if (detailsBeanArrayList != null) {
            communicationBean.setGetStatusListForMedia(detailsBeanArrayList);
        }
        if(taskDetailsBean != null){
            communicationBean.setTaskDetailsBean(taskDetailsBean);
        }
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }


    public void userDeviceRegistration(EnumJsonWebservicename webservicename, JSONObject jsonObject) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        queue.addMsg(communicationBean);
    }

    public void getTask(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void listAssignTemplate(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void listUserGroupMemberAccess(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access, GroupMemberAccess groupMemberAccess) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        if (groupMemberAccess != null) {
            communicationBean.setGroupMemberAccess(groupMemberAccess);
        }
        queue.addMsg(communicationBean);
    }

    //    public void callNotification(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {
//        CommunicationBean communicationBean = new CommunicationBean();
//        communicationBean.setEnumJsonWebservicename(webservicename);
//        communicationBean.setNameValuePairs(valuePair);
//        communicationBean.setAccess(access);
//        queue.addMsg(communicationBean);
//    }
    public void callNotification(EnumJsonWebservicename webservicename, JSONObject jsonObject, WebServiceInterface access) {
        Log.i("json", "inside JSonRequestSender callNotification");
        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
//        queue.addMsg(communicationBean);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void templateTask(EnumJsonWebservicename servicename, JSONObject jsonObject, WebServiceInterface access, TaskDetailsBean taskDetailsBean) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(servicename);
        communicationBean.setAccess(access);
        if (taskDetailsBean != null) {
            communicationBean.setMessage_Details_Object(taskDetailsBean);
        }
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        queue.addMsg(communicationBean);
    }

    public void remainderForTemplate(EnumJsonWebservicename webservicename, JSONObject jsonObject, WebServiceInterface access, ArrayList<TaskDetailsBean> detailsBeanArrayList, TaskDetailsBean taskDetailsBean) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        if (detailsBeanArrayList != null) {
            communicationBean.setGetTaskListforPercentage(detailsBeanArrayList);
        }
        if (taskDetailsBean != null) {
            communicationBean.setTaskDetailsBean(taskDetailsBean);
        }
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void getGroup(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void getDeleteCustomTag(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void checkConflicts(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void customTagValueEntry(EnumJsonWebservicename webservicename, JSONObject jsonObject, WebServiceInterface access, ArrayList<ListofFileds> beanArrayList) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        if (beanArrayList != null) {
            communicationBean.setFiledsArrayList(beanArrayList);
        }
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void saveCustomHeaderTagValue(EnumJsonWebservicename webservicename, JSONObject jsonObject, WebServiceInterface access, ArrayList<ListofFileds> beanArrayList) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        if (beanArrayList != null) {
            Log.i("json", "beanArrayList listsize  == " + beanArrayList.size());
            communicationBean.setFiledsArrayList(beanArrayList);
        }
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void customHeaderTagValueEntry(EnumJsonWebservicename webservicename, JSONObject jsonObject, WebServiceInterface access, ArrayList<ListofFileds> beanArrayList) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        if (beanArrayList != null) {
            communicationBean.setFiledsArrayList(beanArrayList);
        }
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void setFormFieldValuesForForm(EnumJsonWebservicename webservicename, JSONObject jsonObject, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }

        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void listAllProject(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void listAllMyProject(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void getAllJobDetails(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void getProject(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void getTaskForJobID(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }
    public void projectCompleted(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }
    public void getMediaSearch(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }
    public void SaveFormAccessRestrictions(EnumJsonWebservicename servicename, JSONObject jsonObject, WebServiceInterface access) {
        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(servicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void getCustomHeaderValue(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }
   public void reactivateStatus(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access,TaskDetailsBean taskDetailsBean) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setTaskDetailsBean(taskDetailsBean);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }
    public void OracleAssignTask(EnumJsonWebservicename webservicename, JSONObject jsonObject,TaskDetailsBean taskDetailsBean, WebServiceInterface access) {
        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        if(taskDetailsBean!=null)
            communicationBean.setTaskDetailsBean(taskDetailsBean);

        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }
    public void OracleTNAReport(EnumJsonWebservicename webservicename, JSONObject jsonObject, WebServiceInterface access) {
        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }
    public void OracleFSRReport(EnumJsonWebservicename webservicename, JSONObject jsonObject, WebServiceInterface access) {
        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }
    public void OracleFSRJOBReport(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {
        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }
    public void OracleTNAJobReport(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access) {
        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }
    /*public void listMembersInGroup(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access){

        CommunicationBean communicationBean=new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }*/
}
