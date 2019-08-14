	

/**
 ***************************************************************************** 
 * Copyright (C) 2011-2015 SUNTENG Corporation. All rights reserved File :
 * 2015-3-18
 * 
 * Description : NotificationCenter.java
 * 
 * Creation : 2015-3-18 Author : maozhi@sunteng.com History : Creation,
 * 2015-3-18, maozhi, Create the file
 ***************************************************************************** 
 */
package com.mob.boot;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;

import com.mob.tool.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
//观察者类
public class NotificationCenter {
    private static int BASE = 0x01;
    private static int generalId(){
        return ++BASE;
    }
    /**
     * 数据加载以及广告加载完成
     */
    public static final int NOTF_STOP_BOOTACTIVITY = generalId();
    /**
     * 显示加载完成
     */
    public static final int NOTF_BOOT_ACTIVITY_GONE = generalId() ;
    /**
     * 启动activity开始显示
     */
    public static int NOTF_BOOT_ACTIVITY_VISIABLE = generalId();

    public static int NOTF_REQUEST_AD_TIMEOUT = generalId();

    private SparseArray<List<WeakReference<NotifyObserver>> > mSparseArray = new SparseArray<List<WeakReference<NotifyObserver>>>();
    //观察者接口
    public static interface NotifyObserver{
        void onNotify(int id, Object object);
    }
    
    private static NotificationCenter gNotificationCenter = null;
    
    public static NotificationCenter getIns(){
        if(gNotificationCenter == null) {
            gNotificationCenter = new NotificationCenter();
        }
        return gNotificationCenter;
    }
    
    private NotificationCenter() {
    }
    
    public void regist(NotifyObserver notifyObserver, int id){
        List<WeakReference<NotifyObserver>> aList = mSparseArray.get(id);
        if(aList == null) {
            //TODO: 将要排除非法ID
            aList = new ArrayList<WeakReference<NotifyObserver>>();
            aList.add(new WeakReference<NotifyObserver>(notifyObserver));
            mSparseArray.put(id, aList);
        }else {
            for (WeakReference<NotifyObserver> weakReference : aList) {
                if(weakReference != null && weakReference.get() != null) {
                    if( weakReference.get()  == notifyObserver) {
                        //已经注册过
                        Utils.printInfo("已经注册过了");
                        return ;
                    }
                }
              }
              aList.add(new WeakReference<NotifyObserver>(notifyObserver));
        }
    }
    
    public void unRegist(NotifyObserver notifyObserver, int id){
        List<WeakReference<NotifyObserver>> aList = mSparseArray.get(id);
        if(aList == null) {
            return ;
        }else {
           Iterator<WeakReference<NotifyObserver>> iterator = aList.iterator();
            while (iterator.hasNext()) {
                WeakReference<NotifyObserver> weakReference =  iterator.next();
                if(weakReference.get() != null && weakReference.get() == notifyObserver) {
                    iterator.remove();
                }
            }
        }
    }
   private Handler handler = new Handler(Looper.getMainLooper());
    
    public void notifyObserver(final int id, final Object object){
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<WeakReference<NotifyObserver>> list = mSparseArray.get(id);
                if(list != null) {
                    for (WeakReference<NotifyObserver> weakReference : list) {
                        if(weakReference != null && weakReference.get() != null) {
                            weakReference.get().onNotify(id, object);
                        }
                    }
                }
            }
        });
    }
    public void notifyObserver(final int id){
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<WeakReference<NotifyObserver>> list = mSparseArray.get(id);
                if(list != null) {
                    for (WeakReference<NotifyObserver> weakReference : list) {
                        if(weakReference != null && weakReference.get() != null) {
                            weakReference.get().onNotify(id, null);
                        }
                    }
                }
            }
        });
    }
    
    public void release(){
        mSparseArray.clear();
    }
}