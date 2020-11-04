package com.fridge.skl.model.slots;

import lombok.Data;

@Data
public class Time {
    private Integer date;
    private Integer month;
    private Integer hour;
    private Integer minute;
    private Integer second;
    private Integer year;
    private Integer intervalDay;
    private Integer nextMonth;
    private String timemark;
    private String timeQuantum;
}
