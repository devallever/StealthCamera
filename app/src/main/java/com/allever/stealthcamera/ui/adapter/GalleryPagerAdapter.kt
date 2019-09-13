package com.allever.stealthcamera.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * Created by Allever on 18/5/16.
 */

class GalleryPagerAdapter(fm: FragmentManager, private val mContext: Context, private val mFragmentList: List<Fragment>) : androidx.fragment.app.FragmentPagerAdapter(fm) {

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
