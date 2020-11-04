package com.fridge.skl.service;

import com.fridge.skl.dto.VoiceNoteMapper;
import com.fridge.skl.dto.VoiceShoplistMapper;
import com.fridge.skl.entity.VoiceNote;
import com.fridge.skl.entity.VoiceShoplist;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.util.CommonUtil;
import com.fridge.skl.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class NoteService extends BaseService {
    @Autowired
    VoiceNoteMapper voiceNoteMapper;
    @Autowired
    VoiceShoplistMapper voiceShoplistMapper;
    @Autowired
    RedisUtil redisUtil;

    /**
     * 帮我记一下
     */
    public ResponseObj noteSomething(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String any = getSlot(request, "any");
        String content = "小优帮您记下了：" + any + ",您可以试试对我说，我刚才让你记录了什么";

        VoiceNote voiceNote = new VoiceNote();
        if (!StringUtils.isEmpty(any) && any.length() > 2550) {
            any = any.substring(0, 2540);
        }
        voiceNote.setNote(any);
        voiceNote.setUserid(getUserId(request));
        voiceNote.setTime(new Date().getTime());
        voiceNoteMapper.insert(voiceNote);
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 刚才记了什么
     */
    public ResponseObj noteMe(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "";
        VoiceNote voiceNote = voiceNoteMapper.selectByUserid(getUserId(request), 0);
        if (voiceNote != null && !StringUtils.isEmpty(voiceNote.getNote())) {
            content = "您刚才让我帮您记录的内容是：" + voiceNote.getNote();
        } else {
            content = "小优找了半天也没有找到您近期让我记什么，难道是我失忆了？";
        }
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 清空语音留言板
     */
    public ResponseObj clearNote(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "";
        int num = voiceNoteMapper.deleteByUserid(getUserId(request));
        if (num != 0) {
            content = "已为您清空留言板，有什么要记录的再来找我啊";
        } else {
            content = "您的留言板本来就是空的,想添加您可以试试对我说，小优小优，帮我记一下：密码是123456";
        }
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 删除上一条留言
     */
    public ResponseObj dellastNote(RequestObj request) {

        ResponseObj resp = initResporse(request);
        String content;
        int num = voiceNoteMapper.deleteLastByUserid(getUserId(request));
        if (num != 0) {
            content = "最后一条记录以为您删除了";
        } else {
            content = "您的留言板本来就是空的,想添加您可以试试对我说，小优小优，帮我记一下：密码是123456";
        }
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }


    /**
     * 上一条记录
     */
    public ResponseObj lastNote(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "";
        VoiceNote voiceNote = voiceNoteMapper.selectByUserid(getUserId(request), 1);
        if (voiceNote != null && !StringUtils.isEmpty(voiceNote.getNote())) {
            content = "您刚才让我帮您记录的内容是：" + voiceNote.getNote();
        } else {
            //TODO
            content = "小优找了半天也没有找到您近期让我记什么，难道是我失忆了？";
        }
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 查询购物清单
     */
    public ResponseObj queryShopList(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "";
        List<VoiceShoplist> shoplists = voiceShoplistMapper.selectByUserid(getUserId(request));
        if (!CollectionUtils.isEmpty(shoplists)) {
            StringBuilder sb = new StringBuilder();
            shoplists.forEach(item -> {
                sb.append(item.getShopitem());
                sb.append(",");
            });

            content = "您的购物清单里记着：" + CommonUtil.cutlaststr(sb.toString(), ",");
        } else {
            content = "小优帮您查了下，您的购物清单空空如也，可以试试对我说：添加  牛肉，鸡蛋，西红柿 到购物清单";
        }
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 添加购物清单
     */
    public ResponseObj addShopList(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "";
        VoiceShoplist voiceitem = new VoiceShoplist();
        String any = getSlot(request, "any");
        voiceitem.setShopitem(any);
        voiceitem.setUserid(getUserId(request));
        voiceitem.setTime(new Date().getTime());
        voiceitem.setDelflg(0);
        int num = voiceShoplistMapper.insert(voiceitem);
        if (num != 0) {

            content = "您的购物清单增加了：" + any;
        } else {
            content = "小优也不知道因为啥，添加失败了";
        }
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 删除购物清单
     */
    public ResponseObj delShopList(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "";
        int num = voiceShoplistMapper.deleteByUserid(getUserId(request));
        if (num != 0) {
            content = "以为您清空购物清单";


        } else {
            content = "小优帮您查了下，您的购物清单空空如也，可以试试对我说：添加  牛肉，鸡蛋，西红柿 到购物清单";

        }
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

}
