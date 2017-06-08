package com.wangzy.joker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.RefreshCallback;
import com.avos.avoscloud.SaveCallback;
import com.common.util.LogUtil;
import com.common.util.StringUtils;
import com.common.util.Tool;
import com.common.view.CircleImageView;
import com.learnncode.mediachooser.activity.PictureChoseActivity;
import com.squareup.picasso.Picasso;
import com.wangzy.joker.App;
import com.wangzy.joker.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditPersonInfoActivity extends BaseJokeActivity {

    @BindView(R.id.circleImageViewHeader)
    CircleImageView circleImageViewHeader;
    @BindView(R.id.viewTop)
    View viewTop;

    @BindView(R.id.textViewUserName)
    EditText textViewUserName;

    @BindView(R.id.editTextPassword)
    EditText editTextPassword;

    @BindView(R.id.buttonSave)
    Button buttonSave;

    @BindView(R.id.textViewEmail)
    TextView textViewEmail;
    private File avatarFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person_info);
        ButterKnife.bind(this);

        initView();
    }


    @Override
    public void initView() {

        final AVUser user = AVUser.getCurrentUser();
        if (null == user) {
            return;
        }
        textViewUserName.setText(user.getUsername());
        textViewEmail.setText(String.valueOf(user.getEmail()));


        user.fetchIfNeededInBackground("avatar", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (null == e) {

                    AVFile headerFile = user.getAVFile("avatar");
                    if (null != headerFile) {
                        String url = headerFile.getUrl();
                        Picasso.with(EditPersonInfoActivity.this).load(url).placeholder(R.drawable.bg_default_avatar).into(circleImageViewHeader);
                    }

                }
            }
        });
    }


    @OnClick(R.id.buttonSave)
    public void onClickSaveUserInfo() {


        String userName = getInput(textViewUserName);
        if (!StringUtils.isEmpty(userName)) {
            AVUser.getCurrentUser().setUsername(userName);
        }

        if (!StringUtils.isEmpty(getInput(editTextPassword))) {
            AVUser.getCurrentUser().setPassword(getInput(editTextPassword));
        }

        if (null!=avatarFile && avatarFile.exists()) {

            AVFile avFile = null;
            try {
                avFile = AVFile.withAbsoluteLocalPath(avatarFile.getName(), avatarFile.getAbsolutePath());
                AVUser.getCurrentUser().put("avatar", avFile);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }


        final AVUser user = AVUser.getCurrentUser();
        user.setFetchWhenSave(true);
        showProgressDialog(false);


        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (null == e) {

                    user.refreshInBackground("avatar", new RefreshCallback<AVObject>() {
                        @Override
                        public void done(AVObject avObject, AVException e) {
                            hideProgressDialog();
                            if (null == e) {

                                setResult(RESULT_OK);
                                finish();
                            } else {

                                Tool.ToastShow(EditPersonInfoActivity.this, e.getMessage());
                            }

                        }
                    });


                } else {
                    Tool.ToastShow(EditPersonInfoActivity.this, e.getMessage());

                    hideProgressDialog();
                }
            }
        });


    }


    @OnClick(R.id.circleImageViewHeader)
    public void onClickHeader() {
        PictureChoseActivity.gotoSelectPicture(this, 1, "", 400, 400);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PictureChoseActivity.SHORT_REQUEST_MEDIAS && resultCode == Activity.RESULT_OK) {

            ArrayList<String> files = data.getStringArrayListExtra(PictureChoseActivity.keyResult);
            avatarFile = new File(files.get(0));
            if (avatarFile.exists()) {

                Picasso.with(this)
                        .load((avatarFile))
                        .error(R.drawable.bg_default_avatar)
                        .placeholder(R.drawable.bg_default_avatar)
                        .into(circleImageViewHeader);


//
//
//                try {
//
//                    AVFile avFile = AVFile.withFile(avatarFile.getName(), avatarFile);
//                    AVUser.getCurrentUser().put("avatar", avFile);
//                    AVUser.getCurrentUser().saveEventually();
//
//
//                    Picasso.with(this)
//                            .load((avatarFile))
//                            .error(R.drawable.bg_default_avatar)
//                            .placeholder(R.drawable.bg_default_avatar)
//                            .into(circleImageViewHeader);
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }


            } else {
                LogUtil.e(App.tag, "crop file not exit");
            }
        }

    }
}
