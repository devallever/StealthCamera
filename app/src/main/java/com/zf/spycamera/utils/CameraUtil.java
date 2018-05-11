package com.zf.spycamera.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;

import java.util.List;

/**
 * Created by Allever on 18/5/11.
 */

public class CameraUtil {
    private static final String TAG = "CameraUtil";
    /**
     * 打印支持的pictureSizes
     *
     * @param parameters
     */
    public static void printSupportPictureSize(Camera.Parameters parameters) {
        if (parameters == null) {
            return;
        }
        List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
        for (Camera.Size size: pictureSizes){
            Log.d(TAG, "pictureSizes:width = " + size.width + " height = " + size.height);
        }
    }

    /**
     * 打印支持的previewSizes
     *
     * @param params
     */
    public static void printSupportPreviewSize(Camera.Parameters params) {
        if (params == null) {
            return;
        }
        List<Camera.Size> previewSizes = params.getSupportedPreviewSizes();
        for (Camera.Size size: previewSizes){
            Log.d(TAG, "previewSizes:width = " + size.width + " height = " + size.height);
        }
    }

    /**
     * 打印支持的聚焦模式
     *
     * @param parameters
     */
    public static void printSupportFocusMode(Camera.Parameters parameters) {
        if (parameters == null) {
            return;
        }
        List<String> focusModes = parameters.getSupportedFocusModes();
        for (String mode : focusModes) {
            Log.d(TAG, "focusModes--" + mode);
        }
    }

    public static Camera.Size getPropPictureSize(Camera.Parameters parameters, int maxHeight) {
        if (parameters == null) {
            return null;
        }
        List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();
        int i;
        for (i = 0; i < pictureSizes.size(); i++) {
            Camera.Size size = pictureSizes.get(i);
            if (size.width <= maxHeight) {
                break;
            }
        }
        return pictureSizes.get(i);
    }

    public static Camera.Size getPropPreviewSize(Camera.Parameters parameters, int maxHeight) {
        if (parameters == null) {
            return null;
        }
        List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();
        int i;
        for (i = 0; i < previewSizes.size(); i++) {
            Camera.Size size = previewSizes.get(i);
            if (size.width <= maxHeight) {
                break;
            }
        }
        return previewSizes.get(i);
    }

    /** Check if this device has a camera */
    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
}
