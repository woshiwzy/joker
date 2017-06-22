package com.wangzy.joker.activity;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.VideoView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.common.util.StringUtils;
import com.common.util.Tool;
import com.common.view.CircleImageView;
import com.common.view.RecyleImageView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.wangzy.joker.App;
import com.wangzy.joker.R;
import com.wangzy.joker.constants.Constant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JokeDetailActivity extends BaseJokeActivity {


    @BindView(R.id.circleImageViewHeader)
    CircleImageView circleImageViewHeader;

    @BindView(R.id.textViewAuthorName)
    TextView textViewAuthorName;

    @BindView(R.id.textViewJokerTitle)
    TextView textViewJokerTitle;

    @BindView(R.id.textViewContent)
    TextView textViewContent;

    @BindView(R.id.imageViewImg)
    RecyleImageView imageViewImg;

    @BindView(R.id.relativeLayoutContents)
    RelativeLayout relativeLayoutContents;

    @BindView(R.id.textViewNiceCount)
    TextView textViewNiceCount;

    @BindView(R.id.imageViewParise)
    ImageView imageViewParise;

    @BindView(R.id.imageViewReport)
    ImageView imageViewReport;

    @BindView(R.id.linearLayoutOperate)
    RelativeLayout linearLayoutOperate;

    @BindView(R.id.relativeLayoutContent)
    RelativeLayout relativeLayoutContent;

    @BindView(R.id.simpleDrawView)
    SimpleDraweeView simpleDrawView;


    @BindView(R.id.scrollView)
    ScrollView scrollView;


    @BindView(R.id.videoView)
    VideoView videoViewAndroid;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    public static AVObject jokeObject;
    public boolean needBackRefresh = false;
    public static float aspectGif = 1.0f;
    @BindView(R.id.textViewSend)
    TextView textViewSend;
    @BindView(R.id.editTextCommitContent)
    EditText editTextCommitContent;
    @BindView(R.id.textViewCommentCounts)
    TextView textViewCommentCounts;

    @BindView(R.id.header)
    View header;

    @BindView(R.id.linearLayoutTitleContent)
    View linearLayoutTitleContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_joke_detail);
        ButterKnife.bind(this);
