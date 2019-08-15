package com.allever.stealthcamera.ui.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by Allever on 18/5/16.
 */

class GalleryPagerAdapter(fm: FragmentManager, private val mContext: Context, private val mFragmentList: List<Fragment>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageWidth(position: Int): Float {
        return 0.75f
    }
}
