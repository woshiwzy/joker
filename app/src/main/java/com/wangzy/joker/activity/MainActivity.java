package com.wangzy.joker.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.common.util.ListUtiles;
import com.common.view.BasePage;
import com.tab.OnTabItemClickListener;
import com.tab.TabItem;
import com.tab.TabLayout;
import com.wangzy.joker.R;
import com.wangzy.joker.constants.Constant;
import com.wangzy.joker.page.PageJoke;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseJokeActivity {

    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;

    @BindView(R.id.relativeLayoutContent)
    RelativeLayout relativeLayoutContent;

    RelativeLayout.LayoutParams lpp;

    BasePage pageJokerText;
    BasePage pageJokerImg;
    BasePage pageJokerVideo;

//    BasePage pageMe;

    BasePage currentPage;


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

        pageJokerText = new PageJoke(this, null, com.wangzy.joker.constants.Constant.JOKETYPE.JOKE_TYPE_TEXT);
        relativeLayoutContent.addView(pageJokerText.getRootView(), lpp);
        this.basePages.add(pageJokerText);


        pageJokerImg = new PageJoke(this, null, com.wangzy.joker.constants.Constant.JOKETYPE.JOKE_TYPE_TEXT_IMG, Constant.JOKETYPE.JOKE_TYPE_TEXT_GIF);
        relativeLayoutContent.addView(pageJokerImg.getRootView(), lpp);
        this.basePages.add(pageJokerImg);

        pageJokerVideo = new PageJoke(this, null, Constant.JOKETYPE.JOKE_TYPE_MP4);
        relativeLayoutContent.addView(pageJokerVideo.getRootView(), lpp);
        this.basePages.add(pageJokerVideo);

//        pageMe = new PageMe(this);
//        relativeLayoutContent.addView(pageMe.getRootView(), lpp);
//        this.basePages.add(pageMe);


        tabLayout.addTabItem(findLinearLayout(R.id.linearLayooutTextJoke));
        tabLayout.addTabItem(findLinearLayout(R.id.linearLayooutImgJoke));
//        tabLayout.addTabItem(findLinearLayout(R.id.linearLayooutGifJoke));
        tabLayout.addTabItem(findLinearLayout(R.id.linearLayooutVideoJoke));

//        tabLayout.addTabItem(findLinearLayout(R.id.linearLayooutMe));

        tabLayout.setOnTabItemClickListener(new OnTabItemClickListener() {
            @Override
            public void onCheckIndex(int index, int total, TabItem tabItem) {
                showIndexPage(index);
            }
        });

        checkIndex(0);

//        AppConnect.getInstance(this).showPopAd(this);
//
//        AppConnect.getInstance(this).showPopAd(this,new AppListener(){
//
//        });
//
//        AppConnect.getInstance(this).checkUpdate(this);


    }


    private static boolean mBackKeyPressed = false;//记录是否有首次按键

    @Override
    public void onBackPressed() {
        if (!mBackKeyPressed) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录

                public void run() {
                    mBackKeyPressed = false;
                }

            }, 2000);
        } else {//退出程序

            this.finish();
            System.exit(0);
        }
    }


    private void checkIndex(int index) {

        tabLayout.checkByIndex(index);
    }


    private void showIndexPage(int index) {
        for (int i = 0, isize = ListUtiles.getListSize(this.basePages); i < isize; i++) {
            if (i == index) {
                basePages.get(i).getRootView().setVisibility(View.VISIBLE);
                currentPage = basePages.get(i);
            } else {
                basePages.get(i).getRootView().setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != currentPage) {
            currentPage.onActivityResult(requestCode, resultCode, data);
        }
    }
}