//      centerLayout=(CenterLayout) findViewById(R.id.centerLayout);

        initView();
    }


    @Override
    public void onBackPressed() {
        setResult(needBackRefresh ? RESULT_OK : RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.textViewCommentCounts)
    public void onCLickCounts() {

        CommentsActivity.joke = jokeObject;

        Tool.startActivity(this, CommentsActivity.class);
    }

    @OnClick(R.id.imageViewShare)
    public void onClickShare() {

        shareAvObject(jokeObject);
    }


    @OnClick(R.id.linearLayoutPairse)
    public void onCLickParise() {
        if (null != jokeObject) {

            jokeObject.increment("nice");
            jokeObject.setFetchWhenSave(true);
            jokeObject.saveEventually();
//                    textViewNiceCount.setText(String.valueOf(jokeObject.getInt("nice")));

            textViewNiceCount.setText(String.valueOf(jokeObject.getInt("nice") + jokeObject.getInt("initNiceCount")));

            needBackRefresh = true;

        }
    }

    @Override
    public void initView() {

        String type = jokeObject.getString("type");
        AVUser author = jokeObject.getAVUser("author");
        AVFile fileHeader = author.getAVFile("avatar");
        simpleDrawView.setAspectRatio(aspectGif);

        if (null != fileHeader) {
            String hader = fileHeader.getThumbnailUrl(true, 35, 35);
            Picasso.with(App.getApp()).load(hader).
                    placeholder(R.drawable.bg_default_avatar).
                    resize(35, 35).
                    centerCrop().
                    into(circleImageViewHeader);
        }

        textViewAuthorName.setText(author.getUsername());
        textViewJokerTitle.setText(jokeObject.getString("title"));


        String result = (jokeObject.getInt("commentCount") > 0 ? " " + jokeObject.getInt("commentCount") + " " : "");

        textViewCommentCounts.setText(result);

//        imageViewParise.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (null != jokeObject) {
//
//                    jokeObject.increment("nice");
//                    jokeObject.setFetchWhenSave(true);
//                    jokeObject.saveEventually();
////                    textViewNiceCount.setText(String.valueOf(jokeObject.getInt("nice")));
//
//                    textViewNiceCount.setText(String.valueOf(jokeObject.getInt("nice") + jokeObject.getInt("initNiceCount")));
//
//                    needBackRefresh = true;
//
//                }
//
//
//            }
//        });


        imageViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        textViewNiceCount.setText(String.valueOf(jokeObject.getInt("nice") + jokeObject.getInt("initNiceCount")));

        switch (type) {
            case Constant.JOKETYPE.JOKE_TYPE_TEXT: {
                textViewContent.setVisibility(View.VISIBLE);
                textViewContent.setText(Html.fromHtml(jokeObject.getString("textContent")));
//                holder.imageViewTypeIcon.setImageResource(R.drawable.icon_type_text);

                imageViewImg.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
            break;
            case Constant.JOKETYPE.JOKE_TYPE_TEXT_IMG: {
                String url = jokeObject.getString("imageUrl");
                showGifOrImage(url, simpleDrawView, imageViewImg);
            }

            break;

            case Constant.JOKETYPE.JOKE_TYPE_TEXT_GIF: {
                String url = jokeObject.getString("imageUrl");
                if (StringUtils.isEmpty(url)) {
                    String resultFileUrl = jokeObject.getAVFile("imageFile").getUrl();
                    showGifOrImage(resultFileUrl, simpleDrawView, imageViewImg);
                } else {
                    url = url.replace("\n", "");
                    showGifOrImage(url, simpleDrawView, imageViewImg);
                }

            }

            break;

            case Constant.JOKETYPE.JOKE_TYPE_MP4: {
//              centerLayout.setVisibility(View.VISIBLE);
                textViewJokerTitle.append("(视频)");
                playVideo(jokeObject.getAVFile("videoFile").getUrl(), true);
//                imageViewImg.setVisibility(View.VISIBLE);
//                displayImage(url, imageViewImg);
//                webViewVideo.loadUrl(jokeObject.getString("videoUrl"));
            }
            break;
        }

//        AppConnect.getInstance(this).setAdBackColor(Color.WHITE); //设置迷你广告广告诧颜色 AppConnect.getInstance(this).setAdForeColor(Color.YELLOW); //若未设置以上两个颜色，则默认为黑底白字
//        LinearLayout miniLayout =(LinearLayout)findViewById(R.id.viewContainer);
//        AppConnect.getInstance(this).showMiniAd(this, miniLayout, 10); //默认 10 秒切换一次广告
    }


    @OnClick(R.id.textViewSend)
    public void onClickSend() {

        if (null == AVUser.getCurrentUser()) {
            Tool.ToastShow(this, "请先登录！");
            return;
        }


        String commentText = getInput(editTextCommitContent);
        if (StringUtils.isEmpty(commentText)) {
            editTextCommitContent.startAnimation(animationShake);
            return;
        }


        AVObject comment = new AVObject("Comment");

        comment.put("commentContent", commentText);
        comment.put("Joke", jokeObject);
        comment.put("author", AVUser.getCurrentUser());


        showProgressDialog(false);

        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {

                hideProgressDialog();

                if (null == e) {

                    editTextCommitContent.setText("");
                    Tool.ToastShow(JokeDetailActivity.this, "评论成功！");

                    int oldCount = 0;
                    try {
                        String count = getInput(textViewCommentCounts).trim();
                        oldCount = Integer.parseInt(count);
                    } catch (Exception ex) {

                    } finally {
                        oldCount++;
                    }
                    textViewCommentCounts.setText(" " + String.valueOf(oldCount) + " ");


                    jokeObject.increment("commentCount");
                    jokeObject.saveEventually();

                } else {
                    showAVException(JokeDetailActivity.this, e);
                }

            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int ori = newConfig.orientation; //获取屏幕方向

        if (ori == Configuration.ORIENTATION_LANDSCAPE) {

            header.setVisibility(View.GONE);
            linearLayoutOperate.setVisibility(View.GONE);
            linearLayoutTitleContent.setVisibility(View.GONE);


        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            header.setVisibility(View.VISIBLE);
            linearLayoutOperate.setVisibility(View.VISIBLE);
            linearLayoutTitleContent.setVisibility(View.VISIBLE);


        }


    }


    private boolean useSystem = true;

    void playVideo(String videoUrl, boolean useSYstem) {
        if (useSYstem) {

            videoViewAndroid.setVisibility(View.VISIBLE);
            videoViewAndroid.setVideoURI(Uri.parse(videoUrl));
            videoViewAndroid.setMediaController(new MediaController(this));
            videoViewAndroid.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {


                    return true;
                }
            });

            videoViewAndroid.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                }
            });


            videoViewAndroid.start();

        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (null != videoViewAndroid) {
            if (videoViewAndroid.isPlaying()) {
                videoViewAndroid.stopPlayback();
            }
        }
    }

    private void showGifOrImage(String url, SimpleDraweeView simpleDrawView, final ImageView imageViewImg) {

        if (url.endsWith("gif")) {


            simpleDrawView.setVisibility(View.VISIBLE);
            textViewJokerTitle.append("(gif)");

            DraweeController draweeController =
                    Fresco.newDraweeControllerBuilder()
                            .setUri(Uri.parse(url))
                            .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
                            .build();

            simpleDrawView.setController(draweeController);

        } else {
            textViewJokerTitle.append("(图片)");
            displayImage(url, imageViewImg);
        }
    }


    private void displayImage(String url, final ImageView imageView) {

        if (url.endsWith("gif")) {


            return;
        }


        Target nTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                int scw = point.x;
                int sch = point.y;

                int nh = (int) (height * (scw * 1.0f / width));

                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(scw, nh);

                imageView.setImageBitmap(bitmap);
                imageView.setLayoutParams(layoutParams);

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        imageView.setTag(nTarget);
        imageView.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.VISIBLE);

        Picasso.with(this).
                load(url).
                config(Bitmap.Config.RGB_565).
                into(nTarget);
    }
}
