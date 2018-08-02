package json;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vignesh on 6/1/2016.
 */
public class Loginuserdetails implements Serializable {

    int id;
    String email;
    String username;
    String code;
    String departmentId;
    String groupId;
    String userStatus;
    String loginStatus;
    String signedStatus;
    String password;
    String firstName;
    String lastName;
    String title;
    String Organization;
    String gender;
    String profileImage;
    String profileImageContent;
    String profileImageExt;
    String personalInfo;
    String professionalDesignation;
    String userType;
    String status;
    String createdBy;
    String createdDate;
    String updatedBy;
    String updatedDate;
    String userImage;
    String listUsers;
    String addMembers;
    String deleteUsers;
    String jobCategory1;
    String jobCategory2;
    String jobCategory3;
    String jobCategory4;
    String textProfile;
    String videoProfile;
    String roleId;
    String roleName;

//    String roles;
    String profession;

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    String specialization;



    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

//    public String getRoles() {
//        return roles;
//    }

//    public void setRoles(String roles) {
//        this.roles = roles;
//    }



    public String getTextProfile() {
        return textProfile;
    }

    public void setTextProfile(String textProfile) {
        this.textProfile = textProfile;
    }

    public String getVideoProfile() {
        return videoProfile;
    }

    public void setVideoProfile(String videoProfile) {
        this.videoProfile = videoProfile;
    }

    public String getJobCategory1() {
        return jobCategory1;
    }

    public void setJobCategory1(String jobCategory1) {
        this.jobCategory1 = jobCategory1;
    }

    public String getJobCategory2() {
        return jobCategory2;
    }

    public void setJobCategory2(String jobCategory2) {
        this.jobCategory2 = jobCategory2;
    }

    public String getJobCategory3() {
        return jobCategory3;
    }

    public void setJobCategory3(String jobCategory3) {
        this.jobCategory3 = jobCategory3;
    }

    public String getJobCategory4() {
        return jobCategory4;
    }

    public void setJobCategory4(String jobCategory4) {
        this.jobCategory4 = jobCategory4;
    }

    //String listRole;
    ArrayList<Usergroubdetails> listGroup;
//    int code;
    String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId(){return id;}

    public void setId(int id){this.id=id;}

    public String getEmail(){return email;}

    public void setEmail(String email){this.email=email;}

    public String getUsername(){return username;}

    public void setUsername(String username){this.username=username;}

    public String getCode(){return code;}

    public void setCode(String code){this.code=code;}

    public String getDepartmentId(){return departmentId;}

    public void setDepartmentId(String departmentId){this.departmentId=departmentId;}

    public String getGroupId(){return groupId;}

    public void setGroupId(String groupId){this.groupId=groupId;}

    public String getUserStatus(){return userStatus;}

    public void setUserStatus(String userStatus){this.userStatus=userStatus;}

    public String getLoginStatus(){return loginStatus;}

    public void setLoginStatus(String loginStatus){this.loginStatus=loginStatus;}

    public String getSignedStatus(){return signedStatus;}

    public void setSignedStatus(String signedStatus){this.signedStatus=signedStatus;}

    public String getPassword(){return password;}

    public void setPassword(String password){this.password=password;}

    public String getFirstName(){return firstName;}

    public void setFirstName(String firstName){this.firstName=firstName;}

    public String getLastName(){return lastName;}

    public void setLastName(String lastName){this.lastName=lastName;}

    public String getTitle(){return title;}

    public void setTitle(String title){this.title=title;}

    public String getOrganization(){return Organization;}

    public void setOrganization(String Organization){this.Organization=Organization;}

    public String getGender(){return gender;}

    public void setGender(String gender){this.gender=gender;}

    public String getProfileImage(){return profileImage;}

    public void setProfileImage(String profileImage){this.profileImage=profileImage;}

    public String getProfileImageContent(){return profileImageContent;}

    public void setProfileImageContent(String profileImageContent){this.profileImageContent=profileImageContent;}

    public String getProfileImageExt() {
        return profileImageExt;
    }

    public void setProfileImageExt(String profileImageExt) {
        this.profileImageExt = profileImageExt;
    }

    public String getPersonalInfo(){return personalInfo;}

    public void setPersonalInfo(String personalInfo){this.personalInfo=personalInfo;}

    public String getProfessionalDesignation(){return professionalDesignation;}

    public void setProfessionalDesignation(String professionalDesignation){this.professionalDesignation=professionalDesignation;}

    public String getUserType(){return userType;}

    public void setUserType(String userType){this.userType=userType;}

    public String getStatus(){return status;}

    public void setStatus(String status){this.status=status;}

    public String getcreatedBy(){return createdBy;}

    public void setcreatedBy(String createdBy){this.createdBy=createdBy;}

    public String getcreatedDate(){return createdDate;}

    public void setcreatedDate(String createdDate){this.createdDate=createdDate;}

    public String getupdatedBy(){return updatedBy;}

    public void setupdatedBy(String updatedBy){this.updatedBy=updatedBy;}

    public String getupdatedDate(){return updatedDate;}

    public void setupdatedDate(String updatedDate){this.updatedDate=updatedDate;}

    public String getuserImage(){return userImage;}

    public void setuserImage(String userImage){this.userImage=userImage;}

    public String getlistUsers(){return listUsers;}

    public void setlistUsers(String clistUsersode){this.listUsers=listUsers;}

    public String getaddMembers(){return addMembers;}

    public void setaddMembers(String caddMembersode){this.addMembers=addMembers;}

    public String getdeleteUsers(){return deleteUsers;}

    public void setdeleteUsers(String deleteUsers){this.deleteUsers=deleteUsers;}

    public ArrayList<Usergroubdetails> getListGroup(){return listGroup;}

    public void setListGroup(ArrayList<Usergroubdetails> listGroup){this.listGroup=listGroup;}

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
