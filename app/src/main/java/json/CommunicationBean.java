package json;

import com.ase.Bean.ListofFileds;
import com.ase.Bean.TaskDetailsBean;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pjsip.pjsua2.app.GroupMemberAccess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vignesh on 6/1/2016.
 */
public class CommunicationBean {

    String email;
    String password;
    String firstname;
    String lastname;
    String title;
    String gender;
    String profileimage;

    public String getOldprofileimage() {
        return oldprofileimage;
    }

    public void setOldprofileimage(String oldprofileimage) {
        this.oldprofileimage = oldprofileimage;
    }

    String oldprofileimage;


    ArrayList<TaskDetailsBean> getTaskListforPercentage;
    ArrayList<TaskDetailsBean> getStatusListForMedia;
    ArrayList<ListofFileds> filedsArrayList;
    private Object message;

    private Object message_Details_Object;

    private Object list_Member_Acces;

    EnumJsonWebservicename enumJsonWebservicename;

    List<NameValuePair> nameValuePairs;

    String inputString;

    WebServiceInterface access;

    JSONObject jsonObject;

    JSONArray jsonArray;

    TaskDetailsBean taskDetailsBean;

    GroupMemberAccess groupMemberAccess;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object getMessage() {
        return message;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public EnumJsonWebservicename getEnumJsonWebservicename() {
        return enumJsonWebservicename;
    }

    public void setEnumJsonWebservicename(EnumJsonWebservicename enumJsonWebservicename) {
        this.enumJsonWebservicename = enumJsonWebservicename;
    }

    public List<NameValuePair> getNameValuePairs() {
        return nameValuePairs;
    }

    public void setNameValuePairs(List<NameValuePair> nameValuePairs) {
        this.nameValuePairs = nameValuePairs;
    }

    public WebServiceInterface getAccess() {
        return access;
    }

    public void setAccess(WebServiceInterface access) {
        this.access = access;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public TaskDetailsBean getTaskDetailsBean() {
        return taskDetailsBean;
    }

    public void setTaskDetailsBean(TaskDetailsBean taskDetailsBean) {
        this.taskDetailsBean = taskDetailsBean;
    }

    public Object getMessage_Details_Object() {
        return message_Details_Object;
    }

    public void setMessage_Details_Object(Object message_Details_Object) {
        this.message_Details_Object = message_Details_Object;
    }

    public ArrayList<TaskDetailsBean> getGetTaskListforPercentage() {
        return getTaskListforPercentage;
    }

    public void setGetTaskListforPercentage(ArrayList<TaskDetailsBean> getTaskListforPercentage) {
        this.getTaskListforPercentage = getTaskListforPercentage;
    }

    public ArrayList<ListofFileds> getFiledsArrayList() {
        return filedsArrayList;
    }

    public void setFiledsArrayList(ArrayList<ListofFileds> filedsArrayList) {
        this.filedsArrayList = filedsArrayList;
    }

    public GroupMemberAccess getGroupMemberAccess() {
        return groupMemberAccess;
    }

    public void setGroupMemberAccess(GroupMemberAccess groupMemberAccess) {
        this.groupMemberAccess = groupMemberAccess;
    }

    public Object getList_Member_Acces() {
        return list_Member_Acces;
    }

    public void setList_Member_Acces(Object list_Member_Acces) {
        this.list_Member_Acces = list_Member_Acces;
    }

    public ArrayList<TaskDetailsBean> getGetStatusListForMedia() {
        return getStatusListForMedia;
    }

    public void setGetStatusListForMedia(ArrayList<TaskDetailsBean> getStatusListForMedia) {
        this.getStatusListForMedia = getStatusListForMedia;
    }

    public String getInputString() {
        return inputString;
    }

    public void setInputString(String inputString) {
        this.inputString = inputString;
    }
}
