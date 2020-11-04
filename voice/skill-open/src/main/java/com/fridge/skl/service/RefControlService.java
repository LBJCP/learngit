package com.fridge.skl.service;

import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.dto.FeedbackMapper;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.model.iotmodel.DeviceInfo;
import com.fridge.skl.util.CommonUtil;
import com.fridge.skl.util.DeviceUtil;
import com.fridge.skl.util.HttpUtil;
import com.fridge.skl.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.fridge.skl.util.URL_Constants.ACTION_OPENDOOR;
import static com.fridge.skl.util.URL_Constants.URL_OPENDOOR;

@Service
@Slf4j
public class RefControlService extends BaseService {

    private static final String TYPEID_633GW = "201c120024000810012100618005560000000000000061800552000000000040";
    private static final String TYPEID_633CH = "201c120024000810012100618005564100000000000061800553000000000040";
    private static final String[] typeid = new String[]{
            TYPEID_633GW, TYPEID_633CH
    };
    @Autowired
    FeedbackMapper feedbackMapper;

    /**
     * 冰箱开门
     */
    public ResponseObj opendoor(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content;
        String deviceId = request.getContext().getDevice().getDeviceId();
        String clientid = request.getContext().getDevice().getClientId();
        String accessToken = request.getContext().getDevice().getAccessToken();


        DeviceInfo binddevice = DeviceUtil.getDeviceList(deviceId, clientid, accessToken, typeid);
        if (binddevice != null) {
            content = openrefdoorhttp(binddevice, accessToken);
        } else {
            content = "对不起，您的家里没有可以打开抽屉的冰箱哦，赶紧去海尔商城买一台吧";
        }
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }




    private final static List<String> contentlist = new ArrayList<>(Arrays.asList(
            "好的，小优已帮您打开解冻模式了",
            "明白，冰箱解冻模式已经打开了，即将开始解冻"
    ));


    private String openrefdoorhttp(DeviceInfo binddevice, String accessToken) {
        JSONObject json = new JSONObject();
        json.put("accessToken", accessToken);
        json.put("mac", binddevice.getDeviceId());
        log.info(json.toJSONString());
        String res = HttpUtil.postJson(URL_OPENDOOR + ACTION_OPENDOOR, json.toJSONString());
        log.info("res===========>" + res);
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("success") && resJson.getBoolean("success")) {
                return "冰箱门已打开了";
            } else if (resJson.containsKey("code") && resJson.getString("code").equals("1")) {
                return "小优费劲了九牛二虎之力也没能打开冰箱门，是不是开着呢";
            }
        }

        return "小优用尽浑身解数也没能打开冰箱门，是不是冰箱网络出了什么问题呢？";
    }

}
