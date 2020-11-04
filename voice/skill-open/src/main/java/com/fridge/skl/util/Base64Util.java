package com.fridge.skl.util;



import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @Author
 * @Date 2020/5/21 9:46
 */
public class Base64Util {

    public static String encodeStr(String str) {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] textByte;
        String encodedText = null;
        try {
            textByte = str.getBytes("UTF-8");
            encodedText = encoder.encodeToString(textByte);
            return encodedText;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedText;
    }

    public static String decodeStr(String encodedStr) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] textBytes = decoder.decode(encodedStr);
        String decodedStr = null;
        try {
            decodedStr = new String(textBytes,"UTF-8");
            return decodedStr;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decodedStr;
    }

}