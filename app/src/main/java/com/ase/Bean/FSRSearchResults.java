package com.ase.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Preethi on 8/19/2017.
 */

public class FSRSearchResults implements Serializable {
    @SerializedName("taskcompleteddates")
    @Expose
    private List<String> taskcompleteddates = null;
    @SerializedName("oracleProjectId")
    @Expose
    private String oracleProjectId;

    private String projectId;

    public List<String> getTaskcompleteddates() {
        return taskcompleteddates;
    }

    public void setTaskcompleteddates(List<String> taskcompleteddates) {
        this.taskcompleteddates = taskcompleteddates;
    }

    public String getOracleProjectId() {
        return oracleProjectId;
    }

    public void setOracleProjectId(String oracleProjectId) {
        this.oracleProjectId = oracleProjectId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
