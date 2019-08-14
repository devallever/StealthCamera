package com.mob.tool;

import android.content.Context;
import android.content.SharedPreferences;

import com.mob.bean.Constant;


/**
 * Created by maozhi on 15-8-20
 * maozhi@sunteng.com
 */
public class FlagModel {
    private static SharedPreferences sharedPreferences = null;
    private static SharedPreferences getSharedPreferences(Context context){
        if(sharedPreferences == null) {
            sharedPreferences =  context.getApplicationContext().getSharedPreferences(Constant.strModelName, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }
    public static Boolean getBoolean(Context paramContext, String paramString, Boolean paramBoolean) {
        return Boolean.valueOf(getSharedPreferences(paramContext).getBoolean(paramString, paramBoolean.booleanValue()));
    }

    public static void putBoolean(Context paramContext, String paramString, Boolean paramBoolean) {
        getSharedPreferences(paramContext).edit().putBoolean(paramString, paramBoolean.booleanValue()).commit();
    }

    public static String getString(Context paramContext, String paramString1, String paramString2) {
        return getSharedPreferences(paramContext).getString(paramString1, paramString2);
    }

    public static void putString(Context paramContext, String paramString1, String paramString2) {
        getSharedPreferences(paramContext).edit().putString(paramString1, paramString2).commit();
    }

    public static Integer getInt(Context paramContext, String paramString, Integer paramInteger) {
        return Integer.valueOf(getSharedPreferences(paramContext).getInt(paramString, paramInteger.intValue()));
    }

    public static void putInt(Context paramContext, String paramString, Integer paramInteger) {
        getSharedPreferences(paramContext).edit().putInt(paramString, paramInteger.intValue()).commit();
    }

    public static Float getFloat(Context paramContext, String paramString, Float paramFloat) {
        return Float.valueOf(getSharedPreferences(paramContext).getFloat(paramString, paramFloat.floatValue()));
    }

    public static void putFloat(Context paramContext, String paramString, Float paramFloat) {
        getSharedPreferences(paramContext).edit().putFloat(paramString, paramFloat.floatValue()).commit();
    }

    public static Long getLong(Context paramContext, String paramString, Long paramLong) {
        return Long.valueOf(getSharedPreferences(paramContext).getLong(paramString, paramLong.longValue()));
    }

    public static void putLong(Context paramContext, String paramString, Long paramLong) {
        getSharedPreferences(paramContext).edit().putLong(paramString, paramLong.longValue()).commit();
    }

}
