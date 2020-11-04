package com.fridge.skl.service;

import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import org.springframework.stereotype.Service;

@Service
public class CounterService extends BaseService {

    /**
     * 帮我记一下
     */
    public ResponseObj add(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content;


        int num1 = Integer.parseInt(getSlot(request, "num"));
        int num2 = Integer.parseInt(getSlot(request, "num2"));

        content = num1 + "加" + num2 + "等于" + (num1 + num2);


        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

}
