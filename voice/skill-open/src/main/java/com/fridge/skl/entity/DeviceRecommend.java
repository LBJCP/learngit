package com.fridge.skl.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * device_recommend
 * @author 
 */
@Data
public class DeviceRecommend implements Serializable {
    /**
     * id id
     */
    private Integer id;

    /**
     * 设备编码
     */
    private String devicecode;

    /**
     * device device
     */
    private String device;

    /**
     * recommend 描述
     */
    private String recommend;

    /**
     * 特点
     */
    private String sup;

    /**
     * 开门播报
     */
    private String opendoor1;

    private String opendoor2;

    private String opendoor3;

    private String opendoor4;

    private String opendoor5;

    private String opendoor6;

    private String opendoor7;

    private String opendoor8;

    private static final long serialVersionUID = 1L;
}