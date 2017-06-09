package com.myapplication3.Bean;

import java.io.Serializable;

/**
 * Created by saravanan on 2/20/2017.
 */

/*"{\"headerTagName\":\"To\",\"value\":\"murugan_hm.com\",\"headerId\":7,\"createdDate\":\"2017-02-20 06:17:40.0\",\"setId\":165}",*/

public class listofEscalations implements Serializable{

    String headerTagName;
    String value;
    String headerId;
    String createdDate;
    String setId;
    String clientId;
    String ActivityTotalHrs;

    public String getHeaderTagName() {
        return headerTagName;
    }

    public void setHeaderTagName(String headerTagName) {
        this.headerTagName = headerTagName;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getSetId() {
        return setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getActivityTotalHrs() {
        return ActivityTotalHrs;
    }

    public void setActivityTotalHrs(String activityTotalHrs) {
        ActivityTotalHrs = activityTotalHrs;
    }
}
