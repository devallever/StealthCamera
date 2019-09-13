package com.allever.stealthcamera

import com.allever.lib.common.app.App
import com.allever.lib.umeng.UMeng

class MyApp: App() {
    override fun onCreate() {
        super.onCreate()
        com.android.absbase.App.setContext(this)
        UMeng.init(this)
    }
}