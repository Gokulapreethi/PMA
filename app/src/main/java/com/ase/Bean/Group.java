
package com.ase.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Group {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("groupName")
    @Expose
    private String groupName;
    @SerializedName("groupOwner")
    @Expose
    private Object groupOwner;
    @SerializedName("organisation")
    @Expose
    private Object organisation;
    @SerializedName("departmentId")
    @Expose
    private Object departmentId;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("logo")
    @Expose
    private Object logo;
    @SerializedName("createdBy")
    @Expose
    private Object createdBy;
    @SerializedName("createdDate")
    @Expose
    private Object createdDate;
    @SerializedName("updatedBy")
    @Expose
    private Object updatedBy;
   /* @SerializedName("updatedDate")
    @Expose
    private long updatedDate;*/
    @SerializedName("groupLogo")
    @Expose
    private Object groupLogo;
    @SerializedName("organisations")
    @Expose
    private Object organisations;
    @SerializedName("departmentref")
    @Expose
    private Object departmentref;
    @SerializedName("listMember")
    @Expose
    private List<ListMembers> listMember = null;
    @SerializedName("deleteGroup")
    @Expose
    private Object deleteGroup;

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
     *     The groupName
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * 
     * @param groupName
     *     The groupName
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * 
     * @return
     *     The groupOwner
     */
    public Object getGroupOwner() {
        return groupOwner;
    }

    /**
     * 
     * @param groupOwner
     *     The groupOwner
     */
    public void setGroupOwner(Object groupOwner) {
        this.groupOwner = groupOwner;
    }

    /**
     * 
     * @return
     *     The organisation
     */
    public Object getOrganisation() {
        return organisation;
    }

    /**
     * 
     * @param organisation
     *     The organisation
     */
    public void setOrganisation(Object organisation) {
        this.organisation = organisation;
    }

    /**
     * 
     * @return
     *     The departmentId
     */
    public Object getDepartmentId() {
        return departmentId;
    }

    /**
     * 
     * @param departmentId
     *     The departmentId
     */
    public void setDepartmentId(Object departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The logo
     */
    public Object getLogo() {
        return logo;
    }

    /**
     * 
     * @param logo
     *     The logo
     */
    public void setLogo(Object logo) {
        this.logo = logo;
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

   /* *//**
     * 
     * @return
     *     The updatedDate
     *//*
    public long getUpdatedDate() {
        return updatedDate;
    }

    *//**
     * 
     * @param updatedDate
     *     The updatedDate
     *//*
    public void setUpdatedDate(long updatedDate) {
        this.updatedDate = updatedDate;
    }*/

    /**
     * 
     * @return
     *     The groupLogo
     */
    public Object getGroupLogo() {
        return groupLogo;
    }

    /**
     * 
     * @param groupLogo
     *     The groupLogo
     */
    public void setGroupLogo(Object groupLogo) {
        this.groupLogo = groupLogo;
    }

    /**
     * 
     * @return
     *     The organisations
     */
    public Object getOrganisations() {
        return organisations;
    }

    /**
     * 
     * @param organisations
     *     The organisations
     */
    public void setOrganisations(Object organisations) {
        this.organisations = organisations;
    }

    /**
     * 
     * @return
     *     The departmentref
     */
    public Object getDepartmentref() {
        return departmentref;
    }

    /**
     * 
     * @param departmentref
     *     The departmentref
     */
    public void setDepartmentref(Object departmentref) {
        this.departmentref = departmentref;
    }

   /* *//**
     * 
     * @return
     *     The listMember
     *//*
    public List<ListMember> getListMember() {
        return listMember;
    }

    *//**
     * 
     * @param listMember
     *     The listMember
     *//*
    public void setListMember(List<ListMember> listMember) {
        this.listMember = listMember;
    }*/

    /**
     * 
     * @return
     *     The deleteGroup
     */
    public Object getDeleteGroup() {
        return deleteGroup;
    }

    /**
     * 
     * @param deleteGroup
     *     The deleteGroup
     */
    public void setDeleteGroup(Object deleteGroup) {
        this.deleteGroup = deleteGroup;
    }

    public List<ListMembers> getListMember() {
        return listMember;
    }

    public void setListMember(List<ListMembers> listMember) {
        this.listMember = listMember;
    }
}
