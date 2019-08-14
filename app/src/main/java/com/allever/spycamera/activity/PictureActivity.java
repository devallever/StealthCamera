package com.allever.spycamera.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.allever.spycamera.RecyclerItemClickListener;
import com.allever.spycamera.adapter.PicAdapter;
import com.allever.spycamera.R;
import com.allever.spycamera.utils.FileUtil;

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
    private String mPkg = "";

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
        mRv.setLayoutManager(new GridLayoutManager(this,3));
        mRv.setAdapter(mAdapter);

        mRv.addOnItemTouchListener(new RecyclerItemClickListener(this, mRv, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (TextUtils.isEmpty(mPkg)){
                    mPkg = getPkg();
                }
                if (!TextUtils.isEmpty(mPkg)){
                    Intent intent=new Intent();
                    intent.setPackage(mPkg);
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

    /**
     * 获取带有Launcher应用包名
     * */
    private String getPkg(){
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final PackageManager packageManager = getPackageManager();
        List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
        String pkg = "";
        ResolveInfo info = null;
        for (int i = 0; i < apps.size(); i++) {
            info = apps.get(i);
            Log.e("TAG", info.activityInfo.loadLabel(packageManager) + " pkgName "
                    + info.activityInfo.applicationInfo.packageName + " className " + info.activityInfo.name);

            pkg = info.activityInfo.applicationInfo.packageName;
            if (pkg.contains("gallery") || pkg.contains("photo")){
//                Log.e("TAG", info.activityInfo.loadLabel(packageManager) + " pkgName "
//                    + info.activityInfo.applicationInfo.packageName + " className " + info.activityInfo.name);
                break;
            }
        }
        return pkg;
    }

    /**
     * 获取系统级别的应用包名
     * */
    private String getSystemGalleryPkg(){
        String pkg = "";
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> applicationInfoList = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (ApplicationInfo app: applicationInfoList){
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0){
                //系统级应用
                pkg = app.packageName;
                Log.d("TAG", "pkg = " + pkg );
                if (pkg.contains("gallery") || pkg.contains("photo")){
                    Log.d("TAG", "has gallery or photo\n");
                    break;
                }
            }
        }
        return pkg;
    }
}
