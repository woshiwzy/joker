package com.wangzy.joker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.common.BaseActivity;
import com.common.util.Tool;
import com.squareup.picasso.Picasso;
import com.wangzy.joker.R;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.waps.AdInfo;
import cn.waps.AppConnect;

public class SplashActivity extends BaseActivity {


    @BindView(R.id.textViewSplash)
    TextView textViewSplash;


    @BindView(R.id.imageViewAd)
    ImageView imageViewAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);


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
        }, 3 * 1000);


        final AdInfo adInfo = AppConnect.getInstance(this).getAdInfo();

        if (null != adInfo) {

            try {
                String url = adInfo.getImageUrls()[0];
                Picasso.with(this).load(url).fit().into(imageViewAd);
                imageViewAd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AppConnect.getInstance(SplashActivity.this).clickAd(SplashActivity.this, adInfo.getAdId());
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();

            }


        }

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
