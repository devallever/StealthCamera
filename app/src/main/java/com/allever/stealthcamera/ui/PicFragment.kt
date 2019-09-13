package com.allever.stealthcamera.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.allever.stealthcamera.R
import com.bumptech.glide.Glide

/**
 * Created by Allever on 18/5/16.
 */

class PicFragment : androidx.fragment.app.Fragment {
    private var mImgPath: String? = null

    constructor()
    @SuppressLint("ValidFragment")
    constructor(imgPath: String) {
        mImgPath = imgPath
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_pic, container, false)
        val iv = view.findViewById<ImageView>(R.id.id_fg_pic_iv)
        Glide.with(activity!!).load(mImgPath).into(iv)
        return view
    }

}
