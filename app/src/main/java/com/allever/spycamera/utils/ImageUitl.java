package com.allever.spycamera.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by Allever on 18/5/11.
 */

public class ImageUitl {
    /**
     * 获取旋转后的图片
     * @param b
     * @param rotateDegree 旋转角度
     * @return
     */
    public static Bitmap getRotateBitmap(Bitmap b, float rotateDegree){
        if (b == null){
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate((float)rotateDegree);
        Bitmap rotaBitmap = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, false);
        return rotaBitmap;
    }
}
