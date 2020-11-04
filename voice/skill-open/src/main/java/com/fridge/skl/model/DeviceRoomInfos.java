package com.fridge.skl.model;

import lombok.Data;

@Data
public class DeviceRoomInfos {
    private Boolean controllable;
    private String deviceCode;
    private String deviceId;
    private String deviceName;
    private String deviceType;
    private Boolean online;
    private DevicePermission permission;
    private String platformType;
    private String room;
    private String wifiType;
}
