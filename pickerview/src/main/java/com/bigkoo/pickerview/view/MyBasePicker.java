package com.bigkoo.pickerview.view;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bigkoo.pickerview.R;

/**
 * Created by wangzy on 15/12/9.
 */
public class MyBasePicker extends BasePickerView {
    public LayoutInflater layoutInflater;
    private boolean dismissable = false;

    public MyBasePicker(Context context) {
        super(context);
    }

    protected void initViews() {
        layoutInflater = LayoutInflater.from(context);
        decorView = (ViewGroup) ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
        rootView = (ViewGroup) layoutInflater.inflate(com.bigkoo.pickerview.R.layout.layout_basepickerview_dialog, decorView, false);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
        contentContainer = (ViewGroup) rootView.findViewById(com.bigkoo.pickerview.R.id.content_container);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.TOP);
        contentContainer.setLayoutParams(params);

        rootView.findViewById(R.id.outmost_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dismissable) {
                    dismiss();
                }
            }
        });
    }

    protected void setBackgroundColor(int color) {
        rootView.findViewById(R.id.outmost_container).setBackgroundColor(color);
    }

    protected void setBackgroundResource(int resId) {
        rootView.findViewById(R.id.outmost_container).setBackgroundResource(resId);
    }

    public void setDismissable(boolean dismissable) {
        this.dismissable = dismissable;
    }
}
