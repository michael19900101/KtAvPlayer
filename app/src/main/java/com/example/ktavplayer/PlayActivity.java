package com.example.ktavplayer;

import android.Manifest;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ktavplayer.player.DNPlayer;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class PlayActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{
    private DNPlayer dnPlayer;
    public String url;
    private SeekBar seekBar;

    private int progress;
    private boolean isTouch;
    private boolean isSeek;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager
                .LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_play);
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        dnPlayer = new DNPlayer();
        dnPlayer.setSurfaceView(surfaceView);
        dnPlayer.setOnPrepareListener(new DNPlayer.OnPrepareListener() {
            /**
             * 视频信息获取完成 随时可以播放的时候回调
             */
            @Override
            public void onPrepared() {
                //获得时间
                int duration = dnPlayer.getDuration();
                //直播： 时间就是0
                if (duration != 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //显示进度条
                            seekBar.setVisibility(View.VISIBLE);
                        }
                    });
                }
                dnPlayer.start();
            }
        });
        dnPlayer.setOnErrorListener(new DNPlayer.OnErrorListener() {
            @Override
            public void onError(int error) {

            }
        });
        dnPlayer.setOnProgressListener(new DNPlayer.OnProgressListener() {

            @Override
            public void onProgress(final int progress2) {
                if (!isTouch) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int duration = dnPlayer.getDuration();
                            //如果是直播
                            if (duration != 0) {
                                if (isSeek){
                                    isSeek = false;
                                    return;
                                }
                                //更新进度 计算比例
                                seekBar.setProgress(progress2 * 100 / duration);
                            }
                        }
                    });
                }
            }
        });
        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        url = getIntent().getStringExtra("url");
//        dnPlayer.setDataSource("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        dnPlayer.setDataSource("/sdcard/b.mp4");
        checkPerm();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager
                    .LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_play);
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        dnPlayer.setSurfaceView(surfaceView);
        dnPlayer.setDataSource(url);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(progress);

    }

    @Override
    protected void onResume() {
        super.onResume();
        dnPlayer.prepare();
    }

    @Override
    protected void onStop() {
        super.onStop();
        dnPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dnPlayer.release();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isTouch = true;
    }

    /**
     * 停止拖动的时候回调
     * @param seekBar
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isSeek = true;
        isTouch = false;
        progress = dnPlayer.getDuration() * seekBar.getProgress() / 100;
        //进度调整
        dnPlayer.seek(progress);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, new EasyPermissions.PermissionCallbacks() {
            @Override
            public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
                checkPerm();
            }

            @Override
            public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
                finish();
            }

            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

            }
        });
    }
    public static final int WRITE_EXTERNAL_STORAGE = 100;
    private void checkPerm() {
        String[] params = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, params)) {
        } else {
            EasyPermissions.requestPermissions(this, "需要读写本地权限", WRITE_EXTERNAL_STORAGE, params);
        }
    }
}
