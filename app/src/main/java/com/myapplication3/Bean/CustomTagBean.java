package com.myapplication3.Bean;

import java.io.Serializable;

/**
 * Created by saravanakumar on 11/29/2016.
 */
public class CustomTagBean implements Serializable {

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTagDataType() {
        return tagDataType;
    }

    public void setTagDataType(String tagDataType) {
        this.tagDataType = tagDataType;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getSetId() {
        return setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    String taskId;
    String tagName;
    String value;
    String tagDataType;
    String datetime;
    String createdDate;
    String tagId;
    String setId;
    String headerId;
}
