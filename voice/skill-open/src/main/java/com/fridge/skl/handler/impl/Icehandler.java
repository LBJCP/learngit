package com.fridge.skl.handler.impl;

import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.Food2Service;
import com.fridge.skl.service.IceService;
import com.fridge.skl.util.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fridge.skl.util.UtilConstants.Skil.DEFREEZE_REF_SKILL_ID;

@Component(DEFREEZE_REF_SKILL_ID)
public class Icehandler implements ResultHandler {
    @Autowired
    IceService IceService;

    @Override
    public ResponseObj handleResult(RequestObj requestObj) {
        String intentName = requestObj.getRequest().getIntents()[0].getIntentName();
        switch (intentName) {
            case UtilConstants.Intent.DO_DEFREEZE_INTENT_NAME:
            default:
                return IceService.defreeze(requestObj);
            case UtilConstants.Intent.DO_RESERVE_DEFREEZE_INTENT_NAME:
                return IceService.redefreeze(requestObj);
            case UtilConstants.Intent.DO_RESERVE_CLEAR_DEFREEZE_INTENT_NAME:
                return IceService.clearfreeze(requestObj);

        }


    }
}
