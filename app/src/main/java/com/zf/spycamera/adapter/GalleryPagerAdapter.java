package com.zf.spycamera.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Allever on 18/5/16.
 */

public class GalleryPagerAdapter extends FragmentPagerAdapter{
    private Context mContext;
    private List<Fragment> mFragmentList;

    public GalleryPagerAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList) {
        super(fm);
        mContext = context;
        mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public float getPageWidth(int position) {
        return 0.75f;
    }
}
