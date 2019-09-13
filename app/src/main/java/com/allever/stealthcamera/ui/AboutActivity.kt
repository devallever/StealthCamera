package com.allever.stealthcamera.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.allever.lib.common.app.BaseActivity
import com.allever.stealthcamera.R

class AboutActivity: BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        findViewById<View>(R.id.about_privacy).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_right -> {
                finish()
            }
            R.id.about_privacy -> {
//                val privacyUrl = "https://plus.google.com/116794250597377070773/posts/SYoEZWDm77x"
//                SystemUtils.startWebView(App.context, privacyUrl)
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AboutActivity::class.java)
            context.startActivity(intent)
        }
    }
}