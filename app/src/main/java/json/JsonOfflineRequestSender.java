package json;

import android.util.Log;

import com.ase.Appreference;
import com.ase.Bean.ListofFileds;
import com.ase.Bean.TaskDetailsBean;
import com.ase.Bean.checkListDetails;
import com.ase.SettingsFragment;
import com.ase.chat.ChatBean;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pjsip.pjsua2.app.GroupMemberAccess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Preethi on 8/2/2017.
 */

public class JsonOfflineRequestSender {

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


    public void taskStatus(EnumJsonWebservicename webservicename, JSONObject jsonObject,TaskDetailsBean taskDetailsBean, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }

        if (taskDetailsBean != null) {
            communicationBean.setTaskDetailsBean(taskDetailsBean);
        }
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }
    public void taskConversactionCaption(EnumJsonWebservicename webservicename, List<NameValuePair> valuePair, WebServiceInterface access,TaskDetailsBean taskDetailsBean) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        communicationBean.setNameValuePairs(valuePair);
        communicationBean.setTaskDetailsBean(taskDetailsBean);
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

    public void SaveCheckListForm(EnumJsonWebservicename webservicename, JSONObject jsonObject, checkListDetails checklistBean, WebServiceInterface access) {

        CommunicationBean communicationBean = new CommunicationBean();
        communicationBean.setEnumJsonWebservicename(webservicename);
        if (jsonObject != null) {
            communicationBean.setJsonObject(jsonObject);
        }

        if (checklistBean != null) {
            communicationBean.setChecklistBean(checklistBean);
        }
        communicationBean.setAccess(access);
        queue.addMsg(communicationBean);
    }

}
