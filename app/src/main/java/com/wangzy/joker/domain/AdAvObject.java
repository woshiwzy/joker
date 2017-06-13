package com.wangzy.joker.domain;

import com.avos.avoscloud.AVObject;

import cn.waps.AdInfo;

/**
 * Created by wangzy on 2017/6/11.
 */

public class AdAvObject extends AVObject{

    AdInfo adInfo;


    public AdAvObject(AdInfo adInfo){

        this.adInfo=adInfo;
    }


    public AdInfo getAdInfo() {
        return adInfo;
    }

    public void setAdInfo(AdInfo adInfo) {
        this.adInfo = adInfo;
    }
}
