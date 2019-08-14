package com.mob.bean;

import android.text.TextUtils;

/**
 * Created by Administrator on 2016/8/25.
 */
public class MobAdbean {
    public static final String ADMOB = "A";
    public static final String FACEBOOK = "F";
    public static final String GDT = "G";
    public static String defPubF = "";
    public static String defPubA = "";
    public static String defPubG = "";
    private  String category = ADMOB;
    private  String pub = "";
    private String appid = "";
    //默认值


    public String getPub() {
        if(TextUtils.isEmpty(pub)) {
           if(category.equals(ADMOB) ) {
               return defPubA;
            }

            else  if(category.equals(FACEBOOK) ) {
               return  defPubF;
            }
            else  if(category.equals(GDT) ) {
               return  defPubG;
            }
        }
        return pub;
    }
    public void setPub(String pub) {
        this.pub = pub;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAppId(String appId) {
        this.appid = appId;
    }

    public String getAppId(){
        return appid;
    }
}
