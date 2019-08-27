package com.allever.stealthcamera.ui.view

import androidx.viewpager.widget.ViewPager
import android.util.Log
import android.view.View

/**
 * Created by Allever on 18/5/16.
 * https://mp.weixin.qq.com/s?__biz=MzAxMTI4MTkwNQ==&mid=2650820073&idx=1&sn=9e084723624180f7ab28e54f2aef132c&scene=23&srcid=0506b08maFirw2pBvnewcDsp#rd
 */

class ZoomOutPageTransformer : androidx.viewpager.widget.ViewPager.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width
        val pageHeight = view.height
        Log.d(TAG, "transformPage: pageWidth = $pageWidth")
        Log.d(TAG, "transformPage: pageHeight = $pageHeight")
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            //view.setAlpha(MIN_ALPHA);
            //view.setScaleX(MIN_SCALE);
            //view.setScaleY(MIN_SCALE);
        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
            val vertMargin = pageHeight * (1 - scaleFactor) / 2
            val horzMargin = pageWidth * (1 - scaleFactor) / 2
            if (position < 0) {//[-1,0]
                view.translationX = horzMargin - vertMargin / 2
                //view.setScaleX(1 + 0.3f * position);
                //view.setScaleY(1 + 0.3f * position);
            } else {//[0,1]
                view.translationX = -horzMargin + vertMargin / 2

                view.scaleX = 1 - 0.2f * position
                view.scaleY = 1 - 0.2f * position
            }

            // Scale the page down (between MIN_SCALE and 1)

            // Fade the page relative to its size.
            //view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.scaleX = MIN_SCALE
            view.scaleY = MIN_SCALE
            //view.setAlpha(MIN_ALPHA);
        }
    }

    companion object {
        private val TAG = "ZoomOutPageTransformer"
        private val MIN_SCALE = 0.8f
        private val MIN_ALPHA = 0.5f
    }
}