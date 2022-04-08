package com.example.ktavplayer;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

import com.example.ktavplayer.player.DNPlayer;

/**
 * @author Lance
 * @date 2018/9/7
 */
public class PlayActivity extends ComponentActivity {
    private DNPlayer dnPlayer;
    public String url;


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
            @Override
            public void onPrepared() {
                dnPlayer.start();
            }
        });

        url = "https://media.w3.org/2010/05/sintel/trailer.mp4";
        dnPlayer.setDataSource(url);
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
}
