package com.tab.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.cretve.model.R;

/**
 * Created by wangzy on 16/8/25.
 */
public class CheckImageView extends ImageView {


    private int checkImg;
    private int uncheckImg;

    private OnCheckListener onCheckListener;
    private boolean isCheck;

    public CheckImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public CheckImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }



    public void init(Context context, AttributeSet attrs) {

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.checkImageView);

        checkImg = ta.getResourceId(R.styleable.checkImageView_checkimg_check, 0);
        uncheckImg = ta.getResourceId(R.styleable.checkImageView_checkimg_uncheck, 0);

        ta.recycle();

        if (0 != checkImg && 0 != uncheckImg) {
            uncheck();
        }
    }


    public void check() {
        setImageResource(checkImg);
        this.isCheck = true;
    }

    public void uncheck() {
        setImageResource(uncheckImg);
        this.isCheck = false;
    }


    public void checkWithListener() {
        setImageResource(checkImg);
        this.isCheck = true;
        if (null != onCheckListener) {
            onCheckListener.onCheckListener(true, this);
        }
    }

    public void uncheckWithListener() {
        setImageResource(uncheckImg);
        this.isCheck = false;
        if (null != onCheckListener) {
            onCheckListener.onCheckListener(false, this);
        }
    }

    public int getCheckImg() {
        return checkImg;
    }

    public void setCheckImg(int checkImg) {
        this.checkImg = checkImg;
    }

    public int getUncheckImg() {
        return uncheckImg;
    }

    public void setUncheckImg(int uncheckImg) {
        this.uncheckImg = uncheckImg;
    }

    public OnCheckListener getOnCheckListener() {
        return onCheckListener;
    }

    public void setOnCheckListener(OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    public static interface OnCheckListener {
        public void onCheckListener(boolean checkCheck, CheckImageView checkImageView);
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
