package com.fridge.skl.model;

/**
 * 匹配用户说法获得意图
 */
public class GuessIntent implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private Long timestamp;

    private Long taskId;

    private String taskName;

    private Long intentId;

    private String intentName;

    private String intentCode;

    private Slot[] slots;

    public GuessIntent(Long timestamp, Long taskId, String taskName, Long intentId, String intentName, Slot[] slots) {
        super();
        this.timestamp = timestamp;
        this.taskId = taskId;
        this.taskName = taskName;
        this.intentId = intentId;
        this.intentName = intentName;
        this.slots = slots;
    }

    public GuessIntent() {
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getIntentId() {
        return intentId;
    }

    public void setIntentId(Long intentId) {
        this.intentId = intentId;
    }

    public Slot[] getSlots() {
        return slots;
    }

    public void setSlots(Slot[] slots) {
        this.slots = slots;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getIntentName() {
        return intentName;
    }

    public void setIntentName(String intentName) {
        this.intentName = intentName;
    }

    public String getIntentCode() {
        return intentCode;
    }

    public void setIntentCode(String intentCode) {
        this.intentCode = intentCode;
    }
}
