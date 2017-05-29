package com.common.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by wangzy on 2016/12/2.
 */

public class RecyleImageView extends ImageView {

    public RecyleImageView(Context context) {
        super(context);
    }

    public RecyleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageDrawable(null);
    }
}
