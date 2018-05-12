package com.zf.spycamera;

import android.content.Context;

import com.mob.core.MobBannerAd;
import com.mob.core.MobInterstitialAd;
import com.mob.main.IMobAdFactory;
import com.radishmobile.admob_lite.MobAdmobBan;
import com.radishmobile.admob_lite.MobAdmobInter;
import com.radishmobile.facebook.MobFacebookBan;
import com.radishmobile.facebook.MobFacebookInter;


/**
 * Created by maozhi on 2017/8/1.
 */

public class AdFactory implements IMobAdFactory {
    @Override
    public MobBannerAd getBannerAd(Context context, String cate, String pub, String appid) {
        MobBannerAd ad  =null;
        switch (cate) {
            case "G":
                break;
            case "F":
                ad = new MobFacebookBan(context,pub);
                break;
            case "A":
                ad = new MobAdmobBan(context,pub);
                break;
        }
        return ad;
    }

    @Override
    public MobInterstitialAd getInterstitialAd(Context context, String cate, String pub, String appid) {
        MobInterstitialAd ad  =null;
        switch (cate) {
            case "G":
                break;
            case "F":
                ad = new MobFacebookInter(context,pub);
                break;
            case "A":
                ad = new MobAdmobInter(context,pub);
                break;
        }
        return ad;
    }
}
