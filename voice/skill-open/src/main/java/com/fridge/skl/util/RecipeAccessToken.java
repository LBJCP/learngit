package com.fridge.skl.util;

import lombok.Data;

import java.util.Date;

@Data
public class RecipeAccessToken {
    private String token;
    private Long validEndTime;
}
