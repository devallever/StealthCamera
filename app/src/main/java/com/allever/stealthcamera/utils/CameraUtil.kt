package com.allever.stealthcamera.utils

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.util.Log

/**
 * Created by Allever on 18/5/11.
 */

object CameraUtil {
    private const val TAG = "CameraUtil"
    /**
     * 打印支持的pictureSizes
     *
     * @param parameters
     */
    fun printSupportPictureSize(parameters: Camera.Parameters?) {
        if (parameters == null) {
            return
        }
        val pictureSizes = parameters.supportedPictureSizes
        for (size in pictureSizes) {
            Log.d(TAG, "pictureSizes:width = " + size.width + " height = " + size.height)
        }
    }

    /**
     * 打印支持的previewSizes
     *
     * @param params
     */
    fun printSupportPreviewSize(params: Camera.Parameters?) {
        if (params == null) {
            return
        }
        val previewSizes = params.supportedPreviewSizes
        for (size in previewSizes) {
            Log.d(TAG, "previewSizes:width = " + size.width + " height = " + size.height)
        }
    }

    /**
     * 打印支持的聚焦模式
     *
     * @param parameters
     */
    fun printSupportFocusMode(parameters: Camera.Parameters?) {
        if (parameters == null) {
            return
        }
        val focusModes = parameters.supportedFocusModes
        for (mode in focusModes) {
            Log.d(TAG, "focusModes--$mode")
        }
    }

    /**
     * 获取相机拍照图片大小 */
    fun getPropPictureSize(parameters: Camera.Parameters?, maxHeight: Int): Camera.Size? {
        if (parameters == null) {
            return null
        }
        val pictureSizes = parameters.supportedPictureSizes
        var i = 0
        while (i < pictureSizes.size) {
            val size = pictureSizes[i]
            Log.d(TAG, "getPropPictureSize: width = " + size.width + " height = " + size.height + " maxHeight = " + maxHeight)
            if (size.height <= maxHeight) {
                break
            }
            i++
        }
        return pictureSizes[i]
    }

    /**
     * 获取相机预览大小
     */
    fun getPropPreviewSize(parameters: Camera.Parameters?, maxHeight: Int): Camera.Size? {
        if (parameters == null) {
            return null
        }
        val previewSizes = parameters.supportedPreviewSizes
        var i = 0
        while (i < previewSizes.size) {
            val size = previewSizes[i]
            if (size.height <= maxHeight) {
                break
            }
            i++
        }
        return previewSizes[i]
    }

    /** Check if this device has a camera  */
    fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }
}
