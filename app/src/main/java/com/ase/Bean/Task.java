
package com.ase.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Task {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("listObserver")
    @Expose
    private List<Object> listObserver = null;
    @SerializedName("signalId")
    @Expose
    private String signalId;
    @SerializedName("parentId")
    @Expose
    private String parentId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("projectId")
    @Expose
    private Integer projectId;
    @SerializedName("group")
    @Expose
    private Object group;
    @SerializedName("org")
    @Expose
    private Object org;
    @SerializedName("from")
    @Expose
    private Object from;
    @SerializedName("to")
    @Expose
    private Integer to;
    @SerializedName("toUser")
    @Expose
    private Object toUser;
    @SerializedName("taskNo")
    @Expose
    private String taskNo;
    @SerializedName("taskCategory")
    @Expose
    private String taskCategory;
    @SerializedName("plannedStartDateTime")
    @Expose
    private Object plannedStartDateTime;
    @SerializedName("plannedEndDateTime")
    @Expose
    private Object plannedEndDateTime;
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
    private Integer parentTask;
    @SerializedName("isRemainderRequired")
    @Expose
    private Object isRemainderRequired;
    @SerializedName("remainderDateTime")
    @Expose
    private Object remainderDateTime;
    @SerializedName("remainderTone")
    @Expose
    private Object remainderTone;
    @SerializedName("remainderQuotes")
    @Expose
    private String remainderQuotes;
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
    @SerializedName("updatedDate")
    @Expose
    private Object updatedDate;
    @SerializedName("latestDescription")
    @Expose
    private Object latestDescription;
    @SerializedName("completedPercentage")
    @Expose
    private Integer completedPercentage;
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
    private List<Object> listTaskFiles = null;
    @SerializedName("listTaskConversation")
    @Expose
    private List<Object> listTaskConversation = null;
    @SerializedName("listTaskTransaction")
    @Expose
    private List<Object> listTaskTransaction = null;
    @SerializedName("taskPriority")
    @Expose
    private Object taskPriority;
    @SerializedName("taskOverDue")
    @Expose
    private Boolean taskOverDue;
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
    private String timeFrequency;
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
    @SerializedName("listTaskToUser")
    @Expose
    private List<Object> listTaskToUser = null;
    @SerializedName("oracleTaskId")
    @Expose
    private Object oracleTaskId;
    @SerializedName("estimatedTravelHours")
    @Expose
    private Object estimatedTravelHours;
    @SerializedName("estimatedActivityHours")
    @Expose
    private Object estimatedActivityHours;
    @SerializedName("taskActivity")
    @Expose
    private Object taskActivity;
    @SerializedName("timeFrequencyPrefix")
    @Expose
    private Object timeFrequencyPrefix;
    @SerializedName("timeFrequencySuffix")
    @Expose
    private Object timeFrequencySuffix;
    @SerializedName("isFreeze")
    @Expose
    private Object isFreeze;
    @SerializedName("plannedStart")
    @Expose
    private Object plannedStart;
    @SerializedName("plannedEnd")
    @Expose
    private Object plannedEnd;
    @SerializedName("remindDate")
    @Expose
    private Object remindDate;
    @SerializedName("addTask")
    @Expose
    private Object addTask;
    @SerializedName("formId")
    @Expose
    private Object formId;
    @SerializedName("result_code")
    @Expose
    private Integer resultCode;
    @SerializedName("result_text")
    @Expose
    private Object resultText;
    @SerializedName("orgName")
    @Expose
    private Object orgName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Object> getListObserver() {
        return listObserver;
    }

    public void setListObserver(List<Object> listObserver) {
        this.listObserver = listObserver;
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

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Object getGroup() {
        return group;
    }

    public void setGroup(Object group) {
        this.group = group;
    }

    public Object getOrg() {
        return org;
    }

    public void setOrg(Object org) {
        this.org = org;
    }

    public Object getFrom() {
        return from;
    }

    public void setFrom(Object from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public Object getToUser() {
        return toUser;
    }

    public void setToUser(Object toUser) {
        this.toUser = toUser;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
    }

    public Object getPlannedStartDateTime() {
        return plannedStartDateTime;
    }

    public void setPlannedStartDateTime(Object plannedStartDateTime) {
        this.plannedStartDateTime = plannedStartDateTime;
    }

    public Object getPlannedEndDateTime() {
        return plannedEndDateTime;
    }

    public void setPlannedEndDateTime(Object plannedEndDateTime) {
        this.plannedEndDateTime = plannedEndDateTime;
    }

    public Object getActualStartDateTime() {
        return actualStartDateTime;
    }

    public void setActualStartDateTime(Object actualStartDateTime) {
        this.actualStartDateTime = actualStartDateTime;
    }

    public Object getActualEndDateTime() {
        return actualEndDateTime;
    }

    public void setActualEndDateTime(Object actualEndDateTime) {
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

    public Integer getParentTask() {
        return parentTask;
    }

    public void setParentTask(Integer parentTask) {
        this.parentTask = parentTask;
    }

    public Object getIsRemainderRequired() {
        return isRemainderRequired;
    }

    public void setIsRemainderRequired(Object isRemainderRequired) {
        this.isRemainderRequired = isRemainderRequired;
    }

    public Object getRemainderDateTime() {
        return remainderDateTime;
    }

    public void setRemainderDateTime(Object remainderDateTime) {
        this.remainderDateTime = remainderDateTime;
    }

    public Object getRemainderTone() {
        return remainderTone;
    }

    public void setRemainderTone(Object remainderTone) {
        this.remainderTone = remainderTone;
    }

    public String getRemainderQuotes() {
        return remainderQuotes;
    }

    public void setRemainderQuotes(String remainderQuotes) {
        this.remainderQuotes = remainderQuotes;
    }

    public Object getDependentTasks() {
        return dependentTasks;
    }

    public void setDependentTasks(Object dependentTasks) {
        this.dependentTasks = dependentTasks;
    }

    public Object getTaskOwner() {
        return taskOwner;
    }

    public void setTaskOwner(Object taskOwner) {
        this.taskOwner = taskOwner;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public Object getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Object createdDate) {
        this.createdDate = createdDate;
    }

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

    public Object getLatestDescription() {
        return latestDescription;
    }

    public void setLatestDescription(Object latestDescription) {
        this.latestDescription = latestDescription;
    }

    public Integer getCompletedPercentage() {
        return completedPercentage;
    }

    public void setCompletedPercentage(Integer completedPercentage) {
        this.completedPercentage = completedPercentage;
    }

    public Object getVersion() {
        return version;
    }

    public void setVersion(Object version) {
        this.version = version;
    }

    public List<Object> getTaskDescriptions() {
        return taskDescriptions;
    }

    public void setTaskDescriptions(List<Object> taskDescriptions) {
        this.taskDescriptions = taskDescriptions;
    }

    public List<Object> getTaskFiles() {
        return taskFiles;
    }

    public void setTaskFiles(List<Object> taskFiles) {
        this.taskFiles = taskFiles;
    }

    public List<Object> getListTaskFiles() {
        return listTaskFiles;
    }

    public void setListTaskFiles(List<Object> listTaskFiles) {
        this.listTaskFiles = listTaskFiles;
    }

    public List<Object> getListTaskConversation() {
        return listTaskConversation;
    }

    public void setListTaskConversation(List<Object> listTaskConversation) {
        this.listTaskConversation = listTaskConversation;
    }

    public List<Object> getListTaskTransaction() {
        return listTaskTransaction;
    }

    public void setListTaskTransaction(List<Object> listTaskTransaction) {
        this.listTaskTransaction = listTaskTransaction;
    }

    public Object getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(Object taskPriority) {
        this.taskPriority = taskPriority;
    }

    public Boolean getTaskOverDue() {
        return taskOverDue;
    }

    public void setTaskOverDue(Boolean taskOverDue) {
        this.taskOverDue = taskOverDue;
    }

    public Object getFormattedStartDate() {
        return formattedStartDate;
    }

    public void setFormattedStartDate(Object formattedStartDate) {
        this.formattedStartDate = formattedStartDate;
    }

    public Object getFormattedEndDate() {
        return formattedEndDate;
    }

    public void setFormattedEndDate(Object formattedEndDate) {
        this.formattedEndDate = formattedEndDate;
    }

    public Object getFormattedReminderDate() {
        return formattedReminderDate;
    }

    public void setFormattedReminderDate(Object formattedReminderDate) {
        this.formattedReminderDate = formattedReminderDate;
    }

    public Object getMeasure() {
        return measure;
    }

    public void setMeasure(Object measure) {
        this.measure = measure;
    }

    public Object getDateFrequency() {
        return dateFrequency;
    }

    public void setDateFrequency(Object dateFrequency) {
        this.dateFrequency = dateFrequency;
    }

    public String getTimeFrequency() {
        return timeFrequency;
    }

    public void setTimeFrequency(String timeFrequency) {
        this.timeFrequency = timeFrequency;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Object getSearchType() {
        return searchType;
    }

    public void setSearchType(Object searchType) {
        this.searchType = searchType;
    }

    public Object getReplyRequired() {
        return replyRequired;
    }

    public void setReplyRequired(Object replyRequired) {
        this.replyRequired = replyRequired;
    }

    public Object getDuration() {
        return duration;
    }

    public void setDuration(Object duration) {
        this.duration = duration;
    }

    public Object getDurationWords() {
        return durationWords;
    }

    public void setDurationWords(Object durationWords) {
        this.durationWords = durationWords;
    }

    public String getIsGroupTask() {
        return isGroupTask;
    }

    public void setIsGroupTask(String isGroupTask) {
        this.isGroupTask = isGroupTask;
    }

    public List<Object> getListTaskToUser() {
        return listTaskToUser;
    }

    public void setListTaskToUser(List<Object> listTaskToUser) {
        this.listTaskToUser = listTaskToUser;
    }

    public Object getOracleTaskId() {
        return oracleTaskId;
    }

    public void setOracleTaskId(Object oracleTaskId) {
        this.oracleTaskId = oracleTaskId;
    }

    public Object getEstimatedTravelHours() {
        return estimatedTravelHours;
    }

    public void setEstimatedTravelHours(Object estimatedTravelHours) {
        this.estimatedTravelHours = estimatedTravelHours;
    }

    public Object getEstimatedActivityHours() {
        return estimatedActivityHours;
    }

    public void setEstimatedActivityHours(Object estimatedActivityHours) {
        this.estimatedActivityHours = estimatedActivityHours;
    }

    public Object getTaskActivity() {
        return taskActivity;
    }

    public void setTaskActivity(Object taskActivity) {
        this.taskActivity = taskActivity;
    }

    public Object getTimeFrequencyPrefix() {
        return timeFrequencyPrefix;
    }

    public void setTimeFrequencyPrefix(Object timeFrequencyPrefix) {
        this.timeFrequencyPrefix = timeFrequencyPrefix;
    }

    public Object getTimeFrequencySuffix() {
        return timeFrequencySuffix;
    }

    public void setTimeFrequencySuffix(Object timeFrequencySuffix) {
        this.timeFrequencySuffix = timeFrequencySuffix;
    }

    public Object getIsFreeze() {
        return isFreeze;
    }

    public void setIsFreeze(Object isFreeze) {
        this.isFreeze = isFreeze;
    }

    public Object getPlannedStart() {
        return plannedStart;
    }

    public void setPlannedStart(Object plannedStart) {
        this.plannedStart = plannedStart;
    }

    public Object getPlannedEnd() {
        return plannedEnd;
    }

    public void setPlannedEnd(Object plannedEnd) {
        this.plannedEnd = plannedEnd;
    }

    public Object getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(Object remindDate) {
        this.remindDate = remindDate;
    }

    public Object getAddTask() {
        return addTask;
    }

    public void setAddTask(Object addTask) {
        this.addTask = addTask;
    }

    public Object getFormId() {
        return formId;
    }

    public void setFormId(Object formId) {
        this.formId = formId;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public Object getResultText() {
        return resultText;
    }

    public void setResultText(Object resultText) {
        this.resultText = resultText;
    }

    public Object getOrgName() {
        return orgName;
    }

    public void setOrgName(Object orgName) {
        this.orgName = orgName;
    }

}
