package com.allever.stealthcamera.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Allever on 18/5/11.
 */

public class ServiceUtil {
    private static final String TAG = "ServiceUtil";
    public static boolean isServiceRun(Context context, String className){
        if (context == null) {
            return false;
        }
        boolean isRun = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(200);
        for (ActivityManager.RunningServiceInfo serviceInfo: serviceList){
            if (serviceInfo.service.getClassName().equals(className)){
                isRun = true;
                break;
            }
        }
        Log.d(TAG, "isServiceRun: isRunning = " + isRun);
        return isRun;
    }
}
