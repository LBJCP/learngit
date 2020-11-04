package com.fridge.skl.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Slf4j
public class SignUtil {
    public static String getSign(String appId, String appKey, String timestamp, String body, String url) {
        URL urlObj = null;
        try {
            urlObj = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        url = urlObj.getPath();
        appKey = appKey.trim();
        appKey = appKey.replaceAll("\"", "");
        if (body != null) {
            body = body.trim();
        }
        if (!body.equals("")) {
            body = body.replaceAll(" ", "");
            body = body.replaceAll("\t", "");
            body = body.replaceAll("\r", "");
            body = body.replaceAll("\n", "");
        }
//        log.info("body:" + body);
        StringBuffer sb = new StringBuffer();
        sb.append(url).append(body).append(appId).append(appKey).append(timestamp);

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
        return hex.toString();
    }

    public static void main(String[] args) throws MalformedURLException {
        System.out.println(getSign("MB-UZHSH-0000", "f50c76fbc8271d361e1f6b5973f54585", "1583205672000", "", "https://uws.haier.net/uds/v1/protected/deviceinfos"));


    }
//    public static String getUnlifeSign(){
//
//    }



    /**
     * 冷萃场景签名
     *
     * @param json 请求头内部
     * @return 返回签名字符串
     */
    public static String getIceSceneSign(JSONObject json) {
        String source = String.join("|", json.getString("userCenterId"),json.getString("uHomeId"),
                json.getString("deviceCode"),  json.getString("appVersion"), json.getString("timestamp"));
        return getMD5Str(source);
    }

    /**
     * 冰箱卡片签名
     *
     * @param builder 请求头内部
     * @return 返回签名字符串
     */
    public static String getCardSceneSign(Headers.Builder builder) {
        String source = String.join("|", builder.get("userCenterId"),builder.get("uHomeId"),
                builder.get("deviceCode"),  builder.get("appVersion"), builder.get("timestamp"));
        return getMD5Str(source);
    }


    public static String getMD5Str(String str) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(str.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        String md5Str = new BigInteger(1, digest).toString(16);
        return md5Str;
    }



    public static String getPushSign(JSONObject jo, String sysSecret) {
        char[] s = jo.toString().toCharArray();
        Arrays.sort(s);
        String outputSignOriginalStr = new String(s) + sysSecret;
        String sign = getMD5Str(outputSignOriginalStr);
        return sign;
    }
}
