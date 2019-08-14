package com.mob.core;

import android.content.Context;
import android.widget.RelativeLayout;

import com.mob.bean.MobAdbean;
import com.mob.main.IMobAd;
import com.mob.bean.Model;
import com.mob.tool.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * 广告引擎类
 * Created by maozhi on 2016/8/25.
 */
public abstract class MobAd implements IMobAd {


    protected Context mContext = null;
    public int mBannerPos = RelativeLayout.ALIGN_PARENT_BOTTOM;

    protected LinkedList<MobAdbean> mPolicyAds = new LinkedList<>();

    public MobAd(Context context){
        mContext =context;
        Utils.gContext = context.getApplicationContext();
        Model model = Model.getInstance(); //初始化model
    }

    public abstract void popAdShow();

    //解析数据
    protected void paraseData(LinkedList<MobAdbean> policyAds , String data) {
        try{
            policyAds.clear();  //先清空策略

            JSONObject jsonObject = new JSONObject(data);
            //该属性为banner特有，默然将banner放置在底部
            mBannerPos =  jsonObject.optInt("pos", RelativeLayout.ALIGN_PARENT_BOTTOM);

            //获取默认的pub值
            MobAdbean.defPubA = jsonObject.optString("dpa");
            MobAdbean.defPubF = jsonObject.optString("dpf");
            MobAdbean.defPubG = jsonObject.optString("dpg");
            JSONArray arrayBeans = jsonObject.optJSONArray("pl");

            //遍历数组获取pub值
            for (int i = 0 ;i < arrayBeans.length(); ++i) {
                JSONObject jsonadbean = arrayBeans.getJSONObject(i);
                String categroy =  jsonadbean.optString("cate");
                String pub = jsonadbean.optString("pub");
                String appid = jsonadbean.optString("appid");
                MobAdbean adbean = new MobAdbean();
                adbean.setCategory(categroy);
                adbean.setPub(pub);
                adbean.setAppId(appid);
                policyAds.add(adbean);
            }
        }catch (Exception e) {
            Utils.printInfo("parsedata + " +e.toString());
            e.printStackTrace();
        }
    }

}
