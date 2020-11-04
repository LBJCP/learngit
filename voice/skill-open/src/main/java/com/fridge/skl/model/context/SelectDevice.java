package com.fridge.skl.model.context;

import lombok.Data;

@Data
public class SelectDevice {
    private Boolean controllable;
    private String deviceCode;
    private String deviceId;
    private String deviceName;
    private String deviceType;
    private Boolean online;
    private String platformType;
    private String productCodeT;
    private String productNameT;
    private String room;
    private String roomId;
    private String roomName;
    private String wifiType;
}
