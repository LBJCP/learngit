package com.fridge.skl.handler.impl;

import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.GuessIntent;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.ChatService;
import com.fridge.skl.util.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fridge.skl.util.UtilConstants.Skil.ASK_FATHER_SKILL_ID;

@Component(ASK_FATHER_SKILL_ID)
public class ChatHandler implements ResultHandler {
    @Autowired
    ChatService chatService;

    @Override
    public ResponseObj handleResult(RequestObj requestObj) {
        GuessIntent[] intents = requestObj.getRequest().getIntents();
        String intentName = intents[0].getIntentName();
        if (UtilConstants.Intent.ASK_FATHER_INTENT_NAME.equals(intentName)) {

            return chatService.askFather(requestObj);
        }
        return new ResponseObj();
    }
}
