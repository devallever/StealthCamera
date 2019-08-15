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
                var b: Bitmap? = null
                if (null != data) {
                    // data是字节数据，将其解析成位图
                    b = BitmapFactory.decodeByteArray(data, 0, data.size)
                    val rotaBitmap = ImageUitl.getRotateBitmap(b, 90.0f)
                    FileUtil.saveBitmap(rotaBitmap)
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
        mParams?.setPictureSize(pictureSize.width, pictureSize.height)
        val previewSize = CameraUtil.getPropPreviewSize(mParams,
                maxHeight)
        mParams?.setPreviewSize(previewSize.width, previewSize.height)

        // 旋转，把预览垂直
        mCamera?.setDisplayOrientation(90)

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

}
