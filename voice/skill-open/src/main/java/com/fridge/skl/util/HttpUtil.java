package com.fridge.skl.util;


import com.alibaba.fastjson.JSONObject;


import okhttp3.*;


import javax.xml.crypto.Data;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.fridge.skl.util.DateUtils.DATE_TIME_FORMAT;


public class HttpUtil {

    public static String CONTENTTYPE_JSON = "application/json";
    public static String CLIENT_ID = "unilife_standard_api_haieryinxiang";
    public static String CLIENT_SECRET = "unilife_standard_api_haieryinxiang_123456";

    public static String getToken() {

        String token = "";
        String validEndTime;
        if ( UtilConstants.token.getToken() != null &&UtilConstants.token.getValidEndTime().after(new Date())) {
          //  System.out.println("old" +UtilConstants.token.getToken());
            token = UtilConstants.token.getToken();

        } else {

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .build();

            FormBody formBody = new FormBody.Builder()
                    .add("client_id", CLIENT_ID)
                    .add("client_secret", CLIENT_SECRET)
                    .build();

            final Request request = new Request.Builder()
                    .url("http://api.unilifemedia.com/service/access?&grant_type=client_credentials")
                    .post(formBody)
                    .build();


            Call call = okHttpClient.newCall(request);


            try {
                Response response = call.execute();
                if (response.code() == 200) {
                    JSONObject js = JSONObject.parseObject(response.body().string());
                    token = js.getString("access_token");
                    validEndTime = js.getString("validEndTime");
                    Date validtime= DateUtils.stringToDate(validEndTime,DATE_TIME_FORMAT);
//                    UtilConstants.tokenMap.put("token", token);
//                    UtilConstants.tokenMap.put("validEndTime", validtime);
                    UtilConstants.token.setToken(token);
                    UtilConstants.token.setValidEndTime(validtime);
//                    System.out.println("new" +UtilConstants.token.getToken());
//                    System.out.println("new" +  UtilConstants.token.getValidEndTime());

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        return token;

    }

    public static String postJson(String url, String json) {


        String res = "";
        try {
            MediaType JSON
                    = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(JSON, json);


            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
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

    public static String postByAuthorization(String url, String json, String token) {


        String res = "";
        try {
            MediaType JSON
                    = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(JSON, json);


            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .header("Authorization", "Bearer " + token)
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


    public static String postJsonWithHead(String url, String json, Headers headers) {


        String res = "";
        try {
            MediaType JSON
                    = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(JSON, json);


            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .headers(headers)
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

    public static String getJsonWithHead(String url, Headers headers) {


        String res = "";
        try {
            MediaType JSON
                    = MediaType.parse("application/json");

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .headers(headers)
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


    /**
     * SHA-256加密
     *
     * @param data 待加密数据
     * @return byte[] 消息摘要
     * @throws Exception
     */
    public static byte[] encodeSHA256(byte[] data) throws Exception {
        // 初始化MessageDigest
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        // 执行消息摘要
        return md.digest(data);
    }

    /**
     * 功能描述:byte转十六进制,并输出32位字符串
     *
     * @param bytes
     * @return
     */
    public static String binaryToHexString(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        String hexStr = "0123456789abcdef";
        for (int i = 0; i < bytes.length; i++) {
            hex.append(String.valueOf(hexStr.charAt((bytes[i] & 0xF0) >> 4)));
            hex.append(String.valueOf(hexStr.charAt(bytes[i] & 0x0F)));
        }
        return hex.toString().substring(16, 48);
    }

    public static void main(String[] args) throws Exception {
        String appKey = "MB-SJHZX-0000";
        String clientid = "C8631420CC31";
        String timestamp = "1570773326252";
        System.out.println(binaryToHexString(encodeSHA256((appKey + clientid + timestamp).getBytes())));


    }

}
