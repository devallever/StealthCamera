package com.allever.spycamera.view;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.allever.spycamera.CameraManager;
import com.allever.spycamera.R;
import com.allever.spycamera.utils.DisplayUtil;
import com.allever.spycamera.utils.FloatWindowUtil;
import com.allever.spycamera.utils.SPUtil;

/**
 * Created by Allever on 18/5/11.
 */

public class PreviewView extends FrameLayout{
    private static final String TAG = "PreviewView";

    /**
     * 记录悬浮窗的宽度
     */
    public static int viewWidth;

    /**
     * 记录悬浮窗的高度
     */
    public static int viewHeight;

    /**
     * 悬浮窗的参数
     */
    private WindowManager.LayoutParams mParams;

    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xInScreen;

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yInScreen;

    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownInScreen;

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private float yDownInScreen;

    /**
     * 记录手指按下时在悬浮窗的View上的横坐标的值
     */
    private float xInView;

    /**
     * 记录手指按下时在悬浮窗的View上的纵坐标的值
     */
    private float yInView;

    private Context mContext;

    public PreviewView(Context context) {
        super(context);
        mContext = context;
        if (SPUtil.getShowPreview(mContext)) {
            viewWidth = DisplayUtil.dip2px(mContext,80f);
            float rate = DisplayUtil.getScreenRate(mContext);
            viewHeight = (int) (viewWidth * rate);
        } else {
            viewWidth = DisplayUtil.dip2px(mContext, 60f);
            viewHeight = DisplayUtil.dip2px(mContext, 60f);
        }

        CameraSurfaceView surfaceView = new CameraSurfaceView(mContext);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        if (SPUtil.getShowPreview(mContext)) {
            this.addView(surfaceView, params);
        } else {
            // 把surfaceView设置成大小都为1，以便后面图片覆盖
            FrameLayout.LayoutParams smallParams = new FrameLayout.LayoutParams(
                    1, 1);
            smallParams.gravity = Gravity.CENTER;
            this.addView(surfaceView, smallParams);
            // 不显示预览框，则用图片把预览框覆盖
            ImageView imageView = new ImageView(mContext);
            imageView
                    .setBackgroundResource(R.drawable.ic_take_photo);
            this.addView(imageView, params);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                xInView = event.getX();
                yInView = event.getY();
                xInScreen = event.getRawX();
                //yInScreen = event.getRawY() - DisplayUtil.getStatusBarHeight(mContext);
                yInScreen = event.getRawY();
                xDownInScreen = xInScreen;
                yDownInScreen = yInScreen;
                Log.d(TAG, "x = " + xDownInScreen + ", y = " + yDownInScreen);
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                //yInScreen = event.getRawY() - DisplayUtil.getStatusBarHeight(mContext);
                //全屏不需要减去状态了高度
                yInScreen = event.getRawY();
                // 手指移动距离大于20的时候更新悬浮窗的位置
                if (Math.abs(xInScreen - xDownInScreen) > 20
                        || Math.abs(yInScreen - yDownInScreen) > 20) {
                    updateViewPosition();
                }
                break;
            case MotionEvent.ACTION_UP:
                // 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
                if (Math.abs(xDownInScreen - xInScreen) <= 20
                        && Math.abs(yDownInScreen - yInScreen) <= 20) {
                    CameraManager.getIns().takePicture();

                    Toast.makeText(mContext,"Done",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params
     *            小悬浮窗的参数
     */
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    /**
     * 更新悬浮窗在屏幕中的位置。
     */
    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        FloatWindowUtil.updateFloatWindow(mContext, this, mParams);
    }
}
