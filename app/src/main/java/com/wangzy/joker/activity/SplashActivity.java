package com.wangzy.joker.activity;

import android.content.Intent;
import android.os.Bundle;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
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


                try {
                    AVInstallation.getCurrentInstallation().put("device", Tool.getImei(SplashActivity.this));
                } catch (Exception e) {

                } finally {
                    AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            PushService.subscribe(SplashActivity.this, "public", MainActivity.class);
                            Tool.startActivityForResult(SplashActivity.this, MainActivity.class, SPLASH_TO_MAIN);
                            finish();
                        }
                    });

                }


            }
        }, 2 * 1000);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        AVAnalytics.trackAppOpened(intent);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SPLASH_TO_MAIN && resultCode == RESULT_OK) {

            finish();
        }
    }
}
