package com.allever.stealthcamera.ui.view

import android.content.Context
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

import com.allever.stealthcamera.CameraManager
import com.allever.stealthcamera.utils.DisplayUtil

/**
 * Created by Allever on 18/5/11.
 */

class CameraSurfaceView @JvmOverloads constructor(internal var mContext: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : SurfaceView(mContext, attrs, defStyle), SurfaceHolder.Callback {

    internal var mSurfaceHolder: SurfaceHolder? = null

    init {
        mSurfaceHolder = this.holder
        //translucent半透明 transparent透明
        mSurfaceHolder?.setFormat(PixelFormat.TRANSPARENT)
        mSurfaceHolder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        mSurfaceHolder?.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        val p = DisplayUtil.getScreenMetrics(mContext)
        val height = p.y.toFloat()
        val maxHeight = (height * 3 / 5).toInt()
        CameraManager.startPreview(mSurfaceHolder!!, maxHeight)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        CameraManager.stopCamera()
    }
}
