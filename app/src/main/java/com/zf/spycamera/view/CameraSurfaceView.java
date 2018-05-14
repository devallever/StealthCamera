package com.zf.spycamera.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zf.spycamera.CameraManager;
import com.zf.spycamera.utils.DisplayUtil;

/**
 * Created by Allever on 18/5/11.
 */

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    SurfaceHolder mSurfaceHolder = null;
    Context mContext;

    public CameraSurfaceView(Context context) {
        this(context, null);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        mSurfaceHolder = this.getHolder();
        //translucent半透明 transparent透明
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //默认全屏的比例预览
        //高宽比
        //float previewRate = DisplayUtil.getScreenRate(mContext);
        Point p = DisplayUtil.getScreenMetrics(mContext);
        float height = p.y;
        int maxHeight = (int) (height * 3 / 5);
        CameraManager.getIns().startPreview(mSurfaceHolder,maxHeight);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        CameraManager.getIns().stopCamera();
    }
}
