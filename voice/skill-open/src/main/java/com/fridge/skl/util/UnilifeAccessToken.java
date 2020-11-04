package com.fridge.skl.util;

import lombok.Data;

import java.util.Date;

@Data
public class UnilifeAccessToken {
    private String token;
    private Date validEndTime;
}
