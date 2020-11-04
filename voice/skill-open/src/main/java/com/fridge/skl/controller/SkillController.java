package com.fridge.skl.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.service.AsynLogService;
import com.fridge.skl.service.SkillService;
import com.fridge.skl.util.JsonUtil;
import com.fridge.skl.util.UtilConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/skill")
@RestController
public class SkillController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //ObjectMapper是jackson-databind包中的一个类，提供读写JSON的功能，可以方便的进行对象和JSON转换：
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    AsynLogService asynLogService;
    @Autowired
    SkillService skillService;

    @PostMapping("/process")
    public ResponseObj process(@RequestBody String requeststr) {
        ResponseObj resp = new ResponseObj();
        logger.info("接收到请求信息为:{}", requeststr);
        try {
            //JSON.parseObject（String str）是将str转化为相应的JSONObject对象，其中str是“键值对”形式的json字符串，
            // 转化为JSONObject对象之后就可以使用其内置的方法，进行各种处理了。
            RequestObj request = JSONObject.parseObject(requeststr, RequestObj.class);

            if (UtilConstants.Public.REQUEST_TYPE_START.equals(request.getRequest().getType()) || UtilConstants.Public.REQUEST_TYPE_END.equals(request.getRequest().getType())) {

                resp = skillService.doStartAndEnd(request);

            } else if (UtilConstants.Public.REQUEST_TYPE_CONTINUE.equals(request.getRequest().getType())) {

                resp = skillService.doContinue(request);
            }

            asynLogService.requestlog(request, resp);
            logger.info("返回信息为:{}", JsonUtil.formatJson(objectMapper.writeValueAsString(resp)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return resp;
        }
        return resp;
    }


}
