package com.fridge.skl.service;


import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.factory.FactoryForhandler;
import com.fridge.skl.handler.CallBackHandler;
import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.handler.impl.ErrorHandler;
import com.fridge.skl.model.*;
import com.fridge.skl.model.response.Speech;
import com.fridge.skl.model.response.SpeechContent;
import com.fridge.skl.util.UtilConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.fridge.skl.util.UtilConstants.Skil.FEEDBERAK_SKILL_ID;

/**
 * 技能分发路由类
 */
@Component
public class SkillService {
    private static Logger logger = LoggerFactory.getLogger(SkillService.class);
    //@Autowired 注释，它可以对类成员变量、方法及构造函数进行标注，完成自动装配的工作。 
    // 通过 @Autowired的使用来消除 set ，get方法。
    @Autowired
    private FactoryForhandler factoryForhandler;
    @Autowired
    ErrorHandler errorHandler;

    public ResponseObj doStartAndEnd(RequestObj request) {

        ResponseObj resp = new ResponseObj();
        resp.setVersion("1.0");
        resp.setSession(request.getSession());//这是常用的方式，从当前request中获取session，如果获取不到session，
        // 则会自动创建一个session，默认为true,
        // 并返回新创建的session；如果获取到，则返回获取到的session;
        resp.setContext(request.getContext());

        Response response = new Response();
        Speech speech = new Speech();
        response.setValid(true);//若用户语料无法解析，则设置false

        speech.setType(SpeechTypeEnum.text);
        SpeechContent sc = new SpeechContent();

        if (UtilConstants.Public.REQUEST_TYPE_START.equals(request.getRequest().getType())) {
            //如果用户的请求语句和语料库中的开始语句类型相同
            //获取类型和语句
            sc.setType(UtilConstants.Public.REQUEST_TYPE_START);
            sc.setInfo(UtilConstants.Public.WELCOME_INFO);
            //小优会继续回复，而不会停止响应
            response.setShouldEndSession(false);
            //如果技能的id和用户请求的id相同，则反馈技能语句并获取技能内容，否则会反馈不会技能的语句
            if (FEEDBERAK_SKILL_ID.equals(request.getContext().getSkill().getSkillId())) {
                speech.setContent("欢迎使用吐槽大会技能，有什么要吐槽的尽管对我说吧");
            } else {
                speech.setContent("你打开的技能我还不会哦，但我会很快学会的");
            }
            //否则，如果用户的请求语句和语料库中的结束语句类型相同，则获取技能结束的类型和语句
        } else if (UtilConstants.Public.REQUEST_TYPE_END.equals(request.getRequest().getType())) {

            sc.setType(UtilConstants.Public.REQUEST_TYPE_END);
            sc.setInfo(UtilConstants.Public.END_INFO);
            //小优不会继续回复，对话结束
            response.setShouldEndSession(true);

            if (FEEDBERAK_SKILL_ID.equals(request.getContext().getSkill().getSkillId())) {

                speech.setContent("那再见了，有什么想找小优反馈的欢迎再来哦");
            }
        }

        //speech.setContent(JSONObject.toJSONString(sc));
        response.setSpeech(speech);
        resp.setResponse(response);

        return resp;

    }

    public ResponseObj doContinue(RequestObj request) {

        ResponseObj resp = new ResponseObj();

        //获得技能id
        String skillId = request.getContext().getSkill().getSkillId();
        if (StringUtils.isEmpty(skillId)) {

            resp = doInvalid(request);
        } else {

            ResultHandler resultHandler = factoryForhandler.getHandler(skillId);
            GuessIntent[] intents = request.getRequest().getIntents();
            if (resultHandler != null && intents != null && intents.length > 0) {
                resp = resultHandler.handleResult(request);
            } else {
                resp = errorHandler.handleResult(request);
            }
        }

        return resp;

    }

    /**
     * iot回调入口
     *
     * @param request 原请求的request
     * @param iotok   iot下发结果
     */

    public void doCallBack(RequestObj request, boolean iotok) {

        String skillId = request.getContext().getSkill().getSkillId();

        CallBackHandler resultHandler = factoryForhandler.getCallBackHandler(skillId);
        resultHandler.handleCallBack(request,null, iotok);

    }

    /**
     * iot回调入口 whitprarm
     *
     * @param request 原请求的request
     * @param param   传参
     * @param iotok   iot下发结果
     */

    public void doCallBack(RequestObj request, JSONObject param, boolean iotok) {

        String skillId = request.getContext().getSkill().getSkillId();

        CallBackHandler resultHandler = factoryForhandler.getCallBackHandler(skillId);
        resultHandler.handleCallBack(request, param, iotok);

    }

    private static ResponseObj doInvalid(RequestObj request) {

        ResponseObj resp = new ResponseObj();
        resp.setVersion("1.0");
        resp.setSession(request.getSession());
        resp.setContext(request.getContext());

        Response response = new Response();

        Speech speech = new Speech();
        speech.setType(SpeechTypeEnum.text);
        SpeechContent sc = new SpeechContent();

        response.setValid(false);
        sc.setType(UtilConstants.Public.ERROR_TYPE);
        sc.setInfo(UtilConstants.Public.ERROR_INFO);
        response.setShouldEndSession(false);

        speech.setContent(JSONObject.toJSONString(sc));
        response.setSpeech(speech);
        resp.setResponse(response);

        return resp;

    }


}