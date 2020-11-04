package com.fridge.skl.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * unlife_food
 * @author zhangn
 */
@Data
public class UnlifeFood implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 别名
     */
    private String alias;

    /**
     * 保质期
     */
    private Integer shelflife;

    /**
     * 过期提醒比例
     */
    private Float overdue;

    /**
     * 分类id
     */
    private String classifyfaceid;

    /**
     * 存储舱室
     */
    private String classifyfacename;

    /**
     * 建议存储舱室
     */
    private String storearea;

    private String storeareaadvise;

    /**
     * 建议存储温度
     */
    private Integer storec;

    /**
     * 热量
     */
    private Integer heathot;

    /**
     * 图片
     */
    private String picurl;

    /**
     * 适宜人群
     */
    private String fituser;

    /**
     * 不适宜人群
     */
    private String avoiduser;

    /**
     * 介绍
     */
    private String introducte;

    /**
     * 收藏
     */
    private String iscollection;

    /**
     * 是否有效
     */
    private String isvaild;

    /**
     * 版本号
     */
    private String updatedate;

    private String deviceid;

    private String realcatalogid;

    private String adviseautomode;

    private String advisedrymode;

    private String noadvisecabin;

    private Integer coldminday;

    private Integer coldmaxday;

    private Integer coldmintemp;

    private Integer coldmaxtemp;

    private Integer freezingminday;

    private Integer freezingmaxday;

    private Integer freezingmintemp;

    private Integer freezingmaxtemp;

    private Integer minwater;

    private Integer maxwater;

    private String picver;

    private String businesstype;

    private static final long serialVersionUID = 1L;
}