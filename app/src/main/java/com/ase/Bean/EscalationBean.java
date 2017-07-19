package com.ase.Bean;

import java.io.Serializable;

/**
 * Created by vadivel on 04/18/17.
 */
public class EscalationBean implements Serializable {
    private String tagName;
    private String tagValue;
    private String tagId;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }
}
