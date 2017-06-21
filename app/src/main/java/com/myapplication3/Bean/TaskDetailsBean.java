package com.myapplication3.Bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Amuthan on 01/07/2016.
 */

/*
<?xml version="1.0"?><TaskDetailsinfo><TaskDetails taskName="Task For Dev IOS" taskDescription="Type Your Description Here hello"
fromUserId="0" toUserId="0" taskNo="201607011337472648289" plannedStartDateTime="(null)" plannedEndDateTime="(null)"
isRemainderRequired="1" remainderFrequency="2016-06-24 20:00:00" taskStatus="A"
signalid="8459926191602335744"</TaskDetails></TaskDetailsinfo>
 */
public class TaskDetailsBean implements Serializable {

    private String taskName;
    private String taskDescription;
    private String fromUserId;
    private String fromUserName;
    private String toUserName;
    private String toUserId;
    private String taskNo;
    private String plannedStartDateTime;
    private String plannedEndDateTime;
    private String isRemainderRequired;
    private String remainderFrequency;
    private String utcPlannedStartDateTime;
    private String utcplannedEndDateTime;
    private String utcPemainderFrequency;
    private String taskStatus;
    private String signalid;
    private String toUserEmail;
    private String fromUserEmail;
    private String parentId;
    private String taskPriority;
    private String completedPercentage;
    private String dateTime;
    private String sendStatus;
    private String taskType;
    private String MimeType;
    private String ownerOfTask;
    private String dateFrequency;
    private String timeFrequency;
    private String taskId;
    private int msg_status = 0;
    private int show_progress = 0;
    private int read_status = 0;
    private String reminderQuote;
    private String taskRequestType;
    private String tasktime;
    private String serverFileName;
    private String TaskReceiver;
    private String taskObservers;
    private String requestStatus;
    private String groupTaskMembers;
    private String duration;
    private String durationUnit;
    private boolean playing;
    private String taskUTCTime;
    private String taskUTCDateTime;
    private boolean isSelect;
    private String rejectedObserver;
    private boolean youRemoved;
    private String daysOfTheWeek;
    private String repeatFrequency;
    private String subType;
    private String taskTagName;
    private String taskTagValue;
    private int customTagId;
    private boolean customTagVisible = true;
    private int customSetId;
    private String ws_send = null;
    private String syncEnable;
    private String projectId;
    private String longmessage;
    private String parentTaskId;
    private String catagory;
    private String issueId;
    private String Description;
    private String Organisation;
    private String Project_ownerName;
    private String ListMemberProject;
    private String Private_Member;
    private String taskAddObservers;
    private String taskRemoveObservers;
    private String isParentTask;
    private String reAssignFrom;
    private String reAssignTo;
    private String id;
    private String projectName;
    private String taskMemberList;
    private String taskPlannedBeforeEndDate;
    private String taskPlannedLatestEndDate;
    private String fromTaskName;
    private String toTaskName;


    private String EstimatedTravel;
    private String EstimatedActivity;
    private String TotalTravel;
    private String TotalActivity;
    private String StartDate;
    private String EndDate;
    private String Address;
    private String mcModel;
    private String mcSrNo;
    private String reportedBy;
    private String HMReading;
    private String Observation;
    private String Activity;
    private String CustomerRemarks;
    private String CustomerSignature;



    //ASE Newly Added
    private int result;
    private ArrayList<JobCardBean> jobcard;
    private String travelStartTime;
    private String activityStartTime;
    private String activityEndTime;
    private String travelEndTime;
    private String projectStatus;
    private String toTravelStartTime;
    private String toTravelEndTime;
    private String CustomerName;
    private String photoPath;
    private String technicianSignature;
    private String customerSignatureName;
    private String actionTaken;


    public String getEstimatedTravel() {
        return EstimatedTravel;
    }

    public void setEstimatedTravel(String estimatedTravel) {
        EstimatedTravel = estimatedTravel;
    }

    public String getEstimatedActivity() {
        return EstimatedActivity;
    }

    public void setEstimatedActivity(String estimatedActivity) {
        EstimatedActivity = estimatedActivity;
    }

