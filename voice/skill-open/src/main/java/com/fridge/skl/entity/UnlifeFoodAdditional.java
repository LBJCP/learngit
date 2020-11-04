package com.fridge.skl.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * unlife_food_additional
 * @author 
 */
@Data
public class UnlifeFoodAdditional implements Serializable {
    private Integer id;

    private Integer foodid;

    private String key;

    private String value;

    private static final long serialVersionUID = 1L;
}