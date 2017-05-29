package com.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.cretve.model.R;


public class AutoTextView extends TextView {
    private float minTextSize, maxTextSize;
    private float DEFAULT_MIN_TEXT_SIZE;
    private float DEFAULT_MAX_TEXT_SIZE;
    private ChangeLayoutListener listener;
    private View parentView;
    private int width = 0;
    private int oldWidth = 0;
    private int height = 0;
    private boolean isChangeHeight;

    public AutoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.AutoTextView);
        isChangeHeight = a.getBoolean(R.styleable.AutoTextView_isChangeHeight,
                false);
        DEFAULT_MIN_TEXT_SIZE = a.getDimension(
                R.styleable.AutoTextView_minTextSize, 1);
        DEFAULT_MAX_TEXT_SIZE = a.getDimension(
                R.styleable.AutoTextView_maxTextSize, 30);
        a.recycle();
        initialise();
    }

    public void setChangeLayoutListener(ChangeLayoutListener listener, View view) {
        this.listener = listener;
        this.parentView = view;
    }

    public boolean isChangeHeight() {
        return isChangeHeight;
    }

    public void setChangeHeight(boolean isChangeHeight) {
        this.isChangeHeight = isChangeHeight;
    }

    private void initialise() {
        maxTextSize = this.getTextSize();

        if (maxTextSize <= DEFAULT_MIN_TEXT_SIZE) {
            maxTextSize = DEFAULT_MAX_TEXT_SIZE;
        }

        minTextSize = DEFAULT_MIN_TEXT_SIZE;
    }

    ;

    private void resize(String text, int textWidth, int preWidth) {

        if (listener != null) {
            if (!listener.isOk(parentView)) {
                return;
            }
        }
        boolean isUpdate = false;
        if (textWidth > 0) {
            int availableWidth = textWidth - this.getPaddingLeft()
                    - this.getPaddingRight();
            float preSize = getPaint().getTextSize();
            float curSize = 0;
            curSize = maxTextSize;
            getPaint().setTextSize(curSize);
            if ((curSize > minTextSize)
                    && (getPaint().measureText(text) > availableWidth)) {

                if (preWidth < textWidth) {
                    curSize = maxTextSize;
                } else {
                    curSize = preSize;
                }
                getPaint().setTextSize(curSize);
                while ((curSize > minTextSize)
                        && (getPaint().measureText(text) > availableWidth)) {
                    isUpdate = true;
                    curSize -= 1;
                    if (curSize <= minTextSize) {
                        curSize = minTextSize;
                        getPaint().setTextSize(curSize);
                        break;
                    }
                    getPaint().setTextSize(curSize);

                }
                if (isUpdate) {

                    // invalidate();
                    if (listener != null) {
                        listener.isChange(parentView, text);
                    }
                }
            }

        }

    }

    ;

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        width = getWidth();
        if (oldWidth == 0) {
            oldWidth = width;
        }
        if (getWidth() > 0) {
            height = getHeight();
            resize(getText().toString(), width, oldWidth);
            super.onDraw(canvas);
            oldWidth = width;

        } else {
            super.onDraw(canvas);
            height = getHeight();
            width = getWidth();
            resize(getText().toString(), width, oldWidth);
            oldWidth = width;
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isChangeHeight && height > 0 && width > 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(getMeasuredWidth(), height);

        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        }
    }


    public int getFitWidth() {
        String tex = getText().toString();
        float len = getPaint().measureText(tex);
        return (int) len;
    }

    public interface ChangeLayoutListener {

        public void isChange(View view, String text);

        public boolean isOk(View view); //
    }

}