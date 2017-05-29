package com.tab;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tab.widget.CheckImageView;
import com.tab.widget.CheckTextView;

/**
 * Created by wangzy on 2016/11/3.
 */

public class TabItem {

    public View rootView;
    private CheckImageView imageView;
    private CheckTextView textView;

    private int checkColor = -1;
    private int unCheckColor = -1;
    private boolean showBg=true;


    public TabItem(LinearLayout linearLayout) {

        this.rootView = linearLayout;

        Object[] widgets = findImageTextViewFromContainer(linearLayout);

        this.imageView = (CheckImageView) widgets[0];
        this.textView = (CheckTextView) widgets[1];
    }

    public TabItem(LinearLayout linearLayout, int checkColor, int unCheckColor) {
        this(linearLayout);
        this.checkColor = checkColor;
        this.unCheckColor = unCheckColor;
    }


    public void check() {
        imageView.check();
        textView.check();
        if (-1 != checkColor && showBg) {
            rootView.setBackgroundResource(checkColor);
        }

    }

    public void unCheck() {
        imageView.uncheck();
        textView.uncheck();
        if (-1 != checkColor && showBg) {
            rootView.setBackgroundResource(unCheckColor);
        }
    }


    public int getCheckColor() {
        return checkColor;
    }

    public void setCheckColor(int checkColor) {
        this.checkColor = checkColor;
    }

    public int getUnCheckColor() {
        return unCheckColor;
    }

    public void setUnCheckColor(int unCheckColor) {
        this.unCheckColor = unCheckColor;
    }

    Object[] findImageTextViewFromContainer(ViewGroup view) {

        Object[] object = new Object[2];

        CheckImageView imageView = (CheckImageView) view.getChildAt(0);
        CheckTextView textView = (CheckTextView) view.getChildAt(1);
        object[0] = imageView;
        object[1] = textView;

        return object;

    }


    public boolean isShowBg() {
        return showBg;
    }

    public void setShowBg(boolean showBg) {
        this.showBg = showBg;
    }
}
