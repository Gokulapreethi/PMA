package com.ase.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Preethi on 8/19/2017.
 */

public class FSRResultBean implements Serializable{
    @SerializedName("result_code")
    @Expose
    private Integer resultCode;
    @SerializedName("result_text")
    @Expose
    private String resultText;
    @SerializedName("listProject")
    @Expose
    private List<FSRSearchResults> listProject = null;

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultText() {
        return resultText;
    }

    public void setResultText(String resultText) {
        this.resultText = resultText;
    }

    public List<FSRSearchResults> getListProject() {
        return listProject;
    }

    public void setListProject(List<FSRSearchResults> listProject) {
        this.listProject = listProject;
    }
}

