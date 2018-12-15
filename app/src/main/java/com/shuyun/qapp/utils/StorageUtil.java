package com.shuyun.qapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class StorageUtil {
    private final static String PREFS_NAME = "$LOCALSTORAGE$";

    public static void setLocalStorage(Context mContent, String key, String value) {
        SharedPreferences settings = mContent.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void removeLocalStorage(Context mContent, String key) {
        SharedPreferences settings = mContent.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        editor.apply();
    }

    public static String getLocalStorage(Context mContent, String key) {
        SharedPreferences settings = mContent.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        return settings.getString(key, null);
    }
}