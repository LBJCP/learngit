package com.fridge.skl.service;

import com.fridge.skl.dto.PregnantMapper;
import com.fridge.skl.entity.Pregnant;
import com.fridge.skl.handler.ResultHandler;
import com.fridge.skl.model.*;
import com.fridge.skl.model.response.Speech;
import com.fridge.skl.model.response.SpeechContent;
import com.fridge.skl.util.DateUtils;
import com.fridge.skl.util.UtilConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.fridge.skl.util.UtilConstants.Skil.PREGNANT_MANAGER_SKILL_ID;

@Service
public class PregnantService extends BaseService{
    @Autowired(required = false)
    private PregnantMapper pregnantMapper;

    /**
     * 开始备孕
     */
    public ResponseObj doStartPregnancy(RequestObj request) {
        ResponseObj resp = initResporse(request);


        String userdid = request.getContext().getUser().getUserId();

        String content = "对不起，没有查询到您的步数信息";
        Pregnant pregnant = pregnantMapper.get(userdid);
        if (pregnant != null) {
            Date startTime = pregnant.getStarttime();
            Date preTime = pregnant.getPretime();
            if (startTime != null) {
                content = "您已经于" + DateUtils.getChinessDate(preTime) + "标记怀孕，如要开始备孕请先取消";
            } else {
                content = "您已经于" + DateUtils.getChinessDate(preTime) + "开始备孕，重新开始备孕请先取消";
            }

        } else {
            pregnant = new Pregnant();
            pregnant.setUserid(userdid);
            pregnant.setPretime(new Date());
            pregnantMapper.insert(pregnant);
            ;
            content = "今天是" + DateUtils.getChinessDate(new Date()) + "，您已开始备孕。小优已为您创建备孕期档案";
        }

        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 备孕查询
     */
    public ResponseObj doSearchPregnancy(RequestObj request) {
        ResponseObj resp = initResporse(request);

        String userdid = request.getContext().getUser().getUserId();

        String content = "";
        Pregnant pregnant = pregnantMapper.get(userdid);
        if (pregnant != null) {
            Date startTime = pregnant.getStarttime();
            Date preTime = pregnant.getPretime();
            if (startTime != null) {
                Long datanum = DateUtils.getDayByMinusDate(startTime, new Date());
                content = "您已经于" + DateUtils.getChinessDate(preTime) + "标记怀孕，已怀孕" + datanum + "天";
            } else {
                Long datanum = DateUtils.getDayByMinusDate(preTime, new Date());

                content = "您已经于" + DateUtils.getChinessDate(preTime) + "开始备孕，已备孕" + datanum + "天";
            }

        } else {

            content = "对不起，您还没有开始备孕";
        }

        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }


    /**
     * 取消备孕
     */
    public ResponseObj dodelectPregnancy(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String userdid = request.getContext().getUser().getUserId();

        String content = "";
        Pregnant pregnant = pregnantMapper.get(userdid);
        if (pregnant != null) {
            pregnantMapper.delete(pregnant);
            content = "取消备孕成功";
        } else {

            content = "对不起，您并没有开始备孕";
        }

        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }


    /**
     * 开始怀孕 TODO
     */
    public ResponseObj doStartPregant(RequestObj request) {
        ResponseObj resp = initResporse(request);

        String userdid = request.getContext().getUser().getUserId();

        String content = "";
        Pregnant pregnant = pregnantMapper.get(userdid);
        if (pregnant != null) {
            Date startTime = pregnant.getStarttime();
            Date preTime = pregnant.getPretime();
            if (startTime != null) {
                content = "您已经于" + DateUtils.getChinessDate(preTime) + "标记怀孕，如要开始备孕请先取消";
            } else {
                content = "您已经于" + DateUtils.getChinessDate(preTime) + "开始备孕，重新开始备孕请先取消";
            }

        } else {
            pregnant = new Pregnant();
            pregnant.setId(userdid);
            pregnant.setStarttime(new Date());
            pregnantMapper.insert(pregnant);
            content = "开始备孕成功";
        }

        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 怀孕查询
     */
    public ResponseObj doSearchPregant(RequestObj request) {
        ResponseObj resp = initResporse(request);

        String userdid = request.getContext().getUser().getUserId();

        String content = "";
        Pregnant pregnant = pregnantMapper.get(userdid);
        if (pregnant != null) {
            Date startTime = pregnant.getStarttime();
            Date preTime = pregnant.getPretime();
            if (startTime != null) {
                content = "您已经于" + DateUtils.getChinessDate(preTime) + "标记怀孕";
            } else {
                Long datanum = DateUtils.getDayByMinusDate(preTime, new Date());

                content = "您已经于" + DateUtils.getChinessDate(preTime) + "开始备孕，已备孕" + datanum + "天";
            }

        } else {

            content = "您还没有开始备孕";
        }

        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 取消怀孕
     */
    public ResponseObj dodelectPregant(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String userdid = request.getContext().getUser().getUserId();

        String content = "";
        Pregnant pregnant = pregnantMapper.get(userdid);
        if (pregnant != null) {
            Date startTime = pregnant.getStarttime();
            Date preTime = pregnant.getPretime();
            if (startTime != null) {
                content = "您已经于" + DateUtils.getChinessDate(preTime) + "标记怀孕，如要开始备孕请先取消";
            } else {
                content = "您已经于" + DateUtils.getChinessDate(preTime) + "开始备孕，重新开始备孕请先取消";
            }
            pregnantMapper.delete(pregnant);
            content = "取消备孕成功";
        } else {

            content = "对不起，您并没有开始备孕";
        }

        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }


}
