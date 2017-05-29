package com.common.cachu.lru;

import android.graphics.Bitmap;
import android.widget.ImageView;

public interface OnImageLoadListener {
	public void onImgLoad(ImageView imgView, Bitmap bitmap, String url);
}
