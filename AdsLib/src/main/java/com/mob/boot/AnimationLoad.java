package com.mob.boot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.mob.R;

/**
 * Created by maozhi on 15-8-21
 * maozhi@sunteng.com
 */
public class AnimationLoad extends AnimationDrawable {
    @Override
    public void addFrame(Drawable frame, int duration) {
        super.addFrame(frame, duration);
    }
    private Context mContext = null;
    public AnimationLoad(Context context){
        super();
        mContext = context;
        init();
    }
    private void init() {
        Bitmap bitmap =  BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_loading_1); addFrame(new BitmapDrawable(bitmap), 100);
        bitmap =  BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_loading_2); addFrame(new BitmapDrawable(bitmap), 100);
        bitmap =  BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_loading_3); addFrame(new BitmapDrawable(bitmap), 100);
        bitmap =  BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_loading_4); addFrame(new BitmapDrawable(bitmap), 100);
        bitmap =  BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_loading_5); addFrame(new BitmapDrawable(bitmap), 100);
        bitmap =  BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_loading_6); addFrame(new BitmapDrawable(bitmap), 100);
        bitmap =  BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_loading_7); addFrame(new BitmapDrawable(bitmap), 100);
        bitmap =  BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_loading_8); addFrame(new BitmapDrawable(bitmap), 100);
        bitmap =  BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_loading_9); addFrame(new BitmapDrawable(bitmap), 100);
        bitmap =  BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_loading_10); addFrame(new BitmapDrawable(bitmap), 100);
        bitmap =  BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_loading_11); addFrame(new BitmapDrawable(bitmap), 100);
        bitmap =  BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_loading_12); addFrame(new BitmapDrawable(bitmap), 100);
        bitmap =  BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_loading_13); addFrame(new BitmapDrawable(bitmap), 100);

        setOneShot(false);
    }
}