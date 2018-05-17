package com.zf.spycamera.activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private static final String TAG = "PictureActivity";
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

                ResolveInfo info = getPkg();
                if (info != null){
                    Intent intent=new Intent();
                    intent.setPackage(info.activityInfo.applicationInfo.packageName);
                    File file=new File(mFilePathList.get(position));
                    intent.setDataAndType(Uri.fromFile(file),"image/*");
                    intent.setAction(Intent.ACTION_VIEW);
                    startActivity(intent);
                }else {
                    Intent albumIntent = new Intent(Intent.ACTION_VIEW);
                    File file;
                    file = new File(mFilePathList.get(position));
                    albumIntent.setDataAndType(Uri.fromFile(file), "image/*");
                    startActivity(albumIntent);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));
    }

    private ResolveInfo getPkg(){
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final PackageManager packageManager = getPackageManager();
        List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
        String pkg = "";
        ResolveInfo info = null;
        for (int i = 0; i < apps.size(); i++) {
            info = apps.get(i);
//            Log.e("TAG", info.activityInfo.loadLabel(packageManager) + " pkgName "
//                    + info.activityInfo.applicationInfo.packageName + " className " + info.activityInfo.name);
//            String str = info.activityInfo.loadLabel(packageManager) + " pkgName "
//                    + info.activityInfo.applicationInfo.packageName + " className " + info.activityInfo.name;
//            Log.d(TAG, "printPKGName: " + str);

            pkg = info.activityInfo.applicationInfo.packageName;
            if (pkg.contains("photo") || pkg.contains("gallery")){
                Log.e("TAG", info.activityInfo.loadLabel(packageManager) + " pkgName "
                    + info.activityInfo.applicationInfo.packageName + " className " + info.activityInfo.name);
                String str = info.activityInfo.loadLabel(packageManager) + " pkgName "
                    + info.activityInfo.applicationInfo.packageName + " className " + info.activityInfo.name;
                Log.d(TAG, "printPKGName: " + str);
                break;
            }
        }
        return info;
    }
}
