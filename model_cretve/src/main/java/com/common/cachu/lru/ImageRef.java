package com.common.cachu.lru;

import java.io.Serializable;

import android.widget.ImageView;

/**
 * 存放图片信息
 */
class ImageRef implements Serializable {

	/** 图片对应ImageView控件 */
	ImageView imageView;
	/** 图片URL地址 */
	String url;
	/** 图片缓存路径 */
	String filePath;
	/** 默认图资源ID */
	int resId;
	int width = 0;
	int height = 0;
	boolean isScaleWidth;
	boolean isRound;
	int cornor;

	OnImageLoadListener loadListener;

	/**
	 * 构造函数
	 * 
	 * @param imageView
	 * @param url
	 * @param resId
	 * @param filePath
	 */
	ImageRef(ImageView imageView, String url, String filePath, int resId) {
		this.imageView = imageView;
		this.url = url;
		this.filePath = filePath;
		this.resId = resId;
	}

	ImageRef(ImageView imageView, String url, String filePath, int resId, int cornor) {
		this.imageView = imageView;
		this.url = url;
		this.filePath = filePath;
		this.resId = resId;
		this.cornor = cornor;
	}

	ImageRef(ImageView imageView, String url, String filePath, int resId, int width, int height, int cornor) {
		this.imageView = imageView;
		this.url = url;
		this.filePath = filePath;
		this.width = width;
		this.height = height;
		this.resId = resId;
		this.cornor = cornor;
	}

	ImageRef(ImageView imageView, String url, String filePath, int resId, boolean isScaleWidth) {
		this.imageView = imageView;
		this.url = url;
		this.filePath = filePath;
		this.resId = resId;
		this.isScaleWidth = isScaleWidth;
	}

	ImageRef(ImageView imageView, String url, String filePath, int resId, int width, int height) {
		this.imageView = imageView;
		this.url = url;
		this.filePath = filePath;
		this.resId = resId;
		this.width = width;
		this.height = height;
	}

	public OnImageLoadListener getLoadListener() {
		return loadListener;
	}

	public void setLoadListener(OnImageLoadListener loadListener) {
		this.loadListener = loadListener;
	}

}