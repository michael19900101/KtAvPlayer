package com.example.ktavplayer;

import android.Manifest;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ktavplayer.player.KTPlayer;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class PlayActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{
    private KTPlayer ktPlayer;
    private SeekBar seekBar;

    private int progress;
    private boolean isTouch;
    private boolean isSeek;
    private String mVideoPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File videoFile = new File(getExternalFilesDir(null),"ktavplayer/one_piece.mp4");
        mVideoPath = videoFile.getAbsolutePath();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager
                .LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_play);
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        ktPlayer = new KTPlayer();
        ktPlayer.setSurfaceView(surfaceView);
        ktPlayer.setOnPrepareListener(new KTPlayer.OnPrepareListener() {
            /**
             * 视频信息获取完成 随时可以播放的时候回调
             */
            @Override
            public void onPrepared() {
                //获得时间
                int duration = ktPlayer.getDuration();
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
                ktPlayer.start();
            }
        });
        ktPlayer.setOnErrorListener(new KTPlayer.OnErrorListener() {
            @Override
            public void onError(int error) {

            }
        });
        ktPlayer.setOnProgressListener(new KTPlayer.OnProgressListener() {

            @Override
            public void onProgress(final int progress2) {
                if (!isTouch) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int duration = ktPlayer.getDuration();
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
//        ktPlayer.setDataSource("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        ktPlayer.setDataSource(mVideoPath);
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
        ktPlayer.setSurfaceView(surfaceView);
        ktPlayer.setDataSource(mVideoPath);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(this);
        seekBar.setProgress(progress);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ktPlayer.prepare();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ktPlayer.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ktPlayer.release();
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
        progress = ktPlayer.getDuration() * seekBar.getProgress() / 100;
        //进度调整
        ktPlayer.seek(progress);
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
