package com.fridge.skl.handler.impl;

import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.FoodService;
import com.fridge.skl.util.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fridge.skl.util.UtilConstants.Skil.FOOD_MANAGER_SKILL_ID;

@Component(FOOD_MANAGER_SKILL_ID)
public class ExpireFoodHandler implements ResultHandler {
    @Autowired
    FoodService foodService;


    @Override
    public ResponseObj handleResult(RequestObj request) {
        String intentName = request.getRequest().getIntents()[0].getIntentName();
        if (UtilConstants.Intent.QUERY_EXPIRE_FOOD_INTENT_NAME.equals(intentName)) {

            return foodService.doQueryExpiredFood(request);

        } else if (UtilConstants.Intent.DEL_EXPIRE_FOOD_INTENT_NAME.equals(intentName)) {

            return foodService.doDelExpireFood(request);

        } else if (UtilConstants.Intent.QUERY_FRIDGE_FOOD_INTENT_NAME.equals(intentName)) {
            return foodService.doQueryKgqFood(request);

        } else if (UtilConstants.Intent.QUERY_LOCATION_FOOD_INTENT_NAME.equals(intentName)) {

            return foodService.doQueryCsKgqFood(request);
        } else if (UtilConstants.Intent.QUERY_GQ_FOOD_INTENT_NAME.equals(intentName)) {

            return foodService.doQueryGqFood(request);
        } else if (UtilConstants.Intent.QUERY_CC_DATE_INTENT_NAME.equals(intentName)) {

            return foodService.doQueryRq(request);
        } else if (UtilConstants.Intent.QUERY_FOOD_TIME_INTENT_NAME.equals(intentName)) {

            return foodService.doQueryTime(request);
        }

        return null;
    }

}
