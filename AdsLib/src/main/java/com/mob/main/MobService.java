package com.mob.main;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Administrator on 2016/8/25.
 */
public abstract class MobService {

    public abstract void loadInterstitalAd(Context context, String tag, IMobAdListener mobAdListener) ;
    public abstract void onPause(Context applicationContext);
    public abstract void onResume(Context applicationContext) ;
    public abstract void loadBanner(Activity activity, String tag, IMobAdListener mobAdListener);
    public abstract void startService(Context context, String data, IMobAdFactory mobAdFactory);
    public abstract void addTestDevice(String device);
    private static MobService mobservice = new MobServiceImp();
    public static MobService getIns(){
        return mobservice;
    }
}
