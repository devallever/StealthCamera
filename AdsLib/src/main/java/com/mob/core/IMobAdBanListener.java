package com.mob.core;

/**
 * Created by Administrator on 2016/8/26.
 */
public interface IMobAdBanListener {
    void onAdBanLoaded(MobBannerAd mobban);
    void onAdBanFailedToLoad();
}
