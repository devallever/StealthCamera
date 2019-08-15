package com.allever.stealthcamera.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity

import com.allever.stealthcamera.R
import com.allever.stealthcamera.ui.adapter.GalleryPagerAdapter
import com.allever.stealthcamera.utils.FileUtil
import com.allever.stealthcamera.ui.view.ZoomOutPageTransformer

import java.util.ArrayList

/**
 * Created by Allever on 18/5/16.
 */

class GalleryActivity : AppCompatActivity() {
    private var mVp: ViewPager? = null
    private var mAdapter: GalleryPagerAdapter? = null
    private var mFragmentList: MutableList<Fragment>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        initData()
        initView()
    }

    private fun initData() {
        val imagePathList = FileUtil.allFilePath
        mFragmentList = ArrayList()
        for (path in imagePathList) {
            mFragmentList!!.add(PicFragment(path))
        }
    }

    private fun initView() {
        mVp = findViewById(R.id.id_gallery_vp)
        mVp!!.setPageTransformer(true, ZoomOutPageTransformer())
        mAdapter = GalleryPagerAdapter(supportFragmentManager, this, mFragmentList!!)
        mVp!!.adapter = mAdapter
    }
}
