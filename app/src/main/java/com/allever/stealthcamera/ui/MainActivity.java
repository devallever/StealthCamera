package com.allever.stealthcamera.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.allever.stealthcamera.FloatWindowService;
import com.allever.stealthcamera.R;
import com.allever.stealthcamera.function.permission.FloatWindowManager;
import com.allever.stealthcamera.function.permission.rom.RomUtils;
import com.allever.stealthcamera.function.permission.rom.VivoUtils;
import com.allever.stealthcamera.utils.CameraUtil;

public class MainActivity extends AppCompatActivity{

    private ImageView mIvCam;
    private ImageView mIvSetting;
    private ImageView mIvPic;
    private ImageView mIvGenCam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initData(){
    }

    private void initView(){
        mIvCam = findViewById(R.id.id_main_iv_camera);
        mIvSetting = findViewById(R.id.id_main_iv_settings);
        mIvPic = findViewById(R.id.id_main_iv_pictures);
        mIvGenCam = findViewById(R.id.id_main_iv_general_camera);

        if (FloatWindowService.mService == null) {
            mIvCam.setImageResource(R.drawable.ic_camera_off);
        } else {
            mIvCam.setImageResource(R.drawable.ic_camera_on);
        }

        mIvCam.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (!CameraUtil.checkCameraHardware(MainActivity.this)){
                   return;
               }

               if (FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this)) {
                   Intent floatIntent = new Intent(MainActivity.this, FloatWindowService.class);
                   if (FloatWindowService.mService == null) {
                       startService(floatIntent);
                       mIvCam.setImageResource(R.drawable.ic_camera_on);
                   } else {
                       stopService(floatIntent);
                       mIvCam.setImageResource(R.drawable.ic_camera_off);
                   }
               }else {
                   showSettingDialog();
               }
           }
       });

        mIvSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置界面
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        mIvPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //相册
                Intent intent = new Intent(MainActivity.this, PictureActivity.class);
                startActivity(intent);

//                Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
//                startActivity(intent);

            }
        });

        mIvGenCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CameraUtil.checkCameraHardware(MainActivity.this)){
                    return;
                }
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });


    }

    private void showSettingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.string_dialog_title);
        builder.setMessage(R.string.string_dialog_message);
        builder.setPositiveButton(R.string.string_dialog_setting_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (RomUtils.checkIsVivoRom()) {
                    VivoUtils.applyOppoPermission(MainActivity.this);
                } else {
                    FloatWindowManager.getInstance().applyPermission(MainActivity.this);
                }
            }
        });
        builder.setNegativeButton(R.string.string_dialog_cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    private long mPrevClickBackTime = -1;
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (mPrevClickBackTime == -1 || currentTime - mPrevClickBackTime > 3000) {
            mPrevClickBackTime = currentTime;
            Toast.makeText(this, "Press again to exit",
                    Toast.LENGTH_LONG).show();
            return;
        }
        super.onBackPressed();
    }
}
