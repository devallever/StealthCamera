package com.allever.stealthcamera.utils

import android.app.ActivityManager
import android.content.Context
import android.util.Log

/**
 * Created by Allever on 18/5/11.
 */

object ServiceUtil {
    private val TAG = "ServiceUtil"
    fun isServiceRun(context: Context?, className: String): Boolean {
        if (context == null) {
            return false
        }
        var isRun = false
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val serviceList = activityManager
                .getRunningServices(200)
        for (serviceInfo in serviceList) {
            if (serviceInfo.service.className == className) {
                isRun = true
                break
            }
        }
        Log.d(TAG, "isServiceRun: isRunning = $isRun")
        return isRun
    }
}
