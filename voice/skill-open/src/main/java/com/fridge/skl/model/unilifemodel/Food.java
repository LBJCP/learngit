package com.fridge.skl.model.unilifemodel;

import lombok.Data;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * 食材列表
 */
@Data
public class Food implements Serializable {
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
    private Integer shelfLife;
    /**
     * 过期提醒比例
     */
    private float overdue;
    /**
     * 分类id
     */
    private String classifyFaceId;
    private String classifyFaceName;
    /**
     * 存储舱室
     */
    private String storeArea;
    /**
     * 建议存储舱室
     */
    private String storeAreaAdvise;
    /**
     * 建议存储温度
     */
    private Integer storeC;
    /**
     * 热量
     */
    private Integer heatHot;
    /**
     * 图片
     */
    private String picUrl;
    /**
     * 适宜人群
     */
    private String fitUser;
    /**
     * 不适宜人群
     */
    private String avoidUser;
    /**
     * 介绍
     */
    private String introducte;
    /**
     * 收藏
     */
    private String isCollection;
    /**
     * 含有元素
     */
    private List<Element> element;
    /**
     * 相克食物
     */
    private List<RelativeFood> relativeFood;
    /**
     * 搭配食物
     */
    private List<TogetherFood> togetherFood;
    /**
     * 是否有效（0无效，1有效）
     */
    private String isVaild;
    /**
     * 版本号
     */
    private String updateDate;
    /***/
    private String deviceId;
    /***/
    private String realCatalogId;
    /***/
    private String adviseAutoMode;
    /***/
    private String adviseDryMode;
    /**
     * 禁止存放舱室
     */
    private String noAdviseCabin;
    /**
     * 冷藏最少时间
     */
    private Integer coldMinDay;
    /**
     * 冷藏最长时间
     */
    private Integer coldMaxDay;
    /**
     * 冷藏最低温度
     */
    private Integer coldMinTemp;
    /**
     * 冷藏最高温度
     */
    private Integer coldMaxTemp;
    /**
     * 冷冻最短时间
     */
    private Integer freezingMinDay;
    /**
     * 冷冻最长时间
     */
    private Integer freezingMaxDay;
    /**
     * 冷冻最低温度
     */
    private Integer freezingMinTemp;
    /**
     * 冷冻最高温度
     */
    private Integer freezingMaxTemp;
    /**
     * 最低含水量
     */
    private Integer minWater;
    /**
     * 最高含水量
     */
    private Integer maxWater;
    /**
     * 附加
     */
    private List<Additional> additional;
    /***/
    private String picVer;
    /***/
    private String businessType;
}
