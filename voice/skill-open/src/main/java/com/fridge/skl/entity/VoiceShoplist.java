package com.fridge.skl.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * voice_shoplist
 * @author 
 */
@Data
public class VoiceShoplist implements Serializable {
    private Integer id;

    private String userid;

    private String shopitem;

    private Long time;

    /**
     * 1:删除0：未删除
     */
    private Integer delflg;

    private static final long serialVersionUID = 1L;
}