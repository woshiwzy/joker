package com.tab;

import android.view.View;
import android.widget.LinearLayout;

import com.common.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by wangzy on 2016/11/3.
 */

public class TabLayout {

    private ArrayList<TabItem> arrayListTabItems;
    private OnTabItemClickListener onTabItemClickListener;
    private int currentCheck=-1;

    private int checkColor=-1;
    private int unCheckColor=-1;

    public TabLayout() {
        init();

    }

    private void init() {
        arrayListTabItems = new ArrayList<>();
    }


    public void addTabItem(LinearLayout linearLayout) {
        TabItem tabItem = new TabItem(linearLayout);
        addTabItem(tabItem);
    }


    public void addTabItem(LinearLayout linearLayout,boolean showBg) {
        TabItem tabItem = new TabItem(linearLayout);
        tabItem.setShowBg(showBg);
        addTabItem(tabItem);
    }


    public void addTabItem(final TabItem tabItem) {

        if(-1!=checkColor){
            tabItem.setCheckColor(checkColor);
        }
        if(-1!=unCheckColor){
            tabItem.setUnCheckColor(unCheckColor);
        }

        arrayListTabItems.add(tabItem);
        tabItem.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0, isize = arrayListTabItems.size(); i < isize; i++) {

                    if (arrayListTabItems.get(i).equals(tabItem)) {
                        tabItem.check();
                        currentCheck=i;
                        if (null != onTabItemClickListener) {
                            onTabItemClickListener.onCheckIndex(i, isize, tabItem);
                        }
                    } else {
                        arrayListTabItems.get(i).unCheck();
                    }
                }

            }
        });
    }


    public void checkByIndex(int index) {

        if(index==currentCheck){
            return;
        }

        if (index < arrayListTabItems.size()) {

            for (int i = 0, isize = arrayListTabItems.size(); i < isize; i++) {

                if (i == index) {
                    arrayListTabItems.get(i).check();

                    currentCheck=i;
                    if (null != onTabItemClickListener) {
                        onTabItemClickListener.onCheckIndex(i, isize, arrayListTabItems.get(index));
                    }
                } else {
                    arrayListTabItems.get(i).unCheck();
                }
            }


        }
    }

    public void checkByIndexWithNoListEner(int index) {

        if(index==currentCheck){
            return;
        }

        if (index < arrayListTabItems.size()) {

            for (int i = 0, isize = arrayListTabItems.size(); i < isize; i++) {

                if (i == index) {
                    currentCheck=i;
                    arrayListTabItems.get(i).check();
                } else {
                    arrayListTabItems.get(i).unCheck();
                }
            }


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

    public OnTabItemClickListener getOnTabItemClickListener() {
        return onTabItemClickListener;
    }

    public void setOnTabItemClickListener(OnTabItemClickListener onTabItemClickListener) {
        this.onTabItemClickListener = onTabItemClickListener;
    }
}
