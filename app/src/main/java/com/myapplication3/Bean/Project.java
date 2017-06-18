
package com.myapplication3.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Project {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("projectName")
    @Expose
    private String projectName;
    @SerializedName("organisationId")
    @Expose
    private Object organisationId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("createdBy")
    @Expose
    private Integer createdBy;
    @SerializedName("createdDate")
    @Expose
    private Object createdDate;
    @SerializedName("updatedBy")
    @Expose
    private Object updatedBy;
    @SerializedName("updatedDate")
    @Expose
    private Object updatedDate;
    @SerializedName("organisations")
    @Expose
    private Object organisations;
    @SerializedName("organisation")
    @Expose
    private Object organisation;
    @SerializedName("deleteProject")
    @Expose
    private Object deleteProject;
    @SerializedName("projectOwner")
    @Expose
    private Object projectOwner;
    @SerializedName("listMemberProject")
    @Expose
    private List<Object> listMemberProject = null;
    @SerializedName("listParentTask")
    @Expose
    private List<Object> listParentTask = null;
    @SerializedName("listSubTask")
    @Expose
    private List<Object> listSubTask = null;
    @SerializedName("hasParentTask")
    @Expose
    private Object hasParentTask;
    @SerializedName("parentTask")
    @Expose
    private Object parentTask;
    @SerializedName("addMembers")
    @Expose
    private Object addMembers;
    @SerializedName("listOrgObservers")
    @Expose
    private List<Object> listOrgObservers = null;
    @SerializedName("oracleProjectId")
    @Expose
    private String oracleProjectId;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("mcModel")
    @Expose
    private String mcModel;
    @SerializedName("mcSrNo")
    @Expose
    private String mcSrNo;
    @SerializedName("serviceRequestDate")
    @Expose
    private Object serviceRequestDate;
    @SerializedName("chasisNo")
    @Expose
    private Object chasisNo;
    @SerializedName("observation")
    @Expose
    private Object observation;
    @SerializedName("oracleCustomerId")
    @Expose
    private Object oracleCustomerId;
    @SerializedName("activity")
    @Expose
    private Object activity;
    @SerializedName("processFlag")
    @Expose
    private Object processFlag;
    @SerializedName("projectCompletedStatus")
    @Expose
    private Object projectCompletedStatus;
    @SerializedName("isActiveStatus")
    @Expose
    private Object isActiveStatus;
    @SerializedName("jobCardType")
    @Expose
    private Object jobCardType;
    @SerializedName("machineMake")
    @Expose
    private Object machineMake;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Object getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Object organisationId) {
        this.organisationId = organisationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
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

    public Object getOrganisations() {
        return organisations;
    }

    public void setOrganisations(Object organisations) {
        this.organisations = organisations;
    }

    public Object getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Object organisation) {
        this.organisation = organisation;
    }

    public Object getDeleteProject() {
        return deleteProject;
    }

    public void setDeleteProject(Object deleteProject) {
        this.deleteProject = deleteProject;
    }

    public Object getProjectOwner() {
        return projectOwner;
    }

    public void setProjectOwner(Object projectOwner) {
        this.projectOwner = projectOwner;
    }

    public List<Object> getListMemberProject() {
        return listMemberProject;
    }

    public void setListMemberProject(List<Object> listMemberProject) {
        this.listMemberProject = listMemberProject;
    }

    public List<Object> getListParentTask() {
        return listParentTask;
    }

    public void setListParentTask(List<Object> listParentTask) {
        this.listParentTask = listParentTask;
    }

    public List<Object> getListSubTask() {
        return listSubTask;
    }

    public void setListSubTask(List<Object> listSubTask) {
        this.listSubTask = listSubTask;
    }

    public Object getHasParentTask() {
        return hasParentTask;
    }

    public void setHasParentTask(Object hasParentTask) {
        this.hasParentTask = hasParentTask;
    }

    public Object getParentTask() {
        return parentTask;
    }

    public void setParentTask(Object parentTask) {
        this.parentTask = parentTask;
    }

    public Object getAddMembers() {
        return addMembers;
    }

    public void setAddMembers(Object addMembers) {
        this.addMembers = addMembers;
    }

    public List<Object> getListOrgObservers() {
        return listOrgObservers;
    }

    public void setListOrgObservers(List<Object> listOrgObservers) {
        this.listOrgObservers = listOrgObservers;
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

    public Object getServiceRequestDate() {
        return serviceRequestDate;
    }

    public void setServiceRequestDate(Object serviceRequestDate) {
        this.serviceRequestDate = serviceRequestDate;
    }

    public Object getChasisNo() {
        return chasisNo;
    }

    public void setChasisNo(Object chasisNo) {
        this.chasisNo = chasisNo;
    }

    public Object getObservation() {
        return observation;
    }

    public void setObservation(Object observation) {
        this.observation = observation;
    }

    public Object getOracleCustomerId() {
        return oracleCustomerId;
    }

    public void setOracleCustomerId(Object oracleCustomerId) {
        this.oracleCustomerId = oracleCustomerId;
    }

    public Object getActivity() {
        return activity;
    }

    public void setActivity(Object activity) {
        this.activity = activity;
    }

    public Object getProcessFlag() {
        return processFlag;
    }

    public void setProcessFlag(Object processFlag) {
        this.processFlag = processFlag;
    }

    public Object getProjectCompletedStatus() {
        return projectCompletedStatus;
    }

    public void setProjectCompletedStatus(Object projectCompletedStatus) {
        this.projectCompletedStatus = projectCompletedStatus;
    }

    public Object getIsActiveStatus() {
        return isActiveStatus;
    }

    public void setIsActiveStatus(Object isActiveStatus) {
        this.isActiveStatus = isActiveStatus;
    }

    public Object getJobCardType() {
        return jobCardType;
    }

    public void setJobCardType(Object jobCardType) {
        this.jobCardType = jobCardType;
    }

    public Object getMachineMake() {
        return machineMake;
    }

    public void setMachineMake(Object machineMake) {
        this.machineMake = machineMake;
    }

}
