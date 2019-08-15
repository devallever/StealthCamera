package com.allever.stealthcamera.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.allever.stealthcamera.R;

import java.util.List;

/**
 * Created by Allever on 18/5/12.
 */

public class PicAdapter extends RecyclerView.Adapter<PicAdapter.MyVH> {
    private static final String TAG = "PicAdapter";
    private List<String> mFilePathList;
    private Context mContext;

    public PicAdapter(Context context, List<String> filePathList){
        mContext = context;
        mFilePathList = filePathList;
    }

    @Override
    public MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_picture,parent,false);
        MyVH myVH = new MyVH(view);
        return myVH;
    }

    @Override
    public void onBindViewHolder(MyVH holder, int position) {
        Glide.with(mContext).load(mFilePathList.get(position)).into(holder.ivPic);
    }

    @Override
    public int getItemCount() {
        return mFilePathList.size();
    }

    class MyVH extends RecyclerView.ViewHolder{
        ImageView ivPic;
        public MyVH(View itemView) {
            super(itemView);
            ivPic = itemView.findViewById(R.id.id_item_iv_pic);
        }
    }
}
