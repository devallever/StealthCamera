package com.allever.stealthcamera.utils

import android.graphics.Bitmap
import android.os.Environment

import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList

/**
 * Created by Allever on 18/5/11.
 */

object FileUtil {

    val allFilePath: List<String>
        get() {
            val list: MutableList<String>
            val lastList: MutableList<String>
            list = ArrayList()
            lastList = ArrayList()
            val dir = File(initPath())
            val files = dir.listFiles()
            if (files != null) {
                var fileName: String
                var filePath: String
                for (i in files.indices) {
                    fileName = files[i].name
                    filePath = initPath() + "/" + fileName
                    list.add(filePath)
                }
            }

            for (i in list.indices.reversed()) {
                lastList.add(list[i])
            }
            return lastList
        }

    /**初始化保存路径
     * @return
     */
    private fun initPath(): String {
        var path: String?
        //path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/" + "zhifei/spycamera";
        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/" + "zhifei/spycamera"
        val f = File(path)
        if (!f.exists()) {
            f.mkdirs()
        }
        return path
    }

    /**保存Bitmap到sdcard
     * @param b
     */
    fun saveBitmap(b: Bitmap) {

        val path = initPath()
        val dataTake = System.currentTimeMillis()
        val jpegName = "$path/$dataTake.jpg"
        try {
            val fout = FileOutputStream(jpegName)
            val bos = BufferedOutputStream(fout)
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            bos.flush()
            bos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}
