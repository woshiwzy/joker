package com.wangzy.joker.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.common.BaseActivity;
import com.common.util.Tool;
import com.wangzy.joker.R;
import com.wangzy.joker.constants.Constant;
import com.yalantis.ucrop.UCrop;

import java.io.File;

/**
 * Created by wangzy on 2017/6/1.
 */

public class BaseJokeActivity extends BaseActivity {


    public Animation animationShake;
    public static final int REQUEST_PERMISSION = 1000;
    public static final int FILE_PICKER_REQUEST_CODE=1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            animationShake= AnimationUtils.loadAnimation(this, R.anim.shake);

    }


    public static void showAVException(Activity activity,Exception e){

        AVException avException=(AVException)e;

        JSONObject jsonObject=JSONObject.parseObject(avException.getMessage());


        Tool.ToastShow(activity,jsonObject.getString("error"));
    }

    /**
     * 裁剪模版函数
     */
    private void dostartCrop() {

        File f1 = new File("");
        String dst = "/sdcard" + File.separator + System.currentTimeMillis() + ".jpeg";
//                String dst = "/sdcard" + File.separator  + "x.jpeg";
        File f2 = new File(dst);


        UCrop.Options options = new UCrop.Options();

        options.setCircleDimmedLayer(false);

        options.setCompressionFormat(Bitmap.CompressFormat.PNG);
        options.setShowCropGrid(true);
        options.setShowCropFrame(true);
        options.setImageToCropBoundsAnimDuration(300);
        options.setActiveWidgetColor(Color.RED);
        options.setFreeStyleCropEnabled(false);
        options.setHideBottomControls(false);
        options.setLogoColor(Color.GRAY);
        options.setStatusBarColor(Color.BLUE);
        options.setToolbarColor(Color.YELLOW);
        options.setToolbarTitle("Test");
        options.setToolbarWidgetColor(Color.DKGRAY);

        UCrop.of(Uri.fromFile(f1), Uri.fromFile(f2))
                .withAspectRatio(9, 9)//比例
                .withMaxResultSize(200, 200)//输出大小
                .withOptions(options)
                .start(BaseJokeActivity.this);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION && grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            dostartCrop();
        }
    }


    public void startSelectCorpPicture(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                dostartCrop();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, REQUEST_PERMISSION);
            }
        } else {
            dostartCrop();
        }
    }



    public void shareAvObject(AVObject jokeObject) {

        if (null != jokeObject) {

            PackageInfo pkg = null;
            try {
                pkg = getPackageManager().getPackageInfo(getApplication().getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            String appName = pkg.applicationInfo.loadLabel(getPackageManager()).toString();

            String type = jokeObject.getString("type");
            String title = jokeObject.getString("title");

            switch (type) {

                case Constant.JOKETYPE.JOKE_TYPE_TEXT:
                    String content = "来自App[" + appName + "]的笑话\n" + jokeObject.getString("textContent");
                    Tool.sendShare(this, content);
                    break;
                case Constant.JOKETYPE.JOKE_TYPE_TEXT_IMG:
                case Constant.JOKETYPE.JOKE_TYPE_TEXT_GIF: {
                    String url = "来自App[" + appName + "]搞笑图片\n" + title + jokeObject.getString("imageUrl");
                    Tool.sendShare(this, url);
                }
                break;
                case Constant.JOKETYPE.JOKE_TYPE_MP4: {
                    String videoUrl = jokeObject.getAVFile("videoFile").getUrl();
                    String url = "来自App[" + appName + "]搞笑视频\n" + title + videoUrl;
                    Tool.sendShare(this, url);
                }
            }


        }
    }
}
