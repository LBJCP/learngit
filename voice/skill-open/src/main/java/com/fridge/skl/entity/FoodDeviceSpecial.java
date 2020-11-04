package com.fridge.skl.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * food_device_special
 * @author 
 */
@Data
public class FoodDeviceSpecial implements Serializable {
    private Integer id;

    private Integer foodid;

    private String typeid;

    private String refdesc;

    private static final long serialVersionUID = 1L;
}