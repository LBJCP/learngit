package com.fridge.skl.handler.impl;

import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.GuessIntent;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.HearthService;
import com.fridge.skl.util.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fridge.skl.util.UtilConstants.Skil.HEALTH_MANAGER_SKILL_ID;

@Component(HEALTH_MANAGER_SKILL_ID)
public class HearthHandler implements ResultHandler {
    @Autowired
    HearthService hearthService;

    @Override
    public ResponseObj handleResult(RequestObj request) {
        String intentName = request.getRequest().getIntents()[0].getIntentName();
        if (UtilConstants.Intent.QUERY_HEL_ALL_INTENT_NAME.equals(intentName)) {

            return hearthService.doQueryHealth(request);
        } else if (UtilConstants.Intent.QUERY_HEL_DIETPLAN_INTENT_NAME.equals(intentName)) {

            return hearthService.doQueryDietPlan(request);
        } else if (UtilConstants.Intent.QUERY_HEL_INDEX_INTENT_NAME.equals(intentName)) {

            return hearthService.doQueryIndex(request);
        } else if (UtilConstants.Intent.QUERY_HEL_STEP_INTENT_NAME.equals(intentName)) {

            return hearthService.doQueryStep(request);
        }
        return new ResponseObj();
    }
}
