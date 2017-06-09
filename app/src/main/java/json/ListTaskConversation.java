package json;

import java.util.ArrayList;

/**
 * Created by prasanth on 7/28/2016.
 */
public class ListTaskConversation {
    int id;
    String task;
    String name;
    String signalId;
    String parentId;
    String description;

    String replyTaskConversation;
    String replyRequired;
    String isReplyRequiredDone;
    String percentageCompleted;
    String taskNo;
    String taskStartDateTime;
    String taskEndDateTime;
    String remainderDateTime;
    String remainderQuotes;
    String dateFrequency;
    String timeFrequency;
    String taskStatus;
    String requestType;
    String requestStatus;
    String createdBy;
    String duration;
    String remark;
    String isRemainderRequired;
    String createdDate;
    private ListFromDetails from;
    private ListFromDetails to;
    ArrayList<ListTaskConversationFiles> listTaskConversationFiles;



    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getReplyTaskConversation() {
        return replyTaskConversation;
    }

    public void setReplyTaskConversation(String replyTaskConversation) {
        this.replyTaskConversation = replyTaskConversation;
    }

    public String getReplyRequired() {
        return replyRequired;
    }

    public void setReplyRequired(String replyRequired) {
        this.replyRequired = replyRequired;
    }

    public String getIsReplyRequiredDone() {
        return isReplyRequiredDone;
    }

    public void setIsReplyRequiredDone(String isReplyRequiredDone) {
        this.isReplyRequiredDone = isReplyRequiredDone;
    }

    public String getIsRemainderRequired() {
        return isRemainderRequired;
    }

    public void setIsRemainderRequired(String isRemainderRequired) {
        this.isRemainderRequired = isRemainderRequired;
    }

    public String getPercentageCompleted() {
        return percentageCompleted;
    }

    public void setPercentageCompleted(String percentageCompleted) {
        this.percentageCompleted = percentageCompleted;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getTaskStartDateTime() {
        return taskStartDateTime;
    }

    public void setTaskStartDateTime(String taskStartDateTime) {
        this.taskStartDateTime = taskStartDateTime;
    }

    public String getTaskEndDateTime() {
        return taskEndDateTime;
    }

    public void setTaskEndDateTime(String taskEndDateTime) {
        this.taskEndDateTime = taskEndDateTime;
    }

    public String getRemainderDateTime() {
        return remainderDateTime;
    }

    public void setRemainderDateTime(String remainderDateTime) {
        this.remainderDateTime = remainderDateTime;
    }

    public String getRemainderQuotes() {
        return remainderQuotes;
    }

    public void setRemainderQuotes(String remainderQuotes) {
        this.remainderQuotes = remainderQuotes;
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

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public ListFromDetails getFrom() {
        return from;
    }

    public void setFrom(ListFromDetails from) {
        this.from = from;
    }

    public ListFromDetails getTo() {
        return to;
    }

    public void setTo(ListFromDetails to) {
        this.to = to;
    }

    public ArrayList<ListTaskConversationFiles> getListTaskConversationFiles() {
        return listTaskConversationFiles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setListTaskConversationFiles(ArrayList<ListTaskConversationFiles> listTaskConversationFiles) {
        this.listTaskConversationFiles = listTaskConversationFiles;

    }
}
