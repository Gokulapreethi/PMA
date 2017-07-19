package com.ase.Bean;

import org.pjsip.pjsua2.app.GroupMemberAccess;

/**
 * Created by prasanth on 2/22/2017.
 */
public class SipNotification {

    private String source;

    private Long source_id;

    private Long from_id;

    private Long to_id;

    private String signal_id;

    private String message;

    private String alert_type;

    private String alert_sub_type;

    private String notify_status;

    private String updated_date;

    private String group_name;

    private String member_added;

    private String ownerOfProject;

    private GroupMemberAccess groupMemberAccess;

    private ProjectDetailsBean projectDetailsBean;

    private TaskDetailsBean taskDetailsBean;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getSource_id() {
        return source_id;
    }

    public void setSource_id(Long source_id) {
        this.source_id = source_id;
    }

    public Long getFrom_id() {
        return from_id;
    }

    public void setFrom_id(Long from_id) {
        this.from_id = from_id;
    }

    public Long getTo_id() {
        return to_id;
    }

    public void setTo_id(Long to_id) {
        this.to_id = to_id;
    }

    public String getSignal_id() {
        return signal_id;
    }

    public void setSignal_id(String signal_id) {
        this.signal_id = signal_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAlert_type() {
        return alert_type;
    }

    public void setAlert_type(String alert_type) {
        this.alert_type = alert_type;
    }

    public String getAlert_sub_type() {
        return alert_sub_type;
    }

    public void setAlert_sub_type(String alert_sub_type) {
        this.alert_sub_type = alert_sub_type;
    }

    public String getNotify_status() {
        return notify_status;
    }

    public void setNotify_status(String notify_status) {
        this.notify_status = notify_status;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getMember_added() {
        return member_added;
    }

    public void setMember_added(String member_added) {
        this.member_added = member_added;
    }

    public GroupMemberAccess getGroupMemberAccess() {
        return groupMemberAccess;
    }

    public void setGroupMemberAccess(GroupMemberAccess groupMemberAccess) {
        this.groupMemberAccess = groupMemberAccess;
    }

    public String getOwnerOfProject() {
        return ownerOfProject;
    }

    public void setOwnerOfProject(String ownerOfProject) {
        this.ownerOfProject = ownerOfProject;
    }

    public ProjectDetailsBean getProjectDetailsBean() {
        return projectDetailsBean;
    }

    public void setProjectDetailsBean(ProjectDetailsBean projectDetailsBean) {
        this.projectDetailsBean = projectDetailsBean;
    }

    public TaskDetailsBean getTaskDetailsBean() {
        return taskDetailsBean;
    }

    public void setTaskDetailsBean(TaskDetailsBean taskDetailsBean) {
        this.taskDetailsBean = taskDetailsBean;
    }
}
