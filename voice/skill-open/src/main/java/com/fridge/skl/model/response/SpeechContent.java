package com.fridge.skl.model.response;


import java.util.Map;

/**
 * 自定义返回speech.content结构
 * 当type是start或者是end时屏端只需要取info展示就可以
 * 当type是error时代表无法解析屏端也只取info展示即可
 * 当type是continue才需要取data做具体判断
 */
public class SpeechContent {

    private String type;
    private String info;
    private Map<String,Object> data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
