package com.allever.stealthcamera.ui

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.content.FileProvider
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.allever.stealthcamera.BuildConfig

import com.allever.stealthcamera.RecyclerItemClickListener
import com.allever.stealthcamera.ui.adapter.PicAdapter
import com.allever.stealthcamera.R
import com.allever.stealthcamera.utils.FileUtil

import java.io.File

/**
 * Created by Allever on 18/5/11.
 */

class PictureActivity : AppCompatActivity() {
    private var mFilePathList: List<String>? = null
    private var mAdapter: PicAdapter? = null
    private var mRv: androidx.recyclerview.widget.RecyclerView? = null
    private var mPkg = ""

    /**
     * 获取带有Launcher应用包名
     */
    private val pkg: String
        get() {
            val mainIntent = Intent(Intent.ACTION_MAIN, null)
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            val packageManager = packageManager
            val apps = packageManager.queryIntentActivities(mainIntent, 0)
            var pkg = ""
            var info: ResolveInfo? = null
            for (i in apps.indices) {
                info = apps[i]
                Log.e("TAG", info!!.activityInfo.loadLabel(packageManager).toString() + " pkgName "
                        + info.activityInfo.applicationInfo.packageName + " className " + info.activityInfo.name)

                pkg = info.activityInfo.applicationInfo.packageName
                if (pkg.contains("gallery") || pkg.contains("photo")) {
                    break
                }
            }
            return pkg
        }

    /**
     * 获取系统级别的应用包名
     */
    private//系统级应用
    val systemGalleryPkg: String
        get() {
            var pkg = ""
            val packageManager = packageManager
            val applicationInfoList = packageManager.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES)
            for (app in applicationInfoList) {
                if (app.flags and ApplicationInfo.FLAG_SYSTEM != 0) {
                    pkg = app.packageName
                    Log.d("TAG", "pkg = $pkg")
                    if (pkg.contains("gallery") || pkg.contains("photo")) {
                        Log.d("TAG", "has gallery or photo\n")
                        break
                    }
                }
            }
            return pkg
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture)

        initData()
        initView()
    }

    private fun initData() {
        mFilePathList = FileUtil.allFilePath
    }

    private fun initView() {
        mRv = findViewById(R.id.id_picture_rv)
        mAdapter = PicAdapter(this, mFilePathList!!)
        mRv!!.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 3)
        mRv!!.adapter = mAdapter

        mRv!!.addOnItemTouchListener(RecyclerItemClickListener(this, mRv!!, object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                if (TextUtils.isEmpty(mPkg)) {
                    mPkg = pkg
                }
                val albumIntent = Intent(Intent.ACTION_VIEW)
                if (!TextUtils.isEmpty(mPkg)) {
                    intent.setPackage(mPkg)
                }

                val file = File(mFilePathList!![position])
                val fileUri: Uri
                fileUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //解决调用相册不显示图片的问题
                    albumIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    FileProvider.getUriForFile(
                            this@PictureActivity,
                            BuildConfig.APPLICATION_ID + ".fileprovider",
                            file
                    )
                } else {
                    Uri.fromFile(file)
                }

                albumIntent.setDataAndType(Uri.parse(fileUri.toString()), "image/*")
                startActivity(albumIntent)
            }

            override fun onItemLongClick(view: View?, position: Int) {

            }
        }))
    }

    companion object {
        private val TAG = "PictureActivity"
    }
}
