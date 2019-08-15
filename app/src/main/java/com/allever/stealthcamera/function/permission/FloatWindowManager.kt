/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.allever.stealthcamera.function.permission

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout

import com.allever.stealthcamera.R
import com.allever.stealthcamera.function.permission.rom.HuaweiUtils
import com.allever.stealthcamera.function.permission.rom.MeizuUtils
import com.allever.stealthcamera.function.permission.rom.MiuiUtils
import com.allever.stealthcamera.function.permission.rom.OppoUtils
import com.allever.stealthcamera.function.permission.rom.QikuUtils
import com.allever.stealthcamera.function.permission.rom.RomUtils

import java.lang.reflect.Field
import java.lang.reflect.Method

@SuppressLint("StaticFieldLeak")
/**
 * Description:
 *
 * @author zhaozp
 * @since 2016-10-17
 */

object FloatWindowManager {
    private val TAG = "FloatWindowManager"
    private var mLayout: RelativeLayout? = null

    private var isWindowDismiss = true
    private var windowManager: WindowManager? = null
    private var mParams: WindowManager.LayoutParams? = null
    private val floatView: AVCallFloatView? = null
    private var dialog: Dialog? = null

    fun applyOrShowFloatWindow(context: Context): Boolean {
        return checkPermission(context)
    }

    private fun checkPermission(context: Context): Boolean {
        //6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
        if (Build.VERSION.SDK_INT < 23) {
            when {
                RomUtils.checkIsMiuiRom() -> return miuiPermissionCheck(context)
                RomUtils.checkIsMeizuRom() -> return meizuPermissionCheck(context)
                RomUtils.checkIsHuaweiRom() -> return huaweiPermissionCheck(context)
                RomUtils.checkIs360Rom() -> return qikuPermissionCheck(context)
                RomUtils.checkIsOppoRom() -> return oppoROMPermissionCheck(context)
            }
        }
        return commonROMPermissionCheck(context)
    }

    private fun huaweiPermissionCheck(context: Context): Boolean {
        return HuaweiUtils.checkFloatWindowPermission(context)
    }

    private fun miuiPermissionCheck(context: Context): Boolean {
        return MiuiUtils.checkFloatWindowPermission(context)
    }

    private fun meizuPermissionCheck(context: Context): Boolean {
        return MeizuUtils.checkFloatWindowPermission(context)
    }

    private fun qikuPermissionCheck(context: Context): Boolean {
        return QikuUtils.checkFloatWindowPermission(context)
    }

    private fun oppoROMPermissionCheck(context: Context): Boolean {
        return OppoUtils.checkFloatWindowPermission(context)
    }

    private fun commonROMPermissionCheck(context: Context): Boolean {
        //最新发现魅族6.0的系统这种方式不好用，天杀的，只有你是奇葩，没办法，单独适配一下
        if (RomUtils.checkIsMeizuRom()) {
            return meizuPermissionCheck(context)
        } else {
            var result: Boolean? = true
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    val clazz = Settings::class.java
                    val canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context::class.java)
                    result = canDrawOverlays.invoke(null, context) as Boolean
                } catch (e: Exception) {
                    Log.e(TAG, Log.getStackTraceString(e))
                }

            }
            return result!!
        }
    }

    fun applyPermission(context: Context) {
        if (Build.VERSION.SDK_INT < 23) {
            when {
                RomUtils.checkIsMiuiRom() -> miuiROMPermissionApply(context)
                RomUtils.checkIsMeizuRom() -> meizuROMPermissionApply(context)
                RomUtils.checkIsHuaweiRom() -> huaweiROMPermissionApply(context)
                RomUtils.checkIs360Rom() -> ROM360PermissionApply(context)
                RomUtils.checkIsOppoRom() -> oppoROMPermissionApply(context)
            }
        }
        commonROMPermissionApply(context)
    }

    private fun ROM360PermissionApply(context: Context) {
        QikuUtils.applyPermission(context)
    }

    private fun huaweiROMPermissionApply(context: Context) {
        HuaweiUtils.applyPermission(context)
    }

    private fun meizuROMPermissionApply(context: Context) {
        MeizuUtils.applyPermission(context)
    }

    private fun miuiROMPermissionApply(context: Context) {
        MiuiUtils.applyMiuiPermission(context)
    }

    private fun oppoROMPermissionApply(context: Context) {
        OppoUtils.applyOppoPermission(context)
    }

    /**
     * 通用 rom 权限申请
     */
    private fun commonROMPermissionApply(context: Context) {
        //这里也一样，魅族系统需要单独适配
        if (RomUtils.checkIsMeizuRom()) {
            meizuROMPermissionApply(context)
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    val clazz = Settings::class.java
                    val field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION")

                    val intent = Intent(field.get(null).toString())
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.data = Uri.parse("package:" + context.packageName)
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Log.e(TAG, Log.getStackTraceString(e))
                }

            }
        }
    }

    private fun showConfirmDialog(context: Context, result: OnConfirmResult) {
        showConfirmDialog(context, "您的手机没有授予悬浮窗权限，请开启后再试", result)
    }

    private fun showConfirmDialog(context: Context, message: String, result: OnConfirmResult) {
        if (dialog != null && dialog!!.isShowing) {
            dialog!!.dismiss()
        }

        dialog = AlertDialog.Builder(context).setCancelable(true).setTitle("")
                .setMessage(message)
                .setPositiveButton("现在去开启"
                ) { dialog, which ->
                    result.confirmResult(true)
                    dialog.dismiss()
                }.setNegativeButton("暂不开启"
                ) { dialog, which ->
                    result.confirmResult(false)
                    dialog.dismiss()
                }.create()
        dialog!!.show()
    }

    private interface OnConfirmResult {
        fun confirmResult(confirm: Boolean)
    }

    private fun showWindow(context: Context) {
        if (!isWindowDismiss) {
            Log.e(TAG, "view is already added here")
            return
        }

        isWindowDismiss = false
        if (windowManager == null) {
            windowManager = context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        }

        if (mLayout == null) {
            mLayout = LayoutInflater.from(context).inflate(R.layout.image_window, null) as RelativeLayout
        }
        //关闭硬件加速
        mLayout!!.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        mParams = WindowManager.LayoutParams()
        mParams!!.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY
        mParams!!.width = WindowManager.LayoutParams.MATCH_PARENT
        mParams!!.height = WindowManager.LayoutParams.MATCH_PARENT
        mParams!!.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        mParams!!.format = PixelFormat.TRANSLUCENT

        windowManager!!.addView(mLayout, mParams)
    }

    fun dismissWindow() {
        if (isWindowDismiss) {
            Log.e(TAG, "window can not be dismiss cause it has not been added")
            return
        }

        isWindowDismiss = true
        floatView!!.setIsShowing(false)
        if (windowManager != null) {
            windowManager!!.removeViewImmediate(floatView)
        }
    }

    private fun dp2px(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}
