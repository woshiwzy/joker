package com.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

/**
 * Created by wangzy on 2016/11/16.
 */

public abstract class CommonBaseAdapter extends BaseAdapter{


    public Context context;
    public LayoutInflater layoutInflater;


    public  CommonBaseAdapter(Context context){
        this.context=context;
        this.layoutInflater=LayoutInflater.from(context);
    }

}
