package com.zf.spycamera.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zf.spycamera.RecyclerItemClickListener;
import com.zf.spycamera.adapter.PicAdapter;
import com.zf.spycamera.R;
import com.zf.spycamera.utils.FileUtil;

import java.io.File;
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
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRv.setLayoutManager(new GridLayoutManager(this,3));
        mRv.setAdapter(mAdapter);

        mRv.addOnItemTouchListener(new RecyclerItemClickListener(this, mRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent albumIntent = new Intent(Intent.ACTION_VIEW);
                File file;
                file = new File(mFilePathList.get(position));
                albumIntent.setDataAndType(Uri.fromFile(file), "image/*");
                startActivity(albumIntent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }
}
