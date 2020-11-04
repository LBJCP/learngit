package com.fridge.skl.handler.impl;

import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.MaquillageManagerService;
import com.fridge.skl.util.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fridge.skl.util.UtilConstants.Skil.MAQ_MANAGER_SKILL_ID;

@Component(MAQ_MANAGER_SKILL_ID)
public class MaquillageManagerhandler implements ResultHandler {
    @Autowired
    MaquillageManagerService maquillageManagerService;

    @Override
    public ResponseObj handleResult(RequestObj requestObj) {
        String intentName = requestObj.getRequest().getIntents()[0].getIntentName();
        switch (intentName) {
            case UtilConstants.Intent.ADD_MAQ_INTENT_NAME:
            default:
                return maquillageManagerService.addmaq(requestObj);
            case UtilConstants.Intent.DEL_MAQ_INTENT_NAME:
                return maquillageManagerService.delmaq(requestObj);
            case UtilConstants.Intent.QUERY_MAQ_INTENT_NAME:
                return maquillageManagerService.querymaq(requestObj);
        }
    }
}
