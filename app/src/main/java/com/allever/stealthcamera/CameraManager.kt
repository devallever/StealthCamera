package com.allever.stealthcamera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.hardware.Camera
import android.util.Log
import android.view.SurfaceHolder

import com.allever.stealthcamera.utils.CameraUtil
import com.allever.stealthcamera.utils.FileUtil
import com.allever.stealthcamera.utils.ImageUitl

import java.io.IOException
import android.content.Context
import android.view.Surface
import android.view.WindowManager
import com.allever.lib.common.app.App


/**
 * Created by Allever on 18/5/11.
 */

object CameraManager {
    private const val TAG = "CameraManager"
    private var isPreviewing = false
    private var isCapturing = false
    private var mParams: Camera.Parameters? = null

    private var mCamera: Camera? = null

    fun openCamera(tagInfo: Int) {
        if (mCamera == null) {
            try {
                mCamera = Camera.open(getCameraInfoId(tagInfo))
            } catch (e: Exception) {
                e.printStackTrace()
                return
            }

            //mCamera = Camera.open(getCameraInfoId(tagInfo));
        }
    }

    fun stopCamera() {
        mCamera?.setPreviewCallback(null)
        mCamera?.stopPreview()
        isPreviewing = false
    }

    fun releaseCamera() {
        mCamera?.setPreviewCallback(null)
        mCamera?.stopPreview()
        isPreviewing = false
        mCamera?.release()
        mCamera = null
    }


    fun takePicture() {
        if (isPreviewing && mCamera != null && !isCapturing) {
            isCapturing = true
            mCamera?.takePicture(null, null, Camera.PictureCallback { data, camera ->
                Log.d(TAG, "onPictureTaken: ")
                val b: Bitmap?
                if (null != data) {
                    // data是字节数据，将其解析成位图
                    b = BitmapFactory.decodeByteArray(data, 0, data.size)
                    val cameraId = getCameraInfoId(Camera.CameraInfo.CAMERA_FACING_BACK)
                    val degree = getCameraRotationDegree(cameraId)
                    val rotaBitmap = ImageUitl.getRotateBitmap(b, degree.toFloat())
                    if (rotaBitmap != null) {
                        FileUtil.saveBitmap(rotaBitmap)
                    }
                }
                // 一般Camera在pictureCallBack后会暂停PreView，发现三星手机在底层封装能自动重启PreView功能
                mCamera?.startPreview()
                isPreviewing = true
                isCapturing = false
            })
        }
    }

    /**
     * 开启预览
     *
     * @param holder
     */
    fun startPreview(holder: SurfaceHolder,
                     maxHeight: Int) {
        Log.d(TAG,
                "maxHeight = $maxHeight")
        if (isPreviewing) {
            mCamera?.setPreviewCallback(null)
            mCamera?.stopPreview()
            // return;
        }

        isCapturing = false

        mParams = mCamera?.parameters
        mParams?.pictureFormat = PixelFormat.JPEG// 设置拍照后存储的图片格式

        // 打印camera支持的图片大小和预览大小
        CameraUtil.printSupportPictureSize(mParams)
        CameraUtil.printSupportPreviewSize(mParams)

        // 设置PreviewSize和PictureSize
        val pictureSize = CameraUtil.getPropPictureSize(mParams,
                maxHeight)
        mParams?.setPictureSize(pictureSize?.width!!, pictureSize.height)
        val previewSize = CameraUtil.getPropPreviewSize(mParams,
                maxHeight)
        mParams?.setPreviewSize(previewSize?.width!!, previewSize.height)

        // 旋转，把预览垂直, 不同的设备角度不同
        val cameraId = getCameraInfoId(Camera.CameraInfo.CAMERA_FACING_BACK)
        val degree = getCameraRotationDegree(cameraId)
        mCamera?.setDisplayOrientation(degree)

        // 打印支持的聚集模式
        CameraUtil.printSupportFocusMode(mParams)

        val focusModes = mParams?.supportedFocusModes
        if (focusModes?.isNotEmpty() == true) {
            for (mode in focusModes) {
                Log.d(TAG, "startPreview: mode = $mode")
            }
        }

        if (focusModes?.contains("continuous-video") == true) {
            mParams?.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO
        }
        mCamera?.parameters = mParams

        try {
            mCamera?.setPreviewDisplay(holder)
            mCamera?.startPreview()// 开启预览
        } catch (e: IOException) {
            e.printStackTrace()
        }

        isPreviewing = true

        // 重新get一次
        mParams = mCamera?.parameters
        Log.d(TAG,
                "最终设置:PreviewSize--With = "
                        + mParams?.previewSize?.width + ", Height = "
                        + mParams?.previewSize?.height)
        Log.d(TAG,
                "最终设置:PictureSize--With = "
                        + mParams?.pictureSize?.width + ", Height = "
                        + mParams?.pictureSize?.height)
    }


    /**
     * 获取cameraId 前置或后置摄像头，下标0开始计算
     * @param tagInfo Camera.CameraInfo.CAMERA_FACING_BACK 或 CAMERA_FACING_FRONT
     */
    private fun getCameraInfoId(tagInfo: Int): Int {
        Log.d(TAG, "getCameraInfoId: ")
        val cameraInfo = Camera.CameraInfo()
        // 开始遍历摄像头，得到camera info
        val cameraCount = Camera.getNumberOfCameras()
        Log.d(TAG, "getCameraInfoId: cameraCount = $cameraCount")
        var cameraId = 0
        while (cameraId < cameraCount) {
            Camera.getCameraInfo(cameraId, cameraInfo)
            if (cameraInfo.facing == tagInfo) {
                break
            }
            cameraId++
        }
        Log.d(TAG, "getCameraInfoId: cameraId = $cameraId")
        return cameraId
    }


    fun getRotationOnTakePickPic(cameraId: Int): Int {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(cameraId, info)
        val windowManager = App.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val rotation = windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }
        return if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
            (info.orientation - degrees + 360) % 360
        } else {  // back-facing camera
            (info.orientation + degrees) % 360
        }
    }

    fun getCameraRotationDegreeOnPreview(cameraId: Int): Int {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(cameraId, info)
        val windowManager = App.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val rotation = windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }
        var result: Int
        //前置
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360
            result = (360 - result) % 360
        } else {
            result = (info.orientation - degrees + 360) % 360
        }//后置

        return result
    }
}
