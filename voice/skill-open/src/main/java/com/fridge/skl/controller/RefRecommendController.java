package com.fridge.skl.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.entity.DeviceRecommend;
import com.fridge.skl.model.ref.RecommendRefRequest;
import com.fridge.skl.service.RecommendService;
import com.fridge.skl.util.JsonUtil;
import com.fridge.skl.util.Result;
import com.fridge.skl.util.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 冰箱介绍接口
 */
@RequestMapping("/recommend")
@RestController
public class RefRecommendController {
    @Autowired
    RecommendService recommendService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/ref")
    public Result process(@RequestBody() @Valid RecommendRefRequest request) {
        logger.info("/recommend/ref:{}", request);

        DeviceRecommend recommend = recommendService.recommdRef(request.getDevicecode());
        if (recommend == null) {
            return ResultUtil.success("操作成功","您的智能冰箱上线啦。主人你好，我是小优，是您的智能小管家，我为您介绍一下这款冰箱的特色吧。这是一款海尔全空间保鲜冰箱,可以让冷藏冷冻都保鲜。这款冰箱大肠杆菌和金黄色葡萄球菌杀菌率99.99%。这款冰箱有这么多优点，一定可以满足您未来十年的高品质健康生活！您可以问我什么是全空间保鲜或母婴专属空间怎么用。");

        } else {
            switch (request.getTtsflg()) {
                case 0:
                default:
                    return ResultUtil.success("操作成功", recommend.getRecommend());
                case 1:
                    return ResultUtil.success("操作成功", recommend.getOpendoor1());
                case 2:
                    return ResultUtil.success("操作成功", recommend.getOpendoor2());
                case 3:
                    return ResultUtil.success("操作成功", recommend.getOpendoor3());
                case 4:
                    return ResultUtil.success("操作成功", recommend.getOpendoor4());
                case 5:
                    return ResultUtil.success("操作成功", recommend.getOpendoor5());
                case 6:
                    return ResultUtil.success("操作成功", recommend.getOpendoor6());
                case 7:
                    return ResultUtil.success("操作成功", recommend.getOpendoor7());
                case 8:
                    return ResultUtil.success("操作成功", recommend.getOpendoor8());
            }
        }
    }
}
