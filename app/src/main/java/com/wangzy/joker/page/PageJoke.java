package com.wangzy.joker.page;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.common.util.LogUtil;
import com.common.view.BasePage;
import com.wangzy.joker.App;
import com.wangzy.joker.R;
import com.wangzy.joker.adapter.JokeAdapter;
import com.wangzy.joker.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangzy on 2017/5/26.
 */

public class PageJoke extends BasePage {


    @BindView(R.id.recycleViewTextJoker)
    RecyclerView recycleViewTextJoker;

    JokeAdapter jokeAdapter;

    String[] type = {};

    public PageJoke(Activity activity, String... type) {
        super(activity);
        this.type = type;
        initView();
    }

    @Override
    public void initView() {
        rootView = layoutInflater.inflate(R.layout.page_text_joke, null);
        ButterKnife.bind(this, rootView);

        this.jokeAdapter = new JokeAdapter(activity);
        recycleViewTextJoker.setAdapter(jokeAdapter);
        recycleViewTextJoker.setLayoutManager(new LinearLayoutManager(activity));

        recycleViewTextJoker.addItemDecoration(new RecycleViewDivider(activity, LinearLayoutManager.VERTICAL));
        AVQuery<AVObject> jokerQuery = new AVQuery<>("Joke");

        ArrayList<String> types = new ArrayList<>();
        for (String str : type) {
            types.add(str);
        }

        jokerQuery.whereContainedIn("type", types);

        jokerQuery.include("author");
        jokerQuery.include("author.avatar");

        jokerQuery.setLimit(10);
        jokerQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

                if (null == e) {
                    jokeAdapter.addMoreJokers(list);
                } else {

                    LogUtil.e(App.tag, "请求报错");
                }

            }
        });


    }
}
