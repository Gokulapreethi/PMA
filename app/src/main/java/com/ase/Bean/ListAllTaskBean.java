
package com.ase.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListAllTaskBean {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("listObserver")
    @Expose
    private List<ListObserver> listObserver = null;
    @SerializedName("signalId")
    @Expose
    private String signalId;
    @SerializedName("parentId")
    @Expose
    private Object parentId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("projectId")
    @Expose
    private String projectId;
    @SerializedName("group")
    @Expose
    private Group group;
    @SerializedName("from")
    @Expose
    private From from;
    @SerializedName("to")
    @Expose
    private Object to;
    @SerializedName("toUser")
    @Expose
    private ToUser toUser;
    @SerializedName("taskNo")
    @Expose
    private String taskNo;
    @SerializedName("plannedStartDateTime")
    @Expose
    private Object plannedStartDateTime;
    @SerializedName("plannedEndDateTime")
    @Expose
    private String plannedEndDateTime;
    @SerializedName("actualStartDateTime")
    @Expose
    private Object actualStartDateTime;
    @SerializedName("actualEndDateTime")
    @Expose
    private Object actualEndDateTime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("readStatus")
    @Expose
    private String readStatus;
    @SerializedName("parentTask")
    @Expose
    private String parentTask;
    @SerializedName("isRemainderRequired")
    @Expose
    private String isRemainderRequired;
    @SerializedName("remainderDateTime")
    @Expose
    private Object remainderDateTime;
    @SerializedName("remainderTone")
    @Expose
    private Object remainderTone;
    @SerializedName("remainderQuotes")
    @Expose
    private Object remainderQuotes;
    @SerializedName("dependentTasks")
    @Expose
    private Object dependentTasks;
    @SerializedName("taskOwner")
    @Expose
    private Object taskOwner;
    @SerializedName("createdBy")
    @Expose
    private Object createdBy;
    @SerializedName("createdDate")
    @Expose
    private Object createdDate;
    @SerializedName("updatedBy")
    @Expose
    private Object updatedBy;
    /*@SerializedName("updatedDate")
    @Expose
    private long updatedDate;*/
    @SerializedName("latestDescription")
    @Expose
    private Object latestDescription;
    @SerializedName("completedPercentage")
    @Expose
    private int completedPercentage;
    @SerializedName("version")
    @Expose
    private Object version;
    @SerializedName("taskDescriptions")
    @Expose
    private List<Object> taskDescriptions = null;
    @SerializedName("taskFiles")
    @Expose
    private List<Object> taskFiles = null;
    @SerializedName("listTaskFiles")
    @Expose
    private List<ListTaskFile> listTaskFiles = null;
    @SerializedName("listTaskConversation")
    @Expose
    private List<Object> listTaskConversation = null;
    @SerializedName("taskPriority")
    @Expose
    private int taskPriority;
    @SerializedName("taskOverDue")
    @Expose
    private boolean taskOverDue;
    @SerializedName("formattedStartDate")
    @Expose
    private Object formattedStartDate;
    @SerializedName("formattedEndDate")
    @Expose
    private Object formattedEndDate;
    @SerializedName("formattedReminderDate")
    @Expose
    private Object formattedReminderDate;
    @SerializedName("measure")
    @Expose
    private Object measure;
    @SerializedName("dateFrequency")
    @Expose
    private Object dateFrequency;
    @SerializedName("timeFrequency")
    @Expose
    private Object timeFrequency;
    @SerializedName("remark")
    @Expose
    private Object remark;
    @SerializedName("type")
    @Expose
    private Object type;
    @SerializedName("searchType")
    @Expose
    private Object searchType;
    @SerializedName("replyRequired")
    @Expose
    private Object replyRequired;
    @SerializedName("duration")
    @Expose
    private Object duration;
    @SerializedName("durationWords")
    @Expose
    private Object durationWords;
    @SerializedName("isGroupTask")
    @Expose
    private String isGroupTask;
    private String taskCategory;

    /**
     * @return The id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return The listObserver
     */
    public List<ListObserver> getListObserver() {
        return listObserver;
    }

    /**
     * @param listObserver The listObserver
     */
    public void setListObserver(List<ListObserver> listObserver) {
        this.listObserver = listObserver;
    }

    /**
     * @return The signalId
     */
    public String getSignalId() {
        return signalId;
    }

    /**
     * @param signalId The signalId
     */
    public void setSignalId(String signalId) {
        this.signalId = signalId;
    }

    /**
     * @return The parentId
     */
    public Object getParentId() {
        return parentId;
    }

    /**
     * @param parentId The parentId
     */
    public void setParentId(Object parentId) {
        this.parentId = parentId;
    }

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The projectId
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * @param projectId The projectId
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**
     * @return The group
     */
    public Group getGroup() {
        return group;
    }

    /**
     * @param group The group
     */
    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * @return The from
     */
    public From getFrom() {
        return from;
    }

    /**
     * @param from The from
     */
    public void setFrom(From from) {
        this.from = from;
    }

    /**
     * @return The to
     */
    public Object getTo() {
        return to;
    }

    /**
     * @param to The to
     */
    public void setTo(Object to) {
        this.to = to;
    }

    /**
     * @return The toUser
     */
    public ToUser getToUser() {
        return toUser;
    }

    /**
     * @param toUser The toUser
     */
    public void setToUser(ToUser toUser) {
        this.toUser = toUser;
    }

    /**
     * @return The taskNo
     */
    public String getTaskNo() {
        return taskNo;
    }

    /**
     * @param taskNo The taskNo
     */
    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    /**
     * @return The plannedStartDateTime
     */
    public Object getPlannedStartDateTime() {
        return plannedStartDateTime;
    }

    /**
     * @param plannedStartDateTime The plannedStartDateTime
     */
    public void setPlannedStartDateTime(Object plannedStartDateTime) {
        this.plannedStartDateTime = plannedStartDateTime;
    }

    /**
     * @return The plannedEndDateTime
     */
    public String getPlannedEndDateTime() {
        return plannedEndDateTime;
    }

    /**
     * @param plannedEndDateTime The plannedEndDateTime
     */
    public void setPlannedEndDateTime(String plannedEndDateTime) {
        this.plannedEndDateTime = plannedEndDateTime;
    }

    /**
     * @return The actualStartDateTime
     */
    public Object getActualStartDateTime() {
        return actualStartDateTime;
    }

    /**
     * @param actualStartDateTime The actualStartDateTime
     */
    public void setActualStartDateTime(Object actualStartDateTime) {
        this.actualStartDateTime = actualStartDateTime;
    }

    /**
     * @return The actualEndDateTime
     */
    public Object getActualEndDateTime() {
        return actualEndDateTime;
    }

    /**
     * @param actualEndDateTime The actualEndDateTime
     */
    public void setActualEndDateTime(Object actualEndDateTime) {
        this.actualEndDateTime = actualEndDateTime;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The readStatus
     */
    public String getReadStatus() {
        return readStatus;
    }

    /**
     * @param readStatus The readStatus
     */
    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    /**
     * @return The parentTask
     */
    public String getParentTask() {
        return parentTask;
    }

    /**
     * @param parentTask The parentTask
     */
    public void setParentTask(String parentTask) {
        this.parentTask = parentTask;
    }

    /**
     * @return The isRemainderRequired
     */
    public String getIsRemainderRequired() {
        return isRemainderRequired;
    }

    /**
     * @param isRemainderRequired The isRemainderRequired
     */
    public void setIsRemainderRequired(String isRemainderRequired) {
        this.isRemainderRequired = isRemainderRequired;
    }

    /**
     * @return The remainderDateTime
     */
    public Object getRemainderDateTime() {
        return remainderDateTime;
    }

    /**
     * @param remainderDateTime The remainderDateTime
     */
    public void setRemainderDateTime(Object remainderDateTime) {
        this.remainderDateTime = remainderDateTime;
    }

    /**
     * @return The remainderTone
     */
    public Object getRemainderTone() {
        return remainderTone;
    }

    /**
     * @param remainderTone The remainderTone
     */
    public void setRemainderTone(Object remainderTone) {
        this.remainderTone = remainderTone;
    }

    /**
     * @return The remainderQuotes
     */
    public Object getRemainderQuotes() {
        return remainderQuotes;
    }

    /**
     * @param remainderQuotes The remainderQuotes
     */
    public void setRemainderQuotes(Object remainderQuotes) {
        this.remainderQuotes = remainderQuotes;
    }

    /**
     * @return The dependentTasks
     */
    public Object getDependentTasks() {
        return dependentTasks;
    }

    /**
     * @param dependentTasks The dependentTasks
     */
    public void setDependentTasks(Object dependentTasks) {
        this.dependentTasks = dependentTasks;
    }

    /**
     * @return The taskOwner
     */
    public Object getTaskOwner() {
        return taskOwner;
    }

    /**
     * @param taskOwner The taskOwner
     */
    public void setTaskOwner(Object taskOwner) {
        this.taskOwner = taskOwner;
    }

    /**
     * @return The createdBy
     */
    public Object getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy The createdBy
     */
    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return The createdDate
     */
    public Object getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate The createdDate
     */
    public void setCreatedDate(Object createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return The updatedBy
     */
    public Object getUpdatedBy() {
        return updatedBy;
    }

    /**
     * @param updatedBy The updatedBy
     */
    public void setUpdatedBy(Object updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * @return The updatedDate
     */
   /* public long getUpdatedDate() {
        return updatedDate;
    }

    *//**
     * @param updatedDate The updatedDate
     *//*
    public void setUpdatedDate(long updatedDate) {
        this.updatedDate = updatedDate;
    }*/

    /**
     * @return The latestDescription
     */
    public Object getLatestDescription() {
        return latestDescription;
    }

    /**
     * @param latestDescription The latestDescription
     */
    public void setLatestDescription(Object latestDescription) {
        this.latestDescription = latestDescription;
    }

    /**
     * @return The completedPercentage
     */
    public int getCompletedPercentage() {
        return completedPercentage;
    }

    /**
     * @param completedPercentage The completedPercentage
     */
    public void setCompletedPercentage(int completedPercentage) {
        this.completedPercentage = completedPercentage;
    }

    /**
     * @return The version
     */
    public Object getVersion() {
        return version;
    }

    /**
     * @param version The version
     */
    public void setVersion(Object version) {
        this.version = version;
    }

    /**
     * @return The taskDescriptions
     */
    public List<Object> getTaskDescriptions() {
        return taskDescriptions;
    }

    /**
     * @param taskDescriptions The taskDescriptions
     */
    public void setTaskDescriptions(List<Object> taskDescriptions) {
        this.taskDescriptions = taskDescriptions;
    }

    /**
     * @return The taskFiles
     */
    public List<Object> getTaskFiles() {
        return taskFiles;
    }

    /**
     * @param taskFiles The taskFiles
     */
    public void setTaskFiles(List<Object> taskFiles) {
        this.taskFiles = taskFiles;
    }

    /**
     * @return The listTaskFiles
     */
    public List<ListTaskFile> getListTaskFiles() {
        return listTaskFiles;
    }

    /**
     * @param listTaskFiles The listTaskFiles
     */
    public void setListTaskFiles(List<ListTaskFile> listTaskFiles) {
        this.listTaskFiles = listTaskFiles;
    }

    /**
     * @return The listTaskConversation
     */
    public List<Object> getListTaskConversation() {
        return listTaskConversation;
    }

    /**
     * @param listTaskConversation The listTaskConversation
     */
    public void setListTaskConversation(List<Object> listTaskConversation) {
        this.listTaskConversation = listTaskConversation;
    }

    /**
     * @return The taskPriority
     */
    public int getTaskPriority() {
        return taskPriority;
    }

    /**
     * @param taskPriority The taskPriority
     */
    public void setTaskPriority(int taskPriority) {
        this.taskPriority = taskPriority;
    }

    /**
     * @return The taskOverDue
     */
    public boolean isTaskOverDue() {
        return taskOverDue;
    }

    /**
     * @param taskOverDue The taskOverDue
     */
    public void setTaskOverDue(boolean taskOverDue) {
        this.taskOverDue = taskOverDue;
    }

    /**
     * @return The formattedStartDate
     */
    public Object getFormattedStartDate() {
        return formattedStartDate;
    }

    /**
     * @param formattedStartDate The formattedStartDate
     */
    public void setFormattedStartDate(Object formattedStartDate) {
        this.formattedStartDate = formattedStartDate;
    }

    /**
     * @return The formattedEndDate
     */
    public Object getFormattedEndDate() {
        return formattedEndDate;
    }

    /**
     * @param formattedEndDate The formattedEndDate
     */
    public void setFormattedEndDate(Object formattedEndDate) {
        this.formattedEndDate = formattedEndDate;
    }

    /**
     * @return The formattedReminderDate
     */
    public Object getFormattedReminderDate() {
        return formattedReminderDate;
    }

    /**
     * @param formattedReminderDate The formattedReminderDate
     */
    public void setFormattedReminderDate(Object formattedReminderDate) {
        this.formattedReminderDate = formattedReminderDate;
    }

    /**
     * @return The measure
     */
    public Object getMeasure() {
        return measure;
    }

    /**
     * @param measure The measure
     */
    public void setMeasure(Object measure) {
        this.measure = measure;
    }

    /**
     * @return The dateFrequency
     */
    public Object getDateFrequency() {
        return dateFrequency;
    }

    /**
     * @param dateFrequency The dateFrequency
     */
    public void setDateFrequency(Object dateFrequency) {
        this.dateFrequency = dateFrequency;
    }

    /**
     * @return The timeFrequency
     */
    public Object getTimeFrequency() {
        return timeFrequency;
    }

    /**
     * @param timeFrequency The timeFrequency
     */
    public void setTimeFrequency(Object timeFrequency) {
        this.timeFrequency = timeFrequency;
    }

    /**
     * @return The remark
     */
    public Object getRemark() {
        return remark;
    }

    /**
     * @param remark The remark
     */
    public void setRemark(Object remark) {
        this.remark = remark;
    }

    /**
     * @return The type
     */
    public Object getType() {
        return type;
    }

    /**
     * @param type The type
     */
    public void setType(Object type) {
        this.type = type;
    }

    /**
     * @return The searchType
     */
    public Object getSearchType() {
        return searchType;
    }

    /**
     * @param searchType The searchType
     */
    public void setSearchType(Object searchType) {
        this.searchType = searchType;
    }

    /**
     * @return The replyRequired
     */
    public Object getReplyRequired() {
        return replyRequired;
    }

    /**
     * @param replyRequired The replyRequired
     */
    public void setReplyRequired(Object replyRequired) {
        this.replyRequired = replyRequired;
    }

    /**
     * @return The duration
     */
    public Object getDuration() {
        return duration;
    }

    /**
     * @param duration The duration
     */
    public void setDuration(Object duration) {
        this.duration = duration;
    }

    /**
     * @return The durationWords
     */
    public Object getDurationWords() {
        return durationWords;
    }

    /**
     * @param durationWords The durationWords
     */
    public void setDurationWords(Object durationWords) {
        this.durationWords = durationWords;
    }

    /**
     * @return The isGroupTask
     */
    public String getIsGroupTask() {
        return isGroupTask;
    }

    /**
     * @param isGroupTask The isGroupTask
     */
    public void setIsGroupTask(String isGroupTask) {
        this.isGroupTask = isGroupTask;
    }

    public String getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
    }
}
