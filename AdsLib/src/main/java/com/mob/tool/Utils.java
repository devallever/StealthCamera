package com.mob.tool;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.mob.main.IMobAdFactory;

import java.util.Random;

/**
 * Created by Administrator on 2016/8/25.
 */
public class Utils {

    public static Context gContext = null;

    public static IMobAdFactory gFactory = null;

    public static void printInfo(String s) {
        Log.i("MobAds", s);
    }

    public static Handler gHandle = new Handler(Looper.getMainLooper());

    //获取随机数
    public static int getRandom(int num) {
        Random random = new Random();
        int ran = random.nextInt(num);
        return ran;
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /*
    private static DisplayImageOptions gOptions = null;

    public static DisplayImageOptions getOptions() {
        return gOptions;
    }

    public static void initImageLoader(Context context) {
        if (gOptions == null && !ImageLoader.getInstance().isInited()) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).diskCacheFileCount(200)
                    .build();

            ImageLoader.getInstance().init(config);
            gOptions = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc().build();
        }
    }*/

    //选择activity的viewgroup
    public static ViewGroup findViewGroup(Activity activity) {
        FrameLayout contentView = (FrameLayout) activity.findViewById(Window.ID_ANDROID_CONTENT);
        View childView = contentView.getChildAt(0);
        if (childView == null) {
            return null;
        }
        contentView.removeViewAt(0);
        RelativeLayout relativeLayout = new RelativeLayout(activity);
        RelativeLayout.LayoutParams rLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(rLayoutParams);
        relativeLayout.addView(childView);
        return relativeLayout;
    }


}
