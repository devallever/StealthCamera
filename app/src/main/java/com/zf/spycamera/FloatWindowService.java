package com.zf.spycamera;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zf.spycamera.utils.FloatWindowUtil;

import java.io.Flushable;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.transform.sax.TemplatesHandler;

/**
 * Created by Allever on 18/5/11.
 */

public class FloatWindowService extends Service {
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
        mTimer.cancel();
        mTimer = null;
        CameraManager.getIns().releaseCamera();

        //关闭浮窗
        FloatWindowUtil.removeFloatWindow(getApplicationContext());
        mService = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mTimer == null){
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new RefreshTask(),0,100);
        }

        CameraManager.getIns().openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class RefreshTask extends TimerTask{
        @Override
        public void run() {
            // 当前界面是桌面，且没有悬浮窗显示，则创建悬浮窗。
            if (!FloatWindowUtil.isFloatWindowShowing()) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        FloatWindowUtil.createFloatWindow(getApplicationContext());
                    }
                });
            }
        }
    }

}
