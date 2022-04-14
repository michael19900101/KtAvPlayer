/**
 *
 * Created by 公众号：字节流动 on 2021/3/16.
 * https://github.com/githubhaohao/LearnFFmpeg
 * 最新文章首发于公众号：字节流动，有疑问或者技术交流可以添加微信 Byte-Flow ,领取视频教程, 拉你进技术交流群
 *
 * */

package com.example.ktavplayer;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ktavplayer.player.KTPlayer;
import com.example.ktavplayer.util.AssetHelper;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity  implements EasyPermissions.PermissionCallbacks{
    private static final String TAG = "MainActivity";
    private static final int READ_EXTERNAL_PERM = 123;
    private static final int WRITE_EXTERNAL_PERM = 456;
    private static final String[] REQUEST_PERMISSIONS = {
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView)findViewById(R.id.text_view)).setText("FFmpeg 版本和编译配置信息\n\n" + KTPlayer.GetFFmpegVersion());
        findViewById(R.id.btnJumpPlay).setOnClickListener(
                v -> startActivity(new Intent(MainActivity.this, PlayActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        copyAssertResourcesToSDCard();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @AfterPermissionGranted(WRITE_EXTERNAL_PERM)
    private void copyAssertResourcesToSDCard(){
        if (hasSDCardPermission()) {
            // 插入数据到应用程序专属特定目录下自定义文件夹 (/storage/emulated/0/Android/data/com.example.ktavplayer/files/ktavplayer)
            File videoFile = new File(getExternalFilesDir(null),"ktavplayer");
            AssetHelper.Companion.copyAssetMultipleFile(this,"ktavplayer", videoFile);
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.need_sdcard_permission),
                    WRITE_EXTERNAL_PERM,
                    REQUEST_PERMISSIONS);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            String yes = "yes";
            String no = "no";

            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(
                    this,
                    "从设置页码返回，sd卡权限："+ (hasSDCardPermission() ? yes : no),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    private boolean hasSDCardPermission() {
        return EasyPermissions.hasPermissions(this, REQUEST_PERMISSIONS);
    }

}
