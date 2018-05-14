package com.zf.spycamera.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mob.main.MobService;
import com.mob.permission.FloatWindowManager;
import com.mob.permission.rom.RomUtils;
import com.mob.permission.rom.VivoUtils;
import com.radishmobile.rate.ExitDialog;
import com.radishmobile.rate.IExitListener;
import com.radishmobile.rate.RateUtils;
import com.zf.spycamera.AdFactory;
import com.zf.spycamera.Controller;
import com.zf.spycamera.FloatWindowService;
import com.zf.spycamera.R;
import com.zf.spycamera.utils.CameraUtil;

public class MainActivity extends AppCompatActivity implements IExitListener {

    private ImageView mIvCam;
    private ImageView mIvSetting;
    private ImageView mIvPic;
    private ImageView mIvGenCam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Controller.getIns().init(this,new AdFactory());
//        Controller.getIns().loadInterAd(this);
//        Controller.getIns().loadBannerAd(this);

        initData();
        initView();
    }

    private void initData(){
    }

    private boolean mIsStartService = false;
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
               //todo 启动偷拍
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


               //debug
//               if (FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this)) {
//                   Intent floatIntent = new Intent(MainActivity.this, FloatWindowService.class);
//                   if (!mIsStartService) {
//                       //startService(floatIntent);
//                       mIvCam.setImageResource(R.drawable.img_secret_camera_on);
//                   } else {
//                       //stopService(floatIntent);
//                       mIvCam.setImageResource(R.drawable.img_secret_camera_off);
//                   }
//                   mIsStartService = !mIsStartService;
//               }else {
//                   showSettingDialog();
//               }


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


    @Override
    public void onBackPressed() {
        ExitDialog exitDialog = new ExitDialog(this);
        exitDialog.setExitListener(this);
        exitDialog.show();
    }

    @Override
    public void onClickExit() {
//		Process.killProcess(Process.myPid());
        finish();
    }

    @Override
    public void onClickRate() {
        RateUtils.openAppInPlay(this, getPackageName());
    }

    @Override
    public void onCancel() {

    }
}
