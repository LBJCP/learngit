package com.fridge.skl.service;

import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.util.HttpUtil;
import com.fridge.skl.util.JsonUtil;
import okhttp3.Headers;
import org.springframework.stereotype.Service;

import static com.fridge.skl.util.URL_Constants.*;

@Service
public class IDService {
    public static final String APPID = "MB-SUAIVOICER-0000";
    public static final String CONTENTTYPE_JSON = "application/json";
    public static final String RETCOD_OK = "00000";

    /**
     * 获取用户中心id
     */
    public String getUserCenterID(String deviceId, String clientId, String accessToken) {

        JSONObject resJson = getUserCenterData(deviceId, clientId, accessToken);
        if (resJson != null && resJson.containsKey("userId")) {
            return resJson.getString("userId");
        }

        return null;
    }

    /**
     * 获取用户中心token
     */
    public String getUserCenterToken(String deviceId, String clientId, String accessToken) {

        JSONObject resJson = getUserCenterData(deviceId, clientId, accessToken);
        if (resJson != null && resJson.containsKey("accessToken")) {
            return resJson.getString("accessToken");
        }
        return null;
    }

    /**
     * 获取用户中心id和token
     */
    public JSONObject getUserCenterData(String deviceId, String clientId, String accessToken) {

        JSONObject json = new JSONObject();
        json.put("deviceType", 0);
        json.put("retType", 1);
        Headers.Builder builder = new Headers.Builder();

        builder.add("appId", APPID);
        builder.add("deviceId", deviceId);
        builder.add("Content-Type", CONTENTTYPE_JSON);
        builder.add("clientId", clientId);
        builder.add("accessToken", accessToken);

        String res = HttpUtil.postJsonWithHead(URL_DOT_ACCESS + ACTION_GETUSERCENTERID, json.toJSONString(), builder.build());
        if (!"".equals(res) && JsonUtil.isJsonObject(res)) {
            JSONObject resJson = JSONObject.parseObject(res);
            if (resJson.containsKey("retCode") && RETCOD_OK.equals(resJson.getString("retCode"))) {
                if (resJson.containsKey("data") && resJson.getJSONObject("data") != null) {
                    return resJson.getJSONObject("data");
                }
            }
        }

        return null;
    }

}
