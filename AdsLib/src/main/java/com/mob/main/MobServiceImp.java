package com.mob.main;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.mob.core.MobAd;
import com.mob.core.MobAdBanner;
import com.mob.core.MobAdInter;
import com.mob.bean.Model;
import com.mob.tool.Utils;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2016/8/25.
 */
public class MobServiceImp extends MobService {

    @Override
    public void loadInterstitalAd(Context context, String tag, IMobAdListener mobAdListener) {
        MobAd engine  = new MobAdInter(context);
        engine.loadAd((Activity) context,tag, mobAdListener);
    }


    @Override
    public void onPause(Context applicationContext) {
        MobclickAgent.onPause(applicationContext);
    }

    @Override
    public void onResume(Context applicationContext) {
        MobclickAgent.onResume(applicationContext);
    }


    @Override
    public void loadBanner(Activity activity, String tag, IMobAdListener iMobAdListener) {
        MobAd engine  =new MobAdBanner(activity);
        engine.loadAd(activity,tag,iMobAdListener);
    }

    @Override
    public void startService(Context context, String data, IMobAdFactory iMobAdFactory) {
        if(context == null) {
            return;
        }
        Utils.gFactory = iMobAdFactory;
        Utils.gContext = context.getApplicationContext();
        Model.getInstance().initModel(data);
    }

    @Override
    public void addTestDevice(String device) {
        if(TextUtils.isEmpty(device)) {
            return ;
        }
        Model.getInstance().addTestDevice(device);
    }

}
