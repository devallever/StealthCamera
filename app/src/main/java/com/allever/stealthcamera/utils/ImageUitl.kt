package com.allever.stealthcamera.utils

import android.graphics.Bitmap
import android.graphics.Matrix

/**
 * Created by Allever on 18/5/11.
 */

object ImageUitl {
    /**
     * 获取旋转后的图片
     * @param b
     * @param rotateDegree 旋转角度
     * @return
     */
    fun getRotateBitmap(b: Bitmap?, rotateDegree: Float): Bitmap? {
        if (b == null) {
            return null
        }
        val matrix = Matrix()
        matrix.postRotate(rotateDegree)
        return Bitmap.createBitmap(b, 0, 0, b.width, b.height, matrix, false)
    }
}
