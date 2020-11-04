package com.fridge.skl.handler.impl;

import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.GuessIntent;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.PregnantService;
import com.fridge.skl.util.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fridge.skl.util.UtilConstants.Skil.PREGNANT_MANAGER_SKILL_ID;

@Component(PREGNANT_MANAGER_SKILL_ID)
public class PregnantHandler implements ResultHandler {
    @Autowired
    PregnantService pregnantService;

    @Override
    public ResponseObj handleResult(RequestObj requestObj) {
        GuessIntent[] intents = requestObj.getRequest().getIntents();
        String intentName = intents[0].getIntentName();
        if (UtilConstants.Intent.INSERT_PRE_DATE_INTENT_NAME.equals(intentName)) {

            return pregnantService.doStartPregnancy(requestObj);
        } else if (UtilConstants.Intent.QUERY_PRE_DATE_INTENT_NAME.equals(intentName)) {

            return pregnantService.doSearchPregnancy(requestObj);
        } else if (UtilConstants.Intent.DELETE_PRE_DATE_INTENT_NAME.equals(intentName)) {

            return pregnantService.dodelectPregnancy(requestObj);
        }
        return new ResponseObj();
    }
}
