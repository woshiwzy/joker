package com.common.task;

import com.common.net.NetResult;

import java.util.HashMap;

public abstract class NetCallBack {

    public void onPreCall() {
    }

    public void onPreCall(BaseTask baseTask) {
    }

    public NetResult onDoInBack(HashMap<String, String> paramMap, BaseTask baseTask) {

        return null;
    }

    public void onCanCell() {
    }

    public void onCanCell(BaseTask baseTask) {
        baseTask.hideDialogSelf();
    }

    public void foreKill(BaseTask baseTask){

    }

    public void onFinish(NetResult result) {
    }

    public void onFinish(NetResult result, BaseTask baseTask) {

    }

}
