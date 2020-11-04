package com.fridge.skl.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * requestlog
 * @author 
 */
@Data
public class Requestlog implements Serializable {
    private Integer id;

    private String sn;

    private String userid;

    private String local;

    private String deviceid;

    private String devicetype;

    private String accesstoken;

    private String clientid;

    private String timestamp;

    private String sessionid;

    private String taskid;

    private String taskname;

    private String intentid;

    private String intentname;

    private String intentcode;

    private String slots;

    private String content;

    private String response;

    private static final long serialVersionUID = 1L;
}