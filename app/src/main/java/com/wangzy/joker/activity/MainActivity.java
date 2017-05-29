package com.wangzy.joker.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.common.BaseActivity;
import com.common.util.ListUtiles;
import com.common.view.BasePage;
import com.tab.OnTabItemClickListener;
import com.tab.TabItem;
import com.tab.TabLayout;
import com.wangzy.joker.R;
import com.wangzy.joker.constants.Constant;
import com.wangzy.joker.page.PageJoke;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;

    @BindView(R.id.relativeLayoutContent)
    RelativeLayout relativeLayoutContent;

    RelativeLayout.LayoutParams lpp;

    BasePage pageJokerText;
    BasePage pageJokerImg;
    BasePage pageJokerVideo;


    TabLayout tabLayout = new TabLayout();

    ArrayList<BasePage> basePages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        initView();
    }

    @Override
    public void initView() {

        this.basePages = new ArrayList<>();

        lpp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        pageJokerText = new PageJoke(this, com.wangzy.joker.constants.Constant.JOKETYPE.JOKE_TYPE_TEXT);
        relativeLayoutContent.addView(pageJokerText.getRootView(), lpp);
        this.basePages.add(pageJokerText);


        pageJokerImg = new PageJoke(this, com.wangzy.joker.constants.Constant.JOKETYPE.JOKE_TYPE_TEXT_IMG, Constant.JOKETYPE.JOKE_TYPE_TEXT_GIF);
        relativeLayoutContent.addView(pageJokerImg.getRootView(), lpp);
        this.basePages.add(pageJokerImg);

        pageJokerVideo = new PageJoke(this, Constant.JOKETYPE.JOKE_TYPE_TEXT_VIDEO);
        relativeLayoutContent.addView(pageJokerVideo.getRootView(), lpp);
        this.basePages.add(pageJokerVideo);


        tabLayout.addTabItem(findLinearLayout(R.id.linearLayooutTextJoke));
        tabLayout.addTabItem(findLinearLayout(R.id.linearLayooutImgJoke));
//        tabLayout.addTabItem(findLinearLayout(R.id.linearLayooutGifJoke));
        tabLayout.addTabItem(findLinearLayout(R.id.linearLayooutVideoJoke));
        tabLayout.addTabItem(findLinearLayout(R.id.linearLayooutMe));
        tabLayout.setOnTabItemClickListener(new OnTabItemClickListener() {
            @Override
            public void onCheckIndex(int index, int total, TabItem tabItem) {
                showIndexPage(index);
            }
        });
    }


    private void showIndexPage(int index) {
        for (int i = 0, isize = ListUtiles.getListSize(this.basePages); i < isize; i++) {
            if (i == index) {
                basePages.get(i).getRootView().setVisibility(View.VISIBLE);
            } else {
                basePages.get(i).getRootView().setVisibility(View.GONE);
            }
        }
    }
}