    public String getTotalTravel() {
        return TotalTravel;
    }

    public void setTotalTravel(String totalTravel) {
        TotalTravel = totalTravel;
    }

    public String getTotalActivity() {
        return TotalActivity;
    }

    public void setTotalActivity(String totalActivity) {
        TotalActivity = totalActivity;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getMcModel() {
        return mcModel;
    }

    public void setMcModel(String mcModel) {
        this.mcModel = mcModel;
    }

    public String getMcSrNo() {
        return mcSrNo;
    }

    public void setMcSrNo(String mcSrNo) {
        this.mcSrNo = mcSrNo;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getHMReading() {
        return HMReading;
    }

    public void setHMReading(String HMReading) {
        this.HMReading = HMReading;
    }

    public String getObservation() {
        return Observation;
    }

    public void setObservation(String observation) {
        Observation = observation;
    }

    public String getActivity() {
        return Activity;
    }

    public void setActivity(String activity) {
        Activity = activity;
    }

    public String getCustomerRemarks() {
        return CustomerRemarks;
    }

    public void setCustomerRemarks(String customerRemarks) {
        CustomerRemarks = customerRemarks;
    }

    public String getCustomerSignature() {
        return CustomerSignature;
    }

    public void setCustomerSignature(String customerSignature) {
        CustomerSignature = customerSignature;
    }
//    private String overdue_Msg;

private String sender_reply;
    private String reply_sender_name;
    private String reply_mime_type;

    public String getReply_mime_type() {
        return reply_mime_type;
    }

    public void setReply_mime_type(String reply_mime_type) {
        this.reply_mime_type = reply_mime_type;
    }

    public String getReply_sender_name() {
        return reply_sender_name;
    }

    public void setReply_sender_name(String reply_sender_name) {
        this.reply_sender_name = reply_sender_name;
    }

    public String getSender_reply() {
        return sender_reply;
    }

    public void setSender_reply(String sender_reply) {
        this.sender_reply = sender_reply;
    }

    public String getPrivate_Member() {
        return Private_Member;
    }

    public void setPrivate_Member(String private_Member) {
        Private_Member = private_Member;
    }



    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }



    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }



    public boolean isYouRemoved() {
        return youRemoved;
    }

    public void setYouRemoved(boolean youRemoved) {
        this.youRemoved = youRemoved;
    }

    public String getRejectedObserver() {
        return rejectedObserver;
    }

    public void setRejectedObserver(String rejectedObserver) {
        this.rejectedObserver = rejectedObserver;
    }
    public String getDaysOfTheWeek() {
        return daysOfTheWeek;
    }

    public void setDaysOfTheWeek(String weekdays) {
        daysOfTheWeek = weekdays;
    }

    public String getRepeatFrequency() {
        return repeatFrequency;
    }

    public void setRepeatFrequency(String repeatFrequency) {
        this.repeatFrequency = repeatFrequency;
    }

    //    private boolean checkTaskIsOverdue;
//    private boolean checkTaskIsClosed;


//    public boolean isCheckTaskIsOverdue() {
//        return checkTaskIsOverdue;
//    }
//
//    public void setCheckTaskIsOverdue(boolean checkTaskIsOverdue) {
//        this.checkTaskIsOverdue = checkTaskIsOverdue;
//    }
//
//    public boolean isCheckTaskIsClosed() {
//        return checkTaskIsClosed;
//    }
//
//    public void setCheckTaskIsClosed(boolean checkTaskIsClosed) {
//        this.checkTaskIsClosed = checkTaskIsClosed;
//    }

    public boolean isSelect() {
        return isSelect;
    }

