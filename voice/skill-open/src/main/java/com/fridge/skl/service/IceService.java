package com.fridge.skl.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.dto.FeedbackMapper;
import com.fridge.skl.model.DeviceRoomInfos;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.model.iotmodel.DeviceInfo;
import com.fridge.skl.util.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.fridge.skl.service.IDService.RETCOD_OK;
import static com.fridge.skl.util.DateUtils.DATE_TIME_FORMAT;
import static com.fridge.skl.util.URL_Constants.*;

@Service
@Slf4j
public class IceService extends BaseService {

    //临时借用的appid 如果有个自己的需要替换
    private static final String APPID = "MB-UZHSH-0000";
    private static final String APPKEY = "f50c76fbc8271d361e1f6b5973f54585";

    private static final String TYPEID_600 = "200861051c408504012100618005294b00000000000061800944410000000040";
    private static int sequence = 1;

    @Autowired
    FeedbackMapper feedbackMapper;

    /**
     * 开始解冻
     */
    public ResponseObj defreeze(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = CommonUtil.getRondomStringFromList(contentlist);
        String deviceId = request.getContext().getDevice().getDeviceId();
        String clientid = request.getContext().getDevice().getClientId();
        String userid = request.getContext().getUser().getUserId();
        String accessToken = request.getContext().getDevice().getAccessToken();


        DeviceInfo binddevice = getDeviceList(deviceId, clientid, accessToken, TYPEID_600);
        if (binddevice != null) {
//            getDeviceInfo(binddevice.getDeviceId(), clientid, accessToken);
            content = oneshotfreezing(binddevice, accessToken, clientid, userid, deviceId);
        } else {
            content = "对不起，您的家里没有可以解冻的冰箱哦";
        }
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 预约解冻
     */
    public ResponseObj redefreeze(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String deviceId = request.getContext().getDevice().getDeviceId();
        String clientid = request.getContext().getDevice().getClientId();
        String userid = request.getContext().getUser().getUserId();
        String accessToken = request.getContext().getDevice().getAccessToken();
        //设备列表
        Date time = getSlotTimeHour(request);
        String timestr = getoriginalTime(request);
        String   content = "对不起，您的家里没有可以解冻的冰箱哦";
        if (time != null) {

            log.info("time:------------->" + DateUtils.dateToString(time, DATE_TIME_FORMAT));
            if (!time.before(new Date())) {


                DeviceInfo binddevice = getDeviceList(deviceId, clientid, accessToken, TYPEID_600);
                if (binddevice != null) {
                    content = refreez(binddevice, accessToken, clientid, userid, DateUtils.dateToString(time, DATE_TIME_FORMAT), timestr);
                }
            } else {
                content = "小优没有时光机，只能预约未来的时间哦";
            }
        } else {
            content = "小优不会读心术，预约的时间能再具体一点吗";
        }
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }


    /**
     * 取消解冻
     */
    public ResponseObj clearfreeze(RequestObj request) {

        ResponseObj resp = initResporse(request);
        String content = CommonUtil.getRondomStringFromList(contentlist);
        String deviceId = request.getContext().getDevice().getDeviceId();
        String clientid = request.getContext().getDevice().getClientId();
        String userid = request.getContext().getUser().getUserId();
        String accessToken = request.getContext().getDevice().getAccessToken();
        //设备列表
        List<DeviceRoomInfos> deviceRoomInfos = getDevice(request);
        DeviceInfo binddevice = getDeviceList(deviceId, clientid, accessToken, TYPEID_600);
        if (binddevice != null) {
//            getDeviceInfo(binddevice.getDeviceId(), clientid, accessToken);
            if (binddevice.isOnline()) {
                content = clearfreezing(binddevice, accessToken, clientid, userid, deviceId);
            } else {
                content = "对不起，您家的冰箱没在线哦，请检查网络连接状况";
            }
        } else {
            content = "对不起，您的家里没有可以解冻的冰箱哦";
        }

        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }


    private final static List<String> contentlist = new ArrayList<>(Arrays.asList(
            "好的，小优已帮您打开解冻模式了",
            "明白，冰箱解冻模式已经打开了，即将开始解冻"
    ));


    /**
     * 获取当前用户所有设备列表
     */
    private DeviceInfo getDeviceList(String deviceId, String clientid, String accessToken, String typeid) {
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

        String res = HttpUtil.getJsonWithHead(URL_IOT_ACCESS + ACTION_DEVICEINFOS, builder.build());
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
                            //System.out.println(deviceInfo.toString
                            if (deviceInfo.getWifiType().equals(typeid)) {
                                return deviceInfo;
                            }
                            // getDeviceInfo(deviceInfo.getDeviceId(), clientid, accessToken);
                        }
                    }

                }
            }
        }


        return null;
    }


    private String oneshotfreezing(DeviceInfo binddevice, String accessToken, String clientid, String userid, String deviceId) {
        JSONObject json = new JSONObject();
        json.put("accessToken", accessToken);
        json.put("clientId", clientid);
        json.put("userId", userid);
        json.put("mac", binddevice.getDeviceId());
        json.put("appId", APPID);
        json.put("appKey", APPKEY);
        json.put("appVersion", "1.0");
        json.put("typeId", binddevice.getWifiType());
        json.put("model", binddevice.getProductNameT());
        //固定解冻食材是牛排 #1精肉2脂肉3牛排
        json.put("food", 3);
        log.info(json.toJSONString());
        String res = HttpUtil.postJson(URL_UNFREEZE + String.format(ACTION_VOICEUNFREEZING, binddevice.getWifiType()), json.toJSONString());
        log.info("res===========>" + res);
        if (!"".equals(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("code") &&resJson.getString("code").equals("0")) {
                //return "解冻模式已开启，即将为您解冻，大约在8分钟之后解冻完成，请及时取出。";
                return "解冻模式已开启，即将为您解冻";
            } else if (resJson.containsKey("code") &&resJson.getString("code").equals("1")) {
                if ("当前冰箱正处于解冻状态".equals(resJson.getString("msg"))) {
                    return "小优帮您查了下，您的冰箱正在进行解冻，请完成后再试";
                }
            }
        }

        return "小优用尽浑身解数也没能打开解冻模式，是不是冰箱网络出了什么问题呢？";
    }

    /**
     * 预约解冻
     */

    private String refreez(DeviceInfo binddevice, String accessToken, String clientid, String userid, String time, String timestr) {
        JSONObject json = new JSONObject();
        json.put("accessToken", accessToken);
        json.put("clientId", clientid);
        json.put("userId", userid);
        json.put("mac", binddevice.getDeviceId());
        json.put("appId", APPID);
        json.put("appKey", APPKEY);
        json.put("appVersion", "1.0");
        json.put("typeId", binddevice.getWifiType());
        json.put("model", binddevice.getProductNameT());
        json.put("yuyueTime", time);
        //固定解冻食材是牛排 #1精肉2脂肉3牛排
        json.put("food", 3);

        log.info(json.toJSONString());
        String res = HttpUtil.postJson(URL_UNFREEZE + String.format(ACTION_SETAPPOINTMENTUNFREEZING, binddevice.getWifiType()), json.toJSONString());
        log.info("res===========>" + res);
        if (!"".equals(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("code") &&resJson.getString("code").equals("0")) {

                return "已经为您预约" + timestr + "开始解冻。";
            } else if (resJson.containsKey("code") &&resJson.getString("code").equals("1")) {
                if ("已经录入10条预约了，不能再添加了".equals(resJson.getString("msg"))) {
                    return "小优帮您查了下，您已经录入10条预约了，不能再预约了，您可以去app端进行取消";
                }
            }
        }

        return "小优用尽浑身解数也没能预约上解冻，是不是冰箱网络出了什么问题呢？";
    }


    private String clearfreezing(DeviceInfo binddevice, String accessToken, String clientid, String userid, String deviceId) {
        JSONObject json = new JSONObject();
        json.put("accessToken", accessToken);
        json.put("clientId", clientid);
        json.put("userId", userid);
        json.put("mac", binddevice.getDeviceId());
        json.put("appId", APPID);
        json.put("appKey", APPKEY);
        json.put("appVersion", "1.0");
        json.put("typeId", binddevice.getWifiType());
        json.put("model", binddevice.getProductNameT());
        log.info(json.toJSONString());
        String res = HttpUtil.postJson(URL_UNFREEZE + String.format(ACTION_STOPUNFREEZING, binddevice.getWifiType()), json.toJSONString());
        log.info(res);
        if (!"".equals(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("code") && resJson.getString("code").equals("0")) {
                return "取消解冻成功，记得把食材取出哦";
            } else if (resJson.containsKey("code") && resJson.getString("code").equals("1")) {
                if ("正在执行预约解冻，请去app上操作".equals(resJson.getString("msg"))) {
                    return "小优帮你查到，您的冰箱还有其他正在执行的预约解冻哦，请去手机app取消后重试";
                } else {
                    return "小优费了九牛二虎之力也没停止解冻，是不是网络出问题了？";
                }
            }
        }

        return "小优用尽浑身解数也没能关闭解冻模式，是不是冰箱网络出了什么问题呢？";
    }

}
