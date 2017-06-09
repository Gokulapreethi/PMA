package com.myapplication3;

/**
 * Created by Amuthan on 26/03/2016.
 */
public class CallerDetailsBean {
    private String user_name;
    private String user_id;
    private int call_id;
    private String status;
    private String presence = null;
    private String remote_uri;
    private boolean auto_redial = true;
    private int retryCallCount = 0;



    public String getPresence() {
        return presence;
    }

    public void setPresence(String presence) {
        this.presence = presence;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getCall_id() {
        return call_id;
    }

    public void setCall_id(int call_id) {
        this.call_id = call_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemote_uri() {
        return remote_uri;
    }

    public void setRemote_uri(String remote_uri) {
        this.remote_uri = remote_uri;
    }

    public boolean isAuto_redial() {
        return auto_redial;
    }

    public void setAuto_redial(boolean auto_redial) {
        this.auto_redial = auto_redial;
    }


    public int getRetryCallCount() {
        return retryCallCount;
    }

    public void setRetryCallCount(int retryCallCount) {
        this.retryCallCount = retryCallCount;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
