package com.fridge.skl.service;

import com.fridge.skl.dto.FeedbackMapper;
import com.fridge.skl.entity.Feedback;
import com.fridge.skl.model.*;
import com.fridge.skl.model.response.Speech;
import com.fridge.skl.model.response.SpeechContent;
import com.fridge.skl.util.CommonUtil;
import com.fridge.skl.util.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackService extends BaseService{
    @Autowired
    FeedbackMapper feedbackMapper;
    private final static List<String> contentlist = new ArrayList<>();

    static {
        contentlist.add("好的，小优记下了，还有要反馈的吗，如果没有您可以说退出我要吐槽");
        contentlist.add("明白，还有吗，如果没有了你可以说退出我要吐槽");
        contentlist.add("好的，我记下了，还有要吐槽的吗，如果没有您可以说退出我要吐槽");
        contentlist.add("好的，小优明白了，还有要反馈的吗，如果没有您可以说退出我要吐槽");
        contentlist.add("懂了，小优都记下来了，还有要反馈的吗，如果没有您可以说退出我要吐槽");
    }

    /**
     * 我要吐槽
     */
    public ResponseObj doFeedback(RequestObj request) {
        ResponseObj resp = initResporse(request);

        String deviceId = request.getContext().getDevice().getDeviceId();
        String devicetype = request.getContext().getDevice().getDeviceType();

        String userdid = request.getContext().getUser().getUserId();
        Feedback feedback = new Feedback();
        feedback.setUserid(userdid);
        feedback.setDeviceid(deviceId);
        feedback.setContent(request.getRequest().getQuery().getContent());
        feedback.setTeiminal(devicetype);
        feedbackMapper.insert(feedback);
        String content = CommonUtil.getRondomStringFromList(contentlist);

        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

}
