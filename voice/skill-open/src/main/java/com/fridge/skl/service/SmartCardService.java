package com.fridge.skl.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.dto.FeedbackMapper;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.model.iot.DeviceBaseInfo;
import com.fridge.skl.model.samrtcard.SmartCard;
import com.fridge.skl.util.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.fridge.skl.util.CommonUtil.*;
import static com.fridge.skl.util.URL_Constants.*;


@Service
@Slf4j
public class SmartCardService extends BaseService {
    @Autowired
    DeviceUtil deviceUtil;
    @Autowired
    JedisUtils ju;

    private static final String CARDNAME = "smartcard";

    private static final String TYPEID_521 = "200861051c408504012100618004374500000000000061800464450000000040";


    private static final String[] typeid = new String[]{
            TYPEID_521
    };

    @Autowired
    FeedbackMapper feedbackMapper;

    /**
     * 开启冰箱智慧卡片
     *
     * @param request 请求
     * @return 回调
     */
    public ResponseObj openSmartCard(RequestObj request) {
        ResponseObj resp = initResporse(request);
        String content = "好的";
        //获得语义槽
        String slot = getSlot(request, CARDNAME);
        if (!controllDevcieInList(request, typeid)) {
            setResponse(resp, "对不起，您家的冰箱暂不支持智慧卡片功能，我们的工程师正在加紧开发，您可以过几天再来试一下");
            return resp;
        }
        DeviceBaseInfo baseinfo = DeviceUtil.getDeviceBaseInfo(getControldevice(request).getId(), getClientId(request), getAccessToken(request), getSn(request));
        if (baseinfo != null) {
            List<SmartCard> cards = getSelectCard(request, baseinfo.getProductNameT());
            List<SmartCard> askcard = cards.stream().filter(card -> slot.equals(card.getName())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(askcard)) {
                //理论上同一个型号下一个卡片只有一次,为防止意外权益解决方案。取第一个卡片
                SmartCard card = askcard.get(0);

                if ("true".equals(card.getSelect())) {
                    //获取设备状态进行互斥判断 TODO
                    String mutualMsg = checkMutual(request);
                    if (StringUtils.isEmpty(mutualMsg)) {

                        JSONObject cmd = JSON.parseObject(card.getCmd());
                        //下发指令  折中方案，下发失败提示用户当前设备状态不支持
                        String usn = deviceUtil.propertyWriteWhitCallBack(
                                getControldevice(request).getId()
                                , getSn(request)
                                , getClientId(request)
                                , getAccessToken(request)
                                , cmd);
                        ju.hSet(IOTREADISFLG + usn, REQUEST, JSON.toJSONString(request));
                        ju.hSet(IOTREADISFLG + usn, PARAM, JSON.toJSONString(card));
                    } else {
                        content = mutualMsg;
                    }
                } else if ("false".equals(card.getSelect())) {
                    content = "冰箱的" + slot + "卡片未被选中，请去手机智家app中选择后再试";
                }

            } else {
                content = "对不起，您的冰箱不支持" + slot + "卡片场景哦，您可以试试别的";
            }
        }

        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * iot 回调
     *
     * @param request 请求
     * @param param   前序参数
     * @param iotok   iot回调执行状况
     */
    public void openSmartCardCB(RequestObj request, JSONObject param, boolean iotok) {
        //IOT下发成功之后调用 TODO
        SmartCard askCard = JSONObject.toJavaObject(param, SmartCard.class);

        String cardname = getSlot(request, CARDNAME);
        if (iotok && setCardModeImp(request, askCard)) {

            //调用推送接口下发结果
            deviceUtil.pushMessage(getUserId(request),
                    cardname + "卡片已打开",
                    getSn(request),
                    getDeviceId(request),
                    getAccessToken(request),
                    getClientId(request));

        } else {
            //调用推送接口下发结果
            deviceUtil.pushMessage(getUserId(request),
                    cardname + "卡片打开失败，当前的冰箱状态无法执行温度设置操作哦，请检查后重试",
                    getSn(request),
                    getDeviceId(request),
                    getAccessToken(request),
                    getClientId(request));
        }
    }

    /**
     * 获取卡片列表和当前mac的选中状态
     *
     * @param request 请求
     * @param mode    设备型号
     * @return 卡片列表
     */
    public List<SmartCard> getSelectCard(RequestObj request, String mode) {

        JSONObject json = new JSONObject();

        json.put("mac", getControldevice(request).getId());
        json.put("model", mode);


        String res = HttpUtil.postJsonWithHead(URL_REFRIGERATOR_SCENE + ACTION_QUERYMODELCARDLIST, json.toJSONString(), builderHeader(request, mode));
        log.info("查询型号下冰箱场景卡片列表接口=======================================" + res);
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("code") && "000000".equals(resJson.getString("code"))) {
                return JSON.parseArray(resJson.getString("data"), SmartCard.class);
            }

        }
        return new ArrayList<>();
    }


    /**
     * 构造统一head
     *
     * @param request 请求
     * @param mode    设备名 BCD***
     * @return
     */
    private Headers builderHeader(RequestObj request, String mode) {
        Headers.Builder builder = new Headers.Builder();


        builder.add("timestamp", DateUtils.getCurrentDateTime());
        builder.add("deviceCode", getControldevice(request).getId());
        builder.add("deviceModel", mode);
        //TODO 传了没用，不传报错
        builder.add("userCenterId", "X20");
        builder.add("uHomeId", getUserId(request));
        builder.add("appId", UtilConstants.APPID);
        builder.add("appKey", UtilConstants.APPKEY);
        builder.add("appVersion", "v1.0");
        builder.add("typeId", getControldevice(request).getWifiType());
        builder.add("accessToken", getAccessToken(request));
        builder.add("sign", SignUtil.getCardSceneSign(builder));
        log.info(builder.get("sign"));
        return builder.build();

    }


    /**
     * 判断互斥逻辑
     *
     * @param request 请求
     * @return 互斥触发播报 null则为未触发互斥
     */
    private String checkMutual(RequestObj request) {
        Map<String, String> deviceinfo = DeviceUtil.getDeviceInfo(getControldevice(request).getId(), getClientId(request), getAccessToken(request));
        switch (getControldevice(request).getWifiType()) {
            case TYPEID_521:
            default:
                return mutual521(deviceinfo);

        }
    }

    /**
     * 521判断互斥逻辑
     *
     * @param deviceinfo 云端状态
     * @return 互斥触发播报 null则为未触发互斥
     */
    private String mutual521(Map<String, String> deviceinfo) {
        if (deviceinfo.containsKey("babyCareStatus") && "true".equals(deviceinfo.get("babyCareStatus"))) {
            return "当前处于母婴模式中，无法设置变温室温度，请关闭后重试";
        }
        if (deviceinfo.containsKey("rareFoodKeepingStatus") && "true".equals(deviceinfo.get("rareFoodKeepingStatus"))) {
            return "当前处于珍品模式中，无法设置变温室温度，请关闭后重试";
        }
        return null;
    }

    /**
     * 下发卡片状态接口
     *
     * @param request   请求
     * @param smartCard 选中卡片信息
     * @return 下发是否成功
     */

    private boolean setCardModeImp(RequestObj request, SmartCard smartCard) {

        JSONObject json = new JSONObject();

        json.put("mac", getControldevice(request).getId());
        json.put("model", smartCard.getModel());
        json.put("cabin", smartCard.getCabin());
        json.put("id", smartCard.getId());
        //1:开启 0：关闭
        json.put("status", 1);

        log.info(json.toJSONString());
        String res = HttpUtil.postJsonWithHead(URL_REFRIGERATOR_SCENE + ACTION_SCENECARD_SET, json.toJSONString(), builderHeader(request, smartCard.getModel()));
        System.out.println(res);
        System.out.println("设置冰箱场景卡片状态接口=======================================");
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            return resJson.containsKey("code") && "000000".equals(resJson.getString("code"));

        }

        return false;
    }


}
