package com.fridge.skl.service;

import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.model.RequestObj;
import com.fridge.skl.model.ResponseObj;
import com.fridge.skl.model.coffee.CoffeeStyle;
import com.fridge.skl.model.context.ControlDevices;
import com.fridge.skl.model.context.ExtendDevice;
import com.fridge.skl.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.fridge.skl.util.URL_Constants.*;

@Service
@Slf4j
public class ColdextractionService extends BaseService {

    private static final String TYPEID_1 = "201c120024000810012300618001214b00000000000061800346f00000000040";
    private static final String TYPEID_2 = "200861051c408504012100618006754500000000000061800351440000000040";

    private static final String TYPEID_3 = "200861051c4085040122c4b950b6230000002ba9b5f29864baad8175bee8ee40";
    private static final String TYPEID_4 = "200861051c408504012100618005255500000000000060859383000000000040";
    //变温执行type
    private static final String[] typeid = new String[]{
            TYPEID_1, TYPEID_2
    };
    //冷藏执行type
    private static final String[] typeid2 = new String[]{
            TYPEID_3, TYPEID_4
    };

    //全部可执行type
    private static final String[] typeid3 = new String[]{
            TYPEID_1, TYPEID_2, TYPEID_3, TYPEID_4
    };

    /**
     * 开始冷萃
     */
    public ResponseObj startCold(RequestObj request) {
        ResponseObj resp = initResporse(request);

        String content;

        ControlDevices selectdevice = matchDevice(request, typeid);
        ControlDevices selectdevice2 = matchDevice(request, typeid2);
        String clientid = getClientId(request);
        String accessToken = getAccessToken(request);
        String typeflg = "";
        //是否下发的iot操作的flg
        boolean sendflg = true;
        String selectDeviceId = "";
        //根据不同的typeid后续处理不同。
        if (selectdevice != null) {
            //变温室
            typeflg = "1";
            selectDeviceId = selectdevice.getId();
        } else if (selectdevice2 != null) {
            //冷藏室
            typeflg = "2";
            selectDeviceId = selectdevice2.getId();
        }

        int tempnow = 0;
        if (!StringUtils.isEmpty(typeflg)) {
            String modeNow = "";
            CoffeeStyle coffeeStyle = getCoffeeExtractDetail(request);
            if (coffeeStyle != null) {
                if (0 == coffeeStyle.getExtractStatus()) {

                    modeNow = "冷萃模式";
                    sendflg = false;
                }
            }

            Map<String, String> itemmap = DeviceUtil.getDeviceInfo(selectDeviceId, clientid, accessToken);

            if (!CollectionUtils.isEmpty(itemmap)) {
                if (itemmap.containsKey("intelligenceMode") && "true".equals(itemmap.get("intelligenceMode"))) {
                    modeNow = "智能模式";
                    sendflg = false;
                }
                if (itemmap.containsKey("travelMode") && "true".equals(itemmap.get("travelMode"))) {
                    modeNow = "出行模式";
                    sendflg = false;
                }
                if (itemmap.containsKey("quickRefrigeratingMode") && "true".equals(itemmap.get("quickRefrigeratingMode"))) {
                    modeNow = "速冷模式";
                    sendflg = false;
                }

                if (itemmap.containsKey("refrigeratorTargetTempLevel") && "2".equals(typeflg)) {
                    tempnow = Integer.parseInt(itemmap.get("refrigeratorTargetTempLevel"));
                }
                if (itemmap.containsKey("vtRoomTargetTempLevel") && "1".equals(typeflg)) {
                    tempnow = Integer.parseInt(itemmap.get("refrigeratorTargetTempLevel"));
                }

            }

            boolean runflg = false;
            if (sendflg) {
                if ("2".equals(typeflg)) {
                    //冷藏下发无模式互斥的状态下执行下发

                    Map<String, String> prop = new HashMap<>();
                    prop.put("refrigeratorTargetTempLevel", "5");
                    String usn = DeviceUtil.propertyWrite(selectDeviceId, clientid, accessToken, prop);
                    if (!StringUtils.isEmpty(usn)) {
                        runflg = oneShotCold(request,
                                "{\"refrigeratorTargetTempLevel\":\"" + tempnow + "\"}",
                                "{\"refrigeratorTargetTempLevel\":\"5\"}"
                        );
                    }


                } else if (typeflg.equals("1")) {

                    Map<String, String> prop = new HashMap<>();
                    prop.put("vtRoomTargetTempLevel", "35");
                    String usn = DeviceUtil.propertyWrite(selectDeviceId, clientid, accessToken, prop);
                    if (!StringUtils.isEmpty(usn)) {
                        runflg = oneShotCold(request,
                                "{\"vtRoomTargetTempLevel\":\"" + tempnow + "\"}",
                                "{\"vtRoomTargetTempLevel\":\"5\"}"
                        );
                    }
                }
                if (runflg) {
                    content = "冷萃模式已开启，温度将调至4摄氏度，10小时后完成冷萃。";
                } else {
                    content = "小优费了半天劲还是没有启动冷萃模式，要不你帮我检查下是什么原因";
                }
            } else {
                if ("冷萃模式".equals(modeNow)) {
                    content = "当前有一个冷萃任务正在进行中，请结束后再试。";
                } else {
                    content = "当前处于" + modeNow + "，无法开启冷萃哦，请关闭后重试";
                }
            }

        } else {
            content = "对不起，您的家里没有支持冷萃咖啡的冰箱哦，赶紧买一台吧";
        }
        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 结束冷萃
     */
    public ResponseObj stopCold(RequestObj request) {
        ResponseObj resp = initResporse(request);

        String content;
        ControlDevices selectdevice = matchDevice(request, typeid3);
        if (selectdevice != null) {
            CoffeeStyle coffeeStyle = getCoffeeExtractDetail(request);
            if (coffeeStyle != null && coffeeStyle.getExtractStatus() == 0) {
                //调用终止冷萃
                if (terminateCoffeeExtractMode(request, coffeeStyle.getId())) {
                    content = "已经结束当前的冷萃任务。";
                } else {
                    content = "小优也不知道为什么终止冷萃失败了，让我等会再试一下";
                }
            } else if (coffeeStyle != null && coffeeStyle.getExtractStatus() == 1) {
                if (completeDrink(request, coffeeStyle.getId())) {

                    content = "已经结束当前的冷萃任务。";
                } else {
                    content = "小优也不知道为什么终止冷萃失败了，让我等会再试一下";
                }

            } else {
                content = "小优查了一下，当前未处于冷萃模式中";

            }

        } else {


            content = "对不起，您的家里没有支持冷萃咖啡的冰箱哦，赶紧买一台吧";
        }


        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 查询冷萃
     */
    public ResponseObj queryCold(RequestObj request) {
        ResponseObj resp = initResporse(request);

        String content = "daikaifa";
        ControlDevices selectdevice = matchDevice(request, typeid3);
        if (selectdevice != null) {
            CoffeeStyle coffeeStyle = getCoffeeExtractDetail(request);
            if (coffeeStyle != null && coffeeStyle.getExtractStatus() == 0) {
                long time = coffeeStyle.getTimeToCompleteSecond() / 60;
                long min = time % 60;
                long Hour = time / 60;
                if (Hour == 0L) {
                    if (min == 0) {
                        content = "距离冷萃完成时间小于1分钟，冷萃即将完成。";
                    } else {
                        content = "距离冷萃完成预计还有" + min + "分钟，请耐心等待。";
                    }
                } else {
                    if (min == 0) {
                        content = "距离冷萃完成预计还有" + Hour + "小时整，请耐心等待。";
                    } else {
                        content = "距离冷萃完成预计还有" + Hour + "小时" + min + "分钟，请耐心等待。";
                    }
                }

            } else if (coffeeStyle != null && coffeeStyle.getExtractStatus() == 1) {

                content = "冷萃已完成，请尽快享用吧。";
            } else {
                content = "抱歉，没有查询到正在执行的冷萃任务。";
            }
        } else {
            content = "对不起，您的家里没有支持冷萃咖啡的冰箱哦，赶紧买一台吧";
        }

        resp.getResponse().getSpeech().setContent(content);

        return resp;
    }

    /**
     * 一键解冻接口调用
     */
    public boolean oneShotCold(RequestObj request, String pretmp, String nowtmp) {
        JSONObject header = builderHeader(request);
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();

        data.put("coffeeTypeId", "1");
        data.put("bakingDegree", "1");
        data.put("temperature", "4");
        data.put("hour", "10");
        data.put("prevCmdInfo", pretmp);
        data.put("nowCmdInfo", nowtmp);

        json.put("header", header);
        json.put("data", data);


        String res = HttpUtil.postJson(URL_COFFIC_TEST + ACTION_ONESHOTCOLD, json.toJSONString());
        System.out.println(res);
        System.out.println("一键解冻接口调用=======================================");
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            return resJson.containsKey("code") && "0".equals(resJson.getString("code"));

        }
        return false;
    }

    /**
     * 通过设备device获取设备型号
     */
    private String getDeviceNameFromList(RequestObj request) {
        ExtendDevice[] devices = request.getContext().getExtendApi().getQueryAllDevices().getData();
        for (ExtendDevice device : devices) {
            if (device.getDeviceId().equals(getControldevice(request).getId())) {
                return device.getProductNameT();
            }
        }
        return "";
    }

    /**
     * 查询冷萃进行状态
     */
    private CoffeeStyle getCoffeeExtractDetail(RequestObj request) {
        JSONObject header = builderHeader(request);
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();

        json.put("header", header);
        json.put("data", data);

        String res = HttpUtil.postJson(URL_COFFIC_TEST + ACTION_QUERYCOLD, json.toJSONString());
        System.out.println(res);
        System.out.println("查询冷萃进行状态=======================================");
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("code") && "0".equals(resJson.get("code"))
                    && resJson.containsKey("data") && null != resJson.getJSONObject("data")) {
                return JSONObject.parseObject(resJson.getJSONObject("data").toJSONString(), CoffeeStyle.class);
            }

        }
        return null;
    }

    /**
     * 构造统一head
     */
    private JSONObject builderHeader(RequestObj request) {
        JSONObject json = new JSONObject();
        json.put("timestamp", DateUtils.getCurrentDateTime());
        json.put("deviceCode", getControldevice(request).getId());
        json.put("deviceModel", getDeviceNameFromList(request));
        //TODO 传了没用，不传报错
        json.put("userCenterId", "X20");
        json.put("uHomeId", getUserId(request));
        json.put("appId", UtilConstants.APPID);
        json.put("appKey", UtilConstants.APPKEY);
        json.put("appVersion", "v1.0");
        json.put("typeId", getControldevice(request).getWifiType());
        json.put("accessToken", getAccessToken(request));
        json.put("sourceType", "2");
        json.put("sign", SignUtil.getIceSceneSign(json));
        return json;

    }

    /**
     * 3.1.5	终止冷萃模式接口
     */
    private boolean terminateCoffeeExtractMode(RequestObj request, Long id) {

        JSONObject header = builderHeader(request);
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("id", id);
        json.put("header", header);
        json.put("data", data);


        String res = HttpUtil.postJson(URL_COFFIC_TEST + ACTION_STOPCOLD, json.toJSONString());
        System.out.println(res);
        System.out.println("终止冷萃模式接口=======================================");
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            return resJson.containsKey("code") && "0".equals(resJson.get("code"));

        }
        return false;
    }

    /**
     * 3.1.6	  饮用完成接口
     */
    private boolean completeDrink(RequestObj request, Long id) {

        JSONObject header = builderHeader(request);
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("id", id);
        json.put("header", header);
        json.put("data", data);

        String res = HttpUtil.postJson(URL_COFFIC_TEST + ACTION_FINISH_DRINK, json.toJSONString());
        System.out.println(res);
        System.out.println("饮用完成=======================================");
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            return resJson.containsKey("code") && "0".equals(resJson.get("code"));

        }
        return false;
    }

}
