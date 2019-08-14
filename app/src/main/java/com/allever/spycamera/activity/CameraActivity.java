package com.allever.spycamera.activity;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.allever.spycamera.CameraManager;
import com.allever.spycamera.R;
import com.allever.spycamera.view.CameraSurfaceView;

/**
 * Created by Allever on 18/5/12.
 */

public class CameraActivity extends AppCompatActivity {
    private static final String TAG = "CameraActivity";
    private ImageView mIvTake;
    private FrameLayout mFl;
    private CameraSurfaceView cameraSurfaceView;
    FrameLayout.LayoutParams params;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        initView();
    }

    private void initView(){
        mIvTake = findViewById(R.id.id_camera_iv_take);
        mIvTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraManager.getIns().takePicture();
                Toast.makeText(CameraActivity.this, "Done", Toast.LENGTH_SHORT).show();
            }
        });

        mFl = findViewById(R.id.id_camera_fl_preview_container);

        params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        CameraManager.getIns().openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        cameraSurfaceView = new CameraSurfaceView(this);
        mFl.addView(cameraSurfaceView,params);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop: ");
        super.onStop();
        mFl.removeView(cameraSurfaceView);
        CameraManager.getIns().releaseCamera();
        cameraSurfaceView = null;
    }
}
