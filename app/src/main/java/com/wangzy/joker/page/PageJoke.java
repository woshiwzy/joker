package com.wangzy.joker.page;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.baoyz.widget.PullRefreshLayout;
import com.common.util.ListUtiles;
import com.common.util.LogUtil;
import com.common.util.Tool;
import com.common.view.BasePage;
import com.wangzy.joker.App;
import com.wangzy.joker.R;
import com.wangzy.joker.activity.JokeDetailActivity;
import com.wangzy.joker.adapter.JokeAdapter;
import com.wangzy.joker.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangzy on 2017/5/26.
 */

public class PageJoke extends BasePage {


    @BindView(R.id.recycleViewTextJoker)
    RecyclerView recycleViewJoker;

    @BindView(R.id.swipeRefreshLayout)
    PullRefreshLayout swipeRefreshLayout;

    @BindView(R.id.viewLoadMore)
    View viewLoadMore;

    private final int PAGE_SIZE = 5;

    JokeAdapter jokeAdapter;
    String[] type = {};

    private AVUser authorUser;


    AVObject avObjectCurent;

    public PageJoke(Activity activity, AVUser authorUser, String... type) {
        super(activity);
        this.type = type;
        this.authorUser = authorUser;
        initView();
    }

    @Override
    public void initView() {
        rootView = layoutInflater.inflate(R.layout.page_text_joke, null);
        ButterKnife.bind(this, rootView);
        swipeRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh(false);
            }
        });


        this.jokeAdapter = new JokeAdapter(activity) {
            @Override
            public void onItemCLick(AVObject joke) {

                avObjectCurent = joke;
                JokeDetailActivity.jokeObject = joke;
                Tool.startActivityForResult(activity, JokeDetailActivity.class, SHORT_GO_DETAIL);
//                PlayVideoDemoActivity.joke=joke;
//                Tool.startActivity(activity,PlayVideoDemoActivity.class);

            }
        };


        recycleViewJoker.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                boolean isScrolBottom = isSlideToBottom(recyclerView);
                if (isScrolBottom) {

                    LogUtil.i(App.tag, "加载更多");
                    refresh(true);

                }
            }
        });


        recycleViewJoker.setAdapter(jokeAdapter);
        recycleViewJoker.setLayoutManager(new LinearLayoutManager(activity));
        recycleViewJoker.addItemDecoration(new RecycleViewDivider(activity, LinearLayoutManager.VERTICAL));


        refresh(false);


    }


    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    public void refresh(final boolean loadMore) {

        AVQuery<AVObject> jokerQuery = new AVQuery<>("Joke");


        jokerQuery.include("author");
        jokerQuery.include("author.avatar");
        jokerQuery.setLimit(PAGE_SIZE);
        jokerQuery.orderByDescending("createdAt");

        if (null != authorUser) {
            jokerQuery.whereEqualTo("author", authorUser);

            ArrayList<String> types = new ArrayList<>();

            types.add("text");
            types.add("text_gif");
            types.add("text_image");
            types.add("mp4");

            jokerQuery.whereContainedIn("type", types);
        } else {

            ArrayList<String> types = new ArrayList<>();
            for (String str : type) {
                if (str.equals("mp4")) {
                    jokerQuery.include("videoFile");
                }
                types.add(str);
            }

            jokerQuery.whereContainedIn("type", types);
        }

        if (loadMore) {
            jokerQuery.skip((jokeAdapter.getItemCount()));
            viewLoadMore.setVisibility(View.VISIBLE);
        } else {
            jokeAdapter.clearData();
        }

        jokerQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

                swipeRefreshLayout.setRefreshing(false);
                viewLoadMore.setVisibility(View.GONE);

                if (null == e) {

                    if (null == authorUser) {

                        if (!ListUtiles.isEmpty(list)) {
                            jokeAdapter.addMoreJokers(list);
                        }

                    } else if (!ListUtiles.isEmpty(list)) {
                        jokeAdapter.addMoreJokers(list);
                    }

                    if (ListUtiles.isEmpty(list) && loadMore) {

                        Tool.ToastShow(activity, "小编正在提交更多搞笑内容，骚年莫急！");
                    }

                    LogUtil.i(App.tag, "new size:" + jokeAdapter.getItemCount());
                } else {
                    LogUtil.e(App.tag, "请求报错");
                }

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SHORT_GO_DETAIL && resultCode == activity.RESULT_OK) {
            if (null != avObjectCurent) {
                avObjectCurent.fetchInBackground(new GetCallback<AVObject>() {
                    @Override
                    public void done(AVObject avObject, AVException e) {
                        if (null == e) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    jokeAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
            }

        }

    }
}
