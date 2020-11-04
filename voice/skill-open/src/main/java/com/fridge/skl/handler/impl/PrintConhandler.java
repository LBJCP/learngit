package com.fridge.skl.handler.impl;

import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.PrintContService;
import com.fridge.skl.util.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fridge.skl.util.UtilConstants.Skil.PRINT_CONTROL_SKILL_ID;

@Component(PRINT_CONTROL_SKILL_ID)
public class PrintConhandler implements ResultHandler {
    @Autowired
    PrintContService printContService;

    @Override
    public ResponseObj handleResult(RequestObj requestObj) {
        String intentName = requestObj.getRequest().getIntents()[0].getIntentName();
        switch (intentName) {
            case UtilConstants.Intent.PRINT_FOOD_INTENT_NAME:
            default:
                return printContService.printfood(requestObj);
        }
    }
}
