package com.allever.stealthcamera.function.permission.rom

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

/**
 * Description:
 *
 * @author Shawn_Dut
 * @since 2018-02-01
 */
object VivoUtils {

    private val TAG = "VivoUtils"


    /**
     * oppo ROM 权限申请
     */
    fun applyOppoPermission(context: Context) {
        try {
            val intent = Intent()
            intent.putExtra("packagename", context.packageName)
            intent.putExtra("title", getAppName(context))
            intent.component = ComponentName.unflattenFromString("com.vivo.permissionmanager/.activity.SoftPermissionDetailActivity")
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 获取应用程序名称
     */
    private fun getAppName(context: Context): String? {
        try {
            val packageManager = context.packageManager
            val packageInfo = packageManager.getPackageInfo(
                    context.packageName, 0)
            val labelRes = packageInfo.applicationInfo.labelRes
            return context.resources.getString(labelRes)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return null
    }
}
