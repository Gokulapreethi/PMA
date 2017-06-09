package com.myapplication3.Forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by vignesh on 2/3/2017.
 */
public class FormAccessBean implements Serializable {

    @SerializedName("taskId")
    @Expose
    private String taskId;

    @SerializedName("formId")
    @Expose
    private String formId;

    @SerializedName("formAccessId")
    @Expose
    private String fromAccessId;

    @SerializedName("Giver")
    @Expose
    private String Giver;

    @SerializedName("MemberName")
    @Expose
    private String MemberName;

    @SerializedName("AccessMode")
    @Expose
    private String AccessMode;

    @SerializedName("Type")
    @Expose
    private String Type;

    @SerializedName("touserId")
    @Expose
    private String touserId;

    public String getTouserId() {
        return touserId;
    }

    public void setTouserId(String touserId) {
        this.touserId = touserId;
    }


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFromAccessId() {
        return fromAccessId;
    }

    public void setFromAccessId(String fromAccessId) {
        this.fromAccessId = fromAccessId;
    }

    public String getGiver() {
        return Giver;
    }

    public void setGiver(String giver) {
        Giver = giver;
    }

    public String getMemberName() {
        return MemberName;
    }

    public void setMemberName(String memberName) {
        MemberName = memberName;
    }

    public String getAccessMode() {
        return AccessMode;
    }

    public void setAccessMode(String type) {
        AccessMode = type;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

}
