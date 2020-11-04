package com.fridge.skl.entity;

import com.fridge.skl.config.DeviceType;

import java.io.Serializable;
import java.util.Date;

public class Feedback implements Serializable {
    private String id;
    private String userid;
    private String deviceid;
    private String content;
    private String teiminal;
    private Date time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTeiminal() {
        return teiminal;
    }

    public void setTeiminal(String teiminal) {
        this.teiminal = teiminal;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }
}
