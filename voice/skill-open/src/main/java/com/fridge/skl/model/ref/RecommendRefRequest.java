package com.fridge.skl.model.ref;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RecommendRefRequest {
    @NotEmpty(message = "devicecode不能为空")
    private String devicecode;
    private int ttsflg;
}
