package com.mob.boot;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mob.R;
import com.mob.tool.Utils;

/**
 * Created by maozhi on 2018/5/24.
 */

public class BootActivity extends Activity implements NotificationCenter.NotifyObserver {


    //activity是否销毁
    private boolean mIsDestroyed = false;

    //广告请求超时时间
    private static final int TIMEOUT_AD = 5000;

    private Handler mHandle = new Handler();

    //当请求超时时执行的runnable
    private Runnable mDelayRunnable = new Runnable() {
        @Override
        public void run() {
            if(!isFinishing()) { //activity还在显示中
                NotificationCenter.getIns().notifyObserver(NotificationCenter.NOTF_REQUEST_AD_TIMEOUT);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIsDestroyed = false;

        //不显示程序的标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.dimAmount=0.5f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        NotificationCenter.getIns().regist(this, NotificationCenter.NOTF_STOP_BOOTACTIVITY);

//        setContentView(R.layout.activity_boot);

        mHandle.postDelayed(mDelayRunnable, TIMEOUT_AD);

        //加载动画
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        RelativeLayout layout = new RelativeLayout(this);
        layout.setLayoutParams(layoutParams);
        ImageView imageView = new ImageView(this);
        layoutParams = new RelativeLayout.LayoutParams(Utils.dip2px(this, 100f),Utils.dip2px(this,100f));
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(imageView, layoutParams);
        setContentView(layout);
        startAnimation(imageView);

    }


    private void startAnimation(ImageView imageView) {
        final AnimationLoad animationLoad = new AnimationLoad(this);
        imageView.setBackgroundDrawable(animationLoad);
        mHandle.post(new Runnable() {
            @Override
            public void run() {
                animationLoad.start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //当显示启动页面时
        NotificationCenter.getIns().notifyObserver(NotificationCenter.NOTF_BOOT_ACTIVITY_VISIABLE);
    }

    @Override
    public void onBackPressed() {}

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mIsDestroyed = true;

        NotificationCenter.getIns().notifyObserver(NotificationCenter.NOTF_BOOT_ACTIVITY_GONE);

        mHandle.post(new Runnable() {
            @Override
            public void run() {
                NotificationCenter.getIns().unRegist(BootActivity.this, NotificationCenter.NOTF_STOP_BOOTACTIVITY);
            }
        });
    }

    @Override
    public void onNotify(int id, Object object) {

        if(id == NotificationCenter.NOTF_STOP_BOOTACTIVITY) { //如果收到停止启动页面
            finish();
        }
    }
}
