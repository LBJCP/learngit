package com.fridge.skl.handler;

import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;

public interface ResultHandler {
    //获取各种相应的信息，例如版本号等基础信息等
    public ResponseObj handleResult(RequestObj requestObj);
}
