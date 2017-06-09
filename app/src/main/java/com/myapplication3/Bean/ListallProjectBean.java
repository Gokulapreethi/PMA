package com.myapplication3.Bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Preethi on 31-05-2017.
 */
public class ListallProjectBean implements Serializable {
//    private String id;
//    private String projectName;
//    private String organisationId;
//    private String description;
//    private String organisations;
//    private ProjectOrganisationBean organisation;
//    private String deleteProject;
//    private ListMember projectOwner;

    //Added for ASE
    private ArrayList<ProjectDetailsBean> listallProject;
    private ProjectDetailsBean projectDTO;
    private String result_code;
    private String result_text;

//    private String project_ownerName;
//    private String projectCompletedPercentage;
//    private ArrayList<ListMember> listMemberProject;
//    private ArrayList<ListAllgetTaskDetails> listSubTask;
//    private ArrayList<ListMember> listTaskToUser;
//    public ListAllgetTaskDetails parentTask;
//    /*Added ASE*/
//    private String oracleProjectId;
//    private String customerName;
//    private String address;
//    private String mcModel;
//    private String mcSrNo;
//    private String serviceRequestDate;
//    private String chasisNo;
//    private String observation;
//    private int oracleCustomerId;
//    private String activity;
//    private String processFlag;

//    private String parentTaskId;
//    private String fromUserName;
//    private String toUserName;
//    private String fromUserId;
//    private String toUserId;
//    private String taskStatus;
//    private String completedPercentage;
//    private String ownerOfTask;
//    private String taskReceiver;
//    private String taskObservers;
//    private String taskNo;
//    private String taskId;
//    private String taskName;
//    private String taskDescription;
//    private String taskType;
//    private String mimeType;
//    private String taskMemberList;
//    private int read_status = 0;
//    private int msg_status = 0;
//    private String plannedStartDateTime;
//    private String plannedEndDateTime;
//    private String catagory;
//    private String isParentTask;
//    private String issueParentId;
//    private String requestStatus;


    public ProjectDetailsBean getProjectDTO() {
        return projectDTO;
    }

    public void setProjectDTO(ProjectDetailsBean projectDTO) {
        this.projectDTO = projectDTO;
    }

    public ArrayList<ProjectDetailsBean> getListallProject() {
        return listallProject;
    }

    public void setListallProject(ArrayList<ProjectDetailsBean> listallProject) {
        this.listallProject = listallProject;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getResult_text() {
        return result_text;
    }

    public void setResult_text(String result_text) {
        this.result_text = result_text;
    }
    /*  public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

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

    public String getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(String organisationId) {
        this.organisationId = organisationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrganisations() {
        return organisations;
    }

    public void setOrganisations(String organisations) {
        this.organisations = organisations;
    }

    public ProjectOrganisationBean getOrganisation() {
        return organisation;
    }

    public void setOrganisation(ProjectOrganisationBean organisation) {
        this.organisation = organisation;
    }

    public String getDeleteProject() {
        return deleteProject;
    }

    public void setDeleteProject(String deleteProject) {
        this.deleteProject = deleteProject;
    }

    public ListMember getProjectOwner() {
        return projectOwner;
    }

    public void setProjectOwner(ListMember projectOwner) {
        this.projectOwner = projectOwner;
    }

    public String getProject_ownerName() {
        return project_ownerName;
    }

    public void setProject_ownerName(String project_ownerName) {
        this.project_ownerName = project_ownerName;
    }

    public String getProjectCompletedPercentage() {
        return projectCompletedPercentage;
    }

    public void setProjectCompletedPercentage(String projectCompletedPercentage) {
        this.projectCompletedPercentage = projectCompletedPercentage;
    }

    public ArrayList<ListMember> getListMemberProject() {
        return listMemberProject;
    }

    public void setListMemberProject(ArrayList<ListMember> listMemberProject) {
        this.listMemberProject = listMemberProject;
    }

    public ArrayList<ListAllgetTaskDetails> getListSubTask() {
        return listSubTask;
    }

    public void setListSubTask(ArrayList<ListAllgetTaskDetails> listSubTask) {
        this.listSubTask = listSubTask;
    }

    public ArrayList<ListMember> getListTaskToUser() {
        return listTaskToUser;
    }

    public void setListTaskToUser(ArrayList<ListMember> listTaskToUser) {
        this.listTaskToUser = listTaskToUser;
    }

    public ListAllgetTaskDetails getParentTask() {
        return parentTask;
    }

    public void setParentTask(ListAllgetTaskDetails parentTask) {
        this.parentTask = parentTask;
    }

    public String getOracleProjectId() {
        return oracleProjectId;
    }

    public void setOracleProjectId(String oracleProjectId) {
        this.oracleProjectId = oracleProjectId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getServiceRequestDate() {
        return serviceRequestDate;
    }

    public void setServiceRequestDate(String serviceRequestDate) {
        this.serviceRequestDate = serviceRequestDate;
    }

    public String getChasisNo() {
        return chasisNo;
    }

    public void setChasisNo(String chasisNo) {
        this.chasisNo = chasisNo;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public int getOracleCustomerId() {
        return oracleCustomerId;
    }

    public void setOracleCustomerId(int oracleCustomerId) {
        this.oracleCustomerId = oracleCustomerId;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getProcessFlag() {
        return processFlag;
    }

    public void setProcessFlag(String processFlag) {
        this.processFlag = processFlag;
    }

    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
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

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getCompletedPercentage() {
        return completedPercentage;
    }

    public void setCompletedPercentage(String completedPercentage) {
        this.completedPercentage = completedPercentage;
    }

    public String getOwnerOfTask() {
        return ownerOfTask;
    }

    public void setOwnerOfTask(String ownerOfTask) {
        this.ownerOfTask = ownerOfTask;
    }

    public String getTaskReceiver() {
        return taskReceiver;
    }

    public void setTaskReceiver(String taskReceiver) {
        this.taskReceiver = taskReceiver;
    }

    public String getTaskObservers() {
        return taskObservers;
    }

    public void setTaskObservers(String taskObservers) {
        this.taskObservers = taskObservers;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getTaskMemberList() {
        return taskMemberList;
    }

    public void setTaskMemberList(String taskMemberList) {
        this.taskMemberList = taskMemberList;
    }

    public int getRead_status() {
        return read_status;
    }

    public void setRead_status(int read_status) {
        this.read_status = read_status;
    }

    public int getMsg_status() {
        return msg_status;
    }

    public void setMsg_status(int msg_status) {
        this.msg_status = msg_status;
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

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getIsParentTask() {
        return isParentTask;
    }

    public void setIsParentTask(String isParentTask) {
        this.isParentTask = isParentTask;
    }

    public String getIssueParentId() {
        return issueParentId;
    }

    public void setIssueParentId(String issueParentId) {
        this.issueParentId = issueParentId;
    }*/
}
