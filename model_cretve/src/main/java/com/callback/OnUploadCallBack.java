package com.callback;

import android.content.Context;

import com.common.net.NetResult;

public interface OnUploadCallBack {

    public void onUploadEnd(NetResult netResult, Context context);

}
