package com.fridge.skl.model.context;

import lombok.Data;

@Data
public class Device implements java.io.Serializable {


    private static final long serialVersionUID = 1L;

    private String deviceId;
    private String accessToken;
    private String clientId;
    private String deviceType;
    private String userCenterToken;


}
