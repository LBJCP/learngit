package com.fridge.skl.handler.impl;

import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.fridge.skl.util.UtilConstants.Skil.FEEDBERAK_SKILL_ID;


@Component(FEEDBERAK_SKILL_ID)
public class FeedbackHandler implements ResultHandler {

    @Autowired
    public  FeedbackService feedbackservic;
    @Override
    public ResponseObj handleResult(RequestObj requestObj) {

        return feedbackservic.doFeedback(requestObj);
    }
}
