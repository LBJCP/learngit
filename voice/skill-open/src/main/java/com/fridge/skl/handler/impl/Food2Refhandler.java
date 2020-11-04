package com.fridge.skl.handler.impl;

import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.Food2Service;
import com.fridge.skl.util.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fridge.skl.util.UtilConstants.Skil.FOOD_MANAGER_2_REF_SKILL_ID;

@Component(FOOD_MANAGER_2_REF_SKILL_ID)
public class Food2Refhandler implements ResultHandler {
    @Autowired
    Food2Service food2Service;

    @Override
    public ResponseObj handleResult(RequestObj requestObj) {
        String intentName = requestObj.getRequest().getIntents()[0].getIntentName();
        switch (intentName) {
            case UtilConstants.Intent.QUERY_FOODSAVEDTIME_INTENT_NAME:
            default:
                return food2Service.queryFoodSavedTime(requestObj);
            case UtilConstants.Intent.QUERY_ISFOODEXPIRE_INTENT_NAME:
                return food2Service.queryFoodExpire(requestObj);
            case UtilConstants.Intent.QUERY_QUERYFOODSAVEINDATE_INTENT_NAME:
                return food2Service.queryInputDay(requestObj);
            case UtilConstants.Intent.QUERY_QUERYFOODCATEGORY_INTENT_NAME:
                return food2Service.queryFoodCategory(requestObj);

            case UtilConstants.Intent.QUERY_FOOD_LOCATION_INTENT_NAME:
                return food2Service.queryFoodLocation(requestObj);
            case UtilConstants.Intent.QUERY_FOOD_EXPIRETIME_INTENT_NAME:
                return food2Service.queryFoodExpireTime(requestObj);

        }


    }
}
