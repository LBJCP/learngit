package com.fridge.skl.handler.impl;

import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.Food2Service;
import com.fridge.skl.service.RefControlService;
import com.fridge.skl.util.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fridge.skl.util.UtilConstants.Skil.REF_SYSTEMCTRL_SOREF_SKILL_ID;

@Component(REF_SYSTEMCTRL_SOREF_SKILL_ID)
public class RefControlRefhandler implements ResultHandler {
    @Autowired
    RefControlService refControlService;

    @Override
    public ResponseObj handleResult(RequestObj requestObj) {
        String intentName = requestObj.getRequest().getIntents()[0].getIntentName();
        switch (intentName) {
            case UtilConstants.Intent.DO_OPENREFREENDOOR_INTENT_NAME:
            default:
                return refControlService.opendoor(requestObj);
        }
    }
}
