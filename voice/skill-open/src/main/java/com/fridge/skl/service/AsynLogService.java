package com.fridge.skl.service;

import com.fridge.skl.dto.RequestlogMapper;
import com.fridge.skl.entity.Requestlog;
import com.fridge.skl.model.GuessIntent;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.model.Slot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsynLogService {
    @Autowired
    RequestlogMapper requestlogMapper;

    @Async
    public void requestlog(RequestObj request, ResponseObj response) {
        Requestlog requestlog = new Requestlog();
        requestlog.setUserid(request.getContext().getUser().getUserId());
        requestlog.setSn(request.getRequest().getRequestId());
        requestlog.setLocal(request.getContext().getLocation().getCityName());
        requestlog.setAccesstoken(request.getContext().getDevice().getAccessToken());
        requestlog.setClientid(request.getContext().getDevice().getClientId());
        requestlog.setDeviceid(request.getContext().getDevice().getDeviceId());
        requestlog.setDevicetype(request.getContext().getDevice().getDeviceType());
        requestlog.setSessionid(request.getSession().getSessionId());
        requestlog.setTimestamp(String.valueOf(request.getRequest().getTimestamp()));
        requestlog.setContent(request.getRequest().getQuery().getContent());
        if (response != null && response.getResponse() != null && response.getResponse().getSpeech() != null) {
            requestlog.setResponse(response.getResponse().getSpeech().getContent());
        }
        GuessIntent[] guessIntents = request.getRequest().getIntents();
        if (guessIntents != null && guessIntents.length > 0) {
            requestlog.setIntentid(String.valueOf(guessIntents[0].getIntentId()));
            requestlog.setIntentname(guessIntents[0].getIntentName());
            requestlog.setTaskid(String.valueOf(guessIntents[0].getTaskId()));
            requestlog.setTaskname(guessIntents[0].getTaskName());
            requestlog.setIntentcode(guessIntents[0].getIntentCode());
            if (guessIntents[0].getSlots() != null && guessIntents[0].getSlots().length > 0) {
                for (Slot slot : guessIntents[0].getSlots()) {
                    requestlog.setSlots((requestlog.getSlots() == null ? "" : requestlog.getSlots()) + "_" + slot.toString());
                }
            }
        }
        requestlogMapper.insert(requestlog);

    }
}
