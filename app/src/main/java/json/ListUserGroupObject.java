package json;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by vignesh on 6/4/2016.
 */
public class ListUserGroupObject {

    @SerializedName("email")
    private String email;
    @SerializedName("username")
    private String username;
    @SerializedName("code")
    private String code;
    @SerializedName("firstName")
    private String firstName;
    @SerializedName("lastName")
    private String lastName;
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("gender")
    private String gender;
    @SerializedName("profileImage")
    private String profileImage;
    @SerializedName("personalInfo")
    private String personalInfo;
    @SerializedName("jobCategory1")
    private String jobCategory1;
    @SerializedName("jobCategory2")
    private String jobCategory2;
    @SerializedName("jobCategory3")
    private String jobCategory3;
    @SerializedName("jobCategory4")
    private String jobCategory4;
    @SerializedName("textProfile")
    private String textProfile;
    @SerializedName("videoProfile")
    private String videoProfile;

    @SerializedName("userType")
    private String userType;
    @SerializedName("profession")
    private String profession;
    @SerializedName("organization")
    private String organization;
    @SerializedName("specialization")
    private String specialization;


    public String getVideoProfile() {
        return videoProfile;
    }

    public void setVideoProfile(String videoProfile) {
        this.videoProfile = videoProfile;
    }

    public String getTextProfile() {
        return textProfile;
    }

    public void setTextProfile(String textProfile) {
        this.textProfile = textProfile;
    }


    public ListUserGroupObject(String email, String username, String code, String firstName, String lastName, String id, String title, String gender, String profileImage, String personalInfo, String job1, String job2, String job3, String job4, String userType, String profession, String organization, String specialization) {
        this.email = email;
        this.username = username;
        this.code = code;
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.title = title;
        this.gender = gender;
        this.profileImage = profileImage;
        this.personalInfo = personalInfo;
        this.jobCategory1 = job1;
        this.jobCategory2 = job2;
        this.jobCategory3 = job3;
        this.jobCategory4 = job4;
        this.userType = userType;
        this.profession = profession;
        this.specialization = specialization;
        this.organization = organization;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(String personalInfo) {
        this.personalInfo = personalInfo;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
