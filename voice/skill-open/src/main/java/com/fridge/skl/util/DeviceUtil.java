package com.fridge.skl.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fridge.skl.model.iot.DeviceBaseInfo;
import com.fridge.skl.model.iotmodel.DeviceInfo;
import okhttp3.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

import static com.fridge.skl.service.IDService.RETCOD_OK;
import static com.fridge.skl.util.URL_Constants.*;

/**
 * @author zhangn
 * @Description
 * @createTime 2020/3/28
 */
@Component
public class DeviceUtil {
    private static final String APPID = "MB-UZHSH-0000";
    private static final String APPKEY = "f50c76fbc8271d361e1f6b5973f54585";

    private static final String PUSHAPPID = "3839472b-a7e4-4b9b-addc-07ff7367378c";
    private static final String PUSHSYSSECRET = "$1$HACZ230J$MKDC/GJ.EPPINDGQA5DAC1";

    @Autowired
    JedisUtils ju;
    private static int sequence = 1;

    private final static Logger logger = LoggerFactory
            .getLogger(DeviceUtil.class);


       /*  deviceRegister("TGT1C3DEB20F8E162SWJ1A49RUYXT0","C8631422CCC3");

        String mac = "C8631422CCC3";
        String accessToken = "TGT1C3DEB20F8E162SWJ1A49RUYXT0";

        JSONObject body = new JSONObject();


        JSONArray toClients = new JSONArray();
        JSONObject client = new JSONObject();
        client.put("clientId", mac.toUpperCase());
        client.put("appId", "MB-UZHSH-0000");
        toClients.add(client);
        body.put("toClients", toClients);


        JSONObject options = new JSONObject();
        options.put("msgName", "sendDelFood");
        options.put("businessType", 1);
        options.put("priority", 1);
        options.put("expires", 60);


        JSONObject message = new JSONObject();
        message.put("version", "v3");
        message.put("options", options);
        body.put("message", message);


        body.put("tag", "fridgeserver");
        body.put("templateId", 1);


        LinkedHashMap templateParams = new LinkedHashMap();
        templateParams.put("STUFF_ID", 1);
        templateParams.put("ALERT_SWITCH", 0);
        templateParams.put("ALERT_YEAR", 0);
        templateParams.put("ALERT_MONTH", 0);
        templateParams.put("ALERT_DAY", 0);
        templateParams.put("ALERT_1_HOUR", 0);
        templateParams.put("ALERT_1_MINUTE", 0);
        templateParams.put("ALERT_1_FREQ", 0);
        templateParams.put("ALERT_2_HOUR", 0);
        templateParams.put("ALERT_2_MINUTE", 0);
        templateParams.put("ALERT_2_FREQ", 0);
        templateParams.put("ALERT_3_HOUR", 0);
        templateParams.put("ALERT_3_MINUTE", 0);
        templateParams.put("ALERT_3_FREQ", 0);
        templateParams.put("ALERT_4_HOUR", 0);
        templateParams.put("ALERT_4_MINUTE", 0);
        templateParams.put("ALERT_4_FREQ", 0);

        body.put("templateParams", templateParams);
        DeviceUtil.send8C(body.toJSONString(), accessToken, mac);*/

//        Map cmdArgs = new HashMap();
//        cmdArgs.put("refrigeratorTargetTempLevel", "5");
//        sendCmd(cmdArgs, "TGT229J0WBCTJ8HX204ZZFB3ZSJK20", "04FA83A4A6FA");
//
//        getFridgeStatusByMac("TGT229J0WBCTJ8HX204ZZFB3ZSJK20", "04FA83A4A6FA");


//
//    /**
//     * 终端设备注册
//     *
//     * @param accessToken
//     * @param mac
//     * @return
//     */
//    public static String deviceRegister(String accessToken, String mac) {
//
//
//        String url = "https://uws.haier.net/ums/v3/account/register";
//        String body = "{\"channel\":5,\"pushId\":\"udse0101" + mac + "\",\"devAlias\":\"qdfridge\",\"msgVersion\":\"v3\"}";
//
//
//        Map<String, String> header = new HashMap<String, String>();
//        header.put("appId", "MB-QDHAIER-0000");
//        header.put("appKey", "61940684a201dc4650b4275ee4d28bfe");
//        header.put("appVersion", "1.0.0");
//        header.put("clientId", CommonUtil.getMac(mac));
//        header.put("accessToken", accessToken);
//        header.put("sequenceId", CommonUtil.gernateRanNum());
//        long currentTimeMillis = System.currentTimeMillis();
//        header.put("sign", CommonUtil.getSign("MB-QDHAIER-0000", "61940684a201dc4650b4275ee4d28bfe", String.valueOf(currentTimeMillis), body,
//                url));
//        header.put("timestamp", String.valueOf(currentTimeMillis));
//        header.put("language", "zh-cn");
//        header.put("timezone", "Asia/Shanghai");
//        header.put("Content-Type", "application/json;charset=UTF-8");
//
//
//        logger.info("请求设备注册 header:" + header);
//        logger.info("请求设备注册 body:" + body);
//
//
//        String result = OkHttpUtil.postJson(url, body, header);
//        logger.info("请求设备注册 result:" + result);
//        return result;
//    }


//    public static String send8C(String body, String accessToken, String mac) {
//
//        String url = "https://uws.haier.net/ums/v3/msg/pushWithTmplByClients";
//
//        Map<String, String> header = new HashMap<String, String>();
//        header.put("appId", "MB-QDHAIER-0000");
//        header.put("appKey", "61940684a201dc4650b4275ee4d28bfe");
//        header.put("appVersion", "1.0.0");
//        header.put("sequenceId", CommonUtil.gernateRanNum());
//        header.put("accessToken", accessToken);
//        header.put("clientId", CommonUtil.getMac(mac));
//        long currentTimeMillis = System.currentTimeMillis();
//        header.put("sign", SignUtil.getSign("MB-QDHAIER-0000", "61940684a201dc4650b4275ee4d28bfe", String.valueOf(currentTimeMillis), body,
//                url));
//        header.put("timestamp", String.valueOf(currentTimeMillis));
//        header.put("language", "zh-cn");
//        header.put("timezone", "Asia/Shanghai");
//        header.put("Content-Type", "application/json;charset=UTF-8");
//
//        logger.info("8C下发 header:" + header);
//        logger.info("8C下发 body:" + body);
//        String result = OkHttpUtil.postJson(url, body, header);
//
//        logger.info("8C下发 result:" + result);
//
//
//        return result;
//
//
//    }


