package com.myapplication3.Bean;

import json.ListFromDetails;

/**
 * Created by thirumal on 26-09-2016.
 */
public class tamplatetask {
    private ListFromDetails from;
    private ListFromDetails toUser;
    private String name;
    private String taskNo;
    private String status;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ListFromDetails getToUser() {
        return toUser;
    }

    public void setToUser(ListFromDetails toUser) {
        this.toUser = toUser;
    }

    public ListFromDetails getFrom() {
        return from;
    }

    public void setFrom(ListFromDetails from) {
        this.from = from;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
