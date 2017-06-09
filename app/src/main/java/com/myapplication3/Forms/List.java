
package com.myapplication3.Forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class List implements Serializable {

    @SerializedName("fieldId")
    @Expose
    private Integer fieldId;
    @SerializedName("fieldType")
    @Expose
    private String fieldType;
    @SerializedName("fieldName")
    @Expose
    private String fieldName;
    @SerializedName("fieldValue")
    @Expose
    private String fieldValue;

    @SerializedName("isInputRequired")
    @Expose
    private String isInputRequired;



    @SerializedName("createdBy")
    @Expose
    private Integer createdBy;

    private String clientId;

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }


    public String getIsInputRequired() {
        return isInputRequired;
    }

    public void setIsInputRequired(String isInputRequired) {
        this.isInputRequired = isInputRequired;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Integer getFieldId() {
        return fieldId;
    }

    public void setFieldId(Integer fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

}
