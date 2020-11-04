package com.fridge.skl.handler.impl;

import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.CounterService;
import com.fridge.skl.service.NoteService;
import com.fridge.skl.util.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fridge.skl.util.UtilConstants.Skil.COUNTER_SKILL_ID;
import static com.fridge.skl.util.UtilConstants.Skil.NOTE_SKILL_ID;

@Component(COUNTER_SKILL_ID)
public class Counterhandler implements ResultHandler {
    @Autowired
    CounterService counterService;

    @Override
    public ResponseObj handleResult(RequestObj requestObj) {
        String intentName = requestObj.getRequest().getIntents()[0].getIntentName();
        switch (intentName) {
            case UtilConstants.Intent.ADD_COUNTER_INTENT_NAME:
            default:
                return counterService.add(requestObj);

        }


    }
}
