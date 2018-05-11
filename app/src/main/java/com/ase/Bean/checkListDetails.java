package com.ase.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Preethi on 4/24/2018.
 */

public class checkListDetails implements Serializable{
    @SerializedName("checkListName")
    @Expose
    private String checkListName;
    @SerializedName("serviceType")
    @Expose
    private String serviceType;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;
    @SerializedName("customer")
    @Expose
    private String customer;
    @SerializedName("hourMeter")
    @Expose
    private String hourMeter;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("label")
    @Expose
    private List<Label> label = null;

    private String checklist_projectId;

    private String checklist_taskId;

    private String checklist_fromId;

    private String customerAddress;

    private String remarks;

    private String advicetoCustomer;

    private String technicianName;

    private String clientName;

    private String signedDate;

    private String technicianSignature;

    private String clientSignature;

    private String remarks_path;

    private String advice_path;

    private String id;

    private String wsSendStatus;

    private String isServiceDone;



    public String getCheckListName() {
        return checkListName;
    }

    public void setCheckListName(String checkListName) {
        this.checkListName = checkListName;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getHourMeter() {
        return hourMeter;
    }

    public void setHourMeter(String hourMeter) {
        this.hourMeter = hourMeter;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Label> getLabel() {
        return label;
    }

    public void setLabel(List<Label> label) {
        this.label = label;
    }

    public String getChecklist_projectId() {
        return checklist_projectId;
    }

    public void setChecklist_projectId(String checklist_projectId) {
        this.checklist_projectId = checklist_projectId;
    }

    public String getChecklist_taskId() {
        return checklist_taskId;
    }

    public void setChecklist_taskId(String checklist_taskId) {
        this.checklist_taskId = checklist_taskId;
    }

    public String getChecklist_fromId() {
        return checklist_fromId;
    }

    public void setChecklist_fromId(String checklist_fromId) {
        this.checklist_fromId = checklist_fromId;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getAdvicetoCustomer() {
        return advicetoCustomer;
    }

    public void setAdvicetoCustomer(String advicetoCustomer) {
        this.advicetoCustomer = advicetoCustomer;
    }

    public String getTechnicianName() {
        return technicianName;
    }

    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(String signedDate) {
        this.signedDate = signedDate;
    }

    public String getTechnicianSignature() {
        return technicianSignature;
    }

    public void setTechnicianSignature(String technicianSignature) {
        this.technicianSignature = technicianSignature;
    }

    public String getClientSignature() {
        return clientSignature;
    }

    public void setClientSignature(String clientSignature) {
        this.clientSignature = clientSignature;
    }

    public String getRemarks_path() {
        return remarks_path;
    }

    public void setRemarks_path(String remarks_path) {
        this.remarks_path = remarks_path;
    }

    public String getAdvice_path() {
        return advice_path;
    }

    public void setAdvice_path(String advice_path) {
        this.advice_path = advice_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWsSendStatus() {
        return wsSendStatus;
    }

    public void setWsSendStatus(String wsSendStatus) {
        this.wsSendStatus = wsSendStatus;
    }

    public String getIsServiceDone() {
        return isServiceDone;
    }

    public void setIsServiceDone(String isServiceDone) {
        this.isServiceDone = isServiceDone;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }
}