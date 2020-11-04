package com.fridge.skl.model.context;

import lombok.Data;

@Data
public class Permission {
    private Auth auth;
    private String authType;
}
