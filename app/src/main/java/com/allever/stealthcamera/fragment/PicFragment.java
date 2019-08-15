package com.allever.stealthcamera.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.allever.stealthcamera.R;

/**
 * Created by Allever on 18/5/16.
 */

public class PicFragment extends Fragment {
    private String mImgPath;
    public PicFragment(){}
    @SuppressLint("ValidFragment")
    public PicFragment(String imgPath){
        mImgPath = imgPath;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_pic,container,false);
        ImageView iv = view.findViewById(R.id.id_fg_pic_iv);
        Glide.with(getActivity()).load(mImgPath).into(iv);
        return view;
    }
}
