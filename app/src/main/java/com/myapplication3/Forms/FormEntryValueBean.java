
package com.myapplication3.Forms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FormEntryValueBean implements Serializable{

    @SerializedName("setId")
    @Expose
    private Integer setId;
    @SerializedName("list")
    @Expose
    private java.util.List<com.myapplication3.Forms.List> list = null;

    public Integer getSetId() {
        return setId;
    }

    public void setSetId(Integer setId) {
        this.setId = setId;
    }

    public java.util.List<com.myapplication3.Forms.List> getList() {
        return list;
    }

    public void setList(java.util.List<com.myapplication3.Forms.List> list) {
        this.list = list;
    }

}

