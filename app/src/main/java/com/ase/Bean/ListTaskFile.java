
package com.ase.Bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListTaskFile {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("fileSource")
    @Expose
    private String fileSource;
    @SerializedName("fileSourceId")
    @Expose
    private int fileSourceId;
    @SerializedName("fileType")
    @Expose
    private String fileType;
    @SerializedName("fileName")
    @Expose
    private Object fileName;
    @SerializedName("fileStatus")
    @Expose
    private String fileStatus;
    @SerializedName("dateOfFile")
    @Expose
    private long dateOfFile;
    @SerializedName("fileContent")
    @Expose
    private Object fileContent;
    @SerializedName("taskFileExt")
    @Expose
    private Object taskFileExt;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("descType")
    @Expose
    private Object descType;
    @SerializedName("file")
    @Expose
    private Object file;

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
     *     The user
     */
    public User getUser() {
        return user;
    }

    /**
     * 
     * @param user
     *     The user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * 
     * @return
     *     The fileSource
     */
    public String getFileSource() {
        return fileSource;
    }

    /**
     * 
     * @param fileSource
     *     The fileSource
     */
    public void setFileSource(String fileSource) {
        this.fileSource = fileSource;
    }

    /**
     * 
     * @return
     *     The fileSourceId
     */
    public int getFileSourceId() {
        return fileSourceId;
    }

    /**
     * 
     * @param fileSourceId
     *     The fileSourceId
     */
    public void setFileSourceId(int fileSourceId) {
        this.fileSourceId = fileSourceId;
    }

    /**
     * 
     * @return
     *     The fileType
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * 
     * @param fileType
     *     The fileType
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * 
     * @return
     *     The fileName
     */
    public Object getFileName() {
        return fileName;
    }

    /**
     * 
     * @param fileName
     *     The fileName
     */
    public void setFileName(Object fileName) {
        this.fileName = fileName;
    }

    /**
     * 
     * @return
     *     The fileStatus
     */
    public String getFileStatus() {
        return fileStatus;
    }

    /**
     * 
     * @param fileStatus
     *     The fileStatus
     */
    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    /**
     * 
     * @return
     *     The dateOfFile
     */
    public long getDateOfFile() {
        return dateOfFile;
    }

    /**
     * 
     * @param dateOfFile
     *     The dateOfFile
     */
    public void setDateOfFile(long dateOfFile) {
        this.dateOfFile = dateOfFile;
    }

    /**
     * 
     * @return
     *     The fileContent
     */
    public Object getFileContent() {
        return fileContent;
    }

    /**
     * 
     * @param fileContent
     *     The fileContent
     */
    public void setFileContent(Object fileContent) {
        this.fileContent = fileContent;
    }

    /**
     * 
     * @return
     *     The taskFileExt
     */
    public Object getTaskFileExt() {
        return taskFileExt;
    }

    /**
     * 
     * @param taskFileExt
     *     The taskFileExt
     */
    public void setTaskFileExt(Object taskFileExt) {
        this.taskFileExt = taskFileExt;
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
     *     The descType
     */
    public Object getDescType() {
        return descType;
    }

    /**
     * 
     * @param descType
     *     The descType
     */
    public void setDescType(Object descType) {
        this.descType = descType;
    }

    /**
     * 
     * @return
     *     The file
     */
    public Object getFile() {
        return file;
    }

    /**
     * 
     * @param file
     *     The file
     */
    public void setFile(Object file) {
        this.file = file;
    }

}
