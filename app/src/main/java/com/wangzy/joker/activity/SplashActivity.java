package com.wangzy.joker.activity;

import android.os.Bundle;

import com.common.BaseActivity;
import com.common.util.Tool;
import com.wangzy.joker.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Tool.startActivity(SplashActivity.this, MainActivity.class);
                finish();
            }
        }, 2 * 1000);

    }

    @Override
    public void onBackPressed() {
    }
}
