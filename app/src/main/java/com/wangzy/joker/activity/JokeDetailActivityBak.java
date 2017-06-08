//package com.wangzy.joker.activity;
//
//import android.graphics.Bitmap;
//import android.graphics.PixelFormat;
//import android.graphics.drawable.Drawable;
//import android.media.AudioManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.text.Html;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.avos.avoscloud.AVFile;
//import com.avos.avoscloud.AVObject;
//import com.avos.avoscloud.AVUser;
//import com.common.BaseActivity;
//import com.common.util.LogUtil;
//import com.common.view.CircleImageView;
//import com.common.view.RecyleImageView;
//import com.facebook.drawee.backends.pipeline.Fresco;
//import com.facebook.drawee.interfaces.DraweeController;
//import com.facebook.drawee.view.SimpleDraweeView;
//import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Target;
//import com.wangzy.joker.App;
//import com.wangzy.joker.R;
//import com.wangzy.joker.constants.Constant;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import io.vov.vitamio.MediaPlayer;
//import io.vov.vitamio.Vitamio;
//import io.vov.vitamio.widget.CenterLayout;
//
//public class JokeDetailActivityBak extends BaseActivity implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback {
//
//
//    @BindView(R.id.circleImageViewHeader)
//    CircleImageView circleImageViewHeader;
//
//    @BindView(R.id.textViewAuthorName)
//    TextView textViewAuthorName;
//
//    @BindView(R.id.textViewJokerTitle)
//    TextView textViewJokerTitle;
//
//    @BindView(R.id.textViewContent)
//    TextView textViewContent;
//
//    @BindView(R.id.imageViewImg)
//    RecyleImageView imageViewImg;
//
//    @BindView(R.id.relativeLayoutContents)
//    RelativeLayout relativeLayoutContents;
//
//    @BindView(R.id.textViewNiceCount)
//    TextView textViewNiceCount;
//
//    @BindView(R.id.imageViewParise)
//    ImageView imageViewParise;
//
//    @BindView(R.id.imageViewReport)
//    ImageView imageViewReport;
//
//    @BindView(R.id.linearLayoutOperate)
//    RelativeLayout linearLayoutOperate;
//
//    @BindView(R.id.relativeLayoutContent)
//    RelativeLayout relativeLayoutContent;
//
//
//    @BindView(R.id.simpleDrawView)
//    SimpleDraweeView simpleDrawView;
//
//
//    //    @BindView(R.id.surface)
//    SurfaceView mPreview;
//
//    @BindView(R.id.centerLayout)
//    CenterLayout centerLayout;
//
//    private int mVideoWidth;
//    private int mVideoHeight;
//
//    private MediaPlayer mMediaPlayer;
//    private SurfaceHolder holder;
//    private boolean mIsVideoSizeKnown = false;
//    private boolean mIsVideoReadyToBePlayed = false;
//
//
//    public static AVObject jokeObject;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        Vitamio.isInitialized(getApplicationContext());
//        setContentView(R.layout.activity_joke_detail);
//        ButterKnife.bind(this);
//
////        centerLayout=(CenterLayout) findViewById(R.id.centerLayout);
//
//
//        initView();
//    }
//
//    @Override
//    public void initView() {
//
//
//        String type = jokeObject.getString("type");
//        AVUser author = jokeObject.getAVUser("author");
//        AVFile fileHeader = author.getAVFile("avatar");
//
//        simpleDrawView.setAspectRatio(1.33f);
//
//
//        if (null != fileHeader) {
//            String hader = fileHeader.getThumbnailUrl(true, 35, 35);
//            Picasso.with(App.getApp()).load(hader).
//                    placeholder(R.drawable.bg_default_avatar).
//                    resize(35, 35).
//                    centerCrop().
//                    into(circleImageViewHeader);
//        }
//
//        textViewAuthorName.setText(author.getUsername());
//
//        textViewJokerTitle.setText(jokeObject.getString("title"));
//
//        imageViewParise.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                jokeObject.increment("nice");
//                jokeObject.setFetchWhenSave(true);
//                jokeObject.saveEventually();
//
//                textViewNiceCount.setText(String.valueOf(jokeObject.getInt("nice") + 1));
//
//            }
//        });
//        imageViewReport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
//
//        textViewNiceCount.setText(String.valueOf(jokeObject.getInt("nice")));
//
//        switch (type) {
//            case Constant.JOKETYPE.JOKE_TYPE_TEXT: {
//                textViewContent.setVisibility(View.VISIBLE);
//                textViewContent.setText(Html.fromHtml(jokeObject.getString("textContent")));
////                holder.imageViewTypeIcon.setImageResource(R.drawable.icon_type_text);
//
//                imageViewImg.setVisibility(View.GONE);
//            }
//            break;
//            case Constant.JOKETYPE.JOKE_TYPE_TEXT_IMG: {
//                String url = jokeObject.getString("imageUrl");
//                showGifOrImage(url, simpleDrawView, imageViewImg);
//            }
//
//
//            break;
//
//            case Constant.JOKETYPE.JOKE_TYPE_TEXT_GIF: {
//                String url = jokeObject.getString("imageUrl");
//                showGifOrImage(url, simpleDrawView, imageViewImg);
//
//            }
//
//            break;
//
//            case Constant.JOKETYPE.JOKE_TYPE_MP4: {
////                centerLayout.setVisibility(View.VISIBLE);
//                textViewJokerTitle.append("(视频)");
//                centerLayout.setVisibility(View.VISIBLE);
//
//
//                mPreview = (SurfaceView) findViewById(R.id.surface);
//                holder = mPreview.getHolder();
//                holder.addCallback(this);
//                holder.setFormat(PixelFormat.RGBA_8888);
//
//
////                imageViewImg.setVisibility(View.VISIBLE);
////                displayImage(url, imageViewImg);
////                webViewVideo.loadUrl(jokeObject.getString("videoUrl"));
//
//            }
//            break;
//        }
//
//
//    }
//
//
//    private void playVideo() {
//        doCleanUp();
//        try {
//
//            // Create a new media player and set the listeners
//            mMediaPlayer = new MediaPlayer(this);
//
//            String playUrl = jokeObject.getAVFile("videoFile").getUrl();
//            mMediaPlayer.setDataSource(playUrl);
//            mMediaPlayer.setDisplay(holder);
//            mMediaPlayer.prepareAsync();
//            mMediaPlayer.setOnBufferingUpdateListener(this);
//            mMediaPlayer.setOnCompletionListener(this);
//            mMediaPlayer.setOnPreparedListener(this);
//            mMediaPlayer.setOnVideoSizeChangedListener(this);
//            setVolumeControlStream(AudioManager.STREAM_MUSIC);
//
//        } catch (Exception e) {
//            LogUtil.e(App.tag, "play error:" + e.getMessage());
//        }
//    }
//
//    private void showGifOrImage(String url, SimpleDraweeView simpleDrawView, final ImageView imageViewImg) {
//
//        if (url.endsWith("gif")) {
//
//
//            simpleDrawView.setVisibility(View.VISIBLE);
//            textViewJokerTitle.append("(gif)");
//
//            DraweeController draweeController =
//                    Fresco.newDraweeControllerBuilder()
//                            .setUri(Uri.parse(url))
//                            .setAutoPlayAnimations(true) // 设置加载图片完成后是否直接进行播放
//                            .build();
//
//            simpleDrawView.setController(draweeController);
//
//        } else {
//            textViewJokerTitle.append("(图片)");
//            displayImage(url, imageViewImg);
//        }
//    }
//
//
//    public void onBufferingUpdate(MediaPlayer arg0, int percent) {
//         LogUtil.e(App.tag, "onBufferingUpdate percent:" + percent);
//
//    }
//
//    public void onCompletion(MediaPlayer arg0) {
//        LogUtil.e(App.tag, "onBufferingUpdate percent:onCompletion" );
//
//    }
//
//    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//        if (width == 0 || height == 0) {
//            return;
//        }
//        mIsVideoSizeKnown = true;
//        mVideoWidth = width;
//        mVideoHeight = height;
//        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
//            startVideoPlayback();
//        }
//    }
//
//    public void onPrepared(MediaPlayer mediaplayer) {
//        mIsVideoReadyToBePlayed = true;
//        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
//            startVideoPlayback();
//        }
//    }
//
//    public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
//        LogUtil.e(App.tag,"surfaceChanged");
//    }
//
//    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
//        LogUtil.e(App.tag,"surfaceDestroyed");
//    }
//
//    public void surfaceCreated(SurfaceHolder holder) {
//        playVideo();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        releaseMediaPlayer();
//        doCleanUp();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        releaseMediaPlayer();
//        doCleanUp();
//    }
//
//    private void releaseMediaPlayer() {
//        if (mMediaPlayer != null) {
//            mMediaPlayer.release();
//            mMediaPlayer = null;
//        }
//    }
//
//    private void doCleanUp() {
//        mVideoWidth = 0;
//        mVideoHeight = 0;
//        mIsVideoReadyToBePlayed = false;
//        mIsVideoSizeKnown = false;
//    }
//
//    private void startVideoPlayback() {
//        holder.setFixedSize(mVideoWidth, mVideoHeight);
//        mMediaPlayer.start();
//    }
//
//    private void displayImage(String url, final ImageView imageView) {
//
//        if (url.endsWith("gif")) {
//
//
//            return;
//        }
//
//
//        Target nTarget = new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//
//                int width = bitmap.getWidth();
//                int height = bitmap.getHeight();
//
//                int scw = point.x;
//                int sch = point.y;
//
//                int nh = (int) (height * (scw * 1.0f / width));
//
//                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(scw, nh);
//
//                imageView.setImageBitmap(bitmap);
//                imageView.setLayoutParams(layoutParams);
//
//            }
//
//            @Override
//            public void onBitmapFailed(Drawable errorDrawable) {
//
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//            }
//        };
//
//        imageView.setTag(nTarget);
//        imageView.setVisibility(View.VISIBLE);
//
//        Picasso.with(this).
//                load(url).
//                config(Bitmap.Config.RGB_565).
//                into(nTarget);
//    }
//}
