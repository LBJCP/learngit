package com.fridge.skl.handler.impl;

import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.FreezerFoodManageService;

import com.fridge.skl.util.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fridge.skl.util.UtilConstants.Skil.FREEZER_FOOD_MANAGE_SKILL_ID;

@Component(FREEZER_FOOD_MANAGE_SKILL_ID)
public class FreezerFoodManageHandler implements ResultHandler {

    @Autowired
    FreezerFoodManageService freezerFoodManageService;

    @Override
    public ResponseObj handleResult(RequestObj requestObj) {
        String intentName = requestObj.getRequest().getIntents()[0].getIntentName();
        if(UtilConstants.Intent.ADD_FOOD_INTENT_NAME_WITH_TIER.equals(intentName)){
            return freezerFoodManageService.addFoodWithTier(requestObj);
        }
        else if(UtilConstants.Intent.REMOVE_FOOD_INTENT_NAME_WITH_TIER.equals(intentName)){
            return freezerFoodManageService.removeFoodWithTier(requestObj);
        }
        else if(UtilConstants.Intent.QUERY_FOOD_INTENT_NAME.equals(intentName)){
            return freezerFoodManageService.queryFood(requestObj);
        }
        return new ResponseObj();
    }

}
