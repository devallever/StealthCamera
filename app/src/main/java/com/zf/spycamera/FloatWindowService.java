package com.zf.spycamera;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.zf.spycamera.utils.FloatWindowUtil;

import java.util.Timer;

/**
 * Created by Allever on 18/5/11.
 */

public class FloatWindowService extends Service {
    private static final String TAG = "FloatWindowService";
    private Timer mTimer;
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
//        mTimer.cancel();
//        mTimer = null;
        CameraManager.getIns().releaseCamera();

        //关闭浮窗
        FloatWindowUtil.removeFloatWindow(getApplicationContext());
        mService = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (mTimer == null){
//            mTimer = new Timer();
//            mTimer.scheduleAtFixedRate(new RefreshTask(),0,100);
//        }

        CameraManager.getIns().openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
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

//    class RefreshTask extends TimerTask{
//        @Override
//        public void run() {
//            Log.d(TAG, "run: ");
//            // 当前界面是桌面，且没有悬浮窗显示，则创建悬浮窗。
//            if (!FloatWindowUtil.isFloatWindowShowing()) {
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        FloatWindowUtil.createFloatWindow(getApplicationContext());
//                    }
//                });
//            }
//        }
//    }

}
