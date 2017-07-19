package com.ase.Bean;

/**
 * Created by vignesh on 11/28/2016.
 */
public class ListofAttribute  {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSignalId() {
        return signalId;
    }

    public void setSignalId(String signalId) {
        this.signalId = signalId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
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

    public String getCompletedPercentage() {
        return completedPercentage;
    }

    public void setCompletedPercentage(String completedPercentage) {
        this.completedPercentage = completedPercentage;
    }

    public String getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
    }

    public String getTaskOverDue() {
        return taskOverDue;
    }

    public void setTaskOverDue(String taskOverDue) {
        this.taskOverDue = taskOverDue;
    }

    public String getIsGroupTask() {
        return isGroupTask;
    }

    public void setIsGroupTask(String isGroupTask) {
        this.isGroupTask = isGroupTask;
    }

    String id;
    String signalId;
    String parentId;
    String name;
    String description;
    String to;
    String taskNo;
    String status;
    String readStatus;
    String isRemainderRequired;
    String remainderDateTime;
    String completedPercentage;
    String taskPriority;
    String taskOverDue;
    String isGroupTask;

}
