
package com.myapplication3.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListTaskTransaction {

    @SerializedName("id")
    @Expose
    private Object id;
    @SerializedName("task")
    @Expose
    private Task task;
    @SerializedName("from")
    @Expose
    private From from;
    @SerializedName("travelStartTime")
    @Expose
    private String travelStartTime;
    @SerializedName("activityStartTime")
    @Expose
    private String activityStartTime;
    @SerializedName("activityEndTime")
    @Expose
    private String activityEndTime;
    @SerializedName("travelEndTime")
    @Expose
    private String travelEndTime;
    @SerializedName("signature")
    @Expose
    private String signature;
    @SerializedName("remarks")
    @Expose
    private String remarks;
    @SerializedName("observation")
    @Expose
    private String observation;
    @SerializedName("hourMeterReading")
    @Expose
    private String hourMeterReading;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("project")
    @Expose
    private Project project;
    @SerializedName("signatures")
    @Expose
    private String signatures;
    @SerializedName("customerSignatureName")
    @Expose
    private String customerSignatureName;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("technicianSignature")
    @Expose
    private String technicianSignature;
    @SerializedName("toTravelStartDateTime")
    @Expose
    private String toTravelStartDateTime;
    @SerializedName("toTravelEndDateTime")
    @Expose
    private String toTravelEndDateTime;
    @SerializedName("photos")
    @Expose
    private String photos;
    @SerializedName("technicianSignatures")
    @Expose
    private String technicianSignatures;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public String getTravelStartTime() {
        return travelStartTime;
    }

    public void setTravelStartTime(String travelStartTime) {
        this.travelStartTime = travelStartTime;
    }

    public String getActivityStartTime() {
        return activityStartTime;
    }

    public void setActivityStartTime(String activityStartTime) {
        this.activityStartTime = activityStartTime;
    }

    public String getActivityEndTime() {
        return activityEndTime;
    }

    public void setActivityEndTime(String activityEndTime) {
        this.activityEndTime = activityEndTime;
    }

    public String getTravelEndTime() {
        return travelEndTime;
    }

    public void setTravelEndTime(String travelEndTime) {
        this.travelEndTime = travelEndTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
    public String getToTravelStartDateTime() {
        return toTravelStartDateTime;
    }

    public void setToTravelStartDateTime(String toTravelStartDateTime) {
        this.toTravelStartDateTime = toTravelStartDateTime;
    }

    public String getToTravelEndDateTime() {
        return toTravelEndDateTime;
    }

    public void setToTravelEndDateTime(String toTravelEndDateTime) {
        this.toTravelEndDateTime = toTravelEndDateTime;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getHourMeterReading() {
        return hourMeterReading;
    }

    public void setHourMeterReading(String hourMeterReading) {
        this.hourMeterReading = hourMeterReading;
    }

    public String getSignatures() {
        return signatures;
    }

    public void setSignatures(String signatures) {
        this.signatures = signatures;
    }

    public String getCustomerSignatureName() {
        return customerSignatureName;
    }

    public void setCustomerSignatureName(String customerSignatureName) {
        this.customerSignatureName = customerSignatureName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTechnicianSignature() {
        return technicianSignature;
    }

    public void setTechnicianSignature(String technicianSignature) {
        this.technicianSignature = technicianSignature;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getTechnicianSignatures() {
        return technicianSignatures;
    }

    public void setTechnicianSignatures(String technicianSignatures) {
        this.technicianSignatures = technicianSignatures;
    }
}
