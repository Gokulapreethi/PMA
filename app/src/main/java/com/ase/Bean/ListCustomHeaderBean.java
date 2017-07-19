
package com.ase.Bean;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vignesh on 12/1/2016.
 */

public class ListCustomHeaderBean {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("tagName")
    @Expose
    private String tagName;
    @SerializedName("dataType")
    @Expose
    private String dataType;
    @SerializedName("isInputRequired")
    @Expose
    private String isInputRequired;
    @SerializedName("createdBy")
    @Expose
    private Object createdBy;
    @SerializedName("createdDate")
    @Expose
    private Object createdDate;
    @SerializedName("updatedBy")
    @Expose
    private Object updatedBy;
    @SerializedName("updatedDate")
    @Expose
    private Object updatedDate;

    /**
     * 
     * @return
     *     The id
     */
    public int getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The tagName
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * 
     * @param tagName
     *     The tagName
     */
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    /**
     * 
     * @return
     *     The dataType
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * 
     * @param dataType
     *     The dataType
     */
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    /**
     * 
     * @return
     *     The isInputRequired
     */
    public String getIsInputRequired() {
        return isInputRequired;
    }

    /**
     * 
     * @param isInputRequired
     *     The isInputRequired
     */
    public void setIsInputRequired(String isInputRequired) {
        this.isInputRequired = isInputRequired;
    }

    /**
     * 
     * @return
     *     The createdBy
     */
    public Object getCreatedBy() {
        return createdBy;
    }

    /**
     * 
     * @param createdBy
     *     The createdBy
     */
    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * 
     * @return
     *     The createdDate
     */
    public Object getCreatedDate() {
        return createdDate;
    }

    /**
     * 
     * @param createdDate
     *     The createdDate
     */
    public void setCreatedDate(Object createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * 
     * @return
     *     The updatedBy
     */
    public Object getUpdatedBy() {
        return updatedBy;
    }

    /**
     * 
     * @param updatedBy
     *     The updatedBy
     */
    public void setUpdatedBy(Object updatedBy) {
        this.updatedBy = updatedBy;
    }

    /**
     * 
     * @return
     *     The updatedDate
     */
    public Object getUpdatedDate() {
        return updatedDate;
    }

    /**
     * 
     * @param updatedDate
     *     The updatedDate
     */
    public void setUpdatedDate(Object updatedDate) {
        this.updatedDate = updatedDate;
    }

}
