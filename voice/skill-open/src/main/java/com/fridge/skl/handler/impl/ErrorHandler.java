package com.fridge.skl.handler.impl;

import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ErrorHandler implements ResultHandler {
    @Autowired
    ErrorService errorService;

    @Override
    public ResponseObj handleResult(RequestObj requestObj) {
        return errorService.onError(requestObj);
    }
}
