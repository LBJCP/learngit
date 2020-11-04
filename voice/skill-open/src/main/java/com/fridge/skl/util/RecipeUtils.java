package com.fridge.skl.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fridge.skl.model.recipe.SmartRecipeFuzzySearchResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.fridge.skl.util.URL_Constants.*;

@Slf4j
public class RecipeUtils {
    public static final String APP_ID = "recipe_client_fridge_speech";
    public static final String APP_SECRET = "4216ECF394F623F1B023E4F6A63386DC";
    public static RecipeAccessToken ACCESSTOKEN = new RecipeAccessToken();

    public static String getSign(String appId, String appSecret, String timestamp, String body) {
        appSecret = appSecret.trim();
        appSecret = appSecret.replaceAll("\"", "");
        if (body != null) {
            body = body.trim();
        }
        if (!body.equals("")) {
            body = body.replaceAll(" ", "");
            body = body.replaceAll("\t", "");
            body = body.replaceAll("\r", "");
            body = body.replaceAll("\n", "");
        }
        log.info("body:" + body);
        StringBuffer sb = new StringBuffer();
        sb.append(body).append(appId).append(appSecret).append(timestamp);
        System.out.println("加密字符串：" + sb.toString());
        MessageDigest md = null;
        byte[] bytes = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            bytes = md.digest(sb.toString().getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BinaryToHexString(bytes);
    }

    static String BinaryToHexString(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        String hexStr = "0123456789abcdef";
        for (int i = 0; i < bytes.length; i++) {
            hex.append(String.valueOf(hexStr.charAt((bytes[i] & 0xF0) >> 4)));
            hex.append(String.valueOf(hexStr.charAt(bytes[i] & 0x0F)));
        }
        System.out.println(hex.toString());
        return hex.toString();
    }

    public static String getAccessToken() {

        RecipeAccessToken token = null;
        if (ACCESSTOKEN != null && ACCESSTOKEN.getToken() != null
                && ACCESSTOKEN.getValidEndTime() >= new Date().getTime()) {
            token = ACCESSTOKEN;

        } else {
            log.info("获取token");
            try {

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(50, TimeUnit.SECONDS)//设置连接超时时间
                        .readTimeout(50, TimeUnit.SECONDS)//设置读取超时时间

                        .build();

                MediaType JSON = MediaType.parse("application/json");
                JSONObject json = new JSONObject();
                json.put("appId", APP_ID);
                json.put("appSecret", APP_SECRET);
                RequestBody body = RequestBody.create(JSON, json.toString());


                Request request = new Request.Builder()
                        .url(URL_RECIPE + ACTION_RECPIR_GETACCESSTOKEN)
                        .post(body)
                        .build();


                Response response = client.newCall(request).execute();

                if (response.code() == 200) {
                    JSONObject js = JSONObject.parseObject(response.body().string());
                    token = new RecipeAccessToken();
                    JSONObject data = js.getJSONObject("data");
                    token.setToken(data.getString("accessToken"));
                    //有效时间到期钱1小时刷新token
                    Long time = new Date().getTime() + data.getLong("expiresIn") - 1000 * 60 * 60;
                    token.setValidEndTime(time);
                    ACCESSTOKEN = token;
                    log.info("newtoken___" + ACCESSTOKEN.getToken());
                    log.info("tokentime___" + DateUtils.dateToDateTime(new Date(ACCESSTOKEN.getValidEndTime())));

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return token.getToken();

    }

    public static String postByAuthorization(String url, String json) {

        String timestamp = String.valueOf(new Date().getTime() / 1000);
        System.out.println(url);
        System.out.println(json);
        System.out.println(timestamp);
        String res = "";
        try {
            MediaType JSON = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(JSON, json);

            Request request = new Request.Builder()
                    .url(URL_RECIPE + url)
                    .post(body)
                    .header("Authorization", "Bearer " + getAccessToken())
                    .header("timestamp", timestamp)
                    .header("sign", getSign(APP_ID, APP_SECRET, timestamp, url + json))
                    .build();
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(50, TimeUnit.SECONDS)//设置连接超时时间
                    .readTimeout(50, TimeUnit.SECONDS)//设置读取超时时间
                    .build();

            Response response = client.newCall(request).execute();

            res = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;

    }


    public static void checkSign() {

        System.out.println(getSign(APP_ID, APP_SECRET, "1591758625591", "123456"));
    }


    public static void main(String[] args) {

        SmartRecipeFuzzySearchResponse response = new SmartRecipeFuzzySearchResponse();
        response.setCurrentPage(1);
        response.setFridgeType("");
        response.setMacId("");
        response.setPageSize(5);
        response.setUserId("");
        List<String> kws = new ArrayList<>();

        kws.add("菠菜");
        kws.add("牛肉");


        response.setKw(kws);

        List<String> equipmentType = new ArrayList<>();
        response.setEquipmentType(equipmentType);



        System.out.println(postByAuthorization(ACTION_RECPIR_UNIONRECIPEFUZZYSEARCH_V2, JSON.toJSONString(response)));



//        FindRecipeResponse response = new FindRecipeResponse();
//        response.setCurrentPage(1);
//        response.setFridgeType("");
//        response.setMacId("");
//        response.setPageSize(5);
//        response.setUserId("");
//
//        StringBuilder sb = new StringBuilder();
//
//        sb.append("猪肉");
//
//        response.setKw(sb.toString());
//
//        List<String> equipmentType = new ArrayList<>();
//        response.setEquipmentType(equipmentType);
//
//
//        System.out.println(postByAuthorization(ACTION_RECPIR_FINDRECIPE, JSON.toJSONString(response)));

    }
}
