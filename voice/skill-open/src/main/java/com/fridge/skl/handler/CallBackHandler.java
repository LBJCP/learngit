package com.fridge.skl.handler;

import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;

public interface CallBackHandler {
    public ResponseObj handleCallBack(RequestObj requestObj, JSONObject param, boolean isiotok);
}
