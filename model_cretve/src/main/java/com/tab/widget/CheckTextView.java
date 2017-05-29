package com.tab.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

import com.cretve.model.R;

/**
 * Created by wangzy on 16/8/25.
 */
public class CheckTextView extends TextView {

    private int checkTextColor= Color.parseColor("#818BA6");
    private int uncheckTextColor=Color.parseColor("#979597");

    private OnCheckListener onCheckListener;
    private boolean isCheck;

    public CheckTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public CheckTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    public void init(Context context, AttributeSet attrs) {

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.checkTextView);

        checkTextColor = ta.getColor(R.styleable.checkTextView_checkimg_check_color, 0);
        uncheckTextColor = ta.getColor(R.styleable.checkTextView_checkimg_uncheck_color, 0);


        if (0 != checkTextColor && 0 != uncheckTextColor) {
            uncheck();
        }

        ta.recycle();
    }


    public void check() {

        setTextColor(checkTextColor);

        this.isCheck = true;
    }

    public void uncheck() {
        setTextColor(uncheckTextColor);
        this.isCheck = false;
    }


    public OnCheckListener getOnCheckListener() {
        return onCheckListener;
    }

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    public static interface OnCheckListener {
        public void onCheckListener(boolean checkCheck, CheckTextView checkImageView);
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
