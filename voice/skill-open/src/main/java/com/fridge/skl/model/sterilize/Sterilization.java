package com.fridge.skl.model.sterilize;

import lombok.Data;

@Data
public class Sterilization {
    private Integer id;
    private String userId;
    private String mac;
    private Integer sterilizeType;
    private Integer subSterilizeType;
    private String createTime;
    private String sterilizeEndTime;
    private String sterilizePercent;
    private String afterEndSecond;
    private String afterStartSecond;
    private Integer status;
}
