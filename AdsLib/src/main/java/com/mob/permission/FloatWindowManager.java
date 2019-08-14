/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.mob.permission;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;


import com.mob.R;
import com.mob.permission.rom.HuaweiUtils;
import com.mob.permission.rom.MeizuUtils;
import com.mob.permission.rom.MiuiUtils;
import com.mob.permission.rom.OppoUtils;
import com.mob.permission.rom.QikuUtils;
import com.mob.permission.rom.RomUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Description:
 *
 * @author zhaozp
 * @since 2016-10-17
 */

public class FloatWindowManager {
    private static final String TAG = "FloatWindowManager";

    private static volatile FloatWindowManager instance;
    private RelativeLayout mLayout;

    private boolean isWindowDismiss = true;
    private WindowManager windowManager = null;
    private WindowManager.LayoutParams mParams = null;
    private AVCallFloatView floatView = null;
    private Dialog dialog;

    public static FloatWindowManager getInstance() {
        if (instance == null) {
            synchronized (FloatWindowManager.class) {
                if (instance == null) {
                    instance = new FloatWindowManager();
                }
            }
        }
        return instance;
    }

    public boolean applyOrShowFloatWindow(Context context) {
        if (checkPermission(context)) {
//            showWindow(context);
            return true;
        } else {
//            applyPermission(context);
            return false;
        }
    }

