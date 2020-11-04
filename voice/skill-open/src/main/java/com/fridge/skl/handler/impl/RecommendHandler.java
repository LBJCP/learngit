package com.fridge.skl.handler.impl;

import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.GuessIntent;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.RecommendService;
import com.fridge.skl.util.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fridge.skl.util.UtilConstants.Skil.RECOMMAND_DEVICE_SKILL_ID;

@Component(RECOMMAND_DEVICE_SKILL_ID)
public class RecommendHandler implements ResultHandler {
    @Autowired
    RecommendService recommendService;

    @Override
    public ResponseObj handleResult(RequestObj requestObj) {
        GuessIntent[] intents = requestObj.getRequest().getIntents();
        String intentName = intents[0].getIntentName();
        if (UtilConstants.Intent.RECOM_SUP_REF_INTENT_NAME.equals(intentName)) {
            return recommendService.recommdRefSup(requestObj);
        }
        return new ResponseObj();
    }
}
