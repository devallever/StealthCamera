//package com.zf.spycamera;
//
//import android.app.Activity;
//import android.content.Context;
//
//import com.mob.main.IMobAd;
//import com.mob.main.IMobAdFactory;
//import com.mob.main.IMobAdListener;
//import com.mob.main.MobService;
//import com.mob.tool.Utils;
//
///**
// * Created by maozhi on 2018/1/17.
// */
//
//public class Controller {
//
//    private final String data = "{\n" +
//            "    \"mobinter\": {\n" +
//            "        \"dpa\": \"\", \n" +
//            "        \"dpf\": \"\", \n" +
//            "        \"dpg\": \"\", \n" +
//            "        \"pl\": [\n" +
//            "            {\n" +
//            "                \"cate\": \"F\", \n" +
//            "                \"pub\": \"2017279815261427_2017280058594736\"\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"cate\": \"A\",\n" +
//            "                \"pub\": \"ca-app-pub-6904969054409142/6282657516\"\n" +
//            "            }\n" +
//            "        ]\n" +
//            "    },\n" +
//            "  \"mobinterexit\": {\n" +
//            "        \"dpa\": \"\", \n" +
//            "        \"dpf\": \"\", \n" +
//            "        \"dpg\": \"\", \n" +
//            "        \"pl\": [\n" +
//            "            {\n" +
//            "                \"cate\": \"F\", \n" +
//            "                \"pub\": \"2017279815261427_2017280058594736\"\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"cate\": \"A\",\n" +
//            "                \"pub\": \"ca-app-pub-6904969054409142/6282657516\"\n" +
//            "            }\n" +
//            "        ]\n" +
//            "    },\n" +
//            "    \"mobbanner\": {\n" +
//            "        \"dpa\": \"\", \n" +
//            "        \"dpf\": \"\", \n" +
//            "        \"dpg\": \"\", \n" +
//            "        \"pl\": [\n" +
//            "            {\n" +
//            "                \"cate\": \"F\", \n" +
//            "                \"pub\": \"2017279815261427_2017280608594681\"\n" +
//            "            },\n" +
//            "            {\n" +
//            "                \"cate\": \"A\",\n" +
//            "                \"pub\": \"ca-app-pub-6904969054409142/4805924314\"\n" +
//            "            }\n" +
//            "        ]\n" +
//            "    }\n" +
//            "}";
//
//    private  boolean isInit = false;
//
//    public static Controller getIns(){
//        return ControllerHolder.INS;
//    }
//
//    private Context mContext = null;
//
//    private IMobAdFactory mIMobAdFactory = null;
//
//    /**
//     * 初始化
//     * @param context
//     */
//    public void init(Context context, IMobAdFactory iMobAdFactory){
//        if(!isInit) {
//            isInit = true;
//            mContext = context;
//            //初始化广告
//            if (iMobAdFactory != null){
//                MobService.getIns().startService(context, data,iMobAdFactory);
//                mIMobAdFactory = iMobAdFactory;
//            }
//            //没用到EventBus
////            if (!EventBus.getDefault().isRegistered(this)){
////                EventBus.getDefault().register(this);
////            }
//        }
//    }
//
//    /**
//     * 加载一个插屏广告
//     * @param
//     */
//    public void loadInterAd(Activity activity){
//        if (mIMobAdFactory == null) return;
//        //显示插屏
//        MobService.getIns().loadInterstitalAd(activity, "mobinter", new IMobAdListener() {
//            @Override
//            public void onAdLoaded(IMobAd mobAd) {
//                Utils.printInfo("成功拿到广告");
//                mobAd.showAd();
//            }
//
//            @Override
//            public void onAdFailedToLoad() {
//                Utils.printInfo("广告轮询失败");
//            }
//        });
//    }
//
//    /**
//     * 加载一个Banner广告
//     * @param activity
//     */
//    public void loadBannerAd(Activity activity){
//        if (mIMobAdFactory == null) return;
//        //显示插屏
//        MobService.getIns().loadBanner(activity, "mobbanner", new IMobAdListener() {
//            @Override
//            public void onAdLoaded(IMobAd mobAd) {
//                Utils.printInfo("成功拿到广告");
//                mobAd.showAd();
//            }
//
//            @Override
//            public void onAdFailedToLoad() {
//                Utils.printInfo("广告轮询失败");
//            }
//        });
//    }
//
//
////    @Subscribe(threadMode = ThreadMode.MAIN)
////    public void onMessageEvent(String event) {
////        /* Do something */
////        if (mContext == null) {
////            return;
////        }
////        switch (event){
////            case ConstantElectric.EVENT_ELECTRIC_EXIT:
////            case ConstantFloatBroken.EVENT_FLOATBROKEN_EXIT:
////            case ConstantBlowFire.EVENT_BLOWFIRE_EXIT:
////            case ConstantBroken.EVENT_BROKEN_EXIT:
////                Intent intent = new Intent(mContext,MainActivity.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                mContext.startActivity(intent);
////                break;
////        }
////    }
//
////    /**
////     * 加载广告
////     * @param activity
////     */
////    public void loadaAd(final Activity activity, boolean isShowInter, boolean isShowBanner){
////        if (mIMobAdFactory == null) return;
////        //加载插屏
////        if (isShowInter){
////            loadaAd(activity);
////        }
////        //加载Banner
////        if (isShowBanner){
////            loadaBannerAd(activity);
////        }
////    }
////
////    /**
////     * 显示一个差评广告。
////     */
////    public boolean popAndShowInterstitialAd(Activity activity){
////        if(gMobAdList != null && !gMobAdList.isEmpty()) {
////            IMobAd mobAd = gMobAdList.removeFirst();
////            if(mobAd != null) {
////                if(activity != null) {
////                    mobAd.showAd(activity);
////                }
////
////                return true;
////            }
////        }
////        return false;
////    }
////
////    public boolean isEmptyAd(){
////        return  gMobAdList.isEmpty();
////    }
//
//    private static class ControllerHolder{
//        private static Controller INS = new Controller();
//    }
//}
