package com.mob.permission.rom;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Description:
 *
 * @author Shawn_Dut
 * @since 2018-02-01
 */
public class VivoUtils {

    private static final String TAG = "VivoUtils";


    /**
     * oppo ROM 权限申请
     */
    public static void applyOppoPermission(Context context) {
        try {
            Intent intent = new Intent();
            intent.putExtra("packagename", context.getPackageName());
            intent.putExtra("title", getAppName(context));
            intent.setComponent(ComponentName.unflattenFromString("com.vivo.permissionmanager/.activity.SoftPermissionDetailActivity"));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取应用程序名称
     */
    private static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
