package com.allever.stealthcamera.ui

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.allever.lib.common.util.DLog
import com.allever.lib.common.util.ToastUtils
import com.allever.lib.permission.PermissionCompat
import com.allever.lib.permission.PermissionListener
import com.allever.lib.permission.PermissionManager

import com.allever.stealthcamera.FloatWindowService
import com.allever.stealthcamera.R
import com.allever.stealthcamera.function.permission.FloatWindowManager
import com.allever.stealthcamera.function.permission.rom.RomUtils
import com.allever.stealthcamera.function.permission.rom.VivoUtils
import com.allever.stealthcamera.utils.CameraUtil

class MainActivity : AppCompatActivity() {

    private var mIvCam: ImageView? = null
    private var mIvSetting: ImageView? = null
    private var mIvPic: ImageView? = null
    private var mIvGenCam: ImageView? = null

    private var mPrevClickBackTime: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
        initView()

        requestPermission()
    }

    override fun onResume() {
        super.onResume()
        DLog.d("onResume")
    }

    override fun onStop() {
        super.onStop()
        DLog.d("onStop")
    }

    private fun initData() {}

    private fun initView() {
        mIvCam = findViewById(R.id.id_main_iv_camera)
        mIvSetting = findViewById(R.id.id_main_iv_settings)
        mIvPic = findViewById(R.id.id_main_iv_pictures)
        mIvGenCam = findViewById(R.id.id_main_iv_general_camera)

        if (FloatWindowService.mService == null) {
            mIvCam!!.setImageResource(R.drawable.ic_camera_off)
        } else {
            mIvCam!!.setImageResource(R.drawable.ic_camera_on)
        }

        mIvCam?.setOnClickListener {
            requestPermission(Runnable {
                if (!CameraUtil.checkCameraHardware(this@MainActivity)) {
                    return@Runnable
                }
                if (FloatWindowManager.applyOrShowFloatWindow(this@MainActivity)) {
                    val floatIntent = Intent(this@MainActivity, FloatWindowService::class.java)
                    if (FloatWindowService.mService == null) {
                        startService(floatIntent)
                        mIvCam?.setImageResource(R.drawable.ic_camera_on)
                    } else {
                        stopService(floatIntent)
                        mIvCam?.setImageResource(R.drawable.ic_camera_off)
                    }
                } else {
                    showSettingDialog()
                }
            })
        }

        mIvSetting?.setOnClickListener {
            //设置界面
            val intent = Intent(this@MainActivity, SettingActivity::class.java)
            startActivity(intent)
        }

        mIvPic?.setOnClickListener {
            if (PermissionManager.hasPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //相册
                val intent = Intent(this@MainActivity, PictureActivity::class.java)
                startActivity(intent)

            } else {
                requestPermission()
            }
        }

        mIvGenCam?.setOnClickListener(View.OnClickListener {
            if (!CameraUtil.checkCameraHardware(this@MainActivity)) {
                return@OnClickListener
            }
            if (PermissionManager.hasPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (FloatWindowService.mService != null) {
                    //停止预览
                    val floatIntent = Intent(this@MainActivity, FloatWindowService::class.java)
                    stopService(floatIntent)
                    mIvCam?.setImageResource(R.drawable.ic_camera_off)
                }

                val intent = Intent(this@MainActivity, CameraActivity::class.java)
                startActivity(intent)
            } else {
                requestPermission()
            }
        })


    }


    private fun requestPermission(grantedTask: Runnable? = null) {
        PermissionManager.request(object : PermissionListener {
            override fun onGranted(grantedList: MutableList<String>) {
                grantedTask?.run()
            }

            override fun onDenied(deniedList: MutableList<String>) {
                super.onDenied(deniedList)
                ToastUtils.show("拒绝权限无法使用")
            }

            override fun alwaysDenied(deniedList: MutableList<String>) {
                super.alwaysDenied(deniedList)
                PermissionManager.jumpPermissionSetting(this@MainActivity, 0,
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog.dismiss()
                        })
            }

        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun showSettingDialog() {
        AlertDialog.Builder(this)
                .setTitle(R.string.string_dialog_title)
                .setMessage(R.string.string_dialog_message)
                .setPositiveButton(R.string.string_dialog_setting_button) { dialogInterface, i ->
                    if (RomUtils.checkIsVivoRom()) {
                        VivoUtils.applyOppoPermission(this@MainActivity)
                    } else {
                        FloatWindowManager.applyPermission(this@MainActivity)
                    }
                }
                .setNegativeButton(R.string.string_dialog_cancel_button) { dialogInterface, i -> }
                .show()
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (mPrevClickBackTime == -1L || currentTime - mPrevClickBackTime > 3000) {
            mPrevClickBackTime = currentTime
            Toast.makeText(this, "Press again to exit",
                    Toast.LENGTH_LONG).show()
            return
        }
        super.onBackPressed()
    }
}
