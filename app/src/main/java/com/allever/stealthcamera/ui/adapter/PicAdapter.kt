package com.allever.stealthcamera.ui.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.allever.stealthcamera.R

/**
 * Created by Allever on 18/5/12.
 */

class PicAdapter(private val mContext: Context, private val mFilePathList: List<String>) : androidx.recyclerview.widget.RecyclerView.Adapter<PicAdapter.MyVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_picture, parent, false)
        return MyVH(view)
    }

    override fun onBindViewHolder(holder: MyVH, position: Int) {
        Glide.with(mContext).load(mFilePathList[position]).into(holder.ivPic)
    }

    override fun getItemCount(): Int {
        return mFilePathList.size
    }

    inner class MyVH(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var ivPic: ImageView = itemView.findViewById(R.id.id_item_iv_pic)

    }

    companion object {
        private val TAG = "PicAdapter"
    }
}
