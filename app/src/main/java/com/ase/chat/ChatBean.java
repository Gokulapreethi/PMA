package com.ase.chat;

/**
 * Created by balamurugan on 4/29/2016.
 */
public class ChatBean implements Cloneable {
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private String fromUser;

    private String toname;

    private String message;

    private String chatid;

    private String chatname;

    private String signalid;

    private String msgtype;

    private String datetime;

    private String username;

    private String type;

    private String chatmembers;

    private int msg_status = 0;

    private String path;

    private String file_name;

    private String multimediaURL;

    private String chathistory_name;

    private String opened;

    private String scheduled;

    private String count;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


    public String getScheduled() {
        return scheduled;
    }

    public void setScheduled(String scheduled) {
        this.scheduled = scheduled;
    }




    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatid() {
        return chatid;
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    public String getChatname() {
        return chatname;
    }

    public void setChatname(String chatname) {
        this.chatname = chatname;
    }

    public String getSignalid() {
        return signalid;
    }

    public void setSignalid(String signalid) {
        this.signalid = signalid;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToname() {
        return toname;
    }

    public void setToname(String toname) {
        this.toname = toname;
    }

    public String getChatmembers() {
        return chatmembers;
    }

    public void setChatmembers(String chatmembers) {
        this.chatmembers = chatmembers;
    }

    public int getMsg_status() {
        return msg_status;
    }

    public void setMsg_status(int msg_status) {
        this.msg_status = msg_status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getMultimediaURL() {
        return multimediaURL;
    }

    public void setMultimediaURL(String multimediaURL) {
        this.multimediaURL = multimediaURL;
    }

    public String getChathistory_name() {
        return chathistory_name;
    }

    public String getOpened() {
        return opened;
    }

    public void setOpened(String opened) {
        this.opened = opened;
    }

    public void setChathistory_name(String chathistory_name) {
        this.chathistory_name = chathistory_name;

    }
}
