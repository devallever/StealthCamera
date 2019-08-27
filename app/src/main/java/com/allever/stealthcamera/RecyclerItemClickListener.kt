package com.allever.stealthcamera

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * Created by Allever on 2016/12/16.
 */

class RecyclerItemClickListener(context: Context, recyclerView: androidx.recyclerview.widget.RecyclerView, private val mListener: OnItemClickListener?) : androidx.recyclerview.widget.RecyclerView.OnItemTouchListener {
    private val mGestureDetector: GestureDetector

    init {

        mGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                val childView = recyclerView.findChildViewUnder(e.x, e.y)

                if (childView != null && mListener != null) {
                    mListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView))
                }
            }
        })
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }

    override fun onInterceptTouchEvent(view: androidx.recyclerview.widget.RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView))
        }
        return false
    }

    override fun onTouchEvent(rv: androidx.recyclerview.widget.RecyclerView, e: MotionEvent) {

    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View?, position: Int)
    }
}