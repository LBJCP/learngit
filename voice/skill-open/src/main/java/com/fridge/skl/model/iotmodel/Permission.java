package com.fridge.skl.model.iotmodel;

import lombok.Data;

@Data
public class Permission {
    /**
     * 权限内容
     */
    private AuthInfo auth;
    /**
     * 权限类型	home：家庭分享
     * share：个人分享
     * owener：设备主人
     * server：给appserver的权限
     */
    private String authType;

}
