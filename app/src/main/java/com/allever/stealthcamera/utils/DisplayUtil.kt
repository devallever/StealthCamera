package com.allever.stealthcamera.utils

import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.util.Log

/**
 * Created by Allever on 18/5/11.
 */

object DisplayUtil {
    private val TAG = "DisplayUtil"
    /**
     * dip转px
     * @param context
     * @param dipValue
     * @return
     */
    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * px转dip
     * @param context
     * @param pxValue
     * @return
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 获取屏幕宽度和高度，单位为px
     * @param context
     * @return
     */
    fun getScreenMetrics(context: Context): Point {
        val dm = context.resources.displayMetrics
        val w_screen = dm.widthPixels
        val h_screen = dm.heightPixels
        Log.i(TAG, "Screen---Width = " + w_screen + " Height = " + h_screen + " densityDpi = " + dm.densityDpi)
        return Point(w_screen, h_screen)

    }

    /**
     * 获取屏幕长宽比
     * @param context
     * @return
     */
    fun getScreenRate(context: Context): Float {
        val P = getScreenMetrics(context)
        val H = P.y.toFloat()
        val W = P.x.toFloat()
        return H / W
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        var height = -1
        // 获取status_bar_height资源的ID
        val resourceId = context.resources.getIdentifier(
                "status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            // 根据资源ID获取响应的尺寸值
            height = context.resources.getDimensionPixelSize(resourceId)
        }
        return height
    }
}
