package com.fridge.skl.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * unlife_togetherfood
 * @author 
 */
@Data
public class UnlifeTogetherfood implements Serializable {
    private Integer id;

    private Integer foodid;

    private Integer togetherfoodid;

    private String togetherfoodname;

    private String togetherpicurl;

    private String reason;

    private static final long serialVersionUID = 1L;
}