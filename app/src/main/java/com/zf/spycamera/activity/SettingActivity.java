package com.zf.spycamera.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zf.spycamera.R;
import com.zf.spycamera.utils.SPUtil;

/**
 * Created by Allever on 18/5/11.
 */

public class SettingActivity extends AppCompatActivity {
    private static final String TAG = "SettingActivity";
    private RelativeLayout mRlPreviewCont;
    private ImageView mIvPreview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initData();
        initView();
    }

    private void initData(){

    }

    private void initView(){
        mRlPreviewCont = findViewById(R.id.id_setting_rl_preview_container);
        mIvPreview = findViewById(R.id.id_setting_iv_prieview);

        refreshCb();

        mIvPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.setShowPreview( SettingActivity.this,!SPUtil.getShowPrivate(SettingActivity.this));
                refreshCb();
            }
        });

        mRlPreviewCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtil.setShowPreview( SettingActivity.this,!SPUtil.getShowPrivate(SettingActivity.this));
                refreshCb();
            }
        });

    }

    private void refreshCb(){
        if (SPUtil.getShowPrivate(this)){
            Log.d(TAG, "refreshCb: isShow = " + SPUtil.getShowPrivate(this));
            mIvPreview.setImageResource(R.drawable.checkbox_on);
        }else {
            mIvPreview.setImageResource(R.drawable.checkbox_off);
        }
    }
}
