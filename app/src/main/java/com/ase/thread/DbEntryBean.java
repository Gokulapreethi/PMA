package com.ase.thread;

/**
 * Created by Amuthan on 01/04/2017.
 */

public class DbEntryBean {
    // String FromUri, String toUri, String mimeType, String message
    private String FromUri;
    private String toUri;
    private String mimeType;
    private String message;
    private MessageAccess call_back;

    public String getFromUri() {
        return FromUri;
    }

    public void setFromUri(String fromUri) {
        FromUri = fromUri;
    }

    public String getToUri() {
        return toUri;
    }

    public void setToUri(String toUri) {
        this.toUri = toUri;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageAccess getCall_back() {
        return call_back;
    }

    public void setCall_back(MessageAccess call_back) {
        this.call_back = call_back;
    }
}
