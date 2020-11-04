package com.fridge.skl.model.unilifemodel;

import lombok.Data;

@Data
public class RelativeFood {
    private int foodId;
    private int relativeFoodId;
    private String relativeFoodName;
    private String relativePicUrl;
    /**
     * 相克原因
     */
    private String reason;
}
