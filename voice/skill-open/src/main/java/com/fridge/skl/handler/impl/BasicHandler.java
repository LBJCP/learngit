package com.fridge.skl.handler.impl;

import com.fridge.skl.model.RequestObj;

public class BasicHandler {
    protected String getIntentName(RequestObj requestObj) {
        return requestObj.getRequest().getIntents()[0].getIntentName();
    }
}
