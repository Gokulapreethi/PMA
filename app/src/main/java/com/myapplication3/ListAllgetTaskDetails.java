package com.myapplication3;

import com.google.gson.annotations.SerializedName;
import com.myapplication3.Bean.Group;
import com.myapplication3.Bean.ListTaskTransaction;

import java.util.ArrayList;

import json.ListFromDetails;
import json.ListMember;
import json.ListTaskConversation;

/**
 * Created by prasanth on 8/19/2016.
 */
public class ListAllgetTaskDetails {
    @SerializedName("id")
    int id;
    @SerializedName("signalId")
    String signalId;
    //    @SerializedName("listObserver")
//    String listObserver;
    @SerializedName("parentId")
    String parentId;
    @SerializedName("name")
    String name;
    @SerializedName("description")
    String description;
    @SerializedName("to")
    String to;
    @SerializedName("projectId")
    String projectId;
//    @SerializedName("group")
//    String group;
    /*public ListAllgetTaskDetails (String id,String signalId,String name,String description, ListFromDetails from,ListFromDetails toUser,String projectId,String group,String to){

        this.id=id;
        this.signalId=signalId;
        this.name=name;
        this.description=description;
        this.from=from;
        this.toUser=toUser;
        this.projectId=projectId;
        this.group=group;
        this.to=to;
    }*/

    String taskNo;
    String plannedStartDateTime;
    String plannedEndDateTime;
    String actualStartDateTime;
    String actualEndDateTime;
    String status;
    String readStatus;
    String parentTask;
    String isRemainderRequired;
    String remainderDateTime;
    String remainderQuotes;
    String dependentTasks;
    String taskOwner;
    String createdBy;
    String createdDate;
    String updatedBy;
    String updatedDate;
    String latestDescription;
    String completedPercentage;
    String version;
    /* ArrayList<taskDescriptions> taskDes;
        ArrayList<taskFiles> taskfiles;*/
    ArrayList<ListTaskFiles> listTaskFiles;
    ArrayList<ListTaskConversation> listTaskConversation;
    ArrayList<ListTaskTransaction> listTaskTransaction;
    ArrayList<ListMember> listTaskToUser;
    String taskPriority;
    String taskOverDue;
    String formattedStartDate;
    String formattedEndDate;
    String formattedReminderDate;
    String duration;
    String durationWords;
    String dateFrequency;
    String timeFrequency;
    String remark;
    String type;
    String searchType;
    String replyRequired;
    String isGroupTask;
	String oracleTaskId;
    String estimatedTravelHrs;

    @SerializedName("from")
    private ListFromDetails from;
    @SerializedName("toUser")
    private ListFromDetails toUser;
    @SerializedName("group")
    private Group group;
    private String taskCategory;

    ArrayList<ListFromDetails> listObserver;

    public ArrayList<ListFromDetails> getListObserver() {
        return listObserver;
    }

    public void setListObserver(ArrayList<ListFromDetails> listObserver) {
        this.listObserver = listObserver;
    }


    public ArrayList<ListTaskFiles> getListTaskFiles() {
        return listTaskFiles;
    }

