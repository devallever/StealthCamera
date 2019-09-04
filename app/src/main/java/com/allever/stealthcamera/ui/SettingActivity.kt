package com.allever.stealthcamera.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ImageView

import com.allever.stealthcamera.FloatWindowService
import com.allever.stealthcamera.R
import com.allever.stealthcamera.utils.SPUtil

/**
 * Created by Allever on 18/5/11.
 */

class SettingActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mIvPreview: ImageView
    private lateinit var mIvFrontCameraSwitch: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initData()
        initView()
    }

    private fun initData() {

    }

    private fun initView() {
        findViewById<View>(R.id.id_setting_rl_preview_container).setOnClickListener(this)
        mIvPreview = findViewById(R.id.id_setting_iv_prieview)
        mIvPreview.setOnClickListener(this)

        findViewById<View>(R.id.id_setting_rl_camera_container).setOnClickListener(this)
        mIvFrontCameraSwitch = findViewById(R.id.id_setting_iv_front_camera)
        mIvFrontCameraSwitch.setOnClickListener(this)

        setSwitch(mIvPreview, SPUtil.getShowPreview(this))
        setSwitch(mIvFrontCameraSwitch, SPUtil.getUseFrontCamera(this))
    }

    private fun setSwitch(target: ImageView, switch: Boolean) {
        if (switch) {
            target.setImageResource(R.drawable.ic_preview_on)
        } else {
            target.setImageResource(R.drawable.ic_switch_off)
        }

    }

    override fun onClick(v: View) {
        val id = v.id
        when (id) {
            R.id.id_main_ll_menu_container, R.id.id_setting_iv_prieview -> {
                SPUtil.setShowPreview(this@SettingActivity, !SPUtil.getShowPreview(this@SettingActivity))
                setSwitch(mIvPreview, SPUtil.getShowPreview(this))
                restartFloatService()
            }

            R.id.id_setting_iv_front_camera -> {
                SPUtil.setUseFrontCamera(this@SettingActivity, !SPUtil.getUseFrontCamera(this@SettingActivity))
                setSwitch(mIvFrontCameraSwitch, SPUtil.getUseFrontCamera(this))
                restartFloatService()
            }
            else -> {
            }
        }
    }

    private fun restartFloatService() {
        if (FloatWindowService.mService != null) {
            //相机已启动
            val intent = Intent(this, FloatWindowService::class.java)
            stopService(intent)
            startService(intent)
        }
    }

    companion object {
        private val TAG = "SettingActivity"
    }
}
