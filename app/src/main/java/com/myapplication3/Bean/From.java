
package com.myapplication3.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class From {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("departmentId")
    @Expose
    private int departmentId;
    @SerializedName("groupId")
    @Expose
    private Object groupId;
    @SerializedName("userStatus")
    @Expose
    private String userStatus;
    @SerializedName("loginStatus")
    @Expose
    private String loginStatus;
    @SerializedName("signedStatus")
    @Expose
    private Object signedStatus;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("organization")
    @Expose
    private Object organization;
    @SerializedName("confirmPassword")
    @Expose
    private Object confirmPassword;
    @SerializedName("newPassword")
    @Expose
    private Object newPassword;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("jobCategory1")
    @Expose
    private Object jobCategory1;
    @SerializedName("jobCategory2")
    @Expose
    private Object jobCategory2;
    @SerializedName("jobCategory3")
    @Expose
    private Object jobCategory3;
    @SerializedName("jobCategory4")
    @Expose
    private Object jobCategory4;
    @SerializedName("textProfile")
    @Expose
    private String textProfile;
    @SerializedName("textProfileContent")
    @Expose
    private Object textProfileContent;
    @SerializedName("textProfileExt")
    @Expose
    private Object textProfileExt;
    @SerializedName("videoProfile")
    @Expose
    private String videoProfile;
    @SerializedName("videoProfileContent")
    @Expose
    private Object videoProfileContent;
    @SerializedName("videoProfileExt")
    @Expose
    private Object videoProfileExt;
    @SerializedName("profileImage")
    @Expose
    private String profileImage;
    @SerializedName("profileImageContent")
    @Expose
    private Object profileImageContent;
    @SerializedName("profileImageExt")
    @Expose
    private Object profileImageExt;
    @SerializedName("personalInfo")
    @Expose
    private Object personalInfo;
    @SerializedName("professionalDesignation")
    @Expose
    private Object professionalDesignation;
    @SerializedName("userType")
    @Expose
    private Object userType;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("createdBy")
    @Expose
    private Object createdBy;
    @SerializedName("createdDate")
    @Expose
    private Object createdDate;
    @SerializedName("updatedBy")
    @Expose
    private Object updatedBy;
   /* @SerializedName("updatedDate")
    @Expose
    private long updatedDate;*/
    @SerializedName("userImage")
    @Expose
    private Object userImage;
    @SerializedName("listUsers")
    @Expose
    private Object listUsers;
    @SerializedName("addMembers")
    @Expose
    private Object addMembers;
    @SerializedName("deleteUsers")
    @Expose
    private Object deleteUsers;
    @SerializedName("listRole")
    @Expose
    private List<Object> listRole = null;
    @SerializedName("listGroup")
    @Expose
    private List<Object> listGroup = null;
    @SerializedName("departmentref")
    @Expose
    private Object departmentref;
    @SerializedName("textMultipart")
    @Expose
    private Object textMultipart;
    @SerializedName("videoMultipart")
    @Expose
    private Object videoMultipart;
    @SerializedName("mobileNo")
    @Expose
    private Object mobileNo;
    @SerializedName("groups")
    @Expose
    private Object groups;
    @SerializedName("roles")
    @Expose
    private Object roles;
    @SerializedName("city")
    @Expose
    private Object city;
    @SerializedName("district")
    @Expose
    private Object district;
    @SerializedName("state")
    @Expose
    private Object state;
    @SerializedName("country")
    @Expose
    private Object country;
    @SerializedName("profession")
    @Expose
    private Object profession;
    @SerializedName("occupation")
    @Expose
    private Object occupation;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("specialization")
    @Expose
    private String specialization;
    @SerializedName("accessMode")
    @Expose
    private Object accessMode;
    @SerializedName("percentageCompleted")
    @Expose
    private Object percentageCompleted;
    @SerializedName("oracleStatus")
    @Expose
    private Object oracleStatus;

    /**
     * 
     * @return
     *     The id
     */
    public int getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 
     * @param email
     *     The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 
     * @return
     *     The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * 
     * @param username
     *     The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 
     * @return
     *     The code
     */
    public String getCode() {
        return code;
    }

    /**
     * 
     * @param code
     *     The code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 
     * @return
     *     The departmentId
     */
    public int getDepartmentId() {
        return departmentId;
    }

    /**
     * 
     * @param departmentId
     *     The departmentId
     */
    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * 
     * @return
     *     The groupId
     */
    public Object getGroupId() {
        return groupId;
    }

    /**
     * 
     * @param groupId
     *     The groupId
     */
    public void setGroupId(Object groupId) {
        this.groupId = groupId;
    }

    /**
     * 
     * @return
     *     The userStatus
     */
    public String getUserStatus() {
        return userStatus;
    }

    /**
     * 
     * @param userStatus
     *     The userStatus
     */
    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * 
     * @return
     *     The loginStatus
     */
    public String getLoginStatus() {
        return loginStatus;
    }

    /**
     * 
     * @param loginStatus
     *     The loginStatus
     */
    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    /**
     * 
     * @return
     *     The signedStatus
     */
    public Object getSignedStatus() {
        return signedStatus;
    }

    /**
     * 
     * @param signedStatus
     *     The signedStatus
     */
    public void setSignedStatus(Object signedStatus) {
        this.signedStatus = signedStatus;
    }

    /**
     * 
     * @return
     *     The password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 
     * @param password
     *     The password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 
     * @return
     *     The firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * 
     * @param firstName
     *     The firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * 
     * @return
     *     The lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * 
     * @param lastName
     *     The lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * 
     * @return
     *     The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 
     * @param title
     *     The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     * @return
     *     The organization
     */
    public Object getOrganization() {
        return organization;
    }

    /**
     * 
     * @param organization
     *     The organization
     */
    public void setOrganization(Object organization) {
        this.organization = organization;
    }

    /**
     * 
     * @return
     *     The confirmPassword
     */
    public Object getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * 
     * @param confirmPassword
     *     The confirmPassword
     */
    public void setConfirmPassword(Object confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * 
     * @return
     *     The newPassword
     */
    public Object getNewPassword() {
        return newPassword;
    }

    /**
     * 
     * @param newPassword
     *     The newPassword
     */
    public void setNewPassword(Object newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * 
     * @return
     *     The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * 
     * @param gender
     *     The gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * 
     * @return
     *     The jobCategory1
     */
    public Object getJobCategory1() {
        return jobCategory1;
    }

    /**
     * 
     * @param jobCategory1
     *     The jobCategory1
     */
    public void setJobCategory1(Object jobCategory1) {
        this.jobCategory1 = jobCategory1;
    }

    /**
     * 
     * @return
     *     The jobCategory2
     */
    public Object getJobCategory2() {
        return jobCategory2;
    }

    /**
     * 
     * @param jobCategory2
     *     The jobCategory2
     */
    public void setJobCategory2(Object jobCategory2) {
        this.jobCategory2 = jobCategory2;
    }

    /**
     * 
     * @return
     *     The jobCategory3
     */
    public Object getJobCategory3() {
        return jobCategory3;
    }

    /**
     * 
     * @param jobCategory3
     *     The jobCategory3
     */
    public void setJobCategory3(Object jobCategory3) {
        this.jobCategory3 = jobCategory3;
    }

    /**
     * 
     * @return
     *     The jobCategory4
     */
    public Object getJobCategory4() {
        return jobCategory4;
    }

    /**
     * 
     * @param jobCategory4
     *     The jobCategory4
     */
    public void setJobCategory4(Object jobCategory4) {
        this.jobCategory4 = jobCategory4;
    }

    /**
     * 
     * @return
     *     The textProfile
     */
    public String getTextProfile() {
        return textProfile;
    }

    /**
     * 
     * @param textProfile
     *     The textProfile
     */
    public void setTextProfile(String textProfile) {
        this.textProfile = textProfile;
    }

    /**
     * 
     * @return
     *     The textProfileContent
     */
    public Object getTextProfileContent() {
        return textProfileContent;
    }

    /**
     * 
     * @param textProfileContent
     *     The textProfileContent
     */
    public void setTextProfileContent(Object textProfileContent) {
        this.textProfileContent = textProfileContent;
    }

    /**
     * 
     * @return
     *     The textProfileExt
     */
    public Object getTextProfileExt() {
        return textProfileExt;
    }

    /**
     * 
     * @param textProfileExt
     *     The textProfileExt
     */
    public void setTextProfileExt(Object textProfileExt) {
        this.textProfileExt = textProfileExt;
    }

    /**
     * 
     * @return
     *     The videoProfile
     */
    public String getVideoProfile() {
        return videoProfile;
    }

    /**
     * 
     * @param videoProfile
     *     The videoProfile
     */
    public void setVideoProfile(String videoProfile) {
        this.videoProfile = videoProfile;
    }

    /**
     * 
     * @return
     *     The videoProfileContent
     */
    public Object getVideoProfileContent() {
        return videoProfileContent;
    }

    /**
     * 
     * @param videoProfileContent
     *     The videoProfileContent
     */
    public void setVideoProfileContent(Object videoProfileContent) {
        this.videoProfileContent = videoProfileContent;
    }

    /**
     * 
     * @return
     *     The videoProfileExt
     */
    public Object getVideoProfileExt() {
        return videoProfileExt;
    }

    /**
     * 
     * @param videoProfileExt
     *     The videoProfileExt
     */
    public void setVideoProfileExt(Object videoProfileExt) {
        this.videoProfileExt = videoProfileExt;
    }

    /**
     * 
     * @return
     *     The profileImage
     */
    public String getProfileImage() {
        return profileImage;
    }

    /**
     * 
     * @param profileImage
     *     The profileImage
     */
    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    /**
     * 
     * @return
     *     The profileImageContent
     */
    public Object getProfileImageContent() {
        return profileImageContent;
    }

    /**
     * 
     * @param profileImageContent
     *     The profileImageContent
     */
    public void setProfileImageContent(Object profileImageContent) {
        this.profileImageContent = profileImageContent;
    }

    /**
     * 
     * @return
     *     The profileImageExt
     */
    public Object getProfileImageExt() {
        return profileImageExt;
    }

    /**
     * 
     * @param profileImageExt
     *     The profileImageExt
     */
    public void setProfileImageExt(Object profileImageExt) {
        this.profileImageExt = profileImageExt;
    }

    /**
     * 
     * @return
     *     The personalInfo
     */
    public Object getPersonalInfo() {
        return personalInfo;
    }

    /**
     * 
     * @param personalInfo
     *     The personalInfo
     */
    public void setPersonalInfo(Object personalInfo) {
        this.personalInfo = personalInfo;
    }

    /**
     * 
     * @return
     *     The professionalDesignation
     */
    public Object getProfessionalDesignation() {
        return professionalDesignation;
    }

    /**
     * 
     * @param professionalDesignation
     *     The professionalDesignation
     */
    public void setProfessionalDesignation(Object professionalDesignation) {
        this.professionalDesignation = professionalDesignation;
    }

    /**
     * 
     * @return
     *     The userType
     */
    public Object getUserType() {
        return userType;
    }

    /**
     * 
     * @param userType
     *     The userType
     */
    public void setUserType(Object userType) {
        this.userType = userType;
    }

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The createdBy
     */
    public Object getCreatedBy() {
        return createdBy;
    }

    /**
     * 
     * @param createdBy
     *     The createdBy
     */
    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * 
     * @return
     *     The createdDate
     */
    public Object getCreatedDate() {
        return createdDate;
    }

    /**
     * 
     * @param createdDate
     *     The createdDate
     */
    public void setCreatedDate(Object createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * 
     * @return
     *     The updatedBy
     */
    public Object getUpdatedBy() {
        return updatedBy;
    }

    /**
     * 
     * @param updatedBy
     *     The updatedBy
     */
    public void setUpdatedBy(Object updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * 
     * @return
     *     The updatedDate
     *//*
    public long getUpdatedDate() {
        return updatedDate;
    }

    *//**
     * 
     * @param updatedDate
     *     The updatedDate
     *//*
    public void setUpdatedDate(long updatedDate) {
        this.updatedDate = updatedDate;
    }*/

    /**
     * 
     * @return
     *     The userImage
     */
    public Object getUserImage() {
        return userImage;
    }

    /**
     * 
     * @param userImage
     *     The userImage
     */
    public void setUserImage(Object userImage) {
        this.userImage = userImage;
    }

    /**
     * 
     * @return
     *     The listUsers
     */
    public Object getListUsers() {
        return listUsers;
    }

    /**
     * 
     * @param listUsers
     *     The listUsers
     */
    public void setListUsers(Object listUsers) {
        this.listUsers = listUsers;
    }

    /**
     * 
     * @return
     *     The addMembers
     */
    public Object getAddMembers() {
        return addMembers;
    }

    /**
     * 
     * @param addMembers
     *     The addMembers
     */
    public void setAddMembers(Object addMembers) {
        this.addMembers = addMembers;
    }

    /**
     * 
     * @return
     *     The deleteUsers
     */
    public Object getDeleteUsers() {
        return deleteUsers;
    }

    /**
     * 
     * @param deleteUsers
     *     The deleteUsers
     */
    public void setDeleteUsers(Object deleteUsers) {
        this.deleteUsers = deleteUsers;
    }

    /**
     * 
     * @return
     *     The listRole
     */
    public List<Object> getListRole() {
        return listRole;
    }

    /**
     * 
     * @param listRole
     *     The listRole
     */
    public void setListRole(List<Object> listRole) {
        this.listRole = listRole;
    }

    /**
     * 
     * @return
     *     The listGroup
     */
    public List<Object> getListGroup() {
        return listGroup;
    }

    /**
     * 
     * @param listGroup
     *     The listGroup
     */
    public void setListGroup(List<Object> listGroup) {
        this.listGroup = listGroup;
    }

    /**
     * 
     * @return
     *     The departmentref
     */
    public Object getDepartmentref() {
        return departmentref;
    }

    /**
     * 
     * @param departmentref
     *     The departmentref
     */
    public void setDepartmentref(Object departmentref) {
        this.departmentref = departmentref;
    }

    /**
     * 
     * @return
     *     The textMultipart
     */
    public Object getTextMultipart() {
        return textMultipart;
    }

    /**
     * 
     * @param textMultipart
     *     The textMultipart
     */
    public void setTextMultipart(Object textMultipart) {
        this.textMultipart = textMultipart;
    }

    /**
     * 
     * @return
     *     The videoMultipart
     */
    public Object getVideoMultipart() {
        return videoMultipart;
    }

    /**
     * 
     * @param videoMultipart
     *     The videoMultipart
     */
    public void setVideoMultipart(Object videoMultipart) {
        this.videoMultipart = videoMultipart;
    }

    /**
     * 
     * @return
     *     The mobileNo
     */
    public Object getMobileNo() {
        return mobileNo;
    }

    /**
     * 
     * @param mobileNo
     *     The mobileNo
     */
    public void setMobileNo(Object mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * 
     * @return
     *     The groups
     */
    public Object getGroups() {
        return groups;
    }

    /**
     * 
     * @param groups
     *     The groups
     */
    public void setGroups(Object groups) {
        this.groups = groups;
    }

    /**
     * 
     * @return
     *     The roles
     */
    public Object getRoles() {
        return roles;
    }

    /**
     * 
     * @param roles
     *     The roles
     */
    public void setRoles(Object roles) {
        this.roles = roles;
    }

    /**
     * 
     * @return
     *     The city
     */
    public Object getCity() {
        return city;
    }

    /**
     * 
     * @param city
     *     The city
     */
    public void setCity(Object city) {
        this.city = city;
    }

    /**
     * 
     * @return
     *     The district
     */
    public Object getDistrict() {
        return district;
    }

    /**
     * 
     * @param district
     *     The district
     */
    public void setDistrict(Object district) {
        this.district = district;
    }

    /**
     * 
     * @return
     *     The state
     */
    public Object getState() {
        return state;
    }

    /**
     * 
     * @param state
     *     The state
     */
    public void setState(Object state) {
        this.state = state;
    }

    /**
     * 
     * @return
     *     The country
     */
    public Object getCountry() {
        return country;
    }

    /**
     * 
     * @param country
     *     The country
     */
    public void setCountry(Object country) {
        this.country = country;
    }

    /**
     * 
     * @return
     *     The profession
     */
    public Object getProfession() {
        return profession;
    }

    /**
     * 
     * @param profession
     *     The profession
     */
    public void setProfession(Object profession) {
        this.profession = profession;
    }

    /**
     * 
     * @return
     *     The occupation
     */
    public Object getOccupation() {
        return occupation;
    }

    /**
     * 
     * @param occupation
     *     The occupation
     */
    public void setOccupation(Object occupation) {
        this.occupation = occupation;
    }

    /**
     * 
     * @return
     *     The address
     */
    public Object getAddress() {
        return address;
    }

    /**
     * 
     * @param address
     *     The address
     */
    public void setAddress(Object address) {
        this.address = address;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public Object getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(Object accessMode) {
        this.accessMode = accessMode;
    }

    public Object getPercentageCompleted() {
        return percentageCompleted;
    }

    public void setPercentageCompleted(Object percentageCompleted) {
        this.percentageCompleted = percentageCompleted;
    }

    public Object getOracleStatus() {
        return oracleStatus;
    }

    public void setOracleStatus(Object oracleStatus) {
        this.oracleStatus = oracleStatus;
    }
}
