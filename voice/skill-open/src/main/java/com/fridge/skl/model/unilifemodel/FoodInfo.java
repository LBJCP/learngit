package com.fridge.skl.model.unilifemodel;

import lombok.Data;

@Data
public class FoodInfo {
    private String Id;
    private String deviceId;
    private String name;
    private int open;
    private Long createTime;
    private int shelfLife;
    private String imgUrl;
    private String efficacy;
    private String desc;
    private String location;
    private String dateOfProduct;
}
