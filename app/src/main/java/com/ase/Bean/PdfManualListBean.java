package com.ase.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Preethi on 6/6/2018.
 */

public class PdfManualListBean {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("createdDate")
    @Expose
    private Object createdDate;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("pdfFilePathName")
    @Expose
    private String pdfFilePathName;
    @SerializedName("file_Type")
    @Expose
    private String fileType;
    @SerializedName("user_Name ")
    @Expose
    private String userName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Object createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPdfFilePathName() {
        return pdfFilePathName;
    }

    public void setPdfFilePathName(String pdfFilePathName) {
        this.pdfFilePathName = pdfFilePathName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}