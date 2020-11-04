package com.fridge.skl.model.unilifemodel;

import lombok.Data;

@Data
public class TogetherFood {
    private int foodId;
    private int togetherFoodId;
    private String togetherFoodName;
    private String togetherPicUrl;
    /**
     * 相克原因
     */
    private String reason;

}
