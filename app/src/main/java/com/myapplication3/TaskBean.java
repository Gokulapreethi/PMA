package com.myapplication3;

/**
 * Created by saravanakumar on 7/11/2016.
 */
public class TaskBean {

    //<?xml version="1.0"?><TaskDetailsinfo><TaskDetails taskName="Task Details" taskDescription="Hi"
    // fromUserId="test_cognitivemobile.net" toUserId="aparna_cognitivemobile.net" taskNo="110720161204553982893632"
    // plannedStartDateTime="(null)"
    // plannedEndDateTime="(null)" isRemainderRequired="(null)" remainderDateTime="(null)"
    // taskStatus="assigned" signalid="11072016130028498588776" parentId="11072016130028304366736"
    // ="text" dateTime="2016-07-11 13:00:28" taskPriority="Medium" completedPercentage="0"  /></TaskDetailsinfo>

    String taskName;
    String taskDescription;
    String fromUserId;
    String toUserId;
    String taskNo;
    String plannedStartDateTime;
    String plannedEndDateTime;
    String isRemainderRequired;
    String remainderDateTime;
    String taskStatus;
    String signalid;
    String parentId;
    String dateTime;
    String taskPriority;
    String completedPercentage;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getPlannedStartDateTime() {
        return plannedStartDateTime;
    }

    public void setPlannedStartDateTime(String plannedStartDateTime) {
        this.plannedStartDateTime = plannedStartDateTime;
    }

    public String getPlannedEndDateTime() {
        return plannedEndDateTime;
    }

    public void setPlannedEndDateTime(String plannedEndDateTime) {
        this.plannedEndDateTime = plannedEndDateTime;
    }

    public String getIsRemainderRequired() {
        return isRemainderRequired;
    }

    public void setIsRemainderRequired(String isRemainderRequired) {
        this.isRemainderRequired = isRemainderRequired;
    }

    public String getRemainderDateTime() {
        return remainderDateTime;
    }

    public void setRemainderDateTime(String remainderDateTime) {
        this.remainderDateTime = remainderDateTime;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getSignalid() {
        return signalid;
    }

    public void setSignalid(String signalid) {
        this.signalid = signalid;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
    }

    public String getCompletedPercentage() {
        return completedPercentage;
    }

    public void setCompletedPercentage(String completedPercentage) {
        this.completedPercentage = completedPercentage;
    }
}
