
package com.wangzy.joker.activity;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.avos.avoscloud.AVObject;
import com.wangzy.joker.App;
import com.wangzy.joker.R;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;
import io.vov.vitamio.Vitamio;

public class PlayVideoDemoActivity extends Activity implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener, SurfaceHolder.Callback {

    private static final String TAG = App.tag;
    private int mVideoWidth;
    private int mVideoHeight;

    private MediaPlayer mMediaPlayer;
    private SurfaceView mPreview;
    private SurfaceHolder holder;
    private String path;

    private boolean mIsVideoSizeKnown = false;
    private boolean mIsVideoReadyToBePlayed = false;

    public static AVObject joke;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Vitamio.isInitialized(getApplicationContext());
        setContentView(R.layout.play_video);
        mPreview = (SurfaceView) findViewById(R.id.surface);
        holder = mPreview.getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.RGBA_8888);

    }

    private void playVideo() {
        doCleanUp();
        try {
            path = joke.getAVFile("videoFile").getUrl();
            // Create a new media player and set the listeners
            mMediaPlayer = new MediaPlayer(this);
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            setVolumeControlStream(AudioManager.STREAM_MUSIC);

        } catch (Exception e) {
            Log.e(TAG, "error: " + e.getMessage(), e);
        }
    }

    public void onBufferingUpdate(MediaPlayer arg0, int percent) {
        // Log.d(TAG, "onBufferingUpdate percent:" + percent);

    }

    public void onCompletion(MediaPlayer arg0) {
        Log.d(TAG, "onCompletion called");
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Log.v(TAG, "onVideoSizeChanged called");
        if (width == 0 || height == 0) {
            Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
            return;
        }
        mIsVideoSizeKnown = true;
        mVideoWidth = width;
        mVideoHeight = height;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    public void onPrepared(MediaPlayer mediaplayer) {
        Log.d(TAG, "onPrepared called");
        mIsVideoReadyToBePlayed = true;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
        Log.d(TAG, "surfaceChanged called");

    }

    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
        Log.d(TAG, "surfaceDestroyed called");
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated called");
        playVideo();

    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
        doCleanUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
        doCleanUp();
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void doCleanUp() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;
    }

    private void startVideoPlayback() {
        Log.v(TAG, "startVideoPlayback");
        holder.setFixedSize(mVideoWidth, mVideoHeight);
        mMediaPlayer.start();
    }
}
