package com.wangzy.joker;

import com.avos.avoscloud.AVOSCloud;
import com.common.BaseApp;

/**
 * Created by wangzy on 2017/5/26.
 */

public class App extends BaseApp {


    public static final String tag="joker";

    @Override
    public void onCreate() {
        super.onCreate();
        initLeancloud();
    }

    private void initLeancloud(){

        AVOSCloud.initialize(this,"He8bqYMSgzApsWm1K17X0qTR-gzGzoHsz","IIPPYSgjSBhJTAP8Cm7S9nUb");
        AVOSCloud.setDebugLogEnabled(true);
    }
}
