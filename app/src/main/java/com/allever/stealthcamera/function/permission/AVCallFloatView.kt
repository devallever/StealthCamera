/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.allever.stealthcamera.function.permission

import android.content.Context
import android.graphics.Point
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.FrameLayout
import android.widget.Toast

import com.allever.stealthcamera.R


/**
 * Description:
 *
 * @author zhaozp
 * @since 2016-05-19
 */
class AVCallFloatView(context: Context) : FrameLayout(context) {

    /**
     * 记录手指按下时在小悬浮窗的View上的横坐标的值
     */
    private var xInView: Float = 0.toFloat()

    /**
     * 记录手指按下时在小悬浮窗的View上的纵坐标的值
     */
    private var yInView: Float = 0.toFloat()
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

    private var isAnchoring = false
    private var isShowing = false
    private var windowManager: WindowManager? = null
    private var mParams: WindowManager.LayoutParams? = null


    init {
        initView()
    }

    private fun initView() {
        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val inflater = LayoutInflater.from(context)
        val floatView = inflater.inflate(R.layout.float_window_layout, null)

        addView(floatView)

    }

    fun setParams(params: WindowManager.LayoutParams) {
        mParams = params
    }

    fun setIsShowing(isShowing: Boolean) {
        this.isShowing = isShowing
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isAnchoring) {
            return true
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                xInView = event.x
                yInView = event.y
                xDownInScreen = event.rawX
                yDownInScreen = event.rawY
                xInScreen = event.rawX
                yInScreen = event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                xInScreen = event.rawX
                yInScreen = event.rawY
                // 手指移动的时候更新小悬浮窗的位置
                updateViewPosition()
            }
            MotionEvent.ACTION_UP -> if (Math.abs(xDownInScreen - xInScreen) <= ViewConfiguration.get(context).scaledTouchSlop && Math.abs(yDownInScreen - yInScreen) <= ViewConfiguration.get(context).scaledTouchSlop) {
                // 点击效果
                Toast.makeText(context, "this float window is clicked", Toast.LENGTH_SHORT).show()
            } else {
                //吸附效果
                anchorToSide()
            }
            else -> {
            }
        }
        return true
    }

    private fun anchorToSide() {
        isAnchoring = true
        val size = Point()
        windowManager!!.defaultDisplay.getSize(size)
        val screenWidth = size.x
        val screenHeight = size.y
        val middleX = mParams!!.x + width / 2


        var animTime = 0
        var xDistance = 0
        var yDistance = 0

        val dp_25 = dp2px(15f)

        //1
        if (middleX <= dp_25 + width / 2) {
            xDistance = dp_25 - mParams!!.x
        } else if (middleX <= screenWidth / 2) {
            xDistance = dp_25 - mParams!!.x
        } else if (middleX >= screenWidth - width / 2 - dp2px(25f)) {
            xDistance = screenWidth - mParams!!.x - width - dp_25
        } else {
            xDistance = screenWidth - mParams!!.x - width - dp_25
        }//4
        //3
        //2

        //1
        if (mParams!!.y < dp_25) {
            yDistance = dp_25 - mParams!!.y
        } else if (mParams!!.y + height + dp_25 >= screenHeight) {
            yDistance = screenHeight - dp_25 - mParams!!.y - height
        }//2
        Log.e(TAG, "xDistance  $xDistance   yDistance$yDistance")

        animTime = if (Math.abs(xDistance) > Math.abs(yDistance))
            (xDistance.toFloat() / screenWidth.toFloat() * 600f).toInt()
        else
            (yDistance.toFloat() / screenHeight.toFloat() * 900f).toInt()
        this.post(AnchorAnimRunnable(Math.abs(animTime), xDistance, yDistance, System.currentTimeMillis()))
    }

    fun dp2px(dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    private inner class AnchorAnimRunnable(private val animTime: Int, private val xDistance: Int, private val yDistance: Int, private val currentStartTime: Long) : Runnable {
        private val interpolator: Interpolator
        private val startX: Int
        private val startY: Int

        init {
            interpolator = AccelerateDecelerateInterpolator()
            startX = mParams!!.x
            startY = mParams!!.y
        }

        override fun run() {
            if (System.currentTimeMillis() >= currentStartTime + animTime) {
                isAnchoring = false
                return
            }
            val delta = interpolator.getInterpolation((System.currentTimeMillis() - currentStartTime) / animTime.toFloat())
            val xMoveDistance = (xDistance * delta).toInt()
            val yMoveDistance = (yDistance * delta).toInt()
            Log.e(TAG, "delta:  $delta  xMoveDistance  $xMoveDistance   yMoveDistance  $yMoveDistance")
            mParams!!.x = startX + xMoveDistance
            mParams!!.y = startY + yMoveDistance
            if (!isShowing) {
                return
            }
            windowManager!!.updateViewLayout(this@AVCallFloatView, mParams)
            this@AVCallFloatView.postDelayed(this, 16)
        }
    }

    private fun updateViewPosition() {
        //增加移动误差
        mParams!!.x = (xInScreen - xInView).toInt()
        mParams!!.y = (yInScreen - yInView).toInt()
        Log.e(TAG, "x  " + mParams!!.x + "   y  " + mParams!!.y)
        windowManager!!.updateViewLayout(this, mParams)
    }

    companion object {
        private val TAG = "AVCallFloatView"
    }
}
