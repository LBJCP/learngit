package com.fridge.skl.model.iotmodel;

import lombok.Data;

@Data
public class AuthInfo {
    /**
    * 是否有查看权限
    * */
    private boolean view;
    /**
     * 是否有配置权限
     * */
    private boolean set;
    /**
     * 是否有控制权限
     * */
    private boolean control;
}
