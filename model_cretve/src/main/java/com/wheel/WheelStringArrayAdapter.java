package com.wheel;

import android.content.Context;

public class WheelStringArrayAdapter extends ArrayWheelAdapter {

    private String[] items;

    public WheelStringArrayAdapter(Context context, String[] items) {
        super(context, items);
        this.items = items;
    }

    @Override
    public CharSequence getItemText(int index) {

        return items[index];
    }

}
