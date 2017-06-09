package json;

import org.pjsip.pjsua2.app.SipRegisterBroadastReceiver;

/**
 * Created by vignesh on 6/1/2016.
 */
public class Usergroubdetails {

    int id;
    String groupName;
    String groupOwner;
    String departmentId;
    String description;
    String logo;
    String createdBy;
    String createDate;
    String updatedBy;
    String updatedDate;
    String groupLogo;
    String departmentref;

    public int getId(){return id;}

    public void setId(int id){this.id=id;}

    public String getGroupName(){return groupName;}

    public void setGroupName(String groupName){this.groupName=groupName;}

    public String getGroupOwner(){return groupOwner;}

    public void setGroupOwner(String groupOwner){this.groupOwner=groupOwner;}

    public String getDepartmentId(){return departmentId;}

    public void setDepartmentId(String departmentId){this.departmentId=departmentId;}

    public String getDescription(){return description;}

    public void setDescription(String description){this.description=description;}

    public String getLogo(){return logo;}

    public void setLogo(String logo){this.logo=logo;}

    public String getCreatedBy(){return createdBy;}

    public void setCreatedBy(String createdBy){this.createdBy=createdBy;}

    public String getCreateDate(){return createDate;}

    public void setCreateDate(String createDate){this.createDate=createDate;}

    public String getUpdatedBy(){return updatedBy;}

    public void setUpdatedBy(String updatedBy){this.updatedBy=updatedBy;}

    public String getUpdatedDate(){return updatedDate;}

    public void setUpdatedDate(String updatedDate){this.updatedDate=updatedDate;}

    public String getGroupLogo(){return groupLogo;}

    public void setGroupLogo(String groupLogo){this.groupLogo=groupLogo;}

    public String getDepartmentref(){return departmentref;}

    public void setDepartmentref(String departmentref){this.departmentref=departmentref;}
}
