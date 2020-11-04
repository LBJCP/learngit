package com.fridge.skl.entity;

import java.io.Serializable;
import java.util.Date;

public class Pregnant implements Serializable {
    private String id;
    private String userid;
    private Date starttime;
    private Date pretime;
    private Date time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getPretime() {
        return pretime;
    }

    public void setPretime(Date pretime) {
        this.pretime = pretime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
