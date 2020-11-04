package com.fridge.skl.handler.impl;

import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.ColdextractionService;
import com.fridge.skl.service.NoteService;
import com.fridge.skl.util.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fridge.skl.util.UtilConstants.Skil.COLDEXTRACTION_SKILL_ID;
import static com.fridge.skl.util.UtilConstants.Skil.NOTE_SKILL_ID;

@Component(COLDEXTRACTION_SKILL_ID)
public class ColdextractionHandler implements ResultHandler {

    @Autowired
    ColdextractionService coldextractionService;
    @Override
    public ResponseObj handleResult(RequestObj requestObj) {
        String intentName = requestObj.getRequest().getIntents()[0].getIntentName();

            switch (intentName) {
                case UtilConstants.Intent.START_COLD_INTENT_NAME:
                default:
                    return coldextractionService.startCold(requestObj);
                case UtilConstants.Intent.STOP_COLD_INTENT_NAME:
                    return coldextractionService.stopCold(requestObj);
                case UtilConstants.Intent.QUERY_COLD_INTENT_NAME:
                    return coldextractionService.queryCold(requestObj);
            }
    }
}
