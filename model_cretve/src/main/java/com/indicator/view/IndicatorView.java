package com.indicator.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cretve.model.R;


/**
 * Created by wangzy on 15/7/26.
 */
public class IndicatorView extends LinearLayout {

    private Context context;
    private int margin = 5;

    private int drable_normal= R.drawable.shape_indicator_white;
    private int drable_check = R.drawable.shape_indicator_purple;

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        margin = (int) context.getResources().getDimension(R.dimen.margin_indicator);

        setGravity(Gravity.CENTER);
        setOrientation(HORIZONTAL);
    }

    public void setIndicatorSize(int size) {
        removeAllViews();

        for (int i = 0; i < size; i++) {

            ImageView imageView = new ImageView(context);

            imageView.setTag(String.valueOf(i));

            LinearLayout linearLayout = new LinearLayout(context);

            LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
            lpp.weight = 1;

            if (i == 0) {
                imageView.setImageResource(drable_check);
            } else {
                imageView.setImageResource(drable_normal);
            }

            linearLayout.setGravity(Gravity.CENTER);


            LinearLayout.LayoutParams lppp = new LinearLayout.LayoutParams(-2, -2);
            lppp.leftMargin = margin;
            lppp.rightMargin = margin;
            linearLayout.addView(imageView, lppp);

            addView(linearLayout, lpp);
        }
//        requestLayout();
    }

    public void setIndicatorSize(int size, int drableNormal, int drableCheck) {
        removeAllViews();
        this.drable_normal=drableNormal;
        this.drable_check=drableCheck;

        for (int i = 0; i < size; i++) {

            ImageView imageView = new ImageView(context);

            imageView.setTag(String.valueOf(i));

            LinearLayout linearLayout = new LinearLayout(context);

            LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
            lpp.weight = 1;

            if (i == 0) {
                imageView.setImageResource(drable_check);
            } else {
                imageView.setImageResource(drable_normal);
            }

            linearLayout.setGravity(Gravity.CENTER);


            LinearLayout.LayoutParams lppp = new LinearLayout.LayoutParams(-2, -2);
            lppp.leftMargin = margin;
            lppp.rightMargin = margin;
            linearLayout.addView(imageView, lppp);

            addView(linearLayout, lpp);
        }
        requestLayout();
    }

    public void checkIndex(int index) {

        String tag = String.valueOf(index);
        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {

            ImageView childView = (ImageView) findViewWithTag(String.valueOf(i));
            String xtag = (String) childView.getTag();

            if (tag.equals(xtag)) {
                childView.setImageResource(drable_check);
            } else {
                childView.setImageResource(drable_normal);
            }

        }


    }

}
