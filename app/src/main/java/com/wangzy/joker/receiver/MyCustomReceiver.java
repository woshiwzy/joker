package com.wangzy.joker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.LogUtil;

import java.util.Iterator;

/**
 * Created by wangzy on 2017/6/8.
 */

public class MyCustomReceiver extends BroadcastReceiver {


    private static final String TAG = "MyCustomReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
//        LogUtil.log.d(TAG, "Get Broadcat");
//        try {
//            String action = intent.getAction();
//            String channel = intent.getExtras().getString("com.avos.avoscloud.Channel");
//            //获取消息内容
//            JSONObject json = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));
//
//            Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
//            Iterator itr = json.keys();
//            while (itr.hasNext()) {
//                String key = (String) itr.next();
//                Log.d(TAG, "..." + key + " => " + json.getString(key));
//            }
//        } catch (JSONException e) {
//            Log.d(TAG, "JSONException: " + e.getMessage());
//        }
    }
}