    /**
     * 控制温度命令
     */
    public static String sendCmd(Map cmdArgs, String accessToken, String mac) {

        String url = "https://uws.haier.net/stdudse/v1/modfier/operate";


        Headers.Builder header = new Headers.Builder();


        String appId = "MB-QDHAIER-0000";
        String appKey = "61940684a201dc4650b4275ee4d28bfe";
        header.add("appId", appId);
        header.add("appKey", appKey);
        header.add("appVersion", "1.0.0");
        header.add("sequenceId", CommonUtil.gernateRanNum());
        header.add("accessToken", accessToken);
        header.add("clientId", "99:99:99:99:99:99:99:99:99");
        long currentTimeMillis = System.currentTimeMillis();

        JSONObject body = new JSONObject();
        body.put("cmdArgs", cmdArgs);
        body.put("deviceId", mac.toUpperCase());

        header.add("sign", SignUtil.getSign(appId, appKey, String.valueOf(currentTimeMillis), body.toJSONString(),
                url));
        header.add("timestamp", String.valueOf(currentTimeMillis));
        header.add("language", "zh-cn");
        header.add("timezone", "Asia/Shanghai");
        header.add("Content-Type", "application/json;charset=UTF-8");

        String result = HttpUtil.postJsonWithHead(url, body.toJSONString(), header.build());

        logger.info("控制命令下发 result:" + result);


        return result;


    }

