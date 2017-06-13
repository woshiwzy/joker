package com.wangzy.joker.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.wangzy.joker.R;
import com.wangzy.joker.adapter.CommentsAdapter;
import com.wangzy.joker.view.RecycleViewDivider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.waps.AppConnect;

public class CommentsActivity extends BaseJokeActivity {


    public static AVObject joke;


    @BindView(R.id.recycleViewComments)
    RecyclerView recycleViewComments;

    CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {

        commentsAdapter = new CommentsAdapter(this) {
            @Override
            public void onItemCLick(AVObject joke) {

            }
        };

        recycleViewComments.setAdapter(commentsAdapter);
        recycleViewComments.setLayoutManager(new LinearLayoutManager(this));
//      recycleViewComments.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.HORIZONTAL));


        showProgressDialog(false);

        AVQuery<AVObject> comemnts = new AVQuery("Comment");
        comemnts.whereEqualTo("Joke", joke);
        comemnts.include("author");
        comemnts.include("author.avatar");

        comemnts.setLimit(100);
        comemnts.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                hideProgressDialog();
                if (null == e) {
                    commentsAdapter.addComments(list);
                } else {
                    showAVException(CommentsActivity.this, e);
                }
            }
        });

        AppConnect.getInstance(this).setAdBackColor(R.color.bg_color); //设置迷你广告广告诧颜色 AppConnect.getInstance(this).setAdForeColor(Color.YELLOW); //若未设置以上两个颜色，则默认为黑底白字
        LinearLayout miniLayout =(LinearLayout)findViewById(R.id.viewAd);
        AppConnect.getInstance(this).showMiniAd(this, miniLayout, 10); //默认 10 秒切换一次广告

    }


}
