package com.fridge.skl.model.unilifemodel;

import lombok.Data;

import java.util.List;

@Data
public class FoodListResponse {

    private String errorno;
    private String errormsg;
    private int total;
    private List<Food> data;
    private String version;

}
