package com.fridge.skl.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.dto.DeviceRecommendMapper;
import com.fridge.skl.entity.DeviceRecommend;
import com.fridge.skl.model.*;
import com.fridge.skl.model.iotmodel.DeviceInfo;
import com.fridge.skl.model.response.Speech;
import com.fridge.skl.model.response.SpeechContent;
import com.fridge.skl.util.*;
import okhttp3.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

import static com.fridge.skl.service.IDService.RETCOD_OK;
import static com.fridge.skl.util.URL_Constants.*;

@Service
public class RecommendService extends BaseService {

    @Autowired
    DeviceRecommendMapper deviceRecommendMapper;
    //临时借用的appid 如果有个自己的需要替换
    private static final String APPID = "MB-UZHSH-0000";
    private static final String APPKEY = "f50c76fbc8271d361e1f6b5973f54585";
    private static int sequence = 1;

    /**
     * 介绍冰箱接口
     */
    @Cacheable(cacheNames = "device", key = "#devicecode", unless = "#result == null")
    public DeviceRecommend recommdRef(String devicecode) {
        return deviceRecommendMapper.selectByDeviceCode(devicecode);
    }

    /**
     * 介绍冰箱
     */
    public ResponseObj recommdRefSup(RequestObj request) {
        ResponseObj resp = initResporse(request);

        String deviceId = request.getContext().getDevice().getDeviceId();
        String clientid = request.getContext().getDevice().getClientId();
        String accessToken = request.getContext().getDevice().getAccessToken();

        String content = "对不起，您的家里还没有绑定冰箱，赶紧去买一台吧";
        DeviceInfo binddevice = getDeviceList(deviceId, clientid, accessToken);
        if (binddevice != null) {
            String devicename = binddevice.getProductNameT();
            DeviceRecommend recommend = deviceRecommendMapper.selectByDeviceName(devicename);
            if (recommend != null) {
                content = recommend.getRecommend();
            } else {
                //TODO 可以追加反馈功能，上报用户需要的型号。
                content = "对不起没有找到" + binddevice.getProductNameT() + "这款冰箱的介绍，我们的工程师一定会尽快添加的，您可以过几天再试试";
            }
        }
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 获取当前用户所有设备列表
     */
    private DeviceInfo getDeviceList(String deviceId, String clientid, String accessToken) {
        Headers.Builder builder = new Headers.Builder();

        builder.add("appId", APPID);
        builder.add("version", "0.3");
        builder.add("clientId", clientid);
        builder.add("accessToken", accessToken);

        if (sequence == 999999) {
            sequence = 1;
        } else {
            sequence++;
        }
        String presequenceid = "000000" + sequence;
        String sequenceid = presequenceid.substring(presequenceid.length() - 6);
        String sequenceId = DateUtils.getCurrentDateTime(DateUtils.DATE_FORMAT_FREE) + sequenceid;
        builder.add("sequenceId", sequenceId);


        Long datatime = new Date().getTime();
        String sign = SignUtil.getSign(APPID, APPKEY, datatime.toString(), "", URL_IOT_ACCESS + ACTION_DEVICEINFOS);
        builder.add("sign", sign);
        builder.add("timestamp", datatime.toString());
        builder.add("language", "zh-cn");
        builder.add("timezone", "Asia/Shanghai");
        builder.add("appKey", APPKEY);


        builder.add("Content-Type", "application/json;charset=UTF-8");

        String res = HttpUtil.getJsonWithHead(URL_IOT_ACCESS + ACTION_LASTREPORTSTATUS, builder.build());
        DeviceInfo deviceInfoforchat = null;
        //System.out.println("RES==================" + res);//TODO
        if (!"".equals(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("retCode") && RETCOD_OK.equals(resJson.getString("retCode"))) {
                if (resJson.containsKey("deviceinfos")) {
                    JSONArray deviceinfos = resJson.getJSONArray("deviceinfos");
                    for (int i = 0; i < deviceinfos.size(); i++) {
                        DeviceInfo deviceInfo = JSON.toJavaObject(deviceinfos.getJSONObject(i), DeviceInfo.class);
                        if (deviceInfo != null && !StringUtils.isEmpty(deviceInfo.getDeviceType()) && deviceInfo.getDeviceType().length() > 2) {
                            //判断是冰箱 可扩展其他类型设备
                            if ("01".equals(deviceInfo.getDeviceType().substring(0, 2)) && deviceInfo.isOnline()) {
                                return deviceInfo;
                            }
                        }
                    }

                }
            }
        }


        return null;
    }

}
