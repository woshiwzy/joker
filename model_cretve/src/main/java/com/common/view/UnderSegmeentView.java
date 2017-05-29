package com.common.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.common.util.Tool;

/**
 * Created by wangzy on 15/11/19.
 */
public class UnderSegmeentView extends View {

    private int segment = 8;
    private int current = 1;
        private int color = 0xff038AF7;
//    private int color = Color.BLUE;
    private int w;
    private int h;
    private  int ew;
    private int segmentStart;
    private Paint paint;
    private float centerWidthFraction = 0.8f;
    private boolean enable=true;


    public UnderSegmeentView(Context context) {
        super(context);
        init();
    }

    public UnderSegmeentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(4);

        if(w==0){
            w= Tool.getDisplayMetrics(getContext()).x;
        }
        ew=w/segment;

        setCurrent(current);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        w = getWidth();
        h = getHeight();
        canvas.drawColor(Color.TRANSPARENT);

        if (0 != segment) {
            Rect rect = new Rect();
            int pading = (int) ((ew - ew * centerWidthFraction) / 2);
            rect.left = segmentStart+pading;
            rect.right = segmentStart+ ew-pading;
            rect.top = 0;
            rect.bottom = h;

            canvas.drawRect(rect, paint);
        }
    }

    public int getSegment() {
        return segment;
    }

    public void setSegment(int segment) {
        this.segment = segment;
        postInvalidate();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
        invalidate();
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
//        int ew = w / segment;
        this.segmentStart = current * ew;
        invalidate();
    }

    private boolean isanimation=false;
    boolean test=true;
    public void setCurrentAnimation(final int current) {

        int ew = w / segment;

        int currentStart = current * ew;
        int oldStart = this.current * ew;

        clearAnimation();

        ValueAnimator valueAnimator = ValueAnimator.ofInt(oldStart, currentStart);
        valueAnimator.setDuration(200);
        valueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                isanimation=true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                UnderSegmeentView.this.current = current;
                isanimation=false;
            }
        });

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                UnderSegmeentView.this.segmentStart = value;
                invalidate();
            }
        });
        valueAnimator.start();

    }


    public float getCenterWidthFraction() {
        return centerWidthFraction;
    }

    public void setCenterWidthFraction(float centerWidthFraction) {
        if (centerWidthFraction > 1) {
            centerWidthFraction = 1;
        }
        this.centerWidthFraction = centerWidthFraction;
        invalidate();
    }


    public int getEw() {
        return ew;
    }

    public void setEw(int ew) {
        this.ew = ew;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
