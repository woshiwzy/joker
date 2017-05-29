package com.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

/**
 * Created by wangzy on 16/1/7.
 */
public class MyHorizontalScrollView extends HorizontalScrollView {

    private boolean isScrollAble = true;

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {


        if (isScrollAble) {
            return super.onTouchEvent(ev);
        } else {
            return true;
        }

    }

    public boolean isScrollAble() {
        return isScrollAble;
    }

    public void setScrollAble(boolean scrollAble) {
        isScrollAble = scrollAble;
    }
}
