package com.fridge.skl.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.dto.FeedbackMapper;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.model.context.ControlDevices;
import com.fridge.skl.model.iotmodel.DeviceInfo;
import com.fridge.skl.model.sterilize.DaySterilization;
import com.fridge.skl.model.sterilize.Sterilization;
import com.fridge.skl.util.DateUtils;
import com.fridge.skl.util.DeviceUtil;
import com.fridge.skl.util.HttpUtil;
import com.fridge.skl.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

import static com.fridge.skl.util.CommonUtil.containsvalue;
import static com.fridge.skl.util.URL_Constants.*;

@Service
@Slf4j
public class SterilizeService extends BaseService {
    //   BCD-633WDCHU1 BCD-633WDCSU1 BCD-633WICTU1 BCD-633WIGWU1 BCD-609WDCTU1 BCD-609WDGWU1 BCD-601WDGWU1 659WISSU1 620WICBU1
    private static final String TYPEID_1 = "201c120024000810012100618005564100000000000061800553000000000040";
    private static final String TYPEID_2 = "201c120024000810012100618005560000000000000061800552000000000040";
    private static final String TYPEID_3 = "200861051c408504012800618005294400000000000061800721000000000040";
    private static final String TYPEID_4 = "200861051c4085040121edb4d3b4cc0000008072ba2d9da5a32c56d2c9a4b240";
    private static final String TYPEID_5 = "200861051c4085040122e154ea5ae400000032b54979dc022989c070fe7ae140";
    private static final String TYPEID_7 = "200861051c4085040121ad3d3057de000000643c84967e5cda6771250b3e8240";
    private static final String TYPEID_8 = "200861051c4085040128fbafa0d9bb000000bc9e7b3ecabf7c5d6e7cfa854440";


    private static final String TYPEID_6 = "200861051c40850401210061800525414e000000000061800908000000000040";
    private static final String[] typeid = new String[]{
            TYPEID_1, TYPEID_2, TYPEID_3, TYPEID_4, TYPEID_5
    };
    private static final String[] typeid2 = new String[]{
            TYPEID_6
    };

    private static final String[] typeid3 = new String[]{
            TYPEID_1, TYPEID_2, TYPEID_3, TYPEID_4, TYPEID_5, TYPEID_7, TYPEID_8
    };

    @Autowired
    FeedbackMapper feedbackMapper;

    /**
     * 查询冰箱杀菌进度
     *
     * @param request 请求
     * @return 回调
     */
    public ResponseObj querySterilize(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content;
        String userid = getUserId(request);
        String deviceId = getDeviceId(request);
        String clientid = getClientId(request);
        String accessToken = getAccessToken(request);


        DeviceInfo binddevice = DeviceUtil.getDeviceList(deviceId, clientid, accessToken, typeid);
        if (binddevice != null) {
            content = getSterilization(binddevice, userid);
        } else {
            content = "对不起，您的家里没有可以杀菌的冰箱哦，赶紧去海尔商城买一台吧";
        }
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }


