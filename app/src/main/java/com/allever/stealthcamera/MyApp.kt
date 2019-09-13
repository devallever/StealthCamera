package com.allever.stealthcamera

import com.allever.lib.common.app.App

class MyApp: App() {
    override fun onCreate() {
        super.onCreate()
        com.android.absbase.App.setContext(this)
    }
}