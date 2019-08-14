package com.mob.core;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mob.bean.MobAdbean;
import com.mob.main.IMobAdListener;
import com.mob.bean.Model;
import com.mob.tool.Utils;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/8/29.
 */
public class MobAdBanner extends MobAd implements IMobAdBanListener {
    public MobAdBanner(Context context) {
        super(context);
    }
    private Activity mActivity = null;

    private  IMobAdListener mMobAdListener = null;
    @Override
    public void loadAd(final  Activity activity, final String tag , final IMobAdListener iMobAdListener) {
        try {
            mMobAdListener = iMobAdListener;
            mActivity = activity;
            String data = Model.getInstance().getOnlineData( tag);
            Utils.printInfo("addban will " +tag);
            if(TextUtils.isEmpty(data)) { //如果数据为空则去监听数据改变
                Utils.printInfo("addban wait " +tag);
                Model.getInstance().registDataChangeObserver(new Model.NotifyObserver() {
                    @Override
                    public void onDataChange(JSONObject jsonObject) {
                        Utils.gHandle.post(new Runnable() {
                            @Override
                            public void run() {
                                Utils.printInfo("ondata " + tag);
                                loadAd(activity,tag,iMobAdListener);
                            }
                        });
                    }
                });
                return;
            }
            Utils.printInfo("addban");
            //解析数据
            paraseData(mPolicyAds, data);
            //解析完数据后从广告链表中提取广告。
            popAdShow();
        }catch (Exception e) {
            e.printStackTrace();
            Utils.printInfo(e.toString());
            if(mMobAdListener != null) {
                mMobAdListener.onAdFailedToLoad();
            }
        }
    }



    @Override
    public void showAd() {
        if(mBannerView != null) {
            Utils.gHandle.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        //当banner加载完毕后，则添加banner显示出来哟
                        ViewGroup viewGroup =  Utils.findViewGroup(mActivity);
                        if(viewGroup != null) {
                            viewGroup.addView(mBannerView, getBannerLayoutParams());
                            mBannerView = null; //当显示完成后置null
                            mActivity.setContentView(viewGroup);
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                        Utils.printInfo(e.toString());
                    }
                }
            });
        }
    }


    @Override
    public void showAd(Activity activity) {
        showAd();
    }

    private View mBannerView = null;

    @Override
    public void onAdBanLoaded(final MobBannerAd mobban) {
        mBannerView = mobban.getBannerView();
        if(mMobAdListener != null) {
            mMobAdListener.onAdLoaded(this);
        }
    }

    @Override
    public boolean isLoaded() {
        return mBannerView != null ? true : false;
    }

    @Override
    public void onAdBanFailedToLoad() {
        popAdShow();
    }

    @Override
    public void popAdShow() {
        try {
            if(mPolicyAds.isEmpty()) {
                Utils.printInfo("banner ads empty");
                if(mMobAdListener != null) {
                    mMobAdListener.onAdFailedToLoad();
                }
                return;
            }
            MobAdbean adbean =  mPolicyAds.removeFirst();
            MobBannerAd bannerAd = null;
            if(adbean != null) {

                if(Utils.gFactory != null) {
                    bannerAd = Utils.gFactory.getBannerAd(mContext,adbean.getCategory(),adbean.getPub(),adbean.getAppId());
                    if(bannerAd != null) {
                        bannerAd.setAdListener(this);
                        bannerAd.loadAd();
                    }
                }else {
                    Utils.printInfo("MobBanner Factory is null");
                }
//                if(!TextUtils.isEmpty(adbean.getPub())) { //且pub不为空
//                    if(adbean.getCategory().equals(MobAdbean.ADMOB)) {  //如果是ADMOB广告则加载admob
//                        bannerAd = new MobAdmobBan(mContext, adbean.getPub());
//                    }else if(adbean.getCategory().equals(MobAdbean.FACEBOOK)) {
//                        bannerAd = new MobFacebookBan(mContext, adbean.getPub());
//                    }
//                    else if(adbean.getCategory().equals(MobAdbean.GDT)) {
//                        bannerAd = new MobGDTBan(mContext, adbean.getPub(),adbean.getAppId());
//                    }
//                    bannerAd.setPub(adbean.getPub());
//                    bannerAd.setAdListener(this);
//                    bannerAd.loadAd();
//                }
            }
        }catch (Exception e) {
            e.printStackTrace();
            Utils.printInfo(e.toString());
        }
    }

    private RelativeLayout.LayoutParams getBannerLayoutParams(){
        RelativeLayout.LayoutParams bannerParameters =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        bannerParameters.addRule(mBannerPos);
        return bannerParameters;
    }
    @Override
    public int getAdType() {
        return BANNER;
    }


}
