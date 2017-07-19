
package com.ase;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoreFieldsBean implements Serializable{

    @SerializedName("requestType")
    @Expose
    private String requestType;
    @SerializedName("mimeTypes")
    @Expose
    private List<String> mimeTypes = null;
    @SerializedName("requestBys")
    @Expose
    private List<String> requestBys = null;
    private String clientId;


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }


    public String getRequestType() {
        return requestType;
    }


    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public List<String> getMimeTypes() {
        return mimeTypes;
    }

    public void setMimeTypes(List<String> mimeTypes) {
        this.mimeTypes = mimeTypes;
    }

    public List<String> getRequestBys() {
        return requestBys;
    }

    public void setRequestBys(List<String> requestBys) {
        this.requestBys = requestBys;
    }

}
