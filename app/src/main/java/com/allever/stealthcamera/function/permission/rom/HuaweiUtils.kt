/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.allever.stealthcamera.function.permission.rom

import android.annotation.TargetApi
import android.app.AppOpsManager
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.util.Log
import android.widget.Toast

import java.lang.reflect.Method

object HuaweiUtils {
    private val TAG = "HuaweiUtils"

    /**
     * 检测 Huawei 悬浮窗权限
     */
    fun checkFloatWindowPermission(context: Context): Boolean {
        val version = Build.VERSION.SDK_INT
        return if (version >= 19) {
            checkOp(context, 24) //OP_SYSTEM_ALERT_WINDOW = 24;
        } else true
    }

    /**
     * 去华为权限申请页面
     */
    fun applyPermission(context: Context) {
        try {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //   ComponentName comp = new ComponentName("com.huawei.systemmanager","com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            //   ComponentName comp = new ComponentName("com.huawei.systemmanager",
            //      "com.huawei.permissionmanager.ui.SingleAppActivity");//华为权限管理，跳转到指定app的权限管理位置需要华为接口权限，未解决
            var comp = ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity")//悬浮窗管理页面
            intent.component = comp
            if (RomUtils.emuiVersion == 3.1) {
                //emui 3.1 的适配
                context.startActivity(intent)
            } else {
                //emui 3.0 的适配
                comp = ComponentName("com.huawei.systemmanager", "com.huawei.notificationmanager.ui.NotificationManagmentActivity")//悬浮窗管理页面
                intent.component = comp
                context.startActivity(intent)
            }
        } catch (e: SecurityException) {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //   ComponentName comp = new ComponentName("com.huawei.systemmanager","com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            val comp = ComponentName("com.huawei.systemmanager",
                    "com.huawei.permissionmanager.ui.MainActivity")//华为权限管理，跳转到本app的权限管理页面,这个需要华为接口权限，未解决
            //      ComponentName comp = new ComponentName("com.huawei.systemmanager","com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity");//悬浮窗管理页面
            intent.component = comp
            context.startActivity(intent)
            Log.e(TAG, Log.getStackTraceString(e))
        } catch (e: ActivityNotFoundException) {
            /**
             * 手机管家版本较低 HUAWEI SC-UL10
             */
            //   Toast.makeText(MainActivity.this, "act找不到", Toast.LENGTH_LONG).show();
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val comp = ComponentName("com.Android.settings", "com.android.settings.permission.TabItem")//权限管理页面 android4.4
            //   ComponentName comp = new ComponentName("com.android.settings","com.android.settings.permission.single_app_activity");//此处可跳转到指定app对应的权限管理页面，但是需要相关权限，未解决
            intent.component = comp
            context.startActivity(intent)
            e.printStackTrace()
            Log.e(TAG, Log.getStackTraceString(e))
        } catch (e: Exception) {
            //抛出异常时提示信息
            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_LONG).show()
            Log.e(TAG, Log.getStackTraceString(e))
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun checkOp(context: Context, op: Int): Boolean {
        val version = Build.VERSION.SDK_INT
        if (version >= 19) {
            val manager = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            try {
                val clazz = AppOpsManager::class.java
                val method = clazz.getDeclaredMethod("checkOp", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType, String::class.java)
                return AppOpsManager.MODE_ALLOWED == method.invoke(manager, op, Binder.getCallingUid(), context.packageName) as Int
            } catch (e: Exception) {
                Log.e(TAG, Log.getStackTraceString(e))
            }

        } else {
            Log.e(TAG, "Below API 19 cannot invoke!")
        }
        return false
    }
}


