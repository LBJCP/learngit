package com.fridge.skl.model.iot;

import lombok.Data;

@Data
public class DeviceBaseInfo {
    private String connectionStatus;
    private String deviceId;
    private String productCodeT;
    private String productNameT;
}
