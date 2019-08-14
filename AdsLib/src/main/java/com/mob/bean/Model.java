package com.mob.bean;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.mob.tool.Utils;
import com.umeng.onlineconfig.OnlineConfigAgent;
import com.umeng.onlineconfig.UmengOnlineConfigureListener;

import org.json.JSONObject;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
public class Model   {
    private static Model ourInstance = new Model();
    public static  boolean isChineseVersion = false;

    public static  String DATA = "";

    private List<String> mTestDevice = new ArrayList<String>();
    public void addTestDevice(String device) {
        if(!mTestDevice.contains(device)){
            mTestDevice.add(device);
        }
    }

    public List<String> getTestDevice(){
        return  mTestDevice;
    }

    //观察者接口
    public  interface NotifyObserver{
        void onDataChange(JSONObject jsonObject);
    }

    public static Model getInstance() {
        return ourInstance;
    }

    private Model() {
    }

    public void initModel(String data){
        DATA = data;
        checkIsChineseVersion(Utils.gContext);

        if(isChineseVersion) { //如果是中文版走友盟的那一套
            OnlineConfigAgent.getInstance().updateOnlineConfig(Utils.gContext);
            OnlineConfigAgent.getInstance().setOnlineConfigListener(new UmengOnlineConfigureListener() {
                @Override
                public void onDataReceived( final JSONObject jsonObject) {
                    Utils.gHandle.post(new Runnable() {
                        @Override
                        public void run() {
                            Iterator<SoftReference<NotifyObserver>> iterator = notifyObserverList.iterator();
                            while (iterator.hasNext()) {
                                SoftReference<NotifyObserver> weakReference =  iterator.next();
                                if(weakReference.get() != null ) {
                                    weakReference.get().onDataChange(jsonObject);
                                }
                            }
                            notifyObserverList.clear();  //当广播完成后清空列表
                        }

                    });
                }
            });
        }
    }

    private List<SoftReference<NotifyObserver>> notifyObserverList = new ArrayList<SoftReference<NotifyObserver>>();
    public void registDataChangeObserver(NotifyObserver notifyObserver){
        boolean isContain = false;
        for (SoftReference<NotifyObserver> sn: notifyObserverList) {
            if(sn != null) {
                if(sn.get() != null) {
                    if(sn.get() == notifyObserver) {
                        isContain =  true;
                    }
                }
            }
        }
        if(!isContain) {
            SoftReference<NotifyObserver> notify = new SoftReference<NotifyObserver>(notifyObserver);
            notifyObserverList.add(notify);
        }
    }

    public void unRegistDataChangeObserver(NotifyObserver notifyObserver){
        Iterator<SoftReference<NotifyObserver>> iterator = notifyObserverList.iterator();
        while (iterator.hasNext()) {
            SoftReference<NotifyObserver> weakReference =  iterator.next();
            if(weakReference.get() != null && weakReference.get() == notifyObserver) {
                iterator.remove();
            }
        }
    }


    public String getOnlineData(String key){
        String addata = null;
        if(isChineseVersion) { //如果是中国版本走友盟通道
            addata =  OnlineConfigAgent.getInstance().getConfigParams(Utils.gContext, key );
        }else {

            try {
                JSONObject jsonObject = new JSONObject(DATA);
                String str =  jsonObject.opt(key).toString();
                addata = str;

            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return addata;
    }


    public  void checkIsChineseVersion(Context context){
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            isChineseVersion =appInfo.metaData.getBoolean(Constant.LANG_VERSION_KEY);
        }catch (Exception e) {
            e.printStackTrace();
            isChineseVersion = false;
        }
        Utils.printInfo("version chinese " + isChineseVersion);
    }
}