    public String getUtcPlannedStartDateTime() {
        return utcPlannedStartDateTime;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private boolean selected;

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public void setUtcPlannedStartDateTime(String utcPlannedStartDateTime) {
        this.utcPlannedStartDateTime = utcPlannedStartDateTime;
    }

    public String getUtcplannedEndDateTime() {
        return utcplannedEndDateTime;
    }

    public void setUtcplannedEndDateTime(String utcplannedEndDateTime) {
        this.utcplannedEndDateTime = utcplannedEndDateTime;
    }

    public String getUtcPemainderFrequency() {
        return utcPemainderFrequency;
    }

    public void setUtcPemainderFrequency(String utcPemainderFrequency) {
        this.utcPemainderFrequency = utcPemainderFrequency;
    }

    public String getTaskUTCTime() {
        return taskUTCTime;
    }

    public void setTaskUTCTime(String taskUTCTime) {
        this.taskUTCTime = taskUTCTime;
    }

    public String getTaskUTCDateTime() {
        return taskUTCDateTime;
    }

    public void setTaskUTCDateTime(String taskUTCDateTime) {
        this.taskUTCDateTime = taskUTCDateTime;
    }

    public String getGroupTaskMembers() {
        return groupTaskMembers;
    }

    public void setGroupTaskMembers(String groupTaskMembers) {
        this.groupTaskMembers = groupTaskMembers;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getTaskObservers() {
        return taskObservers;
    }

    public void setTaskObservers(String taskObservers) {
        this.taskObservers = taskObservers;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    private String remark;

    public String getReminderQuote() {
        return reminderQuote;
    }

    public void setReminderQuote(String reminderQuote) {
        this.reminderQuote = reminderQuote;
    }

    public String getTaskRequestType() {
        return taskRequestType;
    }

    public void setTaskRequestType(String taskRequestType) {
        this.taskRequestType = taskRequestType;
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getMimeType() {
        return MimeType;
    }

    public void setMimeType(String mimeType) {
        MimeType = mimeType;
    }

    public String getOwnerOfTask() {
        return ownerOfTask;
    }

    public void setOwnerOfTask(String ownerOfTask) {
        this.ownerOfTask = ownerOfTask;
    }

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

    public String getRemainderFrequency() {
        return remainderFrequency;
    }

    public void setRemainderFrequency(String remainderFrequency) {
        this.remainderFrequency = remainderFrequency;
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

    public String getToUserEmail() {
        return toUserEmail;
    }

    public void setToUserEmail(String toUserEmail) {
        this.toUserEmail = toUserEmail;
    }

    public String getFromUserEmail() {
        return fromUserEmail;
    }

    public void setFromUserEmail(String fromUserEmail) {
        this.fromUserEmail = fromUserEmail;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    public int getMsg_status() {
        return msg_status;
    }

    public void setMsg_status(int msg_status) {
        this.msg_status = msg_status;
    }

    public int getShow_progress() {
        return show_progress;
    }

    public void setShow_progress(int show_progress) {
        this.show_progress = show_progress;
    }

    public int getRead_status() {
        return read_status;
    }

    public void setRead_status(int read_status) {
        this.read_status = read_status;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getTasktime() {
        return tasktime;
    }

    public void setTasktime(String tasktime) {
        this.tasktime = tasktime;
    }

    public String getServerFileName() {
        return serverFileName;
    }

    public void setServerFileName(String serverFileName) {
        this.serverFileName = serverFileName;
    }

    public String getTaskReceiver() {
        return TaskReceiver;
    }

    public void setTaskReceiver(String taskReceiver) {
        TaskReceiver = taskReceiver;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getTaskTagName() {
        return taskTagName;
    }

    public void setTaskTagName(String taskTagName) {
        this.taskTagName = taskTagName;
    }

    public String getTaskTagValue() {
        return taskTagValue;
    }

    public void setTaskTagValue(String taskTagValue) {
        this.taskTagValue = taskTagValue;
    }

    public int getCustomTagId() {
        return customTagId;
    }

    public void setCustomTagId(int customTagId) {
        this.customTagId = customTagId;
    }

    public boolean isCustomTagVisible() {
        return customTagVisible;
    }

    public void setCustomTagVisible(boolean customTagVisible) {
        this.customTagVisible = customTagVisible;
    }

    public int getCustomSetId() {
        return customSetId;
    }

    public void setCustomSetId(int customSetId) {
        this.customSetId = customSetId;
    }

    public String getWs_send() {
        return ws_send;
    }

    public void setWs_send(String ws_send) {
        this.ws_send = ws_send;
    }

    public String getSyncEnable() {
        return syncEnable;
    }

    public void setSyncEnable(String syncEnable) {
        this.syncEnable = syncEnable;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public String getLongmessage() {
        return longmessage;
    }

    public void setLongmessage(String longmessage) {
        this.longmessage = longmessage;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getProject_ownerName() {
        return Project_ownerName;
    }

    public void setProject_ownerName(String project_ownerName) {
        Project_ownerName = project_ownerName;
    }

    public String getOrganisation() {
        return Organisation;
    }

    public void setOrganisation(String organisation) {
        Organisation = organisation;
    }

    public String getListMemberProject() {
        return ListMemberProject;
    }

    public void setListMemberProject(String listMemberProject) {
        ListMemberProject = listMemberProject;
    }

    public String getTaskAddObservers() {
        return taskAddObservers;
    }

    public void setTaskAddObservers(String taskAddObservers) {
        this.taskAddObservers = taskAddObservers;
    }

    public String getTaskRemoveObservers() {
        return taskRemoveObservers;
    }

    public void setTaskRemoveObservers(String taskRemoveObservers) {
        this.taskRemoveObservers = taskRemoveObservers;
    }

    public String getIsParentTask() {
        return isParentTask;
    }

    public void setIsParentTask(String isParentTask) {
        this.isParentTask = isParentTask;
    }

    public String getReAssignFrom() {
        return reAssignFrom;
    }

    public void setReAssignFrom(String reAssignFrom) {
        this.reAssignFrom = reAssignFrom;
    }

    public String getReAssignTo() {
        return reAssignTo;
    }

    public void setReAssignTo(String reAssignTo) {
        this.reAssignTo = reAssignTo;
    }

//    public String getOverdue_Msg() {
//        return overdue_Msg;
//    }
//
//    public void setOverdue_Msg(String overdue_Msg) {
//        this.overdue_Msg = overdue_Msg;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTaskMemberList() {
        return taskMemberList;
    }

    public void setTaskMemberList(String taskMemberList) {
        this.taskMemberList = taskMemberList;
    }

    public String getTaskPlannedBeforeEndDate() {
        return taskPlannedBeforeEndDate;
    }

    public void setTaskPlannedBeforeEndDate(String taskPlannedBeforeEndDate) {
        this.taskPlannedBeforeEndDate = taskPlannedBeforeEndDate;
    }

    public String getTaskPlannedLatestEndDate() {
        return taskPlannedLatestEndDate;
    }

    public void setTaskPlannedLatestEndDate(String taskPlannedLatestEndDate) {
        this.taskPlannedLatestEndDate = taskPlannedLatestEndDate;
    }

    public String getFromTaskName() {
        return fromTaskName;
    }

    public void setFromTaskName(String fromTaskName) {
        this.fromTaskName = fromTaskName;
    }

    public String getToTaskName() {
        return toTaskName;
    }

    public void setToTaskName(String toTaskName) {
        this.toTaskName = toTaskName;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public ArrayList<JobCardBean> getJobcard() {
        return jobcard;
    }

    public void setJobcard(ArrayList<JobCardBean> jobcard) {
        this.jobcard = jobcard;
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

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getToTravelStartTime() {
        return toTravelStartTime;
    }

    public void setToTravelStartTime(String toTravelStartTime) {
        this.toTravelStartTime = toTravelStartTime;
    }

    public String getToTravelEndTime() {
        return toTravelEndTime;
    }

    public void setToTravelEndTime(String toTravelEndTime) {
        this.toTravelEndTime = toTravelEndTime;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getTechnicianSignature() {
        return technicianSignature;
    }

    public void setTechnicianSignature(String technicianSignature) {
        this.technicianSignature = technicianSignature;
    }

    public String getCustomerSignatureName() {
        return customerSignatureName;
    }

    public void setCustomerSignatureName(String customerSignatureName) {
        this.customerSignatureName = customerSignatureName;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }
}


