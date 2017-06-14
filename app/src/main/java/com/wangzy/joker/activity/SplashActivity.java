package com.wangzy.joker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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

//    private static final String POSITION_ID = "b373ee903da0c6fc9c9da202df95a500";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);


//        ApplicationInfo appInfo = null;
//        try {
//            appInfo = this.getPackageManager()
//                    .getApplicationInfo(getPackageName(),
//                            PackageManager.GET_META_DATA);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        String msg=appInfo.metaData.getString("APP_PID");


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

//        if("xiaomi".equals(msg)){
//
//            ViewGroup mContainer = (ViewGroup) findViewById(R.id.splash_ad_container);
//
//            SplashAd splashAd = new SplashAd(this, mContainer, R.mipmap.ic_launcher, new SplashAdListener() {
//                @Override
//                public void onAdPresent() {
//                    // 开屏广告展示
//                }
//
//                @Override
//                public void onAdClick() {
//                    //用户点击了开屏广告
//                }
//
//                @Override
//                public void onAdDismissed() {
//                    //这个方法被调用时，表示从开屏广告消失。
//
//
//                }
//
//                @Override
//                public void onAdFailed(String s) {
//                }
//            });
//            splashAd.requestAd(POSITION_ID);
//        }


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
