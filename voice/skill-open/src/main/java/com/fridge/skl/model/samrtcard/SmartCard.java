package com.fridge.skl.model.samrtcard;

import lombok.Data;

@Data
public class SmartCard {
    private long id;
    private String mac;
    private String model;
    private String typeId;
    private String cabin;
    private String type;
    private int remind;
    private String name;
    private String img;
    private String linkUrl;
    private String cmd;
    private String keepTime;
    private int keepTimeUnit;
    private String remainTime;
    private String temperature;
    private String openTime;
    private int openStatus;
    private String select;
    private String remark;

}
