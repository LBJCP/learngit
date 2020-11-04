package com.fridge.skl.model.coffee;

import lombok.Data;

@Data
public class CoffeeStyle {
    private Long id;
    private String deviceCode;
    private String deviceModel;
    private String typeId;
    private Long coffeeTypeId;
    private String coffeeTypeName;
    private Integer bakingDegree;
    private Integer coffeeRatio;
    private Integer waterRatio;
    private Double temperature;
    private Double hour;
    private Double bestDrinkHour;
    private Double reminderHour;
    private String extractStartTime;
    private String extractCompleteTime;
    private String bestDrinkStartTime;
    private String bestDrinkEndTime;
    private String willExpireReminderTime;
    private String expireReminderTime;
    private Integer extractStatus;
    private Integer drinkStatus;
    private Integer showStatus;
    private Long timeToCompleteSecond;
    private Long timeToWillExpireReminderSecond;
    private Long timeToExpireReminderSecond;
    private String currentTime;
    private String prevCmdInfo;
    private String nowCmdInfo;
}
