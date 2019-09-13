package com.allever.stealthcamera.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.allever.lib.common.ui.widget.recycler.BaseRecyclerViewAdapter
import com.allever.lib.common.ui.widget.recycler.BaseViewHolder

import com.bumptech.glide.Glide
import com.allever.stealthcamera.R
import com.android.absbase.utils.DeviceUtils
import kotlin.math.roundToInt

/**
 * Created by Allever on 18/5/12.
 */

class PicAdapter(mContext: Context, resId: Int, mFilePathList: MutableList<String>)
    : BaseRecyclerViewAdapter<String>(mContext, resId, mFilePathList) {
    private var mItemWidth = 0

    init {
        val screenWidth = DeviceUtils.SCREEN_WIDTH_PX.toFloat()
        val margin = DeviceUtils.dip2px(4f)
        mItemWidth = ((screenWidth - margin * 4) / 3).roundToInt()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val itemView = LayoutInflater.from(mContext).inflate(
                R.layout.item_picture, parent,
                false
        )
        val holder = BaseViewHolder(mContext, itemView)
        val lp = itemView.layoutParams
//        lp.width = mItemWidth.toInt()
        //根据宽高决定高度
        lp.height = mItemWidth
        itemView.layoutParams = lp
        //崩溃
//        itemView.tag = holder

        return holder
    }

    override fun bindHolder(holder: BaseViewHolder, position: Int, item: String) {
        Glide.with(mContext).load(item).into(holder.getView(R.id.id_item_iv_pic)!!)
    }

    companion object {
        private const val TAG = "PicAdapter"
    }
}
