package com.allever.stealthcamera;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.allever.stealthcamera.utils.CameraUtil;
import com.allever.stealthcamera.utils.FloatWindowUtil;
import com.allever.stealthcamera.utils.SPUtil;


/**
 * Created by Allever on 18/5/11.
 */

public class FloatWindowService extends Service {
    private static final String TAG = "FloatWindowService";
    private Context mContext;
    private Handler mHandler = new Handler();

    public static FloatWindowService mService;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mService = this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CameraManager.INSTANCE.releaseCamera();

        //关闭浮窗
        FloatWindowUtil.removeFloatWindow(getApplicationContext());
        mService = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CameraManager.INSTANCE.openCamera(CameraUtil.INSTANCE.getCameraId(SPUtil.INSTANCE.getUseFrontCamera(this)));
        //没有悬浮窗显示，则创建悬浮窗。
        if (!FloatWindowUtil.isFloatWindowShowing()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    FloatWindowUtil.createFloatWindow(getApplicationContext());
                }
            });
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
