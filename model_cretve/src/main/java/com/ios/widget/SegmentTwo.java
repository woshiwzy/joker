package com.ios.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.common.util.Tool;
import com.cretve.model.R;

/**
 * Created by wangzy on 16/1/5.
 */
public class SegmentTwo extends View {

    private OnSegmentClickListener onSegmentClickListener;
    private String leftText = "Search Venue or DJ/Band";
    private String rightText = "Search Location";
    private int currentIndex = 0;
    private float textSize=13.0f;

    private int selectColor=0xff7476e1;
    private int normalColor=0xff7476e1;

    public SegmentTwo(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        setBackgroundResource(R.drawable.icon_segment_left);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int w = getWidth();
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:

                int x = (int) event.getX();
                if (x <= w / 2) {
                    currentIndex = 0;
                    setBackgroundResource(R.drawable.icon_segment_left);
                } else {
                    currentIndex = 1;
                    setBackgroundResource(R.drawable.icon_segment_right);
                }
                if (null != onSegmentClickListener) {
                    onSegmentClickListener.onclickIndex(currentIndex);
                }

                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    private Paint paint;
    private void initPaint(){

        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(textSize);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        int w = getWidth();
        int h = getHeight();

        canvas.drawColor(Color.TRANSPARENT);


        if(currentIndex==0){

            paint.setColor(selectColor);
            canvas.drawText(leftText,w/4-paint.measureText(leftText)/2, (int)(h/2-Tool.getFontHeight(paint)/2),paint);
            canvas.drawText(rightText,w/4-paint.measureText(leftText)/2, (int)(h/2-Tool.getFontHeight(paint)/2),paint);

        }else{


        }


    }

    public OnSegmentClickListener getOnSegmentClickListener() {
        return onSegmentClickListener;
    }

    public void setOnSegmentClickListener(OnSegmentClickListener onSegmentClickListener) {
        this.onSegmentClickListener = onSegmentClickListener;
    }

    public static interface OnSegmentClickListener {
        public void onclickIndex(int index);
    }
}
