package com.allever.stealthcamera.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.allever.stealthcamera.ui.view.PreviewView;

/**
 * Created by Allever on 18/5/11.
 */

public class FloatWindowUtil {

    /**
     * 小悬浮窗View的实例
     */
    private static PreviewView previewView;

    /**
     * 小悬浮窗View的参数
     */
    private static WindowManager.LayoutParams floatWindowViewParams;


    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param context
     *            必须为应用程序的Context.
     */
    public static void createFloatWindow(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (previewView == null) {
            previewView = new PreviewView(context);
            if (floatWindowViewParams == null) {
                floatWindowViewParams = new WindowManager.LayoutParams();
//                floatWindowViewParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                //解决8.0 悬浮窗崩溃的问题
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    floatWindowViewParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                } else {
                    floatWindowViewParams.type = WindowManager.LayoutParams.TYPE_PHONE;
                }
                floatWindowViewParams.format = PixelFormat.RGBA_8888;
                floatWindowViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                floatWindowViewParams.gravity = Gravity.LEFT | Gravity.TOP;
                floatWindowViewParams.width = PreviewView.Companion.getViewWidth();
                floatWindowViewParams.height = PreviewView.Companion.getViewHeight();
                floatWindowViewParams.x = screenWidth;
                floatWindowViewParams.y = 0;
            }
            previewView.setParams(floatWindowViewParams);
            windowManager.addView(previewView, floatWindowViewParams);
        }
    }

    public static void updateFloatWindow(Context context, View view, WindowManager.LayoutParams param) {
        WindowManager windowManager = getWindowManager(context);
        windowManager.updateViewLayout(view, param);
    }


    /**
     * 将小悬浮窗从屏幕上移除。
     *
     * @param context
     *            必须为应用程序的Context.
     */
    public static void removeFloatWindow(Context context) {
        if (previewView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(previewView);
            previewView = null;
            floatWindowViewParams = null;
        }
    }

    /**
     * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。
     *
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。
     */
    public static boolean isFloatWindowShowing() {
        return previewView != null;
    }

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。
     *
     * @param context
     *            必须为应用程序的Context.
     * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。
     */
    private static WindowManager getWindowManager(Context context) {
        if (context == null){
            return null;
        }
        WindowManager windowManager = null;
        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return windowManager;
    }
}
