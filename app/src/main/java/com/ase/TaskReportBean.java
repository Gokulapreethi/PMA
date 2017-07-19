package com.ase;

/**
 * Created by Preethi on 22-05-2017.
 */
public class TaskReportBean {
    String createdDate;
    String headerTagName;
    String setId;
    String value;
    String headerId;
    String ActualTotalHrs;
    String ActivityTotalHrs;

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getHeaderTagName() {
        return headerTagName;
    }

    public void setHeaderTagName(String headerTagName) {
        this.headerTagName = headerTagName;
    }

    public String getSetId() {
        return setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getActualTotalHrs() {
        return ActualTotalHrs;
    }

    public void setActualTotalHrs(String actualTotalHrs) {
        ActualTotalHrs = actualTotalHrs;
    }

    public String getActivityTotalHrs() {
        return ActivityTotalHrs;
    }

    public void setActivityTotalHrs(String activityTotalHrs) {
        ActivityTotalHrs = activityTotalHrs;
    }
}
