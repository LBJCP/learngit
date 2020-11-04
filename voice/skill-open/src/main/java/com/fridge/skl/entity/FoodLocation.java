package com.fridge.skl.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * food_location
 * @author 
 */
@Data
public class FoodLocation implements Serializable {
    private Integer id;

    private String foodname;

    private Integer foodid;

    private String devicetype;

    private Integer locationid;

    private String loacationname;

    private static final long serialVersionUID = 1L;
}