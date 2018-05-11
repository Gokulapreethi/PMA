package com.ase.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Preethi on 4/24/2018.
 */

public class Label implements Serializable{

    @SerializedName("issueType")
    @Expose
    private String issueType;
    @SerializedName("item")
    @Expose
    private String item;
    @SerializedName("jobDescription")
    @Expose
    private String jobDescription;
    @SerializedName("jobstatus")
    @Expose
    private String jobstatus;
    @SerializedName("id")
    @Expose
    private String id;

    private String quantity;

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobstatus() {
        return jobstatus;
    }

    public void setJobstatus(String jobstatus) {
        this.jobstatus = jobstatus;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}