package com.fridge.skl.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.handler.CallBackHandler;
import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.SmartCardService;
import com.fridge.skl.util.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fridge.skl.util.UtilConstants.Skil.STERILIZE_SMART_CARD_SKILL_ID;

@Component(STERILIZE_SMART_CARD_SKILL_ID)
public class SmartCardhandler extends BasicHandler implements ResultHandler, CallBackHandler {
    @Autowired
    SmartCardService smartCardService;

    @Override
    public ResponseObj handleResult(RequestObj requestObj) {
        switch (getIntentName(requestObj)) {
            case UtilConstants.Intent.OPEN_SMARTCARD_INTENT_NAME:
            default:
                return smartCardService.openSmartCard(requestObj);

        }
    }

    @Override
    public ResponseObj handleCallBack(RequestObj requestObj, JSONObject param, boolean iotok) {
        switch (getIntentName(requestObj)) {
            case UtilConstants.Intent.OPEN_SMARTCARD_INTENT_NAME:
            default:
                smartCardService.openSmartCardCB(requestObj, param, iotok);

        }
        return null;
    }

}
