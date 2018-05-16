package com.zf.spycamera.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zf.spycamera.adapter.PicAdapter;
import com.zf.spycamera.R;
import com.zf.spycamera.utils.FileUtil;

import java.util.List;

/**
 * Created by Allever on 18/5/11.
 */

public class PictureActivity extends AppCompatActivity {
    private List<String> mFilePathList;
    private PicAdapter mAdapter;
    private RecyclerView mRv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        initData();
        initView();
    }

    private void initData(){
        mFilePathList = FileUtil.getAllFilePath();
    }

    private void initView(){
        mRv = findViewById(R.id.id_picture_rv);
        mAdapter = new PicAdapter(this, mFilePathList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRv.setLayoutManager(linearLayoutManager);
        mRv.setAdapter(mAdapter);
    }
}
