package com.allever.stealthcamera.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Allever on 18/5/11.
 */

public class FileUtil {
    /**初始化保存路径
     * @return
     */
    public static String initPath(){
        String path = null;
        //path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/" + "zhifei/spycamera";
        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/" + "zhifei/spycamera";
        File f = new File(path);
        if(!f.exists()){
            f.mkdirs();
        }
        return path;
    }

    /**保存Bitmap到sdcard
     * @param b
     */
    public static void saveBitmap(Bitmap b){

        String path = initPath();
        long dataTake = System.currentTimeMillis();
        String jpegName = path + "/" + dataTake +".jpg";
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static List<String> getAllFilePath() {
        List<String> list, lastList;
        list = new ArrayList<String>();
        lastList = new ArrayList<String>();
        File dir = new File(FileUtil.initPath());
        File[] files = dir.listFiles();
        if (files != null) {
            String fileName;
            String filePath;
            for (int i = 0; i < files.length; i++) {
                fileName = files[i].getName();
                filePath = FileUtil.initPath()+"/"+fileName;
                list.add(filePath);
            }
        }

        for (int i = list.size()-1; i >=0; i--) {
            lastList.add(list.get(i));
        }
        return lastList;
    }
}
