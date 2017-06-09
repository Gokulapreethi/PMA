package com.myapplication3.Bean;

import java.util.ArrayList;

/**
 * Created by vignesh on 11/28/2016.
 */
public class CustomBean {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ListofFileds getMaster() {
        return master;
    }

    public void setMaster(ListofFileds master) {
        this.master = master;
    }

    public ListofAttribute getTask() {
        return task;
    }

    public void setTask(ListofAttribute task) {
        this.task = task;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    String id;
    ListofFileds master;
    ListofAttribute task;
    String value;




}
