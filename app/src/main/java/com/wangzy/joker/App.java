package com.wangzy.joker;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.common.BaseApp;
import com.facebook.drawee.backends.pipeline.Fresco;

//import com.xiaomi.ad.AdSdk;
//import com.xiaomi.ad.AdSdk;

/**
 * Created by wangzy on 2017/5/26.
 */

public class App extends BaseApp {


    public static final String tag = "joker";
    public static App app;

    public boolean isCommitJoke = true;

    @Override
    public void onCreate() {
        super.onCreate();
        this.app = this;
        Fresco.initialize(this);
        initLeancloud();
        initAd();
        initMiAd();
    }

    private void initAd(){
//        AppConnect.getInstance(this);
//        AppConnect.getInstance(this).initAdInfo();

    }
    private void initMiAd(){
//        AdSdk.initialize(this,"2882303761517587016");
//        AdSdk.setDebugOn();
    }

    private void initLeancloud() {

        AVOSCloud.initialize(this, "He8bqYMSgzApsWm1K17X0qTR-gzGzoHsz", "IIPPYSgjSBhJTAP8Cm7S9nUb");
//        AVOSCloud.setDebugLogEnabled(true);
        AVQuery<AVObject> avObjectAVQuery = new AVQuery<>("Config");

        avObjectAVQuery.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (null == e) {
                    isCommitJoke = avObject.getBoolean("commitJoke");
                }
            }
        });


    }



    public static App getApp() {

        return app;
    }
}
