package com.allever.stealthcamera.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Allever on 18/5/11.
 */

public class SPUtil {
    private static final String KEY_PRIVATE = "is_show";
    private static final String SP_NAME = "setting";
    private static final String KEY_Y_VALUE = "y_value";
    private static final String KEY_X_VALUE = "x_value";
    public static void setShowPreview(Context context, boolean value){
        if (context == null){
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_PRIVATE,value);
        editor.commit();
    }

    public static boolean getShowPreview(Context context){
        if (context == null) {
            return false;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        boolean isPrivate = sharedPreferences.getBoolean(KEY_PRIVATE,true);
        return isPrivate;
    }

    public static void setLastY(Context context, float value){
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE).edit();
        editor.putFloat(KEY_Y_VALUE,value);
        editor.commit();
    }

    public static void setLastX(Context context, float value){
        if (context == null) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE).edit();
        editor.putFloat(KEY_Y_VALUE,value);
        editor.commit();
    }

    public static float getLastY(Context context){
        if (context == null) {
            return -1.0f;
        }
        float value;
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        value = sharedPreferences.getFloat(KEY_Y_VALUE, -1.0f);
        return value;
    }

    public static float getLastX(Context context){
        if (context == null) {
            return -1.0f;
        }
        float value;
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        value = sharedPreferences.getFloat(KEY_X_VALUE, -1.0f);
        return value;
    }

    public static void clearLastXY(Context context){
        setLastX(context,-1.0f);
        setLastY(context, -1.0f);
    }

}
