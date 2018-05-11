package com.zf.spycamera.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Allever on 18/5/11.
 */

public class SPUtil {
    private static final String KEY_PRIVATE = "is_show";
    private static final String SP_NAME = "setting";
    public static void setShowPreview(Context context, boolean value){
        if (context == null){
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_PRIVATE,value);
        editor.commit();
    }

    public static boolean getShowPrivate(Context context){
        if (context == null) {
            return false;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        boolean isPrivate = sharedPreferences.getBoolean(KEY_PRIVATE,true);
        return isPrivate;
    }
}
