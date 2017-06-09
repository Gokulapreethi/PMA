
package com.myapplication3.Forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FormFieldsBean implements Serializable{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("formTemp")
    @Expose
    private FormTemp formTemp;
    @SerializedName("fieldName")
    @Expose
    private String fieldName;
    @SerializedName("fieldType")
    @Expose
    private String fieldType;
    @SerializedName("isInputRequired")
    @Expose
    private String isInputRequired;
    @SerializedName("createdBy")
    @Expose
    private Integer createdBy;
    /*@SerializedName("createdDate")
    @Expose
    private String createdDate;*/
    @SerializedName("updatedBy")
    @Expose
    private Object updatedBy;
    @SerializedName("updatedDate")
    @Expose
    private Object updatedDate;

    String clientId;

    String fieldValue;

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public FormTemp getFormTemp() {
        return formTemp;
    }

    public void setFormTemp(FormTemp formTemp) {
        this.formTemp = formTemp;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getIsInputRequired() {
        return isInputRequired;
    }

    public void setIsInputRequired(String isInputRequired) {
        this.isInputRequired = isInputRequired;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    /*public int getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(int createdDate) {
        this.createdDate = createdDate;
    }
*/
    public Object getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Object updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Object getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Object updatedDate) {
        this.updatedDate = updatedDate;
    }

}
