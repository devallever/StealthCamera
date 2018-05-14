package com.zf.spycamera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import com.zf.spycamera.utils.CameraUtil;
import com.zf.spycamera.utils.FileUtil;
import com.zf.spycamera.utils.ImageUitl;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * Created by Allever on 18/5/11.
 */

public enum  CameraManager {
    INS;

    private static final String TAG = "CameraManager";
    private boolean isPreviewing = false;
    private boolean isCapturing = false;
    private float mPreviwRate = -1f;
    private Camera.Parameters mParams;

    private Camera mCamera;

    public static CameraManager getIns(){
        return INS;
    }

    public void openCamera(int tagInfo){
        if (mCamera == null){
            try {
                mCamera = Camera.open(getCameraInfoId(tagInfo));
            }catch (Exception e){
                e.printStackTrace();
                return;
            }
            //mCamera = Camera.open(getCameraInfoId(tagInfo));
        }
    }

    public void stopCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreviewing = false;
        }
    }

    public void releaseCamera(){
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            isPreviewing = false;
            mPreviwRate = -1f;
            mCamera.release();
            mCamera = null;
        }
    }


    public void takePicture(){
        if (isPreviewing && (mCamera != null) && (isCapturing == false)) {
            isCapturing = true;
            mCamera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    Log.d(TAG, "onPictureTaken: ");
                    Bitmap b = null;
                    if (null != data) {
                        // data是字节数据，将其解析成位图
                        b = BitmapFactory.decodeByteArray(data, 0, data.length);
                        Bitmap rotaBitmap = ImageUitl.getRotateBitmap(b, 90.0f);
                        FileUtil.saveBitmap(rotaBitmap);
                    }
                    // 一般Camera在pictureCallBack后会暂停PreView，发现三星手机在底层封装能自动重启PreView功能
                    mCamera.startPreview();
                    isPreviewing = true;
                    isCapturing = false;
                }
            });
        }
    }

    /**
     * 开启预览
     *
     * @param holder
     * @param previewRate
     */
    public void startPreview(SurfaceHolder holder, float previewRate,
                               int maxHeight) {
        Log.d(TAG, "doStartPreview...rate = " + previewRate
                + ", maxHeight = " + maxHeight);
        if (isPreviewing) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            // return;
        }
        if (mCamera != null) {
            isCapturing = false;

            mParams = mCamera.getParameters();
            mParams.setPictureFormat(PixelFormat.JPEG);// 设置拍照后存储的图片格式

            // 打印camera支持的图片大小和预览大小
            CameraUtil.printSupportPictureSize(mParams);
            CameraUtil.printSupportPreviewSize(mParams);

            // 设置PreviewSize和PictureSize
            Camera.Size pictureSize = CameraUtil.getPropPictureSize(mParams,
                    maxHeight);
            mParams.setPictureSize(pictureSize.width, pictureSize.height);
            Camera.Size previewSize = CameraUtil.getPropPreviewSize(mParams,
                    maxHeight);
            mParams.setPreviewSize(previewSize.width, previewSize.height);

            // 旋转，把预览垂直
            mCamera.setDisplayOrientation(90);

            // 打印支持的聚集模式
            CameraUtil.printSupportFocusMode(mParams);

            List<String> focusModes = mParams.getSupportedFocusModes();
            if (focusModes.contains("continuous-video")) {
                mParams.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            }
            mCamera.setParameters(mParams);

            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();// 开启预览
            } catch (IOException e) {
                e.printStackTrace();
            }

            isPreviewing = true;
            mPreviwRate = previewRate;

            // 重新get一次
            mParams = mCamera.getParameters();
            Log.d(TAG,
                    "最终设置:PreviewSize--With = "
                            + mParams.getPreviewSize().width + ", Height = "
                            + mParams.getPreviewSize().height);
            Log.d(TAG,
                    "最终设置:PictureSize--With = "
                            + mParams.getPictureSize().width + ", Height = "
                            + mParams.getPictureSize().height);
        }
    }



    /**
     * 获取cameraId 前置或后置摄像头，下标0开始计算
     * @param tagInfo Camera.CameraInfo.CAMERA_FACING_BACK 或 CAMERA_FACING_FRONT*/
    private int getCameraInfoId(int tagInfo){
        Log.d(TAG, "getCameraInfoId: ");
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        // 开始遍历摄像头，得到camera info
        int cameraCount = Camera.getNumberOfCameras();
        Log.d(TAG, "getCameraInfoId: cameraCount = " + cameraCount);
        int cameraId;
        for (cameraId = 0; cameraId < cameraCount; cameraId++) {
            Camera.getCameraInfo(cameraId, cameraInfo);
            if (cameraInfo.facing == tagInfo) {
                break;
            }
        }
        Log.d(TAG, "getCameraInfoId: cameraId = " + cameraId);
        return cameraId;
    }

}
