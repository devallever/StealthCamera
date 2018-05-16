package com.zf.spycamera.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.zf.spycamera.R;
import com.zf.spycamera.adapter.GalleryPagerAdapter;
import com.zf.spycamera.fragment.PicFragment;
import com.zf.spycamera.utils.FileUtil;
import com.zf.spycamera.view.ZoomOutPageTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allever on 18/5/16.
 */

public class GalleryActivity extends AppCompatActivity {
    private ViewPager mVp;
    private GalleryPagerAdapter mAdapter;
    private List<Fragment> mFragmentList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        initData();
        initView();
    }

    private void initData(){
        List<String> imagePathList = FileUtil.getAllFilePath();
        mFragmentList = new ArrayList<>();
        for (String path: imagePathList){
            mFragmentList.add(new PicFragment(path));
        }
    }
    private void initView(){
        mVp = findViewById(R.id.id_gallery_vp);
        mVp.setPageTransformer(true, new ZoomOutPageTransformer());
        mAdapter = new GalleryPagerAdapter(getSupportFragmentManager(),this,mFragmentList);
        mVp.setAdapter(mAdapter);
    }
}
