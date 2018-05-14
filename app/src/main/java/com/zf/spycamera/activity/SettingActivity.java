package com.zf.spycamera.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zf.spycamera.FloatWindowService;
import com.zf.spycamera.R;
import com.zf.spycamera.utils.FloatWindowUtil;
import com.zf.spycamera.utils.SPUtil;
import com.zf.spycamera.view.PreviewView;

import java.util.Set;

/**
 * Created by Allever on 18/5/11.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
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
        mRlPreviewCont.setOnClickListener(this);
        mIvPreview.setOnClickListener(this);

        refreshCb();
    }

    private void refreshCb(){
        if (SPUtil.getShowPreview(this)){
            Log.d(TAG, "refreshCb: isShow = " + SPUtil.getShowPreview(this));
            mIvPreview.setImageResource(R.drawable.checkbox_on);
        }else {
            mIvPreview.setImageResource(R.drawable.checkbox_off);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.id_main_ll_menu_container:
            case R.id.id_setting_iv_prieview:
                SPUtil.setShowPreview( SettingActivity.this,!SPUtil.getShowPreview(SettingActivity.this));
                refreshCb();
                if (FloatWindowService.mService != null){
                    //相机已启动
                    Intent intent = new Intent(this, FloatWindowService.class);
                    stopService(intent);
                    startService(intent);
                }
                break;
            default:
                break;
        }
    }
}
