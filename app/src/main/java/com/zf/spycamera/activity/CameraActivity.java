package com.zf.spycamera.activity;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.zf.spycamera.CameraManager;
import com.zf.spycamera.R;

/**
 * Created by Allever on 18/5/12.
 */

public class CameraActivity extends AppCompatActivity {
    private static final String TAG = "CameraActivity";
    private ImageView mIvTake;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        CameraManager.getIns().openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    @Override
    protected void onStop() {
        super.onStop();
        CameraManager.getIns().releaseCamera();
    }
}
