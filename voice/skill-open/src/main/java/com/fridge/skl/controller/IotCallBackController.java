package com.fridge.skl.controller;

import com.alibaba.fastjson.JSON;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.iot.IotCallBack;
import com.fridge.skl.service.AsynLogService;
import com.fridge.skl.service.SkillService;
import com.fridge.skl.util.JedisUtils;
import com.fridge.skl.util.Result;
import com.fridge.skl.util.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.fridge.skl.util.CommonUtil.*;

/**
 * 同步优越食材列表
 */
@RequestMapping("/iotcallback")
@RestController
@Slf4j
public class IotCallBackController {

    @Autowired
    JedisUtils ju;
    @Autowired
    SkillService skillService;
    @Autowired
    AsynLogService asynLogService;

    @PostMapping("/call")
    public Result process(@RequestBody() IotCallBack iotCallBack) {
        log.info(iotCallBack.toString());
        if (ju.hExists(IOTREADISFLG + iotCallBack.getUsn(), REQUEST)) {
            //原请求的request和iot的执行结果
            skillService.doCallBack(
                    JSON.parseObject(ju.hGet(IOTREADISFLG + iotCallBack.getUsn(), REQUEST), RequestObj.class),
                    ju.hExists(IOTREADISFLG + iotCallBack.getUsn(), PARAM) ? JSON.parseObject(ju.hGet(IOTREADISFLG + iotCallBack.getUsn(), PARAM)) : null,
                    "00000".equals(iotCallBack.getRetCode())
            );
        }
        return ResultUtil.success();
    }

}
