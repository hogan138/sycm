package com.shuyun.qapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 保存个人信息
 */
public class SaveUserInfo {

    public static final String PREFERENCE_NAME = "person_info";
    private static SharedPreferences mSharedPreferences;
    private static SaveUserInfo mPreferenceUtils;

    private SaveUserInfo(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static SaveUserInfo getInstance(Context cxt) {
        if (mPreferenceUtils == null) {
            mPreferenceUtils = new SaveUserInfo(cxt);
        }

        return mPreferenceUtils;
    }

    public void setUserInfo(String str_name, String str_value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(str_name, str_value);
        editor.apply();
    }

    public String getUserInfo(String str_name) {
        return mSharedPreferences.getString(str_name, "");
    }

    /**
     * 首选项保存boolean类型值
     *
     * @param strName
     * @param value
     */
    public void setBooleanValue(String strName, Boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(strName, value);
        editor.apply();
    }

    /**
     * 取出boolean类型的值
     * @param strName
     * @return
     */
    public Boolean getBooleanValue(String strName) {
        return mSharedPreferences.getBoolean(strName, false);
    }
}
