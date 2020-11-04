package com.fridge.skl.model.iot;

import lombok.Data;

@Data
public class IotCallBack {
    private String deviceId;
    private String resCode;
    private String result;
    private String retCode;
    private String retInfo;
    private String usn;
}
