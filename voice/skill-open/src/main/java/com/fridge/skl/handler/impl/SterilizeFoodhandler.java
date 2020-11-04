package com.fridge.skl.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.handler.CallBackHandler;
import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.SterilizeService;
import com.fridge.skl.util.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fridge.skl.util.UtilConstants.Skil.STERILIZE_FOOD_REF_SKILL_ID;

@Component(STERILIZE_FOOD_REF_SKILL_ID)
public class SterilizeFoodhandler implements ResultHandler, CallBackHandler {
    @Autowired
    SterilizeService sterilizeService;

    @Override
    public ResponseObj handleResult(RequestObj requestObj) {
        String intentName = requestObj.getRequest().getIntents()[0].getIntentName();
        switch (intentName) {
            case UtilConstants.Intent.DEMO_ADDMAST_INTENT_NAME:
            default:
                return sterilizeService.addMasts(requestObj);

        }
    }

    @Override
    public ResponseObj handleCallBack(RequestObj requestObj, JSONObject param, boolean iotok) {
        String intentName = requestObj.getRequest().getIntents()[0].getIntentName();
        switch (intentName) {
            case UtilConstants.Intent.DEMO_ADDMAST_INTENT_NAME:
            default:
                sterilizeService.addMastsCB(requestObj, iotok);

        }
        return null;
    }
}
