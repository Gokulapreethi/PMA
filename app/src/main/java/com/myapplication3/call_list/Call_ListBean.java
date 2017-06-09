package com.myapplication3.call_list;

/**
 * Created by vignesh on 6/28/2016.
 */
public class Call_ListBean {

    String type;
    String host;
    String participant;
    String start_time;
    String call_duration;
    String recording_path;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getCall_duration() {
        return call_duration;
    }

    public void setCall_duration(String call_duration) {
        this.call_duration = call_duration;
    }

    public String getRecording_path() {
        return recording_path;
    }

    public void setRecording_path(String recording_path) {
        this.recording_path = recording_path;
    }


}
