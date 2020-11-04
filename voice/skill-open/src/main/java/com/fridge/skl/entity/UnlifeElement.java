package com.fridge.skl.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * unlife_element
 * @author zhangn
 */
@Data
public class UnlifeElement implements Serializable {
    private Integer id;

    private Integer foodid;

    private String elementname;

    private String elementcount;

    private static final long serialVersionUID = 1L;
}