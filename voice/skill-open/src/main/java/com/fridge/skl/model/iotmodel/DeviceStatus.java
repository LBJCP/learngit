package com.fridge.skl.model.iotmodel;

import lombok.Data;

import java.util.Map;

@Data
public class DeviceStatus {
    private Long timestamp;
    private String deviceId;
    private Map<String, String> statuses;
}
