package com.ase.Bean;

import java.io.Serializable;

/**
 * Created by vignesh on 11/28/2016.
 */
public class ListofFileds implements Serializable{

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getIsInputRequired() {
        return isInputRequired;
    }

    public void setIsInputRequired(String isInputRequired) {
        this.isInputRequired = isInputRequired;
    }

    public String getCreatedBy() {
        return createdBy;
    }



    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getHeaderId() {
        return headerId;
    }

    public void setHeaderId(String headerId) {
        this.headerId = headerId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSignalId() {
        return signalId;
    }

    public void setSignalId(String signalId) {
        this.signalId = signalId;
    }

    String id;
    String task;
    String name;
    String dataType;
    String isInputRequired;
    String createdBy;
    String tagName;
    String headerId;
    String signalId;

    String clientId;

}
