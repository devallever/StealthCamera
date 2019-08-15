package com.allever.stealthcamera.ui.view;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

/**
 * Created by Allever on 18/5/16.
 * https://mp.weixin.qq.com/s?__biz=MzAxMTI4MTkwNQ==&mid=2650820073&idx=1&sn=9e084723624180f7ab28e54f2aef132c&scene=23&srcid=0506b08maFirw2pBvnewcDsp#rd
 */

public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final String TAG = "ZoomOutPageTransformer";
    private static final float MIN_SCALE = 0.8f;
    private static final float MIN_ALPHA = 0.5f;

    @Override
    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();
        Log.d(TAG, "transformPage: pageWidth = " + pageWidth);
        Log.d(TAG, "transformPage: pageHeight = " + pageHeight);
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            //view.setAlpha(MIN_ALPHA);
            //view.setScaleX(MIN_SCALE);
            //view.setScaleY(MIN_SCALE);
        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {//[-1,0]
                view.setTranslationX(horzMargin - vertMargin / 2);
                //view.setScaleX(1 + 0.3f * position);
                //view.setScaleY(1 + 0.3f * position);
            } else {//[0,1]
                view.setTranslationX(-horzMargin + vertMargin / 2);

                view.setScaleX(1 - 0.2f * position);
                view.setScaleY(1 - 0.2f * position);
            }

            // Scale the page down (between MIN_SCALE and 1)

            // Fade the page relative to its size.
            //view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setScaleX(MIN_SCALE);
            view.setScaleY(MIN_SCALE);
            //view.setAlpha(MIN_ALPHA);
        }
    }
}