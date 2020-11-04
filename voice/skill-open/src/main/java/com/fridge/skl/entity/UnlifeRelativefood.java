package com.fridge.skl.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * unlife_relativefood
 * @author 
 */
@Data
public class UnlifeRelativefood implements Serializable {
    private Integer id;

    private Integer foodid;

    private Integer relativefoodid;

    private String relativefoodname;

    private String relativepicurl;

    private String reason;

    private static final long serialVersionUID = 1L;
}