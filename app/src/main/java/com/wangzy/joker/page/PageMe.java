package com.wangzy.joker.page;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.common.util.DialogCallBackListener;
import com.common.util.DialogUtils;
import com.common.util.MyAnimationUtils;
import com.common.util.StringUtils;
import com.common.util.Tool;
import com.common.view.BasePage;
import com.common.view.CircleImageView;
import com.squareup.picasso.Picasso;
import com.wangzy.joker.R;
import com.wangzy.joker.activity.BaseJokeActivity;
import com.wangzy.joker.activity.EditPersonInfoActivity;
import com.wangzy.joker.activity.RegisterActivity;
import com.wangzy.joker.activity.UpdateLoadJokeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangzy on 2017/6/1.
 */

public class PageMe extends BasePage {

    @BindView(R.id.circleImageViewHeader)
    CircleImageView circleImageViewHeader;
    @BindView(R.id.textViewUserName)
    TextView textViewUserName;
    @BindView(R.id.viewLoginCenter)
    LinearLayout viewLoginCenter;
    @BindView(R.id.buttonLogin)
    Button buttonLogin;
    @BindView(R.id.editUserName)
    EditText editUserName;
    @BindView(R.id.editTextPassword)
    EditText editTextPassword;
    @BindView(R.id.viewLoginContent)
    RelativeLayout viewLoginContent;
    @BindView(R.id.textViewPostCount)
    TextView textViewPostCount;

    @BindView(R.id.textViewEditInfo)
    TextView textViewEditInfo;


    @BindView(R.id.textViewEmail)
    TextView textViewEmail;

    @BindView(R.id.textViewUpdateJoke)
    TextView textViewUpdateJoke;

    @BindView(R.id.viewMyJokes)
    RelativeLayout viewMyJokes;


    short GO_REGISTER = 100;
    short GO_EDIT = 101;
    short GO_UPLOAD = 102;


    public PageMe(Activity activity) {
        super(activity);
        initView();
    }

    @Override
    public void initView() {
        rootView = layoutInflater.inflate(R.layout.page_me, null);
        ButterKnife.bind(this, rootView);


        textViewEditInfo.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        textViewUpdateJoke.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);


        if (null == AVUser.getCurrentUser()) {
            viewLoginContent.setVisibility(View.VISIBLE);

        } else {
            viewLoginContent.setVisibility(View.GONE);
            initUserView();
        }

    }

    @OnClick(R.id.buttonRegister)
    public void onCLickRegister() {
        Tool.startActivityForResult(activity, RegisterActivity.class, GO_REGISTER);
    }


    private void initUserView() {

        final AVUser user = AVUser.getCurrentUser();
        if (null == user) {
            return;
        }

        textViewUserName.setText(user.getUsername());
        textViewEmail.setText(user.getEmail());


        user.fetchIfNeededInBackground("avatar", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (null == e) {


                    AVFile headerFile = user.getAVFile("avatar");

                    if (null != headerFile) {
                        String url = headerFile.getUrl();
                        Picasso.with(activity).load(url).placeholder(R.drawable.bg_default_avatar).into(circleImageViewHeader);
                    }
                }

            }
        });


        AVQuery<AVObject> jokerQuery = new AVQuery<>("Joke");

        jokerQuery.whereEqualTo("author", user);

        jokerQuery.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                textViewPostCount.setText(String.valueOf(i));
            }
        });


        PageJoke pageJoke = new PageJoke(activity, user, "");

        RelativeLayout.LayoutParams lpp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewMyJokes.addView(pageJoke.getRootView(), lpp);

    }

    @OnClick(R.id.textViewForgetPwd)
    public void onCLickForgetPwd() {

        String email = getInput(editUserName);
        if (StringUtils.isEmpty(email)) {
            editUserName.startAnimation(((BaseJokeActivity) activity).animationShake);
            return;
        } else {

            AVUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {

                        Tool.ToastShow(activity, "重置密码邮件发送成功，请进入你的邮箱查看！");

                    } else {
                        e.printStackTrace();
                    }
                }
            });
        }


    }


    @OnClick(R.id.textViewEditInfo)
    public void onClickEditInfo() {
        Tool.startActivityForResult(activity, EditPersonInfoActivity.class, GO_EDIT);
    }

    @OnClick(R.id.textViewUpdateJoke)
    public void onClickUpLoad() {

        Tool.startActivityForResult(activity, UpdateLoadJokeActivity.class, GO_UPLOAD);
    }

    @OnClick(R.id.viewFeedBack)
    public void onFedBackClick() {
        FeedbackAgent agent = new FeedbackAgent(activity);
        agent.startDefaultThreadActivity();

    }

    @OnClick(R.id.viewLogOut)
    public void onClickLogout() {


        DialogUtils.showConfirmDialog(activity, "", "确认退出", "确认", "取消", new DialogCallBackListener() {
            @Override
            public void onDone(boolean yesOrNo) {

                if (yesOrNo) {
                    if (null != AVUser.getCurrentUser()) {
                        AVUser.getCurrentUser().logOut();
                        viewLoginContent.setVisibility(View.VISIBLE);
                    }

                }
            }
        });


    }


    @OnClick(R.id.buttonLogin)
    public void onClickLogin() {

        if (!StringUtils.isEmpty(getInput(editUserName)) && !StringUtils.isEmpty(getInput(editTextPassword))) {

            ((BaseJokeActivity) activity).showProgressDialog(false);

            AVUser.logInInBackground(getInput(editUserName), getInput(editTextPassword), new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {

                    ((BaseJokeActivity) activity).hideProgressDialog();
                    if (null == e) {
                        initUserView();

                        MyAnimationUtils.animationHideview(viewLoginContent, AnimationUtils.loadAnimation(activity, R.anim.slide_bottom_out));
                    } else {

                        BaseJokeActivity.showAVException(activity, e);

//                        Tool.ToastShow(activity,avException.getMessage());
                    }

                }
            });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode == GO_REGISTER || (requestCode == GO_EDIT)) && resultCode == Activity.RESULT_OK) {

            if (null == AVUser.getCurrentUser()) {
                viewLoginContent.setVisibility(View.VISIBLE);

            } else {
                viewLoginContent.setVisibility(View.GONE);
                initUserView();
            }
        }


    }
}
