package com.allever.stealthcamera.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout

import com.allever.stealthcamera.FloatWindowService
import com.allever.stealthcamera.R
import com.allever.stealthcamera.utils.SPUtil

/**
 * Created by Allever on 18/5/11.
 */

class SettingActivity : AppCompatActivity(), View.OnClickListener {
    private var mRlPreviewCont: RelativeLayout? = null
    private var mIvPreview: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initData()
        initView()
    }

    private fun initData() {

    }

    private fun initView() {
        mRlPreviewCont = findViewById(R.id.id_setting_rl_preview_container)
        mIvPreview = findViewById(R.id.id_setting_iv_prieview)
        mRlPreviewCont!!.setOnClickListener(this)
        mIvPreview!!.setOnClickListener(this)

        refreshCb()
    }

    private fun refreshCb() {
        if (SPUtil.getShowPreview(this)) {
            Log.d(TAG, "refreshCb: isShow = " + SPUtil.getShowPreview(this))
            mIvPreview!!.setImageResource(R.drawable.ic_preview_on)
        } else {
            mIvPreview!!.setImageResource(R.drawable.ic_preview_off)
        }
    }

    override fun onClick(v: View) {
        val id = v.id
        when (id) {
            R.id.id_main_ll_menu_container, R.id.id_setting_iv_prieview -> {
                SPUtil.setShowPreview(this@SettingActivity, !SPUtil.getShowPreview(this@SettingActivity))
                refreshCb()
                if (FloatWindowService.mService != null) {
                    //相机已启动
                    val intent = Intent(this, FloatWindowService::class.java)
                    stopService(intent)
                    startService(intent)
                }
            }
            else -> {
            }
        }
    }

    companion object {
        private val TAG = "SettingActivity"
    }
}
