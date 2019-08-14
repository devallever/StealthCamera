package com.mob.core;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/25.
 */
public abstract class MobInterstitialAd {

    public abstract void showAd();
    public void showAd(Activity activity){
        showAd();
    }
    private List<String> mTestDeviceList = new ArrayList<>();

    protected IMobAdInterListener mAdListener = null;
    public void setAdListener(IMobAdInterListener adListener) {
        mAdListener = adListener;
    }

    protected  Context mContext = null;
   protected String mPub = null;
    public MobInterstitialAd(Context context , String pub ) {
        mContext = context;
        mPub = pub;
        initSdk();
        initTestDevice();
    }
    public MobInterstitialAd(Context context) {
        mContext = context;
        initSdk();
    }
    public void setPub(String pub ){
        mPub = pub;
    }

    public abstract void loadAd();

    public abstract  void initSdk();
    public abstract String getTag();

    private void initTestDevice(){
        if (mTestDeviceList.size()>0){
            mTestDeviceList.clear();
        }
        mTestDeviceList.add("e01bfd27a863893344db5c54611b261a");
        mTestDeviceList.add("F711E9F86475CB61F3477AB351BC65B2");
        mTestDeviceList.add("D08BBD92A113AA1D7DDA56A176398467");//Samsung-S5
        mTestDeviceList.add("0901851C4038EED18CB00CE3A4CD0F47");//Nexus5
    }

    protected List<String> getTestDeviceList(){
        return mTestDeviceList;
    }
}