    /**
     * 查询冰箱杀菌次数
     */
    public ResponseObj querSterilitNum(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content;
        String userid = getUserId(request);
        String deviceId = getDeviceId(request);
        String clientid = getClientId(request);
        String accessToken = getAccessToken(request);


        DeviceInfo binddevice = DeviceUtil.getDeviceList(deviceId, clientid, accessToken, typeid);
        if (binddevice != null) {
            content = countSteriliza(binddevice, userid);
        } else {
            content = "对不起，您的家里没有可以杀菌的冰箱哦，赶紧去海尔商城买一台吧";
        }
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 添加松茸
     */
    public ResponseObj addMasts(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content;

        ControlDevices controldevice = getControldevice(request);
        String food = getSlot(request, "sterfood");
        String relfood = getOriginalSlot(request, "sterfood");
        //添加食材
        if (addfood(controldevice.getId(), getUserId(request), food)) {
            //下发值令和调用接口
            if (containsvalue(typeid3, controldevice.getWifiType())
                    && iotSteriliza(controldevice.getId(), getClientId(request), getAccessToken(request))
                    && oneShotSteriliza(controldevice.getId(), getUserId(request))) {
                content = "好的，" + relfood + "已存入，" + relfood + "对存储环境要求较高，已为您开启智慧杀菌模式，让您吃的更健康更放心哦";
            } else {
                content = "好的，" + relfood + "已存入冰箱冷藏室";
            }
        } else {
            content = "小优也不知道什么原因，添加失败了";
        }

        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    public void addMastsCB(RequestObj request, boolean iotok) {

    }

    /**
     * `
     * <p>
     * demo 一键开始杀菌
     */
    public ResponseObj onShotSterilit(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content;
        String userid = request.getContext().getUser().getUserId();
        String deviceId = request.getContext().getDevice().getDeviceId();
        String clientid = getClientId(request);
        String accessToken = getAccessToken(request);

        DeviceInfo binddevice = DeviceUtil.getDeviceList(deviceId, clientid, accessToken, typeid);
        if (binddevice != null) {
            if (iotSteriliza(binddevice.getDeviceId(), clientid, accessToken)) {
                content = demooneShotSteriliza(binddevice, userid);
            } else {
                content = "小优也不知道什么原因，开启杀菌失败了";
            }
        } else {
            content = "对不起，您的家里没有支持杀菌的冰箱哦，赶紧去海尔商城买一台吧";
        }
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    private Boolean iotSteriliza(String deviceid, String clientid, String accessToken) {
        Map<String, String> prop = new HashMap<>();
        prop.put("sterilizationStatus", "true");
        String usn = DeviceUtil.propertyWrite(deviceid, clientid, accessToken, prop);
        return !StringUtils.isEmpty(usn);
    }

    private final static List<String> contentlist = new ArrayList<>(Arrays.asList(
            "好的，小优已帮您打开解冻模式了",
            "明白，冰箱解冻模式已经打开了，即将开始解冻"
    ));


    private String getSterilization(DeviceInfo binddevice, String userid) {
        JSONObject json = new JSONObject();
        json.put("userId", userid);
        json.put("mac", binddevice.getDeviceId());
        log.info(json.toJSONString());
        String res = HttpUtil.postJson(URL_STERILIZE_MANAGER + ACTION_QUERYSTERILIZE_MAQ, json.toJSONString());
        log.info("res===========>" + res);
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("retCode") && "00000".equals(resJson.getString("retCode"))) {
                if (resJson.containsKey("data")) {
                    JSONObject jsonObject = resJson.getJSONObject("data");
                    Sterilization sterilization = JSON.parseObject(jsonObject.toJSONString(), Sterilization.class);
                    if (1 == sterilization.getStatus()) {
                        String timestr = DateUtils.getDatePoormin(DateUtils.stringToTime(sterilization.getSterilizeEndTime()), new Date());
                        return "杀菌正在进行中，还需要" + timestr + "分钟结束，请耐心等待";
                    } else {
                        return "杀菌已经结束了，您可以安心享受美食了";
                    }
                }

            }

        }

        return "小优费劲了九牛二虎之力也没能查到杀菌的状态，是不是冰箱网络出了什么问题呢？";
    }

    private String countSteriliza(DeviceInfo binddevice, String userid) {
        JSONObject json = new JSONObject();
        json.put("userId", userid);
        json.put("mac", binddevice.getDeviceId());
        log.info(json.toJSONString());
        String res = HttpUtil.postJson(URL_STERILIZE_MANAGER + ACTION_COUNTSTER_MAQ, json.toJSONString());
        log.info("res===========>" + res);
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("retCode") && "00000".equals(resJson.getString("retCode"))) {
                if (resJson.containsKey("data")) {
                    JSONObject jsonObject = resJson.getJSONObject("data");
                    DaySterilization daySterilization = JSON.parseObject(jsonObject.toJSONString(), DaySterilization.class);
                    if (daySterilization != null && daySterilization.getTotalTimes() != 0) {
                        return "今天以为您杀菌" + daySterilization.getTotalTimes() + "次";
                    } else {
                        return "您今天还没进行过杀菌哦，您可以试试对我说，冰箱开始杀菌";
                    }
                }

            }

        }

        return "小优费劲了九牛二虎之力也没能查到您的杀菌状态，是不是冰箱网络出了什么问题呢？";
    }

    private Boolean oneShotSteriliza(String deviceid, String userid) {
        JSONObject json = new JSONObject();
        json.put("userId", userid);
        json.put("mac", deviceid);
        json.put("source", 2);
        log.info(json.toJSONString());
        String res = HttpUtil.postJson(URL_STERILIZE_MANAGER + ACTION_ONESHOTSTER_MAQ, json.toJSONString());
        log.info("res===========>" + res);
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            return resJson.containsKey("retCode") && "00000".equals(resJson.getString("retCode"));
        }
        return false;
    }

    /**
     * 添加食材
     *
     * @return 回答内容
     */
    private boolean addfood(String deviceid, String userid, String foodname) {
        JSONObject json = new JSONObject();
        json.put("deviceId", deviceid.toLowerCase());
        json.put("userId", userid);
        json.put("name", foodname);
        json.put("createTime", new Date().getTime());
        json.put("shelfLife", 7);
        json.put("location", 1);//冰箱添加层数
        String res = HttpUtil.postJson(URL_UNILIFE + ACTION_ADDFOOD, json.toJSONString());
        log.info("addfoodres===========>" + res);
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (0 == resJson.getIntValue("errorno")) {
                return true;
            }
        }
        return false;

    }

    private String demooneShotSteriliza(DeviceInfo binddevice, String userid) {
        JSONObject json = new JSONObject();
        json.put("userId", userid);
        json.put("mac", binddevice.getDeviceId());
        json.put("source", 2);
        json.put("typeId", binddevice.getWifiType());
        log.info(json.toJSONString());
        String res = HttpUtil.postJson(URL_STERILIZE_MANAGER + ACTION_ONESHOTSTER_MAQ, json.toJSONString());
        log.info("res===========>" + res);
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("retCode") && "00000".equals(resJson.getString("retCode"))) {
                return "好的，冰箱杀菌已开启，半小时后您就可以安心享受美食啦。";
            }
        }

        return "小优费劲了九牛二虎之力也没能查到您的杀菌状态，是不是冰箱网络出了什么问题呢？";
    }
}
