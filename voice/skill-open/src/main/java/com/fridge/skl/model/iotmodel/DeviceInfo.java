package com.fridge.skl.model.iotmodel;

import lombok.Data;

import java.util.List;

@Data
public class DeviceInfo {
    private String deviceName;
    private String deviceId;
    private String wifiType;
    private String deviceType;
    private String productCodeT;
    private String productNameT;
    private boolean online;
    private AuthInfo totalPermission;
    private List<Permission> Permission;
}
