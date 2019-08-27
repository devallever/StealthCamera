package com.allever.stealthcamera.ui

import android.hardware.Camera
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast

import com.allever.stealthcamera.CameraManager
import com.allever.stealthcamera.R
import com.allever.stealthcamera.ui.view.CameraSurfaceView

/**
 * Created by Allever on 18/5/12.
 */

class CameraActivity : AppCompatActivity() {
    private var mIvTake: ImageView? = null
    private var mFl: FrameLayout? = null
    private var cameraSurfaceView: CameraSurfaceView? = null
    private var params: FrameLayout.LayoutParams? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        initView()
    }

    private fun initView() {
        mIvTake = findViewById(R.id.id_camera_iv_take)
        mIvTake!!.setOnClickListener {
            CameraManager.takePicture()
            Toast.makeText(this@CameraActivity, "Done", Toast.LENGTH_SHORT).show()
        }

        mFl = findViewById(R.id.id_camera_fl_preview_container)

        params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT)
    }

    override fun onResume() {
        Log.d(TAG, "onResume: ")
        super.onResume()
        CameraManager.openCamera(Camera.CameraInfo.CAMERA_FACING_BACK)
        cameraSurfaceView = CameraSurfaceView(this)
        mFl!!.addView(cameraSurfaceView, params)
    }

    override fun onStop() {
        Log.d(TAG, "onStop: ")
        super.onStop()
        mFl!!.removeView(cameraSurfaceView)
        CameraManager.releaseCamera()
        cameraSurfaceView = null
    }

    companion object {
        private val TAG = "CameraActivity"
    }
}
