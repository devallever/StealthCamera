package com.allever.stealthcamera.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Allever on 18/5/11.
 */

object SPUtil {
    private val KEY_PRIVATE = "is_show"
    private val SP_NAME = "setting"
    private val KEY_Y_VALUE = "y_value"
    private val KEY_X_VALUE = "x_value"
    fun setShowPreview(context: Context?, value: Boolean) {
        if (context == null) {
            return
        }
        val editor = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
        editor.putBoolean(KEY_PRIVATE, value)
        editor.commit()
    }

    fun getShowPreview(context: Context?): Boolean {
        if (context == null) {
            return false
        }
        val sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        val isPrivate = sharedPreferences.getBoolean(KEY_PRIVATE, true)
        return isPrivate
    }

    fun setLastY(context: Context?, value: Float) {
        if (context == null) {
            return
        }
        val editor = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
        editor.putFloat(KEY_Y_VALUE, value)
        editor.commit()
    }

    fun setLastX(context: Context?, value: Float) {
        if (context == null) {
            return
        }
        val editor = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE).edit()
        editor.putFloat(KEY_Y_VALUE, value)
        editor.commit()
    }

    fun getLastY(context: Context?): Float {
        if (context == null) {
            return -1.0f
        }
        val value: Float
        val sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        value = sharedPreferences.getFloat(KEY_Y_VALUE, -1.0f)
        return value
    }

    fun getLastX(context: Context?): Float {
        if (context == null) {
            return -1.0f
        }
        val value: Float
        val sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        value = sharedPreferences.getFloat(KEY_X_VALUE, -1.0f)
        return value
    }

    fun clearLastXY(context: Context) {
        setLastX(context, -1.0f)
        setLastY(context, -1.0f)
    }

}
