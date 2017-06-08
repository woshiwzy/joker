package com.wangzy.joker.picasso;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;
import java.util.HashMap;

public class VideoRequestHandler extends RequestHandler {

    public String SCHEME_VIDEO = "video";

    @Override
    public boolean canHandleRequest(Request data) {
        String scheme = data.uri.getScheme();
        return (SCHEME_VIDEO.equals(scheme));
    }

    @Override
    public Result load(Request data, int arg1) throws IOException {


        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        String uriText=data.uri.toString();

        String url = uriText.replace(SCHEME_VIDEO+":","");

        if (url.startsWith("http")) {
            retriever.setDataSource(url, new HashMap<String, String>());
        } else {
            retriever.setDataSource(url);
        }

        Bitmap bitmap = retriever.getFrameAtTime();


        return new Result(bitmap, Picasso.LoadedFrom.DISK);

//        String path = data.uri.toString().replace((SCHEME_VIDEO+":"),"");
//        Bitmap bm = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.MINI_KIND);
//        return new Result(bm, Picasso.LoadedFrom.DISK);
    }
}