    public void setListTaskFiles(ArrayList<ListTaskFiles> listTaskFiles) {
        this.listTaskFiles = listTaskFiles;
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

    public String getPlannedStartDateTime() {
        return plannedStartDateTime;
    }

    public void setPlannedStartDateTime(String plannedStartDateTime) {
        this.plannedStartDateTime = plannedStartDateTime;
    }

    public ListFromDetails getFromlist() {
        return from;
    }

    public void setFromlist(ListFromDetails fromlist) {
        this.from = fromlist;
    }

    public ListFromDetails getTolist() {
        return toUser;
    }

    public void setTolist(ListFromDetails tolist) {
        this.toUser = tolist;
    }

    public String getPlannedEndDateTime() {
        return plannedEndDateTime;
    }

    public void setPlannedEndDateTime(String plannedEndDateTime) {
        this.plannedEndDateTime = plannedEndDateTime;
    }

    public String getActualStartDateTime() {
        return actualStartDateTime;
    }

    public void setActualStartDateTime(String actualStartDateTime) {
        this.actualStartDateTime = actualStartDateTime;
    }

    public String getActualEndDateTime() {
        return actualEndDateTime;
    }

    public void setActualEndDateTime(String actualEndDateTime) {
        this.actualEndDateTime = actualEndDateTime;
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

    public String getParentTask() {
        return parentTask;
    }

    public void setParentTask(String parentTask) {
        this.parentTask = parentTask;
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

    public String getDependentTasks() {
        return dependentTasks;
    }

    public void setDependentTasks(String dependentTasks) {
        this.dependentTasks = dependentTasks;
    }

    public String getTaskOwner() {
        return taskOwner;
    }

    public void setTaskOwner(String taskOwner) {
        this.taskOwner = taskOwner;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getLatestDescription() {
        return latestDescription;
    }

    public void setLatestDescription(String latestDescription) {
        this.latestDescription = latestDescription;
    }

    public String getCompletedPercentage() {
        return completedPercentage;
    }

    public void setCompletedPercentage(String completedPercentage) {
        this.completedPercentage = completedPercentage;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public String getFormattedStartDate() {
        return formattedStartDate;
    }

    public void setFormattedStartDate(String formattedStartDate) {
        this.formattedStartDate = formattedStartDate;
    }

    public String getFormattedEndDate() {
        return formattedEndDate;
    }

    public void setFormattedEndDate(String formattedEndDate) {
        this.formattedEndDate = formattedEndDate;
    }

    public String getFormattedReminderDate() {
        return formattedReminderDate;
    }

    public void setFormattedReminderDate(String formattedReminderDate) {
        this.formattedReminderDate = formattedReminderDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDurationWords() {
        return durationWords;
    }

    public void setDurationWords(String durationWords) {
        this.durationWords = durationWords;
    }

    public String getDateFrequency() {
        return dateFrequency;
    }

    public void setDateFrequency(String dateFrequency) {
        this.dateFrequency = dateFrequency;
    }

    public String getTimeFrequency() {
        return timeFrequency;
    }

    public void setTimeFrequency(String timeFrequency) {
        this.timeFrequency = timeFrequency;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public String getSignalId() {
        return signalId;
    }

    public void setSignalId(String signalId) {
        this.signalId = signalId;
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

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /*public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }*/

//    public String getListObserver() {
//        return listObserver;
//    }
//
//    public void setListObserver(String listObserver) {
//        this.listObserver = listObserver;
//    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public ListFromDetails getFrom() {
        return from;
    }

    public void setFrom(ListFromDetails from) {
        this.from = from;
    }

    public ListFromDetails getToUser() {
        return toUser;
    }

    public void setToUser(ListFromDetails toUser) {
        this.toUser = toUser;
    }

    public String getRemainderQuotes() {
        return remainderQuotes;
    }

    public void setRemainderQuotes(String remainderQuotes) {
        this.remainderQuotes = remainderQuotes;
    }

    public ArrayList<ListTaskConversation> getListTaskConversation() {
        return listTaskConversation;
    }

    public void setListTaskConversation(ArrayList<ListTaskConversation> listTaskConversation) {
        this.listTaskConversation = listTaskConversation;
    }

    public ArrayList<ListMember> getListTaskToUser() {
        return listTaskToUser;
    }

    public void setListTaskToUser(ArrayList<ListMember> listTaskToUser) {
        this.listTaskToUser = listTaskToUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getReplyRequired() {
        return replyRequired;
    }

    public void setReplyRequired(String replyRequired) {
        this.replyRequired = replyRequired;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsGroupTask() {
        return isGroupTask;
    }

    public void setIsGroupTask(String isGroupTask) {
        this.isGroupTask = isGroupTask;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
    }

    public ArrayList<ListTaskTransaction> getListTaskTransaction() {
        return listTaskTransaction;
    }

    public void setListTaskTransaction(ArrayList<ListTaskTransaction> listTaskTransaction) {
        this.listTaskTransaction = listTaskTransaction;
    }

    public String getOracleTaskId() {
        return oracleTaskId;
    }

    public void setOracleTaskId(String oracleTaskId) {
        this.oracleTaskId = oracleTaskId;
    }

    public String getEstimatedTravelHrs() {
        return estimatedTravelHrs;
    }

    public void setEstimatedTravelHrs(String estimatedTravelHrs) {
        this.estimatedTravelHrs = estimatedTravelHrs;
    }
}
