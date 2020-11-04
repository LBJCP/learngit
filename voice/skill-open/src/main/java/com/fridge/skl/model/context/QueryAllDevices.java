package com.fridge.skl.model.context;

import lombok.Data;

@Data
public class QueryAllDevices {
    private String msg;
    private String code;
    private SelectDevice data;
}
