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

}