    public static String getFridgeStatusByMac(String accessToken, String mac) {

        String url = "https://uws.haier.net/uds/v1/protected/{deviceId}/lastReportStatus";
        url = url.replace("{deviceId}", mac.toUpperCase());
        Headers.Builder header = new Headers.Builder();


        String appId = "MB-QDHAIER-0000";
        String appKey = "61940684a201dc4650b4275ee4d28bfe";
        header.add("appId", appId);
        header.add("appKey", appKey);
        header.add("appVersion", "1.0.0");
        header.add("sequenceId", CommonUtil.gernateRanNum());
        header.add("accessToken", accessToken);
        header.add("clientId", "99:99:99:99:99:99:99:99:99");
        long currentTimeMillis = System.currentTimeMillis();

        header.add("sign", SignUtil.getSign(appId, appKey, String.valueOf(currentTimeMillis), "",
                url));
        header.add("timestamp", String.valueOf(currentTimeMillis));
        header.add("language", "zh-cn");
        header.add("timezone", "Asia/Shanghai");
        header.add("Content-Type", "application/json;charset=UTF-8");

        String result = HttpUtil.postJsonWithHead(url, "", header.build());

        logger.info("getFridgeStatusByMac result:" + result);


        return result;


    }

    /**
     * 获取当前用户所有设备列表   2020.08 技能平台上线设备列表和设备选择 功能逐渐废弃
     * typeids 为空则搜索所有冰箱
     */
    public static DeviceInfo getDeviceList(String deviceId, String clientid, String accessToken, String[] typeids) {
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
                            if (typeids != null && typeids.length != 0) {
                                for (int j = 0; j < typeids.length; j++) {
                                    if (deviceInfo.getWifiType().equals(typeids[j]) && deviceInfo.isOnline()) {
                                        return deviceInfo;
                                    }
                                }
                            } else {
                                if ("01".equals(deviceInfo.getDeviceType().substring(0, 2)) && deviceInfo.isOnline()) {
                                    return deviceInfo;
                                }
                            }
                            // getDeviceInfo(deviceInfo.getDeviceId(), clientid, accessToken);
                        }
                    }

                }
            }
        }


        return null;
    }

    /**
     * 获取设备状态
     */
    public static Map<String, String> getDeviceInfo(String deviceId, String clientid, String accessToken) {
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
        String sign = SignUtil.getSign(APPID, APPKEY, datatime.toString(), "", URL_IOT_ACCESS + String.format(ACTION_LASTREPORTSTATUS, deviceId));
        builder.add("sign", sign);
        builder.add("timestamp", datatime.toString());
        builder.add("language", "zh-cn");
        builder.add("timezone", "Asia/Shanghai");
        builder.add("appKey", APPKEY);


        builder.add("Content-Type", "application/json;charset=UTF-8");

        String res = HttpUtil.getJsonWithHead(URL_IOT_ACCESS + String.format(ACTION_LASTREPORTSTATUS, deviceId), builder.build());
        System.out.println("获取设备状态------->" + res);
        if (!"".equals(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("retCode") && RETCOD_OK.equals(resJson.getString("retCode"))) {
                if (resJson.containsKey("deviceStatus")) {
                    JSONObject devicestatus = resJson.getJSONObject("deviceStatus");
                    if (devicestatus.containsKey("statuses")) {
                        return JSONObject.parseObject(devicestatus.getJSONObject("statuses").toJSONString(), new TypeReference<Map<String, String>>() {
                        });
                    }
                }
            }
        }
        return null;
    }


    /**
     * 获取设备型号信息
     *
     * @param deviceId
     * @param clientid
     * @param accessToken
     * @return
     */
    public static DeviceBaseInfo getDeviceBaseInfo(String deviceId, String clientid, String accessToken, String sn) {

        JSONObject json = new JSONObject();
        json.put("deviceId", deviceId);
        Headers.Builder builder = new Headers.Builder();

        builder.add("appId", APPID);
        builder.add("version", "0.3");
        builder.add("clientId", clientid);
        builder.add("accessToken", accessToken);

        builder.add("sequenceId", sn);

        Long datatime = new Date().getTime();
        String sign = SignUtil.getSign(APPID, APPKEY, datatime.toString(), json.toJSONString(), URL_IOT_ACCESS + ACTION_GETBASEINFO);
        builder.add("sign", sign);
        builder.add("timestamp", datatime.toString());
        builder.add("language", "zh-cn");
        builder.add("timezone", "Asia/Shanghai");
        builder.add("appKey", APPKEY);


        builder.add("Content-Type", "application/json;charset=UTF-8");

        String res = HttpUtil.postJsonWithHead(URL_IOT_ACCESS + ACTION_GETBASEINFO, json.toJSONString(), builder.build());
        System.out.println(res);
        System.out.println("获取设备型号信息状=======================================");
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("retCode") && RETCOD_OK.equals(resJson.getString("retCode"))) {
                if (resJson.containsKey("deviceBaseInfo")) {
                    return JSON.parseObject(resJson.getString("deviceBaseInfo"), DeviceBaseInfo.class);
                }
            }
        }
        return null;
    }


    public static String propertyWrite(String deviceId, String clientid, String accessToken, Map cmdArgs) {

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

        JSONObject json = new JSONObject();
        json.put("deviceId", deviceId);
        json.put("sn", "sn" + sequenceId);
        json.put("cmdArgs", cmdArgs);

        json.put("callbackUrl", "");
        json.put("accessToken", accessToken);

        builder.add("sequenceId", sequenceId);

        Long datatime = new Date().getTime();
        String sign = SignUtil.getSign(APPID, APPKEY, datatime.toString(), json.toJSONString(), URL_IOT_ACCESS + ACTION_OPERATE);
        builder.add("sign", sign);
        builder.add("timestamp", datatime.toString());
        builder.add("language", "zh-cn");
        builder.add("timezone", "Asia/Shanghai");
        builder.add("appKey", APPKEY);
        builder.add("Content-Type", "application/json;charset=UTF-8");

        String res = HttpUtil.postJsonWithHead(URL_IOT_ACCESS + ACTION_OPERATE, json.toJSONString(), builder.build());
        System.out.println(res);
        System.out.println("=======================================");
        if (!"".equals(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("retCode") && RETCOD_OK.equals(resJson.getString("retCode"))) {
                if (resJson.containsKey("usn")) {
                    return resJson.getString("usn");
                }
            }
        }
        return "";
    }

    public String propertyWriteWhitCallBack(String deviceId, String sn, String clientid, String accessToken, JSONObject cmdArgs) {

        Headers.Builder builder = new Headers.Builder();

        builder.add("appId", APPID);
        builder.add("version", "0.3");
        builder.add("clientId", clientid);
        builder.add("accessToken", accessToken);

        JSONObject json = new JSONObject();
        json.put("deviceId", deviceId);

        json.put("cmdArgs", cmdArgs);
        logger.info(json.toJSONString());
        //   本地环境 json.put("callbackUrl", "http://g2c0129659.qicp.vip/iotcallback/call");
        json.put("callbackUrl", "http://aivoice.linkcook.cn/iotcallback/call");
        json.put("accessToken", accessToken);

        builder.add("sequenceId", sn);

        Long datatime = new Date().getTime();
        String sign = SignUtil.getSign(APPID, APPKEY, datatime.toString(), json.toJSONString(), URL_IOT_ACCESS + ACTION_OPERATE);
        builder.add("sign", sign);
        builder.add("timestamp", datatime.toString());
        builder.add("language", "zh-cn");
        builder.add("timezone", "Asia/Shanghai");
        builder.add("appKey", APPKEY);
        builder.add("Content-Type", "application/json;charset=UTF-8");

        String res = HttpUtil.postJsonWithHead(URL_IOT_ACCESS + ACTION_OPERATE, json.toJSONString(), builder.build());
        System.out.println(res);
        System.out.println("=======================================");
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("retCode") && RETCOD_OK.equals(resJson.getString("retCode"))) {
                if (resJson.containsKey("usn")) {
                    return resJson.getString("usn");
                }
            }
        }
        return "";
    }

    public static void main(String[] args) {

        JSONObject jo = new JSONObject();
        jo.put("appKey", PUSHAPPID);
        jo.put("timestamp", System.currentTimeMillis());
        jo.put("userId", "34480613");
        jo.put("pushInfo", "您要做的北京烤鸭已经做完了");
        jo.put("pushCode", "D0001");
        jo.put("messageType", "1");
        jo.put("sn", "20200907164632830000342930");
        jo.put("requestDeviceId", "C86314202FB9");
        jo.put("accessToken", "TGT2F8IENR7YS15M2W30HR3S02P9B0");
        jo.put("clientId", "FC746CFD-9FB6-4FC0-BEE8-2BBFD3F30432");

        JSONObject sourceInfo = new JSONObject();
        sourceInfo.put("deviceId", "04FA832ADD19");
        sourceInfo.put("wifiType", "200861051c408504012200618005094600000000000061800541420000000040");
        sourceInfo.put("deviceType", "Fridge");
        jo.put("sourceInfo", sourceInfo);

        JSONObject nlpInfo = new JSONObject();
        nlpInfo.put("domain", "Fridge");
        nlpInfo.put("intent", "setTemperature");
        jo.put("nlpInfo", nlpInfo);

        String sign = SignUtil.getPushSign(jo, PUSHSYSSECRET);
        jo.put("sign", sign);
        System.out.println(jo.toJSONString());

        String res = HttpUtil.postJson("https://www.haigeek.com/developerskill/pushMsg", jo.toJSONString());
        System.out.println(res);
        System.out.println("=======================================");


    }

    public boolean pushMessage(String userid, String message, String sn, String mac, String token, String clientId) {
        JSONObject jo = new JSONObject();
        jo.put("appKey", PUSHAPPID);
        jo.put("timestamp", System.currentTimeMillis());
        jo.put("userId", userid);
        jo.put("pushInfo", message);
        jo.put("pushCode", "D0001");
        jo.put("messageType", "1");
        jo.put("sn", sn);
        jo.put("requestDeviceId", mac);
        jo.put("accessToken", token);
        jo.put("clientId", clientId);

//        JSONObject sourceInfo = new JSONObject();
//        sourceInfo.put("deviceId", "04FA832ADD19");
//        sourceInfo.put("wifiType", "200861051c408504012200618005094600000000000061800541420000000040");
//        sourceInfo.put("deviceType", "Fridge");
//        jo.put("sourceInfo", sourceInfo);
//
//        JSONObject nlpInfo = new JSONObject();
//        nlpInfo.put("domain", "Fridge");
//        nlpInfo.put("intent", "setTemperature");
//        jo.put("nlpInfo", nlpInfo);

        String sign = SignUtil.getPushSign(jo, PUSHSYSSECRET);
        jo.put("sign", sign);
//        System.out.println(jo.toJSONString());

        String res = HttpUtil.postJson(URL_PUSH_MESSAGE + ACTION_PUSHMESSAGE, jo.toJSONString());
        System.out.println(res);
        System.out.println("推送调用=======================================");
        if (!StringUtils.isEmpty(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            return resJson.containsKey("retCode") && RETCOD_OK.equals(resJson.getString("retCode"));
        }

        return false;
    }


}
