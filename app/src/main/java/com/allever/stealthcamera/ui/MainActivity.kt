package com.allever.stealthcamera.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast

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

        mIvCam!!.setOnClickListener(View.OnClickListener {
            if (!CameraUtil.checkCameraHardware(this@MainActivity)) {
                return@OnClickListener
            }

            if (FloatWindowManager.applyOrShowFloatWindow(this@MainActivity)) {
                val floatIntent = Intent(this@MainActivity, FloatWindowService::class.java)
                if (FloatWindowService.mService == null) {
                    startService(floatIntent)
                    mIvCam!!.setImageResource(R.drawable.ic_camera_on)
                } else {
                    stopService(floatIntent)
                    mIvCam!!.setImageResource(R.drawable.ic_camera_off)
                }
            } else {
                showSettingDialog()
            }
        })

        mIvSetting!!.setOnClickListener {
            //设置界面
            val intent = Intent(this@MainActivity, SettingActivity::class.java)
            startActivity(intent)
        }

        mIvPic!!.setOnClickListener {
            //相册
            val intent = Intent(this@MainActivity, PictureActivity::class.java)
            startActivity(intent)

            //                Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
            //                startActivity(intent);
        }

        mIvGenCam!!.setOnClickListener(View.OnClickListener {
            if (!CameraUtil.checkCameraHardware(this@MainActivity)) {
                return@OnClickListener
            }
            val intent = Intent(this@MainActivity, CameraActivity::class.java)
            startActivity(intent)
        })


    }

    private fun showSettingDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.string_dialog_title)
        builder.setMessage(R.string.string_dialog_message)
        builder.setPositiveButton(R.string.string_dialog_setting_button) { dialogInterface, i ->
            if (RomUtils.checkIsVivoRom()) {
                VivoUtils.applyOppoPermission(this@MainActivity)
            } else {
                FloatWindowManager.applyPermission(this@MainActivity)
            }
        }
        builder.setNegativeButton(R.string.string_dialog_cancel_button) { dialogInterface, i -> }
        builder.show()
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
