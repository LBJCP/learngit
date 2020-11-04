package com.fridge.skl.model.response;

import com.alibaba.fastjson.JSONArray;

/**
 * @Description TODO
 * @Date 2019/8/14 9:20
 * @Created by liupeng
 */
public class Command {
    JSONArray results;

    public JSONArray getResults() {
        return results;
    }

    public void setResults(JSONArray results) {
        this.results = results;
    }
}
