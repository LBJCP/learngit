package com.fridge.skl.model.recipe;

import lombok.Data;

import java.util.List;

@Data
public class SmartRecipeFuzzySearchResponse {
    private Integer currentPage;
    private List<String> equipmentType;
    private String fridgeType;
    private List<String> kw;
    private String macId;
    private Integer pageSize;
    private String userId;
}
