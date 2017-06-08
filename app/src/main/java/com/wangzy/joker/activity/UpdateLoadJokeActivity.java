package com.wangzy.joker.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.avos.avoscloud.AVACL;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.common.util.LogUtil;
import com.common.util.StringUtils;
import com.common.util.Tool;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.squareup.picasso.Picasso;
import com.wangzy.joker.App;
import com.wangzy.joker.R;
import com.wangzy.joker.constants.Constant;
import com.wangzy.joker.dialog.JokeTypeDialog;

import java.io.File;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateLoadJokeActivity extends BaseJokeActivity {


    @BindView(R.id.editTextTitle)
    EditText editTextTitle;
    @BindView(R.id.textViewType)
    TextView textViewType;
    @BindView(R.id.viewTypeContainer)
    LinearLayout viewTypeContainer;
    @BindView(R.id.editTextContent)
    EditText editTextContent;
    @BindView(R.id.viewTextContentInput)
    LinearLayout viewTextContentInput;
    @BindView(R.id.imageViewPicture)
    ImageView imageViewPicture;
    @BindView(R.id.viewImageConainer)
    LinearLayout viewImageConainer;
    @BindView(R.id.viewGifConainer)
    LinearLayout viewGifConainer;
    @BindView(R.id.videoView)
    VideoView videoView;
    @BindView(R.id.viewVideoConatainer)
    LinearLayout viewVideoConatainer;

    @BindView(R.id.simpleDrawView)
    SimpleDraweeView simpleDrawView;


    JokeTypeDialog jokeTypeDialog;

    @BindView(R.id.buttontSelectContent)
    Button buttontSelectContent;

    Set<String> imgSet = new HashSet<>();

    String curerntType = "text";
    String curentSelectPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);//初始化框架

        setContentView(R.layout.activity_update_joke);
        ButterKnife.bind(this);


        imgSet.add(".png");
        imgSet.add(".jpeg");
        imgSet.add(".jpg");

        jokeTypeDialog = new JokeTypeDialog(this) {
            @Override
            public void onClickType(String type, String label) {
                textViewType.setText(label);
                textViewType.setTag(type);

                switch (type) {
                    case Constant.JOKETYPE.JOKE_TYPE_TEXT:
                        curerntType = Constant.JOKETYPE.JOKE_TYPE_TEXT;

                        viewGifConainer.setVisibility(View.GONE);
                        viewTextContentInput.setVisibility(View.VISIBLE);
                        viewVideoConatainer.setVisibility(View.GONE);
                        viewImageConainer.setVisibility(View.GONE);
                        buttontSelectContent.setVisibility(View.GONE);

                        if (videoView.isPlaying()) {
                            videoView.pause();
                        }
                        break;
                    case Constant.JOKETYPE.JOKE_TYPE_TEXT_IMG:

                        curerntType = Constant.JOKETYPE.JOKE_TYPE_TEXT_IMG;

                        viewGifConainer.setVisibility(View.VISIBLE);
                        viewTextContentInput.setVisibility(View.GONE);
                        viewVideoConatainer.setVisibility(View.GONE);
                        viewImageConainer.setVisibility(View.VISIBLE);
                        buttontSelectContent.setVisibility(View.VISIBLE);

                        buttontSelectContent.setText("点击选择图片或GIF");

                        if (videoView.isPlaying()) {
                            videoView.pause();
                        }

                        break;
                    case Constant.JOKETYPE.JOKE_TYPE_MP4:
                        curerntType = Constant.JOKETYPE.JOKE_TYPE_MP4;

                        buttontSelectContent.setText("点击选择MP4");
                        viewGifConainer.setVisibility(View.GONE);
                        viewTextContentInput.setVisibility(View.GONE);
                        viewVideoConatainer.setVisibility(View.GONE);
                        viewImageConainer.setVisibility(View.GONE);
                        videoView.setVisibility(View.VISIBLE);
                        buttontSelectContent.setVisibility(View.VISIBLE);

                        if (!StringUtils.isEmpty(curentSelectPath) && curentSelectPath.endsWith(".mp4")) {

                            playUri(curentSelectPath);

                        }

                        break;

                }

            }
        };

        simpleDrawView.setAspectRatio(1.33f);

    }


    @OnClick(R.id.buttonUpload)
    public void onClickUpload() {

        if (App.getApp().isCommitJoke==false) {
            Tool.ToastShow(UpdateLoadJokeActivity.this, "暂时不能提交!");
            return;
        }


        String title = getInput(editTextTitle);

        if (StringUtils.isEmpty(title)) {
            editTextTitle.startAnimation(animationShake);
            return;
        }

        String type = curerntType;
        if (StringUtils.isEmpty(getInput(textViewType))) {
            textViewType.startAnimation(animationShake);
            return;
        }

        String textContent = getInput(editTextContent);
        if (Constant.JOKETYPE.JOKE_TYPE_TEXT.equals(type) && StringUtils.isEmpty(textContent)) {
            editTextContent.startAnimation(animationShake);
            return;
        }

        if (!Constant.JOKETYPE.JOKE_TYPE_TEXT.equals(type) && StringUtils.isEmpty(curentSelectPath)) {

            Tool.ToastShow(this, "没有选择文件。");
            return;
        }


        LogUtil.i(App.tag, "title:" + title);
        LogUtil.i(App.tag, "type:" + type);
        LogUtil.i(App.tag, "path:" + curentSelectPath);
        LogUtil.i(App.tag, "textContent:" + textContent);

        try {

            AVObject newJoke = new AVObject("Joke");
            newJoke.put("title", title);

            if (curerntType.equals(Constant.JOKETYPE.JOKE_TYPE_TEXT)) {

                newJoke.put("textContent", textContent);
                newJoke.put("type", Constant.JOKETYPE.JOKE_TYPE_TEXT);

            } else if (curerntType.equals(Constant.JOKETYPE.JOKE_TYPE_MP4)) {

                File dstFile = new File(curentSelectPath);
                if (dstFile.length() > 1024 * 1024 * 35) {
                    Tool.ToastShow(UpdateLoadJokeActivity.this, "文件太大!");

                    return;
                } else {
                    newJoke.put("type", curentSelectPath);
                    AVFile mp4File = AVFile.withAbsoluteLocalPath(curentSelectPath, curentSelectPath);
                    newJoke.put("videoFile", mp4File);

                }

            } else if (!StringUtils.isEmpty(curentSelectPath)) {

                if (curentSelectPath.endsWith(".gif")) {
                    newJoke.put("type", Constant.JOKETYPE.JOKE_TYPE_TEXT_GIF);
                } else {
                    newJoke.put("type", Constant.JOKETYPE.JOKE_TYPE_TEXT_IMG);
                }

                File dstFile = new File(curentSelectPath);
                if (dstFile.length() > 1024 * 1024 * 5) {
                    Tool.ToastShow(UpdateLoadJokeActivity.this, "文件太大!");

                    return;
                } else {

                    AVFile avFile = AVFile.withAbsoluteLocalPath(curentSelectPath, curentSelectPath);
                    newJoke.put("imageFile", avFile);
                }

            }

            AVACL avacl = new AVACL();
            avacl.setPublicReadAccess(true);
            avacl.setPublicWriteAccess(true);
            newJoke.setACL(avacl);

            Random random = new Random();
            int rint = random.nextInt(1000);

            newJoke.put("initNiceCount", rint);
            newJoke.put("nice", 0);
            newJoke.put("author", AVUser.getCurrentUser());

            showProgressDialog(false);

            newJoke.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    hideProgressDialog();

                    if (null == e) {

                        Tool.ToastShow(UpdateLoadJokeActivity.this, "上传成功!");

                    } else {
                        showAVException(UpdateLoadJokeActivity.this, e);
                    }

                }
            });

        } catch (Exception e) {
            Tool.ToastShow(UpdateLoadJokeActivity.this, "错误请重试.");
        }

    }

    @OnClick(R.id.buttontSelectContent)
    public void onClickSelectContent() {

        String pattrn = "";

        switch (curerntType) {
            case Constant.JOKETYPE.JOKE_TYPE_TEXT_IMG:

                pattrn = ".*\\.png$|.*\\.jpg$|.*\\.jpeg$|.*\\.gif$";
                break;
            case Constant.JOKETYPE.JOKE_TYPE_MP4:
                curerntType = Constant.JOKETYPE.JOKE_TYPE_MP4;
                pattrn = ".*\\.mp4$";
                break;

        }


        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(FILE_PICKER_REQUEST_CODE)
                .withHiddenFiles(true)
                .withFilter(Pattern.compile(pattrn))
                .start();


    }

    @OnClick(R.id.viewTypeContainer)
    public void onClickType() {

        jokeTypeDialog.show();

    }

    @Override
    public void onBackPressed() {
        if (null != jokeTypeDialog && jokeTypeDialog.isShowing()) {
            jokeTypeDialog.dismiss();
            return;
        }
        finish();
    }


    @Override
    protected void onPause() {
        super.onPause();

        videoView.pause();
    }


    private void playUri(String path) {

        viewVideoConatainer.setVisibility(View.VISIBLE);
        videoView.setVisibility(View.VISIBLE);
        videoView.setVideoURI(Uri.parse(path));
        videoView.setMediaController(new android.widget.MediaController(this));
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                return true;
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                hideProgressDialog();
            }
        });


        videoView.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String path = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            curentSelectPath = path;


            if (!StringUtils.isEmpty(path)) {

                if (path.toLowerCase().endsWith(".gif")) {

                    DraweeController draweeController =
                            Fresco.newDraweeControllerBuilder()
//                                    .setUri(path)
                                    .setUri(Uri.fromFile(new File(path)))
                                    .setTapToRetryEnabled(true)
                                    .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                                    .build();

//                    simpleDrawView.setImageURI(Uri.fromFile(new File(path)));
                    simpleDrawView.setController(draweeController);

                } else if (path.toLowerCase().endsWith(".mp4")) {

                    playUri(path);

                    showProgressDialog(false);


                } else {
                    //this is picture


                    Picasso.with(App.getApp()).
                            load(new File(path)).
                            resize(300, 300).
                            config(Bitmap.Config.RGB_565).
                            centerInside().
                            into(imageViewPicture);

                    viewVideoConatainer.setVisibility(View.GONE);
                    viewVideoConatainer.setVisibility(View.GONE);
                    viewTextContentInput.setVisibility(View.GONE);
                }

            }

        }

    }
}