    private boolean checkPermission(Context context) {
        //6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                return miuiPermissionCheck(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                return meizuPermissionCheck(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                return huaweiPermissionCheck(context);
            } else if (RomUtils.checkIs360Rom()) {
                return qikuPermissionCheck(context);
            } else if (RomUtils.checkIsOppoRom()) {
                return oppoROMPermissionCheck(context);
            }
        }
        return commonROMPermissionCheck(context);
    }

    private boolean huaweiPermissionCheck(Context context) {
        return HuaweiUtils.checkFloatWindowPermission(context);
    }

    private boolean miuiPermissionCheck(Context context) {
        return MiuiUtils.checkFloatWindowPermission(context);
    }

    private boolean meizuPermissionCheck(Context context) {
        return MeizuUtils.checkFloatWindowPermission(context);
    }

    private boolean qikuPermissionCheck(Context context) {
        return QikuUtils.checkFloatWindowPermission(context);
    }

    private boolean oppoROMPermissionCheck(Context context) {
        return OppoUtils.checkFloatWindowPermission(context);
    }

    private boolean commonROMPermissionCheck(Context context) {
        //最新发现魅族6.0的系统这种方式不好用，天杀的，只有你是奇葩，没办法，单独适配一下
        if (RomUtils.checkIsMeizuRom()) {
            return meizuPermissionCheck(context);
        } else {
            Boolean result = true;
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    Class clazz = Settings.class;
                    Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                    result = (Boolean) canDrawOverlays.invoke(null, context);
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
            return result;
        }
    }

    public void applyPermission(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                miuiROMPermissionApply(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                meizuROMPermissionApply(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                huaweiROMPermissionApply(context);
            } else if (RomUtils.checkIs360Rom()) {
                ROM360PermissionApply(context);
            } else if (RomUtils.checkIsOppoRom()) {
                oppoROMPermissionApply(context);
            }
        }
        commonROMPermissionApply(context);
    }

    private void ROM360PermissionApply(final Context context) {
//        showConfirmDialog(context, new OnConfirmResult() {
//            @Override
//            public void confirmResult(boolean confirm) {
//                if (confirm) {
//                    QikuUtils.applyPermission(context);
//                } else {
//                    Log.e(TAG, "ROM:360, user manually refuse OVERLAY_PERMISSION");
//                }
//            }
//        });
        QikuUtils.applyPermission(context);
    }

    private void huaweiROMPermissionApply(final Context context) {
//        showConfirmDialog(context, new OnConfirmResult() {
//            @Override
//            public void confirmResult(boolean confirm) {
//                if (confirm) {
//                    HuaweiUtils.applyPermission(context);
//                } else {
//                    Log.e(TAG, "ROM:huawei, user manually refuse OVERLAY_PERMISSION");
//                }
//            }
//        });
        HuaweiUtils.applyPermission(context);
    }

    private void meizuROMPermissionApply(final Context context) {
//        showConfirmDialog(context, new OnConfirmResult() {
//            @Override
//            public void confirmResult(boolean confirm) {
//                if (confirm) {
//                    MeizuUtils.applyPermission(context);
//                } else {
//                    Log.e(TAG, "ROM:meizu, user manually refuse OVERLAY_PERMISSION");
//                }
//            }
//        });
        MeizuUtils.applyPermission(context);
    }

    private void miuiROMPermissionApply(final Context context) {
//        showConfirmDialog(context, new OnConfirmResult() {
//            @Override
//            public void confirmResult(boolean confirm) {
//                if (confirm) {
//                    MiuiUtils.applyMiuiPermission(context);
//                } else {
//                    Log.e(TAG, "ROM:miui, user manually refuse OVERLAY_PERMISSION");
//                }
//            }
//        });
        MiuiUtils.applyMiuiPermission(context);
    }

    private void oppoROMPermissionApply(final Context context) {
//        showConfirmDialog(context, new OnConfirmResult() {
//            @Override
//            public void confirmResult(boolean confirm) {
//                if (confirm) {
//                    OppoUtils.applyOppoPermission(context);
//                } else {
//                    Log.e(TAG, "ROM:miui, user manually refuse OVERLAY_PERMISSION");
//                }
//            }
//        });
        OppoUtils.applyOppoPermission(context);
    }

    /**
     * 通用 rom 权限申请
     */
    private void commonROMPermissionApply(final Context context) {
        //这里也一样，魅族系统需要单独适配
        if (RomUtils.checkIsMeizuRom()) {
            meizuROMPermissionApply(context);
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
//                showConfirmDialog(context, new OnConfirmResult() {
//                    @Override
//                    public void confirmResult(boolean confirm) {
//                        if (confirm) {
//
//                        } else {
//                            Log.d(TAG, "user manually refuse OVERLAY_PERMISSION");
//                            //需要做统计效果
//                        }
//                    }
//                });
                try {
                    Class clazz = Settings.class;
                    Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");

                    Intent intent = new Intent(field.get(null).toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    context.startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
        }
    }

    private void showConfirmDialog(Context context, OnConfirmResult result) {
        showConfirmDialog(context, "您的手机没有授予悬浮窗权限，请开启后再试", result);
    }

    private void showConfirmDialog(Context context, String message, final OnConfirmResult result) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new AlertDialog.Builder(context).setCancelable(true).setTitle("")
                .setMessage(message)
                .setPositiveButton("现在去开启",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirmResult(true);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("暂不开启",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirmResult(false);
                                dialog.dismiss();
                            }
                        }).create();
        dialog.show();
    }

    private interface OnConfirmResult {
        void confirmResult(boolean confirm);
    }

    private void showWindow(Context context) {
        if (!isWindowDismiss) {
            Log.e(TAG, "view is already added here");
            return;
        }

        isWindowDismiss = false;
        if (windowManager == null) {
            windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        }

        if (mLayout == null) {
            mLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.image_window, null);
        }
        //关闭硬件加速
        mLayout.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mParams = new WindowManager.LayoutParams();
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        mParams.format = PixelFormat.TRANSLUCENT;

        windowManager.addView(mLayout, mParams);
    }

    public void dismissWindow() {
        if (isWindowDismiss) {
            Log.e(TAG, "window can not be dismiss cause it has not been added");
            return;
        }

        isWindowDismiss = true;
        floatView.setIsShowing(false);
        if (windowManager != null && floatView != null) {
            windowManager.removeViewImmediate(floatView);
        }
    }

    private int dp2px(Context context, float dp){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
