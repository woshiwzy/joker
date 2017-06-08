package com.bigkoo.pickerview.view;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by wangzy on 16/1/7.
 */
public class DialogPickerView extends MyBasePicker {


    public DialogPickerView(Context context) {
        super(context);


        Animation inAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);

        inAnimation.setDuration(200);
        setInAnim(inAnimation);

        Animation outAnimation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        outAnimation.setDuration(200);
        setOutAnim(outAnimation);
    }
}
