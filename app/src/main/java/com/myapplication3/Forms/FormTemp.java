
package com.myapplication3.Forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FormTemp implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("templateName")
    @Expose
    private String templateName;
    @SerializedName("organisationId")
    @Expose
    private Object organisationId;
    @SerializedName("organisationName")
    @Expose
    private Object organisationName;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("viewMode")
    @Expose
    private String viewMode;
    @SerializedName("entryMode")
    @Expose
    private Object entryMode;
    @SerializedName("isTemplate")
    @Expose
    private Object isTemplate;
   /* @SerializedName("createdDate")
    @Expose
    private Integer createdDate;*/
    @SerializedName("createdBy")
    @Expose
    private Object createdBy;
    @SerializedName("updatedDate")
    @Expose
    private Object updatedDate;
    @SerializedName("updatedBy")
    @Expose
    private Object updatedBy;
    @SerializedName("deleteForms")
    @Expose
    private Object deleteForms;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Object getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Object organisationId) {
        this.organisationId = organisationId;
    }

    public Object getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(Object organisationName) {
        this.organisationName = organisationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getViewMode() {
        return viewMode;
    }

    public void setViewMode(String viewMode) {
        this.viewMode = viewMode;
    }

    public Object getEntryMode() {
        return entryMode;
    }

    public void setEntryMode(Object entryMode) {
        this.entryMode = entryMode;
    }

    public Object getIsTemplate() {
        return isTemplate;
    }

    public void setIsTemplate(Object isTemplate) {
        this.isTemplate = isTemplate;
    }

  /*  public Integer getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Integer createdDate) {
        this.createdDate = createdDate;
    }*/

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public Object getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Object updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Object getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Object updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Object getDeleteForms() {
        return deleteForms;
    }

    public void setDeleteForms(Object deleteForms) {
        this.deleteForms = deleteForms;
    }

}
