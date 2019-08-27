package com.allever.stealthcamera.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by Allever on 18/5/16.
 */

class GalleryPagerAdapter(fm: androidx.fragment.app.FragmentManager, private val mContext: Context, private val mFragmentList: List<androidx.fragment.app.Fragment>) : androidx.fragment.app.FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageWidth(position: Int): Float {
        return 0.75f
    }
}
