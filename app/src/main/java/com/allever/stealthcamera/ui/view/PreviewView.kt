package com.allever.stealthcamera.ui.view

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast

import com.allever.stealthcamera.CameraManager
import com.allever.stealthcamera.R
import com.allever.stealthcamera.utils.DisplayUtil
import com.allever.stealthcamera.utils.FloatWindowUtil
import com.allever.stealthcamera.utils.SPUtil
import kotlin.math.abs

/**
 * Created by Allever on 18/5/11.
 */

class PreviewView(private val mContext: Context) : FrameLayout(mContext) {

    /**
     * 悬浮窗的参数
     */
    private var mParams: WindowManager.LayoutParams? = null

    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private var xInScreen: Float = 0.toFloat()

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private var yInScreen: Float = 0.toFloat()

    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private var xDownInScreen: Float = 0.toFloat()

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private var yDownInScreen: Float = 0.toFloat()

    /**
     * 记录手指按下时在悬浮窗的View上的横坐标的值
     */
    private var xInView: Float = 0.toFloat()

    /**
     * 记录手指按下时在悬浮窗的View上的纵坐标的值
     */
    private var yInView: Float = 0.toFloat()

    init {
        if (SPUtil.getShowPreview(mContext)) {
            viewWidth = DisplayUtil.dip2px(mContext, 80f)
            val rate = DisplayUtil.getScreenRate(mContext)
            viewHeight = (viewWidth * rate).toInt()
        } else {
            viewWidth = DisplayUtil.dip2px(mContext, 60f)
            viewHeight = DisplayUtil.dip2px(mContext, 60f)
        }

        val surfaceView = CameraSurfaceView(mContext)
        val params = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT)
        if (SPUtil.getShowPreview(mContext)) {
            this.addView(surfaceView, params)
        } else {
            // 把surfaceView设置成大小都为1，以便后面图片覆盖
            val smallParams = LayoutParams(
                    1, 1)
            smallParams.gravity = Gravity.CENTER
            this.addView(surfaceView, smallParams)
            // 不显示预览框，则用图片把预览框覆盖
            val imageView = ImageView(mContext)
            imageView
                    .setBackgroundResource(R.drawable.ic_take_photo)
            this.addView(imageView, params)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 手指按下时记录必要数据,纵坐标的值都需要减去状态栏高度
                xInView = event.x
                yInView = event.y
                xInScreen = event.rawX
                //yInScreen = event.getRawY() - DisplayUtil.getStatusBarHeight(mContext);
                yInScreen = event.rawY
                xDownInScreen = xInScreen
                yDownInScreen = yInScreen
                Log.d(TAG, "x = $xDownInScreen, y = $yDownInScreen")
            }
            MotionEvent.ACTION_MOVE -> {
                xInScreen = event.rawX
                //yInScreen = event.getRawY() - DisplayUtil.getStatusBarHeight(mContext);
                //全屏不需要减去状态了高度
                yInScreen = event.rawY
                // 手指移动距离大于20的时候更新悬浮窗的位置
                if (Math.abs(xInScreen - xDownInScreen) > 20 || Math.abs(yInScreen - yDownInScreen) > 20) {
                    updateViewPosition()
                }
            }
            MotionEvent.ACTION_UP ->
                // 如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen和yInScreen相等，则视为触发了单击事件。
                if (abs(xDownInScreen - xInScreen) <= 20 && Math.abs(yDownInScreen - yInScreen) <= 20) {
                    CameraManager.takePicture()

                    Toast.makeText(mContext, "Done",
                            Toast.LENGTH_SHORT).show()
                }
            else -> {
            }
        }
        return true
    }

    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params
     * 小悬浮窗的参数
     */
    fun setParams(params: WindowManager.LayoutParams) {
        mParams = params
    }

    /**
     * 更新悬浮窗在屏幕中的位置。
     */
    private fun updateViewPosition() {
        mParams!!.x = (xInScreen - xInView).toInt()
        mParams!!.y = (yInScreen - yInView).toInt()
        FloatWindowUtil.updateFloatWindow(mContext, this, mParams)
    }

    companion object {
        private val TAG = "PreviewView"

        /**
         * 记录悬浮窗的宽度
         */
        var viewWidth: Int = 0

        /**
         * 记录悬浮窗的高度
         */
        var viewHeight: Int = 0
    }
}
