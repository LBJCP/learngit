package com.fridge.skl.handler.impl;

import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.SterilizeService;
import com.fridge.skl.util.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fridge.skl.util.UtilConstants.Skil.STERILIZE_REF_SKILL_ID;

@Component(STERILIZE_REF_SKILL_ID)
public class Sterilizehandler implements ResultHandler {
    @Autowired
    SterilizeService sterilizeService;

    @Override
    public ResponseObj handleResult(RequestObj requestObj) {
        String intentName = requestObj.getRequest().getIntents()[0].getIntentName();
        switch (intentName) {
            case UtilConstants.Intent.QUERY_MTER_INTENT_NAME:
            default:
                return sterilizeService.querySterilize(requestObj);
            case UtilConstants.Intent.COUNT_MTER_INTENT_NAME:
                return sterilizeService.querSterilitNum(requestObj);
            case UtilConstants.Intent.DEMO_ADDMAST_INTENT_NAME:
                return sterilizeService.addMasts(requestObj);
            case UtilConstants.Intent.DEMO_STARTMAST_INTENT_NAME:
                return sterilizeService.onShotSterilit(requestObj);
        }
    }
}
