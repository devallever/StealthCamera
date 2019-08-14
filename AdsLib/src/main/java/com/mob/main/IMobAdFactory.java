package com.mob.main;

import android.content.Context;

import com.mob.core.MobBannerAd;
import com.mob.core.MobInterstitialAd;

/**
 * Created by maozhi on 2017/8/1.
 */

public interface IMobAdFactory {
    MobBannerAd getBannerAd(Context context, String cate, String pub,String appid);
    MobInterstitialAd getInterstitialAd(Context context, String cate, String pub, String appid);
}
