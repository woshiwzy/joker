package com.common.util;

import android.content.DialogInterface;

public abstract class DialogCallBackListener {

    public void onCallBack(boolean yesNo, String text) {
    }
    public void onCallBack(boolean yesNo, String text,DialogInterface dialog) {
    }

    public void onDo() {
    }

    public void onDone(boolean yesOrNo){}

}
