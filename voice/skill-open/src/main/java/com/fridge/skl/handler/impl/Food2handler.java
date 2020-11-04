package com.fridge.skl.handler.impl;

import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.Food2Service;
import com.fridge.skl.util.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fridge.skl.util.UtilConstants.Skil.FOOD_MANAGER_2_SKILL_ID;

@Component(FOOD_MANAGER_2_SKILL_ID)
public class Food2handler implements ResultHandler {
    @Autowired
    Food2Service food2Service;

    @Override
    public ResponseObj handleResult(RequestObj requestObj) {
        String intentName = requestObj.getRequest().getIntents()[0].getIntentName();
        switch (intentName) {
            case UtilConstants.Intent.QUERY_SAVETIME_INTENT_NAME:
            default:
                return food2Service.getSelfLife(requestObj);
            case UtilConstants.Intent.QUERY_SAVETIME_COLD_INTENT_NAME:
                return food2Service.getColdTme(requestObj);
            case UtilConstants.Intent.QUERY_FOOD_FITUSER_INTENT_NAME:
                return food2Service.getFitUser(requestObj);
            case UtilConstants.Intent.QUERY_FOOD_AVOIDUSER_INTENT_NAME:
                return food2Service.getAvoidUser(requestObj);
            case UtilConstants.Intent.QUERY_TOGETHERFOOD_INTENT_NAME:
                return food2Service.getTogetherFood(requestObj);
            case UtilConstants.Intent.QUERY_RELATIVEFOOD_INTENT_NAME:
                return food2Service.getRelativeFood(requestObj);
            case UtilConstants.Intent.QUERY_TOWFOODFIT_INTENT_NAME:
                return food2Service.getTwoFoodFit(requestObj);
            case UtilConstants.Intent.QUERY_WAYTOSAVEFOOD_INTENT_NAME:
                return food2Service.getHowtoSave(requestObj);

        }


    }
}
