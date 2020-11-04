package com.fridge.skl.model.context;

import lombok.Data;

@Data
public class AllDevices {
    String msg;
    String code;
    ExtendDevice[] data;
}
