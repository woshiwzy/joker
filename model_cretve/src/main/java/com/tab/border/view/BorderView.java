package com.tab.border.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wangzy on 15/10/20.
 */
public class BorderView extends View {


    private int color = 0xff2DA0DC;
    private int bgColor = 0xfff5f5f5;
    private boolean isLeft = true;
    private Paint paint;
    private Block block = new Block();
    private int w;

    public BorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null == canvas) {
            return;
        }

        int h = getHeight();
        w = getWidth();
        canvas.drawColor(bgColor);

        Rect rect = new Rect();
        rect.left = block.blockStart;
        rect.top = 0;
        rect.right = rect.left + w / 2;
        rect.bottom = h;

        paint.setColor(color);
        canvas.drawRect(rect, paint);

        paint.setColor(0xffA1A1A1);
//        canvas.drawLine(0, h - 1, w, h - 1, paint);

        canvas.drawLine(0, h - 1, block.blockStart, h - 1, paint);
        canvas.drawLine(block.blockStart + w / 2, h - 1, w, h - 1, paint);

    }

    public void showLeft() {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(block.blockStart, 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                block.blockStart = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(200);

        valueAnimator.start();

        isLeft = true;
    }

    public void showRight() {

        ValueAnimator valueAnimator = ValueAnimator.ofInt(block.blockStart, w / 2);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                block.blockStart = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(200);
        valueAnimator.start();
        isLeft = false;
    }


    private class Block {
        private int blockStart = 0;
    }
}
