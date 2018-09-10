package com.shuyun.qapp.utils;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;

import com.shuyun.qapp.net.AppConst;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Random;

/**
 * Created by sunxiao on 2018/5/3.
 * 编码解码相关工具类
 */

public class EncodeAndStringTool {
    private static final String TAG = "EncodeAndStringTool";

    /**
     * 获得登录终端的序列号
     *
     * @return
     */
    public static String getTsn(Context mContext) {
        String sn = android.os.Build.SERIAL;
        if (sn == null || sn.isEmpty()) {
            sn = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return sn;
    }

    /**
     * salt应用随机生成的字符串
     *
     * @param length
     * @return
     */
    public static String generateRandomString(int length) {
        String str = "abcdefghigklmnopkrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sf = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);//0~61
            sf.append(str.charAt(number));
        }
        return sf.toString();
    }

    /**
     * Base64编码
     *
     * @param input 要编码的字符串
     * @return Base64编码后的字符串
     */
    public static byte[] base64Encode(String input) {
        return base64Encode(input.getBytes());
    }

    /**
     * Base64编码
     *
     * @param input 要编码的字节数组
     * @return Base64编码后的字符串
     */
    public static byte[] base64Encode(byte[] input) {
        return Base64.encode(input, Base64.NO_WRAP);
    }

    /**
     * Base64编码
     *
     * @param input 要编码的字节数组
     * @return Base64编码后的字符串
     */
    public static String base64Encode2String(byte[] input) {
        return Base64.encodeToString(input, Base64.NO_WRAP);
    }

    /**
     * Base64解码
     *
     * @param input 要解码的字符串
     * @return Base64解码后的字符串
     */
    public static byte[] base64Decode(String input) {
        return Base64.decode(input, Base64.NO_WRAP);
    }

    /**
     * Base64解码
     *
     * @param input 要解码的字符串
     * @return Base64解码后的字符串
     */
    public static byte[] base64Decode(byte[] input) {
        return Base64.decode(input, Base64.NO_WRAP);
    }


    /**
     * MD5加密
     *
     * @param data 明文字符串
     * @return 16进制密文
     */
    public static String encryptMD5ToString(String data) {
        return encryptMD5ToString(data.getBytes());
    }

    /**
     * MD5加密
     *
     * @param data 明文字节数组
     * @return 16进制密文
     */
    public static String encryptMD5ToString(byte[] data) {
        return bytes2HexString(encryptMD5(data));
    }

    /**
     * 将MD5后的字符串转化为16进制小写
     *
     * @param bytes
     * @return
     */
    private static String bytes2HexString(byte[] bytes) {
        StringBuffer hexStr = new StringBuffer();
        int num;
        for (int i = 0; i < bytes.length; i++) {
            num = bytes[i];
            if (num < 0) {
                num += 256;
            }
            if (num < 16) {
                hexStr.append("0");
            }
            hexStr.append(Integer.toHexString(num));
        }
        return hexStr.toString().toLowerCase();
    }

    /**
     * MD5加密
     *
     * @param data 明文字节数组
     * @return 密文字节数组
     */
    public static byte[] encryptMD5(byte[] data) {
        return encryptAlgorithm(data, "MD5");
    }

    /**
     * 对data进行algorithm算法加密
     *
     * @param data      明文字节数组
     * @param algorithm 加密算法
     * @return 密文字节数组
     */
    private static byte[] encryptAlgorithm(byte[] data, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * 2的n-1次方;
     *
     * @param mdCode
     * @return
     */
    public static String getCode(String mdCode) {
        StringBuilder sb = new StringBuilder(5);
        for (int i = 0; i < 5; ++i) {
            int pos = 2 << i;
            sb.append(mdCode.charAt(pos - 1));
        }
        return sb.toString();
    }

    /**
     * 判断String是否为空
     *
     * @param str
     * @return
     */
    public static boolean isStringEmpty(Object str) {
        if (str == null || str == "" || TextUtils.isEmpty(str.toString().replaceAll(" ", ""))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断String是否为空
     *
     * @param str
     * @return
     */
    public static boolean isObjectEmpty(Object str) {
        if (str == null || str == "") {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断集合是否为空
     *
     * @param str
     * @return
     */
    public static boolean isListEmpty(Object str) {
        if (str == null || str == "" || ((List) str).size() == 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * @param phoneNum
     * @param code
     * @return 判断手机号和验证码或者手机号和密码是否有一个为空
     */
    public static boolean checkNull(String phoneNum, String code) {
        if (TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(code)) {
            return false;
        }
        return true;
    }

    /**
     * @param mContext
     * @return
     */
    public static String getCombSycm(Context mContext, String jwtToken, long curTime) {

        String salt = (String) SharedPrefrenceTool.get(mContext, "salt", "");
        String random = (String) SharedPrefrenceTool.get(mContext, "random", "");
        String codeStr = jwtToken + curTime + AppConst.V + salt + random + AppConst.APP_KEY;

        return codeStr;
    }
}
